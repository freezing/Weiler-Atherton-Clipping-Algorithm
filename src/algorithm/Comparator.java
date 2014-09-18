package algorithm;

import java.awt.geom.Point2D;

public class Comparator {
  private static double EPS = 1e-6;
  
  public static int compare(Point2D a, Point2D b, double EPS) {
    if (a.getX() < b.getX() - EPS) {
      return -1;
    } else if (a.getX() > b.getX() + EPS) {
      return 1;
    } else if (a.getY() < b.getY() - EPS) {
      return -1;
    } else if (a.getY() > b.getY() + EPS) {
      return 1;
    } else {
      return 0;
    }
  }
  
  public static int compare(Point2D a, Point2D b) {
    return compare(a, b, EPS);
  }
}
