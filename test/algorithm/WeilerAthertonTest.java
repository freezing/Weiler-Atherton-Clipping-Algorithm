package algorithm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class WeilerAthertonTest {

  @Test
  public void test() {
    List<Point2D.Float> subjectVertices = new ArrayList<>();
    subjectVertices.add(new Point2D.Float(62, 125));
    subjectVertices.add(new Point2D.Float(390, 230));
    subjectVertices.add(new Point2D.Float(103, 390));
    
    List<Point2D.Float> clippingVertices = new ArrayList<>();
    clippingVertices.add(new Point2D.Float(168, 408));
    clippingVertices.add(new Point2D.Float(421, 337));
    clippingVertices.add(new Point2D.Float(175, 268));
    
    Polygon subjectPolygon = new Polygon(subjectVertices);
    subjectPolygon.setSubject(true);
    Polygon clippingPolygon = new Polygon(clippingVertices);
    
    WeilerAtherton weiler = new WeilerAtherton(subjectPolygon, clippingPolygon);
    weiler.execute();
    
    System.out.println("Weiler states = " + weiler.getStates().size());
  }
}
