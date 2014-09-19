package algorithm;

import java.util.ArrayList;
import java.util.List;

import states.WeilerAthertonState;
import utils.Preconditions;
import algorithm.sweepline.SweepLine;

public class WeilerAtherton {
  
  private List<WeilerAthertonState> states;
  private SweepLine sweepLine;
  
  private Polygon subjectPolygon;
  private Polygon clippingPolygon;
  
  public WeilerAtherton(Polygon subjectPolygon, Polygon clippingPolygon) {
    sweepLine = new SweepLine();
    this.subjectPolygon = Preconditions.checkNotNull(subjectPolygon);
    this.clippingPolygon = Preconditions.checkNotNull(clippingPolygon);
  }
  
  public SweepLine getSweepLine() {
    return sweepLine;
  }
  
  public List<WeilerAthertonState> getStates() {
    return states;
  }

  public void execute() {
    states = new ArrayList<>();
    Pair<List<PolygonSegment>, List<PolygonSegment>> polygons =
        IntersectionFinder.findIntersectionPoints(subjectPolygon, clippingPolygon, sweepLine);
    // TODO: Implement Weiler-Atherton Clipping Algorithm
  }
}
