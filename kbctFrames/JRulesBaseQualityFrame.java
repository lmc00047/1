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
//                        JRulesBaseQualityFrame.java
//
//
//**********************************************************************

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
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import kbct.JFIS;
import kbct.JKBCT;
import kbct.JKBCTOutput;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbctAux.DoubleField;
import kbctAux.JOpenFileChooser;
import kbctAux.MessageKBCT;
import kbctAux.PerformanceFile;

import org.freehep.util.export.ExportDialog;

import print.JPrintPreview;
import fis.JExtendedDataFile;
import fis.JFileListener;
import fis.JRule;
import fis.JSemaphore;
import fis.jnifis;

/**
 * kbctFrames.JRulesBaseQualityFrame generate a frame which is launched
 * when expert selects Quality from JExpertFrame.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
//------------------------------------------------------------------------------
public class JRulesBaseQualityFrame extends JChildFrame {
  static final long serialVersionUID=0;	
  private JExpertFrame Parent_EF;
  private JRuleFrame Parent_RF;
  private JKBCT kbct;
  private JFIS fis;
  private JExtendedDataFile DataFile;
  private JScrollPane jPanelOutputs= new JScrollPane();
  private JScrollPane jMainPanelResults= new JScrollPane();
  private JPanel jPanelResults= new JPanel();
  private JButton jButtonResultFile = new JButton();
  private JTextField jTFResultFile = new JTextField();
  private String ResultFile= null;
  private JButton jButtonTestFile = new JButton();
  private JTextField jTFTestFile = new JTextField();
  private String TestFile= null;
  private JFileListener JFileListener;
  private JLabel jLabelBlankThres = new JLabel();
  private DoubleField jdfBlankThres = new DoubleField();
  private double BlankThres= MainKBCT.getLocale().DefaultBlankThres();
  private JLabel jLabelAmbThres = new JLabel();
  private DoubleField jdfAmbThres = new DoubleField();
  private double AmbThres= MainKBCT.getLocale().DefaultAmbThres();
  private JButton jbDefaultValues = new JButton();
  private JButton jbCompute = new JButton();
  private JSemaphore JPerformanceFrameOpen;
  private JMenuItem jMenuPrint= new JMenuItem();
  private JMenuItem jMenuExport= new JMenuItem();
  private JMenuItem jMenuHelp= new JMenuItem();
  private JMenuItem jMenuClose= new JMenuItem();
  private boolean FLAG= false;
  private JTable jTable1;
  private JTable jTable2;
  private JTable jTable3;
  private DecimalFormat df;
  private double indAccComparison;
  private double indAccCons;
  private double indAcc;
  private double indAccLRN;
  private double indAccTST;
  private double indSCE;
  //private double indSCELRN;
  //private double indSCETST;
  private boolean warningTut= false;
  private boolean[] covdata;
  private DoubleField jdfCoverage;
  private DoubleField jdfAvgCFD;
  private DoubleField jdfRMSE;
  private DoubleField jdfMSE;
  private DoubleField jdfMAE;
  private ImageIcon icon_kbct= LocaleKBCT.getIconGUAJE();
  private boolean fingramsFlag= false;
  private boolean vistable= false;
  
//------------------------------------------------------------------------------
  public void Translate() throws Throwable {
    this.setIconImage(icon_kbct.getImage());
    if (MainKBCT.getConfig().GetTutorialFlag())
	  this.setTitle(LocaleKBCT.GetString("PerformanceTitle")+": "+this.TestFile);
    else
  	  this.setTitle(LocaleKBCT.GetString("PerformanceTitle"));
    	
    this.jMenuPrint.setText(LocaleKBCT.GetString("Print"));
    this.jMenuExport.setText(LocaleKBCT.GetString("Export"));
    this.jMenuHelp.setText(LocaleKBCT.GetString("Help"));
    this.jMenuClose.setText(LocaleKBCT.GetString("Close"));
    this.jButtonResultFile.setText(LocaleKBCT.GetString("ResultFile") + " :");
    this.jButtonTestFile.setText(LocaleKBCT.GetString("TestFile") + " :");
    this.jLabelBlankThres.setText(LocaleKBCT.GetString("BlankThres") + ":");
    this.jLabelAmbThres.setText(LocaleKBCT.GetString("AmbThres") + ":");
    this.jbCompute.setText(LocaleKBCT.GetString("Compute"));
    this.jbDefaultValues.setText(LocaleKBCT.GetString("DefaultValues"));
    double valueBT = this.jdfBlankThres.getValue();
    this.jdfBlankThres.setLocale(LocaleKBCT.Locale());
    this.jdfBlankThres.setValue(valueBT);
    double valueAT = this.jdfAmbThres.getValue();
    this.jdfAmbThres.setLocale(LocaleKBCT.Locale());
    this.jdfAmbThres.setValue(valueAT);
    this.repaint();
  }
//------------------------------------------------------------------------------
  public JRulesBaseQualityFrame(JExpertFrame parent, JFIS fis, JExtendedDataFile data_file, JSemaphore open, boolean flag, boolean ff, boolean vistable ) {
    super(parent);
    this.Parent_EF= parent;
    this.kbct= this.Parent_EF.Temp_kbct;
    this.vistable= vistable;
    this.Init(fis, data_file, open, flag, ff);
  }
//------------------------------------------------------------------------------
  public JRulesBaseQualityFrame(JRuleFrame parent, JFIS fis, JExtendedDataFile data_file, JSemaphore open, boolean flag, boolean ff ) {
    super(parent);
    this.Parent_RF= parent;
    this.kbct= this.Parent_RF.kbctjrf;
    this.Init(fis, data_file, open, flag, ff);
  }
//------------------------------------------------------------------------------
  private void Init(JFIS fis, JExtendedDataFile data_file, JSemaphore open, boolean flag, boolean ff) {
	    this.fis = fis;
	    this.DataFile = data_file;
	    this.JPerformanceFrameOpen = open;
	    File RF= JKBCTFrame.BuildFile("result");
	    this.ResultFile=RF.getAbsolutePath();
	    this.BlankThres= MainKBCT.getConfig().GetBlankThres();
	    this.AmbThres= MainKBCT.getConfig().GetAmbThres();
	    this.FLAG= flag;
	    this.fingramsFlag= ff;
	    try {
	      jbInit();
	      this.JPerformanceFrameOpen.Acquire();
	    }
	    catch(Throwable t) {
	    	t.printStackTrace();
	        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in constructor of JRulesBaseQualityFrame: "+t);
	    }
  }
//------------------------------------------------------------------------------
  private void jbInit() throws Throwable {
    //System.out.println("JRBQF: jbInit 1");
	JMenuBar jmb= new JMenuBar();
    jmb.add(this.jMenuPrint);
    jmb.add(this.jMenuExport);
    jmb.add(this.jMenuHelp);
    jmb.add(this.jMenuClose);
    this.setJMenuBar(jmb);
    this.getContentPane().setLayout(new GridBagLayout());
    df= new DecimalFormat();
    df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
    dfs.setDecimalSeparator((new String(".").charAt(0)));
    df.setDecimalFormatSymbols(dfs);
    //System.out.println("JRBQF: jbInit 2");
    //System.out.println("JRBQF: this.FLAG="+this.FLAG);
    //System.out.println("JRBQF: this.fingramsFlag="+this.fingramsFlag);
    if ( (!this.FLAG) && (!this.fingramsFlag) ) {
      //System.out.println("JRBQF: jbInit 2.1");
      JPanel jPanelResultFile = new JPanel(new GridBagLayout());
      jPanelResultFile.add(jButtonResultFile, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      File RF= JKBCTFrame.BuildFile("result");
      this.ResultFile=RF.getAbsolutePath();
      jTFResultFile.setText(ResultFile);
      jPanelResultFile.add(jTFResultFile, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 100, 0));
      jPanelResultFile.add(jButtonTestFile, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      File DF= new File(this.DataFile.ActiveFileName());
      TestFile= DF.getAbsolutePath();
      jTFTestFile.setText(TestFile);
      jPanelResultFile.add(jTFTestFile, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 100, 0));
      this.getContentPane().add(jPanelResultFile, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
      JPanel jPanelParameters = new JPanel(new GridBagLayout());
      jPanelParameters.add(this.jLabelBlankThres, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      this.jdfBlankThres.setValue(MainKBCT.getConfig().GetBlankThres());
      jPanelParameters.add(this.jdfBlankThres, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 50, 0));
      jPanelParameters.add(this.jLabelAmbThres, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      this.jdfAmbThres.setValue(MainKBCT.getConfig().GetAmbThres());
      jPanelParameters.add(this.jdfAmbThres, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 50, 0));
      jPanelParameters.add(jbDefaultValues, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      jPanelParameters.add(this.jbCompute, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 50, 0));
      this.getContentPane().add(jPanelParameters, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
    } else {
         //System.out.println("JRBQF: jbInit 2.2");
         this.ComputeOutputs();
    }
    //System.out.println("JRBQF: jbInit 3");
    this.JFileListener = new JFileListener() {
      public void Closed() { JRulesBaseQualityFrame.this.dispose(); }
      public void ReLoaded() {}
    };
    this.DataFile.AddJFileListener(this.JFileListener);
    this.jButtonResultFile.addActionListener(new java.awt.event.ActionListener() { public void actionPerformed(ActionEvent e) { jButtonResultFile_actionPerformed(); } });
    this.jButtonTestFile.addActionListener(new java.awt.event.ActionListener() { public void actionPerformed(ActionEvent e) { jButtonTestFile_actionPerformed(); } });
    this.jbDefaultValues.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	JRulesBaseQualityFrame.this.jdfBlankThres.setValue(LocaleKBCT.DefaultBlankThres());    
        	JRulesBaseQualityFrame.this.jdfAmbThres.setValue(LocaleKBCT.DefaultAmbThres());    
        }
    } );
    this.jbCompute.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
    	  double BT= JRulesBaseQualityFrame.this.jdfBlankThres.getValue();
    	  double AT= JRulesBaseQualityFrame.this.jdfAmbThres.getValue();
          if ( (BT >= 0) && (BT <= 1) && (AT >= 0) && (AT <= 1) ) {
              JRulesBaseQualityFrame.this.ResultFile= JRulesBaseQualityFrame.this.jTFResultFile.getText();
              JRulesBaseQualityFrame.this.TestFile= JRulesBaseQualityFrame.this.jTFTestFile.getText();
              JRulesBaseQualityFrame.this.BlankThres= JRulesBaseQualityFrame.this.jdfBlankThres.getValue();
              MainKBCT.getConfig().SetBlankThres(JRulesBaseQualityFrame.this.BlankThres);
              JRulesBaseQualityFrame.this.AmbThres= JRulesBaseQualityFrame.this.jdfAmbThres.getValue();
              MainKBCT.getConfig().SetAmbThres(JRulesBaseQualityFrame.this.AmbThres);
              JRulesBaseQualityFrame.this.getContentPane().removeAll();
              JRulesBaseQualityFrame.this.getContentPane().setLayout(new GridBagLayout());
              try {
                 boolean warning= JRulesBaseQualityFrame.this.selectVariablesInTestDataFile();
                 //System.out.println("warning="+warning);
                 if (!warning) {
                     ComputeOutputs();
                     JRulesBaseQualityFrame.this.Translate();
                     JRulesBaseQualityFrame.this.pack();
                     JRulesBaseQualityFrame.this.setLocation(JRulesBaseQualityFrame.this.ChildPosition(JRulesBaseQualityFrame.this.getSize()));
                     JRulesBaseQualityFrame.this.setVisible(true);
                 } else
                     JRulesBaseQualityFrame.this.dispose();
              } catch (Throwable t) {
    	         // t.printStackTrace();
    	         MessageKBCT.Error(JRulesBaseQualityFrame.this, LocaleKBCT.GetString("Error"), "Error in jbCompute_actionPerformed: "+t);
    	         JRulesBaseQualityFrame.this.dispose();
              }
          } else {
  	         MessageKBCT.Error(JRulesBaseQualityFrame.this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("BTATmessage"));
          }
    }} );
    jMenuPrint.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JRulesBaseQualityFrame.this.jMenuPrint_actionPerformed(); } });
    jMenuExport.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JRulesBaseQualityFrame.this.jMenuExport_actionPerformed(); } });
    jMenuHelp.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JRulesBaseQualityFrame.this.jMenuHelp_actionPerformed(); } });
    jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JRulesBaseQualityFrame.this.dispose(); } });
    this.Translate();
    this.pack();
    if (!MainKBCT.getConfig().GetTutorialFlag())
        this.setLocation(this.ChildPosition(this.getSize()));
    else if (!this.warningTut)
        this.setLocation(0,0);
    	
    //if (LocaleKBCT.isWindowsPlatform())
      //  this.setResizable(false);
    //else
        this.setResizable(true);
    	
    if (this.fingramsFlag) {
    	//System.out.println("H1: setVisible(false)");
        this.setVisible(false);
    } else {    	
        if ( (MainKBCT.getConfig().GetTESTautomatic()) || ((MainKBCT.getConfig().GetTutorialFlag()) && (this.warningTut)) ) {
        	//System.out.println("H2: setVisible(false)");
            this.setVisible(false);
        } else
            this.setVisible(true);

        if ( (MainKBCT.getConfig().GetTutorialFlag()) && (this.warningTut) )
    	    this.dispose();
    }
  }
//------------------------------------------------------------------------------
  boolean selectVariablesInTestDataFile() throws Throwable {
    boolean warning= false;
	File f= new File(this.TestFile);
	if (f.exists()) {
      this.DataFile= new JExtendedDataFile(this.TestFile, true);
	  //System.out.println("JKBInterpretabilityFrame: selectVariablesInTestDataFile");
	  String selVars= MainKBCT.getJMF().getSelectedVariablesInDataFile();
	  selVars= selVars.replaceAll(", ",",");
	  //System.out.println(" -> SelVars= "+selVars);
	  String[] selV= selVars.split(",");
      int NbIn= fis.NbInputs();
      //System.out.println("NbIn="+NbIn);
      int NbOut= fis.NbOutputs();
      //System.out.println("NbOut="+NbOut);
      int DataVar= this.DataFile.VariableCount();
      //System.out.println("DataVar="+DataVar);
      Integer[] vars= new Integer[NbIn+NbOut];
      if (selV.length == NbIn+NbOut) {
          if (DataVar < NbIn + NbOut) {
        	  //System.out.println("w1");
        	  warning= true;
          }
          if (!warning) {
              for (int i=0; i<vars.length; i++) {
            	   vars[i]= new Integer(selV[i]) + 1;
            	   //System.out.println("var["+i+"]="+vars[i]);
              }
          }
    	  
      } else {
    	  // KB was simplified but it is not saved yet
       	  //System.out.println("Select 1");
          if (MainKBCT.getConfig().GetTESTautomatic()) {
              Object[] selectedVars= ((JExpertFrame)this.parent).Parent.variables.toArray();
              for (int i=0; i<vars.length; i++) {
                   vars[i]= (Integer)selectedVars[i]+1;
              	 //System.out.println("Select 1.1: selVar="+vars[i]);
              }            
            } else {
                for (int i=0; i<NbIn; i++) {
                  String msg= LocaleKBCT.GetString("Select_Data_link_to_Var")+" "+this.kbct.GetInput(i+1).GetName();
                  Integer opt= (Integer)MessageKBCT.SelectDataVar(this, msg, DataVar);
                  if (opt==null) {
                	// System.out.println("w2");
                    warning= true;
                    break;
                  } else
                      vars[i]= opt;
                }
                if (! warning) {
                  for (int i=0; i<NbOut; i++) {
                    String msg= LocaleKBCT.GetString("Select_Data_link_to_Var")+" "+this.kbct.GetOutput(i+1).GetName();
                    Integer opt= (Integer)MessageKBCT.SelectDataVar(this, msg, DataVar);
                    if (opt==null) {
                  	  //System.out.println("w3");
                      warning= true;
                      break;
                    } else
                    vars[i+NbIn]= opt;
                  }
                }
            }
      }
  	  //System.out.println("Select 2");
      if (!warning) {
        DecimalFormat df= new DecimalFormat();
        df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
        df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
        DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
        dfs.setDecimalSeparator((new String(".").charAt(0)));
        df.setDecimalFormatSymbols(dfs);
        df.setGroupingSize(20);
        PrintWriter fOut = new PrintWriter(new FileOutputStream(this.DataFile.FileName()+".jrbqtst"), true);
        int NbLines= this.DataFile.VariableData(0).length;
        int NbCol= NbIn+NbOut;
        for (int k = 0; k < NbLines; k++) {
          for (int n = 0; n < NbCol; n++) {
            int d= vars[n].intValue()-1;
            fOut.print(df.format(this.DataFile.VariableData(d)[k]));
            if (n==NbCol-1)
              fOut.println();
            else
              fOut.print(",");
          }
        }
        fOut.flush();
        fOut.close();
        this.DataFile= new JExtendedDataFile(this.DataFile.FileName()+".jrbqtst", true);
      }
    } else {
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("DataFileNoExist"));
        this.dispose();
    }
	//System.out.println("Select 3");
    return warning;
  }
/*  boolean selectVariablesInTestDataFile() throws Throwable {
    boolean warning= false;
    File f= new File(this.TestFile);
    if (f.exists()) {
        this.DataFile= new JExtendedDataFile(this.TestFile, true);
        int NbIn= fis.NbInputs();
        int NbOut= fis.NbOutputs();
        int DataVar= this.DataFile.VariableCount();
        Integer[] vars= new Integer[NbIn+NbOut];
        if (NbIn+NbOut != DataVar) {
        	//System.out.println("Select 1");
          if (MainKBCT.getConfig().GetTESTautomatic()) {
            Object[] selectedVars= this.Parent_EF.Parent.variables.toArray();
            for (int i=0; i<vars.length; i++) {
                vars[i]= (Integer)selectedVars[i]+1;
            	//System.out.println("Select 1.1: selVar="+vars[i]);
            }            
          } else {
              for (int i=0; i<NbIn; i++) {
                String msg= LocaleKBCT.GetString("Select_Data_link_to_Var")+" "+fis.GetInput(i).GetName();
                Integer opt= (Integer)MessageKBCT.SelectDataVar(this, msg, DataVar);
                if (opt==null) {
                  warning= true;
                  break;
                } else
                    vars[i]= opt;
              }
              if (! warning) {
                for (int i=0; i<NbOut; i++) {
                  String msg= LocaleKBCT.GetString("Select_Data_link_to_Var")+" "+fis.GetOutput(i).GetName();
                  Integer opt= (Integer)MessageKBCT.SelectDataVar(this, msg, DataVar);
                  if (opt==null) {
                    warning= true;
                    break;
                  } else
                  vars[i+NbIn]= opt;
                }
              }
          }
      	  //System.out.println("Select 2");
          if (!warning) {
            DecimalFormat df= new DecimalFormat();
            df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
            df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
            DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
            dfs.setDecimalSeparator((new String(".").charAt(0)));
            df.setDecimalFormatSymbols(dfs);
            df.setGroupingSize(20);
            PrintWriter fOut = new PrintWriter(new FileOutputStream(this.TestFile+".jrbqtst"), true);
            int NbLines= this.DataFile.VariableData(0).length;
            int NbCol= NbIn+NbOut;
            for (int k = 0; k < NbLines; k++) {
              for (int n = 0; n < NbCol; n++) {
                int d= vars[n].intValue()-1;
                fOut.print(df.format(this.DataFile.VariableData(d)[k]));
                if (n==NbCol-1)
                  fOut.println();
                else
                  fOut.print(",");
              }
            }
            fOut.flush();
            fOut.close();
            this.DataFile= new JExtendedDataFile(this.TestFile+".jrbqtst", true);
          }
        }
    } else {
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("DataFileNoExist"));
        this.dispose();
    }
	//System.out.println("Select 3");
    return warning;
  }*/
