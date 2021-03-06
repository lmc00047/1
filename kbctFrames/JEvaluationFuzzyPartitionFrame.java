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
//                     JEvaluationFuzzyPartitionFrame.java
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
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import kbct.JEvaluationFuzzyPartition;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbct.jnikbct;
import kbctAux.MessageKBCT;

import org.freehep.util.export.ExportDialog;

import print.JPrintPreview;

/**
 * kbctFrames.JEvaluationFuzzyPartitionFrame displays a table with information
 * about evaluation of fuzzy partitions using different criteria.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
//------------------------------------------------------------------------------
public class JEvaluationFuzzyPartitionFrame extends JChildFrame {
  static final long serialVersionUID=0;	
  private JTable jTable;
  private JScrollPane jPanel= new JScrollPane();
  private DefaultTableModel Model;
  private JTable jTable_current;
  private TitledBorder titledBorderPanel_current;
  private JPanel jPanel_current= new JPanel();
  private DefaultTableModel Model_current;
  private JTable jTable_hfp;
  private TitledBorder titledBorderPanel_hfp;
  private JScrollPane jPanel_hfp= new JScrollPane();
  private DefaultTableModel Model_hfp;
  private JTable jTable_regular;
  private TitledBorder titledBorderPanel_regular;
  private JScrollPane jPanel_regular= new JScrollPane();
  private DefaultTableModel Model_regular;
  private JTable jTable_kmeans;
  private TitledBorder titledBorderPanel_kmeans;
  private JScrollPane jPanel_kmeans= new JScrollPane();
  private DefaultTableModel Model_kmeans;
  private int NbRows;
  private int NbRows_hfp;
  private int NbRows_regular;
  private int NbRows_kmeans;
  private Vector<String> Title;
  private Vector<Vector> Data;
  private Vector<Vector> Data_current;
  private Vector<Vector> Data_hfp;
  private Vector<Vector> Data_regular;
  private Vector<Vector> Data_kmeans;
  private JMenuBar jMenuBarRules= new JMenuBar();
  private JMenuItem jMenuPrint= new JMenuItem();
  private JMenuItem jMenuExport= new JMenuItem();
  private JMenuItem jMenuHelp= new JMenuItem();
  private JMenuItem jMenuClose= new JMenuItem();
  private JEvaluationFuzzyPartition[] EFP;
  private JEvaluationFuzzyPartition[] EFP_current;
  private JEvaluationFuzzyPartition[] EFP_hfp;
  private JEvaluationFuzzyPartition[] EFP_regular;
  private JEvaluationFuzzyPartition[] EFP_kmeans;
  private double[] selOptionHFP= new double[3];
  private double[] selOptionRegular= new double[3];
  private double[] selOptionKmeans= new double[3];
  private double[] selValueHFP= new double[3];
  private double[] selValueRegular= new double[3];
  private double[] selValueKmeans= new double[3];
  private double[] bestOptionsPC= new double[3]; 
  private double[] bestOptionsPE= new double[3]; 
  private double[] bestOptionsChI= new double[3]; 
  private double[] bestOptions= new double[3];
  private double[] bestSuggestedPC= new double[4]; 
  private double[] bestSuggestedPE= new double[4]; 
  private double[] bestSuggestedChI= new double[4]; 
  private double[] bestSuggested= new double[3];
  private String PartitionType;
  private int currentNblabels=2;
//------------------------------------------------------------------------------
  public JEvaluationFuzzyPartitionFrame( JKBCTFrame parent, JEvaluationFuzzyPartition[] efp_curr, JEvaluationFuzzyPartition[] efp, String PartitionType ) {
    super(parent);
    this.EFP_current= efp_curr;
    this.EFP= efp;
    this.PartitionType= PartitionType;
    this.NbRows= this.EFP.length;
    try {
      jbInit();
      JKBCTFrame.AddTranslatable(this);
      this.setSize(780, this.NbRows*28+100);
      this.setLocation(this.ChildPosition(this.getSize()));
      this.setVisible(true);
    } catch( Throwable t ) {
    	t.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en constructor en JEvaluationOfPartitionsFrame 1: "+t);
    }
  }
//------------------------------------------------------------------------------
  public JEvaluationFuzzyPartitionFrame( JKBCTFrame parent, JEvaluationFuzzyPartition[] efp_curr, JEvaluationFuzzyPartition[] efp_hfp, JEvaluationFuzzyPartition[] efp_regular, JEvaluationFuzzyPartition[] efp_kmeans ) {
    super(parent);
    this.EFP_current= efp_curr;
    this.EFP_hfp= efp_hfp;
    this.EFP_regular= efp_regular;
    this.EFP_kmeans= efp_kmeans;
    this.NbRows_hfp= this.EFP_hfp.length;
    this.NbRows_regular= this.EFP_regular.length;
    this.NbRows_kmeans= this.EFP_kmeans.length;
    try {
      jbInit();
      JKBCTFrame.AddTranslatable(this);
      if (this.EFP != null)
          this.setSize(780, this.NbRows*28+100);
      else
          this.setSize(780, this.NbRows_hfp*28+this.NbRows_regular*28+this.NbRows_kmeans*28+50);

      this.setLocation(this.ChildPosition(this.getSize()));
      this.setVisible(true);
    } catch( Throwable t ) {
    	t.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en constructor en JEvaluationOfPartitionsFrame 2: "+t);
    }
  }
//------------------------------------------------------------------------------
  private void jbInit() throws Throwable {
    this.setJMenuBar(this.jMenuBarRules);
    jMenuBarRules.add(jMenuPrint);
    jMenuBarRules.add(jMenuExport);
    jMenuBarRules.add(jMenuHelp);
    jMenuBarRules.add(jMenuClose);
    this.jbInitTables();
    this.Translate();
    this.jMenuPrint.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuPrint_actionPerformed(); }} );
    this.jMenuExport.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExport_actionPerformed(); }} );
    this.jMenuHelp.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuHelp_actionPerformed(); }} );
    this.jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuClose_actionPerformed(); }} );
  }
//------------------------------------------------------------------------------
  private void jbInitTables() throws Throwable {
    DecimalFormat df= new DecimalFormat();
    df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals()+2);//5
    df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals()+2);//5
    Title= new Vector<String>();
    Title.add(LocaleKBCT.GetString("Labels"));
    Title.add(LocaleKBCT.GetString("PartitionCoefficient"));
    Title.add(LocaleKBCT.GetString("PartitionEntropy"));
    Title.add(LocaleKBCT.GetString("ChenIndex"));
    // Current Partition
    Data_current= new Vector<Vector>();
    Data_current.add(Title);
    Vector row_data_curr = new Vector();
    this.currentNblabels = this.EFP_current[0].getNbLabels();
    row_data_curr.add(new Integer(this.currentNblabels));
    double PC_curr = this.EFP_current[0].getPC();
    row_data_curr.add(df.format(new Double(PC_curr)));
    this.bestSuggestedPC[0]= PC_curr;
    double PE_curr = this.EFP_current[0].getPE();
    row_data_curr.add(df.format(new Double(PE_curr)));
    this.bestSuggestedPE[0]= PE_curr;
    double ChI_curr = this.EFP_current[0].getChenIndex();
    row_data_curr.add(df.format(new Double(ChI_curr)));
    this.bestSuggestedChI[0]= ChI_curr;
    Data_current.add(row_data_curr);
    jTable_current= new JTable();
    Model_current= new DefaultTableModel() {
      static final long serialVersionUID=0;	
      public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
      }
      public boolean isCellEditable(int row, int col) {
        return false;
      }
    };
    Model_current.setDataVector(Data_current, Title);
    jTable_current.setModel(Model_current);
    for( int i=0 ; i<this.jTable_current.getColumnCount() ; i++ )
      this.jTable_current.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
   	    static final long serialVersionUID=0;	
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
          super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
          super.setHorizontalAlignment(SwingConstants.CENTER);
          if (row==0) {
              super.setForeground(Color.black);
              super.setBackground(Color.green);
          } else {
              if ( ( (column==1) && (JEvaluationFuzzyPartitionFrame.this.bestSuggested[0]==1)) ||
                      ( (column==2) && (JEvaluationFuzzyPartitionFrame.this.bestSuggested[1]==1)) ||
                      ( (column==3) && (JEvaluationFuzzyPartitionFrame.this.bestSuggested[2]==1))) {
                     super.setForeground(Color.blue);
              } else {
                  super.setForeground(Color.green);
              }
              super.setBackground(Color.white);
          }
          return this;
        }
      });
    TableColumnModel columns = jTable_current.getColumnModel();
    columns.getColumn(0).setPreferredWidth(160+LocaleKBCT.GetString("Labels").length());
    columns.getColumn(1).setPreferredWidth(160+LocaleKBCT.GetString("PartitionCoefficient").length());
    columns.getColumn(2).setPreferredWidth(160+LocaleKBCT.GetString("PartitionEntropy").length());
    columns.getColumn(3).setPreferredWidth(160+LocaleKBCT.GetString("ChenIndex").length());
    jPanel_current.add(jTable_current);
    titledBorderPanel_current= new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    jPanel_current.setBorder(this.titledBorderPanel_current);
    if (this.EFP!=null) {
      Data = new Vector<Vector>();
      for (int row = 0; row < this.NbRows - 1; row++) {
        Vector row_data = new Vector();
        int NbLabels = this.EFP[row].getNbLabels();
        row_data.add(new Integer(NbLabels));
        double PC = this.EFP[row].getPC();
        row_data.add(df.format(new Double(PC)));
        double PE = this.EFP[row].getPE();
        row_data.add(df.format(new Double(PE)));
        double Chen = this.EFP[row].getChenIndex();
        row_data.add(df.format(new Double(Chen)));
        Data.add(row_data);
      }
      jTable= new JTable();
      Model= new DefaultTableModel() {
   	    static final long serialVersionUID=0;	
        public Class getColumnClass(int c) {
          return getValueAt(0, c).getClass();
        }
        public boolean isCellEditable(int row, int col) {
          return false;
        }
      };
      Model.setDataVector(Data, Title);
      jTable.setModel(Model);
      for( int i=0 ; i<this.jTable.getColumnCount() ; i++ )
        this.jTable.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
          static final long serialVersionUID=0;	
          public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            super.setHorizontalAlignment(SwingConstants.CENTER);
            return this;
          }
        });
      jPanel.getViewport().add(jTable);
      this.getContentPane().setLayout(new GridBagLayout());
      this.getContentPane().add(jPanel_current, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
          , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
      this.getContentPane().add(jPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
          , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
    } else {
      // HFP Partition
      Data_hfp= new Vector<Vector>();
      int maxLimAux= Math.max(this.NbRows_regular, this.NbRows_hfp);
      int maxLim= Math.max(maxLimAux, this.NbRows_kmeans);
      double[] valuesPC= new double[maxLim-1];
      double[] valuesPE= new double[maxLim-1];
      double[] valuesChI= new double[maxLim-1];
      for (int row = 0; row < this.NbRows_hfp - 1; row++) {
        Vector row_data = new Vector();
        if ( (this.EFP_hfp!=null) && (this.EFP_hfp[row]!=null) ) {
          int NbLabels = this.EFP_hfp[row].getNbLabels();
          row_data.add(new Integer(NbLabels));
          double PC = this.EFP_hfp[row].getPC();
          row_data.add(df.format(new Double(PC)));
          valuesPC[row]= PC;
          double PE = this.EFP_hfp[row].getPE();
          row_data.add(df.format(new Double(PE)));
          valuesPE[row]= PE;
          double ChI = this.EFP_hfp[row].getChenIndex();
          row_data.add(df.format(new Double(ChI)));
          valuesChI[row]= ChI;
          Data_hfp.add(row_data);
          if (row==this.currentNblabels-2) {
            this.bestSuggestedPC[1]= PC;
            this.bestSuggestedPE[1]= PE;
            this.bestSuggestedChI[1]= ChI;
          }
        }
      }
      double[] maxPC= jnikbct.findMax(valuesPC);
      this.selOptionHFP[0]=maxPC[0]-1;
      this.selValueHFP[0]=maxPC[1];
      this.bestOptionsPC[0]= maxPC[1];
      double[] minPE= jnikbct.findMin(valuesPE);
      this.selOptionHFP[1]=minPE[0]-1;
      this.selValueHFP[1]=minPE[1];
      this.bestOptionsPE[0]= minPE[1];
      double[] maxChI= jnikbct.findMax(valuesChI);
      this.selOptionHFP[2]=maxChI[0]-1;
      this.selValueHFP[2]=maxChI[1];
      this.bestOptionsChI[0]= maxChI[1];
      jTable_hfp= new JTable();
      Model_hfp= new DefaultTableModel() {
   	    static final long serialVersionUID=0;	
        public Class getColumnClass(int c) {
          return getValueAt(0, c).getClass();
        }
        public boolean isCellEditable(int row, int col) {
          return false;
        }
      };
      Model_hfp.setDataVector(Data_hfp, Title);
      jTable_hfp.setModel(Model_hfp);
      for( int i=0 ; i<this.jTable_hfp.getColumnCount() ; i++ )
        this.jTable_hfp.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
          static final long serialVersionUID=0;	
          public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            super.setHorizontalAlignment(SwingConstants.CENTER);
            if (row==JEvaluationFuzzyPartitionFrame.this.currentNblabels-2)
               super.setForeground(Color.green);
            else
               super.setForeground(Color.black);

            if ( (row==JEvaluationFuzzyPartitionFrame.this.currentNblabels-2) && 
            		( ( (column==1) && (JEvaluationFuzzyPartitionFrame.this.bestSuggested[0]==2)) ||
                      ( (column==2) && (JEvaluationFuzzyPartitionFrame.this.bestSuggested[1]==2)) ||
                      ( (column==3) && (JEvaluationFuzzyPartitionFrame.this.bestSuggested[2]==2))) )
                   super.setForeground(Color.blue);

            if ((column>0) && (JEvaluationFuzzyPartitionFrame.this.selOptionHFP[column-1]==row)) {
                super.setBackground(Color.yellow);
                if ( ( (column==1) && (JEvaluationFuzzyPartitionFrame.this.bestOptions[0]==1)) ||
                     ( (column==2) && (JEvaluationFuzzyPartitionFrame.this.bestOptions[1]==1)) ||
                     ( (column==3) && (JEvaluationFuzzyPartitionFrame.this.bestOptions[2]==1)))
                    super.setForeground(Color.red);
                	
        	} else		
                super.setBackground(Color.white);

            return this;
          }
        });
      jPanel_hfp.getViewport().add(jTable_hfp);
      titledBorderPanel_hfp= new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
      jPanel_hfp.setBorder(this.titledBorderPanel_hfp);
      // Regular Partition
      Data_regular= new Vector<Vector>();
      for (int row = 0; row < this.NbRows_regular - 1; row++) {
    	//System.out.println("JEvaluationFuzzyPartitionFrame: row="+row);
        Vector row_data = new Vector();
        if ( (this.EFP_regular!=null) && (this.EFP_regular[row]!=null) ) {
          int NbLabels = this.EFP_regular[row].getNbLabels();
      	  //System.out.println("JEvaluationFuzzyPartitionFrame: NbLabels="+NbLabels);
          row_data.add(new Integer(NbLabels));
          double PC = this.EFP_regular[row].getPC();
          row_data.add(df.format(new Double(PC)));
          valuesPC[row]= PC;
          double PE = this.EFP_regular[row].getPE();
          row_data.add(df.format(new Double(PE)));
          valuesPE[row]= PE;
          double ChI = this.EFP_regular[row].getChenIndex();
          row_data.add(df.format(new Double(ChI)));
          valuesChI[row]= ChI;
          Data_regular.add(row_data);
          if (row==this.currentNblabels-2) {
            this.bestSuggestedPC[2]= PC;
            this.bestSuggestedPE[2]= PE;
            this.bestSuggestedChI[2]= ChI;
          }
        }
      }
      maxPC= jnikbct.findMax(valuesPC);
      this.selOptionRegular[0]=maxPC[0]-1;
      this.selValueRegular[0]=maxPC[1];
      this.bestOptionsPC[1]= maxPC[1];
      minPE= jnikbct.findMin(valuesPE);
      this.selOptionRegular[1]=minPE[0]-1;
      this.selValueRegular[1]=minPE[1];
      this.bestOptionsPE[1]= minPE[1];
      maxChI= jnikbct.findMax(valuesChI);
      this.selOptionRegular[2]=maxChI[0]-1;
      this.selValueRegular[2]=maxChI[1];
      this.bestOptionsChI[1]= maxChI[1];
      jTable_regular= new JTable();
      Model_regular= new DefaultTableModel() {
   	    static final long serialVersionUID=0;	
        public Class getColumnClass(int c) {
          return getValueAt(0, c).getClass();
        }
        public boolean isCellEditable(int row, int col) {
          return false;
        }
      };
      Model_regular.setDataVector(Data_regular, Title);
      jTable_regular.setModel(Model_regular);
      for( int i=0 ; i<this.jTable_regular.getColumnCount() ; i++ )
        this.jTable_regular.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
          static final long serialVersionUID=0;	
          public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            super.setHorizontalAlignment(SwingConstants.CENTER);
            if (row==JEvaluationFuzzyPartitionFrame.this.currentNblabels-2)
                super.setForeground(Color.green);
            else
                super.setForeground(Color.black);

            if ( (row==JEvaluationFuzzyPartitionFrame.this.currentNblabels-2) && 
            		( ( (column==1) && (JEvaluationFuzzyPartitionFrame.this.bestSuggested[0]==3)) ||
                      ( (column==2) && (JEvaluationFuzzyPartitionFrame.this.bestSuggested[1]==3)) ||
                      ( (column==3) && (JEvaluationFuzzyPartitionFrame.this.bestSuggested[2]==3))) )
                   super.setForeground(Color.blue);

            if ((column>0) && (JEvaluationFuzzyPartitionFrame.this.selOptionRegular[column-1]==row)) {
                super.setBackground(Color.yellow);
                if ( ( (column==1) && (JEvaluationFuzzyPartitionFrame.this.bestOptions[0]==2)) ||
                        ( (column==2) && (JEvaluationFuzzyPartitionFrame.this.bestOptions[1]==2)) ||
                        ( (column==3) && (JEvaluationFuzzyPartitionFrame.this.bestOptions[2]==2)))
                       super.setForeground(Color.red);
                   	
           	} else		
                super.setBackground(Color.white);

            return this;
          }
        });
      jPanel_regular.getViewport().add(jTable_regular);
      titledBorderPanel_regular= new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
      jPanel_regular.setBorder(this.titledBorderPanel_regular);
      // Kmeans Partition
      Data_kmeans= new Vector<Vector>();
      for (int row = 0; row < this.NbRows_kmeans - 1; row++) {
        Vector row_data = new Vector();
        if ( (this.EFP_kmeans!=null) && (this.EFP_kmeans[row]!=null) ) {
          int NbLabels = this.EFP_kmeans[row].getNbLabels();
          row_data.add(new Integer(NbLabels));
          double PC = this.EFP_kmeans[row].getPC();
          row_data.add(df.format(new Double(PC)));
          valuesPC[row]= PC;
          double PE = this.EFP_kmeans[row].getPE();
          row_data.add(df.format(new Double(PE)));
          valuesPE[row]= PE;
          double ChI = this.EFP_kmeans[row].getChenIndex();
          row_data.add(df.format(new Double(ChI)));
          valuesChI[row]= ChI;
          Data_kmeans.add(row_data);
          if (row==this.currentNblabels-2) {
            this.bestSuggestedPC[3]= PC;
            this.bestSuggestedPE[3]= PE;
            this.bestSuggestedChI[3]= ChI;
          }
        }
      }
      maxPC= jnikbct.findMax(valuesPC);
      this.selOptionKmeans[0]=maxPC[0]-1;
      this.selValueKmeans[0]=maxPC[1];
      this.bestOptionsPC[2]= maxPC[1];
      minPE= jnikbct.findMin(valuesPE);
      this.selOptionKmeans[1]=minPE[0]-1;
      this.selValueKmeans[1]=minPE[1];
      this.bestOptionsPE[2]= minPE[1];
      maxChI= jnikbct.findMax(valuesChI);
      this.selOptionKmeans[2]=maxChI[0]-1;
      this.selValueKmeans[2]=maxChI[1];
      this.bestOptionsChI[2]= maxChI[1];
      this.bestOptions[0]= jnikbct.findMax(this.bestOptionsPC)[0];
      this.bestOptions[1]= jnikbct.findMin(this.bestOptionsPE)[0];
      this.bestOptions[2]= jnikbct.findMax(this.bestOptionsChI)[0];
      this.bestSuggested[0]= jnikbct.findMax(this.bestSuggestedPC)[0];
      this.bestSuggested[1]= jnikbct.findMin(this.bestSuggestedPE)[0];
      this.bestSuggested[2]= jnikbct.findMax(this.bestSuggestedChI)[0];
      jTable_kmeans= new JTable();
      Model_kmeans= new DefaultTableModel() {
   	    static final long serialVersionUID=0;	
        public Class getColumnClass(int c) {
          return getValueAt(0, c).getClass();
        }
        public boolean isCellEditable(int row, int col) {
          return false;
        }
      };
      Model_kmeans.setDataVector(Data_kmeans, Title);
      jTable_kmeans.setModel(Model_kmeans);
      for( int i=0 ; i<this.jTable_kmeans.getColumnCount() ; i++ )
        this.jTable_kmeans.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
          static final long serialVersionUID=0;	
          public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            super.setHorizontalAlignment(SwingConstants.CENTER);
            if (row==JEvaluationFuzzyPartitionFrame.this.currentNblabels-2)
                super.setForeground(Color.green);
            else
                super.setForeground(Color.black);

            if ( (row==JEvaluationFuzzyPartitionFrame.this.currentNblabels-2) && 
            		( ( (column==1) && (JEvaluationFuzzyPartitionFrame.this.bestSuggested[0]==4)) ||
                      ( (column==2) && (JEvaluationFuzzyPartitionFrame.this.bestSuggested[1]==4)) ||
                      ( (column==3) && (JEvaluationFuzzyPartitionFrame.this.bestSuggested[2]==4))) )
                   super.setForeground(Color.blue);

            if ((column>0) && (JEvaluationFuzzyPartitionFrame.this.selOptionKmeans[column-1]==row)) {
                super.setBackground(Color.yellow);
                if ( ( (column==1) && (JEvaluationFuzzyPartitionFrame.this.bestOptions[0]==3)) ||
                        ( (column==2) && (JEvaluationFuzzyPartitionFrame.this.bestOptions[1]==3)) ||
                        ( (column==3) && (JEvaluationFuzzyPartitionFrame.this.bestOptions[2]==3)))
                       super.setForeground(Color.red);
                   	
           	} else		
                super.setBackground(Color.white);

            return this;
          }
        });
      jPanel_kmeans.getViewport().add(jTable_kmeans);
      titledBorderPanel_kmeans= new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
      jPanel_kmeans.setBorder(this.titledBorderPanel_kmeans);
      // Main Panel
      this.getContentPane().setLayout(new GridBagLayout());
      this.getContentPane().add(jPanel_current, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
          , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      this.getContentPane().add(jPanel_hfp, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
          , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      this.getContentPane().add(jPanel_regular, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
          , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      this.getContentPane().add(jPanel_kmeans, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0
          , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    }
  }
//------------------------------------------------------------------------------
  public void Translate() throws Throwable {
    jMenuPrint.setText(LocaleKBCT.GetString("Print"));
    jMenuExport.setText(LocaleKBCT.GetString("Export"));
    jMenuHelp.setText(LocaleKBCT.GetString("Help"));
    jMenuClose.setText(LocaleKBCT.GetString("Close"));
    if (this.EFP!= null)
        this.setTitle(this.PartitionType+": "+LocaleKBCT.GetString("EvaluationOfFuzzyPartitions"));
    else {
      this.setTitle(LocaleKBCT.GetString("EvaluationOfFuzzyPartitions"));
      titledBorderPanel_current.setTitle(LocaleKBCT.GetString("ExpertPartition"));
      titledBorderPanel_hfp.setTitle(LocaleKBCT.GetString("PartitionHFP"));
      titledBorderPanel_regular.setTitle(LocaleKBCT.GetString("PartitionRegular"));
      titledBorderPanel_kmeans.setTitle(LocaleKBCT.GetString("PartitionKmeans"));
    }
  }
//------------------------------------------------------------------------------
  void jMenuClose_actionPerformed() { this.dispose(); }
//------------------------------------------------------------------------------
  public void dispose() { super.dispose(); }
//------------------------------------------------------------------------------
  void jMenuPrint_actionPerformed() {
    try {
      Printable p= new Printable() {
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
          if (pageIndex >= 1)
            return Printable.NO_SUCH_PAGE;
          else
            return JEvaluationFuzzyPartitionFrame.this.print(graphics, pageFormat);
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
            JEvaluationFuzzyPartitionFrame.this.getContentPane().paint(g);
            g.translate(0, JEvaluationFuzzyPartitionFrame.this.getContentPane().getHeight());
            JEvaluationFuzzyPartitionFrame.this.getContentPane().paint(g);
            g.setColor(Color.gray);
            g.drawLine(0,0,0, JEvaluationFuzzyPartitionFrame.this.getContentPane().getHeight()-1);
        }
      };
      panel.setSize(new Dimension(this.getContentPane().getWidth(), this.getContentPane().getHeight()));
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
       scalew =  pageWidth / getWidth();

     if(  getHeight() >= pageHeight)
       scaleh =  pageWidth / getHeight();

     g2.translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
     g2.scale( scalew, scaleh );
     this.getContentPane().print( g2 );

     return Printable.PAGE_EXISTS;
 }
//------------------------------------------------------------------------------
  void jMenuHelp_actionPerformed() { 
        MainKBCT.setJB(new JBeginnerFrame("main/MainMenuData.html#Evaluation"));
		MainKBCT.getJB().setVisible(true);
		SwingUtilities.updateComponentTreeUI(this);
  }
}