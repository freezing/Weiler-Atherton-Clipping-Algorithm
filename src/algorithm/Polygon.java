package algorithm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import algorithm.sweepline.SweepLine;

public class Polygon {
  private List<Point2D.Float> vertices;
  
  public Polygon(List<Point2D.Float> vertices) {
    this.vertices = vertices;
  }
  
  public Point2D.Float getVertex(int idx) {
    return vertices.get(idx);
  }
  
  public List<PolygonSegment> toSegments() {
    List<PolygonSegment> segments = new ArrayList<>();
    for (int i = 0; i < vertices.size(); i++) {
      SegmentPoint startPoint = new SegmentPoint(vertices.get(i));
      SegmentPoint endPoint = new SegmentPoint(vertices.get((i + 1) % vertices.size()));
      PolygonSegment polygonSegment = new PolygonSegment();
      polygonSegment.setStartPoint(startPoint);
      polygonSegment.setEndPoint(endPoint);
      segments.add(polygonSegment);
    }
    return segments;
  }
  
  public List<PolygonSegment> toSegments(SweepLine sweepLine) {
    List<PolygonSegment> segments = toSegments();
    for (PolygonSegment segment : segments) {
      segment.setSweepLine(sweepLine);
    }
    return segments;
  }
}