//------------------------------------------------------------------------------
  public void dispose() {
    super.dispose();
    this.DataFile.RemoveJFileListener(this.JFileListener);
    this.JPerformanceFrameOpen.Release();
  }
//------------------------------------------------------------------------------
  void jButtonResultFile_actionPerformed() {
    JOpenFileChooser file_chooser = new JOpenFileChooser(MainKBCT.getConfig().GetKBCTFilePath());
    if( file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ) {
      String file_name = file_chooser.getSelectedFile().getAbsolutePath();
      File f = new File( file_name );
      if( f.exists() ) {
        String message = LocaleKBCT.GetString("TheResultFile") + " : " + file_name + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");
        if( MessageKBCT.Confirm(this, message, 0, false, false, false) == JOptionPane.NO_OPTION )
          return;
      }
      this.ResultFile = file_name;
      this.jTFResultFile.setText(this.ResultFile);
    }
  }
//------------------------------------------------------------------------------
  void jButtonTestFile_actionPerformed() {
    JOpenFileChooser file_chooser = new JOpenFileChooser(MainKBCT.getConfig().GetKBCTFilePath());
    if( file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ) {
      String file_name = file_chooser.getSelectedFile().getAbsolutePath();
      File f = new File( file_name );
      if( f.exists() ) {
        this.TestFile = file_name;
        this.jTFTestFile.setText(this.TestFile);
      }
    }
  }
//------------------------------------------------------------------------------
  private void ComputeOutputs() {
    try {
    	//System.out.println("CO1");
      if (!MainKBCT.getConfig().GetTESTautomatic()) {
          String QualityLogFile= (JKBCTFrame.BuildFile("LogQuality.txt")).getAbsolutePath();
          MessageKBCT.BuildLogFile(QualityLogFile, JRulesBaseQualityFrame.this.Parent_EF.Parent.IKBFile, JKBCTFrame.KBExpertFile, JRulesBaseQualityFrame.this.Parent_EF.Parent.KBDataFile, "Quality");
      }
  	  //System.out.println("CO2");
      for(int i=0; i<fis.NbOutputs(); i++) {
    	  if ( (!MainKBCT.getConfig().GetTutorialFlag()) || (MainKBCT.getConfig().GetTutorialFlag() && fingramsFlag) ) {
    		  //System.out.println("JRBQF 1");
              this.Compute(i);
    	  }
    	  
		 //System.out.println("JRBQF 2: "+fingramsFlag);
         this.indAccLRN= this.getAccIndex();
         if ( ( (MainKBCT.getConfig().GetTESTautomatic()) || (MainKBCT.getConfig().GetTutorialFlag()) ) && ( (!fingramsFlag) || this.vistable) ) {
   		     //System.out.println("JRBQF 3");
             String aux= this.Parent_EF.Parent.OrigKBDataFile;
             if (aux.contains("train")) {
            	 this.TestFile= aux.replace("train","test");
             } else if (aux.contains("lrn.sample")) {
            	 this.TestFile= aux.replace("lrn.sample","tst.sample");
             } else if (aux.contains("tra.txt")) { 
            	 this.TestFile= aux.replace("tra.txt","tst.txt");
             } else if (aux.contains("tra.dat")) { 
            	 this.TestFile= aux.replace("tra.dat","tst.dat");
             } else {
                 String ErrorMsg= LocaleKBCT.GetString("IncorrectTESTfileName1") + "\n" + LocaleKBCT.GetString("IncorrectTESTfileName2");
            	 MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), ErrorMsg);
            	 this.warningTut= true;
            	 break;
             }
            
             // Benchmark datasets
             this.DataFile= new JExtendedDataFile(JRulesBaseQualityFrame.this.TestFile, true);
             this.selectVariablesInTestDataFile();
             this.Compute(i);
             this.indAccTST= this.getAccIndex();
         }
      }
      if (!MainKBCT.getConfig().GetTESTautomatic()) {
          MessageKBCT.CloseLogFile("Quality");
      }
      this.jPanelOutputs.getViewport().add(this.jPanelResults);
      this.getContentPane().add(jPanelOutputs, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
         ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
    } catch( Throwable t ) {
        t.printStackTrace();
    	MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JRulesBaseQualityFrame in ComputeOutputs: "+t);
        this.dispose();
    }
  }
