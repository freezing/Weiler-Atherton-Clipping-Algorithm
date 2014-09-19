package algorithm;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import algorithm.sweepline.SweepLine;

public class Polygon {
  private static final double EPS = 1e-1;
  
  private List<Point2D.Float> vertices;
  private boolean isFinished;
  
  public Polygon() {
    vertices = new ArrayList<>();
  }
  
  public void addVertex(Point2D.Float vertex) {
    vertices.add(vertex);
  }
  
  public void replaceLastVertex(Point2D.Float vertex) {
    if (vertices.size() > 0) {
      vertices.set(vertices.size() - 1, vertex);
    }
  }
  
  public Polygon(List<Point2D.Float> vertices) {
    this.vertices = vertices;
  }
  
  public List<Point2D.Float> getVertices() {
    return vertices;
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
  
  public Path2D asPath2D() {
    Path2D path = new Path2D.Double();
    if (vertices.size() > 1) {
      Point2D current = vertices.get(0);
      path.moveTo(current.getX(), current.getY());
      
      for (int i = 1; i < vertices.size(); i++) {
        current = vertices.get(i);
        path.lineTo(current.getX(), current.getY());
      }
      
      if (isFinished) {
        path.closePath();
      }
      return path;
    }
    return path;
  }
  
  public List<PolygonSegment> toSegments(SweepLine sweepLine) {
    List<PolygonSegment> segments = toSegments();
    for (PolygonSegment segment : segments) {
      segment.setSweepLine(sweepLine);
    }
    return segments;
  }
  
  public void setFinished(boolean isFinished) {
    this.isFinished = isFinished;
  }
  
  public boolean isFinished() {
    return isFinished;
  }

  public boolean finishPolygon() {
    if (selfIntersects()) {
      return false;
    } else {
      setFinished(true);
      vertices.remove(vertices.size() - 1);
      fixVerticalLines();
      makeClockwise();
      return true;
    }
  }

  private void makeClockwise() {
    // TODO(freezing): If polygon is counter-clockwise, inverse vertices    
  }

  private void fixVerticalLines() {
    for (int i = 0; i < vertices.size(); i++) {
      int nextVertex = (i + 1) % vertices.size();
      // If two adjacent vertices have the same X coordinate (withing range EPS)
      if (Math.abs(vertices.get(i).getX() - vertices.get(nextVertex).getX()) < EPS) {
        vertices.get(i).x += 2 * EPS;
      }
    }
  }

  private boolean selfIntersects() {
    // TODO(freezing): Implement self intersection checking algorithm
    return false;
  }
}
