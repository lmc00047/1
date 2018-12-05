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
//                              JViewVertexFrame.java
//
//
//**********************************************************************

// Contains: JViewVertexFrame, VertexDataHistogramFrame

package kbctFrames;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
//import java.io.BufferedReader;
import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Enumeration;
//import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.TitledBorder;

import kbct.JEvaluationFuzzyPartition;
import kbct.JFISHFP;
import kbct.JInputHFP;
import kbct.JKBCT;
import kbct.JVariable;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbct.jnikbct;
import kbctAux.IntegerField;
import kbctAux.JDataFile;
import kbctAux.JOpenFileChooser;
import kbctAux.MessageKBCT;
import KB.LabelKBCT;

import fis.JExtendedDataFile;
import fis.JFileListener;

/**
 * Classes used in order to generate the window with information about
 * induction of partitions.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
//------------------------------------------------------------------------------
public class JViewVertexFrame extends JDialog {
  static final long serialVersionUID=0;	
  private JMainFrame Parent;
  private JExtendedDataFile DataFile;
  private String VariableName;
  private int VariableNumber;
  private int NbMF;
  private int NbInputs= -1;
  private Vector MPV;
  private String Vertex_hfp_File;
  private String HFP_hfp_File;
  private String Vertex_regular_File;
  private String HFP_regular_File;
  private String Vertex_kmeans_File;
  private String HFP_kmeans_File;
  private JFISHFP hfp_hfp = null;
  private JVertexFile jvf_hfp = null;
  private JFISHFP hfp_regular = null;
  private JVertexFile jvf_regular = null;
  private JFISHFP hfp_kmeans = null;
  private JVertexFile jvf_kmeans = null;
  private JKBCT kbct;
  private JVariable jv_expert;
  private JVariable jv_data;
  //private String[] VariablesNames;
  private boolean automatic= false;
  private boolean PartitionInduction= false;
  private boolean PartitionSelection= false;
//------------------------------------------------------------------------------
  public JViewVertexFrame( JMainFrame parent, String vertex_hfp_file, String hfp_hfp_file, String vertex_regular_file, String hfp_regular_file, String vertex_kmeans_file, String hfp_kmeans_file, JExtendedDataFile data_file, String VariableName, int VariableNumber, int NbMF, Vector MPV, JKBCT kbct, JVariable jve, JVariable jvd, boolean autom, boolean PI, boolean PS ) {
	this.Parent = parent;
    this.Vertex_hfp_File = vertex_hfp_file;
    this.HFP_hfp_File = hfp_hfp_file;
    this.Vertex_regular_File = vertex_regular_file;
    this.HFP_regular_File = hfp_regular_file;
    this.Vertex_kmeans_File = vertex_kmeans_file;
    this.HFP_kmeans_File = hfp_kmeans_file;
    this.DataFile = data_file;
    this.VariableName= VariableName;
    this.VariableNumber= VariableNumber;
    this.NbMF= NbMF;
    this.MPV= MPV;
    this.kbct= kbct;
    this.jv_expert= jve;
    this.jv_data= jvd;
    //this.VariablesNames= VariablesNames;
    this.automatic= autom;
    this.PartitionInduction= PI;
    this.PartitionSelection= PS;
    try {
    	if (this.HFP_hfp_File != null) {
    		this.hfp_hfp = new JFISHFP(data_file.FileName(), this.HFP_hfp_File);
            if (this.hfp_hfp!=null)
                this.NbInputs= this.hfp_hfp.GetNbInput();
    	}
      //if (hfp_hfp==null)
    	//  System.out.println("HFP null");
      
    	if (this.HFP_regular_File != null) {
    		this.hfp_regular = new JFISHFP(data_file.FileName(), this.HFP_regular_File);
            if ( (this.NbInputs==-1) && (this.hfp_regular != null) )
                this.NbInputs= this.hfp_regular.GetNbInput();
    	}
      //if (hfp_regular==null)
    	//  System.out.println("REG null");

    	if (this.HFP_kmeans_File != null) {
    		this.hfp_kmeans = new JFISHFP(data_file.FileName(), this.HFP_kmeans_File);
            if ( (this.NbInputs==-1) && (this.hfp_kmeans != null) )
                this.NbInputs= this.hfp_kmeans.GetNbInput();
    	}
      //if (hfp_kmeans==null)
    	//  System.out.println("KM null");
    } catch (Throwable t) {
    	t.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in constructor of JViewVertexFrame 1: "+t);
    }
    try {
    	if (this.Vertex_hfp_File != null)
            jvf_hfp= new JVertexFile(this.Vertex_hfp_File);
      //if (jvf_hfp==null)
    	//  System.out.println("HFP V null");

    	if (this.Vertex_regular_File != null)
            jvf_regular= new JVertexFile(this.Vertex_regular_File);
      //if (jvf_regular==null)
    	//  System.out.println("REG V null");

    	if (this.Vertex_kmeans_File != null)
            jvf_kmeans= new JVertexFile(this.Vertex_kmeans_File);
      //if (jvf_kmeans==null)
    	//  System.out.println("KM V null");
    } catch (Exception e) {
    	e.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in constructor of JViewVertexFrame 2: "+e);
    }
    try { jbInit(); }
    catch(Throwable t) {
    	t.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in constructor of JViewVertexFrame 3: "+t);
    }
  }
//------------------------------------------------------------------------------
  private void jbInit() throws Throwable {
    JInputHFP inh=null;
    if (this.hfp_hfp!=null)
    	inh= this.hfp_hfp.GetInput(VariableNumber);

    Vector vh= null;
    if (this.jvf_hfp != null) 
    	vh= this.jvf_hfp.Vertices(VariableNumber);

    JInputHFP inr=null;
    if (this.hfp_regular!=null)
    	inr= this.hfp_regular.GetInput(VariableNumber);

    Vector vr= null;
    if (this.jvf_regular != null) 
    	vr= this.jvf_regular.Vertices(VariableNumber);

    JInputHFP ink=null;
    if (this.hfp_kmeans!=null)
    	ink= this.hfp_kmeans.GetInput(VariableNumber);

    Vector vk= null;
    if (this.jvf_kmeans != null) 
    	vk= this.jvf_kmeans.Vertices(VariableNumber);
    
    // 5 -> Number of MF. Será variable según el número de etiquetas que defina el usuario (por defecto 3)
    if (this.VariableNumber < this.NbInputs) {
      new VertexDataHistogramFrame(this.Parent, VariableNumber, this.DataFile,
                                   inh, vh, inr, vr, ink, vk,
                                   this.NbMF, this.MPV, this.kbct, this.jv_expert, this.jv_data, this.VariableName, this.automatic, this.PartitionInduction, this.PartitionSelection);
    } /*else {
      new VertexDataHistogramFrame(this.Parent, VariableNumber, this.DataFile,
                                   this.hfp_hfp.GetInput(VariableNumber),
                                   this.jvf_hfp.Vertices(VariableNumber),
                                   this.hfp_regular.GetInput(VariableNumber),
                                   this.jvf_regular.Vertices(VariableNumber),
                                   this.hfp_kmeans.GetInput(VariableNumber),
                                   this.jvf_kmeans.Vertices(VariableNumber),
                                   this.NbMF, this.MPV, this.kbct, this.jv_expert, this.jv_data, this.VariableName, this.automatic, this.PartitionInduction, this.PartitionSelection);
    }*/
  }
}


