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
//                              JAdvOptFrame.java
//
//
//**********************************************************************

// Contains: MFParams, MFTriangularParams, MFTrapezoidalParams,
// MFSemiTrapezoidalInfParams, MFSemiTrapezoidalSupParams,
// MFDiscreteParams, MFUniversalParams, MFGausssianParams,
// JAdvOptFrame, JMFsPanel

package kbctFrames;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import kbct.JConvert;
import kbct.JKBCT;
import kbct.JKBCTInput;
import kbct.JVariable;
import kbct.KBCTListener;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbctAux.DoubleField;
import kbctAux.DoubleFieldInput;
import kbctAux.IntegerField;
import kbctAux.MessageKBCT;
import kbctAux.Translatable;

import org.freehep.util.export.ExportDialog;

import print.JPrintPreview;
import KB.LabelKBCT;
import KB.variable;
import fis.JSemaphore;

/**
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
//------------------------------------------------------------------------------
/**
 * Define parametres of membership functions ("labels").
 */
abstract class MFParams {
  protected boolean DefinedMP;
  protected boolean DefinedMPaux;
  protected JLabel MPLabel;
  protected DoubleField MP;
  protected JLabel MPauxLabel;
  protected DoubleField MPaux;
  protected JLabel [] jLParams;
  protected DoubleField [] dfParams;
//------------------------------------------------------------------------------
  public MFParams() {}
  public abstract int Number();
//------------------------------------------------------------------------------
  public void SetDefinedMP( boolean FLAG ) throws Throwable {
    this.DefinedMP= FLAG;
  }
//------------------------------------------------------------------------------
  public void SetDefinedMPaux( boolean FLAG ) throws Throwable {
    this.DefinedMPaux= FLAG;
  }
//------------------------------------------------------------------------------
  public void SetMP( double mp ) throws Throwable {
    this.MP.setValue(mp);
    this.DefinedMP= true;
  }
//------------------------------------------------------------------------------
  public double GetMP() { return this.MP.getValue(); }
//------------------------------------------------------------------------------
  public void SetMPaux( double mp ) throws Throwable {
    this.MPaux.setValue(mp);
    this.DefinedMPaux= true;
  }
//------------------------------------------------------------------------------
  public double GetMPaux() { return this.MPaux.getValue(); }
//------------------------------------------------------------------------------
  public void SetParams( double [] params ) throws Throwable {
    if( params.length == this.dfParams.length )
      for( int i=0 ; i<this.dfParams.length ; i++ )
        this.dfParams[i].setValue(params[i]);
  }
//------------------------------------------------------------------------------
  public double [] GetParams() {
    double [] result = new double[this.dfParams.length];
    for( int i=0 ; i<result.length ; i++ )
      result[i] = this.dfParams[i].getValue();

    return result;
  }
//------------------------------------------------------------------------------
  public void RAZParams() throws Throwable {
    this.DefinedMP= false;
    for( int i=0 ; i<this.dfParams.length ; i++ )
      this.dfParams[i].setValue(0);
  }
//------------------------------------------------------------------------------
  public void Translate() {
    if (this.dfParams.length==4) {
	    this.MPLabel.setText(LocaleKBCT.GetString("MP")+" (P2):");
        this.MPauxLabel.setText(LocaleKBCT.GetString("MP")+" (P3):");
    } else {
	    this.MPLabel.setText(LocaleKBCT.GetString("MP")+":");
    }
    if (this.DefinedMP) {
      double mp = this.MP.getValue();
      this.MP.setLocale(LocaleKBCT.Locale());
      this.MP.setValue(mp);
    }
    if (this.DefinedMPaux) {
        double mpaux = this.MPaux.getValue();
        this.MPaux.setLocale(LocaleKBCT.Locale());
        this.MPaux.setValue(mpaux);
    }
    for( int i=0 ; i<this.dfParams.length ; i++ ) {
      double sauve= this.dfParams[i].getValue();
      this.dfParams[i].setLocale(LocaleKBCT.Locale());
      this.dfParams[i].setValue(sauve);
    }
  }
}


//------------------------------------------------------------------------------
class MFTriangularParams extends MFParams {
  private JVariable input;
//------------------------------------------------------------------------------
  public MFTriangularParams( JPanel panel, JVariable input ) throws Throwable {
    this.input= input;
    MPLabel= new JLabel();
    MP= new DoubleField();
    jLParams= new JLabel[3];
    dfParams= new DoubleFieldInput[3];
    for( int i=0 ; i<3 ; i++ ) {
      jLParams[i]= new JLabel();
      dfParams[i]= new DoubleFieldInput(input);
    }
    this.RAZParams();
    panel.removeAll();
    panel.add(MPLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    panel.add(MP, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 60, 0));
    panel.add(jLParams[0], new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    panel.add(dfParams[0], new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 60, 0));
    panel.add(jLParams[1], new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    panel.add(dfParams[1], new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 60, 0));
    panel.add(jLParams[2], new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    panel.add(dfParams[2], new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 60, 0));
    this.Translate();
  }
//------------------------------------------------------------------------------
  public void Translate() {
    super.Translate();
    jLParams[0].setText(LocaleKBCT.GetString("Lower") + ":");
    jLParams[1].setText(LocaleKBCT.GetString("Top") + ":");
    jLParams[2].setText(LocaleKBCT.GetString("Upper") + ":");
  }
//------------------------------------------------------------------------------
  public int Number() { return LabelKBCT.TRIANGULAR; }
//------------------------------------------------------------------------------
  public void RAZParams() throws Throwable {
    double [] range = input.GetInputInterestRange();
    dfParams[0].setValue(range[0]);
    dfParams[1].setValue(range[0] + ((range[1] - range[0])/2));
    dfParams[2].setValue(range[1]);
  }
}


//------------------------------------------------------------------------------
class MFTrapezoidalParams extends MFParams {
  private JVariable input;
//------------------------------------------------------------------------------
  public MFTrapezoidalParams( JPanel panel, JVariable input ) throws Throwable {
    this.input= input;
    MPLabel= new JLabel();
    MP= new DoubleField();
    MPauxLabel= new JLabel();
    MPaux= new DoubleField();
    jLParams= new JLabel[4];
    dfParams= new DoubleFieldInput[4];
    for( int i=0 ; i<4 ; i++ ) {
      jLParams[i]= new JLabel();
      dfParams[i]=  new DoubleFieldInput(input);
    }
    this.RAZParams();
    panel.removeAll();
    panel.add(MPLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel.add(MP, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 60, 0));
    panel.add(MPauxLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel.add(MPaux, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 60, 0));
    panel.add(jLParams[0], new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    panel.add(dfParams[0], new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 60, 0));
    panel.add(jLParams[1], new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    panel.add(dfParams[1], new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 60, 0));
    panel.add(jLParams[2], new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    panel.add(dfParams[2], new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 60, 0));
    panel.add(jLParams[3], new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    panel.add(dfParams[3], new GridBagConstraints(6, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 60, 0));
    this.Translate();
  }
//------------------------------------------------------------------------------
  public void Translate() {
    super.Translate();
    jLParams[0].setText(LocaleKBCT.GetString("Lower") + ":");
    jLParams[1].setText(LocaleKBCT.GetString("Top") + " " + LocaleKBCT.GetString("Lower") + ":");
    jLParams[2].setText(LocaleKBCT.GetString("Top") + " " + LocaleKBCT.GetString("Upper") + ":");
    jLParams[3].setText(LocaleKBCT.GetString("Upper") + ":");
  }
//------------------------------------------------------------------------------
  public int Number() { return LabelKBCT.TRAPEZOIDAL; }
//------------------------------------------------------------------------------
  public void RAZParams() throws Throwable {
    double [] range = input.GetInputInterestRange();
    dfParams[0].setValue(range[0]);
    dfParams[1].setValue(range[0] + ((range[1] - range[0])/3));
    dfParams[2].setValue(range[1] - ((range[1] - range[0])/3));
    dfParams[3].setValue(range[1]);
  }
}


//------------------------------------------------------------------------------
class MFSemiTrapezoidalInfParams extends MFParams {
  private JVariable input;
//------------------------------------------------------------------------------
  public MFSemiTrapezoidalInfParams( JPanel panel, JVariable input ) throws Throwable {
    this.input = input;
    MPLabel= new JLabel();
    MP= new DoubleField();
    jLParams= new JLabel[3];
    dfParams= new DoubleFieldInput[3];
    for( int i=0 ; i<3 ; i++ ) {
      jLParams[i]= new JLabel();
      dfParams[i]=  new DoubleFieldInput(input);
    }
    this.RAZParams();
    this.dfParams[0].setEnabled(false);
    this.dfParams[0].setValue(input.GetInputInterestRange()[0]);
    panel.removeAll();
    panel.add(MPLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    panel.add(MP, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 60, 0));
    panel.add(jLParams[0], new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    panel.add(dfParams[0], new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 60, 0));
    panel.add(jLParams[1], new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    panel.add(dfParams[1], new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 60, 0));
    panel.add(jLParams[2], new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    panel.add(dfParams[2], new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 60, 0));
    this.Translate();
  }
//------------------------------------------------------------------------------
  public void Translate() {
    super.Translate();
    jLParams[0].setText(LocaleKBCT.GetString("Top") + " " + LocaleKBCT.GetString("Lower") + ":");
    jLParams[1].setText(LocaleKBCT.GetString("Top") + " " + LocaleKBCT.GetString("Upper") + ":");
    jLParams[2].setText(LocaleKBCT.GetString("Upper") + ":");
  }
//------------------------------------------------------------------------------
  public int Number() { return LabelKBCT.SEMITRAPEZOIDALINF; }
//------------------------------------------------------------------------------
  public void RAZParams() throws Throwable {
    double [] range = input.GetInputInterestRange();
    dfParams[0].setValue(range[0]);
    dfParams[1].setValue(range[0] + ((range[1] - range[0])/2));
    dfParams[2].setValue(range[1]);
  }
}


//------------------------------------------------------------------------------
class MFSemiTrapezoidalSupParams extends MFParams {
  private JVariable input;
//------------------------------------------------------------------------------
  public MFSemiTrapezoidalSupParams( JPanel panel, JVariable input ) throws Throwable {
    this.input = input;
    MPLabel= new JLabel();
    MP= new DoubleField();
    jLParams= new JLabel[3];
    dfParams= new DoubleFieldInput[3];
    for( int i=0 ; i<3 ; i++ ) {
      jLParams[i]= new JLabel();
      dfParams[i]=  new DoubleFieldInput(input);
    }
    this.RAZParams();
    this.dfParams[2].setEnabled(false);
    this.dfParams[2].setValue(input.GetInputInterestRange()[1]);
    panel.removeAll();
    panel.add(MPLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    panel.add(MP, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 60, 0));
    panel.add(jLParams[0], new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    panel.add(dfParams[0], new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 60, 0));
    panel.add(jLParams[1], new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    panel.add(dfParams[1], new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 60, 0));
    panel.add(jLParams[2], new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    panel.add(dfParams[2], new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 60, 0));
    this.Translate();
  }
//------------------------------------------------------------------------------
  public void Translate() {
    super.Translate();
    jLParams[0].setText(LocaleKBCT.GetString("Lower") + ":");
    jLParams[1].setText(LocaleKBCT.GetString("Top") + " " + LocaleKBCT.GetString("Lower") + ":");
    jLParams[2].setText(LocaleKBCT.GetString("Top") + " " + LocaleKBCT.GetString("Upper") + ":");
  }
//------------------------------------------------------------------------------
  public int Number() { return LabelKBCT.SEMITRAPEZOIDALSUP; }
//------------------------------------------------------------------------------
  public void RAZParams() throws Throwable {
    double [] range= input.GetInputInterestRange();
    dfParams[0].setValue(range[0]);
    dfParams[1].setValue(range[0] + ((range[1] - range[0])/2));
    dfParams[2].setValue(range[1]);
  }
}


//------------------------------------------------------------------------------
class MFDiscreteParams extends MFParams {
  private JLabel jLNumberOfValues= new JLabel();
  private IntegerField ifNumberOfValues= new IntegerField();
  private JVariable Input;
  private JPanel Panel;
  private JButton jbModifyNumberOfValues = new JButton();
//------------------------------------------------------------------------------
  public MFDiscreteParams( JPanel panel, JVariable input ) throws Throwable {
    this.Input= input;
    this.Panel= panel;
    MPLabel= new JLabel();
    MP= new DoubleField();
    this.InitParams(1);
    this.jbModifyNumberOfValues.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
      if( ifNumberOfValues.getValue() <= 0 )
        MessageKBCT.Error(null, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("NumberOfValues") + " " + LocaleKBCT.GetString("MustBePositive"));
      else {
        try {
          DoubleField [] old_dfParams = MFDiscreteParams.this.dfParams;
          MFDiscreteParams.this.InitParams(ifNumberOfValues.getValue());
          for( int i=0 ; i<Math.min(old_dfParams.length, MFDiscreteParams.this.dfParams.length) ; i++ )
            MFDiscreteParams.this.dfParams[i].setValue(old_dfParams[i].getValue());
        } catch( Throwable t ) { MessageKBCT.Error(null, t);}
      }
    }} );
  }
