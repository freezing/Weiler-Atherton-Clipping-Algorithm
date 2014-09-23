package algorithm;

import java.awt.geom.Point2D;

public class SegmentPoint extends Point2D.Float implements Comparable<SegmentPoint> {
  private static final long serialVersionUID = 1L;
  
  private PolygonSegment polygonSegment;
  private PolygonSegment anotherPolygonSegment;
  private SegmentPointType type;
  private boolean isProcessed;
  
  public SegmentPoint(Point2D.Float point) {
    super(point.x, point.y);
    isProcessed = false;
  }
  
  public SegmentPoint(Point2D.Float point, PolygonSegment polygonSegment, SegmentPointType type) {
    super(point.x, point.y);
    isProcessed = false;
    this.type = type;
    this.polygonSegment = polygonSegment;
  }
  
  /**
   * Returns segment belonging to subject polygon.
   * @return
   */
  public PolygonSegment getPolygonSegment() {
    return polygonSegment;
  }
  
  public PolygonSegment getAnotherPolygonSegment() {
    return anotherPolygonSegment;
  }
  
  public SegmentPointType getType() {
    return type;
  }

  public boolean isProcessed() {
    return isProcessed;
  }

  public void setProcessed(boolean isProcessed) {
    this.isProcessed = isProcessed;
  }

  @Override
  public int compareTo(SegmentPoint o) {
    int cmp = Comparator.compare(this, o);
    if (cmp == 0) {
      if (type == o.getType()) {
        return 0;
      } else if (type == SegmentPointType.END){
        return -1;
      } else {
        return 1;
      }
    } else {
      return cmp;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    SegmentPoint other = (SegmentPoint) obj;
    return this.compareTo(other) == 0;
  }

  public void setPolygonSegments(PolygonSegment segmentA, PolygonSegment segmentB) {
    this.polygonSegment = segmentA;
    this.anotherPolygonSegment = segmentB;
  }

  public void setType(SegmentPointType type) {
    this.type = type;
  }
  
  @Override
  public String toString() {
    return type + ", (" + x + ", " + y + ")";
  }
}
