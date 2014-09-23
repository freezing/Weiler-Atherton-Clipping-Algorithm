package algorithm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import states.WeilerAthertonState;
import utils.Preconditions;
import algorithm.sweepline.SweepLine;

public class WeilerAtherton {
  
  private List<WeilerAthertonState> states;
  private SweepLine sweepLine;
  
  private Polygon subjectPolygon;
  private Polygon clippingPolygon;
  
  private List<Point2D> enteringPoints;
  private List<Point2D> exitingPoints;
  
  private List<Polygon> polygons;
  
  public WeilerAtherton(Polygon subjectPolygon, Polygon clippingPolygon) {
    sweepLine = new SweepLine();
    this.polygons = new ArrayList<>();
    this.subjectPolygon = Preconditions.checkNotNull(subjectPolygon);
    this.clippingPolygon = Preconditions.checkNotNull(clippingPolygon);
  }
  
  public List<Point2D> getEnteringPoints() {
    return enteringPoints;
  }
  
  public List<Point2D> getExitingPoints() {
    return exitingPoints;
  }
  
  public SweepLine getSweepLine() {
    return sweepLine;
  }
  
  public List<WeilerAthertonState> getStates() {
    return states;
  }

  public void execute() {
    // Sort vertices in subject polygon clockwise
    subjectPolygon.sortClockwise();
    
    // Sort vertices in clipping polygon counter-clockwise
    clippingPolygon.sortClockwise();
    
    states = new ArrayList<>();
    Pair<List<PolygonSegment>, List<PolygonSegment>> polygons =
        IntersectionFinder.findIntersectionPoints(subjectPolygon, clippingPolygon, sweepLine);
    
    // For each segment line sort intersection points by their distance from start point
    System.out.println("Intersection points (subject polygon):");
    for (PolygonSegment segment : polygons.getFirst()) {
      segment.sortIntersectionPoints();
      System.out.println(segment.intersectionPointsToString());
      System.out.println();
    }
    
    for (PolygonSegment segment : polygons.getSecond()) {
      segment.sortIntersectionPoints();
    }
    
    markPointsAsEnteringAndExiting(polygons.getFirst(),
        clippingPolygon.contains(subjectPolygon.getVertex(0)));
    
    this.enteringPoints = new ArrayList<>();
    this.exitingPoints = new ArrayList<>();
    Set<SegmentPoint> enteringPoints = new HashSet<>();
    for (PolygonSegment segment : polygons.getFirst()) {
      for (int i = 0; i < segment.getIntersectionPoints().size(); i++) {
        if (segment.getIntersectionPointType(i) == SegmentPointType.ENTERING) {
          enteringPoints.add(segment.getIntersectionPoints().get(i));
          this.enteringPoints.add(segment.getIntersectionPoints().get(i));
        } else if (segment.getIntersectionPointType(i) == SegmentPointType.EXITING) {
          this.exitingPoints.add(segment.getIntersectionPoints().get(i));
        }
      }
    }

    for (SegmentPoint enteringPoint : enteringPoints) {
      if (!enteringPoint.isProcessed()) {
        findNextPolygon(enteringPoint);
      }
    }
  }

  private void findNextPolygon(SegmentPoint enteringPoint) {
    List<Point2D.Float> vertices = new ArrayList<>(); 
    SegmentPoint lastPoint = enteringPoint;
    SegmentPoint nextPoint = null;

    vertices.add(lastPoint);
    while (true) {
      WeilerAthertonState state = new WeilerAthertonState();
      PolygonSegment segment = null;
      
      if (lastPoint.getType() == SegmentPointType.ENTERING) {
        // Go along subject polygon
        segment = lastPoint.getPolygonSegment();
      } else if (lastPoint.getType() == SegmentPointType.EXITING) {
        // Go along clipping polygon
        segment = lastPoint.getAnotherPolygonSegment();
      } else {
        // If it is polygon vertex take the only segment
        segment = lastPoint.getPolygonSegment();
      }

      lastPoint.setProcessed(true);
      nextPoint = segment.findNextPoint(lastPoint);
      if (nextPoint.isProcessed()) {
        break;
      }
      lastPoint = nextPoint;
      vertices.add(nextPoint);
      
      // Create WeilerAthertonState
      List<Polygon> currentPolygons = new ArrayList<>();
      for (Polygon polygon : polygons) {
        currentPolygons.add(polygon);
      }
      state.setPolygons(currentPolygons);
      state.setSegments(createSegments(vertices));
      states.add(state);
    }
    
    Polygon polygon = new Polygon(vertices);
    polygons.add(polygon);
    // Create state for displaying whole polygon
    WeilerAthertonState currState = new WeilerAthertonState();
    List<Polygon> currentPolygons = new ArrayList<>();
    for (Polygon p : polygons) {
      currentPolygons.add(p);
    }
    currState.setPolygons(currentPolygons);
    states.add(currState);
  }

  private List<PolygonSegment> createSegments(List<Point2D.Float> vertices) {
    List<PolygonSegment> segments = new ArrayList<>();
    for (int i = 0; i < vertices.size() - 1; i++) {
      SegmentPoint startPoint = new SegmentPoint(vertices.get(i));
      SegmentPoint endPoint = new SegmentPoint(vertices.get(i + 1));
      segments.add(new PolygonSegment(startPoint, endPoint));
    }
    return segments;
  }

  private void markPointsAsEnteringAndExiting(List<PolygonSegment> subjectPolygon, boolean isNextExiting) {
    for (int i = 0; i < subjectPolygon.size(); i++) {
      PolygonSegment segment = subjectPolygon.get(i);
      for (int j = 0; j < segment.getIntersectionPoints().size(); j++) {
        SegmentPointType type = getIntersectionType(isNextExiting);
        segment.setIntersectionType(j, type);
        segment.getIntersectionPoints().get(j).setType(type);
        isNextExiting = !isNextExiting;
      }
    }
  }

  private SegmentPointType getIntersectionType(boolean isNextExiting) {
    if (isNextExiting) {
      return SegmentPointType.EXITING;
    } else {
      return SegmentPointType.ENTERING;
    }
  }
}