//------------------------------------------------------------------------------
  public void Translate() {
    super.Translate();
    this.jLNumberOfValues.setText(LocaleKBCT.GetString("NumberOfValues") + ":");
    this.jbModifyNumberOfValues.setText(LocaleKBCT.GetString("Modify"));
    for( int i=0 ; i<this.jLParams.length ; i++ )
      jLParams[i].setText(LocaleKBCT.GetString("Value") + " " + Integer.toString(i+1) + ":");
  }
//------------------------------------------------------------------------------
  public void SetParams( double [] params ) throws Throwable {
    this.InitParams(params.length);
    super.SetParams(params);
  }
//------------------------------------------------------------------------------
  private void InitParams( int number_of_params ) throws Throwable {
    this.ifNumberOfValues.setValue(number_of_params);
    jLParams= new JLabel[number_of_params];
    dfParams= new DoubleFieldInput[number_of_params];
    for( int i=0 ; i<number_of_params ; i++ ) {
      jLParams[i]= new JLabel();
      dfParams[i]= new DoubleFieldInput(this.Input);
    }
    MP.setEnabled(false);
    this.RAZParams();
    Panel.removeAll();
    Panel.add(MPLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    Panel.add(MP, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 60, 0));
    Panel.add(jLNumberOfValues, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    Panel.add(ifNumberOfValues, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 60, 0));
    Panel.add(jbModifyNumberOfValues, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 60, 0));
    for( int i=0 ; i<number_of_params ; i++ ) {
      Panel.add(jLParams[i], new GridBagConstraints(0, i+2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
      Panel.add(dfParams[i], new GridBagConstraints(1, i+2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 60, 0));
    }
    this.Translate();
  }
//------------------------------------------------------------------------------
  public int Number() { return LabelKBCT.DISCRETE; }
//------------------------------------------------------------------------------
  public void RAZParams() throws Throwable {
    double [] range= this.Input.GetInputInterestRange();
    double ecart = (range[1] - range[0]) / (this.dfParams.length + 1);
    for( int i=0 ; i<this.dfParams.length ; i++ )
      dfParams[i].setValue(range[0] + ((i+1)*ecart));
  }
}


/**
 * Display a window with information about membership functions ("labels").
 */
public class JAdvOptFrame extends JDialog implements Translatable {
  static final long serialVersionUID=0;	
  protected JKBCT kbct;
  protected JSemaphore Semaphore;
  protected int Number;
  protected JVariable Temp;
  private KBCTListener KBCTListener;
  private MFParams MFParams = null;
  private int SELECTED_LABEL= 1;
  private String VariableType= "";
  protected JPanel PanelInput = new JPanel(new GridBagLayout());
  public static Color[] colors;
  // menus
  private JMenuBar jMenuBarInput = new JMenuBar();
  // menu MFs
  private JMenu jMenuMFs = new JMenu();
  private JMenuItem jMenuNewMF = new JMenuItem();
  private JMenuItem jMenuRemoveMF = new JMenuItem();
  private JMenuItem jMenuMergeMFs = new JMenuItem();
  private JMenuItem jMenuCopyMF = new JMenuItem();
  private JMenuItem jMenuUpMF = new JMenuItem();
  private JMenuItem jMenuDownMF = new JMenuItem();
  private JMenuItem jMenuRefinementMF = new JMenuItem();
  private JMenuItem jMenuScalingMFs = new JMenuItem();
  // menu print
  private JMenuItem jMenuPrint = new JMenuItem();
  // menu expert
  private JMenuItem jMenuExport = new JMenuItem();
  // menu reset zoom
  private JMenuItem jMenuDisplayResetZoom = new JMenuItem();
  // menu close
  private JMenuItem jMenuClose = new JMenuItem();
  // menu help
  private JMenuItem jMenuHelp = new JMenuItem();
  // panel MFs
  private TitledBorder titledBorderMFs;
  private JPanel jPanelMFs = new JPanel(new GridBagLayout());
  private TitledBorder titledBorderListMF;
  private DefaultListModel jListMFModel = new DefaultListModel();
  protected JList jListMF = new JList(jListMFModel);
  private JScrollPane jScrollPaneListMF = new JScrollPane(jListMF);
  private JMFsPanel jGraphMFs;
  private TitledBorder titledBorderMFParameters;
  private JPanel jPanelMFParameters = new JPanel(new GridBagLayout()) {
    static final long serialVersionUID=0;	
    public void setEnabled(boolean enabled) {
      super.setEnabled(enabled);
      if ( (JAdvOptFrame.this.Temp.GetScaleName().equals("user")) ||
           (JAdvOptFrame.this.NewMF) )
        JAdvOptFrame.this.jTFMFName.setEnabled(true);
      else
        JAdvOptFrame.this.jTFMFName.setEnabled(false);

      JAdvOptFrame.this.jCBMFType.setEnabled(enabled);
      JAdvOptFrame.this.jBApplyParameter.setEnabled(enabled);
      JAdvOptFrame.this.jBCancelParameter.setEnabled(enabled);
    }
  };
  private JScrollPane jScrollPaneMFParameters = new JScrollPane(this.jPanelMFParameters);
  private JLabel jLName = new JLabel();
  private JTextField jTFMFName = new JTextField();
  private JPanel jPType = new JPanel(new GridBagLayout());
  private JLabel jLType = new JLabel();
  private DefaultComboBoxModel jCBMFTypeModel = new DefaultComboBoxModel() {
    static final long serialVersionUID=0;	
    public Object getElementAt(int index) {
      String Label_Defenition="";
      switch(index) {
        case 0: Label_Defenition= "triangular"; break;
        case 1: Label_Defenition= "trapezoidal"; break;
        case 2: Label_Defenition= "SemiTrapezoidalInf"; break;
        case 3: Label_Defenition= "SemiTrapezoidalSup"; break;
      }
      try { return LocaleKBCT.GetString(Label_Defenition); }
      catch( Throwable t ) { return null; }
    }
  };
  private JComboBox jCBMFType = new JComboBox(jCBMFTypeModel);
  private JPanel jPValidParameter = new JPanel();
  private JButton jBApplyParameter = new JButton();
  private JButton jBCancelParameter = new JButton();
  private JPanel jPanelMFParams = new JPanel(new GridBagLayout());
  private boolean NewMF = false;
  // popup MFs
  private JPopupMenu jPopupMFs = new JPopupMenu();
  private JMenuItem jPopupNewMF = new JMenuItem();
  private JMenuItem jPopupRemoveMF = new JMenuItem();
  private JMenuItem jPopupGroupMF = new JMenuItem();
  private JMenuItem jPopupCopyMF = new JMenuItem();
  private JMenuItem jPopupUpMF = new JMenuItem();
  private JMenuItem jPopupDownMF = new JMenuItem();
  private JMenuItem jPopupRefinementMF = new JMenuItem();
  private JMenuItem jPopupScalingMFs = new JMenuItem();
  
