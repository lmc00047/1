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
//                              JLinksFrame.java
//
//
//**********************************************************************

//***********************************************************************

//
//
//                              JLinksFrame.java
//
//
// Author(s) : Jean-Luc LABLEE
// FISPRO Version : 2.1
// Contact : fispro@ensam.inra.fr
// Last modification date:  September 15, 2004
// File :

//**********************************************************************

package kbctFrames;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.*;

//import kbct.JKBCTInput;
import kbct.JKBCTOutput;
import kbct.jnikbct;
import kbct.JFIS;
import kbct.JKBCT;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbctAux.DoubleField;
import kbctAux.JOpenFileChooser;
import kbctAux.MessageKBCT;
import KB.Rule;
import KB.LabelKBCT;
import fis.JExtendedDataFile;
import fis.JFISDialog;
//import fis.JOutput;
//import fis.JRule;
import fis.JInput;
import fis.jnifis;

/**
 * kbctFrames.JLinksFrame.
 * 
 *@author Jose Maria Alonso Moral
 *@version 3.0 , 07/08/15
 */
//------------------------------------------------------------------------------
public class JLinksFrame extends JFISDialog {
	  static final long serialVersionUID=0;	
	  private JInferenceFrame Parent;
	  private JFIS fis;
	  private JExtendedDataFile trainingDataFile;
	  private JExtendedDataFile syntheticDataFile;
	  private JLabel jLabelThres = new JLabel();
	  private double linksThres;
	  private DoubleField jDFThres = new DoubleField();
	  private JPanel jPanelValidation = new JPanel(new GridBagLayout());
	  private JButton jButtonApply = new JButton();
	  private JButton jButtonCancel = new JButton();
	  private JLabel jLabelLinksFile = new JLabel();
	  private JTextField jTFLinksFile = new JTextField();
	  private String linksFile;
	  private JButton jButtonSelectLinksFile = new JButton("...");
	  private int NbRules;
	  private int NbActiveRules;
	  private double[] rulesCCFiringDegree;
	  private double[] rulesICFiringDegree;
	  private double[] rulesPositiveFiringDegree;
	  private double[] rulesNegativeFiringDegree;
	  private Integer[] itemsRulesNumbers;
	  private Vector<Integer>[] itemsRulesCorrectConclusion;
	  private Vector<Integer>[] itemsRulesIncorrectConclusion;
	  private Vector<Integer>[] rulesPositiveItems;
	  private Vector<Integer>[] rulesNeutralItems;
	  private Vector<Integer>[] rulesNegativeItems;
	  private int[] rulesToBeExpanded;
	  private int[] activeRules;
	  private boolean[] actives;
	  private int[] ruleLengths;
	  private int[] ruleOutputs;
	  private DecimalFormat df;
	  private String syntDataFileName=null;
	  private long ptrid;
	  private int[] outClasses;
	  private int[] NbPrem;
	  private int[] nper;
	  private double[] FR;
	  //private double[][] SFR;
	  private double cis;
	  private double cisc;
	  private double ciscpf;
	  private double LimMax= MainKBCT.getConfig().GetLimMaxIntIndex();
	  //private double LimMax= 10000; // LimMax > NbClasses * NbInputs * NbData
	  //private double LimMax= 40000; // LimMax > NbClasses * NbInputs * NbData
	  //private double LimMax= 10000000; // LimMax > NbClasses * NbInputs * NbData * 1000
	  //private int[] mMaskOC;
	  private boolean fingramsFlag;
	  private String fingramsMetric;
	  private String fingramsLayout;
	  private boolean smoteFlag;
	  private boolean regOpt;
	  private int[] samplesPerClass;
	  private boolean cancel= false;
	  //private double[] MSDoC;
	  private double[][] murules;
	  
