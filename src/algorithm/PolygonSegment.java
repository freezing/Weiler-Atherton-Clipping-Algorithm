package algorithm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import algorithm.sweepline.SweepLine;

public class PolygonSegment implements Comparable<PolygonSegment>{
  private static final Double EPS = 1e-3;
  private SweepLine sweepLine;

  private SegmentPoint startPoint;
  private SegmentPoint endPoint;
  private List<Point2D> intersectionPoints;
  
  public PolygonSegment() {
    this.intersectionPoints = new ArrayList<>();
  }
  
  public PolygonSegment(SegmentPoint startPoint, SegmentPoint endPoint) {
    this.startPoint = startPoint;
    this.endPoint = endPoint;
    this.intersectionPoints = new ArrayList<>();
  }
  
  public void addIntersectionPoint(Point2D point) {
    if (!intersectionPoints.contains(point)) {
      intersectionPoints.add(point);
    }
  }
  
  public void setSweepLine(SweepLine sweepLine) {
    this.sweepLine = sweepLine;
  }
  
  public Point2D getStartPoint() {
    return startPoint;
  }
  
  public Point2D getEndPoint() {
    return endPoint;
  }
  
  public void setStartPoint(SegmentPoint startPoint) {
    this.startPoint = startPoint;
    startPoint.setPolygonSegments(this, null);
  }
  
  public void setEndPoint(SegmentPoint endPoint) {
    this.endPoint = endPoint;
    endPoint.setPolygonSegments(this, null);
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
      return startPoint;
    } else {
      return endPoint;
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

  /**
   * 
   * If we have segment AB (A is left point) then return
   * A if statusLineX <= A.x. Otherwise, return the same SegmentPoint as A
   * just values are changed:
   * A.x = statusLineX
   * A.y = corresponding Y value on the segment
   * @param statusLineX
   * @return Point where statusLineX intersects with the segment [leftPoint, rightPoint]
   * or the leftPoint if it doesn't intersect with the segment.
   */
  public SegmentPoint getLeftPoint(Double statusLineX) {
    SegmentPoint leftPoint = getLeftPoint();
    if (statusLineX < leftPoint.x + EPS) {
      return leftPoint;
    } else {
      SegmentPoint rightPoint = getRightPoint();
      float A = rightPoint.y - leftPoint.y;
      float B = -(rightPoint.x - leftPoint.x);
      float C = leftPoint.y * rightPoint.x - leftPoint.x * rightPoint.y;
      
      if (Math.abs(A) < EPS) {
        // By + C = 0 -> y = -C/B
        float y = -C / B;
        float x = statusLineX.floatValue();
        SegmentPoint newLeftPoint = new SegmentPoint(
            new Point2D.Float(x, y), leftPoint.getPolygonSegment(), leftPoint.getType());
        return newLeftPoint;
      } else {
        // Ax + By + C = 0
        float x = statusLineX.floatValue();
        float y = (-C - A * x) / B;
        SegmentPoint newLeftPoint = new SegmentPoint(
            new Point2D.Float(x, y), leftPoint.getPolygonSegment(), leftPoint.getType());
        return newLeftPoint;
      }
    }
  }
  
  @Override
  public String toString() {
    /*String stringValue = "[(" + startPoint.getX() + ", " + startPoint.getY() + ")";
    for (Point2D point : intersectionPoints) {
      stringValue += ", (" + point.getX() + ", " + point.getY() + ")";
    }
    stringValue += ", (" + endPoint.getX() + ", " + endPoint.getY() + ")" + "]";*/
    String stringValue = "[" + startPoint;
    for (Point2D point : intersectionPoints) {
      stringValue += ", " + point;
    }
    stringValue += ", " + endPoint + "]";
    return stringValue;
  }

  @Override
  public int compareTo(PolygonSegment o) {
    double y1 = getLeftPoint(sweepLine.getStatusLineX()).getY();
    double y2 = o.getLeftPoint(sweepLine.getStatusLineX()).getY();
    if (Math.abs(y1 - y2) < EPS) {
      return 0;
    } else if (y1 > y2) {
      return 1;
    } else {
      return -1;
    }
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (obj instanceof PolygonSegment) {
      PolygonSegment other = (PolygonSegment) obj;
      return this.compareTo(other) == 0;
    } else {
      return false;
    }
  }
}