//------------------------------------------------------------------------------
  private void Compute( int output_number ) {
  	//System.out.println("OUTPUT TYPE: "+this.kbct.GetOutput(output_number+1).GetType());
    if (this.kbct.GetOutput(output_number+1).GetType().equals("numerical"))
    	this.ComputeRegression(output_number);
    else
    	this.ComputeClassification(output_number);
  }
//------------------------------------------------------------------------------
  private void ComputeClassification( int output_number ) {
    try {
      MessageKBCT.WriteLogFile((LocaleKBCT.GetString("Accuracy")).toUpperCase(), "Quality");
      Date dd= new Date(System.currentTimeMillis());
      MessageKBCT.WriteLogFile("----------------------------------", "Quality");
      MessageKBCT.WriteLogFile("time begin -> "+DateFormat.getTimeInstance().format(dd), "Quality");
      //String logBaggingFile;
      String tfile="";
      if (this.TestFile==null) {
          File DF= new File(this.DataFile.ActiveFileName());
          tfile= DF.getAbsolutePath();
      } else {
    	  tfile= this.TestFile;
      }
      //System.out.println("tfile="+tfile);
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("TestFile")+"= "+tfile, "Quality");
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("BlankThres")+" ="+MainKBCT.getConfig().GetBlankThres(), "Quality");
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("AmbThres")+" ="+MainKBCT.getConfig().GetAmbThres(), "Quality");
      fis.JOutput output = this.fis.GetOutput(output_number);
      double[] result;
      if (!FLAG) {
          result= this.fis.Infer(output_number, this.DataFile.FileName(), this.jTFResultFile.getText(), this.jdfBlankThres.getValue(), false );
      } else {
          result= this.fis.Infer(output_number, this.DataFile.FileName(), this.ResultFile, this.BlankThres, false );
      }
      JPanel jPanelResult= new JPanel(new GridBagLayout());
      this.jdfCoverage= new DoubleField();
      DoubleField jdfAccuracy= new DoubleField();
      DoubleField jdfAccuracyCons= new DoubleField();
      DoubleField jdfAccuracyComparison= new DoubleField();
      DoubleField jdfSCE= new DoubleField();
      // Confidence Firing Degree (Difference between the two main classes)
      this.jdfAvgCFD= new DoubleField();
      DoubleField jdfMinCFD= new DoubleField();
      DoubleField jdfMaxCFD= new DoubleField();
      DoubleField jdfMaxError= new DoubleField();
      JLabel jLabelCoverage= new JLabel();
      JLabel jLabelAccuracy= new JLabel();
      JLabel jLabelAccuracyCons= new JLabel();
      JLabel jLabelAccuracyComparison= new JLabel();
      JLabel jLabelAvgCFD= new JLabel();
      JLabel jLabelMinCFD= new JLabel();
      JLabel jLabelMaxCFD= new JLabel();
      JLabel jLabelMaxError= new JLabel();
      JLabel jLabelSCE= new JLabel();
      TitledBorder titledBorderResult = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
      jPanelResult.setBorder(titledBorderResult);
      jPanelResult.add(jLabelCoverage, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
      //jdfCoverage.setEnabled(false);
      jdfCoverage.setBackground(Color.WHITE);
      jdfCoverage.setForeground(Color.BLUE);
      jdfCoverage.setEditable(false);
      jPanelResult.add(jdfCoverage, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));
      jPanelResult.add(jLabelAccuracy, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
      //jdfAccuracy.setEnabled(false);
      jdfAccuracy.setBackground(Color.WHITE);
      jdfAccuracy.setForeground(Color.BLUE);
      jdfAccuracy.setEditable(false);
      jPanelResult.add(jdfAccuracy, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));
      jPanelResult.add(jLabelAccuracyCons, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
      //jdfAccuracyCons.setEnabled(false);
      jdfAccuracyCons.setBackground(Color.WHITE);
      jdfAccuracyCons.setForeground(Color.BLUE);
      jdfAccuracyCons.setEditable(false);
      jPanelResult.add(jdfAccuracyCons, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));
      jPanelResult.add(jLabelAccuracyComparison, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
      //jdfAccuracyComparison.setEnabled(false);
      jdfAccuracyComparison.setBackground(Color.WHITE);
      jdfAccuracyComparison.setForeground(Color.BLUE);
      jdfAccuracyComparison.setEditable(false);
      jPanelResult.add(jdfAccuracyComparison, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));
      jPanelResult.add(jLabelAvgCFD, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
      //jdfAvgCFD.setEnabled(false);
      jdfAvgCFD.setBackground(Color.WHITE);
      jdfAvgCFD.setForeground(Color.BLUE);
      jdfAvgCFD.setEditable(false);
      jPanelResult.add(jdfAvgCFD, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));
      jPanelResult.add(jLabelMinCFD, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
      //jdfMinCFD.setEnabled(false);
      jdfMinCFD.setBackground(Color.WHITE);
      jdfMinCFD.setForeground(Color.BLUE);
      jdfMinCFD.setEditable(false);
      jPanelResult.add(jdfMinCFD, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));
      jPanelResult.add(jLabelMaxCFD, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
      //jdfMaxCFD.setEnabled(false);
      jdfMaxCFD.setBackground(Color.WHITE);
      jdfMaxCFD.setForeground(Color.BLUE);
      jdfMaxCFD.setEditable(false);
      jPanelResult.add(jdfMaxCFD, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));
      jPanelResult.add(jLabelMaxError, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
      //jdfMaxError.setEnabled(false);
      jdfMaxError.setBackground(Color.WHITE);
      jdfMaxError.setForeground(Color.BLUE);
      jdfMaxError.setEditable(false);
      jPanelResult.add(jdfMaxError, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));
      jPanelResult.add(jLabelSCE, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
      //jdfSCE.setEnabled(false);
      jdfSCE.setBackground(Color.WHITE);
      jdfSCE.setForeground(Color.BLUE);
      jdfSCE.setEditable(false);
      jPanelResult.add(jdfSCE, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));

	  DecimalFormat df= new DecimalFormat();
	  //df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals()+2);//5
	  //df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals()+2);//5
	  df.setMaximumFractionDigits(3);
	  df.setMinimumFractionDigits(3);
	  DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
	  dfs.setDecimalSeparator((new String(".").charAt(0)));
	  df.setDecimalFormatSymbols(dfs);
	  df.setGroupingSize(20);
      //jdfCoverage.setValue(result[1]*100);
      jLabelCoverage.setText(LocaleKBCT.GetString("Coverage") + " (%) :");
      jLabelAccuracy.setText(LocaleKBCT.GetString("Accuracy") + " :");
      jLabelAccuracyCons.setText(LocaleKBCT.GetString("Accuracy") + " (CONS) :");
      jLabelAccuracyComparison.setText(LocaleKBCT.GetString("Accuracy") + " (BT=0) :");
      jLabelAvgCFD.setText(LocaleKBCT.GetString("AvgCFD") + " :");
      jLabelMinCFD.setText(LocaleKBCT.GetString("MinCFD") + " :");
      jLabelMaxCFD.setText(LocaleKBCT.GetString("MaxCFD") + " :");
      jLabelMaxError.setText(LocaleKBCT.GetString("MaxError") + " :");
      jLabelSCE.setText(LocaleKBCT.GetString("SCE")+" :");
      titledBorderResult.setTitle(output.GetName());
      // VariableType= logical -> Nature = 1
      // VariableType= categorical -> Nature = 1
      // VariableType= numerical -> Nature = 0
      JKBCTOutput jout= this.kbct.GetOutput(output_number+1);
      int NbLabels= jout.GetLabelsNumber();
      String[] LabelsNames= jout.GetLabelsName();
      //System.out.println("NbLabs="+NbLabels);
      double[] NbOutputLabelTEC= new double[NbLabels]; // total error cases
      double[] NbOutputLabelTAC= new double[NbLabels]; // total ambiguity cases
      double[] NbOutputLabelTUC= new double[NbLabels]; // total unclassified cases
      int[] NbOutputLabels= new int[NbLabels];
      Vector ErrorCases= new Vector();
      Vector NoClassifCases= new Vector();
      Vector NoClassifErrorCases= new Vector();
      Vector AmbiguityCases= new Vector();
      Vector AmbiguityErrorCases= new Vector();
      PerformanceFile PerfFile= new PerformanceFile(this.ResultFile);
      double[][] data= PerfFile.GetData();
      int NbData= PerfFile.DataLength();
      double[] observed= new double[NbData];
      double[] infered= new double[NbData];
      double[] output_agg= new double[NbData];
      double[] warning= new double[NbData];
      boolean[] warningtie= new boolean[NbData];
      double[] error= new double[NbData];
      double[] diffCFD= new double[NbData];
      double Precision=0;
      double Recall=0;
      double Fmeasure=0;
      // rows: observed
      // columns: infered
      int[][] ErrorConfusion= new int[NbLabels][NbLabels];
      int[][] AmbiguityConfusion= new int[NbLabels][NbLabels];
      Vector[][] EC= new Vector[NbLabels][NbLabels];
      Vector[][] AC= new Vector[NbLabels][NbLabels];
      int NbTEC=0;
      int NbTAC=0;
      int NbACerror=0;
      int NbTUC=0;
      int NbUCerror=0;
      String EClist= "";
      String UClist= "";
      String UCElist= "";
      String AClist= "";
      String ACElist= "";
      if (!jout.GetType().equals("numerical")) {
          double limBT= this.BlankThres;
          double limAT= this.AmbThres;
          if (!FLAG) {
              limBT= this.jdfBlankThres.getValue();
              limAT= this.jdfAmbThres.getValue();
          }
          int NbLabOut= jout.GetLabelsNumber();
          //System.out.println("NbLabOut="+NbLabOut);
          int[] labelsUsed= new int[NbLabOut];
          for (int n=0; n<NbLabOut; n++)
        	   labelsUsed[n]=0;

          boolean warnCheck= false;
          if ( (jout.GetScaleName().equals("user")) && 
               ( (jout.GetInputInterestRange()[0]!=1) ||
           	   (jout.GetInputInterestRange()[1]!=jout.GetLabelsNumber()) ) &&
           	   (jout.isOutput()) &&
           	   ( (jout.GetType().equals("logical")) || (jout.GetType().equals("categorical")) ) ) {
                   warnCheck= true;
          } 
          jnifis.setUserHomeFisproPath(MainKBCT.getConfig().GetKbctPath());
          int nbRules= this.fis.NbRules();
          for (int n = 0; n < nbRules; n++) {
        	   JRule jr= this.fis.GetRule(n);
        	   double[] act= jr.Actions();
        	   if (warnCheck) {
        		   String[] labNames= jout.GetUserLabelsName();
                   //System.out.println("act[output_number]="+act[output_number]);
   			       for (int k=0; k<labNames.length; k++) {
        			    if (labNames[k].equals(""+act[output_number])) {
       	        	        labelsUsed[k]=1;
        			    }
        		   }
        	   } else {
        	       labelsUsed[(int)act[output_number]-1]=1;
        	   }
          }  
          int rem[]= new int[NbLabOut];
          int cont=1;
          for (int n=0; n<NbLabOut; n++) {
               //System.out.println("labelsUsed["+n+"]="+labelsUsed[n]);
        	   if (labelsUsed[n]==0)
        		   cont++;

        	   rem[n]=cont;
          }
        double totsce=0;
        for (int n = 0; n < NbData; n++) {
   	      warningtie[n]= false;
          observed[n] = (double)Math.round(1000*data[n][0])/1000;
          infered[n] = (double)Math.round(1000*data[n][1])/1000;
          warning[n] = data[n][2];
          //System.out.println("n="+n+"  obs="+observed[n]+"  inf="+infered[n]+"  warn="+warning[n]);          
          this.fis.Infer(this.DataFile.GetData()[n]);
          double output_infer= (double)Math.round(1000*this.fis.SortiesObtenues()[output_number])/1000;
          //System.out.println("out_infer="+output_infer);          
          if (warnCheck) {
   		      String[] labNames= jout.GetUserLabelsName();
              boolean wo= false;
              boolean wi= false;
   		      for (int k=0; k<labNames.length; k++) {
		        //System.out.println("  labNames["+k+"]="+labNames[k]);          
			    if ( (!wi) && (labNames[k].equals(""+output_infer)) ) {
	        	    output_infer= k+1;
			        //System.out.println("  -> "+String.valueOf(k+1));  
	        	    infered[n]= k+1;
	        	    wi= true;
			    }
			    if ( (!wo) && (labNames[k].equals(""+observed[n])) ) {
	        	    observed[n]= k+1;
	        	    wo= true;
			    }
			    if (wi && wo)
			    	break;
		      }
   	          //System.out.println("--> n="+n+"  obs="+observed[n]+"  inf="+infered[n]);          
          } 
          //System.out.println("out_infer_mod="+output_infer);          
          double[][] d= this.fis.AgregationResult(output_number);
          if (output_infer > 0) {
        	  int ind=(int)output_infer - rem[(int)output_infer-1];
              double auxfd= (double)Math.round(1000*d[1][ind])/1000;
              totsce= totsce + Math.pow((1-auxfd), 2);
              //System.out.println("d.length="+d.length);
              //System.out.println("d[0].length="+d[0].length);
              for (int k=0; k<d[0].length; k++) {
                   //System.out.println("d[0]["+k+"]="+d[0][k]);
                   //System.out.println("d[1]["+k+"]="+d[1][k]);
                   //System.out.println("auxfd -> "+auxfd+"  d[1]["+k+"]="+d[1][k]);
                  if (limBT > limAT) {
                	  if ( (warning[n]==2) && (Math.abs((double)Math.round(1000*d[1][k])/1000-auxfd) > limAT) )
                		  warning[n]= -1;
                  }
            	  if ( (k!=ind) && (Math.abs((double)Math.round(1000*d[1][k])/1000-auxfd) <= limAT) ) {
      	    	         if (warning[n]!=2) 
    	    	    	     warning[n]=2;

      	    	         if (d[1][k]==auxfd) {
        	    	         warningtie[n]= true;
        	    	     //System.out.println("k="+k+"  ind="+ind+"  d[1][k]="+d[1][k]);
        	    	     //System.out.println("warningtie[n="+String.valueOf(n+1)+"]");
        	                 break;
      	    	         }
        	       }
            	   if (k!=ind)
                       totsce= totsce + Math.pow((double)Math.round(1000*d[1][k])/1000, 2);
              }
              output_agg[n]= auxfd;
              
          } else {
              output_agg[n]= 0;
              totsce= totsce + 1;
          }
          diffCFD[n]=this.computeDiffCFD(d);
        	  
          if ( (observed[n] != infered[n]) || (output_agg[n] <= limBT) )
            error[n] = 1;
          else {
            error[n] = 0;
          }
        }
        this.indSCE= (totsce)/(NbData*NbLabOut);
        double avgcf=diffCFD[0];
        double mincf=diffCFD[0];
        double maxcf=diffCFD[0];
        for (int n=1; n<diffCFD.length; n++) {
        	 //System.out.println("diffCFD[n] -> "+diffCFD[n]);
        	 avgcf= avgcf + diffCFD[n];
        	 if (diffCFD[n] < mincf)
        		 mincf= diffCFD[n];
        	 if (diffCFD[n] > maxcf)
        		 maxcf= diffCFD[n];
        }
        avgcf= avgcf/diffCFD.length;
        jdfAvgCFD.setValue(avgcf);
        jdfMinCFD.setValue(mincf);
        jdfMaxCFD.setValue(maxcf);
        double[] NbOutputLabelTTP= new double[NbLabels]; // total true positives
        double[] NbOutputLabelTFP= new double[NbLabels]; // total false positives
        double[] NbOutputLabelTTN= new double[NbLabels]; // total true negatives
        double[] NbOutputLabelTFN= new double[NbLabels]; // total false negatives
        double[] NbOutputLabelACerr= new double[NbLabels];
        double[] NbOutputLabelUCerr= new double[NbLabels];
        this.covdata= new boolean[NbData];
        for (int n = 0; n < NbData; n++) {
          this.covdata[n]= true;
          for (int m = 0; m < NbLabels; m++) {
            if (observed[n] == m + 1) {
              NbOutputLabels[m]++;
              if (error[n] != 0) {
            	  if ( (infered[n]<0) || (output_agg[n]<=limBT) ) {
                      NbOutputLabelTUC[m]++;
                      NoClassifCases.add(new Integer(n + 1));
                      this.covdata[n]= false;
                      if ( ( ((int)observed[n]-1)!=((int)infered[n]-1) ) || (warningtie[n]) ) {
                          NbOutputLabelUCerr[m]++;
                          NoClassifErrorCases.add(new Integer(n + 1));
                      }
            	  } else {
            		  if (warning[n] == 2) {
                          NbOutputLabelTAC[m]++;
                          AmbiguityCases.add(new Integer(n + 1));
                          if ( (infered[n]>0) && (output_agg[n]>limBT) ) {
                            if ( ( ((int)observed[n]-1)!=((int)infered[n]-1) ) || (warningtie[n]) ) {
                          	    NbOutputLabelACerr[m]++;
                                AmbiguityErrorCases.add(new Integer(n + 1));
                            }
                            AmbiguityConfusion[(int)observed[n]-1][(int)infered[n]-1]++;
                            if (AC[(int)observed[n]-1][(int)infered[n]-1]==null)
                              AC[(int)observed[n]-1][(int)infered[n]-1]= new Vector();

                            AC[(int)observed[n]-1][(int)infered[n]-1].add(new Integer(n+1));
                          }
            		  } else {
                          if ( (infered[n]>0) && (output_agg[n]>limBT) ) {
                              NbOutputLabelTEC[m]++;
                              ErrorCases.add(new Integer(n + 1));
                              ErrorConfusion[(int)observed[n]-1][(int)infered[n]-1]++;
                              if (EC[(int)observed[n]-1][(int)infered[n]-1]==null)
                                EC[(int)observed[n]-1][(int)infered[n]-1]= new Vector();

                              EC[(int)observed[n]-1][(int)infered[n]-1].add(new Integer(n+1));
                           } 
            			  
            		  }
            	  }
              } else {
                  if (warningtie[n]) {
                	  NbOutputLabelACerr[m]++;
                      AmbiguityErrorCases.add(new Integer(n + 1));
                  }
            	  if (warning[n] == 2) {
                      NbOutputLabelTAC[m]++;
                      AmbiguityCases.add(new Integer(n + 1));
                      //System.out.println("infered[n] -> "+infered[n]);
                      //System.out.println("output_agg[n] -> "+output_agg[n]);
                      if ( (infered[n]>0) && (output_agg[n]>limBT) ) {
                          AmbiguityConfusion[(int)observed[n]-1][(int)infered[n]-1]++;
                          if (AC[(int)observed[n]-1][(int)infered[n]-1]==null)
                            AC[(int)observed[n]-1][(int)infered[n]-1]= new Vector();

                          AC[(int)observed[n]-1][(int)infered[n]-1].add(new Integer(n+1));
                        }
        		  } else {
            	      ErrorConfusion[(int)observed[n]-1][(int)infered[n]-1]++;
                      if (EC[(int)observed[n]-1][(int)infered[n]-1]==null)
                          EC[(int)observed[n]-1][(int)infered[n]-1]= new Vector();

                      EC[(int)observed[n]-1][(int)infered[n]-1].add(new Integer(n+1));
        		  }
              }
            }
          }
        }
        for (int m = 0; m < NbLabels; m++) {
            for (int n = 0; n < NbData; n++) {
          	    if (observed[n]==m+1) {
          	    	if ( (observed[n]==infered[n]) && (output_agg[n]>limBT) )
                		NbOutputLabelTTP[m]++;
          	    	else
          	    		NbOutputLabelTFN[m]++;
          	    } else {
          	    	if ( (infered[n]==m+1) && (output_agg[n]>limBT) )
                		NbOutputLabelTFP[m]++;
          	    	else
          	    		NbOutputLabelTTN[m]++;
          	    }
            }
        }
        //for (int n=0; n<NbLabels; n++) {
        //	 System.out.println("NbOutputLabelRealAmbiguity["+n+"]="+NbOutputLabelRealAmbiguity[n]);
        //}
        Vector Title1, Data1;
        Title1 = new Vector();
        Title1.add(LocaleKBCT.GetString("Label"));
        Title1.add(LocaleKBCT.GetString("Data"));
        Title1.add(LocaleKBCT.GetString("ErrorEC"));
        Title1.add(LocaleKBCT.GetString("AmbiguityTotal"));
        Title1.add(LocaleKBCT.GetString("AmbiguityReal"));
        Title1.add(LocaleKBCT.GetString("NoClassif"));
        Title1.add(LocaleKBCT.GetString("NoClassifErrorCases"));
        Title1.add("TP");
        Title1.add("FP");
        Title1.add("TN");
        Title1.add("FN");
        Title1.add(LocaleKBCT.GetString("Precision"));
        Title1.add(LocaleKBCT.GetString("Recall"));
        Title1.add(LocaleKBCT.GetString("Fmeasure"));
        Data1 = new Vector();
        Data1.add(Title1);
        double nec=0;
        double nact=0;
        double nacr=0;
        double nuc=0;
        double nucr=0;
        double ntp=0;
        double nfp=0;
        double ntn=0;
        double nfn=0;
        double npre=0;
        double nrec=0;
        double nfme=0;
        for (int n = 0; n < NbLabels+1; n++) {
          Vector v = new Vector();
          if (n==NbLabels) {
              v.add(LocaleKBCT.GetString("total").toUpperCase());
              v.add(new Integer(NbData));
              v.add(new Integer((int)nec));
              v.add(new Integer((int)nact));
              v.add(new Integer((int)nacr));
              v.add(new Integer((int)nuc));
              v.add(new Integer((int)nucr));
              // True Positives (TP)
              v.add(new Integer((int)ntp));
              // False Positives (FP)
              v.add(new Integer((int)nfp));
              // True Negatives (TN)
              v.add(new Integer((int)ntn));
              // False Negatives (FN)
              v.add(new Integer((int)nfn));
              // Global Precision
              Precision= npre;
              v.add(new Double(df.format(Precision)));
              // Global Recall
              Recall= nrec;
              v.add(new Double(df.format(Recall)));
              // Global F-measure
              Fmeasure= nfme;
              v.add(new Double(df.format(Fmeasure)));
          } else {
              v.add(LabelsNames[n]);
              v.add(new Integer(NbOutputLabels[n]));
              v.add(new Integer( (int) NbOutputLabelTEC[n]));
              nec= nec+(int)NbOutputLabelTEC[n];
              v.add(new Integer( (int) NbOutputLabelTAC[n]));
              nact= nact+(int)NbOutputLabelTAC[n];
              v.add(new Integer( (int) NbOutputLabelACerr[n]));
              nacr= nacr+(int)NbOutputLabelACerr[n];
              v.add(new Integer( (int) NbOutputLabelTUC[n]));
              nuc= nuc+(int)NbOutputLabelTUC[n];
              v.add(new Integer( (int) NbOutputLabelUCerr[n]));
              nucr= nucr+(int)NbOutputLabelUCerr[n];
              // True Positives (TP)
              v.add(new Integer( (int) NbOutputLabelTTP[n]));
              ntp= ntp+(int)NbOutputLabelTTP[n];
              // False Positives (FP)
              v.add(new Integer( (int) NbOutputLabelTFP[n]));
              nfp= nfp+(int)NbOutputLabelTFP[n];
              // True Negatives (TN)
              v.add(new Integer( (int) NbOutputLabelTTN[n]));
              ntn= ntn+(int)NbOutputLabelTTN[n];
              // False Negatives (FN)
              v.add(new Integer( (int) NbOutputLabelTFN[n]));
              nfn= nfn+(int)NbOutputLabelTFN[n];
              // Precision
              //System.out.println("NbOutputLabelTTP["+n+"]="+NbOutputLabelTTP[n]);
              //System.out.println("NbOutputLabelTFP["+n+"]="+NbOutputLabelTFP[n]);
              //System.out.println("NbOutputLabelTTN["+n+"]="+NbOutputLabelTTN[n]);
              //System.out.println("NbOutputLabelTFN["+n+"]="+NbOutputLabelTFN[n]);
              boolean warnPr= false;
              if (NbOutputLabelTTP[n]+NbOutputLabelTFP[n]==0) {
                  v.add("0.0");
                  warnPr= true;
              } else {
                  Precision=(NbOutputLabelTTP[n])/(NbOutputLabelTTP[n]+NbOutputLabelTFP[n]);
                  v.add(new Double(df.format(Precision)));
              }
              npre= npre+((double)NbOutputLabels[n]/NbData)*Precision;  
              // Recall
              boolean warnRe= false;
              if (NbOutputLabelTTP[n]+NbOutputLabelTFN[n]==0) {
                  v.add("0.0");
                  warnRe= true;
              } else {
                  Recall=(NbOutputLabelTTP[n])/(NbOutputLabelTTP[n]+NbOutputLabelTFN[n]);
                  v.add(new Double(df.format(Recall)));
              }
              nrec= nrec+((double)NbOutputLabels[n]/NbData)*Recall;
              // Fmeasure
              if ( (Precision+Recall==0) || (warnPr) || (warnRe) )
                  v.add("0.0");
              else {
                  Fmeasure=(2*Precision*Recall)/(Precision+Recall);
                  v.add(new Double(df.format(Fmeasure)));
              }
              nfme= nfme+((double)NbOutputLabels[n]/NbData)*Fmeasure;  
          }
          Data1.add(v);
        }
        jdfCoverage.setValue(100-nuc*100/NbData);
        this.jTable1 = new JTable();
        DefaultTableModel RuleModel1 = new DefaultTableModel() {
       	  static final long serialVersionUID=0;	
          public Class getColumnClass(int c) {return getValueAt(0, c).getClass();}
          public boolean isCellEditable(int row, int col) { return false; }
        };
        RuleModel1.setDataVector(Data1, Title1);
        this.jTable1.setModel(RuleModel1);
        Vector Title2, Data2;
        Title2 = new Vector();
        Title2.add(LocaleKBCT.GetString("observed")+" / "+LocaleKBCT.GetString("infered"));
        for (int n = 0; n < NbLabels; n++)
          Title2.add(LabelsNames[n]);

        Data2= new Vector();
        Data2.add(Title2);
        for (int n=0; n<NbLabels; n++) {
          Vector v= new Vector();
          v.add(LabelsNames[n]);
          for (int m=0; m<NbLabels; m++)
            v.add(new Integer(ErrorConfusion[n][m]));

          Data2.add(v);
        }
        this.jTable2= new JTable();
        DefaultTableModel RuleModel2 = new DefaultTableModel() {
       	  static final long serialVersionUID=0;	
          public Class getColumnClass(int c) { return getValueAt(0, c).getClass(); }
          public boolean isCellEditable(int row, int col) { return false; }
        };
        RuleModel2.setDataVector(Data2, Title2);
        this.jTable2.setModel(RuleModel2);
        Vector Title3, Data3;
        Title3 = new Vector();
        Title3.add(LocaleKBCT.GetString("observed")+" / "+LocaleKBCT.GetString("infered"));
        for (int n = 0; n < NbLabels; n++)
          Title3.add(LabelsNames[n]);

        Data3= new Vector();
        Data3.add(Title3);
        for (int n=0; n<NbLabels; n++) {
          Vector v= new Vector();
          v.add(LabelsNames[n]);
          for (int m=0; m<NbLabels; m++)
            v.add(new Integer(AmbiguityConfusion[n][m]));

          Data3.add(v);
        }
        this.jTable3= new JTable();
        DefaultTableModel RuleModel3 = new DefaultTableModel() {
       	  static final long serialVersionUID=0;	
          public Class getColumnClass(int c) { return getValueAt(0, c).getClass(); }
          public boolean isCellEditable(int row, int col) { return false; }
        };
        RuleModel3.setDataVector(Data3, Title3);
        this.jTable3.setModel(RuleModel3);
        //System.out.println("LNlength="+LabelsNames.length);
        this.SetUpTable(LabelsNames, EC, AC);
        this.InitColumnSizes(LabelsNames, NbLabels);
        int aux=0;
        jPanelResult.add( jTable1, new GridBagConstraints(0, 10+aux, 0, 1, 1.0, 1.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
        aux++;
        if (!NoClassifCases.isEmpty()) {
          Object[] ncca= NoClassifCases.toArray();
          NbTUC=ncca.length;
          for (int m=0; m<NbTUC; m++) {
        	  UClist= UClist + ncca[m].toString();
        	  if (m!=NbTUC-1)
        		  UClist= UClist + ", ";
          }
          int NbLines= ncca.length/25 + 1;
          String[] NoClassifLabels= new String[NbLines];
          for (int k=0; k<NbLines; k++) {
            if (25*k==ncca.length)
              break;

            String lab;
            if (25*k+1<ncca.length)
              lab= ncca[25*k]+", ";
            else
              lab= ncca[25*k].toString();

            for (int m=1; m<25; m++)
              if (25*k+m >= ncca.length)
                break;
              else {
                lab= lab+ncca[25*k+m];
                if (25*k+m!=ncca.length-1)
                  lab= lab+", ";
              }
            NoClassifLabels[k]= lab;
          }
          for (int k=0; k<NbLines; k++) {
            JLabel jLabelNoClassifCases= new JLabel();
            jLabelNoClassifCases.setToolTipText(LocaleKBCT.GetString("NoClassifCasesToolTipText"));
            if (k==0) {
              jLabelNoClassifCases.setText(LocaleKBCT.GetString("NoClassifCases") + ": "+NoClassifLabels[k]);
              jPanelResult.add(jLabelNoClassifCases, new GridBagConstraints(0, 10+aux, 2, 1, 0.0, 0.0
                    ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 2, 2), 0, 0));
            } else {
              jLabelNoClassifCases.setText(NoClassifLabels[k]);
              jPanelResult.add(jLabelNoClassifCases, new GridBagConstraints(0, 10+aux, 2, 1, 0.0, 0.0
                    ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, LocaleKBCT.GetString("NoClassifCases").length()*8, 2, 2), 0, 0));
            }
            aux++;
          }
        }
        if (!NoClassifErrorCases.isEmpty()) {
            Object[] nceca= NoClassifErrorCases.toArray();
            NbUCerror=nceca.length;
            for (int m=0; m<NbUCerror; m++) {
          	  UCElist= UCElist + nceca[m].toString();
          	  if (m!=NbUCerror-1)
          		  UCElist= UCElist + ", ";
            }
            int NbLines= nceca.length/25 + 1;
            String[] NoClassifErrorLabels= new String[NbLines];
            for (int k=0; k<NbLines; k++) {
              if (25*k==nceca.length)
                break;

              String lab;
              if (25*k+1<nceca.length)
                lab= nceca[25*k]+", ";
              else
                lab= nceca[25*k].toString();

              for (int m=1; m<25; m++)
                if (25*k+m >= nceca.length)
                  break;
                else {
                  lab= lab+nceca[25*k+m];
                  if (25*k+m!=nceca.length-1)
                    lab= lab+", ";
                }
              NoClassifErrorLabels[k]= lab;
            }
            for (int k=0; k<NbLines; k++) {
              JLabel jLabelNoClassifErrorCases= new JLabel();
              jLabelNoClassifErrorCases.setToolTipText(LocaleKBCT.GetString("NoClassifErrorCasesToolTipText"));
              if (k==0) {
                jLabelNoClassifErrorCases.setText(LocaleKBCT.GetString("NoClassifErrorCases") + ": "+NoClassifErrorLabels[k]);
                jPanelResult.add(jLabelNoClassifErrorCases, new GridBagConstraints(0, 10+aux, 2, 1, 0.0, 0.0
                      ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 2, 2), 0, 0));
              } else {
                jLabelNoClassifErrorCases.setText(NoClassifErrorLabels[k]);
                jPanelResult.add(jLabelNoClassifErrorCases, new GridBagConstraints(0, 10+aux, 2, 1, 0.0, 0.0
                      ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, LocaleKBCT.GetString("NoClassifCases").length()*8, 2, 2), 0, 0));
              }
              aux++;
            }
          }
        if (!ErrorCases.isEmpty()) {
          Object[] eca= ErrorCases.toArray();
          NbTEC=eca.length;
          for (int m=0; m<NbTEC; m++) {
        	  EClist= EClist + eca[m].toString();
        	  if (m!=NbTEC-1)
        		  EClist= EClist + ", ";
          }
          int NbLines= eca.length/27 + 1;
          String[] ErrorLabels= new String[NbLines];
          for (int k=0; k<NbLines; k++) {
            if (27*k==eca.length)
              break;

            String lab;
            if (27*k+1<eca.length)
              lab= eca[27*k]+", ";
            else
              lab= eca[27*k].toString();

            for (int m=1; m<27; m++)
              if (27*k+m >= eca.length)
                break;
              else {
                lab= lab+eca[27*k+m];
                if (27*k+m!=eca.length-1)
                  lab= lab+", ";
              }
            ErrorLabels[k]= lab;
          }
          for (int k=0; k<NbLines; k++) {
            JLabel jLabelErrorCases= new JLabel();
            jLabelErrorCases.setToolTipText(LocaleKBCT.GetString("ErrorCasesToolTipText"));
            if (k==0) {
              jLabelErrorCases.setText(LocaleKBCT.GetString("ErrorCases") + ": "+ErrorLabels[k]);
              jPanelResult.add(jLabelErrorCases, new GridBagConstraints(0, 10+aux, 2, 1, 0.0, 0.0
                    ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 2, 2), 0, 0));
            } else {
              jLabelErrorCases.setText(ErrorLabels[k]);
              jPanelResult.add(jLabelErrorCases, new GridBagConstraints(0, 10+aux, 2, 1, 0.0, 0.0
                    ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, LocaleKBCT.GetString("ErrorCases").length()*8, 2, 2), 0, 0));
            }
            aux++;
          }
          jPanelResult.add( jTable2, new GridBagConstraints(0, 10+aux, 0, 1, 1.0, 1.0
                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
          aux++;
        }
        if (!AmbiguityCases.isEmpty()) {
          Object[] aca= AmbiguityCases.toArray();
          NbTAC=aca.length;
          for (int m=0; m<NbTAC; m++) {
        	  AClist= AClist + aca[m].toString();
        	  if (m!=NbTAC-1)
        		  AClist= AClist + ", ";
          }
          int NbLines= aca.length/25 + 1;
          String[] AmbiguityLabels= new String[NbLines];
          for (int k=0; k<NbLines; k++) {
            if (25*k==aca.length)
              break;

            String lab;
            if (25*k+1<aca.length)
              lab= aca[25*k]+", ";
            else
              lab= aca[25*k].toString();

            for (int m=1; m<25; m++)
              if (25*k+m >= aca.length)
                break;
              else {
                lab= lab+aca[25*k+m];
                if (25*k+m!=aca.length-1)
                  lab= lab+", ";
              }
            AmbiguityLabels[k]= lab;
          }
          for (int k=0; k<NbLines; k++) {
            JLabel jLabelAmbiguityCases= new JLabel();
            jLabelAmbiguityCases.setToolTipText(LocaleKBCT.GetString("AmbiguityCasesToolTipText"));
            if (k==0) {
              jLabelAmbiguityCases.setText(LocaleKBCT.GetString("AmbiguityCases") + ": "+AmbiguityLabels[k]);
              jPanelResult.add(jLabelAmbiguityCases, new GridBagConstraints(0, 10+aux, 2, 1, 0.0, 0.0
                    ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 2, 2), 0, 0));
            } else {
              jLabelAmbiguityCases.setText(AmbiguityLabels[k]);
              jPanelResult.add(jLabelAmbiguityCases, new GridBagConstraints(0, 10+aux, 2, 1, 0.0, 0.0
                    ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, LocaleKBCT.GetString("AmbiguityCases").length()*8, 2, 2), 0, 0));
            }
            aux++;
          }
          if (!AmbiguityErrorCases.isEmpty()) {
              Object[] aeca= AmbiguityErrorCases.toArray();
              NbACerror=aeca.length;
              for (int m=0; m<NbACerror; m++) {
            	  ACElist= ACElist + aeca[m].toString();
            	  if (m!=NbACerror-1)
            		  ACElist= ACElist + ", ";
              }
              int NbAELines= aeca.length/25 + 1;
              String[] AmbiguityAELabels= new String[NbAELines];
              for (int k=0; k<NbAELines; k++) {
                if (25*k==aeca.length)
                  break;

                String lab;
                if (25*k+1<aeca.length)
                  lab= aeca[25*k]+", ";
                else
                  lab= aeca[25*k].toString();

                for (int m=1; m<25; m++)
                  if (25*k+m >= aeca.length)
                    break;
                  else {
                    lab= lab+aeca[25*k+m];
                    if (25*k+m!=aeca.length-1)
                      lab= lab+", ";
                  }
                AmbiguityAELabels[k]= lab;
              }
              for (int k=0; k<NbAELines; k++) {
                JLabel jLabelAmbiguityErrorCases= new JLabel();
                jLabelAmbiguityErrorCases.setToolTipText(LocaleKBCT.GetString("AmbiguityErrorCasesToolTipText"));
                if (k==0) {
                  jLabelAmbiguityErrorCases.setText(LocaleKBCT.GetString("AmbiguityErrorCases") + ": "+AmbiguityAELabels[k]);
                  jPanelResult.add(jLabelAmbiguityErrorCases, new GridBagConstraints(0, 10+aux, 2, 1, 0.0, 0.0
                        ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 2, 2), 0, 0));
                } else {
                  jLabelAmbiguityErrorCases.setText(AmbiguityAELabels[k]);
                  jPanelResult.add(jLabelAmbiguityErrorCases, new GridBagConstraints(0, 10+aux, 2, 1, 0.0, 0.0
                        ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, LocaleKBCT.GetString("AmbiguityErrorCases").length()*8, 2, 2), 0, 0));
                }
                aux++;
              }
            }
          jPanelResult.add( jTable3, new GridBagConstraints(0, 10+aux, 0, 1, 1.0, 1.0
                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
        }
      }
      this.jPanelResults.add(jPanelResult, new GridBagConstraints(0, output_number, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

      this.jMainPanelResults.setWheelScrollingEnabled(true);
      this.jMainPanelResults.getViewport().add(this.jPanelResults, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("Data")+"= "+NbData, "Quality");
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("ErrorCases")+"= "+NbTEC, "Quality");
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("AmbiguityCases")+"= "+NbTAC, "Quality");
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("AmbiguityErrorCases")+"= "+NbACerror, "Quality");
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("NoClassifCases")+"= "+NbTUC, "Quality");
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("NoClassifErrorCases")+"= "+NbUCerror, "Quality");
      String datacc=df.format(NbData)+"	";
      datacc=datacc+df.format(NbTEC)+"	";
      datacc=datacc+df.format(NbTAC)+"	";
      datacc=datacc+df.format(NbACerror)+"	";
      datacc=datacc+df.format(NbTUC)+"	";
      datacc=datacc+df.format(NbUCerror)+"	";
      datacc=datacc+df.format(result[1]*100)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("Coverage")+"= "+df.format(result[1]*100), "Quality");
      datacc=datacc+df.format(jdfAvgCFD.getValue())+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("AvgCFD")+"= "+df.format(jdfAvgCFD.getValue()), "Quality");
      datacc=datacc+df.format(jdfMinCFD.getValue())+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("MinCFD")+"= "+df.format(jdfMinCFD.getValue()), "Quality");
      datacc=datacc+df.format(jdfMaxCFD.getValue())+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("MaxCFD")+"= "+df.format(jdfMaxCFD.getValue()), "Quality");
      jdfMaxError.setValue(Math.abs(result[2]));
      datacc=datacc+df.format(Math.abs(result[2]))+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("MaxError")+"= "+df.format(result[2]), "Quality");
      this.indAcc= 1 - ((double)(NbTEC + NbACerror + NbTUC))/((double)NbData);
      datacc=datacc+df.format(this.indAcc)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("Accuracy")+"= "+df.format(this.indAcc), "Quality");
      jdfAccuracy.setValue(this.indAcc);
      this.indAccCons= 1 - ((double)(NbTEC + NbTAC + NbTUC))/((double)NbData);
      datacc=datacc+df.format(this.indAccCons)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("Accuracy")+"(CONS) = "+df.format(this.indAccCons), "Quality");
      jdfAccuracyCons.setValue(this.indAccCons);
      this.indAccComparison= 1 - ((double)(NbTEC + NbACerror + NbUCerror))/((double)NbData);
      datacc=datacc+df.format(this.indAccComparison)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("Accuracy")+"(BT=0) = "+df.format(this.indAccComparison), "Quality");
      jdfAccuracyComparison.setValue(this.indAccComparison);
      datacc=datacc+df.format(Precision)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("Precision")+"= "+Precision, "Quality");
      datacc=datacc+df.format(Recall)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("Recall")+"= "+Recall, "Quality");
      datacc=datacc+df.format(Fmeasure)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("Fmeasure")+"= "+Fmeasure, "Quality");
      jdfSCE.setValue(this.indSCE);
      datacc=datacc+df.format(this.indSCE)+"	";
      MessageKBCT.WriteLogFile("	**********************************************", "Quality");
      // Table of statistics
      int nbRowsT1=this.jTable1.getModel().getRowCount();
      int nbColsT1=this.jTable1.getModel().getColumnCount();
      for (int k=0; k<nbRowsT1; k++) {
    	  String rowlab= "";
          for (int l=0; l<nbColsT1; l++) {
        	   rowlab= rowlab + this.jTable1.getModel().getValueAt(k,l);
        	   if (l<nbColsT1-1)
        		   rowlab= rowlab + ";";
          }
          MessageKBCT.WriteLogFile("	"+rowlab, "Quality");
      }
      if (!UClist.equals("")) {
          MessageKBCT.WriteLogFile("	**********************************************", "Quality");
          MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("NoClassifCases")+": "+UClist, "Quality");
          if (!UCElist.equals(""))
              MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("NoClassifErrorCases")+": "+UCElist, "Quality");
          // Confussion matrix (Ambiguity cases)
      }
      if (!EClist.equals("")) {
          MessageKBCT.WriteLogFile("	**********************************************", "Quality");
          MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("ErrorCases")+": "+EClist, "Quality");
          // Confussion matrix (Error cases)
          int nbRowsT2=this.jTable2.getModel().getRowCount();
          int nbColsT2=this.jTable2.getModel().getColumnCount();
          for (int k=0; k<nbRowsT2; k++) {
        	  String rowlab= "";
              for (int l=0; l<nbColsT2; l++) {
            	   rowlab= rowlab + this.jTable2.getModel().getValueAt(k,l);
            	   if (l<nbColsT2-1)
            		   rowlab= rowlab + ";";
              }
              MessageKBCT.WriteLogFile("	"+rowlab, "Quality");
          }
      }
      if (!AClist.equals("")) {
          MessageKBCT.WriteLogFile("	**********************************************", "Quality");
          MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("AmbiguityCases")+": "+AClist, "Quality");
          if (!ACElist.equals(""))
              MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("AmbiguityErrorCases")+": "+ACElist, "Quality");
          // Confussion matrix (Ambiguity cases)
          int nbRowsT3=this.jTable3.getModel().getRowCount();
          int nbColsT3=this.jTable3.getModel().getColumnCount();
          for (int k=0; k<nbRowsT3; k++) {
        	  String rowlab= "";
              for (int l=0; l<nbColsT3; l++) {
            	   rowlab= rowlab + this.jTable3.getModel().getValueAt(k,l);
            	   if (l<nbColsT3-1)
            		   rowlab= rowlab + ";";
              }
              MessageKBCT.WriteLogFile("	"+rowlab, "Quality");
          }
      }
      MessageKBCT.WriteLogFile("	**********************************************", "Quality");
      MessageKBCT.WriteLogFile("	EXCEL", "Quality");
      MessageKBCT.WriteLogFile("	"+datacc.replace(".",","), "Quality");
      // todo se guarda en LogQuality
 	  // en LOGacc solo lo del EXCEL
      MessageKBCT.BuildLogFile(JKBCTFrame.BuildFile("LOGacc.txt").getAbsolutePath(),null,null,null, "Interpretability");
      MessageKBCT.WriteLogFile(datacc.replace(".",","), "Interpretability"); // LOGacc.txt
      MessageKBCT.WriteLogFile("	**********************************************", "Quality");
      dd= new Date(System.currentTimeMillis());
      MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(dd), "Quality");
    } catch( Throwable t ) {
        t.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JRulesBaseQualityFrame in ComputeClassification: "+t);
    }
    File f = new File("infer.out");
    f.delete();
    this.validate();
    this.repaint();
  }