  private JVariableFrame Parent;
  private String firstKey= null;
  private long firstKeyPressed= -1;

//------------------------------------------------------------------------------
  protected JAdvOptFrame(JVariableFrame parent) {
    super(parent);
    this.Parent= parent;
    JKBCTFrame.AddTranslatable(this);
  }
//------------------------------------------------------------------------------
  public JAdvOptFrame(JVariableFrame parent, JKBCT kbct, int number, JVariable Temp, String VariableType ) throws Throwable {
    super(parent);
    this.Parent= parent;
    this.kbct = kbct;
    this.Number = number;
    this.Temp = Temp;
    this.VariableType= VariableType;
    if (this.VariableType.equals("Input"))
      this.Semaphore = this.kbct.GetInputSemaphore(this.Number);
    else if (this.VariableType.equals("Output"))
      this.Semaphore = this.kbct.GetOutputSemaphore(this.Number);

    JKBCTFrame.AddTranslatable(this);
  }
//------------------------------------------------------------------------------
  public int print(java.awt.Graphics g, PageFormat pageFormat) throws PrinterException {
    java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
    double scalew = 1;
    double scaleh = 1;
    double pageHeight = pageFormat.getImageableHeight();
    double pageWidth = pageFormat.getImageableWidth();
    if (getWidth() >= pageWidth)
      scalew = pageWidth / getWidth();

    if (getHeight() >= pageHeight)
      scaleh = pageWidth / getHeight();

    g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
    g2.scale(scalew, scaleh);
    this.PanelInput.print(g2);
    return Printable.PAGE_EXISTS;
  }
//------------------------------------------------------------------------------
  protected void InitComponents() {
    this.getContentPane().setLayout(new GridBagLayout());
    this.getContentPane().add(this.PanelInput, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  }
//------------------------------------------------------------------------------
  protected void jbInit() throws Throwable {
    // menus
    this.setJMenuBar(this.jMenuBarInput);
    // menu MFs
    jMenuBarInput.add(jMenuMFs);
      jMenuMFs.add(jMenuNewMF);
      jMenuMFs.add(jMenuRemoveMF);
      jMenuMFs.add(jMenuMergeMFs);
      jMenuMFs.add(jMenuCopyMF);
      jMenuMFs.add(jMenuUpMF);
      jMenuMFs.add(jMenuDownMF);
      jMenuMFs.add(jMenuRefinementMF);
      jMenuMFs.addSeparator();
      jMenuMFs.add(jMenuScalingMFs);
    // menu print
    jMenuBarInput.add(jMenuPrint);
    // menu export
    jMenuBarInput.add(jMenuExport);
    // menu reset zoom
    jMenuBarInput.add(this.jMenuDisplayResetZoom);
    // menu help
    jMenuBarInput.add(jMenuHelp);
    // menu close
    jMenuBarInput.add(jMenuClose);
    // popup MFs
    jPopupMFs.add(jPopupNewMF);
    jPopupMFs.add(jPopupRemoveMF);
    jPopupMFs.add(jPopupGroupMF);
    jPopupMFs.add(jPopupCopyMF);
    jPopupMFs.add(jPopupUpMF);
    jPopupMFs.add(jPopupDownMF);
    jPopupMFs.add(jPopupRefinementMF);
    jPopupMFs.addSeparator();
    jPopupMFs.add(jPopupScalingMFs);

    this.InitComponents();
    titledBorderMFs = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(134, 134, 134)), "");
    jPanelMFs.setBorder(titledBorderMFs);
    this.AddPanelMFs();
    jGraphMFs= new JMFsPanel(this.Temp);
    jPanelMFs.add(jGraphMFs, new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    JAdvOptFrame.colors = this.jGraphMFs.getColors();
    titledBorderListMF = new TitledBorder(BorderFactory.createEtchedBorder(
        Color.white, new Color(134, 134, 134)), "");
    jScrollPaneListMF.setBorder(titledBorderListMF);
    jPanelMFs.add(jScrollPaneListMF, new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0
        , GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 20, 0));
    jListMF.setBackground(Color.white);
    jListMF.setCellRenderer(new DefaultListCellRenderer() {
   	  static final long serialVersionUID=0;	
      public Component getListCellRendererComponent(JList clist, Object value,
        int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(clist, value, index, isSelected, cellHasFocus);
        try {
          if (! JAdvOptFrame.this.Temp.GetScaleName().equals("user"))
            super.setText(LocaleKBCT.GetString(JAdvOptFrame.this.Temp.GetLabelsName(index)));
          else if (! JAdvOptFrame.this.Temp.GetUserLabelsName(index).equals(""))
            super.setText(JAdvOptFrame.this.Temp.GetUserLabelsName(index));
          else
            super.setText(LocaleKBCT.GetString("Label")+String.valueOf(index + 1));

          if (index < JAdvOptFrame.colors.length)
            super.setForeground(JAdvOptFrame.colors[index]);
          else {
        	  int lim= JAdvOptFrame.colors.length;
        	  int ind= index/lim;
              super.setForeground(JAdvOptFrame.colors[index-ind*lim]);
              //super.setForeground(Color.black);
          }
          if (isSelected == true) {
            SELECTED_LABEL= index+1;
            super.setBackground(Color.lightGray);
            super.setBorder(BorderFactory.createLineBorder(Color.black, 1));
          }
        }
        catch (Throwable e) {}
        return this;
      }
    });
    jListMF.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    this.jPanelMFParameters.setEnabled(false);
    titledBorderMFParameters = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(134, 134, 134)), "");
    this.jScrollPaneMFParameters.setBorder(titledBorderMFParameters);
    jPValidParameter.add(jBApplyParameter, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 0, 0));
    jPValidParameter.add(jBCancelParameter, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 0, 0));
    jPanelMFParameters.add(jPValidParameter, new GridBagConstraints(0, 3, 6, 1, 0.0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jPanelMFParameters.add(jPType, new GridBagConstraints(0, 0, 6, 1, 0.0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jPType.add(jLName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    jPType.add(jTFMFName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 75, 0));
    jPType.add(jLType, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            , GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    for (int i = 0; i < 4; i++)
      this.jCBMFTypeModel.addElement(new String());

    jPType.add(jCBMFType, new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
            , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
    jPanelMFParameters.add(this.jPanelMFParams, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.HideMFParameters();
  }
//------------------------------------------------------------------------------
  protected void AddPanelMFs() {
    this.PanelInput.add(this.jPanelMFs, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
         , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));
    jPanelMFs.add(this.jScrollPaneMFParameters, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
         , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  }
//------------------------------------------------------------------------------
  protected void RemovePanelMFs() { this.PanelInput.remove(this.jPanelMFs); }
//------------------------------------------------------------------------------
  private void AddPanelMFParameter() {
    this.PanelInput.add(this.jScrollPaneMFParameters, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
         , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));
  }
//------------------------------------------------------------------------------
  private void RemovePanelMFParameter() { this.PanelInput.remove(this.jScrollPaneMFParameters); }
//------------------------------------------------------------------------------
  public void Show() throws Throwable {
    jbInit();
    InitObjectsWithTemp();
    Translate();
    Events();
    if (this.Temp.GetLabelsNumber() != 0)
      this.jListMF.setSelectedIndex(0);
    else
      this.RemovePanelMFs();

    this.pack();
    if (this.Semaphore != null)
      this.Semaphore.Acquire();

    this.setVisible(true);
  }
//------------------------------------------------------------------------------
  protected void TranslateComponents() {
    this.setTitle(LocaleKBCT.GetString("AdvOpt")+": "+this.Temp.GetName());
  }
//------------------------------------------------------------------------------
  public void Translate() throws Throwable {
    // menu MFs
    jMenuMFs.setText(LocaleKBCT.GetString("MFs"));
      jMenuNewMF.setText(LocaleKBCT.GetString("NewMF"));
      jMenuRemoveMF.setText(LocaleKBCT.GetString("RemoveMF"));
      jMenuMergeMFs.setText(LocaleKBCT.GetString("MergeMFs"));
      jMenuCopyMF.setText(LocaleKBCT.GetString("CopyMF"));
      jMenuUpMF.setText(LocaleKBCT.GetString("UpMF"));
      jMenuDownMF.setText(LocaleKBCT.GetString("DownMF"));
      jMenuRefinementMF.setText(LocaleKBCT.GetString("RefinementMF"));
      jMenuScalingMFs.setText(LocaleKBCT.GetString("ScalingMFs"));
    // menu print
    jMenuPrint.setText(LocaleKBCT.GetString("Print"));
    // menu export
    jMenuExport.setText(LocaleKBCT.GetString("Export"));
    // menu reset zoom
    jMenuDisplayResetZoom.setText(LocaleKBCT.GetString("ResetZoom"));
    // menu close
    jMenuClose.setText(LocaleKBCT.GetString("Close"));
    // menu help
    jMenuHelp.setText(LocaleKBCT.GetString("Help"));
    // popup MFs
    jPopupNewMF.setText(LocaleKBCT.GetString("NewMF"));
    jPopupRemoveMF.setText(LocaleKBCT.GetString("RemoveMF"));
    jPopupGroupMF.setText(LocaleKBCT.GetString("MergeMFs"));
    jPopupCopyMF.setText(LocaleKBCT.GetString("CopyMF"));
    jPopupUpMF.setText(LocaleKBCT.GetString("UpMF"));
    jPopupDownMF.setText(LocaleKBCT.GetString("DownMF"));
    jPopupRefinementMF.setText(LocaleKBCT.GetString("RefinementMF"));
    jPopupScalingMFs.setText(LocaleKBCT.GetString("ScalingMFs"));

    this.TranslateComponents();
    titledBorderMFs.setTitle(LocaleKBCT.GetString("MFs"));
    titledBorderListMF.setTitle(LocaleKBCT.GetString("MF"));
    titledBorderMFParameters.setTitle(LocaleKBCT.GetString("MFParameters"));
    jBApplyParameter.setText(LocaleKBCT.GetString("Apply"));
    jBCancelParameter.setText(LocaleKBCT.GetString("Cancel"));
    jLType.setText(LocaleKBCT.GetString("Type") + ":");
    jLName.setText(LocaleKBCT.GetString("Name") + ":");
    if (this.MFParams != null) {
      //System.out.println("Translate: MFParams");
      this.MFParams.Translate();
      double MP= this.MFParams.GetMP();
      double[] params = this.MFParams.GetParams();
      this.jCBMFType.setSelectedIndex(this.MFParams.Number());
      this.MFParams.SetMP(MP);
      this.MFParams.SetParams(params);
    }
  }
//------------------------------------------------------------------------------
  protected void InitObjectsWithTemp() throws Throwable {
    this.jListMFModel.removeAllElements();
    for (int i = 0; i < this.Temp.GetLabelsNumber(); i++)
      this.jListMFModel.addElement(new String());

    this.jGraphMFs.SetInput(this.Temp);
    this.jGraphMFs.repaint();
  }
//------------------------------------------------------------------------------
  protected void Events() {
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) { this_windowClosing(); }
      public void windowActivated(WindowEvent e) { this_windowActivated(); }
    });
    jListMF.addKeyListener(new KeyListener() {
        public void keyPressed(KeyEvent e) { jListMF_keyPressed(e);}
        public void keyReleased(KeyEvent e) { }
        public void keyTyped(KeyEvent e) { }
    });
    jMenuMFs.addMenuListener(new MenuListener() {
      public void menuSelected(MenuEvent e) { jMenuMFs_menuSelected(e); }
      public void menuDeselected(MenuEvent e) {}
      public void menuCanceled(MenuEvent e) {}
    });
    jMenuNewMF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuNewMF_actionPerformed(e); } });
    jMenuRemoveMF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuRemoveMF_actionPerformed(e); } });
    jMenuMergeMFs.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuMergeMFs_actionPerformed(); } });
    jMenuCopyMF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuCopyMF_actionPerformed(); } });
    jMenuUpMF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuUpMF_actionPerformed(); } });
    jMenuDownMF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuDownMF_actionPerformed(); } });
    jMenuRefinementMF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuRefinementMF_actionPerformed(); } });
    jMenuScalingMFs.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuScalingMFs_actionPerformed(e); } });
    // popups
    jPopupNewMF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupNewMF_actionPerformed(e); } });
    jPopupRemoveMF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupRemoveMF_actionPerformed(e); } });
    jPopupGroupMF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupGroupMF_actionPerformed(); } });
    jPopupCopyMF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupCopyMF_actionPerformed(); } });
    jPopupUpMF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupUpMF_actionPerformed(); } });
    jPopupDownMF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupDownMF_actionPerformed(); } });
    jPopupRefinementMF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupRefinementMF_actionPerformed(); } });
    jPopupScalingMFs.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupScalingMFs_actionPerformed(e); } });
    jMenuPrint.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuPrint_actionPerformed(); } });
    jMenuExport.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExport_actionPerformed(); } });
    this.jMenuDisplayResetZoom.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         try {
          double Xrange[]= JAdvOptFrame.this.jGraphMFs.GetInput().GetInputInterestRange();
          JAdvOptFrame.this.jGraphMFs.setXRange(Xrange[0], Xrange[1]);
          JAdvOptFrame.this.jGraphMFs.setYRange(0,1);
          JAdvOptFrame.this.jGraphMFs.repaint();
         } catch( Throwable t ) { MessageKBCT.Error(null, t); }
       }
     });
    jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuClose_actionPerformed(); } });
    jMenuHelp.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuHelp_actionPerformed(); } });
    jCBMFType.addItemListener(new ItemListener() { public void itemStateChanged(ItemEvent e) { jCBMFType_itemStateChanged(e); } });
    jBApplyParameter.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jBApplyParameter_actionPerformed(e); } });
    jBCancelParameter.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jBCancelParameter_actionPerformed(e); } });
    jListMF.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e) { jListMF_mousePressed(e); }});
    jListMF.addListSelectionListener(new ListSelectionListener() { public void valueChanged(ListSelectionEvent e) { jListMF_valueChanged(e); } });
    this.KBCTListener = new KBCTListener() {
      public void KBCTClosed() { JAdvOptFrame.this.dispose(); }
      public void InputRemoved(int input_number) {}
      public void OutputRemoved(int output_number) {}
      public void InputAdded(int input_number) {}
      public void OutputAdded(int output_number) {}
      public void InputReplaced(int input_number) {}
      public void OutputReplaced(int output_number) {}
      public void InputActiveChanged(int input_number) {}
      public void OutputActiveChanged(int output_number) {}
      public void InputNameChanged(int input_number) {}
      public void OutputNameChanged(int output_number) {}
      public void InputPhysicalRangeChanged(int input_number) {}
      public void InputInterestRangeChanged(int input_number) {}
      public void OutputPhysicalRangeChanged(int output_number) {}
      public void OutputInterestRangeChanged(int output_number) {}
      public void MFRemovedInInput(int input_number, int mf_number) {}
      public void MFRemovedInOutput(int output_number, int mf_number) {}
      public void MFAddedInInput(int input_number) {}
      public void MFAddedInOutput(int output_number) {}
      public void MFReplacedInInput(int input_number) {}
      public void MFReplacedInOutput(int output_number) {}
      public void OutputDefaultChanged(int output_number) {}
      public void RulesModified() {}
    };
    this.kbct.AddKBCTListener(this.KBCTListener);
  }