//------------------------------------------------------------------------------
class VertexDataHistogramFrame extends DataHistogramFrame {
  static final long serialVersionUID=0;	
  private JLabel jLabelNumberOfMF_hfp = new JLabel();
  private IntegerField jIFNumberOfMF_hfp = new IntegerField();
  private JButton jButtonApplyNumberOfMF_hfp = new JButton();
  private JButton jButtonEvaluation_hfp = new JButton();
  private JLabel jLabelNumberOfMF_regular = new JLabel();
  private IntegerField jIFNumberOfMF_regular = new IntegerField();
  private JButton jButtonApplyNumberOfMF_regular = new JButton();
  private JButton jButtonEvaluation_regular = new JButton();
  private JLabel jLabelNumberOfMF_kmeans = new JLabel();
  private IntegerField jIFNumberOfMF_kmeans = new IntegerField();
  private JButton jButtonApplyNumberOfMF_kmeans = new JButton();
  private JButton jButtonEvaluation_kmeans = new JButton();
  private JInputHFP hfp_input;
  private Vector hfp_vertices;
  private JInputHFP regular_input;
  private Vector regular_vertices;
  private JInputHFP kmeans_input;
  private Vector kmeans_vertices;
  private Vector InputPanels_hfp = new Vector();
  private Vector InputPanels_regular = new Vector();
  private Vector InputPanels_kmeans = new Vector();
  private Vector MPV;
  private JPanel jPanelAllPartitions= new JPanel(new GridBagLayout());
  private JPanel jPanel_hfp = new JPanel(new GridBagLayout());
  private JPanel jPanel_regular = new JPanel(new GridBagLayout());
  private JPanel jPanel_kmeans = new JPanel(new GridBagLayout());
  private JScrollPane jsp_hfp= new JScrollPane();
  private JScrollPane jsp_regular= new JScrollPane();
  private JScrollPane jsp_kmeans= new JScrollPane();
  private ScrollPaneLayout gridSBL_hfp = new ScrollPaneLayout();
  private ScrollPaneLayout gridSBL_regular = new ScrollPaneLayout();
  private ScrollPaneLayout gridSBL_kmeans = new ScrollPaneLayout();
  private TitledBorder titledBorderAllPartitions;
  private TitledBorder titledBorderPartitionHFP;
  private TitledBorder titledBorderPartitionRegular;
  private TitledBorder titledBorderPartitionKmeans;
  private JFrame jTestFrame= null;
  private JButton jButtonTestFile = new JButton();
  private JTextField jTFTestFile = new JTextField();
  private JButton jbCompute = new JButton();
  private double[] d;
  private boolean ShowEvaluationAll= false;
  private boolean ShowEvaluationHFP= false;
  private boolean ShowEvaluationRegular= false;
  private boolean ShowEvaluationKmeans= false;
  private JExtendedDataFile DataFile;
  private JFileListener JFileListener;
//------------------------------------------------------------------------------
  public VertexDataHistogramFrame( JMainFrame parent, int index, JExtendedDataFile data_file, JInputHFP hfp_input, Vector hfp_vertices, JInputHFP regular_input, Vector regular_vertices, JInputHFP kmeans_input, Vector kmeans_vertices, int number_of_mf, Vector modal_points, JKBCT kbct, JVariable jve, JVariable jvd, String VariableName, boolean automatic, boolean PI, boolean PS ) throws Throwable {
    super(parent);
    this.hfp_input = hfp_input;
    this.hfp_vertices = hfp_vertices;
	//System.out.println("JViewVertexFrame: constructor: 1a: this.hfp_vertices.size() -> "+this.hfp_vertices.size());
    this.regular_input = regular_input;
    this.regular_vertices = regular_vertices;
    this.kmeans_input = kmeans_input;
    this.kmeans_vertices = kmeans_vertices;
    this.MPV= modal_points;
    Dimension dim = getToolkit().getScreenSize();
    this.x_size= dim.width;
    this.y_size= dim.height;
    //System.out.println("JViewVertexFrame: expert nbmfs: "+jve.GetLabelsNumber());
    String suggestedPart= this.suggestTheBestPartition(index, data_file, jve, automatic, PI, PS);
    //System.out.println("JViewVertexFrame: suggestion: "+suggestedPart);
    //if (sugestedPart.equals("-1"))
    //	sugestedPart="regular";
    
    if (VariableName != null)
      super.jbInitInducePartitions( VariableName, index, data_file, kbct, jve, jvd, suggestedPart);
    else
      super.jbInitInducePartitions( hfp_input.GetName(), index, data_file, kbct, jve, jvd, suggestedPart);

    this.DataFile= data_file;
    this.JFileListener = new JFileListener() {
        public void Closed() { VertexDataHistogramFrame.this.dispose(); }
        public void ReLoaded() { VertexDataHistogramFrame.this.DataReLoaded(); }
     };
     this.DataFile.AddJFileListener(this.JFileListener);

     this.titledBorderAllPartitions = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    this.jPanelAllPartitions.setBorder(this.titledBorderAllPartitions);
    // HFP Partition Panel
    this.titledBorderPartitionHFP = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    this.jPanel_hfp.setBorder(this.titledBorderPartitionHFP);
    JPanel jPanelMumberOfMF_hfp = new JPanel(new GridBagLayout());
    jPanelMumberOfMF_hfp.add(this.jLabelNumberOfMF_hfp,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 0), 0, 0));
    this.jIFNumberOfMF_hfp.setValue(number_of_mf);
    jPanelMumberOfMF_hfp.add(this.jIFNumberOfMF_hfp,  new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 40, 0));
    jPanelMumberOfMF_hfp.add(this.jButtonApplyNumberOfMF_hfp,  new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
    this.jPanel_hfp.add(jPanelMumberOfMF_hfp,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.jsp_hfp.setLayout(this.gridSBL_hfp);
    if (this.hfp_vertices!=null) {
    	//System.out.println("JViewVertexFrame: constructor: number_of_mf -> "+number_of_mf);
    	//System.out.println("JViewVertexFrame: constructor: 1b: this.hfp_vertices.size() -> "+this.hfp_vertices.size());
        if( number_of_mf > this.hfp_vertices.size() + 1 )
           this.ShowVertices_hfp(this.hfp_vertices.size()+1);
        else
           this.ShowVertices_hfp(number_of_mf);
    } //else 
    	//System.out.println("JViewVertexFrame: this.hfp_vertices -> NULL");
    
    this.jButtonApplyNumberOfMF_hfp.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
      try {
       ShowVertices_hfp(jIFNumberOfMF_hfp.getValue());
       repaint();
      } catch(Throwable t) { 
    	  t.printStackTrace();
    	  MessageKBCT.Error(null, t);
      }
      } });
    this.jPanel_hfp.add(this.jsp_hfp,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.jButtonEvaluation_hfp.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
       VertexDataHistogramFrame.this.jButtonEvaluationHFP_actionPerformed();
    } });
    this.jPanel_hfp.add(this.jButtonEvaluation_hfp,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.jPanelAllPartitions.add(this.jPanel_hfp,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    // Regular Partition Panel
    this.titledBorderPartitionRegular = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    this.jPanel_regular.setBorder(this.titledBorderPartitionRegular);
    JPanel jPanelMumberOfMF_regular = new JPanel(new GridBagLayout());
    jPanelMumberOfMF_regular.add(this.jLabelNumberOfMF_regular,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 0), 0, 0));
    this.jIFNumberOfMF_regular.setValue(number_of_mf);
    jPanelMumberOfMF_regular.add(this.jIFNumberOfMF_regular,  new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 40, 0));
    jPanelMumberOfMF_regular.add(this.jButtonApplyNumberOfMF_regular,  new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
    this.jPanel_regular.add(jPanelMumberOfMF_regular,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    if (this.regular_vertices!=null) {
        this.jsp_regular.setLayout(this.gridSBL_regular);
        //System.out.println("JViewVertexFrame: nomf: "+number_of_mf);
        //System.out.println("JViewVertexFrame: verticessize: "+this.regular_vertices.size());
        if( number_of_mf > this.regular_vertices.size() + 1 )
           this.ShowVertices_regular(this.regular_vertices.size()+1);
        else
           this.ShowVertices_regular(number_of_mf);
    }
    this.jButtonApplyNumberOfMF_regular.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
          try {
             ShowVertices_regular(jIFNumberOfMF_regular.getValue());
             repaint();
          } catch(Throwable t) {
        	  t.printStackTrace();
        	  MessageKBCT.Error(null, t);
          }
          } });
    this.jPanel_regular.add(this.jsp_regular,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.jButtonEvaluation_regular.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
        VertexDataHistogramFrame.this.jButtonEvaluationRegular_actionPerformed();
    } });
    this.jPanel_regular.add(this.jButtonEvaluation_regular,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.jPanelAllPartitions.add(this.jPanel_regular,  new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    // Kmeans Partition Panel
    this.titledBorderPartitionKmeans = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    this.jPanel_kmeans.setBorder(this.titledBorderPartitionKmeans);
    JPanel jPanelMumberOfMF_kmeans = new JPanel(new GridBagLayout());
    jPanelMumberOfMF_kmeans.add(this.jLabelNumberOfMF_kmeans,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 0), 0, 0));
    this.jIFNumberOfMF_kmeans.setValue(number_of_mf);
    jPanelMumberOfMF_kmeans.add(this.jIFNumberOfMF_kmeans,  new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 40, 0));
    jPanelMumberOfMF_kmeans.add(this.jButtonApplyNumberOfMF_kmeans,  new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
    this.jPanel_kmeans.add(jPanelMumberOfMF_kmeans,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.jsp_kmeans.setLayout(this.gridSBL_kmeans);
    if (this.kmeans_vertices!=null) {
        if( number_of_mf > this.kmeans_vertices.size() + 1 )
           this.ShowVertices_kmeans(this.kmeans_vertices.size()+1);
        else
           this.ShowVertices_kmeans(number_of_mf);
    }
    this.jButtonApplyNumberOfMF_kmeans.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
      try {
       ShowVertices_kmeans(jIFNumberOfMF_kmeans.getValue());
       repaint();
      }
      catch(Throwable t) {
    	  t.printStackTrace();
    	  MessageKBCT.Error(null, t);
      }
    } });
    this.jPanel_kmeans.add(this.jsp_kmeans,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.jButtonEvaluation_kmeans.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
        VertexDataHistogramFrame.this.jButtonEvaluationKmeans_actionPerformed();
     } });
    this.jPanel_kmeans.add(this.jButtonEvaluation_kmeans,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.jPanelAllPartitions.add(this.jPanel_kmeans,  new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.jHistogramFramePanel.add(this.jPanelAllPartitions,  new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
    this.jMenuSave.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuSave_actionPerformed(false, false, false); } });
    this.jbSPApply.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
        VertexDataHistogramFrame.this.jbSPApply_actionPerformed(false, false, false);
     } });
    this.jMenuEvaluation.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuEvaluation_actionPerformed(); } });
    if (!automatic) {
        this.Show();
    } else {
        this.jMenuSave_actionPerformed(automatic, PI, PS);
        this.jMenuClose_actionPerformed();
    }
  }
