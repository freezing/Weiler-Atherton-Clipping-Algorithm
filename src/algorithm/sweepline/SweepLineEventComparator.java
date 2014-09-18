package algorithm.sweepline;

import java.util.Comparator;

import algorithm.PolygonSegment;

public class SweepLineEventComparator implements Comparator<PolygonSegment> {
  private static final double EPS = 1e-6;
  
  private SweepLine sweepLine;
  
  public SweepLineEventComparator(SweepLine sweepLine) {
    this.sweepLine = sweepLine;
  }
  
  @Override
  public int compare(PolygonSegment o1, PolygonSegment o2) {
 //   System.out.println("Status Line X = " + sweepLine.getStatusLineX());
  /*  return MathUtils.counterClockwiseComparison(o1.getLeftPoint(sweepLine.getStatusLineX()), 
        o1.getRightPoint(),
        o2.getLeftPoint(sweepLine.getStatusLineX()));*/
    double y1 = o1.getLeftPoint(sweepLine.getStatusLineX()).getY();
    double y2 = o2.getLeftPoint(sweepLine.getStatusLineX()).getY();
    if (Math.abs(y1 - y2) < EPS) {
      return 0;
    } else if (y1 > y2) {
      return 1;
    } else {
      return -1;
    }
  }
}