//------------------------------------------------------------------------------
  private void ComputeRegression( int output_number ) {
	    try {
	        MessageKBCT.WriteLogFile((LocaleKBCT.GetString("Accuracy")).toUpperCase(), "Quality");
	        Date dd= new Date(System.currentTimeMillis());
	        MessageKBCT.WriteLogFile("----------------------------------", "Quality");
	        MessageKBCT.WriteLogFile("time begin -> "+DateFormat.getTimeInstance().format(dd), "Quality");
	        String tfile="";
	        if (this.TestFile==null) {
	            File DF= new File(this.DataFile.ActiveFileName());
	            tfile= DF.getAbsolutePath();
	        } else {
	      	    tfile= this.TestFile;
	        }
	        //System.out.println("tfile="+tfile);
	        MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("TestFile")+"= "+tfile, "Quality");
	        MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("BlankThres")+" ="+MainKBCT.getConfig().GetBlankThres(), "Quality");
	        fis.JOutput output = this.fis.GetOutput(output_number);
	        double[] result, resulti;
	        if (!FLAG) {
	            result=this.fis.Infer(output_number, this.DataFile.FileName(), this.jTFResultFile.getText(), this.jdfBlankThres.getValue(), false );
	        } else {
	            result=this.fis.Infer(output_number, this.DataFile.FileName(), this.ResultFile, this.BlankThres, false );
	        }
            resulti=this.fis.InferErrorRegression();
	        JPanel jPanelResult= new JPanel(new GridBagLayout());
	    	DoubleField jdfPI= new DoubleField();
	    	this.jdfRMSE= new DoubleField();
	    	this.jdfMSE= new DoubleField();
	    	this.jdfMAE= new DoubleField();
	        this.jdfCoverage= new DoubleField();
	        DoubleField jdfMaxError= new DoubleField();
	        DoubleField jdfAccuracy= new DoubleField();
	        JLabel jLabelPI= new JLabel();
	        JLabel jLabelRMSE= new JLabel();
	        JLabel jLabelMSE= new JLabel();
	        JLabel jLabelMAE= new JLabel();
	        JLabel jLabelCoverage= new JLabel();
	        JLabel jLabelItems= new JLabel();
	        JLabel jLabelMaxError= new JLabel();
	        JLabel jLabelAccuracy= new JLabel();
	        TitledBorder titledBorderResult = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
	        jPanelResult.setBorder(titledBorderResult);
	        jPanelResult.add(jLabelPI, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
	        //jdfPI.setEnabled(false);
	        jdfPI.setBackground(Color.WHITE);
	        jdfPI.setForeground(Color.BLUE);
	        jdfPI.setEditable(false);
	        jPanelResult.add(jdfPI, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 100, 0));
	        jPanelResult.add(jLabelRMSE, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
	        //jdfRMSE.setEnabled(false);
	        jdfRMSE.setBackground(Color.WHITE);
	        jdfRMSE.setForeground(Color.BLUE);
	        jdfRMSE.setEditable(false);
	        jPanelResult.add(jdfRMSE, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 40, 0));
	        jPanelResult.add(jLabelMSE, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
	        //jdfMSE.setEnabled(false);
	        jdfMSE.setBackground(Color.WHITE);
	        jdfMSE.setForeground(Color.BLUE);
	        jdfMSE.setEditable(false);
	        jPanelResult.add(jdfMSE, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 40, 0));
	        jPanelResult.add(jLabelMAE, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
	        //jdfMAE.setEnabled(false);
	        jdfMAE.setBackground(Color.WHITE);
	        jdfMAE.setForeground(Color.BLUE);
	        jdfMAE.setEditable(false);
	        jPanelResult.add(jdfMAE, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 40, 0));
	        jPanelResult.add(jLabelCoverage, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	        //jdfCoverage.setEnabled(false);
	        jdfCoverage.setBackground(Color.WHITE);
	        jdfCoverage.setForeground(Color.BLUE);
	        jdfCoverage.setEditable(false);
	        jPanelResult.add(jdfCoverage, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));

            int c=0;
	        if (result[1] < 1) {
		        jPanelResult.add(jLabelItems, new GridBagConstraints(0, 5+c, 1, 1, 0.0, 0.0
		                ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	        	
                int lim= this.DataFile.DataLength();
                this.covdata= new boolean[lim];
                int cont= 0;
                for (int n=0; n<lim; n++) {
                     this.fis.Infer(this.DataFile.GetData()[n]);
                     double[][] d= this.fis.AgregationResult(output_number);
                     //System.out.println("data.length -> "+d.length);
                     //System.out.println("data[0].length -> "+d[0].length);
                     boolean warning= false;
                     for (int m=0; m<d[0].length; m++) {
                          //System.out.println("d[0][m] -> "+d[0][m]);
                          //System.out.println("d[1][m] -> "+d[1][m]);
                    	  if (d[1][m] > MainKBCT.getConfig().GetBlankThres()) {
                    		  warning= true;
                    	      break;
                    	  }
                     }
                     if (!warning) {
                    	 this.covdata[n]= false;
                    	 cont++;
                     } else {
                    	 this.covdata[n]= true;
                     }
                }
                //System.out.println("data -> "+lim);
                //System.out.println("not covered -> "+cont);
                //double cov= 100*(1-((double)cont)/lim);
                //System.out.println("coverage -> "+cov);
       		    //System.out.print("items: ");
	            Vector lines= new Vector();
	            String l="";
	            int k=0;
                for (int n=0; n<lim; n++) {
                	 if (!this.covdata[n]) {
                		 //System.out.print(String.valueOf(n+1)+", ");
                		 l=l+String.valueOf(n+1);
                		 k++;
                		 if (k < cont)
                			 l= l+", ";
                		 if ( (l.length()>40) || (k==cont) ) {
                			 lines.add(l);
                			 l= "";
                		 }
                	 }
                }
                Object[] obj= lines.toArray();
                int NbLines= obj.length;
                for (int n=0; n<NbLines; n++) {
                	//System.out.println(lines[n].length());
                    JLabel items= new JLabel(obj[n].toString());
                    items.setToolTipText(LocaleKBCT.GetString("NCitems"));
                	jPanelResult.add(items, new GridBagConstraints(1, 5+c, 1, 1, 0.0, 0.0
    		                ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
    	        	
                    c++;
                }
	        }
	        jPanelResult.add(jLabelMaxError, new GridBagConstraints(0, 5+c, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	        //jdfMaxError.setEnabled(false);
	        jdfMaxError.setBackground(Color.WHITE);
	        jdfMaxError.setForeground(Color.BLUE);
	        jdfMaxError.setEditable(false);
	        jPanelResult.add(jdfMaxError, new GridBagConstraints(1, 5+c, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));

	        jdfPI.setValue(resulti[0]);
	        jdfRMSE.setValue(resulti[1]);
	        jdfMSE.setValue((Math.pow(resulti[1],2))/2);
	        jdfMAE.setValue(resulti[2]);
	        jdfCoverage.setValue(result[1]*100);
	        jdfMaxError.setValue(result[2]);
	        jLabelPI.setText("PI" + " :");
	        jLabelRMSE.setText("RMSE" + " :");
	        jLabelMSE.setText("MSE" + " :");
	        jLabelMAE.setText("MAE" + " :");
	        jLabelCoverage.setText(LocaleKBCT.GetString("Coverage") + " (%) :");
	        if (result[1] < 1)
	            jLabelItems.setText(LocaleKBCT.GetString("items") + ":");
	        
	        jLabelMaxError.setText(LocaleKBCT.GetString("MaxError") + " :");
	        jLabelAccuracy.setText(LocaleKBCT.GetString("Accuracy") + " :");
	        titledBorderResult.setTitle(output.GetName());
	        this.jPanelResults.add(jPanelResult, new GridBagConstraints(0, output_number, 2, 1, 1.0, 1.0
	              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	        this.jMainPanelResults.setWheelScrollingEnabled(true);
	        this.jMainPanelResults.getViewport().add(this.jPanelResults, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
	                ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

		    PerformanceFile PerfFile= new PerformanceFile(this.ResultFile);
		    double[][] data= PerfFile.GetData();
		    int NbData= PerfFile.DataLength();
	        MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("Data")+"= "+NbData, "Quality");
	  	  DecimalFormat df= new DecimalFormat();
	  	  df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals()+2);//5
	  	  df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals()+2);//5
	  	  DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
	  	  dfs.setDecimalSeparator((new String(".").charAt(0)));
	  	  df.setDecimalFormatSymbols(dfs);
	  	  df.setGroupingSize(20);
	      String datacc=df.format(NbData)+"	";
	      //System.out.println("result.length="+result.length);
	      datacc=datacc+df.format(result[0])+"	";
	      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("Performance")+"= "+df.format(result[0]), "Quality");
	  	  df.setMaximumFractionDigits(3);
	  	  df.setMinimumFractionDigits(3);
	      datacc=datacc+df.format(result[1]*100)+"	";
	      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("Coverage")+"= "+df.format(result[1]*100), "Quality");
	      datacc=datacc+df.format(result[2])+"	"+df.format(Math.abs(result[2]))+"	";
	      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("MaxError")+"= "+df.format(result[2]), "Quality");
	      this.indAcc= 1 - (result[0])/((double)NbData);
	      datacc=datacc+df.format(this.indAcc)+"	";
	      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("Accuracy")+"= "+df.format(this.indAcc), "Quality");
	      jdfAccuracy.setValue(this.indAcc);
	      datacc=datacc+df.format(resulti[0])+"	";
	      MessageKBCT.WriteLogFile("	PI= "+df.format(resulti[0]), "Quality");
	      datacc=datacc+df.format(resulti[1])+"	";
	      MessageKBCT.WriteLogFile("	RMSE= "+df.format(resulti[1]), "Quality");
	      datacc=datacc+df.format((Math.pow(resulti[1],2))/2)+"	";
	      MessageKBCT.WriteLogFile("	MSE= "+df.format((Math.pow(resulti[1],2))/2), "Quality");
	      datacc=datacc+df.format(resulti[2])+"	";
	      MessageKBCT.WriteLogFile("	MAE= "+df.format(resulti[2]), "Quality");
	      MessageKBCT.WriteLogFile("	**********************************************", "Quality");
	      MessageKBCT.WriteLogFile("	EXCEL", "Quality");
	      MessageKBCT.WriteLogFile("	"+datacc.replace(".",","), "Quality");
	      // todo se guarda en LogQuality
	   	  // en LOGacc solo lo del EXCEL
	      if (tfile.contains("test"))
	          MessageKBCT.BuildLogFile(JKBCTFrame.BuildFile("LOGacc-test.txt").getAbsolutePath(),null,null,null, "Interpretability");
	      else
	          MessageKBCT.BuildLogFile(JKBCTFrame.BuildFile("LOGacc-train.txt").getAbsolutePath(),null,null,null, "Interpretability");
	      	  
	      MessageKBCT.WriteLogFile(datacc.replace(".",","), "Interpretability"); // LOGacc.txt
	      MessageKBCT.WriteLogFile("	**********************************************", "Quality");
	      dd= new Date(System.currentTimeMillis());
	      MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(dd), "Quality");
	  } catch( Throwable t ) {
	      t.printStackTrace();
	      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JRulesBaseQualityFrame in ComputeRegression: "+t);
	  }
	  File f = new File("infer.out");
	  f.delete();
	  this.validate();
	  this.repaint();
  }
