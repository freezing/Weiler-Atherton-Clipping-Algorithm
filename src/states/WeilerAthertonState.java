package states;

import java.util.ArrayList;
import java.util.List;

import algorithm.Polygon;
import algorithm.PolygonSegment;

public class WeilerAthertonState {
  List<Polygon> polygons;
  List<PolygonSegment> segments;
  
  public WeilerAthertonState() {
    this.polygons = new ArrayList<>();
    this.segments = new ArrayList<>();
  }

  public List<Polygon> getPolygons() {
    return polygons;
  }

  public void setPolygons(List<Polygon> polygons) {
    this.polygons = polygons;
  }

  public List<PolygonSegment> getSegments() {
    return segments;
  }

  public void setSegments(List<PolygonSegment> segments) {
    this.segments = segments;
  }
}
