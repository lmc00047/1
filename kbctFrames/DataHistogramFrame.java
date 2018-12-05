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
//                              DataHistogramFrame.java
//
//
//**********************************************************************

// Contains: DataHistogramFrame, DataHistogramInputFrame, JPanelInput,
// PlotRectangleListener, HistogramPanel, HistogramInputPanel

package kbctFrames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import kbct.JKBCT;
import kbct.JVariable;
import kbct.KBCTListener;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbctAux.DoubleField;
import kbctAux.IntegerField;
import kbctAux.MessageKBCT;
import kbctAux.NumberFieldActionListener;

import org.freehep.util.export.ExportDialog;

import print.JPrintPreview;
import ptolemy.plot.Histogram;
import KB.LabelKBCT;
import fis.FISPlot;
import fis.JExtendedDataFile;
import fis.JFileListener;

/**
 * kbctFrames.DataHistogramFrame displays a frame with data distribution in a histogram.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
//------------------------------------------------------------------------------
public class DataHistogramFrame extends JChildFrame {
  static final long serialVersionUID=0;	  
  // menus
  private JMenuBar jMenuBarInput= new JMenuBar();
  private JMenu jMenuOptions= new JMenu();
  // menu range
  private JMenu jMenuRange = new JMenu();
  private JMenuItem jMenuPhysicalRange= new JMenuItem();
  private JMenuItem jMenuInterestRange= new JMenuItem();
  // menu options
  private JMenuItem jMenuPrint= new JMenuItem();
  private JMenuItem jMenuExport= new JMenuItem();
  private JMenuItem jMenuHelp= new JMenuItem();
  private JMenuItem jMenuClose= new JMenuItem();
  protected JMenuItem jMenuSave= new JMenuItem();
  protected JMenuItem jMenuEvaluation= new JMenuItem();

  protected JPanel jPanelHisto= new JPanel();
  private JLabel jLabelNumberOfClass= new JLabel();
  private IntegerField jIFNumberOfClass= new IntegerField();
  private JButton jButtonApply= new JButton();
  protected HistogramPanel jGraphHistogram;
  private int DefaultNumberOfClasses= 15;
  protected Histogram Histogram;
  protected JPanel jHistogramFramePanel;
  protected JPanel jPartitionPanel;
  protected JScrollPane jLeftPanel= new JScrollPane();
  private TitledBorder titledBorderExpertPartition;
  protected int DataIndex;
  protected JExtendedDataFile DataFile;
  private JFileListener JFileListener;
  protected String Title;
  protected int x_size= 400;
  protected int y_size= 300;
  private boolean InducePartitions= false;
  private TitledBorder titledBorderHistogram;
  // Panel select partition
  private JPanel jPanelSelectPartition = new JPanel(new GridBagLayout());
  private TitledBorder titledBorderSelectPartition;
  protected JLabel jLabelType = new JLabel();
  protected DefaultComboBoxModel jDCBMTypeModel = new DefaultComboBoxModel() {
    static final long serialVersionUID=0;	
    public Object getElementAt(int index) {
        String Type;
        switch(index) {
          case 0:  Type = "regular"; break;
          case 1:  Type = "hfp"; break;
          case 2:  Type = "kmeans"; break;
          case 3:  Type = LocaleKBCT.GetString("ExpertPartition"); break;
          default: Type = "regular"; break;
        }
        try { return Type; }
        catch( Throwable t ) { return null; }
      }
    };
  protected JComboBox jCBType = new JComboBox(jDCBMTypeModel);
  private JLabel jLabelNumberOfMF = new JLabel();
  protected DefaultComboBoxModel jDCBMNumberOfMFModel= new DefaultComboBoxModel() {
    static final long serialVersionUID=0;	
    public Object getElementAt(int index) {
    	//System.out.println("index="+index);
        return new Integer(index+2);
    }
  };
  protected JComboBox jCBNumberOfMF= new JComboBox(jDCBMNumberOfMFModel);
  protected JButton jbSPApply = new JButton();
  // Panel Range
  private JPanel jPanelRange = new JPanel(new GridBagLayout());
  private TitledBorder titledBorderRange;
  // Panel Expert Range
  private JPanel jPanelExpertRange = new JPanel(new GridBagLayout());
  private TitledBorder titledBorderExpertRange;
  // panel physical range
  private JPanel jPanelRangeP = new JPanel(new GridBagLayout());
  private TitledBorder titledBorderRangeP;
  private JLabel jLabelLowerP = new JLabel();
  private DoubleField dfLowerP = new DoubleField();
  private JLabel jLabelUpperP = new JLabel();
  private DoubleField dfUpperP = new DoubleField();
  // panel interest range
  private JPanel jPanelRangeI = new JPanel(new GridBagLayout());
  private TitledBorder titledBorderRangeI;
  private JLabel jLabelLowerI = new JLabel();
  private DoubleField dfLowerI = new DoubleField();
  private JLabel jLabelUpperI = new JLabel();
  private DoubleField dfUpperI = new DoubleField();
  // panel data range
  private JPanel jPanelDataRange = new JPanel(new GridBagLayout());
  private TitledBorder titledBorderDataRange;
  private JLabel jLabelDataLower = new JLabel();
  private DoubleField dfDataLower = new DoubleField();
  private JLabel jLabelDataUpper = new JLabel();
  private DoubleField dfDataUpper = new DoubleField();

  protected JKBCT kbct;
  protected JVariable jv_expert;
  protected JVariable jv_data;
  protected String Type;
  protected int NOL;
  private JMFsPanel jmfsp;

  protected JEvaluationFuzzyPartitionFrame jeopf_HFP= null;
  protected JEvaluationFuzzyPartitionFrame jeopf_Regular= null;
  protected JEvaluationFuzzyPartitionFrame jeopf_Kmeans= null;
  protected JEvaluationFuzzyPartitionFrame jeopf_All= null;
//------------------------------------------------------------------------------
  public DataHistogramFrame( JMainFrame parent, String title, int data_index, JExtendedDataFile data_file ) {
    super(parent);
    try {
      jbInit(title, data_index, data_file);
      this.Show();
    } catch(Throwable t) {
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en constructor en DataHistogramFrame: "+t);
    }
  }
//------------------------------------------------------------------------------
  public DataHistogramFrame( JMainFrame parent ) { super(parent); }
//------------------------------------------------------------------------------
  protected void jbInit( String title, int data_index, JExtendedDataFile data_file ) throws Throwable {
    JMenuBar jmb = new JMenuBar();
    jmb.add(this.jMenuPrint);
    jmb.add(this.jMenuExport);
    jmb.add(this.jMenuHelp);
    jmb.add(this.jMenuClose);
    this.setJMenuBar(jmb);
    this.Title = title;
    this.DataIndex = data_index;
    this.DataFile= new JExtendedDataFile(data_file.ActiveFileName(), true);
    this.jHistogramFramePanel = new JPanel(new GridBagLayout());
    this.getContentPane().add(this.jHistogramFramePanel);
    // panel histogramme
    this.jPanelHisto.setLayout(new GridBagLayout());
    this.jHistogramFramePanel.add(jPanelHisto, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    this.jPanelHisto.add(this.jLabelNumberOfClass, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.jIFNumberOfClass.setValue(this.DefaultNumberOfClasses);
    this.jIFNumberOfClass.SetAction( new NumberFieldActionListener() {
      public void NumberFieldAction() {
        if( DataHistogramFrame.this.jIFNumberOfClass.getValue() <= 0 )
          DataHistogramFrame.this.jIFNumberOfClass.setValue(DataHistogramFrame.this.DefaultNumberOfClasses);
      }
    });
    this.jPanelHisto.add(this.jIFNumberOfClass, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 30, 0));
    this.jButtonApply.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) { DataHistogramFrame.this.ApplyNumberOfClasses(); }
    });
    this.jPanelHisto.add(this.jButtonApply, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.jGraphHistogram = this.HistogramPanel();
    this.jGraphHistogram.SetNbBins(this.DefaultNumberOfClasses);
    this.jGraphHistogram.AddData(this.DataFile.VariableData(this.DataIndex));
    this.jPanelHisto.add(this.jGraphHistogram,     new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 0, 0), 0, 0));
    // evenements
    this.JFileListener= new JFileListener() {
       public void Closed() { DataHistogramFrame.this.dispose(); }
       public void ReLoaded() { DataHistogramFrame.this.DataReLoaded(); }
    };
    this.DataFile.AddJFileListener(this.JFileListener);
    jMenuPrint.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuPrint_actionPerformed(); } });
    jMenuExport.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExport_actionPerformed(); } });
    jMenuHelp.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuHelp_actionPerformed(); } });
    jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuClose_actionPerformed(); } });
  }
//------------------------------------------------------------------------------
  protected void jbInitInducePartitions( String title, int data_index, JExtendedDataFile data_file, JKBCT kbct, JVariable jve, JVariable jvd, String SugestedPartition ) throws Throwable {
    this.InducePartitions= true;
    this.kbct= kbct;
    this.jv_expert= jve;
    this.jv_data= jvd;
    // menus
    this.setJMenuBar(this.jMenuBarInput);
    jMenuBarInput.add(jMenuRange);
      jMenuRange.add(jMenuPhysicalRange);
      jMenuRange.add(jMenuInterestRange);
    jMenuBarInput.add(jMenuSave);
    jMenuBarInput.add(jMenuEvaluation);
    jMenuBarInput.add(jMenuPrint);
    jMenuBarInput.add(jMenuExport);
    jMenuBarInput.add(jMenuHelp);
    jMenuBarInput.add(jMenuClose);

    this.Title = title;
    this.DataIndex = data_index;
    this.DataFile= new JExtendedDataFile(data_file.ActiveFileName(), true);
    this.jHistogramFramePanel = new JPanel(new GridBagLayout());
    this.getContentPane().add(this.jHistogramFramePanel);
    // panel histogramme
    JPanel jp1= new JPanel(new GridBagLayout());
    this.jHistogramFramePanel.add(jp1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
           ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
    this.jPartitionPanel= new JPanel(new GridBagLayout());
    this.jPanelHisto.setLayout(new GridBagLayout());
    this.titledBorderHistogram= new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    this.jPanelHisto.setBorder(titledBorderHistogram);
    jPartitionPanel.add(jPanelHisto, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
           ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jp1.add(jPartitionPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
           ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.jPanelHisto.add(this.jLabelNumberOfClass, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
           ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
    this.jIFNumberOfClass.setValue(this.DefaultNumberOfClasses);
    this.jIFNumberOfClass.SetAction( new NumberFieldActionListener() {
      public void NumberFieldAction() {
        if( DataHistogramFrame.this.jIFNumberOfClass.getValue() <= 0 )
          DataHistogramFrame.this.jIFNumberOfClass.setValue(DataHistogramFrame.this.DefaultNumberOfClasses);
      }
    });
    this.jPanelHisto.add(this.jIFNumberOfClass, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
          ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 30, 0));
    this.jButtonApply.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) { DataHistogramFrame.this.ApplyNumberOfClasses(); }
    });
    this.jPanelHisto.add(this.jButtonApply, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
          ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));
    this.jGraphHistogram = this.HistogramPanel();
    this.jGraphHistogram.SetNbBins(this.DefaultNumberOfClasses);
    this.jGraphHistogram.AddData(this.DataFile.VariableData(this.DataIndex));
    this.jPanelHisto.add(this.jGraphHistogram, new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0
          ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 0, 0), 0, 0));
    JPanel jp_aux= new JPanel(new GridBagLayout());
    jmfsp= new JMFsPanel(this.jv_expert);
    FISPlot.DisableZoom(jmfsp);
    this.titledBorderExpertPartition= new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    jp_aux.setBorder(titledBorderExpertPartition);
    jp_aux.add(jmfsp, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
           ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jPartitionPanel.add(jp_aux, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
           ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    JPanel jp2= new JPanel(new GridBagLayout());
    jp1.add(jp2, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
           ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jp2.add(this.jPanelSelectPartition, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
             ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    // panel select partition
    this.titledBorderSelectPartition= new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    this.jPanelSelectPartition.setBorder(this.titledBorderSelectPartition);
    // Type
    this.jPanelSelectPartition.add(jLabelType, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
    for( int i=0 ; i<4 ; i++ )
      this.jDCBMTypeModel.addElement(new String());

    //this.jDCBMTypeModel.setSelectedItem("regular");
    //this.Type= "regular";
    this.jDCBMTypeModel.setSelectedItem(SugestedPartition);
    this.Type= SugestedPartition;
    this.jPanelSelectPartition.add(jCBType, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
    // Number of MF
    this.jPanelSelectPartition.add(jLabelNumberOfMF, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
    for( int i=2 ; i<10 ; i++ )
      this.jDCBMNumberOfMFModel.addElement(new String());

    this.jDCBMNumberOfMFModel.setSelectedItem(new Integer(this.jv_expert.GetLabelsNumber()));
    this.NOL= this.jv_expert.GetLabelsNumber();
    this.jPanelSelectPartition.add(jCBNumberOfMF, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
    this.jPanelSelectPartition.add(this.jbSPApply, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
             ,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
    // panel range
    this.titledBorderRange= new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    this.jPanelRange.setBorder(this.titledBorderRange);
    jp2.add(jPanelRange, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    // panel expert range
    this.titledBorderExpertRange= new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    this.jPanelExpertRange.setBorder(this.titledBorderExpertRange);
    // panel physical range
    this.titledBorderRangeP = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    this.jPanelRangeP.setBorder(this.titledBorderRangeP);
    this.jPanelRangeP.add(this.jLabelLowerP, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    this.jPanelRangeP.add(this.dfLowerP, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 50, 0));
    this.jPanelRangeP.add(this.jLabelUpperP, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    this.jPanelRangeP.add(this.dfUpperP, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 50, 0));
    this.jPanelExpertRange.add(this.jPanelRangeP, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.dfLowerP.setEnabled(false);
    this.dfUpperP.setEnabled(false);
    // panel interest range
    this.titledBorderRangeI = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    this.jPanelRangeI.setBorder(this.titledBorderRangeI);
    this.jPanelRangeI.add(this.jLabelLowerI, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    this.jPanelRangeI.add(this.dfLowerI, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 50, 0));
    this.jPanelRangeI.add(this.jLabelUpperI, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    this.jPanelRangeI.add(this.dfUpperI, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 50, 0));
    this.jPanelExpertRange.add(this.jPanelRangeI, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.dfLowerI.setEnabled(false);
    this.dfUpperI.setEnabled(false);
    this.jPanelRange.add(jPanelExpertRange, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    // panel data range
    this.titledBorderDataRange= new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    this.jPanelDataRange.setBorder(this.titledBorderDataRange);
    this.jPanelDataRange.add(this.jLabelDataLower, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    this.jPanelDataRange.add(this.dfDataLower, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 50, 0));
    this.jPanelDataRange.add(this.jLabelDataUpper, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    this.jPanelDataRange.add(this.dfDataUpper, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 50, 0));
    this.dfDataLower.setEnabled(false);
    this.dfDataUpper.setEnabled(false);
    this.jPanelRange.add(this.jPanelDataRange, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    // evenements
    this.JFileListener = new JFileListener() {
       public void Closed() { DataHistogramFrame.this.dispose(); }
       public void ReLoaded() { DataHistogramFrame.this.DataReLoaded(); }
    };
    this.DataFile.AddJFileListener(this.JFileListener);
    jMenuPhysicalRange.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuPhysicalRange_actionPerformed(); } });
    jMenuInterestRange.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuInterestRange_actionPerformed(); } });
    jMenuPrint.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuPrint_actionPerformed(); } });
    jMenuExport.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExport_actionPerformed(); } });
    jMenuHelp.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuHelp_actionPerformed(); } });
    jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuClose_actionPerformed(); } });
    jCBType.addItemListener(new ItemListener() { public void itemStateChanged(ItemEvent e) { jType_itemStateChanged(e); }});
    jCBNumberOfMF.addItemListener(new ItemListener() { public void itemStateChanged(ItemEvent e) { jNOL_itemStateChanged(e); }});
  }
//------------------------------------------------------------------------------
  protected void ApplyNumberOfClasses() { DataHistogramFrame.this.jGraphHistogram.SetNbBins(DataHistogramFrame.this.jIFNumberOfClass.getValue()); }
//------------------------------------------------------------------------------
  protected void DataReLoaded() {
    this.jGraphHistogram.AddData(this.DataFile.VariableData(this.DataIndex));
    this.repaint();
  }
//------------------------------------------------------------------------------
  protected HistogramPanel HistogramPanel() throws Throwable { return new HistogramPanel(); }
//------------------------------------------------------------------------------
  public void Show() {
    this.setTitle(this.Title);
    this.Translate();
    this.pack();
    this.setSize(this.x_size,this.y_size);
    this.setLocation(this.ChildPosition(this.getSize()));
    this.setVisible(true);
  }
//------------------------------------------------------------------------------
  public void dispose() {
    super.dispose();
    if (this.jeopf_HFP!=null)
      this.jeopf_HFP.dispose();
    if (this.jeopf_Regular!=null)
      this.jeopf_Regular.dispose();
    if (this.jeopf_Kmeans!=null)
      this.jeopf_Kmeans.dispose();
    if (this.jeopf_All!=null)
      this.jeopf_All.dispose();

    this.DataFile.RemoveJFileListener(this.JFileListener);
  }
//------------------------------------------------------------------------------
  public void Translate() {
    this.setTitle(this.Title);
    this.jLabelNumberOfClass.setText(LocaleKBCT.GetString("Classes")+":");
    this.jButtonApply.setText(LocaleKBCT.GetString("Apply"));
    this.jMenuPrint.setText(LocaleKBCT.GetString("Print"));
    this.jMenuExport.setText(LocaleKBCT.GetString("Export"));
    this.jMenuHelp.setText(LocaleKBCT.GetString("Help"));
    this.jMenuClose.setText(LocaleKBCT.GetString("Close"));
    if (this.InducePartitions) {
      this.jMenuOptions.setText(LocaleKBCT.GetString("Options"));
      this.jMenuRange.setText(LocaleKBCT.GetString("Range"));
      this.jMenuPhysicalRange.setText(LocaleKBCT.GetString("RangeP"));
      this.jMenuInterestRange.setText(LocaleKBCT.GetString("RangeI"));
      this.jMenuSave.setText(LocaleKBCT.GetString("Save"));
      this.jMenuEvaluation.setText(LocaleKBCT.GetString("Evaluation"));
      this.jMenuEvaluation.setToolTipText(LocaleKBCT.GetString("EvaluationOfFuzzyPartitions"));
      this.titledBorderHistogram.setTitle(LocaleKBCT.GetString("DataDistribution"));
      this.titledBorderExpertPartition.setTitle(LocaleKBCT.GetString("ExpertPartition"));
      this.titledBorderSelectPartition.setTitle(LocaleKBCT.GetString("SelectPartition"));
      this.jLabelType.setText(LocaleKBCT.GetString("TypeOfPartition") + ":");
      this.jLabelNumberOfMF.setText(LocaleKBCT.GetString("NOL") + ":");
      this.titledBorderRange.setTitle(LocaleKBCT.GetString("Range"));
      this.titledBorderExpertRange.setTitle(LocaleKBCT.GetString("Expert"));
      this.titledBorderRangeP.setTitle(LocaleKBCT.GetString("RangeP"));
      this.titledBorderRangeI.setTitle(LocaleKBCT.GetString("RangeI"));
      this.titledBorderDataRange.setTitle(LocaleKBCT.GetString("Data"));
      this.jLabelLowerP.setText(LocaleKBCT.GetString("Lower") + ":");
      this.jLabelUpperP.setText(LocaleKBCT.GetString("Upper") + ":");
      this.jLabelLowerI.setText(LocaleKBCT.GetString("Lower") + ":");
      this.jLabelUpperI.setText(LocaleKBCT.GetString("Upper") + ":");
      this.jLabelDataLower.setText(LocaleKBCT.GetString("Lower") + ":");
      this.jLabelDataUpper.setText(LocaleKBCT.GetString("Upper") + ":");
      this.dfLowerP.setLocale(LocaleKBCT.Locale());
      this.dfLowerP.setValue(this.jv_expert.GetInputPhysicalRange()[0]);
      this.dfUpperP.setLocale(LocaleKBCT.Locale());
      this.dfUpperP.setValue(this.jv_expert.GetInputPhysicalRange()[1]);
      this.dfLowerI.setLocale(LocaleKBCT.Locale());
      this.dfLowerI.setValue(this.jv_expert.GetInputInterestRange()[0]);
      this.dfUpperI.setLocale(LocaleKBCT.Locale());
      this.dfUpperI.setValue(this.jv_expert.GetInputInterestRange()[1]);
      this.dfDataLower.setLocale(LocaleKBCT.Locale());
      this.dfDataLower.setValue(this.jv_data.GetInputInterestRange()[0]);
      this.dfDataUpper.setLocale(LocaleKBCT.Locale());
      this.dfDataUpper.setValue(this.jv_data.GetInputInterestRange()[1]);
      this.jbSPApply.setText(LocaleKBCT.GetString("Apply"));
    }
  }
//------------------------------------------------------------------------------
  protected String RangeTypeP() { return new String("PhysicalRange"); }
//------------------------------------------------------------------------------
  protected String RangeTypeI() { return new String("InterestRange"); }
//------------------------------------------------------------------------------
  void jMenuPhysicalRange_actionPerformed() {
    JRangeFrame jrf = new JRangeFrame(this, this.jv_expert, this.RangeTypeP());
    jrf.setLocation(JChildFrame.ChildPosition(this, jrf.getSize()));
    double [] new_range = jrf.Show();
    double[] InterestDataRange= jv_data.GetInputInterestRange();
    if( new_range != null ) {
      try {
        if ( !((new_range[0]<=InterestDataRange[0]) &&
               (new_range[1]>=InterestDataRange[1]) ))
               throw new Throwable("RangeExpert>RangeData");

        if ((new_range[0]<=this.jv_expert.GetInputInterestRange()[0]) && (new_range[1]>=this.jv_expert.GetInputInterestRange()[1])){
          this.jv_expert.SetInputPhysicalRange(new_range);
          this.dfLowerP.setValue(new_range[0]);
          this.dfUpperP.setValue(new_range[1]);
        } else
            throw new Throwable("RangeP>RangeI");
      } catch( Throwable t ) {
        String message= t.getMessage();
        if (message.equals("RangeP>RangeI") || message.equals("RangeExpert>RangeData"))
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString(t.getMessage()));
        else
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en DataHistogramFrame en jMenuPhysicalRange_actionPerformed: "+t.getMessage());
      }
    }
  }
//------------------------------------------------------------------------------
  void jMenuInterestRange_actionPerformed() {
    JRangeFrame jrf = new JRangeFrame(this, this.jv_expert, this.RangeTypeI());
    jrf.setLocation(JChildFrame.ChildPosition(this, jrf.getSize()));
    double [] new_range = jrf.Show();
    if( new_range != null ) {
      try {
        if ( !((new_range[0]>=this.jv_expert.GetInputPhysicalRange()[0]) &&
               (new_range[1]<=this.jv_expert.GetInputPhysicalRange()[1]) ))
             throw new Throwable("RangeP>RangeI");
        else {
          this.jv_expert.SetInputInterestRange(new_range);
          // repaint expert partition
          this.jmfsp.SetXRange(new_range);
          this.dfLowerI.setValue(new_range[0]);
          this.dfUpperI.setValue(new_range[1]);
          Vector<String> MPE= new Vector<String>();
          int NOL= this.jv_expert.GetLabelsNumber();
          String MPA[]= new String[NOL];
          for (int n=0; n<NOL; n++) {
            String MP= this.jv_expert.GetMP(n+1);
            MPA[n]=MP;
            if (!MP.equals("No MP")) {
              Double modal_point= new Double(MP);
              double[] InterestRange= this.jv_expert.GetInputInterestRange();
              if ( (modal_point.doubleValue()<InterestRange[0]) ||
                   (modal_point.doubleValue()>InterestRange[1]) ) {
                this.jv_expert.SetMP(n+1, "No MP", false);
                MPA[n]="No MP";
                MPE.add(MP);
              }
            }
          }
          Enumeration en= MPE.elements();
          String ModalPoints= "";
          int cont=0;
          while (en.hasMoreElements()) {
            cont++;
            ModalPoints= ModalPoints+LocaleKBCT.GetString("ModalPoint")+": "+ en.nextElement() +"\n";
          }
          if (!ModalPoints.equals(""))
            if (cont>1)
              MessageKBCT.Information(this, ModalPoints + LocaleKBCT.GetString("Out_of_range1"));
            else
              MessageKBCT.Information(this, ModalPoints + LocaleKBCT.GetString("Out_of_range2"));

          for (int n=0; n<NOL; n++) {
            LabelKBCT l= null;
            if (n==0) {
                l= this.jv_expert.GetLabel(1);
                l.SetP1(new_range[0]);
                this.jv_expert.ReplaceLabel(1, l);
            } else if (n==NOL-1) {
                l= this.jv_expert.GetLabel(NOL);
                l.SetP3(new_range[1]);
                this.jv_expert.ReplaceLabel(NOL, l);
            }
            this.jv_expert.SetMP(n+1, MPA[n], false);
          }
        }
      } catch( Throwable t ) {
        String message= t.getMessage();
        if (message.equals("RangeP>RangeI") || message.equals("RangeExpert>RangeData"))
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString(t.getMessage()));
        else
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en DataHistogramFrame en jMenuInterestRange_actionPerformed: "+t.getMessage());
      }
    }
  }
//------------------------------------------------------------------------------
  void jType_itemStateChanged(ItemEvent e) {
    if( e.getStateChange() == ItemEvent.SELECTED )
      this.Type= (String)this.jDCBMTypeModel.getElementAt(this.jCBType.getSelectedIndex());
  }
//------------------------------------------------------------------------------
  void jNOL_itemStateChanged(ItemEvent e) {
    if( e.getStateChange() == ItemEvent.SELECTED )
      this.NOL= ((Integer)this.jDCBMNumberOfMFModel.getElementAt(this.jCBNumberOfMF.getSelectedIndex())).intValue();
  }
//------------------------------------------------------------------------------
  void jMenuClose_actionPerformed() { 
      //MessageKBCT.Information(this, "PARTITION NOT SAVED YET");
	  this.dispose();
  }
//------------------------------------------------------------------------------
  void jMenuPrint_actionPerformed() {
    try {
      Printable p= new Printable() {
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
          if (pageIndex >= 1)
              return Printable.NO_SUCH_PAGE;
          else
              return DataHistogramFrame.this.print(graphics, pageFormat);
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
          DataHistogramFrame.this.jHistogramFramePanel.paint(g);
          g.translate(0, DataHistogramFrame.this.jHistogramFramePanel.getHeight());
          DataHistogramFrame.this.jHistogramFramePanel.paint(g);
          g.setColor(Color.gray);
          g.drawLine(0,0,0, DataHistogramFrame.this.jHistogramFramePanel.getHeight()-1);
        }
      };
      panel.setSize(new Dimension(this.jHistogramFramePanel.getWidth(), this.jHistogramFramePanel.getHeight()));
      export.showExportDialog( panel, "Export view as ...", panel, "export" );
    } catch (Exception ex) { MessageKBCT.Error(null, ex); }
  }
//------------------------------------------------------------------------------
  public int print(java.awt.Graphics g, PageFormat pageFormat) throws PrinterException {
     java.awt.Graphics2D  g2 = ( java.awt.Graphics2D )g;
     double scalew=1;
     double scaleh=1;
     double pageHeight = pageFormat.getImageableHeight();
     // See how much we need to scale the image to fit in the page's width
     double pageWidth = pageFormat.getImageableWidth();
     if(  getWidth() >= pageWidth )
       scalew = pageWidth / getWidth();

     if(  getHeight() >= pageHeight)
       scaleh =  pageWidth / getHeight();

     g2.translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
     g2.scale( scalew, scaleh );
     this.jHistogramFramePanel.print( g2 );
     return Printable.PAGE_EXISTS;
 }
//------------------------------------------------------------------------------
  void jMenuHelp_actionPerformed() { 
        MainKBCT.setJB(new JBeginnerFrame("main/MainMenuData.html#InducePartitionsWindow"));
		MainKBCT.getJB().setVisible(true);
		SwingUtilities.updateComponentTreeUI(this);
  }
}


//------------------------------------------------------------------------------
// fenêtre data histogram avec affichage du panel de l'entrée ou sortie correspondante
class DataHistogramInputFrame extends DataHistogramFrame {
  static final long serialVersionUID=0;	  
  private JKBCT kbct_Data;
  private int KBCTIndex;
  private JExtendedDataFile DataFile;
  private JFileListener JFileListener;
  private KBCTListener KBCTListener;
  double [] input_range;
  String VariableName;
//------------------------------------------------------------------------------
  public DataHistogramInputFrame( JMainFrame parent, JKBCT kbct, int kbct_index, int data_index, JExtendedDataFile data_file, String VariableName ) throws Throwable {
    super(parent);
    this.kbct_Data= kbct;
    this.KBCTIndex= kbct_index;
    this.VariableName= VariableName;
    super.jbInit( this.VariableName, data_index, data_file);
    this.DataFile= data_file;
    this.JFileListener = new JFileListener() {
        public void Closed() { DataHistogramInputFrame.this.dispose(); }
        public void ReLoaded() { DataHistogramInputFrame.this.DataReLoaded(); }
     };
     this.DataFile.AddJFileListener(this.JFileListener);
    this.Show();
  }
//------------------------------------------------------------------------------
  public void dispose() {
    super.dispose();
    this.DataFile.RemoveJFileListener(this.JFileListener);
    if( this.kbct_Data != null )
      this.kbct_Data.RemoveKBCTListener(this.KBCTListener);
  }
//------------------------------------------------------------------------------
  private void AdjustXRange() {
    this.jGraphHistogram.AdjustXRange();
    this.jGraphHistogram.repaint();
  }
//------------------------------------------------------------------------------
  protected HistogramPanel HistogramPanel() throws Throwable { return new HistogramInputPanel(this.GetInput(this.KBCTIndex)/*, this.Histogram*/); }
