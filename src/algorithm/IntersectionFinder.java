package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import algorithm.sweepline.SweepLine;

public class IntersectionFinder {
  public static Pair<List<PolygonSegment>, List<PolygonSegment>> findIntersectionPoints(
      Polygon subjectPolygon,
      Polygon clippingPolygon,
      SweepLine sweepLine) {
    // Get polygon segments
    List<PolygonSegment> subjectPolygonSegments = subjectPolygon.toSegments(sweepLine);
    List<PolygonSegment> clippingPolygonSegments = clippingPolygon.toSegments(sweepLine);
    
    // Get end points of each polygon segments (marked as start and end points based on the status
    // line)
    List<SegmentPoint> subjectPolygonSegmentPoints = createSegmentPoints(subjectPolygonSegments);
    List<SegmentPoint> clippingPolygonSegmentPoints = createSegmentPoints(clippingPolygonSegments);
    
    // Merge together all segment points
    List<SegmentPoint> allSegmentPoints = subjectPolygonSegmentPoints;
    allSegmentPoints.addAll(clippingPolygonSegmentPoints);
    
    // Sort all segment points by x coordinate then by y (ASC both)
    Collections.sort(allSegmentPoints);
    
    // Do the sweep line algorithm
    sweepLine.setPoints(allSegmentPoints);
    while (!sweepLine.isFinished()) {
      sweepLine.processNext();
    }
    
    // Return polygons with intersection points
    return new Pair<>(subjectPolygonSegments, clippingPolygonSegments);    
  }

  protected static List<SegmentPoint> createSegmentPoints(List<PolygonSegment> polygonSegments) {
    List<SegmentPoint> polygonSegmentPoints = new ArrayList<>();
    for (PolygonSegment polygonSegment : polygonSegments) {
      List<SegmentPoint> segmentPoints = polygonSegment.toSegmentPoints();
      polygonSegmentPoints.addAll(segmentPoints);
    }
    return polygonSegmentPoints;
  }
}
