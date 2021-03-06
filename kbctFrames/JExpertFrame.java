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
//                            JExpertFrame.java
//
//
//**********************************************************************

package kbctFrames;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.util.TimerTask;
import java.lang.Runtime;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import kbct.GAruleSelection;
import kbct.GeneticTuning;
import kbct.InfoConsistency;
import kbct.JLogicalView;
import kbct.JConsistency;
import kbct.JConvert;
import kbct.JFIS;
import kbct.JGENFIS;
import kbct.JKBCT;
import kbct.JKBCTInput;
import kbct.JKBCTOutput;
import kbct.JSolveProblem;
import kbct.JVariable;
import kbct.KBCTListener;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbct.jnikbct;
import kbctAux.JFileFilter;
import kbctAux.JOpenFileChooser;
import kbctAux.MessageKBCT;
import kbctAux.OWLwriter;

import org.freehep.util.export.ExportDialog;

import print.JPrintPreviewTable;
import KB.LabelKBCT;
import KB.Rule;
import KB.variable;
import fis.JExtendedDataFile;
import fis.JSemaphore;
import fis.jnifis;

/**
 * kbctFrames.JExpertFrame generate the main frame which is launched
 * when KBCT is started. It lets user define inputs, outputs, rules, ...
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
public class JExpertFrame extends JKBCTFrame {
  static final long serialVersionUID=0;	
  protected JKBCT kbct_Data= null;
  protected JKBCT kbct= null;             // object kbct
  private KBCTListener KBCTListener;          // listener de KBCT
  // gestion de menus
  private JMenuBar jMenuBarKBCT = new JMenuBar();
  // menu kbct
  private JMenu jMenuKBCT = new JMenu();
    private JMenuItem jMenuClose = new JMenuItem();
    private JMenuItem jMenuSave = new JMenuItem();
    private JMenuItem jMenuSaveAs = new JMenuItem();
    private JMenuItem jMenuSaveAsFIS = new JMenuItem();
    private JMenuItem jMenuSetFISoptions = new JMenuItem();
    private JMenuItem jMenuConvert = new JMenuItem();
    private JMenuItem jMenuLoadOntology = new JMenuItem();
    private JMenuItem jMenuSaveAsFuzzyOntology = new JMenuItem();
    private JMenuItem jMenuSaveAsFuzzyOntologyWithoutRules = new JMenuItem();
    private JMenuItem jMenuExit = new JMenuItem();
  // menu inputs
  private JMenu jMenuInputs = new JMenu();
    private JMenuItem jMenuNewInput = new JMenuItem();
    private JMenuItem jMenuEditInput = new JMenuItem();
    private JMenuItem jMenuRemoveInput = new JMenuItem();
    private JMenuItem jMenuUpInput = new JMenuItem();
    private JMenuItem jMenuDownInput = new JMenuItem();
    private JMenuItem jMenuCopyInput = new JMenuItem();
  // menu outputs
  private JMenu jMenuOutputs = new JMenu();
    private JMenuItem jMenuNewOutput = new JMenuItem();
    private JMenuItem jMenuEditOutput = new JMenuItem();
    private JMenuItem jMenuRemoveOutput = new JMenuItem();
    private JMenuItem jMenuUpOutput = new JMenuItem();
    private JMenuItem jMenuDownOutput = new JMenuItem();
    private JMenuItem jMenuCopyOutput = new JMenuItem();
  // menu rules
  private JMenu jMenuRules = new JMenu();
    private JMenuItem jMenuNewRule = new JMenuItem();
    private JMenuItem jMenuRemoveRule = new JMenuItem();
    private JMenuItem jMenuRemoveRedundantRules = new JMenuItem();
    private JMenuItem jMenuOrderByNbPremisesRules = new JMenuItem();
    private JMenuItem jMenuCumulatedLocalWeightRules = new JMenuItem();
    private JMenuItem jMenuOrderByLocalWeightRules = new JMenuItem();
    private JMenuItem jMenuCumulatedGlobalWeightRules = new JMenuItem();
    private JMenuItem jMenuOrderByGlobalWeightRules = new JMenuItem();
    private JMenuItem jMenuCumulatedWeightRules = new JMenuItem();
    private JMenuItem jMenuOrderByWeightRules = new JMenuItem();
    private JMenuItem jMenuLocalInterpretabilityRules = new JMenuItem();
    private JMenuItem jMenuOrderByLocalInterpretabilityRules = new JMenuItem();
    private JMenuItem jMenuGlobalInterpretabilityRules = new JMenuItem();
    private JMenuItem jMenuOrderByGlobalInterpretabilityRules = new JMenuItem();
    private JMenuItem jMenuInterpretabilityRules = new JMenuItem();
    private JMenuItem jMenuOrderByInterpretabilityRules = new JMenuItem();
    private JMenuItem jMenuOrderByOutputClass = new JMenuItem();
    private JMenuItem jMenuReverseOrder = new JMenuItem();
    private JMenuItem jMenuSelectRules = new JMenuItem();
    private JMenuItem jMenuCopyRules = new JMenuItem();
    private JMenuItem jMenuUpRules = new JMenuItem();
    private JMenuItem jMenuDownRules = new JMenuItem();
    private JMenuItem jMenuFingramRules = new JMenuItem();
    private JMenuItem jMenuMergeRules = new JMenuItem();
    private JMenuItem jMenuExpandRules = new JMenuItem();
    private JMenuItem jMenuActiveRules = new JMenuItem();
    private JMenuItem jMenuInActiveRules = new JMenuItem();
    private JMenuItem jMenuGenerateAllRules = new JMenuItem();
    private JMenuItem jMenuPrintRules= new JMenuItem();
    private JMenuItem jMenuExportRules= new JMenuItem();
  // menus popup
  private JPopupMenu jPopupMenuInputs = new JPopupMenu();
    private JMenuItem jPopupNewInput = new JMenuItem();
    private JMenuItem jPopupEditInput = new JMenuItem();
    private JMenuItem jPopupRemoveInput = new JMenuItem();
    private JMenuItem jPopupUpInput = new JMenuItem();
    private JMenuItem jPopupDownInput = new JMenuItem();
    private JMenuItem jPopupCopyInput = new JMenuItem();
  private JPopupMenu jPopupMenuOutputs = new JPopupMenu();
    private JMenuItem jPopupNewOutput = new JMenuItem();
    private JMenuItem jPopupEditOutput = new JMenuItem();
    private JMenuItem jPopupRemoveOutput = new JMenuItem();
    private JMenuItem jPopupUpOutput = new JMenuItem();
    private JMenuItem jPopupDownOutput = new JMenuItem();
    private JMenuItem jPopupCopyOutput = new JMenuItem();
  private JPopupMenu jPopupMenuRules = new JPopupMenu();
    private JMenuItem jPopupNewRule = new JMenuItem();
    private JMenuItem jPopupRemoveRule = new JMenuItem();
    private JMenuItem jPopupRemoveRedundantRules = new JMenuItem();
    private JMenuItem jPopupOrderByNbPremisesRules = new JMenuItem();
    private JMenuItem jPopupCumulatedLocalWeightRules = new JMenuItem();
    private JMenuItem jPopupOrderByLocalWeightRules = new JMenuItem();
    private JMenuItem jPopupCumulatedGlobalWeightRules = new JMenuItem();
    private JMenuItem jPopupOrderByGlobalWeightRules = new JMenuItem();
    private JMenuItem jPopupCumulatedWeightRules = new JMenuItem();
    private JMenuItem jPopupOrderByWeightRules = new JMenuItem();
    private JMenuItem jPopupLocalInterpretabilityRules = new JMenuItem();
    private JMenuItem jPopupOrderByLocalInterpretabilityRules = new JMenuItem();
    private JMenuItem jPopupGlobalInterpretabilityRules = new JMenuItem();
    private JMenuItem jPopupOrderByGlobalInterpretabilityRules = new JMenuItem();
    private JMenuItem jPopupInterpretabilityRules = new JMenuItem();
    private JMenuItem jPopupOrderByInterpretabilityRules = new JMenuItem();
    private JMenuItem jPopupOrderByOutputClass = new JMenuItem();
    private JMenuItem jPopupReverseOrder = new JMenuItem();
    private JMenuItem jPopupCopyRule = new JMenuItem();
    private JMenuItem jPopupSelectRule = new JMenuItem();
    private JMenuItem jPopupUpRule = new JMenuItem();
    private JMenuItem jPopupDownRule = new JMenuItem();
    private JMenuItem jPopupExpandRule = new JMenuItem();
    private JMenuItem jPopupMergeRule = new JMenuItem();
    private JMenuItem jPopupFingramRules = new JMenuItem();
    private JMenuItem jPopupActiveRule = new JMenuItem();
    private JMenuItem jPopupInActiveRule = new JMenuItem();

  private JPanel jPHeader = new JPanel(new GridBagLayout());
  // nom
  private JPanel jPName = new JPanel(new GridBagLayout());
  private TitledBorder titledBorderName;
  protected JTextField jTFName = new JTextField();
  // Lists
  private JList jListInputs = new JList() {
    static final long serialVersionUID=0;	
    protected ListSelectionModel createSelectionModel() { return new DefaultListSelectionModel() {
   	  static final long serialVersionUID=0;
      public boolean isSelectedIndex(int index) {
        boolean retour = super.isSelectedIndex(index);
            if( (JExpertFrame.this.Temp_kbct.GetNbInputs()==0) || ( ((JSemaphore)JExpertFrame.this.Temp_kbct.GetInputSemaphore(index)).Permits() == 0 ) )
              return false;
        return retour;
        }
      }; }
    };
  private JList jListOutputs = new JList() {
  	static final long serialVersionUID=0;
    protected ListSelectionModel createSelectionModel() { return new DefaultListSelectionModel() {
   	  static final long serialVersionUID=0;
      public boolean isSelectedIndex(int index) {
        boolean retour = super.isSelectedIndex(index);
        if( (JExpertFrame.this.Temp_kbct.GetNbOutputs()==0) || ( ((JSemaphore)JExpertFrame.this.Temp_kbct.GetOutputSemaphore(index)).Permits() == 0 ) )
          return false;
        return retour;
        }
      }; }
    };
  private DefaultListModel jListInputsModel;
  private DefaultListModel jListOutputsModel;
  // entradas
  private JScrollPane jScrollPaneInputs = new JScrollPane();
  private TitledBorder titledBorderInputs;
  // reglas
  private JTable jTableRules;
  private JScrollPane jPanelRules;
  private TitledBorder titledBorderRules;
  private DefaultTableModel RuleModel;
  private int NbIn;
  private int NbOut;
  private int NbRule;
  private Vector Title;
  private Vector Data;
  private JSemaphore JRuleFrameOpen= new JSemaphore();
  // salidas
  private JScrollPane jScrollPaneOutputs = new JScrollPane();
  private TitledBorder titledBorderOutputs;
  // coherencia
  protected static JConsistencyFrame jcf;
  private JSolveProblem jsp;
  private JPanel jPanelButtons= new JPanel(new GridBagLayout());
  private JPanel jPanelPanels= new JPanel(new GridBagLayout());
  protected JInferenceFrame jif;
  protected JRulesBaseQualityFrame jrbqf;
  protected JRulesBaseQualityFrame jrbqfTut;
  protected JKBInterpretabilityFrame jkbif;
  private JFISConsole jfcRuleIntWeights;
  private JFISConsole jfcRuleWeights;
  private JFISConsole jfcLSC;
  private JFISConsole jfcOF;
  private JFISConsole jfcCompleteness;
  private JFISConsole jfcLinguisticSummary;
  private JFISConsole jfcLogView;
  private int cursor=0;
  // reduce data base
  private Vector MSGS;
  private boolean DataFileModificationSimplify= false;
  // parameters for quality evaluation
  private double[][] qold= null;
  // flag for clossing KBCT window in Linux
  private boolean clossing= false;
  // onltology
  protected static JOntologyFrame jowlf;
  // rule weights
  private double[] ruleIntWeights;
  private double[] ruleWeights;
  private double[] ruleWeightsCC;
  private double[] ruleWeightsIC;
  private int[] SelRuleNumbers;
  private Rule[] SelRules;
  private String firstKey= null;
  private long firstKeyPressed= -1;
  
//------------------------------------------------------------------------------
  public JExpertFrame() {
  }
  
  
  public JExpertFrame(JMainFrame Parent) {
    this.Parent= Parent;
    this.expertWindow= true;
    //System.out.println("new JEF -> JMF");
    try {
      jbInit();
      this.InitJExpertFrameWithKBCT();
      JKBCTFrame.AddTranslatable(this);
    }
    catch(Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in constructor1 of JExpertFrame: "+t);
      this.dispose();
    }
  }
//------------------------------------------------------------------------------
  public JExpertFrame( JKBCT kbct ) {
    try {
      //System.out.println("new JEF -> JKBCT");
      this.expertWindow= true;
      jbInit();
      this.kbct = kbct;
      this.Temp_kbct= new JKBCT(kbct);
	  //MainKBCT.getConfig().SetMaxTreeDepth(this.Temp_kbct.GetNbInputs());
      this.InitJExpertFrameWithKBCT();
      JKBCTFrame.AddTranslatable(this);
    } catch(Throwable t) {
    	t.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in constructor2 of JExpertFrame: "+t);
        this.dispose();
    }
  }
//------------------------------------------------------------------------------
  public JExpertFrame( String file_name, JMainFrame Parent, String TempFile ) {
    this.Parent= Parent;
    this.expertWindow= true;
    //System.out.println("new JEF -> file_name, JMF, TempFile: "+TempFile);
    try {
      jbInit();
      if (TempFile == null) {
        //System.out.println("JEF: create kbct");
        //System.out.println("JEF: file_name -> "+file_name);
        this.kbct= new JKBCT(file_name);
        File f= new File(file_name);
        File temp= JKBCTFrame.BuildFile("temp"+f.getName());
        if (temp.exists())
          temp.delete();

        //System.out.println("JEF: create Temp_kbct");
        this.Temp_kbct= new JKBCT(file_name);
       
        //System.out.println("JEF: create temp file -> "+temp.getAbsolutePath());
        this.Temp_kbct.Save(temp.getAbsolutePath(), false);
      } else {
        this.kbct= new JKBCT(file_name);
        this.Temp_kbct= new JKBCT(TempFile);
      }
	  //MainKBCT.getConfig().SetMaxTreeDepth(this.Temp_kbct.GetNbInputs());
      this.InitJExpertFrameWithKBCT();
      JKBCTFrame.AddTranslatable(this);
    } catch(Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in constructor3 of JExpertFrame: "+t);
      this.dispose();
    }
  }
//------------------------------------------------------------------------------
  private void jbInit() throws Throwable {
    MainKBCT.getConfig().SetFingramsSelectedSample(LocaleKBCT.DefaultFingramsSelectedSample());
	if (MainKBCT.getConfig().GetTESTautomatic())
	    this.setVisible(false);

	this.setIconImage(icon_guaje.getImage());
    this.setState(Frame.NORMAL);
    // menu kbct
    jMenuBarKBCT.add(jMenuKBCT);
    jMenuKBCT.add(jMenuClose);
    jMenuKBCT.add(jMenuSave);
    jMenuKBCT.add(jMenuSaveAs);
    jMenuKBCT.add(jMenuSaveAsFIS);
    jMenuKBCT.addSeparator();
    jMenuKBCT.add(jMenuSetFISoptions);
    jMenuKBCT.add(jMenuConvert);
    jMenuKBCT.addSeparator();
    jMenuKBCT.add(jMenuLoadOntology);
    jMenuKBCT.add(jMenuSaveAsFuzzyOntology);
    jMenuKBCT.add(jMenuSaveAsFuzzyOntologyWithoutRules);
    jMenuKBCT.addSeparator();
    jMenuKBCT.add(jMenuExit);
    // menu inputs
    jMenuBarKBCT.add(jMenuInputs);
    jMenuInputs.add(jMenuNewInput);
    jMenuInputs.add(jMenuEditInput);
    jMenuInputs.add(jMenuRemoveInput);
    jMenuInputs.add(jMenuCopyInput);
    jMenuInputs.add(jMenuUpInput);
    jMenuInputs.add(jMenuDownInput);
    // menu outputs
    jMenuBarKBCT.add(jMenuOutputs);
    jMenuOutputs.add(jMenuNewOutput);
    jMenuOutputs.add(jMenuEditOutput);
    jMenuOutputs.add(jMenuRemoveOutput);
    jMenuOutputs.add(jMenuCopyOutput);
    jMenuOutputs.add(jMenuUpOutput);
    jMenuOutputs.add(jMenuDownOutput);
    // menu rules
    jMenuBarKBCT.add(jMenuRules);
    jMenuRules.add(jMenuNewRule);
    jMenuRules.add(jMenuRemoveRule);
    jMenuRules.addSeparator();
    jMenuRules.add(jMenuCumulatedLocalWeightRules);
    jMenuRules.add(jMenuOrderByLocalWeightRules);
    jMenuRules.add(jMenuCumulatedGlobalWeightRules);
    jMenuRules.add(jMenuOrderByGlobalWeightRules);
    jMenuRules.add(jMenuCumulatedWeightRules);
    jMenuRules.add(jMenuOrderByWeightRules);
    jMenuRules.addSeparator();
    jMenuRules.add(jMenuLocalInterpretabilityRules);
    jMenuRules.add(jMenuOrderByLocalInterpretabilityRules);
    jMenuRules.add(jMenuGlobalInterpretabilityRules);
    jMenuRules.add(jMenuOrderByGlobalInterpretabilityRules);
    jMenuRules.add(jMenuInterpretabilityRules);
    jMenuRules.add(jMenuOrderByInterpretabilityRules);
    jMenuRules.addSeparator();
    jMenuRules.add(jMenuOrderByNbPremisesRules);
    jMenuRules.add(jMenuOrderByOutputClass);
    jMenuRules.add(jMenuReverseOrder);
    jMenuRules.addSeparator();
    jMenuRules.add(jMenuCopyRules);
    jMenuRules.add(jMenuUpRules);
    jMenuRules.add(jMenuDownRules);
    jMenuRules.addSeparator();
    jMenuRules.add(jMenuRemoveRedundantRules);
    jMenuRules.add(jMenuExpandRules);
    jMenuRules.add(jMenuMergeRules);
    jMenuRules.addSeparator();
    jMenuRules.add(jMenuFingramRules);
    jMenuRules.addSeparator();
    jMenuRules.add(jMenuSelectRules);
    jMenuRules.add(jMenuGenerateAllRules);
    jMenuRules.addSeparator();
    jMenuRules.add(jMenuActiveRules);
    jMenuRules.add(jMenuInActiveRules);
    jMenuRules.addSeparator();
    jMenuRules.add(jMenuPrintRules);
    jMenuRules.add(jMenuExportRules);
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
    // menu help
    jMenuBarKBCT.add(jMenuHelp);
      jMenuHelp.add(jMenuItemHelp);
      jMenuHelp.add(jMenuAbout);
      jMenuHelp.add(jMenuLicense);
    // menus popup
    // popup inputs
    jPopupMenuInputs.add(jPopupNewInput);
    jPopupMenuInputs.add(jPopupEditInput);
    jPopupMenuInputs.add(jPopupRemoveInput);
    jPopupMenuInputs.add(jPopupCopyInput);
    jPopupMenuInputs.add(jPopupUpInput);
    jPopupMenuInputs.add(jPopupDownInput);
    // popup outputs
    jPopupMenuOutputs.add(jPopupNewOutput);
    jPopupMenuOutputs.add(jPopupEditOutput);
    jPopupMenuOutputs.add(jPopupRemoveOutput);
    jPopupMenuOutputs.add(jPopupCopyOutput);
    jPopupMenuOutputs.add(jPopupUpOutput);
    jPopupMenuOutputs.add(jPopupDownOutput);
    // popup rules
    jPopupMenuRules.add(jPopupNewRule);
    jPopupMenuRules.add(jPopupRemoveRule);
    jPopupMenuRules.addSeparator();
    jPopupMenuRules.add(jPopupCumulatedLocalWeightRules);
    jPopupMenuRules.add(jPopupOrderByLocalWeightRules);
    jPopupMenuRules.add(jPopupCumulatedGlobalWeightRules);
    jPopupMenuRules.add(jPopupOrderByGlobalWeightRules);
    jPopupMenuRules.add(jPopupCumulatedWeightRules);
    jPopupMenuRules.add(jPopupOrderByWeightRules);
    jPopupMenuRules.addSeparator();
    jPopupMenuRules.add(jPopupLocalInterpretabilityRules);
    jPopupMenuRules.add(jPopupOrderByLocalInterpretabilityRules);
    jPopupMenuRules.add(jPopupGlobalInterpretabilityRules);
    jPopupMenuRules.add(jPopupOrderByGlobalInterpretabilityRules);
    jPopupMenuRules.add(jPopupInterpretabilityRules);
    jPopupMenuRules.add(jPopupOrderByInterpretabilityRules);
    jPopupMenuRules.addSeparator();
    jPopupMenuRules.add(jPopupOrderByNbPremisesRules);
    jPopupMenuRules.add(jPopupOrderByOutputClass);
    jPopupMenuRules.add(jPopupReverseOrder);
    jPopupMenuRules.addSeparator();
    jPopupMenuRules.add(jPopupCopyRule);
    jPopupMenuRules.add(jPopupUpRule);
    jPopupMenuRules.add(jPopupDownRule);
    jPopupMenuRules.addSeparator();
    jPopupMenuRules.add(jPopupRemoveRedundantRules);
    jPopupMenuRules.add(jPopupExpandRule);
    jPopupMenuRules.add(jPopupMergeRule);
    jPopupMenuRules.addSeparator();
    jPopupMenuRules.add(jPopupFingramRules);
    jPopupMenuRules.addSeparator();
    jPopupMenuRules.add(jPopupSelectRule);
    jPopupMenuRules.addSeparator();
    jPopupMenuRules.add(jPopupActiveRule);
    jPopupMenuRules.add(jPopupInActiveRule);

    this.setJMenuBar(jMenuBarKBCT);
    this.getContentPane().setLayout(new GridBagLayout());
    this.getContentPane().add(jPHeader, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    titledBorderName = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    jPName.setBorder(titledBorderName);
    this.getContentPane().add(jPName, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 220, 0, 220), 0, 0));
    this.jPName.add(jTFName, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.getContentPane().add(jPanelPanels, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.3
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    // inputs
    this.jPanelPanels.add(jScrollPaneInputs, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0));
	if (!MainKBCT.getConfig().GetTESTautomatic())
      jScrollPaneInputs.getViewport().add(jListInputs, null);

    this.jListInputs.setDropTarget(new VariableDrop(this, true));
	titledBorderInputs = new TitledBorder("");
    jScrollPaneInputs.setBorder(titledBorderInputs);
    // outputs
    this.jPanelPanels.add(jScrollPaneOutputs, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0));
	if (!MainKBCT.getConfig().GetTESTautomatic())
      jScrollPaneOutputs.getViewport().add(jListOutputs, null);

    this.jListOutputs.setDropTarget(new VariableDrop(this, false));
	titledBorderOutputs = new TitledBorder("");
    jScrollPaneOutputs.setBorder(titledBorderOutputs);
    // consistency
    this.jPanelButtons.add(jButtonConsistency, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
             ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    // simplify
    this.jPanelButtons.add(jButtonSimplify, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    // reduction by logView
    this.jPanelButtons.add(jButtonLogView, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    // optimization
    this.jPanelButtons.add(jButtonOptimization, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    // completeness
    this.jPanelButtons.add(jButtonCompleteness, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    // infer
    this.jPanelButtons.add(jButtonInfer, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    // fingrams
    this.jPanelButtons.add(jButtonFingrams, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    // quality
    this.jPanelButtons.add(jButtonQuality, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    // summary
    this.jPanelButtons.add(jButtonSummary, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

    this.jPanelPanels.add(jPanelButtons, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

    // traduction
    Translate();
    // management of events
    Events();

    JKBCTFrame.JExpertFrames.addElement(this);
    JKBCTFrame.AddTranslatable(this);
    this.pack();
    Dimension dim = getToolkit().getScreenSize();
    this.setSize(700, 480);
    this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
  }
//------------------------------------------------------------------------------
  private void jbInitTableRules() {
    //System.out.println("jbInitTableRules");
    NbIn = this.Temp_kbct.GetNbInputs();
    NbOut = this.Temp_kbct.GetNbOutputs();
    NbRule = this.Temp_kbct.GetNbRules();
    Title = new Vector<String>();
    for( int i=0 ; i<NbIn+NbOut+3 ; i++ )
      Title.add(new String());

    Data = new Vector<Vector>();
    for( int rule=0 ; rule<NbRule ; rule++ ) {
      Rule jr = (Rule)this.Temp_kbct.GetRule(rule+1);
      Vector rule_data = new Vector();
      rule_data.add(new Integer(rule+1));
      rule_data.add(jr.GetType());
      rule_data.add(new Boolean(jr.GetActive()));
      int[] in_labels_number= jr.Get_in_labels_number();
      for (int n=0; n<NbIn;n++) {
        rule_data.add(new Integer(in_labels_number[n]));
        //if ( (rule==2) && (n==0) ) {
        //	System.out.println(in_labels_number[n]);
        //}
      }
      int[] out_labels_number= jr.Get_out_labels_number();
      for (int n=0; n<NbOut;n++) {
        rule_data.add(new Integer(out_labels_number[n]));
      }
      Data.add( rule_data );
    }
    jTableRules = new JTable();
    RuleModel = new DefaultTableModel() {
   	  static final long serialVersionUID=0;
      public Class getColumnClass(int c) { return getValueAt(0, c).getClass(); }
      public boolean isCellEditable(int row, int col) {
          if( (col == 0) || (col == 1) )
              return false;
          else if ( (col>2) && (!JExpertFrame.this.Temp_kbct.GetRule(row+1).GetActive()) )
              return false;
          else
              return true;
        }
      };
    RuleModel.setDataVector(Data, Title);
    RuleModel.addTableModelListener(new TableModelListener() {
      public void tableChanged(TableModelEvent e) {
    	int row = e.getFirstRow();
        int column = e.getColumn();
        //System.out.println("tableChanged: row="+row+"  col="+column);
        Object data = RuleModel.getValueAt(row, column);
          if( (column == 0) || (column == 1) )
        	if (!MainKBCT.getConfig().GetTESTautomatic())
              JExpertFrame.this.repaint();
          if( column == 2 ) {
            JExpertFrame.this.Temp_kbct.SetRuleActive(row+1, ((Boolean)data).booleanValue());
            //System.out.println("JExpertFrame: tableChanged: AR:"+JExpertFrame.this.Temp_kbct.GetNbActiveRules());
            if (JExpertFrame.this.Temp_kbct.GetNbActiveRules()>0) {
            	JExpertFrame.this.EnableAllButtons();
            } else {
            	JExpertFrame.this.DisableAllButtons();
            }
        	if (!MainKBCT.getConfig().GetTESTautomatic())
                JExpertFrame.this.repaint();
          }
          if( column > 2 ) {
            int conc= ((Integer)data).intValue();
            int NbIn= JExpertFrame.this.NbIn;
            Rule r= JExpertFrame.this.Temp_kbct.GetRule(row+1);
            //System.out.println("Rule changed -> "+r.GetNumber());
            if (column <= NbIn+2) {
              r.SetInputLabel(column-2, conc);
              JExpertFrame.this.Temp_kbct.ReplaceRule( row+1, r);
            } else {
              r.SetOutputLabel(column-NbIn-2, conc);
              JExpertFrame.this.Temp_kbct.ReplaceRule( row+1, r);
            }
          }
        }
      } );
    jTableRules.setModel(RuleModel);
    for( int i=0 ; i<this.jTableRules.getColumnCount() ; i++ )
      this.jTableRules.getColumnModel().getColumn(i).setHeaderRenderer(new DefaultTableCellRenderer() {
      	static final long serialVersionUID=0;
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
          super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
          super.setHorizontalAlignment(SwingConstants.CENTER);
          super.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
          //System.out.println("JExpertFrame:  row="+row+"  col="+column);
          if( column == 0 ) { super.setText(LocaleKBCT.GetString("Rule")); }
          if( column == 1 ) { super.setText(LocaleKBCT.GetString("Type")); }
          if( column == 2 ) { super.setText(LocaleKBCT.GetString("Active")); }
          try {
        	  //System.out.println("JEF: JExpertFrame.this.Temp_kbct -> "+JExpertFrame.this.Temp_kbct.GetPtr());
            JVariable input = null;
            if( (column == 3) && (NbIn != 0) ) {
              input= JExpertFrame.this.Temp_kbct.GetInput(1);
              super.setText(LocaleKBCT.GetString("If") + " " + input.GetName());
            }
            if( (column > 3) && (column < (NbIn+3)) ) {
              input= JExpertFrame.this.Temp_kbct.GetInput(column-2);
              super.setText(LocaleKBCT.GetString("AND") + " " + input.GetName());
            }
            if( column == (NbIn+3) ) {
              input= JExpertFrame.this.Temp_kbct.GetOutput(1);
              super.setText(LocaleKBCT.GetString("THEN") + " " + input.GetName());
            }
            if( (column > (NbIn+3)) && (column < (NbIn+NbOut+3)) ) {
              input= JExpertFrame.this.Temp_kbct.GetOutput(column-NbIn-2);
              super.setText(input.GetName());
            }
            //super.setForeground(Color.blue);
            super.setForeground(Color.yellow);
            super.setBackground(Color.black);
          }
          catch( Throwable t ) {
        	//System.out.println("ERROR blabla");
            t.printStackTrace();
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: printing table: "+t);
          }
          return this;
        }
        });
    MouseAdapter ma = new MouseAdapter() { public void mousePressed(MouseEvent e) { jTableRules_mousePressed(e); } };
    jTableRules.addMouseListener(ma);
    jTableRules.getTableHeader().addMouseListener(ma);
    jTableRules.addKeyListener(new KeyListener() {
      public void keyPressed(KeyEvent e) { jTableRules_keyPressed(e);}
      public void keyReleased(KeyEvent e) { }
      public void keyTyped(KeyEvent e) { }
    } );
    jTableRules.getTableHeader().setReorderingAllowed(false);
    jTableRules.getTableHeader().setResizingAllowed(true);
    jTableRules.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    if( this.jPanelRules != null )
      this.getContentPane().remove(this.jPanelRules);

    jPanelRules = new JScrollPane(jTableRules);
    titledBorderRules = new TitledBorder("");
    jPanelRules.setBorder(titledBorderRules);
    jPanelRules.setWheelScrollingEnabled(true);
    try {
      SetUpInitColumns(0);
      SetUpInitColumns(1);
      SetUpInitColumns(2);
    } catch (Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: table initialization: "+t);
    }
    for (int n=0; n<NbIn;n++)
      SetUpColumn( jTableRules.getColumnModel().getColumn(n+3), Temp_kbct.GetInput(n+1));

    for (int n=0; n<NbOut;n++)
      SetUpColumn( jTableRules.getColumnModel().getColumn(NbIn+n+3), Temp_kbct.GetOutput(n+1));

    InitColumnSizes();
    if (this.cursor>0) {
      JScrollBar scrollBar= jPanelRules.getVerticalScrollBar();
      scrollBar.setMaximum(scrollBar.getMaximum()+this.cursor*50);
      scrollBar.setValue(scrollBar.getMaximum());
      jPanelRules.setVerticalScrollBar(scrollBar);
    }
    jTableRules.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    this.getContentPane().add(jPanelRules, new GridBagConstraints(1, 3, 1, 1, 0.0, 1.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

    titledBorderRules.setTitle(LocaleKBCT.GetString("Rules"));
	if (MainKBCT.getConfig().GetTESTautomatic())
	    this.setVisible(false);
	else
	    this.setVisible(true);
  }
//------------------------------------------------------------------------------
  public void Translate() {
    this.TranslateTitle();
    this.TranslateMenu();
    this.TranslateOptions();
    this.TranslateFileChooser();
    this.titledBorderName.setTitle(LocaleKBCT.GetString("Name"));
    this.titledBorderInputs.setTitle(LocaleKBCT.GetString("Inputs"));
    this.titledBorderOutputs.setTitle(LocaleKBCT.GetString("Outputs"));
    this.jButtonConsistency.setText(LocaleKBCT.GetString("Consistency"));
    this.jButtonConsistency.setToolTipText(LocaleKBCT.GetString("ConsistencyMsg"));
    this.jButtonSimplify.setText(LocaleKBCT.GetString("Simplify"));
    this.jButtonSimplify.setToolTipText(LocaleKBCT.GetString("SimplifyMsg"));
    this.jButtonLogView.setText(LocaleKBCT.GetString("LogView"));
    this.jButtonLogView.setToolTipText(LocaleKBCT.GetString("LogViewMsg"));
    this.jButtonOptimization.setText(LocaleKBCT.GetString("Optimization"));
    this.jButtonOptimization.setToolTipText(LocaleKBCT.GetString("OptimizationMsg"));
    this.jButtonCompleteness.setText(LocaleKBCT.GetString("Completeness"));
    this.jButtonCompleteness.setToolTipText(LocaleKBCT.GetString("CompletenessMsg"));
    this.jButtonQuality.setText(LocaleKBCT.GetString("Quality"));
    this.jButtonQuality.setToolTipText(LocaleKBCT.GetString("QualityMsg"));
    this.jButtonInfer.setText(LocaleKBCT.GetString("Infer"));
    this.jButtonInfer.setToolTipText(LocaleKBCT.GetString("InferMsg"));
    this.jButtonSummary.setText(LocaleKBCT.GetString("Summary"));
    this.jButtonSummary.setToolTipText(LocaleKBCT.GetString("SummaryMsg"));
    this.jButtonFingrams.setText(LocaleKBCT.GetString("Fingrams"));
    this.jButtonFingrams.setToolTipText(LocaleKBCT.GetString("FingramsMsg"));
	if (!MainKBCT.getConfig().GetTESTautomatic())
        this.repaint();
  }
//------------------------------------------------------------------------------
  private void TranslateTitle() {
    this.setTitle(LocaleKBCT.GetString("KBCT"));
    if( this.Temp_kbct != null )
      if( this.Temp_kbct.GetKBCTFile() != null )
        this.setTitle(LocaleKBCT.GetString("KBCT") + ": " + this.kbct.GetKBCTFile());
  }
//------------------------------------------------------------------------------
  private void TranslateMenu() {
    // menu KBCT
    this.jMenuKBCT.setText(LocaleKBCT.GetString("KBCT"));
    this.jMenuClose.setText(LocaleKBCT.GetString("Close"));
    this.jMenuClose.setToolTipText(LocaleKBCT.GetString("CloseKB"));
    this.jMenuSave.setText(LocaleKBCT.GetString("Save"));
    this.jMenuSave.setToolTipText(LocaleKBCT.GetString("SaveKB"));
    this.jMenuSaveAs.setText(LocaleKBCT.GetString("SaveAs"));
    this.jMenuSaveAs.setToolTipText(LocaleKBCT.GetString("SaveAsKB"));
    this.jMenuSaveAsFIS.setText(LocaleKBCT.GetString("SaveAsFIS"));
    this.jMenuSaveAsFIS.setToolTipText(LocaleKBCT.GetString("GenerateFISFile"));
    this.jMenuSetFISoptions.setText(LocaleKBCT.GetString("SetFISoptionsMenu"));
    this.jMenuSetFISoptions.setToolTipText(LocaleKBCT.GetString("SetFISoptionsText"));
    this.jMenuConvert.setText(LocaleKBCT.GetString("Convert"));
    this.jMenuConvert.setToolTipText(LocaleKBCT.GetString("ConvertKB"));
    this.jMenuLoadOntology.setText(LocaleKBCT.GetString("LoadOntology"));
    this.jMenuLoadOntology.setToolTipText(LocaleKBCT.GetString("LoadOntologyText"));
    this.jMenuSaveAsFuzzyOntology.setText(LocaleKBCT.GetString("SaveAsFuzzyOntology"));
    this.jMenuSaveAsFuzzyOntologyWithoutRules.setText(LocaleKBCT.GetString("SaveAsFuzzyOntologyWithoutRules"));
    this.jMenuExit.setText(LocaleKBCT.GetString("Exit"));
    this.jMenuExit.setToolTipText(LocaleKBCT.GetString("ExitKB"));
    // menu INPUTS
    this.jMenuInputs.setText(LocaleKBCT.GetString("Inputs"));
      this.jMenuNewInput.setText(LocaleKBCT.GetString("NewInput")+" (CTRL+N)");
      this.jMenuEditInput.setText(LocaleKBCT.GetString("Edit")+" (CTRL+E)");
      this.jMenuRemoveInput.setText(LocaleKBCT.GetString("Remove")+" (CTRL+DEL)");
      this.jMenuCopyInput.setText(LocaleKBCT.GetString("Copy")+" (CTRL+C)");
      this.jMenuUpInput.setText(LocaleKBCT.GetString("Up")+" (CTRL+U)");
      this.jMenuDownInput.setText(LocaleKBCT.GetString("Down")+" (CTRL+D)");
    // menu OUTPUTS
    this.jMenuOutputs.setText(LocaleKBCT.GetString("Outputs"));
      this.jMenuNewOutput.setText(LocaleKBCT.GetString("NewOutput")+" (CTRL+N)");
      this.jMenuEditOutput.setText(LocaleKBCT.GetString("Edit")+" (CTRL+E)");
      this.jMenuRemoveOutput.setText(LocaleKBCT.GetString("Remove")+" (CTRL+DEL)");
      this.jMenuCopyOutput.setText(LocaleKBCT.GetString("Copy")+" (CTRL+C)");
      this.jMenuUpOutput.setText(LocaleKBCT.GetString("Up")+" (CTRL+U)");
      this.jMenuDownOutput.setText(LocaleKBCT.GetString("Down")+" (CTRL+D)");
    // menu RULES
    this.jMenuRules.setText(LocaleKBCT.GetString("Rules"));
      this.jMenuNewRule.setText(LocaleKBCT.GetString("NewRule")+" (CTRL+N)");
      this.jMenuRemoveRule.setText(LocaleKBCT.GetString("Remove")+" (CTRL+DEL)");
      this.jMenuRemoveRedundantRules.setText(LocaleKBCT.GetString("RemoveRedundantRules"));
      this.jMenuOrderByOutputClass.setText(LocaleKBCT.GetString("OrderByOutput")+" (CTRL+O)");
      this.jMenuCumulatedLocalWeightRules.setText(LocaleKBCT.GetString("CumulatedLocalWeight"));
      this.jMenuOrderByLocalWeightRules.setText(LocaleKBCT.GetString("OrderByLocalWeight"));
      this.jMenuCumulatedGlobalWeightRules.setText(LocaleKBCT.GetString("CumulatedGlobalWeight"));
      this.jMenuOrderByGlobalWeightRules.setText(LocaleKBCT.GetString("OrderByGlobalWeight"));
      this.jMenuCumulatedWeightRules.setText(LocaleKBCT.GetString("CumulatedWeight"));
      this.jMenuOrderByWeightRules.setText(LocaleKBCT.GetString("OrderByWeight")+" (CTRL+W)");
      this.jMenuLocalInterpretabilityRules.setText(LocaleKBCT.GetString("CumulatedLocalIntWeight"));
      this.jMenuOrderByLocalInterpretabilityRules.setText(LocaleKBCT.GetString("OrderByLocalInterpretability"));
      this.jMenuGlobalInterpretabilityRules.setText(LocaleKBCT.GetString("CumulatedGlobalIntWeight"));
      this.jMenuOrderByGlobalInterpretabilityRules.setText(LocaleKBCT.GetString("OrderByGlobalInterpretability"));
      this.jMenuInterpretabilityRules.setText(LocaleKBCT.GetString("CumulatedIntWeight"));
      this.jMenuOrderByInterpretabilityRules.setText(LocaleKBCT.GetString("OrderByInterpretability")+" (CTRL+I)");
      this.jMenuOrderByNbPremisesRules.setText(LocaleKBCT.GetString("OrderByNbPremises")+" (CTRL+P)");
      this.jMenuReverseOrder.setText(LocaleKBCT.GetString("ReverseOrder")+" (CTRL+R)");
      this.jMenuSelectRules.setText(LocaleKBCT.GetString("RuleSelection"));
      this.jMenuCopyRules.setText(LocaleKBCT.GetString("Copy")+" (CTRL+C)");
      this.jMenuUpRules.setText(LocaleKBCT.GetString("Up")+" (CTRL+U)");
      this.jMenuDownRules.setText(LocaleKBCT.GetString("Down")+" (CTRL+D)");
      this.jMenuExpandRules.setText(LocaleKBCT.GetString("Expand")+" (CTRL+E)");
      this.jMenuMergeRules.setText(LocaleKBCT.GetString("Group")+" (CTRL+M)");
      this.jMenuFingramRules.setText(LocaleKBCT.GetString("Fingrams")+" (CTRL+F)");
      this.jMenuActiveRules.setText(LocaleKBCT.GetString("Activate"));
      this.jMenuInActiveRules.setText(LocaleKBCT.GetString("Deactivate"));
      this.jMenuGenerateAllRules.setText(LocaleKBCT.GetString("GenerateAllRules"));
      this.jMenuPrintRules.setText(LocaleKBCT.GetString("Print"));
      this.jMenuExportRules.setText(LocaleKBCT.GetString("Export"));
    // menus popup
    this.jPopupNewInput.setText(LocaleKBCT.GetString("NewInput")+" (CTRL+N)");
    this.jPopupEditInput.setText(LocaleKBCT.GetString("Edit")+" (CTRL+E)");
    this.jPopupRemoveInput.setText(LocaleKBCT.GetString("Remove")+" (CTRL+DEL)");
    this.jPopupCopyInput.setText(LocaleKBCT.GetString("Copy")+" (CTRL+C)");
    this.jPopupUpInput.setText(LocaleKBCT.GetString("Up")+" (CTRL+U)");
    this.jPopupDownInput.setText(LocaleKBCT.GetString("Down")+" (CTRL+D)");
    this.jPopupNewRule.setText(LocaleKBCT.GetString("NewRule")+" (CTRL+N)");
    this.jPopupRemoveRule.setText(LocaleKBCT.GetString("Remove")+" (CTRL+DEL)");
    this.jPopupRemoveRedundantRules.setText(LocaleKBCT.GetString("RemoveRedundantRules"));
    this.jPopupReverseOrder.setText(LocaleKBCT.GetString("ReverseOrder")+" (CTRL+R)");
    this.jPopupOrderByOutputClass.setText(LocaleKBCT.GetString("OrderByOutput")+" (CTRL+O)");
    this.jPopupCumulatedLocalWeightRules.setText(LocaleKBCT.GetString("CumulatedLocalWeight"));
    this.jPopupOrderByLocalWeightRules.setText(LocaleKBCT.GetString("OrderByLocalWeight"));
    this.jPopupCumulatedGlobalWeightRules.setText(LocaleKBCT.GetString("CumulatedGlobalWeight"));
    this.jPopupOrderByGlobalWeightRules.setText(LocaleKBCT.GetString("OrderByGlobalWeight"));
    this.jPopupCumulatedWeightRules.setText(LocaleKBCT.GetString("CumulatedWeight"));
    this.jPopupOrderByWeightRules.setText(LocaleKBCT.GetString("OrderByWeight")+" (CTRL+W)");
    this.jPopupLocalInterpretabilityRules.setText(LocaleKBCT.GetString("CumulatedLocalIntWeight"));
    this.jPopupOrderByLocalInterpretabilityRules.setText(LocaleKBCT.GetString("OrderByLocalInterpretability"));
    this.jPopupGlobalInterpretabilityRules.setText(LocaleKBCT.GetString("CumulatedGlobalIntWeight"));
    this.jPopupOrderByGlobalInterpretabilityRules.setText(LocaleKBCT.GetString("OrderByGlobalInterpretability"));
    this.jPopupInterpretabilityRules.setText(LocaleKBCT.GetString("CumulatedIntWeight"));
    this.jPopupOrderByInterpretabilityRules.setText(LocaleKBCT.GetString("OrderByInterpretability")+" (CTRL+I)");
    this.jPopupOrderByNbPremisesRules.setText(LocaleKBCT.GetString("OrderByNbPremises")+" (CTRL+P)");
    this.jPopupSelectRule.setText(LocaleKBCT.GetString("RuleSelection"));
    this.jPopupCopyRule.setText(LocaleKBCT.GetString("Copy")+" (CTRL+C)");
    this.jPopupUpRule.setText(LocaleKBCT.GetString("Up")+" (CTRL+U)");
    this.jPopupDownRule.setText(LocaleKBCT.GetString("Down")+" (CTRL+D)");
    this.jPopupExpandRule.setText(LocaleKBCT.GetString("Expand")+" (CTRL+E)");
    this.jPopupMergeRule.setText(LocaleKBCT.GetString("Group")+" (CTRL+M)");
    this.jPopupFingramRules.setText(LocaleKBCT.GetString("Fingrams")+" (CTRL+F)");
    this.jPopupActiveRule.setText(LocaleKBCT.GetString("Activate"));
    this.jPopupInActiveRule.setText(LocaleKBCT.GetString("Deactivate"));
    this.jPopupNewOutput.setText(LocaleKBCT.GetString("NewOutput")+" (CTRL+N)");
    this.jPopupEditOutput.setText(LocaleKBCT.GetString("Edit")+" (CTRL+E)");
    this.jPopupRemoveOutput.setText(LocaleKBCT.GetString("Remove")+" (CTRL+DEL)");
    this.jPopupCopyOutput.setText(LocaleKBCT.GetString("Copy")+" (CTRL+C)");
    this.jPopupUpOutput.setText(LocaleKBCT.GetString("Up")+" (CTRL+U)");
    this.jPopupDownOutput.setText(LocaleKBCT.GetString("Down")+" (CTRL+D)");
    if (this.jTableRules!=null)
      titledBorderRules.setTitle(LocaleKBCT.GetString("Rules"));
  }
//------------------------------------------------------------------------------
  private void Events() {
    this.addWindowStateListener(new WindowAdapter() {
        public void windowStateChanged(WindowEvent e) { this_windowStateChanged();}
      });
    // evenentos de menus
    // menu kbct
    jMenuKBCT.addMenuListener(new MenuListener() {
      public void menuSelected(MenuEvent e) { jMenuKBCT_menuSelected(); }
      public void menuDeselected(MenuEvent e) {}
      public void menuCanceled(MenuEvent e) {}
      });
    jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuClose_actionPerformed(); }} );
    jMenuSave.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuSave_actionPerformed(); }} );
    jMenuSaveAs.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuSaveAs_actionPerformed(); }} );
    jMenuSaveAsFIS.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuSaveAsFIS_actionPerformed(); }} );
    jMenuSetFISoptions.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuSetFISoptions_actionPerformed(); }} );
    jMenuConvert.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuConvert_actionPerformed(); }} );
    jMenuLoadOntology.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuLoadOntology_actionPerformed(); }} );
    jMenuSaveAsFuzzyOntology.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuSaveAsFuzzyOntology_actionPerformed(); }} );
    jMenuSaveAsFuzzyOntologyWithoutRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuSaveAsFuzzyOntologyWithoutRules_actionPerformed(); }} );
    jMenuExit.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExit_actionPerformed(); }} );
    // menu INPUTS
    jMenuInputs.addMenuListener(new MenuListener() {
      public void menuSelected(MenuEvent e) { jMenuInputs_menuSelected(); }
      public void menuDeselected(MenuEvent e) {}
      public void menuCanceled(MenuEvent e) {}
      });
      jMenuNewInput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuNewInput_actionPerformed(); }} );
      jMenuEditInput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuEditInput_actionPerformed(); }} );
      jMenuRemoveInput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuRemoveInput_actionPerformed(); }} );
      jMenuCopyInput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuCopyInput_actionPerformed(); }} );
      jMenuUpInput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuUpInput_actionPerformed(); }} );
      jMenuDownInput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuDownInput_actionPerformed(); }} );
    // menu OUTPUTS
    jMenuOutputs.addMenuListener(new MenuListener() {
      public void menuSelected(MenuEvent e) { jMenuOutputs_menuSelected(); }
      public void menuDeselected(MenuEvent e) {}
      public void menuCanceled(MenuEvent e) {}
      });
    jMenuNewOutput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuNewOutput_actionPerformed(); }} );
      jMenuEditOutput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuEditOutput_actionPerformed(); }} );
      jMenuRemoveOutput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuRemoveOutput_actionPerformed(); }} );
      jMenuCopyOutput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuCopyOutput_actionPerformed(); }} );
      jMenuUpOutput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuUpOutput_actionPerformed(); }} );
      jMenuDownOutput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuDownOutput_actionPerformed(); }} );
    // menu RULES
    jMenuRules.addMenuListener(new MenuListener() {
      public void menuSelected(MenuEvent e) { jMenuRules_menuSelected(); }
      public void menuDeselected(MenuEvent e) {}
      public void menuCanceled(MenuEvent e) {}
      });
    jMenuNewRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuNewRule_actionPerformed(); }} );
    jMenuRemoveRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuRemoveRule_actionPerformed(); }} );
    jMenuRemoveRedundantRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuRemoveRedundantRules_actionPerformed(); }} );
    jMenuReverseOrder.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuReverseOrder_actionPerformed(false); }} );
    jMenuOrderByOutputClass.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuOrderByOutputClass_actionPerformed(false); }} );
    jMenuCumulatedLocalWeightRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuCumulatedLocalWeightRules_actionPerformed(); }} );
    jMenuOrderByLocalWeightRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuOrderByLocalWeightRules_actionPerformed(false); }} );
    jMenuCumulatedGlobalWeightRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuCumulatedGlobalWeightRules_actionPerformed(); }} );
    jMenuOrderByGlobalWeightRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuOrderByGlobalWeightRules_actionPerformed(false); }} );
    jMenuCumulatedWeightRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuCumulatedWeightRules_actionPerformed(); }} );
    jMenuOrderByWeightRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuOrderByWeightRules_actionPerformed(false); }} );
    jMenuLocalInterpretabilityRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuLocalInterpretabilityRules_actionPerformed(); }} );
    jMenuOrderByLocalInterpretabilityRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuOrderByLocalInterpretabilityRules_actionPerformed(false); }} );
    jMenuGlobalInterpretabilityRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuGlobalInterpretabilityRules_actionPerformed(); }} );
    jMenuOrderByGlobalInterpretabilityRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuOrderByGlobalInterpretabilityRules_actionPerformed(false); }} );
    //aquiiiii interpretabilidad
    jMenuInterpretabilityRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuInterpretabilityRules_actionPerformed(); }} );
    jMenuOrderByInterpretabilityRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuOrderByInterpretabilityRules_actionPerformed(false); }} );
    jMenuOrderByNbPremisesRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuOrderByNbPremisesRules_actionPerformed(false); }} );
    jMenuSelectRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuSelectRules_actionPerformed(); }} );
    jMenuCopyRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuCopyRules_actionPerformed(); }} );
    jMenuUpRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuUpRules_actionPerformed(); }} );
    jMenuDownRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuDownRules_actionPerformed(); }} );
    jMenuExpandRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExpandRules_actionPerformed(); }} );
    jMenuMergeRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuMergeRules_actionPerformed(); }} );
    jMenuFingramRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonFingrams_actionPerformed(false,null); }} );
    jMenuActiveRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuActiveRules_actionPerformed(); }} );
    jMenuInActiveRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuInActiveRules_actionPerformed(); }} );
    jMenuGenerateAllRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuGenerateAllRules_actionPerformed(); }} );
    jMenuPrintRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuPrintRules_actionPerformed(); }} );
    jMenuExportRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExportRules_actionPerformed(); }} );

    this.OptionsEvents();

    jTFName.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jTFName_actionPerformed(); }} );
    jTFName.addFocusListener(new FocusAdapter() { public void focusLost(FocusEvent e) { jTFName_focusLost(); }} );
    jButtonConsistency.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonConsistency_actionPerformed(false); }} );
    jButtonSimplify.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonSimplify_actionPerformed(false); }} );
    jButtonLogView.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonLogView_actionPerformed(false); }} );
    jButtonOptimization.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonOptimization_actionPerformed(false); }} );
    jButtonCompleteness.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonCompleteness_actionPerformed(false); }} );
    //BOTON CALIDAD
    jButtonQuality.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonQuality_actionPerformed(true,false); }} );
   
    
    jButtonInfer.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonInfer_actionPerformed(); }} );
    jButtonSummary.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonSummary_actionPerformed(); }} );
    jButtonFingrams.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonFingrams_actionPerformed(false,null); }} );
    jListInputs.addKeyListener(new KeyListener() {
      public void keyPressed(KeyEvent e) { jListInputs_keyPressed(e);}
      public void keyReleased(KeyEvent e) { }
      public void keyTyped(KeyEvent e) { }
    } );
    jListOutputs.addKeyListener(new KeyListener() {
      public void keyPressed(KeyEvent e) { jListOutputs_keyPressed(e);}
      public void keyReleased(KeyEvent e) { }
      public void keyTyped(KeyEvent e) { }
    } );
    // popup inputs
    jListInputs.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e) { jListInputs_mousePressed(e); }} );
      jPopupNewInput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupNewInput_actionPerformed(); }} );
      jPopupEditInput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupEditInput_actionPerformed(); }} );
      jPopupRemoveInput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupRemoveInput_actionPerformed(); }} );
      jPopupCopyInput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupCopyInput_actionPerformed(); }} );
      jPopupUpInput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupUpInput_actionPerformed(); }} );
      jPopupDownInput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupDownInput_actionPerformed(); }} );
    // popup rules
      jPopupNewRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupNewRule_actionPerformed(); }} );
      jPopupRemoveRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupRemoveRule_actionPerformed(); }} );
      jPopupRemoveRedundantRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupRemoveRedundantRules_actionPerformed(); }} );
      jPopupReverseOrder.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupReverseOrder_actionPerformed(); }} );
      jPopupOrderByOutputClass.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupOrderByOutputClass_actionPerformed(); }} );
      jPopupCumulatedLocalWeightRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupCumulatedLocalWeightRules_actionPerformed(); }} );
      jPopupOrderByLocalWeightRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupOrderByLocalWeightRules_actionPerformed(); }} );
      jPopupCumulatedGlobalWeightRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupCumulatedGlobalWeightRules_actionPerformed(); }} );
      jPopupOrderByGlobalWeightRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupOrderByGlobalWeightRules_actionPerformed(); }} );
      jPopupCumulatedWeightRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupCumulatedWeightRules_actionPerformed(); }} );
      jPopupOrderByWeightRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupOrderByWeightRules_actionPerformed(); }} );
      jPopupLocalInterpretabilityRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupLocalInterpretabilityRules_actionPerformed(); }} );
      jPopupOrderByLocalInterpretabilityRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupOrderByLocalInterpretabilityRules_actionPerformed(); }} );
      jPopupGlobalInterpretabilityRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupGlobalInterpretabilityRules_actionPerformed(); }} );
      jPopupOrderByGlobalInterpretabilityRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupOrderByGlobalInterpretabilityRules_actionPerformed(); }} );
      jPopupInterpretabilityRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupInterpretabilityRules_actionPerformed(); }} );
      jPopupOrderByInterpretabilityRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupOrderByInterpretabilityRules_actionPerformed(); }} );
      jPopupOrderByNbPremisesRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupOrderByNbPremisesRules_actionPerformed(); }} );
      jPopupSelectRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupSelectRule_actionPerformed(); }} );
      jPopupCopyRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupCopyRule_actionPerformed(); }} );
      jPopupUpRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupUpRule_actionPerformed(); }} );
      jPopupDownRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupDownRule_actionPerformed(); }} );
      jPopupExpandRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupExpandRule_actionPerformed(); }} );
      jPopupMergeRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupMergeRule_actionPerformed(); }} );
      jPopupFingramRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonFingrams_actionPerformed(false,null); }} );
      jPopupActiveRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupActiveRule_actionPerformed(); }} );
      jPopupInActiveRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupInActiveRule_actionPerformed(); }} );
    // popup outputs
    jListOutputs.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e) { jListOutputs_mousePressed(e); }} );
      jPopupNewOutput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupNewOutput_actionPerformed(); }} );
      jPopupEditOutput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupEditOutput_actionPerformed(); }} );
      jPopupRemoveOutput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupRemoveOutput_actionPerformed(); }} );
      jPopupCopyOutput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupCopyOutput_actionPerformed(); }} );
      jPopupUpOutput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupUpOutput_actionPerformed(); }} );
      jPopupDownOutput.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupDownOutput_actionPerformed(); }} );
  }
//------------------------------------------------------------------------------
  private void InitJExpertFrame() {
    this.TranslateTitle();
    this.jListInputsModel = new DefaultListModel();
    this.jListInputs.setModel(this.jListInputsModel);
    this.jListOutputsModel = new DefaultListModel();
    this.jListOutputs.setModel(this.jListOutputsModel);
    this.jListInputs.setEnabled(false);
    this.jListOutputs.setEnabled(false);
    this.jTFName.setEnabled(false);
    this.jTFName.setText("");
    this.DisableAllButtons();
  }
//------------------------------------------------------------------------------
  protected void InitJExpertFrameWithKBCT() throws Throwable {
    this.InitJExpertFrame();
    if( this.Temp_kbct != null ) {
      this.jTFName.setEnabled(true);
      this.jListInputs.setEnabled(true);
      this.jListOutputs.setEnabled(true);
      if (this.Temp_kbct.GetNbInputs()>0 && this.Temp_kbct.GetNbOutputs()>0 && this.Temp_kbct.GetNbActiveRules()>0) {
          this.EnableAllButtons();
      } else {
          this.DisableAllButtons();
      }
      jListInputs.setCellRenderer(new DefaultListCellRenderer() {
      	static final long serialVersionUID=0;
        public Component getListCellRendererComponent( JList list, Object value, int index, boolean selected, boolean hasFocus ) {
          super.getListCellRendererComponent( list, value, index, selected, hasFocus);
          try {
            if (JExpertFrame.this.Temp_kbct.GetNbInputs()>0)
              super.setText(JExpertFrame.this.Temp_kbct.GetInput(index+1).GetName());
          } catch( Throwable e ) {
            e.printStackTrace();
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: InitJExpertFrameWithKBCT1: "+e);
          }
          return this;
        }
      });
      jListOutputs.setCellRenderer(new DefaultListCellRenderer() {
    	static final long serialVersionUID=0;
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
          super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus);
          try {
            if (JExpertFrame.this.Temp_kbct.GetNbOutputs()>0)
              super.setText(JExpertFrame.this.Temp_kbct.GetOutput(index+1).GetName());
          } catch( Throwable e ) {
            e.printStackTrace();
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: InitJExpertFrameWithKBCT2: "+e);
          }
          return this;
        }
      });
      this.jTFName.setText(this.Temp_kbct.GetName());
      for( int i=0 ; i<this.Temp_kbct.GetNbInputs() ; i++ )
        this.jListInputsModel.addElement(new String());

      for( int i=0 ; i<this.Temp_kbct.GetNbOutputs() ; i++ )
        this.jListOutputsModel.addElement(new String());

      if( (this.Temp_kbct.GetNbInputs() > 0) && (this.Temp_kbct.GetNbOutputs() > 0) ) {
         ////Pintar Tabla de reglas
         jbInitTableRules();
         this.JRuleFrameOpen.Acquire();
      }
      // listeners de KBCT
      this.Temp_kbct.RemoveKBCTListener(this.KBCTListener);
      this.KBCTListener = new KBCTListener() {
        public void KBCTClosed() {}
        public void InputActiveChanged( int input_number ) { 
        	if (!MainKBCT.getConfig().GetTESTautomatic())
        	    JExpertFrame.this.repaint();
        	}
        public void OutputActiveChanged( int output_number ) { 
        	if (!MainKBCT.getConfig().GetTESTautomatic())
        	   JExpertFrame.this.repaint();
        	}
        public void InputNameChanged( int input_number ) {
        	if (!MainKBCT.getConfig().GetTESTautomatic())
        	   JExpertFrame.this.repaint();
        	}
        public void OutputNameChanged( int output_number ) { 
        	if (!MainKBCT.getConfig().GetTESTautomatic())
        	   JExpertFrame.this.repaint();
        	}
        public void InputReplaced( int input_number ) {
          //System.out.println("JEF: InputReplaced: "+input_number);
          // checking rule base
          int NbL= JExpertFrame.this.Temp_kbct.GetInput(input_number-1).GetLabelsNumber();
          //System.out.println("NbL="+NbL);
          if (NbL==2) {
            for (int n=0; n<JExpertFrame.this.Temp_kbct.GetNbRules(); n++) {
               Rule r= JExpertFrame.this.Temp_kbct.GetRule(n+1);
               if (r.GetActive()) {
                   int[] ins= r.Get_in_labels_number();
                   if (ins[input_number-2] > 2*NbL) {
                       while (ins[input_number-2] > 2*NbL)
                	          ins[input_number-2]= ins[input_number-2]-2*NbL;

                       r.Set_in_labels_exp(ins);
                       JExpertFrame.this.Temp_kbct.ReplaceRule(r.GetNumber(), r);
                	   break;
                   }
               }
            }
          }
          if ((JExpertFrame.this.Temp_kbct.GetNbInputs()>0)&&(JExpertFrame.this.Temp_kbct.GetNbOutputs()>0))
               JExpertFrame.this.ReInitTableRules();
      	  if (!MainKBCT.getConfig().GetTESTautomatic())
            JExpertFrame.this.repaint();
        }
        public void OutputReplaced( int output_number ) {
          if ((JExpertFrame.this.Temp_kbct.GetNbInputs()>0)&&(JExpertFrame.this.Temp_kbct.GetNbOutputs()>0))
               JExpertFrame.this.ReInitTableRules();
      	  if (!MainKBCT.getConfig().GetTESTautomatic())
            JExpertFrame.this.repaint();
        }
        public void InputPhysicalRangeChanged( int input_number ) {}
        public void InputInterestRangeChanged( int input_number ) {}
        public void OutputPhysicalRangeChanged( int output_number ) {}
        public void OutputInterestRangeChanged( int output_number ) {}
        public void InputRemoved( int input_number ) {
          int NbRules= JExpertFrame.this.Temp_kbct.GetNbRules();
          JExpertFrame.this.NbIn= JExpertFrame.this.Temp_kbct.GetNbInputs()-1;
            if (NbRules>0)
            for (int n=0; n<NbRules;n++) {
               Rule r= JExpertFrame.this.Temp_kbct.GetRule(n+1);
               int[] in_labels_number= r.Get_in_labels_number();
               int r_N_inputs= r.GetNbInputs();
               int[] in_labels_new= new int[r_N_inputs-1];
               for (int m=0;m<r_N_inputs;m++)
                 if (m+1<input_number)
                   in_labels_new[m]= in_labels_number[m];
                 else if (m+1>input_number)
                   in_labels_new[m-1]= in_labels_number[m];
               Rule r_new= new Rule(n+1, r_N_inputs-1, r.GetNbOutputs(), in_labels_new, r.Get_out_labels_number(), r.GetType(), r.GetActive());
               JExpertFrame.this.Temp_kbct.ReplaceRule(n+1,r_new);
            }
          if ((JExpertFrame.this.Temp_kbct.GetNbInputs()>0)&&(JExpertFrame.this.Temp_kbct.GetNbOutputs()>0))
               JExpertFrame.this.ReInitTableRules();
          else if (JExpertFrame.this.jPanelRules != null) {
            for (int n=0; n<NbRules; n++)
              JExpertFrame.this.Temp_kbct.RemoveRule(0);

            JExpertFrame.this.getContentPane().remove(JExpertFrame.this.jPanelRules);
            JExpertFrame.this.pack();
        	if (!MainKBCT.getConfig().GetTESTautomatic())
              JExpertFrame.this.repaint();
          }
    	  MainKBCT.getConfig().SetMaxTreeDepth(JExpertFrame.this.Temp_kbct.GetNbInputs());
        }
        public void OutputRemoved( int output_number ) {
          int NbRules= JExpertFrame.this.Temp_kbct.GetNbRules();
          JExpertFrame.this.NbOut= JExpertFrame.this.Temp_kbct.GetNbOutputs()-1;
            if (NbRules>0)
            for (int n=0; n<NbRules;n++) {
               Rule r= JExpertFrame.this.Temp_kbct.GetRule(n+1);
               int[] out_labels_number= r.Get_out_labels_number();
               int r_N_outputs= r.GetNbOutputs();
               int[] out_labels_new= new int[r_N_outputs-1];
               for (int m=0;m<r_N_outputs;m++)
                 if (m+1<output_number)
                   out_labels_new[m]= out_labels_number[m];
                 else if (m+1>output_number)
                   out_labels_new[m-1]= out_labels_number[m];

               Rule r_new= new Rule(n+1, r.GetNbInputs(), r_N_outputs-1, r.Get_in_labels_number(), out_labels_new, r.GetType(), r.GetActive());
               JExpertFrame.this.Temp_kbct.ReplaceRule(n+1,r_new);
            }
          if ((JExpertFrame.this.Temp_kbct.GetNbInputs()>0)&&(JExpertFrame.this.Temp_kbct.GetNbOutputs()>0))
               JExpertFrame.this.ReInitTableRules();
          else if (JExpertFrame.this.jPanelRules != null) {
               for (int n=0; n<NbRules; n++)
                 JExpertFrame.this.Temp_kbct.RemoveRule(0);

               JExpertFrame.this.getContentPane().remove(JExpertFrame.this.jPanelRules);
               JExpertFrame.this.pack();
           	   if (!MainKBCT.getConfig().GetTESTautomatic())
                  JExpertFrame.this.repaint();
          }
        }
        public void InputAdded( int input_number ) {
          int NbRules= JExpertFrame.this.Temp_kbct.GetNbRules();
          JExpertFrame.this.NbIn= JExpertFrame.this.Temp_kbct.GetNbInputs()+1;
          if (NbRules>0)
            for (int n = 0; n < NbRules; n++) {
              Rule r = JExpertFrame.this.Temp_kbct.GetRule(n + 1);
              int[] in_labels_number = r.Get_in_labels_number();
              int r_N_inputs = r.GetNbInputs();
              int[] in_labels_new = new int[r_N_inputs + 1];
              for (int m = 0; m < r_N_inputs; m++)
                in_labels_new[m] = in_labels_number[m];

              in_labels_new[r_N_inputs] = 0;
              Rule r_new = new Rule(n + 1, r_N_inputs + 1, r.GetNbOutputs(), in_labels_new, r.Get_out_labels_number(), r.GetType(), r.GetActive());
              JExpertFrame.this.Temp_kbct.ReplaceRule(n + 1, r_new);
            }
          if ((JExpertFrame.this.Temp_kbct.GetNbInputs()>0)&&(JExpertFrame.this.Temp_kbct.GetNbOutputs()>0))
               JExpertFrame.this.ReInitTableRules();

          //MainKBCT.getConfig().SetMaxTreeDepth(JExpertFrame.this.Temp_kbct.GetNbInputs());
        }
        public void OutputAdded( int output_number ) {
          int NbRules= JExpertFrame.this.Temp_kbct.GetNbRules();
          JExpertFrame.this.NbOut= JExpertFrame.this.Temp_kbct.GetNbOutputs()+1;
          if (NbRules>0)
            for (int n = 0; n < NbRules; n++) {
              Rule r = JExpertFrame.this.Temp_kbct.GetRule(n + 1);
              int[] out_labels_number = r.Get_out_labels_number();
              int r_N_outputs = r.GetNbOutputs();
              int[] out_labels_new = new int[r_N_outputs + 1];
              for (int m = 0; m < r_N_outputs; m++)
                out_labels_new[m] = out_labels_number[m];

              out_labels_new[r_N_outputs] = 0;
              Rule r_new = new Rule(n + 1, r.GetNbInputs(), r_N_outputs + 1, r.Get_in_labels_number(), out_labels_new, r.GetType(), r.GetActive());
              JExpertFrame.this.Temp_kbct.ReplaceRule(n + 1, r_new);
            }
          if ((JExpertFrame.this.Temp_kbct.GetNbInputs()>0)&&(JExpertFrame.this.Temp_kbct.GetNbOutputs()>0))
               JExpertFrame.this.ReInitTableRules();
        }
        public void MFRemovedInInput( int input_number, int mf_removed ) {
          JExpertFrame.this.UpdateRules(input_number,"INPUT", mf_removed+1);
          if ((JExpertFrame.this.Temp_kbct.GetNbInputs()>0)&&(JExpertFrame.this.Temp_kbct.GetNbOutputs()>0))
               JExpertFrame.this.ReInitTableRules();
        }
        public void MFRemovedInOutput( int output_number, int mf_removed ) {
          JExpertFrame.this.UpdateRules(output_number,"OUTPUT", mf_removed+1);
          if ((JExpertFrame.this.Temp_kbct.GetNbInputs()>0)&&(JExpertFrame.this.Temp_kbct.GetNbOutputs()>0))
               JExpertFrame.this.ReInitTableRules();
        }
        public void MFAddedInInput( int input_number ) {
          if ((JExpertFrame.this.Temp_kbct.GetNbInputs()>0)&&(JExpertFrame.this.Temp_kbct.GetNbOutputs()>0))
               JExpertFrame.this.ReInitTableRules();
        }
        public void MFAddedInOutput( int output_number ) {
          if ((JExpertFrame.this.Temp_kbct.GetNbInputs()>0)&&(JExpertFrame.this.Temp_kbct.GetNbOutputs()>0))
               JExpertFrame.this.ReInitTableRules();
        }
        public void MFReplacedInInput( int input_number ) {
          if ((JExpertFrame.this.Temp_kbct.GetNbInputs()>0)&&(JExpertFrame.this.Temp_kbct.GetNbOutputs()>0))
               JExpertFrame.this.ReInitTableRules();
        }
        public void MFReplacedInOutput( int output_number ) {
          if ((JExpertFrame.this.Temp_kbct.GetNbInputs()>0)&&(JExpertFrame.this.Temp_kbct.GetNbOutputs()>0))
               JExpertFrame.this.ReInitTableRules();
        }
        public void OutputDefaultChanged( int output_number ) {}
        public void RulesModified() {}
      };
      this.Temp_kbct.AddKBCTListener(this.KBCTListener);
    }
  }
//------------------------------------------------------------------------------
  void jMenuNew_actionPerformed() {
    if( this.Close() == false )
      return;
    try {
     this.Temp_kbct = new JKBCT();
     this.kbct= new JKBCT(this.Temp_kbct);
     this.InitJExpertFrameWithKBCT();
    } catch( Throwable t ) {
      t.printStackTrace();
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: jMenuNew_actionPerformed: "+t);
      if( this.Temp_kbct != null ) {
        this.Temp_kbct.Delete();
        this.kbct.Delete();
      }
      this.Temp_kbct = null;
      this.kbct = null;
      this.InitJExpertFrame();
    }
  }
//------------------------------------------------------------------------------
  void jMenuOpen_actionPerformed(ActionEvent e) {
    JKBCT new_kbct = null;
    JOpenFileChooser file_chooser = new JOpenFileChooser(MainKBCT.getConfig().GetKBCTFilePath());
    file_chooser.setAcceptAllFileFilterUsed(true);
    JFileFilter filter1 = new JFileFilter(("KB").toLowerCase(), LocaleKBCT.GetString("FilterKBFile"));
    JFileFilter filter2 = new JFileFilter(("XML").toLowerCase(), LocaleKBCT.GetString("FilterKBFile"));
    file_chooser.addChoosableFileFilter(filter1);
    file_chooser.addChoosableFileFilter(filter2);
    file_chooser.setFileFilter(filter2);
    JCheckBox jcb = new JCheckBox(LocaleKBCT.GetString("OpenInNewWindow"), false);
    file_chooser.AddComponent(jcb);
    if( file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ) {
      MainKBCT.getConfig().SetKBCTFilePath(file_chooser.getSelectedFile().getParent());
      if( jcb.isSelected() == true ) {
        // apertura de una nueva ventana
        new JExpertFrame( file_chooser.getSelectedFile().getAbsolutePath(), this.Parent, null );
      } else {
        try {
          new_kbct = new JKBCT( file_chooser.getSelectedFile().getAbsolutePath() );
          if( this.Close() == false )
            return;

          this.kbct= new_kbct;
          this.Temp_kbct= new JKBCT(this.kbct);
          this.InitJExpertFrameWithKBCT();
        } catch( Exception exception ) {
            exception.printStackTrace();
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: jMenuOpen_actionPerformed 1: "+exception);
            if( new_kbct != null ) new_kbct.Delete();
              new_kbct = null;
        } catch( Throwable t ) {
            t.printStackTrace();
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: jMenuOpen_actionPerformed 2: "+t);
            if( this.Temp_kbct != null ) {
              this.Temp_kbct.Delete();
              this.kbct.Delete();
            }
            if( new_kbct != null )
              new_kbct.Delete();

            this.kbct = null;
            this.Temp_kbct = null;
            new_kbct = null;
            this.InitJExpertFrame();
        }
      }
    }
  }
//------------------------------------------------------------------------------
  void jMenuSetFISoptions_actionPerformed() {
    JConvert.SetFISoptions(this.Temp_kbct, this);
  }
//------------------------------------------------------------------------------
  void jMenuConvert_actionPerformed() {
    try {
        JConvertFrame jcf= new JConvertFrame(this);
        jcf.setVisible(true);
    } catch( Throwable ex ) {
        ex.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: jMenuConvert_actionPerformed: "+ex);
    }
  }
//------------------------------------------------------------------------------
  void jMenuLoadOntology_actionPerformed() { 
	    try {
	    	JExpertFrame.jowlf= new JOntologyFrame(this, this.Temp_kbct);
	        if ( (JExpertFrame.jowlf!=null) && (!JExpertFrame.jowlf.getWarning()) ) {
	        	  JExpertFrame.jowlf.setVisible(true);
	        } //else
	        	//System.out.println("jowlf null or warning");
	       }
	       catch( Throwable ex ) {
	         ex.printStackTrace();
	         MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: jMenuLoadOntology_actionPerformed: "+ex);
        }
  }	       
//------------------------------------------------------------------------------
  void jMenuSaveAsFuzzyOntology_actionPerformed() { 
      try {
    	  File f= new File(JKBCTFrame.KBExpertFile);
    	  String outFile= f.getParent()+System.getProperty("file.separator")+"FuzzyVar"+f.getName()+".owl";
    	  String name=JKBCTFrame.OntologyFile;
    	  //System.out.println("name -> "+JKBCTFrame.OntologyFile);
          /*if ( (JKBCTFrame.OntologyFile==null) || (JKBCTFrame.OntologyFile.equals("")) ) {
        	  //name=MainKBCT.getConfig().GetOntologyPath()+System.getProperty("file.separator")+"newOnt"+Math.random()+".owl";
        	  name=MainKBCT.getConfig().GetOntologyPath()+System.getProperty("file.separator")+"newOnt.import.rules.owl";
          }*/
          //name= name +".import.rules.owl";
          File font= new File(name);
		  String fName= font.getName();
  		  String fileOntName= fName+".import.owl";
  		  //System.out.println("fileOntName="+fileOntName);
  		  //String fileOntName= font.getName();
  		  File ffuzzy= new File(outFile);
		  String ffName= ffuzzy.getName();
    	  PrintWriter fOut = new PrintWriter(new FileOutputStream(outFile), true);
          OWLwriter ow= new OWLwriter(fOut);
          String outfvFile= f.getParent()+System.getProperty("file.separator")+"FuzzyVar.owl";
  		  //System.out.println("outfvFile="+outfvFile);
    	  PrintWriter ffOut = new PrintWriter(new FileOutputStream(outfvFile), true);
    	  if (this.Temp_kbct.GetNbRules() > 0) {
        	  this.SaveOntVars(ow, ffOut, outFile, true);
    	      this.SaveOntRules(ow, fileOntName, ffName);
    	  } else {
        	  this.SaveOntVars(ow, ffOut, outFile, false);
    	  }
          ow.OWLclose();
      } catch (Exception e) {
	         e.printStackTrace();
	         MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: jMenuSaveAsFuzzyOntologyWithoutRules_actionPerformed: "+e);
      }
  }
//------------------------------------------------------------------------------
  void jMenuSaveAsFuzzyOntologyWithoutRules_actionPerformed() { 
      //String ontFile= JKBCTFrame.OntologyFile;
      try {
   		  //PrintWriter fOut = new PrintWriter(new FileOutputStream(ontFile+".kb.owl"), true);
    	  File f= new File(JKBCTFrame.KBExpertFile);
    	  String outFile= f.getParent()+System.getProperty("file.separator")+"FuzzyVar"+f.getName()+".owl";
    	  //System.out.println("outFile="+outFile);
    	  PrintWriter fOut = new PrintWriter(new FileOutputStream(outFile), true);
          //LineNumberReader lnr= new LineNumberReader(new InputStreamReader(new FileInputStream(ontFile)));
	      //String l;
	      //while((l= lnr.readLine())!=null) {
	    	//    fOut.println(l);
	      //}
          OWLwriter ow= new OWLwriter(fOut);
          String outfvFile= f.getParent()+System.getProperty("file.separator")+"FuzzyVar.owl";
    	  PrintWriter ffOut = new PrintWriter(new FileOutputStream(outfvFile), true);
    	  //String name=JKBCTFrame.OntologyFile;
          //if (JKBCTFrame.OntologyFile==null) {
        	//  name=MainKBCT.getConfig().GetOntologyPath()+System.getProperty("file.separator")+"newOnt.import.rules.owl";
          //}
  		  //File font= new File(name);
		  //String fName= font.getName();
  		  //String fileOntName= fName+".import.owl";
          this.SaveOntVars(ow, ffOut, outFile, false);
          ow.OWLclose();
      } catch (Exception e) {
	         e.printStackTrace();
	         MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: jMenuSaveAsFuzzyOntologyWithoutRules_actionPerformed: "+e);
      }
	  //this.jMenuSaveAsFuzzyOntologyWithoutRules_actionPerformed();
      //System.out.println(MainKBCT.getConfig().GetOntologyPath());
  }
//------------------------------------------------------------------------------
  private void SaveOntVars(OWLwriter ow, PrintWriter ffOut, String outFile, boolean rules) {
	  ow.OWLwriteFuzzyVar(ffOut);
	  ow.OWLwriteHeader(outFile);
	  Vector<String> vnames= new Vector<String>();
	  int limin= this.getKBCTTemp().GetNbInputs();
	  for (int n=0; n<limin; n++) {
	       JKBCTInput jin= this.Temp_kbct.GetInput(n+1);
	       boolean ont= jin.GetV().GetFlagOntology();
	           if (ont) {
	           String name= jin.GetV().GetNameFromOntology();
	        	   //System.out.println("input --> NameOnt= "+name);  
	        	   ow.OWLwriteClass(name);
	        	   vnames.add(name);
	           }
	  }
	  int limout= this.Temp_kbct.GetNbOutputs();
	  for (int n=0; n<limout; n++) {
	   	   JKBCTOutput jout= this.Temp_kbct.GetOutput(n+1);
	   	   boolean ont= jout.GetV().GetFlagOntology();
	       if (ont) {
	   	       String name= jout.GetV().GetNameFromOntology();
               ow.OWLwriteClass(name);
	       	   vnames.add(name);
	       }
	  }
	  //<rdf:Property rdf:about="http://www.um.es/OntoRep/FuzzyVar_1_0.owl#mp"/>
	  //this.OWLwriteFunctionalProperty(fOut, "");
      ow.OWLwriteFunctionalProperty("nameVar");
      ow.OWLwriteFunctionalProperty("physicalLowerRange");
      ow.OWLwriteFunctionalProperty("physicalUpperRange");
      ow.OWLwriteFunctionalProperty("interestLowerRange");
      ow.OWLwriteFunctionalProperty("interestUpperRange");
      ow.OWLwriteFunctionalProperty("trust");
      ow.OWLwriteFunctionalProperty("classif");
      ow.OWLwriteFunctionalProperty("scaleOfLabels");
      ow.OWLwriteFunctionalProperty("numLabels");
      ow.OWLwriteFunctionalProperty("isLabelOf");
      ow.OWLwriteFunctionalProperty("nameLab");
      ow.OWLwriteFunctionalProperty("mp");
      ow.OWLwriteFunctionalProperty("mf");
      ow.OWLwriteFunctionalProperty("p1");
      ow.OWLwriteFunctionalProperty("p2");
      ow.OWLwriteFunctionalProperty("p3");
      ow.OWLwriteFunctionalProperty("p4");
      ow.OWLwriteInverseFunctionalProperty("label");
	  for (int n=0; n<limin; n++) {
	           JKBCTInput jin= this.Temp_kbct.GetInput(n+1);
		       variable v= jin.GetV();
	           boolean ont= v.GetFlagOntology();
	           if (ont) {
		    	   if (v.GetFlagOntObjectProperty() || v.GetFlagOntDatatypeProperty())
	        	       ow.OWLwriteVariable(jin, false);
	           }
	      }
	  for (int n=0; n<limout; n++) {
	       JKBCTOutput jout= this.Temp_kbct.GetOutput(n+1);
	   	   //String name= jout.GetName();
	       variable v= jout.GetV();
	   	   boolean ont= v.GetFlagOntology();
	       if (ont) {
	    	   if (v.GetFlagOntObjectProperty() || v.GetFlagOntDatatypeProperty())
	       	       ow.OWLwriteVariable(jout, true);
	           //System.out.println("output --> Name= "+name);  
	           //System.out.println("output --> NameOnt= "+jout.GetV().GetNameFromOntology());  
	       }
	  }
	  String name=JKBCTFrame.OntologyFile;
      if (JKBCTFrame.OntologyFile==null) {
    	  //name=MainKBCT.getConfig().GetOntologyPath()+System.getProperty("file.separator")+"newOnt"+Math.random()+".owl";
    	  name=MainKBCT.getConfig().GetOntologyPath()+System.getProperty("file.separator")+"newOnt.import.owl";
      }
	  //System.out.println("ontFile="+name);
      Object[] obj= vnames.toArray();
      String[] ontnames= new String[obj.length];
      for (int n=0; n<obj.length; n++) {
    	  ontnames[n]= obj[n].toString();
    	  //System.out.println("ontnames[n]="+ontnames[n]);
      }
      ow.OWLclose();
	  ow.OWLwriteVarOntology(name, ontnames, outFile, rules);
  }
//------------------------------------------------------------------------------
  private void SaveOntRules(OWLwriter ow, String fileOntName, String fileFuzzyName) {
	  ow.OWLwriteRulesOntology(this.Temp_kbct, fileOntName, fileFuzzyName);
  }
//------------------------------------------------------------------------------
  void jMenuExit_actionPerformed() { 
	  this.dispose();
  }
//------------------------------------------------------------------------------
  private boolean Close() {
	//System.out.println("JEF: Close():1: this.Temp_kbct -> "+this.Temp_kbct.GetPtr());
	//System.out.println("JEF: Close():2: this.kbct -> "+this.kbct.GetPtr());
    if( this.Temp_kbct != null ) {
      try {
        if( (this.Temp_kbct.Modified() == true) ||
                  (!jnikbct.EqualKBCT(this.kbct.GetPtr(),this.Temp_kbct.GetPtr())) ) {
          int opt;
          if ( (MainKBCT.getConfig().GetTESTautomatic()) || (MainKBCT.getConfig().GetTutorialFlag()) )
        	  opt= JOptionPane.NO_OPTION;
          else
        	  opt= MessageKBCT.Confirm(this, LocaleKBCT.GetString("TheKBCTIsNotSaved") + "\n" + LocaleKBCT.GetString("DoYouWantToSaveIt"), 0, false, false, false);

          if( opt == JOptionPane.YES_OPTION ) {
            boolean warning1= false;
            boolean warning2= false;

            if ( (this.Parent.kbct_Data!=null) && ( (this.DataFileModificationSimplify) || (this.Parent.kbct_Data.Modified()) ) && (this.Parent.DataFileNoSaved!=null) ) {
                warning1= true;
                //System.out.println("this.DataFileModificationSimplify=" +this.DataFileModificationSimplify);
                //System.out.println("this.Parent.kbct_Data.Modified()=" +this.Parent.kbct_Data.Modified());
                String msg= LocaleKBCT.GetString("TheDataFileIsNotSaved") + "\n" +
                            LocaleKBCT.GetString("WarningSaveDataFile") + "\n" +
                            LocaleKBCT.GetString("DoYouWantToContinue");
                if( MessageKBCT.Confirm(this, msg, 0, false, false, false) == JOptionPane.YES_OPTION ) {
                  warning2= true;
                  String DFile=this.Parent.DataFile.FileName();
                  String DFileNoSaved=this.Parent.DataFileNoSaved.FileName();
                  File f= new File(DFile);
                  PrintWriter fOut = new PrintWriter(new FileOutputStream(f, false));
                  LineNumberReader lnr= new LineNumberReader(new InputStreamReader(new FileInputStream(DFileNoSaved)));
                  String l;
                  while((l= lnr.readLine())!=null)
                    fOut.println(l);

                  fOut.flush();
                  fOut.close();
                  this.Parent.DataFile.Reload(DFile);
                  this.Parent.DataFileNoSaved.Close();
                  this.Parent.DataFileNoSaved= null;
                  this.DataFileModificationSimplify= false;
                } else {
                  if (this.Parent.variables.toArray().length!=this.Parent.OLD_variables.toArray().length) {
                     this.Parent.variables= this.Parent.OLD_variables;
                  }
                }
            }
            if ( (!warning1) || (warning1 && warning2) ) {
              if( this.Temp_kbct.GetKBCTFile() == null ) {
                if( this.SaveAs() == false )
                  return false;
              } else
                 this.auxSave();
            }
          } else {
            /////////////
            this.DataFileModificationSimplify= false;
            this.Parent.DataFileModificationSimplify= false;
            /////////////
        	//System.out.println(" -> kbct="+this.kbct.GetPtr());
        	//System.out.println(" -> kbctTemp="+this.Temp_kbct.GetPtr());
            //System.out.println(" -> htsize="+jnikbct.getHashtableSize());
      	    //System.out.println("JEF: Close():2: this.Temp_kbct -> "+this.Temp_kbct.GetPtr());
            if (this.Temp_kbct!=null) {
                long[] exc= new long[8];
                exc[0]= this.Temp_kbct.GetPtr();
	            exc[1]= this.Temp_kbct.GetCopyPtr();
                if (this.Parent.kbct_Data!=null) {
                	exc[2]= this.Parent.kbct_Data.GetPtr();
                	exc[3]= this.Parent.kbct_Data.GetCopyPtr();
                } else {
                	exc[2]= -1;
                	exc[3]= -1;
                }
                if (this.Parent.kbct!=null) {
                	exc[4]= this.Parent.kbct.GetPtr();
                	exc[5]= this.Parent.kbct.GetCopyPtr();
                } else {
                	exc[4]= -1;
                	exc[5]= -1;
                }
                if (this.kbct!=null) {
                	exc[6]= this.kbct.GetPtr();
                	exc[7]= this.kbct.GetCopyPtr();
                } else {
                	exc[6]= -1;
                	exc[7]= -1;
                }
           	    jnikbct.cleanHashtable(this.Temp_kbct.GetPtr()+1,exc);
          	    //System.out.println("JEF: Close():3: this.Temp_kbct -> "+this.Temp_kbct.GetPtr());
           	    //System.out.println(" -> htsize="+jnikbct.getHashtableSize());
           	    //System.out.println(" -> "+this.Temp_kbct.GetKBCTFile());
           	    //if (jnikbct.getHashtableSize()==-1) 
           	    	//System.out.println("ht NULL");

           	    //System.out.println("Save kbct file -> "+this.Temp_kbct.GetKBCTFile());
           	    if ( (MainKBCT.getConfig().GetTESTautomatic()) || (MainKBCT.getConfig().GetTutorialFlag()) ) {
           	        this.kbct.Save(this.Temp_kbct.GetKBCTFile(), false);
                    this.kbct.Save(this.kbct.GetKBCTFile(), false);
                    this.Temp_kbct= new JKBCT(this.Temp_kbct.GetKBCTFile());
                    this.Temp_kbct.AddKBCTListener(this.KBCTListener);
           	    }
            }
      	    //System.out.println("JEF: Close():4: this.Temp_kbct -> "+this.Temp_kbct.GetPtr());
            this.Parent.kbct= new JKBCT(this.Temp_kbct.GetKBCTFile());
            //System.out.println("JEF: close() -> this.Parent.kbct: "+this.Parent.kbct.GetPtr());
            this.Parent.kbct.SetKBCTFile(this.kbct.GetKBCTFile());
            if ( (this.Parent.variables!=null) && (this.Parent.variables.toArray().length!=this.Parent.OLD_variables.toArray().length) ) {
               this.Parent.variables= this.Parent.OLD_variables;
            }
            //////////
      	    //System.out.println("JEF: Close():5: this.Temp_kbct -> "+this.Temp_kbct.GetPtr());
            if ( (this.Parent.kbct_Data!=null) && !(this.Parent.IKBFile.equals("")) ) {
                 this.Parent.ReloadData();
            }
      	    //System.out.println("JEF: Close():6: this.Temp_kbct -> "+this.Temp_kbct.GetPtr());
            if (!LocaleKBCT.isWindowsPlatform())
            	this.Parent.jef.dispose();
            //////////
      	    //System.out.println("JEF: Close():7: this.Temp_kbct -> "+this.Temp_kbct.GetPtr());
          }
        }
  	    //System.out.println("JEF: Close():8: this.Temp_kbct -> "+this.Temp_kbct.GetPtr());
        if (JExpertFrame.jcf!=null) {
        	JExpertFrame.jcf.dispose();
            JKBCTFrame.RemoveTranslatable(JExpertFrame.jcf);
            JExpertFrame.jcf= null;
        }
  	    //System.out.println("JEF: Close():9: this.Temp_kbct -> "+this.Temp_kbct.GetPtr());
        if (this.jfcRuleIntWeights!=null) {
        	this.jfcRuleIntWeights.dispose();
            JKBCTFrame.RemoveTranslatable(this.jfcRuleIntWeights);
            this.jfcRuleIntWeights= null;
        }
  	    //System.out.println("JEF: Close():10: this.Temp_kbct -> "+this.Temp_kbct.GetPtr());
        if (this.jfcRuleWeights!=null) {
        	this.jfcRuleWeights.dispose();
            JKBCTFrame.RemoveTranslatable(this.jfcRuleWeights);
            this.jfcRuleWeights= null;
        }
  	    //System.out.println("JEF: Close():11: this.Temp_kbct -> "+this.Temp_kbct.GetPtr());
        if (this.jfcLSC!=null) {
        	this.jfcLSC.dispose();
            JKBCTFrame.RemoveTranslatable(this.jfcLSC);
            this.jfcLSC= null;
        }
  	    //System.out.println("JEF: Close():12: this.Temp_kbct -> "+this.Temp_kbct.GetPtr());
        if (this.jfcOF!=null) {
        	this.jfcOF.dispose();
            JKBCTFrame.RemoveTranslatable(this.jfcOF);
            this.jfcOF= null;
        }
  	    //System.out.println("JEF: Close():13: this.Temp_kbct -> "+this.Temp_kbct.GetPtr());
        if (this.jfcCompleteness!=null) {
        	this.jfcCompleteness.dispose();
            JKBCTFrame.RemoveTranslatable(this.jfcCompleteness);
            this.jfcCompleteness= null;
        }
  	    //System.out.println("JEF: Close():14: this.Temp_kbct -> "+this.Temp_kbct.GetPtr());
        if (this.jfcLinguisticSummary!=null) {
        	this.jfcLinguisticSummary.dispose();
            JKBCTFrame.RemoveTranslatable(this.jfcLinguisticSummary);
            this.jfcLinguisticSummary= null;
        }
  	    //System.out.println("JEF: Close():15: this.Temp_kbct -> "+this.Temp_kbct.GetPtr());
        if (this.jfcLogView!=null) {
        	this.jfcLogView.dispose();
            JKBCTFrame.RemoveTranslatable(this.jfcLogView);
            this.jfcLogView= null;
        }
  	    //System.out.println("JEF: Close():16: this.Temp_kbct -> "+this.Temp_kbct.GetPtr());
        if (JExpertFrame.jowlf!=null) {
        	JExpertFrame.jowlf.dispose();
            JKBCTFrame.RemoveTranslatable(JExpertFrame.jowlf);
            JExpertFrame.jowlf= null;
        }
  	    //System.out.println("JEF: Close():17: this.Temp_kbct -> "+this.Temp_kbct.GetPtr());
        if (this.jrbqf!=null) {
        	this.jrbqf.dispose();
            JKBCTFrame.RemoveTranslatable(this.jrbqf);
            this.jrbqf= null;
        }
        if (this.jrbqfTut!=null) {
        	this.jrbqfTut.dispose();
            JKBCTFrame.RemoveTranslatable(this.jrbqfTut);
            this.jrbqfTut= null;
        }
		if (this.jdSVG != null) {
			this.jdSVG.dispose();
		}
        if (JKBCTFrame.fingrams != null) {
        	for (int n=0; n<JKBCTFrame.fingrams.length; n++) {
        		 if (JKBCTFrame.fingrams[n]!=null) {
        			 JKBCTFrame.fingrams[n].dispose();
        		 }
        	}
        }
      } catch( Throwable except ) {
        except.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: Close(): "+except);
        return false;
      }
    }
    return true;
  }
//------------------------------------------------------------------------------
  public void auxSave() throws Throwable {
    String aux= this.kbct.GetKBCTFile();
    this.Temp_kbct.Save();
    this.kbct= new JKBCT(this.Temp_kbct);
    this.kbct.SetKBCTFile(aux);
    this.kbct.Save();
    int NbOutputs= this.kbct.GetNbOutputs();
    String[] OutputTypes= null;
    if (NbOutputs >0) {
      OutputTypes= new String[NbOutputs];
      for (int n=0; n<NbOutputs; n++)
        OutputTypes[n]= this.kbct.GetOutput(n+1).GetType();
    }
    if (JConvert.conjunction==null || JConvert.disjunction==null || JConvert.defuzzification==null)        
    	JConvert.SetFISoptionsDefault(NbOutputs, OutputTypes);
  }
//------------------------------------------------------------------------------
  public void disposeOpen() {
    JKBCTFrame.JExpertFrames.removeElement(this);
    JKBCTFrame.RemoveTranslatable(this);
    super.dispose();
    if (this.JRuleFrameOpen.Permits()==0)
      this.JRuleFrameOpen.Release();
  }
//------------------------------------------------------------------------------
  public void dispose() {
    MainKBCT.getConfig().SetFingramsSelectedSample(LocaleKBCT.DefaultFingramsSelectedSample());
    if( this.Close() == false )
      return;

    this.kbct_Data= null;
    JKBCTFrame.JExpertFrames.removeElement(this);
    JKBCTFrame.RemoveTranslatable(this);

    super.dispose();

    if (this.JRuleFrameOpen.Permits()==0) {
      this.JRuleFrameOpen.Release();
    }
    if (this.getExtendedState()==Frame.MAXIMIZED_BOTH) {
        this.clossing= true;
    } else {
        this.clossing= false;
    }
  }
//------------------------------------------------------------------------------
  public void jMenuClose_actionPerformed() { 
	  this.dispose();
  }
//------------------------------------------------------------------------------
  void jMenuSave_actionPerformed() { this.Save(); }
//------------------------------------------------------------------------------
  private void Save() {
    try {
      boolean warning1= false;
      boolean warning2= false;
      if ( (this.Parent.kbct_Data!=null) && ( (this.DataFileModificationSimplify) || (this.Parent.kbct_Data.Modified()) ) && (this.Parent.DataFileNoSaved!=null) ) {
          //System.out.println("this.DataFileModificationSimplify= "+this.DataFileModificationSimplify);
          //System.out.println("this.Parent.kbct_Data.Modified()= "+this.Parent.kbct_Data.Modified());
    	  warning1= true;
          String msg= LocaleKBCT.GetString("TheDataFileIsNotSaved") + "\n" +
                      LocaleKBCT.GetString("WarningSaveDataFile") + "\n" +
                      LocaleKBCT.GetString("DoYouWantToContinue");
          if( (MainKBCT.getConfig().GetTutorialFlag()) || (MessageKBCT.Confirm(this, msg, 0, false, false, false) == JOptionPane.YES_OPTION ) ) {
            warning2= true;
            if (this.Parent.DataFileNoSaved!=null) {
            	// System.out.println("JExpertFrame: Save: this.Parent.DataFileNoSaved!=null");
              String DFile=this.Parent.DataFile.FileName();
              String DFileNoSaved=this.Parent.DataFileNoSaved.FileName();
              File f= new File(DFile);
              f.delete();
              PrintWriter fOut = new PrintWriter(new FileOutputStream(f, false));
              LineNumberReader lnr= new LineNumberReader(new InputStreamReader(new FileInputStream(DFileNoSaved)));
              String l;
              while((l= lnr.readLine())!=null)
                fOut.println(l);

              fOut.flush();
              fOut.close();
              this.Parent.DataFile= new JExtendedDataFile(DFile, true);
              this.Parent.DataFileNoSaved.Close();
              this.Parent.DataFileNoSaved= null;
              this.DataFileModificationSimplify= false;
            }
          }
      }
      if ( (!warning1) || (warning1 && warning2) )
        this.auxSave();
    } catch( Throwable except ) {
        except.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: jMenuSave: "+except);
    }
  }
//------------------------------------------------------------------------------
  private boolean SaveAs() throws Throwable {
    JFileChooser file_chooser = new JFileChooser(MainKBCT.getConfig().GetKBCTFilePath());
    file_chooser.setAcceptAllFileFilterUsed(false);
    JFileFilter filter = new JFileFilter(("XML").toLowerCase(), LocaleKBCT.GetString("FilterKBFile"));
    file_chooser.addChoosableFileFilter(filter);
    file_chooser.setSelectedFile(new File(MainKBCT.getConfig().GetKBCTFilePath() + System.getProperty("file.separator") + this.Temp_kbct.GetName().toLowerCase().replaceAll(" ","-") + "." + ("KB.XML").toLowerCase()));
    if( file_chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION ) {
      String file_name = file_chooser.getSelectedFile().getAbsolutePath();
      if( file_name.lastIndexOf('.') == -1 )
       file_name += '.' + ("KB.XML").toLowerCase();

      File f = new File( file_name );
      if( f.exists() ) {
        String message = LocaleKBCT.GetString("TheKBCTFile") + " : " + file_name + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");
        if( MessageKBCT.Confirm(this, message, 1, false, false, false) == JOptionPane.NO_OPTION )
          return false;
      } try {
          MainKBCT.getConfig().SetKBCTFilePath(file_chooser.getSelectedFile().getParent());
          this.kbct= new JKBCT(this.Temp_kbct);
          this.kbct.Save(file_name, false);
          JMainFrame.ChangeKBExpertFile(file_name);
          this.TranslateTitle();
          return true;
      } catch( Throwable t ) {
          t.printStackTrace();
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: SaveAs(): "+t);
      }
    }
    return false;
  }
//------------------------------------------------------------------------------
  private boolean SaveAsFIS() throws Throwable {
    JFileChooser file_chooser = new JFileChooser(MainKBCT.getConfig().GetKBCTFilePath());
    file_chooser.setAcceptAllFileFilterUsed(false);
    JFileFilter filter = new JFileFilter(("FIS").toLowerCase(), LocaleKBCT.GetString("FilterFISFile"));
    file_chooser.addChoosableFileFilter(filter);
    file_chooser.setSelectedFile(new File(MainKBCT.getConfig().GetKBCTFilePath() + System.getProperty("file.separator") + this.Temp_kbct.GetName().toLowerCase() + "." + ("FIS").toLowerCase()));
    if( file_chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION ) {
      String file_name = file_chooser.getSelectedFile().getAbsolutePath();
      if( file_name.lastIndexOf('.') == -1 )
       file_name += '.' + ("FIS").toLowerCase();

      File f = new File( file_name );
      if( f.exists() ) {
        String message = LocaleKBCT.GetString("TheFISFile") + " : " + file_name + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");
        if( MessageKBCT.Confirm(this, message, 1, false, false, false) == JOptionPane.NO_OPTION )
          return false;
        }
      try {
        MainKBCT.getConfig().SetKBCTFilePath(file_chooser.getSelectedFile().getParent());
        Temp_kbct.Save();
        Temp_kbct.SaveFIS(file_name);
        this.TranslateTitle();
        return true;
      } catch( Throwable t ) {
          t.printStackTrace();
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: SaveAsFIS 1: "+t);
      }
    }
    return false;
  }
//------------------------------------------------------------------------------
  boolean SaveAsFIS(JKBCT kbctaux) throws Throwable {
    JFileChooser file_chooser = new JFileChooser(MainKBCT.getConfig().GetKBCTFilePath());
    file_chooser.setAcceptAllFileFilterUsed(false);
    JFileFilter filter = new JFileFilter(("FIS").toLowerCase(), LocaleKBCT.GetString("FilterFISFile"));
    file_chooser.addChoosableFileFilter(filter);
    file_chooser.setSelectedFile(new File(MainKBCT.getConfig().GetKBCTFilePath() + System.getProperty("file.separator") + kbctaux.GetName().toLowerCase() + "." + ("FIS").toLowerCase()));
    if( file_chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION ) {
      String file_name = file_chooser.getSelectedFile().getAbsolutePath();
      if( file_name.lastIndexOf('.') == -1 )
       file_name += '.' + ("FIS").toLowerCase();

      File f = new File( file_name );
      if( f.exists() ) {
        String message = LocaleKBCT.GetString("TheFISFile") + " : " + file_name + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");
        if( MessageKBCT.Confirm(this, message, 1, false, false, false) == JOptionPane.NO_OPTION )
          return false;
      } try {
          MainKBCT.getConfig().SetKBCTFilePath(file_chooser.getSelectedFile().getParent());
          kbctaux.SaveFIS(file_name);
          return true;
      } catch( Throwable t ) {
          t.printStackTrace();
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: SaveAsFIS 2: "+t);
      }
    }
    return false;
  }
//------------------------------------------------------------------------------
  void jMenuSaveAs_actionPerformed() {
    try { SaveAs(); }
    catch( Throwable t ) {
      t.printStackTrace();
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: jMenuSaveAs: "+t);
    }
  }
//------------------------------------------------------------------------------
  void jMenuSaveAsFIS_actionPerformed() {
    try { SaveAsFIS(); }
    catch( Throwable t ) {
      t.printStackTrace();
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: jMenuSaveAsFIS: "+t);
    }
  }
//------------------------------------------------------------------------------
  private void NewInput() {
    try {
      variable v= new variable();
      JKBCTInput new_input = new JKBCTInput(v, this.Temp_kbct.GetNbInputs()+1);
      new_input.SetName(LocaleKBCT.GetString("Input") + " " + String.valueOf(this.Temp_kbct.GetNbInputs()+1));
      this.Temp_kbct.AddInput( new_input );
      this.InitJExpertFrameWithKBCT();
      JInputFrame jif = new JInputFrame(this, this.Temp_kbct, this.Temp_kbct.GetNbInputs()-1);
      jif.Show();
    } catch( Throwable en ) {
        en.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: NewInput: "+en);
    }
  }
//------------------------------------------------------------------------------
  private void EditInput() {
    try {
      int [] selected = this.jListInputs.getSelectedIndices();
      for( int i=0 ; i<selected.length ; i++ ) {
        JInputFrame jif = new JInputFrame(this, this.Temp_kbct, selected[i] );
        jif.Show();
      }
     } catch( Throwable t ) {
         t.printStackTrace();
         MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: EditInput: "+t);
     }
  }
//------------------------------------------------------------------------------
  private void RemoveInput() {
    try {
      int [] selected = this.jListInputs.getSelectedIndices();
      if ( (this.Parent.kbct_Data != null) && (this.Temp_kbct.GetNbInputs() == this.Parent.kbct_Data.GetNbInputs()) ) {
           for( int i=selected.length-1 ; i>=0 ; i-- ) {
             this.Parent.kbct_Data.RemoveInput(selected[i]+1);
             this.Parent.UpdateDataFile(selected[i], "REMOVE", 0);
             Object[] SelVar= this.Parent.variables.toArray();
             this.Parent.variables= new Vector();
             this.Parent.DataNbVariables--;
             for (int n=0; n<this.Parent.DataNbVariables; n++) {
               if (this.Parent.OrigKBDataFile==this.Parent.KBDataFile)
                   this.Parent.variables.add(new Integer(n));
               else {
                 if (n < selected[i])
                     this.Parent.variables.add(SelVar[n]);
                 else if (n >= selected[i])
                     this.Parent.variables.add(SelVar[n+1]);
               }
             }
           }
      }
      for( int i=selected.length-1 ; i>=0 ; i-- )
        this.Temp_kbct.RemoveInput(selected[i]+1);

      this.InitJExpertFrameWithKBCT();
    } catch( Throwable en ) {
        en.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: RemoveInput: "+en);
    }
  }
//------------------------------------------------------------------------------
  public void RemoveInput(int InputNumber, int mode) {
    //System.out.println("RemoveInput 1");
    try {
      if ( (this.Parent.kbct_Data != null) && (this.Temp_kbct.GetNbInputs() == this.Parent.kbct_Data.GetNbInputs()) ) {
    	   //System.out.println("RemoveInput 1.1");
           this.Parent.kbct_Data.RemoveInput(InputNumber);
   	       //System.out.println("RemoveInput 1.2");
           this.Parent.UpdateDataFile(InputNumber-1, "REMOVE", mode);
     	   //System.out.println("RemoveInput 1.3");
           Object[] SelVar= this.Parent.variables.toArray();
           this.Parent.variables= new Vector();
           this.Parent.DataNbVariables--;
   	       //System.out.println("RemoveInput 1.4");
           for (int n=0; n<this.Parent.DataNbVariables; n++) {
             if (this.Parent.OrigKBDataFile==this.Parent.KBDataFile)
                 this.Parent.variables.add(new Integer(n));
             else {
               if (n < InputNumber-1)
                   this.Parent.variables.add(SelVar[n]);
               else if (n >= InputNumber-1)
                   this.Parent.variables.add(SelVar[n+1]);
             }
           }
   	       //System.out.println("RemoveInput 1.5");
      }
      //System.out.println("RemoveInput 2");
      this.Temp_kbct.RemoveInput(InputNumber);
      //System.out.println("RemoveInput 3");
      this.InitJExpertFrameWithKBCT();
    } catch( Throwable en ) {
        en.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: RemoveInput: "+en);
    }
    //System.out.println("RemoveInput 4");
  }
//------------------------------------------------------------------------------
  void CopyInput() {
    try {
      int[] SelectedInputs= this.jListInputs.getSelectedIndices();
      for (int i=0; i<SelectedInputs.length; i++) {
         int SelIn= SelectedInputs[i];
         JKBCTInput SelInput= this.Temp_kbct.GetInput(SelIn+1);
         variable vSel= SelInput.GetV();
         int NbLab= vSel.GetLabelsNumber();
         String Scale= vSel.GetScaleName();
         variable vNew= new variable("("+LocaleKBCT.GetString("Copy")+")"+vSel.GetName(), vSel.GetType(), vSel.GetTrust(), vSel.GetClassif(), vSel.GetLowerPhysicalRange(), vSel.GetUpperPhysicalRange(), vSel.GetLowerInterestRange(), vSel.GetUpperInterestRange(), NbLab, Scale, vSel.isActive(), vSel.GetFlagModify());
         vNew.SetLabelProperties();
         String[] LabNames= vSel.GetLabelsName();
         String[] LabUserNames= vSel.GetUserLabelsName();
         vNew.InitLabelsName(LabNames.length);
         for (int n=0; n<NbLab; n++) {
             vNew.SetUserLabelsName(n+1, LabUserNames[n]);
         }
         for (int n=0; n<NbLab; n++) {
           vNew.SetLabelsName(n+1, LabNames[n]);
           LabelKBCT SelLab= vSel.GetLabel(n+1);
           double[] d= SelLab.GetParams();
           LabelKBCT NewLab= new LabelKBCT();
           switch(d.length) {
             case 1: NewLab= new LabelKBCT(SelLab.GetName(), SelLab.GetP1(), SelLab.GetLabel_Number()); break;
             case 2: NewLab= new LabelKBCT(SelLab.GetName(), SelLab.GetP1(), SelLab.GetP2(), SelLab.GetLabel_Number()); break;
             case 3: NewLab= new LabelKBCT(SelLab.GetName(), SelLab.GetP1(), SelLab.GetP2(), SelLab.GetP3(), SelLab.GetLabel_Number()); break;
             case 4: NewLab= new LabelKBCT(SelLab.GetName(), SelLab.GetP1(), SelLab.GetP2(), SelLab.GetP3(), SelLab.GetP4(), SelLab.GetLabel_Number()); break;
           }
           NewLab.SetMP(SelLab.GetMP());
           vNew.ReplaceLabel(n+1, NewLab);
           if (vSel.GetType().equals("logical") || vSel.GetType().equals("categorical"))
               vNew.SetMP(n+1, vSel.GetMP(n+1), true);
           else
               vNew.SetMP(n+1, vSel.GetMP(n+1), false);
         }
         vNew.SetORLabelsName(vSel.GetORLabelsName());
         vNew.SetScaleName(Scale);
         JKBCTInput new_input = new JKBCTInput(vNew, this.Temp_kbct.GetNbInputs()+1);
         this.Temp_kbct.AddInput( new_input );
         this.InitJExpertFrameWithKBCT();
      }
    } catch (Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);}
  }
//------------------------------------------------------------------------------
  void UpInput() {
    try {
      int[] SelectedInputs= this.jListInputs.getSelectedIndices();
      for (int i=0; i<SelectedInputs.length; i++) {
         int SelIn= SelectedInputs[i];
         JKBCTInput SelInput= this.Temp_kbct.GetInput(SelIn+1);
         JKBCTInput PrevInput= this.Temp_kbct.GetInput(SelIn);
         this.Temp_kbct.ReplaceInput(SelIn+1, SelInput);
         this.Temp_kbct.ReplaceInput(SelIn+2, PrevInput);
         if (this.Parent.kbct_Data!=null) {
           int DataNbInputs= this.Parent.kbct_Data.GetNbInputs();
           if ( DataNbInputs >= SelIn+1) {
             JKBCTInput SelInputData= this.Parent.kbct_Data.GetInput(SelIn+1);
             JKBCTInput PrevInputData= this.Parent.kbct_Data.GetInput(SelIn);
             this.Parent.kbct_Data.ReplaceInput(SelIn+1, SelInputData);
             this.Parent.kbct_Data.ReplaceInput(SelIn+2, PrevInputData);
             this.Parent.UpdateDataFile(SelIn, "UP", 0);
           }
         }
      }
      this.UpdateRuleBase( SelectedInputs[0], SelectedInputs.length, "UP", "INPUT" );
      this.InitJExpertFrameWithKBCT();
    } catch (Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);}
  }
//------------------------------------------------------------------------------
  void DownInput() {
    try {
      int[] SelectedInputs= this.jListInputs.getSelectedIndices();
      for (int i=SelectedInputs.length; i > 0; i--) {
         int SelIn= SelectedInputs[i-1];
         JKBCTInput SelInput= this.Temp_kbct.GetInput(SelIn+1);
         JKBCTInput NextInput= this.Temp_kbct.GetInput(SelIn+2);
         this.Temp_kbct.ReplaceInput(SelIn+2, NextInput);
         this.Temp_kbct.ReplaceInput(SelIn+3, SelInput);
         if (this.Parent.kbct_Data!=null) {
           int DataNbInputs= this.Parent.kbct_Data.GetNbInputs();
           if ( DataNbInputs >= SelIn+2) {
             JKBCTInput SelInputData= this.Parent.kbct_Data.GetInput(SelIn+1);
             JKBCTInput NextInputData= this.Parent.kbct_Data.GetInput(SelIn+2);
             this.Parent.kbct_Data.ReplaceInput(SelIn+2, NextInputData);
             this.Parent.kbct_Data.ReplaceInput(SelIn+3, SelInputData);
             this.Parent.UpdateDataFile(SelIn, "DOWN", 0);
           }
         }
      }
      this.UpdateRuleBase( SelectedInputs[0], SelectedInputs.length, "DOWN", "INPUT" );
      this.InitJExpertFrameWithKBCT();
    } catch (Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);}
  }
//------------------------------------------------------------------------------
  void jTFName_actionPerformed() {
    this.Temp_kbct.SetName(this.jTFName.getText());
  }
//------------------------------------------------------------------------------
  void jTFName_focusLost() {
    if (this.Temp_kbct!=null)
        this.Temp_kbct.SetName(this.jTFName.getText());
  }
//------------------------------------------------------------------------------
  private void NewOutput() {
    try {
      variable v= new variable();
      JKBCTOutput new_output = new JKBCTOutput(v, this.Temp_kbct.GetNbOutputs()+1);
      new_output.SetName(LocaleKBCT.GetString("Output") + " " + String.valueOf(this.Temp_kbct.GetNbOutputs()+1));
      this.Temp_kbct.AddOutput( new_output );
      this.InitJExpertFrameWithKBCT();
      if (!MainKBCT.getConfig().GetTutorialFlag()) {
          JOutputFrame jof = new JOutputFrame(this, this.Temp_kbct, this.Temp_kbct.GetNbOutputs()-1);
          jof.Show();
      }
    } catch( Throwable en ) {
        en.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: NewOutput: "+en);
    }
  }
//------------------------------------------------------------------------------
  private void RemoveOutput() {
    try {
      int [] selected = this.jListOutputs.getSelectedIndices();
      if ( (this.Parent.kbct_Data != null) && (this.Temp_kbct.GetNbOutputs() == this.Parent.kbct_Data.GetNbOutputs()) ) {
           for( int i=selected.length-1 ; i>=0 ; i-- ) {
             this.Parent.kbct_Data.RemoveOutput(selected[i] + 1);
             this.Parent.UpdateDataFile(selected[i]+this.kbct_Data.GetNbInputs(), "REMOVE", 0);
             Object[] SelVar= this.Parent.variables.toArray();
             this.Parent.variables= new Vector();
             this.Parent.DataNbVariables--;
             for (int n=0; n<this.Parent.DataNbVariables; n++) {
               if (this.Parent.OrigKBDataFile==this.Parent.KBDataFile)
                   this.Parent.variables.add(new Integer(n));
               else {
                 if (n < selected[i])
                     this.Parent.variables.add(SelVar[n]);
                 else if (n >= selected[i])
                     this.Parent.variables.add(SelVar[n+1]);
               }
             }
           }
      }
      for( int i=selected.length-1 ; i>=0 ; i-- )
        this.Temp_kbct.RemoveOutput(selected[i]+1);

      this.InitJExpertFrameWithKBCT();
    } catch( Throwable en ) {
        en.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: RemoveOutput: "+en);
    }
  }
//------------------------------------------------------------------------------
  public void RemoveOutput(int OutputNumber, int mode) {
    try {
      if ( (this.Parent.kbct_Data != null) && (this.Temp_kbct.GetNbOutputs() == this.Parent.kbct_Data.GetNbOutputs()) ) {
           this.Parent.kbct_Data.RemoveOutput(OutputNumber);
           this.Parent.UpdateDataFile(OutputNumber-1+this.kbct_Data.GetNbInputs(), "REMOVE", mode);
           Object[] SelVar= this.Parent.variables.toArray();
           this.Parent.variables= new Vector();
           this.Parent.DataNbVariables--;
           for (int n=0; n<this.Parent.DataNbVariables; n++) {
             if (this.Parent.OrigKBDataFile==this.Parent.KBDataFile)
                 this.Parent.variables.add(new Integer(n));
             else {
               if (n < OutputNumber-1+this.kbct_Data.GetNbInputs())
                   this.Parent.variables.add(SelVar[n]);
               else if (n >= OutputNumber-1+this.kbct_Data.GetNbInputs())
                   this.Parent.variables.add(SelVar[n+1]);
             }
           }
      }
      this.Temp_kbct.RemoveOutput(OutputNumber);
      this.InitJExpertFrameWithKBCT();
    } catch( Throwable en ) {
        en.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: RemoveOutput: "+en);
    }
  }
//------------------------------------------------------------------------------
  private void EditOutput() {
    try {
      int [] selected = this.jListOutputs.getSelectedIndices();
      for( int i=0 ; i<selected.length ; i++ ) {
           JOutputFrame jof = new JOutputFrame(this, this.Temp_kbct, selected[i] );
           jof.Show();
      }
    } catch( Throwable en ) {
        en.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: EditOutput: "+en);
    }
  }
//------------------------------------------------------------------------------
  void CopyOutput() {
    try {
      int[] SelectedOutputs= this.jListOutputs.getSelectedIndices();
      for (int i=0; i<SelectedOutputs.length; i++) {
         int SelOut= SelectedOutputs[i];
         JKBCTOutput SelOutput= this.Temp_kbct.GetOutput(SelOut+1);
         variable vSel= SelOutput.GetV();
         int NbLab= vSel.GetLabelsNumber();
         String Scale= vSel.GetScaleName();
         variable vNew= new variable("("+LocaleKBCT.GetString("Copy")+")"+vSel.GetName(), vSel.GetType(), vSel.GetTrust(), vSel.GetClassif(), vSel.GetLowerPhysicalRange(), vSel.GetUpperPhysicalRange(), vSel.GetLowerInterestRange(), vSel.GetUpperInterestRange(), NbLab, Scale, vSel.isActive(), vSel.GetFlagModify());
         vNew.SetLabelProperties();
         String[] LabNames= vSel.GetLabelsName();
         String[] LabUserNames= vSel.GetUserLabelsName();
         vNew.InitLabelsName(LabNames.length);
         for (int n=0; n<NbLab; n++) {
           vNew.SetUserLabelsName(n+1, LabUserNames[n]);
         }
         for (int n=0; n<NbLab; n++) {
           vNew.SetLabelsName(n+1, LabNames[n]);
           LabelKBCT SelLab= vSel.GetLabel(n+1);
           double[] d= SelLab.GetParams();
           LabelKBCT NewLab= new LabelKBCT();
           switch(d.length) {
             case 1: NewLab= new LabelKBCT(SelLab.GetName(), SelLab.GetP1(), SelLab.GetLabel_Number()); break;
             case 2: NewLab= new LabelKBCT(SelLab.GetName(), SelLab.GetP1(), SelLab.GetP2(), SelLab.GetLabel_Number()); break;
             case 3: NewLab= new LabelKBCT(SelLab.GetName(), SelLab.GetP1(), SelLab.GetP2(), SelLab.GetP3(), SelLab.GetLabel_Number()); break;
             case 4: NewLab= new LabelKBCT(SelLab.GetName(), SelLab.GetP1(), SelLab.GetP2(), SelLab.GetP3(), SelLab.GetP4(), SelLab.GetLabel_Number()); break;
           }
           NewLab.SetMP(SelLab.GetMP());
           vNew.ReplaceLabel(n+1, NewLab);
           if (vSel.GetType().equals("logical") || vSel.GetType().equals("categorical")) {
        	   String auxmp= vSel.GetMP(n+1);
        	   if (auxmp.equals("No MP"))
                   vNew.SetMP(n+1, String.valueOf(n+1), true);
        	   else
                   vNew.SetMP(n+1, auxmp, true);
        		   
           } else {
               vNew.SetMP(n+1, vSel.GetMP(n+1), false);
           }
         }
         vNew.SetORLabelsName(vSel.GetORLabelsName());
         vNew.SetScaleName(Scale);
         JKBCTOutput new_output = new JKBCTOutput(vNew, this.Temp_kbct.GetNbOutputs()+1);
         this.Temp_kbct.AddOutput( new_output );
         this.InitJExpertFrameWithKBCT();
      }
    } catch (Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);}
  }
//------------------------------------------------------------------------------
  void UpOutput() {
    try {
      int[] SelectedOutputs= this.jListOutputs.getSelectedIndices();
      for (int i=0; i<SelectedOutputs.length; i++) {
         int SelOut= SelectedOutputs[i];
         JKBCTOutput SelOutput= this.Temp_kbct.GetOutput(SelOut+1);
         JKBCTOutput PrevOutput= this.Temp_kbct.GetOutput(SelOut);
         this.Temp_kbct.ReplaceOutput(SelOut+1, SelOutput);
         this.Temp_kbct.ReplaceOutput(SelOut+2, PrevOutput);
         if (this.Parent.kbct_Data!=null) {
           int DataNbOutputs= this.Parent.kbct_Data.GetNbOutputs();
           if ( DataNbOutputs >= SelOut+1) {
             JKBCTOutput SelOutputData= this.Parent.kbct_Data.GetOutput(SelOut+1);
             JKBCTOutput PrevOutputData= this.Parent.kbct_Data.GetOutput(SelOut);
             this.Parent.kbct_Data.ReplaceOutput(SelOut+1, SelOutputData);
             this.Parent.kbct_Data.ReplaceOutput(SelOut+2, PrevOutputData);
             this.Parent.UpdateDataFile(SelOut, "UP", 0);
           }
         }
      }
      this.UpdateRuleBase( SelectedOutputs[0], SelectedOutputs.length, "UP", "OUTPUT" );
      this.InitJExpertFrameWithKBCT();
    } catch (Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);}
  }
//------------------------------------------------------------------------------
  void DownOutput() {
    try {
      int[] SelectedOutputs= this.jListOutputs.getSelectedIndices();
      for (int i=SelectedOutputs.length; i > 0; i--) {
         int SelOut= SelectedOutputs[i-1];
         JKBCTOutput SelOutput= this.Temp_kbct.GetOutput(SelOut+1);
         JKBCTOutput NextOutput= this.Temp_kbct.GetOutput(SelOut+2);
         this.Temp_kbct.ReplaceOutput(SelOut+2, NextOutput);
         this.Temp_kbct.ReplaceOutput(SelOut+3, SelOutput);
         if (this.Parent.kbct_Data!=null) {
           int DataNbOutputs= this.Parent.kbct_Data.GetNbOutputs();
           if ( DataNbOutputs >= SelOut+2) {
             JKBCTOutput SelOutputData= this.Parent.kbct_Data.GetOutput(SelOut+1);
             JKBCTOutput NextOutputData= this.Parent.kbct_Data.GetOutput(SelOut+2);
             this.Parent.kbct_Data.ReplaceOutput(SelOut+2, NextOutputData);
             this.Parent.kbct_Data.ReplaceOutput(SelOut+3, SelOutputData);
             this.Parent.UpdateDataFile(SelOut, "DOWN", 0);
           }
         }
      }
      this.UpdateRuleBase( SelectedOutputs[0], SelectedOutputs.length, "DOWN", "OUTPUT" );
      this.InitJExpertFrameWithKBCT();
    } catch (Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);}
  }
//------------------------------------------------------------------------------
  void jButtonFingrams_actionPerformed(boolean automatic, boolean[] selFing) {
	    int option=0;
	    if ( (!MainKBCT.getConfig().GetTutorialFlag()) && (!automatic) )
	        option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeFingrams"));
	    
 	    if (option==0) {
 	    	boolean fwos= true;
	        MainKBCT.getConfig().SetFingramWOS(fwos);
 	    	boolean fws= false;
	        MainKBCT.getConfig().SetFingramWS(fws);
 	    	//System.out.println("fws -> "+fws);
 	    	boolean cancel= false;
 	    	int[] SelectedRules= null;
 	    	boolean[] RulesActivation= null;
 	        if (fwos || fws) {
 	            SelectedRules= this.jTableRules.getSelectedRows();
 	            int lim= SelectedRules.length;
 	            if (lim > 1) {
 	            	int NbRules= this.Temp_kbct.GetNbRules();
 	            	RulesActivation= new boolean[NbRules];
 	            	for (int n=0; n<NbRules; n++) {
 	            		 RulesActivation[n]= this.Temp_kbct.GetRule(n+1).GetActive();
 	            		 this.Temp_kbct.SetRuleActive(n+1, false);
 	            	}
 	            	for (int n=0; n<lim; n++) {
	            		 this.Temp_kbct.SetRuleActive(SelectedRules[n]+1, RulesActivation[SelectedRules[n]]);
 	            	}
 	            	//System.out.println("NAR="+this.Temp_kbct.GetNbActiveRules());
 	            }
 	        	this.AssessingInterpretability(true);
 	        	if ( (!MainKBCT.getConfig().GetFINGRAMSautomatic()) && (!automatic) && (this.Temp_kbct!=null) ) {
 	        	    this.setPathFinderParameters((int)this.jkbif.getMaxSFR(),this.Temp_kbct.GetNbActiveRules());
 	        	} 
 	        	/*if ( (MainKBCT.getConfig().GetTutorialFlag()) && (this.Temp_kbct!=null) ) {
                    MainKBCT.getConfig().SetPathFinderParQ(this.Temp_kbct.GetNbActiveRules()-1); 	        		
 	        	}*/
 	        	Object ofm= MainKBCT.getConfig().GetFingramsMetric();
 	        	if ( (!MainKBCT.getConfig().GetFINGRAMSautomatic()) && (!automatic) )
 	        	    ofm= MessageKBCT.SelectFingramsMetric(this);
 	        	
 	        	if (ofm != null) {
			        String fm= ofm.toString();
				    //System.out.println("fm -> "+fm);
				    if ( (fm.equals("MS")) || (fm.equals(LocaleKBCT.GetString("MS"))) )
				        MainKBCT.getConfig().SetFingramsMetric("MS");
				    else if ( (fm.equals("MSFD")) || (fm.equals(LocaleKBCT.GetString("MSFD"))) )
				        MainKBCT.getConfig().SetFingramsMetric("MSFD");
				    else
				    	MainKBCT.getConfig().SetFingramsMetric("MA");
			        
			        //MainKBCT.getConfig().SetFingramsMetric(fm);
  			        // System.out.println("this.fingramsMetric -> "+fm);
			        Object ofl= MainKBCT.getConfig().GetFingramsLayout();
			        if ( (!MainKBCT.getConfig().GetFINGRAMSautomatic()) && (!automatic) )
			            ofl= MessageKBCT.SelectFingramsLayout(this);
			        
			        if (ofl!=null) {
			            String fl= ofl.toString();
			            MainKBCT.getConfig().SetFingramsLayout(fl);
			            //System.out.println("this.fingramsLayout -> "+fl);
			        } else {
			        	cancel= true;
			        }
 	        	} else {
 	        		cancel= true;
 	        	}
 	        }
 	        if (!cancel) {
 	          if ( (!MainKBCT.getConfig().GetFINGRAMSautomatic()) && (!automatic) ) {
 	              int selSample= MainKBCT.getConfig().GetFingramsSelectedSample();
 	              //System.out.println("selSample -> "+selSample);
                  if (selSample < 0) {
                    Integer op= (Integer)MessageKBCT.SelectSampleForInstanceBasedFingrams(this,this.Parent.getDataFile().GetActiveCount());
                    if (op!=null) {
                        //System.out.println("op -> "+op.intValue());
                        selSample= op.intValue();
                        MainKBCT.getConfig().SetFingramsSelectedSample(selSample);
                    }
                  }
 	        	  MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticFingramsProcedure")+"\n\n"+
 	    		  LocaleKBCT.GetString("Processing")+" ..."+"\n"+
 	              "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
 	              "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
 	          }
 	          /*if (MainKBCT.getConfig().GetTutorialFlag()) {
 	            //System.out.println("JExpertFrame: TUTORIAL");
 	            MainKBCT.getConfig().SetFingramsSelectedSample(1);
 	            MainKBCT.getConfig().SetFingramsLayout(LocaleKBCT.DefaultFingramsLayout());
 	            MainKBCT.getConfig().SetFingramsMetric(LocaleKBCT.DefaultFingramsMetric());
 	          }*/
 	          boolean aux= MainKBCT.getConfig().GetTESTautomatic();
 	          MainKBCT.getConfig().SetTESTautomatic(true);
	          //Date d= new Date(System.currentTimeMillis());
			  //System.out.println("JEF-T1 -> "+DateFormat.getDateTimeInstance().format(d));
 	          this.jButtonInfer_actionPerformed();
 	          double fingramsIntIndex= -1;
 	          if (this.jif!=null) {
 	    	    if (fws)
 	              this.jif.jMenuFingrams_actionPerformed("WS");

 	    	    if (fwos)
 	              this.jif.jMenuFingrams_actionPerformed("WOS");
 	    	    
                if (fws || fwos)
 	    	        fingramsIntIndex= this.jif.jlf.getCIS();
 	           }
	           //d= new Date(System.currentTimeMillis());
			   //System.out.println("JEF-T2 -> "+DateFormat.getDateTimeInstance().format(d));
 	           MainKBCT.getConfig().SetTESTautomatic(aux);
 	           if ( (!MainKBCT.getConfig().GetTutorialFlag()) && (!MainKBCT.getConfig().GetTESTautomatic()) ) {
 	             MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticFingramsProcedureEnded"));
 	           }
 	           boolean reg;
 	           if (this.Temp_kbct.GetOutput(1).GetClassif().equals("yes"))
	             reg= false;
 	           else
 	    	     reg= true;
 	      
 	           MainKBCT.getConfig().SetTESTautomatic(true);
	           //d= new Date(System.currentTimeMillis());
			   //System.out.println("JEF-T3 -> "+DateFormat.getDateTimeInstance().format(d));
 	           this.AssessingAccuracy(true, false);
 	           double[] accind= new double[4];
               accind[0]= this.jrbqf.getCoverage();
               boolean warningabort= false;
 	           if (reg) {
 	               accind[1]= this.jrbqf.getRMSE();
 	               accind[2]= this.jrbqf.getMSE();
 	               accind[3]= this.jrbqf.getMAE();
 	           } else {
                   accind[1]= this.jrbqf.getAccIndexLRN();
                   accind[2]= this.jrbqf.getAvConfFD();
                   if (accind[2] == -1)
                	   warningabort= true;
 	           }
 	           if (!warningabort) {
 	               boolean[] covdata= this.jrbqf.getCovData();
 	               this.jrbqf.dispose();
 	               this.jrbqf= null;
 	               MainKBCT.getConfig().SetTESTautomatic(aux);
	               // d= new Date(System.currentTimeMillis());
			       //System.out.println("JEF-T4 -> "+DateFormat.getDateTimeInstance().format(d));
 	               //this.AssessingInterpretability(true);
 	               double[] intind= this.jkbif.getInterpretabilityIndicators();
 	               //int[] fr= this.jkbif.getMaxSFRperRule();
 	               //for (int n=0; n<fr.length; n++) {
 	        	   //   System.out.println("active rule "+n+" -> "+fr[n]);
 	               //}
 	               this.jkbif.dispose();
 	               this.jkbif= null;
 	               if (RulesActivation != null) {
                       for (int n=0; n<RulesActivation.length; n++) {
	            		    this.Temp_kbct.SetRuleActive(n+1, RulesActivation[n]);
                       }
 	               }
 	               if (fws && fwos)
 		               this.visualizeSVG("ALL", reg, accind, covdata, intind, fingramsIntIndex, selFing);
 	               else if (fws)
 		               this.visualizeSVG("WS", reg, accind, covdata, intind, fingramsIntIndex, selFing);
 	               else if (fwos)
 		               this.visualizeSVG("WOS", reg, accind, covdata, intind, fingramsIntIndex, selFing); 	    
	               //d= new Date(System.currentTimeMillis());
 	               //System.out.println("JEF-T5 -> "+DateFormat.getDateTimeInstance().format(d));
               	   //MainKBCT.getConfig().SetFingramsSelectedSample(-1);
 	           } else {
 	               this.jrbqf.dispose();
 	               this.jrbqf= null;
 	               MainKBCT.getConfig().SetTESTautomatic(aux);
 	               //MainKBCT.getConfig().SetFingramsSelectedSample(-1);
 	               this.jkbif.dispose();
 	               this.jkbif= null;
 	               if (RulesActivation != null) {
                       for (int n=0; n<RulesActivation.length; n++) {
	            		    this.Temp_kbct.SetRuleActive(n+1, RulesActivation[n]);
                       }
 	               }
 	           }
 	        }
 	  }
  }
//------------------------------------------------------------------------------
  void jButtonConsistency_actionPerformed(boolean automatic) {
        //System.out.println("JEF: Consistency -> htsize="+jnikbct.getHashtableSize());
        //System.out.println("JEF: Consistency -> id="+jnikbct.getId());
        long ptr= jnikbct.getId();
	    boolean warning= false;
	    for (int n=0; n<this.Temp_kbct.GetNbRules(); n++) {
	      if (!warning) {
	        Rule r= this.Temp_kbct.GetRule(n+1);
	        int[] out= r.Get_out_labels_number();
	        for (int m=0; m<out.length; m++) {
	          if (out[m]!=0)
	            warning= true;
	        }
	        warning= !warning;
	      }
	    }
	    if (!warning) {
	    	int option=0;
	    	if (!automatic)
	     	    option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeSolveLingConflicts"));

	     	if (option==0) {
	            // Solving conflicts
	     	    File fname= new File(JKBCTFrame.KBExpertFile);
	     	    String name=fname.getName();
	     	    String LogName="LogConsistency_"+name.substring(0,name.length()-7);
	     	    String ConsistencyLogFile= (JKBCTFrame.BuildFile(LogName)).getAbsolutePath();
	     	    MessageKBCT.BuildLogFile(ConsistencyLogFile+".txt", this.Parent.IKBFile, JKBCTFrame.KBExpertFile, this.Parent.KBDataFile, "Consistency");
	            boolean warningSC= false;
	            MessageKBCT.WriteLogFile("----------------------------------", "Consistency");
	            MessageKBCT.WriteLogFile((LocaleKBCT.GetString("SolvingLinguisticConflicts")).toUpperCase(), "Consistency");
	            boolean endSC= false;
	            int iter= 1;
	            while (!endSC) {
	     	       long TM=Runtime.getRuntime().totalMemory();
	    	       long MM=Runtime.getRuntime().maxMemory();
	    	       if (TM>=MM) {
	                   if (!MainKBCT.getConfig().GetTESTautomatic()) {
	    	    	     String message= LocaleKBCT.GetString("WarningConsistencySolvingConflictsHalted")+"\n"+
	                     LocaleKBCT.GetString("WarningJVMOutOfMemory")+"\n"+
	                     LocaleKBCT.GetString("WarningReleaseMemory")+"\n"+
	                     LocaleKBCT.GetString("WarningConsistencySolvingConflictsOutOfMemory");
	    	             if (!MainKBCT.flagHalt) {
	    	    	       MessageKBCT.Information(null, message);
	    	             }
	                   }
	                   if (!MainKBCT.flagHalt) {
	                       MessageKBCT.WriteLogFile(LocaleKBCT.GetString("WarningConsistencySolvingConflictsHalted"), "Consistency");
	                       MainKBCT.flagHalt= true;
	                   }
	                   break;
	    	       } else {
	            	   MessageKBCT.WriteLogFile(LocaleKBCT.GetString("Iteration")+" "+iter, "Consistency");
	                   Date d= new Date(System.currentTimeMillis());
	                   MessageKBCT.WriteLogFile("  "+"time begin -> "+DateFormat.getTimeInstance().format(d), "Consistency");
	                   iter++;
	                   boolean[] SC= this.SolveConflictsRuleBase(this.getJExtDataFile());
	                   endSC= SC[0];
	                   if (SC[1])
	                     warningSC= true;
	                   if (SC[2])
	                     MessageKBCT.RemoveLastIteration();
	                   d= new Date(System.currentTimeMillis());
	                   MessageKBCT.WriteLogFile("  "+"time end -> "+DateFormat.getTimeInstance().format(d), "Consistency");
	                   if ( (!MainKBCT.getConfig().GetTESTautomatic()) && (iter > 100) )
	                	  endSC=true;
	              }
	            }
	            if (!warningSC) {
	                MessageKBCT.WriteLogFile(LocaleKBCT.GetString("NoConflictsSolution"), "Consistency");
	            	if (!MainKBCT.getConfig().GetTESTautomatic())
	            		MessageKBCT.Information(this, LocaleKBCT.GetString("NoConflictsSolution"));
	            }
	            MessageKBCT.WriteLogFile("----------------------------------", "Consistency");
	            MessageKBCT.WriteLogFile(LocaleKBCT.GetString("AutomaticConsistencyEnded"), "Consistency");
	            Date d= new Date(System.currentTimeMillis());
	            MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "Consistency");
	            MessageKBCT.CloseLogFile("Consistency");
	        	if ( (!MainKBCT.getConfig().GetTESTautomatic()) && (warningSC) ) {
	                try {
	                	this.jfcLSC= new JFISConsole(this, ConsistencyLogFile+".txt", false);
	                } catch (Throwable t) {
	                    t.printStackTrace();
	                	MessageKBCT.Error(null, t);
	                }
	            }
	        } else {
	       	    if (JExpertFrame.jcf != null) {
	                jcf.dispose();
	                JKBCTFrame.RemoveTranslatable(JExpertFrame.jcf);
	                JExpertFrame.jcf= null;
	            }
	            //JConsistency jc= new JConsistency(this, this.Temp_kbct);
	            JConsistency jc= new JConsistency(this.Temp_kbct);
	            jc.AnalysisOfConsistency(false);
	            JExpertFrame.jcf= new JConsistencyFrame(this, this.Temp_kbct, jc, true, null);
	            if (jc.getWARNING())
	        	    JExpertFrame.jcf.setVisible(true);
	            else
	                MessageKBCT.Information(this, LocaleKBCT.GetString("ConsistencyCheckingGood"));
	        }
	    } else {
	        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("SelectALabelForEachOutput"));
	    }
        long[] exc= new long[8];
        exc[0]= this.Temp_kbct.GetPtr();
        exc[1]= this.Temp_kbct.GetCopyPtr();
        if (this.Parent.kbct_Data!=null) {
        	exc[2]= this.Parent.kbct_Data.GetPtr();
        	exc[3]= this.Parent.kbct_Data.GetCopyPtr();
        } else {
        	exc[2]= -1;
        	exc[3]= -1;
        }
        if (this.Parent.kbct!=null) {
        	exc[4]= this.Parent.kbct.GetPtr();
        	exc[5]= this.Parent.kbct.GetCopyPtr();
        } else {
        	exc[4]= -1;
        	exc[5]= -1;
        }
        if (this.kbct!=null) {
        	exc[6]= this.kbct.GetPtr();
        	exc[7]= this.kbct.GetCopyPtr();
        } else {
        	exc[6]= -1;
        	exc[7]= -1;
        }
 	    //System.out.println("JExpertFrame: jButtonConsistency_ac");
	    jnikbct.cleanHashtable(ptr,exc);
 	    //System.out.println("");
        //System.out.println("JEF: Consistency -> htsize="+jnikbct.getHashtableSize());
        //System.out.println("JEF: Consistency -> id="+jnikbct.getId());
  }
  //------------------------------------------------------------------------------
  void jButtonLogView_actionPerformed(boolean automatic) {
      //System.out.println("JEF: LV -> htsize="+jnikbct.getHashtableSize());
      //System.out.println("JEF: LV -> id="+jnikbct.getId());
      //System.out.println("JEF: LV -> Temp_kbct="+this.Temp_kbct.GetPtr());
      long ptrid= jnikbct.getId();
      //System.out.println("ptrid="+ptrid);
	  boolean warning= false;
	  boolean warningend= false;
	  try {
		  this.Temp_kbct.Save();
	  } catch (Throwable t) {
		  t.printStackTrace();
		  MessageKBCT.Error(null, t);
	  } 
	  File tempkbini= BuildFile("kb_unred.kb.xml");
	  File tempkbend= BuildFile("kb_espresso_red.kb.xml");
      JLogicalView jlv= new JLogicalView(this.Temp_kbct, tempkbini, tempkbend);
      boolean[] res;
	  if (this.Parent.DataFileNoSaved != null)
	      res= jlv.computeLVindex(this.Parent.DataFileNoSaved);
	  else
	      res= jlv.computeLVindex(this.Parent.DataFile);

	  if (!automatic) {
	      warning= res[0];
          warningend= res[1];
          if (!warningend && !warning)
        	  this.ReInitTableRules();
        	  
          if (!warningend) {
		      if (!warning) {
			      try {
				      File temp1= BuildFile("kb_unred.kb.xml.pla");
				      this.jfcLogView= new JFISConsole(this, temp1.getAbsolutePath(), false);
				      File temp2= BuildFile("kb_espresso_red.kb.xml.pla");
				      this.jfcLogView= new JFISConsole(this, temp2.getAbsolutePath(), false);
				      File temp3= BuildFile("kb_espresso_red.kb.xml.log.txt");
				      this.jfcLogView= new JFISConsole(this, temp3.getAbsolutePath(), true);
			      } catch (Throwable t) {
			    	  t.printStackTrace();
				      MessageKBCT.Error(null, t);
			      }
		      } else {
			      try {
				      File temp= BuildFile("kb_espresso_red.kb.xml.pla.err.txt");
				      this.jfcLogView= new JFISConsole(this, temp.getAbsolutePath(), false);
			      } catch (Throwable t) {
			    	  t.printStackTrace();
				      MessageKBCT.Error(null, t);
			      }
		      }
	       }
	  }
      long[] exc= new long[8];
      exc[0]= this.Temp_kbct.GetPtr();
      exc[1]= this.Temp_kbct.GetCopyPtr();
      if (this.Parent.kbct_Data!=null) {
      	exc[2]= this.Parent.kbct_Data.GetPtr();
      	exc[3]= this.Parent.kbct_Data.GetCopyPtr();
      } else {
      	exc[2]= -1;
      	exc[3]= -1;
      }
      if (this.Parent.kbct!=null) {
      	exc[4]= this.Parent.kbct.GetPtr();
      	exc[5]= this.Parent.kbct.GetCopyPtr();
      } else {
      	exc[4]= -1;
      	exc[5]= -1;
      }
      if (this.kbct!=null) {
      	exc[6]= this.kbct.GetPtr();
      	exc[7]= this.kbct.GetCopyPtr();
      } else {
      	exc[6]= -1;
      	exc[7]= -1;
      }
      jnikbct.cleanHashtable(ptrid,exc);
      //System.out.println("JEF: LV -> htsize="+jnikbct.getHashtableSize());
      //System.out.println("JEF: LV -> id="+jnikbct.getId());
      //System.out.println("JEF: LV -> Temp_kbct="+this.Temp_kbct.GetPtr());
  }
//------------------------------------------------------------------------------
  void jButtonSimplify_actionPerformed(boolean automatic) {
        //System.out.println("JEF: Simplify -> htsize="+jnikbct.getHashtableSize());
        //System.out.println("JEF: Simplify -> id="+jnikbct.getId());
        //System.out.println("JEF: Simplify -> kbctData="+this.Parent.kbct_Data.GetPtr());
        //System.out.println("JEF: Simplify -> kbctDataCopy="+this.Parent.kbct_Data.GetCopyPtr());
        //System.out.println("this.Parent.kbct_Data.Modified()= "+this.Parent.kbct_Data.Modified());
        //long ptr= jnikbct.getId();
        boolean warnCheck= false;
        JKBCTOutput jout= this.Temp_kbct.GetOutput(1);
        if ( (jout.GetScaleName().equals("user")) && 
             ( (jout.GetInputInterestRange()[0]!=1) ||
       	     (jout.GetInputInterestRange()[1]!=jout.GetLabelsNumber()) ) &&
       	     (jout.isOutput()) &&
       	     ( (jout.GetType().equals("logical")) || (jout.GetType().equals("categorical")) ) ) {
               warnCheck= true;
        } 
        boolean warning= false;
        if (!warnCheck) {
	        for (int n=0; n<this.Temp_kbct.GetNbRules(); n++) {
	           if (!warning) {
	              Rule r= this.Temp_kbct.GetRule(n+1);
	              int[] out= r.Get_out_labels_number();
	              for (int m=0; m<out.length; m++) {
	                   if (out[m]!=0)
	                       warning= true;
	              }
	              warning= !warning;
	           }
	        }
        }
	    if (!warning) {
	        // mode=0 -> Supervised
	        // mode=1 -> Automatic
	        int mode=1;
	        if (!automatic)
	    	    mode= MessageKBCT.SelectMode(this, LocaleKBCT.GetString("SelectSimplificationMode"));

	        File fname= new File(JKBCTFrame.KBExpertFile);
	        String name=fname.getName();
	        String LogName="LogSimplify_"+name.substring(0,name.length()-7);
	        String SimplifyLogFile= (JKBCTFrame.BuildFile(LogName)).getAbsolutePath();
	        MessageKBCT.BuildLogFile(SimplifyLogFile+".txt", this.Parent.IKBFile, JKBCTFrame.KBExpertFile, this.Parent.KBDataFile, "Simplify");
	        if (mode == 0) {
	            Object option= MessageKBCT.SimplifyOptions(this);
	            if (option != null) {
	              if (option.equals(LocaleKBCT.GetString("ReduceDataBase"))) {
	                  MainKBCT.getConfig().SetOnlyDBsimplification(true);
	                  MainKBCT.getConfig().SetOnlyRBsimplification(false);
	                  this.ReduceDataBase(mode, automatic);
	              } else if (option.equals(LocaleKBCT.GetString("ReduceRuleBase"))) {
	                  MainKBCT.getConfig().SetOnlyDBsimplification(false);
	                  MainKBCT.getConfig().SetOnlyRBsimplification(true);
	                  this.ReduceRuleBase(mode, automatic);
	              }
	           }
	        } else if (mode==1) {
	           MessageKBCT.WriteLogFile((LocaleKBCT.GetString("AutomaticSimplificationProcedure")).toUpperCase(), "Simplify");
	           MessageKBCT.WriteLogFile("----------------------------------", "Simplify");
	           int opt=1;
	           if (!automatic) {
	             String msg= LocaleKBCT.GetString("WarningAutomaticSimplification1")+ "\n" +
	                         LocaleKBCT.GetString("WarningAutomaticSimplification2")+ "\n" +
	                         LocaleKBCT.GetString("WarningSaveReduceDataBase3")+ "\n" +
	                         LocaleKBCT.GetString("DoYouWantToSaveIt");
	             opt= MessageKBCT.Confirm(this, msg, 0, true, false, false);
	           }
	           if (opt != 2) {
	             if (opt==0) {
	               try {
	                 this.Save();
	               } catch (Throwable t) {
	                 t.printStackTrace();
	                 MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);}
	             }
	             this.setSimplifyQualityOptions(automatic);
	             
	             MessageKBCT.WriteLogFile((LocaleKBCT.GetString("Options")).toUpperCase(), "Simplify");
	             MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("SelectedOutput")+"= "+MainKBCT.getConfig().GetSelectedOutput(), "Simplify");
	             boolean onlyDBsimp=MainKBCT.getConfig().GetOnlyDBsimplification();
	             boolean onlyRBsimp=MainKBCT.getConfig().GetOnlyRBsimplification();
	             MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("OnlyDBsimplification")+"= "+onlyDBsimp, "Simplify");
	             MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("OnlyRBsimplification")+"= "+onlyRBsimp, "Simplify");

	             if (MainKBCT.getConfig().GetFirstReduceRuleBase())
	                 MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("FirstReduceRuleBase"), "Simplify");
	             else
	                 MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("FirstReduceDataBase"), "Simplify");
	            	 
	             MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("MaximumLossOfCoverage")+"= "+MainKBCT.getConfig().GetMaximumLossOfCoverage()+" %", "Simplify");
	             MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("MaximumNumberNewUnclassifiedCases")+"= "+MainKBCT.getConfig().GetMaximumNumberNewUnclassifiedCases(), "Simplify");
	             MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("selectedPerformance")+"= "+MainKBCT.getConfig().GetSelectedPerformance(), "Simplify");
	             MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("MaximumLossOfPerformance")+"= "+MainKBCT.getConfig().GetMaximumLossOfPerformance()+" %", "Simplify");
	             MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("MaximumNumberNewErrorCases")+"= "+MainKBCT.getConfig().GetMaximumNumberNewErrorCases(), "Simplify");
	             MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("MaximumNumberNewAmbiguityCases")+"= "+MainKBCT.getConfig().GetMaximumNumberNewAmbiguityCases(), "Simplify");
	             MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("ruleRemoval")+"= "+MainKBCT.getConfig().GetRuleRemoval(), "Simplify");
	             MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("variableRemoval")+"= "+MainKBCT.getConfig().GetVariableRemoval(), "Simplify");
	             MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("premiseRemoval")+"= "+MainKBCT.getConfig().GetPremiseRemoval(), "Simplify");
	             String OR= LocaleKBCT.GetString("No");
	             String optOR= MainKBCT.getConfig().GetSimpRuleRanking();
	          	 if (!optOR.equals("false"))
	          		  OR= LocaleKBCT.GetString("Yes")+" ["+LocaleKBCT.GetString(optOR)+"]";

	             MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("RuleRanking")+"= "+OR, "Simplify");
	             MessageKBCT.WriteLogFile("----------------------------------", "Simplify");
	             if (!automatic)
	                 MessageKBCT.Information(this, LocaleKBCT.GetString("Processing")+" ..."+"\n"+
	                                               "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
	                                               "... "+LocaleKBCT.GetString("PleaseWait")+" ...");

	             int cont=1;
	             boolean warningRDB= false;
	             boolean warningRRB= false;
	             //////////////
	             boolean endRDB= false;
	             boolean endRRB= false;
	             MessageKBCT.WriteLogFile(LocaleKBCT.GetString("SimplifyMsg"), "Simplify");
	             while (!endRDB || !endRRB) {
	      	       long TM=Runtime.getRuntime().totalMemory();
	    	       long MM=Runtime.getRuntime().maxMemory();
	    	       if (TM>=MM) {
	                   if (!MainKBCT.getConfig().GetTESTautomatic()) {
	    	    	     String message= LocaleKBCT.GetString("WarningSimplificationHalted")+"\n"+
	                     LocaleKBCT.GetString("WarningJVMOutOfMemory")+"\n"+
	                     LocaleKBCT.GetString("WarningSimplificationSaveKB")+"\n"+
	                     LocaleKBCT.GetString("WarningReleaseMemory");
	    	             if (!MainKBCT.flagHalt) {
	    	    	       MessageKBCT.Information(null, message);
	    	             }
	                   }
	                   if (!MainKBCT.flagHalt) {
	                       MessageKBCT.WriteLogFile(LocaleKBCT.GetString("WarningSimplificationHalted"), "Simplify");
	                       MainKBCT.flagHalt= true;
	                   }
	                   break;
	    	       } else {
	    	    	   long ptrid= jnikbct.getId();
	                   MessageKBCT.WriteLogFile(LocaleKBCT.GetString("Iteration")+" "+cont, "Simplify");
	                   Date d= new Date(System.currentTimeMillis());
	                   MessageKBCT.WriteLogFile("time begin -> "+DateFormat.getTimeInstance().format(d), "Simplify");
	                   if (onlyDBsimp) {
	                       MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("ReduceDataBase"), "Simplify");
	                       boolean[] RDB= this.ReduceDataBase(mode, automatic);
	                       if (RDB[1])
	                           warningRDB= true;

	                       endRDB= true;
	                       endRRB= true;
	                   } else if (onlyRBsimp) {
	                       MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("ReduceRuleBase"), "Simplify");
	                       boolean[] RRB= this.ReduceRuleBase(mode, automatic);
	                       if (RRB[1])
	                         warningRRB= true;

	                       endRDB= true;
	                       endRRB= true;
	                   } else {
	                     if (MainKBCT.getConfig().GetFirstReduceRuleBase()) {
		                    // MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("NbTotalRules")+"= "+this.Temp_kbct.GetNbActiveRules(), "Simplify");
	                        MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("NbTotalRules")+"= "+this.Temp_kbct.GetNbRules(), "Simplify");
	                        MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("ReduceRuleBase"), "Simplify");
	                   	    //System.out.println(" -> htsize="+jnikbct.getHashtableSize());
	                        boolean[] RRB= this.ReduceRuleBase(mode, automatic);
	                        //MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("NbTotalRules")+"= "+this.Temp_kbct.GetNbActiveRules(), "Simplify");
	                        MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("NbTotalRules")+"= "+this.Temp_kbct.GetNbRules(), "Simplify");
	                        endRRB= RRB[0];
	                        if (RRB[1])
	                            warningRRB= true;

	                        MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("ReduceDataBase"), "Simplify");
	                   	    //System.out.println(" -> htsize="+jnikbct.getHashtableSize());
	                        boolean[] RDB= this.ReduceDataBase(mode, automatic);
	                        endRDB= RDB[0];
	                        if (RDB[1])
	                            warningRDB= true;
	                     } else {
	                       MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("ReduceDataBase"), "Simplify");
	                       boolean[] RDB= this.ReduceDataBase(mode, automatic);
	                       endRDB= RDB[0];
	                       if (RDB[1])
	                         warningRDB= true;

	                       MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("NbTotalRules")+"= "+this.Temp_kbct.GetNbActiveRules(), "Simplify");
	                       MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("ReduceRuleBase"), "Simplify");
	                       boolean[] RRB= this.ReduceRuleBase(mode, automatic);
	                       MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("NbTotalRules")+"= "+this.Temp_kbct.GetNbActiveRules(), "Simplify");
	                       endRRB= RRB[0];
	                       if (RRB[1])
	                         warningRRB= true;
	                     }
                   	    //System.out.println(" -> htsize="+jnikbct.getHashtableSize());
	    	         }
	                 d= new Date(System.currentTimeMillis());
	                 MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "Simplify");
	                 cont++;
	                 long[] exc= new long[8];
	                 exc[0]= this.Temp_kbct.GetPtr();
	                 exc[1]= this.Temp_kbct.GetCopyPtr();
	                 if (this.Parent.kbct_Data!=null) {
	                 	exc[2]= this.Parent.kbct_Data.GetPtr();
	                 	exc[3]= this.Parent.kbct_Data.GetCopyPtr();
	                 } else {
	                 	exc[2]= -1;
	                 	exc[3]= -1;
	                 }
	                 if (this.Parent.kbct!=null) {
	                 	exc[4]= this.Parent.kbct.GetPtr();
	                 	exc[5]= this.Parent.kbct.GetCopyPtr();
	                 } else {
	                 	exc[4]= -1;
	                 	exc[5]= -1;
	                 }
	                 if (this.kbct!=null) {
	                 	exc[6]= this.kbct.GetPtr();
	                 	exc[7]= this.kbct.GetCopyPtr();
	                 } else {
	                 	exc[6]= -1;
	                 	exc[7]= -1;
	                 }
	                 jnikbct.cleanHashtable(ptrid,exc);
               	     //System.out.println(" CLEAN -> htsize="+jnikbct.getHashtableSize());
	               }
	             }
	             this.qold= null;

	             if (!automatic) {
	               MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticSimplificationEnded"));
	             }
	             // Estos mensajes solo se imprimen si no hay reduccion en ninguna iteracion
	             if ( (!warningRRB) && (!onlyDBsimp) ) {
	            	 if (!automatic)
	            		 MessageKBCT.Information(this, LocaleKBCT.GetString("NoRuleBaseReduction"));

	            	 MessageKBCT.WriteLogFile(LocaleKBCT.GetString("NoRuleBaseReduction"), "Simplify");
	             }
	             if ( (!warningRDB) && (!onlyRBsimp) ) {
	            	 if (!automatic)
	                     MessageKBCT.Information(this, LocaleKBCT.GetString("NoDataBaseReduction"));

	            	 MessageKBCT.WriteLogFile(LocaleKBCT.GetString("NoDataBaseReduction"), "Simplify");
	             }
	             //////////////////////////////////////////////////////////////////////////////
	             MessageKBCT.WriteLogFile("----------------------------------", "Simplify");
	             MessageKBCT.WriteLogFile(LocaleKBCT.GetString("AutomaticSimplificationEnded"), "Simplify");
	             Date d= new Date(System.currentTimeMillis());
	             MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "Simplify");
	             MessageKBCT.CloseLogFile("Simplify");
	          }
	        }
	    } else {
	        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("SelectALabelForEachOutput"));
	    }
	    //jnikbct.cleanHashtable(ptr);
        //System.out.println("JEF: Simplify -> htsize="+jnikbct.getHashtableSize());
        //System.out.println("JEF: Simplify -> id="+jnikbct.getId());
        //System.out.println("JEF: Simplify -> kbctData="+this.Parent.kbct_Data.GetPtr());
        //System.out.println("JEF: Simplify -> kbctDataCopy="+this.Parent.kbct_Data.GetCopyPtr());
        //System.out.println("this.Parent.kbct_Data.Modified()= "+this.Parent.kbct_Data.Modified());
  }
//------------------------------------------------------------------------------
  boolean[] ReduceDataBase(int mod, boolean automatic) {
      //System.out.println("JEF: RDB: Simplify -> htsize="+jnikbct.getHashtableSize());
      //System.out.println("JEF: RDB: Simplify -> id="+jnikbct.getId());
    Date d= new Date(System.currentTimeMillis());
    MessageKBCT.WriteLogFile("  "+"RDB: time begin -> "+DateFormat.getTimeInstance().format(d), "Simplify");
    boolean[] result= new boolean[2];
    result[0]= false;
    result[1]= false;
    int opt=1;
    if (mod==0) {
      this.setSimplifyQualityOptions(automatic);
      if (!automatic) {
        String msg= LocaleKBCT.GetString("WarningSaveReduceDataBase1")+ "\n" +
                  LocaleKBCT.GetString("WarningSaveReduceDataBase2")+ "\n" +
                  LocaleKBCT.GetString("WarningSaveReduceDataBase3")+ "\n" +
                  LocaleKBCT.GetString("DoYouWantToSaveIt");
        opt= MessageKBCT.Confirm(this, msg, 0, true, false, false);
      } 
    }
    if (opt != 2) {
      if (opt==0) {
        try {
            this.Save();
        } catch (Throwable t) {
          t.printStackTrace();
          MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);
        }
      }
      if (this.Temp_kbct.GetNbActiveRules()>0) {
          //JConsistency jc= new JConsistency(this, this.Temp_kbct);
          JConsistency jc= new JConsistency(this.Temp_kbct);
    	  int NbInputs=this.Temp_kbct.GetNbInputs();
   	      int[] NbLabelsPerInput= new int[NbInputs];
    	  String simpRankOpt= MainKBCT.getConfig().GetSimpRuleRanking();
          if (!simpRankOpt.equals("false")) {
  	           for (int n=0; n<NbInputs; n++) {
	                NbLabelsPerInput[n]= this.Temp_kbct.GetInput(n+1).GetLabelsNumber();
	           }
  	           // ordered by Increase Complexity
        	   //this.Temp_kbct= jc.OrderByInterpretabilityRules(this.Temp_kbct, NbLabelsPerInput);
          }
          if (this.qold==null)
              this.qold= jc.KBquality(this.Temp_kbct, null, 0, 0, this.getJExtDataFile());

          this.jsp= new JSolveProblem(this.Temp_kbct, this, null);
          boolean warning= false;
          boolean end= false;
          InfoConsistency ic= null;
          // mode=0 -> Supervised
          // mode=1 -> Automatic
          int mode=1;
          if (mod==0) {
            if (!automatic)
              mode= MessageKBCT.SelectMode(this, LocaleKBCT.GetString("SelectReductionMode"));
          }
          if (mode == -1) {
              warning= true;
              end= true;
          }
          this.MSGS= new Vector();
          if ((mod==0) && (mode==1))
              MessageKBCT.Information(this, LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                                        "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                                        "... "+LocaleKBCT.GetString("PleaseWait")+" ...");

          //long ptr= jnikbct.getId();
          if (!end) {
            MessageKBCT.WriteLogFile("  "+"> Remove variables and/or labels", "Simplify");
            if (!simpRankOpt.equals("false")) {
   	           // ordered by Increase Complexity
          	    NbInputs=this.Temp_kbct.GetNbInputs();
       	        NbLabelsPerInput= new int[NbInputs];
   	            for (int n=0; n<NbInputs; n++) {
	                 NbLabelsPerInput[n]= this.Temp_kbct.GetInput(n+1).GetLabelsNumber();
	            }
            	jc.OrderByInterpretabilityRules(this.Temp_kbct, NbLabelsPerInput);
                jc.setKBCT(this.Temp_kbct);
            }
            jc.AnalysisOfConsistency(true);
            this.jsp.setKBCT(this.Temp_kbct);
            this.jsp.setJConsistency(jc);
            Vector inputs_solutions= this.jsp.getSolutions(3);
            Object[] in_sol= inputs_solutions.toArray();
            int n= in_sol.length-1;
            //System.out.println("JEF: RDB1: Simplify -> htsize="+jnikbct.getHashtableSize());
            //System.out.println("JEF: RDB1: Simplify -> id="+jnikbct.getId());
            while ( (!end) && (n>=0) ) {
              warning= true;
              ic= (InfoConsistency)in_sol[n--];
              int NumLabel= ic.getLabelNum();
              if (NumLabel==-1) {
                // eliminar variable de entrada no usada
                int option= 0;
                if (mode==0)
                    option= MessageKBCT.Confirm(this, ic.getError()+"\n"+LocaleKBCT.GetString("DoYouWantToContinue"), 0, false, true, false);

                if (option==2)
                  end= true;
                else if (option==0) {
                  this.AssessQualityForKeepOrDiscardChange(jc, true, ic, mode, this.getJExtDataFile(), qold);
                }
              } else {
                // eliminar etiqueta de entrada no usada
            	    JKBCTInput jin= this.Temp_kbct.GetInput(ic.getVarNum());
                    int option=0;
                    if ( (jin.GetLabelsNumber() < 3) || !(jin.GetType().equals("numerical")) )
                        option= -1;
                    else if (mode==0)
                        option= MessageKBCT.Confirm(this, ic.getError()+"\n"+LocaleKBCT.GetString("DoYouWantToContinue"), 0, false, true, false);

                    if (option==2)
                        end= true;
                    else if (option==0) {
                        this.AssessQualityForKeepOrDiscardChange(jc, false, ic, mode, this.getJExtDataFile(), qold);
                    }
            }
          }
          //jnikbct.cleanHashtable(ptr);
          // solo valido para 1 salida          
          if (this.Temp_kbct.GetOutput(1).GetType().equals("numerical")) {
            if (!end) {
              this.ReInitJSolveProblem(jc, true);
              Vector outputs_solutions= this.jsp.getSolutions(4);
              Object[] out_sol= outputs_solutions.toArray();
              n= out_sol.length-1;
              while ( (!end) && (n>=0) ) {
                warning= true;
                ic= (InfoConsistency)out_sol[n--];
                int NumLabel= ic.getLabelNum();
                if (NumLabel==-1) {
                  // eliminar variable de salida no usada
                  int option= 0;
                  if (mode==0)
                      option= MessageKBCT.Confirm(this, ic.getError()+"\n"+LocaleKBCT.GetString("DoYouWantToContinue"), 0, false, true, false);

                  if (option==2)
                    end= true;
                  else if (option==0) {
                    this.AssessQualityForKeepOrDiscardChange(jc, true, ic, mode, this.getJExtDataFile(), qold);
                  }
                } else {
                  // eliminar etiqueta de salida no usada
                  int option=0;
                  if (this.Temp_kbct.GetOutput(ic.getVarNum()).GetLabelsNumber() < 3)
                      option= -1;
                  else if (mode==0)
                      option= MessageKBCT.Confirm(this, ic.getError()+"\n"+LocaleKBCT.GetString("DoYouWantToContinue"), 0, false, true, false);

                  if (option==2)
                    end= true;
                  else if (option==0) {
                    this.AssessQualityForKeepOrDiscardChange(jc, false, ic, mode, this.getJExtDataFile(), qold);
                  }
                }
              }
            }
          }
        }
        int var_num=1;
        int lab_num=1;//-2
        // agrupar etiquetas
        MessageKBCT.WriteLogFile("  "+"> Group labels", "Simplify");
        //System.out.println("TEMPPPPPP");
        NbInputs=this.Temp_kbct.GetNbInputs();
        NbLabelsPerInput= new int[NbInputs];
	    for (int n=0; n<NbInputs; n++) {
             NbLabelsPerInput[n]= this.Temp_kbct.GetInput(n+1).GetLabelsNumber();
        }
        jc.OrderByInterpretabilityRules(this.Temp_kbct, NbLabelsPerInput);
        jc.setKBCT(this.Temp_kbct);
        ic= jc.GroupLabels(this.Temp_kbct, "Input", var_num, lab_num);
        //System.out.println("JEF: RDB2: Simplify -> htsize="+jnikbct.getHashtableSize());
        //System.out.println("JEF: RDB2: Simplify -> id="+jnikbct.getId());
        long ptrid= jnikbct.getId();
        while ( (!end) && (ic != null) ) {
          warning= true;
          int option=0;
          if (mode==0)
            option= MessageKBCT.Confirm(this, ic.getError()+"\n"+LocaleKBCT.GetString("DoYouWantToContinue"), 0, false, true, false);

          if (option==2)
            end= true;
          else if (option==0) {
            JKBCT kbctaux= new JKBCT(this.Temp_kbct);
            //long ptraux= kbctaux.GetPtr();
            kbctaux.SetKBCTFile(this.Temp_kbct.GetKBCTFile()+".tmp.kb.xml");
            try {
                kbctaux.Save();
            } catch (Throwable t) {
                t.printStackTrace();
                MessageKBCT.Error( this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: ReduceDataBase: saving kbctaux:   " + t );
            }
            String msgGL= this.jsp.SolveProblemLabelsGroup(ic, mode);
            MSGS.add(msgGL);
            double[][] qnew= jc.KBquality(this.Temp_kbct, null, 0, 0, this.getJExtDataFile());
            int aux= 1;
            if (jc.GetWorseKBquality(qold, qnew)) {
              int keepChange= 1;
              if (mode==0)
                  keepChange= MessageKBCT.Confirm(this, LocaleKBCT.GetString("QualityKBgetWorse")+"\n"+
                                                        LocaleKBCT.GetString("DoYouWantToKeepLastChange"), 1, false, false, false);
              if (keepChange!=0) {
                this.DiscardChangeInReduceDataBase(kbctaux, null, null, jc, MSGS);
                aux++;
              } else {
                  MessageKBCT.WriteLogFile("    -> "+msgGL, "Simplify");
              }
              kbctaux.Close();
              kbctaux.Delete();
              //jnikbct.DeleteKBCT(ptraux+1);
            } else {
                MessageKBCT.WriteLogFile("    -> "+msgGL, "Simplify");
            }
            ic= jc.GroupLabels(this.Temp_kbct, ic.getVarType(), ic.getVarNum(), ic.getLabelNum()+aux);
          } else {
            ic= jc.GroupLabels(this.Temp_kbct, ic.getVarType(), ic.getVarNum(), ic.getLabelNum()+2);
          }
        }
        long[] exc= new long[8];
        exc[0]= this.Temp_kbct.GetPtr();
        exc[1]= this.Temp_kbct.GetCopyPtr();
        if (this.Parent.kbct_Data!=null) {
        	exc[2]= this.Parent.kbct_Data.GetPtr();
        	exc[3]= this.Parent.kbct_Data.GetCopyPtr();
        } else {
        	exc[2]= -1;
        	exc[3]= -1;
        }
        if (this.Parent.kbct!=null) {
        	exc[4]= this.Parent.kbct.GetPtr();
        	exc[5]= this.Parent.kbct.GetCopyPtr();
        } else {
        	exc[4]= -1;
        	exc[5]= -1;
        }
        if (this.kbct!=null) {
        	exc[6]= this.kbct.GetPtr();
        	exc[7]= this.kbct.GetCopyPtr();
        } else {
        	exc[6]= -1;
        	exc[7]= -1;
        }
        jnikbct.cleanHashtable(ptrid,exc);
        //System.out.println("JEF: RDB3: Simplify -> htsize="+jnikbct.getHashtableSize());
        //System.out.println("JEF: RDB3: Simplify -> id="+jnikbct.getId());
        ptrid= jnikbct.getId();
        ///////////////////////
        // Try to delete variables
        if (MainKBCT.getConfig().GetVariableRemoval()) {
            MessageKBCT.WriteLogFile("  "+"> Force Variable Removing", "Simplify");
            //System.out.println("Force Variable Removing 1");
            int[] premByVar= new int[this.Temp_kbct.GetNbInputs()];
            for (int n=0; n<premByVar.length; n++) {
            	premByVar[n]=0;
            }    
            for (int n=1; n<=this.Temp_kbct.GetNbRules(); n++) {
                Rule r = this.Temp_kbct.GetRule(n);
                if (r.GetActive()) {
                  int[] aux= r.Get_in_labels_number();
                  for (int k=0; k<aux.length; k++) {
                       if (aux[k]!=0)
                    	   premByVar[k]++;
                  }
                }
            }    
            //System.out.println("Force Variable Removing 2");
            int[] orderedVar= new int[this.Temp_kbct.GetNbInputs()];
            for (int i=0; i<orderedVar.length; i++) {
           	    orderedVar[i]= i+1;
            }
            for (int i=1; i<orderedVar.length; i++) {
                 for (int k=0; k<i; k++) {
                      if (premByVar[orderedVar[i]-1]>premByVar[orderedVar[k]-1]) {
                          for (int m=i; m>k; m--) {
                              orderedVar[m]=orderedVar[m-1];
                          }
                          orderedVar[k]=i+1;
                          break;
                      }
                 }
             }
          //System.out.println("Force Variable Removing 3");
          int NbDataInputsOLD = this.Parent.kbct_Data.GetNbInputs();
          for (int m = 0; m < orderedVar.length; m++) {
            //System.out.println("Force Variable Removing 3.1 -> "+orderedVar[m]);
            ic = new InfoConsistency();
            String sol = LocaleKBCT.GetString("TwoOptions") + ":" + "\n"
                + "1. - " + LocaleKBCT.GetString("RemoveThe") + " " +
                LocaleKBCT.GetString("Variable").toLowerCase() + "\n"
                + "2. - " + LocaleKBCT.GetString("UseVariable");
            ic.setVarNum(orderedVar[m]);
            ic.setVarType("Input");
            ic.setRemoveVar(true);
            ic.setLabelNum(-1);
            ic.setSolution(sol);
            boolean remove= this.AssessQualityForKeepOrDiscardChange(jc, true, ic, mode, this.getJExtDataFile(), qold);
            //System.out.println("Force Variable Removing 3.1 -> remove="+remove);
            if (remove) {
                for (int k=0; k<orderedVar.length; k++) {
                    if (orderedVar[k]>orderedVar[m])
                    	orderedVar[k]--;
                }  
            }
          }
          //System.out.println("Force Variable Removing 3.2");
          int NbDataInputsNEW = this.Parent.kbct_Data.GetNbInputs();
          if (NbDataInputsNEW != NbDataInputsOLD) {
            this.DataFileModificationSimplify = true;
            this.Parent.DataFileModificationSimplify = true;
          }
        }
        ///////////////////////
        exc= new long[8];
        exc[0]= this.Temp_kbct.GetPtr();
        exc[1]= this.Temp_kbct.GetCopyPtr();
        if (this.Parent.kbct_Data!=null) {
        	exc[2]= this.Parent.kbct_Data.GetPtr();
        	exc[3]= this.Parent.kbct_Data.GetCopyPtr();
        } else {
        	exc[2]= -1;
        	exc[3]= -1;
        }
        if (this.Parent.kbct!=null) {
        	exc[4]= this.Parent.kbct.GetPtr();
        	exc[5]= this.Parent.kbct.GetCopyPtr();
        } else {
        	exc[4]= -1;
        	exc[5]= -1;
        }
        if (this.kbct!=null) {
        	exc[6]= this.kbct.GetPtr();
        	exc[7]= this.kbct.GetCopyPtr();
        } else {
        	exc[6]= -1;
        	exc[7]= -1;
        }
        jnikbct.cleanHashtable(ptrid,exc);
        //System.out.println("JEF: RDB4: Simplify -> htsize="+jnikbct.getHashtableSize());
        //System.out.println("JEF: RDB4: Simplify -> id="+jnikbct.getId());
        //System.out.println("Force Variable Removing 4");
        boolean NoPopUpMessage= false;
        if (warning) {
          if (mod==0)
             MessageKBCT.Information(this, LocaleKBCT.GetString("DataBaseReductionEnded"));

           result[1]= true;
        } else {
            if (mod==0)
               MessageKBCT.Information(this, LocaleKBCT.GetString("NoDataBaseReduction"));

            NoPopUpMessage= true;
            result[0]= true;
            d= new Date(System.currentTimeMillis());
            MessageKBCT.WriteLogFile("  "+"RDB: time end -> "+DateFormat.getTimeInstance().format(d), "Simplify");
            return result;
        }
        //System.out.println("JEF: RDB5: Simplify -> htsize="+jnikbct.getHashtableSize());
        //System.out.println("JEF: RDB5: Simplify -> id="+jnikbct.getId());
        //System.out.println("Force Variable Removing 5");
        if (mode==1) {
          Object[] aux= MSGS.toArray();
          int lim= aux.length;
          if (lim > 0) {
              final JDialog jd = new JDialog(this);
              jd.setTitle(LocaleKBCT.GetString("Information"));
              jd.getContentPane().setLayout(new GridBagLayout());
              JScrollPane jPanelMessages= new JScrollPane();
              JPanel jp= new JPanel(new GridBagLayout());
              int cont=0;
              while (cont<lim) {
                JLabel jl= new JLabel(aux[cont].toString());
                jp.add(jl, new GridBagConstraints(0, cont, 1, 1, 1.0, 1.0
                      ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
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
              if (!automatic) {
                jd.setModal(true);
                jd.pack();
                jd.setLocation(JChildFrame.ChildPosition(this, jd.getSize()));
                jd.setVisible(true);
              } else
                  jd.dispose();
           	  
          } else if (!NoPopUpMessage) {
              if (mod==0)
                 MessageKBCT.Information(this, LocaleKBCT.GetString("NoDataBaseReduction"));
              else
                 result[1]= false;
          }
        }
        //System.out.println("Force Variable Removing 6");
        if (JExpertFrame.jcf != null) {
        	JExpertFrame.jcf.dispose();
            JKBCTFrame.RemoveTranslatable(JExpertFrame.jcf);
            JExpertFrame.jcf= null;
        }
      }
    }
    //System.out.println("Force Variable Removing 7");
    result[0]= true;
    d= new Date(System.currentTimeMillis());
    MessageKBCT.WriteLogFile("  "+"RDB: time end -> "+DateFormat.getTimeInstance().format(d), "Simplify");
    //System.out.println("JEF: RDB: Simplify -> htsize="+jnikbct.getHashtableSize());
    //System.out.println("JEF: RDB: Simplify -> id="+jnikbct.getId());
    return result;
  }
 //------------------------------------------------------------------------------
  private void ReInitJSolveProblem(JConsistency jc, boolean flag) {
    this.jsp= new JSolveProblem(this.Temp_kbct, this, null);
    if (flag) {
      this.jsp.setKBCT(this.Temp_kbct);
      jc.setKBCT(this.Temp_kbct);
      jc.AnalysisOfConsistency(true);
    }
    this.jsp.setJConsistency(jc);
 }
//------------------------------------------------------------------------------
  private boolean AssessQualityForKeepOrDiscardChange(JConsistency jc, boolean flag, InfoConsistency ic, int mode, JExtendedDataFile jedf, double[][] qold) {
    long ptrid= jnikbct.getId();
	//System.out.println("AssessQualityForKeepOrDiscardChange 1");
	boolean remove= false;
    //System.out.println("JEF: AssessQualityForKeepOrDiscardChange1 -> htsize="+jnikbct.getHashtableSize());
    //System.out.println("JEF: AssessQualityForKeepOrDiscardChange1 -> id="+jnikbct.getId());
	this.ReInitJSolveProblem(jc, flag);
    //System.out.println("AssessQualityForKeepOrDiscardChange 2");
    JKBCT kbctauxData= null;
    if (this.Parent.kbct_Data != null) {
      kbctauxData= new JKBCT(this.Parent.kbct_Data);
      //long ptr= kbctauxData.GetPtr();
      if (this.Parent.kbct_Data.GetKBCTFile()==null) {
        this.Parent.kbct_Data.SetKBCTFile(this.Temp_kbct.GetKBCTFile()+".data.kb.xml");
      }
      kbctauxData.SetKBCTFile(this.Parent.kbct_Data.GetKBCTFile()+".tmp.kb.xml");
      try {
         kbctauxData.Save();
      } catch (Throwable t) {
         t.printStackTrace();
         MessageKBCT.Error( this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: ReduceDataBase: saving kbctauxData:   " + t );
      }
      kbctauxData.Close();
      kbctauxData.Delete();
      //jnikbct.DeleteKBCT(ptr+1);
    }
    //System.out.println("AssessQualityForKeepOrDiscardChange 3");
    Enumeration envaraux= null;
    if (this.Parent.variables != null) {
      envaraux= this.Parent.variables.elements();
    }
    JKBCT kbctaux= new JKBCT(this.Temp_kbct);
    //long ptraux= kbctaux.GetPtr();
    File faux= new File(this.Temp_kbct.GetKBCTFile()+".tmp1.kb.xml");
    if (faux.exists()) {
    	faux.delete();
        kbctaux.SetKBCTFile(this.Temp_kbct.GetKBCTFile()+".tmp2.kb.xml");
    } else {
        kbctaux.SetKBCTFile(this.Temp_kbct.GetKBCTFile()+".tmp1.kb.xml");
    }
    try {
       kbctaux.Save();
    } catch (Throwable t) {
       t.printStackTrace();
       MessageKBCT.Error( this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: ReduceDataBase: saving kbctaux:   " + t );
    }
    //System.out.println("AssessQualityForKeepOrDiscardChange 4");
    //System.out.println("JEF: AssessQualityForKeepOrDiscardChange2 -> htsize="+jnikbct.getHashtableSize());
    //System.out.println("JEF: AssessQualityForKeepOrDiscardChange2 -> id="+jnikbct.getId());
    int NbConfOLD= jc.ContLinguisticConflicts(kbctaux);
    JExtendedDataFile jedfaux= null;
    String msg= "";
    if (flag) {
      //System.out.println("AssessQualityForKeepOrDiscardChange 4.1.1");
      File f= new File(jedf.FileName());
      File temp= JKBCTFrame.BuildFile("tempaux1"+f.getName());
      if (temp.exists())
        temp.delete();
      try {
         //System.out.println("AssessQualityForKeepOrDiscardChange 4.1.2");
         jedfaux= new JExtendedDataFile(jedf.ActiveFileName(), true);
         //System.out.println("AssessQualityForKeepOrDiscardChange 4.1.3");
         jedfaux.Save(temp.getAbsolutePath());
      } catch (Throwable t) {
    	  t.printStackTrace();
    	  MessageKBCT.Error(null,t);
      }
      //System.out.println("AssessQualityForKeepOrDiscardChange 4.1.4");
      msg=this.jsp.SolveProblemVariable(ic, true, mode);
    } else {
  	  //if (ic.getVarNum()==10) {
      //    System.out.println("JExpertFrame -> "+ic.getVarNum()+"  "+ic.getError());
      //    System.out.println("AssessQualityForKeepOrDiscardChange 4.2");
  	  //}
      msg=this.jsp.SolveProblemLabel(ic, true, mode);
    }
    //System.out.println("AssessQualityForKeepOrDiscardChange 5");
    MSGS.add(msg);
    //System.out.println("JEF: AssessQualityForKeepOrDiscardChange3 -> htsize="+jnikbct.getHashtableSize());
    //System.out.println("JEF: AssessQualityForKeepOrDiscardChange3 -> id="+jnikbct.getId());
    double[][] qnew= jc.KBquality(this.Temp_kbct, null, 0, 0, this.getJExtDataFile());
    int NbConfNEW= jc.ContLinguisticConflicts(this.Temp_kbct);
    //System.out.println("AssessQualityForKeepOrDiscardChange 6");
    if ( (NbConfNEW > NbConfOLD) || jc.GetWorseKBquality(qold, qnew)) {
         int keepChange= 1;
         if (mode==0)
             keepChange= MessageKBCT.Confirm(this, LocaleKBCT.GetString("QualityKBgetWorse")+"\n"+
                                               LocaleKBCT.GetString("DoYouWantToKeepLastChange"),1, false, false, false);
         if (keepChange!=0) {
         	 // if (ic.getVarNum()==10) {
             //     System.out.println(" -> NbConfNEW="+NbConfNEW+"  NbConfOLD="+NbConfOLD);
             //     System.out.println(" -> DiscardChangeInReduceDataBase");
          	 // }
             this.DiscardChangeInReduceDataBase(kbctaux, kbctauxData, envaraux, jc, MSGS);
             if (jedfaux != null) {
               this.setJExtDataFile(jedfaux.FileName());
             }
         } else{
        	 if (flag) {
       	       this.DataFileModificationSimplify= true;
       	       this.Parent.DataFileModificationSimplify= true;
       	       remove= true;
             }
             MessageKBCT.WriteLogFile("    -> "+msg, "Simplify");
         }        	 
      } else { 
    	  if (flag) {
    	    this.DataFileModificationSimplify= true;
  	        this.Parent.DataFileModificationSimplify= true;
   	        remove= true;
          }
          MessageKBCT.WriteLogFile("    -> "+msg, "Simplify");
      }
    kbctaux.Close();
    kbctaux.Delete();
    //jnikbct.DeleteKBCT(ptraux+1);
    //System.out.println("AssessQualityForKeepOrDiscardChange 7");
    //System.out.println("JEF: AssessQualityForKeepOrDiscardChange4 -> tempkbct="+this.Temp_kbct.GetPtr());
    //jnikbct.cleanHashtable(this.Temp_kbct.GetPtr()+1);
    long[] exc= new long[8];
    exc[0]= this.Temp_kbct.GetPtr();
    exc[1]= this.Temp_kbct.GetCopyPtr();
    if (this.Parent.kbct_Data!=null) {
    	exc[2]= this.Parent.kbct_Data.GetPtr();
    	exc[3]= this.Parent.kbct_Data.GetCopyPtr();
    } else {
    	exc[2]= -1;
    	exc[3]= -1;
    }
    if (this.Parent.kbct!=null) {
    	exc[4]= this.Parent.kbct.GetPtr();
    	exc[5]= this.Parent.kbct.GetCopyPtr();
    } else {
    	exc[4]= -1;
    	exc[5]= -1;
    }
    if (this.kbct!=null) {
    	exc[6]= this.kbct.GetPtr();
    	exc[7]= this.kbct.GetCopyPtr();
    } else {
    	exc[6]= -1;
    	exc[7]= -1;
    }
    jnikbct.cleanHashtable(ptrid,exc);
    //System.out.println("JEF: AssessQualityForKeepOrDiscardChange4 -> htsize="+jnikbct.getHashtableSize());
    //System.out.println("JEF: AssessQualityForKeepOrDiscardChange4 -> id="+jnikbct.getId());
    return remove;
  }
//------------------------------------------------------------------------------
  private void DiscardChangeInReduceDataBase(JKBCT kbctaux, JKBCT KBdata, Enumeration envar, JConsistency jc, Vector MSGS) {
    if (KBdata != null) {
      String PathDataName = this.Parent.kbct_Data.GetKBCTFile();
      if (this.Parent.kbct_Data!=null) {
    	  this.Parent.kbct_Data.Close();
    	  this.Parent.kbct_Data.Delete();
    	  this.Parent.kbct_Data= null;
      }
      this.Parent.kbct_Data= new JKBCT(KBdata.GetKBCTFile());
      this.Parent.kbct_Data.SetKBCTFile(PathDataName);
      try {
          this.Parent.kbct_Data.Save();
      } catch (Throwable t) {
    	  t.printStackTrace();
    	  MessageKBCT.Error(null,t);
      }
    }
    if (envar != null) {
      this.Parent.variables= new Vector();
      int cont=0;
      while (envar.hasMoreElements()) {
        this.Parent.variables.add(envar.nextElement());
        cont++;
      }
      this.Parent.DataNbVariables= cont;
    }
    String PathName = this.Temp_kbct.GetKBCTFile();
    //this.Temp_kbct.Close();
    //this.Temp_kbct.Delete();
    //this.Temp_kbct= null;
    this.Temp_kbct= new JKBCT(kbctaux.GetKBCTFile());
    this.Temp_kbct.SetKBCTFile(PathName);
    try {
      this.Temp_kbct.Save();
    } catch (Throwable t) {
    	t.printStackTrace();
  	    MessageKBCT.Error(null,t);
   	}
    this.Temp_kbct.AddKBCTListener(this.KBCTListener);
    jc.setKBCT(this.Temp_kbct);
    MSGS.remove(MSGS.lastElement());
    ////////////
    try {
        this.InitJExpertFrameWithKBCT();
    } catch (Throwable t) {
    	t.printStackTrace();
  	    MessageKBCT.Error(null,t);
    }
    ////////////
    this.ReInitTableRules();
  }
//------------------------------------------------------------------------------
  private JMainFrame getJMF() { return this.Parent; }
//------------------------------------------------------------------------------
  public JExtendedDataFile getJExtOrigDataFile() {
	return this.getJMF().getOrigDataFile();
  }
//------------------------------------------------------------------------------
  public JExtendedDataFile getJExtDataFile() {
    JExtendedDataFile jedf;
    if (this.getJMF().getDataFileNoSaved()!= null)
        jedf= this.getJMF().getDataFileNoSaved();
    else if (this.getJMF().getDataFile() != null)
        jedf= this.getJMF().getDataFile();
    else
        jedf= null;

    return jedf;
  }
//------------------------------------------------------------------------------
  public void setJExtDataFile (String jedf) {
    if (this.getJMF().getDataFileNoSaved()!= null)
        this.getJMF().setDataFileNoSaved(jedf);
    else
        this.getJMF().setDataFile(jedf);
  }
//------------------------------------------------------------------------------
  boolean[] ReduceRuleBase(int mod, boolean automatic) {
      //System.out.println("JEF: RRB: Simplify -> htsize="+jnikbct.getHashtableSize());
      //System.out.println("JEF: RRB: Simplify -> id="+jnikbct.getId());
    long ptrid= jnikbct.getId();
	Date d= new Date(System.currentTimeMillis());
	MessageKBCT.WriteLogFile("  "+"RRB: time begin -> "+DateFormat.getTimeInstance().format(d), "Simplify");
    boolean[] result= new boolean[2];
    result[0]= false;
    result[1]= false;

    if (mod==0) {
      this.setSimplifyQualityOptions(automatic);
    }
    JKBCT auxkbct= new JKBCT(this.Temp_kbct);
    String auxFile= this.Temp_kbct.GetKBCTFile()+".tmp.kb.xml";
    auxkbct.SetKBCTFile(auxFile);
    try {
        auxkbct.Save();
    } catch (Throwable t) {
        t.printStackTrace();
        MessageKBCT.Error( this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: ReduceRuleBase: saving auxkbct:   " + t );
    }
    //JConsistency jc= new JConsistency(this, auxkbct);
    JConsistency jc= new JConsistency(auxkbct);
    if (this.qold==null) {
    	this.qold= jc.KBquality(auxkbct, null, 0, 0, this.getJExtDataFile());
    }
    if (mod==0)
      MessageKBCT.Information(this, LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                                    "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                                    "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
    if (auxkbct.GetNbActiveRules()>0) {
      JKBCT newKBCT= jc.SimplifyRuleBase(auxkbct, this.getJExtDataFile(), this.qold);
      if (newKBCT != null) {
          newKBCT.SetKBCTFile(this.Temp_kbct.GetKBCTFile()+".tmp.kb.xml");
          //System.out.println("newKBCT -> "+newKBCT.GetPtr());
          boolean show= true;
          if (mod==0) {
            MessageKBCT.Information(this, LocaleKBCT.GetString("RuleBaseReductionEnded"));
          } else {
            show= false;
          }
          //System.out.println("RuleBaseReductionEnded -> Open JRuleFrame");
          JRuleFrame jrf= null;
          if (!this.Parent.KBDataFile.equals(""))
              jrf= new JRuleFrame(this, newKBCT, new JSemaphore(), true, false, true, show);
          else
              jrf= new JRuleFrame(this, newKBCT, new JSemaphore(), true, false, false, show);

          if (mod==1) {
            jrf.jMenuSave_actionPerformed(false);
            jrf.dispose();
          } else {
              String aux= this.Temp_kbct.GetKBCTFile();
        	  this.Temp_kbct= new JKBCT(aux);
              this.Temp_kbct.AddKBCTListener(this.KBCTListener);
        	  this.ReInitTableRules();
          }
          result[1]= true;
      } else {
        if (mod==0)
          MessageKBCT.Information(this, LocaleKBCT.GetString("NoRuleBaseReduction"));

        result[0]= true;
        d= new Date(System.currentTimeMillis());
        MessageKBCT.WriteLogFile("  "+"RRB: time end -> "+DateFormat.getTimeInstance().format(d), "Simplify");
        return result;
      }
    } else {
      result[0]= true;
    }
    d= new Date(System.currentTimeMillis());
    MessageKBCT.WriteLogFile("  "+"RRB: time end -> "+DateFormat.getTimeInstance().format(d), "Simplify");
    auxkbct.Close();
    auxkbct.Delete();
    if (automatic) {
      long[] exc= new long[8];
      exc[0]= this.Temp_kbct.GetPtr();
      exc[1]= this.Temp_kbct.GetCopyPtr();
      if (this.Parent.kbct_Data!=null) {
    	exc[2]= this.Parent.kbct_Data.GetPtr();
    	exc[3]= this.Parent.kbct_Data.GetCopyPtr();
      } else {
    	exc[2]= -1;
    	exc[3]= -1;
      }
      if (this.Parent.kbct!=null) {
    	exc[4]= this.Parent.kbct.GetPtr();
    	exc[5]= this.Parent.kbct.GetCopyPtr();
      } else {
    	exc[4]= -1;
    	exc[5]= -1;
      }
      if (this.kbct!=null) {
    	exc[6]= this.kbct.GetPtr();
    	exc[7]= this.kbct.GetCopyPtr();
      } else {
    	exc[6]= -1;
    	exc[7]= -1;
      }
      //System.out.println("JExpertFrame: ReduceRuleBase: "+automatic);
      //System.out.println("JExpertFrame: ReduceRuleBase: "+ptrid);
      //for (int n=0; n<8; n++) {
        // System.out.println("  -> exc["+n+"]="+exc[n]);
      //}
      jnikbct.cleanHashtable(ptrid,exc);
      //System.out.println("JEF: RRB: Simplify -> htsize="+jnikbct.getHashtableSize());
      //System.out.println("JEF: RRB: Simplify -> id="+jnikbct.getId());
    }
    return result;
  }
//------------------------------------------------------------------------------
  boolean[] SolveRedundancyRuleBase(JExtendedDataFile jedf) {
    boolean[] result= new boolean[3];
    result[0]= false;
    result[1]= false;
    result[2]= false;
    JKBCT auxkbct= new JKBCT(this.Temp_kbct);
    auxkbct.SetKBCTFile(this.Temp_kbct.GetKBCTFile()+".tmp.kb.xml");
    try {
       auxkbct.Save();
    } catch (Throwable t) {
       t.printStackTrace();
       MessageKBCT.Error( this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: SolveConflictsRuleBase: saving auxkbct:   " + t );
    }
    if (auxkbct.GetNbActiveRules()>0) {
      //JConsistency jc= new JConsistency(this, auxkbct);
      JConsistency jc= new JConsistency(auxkbct);
      this.jsp= new JSolveProblem(auxkbct, this, null);
      jc.AnalysisOfConsistency(false);
      Vector V_CONSISTENCY_ERRORS= jc.getVector(1);
      Enumeration CONSISTENCY_ERRORS= V_CONSISTENCY_ERRORS.elements();
      while (CONSISTENCY_ERRORS.hasMoreElements()) {
          InfoConsistency ic= (InfoConsistency)CONSISTENCY_ERRORS.nextElement();
          String message1= ic.getMessage1();
          if (message1.equals("SamePremiseSameConclussions")) {
               int ruleNum1= ic.getRuleNum1();
               int ruleNum2= ic.getRuleNum2();
               int[] SelRules= new int[2];
               if (ruleNum1 > ruleNum2) {
                 SelRules[0]= ruleNum2-1;
                 SelRules[1]= ruleNum1-1;
               } else {
                 SelRules[0]= ruleNum1-1;
                 SelRules[1]= ruleNum2-1;
               }
               this.jsp.setKBCT(auxkbct);
               String message= LocaleKBCT.GetString("TheRules") + " " +
                               ruleNum1 + " " +
                               LocaleKBCT.GetString("and") + " " +
                               ruleNum2 + " " +
                               LocaleKBCT.GetString(message1);

               MessageKBCT.WriteLogFile("  "+message, "Consistency");
               //int res= this.CompleteRule(ruleNum1);
               //if (res==0) {
               this.RemoveRule(SelRules[0]+1);
               //}
               result[1]= true;
               break;
          }
      }
    }
    if (!result[1]) {
      result[0]= true;
    }
    return result;
  }
//------------------------------------------------------------------------------
  boolean[] SolveConflictsRuleBase(JExtendedDataFile jedf) {
    boolean[] result= new boolean[3];
    result[0]= false;
    result[1]= false;
    result[2]= false;
    JKBCT auxkbct= new JKBCT(this.Temp_kbct);
    auxkbct.SetKBCTFile(this.Temp_kbct.GetKBCTFile()+".tmp.kb.xml");
    try {
       auxkbct.Save();
    } catch (Throwable t) {
       t.printStackTrace();
       MessageKBCT.Error( this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: SolveConflictsRuleBase: saving auxkbct:   " + t );
    }
    if (auxkbct.GetNbActiveRules()>0) {
      //JConsistency jc= new JConsistency(this, auxkbct);
      JConsistency jc= new JConsistency(auxkbct);
      this.jsp= new JSolveProblem(auxkbct, this, null);
      jc.AnalysisOfConsistency(false);
      Vector V_CONSISTENCY_ERRORS= jc.getVector(1);
      Enumeration CONSISTENCY_ERRORS= V_CONSISTENCY_ERRORS.elements();
      while (CONSISTENCY_ERRORS.hasMoreElements()) {
          InfoConsistency ic= (InfoConsistency)CONSISTENCY_ERRORS.nextElement();
          String message1= ic.getMessage1();
          if (message1.equals("SamePremiseDifferentConclussions")) {
               int ruleNum1= ic.getRuleNum1();
               int ruleNum2= ic.getRuleNum2();
               int[] SelRules= new int[2];
               if (ruleNum1 > ruleNum2) {
                 SelRules[0]= ruleNum2-1;
                 SelRules[1]= ruleNum1-1;
               } else {
                 SelRules[0]= ruleNum1-1;
                 SelRules[1]= ruleNum2-1;
               }
               this.jsp.setKBCT(auxkbct);
               String message= LocaleKBCT.GetString("TheRules") + " " +
                               ruleNum1 + " " +
                               LocaleKBCT.GetString("and") + " " +
                               ruleNum2 + " " +
                               LocaleKBCT.GetString(message1);

               MessageKBCT.WriteLogFile("  "+message, "Consistency");
               this.jsp.SolveConflictExpandingRules(SelRules, jedf, true, false, false);
               result[1]= true;
               this.jsp= null;
               break;
          } else if (message1.equals("IsIncomplete_ConclusionNotDefined")) {
              int ruleNum= ic.getRuleNum1();
              String message= LocaleKBCT.GetString("TheRule") + " " +
                              ruleNum + " " +
                              LocaleKBCT.GetString(message1);

              MessageKBCT.WriteLogFile("  "+message, "Consistency");
              int res= this.CompleteRule(ruleNum);
              if (res==0) {
                  this.RemoveRule(ruleNum);
              }
              result[1]= true;
              break;
          }
      }
      if (!result[1]) {
        Vector V_REDUNDANCY_OR_SPECIFICITY_WARNINGS= jc.getVector(2);
        Enumeration REDUNDANCY_OR_SPECIFICITY_WARNINGS= V_REDUNDANCY_OR_SPECIFICITY_WARNINGS.elements();
        while (REDUNDANCY_OR_SPECIFICITY_WARNINGS.hasMoreElements()) {
          InfoConsistency ic= (InfoConsistency)REDUNDANCY_OR_SPECIFICITY_WARNINGS.nextElement();
          String message1= ic.getMessage1();
          String message2= ic.getMessage2();
          int ruleNum1= ic.getRuleNum1();
          int ruleNum2= ic.getRuleNum2();
          if ( (message2.equals("DifferentConclusions")) && ( (message1.equals("HaveIntersectionNoEmpty")) || (message1.equals("HaveIntersectionNoEmptyCommomPart")) || (message1.equals("IsIncludedIntoTheOneCoveredByTheRule")) ) ) {
               int[] SelRules= new int[2];
               if (ruleNum1 > ruleNum2) {
                 SelRules[0]= ruleNum2-1;
                 SelRules[1]= ruleNum1-1;
               } else {
                 SelRules[0]= ruleNum1-1;
                 SelRules[1]= ruleNum2-1;
               }
               this.jsp.setKBCT(auxkbct);
               String message;
               if ( (message1.equals("HaveIntersectionNoEmpty")) || (message1.equals("HaveIntersectionNoEmptyCommomPart")) )
                   message= LocaleKBCT.GetString("TheRules") + " " +
                            ruleNum1 + " " +
                            LocaleKBCT.GetString("and") + " " +
                            ruleNum2 + " " +
                            LocaleKBCT.GetString("HaveIntersectionNoEmpty");
               else 
                   message= LocaleKBCT.GetString("TheInputSpaceCoveredByTheRule") + " " +
                            ruleNum1 + " " +
                            LocaleKBCT.GetString(message1) + " " +
                            ruleNum2;

               MessageKBCT.WriteLogFile("  "+message, "Consistency");
               boolean commonPart=false;
               if (ic.getMessage1().equals("HaveIntersectionNoEmptyCommomPart"))
              	 commonPart=true;

               this.jsp.SolveConflictExpandingRules(SelRules, jedf, true, commonPart, false);
               result[1]= true;
               this.jsp= null;
               break;
          } else if ( (message2.equals("SameConclusions")) && (message1.equals("HaveIntersectionNoEmpty")) ) {
              int[] SelRules= new int[2];
              if (ruleNum1 > ruleNum2) {
                SelRules[0]= ruleNum2-1;
                SelRules[1]= ruleNum1-1;
              } else {
                SelRules[0]= ruleNum1-1;
                SelRules[1]= ruleNum2-1;
              }
              this.jsp.setKBCT(auxkbct);
              String message= LocaleKBCT.GetString("TheRules") + " " +
                              ruleNum1 + " " +
                              LocaleKBCT.GetString("and") + " " +
                              ruleNum2 + " " +
                              LocaleKBCT.GetString("HaveIntersectionNoEmpty");

              MessageKBCT.WriteLogFile("  "+message, "Consistency");
              this.jsp.SolveConflictExpandingRules(SelRules, jedf, true, false, true);
              result[1]= true;
              this.jsp= null;
              break;
          }
        }
      }
    }
    if (!result[1]) {
      result[0]= true;
    }
    return result;
  }
//------------------------------------------------------------------------------
  boolean[] SolveCompletenessConflictsRuleBase(JExtendedDataFile jedf) {
    //System.out.println("JEF: SolveCompletenessConflictsRuleBase: confNum="+confNum);
    boolean[] result= new boolean[2];
    result[0]= false;
    result[1]= false;
    JKBCT auxkbct= new JKBCT(this.Temp_kbct);
    auxkbct.SetKBCTFile(this.Temp_kbct.GetKBCTFile()+".tmp.kb.xml");
    try {
       auxkbct.Save();
    } catch (Throwable t) {
       t.printStackTrace();
       MessageKBCT.Error( this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: SolveCompletenessConflictsRuleBase: saving auxkbct:   " + t );
    }
    if (auxkbct.GetNbActiveRules()>0) {
      //JConsistency jc= new JConsistency(this, auxkbct);
      JConsistency jc= new JConsistency(auxkbct);
      this.jsp= new JSolveProblem(auxkbct, this, null);
      jc.AnalysisOfConsistency(false);
      Vector V_CONSISTENCY_ERRORS= jc.getVector(1);
      Object[] obj=  V_CONSISTENCY_ERRORS.toArray();
      //Enumeration CONSISTENCY_ERRORS= V_CONSISTENCY_ERRORS.elements();
      int inir= 0;
      if ( (obj!=null) && (obj.length>0) )
          inir= ((InfoConsistency)obj[0]).getRuleNum1();
      
      //System.out.println("inir -> "+inir);
      for (int n= obj.length-1; n>=0; n--) {
      //while (CONSISTENCY_ERRORS.hasMoreElements()) {
          //InfoConsistency ic= (InfoConsistency)CONSISTENCY_ERRORS.nextElement();
          InfoConsistency ic= (InfoConsistency)obj[n];
          String message1= ic.getMessage1();
          if (message1.equals("IsIncomplete_ConclusionNotDefined")) {
              int ruleNumR= ic.getRuleNum1();
              //System.out.println("ruleNumR -> "+ruleNumR);
              //int ruleNum= ic.getRuleNum1()+confNum;
              //int LastRule= this.Temp_kbct.GetNbActiveRules();
              //if (ruleNum <= LastRule) {
              	//System.out.println("JEF: SolveCompletenessConflictsRuleBase: ruleNumR="+ruleNumR);
                Rule r= this.Temp_kbct.GetRule(ruleNumR);
                int outLab= this.CompleteRule(ruleNumR);
                if (outLab > 0) {
      		      String ruleMsg= "    "+LocaleKBCT.GetString("Rule")+" "+inir+": "+LocaleKBCT.GetString("If")+" ";
                  //Rule r= this.Temp_kbct.GetRule(LastRule);
                  int[] inputs= r.Get_in_labels_number();
      		      for (int m=0; m<NbIn-1; m++) {
      			     JKBCTInput in= this.Temp_kbct.GetInput(m+1);
	    	         ruleMsg= ruleMsg + in.GetName() + " " + LocaleKBCT.GetString("IS") + " " + in.GetLabelsName(inputs[m]-1) + " " + LocaleKBCT.GetString("AND")+ " ";
	              }
 			      JKBCTInput in= this.Temp_kbct.GetInput(NbIn);
                  JKBCTOutput out= this.Temp_kbct.GetOutput(1);
 			      ruleMsg= ruleMsg + in.GetName() + " " + LocaleKBCT.GetString("IS") + " " + in.GetLabelsName(inputs[NbIn-1]-1) + " " + LocaleKBCT.GetString("THEN")+ " " + out.GetName() + " " + LocaleKBCT.GetString("IS") + " " + out.GetLabelsName(outLab-1) ;
	              MessageKBCT.WriteLogFile(ruleMsg, "Completeness");
	              inir++;
                } else {
            	    this.RemoveRule(ruleNumR);
                    //String ruleMsg= "    "+LocaleKBCT.GetString("Rule")+" "+ruleNumR+": "+LocaleKBCT.GetString("Removed");
            	    //MessageKBCT.WriteLogFile(ruleMsg, "Completeness");
                }
                result[1]= true;
                //break;
             //}
          }
      }
    }
    auxkbct.Close();
    auxkbct.Delete();
    if (!result[1]) {
      result[0]= true;
    }
    return result;
  }
//------------------------------------------------------------------------------
  void setSimplifyQualityOptions(boolean automatic) {
    int option= 1;
    if (!automatic) {
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

  	  String message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
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
    }
    if (option==0) {
        int TC= 0;
        JExtendedDataFile jedf= this.getJExtDataFile();
        if (jedf!=null)
          TC= jedf.DataLength();

        new JSimpleFrame(this, this.Temp_kbct, TC);
    }
  }
//------------------------------------------------------------------------------
  void jButtonOptimization_actionPerformed(boolean automatic) {
      //System.out.println("JEF: Opt -> htsize="+jnikbct.getHashtableSize());
      //System.out.println("JEF: Opt -> id="+jnikbct.getId());
	    boolean warning= false;
	    for (int n=0; n<this.Temp_kbct.GetNbRules(); n++) {
	      if (!warning) {
	        Rule r= this.Temp_kbct.GetRule(n+1);
	        int[] out= r.Get_out_labels_number();
	        for (int m=0; m<out.length; m++) {
	          if (out[m]!=0)
	            warning= true;
	        }
	        warning= !warning;
	      }
	    }
	    if (!warning) {
		    // mode=0 -> Supervised
	    	// mode=1 -> Automatic
	    	int mode=1;
	    	int optOpt= MainKBCT.getConfig().GetOptOptimization();
	    	int algorithm= MainKBCT.getConfig().GetOptAlgorithm();
	    	if (!automatic) {
	    		optOpt= MessageKBCT.ChooseOptimization(this);
	    		if (optOpt == 0) {
	    			algorithm= MessageKBCT.PartitionOptimizationAlgorithm(this);
	    		    mode= MessageKBCT.SelectMode(this, LocaleKBCT.GetString("SelectOptimizationMode"));
	    	    }
	    	}
	    	if (optOpt==0) {
	    	  // Patition Tuning
	    	  if (algorithm==0) {
	    	    // GeneticTuning
	            try {
	              String BoundedOpt= LocaleKBCT.GetString("No");
	        	  if (MainKBCT.getConfig().GetBoundedOptimization())
	        		  BoundedOpt= LocaleKBCT.GetString("Yes");

	        	  String message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
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
	              int option=1;
	              if (!MainKBCT.getConfig().GetTESTautomatic())
	                  option= MessageKBCT.Confirm(this, message, 1, false, false, false);

	              if (option==0) {
	                  this.setOptimizationGTParameters();
	              }
	              if (!MainKBCT.getConfig().GetTESTautomatic()) {
	                   MessageKBCT.Information(this, LocaleKBCT.GetString("Processing")+" ..."+"\n"+
	                                           "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
	                                           "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
	              }
	              GeneticTuning gt;
	              long[] exc= new long[8];
	              exc[0]= this.Temp_kbct.GetPtr();
		          exc[1]= this.Temp_kbct.GetCopyPtr();
	              if (this.Parent.kbct_Data!=null) {
	              	  exc[2]= this.Parent.kbct_Data.GetPtr();
	                  exc[3]= this.Parent.kbct_Data.GetCopyPtr();
	              } else {
	               	  exc[2]= -1;
	                  exc[3]= -1;
	              }
	              if (this.Parent.kbct!=null) {
	                  exc[4]= this.Parent.kbct.GetPtr();
	                  exc[5]= this.Parent.kbct.GetCopyPtr();
                  } else {
	              	  exc[4]= -1;
	                  exc[5]= -1;
	              }
	              if (this.kbct!=null) {
	               	  exc[6]= this.kbct.GetPtr();
	                  exc[7]= this.kbct.GetCopyPtr();
                  } else {
	               	  exc[6]= -1;
	                  exc[7]= -1;
	              }
	              File fname= new File(this.Parent.IKBFile);
	     	      String name=fname.getName();
	     	      String LogName="LogOptimizationGT_"+name.substring(0,name.length()-8);
	     	      File f= JKBCTFrame.BuildFile(LogName);
	              String OptimizationLogFile= f.getAbsolutePath();
	        	  boolean aux= MainKBCT.getConfig().GetTESTautomatic();
	        	  MainKBCT.getConfig().SetTESTautomatic(true);
	              MessageKBCT.BuildLogFile(OptimizationLogFile+".txt", this.Parent.IKBFile, JKBCTFrame.KBExpertFile, this.Parent.KBDataFile, "Optimization");
	     	      String LogOGTconfig="LogOGTconfig_"+name.substring(0,name.length()-8);
	     	      File f_new= new File(f.getParentFile().getAbsolutePath()+System.getProperty("file.separator")+LogOGTconfig);
	     	      if (!f_new.exists()) {
	                  MessageKBCT.WriteLogFile((LocaleKBCT.GetString("AutomaticOptimizationProcedure")).toUpperCase(), "Optimization");
	                  MessageKBCT.WriteLogFile("----------------------------------", "Optimization");
	                  MessageKBCT.WriteLogFile(LocaleKBCT.GetString("GeneticTuning").toUpperCase(), "Optimization");
	                  gt= new GeneticTuning(this.Temp_kbct, this.getJExtDataFile(), exc, LogOGTconfig, false);
	              } else {
	                  gt= new GeneticTuning(this.Temp_kbct, this.getJExtDataFile(), exc, LogOGTconfig, true);
	              }
	        	  MainKBCT.getConfig().SetTESTautomatic(aux);
	              if (MainKBCT.getConfig().GetInitialGeneration()==LocaleKBCT.DefaultInitialGeneration()) {
	            	  //if (gt.GetKBCT()==null)
	            		//  System.out.println("GT kb null");
	            	  
	            	  if (gt.GetKBCT()!=null) {
	            	      this.Temp_kbct= gt.GetKBCT();
	            		  /*String auxname= this.Temp_kbct.GetKBCTFile();
	            		  System.out.println("auxname1: "+auxname);
	            		  JKBCT kbaux= gt.GetKBCT();
	            		  System.out.println("auxname2: "+kbaux.GetKBCTFile());
	            	      this.Temp_kbct= new JKBCT(kbaux);
	            	      this.Temp_kbct.SetKBCTFile(auxname);*/
	            	  }

	            	  MessageKBCT.WriteLogFile("----------------------------------", "Optimization");
	                  MessageKBCT.WriteLogFile(LocaleKBCT.GetString("AutomaticOptimizationEnded"), "Optimization");
	                  Date d= new Date(System.currentTimeMillis());
	                  MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "Optimization");
	              	  MessageKBCT.CloseLogFile("Optimization");
	                  if (!MainKBCT.getConfig().GetTESTautomatic()) {
	                      try {
	                    	  this.jfcOF= new JFISConsole(this, OptimizationLogFile+".txt", false);
	                      } catch (Throwable t) {
	                    	  t.printStackTrace();
	                    	  MessageKBCT.Error(null, t);
	                      }
	                    }
	              }	else {
	              	  MessageKBCT.CloseLogFile("Optimization");
	              }
	            } catch (Throwable t) {
	              t.printStackTrace();
	          	  MessageKBCT.Error(null,t);
	            }
	    	  } else {
	    		 //System.out.println("SW1: mode="+mode); 
 	    	     // solisWetts (FisPro)
	    		 if (!MainKBCT.getConfig().GetTESTautomatic()) {
	                 String SWopt="VariableByVariable";
	                 if (MainKBCT.getConfig().GetSWoption()==2)
	                     SWopt= "LabelByLabel";
	              	  
	                 String BoundedOpt= LocaleKBCT.GetString("No");
	        	     if (MainKBCT.getConfig().GetBoundedOptimization())
	        	         BoundedOpt= LocaleKBCT.GetString("Yes");

	        	     String message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
	                              +"   "+LocaleKBCT.GetString("SWoption")+"= "+LocaleKBCT.GetString(SWopt)+"\n"
	                              +"   "+LocaleKBCT.GetString("NbIterations")+"= "+MainKBCT.getConfig().GetNbIterations()+"\n"
	                              +"   "+LocaleKBCT.GetString("Cnear")+"= "+BoundedOpt+"\n"+"\n"
	                              +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
	                              +LocaleKBCT.GetString("DoYouWantToChangeSelectedValues");
	                 int option=1;
	                 if ( (!MainKBCT.getConfig().GetTESTautomatic()) && (!MainKBCT.getConfig().GetTutorialFlag()) )
	                     option= MessageKBCT.Confirm(this, message, 1, false, false, false);

	                 if (option==0) {
	                     this.setOptimizationSWParameters();
	                 }
	    		  }
	    		  if (mode==1) {
	    	    	 if (MainKBCT.getConfig().GetSWoption()==1)
	    		         this.OptimizeDBvariable();
	                 else
	    	             this.OptimizeDBlabels();
	    	      } else {
                      try {
	    	    	      int iniNbRules= this.Temp_kbct.GetNbRules();
	    	    	      int NbAddedRules=0;
	    	      	      int[] AddRules= new int[iniNbRules];
	    	              File temp= JKBCTFrame.BuildFile("tempExpRulesN.kb.xml");
	    	              for (int n=0; n<iniNbRules; n++) {
	    	        	      Rule r= this.Temp_kbct.GetRule(n+1);
	    	        	      AddRules[n]=0;
	    	      	          if (r.GetActive()) {
	    	                      JKBCT kbctaux= new JKBCT(this.Temp_kbct);
	    	                      kbctaux.SetKBCTFile(temp.getAbsolutePath());
	    	                      kbctaux.Save();
	    	    	    	      JConsistency caux= new JConsistency(kbctaux);
	    	    	    	      Rule[] rexp= caux.expandRule(kbctaux, n+1);
	    	    	      	      if ( (rexp!=null) && (rexp.length > 1) ) {
		    	                      this.Temp_kbct.SetRuleActive(n+1,false);
  	    	                          for (int m=0; m<rexp.length; m++) {
	    	                               this.Temp_kbct.AddRule(rexp[m]);
	    	                          }
	    	    	      	    	  NbAddedRules= NbAddedRules+rexp.length;
	    	    	        	      AddRules[n]=1;
	    	    	      	      }
	    	    	          }
	    	              }
	    	        	  new JOptFrame(this, this.Temp_kbct, this.getJExtDataFile(), false);
	    	        	  if (NbAddedRules > 0) {
                              //System.out.println("NR1="+this.Temp_kbct.GetNbRules());
	    	        	      for (int n=0; n<NbAddedRules; n++) {
	    	        	           this.Temp_kbct.RemoveRule(iniNbRules);
	    	                  }          
                              //System.out.println("NR2="+this.Temp_kbct.GetNbRules());
	    	                  for (int n=0; n<iniNbRules; n++) {
	    	         	           if (AddRules[n]==1)
		    	                       this.Temp_kbct.SetRuleActive(n+1,true);
	    	           	      }
	    	                  this.ReInitTableRules();
	    	        	  }
		              } catch (Throwable t) {
			              t.printStackTrace();
			          	  MessageKBCT.Error(null,t);
			          }
	    	    	}
	    	  }
	        } else if (optOpt==1) {
	        	// Rule Selection
	            try {
	                int option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeRuleSelection"), 0, false, false, false);
                    if (option==0) {
	          	        //System.out.println("RuleSelection 1");
                        String IntIndex= "";
                        int iindex= MainKBCT.getConfig().GetRuleSelectionInterpretabilityIndex();
                        switch (iindex) {
                          // NR
                          case 0: IntIndex= LocaleKBCT.GetString("IshibuchiNbRules");
                                  break;                        
                          // TRL
                          case 1: IntIndex= LocaleKBCT.GetString("IshibuchiTotalRuleLength");
                                  break;
                          // Max SFR
                          case 2: IntIndex= LocaleKBCT.GetString("MaxFiredRulesTraining");
                                  break;
                          // Average SFR
                          case 3: IntIndex= LocaleKBCT.GetString("AverageFiredRulesTraining");
                                  break;
                          // Min SFR
                          case 4: IntIndex= LocaleKBCT.GetString("MinFiredRulesTraining");
                                  break;
                          // AccRuleComp
                          case 5: IntIndex= LocaleKBCT.GetString("AccumulatedRuleComplexity");
                                  break;
                          // HILK IntIndex
                          case 6: IntIndex= LocaleKBCT.GetString("InterpretabilityIndex")+" (HILK)";
                                  break;
                          // LogView
                  		  case 7:  IntIndex= LocaleKBCT.GetString("LVindexTraining");
                                   break;
                          default: IntIndex= LocaleKBCT.GetString("IshibuchiTotalRuleLength");
                                   break;
                        }
                    	String message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
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

	                    if (option==0) {
	                        this.setOptimizationGARSParameters();
	                    }
 	                    //System.out.println("opt="+option);
	                    if (!MainKBCT.getConfig().GetTESTautomatic()) {
	                        MessageKBCT.Information(this, LocaleKBCT.GetString("Processing")+" ..."+"\n"+
	                                             "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
	                                             "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
	                    }
	                    //this.RuleSelection();
 	          	        //System.out.println("RuleSelection 2");
	         	        File fname= new File(JKBCTFrame.KBExpertFile);
	       	            String name=fname.getName();
	       	            String LogName="LogOptimizationGARS_"+name.substring(0,name.length()-7);
	       	            File f= JKBCTFrame.BuildFile(LogName);
	                    String OptimizationLogFile= f.getAbsolutePath();
    	        	    boolean aux= MainKBCT.getConfig().GetTESTautomatic();
    	        	    MainKBCT.getConfig().SetTESTautomatic(true);
	      	            MessageKBCT.BuildLogFile(OptimizationLogFile+".txt", this.Parent.IKBFile, JKBCTFrame.KBExpertFile, this.Parent.KBDataFile, "Optimization");
	       	            String LogOGARSconfig="LogOGARSconfig_"+name.substring(0,name.length()-7);
	       	            File f_new= new File(f.getParentFile().getAbsolutePath()+System.getProperty("file.separator")+LogOGARSconfig);
	       	            //System.out.println(f_new.getAbsolutePath());
                        GAruleSelection gars;
      	                long[] exc= new long[8];
    	                exc[0]= this.Temp_kbct.GetPtr();
    		            exc[1]= this.Temp_kbct.GetCopyPtr();
    	                if (this.Parent.kbct_Data!=null) {
    	              	    exc[2]= this.Parent.kbct_Data.GetPtr();
    	                    exc[3]= this.Parent.kbct_Data.GetCopyPtr();
    	                } else {
    	               	    exc[2]= -1;
    	                    exc[3]= -1;
    	                }
    	                if (this.Parent.kbct!=null) {
    	                    exc[4]= this.Parent.kbct.GetPtr();
    	                    exc[5]= this.Parent.kbct.GetCopyPtr();
                        } else {
    	              	    exc[4]= -1;
    	                    exc[5]= -1;
    	                }
    	                if (this.kbct!=null) {
    	               	    exc[6]= this.kbct.GetPtr();
    	                    exc[7]= this.kbct.GetCopyPtr();
                        } else {
    	               	    exc[6]= -1;
    	                    exc[7]= -1;
    	                }
	       	            if (!f_new.exists()) {
	                        MessageKBCT.WriteLogFile((LocaleKBCT.GetString("AutomaticOptimizationProcedure")).toUpperCase(), "Optimization");
	                        MessageKBCT.WriteLogFile("----------------------------------", "Optimization");
	                        MessageKBCT.WriteLogFile(LocaleKBCT.GetString("RuleSelection").toUpperCase(), "Optimization");
	                        gars= new GAruleSelection(this.Temp_kbct, this.getJExtDataFile(), exc, LogOGARSconfig, false);
	                    } else {
	                        gars= new GAruleSelection(this.Temp_kbct, this.getJExtDataFile(), exc, LogOGARSconfig, true);
	                    }
    	        	    MainKBCT.getConfig().SetTESTautomatic(aux);
	          	        //System.out.println("RuleSelection 3");
	          	        //System.out.println("  -> "+MainKBCT.getConfig().GetRuleSelectionInitialGeneration());
	                    if (MainKBCT.getConfig().GetRuleSelectionInitialGeneration()==LocaleKBCT.DefaultRuleSelectionInitialGeneration()) {
	              	        //System.out.println("RuleSelection 4");
	  	            	    if (gars.GetKBCT()!=null) {
		              	        this.Temp_kbct= gars.GetKBCT();
		            	    }
	              	        //System.out.println("NbRules="+this.Temp_kbct.GetNbActiveRules());
	              	        //this.ReInitTableRules();
	                        MessageKBCT.WriteLogFile("----------------------------------", "Optimization");
	                        MessageKBCT.WriteLogFile(LocaleKBCT.GetString("AutomaticOptimizationEnded"), "Optimization");
	                        Date d= new Date(System.currentTimeMillis());
	                        MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "Optimization");
	                	    MessageKBCT.CloseLogFile("Optimization");
	                        if (!MainKBCT.getConfig().GetTESTautomatic()) {
	                            try {
	                      	        this.jfcOF= new JFISConsole(this, OptimizationLogFile+".txt", false);
	                            } catch (Throwable t) {
	                        	    t.printStackTrace();
	                        	    MessageKBCT.Error(null, t);
	                            }
	                        }
	                   } else {
	                	    MessageKBCT.CloseLogFile("Optimization");
	                   }
	                 }
	              } catch (Throwable t) {
	                  t.printStackTrace();
	            	  MessageKBCT.Error(null,t);
	              }
	        }
	    } else {
	        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("SelectALabelForEachOutput"));
	    }
	    //System.out.println("JEF: Opt -> htsize="+jnikbct.getHashtableSize());
	    //System.out.println("JEF: Opt -> id="+jnikbct.getId());
  }
//------------------------------------------------------------------------------
  private void OptimizeDBlabels() {
		// solisWetts (FisPro)
		//System.out.println("OptimizeDBlabels()"); 
    try {
          if ( (!MainKBCT.getConfig().GetTESTautomatic()) && (!MainKBCT.getConfig().GetTutorialFlag()) ) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                      "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                      "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
          }
   	      File fname= new File(JKBCTFrame.KBExpertFile);
 	      String name=fname.getName();
 	      String LogName="LogOptimizationSW_"+name.substring(0,name.length()-7);
    	  String OptimizationLogFile= (JKBCTFrame.BuildFile(LogName)).getAbsolutePath();
    	  MessageKBCT.BuildLogFile(OptimizationLogFile+".txt", this.Parent.IKBFile, JKBCTFrame.KBExpertFile, this.Parent.KBDataFile, "Optimization");
          MessageKBCT.WriteLogFile((LocaleKBCT.GetString("AutomaticOptimizationProcedure")).toUpperCase(), "Optimization");
          MessageKBCT.WriteLogFile("----------------------------------", "Optimization");
          MessageKBCT.WriteLogFile(LocaleKBCT.GetString("SolisWetts").toUpperCase(), "Optimization");

          String SWopt= "LabelByLabel";
          MessageKBCT.WriteLogFile(LocaleKBCT.GetString("SWoption")+"= "+LocaleKBCT.GetString(SWopt), "Optimization");
          MessageKBCT.WriteLogFile(LocaleKBCT.GetString("NbIterations")+"= "+MainKBCT.getConfig().GetNbIterations(), "Optimization");
          String BoundedOpt= LocaleKBCT.GetString("No");
    	  if (MainKBCT.getConfig().GetBoundedOptimization())
    		  BoundedOpt= LocaleKBCT.GetString("Yes");

    	  MessageKBCT.WriteLogFile(LocaleKBCT.GetString("Cnear")+"= "+BoundedOpt, "Optimization");
          MessageKBCT.WriteLogFile("----------------------------------", "Optimization");
          /// Ordenar las variables por orden de aparicion
          int[] premByVar= new int[this.Temp_kbct.GetNbInputs()];
          for (int n=0; n<premByVar.length; n++) {
          	premByVar[n]=0;
          }    
          for (int n=1; n<=this.Temp_kbct.GetNbRules(); n++) {
              Rule r = this.Temp_kbct.GetRule(n);
              if (r.GetActive()) {
                int[] aux= r.Get_in_labels_number();
                for (int k=0; k<aux.length; k++) {
                     if (aux[k]!=0)
                  	   premByVar[k]++;
                }
              }
          }    
          int[] orderedVar= new int[this.Temp_kbct.GetNbInputs()];
          for (int i=0; i<orderedVar.length; i++) {
         	    orderedVar[i]= i+1;
          }
          for (int i=1; i<orderedVar.length; i++) {
               for (int k=0; k<i; k++) {
                    if (premByVar[orderedVar[i]-1]>premByVar[orderedVar[k]-1]) {
                        for (int m=i; m>k; m--) {
                            orderedVar[m]=orderedVar[m-1];
                        }
                        orderedVar[k]=i+1;
                        break;
                    }
               }
           }
        /////////////////////////////////////////////////
	      int iniNbRules= this.Temp_kbct.GetNbRules();
          Vector<int[]> vars= new Vector<int[]>();
          for (int m = 0; m < orderedVar.length; m++) {
        	   JKBCTInput jin=this.kbct.GetInput(orderedVar[m]);
               int NbLab=jin.GetLabelsNumber();
        	   int[] UsedLabelsByInput= new int[NbLab];
               for (int k=0; k<NbLab; k++) {
        	        UsedLabelsByInput[k]=0;
               }
               if ( (!jin.GetTrust().equals("hhigh")) && (jin.GetType().equals("numerical")) ) {
                 for (int n=0; n<iniNbRules; n++) {
  	                Rule r= this.Temp_kbct.GetRule(n+1);
  	                if (r.GetActive()) {
  	                    int SelLabel= r.Get_in_labels_number()[orderedVar[m]-1];
        	            //System.out.println("var:"+orderedVar[m]+"  SelLabel="+SelLabel);
                        // Basic Label
  	                    if ( (SelLabel > 0) && (SelLabel<=NbLab) )
  	        	           UsedLabelsByInput[SelLabel - 1]= 1;
                        // NOT Label
  	                    if ( (SelLabel>NbLab) && (SelLabel<=2*NbLab) )
   	        	           UsedLabelsByInput[SelLabel - 1 - NbLab]= 1;
  	                    // OR Label
  	                    if (SelLabel>2*NbLab) {
  	        	            int NbORlabels= jnikbct.NbORLabels(SelLabel-2*NbLab, NbLab);
  	        	            int firstLab= jnikbct.option(SelLabel-2*NbLab, NbORlabels, NbLab);
  	        	            int lastLab= firstLab + NbORlabels-1;
  	        	            //System.out.println("first="+firstLab+"  last="+lastLab);
  	        	            UsedLabelsByInput[firstLab - 1]= 1;
  	        	            UsedLabelsByInput[lastLab - 1]= 1;
  	                    }
  	                }
  	           }
             }
  	         vars.add(UsedLabelsByInput);
          }
	      int NbAddedRules=0;
  	      int[] AddRules= new int[iniNbRules];
          File temp= JKBCTFrame.BuildFile("tempExpRulesN.kb.xml");
          for (int n=0; n<iniNbRules; n++) {
    	      Rule r= this.Temp_kbct.GetRule(n+1);
    	      AddRules[n]=0;
  	          if (r.GetActive()) {
                  JKBCT kbctaux= new JKBCT(this.Temp_kbct);
                  kbctaux.SetKBCTFile(temp.getAbsolutePath());
                  kbctaux.Save();
	    	      JConsistency caux= new JConsistency(kbctaux);
	    	      Rule[] rexp= caux.expandRule(kbctaux, n+1);
	      	      if ( (rexp!=null) && (rexp.length > 1) ) {
                      this.Temp_kbct.SetRuleActive(n+1,false);
                        for (int m=0; m<rexp.length; m++) {
                           this.Temp_kbct.AddRule(rexp[m]);
                      }
	      	    	  NbAddedRules= NbAddedRules+rexp.length;
	        	      AddRules[n]=1;
	      	      }
	          }
          }
          
          Object[] obj= vars.toArray();
          int[] contVar= new int[orderedVar.length];
          for (int m=0; m<orderedVar.length; m++)
            contVar[m]=0;
          
          JOptFrame jof= new JOptFrame(this, this.Temp_kbct, this.getJExtDataFile(), true);
          for (int m = 0; m < orderedVar.length; m++) {
        	  contVar[m]++;
        	  //System.out.println("var: "+orderedVar[m]);
              if ( (!this.Temp_kbct.GetInput(orderedVar[m]).GetTrust().equals("hhigh")) && (contVar[m]<=5) ) {
                MessageKBCT.WriteLogFile(LocaleKBCT.GetString("Input")+"="+orderedVar[m], "Optimization");
        	    Date d= new Date(System.currentTimeMillis());
                MessageKBCT.WriteLogFile("time begin -> "+DateFormat.getTimeInstance().format(d), "Optimization");
                int NbLab=this.kbct.GetInput(orderedVar[m]).GetLabelsNumber();
                //System.out.println("NbLab="+NbLab);
                int[] contLab= new int[NbLab];
                for (int k=0; k<NbLab; k++)
            	    contLab[k]=0;

                boolean warningAccImprovement=false;
                int[] UsedLabels= (int[])obj[m];
                //for (int k=0; k<UsedLabels.length; k++) {
                	//System.out.println("UL("+k+") -> "+UsedLabels[k]);
                //}
                for (int k=0; k<NbLab; k++) {
            	 long TM=Runtime.getRuntime().totalMemory();
            	 long MM=Runtime.getRuntime().maxMemory();
     	         //System.out.println("MM -> "+MM);
     	         //System.out.println("TM -> "+TM);
     	         //System.out.println("free memory -> "+String.valueOf(MM-TM));
            	 if (TM>=MM) {
                         if (!MainKBCT.getConfig().GetTESTautomatic()) {
            	    	       String msg= LocaleKBCT.GetString("WarningSWHalted")+"\n"+
                               LocaleKBCT.GetString("WarningJVMOutOfMemory")+"\n"+
                               LocaleKBCT.GetString("WarningReleaseMemory");
                               if (!MainKBCT.flagHalt) {
            	    	           MessageKBCT.Information(null, msg);
                               }
                         }
                         if (!MainKBCT.flagHalt) {
                             MessageKBCT.WriteLogFile("    "+LocaleKBCT.GetString("WarningSWHalted"), "Optimization");
                             MainKBCT.flagHalt= true;
                         }
                         break;
            	 } else {
                   if (UsedLabels[k]==1) {
            	       contLab[k]++;
                       if (contLab[k]<=10) {
                         MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("Label")+"="+String.valueOf(k+1), "Optimization");
                         d= new Date(System.currentTimeMillis());
                         MessageKBCT.WriteLogFile("    "+"time begin -> "+DateFormat.getTimeInstance().format(d), "Optimization");
                         if (m>=0) {
                       	   //System.out.println(" -> l: "+String.valueOf(k+1));
   			               String r= jof.Apply(orderedVar[m], k+1);
   			               if ( r.equals(LocaleKBCT.GetString("AccuracyImprovement")) ) {
   	                         MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("AccuracyImprovement"), "Optimization");
   	                         warningAccImprovement= true;
   			    	         k=-1;
   			               }	
                           d= new Date(System.currentTimeMillis());
                           MessageKBCT.WriteLogFile("    "+"time end -> "+DateFormat.getTimeInstance().format(d), "Optimization");
                         }
                       }
                  }
                  if (warningAccImprovement)
            	    m=-1;

                  d= new Date(System.currentTimeMillis());
                  MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "Optimization");
                  MessageKBCT.WriteLogFile("", "Optimization");
                  }
               }
            }
          }
          jof.dispose();
          jof=null;
    	  if (NbAddedRules > 0) {
              //System.out.println("NR1="+this.Temp_kbct.GetNbRules());
    	      for (int n=0; n<NbAddedRules; n++) {
    	           this.Temp_kbct.RemoveRule(iniNbRules);
              }          
              //System.out.println("NR2="+this.Temp_kbct.GetNbRules());
              for (int n=0; n<iniNbRules; n++) {
     	           if (AddRules[n]==1)
                       this.Temp_kbct.SetRuleActive(n+1,true);
       	      }
              if (!MainKBCT.getConfig().GetTESTautomatic())
                  this.ReInitTableRules();
    	  }
          MessageKBCT.WriteLogFile("----------------------------------", "Optimization");
          MessageKBCT.WriteLogFile(LocaleKBCT.GetString("AutomaticOptimizationEnded"), "Optimization");
          Date d= new Date(System.currentTimeMillis());
          MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "Optimization");
      	  MessageKBCT.CloseLogFile("Optimization");
          if ( (!MainKBCT.getConfig().GetTESTautomatic()) || (MainKBCT.getConfig().GetTutorialFlag()) ) {
            try {
          	    this.jfcOF= new JFISConsole(this, OptimizationLogFile+".txt", false);
            } catch (Throwable t) {
            	t.printStackTrace();
            	MessageKBCT.Error(null, t);
            }
          }
    } catch (Throwable t) {
        t.printStackTrace();
        MessageKBCT.WriteLogFile(t.getLocalizedMessage(), "Optimization");
        if (!MainKBCT.getConfig().GetTESTautomatic()) {
            MessageKBCT.Error(null, t);
        }
    }
  }
//------------------------------------------------------------------------------
  private void OptimizeDBvariable() {
		//System.out.println("OptimizeDBvariable()"); 
		// solisWetts (FisPro)
    try {
          if ( (!MainKBCT.getConfig().GetTESTautomatic()) && (!MainKBCT.getConfig().GetTutorialFlag()) ) {
              MessageKBCT.Information(this, LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                      "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                      "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
          }
    	  String OptimizationLogFile= (JKBCTFrame.BuildFile("LogOptimizationSW")).getAbsolutePath();
    	  MessageKBCT.BuildLogFile(OptimizationLogFile+".txt", this.Parent.IKBFile, JKBCTFrame.KBExpertFile, this.Parent.KBDataFile, "Optimization");
          MessageKBCT.WriteLogFile((LocaleKBCT.GetString("AutomaticOptimizationProcedure")).toUpperCase(), "Optimization");
          MessageKBCT.WriteLogFile("----------------------------------", "Optimization");
          MessageKBCT.WriteLogFile(LocaleKBCT.GetString("SolisWetts").toUpperCase(), "Optimization");
          String SWopt="VariableByVariable";
          MessageKBCT.WriteLogFile(LocaleKBCT.GetString("SWoption")+"= "+LocaleKBCT.GetString(SWopt), "Optimization");
          MessageKBCT.WriteLogFile(LocaleKBCT.GetString("NbIterations")+"= "+MainKBCT.getConfig().GetNbIterations(), "Optimization");
          String BoundedOpt= LocaleKBCT.GetString("No");
    	  if (MainKBCT.getConfig().GetBoundedOptimization())
    		  BoundedOpt= LocaleKBCT.GetString("Yes");

    	  MessageKBCT.WriteLogFile(LocaleKBCT.GetString("Cnear")+"= "+BoundedOpt, "Optimization");
          MessageKBCT.WriteLogFile("----------------------------------", "Optimization");

          /// Ordenar las variables por orden de aparicion
          int[] premByVar= new int[this.Temp_kbct.GetNbInputs()];
          for (int n=0; n<premByVar.length; n++) {
          	premByVar[n]=0;
          }
          //System.out.println("this.Temp_kbct.GetPtr()="+this.Temp_kbct.GetPtr());
          //System.out.println("this.Temp_kbct.GetCopyPtr()="+this.Temp_kbct.GetCopyPtr());
          //System.out.println("NR="+this.Temp_kbct.GetNbRules());
          for (int n=1; n<=this.Temp_kbct.GetNbRules(); n++) {
              Rule r = this.Temp_kbct.GetRule(n);
              if (r.GetActive()) {
                int[] aux= r.Get_in_labels_number();
                for (int k=0; k<aux.length; k++) {
                     if (aux[k]!=0)
                  	   premByVar[k]++;
                }
              }
          }    
          int[] orderedVar= new int[this.Temp_kbct.GetNbInputs()];
          for (int i=0; i<orderedVar.length; i++) {
         	    orderedVar[i]= i+1;
          }
          for (int i=1; i<orderedVar.length; i++) {
               for (int k=0; k<i; k++) {
                    if (premByVar[orderedVar[i]-1]>premByVar[orderedVar[k]-1]) {
                        for (int m=i; m>k; m--) {
                            orderedVar[m]=orderedVar[m-1];
                        }
                        orderedVar[k]=i+1;
                        break;
                    }
               }
           }
        /////////////////////////////////////////////////
  	      int iniNbRules= this.Temp_kbct.GetNbRules();
          //System.out.println("JExpertFrame: OptimizeDBvariable1: "+iniNbRules+" rules");
          Vector<int[]> vars= new Vector<int[]>();

          //System.out.println("JExpertFrame: OptimizeDBvariable10");
          int[] contVar= new int[orderedVar.length];
          for (int m=0; m<orderedVar.length; m++)
            contVar[m]=0;

          //System.out.println("JExpertFrame: OptimizeDBvariable11");
          //System.out.println("this.Temp_kbct.GetPtr()="+this.Temp_kbct.GetPtr());
          //System.out.println("this.Temp_kbct.GetCopyPtr()="+this.Temp_kbct.GetCopyPtr());
          for (int m = 0; m < orderedVar.length; m++) {
        	   JKBCTInput jin=this.kbct.GetInput(orderedVar[m]);
               int NbLab=jin.GetLabelsNumber();
        	   int[] UsedLabelsByInput= new int[NbLab];
               for (int k=0; k<NbLab; k++) {
        	        UsedLabelsByInput[k]=0;
               }
               if ( (!jin.GetTrust().equals("hhigh")) && (jin.GetType().equals("numerical")) ) {
                 for (int n=0; n<iniNbRules; n++) {
  	                Rule r= this.Temp_kbct.GetRule(n+1);
  	                if (r.GetActive()) {
  	                    int SelLabel= r.Get_in_labels_number()[orderedVar[m]-1];
                        // Basic Label
  	                    if ( (SelLabel > 0) && (SelLabel<=NbLab) )
  	        	           UsedLabelsByInput[SelLabel - 1] = 1;
                        // NOT Label
  	                    if ( (SelLabel>NbLab) && (SelLabel<=2*NbLab) )
  	        	           UsedLabelsByInput[SelLabel - 1 - NbLab] = 1;
                        // OR Label
  	                    if ( (NbLab>2) && (SelLabel>2*NbLab) ) {
  	        	            int NbORlabels= jnikbct.NbORLabels(SelLabel-2*NbLab, NbLab);
  	        	            int firstLab= jnikbct.option(SelLabel-2*NbLab, NbORlabels, NbLab);
  	        	            int lastLab= firstLab + NbORlabels-1;
  	        	            UsedLabelsByInput[firstLab - 1] = 1;
  	        	            UsedLabelsByInput[lastLab - 1] = 1;
  	                    }
  	                }
  	           }
             }
  	         vars.add(UsedLabelsByInput);
          }
          //System.out.println("this.Temp_kbct.GetPtr()="+this.Temp_kbct.GetPtr());
          //System.out.println("this.Temp_kbct.GetCopyPtr()="+this.Temp_kbct.GetCopyPtr());
	      int NbAddedRules=0;
  	      int[] AddRules= new int[iniNbRules];
          File temp= JKBCTFrame.BuildFile("tempExpRulesN.kb.xml");
          for (int n=0; n<iniNbRules; n++) {
    	      Rule r= this.Temp_kbct.GetRule(n+1);
    	      AddRules[n]=0;
  	          if (r.GetActive()) {
                  JKBCT kbctaux= new JKBCT(this.Temp_kbct);
                  kbctaux.SetKBCTFile(temp.getAbsolutePath());
                  kbctaux.Save();
	    	      JConsistency caux= new JConsistency(kbctaux);
	    	      Rule[] rexp= caux.expandRule(kbctaux, n+1);
	      	      if ( (rexp!=null) && (rexp.length > 1) ) {
                      this.Temp_kbct.SetRuleActive(n+1,false);
                        for (int m=0; m<rexp.length; m++) {
                           this.Temp_kbct.AddRule(rexp[m]);
                      }
	      	    	  NbAddedRules= NbAddedRules+rexp.length;
	        	      AddRules[n]=1;
	      	      }
	      	      kbctaux.Close();
	      	      kbctaux.Delete();
	          }
          }
          //System.out.println("JExpertFrame: OptimizeDBvariable13");
          Object[] obj= vars.toArray();
          //System.out.println("this.Temp_kbct.GetPtr()="+this.Temp_kbct.GetPtr());
          //System.out.println("this.Temp_kbct.GetCopyPtr()="+this.Temp_kbct.GetCopyPtr());
          //System.out.println("this.Temp_kbct -> "+this.Temp_kbct.GetNbRules());
    	  if (NbAddedRules > 0) {
              //System.out.println("NR1="+this.Temp_kbct.GetNbRules());
    	      for (int n=0; n<NbAddedRules; n++) {
    	           this.Temp_kbct.RemoveRule(iniNbRules);
              }          
              //System.out.println("NR2="+this.Temp_kbct.GetNbRules());
              for (int n=0; n<iniNbRules; n++) {
     	           if (AddRules[n]==1)
                       this.Temp_kbct.SetRuleActive(n+1,true);
       	      }
              //System.out.println("NR3="+this.Temp_kbct.GetNbActiveRules());
              if (!MainKBCT.getConfig().GetTESTautomatic())
                  this.ReInitTableRules();
    	  }
          JOptFrame jof= new JOptFrame(this, this.Temp_kbct, this.getJExtDataFile(), true);
          //System.out.println("this.Temp_kbct.GetPtr()="+this.Temp_kbct.GetPtr());
          //System.out.println("this.Temp_kbct.GetCopyPtr()="+this.Temp_kbct.GetCopyPtr());
          for (int m=0; m < orderedVar.length; m++) {
        	  contVar[m]++;
        	  //System.out.println("var: "+orderedVar[m]);
              if ( (!this.Temp_kbct.GetInput(orderedVar[m]).GetTrust().equals("hhigh")) && (contVar[m]<=5) ) {
     	        long TM=Runtime.getRuntime().totalMemory();
    	        long MM=Runtime.getRuntime().maxMemory();
    	        //System.out.println("MM -> "+MM);
    	        //System.out.println("TM -> "+TM);
    	        //System.out.println("free memory -> "+String.valueOf(MM-TM));
    	        if (TM>=MM) {
                     if (!MainKBCT.getConfig().GetTESTautomatic()) {
    	    	       String msg= LocaleKBCT.GetString("WarningSWHalted")+"\n"+
                       LocaleKBCT.GetString("WarningJVMOutOfMemory")+"\n"+
                       LocaleKBCT.GetString("WarningReleaseMemory");
                       if (!MainKBCT.flagHalt) {
    	    	           MessageKBCT.Information(null, msg);
                       }
                     }
                     if (!MainKBCT.flagHalt) {
                         MessageKBCT.WriteLogFile("    "+LocaleKBCT.GetString("WarningSWHalted"), "Optimization");
                         MainKBCT.flagHalt= true;
                     }
                     break;
    	        } else {
                   MessageKBCT.WriteLogFile(LocaleKBCT.GetString("Input")+"="+orderedVar[m], "Optimization");
            	   Date d= new Date(System.currentTimeMillis());
                   MessageKBCT.WriteLogFile("time begin -> "+DateFormat.getTimeInstance().format(d), "Optimization");
                   boolean warningAccImprovement=false;
                   int[] UsedLabels= (int[])obj[m];
                   //System.out.println("JOptFrame: Apply");
                   //for (int k=0; k<UsedLabels.length; k++) {
                     //  System.out.println("JOptFrame: UsedLabels -> "+UsedLabels[k]);
                   //}
   			       String r= jof.Apply(orderedVar[m], UsedLabels);
   		           //System.out.println("this.Temp_kbct.GetPtr()="+this.Temp_kbct.GetPtr());
   		           //System.out.println("this.Temp_kbct.GetCopyPtr()="+this.Temp_kbct.GetCopyPtr());
                   //System.out.println("JOptFrame: END");
   			       if ( r.equals(LocaleKBCT.GetString("AccuracyImprovement")) ) {
   	                 MessageKBCT.WriteLogFile("      "+LocaleKBCT.GetString("AccuracyImprovement"), "Optimization");
	                 warningAccImprovement= true;
   			       }	

                   if (warningAccImprovement)
            	      m=-1;

                   d= new Date(System.currentTimeMillis());
                   MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "Optimization");
                   MessageKBCT.WriteLogFile("", "Optimization");
               }
            }
          }
          jof.dispose();
          jof=null;
          //System.out.println("this.Temp_kbct.GetPtr()="+this.Temp_kbct.GetPtr());
          //System.out.println("this.Temp_kbct.GetCopyPtr()="+this.Temp_kbct.GetCopyPtr());
    	  /*if (NbAddedRules > 0) {
              System.out.println("NR1="+this.Temp_kbct.GetNbRules());
    	      for (int n=0; n<NbAddedRules; n++) {
    	           this.Temp_kbct.RemoveRule(iniNbRules);
              }          
              System.out.println("NR2="+this.Temp_kbct.GetNbRules());
              for (int n=0; n<iniNbRules; n++) {
     	           if (AddRules[n]==1)
                       this.Temp_kbct.SetRuleActive(n+1,true);
       	      }
              System.out.println("NR3="+this.Temp_kbct.GetNbActiveRules());
              if (!MainKBCT.getConfig().GetTESTautomatic())
                  this.ReInitTableRules();
    	  }*/
          //System.out.println("this.Temp_kbct.GetPtr()="+this.Temp_kbct.GetPtr());
          //System.out.println("this.Temp_kbct.GetCopyPtr()="+this.Temp_kbct.GetCopyPtr());
          MessageKBCT.WriteLogFile("----------------------------------", "Optimization");
          MessageKBCT.WriteLogFile(LocaleKBCT.GetString("AutomaticOptimizationEnded"), "Optimization");
          Date d= new Date(System.currentTimeMillis());
          MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "Optimization");
      	  MessageKBCT.CloseLogFile("Optimization");
          if ( (!MainKBCT.getConfig().GetTESTautomatic()) || (MainKBCT.getConfig().GetTutorialFlag()) ) {
            try {
          	  this.jfcOF= new JFISConsole(this, OptimizationLogFile+".txt", false);
            } catch (Throwable t) {
            	t.printStackTrace();
            	MessageKBCT.Error(null, t);
            }
          }
    } catch (Throwable t) {
        t.printStackTrace();
        MessageKBCT.WriteLogFile(t.getLocalizedMessage(), "Optimization");
        if (!MainKBCT.getConfig().GetTESTautomatic()) {
            MessageKBCT.Error(null, t);
        }
    }
  }
//------------------------------------------------------------------------------
  void jButtonCompleteness_actionPerformed(boolean automatic) {
      //System.out.println("JEF: Completeness -> htsize="+jnikbct.getHashtableSize());
      //System.out.println("JEF: Completeness -> id="+jnikbct.getId());
      long ptrid= jnikbct.getId();
	    boolean warning= false;
	    if (MainKBCT.getConfig().GetTESTautomatic()) {
	        for (int n=0; n<this.Temp_kbct.GetNbRules(); n++) {
	             if (!warning) {
	                 Rule r= this.Temp_kbct.GetRule(n+1);
	                 int[] out= r.Get_out_labels_number();
	                 for (int m=0; m<out.length; m++) {
	                      if (out[m]!=0)
	                          warning= true;
	                 }
	                 warning= !warning;
	             }
	        }
	    }
	    if (!warning) {
	    	// mode=0 -> Supervised
	    	// mode=1 -> Automatic
	    	int mode=1;
	    	if (!automatic)
	    	    mode= MessageKBCT.SelectMode(this, LocaleKBCT.GetString("SelectCompletenessMode"));

	    	File fname= new File(JKBCTFrame.KBExpertFile);
	     	String name=fname.getName();
	     	String LogName="LogCompleteness_"+name.substring(0,name.length()-7);
	     	String CompletenessLogFile= (JKBCTFrame.BuildFile(LogName)).getAbsolutePath();
	        MessageKBCT.BuildLogFile(CompletenessLogFile+".txt", this.Parent.IKBFile, JKBCTFrame.KBExpertFile, this.Parent.KBDataFile, "Completeness");
	        MessageKBCT.WriteLogFile((LocaleKBCT.GetString("AutomaticCompletenessProcedure")).toUpperCase(), "Completeness");
	        MessageKBCT.WriteLogFile("----------------------------------", "Completeness");
	        warning=false;
	        if (mode == 0) {
	            Object option=null;
	            if (!this.Parent.KBDataFile.equals(""))
	              option= MessageKBCT.CompletenessOptions(this);

	            if (option != null) {
	              if (option.equals(LocaleKBCT.GetString("RuleBaseCompletenessAnalysis"))) {
	                  Rule[] rr= this.RuleBaseCompletenessAnalysis(mode, automatic);
	                  if (rr!=null) {
	                	  for (int n=0; n<rr.length; n++) {
	                		  this.Temp_kbct.AddRule(rr[n]);
	                	  }
	                	  warning=true;
	                  }
	              } else if (option.equals(LocaleKBCT.GetString("CompleteRuleBase"))) {
	            	  //System.out.println("CALL to CompleteRuleBase");
	                  warning= this.CompleteRuleBase(mode, automatic, null);
	              }
	           }
	        } else if (mode==1) {
	           if (!automatic)
	               MessageKBCT.Information(this, LocaleKBCT.GetString("Processing")+" ..."+"\n"+
	                                             "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
	                                             "... "+LocaleKBCT.GetString("PleaseWait")+" ...");

	           Rule[] rr= this.RuleBaseCompletenessAnalysis(mode, automatic);
	           if (rr!=null) {
	         	  warning=true;
	              if (!this.Parent.KBDataFile.equals("")) {
	                 boolean res= this.CompleteRuleBase(mode, automatic, rr);
	                 if (!res) {
	                	//System.out.println("NbRules="+this.Temp_kbct.GetNbRules());
	      	            //System.out.println("rr.length= "+rr.length);
	                	int r= this.Temp_kbct.GetNbRules()-rr.length;
	      	            //System.out.println("r= "+r);
	               	    for (int n=0; n<rr.length; n++) {
	            		     this.Temp_kbct.RemoveRule(r);
	            	    }
	                 }
	              }
	           }
	        }
	        MessageKBCT.WriteLogFile("----------------------------------", "Completeness");
	        MessageKBCT.WriteLogFile(LocaleKBCT.GetString("AutomaticCompletenessEnded"), "Completeness");
	        Date d= new Date(System.currentTimeMillis());
	        MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "Completeness");
	        MessageKBCT.CloseLogFile("Completeness");
	        if (!automatic) {
	            MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticCompletenessEnded"));
	        	if ( (!MainKBCT.getConfig().GetTESTautomatic()) && (warning) ) {
	              try {
	            	  this.ReInitTableRules();
	              	  this.jfcCompleteness= new JFISConsole(this, CompletenessLogFile+".txt", false);
	              } catch (Throwable t) {
	                  t.printStackTrace();
	            	  MessageKBCT.Error(null, t);
	              }
	            }
	        }
	    } else {
	        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("SelectALabelForEachOutput"));
	    }
        long[] exc= new long[8];
        exc[0]= this.Temp_kbct.GetPtr();
        exc[1]= this.Temp_kbct.GetCopyPtr();
        if (this.Parent.kbct_Data!=null) {
        	exc[2]= this.Parent.kbct_Data.GetPtr();
        	exc[3]= this.Parent.kbct_Data.GetCopyPtr();
        } else {
        	exc[2]= -1;
        	exc[3]= -1;
        }
        if (this.Parent.kbct!=null) {
        	exc[4]= this.Parent.kbct.GetPtr();
        	exc[5]= this.Parent.kbct.GetCopyPtr();
        } else {
        	exc[4]= -1;
        	exc[5]= -1;
        }
        if (this.kbct!=null) {
        	exc[6]= this.kbct.GetPtr();
        	exc[7]= this.kbct.GetCopyPtr();
        } else {
        	exc[6]= -1;
        	exc[7]= -1;
        }
	    jnikbct.cleanHashtable(ptrid,exc);
	    //  System.out.println("JEF: Completeness -> htsize="+jnikbct.getHashtableSize());
	    //  System.out.println("JEF: Completeness -> id="+jnikbct.getId());
  }
//------------------------------------------------------------------------------
  private Rule[] RuleBaseCompletenessAnalysis(int mode, boolean automatic) {
      Vector<Rule> newRules= new Vector<Rule>();
      boolean warning= false;
	  String message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
                        +"   "+LocaleKBCT.GetString("CompletenessThres")+"= "+MainKBCT.getConfig().GetCompletenessThres()+"\n"+"\n"
                        +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
                        +LocaleKBCT.GetString("DoYouWantToChangeSelectedValues");

        int option=1;
        if ( (!automatic) && (mode!=1) ) {
            option= MessageKBCT.Confirm(this, message, 1, false, false, false);

            if (option==0)
                this.setCompletenessThreshold();
        }
        MessageKBCT.WriteLogFile((LocaleKBCT.GetString("Options")).toUpperCase(), "Completeness");
        MessageKBCT.WriteLogFile("  "+LocaleKBCT.GetString("CompletenessThres")+"= "+MainKBCT.getConfig().GetCompletenessThres(), "Completeness");
        MessageKBCT.WriteLogFile("----------------------------------", "Completeness");
     	MessageKBCT.WriteLogFile(LocaleKBCT.GetString("RuleBaseCompletenessAnalysis").toUpperCase(), "Completeness");
        File temp1= JKBCTFrame.BuildFile("temprbqCompleteness.fis");
        String fis_name= temp1.getAbsolutePath();
        try {
          this.Temp_kbct.Save();
          this.Temp_kbct.SaveFISquality(fis_name);
          JFIS fisComp= new JFIS(fis_name);
          int NbOut= this.Temp_kbct.GetNbOutputs();
          String outputName= this.Temp_kbct.GetOutput(1).GetName();
          int NbRules= this.Temp_kbct.GetNbRules();
          int NbIn= this.Temp_kbct.GetNbInputs();
          int[] NOL= new int[NbIn];
          String[] VarNames= new String[NbIn]; 
          Vector<Double> mp= new Vector<Double>();
          Vector<String> mpNames= new Vector<String>();
          double[] values= new double[NbIn];
          int[] cont= new int[NbIn];
          int[] PartialCont=new int[NbIn];
          for (int n=0; n<NbIn; n++) {
        	  values[n]=0;
        	  cont[n]=0;
        	  PartialCont[n]=0;
          	  JKBCTInput in= this.Temp_kbct.GetInput(n+1);
              String[] names= in.GetLabelsName();
              for (int k=0; k<names.length; k++) {
              	  mpNames.add(names[k]);
              }
        	  int NbLab= in.GetLabelsNumber();
        	  NOL[n]= NbLab;
        	  VarNames[n]= in.GetName();
           	  for (int k=0; k<NbLab; k++) {
                   LabelKBCT lab= in.GetLabel(k+1);
                   double[] par= lab.GetParams();
                   if (par.length==4) {
                	   double v= (par[0]+par[1])/2;
                	   mp.add(new Double(v));
                   } else if (par.length==3)
                	   mp.add(new Double(par[1]));
                   else if (par.length==1)
                	   mp.add(new Double(par[0]));
           	  }       	  
          }
          Object[] vv= mp.toArray();
          Object[] vvNames= mpNames.toArray();
          int[] cumulatedNOL= new int[NbIn];
          cumulatedNOL[0]=0;
          for (int n=1; n<NbIn; n++) {
        	  cumulatedNOL[n]=cumulatedNOL[n-1]+NOL[n-1]; 
          }          
          int[] contStep= new int[NbIn];
    	  contStep[0]=1; 
          for (int n=1; n<NbIn; n++) {
        	  contStep[n]=contStep[n-1]*NOL[n-1]; 
          }          
          int NbAreas=1;
          for (int n=0; n<NbIn; n++) {
        	  NbAreas=NbAreas*NOL[n];
          }
          int contCompleteness=0;
      	  boolean cflag= false;
      	  if (this.Temp_kbct.GetOutput(1).GetClassif().equals("yes"))
      		  cflag= true;
      	  
          for (int n=0; n<NbAreas; n++) {
              String[] LabNames= new String[NbIn];
              int[] in_labels= new int[NbIn];
        	  for (int k=0; k<NbIn; k++) {
          		PartialCont[k]++;
                int index=cumulatedNOL[k]+cont[k];
        		values[k]= ((Double)vv[index]).doubleValue();
        		LabNames[k]= (String)vvNames[index];
            	in_labels[k]= cont[k]+1;
        		if (PartialCont[k]==contStep[k]) {
        		  PartialCont[k]=0;
        		  cont[k]++;
        	      if (cont[k]==NOL[k])
        	    	  cont[k]=0;
        		}
        	}
        	fisComp.Infer(values);
            double[] outputs_values= fisComp.SortiesObtenues();
            double[][] res= fisComp.AgregationResult(0);
            //System.out.println("res.length -> "+res.length);
            //System.out.println("res[0].length -> "+res[0].length);
 	        if ( ( cflag && ( (outputs_values[0]==-1) || ( (outputs_values[0]!=-1) && res[1][(int)outputs_values[0]-1] < MainKBCT.getConfig().GetCompletenessThres()) ) ) || 
	        	 ( !cflag && (outputs_values[0]==-1) ) ) {
 	        		 contCompleteness++;
 	        		 String ruleMsg= "    "+contCompleteness+".- "+LocaleKBCT.GetString("If")+" ";
 	 	        	 for (int m=0; m<NbIn-1; m++) {
 	 	    	         ruleMsg= ruleMsg + VarNames[m] + " " + LocaleKBCT.GetString("IS") + " " + LabNames[m] + " " + LocaleKBCT.GetString("AND")+ " ";
 	 	        	 }
 	    	         ruleMsg= ruleMsg + VarNames[NbIn-1] + " " + LocaleKBCT.GetString("IS") + " " + LabNames[NbIn-1] + " " + LocaleKBCT.GetString("THEN")+ " " + outputName + " " + LocaleKBCT.GetString("IS") + " ?";
 	 	        	 MessageKBCT.WriteLogFile(ruleMsg, "Completeness");
                     int rule_number=NbRules+contCompleteness;
                     int[] out_labels= new int[NbOut];
                     for (int m=0; m<NbOut; m++) {
                    	 out_labels[m]=0;
                     }
 	 	        	 Rule r= new Rule(rule_number,NbIn,NbOut,in_labels,out_labels,"I",true);
 	 	        	 newRules.add(r);
  	        	     warning= true;
 	        	}	
          }
          fisComp.Close();
          fisComp.Delete();
        } catch (Throwable t) {
            t.printStackTrace();
            MessageKBCT.Error(null, t);
        }
        Rule[] result= null;
        if (warning) {
        	Object[] vvRules= newRules.toArray();
        	result= new Rule[vvRules.length];
        	for (int n=0; n<result.length; n++) {
        		Rule r= (Rule)vvRules[n];
 	        	int[] vv_in_labels= r.Get_in_labels_number();
 	        	int[] in_labels= new int[NbIn];
                for (int m=0; m<NbIn; m++) {
                  	 in_labels[m]= vv_in_labels[m];
                }
 	            int[] out_labels= new int[NbOut];
                for (int m=0; m<NbOut; m++) {
               	 out_labels[m]=0;
                }
                result[n]= new Rule(r.GetNumber(),NbIn,NbOut,in_labels,out_labels,"I",true);
        	}        	
        }
        return result;
  }
//------------------------------------------------------------------------------
  private boolean CompleteRuleBase(int mode, boolean automatic, Rule[] rr) {
	  MessageKBCT.WriteLogFile(LocaleKBCT.GetString("CompleteRuleBase").toUpperCase(), "Completeness");
  	  //System.out.println("JEF: CompleteRuleBase: NbRules="+this.Temp_kbct.GetNbRules());
      if (rr!=null) {
    	  for (int n=0; n<rr.length; n++) {
    		  this.Temp_kbct.AddRule(rr[n]);
    	  }
      }
  	  //System.out.println("JEF: CompleteRuleBase: NbRules="+this.Temp_kbct.GetNbRules());
      // Solving conflicts
      boolean warningSC= false;
      boolean endSC= false;
      //int iter= 1;
      //int cont=0;
      while (!endSC) {
    	   //System.out.println("cont -> "+cont);
	       long TM=Runtime.getRuntime().totalMemory();
	       long MM=Runtime.getRuntime().maxMemory();
	       if (TM>=MM) {
             if (!MainKBCT.getConfig().GetTESTautomatic()) {
	    	     String message= LocaleKBCT.GetString("WarningConsistencySolvingConflictsHalted")+"\n"+
                                 LocaleKBCT.GetString("WarningJVMOutOfMemory")+"\n"+
                                 LocaleKBCT.GetString("WarningReleaseMemory")+"\n"+
                                 LocaleKBCT.GetString("WarningConsistencySolvingConflictsOutOfMemory");
	             if (!MainKBCT.flagHalt) {
	    	         MessageKBCT.Information(null, message);
	             }
             }
             if (!MainKBCT.flagHalt) {
                 MessageKBCT.WriteLogFile(LocaleKBCT.GetString("WarningConsistencySolvingConflictsHalted"), "Completeness");
                 MainKBCT.flagHalt= true;
             }
             break;
	       } else {
             //iter++;
             boolean[] SC= this.SolveCompletenessConflictsRuleBase(this.getJExtDataFile());
             //cont++;
             endSC= SC[0];
             if (SC[1])
               warningSC= true;

             //if ( (!MainKBCT.getConfig().GetTESTautomatic()) && (iter > 100) )
          	  //endSC=true;
        }
      }
      if (!warningSC) {
          MessageKBCT.WriteLogFile(LocaleKBCT.GetString("NoConflictsSolution"), "Completeness");
      	if (!MainKBCT.getConfig().GetTESTautomatic())
      		MessageKBCT.Information(this, LocaleKBCT.GetString("NoConflictsSolution"));
      }
      return warningSC;
  }
//------------------------------------------------------------------------------
  public int CompleteRule(int r) {
     int result=0;
	 int NbRules= this.Temp_kbct.GetNbRules();
	 int NbIn= this.Temp_kbct.GetNbInputs();
     int NbOut= this.Temp_kbct.GetNbOutputs();
	 int NbOutLabels= this.Temp_kbct.GetOutput(1).GetLabelsNumber();
	 Vector<double[][]> quality= new Vector<double[][]>(); 
     try {
       File temp= JKBCTFrame.BuildFile("tempCompleteRule.kb.xml");
       JKBCT kbctaux= new JKBCT(this.Temp_kbct);
       //long ptr= kbctaux.GetPtr();
       kbctaux.SetKBCTFile(temp.getAbsolutePath());
       kbctaux.Save();
       //JConsistency jc= new JConsistency(this, kbctaux);
       JConsistency jc= new JConsistency(kbctaux);
       double[][] qinitial= jc.KBquality(kbctaux, null, 0, 0, this.getJExtDataFile());
       quality.add(qinitial);
       Rule raux= kbctaux.GetRule(r);
	   kbctaux.RemoveRule(r-1);
	   kbctaux.Save();
   	   int[] in_labels= raux.Get_in_labels_number();
       for (int n=0; n<NbOutLabels; n++) {
	        int[] new_in_labels= new int[NbIn];
            for (int m=0; m<NbIn; m++) {
              	 new_in_labels[m]= in_labels[m];
            }
	        int[] out_labels= new int[NbOut];
            for (int m=0; m<NbOut; m++) {
           	 out_labels[m]=n+1;
            }
            Rule rnew= new Rule(NbRules,NbIn,NbOut,in_labels,out_labels,"I",true);
    	    kbctaux.AddRule(rnew);
    	    kbctaux.Save();
    	    double[][] q= jc.KBquality(kbctaux, null, 0, 0, this.getJExtDataFile());
    	    quality.add(q);
    	    kbctaux.RemoveRule(NbRules-1);
    	    kbctaux.Save();
       }
       // Comprobar cual es la mejor opcion
       Object[] vvq= quality.toArray();
       int theBest=0;
       double[][] qold= qinitial;
       for (int n=1; n<vvq.length; n++) {
            double[][] qnew= (double[][])vvq[n];
            if (!jc.GetWorseKBquality(qold, qnew)) {
            	theBest=n;
            	qold= qnew;
            }
       }
       result= theBest;
       if (theBest > 0) {
         int[] new_in_labels= new int[NbIn];
         for (int m=0; m<NbIn; m++) {
         	 new_in_labels[m]= in_labels[m];
         }
         int[] out_labels= new int[NbOut];
         for (int m=0; m<NbOut; m++) {
      	   out_labels[m]=theBest;
         }
         Rule rnew= new Rule(NbRules,NbIn,NbOut,in_labels,out_labels,"I",true);
         this.Temp_kbct.RemoveRule(raux.GetNumber()-1);
         this.Temp_kbct.AddRule(rnew);
       }
       this.ReInitTableRules();
       kbctaux.Close();
       kbctaux.Delete();
       //jnikbct.DeleteKBCT(ptr+1);
     } catch (Throwable t) {
    	 t.printStackTrace();
    	 MessageKBCT.Error(null,t);
     }
     return result;
  }
//------------------------------------------------------------------------------
  //aquiiiiiiiiiii este es el boton calidad
  public void jButtonQuality_actionPerformed(boolean ff, boolean vis) {
	  //System.out.println("JEF: Quality -> htsize="+jnikbct.getHashtableSize());
	  //System.out.println("JEF: Quality -> id="+jnikbct.getId());
      if (MainKBCT.getConfig().GetTESTautomatic()) {
    	  File fname= new File(JKBCTFrame.KBExpertFile);
    	  String name=fname.getName();
          int ind= name.indexOf("_");
    	  String LogName="LogQuality_"+name.substring(ind+1,name.length()-7);
    	  //String LogName="LogQuality_"+name;
    	  String QualityLogFile= (JKBCTFrame.BuildFile(LogName)).getAbsolutePath();
    	  MessageKBCT.BuildLogFile(QualityLogFile+".txt", this.Parent.IKBFile, JKBCTFrame.KBExpertFile, this.Parent.KBDataFile, "Quality");
          MessageKBCT.WriteLogFile((LocaleKBCT.GetString("AutomaticQualityEvaluationProcedure")).toUpperCase(), "Quality");
          MessageKBCT.WriteLogFile("----------------------------------", "Quality");
          double K=0.4;
          double AccLRN=0;
          double AccTST=0;
          if ( (this.getJExtDataFile()!=null) && (!this.flagOnlyInterpretability) ) {
              double[] Acc= this.AssessingAccuracy(false, true);
              AccLRN= Acc[0];
              AccTST= Acc[1];
          }
          double Int= this.AssessingInterpretability(false);
          double QLRN= K*AccLRN + (1-K)*Int;
          double QTST= K*AccTST + (1-K)*Int;
          MessageKBCT.WriteQualityLogFileCV(QLRN, K, AccLRN, Int, true);
          MessageKBCT.WriteQualityLogFileCV(QTST, K, AccTST, Int, false);
          MessageKBCT.WriteLogFile((LocaleKBCT.GetString("AutomaticQualityEnded")).toUpperCase(), "Quality");
          Date d= new Date(System.currentTimeMillis());
          MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "Quality");
          MessageKBCT.CloseLogFile("Quality");
      } else {
          boolean warnCheck= false;
          JKBCTOutput jout= this.Temp_kbct.GetOutput(1);
          if ( (jout.GetScaleName().equals("user")) && 
               ( (jout.GetInputInterestRange()[0]!=1) ||
           	   (jout.GetInputInterestRange()[1]!=jout.GetLabelsNumber()) ) &&
           	   (jout.isOutput()) &&
           	   ( (jout.GetType().equals("logical")) || (jout.GetType().equals("categorical")) ) ) {
                   warnCheck= true;
          } 
          boolean warning= false;
          if (!warnCheck) {
            for (int n=0; n<this.Temp_kbct.GetNbRules(); n++) {
              if (!warning) {
                Rule r= this.Temp_kbct.GetRule(n+1);
                int[] out= r.Get_out_labels_number();
                for (int m=0; m<out.length; m++) {
                  if (out[m]!=0)
                    warning= true;
                }
                warning= !warning;
              }
            }
          }
          if (!warning) {
        	  if (MainKBCT.getConfig().GetTutorialFlag()) {
           		  this.AssessingAccuracy(!ff,vis);
           		  this.AssessingInterpretability(!ff);
        		  
        	  } else {
        	      int op= 1;
                  if (!this.Parent.KBDataFile.equals(""))
                      op= MessageKBCT.QualityOptions(this);

                  if (op==0) {
                      this.AssessingAccuracy(false,vis);
                  } else if (op==1) {
           	          String QualityLogFile= (JKBCTFrame.BuildFile("LogQuality")).getAbsolutePath();
            	      MessageKBCT.BuildLogFile(QualityLogFile+".txt", this.Parent.IKBFile, JKBCTFrame.KBExpertFile, this.Parent.KBDataFile, "Quality");
                      this.AssessingInterpretability(false);
                      MessageKBCT.CloseLogFile("Quality");
                  }
        	  }
          } else {
              MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("SelectALabelForEachOutput"));
          }
      }
	  //System.out.println("JEF: Quality -> htsize="+jnikbct.getHashtableSize());
	  //System.out.println("JEF: Quality -> id="+jnikbct.getId());
  }
//------------------------------------------------------------------------------
  protected double[] AssessingAccuracy(boolean ff, boolean vistable) {
	double[] resAcc={0,0};
	try {
      // ACCURACY
      File temp1= JKBCTFrame.BuildFile("temprbq.fis");
      String fis_name= temp1.getAbsolutePath();
      this.Temp_kbct.Save();
      long ptrid= jnikbct.getId();
//      System.out.println("JExpertFrame: AssessingAccuracy: ptrid="+ptrid);
      this.Temp_kbct.SaveFISquality(fis_name);
      long[] exc= new long[8];
      exc[0]= this.Temp_kbct.GetPtr();
      exc[1]= this.Temp_kbct.GetCopyPtr();
      if (this.Parent.kbct_Data!=null) {
      	exc[2]= this.Parent.kbct_Data.GetPtr();
      	exc[3]= this.Parent.kbct_Data.GetCopyPtr();
      } else {
      	exc[2]= -1;
      	exc[3]= -1;
      }
      if (this.Parent.kbct!=null) {
      	exc[4]= this.Parent.kbct.GetPtr();
      	exc[5]= this.Parent.kbct.GetCopyPtr();
      } else {
      	exc[4]= -1;
      	exc[5]= -1;
      }
      if (this.kbct!=null) {
      	exc[6]= this.kbct.GetPtr();
      	exc[7]= this.kbct.GetCopyPtr();
      } else {
      	exc[6]= -1;
      	exc[7]= -1;
      }
      jnikbct.cleanHashtable(ptrid,exc);
//      System.out.println("JExpertFrame: AssessingAccuracy: new fis");
      JFIS fis_file= new JFIS(fis_name);
      File result= JKBCTFrame.BuildFile("result");
      String ResultFile=result.getAbsolutePath();
      File f;
      if (this.Parent.DataFileNoSaved != null)
        f = new File(this.Parent.DataFileNoSaved.ActiveFileName());
      else
        f = new File(this.Parent.DataFile.ActiveFileName());

      String TestFile=f.getAbsolutePath();
      String message= LocaleKBCT.GetString("Default_values")+":"+"\n"
                   +"   "+LocaleKBCT.GetString("ResultFile")+"= "+ResultFile+"\n"
                   +"   "+LocaleKBCT.GetString("TestFile")+"= "+TestFile+"\n"
                   +"   "+LocaleKBCT.GetString("BlankThres")+"= "+MainKBCT.getConfig().GetBlankThres()+"\n"
                   +"   "+LocaleKBCT.GetString("AmbThres")+"= "+MainKBCT.getConfig().GetAmbThres()+"\n"+"\n"
                   +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
                   +LocaleKBCT.GetString("DoYouWantToMakeChanges");

      int option=1;
      if ( (!MainKBCT.getConfig().GetTESTautomatic()) && (!MainKBCT.getConfig().GetTutorialFlag()) )
          option= MessageKBCT.Confirm(this, message, 1, false, false, false);

      if (this.jrbqf!=null)
        this.jrbqf.dispose();

	  //System.out.println("AssessingAccuracy");
      if (option==0) {
    	  //System.out.println("AssessingAccuracy 1.0");
          if (this.Parent.DataFileNoSaved != null)
        	  this.jrbqf= new JRulesBaseQualityFrame(this, fis_file, this.Parent.DataFileNoSaved, new JSemaphore(), false, ff, vistable);
          else
        	  this.jrbqf= new JRulesBaseQualityFrame(this, fis_file, this.Parent.DataFile, new JSemaphore(), false, ff, vistable);
      } else {
          if (this.Parent.DataFileNoSaved != null) {
        	  //System.out.println("AssessingAccuracy 1.1");
        	  this.jrbqf= new JRulesBaseQualityFrame(this, fis_file, this.Parent.DataFileNoSaved, new JSemaphore(), true, ff, vistable);
        	  if ( (MainKBCT.getConfig().GetTutorialFlag()) && ( (!ff) || (vistable) ) ) {
            	  //System.out.println("AssessingAccuracy 1.1.1");
        		  MainKBCT.getConfig().SetTutorialFlag(false);
        	      if (this.jrbqfTut!=null)
        	          this.jrbqfTut.dispose();
        	      
        		  this.jrbqfTut= new JRulesBaseQualityFrame(this, fis_file, this.Parent.DataFileNoSaved, new JSemaphore(), true, ff, vistable);
        		  MainKBCT.getConfig().SetTutorialFlag(true);
        	  }
          } else {
        	  //System.out.println("AssessingAccuracy 1.2");
        	  this.jrbqf= new JRulesBaseQualityFrame(this, fis_file, this.Parent.DataFile, new JSemaphore(), true, ff, vistable);
        	  if ( (MainKBCT.getConfig().GetTutorialFlag()) && ( (!ff) || (vistable) ) ) {
            	  //System.out.println("AssessingAccuracy 1.2.1");
        		  MainKBCT.getConfig().SetTutorialFlag(false);
        	      if (this.jrbqfTut!=null)
        	          this.jrbqfTut.dispose();

        	      this.jrbqfTut= new JRulesBaseQualityFrame(this, fis_file, this.Parent.DataFile, new JSemaphore(), true, ff, vistable);
        		  MainKBCT.getConfig().SetTutorialFlag(true);
        	  }
          }
      }
      resAcc[0]= this.jrbqf.getAccIndexLRN();
      resAcc[1]= this.jrbqf.getAccIndexTST();
      //System.out.println("AccIndexLRN -> "+resAcc[0]);
      //System.out.println("AccIndexTST -> "+resAcc[1]);
    } catch (Throwable t) {
        //t.printStackTrace();
        MessageKBCT.Error(null, t);
    }
    return resAcc;
  }
//------------------------------------------------------------------------------
  //aquiiiiiiiiiii este es el boton interpretabilidad
  public double AssessingInterpretability(boolean fingFlag) {
      // INTERPRETABILITY
      if (this.jkbif!=null) {
          JKBCTFrame.RemoveTranslatable(this.jkbif);
          this.jkbif.dispose();
      }
      this.jkbif= new JKBInterpretabilityFrame(this, this.Temp_kbct, fingFlag);
      //double ind= this.jkbif.getIntIndex();
      return this.jkbif.getIntIndex(); //indiceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
  }
//------------------------------------------------------------------------------
  public void jButtonInfer_actionPerformed() { 
	  //System.out.println("JEF: Infer -> htsize="+jnikbct.getHashtableSize());
	  //System.out.println("JEF: Infer -> id="+jnikbct.getId());
	  this.jButtonInfer(); 
	  //System.out.println("JEF: Infer -> htsize="+jnikbct.getHashtableSize());
	  //System.out.println("JEF: Infer -> id="+jnikbct.getId());
  }
//------------------------------------------------------------------------------
  void jButtonInfer() {
    //System.out.println("JExpertFrame: crear ventana INFER: INI");
    boolean warnCheck= false;
    JKBCTOutput jout= this.Temp_kbct.GetOutput(1);
    if ( (jout.GetScaleName().equals("user")) && 
         ( (jout.GetInputInterestRange()[0]!=1) ||
         (jout.GetInputInterestRange()[1]!=jout.GetLabelsNumber()) ) &&
         (jout.isOutput()) &&
         ( (jout.GetType().equals("logical")) || (jout.GetType().equals("categorical")) ) ) {
            warnCheck= true;
    } 
    //System.out.println("JExpertFrame: crear ventana INFER: 1");
    boolean warning= false;
    if (!warnCheck) {
      for (int n=0; n<this.Temp_kbct.GetNbRules(); n++) {
        if (!warning) {
          Rule r= this.Temp_kbct.GetRule(n+1);
          int[] out= r.Get_out_labels_number();
          for (int m=0; m<out.length; m++) {
            if (out[m]!=0)
              warning= true;
          }
          warning= !warning;
        }
      }
    }
    //System.out.println("JExpertFrame: crear ventana INFER: 2");
    if (!warning) {
      try {
        File temp1= JKBCTFrame.BuildFile("tempinfer.kb.xml");
        File temp2= JKBCTFrame.BuildFile("tempinfer.fis");
        //System.out.println("JExpertFrame: crear ventana INFER: 3");
        JKBCT kbct_fis= new JKBCT(this.Temp_kbct);
        //System.out.println("JExpertFrame: crear ventana INFER: 4");
        kbct_fis.Save(temp1.getPath(), false);
        //System.out.println("JExpertFrame: crear ventana INFER: 5");
        JKBCT kbctRulesInfer= kbct_fis.SaveFIS(temp2.getPath());
        //System.out.println("JExpertFrame: crear ventana INFER: 6");
        JFIS jf = new JFIS(temp2.getPath());
        int NbRules= kbct_fis.GetNbRules();
        for (int n= NbRules; n >= 1; n--)
          kbct_fis.RemoveRule(n-1);

        //System.out.println("JExpertFrame: crear ventana INFER: 7");
        this.TranslateRules_FIS2KBCT(jf, kbct_fis);
        if (this.jif!=null)
            jif.dispose();

        //System.out.println("JExpertFrame: crear ventana INFER: 8");
        if (this.Parent.DataFileNoSaved!=null) {
          //System.out.println("JExpertFrame: crear ventana INFER: 8a");
          jif= new JInferenceFrame(this, jf, new JSemaphore(), this.Parent.DataFileNoSaved, kbctRulesInfer);
        } else {
          //System.out.println("JExpertFrame: crear ventana INFER: 8b");
          jif= new JInferenceFrame(this, jf, new JSemaphore(), this.Parent.DataFile, kbctRulesInfer);
        }
        //System.out.println("JExpertFrame: crear ventana INFER: 9");
        if (!MainKBCT.getConfig().GetTESTautomatic()) {
            //System.out.println("JExpertFrame: crear ventana INFER: 9a");
            jif.setVisible(true);
        } else {
            //System.out.println("JExpertFrame: crear ventana INFER: 9b");
            jif.setVisible(false);
            if (MainKBCT.getConfig().GetINFERautomatic())
                jif.jMenuAllData_actionPerformed();
        }
        //System.out.println("JExpertFrame: crear ventana INFER: 10");
        if ( (!MainKBCT.getConfig().GetTESTautomatic()) && (!MainKBCT.getConfig().GetTutorialFlag()) ) {
          if (this.jrfi!=null) {
              jrfi.dispose();
          }
          jrfi= new JRuleFrameInfer(this, kbctRulesInfer, new JSemaphore(), jif.getWidth(), jif.getHeight());
        }
        if ( (!MainKBCT.getConfig().GetTESTautomatic()) && (!MainKBCT.getConfig().GetTutorialFlag()) )
            jrfi.setVisible(true);
        
        //System.out.println("JExpertFrame: crear ventana INFER: 11");
        temp1.deleteOnExit();
        temp2.deleteOnExit();
        kbct_fis.Close();
        kbct_fis.Delete();
      } catch (Throwable t) {
          t.printStackTrace();
          MessageKBCT.Error(null, t);
      }
    } else {
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("SelectALabelForEachOutput"));
    }
    //System.out.println("JExpertFrame: crear ventana INFER: END");
  }
//------------------------------------------------------------------------------
  void jButtonSummary_actionPerformed() { 
	  //System.out.println("JEF: Summary -> htsize="+jnikbct.getHashtableSize());
	  //System.out.println("JEF: Summary -> id="+jnikbct.getId());
      long ptr= jnikbct.getId();
	  this.jButtonSummary(); 
      long[] exc= new long[8];
      exc[0]= this.Temp_kbct.GetPtr();
      exc[1]= this.Temp_kbct.GetCopyPtr();
      if (this.Parent.kbct_Data!=null) {
      	exc[2]= this.Parent.kbct_Data.GetPtr();
      	exc[3]= this.Parent.kbct_Data.GetCopyPtr();
      } else {
      	exc[2]= -1;
      	exc[3]= -1;
      }
      if (this.Parent.kbct!=null) {
      	exc[4]= this.Parent.kbct.GetPtr();
      	exc[5]= this.Parent.kbct.GetCopyPtr();
      } else {
      	exc[4]= -1;
      	exc[5]= -1;
      }
      if (this.kbct!=null) {
      	exc[6]= this.kbct.GetPtr();
      	exc[7]= this.kbct.GetCopyPtr();
      } else {
      	exc[6]= -1;
      	exc[7]= -1;
      }
      jnikbct.cleanHashtable(ptr,exc);
	  //System.out.println("JEF: Summary -> htsize="+jnikbct.getHashtableSize());
	  //System.out.println("JEF: Summary -> id="+jnikbct.getId());
  }
//------------------------------------------------------------------------------
  void jButtonSummary() {
	this.Temp_kbct.GetInputsUsedInRuleBase();
	
    boolean warning= false;
    for (int n=0; n<this.Temp_kbct.GetNbRules(); n++) {
      if (!warning) {
        Rule r= this.Temp_kbct.GetRule(n+1);
        int[] out= r.Get_out_labels_number();
        for (int m=0; m<out.length; m++) {
          if (out[m]!=0)
            warning= true;
        }
        warning= !warning;
      }
    }
    if (!warning) {
      try {
        File temp= JKBCTFrame.BuildFile("tempSummary.html");
        JKBCT kbct_fis= new JKBCT(this.Temp_kbct);
        kbct_fis.SaveLinguisticSummary(temp.getAbsolutePath());
    	this.jfcLinguisticSummary= new JFISConsole(this, temp.getAbsolutePath(), true);
        temp.deleteOnExit();
      } catch (Throwable t) {
          t.printStackTrace();
          MessageKBCT.Error(null, t);
      }
    } else {
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("SelectALabelForEachOutput"));
    }
  }
//------------------------------------------------------------------------------
  private void UpdateRules ( int var_number, String type, int mf_removed ) {
    int NbRules= this.Temp_kbct.GetNbRules();
    for (int n=0; n<NbRules; n++) {
      Rule r= this.Temp_kbct.GetRule(n+1);
      int Number= 0;
      int NOL= 0;
      if (type.equals("INPUT")) {
        Number= r.Get_in_labels_number()[var_number-1];
        NOL= this.Temp_kbct.GetInput(var_number).GetLabelsNumber();
      } else if (type.equals("OUTPUT")) {
        Number= r.Get_out_labels_number()[var_number-1];
        NOL= this.Temp_kbct.GetOutput(var_number).GetLabelsNumber();
      }
      int NewNumber= 0;
      if (Number <= NOL+1) {
          // Etiqueta basica
          if (Number < mf_removed)
            NewNumber= Number;
          else if (Number > mf_removed)
            NewNumber= Number - 1;
          else
            NewNumber= 0;
      } else if ( (Number > NOL+1) && (Number <= 2*(NOL+1)) ){
          // Etiqueta NOT
          if ( (Number - NOL -1) < mf_removed)
            NewNumber= Number - 1;
          else if ((Number - NOL -1) > mf_removed)
            NewNumber= Number - 2;
          else
            NewNumber= 0;
      } else {
          // Etiqueta OR
          int SelLabel= Number-2*(NOL+1);
          int NbLabelsOR= jnikbct.NbORLabels(SelLabel, NOL+1);
          int option= jnikbct.option(SelLabel, NbLabelsOR, NOL+1);
          if (option==mf_removed) {
              if (NbLabelsOR==2)
                NewNumber= option;
              else
                NewNumber= jnikbct.NumberORLabel(option, NbLabelsOR-1, NOL);
          } else if (option < mf_removed) {
              if ( (NbLabelsOR==2) && (option==mf_removed-1) )
                NewNumber= option;
              else if (option+NbLabelsOR-1 < mf_removed) {
                if ( (NbLabelsOR==NOL-1) && (option+NbLabelsOR-1 > NOL-2) ) {
                    if (option+NbLabelsOR-1 < NOL)
                      NewNumber= 2*NOL;
                    else
                      NewNumber= NOL + 1;
                } else
                    NewNumber= jnikbct.NumberORLabel(option, NbLabelsOR, NOL);
              } else
                NewNumber= jnikbct.NumberORLabel(option, NbLabelsOR-1, NOL);
          } else if (option > mf_removed) {
              if (NbLabelsOR > NOL-2) {
                  if ( (mf_removed==1) && (option==2) )
                    NewNumber= 2*NOL;
                  else
                    NewNumber= NOL+1;
              } else
                  NewNumber= jnikbct.NumberORLabel(option-1, NbLabelsOR, NOL);
          }
      }
      if ( (NOL==2) && (NewNumber > 2) ) {
         if (NewNumber==3)
        	 NewNumber=2;
         else
        	 NewNumber=1;
      }
      if (type.equals("INPUT"))
        r.SetInputLabel(var_number, NewNumber);
      else if (type.equals("OUTPUT"))
        r.SetOutputLabel(var_number, NewNumber);

      this.Temp_kbct.ReplaceRule(n+1,r);
    }
  }
//------------------------------------------------------------------------------
  private void UpdateRuleBase ( int ini, int length, String Update, String type ) {
    int NbRules= this.Temp_kbct.GetNbRules();
    for (int n=0; n<NbRules;n++) {
        Rule r= this.Temp_kbct.GetRule(n+1);
        Rule r_new= null;
        if (type.equals("INPUT")) {
          int[] in_labels_number= r.Get_in_labels_number();
          int r_N_inputs= r.GetNbInputs();
          int[] in_labels_new= new int[r_N_inputs];
          if (Update.equals("UP")) {
              for (int m=0;m<r_N_inputs;m++) {
                if ( (m >= ini-1) && (m < ini+length-1) )
                  in_labels_new[m]= in_labels_number[m+1];
                else if (m == ini+length-1)
                  in_labels_new[m]= in_labels_number[ini-1];
                else
                  in_labels_new[m]= in_labels_number[m];
              }
          } else if (Update.equals("DOWN")) {
              for (int m=0;m<r_N_inputs;m++) {
                if ( (m > ini) && (m <= ini+length) )
                  in_labels_new[m]= in_labels_number[m-1];
                else if (m == ini)
                  in_labels_new[m]= in_labels_number[ini+length];
                else
                  in_labels_new[m]= in_labels_number[m];
              }
           }
           r_new= new Rule(n+1, r_N_inputs, r.GetNbOutputs(), in_labels_new, r.Get_out_labels_number(), r.GetType(), r.GetActive());
        } else if (type.equals("OUTPUT")) {
            int[] out_labels_number= r.Get_out_labels_number();
            int r_N_outputs= r.GetNbOutputs();
            int[] out_labels_new= new int[r_N_outputs];
            if (Update.equals("UP")) {
              for (int m=0;m<r_N_outputs;m++) {
                if ( (m >= ini-1) && (m < ini+length-1) )
                  out_labels_new[m]= out_labels_number[m+1];
                else if (m == ini+length-1)
                  out_labels_new[m]= out_labels_number[ini-1];
                else
                  out_labels_new[m]= out_labels_number[m];
              }
            } else if (Update.equals("DOWN")) {
              for (int m=0;m<r_N_outputs;m++) {
                if ( (m > ini) && (m <= ini+length) )
                  out_labels_new[m]= out_labels_number[m-1];
                else if (m == ini)
                  out_labels_new[m]= out_labels_number[ini+length];
                else
                  out_labels_new[m]= out_labels_number[m];
              }
           }
           r_new= new Rule(n+1, r.GetNbInputs(), r_N_outputs, r.Get_in_labels_number(), out_labels_new, r.GetType(), r.GetActive());
        }
        this.Temp_kbct.ReplaceRule(n+1,r_new);
     }
  }
//------------------------------------------------------------------------------
  void jPopupNewInput_actionPerformed() { this.NewInput(); }
//------------------------------------------------------------------------------
  void jPopupEditInput_actionPerformed() { this.EditInput(); }
//------------------------------------------------------------------------------
  void jPopupRemoveInput_actionPerformed() { this.RemoveInput(); }
//------------------------------------------------------------------------------
  void jPopupCopyInput_actionPerformed() { this.CopyInput(); }
//------------------------------------------------------------------------------
  void jPopupUpInput_actionPerformed() { this.UpInput(); }
//------------------------------------------------------------------------------
  void jPopupDownInput_actionPerformed() { this.DownInput(); }
//------------------------------------------------------------------------------
  void jPopupNewRule_actionPerformed() { this.NewRule(); }
//------------------------------------------------------------------------------
  void jPopupRemoveRule_actionPerformed() { this.RemoveRule(); }
//------------------------------------------------------------------------------
  void jPopupRemoveRedundantRules_actionPerformed() { this.RemoveRedundantRules(); }
//------------------------------------------------------------------------------
  void jPopupReverseOrder_actionPerformed() { this.ReverseOrder(false); }
//------------------------------------------------------------------------------
  protected void ReverseOrder(boolean automatic) {
      int[] SelectedRules= null;
      if (automatic) {
          this.RuleRankingLogFile("init");
    	  SelectedRules= this.buildSelectedRules();
      } else {      
          SelectedRules= this.jTableRules.getSelectedRows();
      }
      int lim= SelectedRules.length;
	  int[] orderedRules= new int[lim];
      Rule[] rules= new Rule[lim];
      for (int i=0; i<lim; i++) {
	       int SelRule= SelectedRules[i];
	       rules[i]= this.Temp_kbct.GetRule(SelRule+1);
	  }
	  // ordenar las reglas
      int cont=lim;
	  for (int i=0; i<lim; i++) {
           orderedRules[i]= cont;
       	   cont--;
      }
	  this.orderSelectedRules(null, SelectedRules, orderedRules, rules, automatic, 0);
  }
//------------------------------------------------------------------------------
  private void RuleRankingLogFile(String opt) {
      if (opt.equals("init")) {
          File fname= new File(JKBCTFrame.KBExpertFile);
          String name=fname.getName();
          String LogName="LogRuleRanking_"+name.substring(0,name.length()-7);
          String RankingLogFile= (JKBCTFrame.BuildFile(LogName)).getAbsolutePath();
          MessageKBCT.BuildLogFile(RankingLogFile+".txt", this.Parent.IKBFile, JKBCTFrame.KBExpertFile, this.Parent.KBDataFile, "RuleRanking");
          MessageKBCT.WriteLogFile("----------------------------------", "RuleRanking");
          MessageKBCT.WriteLogFile(LocaleKBCT.GetString("AutomaticRuleRankingProcedure"), "RuleRanking");
      } else if (opt.equals("end")) {
              MessageKBCT.WriteLogFile(LocaleKBCT.GetString("AutomaticRuleRankingEnded"), "RuleRanking");
              Date d= new Date(System.currentTimeMillis());
              MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "RuleRanking");
              MessageKBCT.CloseLogFile("RuleRanking");
      } 
  }
//------------------------------------------------------------------------------
  private int[] buildSelectedRules() {
      int[] result= null;
	  String selOut= MainKBCT.getConfig().GetOutputClassSelected();
	  //System.out.println("selOut="+selOut);
	  int out=0;
	  if (!selOut.equals(LocaleKBCT.GetString("all"))) {
	      out= (new Integer(selOut)).intValue();
	      Vector v= new Vector();
	      int NbRules= this.Temp_kbct.GetNbRules();
          for (int n=0; n<NbRules; n++) {
               Rule r= this.Temp_kbct.GetRule(n+1);
               int[] outlabs= r.Get_out_labels_number();
               if (outlabs[0]==out)
            	   v.add(new Integer(n));
          }
          Object[] objs= v.toArray();
          //System.out.println("objs.length="+objs.length);
          result= new int[objs.length];
          for (int n=0; n<objs.length; n++) {
        	   result[n]= ((Integer)objs[n]).intValue();
          }
	  } else {
		  // all 
	      int NbRules= this.Temp_kbct.GetNbRules();
          result= new int[NbRules];
          for (int n=0; n<NbRules; n++) {
    	       result[n]= n;
          }
	  }
	  return result;
  }
//------------------------------------------------------------------------------
  private void orderSelectedRules(String opt, int[] SelectedRules, int[] orderedRules, Rule[] rules, boolean automatic, int contInActive) {
      int lim=SelectedRules.length;
      if (opt != null) {
	      DecimalFormat df= new DecimalFormat();
	      df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals()+2);//5
	      df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals()+2);//5
	      DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
	      dfs.setDecimalSeparator((new String(".").charAt(0)));
	      df.setDecimalFormatSymbols(dfs);
	      df.setGroupingSize(20);
          if (opt.equals("ACC")) {
	        double total= 0;
	        double totalCC= 0;
	        double totalIC= 0;
	        for (int n=0; n<rules.length; n++) {
	           MessageKBCT.WriteLogFile(LocaleKBCT.GetString("Rule")+String.valueOf(SelectedRules[n]+1)+"  => "+df.format(this.ruleWeights[n])+"  (CC="+df.format(this.ruleWeightsCC[n])+")  (IC="+df.format(this.ruleWeightsIC[n])+")", "RuleRanking");
	           if (this.ruleWeights[n]>0) {
	             total= total + this.ruleWeights[n];
	             totalCC= totalCC + this.ruleWeightsCC[n];
	             totalIC= totalIC + this.ruleWeightsIC[n];
	           }
	        }
	        MessageKBCT.WriteLogFile(LocaleKBCT.GetString("sum").toUpperCase()+ "  => "+df.format(total)+"  (CC="+df.format(totalCC)+")  (IC="+df.format(totalIC)+")", "RuleRanking");
        } else if (opt.equals("INT")) {
            double total= 0;
            for (int n=0; n<rules.length; n++) {
            	 MessageKBCT.WriteLogFile(LocaleKBCT.GetString("Rule")+ String.valueOf(SelectedRules[n]+1)+"  => "+df.format(this.ruleIntWeights[n]), "RuleRanking");
                 if (this.ruleIntWeights[n]>0)
                     total= total + this.ruleIntWeights[n];
            }
	        MessageKBCT.WriteLogFile(LocaleKBCT.GetString("sum").toUpperCase()+ "  => "+df.format(total), "RuleRanking");
        }
      }
      //System.out.println("lim="+lim);
      for (int i=0; i<lim; i++) {
	       Rule r= rules[orderedRules[i]-1];
	       if (automatic) {
	           MessageKBCT.WriteLogFile(LocaleKBCT.GetString("Rule")+r.GetNumber(), "RuleRanking");
	       }
	       r.SetNumber(SelectedRules[i]+1);
       	   if ( (contInActive>0) && (i==lim-contInActive) ) {
    	      r.SetActive(false);
    	      contInActive--;
    	   } else {
    	      r.SetActive(true);
    	   }
      	   this.Temp_kbct.ReplaceRule(SelectedRules[i]+1, r);
	  }
     if (automatic) {
         this.RuleRankingLogFile("end");
     } 
     if (this.isShowing()) {
   	  //System.out.println("JEF showing");
	     this.ReInitTableRules();
     }
	  //System.out.println("reverse order end");
  }
//------------------------------------------------------------------------------
  void jPopupOrderByOutputClass_actionPerformed() { this.OrderByOutputClass(false); }
//------------------------------------------------------------------------------
  protected void OrderByOutputClass(boolean automatic) {
  	  int[] SelectedRules=null;
      if (automatic) {
          this.RuleRankingLogFile("init");
    	  // all 
          int NbRules= this.Temp_kbct.GetNbRules();
          SelectedRules= new int[NbRules];
          for (int n=0; n<NbRules; n++) {
        	   SelectedRules[n]= n;
          }
      } else {
  	      SelectedRules= this.jTableRules.getSelectedRows();
      }
  	  int lim=SelectedRules.length;
  	  //System.out.println("lim="+lim);
      int[] ruleOutputs= new int[lim];
	  int[] orderedRules= new int[lim];
      Rule[] rules= new Rule[lim];
      for (int i=0; i<lim; i++) {
	       int SelRule= SelectedRules[i];
	       rules[i]= this.Temp_kbct.GetRule(SelRule+1);
           int[] outs= rules[i].Get_out_labels_number();
           ruleOutputs[i]= outs[0];
	  }
	  // ordenar las reglas
	  for (int i=0; i<lim; i++) {
           orderedRules[i]= i+1;
      }
      for (int i=1; i<lim; i++) {
           for (int k=0; k<i; k++) {
          	  //System.out.println("i="+i+"  k="+k);
           	  //System.out.println("orderedRules[i]="+orderedRules[i]+"  orderedRules[k]="+orderedRules[k]);
	            if (ruleOutputs[orderedRules[i]-1]<ruleOutputs[orderedRules[k]-1]) {
	                for (int m=i; m>k; m--) {
	                     orderedRules[m]=orderedRules[m-1];
	                }
	                orderedRules[k]=i+1;
	                break;
	            }
	       }
      }
	  this.orderSelectedRules(null, SelectedRules, orderedRules, rules, automatic, 0);
  }
//------------------------------------------------------------------------------
  void jPopupOrderByNbPremisesRules_actionPerformed() { this.OrderByNbPremisesRules(false); }
//------------------------------------------------------------------------------
  protected void OrderByNbPremisesRules(boolean automatic) {
      int[] SelectedRules= null;
      if (automatic) {
          this.RuleRankingLogFile("init");
    	  SelectedRules= this.buildSelectedRules();
      } else {      
          SelectedRules= this.jTableRules.getSelectedRows();
      }
      int lim=SelectedRules.length;
      int[] ruleNbPremises= new int[lim];
	  int[] orderedRules= new int[lim];
      Rule[] rules= new Rule[lim];
      for (int i=0; i<lim; i++) {
	       int SelRule= SelectedRules[i];
	       rules[i]= this.Temp_kbct.GetRule(SelRule+1);
           int[] premises= rules[i].Get_in_labels_number();
           int cont=0;
           for (int k=0; k<premises.length; k++) {
          	 if (premises[k]!=0)
          		 cont++;
           }
           ruleNbPremises[i]=cont;
	  }
	  // ordenar las reglas
	  for (int i=0; i<lim; i++) {
          orderedRules[i]= i+1;
      }
      for (int i=1; i<lim; i++) {
             for (int k=0; k<i; k++) {
	              if (ruleNbPremises[orderedRules[i]-1]<ruleNbPremises[orderedRules[k]-1]) {
	                  for (int m=i; m>k; m--) {
	                      orderedRules[m]=orderedRules[m-1];
	                  }
	                  orderedRules[k]=i+1;
	                  break;
	              }
	         }
      }
	  this.orderSelectedRules(null, SelectedRules, orderedRules, rules, automatic, 0);
  }
//------------------------------------------------------------------------------
  void jPopupLocalInterpretabilityRules_actionPerformed() { this.CumulatedIntWeightRules("Local", false); }
//------------------------------------------------------------------------------
  void jPopupOrderByLocalInterpretabilityRules_actionPerformed() { this.OrderByInterpretabilityRules("Local", false); }
//------------------------------------------------------------------------------
  void jPopupGlobalInterpretabilityRules_actionPerformed() { this.CumulatedIntWeightRules("Global", false); }
//------------------------------------------------------------------------------
  void jPopupOrderByGlobalInterpretabilityRules_actionPerformed() { this.OrderByInterpretabilityRules("Global", false); }
//------------------------------------------------------------------------------
  void jPopupInterpretabilityRules_actionPerformed() { this.CumulatedIntWeightRules("L+G", false); }
//------------------------------------------------------------------------------
  private int CumulatedIntWeightRules(String option, boolean automatic) {
      int result=-1;
	  try {
	        JKBCT kbctaux= new JKBCT(this.Temp_kbct);
	        int NbInputs= kbctaux.GetNbInputs();
	        int[] NbLabels= new int[NbInputs];
            if (option.equals("Local") || option.equals("L+G")) {
	            for (int n=0; n<NbInputs; n++) {
	     	         NbLabels[n]= kbctaux.GetInput(n+1).GetLabelsNumber();
	            }
            }
	        // ordenar las variables por numero de apariciones
	        int[] premByVar= new int[NbInputs];
	        for (int n=0; n<premByVar.length; n++) {
	        	premByVar[n]=0;
	        }    
	        int NbRules= kbctaux.GetNbRules();
            if (option.equals("Global")  || option.equals("L+G")) {
	            for (int n=1; n<=NbRules; n++) {
	                 Rule raux= kbctaux.GetRule(n);
	                 if (raux.GetActive()) {
	                     int[] aux= raux.Get_in_labels_number();
	                     for (int k=0; k<aux.length; k++) {
	                          if (aux[k]!=0)
	                	          premByVar[k]++;
	                     }
	                 }
	            }    
	            //for (int i=0; i<NbInputs; i++) {
	              //   System.out.println("var["+String.valueOf(i+1)+"]="+premByVar[i]);
	            //}
            }
	        if (automatic) {
	            this.RuleRankingLogFile("init");
	            this.SelRuleNumbers= this.buildSelectedRules();
	        } else {      
	        	this.SelRuleNumbers= this.jTableRules.getSelectedRows();
	        }
	 	    int lim=this.SelRuleNumbers.length;
	        boolean[] actives= new boolean[NbRules];
	        for (int n=0; n<NbRules; n++) {
	    	        Rule r= kbctaux.GetRule(n+1);
	    	        actives[n]= r.GetActive();
	                r.SetActive(false);
	        }
	        this.SelRules= new Rule[lim];
	        this.ruleIntWeights= new double[lim];
	        int contInActive=0;
	        for (int n=0; n<lim; n++) {
	            int SelRule= this.SelRuleNumbers[n];
	            this.SelRules[n]= kbctaux.GetRule(SelRule+1);
	            if (actives[this.SelRuleNumbers[n]]) {
	     	        //System.out.println("SelectedRules["+n+"]="+SelectedRules[n]);
                    Rule r= kbctaux.GetRule(this.SelRuleNumbers[n]+1);
	                if (option.equals("Local")) {
		            	this.ruleIntWeights[n]= this.computeInterpLocalWeightRule(r, NbLabels);
	                } else if (option.equals("Global")) {
		            	this.ruleIntWeights[n]= this.computeInterpGlobalWeightRule(r, NbRules, premByVar);
	                } else if (option.equals("L+G")) {
		            	double local= this.computeInterpLocalWeightRule(r, NbLabels);
		            	double global= this.computeInterpGlobalWeightRule(r, NbRules, premByVar);
		            	this.ruleIntWeights[n]= (local+global)/2;
	                }
	            } else {
	            	this.ruleIntWeights[n]=-1;
	            	contInActive++;
	            }
	        }
	        result= contInActive;
	        if (!automatic) {
	            File temp= JKBCTFrame.BuildFile("tempRuleIntWeights.txt");
	            jnikbct.SaveRuleIntWeights(option, temp.getAbsolutePath(), this.SelRuleNumbers, this.ruleIntWeights);
	  	        this.jfcRuleIntWeights= new JFISConsole(this, temp.getAbsolutePath(), false);
	 	        temp.deleteOnExit();
	        }
	     } catch (Throwable t) {
	        t.printStackTrace();
	        MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);
	     }
	     return result;
  }
//------------------------------------------------------------------------------
  protected double computeInterpLocalWeightRule(Rule r, int[] NbLabels) {
	  double result=-1;
	  int[] premises= r.Get_in_labels_number();
      int lim= premises.length;
      double[] resultV= new double[lim];
      //System.out.println("rule"+r.GetNumber());
      for (int n=0; n<lim; n++) {
           double prem=0;
           if (premises[n]>0) {
               if (premises[n] <= NbLabels[n]) {
            	   prem= prem+1;
               } else if (premises[n] <= 2*NbLabels[n]) {
            	   prem= prem+NbLabels[n]-0.5;///////////
               } else {
            	   int SelLab= premises[n]- 2*NbLabels[n];
            	   int nbORlabs= jnikbct.NbORLabels(SelLab,NbLabels[n]);
            	   prem= prem+nbORlabs;
               }
               //System.out.println(" --> prem="+prem);
           }
           resultV[n]= 1-(prem/NbLabels[n]);
           //System.out.println(" --> resultV[n]="+resultV[n]);
      }
      for (int n=0; n<lim; n++) {
           if (n==0)
    	       result= resultV[n];
           else
        	   result= result*resultV[n];
      }
      //System.out.println(" --> result="+result);
      //result= 1-result;
      return result;
  }
/*
  protected double computeInterpLocalWeightRule(Rule r, int[] NbLabels) {
	  double result=-1;
	  int[] premises= r.Get_in_labels_number();
      // Total number of labels
	  int contTotalLabels=0;
	  // Number of premises
      int contPrem=0;
      // Number of elementary premises
      int contElementaryPrem=0;
      // Number of composite OR premises
      int contCompositeORPrem=0;
      // Number of composite NOT premises
      int contCompositeNOTPrem=0;
      // Maximum number of labels
      double max=0;
      int lim= premises.length;
      for (int n=0; n<lim; n++) {
   	       //max= max+NbLabels[n]-1;
    	   max= max+NbLabels[n];
           if (premises[n]>0) {
               contPrem++;
               if (premises[n] <= NbLabels[n]) {
            	   contElementaryPrem++;
            	   contTotalLabels++;
               } else if (premises[n] <= 2*NbLabels[n]) {
            	   contCompositeNOTPrem++;  	   
            	   //contTotalLabels= contTotalLabels+NbLabels[n]-1;
            	   contTotalLabels= contTotalLabels+NbLabels[n];
               } else {
            	   contCompositeORPrem++;
            	   int SelLab= premises[n]- 2*NbLabels[n];
            	   int nbORlabs= jnikbct.NbORLabels(SelLab,NbLabels[n]);
            	   //System.out.println("nbORlabs="+nbORlabs);
            	   contTotalLabels= contTotalLabels+nbORlabs;
               }
           }
      }
      //System.out.println("rule"+r.GetNumber());
      //System.out.println(" --> max="+max);
      //System.out.println(" --> contPrem="+contPrem);
      //System.out.println(" --> contElementaryPrem="+contElementaryPrem);
      //System.out.println(" --> contCompositeNOTPrem="+contCompositeNOTPrem);
      //System.out.println(" --> contCompositeORPrem="+contCompositeORPrem);
      //System.out.println(" --> contTotalLabels="+contTotalLabels);
      //result= contPrem + contElementaryPrem + contCompositeNOTPrem + contCompositeORPrem;
      //result= (contPrem + contElementaryPrem + contCompositeNOTPrem + contCompositeORPrem)/lim;
      if ( (contElementaryPrem==1) && (contCompositeORPrem==0) && (contCompositeNOTPrem==0) ) {
    	  result=1;
      } else if (contCompositeNOTPrem==lim) {
    	  result=0;	  
      } else {
          result= Math.pow(1-contTotalLabels/max, contPrem);
      }
      return result;
  }
 */
//------------------------------------------------------------------------------
  protected double computeInterpGlobalWeightRule(Rule r, int NbRules, int[] NbPremByVar) {
      double result= -1;
	  int[] premises= r.Get_in_labels_number();
      // Total number of used premises
	  int contTotalUsedPremises=0;
	  // Number of premises
      int contPrem=0;
      int lim= premises.length;
      // Maximum number of used premises
      double max=NbRules*lim;
      for (int n=0; n<lim; n++) {
           if (premises[n]>0) {
        	   contPrem++;
               contTotalUsedPremises=contTotalUsedPremises+NbPremByVar[n];
           }
      }
      //System.out.println("rule"+r.GetNumber());
      //System.out.println(" --> max="+max);
      //System.out.println(" --> contTotalUsedPremises="+contTotalUsedPremises);
      //System.out.println(" --> contPrem="+contPrem);
      if (contTotalUsedPremises==1) {
    	  result=1;
      } else {
          result= Math.pow(1-contTotalUsedPremises/max, contPrem);
      }
      return result;
  }
//------------------------------------------------------------------------------
  void jPopupOrderByInterpretabilityRules_actionPerformed() { this.OrderByInterpretabilityRules("L+G", false); }
//------------------------------------------------------------------------------
  protected void OrderByInterpretabilityRules(String option, boolean automatic) {
	    try {
            int contInActive= this.CumulatedIntWeightRules(option, automatic);
            int lim=this.SelRuleNumbers.length;
	    	// ordenar las reglas
	        int[] orderedRules= new int[lim];
	        for (int i=0; i<lim; i++) {
	             orderedRules[i]= i+1;
	        	   //System.out.println("ruleWeights[i]="+ruleWeights[i]);
	        }
	        for (int i=1; i<lim; i++) {
	             for (int n=0; n<i; n++) {
	                	if (ruleIntWeights[orderedRules[i]-1]>ruleIntWeights[orderedRules[n]-1]) {
	                		for (int m=i; m>n; m--) {
	                 			 orderedRules[m]=orderedRules[m-1];
	                  	}
	                  	orderedRules[n]= i+1;
	                    break;
	                  }
	             }
	        }
	  	    this.orderSelectedRules("INT", this.SelRuleNumbers, orderedRules, this.SelRules, automatic, contInActive);
	     } catch (Throwable t) {
	        t.printStackTrace();
	        MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);
	     }
  }
//------------------------------------------------------------------------------
  void jPopupCumulatedLocalWeightRules_actionPerformed() { this.CumulatedWeightRules("Local", false); }
//------------------------------------------------------------------------------
  protected int CumulatedWeightRules(String option, boolean automatic) {
    int result=-1;
	try {
	  if (this.Parent.kbct_Data!=null) {
          JKBCT kbctaux= new JKBCT(this.Temp_kbct);
          File tempauxKB= JKBCTFrame.BuildFile("tempLinksaux.kb.xml");
          File tempauxFIS= JKBCTFrame.BuildFile("tempLinksaux.fis");
          if (automatic) {
              this.RuleRankingLogFile("init");
        	  this.SelRuleNumbers= this.buildSelectedRules();
          } else {      
        	  this.SelRuleNumbers= this.jTableRules.getSelectedRows();
          }
          MessageKBCT.Information(this, LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                  "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                  "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
          int lim=this.SelRuleNumbers.length;
          this.ruleWeights= new double[lim];
          this.ruleWeightsCC= new double[lim];
          this.ruleWeightsIC= new double[lim];
          JExtendedDataFile jedfauxCC= new JExtendedDataFile(this.getJExtDataFile().ActiveFileName(), true);
          JExtendedDataFile jedfauxIC= new JExtendedDataFile(this.getJExtDataFile().ActiveFileName(), true);
          int NbActiveData= jedfauxCC.GetActiveCount();
          //System.out.println("NbActiveData="+NbActiveData);
          double[] outdata= jedfauxCC.VariableData(kbctaux.GetNbInputs());
          int outLabs= kbctaux.GetOutput(1).GetLabelsNumber();
          int[] NbDataClass= new int[outLabs];
          for (int n=0; n<NbActiveData; n++) {
              for (int i=0; i<outLabs; i++) {
                  if (i==outdata[n]-1) {
       	    	     NbDataClass[i]++;
       	    	     break;
       	         }
             }
          }
          //for (int n=0; n<outLabs; n++) {
       	    //   System.out.println("NbDataClass["+n+"]="+NbDataClass[n]);
          //}
          int NbRules= kbctaux.GetNbRules();
          //System.out.println("NbRules="+NbRules);
          int[] NbRuleClass= new int[outLabs];
          boolean[] actives= new boolean[NbRules];
          for (int n=0; n<NbRules; n++) {
      	    Rule r= kbctaux.GetRule(n+1);
            int out= r.Get_out_labels_number()[0];
            for (int i=0; i<outLabs; i++) {
                 if (i==out-1) {
      	    	     NbRuleClass[i]++;
       	    	     break;
      	         }
            }
      	    actives[n]= r.GetActive();
            r.SetActive(false);
          }
          //for (int n=0; n<outLabs; n++) {
        	//   System.out.println("NbRuleClass["+n+"]="+NbRuleClass[n]);
          //}
          this.SelRules= new Rule[lim];
          //jnifis.FisproPath(MainKBCT.getConfig().GetKbctPath());
          jnifis.setUserHomeFisproPath(MainKBCT.getConfig().GetKbctPath());
          int contInActive=0;
          for (int i=0; i<lim; i++) {
                 int SelRule= this.SelRuleNumbers[i];
                 this.SelRules[i]= kbctaux.GetRule(SelRule+1);
                 if (actives[SelRule]) {
                    kbctaux.GetRule(SelRule+1).SetActive(true);
                    int[] outputs= this.SelRules[i].Get_out_labels_number();
                    boolean warning= false;
                    if (kbctaux.GetOutput(1).GetClassif().equals("yes")) {
                        warning= true;
                    }
                    kbctaux.SetKBCTFile(tempauxKB.getAbsolutePath());
                    kbctaux.Save();
                    kbctaux.SaveFISquality(tempauxFIS.getAbsolutePath());
                    JFIS fis= new JFIS(tempauxFIS.getAbsolutePath());
                    double ccl=0;
                    double icl=0;
                    if (warning) {
                        double[] resCC=this.CumulatedWeights(jedfauxCC, outputs, fis, true);
                        this.ruleWeightsCC[i]= resCC[0];
             	        //  System.out.println("this.ruleWeightsCC[i]="+this.ruleWeightsCC[i]);
                        double[] resIC=this.CumulatedWeights(jedfauxIC, outputs, fis, false);
                        this.ruleWeightsIC[i]=resIC[0];
             	    //System.out.println("this.ruleWeightsIC[i]="+this.ruleWeightsIC[i]);
                    //this.ruleWeights[i]= ruleWeightsCC[i] - ruleWeightsIC[i];
                        //System.out.println("dif="+dif);
                        //double add=ruleWeightsCC[i] + ruleWeightsIC[i];
                        //System.out.println("dif="+dif+"  add="+add);
                        ccl= resCC[1];
                        icl= resIC[1];
                        //System.out.println("ccl="+ccl+"  icl="+icl);
                        //this.ruleWeights[i]= dif*(ccl-icl)/(ccl+icl);
                        //this.ruleWeights[i]= dif/ccl;
                    } else {
                    	double[] resCCIC= this.CumulatedWeights(fis);
                        this.ruleWeightsCC[i]= resCCIC[0];
                        ccl= resCCIC[1];
                        this.ruleWeightsIC[i]= resCCIC[2];
                        icl= resCCIC[3];
                    }
                    double dif=this.ruleWeightsCC[i] - this.ruleWeightsIC[i];
	                if (option.equals("Local")) {
	                	// similar to Support
	                    this.ruleWeights[i]= dif/(ccl+icl);
	                    //System.out.println("i="+i);
	             	    //System.out.println("--> this.ruleWeightsCC[i]="+this.ruleWeightsCC[i]);
	             	    //System.out.println("--> this.ruleWeightsIC[i]="+this.ruleWeightsIC[i]);
	                    //System.out.println("--> dif="+dif);
	                    //System.out.println("--> this.ruleWeights[i]="+this.ruleWeights[i]);
	                } else if (option.equals("Global")) {
	                	// similar to Unusualness
		            	//this.ruleWeights[i]= ((ccl+icl)/NbActiveData)*( (ccl/(ccl+icl)) - ((NbDataClass[outputs[0]-1]/NbActiveData)*(NbRuleClass[outputs[0]-1]/NbRules)) );
	                    double rc=NbRuleClass[outputs[0]-1];
	                    double tr=NbRules;
                        double dc=NbDataClass[outputs[0]-1];
                        double td=NbActiveData;
	                	this.ruleWeights[i]= 1 - ( 0.1*(rc/tr) + 0.9*((ccl+icl)/td)*( (ccl/(ccl+icl)) - (dc/td) ) );
	                    //System.out.println("i="+i);
	                    //System.out.println("--> nbrClr="+NbRuleClass[outputs[0]-1]);
	                    //System.out.println("--> nbr="+NbRules);
	                    //System.out.println("--> nbrClr/nbr="+String.valueOf(NbRuleClass[outputs[0]-1]/NbRules));
	                    //System.out.println("--> nbrClr/nbr="+c);
	                    //System.out.println("--> cc="+ccl);
	                    //System.out.println("--> ic="+icl);
	                    //System.out.println("--> cc+ic="+String.valueOf(ccl+icl));
	                    //System.out.println("--> exClr="+NbDataClass[outputs[0]-1]);
	                    //System.out.println("--> totData="+NbActiveData);
	                    //System.out.println("--> exClr/totData="+String.valueOf(NbDataClass[outputs[0]-1]/NbActiveData));
	                    //System.out.println("--> i1="+String.valueOf(0.1*(rc/tr)));
	                    //System.out.println("--> i2="+String.valueOf(0.9*((ccl+icl)/td)));
	                    //System.out.println("--> i3="+String.valueOf((ccl/(ccl+icl)) - (dc/td)));
	                    //System.out.println("--> this.ruleWeights[i]="+this.ruleWeights[i]);
	                } else if (option.equals("L+G")) {
		            	double local= this.ruleWeights[i]= dif/(ccl+icl);
		            	//double global= ((ccl+icl)/NbActiveData)*( (ccl/(ccl+icl)) - ((NbDataClass[outputs[0]-1]/NbActiveData)*(NbRuleClass[outputs[0]-1]/NbRules)) );
	                    double rc=NbRuleClass[outputs[0]-1];
	                    double tr=NbRules;
                        double dc=NbDataClass[outputs[0]-1];
                        double td=NbActiveData;
                        double global= 1 - ( 0.1*(rc/tr) + 0.9*((ccl+icl)/td)*( (ccl/(ccl+icl)) - (dc/td) ) );
		            	this.ruleWeights[i]= (local+global)/2;
	                    //System.out.println("i="+i);
	             	    //System.out.println("--> this.ruleWeightsCC[i]="+this.ruleWeightsCC[i]);
	             	    //System.out.println("--> this.ruleWeightsIC[i]="+this.ruleWeightsIC[i]);
	                    //System.out.println("--> dif="+dif);
	                    //System.out.println("--> nbrClr="+NbRuleClass[outputs[0]-1]);
	                    //System.out.println("--> nbr="+NbRules);
	                    //System.out.println("--> cc="+ccl);
	                    //System.out.println("--> ic="+icl);
	                    //System.out.println("--> cc+ic="+String.valueOf(ccl+icl));
	                    //System.out.println("--> totData="+NbActiveData);
	                    //System.out.println("--> exClr="+NbDataClass[outputs[0]-1]);
	                    //System.out.println("--> local="+local);
	                    //System.out.println("--> global="+global);
	                    //System.out.println("--> this.ruleWeights[i]="+this.ruleWeights[i]);
	                }
                    kbctaux.GetRule(SelRule+1).SetActive(false);
                 } else { 
                	this.ruleWeights[i]= -(NbActiveData+1);
               	    contInActive++;
                 } 
            }
          result= contInActive;
          if (!automatic) {
              File temp= JKBCTFrame.BuildFile("tempRuleWeights.txt");
              jnikbct.SaveRuleWeights(option, temp.getAbsolutePath(), this.SelRuleNumbers, this.ruleWeights, this.ruleWeightsCC, this.ruleWeightsIC);
  	          this.jfcRuleWeights= new JFISConsole(this, temp.getAbsolutePath(), false);
              temp.deleteOnExit();
          }
      }
    } catch (Throwable t) {
        t.printStackTrace();
        MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);
    }
    return result;
  }
//------------------------------------------------------------------------------
  void jPopupOrderByLocalWeightRules_actionPerformed() { this.OrderByWeightRules("Local", false); }
//------------------------------------------------------------------------------
  protected void OrderByWeightRules(String option, boolean automatic) {
    try {
      if (this.Parent.kbct_Data!=null) {
          int contInActive= this.CumulatedWeightRules(option, automatic);
          int lim=this.SelRuleNumbers.length;
          int[] orderedRules= new int[lim];
          // ordenar las reglas
          for (int i=0; i<lim; i++) {
        	   orderedRules[i]= i+1;
        	   //System.out.println("ruleWeights[i]="+ruleWeights[i]);
          }
          for (int i=1; i<lim; i++) {
               for (int n=0; n<i; n++) {
                  	if (this.ruleWeights[orderedRules[i]-1]>this.ruleWeights[orderedRules[n]-1]) {
                  		for (int m=i; m>n; m--) {
                   			 orderedRules[m]=orderedRules[m-1];
                    	}
                    	orderedRules[n]= i+1;
                        break;
                    }
               }
          }
   	      this.orderSelectedRules("ACC", this.SelRuleNumbers, orderedRules, this.SelRules, automatic, contInActive);
          if (automatic)
              MessageKBCT.Information(this, LocaleKBCT.GetString("AutomaticRuleRankingEnded"));

      }
    } catch (Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);
    }
  }
//------------------------------------------------------------------------------
  private double[] CumulatedWeights(JExtendedDataFile jedfaux, int[] outputs, JFIS fis, boolean CC) {
    double[] result= new double[2];
    result[0]= 0; //ruleWeight
    result[1]= 0; //number
    try {
        // Desactivar los datos cuya conclusion no coincide con la de la regla
        boolean[] dataActive= new boolean[jedfaux.DataLength()];
        int contInactiveData=dataActive.length-jedfaux.GetActiveCount();
        boolean AllInactive=false;
        for (int k=0; k<dataActive.length; k++) {
           dataActive[k]= jedfaux.GetActive(k);
           boolean incorrectConclusion= false;
           for (int m=0; m<outputs.length; m++) {
                int var= jedfaux.VariableCount()-outputs.length+m;
                double output= jedfaux.VariableData(var)[k];
                if (outputs[m] != output) {
                    incorrectConclusion= true;
                }
           }
           if ( ( (CC) && (incorrectConclusion) ) ||
        		( (!CC) && (!incorrectConclusion) ) )  {
               contInactiveData++;
               if (contInactiveData==dataActive.length)
                   AllInactive=true;
               else
               	   jedfaux.SetActive(k+1, false);
           } 
         }
         if (!AllInactive) {
    	   // System.out.println("fis.rules="+fis.NbActiveRules());
           fis.Links(jedfaux, 1e-6, false, jedfaux.ActiveFileName(), null);
           result= this.readRulesItemsFile(jedfaux.ActiveFileName()+"."+jnifis.LinksRulesItemsExtension());
         } 
         for (int k=0; k<dataActive.length; k++) {
     	    jedfaux.SetActive(k+1, dataActive[k]);
         }
    } catch (Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);
    }
    return result;
  }
//------------------------------------------------------------------------------
  private double[] CumulatedWeights(JFIS fis) {
    double[] result= new double[4];
    result[0]= 0; //ruleWeightCC
    result[1]= 0; //numberCC
    result[2]= 0; //ruleWeightIC
    result[3]= 0; //numberIC
    try {
    	  JExtendedDataFile jedfaux= new JExtendedDataFile(this.getJExtDataFile().ActiveFileName(), true);
    	  File f= new File(jedfaux.ActiveFileName());
          File temp= JKBCTFrame.BuildFile(f.getName()+".tmp");
          //System.out.println("temp.getAbsolutePath() -> "+temp.getAbsolutePath());
    	  fis.Links(jedfaux, 1e-6, false, temp.getAbsolutePath(), null);
    	  //fis.Links(jedfaux, 1e-6, false, jedfaux.ActiveFileName(), null);
		  String infile= temp.getAbsolutePath()+".items.murules";
    	  int NbData= jedfaux.DataLength();
    	  int NbAcRs= fis.NbActiveRules();
    	  //System.out.println("NbAcRs -> "+NbAcRs);
          double[][] murules= new double[NbData][NbAcRs];
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
          double rulesCCFiringDegree= 0;
          double rulesICFiringDegree= 0;
          int contCC=0;
          int contIC=0;
		  for (int i=0; i<NbData; i++) {
			 if (murules[i][0] > 0) {
				 boolean cc= false;
				 if (murules[i][0] >= MainKBCT.getConfig().GetBlankThres()) {
	 	    		 cc = true;
				 }
			     if (cc) {
			    	 rulesCCFiringDegree= rulesCCFiringDegree + murules[i][0];
					 contCC++;
				 } else {
				   	 rulesICFiringDegree= rulesICFiringDegree + murules[i][0];
					 contIC++;
			     }
			 }
         }
	     result[0]= rulesCCFiringDegree;
	     result[1]= contCC;
	     result[2]= rulesICFiringDegree;
		 result[3]= contIC++;
    } catch (Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);
    }
    return result;
  }
//------------------------------------------------------------------------------
  private double[] readRulesItemsFile(String file) {
    double[] result= new double[2];
    result[0]= 0; //weight
    result[1]= 0; //number
    try {
      LineNumberReader lnr= new LineNumberReader(new InputStreamReader(new FileInputStream(file)));
      lnr.readLine(); // numero de reglas
      lnr.readLine(); // numero maximo de datos manejados por una regla
      String l;
      while ((l=lnr.readLine())!=null) {
       	    int pos= l.indexOf(",");
      	    String aux= l.substring(pos+1);
            pos= aux.indexOf(",");
            result[0]= result[0] + new Double(aux.substring(0,pos)).doubleValue();
            aux= aux.substring(pos+1);
            pos= aux.indexOf(",");
            result[1]= result[1] + new Double(aux.substring(0,pos)).doubleValue();
            //while (pos>0) {
            //   System.out.println("pos="+pos+"  aux="+aux.substring(0,pos));
            //   result[1]++;
            //   aux= aux.substring(pos+1);
            //   pos= aux.indexOf(",");
            //}
      }
    } catch( Throwable except ) { 
        except.printStackTrace();
    	MessageKBCT.Error( null, except );
    }
    return result;
  }
//------------------------------------------------------------------------------
  void jPopupCumulatedGlobalWeightRules_actionPerformed() { this.CumulatedWeightRules("Global", false); }
//------------------------------------------------------------------------------
  void jPopupOrderByGlobalWeightRules_actionPerformed() { this.OrderByWeightRules("Global", false); }
//------------------------------------------------------------------------------
  void jPopupCumulatedWeightRules_actionPerformed() { this.CumulatedWeightRules("L+G", false); }
//------------------------------------------------------------------------------
  void jPopupOrderByWeightRules_actionPerformed() { this.OrderByWeightRules("L+G", false); }
//------------------------------------------------------------------------------
  void jPopupSelectRule_actionPerformed() { this.RuleSelection(); }
//------------------------------------------------------------------------------
  private void RuleSelection() {
    //System.out.println("Rule Selection: init");
    try {
        String IntIndex= "";
        int iindex= MainKBCT.getConfig().GetRuleSelectionInterpretabilityIndex();
        switch (iindex) {
          // NR
          case 0: IntIndex= LocaleKBCT.GetString("IshibuchiNbRules");
                  break;                        
          // TRL
          case 1: IntIndex= LocaleKBCT.GetString("IshibuchiTotalRuleLength");
                  break;
          // Max SFR
          case 2: IntIndex= LocaleKBCT.GetString("MaxFiredRulesTraining");
                  break;
          // Average SFR
          case 3: IntIndex= LocaleKBCT.GetString("AverageFiredRulesTraining");
                  break;
          // Min SFR
          case 4: IntIndex= LocaleKBCT.GetString("MinFiredRulesTraining");
                  break;
          // AccRuleComp
          case 5: IntIndex= LocaleKBCT.GetString("AccumulatedRuleComplexity");
                  break;
          // HILK IntIndex
          case 6: IntIndex= LocaleKBCT.GetString("InterpretabilityIndex")+" (HILK)";
                  break;
          // LogView
          case 7:  IntIndex= LocaleKBCT.GetString("LVindexTraining");
                   break;
          default: IntIndex= LocaleKBCT.GetString("IshibuchiTotalRuleLength");
                   break;
        }
    	String message= LocaleKBCT.GetString("SelectedValues")+":"+"\n"
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
        int option=1;
        if (!MainKBCT.getConfig().GetTESTautomatic())
            option= MessageKBCT.Confirm(this, message, 1, false, false, false);

        if (option==0) {
            this.setOptimizationGARSParameters();
        }
        if (!MainKBCT.getConfig().GetTESTautomatic()) {
             MessageKBCT.Information(this, LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                                     "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                                     "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
        }
	    int[] SelectedRules= null;
	    boolean[] SelRules= null;
	    boolean[] RulesActivation= null;
	    boolean[] RulesActivationNonSelRules= null;
        SelectedRules= this.jTableRules.getSelectedRows();
        int lim= SelectedRules.length;
       	int NbRules= this.Temp_kbct.GetNbRules();
	    if (lim > 1) {
	       	SelRules= new boolean[NbRules];
	       	RulesActivation= new boolean[NbRules];
	       	for (int n=0; n<NbRules; n++) {
	       		 RulesActivation[n]= this.Temp_kbct.GetRule(n+1).GetActive();
	       		 this.Temp_kbct.SetRuleActive(n+1, false);
	       		 SelRules[n]= false;
	       	}
	       	for (int n=0; n<lim; n++) {
           		 this.Temp_kbct.SetRuleActive(SelectedRules[n]+1, RulesActivation[SelectedRules[n]]);
           		 SelRules[SelectedRules[n]]= true;
	       	}
	       	//System.out.println("NAR="+this.Temp_kbct.GetNbActiveRules());
	       	RulesActivationNonSelRules= new boolean[NbRules-lim];
	       	int c=0;
	       	for (int n=0; n<NbRules; n++) {
	       		 if (!SelRules[n])
	       		     RulesActivationNonSelRules[c++]= RulesActivation[n];
	       	}
	    }
        GAruleSelection gars;
        long[] exc= new long[8];
        exc[0]= this.Temp_kbct.GetPtr();
        exc[1]= this.Temp_kbct.GetCopyPtr();
        if (this.Parent.kbct_Data!=null) {
      	    exc[2]= this.Parent.kbct_Data.GetPtr();
            exc[3]= this.Parent.kbct_Data.GetCopyPtr();
        } else {
       	    exc[2]= -1;
            exc[3]= -1;
        }
        if (this.Parent.kbct!=null) {
            exc[4]= this.Parent.kbct.GetPtr();
            exc[5]= this.Parent.kbct.GetCopyPtr();
        } else {
      	    exc[4]= -1;
            exc[5]= -1;
        }
        if (this.kbct!=null) {
       	    exc[6]= this.kbct.GetPtr();
            exc[7]= this.kbct.GetCopyPtr();
        } else {
       	    exc[6]= -1;
            exc[7]= -1;
        }
 	    File fname= new File(JKBCTFrame.KBExpertFile);
	    String name=fname.getName();
	    String LogName="LogOptimizationGARS_"+name.substring(0,name.length()-7);
	    File f= JKBCTFrame.BuildFile(LogName);
        String OptimizationLogFile= f.getAbsolutePath();
	    boolean aux= MainKBCT.getConfig().GetTESTautomatic();
	    MainKBCT.getConfig().SetTESTautomatic(true);
	    MessageKBCT.BuildLogFile(OptimizationLogFile+".txt", this.Parent.IKBFile, JKBCTFrame.KBExpertFile, this.Parent.KBDataFile, "Optimization");
	    String LogOGARSconfig="LogOGARSconfig_"+name.substring(0,name.length()-7);
	    File f_new= new File(f.getParentFile().getAbsolutePath()+System.getProperty("file.separator")+LogOGARSconfig);
	    if (!f_new.exists()) {
            MessageKBCT.WriteLogFile((LocaleKBCT.GetString("AutomaticOptimizationProcedure")).toUpperCase(), "Optimization");
            MessageKBCT.WriteLogFile("----------------------------------", "Optimization");
            MessageKBCT.WriteLogFile(LocaleKBCT.GetString("RuleSelection").toUpperCase(), "Optimization");
            gars= new GAruleSelection(this.Temp_kbct, this.getJExtDataFile(), exc, LogOGARSconfig, false);
        } else {
            gars= new GAruleSelection(this.Temp_kbct, this.getJExtDataFile(), exc, LogOGARSconfig, true);
        }
	    MainKBCT.getConfig().SetTESTautomatic(aux);
        if (MainKBCT.getConfig().GetRuleSelectionInitialGeneration()==LocaleKBCT.DefaultRuleSelectionInitialGeneration()) {
      	    this.Temp_kbct= gars.GetKBCT();
            MessageKBCT.WriteLogFile("----------------------------------", "Optimization");
            MessageKBCT.WriteLogFile(LocaleKBCT.GetString("AutomaticOptimizationEnded"), "Optimization");
            Date d= new Date(System.currentTimeMillis());
            MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "Optimization");
        	MessageKBCT.CloseLogFile("Optimization");
            if (!MainKBCT.getConfig().GetTESTautomatic()) {
                try {
              	  this.jfcOF= new JFISConsole(this, OptimizationLogFile+".txt", false);
                } catch (Throwable t) {
                	t.printStackTrace();
                	MessageKBCT.Error(null, t);
                }
              }
        }	else {
        	  MessageKBCT.CloseLogFile("Optimization");
        }
        /*System.out.println("selRules -> "+lim);
        System.out.println("iniRules -> "+NbRules);
        NbRules= this.Temp_kbct.GetNbRules();
        System.out.println("finules -> "+NbRules);
        for (int n=0; n<NbRules; n++) {
        	 Rule r= this.Temp_kbct.GetRule(n+1);
             System.out.println("rule -> "+r.GetNumber());
             int[] ins= r.Get_in_labels_number();
        	 for (int m=0; m<ins.length; m++) {
                  System.out.println("   "+ins[m]);
        	 }
             System.out.println("   "+r.Get_out_labels_number()[0]);
        }*/
        if (RulesActivationNonSelRules != null) {
            for (int n=0; n<RulesActivationNonSelRules.length; n++) {
          		 this.Temp_kbct.SetRuleActive(n+1, RulesActivationNonSelRules[n]);
            }
        }
        //this.pack();
        //this.repaint();
        this.ReInitTableRules();
      } catch (Throwable t) {
          t.printStackTrace();
    	  MessageKBCT.Error(null,t);
      }
    //System.out.println("Rule Selection: end");
  }
//------------------------------------------------------------------------------
  void jPopupCopyRule_actionPerformed() { this.CopyRules(); }
//------------------------------------------------------------------------------
  private void CopyRules() {
    try {
      int[] SelectedRules= this.jTableRules.getSelectedRows();
      for (int i=0; i<SelectedRules.length; i++) {
         int SelRule= SelectedRules[i];
         Rule rSel= this.Temp_kbct.GetRule(SelRule+1);
         int NbInputs= rSel.GetNbInputs();
         int[] inputsSel= rSel.Get_in_labels_number();
         int[] inputsNew= new int[NbInputs];
         for (int n=0; n<NbInputs; n++) {
           inputsNew[n]= inputsSel[n];
         }
         int NbOutputs= rSel.GetNbOutputs();
         int[] outputsSel= rSel.Get_out_labels_number();
         int[] outputsNew= new int[NbOutputs];
         for (int n=0; n<NbOutputs; n++) {
           outputsNew[n]= outputsSel[n];
         }
         Rule rNew= new Rule(rSel.GetNumber(), NbInputs, NbOutputs, inputsNew, outputsNew, rSel.GetType(), rSel.GetActive());
         this.Temp_kbct.AddRule( rNew );
         this.InitJExpertFrameWithKBCT();
      }
    } catch (Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);
    }
  }
//------------------------------------------------------------------------------
  private void UpRules () {
    int[] SelectedRules= this.jTableRules.getSelectedRows();
    this.UpRules(SelectedRules);
    try { this.InitJExpertFrameWithKBCT(); }
    catch (Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);}
  }
//------------------------------------------------------------------------------
  private void DownRules () {
    int[] SelectedRules= this.jTableRules.getSelectedRows();
    this.DownRules(SelectedRules);
    try { this.InitJExpertFrameWithKBCT(); }
    catch (Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);}
  }
//------------------------------------------------------------------------------
  private void UpRules (int[] SelectedRules) {
    for (int i=0; i<SelectedRules.length; i++) {
       int SelR= SelectedRules[i];
       Rule SelRule= this.Temp_kbct.GetRule(SelR+1);
       Rule PrevRule= this.Temp_kbct.GetRule(SelR);
       this.Temp_kbct.ReplaceRule(SelR, SelRule);
       this.Temp_kbct.ReplaceRule(SelR+1, PrevRule);
    }
  }
//------------------------------------------------------------------------------
  void jPopupUpRule_actionPerformed() { this.UpRules(); }
//------------------------------------------------------------------------------
  void DownRules(int[] SelectedRules) {
    for (int i=SelectedRules.length; i > 0; i--) {
       int SelR= SelectedRules[i-1];
       Rule SelRule= this.Temp_kbct.GetRule(SelR+1);
       Rule NextRule= this.Temp_kbct.GetRule(SelR+2);
       this.Temp_kbct.ReplaceRule(SelR+1, NextRule);
       this.Temp_kbct.ReplaceRule(SelR+2, SelRule);
    }
  }
//------------------------------------------------------------------------------
  void jPopupDownRule_actionPerformed() { this.DownRules(); }
//------------------------------------------------------------------------------
  void jPopupExpandRule_actionPerformed() { this.ExpandRules(); }
//------------------------------------------------------------------------------
  void jPopupMergeRule_actionPerformed() { this.MergeRules(); }
//------------------------------------------------------------------------------
  void jPopupActiveRule_actionPerformed() { this.ActiveRules(); }
//------------------------------------------------------------------------------
  void jPopupInActiveRule_actionPerformed() { this.DeActiveRules(); }
//------------------------------------------------------------------------------
  void jPopupNewOutput_actionPerformed() { this.NewOutput(); }
//------------------------------------------------------------------------------
  void jPopupEditOutput_actionPerformed() { this.EditOutput(); }
//------------------------------------------------------------------------------
  void jPopupRemoveOutput_actionPerformed() { this.RemoveOutput(); }
//------------------------------------------------------------------------------
  void jPopupCopyOutput_actionPerformed() { this.CopyOutput(); }
//------------------------------------------------------------------------------
  void jPopupUpOutput_actionPerformed() { this.UpOutput(); }
//------------------------------------------------------------------------------
  void jPopupDownOutput_actionPerformed() { this.DownOutput(); }
//------------------------------------------------------------------------------
  void jMenuKBCT_menuSelected() {
    if( this.Temp_kbct == null ) {
        this.jMenuClose.setEnabled(false);
        this.jMenuSave.setEnabled(false);
        this.jMenuSaveAs.setEnabled(false);
        this.jMenuSaveAsFIS.setEnabled(false);
        this.jMenuLoadOntology.setEnabled(false);
        this.jMenuSaveAsFuzzyOntology.setEnabled(false);
        this.jMenuSaveAsFuzzyOntologyWithoutRules.setEnabled(false);
    } else {
        this.jMenuClose.setEnabled(true);
        if( this.Temp_kbct.GetKBCTFile() == null ) {
            this.jMenuSave.setEnabled(false);
            this.jMenuSaveAsFIS.setEnabled(false);
        } else {
            this.jMenuSave.setEnabled(true);
            this.jMenuSaveAsFIS.setEnabled(true);
        }
        this.jMenuSaveAs.setEnabled(true);
        this.jMenuLoadOntology.setEnabled(true);
  	    //System.out.println("JKBCTFrame.OntologyFile -> "+JKBCTFrame.OntologyFile);
        if ( (JKBCTFrame.OntologyFile!=null) && (!JKBCTFrame.OntologyFile.equals("")) ) {
            this.jMenuSaveAsFuzzyOntology.setEnabled(true);
            if (this.Temp_kbct.GetNbActiveRules() > 0) {
                this.jMenuSaveAsFuzzyOntologyWithoutRules.setEnabled(true);
            } else {
            	this.jMenuSaveAsFuzzyOntologyWithoutRules.setEnabled(false);
            }
        } else {
            this.jMenuSaveAsFuzzyOntology.setEnabled(false);
            this.jMenuSaveAsFuzzyOntologyWithoutRules.setEnabled(false);
        }
    }
  }
//------------------------------------------------------------------------------
  void jMenuInputs_menuSelected() {
    if( this.Temp_kbct == null ) {
      this.jMenuNewInput.setEnabled(false);
      this.jMenuEditInput.setEnabled(false);
      this.jMenuRemoveInput.setEnabled(false);
      this.jMenuCopyInput.setEnabled(false);
      this.jMenuUpInput.setEnabled(false);
      this.jMenuDownInput.setEnabled(false);
    } else {
        this.jMenuNewInput.setEnabled(true);
        if( this.jListInputs.getSelectedIndices().length == 0 ) {
           this.jMenuEditInput.setEnabled(false);
           this.jMenuRemoveInput.setEnabled(false);
           this.jMenuCopyInput.setEnabled(false);
           this.jMenuUpInput.setEnabled(false);
           this.jMenuDownInput.setEnabled(false);
        } else {
           this.jMenuEditInput.setEnabled(true);
           this.jMenuRemoveInput.setEnabled(true);
           this.jMenuCopyInput.setEnabled(true);
           int[] SelectedInputs= this.jListInputs.getSelectedIndices();
           if ( SelectedInputs.length > 1 ) {
               int FirstSelectedInput= SelectedInputs[0];
               int LastSelectedInput= SelectedInputs[SelectedInputs.length-1];
               if ( (LastSelectedInput - FirstSelectedInput + 1) == SelectedInputs.length ) {
                 // Consecutive inputs
                   if ( FirstSelectedInput == 0 )
                        this.jMenuUpInput.setEnabled(false);
                   else
                        this.jMenuUpInput.setEnabled(true);

                   if ( LastSelectedInput == this.Temp_kbct.GetNbInputs() - 1 )
                        this.jMenuDownInput.setEnabled(false);
                   else
                        this.jMenuDownInput.setEnabled(true);
               } else {
                   this.jMenuUpInput.setEnabled(false);
                   this.jMenuDownInput.setEnabled(false);
               }
           } else {
               if ( this.jListInputs.getSelectedIndex() == 0 )
                    this.jMenuUpInput.setEnabled(false);
               else
                  this.jMenuUpInput.setEnabled(true);

               if ( this.jListInputs.getSelectedIndex() == this.Temp_kbct.GetNbInputs() - 1 )
                    this.jMenuDownInput.setEnabled(false);
               else
                    this.jMenuDownInput.setEnabled(true);
           }
        }
    }
  }
//------------------------------------------------------------------------------
  void jMenuRules_menuSelected() {
    if( this.Temp_kbct == null ) {
      this.jMenuNewRule.setEnabled(false);
      this.jMenuRemoveRule.setEnabled(false);
      this.jMenuRemoveRedundantRules.setEnabled(false);
      this.jMenuReverseOrder.setEnabled(false);
      this.jMenuOrderByOutputClass.setEnabled(false);
      this.jMenuCumulatedLocalWeightRules.setEnabled(false);
      this.jMenuOrderByLocalWeightRules.setEnabled(false);
      this.jMenuCumulatedGlobalWeightRules.setEnabled(false);
      this.jMenuOrderByGlobalWeightRules.setEnabled(false);
      this.jMenuCumulatedWeightRules.setEnabled(false);
      this.jMenuOrderByWeightRules.setEnabled(false);
      this.jMenuLocalInterpretabilityRules.setEnabled(false);
      this.jMenuOrderByLocalInterpretabilityRules.setEnabled(false);
      this.jMenuGlobalInterpretabilityRules.setEnabled(false);
      this.jMenuOrderByGlobalInterpretabilityRules.setEnabled(false);
      this.jMenuInterpretabilityRules.setEnabled(false);
      this.jMenuOrderByInterpretabilityRules.setEnabled(false);
      this.jMenuOrderByNbPremisesRules.setEnabled(false);
      this.jMenuSelectRules.setEnabled(false);
      this.jMenuCopyRules.setEnabled(false);
      this.jMenuUpRules.setEnabled(false);
      this.jMenuDownRules.setEnabled(false);
      this.jMenuExpandRules.setEnabled(false);
      this.jMenuMergeRules.setEnabled(false);
      this.jMenuFingramRules.setEnabled(false);
      this.jMenuActiveRules.setEnabled(false);
      this.jMenuInActiveRules.setEnabled(false);
      this.jMenuGenerateAllRules.setEnabled(false);
      this.jMenuPrintRules.setEnabled(false);
      this.jMenuExportRules.setEnabled(false);
    } else if (this.Temp_kbct.GetNbActiveRules()>0) {
        this.jMenuNewRule.setEnabled(true);
        if ( this.jTableRules.getSelectedRowCount() > 0 ) {
          this.jMenuRemoveRule.setEnabled(true);
          this.jMenuCopyRules.setEnabled(true);
          this.jMenuActiveRules.setEnabled(true);
          this.jMenuInActiveRules.setEnabled(true);
          this.jMenuRemoveRedundantRules.setEnabled(true);
    	  if (this.Parent.kbct_Data!=null) {
              this.jMenuCumulatedLocalWeightRules.setEnabled(true);
              this.jMenuCumulatedGlobalWeightRules.setEnabled(true);
              this.jMenuCumulatedWeightRules.setEnabled(true);
    	  } else {
              this.jMenuCumulatedLocalWeightRules.setEnabled(false);
              this.jMenuCumulatedGlobalWeightRules.setEnabled(false);
              this.jMenuCumulatedWeightRules.setEnabled(false);
    	  }
          this.jMenuLocalInterpretabilityRules.setEnabled(true);
          this.jMenuGlobalInterpretabilityRules.setEnabled(true);
          this.jMenuInterpretabilityRules.setEnabled(true);
        } else {
          this.jMenuRemoveRule.setEnabled(false);
          this.jMenuReverseOrder.setEnabled(false);
          this.jMenuOrderByOutputClass.setEnabled(false);
          this.jMenuCumulatedLocalWeightRules.setEnabled(false);
          this.jMenuOrderByLocalWeightRules.setEnabled(false);
          this.jMenuCumulatedGlobalWeightRules.setEnabled(false);
          this.jMenuOrderByGlobalWeightRules.setEnabled(false);
          this.jMenuCumulatedWeightRules.setEnabled(false);
          this.jMenuOrderByWeightRules.setEnabled(false);
          this.jMenuLocalInterpretabilityRules.setEnabled(false);
          this.jMenuOrderByLocalInterpretabilityRules.setEnabled(false);
          this.jMenuGlobalInterpretabilityRules.setEnabled(false);
          this.jMenuOrderByGlobalInterpretabilityRules.setEnabled(false);
          this.jMenuInterpretabilityRules.setEnabled(false);
          this.jMenuOrderByInterpretabilityRules.setEnabled(false);
          this.jMenuOrderByNbPremisesRules.setEnabled(false);
          this.jMenuSelectRules.setEnabled(false);
          this.jMenuCopyRules.setEnabled(false);
          this.jMenuUpRules.setEnabled(false);
          this.jMenuDownRules.setEnabled(false);
          this.jMenuRemoveRedundantRules.setEnabled(false);
          this.jMenuExpandRules.setEnabled(false);
          this.jMenuMergeRules.setEnabled(false);
          this.jMenuFingramRules.setEnabled(false);
          this.jMenuActiveRules.setEnabled(false);
          this.jMenuInActiveRules.setEnabled(false);
        }
        this.jMenuGenerateAllRules.setEnabled(false);
        this.jMenuPrintRules.setEnabled(true);
        this.jMenuExportRules.setEnabled(true);
        if ( this.jTableRules.getSelectedRowCount() > 1 ) {
            this.jMenuExpandRules.setEnabled(false);
            if ( (this.getJExtDataFile()!=null) || (this.Parent.kbct_Data!=null) ) {
                this.jMenuCumulatedLocalWeightRules.setEnabled(true);
                this.jMenuOrderByLocalWeightRules.setEnabled(true);
                this.jMenuCumulatedGlobalWeightRules.setEnabled(true);
                this.jMenuOrderByGlobalWeightRules.setEnabled(true);
                this.jMenuCumulatedWeightRules.setEnabled(true);
                this.jMenuOrderByWeightRules.setEnabled(true);
            }
            this.jMenuReverseOrder.setEnabled(true);
            this.jMenuOrderByOutputClass.setEnabled(true);
            this.jMenuLocalInterpretabilityRules.setEnabled(true);
            this.jMenuOrderByLocalInterpretabilityRules.setEnabled(true);
            this.jMenuGlobalInterpretabilityRules.setEnabled(true);
            this.jMenuOrderByGlobalInterpretabilityRules.setEnabled(true);
            this.jMenuInterpretabilityRules.setEnabled(true);
            this.jMenuOrderByInterpretabilityRules.setEnabled(true);
            this.jMenuOrderByNbPremisesRules.setEnabled(true);
            this.jMenuSelectRules.setEnabled(true);
            this.jMenuRemoveRedundantRules.setEnabled(true);
            this.jMenuMergeRules.setEnabled(true);
            this.jMenuFingramRules.setEnabled(true);
            this.jMenuExpandRules.setEnabled(true);
            int[] SelectedRules= this.jTableRules.getSelectedRows();
            int FirstSelectedRule= SelectedRules[0];
            int LastSelectedRule= SelectedRules[SelectedRules.length-1];
            if ( (LastSelectedRule - FirstSelectedRule + 1) == SelectedRules.length ) {
                // Consecutive rules
                if ( FirstSelectedRule == 0 )
                     this.jMenuUpRules.setEnabled(false);
                else
                     this.jMenuUpRules.setEnabled(true);

                if ( LastSelectedRule == this.Temp_kbct.GetNbRules() - 1 )
                     this.jMenuDownRules.setEnabled(false);
                else
                     this.jMenuDownRules.setEnabled(true);
            } else {
                this.jMenuUpRules.setEnabled(false);
                this.jMenuDownRules.setEnabled(false);
            }
        } else if ( this.jTableRules.getSelectedRowCount() == 1 ) {
            this.jMenuRemoveRedundantRules.setEnabled(true);
            this.jMenuReverseOrder.setEnabled(false);
            this.jMenuOrderByOutputClass.setEnabled(false);
            this.jMenuOrderByLocalWeightRules.setEnabled(false);
            this.jMenuOrderByGlobalWeightRules.setEnabled(false);
      	    if (this.Parent.kbct_Data!=null) {
                this.jMenuCumulatedLocalWeightRules.setEnabled(true);
                this.jMenuCumulatedGlobalWeightRules.setEnabled(true);
                this.jMenuCumulatedWeightRules.setEnabled(true);
    	    } else {
                this.jMenuCumulatedLocalWeightRules.setEnabled(false);
                this.jMenuCumulatedGlobalWeightRules.setEnabled(false);
                this.jMenuCumulatedWeightRules.setEnabled(false);
    	    }
            this.jMenuOrderByWeightRules.setEnabled(false);
            this.jMenuLocalInterpretabilityRules.setEnabled(true);
            this.jMenuOrderByLocalInterpretabilityRules.setEnabled(false);
            this.jMenuGlobalInterpretabilityRules.setEnabled(true);
            this.jMenuOrderByGlobalInterpretabilityRules.setEnabled(false);
            this.jMenuInterpretabilityRules.setEnabled(true);
            this.jMenuOrderByInterpretabilityRules.setEnabled(false);
            this.jMenuOrderByNbPremisesRules.setEnabled(false);
            this.jMenuSelectRules.setEnabled(false);
            this.jMenuFingramRules.setEnabled(false);
            this.jMenuMergeRules.setEnabled(false);
            this.jMenuExpandRules.setEnabled(true);
            if ( this.jTableRules.getSelectedRow() == 0 )
                 this.jMenuUpRules.setEnabled(false);
            else
                 this.jMenuUpRules.setEnabled(true);

            if ( this.jTableRules.getSelectedRow() == this.Temp_kbct.GetNbRules() - 1 )
                 this.jMenuDownRules.setEnabled(false);
            else
                 this.jMenuDownRules.setEnabled(true);
        }
    } else {
    	if ((this.Temp_kbct.GetNbInputs()>0)&&(this.Temp_kbct.GetNbOutputs()>0)) {
             this.jMenuNewRule.setEnabled(true);
             this.jMenuGenerateAllRules.setEnabled(true);
    	} else {
            this.jMenuNewRule.setEnabled(false);
            this.jMenuGenerateAllRules.setEnabled(false);
    	}	
        this.jMenuRemoveRule.setEnabled(false);
        this.jMenuRemoveRedundantRules.setEnabled(false);
        this.jMenuReverseOrder.setEnabled(false);
        this.jMenuOrderByOutputClass.setEnabled(false);
        this.jMenuCumulatedLocalWeightRules.setEnabled(false);
        this.jMenuOrderByLocalWeightRules.setEnabled(false);
        this.jMenuCumulatedGlobalWeightRules.setEnabled(false);
        this.jMenuOrderByGlobalWeightRules.setEnabled(false);
        this.jMenuCumulatedWeightRules.setEnabled(false);
        this.jMenuOrderByWeightRules.setEnabled(false);
        this.jMenuLocalInterpretabilityRules.setEnabled(false);
        this.jMenuOrderByLocalInterpretabilityRules.setEnabled(false);
        this.jMenuGlobalInterpretabilityRules.setEnabled(false);
        this.jMenuOrderByGlobalInterpretabilityRules.setEnabled(false);
        this.jMenuInterpretabilityRules.setEnabled(false);
        this.jMenuOrderByInterpretabilityRules.setEnabled(false);
        this.jMenuOrderByNbPremisesRules.setEnabled(false);
        this.jMenuSelectRules.setEnabled(false);
        this.jMenuCopyRules.setEnabled(false);
        this.jMenuUpRules.setEnabled(false);
        this.jMenuDownRules.setEnabled(false);
        this.jMenuExpandRules.setEnabled(false);
        this.jMenuMergeRules.setEnabled(false);
        this.jMenuFingramRules.setEnabled(false);
        this.jMenuActiveRules.setEnabled(false);
        this.jMenuInActiveRules.setEnabled(false);
        this.jMenuPrintRules.setEnabled(false);
        this.jMenuExportRules.setEnabled(false);
    }
  }
//------------------------------------------------------------------------------
  void jMenuOutputs_menuSelected() {
    if( this.Temp_kbct == null ) {
        this.jMenuNewOutput.setEnabled(false);
        this.jMenuEditOutput.setEnabled(false);
        this.jMenuRemoveOutput.setEnabled(false);
        this.jMenuCopyOutput.setEnabled(false);
        this.jMenuUpOutput.setEnabled(false);
        this.jMenuDownOutput.setEnabled(false);
    } else {
        this.jMenuNewOutput.setEnabled(true);
        if( this.jListOutputs.getSelectedIndices().length == 0 ) {
           this.jMenuEditOutput.setEnabled(false);
           this.jMenuRemoveOutput.setEnabled(false);
           this.jMenuCopyOutput.setEnabled(false);
           this.jMenuUpOutput.setEnabled(false);
           this.jMenuDownOutput.setEnabled(false);
        } else {
           this.jMenuEditOutput.setEnabled(true);
           this.jMenuRemoveOutput.setEnabled(true);
           this.jMenuCopyOutput.setEnabled(true);
           int[] SelectedOutputs= this.jListOutputs.getSelectedIndices();
           if ( SelectedOutputs.length > 1 ) {
               int FirstSelectedOutput= SelectedOutputs[0];
               int LastSelectedOutput= SelectedOutputs[SelectedOutputs.length-1];
               if ( (LastSelectedOutput - FirstSelectedOutput + 1) == SelectedOutputs.length ) {
                 // Consecutive inputs
                   if ( FirstSelectedOutput == 0 )
                        this.jMenuUpOutput.setEnabled(false);
                   else
                        this.jMenuUpOutput.setEnabled(true);

                   if ( LastSelectedOutput == this.Temp_kbct.GetNbOutputs() - 1 )
                        this.jMenuDownOutput.setEnabled(false);
                   else
                        this.jMenuDownOutput.setEnabled(true);
               } else {
                   this.jMenuUpOutput.setEnabled(false);
                   this.jMenuDownOutput.setEnabled(false);
               }
           } else {
               if ( this.jListOutputs.getSelectedIndex() == 0 )
                    this.jMenuUpOutput.setEnabled(false);
               else
                    this.jMenuUpOutput.setEnabled(true);

               if ( this.jListOutputs.getSelectedIndex() == this.Temp_kbct.GetNbOutputs() - 1 )
                    this.jMenuDownOutput.setEnabled(false);
               else
                    this.jMenuDownOutput.setEnabled(true);
           }
        }
    }
  }
//------------------------------------------------------------------------------
  void jListInputs_keyPressed(KeyEvent e) {
    if( this.jListInputs.isEnabled() == false )
      return;

    if( this.jListInputs.getSelectedIndices().length > 0 ) {
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
                if (e.getKeyCode()==KeyEvent.VK_DELETE)
                    this.RemoveInput();
                else if (e.getKeyCode()==KeyEvent.VK_E)
                    this.EditInput();
                else if (e.getKeyCode()==KeyEvent.VK_N)
                    this.NewInput();
                else if (e.getKeyCode()==KeyEvent.VK_C)
                    this.CopyInput();
                else if (e.getKeyCode()==KeyEvent.VK_U)
                    this.UpInput();
                else if (e.getKeyCode()==KeyEvent.VK_D)
                    this.DownInput();

                this.firstKey= null;
    	    } else {
        		this.firstKeyPressed= tt;
    	    }
    	}
    }
  }
//------------------------------------------------------------------------------
  void jListOutputs_keyPressed(KeyEvent e) {
    if( this.jListOutputs.isEnabled() == false )
      return;

    if( this.jListOutputs.getSelectedIndices().length > 0 ) {
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
                if (e.getKeyCode()==KeyEvent.VK_DELETE)
                    this.RemoveOutput();
                else if (e.getKeyCode()==KeyEvent.VK_E)
                    this.EditOutput();
                else if (e.getKeyCode()==KeyEvent.VK_N)
                    this.NewOutput();
                else if (e.getKeyCode()==KeyEvent.VK_C)
                    this.CopyOutput();
                else if (e.getKeyCode()==KeyEvent.VK_U)
                    this.UpOutput();
                else if (e.getKeyCode()==KeyEvent.VK_D)
                    this.DownOutput();

                this.firstKey= null;
    	    } else {
        		this.firstKeyPressed= tt;
    	    }
    	}
    }
  }
//------------------------------------------------------------------------------
  void jListInputs_mousePressed(MouseEvent e) {
    if( this.jListInputs.isEnabled() == false )
      return;

    if (SwingUtilities.isRightMouseButton(e)) {
      if( this.jListInputs.getSelectedIndices().length == 0 ) {
          this.jPopupEditInput.setEnabled(false);
          this.jPopupRemoveInput.setEnabled(false);
          this.jPopupCopyInput.setEnabled(false);
          this.jPopupUpInput.setEnabled(false);
          this.jPopupDownInput.setEnabled(false);
      } else {
          this.jPopupEditInput.setEnabled(true);
          this.jPopupRemoveInput.setEnabled(true);
          this.jPopupCopyInput.setEnabled(true);
          int[] SelectedInputs= this.jListInputs.getSelectedIndices();
          if ( SelectedInputs.length > 1 ) {
              int FirstSelectedInput= SelectedInputs[0];
              int LastSelectedInput= SelectedInputs[SelectedInputs.length-1];
              if ( (LastSelectedInput - FirstSelectedInput + 1) == SelectedInputs.length ) {
                // Consecutive inputs
                  if ( FirstSelectedInput == 0 )
                       this.jPopupUpInput.setEnabled(false);
                  else
                       this.jPopupUpInput.setEnabled(true);

                  if ( LastSelectedInput == this.Temp_kbct.GetNbInputs() - 1 )
                       this.jPopupDownInput.setEnabled(false);
                  else
                       this.jPopupDownInput.setEnabled(true);
              } else {
                  this.jPopupUpInput.setEnabled(false);
                  this.jPopupDownInput.setEnabled(false);
              }
          } else {
              if ( this.jListInputs.getSelectedIndex() == 0 )
                   this.jPopupUpInput.setEnabled(false);
              else
                   this.jPopupUpInput.setEnabled(true);

              if ( this.jListInputs.getSelectedIndex() == this.Temp_kbct.GetNbInputs() - 1 )
                   this.jPopupDownInput.setEnabled(false);
              else
                   this.jPopupDownInput.setEnabled(true);
          }
        }
      SwingUtilities.updateComponentTreeUI(this.jPopupMenuInputs);
      jPopupMenuInputs.show(this.jListInputs, e.getX(), e.getY());
      }
    // double click
    if( SwingUtilities.isLeftMouseButton(e) && (e.getClickCount() == 2) && (jListInputs.isSelectionEmpty() == false) )
      this.EditInput();
  }
//------------------------------------------------------------------------------
  void jListOutputs_mousePressed(MouseEvent e) {
    if( this.jListOutputs.isEnabled() == false )
      return;

    if (SwingUtilities.isRightMouseButton(e)) {
      if( this.jListOutputs.getSelectedIndices().length == 0 ) {
          this.jPopupEditOutput.setEnabled(false);
          this.jPopupRemoveOutput.setEnabled(false);
          this.jPopupCopyOutput.setEnabled(false);
          this.jPopupUpOutput.setEnabled(false);
          this.jPopupDownOutput.setEnabled(false);
      } else {
          this.jPopupEditOutput.setEnabled(true);
          this.jPopupRemoveOutput.setEnabled(true);
          this.jPopupCopyOutput.setEnabled(true);
          int[] SelectedOutputs= this.jListOutputs.getSelectedIndices();
          if ( SelectedOutputs.length > 1 ) {
             int FirstSelectedOutput= SelectedOutputs[0];
             int LastSelectedOutput= SelectedOutputs[SelectedOutputs.length-1];
             if ( (LastSelectedOutput - FirstSelectedOutput + 1) == SelectedOutputs.length ) {
                // Consecutive outputs
                if ( FirstSelectedOutput == 0 )
                     this.jPopupUpOutput.setEnabled(false);
                else
                     this.jPopupUpOutput.setEnabled(true);

                if ( LastSelectedOutput == this.Temp_kbct.GetNbOutputs() - 1 )
                     this.jPopupDownOutput.setEnabled(false);
                else
                     this.jPopupDownOutput.setEnabled(true);
             } else {
                 this.jPopupUpOutput.setEnabled(false);
                 this.jPopupDownOutput.setEnabled(false);
             }
          } else {
              if ( this.jListOutputs.getSelectedIndex() == 0 )
                   this.jPopupUpOutput.setEnabled(false);
              else
                   this.jPopupUpOutput.setEnabled(true);

              if ( this.jListOutputs.getSelectedIndex() == this.Temp_kbct.GetNbOutputs() - 1 )
                   this.jPopupDownOutput.setEnabled(false);
              else
                   this.jPopupDownOutput.setEnabled(true);
          }
        }
      SwingUtilities.updateComponentTreeUI(this.jPopupMenuOutputs);
      jPopupMenuOutputs.show(this.jListOutputs, e.getX(), e.getY());
      }
    // double click
    if( SwingUtilities.isLeftMouseButton(e) && (e.getClickCount() == 2) && (jListOutputs.isSelectionEmpty() == false) )
      this.EditOutput();
  }
//------------------------------------------------------------------------------
  void this_windowStateChanged() {
    if (!clossing) {
      if (this.Temp_kbct.GetNbActiveRules()>0) {
          this.EnableAllButtons();
      } else {
          this.DisableAllButtons();
      }
      if ((this.Temp_kbct!=null)&&(this.Temp_kbct.GetNbInputs()>0)&&(this.Temp_kbct.GetNbOutputs()>0))
         this.ReInitTableRules();
    } else { 
    	this.clossing= false;
    }	
  }
//------------------------------------------------------------------------------
  void jMenuNewInput_actionPerformed() { this.NewInput(); }
//------------------------------------------------------------------------------
  void jMenuEditInput_actionPerformed() { this.EditInput(); }
//------------------------------------------------------------------------------
  void jMenuRemoveInput_actionPerformed() { this.RemoveInput(); }
//------------------------------------------------------------------------------
  void jMenuCopyInput_actionPerformed() { this.CopyInput(); }
//------------------------------------------------------------------------------
  void jMenuUpInput_actionPerformed() { this.UpInput(); }
//------------------------------------------------------------------------------
  void jMenuDownInput_actionPerformed() { this.DownInput(); }
//------------------------------------------------------------------------------
  void jMenuNewOutput_actionPerformed() { this.NewOutput(); }
//------------------------------------------------------------------------------
  void jMenuEditOutput_actionPerformed() { this.EditOutput(); }
//------------------------------------------------------------------------------
  void jMenuRemoveOutput_actionPerformed() { this.RemoveOutput(); }
//------------------------------------------------------------------------------
  void jMenuCopyOutput_actionPerformed() { this.CopyOutput(); }
//------------------------------------------------------------------------------
  void jMenuUpOutput_actionPerformed() { this.UpOutput(); }
//------------------------------------------------------------------------------
  void jMenuDownOutput_actionPerformed() { this.DownOutput(); }
//------------------------------------------------------------------------------
  void jMenuNewRule_actionPerformed() { this.NewRule(); }
//------------------------------------------------------------------------------
  void jMenuRemoveRule_actionPerformed() { this.RemoveRule(); }
//------------------------------------------------------------------------------
  void jMenuRemoveRedundantRules_actionPerformed() { this.RemoveRedundantRules(); }
//------------------------------------------------------------------------------
  void jMenuReverseOrder_actionPerformed(boolean automatic) { this.ReverseOrder(automatic); }
//------------------------------------------------------------------------------
  void jMenuOrderByOutputClass_actionPerformed(boolean automatic) { this.OrderByOutputClass(automatic); }
//------------------------------------------------------------------------------
  void jMenuCumulatedLocalWeightRules_actionPerformed() { this.CumulatedWeightRules("Local", false); }
//------------------------------------------------------------------------------
  void jMenuOrderByLocalWeightRules_actionPerformed(boolean automatic) { this.OrderByWeightRules("Local", automatic); }
//------------------------------------------------------------------------------
  void jMenuCumulatedGlobalWeightRules_actionPerformed() { this.CumulatedWeightRules("Global", false); }
//------------------------------------------------------------------------------
  void jMenuOrderByGlobalWeightRules_actionPerformed(boolean automatic) { this.OrderByWeightRules("Global", automatic); }
//------------------------------------------------------------------------------
  void jMenuCumulatedWeightRules_actionPerformed() { this.CumulatedWeightRules("L+G", false); }
//------------------------------------------------------------------------------
  void jMenuOrderByWeightRules_actionPerformed(boolean automatic) { this.OrderByWeightRules("L+G", automatic); }
//------------------------------------------------------------------------------
  void jMenuLocalInterpretabilityRules_actionPerformed() { this.CumulatedIntWeightRules("Local", false); }
//------------------------------------------------------------------------------
  void jMenuOrderByLocalInterpretabilityRules_actionPerformed(boolean automatic) { this.OrderByInterpretabilityRules("Local", automatic); }
//------------------------------------------------------------------------------
  void jMenuGlobalInterpretabilityRules_actionPerformed() { this.CumulatedIntWeightRules("Global", false); }
//------------------------------------------------------------------------------
  void jMenuOrderByGlobalInterpretabilityRules_actionPerformed(boolean automatic) { this.OrderByInterpretabilityRules("Global", automatic); }
//------------------------------------------------------------------------------
  void jMenuInterpretabilityRules_actionPerformed() { this.CumulatedIntWeightRules("L+G", false); }
//------------------------------------------------------------------------------
  void jMenuOrderByInterpretabilityRules_actionPerformed(boolean automatic) { this.OrderByInterpretabilityRules("L+G", automatic); }
//------------------------------------------------------------------------------
  void jMenuOrderByNbPremisesRules_actionPerformed(boolean automatic) { this.OrderByNbPremisesRules(automatic); }
//------------------------------------------------------------------------------
  void jMenuSelectRules_actionPerformed() { this.RuleSelection(); }
//------------------------------------------------------------------------------
  void jMenuCopyRules_actionPerformed() { this.CopyRules(); }
//------------------------------------------------------------------------------
  void jMenuUpRules_actionPerformed() { this.UpRules(); }
//------------------------------------------------------------------------------
  void jMenuDownRules_actionPerformed() { this.DownRules(); }
//------------------------------------------------------------------------------
  void jMenuExpandRules_actionPerformed() { this.ExpandRules(); }
//------------------------------------------------------------------------------
  void jMenuMergeRules_actionPerformed() { this.MergeRules(); }
//------------------------------------------------------------------------------
  void jMenuActiveRules_actionPerformed() { this.ActiveRules(); }
//------------------------------------------------------------------------------
  void jMenuInActiveRules_actionPerformed() { this.DeActiveRules(); }
//------------------------------------------------------------------------------
  void jMenuGenerateAllRules_actionPerformed() { this.GenerateAllRules(); }
//------------------------------------------------------------------------------
  //Methods for building and manipulating rules table
//------------------------------------------------------------------------------
  private void GenerateAllRules() {
    try {
      File temp1= JKBCTFrame.BuildFile("tempGARfile.kb.xml");
      File temp2= JKBCTFrame.BuildFile("tempGARfile.fis");
      File temp3= JKBCTFrame.BuildFile("tempGARfile");
      JKBCT kbct_fis= new JKBCT(this.Temp_kbct);
      kbct_fis.Save(temp1.getPath(), false);
      kbct_fis.SaveFIS(temp2.getPath());
      JFIS jf = new JFIS(temp2.getPath());
      jf.SetName(temp3.getName());
      //fis.jnifis.FisproPath(MainKBCT.getConfig().GetKbctPath());
      fis.jnifis.setUserHomeFisproPath(MainKBCT.getConfig().GetKbctPath());
      JGENFIS jgf = new JGENFIS(jf, null, false, 0);
      this.TranslateRules_FIS2KBCT(jgf, this.Temp_kbct);
      this.Temp_kbct.Save();
      jf.Delete();
      temp1.delete();
      temp2.delete();
      File f_rules= new File(temp3.getPath()+".rules");
      f_rules.delete();
      temp3.delete();
      this.InitJExpertFrameWithKBCT();
      this.ReInitTableRules();
    } catch (Throwable t) {
        t.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: GenerateAllRules(): "+t);
    }
  }
//------------------------------------------------------------------------------
  private void NewRule() {
    try {
      int rule_number= this.Temp_kbct.GetNbRules();
      int input_number= this.Temp_kbct.GetNbInputs();
      int output_number= this.Temp_kbct.GetNbOutputs();
      int[] in_labels= new int[input_number];
      for (int n=0; n<input_number; n++)
        in_labels[n]= 0;

      int[] out_labels= new int[output_number];
      for (int n=0; n<output_number; n++)
        out_labels[n]= 0;

      this.Temp_kbct.AddRule(new Rule(rule_number, input_number, output_number, in_labels, out_labels, "E", true));
      if (!this.jButtonConsistency.isEnabled()) {
        this.jButtonConsistency.setEnabled(true);
        this.jButtonCompleteness.setEnabled(true);
        this.jButtonInfer.setEnabled(true);
        this.jButtonSummary.setEnabled(true);
        this.jButtonFingrams.setEnabled(true);
      }
      if ( (!this.jButtonSimplify.isEnabled()) && (!this.Parent.KBDataFile.equals("")) )
        this.jButtonSimplify.setEnabled(true);

      if ( (!this.jButtonLogView.isEnabled()) && (!this.Parent.KBDataFile.equals("")) )
          this.jButtonLogView.setEnabled(true);

      if ( (!this.jButtonOptimization.isEnabled()) && (!this.Parent.KBDataFile.equals("")) )
          this.jButtonOptimization.setEnabled(true);

      if (!this.jButtonQuality.isEnabled())
        this.jButtonQuality.setEnabled(true);

      this.cursor=rule_number;
      this.ReInitTableRules();
      this.cursor=0;
      if (!MainKBCT.getConfig().GetTESTautomatic())
        this.repaint();
   } catch( Throwable en ) {
      en.printStackTrace();
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: NewRule(): "+en);
   }
  }
//------------------------------------------------------------------------------
  private void RemoveRule() {
    int[] SelectedRules= this.jTableRules.getSelectedRows();
    if (SelectedRules.length==0) {
        String message= LocaleKBCT.GetString("YouMustSelectRulesToRemove");
        MessageKBCT.Information(this, message);
    } else {
      String message= LocaleKBCT.GetString("DoYouWantToRemoveAllSelectedRules");
      int option= MessageKBCT.Confirm(this, message, 0, false, false, false);
      if (option==0) {
        for (int i=0; i<SelectedRules.length; i++)
          this.Temp_kbct.RemoveRule(SelectedRules[i]-i);

        if (this.Temp_kbct.GetNbActiveRules()==0) {
            this.DisableAllButtons();
        }
        this.ReInitTableRules();
    	if (!MainKBCT.getConfig().GetTESTautomatic())
          this.repaint();
      }
    }
  }
//------------------------------------------------------------------------------
  public void RemoveRule(int RuleNum) {
    this.Temp_kbct.RemoveRule(RuleNum-1);
    if (this.Temp_kbct.GetNbRules()==0) {
        this.DisableAllButtons();
    }
    this.ReInitTableRules();
	if (!MainKBCT.getConfig().GetTESTautomatic())
      this.repaint();
  }
//------------------------------------------------------------------------------
  private void RemoveRedundantRules() {
        //System.out.println("Remove Redundant Rules");
	    int option=0;
		//if (!automatic)
	 	    option= MessageKBCT.Confirm(this, LocaleKBCT.GetString("DoYouWantToMakeSolveRedundancy"));

	 	if (option==0) {
	        // Solving conflicts
	 	    File fname= new File(JKBCTFrame.KBExpertFile);
	 	    String name=fname.getName();
	 	    String LogName="LogConsistency_"+name.substring(0,name.length()-7);
	 	    String ConsistencyLogFile= (JKBCTFrame.BuildFile(LogName)).getAbsolutePath();
	 	    MessageKBCT.BuildLogFile(ConsistencyLogFile+".txt", this.Parent.IKBFile, JKBCTFrame.KBExpertFile, this.Parent.KBDataFile, "Consistency");
	        boolean warningSC= false;
	        MessageKBCT.WriteLogFile("----------------------------------", "Consistency");
	        MessageKBCT.WriteLogFile((LocaleKBCT.GetString("SolvingLinguisticConflicts")).toUpperCase(), "Consistency");
	        boolean endSC= false;
	        if (!MainKBCT.getConfig().GetTESTautomatic())
	             MessageKBCT.Information(this, LocaleKBCT.GetString("Processing")+" ..."+"\n"+
	                                           "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
	                                           "... "+LocaleKBCT.GetString("PleaseWait")+" ...");

	        int iter= 1;
	        while (!endSC) {
	 	       long TM=Runtime.getRuntime().totalMemory();
		       long MM=Runtime.getRuntime().maxMemory();
		       if (TM>=MM) {
	               if (!MainKBCT.getConfig().GetTESTautomatic()) {
		    	     String message= LocaleKBCT.GetString("WarningConsistencySolvingConflictsHalted")+"\n"+
	                 LocaleKBCT.GetString("WarningJVMOutOfMemory")+"\n"+
	                 LocaleKBCT.GetString("WarningReleaseMemory")+"\n"+
	                 LocaleKBCT.GetString("WarningConsistencySolvingConflictsOutOfMemory");
		             if (!MainKBCT.flagHalt) {
		    	       MessageKBCT.Information(null, message);
		             }
	               }
	               if (!MainKBCT.flagHalt) {
	                   MessageKBCT.WriteLogFile(LocaleKBCT.GetString("WarningConsistencySolvingConflictsHalted"), "Consistency");
	                   MainKBCT.flagHalt= true;
	               }
	               break;
		       } else {
	        	   MessageKBCT.WriteLogFile(LocaleKBCT.GetString("Iteration")+" "+iter, "Consistency");
	               Date d= new Date(System.currentTimeMillis());
	               MessageKBCT.WriteLogFile("  "+"time begin -> "+DateFormat.getTimeInstance().format(d), "Consistency");
	               iter++;
	               boolean[] SC= this.SolveRedundancyRuleBase(this.getJExtDataFile());
	               endSC= SC[0];
	               if (SC[1])
	                 warningSC= true;
	               if (SC[2])
	                 MessageKBCT.RemoveLastIteration();
	               d= new Date(System.currentTimeMillis());
	               MessageKBCT.WriteLogFile("  "+"time end -> "+DateFormat.getTimeInstance().format(d), "Consistency");
	               if ( (!MainKBCT.getConfig().GetTESTautomatic()) && (iter > 100) )
	            	  endSC=true;
	          }
	        }
	        if (!warningSC) {
	            MessageKBCT.WriteLogFile(LocaleKBCT.GetString("NoConflictsSolution"), "Consistency");
	        	if (!MainKBCT.getConfig().GetTESTautomatic())
	        		MessageKBCT.Information(this, LocaleKBCT.GetString("NoConflictsSolution"));
	        }
	        MessageKBCT.WriteLogFile("----------------------------------", "Consistency");
	        MessageKBCT.WriteLogFile(LocaleKBCT.GetString("AutomaticConsistencyEnded"), "Consistency");
	        Date d= new Date(System.currentTimeMillis());
	        MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "Consistency");
	        MessageKBCT.CloseLogFile("Consistency");
	    	if ( (!MainKBCT.getConfig().GetTESTautomatic()) && (warningSC) ) {
	            try {
	            	this.jfcLSC= new JFISConsole(this, ConsistencyLogFile+".txt", false);
	            } catch (Throwable t) {
	                t.printStackTrace();
	            	MessageKBCT.Error(null, t);
	            }
	        }
	    } else {
	   	    if (JExpertFrame.jcf != null) {
	            jcf.dispose();
	            JKBCTFrame.RemoveTranslatable(JExpertFrame.jcf);
	            JExpertFrame.jcf= null;
	        }
	        //JConsistency jc= new JConsistency(this, this.Temp_kbct);
	        JConsistency jc= new JConsistency(this.Temp_kbct);
	        jc.AnalysisOfConsistency(false);
	        JExpertFrame.jcf= new JConsistencyFrame(this, this.Temp_kbct, jc, true, null);
	        if (jc.getWARNING())
	    	    JExpertFrame.jcf.setVisible(true);
	        else
	            MessageKBCT.Information(this, LocaleKBCT.GetString("ConsistencyCheckingGood"));
	    }
  }
//------------------------------------------------------------------------------
  private void MergeRules() {
    try {
      int OldNbRules= this.Temp_kbct.GetNbRules();
      int[] SelectedRules= this.jTableRules.getSelectedRows();
      if (SelectedRules.length > 1) {
        //JConsistency jc= new JConsistency(this, this.Temp_kbct);
        JConsistency jc= new JConsistency(this.Temp_kbct);
        boolean end= false;
        int NbSelRules= SelectedRules.length;
        Rule rPrev=null;
        for (int i=0; i<NbSelRules; i++) {
           int SelRule= SelectedRules[i];
           //System.out.println("SelRule: "+SelRule);
           Rule rSel= this.Temp_kbct.GetRule(SelRule+1);
           if (!rSel.GetActive()) {
             MessageKBCT.Information(this, LocaleKBCT.GetString("MsgMergeAllActiveRules"));
             end= true;
             break;
           }
           if (i>0) {
             if (!rSel.GetType().equals(rPrev.GetType())) {
               MessageKBCT.Information(this, LocaleKBCT.GetString("MsgMergeAllRulesSametype"));
               end= true;
               break;
             }
             if (JConsistency.DifferentConclusions(rSel, rPrev)) {
               MessageKBCT.Information(this, LocaleKBCT.GetString("MsgMergeAllRulesSameConclusion"));
               end= true;
               break;
             }
           }
           rPrev= rSel;
        }
        if (!end) {
          this.jsp= new JSolveProblem(this.Temp_kbct, this, null);
          int NbOldRules= this.Temp_kbct.GetNbRules();
          int L0= SelectedRules[0];
          int L1= SelectedRules.length;
          int remRules= NbOldRules - L0 - L1;
          while (!end) {
            int[] SelRules= new int[L1];
            for (int n=0; n<SelRules.length; n++) {
                SelRules[n]= SelectedRules[n];
                //SelRules[n]= L0 + n;
            }
            double[][] qold= jc.KBquality(this.Temp_kbct, null, 0, 0, this.getJExtDataFile());
            this.Temp_kbct= jc.MergeRules(this.Temp_kbct, SelRules, this.getJExtDataFile(), qold, null);
            this.Temp_kbct.AddKBCTListener(this.KBCTListener);
            int NbNewRules= this.Temp_kbct.GetNbRules();
            if (NbNewRules == NbOldRules )
              end= true;
            else {
              NbOldRules= NbNewRules;
              L1= NbNewRules - L0 - remRules;
              if (L1==1)
                end= true;
            }
          }
          if (this.Temp_kbct.GetNbRules() != OldNbRules) {
            this.ReInitTableRules();
          } else {
            MessageKBCT.Information(this, LocaleKBCT.GetString("MsgMergeNoMergeRules"));
          }
        }
      }
    } catch (Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);}
  }
//------------------------------------------------------------------------------
  private void ExpandRules() {
    try {
      int OldNbRules= this.Temp_kbct.GetNbRules();
      int[] SelectedRules= this.jTableRules.getSelectedRows();
      if (SelectedRules.length == 1) {
        File temp= JKBCTFrame.BuildFile("tempExpRules1.kb.xml");
        JKBCT kbctaux= new JKBCT(this.Temp_kbct);
        kbctaux.SetKBCTFile(temp.getAbsolutePath());
        kbctaux.Save();
        //JConsistency jc= new JConsistency(this, kbctaux);
        JConsistency jc= new JConsistency(kbctaux);
        Rule[] rexp= jc.expandRule(kbctaux, SelectedRules[0]+1);
        for (int m=0; m<rexp.length; m++) {
          this.Temp_kbct.AddRule(rexp[m]);
        }
        if (OldNbRules < this.Temp_kbct.GetNbRules())
          this.ReInitTableRules();
      } else {
          File temp= JKBCTFrame.BuildFile("tempExpRulesN.kb.xml");
          for (int n=0; n<SelectedRules.length; n++) {
               JKBCT kbctaux= new JKBCT(this.Temp_kbct);
               kbctaux.SetKBCTFile(temp.getAbsolutePath());
               kbctaux.Save();
               JConsistency jc= new JConsistency(kbctaux);
               Rule[] rexp= jc.expandRule(kbctaux, SelectedRules[n]+1);
               for (int m=0; m<rexp.length; m++) {
                    this.Temp_kbct.AddRule(rexp[m]);
               }
          }
          if (OldNbRules < this.Temp_kbct.GetNbRules())
            this.ReInitTableRules();
      }
    } catch (Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);
    }
  }
//------------------------------------------------------------------------------
  private void ActiveRules() {
    this.ChangeActiveFlagInSelectedRules(true);
  }
//------------------------------------------------------------------------------
  private void DeActiveRules() {
    this.ChangeActiveFlagInSelectedRules(false);
  }
//------------------------------------------------------------------------------
  private void ChangeActiveFlagInSelectedRules(boolean active) {
    int[] SelectedRules= this.jTableRules.getSelectedRows();
    for (int i=0; i<SelectedRules.length; i++) {
    	 this.Temp_kbct.SetRuleActive(SelectedRules[i]+1, active);
    }
    //System.out.println("ActiveRules="+this.Temp_kbct.GetNbActiveRules());
    try { this.InitJExpertFrameWithKBCT(); }
    catch (Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);}
  }
//------------------------------------------------------------------------------
  public void ReInitTableRules() {
    try { jbInitTableRules(); }
    catch( Throwable t ) {
      t.printStackTrace();
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JExpertFrame: ReInitTableRule(): "+t);
    }
  }
//------------------------------------------------------------------------------
  private void SetUpInitColumns(int column) throws Throwable {
    jTableRules.getColumnModel().getColumn(column).setCellRenderer( new DefaultTableCellRenderer() {
    static final long serialVersionUID=0;	
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      this.setHorizontalAlignment(SwingConstants.CENTER);
      if (column==2) {
        Object data = RuleModel.getValueAt(row, column);
        if (((Boolean)data).booleanValue()) {
          super.setText(LocaleKBCT.GetString("yes"));
        } else {
          //System.out.println("SetUpInitColumns: NO");
          super.setText(LocaleKBCT.GetString("no"));
        }
      }
      Rule r= Temp_kbct.GetRule(row+1);
      if (r!=null) {
          String RuleType= r.GetType();
          boolean RuleActive= r.GetActive();
          if(!RuleActive) {
             super.setForeground(Color.gray);
             super.setBackground(Color.yellow);
             return this;
          }
          if (RuleType.equals("I"))
              super.setForeground(Color.red);
          else if (RuleType.equals("E"))
              super.setForeground(Color.black);
          else if ( (RuleType.equals("S")) || (RuleType.equals("P")) )
              super.setForeground(Color.blue);

          super.setBackground(Color.green);
      }
      return this;
      }
    });
  }
//------------------------------------------------------------------------------
  private void SetUpColumn( TableColumn Column, final JVariable input ) {
    final JComboBox comboBox = new JComboBox();
    comboBox.addItem(new Integer(0));
    int NOL= input.GetLabelsNumber();
    int lim= 0;
    if (input.isOutput())
      lim= NOL;
    else if (NOL==3)
      lim= 2*NOL;
    else
      lim= 2*NOL + jnikbct.serie(NOL-1)-3;

    for( int i=0 ; i< lim ; i++ )
      comboBox.addItem(new Integer(i + 1));

    // renderer de la combo box
    comboBox.setRenderer(new BasicComboBoxRenderer() {
    static final long serialVersionUID=0;	
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      // centrage des elements de la combo box
      super.setHorizontalAlignment(SwingConstants.LEFT);
      boolean warning= false;
      if ( (input.GetScaleName().equals("user")) && 
          	   (input.GetInputInterestRange()[0]!=1) &&
       	   (input.GetInputInterestRange()[1]!=input.GetLabelsNumber()) &&
       	   (input.isOutput()) &&
       	   ( (input.GetType().equals("logical")) || (input.GetType().equals("categorical")) ) ) {
               warning= true;
      } 
      if (warning) {
		  String[] labNames= input.GetUserLabelsName();
		  boolean warn= false;
		  for (int n=0; n<labNames.length; n++) {
			   if (labNames[n].equals(""+index+".0")) {
                   this.setText(labNames[n]);
                   warn= true;
				   break;   
			   }
		  }
          //System.out.println("warn1="+warn);
		  if (!warn) {
              super.setText(input.GetUserLabelsName(index-1));
		  }
      }
      else if( index==0 ) {
        super.setText(new String());
        return this;
      } else {
        if( index == -1 ) {
          return this.getListCellRendererComponent(list, value, comboBox.getSelectedIndex(), isSelected, cellHasFocus);
        }
          int NbLabels= input.GetLabelsNumber();
          if (!input.GetScaleName().equals("user")) {
              if (index>NbLabels) {
                  if (index > 2*NbLabels) {
                      super.setForeground(Color.red);
                      super.setText(input.GetORLabelsName(index-1-2*NbLabels));
                  } else {
                      super.setForeground(Color.blue);
                      super.setText(LocaleKBCT.GetString("NOT")+"("+LocaleKBCT.GetString(input.GetLabelsName(index-1-NbLabels))+")");
                  }
              } else
                  super.setText(LocaleKBCT.GetString(input.GetLabelsName(index-1)));
          } else {
            if (index>NbLabels) {
                if (index > 2*NbLabels) {
                    super.setForeground(Color.red);
                    //if (index==59) {
                    //    System.out.println("name="+input.GetORLabelsName(index-1-2*NbLabels));
                    //}
                    super.setText(input.GetORLabelsName(index-1-2*NbLabels));
                } else {
                    super.setForeground(Color.blue);
                    super.setText(LocaleKBCT.GetString("NOT")+"("+input.GetUserLabelsName(index-1-NbLabels)+")");
                }
            } else
                super.setText(input.GetUserLabelsName(index-1));
          }
        }
      super.setToolTipText(super.getText());
      return this;
      }
    });
    Column.setCellEditor(new DefaultCellEditor(comboBox) {
    static final long serialVersionUID=0;	
    public Object getCellEditorValue() { return new Integer(comboBox.getSelectedIndex()); }
    });
    Column.setCellRenderer(new DefaultTableCellRenderer() {
    static final long serialVersionUID=0;	
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      boolean warning= false;
      if ( (input.GetScaleName().equals("user")) && 
           (input.GetInputInterestRange()[0]!=1) &&
       	   (input.GetInputInterestRange()[1]!=input.GetLabelsNumber()) &&
       	   (input.isOutput()) &&
       	   ( (input.GetType().equals("logical")) || (input.GetType().equals("categorical")) ) ) {
               warning= true;
      } 
      //System.out.println("warning="+warning);
      if( !warning && ((Integer)value).intValue() == 0 )
          this.setText("");
      else {
        try {
          int NbLabels= input.GetLabelsNumber();
          if (!input.GetScaleName().equals("user")) {
              if (((Integer)value).intValue()>NbLabels) {
                  if (((Integer)value).intValue() > 2*NbLabels) {
                	  if (NbLabels > 2)
                          super.setText(input.GetORLabelsName(((Integer)value).intValue()-1-2*NbLabels));
                	  else
                          super.setText("");
                  } else
                    this.setText(LocaleKBCT.GetString("NOT")+"("+LocaleKBCT.GetString(input.GetLabelsName(((Integer)value).intValue()-1-NbLabels))+")");
              } else
                  this.setText(LocaleKBCT.GetString(input.GetLabelsName(((Integer)value).intValue()-1)));
          } else {
        	  if (warning) {
        		  String[] labNames= input.GetUserLabelsName();
        		  boolean warn= false;
        		  for (int n=0; n<labNames.length; n++) {
        			   if (labNames[n].equals(value.toString()+".0")) {
                           this.setText(labNames[n]);
                           warn= true;
        				   break;   
        			   }
        		  }
                  //System.out.println("warn2="+warn);
        		  if (!warn) {
                      //System.out.println("value="+value);
                      //System.out.println("text="+String.valueOf(((Integer)value).intValue()-1));
                      this.setText(input.GetUserLabelsName(((Integer)value).intValue()-1));
        		  }
              } else if (((Integer)value).intValue()>NbLabels) {
                  if (((Integer)value).intValue() > 2*NbLabels)
                    super.setText(input.GetORLabelsName(((Integer)value).intValue()-1-2*NbLabels));
                  else
                    this.setText(LocaleKBCT.GetString("NOT")+"("+input.GetUserLabelsName(((Integer)value).intValue()-1-NbLabels)+")");
              } else
                  this.setText(input.GetUserLabelsName(((Integer)value).intValue()-1));
          }
        } catch (Throwable t ) {
            t.printStackTrace();
            this.setText("");
        }
      }
      this.setHorizontalAlignment(SwingConstants.CENTER);
      Rule r= Temp_kbct.GetRule(row+1);
      if (r!=null) {
          boolean RuleActive= r.GetActive();
          String RuleType= r.GetType();
          if(!RuleActive) {
             super.setForeground(Color.gray);
             super.setBackground(Color.yellow);
             return this;
          }
          if (RuleType.equals("I"))
              super.setForeground(Color.red);
          else if (RuleType.equals("E"))
              super.setForeground(Color.black);
          else if ( (RuleType.equals("S")) || (RuleType.equals("P")) )
              super.setForeground(Color.blue);
          if (super.getBackground().equals(Color.yellow))
              super.setBackground(Color.white);
      }
      return this;
      }
    });
  }
//------------------------------------------------------------------------------
  private int CellWidth( TableColumnModel columns, int column ) {
    if( this.NbRule == 0 )
      return 0;

    try { return columns.getColumn(column).getCellEditor().getTableCellEditorComponent(this.jTableRules, this.jTableRules.getModel().getValueAt(0, column), false, 0, column).getPreferredSize().width; }
    catch (NullPointerException e) {
    	e.printStackTrace();
    	return jTableRules.getDefaultEditor(columns.getClass()).getTableCellEditorComponent(this.jTableRules, this.jTableRules.getModel().getValueAt(0, column), false, 0, column).getPreferredSize().width; }
  }
//------------------------------------------------------------------------------
  private int HeaderWidth( TableColumnModel columns, int column ) {
    try { return columns.getColumn(column).getHeaderRenderer().getTableCellRendererComponent(this.jTableRules, columns.getColumn(column).getHeaderValue(), false, false, 0, column).getPreferredSize().width; }
    catch (NullPointerException e) {
    	e.printStackTrace();
    	return jTableRules.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(this.jTableRules, columns.getColumn(column).getHeaderValue(), false, false, 0, column).getPreferredSize().width; }
  }
//------------------------------------------------------------------------------
  private void InitColumnSizes() {
    TableColumnModel columns = jTableRules.getColumnModel();
    columns.getColumn(0).setPreferredWidth(HeaderWidth(columns, 0)+30);
    columns.getColumn(1).setPreferredWidth(HeaderWidth(columns, 1)+30);
    columns.getColumn(2).setPreferredWidth(HeaderWidth(columns, 2)+30);
    int lim= (this.getWidth()-150)/(this.jTableRules.getColumnCount()-3);
    for( int i=3 ; i<this.jTableRules.getColumnCount() ; i++ )
      columns.getColumn(i).setPreferredWidth(Math.max(lim,Math.max(HeaderWidth(columns,i),CellWidth(columns,i)))+30);
  }
//------------------------------------------------------------------------------
  void jTableRules_keyPressed(KeyEvent e) {
    if( this.jTableRules.getSelectedRow() == -1 )
      return;

    if( this.jTableRules.getSelectedRows().length > 0 ) {
    	//System.out.println("e.getKeyCode() -> "+e.getKeyCode());
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
    	        if (e.getKeyCode()==KeyEvent.VK_DELETE)
                    this.RemoveRule();
                else if (e.getKeyCode()==KeyEvent.VK_N)
                    this.NewRule();
                else if (e.getKeyCode()==KeyEvent.VK_C)
                    this.CopyRules();
                else if (e.getKeyCode()==KeyEvent.VK_M)
                    this.MergeRules();
                else if (e.getKeyCode()==KeyEvent.VK_F)
                	this.jButtonFingrams_actionPerformed(false,null);
                else if (e.getKeyCode()==KeyEvent.VK_E)
                    this.ExpandRules();
                else if (e.getKeyCode()==KeyEvent.VK_O)
                    this.OrderByOutputClass(false);
                else if (e.getKeyCode()==KeyEvent.VK_W)
                    this.OrderByWeightRules("L+G", false);
                else if (e.getKeyCode()==KeyEvent.VK_P)
                    this.OrderByNbPremisesRules(false);
                else if (e.getKeyCode()==KeyEvent.VK_I)
                    this.OrderByInterpretabilityRules("L+G", false);
                else if (e.getKeyCode()==KeyEvent.VK_U)
                    this.UpRules();
                else if (e.getKeyCode()==KeyEvent.VK_D)
                    this.DownRules();
                else if (e.getKeyCode()==KeyEvent.VK_R)
                    this.ReverseOrder(false);
    	  
    	        this.firstKey= null;
    	    } else {
        		this.firstKeyPressed= tt;
    	    }
    	}
    }
  }
//------------------------------------------------------------------------------
  void jTableRules_mousePressed(MouseEvent e) {
	  //System.out.println("JExpertFrame: MousePressed");
    if (SwingUtilities.isRightMouseButton(e)) {
      if( this.jTableRules.getSelectedRow() == -1 ) {
          this.jPopupRemoveRule.setEnabled(false);
          this.jPopupRemoveRedundantRules.setEnabled(false);
          this.jPopupSelectRule.setEnabled(false);
          this.jPopupReverseOrder.setEnabled(false);
          this.jPopupOrderByOutputClass.setEnabled(false);
          this.jPopupCumulatedLocalWeightRules.setEnabled(false);
          this.jPopupOrderByLocalWeightRules.setEnabled(false);
          this.jPopupCumulatedGlobalWeightRules.setEnabled(false);
          this.jPopupOrderByGlobalWeightRules.setEnabled(false);
          this.jPopupCumulatedWeightRules.setEnabled(false);
          this.jPopupOrderByWeightRules.setEnabled(false);
          this.jPopupLocalInterpretabilityRules.setEnabled(false);
          this.jPopupOrderByLocalInterpretabilityRules.setEnabled(false);
          this.jPopupGlobalInterpretabilityRules.setEnabled(false);
          this.jPopupOrderByGlobalInterpretabilityRules.setEnabled(false);
          this.jPopupInterpretabilityRules.setEnabled(false);
          this.jPopupOrderByInterpretabilityRules.setEnabled(false);
          this.jPopupOrderByNbPremisesRules.setEnabled(false);
          this.jPopupCopyRule.setEnabled(false);
          this.jPopupUpRule.setEnabled(false);
          this.jPopupDownRule.setEnabled(false);
          this.jPopupExpandRule.setEnabled(false);
          this.jPopupMergeRule.setEnabled(false);
          this.jPopupActiveRule.setEnabled(false);
          this.jPopupInActiveRule.setEnabled(false);
      } else if (this.Temp_kbct.GetRule(this.jTableRules.getSelectedRow()+1).GetActive()) {
          this.jPopupRemoveRule.setEnabled(true);
          this.jPopupCopyRule.setEnabled(true);
          this.jPopupActiveRule.setEnabled(true);
          this.jPopupInActiveRule.setEnabled(true);
          this.jPopupRemoveRedundantRules.setEnabled(true);
          if ( this.jTableRules.getSelectedRowCount() > 1 ) {
            if ( (this.getJExtDataFile()!=null) || (this.Parent.kbct_Data!=null) ) {
                this.jPopupCumulatedLocalWeightRules.setEnabled(true);
                this.jPopupOrderByLocalWeightRules.setEnabled(true);
                this.jPopupCumulatedGlobalWeightRules.setEnabled(true);
                this.jPopupOrderByGlobalWeightRules.setEnabled(true);
                this.jPopupCumulatedWeightRules.setEnabled(true);
                this.jPopupOrderByWeightRules.setEnabled(true);
            }
            this.jPopupSelectRule.setEnabled(true);
            this.jPopupRemoveRedundantRules.setEnabled(true);
            this.jPopupReverseOrder.setEnabled(true);
            this.jPopupOrderByOutputClass.setEnabled(true);
            this.jPopupLocalInterpretabilityRules.setEnabled(true);
            this.jPopupOrderByLocalInterpretabilityRules.setEnabled(true);
            this.jPopupGlobalInterpretabilityRules.setEnabled(true);
            this.jPopupOrderByGlobalInterpretabilityRules.setEnabled(true);
            this.jPopupInterpretabilityRules.setEnabled(true);
            this.jPopupOrderByInterpretabilityRules.setEnabled(true);
            this.jPopupOrderByNbPremisesRules.setEnabled(true);
            this.jPopupMergeRule.setEnabled(true);
            //this.jPopupExpandRule.setEnabled(false);
            this.jPopupExpandRule.setEnabled(true);
            int[] SelectedRules= this.jTableRules.getSelectedRows();
            int FirstSelectedRule= SelectedRules[0];
            int LastSelectedRule= SelectedRules[SelectedRules.length-1];
            if ( (LastSelectedRule - FirstSelectedRule + 1) == SelectedRules.length ) {
                // Consecutive rules
                if ( FirstSelectedRule == 0 )
                     this.jPopupUpRule.setEnabled(false);
                else
                     this.jPopupUpRule.setEnabled(true);

                if ( LastSelectedRule == this.Temp_kbct.GetNbRules() - 1 )
                     this.jPopupDownRule.setEnabled(false);
                else
                     this.jPopupDownRule.setEnabled(true);
            } else {
                this.jPopupUpRule.setEnabled(false);
                this.jPopupDownRule.setEnabled(false);
            }
        } else if ( this.jTableRules.getSelectedRowCount() == 1 ) {
            this.jPopupSelectRule.setEnabled(false);
            this.jPopupRemoveRedundantRules.setEnabled(true);
            this.jPopupReverseOrder.setEnabled(false);
            this.jPopupOrderByOutputClass.setEnabled(false);
            this.jPopupOrderByLocalWeightRules.setEnabled(false);
            this.jPopupOrderByGlobalWeightRules.setEnabled(false);
            if (this.Parent.kbct_Data!=null) {
                this.jPopupCumulatedLocalWeightRules.setEnabled(true);
                this.jPopupCumulatedGlobalWeightRules.setEnabled(true);
                this.jPopupCumulatedWeightRules.setEnabled(true);
            } else {
                this.jPopupCumulatedLocalWeightRules.setEnabled(false);
                this.jPopupCumulatedGlobalWeightRules.setEnabled(false);
                this.jPopupCumulatedWeightRules.setEnabled(false);
            }
            this.jPopupOrderByWeightRules.setEnabled(false);
            this.jPopupLocalInterpretabilityRules.setEnabled(true);
            this.jPopupOrderByLocalInterpretabilityRules.setEnabled(false);
            this.jPopupGlobalInterpretabilityRules.setEnabled(true);
            this.jPopupOrderByGlobalInterpretabilityRules.setEnabled(false);
            this.jPopupInterpretabilityRules.setEnabled(true);
            this.jPopupOrderByInterpretabilityRules.setEnabled(false);
            this.jPopupOrderByNbPremisesRules.setEnabled(false);
            this.jPopupMergeRule.setEnabled(false);
            this.jPopupExpandRule.setEnabled(true);
            if ( this.jTableRules.getSelectedRow() == 0 )
                 this.jPopupUpRule.setEnabled(false);
            else
                 this.jPopupUpRule.setEnabled(true);

            if ( this.jTableRules.getSelectedRow() == this.Temp_kbct.GetNbRules() - 1 )
                 this.jPopupDownRule.setEnabled(false);
            else
                 this.jPopupDownRule.setEnabled(true);
        }
    } else {
      this.jPopupSelectRule.setEnabled(false);
      this.jPopupRemoveRule.setEnabled(false);
      this.jPopupRemoveRedundantRules.setEnabled(false);
      this.jPopupReverseOrder.setEnabled(false);
      this.jPopupOrderByOutputClass.setEnabled(false);
      this.jPopupCumulatedLocalWeightRules.setEnabled(false);
      this.jPopupOrderByLocalWeightRules.setEnabled(false);
      this.jPopupCumulatedGlobalWeightRules.setEnabled(false);
      this.jPopupOrderByGlobalWeightRules.setEnabled(false);
      this.jPopupCumulatedWeightRules.setEnabled(false);
      this.jPopupOrderByWeightRules.setEnabled(false);
      this.jPopupLocalInterpretabilityRules.setEnabled(false);
      this.jPopupOrderByLocalInterpretabilityRules.setEnabled(false);
      this.jPopupGlobalInterpretabilityRules.setEnabled(false);
      this.jPopupOrderByGlobalInterpretabilityRules.setEnabled(false);
      this.jPopupInterpretabilityRules.setEnabled(false);
      this.jPopupOrderByInterpretabilityRules.setEnabled(false);
      this.jPopupOrderByNbPremisesRules.setEnabled(false);
      this.jPopupCopyRule.setEnabled(false);
      this.jPopupUpRule.setEnabled(false);
      this.jPopupDownRule.setEnabled(false);
      this.jPopupExpandRule.setEnabled(false);
      this.jPopupMergeRule.setEnabled(false);
      this.jPopupActiveRule.setEnabled(true);
      this.jPopupInActiveRule.setEnabled(false);
    }
    SwingUtilities.updateComponentTreeUI(this.jPopupMenuRules);   // affecte le look actuel au composant jPopupMenuInputs
    jPopupMenuRules.show(this.jTableRules, e.getX(), e.getY());   // affichage du popup
    }
  }
//------------------------------------------------------------------------------
  void jMenuPrintRules_actionPerformed() {
    try {
      //System.out.println("JExpertFrame: jMenuPrintRules: 1");
      final JPrintTable table= new JPrintTable(this.RuleModel);
      //System.out.println("JExpertFrame: jMenuPrintRules: 2");
      TableColumnModel columnsINI = this.jTableRules.getColumnModel();
      TableColumnModel columns = table.getColumnModel();
      //System.out.println("JExpertFrame: jMenuPrintRules: 3");
      columns.getColumn(0).setWidth(columnsINI.getColumn(0).getWidth());
      columns.getColumn(1).setWidth(columnsINI.getColumn(1).getWidth());
      columns.getColumn(2).setWidth(columnsINI.getColumn(2).getWidth());
      for( int i=3 ; i<table.getColumnCount() ; i++ )
        columns.getColumn(i).setWidth(columnsINI.getColumn(i).getWidth());

      //System.out.println("JExpertFrame: jMenuPrintRules: 4");
      JPrintPreviewTable pp= new JPrintPreviewTable(this, table);
      //System.out.println("JExpertFrame: jMenuPrintRules: 5");
      pp.Show();
    } catch (Exception ex) {
      ex.printStackTrace();
      MessageKBCT.Error(null, ex); }
  }
//------------------------------------------------------------------------------
  void jMenuExportRules_actionPerformed() {
    try {
      ExportDialog export= new ExportDialog();
      final JPrintTable table= new JPrintTable(this.RuleModel);
      table.setSize(table.getPreferredSize());
      table.getTableHeader().setSize(table.getTableHeader().getPreferredSize());
      JPanel panel = new JPanel() {
   	    static final long serialVersionUID=0;	
        public void paint(Graphics g) {
          table.getTableHeader().paint(g);
          g.translate(0,table.getTableHeader().getHeight());
          table.paint(g);
          g.setColor(Color.gray);
          g.drawLine(0,0,0,table.getHeight()-1);
        }
      };
    panel.setSize(new Dimension(table.getWidth(), table.getHeight() + table.getTableHeader().getHeight()));
    export.showExportDialog( panel, "Export view as ...", panel, "export" );
    }
  catch (Exception ex) {
    ex.printStackTrace();
    MessageKBCT.Error(null, ex); }
  }
//------------------------------------------------------------------------------
/**
 * Class of table for printing
 */
  public class JPrintTable extends JTable {
    static final long serialVersionUID=0;	
    public JPrintTable(TableModel dm) {
      super( dm );
      //final JKBCT copyaux= new JKBCT(Temp_kbct.GetKBCTFile());
      final JKBCT copyaux= new JKBCT(Temp_kbct);
      copyaux.SetKBCTFile(Temp_kbct.GetKBCTFile());
      this.setColumnModel(new DefaultTableColumnModel() {
   	    static final long serialVersionUID=0;	
        public void addColumn(TableColumn tc) {
          if( tc.getModelIndex() <= copyaux.GetNbInputs()+copyaux.GetNbOutputs()+2 ) { super.addColumn(tc); return; }
        }
      });
      this.createDefaultColumnsFromModel();
      for( int i=0 ; i<this.getColumnCount() ; i++ )
      this.getColumnModel().getColumn(i).setHeaderRenderer(new DefaultTableCellRenderer() {
   	    static final long serialVersionUID=0;	
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
          super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
          super.setHorizontalAlignment(SwingConstants.CENTER);
          super.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
          if( column == 0 ) { super.setText(LocaleKBCT.GetString("Rule")); }
          if( column == 1 ) { super.setText(LocaleKBCT.GetString("Type")); }
          if( column == 2 ) { super.setText(LocaleKBCT.GetString("Active")); }
          try {
            JVariable input = null;
            if( (column == 3) && (NbIn != 0) ) {
              input = copyaux.GetInput(1);
              super.setText(LocaleKBCT.GetString("If") + " " + input.GetName());
            }
            if( (column > 3) && (column < (NbIn+3)) ) {
              input = copyaux.GetInput(column-2);
              super.setText(LocaleKBCT.GetString("AND") + " " + input.GetName());
            }
            if( column == (NbIn+3) ) {
              input = copyaux.GetOutput(1);
              super.setText(LocaleKBCT.GetString("THEN") + " " + input.GetName());
            }
            if( (column > (NbIn+3)) && (column < (NbIn+NbOut+3)) ) {
              input = copyaux.GetOutput(column-NbIn-2);
              super.setText(input.GetName());
            }
            //super.setForeground(Color.blue);
            super.setForeground(Color.yellow);
            super.setBackground(Color.black);
          } catch( Throwable t ) {
            t.printStackTrace();
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JPrintTable: jbInitTables: "+t);
          }
          return this;
          }
        });
    for( int i = 0; i < this.getColumnCount(); i++ )
      this.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
   	    static final long serialVersionUID=0;	
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        int col_mod = table.convertColumnIndexToModel(column);
        if (col_mod >= 3) {
          if ( ( (Integer) value).intValue() == 0)
            this.setText("");
          else {
            try {
              JVariable input;
              int NbInputs= copyaux.GetNbInputs();
              if (col_mod-3 < NbInputs)
                input= copyaux.GetInput(col_mod-3 + 1);
              else
                input= copyaux.GetOutput(col_mod-3 + 1 - NbInputs);

              int NbLabels = input.GetLabelsNumber();
              if (!input.GetScaleName().equals("user")) {
                if ( ( (Integer) value).intValue() > NbLabels) {
                  if ( ( (Integer) value).intValue() > 2 * NbLabels)
                    super.setText(input.GetORLabelsName( ( (Integer) value).intValue() - 1 - 2 * NbLabels));
                  else
                    this.setText(LocaleKBCT.GetString("NOT") + "(" + LocaleKBCT.GetString(input.GetLabelsName( ( (Integer) value).intValue() - 1 - NbLabels)) + ")");
                }
                else
                  this.setText(LocaleKBCT.GetString(input.GetLabelsName( ( (Integer) value).intValue() - 1)));
              }
              else {
                if ( ( (Integer) value).intValue() > NbLabels) {
                  if ( ( (Integer) value).intValue() > 2 * NbLabels)
                    super.setText(input.GetORLabelsName( ( (Integer) value).intValue() - 1 - 2 * NbLabels));
                  else
                    this.setText(LocaleKBCT.GetString("NOT") + "(" + input.GetUserLabelsName( ( (Integer) value).intValue() - 1 - NbLabels) + ")");
                }
                else
                  this.setText(input.GetUserLabelsName( ( (Integer) value).intValue() - 1));
              }
            } catch (Throwable t ) {
                t.printStackTrace();
                this.setText("");
            }
          }
        } else
          super.setBackground(Color.green);

        this.setHorizontalAlignment(SwingConstants.CENTER);
        Rule r= copyaux.GetRule(row+1);
        boolean RuleActive= r.GetActive();
        String RuleType= r.GetType();
        if (!RuleActive) {
          super.setForeground(Color.gray);
          super.setBackground(Color.yellow);
          return this;
        }
        if (RuleType.equals("I"))
          super.setForeground(Color.red);
        else if (RuleType.equals("E"))
          super.setForeground(Color.black);
        else if ( (RuleType.equals("S")) || (RuleType.equals("P")) )
          super.setForeground(Color.blue);

        if (col_mod >= 3)
          super.setBackground(Color.white);

        return this;
        }
      });
    }
  }
//------------------------------------------------------------------------------
  public void DisableAllButtons() {
      this.jButtonConsistency.setEnabled(false);
      this.jButtonSimplify.setEnabled(false);
      this.jButtonLogView.setEnabled(false);
      this.jButtonOptimization.setEnabled(false);
      this.jButtonCompleteness.setEnabled(false);
      this.jButtonQuality.setEnabled(false);
      this.jButtonInfer.setEnabled(false);
      this.jButtonSummary.setEnabled(false);
      this.jButtonFingrams.setEnabled(false);
  }
//------------------------------------------------------------------------------
  public void EnableAllButtons() {
      this.jButtonConsistency.setEnabled(true);
      this.jButtonSimplify.setEnabled(true);
      this.jButtonLogView.setEnabled(true);
      this.jButtonOptimization.setEnabled(true);
      this.jButtonCompleteness.setEnabled(true);
      this.jButtonQuality.setEnabled(true);
      this.jButtonInfer.setEnabled(true);
      this.jButtonSummary.setEnabled(true);
      this.jButtonFingrams.setEnabled(true);
  }
}

class InterruptTimerTask extends TimerTask {
	private Thread thread;
	public InterruptTimerTask(Thread t) {
		this.thread= t;
	}
	public void run() {
		thread.interrupt();
	}
}