//------------------------------------------------------------------------------
  void jMenuClose_actionPerformed() { this.dispose(); }
//------------------------------------------------------------------------------
  void this_windowClosing() { this.dispose(); }
//------------------------------------------------------------------------------
  void jMenuHelp_actionPerformed() { 
	if (this.Parent.output)
	    MainKBCT.setJB(new JBeginnerFrame("expert/ExpertMenuOutputs.html#AdvOpt"));
	else
	    MainKBCT.setJB(new JBeginnerFrame("expert/ExpertMenuInputs.html#AdvOpt"));
		
	MainKBCT.getJB().setVisible(true);
	SwingUtilities.updateComponentTreeUI(this);
  }
//------------------------------------------------------------------------------
  void this_windowActivated() { this.repaint(); }
//------------------------------------------------------------------------------
  void jListMF_keyPressed(KeyEvent e) {
    if( this.jListMF.isEnabled() == false )
      return;

    int[] lab= this.jListMF.getSelectedIndices(); 
    int L= lab.length;
    if(L > 0) {
    	if (this.firstKey==null) {
    		this.firstKey= ""+KeyEvent.VK_CONTROL;
    		this.firstKeyPressed= System.currentTimeMillis();
    	} else {
    		boolean warning= false;
    		long tt=-1;
            if (this.firstKey.equals(""+KeyEvent.VK_CONTROL)) {
            	if (this.firstKeyPressed!=-1) {
            		tt= System.currentTimeMillis();
            		//System.out.println(tt-this.firstKeyPressed);
            		if (tt-this.firstKeyPressed>300) {
            			warning=true;
            		}
            	} else {
            		warning= true;
            	}
            }
    		if (!warning) {
                if (e.getKeyCode()==KeyEvent.VK_DELETE) {
                	// Remove Label
                    this.RemoveMF();
                } else if (e.getKeyCode()==KeyEvent.VK_N) {
                	// New Label
                         if ( (this.Temp.GetScaleName().equals("user")) || (this.Temp.GetLabelsNumber() <= 8) )
                             this.NewMF();
                } else if (e.getKeyCode()==KeyEvent.VK_M) {
                	// Merge Labels
                         if ( (L > 1) && (L < this.Temp.GetLabelsNumber()) )
                             this.GroupMFs();
                } else if (e.getKeyCode()==KeyEvent.VK_C) {
                	// Copy Labels
                	//System.out.println("lab[0]="+lab[0]);
                         if (L == 1) 
                             this.CopyMF();
                } else if (e.getKeyCode()==KeyEvent.VK_U) {
                	// Up Labels
                	//System.out.println("lab[0]="+lab[0]);
                         if ( (L == 1) && (lab[0]>0) )
                             this.UpMF();
                } else if (e.getKeyCode()==KeyEvent.VK_D) {
                	// Down Labels
                	//System.out.println("lab[0]="+lab[0]);
                         if ( (L == 1) && (lab[0]<this.Temp.GetLabelsNumber()-1) )
                             this.DownMF();
                } else if (e.getKeyCode()==KeyEvent.VK_R) {
                	// Refinement
                    this.Refinement();
                } else if (e.getKeyCode()==KeyEvent.VK_S) {
                	// Scalling MFs
                    this.ScalingMFs();
                }
                this.firstKey= null;
    	    } else {
        		this.firstKeyPressed= tt;
    	    }
    	}
    }
  }
//------------------------------------------------------------------------------
  public void dispose() {
    super.dispose();
    if (this.Semaphore != null)
      this.Semaphore.Release();
    if (this.KBCTListener != null)
      this.kbct.RemoveKBCTListener(this.KBCTListener);

    JKBCTFrame.RemoveTranslatable(this);
  }
//------------------------------------------------------------------------------
  void jCBMFType_itemStateChanged(ItemEvent e) {
    if( e.getStateChange() == ItemEvent.SELECTED ) {
        try {
          switch (this.jCBMFType.getSelectedIndex()) {
            case LabelKBCT.TRIANGULAR:
              this.MFParams= new MFTriangularParams(this.jPanelMFParams, this.Temp);
              break;
            case LabelKBCT.TRAPEZOIDAL:
              this.MFParams= new MFTrapezoidalParams(this.jPanelMFParams, this.Temp);
              break;
            case LabelKBCT.SEMITRAPEZOIDALINF:
              this.MFParams= new MFSemiTrapezoidalInfParams(this.jPanelMFParams, this.Temp);
              break;
            case LabelKBCT.SEMITRAPEZOIDALSUP:
              this.MFParams= new MFSemiTrapezoidalSupParams(this.jPanelMFParams, this.Temp);
              break;
            case LabelKBCT.DISCRETE:
              this.MFParams= new MFDiscreteParams(this.jPanelMFParams, this.Temp);
              break;
          }
          this.jPanelMFParameters.setEnabled(true);
          this.jPanelMFParameters.revalidate();
        }
        catch (Throwable t) {
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JAdvOptFrame in jCBMFType_itemStateChanged: "+t);
        }
      }
  }
//------------------------------------------------------------------------------
  private void HideMFParameters() {
    this.NewMF = false;
    this.jTFMFName.setText("");
    this.jCBMFType.setSelectedIndex(-1);
    this.MFParams = null;
    this.jPanelMFParams.removeAll();
    this.jPanelMFParameters.revalidate();
    this.jPanelMFParameters.setEnabled(false);
    this.jListMF.clearSelection();
    this.repaint();
  }
//------------------------------------------------------------------------------
  private void ShowNewMFParameters() throws Throwable {
    this.jListMF.clearSelection();
    this.NewMF = true;
    this.jTFMFName.setText(LocaleKBCT.GetString("Label") +" "+ String.valueOf(this.Temp.GetLabelsNumber() + 1));
    this.jTFMFName.setEnabled(true);
    this.jCBMFType.setSelectedIndex(0);
    if (this.Temp.GetLabelsNumber() == 0) {
      this.AddPanelMFParameter();
      this.pack();
    }
    this.validate();
  }
//------------------------------------------------------------------------------
  private void ShowNewMFParameters(String name) throws Throwable {
    this.jListMF.clearSelection();
    this.NewMF = true;
    this.jTFMFName.setText(name);
    this.jTFMFName.setEnabled(true);
    this.validate();
  }
//------------------------------------------------------------------------------
  private void NewMF() {
    try { this.ShowNewMFParameters(); }
    catch( Throwable t ) {
      this.HideMFParameters();
      MessageKBCT.Error( null, t );
    }
  }
//------------------------------------------------------------------------------
  void jMenuNewMF_actionPerformed(ActionEvent e) { this.NewMF(); }
//------------------------------------------------------------------------------
  void jPopupNewMF_actionPerformed(ActionEvent e) { this.NewMF(); }
//------------------------------------------------------------------------------
  void jBCancelParameter_actionPerformed(ActionEvent e) {
    this.HideMFParameters();
    try {
      if (this.Temp.GetLabelsNumber() == 0) {
        this.RemovePanelMFParameter();
        this.pack();
      }
    } catch (Throwable t) {
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JAdvOptFrame in jBCancelParameter_actionPerformed: "+t);
    }
  }
//------------------------------------------------------------------------------
  void jBApplyParameter_actionPerformed(ActionEvent e) {
    if (this.jTFMFName.getText().equals("")) {
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"),
                       LocaleKBCT.GetString("EnterAName"));
      return;
    }
    LabelKBCT newMF = null;
    try {
      if( this.NewMF ) {
        boolean pack = false;
        if( this.Temp.GetLabelsNumber() == 0 )
          pack = true;

        //System.out.println("apply1");
        newMF= this.SelectNewMF(this.jCBMFType.getSelectedIndex(), this.Temp.GetLabelsNumber()+1);
        //System.out.println("apply2");
        this.Temp.SetUserLabelsName(newMF.GetLabel_Number(), this.jTFMFName.getText());
        //System.out.println("apply3");
        this.Temp.SetScaleName("user");
        //System.out.println("apply4");
        this.AddMF(newMF);
        //System.out.println("apply5");
        this.InitObjectsWithTemp();
        //System.out.println("apply6");
        if( pack == true ) {
          this.RemovePanelMFParameter();
          this.AddPanelMFs();
          this.pack();
        }
        this.Number= this.Number+2;
        this.Temp.SetORLabelsName();
        if (this.VariableType.equals("Input"))
            this.kbct.ReplaceInput(this.Number, this.Temp);
        else if (this.VariableType.equals("Output"))
            this.kbct.ReplaceOutput(this.Number, this.Temp);
      } else {
        LabelKBCT et= this.Temp.GetLabel(this.SELECTED_LABEL);
        String MP= et.GetMP();
        double newMP= this.MFParams.GetMP();
        newMF= this.SelectNewMF(this.jCBMFType.getSelectedIndex(), this.SELECTED_LABEL);
        if ( (newMP==0) && (MP.equals("No MP")) )
          newMF.SetMP(MP);
        else {
          double[] par= et.GetParams();
          if ( (par.length==3) && (newMP >= (par[0]+par[1])/2) && (newMP <= (par[1]+par[2])/2) ) {
              newMF.SetMP("" + newMP);
              newMF.SetP2(newMP);
              if (this.SELECTED_LABEL > 1) {
                LabelKBCT ant= this.Temp.GetLabel(this.SELECTED_LABEL-1);
                if (ant.GetParams().length==3)
                    ant.SetP3(newMP);
                else 
                    ant.SetP4(newMP);
              }
              if (this.SELECTED_LABEL < this.Temp.GetLabelsNumber()) {
                LabelKBCT pos= this.Temp.GetLabel(this.SELECTED_LABEL+1);
                pos.SetP1(newMP);
              }
          } else if ( (par.length==4) && (newMP >= (par[0]+par[1])/2) && (newMP <= (par[2]+par[3])/2) ) {
            newMF.SetMP("" + newMP);
          } else {
            newMF.SetMP(MP);
            String msg;
            if (par.length==3)
                msg= LocaleKBCT.GetString("MPmustBeIncludedIntoSupport")+"\n"
                        +"   "+String.valueOf((par[0]+par[1])/2)+" <= "+LocaleKBCT.GetString("MP")+" <= "+String.valueOf((par[1]+par[2])/2);
            else
                msg= LocaleKBCT.GetString("MPmustBeIncludedIntoSupport")+"\n"
                        +"   "+String.valueOf((par[0]+par[1])/2)+" <= "+LocaleKBCT.GetString("MP")+" <= "+String.valueOf((par[2]+par[3])/2);

            MessageKBCT.Warning(this, LocaleKBCT.GetString("WARNING"), msg);
          }
        }
        this.ReplaceMF(this.jListMF.getSelectedIndex()+1, newMF);
        if (this.Temp.GetScaleName().equals("user")) {
          this.Temp.SetUserLabelsName(newMF.GetLabel_Number(), this.jTFMFName.getText());
          this.Temp.SetORLabelsName();
        }
      }
    } catch (Throwable t) {
        t.printStackTrace();
    	MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JAdvOptFrame in jBApplyParameter_actionPerformed: "+t);
        return;
    }
    this.HideMFParameters();
  }
