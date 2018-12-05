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
//                              JOptFrame.java
//
// Author(s) : Jean-luc LABLEE
// FISPRO Version : 3.2 - Copyright INRA - Cemagref - 2002 - IDDN.FR.001.030024.000.R.P.2005.003.31235
// Licence http://www.cecill.info/licences/Licence_CeCILL_V2-en.html/
// Last modification date:  July 31, 2009 - Contact : fispro@supagro.inra.fr
//
//**********************************************************************
package kbctFrames;

import java.awt.*;
import java.io.*;
import javax.swing.*;

import java.awt.event.*;
import javax.swing.border.*;
//import javax.swing.event.*;
import java.util.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import kbct.JFIS;
import kbct.JKBCT;
import kbct.JKBCTInput;
import kbct.JKBCTOutput;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbctAux.DoubleField;
import kbctAux.IntegerField;
import kbctAux.MessageKBCT;
import kbctAux.PerformanceFile;
import KB.LabelKBCT;
import fis.JExtendedDataFile;
import fis.JInput;
import fis.JOutput;
import fis.jnifis;

import fis.JFISDialog;

/**
 * kbctFrames.JOptFrame.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
//------------------------------------------------------------------------------
public class JOptFrame extends JFISDialog {
  static final long serialVersionUID=0;
  private JKBCTFrame Parent;
  private JKBCT kbct;
  private String fis_name;
  private JExtendedDataFile DataFile;
  private JFIS fis;
  private JPanel jPanelOutput;
  private ButtonGroup jBGOutput;
  private JLabel jLabelMaxOfIter;
  private IntegerField jIFMaxOfIter;
  double StdGaussianNoise = 0.005;
  int MaxOfConstraints;
  int MaxOfFailures;
  private JLabel jLabelDistanceThres;
  private DoubleField jDFDistanceThres;
  private JLabel jLabelBlankThres;
  private DoubleField jDFBlankThres;
  private JCheckBox cNear;
  private JButton jbAdvanced;
  private JButton jButtonApply;
  private JButton jButtonCancel;
  private JButton jButtonAllInputs;
  private JButton jButtonAllOutputs;
  private AbstractButton abNature;
  private AbstractButton abClassif;
  private Vector jVectorKey;
  private Vector jVectorInputs;
  private Vector jVectorOutput;
  private Vector jVectorRules;
  private JPanel jPanelOpt = null;
  private JComponent jCompOpt = null;
  private int SeedValue = 1;
  private double l1 = 0.4, l2 = 0.2, l3 = 0.5;
  private Vector jVectorPFF;
// private boolean SaisieMaxFailures = false;
// private boolean SaisieMaxConstraints = false;
  private boolean InputsSelected = false;
  private boolean OutputSelected = false;
//  private boolean RulesSelected = false;
  private JTextField jtfKey;
  private JCheckBox jcbDisplayKey;
  private JLabel jLabelLossOfCoverage = new JLabel();
  private DoubleField jDFLossOfCoverage = new DoubleField();
  private boolean automatic;
  private int Input_number;
  private int Label_Number;
  private int[] UsedLabels;
  private ImageIcon icon_guaje= LocaleKBCT.getIconGUAJE();
  private DecimalFormat df;
  
//------------------------------------------------------------------------------
  public JOptFrame( JKBCTFrame parent, JKBCT kbct, JExtendedDataFile data, boolean automatic ) throws Throwable {
    super(parent);
    this.Parent = parent;
    this.kbct= kbct;
    //System.out.println("kb rules= "+this.kbct.GetNbRules());
	File temp= JKBCTFrame.BuildFile("temprboptSW.fis");
    this.fis_name= temp.getAbsolutePath();
    //System.out.println("this.kbct.GetPtr()="+this.kbct.GetPtr());
    //System.out.println("this.kbct.GetCopyPtr()="+this.kbct.GetCopyPtr());
    this.kbct.Save();
    //System.out.println("this.kbct.GetPtr()="+this.kbct.GetPtr());
    //System.out.println("this.kbct.GetCopyPtr()="+this.kbct.GetCopyPtr());
    this.kbct.SaveFIS(fis_name);
    this.fis= new JFIS(fis_name);
    //System.out.println("fis rules= "+this.fis.NbRules());
    jnifis.setUserHomeFisproPath(MainKBCT.getConfig().GetKbctPath());
    this.DataFile = data;
    this.automatic= automatic;
    try
      {
      if( this.fis.NbRules() == 0 ) {
          MessageKBCT.Error(this,LocaleKBCT.GetString("Error"),LocaleKBCT.GetString("OptimizationImpossibleTheFISHasNoRules"));
          return;
      }
      this.jBGOutput = new ButtonGroup();
      this.jPanelOutput = new JPanel(new GridBagLayout());
      this.jPanelOutput.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),LocaleKBCT.GetString("OutputToBeOptimized")));
      for( int i=0, x=0, y=0 ; i<this.fis.NbOutputs() ; i++ ) {
        JOutput output = this.fis.GetOutput(i);
        if( output.GetActive() == true )
          {
          JRadioButton jrb = new JRadioButton(output.GetName());
          jrb.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e)
            {
            try { JOptFrame.this.jbInit(); }
            catch(Throwable t) { MessageKBCT.Error(null, t); }
            } });
          this.jBGOutput.add(jrb);
          this.jPanelOutput.add(jrb,  new GridBagConstraints(x, y, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
          x++;
          }
        }
      ((JRadioButton)this.jBGOutput.getElements().nextElement()).setSelected(true); // active la premiere sortie
	  this.df= new DecimalFormat();
	  this.df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
	  this.df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
	  DecimalFormatSymbols dfs= this.df.getDecimalFormatSymbols();
	  dfs.setDecimalSeparator((new String(".").charAt(0)));
	  this.df.setDecimalFormatSymbols(dfs);
	  this.df.setGroupingSize(20);
	  boolean classifFlag= true;
      if (this.kbct.GetOutput(1).GetType().equals("numerical")) { 
          classifFlag= false;
      }
      if (classifFlag) {   
          double[][] qold= this.FISquality(this.fis, this.kbct, this.DataFile);
          double accfispro= qold[0][0]/this.DataFile.DataLength();
          MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("OldError")+" (1- ACCBT0) = "+this.df.format(accfispro), "Optimization");
      } else {
    	  /*double[] res= this.fis.InferErrorRegression();
    	  for (int hh=0; hh<res.length; hh++) {
    		   System.out.println("res["+hh+"]="+res[hh]);
    	  }
    	  System.out.println();*/
          //MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("OldError")+" (RMSE) = "+this.df.format(this.fis.InferErrorRegression()[1]), "Optimization");
          double[][] qold= this.FISquality(this.fis, this.kbct, this.DataFile);
          MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("OldError")+" (RMSE) = "+qold[0][0], "Optimization");
      }
      MessageKBCT.WriteLogFile("----------------------------------", "Optimization");
      if (!automatic) {
          jbInit();
          this.setLocation(JChildFrame.ChildPosition(this.Parent, this.getSize()));
          this.setVisible(true);
        }
    } catch(Throwable t) {
    	t.printStackTrace();
    	MessageKBCT.Error(null, t); 
    }
  }
