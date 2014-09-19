package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JRadioButton;

import states.SweepLineState;
import states.WeilerAthertonState;
import algorithm.Polygon;
import algorithm.PolygonSegment;
import algorithm.SegmentPoint;
import algorithm.WeilerAtherton;

public class Framework extends JPanel implements MouseListener, MouseMotionListener {
  private static final long serialVersionUID = 1L;

  private static final int LINE_STROKE = 2;
  
  private static final Color DARK_GREEN = new Color(0, 153, 51);
  
  private static final Color ENTERING_COLOR = Color.GREEN;
  private static final Color EXITING_COLOR = Color.RED;
  
  private static final Color SUBJECT_COLOR = new Color(255, 51, 0);
  private static final Color CLIPPING_COLOR = new Color(76, 0, 0);
  private static final Color INTERSECTION_POINTS_COLOR = Color.GREEN;
  private static final Color STATUS_LINE_COLOR = Color.GREEN;
  
  private static final int POINT_WIDTH = 8;
  private static final int POINT_HEIGHT = 8;
  private static final double GUI_CLOSE_POINTS_DISTANCE = 10;
  
  private JRadioButton firstPolygonRadio;
  private JRadioButton secondPolygonRadio;

  private Polygon subjectPolygon;
  private Polygon clippingPolygon;
  
  private int currentState;
  private List<SweepLineState> sweepLineStates;
  private List<WeilerAthertonState> weilerAthertonStates;
  
  public Framework(JRadioButton firstPolygonRadio, JRadioButton secondPolygonRadio) {
    this.firstPolygonRadio = firstPolygonRadio;
    this.secondPolygonRadio = secondPolygonRadio;
    
    addMouseListener(this);
    addMouseMotionListener(this);
    reset();
  }
  
  public void reset() {
    currentState = -1;
    subjectPolygon = null;
    clippingPolygon = null;
    sweepLineStates = null;
    weilerAthertonStates = null;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setStroke(new BasicStroke(LINE_STROKE));
    
    // Paint background to white
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, getWidth(), getHeight());
    