//------------------------------------------------------------------------------
  protected LabelKBCT SelectNewMF(int n, int LN) {
    LabelKBCT newMF=null;
    switch (n) {
      case LabelKBCT.TRIANGULAR:
        newMF= new LabelKBCT("triangular", this.MFParams.GetParams()[0], this.MFParams.GetParams()[1], this.MFParams.GetParams()[2], LN);
        break;
      case LabelKBCT.TRAPEZOIDAL:
        newMF= new LabelKBCT("trapezoidal", this.MFParams.GetParams()[0], this.MFParams.GetParams()[1], this.MFParams.GetParams()[2], this.MFParams.GetParams()[3], LN);
        break;
      case LabelKBCT.SEMITRAPEZOIDALINF:
        newMF= new LabelKBCT("SemiTrapezoidalInf", this.MFParams.GetParams()[0], this.MFParams.GetParams()[1], this.MFParams.GetParams()[2], LN);
        break;
      case LabelKBCT.SEMITRAPEZOIDALSUP:
        newMF= new LabelKBCT("SemiTrapezoidalSup", this.MFParams.GetParams()[0], this.MFParams.GetParams()[1], this.MFParams.GetParams()[2], LN);
        break;
      case LabelKBCT.DISCRETE:
        newMF= new LabelKBCT("discrete", this.MFParams.GetParams()[0], LN);
        break;
    }
    return newMF;
  }
//------------------------------------------------------------------------------
  protected void ReplaceMF(int mf_number, LabelKBCT new_e) throws Throwable {
    this.Temp.ReplaceLabel(mf_number, new_e);
    this.Temp.SetORLabelsName();
  }
//------------------------------------------------------------------------------
  protected void AddMF( LabelKBCT new_e ) throws Throwable {
    this.Temp.AddLabel(new_e);
    this.Temp.SetORLabelsName();
    if (this.VariableType.equals("Input"))
      this.kbct.MFAddedInInput(new_e.GetLabel_Number());
    else if (this.VariableType.equals("Output"))
      this.kbct.MFAddedInOutput(new_e.GetLabel_Number());
  }
//------------------------------------------------------------------------------
  void jListMF_valueChanged(ListSelectionEvent e) {
    try {
      if (this.jListMF.getSelectedIndex() != -1) {
        if ( this.Temp.GetScaleName().equals("user"))
          this.jTFMFName.setText(this.Temp.GetUserLabelsName(this.jListMF.getSelectedIndex()));
        else if (! this.Temp.GetLabelsName(this.jListMF.getSelectedIndex()).equals(""))
          this.jTFMFName.setText(LocaleKBCT.GetString(this.Temp.GetLabelsName(this.jListMF.getSelectedIndex())));
        else
          this.jTFMFName.setText(LocaleKBCT.GetString("Label")+" "+String.valueOf(this.jListMF.getSelectedIndex()+1));

        LabelKBCT et = this.Temp.GetLabel(this.jListMF.getSelectedIndex()+1);
        this.jCBMFType.setSelectedIndex(et.GetLabelType_Number());
        this.jPanelMFParameters.setEnabled(true);
        String MP= et.GetMP();
        if (!MP.equals("No MP"))
          this.MFParams.SetMP((new Double(MP)).doubleValue());
        else
          this.MFParams.SetDefinedMP(false);

        if (et.GetParams().length==4) {
           String MPaux= et.GetMPaux();
           if (!MPaux.equals("No MP"))
               this.MFParams.SetMPaux((new Double(MPaux)).doubleValue());
           else
               this.MFParams.SetDefinedMPaux(false);
        }
        this.MFParams.SetParams(et.GetParams());
      }
    } catch (Throwable t) {
        this.HideMFParameters();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JAdvOptFrame in jListMF_valueChanged: "+t);
    }
  }
//------------------------------------------------------------------------------
  protected void Replace(JVariable new_input) throws Throwable {
    this.kbct.ReplaceInput(this.Number, (JKBCTInput)new_input);
  }
//------------------------------------------------------------------------------
  private void RemoveMF() {
    try {
      this.RemoveMF(this.jListMF.getSelectedIndex(), true);
      this.InitObjectsWithTemp();
      this.HideMFParameters();
      if( this.Temp.GetLabelsNumber() != 0 )
        this.jListMF.setSelectedIndex(0);
      else {
        this.RemovePanelMFs();
        this.pack();
      }
    } catch( Throwable t ) { MessageKBCT.Error( null, t ); }
  }
//------------------------------------------------------------------------------
  protected void RemoveMF( int mf_number, boolean expand ) throws Throwable {
    this.Temp.RemoveLabel(mf_number, expand);
    this.Temp.SetORLabelsName();
    if (this.VariableType.equals("Input")) {
      Object[] listeners = this.kbct.GetListeners().toArray();
      for (int i = 0; i < listeners.length; i++)
        ( (KBCTListener) listeners[i]).MFRemovedInInput(this.Number+1, mf_number);

      this.kbct.ReplaceInput(this.Number+2, this.Temp);
    } else if (this.VariableType.equals("Output")) {
      Object[] listeners = this.kbct.GetListeners().toArray();
      for (int i = 0; i < listeners.length; i++)
        ( (KBCTListener) listeners[i]).MFRemovedInOutput(this.Number+1, mf_number);

      this.kbct.ReplaceOutput(this.Number+2, this.Temp);
    }
  }
//------------------------------------------------------------------------------
  void jPopupRemoveMF_actionPerformed(ActionEvent e) { this.RemoveMF(); }
//------------------------------------------------------------------------------
  void jMenuRemoveMF_actionPerformed(ActionEvent e) { this.RemoveMF(); }
//------------------------------------------------------------------------------
  void jPopupScalingMFs_actionPerformed(ActionEvent e) { this.ScalingMFs(); }
//------------------------------------------------------------------------------
  void jMenuScalingMFs_actionPerformed(ActionEvent e) { this.ScalingMFs(); }
//------------------------------------------------------------------------------
  void ScalingMFs() {
      JScalingMFsFrame jsmf = new JScalingMFsFrame(this, this.Temp, "ScalingMFs");
      jsmf.setLocation(JChildFrame.ChildPosition(this, jsmf.getSize()));
      double[] res= jsmf.Show();
      //for (int n=0; n<res.length; n++) {
      //  System.out.println("res["+n+"]="+res[n]);
      //}
      double min= res[0];
      double C= res[1];
      double max= res[2];
      double A= res[3];
      double B= res[4];
      Vector vertex= new Vector();
      int NbLabels= this.Temp.GetLabelsNumber();
      for (int n=0; n<NbLabels; n++) {
    	   double[] params= this.Temp.GetLabel(n+1).GetParams();
    	   if (params.length==1)
    		   vertex.add(params[0]);
    	   else if (params.length==3)
    		   vertex.add(params[1]);
    	   else if (params.length==4) {
    		   vertex.add(params[1]);
    		   vertex.add(params[2]);
    	   }
      }
      Object[] MPold= vertex.toArray();
      //for (int n=0; n<MPold.length; n++) {
      //     System.out.println("mp["+n+"]="+MPold[n]);
      //}
      double[] MP= new double[MPold.length];
      int lim=MP.length;
      for (int n=0; n<lim; n++) {
           double mpold= (new Double(MPold[n].toString())).doubleValue();
           if ( (mpold <= min) || (mpold >= max) ) {
        	   MP[n]=mpold;
           } else if (mpold < C) {
    		   MP[n]= Math.max(min,C - this.scaleFunctionA(min,C,A,mpold)*(C-min));
           } else if (mpold > C) {
        	   MP[n]= Math.min(max,C + this.scaleFunctionB(max,C,B,mpold)*(max-C));
           } else {
         	   MP[n]=C; 
           }
    	   //System.out.println("mp["+n+"]="+MP[n]);
      }
      if (MP != null) {
      	int NOL= this.Temp.GetLabelsNumber();
          int[] NOMPlab= new int[NOL];
          for (int n=0; n<NOL; n++) {
          	   int nbpar= this.Temp.GetLabel(n+1).GetParams().length;
          	   if (nbpar==4) {
          		   NOMPlab[n]= 2;
          	   } else {
          		   NOMPlab[n]= 1;
          	   }
          }
          DecimalFormat df= new DecimalFormat();
          df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
          df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
          DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
          dfs.setDecimalSeparator((new String(".").charAt(0)));
          df.setDecimalFormatSymbols(dfs);
          df.setGroupingSize(20);
          int cont=0;
          for (int n=0; n<NOL; n++) {
          	if (NOMPlab[n]==1) {
                  String aux="No MP";
                  if (MP[cont] != this.Temp.GetInputInterestRange()[0]-1)
                      aux = df.format(new Double(MP[cont]));

                  if (MP[cont] >= this.Temp.GetInputInterestRange()[0]) {
                      this.Temp.SetMP(n+1, aux, false);
                      variable var= this.Temp.GetV();
        	            LabelKBCT et= var.GetLabel(n+1);
        	            if (et.GetParams().length==3) {
            	            et.SetP2(MP[cont]);
                          var.ReplaceLabel(n+1,et);
                          if (n==0) {
            	                LabelKBCT post= var.GetLabel(2);
             	                post.SetP1(MP[cont]);
                              var.ReplaceLabel(2,post);
                          } else if (n==NOL-1) {
          	                LabelKBCT ant= var.GetLabel(n);
                              if (ant.GetParams().length==3)
            	                    ant.SetP3(MP[cont]);
                              else
              	                ant.SetP4(MP[cont]);

                              var.ReplaceLabel(n,ant);
                          } else {
          	                LabelKBCT ant= var.GetLabel(n);
                              if (ant.GetParams().length==3)
              	                ant.SetP3(MP[cont]);
                              else
             	                    ant.SetP4(MP[cont]);

                              var.ReplaceLabel(n,ant);
          	                LabelKBCT post= var.GetLabel(n+2);
          	                post.SetP1(MP[cont]);
                              var.ReplaceLabel(n+2,post);
                          }
        	            }
                      this.Temp.SetV(var);
                 } else {
                       this.Temp.SetMP(n+1, aux, false);
                 }
          	} else {
                  for (int k=0; k<2; k++) {
          		  String aux="No MP";
                    if (MP[cont] != this.Temp.GetInputInterestRange()[0]-1)
                        aux = df.format(new Double(MP[cont]));

                    if (MP[cont] >= this.Temp.GetInputInterestRange()[0]) {
                        if (k==0)
                  	      this.Temp.SetMP(n+1, aux, false);
                        else
                  	      this.Temp.SetMPaux(n+1, aux);
                      	  
                        variable var= this.Temp.GetV();
        	              LabelKBCT et= var.GetLabel(n+1);
        	              if (k==0) {
            	              et.SetP2(MP[cont]);
                            var.ReplaceLabel(n+1,et);
                            if (n > 0) {
          	                  LabelKBCT ant= var.GetLabel(n);
                                if (ant.GetParams().length==3)
            	                      ant.SetP3(MP[cont]);
                                else
              	                  ant.SetP4(MP[cont]);

                                var.ReplaceLabel(n,ant);
                            }
        	              } else {
            	              et.SetP3(MP[cont]);
                            var.ReplaceLabel(n+1,et);
                            if (n < NOL-1) {
            	                  LabelKBCT post= var.GetLabel(n+2);
          	                  post.SetP1(MP[cont]);
                                var.ReplaceLabel(n+2,post);
                            }
        	              }
                        this.Temp.SetV(var);
                   } else {
                       this.Temp.SetMP(n+1, aux, false);
                   }
                   if (k==0)
                  	cont++;
          	   }
              }
          	cont++;
          }
        }
  }
