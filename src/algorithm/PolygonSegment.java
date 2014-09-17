package algorithm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class PolygonSegment {
  private SegmentPoint startPoint;
  private SegmentPoint endPoint;
  private List<Point2D> intersectionPoints;
  
  public PolygonSegment(SegmentPoint startPoint, SegmentPoint endPoint) {
    this.startPoint = startPoint;
    this.endPoint = endPoint;
    this.intersectionPoints = new ArrayList<>();
  }
  
  public void addIntersectionPoint(Point2D point) {
    intersectionPoints.add(point);
  }
  
  public Point2D getStartPoint() {
    return startPoint;
  }
  
  public Point2D getEndPoint() {
    return endPoint;
  }
  
  public List<Point2D> getIntersectionPoints() {
    return intersectionPoints;
  }
  
  public SegmentPoint getLeftPoint() {
    if (startPoint.compareTo(endPoint) == -1) {
      return startPoint;
    } else {
      return endPoint;
    }
  }
  
  public SegmentPoint getRightPoint() {
    if (startPoint.compareTo(endPoint) == 1) {
      return endPoint;
    } else {
      return startPoint;
    }
  }
  
  public List<SegmentPoint> toSegmentPoints() {
    List<SegmentPoint> segmentPoints = new ArrayList<>();
    SegmentPointType startPointType = startPoint.compareTo(endPoint) == -1
        ? SegmentPointType.START
        : SegmentPointType.END;
    
    SegmentPointType endPointType = endPoint.compareTo(startPoint) == -1
        ? SegmentPointType.START
        : SegmentPointType.END;
    
    segmentPoints.add(new SegmentPoint(startPoint, this, startPointType));
    segmentPoints.add(new SegmentPoint(endPoint, this, endPointType));
    return segmentPoints;
  }

  public SegmentPoint getLeftPoint(Double statusLineX) {
    // TODO(freezing): If we have segment AB (A is left point) then return
    // A if statusLineX <= A.x. Otherwise, return the same SegmentPoint as A
    // just values are changed:
    // A.x = statusLineX
    // A.y = corresponding Y value on the segment
    return null;
  }
}
