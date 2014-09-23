package algorithm;

import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.List;

import algorithm.sweepline.SweepLine;

public class Polygon {
  private static final double EPS = 1e-1;
  
  private List<Point2D.Float> vertices;
  private boolean isFinished;
  private boolean isClockwise;
  private boolean isSubject;
  
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
  
  public double calculateArea() {
    double area = 0.0;
    SegmentPoint A = new SegmentPoint(vertices.get(0));
    for (int i = 1; i < vertices.size() - 1; i++) {
      SegmentPoint B = new SegmentPoint(vertices.get(i));
      SegmentPoint C = new SegmentPoint(vertices.get(i + 1));
      area += (Math.abs(MathUtils.vectorProduct(A, B, C)) / 2.0);
    }
    return area;
  }
  
  public boolean isSubject() {
    return isSubject;
  }
  
  public void setSubject(boolean isSubject) {
    this.isSubject = isSubject;
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
      polygonSegment.setSubject(isSubject);
      segments.add(polygonSegment);
    }
    
    for (int i = 0; i < segments.size(); i++) {
      int nextIdx = (i + 1) % segments.size();
      segments.get(i).setNextSegment(segments.get(nextIdx));
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
    if (checkSelfIntersection(vertices)) {
      return false;
    } else {
      setFinished(true);
      vertices.remove(vertices.size() - 1);
      fixVerticalLines();
      return true;
    }
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

  private boolean checkSelfIntersection(List<Float> verticesToCheck) {
    for (int i = 1; i < verticesToCheck.size(); i++) {
      // Skip adjacent segments
      for (int j = i + 2; j < verticesToCheck.size(); j++) {
        Line2D lineA = new Line2D.Float(verticesToCheck.get(i - 1), verticesToCheck.get(i));
        Line2D lineB = new Line2D.Float(verticesToCheck.get(j - 1), verticesToCheck.get(j));
        if (lineA.intersectsLine(lineB)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Checks if polygon self-intersects if the last vertex is currentPoint.
   * @param currentPoint
   * @return
   */
  public boolean canReplaceLastVertex(Float currentPoint) {
    List<Point2D.Float> verticesToCheck = new ArrayList<>();
    for (int i = 0; i < vertices.size() - 1; i++) {
      verticesToCheck.add(vertices.get(i));
    }
    verticesToCheck.add(currentPoint);
    return !checkSelfIntersection(verticesToCheck);
  }

  public void checkClockwise() {
    // Find most-left most-bottom point
    int mostLeft = 0;
    Point2D.Float mostLeftPoint = vertices.get(0);
    for (int i = 1; i < vertices.size(); i++) {
      if (mostLeftPoint.getX() > vertices.get(i).getX()
          || (Math.abs(mostLeftPoint.getX() - vertices.get(i).getX()) < EPS)
          && mostLeftPoint.getY() > vertices.get(i).getY()) {
        mostLeftPoint = vertices.get(i);
        mostLeft = i;
      }
    }
    
    Point2D.Float nextPoint = vertices.get( (mostLeft + 1) % vertices.size() );
    Point2D.Float prevPoint = vertices.get ( (mostLeft + vertices.size() - 1) % vertices.size() );
    
    isClockwise = false;
    double vp = MathUtils.vectorProduct(new SegmentPoint(prevPoint),
        new SegmentPoint(mostLeftPoint),
        new SegmentPoint(nextPoint));
    if (vp > 0.0) {
      isClockwise = true;
    }
  }
  
  public void sortClockwise() {
    checkClockwise();
    if (!isClockwise) {
      invertVertices();
    }
  }

  public void sortCounterClockwise() {
    checkClockwise();
    if (isClockwise) {
      invertVertices();
    }
  }

  private void invertVertices() {
    List<Point2D.Float> sortedVertices = new ArrayList<>();
    for (int i = vertices.size() - 1; i >= 0; i--) {
      sortedVertices.add(vertices.get(i));
    }
    vertices = sortedVertices;
  }

  /**
   * Checks if the given point is inside the polygon.
   * @param point
   * @return
   */
  public boolean contains(Point2D.Float point) {
    boolean oddIntersections = false;
    List<PolygonSegment> segments = toSegments();
    
    for (PolygonSegment segment : segments) {
        Point2D A = segment.getStartPoint();
        Point2D B = segment.getEndPoint();
        
        if (A.getY() < point.getY() && B.getY() > point.getY()
            || B.getY() < point.getY() && A.getY() > point.getY()) {
          if (A.getX() < point.getX() && B.getX() > point.getX()
              || B.getX() < point.getX() && A.getX() > point.getX()) {
            oddIntersections = !oddIntersections;
          }
        } 
    }
    return oddIntersections;
  }
}