//------------------------------------------------------------------------------
  private double scaleFunctionA(double min, double C, double A, double x) {
      double f= (C-x)/(min-C);
	  double res= Math.pow(Math.abs(f),A);
	  //System.out.println("resA="+res);
	  return res;  
  }
//------------------------------------------------------------------------------
  private double scaleFunctionB(double max, double C, double B, double x) {
      double f= (x-C)/(max-C);
	  double res= Math.pow(Math.abs(f),B);
	  //System.out.println("resB="+res);
	  return res;  
  }
//------------------------------------------------------------------------------
  void jMenuMFs_menuSelected(MenuEvent e) {
    int NbRows= this.Temp.GetLabelsNumber();
    if(this.jListMF.isSelectionEmpty() == false ) {
    	int[] sel= this.jListMF.getSelectedIndices();
    	int L= sel.length;
    	// Remove Labels
        if (NbRows - L >=2)
            this.jMenuRemoveMF.setEnabled(true);
    	else
            this.jMenuRemoveMF.setEnabled(false);

    	// Merge Labels
    	if ( (L>1) && (NbRows >= L+1) )
            this.jMenuMergeMFs.setEnabled(true);
    	else
            this.jMenuMergeMFs.setEnabled(false);
    		
    	// Copy Label
    	// Up Labels
    	// Down Labels
        if (L==1) {
            this.jMenuCopyMF.setEnabled(true);
        	if (sel[0] > 0)
                this.jMenuUpMF.setEnabled(true);
        	else
                this.jMenuUpMF.setEnabled(false);
        		
           	if (sel[0] < NbRows-1)
           	    this.jMenuDownMF.setEnabled(true);
           	else
                this.jMenuDownMF.setEnabled(false);
           		
        } else {
            this.jMenuCopyMF.setEnabled(false);
            this.jMenuUpMF.setEnabled(false);
            this.jMenuDownMF.setEnabled(false);
        }
    	// Refinement Labels
        this.jMenuRefinementMF.setEnabled(true);

    } else {
        this.jMenuRemoveMF.setEnabled(false);
        this.jMenuMergeMFs.setEnabled(false);
        this.jMenuCopyMF.setEnabled(false);
        this.jMenuUpMF.setEnabled(false);
        this.jMenuDownMF.setEnabled(false);
        this.jMenuRefinementMF.setEnabled(false);
    }
    if ( (this.Temp.GetScaleName().equals("user")) || (NbRows<9) )
      this.jMenuNewMF.setEnabled(true);
    else
      this.jMenuNewMF.setEnabled(false);
    
    this.jMenuScalingMFs.setEnabled(true);
  }
//------------------------------------------------------------------------------
  void jListMF_mousePressed(MouseEvent e) {
    if (SwingUtilities.isRightMouseButton(e)) {
      int NbRows= this.Temp.GetLabelsNumber();
      if (this.Temp.GetScaleName().equals("user"))
          this.jPopupNewMF.setEnabled(true);
      else {    	  
          if (NbRows==9)
              this.jPopupNewMF.setEnabled(false);
          else
              this.jPopupNewMF.setEnabled(true);
      }
      int L= this.jListMF.getSelectedIndices().length;
      if (NbRows - L >=2)
        this.jPopupRemoveMF.setEnabled(true);
      else
        this.jPopupRemoveMF.setEnabled(false);

      if (L > 1) {
        this.jPopupUpMF.setEnabled(false);
        this.jPopupDownMF.setEnabled(false);
        if (NbRows - L >= 1 ) {
          this.jPopupGroupMF.setEnabled(true);
        } else {
          this.jPopupGroupMF.setEnabled(false);
        }
        if ( (this.Temp.GetScaleName().equals("user")) || (L-1+NbRows<=9) )
          this.jPopupRefinementMF.setEnabled(true);
        else
          this.jPopupRefinementMF.setEnabled(false);

      } else {
        this.jPopupGroupMF.setEnabled(false);
        if (L==1) {
          if ( (this.Temp.GetScaleName().equals("user")) || (NbRows+1 <= 9) )
        	this.jPopupCopyMF.setEnabled(true);
          else
          	this.jPopupCopyMF.setEnabled(false);
        	  
          int SelInd= this.jListMF.getSelectedIndex();
          if (SelInd==0) {
              this.jPopupUpMF.setEnabled(false);
              this.jPopupDownMF.setEnabled(true);
          } else if (SelInd+1==this.Temp.GetLabelsNumber()) {
              this.jPopupUpMF.setEnabled(true);
              this.jPopupDownMF.setEnabled(false);
          } else {
              this.jPopupUpMF.setEnabled(true);
              this.jPopupDownMF.setEnabled(true);
          }
        if (NbRows<=9)
          this.jPopupRefinementMF.setEnabled(true);
        else
          this.jPopupRefinementMF.setEnabled(false);
        } else {
            this.jPopupCopyMF.setEnabled(false);
            this.jPopupUpMF.setEnabled(false);
            this.jPopupDownMF.setEnabled(false);
        }
      }
      SwingUtilities.updateComponentTreeUI(jPopupMFs);
      jPopupMFs.show(jListMF, e.getX(), e.getY());
    }
  }
//------------------------------------------------------------------------------
  void jMenuMergeMFs_actionPerformed() { this.GroupMFs(); }
//------------------------------------------------------------------------------
  void jPopupGroupMF_actionPerformed() { this.GroupMFs(); }
//------------------------------------------------------------------------------
  void GroupMFs() {
    int[] L= this.jListMF.getSelectedIndices();
    if (L.length>1) {
      int NbLabels= this.Temp.GetLabelsNumber();
      LabelKBCT e_ini= this.Temp.GetLabel(L[0]+1);
      LabelKBCT e_fin= this.Temp.GetLabel(L[L.length-1]+1);
      LabelKBCT e_group= JConvert.ORLabel(e_ini, e_fin, e_ini.GetLabel_Number(), NbLabels);
      this.Temp.ReplaceLabel(e_ini.GetLabel_Number(), e_group);
      String ScaleName= this.Temp.GetScaleName();
      String[] LabelsNames;
      if (ScaleName.equals("user"))
        LabelsNames= this.Temp.GetUserLabelsName();
      else
        LabelsNames= this.Temp.GetLabelsName();

      String e_group_Name;
      if (ScaleName.equals("user"))
          e_group_Name=LabelsNames[L[0]];
      else
          e_group_Name=LocaleKBCT.GetString(LabelsNames[L[0]]);

      for (int n=1; n<L.length; n++) {
        if (ScaleName.equals("user"))
          e_group_Name= "("+e_group_Name + ") "+LocaleKBCT.GetString("OR")+" ("+ LabelsNames[L[n]]+")";
        else
          e_group_Name= "("+e_group_Name + ") "+LocaleKBCT.GetString("OR")+" ("+ LocaleKBCT.GetString(LabelsNames[L[n]])+")";
      }
      for (int n=1; n<=NbLabels; n++) {
        if (n==e_ini.GetLabel_Number())
          this.Temp.SetUserLabelsName(n, e_group_Name);
        else {
          if (ScaleName.equals("user"))
            this.Temp.SetUserLabelsName(n, LabelsNames[n-1]);
          else
            this.Temp.SetUserLabelsName(n, LocaleKBCT.GetString(LabelsNames[n-1]));
        }
      }
      this.Temp.SetScaleName("user");
      this.Temp.SetLabelsName();
      this.Temp.SetORLabelsName();
      if (this.VariableType.equals("Input"))
        this.kbct.ReplaceInput(this.Number+2, this.Temp);
      else if (this.VariableType.equals("Output"))
        this.kbct.ReplaceOutput(this.Number+2, this.Temp);

      try {
        for (int n= 1; n<L.length; n++)
          this.RemoveMF(L[1], false);
      } catch( Throwable t ) { MessageKBCT.Error( null, t ); }
    }
  }
//------------------------------------------------------------------------------
  void jMenuPrint_actionPerformed() {
    try {
      Printable p= new Printable() {
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
          if (pageIndex >= 1)
              return Printable.NO_SUCH_PAGE;
          else
              return JAdvOptFrame.this.print(graphics, pageFormat);
        }
      };
      new JPrintPreview(this.Parent, p);
    } catch (Exception ex) { MessageKBCT.Error(null, ex); }
  }
//------------------------------------------------------------------------------
  void jMenuExport_actionPerformed() {
    try {
      ExportDialog export= new ExportDialog();
      JPanel panel = new JPanel() {
   	    static final long serialVersionUID=0;	
        public void paint(Graphics g) {
          JAdvOptFrame.this.PanelInput.paint(g);
          g.translate(0, JAdvOptFrame.this.PanelInput.getHeight());
          JAdvOptFrame.this.PanelInput.paint(g);
          g.setColor(Color.gray);
          g.drawLine(0,0,0, JAdvOptFrame.this.PanelInput.getHeight()-1);
        }
      };
      panel.setSize(new Dimension(this.PanelInput.getWidth(), this.PanelInput.getHeight()));
      export.showExportDialog( panel, "Export view as ...", panel, "export" );
    } catch (Exception ex) { MessageKBCT.Error(null, ex); }
  }
//------------------------------------------------------------------------------
  void jMenuUpMF_actionPerformed() { 
	  this.UpMF(); 
  }
//------------------------------------------------------------------------------
  void jPopupUpMF_actionPerformed() {
	  this.UpMF(); 
  }
//------------------------------------------------------------------------------
  void UpMF() {
	    int LabelNumber= this.jListMF.getSelectedIndex()+1;
	    String ScaleName= this.Temp.GetScaleName();
	    String[] aux;
	    if (ScaleName.equals("user"))
	      aux= this.Temp.GetUserLabelsName();
	    else
	      aux= this.Temp.GetLabelsName();

	    int NbLabels= this.Temp.GetLabelsNumber();
	    String[] LabelsNames= new String[NbLabels];
	    for (int n=0; n<NbLabels; n++)
	      LabelsNames[n]= aux[n];

	    for (int n=1; n<=NbLabels; n++) {
	      if (n==LabelNumber-1)
	          this.Temp.SetUserLabelsName(n, LabelsNames[n]);
	      else if (n==LabelNumber)
	          this.Temp.SetUserLabelsName(n, LabelsNames[n-2]);
	      else {
	          if (ScaleName.equals("user"))
	            this.Temp.SetUserLabelsName(n, LabelsNames[n-1]);
	          else
	            this.Temp.SetUserLabelsName(n, LocaleKBCT.GetString(LabelsNames[n-1]));
	      }
	    }
	    LabelKBCT e1= this.Temp.GetLabel(LabelNumber-1);
	    LabelKBCT e2= this.Temp.GetLabel(LabelNumber);
	    e1.SetLabel_Number(LabelNumber);
	    e2.SetLabel_Number(LabelNumber-1);
	    this.Temp.ReplaceLabel(LabelNumber-1,e2);
	    this.Temp.ReplaceLabel(LabelNumber,e1);
	    this.Temp.SetScaleName("user");
	    this.Temp.SetLabelsName();
	    this.Temp.SetORLabelsName();
	    if (this.VariableType.equals("Input")) {
	      Object[] listeners = this.kbct.GetListeners().toArray();
	      for (int i = 0; i < listeners.length; i++)
	        ( (KBCTListener) listeners[i]).MFReplacedInInput(this.Number+2);

	      this.kbct.ReplaceInput(this.Number+2, this.Temp);
	    } else if (this.VariableType.equals("Output")) {
	      Object[] listeners = this.kbct.GetListeners().toArray();
	      for (int i = 0; i < listeners.length; i++)
	        ( (KBCTListener) listeners[i]).MFReplacedInOutput(this.Number+2);

	      this.kbct.ReplaceOutput(this.Number+2, this.Temp);
	    }
  }