//------------------------------------------------------------------------------
  private void ShowVertices_hfp( int number_of_mf ) throws Throwable {
  	//System.out.println("JViewVertexFrame: ShowVertices_hfp: number_of_mf -> "+number_of_mf);
	if (this.hfp_vertices!=null) {
        for( int i=0 ; i<this.InputPanels_hfp.size() ; i++ )
            this.jsp_hfp.remove((Component)this.InputPanels_hfp.elementAt(i));

        this.InputPanels_hfp = new Vector();
    	//System.out.println("JViewVertexFrame: this.hfp_vertices.size() -> "+this.hfp_vertices.size());
        if( this.hfp_vertices.size() < 1 )
            return;

        if( number_of_mf > this.hfp_vertices.size() + 1 ) {
            number_of_mf = this.hfp_vertices.size()+1;
            MessageKBCT.Information(this, LocaleKBCT.GetString("Maximum_Number_Of_MF_is")+" "+number_of_mf);
        }
        if( number_of_mf < 2 )
            number_of_mf = 2;

        this.jIFNumberOfMF_hfp.setValue(number_of_mf);
        JPanel jp= new JPanel(new GridBagLayout());
        for( int i=0 ; i<number_of_mf-1 ; i++ ) {
        	//System.out.println("JViewVertexFrame: number_of_mf:"+number_of_mf+"  i="+i);
            double [] range = this.hfp_input.GetInputInterestRange();
        	if (i < this.hfp_vertices.size()) {
                Vector vdata = (Vector)this.hfp_vertices.elementAt(i);
                double [] data = new double[vdata.size()];
                for( int j=0 ; j<vdata.size() ; j++ )
                     data[j]= ((Double)vdata.elementAt(j)).doubleValue();

                JVariable new_input = new JVariable(i+1, data, range[0], range[1], "");
                JPanelInput jpi = new JPanelInput(new_input);
                jpi.SetMPV(MPV);
                jpi.addMouseListener(new MouseListener() {
                  public void mousePressed(MouseEvent e) {
                   VertexDataHistogramFrame.this.jsp_hfp.repaint();
                   VertexDataHistogramFrame.this.jsp_regular.repaint();
                   VertexDataHistogramFrame.this.jsp_kmeans.repaint();
                  }
                  public void mouseReleased(MouseEvent e) {
                   VertexDataHistogramFrame.this.jsp_hfp.repaint();
                   VertexDataHistogramFrame.this.jsp_regular.repaint();
                   VertexDataHistogramFrame.this.jsp_kmeans.repaint();
                  }
                  public void mouseEntered(MouseEvent e) {
                   VertexDataHistogramFrame.this.jsp_hfp.repaint();
                   VertexDataHistogramFrame.this.jsp_regular.repaint();
                   VertexDataHistogramFrame.this.jsp_kmeans.repaint();
                  }
                  public void mouseExited(MouseEvent e) {
                   VertexDataHistogramFrame.this.jsp_hfp.repaint();
                   VertexDataHistogramFrame.this.jsp_regular.repaint();
                   VertexDataHistogramFrame.this.jsp_kmeans.repaint();
                  }
                  public void mouseClicked(MouseEvent e) {
                   VertexDataHistogramFrame.this.jsp_hfp.repaint();
                   VertexDataHistogramFrame.this.jsp_regular.repaint();
                   VertexDataHistogramFrame.this.jsp_kmeans.repaint();
                  }
               });
             this.InputPanels_hfp.add(jpi);
             jp.add(jpi,  new GridBagConstraints(0, i, 1, 1, 1.0, 1.0
               ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        	}
      }
      this.jsp_hfp.getViewport().add(jp,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	} //else
		//System.out.println("JViewVertexFrame: this.hfp_vertices -> NULL");
  }
//------------------------------------------------------------------------------
  private void ShowVertices_regular( int number_of_mf ) throws Throwable {
	if (this.regular_vertices!=null) {
	    for( int i=0 ; i<this.InputPanels_regular.size() ; i++ )
            this.jsp_regular.remove((Component)this.InputPanels_regular.elementAt(i));

        this.InputPanels_regular= new Vector();
        if( this.regular_vertices.size() < 1 )
            return;

        if( number_of_mf > this.regular_vertices.size() + 1 ) {
            number_of_mf = this.regular_vertices.size()+1;
            MessageKBCT.Information(this, LocaleKBCT.GetString("Maximum_Number_Of_MF_is")+" "+number_of_mf);
        }
        if( number_of_mf < 2 )
            number_of_mf = 2;

        this.jIFNumberOfMF_regular.setValue(number_of_mf);
        JPanel jp= new JPanel(new GridBagLayout());
        for( int i=0 ; i<number_of_mf-1 ; i++ ) {
             double [] range = this.regular_input.GetInputInterestRange();
             Vector vdata = (Vector)this.regular_vertices.elementAt(i);
             double [] data = new double[vdata.size()];
             for( int j=0 ; j<vdata.size() ; j++ )
                  data[j]= ((Double)vdata.elementAt(j)).doubleValue();

             JVariable new_input = new JVariable(i+1, data, range[0], range[1], "");
             JPanelInput jpi = new JPanelInput(new_input);
             jpi.SetMPV(MPV);
             jpi.addMouseListener(new MouseListener() {
             public void mousePressed(MouseEvent e) {
                VertexDataHistogramFrame.this.jsp_hfp.repaint();
                VertexDataHistogramFrame.this.jsp_regular.repaint();
                VertexDataHistogramFrame.this.jsp_kmeans.repaint();
             }
             public void mouseReleased(MouseEvent e) {
                VertexDataHistogramFrame.this.jsp_hfp.repaint();
                VertexDataHistogramFrame.this.jsp_regular.repaint();
                VertexDataHistogramFrame.this.jsp_kmeans.repaint();
             }
             public void mouseEntered(MouseEvent e) {
                VertexDataHistogramFrame.this.jsp_hfp.repaint();
                VertexDataHistogramFrame.this.jsp_regular.repaint();
                VertexDataHistogramFrame.this.jsp_kmeans.repaint();
             }
             public void mouseExited(MouseEvent e) {
                VertexDataHistogramFrame.this.jsp_hfp.repaint();
                VertexDataHistogramFrame.this.jsp_regular.repaint();
                VertexDataHistogramFrame.this.jsp_kmeans.repaint();
             }
             public void mouseClicked(MouseEvent e) {
                VertexDataHistogramFrame.this.jsp_hfp.repaint();
                VertexDataHistogramFrame.this.jsp_regular.repaint();
                VertexDataHistogramFrame.this.jsp_kmeans.repaint();
             }
             });
             this.InputPanels_regular.add(jpi);
             jp.add(jpi,  new GridBagConstraints(0, i, 1, 1, 1.0, 1.0
                   ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        }
        this.jsp_regular.getViewport().add(jp,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
     }
  }
//------------------------------------------------------------------------------
  private void ShowVertices_kmeans( int number_of_mf ) throws Throwable {
	if (this.kmeans_vertices!=null) {
        for( int i=0 ; i<this.InputPanels_kmeans.size() ; i++ )
            this.jsp_kmeans.remove((Component)this.InputPanels_kmeans.elementAt(i));

        this.InputPanels_kmeans= new Vector();
        if( this.kmeans_vertices.size() < 1 )
           return;

        if( number_of_mf > this.kmeans_vertices.size() + 1 ) {
           number_of_mf = this.kmeans_vertices.size()+1;
           MessageKBCT.Information(this, LocaleKBCT.GetString("Maximum_Number_Of_MF_is")+" "+number_of_mf);
        }
        if( number_of_mf < 2 )
           number_of_mf = 2;

        this.jIFNumberOfMF_kmeans.setValue(number_of_mf);
        JPanel jp= new JPanel(new GridBagLayout());
        for( int i=0 ; i<number_of_mf-1 ; i++ ) {
            double [] range = this.kmeans_input.GetInputInterestRange();
            Vector vdata = (Vector)this.kmeans_vertices.elementAt(i);
            double [] data = new double[vdata.size()];
            for( int j=0 ; j<vdata.size() ; j++ )
                data[j]= ((Double)vdata.elementAt(j)).doubleValue();

            JVariable new_input = new JVariable(i+1, data, range[0], range[1], "");
            JPanelInput jpi = new JPanelInput(new_input);
            jpi.SetMPV(MPV);
            jpi.addMouseListener(new MouseListener() {
               public void mousePressed(MouseEvent e) {
                  VertexDataHistogramFrame.this.jsp_hfp.repaint();
                  VertexDataHistogramFrame.this.jsp_regular.repaint();
                  VertexDataHistogramFrame.this.jsp_kmeans.repaint();
               }
               public void mouseReleased(MouseEvent e) {
                  VertexDataHistogramFrame.this.jsp_hfp.repaint();
                  VertexDataHistogramFrame.this.jsp_regular.repaint();
                  VertexDataHistogramFrame.this.jsp_kmeans.repaint();
               }
               public void mouseEntered(MouseEvent e) {
                  VertexDataHistogramFrame.this.jsp_hfp.repaint();
                  VertexDataHistogramFrame.this.jsp_regular.repaint();
                  VertexDataHistogramFrame.this.jsp_kmeans.repaint();
               }
               public void mouseExited(MouseEvent e) {
                  VertexDataHistogramFrame.this.jsp_hfp.repaint();
                  VertexDataHistogramFrame.this.jsp_regular.repaint();
                  VertexDataHistogramFrame.this.jsp_kmeans.repaint();
               }
               public void mouseClicked(MouseEvent e) {
                  VertexDataHistogramFrame.this.jsp_hfp.repaint();
                  VertexDataHistogramFrame.this.jsp_regular.repaint();
                  VertexDataHistogramFrame.this.jsp_kmeans.repaint();
               }
            });
            this.InputPanels_kmeans.add(jpi);
            jp.add(jpi,  new GridBagConstraints(0, i, 1, 1, 1.0, 1.0
                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        }
        this.jsp_kmeans.getViewport().add(jp,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	 }
  }
//------------------------------------------------------------------------------
  private JEvaluationFuzzyPartition[] Evaluation_current() throws Throwable {
    JEvaluationFuzzyPartition[] eval= new JEvaluationFuzzyPartition[1];
    eval[0]= new JEvaluationFuzzyPartition(this.jv_expert, d);
    return eval;
  }
//------------------------------------------------------------------------------
  private JEvaluationFuzzyPartition[] Evaluation_hfp( int number_of_mf ) throws Throwable {
    JEvaluationFuzzyPartition[] eval= new JEvaluationFuzzyPartition[number_of_mf];
    double [] range = this.hfp_input.GetInputInterestRange();
    for( int i=0 ; i<number_of_mf-1 ; i++ ) {
    	if (i < this.hfp_vertices.size()) {
            Vector vdata = (Vector)this.hfp_vertices.elementAt(i);
            double [] data = new double[vdata.size()];
            for( int j=0 ; j<vdata.size() ; j++ )
                 data[j] = ((Double)vdata.elementAt(j)).doubleValue();

            JVariable new_input = new JVariable(i+1, data, range[0], range[1], "");
            eval[i]= new JEvaluationFuzzyPartition(new_input, d);
    	} else {
    		eval[i]= null;
    	}
    }
    return eval;
  }
//------------------------------------------------------------------------------
  private void ShowEvaluation_hfp( int number_of_mf ) throws Throwable {
    if( this.hfp_vertices.size() < 2 )
      return;

    if( number_of_mf < 2 )
      number_of_mf = 2;

    this.jIFNumberOfMF_hfp.setValue(number_of_mf);
    if (!this.ShowEvaluationHFP)
      this.SelectDataTest();

    this.ShowEvaluationHFP= true;
    if (this.d!=null) {
      JEvaluationFuzzyPartition[] eval_curr= this.Evaluation_current();
      JEvaluationFuzzyPartition[] eval= this.Evaluation_hfp(number_of_mf);
      this.jeopf_HFP= new JEvaluationFuzzyPartitionFrame( this.parent, eval_curr, eval, LocaleKBCT.GetString("PartitionHFP"));
      this.ShowEvaluationHFP= false;
      this.d= null;
    }
  }
//------------------------------------------------------------------------------
  private JEvaluationFuzzyPartition[] Evaluation_regular( int number_of_mf ) throws Throwable {
    JEvaluationFuzzyPartition[] eval= new JEvaluationFuzzyPartition[number_of_mf];
    double [] range = this.regular_input.GetInputInterestRange();
    for( int i=0 ; i<number_of_mf-1 ; i++ ) {
    	if (i < this.regular_vertices.size()) {
            Vector vdata = (Vector)this.regular_vertices.elementAt(i);
            double [] data = new double[vdata.size()];
            for( int j=0 ; j<vdata.size() ; j++ )
                 data[j] = ((Double)vdata.elementAt(j)).doubleValue();

            JVariable new_input = new JVariable(i+1, data, range[0], range[1], "");
            eval[i]= new JEvaluationFuzzyPartition(new_input, d);
    	} else {
    		eval[i]= null;
    	}
    }
    return eval;
  }
//------------------------------------------------------------------------------
  private void ShowEvaluation_regular( int number_of_mf ) throws Throwable {
    if( this.regular_vertices.size() < 2 )
      return;

    if( number_of_mf < 2 )
      number_of_mf = 2;

    this.jIFNumberOfMF_regular.setValue(number_of_mf);
    if (!this.ShowEvaluationRegular)
      this.SelectDataTest();

    this.ShowEvaluationRegular= true;
    if (this.d!=null) {
      JEvaluationFuzzyPartition[] eval_curr= this.Evaluation_current();
      JEvaluationFuzzyPartition[] eval= this.Evaluation_regular(number_of_mf);
      this.jeopf_Regular= new JEvaluationFuzzyPartitionFrame( this.parent, eval_curr, eval, LocaleKBCT.GetString("PartitionRegular"));
      this.ShowEvaluationRegular= false;
      this.d= null;
    }
  }
//------------------------------------------------------------------------------
  private JEvaluationFuzzyPartition[] Evaluation_kmeans( int number_of_mf ) throws Throwable {
    JEvaluationFuzzyPartition[] eval= new JEvaluationFuzzyPartition[number_of_mf];
    double [] range = this.kmeans_input.GetInputInterestRange();
    for( int i=0 ; i<number_of_mf-1 ; i++ ) {
    	if (i < this.kmeans_vertices.size()) {
            Vector vdata = (Vector)this.kmeans_vertices.elementAt(i);
            double [] data = new double[vdata.size()];
            for( int j=0 ; j<vdata.size() ; j++ )
                 data[j] = ((Double)vdata.elementAt(j)).doubleValue();

            JVariable new_input = new JVariable(i+1, data, range[0], range[1], "");
            eval[i]= new JEvaluationFuzzyPartition(new_input, d);
    	} else {
    		eval[i]= null;
    	}
    }
    return eval;
  }
//------------------------------------------------------------------------------
  private void ShowEvaluation_kmeans( int number_of_mf ) throws Throwable {
    if( this.kmeans_vertices.size() < 2 )
      return;

    if( number_of_mf < 2 )
      number_of_mf = 2;

    this.jIFNumberOfMF_kmeans.setValue(number_of_mf);
    if (!this.ShowEvaluationKmeans)
      this.SelectDataTest();

    this.ShowEvaluationKmeans= true;
    if (this.d!=null) {
      JEvaluationFuzzyPartition[] eval_curr= this.Evaluation_current();
      JEvaluationFuzzyPartition[] eval= this.Evaluation_kmeans(number_of_mf);
      this.jeopf_Kmeans= new JEvaluationFuzzyPartitionFrame( this.parent, eval_curr, eval, LocaleKBCT.GetString("PartitionKmeans"));
      this.ShowEvaluationKmeans= false;
      this.d= null;
    }
  }
//------------------------------------------------------------------------------
  private void ShowEvaluation_All( int number_of_mf_hfp, int number_of_mf_regular, int number_of_mf_kmeans ) throws Throwable {
    if (!this.ShowEvaluationAll)
      this.SelectDataTest();

    this.ShowEvaluationAll= true;
    if (this.d!=null) {
      JEvaluationFuzzyPartition[] eval_curr= this.Evaluation_current();
      JEvaluationFuzzyPartition[] eval_hfp= this.Evaluation_hfp(number_of_mf_hfp);
      JEvaluationFuzzyPartition[] eval_regular= this.Evaluation_regular(number_of_mf_regular);
      JEvaluationFuzzyPartition[] eval_kmeans= this.Evaluation_kmeans(number_of_mf_kmeans);
      this.jeopf_All= new JEvaluationFuzzyPartitionFrame( this.parent, eval_curr, eval_hfp, eval_regular, eval_kmeans);
      this.ShowEvaluationAll= false;
      this.d= null;
    }
  }
//------------------------------------------------------------------------------
  void jMenuEvaluation_actionPerformed() {
    try {
      this.ShowEvaluation_All(jIFNumberOfMF_hfp.getValue(), jIFNumberOfMF_regular.getValue(), jIFNumberOfMF_kmeans.getValue());
    } catch (Throwable t) {
        t.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JViewVertexFrame: jMenuEvaluation_actionPerformed: "+t);
    }
  }
//------------------------------------------------------------------------------
  String suggestTheBestPartition(int varNumber, JExtendedDataFile dataFile, JVariable currentVar, boolean automatic, boolean PI, boolean PS) {
    try {
      if (automatic && PS) {
          if (this.d == null) {
      	    this.d= dataFile.VariableData(varNumber);
          }
    	  JEvaluationFuzzyPartition[] evalRegular= new JEvaluationFuzzyPartition[8];
    	  JEvaluationFuzzyPartition[] evalHFP= new JEvaluationFuzzyPartition[8];
    	  JEvaluationFuzzyPartition[] evalKmeans= new JEvaluationFuzzyPartition[8];
    	  //System.out.println("STBP 1");
    	  for (int n=0; n<8; n++) {
        	 //System.out.println("STBP 1.1");
             JVariable vregular=this.buildPartition(this.regular_input, this.regular_vertices, n+2);
             if (vregular==null)
            	 evalRegular[n]= null;
             else	 
                 evalRegular[n]= new JEvaluationFuzzyPartition(vregular, this.d);

             //System.out.println("STBP 1.2");
             JVariable vhfp=this.buildPartition(this.hfp_input, this.hfp_vertices, n+2);
             if (vhfp==null)
            	 evalHFP[n]= null;
             else	 
                 evalHFP[n]= new JEvaluationFuzzyPartition(vhfp, this.d);

             //System.out.println("STBP 1.3");
             JVariable vkmeans=this.buildPartition(this.kmeans_input, this.kmeans_vertices, n+2);
             if (vkmeans==null)
            	 evalKmeans[n]= null;
             else	 
                 evalKmeans[n]= new JEvaluationFuzzyPartition(vkmeans, this.d);
    	  }
    	  //System.out.println("STBP 2");
    	  DecimalFormat df= new DecimalFormat();
    	  df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals()+2);//5
    	  df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals()+2);//5
    	  double[] totalPC= new double[3];
    	  double[] totalPCind= new double[3];
    	  double[] totalPE= new double[3];
    	  double[] totalPEind= new double[3];
    	  double[] totalChI= new double[3];
    	  double[] totalChIind= new double[3];
    	  double[] regPC= new double[8];
    	  double[] regPE= new double[8];
    	  double[] regChI= new double[8];
    	  //System.out.println("STBP 3");
          for (int n=0; n<8; n++) {
        	  if (evalRegular[n]!=null) {
                  regPC[n]= evalRegular[n].getPC();
                  regPE[n]= evalRegular[n].getPE();
                  regChI[n]= evalRegular[n].getChenIndex();
        	      MessageKBCT.WriteLogFile("----  Regular", "SelectPartitions");
                  MessageKBCT.WriteLogFile("------  "+String.valueOf(n+2)+"  ->  PC="+df.format(regPC[n])+"  PE="+df.format(regPE[n])+"  ChI="+df.format(regChI[n]), "SelectPartitions");
        	  }
     	  }
    	  //System.out.println("STBP 4");
          double[] bestRegPC= jnikbct.findMax(regPC);
          double[] bestRegPE= jnikbct.findMin(regPE);
          double[] bestRegChI= jnikbct.findMax(regChI);
          totalPC[0]=bestRegPC[1];
          totalPCind[0]=bestRegPC[0]+1;
          totalPE[0]=bestRegPE[1];
          totalPEind[0]=bestRegPE[0]+1;
          totalChI[0]=bestRegChI[1];
          totalChIind[0]=bestRegChI[0]+1;
          MessageKBCT.WriteLogFile("++++++  The best Regular:  ->  PC="+df.format(bestRegPC[1])+" ("+String.valueOf(bestRegPC[0]+1)+" labels)   PE="+df.format(bestRegPE[1])+" ("+String.valueOf(bestRegPE[0]+1)+" labels)   ChI="+df.format(bestRegChI[1])+" ("+String.valueOf(bestRegChI[0]+1)+" labels)", "SelectPartitions");
    	  double[] hfpPC= new double[8];
    	  double[] hfpPE= new double[8];
    	  double[] hfpChI= new double[8];
    	  //System.out.println("STBP 5");
          for (int n=0; n<8; n++) {
        	  if (evalHFP[n]!=null) {
                  hfpPC[n]= evalHFP[n].getPC();
                  hfpPE[n]= evalHFP[n].getPE();
                  hfpChI[n]= evalHFP[n].getChenIndex();
                  MessageKBCT.WriteLogFile("----  HFP", "SelectPartitions");
                  MessageKBCT.WriteLogFile("----  "+String.valueOf(n+2)+"  ->  PC="+df.format(hfpPC[n])+"  PE="+df.format(hfpPE[n])+"  ChI="+df.format(hfpChI[n]), "SelectPartitions");
        	  }
     	  }
    	  //System.out.println("STBP 6");
          double[] bestHFPPC= jnikbct.findMax(hfpPC);
          double[] bestHFPPE= jnikbct.findMin(hfpPE);
          double[] bestHFPChI= jnikbct.findMax(hfpChI);
          totalPC[1]=bestHFPPC[1];
          totalPCind[1]=bestHFPPC[0]+1;
          totalPE[1]=bestHFPPE[1];
          totalPEind[1]=bestHFPPE[0]+1;
          totalChI[1]=bestHFPChI[1];
          totalChIind[1]=bestHFPChI[0]+1;
          MessageKBCT.WriteLogFile("++++++  The best HFP:  ->  PC="+df.format(bestHFPPC[1])+" ("+String.valueOf(bestHFPPC[0]+1)+" labels)   PE="+df.format(bestHFPPE[1])+" ("+String.valueOf(bestHFPPE[0]+1)+" labels)   ChI="+df.format(bestHFPChI[1])+" ("+String.valueOf(bestHFPChI[0]+1)+" labels)", "SelectPartitions");
    	  double[] kmeansPC= new double[8];
    	  double[] kmeansPE= new double[8];
    	  double[] kmeansChI= new double[8];
    	  //System.out.println("STBP 7");
          for (int n=0; n<8; n++) {
        	  if (evalKmeans[n]!=null) {
                  kmeansPC[n]= evalKmeans[n].getPC();
                  kmeansPE[n]= evalKmeans[n].getPE();
                  kmeansChI[n]= evalKmeans[n].getChenIndex();
                  MessageKBCT.WriteLogFile("----  Kmeans", "SelectPartitions");
                  MessageKBCT.WriteLogFile("----  "+String.valueOf(n+2)+"  ->  PC="+df.format(kmeansPC[n])+"  PE="+df.format(kmeansPE[n])+"  ChI="+df.format(kmeansChI[n]), "SelectPartitions");
        	  }
     	  }
    	  //System.out.println("STBP 8");
          double[] bestKmeansPC= jnikbct.findMax(kmeansPC);
          double[] bestKmeansPE= jnikbct.findMin(kmeansPE);
          double[] bestKmeansChI= jnikbct.findMax(kmeansChI);
          totalPC[2]=bestKmeansPC[1];
          totalPCind[2]=bestKmeansPC[0]+1;
          totalPE[2]=bestKmeansPE[1];
          totalPEind[2]=bestKmeansPE[0]+1;
          totalChI[2]=bestKmeansChI[1];
          totalChIind[2]=bestKmeansChI[0]+1;
          MessageKBCT.WriteLogFile("++++++  The best Kmeans:  ->  PC="+df.format(bestKmeansPC[1])+" ("+String.valueOf(bestKmeansPC[0]+1)+" labels)   PE="+df.format(bestKmeansPE[1])+" ("+String.valueOf(bestKmeansPE[0]+1)+" labels)   ChI="+df.format(bestKmeansChI[1])+" ("+String.valueOf(bestKmeansChI[0]+1)+" labels)", "SelectPartitions");
          double[] bestTotalPC= jnikbct.findMax(totalPC);
          double[] bestTotalPE= jnikbct.findMin(totalPE);
          double[] bestTotalChI= jnikbct.findMax(totalChI);
          String[] bestType= new String[3];
          double[] values= {bestTotalPC[0],bestTotalPE[0],bestTotalChI[0]};
    	  //System.out.println("STBP 9");
          for (int n=0; n<3; n++) {
              if (values[n]==0)
            	  bestType[n]= "Regular";
              else if (values[n]==1)
            	  bestType[n]= "HFP";
              else
            	  bestType[n]= "Kmeans";
          }
          MessageKBCT.WriteLogFile("++++++  The best partition:  ->  PC="+df.format(bestTotalPC[1])+" ("+String.valueOf(totalPCind[(int)bestTotalPC[0]-1])+" labels, "+bestType[0]+")    PE="+df.format(bestTotalPE[1])+" ("+String.valueOf(totalPEind[(int)bestTotalPE[0]-1])+" labels, "+bestType[1]+")    ChI="+df.format(bestTotalChI[1])+" ("+String.valueOf(totalChIind[(int)bestTotalChI[0]-1])+" labels, "+bestType[2]+")", "SelectPartitions");
          String[][] bestTypeNbLabels= new String[8][3];
    	  //System.out.println("STBP 10");
          for (int n=2; n<10; n++) {
        	  double[] indPCNbLab= {regPC[n-2],hfpPC[n-2],kmeansPC[n-2]};
              double[] bestPCNbLab= jnikbct.findMax(indPCNbLab);
              if (bestPCNbLab[0]==1)
            	  bestTypeNbLabels[n-2][0]= "Regular";
              else if (bestPCNbLab[0]==2)
            	  bestTypeNbLabels[n-2][0]= "HFP";
              else if (bestPCNbLab[0]==3)
            	  bestTypeNbLabels[n-2][0]= "Kmeans";

              double[] indPENbLab= {regPE[n-2],hfpPE[n-2],kmeansPE[n-2]};
              double[] bestPENbLab= jnikbct.findMin(indPENbLab);
              if (bestPENbLab[0]==1)
            	  bestTypeNbLabels[n-2][1]= "Regular";
              else if (bestPENbLab[0]==2)
            	  bestTypeNbLabels[n-2][1]= "HFP";
              else if (bestPENbLab[0]==3)
            	  bestTypeNbLabels[n-2][1]= "Kmeans";

              double[] indChINbLab= {regChI[n-2],hfpChI[n-2],kmeansChI[n-2]};
              double[] bestChINbLab= jnikbct.findMax(indChINbLab);
              if (bestChINbLab[0]==1)
            	  bestTypeNbLabels[n-2][2]= "Regular";
              else if (bestChINbLab[0]==2)
            	  bestTypeNbLabels[n-2][2]= "HFP";
              else if (bestChINbLab[0]==3)
            	  bestTypeNbLabels[n-2][2]= "Kmeans";
          }
    	  //System.out.println("STBP 11");
          for (int n=2; n<10; n++) {
               MessageKBCT.WriteLogFile("++++++  The best partition ("+n+" labels):  ->  PC="+bestTypeNbLabels[n-2][0]+"    PE="+bestTypeNbLabels[n-2][1]+"    ChI="+bestTypeNbLabels[n-2][2], "SelectPartitions");
          }
      } 
      //else {
	      //System.out.println("STBP 12");
    	  JEvaluationFuzzyPartition[] eval= new JEvaluationFuzzyPartition[4];
          if (this.d == null) {
    	    this.d= dataFile.VariableData(varNumber);
          }
          // current partition
    	  //System.out.println("STBP 13");
          eval[0]= new JEvaluationFuzzyPartition(currentVar, this.d);
          int NbLabels= currentVar.GetLabelsNumber();
    	  //System.out.println("JViewVertexFrame: NbLabels="+NbLabels);
          // regular partition
          JVariable vr=null;
          if ( (this.regular_input!=null) && (this.regular_vertices!=null) )
              vr=this.buildPartition(this.regular_input, this.regular_vertices, NbLabels);

          if (vr!=null)
              eval[1]= new JEvaluationFuzzyPartition(vr, this.d);
          else
        	  eval[1]= null;

          // hfp partition
    	  //System.out.println("STBP 14");
          JVariable vh= null;
          if ( (this.hfp_input!=null) && (this.hfp_vertices!=null) )
              vh=this.buildPartition(this.hfp_input, this.hfp_vertices, NbLabels);

          if (vh!=null)
              eval[2]= new JEvaluationFuzzyPartition(vh, this.d);
          else
        	  eval[2]= null;

          // kmeans partition
    	  //System.out.println("STBP 15");
          JVariable vk= null;
          if ( (this.kmeans_input!=null) && (this.kmeans_vertices!=null) )
              vk=this.buildPartition(this.kmeans_input, this.kmeans_vertices, NbLabels);

          if (vk!=null)
              eval[3]= new JEvaluationFuzzyPartition(vk, this.d);
          else 
        	  eval[3]= null;

          if ( (vr!=null) && (vh==null) && (vk==null) )
        	  return "regular";
          if ( (vr==null) && (vh!=null) && (vk==null) )
        	  return "hfp";
          if ( (vr==null) && (vh==null) && (vk!=null) )
        	  return "kmeans";
          
          //System.out.println("STBP 16");
          int contCurr=0;
          int contReg=0;
          int contHfp=0;
          int contKmeans=0;
          if ( (vr!=null) && (vh!=null) && (vk!=null) ) {
              if ( (eval[0].getPC() >= eval[1].getPC()) && (eval[0].getPC() >= eval[2].getPC()) && (eval[0].getPC() >= eval[3].getPC()) )
    	          contCurr++;
              else if ( (eval[1].getPC() >= eval[0].getPC()) && (eval[1].getPC() >= eval[2].getPC()) && (eval[1].getPC() >= eval[3].getPC()) )
    	          contReg++;
              else if ( (eval[2].getPC() >= eval[0].getPC()) && (eval[2].getPC() >= eval[1].getPC()) && (eval[2].getPC() >= eval[3].getPC()) )
    	          contHfp++;
              else if ( (eval[3].getPC() >= eval[0].getPC()) && (eval[3].getPC() >= eval[1].getPC()) && (eval[3].getPC() >= eval[2].getPC()) )
    	          contKmeans++;

              if ( (eval[0].getPE() <= eval[1].getPE()) && (eval[0].getPE() <= eval[2].getPE()) && (eval[0].getPE() <= eval[3].getPE()) )
    	          contCurr++;
              else if ( (eval[1].getPE() <= eval[0].getPE()) && (eval[1].getPE() <= eval[2].getPE()) && (eval[1].getPE() <= eval[3].getPE()) )
    	          contReg++;
              else if ( (eval[2].getPE() <= eval[0].getPE()) && (eval[2].getPE() <= eval[1].getPE()) && (eval[2].getPE() <= eval[3].getPE()) )
    	          contHfp++;
              else if ( (eval[3].getPE() <= eval[0].getPE()) && (eval[3].getPE() <= eval[1].getPE()) && (eval[3].getPE() <= eval[2].getPE()) )
    	          contKmeans++;
      
              if ( (eval[0].getChenIndex() >= eval[1].getChenIndex()) && (eval[0].getChenIndex() >= eval[2].getChenIndex()) && (eval[0].getChenIndex() >= eval[3].getChenIndex()) )
       	          contCurr++;
              else if ( (eval[1].getChenIndex() >= eval[0].getChenIndex()) && (eval[1].getChenIndex() >= eval[2].getChenIndex()) && (eval[1].getChenIndex() >= eval[3].getChenIndex()) )
    	          contReg++;
              else if ( (eval[2].getChenIndex() >= eval[0].getChenIndex()) && (eval[2].getChenIndex() >= eval[1].getChenIndex()) && (eval[2].getChenIndex() >= eval[3].getChenIndex()) )
    	          contHfp++;
              else if ( (eval[3].getChenIndex() >= eval[0].getChenIndex()) && (eval[3].getChenIndex() >= eval[1].getChenIndex()) && (eval[3].getChenIndex() >= eval[2].getChenIndex()) )
    	          contKmeans++;
          } else {
        	  return "regular";
          }
    	  //System.out.println("STBP 17");
          this.d=null;
          if ( (contCurr >= contReg) && (contCurr >= contHfp) && (contCurr >= contKmeans) ) {
              return LocaleKBCT.GetString("ExpertPartition");
          } else if ( (contReg >= contCurr) && (contReg >= contHfp) && (contReg >= contKmeans) ) {
              return "regular";
          } else if ( (contHfp >= contCurr) && (contHfp >= contReg) && (contHfp >= contKmeans) ) {
              return "hfp";
          } else if ( (contKmeans >= contCurr) && (contKmeans >= contReg) && (contKmeans >= contHfp) ) {
              return "kmeans";
          }
        //}
    } catch (Throwable t) {
        t.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in VertexDataHistogramFrame (JViewVertexFrame.java): sugestTheBestPartition(): "+t);
    }
    this.d=null;
    return "-1";
  }
//------------------------------------------------------------------------------
  JVariable buildPartition(JInputHFP jinHFP, Vector vertices, int NbLabels) throws Throwable {
	    //System.out.println("JViewVertexFrame: buildPartition: NbLabels: "+NbLabels);
        //System.out.println("BP1");
	    double [] range = jinHFP.GetInputInterestRange();
        //System.out.println("BP1: vs="+vertices.size()+"  nl="+NbLabels);
	    if (vertices.size() < NbLabels-1) {
	    	NbLabels= vertices.size()+1;
	    	//System.out.println("JViewVertexFrame: buildPartition: NULL: new NbLabels: "+NbLabels);
	    	//return null;
	    }
        //System.out.println("BP2");
	    double[] data;
	    if (NbLabels >= 2) {
	        Vector vdata = (Vector)vertices.elementAt(NbLabels-2);
	        data = new double[vdata.size()];
	        for( int j=0 ; j<vdata.size() ; j++ )
	             data[j] = ((Double)vdata.elementAt(j)).doubleValue();
	    } else {
	        data = new double[2];
	    	data[0]= range[0];
	    	data[1]= range[1];
	    }
        //System.out.println("BP3");
	    JVariable partition = new JVariable(NbLabels-1, data, range[0], range[1], "");
        //if (partition==null)
        	//System.out.println("JViewVertexFrame: buildPartition: NULL");
        
        return partition;
  }
//------------------------------------------------------------------------------
  void jButtonEvaluationHFP_actionPerformed() {
    try { ShowEvaluation_hfp(jIFNumberOfMF_hfp.getValue()); }
    catch(Throwable t) {
    	t.printStackTrace();
    	MessageKBCT.Error(null, t); 
    	}
  }
//------------------------------------------------------------------------------
  void jButtonEvaluationRegular_actionPerformed() {
    try { ShowEvaluation_regular(jIFNumberOfMF_regular.getValue()); }
    catch(Throwable t) {
    	t.printStackTrace();
    	MessageKBCT.Error(null, t); 
    	}
  }
//------------------------------------------------------------------------------
  void jButtonEvaluationKmeans_actionPerformed() {
    try { ShowEvaluation_kmeans(jIFNumberOfMF_kmeans.getValue()); }
    catch(Throwable t) {
    	t.printStackTrace();
    	MessageKBCT.Error(null, t); 
    	}
  }
//------------------------------------------------------------------------------
  private void SelectDataTest() {
    File f= new File(this.DataFile.FileName());
    String TestFile=f.getAbsolutePath();
    String message= LocaleKBCT.GetString("TestFile")+"= "+TestFile+"\n"
                    +LocaleKBCT.GetString("DoYouWantToReplaceIt");

    int option= MessageKBCT.Confirm(this, message, 1, false, false, false);
    if (option==0) {
      this.jTestFrame= new JFrame();
      this.jTestFrame.getContentPane().setLayout(new GridBagLayout());
      this.jTestFrame.setIconImage(LocaleKBCT.getIconGUAJE().getImage());
      JPanel jPanelTestFile = new JPanel(new GridBagLayout());
      jPanelTestFile.add(jButtonTestFile, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      File DF= new File(this.DataFile.ActiveFileName());
      TestFile= DF.getAbsolutePath();
      jTFTestFile.setText(TestFile);
      jPanelTestFile.add(jTFTestFile, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 100, 0));
      jPanelTestFile.add(this.jbCompute, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 50, 0));
      this.jTestFrame.getContentPane().add(jPanelTestFile, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
      this.jButtonTestFile.setText(LocaleKBCT.GetString("TestFile") + " :");
      this.jbCompute.setText(LocaleKBCT.GetString("Compute"));
      Dimension dim = getToolkit().getScreenSize();
      this.jTestFrame.setSize(600, 150);
      this.jTestFrame.setLocation(dim.width/2 - this.jTestFrame.getWidth()/2, dim.height/2 - this.jTestFrame.getHeight()/2);
      this.jTestFrame.setResizable(false);
      this.jTestFrame.pack();
      this.jTestFrame.setVisible(true);
      this.jButtonTestFile.addActionListener(new java.awt.event.ActionListener() { public void actionPerformed(ActionEvent e) {
        JOpenFileChooser file_chooser = new JOpenFileChooser(MainKBCT.getConfig().GetKBCTFilePath());
        if( file_chooser.showOpenDialog(VertexDataHistogramFrame.this.jTestFrame) == JFileChooser.APPROVE_OPTION ) {
          String file_name = file_chooser.getSelectedFile().getAbsolutePath();
          File f = new File( file_name );
          if( f.exists() ) {
            VertexDataHistogramFrame.this.jTFTestFile.setText(file_name);
          }
        }
      } });
      this.jbCompute.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
             try {
                JDataFile aux= new JDataFile(jTFTestFile.getText());
                int var= VertexDataHistogramFrame.this.DataIndex;
                int NbIn= VertexDataHistogramFrame.this.kbct.GetNbInputs();
                int NbOut= VertexDataHistogramFrame.this.kbct.GetNbOutputs();
                int DataVar= VertexDataHistogramFrame.this.DataFile.VariableCount();
                if (NbIn+NbOut != DataVar) {
                    String msg= LocaleKBCT.GetString("Select_Data_link_to_Var");
                    Integer opt= (Integer)MessageKBCT.SelectDataVar(VertexDataHistogramFrame.this, msg, 1);
                    var= opt.intValue();
                }
                VertexDataHistogramFrame.this.d= aux.VariableData(var);
                VertexDataHistogramFrame.this.jTestFrame.dispose();
                VertexDataHistogramFrame.this.jTestFrame= null;
                if (VertexDataHistogramFrame.this.ShowEvaluationAll) {
                    VertexDataHistogramFrame.this.jMenuEvaluation_actionPerformed();
                } else if (VertexDataHistogramFrame.this.ShowEvaluationHFP) {
                    VertexDataHistogramFrame.this.jButtonEvaluationHFP_actionPerformed();
                } else if (VertexDataHistogramFrame.this.ShowEvaluationRegular) {
                    VertexDataHistogramFrame.this.jButtonEvaluationRegular_actionPerformed();
                } else if (VertexDataHistogramFrame.this.ShowEvaluationKmeans) {
                    VertexDataHistogramFrame.this.jButtonEvaluationKmeans_actionPerformed();
                }
                VertexDataHistogramFrame.this.d= null;
             } catch (Throwable t) {
                t.printStackTrace();
                VertexDataHistogramFrame.this.d= VertexDataHistogramFrame.this.DataFile.VariableData(VertexDataHistogramFrame.this.DataIndex);
             }
          }
      });
    } else
        d= this.DataFile.VariableData(this.DataIndex);
  }
//------------------------------------------------------------------------------
  void jMenuSave_actionPerformed(boolean automatic, boolean PI, boolean PS) {
    if (PS)
	  MessageKBCT.WriteLogFile(this.Title, "SelectPartitions");
    
    if (PI) {
    	String typeIP= MainKBCT.getConfig().GetInductionType();
    	int nbLabIP= MainKBCT.getConfig().GetInductionNbLabels();
    	//System.out.println("typeIP= "+typeIP+"  nbLabIP="+nbLabIP);
        JVariable new_variable;
        Vector vdata= new Vector();
        double[] data;
        try {
           if (typeIP.equals("hfp")) {
        	   if ( (!automatic) && (this.hfp_vertices.size() < nbLabIP-1) ) {
        		   MessageKBCT.Information(this, LocaleKBCT.GetString("Maximum_Number_Of_MF_is")+" "+String.valueOf(this.kmeans_vertices.size()+1));
        		   return;
        	   } else {
        		   if (nbLabIP-2 <= this.hfp_vertices.size())
                       vdata = (Vector)this.hfp_vertices.elementAt(nbLabIP-2);
        		   else
                       vdata = (Vector)this.hfp_vertices.elementAt(this.hfp_vertices.size()-1);
        	   }
           } else if (typeIP.equals("regular")) {
    		   if (nbLabIP-2 <= this.regular_vertices.size())
                   vdata = (Vector)this.regular_vertices.elementAt(nbLabIP-2);
    		   else
                   vdata = (Vector)this.regular_vertices.elementAt(this.regular_vertices.size()-1);
    			   
           } else {
        	   if ( (!automatic) && (this.kmeans_vertices.size() < nbLabIP-1) ) {
        		   MessageKBCT.Information(this, LocaleKBCT.GetString("Maximum_Number_Of_MF_is")+" "+String.valueOf(this.kmeans_vertices.size()+1));
        		   return;
        	   } else { 
        		   if (nbLabIP-2 <= this.kmeans_vertices.size())
                       vdata = (Vector)this.kmeans_vertices.elementAt(nbLabIP-2);
        		   else
                       vdata = (Vector)this.kmeans_vertices.elementAt(this.kmeans_vertices.size()-1);
        	   } 
           }
           data = new double[vdata.size()];
           for( int j=0 ; j<vdata.size() ; j++ )
                data[j] = ((Double)vdata.elementAt(j)).doubleValue();

           new_variable= new JVariable(this.jv_expert.GetPtr(), data, this.jv_data.GetInputInterestRange()[0], this.jv_data.GetInputInterestRange()[1], "");
           this.jv_expert.SetLabelsNumber(0);
           int NOL= new_variable.GetLabelsNumber();
           
           for (int n=0; n<NOL; n++) {
                LabelKBCT label= new_variable.GetLabel(n + 1);
                if (n==0)
                    label.SetP1(this.jv_expert.GetInputInterestRange()[0]);
                else if (n==NOL-1)
                    label.SetP3(this.jv_expert.GetInputInterestRange()[1]);

                this.jv_expert.AddLabel(label);
           }
           Enumeration en= this.MPV.elements();
           int NbMP=0;
           while (en.hasMoreElements()) {
              String MP= (String) en.nextElement();
              NbMP++;
              double modal_point= (new Double(MP)).doubleValue();
              for (int n=0; n<NOL; n++) {
                 LabelKBCT label= jv_expert.GetLabel(n + 1);
                 double p1= label.GetP1();
                 double p2= label.GetP2();
                 double p3= label.GetP3();
                 String mp= label.GetMP();
                 boolean flag= false;
                 if (!mp.equals("No MP")) {
                     double old_modal_point= (new Double(mp)).doubleValue();
                     double diff1= Math.abs(p2-old_modal_point);
                     double diff2= Math.abs(p2-modal_point);
                     if (diff1 <= diff2)
                         flag= true;
                 }
                 if (!flag) {
                     if ( ( (n==0) && ((p2+p3)/2 >= modal_point) ) ||
                    	  ( (n==NOL-1) && ((p1+p2)/2 <= modal_point) ) || 
                    	  ( ((p1+p2)/2 <= modal_point) && ( (p2+p3)/2 >= modal_point) ) ) {
                          //NOTA: no se inducen funciones trapezoidales.
                    	  label.SetMP(MP);
                          this.jv_expert.ReplaceLabel(n+1, label);
                          break;
                   }
                 }
               }
           }
           this.jv_expert.SetLabelsName();
           this.jv_expert.SetType("numerical");
           if (this.DataIndex+1<=this.kbct.GetNbInputs())
               jnikbct.ReplaceInput(this.kbct.GetPtr(), this.DataIndex+1, this.jv_expert.GetV());
           else
               jnikbct.ReplaceOutput(this.kbct.GetPtr(), this.DataIndex+1-this.kbct.GetNbInputs(), this.jv_expert.GetV());

           jnikbct.SaveKBCT(this.kbct.GetPtr(), this.kbct.GetKBCTFile());
           this.MPV= new Vector();
           for (int n=0; n<NOL; n++) {
                String mp=jv_expert.GetLabel(n+1).GetMP();
                if (!mp.equals("No MP"))
                    this.MPV.add(mp);
           }
           this.ShowVertices_hfp(jIFNumberOfMF_hfp.getValue());
           this.ShowVertices_regular(jIFNumberOfMF_regular.getValue());
           this.ShowVertices_kmeans(jIFNumberOfMF_kmeans.getValue());
           this.repaint();
        } catch (Throwable t) {
        	t.printStackTrace();
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JViewVertexFrame: jMenuSave_actionPerformed1: "+t);
        }
    		
    } else if (this.Type.equals(LocaleKBCT.GetString("ExpertPartition"))) {
        if (!automatic)
      	  MessageKBCT.Information(this, LocaleKBCT.GetString("SavingCurrentPartitionNoChanges"));
        else if (PS) 
          MessageKBCT.WriteLogFile("     "+LocaleKBCT.GetString("SavingCurrentPartitionNoChanges"), "SelectPartitions");
    } else {
    	int confirm= 0;
    	if (!automatic)
    	    confirm= MessageKBCT.Confirm(this, LocaleKBCT.GetString("ReplaceVariableFromKB")+
                                             "\n"+
                                             LocaleKBCT.GetString("DoYouWantToReplaceIt"), 0, false, false, false);

    	if (confirm == 0) {
        JVariable new_variable;
        Vector vdata= new Vector();
        double[] data;
        try {
           if (this.Type.equals("hfp")) {
        	   if (PS)
        		   MessageKBCT.WriteLogFile("     "+LocaleKBCT.GetString("PartitionHFP"), "SelectPartitions");
        	   
        	   if ( (!automatic) && (this.hfp_vertices.size() < this.NOL-1) ) {
        		   MessageKBCT.Information(this, LocaleKBCT.GetString("Maximum_Number_Of_MF_is")+" "+String.valueOf(this.kmeans_vertices.size()+1));
        		   return;
        	   } else   
                   vdata = (Vector)this.hfp_vertices.elementAt(this.NOL-2);
           } else if (this.Type.equals("regular")) {
        	   if (PS)
        		   MessageKBCT.WriteLogFile("     "+LocaleKBCT.GetString("PartitionRegular"), "SelectPartitions");
        	   
               vdata = (Vector)this.regular_vertices.elementAt(this.NOL-2);
           } else {
        	   if (PS)
        		   MessageKBCT.WriteLogFile("     "+LocaleKBCT.GetString("PartitionKmeans"), "SelectPartitions");
        	   
        	   if ( (!automatic) && (this.kmeans_vertices.size() < this.NOL-1) ) {
        		   MessageKBCT.Information(this, LocaleKBCT.GetString("Maximum_Number_Of_MF_is")+" "+String.valueOf(this.kmeans_vertices.size()+1));
        		   return;
        	   } else   
                   vdata = (Vector)this.kmeans_vertices.elementAt(this.NOL-2);
           }
           data = new double[vdata.size()];
           for( int j=0 ; j<vdata.size() ; j++ )
                data[j] = ((Double)vdata.elementAt(j)).doubleValue();

           new_variable= new JVariable(this.jv_expert.GetPtr(), data, this.jv_data.GetInputInterestRange()[0], this.jv_data.GetInputInterestRange()[1], "");
           this.jv_expert.SetLabelsNumber(0);
           int NOL= new_variable.GetLabelsNumber();
           if (PS) {
        	   MessageKBCT.WriteLogFile("     "+LocaleKBCT.GetString("NOL")+"= "+NOL, "SelectPartitions");
        	   MessageKBCT.WriteLogFile("*************************", "SelectPartitions");
           }
           for (int n=0; n<NOL; n++) {
                LabelKBCT label= new_variable.GetLabel(n + 1);
                if (n==0)
                    label.SetP1(this.jv_expert.GetInputInterestRange()[0]);
                else if (n==NOL-1)
                    label.SetP3(this.jv_expert.GetInputInterestRange()[1]);

                this.jv_expert.AddLabel(label);
           }
           Enumeration en= this.MPV.elements();
           int NbMP=0;
           while (en.hasMoreElements()) {
              String MP= (String) en.nextElement();
              NbMP++;
              double modal_point= (new Double(MP)).doubleValue();
              for (int n=0; n<NOL; n++) {
                 LabelKBCT label= jv_expert.GetLabel(n + 1);
                 double p1= label.GetP1();
                 double p2= label.GetP2();
                 double p3= label.GetP3();
                 String mp= label.GetMP();
                 boolean flag= false;
                 if (!mp.equals("No MP")) {
                     double old_modal_point= (new Double(mp)).doubleValue();
                     double diff1= Math.abs(p2-old_modal_point);
                     double diff2= Math.abs(p2-modal_point);
                     if (diff1 <= diff2)
                         flag= true;
                 }
                 if (!flag) {
                     if ( ( (n==0) && ((p2+p3)/2 >= modal_point) ) ||
                    	  ( (n==NOL-1) && ((p1+p2)/2 <= modal_point) ) || 
                    	  ( ((p1+p2)/2 <= modal_point) && ( (p2+p3)/2 >= modal_point) ) ) {
                          //NOTA: no se inducen funciones trapezoidales.
                    	  label.SetMP(MP);
                          this.jv_expert.ReplaceLabel(n+1, label);
                          break;
                   }
                 }
               }
           }
           this.jv_expert.SetLabelsName();
           this.jv_expert.SetType("numerical");
           if (this.DataIndex+1<=this.kbct.GetNbInputs())
               jnikbct.ReplaceInput(this.kbct.GetPtr(), this.DataIndex+1, this.jv_expert.GetV());
           else
               jnikbct.ReplaceOutput(this.kbct.GetPtr(), this.DataIndex+1-this.kbct.GetNbInputs(), this.jv_expert.GetV());

           jnikbct.SaveKBCT(this.kbct.GetPtr(), this.kbct.GetKBCTFile());
           this.MPV= new Vector();
           for (int n=0; n<NOL; n++) {
                String mp=jv_expert.GetLabel(n+1).GetMP();
                if (!mp.equals("No MP"))
                    this.MPV.add(mp);
           }
           this.ShowVertices_hfp(jIFNumberOfMF_hfp.getValue());
           this.ShowVertices_regular(jIFNumberOfMF_regular.getValue());
           this.ShowVertices_kmeans(jIFNumberOfMF_kmeans.getValue());
           this.repaint();
        } catch (Throwable t) {
        	t.printStackTrace();
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JViewVertexFrame: jMenuSave_actionPerformed2: "+t);
        }
      }
    }
  }
//------------------------------------------------------------------------------
  void jbSPApply_actionPerformed(boolean automatic, boolean PI, boolean PS) {
	  //MessageKBCT.Information(this, "PARTITION NOT SAVED YET");
	  jMenuSave_actionPerformed(automatic, PI, PS);
  }
//------------------------------------------------------------------------------
  public void Translate() {
    super.Translate();
    this.jLabelNumberOfMF_hfp.setText(LocaleKBCT.GetString("MFs")+":");
    this.jButtonApplyNumberOfMF_hfp.setText(LocaleKBCT.GetString("Apply"));
    this.jButtonEvaluation_hfp.setText(LocaleKBCT.GetString("Evaluation"));
    this.jButtonEvaluation_hfp.setToolTipText(LocaleKBCT.GetString("EvaluationOfFuzzyPartitions"));
    this.jLabelNumberOfMF_regular.setText(LocaleKBCT.GetString("MFs")+":");
    this.jButtonApplyNumberOfMF_regular.setText(LocaleKBCT.GetString("Apply"));
    this.jButtonEvaluation_regular.setText(LocaleKBCT.GetString("Evaluation"));
    this.jButtonEvaluation_regular.setToolTipText(LocaleKBCT.GetString("EvaluationOfFuzzyPartitions"));
    this.jLabelNumberOfMF_kmeans.setText(LocaleKBCT.GetString("MFs")+":");
    this.jButtonApplyNumberOfMF_kmeans.setText(LocaleKBCT.GetString("Apply"));
    this.jButtonEvaluation_kmeans.setText(LocaleKBCT.GetString("Evaluation"));
    this.jButtonEvaluation_kmeans.setToolTipText(LocaleKBCT.GetString("EvaluationOfFuzzyPartitions"));
    this.titledBorderAllPartitions.setTitle(LocaleKBCT.GetString("AllPartitions"));
    this.titledBorderPartitionHFP.setTitle(LocaleKBCT.GetString("PartitionHFP"));
    this.titledBorderPartitionRegular.setTitle(LocaleKBCT.GetString("PartitionRegular"));
    this.titledBorderPartitionKmeans.setTitle(LocaleKBCT.GetString("PartitionKmeans"));
    if (this.jTestFrame!=null) {
      this.jButtonTestFile.setText(LocaleKBCT.GetString("TestFile") + " :");
      this.jbCompute.setText(LocaleKBCT.GetString("Compute"));
    }
  }
//------------------------------------------------------------------------------
  protected HistogramPanel HistogramPanel() throws Throwable { return new HistogramInputPanel(this.hfp_input/*, this.Histogram*/); }
//------------------------------------------------------------------------------
  public void this_windowClosing() {
    this.dispose();
  }
}