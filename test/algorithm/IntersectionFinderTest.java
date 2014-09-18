package algorithm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.junit.Test;

import algorithm.sweepline.SweepLine;

public class IntersectionFinderTest {

  @Test
  public void test() {
    // Create subject polygon vertices
    List<Point2D.Float> subjectVertices = new ArrayList<>();
    subjectVertices.add(new Point2D.Float(1.56f, 4.76f));
    subjectVertices.add(new Point2D.Float(6.00f, 4.00f));
    subjectVertices.add(new Point2D.Float(5.80f, 0.90f));
    subjectVertices.add(new Point2D.Float(0.82f, 0.66f));
    subjectVertices.add(new Point2D.Float(0.40f, 4.04f));
    subjectVertices.add(new Point2D.Float(0.80f, 6.08f));
    
    // Create clipping polygon vertices
    List<Point2D.Float> clippingVertices = new ArrayList<>();
    clippingVertices.add(new Point2D.Float(5.26f, 5.68f));
    clippingVertices.add(new Point2D.Float(2.06f, 3.52f));
    clippingVertices.add(new Point2D.Float(2.60f, -0.72f));
    clippingVertices.add(new Point2D.Float(3.26f, 1.64f));
    clippingVertices.add(new Point2D.Float(3.92f, -1.46f));
    clippingVertices.add(new Point2D.Float(5.28f, 2.56f));
    clippingVertices.add(new Point2D.Float(7.68f, 2.68f));
    clippingVertices.add(new Point2D.Float(7.26f, 5.10f));
    
    SweepLine sweepLine = new SweepLine();
    Polygon subjectPolygon = new Polygon(subjectVertices);
    Polygon clippingPolygon = new Polygon(clippingVertices);
    Pair<List<PolygonSegment>, List<PolygonSegment>> polygons =
        IntersectionFinder.findIntersectionPoints(subjectPolygon, clippingPolygon, sweepLine);
    
    System.out.println("Subject polygon intersections:");
    for (PolygonSegment segment : polygons.getFirst()) {
      for (Point2D point : segment.getIntersectionPoints()) {
        System.out.println(point);
      }
    }
    
    System.out.println();
    System.out.println("Clipping polygon intersections:");
    for (PolygonSegment segment : polygons.getSecond()) {
      for (Point2D point : segment.getIntersectionPoints()) {
        System.out.println(point);
      }
    }
  }
  
  @Test
  public void test_treeset() {
    TreeSet<Integer> tree = new TreeSet<>();
    tree.add(3);
    tree.add(5);
    tree.add(10);
    
    System.out.println("Next should be null:");
    System.out.println(tree.higher(10));
    System.out.println(tree.higher(11));
    
    System.out.println();
    System.out.println("Next should be 10:");
    System.out.println(tree.higher(5));
    System.out.println(tree.higher(6));
    
    System.out.println();
    System.out.println("Next should be 5:");
    System.out.println(tree.higher(3));
    System.out.println(tree.higher(4));
    
    System.out.println();
    System.out.println("Next should be 3:");
    System.out.println(tree.higher(2));
    System.out.println(tree.higher(1));
    
    System.out.println("Next should be 5:");
    System.out.println(tree.lower(6));
    System.out.println(tree.lower(9));
    System.out.println(tree.lower(10));
    
    System.out.println();
    System.out.println("Next should be 3:");
    System.out.println(tree.lower(5));
    System.out.println(tree.lower(4));
    
    System.out.println();
    System.out.println("Next should be null:");
    System.out.println(tree.lower(3));
    System.out.println(tree.lower(2));
  }
  
  @Test
  public void test_complex() {
 // Create subject polygon vertices
    List<Point2D.Float> subjectVertices = new ArrayList<>();
    subjectVertices.add(new Point2D.Float(1.10f, 4.80f));
    subjectVertices.add(new Point2D.Float(5.34f, 4.24f));
    subjectVertices.add(new Point2D.Float(2.38f, 3.32f));
    subjectVertices.add(new Point2D.Float(5.80f, 2.84f));
    subjectVertices.add(new Point2D.Float(3.76f, 2.34f));
    subjectVertices.add(new Point2D.Float(6.08f, 2.08f));
    subjectVertices.add(new Point2D.Float(1.66f, 1.62f));
    subjectVertices.add(new Point2D.Float(6.66f, 0.96f));
    subjectVertices.add(new Point2D.Float(1.84f, 0.66f));
    subjectVertices.add(new Point2D.Float(0.48f, 1.80f));
    subjectVertices.add(new Point2D.Float(1.54f, 2.30f));
    subjectVertices.add(new Point2D.Float(2.38f, 2.28f));
    subjectVertices.add(new Point2D.Float(2.78f, 2.76f));
    subjectVertices.add(new Point2D.Float(0.52f, 2.56f));
    subjectVertices.add(new Point2D.Float(2.16f, 4.06f));
    subjectVertices.add(new Point2D.Float(0.50f, 4.02f));
    subjectVertices.add(new Point2D.Float(0.70f, 5.72f));
    
    // Create clipping polygon vertices
    List<Point2D.Float> clippingVertices = new ArrayList<>();
    clippingVertices.add(new Point2D.Float(5.02f, 0.44f));
    clippingVertices.add(new Point2D.Float(4.26f, 5.44f));
    clippingVertices.add(new Point2D.Float(1.72f, 4.40f));
    clippingVertices.add(new Point2D.Float(3.00f, 4.30f));
    clippingVertices.add(new Point2D.Float(3.06f, 2.32f));
    
    SweepLine sweepLine = new SweepLine();
    Polygon subjectPolygon = new Polygon(subjectVertices);
    Polygon clippingPolygon = new Polygon(clippingVertices);
    Pair<List<PolygonSegment>, List<PolygonSegment>> polygons =
        IntersectionFinder.findIntersectionPoints(subjectPolygon, clippingPolygon, sweepLine);
    
    System.out.println("Subject polygon intersections:");
    for (PolygonSegment segment : polygons.getFirst()) {
      for (Point2D point : segment.getIntersectionPoints()) {
        System.out.println(point);
      }
    }
    
    System.out.println();
    System.out.println("Clipping polygon intersections:");
    for (PolygonSegment segment : polygons.getSecond()) {
      for (Point2D point : segment.getIntersectionPoints()) {
        System.out.println(point);
      }
    }
  }
  
  @Test
  public void test_createSegmentPoints() {
    // Create subject polygon vertices
    List<Point2D.Float> subjectVertices = new ArrayList<>();
    subjectVertices.add(new Point2D.Float(1.56f, 4.76f));
    subjectVertices.add(new Point2D.Float(6.00f, 4.00f));
    subjectVertices.add(new Point2D.Float(5.80f, 0.90f));
    subjectVertices.add(new Point2D.Float(0.82f, 0.66f));
    subjectVertices.add(new Point2D.Float(0.40f, 4.04f));
    subjectVertices.add(new Point2D.Float(0.80f, 6.08f));
    
    List<PolygonSegment> polygonSegments = new Polygon(subjectVertices).toSegments();
    List<SegmentPoint> segmentPoints = IntersectionFinder.createSegmentPoints(polygonSegments);
    
    System.out.println("Segment points:");
    for (SegmentPoint segmentPoint : segmentPoints) {
      System.out.println(segmentPoint);
    }
  }
}