//------------------------------------------------------------------------------
  private void jbInit()  throws Throwable {
    jLabelMaxOfIter = new JLabel();
    jIFMaxOfIter = new IntegerField();
    jLabelDistanceThres = new JLabel();
    jDFDistanceThres = new DoubleField();
    jLabelBlankThres = new JLabel();
    jDFBlankThres = new DoubleField();
    cNear = new JCheckBox();
    jbAdvanced = new JButton();
    jButtonApply = new JButton();
    jButtonAllInputs = new JButton();
    jButtonAllOutputs = new JButton();
    jButtonCancel = new JButton();
    this.jVectorKey = new Vector();
    this.jVectorInputs = new Vector();
    this.jVectorOutput = new Vector();
    this.jVectorRules = new Vector();
    this.jVectorPFF = new Vector();
    if( this.jPanelOpt != null )
        this.getContentPane().remove(this.jCompOpt);

    this.jPanelOpt = new JPanel(new GridBagLayout());

    // panel sortie
    jPanelOpt.add(jPanelOutput,  new GridBagConstraints(0, 0, 5, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));

    // nombre d'iterations
    //this.jIFMaxOfIter.setValue(100);
    this.jIFMaxOfIter.setValue(MainKBCT.getConfig().GetNbIterations());
    // loss of coverage
    jPanelOpt.add(this.jLabelLossOfCoverage, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
    		,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
	this.jDFLossOfCoverage.setValue(10);
	jPanelOpt.add(this.jDFLossOfCoverage, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 40, 0));
	jPanelOpt.add(new JLabel("%"), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 40, 0));
    
    // distance des centres
    jPanelOpt.add(this.jLabelDistanceThres, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    this.jDFDistanceThres.NumberFormat().setMaximumFractionDigits(10);
    this.jDFDistanceThres.setValue(0.000001);
    jPanelOpt.add(this.jDFDistanceThres, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 40, 0));

    // seuil blanc
    jPanelOpt.add(this.jLabelBlankThres, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    this.jDFBlankThres.setValue(MainKBCT.getConfig().GetBlankThres());
    jPanelOpt.add(this.jDFBlankThres, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 40, 0));

    // infere sans redondance -supprime le 19 mai 2003
    // (a remplacer par disjonction max)
    // checkbox reused for MF max breakpoint constraint
    if (MainKBCT.getConfig().GetBoundedOptimization())
        this.cNear.setSelected(true);
    else 
        this.cNear.setSelected(false);
    	
    jPanelOpt.add(this.cNear, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0
	   ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));

    // options avancees
    jPanelOpt.add(this.jbAdvanced, new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    this.jbAdvanced.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jbAdvanced_actionPerformed(e); } });

    // panel selection
    JPanel jPanelSelection = new JPanel(new GridBagLayout());
    jPanelOpt.add(jPanelSelection, new GridBagConstraints(0, 5, 5, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    //allInputs button
    jButtonAllInputs.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonAllInputs_actionPerformed(e); } });
    jPanelSelection.add(jButtonAllInputs,  new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    //allOutputs button
    if( this.fis.GetOutput(this.SelectedOutput()).GetNature() == JOutput.FUZZY )
      {
      jButtonAllOutputs.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonAllOutputs_actionPerformed(e); } });
      jPanelSelection.add(jButtonAllOutputs,  new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      }
    // panel cle
    JPanel jPanelKey = new JPanel(new GridBagLayout());
    jPanelOpt.add(jPanelKey, new GridBagConstraints(0, 6, 5, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));

    // entrees
    final JPanel jPanelInputs = new JPanel(new GridBagLayout());
    JScrollPane jsp = new JScrollPane(jPanelInputs) { 
   	    static final long serialVersionUID=0;
    	public Dimension getMinimumSize() { return new Dimension(0, Math.min(150, jPanelInputs.getMinimumSize().height)); } };
    jPanelKey.add(jsp, new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    int y = 0;
    for( int i=0, x=0 ; i<this.fis.NbInputs() ; i++ ) {
      JInput input = this.fis.GetInput(i);
      if ( (input.GetActive()==true) &&
    	   (this.kbct.GetInput(i+1).GetType().equals("numerical")) &&
    	   (!this.kbct.GetInput(i+1).GetTrust().equals("hhigh")) ) {
    	  //System.out.println("OPT -> "+String.valueOf(i+1));
        JCheckBox jcbPFF = new JCheckBox(LocaleKBCT.GetString("PFF"), true);
        jcbPFF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JOptFrame.this.jtfKey.setText(JOptFrame.this.GetKey()); } } );
        if (i+1==this.Input_number)
            jcbPFF.setSelected(true);
        else
            jcbPFF.setSelected(false);

        jcbPFF.setEnabled(true);
        this.jVectorInputs.add(jcbPFF);
        this.jVectorPFF.add(jcbPFF);
        JPanel jPanelInput = new JPanel(new GridBagLayout());
        jPanelInput.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),input.GetName()));
        jPanelInputs.add(jPanelInput, new GridBagConstraints(x, y, 1, 1, 1.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        y = (x == 5) ? y+1 : y;
        x = (x == 5) ? 0 : x+1;
        jPanelInput.add(jcbPFF, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        Vector jVectorMFS = new Vector();
        JKBCTInput inkbct= this.kbct.GetInput(i+1);
        int NbLabKB=inkbct.GetLabelsNumber();
        for( int j=0 ; j<input.GetNbMF() ; j++ ) {
            String name="";
            if (j < NbLabKB) {
                if (!inkbct.GetScaleName().equals("user"))
          	      name= LocaleKBCT.GetString(inkbct.GetLabelsName(j));
                else
          	      name= inkbct.GetUserLabelsName(j);
            } else {
          	  name=input.GetMF(j).GetName();
            }
            JCheckBox jcbMF = new JCheckBox(LocaleKBCT.GetString("Optimize") + " " + name, true);
            jcbMF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JOptFrame.this.jtfKey.setText(JOptFrame.this.GetKey()); } } );
            if ( (i+1==this.Input_number) && (this.UsedLabels != null) && (j < this.UsedLabels.length) && (this.UsedLabels[j]==1) )
                jcbMF.setSelected(true);
            else if ( (i+1==this.Input_number) && (j+1==this.Label_Number) )
                jcbMF.setSelected(true);
            else
                jcbMF.setSelected(false);

            this.jVectorInputs.add(jcbMF);
            jVectorMFS.add(jcbMF);
            if (j < NbLabKB)
                jPanelInput.add(jcbMF, new GridBagConstraints(0, 1+j, 1, 1, 0.0, 0.0
                    ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
          }
          this.jVectorPFF.add(jVectorMFS);
        } else {
      	  //System.out.println("NOT OPT -> "+String.valueOf(i+1));
          JCheckBox jcbPFF = new JCheckBox(LocaleKBCT.GetString("PFF"), false);
          jcbPFF.setSelected(false);
          jcbPFF.setVisible(false);
          this.jVectorInputs.add(jcbPFF);
          this.jVectorPFF.add(jcbPFF);
          Vector jVectorMFS = new Vector();
          for( int j=0 ; j<input.GetNbMF() ; j++ ) {
              JCheckBox jcbMF = new JCheckBox(LocaleKBCT.GetString("Optimize"), false);
              jcbMF.setSelected(false);
              jcbMF.setVisible(false);
              this.jVectorInputs.add(jcbMF);
              jVectorMFS.add(jcbMF);
            }
          this.jVectorPFF.add(jVectorMFS);
        }
      }
    this.jVectorKey.addAll(this.jVectorInputs);
    // sortie
    JPanel jPanelOutput = new JPanel(new GridBagLayout());
    final JOutput output = this.fis.GetOutput(this.SelectedOutput());
    final int nature = output.GetNature();
    // bouton abstrait nature de la sortie
    abNature = new AbstractButton() { 
   	    static final long serialVersionUID=0;
    	public boolean isSelected() { return (nature == JOutput.FUZZY) ? true : false; } };

    this.jVectorKey.add(abNature);

    if( nature == JOutput.FUZZY ) // sortie floue
      {
      jPanelOutput.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),output.GetName()));
      final JPanel inner_output = new JPanel(new GridBagLayout());
      JScrollPane jsp_output = new JScrollPane(inner_output) { 
    	  static final long serialVersionUID=0;
    	  public Dimension getMinimumSize() { return new Dimension(inner_output.getSize().width, Math.min(150, inner_output.getMinimumSize().height)); } };
      jPanelOutput.add(jsp_output, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      jPanelKey.add(jPanelOutput, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 0, 5), 0, 0));
      // bouton abstrait classif
      final boolean classif = output.GetClassif();
       abClassif = new AbstractButton() {
    	   static final long serialVersionUID=0;
    	   public boolean isSelected() { return classif; } };
      this.jVectorKey.add(abClassif);
      JCheckBox jcbPFF = new JCheckBox(LocaleKBCT.GetString("PFF"), true);
      jcbPFF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JOptFrame.this.jtfKey.setText(JOptFrame.this.GetKey()); } } );
      this.jVectorOutput.add(jcbPFF);
      this.jVectorPFF.add(jcbPFF);
      inner_output.add(jcbPFF, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      Vector jVectorMFS = new Vector();
      for( int j=0 ; j<output.GetNbMF() ; j++ )
        {
        JCheckBox jcbMF = new JCheckBox(LocaleKBCT.GetString("Optimize") + " " + output.GetMF(j).GetName(), true);
        jcbMF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JOptFrame.this.jtfKey.setText(JOptFrame.this.GetKey()); } } );
        this.jVectorOutput.add(jcbMF);
        jVectorMFS.add(jcbMF);
        inner_output.add(jcbMF, new GridBagConstraints(0, 1+j, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        }
      this.jVectorPFF.add(jVectorMFS);
      this.jVectorKey.addAll(this.jVectorOutput);
      }

    // affichage de la cle
    JPanel jpDisplayKey= new JPanel(new GridBagLayout());
    this.jcbDisplayKey= new JCheckBox(LocaleKBCT.GetString("DisplayKey"));
    this.jcbDisplayKey.setSelected(true);
    this.jtfKey= new JTextField(this.GetKey());
    //System.out.println(this.jtfKey.getText());
    this.jtfKey.setEditable(false);
    this.jcbDisplayKey.addActionListener(new ActionListener()
      {
      public void actionPerformed(ActionEvent e) { jtfKey.setVisible(jcbDisplayKey.isSelected()); }
      });
    jpDisplayKey.add(this.jcbDisplayKey, new GridBagConstraints(0, 0, 1, 1, 0.01, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jpDisplayKey.add(this.jtfKey, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
    jPanelKey.add(jpDisplayKey, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));

    // panel validation
    JPanel jPanelValidation = new JPanel(new GridBagLayout());
    //apply button
    jButtonApply.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonApply_actionPerformed(e); } });
    jPanelValidation.add(jButtonApply,  new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    // cancel button
    jButtonCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JOptFrame.this.dispose(); } });
    jPanelValidation.add(jButtonCancel,  new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    jPanelOpt.add(jPanelValidation, new GridBagConstraints(0, 7, 5, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

    // selectionne les entrees
    if (!this.automatic) {
        this.jButtonAllInputs_actionPerformed(null);
    } else {
        for( int i=0;i<=this.jVectorRules.size()-1 ; i++ ) {
            ((AbstractButton)this.jVectorRules.elementAt(i)).setSelected(false);
            ((AbstractButton)this.jVectorRules.elementAt(i)).setEnabled(false);
            }
    }
    this.jtfKey.setText(this.GetKey());

    // affichage
    this.Translate();
    this.setModal(true);
    this.jCompOpt = this.jPanelOpt;
    this.getContentPane().add(this.jCompOpt);
    this.pack();
  }
//------------------------------------------------------------------------------
  private void Translate() {
    this.setTitle(LocaleKBCT.GetString("Optimization") + " " + LocaleKBCT.GetString("SolisWetts"));
    this.jLabelMaxOfIter.setText(LocaleKBCT.GetString("MaximumNumberOfIterations") + ":");
    this.jLabelLossOfCoverage.setText(LocaleKBCT.GetString("LossOfCoverage") + ":");
    this.jLabelDistanceThres.setText(LocaleKBCT.GetString("CenterDistanceThreshold") + ":");
    this.jLabelBlankThres.setText(LocaleKBCT.GetString("BlankThres") + ":");
    this.cNear.setText(LocaleKBCT.GetString("Cnear"));
    this.jbAdvanced.setText(LocaleKBCT.GetString("AdvancedParameters"));
    this.jButtonApply.setText(LocaleKBCT.GetString("Apply"));
    this.jButtonCancel.setText(LocaleKBCT.GetString("Cancel"));
    this.jButtonAllInputs.setText(LocaleKBCT.GetString("SelectallInputs"));
    this.jButtonAllOutputs.setText(LocaleKBCT.GetString("SelectallOutput"));
   }
//------------------------------------------------------------------------------
  void jbAdvanced_actionPerformed(ActionEvent e) {
  final JDialog dialog = new JFISDialog();
  dialog.setIconImage(icon_guaje.getImage());
  dialog.setTitle(LocaleKBCT.GetString("AdvancedParameters"));
  dialog.getContentPane().setLayout(new GridBagLayout());
  // init des valeurs par defaut
  this.InitMaxFailuresConstraints();
  // seed value
  JLabel jLabelSeedValue = new JLabel(LocaleKBCT.GetString("SeedValue") + ":");
  final IntegerField jIFSeedValue = new IntegerField();
  jIFSeedValue.setValue(this.SeedValue);
  dialog.getContentPane().add(jLabelSeedValue, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
  dialog.getContentPane().add(jIFSeedValue, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 40, 0));

  // Solis Wets constants
  JPanel jPanelSolisWets = new JPanel(new GridBagLayout());
  jPanelSolisWets.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),LocaleKBCT.GetString("SolisWetsConstants")));
  dialog.getContentPane().add(jPanelSolisWets, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
  final DoubleField jDFl1 = new DoubleField();
  jDFl1.setValue(this.l1);
  jDFl1.setEditable(false);
  final DoubleField jDFl2 = new DoubleField();
  jDFl2.setValue(this.l2);
  jDFl2.setEditable(false);
  final DoubleField jDFl3 = new DoubleField();
  jDFl3.setValue(this.l3);
  jDFl3.setEditable(false);
  jPanelSolisWets.add(new JLabel("l1:"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
  jPanelSolisWets.add(jDFl1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 20, 0));
  jPanelSolisWets.add(new JLabel("l2:"), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
  jPanelSolisWets.add(jDFl2, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 20, 0));
  jPanelSolisWets.add(new JLabel("l3:"), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
  jPanelSolisWets.add(jDFl3, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 20, 0));

  // ecart type bruit gaussien
  JLabel jLabelStdGaussianNoise = new JLabel(LocaleKBCT.GetString("StdGaussianNoise") + ":");
  final DoubleField jDFStdGaussianNoise = new DoubleField();
  dialog.getContentPane().add(jLabelStdGaussianNoise, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
  jDFStdGaussianNoise.setValue(this.StdGaussianNoise);
  dialog.getContentPane().add(jDFStdGaussianNoise, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 40, 0));

  // nombre de contraintes
  JLabel jLabelMaxOfConstraints = new JLabel(LocaleKBCT.GetString("MaximumNumberOfConstraints") + ":");
  final IntegerField jIFMaxOfConstraints = new IntegerField();
  dialog.getContentPane().add(jLabelMaxOfConstraints, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
  jIFMaxOfConstraints.setValue(this.MaxOfConstraints);
  dialog.getContentPane().add(jIFMaxOfConstraints, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 40, 0));

  // nombre d'echecs
  final IntegerField jIFMaxOfFailures = new IntegerField();
  jIFMaxOfFailures.setValue(this.MaxOfFailures);
  
  // nombre d'iterations
  dialog.getContentPane().add(this.jLabelMaxOfIter, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
	  ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
  dialog.getContentPane().add(this.jIFMaxOfIter, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
	  ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 40, 0));
  
  // panel validation
  JPanel jPanelValidation = new JPanel();
  JButton jbApply = new JButton(LocaleKBCT.GetString("Apply"));
  jbApply.addActionListener(new ActionListener()
    {
    public void actionPerformed(ActionEvent e) {
      JOptFrame.this.SeedValue = jIFSeedValue.getValue();
      JOptFrame.this.l1 = jDFl1.getValue();
      JOptFrame.this.l2 = jDFl2.getValue();
      JOptFrame.this.l3 = jDFl3.getValue();
      JOptFrame.this.StdGaussianNoise = jDFStdGaussianNoise.getValue();
      //if( JOptFrame.this.MaxOfConstraints != jIFMaxOfConstraints.getValue() )
        //JOptFrame.this.SaisieMaxConstraints = true;
      JOptFrame.this.MaxOfConstraints = jIFMaxOfConstraints.getValue();
      //if( JOptFrame.this.MaxOfFailures != jIFMaxOfFailures.getValue() )
        //JOptFrame.this.SaisieMaxFailures = true;
      JOptFrame.this.MaxOfFailures = jIFMaxOfFailures.getValue();
      dialog.dispose();
      }
    });
  JButton jbCancel = new JButton(LocaleKBCT.GetString("Cancel"));
  jbCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { dialog.dispose(); } });
  jPanelValidation.add(jbApply);
  jPanelValidation.add(jbCancel);
  dialog.getContentPane().add(jPanelValidation, new GridBagConstraints(0, 5, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

  // affichage
  dialog.pack();
  dialog.setModal(true);
  //dialog.setResizable(false);
  dialog.setLocation(JChildFrame.ChildPosition(JOptFrame.this, dialog.getSize()));
  dialog.setVisible(true);
  }
//------------------------------------------------------------------------------
  private int SelectedOutput() throws Throwable {
  Enumeration items = this.jBGOutput.getElements();
  for( int i=0 ; i<this.fis.NbOutputs() ; i++ )
    if( this.fis.GetOutput(i).GetActive() == true )
      if( ((JRadioButton)items.nextElement()).isSelected() )
        return i;
  throw new Exception("NoOutputFound");
  }
//------------------------------------------------------------------------------
  private String GetKey() {
  String key = new String();
  for( int i=this.jVectorKey.size()-1 ; i>=0 ; i-- ) {
	   boolean sel= ((AbstractButton)this.jVectorKey.elementAt(i)).isSelected();
	   boolean vis= ((AbstractButton)this.jVectorKey.elementAt(i)).isVisible();
	   //System.out.println("i="+i+"   sel="+sel+"   vis="+vis);
	   if (sel && vis)
             key = key + "1";
	   else
             key = key + "0";
  }
  //System.out.println("KEY: "+key);
  return key;
  }
//------------------------------------------------------------------------------
  void jButtonApply_actionPerformed(ActionEvent e) {
	  //System.out.println(this.GetKey());
	  this.Apply();
  }
//------------------------------------------------------------------------------
  protected String Apply(int in, int[] ul) throws Throwable {
	  this.Input_number= in;
	  this.UsedLabels= ul;
	  this.jbInit();
	  return this.Apply();
  }
//------------------------------------------------------------------------------
  protected String Apply(int in, int ln) throws Throwable {
	  this.Input_number= in;
	  this.Label_Number= ln;
	  this.jbInit();
	  return this.Apply();
  }
//------------------------------------------------------------------------------
  protected String Apply() {
        String result=null;
	    try {
    	  //System.out.println("JOptFrame: Apply1");
	      this.InitMaxFailuresConstraints();
    	  //System.out.println("JOptFrame: Apply2");
          double[][] qold= this.FISquality(this.fis, this.kbct, this.DataFile);
    	  //System.out.println("qold.length="+qold.length+"  qold[0].length="+qold[0].length);
          //for (int kk=0; kk<qold.length; kk++) {
            //  for (int hh=0; hh<qold[0].length; hh++) {
        	  //     System.out.println(qold[kk][hh]);
            //  }
          //}
          //System.out.println("qold[0][0]="+qold[0][0]);
          //System.out.println("this.DataFile.DataLength()="+this.DataFile.DataLength());
          boolean warning= false;
          double accfispro= 1 - qold[0][0]/this.DataFile.DataLength();
          if (qold[0][0] > this.DataFile.DataLength()) {
        	  accfispro=qold[0][0];
        	  warning= true;
          }
          double covfispro= qold[0][1];
          if (warning) {
    	      MessageKBCT.WriteLogFile("      "+"RMSE1="+accfispro, "Optimization");
	      } else {
        	  MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("Accuracy")+"1="+this.df.format(accfispro), "Optimization");
          }
    	  MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("Coverage")+"1="+this.df.format(covfispro), "Optimization");
	      //System.out.println("NewQuality[n][1]: "+NewQuality[n][1]+" -> OldQuality[n][1]: "+OldQuality[n][1]);
    	  //System.out.println("JOptFrame: Apply3");
    	  //System.out.println("Generating newfis");
    	  //System.out.println(this.GetKey());
          //System.out.println(this.jIFMaxOfIter.getValue());
          //System.out.println(this.StdGaussianNoise);
          //System.out.println(this.MaxOfConstraints);
          //System.out.println(this.MaxOfFailures);
          //System.out.println(this.jDFDistanceThres.getValue());
          //System.out.println(this.SelectedOutput());
          //System.out.println(this.cNear.isSelected());
          //System.out.println(this.SeedValue);
          //System.out.println(this.l1);
          //System.out.println(this.l2);
          //System.out.println(this.l3);
          //System.out.println(this.jDFBlankThres.getValue());
          //System.out.println(this.jDFLossOfCoverage.getValue());
          JFIS new_fis = this.fis.NewCustomFISOPT(this.DataFile, this.GetKey(), this.jIFMaxOfIter.getValue(), this.StdGaussianNoise, this.MaxOfConstraints, this.MaxOfFailures, this.jDFDistanceThres.getValue(), this.SelectedOutput(), this.cNear.isSelected(), this.SeedValue, this.l1, this.l2, this.l3, 5, this.jDFBlankThres.getValue(), this.jDFLossOfCoverage.getValue() / 100.0);
          //if( new_fis != null ) { JFISFrame jf = new JFISFrame(new_fis, this.DataFile, this.Parent); }
          //else { MessageKBCT.Information(this, LocaleKBCT.GetString("NoPerformanceImprovement")); }
          //this.dispose();
          
          //System.out.println("JOptFrame: Apply4");
	      if( new_fis != null ) {
	    	  //System.out.println("-> newfis generated");
              if (!automatic) {
	              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticOptimizationEnded"));
              }
              //////////////////////////////
              // Generar un nuevo FIS      
              // Actualizando las etiquetas OR y NOT
              //////////////////////////////
              double[][] qnew= this.FISquality(new_fis, this.kbct, this.DataFile);
              if (this.GetBetterFISquality(qold, qnew)) {
    	    	  //System.out.println("-> improved quality");
		          result=LocaleKBCT.GetString("AccuracyImprovement");
	              for (int n=0; n<this.kbct.GetNbInputs(); n++) {
		    		  JKBCTInput in= this.kbct.GetInput(n+1);
		    		  for (int k=0; k<in.GetLabelsNumber(); k++) {
		    			  LabelKBCT lab= in.GetLabel(k+1);
		    			  double[] Params= new_fis.GetInput(n).GetMF(k).GetParams();
		    			  if (lab.GetParams().length==Params.length) {
		    			    for (int i=0; i<Params.length; i++) {
		    				  //if (n==12)
		    				    //  System.out.println("P["+i+"]="+Params[i]);
		    				  switch (i+1) {
		    				    case 1: lab.SetP1(Params[i]);
		    				    	    break;
		    				    case 2: lab.SetP2(Params[i]);
		    				            break;
		    				    case 3: lab.SetP3(Params[i]);
		    				            break;
		    				    case 4: lab.SetP4(Params[i]);
		    				            break;
		    				  }
		    			    }
		    			  in.ReplaceLabel(k+1,lab);
		    			  } /*else {
			    			  System.out.println("n="+n);
			    			  System.out.println("k="+k);
			    			  System.out.println("nbparams="+lab.GetParams().length);
			    			  System.out.println("nbparams-new="+Params.length);
		    			  }*/
		    		  }
		    		  this.kbct.ReplaceInput(n+2, in);
		    	  }
    	          accfispro= 1 - qnew[0][0]/this.DataFile.DataLength();
    	          if (qold[0][0] > this.DataFile.DataLength()) {
    	        	  accfispro=qnew[0][0];
    	        	  warning= true;
    	          }
    	          covfispro= qnew[0][1];
    	          if (warning) {
    	    	      MessageKBCT.WriteLogFile("      "+"RMSE2="+accfispro, "Optimization");
    		      } else {
    	        	  MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("Accuracy")+"2="+this.df.format(accfispro), "Optimization");
    	          }
    	    	  MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("Coverage")+"2="+this.df.format(covfispro), "Optimization");
	              this.kbct.Save();
	              this.kbct.SaveFIS(this.fis_name);
	              this.fis= new JFIS(this.fis_name);
              } else {
    	    	  //System.out.println(" -> No improvement 1");
                  if (!automatic) {
    	    	      MessageKBCT.Information(this, LocaleKBCT.GetString("NoAccuracyImprovement"));
                  }
		          result=LocaleKBCT.GetString("NoAccuracyImprovement");
              }
              new_fis.Close();
	      } else {
	    	  //System.out.println(" -> No improvement 2");
              if (!automatic) {
	    	      MessageKBCT.Information(this, LocaleKBCT.GetString("NoAccuracyImprovement"));
              } 
	          result=LocaleKBCT.GetString("NoAccuracyImprovement");
	      }
	      this.dispose();
	      return result;
	    } catch(Throwable t) {
	    	t.printStackTrace();
	        MessageKBCT.WriteLogFile(t.getLocalizedMessage(), "Optimization");
	        if (!MainKBCT.getConfig().GetTESTautomatic()) {
	            MessageKBCT.Error(null, t);
	        } else if (t.getLocalizedMessage().equals("Java heap space")) {
		    	//t.printStackTrace();
	        	System.exit(-1);
	        }
	    	return null;
	    }
  }
