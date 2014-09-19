package algorithm.sweepline;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import states.SweepLineState;
import algorithm.MathUtils;
import algorithm.PolygonSegment;
import algorithm.SegmentPoint;
import algorithm.SegmentPointType;

public class SweepLine {
  private static final Double EPS = 1e-3;
  
  private List<SegmentPoint> segmentPoints;
  int nextSegmentPointToBeProcessed;
  private TreeSet<SegmentPoint> intersectionPoints;
  private TreeSet<SegmentPoint> allIntersectionPoints;
  
  private TreeSet<PolygonSegment> segments;
  private Double statusLineX;
  
  private boolean isFinished;
  
  private List<SweepLineState> states;
  private SweepLineState currentState;
  
  public SweepLine() {
    states = new ArrayList<>();
    isFinished = false;
    statusLineX = Double.NEGATIVE_INFINITY;
  }

  public List<SweepLineState> getStates() {
    return states;
  }
  
  public void setPoints(List<SegmentPoint> segmentPoints) {
    this.segmentPoints = segmentPoints;
    this.intersectionPoints = new TreeSet<>();
    this.allIntersectionPoints = new TreeSet<>();
    this.nextSegmentPointToBeProcessed = 0;
  //  this.segments = new TreeSet<>(new SweepLineEventComparator(this));
    this.segments = new TreeSet<>();    
  }

  public boolean isFinished() {
    return isFinished;
  }

  public void processNext() {  
    // Chose if next event should be an intersection point or segment point    
    SegmentPoint eventPoint = getNextPoint();

    System.out.println(eventPoint);
    if (eventPoint == null) {
      // If next event doesn't exist (ie. it is null), set finished flag to true
      // and exit
      isFinished = true;
      return;
    }
    
    currentState = new SweepLineState();
    currentState.setProcessedPoint(eventPoint);
    
    // Set status line x coordinate    
    PolygonSegment polygonSegment = eventPoint.getPolygonSegment();
    if (eventPoint.getType() == SegmentPointType.START) {
      statusLineX = eventPoint.getX() + 5 * EPS;
      // If it is a start point then we should insert new PolygonSegment
      if (!segments.add(polygonSegment)) {
        System.err.println("Polygon segment not added (" + polygonSegment + ") !!!!!!");
      }
      
      // Get neighboring polygon segments
      PolygonSegment lowerPolygonSegment = segments.lower(polygonSegment);
      PolygonSegment higherPolygonSegment = segments.higher(polygonSegment);
      
      // Check intersection and add intersection point to the segments if it exists
      checkAndAddIntersectionPoints(lowerPolygonSegment, polygonSegment);
      checkAndAddIntersectionPoints(polygonSegment, higherPolygonSegment);
    } else if (eventPoint.getType() == SegmentPointType.END){
      statusLineX = eventPoint.getX() - 5 * EPS;
      // If it is an ending point then we should get polygon segments that will be neighboring 
      // when this segment is removed
      PolygonSegment lowerPolygonSegment = segments.lower(polygonSegment);
      PolygonSegment higherPolygonSegment = segments.higher(polygonSegment);
      
      // Check intersection between new neighboring points and add intersection point
      // to the segments if it exists
      checkAndAddIntersectionPoints(lowerPolygonSegment, higherPolygonSegment);
      
      // Remove point 
      segments.remove(polygonSegment);
    } else {
      statusLineX = eventPoint.getX() + 5 * EPS;
      // If it is an intersection point swap the segments
      // (it is done by removing them and adding them again,
      // and it works because new status line x coordinate has been set)
      segments.remove(eventPoint.getPolygonSegment());
      segments.remove(eventPoint.getAnotherPolygonSegment());
      
      segments.add(eventPoint.getPolygonSegment());
      segments.add(eventPoint.getAnotherPolygonSegment());
      
      // Check intersections for both segments that are swapped
      PolygonSegment lowerPolygonSegment = segments.lower(eventPoint.getPolygonSegment());
      PolygonSegment higherPolygonSegment = segments.higher(eventPoint.getPolygonSegment());
      checkAndAddIntersectionPoints(lowerPolygonSegment, eventPoint.getPolygonSegment());
      checkAndAddIntersectionPoints(eventPoint.getPolygonSegment(), higherPolygonSegment);
      
      lowerPolygonSegment = segments.lower(eventPoint.getAnotherPolygonSegment());
      higherPolygonSegment = segments.higher(eventPoint.getAnotherPolygonSegment());
      checkAndAddIntersectionPoints(lowerPolygonSegment, eventPoint.getAnotherPolygonSegment());
      checkAndAddIntersectionPoints(eventPoint.getAnotherPolygonSegment(), higherPolygonSegment);
    }
    
    for (PolygonSegment segment : segments) {
      currentState.addSegment(segment);
    }
    
    currentState.addAllIntersectionPoints(allIntersectionPoints);
    currentState.setStatusLineX(statusLineX);
    states.add(currentState);
  }

  private SegmentPoint getNextPoint() {
    if (nextSegmentPointToBeProcessed >= segmentPoints.size()) {
      // If there aren't any points left
      return null;
    }

    SegmentPoint segmentPoint = segmentPoints.get(nextSegmentPointToBeProcessed);
    if (!intersectionPoints.isEmpty()) {
      SegmentPoint intersectionPoint = intersectionPoints.first();
      if (intersectionPoint.compareTo(segmentPoint) == -1) {
        // If intersection point is before next segment point
        // set current segmentPoint to next intersectionPoint
        intersectionPoints.remove(intersectionPoint);
        segmentPoint = intersectionPoint;
      } else {
        nextSegmentPointToBeProcessed++;
      }
    } else {
      nextSegmentPointToBeProcessed++;
    }
    return segmentPoint;
  }

  /**
   * Check if polygonSegments intersect and if they do
   * add intersection points to both of them.
   * Also add the intersection point to the intersectionPoints list
   * @param segmentA
   * @param segmentB
   */
  private void checkAndAddIntersectionPoints(PolygonSegment segmentA,
      PolygonSegment segmentB) {
    if (segmentA == null || segmentB == null) {
      // If at least one segment is null exit
      return;
    }
    
    SegmentPoint intersectionPoint = MathUtils.getIntersectionPoint(segmentA, segmentB);
    if (intersectionPoint == null) {
      return;
    }

    intersectionPoint.setPolygonSegments(segmentA, segmentB);
    intersectionPoint.setType(SegmentPointType.INTERSECTION);
    
    // Add intersection point to both segments
    segmentA.addIntersectionPoint(intersectionPoint);
    segmentB.addIntersectionPoint(intersectionPoint);
    
    if (!allIntersectionPoints.contains(intersectionPoint)) {
      // If intersection point doesn't exist already in the sweep line points
      // insert it
      intersectionPoints.add(intersectionPoint);
      allIntersectionPoints.add(intersectionPoint);
      currentState.addIntersectionPoint(intersectionPoint);
    }
  }

  public Double getStatusLineX() {
    return statusLineX;
  }
}
