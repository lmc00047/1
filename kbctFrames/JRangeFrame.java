//***********************************************************************
//
//   GUAJE: Generating Understandable and Accurate Fuzzy Models in a Java Environment
//
//   Contact: guajefuzzy@gmail.com
//
//   Copyright (C) 2007 - 2015  Jose Maria Alonso Moral
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program. If not, see <http://www.gnu.org/licenses/>.
//
//***********************************************************************

//***********************************************************************
//
//
//                              JRangeFrame.java
//
//
//**********************************************************************
package kbctFrames;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kbct.JVariable;
import kbct.LocaleKBCT;
import kbctAux.DoubleField;
import kbctAux.MessageKBCT;

/**
 * kbctFrames.JRangeFrame displays a frame which let user changes variable range.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
//------------------------------------------------------------------------------
public class JRangeFrame extends JDialog {
  static final long serialVersionUID=0;
  private JPanel jPanelDisplayRange = new JPanel();
  private GridBagLayout gridBLDisplayRange = new GridBagLayout();
  private JPanel jPanelSaisie = new JPanel();
  private JPanel jPanelValidation = new JPanel();
  private GridBagLayout gridBagLayoutValidation = new GridBagLayout();
  private JButton jButtonCancel = new JButton();
  private JButton jButtonApply = new JButton();
  private GridBagLayout gridBLSaisie = new GridBagLayout();
  private JLabel jLabelLower = new JLabel();
  private JLabel jLabelUpper = new JLabel();
  private DoubleField dfLower = new DoubleField();
  private DoubleField dfUpper = new DoubleField();
  private double [] InputRange;       // domaine de l'entrée
  private double [] NewRange;         // nouveau domaine saisi
//------------------------------------------------------------------------------
  public JRangeFrame( JVariableFrame parent, JVariable input, String title ) {
    super(parent);
    try {
      this.setTitle(LocaleKBCT.GetString(title));
      if (title.equals("PhysicalRange"))
        this.InputRange = input.GetInputPhysicalRange();
      else
        this.InputRange = input.GetInputInterestRange();

      jbInit();
    } catch( Throwable t) {
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en constructor 1 de JRangeFrame: "+t);
    }
  }
//------------------------------------------------------------------------------
  public JRangeFrame( JMainFrame parent, JVariable input, String title ) {
    super(parent);
    try {
      this.setTitle(LocaleKBCT.GetString(title));
      if (title.equals("PhysicalRange"))
        this.InputRange = input.GetInputPhysicalRange();
      else
        this.InputRange = input.GetInputInterestRange();

      jbInit();
    } catch( Throwable t) {
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en constructor 2 de JRangeFrame: "+t);
    }
  }
//------------------------------------------------------------------------------
  public JRangeFrame( DataHistogramFrame parent, JVariable input, String title ) {
    super(parent);
    try {
      this.setTitle(LocaleKBCT.GetString(title));
      if (title.equals("PhysicalRange"))
        this.InputRange = input.GetInputPhysicalRange();
      else
        this.InputRange = input.GetInputInterestRange();

      jbInit();
    } catch( Throwable t) {
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en constructor 3 de JRangeFrame: "+t);
    }
  }
//------------------------------------------------------------------------------
  private void jbInit() throws Throwable {
	this.setIconImage(LocaleKBCT.getIconGUAJE().getImage());
    jPanelDisplayRange.setLayout(gridBLDisplayRange);
    jPanelValidation.setLayout(gridBagLayoutValidation);
    jButtonCancel.setText(LocaleKBCT.GetString("Cancel"));
    jButtonCancel.addActionListener(new java.awt.event.ActionListener() { public void actionPerformed(ActionEvent e) { jButtonCancel_actionPerformed(); } });
    jButtonApply.setText(LocaleKBCT.GetString("Apply"));
    jButtonApply.addActionListener(new java.awt.event.ActionListener() { public void actionPerformed(ActionEvent e) { jButtonApply_actionPerformed(); } });
    jPanelSaisie.setLayout(gridBLSaisie);
    dfLower.setValue(this.InputRange[0]);
    dfUpper.setValue(this.InputRange[1]);
    this.jLabelLower.setText(LocaleKBCT.GetString("Lower")+":");
    this.jLabelUpper.setText(LocaleKBCT.GetString("Upper")+":");
    this.getContentPane().add(jPanelDisplayRange);
    jPanelDisplayRange.add(jPanelSaisie, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    jPanelDisplayRange.add(jPanelValidation, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
    jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    jPanelValidation.add(jButtonCancel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    jPanelSaisie.add(jLabelLower, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
    jPanelSaisie.add(jLabelUpper, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
    jPanelSaisie.add(dfLower, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 50, 0));
    jPanelSaisie.add(dfUpper, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 50, 0));
    this.setResizable(false);
    this.setModal(true);
    this.pack();
    this.setSize(220, 120);
  }
//------------------------------------------------------------------------------
  private void jButtonCancel_actionPerformed() {
    this.NewRange = null;
    this.dispose();
  }
//------------------------------------------------------------------------------
  private double [] ReadRange() throws Exception {
    double [] result = new double[2];
    result[0] = dfLower.getValue();
    result[1] = dfUpper.getValue();
    if( result[0] >= result[1] )
      throw new Exception(LocaleKBCT.GetString("Upper")+" "+LocaleKBCT.GetString("MustBeHigherThan")+" "+LocaleKBCT.GetString("Lower"));

    return result;
  }
//------------------------------------------------------------------------------
  private void jButtonApply_actionPerformed() {
    try {
      this.NewRange = this.ReadRange();
      this.dispose();
    } catch( Exception ex ) {
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JRangeFrame en jButtonApply_actionPerformed: "+ex);
    }
  }
//------------------------------------------------------------------------------
  public double [] Show() {
    this.setVisible(true);
    return this.NewRange;
  }
}