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
//                              DataChooserFrame.java
//
//
//**********************************************************************
package kbctFrames;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import kbct.JKBCT;
import kbct.JVariable;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbctAux.MessageKBCT;
import fis.JExtendedDataFile;

/**
 * kbctFrames.DataChooserFrame displays a frame which is used by "View Menu" of main frame (MMI-Fuzzy Logic-IM).
 * User can choose variable to view and representation mode (Histogram, XY Panel).
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

//------------------------------------------------------------------------------
public class DataChooserFrame extends JDialog {
  static final long serialVersionUID=0;	  
  GridBagLayout gridBagLayout= new GridBagLayout();
  //panel histogram
  JPanel jPanelHistogram= new JPanel();
  GridBagLayout gridBLHistogram= new GridBagLayout();
  ScrollPaneLayout gridSBLHistogram= new ScrollPaneLayout();
  TitledBorder titledBorderHistogram;
  JScrollPane jScrollPanelHistogram= new JScrollPane();
  JButton jButtonAll= new JButton();
  JButton jButtonSwitch= new JButton();
  Vector<JCheckBox> CheckBoxs= new Vector<JCheckBox>();
  // panel XY
  JPanel jPanelXY= new JPanel();
  GridBagLayout gridBLXY= new GridBagLayout();
  TitledBorder titledBorderXY;
  JLabel jLabelX= new JLabel("X:");
  JLabel jLabelY= new JLabel("Y:");
  JComboBox jCBX= new JComboBox();
  JComboBox jCBY= new JComboBox();
  // panel validation
  JPanel jPanelValidation= new JPanel();
  GridBagLayout gridBLValidation= new GridBagLayout();
  JButton jButtonApply= new JButton();
  JButton jButtonCancel= new JButton();
  private JMainFrame Parent; // parent
  private JKBCT kbct_Data;
  private JKBCT kbct;
  private JExtendedDataFile DataFile;   // object data file
  private String[] VariablesNames;
  private static DataXYFrame dxyf1= null;
  private static DataXYFrame dxyf2= null;
  private static DataHistogramInputFrame dhif1= null;
  private static DataHistogramInputFrame dhif2= null;
  private static DataHistogramInputFrame dhif3= null;
  private static DataHistogramInputFrame dhif4= null;

  //------------------------------------------------------------------------------
  public DataChooserFrame(JMainFrame parent, JKBCT kbct_Data, JKBCT kbct, JExtendedDataFile data_file) {
    super(parent);
    this.Parent= parent;
    this.kbct_Data= kbct_Data;
    this.kbct= kbct;
    this.DataFile= data_file;
    this.VariablesNames= new String[kbct_Data.GetNbInputs()+kbct_Data.GetNbOutputs()];
    int output=0;
    for (int n=0; n<VariablesNames.length; n++) {
      String NameExpert= null;
      String NameData= null;
      if (n<kbct_Data.GetNbInputs()) {
         if (kbct != null) {
           if (n<kbct.GetNbInputs())
             NameExpert= kbct.GetInput(n+1).GetName();
           else
             NameExpert= null;
         }
         NameData= kbct_Data.GetInput(n+1).GetName();
      } else {
         if (kbct != null) {
           if (output<kbct.GetNbOutputs())
             NameExpert= kbct.GetOutput(output+1).GetName();
           else
             NameExpert= null;
         }
         output++;
         NameData= kbct_Data.GetOutput(output).GetName();
      }
      if ( (NameExpert==null)|| NameExpert.equals(NameData))
        this.VariablesNames[n]=NameData;
      else
        this.VariablesNames[n]=NameData+" => "+NameExpert;
    }
    try { 
    	jbInit();
    } catch(Throwable e) {
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en constructor en DataChooserFrame: "+e);
    }
  }
//------------------------------------------------------------------------------
  private void jbInit() throws Throwable {
    this.getContentPane().setLayout(gridBagLayout);
    this.setTitle(LocaleKBCT.GetString("DataChooser"));
    // panel histogram
    jPanelHistogram.setLayout(gridBLHistogram);
    jScrollPanelHistogram.setLayout(gridSBLHistogram);
    titledBorderHistogram= new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),LocaleKBCT.GetString("Histogram"));
    jScrollPanelHistogram.setBorder(titledBorderHistogram);
    jButtonAll.setText(LocaleKBCT.GetString("All"));
    jButtonAll.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonAll_actionPerformed(); } });
    jButtonSwitch.setText(LocaleKBCT.GetString("Switch"));
    jButtonSwitch.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonSwitch_actionPerformed(); } });
    this.getContentPane().add(jScrollPanelHistogram, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));
    jPanelHistogram.add(jButtonAll, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 5, 10), 0, 0));
    jPanelHistogram.add(jButtonSwitch, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 5, 10), 0, 0));
    int NbVariables= this.VariablesNames.length;
    for( int i=0 ; i<NbVariables ; i++ ) {
      JCheckBox cb = new JCheckBox(this.VariableName(i));
      this.CheckBoxs.add(cb);
      jPanelHistogram.add(cb, new GridBagConstraints(0, i+1, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    }
    jScrollPanelHistogram.getViewport().add(jPanelHistogram, null);
    // panel XY
    jPanelXY.setLayout(gridBLXY);
    titledBorderXY= new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"XY");
    jPanelXY.setBorder(titledBorderXY);
    jPanelXY.add(jLabelX, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    jPanelXY.add(jLabelY, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    jPanelXY.add(jCBX, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    jPanelXY.add(jCBY, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    this.getContentPane().add(jPanelXY, new GridBagConstraints(1, 1, 1, 1, 0.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 0, 5), 0, 0));
    DefaultComboBoxModel dcbmx = new DefaultComboBoxModel();
    dcbmx.addElement(new String());
    DefaultComboBoxModel dcbmy = new DefaultComboBoxModel();
    dcbmy.addElement(new String());
    for( int i=0 ; i<NbVariables ; i++ ) {
      dcbmx.addElement(this.VariableName(i));
      dcbmy.addElement(this.VariableName(i));
    }
    this.jCBX.setModel(dcbmx);
    this.jCBY.setModel(dcbmy);
    this.jCBX.setRenderer(new BasicComboBoxRenderer() {
   	  static final long serialVersionUID=0;	
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        try {
          if( (index > 0) && (index <= kbct_Data.GetNbInputs()) && (kbct_Data.GetInput(index-1).GetActive() == false) )
            this.setForeground(Color.gray);
          if( (index > kbct_Data.GetNbInputs()) && (kbct_Data.GetOutput(index-1-kbct_Data.GetNbInputs()).GetActive() == false) )
            this.setForeground(Color.gray);
        } catch( Throwable t ) {}
        return this;
        }
      });
    this.jCBY.setRenderer(new BasicComboBoxRenderer() {
   	  static final long serialVersionUID=0;	
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        try {
          if( (index > 0) && (index <= kbct_Data.GetNbInputs()) && (kbct_Data.GetInput(index-1).GetActive() == false) )
            this.setForeground(Color.gray);
          if( (index > kbct_Data.GetNbInputs()) && (kbct_Data.GetOutput(index-1-kbct_Data.GetNbInputs()).GetActive() == false) )
            this.setForeground(Color.gray);
        } catch( Throwable t ) {}
        return this;
        }
      });
    // panel validation
    jPanelValidation.setLayout(gridBLValidation);
    jButtonApply.setText(LocaleKBCT.GetString("Apply"));
    jButtonApply.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonApply_actionPerformed(e); } });
    jButtonCancel.setText(LocaleKBCT.GetString("Cancel"));
    jButtonCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonCancel_actionPerformed(); } });
    this.getContentPane().add(jPanelValidation, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanelValidation.add(jButtonCancel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    if (MainKBCT.getConfig().GetTutorialFlag()) {
    	//System.out.println("Apply");
        this.jButtonApply_actionPerformed(null);
    } else {
        this.pack();
        this.setModal(true);
        this.setSize(500, 200);
        this.setLocation(JChildFrame.ChildPosition(this.Parent, this.getSize()));
        this.setVisible(true);
    }
  }
//------------------------------------------------------------------------------
  void jButtonCancel_actionPerformed() { this.dispose(); }
//------------------------------------------------------------------------------
  void jButtonApply_actionPerformed(ActionEvent e) {
    try {
      for( int i=0 ; i<this.CheckBoxs.size() ; i++ )
        if( ((JCheckBox)this.CheckBoxs.elementAt(i)).isSelected() == true ) {
        	DataHistogramInputFrame dhif= new DataHistogramInputFrame(this.Parent, this.kbct_Data, i, i, this.DataFile, this.VariableName(i));
            Point p= dhif.getLocationOnScreen();
        	dhif.setLocation((int)p.getX()+20*i, (int)p.getY()+20*i);
        }
      int xi = this.jCBX.getSelectedIndex();
      int yi = this.jCBY.getSelectedIndex();
      if( (xi != 0) && (yi != 0) )
	    new DataXYFrame( this.Parent, xi-1, yi-1, this.kbct_Data, xi-1, yi-1, this.DataFile );

      if (MainKBCT.getConfig().GetTutorialFlag()) {
    	  // For JTutorialFrame (IRIS dataset)
    	  if ( ( (this.dhif1==null) || (!this.dhif1.isVisible()) ) && (this.VariablesNames.length>=1) ) {
      	        this.dhif1= new DataHistogramInputFrame(this.Parent, this.kbct_Data, 0, 0, this.DataFile, this.VariableName(0));
    	  }
    	  if ( ( (this.dhif2==null) || (!this.dhif2.isVisible()) ) && (this.VariablesNames.length>=2) ) {
      	        this.dhif2= new DataHistogramInputFrame(this.Parent, this.kbct_Data, 1, 1, this.DataFile, this.VariableName(1));
    	  }
    	  if ( ( (this.dhif3==null) || (!this.dhif3.isVisible()) ) && (this.VariablesNames.length>=3) ) {
      	        this.dhif3= new DataHistogramInputFrame(this.Parent, this.kbct_Data, 2, 2, this.DataFile, this.VariableName(2));
    	  }
    	  if ( ( (this.dhif4==null) || (!this.dhif4.isVisible()) ) && (this.VariablesNames.length>=4) ) {
      	        this.dhif4= new DataHistogramInputFrame(this.Parent, this.kbct_Data, 3, 3, this.DataFile, this.VariableName(3));
    	  }
    	  if ( ( (this.dxyf1==null) || (!this.dxyf1.isVisible()) ) && (this.VariablesNames.length>=2) ) {
    	        this.dxyf1= new DataXYFrame( this.Parent, 0, 1, this.kbct_Data, 0, 1, this.DataFile );
    	  } 
    	  if ( ( (this.dxyf2==null) || (!this.dxyf2.isVisible()) ) && (this.VariablesNames.length>=2) ) {
    	        this.dxyf2= new DataXYFrame( this.Parent, 0, this.VariablesNames.length-1, this.kbct_Data, 0, this.VariablesNames.length-1, this.DataFile );
    	  }
      	  Point p= dhif1.getLocationOnScreen();
    	  if (p!=null) {
              if (this.dhif1 != null)
    		      this.dhif1.setLocation((int)p.getX()+20, (int)p.getY()+20);
              
              if (this.dhif2 != null)
    		      this.dhif2.setLocation((int)p.getX()+40, (int)p.getY()+40);

              if (this.dhif3 != null)
                  this.dhif3.setLocation((int)p.getX()+60, (int)p.getY()+60);

              if (this.dhif4 != null)
                  this.dhif4.setLocation((int)p.getX()+80, (int)p.getY()+80);

              if (this.dxyf1 != null)
                  this.dxyf1.setLocation((int)p.getX()+100, (int)p.getY()+100);

              if (this.dxyf2 != null)
                  this.dxyf2.setLocation((int)p.getX()+120, (int)p.getY()+120);
    	  }
      }

      this.dispose();
    } catch( Throwable t ) {
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en DataChooserFrame en jButtonApply_actionPerformed: "+e);
    }
  }
//------------------------------------------------------------------------------
  void jButtonAll_actionPerformed() {
    for( int i=0 ; i<this.CheckBoxs.size() ; i++ ) {
        ((JCheckBox)this.CheckBoxs.elementAt(i)).setSelected(true);
    }
  }
//------------------------------------------------------------------------------
  void jButtonSwitch_actionPerformed() {
    for( int i=0 ; i<this.CheckBoxs.size() ; i++ ) {
    	boolean aux= ((JCheckBox)this.CheckBoxs.elementAt(i)).isSelected();
        ((JCheckBox)this.CheckBoxs.elementAt(i)).setSelected(!aux);
    }
  }
//------------------------------------------------------------------------------
  private String VariableName( int index ) {
    if (this.kbct != null)
      return this.VariablesNames[index];
    else
      return this.GetInput(index).GetName();
  }
//------------------------------------------------------------------------------
  private JVariable GetInput( int index ) {
    if( this.kbct_Data != null )
      if( index < this.kbct_Data.GetNbInputs() )
        return this.kbct_Data.GetInput(index+1);
      else
        return this.kbct_Data.GetOutput(index-this.kbct_Data.GetNbInputs()+1);
    else
      return null;
  }
}