//------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Evaluate quality of knowledge base contains in "kbctaux" after merging rules "r1" and "r2".
   * Return an array with next information:
   * <ul>
   *   <li> Performance. </li>
   *   <li> Coverage. </li>
   *   <li> Number of error cases. </li>
   *   <li> Number of ambiguity cases. </li>
   * </ul>
   * </p>
   * @return quality indices [number of output] [ [performance], [coverage], [error cases], [ambiguity cases] ]
   */
  public double[][] FISquality( JFIS fiskb, JKBCT kbctaux, JExtendedDataFile jedf ) {
    double[][] result= null;
    try {
      JExtendedDataFile DataFile= jedf;
      if ( (DataFile!=null) && (fiskb.NbRules() > 0) ) {
        File temp= JKBCTFrame.BuildFile("temprbqcons.fis");
        int NbOutputs= fiskb.NbOutputs();
        result= new double[NbOutputs][4];
        for (int n=0; n<NbOutputs; n++) {
          String ResultFile=temp.getParentFile() + System.getProperty("file.separator") + "resultcons";
          double[] inf_result= fiskb.Infer(n, DataFile.ActiveFileName(), ResultFile, 0.1, false );
          result[n][0]=inf_result[0];
          result[n][1]=inf_result[1]*100;
          fis.JOutput output = fiskb.GetOutput(n);
          if (output.GetNature()!=0) {
            JKBCTOutput jout= kbctaux.GetOutput(n+1);
            int NbLabels= jout.GetLabelsNumber();
            PerformanceFile PerfFile= new PerformanceFile(ResultFile);
            double[][] data= PerfFile.GetData();
            int NbData= PerfFile.DataLength();
            double[] observed= new double[NbData];
            double[] infered= new double[NbData];
            double[] warning= new double[NbData];
            double[] error= new double[NbData];
            for (int m=0; m<PerfFile.DataLength(); m++) {
              observed[m]= data[m][0];
              infered[m]= data[m][1];
              warning[m]= data[m][2];
              if (observed[m] != infered[m])
                error[m]= 1;
              else
                error[m]= 0;
            }
            double NbOutputError= 0;
            double NbOutputAmbiguity= 0;
            for (int k=0; k<NbData; k++) {
              for (int m=0; m<NbLabels; m++) {
                if (observed[k]==m+1) {
                  if (warning[k]==2) {
                     NbOutputAmbiguity++;
                  } else if (error[k]!=0)
                     NbOutputError++;
                }
              }
            }
            result[n][2]= NbOutputError;  // NbErrorsCases
            result[n][3]= NbOutputAmbiguity;  // NbAmbiguityCases
          } else {
            // numerical
            result[n][2]= -1;  // NbErrorsCases
            result[n][3]= -1;  // NbAmbiguityCases
          }
        }
    }
    } catch (java.lang.OutOfMemoryError e) {
        MessageKBCT.Error(null, e);
    } catch (Throwable t) {
        //t.printStackTrace();
        MessageKBCT.Error(null, t);
    }
    return result;
  }