    // If current state is state when the polygons are being drawn
    if (currentState == -1) {
      // Draw polygons
      drawPolygons(g2d, SUBJECT_COLOR, CLIPPING_COLOR);
    } else if (sweepLineStates == null || weilerAthertonStates == null) {
      // If we don't have both sweepLineStates and weilerAthertonStates
      // at this point, then there was an error
      System.err.println("Current state is not -1 but we don't have both states!!!");
      
      if (sweepLineStates == null) {
        System.err.println(sweepLineStates);
      }
      
      if (weilerAthertonStates == null) {
        System.err.println(weilerAthertonStates);
      }
    } else if (currentState < sweepLineStates.size()) {
      // First draw both polygons in BLACK color
      drawPolygons(g2d, Color.BLACK);

      // Draw SweepLineState
      drawSweepLineState(g2d, sweepLineStates.get(currentState));
    } else if (currentState < weilerAthertonStates.size()){
      // First draw both polygons in BLACK color
      drawPolygons(g2d, Color.BLACK);

      // Then draw WeilerAthertonState
      drawWeilerAthertonState(g2d,
          weilerAthertonStates.get(currentState - sweepLineStates.size()));
    } else {
      System.err.println("Unknown current state index!!!");
    }
  }
  
  private void drawPolygons(Graphics2D g2d, Color subjectColor, Color clippingColor) {
    drawPolygon(g2d, subjectPolygon, subjectColor);
    drawPolygon(g2d, clippingPolygon, clippingColor);
    
    // Draw vertices
    drawVertices(g2d, subjectPolygon, subjectColor);
    drawVertices(g2d, clippingPolygon, clippingColor);
  }
  
  private void drawPolygons(Graphics2D g2d, Color color) {
    drawPolygons(g2d, color, color);
  }

  private void drawWeilerAthertonState(Graphics2D g2d, WeilerAthertonState weilerAthertonState) {
    // TODO(freezing): Draw Weiler-Atherton State
  }

  private void drawSweepLineState(Graphics2D g2d, SweepLineState sweepLineState) {
    // 1. Draw current list of segments that are intersecting status line (BLUE)
    g2d.setColor(Color.BLUE);
    for (PolygonSegment segment : sweepLineState.getCurrentSegments()) {
      Point2D pointA = segment.getStartPoint();
      Point2D pointB = segment.getEndPoint();

      g2d.drawLine(
          (int) Math.round(pointA.getX()),
          (int) Math.round(pointA.getY()),
          (int) Math.round(pointB.getX()),
          (int) Math.round(pointB.getY()));
    }
    
    // 2. Draw all intersection points so far
    g2d.setColor(INTERSECTION_POINTS_COLOR);
    for (SegmentPoint point : sweepLineState.getAllIntersectionPoints()) {
      drawPoint(g2d, point);
    }
    
    // 3. Draw new intersection points from the SweepLineState (RED)
    g2d.setColor(Color.RED);
    for (SegmentPoint point : sweepLineState.getIntersectionPoints()) {
      drawPoint(g2d, point);
    }
    
    // 4. Draw status line
    int statusLineX = (int) Math.round(sweepLineState.getStatusLineX());
    System.out.println(statusLineX);
    g2d.setColor(STATUS_LINE_COLOR);
    g2d.drawLine(statusLineX, 0, statusLineX, getHeight());
  }

  private void drawVertices(Graphics2D g2d, Polygon polygon, Color color) {
    if (polygon != null) {
      g2d.setColor(color);
      for (Point2D point : polygon.getVertices()) {
        drawPoint(g2d, point);
      }
    }
  }

  private void drawPoint(Graphics2D g2d, Point2D point) {
    g2d.fillOval((int)point.getX() - POINT_WIDTH / 2,
        (int)point.getY() - POINT_HEIGHT / 2, POINT_WIDTH, POINT_HEIGHT);
  }

  private void drawPolygon(Graphics2D g2d, Polygon polygon, Color color) {
    if (polygon != null) {
      g2d.setColor(color);
      g2d.draw(polygon.asPath2D());
    }
  }
  
  private void fillPolygon(Graphics2D g2d, Polygon polygon, Color color) {
    g2d.setColor(color);
    g2d.draw(polygon.asPath2D());
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    Polygon polygon = getSelectedPolygon(false);
    if (polygon != null && !polygon.isFinished()) {
      polygon.replaceLastVertex(new Point2D.Float(e.getX(), e.getY()));
      repaint();
    }
  }
  
  public void nextStepAction() {
    if (currentState == -1 
        && subjectPolygon != null && subjectPolygon.isFinished() 
        && clippingPolygon != null && clippingPolygon.isFinished()) {
      WeilerAtherton weilerAtherton = new WeilerAtherton(subjectPolygon, clippingPolygon);
      System.out.println("Executing Weiler-Atherton algorithm...");
      weilerAtherton.execute();
      
      // Set current state to the first SweepLine event
      currentState = 0;
      
      // Set SweepLine and WeilerAtherton States
      sweepLineStates = weilerAtherton.getSweepLine().getStates();
      weilerAthertonStates = weilerAtherton.getStates();
    }
    
    // We need both sweepLineStates and weilerAthertonStates to exist to use this action
    if (sweepLineStates != null && weilerAthertonStates != null && 
        currentState < sweepLineStates.size() + weilerAthertonStates.size() - 1) {
      currentState++;
      repaint();
    }
  }
  
  public void prevStepAction() {
    // We need both sweepLineStates and weilerAthertonStates to exist to use this action
    if (currentState > 0 && sweepLineStates != null && weilerAthertonStates != null) {
      currentState--;
      repaint();
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mouseDragged(MouseEvent e) {}

  @Override
  public void mousePressed(MouseEvent e) {}

  /**
   * If current point matches the first point (within a range) then finish drawing the polygon.
   * Otherwise, add new point to the polygon that is being drawn.
   * 
   * <p>NOTE: Every time when new segment line is added check if polygon is self-intersecting.</p>
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    if (currentState != -1 ) {
      // If Weiler-Atherton algorithm is executed then do nothing
      // (it is allowed only to use next and prev steps, or reset button)
      return;
    }
    
    // Get current point
    Point2D.Float currentPoint = new Point2D.Float(e.getX(), e.getY());
    
    // Check which polygon is being drawn
    Polygon polygon = getSelectedPolygon();
    if (polygon == null) {
      // This should never happen
      return;
    }

    // Check if point matches the first point
    if (polygon.getVertices().size() > 0 
        && guiClosePoints(currentPoint, polygon.getVertex(0))) {
      if (!polygon.finishPolygon()) {
        System.out.println("Polygon is not finished (Maybe has self-intersections)");
      } 
    } else {
      if (polygon.getVertices().isEmpty()) {
        // If this is the first point add it
        polygon.addVertex(currentPoint);
      } else {
        // Otherwise, replace last point
        polygon.replaceLastVertex(currentPoint);
      }
      // Add next point that will be drawn (and replaced when clicked)
      polygon.addVertex(currentPoint);
      // TODO(freezing): Check self-intersection
    }
    repaint();
  }

  private Polygon getSelectedPolygon(boolean generateNewIfNull) {
    Polygon polygon = null;

    if (firstPolygonRadio.isSelected()) {
      if (!generateNewIfNull) {
        return subjectPolygon;
      }
      polygon = getSubjectPolygon();
    } else if (secondPolygonRadio.isSelected()) {
      if (!generateNewIfNull) {
        return clippingPolygon;
      }
      polygon = getClippingPolygon();
    } else {
      System.err.println("No polygons are selected!!!");
    }
    return polygon;
  }
  
  private Polygon getSelectedPolygon() {
    return getSelectedPolygon(true);
  }

  /**
   * Checks if pointA and pointB are the same.
   * It is used for GUI checking.
   * @param pointA
   * @param pointB
   * @return
   */
  private boolean guiClosePoints(Point2D.Float pointA, Point2D.Float pointB) {
    double distance = pointA.distance(pointB);
    return distance < GUI_CLOSE_POINTS_DISTANCE;
  }

  private Polygon getSubjectPolygon() {
    if (subjectPolygon == null || subjectPolygon.isFinished()) {
      subjectPolygon = new Polygon();
    }
    return subjectPolygon;
  }
  
  private Polygon getClippingPolygon() {
    if (clippingPolygon == null || clippingPolygon.isFinished()) {
      clippingPolygon = new Polygon();
    }
    return clippingPolygon;
  }
  
  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

}