//------------------------------------------------------------------------------
  void jMenuDownMF_actionPerformed() { 
	  this.DownMF(); 
  }
//------------------------------------------------------------------------------
  void jPopupDownMF_actionPerformed() {
	  this.DownMF(); 
  }
//------------------------------------------------------------------------------
  void DownMF() {
	    int LabelNumber= this.jListMF.getSelectedIndex()+1;
	    String ScaleName= this.Temp.GetScaleName();
	    String[] aux;
	    if (ScaleName.equals("user"))
	      aux= this.Temp.GetUserLabelsName();
	    else
	      aux= this.Temp.GetLabelsName();

	    int NbLabels= this.Temp.GetLabelsNumber();
	    String[] LabelsNames= new String[NbLabels];
	    for (int n=0; n<NbLabels; n++)
	      LabelsNames[n]= aux[n];

	    for (int n=1; n<=NbLabels; n++) {
	      if (n==LabelNumber)
	          this.Temp.SetUserLabelsName(n, LabelsNames[n]);
	      else if (n==LabelNumber+1)
	          this.Temp.SetUserLabelsName(n, LabelsNames[n-2]);
	      else {
	          if (ScaleName.equals("user"))
	            this.Temp.SetUserLabelsName(n, LabelsNames[n-1]);
	          else
	            this.Temp.SetUserLabelsName(n, LocaleKBCT.GetString(LabelsNames[n-1]));
	      }
	    }
	    LabelKBCT e1= this.Temp.GetLabel(LabelNumber);
	    LabelKBCT e2= this.Temp.GetLabel(LabelNumber+1);
	    e1.SetLabel_Number(LabelNumber+1);
	    e2.SetLabel_Number(LabelNumber);
	    this.Temp.ReplaceLabel(LabelNumber,e2);
	    this.Temp.ReplaceLabel(LabelNumber+1,e1);
	    this.Temp.SetScaleName("user");
	    this.Temp.SetLabelsName();
	    this.Temp.SetORLabelsName();
	    if (this.VariableType.equals("Input")) {
	      Object[] listeners = this.kbct.GetListeners().toArray();
	      for (int i = 0; i < listeners.length; i++)
	        ((KBCTListener)listeners[i]).MFReplacedInInput(this.Number+2);

	      this.kbct.ReplaceInput(this.Number+2, this.Temp);
	    } else if (this.VariableType.equals("Output")) {
	      Object[] listeners = this.kbct.GetListeners().toArray();
	      for (int i = 0; i < listeners.length; i++)
	        ((KBCTListener)listeners[i]).MFReplacedInOutput(this.Number+2);

	      this.kbct.ReplaceOutput(this.Number+2, this.Temp);
	    }
  }
//------------------------------------------------------------------------------
  void jMenuRefinementMF_actionPerformed() {
	  this.Refinement();
  }
//------------------------------------------------------------------------------
  void jPopupRefinementMF_actionPerformed() {
	  this.Refinement();
  }
//------------------------------------------------------------------------------
  void Refinement() {
	    int[] L= this.jListMF.getSelectedIndices();
	    int NbLabels= this.Temp.GetLabelsNumber();
	    if (L.length==1) {
	      if (L[0]==0)
	          this.RefinementFirstMF(L, NbLabels);
	      else if (L[0]==NbLabels-1)
	          this.RefinementLastMF(NbLabels);
	      else
	          this.RefinementIntermediateMF(L, NbLabels);
	    } else {
	        this.RefinementSeveralMFs(L, NbLabels);
	    }
	    this.Temp.SetScaleName("user");
	    this.Temp.SetLabelsName();
	    this.Temp.SetORLabelsName();
	    if (this.VariableType.equals("Input")) {
	      Object[] listeners = this.kbct.GetListeners().toArray();
	      for (int i = 0; i < listeners.length; i++)
	        ((KBCTListener)listeners[i]).MFReplacedInInput(this.Number+2);

	      this.kbct.ReplaceInput(this.Number+2, this.Temp);
	    } else if (this.VariableType.equals("Output")) {
	      Object[] listeners = this.kbct.GetListeners().toArray();
	      for (int i = 0; i < listeners.length; i++)
	        ((KBCTListener)listeners[i]).MFReplacedInOutput(this.Number+2);

	      this.kbct.ReplaceOutput(this.Number+2, this.Temp);
	    }
  }
//------------------------------------------------------------------------------
  private void RefinementFirstMF(int[] L, int NbLabels) {
    LabelKBCT e1= this.Temp.GetLabel(L[0]+1);
    LabelKBCT e2= this.Temp.GetLabel(L[0]+2);
    double[] d2= e2.GetParams();
    double p1= d2[0];
    double p2= d2[1];
    double m= (p1+p2)/2;
    e1.SetP3(m);
    if (NbLabels > 2) {
        e2.SetName("trapezoidal");
        e2.InitParams(4);
        e2.SetP1(p1);
        e2.SetP2(m);
        e2.SetP3(d2[d2.length-2]);
        e2.SetP4(d2[d2.length-1]);
    } else {
        e2.SetP1(p1);
        e2.SetP2(m);
    }
    this.Temp.ReplaceLabel(e1.GetLabel_Number(), e1);
    this.Temp.ReplaceLabel(e2.GetLabel_Number(), e2);
    String ScaleName= this.Temp.GetScaleName();
    String[] LabelsNames;
    if (ScaleName.equals("user"))
      LabelsNames= this.Temp.GetUserLabelsName();
    else
      LabelsNames= this.Temp.GetLabelsName();

    String e1_name;
    String e2_name;
    if (ScaleName.equals("user")) {
      e1_name= LocaleKBCT.GetString("Strictly")+" ("+LabelsNames[0]+")";
      e2_name= LocaleKBCT.GetString("MoreOrLess")+" ("+LabelsNames[1]+")";
    } else {
      e1_name= LocaleKBCT.GetString("Strictly")+" ("+LocaleKBCT.GetString(LabelsNames[0])+")";
      e2_name= LocaleKBCT.GetString("MoreOrLess")+" ("+LocaleKBCT.GetString(LabelsNames[1])+")";
    }
    this.Temp.SetUserLabelsName(1, e1_name);
    this.Temp.SetUserLabelsName(2, e2_name);
    for (int n=3; n<=NbLabels; n++) {
        if (ScaleName.equals("user"))
          this.Temp.SetUserLabelsName(n, LabelsNames[n-1]);
        else
          this.Temp.SetUserLabelsName(n, LocaleKBCT.GetString(LabelsNames[n-1]));
    }
  }
//------------------------------------------------------------------------------
  private void RefinementLastMF(int NbLabels) {
    LabelKBCT e1= this.Temp.GetLabel(NbLabels-1);
    LabelKBCT e2= this.Temp.GetLabel(NbLabels);
    double[] d1= e1.GetParams();
    double p1= d1[d1.length-2];
    double p2= d1[d1.length-1];
    double m= (p1+p2)/2;
    if (NbLabels > 2) {
        e1.InitParams(4);
        e1.SetName("trapezoidal");
        e1.SetP1(d1[0]);
        e1.SetP2(d1[1]);
        e1.SetP3(m);
        e1.SetP4(p2);
    } else {
        e1.SetP2(m);
        e1.SetP3(p2);
    }
    e2.SetP1(m);
    this.Temp.ReplaceLabel(e1.GetLabel_Number(), e1);
    this.Temp.ReplaceLabel(e2.GetLabel_Number(), e2);
    String ScaleName= this.Temp.GetScaleName();
    String[] LabelsNames;
    if (ScaleName.equals("user"))
      LabelsNames= this.Temp.GetUserLabelsName();
    else
      LabelsNames= this.Temp.GetLabelsName();

    String e1_name;
    String e2_name;
    if (ScaleName.equals("user")) {
      e1_name= LocaleKBCT.GetString("MoreOrLess")+" ("+LabelsNames[NbLabels-2]+")";
      e2_name= LocaleKBCT.GetString("Strictly")+" ("+LabelsNames[NbLabels-1]+")";
    } else {
      e1_name= LocaleKBCT.GetString("MoreOrLess")+" ("+LocaleKBCT.GetString(LabelsNames[NbLabels-2])+")";
      e2_name= LocaleKBCT.GetString("Strictly")+" ("+LocaleKBCT.GetString(LabelsNames[NbLabels-1])+")";
    }
    this.Temp.SetUserLabelsName(NbLabels-1, e1_name);
    this.Temp.SetUserLabelsName(NbLabels, e2_name);
    for (int n=1; n<=NbLabels-2; n++) {
        if (ScaleName.equals("user"))
          this.Temp.SetUserLabelsName(n, LabelsNames[n-1]);
        else
          this.Temp.SetUserLabelsName(n, LocaleKBCT.GetString(LabelsNames[n-1]));
    }
  }
//------------------------------------------------------------------------------
  private void RefinementIntermediateMF(int[] L, int NbLabels) {
    LabelKBCT e1= this.Temp.GetLabel(L[0]);
    LabelKBCT e2= this.Temp.GetLabel(L[0]+1);
    LabelKBCT e3= this.Temp.GetLabel(L[0]+2);
    double[] d1= e1.GetParams();
    double[] d2= e2.GetParams();
    double[] d3= e3.GetParams();
    double p1= d2[0];
    double p2l= d2[1];
    double m1= (p1+p2l)/2;
    double p2r= d2[d2.length-2];
    double p3= d2[d2.length-1];
    double m2= (p2r+p3)/2;
    if (L[0] > 1) {
        e1.InitParams(4);
        e1.SetName("trapezoidal");
        e1.SetP1(d1[0]);
        e1.SetP2(d1[1]);
        e1.SetP3(m1);
        e1.SetP4(p2l);
    } else {
        e1.SetP2(m1);
    }
    e2.SetP1(m1);
    if (d2.length==4)
      e2.SetP4(m2);
    else
      e2.SetP3(m2);

    if (L[0] < NbLabels-2) {
        e3.InitParams(4);
        e3.SetName("trapezoidal");
        e3.SetP1(p2r);
        e3.SetP2(m2);
        e3.SetP3(d3[d3.length-2]);
        e3.SetP4(d3[d3.length-1]);
    } else {
        e3.SetP2(m2);
    }
    this.Temp.ReplaceLabel(e1.GetLabel_Number(), e1);
    this.Temp.ReplaceLabel(e2.GetLabel_Number(), e2);
    this.Temp.ReplaceLabel(e3.GetLabel_Number(), e3);
    String ScaleName= this.Temp.GetScaleName();
    String[] LabelsNames;
    if (ScaleName.equals("user"))
      LabelsNames= this.Temp.GetUserLabelsName();
    else
      LabelsNames= this.Temp.GetLabelsName();

    String e1_name;
    String e2_name;
    String e3_name;
    if (ScaleName.equals("user")) {
      e1_name= LocaleKBCT.GetString("MoreOrLess")+" ("+LabelsNames[L[0]-1]+")";
      e2_name= LocaleKBCT.GetString("Strictly")+" ("+LabelsNames[L[0]]+")";
      e3_name= LocaleKBCT.GetString("MoreOrLess")+" ("+LabelsNames[L[0]+1]+")";
    } else {
      e1_name= LocaleKBCT.GetString("MoreOrLess")+" ("+LocaleKBCT.GetString(LabelsNames[L[0]-1])+")";
      e2_name= LocaleKBCT.GetString("Strictly")+" ("+LocaleKBCT.GetString(LabelsNames[L[0]])+")";
      e3_name= LocaleKBCT.GetString("MoreOrLess")+" ("+LocaleKBCT.GetString(LabelsNames[L[0]+1])+")";
    }
    this.Temp.SetUserLabelsName(L[0], e1_name);
    this.Temp.SetUserLabelsName(L[0]+1, e2_name);
    this.Temp.SetUserLabelsName(L[0]+2, e3_name);
    for (int n=1; n< L[0]; n++) {
        if (ScaleName.equals("user"))
          this.Temp.SetUserLabelsName(n, LabelsNames[n-1]);
        else
          this.Temp.SetUserLabelsName(n, LocaleKBCT.GetString(LabelsNames[n-1]));
    }
    for (int n=L[0]+3; n<=NbLabels; n++) {
        if (ScaleName.equals("user"))
          this.Temp.SetUserLabelsName(n, LabelsNames[n-1]);
        else
          this.Temp.SetUserLabelsName(n, LocaleKBCT.GetString(LabelsNames[n-1]));
    }
  }