//------------------------------------------------------------------------------
  protected void DataReLoaded() {
    try {
      super.DataReLoaded();
      JVariable input = this.GetInput(this.KBCTIndex);
      if( input != null )
        this.AdjustXRange();

      this.repaint();
    } catch( Throwable t ) {
      MessageKBCT.Error( this, LocaleKBCT.GetString("Error"), "Error en DataHistogramInputFrame en DataReLoaded:"+ t);
    }
  }
//------------------------------------------------------------------------------
  protected JVariable GetInput( int index ) throws Throwable {
    if( this.kbct_Data == null )
      return null;

    if( index < this.kbct_Data.GetNbInputs() )
      return this.kbct_Data.GetInput(index+1);
    else
      return this.kbct_Data.GetOutput(index-this.kbct_Data.GetNbInputs()+1);
  }
//------------------------------------------------------------------------------
  protected void ApplyNumberOfClasses() {
    super.ApplyNumberOfClasses();
    this.AdjustXRange();
  }
}


//------------------------------------------------------------------------------
// Classe d'affichage des SEFs
class JPanelInput extends FISPlot implements PlotRectangleListener {
  static final long serialVersionUID=0;	  
  private JVariable Input;
  private Vector MPV;
  protected Rectangle listener_rect;
//------------------------------------------------------------------------------
  public Dimension getPreferredSize() { return new Dimension(20,100); }
  public Dimension getMinimumSize() { return  new Dimension(20,100); }
//------------------------------------------------------------------------------
  public void SetInput( JVariable input ) { this.Input = input; this.repaint(); }
//------------------------------------------------------------------------------
  public void SetMPV( Vector MPV ) { this.MPV = MPV; }
//------------------------------------------------------------------------------
  public JPanelInput(JVariable input) throws Throwable {
    this.setColor(false);
    this.Input= input;
    if( this.Input != null )
      this.setXRange(this.Input.GetInputInterestRange()[0], this.Input.GetInputInterestRange()[1]);

    this.setYRange(0, 1);
    this.AddYTicks();
    FISPlot.DisableZoom(this);
  }
//------------------------------------------------------------------------------
  protected void AddYTicks() {
    this.addYTick("0", 0);
    this.addYTick("", 0.5);
    this.addYTick("1", 1);
  }
//------------------------------------------------------------------------------
  public void PlotRectangle( Rectangle rect ) {
    this.listener_rect = rect;
    this.repaint();
  }
//------------------------------------------------------------------------------
  public void paintComponent(Graphics g) {
    try {
      // affichage de chaque SEF
      this.clear(false);
      for (int i = 0; i < this.Input.GetLabelsNumber(); i++) {
        LabelKBCT et = this.Input.GetLabel(i + 1);
        et.Draw(this, i);
      }
      super.paintComponent(g);
      // ajustement de la taille du rectangle plot au listener
      this.setPlotRectangle(this.listener_rect);
      super.paintComponent(g);
      this.setPlotRectangle(null);  // supprime la memorisation du setPlotRectangle
      if ( this.MPV != null)
        FISPlot.DrawMPInducePartitions(g, this, MPV, this._xMin, this._xMax);

      // affichage des bornes du domaine de l'entree
      if( this.Input != null )
        FISPlot.DrawXInputRange(g, this, this.Input.GetInputInterestRange(), this._xMin, this._xMax);
    } catch( Throwable t ) { MessageKBCT.Error( null, t ); }
  }
}


