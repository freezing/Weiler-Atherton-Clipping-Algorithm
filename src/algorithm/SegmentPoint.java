package algorithm;

import java.awt.geom.Point2D;

public class SegmentPoint extends Point2D.Float implements Comparable<SegmentPoint> {
  private static final long serialVersionUID = 1L;
  
  private PolygonSegment polygonSegment;
  private PolygonSegment anotherPolygonSegment;
  private SegmentPointType type;
  
  public SegmentPoint(Point2D.Float point, PolygonSegment polygonSegment, SegmentPointType type) {
    super(point.x, point.y);
    this.type = type;
    this.polygonSegment = polygonSegment;
  }
  
  public PolygonSegment getPolygonSegment() {
    return polygonSegment;
  }
  
  public PolygonSegment getAnotherPolygonSegment() {
    return anotherPolygonSegment;
  }
  
  public SegmentPointType getType() {
    return type;
  }

  @Override
  public int compareTo(SegmentPoint o) {
    return Comparator.compare(this, o);
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
}
