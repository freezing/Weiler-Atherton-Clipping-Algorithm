package gui.actions;

import gui.Framework;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ResetAction extends AbstractAction {
  private static final long serialVersionUID = 1L;
  
  private Framework framework;
  
  public ResetAction(Framework framework) {
    this.framework = framework;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    framework.reset();
  }
  
}
