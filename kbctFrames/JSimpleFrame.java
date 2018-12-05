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
//                           JSimpleFrame.java
//
//
//**********************************************************************

package kbctFrames;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import kbct.JKBCT;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbctAux.DoubleField;
import kbctAux.IntegerField;
import kbctAux.MessageKBCT;
import fis.JFISDialog;

/**
 * kbctFrames.JSimpleFrame
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
//------------------------------------------------------------------------------
public class JSimpleFrame extends JFISDialog {
  static final long serialVersionUID=0;	
  private JKBCTFrame Parent;
  private JKBCT kbct;
  private ButtonGroup jbgOutput = new ButtonGroup();
  private JPanel jPanelOutput = new JPanel(new GridBagLayout());
  private JRadioButton jRBonlyDBsimp = new JRadioButton();
  private JRadioButton jRBonlyRBsimp = new JRadioButton();
  private JRadioButton jRBFirstReduceRuleBase = new JRadioButton();
  private JRadioButton jRBFirstReduceDataBase = new JRadioButton();
  private DoubleField jDFMaximumLossOfCoverage = new DoubleField();
  private JRadioButton jRBLossOfCoverage = new JRadioButton();
  private IntegerField jIFMaximumNumberNewUnclassifiedCases = new IntegerField();
  private JRadioButton jRBunclassifiedCases = new JRadioButton();
  private JRadioButton jRBLossOfPerformance = new JRadioButton();
  private DoubleField jDFMaximumLossOfPerformance = new DoubleField();
  private IntegerField jIFMaximumNumberNewErrorCases = new IntegerField();
  private JRadioButton jRBerrorCases = new JRadioButton();
  private JLabel jMaximumNumberNewAmbiguityCases = new JLabel();
  private IntegerField jIFMaximumNumberNewAmbiguityCases = new IntegerField();
  private JCheckBox jCBruleRemoval = new JCheckBox();
  private JCheckBox jCBvariableRemoval = new JCheckBox();
  private JCheckBox jCBpremiseRemoval = new JCheckBox();
  private JCheckBox jCBorderRules = new JCheckBox();
  private JPanel jPanelValidation = new JPanel(new GridBagLayout());
  private JButton jButtonApply = new JButton();
  private JButton jButtonCancel = new JButton();
  private JButton jButtonDefault = new JButton();
  private int NbTotalCases= 0;
  private JComboBox jCBoutputClass;
  private int NbOutputClasses=0;
  private JComboBox jCBoptOR;
  private boolean classif= false;
  //------------------------------------------------------------------------------
  public JSimpleFrame(JExpertFrame parent, JKBCT kbct, int totalCases) {
    super(parent);
    this.Parent = parent;
    this.kbct= kbct;
    this.NbTotalCases= totalCases;
    try {
      jbInit();
    } catch(Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(null, t);
    }
  }
  //------------------------------------------------------------------------------
  public JSimpleFrame(JMainFrame parent, JKBCT kbct) {
    super(parent);
    this.Parent = parent;
    this.kbct= kbct;
    this.NbTotalCases= 0;
    try {
      jbInit();
    } catch(Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(null, t);
    }
  }
//------------------------------------------------------------------------------
  private void jbInit() throws Throwable {
	this.setIconImage(LocaleKBCT.getIconGUAJE().getImage());
    this.getContentPane().setLayout(new GridBagLayout());
    this.jPanelOutput.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),LocaleKBCT.GetString("Outputs")));
    boolean first_active = false;
    int lim=1;
    if (this.kbct!=null) {
    	lim=this.kbct.GetNbOutputs();
    	if (this.kbct.GetOutput(1).GetClassif().equals("yes"))
    	    this.classif= true;
    }
    for( int i=0 ; i<lim ; i++ ) {
      JRadioButton jrb;
      if (this.kbct!=null)
    	  jrb= new JRadioButton(this.kbct.GetOutput(i+1).GetName());
      else
    	  jrb= new JRadioButton(LocaleKBCT.GetString("Output"));
    	  
      this.jbgOutput.add(jrb);
      if( i == MainKBCT.getConfig().GetSelectedOutput()-1 )
        jrb.setSelected(true);
      if ( (this.kbct!=null) && ( this.kbct.GetOutput(i+1).GetActive() == false ) )
        jrb.setEnabled(false);
      else {
        if( first_active == false ) {
          jrb.setSelected(true);
          first_active = true;
        }
      }
      this.jPanelOutput.add(jrb, new GridBagConstraints(i%2==0?0:1, i/2, 1, 1, 0.0, 0.0
          ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets( 0, 0, 0, 0), 0, 0));
      }
    this.getContentPane().add(this.jPanelOutput,   new GridBagConstraints(0, 0, 5, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));

    this.getContentPane().add(this.jRBonlyDBsimp,   new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    this.getContentPane().add(this.jRBonlyRBsimp,   new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    if (this.kbct!=null) {
    	this.NbOutputClasses= this.kbct.GetOutput(1).GetLabelsNumber();
    } else {
    	this.NbOutputClasses=1;
    }
   	DefaultComboBoxModel jDCBMoutputClassModel = new DefaultComboBoxModel() {
   	  static final long serialVersionUID=0;	
   	  public Object getElementAt(int index) {
   		        String out= String.valueOf(index+1);
                if (index==JSimpleFrame.this.NbOutputClasses)
                  	out=LocaleKBCT.GetString("all");

                try { return out; }
   		        catch( Throwable t ) { return null; }
   		      }
   		    };
   	this.jCBoutputClass = new JComboBox(jDCBMoutputClassModel);
    for(int i=0; i<this.NbOutputClasses+1; i++)
        jDCBMoutputClassModel.addElement(new String());
    
    //System.out.println("this.classif -> "+this.classif);
    if (this.classif) {
    	String prevSelOutCl= MainKBCT.getConfig().GetOutputClassSelected();
        //System.out.println("prevSelOutCl="+prevSelOutCl);
        if (!prevSelOutCl.equals(LocaleKBCT.GetString("all"))) {
	        int ind= (new Integer(prevSelOutCl)).intValue()-1;
            if ( (ind >= 0) && (ind <= this.NbOutputClasses) ) {
       	        jCBoutputClass.setSelectedIndex(ind);
            } else {
    	        jCBoutputClass.setSelectedIndex(this.NbOutputClasses);
            }
        } else {
    	    jCBoutputClass.setSelectedIndex(this.NbOutputClasses);
        }
    } else {
    	jCBoutputClass.setSelectedIndex(this.NbOutputClasses);
    	jCBoutputClass.setEditable(false);
    	jCBoutputClass.setEnabled(false);
    }
    this.getContentPane().add(jCBoutputClass,   new GridBagConstraints(1, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    boolean onlyDBsimp= MainKBCT.getConfig().GetOnlyDBsimplification();
    if (onlyDBsimp) {
    	this.jRBonlyDBsimp.setSelected(true);
    	this.jRBonlyRBsimp.setSelected(false);
        this.jCBoutputClass.setEnabled(false);
        this.jRBFirstReduceRuleBase.setEnabled(false);
        this.jRBFirstReduceDataBase.setEnabled(false);
    }
  	boolean onlyRBsimp= MainKBCT.getConfig().GetOnlyRBsimplification();
  	if (onlyRBsimp) {
    	this.jRBonlyDBsimp.setSelected(false);
    	this.jRBonlyRBsimp.setSelected(true);
    	if (this.classif)
            this.jCBoutputClass.setEnabled(true);
    	
        this.jRBFirstReduceRuleBase.setEnabled(false);
        this.jRBFirstReduceDataBase.setEnabled(false);
    } else {
        this.jCBoutputClass.setEnabled(false);
    }
    boolean frrbFLAG= MainKBCT.getConfig().GetFirstReduceRuleBase();
    this.getContentPane().add(this.jRBFirstReduceRuleBase,     new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.jRBFirstReduceRuleBase.setSelected(frrbFLAG);
    this.getContentPane().add(this.jRBFirstReduceDataBase,     new GridBagConstraints(0, 4, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.jRBFirstReduceDataBase.setSelected(!frrbFLAG);
    this.getContentPane().add(this.jRBLossOfCoverage,     new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    this.jRBLossOfCoverage.setSelected(true);
    this.jDFMaximumLossOfCoverage.setValue(MainKBCT.getConfig().GetMaximumLossOfCoverage());
    this.jDFMaximumLossOfCoverage.setEnabled(true);
    this.getContentPane().add(this.jDFMaximumLossOfCoverage,   new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));
    this.getContentPane().add(new JLabel("%"),  new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    this.getContentPane().add(this.jRBunclassifiedCases,     new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.jRBunclassifiedCases.setSelected(false);
    this.jIFMaximumNumberNewUnclassifiedCases.setValue(MainKBCT.getConfig().GetMaximumNumberNewUnclassifiedCases());
    this.getContentPane().add(this.jIFMaximumNumberNewUnclassifiedCases,   new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));
    this.jIFMaximumNumberNewUnclassifiedCases.setEnabled(false);

    this.getContentPane().add(this.jRBLossOfPerformance,     new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
             ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    boolean perfFLAG= MainKBCT.getConfig().GetSelectedPerformance();
    this.jRBLossOfPerformance.setSelected(perfFLAG);
    this.jDFMaximumLossOfPerformance.setValue(MainKBCT.getConfig().GetMaximumLossOfPerformance());
    this.jDFMaximumLossOfPerformance.setEnabled(perfFLAG);
    this.getContentPane().add(this.jDFMaximumLossOfPerformance,   new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));
    this.getContentPane().add(new JLabel("%"),  new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    this.getContentPane().add(this.jRBerrorCases,     new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.jRBerrorCases.setSelected(!perfFLAG);
    this.jIFMaximumNumberNewErrorCases.setValue(MainKBCT.getConfig().GetMaximumNumberNewErrorCases());
    this.getContentPane().add(this.jIFMaximumNumberNewErrorCases,   new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));
    this.jIFMaximumNumberNewErrorCases.setEnabled(!perfFLAG);

    this.getContentPane().add(this.jMaximumNumberNewAmbiguityCases,    new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 26, 5, 5), 0, 0));
    this.jIFMaximumNumberNewAmbiguityCases.setValue(MainKBCT.getConfig().GetMaximumNumberNewAmbiguityCases());
    this.getContentPane().add(this.jIFMaximumNumberNewAmbiguityCases,   new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));
    this.jIFMaximumNumberNewAmbiguityCases.setEnabled(!perfFLAG);

    this.jCBruleRemoval.setSelected(MainKBCT.getConfig().GetRuleRemoval());
    this.getContentPane().add(this.jCBruleRemoval,   new GridBagConstraints(0, 10, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    this.jCBvariableRemoval.setSelected(MainKBCT.getConfig().GetVariableRemoval());
    this.getContentPane().add(this.jCBvariableRemoval,   new GridBagConstraints(0, 11, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    this.jCBpremiseRemoval.setSelected(MainKBCT.getConfig().GetPremiseRemoval());
    this.getContentPane().add(this.jCBpremiseRemoval,   new GridBagConstraints(0, 12, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    String opt= MainKBCT.getConfig().GetSimpRuleRanking();
    if (!opt.equals("false"))
        this.jCBorderRules.setSelected(true);
    else
        this.jCBorderRules.setSelected(false);
    	
   	DefaultComboBoxModel jDCBMoptORClassModel = new DefaultComboBoxModel() {
     	  static final long serialVersionUID=0;	
     	  public Object getElementAt(int index) {
     		     String out= "false";
                 switch(index) {
                   case 1: out= LocaleKBCT.GetString("IncreasingRuleComplexity");
                	       break;
                   case 2: out= LocaleKBCT.GetString("DecreasingRuleComplexity");
        	               break;
                   case 3: out= LocaleKBCT.GetString("ComparisonRuleComplexity");
                           break;
                   case 4: out= LocaleKBCT.GetString("RandomIDRuleComplexity");
	                       break;
                   default: out= "";
        	                break;
                 }
                 try { return out; }
     		        catch( Throwable t ) { return null; }
     		     }
     		    };
    this.jCBoptOR= new JComboBox(jDCBMoptORClassModel);
    for(int i=0; i<5; i++)
     jDCBMoptORClassModel.addElement(new String());

    String prevSeloptOR= MainKBCT.getConfig().GetSimpRuleRanking();
    //System.out.println("prevSeloptOR="+prevSeloptOR);
    if (!prevSeloptOR.equals("false")) {
        this.jCBoptOR.setEnabled(true);
        if (prevSeloptOR.equals("IncreasingRuleComplexity"))
       	    this.jCBoptOR.setSelectedIndex(1);
        else if (prevSeloptOR.equals("DecreasingRuleComplexity"))
       	    this.jCBoptOR.setSelectedIndex(2);
        else if (prevSeloptOR.equals("ComparisonRuleComplexity"))
       	    this.jCBoptOR.setSelectedIndex(3);
        else
       	    this.jCBoptOR.setSelectedIndex(4);
    } else {
        this.jCBoptOR.setEnabled(false);
        this.jCBoptOR.setSelectedIndex(0);
    }
    this.getContentPane().add(this.jCBorderRules,   new GridBagConstraints(0, 13, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.getContentPane().add(this.jCBoptOR,   new GridBagConstraints(1, 13, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    
    // panel validation
    jPanelValidation.add(jButtonApply,  new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    jPanelValidation.add(jButtonCancel,  new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    jPanelValidation.add(jButtonDefault,  new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.getContentPane().add(jPanelValidation,          new GridBagConstraints(0, 14, 5, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

    // evenements
    this.jRBonlyDBsimp.addItemListener(new java.awt.event.ItemListener() { public void itemStateChanged(ItemEvent e) { jRBonlyDBsimp_itemStateChanged(); } });
    this.jRBonlyRBsimp.addItemListener(new java.awt.event.ItemListener() { public void itemStateChanged(ItemEvent e) { jRBonlyRBsimp_itemStateChanged(); } });
    this.jRBFirstReduceRuleBase.addItemListener(new java.awt.event.ItemListener() { public void itemStateChanged(ItemEvent e) { jRBFirstReduceRuleBase_itemStateChanged(); } });
    this.jRBFirstReduceDataBase.addItemListener(new java.awt.event.ItemListener() { public void itemStateChanged(ItemEvent e) { jRBFirstReduceDataBase_itemStateChanged(); } });
    this.jRBLossOfCoverage.addItemListener(new java.awt.event.ItemListener() { public void itemStateChanged(ItemEvent e) { jRBLossOfCoverage_itemStateChanged(); } });
    this.jRBunclassifiedCases.addItemListener(new java.awt.event.ItemListener() { public void itemStateChanged(ItemEvent e) { jRBunclassifiedCases_itemStateChanged(); } });
    this.jRBLossOfPerformance.addItemListener(new java.awt.event.ItemListener() { public void itemStateChanged(ItemEvent e) { jRBLossOfPerformance_itemStateChanged(); } });
    this.jRBerrorCases.addItemListener(new java.awt.event.ItemListener() { public void itemStateChanged(ItemEvent e) { jRBerrorCases_itemStateChanged(); } });
    this.jRBerrorCases.addItemListener(new java.awt.event.ItemListener() { public void itemStateChanged(ItemEvent e) { jRBerrorCases_itemStateChanged(); } });
    this.jCBorderRules.addItemListener(new ItemListener() { public void itemStateChanged(ItemEvent e) { jCBorderRules_itemStateChanged(); }});
    jButtonApply.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonApply_actionPerformed(); } });
    jButtonCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonCancel_actionPerformed(); } });
    jButtonDefault.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonDefault_actionPerformed(); } });
    // affichage
    this.Translate();
    this.pack();
    this.setResizable(false);
    this.setModal(true);
    this.setLocation(JChildFrame.ChildPosition(this.Parent, this.getSize()));
    this.setVisible(true);
  }
//------------------------------------------------------------------------------
  private void Translate() {
    this.setTitle(LocaleKBCT.GetString("Simple"));
    this.jRBonlyDBsimp.setText(LocaleKBCT.GetString("OnlyDBsimplification"));
    this.jRBonlyRBsimp.setText(LocaleKBCT.GetString("OnlyRBsimplification"));
    this.jRBFirstReduceRuleBase.setText(LocaleKBCT.GetString("FirstReduceRuleBase"));
    this.jRBFirstReduceDataBase.setText(LocaleKBCT.GetString("FirstReduceDataBase"));
    this.jRBLossOfCoverage.setText(LocaleKBCT.GetString("MaximumLossOfCoverage") + ":");
    this.jRBunclassifiedCases.setText(LocaleKBCT.GetString("MaximumNumberNewUnclassifiedCases") + ":");
    this.jRBLossOfPerformance.setText(LocaleKBCT.GetString("MaximumLossOfPerformance") + ":");
    this.jRBerrorCases.setText(LocaleKBCT.GetString("MaximumNumberNewErrorCases") + ":");
    this.jMaximumNumberNewAmbiguityCases.setText(LocaleKBCT.GetString("MaximumNumberNewAmbiguityCases") + ":");
    this.jCBruleRemoval.setText(LocaleKBCT.GetString("ruleRemoval"));
    this.jCBvariableRemoval.setText(LocaleKBCT.GetString("variableRemoval"));
    this.jCBpremiseRemoval.setText(LocaleKBCT.GetString("premiseRemoval"));
    this.jCBorderRules.setText(LocaleKBCT.GetString("orderRules"));
    jButtonApply.setText(LocaleKBCT.GetString("Apply"));
    jButtonCancel.setText(LocaleKBCT.GetString("Cancel"));
    jButtonDefault.setText(LocaleKBCT.GetString("DefaultValues"));
  }
//------------------------------------------------------------------------------
  private int OutputSelected() {
  int retour = 0;
  for( Enumeration e = this.jbgOutput.getElements() ; e.hasMoreElements() ; retour++ )
    if( ((AbstractButton)e.nextElement()).isSelected() == true )
      return retour;
  return retour;
  }
//------------------------------------------------------------------------------
  void jButtonApply_actionPerformed() {
	MainKBCT.getConfig().SetOnlyDBsimplification(this.jRBonlyDBsimp.isSelected());
	MainKBCT.getConfig().SetOnlyRBsimplification(this.jRBonlyRBsimp.isSelected());
    if (this.jRBonlyRBsimp.isSelected())
	    MainKBCT.getConfig().SetOutputClassSelected(this.jCBoutputClass.getSelectedItem().toString());
    else
    	MainKBCT.getConfig().SetOutputClassSelected(LocaleKBCT.GetString("all"));
    
	MainKBCT.getConfig().SetFirstReduceRuleBase(this.jRBFirstReduceRuleBase.isSelected());
    MainKBCT.getConfig().SetSelectedOutput(this.OutputSelected()+1);
    if (this.jRBLossOfCoverage.isSelected())
       MainKBCT.getConfig().SetMaximumLossOfCoverage(this.jDFMaximumLossOfCoverage.getValue());
    else if (this.NbTotalCases>0)
       MainKBCT.getConfig().SetMaximumLossOfCoverage((this.jIFMaximumNumberNewUnclassifiedCases.getValue()*100)/this.NbTotalCases);
    else
       MainKBCT.getConfig().SetMaximumLossOfCoverage(LocaleKBCT.DefaultMaximumLossOfCoverage());
    	
    MainKBCT.getConfig().SetMaximumLossOfPerformance(this.jDFMaximumLossOfPerformance.getValue());
    MainKBCT.getConfig().SetMaximumNumberNewErrorCases(this.jIFMaximumNumberNewErrorCases.getValue());
    MainKBCT.getConfig().SetMaximumNumberNewAmbiguityCases(this.jIFMaximumNumberNewAmbiguityCases.getValue());
    MainKBCT.getConfig().SetMaximumNumberNewUnclassifiedCases(this.jIFMaximumNumberNewUnclassifiedCases.getValue());
    MainKBCT.getConfig().SetRuleRemoval(this.jCBruleRemoval.isSelected());
    MainKBCT.getConfig().SetVariableRemoval(this.jCBvariableRemoval.isSelected());
    MainKBCT.getConfig().SetPremiseRemoval(this.jCBpremiseRemoval.isSelected());
    if (!this.jCBorderRules.isSelected()) {
    	MainKBCT.getConfig().SetSimpRuleRanking("false");
    } else {
        if (this.jCBoptOR.getSelectedItem().toString().equals(LocaleKBCT.GetString("IncreasingRuleComplexity")))
        	MainKBCT.getConfig().SetSimpRuleRanking("IncreasingRuleComplexity");
        else if (this.jCBoptOR.getSelectedItem().toString().equals(LocaleKBCT.GetString("DecreasingRuleComplexity")))
        	MainKBCT.getConfig().SetSimpRuleRanking("DecreasingRuleComplexity");
        else if (this.jCBoptOR.getSelectedItem().toString().equals(LocaleKBCT.GetString("ComparisonRuleComplexity")))
        	MainKBCT.getConfig().SetSimpRuleRanking("ComparisonRuleComplexity");
        else if (this.jCBoptOR.getSelectedItem().toString().equals(LocaleKBCT.GetString("RandomIDRuleComplexity")))
        	MainKBCT.getConfig().SetSimpRuleRanking("RandomIDRuleComplexity");
    }
    MainKBCT.getConfig().SetSelectedPerformance(this.jRBLossOfPerformance.isSelected());
    this.dispose();
  }
//------------------------------------------------------------------------------
  void jButtonCancel_actionPerformed() { this.dispose(); }
//------------------------------------------------------------------------------
  void jButtonDefault_actionPerformed() {
        int cont=0;
	    for( Enumeration e = this.jbgOutput.getElements() ; e.hasMoreElements() ; cont++ )
		    if( cont==LocaleKBCT.DefaultSelectedOutput() )
		    	((AbstractButton)e.nextElement()).setSelected(true);
		    else
		    	((AbstractButton)e.nextElement()).setSelected(false);

	    this.jDFMaximumLossOfCoverage.setValue(LocaleKBCT.DefaultMaximumLossOfCoverage());
	    this.jDFMaximumLossOfPerformance.setValue(LocaleKBCT.DefaultMaximumLossOfPerformance());
	    this.jIFMaximumNumberNewErrorCases.setValue(LocaleKBCT.DefaultMaximumNumberNewErrorCases());
	    this.jIFMaximumNumberNewAmbiguityCases.setValue(LocaleKBCT.DefaultMaximumNumberNewAmbiguityCases());
	    this.jIFMaximumNumberNewUnclassifiedCases.setValue(LocaleKBCT.DefaultMaximumNumberNewUnclassifiedCases());
	    this.jCBruleRemoval.setSelected(LocaleKBCT.DefaultRuleRemoval());
	    this.jCBvariableRemoval.setSelected(LocaleKBCT.DefaultVariableRemoval());
	    this.jCBpremiseRemoval.setSelected(LocaleKBCT.DefaultPremiseRemoval());
	    if (LocaleKBCT.DefaultSimpRuleRanking().equals("false")) {
	        this.jCBorderRules.setSelected(false);
	    	this.jCBoptOR.setEnabled(false);
  	        this.jCBoptOR.setSelectedIndex(0);
        } else {
	        this.jCBorderRules.setSelected(true);
	        this.jCBoptOR.setEnabled(true);
	        if (MainKBCT.getConfig().GetSimpRuleRanking().equals("IncreasingRuleComplexity"))
		     	this.jCBoptOR.setSelectedIndex(1);
		    else if (MainKBCT.getConfig().GetSimpRuleRanking().equals("DecreasingRuleComplexity"))
		       	this.jCBoptOR.setSelectedIndex(2);
		    else if (MainKBCT.getConfig().GetSimpRuleRanking().equals("ComparisonRuleComplexity"))
		       	this.jCBoptOR.setSelectedIndex(3);
		    else
		       	this.jCBoptOR.setSelectedIndex(4);
	    }
	    this.jRBonlyDBsimp.setSelected(LocaleKBCT.DefaultOnlyDBsimplification());
        this.jRBonlyRBsimp.setSelected(LocaleKBCT.DefaultOnlyRBsimplification());
        this.jCBoutputClass.setSelectedIndex((new Integer(LocaleKBCT.DefaultOutputClassSelected())).intValue());
        if (this.classif)
        	this.jCBoutputClass.setEnabled(true);
        
        boolean frrbFLAG= LocaleKBCT.DefaultFirstReduceRuleBase();
        this.jRBFirstReduceRuleBase.setSelected(frrbFLAG);
        this.jRBFirstReduceDataBase.setSelected(!frrbFLAG);
        this.jRBLossOfCoverage.setSelected(true);
        this.jRBunclassifiedCases.setSelected(false);
        boolean perfFLAG= LocaleKBCT.DefaultSelectedPerformance();
        this.jRBLossOfPerformance.setSelected(perfFLAG);
        this.jRBerrorCases.setSelected(!perfFLAG);
  }
//------------------------------------------------------------------------------
  void jCBorderRules_itemStateChanged() {
	if( this.jCBorderRules.isSelected() == true ) {
  	    this.jCBoptOR.setEnabled(true);
	} else {
  	    this.jCBoptOR.setEnabled(false);
	}
  }
//------------------------------------------------------------------------------
  void jRBonlyDBsimp_itemStateChanged() {
	if( this.jRBonlyDBsimp.isSelected() == true ) {
        this.jRBonlyRBsimp.setSelected(false);
        this.jCBoutputClass.setEnabled(false);
        this.jRBFirstReduceRuleBase.setEnabled(false);
        this.jRBFirstReduceDataBase.setEnabled(false);
        this.jCBruleRemoval.setEnabled(false);
        this.jCBpremiseRemoval.setEnabled(false);
        this.jCBvariableRemoval.setEnabled(true);
        this.jCBorderRules.setEnabled(false);
	    this.jCBoptOR.setEnabled(false);
	} else {
		if (this.classif)
	        this.jCBoutputClass.setEnabled(true);
		
        this.jRBFirstReduceRuleBase.setEnabled(true);
        this.jRBFirstReduceDataBase.setEnabled(true);
        this.jCBruleRemoval.setEnabled(true);
        this.jCBpremiseRemoval.setEnabled(true);
        this.jCBorderRules.setEnabled(true);
        this.jCBvariableRemoval.setEnabled(true);
        this.jCBorderRules.setEnabled(true);
	}
  }
//------------------------------------------------------------------------------
  void jRBonlyRBsimp_itemStateChanged() {
    if( this.jRBonlyRBsimp.isSelected() == true ) {
        if (this.classif)
    	    this.jCBoutputClass.setEnabled(true);
        
        this.jRBonlyDBsimp.setSelected(false);
        this.jRBFirstReduceRuleBase.setEnabled(false);
        this.jRBFirstReduceDataBase.setEnabled(false);
        this.jCBvariableRemoval.setEnabled(false);
        this.jCBruleRemoval.setEnabled(true);
        this.jCBpremiseRemoval.setEnabled(true);
        this.jCBorderRules.setEnabled(true);
    } else {
        this.jCBoutputClass.setEnabled(false);
        this.jRBFirstReduceRuleBase.setEnabled(true);
        this.jRBFirstReduceDataBase.setEnabled(true);
        this.jCBvariableRemoval.setEnabled(true);
        this.jCBruleRemoval.setEnabled(true);
        this.jCBpremiseRemoval.setEnabled(true);
        this.jCBorderRules.setEnabled(true);
    }
}
//------------------------------------------------------------------------------
  void jRBFirstReduceRuleBase_itemStateChanged() {
    if( this.jRBFirstReduceRuleBase.isSelected() == true )
        this.jRBFirstReduceDataBase.setSelected(false);
    else
        this.jRBFirstReduceDataBase.setSelected(true);
  }
//------------------------------------------------------------------------------
  void jRBFirstReduceDataBase_itemStateChanged() {
    if( this.jRBFirstReduceDataBase.isSelected() == true )
        this.jRBFirstReduceRuleBase.setSelected(false);
    else
        this.jRBFirstReduceRuleBase.setSelected(true);
  }
//------------------------------------------------------------------------------
  void jRBLossOfCoverage_itemStateChanged() {
    if( this.jRBLossOfCoverage.isSelected() == true ) {
      this.jDFMaximumLossOfCoverage.setEnabled(true);
      this.jRBunclassifiedCases.setSelected(false);
      this.jIFMaximumNumberNewUnclassifiedCases.setEnabled(false);
    } else {
      this.jDFMaximumLossOfCoverage.setEnabled(false);
      this.jRBunclassifiedCases.setSelected(true);
      this.jIFMaximumNumberNewUnclassifiedCases.setEnabled(true);
    }
  }
//------------------------------------------------------------------------------
  void jRBunclassifiedCases_itemStateChanged() {
    if( this.jRBunclassifiedCases.isSelected() == true ) {
      this.jRBLossOfCoverage.setSelected(false);
      this.jDFMaximumLossOfCoverage.setEnabled(false);
      this.jIFMaximumNumberNewUnclassifiedCases.setEnabled(true);
    } else {
      this.jRBLossOfCoverage.setSelected(true);
      this.jDFMaximumLossOfCoverage.setEnabled(true);
      this.jIFMaximumNumberNewUnclassifiedCases.setEnabled(false);
    }
  }
//------------------------------------------------------------------------------
  void jRBLossOfPerformance_itemStateChanged() {
    if( this.jRBLossOfPerformance.isSelected() == true ) {
      this.jDFMaximumLossOfPerformance.setEnabled(true);
      this.jRBerrorCases.setSelected(false);
      this.jIFMaximumNumberNewErrorCases.setEnabled(false);
      this.jIFMaximumNumberNewAmbiguityCases.setEnabled(false);
    } else {
      this.jDFMaximumLossOfPerformance.setEnabled(false);
      this.jRBerrorCases.setSelected(true);
      this.jIFMaximumNumberNewErrorCases.setEnabled(true);
      this.jIFMaximumNumberNewAmbiguityCases.setEnabled(true);
    }
  }
//------------------------------------------------------------------------------
  void jRBerrorCases_itemStateChanged() {
    if( this.jRBerrorCases.isSelected() == true ) {
      this.jRBLossOfPerformance.setSelected(false);
      this.jDFMaximumLossOfPerformance.setEnabled(false);
      this.jIFMaximumNumberNewErrorCases.setEnabled(true);
      this.jIFMaximumNumberNewAmbiguityCases.setEnabled(true);
    } else {
      this.jRBLossOfPerformance.setSelected(true);
      this.jDFMaximumLossOfPerformance.setEnabled(true);
      this.jIFMaximumNumberNewErrorCases.setEnabled(false);
      this.jIFMaximumNumberNewAmbiguityCases.setEnabled(false);
    }
  }
}