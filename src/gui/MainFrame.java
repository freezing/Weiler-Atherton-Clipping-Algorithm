package gui;

import gui.actions.NextStepAction;
import gui.actions.PrevStepAction;
import gui.actions.ResetAction;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class MainFrame extends JFrame implements MouseMotionListener {
  private static final long serialVersionUID = 1L;

  private JLabel coordinatesLabel;
  
  public MainFrame() {
    JPanel toolkitPanel = new JPanel(new FlowLayout());
    JButton prevStepButton = new JButton("Prev");
    JButton nextStepButton = new JButton("Next");
    JButton resetButton = new JButton("Reset");
    
    coordinatesLabel = new JLabel("");
    
    ButtonGroup radioButtonGroup = new ButtonGroup();
    
    JRadioButton firstPolygonButton = new JRadioButton("First polygon");
    JRadioButton secondPolygonButton = new JRadioButton("Second polygon");
    
    firstPolygonButton.setSelected(true);
    
    radioButtonGroup.add(firstPolygonButton);
    radioButtonGroup.add(secondPolygonButton);
    
    toolkitPanel.add(coordinatesLabel);
    toolkitPanel.add(firstPolygonButton);
    toolkitPanel.add(secondPolygonButton);
    
    toolkitPanel.add(prevStepButton);
    toolkitPanel.add(nextStepButton);
    toolkitPanel.add(resetButton);
    
    Framework framework = new Framework(firstPolygonButton, secondPolygonButton);
    
    prevStepButton.setAction(new PrevStepAction(framework));
    nextStepButton.setAction(new NextStepAction(framework));
    resetButton.setAction(new ResetAction(framework));
    
    prevStepButton.setText("Prev");
    nextStepButton.setText("Next");
    resetButton.setText("Reset");
    
    add(framework, BorderLayout.CENTER);
    add(toolkitPanel, BorderLayout.SOUTH);
    
    framework.addMouseMotionListener(this);
    
    setSize(800, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }
  
  @Override
  public void mouseDragged(MouseEvent e) {}

  @Override
  public void mouseMoved(MouseEvent e) {
    coordinatesLabel.setText(e.getPoint().x + ", " + e.getPoint().y);
  }

  public static void main(String[] args) {
    MainFrame frame = new MainFrame();
    frame.setTitle("Weiler-Atherton Algorithm (using Sweep Line) - Nikola Stojiljkovic RN-10/11");
    frame.setVisible(true);
  }
}
