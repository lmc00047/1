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
//                              JMainFrame.java
//
//
//**********************************************************************

package kbctFrames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.TitledBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import kbct.JConvert;
import kbct.JFIS;
import kbct.JFISFPA;
import kbct.JFISHFP;
import kbct.JGENFIS;
import kbct.JInputHFP;
import kbct.JKBCT;
import kbct.JKBCTInput;
import kbct.JKBCTOutput;
import kbct.JVariable;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbct.jnikbct;
import kbctAux.DoubleField;
import kbctAux.IntegerField;
import kbctAux.JFileFilter;
import kbctAux.JOpenFileChooser;
import kbctAux.MessageKBCT;
import kbctAux.ParserWekaFiles;
import xml.XMLParser;
import xml.XMLWriter;
import KB.LabelKBCT;
import KB.Rule;
import KB.variable;
import fis.JExtendedDataFile;
import fis.JPanelGenerateSample;
import fis.JRule;
import fis.JSemaphore;
import fis.jnifis;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.classifiers.trees.J48;

import xfuzzy.lang.*;
import xfuzzy.xfj.*;

/**
 * kbctFrames.JMainFrame generate the main frame which is launched
 * when KBCT is started.
 * <ul>
 *   <li>  User can open a KB or data file. </li>
 *   <li>  User can launch GUAJE with an open KB file. </li>
 *   <li>  User can do inductions (partitions or rules) with data file. </li>
 * </ul>
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
public class JMainFrame extends JKBCTFrame {
  static final long serialVersionUID=0;	
  private boolean KB = false;
  public String IKBFile="";
  public String KBDataFile="";
  public String OrigKBDataFile="C:/GUAJE/IRIS.txt";//????
  private String SelectedVariablesDataFile="";
  private String oldIKBFile="";
  private String oldKBExpertFile="C:/GUAJE/IRIS.txt.kb.xml";//aquiiiiiiiiiiiii
  private String oldKBDataFile="C:/GUAJE/IRIS.txt"; //aquiiiiiiiii
  private String oldConjunction= null;
  private String[] oldDisjunction= null;
  private String[] oldDefuzzification= null;
  private String[] oldKBintPath= null;
  private String[] KBintPath= null;
  private String oldOntologyFile="";
  protected JKBCT kbct= null;
  protected int DataNbVariables=0;
  protected JExtendedDataFile DataFile= null;
  protected JExtendedDataFile DataFileNoSaved= null;
  private String DFile= null;
  private String DFileNoSaved= null;
  private String[] VNames= null;
  protected Vector variables= null;
  protected Vector OLD_variables= null;
  protected JKBCT kbct_Data= null;
  private boolean DataFileLabels= false;
  // gestion de menus
  private JMenuBar jMenuBarKBCT = new JMenuBar();
  // menu kbct
  private JMenu jMenuKBCT = new JMenu();
  private JMenuItem jMenuNew = new JMenuItem();
  private JMenuItem jMenuOpen = new JMenuItem();
  private JMenuItem jMenuClose = new JMenuItem();
  private JMenuItem jMenuSave = new JMenuItem();
  private JMenuItem jMenuSaveAs = new JMenuItem();
  private JMenuItem jMenuSetAutomaticConfiguration = new JMenuItem();
  private JMenuItem jMenuSynthesis = new JMenuItem();
  private JMenuItem jMenuExit = new JMenuItem();
  // menu expert
  private JMenu jMenuMExpert = new JMenu();
  private JMenuItem jMenuExpertNew = new JMenuItem();
  private JMenuItem jMenuExpertOpen = new JMenuItem();
  private JMenuItem jMenuExpertClose = new JMenuItem();
  private JMenuItem jMenuExpertSaveAs = new JMenuItem();
  // menu data
  private JMenu jMenuMData = new JMenu();
  private JMenuItem jMenuDataOpen = new JMenuItem();
  private JMenuItem jMenuDataClose = new JMenuItem();
  private JMenuItem jMenuDataView = new JMenuItem();
  private JMenuItem jMenuDataTable = new JMenuItem();
  private JMenuItem jMenuDataSaveAs = new JMenuItem();
  private JMenuItem jMenuDataSmote = new JMenuItem();
  private JMenuItem jMenuDataFeatureSelection = new JMenuItem();
  private JMenu jMenuDataGenerate = new JMenu();
  private JMenuItem jMenuDataGenerateSample = new JMenuItem();
  private JMenuItem jMenuDataGenerateCrossValidation = new JMenuItem();
  private JMenu jMenuDataInduce = new JMenu();
  private JMenuItem jMenuDataInducePartitions = new JMenuItem();
  private JMenuItem jMenuDataInduceRules = new JMenuItem();
  private JMenuItem jMenuDataBuildKB = new JMenuItem();
  
  private JPanel jPHeader= new JPanel(new GridBagLayout());
  private JPanel jPinfo= new JPanel(new GridBagLayout());
  private JPanel jPFMI= new JPanel(new GridBagLayout());
  private JButton jSetFISoptionsButton= new JButton();
  private JButton jGenerateFISButton= new JButton();
  private TitledBorder titledBorderFISconf;
  private JPanel jPContext= new JPanel(new GridBagLayout());
  private JPanel jPContextNumbers= new JPanel(new GridBagLayout());
  private JTextField jTFcontextName = new JTextField();
  private IntegerField jTFcontextNumberInputs = new IntegerField();
  private IntegerField jTFcontextNumberClasses = new IntegerField();
  private JPanel jPButtons= new JPanel(new GridBagLayout());
  private JPanel jPExpertButton= new JPanel(new GridBagLayout());
  private JPanel jPDataButton= new JPanel(new GridBagLayout());
  private JPanel jPanelPanels= new JPanel(new GridBagLayout());
  private JButton jExpertButton= new JButton();
  private JButton jDataButton= new JButton();
  // context
  private TitledBorder titledBorderContext;
  private TitledBorder titledBorderContextName;
  private TitledBorder titledBorderContextNumberInputs;
  private TitledBorder titledBorderContextNumberClasses;
  // name of expert Knowledge Base
  private TitledBorder titledBorderKBmodeling;
  private TitledBorder titledBorderExpertName;
  // name of data Knowledge Base
  private TitledBorder titledBorderDataName;
  private JTextField jTFDataName = new JTextField();
  // induce rules
  private IntegerField jIFMinCard = new IntegerField();
  private DoubleField jDFMinDeg = new DoubleField();
  private JComboBox jCBStrategy = new JComboBox();
  private DoubleField jThreshold = new DoubleField();
  private IntegerField jClustersNumber = new IntegerField();
  private IntegerField jFoldsNumber = new IntegerField();
  private JExtendedDataFile dataSelect= null;
  private JFIS fis_file_InduceRules;
  
  // select data variable
  Vector CheckBoxs = new Vector();
  
  // Induce partitions
  public static Vector JRFs;
  protected boolean DataFileModificationSimplify= false;
  
  // Interpretability Parameter
  private TextField jProblem = new TextField();
  private IntegerField jNbInputs = new IntegerField();
  private IntegerField jNbClasses = new IntegerField();
  private IntegerField jMaxNbRules = new IntegerField();
  private IntegerField jMaxNbPremises = new IntegerField();
  private DoubleField jParameterL = new DoubleField();
  private DoubleField jParameterM = new DoubleField();
  private IntegerField jLimMaxIntIndex = new IntegerField();
  protected double ValidationDataRatio= 0.8;
  protected int Seed= 2;
    
//------------------------------------------------------------------------------
  public JMainFrame() {
    try {
      jbInit();
      this.InitJKBCTFrameWithKBCT();
    } catch(Throwable t) {
        this.dispose();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in constructor of JMainFrame: "+t);
    }
  }
//------------------------------------------------------------------------------
    //private void jbInit() throws Throwable {
    public void jbInit() throws Throwable {

    MainKBCT.getConfig().SetFingramsSelectedSample(LocaleKBCT.DefaultFingramsSelectedSample());
	if (MainKBCT.getConfig().GetTESTautomatic())
	    this.setVisible(false);

	this.setIconImage(icon_guaje.getImage());
    this.setState(Frame.NORMAL);
  
    // menu kbct
    jMenuBarKBCT.add(jMenuKBCT);
    jMenuKBCT.add(jMenuNew);
    jMenuKBCT.add(jMenuOpen);
    jMenuKBCT.add(jMenuClose);
    jMenuKBCT.add(jMenuSave);
    jMenuKBCT.add(jMenuSaveAs);
    jMenuKBCT.addSeparator();
    jMenuKBCT.add(jMenuSetAutomaticConfiguration);
    jMenuKBCT.addSeparator();
    jMenuKBCT.add(jMenuSynthesis);
    jMenuKBCT.addSeparator();
    jMenuKBCT.add(jMenuExit);
    // menu expert
    jMenuBarKBCT.add(jMenuMExpert);
    jMenuMExpert.add(jMenuExpertNew);
    jMenuMExpert.add(jMenuExpertOpen);
    jMenuMExpert.add(jMenuExpertClose);
    jMenuMExpert.add(jMenuExpertSaveAs);
    // menu data
    jMenuBarKBCT.add(jMenuMData);
    jMenuMData.add(jMenuDataOpen);
    jMenuMData.add(jMenuDataClose);
    jMenuMData.add(jMenuDataView);
    jMenuMData.add(jMenuDataTable);
    jMenuMData.add(jMenuDataSaveAs);
    jMenuMData.addSeparator();
    jMenuMData.add(jMenuDataSmote);
    jMenuMData.add(jMenuDataFeatureSelection);
    jMenuMData.addSeparator();
    jMenuMData.add(jMenuDataGenerate);
    jMenuDataGenerate.add(jMenuDataGenerateSample);
    jMenuDataGenerate.add(jMenuDataGenerateCrossValidation);
    jMenuMData.addSeparator();
    jMenuMData.add(jMenuDataInduce);
    jMenuDataInduce.add(jMenuDataInducePartitions);
    jMenuDataInduce.add(jMenuDataInduceRules);
    jMenuDataInduce.add(jMenuDataBuildKB);
  
    // menu options
    jMenuBarKBCT.add(jMenuOptions);
    jMenuOptions.add(jMenuLanguage);
    jMenuLanguage.add(jRBMenuSpanish);
    jMenuLanguage.add(jRBMenuEnglish);
     
    String idiom = MainKBCT.getLocale().Language();
    if (idiom.equals("en"))
       jRBMenuEnglish.setSelected(true);
    else if (idiom.equals("es"))
       jRBMenuSpanish.setSelected(true);

    jMenuOptions.add(jMenuLook);
    String look = MainKBCT.getConfig().GetLookAndFeel();
    for (int i=0; i<this.jRB.length; i++) {
    	jMenuLook.add(this.jRB[i]);
        String aux=this.lafi[i].getClassName();
        if (look.equals(aux))
        	this.jRB[i].setSelected(true);
        else
        	this.jRB[i].setSelected(false);

        this.jRB[i].setActionCommand(aux);
        this.jRB[i].setEnabled(isAvailableLookAndFeel(aux));
      }

      jMenuOptions.add(jMenuUser);
      jMenuUser.add(jRBMenuBeginner);
      jMenuUser.add(jRBMenuExpert);

    String user = MainKBCT.getConfig().GetUser();
    if (user.equals(LocaleKBCT.GetString("Beginner"))) {
       jRBMenuBeginner.setSelected(true);
       jMenuItemHelp.setEnabled(false);
    } else {
      jRBMenuExpert.setSelected(true);
      jMenuItemHelp.setEnabled(true);
    }
    // menu ayuda
    jMenuBarKBCT.add(jMenuHelp);
      jMenuHelp.add(jMenuItemHelp);
      jMenuHelp.add(jMenuItemQuickStart);
      jMenuHelp.add(jMenuAbout);
      jMenuHelp.add(jMenuLicense);

    this.setJMenuBar(jMenuBarKBCT);
    this.getContentPane().setLayout(new GridBagLayout());
    this.getContentPane().add(jPanelPanels, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.jPanelPanels.add(jPHeader, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));
 
    this.jPanelPanels.add(jPinfo, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 20, 0, 20), 0, 0));
    this.jPinfo.add(jPContext, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    titledBorderContext = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    jPContext.setBorder(titledBorderContext);
    titledBorderContextName = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    jTFcontextName.setBorder(titledBorderContextName);
    jTFcontextName.setEnabled(false);
    this.jPContext.add(this.jTFcontextName, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 300, 0));
    this.jPContext.add(jPContextNumbers, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
    titledBorderContextNumberInputs = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    jTFcontextNumberInputs.setBorder(titledBorderContextNumberInputs);
    jTFcontextNumberInputs.setEnabled(false);
    this.jPContextNumbers.add(this.jTFcontextNumberInputs, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
    titledBorderContextNumberClasses = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    jTFcontextNumberClasses.setBorder(titledBorderContextNumberClasses);
    jTFcontextNumberClasses.setEnabled(false);
    this.jPContextNumbers.add(this.jTFcontextNumberClasses, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));

    titledBorderFISconf = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    jPFMI.setBorder(titledBorderFISconf);
    this.jPinfo.add(jPFMI, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 10, 10, 10), 0, 0));
    this.jPFMI.add(jSetFISoptionsButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.jPFMI.add(jGenerateFISButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    this.jPanelPanels.add(jPButtons, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
    this.jPButtons.add(jPExpertButton, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
    titledBorderKBmodeling = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    titledBorderExpertName = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    jPButtons.setBorder(titledBorderKBmodeling);
    jPExpertButton.setBorder(titledBorderExpertName);
    jTFExpertName.setBackground(Color.WHITE);
    jTFExpertName.setForeground(Color.BLACK);
    jTFExpertName.setEditable(false);
    this.jPExpertButton.add(jTFExpertName, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
    this.jPExpertButton.add(jExpertButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
    this.jPButtons.add(jPDataButton, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
    titledBorderDataName = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    jPDataButton.setBorder(titledBorderDataName);
    
    jTFDataName.setBackground(Color.WHITE);
    jTFDataName.setForeground(Color.BLACK);
    jTFDataName.setEditable(false);
    this.jPDataButton.add(jTFDataName, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
    this.jPDataButton.add(jDataButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
    Translate();
    Events();
    JKBCTFrame.JMainFrames.addElement(this);
    JKBCTFrame.AddTranslatable(this);
    this.pack();
    this.jMenuExpertNew.setEnabled(false);
    this.jMenuExpertOpen.setEnabled(false);//AQUI??????????????
    this.jMenuExpertClose.setEnabled(false);
    this.jMenuExpertSaveAs.setEnabled(false);
    this.jMenuDataOpen.setEnabled(false); //AQUIII??????
    this.jMenuDataClose.setEnabled(false);
    this.jMenuDataView.setEnabled(false);
    this.jMenuDataTable.setEnabled(false);
    this.jMenuDataSaveAs.setEnabled(false);
    this.jMenuDataSmote.setEnabled(false);
    this.jMenuDataFeatureSelection.setEnabled(false);
    this.jMenuDataGenerate.setEnabled(false);
    this.jMenuDataGenerateSample.setEnabled(false);
    this.jMenuDataGenerateCrossValidation.setEnabled(false);
    this.jMenuDataInduce.setEnabled(false);
    this.jMenuDataInducePartitions.setEnabled(false);
    this.jMenuDataInduceRules.setEnabled(false);
    this.jMenuDataBuildKB.setEnabled(false);
    //this.jMenuDataFingrams.setEnabled(false);
    this.jSetFISoptionsButton.setEnabled(false);
    this.jGenerateFISButton.setEnabled(false);
    Dimension dim = getToolkit().getScreenSize();
    this.setSize(600, 400);
    this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
    this.setResizable(false);
  }
//------------------------------------------------------------------------------
  public void Translate() {
    this.TranslateTitle();
    this.TranslateMenu();
    this.TranslateOptions();
    this.TranslateFileChooser();
    this.titledBorderFISconf.setTitle(LocaleKBCT.GetString("FISconf"));
    this.jSetFISoptionsButton.setText(LocaleKBCT.GetString("SetFISoptions"));
    this.jSetFISoptionsButton.setToolTipText(LocaleKBCT.GetString("SetFISoptionsText"));
    this.jGenerateFISButton.setText(LocaleKBCT.GetString("GenerateFIS"));
    this.jGenerateFISButton.setToolTipText(LocaleKBCT.GetString("GenerateFIStext"));
    this.titledBorderContext.setTitle(LocaleKBCT.GetString("Context"));
    this.titledBorderContextName.setTitle(LocaleKBCT.GetString("Name"));
    this.titledBorderContextNumberInputs.setTitle(LocaleKBCT.GetString("NumberOfInputs"));
    this.titledBorderContextNumberClasses.setTitle(LocaleKBCT.GetString("NumberOfClasses"));
    this.titledBorderKBmodeling.setTitle(LocaleKBCT.GetString("KB_modeling"));
    this.titledBorderExpertName.setTitle(LocaleKBCT.GetString("Expert_Knowledge_Base"));
    this.titledBorderDataName.setTitle(LocaleKBCT.GetString("Data_Knowledge_Base"));
    this.jExpertButton.setText(LocaleKBCT.GetString("Expert"));
    this.jDataButton.setText(LocaleKBCT.GetString("Data"));
    this.repaint();
  }
//------------------------------------------------------------------------------
  private void TranslateTitle() {
    this.setTitle(LocaleKBCT.GetString("GUAJE"));
    if( this.KB )
      if( !this.IKBFile.equals("") )
        this.setTitle(LocaleKBCT.GetString("GUAJE") + ": " + this.IKBFile);
  }
//------------------------------------------------------------------------------
  private void TranslateMenu() {
    // men� KBCT
    this.jMenuKBCT.setText(LocaleKBCT.GetString("KBCT"));
    this.jMenuNew.setText(LocaleKBCT.GetString("New"));
    this.jMenuNew.setToolTipText(LocaleKBCT.GetString("NewKB"));
    this.jMenuOpen.setText(LocaleKBCT.GetString("Open"));
    this.jMenuOpen.setToolTipText(LocaleKBCT.GetString("OpenKB"));
    this.jMenuClose.setText(LocaleKBCT.GetString("Close"));
    this.jMenuClose.setToolTipText(LocaleKBCT.GetString("CloseKB"));
    this.jMenuSave.setText(LocaleKBCT.GetString("Save"));
    this.jMenuSave.setToolTipText(LocaleKBCT.GetString("SaveKB"));
    this.jMenuSaveAs.setText(LocaleKBCT.GetString("SaveAs"));
    this.jMenuSaveAs.setToolTipText(LocaleKBCT.GetString("SaveAsKB"));
    this.jMenuSetAutomaticConfiguration.setText(LocaleKBCT.GetString("SetAutomaticConfiguration"));
    this.jMenuSetAutomaticConfiguration.setToolTipText(LocaleKBCT.GetString("SetAutomaticConfiguration_msg"));
    this.jMenuSynthesis.setText(LocaleKBCT.GetString("Synthesis"));
    this.jMenuSynthesis.setToolTipText(LocaleKBCT.GetString("Synthesis_msg"));
    this.jExpertButton.setToolTipText(LocaleKBCT.GetString("NewExpert"));
    this.jDataButton.setToolTipText(LocaleKBCT.GetString("NewData"));
    this.jMenuExit.setText(LocaleKBCT.GetString("Exit"));
    this.jMenuExit.setToolTipText(LocaleKBCT.GetString("ExitKB"));
    // menu expert
    this.jMenuMExpert.setText(LocaleKBCT.GetString("Expert"));
    this.jMenuExpertNew.setText(LocaleKBCT.GetString("New"));
    this.jMenuExpertNew.setToolTipText(LocaleKBCT.GetString("NewExpertKB"));
    this.jMenuExpertOpen.setText(LocaleKBCT.GetString("Open"));
    this.jMenuExpertOpen.setToolTipText(LocaleKBCT.GetString("OpenExpertKB"));
    this.jMenuExpertClose.setText(LocaleKBCT.GetString("Close"));
    this.jMenuExpertClose.setToolTipText(LocaleKBCT.GetString("CloseExpertKB"));
    this.jMenuExpertSaveAs.setText(LocaleKBCT.GetString("SaveAs"));
    this.jMenuExpertSaveAs.setToolTipText(LocaleKBCT.GetString("SaveAsExpertKB"));
    // menu data
    this.jMenuMData.setText(LocaleKBCT.GetString("Data"));
    this.jMenuDataOpen.setText(LocaleKBCT.GetString("Open"));
    this.jMenuDataOpen.setToolTipText(LocaleKBCT.GetString("OpenDataKB"));
    this.jMenuDataClose.setText(LocaleKBCT.GetString("Close"));
    this.jMenuDataClose.setToolTipText(LocaleKBCT.GetString("CloseDataKB"));
    this.jMenuDataView.setText(LocaleKBCT.GetString("View"));
    this.jMenuDataTable.setText(LocaleKBCT.GetString("Table"));
    this.jMenuDataSaveAs.setText(LocaleKBCT.GetString("SaveAs"));
    this.jMenuDataSaveAs.setToolTipText(LocaleKBCT.GetString("SaveAsDataKB"));
    this.jMenuDataSmote.setText("SMOTE");
    this.jMenuDataFeatureSelection.setText(LocaleKBCT.GetString("FeatureSelection"));
    this.jMenuDataGenerate.setText(LocaleKBCT.GetString("Generate"));
    this.jMenuDataGenerateSample.setText(LocaleKBCT.GetString("Sample"));
    this.jMenuDataGenerateCrossValidation.setText(LocaleKBCT.GetString("CrossValidation"));
    this.jMenuDataInduce.setText(LocaleKBCT.GetString("Induce"));
    this.jMenuDataInducePartitions.setText(LocaleKBCT.GetString("Partitions"));
    this.jMenuDataInduceRules.setText(LocaleKBCT.GetString("Rules"));
    this.jMenuDataBuildKB.setText(LocaleKBCT.GetString("KBCT"));
    //this.jMenuDataFingrams.setText(LocaleKBCT.GetString("Fingrams"));
  }
//------------------------------------------------------------------------------
  //private void Events() {
    public void Events() {
    // evenntos de menus
    // menu kbct
    jMenuKBCT.addMenuListener(new MenuListener() {
      public void menuSelected(MenuEvent e) { jMenuKBCT_menuSelected(); }
      public void menuDeselected(MenuEvent e) {}
      public void menuCanceled(MenuEvent e) {}
    });
    jMenuMExpert.addMenuListener(new MenuListener() {
      public void menuSelected(MenuEvent e) { jMenuMExpert_menuSelected();}
      public void menuDeselected(MenuEvent e) {}
      public void menuCanceled(MenuEvent e) {}
    });
    jMenuMData.addMenuListener(new MenuListener() {
      public void menuSelected(MenuEvent e) { jMenuMData_menuSelected(); }
      public void menuDeselected(MenuEvent e) {}
      public void menuCanceled(MenuEvent e) {}
    });
    jMenuHelp.addMenuListener(new MenuListener() {
        public void menuSelected(MenuEvent e) { jMenuHelp_menuSelected(); }
        public void menuDeselected(MenuEvent e) {}
        public void menuCanceled(MenuEvent e) {}
      });
    jMenuNew.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuNew_actionPerformed(); }} );
    jMenuOpen.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuOpen_actionPerformed(null); }} );
    jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuClose_actionPerformed(); }} );
    jMenuSave.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuSave_actionPerformed(); }} );
    jMenuSaveAs.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuSaveAs_actionPerformed(); }} );
    jMenuSetAutomaticConfiguration.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuSetAutomaticConfiguration_actionPerformed(); }} );
    jMenuSynthesis.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuSynthesis_actionPerformed(); }} );
    jMenuExit.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExit_actionPerformed(); }} );
    // menu expert
    jMenuExpertNew.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExpertNew_actionPerformed(); }} );
    jMenuExpertOpen.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExpertOpen_actionPerformed(); }} );
    jMenuExpertClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExpertClose_actionPerformed(); }} );
    jMenuExpertSaveAs.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExpertSaveAs_actionPerformed(); }} );
    // menu data
    jMenuDataOpen.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuDataOpen_actionPerformed(); }} );
    jMenuDataClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuDataClose_actionPerformed(); }} );
    jMenuDataView.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuDataView_actionPerformed(); }} );
    jMenuDataTable.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuDataTable_actionPerformed(); }} );
    jMenuDataSaveAs.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuDataSaveAs_actionPerformed(); }} );
    jMenuDataSmote.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { 
    	jMenuDataSmote_actionPerformed(false); 
        if (!MainKBCT.getConfig().GetTESTautomatic()) {
            MessageKBCT.Information(JMainFrame.this, LocaleKBCT.GetString("AutomaticSmoteEnded"));
        }
    }} );
    jMenuDataFeatureSelection.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuDataFeatureSelection_actionPerformed(false); }} );
    jMenuDataGenerateSample.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuDataGenerateSample_actionPerformed(false); }} );
    jMenuDataGenerateCrossValidation.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuDataGenerateCrossValidation_actionPerformed(false); }} );
    jMenuDataInducePartitions.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuDataInducePartitions_actionPerformed(false, false, true); }} );
    jMenuDataInduceRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuDataInduceRules_actionPerformed(false, false); }} );
    jMenuDataBuildKB.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuDataBuildKB_actionPerformed(null); }} );
    
    this.OptionsEvents();

    // buttons
    jSetFISoptionsButton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jSetFISoptionsButton_actionPerformed(); }} );
    jGenerateFISButton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jGenerateFISButton_actionPerformed(); }} );
    jExpertButton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jExpertButton_actionPerformed(); }} );
    jDataButton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jDataButton_actionPerformed(); }} );
  }
//------------------------------------------------------------------------------
  private void InitJKBCTFrame() { this.TranslateTitle(); }
//------------------------------------------------------------------------------
  public void InitJKBCTFrameWithKBCT() throws Throwable {
  //private void InitJKBCTFrameWithKBCT() throws Throwable {
    this.InitJKBCTFrame();
    jTFcontextName.setEnabled(false);
    jTFcontextNumberInputs.setEnabled(false);
    jTFcontextNumberClasses.setEnabled(false);
    jTFExpertName.setBackground(Color.WHITE);
    jTFExpertName.setForeground(Color.BLACK);
    jTFExpertName.setEditable(false);
    jTFDataName.setBackground(Color.WHITE);
    jTFDataName.setForeground(Color.BLACK);
    jTFDataName.setEditable(false);
    if (!this.KB) {
      this.jSetFISoptionsButton.setEnabled(false);
      this.jGenerateFISButton.setEnabled(false);
      this.jExpertButton.setEnabled(false);
      this.jDataButton.setEnabled(false);
      jTFcontextName.setText("");
      jTFcontextName.setEnabled(false);
      jTFcontextNumberInputs.setText("");
      jTFcontextNumberInputs.setEnabled(false);
      jTFcontextNumberClasses.setText("");
      jTFcontextNumberClasses.setEnabled(false);
      jTFExpertName.setText("");
      jTFDataName.setText("");
    } else {
      jTFcontextName.setText(MainKBCT.getConfig().GetProblem());
      jTFcontextName.setEnabled(true);
      jTFcontextNumberInputs.setText(""+MainKBCT.getConfig().GetNumberOfInputs());
      jTFcontextNumberInputs.setEnabled(true);
      jTFcontextNumberClasses.setText(""+MainKBCT.getConfig().GetNumberOfOutputLabels());
      jTFcontextNumberClasses.setEnabled(true);
      if (JKBCTFrame.KBExpertFile.equals("")) {
         this.jMenuExpertNew.setEnabled(true);
         this.jMenuExpertOpen.setEnabled(true);
         this.jMenuExpertClose.setEnabled(false);
         this.jExpertButton.setEnabled(false);
         this.jSetFISoptionsButton.setEnabled(false);
         this.jGenerateFISButton.setEnabled(false);
      } else {
         this.jSetFISoptionsButton.setEnabled(true);
         this.jGenerateFISButton.setEnabled(true);
         this.jExpertButton.setEnabled(true);
         this.jMenuExpertClose.setEnabled(true);
      }
      if (this.KBDataFile.equals("")){
         this.jMenuDataOpen.setEnabled(true);
         this.jDataButton.setEnabled(false);
         this.jMenuDataClose.setEnabled(false);
         this.jMenuDataView.setEnabled(false);
         this.jMenuDataTable.setEnabled(false);
      } else {
         if (!JKBCTFrame.KBExpertFile.equals(""))
         this.jDataButton.setEnabled(true);
         this.jMenuDataClose.setEnabled(true);
         this.jMenuDataView.setEnabled(true);
         this.jMenuDataTable.setEnabled(true);
      }
      jTFExpertName.setText(JKBCTFrame.KBExpertFile);
      jTFDataName.setText(this.KBDataFile);
    }
  }
//------------------------------------------------------------------------------
  public void jMenuNew_actionPerformed() {
    if( this.Close() == false )
      return;
    try {
     this.KB= true;
     this.IKBFile="";
     JKBCTFrame.KBExpertFile="";
     this.KBDataFile="";
     this.oldIKBFile="";
     this.oldKBExpertFile="";
     this.oldKBDataFile="";
     this.oldConjunction=null;
     this.oldDisjunction=null;
     this.oldDefuzzification=null;
     this.InitJKBCTFrameWithKBCT();
    } catch( Throwable t ) {
       MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JMainFrame: jMenuNew_actionPerformed: "+t);
       this.KB=false;
       this.InitJKBCTFrame();
    }
  }
//------------------------------------------------------------------------------
  void jMenuExpertNew_actionPerformed() {
    try {
      if (this.kbct_Data!=null) {
        this.kbct = new JKBCT(this.kbct_Data);
        int l= this.KBDataFile.lastIndexOf('.');
        if( l == -1 )
            JKBCTFrame.KBExpertFile = this.KBDataFile + "."+("KB.XML").toLowerCase();
        else
        	JKBCTFrame.KBExpertFile = this.KBDataFile.substring(0,l) + "."+("KB.XML").toLowerCase();

        if (!JKBCTFrame.KBExpertFile.equals("") && (new File(JKBCTFrame.KBExpertFile)).exists())
            this.jef= new JExpertFrame(JKBCTFrame.KBExpertFile, this, null);
        else
          this.jef = new JExpertFrame(this);

        this.jef.Temp_kbct= new JKBCT(this.kbct);
        this.jef.kbct= new JKBCT(this.kbct);
        File f = new File(JKBCTFrame.KBExpertFile);
        if (f.exists() && !(MainKBCT.getConfig().GetTESTautomatic())) {
          String message = LocaleKBCT.GetString("TheKBCTFile") + " : " + JKBCTFrame.KBExpertFile + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");
          if (MessageKBCT.Confirm(this, message, 1, false, false, false) == JOptionPane.NO_OPTION)
            this.jef.jMenuSaveAs_actionPerformed();
          else {
            this.jef.Temp_kbct.Save(JKBCTFrame.KBExpertFile, false);
            this.jef.kbct.Save(JKBCTFrame.KBExpertFile, false);
          }
        } else {
          this.jef.Temp_kbct.Save(JKBCTFrame.KBExpertFile, false);
          this.jef.kbct.Save(JKBCTFrame.KBExpertFile, false);
        }
        this.jMenuExpertNew.setEnabled(false);
        this.jMenuExpertOpen.setEnabled(false);
        this.jMenuExpertClose.setEnabled(true);
        this.jExpertButton.setEnabled(true);
      } else {
    	JKBCTFrame.KBExpertFile = MainKBCT.getConfig().GetKBCTFilePath()+System.getProperty("file.separator")+LocaleKBCT.GetString("NewKBExpertFile").toLowerCase() + "."+("KB.XML").toLowerCase();
        this.jMenuExpertNew.setEnabled(false);
        this.jMenuExpertOpen.setEnabled(false);
        this.jef = new JExpertFrame(this);
        this.jef.jMenuNew_actionPerformed();
        File f = new File(JKBCTFrame.KBExpertFile);
        if (f.exists() && !(MainKBCT.getConfig().GetTESTautomatic()) && !(MainKBCT.getConfig().GetTutorialFlag())) {
          String message = LocaleKBCT.GetString("TheKBCTFile") + " : "+JKBCTFrame.KBExpertFile + " " + LocaleKBCT.GetString("AlreadyExist")+"\n"+LocaleKBCT.GetString("DoYouWantToReplaceIt");
          if (MessageKBCT.Confirm(this, message, 1, false, false, false) == JOptionPane.NO_OPTION)
            this.jef.jMenuSaveAs_actionPerformed();
          else {
            this.jef.Temp_kbct.Save(JKBCTFrame.KBExpertFile, false);
            this.jef.kbct.Save(JKBCTFrame.KBExpertFile, false);
          }
        } else {
          this.jef.Temp_kbct.Save(JKBCTFrame.KBExpertFile, false);
          this.jef.kbct.Save(JKBCTFrame.KBExpertFile, false);
        }
        this.kbct= this.jef.Temp_kbct;
        // Save default ikb.xml
        if ( (this.IKBFile==null) || (this.IKBFile.equals("")) ) {
            this.IKBFile= JKBCTFrame.KBExpertFile.replace(".kb.xml",".ikb.xml");
            this.jMenuSave_actionPerformed();
        }
        this.InitJKBCTFrameWithKBCT();
      }
    } catch( Throwable t ) {
       MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JMainFrame: jMenuExpertNew_actionPerformed: "+t);
       this.ResetKBExpertFile();
       this.InitJKBCTFrame();
    }
  }
//------------------------------------------------------------------------------
  void jMenuDataView_actionPerformed() {
    if (this.DataFileNoSaved != null)
        new DataChooserFrame(this, this.kbct_Data, this.kbct, this.DataFileNoSaved);
    else
        new DataChooserFrame(this, this.kbct_Data, this.kbct, this.DataFile);
  }
//------------------------------------------------------------------------------
  void jMenuDataTable_actionPerformed() {
    JDataTableFrame dtf = (JDataTableFrame)JExtendedDataFile.jhtTableFrames.get(this.DataFile.FileName()); //the jhtTableFrames hash table is used to check if this data table frame is already opened
    if( dtf == null ) {
      //if not, it is created
      dtf = new JDataTableFrame( this, this.kbct, this.DataFile);
      JExtendedDataFile.jhtTableFrames.put(this.DataFile.FileName(), dtf);
    }
    dtf.setVisible(true);   //in both cases, the data table frame is shown
  }
//------------------------------------------------------------------------------
  void jMenuDataGenerateSample_actionPerformed(boolean automatic) {
      JPanelGenerateSample panelGS = new JPanelGenerateSample(this.DataFile, this.kbct);
      if( automatic || JOptionPane.showOptionDialog(this, panelGS, LocaleKBCT.GetString("Generate")+" "+LocaleKBCT.GetString("Sample"), JOptionPane.YES_OPTION | JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null) == JOptionPane.YES_OPTION ) {
          try {
        	if (automatic) {
                boolean classif = false;
                if (this.kbct!=null) {
                	if (this.kbct.GetOutput(1).GetClassif().equals("yes"))
                		classif=true;
                }
                 jnifis.GenerateSample(this.DataFile.FileName(), 1, this.ValidationDataRatio, this.Seed, classif, this.DataFile.VariableCount()-1, 0.01, true, false);
        	} else {
        	     jnifis.GenerateSample(this.DataFile.FileName(), panelGS.getSampleNumber(), panelGS.getValidationDataRatio(), panelGS.getSeed(), panelGS.getClassif(), panelGS.getColumnNumber(), panelGS.getPrecision(), panelGS.getReplace(), panelGS.getTruncate());
        	}
        	for (int n=0; n<panelGS.getSampleNumber(); n++) {
                 File f= new File(this.DataFile.FileName());
                 File fPar= new File(f.getParent());
                 String[] files= fPar.list();
                 String DataName=this.DataFile.FileName();
            	 //System.out.println("DataName -> "+DataName);
                 for (int k=0; k<files.length; k++) {
                	 if (files[k].contains("sample."+n)) {
                         File faux= new File(files[k]);
                         DataName= faux.getName();
                		 break;
                	 }
                 }
                 String ParName= fPar.getName();
                 String nameMainFold= f.getParent()+System.getProperty("file.separator")+DataName;
                 File fFold1= new File(nameMainFold);
                 String nameNEW= f.getParent()+System.getProperty("file.separator")+ParName+n+System.getProperty("file.separator");
                 File faux=new File(nameNEW);
                 if (faux.exists()) {
                	 if (faux.isDirectory()) {
                		 File[] ff= faux.listFiles();
                		 for (int k=0; k<ff.length; k++) {
                			  ff[k].delete();
                		 }
                	 }
                 }
                 faux.mkdirs();
                 nameNEW= nameNEW+DataName;
                 fFold1.renameTo(new File(nameNEW));
                 faux= new File(nameMainFold);
                 faux.delete();
                 String trainFileName="";
                 if (nameNEW.contains("lrn.sample")) {
                     nameMainFold= nameMainFold.replace("lrn","tst");
                     trainFileName= nameNEW;
                     nameNEW= nameNEW.replace("lrn","tst");
                 } else if (nameNEW.contains("tst.sample")) {
                     nameMainFold= nameMainFold.replace("tst","lrn");
                     nameNEW= nameNEW.replace("tst","lrn");
                     trainFileName= nameNEW;
                 } else {
                	 System.out.println("----------> WARNING <----------");
                 }
                 File fFold2= new File(nameMainFold);
                 fFold2.renameTo(new File(nameNEW));
                 faux= new File(nameMainFold);
                 faux.delete();
	    	     String ExpertFN="";
                 if (!JKBCTFrame.KBExpertFile.equals("")) {
	    	    	  File fkb= new File(JKBCTFrame.KBExpertFile);
	    	    	  ExpertFN=fkb.getName();
	    	    	  ExpertFN=fkb.getParent()+System.getProperty("file.separator")+ParName+n+System.getProperty("file.separator")+ExpertFN;
                      JKBCT kbctNew= new JKBCT(JMainFrame.this.jef.Temp_kbct);
                      kbctNew.SetKBCTFile(ExpertFN);
                      kbctNew.Save();
                      kbctNew.Close();
                 }
    	    	 File fikb= new File(JMainFrame.this.IKBFile);
    	    	 String name=fikb.getName().replace(".ikb.xml","."+n+".ikb.xml");
   	    	     name=fikb.getParent()+System.getProperty("file.separator")+ParName+n+System.getProperty("file.separator")+name;
     	    	 JMainFrame.this.buildIKBxml(name, trainFileName, ExpertFN);
            }
        } catch (Throwable e1) {
            MessageKBCT.Error("", e1);
        }
      }
  }
//------------------------------------------------------------------------------
  void jMenuDataGenerateCrossValidation_actionPerformed(boolean automatic) {
      final JDialog jd = new JDialog(this);
      jd.setTitle(LocaleKBCT.GetString("CrossValidation"));
      jd.getContentPane().setLayout(new GridBagLayout());
      JPanel jPanelSaisie = new JPanel(new GridBagLayout());
      JPanel jPanelValidation = new JPanel(new GridBagLayout());
      JButton jButtonApply = new JButton(LocaleKBCT.GetString("Apply"));
      JButton jButtonCancel = new JButton(LocaleKBCT.GetString("Cancel"));
      JButton jButtonDefault = new JButton(LocaleKBCT.GetString("DefaultValues"));
      JLabel jLabelFoldsNumber = new JLabel(LocaleKBCT.GetString("FoldsNumber") + " :");
      this.jFoldsNumber.setValue(10);
      jd.getContentPane().add(jPanelSaisie, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      jPanelSaisie.add(jLabelFoldsNumber, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      jPanelSaisie.add(this.jFoldsNumber, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
        ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
      jd.getContentPane().add(jPanelValidation, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
      jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      jPanelValidation.add(jButtonCancel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      jPanelValidation.add(jButtonDefault, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      jButtonApply.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
        int numFolds= JMainFrame.this.jFoldsNumber.getValue();
        if (numFolds < 1) {
            MessageKBCT.Information(JMainFrame.this, LocaleKBCT.GetString("FoldsNumberShouldBe"));
        } else {
      	    JExtendedDataFile jedfAux=JMainFrame.this.jef.getJExtDataFile();
    	    ParserWekaFiles pwf= new ParserWekaFiles(jedfAux, JMainFrame.this.jef.Temp_kbct);
    	    pwf.buildWekaARFFfile(jedfAux.FileName()+".arff");
            try {
    	      FileReader reader = new FileReader(jedfAux.FileName()+".arff");
    	      Instances instances = new Instances(reader);
    	      int rnd = 1;
    	      Random random = new Random(rnd);
    	      instances.randomize(random);
    	      int classIndex=jedfAux.VariableCount()-1;
    		  instances.setClassIndex(classIndex);
    	      instances.stratify(numFolds);
    	      for (int fold = 0; fold < numFolds; fold++) {
    	  		Instances train = instances.trainCV(numFolds, fold, random);
                File ft= new File(jedfAux.FileName());
                File dirCV= new File(ft.getParentFile().getAbsolutePath()+System.getProperty("file.separator")+ "CV"+fold);
                dirCV.mkdir();
                String trainFileName= dirCV.getAbsolutePath()+System.getProperty("file.separator")+ft.getName()+".train."+fold;
                pwf.buildKBCTDataFile(trainFileName, train);
    	  		Instances test = instances.testCV(numFolds, fold);
                String testFileName= dirCV.getAbsolutePath()+System.getProperty("file.separator")+ft.getName()+".test."+fold;
  	    	    pwf.buildKBCTDataFile(testFileName, test);
  	    	    if (JMainFrame.this.IKBFile != null) {
  	    	    	File fikb= new File(JMainFrame.this.IKBFile);
  	    	    	String name=fikb.getName().replace(".ikb.xml","_CV"+fold+".ikb.xml");
  	    	    	name=fikb.getParent()+System.getProperty("file.separator")+"CV"+fold+System.getProperty("file.separator")+name;
  	    	    	String ExpertFN="";
                    if (!JKBCTFrame.KBExpertFile.equals("")) {
  	    	    	   File fkb= new File(JKBCTFrame.KBExpertFile);
  	    	    	   ExpertFN=fkb.getName();
  	    	    	   ExpertFN=fkb.getParent()+System.getProperty("file.separator")+"CV"+fold+System.getProperty("file.separator")+ExpertFN;
                       JKBCT kbctNew= new JKBCT(JMainFrame.this.jef.Temp_kbct);
                       kbctNew.SetKBCTFile(ExpertFN);
                       kbctNew.Save();
                       kbctNew.Close();
                    }
 	    	    	JMainFrame.this.buildIKBxml(name, trainFileName, ExpertFN);
  	    	    }
    	      }
          } catch (Throwable ex) {
        	  ex.printStackTrace();
        	  MessageKBCT.Error(null, ex);
          }
          jd.dispose();
        }
      } } );
      jButtonCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
        jd.dispose();
      } } );
      jButtonDefault.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
      	JMainFrame.this.jFoldsNumber.setValue(10);
      } } );
      jd.setModal(true);
      jd.pack();
      jd.setLocation(JChildFrame.ChildPosition(this, jd.getSize()));
      jd.setVisible(true);
  }
//------------------------------------------------------------------------------
  void buildIKBxml(String IKB, String Data, String Expert) {
      try {
    	  XMLWriter.createIKBFile(IKB);
          if (this.kbct!=null) {
   	          int nout=this.kbct.GetNbOutputs();
              // System.out.println("p5= "+nout);
              if (nout > 0)
                  XMLWriter.writeIKBFileExpert(Expert, JConvert.conjunction, true);
              else 
                  XMLWriter.writeIKBFileExpert(Expert, JConvert.conjunction, false);
        	
          } else {
              XMLWriter.writeIKBFileExpert(Expert, JConvert.conjunction, false);
          }
          String VariableNames="";
          if (this.DataFileLabels) {
              VariableNames="yes";
          } else {
              VariableNames="no";
          }
          String SelectedVariables="";
          if (this.variables==null) {
              SelectedVariables="0";
              for (int i=1; i<this.DataNbVariables; i++) {
                   SelectedVariables=SelectedVariables+", "+i;
              }
          } else {
              Enumeration en= this.variables.elements();
              int value= ((Integer)en.nextElement()).intValue();
              SelectedVariables=SelectedVariables+value;
              while (en.hasMoreElements()) {
                     value= ((Integer)en.nextElement()).intValue();
                     SelectedVariables=SelectedVariables+", "+value;
              }
          }
          XMLWriter.writeIKBFileData(Data, VariableNames, SelectedVariables, Data, this.DataNbVariables);
          DecimalFormat df= new DecimalFormat();
          df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
          df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
          DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
          dfs.setDecimalSeparator((new String(".").charAt(0)));
          df.setDecimalFormatSymbols(dfs);
          df.setGroupingSize(20);
          if (this.kbct_Data!=null) {
              int ins= this.kbct_Data.GetNbInputs();
              int outs= this.kbct_Data.GetNbOutputs();
              int lim= ins+outs;
              for (int n = 0; n < lim; n++) {
                   int N_variable= n+1;
                   variable v;
                   boolean output= false;
                   if (n<ins)
                       v= jnikbct.GetInput(this.kbct_Data.GetPtr(), N_variable);
                   else {
                       v= jnikbct.GetOutput(this.kbct_Data.GetPtr(), N_variable-ins);
                       output=true;
                   }
                   String variable="Variable"+N_variable;
                   String variableName=v.GetName();
                   String variableType=v.GetType();
                   String varLowerRange=df.format(v.GetLowerInterestRange());
                   String varUpperRange=df.format(v.GetUpperInterestRange());
                   int numberOfLabels= v.GetLabelsNumber();
                   XMLWriter.writeIKBFileDataVariable(variable, variableName, variableType, varLowerRange, varUpperRange, numberOfLabels, output);
              }
          }
          XMLWriter.closeIKBFile(false);
          String problem= this.jTFcontextName.getText();
          MainKBCT.getConfig().SetProblem(problem);
          int numberOfInputs= this.jTFcontextNumberInputs.getValue(); 
       	  MainKBCT.getConfig().SetNumberOfInputs(numberOfInputs);
          boolean classFlag= MainKBCT.getConfig().GetClassificationFlag();
          int numberOfOutputLabels= this.jTFcontextNumberClasses.getValue();
          MainKBCT.getConfig().SetNumberOfOutputLabels(numberOfOutputLabels);
          if ( (this.KBintPath==null) || (MainKBCT.getConfig().GetTESTautomatic()) ) {
          	  this.KBintPath= new String[10];
          	  boolean saveDefault= false;
              for (int n=0; n<this.KBintPath.length; n++) {
              	File aux= new File(this.IKBFile);
                  switch (n) {
                      case 0: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint1.kb.xml";
              	              break;
                      case 1: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint211.kb.xml";
      	                      break;
                      case 2: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint212.kb.xml";
                              break;
                      case 3: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint213.kb.xml";
                              break;
                      case 4: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint221.kb.xml";
                              break;
                      case 5: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint222.kb.xml";
                              break;
                      case 6: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint223.kb.xml";
                              break;
                      case 7: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint2.kb.xml";
                              break;
                      case 8: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint3.kb.xml";
                              break;
                      case 9: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint4.kb.xml";
                              break;
                  }
              	  File f = new File(this.KBintPath[n]);
                  boolean warning= f.exists();
                  if (!warning) {
                  	f.createNewFile();
                  	saveDefault=true;
                  }
             
              }
              MainKBCT.getConfig().SetKBCTintFilePath(this.KBintPath);
              if (saveDefault) {
                  this.saveInterpDefaultKBs();
              }
          } else {
          	this.KBintPath= MainKBCT.getConfig().GetKBCTintFilePath();
          }
          String[] conjs=MainKBCT.getConfig().GetKBCTintConjunction();
          String[] disjs=MainKBCT.getConfig().GetKBCTintDisjunction();
          String[] defuzz=MainKBCT.getConfig().GetKBCTintDefuzzification();
          this.oldKBintPath= this.KBintPath;
          this.oldOntologyFile= JKBCTFrame.OntologyFile;
          XMLWriter.writeIKBFileContext(problem, numberOfInputs, classFlag, numberOfOutputLabels);
          XMLWriter.writeIKBFileInterpretability(this.KBintPath,conjs,disjs,defuzz);
          XMLWriter.writeIKBFileOntology(JKBCTFrame.OntologyFile);
          XMLWriter.closeIKBFile(true);
      } catch (Throwable t) {
    	  t.printStackTrace();
    	  MessageKBCT.Error(null, t);
      }
  }

//------------------------------------------------------------------------------
  void jMenuDataInducePartitions_actionPerformed(boolean automatic, boolean PartitionInduction, boolean PartitionSelection) {
    String SelectedVariable;
    if (automatic) {
    	SelectedVariable= LocaleKBCT.GetString("AllInputs");
        if (PartitionSelection) {
    	  String SelectPartitionsLogFile= (JKBCTFrame.BuildFile("LogSelectPartitions")).getAbsolutePath();
 	      MessageKBCT.BuildLogFile(SelectPartitionsLogFile+".txt", this.IKBFile, JKBCTFrame.KBExpertFile, this.KBDataFile, "SelectPartitions");
          MessageKBCT.WriteLogFile((LocaleKBCT.GetString("AutomaticSelectPartitionsProcedure")).toUpperCase(), "SelectPartitions");
          MessageKBCT.WriteLogFile("----------------------------------", "SelectPartitions");
        }
    } else
        SelectedVariable= (String)MessageKBCT.InducePartitions(this, this.kbct_Data, this.jef.Temp_kbct);

    if (SelectedVariable != null) {
      if (SelectedVariable.equals(LocaleKBCT.GetString("AllInputs"))) {
          int inputsNumber= this.jef.Temp_kbct.GetNbInputs();
          JVariable[] jvv= new JVariable[inputsNumber];
          JVariable[] jvdata= new JVariable[inputsNumber];
          for (int n=0; n<inputsNumber; n++) {
        	  jvv[n]= this.jef.Temp_kbct.GetInput(n+1);
        	  jvdata[n]= this.kbct_Data.GetInput(n+1);
          }
          File[] files= new File[6];
          if (PartitionSelection) {
        	  files= this.buildInducePartitionsFiles(-1, jvv, inputsNumber, automatic, "A");
          } else {
              String indType= MainKBCT.getConfig().GetInductionType();
              //System.out.println("indType="+indType);
              if (indType.equalsIgnoreCase("regular")) {
            	  files= this.buildInducePartitionsFiles(-1, jvv, inputsNumber, automatic, "R");
              } else if (indType.equalsIgnoreCase("hfp")) {
            	  files= this.buildInducePartitionsFiles(-1, jvv, inputsNumber, automatic, "H");
              } else if (indType.equalsIgnoreCase("kmeans")) {
            	  files= this.buildInducePartitionsFiles(-1, jvv, inputsNumber, automatic, "K");
              }
          }
          File f_hfp= files[0];
          File f_hfp_vertex= files[1];
          File f_regular= files[2];
          File f_regular_vertex= files[3];
          File f_kmeans= files[4];
          File f_kmeans_vertex= files[5];
          String hpath=null, hvpath=null, rpath=null, rvpath=null, kpath=null, kvpath=null;
          if (f_hfp!=null)
              hpath= f_hfp.getAbsolutePath();
          if (f_hfp_vertex!=null)
              hvpath= f_hfp_vertex.getAbsolutePath();
          if (f_regular!=null)
              rpath= f_regular.getAbsolutePath();
          if (f_regular_vertex!=null)
              rvpath= f_regular_vertex.getAbsolutePath();
          if (f_kmeans!=null)
              kpath= f_kmeans.getAbsolutePath();
          if (f_kmeans_vertex!=null)
              kvpath= f_kmeans_vertex.getAbsolutePath();
          
          for (int n=0; n<inputsNumber; n++) {
              //System.out.println("n="+n);
        	  if (!jvv[n].GetTrust().equals("hhigh")) {
                  int NbMF= jvv[n].GetLabelsNumber()+2;
                  SelectedVariable= jvv[n].GetName();
                  Vector<String> MPV= new Vector<String>();
                  for (int m=0; m<NbMF-2; m++) {
                       String MP= jvv[n].GetMP(m+1);
                       if (!MP.equals("No MP")) {
                           //Double modal_point= new Double(MP);
                           MPV.add(MP);
                       }
                  }
                  if (!PartitionSelection)
                	  NbMF= 9;
                  
                  if (this.DataFileNoSaved != null) {
                      new JViewVertexFrame(this, hvpath, hpath, rvpath, rpath, kvpath, kpath,
                                           this.DataFileNoSaved, SelectedVariable, n, NbMF, MPV, this.kbct, jvv[n], jvdata[n], automatic, PartitionInduction, PartitionSelection);
                  } else {
                      new JViewVertexFrame(this, hvpath, hpath, rvpath, rpath, kvpath, kpath,
                                           this.DataFile, SelectedVariable, n, NbMF, MPV, this.kbct, jvv[n], jvdata[n], automatic, PartitionInduction, PartitionSelection);
                  }
        	  }
          }
          if (automatic && PartitionSelection) {
              MessageKBCT.WriteLogFile("----------------------------------", "SelectPartitions");
              MessageKBCT.WriteLogFile(LocaleKBCT.GetString("AutomaticSelectPartitionsEnded"), "SelectPartitions");
              Date d= new Date(System.currentTimeMillis());
              MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "SelectPartitions");
              MessageKBCT.CloseLogFile("SelectPartitions");
          }
        } else {
          String Aux= SelectedVariable;
          int index=Aux.lastIndexOf("=>");
          if (index>=0)
              Aux= Aux.substring(0,index-1);

          int VariableNumber=0;
          int NbDataInputs= this.kbct_Data.GetNbInputs();
          int NbDataOutputs= this.kbct_Data.GetNbOutputs();
          for (int n=0; n<NbDataInputs+NbDataOutputs; n++)
               if ( ((n<NbDataInputs) && (this.kbct_Data.GetInput(n+1).GetName().equals(Aux))) ||
                    ((n>=NbDataInputs) && (this.kbct_Data.GetOutput(n+1-NbDataInputs).GetName().equals(Aux))) ) {
                     VariableNumber = n;
                     break;
          }
          try {
               String var_type;
               int NbInputs= this.jef.Temp_kbct.GetNbInputs();
               if (VariableNumber < NbInputs) {
                   var_type= "INPUT";
               } else {
                   var_type= "OUTPUT";
               }
               int NbMF= 5;
               Vector MPV= null;
               JVariable jv_data, jv;
               variable v_data;
               if (var_type.equals("INPUT")) {
                   jv_data= this.kbct_Data.GetInput(VariableNumber+1);
                   v_data= jv_data.GetV();
               } else {
                   jv_data= this.kbct_Data.GetOutput(VariableNumber+1-NbDataInputs);
                   v_data= jv_data.GetV();
               }
               MPV= new Vector();
               JVariable jve;
               variable v_expert;
               JKBCT kbct_exp;
               kbct_exp= new JKBCT(this.jef.Temp_kbct.GetKBCTFile());
               if (VariableNumber < NbInputs) {
                   jve= kbct_exp.GetInput(VariableNumber+1);
                   jv= this.jef.Temp_kbct.GetInput(VariableNumber+1);
               } else {
                   jve= kbct_exp.GetOutput(VariableNumber+1-NbInputs);
                   jv= this.jef.Temp_kbct.GetOutput(VariableNumber+1-NbInputs);
               }
               v_expert= jv.GetV();
               if (!v_expert.GetTrust().equals("hhigh")) {
                   NbMF= v_expert.GetLabelsNumber()+2;
                   double[] InterestDataRange= v_data.GetInputInterestRange();
                   double[] PhysicalExpertRange= v_expert.GetInputPhysicalRange();
                   double[] InterestExpertRange= v_expert.GetInputInterestRange();
                   while ( (InterestDataRange[0]<PhysicalExpertRange[0]) ||
                           (InterestDataRange[1]>PhysicalExpertRange[1]) ) {
                            int option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("RangeExpert>RangeData")+"\n"+
                                             LocaleKBCT.GetString("RangeData")+": [ "+ InterestDataRange[0] +", "+ InterestDataRange[1] +"]"+"\n"+
                                             LocaleKBCT.GetString("Expert")+" "+LocaleKBCT.GetString("InterestRange")+": [ "+InterestExpertRange[0]+", "+InterestExpertRange[1]+"]"+"\n"+
                                             LocaleKBCT.GetString("Expert")+" "+LocaleKBCT.GetString("PhysicalRange")+": [ "+v_expert.GetInputPhysicalRange()[0]+", "+v_expert.GetInputPhysicalRange()[1]+"]"+"\n"+
                                             LocaleKBCT.GetString("DoYouWantToContinue"), 0, false, false, false);

                            if (option == 0) {
                                JRangeFrame jrf= new JRangeFrame(this, jv, "InterestRange");
                                jrf.setLocation(JChildFrame.ChildPosition(this, jrf.getSize()));
                                double [] new_range = jrf.Show();
                                if( new_range != null ) {
                                    while ( !((new_range[0]>=v_expert.GetInputPhysicalRange()[0]) && (new_range[1]<=v_expert.GetInputPhysicalRange()[1])) ){
                                             MessageKBCT.Information(this, LocaleKBCT.GetString("RangeP>RangeI")+"\n"+
                                                    LocaleKBCT.GetString("InterestRange")+": [ "+new_range[0]+", "+new_range[1]+"]"+"\n"+
                                                    LocaleKBCT.GetString("PhysicalRange")+": [ "+v_expert.GetInputPhysicalRange()[0]+", "+v_expert.GetInputPhysicalRange()[1]+"]");
                                             jrf= new JRangeFrame(this, jv, "PhysicalRange");
                                             jrf.setLocation(JChildFrame.ChildPosition(this, jrf.getSize()));
                                             double[] PhysicalRange = jrf.Show();
                                             if ((PhysicalRange[0]<=v_expert.GetInputInterestRange()[0]) && (PhysicalRange[1]>=v_expert.GetInputInterestRange()[1]))
                                                 v_expert.SetInputPhysicalRange(PhysicalRange[0], PhysicalRange[1]);
                                    }
                                    v_expert.SetInputInterestRange(new_range[0], new_range[1]);
                                }
                                PhysicalExpertRange= v_expert.GetInputPhysicalRange();
                            } else
                                throw new Exception ("Exit_Induce_Partitions");
               }
               jve.SetV(v_expert);
               Vector MPE= new Vector();
               String MPA[]= new String[NbMF-2];
               for (int n=0; n<NbMF-2; n++) {
                    String MP= v_expert.GetMP(n+1);
                    MPA[n]=MP;
                    if (!MP.equals("No MP")) {
                        Double modal_point= new Double(MP);
                        double[] InterestRange= v_expert.GetInputInterestRange();
                        if ( (modal_point.doubleValue()<InterestRange[0]) ||
                             (modal_point.doubleValue()>InterestRange[1]) ) {
                              v_expert.SetMP(n+1, "No MP", false);
                              MPA[n]="No MP";
                              MPE.add(MP);
                        } else
                              MPV.add(MP);
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

               jv.SetV(v_expert);
               for (int n=0; n<NbMF-2; n++)
                    jv.SetMP(n+1, MPA[n], false);

               JVariable[] jvv= new JVariable[1];
               jvv[0]= jve;
               File[] files= new File[6];
               if (PartitionSelection) {
             	  files= this.buildInducePartitionsFiles(VariableNumber, jvv, this.jef.Temp_kbct.GetNbInputs(), automatic, "A");
               } else {
                   String indType= MainKBCT.getConfig().GetInductionType();
                   if (indType.equalsIgnoreCase("regular")) {
                 	  files= this.buildInducePartitionsFiles(VariableNumber, jvv, this.jef.Temp_kbct.GetNbInputs(), automatic, "R");
                   } else if (indType.equalsIgnoreCase("hfp")) {
                 	  files= this.buildInducePartitionsFiles(VariableNumber, jvv, this.jef.Temp_kbct.GetNbInputs(), automatic, "H");
                   } else if (indType.equalsIgnoreCase("kmeans")) {
                 	  files= this.buildInducePartitionsFiles(VariableNumber, jvv, this.jef.Temp_kbct.GetNbInputs(), automatic, "K");
                   }
               }
               File f_hfp= files[0];
               File f_hfp_vertex= files[1];
               File f_regular= files[2];
               File f_regular_vertex= files[3];
               File f_kmeans= files[4];
               File f_kmeans_vertex= files[5];
               if (this.DataFileNoSaved != null)
                   new JViewVertexFrame(this, f_hfp_vertex.getAbsolutePath(), f_hfp.getAbsolutePath(),
                                          f_regular_vertex.getAbsolutePath(), f_regular.getAbsolutePath(),
                                          f_kmeans_vertex.getAbsolutePath(), f_kmeans.getAbsolutePath(),
                                          this.DataFileNoSaved, SelectedVariable, VariableNumber, NbMF, MPV, this.kbct, jve, jv_data, automatic, PartitionInduction, PartitionSelection);
               else
                   new JViewVertexFrame(this, f_hfp_vertex.getAbsolutePath(), f_hfp.getAbsolutePath(),
                                          f_regular_vertex.getAbsolutePath(), f_regular.getAbsolutePath(),
                                          f_kmeans_vertex.getAbsolutePath(), f_kmeans.getAbsolutePath(),
                                          this.DataFile, SelectedVariable, VariableNumber, NbMF, MPV, this.kbct, jve, jv_data, automatic, PartitionInduction, PartitionSelection);
          } else {
        	  if (!MainKBCT.getConfig().GetTESTautomatic()) {
        		  MessageKBCT.Information(this,LocaleKBCT.GetString("UnalterableVariableMsg"));
        	  }
          }
        } catch (Throwable t) {
            t.printStackTrace();
            if ( (t.getMessage()!=null) && (!t.getMessage().equals("Exit_Induce_Partitions")) )
              MessageKBCT.Error(null, t);
        }
      }
    }
  }
//------------------------------------------------------------------------------
  File[] buildInducePartitionsFiles(int VariableNumber, JVariable[] jve, int NbInputs, boolean automatic, String opt) {
    File[] result= new File[6];
    try {
	  File f= new File(this.DataFile.ActiveFileName());
      JFISHFP hfp= null;
      //boolean warning= false;
      double[][] OLD_range= null;
      //HFP Partition
	  if ( (opt.equals("A")) || (opt.equals("H")) ) {
          result[0]= JKBCTFrame.BuildFile("temp"+f.getName()+".hfp.hfp");
          result[1]= JKBCTFrame.BuildFile("temp"+f.getName()+".hfp.vertices");
          if (result[0].exists()) {
              OLD_range= JFISHFP.ReadHFP(result[0]);
          }
          if (this.DataFileNoSaved != null)
              hfp = JFISHFP.HFPConfig(this.DataFileNoSaved.ActiveFileName(), 0, result[0].getAbsolutePath());
          else
              hfp = JFISHFP.HFPConfig(this.DataFile.ActiveFileName(), 0, result[0].getAbsolutePath());

          hfp.SetHierarchy(0); // hfp->0
          hfp.SetToleranceThreshold(0.01);
          int dist=0;
          if (MainKBCT.getConfig().GetDistance().equals("symbolic"))
    	      dist= 1;
          else if (MainKBCT.getConfig().GetDistance().equals("symbnum"))
    	      dist= 2;
      
          hfp.SetParameters(dist);
          Object [] params = hfp.GetParameters();
          String Distance= params[0].toString();
          String message= LocaleKBCT.GetString("HFPParameters")+"\n"+"\n"
                        + LocaleKBCT.GetString("Default_values")+":"+"\n"
                        +"   "+LocaleKBCT.GetString("Distance")+"= "+LocaleKBCT.GetString(Distance)+"\n"+"\n"
                        +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
                        +LocaleKBCT.GetString("DoYouWantToMakeChanges");
         int option= 1;
         if (!automatic)
             option= MessageKBCT.Confirm(this, message, 1, false, false, false);

         if (option == 0) {
             new JHFPParametersFrame(this, hfp);
         }
         int HFPinputs= hfp.GetNbInput();
         boolean old_r= false;
         if (OLD_range==null) {
             OLD_range= new double[HFPinputs][2];
             old_r= true;
         }
         int lim= Math.min(HFPinputs, NbInputs);
         for (int n= 0; n < lim; n++) {
            JInputHFP jin= hfp.GetInput(n);
            if ( (VariableNumber==-1) && (jve[n].GetLabelsNumber() > 9) )
                jin.SetNumberOfMF(jve[n].GetLabelsNumber());
            else if ( (n==VariableNumber) && (jve[0].GetLabelsNumber() > 9) )
                jin.SetNumberOfMF(jve[0].GetLabelsNumber());
            else
                jin.SetNumberOfMF(9);
            if (old_r) {
               double[] d= jin.GetInputInterestRange();
               OLD_range[n][0]= d[0];
               OLD_range[n][1]= d[1];
            }
            if ( (VariableNumber==-1) || (n==VariableNumber) ) {
                double[] NEW_range;
                if (VariableNumber==-1)
                	NEW_range= jve[n].GetInputInterestRange();
                else
                	NEW_range= jve[0].GetInputInterestRange();
                jin.SetRangeHFP(NEW_range);
                }
         }
         if (this.DataFileNoSaved != null) {
             hfp.Save(this.DataFileNoSaved.ActiveFileName(), result[0].getAbsolutePath());
             JFISHFP.Vertex(this.DataFileNoSaved.ActiveFileName(), result[0].getAbsolutePath(), result[1].getAbsolutePath());
         } else {
             hfp.Save(this.DataFile.ActiveFileName(), result[0].getAbsolutePath());
             JFISHFP.Vertex(this.DataFile.ActiveFileName(), result[0].getAbsolutePath(), result[1].getAbsolutePath());
         }
	  }
      //Regular partition
	  if ( (opt.equals("A")) || (opt.equals("R")) ) {
		  result[2]= JKBCTFrame.BuildFile("temp"+f.getName()+".regular.hfp");
		  result[3]= JKBCTFrame.BuildFile("temp"+f.getName()+".regular.vertices");
          if ( (!opt.equals("A")) && (result[2].exists()) ) {
              OLD_range= JFISHFP.ReadHFP(result[2]);
          }
          if (hfp==null) {
              if (this.DataFileNoSaved != null)
                  hfp = JFISHFP.HFPConfig(this.DataFileNoSaved.ActiveFileName(), 0, result[2].getAbsolutePath());
              else
                  hfp = JFISHFP.HFPConfig(this.DataFile.ActiveFileName(), 0, result[2].getAbsolutePath());
          }
		  hfp.SetHierarchy(2); // regular->2
		  if (!opt.equals("A")) {
	          int HFPinputs= hfp.GetNbInput();
	          boolean old_r= false;
	          if (OLD_range==null) {
	              OLD_range= new double[HFPinputs][2];
	              old_r= true;
	          }
              int lim= Math.min(HFPinputs, NbInputs);
	          for (int n= 0; n < lim; n++) {
	            JInputHFP jin= hfp.GetInput(n);
	            if (jve[n].GetLabelsNumber() > 9)
	                jin.SetNumberOfMF(jve[n].GetLabelsNumber());
	            else
	                jin.SetNumberOfMF(9);
	            if (old_r) {
	                double[] d= jin.GetInputInterestRange();
	                OLD_range[n][0]= d[0];
	                OLD_range[n][1]= d[1];
	            }
	            if ( (VariableNumber==-1) || (n==VariableNumber) ) {
	                 double[] NEW_range;
	                 if (VariableNumber==-1)
	                 	NEW_range= jve[n].GetInputInterestRange();
	                 else
	                 	NEW_range= jve[0].GetInputInterestRange();
	                 jin.SetRangeHFP(NEW_range);
	            }
              }
		  }
          if (this.DataFileNoSaved != null) {
              hfp.Save(this.DataFileNoSaved.ActiveFileName(), result[2].getAbsolutePath());
            	  JFISHFP.Vertex(this.DataFileNoSaved.ActiveFileName(), result[2].getAbsolutePath(), result[3].getAbsolutePath());
          } else {
              hfp.Save(this.DataFile.ActiveFileName(), result[2].getAbsolutePath());
            	  JFISHFP.Vertex(this.DataFile.ActiveFileName(), result[2].getAbsolutePath(), result[3].getAbsolutePath());
          }
	  }
      //Kmeans Partition
	  if ( (opt.equals("A")) || (opt.equals("K")) ) {
		  result[4]= JKBCTFrame.BuildFile("temp"+f.getName()+".kmeans.hfp");
		  result[5]= JKBCTFrame.BuildFile("temp"+f.getName()+".kmeans.vertices");
          if ( (!opt.equals("A")) && (result[4].exists()) ) {
              OLD_range= JFISHFP.ReadHFP(result[4]);
          }
          if (hfp==null) {
              if (this.DataFileNoSaved != null)
                  hfp = JFISHFP.HFPConfig(this.DataFileNoSaved.ActiveFileName(), 0, result[4].getAbsolutePath());
              else
                  hfp = JFISHFP.HFPConfig(this.DataFile.ActiveFileName(), 0, result[4].getAbsolutePath());
          }
          hfp.SetHierarchy(1); // kmeans->1
		  if (!opt.equals("A")) {
	          int HFPinputs= hfp.GetNbInput();
	          boolean old_r= false;
	          if (OLD_range==null) {
	              OLD_range= new double[HFPinputs][2];
	              old_r= true;
	          }
              int lim= Math.min(HFPinputs, NbInputs);
	          for (int n= 0; n < lim; n++) {
	            JInputHFP jin= hfp.GetInput(n);
	            if (jve[n].GetLabelsNumber() > 9)
	                jin.SetNumberOfMF(jve[n].GetLabelsNumber());
	            else
	                jin.SetNumberOfMF(9);
	            if (old_r) {
	                double[] d= jin.GetInputInterestRange();
	                OLD_range[n][0]= d[0];
	                OLD_range[n][1]= d[1];
	            }
	            if ( (VariableNumber==-1) || (n==VariableNumber) ) {
	                 double[] NEW_range;
	                 if (VariableNumber==-1)
	                 	NEW_range= jve[n].GetInputInterestRange();
	                 else
	                 	NEW_range= jve[0].GetInputInterestRange();

	                 jin.SetRangeHFP(NEW_range);
	            }
              }
		  }
          if (this.DataFileNoSaved != null) {
              hfp.Save(this.DataFileNoSaved.ActiveFileName(), result[4].getAbsolutePath());
            	  JFISHFP.Vertex(this.DataFileNoSaved.ActiveFileName(), result[4].getAbsolutePath(), result[5].getAbsolutePath());
          } else {
              hfp.Save(this.DataFile.ActiveFileName(), result[4].getAbsolutePath());
                  JFISHFP.Vertex(this.DataFile.ActiveFileName(), result[4].getAbsolutePath(), result[5].getAbsolutePath());
          }
	  }
      if (hfp!=null) {
          hfp.Delete();
      }
    } catch (Throwable t) {
        t.printStackTrace();
        MessageKBCT.Error(null, t);
    }
    return result;
  }
//------------------------------------------------------------------------------
  void InduceRulesFPA(boolean dataSelection, boolean automatic) {
    String strategy=MainKBCT.getConfig().GetStrategy();
    int strat=0;
    if (strategy.equals(LocaleKBCT.GetString("decrease")))
    	strat=1;
    	
    int cardmin= MainKBCT.getConfig().GetMinCard();
    double matchmin= MainKBCT.getConfig().GetMinDeg();
    int option=1;
    if (!automatic) {
      String message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
                     +"   "+LocaleKBCT.GetString("Strategy")+"= "+strategy+"\n"
                     +"   "+LocaleKBCT.GetString("MinCard")+"= "+cardmin+"\n"
                     +"   "+LocaleKBCT.GetString("MinDeg")+"= "+matchmin+"\n"+"\n"
                     +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
                     +LocaleKBCT.GetString("DoYouWantToChangeSelectedValues");
      option= MessageKBCT.Confirm(this, message, 1, false, false, false);
    }
    if (option==0) {
        this.SetFPAparameters(cardmin, matchmin, strat);
        int st=0;
        if (MainKBCT.getConfig().GetStrategy().equals("decrease"))
        	st=1;
        
        this.InduceRulesFPA(MainKBCT.getConfig().GetMinCard(), MainKBCT.getConfig().GetMinDeg(), st, dataSelection, false);
    } else
        this.InduceRulesFPA(cardmin, matchmin, strat, dataSelection, automatic);
  }
  //------------------------------------------------------------------------------
  void SetFPAparameters(final int cardmin, final double matchmin, final int strat) {
    final JDialog jd = new JDialog(this);
    jd.setTitle(LocaleKBCT.GetString("Induce")+LocaleKBCT.GetString("Rules"));
    jd.getContentPane().setLayout(new GridBagLayout());
    JPanel jPanelSaisie = new JPanel(new GridBagLayout());
    JPanel jPanelValidation = new JPanel(new GridBagLayout());
    JButton jButtonApply = new JButton(LocaleKBCT.GetString("Apply"));
    JButton jButtonCancel = new JButton(LocaleKBCT.GetString("Cancel"));
    JButton jButtonDefault = new JButton(LocaleKBCT.GetString("DefaultValues"));
    JLabel jLabelStrategy = new JLabel(LocaleKBCT.GetString("Strategy") + " :");
    JLabel jLabelMinCard = new JLabel(LocaleKBCT.GetString("MinCard") + " :");
    JLabel jLabelMinDeg = new JLabel(LocaleKBCT.GetString("MinDeg") + " :");
    this.jCBStrategy= new JComboBox();
    this.jCBStrategy.addItem(LocaleKBCT.GetString("min"));
    this.jCBStrategy.addItem(LocaleKBCT.GetString("decrease"));
    this.jCBStrategy.setSelectedIndex(strat);
    this.jIFMinCard.setValue(cardmin);
    this.jDFMinDeg.setValue(matchmin);
    jd.getContentPane().add(jPanelSaisie, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
          ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    jPanelSaisie.add(jLabelStrategy, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanelSaisie.add(jLabelMinCard, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanelSaisie.add(jLabelMinDeg, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanelSaisie.add(jCBStrategy,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
          ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 0, 0));
    jPanelSaisie.add(jIFMinCard, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
    jPanelSaisie.add(jDFMinDeg, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
    jd.getContentPane().add(jPanelValidation, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
          ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
    jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
          ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    jPanelValidation.add(jButtonCancel, new GridBagConstraints(15, 0, 1, 1, 1.0, 0.0
          ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    jPanelValidation.add(jButtonDefault, new GridBagConstraints(30, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    jButtonApply.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
        int strat= JMainFrame.this.jCBStrategy.getSelectedIndex();
        switch(strat) {
          case 0:MainKBCT.getConfig().SetStrategy("min");break;
          case 1:MainKBCT.getConfig().SetStrategy("decrease");break;
        }
        MainKBCT.getConfig().SetMinCard(JMainFrame.this.jIFMinCard.getValue());
        MainKBCT.getConfig().SetMinDeg(JMainFrame.this.jDFMinDeg.getValue());
    	jd.dispose();
    } } );
    jButtonCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
        jd.dispose();
    } } );
    jButtonDefault.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
        int st=0;
        if (LocaleKBCT.DefaultStrategy().equals("decrease"))
        	st=1;

        JMainFrame.this.jCBStrategy.setSelectedIndex(st);
    	JMainFrame.this.jIFMinCard.setValue(LocaleKBCT.DefaultMinCard());
    	JMainFrame.this.jDFMinDeg.setValue(LocaleKBCT.DefaultMinDeg());
    } } );
    jd.setModal(true);
    jd.pack();
    jd.setLocation(JChildFrame.ChildPosition(this, jd.getSize()));
    jd.setVisible(true);
  }  
  //------------------------------------------------------------------------------
  void InduceRulesFPA(int cardmin, double matchmin, int strategy, boolean dataSelection, boolean automatic) {
    try {
        if (automatic) {
            MessageKBCT.WriteLogFile("   "+"Fast Prototyping Algorithm".toUpperCase(), "InduceRules");
        }
      if ( (this.kbct.GetNbInputs() > this.kbct_Data.GetNbInputs()) ||
           (this.kbct.GetNbOutputs() > this.kbct_Data.GetNbOutputs()) ) {
          if (automatic) {
              MessageKBCT.WriteLogFile("   "+LocaleKBCT.GetString("WrongNumberVariablesFPA"), "InduceRules");
          } else
              MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("WrongNumberVariablesFPA"));
      } else {
        // Si hubo seleccion de datos hay que cambiar el archivo de datos que se pasa a FPA
        File f;
        if (!dataSelection) {
          if (this.DataFileNoSaved != null)
            f= new File(this.DataFileNoSaved.ActiveFileName());
          else
            f= new File(this.DataFile.ActiveFileName());
        } else {
            f= new File(this.dataSelect.ActiveFileName());
        }
        File temp1= JKBCTFrame.BuildFile("temp1fpa.kb.xml");
        File temp2= JKBCTFrame.BuildFile("temp2fpa.fis");
        File temp3= JKBCTFrame.BuildFile("temp3fpa.kb.xml");
        JKBCT kbct_fis= new JKBCT(this.kbct);
        //long ptr= kbct_fis.GetPtr(); 
        // Desactivar las reglas del archivo KB para evitar ambiguiedades en la induccion
        for (int n=0; n<kbct_fis.GetNbRules(); n++) {
          kbct_fis.GetRule(n+1).SetActive(false);
        }
        kbct_fis.Save(temp1.getPath(), false);
        kbct_fis.SaveFIS(temp2.getPath());
        JFIS jf = new JFIS(temp2.getPath(), false);
        fis.JExtendedDataFile jedf= new fis.JExtendedDataFile(f.getCanonicalPath(), true);
        //fis.jnifis.FisproPath(MainKBCT.getConfig().GetKbctPath());
        fis.jnifis.setUserHomeFisproPath(MainKBCT.getConfig().GetKbctPath());
        JFIS gen_fis = new JGENFIS(jf, jedf, true, matchmin );
        // Strategy decrease -> 1
        // Strategy min -> 0
        boolean warning= false;
        JFISFPA jffpa= null;
        try {
            jffpa= new JFISFPA(gen_fis, jedf, strategy, cardmin, matchmin );
        } catch (fis.JNIException t) {
        	//t.printStackTrace();
            MessageKBCT.Error(null, t);
            warning=true;
        }
        if (!warning) {
            int kbct_NbRules= kbct_fis.GetNbRules();
            for (int k= 0; k<kbct_NbRules; k++)
                 kbct_fis.RemoveRule(0);

            if (jffpa != null)
                this.TranslateRules_FIS2KBCT(jffpa, kbct_fis);
            
            if (JMainFrame.JRFs == null)
                JMainFrame.JRFs= new Vector();

            JRuleFrame jrf= new JRuleFrame(this, kbct_fis, new JSemaphore(), false, false, false, !automatic);
            if (automatic) {
                jrf.jMenuSave_actionPerformed(false);
                jrf.jMenuClose_actionPerformed();
                MessageKBCT.WriteLogFile("   "+LocaleKBCT.GetString("NumberOfRules")+"= "+kbct_fis.GetNbRules(), "InduceRules");
            } else {
            	JMainFrame.JRFs.add(jrf);
            }
        }
        jf.Delete();
        temp1.delete();
        temp2.delete();
        File f_rules= new File(temp3.getPath()+".rules");
        f_rules.delete();
        temp3.delete();
        kbct_fis.Close();
        kbct_fis.Delete();
        //jnikbct.DeleteKBCT(ptr+1);
      }
    } catch (Throwable t) {
    	t.printStackTrace();
        MessageKBCT.Error(null, t);
    }
  }
//------------------------------------------------------------------------------
  void InduceRulesFDT(boolean dataSelection, boolean automatic) {
    try {
  	    //System.out.println("Induce Rules: FDT");
        if (automatic) {
            MessageKBCT.WriteLogFile("   "+"Fuzzy Decision Trees".toUpperCase(), "InduceRules");
        }
      if ( (this.kbct.GetNbInputs() != this.kbct_Data.GetNbInputs()) ||
           (this.kbct.GetNbOutputs() != this.kbct_Data.GetNbOutputs()) ) {
          if (automatic) {
              MessageKBCT.WriteLogFile("   "+LocaleKBCT.GetString("WrongNumberVariablesFDT"), "InduceRules");
          } else
              MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("WrongNumberVariablesFDT"));
      } else {
        File temp1= JKBCTFrame.BuildFile("temp1fdt.kb.xml");
        File temp2= JKBCTFrame.BuildFile("temp2fdt.fis");
        JKBCT kbctaux= new JKBCT(this.kbct);
        //long ptr= kbctaux.GetPtr();
        // Desactivar las reglas del archivo KB para evitar ambiguiedades en la induccion
        for (int n=0; n<kbctaux.GetNbRules(); n++) {
          kbctaux.GetRule(n+1).SetActive(false);
        }
        kbctaux.SetKBCTFile(temp1.getAbsolutePath());
        String fis_name= temp2.getAbsolutePath();
        kbctaux.Save();
        kbctaux.SaveFIS(fis_name);
        kbctaux.Close();
        kbctaux.Delete();
        //jnikbct.DeleteKBCT(ptr+1);
        JFIS fis_file= new JFIS(fis_name);
        int NbOutputs= fis_file.NbOutputs();
        if( NbOutputs != 0 ) {
            String TreeFile= temp1.getAbsolutePath()+".tree";
            MainKBCT.getConfig().SetTreeFile(TreeFile);
            //if ( (this.jef!=null) && (this.jef.Temp_kbct!=null) )
              //MainKBCT.getConfig().SetMaxTreeDepth(this.jef.Temp_kbct.GetNbInputs());

            int option= 1;
            if (!automatic) {
                String Prune= LocaleKBCT.GetString("No");
                if (MainKBCT.getConfig().GetPrune())
              	  Prune= LocaleKBCT.GetString("Yes");
                
                String Split= LocaleKBCT.GetString("No");
                if (MainKBCT.getConfig().GetSplit())
              	  Split= LocaleKBCT.GetString("Yes");

                String Display= LocaleKBCT.GetString("No");
                if (MainKBCT.getConfig().GetDisplay())
              	  Display= LocaleKBCT.GetString("Yes");

                String message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
                   +"   "+LocaleKBCT.GetString("TreeFile")+"= "+MainKBCT.getConfig().GetTreeFile()+"\n"
                   +"   "+LocaleKBCT.GetString("MaxTreeDepth")+"= "+MainKBCT.getConfig().GetMaxTreeDepth()+"\n"
                   +"   "+LocaleKBCT.GetString("MinSignificantLevel")+"= "+MainKBCT.getConfig().GetMinSignificantLevel()+"\n"
                   +"   "+LocaleKBCT.GetString("LeafMinCard")+"= "+MainKBCT.getConfig().GetLeafMinCard()+"\n"
                   +"   "+LocaleKBCT.GetString("ToleranceThreshold")+"= "+MainKBCT.getConfig().GetToleranceThreshold()+"\n"
                   +"   "+LocaleKBCT.GetString("MinEDGain")+"= "+MainKBCT.getConfig().GetMinEDGain()+"\n"
                   +"   "+LocaleKBCT.GetString("CovThresh")+"= "+MainKBCT.getConfig().GetCovThresh()+"\n"
                   +"   "+LocaleKBCT.GetString("Relgain")+"= "+MainKBCT.getConfig().GetRelgain()+"\n"
                   +"   "+LocaleKBCT.GetString("Prune")+"= "+Prune+"\n"
                   +"   "+LocaleKBCT.GetString("Split")+"= "+Split+"\n"
                   +"   "+LocaleKBCT.GetString("Display")+"= "+Display+"\n"
                   +"   "+LocaleKBCT.GetString("RelPerfLoss")+"= "+MainKBCT.getConfig().GetPerfLoss()+"\n"+"\n"
                   +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
                   +LocaleKBCT.GetString("DoYouWantToChangeSelectedValues");
              option= MessageKBCT.Confirm(this, message, 1, false, false, false);
            } else {
                MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("TreeFile")+"= "+MainKBCT.getConfig().GetTreeFile(), "InduceRules");
                MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("MaxTreeDepth")+"= "+MainKBCT.getConfig().GetMaxTreeDepth(), "InduceRules");
                MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("MinSignificantLevel")+"= "+MainKBCT.getConfig().GetMinSignificantLevel(), "InduceRules");
                MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("LeafMinCard")+"= "+MainKBCT.getConfig().GetLeafMinCard(), "InduceRules");
                MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("ToleranceThreshold")+"= "+MainKBCT.getConfig().GetToleranceThreshold(), "InduceRules");
                MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("MinEDGain")+"= "+MainKBCT.getConfig().GetMinEDGain(), "InduceRules");
                MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("CovThresh")+"= "+MainKBCT.getConfig().GetCovThresh(), "InduceRules");
                MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("Relgain")+"= "+MainKBCT.getConfig().GetRelgain(), "InduceRules");
                MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("Prune")+"= "+MainKBCT.getConfig().GetPrune(), "InduceRules");
                MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("Split")+"= "+MainKBCT.getConfig().GetSplit(), "InduceRules");
                MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("Display")+"= "+MainKBCT.getConfig().GetDisplay(), "InduceRules");
                MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("RelPerfLoss")+"= "+MainKBCT.getConfig().GetPerfLoss(), "InduceRules");
            }
            //fis.jnifis.FisproPath(MainKBCT.getConfig().GetKbctPath());
            fis.jnifis.setUserHomeFisproPath(MainKBCT.getConfig().GetKbctPath());
            // Si hubo seleccion de datos hay que cambiar el archivo de datos que se pasa a FDT
            if (!dataSelection) {
              if (option==0) {
                  if (this.DataFileNoSaved != null)
                    new JTreeGenerateFrame(this, fis_file, this.DataFileNoSaved, this.kbct, TreeFile);
                  else
                    new JTreeGenerateFrame(this, fis_file, this.DataFile, this.kbct, TreeFile);
              } else {
                  if (this.DataFileNoSaved != null)
                    new JTreeGenerateFrame(this, fis_file, this.DataFileNoSaved, this.kbct, TreeFile, automatic);
                  else
                    new JTreeGenerateFrame(this, fis_file, this.DataFile, this.kbct, TreeFile, automatic);
              }
            } else {
                 if (option==0)
                     new JTreeGenerateFrame(this, fis_file, this.dataSelect, this.kbct, TreeFile);
                 else
                     new JTreeGenerateFrame(this, fis_file, this.dataSelect, this.kbct, TreeFile, automatic);
            }
        }
        temp1.delete();
        temp2.delete();
        File f_fis= new File(fis_name);
        f_fis.deleteOnExit();
      }
    } catch (Throwable t) {
    	t.printStackTrace();
    	MessageKBCT.Error(null, t);
    }
  }
//------------------------------------------------------------------------------
  void InduceRulesWM(boolean dataSelection, String fis_name, boolean automatic) {
    try {
        //System.out.println("WM1 -> htsize="+jnikbct.getHashtableSize());
        if (automatic) {
            MessageKBCT.WriteLogFile("   "+"Wang and Mendel".toUpperCase(), "InduceRules");
        }
      if ( (this.kbct.GetNbInputs() != this.kbct_Data.GetNbInputs()) ||
           (this.kbct.GetNbOutputs() != this.kbct_Data.GetNbOutputs()) ) {
          if (automatic) {
              MessageKBCT.WriteLogFile("   "+LocaleKBCT.GetString("WrongNumberVariablesWM"), "InduceRules");
          } else {
              /*System.out.println("error");
              System.out.println("kbct inputs: "+this.kbct.GetNbInputs());
              System.out.println("kbct outputs: "+this.kbct.GetNbOutputs());
              System.out.println("kbctData inputs: "+this.kbct_Data.GetNbInputs());
              System.out.println("kbctData outputs: "+this.kbct_Data.GetNbOutputs());*/
        	  MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("WrongNumberVariablesWM"));
          }
      } else {
          // Si hubo seleccion de datos hay que cambiar el archivo de datos que se pasa a WM
          File tempaux= JKBCTFrame.BuildFile("tempwmaux.kb.xml");
          String fis_name_aux= tempaux.getAbsolutePath();
          JKBCT kbctaux= new JKBCT(this.kbct);
          //long ptr= kbctaux.GetPtr();
      	  //System.out.println(" -> kbctaux="+ptr);

          // Desactivar las reglas del archivo KB para evitar ambiguiedades en la induccion
          for (int n=0; n<kbctaux.GetNbRules(); n++) {
               kbctaux.GetRule(n+1).SetActive(false);
          }
          kbctaux.SetKBCTFile(tempaux.getAbsolutePath());
          kbctaux.Save();
          kbctaux.SaveFIS(fis_name);
          //System.out.println("WM2 -> htsize="+jnikbct.getHashtableSize());
          //System.out.println("WM2 -> delete => "+ptr);
          kbctaux.Close();
          kbctaux.Delete();
          //jnikbct.DeleteKBCT(ptr+1);
          //System.out.println("WM3 -> htsize="+jnikbct.getHashtableSize());
          int NbOutputs= this.fis_file_InduceRules.NbOutputs();
          if( NbOutputs != 0 ) {
              JFIS fis_wm=null;
              if (!dataSelection) {
                  if (this.DataFileNoSaved != null) {
                      fis_wm= JFISHFP.WangMendel(fis_name, this.DataFileNoSaved.ActiveFileName(), fis_name_aux);
                  } else {
                      fis_wm= JFISHFP.WangMendel(fis_name, this.DataFile.ActiveFileName(), fis_name_aux);
                  }
              } else {
                  try {
            	      fis_wm= JFISHFP.WangMendel(fis_name, this.dataSelect.ActiveFileName(), fis_name_aux);
                  } catch (Exception e) {
                	  if (e.getLocalizedMessage().contains("LessThan2DistinctValuesForOutput")) {
                          if (automatic) {
                              MessageKBCT.WriteLogFile("   "+e.getLocalizedMessage(), "InduceRules");
                          } else
                              MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), e.getLocalizedMessage());

                          if (this.DataFileNoSaved != null) {
                              fis_wm= JFISHFP.WangMendel(fis_name, this.DataFileNoSaved.ActiveFileName(), fis_name_aux);
                          } else {
                              fis_wm= JFISHFP.WangMendel(fis_name, this.DataFile.ActiveFileName(), fis_name_aux);
                          }
                	  } else
                          MessageKBCT.Error(null, e);
                  }
              }
              JKBCT kbct_fis= new JKBCT(this.kbct);
              int kbct_NbRules= kbct_fis.GetNbRules();
              for (int k= 0; k<kbct_NbRules; k++)
                   kbct_fis.RemoveRule(0);

              //System.out.println("WM4 -> htsize="+jnikbct.getHashtableSize());
              this.TranslateRules_FIS2KBCT(fis_wm, kbct_fis);
              if (JMainFrame.JRFs == null)
                  JMainFrame.JRFs= new Vector();

              JRuleFrame jrf= new JRuleFrame(this, kbct_fis, new JSemaphore(), false, false, false, !automatic);
              if (automatic) {
                 jrf.jMenuSave_actionPerformed(false);
                 jrf.jMenuClose_actionPerformed();
                 MessageKBCT.WriteLogFile("   "+LocaleKBCT.GetString("NumberOfRules")+"= "+kbct_fis.GetNbRules(), "InduceRules");
              } else {
              	 JMainFrame.JRFs.add(jrf);
              }
              //ptr= kbct_fis.GetPtr();
              kbct_fis.Close();
              kbct_fis.Delete();
              //jnikbct.DeleteKBCT(ptr+1);
              //System.out.println("WM4 -> delete => "+ptr);
          }
          //kbctaux.Close();
          //kbctaux.Delete();
          //System.out.println("WM6 -> htsize="+jnikbct.getHashtableSize());
          tempaux.delete();
          File f_fis= new File(fis_name);
          f_fis.deleteOnExit();
          //jnikbct.cleanHashtable(ptr);
          //jnikbct.DeleteKBCT(ptr);
          //System.out.println("WM7 -> htsize="+jnikbct.getHashtableSize());
        }
    } catch( Throwable t ) {
      String msg= t.getMessage();
      //t.printStackTrace();
      if (msg==null)
          MessageKBCT.Error(null, t);
      else if (msg.startsWith("UnknownNature"))
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("UnknownNature") + ": " + msg.substring(14));
      else
          MessageKBCT.Error(null, t);
    }
  }
//------------------------------------------------------------------------------
  void buildPrototypeRules(boolean automatic) {
	  //System.out.println("JMF: buildPrototypeRules");
	  JExtendedDataFile jedfAux=this.jef.getJExtDataFile();
	  ParserWekaFiles pwf= new ParserWekaFiles(jedfAux, this.jef.Temp_kbct);
	  pwf.buildWekaARFFfile(jedfAux.FileName()+".arff");
      try {
	    FileReader reader = new FileReader(jedfAux.FileName()+".arff");
	    Instances instances = new Instances(reader);
	    SimpleKMeans skm= new SimpleKMeans();
	    int nc= MainKBCT.getConfig().GetClustersNumber();
        if (!automatic) {
            nc=0;
        	for (int n=0; n<this.jef.Temp_kbct.GetNbOutputs(); n++) {
    	    	nc= nc+this.jef.Temp_kbct.GetOutput(n+1).GetLabelsNumber();
    	    }
    	    nc= 5*nc;
    	    MainKBCT.getConfig().SetClustersNumber(nc);	    
	        this.setClusterersNumber();
	        skm.setNumClusters(MainKBCT.getConfig().GetClustersNumber());
	        // set induce rule algorithm
            Object SelectedAlgorithm= MessageKBCT.InduceRules(this, false);
            if (SelectedAlgorithm != null) {
            	 MainKBCT.getConfig().SetInductionRulesAlgorithm(SelectedAlgorithm.toString());
            }
        } else {
	        skm.setNumClusters(nc);
        }
	    skm.buildClusterer(instances);
	    pwf.buildKBCTDataFile(jedfAux.FileName()+".erff", skm.getClusterCentroids());
	    JExtendedDataFile OLD= this.jef.getJExtDataFile();
	    JExtendedDataFile NEW= new JExtendedDataFile(jedfAux.FileName()+".erff", true);
        if (this.DataFileNoSaved!=null) {
	        this.DataFileNoSaved= NEW;
        } else {
	        this.DataFile= NEW;
        }
  	    //System.out.println("JMF: buildPrototypeRules: auto -> "+automatic);
  	    //System.out.println("JMF: buildPrototypeRules: alg -> "+MainKBCT.getConfig().GetInductionRulesAlgorithm());
        if (!automatic) {
            this.jMenuDataInduceRules_actionPerformed(true, true);
        } else { 
            String selAlg= MainKBCT.getConfig().GetInductionRulesAlgorithm();
      	    if (selAlg.equals("Fast Prototyping Algorithm"))
                InduceRulesFPA(MainKBCT.getConfig().GetDataSelection(), automatic);
            else if (selAlg.equals("Fuzzy Decision Trees"))
            	this.InduceRulesFDT(MainKBCT.getConfig().GetDataSelection(), automatic);
            else if (selAlg.equals("Wang and Mendel")) {
                File temp= JKBCTFrame.BuildFile("tempInduceRules.fis");
                InduceRulesWM(MainKBCT.getConfig().GetDataSelection(), temp.getAbsolutePath(), automatic);
            }
        }
        if(automatic) {
           this.jef.ReduceRuleBase(1, automatic);
           MessageKBCT.WriteLogFile("   >"+LocaleKBCT.GetString("PrototypeRules").toUpperCase(), "InduceRules");
        }
        if (this.DataFileNoSaved!=null) {
	        this.DataFileNoSaved= OLD;
        } else {
	        this.DataFile= OLD;
        }
      } catch (Throwable e) {
    	  //e.printStackTrace();
    	  MessageKBCT.Error(null, e);
      }
  }
//------------------------------------------------------------------------------
  private void DataSelection(double thres, JFIS fisaux, boolean automatic) {
     // v�lido para clasificaci�n con una variable de salida
     try {
       if (this.DataFileNoSaved != null) {
           this.dataSelect= new JExtendedDataFile(this.DataFileNoSaved.ActiveFileName(), true);
       } else {
           this.dataSelect= new JExtendedDataFile(this.DataFile.ActiveFileName(), true);
       }
       int NbData= this.dataSelect.GetActiveCount();
       Vector dataToRemove= new Vector();
       int NbInputs= this.kbct.GetNbInputs();
       double[][] inputs= new double[NbInputs][NbData];
       for (int k=0; k<NbInputs; k++) {
         inputs[k]= this.dataSelect.VariableData(k);
       }
       int NbOutputs= this.kbct.GetNbOutputs();
       double[][] outputsObserved= new double[NbOutputs][NbData];
       for (int k=0; k<NbOutputs; k++) {
         outputsObserved[k]= this.dataSelect.VariableData(k+NbInputs);
       }
       int NbVar= NbInputs+NbOutputs;
       double[][] data= new double[NbData][NbVar];
       for (int i=0; i<NbData; i++)
         for (int j=0; j<NbVar; j++) {
           if (j<NbInputs)
             data[i][j]= inputs[j][i];
           else
             data[i][j]= outputsObserved[j-NbInputs][i];
         }
       for (int k=0; k<NbData; k++) {
         fisaux.Infer(data[k]);
         double[] resultsInfered= fisaux.SortiesObtenues();
         for (int n=0; n<NbOutputs; n++) {
           fis.JOutput output = fisaux.GetOutput(n);
           int Alarm= output.GetAlarm();
           // Alarm==0 -> No alarm
           if ( (Alarm==0) && (resultsInfered[n]==outputsObserved[n][k]) ) {
        	   JKBCTOutput jout= this.kbct.GetOutput(n+1);
               int NbLabOut= jout.GetLabelsNumber();
               int[] labelsUsed= new int[NbLabOut];
               for (int m=0; m<NbLabOut; m++)
             	   labelsUsed[m]=0;
               
               //jnifis.FisproPath(MainKBCT.getConfig().GetKbctPath());
               jnifis.setUserHomeFisproPath(MainKBCT.getConfig().GetKbctPath());
               int nbRules= fisaux.NbRules();
               for (int m=0; m<nbRules; m++) {
             	   JRule jr= fisaux.GetRule(m);
             	   double[] act= jr.Actions();
             	   labelsUsed[(int)act[n]-1]=1;  
               }          
               int rem[]= new int[NbLabOut];
               int cont=1;
               for (int m=0; m<NbLabOut; m++) {
             	   if (labelsUsed[m]==0)
             		   cont++;

             	   rem[m]=cont;
               }
             double[][] r= fisaux.AgregationResult(n);
             int outclass= (int)resultsInfered[n];
             if (r[1][(int)outclass-rem[(int)outclass-1]] > thres)
               dataToRemove.add(new Integer(k));
           }
         }
       }
       Object[] obj= dataToRemove.toArray();
       if (obj.length > 0) {
    	 String msg= LocaleKBCT.GetString("NextDataDeactive")+"\n"+"   ";
         int cont=0;
         int contLine=0;
         for (int n=0; n<obj.length; n++) {
           int v= ((Integer)obj[n]).intValue()+1;
           this.dataSelect.SetActive(v, false);
           if (n==obj.length-1)
             msg= msg + v + ".";
           else
             msg= msg + v + ", ";

           if (cont < 14) {
               cont++;
           } else if (n<obj.length-1) {
               msg= msg + "\n"+"   ";
               cont=0;
               contLine++;
           }
         }
         if (!automatic) {
             if (contLine > 15) {
                 final JDialog jd = new JDialog(this);
                 jd.setTitle(LocaleKBCT.GetString("Information"));
                 jd.getContentPane().setLayout(new GridBagLayout());
                 JScrollPane jPanelMessages= new JScrollPane();
                 JPanel jp= new JPanel(new GridBagLayout());
                 cont=0;
                 while (cont<contLine) {
                    int index= msg.indexOf("\n");
                    String aux=msg.substring(0,index);
                    JLabel jl= new JLabel(aux);
                    jp.add(jl, new GridBagConstraints(0, cont, 1, 1, 1.0, 1.0
                           ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
                    msg= msg.substring(index+1, msg.length()-1);
                    cont++;
                 }
                 JButton jButtonOK= new JButton(LocaleKBCT.GetString("OK"));
                 jp.add(jButtonOK, new GridBagConstraints(0, cont, 1, 1, 1.0, 1.0
                         ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
                 jPanelMessages.getViewport().add(jp);
                 jd.getContentPane().add(jPanelMessages, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
                         ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
                 jButtonOK.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
                   jd.dispose();
                   } } );
                 jd.setModal(true);
                 jd.pack();
                 jd.setLocation(JChildFrame.ChildPosition(this, jd.getSize()));
                 jd.setVisible(true);
             } else
        	      MessageKBCT.Information(this, msg);
         }
       } else {
           if (!automatic)
      	       MessageKBCT.Information(this, LocaleKBCT.GetString("NoDataRemoved"));
       }
     } catch (Throwable t) {
         //t.printStackTrace();
    	 MessageKBCT.Error(null, t);
     }
  }
//------------------------------------------------------------------------------
  void jMenuDataInduceRules_actionPerformed(boolean automatic, boolean cluster) {
    try {
      // Comprobar:
      //   1) El FIS no tiene reglas
      //   2) Tiene al menos una salida (si no no se pueden generar conclusiones)
      //   3) Est� abierto el archivo de datos a partir del cual se incucir�n las reglas
   	  //System.out.println(" -> htsize="+jnikbct.getHashtableSize());
      if (automatic) {
     	    String InduceRulesLogFile= (JKBCTFrame.BuildFile("LogInduceRules")).getAbsolutePath();
     	    MessageKBCT.BuildLogFile(InduceRulesLogFile+".txt", this.IKBFile, JKBCTFrame.KBExpertFile, this.KBDataFile, "InduceRules");
            MessageKBCT.WriteLogFile((LocaleKBCT.GetString("AutomaticInduceRulesProcedure")).toUpperCase(), "InduceRules");
            MessageKBCT.WriteLogFile("----------------------------------", "InduceRules");
      }
      if (this.kbct.GetNbOutputs()==0) {
    	if (!automatic)  
            MessageKBCT.Information(this, LocaleKBCT.GetString("NoOutput_NoInduceRules"));
    	else
            MessageKBCT.WriteLogFile("   "+LocaleKBCT.GetString("NoOutput_NoInduceRules"), "InduceRules");
    		
      } else {
        int option=1;
        boolean end= false;
        boolean dataSelection= false;
        File temp= JKBCTFrame.BuildFile("tempInduceRules.fis");
        String fis_name= temp.getAbsolutePath();
        this.kbct.Save();
        this.kbct.SaveFIS(fis_name);
        this.fis_file_InduceRules= new JFIS(fis_name);

        // Antes de seleccionar algoritmo, peguntar si se quiere selecci�n de datos.
        if (this.kbct.GetNbActiveRules()>0) {
            if ( (automatic) || (MainKBCT.getConfig().GetClusteringSelection()) ) {
                if (MainKBCT.getConfig().GetDataSelection())
                	  option=0;
                  else
                    option=1;
            } else {
           	    option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeInductionWithDataSelection"));
            }
        }
        if (option==0) {
          // En caso afirmativo indicar cual es el umbral por defecto y preguntar si se quiere cambiar
          double ToleranceThreshold= MainKBCT.getConfig().GetTolThresDataSelection();
          if (!automatic) {
            String message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
                             +"   "+LocaleKBCT.GetString("ToleranceThreshold")+"= "+ToleranceThreshold+"\n"+"\n"
                             +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
                             +LocaleKBCT.GetString("DoYouWantToChangeSelectedValues");
            option= MessageKBCT.Confirm(this, message, 1, false, false, false);
          } else
        	  option=1;

          if (option==0) {
              // En caso afirmativo abrir ventana para recoger el nuevo valor umbral
              this.setDataSelectionParameters();
          }
          // A continuacion llamar al procedimiento de seleccion de datos con el umbral como parametro
          if (automatic) {
              MessageKBCT.WriteLogFile("   "+LocaleKBCT.GetString("DataSelection"), "InduceRules");
              MessageKBCT.WriteLogFile("    ->"+LocaleKBCT.GetString("ToleranceThreshold")+"= "+MainKBCT.getConfig().GetTolThresDataSelection(), "InduceRules");
          }
          this.DataSelection(MainKBCT.getConfig().GetTolThresDataSelection(), this.fis_file_InduceRules, automatic);
          dataSelection= true;
        } else if (option==-1) {
            end= true;
        }
        // Seleccionar el algoritmo de inducci�n de reglas
        if (!end) {
          if (!automatic) {
            Object SelectedAlgorithm= MessageKBCT.InduceRules(this, !cluster);
            if (SelectedAlgorithm != null) {
            	 //System.out.println("SelectedAlgorithm -> "+SelectedAlgorithm);
                 boolean aux= MainKBCT.getConfig().GetClusteringSelection();
                 if (SelectedAlgorithm.equals("Fast Prototyping Algorithm")) {
                     if (!cluster) {
                	     MainKBCT.getConfig().SetClusteringSelection(false);
                     }
                	 InduceRulesFPA(dataSelection, false);
                 } else if (SelectedAlgorithm.equals("Fuzzy Decision Trees")) {
                     if (!cluster) {
                	     MainKBCT.getConfig().SetClusteringSelection(false);
                     }
                     InduceRulesFDT(dataSelection, false);
                 } else if (SelectedAlgorithm.equals("Wang and Mendel")) {
                     if (!cluster) {
                	     MainKBCT.getConfig().SetClusteringSelection(false);
                     }
                     InduceRulesWM(dataSelection, fis_name, false);
                 } else if (SelectedAlgorithm.equals(LocaleKBCT.GetString("PrototypeRules"))) {
                	 MainKBCT.getConfig().SetClusteringSelection(true);
                	 buildPrototypeRules(false);
                 }
            	 MainKBCT.getConfig().SetClusteringSelection(aux);
            }
          } else {
              if (MainKBCT.getConfig().GetClusteringSelection()) {
                  buildPrototypeRules(true);
              } else {
                  String selAlg= MainKBCT.getConfig().GetInductionRulesAlgorithm();
        	      if (selAlg.equals("Fast Prototyping Algorithm"))
                      InduceRulesFPA(dataSelection, true);
                  else if (selAlg.equals("Fuzzy Decision Trees"))
                      InduceRulesFDT(dataSelection, true);
                  else if (selAlg.equals("Wang and Mendel"))
                      InduceRulesWM(dataSelection, fis_name, true);
              }
          }
        }
        temp.delete();
      }
      if (automatic) {
          MessageKBCT.WriteLogFile("----------------------------------", "InduceRules");
          MessageKBCT.WriteLogFile(LocaleKBCT.GetString("AutomaticInduceRulesEnded"), "InduceRules");
          Date d= new Date(System.currentTimeMillis());
          MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "InduceRules");
          MessageKBCT.CloseLogFile("InduceRules");
      }
    } catch (Throwable t) {
      MessageKBCT.Error(null, t);
    }
    //System.out.println(" -> htsize="+jnikbct.getHashtableSize());
  }
//------------------------------------------------------------------------------
  void setDataSelectionParameters() {
      final JDialog jd = new JDialog(this);
  	  jd.setIconImage(LocaleKBCT.getIconGUAJE().getImage());
      jd.setTitle(LocaleKBCT.GetString("Induce")+" "+LocaleKBCT.GetString("Rules"));
      jd.getContentPane().setLayout(new GridBagLayout());
      JPanel jPanelSaisie = new JPanel(new GridBagLayout());
      JPanel jPanelValidation = new JPanel(new GridBagLayout());
      JButton jButtonApply = new JButton(LocaleKBCT.GetString("Apply"));
      JButton jButtonCancel = new JButton(LocaleKBCT.GetString("Cancel"));
      JButton jButtonDefault = new JButton(LocaleKBCT.GetString("DefaultValues"));
      JLabel jLabelThreshold = new JLabel(LocaleKBCT.GetString("Threshold") + " :");
      this.jThreshold.setValue(MainKBCT.getConfig().GetTolThresDataSelection());
      jd.getContentPane().add(jPanelSaisie, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      jPanelSaisie.add(jLabelThreshold, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      jPanelSaisie.add(jThreshold, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
        ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
      jd.getContentPane().add(jPanelValidation, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
      jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      jPanelValidation.add(jButtonCancel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      jPanelValidation.add(jButtonDefault, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      jButtonApply.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
        double value= JMainFrame.this.jThreshold.getValue();
        if ( (value < 0) || (value > 1) ) {
            MessageKBCT.Information(JMainFrame.this, LocaleKBCT.GetString("ThresholdDataSelectionShouldBe"));
        } else {
      	  MainKBCT.getConfig().SetTolThresDataSelection(value);
          jd.dispose();
        }
      } } );
      jButtonCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
        jd.dispose();
      } } );
      jButtonDefault.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
      	JMainFrame.this.jThreshold.setValue(LocaleKBCT.DefaultTolThresDataSelection());
      } } );
      jd.setResizable(false);
      jd.setModal(true);
      jd.pack();
      jd.setLocation(JChildFrame.ChildPosition(this, jd.getSize()));
      jd.setVisible(true);
  }
//------------------------------------------------------------------------------
  void setClusterersNumber() {
      final JDialog jd = new JDialog(this);
      jd.setTitle(LocaleKBCT.GetString("PrototypeRules"));
      jd.getContentPane().setLayout(new GridBagLayout());
      JPanel jPanelSaisie = new JPanel(new GridBagLayout());
      JPanel jPanelValidation = new JPanel(new GridBagLayout());
      JButton jButtonApply = new JButton(LocaleKBCT.GetString("Apply"));
      JButton jButtonCancel = new JButton(LocaleKBCT.GetString("Cancel"));
      JButton jButtonDefault = new JButton(LocaleKBCT.GetString("DefaultValues"));
      JLabel jLabelClustersNumber = new JLabel(LocaleKBCT.GetString("ClustersNumber") + " :");
      this.jClustersNumber.setValue(MainKBCT.getConfig().GetClustersNumber());
      jd.getContentPane().add(jPanelSaisie, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      jPanelSaisie.add(jLabelClustersNumber, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      jPanelSaisie.add(this.jClustersNumber, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
        ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
      jd.getContentPane().add(jPanelValidation, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
      jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      jPanelValidation.add(jButtonCancel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      jPanelValidation.add(jButtonDefault, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      jButtonApply.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
        int nc= JMainFrame.this.jClustersNumber.getValue();
        if (nc < 0) {
            MessageKBCT.Information(JMainFrame.this, LocaleKBCT.GetString("ClustersNumberShouldBe"));
        } else {
      	  MainKBCT.getConfig().SetClustersNumber(nc);
          jd.dispose();
        }
      } } );
      jButtonCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
        jd.dispose();
      } } );
      jButtonDefault.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
      	JMainFrame.this.jClustersNumber.setValue(LocaleKBCT.DefaultClustersNumber());
      } } );
      jd.setModal(true);
      jd.pack();
      jd.setLocation(JChildFrame.ChildPosition(this, jd.getSize()));
      jd.setVisible(true);
  }
//------------------------------------------------------------------------------
  /*public void jMenuDataFingrams_actionPerformed() {
	  
  }*/
//------------------------------------------------------------------------------
  public void jMenuDataBuildKB_actionPerformed(String options) {
      // SMOTE
      if (MainKBCT.getConfig().GetSMOTE()) {
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("SMOTE")+"\n\n"+
        		  LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                  "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                  "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
          }
    	  //System.out.println("JMF: FS -> htsize="+jnikbct.getHashtableSize());
    	  //System.out.println("JMF: FS -> id="+jnikbct.getId());
          this.jMenuDataSmote_actionPerformed(true);
    	  //System.out.println("JMF: FS -> htsize="+jnikbct.getHashtableSize());
    	  //System.out.println("JMF: FS -> id="+jnikbct.getId());
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticSmoteEnded"));
          }
      }
	  // Feature Selection
      if (MainKBCT.getConfig().GetFeatureSelection()) {
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("FeatureSelection")+"\n\n"+
        		  LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                  "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                  "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
          }
    	  //System.out.println("JMF: FS -> htsize="+jnikbct.getHashtableSize());
    	  //System.out.println("JMF: FS -> id="+jnikbct.getId());
          this.jMenuDataFeatureSelection_actionPerformed(true);
    	  //System.out.println("JMF: FS -> htsize="+jnikbct.getHashtableSize());
    	  //System.out.println("JMF: FS -> id="+jnikbct.getId());
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticFeatureSelectionEnded"));
          }
      }
	  // Induce partitions
      if (MainKBCT.getConfig().GetInducePartitions()) {
          if (options!=null) {
        	  if (options.endsWith("2"))
            	  MainKBCT.getConfig().SetInductionNbLabels(2);
        	  else if (options.endsWith("3"))
        	      MainKBCT.getConfig().SetInductionNbLabels(3);
        	  else if (options.endsWith("4"))
        	      MainKBCT.getConfig().SetInductionNbLabels(4);
        	  else if (options.endsWith("5"))
        	      MainKBCT.getConfig().SetInductionNbLabels(5);
        	  else if (options.endsWith("6"))
        	      MainKBCT.getConfig().SetInductionNbLabels(6);
        	  else if (options.endsWith("7"))
        	      MainKBCT.getConfig().SetInductionNbLabels(7);
        	  else if (options.endsWith("8"))
        	      MainKBCT.getConfig().SetInductionNbLabels(8);
        	  else if (options.endsWith("9"))
        	      MainKBCT.getConfig().SetInductionNbLabels(9);

        	  if (options.contains("R"))
        	      MainKBCT.getConfig().SetInductionType("regular");
        	  else if (options.contains("H"))
        	      MainKBCT.getConfig().SetInductionType("hfp");
        	  else if (options.contains("K"))
        	      MainKBCT.getConfig().SetInductionType("kmeans");
          }
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("Induce_Partitions")+"\n\n"+
        		  LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                  "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                  "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
          }
    	  //System.out.println("JMF: IP -> htsize="+jnikbct.getHashtableSize());
    	  //System.out.println("JMF: IP -> id="+jnikbct.getId());
          this.jMenuDataInducePartitions_actionPerformed(true, true, false);
    	  //System.out.println("JMF: IP -> htsize="+jnikbct.getHashtableSize());
    	  //System.out.println("JMF: IP -> id="+jnikbct.getId());
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticInducePartitionsEnded"));
          }
      }
	  // Select partitions
      if (MainKBCT.getConfig().GetPartitionSelection()) {
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("Select_Partitions")+"\n\n"+
        		  LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                  "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                  "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
          }
    	  //System.out.println("JMF: SP -> htsize="+jnikbct.getHashtableSize());
    	  //System.out.println("JMF: SP -> id="+jnikbct.getId());
          this.jMenuDataInducePartitions_actionPerformed(true, false, true);
    	  //System.out.println("JMF: SP -> htsize="+jnikbct.getHashtableSize());
    	  //System.out.println("JMF: SP -> id="+jnikbct.getId());
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticSelectPartitionsEnded"));
          }
      }
      // Induce rules
      if (MainKBCT.getConfig().GetRuleInduction()) {
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("RuleInduction")+"\n\n"+
        		  LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                  "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                  "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
          }
    	  //System.out.println("JMF: IR -> htsize="+jnikbct.getHashtableSize());
    	  //System.out.println("JMF: IR -> id="+jnikbct.getId());
          this.jMenuDataInduceRules_actionPerformed(true, false);
    	  //System.out.println("JMF: IR -> htsize="+jnikbct.getHashtableSize());
    	  //System.out.println("JMF: IR -> id="+jnikbct.getId());
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticInduceRulesEnded"));
          }
      }
      // Rule ranking
      if (MainKBCT.getConfig().GetRuleRanking()) {
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticRuleRankingProcedure")+"\n\n"+
        		  LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                  "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                  "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
          }
          if (MainKBCT.getConfig().GetOrderRulesByOutputClass())
    	      this.jef.jMenuOrderByOutputClass_actionPerformed(true);

    	  if (MainKBCT.getConfig().GetOrderRulesByLocalWeight())
    	      this.jef.jMenuOrderByLocalWeightRules_actionPerformed(true);

    	  if (MainKBCT.getConfig().GetOrderRulesByGlobalWeight())
    	      this.jef.jMenuOrderByGlobalWeightRules_actionPerformed(true);

    	  if (MainKBCT.getConfig().GetOrderRulesByWeight())
    	      this.jef.jMenuOrderByWeightRules_actionPerformed(true);

    	  if (MainKBCT.getConfig().GetOrderRulesByLocalIntWeight())
    	      this.jef.jMenuOrderByLocalInterpretabilityRules_actionPerformed(true);

    	  if (MainKBCT.getConfig().GetOrderRulesByGlobalIntWeight())
    	      this.jef.jMenuOrderByGlobalInterpretabilityRules_actionPerformed(true);
      
    	  if (MainKBCT.getConfig().GetOrderRulesByIntWeight())
    	      this.jef.jMenuOrderByInterpretabilityRules_actionPerformed(true);

    	  if (MainKBCT.getConfig().GetOrderRulesByNumberPremises())
    	      this.jef.jMenuOrderByNbPremisesRules_actionPerformed(true);

    	  if (MainKBCT.getConfig().GetReverseOrderRules())
    	      this.jef.jMenuReverseOrder_actionPerformed(true);

          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticRuleRankingEnded"));
          }
      }
      // Consistency analysis
      if (MainKBCT.getConfig().GetSolveLingConflicts()) {
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("Consistency")+"\n\n"+
        		  LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                  "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                  "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
          }
          this.jef.jButtonConsistency_actionPerformed(true);
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticConsistencyEnded"));
          }
      }
      // LV reduction
      if (MainKBCT.getConfig().GetLVreduction()) {
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticLVreductionProcedure")+"\n\n"+
        		  LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                  "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                  "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
          }
          this.jef.jButtonLogView_actionPerformed(true);
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticLogViewEnded"));
          }
      }
      // Simplify KB
      if (MainKBCT.getConfig().GetSimplify()) {
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticSimplificationProcedure")+"\n\n"+
        		  LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                  "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                  "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
          }
          this.jef.jButtonSimplify_actionPerformed(true);
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticSimplificationEnded"));
          }
      }
      // Optimization DB
      if (MainKBCT.getConfig().GetOptimization()) {
          boolean aux= MainKBCT.getConfig().GetTESTautomatic();
    	  MainKBCT.getConfig().SetTESTautomatic(true);
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticOptimizationProcedure")+"\n\n"+
        		  LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                  "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                  "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
          }
          this.jef.jButtonOptimization_actionPerformed(true);
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticOptimizationEnded"));
          }
    	  MainKBCT.getConfig().SetTESTautomatic(aux);
      }
      // Completeness analysis
      if (MainKBCT.getConfig().GetCompleteness()) {
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticCompletenessProcedure")+"\n\n"+
        		  LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                  "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                  "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
          }    	  
          this.jef.jButtonCompleteness_actionPerformed(true);
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticCompletenessEnded"));
          }
      }
      // Fingrams
      if (MainKBCT.getConfig().GetFingram()) {
          if (!MainKBCT.getConfig().GetTESTautomatic()) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticFingramsProcedure")+"\n\n"+
        		  LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                  "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                  "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
          }
          boolean aux= MainKBCT.getConfig().GetTESTautomatic();
          MainKBCT.getConfig().SetTESTautomatic(true);
          if ( (this.jef!=null) && (this.jef.Temp_kbct.GetNbActiveRules()>0) ) { 
              this.jef.jButtonInfer_actionPerformed();
              boolean auxFWS= MainKBCT.getConfig().GetFingramWS(); 
              boolean auxFWOS= MainKBCT.getConfig().GetFingramWOS(); 
              //System.out.println("auxFWS="+auxFWS);
              //System.out.println("auxFWOS="+auxFWOS);
              if ( (auxFWS) && (auxFWOS) )
                    this.jef.jif.jMenuFingrams_actionPerformed("ALL");
              else if (auxFWS)
                    this.jef.jif.jMenuFingrams_actionPerformed("WS");
              else if (auxFWOS)
                    this.jef.jif.jMenuFingrams_actionPerformed("WOS");

              if (!MainKBCT.getConfig().GetTESTautomatic()) {
                  MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticFingramsProcedureEnded"));
              }
 	          boolean reg;
 	          if (this.jef.Temp_kbct.GetOutput(1).GetClassif().equals("yes"))
	              reg= false;
 	          else
 	    	      reg= true;

		      this.jef.AssessingAccuracy(true,false);
              double[] accind= new double[3];
              accind[0]= this.jef.jrbqf.getCoverage();
              if (reg) {
	               accind[1]= this.jef.jrbqf.getRMSE();
	               accind[2]= this.jef.jrbqf.getMSE();
              } else {
                   accind[1]= this.jef.jrbqf.getAccIndexLRN();
                   accind[2]= this.jef.jrbqf.getAvConfFD();
	          }
              boolean[] covdata= this.jef.jrbqf.getCovData();
              this.jef.jrbqf.dispose();
	          this.jef.jrbqf= null;
              MainKBCT.getConfig().SetTESTautomatic(aux);
	          this.jef.AssessingInterpretability(true);
              double[] intind= this.jef.jkbif.getInterpretabilityIndicators();
	          this.jef.jkbif.dispose();
	          this.jef.jkbif= null;
 	          double fingramsIntIndex= this.jef.jif.jlf.getCIS();
 	          MainKBCT.getConfig().SetFingramWS(auxFWS);
 	          MainKBCT.getConfig().SetFingramWOS(auxFWOS);
 	          if (auxFWS && auxFWOS)
    	          this.visualizeSVG("ALL", reg, accind, covdata, intind, fingramsIntIndex, null);
              else if (auxFWS)
    	          this.visualizeSVG("WS", reg, accind, covdata, intind, fingramsIntIndex, null);
              else if (auxFWOS)
    	          this.visualizeSVG("WOS", reg, accind, covdata, intind, fingramsIntIndex, null);
          } else {
        	  MainKBCT.getConfig().SetTESTautomatic(aux);
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticFingramsProcedureEnded"));
          	  MessageKBCT.Information(this, LocaleKBCT.GetString("NoActiveRule"));
          }
      }
  }
//------------------------------------------------------------------------------
  public void jMenuOpen_actionPerformed(String file) {
    if (file==null) {
	  JOpenFileChooser file_chooser = new JOpenFileChooser(MainKBCT.getConfig().GetKBCTFilePath());
      file_chooser.setAcceptAllFileFilterUsed(false);
      //JFileFilter filter1 = new JFileFilter(("IKB").toLowerCase(), LocaleKBCT.GetString("FilterIKBFile"));
      JFileFilter filter2 = new JFileFilter(("XML").toLowerCase(), LocaleKBCT.GetString("FilterIKBFile"));
      //file_chooser.addChoosableFileFilter(filter1);
      file_chooser.addChoosableFileFilter(filter2);
      file_chooser.setFileFilter(filter2);
      if( file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ) {
          MainKBCT.getConfig().SetKBCTFilePath(file_chooser.getSelectedFile().getParent());
          this.jMenuOpenIKB(file_chooser.getSelectedFile().getAbsolutePath());
      } 
    } else {
        MainKBCT.getConfig().SetKBCTFilePath(file);
        this.jMenuOpenIKB(file);
    }
  }
//------------------------------------------------------------------------------
  private void jMenuOpenIKB(String file) {
      try {
        this.IKBFile= file;
        this.oldIKBFile= this.IKBFile;
        if( this.Close() == false )
          return;

        this.KB = true;
        boolean warning= this.Open();
        if (!warning)
            this.InitJKBCTFrameWithKBCT();
        else {
            this.KB= false;
            this.IKBFile="";
            this.oldIKBFile="";
            this.ResetKBExpertFile();
            this.ResetKBDataFile();
            this.InitJKBCTFrame();
        }
        
      } catch( Exception exception ) {
          exception.printStackTrace();
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JMainFrame: jMenuOpen_actionPerformed 1: "+exception);
          this.KB= false;
          this.IKBFile="";
          this.oldIKBFile="";
          this.ResetKBExpertFile();
          this.ResetKBDataFile();
      } catch( Throwable t ) {
          t.printStackTrace();
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JMainFrame: jMenuOpen_actionPerformed 2: "+t);
          this.KB= false;
          this.IKBFile="";
          this.oldIKBFile="";
          this.ResetKBExpertFile();
          this.ResetKBDataFile();
          this.InitJKBCTFrame();
      }
  }
//------------------------------------------------------------------------------
  void jMenuExpertOpen_actionPerformed() {
    JOpenFileChooser file_chooser = new JOpenFileChooser(MainKBCT.getConfig().GetKBCTFilePath());
    file_chooser.setAcceptAllFileFilterUsed(false);
    //JFileFilter filter1 = new JFileFilter(("KB").toLowerCase(), LocaleKBCT.GetString("FilterKBFile"));
    JFileFilter filter2 = new JFileFilter(("XML").toLowerCase(), LocaleKBCT.GetString("FilterKBFile"));
    file_chooser.addChoosableFileFilter(filter2);
    //file_chooser.addChoosableFileFilter(filter1);
    file_chooser.setFileFilter(filter2);
    if( file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ) {
      MainKBCT.getConfig().SetKBCTFilePath(file_chooser.getSelectedFile().getParent());
      try {
        boolean warning= false;
        JKBCTFrame.KBExpertFile = file_chooser.getSelectedFile().getAbsolutePath();
        //System.out.println("JKBCTFrame.KBExpertFile= "+JKBCTFrame.KBExpertFile);
        if (JKBCTFrame.KBExpertFile.endsWith("xml")) {
    	    XMLParser theParser = new XMLParser();
            Hashtable hsystem= (Hashtable)theParser.getXMLinfo(JKBCTFrame.KBExpertFile, "SystemInfo");
            String systemName= (String)hsystem.get("Name");
            if (systemName==null) {
                //System.out.println("JKBCTFrame.KBExpertFile 1");
            	MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("OpenValidXML"));
            	warning= true;
            } else {
                //System.out.println("JKBCTFrame.KBExpertFile 2");
            	hsystem=null;
            	warning= false;
            } 
        }
        if (!warning) {
            if (!JKBCTFrame.KBExpertFile.equals("") && (new File(JKBCTFrame.KBExpertFile)).exists())
                this.jef= new JExpertFrame(JKBCTFrame.KBExpertFile, this, null);
            else
                this.jef = new JExpertFrame(this);

        if (!KBExpertFile.endsWith("xml"))
        	KBExpertFile= KBExpertFile+".xml";

        //if (KBExpertFile.endsWith("xml"))
        //	KBExpertFile= KBExpertFile.substring(0,KBExpertFile.length()-4);
        
            this.jef.setVisible(false);
            if (!LocaleKBCT.isWindowsPlatform())
        	    this.jef.dispose();
        
            this.kbct= this.jef.Temp_kbct;
            int NbOutputs= this.kbct.GetNbOutputs();
            String[] OutputType= new String[NbOutputs];
            for (int n=0; n<NbOutputs; n++)
                 OutputType[n]= this.kbct.GetOutput(n+1).GetType();

            JConvert.SetFISoptionsDefault(NbOutputs, OutputType);
            this.oldConjunction=JConvert.conjunction;
            this.oldDisjunction= new String[NbOutputs];
            this.oldDefuzzification= new String[NbOutputs];
            for (int n=0; n<NbOutputs; n++) {
                 this.oldDisjunction[n]= JConvert.disjunction[n];
                 this.oldDefuzzification[n]= JConvert.defuzzification[n];
            }
            if (this.kbct_Data != null) {
                if ( (this.kbct.GetNbInputs() < this.kbct_Data.GetNbInputs()) ||
                     (this.kbct.GetNbOutputs() < this.kbct_Data.GetNbOutputs()) )
                      throw new Throwable("Incorrect NbInput or NbOutput");
            }
            if (!this.KBDataFile.equals(""))
                this.jDataButton.setEnabled(true);

            this.jMenuExpertNew.setEnabled(false);
            this.jMenuExpertOpen.setEnabled(false);
            this.jMenuExpertClose.setEnabled(true);
            this.jExpertButton.setEnabled(true);
            this.InitJKBCTFrameWithKBCT();
        } else {
            this.ResetKBExpertFile();
            this.InitJKBCTFrame();
        }
      } catch( Exception exception ) {
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JMainFrame: jMenuExpertOpen_actionPerformed 1: "+exception);
          this.ResetKBExpertFile();
      } catch( Throwable t ) {
          if (!t.getMessage().equals("Incorrect NbInput or NbOutput"))
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JMainFrame: jMenuExpertOpen_actionPerformed 2: "+t);
          else
            MessageKBCT.Information(this, LocaleKBCT.GetString("Incorrect_KBCT_file"));

          this.ResetKBExpertFile();
          this.InitJKBCTFrame();
      }
    }
  }
//------------------------------------------------------------------------------
// Solo utilizado desde MainKBCT.java para crear ficheros KB de forma automatica
  public void jMenuExpertOpen_actionPerformed(String KBfile) {
      //System.out.println("KBfile="+KBfile);
      File selFile= new File(KBfile);
      //System.out.println("Parent="+selFile.getParent());
	  MainKBCT.getConfig().SetKBCTFilePath(selFile.getParent());
      try {
        KBExpertFile = selFile.getAbsolutePath();
        //System.out.println("KBExpertFile="+KBExpertFile);
        if (!JKBCTFrame.KBExpertFile.equals("") && (new File(JKBCTFrame.KBExpertFile)).exists())
            this.jef= new JExpertFrame(JKBCTFrame.KBExpertFile, this, null);
        else
            this.jef = new JExpertFrame(this);

        //if (KBExpertFile.endsWith("xml"))
        //	KBExpertFile= KBExpertFile.substring(0,KBExpertFile.length()-4);
      
        //System.out.println("setVisible");
        this.jef.setVisible(false);
        if (!LocaleKBCT.isWindowsPlatform())
        	this.jef.dispose();
        
        this.kbct= this.jef.Temp_kbct;
        int NbOutputs= this.kbct.GetNbOutputs();
        //System.out.println("NbOutputs="+NbOutputs);
        String[] OutputType= new String[NbOutputs];
        for (int n=0; n<NbOutputs; n++)
          OutputType[n]= this.kbct.GetOutput(n+1).GetType();

        JConvert.SetFISoptionsDefault(NbOutputs, OutputType);
        this.oldConjunction=JConvert.conjunction;
        this.oldDisjunction= new String[NbOutputs];
        this.oldDefuzzification= new String[NbOutputs];
        for (int n=0; n<NbOutputs; n++) {
          this.oldDisjunction[n]= JConvert.disjunction[n];
          this.oldDefuzzification[n]= JConvert.defuzzification[n];
        }
        if (this.kbct_Data != null) {
          if ( (this.kbct.GetNbInputs() < this.kbct_Data.GetNbInputs()) ||
              (this.kbct.GetNbOutputs() < this.kbct_Data.GetNbOutputs()) )
            throw new Throwable("Incorrect NbInput or NbOutput");
        }
        if (!this.KBDataFile.equals(""))
          this.jDataButton.setEnabled(true);

        this.jMenuExpertNew.setEnabled(false);
        this.jMenuExpertOpen.setEnabled(false);
        this.jMenuExpertClose.setEnabled(true);
        this.jExpertButton.setEnabled(true);
        //System.out.println("InitJKBCTFrameWithKBCT");
        this.InitJKBCTFrameWithKBCT();
      } catch( Exception exception ) {
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JMainFrame: jMenuExpertOpen_actionPerformed 3: "+exception);
          this.ResetKBExpertFile();
      } catch( Throwable t ) {
    	  t.printStackTrace();
          if (!t.getMessage().equals("Incorrect NbInput or NbOutput"))
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JMainFrame: jMenuExpertOpen_actionPerformed 4: "+t);
          else
            MessageKBCT.Information(this, LocaleKBCT.GetString("Incorrect_KBCT_file"));

          this.ResetKBExpertFile();
          this.InitJKBCTFrame();
      }
  }
//------------------------------------------------------------------------------
  void jMenuDataOpen_actionPerformed() {
    try {
      JOpenFileChooser file_chooser= new JOpenFileChooser(MainKBCT.getConfig().GetDataFilePath());
      if( file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ) {
        MainKBCT.getConfig().SetDataFilePath(file_chooser.getSelectedFile().getParent());
        this.DFile= file_chooser.getSelectedFile().getPath();
        this.OrigKBDataFile= this.DFile;
        this.OpenDataFile1();
      }
    } catch( Throwable t ) {
      t.printStackTrace();
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JMainFrame: jMenuDataOpen_actionPerformed: "+t);
      this.ResetKBExpertFile();
      this.ResetKBDataFile();
      this.InitJKBCTFrame();
    }
  }
//------------------------------------------------------------------------------
// S�lo usado desde MainKBCT.java para crear archivos IKB de forma autom�tica
  public void jMenuDataOpen_actionPerformed(String dataFile) {
    try {
        File df= new File(dataFile);
    	MainKBCT.getConfig().SetDataFilePath(df.getParent());
        this.DFile= df.getPath();
        this.OrigKBDataFile= this.DFile;
        this.OpenDataFile1();
    } catch( Throwable t ) {
      t.printStackTrace();
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JMainFrame: jMenuDataOpen_actionPerformed: "+t);
      this.ResetKBExpertFile();
      this.ResetKBDataFile();
      this.InitJKBCTFrame();
    }
  }
//------------------------------------------------------------------------------
  public void jMenuClose_actionPerformed() { this.Close(); }
//------------------------------------------------------------------------------
  void jMenuExpertClose_actionPerformed() {
    try {
      this.jMenuExpertClose.setEnabled(false);
      this.jExpertButton.setEnabled(false);
      this.jDataButton.setEnabled(false);
      JKBCTFrame.KBExpertFile="";
      if (jef != null)
        this.jef.jMenuExit_actionPerformed();

      this.jef= null;
      if (this.kbct != null) {
        this.kbct.Close();
        this.kbct.Delete();
      }
      this.kbct= null;
      this.InitJKBCTFrameWithKBCT();
    } catch( Throwable t ) {
       MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JMainFrame: jMenuExpertClose_actionPerformed: "+t);
       this.ResetKBExpertFile();
       this.InitJKBCTFrame();
    }
  }
//------------------------------------------------------------------------------
  void jMenuDataClose_actionPerformed() {
    try {
      this.jMenuDataClose.setEnabled(false);
      this.jMenuDataView.setEnabled(false);
      this.jMenuDataTable.setEnabled(false);
      this.jDataButton.setEnabled(false);
      this.jMenuDataGenerate.setEnabled(false);
      this.jMenuDataGenerateSample.setEnabled(false);
      this.jMenuDataGenerateCrossValidation.setEnabled(false);
      this.jMenuDataInduce.setEnabled(false);
      this.jMenuDataInducePartitions.setEnabled(false);
      this.jMenuDataInduceRules.setEnabled(false);
      this.jMenuDataBuildKB.setEnabled(false);
      //this.jMenuDataFingrams.setEnabled(false);
      if (this.DataFileNoSaved != null) {
          this.DataFileNoSaved.Close();
          this.DataFileNoSaved= null;
      }
      if (this.DataFile != null) {
          this.DataFile.Close();
          this.DataFile= null;
      }
      if (this.kbct_Data != null) {
          this.kbct_Data.Close();
          this.kbct_Data.Delete();
      }
      this.kbct_Data= null;
      this.KBDataFile="";
      this.OrigKBDataFile="";
      this.SelectedVariablesDataFile="";
      this.DataFileLabels= false;
      this.VNames=null;
      this.DataNbVariables=0;
      if ( (this.jef != null) && (this.jef.kbct_Data != null) )
        this.jef.kbct_Data= null;

      this.InitJKBCTFrameWithKBCT();
    } catch( Throwable t ) {
       MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JMainFrame: jMenuDataClose_actionPerformed: "+t);
       this.ResetKBDataFile();
       this.InitJKBCTFrame();
    }
  }
//------------------------------------------------------------------------------
  void jMenuExpertSaveAs_actionPerformed() {
    try { SaveExpertKBAs(); }
    catch( Throwable t ) {
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JMainFrame: jMenuExpertSaveAs_actionPerformed: "+t);
    }
  }
//------------------------------------------------------------------------------
  void jMenuDataSaveAs_actionPerformed() {
    try { SaveDataAs(); }
    catch( Throwable t ) {
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JMainFrame: jMenuDataSaveAs_actionPerformed: "+t);
    }
  }
//------------------------------------------------------------------------------
  public void jMenuSave_actionPerformed() {
    try { 
        if ( (MainKBCT.getConfig().GetTESTautomatic()) && (this.DataFileNoSaved!=null) ) {
        	this.selectVariablesInDataFile();
        }
        this.Save();
    } catch( Throwable except ) {
    	except.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JMainFrame: jMenuSave_actionPerformed: "+except);
    }
  }
//------------------------------------------------------------------------------
  void jMenuDataSmote_actionPerformed(boolean automatic) {
	  try {
          int option= 0;
          if (!automatic)
		      option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeSMOTE"), 0, false, false, false);
          
          if (option==0) {
        	  if (!automatic)
                  this.setSMOTEparameters();
        	  
    		  JKBCTOutput jout= this.jef.Temp_kbct.GetOutput(1);
    		    int NbClasses= jout.GetLabelsNumber();
    		    double[] outClasses= new double[NbClasses]; 
    		    for (int n=0; n<NbClasses; n++) {
    		    	 outClasses[n]= (new Double(jout.GetMP(n+1))).doubleValue();
    		    }
    	        int lim=1;
    	        if (NbClasses>2)
    	    	    lim= NbClasses;
    	      
    		    JExtendedDataFile[] result= new JExtendedDataFile[lim];
    		    JExtendedDataFile AllDataFile= this.getDataFile();
    		    String file_name= AllDataFile.FileName();
    		    //System.out.println("file_name="+file_name);
    	        double[][] data= AllDataFile.GetData();
    	        int nbVars= AllDataFile.VariableCount();
    	        int nbRows= AllDataFile.DataLength();
    	        //System.out.println("FN="+file_name);
    	        //System.out.println("Columns="+nbVars);
    	        //System.out.println("Rows="+nbRows);
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
    	        int[] contOutputSamples= new int[NbClasses];
    	        for (int n=0; n<nbRows; n++) {
    	        	 for (int m=0; m<NbClasses; m++) {
    	        	      if (data[n][nbVars-1]==outClasses[m]) {
    	        		      contOutputSamples[m]++;
    	        	      }
    	        	 }
    	        }
    	        boolean flagSM= true;
    	        //System.out.println("n=0  ->  "+contOutputSamples[0]);
    		    for (int n=1; n<NbClasses; n++) {
    		    	 //System.out.println("n="+n+"  ->  "+contOutputSamples[n]);
    		    	 if (contOutputSamples[n] != contOutputSamples[0]) {
    		    		 flagSM= false;
    		    		 break;
    		    	 }
    		    }
    	   	    //System.out.println("flagSM="+flagSM);
    		    if (!flagSM) {
    	          String[] filenames= new String[lim];
    		      if (NbClasses > 2) {
    			    // Translate to binary class
    	            for (int n=0; n<NbClasses; n++) {
    	               result[n]= new JExtendedDataFile(file_name, true);
    	               int c1=0, c2=0;
    	               for (int i=0; i<nbRows; i++) {
    	            	    for (int j=0; j<nbVars; j++) {
    	            	    	 if (j==nbVars-1) {
    	            	    		 if (n==0) {
    	            	    		     //if (data[i][j]!= n+1) {
    	            	    		     if (data[i][j]!= outClasses[n]) {
    	            	    			     result[n].setDataElement(i, j, 2);
    	            	    			     c2++;
    	            	    		     } else {
    	            	    			     result[n].setDataElement(i, j, 1);
    	            	    			     c1++;
    	            	    		     }
    	            	    		 } else if (n>0) {
    	            	    		     //if (data[i][j]!=n+1) {
    	            	    		     if (data[i][j]!=outClasses[n]) {
    	            	    			     result[n].setDataElement(i, j, 1);
    	            	    			     c1++;
    	            	    		     } else {
    	            	    			     result[n].setDataElement(i, j, 2);
    	            	    			     c2++;
    	            	    		     }
    	            	    		 }
    	            	    	 } else  { 
    	                             result[n].setDataElement(i, j, data[i][j]);
    	            	    	 }
    	            	    }
    	               }
    	               // reorder data
    	               //this.reorderData(result[n], nbRows, nbVars, c1, c2);
    	        	   //String fn= file_name+"."+String.valueOf(n+1);
    	        	   String fn= file_name+"."+outClasses[n];
    	        	   filenames[n]= fn;
    	               result[n].Save(fn);
    	            }
    		      } else {
    			      result[0]= AllDataFile;
    			      filenames[0]= file_name;
    		      }
    		      // Save data files into KEEL format (adding header)
    	          this.saveAsKEELfile(filenames, result, outClasses);
    		      // CALL to KEEL oversampling methods
    		      // SMOTE
    	          this.buildConfFiles(filenames, outClasses, newSamples);
    	          //String jarfile= "C:/Development/GUAJE1/libs/keellibs/RunKeel.jar";
    	          String jarfile= MainKBCT.getConfig().GetKbctPath()
    	                      +System.getProperty("file.separator")+"libs"
    	                      +System.getProperty("file.separator")+"keellibs"
    	                      +System.getProperty("file.separator")+"RunKeel.jar";

    	          String command="java -Xmx512m -jar "+jarfile;
    	          //System.out.println("command="+command);
    	          int exitVal= this.runProcess(command,"tempOutSmote.txt");
    	          //System.out.println("ExitValue: " + exitVal);
    	          if (exitVal==0) {
    	            // Join eoversampled files
    	    	    this.removeSmoteTestFiles(filenames);
    	            this.buildJoinJEDF(filenames, nbVars, nbRows, data, file_name);
    	          }
    		    } else {
    		        MessageKBCT.Information(this, LocaleKBCT.GetString("SmoteError-BalancedClasses"));
    		    }
          }
	  } catch( Throwable except ) {
	    	except.printStackTrace();
	        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JMainFrame: jMenuDataSmote_actionPerformed: "+except);
	  }
  }
//------------------------------------------------------------------------------
  private void saveAsKEELfile(String[] files, JExtendedDataFile[] jedd, double[] outClasses) throws Throwable {
      DecimalFormat df= new DecimalFormat();
      df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
      df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
      DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
      dfs.setDecimalSeparator((new String(".").charAt(0)));
      df.setDecimalFormatSymbols(dfs);
      df.setGroupingSize(20);
      String prob= this.jef.Temp_kbct.GetName();
	  //System.out.println("fisname -> "+prob);
	  int Nins= this.jef.Temp_kbct.GetNbInputs();
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
			    	
			    JKBCTInput in= this.jef.Temp_kbct.GetInput(m+1);
			    double[] range= in.GetInputInterestRange();
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
  }
//------------------------------------------------------------------------------
  private void buildConfFiles(String[] filenames, double[] outClass, double[] NewSamples) throws Throwable {
	  String runkeelxml= MainKBCT.getConfig().GetKbctPath()
	                     +System.getProperty("file.separator")+"RunKeel.xml";

	  //System.out.println("runkeelxml -> "+runkeelxml);
      PrintStream ps= new PrintStream(new FileOutputStream(runkeelxml, false));
      ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      ps.println("<execution>");
      String lib= MainKBCT.getConfig().GetKbctPath()
                  +System.getProperty("file.separator")+"libs"
                  +System.getProperty("file.separator")+"keellibs"
                  +System.getProperty("file.separator")+"SMOTE-I.jar";
      for (int n=0; n<filenames.length; n++) {
          ps.println("<sentence>");
          ps.println("<command>java</command>");
          ps.println("<option>-Xmx512m</option>");
          ps.println("<option />");
          ps.println("<option>-jar</option>");
          ps.println("<option />");
          ps.println("<option />");
          ps.println("<executableFile>"+lib+"</executableFile>");
          String confFile= JKBCTFrame.BuildFile("smoteConf"+outClass[n]+".txt").getAbsolutePath();
          ps.println("<scriptFile>"+confFile+"</scriptFile>");
          ps.println("</sentence>");
          this.saveSMOTEconfFile(filenames[n]+".dat", confFile, NewSamples[n]);
      }
      ps.println("</execution>");
      ps.flush();
      ps.close();
  }
//------------------------------------------------------------------------------
  private void saveSMOTEconfFile(String dfile, String fname, double NewSamples) throws Throwable {
      //System.out.println("smoteConfFile -> "+fname);
      PrintStream ps= new PrintStream(new FileOutputStream(fname, false));
      ps.println("algorithm = SMOTE");
		// SMOTE: Synthetic Minority Over-sampling Technique
		// N.V. Chawla, K.W. Bowyer, L.O. Hall, W.P. Kegelmeyer.
		// SMOTE: synthetic minority over-sampling technique.
		// Journal of Artificial Intelligence Research 16 (2002) 321-357

		// SMOTE generate positive data instances from other instances in the
		// original dataset
		// selecting k nearest neighbors and using them to perform arithmetical
		// perations to generate the new instance
      ps.println("inputData = \""+dfile+"\" \""+dfile+"\"");
      ps.println("outputData = \""+dfile+".smote.dat\" \""+dfile+".smote.tst.dat\"");
      ps.println("");
      ps.println("seed = 1286082570");
      //ps.println("Number of Neighbors = 5");
      ps.println("Number of Neighbors = "+MainKBCT.getConfig().GetSMOTEnumberOfNeighbors());
		// generate samples as neighbors of both (minority and majority) classes
		// ps.println("Type of SMOTE = both");
		// generate samples as neighbors ONLY of the minority class
		// ps.println("Type of SMOTE = minority");
		// generate samples as neighbors ONLY of the majority class
		// ps.println("Type of SMOTE = ASMO");
      ps.println("Type of SMOTE = "+MainKBCT.getConfig().GetSMOTEtype());
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
//------------------------------------------------------------------------------
  private void removeSmoteTestFiles(String[] fnames) throws Throwable {
	  for (int n=0; n<fnames.length; n++) {
           File f= new File(fnames[n]+".dat.smote.tst.dat");
           if (f.exists())
        	   f.delete();
	  }
  }
//------------------------------------------------------------------------------
  private void saveSyntheticDataFile(String file_name, double[][] data, int NR, int NC) throws Exception {
	  PrintStream ps= new PrintStream(new FileOutputStream(file_name, false));
      this.saveSyntheticDataFile(ps, data, NR, NC);
      ps.flush();
      ps.close();
  }
//------------------------------------------------------------------------------
  private void saveSyntheticDataFile(PrintStream ps, double[][] data, int NR, int NC) throws Exception {
      DecimalFormat df= new DecimalFormat();
      df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
      df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
      DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
      dfs.setDecimalSeparator((new String(".").charAt(0)));
      df.setDecimalFormatSymbols(dfs);
      df.setGroupingSize(20);
	  for (int n=0; n<NR; n++) {
          for (int m=0; m<NC; m++) {
               ps.print(df.format(data[n][m]));
        	   if (m < NC-1)
                   ps.print(",");
          }
          ps.println();
      }
  }
//------------------------------------------------------------------------------
  private JExtendedDataFile buildJoinJEDF(String[] fnames, int NC, int initNbData, double[][] trainData, String trainDataName) throws Throwable {
      JExtendedDataFile[] dd= new JExtendedDataFile[fnames.length];
	  for (int n=0; n<fnames.length; n++) {
    	   String smotedata= fnames[n]+".dat.smote.dat";
	       LineNumberReader lnr= new LineNumberReader(new InputStreamReader(new FileInputStream(smotedata)));
           String l;
           while ((l=lnr.readLine())!=null) {
    	      if (l.startsWith("@data")) {
    	          PrintStream ps= new PrintStream(new FileOutputStream(smotedata+".txt", false));
    	          while ((l=lnr.readLine())!=null) {
                     ps.println(l);
    	          }
    	          ps.flush();
    	          ps.close();
    	      }
           }
           lnr.close();
           dd[n]= new JExtendedDataFile(smotedata+".txt",true);
      }
	  //System.out.println("initNbData= "+initNbData);
	  int[] NbAddedData= new int[fnames.length];
	  int[] accAddedData= new int[fnames.length];
	  int totalAddedData=0;
	  for (int n=0; n<fnames.length; n++) {
		   NbAddedData[n]= dd[n].DataLength() - initNbData;
		   totalAddedData= totalAddedData + NbAddedData[n];
	       accAddedData[n]= totalAddedData;
		   //System.out.println("-> NbData("+String.valueOf(n+1)+")= "+dd[n].DataLength());
	  }
	  //System.out.println("totalAddedData= "+totalAddedData);
	  //for (int n=0; n<accAddedData.length; n++) {
		//   System.out.println(" acc["+n+"]= "+accAddedData[n]);
	  //}
	  double[][] d0= trainData;
	  int NR= initNbData+totalAddedData;
	  double[][] data= new double[NR][NC];
	  int r=initNbData;
      for (int n=0; n<NR; n++) {
    	  if (n < initNbData) {
              for (int m=0; m<NC; m++) {
                   data[n][m]= d0[n][m];
              }
    	  } else {
              //for (int m=0; m<NC; m++) {
                //  data[n][m]= 0;
              //}
    		  int ind= this.getIndexAccAddedData(accAddedData,n-initNbData);
    		  //System.out.println("n="+n+"  -> ind="+ind);
    		  if (n < initNbData + accAddedData[ind-1]) {
                  for (int m=0; m<NC; m++) {
                	   if (m==NC-1)
                		   data[n][m]= ind;
                	   else
                           data[n][m]= dd[ind-1].GetData()[r][m];
                  }
                  r++;
                  if (r==dd[ind-1].GetData().length) {
                	  //System.out.println("r="+r);
                	  r=initNbData;
                  }
    		  }    		  
    	  }
      }
      File fnew= new File(trainDataName+".smote.txt");
      String syntDataFileName= fnew.getAbsolutePath();
      //System.out.println("newDataOversampledFile="+fnew.getAbsolutePath());
      this.saveSyntheticDataFile(syntDataFileName, data, NR, NC);
	  JExtendedDataFile res= new JExtendedDataFile(fnew.getAbsolutePath(),true);
	  return res;
  }
//------------------------------------------------------------------------------
  private int getIndexAccAddedData(int[] data, int k) {
	  for (int n=0; n<data.length; n++) {
		   if (k < data[n])
			   return n+1;
	  }
	  return 0;
  }
//------------------------------------------------------------------------------
  private int runProcess(String command, String tempOutFile) {
	 int exitVal= -1;
     Process p= null;
	 try {
	    Runtime rt= Runtime.getRuntime();
	    //System.out.println("runProcess: cmd -> "+command);
	    p= rt.exec(command);
	    // cleaning output buffer
	    InputStream is = p.getInputStream();
	    InputStreamReader isr = new InputStreamReader(is);
	    BufferedReader br = new BufferedReader(isr);
	    FileOutputStream fos= new FileOutputStream(JKBCTFrame.BuildFile(tempOutFile), false);
	    PrintStream pOutFile= new PrintStream(fos);
	    String line;
	    while ((line = br.readLine()) != null)
		        pOutFile.println(line);

	    pOutFile.flush();
	    pOutFile.close();
	    isr.close();
	    is.close();
	    fos.close();
	    //p.waitFor();
	    //System.out.println("runProcess: waiting.......");
	    exitVal = p.waitFor();
	 } catch (Exception e) {
		  e.printStackTrace();
      	  MessageKBCT.Error( null, e );
		  p.destroy();
	 }
     return exitVal;
  }
//------------------------------------------------------------------------------
  void jMenuDataFeatureSelection_actionPerformed(boolean automatic) {
    try { 
        if (!automatic) {
            MessageKBCT.Information(this, LocaleKBCT.GetString("FeatureSelection")+"\n\n"+
          		  LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                    "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                    "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
        }
    	// Build C45 tree (Weka J48)
        // default options
        // -C 0.25 -M 2
    	J48 c45model= new J48();
    	String P1= MainKBCT.getConfig().GetFeatureSelectionC45P1();
    	String P2= MainKBCT.getConfig().GetFeatureSelectionC45P2();
    	String P3= MainKBCT.getConfig().GetFeatureSelectionC45P3();
    	String P4= MainKBCT.getConfig().GetFeatureSelectionC45P4();
		String[] c45options= {P1,P2,P3,P4};        		
    	c45model.setOptions(c45options);
  	    JExtendedDataFile jedfAux=this.jef.getJExtDataFile();
	    ParserWekaFiles pwf= new ParserWekaFiles(jedfAux, this.jef.Temp_kbct);
	    String fname= jedfAux.FileName()+".arff";
	    pwf.buildWekaARFFfile(fname);
	    FileReader readerLRN = new FileReader(fname);
        Instances instancesLRN = new Instances(readerLRN);
        instancesLRN.setClassIndex(instancesLRN.numAttributes() - 1);
    	c45model.buildClassifier(instancesLRN);
    	String wekaLOG= fname+".C45.log.txt";
    	PrintStream log= new PrintStream(new FileOutputStream(wekaLOG, false));
        log.println(c45model.toString());
    	log.flush();
    	log.close();
    	// Read built tree
    	String[] SelectedVars= this.readWekaTree(wekaLOG);

	    // Update Temp_kbct in JExpertFrame
	    // System.out.println("Updating kbct");
	    int lim1= this.jef.Temp_kbct.GetNbInputs();
	    int lim2= SelectedVars.length/2;
	    for (int n=lim1; n>0; n--) {
		     // System.out.println("Updating kbct: n="+n);
	    	 JKBCTInput in= this.jef.Temp_kbct.GetInput(n);
	    	 String vname= in.GetName();
		     // System.out.println("Updating kbct: vname="+vname);
	    	 boolean warning= false;
             for (int m=0; m<lim2; m++) {
    		      // System.out.println("Updating kbct: m="+m);
            	  if (vname.equals(SelectedVars[2*m])) {
            		  warning= true;
            		  // update number of labels
        		      System.out.println("Updating kbct: changing number of labels");
                      Vector MPV= new Vector();
                      for (int k=0; k<in.GetLabelsNumber(); k++) {
                        String MP= in.GetMP(k+1);
                        if (!MP.equals("No MP"))
                          MPV.add(MP);
                      }
            		  int NOL= (new Integer(SelectedVars[2*m+1])).intValue();
        		      //System.out.println("Updating kbct: NOL="+NOL);
            		  in.SetLabelsNumber(NOL);
            		  if (NOL > 9) {
            			  in.SetScaleName("user");
            			  in.initMFLabelNames();
            			  in.InitLabelsName(NOL);
            		  }
                      if (in.GetV().GetType().equals("categorical")) {
                    	  in.SetInputPhysicalRange(1, in.GetLabelsNumber());
                    	  in.SetInputInterestRange(1, in.GetLabelsNumber());
                      }
                      in.SetLabelsName();
                      in.SetORLabelsName();
                      in.SetLabelProperties();
                      Enumeration en= MPV.elements();
                      int cont= 0;
                      while (en.hasMoreElements()) {
                          String MP = (String) en.nextElement();
                          double modal_point = (new Double(MP)).doubleValue();
                          for (int k=cont; k<NOL; k++) {
                            LabelKBCT label= in.GetLabel(k+1);
                            double[] Par= label.GetParams();
                            int NbPar= Par.length;
                            if ( ( (NbPar==3) && ( (Par[0] + Par[1])/2 <= modal_point) && ((Par[1] + Par[2])/2 >= modal_point)) ||
                          		  ( (NbPar==4) && ( (Par[0] + Par[1])/2 <= modal_point) && ((Par[2] + Par[3])/2 >= modal_point)) ) {
                                in.SetMP(k+1, MP, false);
                                cont= k+1;
                                break;
                            }
                          }
                      }
              	      System.out.println("Replace input "+in.GetPtr());
                      this.jef.Temp_kbct.ReplaceInput(in.GetPtr(), in);
              		  break;
                  }
             }
             if (!warning) {
            	 // Remove variable
   		         System.out.println("Updating kbct: removing input");
         	     System.out.println("Remove input "+n);
           	     this.jef.RemoveInput(n, 1);
             }
	    }
    	// Display results in JFISConsole
        if (this.jfcFS1!=null)
      	    this.jfcFS1.dispose();

   	    this.jfcFS1= new JFISConsole(this, wekaLOG, true);
        if (this.jfcFS2!=null)
	        this.jfcFS2.dispose();

        this.jfcFS2= new JFISConsole(this, wekaLOG+".FS.txt", true);
	    this.jfcFS2.setLocation(this.jfcFS2.getX()+this.jfcFS2.getInsets().top, this.jfcFS2.getY()+this.jfcFS2.getInsets().top);

    } catch( Throwable t ) {
    	t.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JMainFrame: jMenuDataFeatureSelection_actionPerformed: "+t);
    }
  }
//------------------------------------------------------------------------------
  private String[] readWekaTree(String fileLOG) throws Throwable {
	  String[] result= null;
      LineNumberReader lnrLOG= new LineNumberReader(new InputStreamReader(new FileInputStream(fileLOG)));
	  Vector names= new Vector();
      Hashtable h= new Hashtable();
      boolean end= false;
      String l;
      while( (!end) && ((l= lnrLOG.readLine())!=null) ) {
    	  if (l.startsWith("J48 pruned tree")) {
    		  lnrLOG.readLine();
    		  lnrLOG.readLine();
    		  while (!(l= lnrLOG.readLine()).equals("")) {
                String aux1=null;
                int ind=-1;
                if (l.contains("=")) {
                    ind= l.indexOf("=");
                    aux1= l.substring(0,ind-2);
                } else if (l.contains("<")) {
                    ind= l.indexOf("<");
                    aux1= l.substring(0,ind-1);
                } else if (l.contains(">")) {
                    ind= l.indexOf(">");
                    aux1= l.substring(0,ind-1);
                }
                while (aux1.contains("|")) {
                       aux1= aux1.substring(aux1.indexOf("|")+4);
                }
                Vector values= null;
                if (!h.containsKey(aux1)) {
                	names.add(aux1);
                    values= new Vector();
                    //System.out.println(aux1);
    		    } else {
                    values= (Vector)h.get(aux1);
    		    }
                String aux2=l.substring(ind+2);
                if (aux2.contains(":"))
                    aux2= aux2.substring(0,aux2.indexOf(":"));
                   
                if (!values.contains(aux2)) {
                    values.add(aux2);
                    h.put(aux1, values);
                }
                //System.out.println(aux2);
    		  }
 	      } else if (l.startsWith("Number of Leaves")) {
              end= true;
    	  }
      }
      lnrLOG.close();
      String fileLOG2= fileLOG+".FS.txt";
      PrintStream logOut= new PrintStream(new FileOutputStream(fileLOG2, false));
      Object[] objNames= names.toArray();
      result= new String[2*objNames.length];
      int c=0;
      for (int k=0; k<objNames.length; k++) {
      	   String key= (String)objNames[k];
      	   result[c++]= key;
           Vector v= (Vector)h.get(key);
           Object[] objValues= v.toArray();
           int lim=objValues.length;
      	   result[c++]= String.valueOf(lim+1);
           logOut.print("V"+String.valueOf(k+1)+": "+key+" ("+String.valueOf(lim+1)+") => ");
           //System.out.print("V"+String.valueOf(k+1)+": "+key+" ("+String.valueOf(lim+1)+") => ");
           for (int m=0; m<lim; m++) {
                if (m==lim-1) {
                    logOut.println(objValues[m]);
                    //System.out.println(objValues[m]);
                } else {
                    logOut.print(objValues[m]+", ");
                    //System.out.print(objValues[m]+", ");
                }
           }
      }
      logOut.flush();
      logOut.close(); 
      return result;
  }
//------------------------------------------------------------------------------
  void selectVariablesInDataFile() throws Throwable { //aquiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
    File aux= new File(JKBCTFrame.KBExpertFile);
    String auxName= aux.getName();
    String NewDataFile;
    if (auxName.endsWith(".kb.xml"))
        NewDataFile=this.KBDataFile+".data_"+auxName.substring(0,auxName.length()-7);
    else if (auxName.endsWith(".kb"))
        NewDataFile=this.KBDataFile+".data_"+auxName.substring(0,auxName.length()-3);
    else
        NewDataFile=this.KBDataFile+".data_"+auxName;

    //if (this.DataFileNoSaved==null) {
    	//System.out.println("JMainFrame: selectVariablesInDataFile: this.DataFileNoSaved==null");
    //} 
    this.DataFileNoSaved.Save(NewDataFile);
    this.KBDataFile= NewDataFile;
}
//------------------------------------------------------------------------------
  void jMenuSaveAs_actionPerformed() {
    try { this.SaveAs(); }
    catch( Throwable t ) {
      t.printStackTrace();
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JMainFrame en jMenuSaveAs_actionPerformed: "+t);
    }
  }
//------------------------------------------------------------------------------
// S�lo usado por MainKBCT.java para crear archivos IKB de forma autom�tica  
  public void jMenuSaveAs_actionPerformed(String fIKB) {
    try { this.SaveAs(fIKB); }
    catch( Throwable t ) {
      t.printStackTrace();
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JMainFrame en jMenuSaveAs_actionPerformed: "+t);
    }
  }
//------------------------------------------------------------------------------
  protected void setSMOTEparameters() {
    String Balancing= LocaleKBCT.GetString("No");
    if (MainKBCT.getConfig().GetSMOTEbalancing())
		Balancing= LocaleKBCT.GetString("Yes");

	String message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
        +"   "+LocaleKBCT.GetString("SMOTEnumberOfNeighbors")+"= "+MainKBCT.getConfig().GetSMOTEnumberOfNeighbors()+"\n"
        +"   "+LocaleKBCT.GetString("SMOTEtype")+"= "+MainKBCT.getConfig().GetSMOTEtype()+"\n"
        +"   "+LocaleKBCT.GetString("SMOTEbalancing")+"= "+Balancing+"\n"
        +"   ("+LocaleKBCT.GetString("All")+"= "+MainKBCT.getConfig().GetSMOTEbalancingALL()+")\n"
        +"   "+LocaleKBCT.GetString("SMOTEquantity")+"= "+MainKBCT.getConfig().GetSMOTEquantity()+"\n"
        +"   "+LocaleKBCT.GetString("SMOTEdistance")+"= "+MainKBCT.getConfig().GetSMOTEdistance()+"\n"
        +"   "+LocaleKBCT.GetString("SMOTEinterpolation")+"= "+MainKBCT.getConfig().GetSMOTEinterpolation()+"\n"
        +"   "+LocaleKBCT.GetString("SMOTEalpha")+"= "+MainKBCT.getConfig().GetSMOTEalpha()+"\n"
        +"   "+LocaleKBCT.GetString("SMOTEmu")+"= "+MainKBCT.getConfig().GetSMOTEmu()+"\n"+"\n"
        +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
        +LocaleKBCT.GetString("DoYouWantToChangeSelectedValues");
    int option= MessageKBCT.Confirm(this, message, 1, false, false, false);
    if (option==0) {
        this.changeSMOTEParameters();
    }
  }
//------------------------------------------------------------------------------
  private void setHFPparameters() {
      String Distance= MainKBCT.getConfig().GetDistance();
      String message= LocaleKBCT.GetString("HFPParameters")+"\n"+"\n"
                    + LocaleKBCT.GetString("Default_values")+":"+"\n"
                    +"   "+LocaleKBCT.GetString("Distance")+"= "+LocaleKBCT.GetString(Distance)+"\n"+"\n"
                    +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
                    +LocaleKBCT.GetString("DoYouWantToMakeChanges");
      int option= MessageKBCT.Confirm(this, message, 1, false, false, false);
      if (option==0) {
         new JHFPParametersFrame(this, null);
      } 
  }
//------------------------------------------------------------------------------
  void jMenuSetAutomaticConfiguration_actionPerformed() {
      int option= MessageKBCT.setAutomaticConfiguration(this);
	  if (option==0) {
		String SM= LocaleKBCT.GetString("No");
		if (MainKBCT.getConfig().GetSMOTE())
	        SM= LocaleKBCT.GetString("Yes");

	    String IP= LocaleKBCT.GetString("No");
		if (MainKBCT.getConfig().GetInducePartitions())
		    IP= LocaleKBCT.GetString("Yes");

		String PS= LocaleKBCT.GetString("No");
	    if (MainKBCT.getConfig().GetPartitionSelection())
		    PS= LocaleKBCT.GetString("Yes");

		String FS= LocaleKBCT.GetString("No");
	    if (MainKBCT.getConfig().GetFeatureSelection())
		    FS= LocaleKBCT.GetString("Yes");

	    String RI= LocaleKBCT.GetString("No");
	    if (MainKBCT.getConfig().GetRuleInduction())
		    RI= LocaleKBCT.GetString("Yes");

        String RRank= LocaleKBCT.GetString("No");
	    if (MainKBCT.getConfig().GetRuleRanking())
		    RRank= LocaleKBCT.GetString("Yes");

	    String Consistency= LocaleKBCT.GetString("No");
	    if (MainKBCT.getConfig().GetSolveLingConflicts())
		    Consistency= LocaleKBCT.GetString("Yes");

        String LVred= LocaleKBCT.GetString("No");
	    if (MainKBCT.getConfig().GetLVreduction())
		    LVred= LocaleKBCT.GetString("Yes");

	    String Simp= LocaleKBCT.GetString("No");
	    if (MainKBCT.getConfig().GetSimplify())
		    Simp= LocaleKBCT.GetString("Yes");

        String Optimize= LocaleKBCT.GetString("No");
	    if (MainKBCT.getConfig().GetOptimization())
	    	Optimize= LocaleKBCT.GetString("Yes");

        String Completeness= LocaleKBCT.GetString("No");
	    if (MainKBCT.getConfig().GetCompleteness())
	    	Completeness= LocaleKBCT.GetString("Yes");

        String Fingram= LocaleKBCT.GetString("No");
	    if (MainKBCT.getConfig().GetFingram())
	    	Fingram= LocaleKBCT.GetString("Yes");

	    String message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
        +"   "+LocaleKBCT.GetString("SMOTE")+"= "+SM+"\n"
        +"   "+LocaleKBCT.GetString("FeatureSelection")+"= "+FS+"\n"
        +"   "+LocaleKBCT.GetString("InducePartitions")+"= "+IP+"\n"
        +"   "+LocaleKBCT.GetString("PartitionSelection")+"= "+PS+"\n"
        +"   "+LocaleKBCT.GetString("RuleInduction")+"= "+RI+"\n"
        +"   "+LocaleKBCT.GetString("RuleRanking")+"= "+RRank+"\n"
        +"   "+LocaleKBCT.GetString("SolveLingConflicts")+"= "+Consistency+"\n"
        +"   "+LocaleKBCT.GetString("LVred")+"= "+LVred+"\n"
        +"   "+LocaleKBCT.GetString("Simple")+"= "+Simp+"\n"
        +"   "+LocaleKBCT.GetString("Optimization")+"= "+Optimize+"\n"
        +"   "+LocaleKBCT.GetString("Completeness")+"= "+Completeness+"\n"
        +"   "+LocaleKBCT.GetString("Fingrams")+"= "+Fingram+"\n"+"\n"
        +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
        +LocaleKBCT.GetString("DoYouWantToChangeSelectedValues");
        option= MessageKBCT.Confirm(this, message, 1, false, false, true);
        if (option==0) {
            option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeSMOTE"), 0, false, false, false);
            if (option==0) {
        	    MainKBCT.getConfig().SetSMOTE(true);
                this.setSMOTEparameters();
            } else {
        	    MainKBCT.getConfig().SetSMOTE(false);        	  
            }
            option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeFeatureSelection"), 0, false, false, false);
            if (option==0) {
        	    MainKBCT.getConfig().SetFeatureSelection(true);        	  
            } else {
        	    MainKBCT.getConfig().SetFeatureSelection(false);        	  
            }
            option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakePartitionInduction"), 0, false, false, false);
            if (option==0) {
        	    MainKBCT.getConfig().SetInducePartitions(true);
        	    Object obj= MessageKBCT.InductionType(this);
        	    if (obj != null) {
                    String opt= obj.toString();
            	    if (opt.equals("hfp")) {
            		    this.setHFPparameters();
            	    }
            	    MainKBCT.getConfig().SetInductionType(opt);
                    Object selNbLab= MessageKBCT.InductionNbLabels(this);
                    if (selNbLab!=null) {
                        int optNbLab= (new Integer(selNbLab.toString())).intValue();
            	        MainKBCT.getConfig().SetInductionNbLabels(optNbLab);
                    }
        	    }
            } else {
        	    MainKBCT.getConfig().SetInducePartitions(false);        	  
            }
            option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakePartitionSelection"), 0, false, false, false);
            if (option==0) {
        	   MainKBCT.getConfig().SetPartitionSelection(true);        	  
       		   this.setHFPparameters();
            } else {
        	    MainKBCT.getConfig().SetPartitionSelection(false);        	  
            }
            option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeRuleInduction"));
            if (option==0) {
        	  MainKBCT.getConfig().SetRuleInduction(true);
     	      option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeInductionWithDataSelection"));
              if (option==0) {
        	     MainKBCT.getConfig().SetDataSelection(true);
                 double ToleranceThreshold= MainKBCT.getConfig().GetTolThresDataSelection();
                 message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
                                 +"   "+LocaleKBCT.GetString("ToleranceThreshold")+"= "+ToleranceThreshold+"\n"+"\n"
                                 +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
                                 +LocaleKBCT.GetString("DoYouWantToChangeSelectedValues");
                 option= MessageKBCT.Confirm(this, message, 1, false, false, false);
                 if (option==0) {
                    this.setDataSelectionParameters();
                 }
              } else   
        	      MainKBCT.getConfig().SetDataSelection(false);
          
              Object SelectedAlgorithm= MessageKBCT.InduceRules(this, true);
              if (SelectedAlgorithm != null) {
                  MainKBCT.getConfig().SetInductionRulesAlgorithm((String)SelectedAlgorithm);
            	  MainKBCT.getConfig().SetClusteringSelection(false);
                  if (SelectedAlgorithm.equals("Fast Prototyping Algorithm")) {
            	      String strategy=MainKBCT.getConfig().GetStrategy();
            	      int strat=0;
            	      if (strategy.equals("decrease"))
            	    	strat=1;
            	    	
            	      int cardmin= MainKBCT.getConfig().GetMinCard();
            	      double matchmin= MainKBCT.getConfig().GetMinDeg();
                      this.SetFPAparameters(cardmin, matchmin, strat);
                  } else if (SelectedAlgorithm.equals("Fuzzy Decision Trees")) {
                      String Prune= LocaleKBCT.GetString("No");
                      if (MainKBCT.getConfig().GetPrune())
                	      Prune= LocaleKBCT.GetString("Yes");
                  
                      String Split= LocaleKBCT.GetString("No");
                      if (MainKBCT.getConfig().GetSplit())
                	      Split= LocaleKBCT.GetString("Yes");

                      String Display= LocaleKBCT.GetString("No");
                      if (MainKBCT.getConfig().GetDisplay())
                	      Display= LocaleKBCT.GetString("Yes");

                      message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
                                +"   "+LocaleKBCT.GetString("TreeFile")+"= "+MainKBCT.getConfig().GetTreeFile()+"\n"
                                +"   "+LocaleKBCT.GetString("MaxTreeDepth")+"= "+MainKBCT.getConfig().GetMaxTreeDepth()+"\n"
                                +"   "+LocaleKBCT.GetString("MinSignificantLevel")+"= "+MainKBCT.getConfig().GetMinSignificantLevel()+"\n"
                                +"   "+LocaleKBCT.GetString("LeafMinCard")+"= "+MainKBCT.getConfig().GetLeafMinCard()+"\n"
                                +"   "+LocaleKBCT.GetString("ToleranceThreshold")+"= "+MainKBCT.getConfig().GetToleranceThreshold()+"\n"
                                +"   "+LocaleKBCT.GetString("MinEDGain")+"= "+MainKBCT.getConfig().GetMinEDGain()+"\n"
                                +"   "+LocaleKBCT.GetString("CovThresh")+"= "+MainKBCT.getConfig().GetCovThresh()+"\n"
                                +"   "+LocaleKBCT.GetString("Relgain")+"= "+MainKBCT.getConfig().GetRelgain()+"\n"
                                +"   "+LocaleKBCT.GetString("Prune")+"= "+Prune+"\n"
                                +"   "+LocaleKBCT.GetString("Split")+"= "+Split+"\n"
                                +"   "+LocaleKBCT.GetString("Display")+"= "+Display+"\n"
                                +"   "+LocaleKBCT.GetString("RelPerfLoss")+"= "+MainKBCT.getConfig().GetPerfLoss()+"\n"+"\n"
                                +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
                                +LocaleKBCT.GetString("DoYouWantToChangeSelectedValues");
                      option= MessageKBCT.Confirm(this, message, 1, false, false, false);
                      if (option==0) {
                         new JTreeGenerateFrame(this);
                      }
                  } else if (SelectedAlgorithm.equals(LocaleKBCT.GetString("PrototypeRules"))) {
                	     MainKBCT.getConfig().SetClusteringSelection(true);
                         int ClustersNumber= MainKBCT.getConfig().GetClustersNumber();
                         message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
                                         +"   "+LocaleKBCT.GetString("ClustersNumber")+"= "+ClustersNumber+"\n"+"\n"
                                         +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
                                         +LocaleKBCT.GetString("DoYouWantToChangeSelectedValues");
                         option= MessageKBCT.Confirm(this, message, 1, false, false, false);
                         if (option==0) {
                            this.setClusterersNumber();
                         }
                         SelectedAlgorithm= MessageKBCT.InduceRules(this, false);
                         if (SelectedAlgorithm != null) {
                             MainKBCT.getConfig().SetInductionRulesAlgorithm((String)SelectedAlgorithm);
                             if (SelectedAlgorithm.equals("Fast Prototyping Algorithm")) {
                       	      String strategy=MainKBCT.getConfig().GetStrategy();
                       	      int strat=0;
                       	      if (strategy.equals("decrease"))
                       	    	strat=1;
                       	    	
                       	      int cardmin= MainKBCT.getConfig().GetMinCard();
                       	      double matchmin= MainKBCT.getConfig().GetMinDeg();
                                 this.SetFPAparameters(cardmin, matchmin, strat);
                             } else if (SelectedAlgorithm.equals("Fuzzy Decision Trees")) {
                                 String Prune= LocaleKBCT.GetString("No");
                                 if (MainKBCT.getConfig().GetPrune())
                           	      Prune= LocaleKBCT.GetString("Yes");
                             
                                 String Split= LocaleKBCT.GetString("No");
                                 if (MainKBCT.getConfig().GetSplit())
                           	      Split= LocaleKBCT.GetString("Yes");

                                 String Display= LocaleKBCT.GetString("No");
                                 if (MainKBCT.getConfig().GetDisplay())
                           	      Display= LocaleKBCT.GetString("Yes");

                                 message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
                                           +"   "+LocaleKBCT.GetString("TreeFile")+"= "+MainKBCT.getConfig().GetTreeFile()+"\n"
                                           +"   "+LocaleKBCT.GetString("MaxTreeDepth")+"= "+MainKBCT.getConfig().GetMaxTreeDepth()+"\n"
                                           +"   "+LocaleKBCT.GetString("MinSignificantLevel")+"= "+MainKBCT.getConfig().GetMinSignificantLevel()+"\n"
                                           +"   "+LocaleKBCT.GetString("LeafMinCard")+"= "+MainKBCT.getConfig().GetLeafMinCard()+"\n"
                                           +"   "+LocaleKBCT.GetString("ToleranceThreshold")+"= "+MainKBCT.getConfig().GetToleranceThreshold()+"\n"
                                           +"   "+LocaleKBCT.GetString("MinEDGain")+"= "+MainKBCT.getConfig().GetMinEDGain()+"\n"
                                           +"   "+LocaleKBCT.GetString("CovThresh")+"= "+MainKBCT.getConfig().GetCovThresh()+"\n"
                                           +"   "+LocaleKBCT.GetString("Relgain")+"= "+MainKBCT.getConfig().GetRelgain()+"\n"
                                           +"   "+LocaleKBCT.GetString("Prune")+"= "+Prune+"\n"
                                           +"   "+LocaleKBCT.GetString("Split")+"= "+Split+"\n"
                                           +"   "+LocaleKBCT.GetString("Display")+"= "+Display+"\n"
                                           +"   "+LocaleKBCT.GetString("RelPerfLoss")+"= "+MainKBCT.getConfig().GetPerfLoss()+"\n"+"\n"
                                           +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
                                           +LocaleKBCT.GetString("DoYouWantToChangeSelectedValues");
                                 option= MessageKBCT.Confirm(this, message, 1, false, false, false);
                                 if (option==0) {
                                    new JTreeGenerateFrame(this);
                                 }
                             }
                       }
                  } 
              }
            } else { 
        	    MainKBCT.getConfig().SetRuleInduction(false);
          	    MainKBCT.getConfig().SetClusteringSelection(false);
            }
          
     	    option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeRuleRanking"));
            if (option==0) {
        	    MainKBCT.getConfig().SetRuleRanking(true);
                Object SelectedOption= MessageKBCT.RankingOption(this);
                if (SelectedOption != null) {
                  if (SelectedOption.equals(LocaleKBCT.GetString("OrderByOutput")))
            	    MainKBCT.getConfig().SetOrderRulesByOutputClass(true);
                  else
            	    MainKBCT.getConfig().SetOrderRulesByOutputClass(false);

                  if (SelectedOption.equals(LocaleKBCT.GetString("OrderByLocalWeight")))
            	    MainKBCT.getConfig().SetOrderRulesByLocalWeight(true);
                  else
            	    MainKBCT.getConfig().SetOrderRulesByLocalWeight(false);

                  if (SelectedOption.equals(LocaleKBCT.GetString("OrderByGlobalWeight")))
            	    MainKBCT.getConfig().SetOrderRulesByGlobalWeight(true);
                  else
            	    MainKBCT.getConfig().SetOrderRulesByGlobalWeight(false);
                
                  if (SelectedOption.equals(LocaleKBCT.GetString("OrderByWeight")))
            	    MainKBCT.getConfig().SetOrderRulesByWeight(true);
                  else
            	    MainKBCT.getConfig().SetOrderRulesByWeight(false);

                  if (SelectedOption.equals(LocaleKBCT.GetString("OrderByLocalInterpretability")))
            	    MainKBCT.getConfig().SetOrderRulesByLocalIntWeight(true);
                  else
            	    MainKBCT.getConfig().SetOrderRulesByLocalIntWeight(false);

                  if (SelectedOption.equals(LocaleKBCT.GetString("OrderByGlobalInterpretability")))
            	    MainKBCT.getConfig().SetOrderRulesByGlobalIntWeight(true);
                  else
            	    MainKBCT.getConfig().SetOrderRulesByGlobalIntWeight(false);

                  if (SelectedOption.equals(LocaleKBCT.GetString("OrderByInterpretability")))
            	    MainKBCT.getConfig().SetOrderRulesByIntWeight(true);
                  else
            	    MainKBCT.getConfig().SetOrderRulesByIntWeight(false);

                  if (SelectedOption.equals(LocaleKBCT.GetString("OrderByNbPremises")))
            	    MainKBCT.getConfig().SetOrderRulesByNumberPremises(true);
                  else
            	    MainKBCT.getConfig().SetOrderRulesByNumberPremises(false);

                  if (SelectedOption.equals(LocaleKBCT.GetString("ReverseOrder")))
            	    MainKBCT.getConfig().SetReverseOrderRules(true);
                  else
            	    MainKBCT.getConfig().SetReverseOrderRules(false);

                  if (!SelectedOption.equals(LocaleKBCT.GetString("OrderByOutput"))) {
                    Object SelectedOutputClass;
                    // if (this.getKBCTTemp() != null) {
                    //    System.out.println("Temp Kbct");
                    //    SelectedOutputClass= MessageKBCT.SelectOutputClass(this, this.getKBCTTemp());
                    //} else 
                    if (this.kbct != null) {
                        //System.out.println("Kbct");
                        SelectedOutputClass= MessageKBCT.SelectOutputClass(this, this.kbct);
                    } else {
                        //System.out.println("NULL");
                        SelectedOutputClass=LocaleKBCT.GetString("all");
                        //SelectedOutputClass=LocaleKBCT.DefaultOutputClassSelected();
                        //MainKBCT.getConfig().SetOutputClassSelected(LocaleKBCT.DefaultOutputClassSelected());
                    }
                    //Object SelectedOutputClass= MessageKBCT.SelectOutputClass(this, this.getKBCTTemp());
                    //System.out.println("SelectedOutputClass.toString()="+SelectedOutputClass.toString());
                    MainKBCT.getConfig().SetOutputClassSelected(SelectedOutputClass.toString());
                  }
                }
            } else
        	    MainKBCT.getConfig().SetRuleRanking(false);

            option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeSolveLingConflicts"));
            if (option==0) {
        	    MainKBCT.getConfig().SetSolveLingConflicts(true);
            } else
        	    MainKBCT.getConfig().SetSolveLingConflicts(false);
        	  
            option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeLVreduction"));
            if (option==0) {
        	    MainKBCT.getConfig().SetLVreduction(true);
            } else
        	    MainKBCT.getConfig().SetLVreduction(false);

            option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeSimplification"));
     	    if (option==0) {
        	  MainKBCT.getConfig().SetSimplify(true);        	  

          	  String RR= LocaleKBCT.GetString("No");
          	  if (MainKBCT.getConfig().GetRuleRemoval())
          		  RR= LocaleKBCT.GetString("Yes");

              String VR= LocaleKBCT.GetString("No");
          	  if (MainKBCT.getConfig().GetVariableRemoval())
          		  VR= LocaleKBCT.GetString("Yes");

              String PR= LocaleKBCT.GetString("No");
          	  if (MainKBCT.getConfig().GetPremiseRemoval())
          		  PR= LocaleKBCT.GetString("Yes");

              String OR= LocaleKBCT.GetString("No");
              String opt= MainKBCT.getConfig().GetSimpRuleRanking();
          	  if (!opt.equals("false"))
          		  OR= LocaleKBCT.GetString("Yes")+" ["+LocaleKBCT.GetString(opt)+"]";

          	  String SP= LocaleKBCT.GetString("No");
          	  if (MainKBCT.getConfig().GetSelectedPerformance())
          		  SP= LocaleKBCT.GetString("Yes");

          	  String ORBS= LocaleKBCT.GetString("No");
          	  if (MainKBCT.getConfig().GetOnlyRBsimplification()) {
          		  ORBS= LocaleKBCT.GetString("Yes");
              	  String OCS= MainKBCT.getConfig().GetOutputClassSelected();
              	  ORBS= ORBS +"\n" +"   "+LocaleKBCT.GetString("OutputClassSelected")+"= "+ OCS;
          	  }

          	  String ODBS= LocaleKBCT.GetString("No");
          	  if (MainKBCT.getConfig().GetOnlyDBsimplification())
          		  ODBS= LocaleKBCT.GetString("Yes");

          	  message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
          	           +"   "+LocaleKBCT.GetString("SelectedOutput")+"= "+MainKBCT.getConfig().GetSelectedOutput()+"\n";

          	  if (MainKBCT.getConfig().GetFirstReduceRuleBase()) {
          	      message= message+"   "+LocaleKBCT.GetString("FirstReduceRuleBase")+"\n";
              } else {
          	      message= message+"   "+LocaleKBCT.GetString("FirstReduceDataBase")+"\n";
              }
          	  message= message +"   "+LocaleKBCT.GetString("OnlyDBsimplification")+"= "+ODBS+"\n"
              +"   "+LocaleKBCT.GetString("OnlyRBsimplification")+"= "+ORBS+"\n"
              +"   "+LocaleKBCT.GetString("MaximumLossOfCoverage")+"= "+MainKBCT.getConfig().GetMaximumLossOfCoverage()+" (%) \n"
              +"   "+LocaleKBCT.GetString("MaximumLossOfPerformance")+"= "+MainKBCT.getConfig().GetMaximumLossOfPerformance()+" (%) \n"
              +"   "+LocaleKBCT.GetString("MaximumNumberNewErrorCases")+"= "+MainKBCT.getConfig().GetMaximumNumberNewErrorCases()+"\n"
              +"   "+LocaleKBCT.GetString("MaximumNumberNewAmbiguityCases")+"= "+MainKBCT.getConfig().GetMaximumNumberNewAmbiguityCases()+"\n"
              +"   "+LocaleKBCT.GetString("MaximumNumberNewUnclassifiedCases")+"= "+MainKBCT.getConfig().GetMaximumNumberNewUnclassifiedCases()+"\n"
              +"   "+LocaleKBCT.GetString("RuleRemoval")+"= "+RR+"\n"
              +"   "+LocaleKBCT.GetString("VariableRemoval")+"= "+VR+"\n"
              +"   "+LocaleKBCT.GetString("PremiseRemoval")+"= "+PR+"\n"
              +"   "+LocaleKBCT.GetString("RuleRanking")+"= "+OR+"\n"
              +"   "+LocaleKBCT.GetString("SelectedPerformance")+"= "+SP+"\n"+"\n"
                    +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
                    +LocaleKBCT.GetString("DoYouWantToChangeSelectedValues");
          	  option= MessageKBCT.Confirm(this, message, 1, false, false, false);
              if (option==0)
                  new JSimpleFrame(this, this.kbct);
     	    } else
        	    MainKBCT.getConfig().SetSimplify(false);        	  

            option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeOptimization"));
     	    if (option==0) {
        	  MainKBCT.getConfig().SetOptimization(true);

      		  int optOpt= MessageKBCT.ChooseOptimization(this);
        	  MainKBCT.getConfig().SetOptOptimization(optOpt);
        	  if (optOpt==0) {
      		    int algorithm= MessageKBCT.PartitionOptimizationAlgorithm(this);
        	    MainKBCT.getConfig().SetOptAlgorithm(algorithm);
        		if (algorithm==0) {
        			// GeneticTuning
                      String BoundedOpt= LocaleKBCT.GetString("No");
                	  if (MainKBCT.getConfig().GetBoundedOptimization())
                		  BoundedOpt= LocaleKBCT.GetString("Yes");
                	  
        	          message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
                      +"   "+LocaleKBCT.GetString("NbGenerations")+"= "+MainKBCT.getConfig().GetNbGenerations()+"\n"
                      +"   "+LocaleKBCT.GetString("PopLength")+"= "+MainKBCT.getConfig().GetPopulationLength()+"\n"
                      +"   "+LocaleKBCT.GetString("TournamentSize")+"= "+MainKBCT.getConfig().GetTournamentSize()+"\n"
                      +"   "+LocaleKBCT.GetString("MutProb")+"= "+MainKBCT.getConfig().GetMutationProb()+"\n"
                      +"   "+LocaleKBCT.GetString("CrossoverProb")+"= "+MainKBCT.getConfig().GetCrossoverProb()+"\n"
                      +"   "+LocaleKBCT.GetString("ParAlfa")+"= "+MainKBCT.getConfig().GetParAlfa()+"\n"
                      +"   "+LocaleKBCT.GetString("BoundedOptimization")+"= "+BoundedOpt+"\n"
                      +"   "+LocaleKBCT.GetString("InitialGeneration")+"= "+MainKBCT.getConfig().GetInitialGeneration()+"\n"
                      +"   "+LocaleKBCT.GetString("MilestoneGeneration")+"= "+MainKBCT.getConfig().GetMilestoneGeneration()+"\n"+"\n"
                      +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
                      +LocaleKBCT.GetString("DoYouWantToChangeSelectedValues");

        	          option=1;
        	          if (!MainKBCT.getConfig().GetTESTautomatic())
        	              option= MessageKBCT.Confirm(this, message, 1, false, false, false);

                      if (option==0)
                          this.setOptimizationGTParameters();
        		} else if (algorithm==1) {
            	      // solisWetts (FisPro)
        	          String SWopt="VariableByVariable";
        	          if (MainKBCT.getConfig().GetSWoption()==2)
        	              SWopt= "LabelByLabel";

                      String BoundedOpt= LocaleKBCT.GetString("No");
                	  if (MainKBCT.getConfig().GetBoundedOptimization())
                		  BoundedOpt= LocaleKBCT.GetString("Yes");

                	  message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
                             +"   "+LocaleKBCT.GetString("SWoption")+"= "+LocaleKBCT.GetString(SWopt)+"\n"
                             +"   "+LocaleKBCT.GetString("NbIterations")+"= "+MainKBCT.getConfig().GetNbIterations()+"\n"
                             +"   "+LocaleKBCT.GetString("Cnear")+"= "+BoundedOpt+"\n"+"\n"
                             +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
                             +LocaleKBCT.GetString("DoYouWantToChangeSelectedValues");
                      option=1;
                      if (!MainKBCT.getConfig().GetTESTautomatic())
                          option= MessageKBCT.Confirm(this, message, 1, false, false, false);

                      if (option==0)
                          this.setOptimizationSWParameters();
        		} else
              	    MainKBCT.getConfig().SetOptimization(false);
     	      } else {
     	    	// Rule Selection
                  String IntIndex= "";
                  int iindex= MainKBCT.getConfig().GetRuleSelectionInterpretabilityIndex();
                  switch (iindex) {
                    // NR
                    case 0: IntIndex= LocaleKBCT.GetString("IshibuchiNbRules");
                            break;                        
                    // TRL
                    case 1: IntIndex= LocaleKBCT.GetString("IshibuchiTotalRuleLength");
                            break;
                    default: IntIndex= LocaleKBCT.GetString("IshibuchiTotalRuleLength");
                             break;
                  }
              	  message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
                              +"   "+LocaleKBCT.GetString("NbGenerations")+"= "+MainKBCT.getConfig().GetRuleSelectionNbGenerations()+"\n"
                              +"   "+LocaleKBCT.GetString("PopLength")+"= "+MainKBCT.getConfig().GetRuleSelectionPopulationLength()+"\n"
                              +"   "+LocaleKBCT.GetString("TournamentSize")+"= "+MainKBCT.getConfig().GetRuleSelectionTournamentSize()+"\n"
                              +"   "+LocaleKBCT.GetString("MutProb")+"= "+MainKBCT.getConfig().GetRuleSelectionMutationProb()+"\n"
                              +"   "+LocaleKBCT.GetString("CrossoverProb")+"= "+MainKBCT.getConfig().GetRuleSelectionCrossoverProb()+"\n"
                              +"   "+LocaleKBCT.GetString("W1ACC")+"= "+MainKBCT.getConfig().GetRuleSelectionW1()+"\n"
                              +"   "+LocaleKBCT.GetString("W2INT")+"= "+MainKBCT.getConfig().GetRuleSelectionW2()+"\n"
                              +"   "+LocaleKBCT.GetString("InterpretabilityIndex")+"= "+IntIndex+"\n"
                              +"   "+LocaleKBCT.GetString("InitialGeneration")+"= "+MainKBCT.getConfig().GetRuleSelectionInitialGeneration()+"\n"
                              +"   "+LocaleKBCT.GetString("MilestoneGeneration")+"= "+MainKBCT.getConfig().GetRuleSelectionMilestoneGeneration()+"\n"+"\n"
                              +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
                              +LocaleKBCT.GetString("DoYouWantToChangeSelectedValues");

    	          option=1;
    	          if (!MainKBCT.getConfig().GetTESTautomatic())
    	              option= MessageKBCT.Confirm(this, message, 1, false, false, false);

                  if (option==0)
                      this.setOptimizationGARSParameters();
     	      }
       		} else
          	    MainKBCT.getConfig().SetOptimization(false);

     	    option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeCompleteness"));
     	    if (option==0) {
        	    MainKBCT.getConfig().SetCompleteness(true);
          	    message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
                         +"   "+LocaleKBCT.GetString("CompletenessThres")+"= "+MainKBCT.getConfig().GetCompletenessThres()+"\n"+"\n"
                         +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
                         +LocaleKBCT.GetString("DoYouWantToChangeSelectedValues");

          	    option=1;
                if (!MainKBCT.getConfig().GetTESTautomatic())
                    option= MessageKBCT.Confirm(this, message, 1, false, false, false);

                if (option==0)
                    this.setCompletenessThreshold();

       		} else
          	    MainKBCT.getConfig().SetCompleteness(false);

     	    option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeFingrams"));
     	    //System.out.println("option -> "+option);
     	    if (option==0) {
     	    	MainKBCT.getConfig().SetFingram(true);
     	    	if ( (this.jef!=null) && (this.jef.Temp_kbct!=null) ) {
 	 	            this.jef.AssessingInterpretability(true);
 	        	    this.setPathFinderParameters((int)this.jef.jkbif.getMaxSFR(),this.jef.Temp_kbct.GetNbActiveRules());
     	    	} else {
     	    		this.setPathFinderParameters(1,1);
     	    	}
     	        //option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeFingramsWOS"));
     	        //if (option==0) {
     	    	    MainKBCT.getConfig().SetFingramWOS(true);
     	        //} else {
     	    	    //MainKBCT.getConfig().SetFingramWOS(false);
     	        //}
     	    	/*if ( (this.jef!=null) && (this.jef.Temp_kbct.GetOutput(1).GetClassif().equals("yes")) ) {
     	            option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeFingramsWS"));
     	            if (option==0) {
        	            MainKBCT.getConfig().SetFingramWS(true);
                        this.setSMOTEparameters();
       		        } else {
          	            MainKBCT.getConfig().SetFingramWS(false);
                    }
   		        } else {
      	            MainKBCT.getConfig().SetFingramWS(false);
                }*/
     	        //if ( (MainKBCT.getConfig().GetFingramWS()) || (MainKBCT.getConfig().GetFingramWOS()) ) {
     	    	      //MainKBCT.getConfig().SetFingram(true);
     	    	      Object m= MessageKBCT.SelectFingramsMetric(this);
     	    	      if (m!=null)
     	    		      MainKBCT.getConfig().SetFingramsMetric(m.toString());
     	    	      
     	    	      Object l= MessageKBCT.SelectFingramsLayout(this);
     	    	      if (l!=null)
     	    		      MainKBCT.getConfig().SetFingramsLayout(l.toString());

                      /*try {
	                      String lm= MessageKBCT.DataQuestion(this, LocaleKBCT.GetString("FingramsLimMaxIntIndex"));
	     	    	      if (lm!=null) {
	     	                  double limMax= Double.parseDouble(lm);
	     	    		      MainKBCT.getConfig().SetLimMaxIntIndex(limMax);
	     	    	      }
                      } catch (NumberFormatException nfe) {
                          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), nfe.toString());
                      }*/
     	        /*} else {
     	    	      MainKBCT.getConfig().SetFingram(false);
     	        }*/
     	        
     	    } else {
     	    	MainKBCT.getConfig().SetFingram(false);
     	    }
     	    
        } else if (option==2) {
            this.ResetDefaultConfigurationValues();  	  
        }
	  } else if (option==1) {
		  // Interpretability
		  this.setInterpretabilityParameter();
	  } else if (option==2) {
		  // Save current configuration parameters in a XML file
		  this.saveCurrentConfigParameters();
	  }
  }
//------------------------------------------------------------------------------
  void jMenuSynthesis_actionPerformed() {
	    try {
	    	String file_name_GUAJE=this.KBExpertFile;
	    	String file_name_XFUZZY=file_name_GUAJE+".xfl";
	    	//System.out.println("JMainFrame: jMenuSynthesis: GUAJE="+file_name_GUAJE+" -> XFUZZY="+file_name_XFUZZY);
	    	JConvert.GuajeToXfuzzy(file_name_GUAJE, file_name_XFUZZY);
	    	PkgParser pkgparser = new PkgParser();
	    	String xfuzzypath= MainKBCT.getConfig().GetKbctPath()+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"xfuzzy"+System.getProperty("file.separator");
	    	File path = new File(xfuzzypath);
	    	//File path = new File( System.getProperty("xfuzzypath") );
	    	//System.out.println("xfuzzypath -> "+path);
	    	pkgparser.addPath(new File(path,"pkg"));
	    	XflPackage newpkg= pkgparser.parse("xfl");
    	    if (newpkg==null) {
    	    	//System.out.println("error loading xfl package");
    	    	MessageKBCT.Error(this, "ERROR", "Error loading xfl package in xfuzzy for synthesis of java code.");
    	    } else {
    	    	Specification loaded= new Specification(new File(file_name_XFUZZY));
        	    if (loaded!=null) {
          	      File sdir= new File((new File(file_name_XFUZZY)).getParentFile(),"xfjava");
          	      sdir.mkdir();
  	    	      //System.out.println("sdir -> "+sdir.getAbsolutePath());
  	    	      Xfj compiler = new Xfj(loaded,sdir,"");
  	    	      if (compiler!=null) {
  	    	          //System.out.println("msg -> "+compiler.getMessage());
  	    	          MessageKBCT.Information(this, compiler.getMessage());
  	    	          pkgparser= null;
  	    	          newpkg= null;
  	    	          loaded= null;
  	    	          compiler= null;
  	    	          System.gc();
  	    	      } else {
  	        	      //System.out.println("error generating java code");
  	    	    	  MessageKBCT.Error(this, "ERROR", "Error generating java code by xfuzzy.");
  	    	      }
        	    } else {
        	    	System.out.println("error loading xfl file: "+file_name_XFUZZY);
	    	    	MessageKBCT.Error(this, "ERROR","Error loading xfl file by xfuzzy");
        	    }
    	    }
	    } catch( Throwable ex ) {
	        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JMainFrame en jMenuSynthesis_actionPerformed: "+ex);
		    ex.printStackTrace();
	    }
  }
//------------------------------------------------------------------------------
  /**
   * Save current configuration parameters in a XML file
   */
  public void saveCurrentConfigParameters() {
	  File f= new File(MainKBCT.getConfig().GetKbctPath()+System.getProperty("file.separator")+"config");
	  if (!f.exists())
	      f.mkdirs();
	  
	  File confFile= new File(f, "confFileParameters.xml");
      jnikbct.saveCurrentConfigParameters(confFile.getAbsolutePath());
  }
//------------------------------------------------------------------------------
  /**
   * set Comp parameter
   */
  public void setInterpretabilityParameter() {
      final JDialog jd = new JDialog(this);
  	  jd.setIconImage(LocaleKBCT.getIconGUAJE().getImage());
      jd.setTitle(LocaleKBCT.GetString("Interpretability"));
      jd.getContentPane().setLayout(new GridBagLayout());
      JPanel jPanelSaisie = new JPanel(new GridBagLayout());
      JPanel jPanelValidation = new JPanel(new GridBagLayout());
      JButton jButtonApply = new JButton(LocaleKBCT.GetString("Apply"));
      JButton jButtonCancel = new JButton(LocaleKBCT.GetString("Cancel"));
      JButton jButtonDefault = new JButton(LocaleKBCT.GetString("DefaultValues"));
      //JLabel jLabelCompInterpretability = new JLabel(LocaleKBCT.GetString("CompInterpretability") + " :");
      JLabel jLabelProblem = new JLabel(LocaleKBCT.GetString("Name") + " :");
      JLabel jLabelNbInputs = new JLabel(LocaleKBCT.GetString("NumberOfInputs") + " :");
      JLabel jLabelNbClasses = new JLabel(LocaleKBCT.GetString("NumberOfClasses") + " :");
      JLabel jLabelMaxNbRules = new JLabel(LocaleKBCT.GetString("MaxNbRules") + " :");
      JLabel jLabelMaxNbPremises = new JLabel(LocaleKBCT.GetString("MaxNbPremises") + " :");
      JLabel jLabelParameterL = new JLabel(LocaleKBCT.GetString("ParameterL") + " :");
      JLabel jLabelParameterM = new JLabel(LocaleKBCT.GetString("ParameterM") + " :");
      JLabel jLabelLimMaxIntIndex = new JLabel(LocaleKBCT.GetString("FingramsLimMaxIntIndex") + " :");
      //this.jCompInterpretability.setValue(MainKBCT.getConfig().GetCompInterpretability());
      jd.getContentPane().add(jPanelSaisie, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      //jPanelSaisie.add(jLabelCompInterpretability, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
      //  ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      //jPanelSaisie.add(this.jCompInterpretability, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
      //  ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
      jPanelSaisie.add(jLabelProblem, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      jPanelSaisie.add(this.jProblem, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
        ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
      this.jProblem.setText(MainKBCT.getConfig().GetProblem());
      jPanelSaisie.add(jLabelNbInputs, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
        ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      jPanelSaisie.add(this.jNbInputs, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
        ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
      this.jNbInputs.setValue(MainKBCT.getConfig().GetNumberOfInputs());
      jPanelSaisie.add(jLabelNbClasses, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
        ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      jPanelSaisie.add(this.jNbClasses, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
        ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
      this.jNbClasses.setValue(MainKBCT.getConfig().GetNumberOfOutputLabels());
      jPanelSaisie.add(jLabelMaxNbRules, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
        ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      jPanelSaisie.add(this.jMaxNbRules, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
        ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
      this.jMaxNbRules.setValue(MainKBCT.getConfig().GetMaxNbRules());
      jPanelSaisie.add(jLabelMaxNbPremises, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
        ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      jPanelSaisie.add(this.jMaxNbPremises, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
        ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
      this.jMaxNbPremises.setValue(MainKBCT.getConfig().GetMaxNbPremises());
      jPanelSaisie.add(jLabelParameterL, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
        ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      jPanelSaisie.add(this.jParameterL, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
        ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
      this.jParameterL.setValue(MainKBCT.getConfig().GetParameterL());
      jPanelSaisie.add(jLabelParameterM, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
        ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      jPanelSaisie.add(this.jParameterM, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
        ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
      this.jParameterM.setValue(MainKBCT.getConfig().GetParameterM());
      jPanelSaisie.add(jLabelLimMaxIntIndex, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
    	        ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      jPanelSaisie.add(this.jLimMaxIntIndex, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
        ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
      this.jLimMaxIntIndex.setValue(MainKBCT.getConfig().GetLimMaxIntIndex());
      jd.getContentPane().add(jPanelValidation, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
      jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      jPanelValidation.add(jButtonCancel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      jPanelValidation.add(jButtonDefault, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      jButtonApply.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
        //double value= JMainFrame.this.jCompInterpretability.getValue();
        int valueI= JMainFrame.this.jNbInputs.getValue();
        int valueC= JMainFrame.this.jNbClasses.getValue();
        int valueR= JMainFrame.this.jMaxNbRules.getValue();
        int valueP= JMainFrame.this.jMaxNbPremises.getValue();
        double valueL= JMainFrame.this.jParameterL.getValue();
        double valueM= JMainFrame.this.jParameterM.getValue();
        int valueLimMAX= JMainFrame.this.jLimMaxIntIndex.getValue();
        if (valueI < 1) {
            MessageKBCT.Information(JMainFrame.this, LocaleKBCT.GetString("NbInputsShouldBe"));
        } else if (valueC < 2) {
            MessageKBCT.Information(JMainFrame.this, LocaleKBCT.GetString("NbClassesShouldBe"));
        } else if (valueR < valueC) {
            MessageKBCT.Information(JMainFrame.this, LocaleKBCT.GetString("MaxNbRulesShouldBe"));
        } else if (valueP < valueR) {
            MessageKBCT.Information(JMainFrame.this, LocaleKBCT.GetString("MaxNbPremisesShouldBe"));
        } else if ( (valueL < 0) || (valueL > 100) || (valueM < 0) || (valueM > 100) ) {
            MessageKBCT.Information(JMainFrame.this, LocaleKBCT.GetString("ParameterLMShouldBe1"));
        } else if (valueL >= valueM) {
            MessageKBCT.Information(JMainFrame.this, LocaleKBCT.GetString("ParameterLMShouldBe2"));
        } else if (valueLimMAX <= 0) {
            MessageKBCT.Information(JMainFrame.this, LocaleKBCT.GetString("FingramsLimMaxIntIndexBe"));
        } else {
      	  //MainKBCT.getConfig().SetCompInterpretability(value);
      	  MainKBCT.getConfig().SetParameterL(valueL);
      	  MainKBCT.getConfig().SetParameterM(valueM);
      	  MainKBCT.getConfig().SetMaxNbRules(valueR);
      	  MainKBCT.getConfig().SetMaxNbPremises(valueP);
      	  MainKBCT.getConfig().SetNumberOfOutputLabels(valueC);
      	  MainKBCT.getConfig().SetNumberOfInputs(valueI);
      	  MainKBCT.getConfig().SetLimMaxIntIndex(valueLimMAX);
          jd.dispose();
        }
      } } );
      jButtonCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
        jd.dispose();
      } } );
      jButtonDefault.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
    	  //JMainFrame.this.jCompInterpretability.setValue(LocaleKBCT.DefaultCompInterpretability());
    	  JMainFrame.this.jParameterL.setValue(LocaleKBCT.DefaultParameterL());
    	  JMainFrame.this.jParameterM.setValue(LocaleKBCT.DefaultParameterM());
    	  JMainFrame.this.jMaxNbRules.setValue(LocaleKBCT.DefaultMaxNbRules());
    	  JMainFrame.this.jMaxNbPremises.setValue(LocaleKBCT.DefaultMaxNbPremises());
    	  JMainFrame.this.jNbInputs.setValue(LocaleKBCT.DefaultNumberOfInputs());
    	  JMainFrame.this.jNbClasses.setValue(LocaleKBCT.DefaultNumberOfOutputLabels());
    	  JMainFrame.this.jLimMaxIntIndex.setValue(LocaleKBCT.DefaultLimMaxIntIndex());
      } } );
      jd.setResizable(false);
      jd.setModal(true);
      jd.pack();
      jd.setLocation(JChildFrame.ChildPosition(this, jd.getSize()));
      jd.setVisible(true);
  }
//------------------------------------------------------------------------------
  void ResetDefaultConfigurationValues() {
      MainKBCT.getConfig().SetTESTautomatic(LocaleKBCT.DefaultTESTautomatic());
      MainKBCT.getConfig().SetINFERautomatic(LocaleKBCT.DefaultINFERautomatic());
	  MainKBCT.getConfig().SetSMOTE(LocaleKBCT.DefaultSMOTE());
	  MainKBCT.getConfig().SetSMOTEnumberOfNeighbors(LocaleKBCT.DefaultSMOTEnumberOfNeighbors());
	  MainKBCT.getConfig().SetSMOTEtype(LocaleKBCT.DefaultSMOTEtype());
	  MainKBCT.getConfig().SetSMOTEbalancing(LocaleKBCT.DefaultSMOTEbalancing());
	  MainKBCT.getConfig().SetSMOTEbalancingALL(LocaleKBCT.DefaultSMOTEbalancingALL());
	  MainKBCT.getConfig().SetSMOTEquantity(LocaleKBCT.DefaultSMOTEquantity());
	  MainKBCT.getConfig().SetSMOTEdistance(LocaleKBCT.DefaultSMOTEdistance());
	  MainKBCT.getConfig().SetSMOTEinterpolation(LocaleKBCT.DefaultSMOTEinterpolation());
	  MainKBCT.getConfig().SetSMOTEalpha(LocaleKBCT.DefaultSMOTEalpha());
	  MainKBCT.getConfig().SetSMOTEmu(LocaleKBCT.DefaultSMOTEmu());
	  MainKBCT.getConfig().SetInducePartitions(LocaleKBCT.DefaultInducePartitions());
	  MainKBCT.getConfig().SetInductionType(LocaleKBCT.DefaultInductionType());
	  MainKBCT.getConfig().SetInductionNbLabels(LocaleKBCT.DefaultInductionNbLabels());
	  MainKBCT.getConfig().SetPartitionSelection(LocaleKBCT.DefaultPartitionSelection());
	  MainKBCT.getConfig().SetFeatureSelection(LocaleKBCT.DefaultFeatureSelection());
	  MainKBCT.getConfig().SetDataSelection(LocaleKBCT.DefaultDataSelection());
	  MainKBCT.getConfig().SetDistance(LocaleKBCT.DefaultDistance());
	  MainKBCT.getConfig().SetClusteringSelection(LocaleKBCT.DefaultClusteringSelection());
	  MainKBCT.getConfig().SetClustersNumber(LocaleKBCT.DefaultClustersNumber());
	  MainKBCT.getConfig().SetRuleInduction(LocaleKBCT.DefaultRuleInduction());
	  MainKBCT.getConfig().SetInductionRulesAlgorithm(LocaleKBCT.DefaultInductionRulesAlgorithm());
	  MainKBCT.getConfig().SetStrategy(LocaleKBCT.DefaultStrategy());
	  MainKBCT.getConfig().SetMinCard(LocaleKBCT.DefaultMinCard());
	  MainKBCT.getConfig().SetMinDeg(LocaleKBCT.DefaultMinDeg());
	  MainKBCT.getConfig().SetTreeFile(LocaleKBCT.DefaultTreeFile());
      MainKBCT.getConfig().SetMaxTreeDepth(LocaleKBCT.DefaultMaxTreeDepth());
      MainKBCT.getConfig().SetMinSignificantLevel(LocaleKBCT.DefaultMinSignificantLevel());
      MainKBCT.getConfig().SetLeafMinCard(LocaleKBCT.DefaultLeafMinCard());
      MainKBCT.getConfig().SetToleranceThreshold(LocaleKBCT.DefaultToleranceThreshold());
      MainKBCT.getConfig().SetMinEDGain(LocaleKBCT.DefaultMinEDGain());
      MainKBCT.getConfig().SetCovThresh(LocaleKBCT.DefaultCovThresh());
      MainKBCT.getConfig().SetRelgain(LocaleKBCT.DefaultRelgain());
      MainKBCT.getConfig().SetPrune(LocaleKBCT.DefaultPrune());
      MainKBCT.getConfig().SetSplit(LocaleKBCT.DefaultSplit());
      MainKBCT.getConfig().SetDisplay(LocaleKBCT.DefaultDisplay());
      MainKBCT.getConfig().SetPerfLoss(LocaleKBCT.DefaultPerfLoss());
      MainKBCT.getConfig().SetSolveLingConflicts(LocaleKBCT.DefaultSolveLingConflicts());
	  MainKBCT.getConfig().SetRuleRanking(LocaleKBCT.DefaultRuleRanking());
	  MainKBCT.getConfig().SetOrderRulesByOutputClass(LocaleKBCT.DefaultOrderRulesByOutputClass());
	  MainKBCT.getConfig().SetOrderRulesByWeight(LocaleKBCT.DefaultOrderRulesByWeight());
	  MainKBCT.getConfig().SetOrderRulesByLocalIntWeight(LocaleKBCT.DefaultOrderRulesByLocalIntWeight());
	  MainKBCT.getConfig().SetOrderRulesByGlobalIntWeight(LocaleKBCT.DefaultOrderRulesByGlobalIntWeight());
	  MainKBCT.getConfig().SetOrderRulesByIntWeight(LocaleKBCT.DefaultOrderRulesByIntWeight());
	  MainKBCT.getConfig().SetOrderRulesByNumberPremises(LocaleKBCT.DefaultOrderRulesByNumberPremises());
	  MainKBCT.getConfig().SetReverseOrderRules(LocaleKBCT.DefaultReverseOrderRules());
	  MainKBCT.getConfig().SetLVreduction(LocaleKBCT.DefaultLVreduction());        	  
	  MainKBCT.getConfig().SetSimplify(LocaleKBCT.DefaultSimplify());        	  
	  MainKBCT.getConfig().SetFirstReduceRuleBase(LocaleKBCT.DefaultFirstReduceRuleBase());        	  
      MainKBCT.getConfig().SetSelectedOutput(LocaleKBCT.DefaultSelectedOutput());
      MainKBCT.getConfig().SetOnlyDBsimplification(LocaleKBCT.DefaultOnlyDBsimplification());
      MainKBCT.getConfig().SetOnlyRBsimplification(LocaleKBCT.DefaultOnlyRBsimplification());
      MainKBCT.getConfig().SetOutputClassSelected(LocaleKBCT.DefaultOutputClassSelected());
      MainKBCT.getConfig().SetMaximumLossOfCoverage(LocaleKBCT.DefaultMaximumLossOfCoverage());
      MainKBCT.getConfig().SetMaximumLossOfPerformance(LocaleKBCT.DefaultMaximumLossOfPerformance());
      MainKBCT.getConfig().SetMaximumNumberNewErrorCases(LocaleKBCT.DefaultMaximumNumberNewErrorCases());
      MainKBCT.getConfig().SetMaximumNumberNewAmbiguityCases(LocaleKBCT.DefaultMaximumNumberNewAmbiguityCases());
      MainKBCT.getConfig().SetMaximumNumberNewUnclassifiedCases(LocaleKBCT.DefaultMaximumNumberNewUnclassifiedCases());
      MainKBCT.getConfig().SetRuleRemoval(LocaleKBCT.DefaultRuleRemoval());
      MainKBCT.getConfig().SetVariableRemoval(LocaleKBCT.DefaultVariableRemoval());
      MainKBCT.getConfig().SetPremiseRemoval(LocaleKBCT.DefaultPremiseRemoval());
	  MainKBCT.getConfig().SetSimpRuleRanking(LocaleKBCT.DefaultSimpRuleRanking());
      MainKBCT.getConfig().SetSelectedPerformance(LocaleKBCT.DefaultSelectedPerformance());
      MainKBCT.getConfig().SetOrderRulesByWeight(LocaleKBCT.DefaultOrderRulesByWeight());
	  MainKBCT.getConfig().SetOptimization(LocaleKBCT.DefaultOptimization());        	  
	  MainKBCT.getConfig().SetOptAlgorithm(LocaleKBCT.DefaultOptAlgorithm());        	  
	  MainKBCT.getConfig().SetRuleSelectionNbGenerations(LocaleKBCT.DefaultRuleSelectionNbGenerations());
 	  MainKBCT.getConfig().SetRuleSelectionInitialGeneration(LocaleKBCT.DefaultRuleSelectionInitialGeneration());
 	  MainKBCT.getConfig().SetRuleSelectionMilestoneGeneration(LocaleKBCT.DefaultRuleSelectionMilestoneGeneration());
	  MainKBCT.getConfig().SetRuleSelectionPopulationLength(LocaleKBCT.DefaultRuleSelectionPopulationLength());
	  MainKBCT.getConfig().SetRuleSelectionTournamentSize(LocaleKBCT.DefaultRuleSelectionTournamentSize());
	  MainKBCT.getConfig().SetRuleSelectionMutationProb(LocaleKBCT.DefaultRuleSelectionMutationProb());
	  MainKBCT.getConfig().SetRuleSelectionCrossoverProb(LocaleKBCT.DefaultRuleSelectionCrossoverProb());
	  MainKBCT.getConfig().SetRuleSelectionW1(LocaleKBCT.DefaultRuleSelectionW1());
	  MainKBCT.getConfig().SetRuleSelectionW2(LocaleKBCT.DefaultRuleSelectionW2());
	  MainKBCT.getConfig().SetRuleSelectionInterpretabilityIndex(LocaleKBCT.DefaultRuleSelectionInterpretabilityIndex());
	  MainKBCT.getConfig().SetBoundedOptimization(LocaleKBCT.DefaultBoundedOptimization());        	  
	  MainKBCT.getConfig().SetSWoption(LocaleKBCT.DefaultSWoption());        	  
	  MainKBCT.getConfig().SetNbIterations(LocaleKBCT.DefaultNbIterations());        	  
	  MainKBCT.getConfig().SetNbGenerations(LocaleKBCT.DefaultNbGenerations());        	  
 	  MainKBCT.getConfig().SetInitialGeneration(LocaleKBCT.DefaultInitialGeneration());
 	  MainKBCT.getConfig().SetMilestoneGeneration(LocaleKBCT.DefaultMilestoneGeneration());
	  MainKBCT.getConfig().SetPopulationLength(LocaleKBCT.DefaultPopulationLength());        	  
	  MainKBCT.getConfig().SetCrossoverOperator(LocaleKBCT.DefaultCrossoverOperator());        	  
	  MainKBCT.getConfig().SetCrossoverProb(LocaleKBCT.DefaultCrossoverProb());        	  
	  MainKBCT.getConfig().SetMutationProb(LocaleKBCT.DefaultMutationProb());        	  
	  MainKBCT.getConfig().SetTournamentSize(LocaleKBCT.DefaultTournamentSize());        	  
	  MainKBCT.getConfig().SetSeed(LocaleKBCT.DefaultSeed());        	  
	  MainKBCT.getConfig().SetParAlfa(LocaleKBCT.DefaultParAlfa());        	  
	  MainKBCT.getConfig().SetCompleteness(LocaleKBCT.DefaultCompleteness());        	  
	  MainKBCT.getConfig().SetCompletenessThres(LocaleKBCT.DefaultCompletenessThres());        	  
	  MainKBCT.getConfig().SetFingram(LocaleKBCT.DefaultFingram());        	  
	  MainKBCT.getConfig().SetFingramWS(LocaleKBCT.DefaultFingramWS());        	  
	  MainKBCT.getConfig().SetFingramWOS(LocaleKBCT.DefaultFingramWOS());        	  
	  MainKBCT.getConfig().SetLimMaxIntIndex(LocaleKBCT.DefaultLimMaxIntIndex());        	  
	  MainKBCT.getConfig().SetFingramsSelectedSample(LocaleKBCT.DefaultFingramsSelectedSample());        	  
	  MainKBCT.getConfig().SetFingramsMetric(LocaleKBCT.DefaultFingramsMetric());        	  
	  MainKBCT.getConfig().SetFingramsLayout(LocaleKBCT.DefaultFingramsLayout());        	  
	  MainKBCT.getConfig().SetPathFinderThreshold(LocaleKBCT.DefaultPathFinderThreshold());        	  
	  MainKBCT.getConfig().SetPathFinderParQ(LocaleKBCT.DefaultPathFinderParQ());        	  
	  MainKBCT.getConfig().SetGoodnessHighThreshold(LocaleKBCT.DefaultGoodnessHighThreshold());        	  
	  MainKBCT.getConfig().SetGoodnessLowThreshold(LocaleKBCT.DefaultGoodnessLowThreshold());        	  
	  // interpretability
	  MainKBCT.getConfig().SetProblem(LocaleKBCT.DefaultProblem());        	  
	  MainKBCT.getConfig().SetNumberOfInputs(LocaleKBCT.DefaultNumberOfInputs());        	  
	  MainKBCT.getConfig().SetNumberOfOutputLabels(LocaleKBCT.DefaultNumberOfOutputLabels());        	  
      MainKBCT.getConfig().SetParameterL(LocaleKBCT.DefaultParameterL());        	  
	  MainKBCT.getConfig().SetParameterM(LocaleKBCT.DefaultParameterM());        	  
	  MainKBCT.getConfig().SetMaxNbRules(LocaleKBCT.DefaultMaxNbRules());        	  
	  MainKBCT.getConfig().SetMaxNbPremises(LocaleKBCT.DefaultMaxNbPremises());
	  // accuracy
	  MainKBCT.getConfig().SetBlankThres(LocaleKBCT.DefaultBlankThres());
	  MainKBCT.getConfig().SetAmbThres(LocaleKBCT.DefaultAmbThres());
  }
//------------------------------------------------------------------------------
  void jSetFISoptionsButton_actionPerformed() {
      if (this.jef!=null)
    	  JConvert.SetFISoptions(this.jef.Temp_kbct, this);
      else
          JConvert.SetFISoptions(this.kbct, this);
  }
//------------------------------------------------------------------------------
  void jGenerateFISButton_actionPerformed() {
    try {
     JConvertFrame jcf= new JConvertFrame(this);
     jcf.setVisible(true);
    } catch( Throwable ex ) {
    	//ex.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JMainFrame en jGenerateFISButton_actionPerformed: "+ex);
    }
  }
//------------------------------------------------------------------------------
  void jExpertButton_actionPerformed() {
      String TempKBCTFile= null;
      if (this.jef != null) {
          try {
            if (this.jef.Temp_kbct!=null) {
              if (this.jef.Temp_kbct.GetKBCTFile()==null) {
                if (JKBCTFrame.KBExpertFile.equals(""))
                	JKBCTFrame.KBExpertFile= this.jTFExpertName.getText();

                this.jef.Temp_kbct.SetKBCTFile(JKBCTFrame.KBExpertFile);
              }
              this.jef.Temp_kbct.Save();
            }
          } catch (Throwable t) {
              MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JMainFrame en jExpertButton_actionPerformed: "+t);
          }
          TempKBCTFile= this.jef.Temp_kbct.GetKBCTFile();
          this.jef.disposeOpen();
      }
      if (!JKBCTFrame.KBExpertFile.equals("") && (new File(JKBCTFrame.KBExpertFile)).exists()) {
          if (TempKBCTFile != null)
              this.jef= new JExpertFrame(JKBCTFrame.KBExpertFile, this, TempKBCTFile);
          else
              this.jef= new JExpertFrame(JKBCTFrame.KBExpertFile, this, null);
      } else {
          this.jef= new JExpertFrame(this);
          this.jef.jMenuNew_actionPerformed();
      }
      if (this.kbct_Data != null)
        this.jef.kbct_Data= new JKBCT(this.kbct_Data);

      this.kbct= this.jef.Temp_kbct;
      if (this.kbct!=null) {
        if ( (this.kbct.GetNbInputs()>0) & (this.kbct.GetNbOutputs()>0) )
            this.jef.ReInitTableRules();
      }
      this.jef.setVisible(true);
  }
//------------------------------------------------------------------------------
  void jDataButton_actionPerformed() {
    int a= MessageKBCT.InduceData(this);
    if (a==0)
      this.jMenuDataInducePartitions_actionPerformed(false, false, true);
    else if (a==1)
      this.jMenuDataInduceRules_actionPerformed(false, false);
    else if (a==2) {
      this.jMenuDataBuildKB_actionPerformed(null);
    }
  }
//------------------------------------------------------------------------------
  public void jMenuExit_actionPerformed() { this.dispose(); }
//------------------------------------------------------------------------------
  private void OpenDataFile1() throws Throwable {
    LineNumberReader lnr= new LineNumberReader(new InputStreamReader(new FileInputStream(new File(this.DFile))));
    String line= lnr.readLine();
    String VariableNames= line;
    File file= new File(this.DFile);
    if (line.startsWith("VariableNames")) {
      this.DataFileLabels= true;
      VariableNames=line.substring(14,line.length());
      file = new File(this.DFile+".aux");
      PrintWriter fOut = new PrintWriter(new FileOutputStream(file), true);
      while((line= lnr.readLine()) != null)
        fOut.println(line);

      fOut.flush();
      fOut.close();
      lnr.close();
      this.DFile= file.getPath();
      // apertura del fichero de datos
      JExtendedDataFile new_data_file = new JExtendedDataFile(file.getPath(), true);
      this.DataNbVariables= new_data_file.VariableCount();
      new_data_file.Close();
      this.VNames= new String[this.DataNbVariables];
      int[] VNamesAux= new int[this.DataNbVariables];
      int cont=0;
      for (int k=0; k<VariableNames.length(); k++) {
        if (VariableNames.charAt(k)==',')
          VNamesAux[cont++] = k;
      }
      VNamesAux[cont]= VariableNames.length();
      for (int k=0; k<this.DataNbVariables; k++) {
          if (k==0)
            this.VNames[k]= VariableNames.substring(0, VNamesAux[k]);
          else
            this.VNames[k]= VariableNames.substring(VNamesAux[k-1]+1, VNamesAux[k]);
      }
    } else {
      JExtendedDataFile new_data_file = new JExtendedDataFile(file.getPath(), true);
      this.DataNbVariables= new_data_file.VariableCount();
      new_data_file.Close();
      this.VNames= new String[this.DataNbVariables];
      for (int k=0; k<this.DataNbVariables; k++)
         this.VNames[k]= LocaleKBCT.GetString("Variable") + String.valueOf(k + 1);
    }
    int option=1;
	if ( (!MainKBCT.getConfig().GetTESTautomatic()) && (!MainKBCT.getConfig().GetTutorialFlag()))
        option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToSpecifyVariablesToUse")+"\n"+LocaleKBCT.GetString("DefaultAllUsed"), 1, false, false, false);

	if (option==0) {
      this.SelectDataFromDataFile();
    } else {
      this.variables= new Vector();
      this.OLD_variables= new Vector();
      for (int n=0; n<this.DataNbVariables; n++) {
        this.variables.add(new Integer(n));
        this.OLD_variables.add(new Integer(n));
      }
      this.OpenDataFile2();
    }
  }
//------------------------------------------------------------------------------
  private void SelectDataFromDataFile() {
    if ( (!JKBCTFrame.KBExpertFile.equals("")) && (this.jef.Temp_kbct != null) ) {
      boolean warning= false;
      int DataVar= this.DataNbVariables;
      int NbIn= this.jef.Temp_kbct.GetNbInputs();
      int NbOut= this.jef.Temp_kbct.GetNbOutputs();
      Integer[] vars= new Integer[NbIn+NbOut];
      for (int i=0; i<NbIn; i++) {
        String msg= LocaleKBCT.GetString("Select_Data_link_to_Var")+" "+this.jef.Temp_kbct.GetInput(i+1).GetName();
        Object aux= MessageKBCT.SelectDataVar(this, msg, DataVar);
        if (aux==null) {
          warning= true;
          break;
        } else {
            vars[i]= new Integer(((Integer)aux).intValue()-1);
        }
      }
      if (! warning) {
        for (int i=0; i<NbOut; i++) {
          String msg= LocaleKBCT.GetString("Select_Data_link_to_Var")+" "+this.jef.Temp_kbct.GetOutput(i+1).GetName();
          Integer opt= new Integer(((Integer)MessageKBCT.SelectDataVar(this, msg, DataVar)).intValue()-1);
          if (opt==null) {
            warning= true;
            break;
          } else
              vars[i+NbIn]= opt;
        }
      }
      if (! warning) {
        try {
          this.variables= new Vector();
          this.OLD_variables= new Vector();
          for( int i=0 ; i<vars.length ; i++ ) {
              this.variables.add(vars[i]);
              this.OLD_variables.add(vars[i]);
          }
          this.SaveDataFile1();
          this.OpenDataFile2();
        } catch( Throwable t ) {
            t.printStackTrace();
            MessageKBCT.Error(null, t);
        }
      }
    } else {
      final JDialog jd = new JDialog(this);
      jd.setTitle(LocaleKBCT.GetString("DataChooser"));
      jd.getContentPane().setLayout(new GridBagLayout());
      JScrollPane jScrollPanelVariables= new JScrollPane();
      jScrollPanelVariables.setLayout(new ScrollPaneLayout());
      TitledBorder titledBorderVariables= new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),LocaleKBCT.GetString("Variables"));
      jScrollPanelVariables.setBorder(titledBorderVariables);
      JPanel jPanelVariables= new JPanel();
      jPanelVariables.setLayout(new GridBagLayout());
      JButton jButtonAll= new JButton();
      this.CheckBoxs= new Vector();
      jButtonAll.setText(LocaleKBCT.GetString("All"));
      jButtonAll.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          for( int i=0 ; i<JMainFrame.this.CheckBoxs.size() ; i++ )
            ((JCheckBox)JMainFrame.this.CheckBoxs.elementAt(i)).setSelected(true);
        } });
      jd.getContentPane().add(jScrollPanelVariables, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 0, 0), 0, 0));
      jPanelVariables.add(jButtonAll, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 5, 10), 0, 0));
      for( int i=0 ; i<VNames.length ; i++ ) {
          JCheckBox cb = new JCheckBox(VNames[i]);
          this.CheckBoxs.add(cb);
          jPanelVariables.add(cb, new GridBagConstraints(0, i+1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      }
      jScrollPanelVariables.getViewport().add(jPanelVariables, null);
      // panel validation
      JPanel jPanelValidation= new JPanel();
      jPanelValidation.setLayout(new GridBagLayout());
      JButton jButtonApply= new JButton();
      jButtonApply.setText(LocaleKBCT.GetString("Apply"));
      jButtonApply.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
           try {
             JMainFrame.this.variables= new Vector();
             JMainFrame.this.OLD_variables= new Vector();
             for( int i=0 ; i<JMainFrame.this.CheckBoxs.size() ; i++ )
               if( ((JCheckBox)JMainFrame.this.CheckBoxs.elementAt(i)).isSelected() == true ) {
                 JMainFrame.this.variables.add(new Integer(i));
                 JMainFrame.this.OLD_variables.add(new Integer(i));
               }
             JMainFrame.this.SaveDataFile1();
             JMainFrame.this.OpenDataFile2();
             jd.dispose();
           } catch( Throwable t ) { MessageKBCT.Error(null, t); }
        } });
      JButton jButtonCancel= new JButton();
      jButtonCancel.setText(LocaleKBCT.GetString("Cancel"));
      jButtonCancel.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              JMainFrame.this.variables= new Vector();
              JMainFrame.this.OLD_variables= new Vector();
              for (int n=0; n<JMainFrame.this.DataNbVariables; n++) {
                JMainFrame.this.variables.add(new Integer(n));
                JMainFrame.this.OLD_variables.add(new Integer(n));
              }
             JMainFrame.this.OpenDataFile2();
             jd.dispose();
         } });
      jd.getContentPane().add(jPanelValidation, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
      jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      jPanelValidation.add(jButtonCancel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      jd.setModal(true);
      jd.pack();
      jd.setLocation(JChildFrame.ChildPosition(this, jd.getSize()));
      jd.setVisible(true);
    }
  }
//------------------------------------------------------------------------------
  private void OpenDataFile2() {
    try {
    	//System.out.println("JMF: OpenDataFile2(): 1");
      JExtendedDataFile new_data_file = new JExtendedDataFile(this.DFile, true);
      this.DataNbVariables = new_data_file.VariableCount();

      int NbOutputs=-1;
      if ( (this.jef != null) && (this.jef.Temp_kbct != null) && (this.DataNbVariables > this.jef.Temp_kbct.GetNbInputs() + this.jef.Temp_kbct.GetNbOutputs()) ) {
      	//System.out.println("JMF: OpenDataFile2(): 2");
         MessageKBCT.Information(this, LocaleKBCT.GetString("TheKBCTFile")+" "+LocaleKBCT.GetString("has")+" "+this.jef.Temp_kbct.GetNbInputs()+" "+LocaleKBCT.GetString("Inputs").toLowerCase()+" "+LocaleKBCT.GetString("AND").toLowerCase()+" "+this.kbct.GetNbOutputs()+" "+LocaleKBCT.GetString("Outputs").toLowerCase()+"\n"+LocaleKBCT.GetString("You_must_open_valid_data_file_with_less_than")+" "+String.valueOf(this.kbct.GetNbInputs()+this.jef.Temp_kbct.GetNbOutputs()+1)+" "+LocaleKBCT.GetString("Variables")+".");
         int option=1;
     	 if ( (!MainKBCT.getConfig().GetTESTautomatic()) && (!MainKBCT.getConfig().GetTutorialFlag()) )
             option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToSpecifyVariablesToUse")+"\n"+LocaleKBCT.GetString("DefaultAllUsed"), 1, false, false, false);
         if (option==0) {
           this.SelectDataFromDataFile();
         }
      } else {
      	//System.out.println("JMF: OpenDataFile2(): 3");
        if (this.DataNbVariables > 0) {
          if ( (this.jef != null) && (this.jef.Temp_kbct != null) ) {
        	  // KB open
        	  NbOutputs= this.jef.Temp_kbct.GetNbOutputs();
          } else {
              String aux="1";
          	 if ( (!MainKBCT.getConfig().GetTESTautomatic()) && (!MainKBCT.getConfig().GetTutorialFlag()) )
                  aux = MessageKBCT.DataQuestion(this, LocaleKBCT.GetString("Data_file_has") + " " + this.DataNbVariables + " " + LocaleKBCT.GetString("Variables") + ".\n" + LocaleKBCT.GetString("How_many_variables"));

        	  if (aux != null && !aux.equals("null"))
                  NbOutputs = Integer.parseInt(aux);
              else {
                  MessageKBCT.Error(this, LocaleKBCT.GetString("OpenDataKB"), LocaleKBCT.GetString("IncorrectNbOutputs"));
                  this.ResetKBDataFile();
                  this.InitJKBCTFrame();
                  return;
              }
              while ( (NbOutputs > this.DataNbVariables) || (NbOutputs <= 0)) {
                  if (NbOutputs == 0)
                    if (MessageKBCT.Confirm(this, LocaleKBCT.GetString("If_NbOutput_equal_zero_Not_Induced_Rules") + "\n" + LocaleKBCT.GetString("Confirm_NbOutputs_equal_zero"), 0, false, false, false) == JOptionPane.YES_OPTION)
                       break;
                    else
                       aux = MessageKBCT.DataQuestion(this, LocaleKBCT.GetString("Introduce_valid_number_of_outputs") + "\n" + LocaleKBCT.GetString("Data_file_has") + " " + this.DataNbVariables + " " + LocaleKBCT.GetString("Variables") + ".\n" + LocaleKBCT.GetString("How_many_variables"));

                  if (aux != null && !aux.equals("null"))
                    NbOutputs = Integer.parseInt(aux);
                  else {
                    MessageKBCT.Error(this, LocaleKBCT.GetString("OpenDataKB"), LocaleKBCT.GetString("IncorrectNbOutputs"));
                    this.ResetKBDataFile();
                    this.InitJKBCTFrame();
                    return;
                  }
              }
          }
      }
      if ( (this.jef != null) && (this.jef.Temp_kbct != null) && ( (this.DataNbVariables-NbOutputs > this.jef.Temp_kbct.GetNbInputs()) || (NbOutputs > this.jef.Temp_kbct.GetNbOutputs()) ) ) {
      	//System.out.println("JMF: OpenDataFile2(): 4");
          if (this.jef.Temp_kbct.GetNbInputs()>1)
              if (this.jef.Temp_kbct.GetNbOutputs()>1)
                 MessageKBCT.Information(this, LocaleKBCT.GetString("TheKBCTFile")+" "+LocaleKBCT.GetString("has")+" "+this.kbct.GetNbInputs()+" "+LocaleKBCT.GetString("Inputs").toLowerCase()+" "+LocaleKBCT.GetString("AND").toLowerCase()+" "+this.kbct.GetNbOutputs()+" "+LocaleKBCT.GetString("Outputs").toLowerCase()+"\n"+LocaleKBCT.GetString("You_must_open_valid_data_file_with_less_than")+" "+String.valueOf(this.kbct.GetNbInputs()+1)+" "+LocaleKBCT.GetString("Inputs").toLowerCase()+" "+LocaleKBCT.GetString("AND").toLowerCase()+" "+String.valueOf(this.kbct.GetNbOutputs()+1)+" "+LocaleKBCT.GetString("Outputs").toLowerCase()+".");
              else
                 MessageKBCT.Information(this, LocaleKBCT.GetString("TheKBCTFile")+" "+LocaleKBCT.GetString("has")+" "+this.kbct.GetNbInputs()+" "+LocaleKBCT.GetString("Inputs").toLowerCase()+" "+LocaleKBCT.GetString("AND").toLowerCase()+" "+this.kbct.GetNbOutputs()+" "+LocaleKBCT.GetString("Output").toLowerCase()+"\n"+LocaleKBCT.GetString("You_must_open_valid_data_file_with_less_than")+" "+String.valueOf(this.kbct.GetNbInputs()+1)+" "+LocaleKBCT.GetString("Inputs").toLowerCase()+" "+LocaleKBCT.GetString("AND").toLowerCase()+" "+String.valueOf(this.kbct.GetNbOutputs()+1)+" "+LocaleKBCT.GetString("Outputs").toLowerCase()+".");
          else
              if (this.jef.Temp_kbct.GetNbOutputs()>1)
                 MessageKBCT.Information(this, LocaleKBCT.GetString("TheKBCTFile")+" "+LocaleKBCT.GetString("has")+" "+this.kbct.GetNbInputs()+" "+LocaleKBCT.GetString("Input").toLowerCase()+" "+LocaleKBCT.GetString("AND").toLowerCase()+" "+this.kbct.GetNbOutputs()+" "+LocaleKBCT.GetString("Outputs").toLowerCase()+"\n"+LocaleKBCT.GetString("You_must_open_valid_data_file_with_less_than")+" "+String.valueOf(this.kbct.GetNbInputs()+1)+" "+LocaleKBCT.GetString("Inputs").toLowerCase()+" "+LocaleKBCT.GetString("AND").toLowerCase()+" "+String.valueOf(this.kbct.GetNbOutputs()+1)+" "+LocaleKBCT.GetString("Outputs").toLowerCase()+".");
              else
                 MessageKBCT.Information(this, LocaleKBCT.GetString("TheKBCTFile")+" "+LocaleKBCT.GetString("has")+" "+this.kbct.GetNbInputs()+" "+LocaleKBCT.GetString("Input").toLowerCase()+" "+LocaleKBCT.GetString("AND").toLowerCase()+" "+this.kbct.GetNbOutputs()+" "+LocaleKBCT.GetString("Output").toLowerCase()+"\n"+LocaleKBCT.GetString("You_must_open_valid_data_file_with_less_than")+" "+String.valueOf(this.kbct.GetNbInputs()+1)+" "+LocaleKBCT.GetString("Inputs").toLowerCase()+" "+LocaleKBCT.GetString("AND").toLowerCase()+" "+String.valueOf(this.kbct.GetNbOutputs()+1)+" "+LocaleKBCT.GetString("Outputs").toLowerCase()+".");

          MessageKBCT.Error(this, LocaleKBCT.GetString("OpenDataKB"), LocaleKBCT.GetString("IncorrectNbOutputsOrNbInputs"));
          this.ResetKBDataFile();
          this.InitJKBCTFrame();
          return;
       }
       //System.out.println("JMF: Generate KB from data");
       this.kbct_Data= new JKBCT(this.GenerateJKBCTData(this.DFile, NbOutputs, this.variables, this.VNames));
       // sustituir el fichero actual de datos
       if (this.DataFile != null)
           this.DataFile.Close();

       this.DataFile = new_data_file;
       this.KBDataFile = this.DataFile.FileName();
       this.jMenuDataOpen.setEnabled(false);
       this.jMenuDataClose.setEnabled(true);
       this.jMenuDataView.setEnabled(true);
       this.jMenuDataTable.setEnabled(true);
       if (!JKBCTFrame.KBExpertFile.equals(""))
            this.jDataButton.setEnabled(true);

       if (this.jef != null) {
            this.jef.kbct_Data= new JKBCT(this.kbct_Data);
            if (this.jef.jif!=null)
              this.jef.jif.data_file= this.DataFile;
       }
       if (this.kbct == null) {
       	//System.out.println("JMF: OpenDataFile2(): 6");
            this.kbct = new JKBCT(this.kbct_Data);
            int l= this.KBDataFile.lastIndexOf('.');
            if( l == -1 )
            	JKBCTFrame.KBExpertFile = this.KBDataFile + "."+("KB.XML").toLowerCase();
            else
            	JKBCTFrame.KBExpertFile = this.KBDataFile.substring(0,l) + "."+("KB.XML").toLowerCase();

            this.kbct.SetKBCTFile(JKBCTFrame.KBExpertFile);
            if (!JKBCTFrame.KBExpertFile.equals("") && (new File(JKBCTFrame.KBExpertFile)).exists())
                this.jef = new JExpertFrame(JKBCTFrame.KBExpertFile, this, null);
            else
                this.jef = new JExpertFrame(this);

            this.jef.setVisible(false);
            this.jef.kbct= this.kbct;
            this.jef.Temp_kbct= this.kbct;
            this.jef.kbct_Data= new JKBCT(this.kbct_Data);
            File f = new File(JKBCTFrame.KBExpertFile);
            if (f.exists() && !(MainKBCT.getConfig().GetTESTautomatic()) && !(MainKBCT.getConfig().GetTutorialFlag())) {
                String message = LocaleKBCT.GetString("TheKBCTFile") + " : " + JKBCTFrame.KBExpertFile + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");
                if (MessageKBCT.Confirm(this, message, 1, false, false, false) == JOptionPane.NO_OPTION)
                     this.jef.jMenuSaveAs_actionPerformed();
                else
                     this.jef.Temp_kbct.Save(JKBCTFrame.KBExpertFile, false);
            } else
                this.jef.Temp_kbct.Save(JKBCTFrame.KBExpertFile, false);

            String[] OutputType= new String[NbOutputs];
            for (int n=0; n<NbOutputs; n++)
              OutputType[n]= this.kbct.GetOutput(n+1).GetType();

            JConvert.SetFISoptionsDefault(NbOutputs, OutputType);
            this.oldConjunction=JConvert.conjunction;
            this.oldDisjunction= new String[NbOutputs];
            this.oldDefuzzification= new String[NbOutputs];
            for (int n=0; n<NbOutputs; n++) {
              this.oldDisjunction[n]= JConvert.disjunction[n];
              this.oldDefuzzification[n]= JConvert.defuzzification[n];
            }
            this.jMenuExpertNew.setEnabled(false);
            this.jMenuExpertOpen.setEnabled(false);
            this.jMenuExpertClose.setEnabled(true);
            this.jExpertButton.setEnabled(true);
            // Save default ikb.xml file
        	//System.out.println("JMF: OpenDataFile2(): 7");
            if ( (this.IKBFile==null) || (this.IKBFile.equals("")) ) {
            	//System.out.println("JMF: OpenDataFile2(): 8");
                this.IKBFile= JKBCTFrame.KBExpertFile.replace(".kb.xml",".ikb.xml");
                //System.out.println("New IKB file= "+this.IKBFile);
                this.jMenuSave_actionPerformed();
            } //else {
            	//System.out.println("JMF: OpenDataFile2(): 9: "+this.IKBFile);
            //}
         }
         this.InitJKBCTFrameWithKBCT();
       }
     } catch (NumberFormatException nfe) {
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("Valid_number_of_outputs")+" -> "+nfe);
          this.DataNbVariables=0;
     } catch (Throwable ex) {
    	  ex.printStackTrace();
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JMainFrame en OpenDataFile2: "+ex);
     }
  }
//------------------------------------------------------------------------------
  private JKBCT GenerateJKBCTData(String DataFile, int NbOutputs, Vector var, String[] VarNames ) throws Throwable {
    JExtendedDataFile data_file = new JExtendedDataFile(DataFile, true);
    int NbVariables = data_file.VariableCount();
    JKBCT KBCTData = new JKBCT();
    for (int n = 0; n < NbVariables; n++) {
         double[] d = data_file.VariableData(n);
         double max = d[0];
         double min = d[0];
         for (int m = 1; m < d.length; m++) {
              if (max < d[m])
                  max = d[m];
              if (min > d[m])
                  min = d[m];
         }
         variable v = new variable();
         if (var!=null)
             v.SetName(VarNames[((Integer)var.get(n)).intValue()]);
         else
             v.SetName(VarNames[n]);

         v.SetInputPhysicalRange(min, max);
         v.SetInputInterestRange(min, max);
         double[] NbUniqValues = jnikbct.InitUniq(d);
         //System.out.println("JMF: n="+n+"  min="+min+"  max="+max+"  NbUV="+NbUniqValues[0]);
         if (NbUniqValues[0] == 2) {
             v.SetType("logical");
             v.SetLabelsNumber(2);
             v.InitLabelsName(2);
             v.SetLabelsName();
             v.SetLabelProperties();
             v.SetScaleName("user");
             for (int k=1; k<= NbUniqValues[0]; k++) {
                  v.SetMP(k,""+NbUniqValues[k],true);
                  v.SetUserLabelsName(k,""+NbUniqValues[k]);
             }
         } else if ( (NbUniqValues[0] > 2) && (NbUniqValues[0] < 10) ) {
             v.SetType("categorical");
             v.SetLabelsNumber((int)NbUniqValues[0]);
             v.InitLabelsName((int)NbUniqValues[0]);
             v.SetLabelsName();
             v.SetLabelProperties();
             v.SetScaleName("user");
             for (int k=1; k<= NbUniqValues[0]; k++)  {
                 v.SetMP(k,""+NbUniqValues[k],true);
                 v.SetUserLabelsName(k,""+NbUniqValues[k]);
             }
         } else {
             v.SetType("numerical");
             v.SetLabelProperties();
         }
         if (n < NbVariables - NbOutputs) {
             JKBCTInput new_input = new JKBCTInput(v, KBCTData.GetNbInputs() + 1);
             KBCTData.AddInput(new_input);
         } else {
             JKBCTOutput new_output = new JKBCTOutput(v, KBCTData.GetNbOutputs() + 1);
             KBCTData.AddOutput(new_output);
         }
       }
       return KBCTData;
  }
//------------------------------------------------------------------------------
  private void SaveDataFile1() throws Throwable {
    File new_dfile= new File(this.DFile+".data");
    if (new_dfile.exists()) {
        String message = LocaleKBCT.GetString("TheDataFile") + " : " + new_dfile.getPath() + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");
        if (MessageKBCT.Confirm(this, message, 1, false, false, false) == JOptionPane.NO_OPTION)
             this.SaveDataAs(new_dfile.getPath());
        else
             this.SaveDataFile2(new_dfile.getPath());
    } else
      this.SaveDataFile2(new_dfile.getPath());
  }
//------------------------------------------------------------------------------
  private void SaveDataFile2(String file) throws Throwable {
    File new_dfile= new File(file);
    PrintWriter fOut = new PrintWriter(new FileOutputStream(new_dfile.getPath()), true);
    JExtendedDataFile new_data_file = new JExtendedDataFile(this.DFile, true);
    int NbLines = new_data_file.VariableData(0).length;
    for (int k = 0; k < NbLines; k++) {
        for (int n = 0; n < this.variables.size(); n++)
          if (n == this.variables.size() - 1)
            fOut.println(new_data_file.VariableData(((Integer)this.variables.get(n)).intValue())[k]);
          else
            fOut.print(new_data_file.VariableData(((Integer)this.variables.get(n)).intValue())[k] + ",");
    }
    fOut.flush();
    fOut.close();
    this.DFile= new_dfile.getPath();
  }
//------------------------------------------------------------------------------
  protected void UpdateDataFile(int var, String type, int mode) throws Throwable {
	//System.out.println("JMainFrame: UpdateDataFile: var="+var+"  type="+type);
    DecimalFormat df= new DecimalFormat();
    df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
    dfs.setDecimalSeparator((new String(".").charAt(0)));
    df.setDecimalFormatSymbols(dfs);
    df.setGroupingSize(20);
    if (this.DataFileNoSaved==null) {
      this.DFile= this.DataFile.GetFileName();
      File f= new File(this.DFile);
      File temp= JKBCTFrame.BuildFile("temp"+f.getName());
      if (temp.exists())
        temp.delete();

      temp.deleteOnExit();
      this.DFileNoSaved= temp.getAbsolutePath();
      PrintWriter fOut = new PrintWriter(new FileOutputStream(temp, true));
      LineNumberReader lnr= new LineNumberReader(new InputStreamReader(new FileInputStream(this.DFile)));
      String l;
      while((l= lnr.readLine())!=null)
        fOut.println(l);

      fOut.flush();
      fOut.close();
      this.DataFileNoSaved= new JExtendedDataFile(this.DFileNoSaved, true);
      if (mode==0) {
        String msg= LocaleKBCT.GetString("BuildTempDataFile")
                    +": "+this.DataFileNoSaved.GetFileName()
                    +"\n"+LocaleKBCT.GetString("MsgTempDataFile");
        MessageKBCT.Information(this.jef, msg);
      }
    } else {
      if (mode==0) {
        String msg= LocaleKBCT.GetString("ModifyTempDataFile")
                    +": "+this.DataFileNoSaved.GetFileName()
                    +"\n"+LocaleKBCT.GetString("MsgTempDataFile");
        MessageKBCT.Information(this.jef, msg);
      }
    }
	//System.out.println("JMainFrame: UpdateDataFile: 1: file="+this.DFileNoSaved);
    PrintWriter fOut = new PrintWriter(new FileOutputStream(this.DFileNoSaved), true);
    int NbLines = this.DataFileNoSaved.VariableData(0).length;
    int NbCol= this.DataFileNoSaved.VariableCount();
	//System.out.println("JMainFrame: UpdateDataFile: 2: NbLines="+NbLines+"  NbCol="+NbCol);
    for (int k = 0; k < NbLines; k++) {
        for (int n = 0; n < NbCol; n++) {
          if (type.equals("UP")) {
              if (n == var-1)
                fOut.print(df.format(this.DataFileNoSaved.VariableData(var)[k]));
              else if (n == var)
                fOut.print(df.format(this.DataFileNoSaved.VariableData(var-1)[k]));
              else
                fOut.print(df.format(this.DataFileNoSaved.VariableData(n)[k]));
          } else if (type.equals("DOWN")) {
              if (n == var)
                fOut.print(df.format(this.DataFileNoSaved.VariableData(var+1)[k]));
              else if (n == var+1)
                fOut.print(df.format(this.DataFileNoSaved.VariableData(var)[k]));
              else
                fOut.print(df.format(this.DataFileNoSaved.VariableData(n)[k]));
          } else if ( (type.equals("REMOVE")) && (n < NbCol-1) ) {
              if (n >= var)
                fOut.print(df.format(this.DataFileNoSaved.VariableData(n+1)[k]));
              else
                fOut.print(df.format(this.DataFileNoSaved.VariableData(n)[k]));
          }
          if (type.equals("REMOVE")) {
              if (n==NbCol-2)
                fOut.println();
              else if (n<NbCol-1)
                fOut.print(",");
          } else {
              if (n==NbCol-1)
                fOut.println();
              else
                fOut.print(",");
          }
        }
    }
    fOut.flush();
    fOut.close();
	//System.out.println("JMainFrame: UpdateDataFile: 3");
    this.DataFileNoSaved= new JExtendedDataFile(this.DFileNoSaved, true);
	//System.out.println("JMainFrame: UpdateDataFile: 4: NbLines="+this.DataFileNoSaved.VariableData(0).length+"  NbCol="+this.DataFileNoSaved.VariableCount());
  }
//------------------------------------------------------------------------------
  private boolean Open() throws Throwable {
	  boolean warning= false;
	  if (this.IKBFile.endsWith("xml")) {
		  warning= this.OpenXML();
	  } else {
		  this.OpenPlain();
		  this.IKBFile= this.IKBFile+".xml";
		  this.jTFcontextName.setText(MainKBCT.getConfig().GetProblem());
		  this.jTFcontextNumberInputs.setText(""+MainKBCT.getConfig().GetNumberOfInputs());
		  this.jTFcontextNumberClasses.setText(""+MainKBCT.getConfig().GetNumberOfOutputLabels());
		  this.SaveAs(this.IKBFile);
	  }
	  return warning;
  }
//------------------------------------------------------------------------------
  private void OpenPlain() throws Throwable {
    LineNumberReader lnr= new LineNumberReader(new InputStreamReader(new FileInputStream(new File(this.IKBFile))));
    String l;
    boolean warning1=false;
    boolean warning2=false;
    while((l= lnr.readLine())!=null) {
      if (l.equals("[Expert]")) {
        l = lnr.readLine();
        if (l.length()>13)
        	JKBCTFrame.KBExpertFile= l.substring(12, l.length() - 1);
        else
        	JKBCTFrame.KBExpertFile= "";

        this.oldKBExpertFile= JKBCTFrame.KBExpertFile;
        if (!JKBCTFrame.KBExpertFile.equals("") && (new File(JKBCTFrame.KBExpertFile)).exists()){
            this.jef= new JExpertFrame(JKBCTFrame.KBExpertFile, this, null);
            this.jef.setVisible(false);
            if (!LocaleKBCT.isWindowsPlatform())
            	this.jef.dispose();
            
            this.kbct= this.jef.Temp_kbct;
        }
        if (this.kbct!=null) {
           l = lnr.readLine();
           String conjunction= l.substring(13, l.length() - 1);
           int NbOutputs= this.kbct.GetNbOutputs();
           String[] disjunction= null;
           String[] defuzzification= null;
           if (NbOutputs >0) {
             disjunction= new String[NbOutputs];
             defuzzification= new String[NbOutputs];
             for (int n=0; n<NbOutputs; n++) {
               lnr.readLine();
               l= lnr.readLine();
               if (l.startsWith("[Output")) {
                 String disj, defuzz;
                 l = lnr.readLine();
                 disj= l.substring(13, l.length() - 1);
                 l = lnr.readLine();
                 defuzz= l.substring(17, l.length() - 1);
                 disjunction[n]= disj;
                 defuzzification[n]= defuzz;
               } else
                  break;
             }
           }
           JConvert.SetFISoptions(conjunction, disjunction, defuzzification);
           this.oldConjunction= conjunction;
           if (NbOutputs >0) {
             this.oldDisjunction= new String[NbOutputs];
             this.oldDefuzzification= new String[NbOutputs];
             for (int n=0; n<NbOutputs; n++) {
               this.oldDisjunction[n]= JConvert.disjunction[n];
               this.oldDefuzzification[n]= JConvert.defuzzification[n];
             }
           }
        }
      } else if (l.equals("[Data]")) {
        l = lnr.readLine();
        if (l.length()>15)
            this.OrigKBDataFile= l.substring(14, l.length() - 1);
        else
            this.OrigKBDataFile= "";

        l = lnr.readLine();
        if (l.substring(15).equals("'yes'"))
          this.DataFileLabels= true;
        else
          this.DataFileLabels= false;

        l = lnr.readLine();
        if (l.length()>20)
            this.SelectedVariablesDataFile= l.substring(19, l.length() - 1);
        else
            this.SelectedVariablesDataFile= "";

        l = lnr.readLine();
        if (l.length()>11)
            this.KBDataFile= l.substring(10, l.length() - 1);
        else
            this.KBDataFile= "";

        this.oldKBDataFile= this.KBDataFile;
        if (!this.KBDataFile.equals(""))
          this.DataFile= new JExtendedDataFile(this.KBDataFile, true);

        l = lnr.readLine();
        this.DataNbVariables= Integer.parseInt(l.substring(18));
        if (this.DataNbVariables > 0){
          this.kbct_Data = new JKBCT();
          if (!this.SelectedVariablesDataFile.equals("")) {
            this.variables= new Vector();
            this.OLD_variables= new Vector();
            int index= 0;
            String aux= null;
            String value= null;
            for (int i=0; i<this.DataNbVariables; i++) {
                if (i==0) {
                  index= this.SelectedVariablesDataFile.indexOf(",");
                  aux= this.SelectedVariablesDataFile.substring(index+2);
                  value= this.SelectedVariablesDataFile.substring(0, index);
                } else {
                  if (i < this.DataNbVariables-1) {
                    index= aux.indexOf(",");
                    value= aux.substring(0, index);
                    aux = aux.substring(index+2);
                  } else
                    value= aux;
                }
                this.variables.add(new Integer(value));
                this.OLD_variables.add(new Integer(value));
            }
        }
      }
    } else if (l.startsWith("[Input")) {
      variable v = new variable();
      l = lnr.readLine();
      v.SetName(l.substring(6, l.length() - 1));
      l = lnr.readLine();
      v.SetType(l.substring(6, l.length() - 1));
      l = lnr.readLine();
      int indR= l.indexOf(",");
      double LowerR= (new Double(l.substring(10, indR))).doubleValue();
      v.SetLowerPhysicalRange(LowerR);
      v.SetLowerInterestRange(LowerR);
      double UpperR= (new Double(l.substring(indR+2, l.length()-1))).doubleValue();
      v.SetUpperPhysicalRange(UpperR);
      v.SetUpperInterestRange(UpperR);
      l = lnr.readLine();
      int LabelsNumber=Integer.parseInt(l.substring(17));
      v.SetLabelsNumber(LabelsNumber);
      v.InitLabelsName(LabelsNumber);
      v.SetLabelsName();
      v.SetLabelProperties();
      JKBCTInput new_input = new JKBCTInput(v, this.kbct_Data.GetNbInputs()+1);
      this.kbct_Data.AddInput(new_input);
    } else if ( (l.startsWith("[Output")) && (this.kbct_Data != null) ) {
      variable v = new variable();
      l = lnr.readLine();
      v.SetName(l.substring(6, l.length() - 1));
      l = lnr.readLine();
      v.SetType(l.substring(6, l.length() - 1));
      l = lnr.readLine();
      int indR= l.indexOf(",");
      double LowerR= (new Double(l.substring(10, indR))).doubleValue();
      v.SetLowerPhysicalRange(LowerR);
      v.SetLowerInterestRange(LowerR);
      double UpperR= (new Double(l.substring(indR+2, l.length()-1))).doubleValue();
      v.SetUpperPhysicalRange(UpperR);
      v.SetUpperInterestRange(UpperR);
      l = lnr.readLine();
      int LabelsNumber=Integer.parseInt(l.substring(17));
      v.SetLabelsNumber(LabelsNumber);
      v.InitLabelsName(LabelsNumber);
      v.SetLabelsName();
      v.SetLabelProperties();
      JKBCTOutput new_output = new JKBCTOutput(v, this.kbct_Data.GetNbOutputs()+1);
      this.kbct_Data.AddOutput(new_output);
    } else if (l.equals("[Context]")) {
        l = lnr.readLine();
        String prob=l.substring(9,l.length()-1);
    	MainKBCT.getConfig().SetProblem(prob);
        l = lnr.readLine();
        int noi= (new Integer(l.substring(18,l.length()-1))).intValue();
    	MainKBCT.getConfig().SetNumberOfInputs(noi);
        l = lnr.readLine();
        boolean classFlag= true;
        if (l.substring(16,l.length()-1).equals("No"))
        	classFlag= false;
        
    	MainKBCT.getConfig().SetClassificationFlag(classFlag);
        l = lnr.readLine();
        int nol= (new Integer(l.substring(25,l.length()-1))).intValue();
    	MainKBCT.getConfig().SetNumberOfOutputLabels(nol);
    	//System.out.println("prob="+prob+"  noi="+noi+"  cF="+classFlag+"  nol="+nol);
    	warning1=true;
    } else if (l.equals("[Interpretability]")) {
        String[] paths= new String[10];
        String[] conjs= new String[10];
        String[] disjs= new String[10];
        String[] defuzz= new String[10];
        for (int n=0; n<paths.length; n++) {
            if (n>=9) {
        	    l = lnr.readLine();
        	    paths[n]=l.substring(6,l.length()-1);
        	    l = lnr.readLine();
        	    conjs[n]= l.substring(15,l.length()-1);
        	    l = lnr.readLine();
        	    disjs[n]= l.substring(15,l.length()-1);
        	    l = lnr.readLine();
        	    defuzz[n]= l.substring(19,l.length()-1);
            } else {
        	    l = lnr.readLine();
        	    paths[n]=l.substring(5,l.length()-1);
        	    l = lnr.readLine();
        	    conjs[n]= l.substring(14,l.length()-1);
        	    l = lnr.readLine();
        	    disjs[n]= l.substring(14,l.length()-1);
        	    l = lnr.readLine();
        	    defuzz[n]= l.substring(18,l.length()-1);
            }
        }
        this.KBintPath= paths;
        this.oldKBintPath= paths;
        MainKBCT.getConfig().SetKBCTintFilePath(paths);
        MainKBCT.getConfig().SetKBCTintConjunction(conjs);
        MainKBCT.getConfig().SetKBCTintDisjunction(disjs);
        MainKBCT.getConfig().SetKBCTintDefuzzification(defuzz);
    	warning2=true;
      } else if (l.equals("[Ontology]")) {
          l = lnr.readLine();
          if (l.length()>10)
          	JKBCTFrame.OntologyFile= l.substring(9, l.length() - 1);
          else
          	JKBCTFrame.OntologyFile= "";

          this.oldOntologyFile= JKBCTFrame.OntologyFile;
      }
    }
    if (!warning1) {
    	MainKBCT.getConfig().SetProblem(LocaleKBCT.DefaultProblem());
    	MainKBCT.getConfig().SetNumberOfInputs(LocaleKBCT.DefaultNumberOfInputs());
    	MainKBCT.getConfig().SetClassificationFlag(LocaleKBCT.DefaultClassificationFlag());
    	MainKBCT.getConfig().SetNumberOfOutputLabels(LocaleKBCT.DefaultNumberOfOutputLabels());
    }
    if (!warning2) {
        //MainKBCT.getConfig().SetKBCTintFilePath();
        MainKBCT.getConfig().SetKBCTintConjunction(LocaleKBCT.DefaultKBCTintConjunction());
        MainKBCT.getConfig().SetKBCTintDisjunction(LocaleKBCT.DefaultKBCTintDisjunction());
        MainKBCT.getConfig().SetKBCTintDefuzzification(LocaleKBCT.DefaultKBCTintDefuzzification());
    }
    lnr.close();
    if (this.kbct_Data!=null) {
      this.kbct_Data= new JKBCT(this.kbct_Data);
      //System.out.println("kbctData inputs: "+this.kbct_Data.GetNbInputs());
      //System.out.println("kbctData outputs: "+this.kbct_Data.GetNbOutputs());
    }
  }
//------------------------------------------------------------------------------
  private boolean OpenXML() throws Throwable {
  	    //System.out.println("OpenXML");
	    XMLParser theParser = new XMLParser();
        String ExpertFile= (String)theParser.getXMLinfo(this.IKBFile, "ExpertFile");
        if (ExpertFile==null) {
        	MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("OpenValidXML"));
        	return true;
        } else {
            //if (ExpertFile!=null) {
            JKBCTFrame.KBExpertFile= ExpertFile;
            //System.out.println("ExpertFile="+ExpertFile);
            //} else {
    	    //JKBCTFrame.KBExpertFile= "";
            //}
            this.oldKBExpertFile= JKBCTFrame.KBExpertFile;
            if (!JKBCTFrame.KBExpertFile.equals("") && (new File(JKBCTFrame.KBExpertFile)).exists()){
                //System.out.println("new JEF: "+JKBCTFrame.KBExpertFile);
            	this.jef= new JExpertFrame(JKBCTFrame.KBExpertFile, this, null);
                this.jef.setVisible(false);
                if (!LocaleKBCT.isWindowsPlatform())
            	    this.jef.dispose();
            
                this.kbct= this.jef.Temp_kbct;
            }
            if (this.kbct!=null) {
                Hashtable hfis = (Hashtable)theParser.getXMLinfo(this.IKBFile,"FISoperators");
        	    String conjunction= (String)hfis.get("Conjunction");
                int NbOutputs= this.kbct.GetNbOutputs();
                String[] disjunction= null;
                String[] defuzzification= null;
                if (NbOutputs >0) {
                    disjunction= new String[NbOutputs];
                    defuzzification= new String[NbOutputs];
                    for (int n=0; n<NbOutputs; n++) {
                         disjunction[n]= (String)hfis.get("Output"+String.valueOf(n+1)+"-Disjunction");
                         defuzzification[n]= (String)hfis.get("Output"+String.valueOf(n+1)+"-Defuzzification");
                    }
                }
                JConvert.SetFISoptions(conjunction, disjunction, defuzzification);
                this.oldConjunction= conjunction;
                if (NbOutputs >0) {
                    this.oldDisjunction= new String[NbOutputs];
                    this.oldDefuzzification= new String[NbOutputs];
                    for (int n=0; n<NbOutputs; n++) {
                         this.oldDisjunction[n]= JConvert.disjunction[n];
                         this.oldDefuzzification[n]= JConvert.defuzzification[n];
                    }
                }
            }
            Hashtable hdata = (Hashtable)theParser.getXMLinfo(this.IKBFile,"DataInfo");
            String OrigKBDataFile= (String)hdata.get("OrigDataFile");
            if (OrigKBDataFile!=null)
                this.OrigKBDataFile= OrigKBDataFile;
            else
                this.OrigKBDataFile= "";

            String VariableNames= (String)hdata.get("VariableNames");
            if ( (VariableNames!=null) && (VariableNames.equals("'yes'")) )
                this.DataFileLabels= true;
            else
                this.DataFileLabels= false;

            String SelectedVariablesDataFile= (String)hdata.get("SelectedVariables");
            if (SelectedVariablesDataFile!=null)
                this.SelectedVariablesDataFile= SelectedVariablesDataFile;
            else
                this.SelectedVariablesDataFile= "";

            String KBDataFile= (String)hdata.get("DataFile");
            if (KBDataFile != null)
                this.KBDataFile= KBDataFile;
            else
                this.KBDataFile= "";

            this.oldKBDataFile= this.KBDataFile;
            if (!this.KBDataFile.equals(""))
                this.DataFile= new JExtendedDataFile(this.KBDataFile, true);

            String DataNbVariables= (String)hdata.get("DataVariableCount");
            if (DataNbVariables != null)
                this.DataNbVariables= Integer.parseInt(DataNbVariables);
            else
                this.DataNbVariables= 0;

            if (this.DataNbVariables > 0){
                this.kbct_Data = new JKBCT();
                if (!this.SelectedVariablesDataFile.equals("")) {
                    this.variables= new Vector();
                    this.OLD_variables= new Vector();
                    int index= 0;
                    String aux= null;
                    String value= null;
                    for (int i=0; i<this.DataNbVariables; i++) {
                         if (i==0) {
                             index= this.SelectedVariablesDataFile.indexOf(",");
                             aux= this.SelectedVariablesDataFile.substring(index+2);
                             value= this.SelectedVariablesDataFile.substring(0, index);
                         } else {
                             if (i < this.DataNbVariables-1) {
                                 index= aux.indexOf(",");
                                 value= aux.substring(0, index);
                                 aux = aux.substring(index+2);
                             } else
                             value= aux;
                         }
                         this.variables.add(new Integer(value));
                         this.OLD_variables.add(new Integer(value));
                    }
                }
            }
            Hashtable hcontext = (Hashtable)theParser.getXMLinfo(this.IKBFile,"ContextInfo");
    	    MainKBCT.getConfig().SetProblem((String)hcontext.get("Problem"));
    	    int noi= (new Integer((String)hcontext.get("Inputs"))).intValue();
    	    MainKBCT.getConfig().SetNumberOfInputs(noi);
            boolean classFlag= true;
            if (((String)hcontext.get("Classification")).equals("No"))
        	    classFlag= false;
        
    	    MainKBCT.getConfig().SetClassificationFlag(classFlag);
    	    int nol= (new Integer((String)hcontext.get("OutputLabels"))).intValue();
    	    MainKBCT.getConfig().SetNumberOfOutputLabels(nol);

            Hashtable hinterpretability = (Hashtable)theParser.getXMLinfo(this.IKBFile,"InterpretabilityInfo");
            String[] paths= new String[10];
            String[] conjs= new String[10];
            String[] disjs= new String[10];
            String[] defuzz= new String[10];
            for (int n=0; n<10; n++) {
        	     paths[n]= (String)hinterpretability.get("KB"+String.valueOf(n+1)+"-path");
        	     conjs[n]= (String)hinterpretability.get("KB"+String.valueOf(n+1)+"-conjunction");
        	     disjs[n]= (String)hinterpretability.get("KB"+String.valueOf(n+1)+"-disjunction");
        	     defuzz[n]= (String)hinterpretability.get("KB"+String.valueOf(n+1)+"-defuzzification");
            }
            this.KBintPath= paths;
            this.oldKBintPath= paths;
            MainKBCT.getConfig().SetKBCTintFilePath(paths);
            MainKBCT.getConfig().SetKBCTintConjunction(conjs);
            MainKBCT.getConfig().SetKBCTintDisjunction(disjs);
            MainKBCT.getConfig().SetKBCTintDefuzzification(defuzz);
    	
    	    Hashtable hvars = (Hashtable)theParser.getXMLinfo(this.IKBFile,"VarInfo");
            for (int n=0; n<this.DataNbVariables; n++) {
                 variable v = new variable();
                 v.SetName((String)hvars.get("Variable"+String.valueOf(n+1)+"-Name"));
                 v.SetType((String)hvars.get("Variable"+String.valueOf(n+1)+"-Type"));
                 String prueba=(String)hvars.get("Variable"+String.valueOf(n+1)+"-LowerRange");
            //if (prueba==null) {
            	//System.out.println("n="+n);
            //}
                 double LowerR= (new Double(prueba)).doubleValue();
                 v.SetLowerPhysicalRange(LowerR);
                 v.SetLowerInterestRange(LowerR);
                 double UpperR= (new Double((String)hvars.get("Variable"+String.valueOf(n+1)+"-UpperRange"))).doubleValue();
                 v.SetUpperPhysicalRange(UpperR);
                 v.SetUpperInterestRange(UpperR);
                 int LabelsNumber=Integer.parseInt((String)hvars.get("Variable"+String.valueOf(n+1)+"-Labels"));
                 v.SetLabelsNumber(LabelsNumber);
                 v.InitLabelsName(LabelsNumber);
                 v.SetLabelsName();
                 v.SetLabelProperties();
                 boolean output= (new Boolean((String)hvars.get("Variable"+String.valueOf(n+1)+"-Output"))).booleanValue();
                 if (output) {
                     JKBCTOutput new_output = new JKBCTOutput(v, this.kbct_Data.GetNbOutputs()+1);
                     this.kbct_Data.AddOutput(new_output);
                 } else {
                     JKBCTInput new_input = new JKBCTInput(v, this.kbct_Data.GetNbInputs()+1);
                     this.kbct_Data.AddInput(new_input);
                 }
            }
            if (this.kbct_Data!=null)
                this.kbct_Data= new JKBCT(this.kbct_Data);

    	    Hashtable hontfile = (Hashtable)theParser.getXMLinfo(this.IKBFile,"OntologyInfo");
    	    String OntFile= (String)hontfile.get("OntFile");
            if (OntFile!=null) {
                JKBCTFrame.OntologyFile= OntFile;
            } else {
    	        JKBCTFrame.OntologyFile= "";
            }
            this.oldOntologyFile= JKBCTFrame.OntologyFile;
            //this.IKBFile= this.IKBFile.substring(0,this.IKBFile.length()-4);
            this.oldIKBFile= this.IKBFile;
		  //System.out.println(this.IKBFile);
        	return false;
        }
  }
//------------------------------------------------------------------------------
  protected void ReloadData() throws Throwable {
	//  rintln("ReloadData");
    if (this.kbct_Data!=null) {
      String aux= this.kbct_Data.GetKBCTFile();
      this.kbct_Data= new JKBCT();
      this.kbct_Data.SetKBCTFile(aux);
    }
	XMLParser theParser = new XMLParser();
    Hashtable dataInfo= (Hashtable)theParser.getXMLinfo(this.IKBFile, "DataInfo");
    String DataVariableCount= (String)dataInfo.get("DataVariableCount");
    //System.out.println("DataVariableCount="+DataVariableCount);
    int lim= (new Integer(DataVariableCount)).intValue();
    Hashtable varInfo= (Hashtable)theParser.getXMLinfo(this.IKBFile, "VarInfo");
    for (int n=0; n<lim; n++) {
        variable v = new variable();
        String var= "Variable"+String.valueOf(n+1);
        String varname= (String)varInfo.get(var+"-Name");
        v.SetName(varname);
        String vartype= (String)varInfo.get(var+"-Type");
        v.SetType(vartype);
        String varLowerRange= (String)varInfo.get(var+"-LowerRange");
        double LowerR= (new Double(varLowerRange)).doubleValue();
        v.SetLowerPhysicalRange(LowerR);
        v.SetLowerInterestRange(LowerR);
        String varUpperRange= (String)varInfo.get(var+"-UpperRange");
        double UpperR= (new Double(varUpperRange)).doubleValue();
        v.SetUpperPhysicalRange(UpperR);
        v.SetUpperInterestRange(UpperR);
        String varLabelsNumber= (String)varInfo.get(var+"-Labels");
        int LabelsNumber=Integer.parseInt(varLabelsNumber);
        v.SetLabelsNumber(LabelsNumber);
        v.InitLabelsName(LabelsNumber);
        v.SetLabelsName();
        v.SetLabelProperties();
        String varOutputFlag= (String)varInfo.get(var+"-Output");
        boolean outFlag= (new Boolean(varOutputFlag)).booleanValue();
        if (!outFlag) {
            JKBCTInput new_input = new JKBCTInput(v, this.kbct_Data.GetNbInputs()+1);
            this.kbct_Data.AddInput(new_input);
        } else {
            JKBCTOutput new_output = new JKBCTOutput(v, this.kbct_Data.GetNbOutputs()+1);
            this.kbct_Data.AddOutput(new_output);
        }
    }
    //if (IKBFile.endsWith("xml"))
    //	IKBFile= IKBFile.substring(0,IKBFile.length()-4);
    //LineNumberReader lnr= new LineNumberReader(new InputStreamReader(new FileInputStream(new File(IKBFile))));
    //String l;
    /*boolean warning= false;
    //while((l= lnr.readLine())!=null) {
      if (!warning) {
        if (l.equals("[Data]"))
          warning= true;
      } else {
        if (l.startsWith("[Input")) {
          variable v = new variable();
          l = lnr.readLine();
          v.SetName(l.substring(6, l.length() - 1));
          l = lnr.readLine();
          v.SetType(l.substring(6, l.length() - 1));
          l = lnr.readLine();
          int indR= l.indexOf(",");
          double LowerR= (new Double(l.substring(10, indR))).doubleValue();
          v.SetLowerPhysicalRange(LowerR);
          v.SetLowerInterestRange(LowerR);
          double UpperR= (new Double(l.substring(indR+2, l.length()-1))).doubleValue();
          v.SetUpperPhysicalRange(UpperR);
          v.SetUpperInterestRange(UpperR);
          l = lnr.readLine();
          int LabelsNumber=Integer.parseInt(l.substring(17));
          v.SetLabelsNumber(LabelsNumber);
          v.InitLabelsName(LabelsNumber);
          v.SetLabelsName();
          v.SetLabelProperties();
          JKBCTInput new_input = new JKBCTInput(v, this.kbct_Data.GetNbInputs()+1);
          this.kbct_Data.AddInput(new_input);
        } else if (l.startsWith("[Output")) {
          variable v = new variable();
          l = lnr.readLine();
          v.SetName(l.substring(6, l.length() - 1));
          l = lnr.readLine();
          v.SetType(l.substring(6, l.length() - 1));
          l = lnr.readLine();
          int indR= l.indexOf(",");
          double LowerR= (new Double(l.substring(10, indR))).doubleValue();
          v.SetLowerPhysicalRange(LowerR);
          v.SetLowerInterestRange(LowerR);
          double UpperR= (new Double(l.substring(indR+2, l.length()-1))).doubleValue();
          v.SetUpperPhysicalRange(UpperR);
          v.SetUpperInterestRange(UpperR);
          l = lnr.readLine();
          int LabelsNumber=Integer.parseInt(l.substring(17));
          v.SetLabelsNumber(LabelsNumber);
          v.InitLabelsName(LabelsNumber);
          v.SetLabelsName();
          v.SetLabelProperties();
          JKBCTOutput new_output = new JKBCTOutput(v, this.kbct_Data.GetNbOutputs()+1);
          this.kbct_Data.AddOutput(new_output);
        }
      }
    //}*/
    this.DataNbVariables= this.kbct_Data.GetNbInputs()+this.kbct_Data.GetNbOutputs();
    //lnr.close();
    this.DFile= this.DataFile.FileName();
    if( this.DataFile != null ) {
        this.DataFile= new JExtendedDataFile(this.KBDataFile, true);
      }
      if( this.DataFileNoSaved != null ) {
        File fdata= new File(this.DataFileNoSaved.FileName());
        fdata.delete();
        this.DataFileNoSaved.Close();
        this.DataFileNoSaved= null;
        this.DFileNoSaved= null;
      }
      if (this.jef!=null) {
        this.jef= null;
    	this.jef= new JExpertFrame(JKBCTFrame.KBExpertFile, this, null);
        this.jef.setVisible(false);
        this.kbct= this.jef.Temp_kbct;
      }
  }
//------------------------------------------------------------------------------
  private void Save() throws Throwable {
    //System.out.println("JMainFrame - Save");
	this.oldIKBFile= this.IKBFile;
    this.oldKBExpertFile= JKBCTFrame.KBExpertFile;
    this.oldKBDataFile= this.KBDataFile;
    if ( JConvert.conjunction==null ||
    	 JConvert.disjunction==null || 
    	 JConvert.defuzzification==null || 
    	 (JConvert.conjunction!=null && (this.oldConjunction!=null) && (!this.oldConjunction.equals(JConvert.conjunction))) || 
    	 (JConvert.disjunction!=null && (this.oldDisjunction!=null) && (!this.oldDisjunction.equals(JConvert.disjunction))) ||
    	 (JConvert.defuzzification!=null && (this.oldDefuzzification!=null) && (!this.oldDefuzzification.equals(JConvert.defuzzification))) ) {
      if (kbct != null) {
    	int NbOutputs= kbct.GetNbOutputs();
        String[] OutputTypes= null;
        if (NbOutputs >0) {
          OutputTypes= new String[NbOutputs];
          for (int n=0; n<NbOutputs; n++)
            OutputTypes[n]= kbct.GetOutput(n+1).GetType();
        }
        if (JConvert.conjunction==null || JConvert.disjunction==null || JConvert.defuzzification==null)
            JConvert.SetFISoptionsDefault(NbOutputs, OutputTypes);
        
        this.oldConjunction= JConvert.conjunction;
        this.oldDisjunction= JConvert.disjunction;
        this.oldDefuzzification= JConvert.defuzzification;
      }
    }
    XMLWriter.createIKBFile(this.IKBFile);
    //PrintWriter fOut = new PrintWriter(new FileOutputStream(this.IKBFile), false);
    //fOut.println("[Expert]");
    //fOut.println("ExpertFile='"+JKBCTFrame.KBExpertFile+"'");
    //fOut.println("Conjunction='"+JConvert.conjunction+"'");
    //fOut.println();
    if (this.kbct!=null) {
   	  int nout=this.kbct.GetNbOutputs();
      //System.out.println("p5= "+nout);
      /*for (int n=0; n<nout; n++) {
        int N_output= n+1;
        fOut.println("[Output"+N_output+"]");
        fOut.println("Disjunction='"+JConvert.disjunction[n]+"'");
        fOut.println("Defuzzification='"+JConvert.defuzzification[n]+"'");
        fOut.println();
      }*/
        if (nout > 0)
            XMLWriter.writeIKBFileExpert(JKBCTFrame.KBExpertFile, JConvert.conjunction, true);
        else 
            XMLWriter.writeIKBFileExpert(JKBCTFrame.KBExpertFile, JConvert.conjunction, false);
        	
    } else {
        XMLWriter.writeIKBFileExpert(JKBCTFrame.KBExpertFile, JConvert.conjunction, false);
    }
    //fOut.println();
    //fOut.println("[Data]");
    //fOut.println("OrigDataFile='"+this.OrigKBDataFile+"'");
    String VariableNames="";
    if (this.DataFileLabels) {
      // fOut.println("VariablesNames='yes'");
       VariableNames="yes";
    } else {
      // fOut.println("VariablesNames='no'");
       VariableNames="no";
    }
    String SelectedVariables="";
    if (this.variables==null) {
      // fOut.print("SelectedVariables='0");
       SelectedVariables="0";
       for (int i=1; i<this.DataNbVariables; i++) {
      //   fOut.print(", "+i);
         SelectedVariables=SelectedVariables+", "+i;
       }
      // fOut.println("'");
    } else {
    	// fOut.print("SelectedVariables='");
         Enumeration en= this.variables.elements();
         int value= ((Integer)en.nextElement()).intValue();
         SelectedVariables=SelectedVariables+value;
        // fOut.print(value);
         while (en.hasMoreElements()) {
           value= ((Integer)en.nextElement()).intValue();
           SelectedVariables=SelectedVariables+", "+value;
        //   fOut.print(", "+value);
         }
        // fOut.println("'");
    }
    //fOut.println("DataFile='"+this.KBDataFile+"'");
    //fOut.println("DataVariableCount="+this.DataNbVariables);
    this.SelectedVariablesDataFile= SelectedVariables;
    XMLWriter.writeIKBFileData(this.OrigKBDataFile, VariableNames, SelectedVariables, this.KBDataFile, this.DataNbVariables);
    DecimalFormat df= new DecimalFormat();
    df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
    dfs.setDecimalSeparator((new String(".").charAt(0)));
    df.setDecimalFormatSymbols(dfs);
    df.setGroupingSize(20);
    if (this.kbct_Data!=null) {
      int ins= this.kbct_Data.GetNbInputs();
      int outs= this.kbct_Data.GetNbOutputs();
      int lim= ins+outs;
      for (int n = 0; n < lim; n++) {
        int N_variable= n+1;
        variable v;
        boolean output= false;
        if (n<ins)
            v= jnikbct.GetInput(this.kbct_Data.GetPtr(), N_variable);
        else {
            v= jnikbct.GetOutput(this.kbct_Data.GetPtr(), N_variable-ins);
            output=true;
        }
        //fOut.println();
        String variable="Variable"+N_variable;
        //if (n<ins)
          //  fOut.println("[Input"+N_variable+"]");
        //else
          //  fOut.println("[Output"+String.valueOf(N_variable-ins)+"]");

        String variableName=v.GetName();
        //if (variableName!=null && !variableName.equals("")) 
          //  fOut.println("Name='"+variableName+"'");
        //else
          //  fOut.println("Name='"+variable+"'");
        	
        String variableType=v.GetType();
        //fOut.println("Type='"+variableType+"'");
        String varLowerRange=df.format(v.GetLowerInterestRange());
        String varUpperRange=df.format(v.GetUpperInterestRange());
        //String variableRange="[   "+varLowerRange+",   "+varUpperRange+"]";
        //fOut.println("Range="+variableRange);
        int numberOfLabels= v.GetLabelsNumber();
        //fOut.println("Number of labels="+numberOfLabels);
        XMLWriter.writeIKBFileDataVariable(variable, variableName, variableType, varLowerRange, varUpperRange, numberOfLabels, output);
      } 
    } //else {
  	  //System.out.println("kbct_Data NULL");
    //}
    XMLWriter.closeIKBFile(false);
    //fOut.println();
    //fOut.println("[Context]");
    String problem= this.jTFcontextName.getText();
    MainKBCT.getConfig().SetProblem(problem);
    //fOut.println("Problem='"+problem+"'");
    int numberOfInputs= this.jTFcontextNumberInputs.getValue(); 
   	MainKBCT.getConfig().SetNumberOfInputs(numberOfInputs);
    //fOut.println("Number of inputs='"+numberOfInputs+"'");
    boolean classFlag= MainKBCT.getConfig().GetClassificationFlag();
    //if (classFlag)
      //  fOut.println("Classification='yes'");
    //else
      //  fOut.println("Classification='no'");
    	
    int numberOfOutputLabels= this.jTFcontextNumberClasses.getValue();
    MainKBCT.getConfig().SetNumberOfOutputLabels(numberOfOutputLabels);
    //fOut.println("Number of output labels='"+numberOfOutputLabels+"'");
    //fOut.println();
    //fOut.println("[Interpretability]");
    if ( (this.KBintPath==null) || (MainKBCT.getConfig().GetTESTautomatic()) ) {
    	this.KBintPath= new String[10];
    	boolean saveDefault= false;
        for (int n=0; n<this.KBintPath.length; n++) {
        	File aux= new File(this.IKBFile);
            switch (n) {
                //case 0: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+aux.getName().substring(0,aux.getName().length()-4)+"KBint1.kb";
                case 0: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint1.kb.xml";
        	            break;
                //case 1: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+aux.getName().substring(0,aux.getName().length()-4)+"KBint211.kb";
                case 1: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint211.kb.xml";
	                    break;
                //case 2: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+aux.getName().substring(0,aux.getName().length()-4)+"KBint212.kb";
                case 2: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint212.kb.xml";
                        break;
                //case 3: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+aux.getName().substring(0,aux.getName().length()-4)+"KBint213.kb";
                case 3: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint213.kb.xml";
                        break;
                //case 4: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+aux.getName().substring(0,aux.getName().length()-4)+"KBint221.kb";
                case 4: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint221.kb.xml";
                        break;
                //case 5: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+aux.getName().substring(0,aux.getName().length()-4)+"KBint222.kb";
                case 5: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint222.kb.xml";
                        break;
                //case 6: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+aux.getName().substring(0,aux.getName().length()-4)+"KBint223.kb";
                case 6: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint223.kb.xml";
                        break;
                //case 7: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+aux.getName().substring(0,aux.getName().length()-4)+"KBint2.kb";
                case 7: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint2.kb.xml";
                        break;
                //case 8: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+aux.getName().substring(0,aux.getName().length()-4)+"KBint3.kb";
                case 8: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint3.kb.xml";
                        break;
                //case 9: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+aux.getName().substring(0,aux.getName().length()-4)+"KBint4.kb";
                case 9: this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+"KBint4.kb.xml";
                        break;
            }
        	//this.KBintPath[n]=aux.getParent()+System.getProperty("file.separator")+aux.getName().substring(0,aux.getName().length()-4)+"KBint"+String.valueOf(n+1)+".kb";
            //System.out.println("this.KBintPath[n]="+this.KBintPath[n]);
        	File f = new File(this.KBintPath[n]);
            boolean warning= f.exists();
            if (!warning) {
            	f.createNewFile();
            	saveDefault=true;
            }
            /*while(warning) {
              //System.out.println("AutomaticMode="+MainKBCT.getConfig().GetTESTautomatic()); 
              String message= LocaleKBCT.GetString("Interpretability").toUpperCase()+ " -> " + LocaleKBCT.GetString("TheKBCTFile") + " : " + this.KBintPath[n] + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");
              if( !MainKBCT.getConfig().GetTESTautomatic() && MessageKBCT.Confirm(this, message, 1, false, false, false) == JOptionPane.NO_OPTION ) {
              // �Directamente reescribimos los archivos de las BC interpretabilidad?
            	  JOpenFileChooser file_chooser = new JOpenFileChooser(MainKBCT.getConfig().GetKBCTFilePath());
            	  file_chooser.setAcceptAllFileFilterUsed(true);
            	  JFileFilter filter2 = new JFileFilter(("XML").toLowerCase(), LocaleKBCT.GetString("FilterKBCTFile"));
            	  file_chooser.addChoosableFileFilter(filter2);
            	  JFileFilter filter1 = new JFileFilter(("KB").toLowerCase(), LocaleKBCT.GetString("FilterKBCTFile"));
            	  file_chooser.addChoosableFileFilter(filter1);
            	  if( file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ) {
            		  f = new File(file_chooser.getSelectedFile().getAbsolutePath());
                	  warning= false;
            	  }
              } else {
            	  warning= false;
              }
            }*/
        }
        MainKBCT.getConfig().SetKBCTintFilePath(this.KBintPath);
        if (saveDefault) {
            this.saveInterpDefaultKBs();
        }
    } else {
    	this.KBintPath= MainKBCT.getConfig().GetKBCTintFilePath();
    }

    //MainKBCT.getConfig().SetKBCTintFilePath(this.KBintPath);
    String[] conjs=MainKBCT.getConfig().GetKBCTintConjunction();
    String[] disjs=MainKBCT.getConfig().GetKBCTintDisjunction();
    String[] defuzz=MainKBCT.getConfig().GetKBCTintDefuzzification();
    this.oldKBintPath= this.KBintPath;
    //for (int n=0; n<this.KBintPath.length; n++) {
      //  fOut.println("KB"+String.valueOf(n+1)+"='"+this.KBintPath[n]+"'");
      //  fOut.println("conjunction"+String.valueOf(n+1)+"='"+conjs[n]+"'");
      //  fOut.println("disjunction"+String.valueOf(n+1)+"='"+disjs[n]+"'");
      //  fOut.println("defuzzification"+String.valueOf(n+1)+"='"+defuzz[n]+"'");
    //}
    //System.out.println("Save Ontology path file");
    this.oldOntologyFile= JKBCTFrame.OntologyFile;
    //fOut.println();
    //fOut.println("[Ontology]");
    //fOut.println("OntFile='"+JKBCTFrame.OntologyFile+"'");
    //fOut.println();
    //fOut.flush();
    //fOut.close();
    XMLWriter.writeIKBFileContext(problem, numberOfInputs, classFlag, numberOfOutputLabels);
    XMLWriter.writeIKBFileInterpretability(this.KBintPath,conjs,disjs,defuzz);
    XMLWriter.writeIKBFileOntology(JKBCTFrame.OntologyFile);
    XMLWriter.closeIKBFile(true);
  }
//------------------------------------------------------------------------------
  private void SaveDataAs(String FDataName) throws Throwable {
    JFileChooser file_chooser = new JFileChooser(MainKBCT.getConfig().GetDataFilePath());
    file_chooser.setSelectedFile(new File(FDataName));
    if( file_chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION ) {
      String file_name = file_chooser.getSelectedFile().getAbsolutePath();
      File f = new File( file_name );
      if( f.exists() ) {
        String message = LocaleKBCT.GetString("TheDataFile") + " : " + file_name + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");
        if( MessageKBCT.Confirm(this, message, 1, false, false, false) == JOptionPane.NO_OPTION ) {
          this.SaveDataAs(f.getPath());
          return;
        }
      } try {
          MainKBCT.getConfig().SetDataFilePath(file_chooser.getSelectedFile().getParent());
          this.SaveDataFile2(f.getPath());
      } catch( Throwable t ) {
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JMainFrame en SaveDataAs 1: "+t);
      }
    } else
      this.SaveDataAs(FDataName);
  }
//------------------------------------------------------------------------------
  private boolean SaveAs() throws Throwable {
    JFileChooser file_chooser = new JFileChooser(MainKBCT.getConfig().GetKBCTFilePath());
    file_chooser.setAcceptAllFileFilterUsed(false);
    //JFileFilter filter1 = new JFileFilter(("IKB").toLowerCase(), LocaleKBCT.GetString("FilterKBCTFile"));
    //file_chooser.addChoosableFileFilter(filter1);
    JFileFilter filter2 = new JFileFilter(("XML").toLowerCase(), LocaleKBCT.GetString("FilterIKBFile"));
    file_chooser.addChoosableFileFilter(filter2);
    if (!this.IKBFile.equals(""))
        file_chooser.setSelectedFile(new File(MainKBCT.getConfig().GetKBCTFilePath() + System.getProperty("file.separator") + this.IKBFile.toLowerCase() + "." + ("IKB.XML").toLowerCase()));
    else
        file_chooser.setSelectedFile(new File(MainKBCT.getConfig().GetKBCTFilePath() + System.getProperty("file.separator") + LocaleKBCT.GetString("New").toLowerCase() + "." + ("IKB.XML").toLowerCase()));
        //file_chooser.setSelectedFile(new File(MainKBCT.getConfig().GetKBCTFilePath() + System.getProperty("file.separator") + LocaleKBCT.GetString("NewKBFile").toLowerCase() + "." + ("IKB.XML").toLowerCase()));

    if( file_chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION ) {
      String file_name = file_chooser.getSelectedFile().getAbsolutePath();
      if( file_name.lastIndexOf('.') == -1 )
         file_name += '.' + ("IKB.XML").toLowerCase();

      //System.out.println("fn="+file_name);
      File f = new File( file_name );
      if( f.exists() ) {
        String message = LocaleKBCT.GetString("TheKBCTFile") + " : " + file_name + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");
        if( !MainKBCT.getConfig().GetTESTautomatic() && MessageKBCT.Confirm(this, message, 1, false, false, false) == JOptionPane.NO_OPTION )
          return false;
      } 
      try {
          MainKBCT.getConfig().SetKBCTFilePath(file_chooser.getSelectedFile().getParent());
          this.IKBFile= file_name;
          this.Save();
          this.TranslateTitle();
          return true;
      } catch( Throwable t ) {
          t.printStackTrace();
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JMainFrame en SaveAs: "+t);
      }
    }
    return false;
  }
//------------------------------------------------------------------------------
// S�lo usado por MainKBCT para crear archivos IKB de forma autom�tica
  private boolean SaveAs(String fIKB) throws Throwable {
    File fIKBnew= new File(fIKB);
      String file_name = fIKBnew.getAbsolutePath();
      if( file_name.lastIndexOf('.') == -1 )
       file_name += '.' + ("IKB.XML").toLowerCase();

      file_name.replaceAll(" ","-");
      File f = new File( file_name );
      if( f.exists() ) {
        String message = LocaleKBCT.GetString("TheKBCTFile") + " : " + file_name + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");
        if( !MainKBCT.getConfig().GetTESTautomatic() && MessageKBCT.Confirm(this, message, 1, false, false, false) == JOptionPane.NO_OPTION )
          return false;
      } try {
          MainKBCT.getConfig().SetKBCTFilePath(fIKBnew.getParent());
          this.IKBFile= file_name;
          this.Save();
          this.TranslateTitle();
          return true;
      } catch( Throwable t ) {
          //t.printStackTrace();
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JMainFrame en SaveAs: "+t);
      }
    return false;
  }
//------------------------------------------------------------------------------
  private boolean SaveExpertKBAs() {
    JFileChooser file_chooser = new JFileChooser(MainKBCT.getConfig().GetKBCTFilePath());
    file_chooser.setAcceptAllFileFilterUsed(false);
    JFileFilter filter = new JFileFilter(("XML").toLowerCase(), LocaleKBCT.GetString("FilterKBFile"));
    file_chooser.addChoosableFileFilter(filter);
    file_chooser.setSelectedFile(new File(MainKBCT.getConfig().GetKBCTFilePath() + System.getProperty("file.separator") + (LocaleKBCT.GetString("Expert")+"KB").toLowerCase()));
    if( file_chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION ) {
      String file_name = file_chooser.getSelectedFile().getAbsolutePath();
      if( file_name.lastIndexOf('.') == -1 )
       file_name += '.' + ("KB.XML").toLowerCase();

      file_name.replaceAll(" ","-");
      File f = new File( file_name );
        if( f.exists() ) {
          String message = LocaleKBCT.GetString("TheKBCTFile") + " : " + file_name + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");
          if( MessageKBCT.Confirm(this, message, 1, false, false, false) == JOptionPane.NO_OPTION )
            return false;
        }
     try {
        MainKBCT.getConfig().SetKBCTFilePath(file_chooser.getSelectedFile().getParent());
        File ExpertKBFile= new File(JKBCTFrame.KBExpertFile);
        ExpertKBFile.renameTo(f);
        JKBCTFrame.KBExpertFile= file_name;
        if (this.kbct!=null) {
          this.kbct.SetKBCTFile(file_name);
          this.kbct.Save();
        }
        if (this.jef!=null) {
          this.jef.kbct.SetKBCTFile(file_name);
          this.jef.kbct.Save();
        }
        this.jTFExpertName.setText(file_name);
        if (!this.IKBFile.equals(""))
          this.Save();

        return true;
      } catch( Throwable t ) {
         MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JMainFrame en SaveExpertKBAs: "+t);
      }
     }
     return false;
  }
//------------------------------------------------------------------------------
  private boolean SaveDataAs() {
    JFileChooser file_chooser = new JFileChooser(MainKBCT.getConfig().GetKBCTFilePath());
    file_chooser.setAcceptAllFileFilterUsed(true);
    file_chooser.setSelectedFile(new File(MainKBCT.getConfig().GetKBCTFilePath() + System.getProperty("file.separator") + LocaleKBCT.GetString("Data").toLowerCase()));
    if( file_chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION ) {
      String file_name = file_chooser.getSelectedFile().getAbsolutePath();
      file_name.replaceAll(" ","-");
      File f = new File( file_name );
      if( f.exists() ) {
        String message = LocaleKBCT.GetString("TheDataFile") + " : " + file_name + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");
        if( MessageKBCT.Confirm(this, message, 1, false, false, false) == JOptionPane.NO_OPTION )
          return false;
      }
      try {
        MainKBCT.getConfig().SetKBCTFilePath(file_chooser.getSelectedFile().getParent());
        File DataFile= new File(this.KBDataFile);
        DataFile.renameTo(f);
        this.KBDataFile= file_name;
        this.DFile= file_name;
        this.DataFile= new JExtendedDataFile(this.KBDataFile, true);
        this.jTFDataName.setText(file_name);
        if (!this.IKBFile.equals(""))
          this.Save();

        return true;
      } catch( Throwable t ) {
         MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JMainFrame en SaveDataAs 2: "+t);
      }
     }
     return false;
  }
//------------------------------------------------------------------------------
  private boolean ModifiedIKB() {
    if (!this.IKBFile.equals(this.oldIKBFile) ||
        !JKBCTFrame.KBExpertFile.equals(this.oldKBExpertFile) ||
        !this.KBDataFile.equals(this.oldKBDataFile) ||
        !JKBCTFrame.OntologyFile.equals(this.oldOntologyFile) ) {
        //System.out.println("mod1");
    	return true;
    } else {
      if ( (this.oldConjunction==null) && (JConvert.conjunction!=null) ) {
          //System.out.println("mod2");
        return true;
      }
      if ( (JConvert.conjunction!=null) && (this.oldConjunction!=null) && (!JConvert.conjunction.equals(this.oldConjunction)) ) {
          //System.out.println("mod3");
        return true;
      }
      if (this.kbct!=null)
        for (int n=0; n<this.kbct.GetNbOutputs(); n++) {
          if (this.oldDisjunction==null) {
            //System.out.println("mod4");
            return true;
          }
          if (this.oldDefuzzification==null) {
            //System.out.println("mod5");
            return true;
          }
          if ( (JConvert.disjunction!=null) && (JConvert.defuzzification!=null) &&
               (n<JConvert.disjunction.length) && (n<JConvert.defuzzification.length) &&
               (JConvert.disjunction[n]!=null) && (JConvert.defuzzification[n]!=null) &&
               (n<this.oldDisjunction.length) && (n<this.oldDefuzzification.length) &&
               (this.oldDisjunction[n]!=null) && (this.oldDefuzzification[n]!=null) &&
               (!(JConvert.disjunction[n]).equals(this.oldDisjunction[n]) ||
                !(JConvert.defuzzification[n]).equals(this.oldDefuzzification[n]) ) ) {
              //System.out.println("mod6: olddisj="+this.oldDisjunction[n]+"   newdisj="+JConvert.disjunction[n]);
              //System.out.println("mod6: olddef="+this.oldDefuzzification[n]+"   newdef="+JConvert.defuzzification[n]);
              return true;
          }
          if ( (n==0) && (  ( (JConvert.disjunction!=null) && (JConvert.disjunction.length != this.kbct.GetNbOutputs()) ) ||
                            ( (this.oldDisjunction!=null) && (this.oldDisjunction.length != this.kbct.GetNbOutputs()) ) ) ) {
              //System.out.println("mod7");
            return true;
          }
        }
  	  this.KBintPath= MainKBCT.getConfig().GetKBCTintFilePath();
      if (this.oldKBintPath!=null)
  	      for (int n=0; n<this.oldKBintPath.length; n++) {
    	       if (!this.oldKBintPath[n].equals(this.KBintPath[n])) {
    	           //System.out.println("mod8");
    		       return true;
    	       }
          }
      if ( (this.variables != null) && (this.OLD_variables != null) ) {
          Object[] obj= this.variables.toArray();
          Object[] old_obj= this.variables.toArray();
          if (obj.length != old_obj.length) {
	          //System.out.println("mod9");
        	  return true;
          } else {
        	  //System.out.println("obj.length -> "+obj.length);
        	  for (int n=0; n<obj.length; n++) {
        		   int ind= ((Integer)obj[n]).intValue();
        		   int old_ind= ((Integer)old_obj[n]).intValue();
        		   if (ind != old_ind) {
        	           //System.out.println("mod10");
      	        	   return true;
        		   }
        	  }
          }
      }
      return false;
    }
  }
//------------------------------------------------------------------------------
  private boolean ModifiedDataFile() {
	//System.out.println("JMF: ModifiedDataFile");
	//System.out.println("this.kbct_Data -> "+this.kbct_Data.GetPtr());
    if( this.kbct_Data != null && (this.DataFileModificationSimplify || this.kbct_Data.Modified()) ) {
      this.DataFileModificationSimplify= false;
      return true;
    } else
      return false;
  }
//------------------------------------------------------------------------------
  public void Clean() {
	  //System.out.println("JMF: Clean1");
      JConvert.conjunction= null;
      JConvert.disjunction= null;
      JConvert.defuzzification= null;
      this.KB= false;
	  //System.out.println("JMF: Clean2");
      if( this.DataFile != null ) {
        this.DataFile.Close();
        this.DataFile= null;
        this.DFile= null;
      }
	  //System.out.println("JMF: Clean3");
      if( this.DataFileNoSaved != null ) {
        File fdata= new File(this.DataFileNoSaved.FileName());
        fdata.delete();
        this.DataFileNoSaved.Close();
        this.DataFileNoSaved= null;
        this.DFileNoSaved= null;
      }
	  //System.out.println("JMF: Clean4");
      if (this.kbct_Data != null) {
          this.kbct_Data.Close();
          this.kbct_Data.Delete();
      }
	  //System.out.println("JMF: Clean5");
      this.kbct_Data= null;
      if (this.jef != null)
        this.jef.dispose();

	  //System.out.println("JMF: Clean6");
      this.jef= null;
      //if (this.kbct != null) {
    	// long ptr= this.kbct.GetPtr(); 
        // this.kbct.Close();
        // this.kbct.Delete();
        // jnikbct.DeleteKBCT(ptr+1);
      //}
      File f_new = JKBCTFrame.BuildFile("");
      if (f_new.exists()) {
        String[] files = f_new.list();
        for (int n = 0; n < files.length; n++) {
          if (files[n].startsWith("temp") || files[n].startsWith("result")) {
            File faux = new File(f_new, files[n]);
            faux.delete();
          }
        }
      }
      if (JMainFrame.JRFs != null) {
          Object[] obj= JMainFrame.JRFs.toArray();
          for (int n=0; n<obj.length; n++) {
        	   ((JRuleFrame)obj[n]).jMenuClose_actionPerformed();
          }
      }
      //System.out.println("JMF: Clean7");
      //this.HFP_first_time= true;
      this.kbct= null;
      this.IKBFile="";
      JKBCTFrame.KBExpertFile="";
      this.KBDataFile="";
      this.OrigKBDataFile="";
      this.SelectedVariablesDataFile="";
      this.DataFileLabels= false;
      this.VNames=null;
      this.DataNbVariables=0;
      this.oldIKBFile="";
      this.oldKBExpertFile="";
      this.oldKBDataFile="";
      this.oldConjunction= null;
      this.oldDisjunction= null;
      this.oldDefuzzification= null;
	  //System.out.println("JMF: Clean8");
	  //System.out.println(" -> htsize="+jnikbct.getHashtableSize());
	  jnikbct.cleanHashtable();
	  //System.out.println("JMF: Clean9");
	  //System.out.println(" -> htsize="+jnikbct.getHashtableSize());
  }
//------------------------------------------------------------------------------
  private boolean Close() {
    //System.out.println("JMainFrame: close 1");
	if( this.KB ) {
        try {
          boolean warning= false;
          if( this.ModifiedIKB() ) {
           if( (!MainKBCT.getConfig().GetTutorialFlag()) && (!MainKBCT.getConfig().GetTESTautomatic()) && ( MessageKBCT.Confirm(this, LocaleKBCT.GetString("TheIKBIsNotSaved") + "\n" + LocaleKBCT.GetString("DoYouWantToSaveIt"), 0, false, false, false) == JOptionPane.YES_OPTION ) ) {
       	     //System.out.println("JMainFrame: close 2");
             if (this.IKBFile.equals("")) {
           	    //System.out.println("JMainFrame: close 2.1");
                this.SaveAs();
             } else {
           	    //System.out.println("JMainFrame: close 2.2");
                this.Save();
             }
           } else {
         	   //System.out.println("JMainFrame: close 3");
               warning= true;
           }
          }
          if ( (!warning) && ( this.ModifiedDataFile() ) ) {
       	      //System.out.println("JMainFrame: close 4");
              this.Save();
          }
   	      //System.out.println("JMainFrame: close 5");
          this.Clean();
          this.InitJKBCTFrameWithKBCT();
          this.repaint();
      } catch( Throwable except ) {
          except.printStackTrace();
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JMainFrame en Close: "+except);
          return false;
      }
    }
    return true;
  }
//------------------------------------------------------------------------------
  public void dispose() {
    if( this.Close() == false )
      return;

    JKBCTFrame.JMainFrames.removeElement(this);
    JKBCTFrame.RemoveTranslatable(this);
    super.dispose();
    if( JKBCTFrame.JMainFrames.size() == 0 )       // Si es la �nica ventana abierta
      System.exit(0);                       // cierra la aplicaci�n
  }
//------------------------------------------------------------------------------
  void jMenuKBCT_menuSelected() {
    if( !this.KB ) {
      this.jMenuNew.setEnabled(true);
      this.jMenuOpen.setEnabled(true);
      this.jMenuClose.setEnabled(false);
      this.jMenuSave.setEnabled(false);
      this.jMenuSaveAs.setEnabled(false);
      this.jMenuSynthesis.setEnabled(false);
    } else {
      this.jMenuNew.setEnabled(false);
      this.jMenuOpen.setEnabled(false);
      this.jMenuClose.setEnabled(true);
      if( this.IKBFile.equals("") ) {
        this.jMenuSave.setEnabled(false);
        this.jMenuSynthesis.setEnabled(false);
      } else {
        this.jMenuSave.setEnabled(true);
        this.jMenuSynthesis.setEnabled(true);
      }
      this.jMenuSaveAs.setEnabled(true);
    }
  }
//------------------------------------------------------------------------------
  void jMenuMExpert_menuSelected() {
    if( !this.KB ) {
      this.jMenuExpertNew.setEnabled(false);
      this.jMenuExpertOpen.setEnabled(false);
      this.jMenuExpertClose.setEnabled(false);
      this.jMenuExpertSaveAs.setEnabled(false);
    } else {
     if (this.jMenuExpertClose.isEnabled()) {
       this.jMenuExpertNew.setEnabled(false);
       this.jMenuExpertOpen.setEnabled(false);
       this.jMenuExpertSaveAs.setEnabled(true);
     } else {
       this.jMenuExpertNew.setEnabled(true);
       this.jMenuExpertOpen.setEnabled(true);
       this.jMenuExpertSaveAs.setEnabled(false);
     }
    }
  }
//------------------------------------------------------------------------------
  void jMenuMData_menuSelected() {
    if( !this.KB ) {
      this.jMenuDataOpen.setEnabled(false);
      this.jMenuDataClose.setEnabled(false);
      this.jMenuDataView.setEnabled(false);
      this.jMenuDataTable.setEnabled(false);
      this.jMenuDataSaveAs.setEnabled(false);
      this.jMenuDataSmote.setEnabled(false);
      this.jMenuDataFeatureSelection.setEnabled(false);
      this.jMenuDataGenerate.setEnabled(false);
      this.jMenuDataGenerateSample.setEnabled(false);
      this.jMenuDataGenerateCrossValidation.setEnabled(false);
      this.jMenuDataInduce.setEnabled(false);
      this.jMenuDataInducePartitions.setEnabled(false);
      this.jMenuDataInduceRules.setEnabled(false);
      this.jMenuDataBuildKB.setEnabled(false);
      //this.jMenuDataFingrams.setEnabled(false);
    } else {
      if (this.jMenuDataClose.isEnabled()) {
        this.jMenuDataOpen.setEnabled(false);
        this.jMenuDataSaveAs.setEnabled(true);
        this.jMenuDataSmote.setEnabled(true);
        this.jMenuDataFeatureSelection.setEnabled(true);
        this.jMenuDataGenerate.setEnabled(true);
        this.jMenuDataGenerateSample.setEnabled(true);
        this.jMenuDataGenerateCrossValidation.setEnabled(true);
        this.jMenuDataInduce.setEnabled(true);
        this.jMenuDataInducePartitions.setEnabled(true);
        this.jMenuDataInduceRules.setEnabled(true);
        this.jMenuDataBuildKB.setEnabled(true);
        //this.jMenuDataFingrams.setEnabled(true);
      } else {
        this.jMenuDataOpen.setEnabled(true);
        this.jMenuDataSaveAs.setEnabled(false);
        this.jMenuDataSmote.setEnabled(false);
        this.jMenuDataFeatureSelection.setEnabled(false);
        this.jMenuDataGenerate.setEnabled(false);
        this.jMenuDataGenerateSample.setEnabled(false);
        this.jMenuDataGenerateCrossValidation.setEnabled(false);
        this.jMenuDataInduce.setEnabled(false);
        this.jMenuDataInducePartitions.setEnabled(false);
        this.jMenuDataInduceRules.setEnabled(false);
        this.jMenuDataBuildKB.setEnabled(false);
        //this.jMenuDataFingrams.setEnabled(false);
      }
      if (JKBCTFrame.KBExpertFile.equals("")) {
        this.jMenuDataInduce.setEnabled(false);
        this.jMenuDataInducePartitions.setEnabled(false);
        this.jMenuDataInduceRules.setEnabled(false);
        this.jMenuDataBuildKB.setEnabled(false);
        //this.jMenuDataFingrams.setEnabled(false);
      }
    }
  }
//------------------------------------------------------------------------------
  void jMenuHelp_menuSelected() {
    if( !this.KB ) {
      this.jMenuItemQuickStart.setEnabled(true);
    } else {
      this.jMenuItemQuickStart.setEnabled(false);
    }
	if (this.jRBMenuBeginner.isSelected()) {
		this.jMenuItemHelp.setEnabled(false);
	} else {
		this.jMenuItemHelp.setEnabled(true);
	}
    jMenuAbout.setEnabled(true);
    jMenuLicense.setEnabled(true);
  }
//------------------------------------------------------------------------------
  void jMenuItemHelp_actionPerformed(ActionEvent e) { jRBMenuBeginner_actionPerformed(e); }
//------------------------------------------------------------------------------
  void ResetKBExpertFile() {
    JKBCTFrame.KBExpertFile="";
    this.oldKBExpertFile="";
    if (jef != null)
      this.jef.dispose();

    this.jef= null;
    if (this.kbct != null) {
      this.kbct.Close();
      this.kbct.Delete();
    }
    this.kbct= null;
  }
//------------------------------------------------------------------------------
  void ResetKBDataFile() {
    this.KBDataFile="";
    this.oldKBDataFile="";
    this.OrigKBDataFile="";
    this.SelectedVariablesDataFile="";
    this.DataFileLabels= false;
    this.VNames=null;
    if (this.kbct_Data != null) {
      this.kbct_Data.Close();
      this.kbct_Data.Delete();
    }
    this.kbct_Data= null;
    this.DataNbVariables=0;
    this.DataFile= null;
    this.DFile= null;
    this.VNames= null;
    this.variables= null;
    this.OLD_variables= null;
  }
//------------------------------------------------------------------------------
  public JExtendedDataFile getOrigDataFile() {
	JExtendedDataFile res= null;
	try {
	    res= new JExtendedDataFile(this.OrigKBDataFile,true);
    } catch (Throwable t) {
        MessageKBCT.Error(null, t);
    }
	return res;
  }
//------------------------------------------------------------------------------
  public JExtendedDataFile getDataFile() { return this.DataFile; }
//------------------------------------------------------------------------------
  public void setDataFile(String jedf) {
    try {
        File f= new File(this.DataFile.FileName());
        File temp= JKBCTFrame.BuildFile(f.getName());
        this.DataFile= new JExtendedDataFile(jedf,true);
        this.DataFile.Save(temp.getAbsolutePath());
        this.DataFile= new JExtendedDataFile(temp.getAbsolutePath(),true);
    } catch (Throwable t) {
        MessageKBCT.Error(null, t);
    }
  }
//------------------------------------------------------------------------------
  public JExtendedDataFile getDataFileNoSaved() { return this.DataFileNoSaved; }
//------------------------------------------------------------------------------
  public void setDataFileNoSaved(String jedf) {
    try {
      File f= new File(this.DataFileNoSaved.FileName());
      File temp= JKBCTFrame.BuildFile(f.getName());
      this.DataFileNoSaved= new JExtendedDataFile(jedf,true);
      this.DataFileNoSaved.Save(temp.getAbsolutePath());
      this.DataFileNoSaved= new JExtendedDataFile(temp.getAbsolutePath(),true);
    } catch (Throwable t) {
        MessageKBCT.Error(null, t);
    }
  }
//------------------------------------------------------------------------------
  static void ChangeKBExpertFile(String file_name) { KBExpertFile= file_name; }
//------------------------------------------------------------------------------
  public void showInferenceFrame() {
    if (this.jef!=null) {
      if (this.jef.Temp_kbct != null) {
        if (this.jef.Temp_kbct.GetNbActiveRules() > 0) {
          this.jef.jButtonInfer();
        } else {
        	MessageKBCT.Information(this, LocaleKBCT.GetString("NoActiveRule"));
        }
      }
    } 
  }
//------------------------------------------------------------------------------
  public void setInferenceFrameInputValue(int input_number, double new_value) {
    if ( (this.jef!=null) && (this.jef.jif!=null) )
      this.jef.jif.setSliderInputValue(input_number, new_value);
  }
// Interpretability default KBs
//------------------------------------------------------------------------------
  private void saveInterpDefaultKBs() {
    this.saveInterpDefaultKB1();
    this.saveInterpDefaultKB211();
    this.saveInterpDefaultKB212();
    this.saveInterpDefaultKB213();
    this.saveInterpDefaultKB221();
    this.saveInterpDefaultKB222();
    this.saveInterpDefaultKB223();
    this.saveInterpDefaultKB2();
    this.saveInterpDefaultKB3();
    this.saveInterpDefaultKB4();
  }
//------------------------------------------------------------------------------
  // KB1: Data Base Interpretability
  private void saveInterpDefaultKB1() {
	    int NbContextInputs= MainKBCT.getConfig().GetNumberOfInputs();
	    if (NbContextInputs==0)
	    	NbContextInputs=1;
	    	
	    double limsup= 9 * NbContextInputs;
	    double liminf= 2;
	    //System.out.println("Maximum number of rules: "+limsup1);
	    	
	    Vector inputs= new Vector();

	    variable v_NbTotalDefinedLabels= new variable(LocaleKBCT.GetString("NbTotalDefinedLabels"), "numerical", "llow", "no", liminf, limsup, liminf, limsup, 3, "low-high", true, false);
	    v_NbTotalDefinedLabels.SetLabelsName();
	    v_NbTotalDefinedLabels.SetLabelProperties();
	    JKBCTInput jinNbTotalDefinedLabels= new JKBCTInput(v_NbTotalDefinedLabels, 1);
	    inputs.add(jinNbTotalDefinedLabels);

	    variable v_PartitionInterpretability= new variable(LocaleKBCT.GetString("PartitionInterpretability"), "numerical", "llow", "no", 0, 1, 0, 1, 2, "low-high", true, false);
	    v_PartitionInterpretability.SetLabelsName();
	    v_PartitionInterpretability.SetLabelProperties();
	    JKBCTInput jinPartitionInterpretability= new JKBCTInput(v_PartitionInterpretability, 2);
	    inputs.add(jinPartitionInterpretability);

	    this.BuildInterpretabilityKB(inputs, 1, 0);
  }
//------------------------------------------------------------------------------
  // KB211: Rule Base Structural Dimension
  private void saveInterpDefaultKB211() {
	    //int NbContextInputs= MainKBCT.getConfig().GetNumberOfInputs();
	    //double limsup1= Math.pow(9, NbContextInputs);
	    double limsup1= MainKBCT.getConfig().GetMaxNbRules();
	    //System.out.println("Maximum number of rules: "+limsup1);
	    //double limsup2= limsup1*NbContextInputs;
	    double limsup2= MainKBCT.getConfig().GetMaxNbPremises();
	    //System.out.println("Maximum number of premises: "+limsup2);
	    	
	    Vector inputs= new Vector();

	    variable v_NbTotalRules= new variable(LocaleKBCT.GetString("NbTotalRules"), "numerical", "llow", "no", 1, limsup1, 1, limsup1, 3, "low-high", true, false);
	    v_NbTotalRules.SetLabelsName();
	    v_NbTotalRules.SetLabelProperties();
	    JKBCTInput jinNbTotalRules= new JKBCTInput(v_NbTotalRules, 1);
	    inputs.add(jinNbTotalRules);

	    variable v_NbTotalPremises= new variable(LocaleKBCT.GetString("NbTotalPremises"), "numerical", "llow", "no", 1, limsup2, 1, limsup2, 2, "low-high", true, false);
	    v_NbTotalPremises.SetLabelsName();
	    v_NbTotalPremises.SetLabelProperties();
	    JKBCTInput jinNbTotalPremises= new JKBCTInput(v_NbTotalPremises, 2);
	    inputs.add(jinNbTotalPremises);

	    this.BuildInterpretabilityKB(inputs, 211, 1);
  }
//------------------------------------------------------------------------------
  // KB212: Rule Base Structural Complexity
  private void saveInterpDefaultKB212() {
	    Vector inputs= new Vector();

	    // Define inputs
	    variable v_NbRulesWithLessThanTenPercentInputs= new variable(LocaleKBCT.GetString("PercentageNbRulesWithLessThanLPercentInputs"), "numerical", "llow", "no", 0, 100, 0, 100, 2, "low-high", true, false);
	    v_NbRulesWithLessThanTenPercentInputs.SetLabelsName();
	    v_NbRulesWithLessThanTenPercentInputs.SetLabelProperties();
	    JKBCTInput jinNbRulesWithLessThanTenPercentInputs= new JKBCTInput(v_NbRulesWithLessThanTenPercentInputs, 1);
	    inputs.add(jinNbRulesWithLessThanTenPercentInputs);

	    variable v_NbRulesWithBetweenTenAndThirtyPercentInputs= new variable(LocaleKBCT.GetString("PercentageNbRulesWithBetweenLAndMPercentInputs"), "numerical", "llow", "no", 0, 100, 0, 100, 2, "low-high", true, false);
	    v_NbRulesWithBetweenTenAndThirtyPercentInputs.SetLabelsName();
	    v_NbRulesWithBetweenTenAndThirtyPercentInputs.SetLabelProperties();
	    JKBCTInput jinNbRulesWithBetweenTenAndThirtyPercentInputs= new JKBCTInput(v_NbRulesWithBetweenTenAndThirtyPercentInputs, 2);
	    inputs.add(jinNbRulesWithBetweenTenAndThirtyPercentInputs);

	    variable v_NbRulesWithMoreThanThirtyPercentInputs= new variable(LocaleKBCT.GetString("PercentageNbRulesWithMoreThanMPercentInputs"), "numerical", "llow", "no", 0, 100, 0, 100, 2, "low-high", true, false);
	    v_NbRulesWithMoreThanThirtyPercentInputs.SetLabelsName();
	    v_NbRulesWithMoreThanThirtyPercentInputs.SetLabelProperties();
	    JKBCTInput jinNbRulesWithMoreThanThirtyPercentInputs= new JKBCTInput(v_NbRulesWithMoreThanThirtyPercentInputs, 3);
	    inputs.add(jinNbRulesWithMoreThanThirtyPercentInputs);

	    this.BuildInterpretabilityKB(inputs, 212, 2);
  }
//------------------------------------------------------------------------------
  // KB213: Rule Base Structural Framework
  private void saveInterpDefaultKB213() {
	    Vector inputs= new Vector();

	    // Define inputs
	    variable v_outputKB1_RuleBaseStructuralDimension= new variable(LocaleKBCT.GetString("outputKBRuleBaseStructuralDimension"), "numerical", "llow", "no", 0, 1, 0, 1, 3, "low-high", true, false);
	    v_outputKB1_RuleBaseStructuralDimension.SetLabelsName();
	    v_outputKB1_RuleBaseStructuralDimension.SetLabelProperties();
	    JKBCTInput joutputKB1_RuleBaseStructuralDimension= new JKBCTInput(v_outputKB1_RuleBaseStructuralDimension, 1);
	    inputs.add(joutputKB1_RuleBaseStructuralDimension);

	    variable v_outputKB2_RuleBaseStructuralComplexity= new variable(LocaleKBCT.GetString("outputKBRuleBaseStructuralComplexity"), "numerical", "llow", "no", 0, 1, 0, 1, 3, "low-high", true, false);
	    v_outputKB2_RuleBaseStructuralComplexity.SetLabelsName();
	    v_outputKB2_RuleBaseStructuralComplexity.SetLabelProperties();
	    JKBCTInput joutputKB2_RuleBaseStructuralComplexity= new JKBCTInput(v_outputKB2_RuleBaseStructuralComplexity, 2);
	    inputs.add(joutputKB2_RuleBaseStructuralComplexity);

	    this.BuildInterpretabilityKB(inputs, 213, 3);
  }
//------------------------------------------------------------------------------
  // KB221: Rule Base Conceptual Dimension
  private void saveInterpDefaultKB221() {
	    int NbContextInputs= MainKBCT.getConfig().GetNumberOfInputs();
	    if (NbContextInputs==0)
	    	NbContextInputs=1;

	    Vector inputs= new Vector();

	    double limsup= 100;
	    if (NbContextInputs > limsup)
	    	limsup= NbContextInputs;
	    
	    variable v_NbTotalInputs= new variable(LocaleKBCT.GetString("NbTotalInputs"), "numerical", "llow", "no", 1, limsup, 1, limsup, 2, "low-high", true, false);
	    v_NbTotalInputs.SetLabelsName();
	    v_NbTotalInputs.SetLabelProperties();
	    JKBCTInput jinNbTotalInputs= new JKBCTInput(v_NbTotalInputs, 1);
	    inputs.add(jinNbTotalInputs);

	    limsup= 9*NbContextInputs;
	    variable v_NbTotalUsedLabels= new variable(LocaleKBCT.GetString("NbTotalUsedLabels"), "numerical", "llow", "no", 1, limsup, 1, limsup, 3, "low-high", true, false);
	    v_NbTotalUsedLabels.SetLabelsName();
	    v_NbTotalUsedLabels.SetLabelProperties();
	    JKBCTInput jinNbTotalUsedLabels= new JKBCTInput(v_NbTotalUsedLabels, 2);
	    inputs.add(jinNbTotalUsedLabels);

	    this.BuildInterpretabilityKB(inputs, 221, 4);
  }
//------------------------------------------------------------------------------
  // KB222: Rule Base Conceptual Complexity
  private void saveInterpDefaultKB222() {
	    Vector inputs= new Vector();

	    // Define inputs
	    //int NbContextInputs= MainKBCT.getConfig().GetNumberOfInputs();
	    //double limsup1= 9*NbContextInputs;
	    //variable v_NbTotalElementaryUsedLabels= new variable(LocaleKBCT.GetString("PercentageNbTotalElementaryUsedLabels"), "numerical", "llow", "no", 1, limsup1, 1, limsup1, 2, "low-high", true, false);
	    variable v_NbTotalElementaryUsedLabels= new variable(LocaleKBCT.GetString("PercentageNbTotalElementaryUsedLabels"), "numerical", "llow", "no", 0, 100, 0, 100, 2, "low-high", true, false);
	    v_NbTotalElementaryUsedLabels.SetLabelsName();
	    v_NbTotalElementaryUsedLabels.SetLabelProperties();
	    JKBCTInput jinNbTotalElementaryUsedLabels= new JKBCTInput(v_NbTotalElementaryUsedLabels, 1);
	    inputs.add(jinNbTotalElementaryUsedLabels);

	    //double limsup2= 9*jnikbct.serie(8);
	    //variable v_NbTotalORCompositeUsedLabels= new variable(LocaleKBCT.GetString("PercentageNbTotalORCompositeUsedLabels"), "numerical", "llow", "no", 0, limsup2, 0, limsup2, 2, "low-high", true, false);
	    variable v_NbTotalORCompositeUsedLabels= new variable(LocaleKBCT.GetString("PercentageNbTotalORCompositeUsedLabels"), "numerical", "llow", "no", 0, 100, 0, 100, 2, "low-high", true, false);
	    v_NbTotalORCompositeUsedLabels.SetLabelsName();
	    v_NbTotalORCompositeUsedLabels.SetLabelProperties();
	    JKBCTInput jinNbTotalORCompositeUsedLabels= new JKBCTInput(v_NbTotalORCompositeUsedLabels, 3);
	    inputs.add(jinNbTotalORCompositeUsedLabels);
	    
	    //variable v_NbTotalNOTCompositeUsedLabels= new variable(LocaleKBCT.GetString("PercentageNbTotalNOTCompositeUsedLabels"), "numerical", "llow", "no", 0, limsup1, 0, limsup1, 2, "low-high", true, false);
	    variable v_NbTotalNOTCompositeUsedLabels= new variable(LocaleKBCT.GetString("PercentageNbTotalNOTCompositeUsedLabels"), "numerical", "llow", "no", 0, 100, 0, 100, 2, "low-high", true, false);
	    v_NbTotalNOTCompositeUsedLabels.SetLabelsName();
	    v_NbTotalNOTCompositeUsedLabels.SetLabelProperties();
	    JKBCTInput jinNbTotalNOTCompositeUsedLabels= new JKBCTInput(v_NbTotalNOTCompositeUsedLabels, 2);
	    inputs.add(jinNbTotalNOTCompositeUsedLabels);

	    this.BuildInterpretabilityKB(inputs, 222, 5);
  }
//------------------------------------------------------------------------------
  // KB223: Rule Base Conceptual Framework
  private void saveInterpDefaultKB223() {
	    Vector inputs= new Vector();

	    // Define inputs
	    variable v_outputKB1_RuleBaseConceptualDimension= new variable(LocaleKBCT.GetString("outputKBRuleBaseConceptualDimension"), "numerical", "llow", "no", 0, 1, 0, 1, 3, "low-high", true, false);
	    v_outputKB1_RuleBaseConceptualDimension.SetLabelsName();
	    v_outputKB1_RuleBaseConceptualDimension.SetLabelProperties();
	    JKBCTInput joutputKB1_RuleBaseConceptualDimension= new JKBCTInput(v_outputKB1_RuleBaseConceptualDimension, 1);
	    inputs.add(joutputKB1_RuleBaseConceptualDimension);

	    variable v_outputKB2_RuleBaseConceptualComplexity= new variable(LocaleKBCT.GetString("outputKBRuleBaseConceptualComplexity"), "numerical", "llow", "no", 0, 1, 0, 1, 3, "low-high", true, false);
	    v_outputKB2_RuleBaseConceptualComplexity.SetLabelsName();
	    v_outputKB2_RuleBaseConceptualComplexity.SetLabelProperties();
	    JKBCTInput joutputKB2_RuleBaseConceptualComplexity= new JKBCTInput(v_outputKB2_RuleBaseConceptualComplexity, 2);
	    inputs.add(joutputKB2_RuleBaseConceptualComplexity);

	    this.BuildInterpretabilityKB(inputs, 223, 6);
  }
//------------------------------------------------------------------------------
  // KB2: Rule Base Interpretability
  private void saveInterpDefaultKB2() {
	    Vector inputs= new Vector();

	    // Define inputs
	    variable v_outputKB1_RuleBaseStructuralFramework= new variable(LocaleKBCT.GetString("outputKBRuleBaseStructuralFramework"), "numerical", "llow", "no", 0, 1, 0, 1, 3, "low-high", true, false);
	    v_outputKB1_RuleBaseStructuralFramework.SetLabelsName();
	    v_outputKB1_RuleBaseStructuralFramework.SetLabelProperties();
	    JKBCTInput joutputKB1_RuleBaseStructuralFramework= new JKBCTInput(v_outputKB1_RuleBaseStructuralFramework, 1);
	    inputs.add(joutputKB1_RuleBaseStructuralFramework);

	    variable v_outputKB2_RuleBaseConceptualFramework= new variable(LocaleKBCT.GetString("outputKBRuleBaseConceptualFramework"), "numerical", "llow", "no", 0, 1, 0, 1, 3, "low-high", true, false);
	    v_outputKB2_RuleBaseConceptualFramework.SetLabelsName();
	    v_outputKB2_RuleBaseConceptualFramework.SetLabelProperties();
	    JKBCTInput joutputKB2_RuleBaseConceptualFramework= new JKBCTInput(v_outputKB2_RuleBaseConceptualFramework, 2);
	    inputs.add(joutputKB2_RuleBaseConceptualFramework);

	    this.BuildInterpretabilityKB(inputs, 2, 7);
  }
//------------------------------------------------------------------------------
  // KB3: Interpretability Index
  private void saveInterpDefaultKB3() {
	    Vector inputs= new Vector();

	    // Define inputs
	    variable v_outputKB1_DataBaseInterpretability= new variable(LocaleKBCT.GetString("outputKBDataBaseInterpretability"), "numerical", "llow", "no", 0, 1, 0, 1, 2, "low-high", true, false);
	    v_outputKB1_DataBaseInterpretability.SetLabelsName();
	    v_outputKB1_DataBaseInterpretability.SetLabelProperties();
	    JKBCTInput joutputKB1_DataBaseInterpretability= new JKBCTInput(v_outputKB1_DataBaseInterpretability, 1);
	    inputs.add(joutputKB1_DataBaseInterpretability);

	    variable v_outputKB2_RuleBaseInterpretability= new variable(LocaleKBCT.GetString("outputKBRuleBaseInterpretability"), "numerical", "llow", "no", 0, 1, 0, 1, 5, "low-high", true, false);
	    v_outputKB2_RuleBaseInterpretability.SetLabelsName();
	    v_outputKB2_RuleBaseInterpretability.SetLabelProperties();
	    JKBCTInput joutputKB2_RuleBaseInterpretability= new JKBCTInput(v_outputKB2_RuleBaseInterpretability, 2);
	    inputs.add(joutputKB2_RuleBaseInterpretability);

	    this.BuildInterpretabilityKB(inputs, 3, 8);
  }
//------------------------------------------------------------------------------
  // KB4: Interpretability Index (Inference)
  private void saveInterpDefaultKB4() {
	    int NbContextInputs= MainKBCT.getConfig().GetNumberOfInputs();
	    if (NbContextInputs==0)
	    	NbContextInputs=1;

	    double limsup3= Math.pow(9, NbContextInputs);
	    double limsup2= limsup3/2;
	    double limsup1= limsup2/2;

	    Vector inputs= new Vector();

	    // Define inputs
	    variable v_NbRulesTogetherFired2= new variable(LocaleKBCT.GetString("NbRulesTogetherFired2"), "numerical", "llow", "no", 1, limsup1, MainKBCT.getConfig().GetNumberOfOutputLabels(), limsup1, 2, "low-high", true, false);
	    v_NbRulesTogetherFired2.SetLabelsName();
	    v_NbRulesTogetherFired2.SetLabelProperties();
	    JKBCTInput jinNbRulesTogetherFired2= new JKBCTInput(v_NbRulesTogetherFired2, 1);
	    inputs.add(jinNbRulesTogetherFired2);

	    variable v_NbRulesTogetherFired3= new variable(LocaleKBCT.GetString("NbRulesTogetherFired3"), "numerical", "llow", "no", 0, limsup2, 0, limsup2, 2, "low-high", true, false);
	    v_NbRulesTogetherFired3.SetLabelsName();
	    v_NbRulesTogetherFired3.SetLabelProperties();
	    JKBCTInput jinNbRulesTogetherFired3= new JKBCTInput(v_NbRulesTogetherFired3, 2);
	    inputs.add(jinNbRulesTogetherFired3);

	    variable v_NbRulesTogetherFired4= new variable(LocaleKBCT.GetString("NbRulesTogetherFired4"), "numerical", "llow", "no", 0, limsup3, 0, limsup3, 2, "low-high", true, false);
	    v_NbRulesTogetherFired4.SetLabelsName();
	    v_NbRulesTogetherFired4.SetLabelProperties();
	    JKBCTInput jinNbRulesTogetherFired4= new JKBCTInput(v_NbRulesTogetherFired4, 3);
	    inputs.add(jinNbRulesTogetherFired4);

	    this.BuildInterpretabilityKB(inputs, 4, 9);
  }
//------------------------------------------------------------------------------
  private void BuildInterpretabilityKB(Vector inputs, int Number, int Index) {
    String NameKB= MainKBCT.getConfig().GetKBCTintFilePath(Number, Index);
    //System.out.println("NameKB="+NameKB);
    //JKBCT kbctInterp= new JKBCT(NameKB);
    JKBCT kbctInterp= new JKBCT();
    // Define inputs
    if (kbctInterp.GetNbInputs() == 0) {
      Object[] obj= inputs.toArray();
      for (int n=0; n<obj.length; n++) {
        kbctInterp.AddInput((JKBCTInput)obj[n]);
      }
    }
    // Define output
    variable v_output;
    //System.out.println("inputs="+kbctInterp.GetNbInputs());
    //System.out.println("Number="+Number);
    if (kbctInterp.GetNbOutputs() == 0) {
      switch (Index) {
        case 0: v_output= new variable(LocaleKBCT.GetString("outputKBDataBaseInterpretability"), "numerical", "llow", "yes", 0, 1, 0, 1, 2, "low-high", true, false);
                break;
        case 1: v_output= new variable(LocaleKBCT.GetString("outputKBRuleBaseStructuralDimension"), "numerical", "llow", "yes", 0, 1, 0, 1, 3, "low-high", true, false);
                break;
        case 2: v_output= new variable(LocaleKBCT.GetString("outputKBRuleBaseStructuralComplexity"), "numerical", "llow", "yes", 0, 1, 0, 1, 3, "low-high", true, false);
                break;
        case 3: v_output= new variable(LocaleKBCT.GetString("outputKBRuleBaseStructuralFramework"), "numerical", "llow", "yes", 0, 1, 0, 1, 3, "low-high", true, false);
                break;
        case 4: v_output= new variable(LocaleKBCT.GetString("outputKBRuleBaseConceptualDimension"), "numerical", "llow", "yes", 0, 1, 0, 1, 3, "low-high", true, false);
                break;
        case 5: v_output= new variable(LocaleKBCT.GetString("outputKBRuleBaseConceptualComplexity"), "numerical", "llow", "yes", 0, 1, 0, 1, 3, "low-high", true, false);
                break;
        case 6: v_output= new variable(LocaleKBCT.GetString("outputKBRuleBaseConceptualFramework"), "numerical", "llow", "yes", 0, 1, 0, 1, 3, "low-high", true, false);
                break;
        case 7: v_output= new variable(LocaleKBCT.GetString("outputKBRuleBaseInterpretability"), "numerical", "llow", "yes", 0, 1, 0, 1, 5, "low-high", true, false);
                break;
        case 8: v_output= new variable(LocaleKBCT.GetString("InterpretabilityIndex"), "numerical", "llow", "yes", 0, 1, 0, 1, 5, "low-high", true, false);
                break;
        case 9: v_output= new variable(LocaleKBCT.GetString("outputKBLocalExplanation"), "numerical", "llow", "yes", 0, 1, 0, 1, 5, "low-high", true, false);
                break;
        default: v_output= new variable(LocaleKBCT.GetString("Output"), "numerical", "llow", "yes", 0, 1, 0, 1, 3, "low-high", true, false);
                 break;
      }
    
      v_output.SetLabelsName();
      v_output.SetLabelProperties();
      JKBCTOutput joutput= new JKBCTOutput(v_output, 1);
      kbctInterp.AddOutput(joutput);
    }
    // Define rules
    if (kbctInterp.GetNbRules() == 0) {
      int NbR;
      int[][] rules;
      if (Index==0) {
            NbR= 3;
            rules= new int[NbR][3];
            rules[0][0]= 1; rules[0][1]= 0; rules[0][2]= 2;
            rules[1][0]= 0; rules[1][1]= 2; rules[1][2]= 2;
            rules[2][0]= 4; rules[2][1]= 1; rules[2][2]= 1;
            //rules[3][0]= 3; rules[3][1]= 0; rules[3][2]= 2;
        } else if ( (Index==3) || (Index==6) ) {
            NbR= 5;
            rules= new int[NbR][3];
            rules[0][0]= 1; rules[0][1]= 6; rules[0][2]= 1;
            rules[1][0]= 1; rules[1][1]= 3; rules[1][2]= 2;
            rules[2][0]= 2; rules[2][1]= 6; rules[2][2]= 2;
            rules[3][0]= 2; rules[3][1]= 3; rules[3][2]= 3;
            rules[4][0]= 3; rules[4][1]= 0; rules[4][2]= 3;
        } else if (Index==1) {
            NbR= 4;
            rules= new int[NbR][3];
            rules[0][0]= 1; rules[0][1]= 1; rules[0][2]= 1; 
            rules[1][0]= 2; rules[1][1]= 1; rules[1][2]= 2;
            rules[2][0]= 3; rules[2][1]= 0; rules[2][2]= 3;
            rules[3][0]= 0; rules[3][1]= 2; rules[3][2]= 3;
        } else if (Index==4) {
            NbR= 4;
            rules= new int[NbR][3];
            rules[0][0]= 1; rules[0][1]= 1; rules[0][2]= 1; 
            rules[1][0]= 1; rules[1][1]= 2; rules[1][2]= 2;
            rules[2][0]= 0; rules[2][1]= 3; rules[2][2]= 3;
            rules[3][0]= 2; rules[3][1]= 0; rules[3][2]= 3;
        } else if ( (Index==2) || (Index==5) ) {
            NbR= 4;
            rules= new int[NbR][4];
            rules[0][0]= 1; rules[0][1]= 2; rules[0][2]= 1; rules[0][3]= 2;
            rules[1][0]= 2; rules[1][1]= 0; rules[1][2]= 1; rules[1][3]= 1;
            rules[2][0]= 1; rules[2][1]= 0; rules[2][2]= 2; rules[2][3]= 3;
            rules[3][0]= 2; rules[3][1]= 1; rules[3][2]= 2; rules[3][3]= 2;
        } else if (Index==7) {
            NbR= 7;
            rules= new int[NbR][3];
            rules[0][0]= 1; rules[0][1]= 6; rules[0][2]= 5;
            rules[1][0]= 1; rules[1][1]= 3; rules[1][2]= 4;
            rules[2][0]= 2; rules[2][1]= 1; rules[2][2]= 4;
            rules[3][0]= 2; rules[3][1]= 2; rules[3][2]= 3;
            rules[4][0]= 2; rules[4][1]= 3; rules[4][2]= 2;
            rules[5][0]= 3; rules[5][1]= 1; rules[5][2]= 2;
            rules[6][0]= 3; rules[6][1]= 4; rules[6][2]= 1;
        } else if (Index==8) {
            NbR= 8;
            rules= new int[NbR][3];
            rules[0][0]= 0; rules[0][1]= 1; rules[0][2]= 1;
            rules[1][0]= 2; rules[1][1]= 2; rules[1][2]= 2;
            rules[2][0]= 2; rules[2][1]= 3; rules[2][2]= 3;
            rules[3][0]= 1; rules[3][1]= 4; rules[3][2]= 2;
            rules[4][0]= 2; rules[4][1]= 4; rules[4][2]= 4;
            rules[5][0]= 1; rules[5][1]= 5; rules[5][2]= 3;
            rules[6][0]= 2; rules[6][1]= 5; rules[6][2]= 5;
            rules[7][0]= 1; rules[7][1]= 12; rules[7][2]= 1;
        } else {
          // Index==9
          NbR= 3;
          rules= new int[NbR][4];
          rules[0][0]= 2; rules[0][1]= 0; rules[0][2]= 0; rules[0][3]=1;
          rules[1][0]= 1; rules[1][1]= 2; rules[1][2]= 1; rules[1][3]=2;
          rules[2][0]= 0; rules[2][1]= 0; rules[2][2]= 2; rules[2][3]=3;
        }
    
      for (int n=0; n<NbR; n++) {
        //System.out.println("Number="+Number+"  n="+n+"  rules[n]="+rules[n]+"  inputs="+kbctInterp.GetNbInputs());
        kbctInterp.AddRule(this.BuildRule(rules[n], n, kbctInterp.GetNbInputs(), 1));
      }
    }
    try {
        kbctInterp.SetKBCTFile(NameKB);
        kbctInterp.Save();
    } catch (Throwable t) {
        t.printStackTrace();
    	MessageKBCT.Error(null, t);
    }
  }
//------------------------------------------------------------------------------
  private Rule BuildRule(int[] rr, int rule_number, int input_number, int output_number) {
    Rule r= null;
    int[] in_labels= new int[input_number];
    for (int n=0; n<input_number; n++)
      in_labels[n]= rr[n];
    int[] out_labels= new int[output_number];
    for (int n=0; n<output_number; n++)
      out_labels[n]= rr[n+input_number];

    r= new Rule(rule_number, input_number, output_number, in_labels, out_labels, "E", true);
    return r;
  }
//------------------------------------------------------------------------------
  public String getSelectedVariablesInDataFile() {
	  return this.SelectedVariablesDataFile;
  }
}