//------------------------------------------------------------------------------
  private void RefinementSeveralMFs(int[] L, int NbLabels) {
    LabelKBCT[] ee= new LabelKBCT[NbLabels+L.length-1];
    String[] NewLabelsNames= new String[NbLabels+L.length-1];
    String ScaleName= this.Temp.GetScaleName();
    LabelKBCT[] ee_old= new LabelKBCT[NbLabels];
    String[] LabelsNames;
    String[] AuxLabelsNames;
    if (ScaleName.equals("user"))
      AuxLabelsNames= this.Temp.GetUserLabelsName();
    else
      AuxLabelsNames= this.Temp.GetLabelsName();

    LabelsNames= new String[AuxLabelsNames.length];
    for (int k=0; k<AuxLabelsNames.length; k++)
      LabelsNames[k]= AuxLabelsNames[k];

    for (int k=0; k<NbLabels; k++)
      ee_old[k]= this.Temp.GetLabel(k+1);

    int cont=0;
    while (cont<L[0]-1) {
      if (ScaleName.equals("user"))
        NewLabelsNames[cont]= LabelsNames[cont];
      else
        NewLabelsNames[cont]= LocaleKBCT.GetString(LabelsNames[cont]);

      ee[cont]= this.Temp.GetLabel(cont+1);
      cont++;
    }
    double[] d1, d2;
    double p1, p2l, p2r, p3, m1, m2;
    if (L[0] >0) {
      ee[cont]= this.Temp.GetLabel(L[0]);
      ee[cont+1]= this.Temp.GetLabel(L[0]+1);
      d1= ee[cont].GetParams();
      d2= ee[cont+1].GetParams();
      p1= d2[0];
      p2l= d2[1];
      m1= (p1+p2l)/2;
      p2r= d2[d2.length-2];
      p3= d2[d2.length-1];
      m2= (p2r+p3)/2;
      double aux1=0;
      double aux2=0;
      int nbL=3;
      if (ee[cont].GetLabel_Number()==1) {
        ee[cont].SetP2(m1);
      } else {
        ee[cont].InitParams(4);
        ee[cont].SetName("trapezoidal");
        ee[cont].SetP1(d1[0]);
        ee[cont].SetP2(d1[1]);
        ee[cont].SetP3(m1);
        ee[cont].SetP4(p2l);
      }
      aux1=p2r;
      aux2=m2;
      nbL=d2.length;
      ee[cont+1].SetP1(m1);
      if (d2.length==4)
        ee[cont+1].SetP4(m2);
      else {
        ee[cont+1].SetP3(m2);
      }
      if (ScaleName.equals("user"))
        NewLabelsNames[cont]= LocaleKBCT.GetString("MoreOrLess")+" ("+LabelsNames[L[0]-1]+")";
      else
        NewLabelsNames[cont]= LocaleKBCT.GetString("MoreOrLess")+" ("+LocaleKBCT.GetString(LabelsNames[L[0]-1])+")";

      cont++;
      if (ScaleName.equals("user"))
        NewLabelsNames[cont]= LocaleKBCT.GetString("Strictly")+" ("+LabelsNames[L[0]]+")";
      else
        NewLabelsNames[cont]= LocaleKBCT.GetString("Strictly")+" ("+LocaleKBCT.GetString(LabelsNames[L[0]])+")";

      cont++;

      String nameAux= ee[cont-1].GetName();
      for (int k=cont; k<NbLabels; k++) {
        this.Temp.RemoveLabel( cont, false);
      }
      ee[cont-1].SetName(nameAux);
      if (nbL==3)
          ee[cont-1].SetP3(aux2);
      else {
          ee[cont-1].SetP3(aux1);
          ee[cont-1].SetP4(aux2);
      }
      this.Temp.ReplaceLabel(ee[cont-1].GetLabel_Number(), ee[cont-1]);
    } else {
    	ee[cont]= this.Temp.GetLabel(L[0]+1);
        d2= ee[cont].GetParams();
        p1= d2[1];
        p3= d2[2];
        m1= (p1+p3)/2;
        ee[cont].SetP3(m1);
        if (ScaleName.equals("user"))
          NewLabelsNames[cont]= LocaleKBCT.GetString("Strictly")+" ("+LabelsNames[L[0]]+")";
        else
          NewLabelsNames[cont]= LocaleKBCT.GetString("Strictly")+" ("+LocaleKBCT.GetString(LabelsNames[L[0]])+")";

        cont++;
        String nameAux= ee[cont-1].GetName();
        for (int k=cont; k<NbLabels; k++)
          this.Temp.RemoveLabel( cont, false);

        ee[cont-1].SetName(nameAux);
        ee[cont-1].SetP3(m1);
        this.Temp.ReplaceLabel(ee[cont-1].GetLabel_Number(), ee[cont-1]);
    }
    int n=cont;
    int l=1;
    while (l<L.length) {
      ee[n+1]= ee_old[L[l]];
      d1= ee[n+1].GetParams();
      double paux1= d2[d2.length-2];
      double paux2= d1[1];
      double maux= (paux1+paux2)/2;
      ee[n]= new LabelKBCT("triangular", paux1, maux, paux2, this.Temp.GetLabelsNumber()+1);
      if (ScaleName.equals("user"))
        NewLabelsNames[cont]= LocaleKBCT.GetString("Between")+" ("+NewLabelsNames[cont-1]+" : "+LocaleKBCT.GetString("Strictly")+" ("+LabelsNames[L[l]]+"))";
      else
        NewLabelsNames[cont]= LocaleKBCT.GetString("Between")+" ("+NewLabelsNames[cont-1]+" : "+LocaleKBCT.GetString("Strictly")+" ("+LocaleKBCT.GetString(LabelsNames[L[l]])+"))";

      cont++;
      this.Temp.AddLabel(ee[n]);
      ee[n+1].SetP1(maux);
      ee[n+1].SetP2(paux2);
      if (d1.length==4) {
        ee[n+1].SetP3(d1[2]);
        ee[n+1].SetP4((d1[2]+d1[3])/2);
      } else {
        ee[n+1].SetP3((d1[1]+d1[2])/2);
      }
      ee[n+1].SetLabel_Number(this.Temp.GetLabelsNumber()+1);
      if (ScaleName.equals("user"))
        NewLabelsNames[cont]= LocaleKBCT.GetString("Strictly")+" ("+LabelsNames[L[l]]+")";
      else
        NewLabelsNames[cont]= LocaleKBCT.GetString("Strictly")+" ("+LocaleKBCT.GetString(LabelsNames[L[l]])+")";

      cont++;
      this.Temp.AddLabel(ee[n+1]);
      n++;
      l++;
      d2= d1;
    }
    if (cont < ee.length) {
      ee[cont]= ee_old[L[L.length-1]+1];
      d1= ee[cont].GetParams();
      p1= d1[0];
      p2l= d1[1];
      m1= (p1+p2l)/2;
      p2r= d1[d1.length-2];
      p3= d1[d1.length-1];
      ee[cont].SetLabel_Number(this.Temp.GetLabelsNumber()+1);
      if (ee[cont].GetLabel_Number()==ee.length) {
        ee[cont].SetP2(m1);
      } else {
        ee[cont].InitParams(4);
        ee[cont].SetName("trapezoidal");
        ee[cont].SetP1(p1);
        ee[cont].SetP2(m1);
        ee[cont].SetP3(p2r);
        ee[cont].SetP4(p3);
      }
      if (ScaleName.equals("user"))
        NewLabelsNames[cont]= LocaleKBCT.GetString("MoreOrLess")+" ("+LabelsNames[L[L.length-1]+1]+")";
      else
        NewLabelsNames[cont]= LocaleKBCT.GetString("MoreOrLess")+" ("+LocaleKBCT.GetString(LabelsNames[L[L.length-1]+1])+")";

      this.Temp.AddLabel(ee[cont]);
      cont++;
    }
    int caux=2;
    while (cont<NewLabelsNames.length) {
      if (ScaleName.equals("user"))
        NewLabelsNames[cont]= LabelsNames[L[L.length-1]+caux];
      else
        NewLabelsNames[cont]= LocaleKBCT.GetString(LabelsNames[L[L.length-1]+caux]);

      ee[cont]= ee_old[L[L.length-1]+caux];
      ee[cont].SetLabel_Number(this.Temp.GetLabelsNumber()+1);
      this.Temp.AddLabel(ee[cont]);
      caux++;
      cont++;
    }
    for (int k=1; k<=NewLabelsNames.length; k++)
      this.Temp.SetUserLabelsName(k, NewLabelsNames[k-1]);
  }
//------------------------------------------------------------------------------
  void jMenuCopyMF_actionPerformed() {
	  this.CopyMF();
  }
//------------------------------------------------------------------------------
  void jPopupCopyMF_actionPerformed() {
	  this.CopyMF();
  }
//------------------------------------------------------------------------------
  void CopyMF() {
    try {
	  int sel= this.jListMF.getSelectedIndices()[0];
	  //System.out.println("Copy label "+String.valueOf(sel+1));
	  String oldname;
	  if (this.Temp.GetScaleName().equals("user"))
		  oldname= this.Temp.GetUserLabelsName(sel);
	  else
		  oldname= LocaleKBCT.GetString(this.Temp.GetLabelsName(sel));
	  
	  String newname= oldname+" ("+LocaleKBCT.GetString("Copy")+")";
      LabelKBCT e= this.Temp.GetLabel(sel+1);
      double[] par= e.GetParams();
      this.MFParams.SetParams(par);
      this.ShowNewMFParameters(newname); 
	} catch( Throwable t ) {
	    this.HideMFParameters();
	    MessageKBCT.Error( null, t );
	}
  }
//------------------------------------------------------------------------------
  protected void ActiveChanged() { this.kbct.InputActiveChanged(this.Number); }
}