//------------------------------------------------------------------------------
  private void InitColumnSizes(String[] LabelNames, int NbLabels) {
    TableColumnModel columns1 = jTable1.getColumnModel();
    int max=0;
    for (int i=0; i<NbLabels; i++) {
      if (max < LabelNames[i].length())
          max= LabelNames[i].length();
    }
    //System.out.println("max="+max);
    int auxll= LocaleKBCT.GetString("Label").length();
    if (max > auxll) 
        columns1.getColumn(0).setPreferredWidth(8*max);
    else 
        columns1.getColumn(0).setPreferredWidth(40+8*auxll);
    	
    columns1.getColumn(1).setPreferredWidth(40+8*LocaleKBCT.GetString("Data").length());
    columns1.getColumn(2).setPreferredWidth(40+8*LocaleKBCT.GetString("ErrorEC").length());
    columns1.getColumn(3).setPreferredWidth(8*LocaleKBCT.GetString("AmbiguityTotal").length());
    columns1.getColumn(4).setPreferredWidth(8*LocaleKBCT.GetString("AmbiguityReal").length());
    columns1.getColumn(5).setPreferredWidth(40+8*LocaleKBCT.GetString("NoClassif").length());
    columns1.getColumn(6).setPreferredWidth(8*LocaleKBCT.GetString("NoClassifErrorCases").length());
    columns1.getColumn(11).setPreferredWidth(8*LocaleKBCT.GetString("Precision").length());
    columns1.getColumn(12).setPreferredWidth(8*LocaleKBCT.GetString("Recall").length());
    columns1.getColumn(13).setPreferredWidth(8*LocaleKBCT.GetString("Fmeasure").length());
    
    TableColumnModel columns2 = jTable2.getColumnModel();
    columns2.getColumn(0).setPreferredWidth(8*max);
    TableColumnModel columns3 = jTable3.getColumnModel();
    columns3.getColumn(0).setPreferredWidth(8*max);
    //System.out.println("max="+max);
    for (int i=0; i<NbLabels; i++) {
        columns2.getColumn(i+1).setPreferredWidth(8*LabelNames[i].length());
        columns3.getColumn(i+1).setPreferredWidth(8*LabelNames[i].length());
    }
  }