//------------------------------------------------------------------------------
interface PlotRectangleListener {
  public void PlotRectangle( Rectangle rect );
}


//------------------------------------------------------------------------------
// Classe d'affichage de l'histogramme
class HistogramPanel extends Histogram {
  static final long serialVersionUID=0;	  
  protected int dataset = 1;
  protected int nb_bins = 10;
  private Vector<PlotRectangleListener> listeners= new Vector<PlotRectangleListener>();
  private double [] data_range = new double[2];
  protected double [] adjust_range = new double[2];
//------------------------------------------------------------------------------
  public Dimension getPreferredSize() { return new Dimension(400,200); }
  public Dimension getMinimumSize() { return  new Dimension(100,100); }
//------------------------------------------------------------------------------
  public HistogramPanel() throws Throwable {
    this.setBars(0.9, 0);
    FISPlot.DisableZoom(this);
  }
//------------------------------------------------------------------------------
  public void AdjustXRange() { this.setXRange(this.adjust_range[0], this.adjust_range[1]); }
//------------------------------------------------------------------------------
  public void AddData( double data[] ) {
    this.data_range[0] = this.data_range[1] = data[0];
    for( int i=1 ; i<data.length ; i++ ) {
      this.data_range[0] = Math.min(this.data_range[0], data[i]);
      this.data_range[1] = Math.max(this.data_range[1], data[i]);
    }
    double binwidth = this.nb_bins == 1 ? (this.data_range[1]-this.data_range[0])*2 : (this.data_range[1]-this.data_range[0])/(this.nb_bins-1);  // recalcul du binwidth
    this.setBinWidth(binwidth);
    this.clear(false);
    for( int i=0 ; i<data.length ; i++ )
      if( !Double.isNaN((double)data[i]) ) // le cast en double de data[i] est indispensable pour pour une gestion correcte des valeurs NaN
        this.addPoint(this.dataset, data[i]);

    this.adjust_range[0] = this._xBottom-(binwidth/2);
    this.adjust_range[1] = this._xTop;
    this.AdjustXRange();
    this.repaint();
  }
//------------------------------------------------------------------------------
  public void SetNbBins( int nb_bins ) {
    this.nb_bins = nb_bins;
    if( this._points.size() != 0 ) {
      // si l'histogram contient des points
      double binwidth = this.nb_bins == 1 ? (this.data_range[1]-this.data_range[0])*2 : (this.data_range[1]-this.data_range[0])/(this.nb_bins-1);  // recalcul du binwidth
      this.setBinWidth(binwidth);
      Vector points = (Vector)this._points.elementAt(this.dataset);
      this.clear(false);
      for (int i = 0; i < points.size(); i++)
        this.addPoint(this.dataset, ( (Double) points.elementAt(i)).doubleValue());

      this.adjust_range[0] = this._xBottom-(binwidth/2);
      this.adjust_range[1] = this._xTop;
      this.AdjustXRange();
      this.repaint();
    }
  }
//------------------------------------------------------------------------------
  public void AddPlotRectangleListener( PlotRectangleListener listener ) { this.listeners.add(listener); }
//------------------------------------------------------------------------------
  public void paintComponent(Graphics g) {
    try {
      // ajustement de la taille du rectangle plot au panel
      super.paintComponent(g);
      FISPlot.AdjustPlotRectangleSize(g, this);
      super.paintComponent(g);
      this.setPlotRectangle(null);  // supprime la memorisation du setPlotRectangle
      // envoi de la taille du rectangle aux listeners
      for( int i=0 ; i<this.listeners.size() ; i++ )
        ((PlotRectangleListener)this.listeners.elementAt(i)).PlotRectangle(this.getPlotRectangle());
    } catch( Throwable t ) { MessageKBCT.Error( null, t ); }
  }
}


//------------------------------------------------------------------------------
class HistogramInputPanel extends HistogramPanel {
  static final long serialVersionUID=0;	  
  private JVariable Input;
//------------------------------------------------------------------------------
  public HistogramInputPanel( JVariable input ) throws Throwable {
    super();
    this.SetInput(input);
  }
//------------------------------------------------------------------------------
  public void SetInput( JVariable input ) throws Throwable {
    this.Input = input;
    this.repaint();
  }
//------------------------------------------------------------------------------
  public void paintComponent(Graphics g) {
    try {
      super.paintComponent(g);
      if( this.Input != null )
        FISPlot.DrawXInputRange(g, this, this.Input.GetInputInterestRange(), this._xMin, this._xMax);
    } catch( Throwable t ) {}
  }
}