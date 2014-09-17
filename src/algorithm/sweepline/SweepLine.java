package algorithm.sweepline;

import java.util.List;
import java.util.TreeSet;

import algorithm.MathUtils;
import algorithm.PolygonSegment;
import algorithm.SegmentPoint;
import algorithm.SegmentPointType;

public class SweepLine {
  private static final Double EPS = 1e-3;
  
  private List<SegmentPoint> segmentPoints;
  int nextSegmentPointToBeProcessed;
  private TreeSet<SegmentPoint> intersectionPoints;
  
  private TreeSet<PolygonSegment> segments;
  private Double statusLineX;
  
  private boolean isFinished;

  public void setPoints(List<SegmentPoint> segmentPoints) {
    this.segmentPoints = segmentPoints;
    this.intersectionPoints = new TreeSet<>();
    this.nextSegmentPointToBeProcessed = 0;
    this.segments = new TreeSet<>(new SweepLineEventComparator(statusLineX));
  }

  public boolean isFinished() {
    return isFinished;
  }

  public void processNext() {  
    // Chose if next event should be an intersection point or segment point    
    SegmentPoint eventPoint = getNextPoint();
    
    if (eventPoint == null) {
      // If next event doesn't exist (ie. it is null), set finished flag to true
      // and exit
      isFinished = true;
      return;
    }
    
    // Set status line x coordinate
    statusLineX = eventPoint.getX() + 2 * EPS;
    
    PolygonSegment polygonSegment = eventPoint.getPolygonSegment();
    if (eventPoint.getType() == SegmentPointType.START) {
      // If it is a start point then we should insert new PolygonSegment
      segments.add(polygonSegment);
      
      // Get neighboring polygon segments
      PolygonSegment lowerPolygonSegment = segments.lower(polygonSegment);
      PolygonSegment higherPolygonSegment = segments.higher(polygonSegment);
      
      // Check intersection and add intersection point to the segments if it exists
      checkAndAddIntersectionPoints(lowerPolygonSegment, polygonSegment);
      checkAndAddIntersectionPoints(polygonSegment, higherPolygonSegment);
    } else if (eventPoint.getType() == SegmentPointType.END){
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
      // If it is an intersection point swap the segments
      // (it is done by removing them and adding them again,
      // and it works because new status line x coordinate has been set)
      segments.remove(eventPoint.getPolygonSegment());
      segments.remove(eventPoint.getAnotherPolygonSegment());
      
      segments.add(eventPoint.getPolygonSegment());
      segments.add(eventPoint.getAnotherPolygonSegment());
    }
  }

  private SegmentPoint getNextPoint() {
    if (nextSegmentPointToBeProcessed >= segments.size()) {
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
    SegmentPoint intersectionPoint = MathUtils.getIntersectionPoint(segmentA, segmentB);
    intersectionPoint.setPolygonSegments(segmentA, segmentB);
    intersectionPoint.setType(SegmentPointType.INTERSECTION);
    
    // Add intersection point to both segments
    segmentA.addIntersectionPoint(intersectionPoint);
    segmentB.addIntersectionPoint(intersectionPoint);
    
    if (!intersectionPoints.contains(intersectionPoint)) {
      // If intersection point doesn't exist already in the sweep line points
      // insert it
      intersectionPoints.add(intersectionPoint);
    }
  }

}