//------------------------------------------------------------------------------
  private void SetUpTable(final String[] LabelNames, final Vector[][] EC, final Vector[][] AC) {
    for( int i=0 ; i<this.jTable1.getColumnCount() ; i++ )
      jTable1.getColumnModel().getColumn(i).setCellRenderer( new DefaultTableCellRenderer() {
   	  static final long serialVersionUID=0;	
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        super.setHorizontalAlignment(SwingConstants.CENTER);
        //System.out.println("row="+row+"  column="+column);
        if (row==0) {
            super.setBackground(Color.BLACK);
            super.setForeground(Color.YELLOW);
            if (column==0) {
            	super.setToolTipText(LocaleKBCT.GetString("Label"));
                super.setText(LocaleKBCT.GetString("Label"));
            } else if (column==1) {
            	super.setToolTipText(LocaleKBCT.GetString("Data"));
            	super.setText(LocaleKBCT.GetString("Data"));
            } else if (column==2) {
            	super.setToolTipText(LocaleKBCT.GetString("ErrorTTT"));
            	super.setText(LocaleKBCT.GetString("ErrorEC"));
            } else if (column==3) {
            	super.setToolTipText(LocaleKBCT.GetString("AmbiguityTotalTTT"));
            	super.setText(LocaleKBCT.GetString("AmbiguityTotal"));
            } else if (column==4) {
            	super.setToolTipText(LocaleKBCT.GetString("AmbiguityRealTTT"));
            	super.setText(LocaleKBCT.GetString("AmbiguityReal"));
            } else if (column==5) {
            	super.setToolTipText(LocaleKBCT.GetString("NoClassifTTT"));
            	super.setText(LocaleKBCT.GetString("NoClassif"));
            } else if (column==6) {
            	super.setToolTipText(LocaleKBCT.GetString("NoClassifErrorCasesTTT"));
            	super.setText(LocaleKBCT.GetString("NoClassifErrorCases"));
            } else if (column==7) {
            	super.setToolTipText(LocaleKBCT.GetString("tpTTT"));
            } else if (column==8) {
            	super.setToolTipText(LocaleKBCT.GetString("fpTTT"));
            } else if (column==9) {
            	super.setToolTipText(LocaleKBCT.GetString("tnTTT"));
            } else if (column==10) {
            	super.setToolTipText(LocaleKBCT.GetString("fnTTT"));
            }
        } else if (row==LabelNames.length+1) {
            super.setBackground(Color.LIGHT_GRAY);
            super.setForeground(Color.BLUE);
            if (column==2)
            	super.setToolTipText(LocaleKBCT.GetString("ErrorCases")+" "+LocaleKBCT.GetString("InDataFile"));
            else if (column==3)
            	super.setToolTipText(LocaleKBCT.GetString("AmbiguityCases")+" "+LocaleKBCT.GetString("InDataFile"));
            else if (column==4)
            	super.setToolTipText(LocaleKBCT.GetString("AmbiguityErrorCases")+" "+LocaleKBCT.GetString("InDataFile"));
            else if (column==5)
            	super.setToolTipText(LocaleKBCT.GetString("NoClassifCases")+" "+LocaleKBCT.GetString("InDataFile"));
            else if (column==6)
            	super.setToolTipText(LocaleKBCT.GetString("NoClassifErrorCases")+" "+LocaleKBCT.GetString("InDataFile"));
            else if (column==7)
            	super.setToolTipText(LocaleKBCT.GetString("tpTTT")+" "+LocaleKBCT.GetString("InDataFile"));
            else if (column==8)
            	super.setToolTipText(LocaleKBCT.GetString("fpTTT")+" "+LocaleKBCT.GetString("InDataFile"));
            else if (column==9)
            	super.setToolTipText(LocaleKBCT.GetString("tnTTT")+" "+LocaleKBCT.GetString("InDataFile"));
            else if (column==10)
            	super.setToolTipText(LocaleKBCT.GetString("tnTTT")+" "+LocaleKBCT.GetString("InDataFile"));

        } else if (row>0) {
            super.setBackground(Color.WHITE);
            super.setForeground(Color.BLUE);
            if (column==0)
                super.setBackground(Color.GREEN);
            else if (column==2)
            	super.setToolTipText(LocaleKBCT.GetString("NbErrorCases")+" "+LocaleKBCT.GetString("Output")+" "+LabelNames[row-1]+" "+LocaleKBCT.GetString("InDataFile"));
            else if (column==3)
            	super.setToolTipText(LocaleKBCT.GetString("NbAmbiguityCases")+" "+LocaleKBCT.GetString("Output")+" "+LabelNames[row-1]+" "+LocaleKBCT.GetString("InDataFile"));
            else if (column==4)
            	super.setToolTipText(LocaleKBCT.GetString("NbAmbiguityErrorCases")+" "+LocaleKBCT.GetString("Output")+" "+LabelNames[row-1]+" "+LocaleKBCT.GetString("InDataFile"));
            else if (column==5)
            	super.setToolTipText(LocaleKBCT.GetString("NbNoClassifCases")+" "+LocaleKBCT.GetString("Output")+" "+LabelNames[row-1]+" "+LocaleKBCT.GetString("InDataFile"));
            else if (column==6)
            	super.setToolTipText(LocaleKBCT.GetString("NbNoClassifErrorCases")+" "+LocaleKBCT.GetString("Output")+" "+LabelNames[row-1]+" "+LocaleKBCT.GetString("InDataFile"));
            else if (column==7)
            	super.setToolTipText(LocaleKBCT.GetString("tpTTT")+" ("+LocaleKBCT.GetString("Output")+" "+LabelNames[row-1]+") "+LocaleKBCT.GetString("InDataFile"));
            else if (column==8)
            	super.setToolTipText(LocaleKBCT.GetString("fpTTT")+" ("+LocaleKBCT.GetString("Output")+" "+LabelNames[row-1]+") "+LocaleKBCT.GetString("InDataFile"));
            else if (column==9)
            	super.setToolTipText(LocaleKBCT.GetString("tnTTT")+" ("+LocaleKBCT.GetString("Output")+" "+LabelNames[row-1]+") "+LocaleKBCT.GetString("InDataFile"));
            else if (column==10)
            	super.setToolTipText(LocaleKBCT.GetString("fnTTT")+" ("+LocaleKBCT.GetString("Output")+" "+LabelNames[row-1]+") "+LocaleKBCT.GetString("InDataFile"));
        }
        return this;
       }
    });
    for( int i=0 ; i<this.jTable2.getColumnCount() ; i++ )
    jTable2.getColumnModel().getColumn(i).setCellRenderer( new DefaultTableCellRenderer() {
   	  static final long serialVersionUID=0;	
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        super.setHorizontalAlignment(SwingConstants.CENTER);
        super.setForeground(Color.BLUE);
        if ( (row==0) || (column==0) )
          super.setBackground(Color.GREEN);
        else {
          super.setBackground(Color.WHITE);
          Vector v= EC[row-1][column-1];
          if (v!=null) {
            Object[] errors= v.toArray();
            if ( (errors!=null) && (errors[0]!=null) ) {
              String msg=errors[0].toString();
              for (int n=1; n<errors.length; n++)
                msg=msg+","+errors[n].toString();

              super.setToolTipText(msg);
            }
          }
        }
        return this;
       }
    });
    for( int i=0 ; i<this.jTable3.getColumnCount() ; i++ )
    jTable3.getColumnModel().getColumn(i).setCellRenderer( new DefaultTableCellRenderer() {
   	  static final long serialVersionUID=0;	
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        super.setHorizontalAlignment(SwingConstants.CENTER);
        super.setForeground(Color.BLUE);
        if ( (row==0) || (column==0) )
          super.setBackground(Color.GREEN);
        else {
          super.setBackground(Color.WHITE);
          Vector v= AC[row-1][column-1];
          if (v!=null) {
            Object[] ambiguities= v.toArray();
            if ( (ambiguities!=null) && (ambiguities[0]!=null) ) {
              String msg=ambiguities[0].toString();
              for (int n=1; n<ambiguities.length; n++)
                msg=msg+","+ambiguities[n].toString();

              super.setToolTipText(msg);
            }
          }
        }
        return this;
       }
    });
  }