	// ------------------------------------------------------------------------------
	public JLinksFrame(JInferenceFrame parent, JFIS fis, String data, boolean fingrams, boolean smote) {
		super(parent.parent);
		// String graphpath= System.getProperty("graphpath");
		// System.out.println(graphpath);
		this.ptrid = jnikbct.getId();
		this.fis = fis;
		this.Parent = parent;
		this.fingramsFlag = fingrams;
		
		if (this.fingramsFlag && (!MainKBCT.getConfig().GetTESTautomatic())) {
			Object ofm= MessageKBCT.SelectFingramsMetric(parent.parent);
			if (ofm!=null) {
			    this.fingramsMetric= ofm.toString();
			    if (this.fingramsMetric.equals(LocaleKBCT.GetString("MS")))
			        MainKBCT.getConfig().SetFingramsMetric("MS");
			    else if (this.fingramsMetric.equals(LocaleKBCT.GetString("MSFD")))
			        MainKBCT.getConfig().SetFingramsMetric("MSFD");
			    else
			    	MainKBCT.getConfig().SetFingramsMetric("MA");
			    //System.out.println("this.fingramsMetric -> "+this.fingramsMetric);
			    Object ofl= MessageKBCT.SelectFingramsLayout(parent.parent);
			    if (ofl!=null) {
			        this.fingramsLayout= ofl.toString();
			        MainKBCT.getConfig().SetFingramsLayout(this.fingramsLayout);
			        //System.out.println("this.fingramsLayout -> "+this.fingramsLayout);
			    } else {
			    	this.cancel= true;
			    }
			} else {
				this.cancel= true;
			}
		} else {
			this.fingramsMetric= MainKBCT.getConfig().GetFingramsMetric();
			this.fingramsLayout= MainKBCT.getConfig().GetFingramsLayout();
		}
		if (!this.cancel) {
		    this.smoteFlag = smote;
		    if (this.smoteFlag && (!MainKBCT.getConfig().GetTESTautomatic()))
			    this.setSMOTEparameters();

		    try {
            //if (this.fingramsFlag) {	
            	if (this.Parent.parent.Temp_kbct.GetOutput(1).GetClassif().equals("yes"))
			        this.regOpt= false;
            	else
			        this.regOpt= true;
            //}
			//System.out.println("this.regOpt -> "+this.regOpt);
			    this.trainingDataFile = new JExtendedDataFile(data + ".active", true);
			    this.samplesPerClass= new int[this.Parent.parent.Temp_kbct.GetOutput(1).GetLabelsNumber()];
			    double[] outs= this.trainingDataFile.VariableData(this.Parent.parent.Temp_kbct.GetNbActiveInputs());
			    if (!this.regOpt) {
				    // classification problem
				    for (int n=0; n<outs.length; n++) {
				    	 int ind= ((int)outs[n])-1;
				    	 if ( (ind >= 0) && (ind < this.samplesPerClass.length) )
					           this.samplesPerClass[ind]++;
				    	 else {
				    		 throw new Throwable("ClassifRegError");
				    		 //MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("ClassifRegError"));
				    		 //break;
				    	 }
				    }
			    } else {
			    	// regression problem
			    	JKBCTOutput jout= this.Parent.parent.Temp_kbct.GetOutput(1);
				    for (int n=0; n<outs.length; n++) {
				    	 int labFired= jout.GetLabelFired(outs[n]);
				    	 if (labFired>=1)
					         this.samplesPerClass[labFired-1]++;
				    }
			    }
				//for (int n=0; n<this.samplesPerClass.length; n++) {
				  //   System.out.println("this.samplesPerClass["+n+"]= "+this.samplesPerClass[n]);
				//}
		   	    jbInit();
			    //System.out.println("MainKBCT.getConfig().GetTESTautomatic() -> "+MainKBCT.getConfig().GetTESTautomatic());
	            if (MainKBCT.getConfig().GetTESTautomatic())
                    this.jButtonApply_actionPerformed();
	            
		    } catch (Throwable en) {
		    	if (en.getMessage().equals("ClassifRegError")) {
		    		MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("ClassifRegError"));
	    		} else {
			        en.printStackTrace();
			        MessageKBCT.Error(null, en);
		    	}
		    }
		}
	}
	// ------------------------------------------------------------------------------
	private void jbInit() throws Exception {
		this.getContentPane().setLayout(new GridBagLayout());
		// fichiers liens
		this.jTFLinksFile.setText(this.trainingDataFile.FileName());
		this.jButtonSelectLinksFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonSelectLinksFile_actionPerformed();
			}
		});
		this.getContentPane().add(this.jLabelLinksFile,
				new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 0, 5), 0, 0));
		this.getContentPane().add(this.jTFLinksFile,
				new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(5, 0, 0, 5), 20, 0));
		this.getContentPane().add(this.jButtonSelectLinksFile,
				new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(5, 5, 0, 5), 0, -10));

		this.getContentPane().add(jLabelThres,
				new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 0, 5), 0, 0));
		this.jDFThres.NumberFormat().setMaximumFractionDigits(10);
		this.jDFThres.setValue(1e-6);
		this.getContentPane().add(jDFThres,
				new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(5, 0, 0, 5), 40, 0));

		// panel validation
		jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelValidation.add(jButtonCancel, new GridBagConstraints(1, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(jPanelValidation,
				new GridBagConstraints(0, 3, 4, 1, 1.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));

		// events
		jButtonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonApply_actionPerformed();
			}
		});
		jButtonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonCancel_actionPerformed();
			}
		});

		this.Translate();
		this.pack();
		this.setModal(true);
		this.setLocation(JChildFrame.ChildPosition(this.Parent, this.getSize()));
        if (!MainKBCT.getConfig().GetTESTautomatic())
		    this.setVisible(true);
        else
		    this.setVisible(false);
	}
	// ------------------------------------------------------------------------------
	void jButtonSelectLinksFile_actionPerformed() {
		JOpenFileChooser file_chooser = new JOpenFileChooser(MainKBCT.getConfig().GetDataFilePath());
		if (this.jTFLinksFile.getText().equals(""))
			file_chooser.setSelectedFile(new File(this.trainingDataFile.FileName()));
		else
			file_chooser.setSelectedFile(new File(this.jTFLinksFile.getText()));

		file_chooser.setAcceptAllFileFilterUsed(true);
		if (file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			this.jTFLinksFile.setText(file_chooser.getSelectedFile().getAbsolutePath());
	}
	// ------------------------------------------------------------------------------
	private void Translate() {
		this.setTitle(LocaleKBCT.GetString("Links"));
		this.jLabelLinksFile.setText(LocaleKBCT.GetString("PrefixLinksFiles") + ":");
		jLabelThres.setText(LocaleKBCT.GetString("Threshold") + ":");
		jButtonApply.setText(LocaleKBCT.GetString("Apply"));
		jButtonCancel.setText(LocaleKBCT.GetString("Cancel"));
	}
	// ------------------------------------------------------------------------------
	void jButtonCancel_actionPerformed() {
		this.dispose();
	}
	// ------------------------------------------------------------------------------
	void jButtonApply_actionPerformed() {
		try {
			//System.out.println("JLinksFrame: jButtonApply");
			//jnifis.FisproPath(MainKBCT.getConfig().GetKbctPath());
			jnifis.setUserHomeFisproPath(MainKBCT.getConfig().GetKbctPath());
			this.linksFile = this.jTFLinksFile.getText();
			this.linksThres = this.jDFThres.getValue();
			this.dispose();
            int selSample= -1;
            if (this.fingramsFlag)
            	selSample=	MainKBCT.getConfig().GetFingramsSelectedSample();
            
            //System.out.println("selSample -> "+selSample);
            if (!MainKBCT.getConfig().GetTESTautomatic()) {
                if (this.fingramsFlag && (selSample < 0) ) {
                    //System.out.println("o1");
                    Integer op= (Integer)MessageKBCT.SelectSampleForInstanceBasedFingrams(this,this.trainingDataFile.GetActiveCount());
                    if (op!=null) {
                        //System.out.println("op -> "+op.intValue());
                        selSample= op.intValue();
                        MainKBCT.getConfig().SetFingramsSelectedSample(selSample);
                    }
                } /*else {
                    //System.out.println("o2");
                	//MainKBCT.getConfig().SetFingramsSelectedSample(-1);
                }*/
			    MessageKBCT.Information(this, LocaleKBCT.GetString("Processing")
					+ " ..." + "\n" + "   "
					+ LocaleKBCT.GetString("TakeLongTime") + "\n" + "... "
					+ LocaleKBCT.GetString("PleaseWait") + " ...");
            }
            /*if (this.fingramsFlag && MainKBCT.getConfig().GetTutorialFlag() && !MainKBCT.getConfig().GetFINGRAMSautomatic()) {
            	//System.out.println("JLinksFrame: TUTORIAL");
                MainKBCT.getConfig().SetFingramsSelectedSample(1);
                MainKBCT.getConfig().SetFingramsLayout(LocaleKBCT.DefaultFingramsLayout());
                MainKBCT.getConfig().SetFingramsMetric(LocaleKBCT.DefaultFingramsMetric());
            }*/
            if (MainKBCT.getConfig().GetFINGRAMSautomatic())
            	MainKBCT.getConfig().SetFINGRAMSautomatic(false);

            //Date d= new Date(System.currentTimeMillis());
			//System.out.println("T1 -> "+DateFormat.getDateTimeInstance().format(d));
			this.fis.Links(this.trainingDataFile, this.linksThres, false, this.linksFile, null);
	        //d= new Date(System.currentTimeMillis());
			//System.out.println("T2 -> "+DateFormat.getDateTimeInstance().format(d));
			//this.readRulesItemsFile(this.linksFile + "." + jnifis.LinksRulesItemsExtension());
	        //d= new Date(System.currentTimeMillis());
		    //System.out.println("T3 -> "+DateFormat.getDateTimeInstance().format(d));
			this.df = new DecimalFormat();
			this.df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
			this.df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
			DecimalFormatSymbols dfs = this.df.getDecimalFormatSymbols();
			dfs.setDecimalSeparator((new String(".").charAt(0)));
			this.df.setDecimalFormatSymbols(dfs);
			this.df.setGroupingSize(20);
		    this.buildRulesPositiveNegativeItemsFile(this.linksFile);
			String file=null;
            if (this.smoteFlag) {
			    file = this.Parent.parent.getTitle().substring(4) + "." + jnifis.LinksRulesLinksExtension() + ".smote.fs";
            } else  {
			    file = this.Parent.parent.getTitle().substring(4) + "." + jnifis.LinksRulesLinksExtension() + ".fs";
			}
			//PrintStream ps = new PrintStream(new FileOutputStream(file, false));
			if (this.fingramsFlag) {
		        //d= new Date(System.currentTimeMillis());
				//System.out.println("T4 -> "+DateFormat.getDateTimeInstance().format(d));
				// remove this line after updating fingramsGenerator
				//this.buildScienceMapMatrixLinksRulesFile(file, ps);
				// ps.println("NbRules="+this.NbRules);
				// ps.println("NbData="+this.syntheticDataFile.DataLength());
				// String[] mapdotfiles= this.buildGraphDot(this.linksFile + "."
				// + jnifis.LinksRulesLinksExtension()+".scienceMap");
		        //d= new Date(System.currentTimeMillis());
				//System.out.println("T5 -> "+DateFormat.getDateTimeInstance().format(d));

				//String[] mapdotfiles = this.buildGraphDot(file);
				//this.df.setMaximumFractionDigits(5);
				//this.df.setMinimumFractionDigits(5);
		        //d= new Date(System.currentTimeMillis());
				//System.out.println("T6 -> "+DateFormat.getDateTimeInstance().format(d));
				/*if ( (this.fingramsMetric.equals(LocaleKBCT.GetString("MS"))) || (this.fingramsMetric.equals(LocaleKBCT.GetString("All"))) ) {
				      ps.println("CI(S)=" + this.df.format(this.cis));
				      if (!this.regOpt) {
					      ps.println("CI(SC)=" + this.df.format(this.cisc));
					      ps.println("CI(SC-MPF)=" + this.df.format(this.ciscpf));
				      }
				}
				this.df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
				this.df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
				ps.flush();
				ps.close();*/
				
		        //d= new Date(System.currentTimeMillis());
				//System.out.println("T7 -> "+DateFormat.getDateTimeInstance().format(d));
				//this.buildGraphPICTURE(mapdotfiles);
				
				// CALL FINGRAM GENERATOR
				String graphvizPath=System.getProperty("graphpath")+System.getProperty("file.separator");
				String guajePath=MainKBCT.getConfig().GetKbctPath();
                String cpathfingram= MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "vismaps"
						+ System.getProperty("file.separator") + "fingramsGenerator.jar";

                String sep=";";
                if (!LocaleKBCT.isWindowsPlatform())
                	sep=":";

                String cpathbatik= MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-anim.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-awt-util.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-bridge.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-codec.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-css.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-dom.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-ext.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-extension.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-gui-util.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-gvt.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-parser.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-script.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-svg-dom.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-svggen.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-swing.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-transcoder.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-util.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "batik-xml.jar"
						+ sep
						+ MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "batik"
						+ System.getProperty("file.separator") + "xml-apis-ext.jar";

                String cpathjama= MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "jamalib"
						+ System.getProperty("file.separator") + "Jama-1.0.2.jar";

                String cpathexec= MainKBCT.getConfig().GetKbctPath()
						+ System.getProperty("file.separator") + "libs"
						+ System.getProperty("file.separator") + "exec"
						+ System.getProperty("file.separator") + "commons-exec-1.3.jar";

                String cpath= cpathfingram+sep+cpathbatik+sep+cpathjama+sep+cpathexec;
    			//String fsfile= this.linksFile+"."+jnifis.LinksItemsRulesExtension()+".fs";
    			String fsfile= this.Parent.parent.getTitle().substring(4) + "." + jnifis.LinksRulesLinksExtension() +".fs";
    			//String GetFingramsMetric()
    			// Estamos usando siempre simetrica (basica) -> 0
    			// Poner 1 para asimetrica
    			int metric=0;
    			//System.out.println("this.fingramsMetric: "+this.fingramsMetric);
    			if (this.fingramsMetric.equals("MSFD"))
    				metric=1;
    			else if (this.fingramsMetric.equals("MA"))
    				metric=2;
    				
    			//String command = "java -Xmx512m -Dgraphpath=\"" + graphvizPath + "\" -Dpathfinderpath="+pathfinderPath+" -Dfingrampath="+guajePath+" -Dlog4j.configuration=file:config"+System.getProperty("file.separator")+"log4j.properties -classpath "+cpath+" main.Main --input "+fsfile+" -q "+MainKBCT.getConfig().GetPathFinderParQ()+" --metric "+metric;
                //String command = "java -Xmx512m -Dpathfinderpath="+pathfinderPath+" -Dfingrampath="+guajePath+" -Dlog4j.configuration=file:config"+System.getProperty("file.separator")+"log4j.properties -classpath "+cpath+" main.Main --input "+fsfile+" -q "+MainKBCT.getConfig().GetPathFinderParQ()+" --metric "+metric+" --G "+MainKBCT.getConfig().GetLimMaxIntIndex();
                //String command = "java -Xmx512m -Dpathfinderpath="+pathfinderPath+" -Dfingrampath="+guajePath+" -Dlog4j.configuration=file:config"+System.getProperty("file.separator")+"log4j.properties -classpath "+cpath+" main.Main --input "+fsfile+" -q "+MainKBCT.getConfig().GetPathFinderParQ()+" --metric "+metric+" --noWindow";
                //String command = "java -Xmx512m -Dgraphpath=\"" + graphvizPath.substring(0,graphvizPath.length()-1) + "\"" + " -Dpathfinderpath="+pathfinderPath+" -Dfingrampath="+guajePath+" -Dlog4j.configuration=file:config"+System.getProperty("file.separator")+"log4j.properties -classpath "+cpath+" main.Main --input "+fsfile+" -q "+MainKBCT.getConfig().GetPathFinderParQ()+" --metric "+metric+" --noWindow";
    			//String command = "java -Xmx512m -Dgraphpath=\"" + graphvizPath.substring(0,graphvizPath.length()-1) + "\"" + " -Dpathfinderpath="+pathfinderPath+" -Dfingrampath="+guajePath+" -Dlog4j.configuration=file:config"+System.getProperty("file.separator")+"log4j.properties -classpath "+cpath+" main.Main"+" --drawing "+MainKBCT.getConfig().GetFingramsLayout()+" --guaje "+MainKBCT.getConfig().GetLimMaxIntIndex()+" --input "+fsfile+" --metric "+metric+" -q "+MainKBCT.getConfig().GetPathFinderParQ()
    			int qaux= MainKBCT.getConfig().GetPathFinderParQ();
    			//System.out.println("this.NbRules -> "+this.fis.NbRules());
    			if (qaux >= this.fis.NbRules()) {
    				  MainKBCT.getConfig().SetPathFinderParQ(LocaleKBCT.DefaultPathFinderParQ());        	  
    			}
    			String command = "java -Xmx512m -Dgraphpath=\"" + graphvizPath.substring(0,graphvizPath.length()-1) + "\"" + " -Dfingrampath="+guajePath+" -Dlog4j.configuration=file:config"+System.getProperty("file.separator")+"log4j.properties -classpath "+cpath+" main.Main"+" --drawing "+MainKBCT.getConfig().GetFingramsLayout()+" --guaje "+MainKBCT.getConfig().GetLimMaxIntIndex()+" --input \""+fsfile+"\" --metric "+metric+" -q "+MainKBCT.getConfig().GetPathFinderParQ();    			
                if (selSample >= 0) {
                    command= command + " -e "+selSample;
                	//MainKBCT.getConfig().SetFingramsSelectedSample(-1);
                }
				//System.out.println();
				//System.out.println("graphvizPath="+graphvizPath);
				//System.out.println("command="+command);
				//int exitVal = this.runProcess(command, "tempOutFingramsGen.txt");
                //System.out.println("exitVal="+exitVal);			
				this.runProcess(command, "tempOutFingramsGen.txt");
				//this.printFingramsLegend(this.Parent.parent.getTitle().substring(4) + "." + jnifis.LinksRulesLinksExtension());
			}
			// opening console windows
			for (int n = 0; n < this.Parent.jfc.length - 1; n++) {
				//if (this.Parent.jfc[n] != null)
					//this.Parent.jfc[n].dispose();
				String fileToDisplay = null;
				switch (n) {
				case 0:
					fileToDisplay = this.linksFile + "." + jnifis.LinksRulesItemsExtension();
					break;
				case 1:
					//fileToDisplay = this.linksFile + "." + jnifis.LinksItemsRulesExtension() + ".fs";
					fileToDisplay = this.Parent.parent.getTitle().substring(4) + "." + jnifis.LinksRulesLinksExtension() + ".fs";
					break;
				case 2:
					fileToDisplay = this.linksFile + "." + jnifis.LinksItemsRulesExtension();
					break;
				case 3:
					fileToDisplay = this.linksFile + ".items.murules";
					break;
				case 4:
					fileToDisplay = this.linksFile + "." + jnifis.LinksRulesLinksExtension();
					break;
				case 5:
                    if (!this.smoteFlag)
					    fileToDisplay = this.Parent.parent.getTitle().substring(4)+ "." + jnifis.LinksRulesLinksExtension()+ ".scienceMap";
					    break;
				case 6:
                    if (this.smoteFlag)
					    fileToDisplay = this.Parent.parent.getTitle().substring(4)+ "." + jnifis.LinksRulesLinksExtension()+ ".smote.scienceMap";
				        break;
				default:
					fileToDisplay = null;
					break;
				}
				if (fileToDisplay != null) {
					// System.out.println("n="+n+"  fileToDisplay="+fileToDisplay);
					if ((n < 5) && (!this.fingramsFlag)) {
						this.Parent.jfc[n] = new JFISConsole(this.Parent.parent, fileToDisplay, false);
					} else if ( (n>=5) && (this.fingramsFlag) ) {
						if (fileToDisplay!=null)
						    this.Parent.jfc[n] = new JFISConsole(this.Parent.parent, fileToDisplay, true);
						//else
							//this.Parent.jfc[n] = null;
					}
					if (n > 0) {
						if ( (!this.fingramsFlag) && (this.Parent.jfc[n]!=null) )
							this.Parent.jfc[n].setLocation(
											this.Parent.jfc[n].getX() + n*this.Parent.jfc[n].getInsets().top,
											this.Parent.jfc[n].getY() + n*this.Parent.jfc[n].getInsets().top);
						else if (this.fingramsFlag) {
							if ( (n >= 5) && (this.Parent.jfc[n]!=null) )
								this.Parent.jfc[n].setLocation(
										this.Parent.jfc[n].getX() + this.Parent.jfc[n].getInsets().top,
										this.Parent.jfc[n].getY() + this.Parent.jfc[n].getInsets().top);
						}
					}
				}
			}
		} catch (Throwable except) {
		      if (except.getMessage().equals("SelectAGraphvizPath")) {
		       	    String ReadTXT= "ReadTXT";
		       	    String msg;
		       	    if (LocaleKBCT.isWindowsPlatform())
		       	        msg= "YouMustModifyKbctBat";
		       	    else
		       	        msg= "YouMustModifyKbctMake";

		           Throwable exc= new Throwable(LocaleKBCT.GetString(except.getMessage())+"\n"+
		                                        LocaleKBCT.GetString(msg)+"\n"+
		                                        LocaleKBCT.GetString(ReadTXT));
		           MessageKBCT.Error(null, exc);
		      } else {			
			      except.printStackTrace();
			      MessageKBCT.Error(null, except);
		      }
		}
		jnikbct.cleanHashtable(ptrid);
	}
	// ------------------------------------------------------------------------------
	/*void readRulesItemsFile(String file) {
		try {
			LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(file)));
			String l;
			int NR = (new Integer(lnr.readLine()).intValue());
			this.itemsRulesNumbers = new Integer[NR];
			this.itemsRulesCorrectConclusion = new Vector[NR];
			this.itemsRulesIncorrectConclusion = new Vector[NR];
			lnr.readLine();
			for (int n = 0; n < NR; n++) {
				l = lnr.readLine();
				int pos = l.indexOf(",");
				if (pos == -1) {
					pos = l.length();
				}
				String aux = l.substring(0, pos);
				int ruleNumber = new Integer(aux).intValue();
				JRule r = this.fis.GetRule(ruleNumber - 1);
				double[] outputs = r.Actions();
				int cont = 1;
				this.itemsRulesNumbers[n] = new Integer(ruleNumber);
				this.itemsRulesCorrectConclusion[n] = new Vector<Integer>();
				this.itemsRulesIncorrectConclusion[n] = new Vector<Integer>();
				while (l != null) {
					pos = l.indexOf(",");
					if (pos == -1) {
						pos = l.length();
					}
					aux = l.substring(0, pos);
					if (cont == 3) {
						if (aux.equals("0"))
							pos = l.length();
					} else if (cont > 3) {
						int item = (new Integer(aux).intValue());
						boolean incorrectConclusion = false;
						for (int m = 0; m < outputs.length; m++) {
							int var = this.trainingDataFile.VariableCount()	- outputs.length + m;
							double output = this.trainingDataFile.VariableData(var)[item - 1];
							//if ( (item==3) || (item==6) ) {
								//System.out.println("ruleNumber -> "+ruleNumber);
								//System.out.println("item"+item+" -> "+output);
							//}
							// Nature -> 0 (fuzzy)
							// Nature -> 1 (crisp)
							JOutput jout = fis.GetOutput(m);
							//System.out.println("jout.GetClassif() -> "+jout.GetClassif());
							if (jout.GetNature() == JOutput.FUZZY) {
								if (jout.GetClassif()) {
								  int NbMF = jout.GetNbMF();
							      int SelLab = NbMF;
							      for (int i = 0; i < NbMF - 1; i++) {
								     double[] par = jout.GetMF(i).GetParams();
								     double lim = 0;
								     if (par.length == 3) {
									     lim = (par[1] + par[2]) / 2;
								     } else if (par.length == 4) {
									     lim = (par[2] + par[3]) / 2;
								     }
								     if (output < lim) {
									     SelLab = i + 1;
									     break;
								     }
							      }
								  //if ( (item==3) || (item==6) ) {
									//System.out.println("outputs[m] -> "+outputs[m]);
									//System.out.println("SelLab -> "+SelLab);
								  //}
							      if (outputs[m] != SelLab)
								    incorrectConclusion = true;
								  // /*if (jout.GetClassif()) {
									// classification problem
								    if (outputs[m] != SelLab)
									    incorrectConclusion = true;
								  } else {
									// regression problem
									double[] par = jout.GetMF(SelLab-1).GetParams();
							        double lowerlim = 0;
									double upperlim = 0;
									if (par.length == 3) {
									    lowerlim = (par[0] + par[1]) / 2;
									    upperlim = (par[1] + par[2]) / 2;
									} else if (par.length == 4) {
									    lowerlim = (par[0] + par[1]) / 2;
									    upperlim = (par[2] + par[3]) / 2;
									}
									if ( (item==3) || (item==6) ) {
										System.out.println("lowerlim -> "+lowerlim);
										System.out.println("upperlim -> "+upperlim);
									}
									if ( (outputs[m] < lowerlim) || (outputs[m] > upperlim) ) {
								    	 incorrectConclusion = true;
									}
								  }//*/
								  //if ( (item==3) || (item==6) )
									//System.out.println("incorrectConclusion -> "+incorrectConclusion);
								/*} ///*else {
									if (murules[i][ra] < MainKBCT.getConfig().GetBlankThres()) {
										incorrectConclusion = true;
								}//*/
							/*} else {
								if (outputs[m] != output) {
								    incorrectConclusion = true;
								}
							}
						}
						if (!incorrectConclusion) {
							this.itemsRulesCorrectConclusion[n].add(new Integer(item));
						} else {
							this.itemsRulesIncorrectConclusion[n].add(new Integer(item));
						}
					}
					if (pos == l.length()) {
						l = null;
					} else {
						l = l.substring(pos + 2);
					}
					cont++;
				}
			}
		} catch (Throwable except) {
			except.printStackTrace();
			MessageKBCT.Error(null, except);
		}
	}*/
	// ------------------------------------------------------------------------------
	double readRulesIF(String file) {
		double result = 0;
		try {
			LineNumberReader lnr = new LineNumberReader(new InputStreamReader(
					new FileInputStream(file)));
			lnr.readLine(); // numero de reglas
			lnr.readLine(); // numero maximo de datos manejados por una regla
			String l;
			while ((l = lnr.readLine()) != null) {
				int pos = l.indexOf(",");
				String aux = l.substring(pos + 1);
				pos = aux.indexOf(",");
				result = result	+ new Double(aux.substring(0, pos)).doubleValue();
			}
		} catch (Throwable except) {
			except.printStackTrace();
			MessageKBCT.Error(null, except);
		}
		return result;
	}
	// ------------------------------------------------------------------------------
	/*private double CumulatedWeights(JExtendedDataFile jedfaux, int[] outputs, JFIS fis, boolean CC) {
		double ruleWeight = 0;
		try {
			// Desactivar los datos cuya conclusion no coincide con la de la
			// regla
			int contInactiveData = 0;
			boolean AllInactive = false;
			for (int k = 0; k < jedfaux.DataLength(); k++) {
				boolean incorrectConclusion = false;
				for (int m = 0; m < outputs.length; m++) {
					int var = jedfaux.VariableCount() - outputs.length + m;
					double output = jedfaux.VariableData(var)[k];
					// Nature -> 0 (fuzzy)
					// Nature -> 1 (crisp)
					JOutput jout = fis.GetOutput(m);
					if (jout.GetNature() == JOutput.FUZZY) {
						int NbMF = jout.GetNbMF();
						int SelLab = NbMF;
						for (int i = 0; i < NbMF - 1; i++) {
							double[] par = jout.GetMF(i).GetParams();
							double lim = 0;
							if (par.length == 3) {
								lim = (par[1] + par[2]) / 2;
							} else if (par.length == 4) {
								lim = (par[2] + par[3]) / 2;
							}
							if (output < lim) {
								SelLab = i + 1;
								break;
							}
						}
						if (outputs[m] != SelLab)
							incorrectConclusion = true;
					} else if (outputs[m] != output) {
						incorrectConclusion = true;
					}
				}
				if (((CC) && (incorrectConclusion))	|| ((!CC) && (!incorrectConclusion))) {
					contInactiveData++;
					if (contInactiveData == jedfaux.DataLength())
						AllInactive = true;
					else
						jedfaux.SetActive(k + 1, false);
				}
			}
			if (!AllInactive) {
				// File faux= new File(jedfaux.ActiveFileName());
				// System.out.println("faux="+faux.getAbsolutePath());
				// System.out.println("jedfaux="+jedfaux.FileName());
				// if (faux.exists())
				fis.Links(jedfaux, 1e-6, false, jedfaux.ActiveFileName(), null);
				// else
				// fis.Links(jedfaux, 1e-6, false, jedfaux.FileName(), null);

				ruleWeight = this.readRulesIF(jedfaux.ActiveFileName() +"."+ jnifis.LinksRulesItemsExtension());
			}
		} catch (Throwable t) {
			t.printStackTrace();
			MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);
		}
		return ruleWeight;
	}*/
	// ------------------------------------------------------------------------------
	void buildRulesPositiveNegativeItemsFile(String basefile) {
		try {
			String infile= basefile+".items.murules";
			//System.out.println("infile -> "+infile);
			//String outfile= basefile+"."+jnifis.LinksItemsRulesExtension()+".fs";
			String outfile= this.Parent.parent.getTitle().substring(4) + "." + jnifis.LinksRulesLinksExtension() + ".fs";
			//System.out.println("outfile -> "+outfile);
			JExtendedDataFile jedfaux = new JExtendedDataFile(this.trainingDataFile.FileName(), true);
			int NbData= jedfaux.DataLength();
			//int NbAcRs = this.Parent.parent.Temp_kbct.GetNbActiveRules();
			int NbAcRs = MainKBCT.getJMF().jef.Temp_kbct.GetNbActiveRules();
			//System.out.println("NbAcRs -> "+NbAcRs);
			this.murules= new double[NbData][NbAcRs];
			LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(infile)));
			String l;
			int dcont=0;
			while ((l = lnr.readLine()) != null) {
				   String[] splits= l.split(", ");
				   for (int n=0; n<NbAcRs; n++) {
					    murules[dcont][n]= (new Double(splits[n+1])).doubleValue();    
				   }
				   dcont++;
			}
			lnr.close();
			int NbRs = this.Parent.parent.Temp_kbct.GetNbRules();
			double[] ruleOuts= new double[NbRs];
			boolean[] actRules= new boolean[NbRs];
			String[] ruleDesc= new String[NbRs];
			for (int n=0; n<NbRs; n++) {
                 //System.out.println(murules[1][n]);
				 Rule r= this.Parent.parent.Temp_kbct.GetRule(n+1);
				 ruleOuts[n]= r.Get_out_labels_number()[0];
				 actRules[n]= r.GetActive();
				 ruleDesc[n]= this.Parent.parent.Temp_kbct.GetRuleDescription(n+1, "fingramViewer");
			}
			int NbIns = this.Parent.parent.Temp_kbct.GetNbInputs();
			double[] outs= jedfaux.VariableData(NbIns);
            this.rulesCCFiringDegree= new double[NbRs];
            this.rulesICFiringDegree= new double[NbRs];
            this.rulesPositiveFiringDegree= new double[NbRs];
            this.rulesNegativeFiringDegree= new double[NbRs];
			int[] itemsUncovered= new int[NbData];
            //boolean warning= false;
		    JKBCTOutput jout= this.Parent.parent.Temp_kbct.GetOutput(1);
		    String[] outLabNames= null;
		    if (jout.GetScaleName().equals("user"))
		    	outLabNames= jout.GetUserLabelsName();
		    else {
		    	String[] aux= jout.GetLabelsName(); 
		    	outLabNames= new String[aux.length];
		    	for (int n=0; n<aux.length; n++) {
		    		 //outLabNames[n]= LocaleKBCT.GetString(aux[n]);
		    		 outLabNames[n]= aux[n];
		    	}
		    }
		    int nol= jout.GetLabelsNumber();
		    int[] contOuts= new int[nol];
		    if (!this.regOpt) {
		        for (int n=0; n<outs.length; n++) {
		    	     contOuts[((int)outs[n])-1]++;
		        }
		    }
            //if ( (jout.GetType().equals("numerical")) && (jout.GetClassif().equals("no")) ) {
			  this.itemsRulesNumbers = new Integer[NbRs];
         	  this.itemsRulesCorrectConclusion= new Vector[NbRs];
            	  this.itemsRulesIncorrectConclusion= new Vector[NbRs];
                  for (int n=0; n<NbRs; n++) {
      				   this.itemsRulesNumbers[n] = new Integer(n+1);
				       this.itemsRulesCorrectConclusion[n]= new Vector<Integer>();
				       this.itemsRulesIncorrectConclusion[n]= new Vector<Integer>();
                  }
                  //warning= true;
            //}
      	    this.rulesPositiveItems= new Vector[NbRs];
      	    this.rulesNegativeItems= new Vector[NbRs];
      	    this.rulesNeutralItems= new Vector[NbRs];
            for (int n=0; n<NbRs; n++) {
			       this.rulesPositiveItems[n]= new Vector<Integer>();
			       this.rulesNegativeItems[n]= new Vector<Integer>();
			       this.rulesNeutralItems[n]= new Vector<Integer>();
            }
			int contUC=0;
			for (int i=0; i<NbData; i++) {
				int ra=0;
				boolean dcov=false;
				for (int j=0; j<NbRs; j++) {
					 if (actRules[j]) {
						 if (murules[i][ra] > MainKBCT.getConfig().GetBlankThres()) {
							 dcov= true;
                             double outmdeg= 0; 
							 boolean cc= false;
							 //int SelLab=0;
							 if (this.regOpt) {
								 if (jout.GetType().equals("numerical")) {
									    //int NbMF = jout.GetLabelsNumber();
										//int SelLab = jout.GetLabelFired(outs[i]);
										//System.out.println("rule -> "+String.valueOf(ra+1));
										//System.out.println("   ruleOuts[j] -> "+ruleOuts[j]);
										//System.out.println("   SelLab -> "+SelLab);
										//System.out.println("   murules[i][ra] -> "+murules[i][ra]);
										//System.out.println("   BT -> "+MainKBCT.getConfig().GetBlankThres());
										//if (murules[i][ra] >= MainKBCT.getConfig().GetBlankThres()) {
										//if (murules[i][ra] > 0.3) {
										//if (ruleOuts[j] == SelLab) {
									    int labNum= (int)ruleOuts[j];
										LabelKBCT lab= jout.GetLabel(labNum);
										outmdeg= lab.getMembershipDegree(outs[i]);
										double[] par= lab.GetParams();
										if ( (outs[i] > par[0]) && (outs[i] < par[par.length-1]) ) {
											cc = true;
										}
								 } else {
									 if (outs[i]==jout.GetLabel((int)ruleOuts[j]).GetP1()) {
										 cc= true;
										 outmdeg= 1;
									 }
								 }
							 } else {
								 if (outs[i]==ruleOuts[j]) {
									 cc= true;
									 outmdeg= 1;
								 }
							 }
						     if (cc) {
						    	 double omin= Math.min(murules[i][ra], outmdeg);
								 if (omin < MainKBCT.getConfig().GetGoodnessLowThreshold()) {
									 this.rulesNegativeItems[j].add(new Integer(i+1));
							    	 //this.rulesNegativeFiringDegree[j]= this.rulesNegativeFiringDegree[j] + omin;
							    	 this.rulesNegativeFiringDegree[j]= this.rulesNegativeFiringDegree[j] + murules[i][ra];
								 } else if (omin > MainKBCT.getConfig().GetGoodnessHighThreshold()) {
									 this.rulesPositiveItems[j].add(new Integer(i+1));
							    	 //this.rulesPositiveFiringDegree[j]= this.rulesPositiveFiringDegree[j] + omin;
							    	 this.rulesPositiveFiringDegree[j]= this.rulesPositiveFiringDegree[j] + murules[i][ra];
								 } else {
									 this.rulesNeutralItems[j].add(new Integer(i+1));
								 }
						    	 this.rulesCCFiringDegree[j]= this.rulesCCFiringDegree[j] + murules[i][ra];
						    	 //if (warning) 
									this.itemsRulesCorrectConclusion[j].add(new Integer(i+1));
						     } else {
								 this.rulesNegativeItems[j].add(new Integer(i+1));
						    	 this.rulesNegativeFiringDegree[j]= this.rulesNegativeFiringDegree[j] + murules[i][ra];
						    	 this.rulesICFiringDegree[j]= this.rulesICFiringDegree[j] + murules[i][ra];
						    	 //if (warning) 
									this.itemsRulesIncorrectConclusion[j].add(new Integer(i+1));
						     }
							 /*if ( (j==20) && (i==127 || i==371 || i==382) ) {
                                 System.out.println("ruleOuts[j] -> "+ruleOuts[j]);
                                 System.out.println("  outs[i] -> "+outs[i]);
                                 System.out.println("  SelLab -> "+SelLab);
                                 System.out.println("  ruleWeightsCC[j] -> "+ruleWeightsCC[j]);
                                 System.out.println("  ruleWeightsIC[j] -> "+ruleWeightsIC[j]);
                                 System.out.println();
							 }*/
						 }
						 ra++;
					 }
				}
				 if (!dcov) {
					 itemsUncovered[i]= 1;
					 contUC++;
				 } else {
					 itemsUncovered[i]= 0;
				 }
			}
			PrintStream ps = new PrintStream(new FileOutputStream(outfile, false));
		    if (this.regOpt) {
			    ps.println("Regression");
			    if (outLabNames!=null) {
				      for (int n=0; n<nol; n++) {
				    	   ps.print(outLabNames[n]);
				    	   if (n<nol-1)
				    		   ps.print(",");
				      }
					  //ps.println("("+NbData+")");
					  ps.println();
				    }
			} else {
			    ps.println("Classification");
			    if (outLabNames!=null) {
			      for (int n=0; n<nol; n++) {
			    	   ps.print(outLabNames[n]+"("+contOuts[n]+")");
			    	   if (n<nol-1)
			    		   ps.print(",");
			      }
				  ps.println();
			    }
			}
			ps.println();
			//ps.println(LocaleKBCT.GetString("BlankThres") + ": " + MainKBCT.getConfig().GetBlankThres());
			//ps.println(LocaleKBCT.GetString("GoodnessThres") + " ("+LocaleKBCT.GetString("high")+"): " + MainKBCT.getConfig().GetGoodnessHighThreshold());
			//ps.println(LocaleKBCT.GetString("GoodnessThres") + " ("+LocaleKBCT.GetString("low")+"): " + MainKBCT.getConfig().GetGoodnessLowThreshold());
			ps.println("Blank Threshold: " + MainKBCT.getConfig().GetBlankThres());
			ps.println("Goodness Threshold" + " ("+LocaleKBCT.GetString("high")+"): " + MainKBCT.getConfig().GetGoodnessHighThreshold());
			ps.println("Goodness Threshold" + " ("+LocaleKBCT.GetString("low")+"): " + MainKBCT.getConfig().GetGoodnessLowThreshold());
			ps.println();
			//ps.println(LocaleKBCT.GetString("Rules") + ": " + NbAcRs);
			if (contUC>0)
			    ps.println("Rules" + ": " + String.valueOf(NbAcRs+1));
			else
			    ps.println("Rules" + ": " + String.valueOf(NbAcRs));
				
			ps.println("Examples" + ": " + NbData);
			ps.println();
			//int ra = 0;
			this.df.setMaximumFractionDigits(5);
			this.df.setMinimumFractionDigits(5);
			int ra=0;
		   	for (int n = 0; n < NbRs; n++) {
				     if (actRules[n]) {
					     //ps.println(LocaleKBCT.GetString("Rule")+ String.valueOf(n + 1)+": "+ruleDesc[n]);
					     //ps.print(LocaleKBCT.GetString("CorrectConclusion")	+ "  => ");
					     ps.println("Rule"+ String.valueOf(n + 1)+": "+ruleDesc[n]);
					     ps.print("Correct Conclusion  => ");
					     double ratioCC= 0;
				         if ( (this.rulesCCFiringDegree[n] > 0) && (this.itemsRulesCorrectConclusion[n].size() > 0) )
				    	     ratioCC= this.rulesCCFiringDegree[n]/this.itemsRulesCorrectConclusion[n].size();
					     
				         ps.print("(" + this.df.format(this.rulesCCFiringDegree[n]) + ") (" + this.df.format(ratioCC) + ")" + "  => ");
					     Enumeration cc= this.itemsRulesCorrectConclusion[n].elements();
					     this.printItems(cc, ps, ra, murules);

					     //ps.print(LocaleKBCT.GetString("IncorrectConclusion")+ "  => ");
					     ps.print("Incorrect Conclusion  => ");
					     double ratioIC= 0;
				         if ( (this.rulesICFiringDegree[n] > 0) && (this.itemsRulesIncorrectConclusion[n].size() > 0) )
				    	   ratioIC=this.rulesICFiringDegree[n]/this.itemsRulesIncorrectConclusion[n].size();

				         ps.print("(" + this.df.format(this.rulesICFiringDegree[n]) + ") (" + this.df.format(ratioIC) + ")" + "  => ");
				         Enumeration ic= this.itemsRulesIncorrectConclusion[n].elements();
					     this.printItems(ic, ps, ra, murules);

					     //ps.print(LocaleKBCT.GetString("PositiveInstances")+ "  => ");
					     ps.print("Positive Examples  => ");
					     double ratioPI= 0;
				         if ( (this.rulesPositiveFiringDegree[n] > 0) && (this.rulesPositiveItems[n].size() > 0) )
					    	   ratioPI=this.rulesPositiveFiringDegree[n]/this.rulesPositiveItems[n].size();

					     ps.print("(" + this.df.format(this.rulesPositiveFiringDegree[n]) + ") (" + this.df.format(ratioPI) + ")" + "  => ");
						 Enumeration posi= this.rulesPositiveItems[n].elements();
					     this.printItems(posi, ps, ra, murules);

					     //ps.print(LocaleKBCT.GetString("NegativeInstances")+ "  => ");
					     ps.print("Negative Examples  => ");
					     double ratioNI= 0;
				         if ( (this.rulesNegativeFiringDegree[n] > 0) && (this.rulesNegativeItems[n].size() > 0) )
					    	   ratioNI=this.rulesNegativeFiringDegree[n]/this.rulesNegativeItems[n].size();

					     ps.print("(" + this.df.format(this.rulesNegativeFiringDegree[n]) + ") (" + this.df.format(ratioNI) + ")" + "  => ");
						 Enumeration negi= this.rulesNegativeItems[n].elements();
					     this.printItems(negi, ps, ra, murules);
						 ps.println();
					     ra++;
				     }
			} 
            if (contUC > 0) {
		   	  ps.println("Rule"+ String.valueOf(NbRs + 1)+": UNCOVERED INSTANCES");
		      int contaux=0;
			  for (int n=0; n<NbData; n++) {
    	        if (itemsUncovered[n]==1) {
    	        	contaux++;
    	        	ps.print(String.valueOf(n+1)+"("+outs[n]+")");
    	            if (contaux<contUC)
    	        	    ps.print(", ");
    	            else
    	        	    ps.println();
    	        }
              }
            }
			this.df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
			this.df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
		   	ps.flush();
			ps.close();
		} catch (Throwable except) {
			except.printStackTrace();
			MessageKBCT.Error(null, except);
		}
	}
	// ------------------------------------------------------------------------------
	void printItems(Enumeration cc, PrintStream ps, int rind, double[][] murules) {
		if (!cc.hasMoreElements()) {
			ps.print(LocaleKBCT.GetString("NoItems"));
		} else {
			while (cc.hasMoreElements()) {
				int i= Integer.parseInt(cc.nextElement().toString());
				ps.print(i+"("+murules[i-1][rind]+")");
				if (cc.hasMoreElements())
					ps.print(", ");
			}
		}
		ps.println();
	}

	// ------------------------------------------------------------------------------
	void buildScienceMapMatrixLinksRulesFile(String file, PrintStream ps) {
		try {
			//System.out.println("JLinksFrame: buildScienceMapMatrixLinksRulesFile: INI");
			JKBCT kbctaux = new JKBCT(this.Parent.parent.Temp_kbct);
			this.NbRules = kbctaux.GetNbRules();
			this.NbActiveRules = kbctaux.GetNbActiveRules();
			//System.out.println("this.NbRules -> "+this.NbRules);
			// System.out.println("KB name="+this.Parent.parent.getTitle().substring(4));
			this.outClasses = new int[this.NbRules];
			this.NbPrem = new int[this.NbRules];
			this.nper = new int[this.NbRules];
			// this.mMaskOC= new int[this.NbActiveRules];
			// int c=0;
			//System.out.println("JLinksFrame: buildScienceMapMatrixLinksRulesFile 1");
			for (int n = 0; n < this.NbRules; n++) {
				Rule r = kbctaux.GetRule(n + 1);
				if (r.GetActive()) {
					int[] premises = r.Get_in_labels_number();
					int NP = 0;
					for (int m = 0; m < premises.length; m++) {
						if (premises[m] > 0)
							NP++;
					}
					this.nper[n] = 1;
					if (NP == 2)
						this.nper[n] = 2;
					else if (NP == 3)
						this.nper[n] = 3;
					else if (NP > 3)
						this.nper[n] = 4;

					this.NbPrem[n] = NP;
					this.outClasses[n] = r.Get_out_labels_number()[0];
					// this.mMaskOC[c++]=this.outClasses[n];
				} else {
					this.outClasses[n] = 0;
					this.nper[n] = 0;
					this.NbPrem[n] = 0;
				}
			}
			//System.out.println("JLinksFrame: buildScienceMapMatrixLinksRulesFile 2");
			long ptr = kbctaux.GetPtr();
			this.rulesToBeExpanded = kbctaux.GetRulesToBeExpanded();
			this.actives = new boolean[this.NbRules];
			for (int n = 0; n < this.NbRules; n++) {
				Rule r = kbctaux.GetRule(n + 1);
				this.actives[n] = r.GetActive();
				r.SetActive(false);
			}
			this.activeRules = new int[this.NbActiveRules];
			this.ruleLengths = new int[this.NbActiveRules];
			this.ruleOutputs = new int[this.NbActiveRules];
			Rule[] rules = new Rule[this.NbRules];
			//jnifis.FisproPath(MainKBCT.getConfig().GetKbctPath());
			//System.out.println("JLinksFrame: buildScienceMapMatrixLinksRulesFile 3");
			jnifis.setUserHomeFisproPath(MainKBCT.getConfig().GetKbctPath());
			int rule = 0;
			for (int i = 0; i < this.NbRules; i++) {
				if (this.actives[i]) {
					rules[i] = kbctaux.GetRule(i + 1);
					this.ruleLengths[rule] = this.computeRuleLength(rules[i]);
					this.ruleOutputs[rule] = this.getRuleOutput(rules[i]);
					rule++;
				}
			}
			for (int n = 0; n < this.NbRules; n++) {
				kbctaux.GetRule(n + 1).SetActive(actives[n]);
			}
			//System.out.println("JLinksFrame: buildScienceMapMatrixLinksRulesFile 4");
			PrintStream psms= null;
			PrintStream psmsc= null;
			if ( (this.fingramsMetric.equals("MS")) || (this.fingramsMetric.equals(LocaleKBCT.GetString("All"))) ) {
			    psms= new PrintStream(new FileOutputStream(file + ".ms", false));
			    if (!this.regOpt)
			         psmsc= new PrintStream(new FileOutputStream(file + ".msc", false));
		    }
			PrintStream psma= null;
			PrintStream psmac= null;
			if ( (this.fingramsMetric.equals("MA")) || (this.fingramsMetric.equals(LocaleKBCT.GetString("All"))) ) {
			    psma= new PrintStream(new FileOutputStream(file + ".ma", false));
			    if (!this.regOpt)
			        psmac= new PrintStream(new FileOutputStream(file + ".mac", false));
			}
			PrintStream psmfd= null;
			PrintStream psmfdc= null;
			if ( (this.fingramsMetric.equals("MSFD")) || (this.fingramsMetric.equals(LocaleKBCT.GetString("All"))) ) {
			    psmfd= new PrintStream(new FileOutputStream(file + ".msfd", false));
		        if (!this.regOpt)
			        psmfdc= new PrintStream(new FileOutputStream(file + ".msfdc", false));
			}
			PrintStream psmafd= null;
			PrintStream psmafdc= null;
			if ( (this.fingramsMetric.equals("MAFD")) || (this.fingramsMetric.equals(LocaleKBCT.GetString("All"))) ) {
			    psmafd= new PrintStream(new FileOutputStream(file + ".mafd", false));
		        if (!this.regOpt)
			        psmafdc= new PrintStream(new FileOutputStream(file + ".mafdc", false));
			}
			//System.out.println("JLinksFrame: buildScienceMapMatrixLinksRulesFile 5");
			this.printRuleLength(ps, this.NbRules);
			// build synthetic data
			int nbClasses = kbctaux.GetOutput(1).GetLabelsNumber();
			if (this.smoteFlag) {
				this.buildOversampledDataFile(this.linksFile, nbClasses);
				this.callLinksSyntheticData(this.syntheticDataFile, this.syntDataFileName);
			}
			// print matrices
			//System.out.println("JLinksFrame: buildScienceMapMatrixLinksRulesFile 6");
			//System.out.println("this.fingramsMetric -> "+this.fingramsMetric);
			if ( (this.fingramsMetric.equals("MS")) || (this.fingramsMetric.equals(LocaleKBCT.GetString("All"))) ) {
			    this.printMatrixData(ps, psms, "S");
		        if (!this.regOpt)
			        this.printMatrixData(ps, psmsc, "SC");
			}
			if ( (this.fingramsMetric.equals("MA")) || (this.fingramsMetric.equals(LocaleKBCT.GetString("All"))) ) {
			    this.printMatrixData(ps, psma, "A");
		        if (!this.regOpt)
			        this.printMatrixData(ps, psmac, "AC");
			}
			if ( (this.fingramsMetric.equals("MSFD")) || (this.fingramsMetric.equals(LocaleKBCT.GetString("All"))) ) {
			    this.printMatrixData(ps, psmfd, "SFD");
		        if (!this.regOpt)
			        this.printMatrixData(ps, psmfdc, "SFDC");
			}
			if ( (this.fingramsMetric.equals("MAFD")) || (this.fingramsMetric.equals(LocaleKBCT.GetString("All"))) ) {
			    this.printMatrixData(ps, psmafd, "AFD");
		        if (!this.regOpt)
			        this.printMatrixData(ps, psmafdc, "AFDC");
			}
			if (psms!=null) {
			    psms.flush();
			    psms.close();
			}
			if (psmsc!=null) {
			    psmsc.flush();
			    psmsc.close();
			}
			if (psma!=null) {
			    psma.flush();
			    psma.close();
			}
			if (psmac!=null) {
			    psmac.flush();
			    psmac.close();
			}
			if (psmfd!=null) {
			    psmfd.flush();
			    psmfd.close();
			}
			if (psmfdc!=null) {
			    psmfdc.flush();
			    psmfdc.close();
			}
			if (psmafd!=null) {
			    psmafd.flush();
			    psmafd.close();
			}
			if (psmafdc!=null) {
			    psmafdc.flush();
			    psmafdc.close();
			}
			kbctaux.Close();
			kbctaux.Delete();
			jnikbct.DeleteKBCT(ptr + 1);
		} catch (Throwable except) {
			except.printStackTrace();
			MessageKBCT.Error(null, except);
		}
		//System.out.println("JLinksFrame: buildScienceMapMatrixLinksRulesFile: END");
	}
	// ------------------------------------------------------------------------------
	private void buildGraphPICTURE(String[] files) throws Throwable {
		// to put the path in guaje.bat
		String graphvizPath=System.getProperty("graphpath")+System.getProperty("file.separator");
		String execFile;
		if (LocaleKBCT.isWindowsPlatform()) {
			execFile = "dot.exe";
		} else {
			execFile = "dot";
		}
	    File faux= new File(graphvizPath+execFile);
	    //System.out.println("fdot -> "+faux.getAbsolutePath());
	    if (!faux.exists())
	        throw new Exception("SelectAGraphvizPath");

		//int np = files.length / 2;
		String layoutMethod= MainKBCT.getConfig().GetFingramsLayout();
		//String base = graphvizPath + execFile
		//		+ " -Gratio=auto -Gsize=10,10 -Kneato -Tpng -o ";
		String auxbase = graphvizPath + execFile
				+ " -Gratio=auto -K"+layoutMethod+" -Tsvg -q1 -o ";

        //Date d= new Date(System.currentTimeMillis());
		//System.out.println("buildGraphPicture: T1 -> "+DateFormat.getDateTimeInstance().format(d));
		for (int n = 0; n < files.length; n++) {
			// System.out.println("n="+n+"  file -> "+files[n]);
			if (!files[n].equals("")) {
				String auxpicture = files[n] + ".svg";
				String auxcommand = auxbase + auxpicture + " " + files[n];
				//String auxcommand = auxbase + files[n];
				//System.out.println("auxcommand: " + auxcommand);
				// System.out.println("auxpicture="+auxpicture);
				//int auxexitVal= this.runProcess(auxcommand,"tempOutGraph.txt");
		        //d= new Date(System.currentTimeMillis());
				//System.out.println("buildGraphPicture: (n="+n+") -> "+DateFormat.getDateTimeInstance().format(d));
				this.runProcess(auxcommand, "tempOutGraph.txt");
			    // System.out.println("graphviz -> END");
				//System.out.println("auxExitValue: " + auxexitVal);
				/*if (auxexitVal!=0) {
					System.out.println("auxcommand: " + auxcommand);
					//System.out.println("auxpicture="+auxpicture);
				    System.out.println("auxExitValue: " + auxexitVal);
				}*/
			}
		}
        //d= new Date(System.currentTimeMillis());
		//System.out.println("buildGraphPicture: END -> "+DateFormat.getDateTimeInstance().format(d));
	}
	// ------------------------------------------------------------------------------
	/*private String[] buildGraphDot(String file) {
	    if (file.startsWith("Archivos de programa (x86)",3))
	    	file= file.replaceFirst("Archivos de programa (x86)","Archiv~2");
	    else if (file.startsWith("Archivos de programa",3))
	    	file= file.replaceFirst("Archivos de programa","Archiv~1");
	    else if (file.startsWith("Program Files (x86)",3))
	    	file= file.replaceFirst("Program Files (x86)","Progra~2");
	    else if (file.startsWith("Program Files",3))
	    	file= file.replaceFirst("Program Files","Progra~1");
	    else if (file.startsWith("Documents and Settings",3))
	    	file= file.replaceFirst("Documents and Settings","Docume~1");
		// String[] dotfiles= new String[18]; // ma, mac, mfd, mfdc, mafd, mafdc
		// String[] dotfiles = new String[16]; // ms, msc, ma, mac, mfd, mfdc,
		String[] dotfiles = new String[4]; // ms, msc
		for (int n=0; n<dotfiles.length; n++) {
			dotfiles[n]="";
		}
		String baseName = MainKBCT.getConfig().GetKbctPath()
				+ System.getProperty("file.separator") + "libs"
				+ System.getProperty("file.separator") + "vismaps";
		String bpfName, mfName;
		//String pfName;
		if (LocaleKBCT.isWindowsPlatform()) {
			bpfName = baseName + System.getProperty("file.separator")+ "BinaryPathfinder.exe";
			//pfName = baseName + System.getProperty("file.separator")+ "Fast-Pathfinder.exe";
			mfName = baseName + System.getProperty("file.separator")+ "MST-Pathfinder.exe";
		} else {
			bpfName = baseName + System.getProperty("file.separator")+ "BinaryPathfinder";
			//pfName = baseName + System.getProperty("file.separator")+ "Fast-Pathfinder";
			mfName = baseName + System.getProperty("file.separator")+ "MST-Pathfinder";
		}
		// OriginalPathFinder -> r=max and q (2 .. N-1)
		// It may be possible to change source code for r = (max, sum, avg, etc.)
		// BinaryPathFinder -> r=max and q (2 .. N-1)
		// N -> number of nodes
		// FastPathFinder -> q=N-1 and r=max
		// MST-PathFinder -> q=N-1 and r=max
		//String command = mfName + " " + file + ".ms" + " " + q;
		int Q= MainKBCT.getConfig().GetPathFinderParQ();
		boolean binary= false;
		//System.out.println("this.NbActiveRules -> "+this.NbActiveRules);
		if (Q < this.NbActiveRules - 1)
			binary= true;

		String command;
		if (binary) 
			command= bpfName+" "+file+".ms"+" "+Q;
		else
			//command= pfName + " " + file + ".ms";
		    command= mfName+" "+file+".ms";
			
		//String command = pfName + " " + file + ".ms";
		//System.out.println("command: " + command);
		int exitVal=0;
		if ( (this.fingramsMetric.equals(LocaleKBCT.GetString("All"))) || (this.fingramsMetric.equals("MS")) ) {
		  exitVal = this.runProcess(command, "tempOutMSTPathfinder.ms.txt");
		  //System.out.println("ExitValue: " + exitVal);
   		  //if ( (exitVal == 0) || ((!LocaleKBCT.isWindowsPlatform()) && (exitVal == 168)) ) {
		  if (exitVal == 0) {
			this.buildConfGraphDotFile(file + ".ms", file + ".ms.dot");
			this.buildConfGraphMeerkatFile(file + ".ms", file + ".ms.meerkat", this.NbRules);
			dotfiles[0] = file + ".ms.dot";
			this.buildConfGraphDotFile(JKBCTFrame.BuildFile("tempOutMSTPathfinder.ms.txt").getAbsolutePath(), file + ".ms.mpf.dot");
			this.buildConfGraphMeerkatFile(JKBCTFrame.BuildFile("tempOutMSTPathfinder.ms.txt").getAbsolutePath(), file + ".ms.mpf.meerkat", this.NbRules);
			dotfiles[1] = file + ".ms.mpf.dot";
		  } else {
			dotfiles[0] = "";
			dotfiles[1] = "";
		  }
		  if (!this.regOpt) {
			  if (binary) 
				  command= bpfName+" "+file+".msc"+" "+Q;
			  else
		          command = mfName+" "+file+ ".msc";
		          //command = pfName + " " + file + ".msc";
		      //System.out.println("command: " + command);

			  exitVal = this.runProcess(command, "tempOutMSTPathfinder.msc.txt");
  		      // System.out.println("ExitValue: " + exitVal);
    		  //if ( (exitVal == 0) || ((!LocaleKBCT.isWindowsPlatform()) && (exitVal == 168)) ) {
		      if (exitVal == 0) {
			      this.buildConfGraphDotFile(file + ".msc", file + ".msc.dot");
			      this.buildConfGraphMeerkatFile(file + ".msc", file + ".msc.meerkat", this.NbRules);
			      dotfiles[2] = file + ".msc.dot";
			      this.buildConfGraphDotFile(JKBCTFrame.BuildFile("tempOutMSTPathfinder.msc.txt").getAbsolutePath(), file + ".msc.mpf.dot");
			      this.buildConfGraphMeerkatFile(JKBCTFrame.BuildFile("tempOutMSTPathfinder.msc.txt").getAbsolutePath(), file + ".msc.mpf.meerkat", this.NbRules);
			      dotfiles[3] = file + ".msc.mpf.dot";
		    } else {
			  dotfiles[2] = "";
			  dotfiles[3] = "";
		    }
		  }
		}
		return dotfiles;
	}*/
	// ------------------------------------------------------------------------------
	/*private void buildConfGraphMeerkatFile(String inputFile, String outputFile, int NN) {
		// System.out.println("inputFile: "+inputFile);
		// System.out.println("outputFile: "+outputFile);
		try {
			// read input file
			LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(inputFile)));
			String l;
			Vector<Integer> origNodes = new Vector<Integer>();
			Vector<Integer> endNodes = new Vector<Integer>();
			Vector<Double> linkWeights = new Vector<Double>();
			double[][] matrix = null;
			double totalweights = 0;
			while ((l = lnr.readLine()) != null) {
				if (l.startsWith("*arcs") || l.startsWith("*edges")) {
					while ((l = lnr.readLine()) != null) {
						// System.out.println("l="+l);
						int ind1 = l.indexOf(" ");
						// System.out.println("ind1="+ind1);
						Integer n1 = new Integer(l.substring(0, ind1));
						// System.out.println("r1="+r1.intValue());
						origNodes.add(n1);
						String aux = l.substring(ind1 + 1);
						// System.out.println("aux="+aux);
						int ind2 = aux.indexOf(" ");
						// System.out.println("ind2="+ind2);
						Integer n2 = new Integer(aux.substring(0, ind2));
						// System.out.println("r2="+r2.intValue());
						endNodes.add(n2);
						Double lw = new Double(aux.substring(ind2 + 1));
						// System.out.println("lw="+lw.doubleValue());
						linkWeights.add(lw);
					}
				} else if (l.startsWith("*matrix")) {
					// System.out.println("reading MATRIX: NbRules="+this.NbRules);
					matrix = new double[NN][NN];
					for (int n = 0; n < NN; n++) {
						for (int m = 0; m < NN; m++) {
							matrix[n][m] = 0;
						}
					}
					String aux = lnr.readLine();
					for (int n = 0; n < NN; n++) {
						// System.out.println("aux="+aux);
						for (int m = 0; m < NN; m++) {
							int ind = aux.indexOf(" ");
							if (ind > 0)
								matrix[n][m] = (new Double(aux.substring(0, ind))).doubleValue();
							else
								matrix[n][m] = (new Double(aux)).doubleValue();

							if (m < NN - 1)
								aux = aux.substring(ind + 1);
							// System.out.println("  -> matrix["+n+"]["+m+"]="+matrix[n][m]);
						}
						aux = lnr.readLine();
					}
				}
			}
			if (matrix != null) {
				for (int n = 0; n < NN; n++) {
					for (int m = 0; m < NN; m++) {
						if ( (n != m) && (matrix[n][m] > 0)) {
							Integer n1 = new Integer(n + 1);
							// System.out.println("n1="+n1.intValue());
							origNodes.add(n1);
							Integer n2 = new Integer(m + 1);
							// System.out.println("n2="+n2.intValue());
							endNodes.add(n2);
							Double lw = new Double(matrix[n][m]);
							// System.out.println("lw="+lw.doubleValue());
							linkWeights.add(lw);
						}
					}
				}
			}
			lnr.close();
			// print output file
			PrintStream ps = new PrintStream(new FileOutputStream(outputFile,false));
			// Print in the Meerkat format
            ps.println("*Meerkat Version");
            ps.println("Dev_Meerkat_v1.00_20111004_rev1325");
            ps.println();
            ps.println("*Global Item Classes");
            ps.println();
            ps.println("*Filters");
            ps.println("{}");
            ps.println();
            ps.println("*Defaults");
            ps.println("color=#0000ff");
            ps.println("numberOfSides=1");
            ps.println("size=18");
            ps.println();
            ps.println("*Timeframe");
            ps.println("Not Specified");
            ps.println();
            ps.println("*Item Classes");
            ps.println();
            ps.println("*Vertices");
            for (int n=0; n<NN; n++) {
			     int p= n+1;
                 ps.println("R"+p+" {ID=R"+p+"; }");
			}
            ps.println();
            ps.println();
            ps.println("*Arcs");
            ps.println();
            ps.println("*Edges");
			// Print all the edges
			// System.out.println("Print all the edges");
			Object[] objN1 = origNodes.toArray();
			Object[] objN2 = endNodes.toArray();
			Object[] objLW = linkWeights.toArray();
			if ((objN1.length != objN2.length) || (objN1.length != objLW.length)) {
				System.out.println("buildConfGraphMeerkatFile -> ERROR reading and printing the edges of the graph");
			} else {
				int nbEdges = objN1.length;
				for (int n = 0; n < nbEdges; n++) {
					int o1 = ((Integer) objN1[n]).intValue();
					int o2 = ((Integer) objN2[n]).intValue();
					double weight= ((Double)objLW[n]).doubleValue();
				    String link = "R" + o1 + "	" + "R" + o2 + "	{weight=" + weight + "; }";
					ps.println(link);
				}
			}
			ps.println();
			ps.println();
			ps.println("*End");
			ps.println();
			ps.flush();
			ps.close();
			//System.out.println("END");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in buildConfGraphMeerkatFile -> "+e);
		}
	}*/
	// ------------------------------------------------------------------------------
	/*private void buildConfGraphDotFile(String inputFile, String outputFile) {
		// System.out.println("inputFile: "+inputFile);
		// System.out.println("outputFile: "+outputFile);
		try {
			// String[] colorsREG = {"#ffffff","#000000"}
			// String[] colorsREG = {"white","black"}
			// String[] colorsREG = {"grey0","grey12","grey25","grey37","grey50","grey62","grey75","grey87","grey100"}
			String[] colorsCLASS = {"lightgray", "lightyellow", "lightgoldenrod", "yellow", "yellowgreen", "lightseagreen", "green", "teal", "blue", "lightskyblue", "lightblue", "lightcyan", "purple", "magenta", "lightpink", "salmon", "red", "brown", "chocolate", "darkorange", "lightsalmon"};
			// String[] colors = { "lightblue", "lightsalmon", "lightseagreen", "lightgray", "lightpink", "lightcyan", "lightskyblue", "lightgoldenrod", "lightyellow"};
			//String[] colors = {"yellow", "yellowgreen", "green", "darkorange", "teal", "blue", "purple", "magenta", "red", "brown", "chocolate", "darkorange", "salmon"};
			//String[] shapes = { "polygon5", "box", "ellipse", "triangle", "diamond", "trapezium", "house", "circle", "polygon6", "polygon7", "polygon8", "polygon9", "polygon10", "polygon11", "polygon12"};
			JKBCT kbctaux = new JKBCT(this.Parent.parent.Temp_kbct);
			long ptr = kbctaux.GetPtr();
			String caux= kbctaux.GetOutput(1).GetClassif();
			int NbOutLabels=kbctaux.GetOutput(1).GetLabelsNumber();
			boolean classif;
			if (caux.equals("yes")) {
			    classif= true;
			} else {
				classif= false;
			}
			// read input file
			LineNumberReader lnr = new LineNumberReader(new InputStreamReader(
					new FileInputStream(inputFile)));
			String l;
			Vector<Integer> origRules = new Vector<Integer>();
			Vector<Integer> endRules = new Vector<Integer>();
			Vector<Double> linkWeights = new Vector<Double>();
			double[][] matrix = null;
			double totalweights = 0;
			while ((l = lnr.readLine()) != null) {
				if (l.startsWith("*arcs") || l.startsWith("*edges")) {
					while ((l = lnr.readLine()) != null) {
						// System.out.println("l="+l);
						int ind1 = l.indexOf(" ");
						// System.out.println("ind1="+ind1);
						Integer r1 = new Integer(l.substring(0, ind1));
						// System.out.println("r1="+r1.intValue());
						origRules.add(r1);
						String aux = l.substring(ind1 + 1);
						// System.out.println("aux="+aux);
						int ind2 = aux.indexOf(" ");
						// System.out.println("ind2="+ind2);
						Integer r2 = new Integer(aux.substring(0, ind2));
						// System.out.println("r2="+r2.intValue());
						endRules.add(r2);
						Double lw = new Double(aux.substring(ind2 + 1));
						// System.out.println("lw="+lw.doubleValue());
						linkWeights.add(lw);
						if ((outputFile.contains(".msc."))) {
							// double ccij=
							// lw.doubleValue()*Math.pow(this.FR[r1.intValue()-1]*this.FR[r2.intValue()-1],0.5);
							double ccij = lw.doubleValue()
									/ Math.pow(this.FR[r1.intValue() - 1]
											* this.FR[r2.intValue() - 1], 0.5);
							totalweights = totalweights
									+ (this.NbPrem[r1.intValue() - 1] + this.NbPrem[r2
											.intValue() - 1]) * ccij;
						}
					}
				} else if (l.startsWith("*matrix")) {
					// System.out.println("reading MATRIX: NbRules="+this.NbRules);
					matrix = new double[this.NbRules][this.NbRules];
					for (int n = 0; n < this.NbRules; n++) {
						for (int m = 0; m < this.NbRules; m++) {
							matrix[n][m] = 0;
						}
					}
					String aux = lnr.readLine();
					for (int n = 0; n < this.NbRules; n++) {
						// System.out.println("aux="+aux);
						if (this.outClasses[n] > 0) {
							for (int m = 0; m < this.NbRules; m++) {
								if (this.outClasses[m] > 0) {
									int ind = aux.indexOf(" ");
									// if (inputFile.endsWith(".ms"))
									// System.out.println("aux="+aux+"  ind="+ind);

									if (ind > 0)
										matrix[n][m] = (new Double(aux.substring(0, ind))).doubleValue();
									else
										matrix[n][m] = (new Double(aux)).doubleValue();

									if (m < this.NbRules - 1)
										aux = aux.substring(ind + 1);
								} else if (m < this.NbRules - 1) {
									aux = aux.substring(6);
								}
								// System.out.println("  -> matrix["+n+"]["+m+"]="+matrix[n][m]);
							}
						}
						aux = lnr.readLine();
					}
				}
			}
			// if ( (matrix!=null) && (inputFile.endsWith(".ms")) ) {
			// for (int n=0; n<this.NbRules; n++) {
			// for (int m=0; m<this.NbRules; m++) {
			// System.out.println("  -> matrix["+n+"]["+m+"]="+matrix[n][m]);
			// }
			// }
			// }
			if ((outputFile.contains(".msc."))) {
				// double lim=
				// this.NbRules*this.NbRules*this.syntheticDataFile.DataLength();
				double lim = this.LimMax;
				this.ciscpf = 1 - Math.pow(totalweights / lim, 0.5);
				if (this.ciscpf < 0)
					this.ciscpf = 0;

				// this.ciscpf= Math.pow(this.ciscpf,3);
			}
			if (matrix != null) {
				for (int n = 0; n < this.NbRules; n++) {
					for (int m = 0; m < this.NbRules; m++) {
						// if (inputFile.endsWith(".ms")) {
						// System.out.println("matrix["+n+"]["+m+"]="+matrix[n][m]);
						// System.out.println("this.outClasses["+n+"]="+this.outClasses[n]);
						// System.out.println("this.outClasses["+m+"]="+this.outClasses[m]);
						// if (this.outClasses[n]>0)
						// System.out.println("N");
						// if (this.outClasses[m]>0)
						// System.out.println("M");
						// if (n!=m)
						// System.out.println("NM");
						// if (matrix[n][m]>0)
						// System.out.println("MAT");
						// }
						if ((this.outClasses[n] > 0)
								&& (this.outClasses[m] > 0) && (n != m)
								&& (matrix[n][m] > 0)) {
							Integer r1 = new Integer(n + 1);
							// System.out.println("r1="+r1.intValue());
							origRules.add(r1);
							Integer r2 = new Integer(m + 1);
							// System.out.println("r2="+r2.intValue());
							endRules.add(r2);
							Double lw = new Double(matrix[n][m]);
							// System.out.println("lw="+lw.doubleValue());
							linkWeights.add(lw);
							// if (inputFile.endsWith(".ms")) {
							// System.out.println("r1="+r1.intValue()+"  r2="+r2.intValue()+"  lw="+lw.doubleValue());
							// }
						}
					}
				}
			}
			lnr.close();
			// print output file
			PrintStream ps = new PrintStream(new FileOutputStream(outputFile,false));
			// Print in the Graphviz input DOT format
			// To export in PNG, use:
			// dot -Gratio=fill -Gsize=15,15 -Kneato -Tpng -o map.png map.dot &&
			// eog map.png
			// fontsize=20 for Francia/Japan, 30 for Espana/Alemania/China
			//String url="http://localhost:8888/guaje";
			if ((outputFile.contains(".ms.")) || (outputFile.contains(".msc."))
					|| (outputFile.contains(".msfd."))
					|| (outputFile.contains(".msfdc.")))
				ps.println("graph G {");
			else
				ps.println("digraph G {");

			ps.println("	name=\"\";");
			ps.println("	ratio=auto;");
			ps.println("	size=\"10,10\";");
			ps.println("	overlap=\"scale\";");
			ps.println("	nodesep=0.3;");
			// ps.println("	sep=-0.3;");
			ps.println("	center=true;");
			ps.println("	truecolor=true;");
			// ps.println("	orientation=landscape;");
			// ps.println("	splines=true;");
			// File f= new File(this.Parent.parent.getTitle());
			// String Gname=f.getName();
			// ps.println("	label=\""+Gname+"\";");
			// ps.println("	stylesheet=\""+Gname+"\";");
			// ps.println("	outputorder=\"nodesfirst\";");
			// ps.println();
			// ps.println("	edge [size=1];");
			// ps.println("	node [shape=box, margin=\"0,0\", fixedsize=false, fontsize=7, fillcolor=gold2, style=filled, color=white, width=0, height=0];");
			// Print all the nodes (Rules)
			int NbData=1;
			if (this.smoteFlag)
                NbData=this.syntheticDataFile.DataLength();
			else
                NbData=this.trainingDataFile.DataLength();
			
			//System.out.println("DATA -> "+NbData);
			//int contr= 0;
			for (int n = 0; n < this.NbRules; n++) {
				Rule r = kbctaux.GetRule(n + 1);
				if ((r.GetActive()) && (this.outClasses[n] > 0)) {
					String ruleDesc = "R" + String.valueOf(n + 1) + ":" + kbctaux.GetRuleDescription(n+1);
					int[] out_labels_number = r.Get_out_labels_number();
					
					//String shape= "polygon,sides=12";
					//String color= "lightgoldenrod";
					String shape= "circle";
					String color= "grey";
					String pcolor= "black";
					String fc= "black";
					//System.out.println("RULE -> "+String.valueOf (n+1));
                    Vector<Integer> auxircc= this.itemsRulesCorrectConclusion[n]; 
                    int cc=auxircc.size();
					//System.out.println("   itemsRulesCorrectConclusion -> "+cc);
                    //System.out.println("rule: "+"R"+String.valueOf(n+1));
                    //double ccdegree= this.rulesCCFiringDegree[n];
                    //System.out.println("   -> ccdegree="+ccdegree);
                    //double ccicdegree= ccdegree + this.rulesICFiringDegree[n];
                    Vector<Integer> auxiric= this.itemsRulesIncorrectConclusion[n]; 
					int ic=auxiric.size();
					//System.out.println("   itemsRulesIncorrectConclusion -> "+ic);
                    //System.out.println("rule: "+"R"+String.valueOf(n+1));
                    //System.out.println("   -> ccicdegree="+ccicdegree);
					double sd= 1+4*((double)(cc+ic)/(double)NbData);
					int fs= 15 + 2*((int)sd);
					//String s= String.valueOf(sd);
					//System.out.println("  sd -> "+s);
					String nodeInfo;
					String nodeInfoToolTipText;
					double covratio= 0;
					if (cc+ic > 0)
					    covratio= ((double)(cc+ic))/NbData;

					//double consratio= 0;
					//if (ccdegree > 0)
						//consratio= ((double)ccdegree)/ccicdegree;
					//System.out.println("Rule= "+String.valueOf(n+1));
                    //Vector<Integer> auxirpos= this.rulesPositiveItems[n]; 
					//int ipos=auxirpos.size();
                    //Vector<Integer> auxirneg= this.rulesNegativeItems[n]; 
					//int ineg=auxirneg.size();
                    //Vector<Integer> auxirneu= this.rulesNeutralItems[contr++]; 
					//int ineu=auxirneu.size();
					//System.out.println("   ipos="+ipos);
					//System.out.println("   ineg="+ineg);
					//System.out.println("   cc="+cc);
					//System.out.println("   ic="+ic);
                    double ccdegree= this.rulesCCFiringDegree[n];
                    double icdegree= this.rulesICFiringDegree[n];
                    double posdegree= this.rulesPositiveFiringDegree[n];
                    double negdegree= this.rulesNegativeFiringDegree[n];
					//System.out.println("   posdegree="+posdegree);
					//System.out.println("   negdegree="+negdegree);
					//System.out.println("   ccdegree="+ccdegree);
					//System.out.println("   icdegree="+icdegree);
					double consratio= 0;
					if (ccdegree + icdegree > 0)
						consratio= (posdegree-negdegree)/(ccdegree+icdegree);

					// Paper IEEETFS
                    //if (cc+ic > 0)
					  //  consratio= ((double)(cc))/(cc+ic);
					
					//System.out.println("   consratio="+consratio);
					double cratio= 0;
					if (cc > 0)
						cratio= ((double)cc)/this.samplesPerClass[out_labels_number[0]-1];

					if (classif) {
					   //shape= shapes[this.outClasses[n] - 1];
					   color= colorsCLASS[Math.min((this.outClasses[n] - 1)*(colorsCLASS.length/(NbOutLabels-1)),colorsCLASS.length-1)];
					   if (shape.startsWith("polygon")) {
					       String aux= shape.substring(7);
					       //System.out.println("aux: "+aux);
					       shape="polygon,sides="+aux;
					   }
					   //System.out.println("Rule= "+String.valueOf(n+1));
					   //System.out.println(" NbData= "+NbData);
					   //System.out.println("  NbDataCov= "+String.valueOf(cc+ic));
					   //System.out.println("  dratio= "+dratio);
					   //System.out.println("  NbDataClass= "+this.samplesPerClass[out_labels_number[0]-1]);
					   //System.out.println("  NbDataClassCov= "+cc);
					   //cratio= ((double)cc)/this.samplesPerClass[out_labels_number[0]-1];
					   //System.out.println("  cratio= "+cratio);
					   //if (consratio < 0) {
						   // Paper IEEETFS
					      nodeInfo= "\\n"+" (cov="+this.df.format(covratio)
							     +") \\n"+" (G= "+this.df.format(consratio)
							     +") \\n (C"+String.valueOf(this.outClasses[n])+"="+this.df.format(cratio)+")";
				       nodeInfoToolTipText= " (cov="+this.df.format(covratio)+" ; G="
                                + this.df.format(consratio)+" ; C="
			                     + this.df.format(cratio)+")";
					} else {
					   int ind= (int)((this.outClasses[n] - 1)*((float)100/(NbOutLabels-1)));
					   color= color+String.valueOf(ind);
					   if (ind <= 50)
						   fc= "white";

					   //cratio= ((double)(cc))/(cc+ic);
					   //nodeInfo= "\\n"+" (cov="+this.df.format(covratio)+") \\n (L"+String.valueOf(this.outClasses[n])+"="+this.df.format(cratio)+")";
					   //nodeInfo= "\\n"+" (cov="+this.df.format(covratio)+")";
					   //if (consratio < 0) {
						   // Paper IEEETFS
					       //nodeInfo= "\\n"+" (cov="+this.df.format(covratio) +")";
					       nodeInfo= "\\n"+" (cov="+this.df.format(covratio)
							     +") \\n"+" (G= "+this.df.format(consratio)+")";
				       nodeInfoToolTipText= " (cov="+this.df.format(covratio)+" ; G="
                                + this.df.format(consratio)+")";
					}
					if ( (color.equals("white")) || (color.startsWith("grey")) )
					    pcolor= "black";
					else
						pcolor= color;

					ps.println("	" + LocaleKBCT.GetString("Rule")
							+ String.valueOf(n + 1) + " [shape="
							+ shape
							+ ",height="+this.df.format(sd)+",width="+this.df.format(sd)+",fixedsize=true"
							+ ",peripheries=" + this.nper[n] + ",color=" + pcolor + ",fillcolor=" + color
							+ ",fontsize="+fs+",fontcolor="+fc+",style=filled"
							+ ",label=\"R" + String.valueOf(n + 1) + nodeInfo + "\""
							//+ ",href=\"" + url + "\""
							+ ",tooltip=\"" + ruleDesc + nodeInfoToolTipText 
							+ "\"];");
				}
			}
			//kbctaux.Close();
			//kbctaux.Delete();
			//jnikbct.DeleteKBCT(ptr + 1);
			// ps.println();
			// Print all the edges
			//System.out.println("Print all the edges");
			Object[] objR1 = origRules.toArray();
			Object[] objR2 = endRules.toArray();
			Object[] objLW = linkWeights.toArray();
			if ((objR1.length != objR2.length) || (objR1.length != objLW.length)) {
				System.out.println("ERROR reading and printing the edges of the graph");
			} else {
				int nbEdges = objR1.length;
				int[][] printEdges = null;
				boolean warn = false;
				if ((outputFile.contains(".ms."))
						|| (outputFile.contains(".msc."))
						|| (outputFile.contains(".msfd."))
						|| (outputFile.contains(".msfdc."))) {
					printEdges = new int[this.NbRules][this.NbRules];
					warn = true;
				}
				for (int n = 0; n < nbEdges; n++) {
					int o1 = ((Integer) objR1[n]).intValue();
					int o2 = ((Integer) objR2[n]).intValue();
					// if (inputFile.endsWith(".ms")) {
					// System.out.println("printEdges["+String.valueOf(o1-1)+"]["+String.valueOf(o2-1)+"]="+printEdges[o1-1][o2-1]);
					// System.out.println("printEdges["+String.valueOf(o2-1)+"]["+String.valueOf(o1-1)+"]="+printEdges[o2-1][o1-1]);
					// }
					if (!warn || (warn && (printEdges[o1 - 1][o2 - 1] == 0) && (printEdges[o2 - 1][o1 - 1] == 0))) {
					    String linkName = LocaleKBCT.GetString("Rule") + o1;
						String linkToolTipText = "R" + o1;
						if ((outputFile.contains(".ms."))
								|| (outputFile.contains(".msc."))
								|| (outputFile.contains(".msfd."))
								|| (outputFile.contains(".msfdc."))) {
							linkName = linkName + " -- ";
							linkToolTipText = linkToolTipText + " -- ";
						} else {
							linkName = linkName + " -> ";
							linkToolTipText = linkToolTipText + " -> ";
						}
						linkName = linkName + LocaleKBCT.GetString("Rule") + o2;
						linkToolTipText = linkToolTipText + "R" + o2;
						String col= "black";
						if (classif) {
						    col = "red";
					     	if (this.outClasses[o1 - 1] == this.outClasses[o2 - 1])
						    	col = "darkgreen";
						}
						ps.print("	" + linkName);
						String linkDesc = linkToolTipText
								+ " ("
								+ this.df.format(((Double) objLW[n]).doubleValue()) + ")";
						ps.println(" [penwidth="
								+ this.df.format(10 * ((Double) objLW[n]).doubleValue())
								+ ",weight="	
								+ this.df.format(100 *( 1 - ((Double)objLW[n]).doubleValue()))
								+ ",color="	+ col
								+ ",arrowhead=vee,fontsize=15"
								+ ",labelfontcolor=" + col
								+ ",label=\""
								+ this.df.format(((Double) objLW[n]).doubleValue()) + "\""
							    //+ ",href=\"" + url + "\""
							    + ",tooltip=\"" + linkDesc
								+ "\"];");
						// ps.println(" [penwidth="+String.valueOf(1+((Double)objLW[n]).doubleValue())+",color="+col+",arrowhead=vee,fontsize=8,labelfontcolor="+col+",label=\""+((Double)objLW[n]).doubleValue()+"\"];");
						// style=bold
						if (warn) {
							printEdges[o1 - 1][o2 - 1] = 1;
						}
					}
				}
				ps.println("");
			}
			ps.println("}");
			ps.flush();
			ps.close();
			//System.out.println("END");
		} catch (Exception e) {
			e.printStackTrace();
			MessageKBCT.Error(null, e);
		}
	}*/
	// ------------------------------------------------------------------------------
	/*private void printFingramsLegend(String outputFile) {
      try {
		String[] colorsCLASS = {"lightgray", "lightyellow", "lightgoldenrod", "yellow", "yellowgreen", "lightseagreen", "green", "teal", "blue", "lightskyblue", "lightblue", "lightcyan", "purple", "magenta", "lightpink", "salmon", "red", "brown", "chocolate", "darkorange", "lightsalmon"};
		// String[] colors = { "lightblue", "lightsalmon", "lightseagreen", "lightgray", "lightpink", "lightcyan", "lightskyblue", "lightgoldenrod", "lightyellow"};
		//String[] colors = {"yellow", "yellowgreen", "green", "darkorange", "teal", "blue", "purple", "magenta", "red", "brown", "chocolate", "darkorange", "salmon"};
		//String[] shapes = { "polygon5", "box", "ellipse", "triangle", "diamond", "trapezium", "house", "circle", "polygon6", "polygon7", "polygon8", "polygon9", "polygon10", "polygon11", "polygon12"};
		// greycolor
		// Graphviz -> grey0 .. grey99
		// RGB -> #000000 .. fcfcfc
        String[] greycolor= {"00","03","05","08","0a","0d","0f","12","14","17",
        		"1a","1c","1f","21","24","26","29","2b","2e","30",
        		"33","36","38","3b","3d","40","42","45","47","4a",
        		"4d","4f","52","54","57","59","5c","5e","61","63",
        		"66","69","6b","6e","70","73","75","78","7a","7d",
        		"7f","82","85","87","8a","8c","8f","91","94","96",
        		"99","9c","9e","a1","a3","a6","a8","ab","ad","b0",
        		"b3","b5","b8","ba","bd","bf","c2","c4","c7","c9",
        		"cc","cf","d1","d4","d6","d9","db","de","e0","e3",
        		"e5","e8","eb","ed","f0","f2","f5","f7","fa","fc"};
		JKBCT kbctaux = new JKBCT(this.Parent.parent.Temp_kbct);
		long ptr = kbctaux.GetPtr();
		String caux= kbctaux.GetOutput(1).GetClassif();
		int NbOutLabels=kbctaux.GetOutput(1).GetLabelsNumber();
		boolean classif;
		if (caux.equals("yes")) {
		    classif= true;
		} else {
			classif= false;
		}
		// Print legend file
		PrintStream pslegend = new PrintStream(new FileOutputStream(outputFile+".legend.svg",false));
		pslegend.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		pslegend.println("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"");
		pslegend.println("\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");
		pslegend.println("<!-- Title: G Pages: 1 -->");
		//pslegend.println("<svg width=\"300pt\" height=\""+String.valueOf(100+55*(NbOutLabels+1))+"pt\"");
		//pslegend.println("viewBox=\"0.00 300.00 0.00 "+String.valueOf(100+55*(NbOutLabels+1))+".00 \" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">");
        int height= 60*NbOutLabels;
		pslegend.println("<svg width=\"600pt\" height=\""+height+"pt\"");
		pslegend.println(" viewBox=\"40.00 300.00 600 "+height+"\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">");
		pslegend.println("<g id=\"graph1\" class=\"graph\" transform=\"scale(0.840651 0.840651) rotate(0) translate(4 852.479)\">");
		pslegend.println("<polygon fill=\"white\" stroke=\"white\" points=\"-4,5 -4,-852.479 571.116,-852.479 571.116,5 -4,5\"/>");
		JKBCTOutput jout = kbctaux.GetOutput(1);
		String[] labNames = null;
		if (jout.GetScaleName().equals("user")) {
			labNames = jout.GetUserLabelsName();
		} else {
			labNames = jout.GetLabelsName();
		}
		kbctaux.Close();
		kbctaux.Delete();
		jnikbct.DeleteKBCT(ptr + 1);
		for (int n=0; n<NbOutLabels; n++) {
			String stroke= "black";
			String color="grey";
			String label="";
			if (classif) {
				color= colorsCLASS[Math.min(n*(colorsCLASS.length/(NbOutLabels-1)),colorsCLASS.length-1)];
				stroke= color;
				label= label+LocaleKBCT.GetString("Class")+" ";
			} else {
				//int ind= (int)(n*(((float)100/(NbOutLabels-1))-1));
				int ind= Math.min((int)(n*(((float)100/(NbOutLabels-1)))),99);
				color= "#"+greycolor[ind]+greycolor[ind]+greycolor[ind];
                //System.out.println("n="+n+"  i="+ind+"  c="+color);					
			}
			int id= n+1;
			pslegend.println("<g id=\"node"+id+"\" class=\"node\"><title></title>");
            int x= 125;
			int y= -460+n*55;
			pslegend.println("<ellipse fill=\""+color+"\" stroke=\""+stroke+"\" cx=\""+x+"\" cy=\""+y+"\" rx=\"25\" ry=\"25\"/>");
			label= label + labNames[n];
			if (!jout.GetScaleName().equals("user"))
				label = LocaleKBCT.GetString(label);

            int dy= y + 5;
			pslegend.println("<text text-anchor=\"start\" x=\"170\" y=\""+dy+"\" font-family=\"Times New Roman,serif\" font-size=\"18.00\">"+label+"</text>");
			pslegend.println("</g>");
        }
		pslegend.println("</g>");
		pslegend.println("</svg>");
		pslegend.flush();
		pslegend.close();
	  } catch (Exception e) {
			e.printStackTrace();
			MessageKBCT.Error(null, e);
	  }
	}*/
	// ------------------------------------------------------------------------------
	private void printRuleLength(PrintStream ps, int NbRs) {
		String st = LocaleKBCT.GetString("RuleLength");
		ps.println(st);
		for (int n = 0; n < st.length(); n++) {
			ps.print("=");
		}
		ps.println();
		ps.println("// RuleName (outputClass) => Nb of Premises");
		ps.println(LocaleKBCT.GetString("Rules") + ": " + this.NbActiveRules);
		int[] posActiveRules = new int[this.NbActiveRules];
		int rule = 0;
		for (int n = 0; n < NbRs; n++) {
			if (this.actives[n]) {
				posActiveRules[rule] = new Integer(n + 1);
				ps.println(LocaleKBCT.GetString("Rule") + String.valueOf(n + 1)
						+ " (" + this.ruleOutputs[rule] + ")  => "
						+ this.ruleLengths[rule]);
				this.activeRules[rule] = n + 1;
				rule++;
			}
		}
		int cont=0;
		for (int n = 0; n < this.rulesToBeExpanded.length; n++) {
			if (this.rulesToBeExpanded[n] > 0)
				cont++;
		}
		this.itemsRulesNumbers= new Integer[this.NbActiveRules+cont];
		int indexp = 0;
		//System.out.println("this.rulesToBeExpanded.length -> "+this.rulesToBeExpanded.length);
		for (int n = 0; n < this.rulesToBeExpanded.length; n++) {
			//System.out.println("n -> "+n);
			//System.out.println("indexp -> "+indexp);
			//System.out.println("rtbe -> "+this.rulesToBeExpanded[n]);
			//System.out.println("this.itemsRulesNumbers.length -> "+this.itemsRulesNumbers.length);
			//System.out.println("posActiveRules.length -> "+posActiveRules.length);
			if (this.rulesToBeExpanded[n] == 0) {
				this.itemsRulesNumbers[indexp] = posActiveRules[n];
				indexp++;
			} else if (this.rulesToBeExpanded[n] > 0) {
				for (int m = 0; m < this.rulesToBeExpanded[n]; m++) {
					this.itemsRulesNumbers[indexp] = posActiveRules[n];
					indexp++;
				}
			}
		}
		//for (int n=0; n<this.itemsRulesNumbers.length; n++) {
		 //System.out.println("this.itemsRulesNumbers -> "+this.itemsRulesNumbers[n].intValue());
		//}
		for (int n = 0; n < st.length(); n++) {
			ps.print("*");
		}
		ps.println();
	}

	// ------------------------------------------------------------------------------
	private void printMatrixData(PrintStream ps, PrintStream psm, String opt) throws Throwable {
		// opt -> A = Asymmetric
		// opt -> AC = Asymmetric (only conflicts)
		// opt -> SFD = Firing Degree (Symmetric) - diff/max
		// opt -> SFDC = Firing Degree (Symmetric) - diff/max (only conflicts)
		// opt -> AFD = A*FD (Asymmetric)
		// opt -> AFDC = A*FD (Asymmetric) - (only conflicts)
		// optData -> Training / Synthetic
		String st = "";
		String dataFile;
		if (this.smoteFlag)
			dataFile = this.syntDataFileName;
		else
			dataFile = this.trainingDataFile.FileName();

		if (opt.equals("S"))
			st = LocaleKBCT.GetString("SimultaneousFiredRulesSymmetric");
		else if (opt.equals("SC"))
			st = LocaleKBCT.GetString("SimultaneousFiredRulesSymmetricOnlyConflicts");
		else if (opt.equals("A"))
			st = LocaleKBCT.GetString("SimultaneousFiredRulesAsymmetric");
		else if (opt.equals("AC"))
			st = LocaleKBCT.GetString("SimultaneousFiredRulesAsymmetricOnlyConflicts");
		else if (opt.equals("SFD"))
			st = LocaleKBCT.GetString("SimultaneousFiredRulesSymmetricFiringDegree");
		else if (opt.equals("SFDC"))
			st = LocaleKBCT.GetString("SimultaneousFiredRulesSymmetricFiringDegreeOnlyConflicts");
		else if (opt.equals("AFD"))
			st = LocaleKBCT.GetString("SimultaneousFiredRulesAsymmetricByFiringDegree");
		else if (opt.equals("AFDC"))
			st = LocaleKBCT.GetString("SimultaneousFiredRulesAsymmetricByFiringDegreeOnlyConflicts");

		ps.println(st);
		for (int n = 0; n < st.length(); n++) {
			ps.print("=");
		}
		ps.println();
		psm.println("*vertices " + this.NbRules);
		// System.out.println("this.NbRules -> "+this.NbRules);
		// System.out.println("this.NbActiveRules -> "+this.NbActiveRules);
		for (int n = 0; n < this.NbRules; n++) {
			// if (this.outClasses[n] > 0) {
			int ind = n + 1;
			psm.println(ind
							+ " \""
							+ ind
							+ "\" ellipse x_fact 1.22866290766363 y_fact 1.22866290766363 fos 3.5 bw 0.0 ic Emerald");
			// }
		}
		psm.println("*matrix");
		double[][] matrix = new double[this.NbRules][this.NbRules];
		if (opt.equals("S"))
			matrix = this.readItemsRulesFile(dataFile + ".items.rules", true, "NRi", "S");
		else if (opt.equals("SC"))
			matrix = this.readItemsRulesFile(dataFile + ".items.rules", true, "NRi", "SC");
		else if (opt.equals("A"))
			matrix = this.readItemsRulesFile(dataFile + ".items.rules", true, "NRi", "A");
		else if (opt.equals("AC"))
			matrix = this.readItemsRulesFile(dataFile + ".items.rules", true, "NRi", "AC");
		else if (opt.equals("SFD"))
			matrix = this.readItemsRulesFiringDegreeFile(dataFile + ".items.murules", true, "SFD");
		else if (opt.equals("SFDC"))
			matrix = this.readItemsRulesFiringDegreeFile(dataFile + ".items.murules", true, "SFDC");
		else if ((opt.equals("AFD")) || (opt.equals("AFDC"))) {
			double[][] ma = null;
			double[][] mfd = null;
			if (opt.equals("AFD")) {
				ma = this.readItemsRulesFile(dataFile + ".items.rules", true, "NRi", "A");
				mfd = this.readItemsRulesFiringDegreeFile(dataFile
						+ ".items.murules", true, "SFD");
			} else {
				ma = this.readItemsRulesFile(dataFile + ".items.rules", true, "NRi", "AC");
				mfd = this.readItemsRulesFiringDegreeFile(dataFile + ".items.murules", true, "SFDC");
			}
			for (int n = 0; n < this.NbRules; n++) {
				for (int m = 0; m < this.NbRules; m++) {
					matrix[n][m] = ma[n][m] * mfd[n][m];
				}
			}
		}
		// Print Matrix
		double thres= MainKBCT.getConfig().GetPathFinderThreshold();
		for (int n = 0; n < this.NbRules; n++) {
			for (int m = 0; m < this.NbRules; m++) {
				if (matrix[n][m] > thres) {
				    ps.print(this.df.format(matrix[n][m]) + " ");
				    psm.print(this.df.format(matrix[n][m]) + " ");
				} else {
				    ps.print(this.df.format(0) + " ");
				    psm.print(this.df.format(0) + " ");
				}
			}
			ps.println();
			psm.println();
		}
		for (int n = 0; n < st.length(); n++) {
			ps.print("*");
		}
		ps.println();
		/*this.MSDoC= new double[this.NbRules];
		for (int n = 0; n < this.NbRules; n++) {
			for (int m = n+1; m < this.NbRules; m++) {
				if (!this.actives[n]) {
					this.MSDoC[n]=0;						
				} else if (matrix[n][m] > 0) {
                	 this.MSDoC[n]++;
				}
			}
		}*/
	    /*for (int n=0; n<this.NbRules; n++) {
			if (!this.actives[n]) {
				this.MSDoC[n]=0;						
			}
	    }*/
	}

	// ------------------------------------------------------------------------------
	private void buildOversampledDataFile(String file_name, int NbClasses) throws Throwable {
		int lim = 1;
		if (NbClasses > 2)
			lim = NbClasses;

		JExtendedDataFile[] result = new JExtendedDataFile[lim];
		JExtendedDataFile AllDataFile = new JExtendedDataFile(file_name, true);
		double[][] data = AllDataFile.GetData();
		int nbVars = AllDataFile.VariableCount();
		int nbRows = AllDataFile.DataLength();
		double[] newSamples = new double[NbClasses];
		double[] dataClasses = AllDataFile.VariableData(nbVars - 1);
		int[] aux = new int[NbClasses];
		for (int n = 0; n < dataClasses.length; n++) {
			aux[(int) dataClasses[n] - 1]++;
		}
		int maxind = 0;
		for (int n = 1; n < NbClasses; n++) {
			if (aux[n] > aux[maxind])
				maxind = n;
		}
		// System.out.println("maxind -> "+maxind);
		for (int n = 0; n < NbClasses; n++) {
			// System.out.println("C"+String.valueOf(n+1)+" -> "+aux[n]);
			newSamples[n] = (double) (aux[maxind] - aux[n]) / (aux[n]);
		}
		// for (int n=0; n<NbClasses; n++) {
		// System.out.println("newSamples["+n+"] -> "+newSamples[n]);
		// }
		// System.out.println("FN="+file_name);
		// System.out.println("Columns="+nbVars);
		// System.out.println("Rows="+nbRows);
		String[] filenames = new String[lim];
		if (NbClasses > 2) {
			// Translate to binary class
			for (int n = 0; n < NbClasses; n++) {
				result[n] = new JExtendedDataFile(file_name, true);
				int c1 = 0, c2 = 0;
				for (int i = 0; i < nbRows; i++) {
					for (int j = 0; j < nbVars; j++) {
						if (j == nbVars - 1) {
							if (n == 0) {
								if (data[i][j] != n + 1) {
									result[n].setDataElement(i, j, 2);
									c2++;
								} else {
									result[n].setDataElement(i, j, 1);
									c1++;
								}
							} else if (n > 0) {
								if (data[i][j] != n + 1) {
									result[n].setDataElement(i, j, 1);
									c1++;
								} else {
									result[n].setDataElement(i, j, 2);
									c2++;
								}
							}
						} else {
							result[n].setDataElement(i, j, data[i][j]);
						}
					}
				}
				// reorder data
				// this.reorderData(result[n], nbRows, nbVars, c1, c2);
				String fn = file_name + "." + String.valueOf(n + 1);
				filenames[n] = fn;
				result[n].Save(fn);
			}
		} else {
			result[0] = AllDataFile;
			filenames[0] = file_name;
		}
		// Save data files into KEEL format (adding header)
		this.saveAsKEELfile(filenames, result);
		// CALL to KEEL oversampling methods
		// SMOTE
		this.buildConfFiles(filenames, newSamples);
		// String jarfile= "C:/Development/GUAJE1/libs/keellibs/RunKeel.jar";
		String jarfile = MainKBCT.getConfig().GetKbctPath()
				+ System.getProperty("file.separator") + "libs"
				+ System.getProperty("file.separator") + "keellibs"
				+ System.getProperty("file.separator") + "RunKeel.jar";
		String command = "java -Xmx512m -jar " + jarfile;
		// System.out.println("command="+command);
		int exitVal = this.runProcess(command, "tempOutSmote.txt");
		// System.out.println("ExitValue: " + exitVal);
		if (exitVal == 0) {
			// Join eoversampled files
			this.removeSmoteTestFiles(filenames);
			this.syntheticDataFile = this.buildJoinJEDF(filenames, nbVars);
		}
	}

	// ------------------------------------------------------------------------------
	private void removeSmoteTestFiles(String[] fnames) throws Throwable {
		for (int n = 0; n < fnames.length; n++) {
			File f = new File(fnames[n] + ".dat.smote.tst.dat");
			if (f.exists())
				f.delete();
		}
	}

	// ------------------------------------------------------------------------------
	private JExtendedDataFile buildJoinJEDF(String[] fnames, int NC) throws Throwable {
		JExtendedDataFile[] dd = new JExtendedDataFile[fnames.length];
		for (int n = 0; n < fnames.length; n++) {
			String smotedata = fnames[n] + ".dat.smote.dat";
			LineNumberReader lnr = new LineNumberReader(new InputStreamReader(
					new FileInputStream(smotedata)));
			String l;
			while ((l = lnr.readLine()) != null) {
				if (l.startsWith("@data")) {
					PrintStream ps = new PrintStream(new FileOutputStream(
							smotedata + ".txt", false));
					while ((l = lnr.readLine()) != null) {
						ps.println(l);
					}
					ps.flush();
					ps.close();
				}
			}
			lnr.close();
			dd[n] = new JExtendedDataFile(smotedata + ".txt", true);
		}
		int initNbData = this.trainingDataFile.DataLength();
		// System.out.println("initNbData= "+initNbData);
		int[] NbAddedData = new int[fnames.length];
		int[] accAddedData = new int[fnames.length];
		int totalAddedData = 0;
		for (int n = 0; n < fnames.length; n++) {
			NbAddedData[n] = dd[n].DataLength() - initNbData;
			totalAddedData = totalAddedData + NbAddedData[n];
			accAddedData[n] = totalAddedData;
			// System.out.println("-> NbData("+String.valueOf(n+1)+")= "+dd[n].DataLength());
		}
		// System.out.println("totalAddedData= "+totalAddedData);
		// for (int n=0; n<accAddedData.length; n++) {
		// System.out.println(" acc["+n+"]= "+accAddedData[n]);
		// }
		double[][] d0 = this.trainingDataFile.GetData();
		int NR = initNbData + totalAddedData;
		double[][] data = new double[NR][NC];
		int r = initNbData;
		for (int n = 0; n < NR; n++) {
			if (n < initNbData) {
				for (int m = 0; m < NC; m++) {
					data[n][m] = d0[n][m];
				}
			} else {
				// for (int m=0; m<NC; m++) {
				// data[n][m]= 0;
				// }
				int ind = this.getIndexAccAddedData(accAddedData, n
						- initNbData);
				// System.out.println("n="+n+"  -> ind="+ind);
				if (n < initNbData + accAddedData[ind - 1]) {
					for (int m = 0; m < NC; m++) {
						if (m == NC - 1)
							data[n][m] = ind;
						else
							data[n][m] = dd[ind - 1].GetData()[r][m];
					}
					r++;
					if (r == dd[ind - 1].GetData().length) {
						// System.out.println("r="+r);
						r = initNbData;
					}
				}
			}
		}
		File fnew = new File(this.trainingDataFile.FileName() + ".smote.txt");
		this.syntDataFileName = fnew.getAbsolutePath();
		// System.out.println("newDataOversampledFile="+fnew.getAbsolutePath());
		this.saveSyntheticDataFile(this.syntDataFileName, data, NR, NC);
		JExtendedDataFile res = new JExtendedDataFile(fnew.getAbsolutePath(),
				true);
		return res;
	}

	// ------------------------------------------------------------------------------
	private int getIndexAccAddedData(int[] data, int k) {
		for (int n = 0; n < data.length; n++) {
			if (k < data[n])
				return n + 1;
		}
		return 0;
	}

	// ------------------------------------------------------------------------------
	private void buildConfFiles(String[] filenames, double[] NewSamples) throws Throwable {
		String runkeelxml = MainKBCT.getConfig().GetKbctPath()
				+ System.getProperty("file.separator") + "RunKeel.xml";

		// System.out.println("runkeelxml -> "+runkeelxml);
		PrintStream ps = new PrintStream(
				new FileOutputStream(runkeelxml, false));
		ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ps.println("<execution>");
		String lib = MainKBCT.getConfig().GetKbctPath()
				+ System.getProperty("file.separator") + "libs"
				+ System.getProperty("file.separator") + "keellibs"
				+ System.getProperty("file.separator") + "SMOTE-I.jar";
		for (int n = 0; n < filenames.length; n++) {
			ps.println("<sentence>");
			ps.println("<command>java</command>");
			ps.println("<option>-Xmx512m</option>");
			ps.println("<option />");
			ps.println("<option>-jar</option>");
			ps.println("<option />");
			ps.println("<option />");
			ps.println("<executableFile>" + lib + "</executableFile>");
			String confFile = JKBCTFrame.BuildFile(
					"smoteConf" + String.valueOf(n + 1) + ".txt")
					.getAbsolutePath();
			ps.println("<scriptFile>" + confFile + "</scriptFile>");
			ps.println("</sentence>");
			this.saveSMOTEconfFile(filenames[n] + ".dat", confFile,
					NewSamples[n]);
		}
		ps.println("</execution>");
		ps.flush();
		ps.close();
	}

	// ------------------------------------------------------------------------------
	private void saveSMOTEconfFile(String dfile, String fname, double NewSamples) throws Throwable {
		// System.out.println("smoteConfFile -> "+fname);
		PrintStream ps = new PrintStream(new FileOutputStream(fname, false));
		ps.println("algorithm = SMOTE");
		// SMOTE: Synthetic Minority Over-sampling Technique
		// N.V. Chawla, K.W. Bowyer, L.O. Hall, W.P. Kegelmeyer.
		// SMOTE: synthetic minority over-sampling technique.
		// Journal of Artificial Intelligence Research 16 (2002) 321-357

		// SMOTE generate positive data instances from other instances in the
		// original dataset
		// selecting k nearest neighbors and using them to perform arithmetical
		// perations to generate the new instance

		ps.println("inputData = \"" + dfile + "\" \"" + dfile + "\"");
		ps.println("outputData = \"" + dfile + ".smote.dat\" \"" + dfile
				+ ".smote.tst.dat\"");
		ps.println("");
		ps.println("seed = 1286082570");
		// ps.println("Number of Neighbors = 5");
		ps.println("Number of Neighbors = "
				+ MainKBCT.getConfig().GetSMOTEnumberOfNeighbors());
		// generate samples as neighbors of both (minority and majority) classes
		// ps.println("Type of SMOTE = both");
		// generate samples as neighbors ONLY of the minority class
		// ps.println("Type of SMOTE = minority");
		ps.println("Type of SMOTE = " + MainKBCT.getConfig().GetSMOTEtype());
		// generate samples as neighbors ONLY of the majority class
		// ps.println("Type of SMOTE = ASMO");
		// If we want the data class distribution completely balanced
		// ps.println("Balancing = YES");
		// otherwise
		// ps.println("Balancing = NO");
		// If Balancing:YES
		// NewSamples=1
		// esle
		// NewSamples=diff respect to the majority class in the original
		// training dataset (in %)
		if (MainKBCT.getConfig().GetSMOTEbalancing()) {
			if (MainKBCT.getConfig().GetSMOTEbalancingALL()) {
				ps.println("Balancing = YES");
				ps.println("Quantity of generated examples = " + 1);
			} else {
				// balancing according to the majority class
				ps.println("Balancing = NO");
				ps.println("Quantity of generated examples = " + NewSamples);
			}
		} else {
			ps.println("Balancing = NO");
			// adding a percentage of samples to the minority class
			ps.println("Quantity of generated examples = "
					+ MainKBCT.getConfig().GetSMOTEquantity());
		}
		// Distance Function: K-NN implements two distance functions.
		// a) Euclidean with normalized attributed
		// b) HVDM (see paper D.R. Wilson, T.R. Martinez. Reduction Tecniques
		// For Instance-Based Learning Algorithms. Machine Learning 38:3 (2000)
		// 257-286.)
		// ps.println("Distance Function = HVDM");
		// ps.println("Distance Function = Euclidean");
		ps.println("Distance Function = "
				+ MainKBCT.getConfig().GetSMOTEdistance());
		// Type of Interpolation: way of interpolating the neighbors instances
		// to create a synthetic instance.
		// Standard is the original interpolation proposed.
		// ps.println("Type of Interpolation = standard");
		ps.println("Type of Interpolation = "
				+ MainKBCT.getConfig().GetSMOTEinterpolation());
		// Alpha: alpha parameter for the BLX-alpha interpolation
		// ps.println("Alpha = 0.5");
		ps.println("Alpha = " + MainKBCT.getConfig().GetSMOTEalpha());
		// Mu: mu parameter for the SBX interpolation
		// ps.println("Mu = 0.5");
		ps.println("Mu = " + MainKBCT.getConfig().GetSMOTEmu());
		ps.flush();
		ps.close();
	}

	// ------------------------------------------------------------------------------
	private void saveAsKEELfile(String[] files, JExtendedDataFile[] jedd) throws Throwable {
	      DecimalFormat df= new DecimalFormat();
	      df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
	      df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
	      DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
	      dfs.setDecimalSeparator((new String(".").charAt(0)));
	      df.setDecimalFormatSymbols(dfs);
	      df.setGroupingSize(20);
	      String prob = this.fis.GetName();
		  //System.out.println("fisname -> "+prob);
			int Nins = this.fis.NbInputs();
		  //System.out.println("fisinputs -> "+Nins);
		  for (int n=0; n<files.length; n++) {
			   double[][] data= jedd[n].GetData();
			   int NR= jedd[n].DataLength();
			   int NC= Nins+1;
			   //System.out.println("file_name -> "+files[n]);
			   PrintStream fOut= new PrintStream(new FileOutputStream(files[n]+".dat", false));
			   fOut.println("@relation "+prob);
			   String inputnames="";
			   for (int m=0; m<Nins; m++) {
				    String iname= "at"+String.valueOf(m+1);
				    inputnames= inputnames+iname;
				    if (m<Nins-1)
					    inputnames= inputnames+", ";
				    	
					JInput in = this.fis.GetInput(m);
					double[] range = in.GetRange();
				    //System.out.println("lowerange -> "+range[0]);
				    //System.out.println("uppeerange -> "+range[1]);
					double min= range[0];
					double max= range[1];
				    for (int k=0; k<NR; k++) {
				    	 //if (m==0)
				    		// System.out.println(data[k][m]);
						 if (data[k][m] < min)
							 min= data[k][m];
						 
						 if (data[k][m] > max)
							 max= data[k][m];
					}
				    //fOut.println("@attribute "+iname+" real ["+range[0]+", "+range[1]+"]");
				    fOut.println("@attribute "+iname+" real ["+df.format(min)+", "+df.format(max)+"]");
			   }
			   fOut.println("@attribute class {1.000, 2.000}");
			   fOut.println("@inputs "+inputnames);
			   fOut.println("@outputs class");
			   fOut.println("@data");
			   // print data
			   this.saveSyntheticDataFile(fOut, data, NR, NC);
		       fOut.flush();
		       fOut.close();
		  }
		/*String prob = this.fis.GetName();
		// System.out.println("fisname -> "+prob);
		int Nins = this.fis.NbInputs();
		// System.out.println("fisinputs -> "+Nins);
		for (int n = 0; n < files.length; n++) {
			// System.out.println("file_name -> "+files[n]);
			PrintStream fOut = new PrintStream(new FileOutputStream(files[n]
					+ ".dat", false));
			fOut.println("@relation " + prob);
			String inputnames = "";
			for (int m = 0; m < Nins; m++) {
				String iname = "at" + String.valueOf(m + 1);
				inputnames = inputnames + iname;
				if (m < Nins - 1)
					inputnames = inputnames + ", ";

				JInput in = this.fis.GetInput(m);
				double[] range = in.GetRange();
				// System.out.println("lowerange -> "+range[0]);
				// System.out.println("uppeerange -> "+range[1]);
				fOut.println("@attribute " + iname + " real [" + range[0]
						+ ", " + range[1] + "]");
			}
			fOut.println("@attribute class {1.000, 2.000}");
			fOut.println("@inputs " + inputnames);
			fOut.println("@outputs class");
			fOut.println("@data");
			// print data
			double[][] data = jedd[n].GetData();
			int NR = jedd[n].DataLength();
			int NC = Nins + 1;
			this.saveSyntheticDataFile(fOut, data, NR, NC);
			fOut.flush();
			fOut.close();
		}*/
	}

	// ------------------------------------------------------------------------------
	private void saveSyntheticDataFile(String file_name, double[][] data, int NR, int NC) throws Exception {
		PrintStream ps = new PrintStream(new FileOutputStream(file_name, false));
		this.saveSyntheticDataFile(ps, data, NR, NC);
		ps.flush();
		ps.close();
	}

	// ------------------------------------------------------------------------------
	private void saveSyntheticDataFile(PrintStream ps, double[][] data, int NR, int NC) throws Exception {
		for (int n = 0; n < NR; n++) {
			for (int m = 0; m < NC; m++) {
				ps.print(this.df.format(data[n][m]));
				if (m < NC - 1)
					ps.print(",");
			}
			ps.println();
		}
	}

	// ------------------------------------------------------------------------------
	private void callLinksSyntheticData(JExtendedDataFile data, String file_name) throws Throwable {
		this.fis.Links(data, this.linksThres, false, file_name, null);
	}

	// ------------------------------------------------------------------------------
	private int computeRuleLength(Rule r) {
		int result = 0;
		int[] premises = r.Get_in_labels_number();
		for (int n = 0; n < premises.length; n++) {
			if (premises[n] > 0)
				result++;
		}
		return result;
	}

	// ------------------------------------------------------------------------------
	private int getRuleOutput(Rule r) {
		int[] concs = r.Get_out_labels_number();
		return concs[0];
	}

	// ------------------------------------------------------------------------------
	private double[][] readItemsRulesFile(String file, boolean norm, String opt, String Conf) {
		// opt -> option for normalization
		// opt=MAX -> divide by maximum value in the matrix
		// opt=NRi -> divide by the cardinal of the subset Ei of items
		// activating i-th rule
		// System.out.println("file= "+file);
		// System.out.println("norm= "+norm);
		// System.out.println("opt= "+opt);
		// System.out.println("NbActRules= "+this.NbActiveRules);
		int[] NAR = new int[this.NbRules];
		double[][] matrix = new double[this.NbRules][this.NbRules];
		for (int n = 0; n < this.NbRules; n++) {
			NAR[n] = 0;
			for (int m = 0; m < this.NbRules; m++) {
				matrix[n][m] = 0;
			}
		}
		try {
			LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(file)));
			String l;
			double max = 0;
			int cd=0;
			while ((l = lnr.readLine()) != null) {
				int pos = l.indexOf(",");
				if (pos > 0) {
				  //Integer d= new Integer(l.substring(0,pos));
				  //System.out.println("d -> "+d);
				  String aux = l.substring(pos + 2);
				  //System.out.println("aux -> "+aux);
				  pos = aux.indexOf(",");
				  //System.out.println("pos -> "+pos);
				  if ( (pos > 0) || (pos==-1) ) {
					Vector<Integer> values = new Vector<Integer>();
					while (pos > 0) {
						Integer v = new Integer(aux.substring(0, pos));
						values.add(v);
						aux = aux.substring(pos + 2);
						pos = aux.indexOf(",");
						// System.out.println("v -> "+v.intValue());
						// System.out.println("aux -> "+aux);
					}
					Integer v = new Integer(aux);
					// System.out.println("v -> "+v.intValue());
					values.add(v);
					Object[] obj = values.toArray();
					for (int n = 0; n < obj.length; n++) {
						int indr= ((Integer) obj[n]).intValue();
						int row = this.getRuleActiveIndex(indr);
						//System.out.println("cd="+cd);
						//System.out.println("indr="+indr);
						//System.out.println("this.murules="+this.murules.length);
						if ((this.murules[cd][indr-1] > MainKBCT.getConfig().GetBlankThres())) {
						    NAR[row]++;
						// int row= ((Integer)obj[n]).intValue()-1;
						for (int m = 0; m < obj.length; m++) {
							int indc= ((Integer) obj[m]).intValue();
							int column = this.getRuleActiveIndex(indc);
							// int column= ((Integer)obj[m]).intValue()-1;
							// System.out.println("r: "+row+"  c: "+column);
							boolean flag = false;
							if (((Conf.equals("AC")) || (Conf.equals("SC")))
									&& (this.outClasses[row] == this.outClasses[column])) {
								flag = true;
							}
							if (this.outClasses[row] > 0
									&& this.outClasses[column] > 0) {
								if ( (!flag) && (this.murules[cd][indc-1] > MainKBCT.getConfig().GetBlankThres()) ) 
									matrix[row][column]++;

								if ((norm) && (row == column)
										&& (matrix[row][column] > max)
										&& (!flag))
									max = matrix[row][column];
							}
						}
					  }
					}
				}
			  }	
			  cd++;
			}
			lnr.close();
			//for (int n=0; n<NAR.length; n++) {
				// System.out.println("rule "+String.valueOf(n+1)+" -> "+NAR[n]);
			//}
			if (Conf.equals("S")) {
				this.FR = new double[this.NbRules];
				PrintStream fOut = new PrintStream(new FileOutputStream(
						JKBCTFrame.BuildFile("LogFISMAPS.txt"), false));
				fOut.println("Fired Rules");
				fOut.println("===========");
				for (int n = 0; n < this.NbRules; n++) {
					fOut.println("R" + String.valueOf(n + 1) + " -> " + NAR[n]);
					FR[n] = NAR[n];
				}
				fOut.println();
				fOut.println("Simultaneously Fired Rules");
				fOut.println("==========================");
				// this.SFR= new double[this.NbRules][this.NbRules];
				for (int n = 0; n < this.NbRules; n++) {
					for (int m = 0; m < this.NbRules; m++) {
						fOut.println("R" + String.valueOf(n + 1) + " and R"
										+ String.valueOf(m + 1) + " -> "
										+ matrix[n][m]);
						// SFR[n][m]= matrix[n][m];
					}
				}
				fOut.flush();
				fOut.close();
			}
			if ((Conf.equals("S")) || (Conf.equals("SC"))) {
				// Compute interpretability index
				// S -> redundancies and inconsistencies
				// SC -> only inconsistencies
				double total = 0;
				for (int n = 0; n < this.NbRules; n++) {
					for (int m = 0; m < this.NbRules; m++) {
						// total= total +
						// (this.NbPrem[n]+this.NbPrem[m])*matrix[n][m];
						if ( (this.outClasses[n] > 0) && (this.outClasses[m] > 0) && (FR[n]*FR[m] > 0) )
							total = total + (this.NbPrem[n] + this.NbPrem[m])* matrix[n][m]/(Math.pow(FR[n] * FR[m], 0.5));
						//System.out.println("total="+total+"  this.NbPrem["+n+"]="+this.NbPrem[n]+"  this.NbPrem["+m+"]="+this.NbPrem[m]+"  matrix["+n+"]["+m+"]="+matrix[n][m]);
					}
				}
				// System.out.println("Total="+total);
				// System.out.println("NbRules="+this.NbRules);
				// System.out.println("NbData="+this.syntheticDataFile.DataLength());
				// double lim=
				// this.NbRules*this.NbRules*this.syntheticDataFile.DataLength();
				double lim = this.LimMax;
				// double lim= 100000;
				//System.out.println("Lim="+lim);
				//System.out.println("CI="+total);
				if (Conf.equals("S")) {
					this.cis = 1 - Math.pow(total / lim, 0.5);
					if (this.cis < 0)
						this.cis = 0;
					//System.out.println("this.cis -> "+this.cis);
				}
				if (Conf.equals("SC")) {
					this.cisc = 1 - Math.pow(total / lim, 0.5);
					if (this.cisc < 0)
						this.cisc = 0;
				}

				// System.out.println("CISC="+this.cisc);
				// this.cisc= Math.pow(this.cisc,3);
			}
			// normalize matrix
			// System.out.println("max="+max);
			if (norm) {
				if ((Conf.equals("S")) || (Conf.equals("SC"))) {
					for (int n = 0; n < this.NbRules; n++) {
						// System.out.println(" NAR["+n+"]="+NAR[n]);
						for (int m = 0; m < this.NbRules; m++) {
							// System.out.println(" NAR["+m+"]="+NAR[m]);
							// System.out.println(" m["+n+"]["+m+"]="+matrix[n][m]);
							if ((NAR[n] > 0) && (NAR[m] > 0)) {
								double num = matrix[n][m];
								double den = Math.pow(NAR[n] * NAR[m], 0.5);
								//double den = Math.pow(matrix[n][n] * matrix[m][m], 0.5);
								matrix[n][m] = num / den;
							} /*else {
								System.out.print("WARNING: Not fired rule -> ");
								if (NAR[n] == 0)
									System.out.println("R"+ String.valueOf(n + 1));
								else
									System.out.println("R"+ String.valueOf(m + 1));
							}*/
							// System.out.println(" normm["+n+"]["+m+"]="+matrix[n][m]);
						}
					}
				} else if (opt.equals("MAX")) {
					// System.out.println(" --> MAX");
					for (int n = 0; n < this.NbRules; n++) {
						for (int m = 0; m < this.NbRules; m++) {
							if (max > 0)
								matrix[n][m] = (matrix[n][m]) / max;
						}
					}
				} else if (opt.equals("NRi")) {
					// System.out.println(" --> NRi");
					for (int n = 0; n < this.NbRules; n++) {
						// System.out.println(" NAR["+n+"]="+NAR[n]);
						for (int m = 0; m < this.NbRules; m++) {
							// System.out.println(" m["+n+"]["+m+"]="+matrix[n][m]);
							if (NAR[n] > 0)
								matrix[n][m] = (matrix[n][m]) / NAR[n];
							// System.out.println(" normm["+n+"]["+m+"]="+matrix[n][m]);
						}
					}
				}
			}
			for (int n = 0; n < this.NbRules; n++) {
				 matrix[n][n] = 0;
			}
		} catch (Throwable except) {
			except.printStackTrace();
			MessageKBCT.Error(null, except);
		}
		return matrix;
	}

	// ------------------------------------------------------------------------------
	private double[][] readItemsRulesFiringDegreeFile(String file, boolean norm, String Conf) {
		double[][] matrix = new double[this.NbRules][this.NbRules];
		for (int n = 0; n < this.NbRules; n++) {
			for (int m = 0; m < this.NbRules; m++) {
				matrix[n][m] = 0;
			}
		}
		try {
			LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(file)));
			String l;
			double max = 0;
			while ((l = lnr.readLine()) != null) {
				int pos = l.indexOf(",");
				// Integer d= new Integer(l.substring(0,pos));
				// System.out.println("d -> "+d);
				String aux = l.substring(pos + 2);
				// System.out.println("aux -> "+aux);
				pos = aux.indexOf(",");
				if (pos > 0) {
					Vector<Double> values = new Vector<Double>();
					while (pos > 0) {
						Double v = new Double(aux.substring(0, pos));
						values.add(v);
						aux = aux.substring(pos + 2);
						pos = aux.indexOf(",");
						// System.out.println("v -> "+v.doubleValue());
						// System.out.println("aux -> "+aux);
					}
					Double v = new Double(aux);
					// System.out.println("v -> "+v.intValue());
					values.add(v);
					Object[] obj = values.toArray();
					for (int n = 0; n < obj.length; n++) {
						if (((Double) obj[n]).doubleValue() > 0) {
							int row = this.getRuleActiveIndex(n + 1);
							for (int m = 0; m < obj.length; m++) {
								if (((Double) obj[m]).doubleValue() > 0) {
									int column = this.getRuleActiveIndex(m + 1);
									// System.out.println("r: "+row+"  c: "+column);
									double joinvalue = 1;
									// if (flag)
									// joinvalue= joinvalue -
									// (Math.abs(((Double)obj[n]).doubleValue()
									// - ((Double)obj[m]).doubleValue()));
									// else {
									if (row != column) {
										double diff = Math
												.abs(((Double) obj[n])
														.doubleValue()
														- ((Double) obj[m])
																.doubleValue());
										double maxaux = Math
												.max(((Double) obj[n])
														.doubleValue(),
														((Double) obj[m])
																.doubleValue());
										// joinvalue= joinvalue - (diff/maxaux);
										// joinvalue= joinvalue -
										// ((Double)obj[n]).doubleValue();
										joinvalue = diff / maxaux;
									}
									// }
									/*
									 * double joinvalue= 0; if (
									 * (JConvert.disjunction!=null) &&
									 * (JConvert.disjunction.equals("sum")) ) {
									 * //System.out.println("disj= "+JConvert.
									 * disjunction); joinvalue=
									 * ((Double)obj[n]).
									 * doubleValue()+((Double)obj
									 * [m]).doubleValue(); } else { joinvalue=
									 * Math.max( ((Double)obj[n]).doubleValue(),
									 * ((Double)obj[m]).doubleValue() ); }
									 */
									boolean warn = false;
									if ((Conf.equals("SFDC"))
											&& (this.outClasses[row] == this.outClasses[column])) {
										warn = true;
									}
									if (this.outClasses[row] > 0
											&& this.outClasses[column] > 0) {
										if (!warn)
											matrix[row][column] = matrix[row][column]
													+ joinvalue;

										if ((norm)
												&& (matrix[row][column] > max)
												&& (!warn))
											max = matrix[row][column];
									}
								}
							}
						}
					}
				}
			}
			lnr.close();
			// normalize matrix
			// System.out.println("max="+max);
			if (norm) {
				for (int n = 0; n < this.NbRules; n++) {
					for (int m = 0; m < this.NbRules; m++) {
						// System.out.println("m["+n+"]["+m+"]="+matrix[n][m]);
						if (max > 0)
							matrix[n][m] = (matrix[n][m]) / max;
						// System.out.println("mnorm["+n+"]["+m+"]="+matrix[n][m]);
					}
				}
			}
		} catch (Throwable except) {
			except.printStackTrace();
			MessageKBCT.Error(null, except);
		}
		return matrix;
	}

	// ------------------------------------------------------------------------------
	private int getRuleActiveIndex(int r) {
		// System.out.println("getRuleActiveIndex: "+r);
		// int res= -1;
		// int lim= this.activeRules.length;
		// int lim= this.NbRules;
		int goal = this.itemsRulesNumbers[r - 1].intValue();
		// System.out.println("-> "+goal);
		/*
		 * for (int n=0; n<lim; n++) {
		 * //System.out.println("-> "+this.activeRules[n]); if
		 * (this.activeRules[n]==goal) return n; } return res;
		 */
		return goal - 1;
	}

	// ------------------------------------------------------------------------------
	private int runProcess(String command, String tempOutFile) {
		int exitVal = -1;
		Process p = null;
		try {
		    //System.out.println("runProcess: 1 -> "+command);
			Runtime rt = Runtime.getRuntime();
			String fsvgout= null;
			if ( (command.contains("dot")) && (command.contains("-o")) ){
			    //System.out.println("runProcess: cmd -> "+command);
			    String[] splitOpt= command.split("-o");
			    //System.out.println("   >>> s1 -> "+splitOpt[0].substring(0, splitOpt[0].length()-1));
			    String[] splitFiles= splitOpt[1].substring(1, splitOpt[1].length()).split(" ");
			    //System.out.println("   >>> s2 -> "+splitFiles[0]);
			    //System.out.println("   >>> s3 -> "+splitFiles[1]);
			    command= splitOpt[0].substring(0, splitOpt[0].length()-1) + " " +splitFiles[1];
			    fsvgout= splitFiles[0];
			    //System.out.println("runProcess: cmd -> "+command);
			}
		    //System.out.println("runProcess: 2 -> "+command);
	        //Date d= new Date(System.currentTimeMillis());
			//System.out.println("runProcess: T1 -> "+DateFormat.getDateTimeInstance().format(d));
			p = rt.exec(command);
	        //d= new Date(System.currentTimeMillis());
			//System.out.println("runProcess: T2 -> "+DateFormat.getDateTimeInstance().format(d));
			// cleaning output buffer
		    InputStream is = p.getInputStream();
		    InputStreamReader isr = new InputStreamReader(is);
		    BufferedReader br = new BufferedReader(isr);
			if ( (tempOutFile.startsWith("tempOutGraph.txt")) && (fsvgout!=null) ) {
				  //System.out.println("NR: "+this.fis.NbRules());
			      //System.out.println("runProcess: 2");
			      exitVal= this.writeParsedSVGBuffer(br,fsvgout);
			} else {
			    FileOutputStream fos = new FileOutputStream(JKBCTFrame.BuildFile(tempOutFile), false);
			    PrintStream pOutFile = new PrintStream(fos);
			    String line;
	            //d= new Date(System.currentTimeMillis());
			    //System.out.println("runProcess: T3 -> "+DateFormat.getDateTimeInstance().format(d));
			    while ((line = br.readLine()) != null)
				       pOutFile.println(line);

                br.close();
			    pOutFile.flush();
			    pOutFile.close();
			    fos.close();
				exitVal = p.waitFor();
			}
		    //System.out.println("runProcess: 3");
		    is.close();
		    isr.close();
			// p.waitFor();
			// System.out.println("runProcess: waiting.......");
	        //d= new Date(System.currentTimeMillis());
			//System.out.println("runProcess: T4 -> "+DateFormat.getDateTimeInstance().format(d));
		    //System.out.println("runProcess: 4");
		} catch (Throwable e) {
			e.printStackTrace();
			MessageKBCT.Error(null, e);
			p.destroy();
		}
	    //System.out.println("runProcess: 5");
		return exitVal;
	}
	// ------------------------------------------------------------------------------
	private int writeParsedSVGBuffer(BufferedReader originalSVG, String fileName) {
		int result= -1;
		try {
			//System.out.println("INI: "+fileName);
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName), false));
			//String all="";
			String strLine=originalSVG.readLine();
			//System.out.println(strLine);
	        while( (strLine!=null) && (!strLine.equals("")) ) {
				//System.out.println(strLine);
				if (strLine.contains("<!-- Rule")) {
					//System.out.println(strLine);
					bw.write(strLine);
					bw.newLine();

					String first = originalSVG.readLine();
					//System.out.println(first);
					String second = originalSVG.readLine();
					//System.out.println(second);

					String[] splits = first.split("<title>");
					String[] splits2 = second.split("\"");

					strLine = splits[0] + "<title>" + splits2[1] + "</title>";
					// System.out.println(strLine);
					bw.write(strLine);
					bw.newLine();

					// System.out.println(second);
					bw.write(second);
					bw.newLine();

				} else if (!strLine.contains("<title>G</title>")) {
					// System.out.println(strLine);
					bw.write(strLine);
					bw.newLine();
				}
				if (strLine.contains("</svg>")) {
					strLine= null;
					result= 0;
				} else
				    strLine=originalSVG.readLine();
			}
			//System.out.println("END");
			// Close the input stream
			originalSVG.close();
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			result= -1;
		} catch (IOException e) {
			e.printStackTrace();
			result= -1;
		}
		return result;
	}
	// ------------------------------------------------------------------------------
	private void setSMOTEparameters() {
		String Balancing = LocaleKBCT.GetString("No");
		if (MainKBCT.getConfig().GetSMOTEbalancing())
			Balancing = LocaleKBCT.GetString("Yes");

		String message = LocaleKBCT.GetString("SelectedValues") + ":" + "\n"
				+ "   " + LocaleKBCT.GetString("SMOTEnumberOfNeighbors") + "= "
				+ MainKBCT.getConfig().GetSMOTEnumberOfNeighbors() + "\n"
				+ "   " + LocaleKBCT.GetString("SMOTEtype") + "= "
				+ MainKBCT.getConfig().GetSMOTEtype() + "\n" + "   "
				+ LocaleKBCT.GetString("SMOTEbalancing") + "= " + Balancing
				+ "\n" + "   (" + LocaleKBCT.GetString("All") + "= "
				+ MainKBCT.getConfig().GetSMOTEbalancingALL() + ")\n" + "   "
				+ LocaleKBCT.GetString("SMOTEquantity") + "= "
				+ MainKBCT.getConfig().GetSMOTEquantity() + "\n" + "   "
				+ LocaleKBCT.GetString("SMOTEdistance") + "= "
				+ MainKBCT.getConfig().GetSMOTEdistance() + "\n" + "   "
				+ LocaleKBCT.GetString("SMOTEinterpolation") + "= "
				+ MainKBCT.getConfig().GetSMOTEinterpolation() + "\n" + "   "
				+ LocaleKBCT.GetString("SMOTEalpha") + "= "
				+ MainKBCT.getConfig().GetSMOTEalpha() + "\n" + "   "
				+ LocaleKBCT.GetString("SMOTEmu") + "= "
				+ MainKBCT.getConfig().GetSMOTEmu() + "\n" + "\n"
				+ LocaleKBCT.GetString("OnlyExpertUsers") + "\n"
				+ LocaleKBCT.GetString("DoYouWantToChangeSelectedValues");
		int option = MessageKBCT.Confirm(this, message, 1, false, false, false);
		if (option == 0) {
			this.Parent.parent.changeSMOTEParameters();
		}
	}
	// ------------------------------------------------------------------------------
	/*
	 * private double computeMetrics(double[] weights, double[][] matrix) {
	 * double res=0; return res; }
	 */
	// ------------------------------------------------------------------------------
	public double getCIS() {
		return this.cis;
	}
	// ------------------------------------------------------------------------------
	/*public double[] getMSDoC() {
		return this.MSDoC;
	}*/
	// ------------------------------------------------------------------------------
	public boolean getCancelFlag() {
		return this.cancel;
	}
}