//------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Check if "OldQuality" is better than "NewQuality".
   * </p>
   * @param OldQuality quality indices of the knowledge base before changes
   * @param NewQuality quality indices of the knowledge base after changes
   * @return true (precision is reduced) / false
   */
  public boolean GetBetterFISquality( double[][] OldQuality, double[][] NewQuality ) {
	if ( (OldQuality==null) && (NewQuality==null) ) {
      return false;
    }
    if ( (OldQuality!=null) && (NewQuality==null) ) {
      return false;
    }
    for (int n=0; n<OldQuality.length; n++) {
    	// Quality[0] -> Error (Performance) 
    	// Quality[1] -> Coverage
    	// Quality[2] -> ?
    	// Quality[3] -> MaxError
      //System.out.println("NewQuality[n][1]: "+NewQuality[n][1]+" -> OldQuality[n][1]: "+OldQuality[n][1]);
      //System.out.println("NewQuality[n][0]: "+NewQuality[n][0]+" -> OldQuality[n][1]: "+OldQuality[n][0]);
      //System.out.println("NewQuality[n][2]: "+NewQuality[n][2]+" -> OldQuality[n][1]: "+OldQuality[n][2]);
      //System.out.println("NewQuality[n][3]: "+NewQuality[n][3]+" -> OldQuality[n][1]: "+OldQuality[n][3]);
      if ( (NewQuality[n][1] > OldQuality[n][1]) &&
       	   (NewQuality[n][0] <= OldQuality[n][0]) ) {
            return true;
      }
      if ( (NewQuality[n][1] >= OldQuality[n][1]) &&
           (NewQuality[n][0] < OldQuality[n][0]) ) {
            return true;
      }
      if ( (NewQuality[n][1] > OldQuality[n][1]) &&
           (NewQuality[n][0] > OldQuality[n][0]) &&
           (NewQuality[n][0]-OldQuality[n][1] > NewQuality[n][0]-OldQuality[n][0]) ) {
            return true;
      }
      if ( (NewQuality[n][1] < OldQuality[n][1]) &&
           (NewQuality[n][0] < OldQuality[n][0]) &&
           (OldQuality[n][1]-NewQuality[n][1] < OldQuality[n][0]-NewQuality[n][0]) ) {
               return true;
      }
    }
    return false;
  }