//------------------------------------------------------------------------------
  void jMenuPrint_actionPerformed() {
    try {
      Printable p= new Printable() {
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
          if (pageIndex >= 1)
            return Printable.NO_SUCH_PAGE;
          else
            return JRulesBaseQualityFrame.this.print(graphics, pageFormat);
          }
      };
      new JPrintPreview(this, p);
    } catch (Exception ex) { MessageKBCT.Error(null, ex); }
  }
//------------------------------------------------------------------------------
  void jMenuExport_actionPerformed() {
    try {
      ExportDialog export= new ExportDialog();
      JPanel panel = new JPanel() {
   	    static final long serialVersionUID=0;	
        public void paint(Graphics g) {
          JRulesBaseQualityFrame.this.jPanelOutputs.paint(g);
          g.translate(0, JRulesBaseQualityFrame.this.jPanelOutputs.getHeight());
          JRulesBaseQualityFrame.this.jPanelOutputs.paint(g);
          g.setColor(Color.gray);
          g.drawLine(0,0,0, JRulesBaseQualityFrame.this.jPanelOutputs.getHeight()-1);
        }
      };
      panel.setSize(new Dimension(this.jPanelOutputs.getWidth(), this.jPanelOutputs.getHeight()));
      export.showExportDialog( panel, "Export view as ...", panel, "export" );
    } catch (Exception ex) { MessageKBCT.Error(null, ex); }
  }
  //------------------------------------------------------------------------------
  private double computeDiffCFD(double[][] fd) {
      double res=0;
      //System.out.println("fd.length: "+fd.length); -> 2
      //System.out.println("fd[0].length: "+fd[0].length);
      // row 0 -> classes
      // row 1 -> firing degrees
      if ( (fd.length > 1) && (fd[0].length > 1) ) {  
          double max1=Math.max(fd[1][0],fd[1][1]);
          double max2=Math.min(fd[1][0],fd[1][1]);
          for (int i=2; i<fd[0].length; i++) {
           if (fd[1][i] > max1) {
        	   max2= max1;
        	   max1= fd[1][i];
           }
          }
          res= max1 - max2;
      } else {
    	  res= 1;
      }
	  return res;
  }
  //------------------------------------------------------------------------------
  private double getAccIndex() {
     return this.indAcc;
  }
  //------------------------------------------------------------------------------
  public double getAccIndexLRN() {
     return this.indAccLRN;
  }
  //------------------------------------------------------------------------------
  public double getAccIndexTST() {
     return this.indAccTST;
  }
  //------------------------------------------------------------------------------
  public boolean[] getCovData() {
     return this.covdata;
  }
  //------------------------------------------------------------------------------
  public double getCoverage() {
     return this.jdfCoverage.getValue();
  }
  //------------------------------------------------------------------------------
  public double getAvConfFD() {
	  if (this.jdfAvgCFD != null)
          return this.jdfAvgCFD.getValue();
	  else 
		  return -1;
  }
  //------------------------------------------------------------------------------
  public double getRMSE() {
     return this.jdfRMSE.getValue();
  }
  //------------------------------------------------------------------------------
  public double getMSE() {
     return this.jdfMSE.getValue();
  }
  //------------------------------------------------------------------------------
  public double getMAE() {
     return this.jdfMAE.getValue();
  }
  //------------------------------------------------------------------------------
  public int print(java.awt.Graphics g, PageFormat pageFormat) throws PrinterException {
     java.awt.Graphics2D g2 = ( java.awt.Graphics2D )g;
     double scalew=1;
     double scaleh=1;
     double pageHeight = pageFormat.getImageableHeight();
     double pageWidth = pageFormat.getImageableWidth();
     if(  getWidth() >= pageWidth )
       scalew =  pageWidth / getWidth();

     if(  getHeight() >= pageHeight)
       scaleh =  pageWidth / getHeight();

     g2.translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
     g2.scale( scalew, scaleh );
     this.jPanelOutputs.print( g2 );
     return Printable.PAGE_EXISTS;
 }
//------------------------------------------------------------------------------
  void jMenuHelp_actionPerformed() {
	if (this.kbct.GetOutput(1).GetClassif().equals("yes"))
	    MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonQuality.html#AccClassification"));
	else
	    MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonQuality.html#AccRegression"));
		
	MainKBCT.getJB().setVisible(true);
	SwingUtilities.updateComponentTreeUI(this);
  }
}