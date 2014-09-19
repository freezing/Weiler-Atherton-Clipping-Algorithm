package states;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import algorithm.PolygonSegment;
import algorithm.SegmentPoint;

public class SweepLineState {
  private double statusLineX;
  private SegmentPoint processedPoint;
  private List<SegmentPoint> intersectionPoints;
  private List<PolygonSegment> currentSegments;
  private List<SegmentPoint> allIntersectionPoints;
  
  public SweepLineState() {
    allIntersectionPoints = new ArrayList<>();
    intersectionPoints = new ArrayList<>();
    currentSegments = new ArrayList<>();
  }
  
  public double getStatusLineX() {
    return statusLineX;
  }
  public void setStatusLineX(double statusLineX) {
    this.statusLineX = statusLineX;
  }
  public SegmentPoint getProcessedPoint() {
    return processedPoint;
  }
  public void setProcessedPoint(SegmentPoint processedPoint) {
    this.processedPoint = processedPoint;
  }
  public List<SegmentPoint> getIntersectionPoints() {
    return intersectionPoints;
  }
  public void setIntersectionPoints(List<SegmentPoint> intersectionPoints) {
    this.intersectionPoints = intersectionPoints;
  }
  public List<PolygonSegment> getCurrentSegments() {
    return currentSegments;
  }
  public void setCurrentSegments(List<PolygonSegment> currentSegments) {
    this.currentSegments = currentSegments;
  }
  
  public void addSegment(PolygonSegment segment) {
    currentSegments.add(segment);
  }
  
  public void setAllIntersectionPoints(List<SegmentPoint> allIntersectionPoints) {
    this.allIntersectionPoints = allIntersectionPoints;
  }
  
  public List<SegmentPoint> getAllIntersectionPoints() {
    return allIntersectionPoints;
  }
  
  public void addAllIntersectionPoints(Collection<SegmentPoint> points) {
    allIntersectionPoints.addAll(points);
  }
  
  public void addIntersectionPoint(SegmentPoint point) {
    intersectionPoints.add(point);
  }
}
