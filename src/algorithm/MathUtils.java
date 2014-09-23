package algorithm;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class MathUtils {
  private static final double EPS = 1e-3;

  public static double vectorProduct(SegmentPoint A, SegmentPoint B,
      SegmentPoint C) {
    double x1 = B.x - A.x;
    double y1 = B.y - A.y;
    double x2 = C.x - A.x;
    double y2 = C.y - A.y;
    return vectorProduct(x1, y1, x2, y2);
  }
  
  public static int counterClockwiseComparison(SegmentPoint A, SegmentPoint B,
      SegmentPoint C) {
    double x1 = B.x - A.x;
    double y1 = B.y - A.y;
    double x2 = C.x - A.x;
    double y2 = C.y - A.y;
    double VP = vectorProduct(x1, y1, x2, y2);
    if (VP < -EPS) {
      return -1;
    } else if (VP > +EPS) {
      return 1;
    } else {
      return 0;
    }
  }

  private static double vectorProduct(double x1, double y1, double x2, double y2) {
    return x1 * y2 - y1 * x2;
  }

  public static SegmentPoint getIntersectionPoint(PolygonSegment segmentA, PolygonSegment segmentB) {
    Point2D leftPointA = segmentA.getLeftPoint();
    Point2D rightPointA = segmentA.getRightPoint();
    Point2D leftPointB = segmentB.getLeftPoint();
    Point2D rightPointB = segmentB.getRightPoint();

    // TODO(freezing): Create contructor for SegmentPoint which gets both segments as parameter
    Line2D lineA = new Line2D.Double(leftPointA, rightPointA);
    Line2D lineB = new Line2D.Double(leftPointB, rightPointB);
    Point2D.Float point = getIntersectionPoint(lineA, lineB);
    
    if (point == null) {
      return null;
    }
    SegmentPoint intersectionPoint = new SegmentPoint(point, segmentA, SegmentPointType.INTERSECTION);
    intersectionPoint.setPolygonSegments(segmentA, segmentB);
    return intersectionPoint;
  }

  private static Point2D.Float getIntersectionPoint(Line2D lineA, Line2D lineB) {
    if (!lineA.intersectsLine(lineB) ){ 
      return null;
    } else {
      double px = lineA.getX1(),
            py = lineA.getY1(),
            rx = lineA.getX2()-px,
            ry = lineA.getY2()-py;
      double qx = lineB.getX1(),
            qy = lineB.getY1(),
            sx = lineB.getX2()-qx,
            sy = lineB.getY2()-qy;
    
      double det = sx*ry - sy*rx;
      if (det == 0) {
        return null;
      } else {
        double z = (sx*(qy-py)+sy*(px-qx))/det;
        if (z==0 ||  z==1) return null;  // intersection at end point!
            return new Point2D.Float(
              (float)(px+z*rx), (float)(py+z*ry));
          }
     } // end intersection line-line
  }
}
