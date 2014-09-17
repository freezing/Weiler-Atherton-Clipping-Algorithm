package algorithm.sweepline;

import java.util.Comparator;

import algorithm.MathUtils;
import algorithm.PolygonSegment;

public class SweepLineEventComparator implements Comparator<PolygonSegment>{

  private Double statusLineX;
  
  public SweepLineEventComparator(Double statusLineX) {
    this.statusLineX = statusLineX;
  }
  
  @Override
  public int compare(PolygonSegment o1, PolygonSegment o2) {
    System.out.println("Status Line X = " + statusLineX);
    return MathUtils.counterClockwiseComparison(o1.getLeftPoint(statusLineX), 
        o1.getRightPoint(),
        o2.getLeftPoint(statusLineX));
  }
}