//------------------------------------------------------------------------------
  void jButtonAllInputs_actionPerformed(ActionEvent e) {
    this.InputsSelected = !this.InputsSelected;
    this.OutputSelected = false;
    //this.RulesSelected = false;
    for( int i=0;i<=this.jVectorInputs.size()-1 ; i++ ) {
      ((AbstractButton)this.jVectorInputs.elementAt(i)).setSelected(this.InputsSelected);
      ((AbstractButton)this.jVectorInputs.elementAt(i)).setEnabled(true);
      }
    for( int i=0;i<=this.jVectorOutput.size()-1 ; i++ ) {
      ((AbstractButton)this.jVectorOutput.elementAt(i)).setSelected(false);
      ((AbstractButton)this.jVectorOutput.elementAt(i)).setEnabled(false);
      }
    for( int i=0;i<=this.jVectorRules.size()-1 ; i++ ) {
      ((AbstractButton)this.jVectorRules.elementAt(i)).setSelected(false);
      ((AbstractButton)this.jVectorRules.elementAt(i)).setEnabled(false);
      }
    this.jtfKey.setText(this.GetKey());
    }
//------------------------------------------------------------------------------
  void jButtonAllOutputs_actionPerformed(ActionEvent e) {
    this.OutputSelected = !this.OutputSelected;
    //this.RulesSelected = false;
    for( int i=0;i<=this.jVectorOutput.size()-1 ; i++ ) {
      ((AbstractButton)this.jVectorOutput.elementAt(i)).setSelected(this.OutputSelected);
      ((AbstractButton)this.jVectorOutput.elementAt(i)).setEnabled(true);
      }
    for( int i=0;i<=this.jVectorRules.size()-1 ; i++ ) {
      ((AbstractButton)this.jVectorRules.elementAt(i)).setSelected(false);
      ((AbstractButton)this.jVectorRules.elementAt(i)).setEnabled(false);
      }
    this.jtfKey.setText(this.GetKey());
    }
//------------------------------------------------------------------------------
  private void InitMaxFailuresConstraints()
    {
    this.MaxOfFailures= 1000;
    this.MaxOfConstraints= 1000;
    }
}