package algorithm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

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
      
    }
    return segments;
  }
}
