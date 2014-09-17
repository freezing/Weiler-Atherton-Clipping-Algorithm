package algorithm.sweepline;

import algorithm.SegmentPoint;

public class SweepLineEvent {
  private SegmentPoint segmentPoint;
  
  public SweepLineEvent(SegmentPoint segmentPoint) {
    this.segmentPoint = segmentPoint;
  }

  
  public SegmentPoint getSegmentPoint() {
    return segmentPoint;
  }
  
}
