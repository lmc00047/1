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
//                        JKBInterpretabilityFrame.java
//
//
//**********************************************************************

package kbctFrames;

import java.awt.Color;
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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import kbct.JLogicalView;
import kbct.JConvert;
import kbct.JFIS;
import kbct.JKBCT;
import kbct.JKBCTOutput;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbct.jnikbct;
import kbctAux.DoubleField;
import kbctAux.JFileFilter;
import kbctAux.JOpenFileChooser;
import kbctAux.MessageKBCT;

import org.freehep.util.export.ExportDialog;

import print.JPrintPreview;
import KB.Rule;
import fis.JSemaphore;
import fis.JExtendedDataFile;

/**
 * kbctFrames.JKBInterpretabilityFrame.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
//------------------------------------------------------------------------------
public class JKBInterpretabilityFrame extends JChildFrame {
  static final long serialVersionUID=0;	
  private JKBCT kbct;
  private JMenu jMenuView = new JMenu();
    private JMenu jMenuViewKB1 = new JMenu();
      private JMenuItem jMenuViewKB1_db = new JMenuItem();
      private JMenuItem jMenuViewKB1_rb = new JMenuItem();
    private JMenu jMenuViewKB2 = new JMenu();
      private JMenuItem jMenuViewKB2_db = new JMenuItem();
      private JMenuItem jMenuViewKB2_rb = new JMenuItem();
    private JMenu jMenuViewKB3 = new JMenu();
      private JMenuItem jMenuViewKB3_db = new JMenuItem();
      private JMenuItem jMenuViewKB3_rb = new JMenuItem();
    private JMenu jMenuViewKB211 = new JMenu();
      private JMenuItem jMenuViewKB211_db = new JMenuItem();
      private JMenuItem jMenuViewKB211_rb = new JMenuItem();
    private JMenu jMenuViewKB212 = new JMenu();
      private JMenuItem jMenuViewKB212_db = new JMenuItem();
      private JMenuItem jMenuViewKB212_rb = new JMenuItem();
    private JMenu jMenuViewKB213 = new JMenu();
      private JMenuItem jMenuViewKB213_db = new JMenuItem();
      private JMenuItem jMenuViewKB213_rb = new JMenuItem();
    private JMenu jMenuViewKB221 = new JMenu();
      private JMenuItem jMenuViewKB221_db = new JMenuItem();
      private JMenuItem jMenuViewKB221_rb = new JMenuItem();
    private JMenu jMenuViewKB222 = new JMenu();
      private JMenuItem jMenuViewKB222_db = new JMenuItem();
      private JMenuItem jMenuViewKB222_rb = new JMenuItem();
    private JMenu jMenuViewKB223 = new JMenu();
      private JMenuItem jMenuViewKB223_db = new JMenuItem();
      private JMenuItem jMenuViewKB223_rb = new JMenuItem();
    private JMenu jMenuViewKB4 = new JMenu();
      private JMenuItem jMenuViewKB4_db = new JMenuItem();
      private JMenuItem jMenuViewKB4_rb = new JMenuItem();

  private JMenu jMenuOpenKBfile = new JMenu();
    private JMenuItem jMenuOpenKBfile1 = new JMenuItem();
    private JMenuItem jMenuOpenKBfile2 = new JMenuItem();
    private JMenuItem jMenuOpenKBfile3 = new JMenuItem();
    private JMenuItem jMenuOpenKBfile211 = new JMenuItem();
    private JMenuItem jMenuOpenKBfile212 = new JMenuItem();
    private JMenuItem jMenuOpenKBfile213 = new JMenuItem();
    private JMenuItem jMenuOpenKBfile221 = new JMenuItem();
    private JMenuItem jMenuOpenKBfile222 = new JMenuItem();
    private JMenuItem jMenuOpenKBfile223 = new JMenuItem();
    private JMenuItem jMenuOpenKBfile4 = new JMenuItem();

  private JMenuItem jMenuInputs= new JMenuItem();
  private JMenuItem jMenuReset= new JMenuItem();
  private JMenuItem jMenuPrint= new JMenuItem();
  private JMenuItem jMenuExport= new JMenuItem();
  private JMenuItem jMenuHelp= new JMenuItem();
  private JMenuItem jMenuClose= new JMenuItem();
  private DoubleField jdfNbTotalDefinedLabels= new DoubleField();
  private DoubleField jdfPartitionInterpretability= new DoubleField();
  private DoubleField jdfTotalNbRules= new DoubleField();
  private DoubleField jdfTotalNbPremises= new DoubleField();
  private DoubleField jdfRuleBaseStructuralDimension= new DoubleField();
  private DoubleField jdfPercentageNbRulesWithLessThanLPercentInputs= new DoubleField();
  private DoubleField jdfPercentageNbRulesWithBetweenLAndMPercentInputs= new DoubleField();
  private DoubleField jdfPercentageNbRulesWithMoreThanMPercentInputs= new DoubleField();
  private DoubleField jdfRuleBaseStructuralComplexity= new DoubleField();
  private DoubleField jdfRuleBaseStructuralFramework= new DoubleField();
  private DoubleField jdfNbTotalInputs= new DoubleField();
  private DoubleField jdfNbTotalUsedLabels= new DoubleField();
  private DoubleField jdfRuleBaseConceptualDimension= new DoubleField();
  private DoubleField jdfPercentageNbTotalElementaryUsedLabels= new DoubleField();
  private DoubleField jdfPercentageNbTotalNOTCompositeUsedLabels= new DoubleField();
  private DoubleField jdfPercentageNbTotalORCompositeUsedLabels= new DoubleField();
  private DoubleField jdfRuleBaseConceptualComplexity= new DoubleField();
  private DoubleField jdfRuleBaseConceptualFramework= new DoubleField();
  private DoubleField jdfRBInterpretability= new DoubleField();
  private DoubleField jdfDBInterpretability= new DoubleField();
  private DoubleField jdfDimInterpretability= new DoubleField();
  private DoubleField jdfNbRulesTogetherFired2= new DoubleField();
  private DoubleField jdfNbRulesTogetherFired3= new DoubleField();
  private DoubleField jdfNbRulesTogetherFired4= new DoubleField();
  private DoubleField jdfInfInterpretability= new DoubleField();
  private DoubleField jdfInputsTotalNbDefinedLabels= new DoubleField();
  private DoubleField jdfInputsPartitionInterpretability= new DoubleField();
  private DoubleField jdfInputsTotalNbRules= new DoubleField();
  private DoubleField jdfInputsTotalNbPremises= new DoubleField();
  private DoubleField jdfInputsPercentageNbRulesWithLessThanLPercentInputs= new DoubleField();
  private DoubleField jdfInputsPercentageNbRulesWithBetweenLAndMPercentInputs= new DoubleField();
  private DoubleField jdfInputsPercentageNbRulesWithMoreThanMPercentInputs= new DoubleField();
  private DoubleField jdfInputsNbTotalInputs= new DoubleField();
  private DoubleField jdfInputsNbTotalUsedLabels= new DoubleField();
  private DoubleField jdfInputsPercentageNbTotalElementaryUsedLabels= new DoubleField();
  private DoubleField jdfInputsPercentageNbTotalNOTCompositeUsedLabels= new DoubleField();
  private DoubleField jdfInputsPercentageNbTotalORCompositeUsedLabels= new DoubleField();
  private DoubleField jdfInputsNbRulesTogetherFired2= new DoubleField();
  private DoubleField jdfInputsNbRulesTogetherFired3= new DoubleField();
  private DoubleField jdfInputsNbRulesTogetherFired4= new DoubleField();
  private DoubleField jdfNauckIndex= new DoubleField();
  private DoubleField jdfIshibuchiNbRules= new DoubleField();
  private DoubleField jdfIshibuchiTotalRuleLength= new DoubleField();
  private DoubleField jdfAccumulatedRuleComplexity= new DoubleField();
  private DoubleField jdfAccumulatedRuleComplexitySC11= new DoubleField();
  private DoubleField jdfIshibuchiAverageRuleLength= new DoubleField();
  private DoubleField jdfAverageFiredRulesTheory= new DoubleField();
  private DoubleField jdfMinFiredRulesTheory= new DoubleField();
  private DoubleField jdfMaxFiredRulesTheory= new DoubleField();
  private DoubleField jdfAverageFiredRulesTraining= new DoubleField();
  private DoubleField jdfMinFiredRulesTraining= new DoubleField();
  private DoubleField jdfMaxFiredRulesTraining= new DoubleField();
  private DoubleField jdfAverageFiredRulesTest= new DoubleField();
  private DoubleField jdfMinFiredRulesTest= new DoubleField();
  private DoubleField jdfMaxFiredRulesTest= new DoubleField();
  private DoubleField jdfLVindexTraining= new DoubleField();
  private DoubleField jdfLVindexTest= new DoubleField();
  private JButton jButtonTotalNbDefinedLabels= new JButton();
  private JButton jButtonPartitionInterpretability= new JButton();
  private JButton jButtonTotalNbRules= new JButton();
  private JButton jButtonTotalNbPremises= new JButton();
  private JButton jButtonRBstructuralFramework= new JButton();
  private JButton jButtonRBstructuralDimension= new JButton();
  private JButton jButtonPercentageNbRulesWithLessThanLPercentInputs= new JButton();
  private JButton jButtonPercentageNbRulesWithBetweenLAndMPercentInputs= new JButton();
  private JButton jButtonPercentageNbRulesWithMoreThanMPercentInputs= new JButton();
  private JButton jButtonRBstructuralComplexity= new JButton();
  private JButton jButtonNbRulesTogetherFired2= new JButton();
  private JButton jButtonNbRulesTogetherFired3= new JButton();
  private JButton jButtonNbRulesTogetherFired4= new JButton();
  private JButton jButtonInferInterpretability= new JButton();
  private JButton jButtonRBInterpretability= new JButton();
  private JButton jButtonNbTotalInputs= new JButton();
  private JButton jButtonNbTotalUsedLabels= new JButton();
  private JButton jButtonRBconceptualFramework= new JButton();
  private JButton jButtonRBconceptualDimension= new JButton();
  private JButton jButtonPercentageNbTotalElementaryUsedLabels= new JButton();
  private JButton jButtonPercentageNbTotalNOTCompositeUsedLabels= new JButton();
  private JButton jButtonPercentageNbTotalORCompositeUsedLabels= new JButton();
  private JButton jButtonRBconceptualComplexity= new JButton();
  private JButton jButtonDBInterpretability= new JButton();
  private JButton jButtonInterpretabilityIndex= new JButton();
  private JButton jButtonRB1= new JButton();
  private JButton jButtonRB2= new JButton();
  private JButton jButtonRB3= new JButton();
  private JButton jButtonRB211= new JButton();
  private JButton jButtonRB212= new JButton();
  private JButton jButtonRB213= new JButton();
  private JButton jButtonRB221= new JButton();
  private JButton jButtonRB222= new JButton();
  private JButton jButtonRB223= new JButton();
  private JButton jButtonRB4= new JButton();
  private JLabel jLabelInterpMsg= new JLabel();
  private JLabel jLabelRBbehaviourMsg= new JLabel();
  private JLabel jLabelNauckIndex= new JLabel();
  private JLabel jLabelIshibuchiNbRules= new JLabel();
  private JLabel jLabelIshibuchiTotalRuleLength= new JLabel();
  private JLabel jLabelIshibuchiAverageRuleLength= new JLabel();
  private JLabel jLabelAccumulatedRuleComplexity= new JLabel();
  private JLabel jLabelAccumulatedRuleComplexitySC11= new JLabel();
  private JLabel jLabelAverageFiredRulesTheory= new JLabel();
  private JLabel jLabelMinFiredRulesTheory= new JLabel();
  private JLabel jLabelMaxFiredRulesTheory= new JLabel();
  private JLabel jLabelAverageFiredRulesTraining= new JLabel();
  private JLabel jLabelMinFiredRulesTraining= new JLabel();
  private JLabel jLabelMaxFiredRulesTraining= new JLabel();
  private JLabel jLabelAverageFiredRulesTest= new JLabel();
  private JLabel jLabelMinFiredRulesTest= new JLabel();
  private JLabel jLabelMaxFiredRulesTest= new JLabel();
  private JLabel jLabelLVindexTraining= new JLabel();
  private JLabel jLabelLVindexTest= new JLabel();
  private double NbTotalDefinedLabels=0;
  private double PartitionInterpretability=1;
  private double NbTotalRules=0;
  private double NbTotalPremises= 0;
  private double PercentageNbRulesWithLessThanLPercentInputs= 0;
  private double PercentageNbRulesWithBetweenLAndMPercentInputs= 0;
  private double PercentageNbRulesWithMoreThanMPercentInputs= 0;
  private double NbTotalInputs= 0;
  private double NbTotalLabelsUsedByInput= 0;
  private double PercentageNbTotalElementaryLabelsUsedByInput= 0;
  private double PercentageNbTotalNOTCompositeLabelsUsedByInput= 0;
  private double PercentageNbTotalORCompositeLabelsUsedByInput= 0;
  private int NbTuples2=0;
  private int NbTuples3=0;
  private int NbTuples4=0;
  private double InterpIndex= 0;
  private double RBbehaviour= 0;
  private double[] FIS_KB1_DataBaseInterpretability= null;
  private double[] FIS_KB2_RuleBaseInterpretability= null;
  private double[] FIS_KB3_InterpretabilityIndex= null;
  private double[] FIS_KB211_RuleBaseStructuralDimension= null;
  private double[] FIS_KB212_RuleBaseStructuralComplexity= null;
  private double[] FIS_KB213_RuleBaseStructuralFramework= null;
  private double[] FIS_KB221_RuleBaseConceptualDimension= null;
  private double[] FIS_KB222_RuleBaseConceptualComplexity= null;
  private double[] FIS_KB223_RuleBaseConceptualFramework= null;
  private double[] FIS_KB4_LocalExplanation= null;
  private String IntLabel= "very_low";
  private String InferLabel= "very_low";
  private DecimalFormat df;
  private JDataBaseForKBInterpretabilityFrame jdbfif1= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif2= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif3= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif211= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif212= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif213= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif221= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif222= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif223= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif4= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif1_NbDefinedLabels= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif1_PartitionInterpretability= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif211_NbRules= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif211_NbPremises= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif212_PercentageNbRulesWithLessThanLInputs= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif212_PercentageNbRulesWithBetweenLandMInputs= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif212_PercentageNbRulesWithMoreThanMInputs= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif213_RuleBaseStructuralDimension= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif213_RuleBaseStructuralComplexity= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif221_NbTotalInputs= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif221_NbTotalUsedLabels= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif222_PercentageNbTotalElementaryUsedLabels= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif222_PercentageNbTotalNOTCompositeUsedLabels= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif222_PercentageNbTotalORCompositeUsedLabels= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif223_RuleBaseConceptualDimension= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif223_RuleBaseConceptualComplexity= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif2_RuleBaseStructuralFramework= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif2_RuleBaseConceptualFramework= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif3_DBInterpretability= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif3_RBInterpretability= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif3_InterpretabilityIndex= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif4_NbRulesTogetherFired2= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif4_NbRulesTogetherFired3= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif4_NbRulesTogetherFired4= null;
  private JDataBaseForKBInterpretabilityFrame jdbfif4_LocalExplanation= null;
  private JInferenceFrame jifKB1_DataBaseInterpretability= null;
  private JInferenceFrame jifKB2_RuleBaseInterpretability= null;
  private JInferenceFrame jifKB3_InterpretabilityIndex= null;
  private JInferenceFrame jifKB211_RuleBaseStructuralDimension= null;
  private JInferenceFrame jifKB212_RuleBaseStructuralComplexity= null;
  private JInferenceFrame jifKB213_RuleBaseStructuralFramework= null;
  private JInferenceFrame jifKB221_RuleBaseConceptualDimension= null;
  private JInferenceFrame jifKB222_RuleBaseConceptualComplexity= null;
  private JInferenceFrame jifKB223_RuleBaseConceptualFramework= null;
  private JInferenceFrame jifKB4_LocalExplanation= null;
  private JFIS InterpKB1_DataBaseInterpretability= null;
  private JFIS InterpKB2_RuleBaseInterpretability= null;
  private JFIS InterpKB3_InterpretabilityIndex= null;
  private JFIS InterpKB211_RuleBaseStructuralDimension= null;
  private JFIS InterpKB212_RuleBaseStructuralComplexity= null;
  private JFIS InterpKB213_RuleBaseStructuralFramework= null;
  private JFIS InterpKB221_RuleBaseConceptualDimension= null;
  private JFIS InterpKB222_RuleBaseConceptualComplexity= null;
  private JFIS InterpKB223_RuleBaseConceptualFramework= null;
  private JFIS InterpKB4_LocalExplanation= null;
  private JKBCT[] kbctDataBase= new JKBCT[10];
  private JKBCT[] kbctRulesInfer= new JKBCT[10];
  private String[] conjunction= new String[10];
  private String[] disjunction= new String[10];
  private String[] defuzzification= new String[10];
  private JPanel jPanelInputsRB1= new JPanel(new GridBagLayout());
  private JPanel jPanelRuleBase1= new JPanel(new GridBagLayout());
  private JPanel jPanelInputsRB2= new JPanel(new GridBagLayout());
  private JPanel jPanelRuleBaseRB2= new JPanel(new GridBagLayout());
  private JPanel jPanelRuleBaseRB21= new JPanel(new GridBagLayout());
  private JPanel jPanelRuleBaseRB22= new JPanel(new GridBagLayout());
  private JPanel jPanelRuleBase2= new JPanel(new GridBagLayout());
  private JPanel jPanelRuleBase3= new JPanel(new GridBagLayout());
  private JPanel jPanelInputsInf= new JPanel(new GridBagLayout());
  private JPanel jPanelKBI= new JPanel(new GridBagLayout());
  private JPanel jPanelRuleBase= new JPanel(new GridBagLayout());
  private JPanel jPanelInputs= new JPanel(new GridBagLayout());
  private JPanel jPanelOutputDim= new JPanel(new GridBagLayout());
  private JPanel jPanelOutputInf= new JPanel(new GridBagLayout());
  private JPanel jAllPanelDim= new JPanel(new GridBagLayout());
  private JPanel jAllPanelInf= new JPanel(new GridBagLayout());
  private JPanel jPanelOtherIndices= new JPanel(new GridBagLayout());
  private JScrollPane jMainPanelComplex= new JScrollPane();
  private JRadioButton jButtonViewAll= new JRadioButton();
  private JRadioButton jButtonViewRBbehaviour= new JRadioButton();
  private double indInt;
  private JTabbedPane main_TabbedPane = new JTabbedPane();
  private JScrollPane jMainPanelInfer= new JScrollPane();
  private JScrollPane jMainPanelOtherIndices= new JScrollPane();
  private boolean flagFirstBehaviorInference= false;
  // Percentage of inputs used in the rule base
  private double Lpercent= MainKBCT.getConfig().GetParameterL(); 
  private double Mpercent= MainKBCT.getConfig().GetParameterM(); 
  private double NauckIndex;
  private double IshibuchiNbRules;
  private double IshibuchiTotalRuleLength;
  private double IshibuchiAverageRuleLength;
  private double AccumulatedRuleComplexity;
  private double AccumulatedRuleComplexitySC11;
  private double AverageFiredRulesTheory;
  private double MinFiredRulesTheory;
  private double MaxFiredRulesTheory;
  private double AverageFiredRulesTraining;
  private double MinFiredRulesTraining;
  private double MaxFiredRulesTraining;
  private double AverageFiredRulesTest;
  private double MinFiredRulesTest;
  private double MaxFiredRulesTest;
  private double LVindexTraining;
  private double LVindexTest;
  private JExtendedDataFile ddtrain;
  private JExtendedDataFile ddtest;
  private boolean warningAbort;
  private boolean firstDispose=true;
  private long ptrid;
  private boolean fingramsFlag;
  private int[] maxSFR;
  
//------------------------------------------------------------------------------
  public void Translate() throws Throwable {
    this.setTitle(LocaleKBCT.GetString("Interpretability"));
    this.jMenuView.setText(LocaleKBCT.GetString("View"));
    this.jMenuViewKB1.setText(LocaleKBCT.GetString("KBCT")+"1");
    this.jMenuViewKB1_db.setText(LocaleKBCT.GetString("DB")+" 1");
    this.jMenuViewKB1_rb.setText(LocaleKBCT.GetString("RB")+" 1");
    this.jMenuViewKB2.setText(LocaleKBCT.GetString("KBCT")+"2");
    this.jMenuViewKB2_db.setText(LocaleKBCT.GetString("DB")+" 2");
    this.jMenuViewKB2_rb.setText(LocaleKBCT.GetString("RB")+" 2");
    this.jMenuViewKB3.setText(LocaleKBCT.GetString("KBCT")+"3");
    this.jMenuViewKB3_db.setText(LocaleKBCT.GetString("DB")+" 3");
    this.jMenuViewKB3_rb.setText(LocaleKBCT.GetString("RB")+" 3");
    this.jMenuViewKB211.setText(LocaleKBCT.GetString("KBCT")+"211");
    this.jMenuViewKB211_db.setText(LocaleKBCT.GetString("DB")+" 211");
    this.jMenuViewKB211_rb.setText(LocaleKBCT.GetString("RB")+" 211");
    this.jMenuViewKB212.setText(LocaleKBCT.GetString("KBCT")+"212");
    this.jMenuViewKB212_db.setText(LocaleKBCT.GetString("DB")+" 212");
    this.jMenuViewKB212_rb.setText(LocaleKBCT.GetString("RB")+" 212");
    this.jMenuViewKB213.setText(LocaleKBCT.GetString("KBCT")+"213");
    this.jMenuViewKB213_db.setText(LocaleKBCT.GetString("DB")+" 213");
    this.jMenuViewKB213_rb.setText(LocaleKBCT.GetString("RB")+" 213");
    this.jMenuViewKB221.setText(LocaleKBCT.GetString("KBCT")+"221");
    this.jMenuViewKB221_db.setText(LocaleKBCT.GetString("DB")+" 221");
    this.jMenuViewKB221_rb.setText(LocaleKBCT.GetString("RB")+" 221");
    this.jMenuViewKB222.setText(LocaleKBCT.GetString("KBCT")+"222");
    this.jMenuViewKB222_db.setText(LocaleKBCT.GetString("DB")+" 222");
    this.jMenuViewKB222_rb.setText(LocaleKBCT.GetString("RB")+" 222");
    this.jMenuViewKB223.setText(LocaleKBCT.GetString("KBCT")+"223");
    this.jMenuViewKB223_db.setText(LocaleKBCT.GetString("DB")+" 223");
    this.jMenuViewKB223_rb.setText(LocaleKBCT.GetString("RB")+" 223");
    this.jMenuViewKB4.setText(LocaleKBCT.GetString("KBCT")+"4");
    this.jMenuViewKB4_db.setText(LocaleKBCT.GetString("DB")+" 4");
    this.jMenuViewKB4_rb.setText(LocaleKBCT.GetString("RB")+" 4");
    this.jMenuOpenKBfile.setText(LocaleKBCT.GetString("Open"));
    this.jMenuOpenKBfile1.setText(LocaleKBCT.GetString("KBCT")+"1");
    this.jMenuOpenKBfile211.setText(LocaleKBCT.GetString("KBCT")+"211");
    this.jMenuOpenKBfile212.setText(LocaleKBCT.GetString("KBCT")+"212");
    this.jMenuOpenKBfile213.setText(LocaleKBCT.GetString("KBCT")+"213");
    this.jMenuOpenKBfile221.setText(LocaleKBCT.GetString("KBCT")+"221");
    this.jMenuOpenKBfile222.setText(LocaleKBCT.GetString("KBCT")+"222");
    this.jMenuOpenKBfile223.setText(LocaleKBCT.GetString("KBCT")+"223");
    this.jMenuOpenKBfile2.setText(LocaleKBCT.GetString("KBCT")+"2");
    this.jMenuOpenKBfile3.setText(LocaleKBCT.GetString("KBCT")+"3");
    this.jMenuOpenKBfile4.setText(LocaleKBCT.GetString("KBCT")+"4");
    this.jMenuInputs.setText(LocaleKBCT.GetString("Inputs"));
    this.jMenuReset.setText(LocaleKBCT.GetString("Reset"));
    this.jMenuPrint.setText(LocaleKBCT.GetString("Print"));
    this.jMenuExport.setText(LocaleKBCT.GetString("Export"));
    this.jMenuHelp.setText(LocaleKBCT.GetString("Help"));
    this.jMenuClose.setText(LocaleKBCT.GetString("Close"));
    this.jButtonTotalNbDefinedLabels.setText(LocaleKBCT.GetString("NbTotalDefinedLabels"));
    this.jButtonPartitionInterpretability.setText(LocaleKBCT.GetString("PartitionInterpretability"));
    this.jButtonTotalNbRules.setText(LocaleKBCT.GetString("NbTotalRules"));
    this.jButtonTotalNbPremises.setText(LocaleKBCT.GetString("NbTotalPremises"));
    this.jButtonRBstructuralFramework.setText(LocaleKBCT.GetString("outputKBRuleBaseStructuralFramework"));
    this.jButtonRBstructuralDimension.setText(LocaleKBCT.GetString("outputKBRuleBaseStructuralDimension"));
    this.jButtonPercentageNbRulesWithLessThanLPercentInputs.setText(LocaleKBCT.GetString("PercentageNbRulesWithLessThanLPercentInputs"));
    this.jButtonPercentageNbRulesWithBetweenLAndMPercentInputs.setText(LocaleKBCT.GetString("PercentageNbRulesWithBetweenLAndMPercentInputs"));
    this.jButtonPercentageNbRulesWithMoreThanMPercentInputs.setText(LocaleKBCT.GetString("PercentageNbRulesWithMoreThanMPercentInputs"));
    this.jButtonRBstructuralComplexity.setText(LocaleKBCT.GetString("outputKBRuleBaseStructuralComplexity"));
    this.jButtonRBInterpretability.setText(LocaleKBCT.GetString("outputKBRuleBaseInterpretability"));
    this.jButtonInterpretabilityIndex.setText(LocaleKBCT.GetString("InterpretabilityIndex"));
    this.jButtonNbTotalInputs.setText(LocaleKBCT.GetString("NbTotalInputs"));
    this.jButtonNbTotalUsedLabels.setText(LocaleKBCT.GetString("NbTotalUsedLabels"));
    this.jButtonRBconceptualFramework.setText(LocaleKBCT.GetString("outputKBRuleBaseConceptualFramework"));
    this.jButtonRBconceptualDimension.setText(LocaleKBCT.GetString("outputKBRuleBaseConceptualDimension"));
    this.jButtonPercentageNbTotalElementaryUsedLabels.setText(LocaleKBCT.GetString("PercentageNbTotalElementaryUsedLabels"));
    this.jButtonPercentageNbTotalNOTCompositeUsedLabels.setText(LocaleKBCT.GetString("PercentageNbTotalNOTCompositeUsedLabels"));
    this.jButtonPercentageNbTotalORCompositeUsedLabels.setText(LocaleKBCT.GetString("PercentageNbTotalORCompositeUsedLabels"));
    this.jButtonRBconceptualComplexity.setText(LocaleKBCT.GetString("outputKBRuleBaseConceptualComplexity"));
    this.jButtonDBInterpretability.setText(LocaleKBCT.GetString("outputKBDataBaseInterpretability"));
    this.jButtonNbRulesTogetherFired2.setText(LocaleKBCT.GetString("NbRulesTogetherFired2"));
    this.jButtonNbRulesTogetherFired3.setText(LocaleKBCT.GetString("NbRulesTogetherFired3"));
    this.jButtonNbRulesTogetherFired4.setText(LocaleKBCT.GetString("NbRulesTogetherFired4"));
    this.jButtonInferInterpretability.setText(LocaleKBCT.GetString("outputKBLocalExplanation"));
    this.jButtonRB1.setText(LocaleKBCT.GetString("RB")+" 1");
    this.jButtonRB2.setText(LocaleKBCT.GetString("RB")+" 2");
    this.jButtonRB3.setText(LocaleKBCT.GetString("RB")+" 3");
    this.jButtonRB211.setText(LocaleKBCT.GetString("RB")+" 211");
    this.jButtonRB212.setText(LocaleKBCT.GetString("RB")+" 212");
    this.jButtonRB213.setText(LocaleKBCT.GetString("RB")+" 213");
    this.jButtonRB221.setText(LocaleKBCT.GetString("RB")+" 221");
    this.jButtonRB222.setText(LocaleKBCT.GetString("RB")+" 222");
    this.jButtonRB223.setText(LocaleKBCT.GetString("RB")+" 223");
    this.jButtonRB4.setText(LocaleKBCT.GetString("RB")+" 4");
    this.jButtonViewAll.setText(LocaleKBCT.GetString("View"));
    this.jButtonViewRBbehaviour.setText(LocaleKBCT.GetString("View"));
    this.jLabelNauckIndex.setText(LocaleKBCT.GetString("NauckIndex")+" :");
    this.jLabelIshibuchiNbRules.setText(LocaleKBCT.GetString("IshibuchiNbRules")+" :");
    this.jLabelIshibuchiTotalRuleLength.setText(LocaleKBCT.GetString("IshibuchiTotalRuleLength")+" :");
    this.jLabelIshibuchiAverageRuleLength.setText(LocaleKBCT.GetString("IshibuchiAverageRuleLength")+" :");
    this.jLabelAccumulatedRuleComplexity.setText(LocaleKBCT.GetString("AccumulatedRuleComplexity"));
    this.jLabelAccumulatedRuleComplexitySC11.setText(LocaleKBCT.GetString("AccumulatedRuleComplexity")+" (SC2011) :");
    this.jLabelAverageFiredRulesTheory.setText(LocaleKBCT.GetString("AverageFiredRulesTheory")+" :");
    this.jLabelMinFiredRulesTheory.setText(LocaleKBCT.GetString("MinFiredRulesTheory")+" :");
    this.jLabelMaxFiredRulesTheory.setText(LocaleKBCT.GetString("MaxFiredRulesTheory")+" :");
    this.jLabelAverageFiredRulesTraining.setText(LocaleKBCT.GetString("AverageFiredRulesTraining")+" :");
    this.jLabelMinFiredRulesTraining.setText(LocaleKBCT.GetString("MinFiredRulesTraining")+" :");
    this.jLabelMaxFiredRulesTraining.setText(LocaleKBCT.GetString("MaxFiredRulesTraining")+" :");
    this.jLabelLVindexTraining.setText(LocaleKBCT.GetString("LVindexTraining")+" :");
    if (!this.warningAbort) {
        this.jLabelAverageFiredRulesTest.setText(LocaleKBCT.GetString("AverageFiredRulesTest")+" :");
        this.jLabelMinFiredRulesTest.setText(LocaleKBCT.GetString("MinFiredRulesTest")+" :");
        this.jLabelMaxFiredRulesTest.setText(LocaleKBCT.GetString("MaxFiredRulesTest")+" :");
        this.jLabelLVindexTest.setText(LocaleKBCT.GetString("LVindexTest")+" :");
    }
    this.repaint();
  }
//------------------------------------------------------------------------------
  public JKBInterpretabilityFrame( JExpertFrame parent, JKBCT jefkbct, boolean ff ) {
	super(parent);
    try {
      //System.out.println("new JKBInterpretabilityFrame");
   	  this.ptrid= jnikbct.getId();
      //System.out.println("JKBInterpretabilityFrame 1");
      jefkbct.Save();
      //System.out.println("JKBInterpretabilityFrame 2");
      this.kbct= new JKBCT(jefkbct.GetKBCTFile());
      //System.out.println("JKBInterpretabilityFrame 3: "+this.kbct.GetNbInputs());
      this.ddtrain= ((JExpertFrame)this.parent).getJExtDataFile();
      String trainDataFile= this.ddtrain.FileName();
      //System.out.println("trainDataFile= "+trainDataFile);
      this.fingramsFlag= ff;
      String testDataFile= "";
      if (trainDataFile.contains("train")) {
     	  testDataFile= trainDataFile.replace("train","test");
      } else if (trainDataFile.contains("lrn.sample")) {
    	  testDataFile= trainDataFile.replace("lrn.sample","tst.sample");
      } else if (trainDataFile.contains("tra.txt")) { 
    	  testDataFile= trainDataFile.replace("tra.txt","tst.txt");
      } else if (trainDataFile.contains("tra.dat")) { 
    	  testDataFile= trainDataFile.replace("tra.dat","tst.dat");
      } 
      this.warningAbort= false;
      if (testDataFile != null) {
          File ftest= new File(testDataFile);
          if ( (testDataFile.equals("")) || (!ftest.exists()) ) {
              JExtendedDataFile auxddtrain= ((JExpertFrame)this.parent).getJExtOrigDataFile();
              String auxTrainDataFile= auxddtrain.FileName();
              String auxTestDataFile= "";
              if (trainDataFile.contains("train")) {
             	  auxTestDataFile= auxTrainDataFile.replace("train","test");
              } else if (trainDataFile.contains("lrn.sample")) {
            	  auxTestDataFile= auxTrainDataFile.replace("lrn.sample","tst.sample");
              } else if (trainDataFile.contains("tra.txt")) { 
            	  auxTestDataFile= auxTrainDataFile.replace("tra.txt","tst.txt");
              } else if (trainDataFile.contains("tra.dat")) { 
            	  auxTestDataFile= auxTrainDataFile.replace("tra.dat","tst.dat");
              }
              ftest= new File(auxTestDataFile);
    	      if ( (auxTestDataFile != null) && ((testDataFile.equals("")) || (!ftest.exists())) )
        	      this.warningAbort= true;
    	      else
    	    	  testDataFile= auxTestDataFile;
          }
      } else {
	      this.warningAbort= true;
      }
      if (!this.warningAbort) {
    	  this.ddtest= new JExtendedDataFile(testDataFile, true);
          this.selectVariablesInTestDataFile();
      }
      jbInit();
      //System.out.println("JKBInterpretabilityFrame 4");
    } catch(Throwable t) {
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in constructor of JKBInterpretabilityFrame: "+t);
    }
  }
//------------------------------------------------------------------------------
  private void jbInit() throws Throwable {
    if (!this.fingramsFlag) {
      JMenuBar jmb= new JMenuBar();
      this.jMenuViewKB1.add(this.jMenuViewKB1_db);
      this.jMenuViewKB1.add(this.jMenuViewKB1_rb);
      this.jMenuView.add(this.jMenuViewKB1);
      this.jMenuView.addSeparator();
      this.jMenuViewKB211.add(this.jMenuViewKB211_db);
      this.jMenuViewKB211.add(this.jMenuViewKB211_rb);
      this.jMenuView.add(this.jMenuViewKB211);
      this.jMenuViewKB212.add(this.jMenuViewKB212_db);
      this.jMenuViewKB212.add(this.jMenuViewKB212_rb);
      this.jMenuView.add(this.jMenuViewKB212);
      this.jMenuViewKB213.add(this.jMenuViewKB213_db);
      this.jMenuViewKB213.add(this.jMenuViewKB213_rb);
      this.jMenuView.add(this.jMenuViewKB213);
      this.jMenuViewKB221.add(this.jMenuViewKB221_db);
      this.jMenuViewKB221.add(this.jMenuViewKB221_rb);
      this.jMenuView.add(this.jMenuViewKB221);
      this.jMenuViewKB222.add(this.jMenuViewKB222_db);
      this.jMenuViewKB222.add(this.jMenuViewKB222_rb);
      this.jMenuView.add(this.jMenuViewKB222);
      this.jMenuViewKB223.add(this.jMenuViewKB223_db);
      this.jMenuViewKB223.add(this.jMenuViewKB223_rb);
      this.jMenuView.add(this.jMenuViewKB223);
      this.jMenuViewKB2.add(this.jMenuViewKB2_db);
      this.jMenuViewKB2.add(this.jMenuViewKB2_rb);
      this.jMenuView.add(this.jMenuViewKB2);
      this.jMenuView.addSeparator();
      this.jMenuViewKB3.add(this.jMenuViewKB3_db);
      this.jMenuViewKB3.add(this.jMenuViewKB3_rb);
      this.jMenuView.add(this.jMenuViewKB3);
      this.jMenuView.addSeparator();
      this.jMenuViewKB4.add(this.jMenuViewKB4_db);
      this.jMenuViewKB4.add(this.jMenuViewKB4_rb);
      this.jMenuView.add(this.jMenuViewKB4);
      this.jMenuView.setVisible(false);
      jmb.add(this.jMenuView);
      this.jMenuOpenKBfile.add(this.jMenuOpenKBfile1);
      this.jMenuOpenKBfile.addSeparator();
      this.jMenuOpenKBfile.add(this.jMenuOpenKBfile211);
      this.jMenuOpenKBfile.add(this.jMenuOpenKBfile212);
      this.jMenuOpenKBfile.add(this.jMenuOpenKBfile213);
      this.jMenuOpenKBfile.add(this.jMenuOpenKBfile221);
      this.jMenuOpenKBfile.add(this.jMenuOpenKBfile222);
      this.jMenuOpenKBfile.add(this.jMenuOpenKBfile223);
      this.jMenuOpenKBfile.add(this.jMenuOpenKBfile2);
      this.jMenuOpenKBfile.addSeparator();
      this.jMenuOpenKBfile.add(this.jMenuOpenKBfile3);
      this.jMenuOpenKBfile.addSeparator();
      this.jMenuOpenKBfile.add(this.jMenuOpenKBfile4);
      this.jMenuOpenKBfile.setVisible(false);
      jmb.add(this.jMenuOpenKBfile);
      jmb.add(this.jMenuInputs);
      this.jMenuInputs.setVisible(false);
      jmb.add(this.jMenuReset);
      this.jMenuReset.setVisible(false);
      jmb.add(this.jMenuPrint);
      jmb.add(this.jMenuExport);
      jmb.add(this.jMenuHelp);
      jmb.add(this.jMenuClose);
      this.setJMenuBar(jmb);
      this.getContentPane().setLayout(new GridBagLayout());
     
    }
    df= new DecimalFormat();
    df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
    dfs.setDecimalSeparator((new String(".").charAt(0)));
    df.setDecimalFormatSymbols(dfs);
    this.ComputeInterpretability();
    //System.out.println("JKBInterpretabilityFrame 3.2");
    if (!this.fingramsFlag) {
        this.BuildResultsPanelDimension();
        this.BuildResultsPanelInference();
    }
    this.BuildResultsPanelOtherIndices();
    if (!this.fingramsFlag) {
        this.jMenuViewKB1_db.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewDB_actionPerformed(1); } });
        this.jMenuViewKB1_rb.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(1); } });
        this.jMenuViewKB2_db.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewDB_actionPerformed(2); } });
        this.jMenuViewKB2_rb.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(2); } });
        this.jMenuViewKB3_db.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewDB_actionPerformed(3); } });
        this.jMenuViewKB3_rb.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(3); } });
        this.jMenuViewKB211_db.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewDB_actionPerformed(211); } });
        this.jMenuViewKB211_rb.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(211); } });
        this.jMenuViewKB212_db.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewDB_actionPerformed(212); } });
        this.jMenuViewKB212_rb.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(212); } });
        this.jMenuViewKB213_db.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewDB_actionPerformed(213); } });
        this.jMenuViewKB213_rb.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(213); } });
        this.jMenuViewKB221_db.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewDB_actionPerformed(221); } });
        this.jMenuViewKB221_rb.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(221); } });
        this.jMenuViewKB222_db.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewDB_actionPerformed(222); } });
        this.jMenuViewKB222_rb.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(222); } });
        this.jMenuViewKB223_db.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewDB_actionPerformed(223); } });
        this.jMenuViewKB223_rb.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(223); } });
        this.jMenuViewKB4_db.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewDB_actionPerformed(4); } });
        this.jMenuViewKB4_rb.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(4); } });
        this.jMenuOpenKBfile1.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonOpenKBfile_actionPerformed(1); } });
        this.jMenuOpenKBfile211.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonOpenKBfile_actionPerformed(211); } });
        this.jMenuOpenKBfile212.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonOpenKBfile_actionPerformed(212); } });
        this.jMenuOpenKBfile213.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonOpenKBfile_actionPerformed(213); } });
        this.jMenuOpenKBfile221.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonOpenKBfile_actionPerformed(221); } });
        this.jMenuOpenKBfile222.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonOpenKBfile_actionPerformed(222); } });
        this.jMenuOpenKBfile223.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonOpenKBfile_actionPerformed(223); } });
        this.jMenuOpenKBfile2.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonOpenKBfile_actionPerformed(2); } });
        this.jMenuOpenKBfile3.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonOpenKBfile_actionPerformed(3); } });
        this.jMenuOpenKBfile4.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonOpenKBfile_actionPerformed(4); } });
        this.jMenuInputs.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jMenuInputs_actionPerformed(); } });
        this.jMenuReset.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jMenuReset_actionPerformed(); } });
        this.jMenuPrint.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jMenuPrint_actionPerformed(); } });
        this.jMenuExport.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jMenuExport_actionPerformed(); } });
        this.jMenuHelp.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jMenuHelp_actionPerformed(); } });
        this.jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.dispose(); } });
        this.jButtonTotalNbDefinedLabels.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(1,1,true); } });
        this.jButtonPartitionInterpretability.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(2,1,true); } });
        this.jButtonDBInterpretability.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(1,1,false); } });
        this.jButtonTotalNbRules.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(1,211,true); } });
        this.jButtonTotalNbPremises.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(2,211,true); } });
        this.jButtonRBstructuralDimension.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(1,211,false); } });
        this.jButtonPercentageNbRulesWithLessThanLPercentInputs.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(1,212,true); } });
        this.jButtonPercentageNbRulesWithBetweenLAndMPercentInputs.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(2,212,true); } });
        this.jButtonPercentageNbRulesWithMoreThanMPercentInputs.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(3,212,true); } });
        this.jButtonRBstructuralComplexity.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(1,212,false); } });
        this.jButtonRBstructuralFramework.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(1,2,true); } });
        this.jButtonNbTotalInputs.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(1,221,true); } });
        this.jButtonNbTotalUsedLabels.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(2,221,true); } });
        this.jButtonRBconceptualDimension.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(1,221,false); } });
        this.jButtonPercentageNbTotalElementaryUsedLabels.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(1,222,true); } });
        this.jButtonPercentageNbTotalORCompositeUsedLabels.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(2,222,true); } });
        this.jButtonPercentageNbTotalNOTCompositeUsedLabels.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(3,222,true); } });
        this.jButtonRBconceptualComplexity.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(1,222,false); } });
        this.jButtonRBconceptualFramework.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(2,2,true); } });
        this.jButtonRBInterpretability.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(1,2,false); } });
        this.jButtonDBInterpretability.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(1,3,true); } });
        this.jButtonInterpretabilityIndex.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(1,3,false); } });
        this.jButtonNbRulesTogetherFired2.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(1,4,true); } });
        this.jButtonNbRulesTogetherFired3.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(2,4,true); } });
        this.jButtonNbRulesTogetherFired4.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(3,4,true); } });
        this.jButtonInferInterpretability.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonPartition_actionPerformed(1,4,false); } });
        jButtonRB1.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(1); } });
        jButtonRB2.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(2); } });
        jButtonRB3.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(3); } });
        jButtonRB211.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(211); } });
        jButtonRB212.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(212); } });
        jButtonRB213.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(213); } });
        jButtonRB221.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(221); } });
        jButtonRB222.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(222); } });
        jButtonRB223.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(223); } });
        jButtonRB4.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRB_actionPerformed(4); } });
        jButtonViewAll.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewAll_actionPerformed(); } });
        jButtonViewRBbehaviour.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JKBInterpretabilityFrame.this.jButtonViewRBbehaviour_actionPerformed(); } });
        this.Translate();
        this.main_TabbedPane.addTab(LocaleKBCT.GetString("outputKBGlobalDescription"), null, this.jMainPanelComplex,	LocaleKBCT.GetString("outputKBGlobalDescription"));
        this.main_TabbedPane.addTab(LocaleKBCT.GetString("outputKBLocalExplanation"), null, this.jMainPanelInfer,	LocaleKBCT.GetString("outputKBLocalExplanation"));
        this.main_TabbedPane.addTab(LocaleKBCT.GetString("outputKBotherIndices"), null, this.jMainPanelOtherIndices,	LocaleKBCT.GetString("outputKBotherIndices"));
        if (MainKBCT.getConfig().GetTutorialFlag())
            this.main_TabbedPane.setSelectedIndex(2);
        else
            this.main_TabbedPane.setSelectedIndex(0);
    	
        this.getContentPane().add(this.main_TabbedPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    
        this.pack();
        this.setLocation(this.ChildPosition(this.getSize()));
    }
    if ( (MainKBCT.getConfig().GetTESTautomatic()) || (this.fingramsFlag) )
        this.setVisible(false);
    else
        this.setVisible(true);
  }
//------------------------------------------------------------------------------
  public void dispose() {
    super.dispose();
    if (this.jifKB1_DataBaseInterpretability!=null)
      this.jifKB1_DataBaseInterpretability.dispose();

    if (this.jifKB2_RuleBaseInterpretability!=null)
      this.jifKB2_RuleBaseInterpretability.dispose();

    if (this.jifKB3_InterpretabilityIndex!=null)
      this.jifKB3_InterpretabilityIndex.dispose();

    if (this.jifKB211_RuleBaseStructuralDimension!=null)
      this.jifKB211_RuleBaseStructuralDimension.dispose();

    if (this.jifKB212_RuleBaseStructuralComplexity!=null)
        this.jifKB212_RuleBaseStructuralComplexity.dispose();

    if (this.jifKB213_RuleBaseStructuralFramework!=null)
        this.jifKB213_RuleBaseStructuralFramework.dispose();

    if (this.jifKB221_RuleBaseConceptualDimension!=null)
        this.jifKB221_RuleBaseConceptualDimension.dispose();

    if (this.jifKB222_RuleBaseConceptualComplexity!=null)
        this.jifKB222_RuleBaseConceptualComplexity.dispose();

    if (this.jifKB223_RuleBaseConceptualFramework!=null)
        this.jifKB223_RuleBaseConceptualFramework.dispose();

    if (this.jifKB4_LocalExplanation!=null)
        this.jifKB4_LocalExplanation.dispose();
    
    if (this.jdbfif1!=null)
      this.jdbfif1.dispose();

    if (this.jdbfif1_NbDefinedLabels!=null)
      this.jdbfif1_NbDefinedLabels.dispose();

    if (this.jdbfif1_PartitionInterpretability!=null)
      this.jdbfif1_PartitionInterpretability.dispose();

    if (this.jdbfif2!=null)
      this.jdbfif2.dispose();

    if (this.jdbfif2_RuleBaseStructuralFramework!=null)
      this.jdbfif2_RuleBaseStructuralFramework.dispose();

    if (this.jdbfif2_RuleBaseConceptualFramework!=null)
        this.jdbfif2_RuleBaseConceptualFramework.dispose();

    if (this.jdbfif3!=null)
      this.jdbfif3.dispose();

    if (this.jdbfif3_DBInterpretability!=null)
      this.jdbfif3_DBInterpretability.dispose();

    if (this.jdbfif3_RBInterpretability!=null)
        this.jdbfif3_RBInterpretability.dispose();

    if (this.jdbfif3_InterpretabilityIndex!=null)
        this.jdbfif3_InterpretabilityIndex.dispose();

    if (this.jdbfif211!=null)
      this.jdbfif211.dispose();

    if (this.jdbfif211_NbRules!=null)
        this.jdbfif211_NbRules.dispose();

    if (this.jdbfif211_NbPremises!=null)
        this.jdbfif211_NbPremises.dispose();

    if (this.jdbfif212!=null)
        this.jdbfif212.dispose();

    if (this.jdbfif212_PercentageNbRulesWithLessThanLInputs!=null)
        this.jdbfif212_PercentageNbRulesWithLessThanLInputs.dispose();
    
    if (this.jdbfif212_PercentageNbRulesWithBetweenLandMInputs!=null)
        this.jdbfif212_PercentageNbRulesWithBetweenLandMInputs.dispose();

    if (this.jdbfif212_PercentageNbRulesWithMoreThanMInputs!=null)
        this.jdbfif212_PercentageNbRulesWithMoreThanMInputs.dispose();
    
    if (this.jdbfif213!=null)
        this.jdbfif213.dispose();

    if (this.jdbfif213_RuleBaseStructuralDimension!=null)
        this.jdbfif213_RuleBaseStructuralDimension.dispose();

    if (this.jdbfif213_RuleBaseStructuralComplexity!=null)
        this.jdbfif213_RuleBaseStructuralComplexity.dispose();

    if (this.jdbfif221!=null)
        this.jdbfif221.dispose();

    if (this.jdbfif221_NbTotalInputs!=null)
        this.jdbfif221_NbTotalInputs.dispose();

    if (this.jdbfif221_NbTotalUsedLabels!=null)
        this.jdbfif221_NbTotalUsedLabels.dispose();

    if (this.jdbfif222!=null)
        this.jdbfif222.dispose();

    if (this.jdbfif222_PercentageNbTotalElementaryUsedLabels!=null)
        this.jdbfif222_PercentageNbTotalElementaryUsedLabels.dispose();
    
    if (this.jdbfif222_PercentageNbTotalNOTCompositeUsedLabels!=null)
        this.jdbfif222_PercentageNbTotalNOTCompositeUsedLabels.dispose();

    if (this.jdbfif222_PercentageNbTotalORCompositeUsedLabels!=null)
        this.jdbfif222_PercentageNbTotalORCompositeUsedLabels.dispose();

    if (this.jdbfif223!=null)
        this.jdbfif223.dispose();

    if (this.jdbfif223_RuleBaseConceptualDimension!=null)
        this.jdbfif223_RuleBaseConceptualDimension.dispose();

    if (this.jdbfif223_RuleBaseConceptualComplexity!=null)
        this.jdbfif223_RuleBaseConceptualComplexity.dispose();

    if (this.jdbfif4_NbRulesTogetherFired2!=null)
        this.jdbfif4_NbRulesTogetherFired2.dispose();

    if (this.jdbfif4_NbRulesTogetherFired3!=null)
        this.jdbfif4_NbRulesTogetherFired3.dispose();

    if (this.jdbfif4_NbRulesTogetherFired4!=null)
      this.jdbfif4_NbRulesTogetherFired4.dispose();

    if (this.jdbfif4_LocalExplanation!=null)
        this.jdbfif4_LocalExplanation.dispose();
   
    if ( (firstDispose) && (!MainKBCT.getConfig().GetTutorialFlag()) ) {
        long[] exc= new long[8];
        exc[0]= this.parent.Temp_kbct.GetPtr();
        exc[1]= this.parent.Temp_kbct.GetCopyPtr();
        exc[2]= this.parent.Parent.jef.kbct.GetPtr();
        exc[3]= this.parent.Parent.jef.kbct.GetCopyPtr();
        exc[4]= this.parent.Parent.kbct.GetPtr();
        exc[5]= this.parent.Parent.kbct.GetCopyPtr();
        exc[6]= this.parent.Parent.kbct_Data.GetPtr();
        exc[7]= this.parent.Parent.kbct_Data.GetCopyPtr();
        jnikbct.cleanHashtable(this.ptrid,exc);
        this.firstDispose= false;
    } 
  }
//------------------------------------------------------------------------------
  private void BuildResultsPanelDimension() {
    try {
      jPanelInputsRB1.add(this.jButtonTotalNbDefinedLabels, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      jPanelInputsRB1.add(this.jdfNbTotalDefinedLabels, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 15, 0));
      this.jdfNbTotalDefinedLabels.setBackground(Color.WHITE);
      this.jdfNbTotalDefinedLabels.setForeground(Color.BLUE);
      this.jdfNbTotalDefinedLabels.setEditable(false);
      jPanelInputsRB1.add(new JLabel("==>"), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      jPanelInputsRB1.add(this.jButtonPartitionInterpretability, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
   	        ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      jPanelInputsRB1.add(this.jdfPartitionInterpretability, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
  	        ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 15, 0));
      this.jdfPartitionInterpretability.setBackground(Color.WHITE);
      this.jdfPartitionInterpretability.setForeground(Color.BLUE);
      this.jdfPartitionInterpretability.setEditable(false);
      jPanelInputsRB1.add(new JLabel("==>"), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      jPanelRuleBase1.add(this.jButtonRB1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 5, 5, 5), 0, 0));
      jPanelRuleBase1.add(new JLabel("==>"), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(25, 0, 25, 0), 0, 0));
      jPanelRuleBase1.add(this.jButtonDBInterpretability, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(35, 5, 35, 5), 0, 0));
      jPanelRuleBase1.add(this.jdfDBInterpretability, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(37, 0, 37, 5), 15, 0));
      this.jdfDBInterpretability.setBackground(Color.WHITE);
      this.jdfDBInterpretability.setForeground(Color.BLUE);
      this.jdfDBInterpretability.setEditable(false);
      jPanelRuleBase1.add(new JLabel("==>"), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(25, 0, 25, 0), 0, 0));
      jPanelInputsRB2.add(this.jButtonTotalNbRules, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
    	      ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 15, 5), 0, 0));
      jPanelInputsRB2.add(this.jdfTotalNbRules, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
    	        ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 15, 5), 15, 0));
      this.jdfTotalNbRules.setBackground(Color.WHITE);
      this.jdfTotalNbRules.setForeground(Color.BLUE);
      this.jdfTotalNbRules.setEditable(false);
      jPanelInputsRB2.add(new JLabel("==>"), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
  	            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 15, 5), 0, 0));
      jPanelInputsRB2.add(this.jButtonTotalNbPremises, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
  	   	        ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 15, 5), 0, 0));
      jPanelInputsRB2.add(this.jdfTotalNbPremises, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 15, 5), 15, 0));
      this.jdfTotalNbPremises.setBackground(Color.WHITE);
      this.jdfTotalNbPremises.setForeground(Color.BLUE);
      this.jdfTotalNbPremises.setEditable(false);
      jPanelInputsRB2.add(new JLabel("==>"), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 15, 5), 0, 0));
      jPanelInputsRB2.add(this.jButtonPercentageNbRulesWithLessThanLPercentInputs, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(15, 5, 5, 5), 0, 0));
      jPanelInputsRB2.add(this.jdfPercentageNbRulesWithLessThanLPercentInputs, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(15, 0, 5, 5), 15, 0));
      this.jdfPercentageNbRulesWithLessThanLPercentInputs.setBackground(Color.WHITE);
      this.jdfPercentageNbRulesWithLessThanLPercentInputs.setForeground(Color.BLUE);
      this.jdfPercentageNbRulesWithLessThanLPercentInputs.setEditable(false);
      jPanelInputsRB2.add(new JLabel("==>"), new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(15, 0, 5, 5), 0, 0));
      jPanelInputsRB2.add(this.jButtonPercentageNbRulesWithBetweenLAndMPercentInputs, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      jPanelInputsRB2.add(this.jdfPercentageNbRulesWithBetweenLAndMPercentInputs, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 15, 0));
      this.jdfPercentageNbRulesWithBetweenLAndMPercentInputs.setBackground(Color.WHITE);
      this.jdfPercentageNbRulesWithBetweenLAndMPercentInputs.setForeground(Color.BLUE);
      this.jdfPercentageNbRulesWithBetweenLAndMPercentInputs.setEditable(false);
      jPanelInputsRB2.add(new JLabel("==>"), new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 0, 0));
      jPanelInputsRB2.add(this.jButtonPercentageNbRulesWithMoreThanMPercentInputs, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      jPanelInputsRB2.add(this.jdfPercentageNbRulesWithMoreThanMPercentInputs, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 15, 0));
      this.jdfPercentageNbRulesWithMoreThanMPercentInputs.setBackground(Color.WHITE);
      this.jdfPercentageNbRulesWithMoreThanMPercentInputs.setForeground(Color.BLUE);
      this.jdfPercentageNbRulesWithMoreThanMPercentInputs.setEditable(false);
      jPanelInputsRB2.add(new JLabel("==>"), new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 0, 0));
      jPanelInputsRB2.add(this.jButtonNbTotalInputs, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(15, 5, 5, 5), 0, 0));
      jPanelInputsRB2.add(this.jdfNbTotalInputs, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(15, 0, 5, 5), 15, 0));
      this.jdfNbTotalInputs.setBackground(Color.WHITE);
      this.jdfNbTotalInputs.setForeground(Color.BLUE);
      this.jdfNbTotalInputs.setEditable(false);
      jPanelInputsRB2.add(new JLabel("==>"), new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(15, 0, 5, 5), 0, 0));
      jPanelInputsRB2.add(this.jButtonNbTotalUsedLabels, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(15, 5, 5, 5), 0, 0));
      jPanelInputsRB2.add(this.jdfNbTotalUsedLabels, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(15, 0, 5, 5), 15, 0));
      this.jdfNbTotalUsedLabels.setBackground(Color.WHITE);
      this.jdfNbTotalUsedLabels.setForeground(Color.BLUE);
      this.jdfNbTotalUsedLabels.setEditable(false);
      jPanelInputsRB2.add(new JLabel("==>"), new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(15, 0, 5, 5), 0, 0));
      jPanelInputsRB2.add(this.jButtonPercentageNbTotalElementaryUsedLabels, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(15, 5, 5, 5), 0, 0));
      jPanelInputsRB2.add(this.jdfPercentageNbTotalElementaryUsedLabels, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(15, 0, 5, 5), 15, 0));
      this.jdfPercentageNbTotalElementaryUsedLabels.setBackground(Color.WHITE);
      this.jdfPercentageNbTotalElementaryUsedLabels.setForeground(Color.BLUE);
      this.jdfPercentageNbTotalElementaryUsedLabels.setEditable(false);
      jPanelInputsRB2.add(new JLabel("==>"), new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(15, 0, 5, 5), 0, 0));
      jPanelInputsRB2.add(this.jButtonPercentageNbTotalORCompositeUsedLabels, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      jPanelInputsRB2.add(this.jdfPercentageNbTotalORCompositeUsedLabels, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 15, 0));
      this.jdfPercentageNbTotalORCompositeUsedLabels.setBackground(Color.WHITE);
      this.jdfPercentageNbTotalORCompositeUsedLabels.setForeground(Color.BLUE);
      this.jdfPercentageNbTotalORCompositeUsedLabels.setEditable(false);
      jPanelInputsRB2.add(new JLabel("==>"), new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 0, 0));
      jPanelInputsRB2.add(this.jButtonPercentageNbTotalNOTCompositeUsedLabels, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      jPanelInputsRB2.add(this.jdfPercentageNbTotalNOTCompositeUsedLabels, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 15, 0));
      this.jdfPercentageNbTotalNOTCompositeUsedLabels.setBackground(Color.WHITE);
      this.jdfPercentageNbTotalNOTCompositeUsedLabels.setForeground(Color.BLUE);
      this.jdfPercentageNbTotalNOTCompositeUsedLabels.setEditable(false);
      jPanelInputsRB2.add(new JLabel("==>"), new GridBagConstraints(2, 9, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 0, 0));
      jPanelRuleBaseRB2.add(this.jButtonRB211, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 15, 5), 0, 0));
      jPanelRuleBaseRB2.add(new JLabel("==>"), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0));
      jPanelRuleBaseRB2.add(this.jButtonRBstructuralDimension, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(60, 5, 40, 5), 0, 0));
      jPanelRuleBaseRB2.add(this.jdfRuleBaseStructuralDimension, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(60, 0, 40, 5), 15, 0));
      this.jdfRuleBaseStructuralDimension.setBackground(Color.WHITE);
      this.jdfRuleBaseStructuralDimension.setForeground(Color.BLUE);
      this.jdfRuleBaseStructuralDimension.setEditable(false);
      jPanelRuleBaseRB2.add(new JLabel("==>"), new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0));
      jPanelRuleBaseRB2.add(this.jButtonRB212, new GridBagConstraints(0, 2, 1, 1, 1.0, 2.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 15, 5), 0, 0));
      jPanelRuleBaseRB2.add(new JLabel("==>"), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      jPanelRuleBaseRB2.add(this.jButtonRBstructuralComplexity, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(40, 5, 40, 5), 0, 0));
      jPanelRuleBaseRB2.add(this.jdfRuleBaseStructuralComplexity, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(40, 0, 40, 5), 15, 0));
      this.jdfRuleBaseStructuralComplexity.setBackground(Color.WHITE);
      this.jdfRuleBaseStructuralComplexity.setForeground(Color.BLUE);
      this.jdfRuleBaseStructuralComplexity.setEditable(false);
      jPanelRuleBaseRB2.add(new JLabel("==>"), new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      jPanelRuleBaseRB2.add(this.jButtonRB221, new GridBagConstraints(0, 3, 1, 1, 1.0, 2.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 15, 5), 0, 0));
      jPanelRuleBaseRB2.add(new JLabel("==>"), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      jPanelRuleBaseRB2.add(this.jButtonRBconceptualDimension, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(40, 5, 40, 5), 0, 0));
      jPanelRuleBaseRB2.add(this.jdfRuleBaseConceptualDimension, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(40, 0, 40, 5), 15, 0));
      this.jdfRuleBaseConceptualDimension.setBackground(Color.WHITE);
      this.jdfRuleBaseConceptualDimension.setForeground(Color.BLUE);
      this.jdfRuleBaseConceptualDimension.setEditable(false);
      jPanelRuleBaseRB2.add(new JLabel("==>"), new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      jPanelRuleBaseRB2.add(this.jButtonRB222, new GridBagConstraints(0, 4, 1, 1, 1.0, 2.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 35, 5), 0, 0));
      jPanelRuleBaseRB2.add(new JLabel("==>"), new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 20, 0), 0, 0));
      jPanelRuleBaseRB2.add(this.jButtonRBconceptualComplexity, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(40, 5, 60, 5), 0, 0));
      jPanelRuleBaseRB2.add(this.jdfRuleBaseConceptualComplexity, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(40, 0, 60, 5), 15, 0));
      this.jdfRuleBaseConceptualComplexity.setBackground(Color.WHITE);
      this.jdfRuleBaseConceptualComplexity.setForeground(Color.BLUE);
      this.jdfRuleBaseConceptualComplexity.setEditable(false);
      jPanelRuleBaseRB2.add(new JLabel("==>"), new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 20, 0), 0, 0));
      jPanelRuleBaseRB21.add(this.jButtonRB213, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(60, 5, 50, 5), 0, 0));
      jPanelRuleBaseRB21.add(new JLabel("==>"), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      jPanelRuleBaseRB22.add(this.jButtonRBstructuralFramework, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(110, 5, 100, 5), 0, 0));
      jPanelRuleBaseRB22.add(this.jdfRuleBaseStructuralFramework, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(110, 5, 100, 5), 0, 0));
      this.jdfRuleBaseStructuralFramework.setBackground(Color.WHITE);
      this.jdfRuleBaseStructuralFramework.setForeground(Color.BLUE);
      this.jdfRuleBaseStructuralFramework.setEditable(false);
      jPanelRuleBaseRB22.add(new JLabel("==>"), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 0, 0, 0), 0, 0));
      jPanelRuleBaseRB21.add(this.jButtonRB223, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(20, 5, 60, 5), 0, 0));
      jPanelRuleBaseRB21.add(new JLabel("==>"), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 20, 0), 0, 0));
      jPanelRuleBaseRB22.add(this.jButtonRBconceptualFramework, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(100, 5, 110, 5), 0, 0));
      jPanelRuleBaseRB22.add(this.jdfRuleBaseConceptualFramework, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(100, 5, 110, 5), 0, 0));
      this.jdfRuleBaseConceptualFramework.setBackground(Color.WHITE);
      this.jdfRuleBaseConceptualFramework.setForeground(Color.BLUE);
      this.jdfRuleBaseConceptualFramework.setEditable(false);
      jPanelRuleBaseRB22.add(new JLabel("==>"), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0));
      jPanelRuleBase2.add(this.jButtonRB2, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(100, 5, 100, 5), 0, 0));
      jPanelRuleBase2.add(new JLabel("==>"), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      jPanelRuleBase2.add(this.jButtonRBInterpretability, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(220, 5, 220, 5), 0, 0));
      this.jdfRBInterpretability.setBackground(Color.WHITE);
      this.jdfRBInterpretability.setForeground(Color.BLUE);
      this.jdfRBInterpretability.setEditable(false);
      jPanelRuleBase2.add(this.jdfRBInterpretability, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(220, 5, 220, 5), 15, 0));
      jPanelRuleBase2.add(new JLabel("==>"), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      jPanelRuleBase3.add(this.jButtonRB3, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(20, 5, 200, 5), 0, 0));
      jPanelRuleBase3.add(new JLabel("==>"), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      jPanelRuleBase3.setVisible(false);

      this.jPanelInputs.add(jPanelInputsRB1, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
      this.jPanelInputs.add(jPanelRuleBase1, new GridBagConstraints(4, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
      this.jPanelInputs.add(jPanelInputsRB2, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 0), 0, 0));
      this.jPanelInputs.add(jPanelRuleBaseRB2, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
      this.jPanelInputs.add(jPanelRuleBaseRB21, new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
      this.jPanelInputs.add(jPanelRuleBaseRB22, new GridBagConstraints(3, 1, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
      this.jPanelInputs.add(jPanelRuleBase2, new GridBagConstraints(4, 1, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
      jPanelInputs.setVisible(false);

      this.jButtonInterpretabilityIndex.setForeground(Color.blue);
      jPanelOutputDim.add(this.jButtonInterpretabilityIndex, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(40, 0, 5, 5), 0, 0));
      this.jdfDimInterpretability.setBackground(Color.WHITE);
      this.jdfDimInterpretability.setForeground(Color.BLUE);
      this.jdfDimInterpretability.setEditable(false);
      jPanelOutputDim.add(this.jdfDimInterpretability, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(40, 0, 5, 5), 40, 0));
      this.jLabelInterpMsg.setForeground(Color.blue);
      this.jButtonViewAll.setSelected(false);
      jPanelOutputDim.add(this.jButtonViewAll, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 0, 0));
      jPanelOutputDim.add(this.jLabelInterpMsg, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(40, 10, 10, 10), 0, 0));
      this.jAllPanelDim.add(jPanelInputs, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      this.jAllPanelDim.add(jPanelRuleBase3, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      this.jAllPanelDim.add(jPanelOutputDim, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      
      this.jMainPanelComplex.getViewport().add(jAllPanelDim, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    } catch( Throwable t ) {
    	MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JKBInterpretabilityFrame in BuildResultsPanelDimension: "+t);
        this.dispose();
    }
  }
//------------------------------------------------------------------------------
  private void BuildResultsPanelInference() {
    try {
      this.jLabelRBbehaviourMsg.setForeground(Color.blue);
      this.jButtonViewRBbehaviour.setSelected(false);
      jPanelOutputInf.add(this.jButtonViewRBbehaviour, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 0, 0));
      jPanelOutputInf.add(this.jLabelRBbehaviourMsg, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

      this.jAllPanelInf.add(jPanelOutputInf, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

      this.jMainPanelInfer.getViewport().add(jAllPanelInf, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    } catch( Throwable t ) {
    	MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JKBInterpretabilityFrame in BuildResultsPanelInference: "+t);
        this.dispose();
    }
  }
//------------------------------------------------------------------------------
  private void computeBuildResultsPanelInference() {
    try {
      jPanelInputsInf.add(this.jButtonNbRulesTogetherFired2, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      this.jdfNbRulesTogetherFired2.setBackground(Color.WHITE);
      this.jdfNbRulesTogetherFired2.setForeground(Color.BLUE);
      this.jdfNbRulesTogetherFired2.setEditable(false);
      jPanelInputsInf.add(this.jdfNbRulesTogetherFired2, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 15, 0));
      jPanelInputsInf.add(new JLabel("==>"), new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      jPanelInputsInf.add(this.jButtonNbRulesTogetherFired3, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      this.jdfNbRulesTogetherFired3.setBackground(Color.WHITE);
      this.jdfNbRulesTogetherFired3.setForeground(Color.BLUE);
      this.jdfNbRulesTogetherFired3.setEditable(false);
      jPanelInputsInf.add(this.jdfNbRulesTogetherFired3, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 15, 0));
      jPanelInputsInf.add(new JLabel("==>"), new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      jPanelInputsInf.add(this.jButtonNbRulesTogetherFired4, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      this.jdfNbRulesTogetherFired4.setBackground(Color.WHITE);
      this.jdfNbRulesTogetherFired4.setForeground(Color.BLUE);
      this.jdfNbRulesTogetherFired4.setEditable(false);
      jPanelInputsInf.add(this.jdfNbRulesTogetherFired4, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 15, 0));
      jPanelInputsInf.add(new JLabel("==>"), new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

      jPanelKBI.add(this.jButtonRB4, new GridBagConstraints(0, 0, 1, 1, 1.0, 2.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      jPanelKBI.add(new JLabel("==>"), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

      this.jButtonInferInterpretability.setForeground(Color.blue);
      jPanelOutputInf.add(this.jButtonInferInterpretability, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 0, 0));
      this.jdfInfInterpretability.setBackground(Color.WHITE);
      this.jdfInfInterpretability.setForeground(Color.BLUE);
      this.jdfInfInterpretability.setEditable(false);
      jPanelOutputInf.add(this.jdfInfInterpretability, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 40, 0));
      this.jLabelRBbehaviourMsg.setForeground(Color.blue);
      this.jButtonViewRBbehaviour.setSelected(true);
      jPanelOutputInf.add(this.jButtonViewRBbehaviour, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 0, 0));
      jPanelOutputInf.add(this.jLabelRBbehaviourMsg, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

      this.jAllPanelInf.add(jPanelInputsInf, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
      this.jAllPanelInf.add(jPanelKBI, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 0, 0));
      this.jAllPanelInf.add(jPanelOutputInf, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

      this.jMainPanelInfer.getViewport().add(jAllPanelInf, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    } catch( Throwable t ) {
    	MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JKBInterpretabilityFrame in computeBuildResultsPanelInference: "+t);
        this.dispose();
    }
  }
//------------------------------------------------------------------------------
  private void BuildResultsPanelOtherIndices() {
    try {
      this.jdfNauckIndex.setValue(this.NauckIndex);
      this.jdfNauckIndex.setBackground(Color.WHITE);
      this.jdfNauckIndex.setForeground(Color.BLUE);
      this.jdfNauckIndex.setEditable(false);
      this.jPanelOtherIndices.add(this.jLabelNauckIndex, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
      this.jPanelOtherIndices.add(this.jdfNauckIndex, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 60, 0));
      this.jPanelOtherIndices.add(this.jLabelIshibuchiNbRules, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
      this.jPanelOtherIndices.add(this.jdfIshibuchiNbRules, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 60, 0));
      this.jdfIshibuchiNbRules.setValue(this.IshibuchiNbRules);
      this.jdfIshibuchiNbRules.setBackground(Color.WHITE);
      this.jdfIshibuchiNbRules.setForeground(Color.BLUE);
      this.jdfIshibuchiNbRules.setEditable(false);
      this.jPanelOtherIndices.add(this.jLabelIshibuchiTotalRuleLength, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
      this.jPanelOtherIndices.add(this.jdfIshibuchiTotalRuleLength, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 60, 0));
      this.jdfIshibuchiTotalRuleLength.setValue(this.IshibuchiTotalRuleLength);
      this.jdfIshibuchiTotalRuleLength.setBackground(Color.WHITE);
      this.jdfIshibuchiTotalRuleLength.setForeground(Color.BLUE);
      this.jdfIshibuchiTotalRuleLength.setEditable(false);
      this.jPanelOtherIndices.add(this.jLabelIshibuchiAverageRuleLength, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
      this.jPanelOtherIndices.add(this.jdfIshibuchiAverageRuleLength, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 60, 0));
      this.jdfIshibuchiAverageRuleLength.setValue(this.IshibuchiAverageRuleLength);
      this.jdfIshibuchiAverageRuleLength.setBackground(Color.WHITE);
      this.jdfIshibuchiAverageRuleLength.setForeground(Color.BLUE);
      this.jdfIshibuchiAverageRuleLength.setEditable(false);
      this.jPanelOtherIndices.add(this.jLabelAccumulatedRuleComplexity, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
      this.jPanelOtherIndices.add(this.jdfAccumulatedRuleComplexity, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 60, 0));
      this.jdfAccumulatedRuleComplexity.setValue(this.AccumulatedRuleComplexity);
      this.jdfAccumulatedRuleComplexity.setBackground(Color.WHITE);
      this.jdfAccumulatedRuleComplexity.setForeground(Color.BLUE);
      this.jdfAccumulatedRuleComplexity.setEditable(false);
      this.jPanelOtherIndices.add(this.jLabelAccumulatedRuleComplexitySC11, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
      this.jPanelOtherIndices.add(this.jdfAccumulatedRuleComplexitySC11, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 60, 0));
      this.jdfAccumulatedRuleComplexitySC11.setValue(this.AccumulatedRuleComplexitySC11);
      this.jdfAccumulatedRuleComplexitySC11.setBackground(Color.WHITE);
      this.jdfAccumulatedRuleComplexitySC11.setForeground(Color.BLUE);
      this.jdfAccumulatedRuleComplexitySC11.setEditable(false);
      this.jPanelOtherIndices.add(this.jLabelAverageFiredRulesTheory, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
      this.jPanelOtherIndices.add(this.jdfAverageFiredRulesTheory, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 60, 0));
      this.jdfAverageFiredRulesTheory.setValue(this.AverageFiredRulesTheory);
      this.jdfAverageFiredRulesTheory.setBackground(Color.WHITE);
      this.jdfAverageFiredRulesTheory.setForeground(Color.BLUE);
      this.jdfAverageFiredRulesTheory.setEditable(false);
      this.jPanelOtherIndices.add(this.jLabelMinFiredRulesTheory, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
      this.jPanelOtherIndices.add(this.jdfMinFiredRulesTheory, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 60, 0));
      this.jdfMinFiredRulesTheory.setValue(this.MinFiredRulesTheory);
      this.jdfMinFiredRulesTheory.setBackground(Color.WHITE);
      this.jdfMinFiredRulesTheory.setForeground(Color.BLUE);
      this.jdfMinFiredRulesTheory.setEditable(false);
      this.jPanelOtherIndices.add(this.jLabelMaxFiredRulesTheory, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
      this.jPanelOtherIndices.add(this.jdfMaxFiredRulesTheory, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 60, 0));
      this.jdfMaxFiredRulesTheory.setValue(this.MaxFiredRulesTheory);
      this.jdfMaxFiredRulesTheory.setBackground(Color.WHITE);
      this.jdfMaxFiredRulesTheory.setForeground(Color.BLUE);
      this.jdfMaxFiredRulesTheory.setEditable(false);
      this.jPanelOtherIndices.add(this.jLabelAverageFiredRulesTraining, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
      this.jPanelOtherIndices.add(this.jdfAverageFiredRulesTraining, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 60, 0));
      this.jdfAverageFiredRulesTraining.setValue(this.AverageFiredRulesTraining);
      this.jdfAverageFiredRulesTraining.setBackground(Color.WHITE);
      this.jdfAverageFiredRulesTraining.setForeground(Color.BLUE);
      this.jdfAverageFiredRulesTraining.setEditable(false);
      this.jPanelOtherIndices.add(this.jLabelMinFiredRulesTraining, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
      this.jPanelOtherIndices.add(this.jdfMinFiredRulesTraining, new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 60, 0));
      this.jdfMinFiredRulesTraining.setValue(this.MinFiredRulesTraining);
      this.jdfMinFiredRulesTraining.setBackground(Color.WHITE);
      this.jdfMinFiredRulesTraining.setForeground(Color.BLUE);
      this.jdfMinFiredRulesTraining.setEditable(false);
      this.jPanelOtherIndices.add(this.jLabelMaxFiredRulesTraining, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
      this.jPanelOtherIndices.add(this.jdfMaxFiredRulesTraining, new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 60, 0));
      this.jdfMaxFiredRulesTraining.setValue(this.MaxFiredRulesTraining);
      this.jdfMaxFiredRulesTraining.setBackground(Color.WHITE);
      this.jdfMaxFiredRulesTraining.setForeground(Color.BLUE);
      this.jdfMaxFiredRulesTraining.setEditable(false);
      this.jPanelOtherIndices.add(this.jLabelLVindexTraining, new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
      this.jPanelOtherIndices.add(this.jdfLVindexTraining, new GridBagConstraints(1, 12, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 60, 0));
      this.jdfLVindexTraining.setValue(this.LVindexTraining);
      this.jdfLVindexTraining.setBackground(Color.WHITE);
      this.jdfLVindexTraining.setForeground(Color.BLUE);
      this.jdfLVindexTraining.setEditable(false);
      if (!this.warningAbort) {
          this.jPanelOtherIndices.add(this.jLabelAverageFiredRulesTest, new GridBagConstraints(0, 13, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
          this.jPanelOtherIndices.add(this.jdfAverageFiredRulesTest, new GridBagConstraints(1, 13, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 60, 0));
          this.jdfAverageFiredRulesTest.setValue(this.AverageFiredRulesTest);
          this.jdfAverageFiredRulesTest.setBackground(Color.WHITE);
          this.jdfAverageFiredRulesTest.setForeground(Color.BLUE);
          this.jdfAverageFiredRulesTest.setEditable(false);
          this.jPanelOtherIndices.add(this.jLabelMinFiredRulesTest, new GridBagConstraints(0, 14, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
          this.jPanelOtherIndices.add(this.jdfMinFiredRulesTest, new GridBagConstraints(1, 14, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 60, 0));
          this.jdfMinFiredRulesTest.setValue(this.MinFiredRulesTest);
          this.jdfMinFiredRulesTest.setBackground(Color.WHITE);
          this.jdfMinFiredRulesTest.setForeground(Color.BLUE);
          this.jdfMinFiredRulesTest.setEditable(false);
          this.jPanelOtherIndices.add(this.jLabelMaxFiredRulesTest, new GridBagConstraints(0, 15, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
          this.jPanelOtherIndices.add(this.jdfMaxFiredRulesTest, new GridBagConstraints(1, 15, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 60, 0));
          this.jdfMaxFiredRulesTest.setValue(this.MaxFiredRulesTest);
          this.jdfMaxFiredRulesTest.setBackground(Color.WHITE);
          this.jdfMaxFiredRulesTest.setForeground(Color.BLUE);
          this.jdfMaxFiredRulesTest.setEditable(false);
          this.jPanelOtherIndices.add(this.jLabelLVindexTest, new GridBagConstraints(0, 16, 1, 1, 0.0, 0.0
                  ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
          this.jPanelOtherIndices.add(this.jdfLVindexTest, new GridBagConstraints(1, 16, 1, 1, 0.0, 0.0
                  ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 60, 0));
          this.jdfLVindexTest.setValue(this.LVindexTest);
          this.jdfLVindexTest.setBackground(Color.WHITE);
          this.jdfLVindexTest.setForeground(Color.BLUE);
          this.jdfLVindexTest.setEditable(false);
      }
      this.jMainPanelOtherIndices.getViewport().add(jPanelOtherIndices, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    } catch( Throwable t ) {
        t.printStackTrace();
    	MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JKBInterpretabilityFrame in BuildResultsPanelOtherIndices: "+t);
        this.dispose();
    }
  }
//------------------------------------------------------------------------------
  private void ComputeInterpretability() {
	    //System.out.println("JKBInterpretabilityFrame 3.1.1");
    this.NbTotalRules= this.kbct.GetNbActiveRules();
    //System.out.println("JKBInterpretabilityFrame 3.1.2");
    int NbInputs= this.kbct.GetNbInputs();
    int[] UsedInputsByRule= this.UsedInputsByRule((int)this.NbTotalRules, NbInputs);
    this.NbTotalPremises= this.TotalSum(UsedInputsByRule);
    int MaxNbInputs= MainKBCT.getConfig().GetNumberOfInputs();
    double LPercentInputs= this.Lpercent*MaxNbInputs/100;
    double MPercentInputs=this.Mpercent*MaxNbInputs/100;
    int cont1=0;
    int cont2=0;
    int cont3=0;
    for (int n=0; n<UsedInputsByRule.length; n++) {
        if (UsedInputsByRule[n]<=LPercentInputs)
          cont1++;
        else if ( (UsedInputsByRule[n] > LPercentInputs) && (UsedInputsByRule[n] <= MPercentInputs) )
          cont2++;
        else if (UsedInputsByRule[n] > MPercentInputs)
          cont3++;
    }
    this.PercentageNbRulesWithLessThanLPercentInputs= 100*cont1/this.NbTotalRules;
    this.PercentageNbRulesWithBetweenLAndMPercentInputs= 100*cont2/this.NbTotalRules;
    this.PercentageNbRulesWithMoreThanMPercentInputs= 100*cont3/this.NbTotalRules;
    this.NbTotalInputs= NbInputs;
    int[] DefinedLabelsByInput= this.DefindedLabelsByInput(NbInputs);
    this.NbTotalDefinedLabels= this.TotalSum(DefinedLabelsByInput);
    int[] UsedLabelsByInput= this.UsedLabelsByInput(NbInputs, "All");
    this.NbTotalLabelsUsedByInput= this.TotalSum(UsedLabelsByInput);
    int[] UsedElementaryLabelsByInput= this.UsedLabelsByInput(NbInputs, "Elementary");
    this.PercentageNbTotalElementaryLabelsUsedByInput= 100*this.TotalSum(UsedElementaryLabelsByInput)/this.NbTotalLabelsUsedByInput;
    int[] UsedNOTCompositeLabelsByInput= this.UsedLabelsByInput(NbInputs, "NOTComposite");
    this.PercentageNbTotalNOTCompositeLabelsUsedByInput= 100*this.TotalSum(UsedNOTCompositeLabelsByInput)/this.NbTotalLabelsUsedByInput;
    int[] UsedORCompositeLabelsByInput= this.UsedLabelsByInput(NbInputs, "ORComposite");
    this.PercentageNbTotalORCompositeLabelsUsedByInput= 100*this.TotalSum(UsedORCompositeLabelsByInput)/this.NbTotalLabelsUsedByInput;
    double[] inputs= new double[12];
    inputs[0]= this.NbTotalDefinedLabels;
    inputs[1]= this.PartitionInterpretability; 
    inputs[2]= this.NbTotalRules; 
    inputs[3]= this.NbTotalPremises; 
    inputs[4]= this.PercentageNbRulesWithLessThanLPercentInputs; 
    inputs[5]= this.PercentageNbRulesWithBetweenLAndMPercentInputs; 
    inputs[6]= this.PercentageNbRulesWithMoreThanMPercentInputs; 
    inputs[7]= this.NbTotalInputs;
    inputs[8]= this.NbTotalLabelsUsedByInput;
    inputs[9]= this.PercentageNbTotalElementaryLabelsUsedByInput;
    inputs[10]= this.PercentageNbTotalORCompositeLabelsUsedByInput;
    inputs[11]= this.PercentageNbTotalNOTCompositeLabelsUsedByInput;
    //System.out.println("JKBInterpretabilityFrame 3.1.3");
    this.ComputeInterpretability(inputs);
    //System.out.println("JKBInterpretabilityFrame 3.1.4");
  }
//------------------------------------------------------------------------------
  /** inputs
   * in1: total number of defined labels
   * in2: partition interpretability (SFP=1)
   * in3: total number of rules
   * in4: total number of premises
   * in5: percentage of rules which use less than 10% of inputs
   * in6: percentage of rules which between 10 and 30 percent of inputs
   * in7: percentage of rules which use more than 30% of inputs
   * in8: total number of inputs
   * in9: total number of used labels (in the rule base)
   * in10: percentage of elementary labels
   * in11: percentage of NOT composite labels
   * in12: percentage of OR composite labels
	**/
  private void ComputeInterpretability(double[] inputs) {
    MessageKBCT.WriteLogFile((LocaleKBCT.GetString("Interpretability")).toUpperCase(), "Quality");
    Date d= new Date(System.currentTimeMillis());
    MessageKBCT.WriteLogFile("----------------------------------", "Quality");
    MessageKBCT.WriteLogFile("time begin -> "+DateFormat.getTimeInstance().format(d), "Quality");
    try {
      // KB1
      // Data base interpretability
      this.InterpKB1_DataBaseInterpretability= this.BuildInterpretabilityKB1_DataBaseInterpretability();
      this.FIS_KB1_DataBaseInterpretability= new double[2];
      this.FIS_KB1_DataBaseInterpretability[0]= inputs[0];
      this.FIS_KB1_DataBaseInterpretability[1]= inputs[1];
      this.InterpKB1_DataBaseInterpretability.Infer(this.FIS_KB1_DataBaseInterpretability);
      //System.out.println("JKBInterpretabilityFrame 3.1.3.2");
      double[] outputsKB1_DataBaseInterpretability= InterpKB1_DataBaseInterpretability.SortiesObtenues();
      this.jdfDBInterpretability.setValue(outputsKB1_DataBaseInterpretability[0]);
      
      // KB211
      // Rule Base Structural Dimension
      this.InterpKB211_RuleBaseStructuralDimension= this.BuildInterpretabilityKB211_RuleBaseStructuralDimension();
      this.FIS_KB211_RuleBaseStructuralDimension= new double[2];
      this.FIS_KB211_RuleBaseStructuralDimension[0]= inputs[2];
      this.FIS_KB211_RuleBaseStructuralDimension[1]= inputs[3];
      this.InterpKB211_RuleBaseStructuralDimension.Infer(this.FIS_KB211_RuleBaseStructuralDimension);
      double[] outputsKB211_RuleBaseStructuralDimension= this.InterpKB211_RuleBaseStructuralDimension.SortiesObtenues();
      this.jdfRuleBaseStructuralDimension.setValue(outputsKB211_RuleBaseStructuralDimension[0]);
      
      // KB212
      // Rule Base Structural Complexity
      this.InterpKB212_RuleBaseStructuralComplexity= this.BuildInterpretabilityKB212_RuleBaseStructuralComplexity();
      this.FIS_KB212_RuleBaseStructuralComplexity= new double[3];
      this.FIS_KB212_RuleBaseStructuralComplexity[0]= inputs[4];
      this.FIS_KB212_RuleBaseStructuralComplexity[1]= inputs[5];
      this.FIS_KB212_RuleBaseStructuralComplexity[2]= inputs[6];
      this.InterpKB212_RuleBaseStructuralComplexity.Infer(this.FIS_KB212_RuleBaseStructuralComplexity);
      double[] outputsKB212_RuleBaseStructuralComplexity= this.InterpKB212_RuleBaseStructuralComplexity.SortiesObtenues();
      this.jdfRuleBaseStructuralComplexity.setValue(outputsKB212_RuleBaseStructuralComplexity[0]);

      // KB213
      // Rule Base Structural Framework
      this.InterpKB213_RuleBaseStructuralFramework= this.BuildInterpretabilityKB213_RuleBaseStructuralFramework();
      this.FIS_KB213_RuleBaseStructuralFramework= new double[2];
      this.FIS_KB213_RuleBaseStructuralFramework[0]= outputsKB211_RuleBaseStructuralDimension[0];
      this.FIS_KB213_RuleBaseStructuralFramework[1]= outputsKB212_RuleBaseStructuralComplexity[0];
      this.InterpKB213_RuleBaseStructuralFramework.Infer(this.FIS_KB213_RuleBaseStructuralFramework);
      double[] outputsKB213_RuleBaseStructuralFramework= this.InterpKB213_RuleBaseStructuralFramework.SortiesObtenues();
      this.jdfRuleBaseStructuralFramework.setValue(outputsKB213_RuleBaseStructuralFramework[0]);

      // KB221
      // Rule Base Conceptual Dimension
      this.InterpKB221_RuleBaseConceptualDimension= this.BuildInterpretabilityKB221_RuleBaseConceptualDimension();
      this.FIS_KB221_RuleBaseConceptualDimension= new double[2];
      this.FIS_KB221_RuleBaseConceptualDimension[0]= inputs[7];
      this.FIS_KB221_RuleBaseConceptualDimension[1]= inputs[8];
      this.InterpKB221_RuleBaseConceptualDimension.Infer(this.FIS_KB221_RuleBaseConceptualDimension);
      double[] outputsKB221_RuleBaseConceptualDimension= this.InterpKB221_RuleBaseConceptualDimension.SortiesObtenues();
      this.jdfRuleBaseConceptualDimension.setValue(outputsKB221_RuleBaseConceptualDimension[0]);
      
      // KB222
      // Rule Base Conceptual Complexity
      this.InterpKB222_RuleBaseConceptualComplexity= this.BuildInterpretabilityKB222_RuleBaseConceptualComplexity();
      this.FIS_KB222_RuleBaseConceptualComplexity= new double[3];
      this.FIS_KB222_RuleBaseConceptualComplexity[0]= inputs[9];
      this.FIS_KB222_RuleBaseConceptualComplexity[1]= inputs[10];
      this.FIS_KB222_RuleBaseConceptualComplexity[2]= inputs[11];
      this.InterpKB222_RuleBaseConceptualComplexity.Infer(this.FIS_KB222_RuleBaseConceptualComplexity);
      double[] outputsKB222_RuleBaseConceptualComplexity= this.InterpKB222_RuleBaseConceptualComplexity.SortiesObtenues();
      this.jdfRuleBaseConceptualComplexity.setValue(outputsKB222_RuleBaseConceptualComplexity[0]);

      // KB223
      // Rule Base Conceptual Framework
      this.InterpKB223_RuleBaseConceptualFramework= this.BuildInterpretabilityKB223_RuleBaseConceptualFramework();
      this.FIS_KB223_RuleBaseConceptualFramework= new double[2];
      this.FIS_KB223_RuleBaseConceptualFramework[0]= outputsKB221_RuleBaseConceptualDimension[0];
      this.FIS_KB223_RuleBaseConceptualFramework[1]= outputsKB222_RuleBaseConceptualComplexity[0];
      this.InterpKB223_RuleBaseConceptualFramework.Infer(this.FIS_KB223_RuleBaseConceptualFramework);
      double[] outputsKB223_RuleBaseConceptualFramework= this.InterpKB223_RuleBaseConceptualFramework.SortiesObtenues();
      this.jdfRuleBaseConceptualFramework.setValue(outputsKB223_RuleBaseConceptualFramework[0]);
      
      // KB2
      // Rule base interpretability
      this.InterpKB2_RuleBaseInterpretability= this.BuildInterpretabilityKB2_RuleBaseInterpretability();
      this.FIS_KB2_RuleBaseInterpretability= new double[2];
      this.FIS_KB2_RuleBaseInterpretability[0]= outputsKB213_RuleBaseStructuralFramework[0];
      this.FIS_KB2_RuleBaseInterpretability[1]= outputsKB223_RuleBaseConceptualFramework[0];
      this.InterpKB2_RuleBaseInterpretability.Infer(this.FIS_KB2_RuleBaseInterpretability);
      double[] outputsKB2_RuleBaseInterpretability= this.InterpKB2_RuleBaseInterpretability.SortiesObtenues();
      this.jdfRBInterpretability.setValue(outputsKB2_RuleBaseInterpretability[0]);

      // KB3
      // Interpretability Index
      this.InterpKB3_InterpretabilityIndex= this.BuildInterpretabilityKB3_InterpretabilityIndex();
      this.FIS_KB3_InterpretabilityIndex= new double[2];
      this.FIS_KB3_InterpretabilityIndex[0]= outputsKB1_DataBaseInterpretability[0];
      this.FIS_KB3_InterpretabilityIndex[1]= outputsKB2_RuleBaseInterpretability[0];
      this.InterpKB3_InterpretabilityIndex.Infer(this.FIS_KB3_InterpretabilityIndex);
      double[] outputsKB3_InterpretabilityIndex= this.InterpKB3_InterpretabilityIndex.SortiesObtenues();
      //this.jdfDimInterpretability.setValue(outputsKB3_InterpretabilityIndex[0]);
      this.InterpIndex= outputsKB3_InterpretabilityIndex[0];
      
      JKBCTOutput out= this.kbctDataBase[8].GetOutput(1);
      int labFired= out.GetLabelFired(this.InterpIndex);
      this.IntLabel= out.GetLabelsName(labFired-1);
      this.jdfNbTotalDefinedLabels.setValue(inputs[0]);
      this.jdfPartitionInterpretability.setValue(inputs[1]);
      this.jdfTotalNbRules.setValue(inputs[2]);
      this.jdfTotalNbPremises.setValue(inputs[3]);
      this.jdfPercentageNbRulesWithLessThanLPercentInputs.setValue(inputs[4]);
      this.jdfPercentageNbRulesWithBetweenLAndMPercentInputs.setValue(inputs[5]);
      this.jdfPercentageNbRulesWithMoreThanMPercentInputs.setValue(inputs[6]);
      this.jdfNbTotalInputs.setValue(inputs[7]);
      this.jdfNbTotalUsedLabels.setValue(inputs[8]);
      this.jdfPercentageNbTotalElementaryUsedLabels.setValue(inputs[9]);
      this.jdfPercentageNbTotalORCompositeUsedLabels.setValue(inputs[10]);
      this.jdfPercentageNbTotalNOTCompositeUsedLabels.setValue(inputs[11]);
      this.jdfDimInterpretability.setValue(this.InterpIndex);
      if (!this.IntLabel.equals(""))
          this.jLabelInterpMsg.setText("("+LocaleKBCT.GetString(this.IntLabel)+")");
      else
          this.jLabelInterpMsg.setText("("+this.IntLabel+")");
    	  
      String data=this.jdfNbTotalDefinedLabels.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("NbTotalDefinedLabels")+"= "+this.jdfNbTotalDefinedLabels.getValue(), "Quality");
      data=data+this.jdfPartitionInterpretability.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("PartitionInterpretability")+"= "+this.jdfPartitionInterpretability.getValue(), "Quality");
      data=data+this.jdfDBInterpretability.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("outputKBDataBaseInterpretability")+"= "+this.jdfDBInterpretability.getValue(), "Quality");
      data=data+this.jdfTotalNbRules.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("NbTotalRules")+"= "+this.jdfTotalNbRules.getValue(), "Quality");
      data=data+this.jdfTotalNbPremises.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("NbTotalPremises")+"= "+this.jdfTotalNbPremises.getValue(), "Quality");
      data=data+this.jdfRuleBaseStructuralDimension.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("outputKBRuleBaseStructuralDimension")+"= "+this.jdfRuleBaseStructuralDimension.getValue(), "Quality");
      data=data+this.jdfPercentageNbRulesWithLessThanLPercentInputs.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("PercentageNbRulesWithLessThanLPercentInputs")+"= "+this.jdfPercentageNbRulesWithLessThanLPercentInputs.getValue(), "Quality");
      data=data+this.jdfPercentageNbRulesWithBetweenLAndMPercentInputs.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("PercentageNbRulesWithBetweenLAndMPercentInputs")+"= "+this.jdfPercentageNbRulesWithBetweenLAndMPercentInputs.getValue(), "Quality");
      data=data+this.jdfPercentageNbRulesWithMoreThanMPercentInputs.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("PercentageNbRulesWithMoreThanMPercentInputs")+"= "+this.jdfPercentageNbRulesWithMoreThanMPercentInputs.getValue(), "Quality");
      data=data+this.jdfRuleBaseStructuralComplexity.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("outputKBRuleBaseStructuralComplexity")+"= "+this.jdfRuleBaseStructuralComplexity.getValue(), "Quality");
      data=data+this.jdfRuleBaseStructuralFramework.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("outputKBRuleBaseStructuralFramework")+"= "+this.jdfRuleBaseStructuralFramework.getValue(), "Quality");
      data=data+this.jdfNbTotalInputs.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("NbTotalInputs")+"= "+this.jdfNbTotalInputs.getValue(), "Quality");
      data=data+this.jdfNbTotalUsedLabels.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("NbTotalUsedLabels")+"= "+this.jdfNbTotalUsedLabels.getValue(), "Quality");
      data=data+this.jdfRuleBaseConceptualDimension.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("outputKBRuleBaseConceptualDimension")+"= "+this.jdfRuleBaseConceptualDimension.getValue(), "Quality");
      data=data+this.jdfPercentageNbTotalElementaryUsedLabels.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("PercentageNbTotalElementaryUsedLabels")+"= "+this.jdfPercentageNbTotalElementaryUsedLabels.getValue(), "Quality");
      data=data+this.jdfPercentageNbTotalORCompositeUsedLabels.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("PercentageNbTotalORCompositeUsedLabels")+"= "+this.jdfPercentageNbTotalORCompositeUsedLabels.getValue(), "Quality");
      data=data+this.jdfPercentageNbTotalNOTCompositeUsedLabels.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("PercentageNbTotalNOTCompositeUsedLabels")+"= "+this.jdfPercentageNbTotalNOTCompositeUsedLabels.getValue(), "Quality");
      data=data+this.jdfRuleBaseConceptualComplexity.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("outputKBRuleBaseConceptualComplexity")+"= "+this.jdfRuleBaseConceptualComplexity.getValue(), "Quality");
      data=data+this.jdfRuleBaseConceptualFramework.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("outputKBRuleBaseConceptualFramework")+"= "+this.jdfRuleBaseConceptualFramework.getValue(), "Quality");
      data=data+this.jdfRBInterpretability.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("outputKBRuleBaseInterpretability")+"= "+this.jdfRBInterpretability.getValue(), "Quality");
      data=data+this.jdfDimInterpretability.getValue()+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"= "+this.jdfDimInterpretability.getValue(), "Quality");
      if (!this.IntLabel.equals(""))
          data=data+LocaleKBCT.GetString(this.IntLabel)+"	";
      else 
          data=data+this.IntLabel+"	";
    	  
      if (!this.IntLabel.equals(""))
          MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("Label")+"= "+LocaleKBCT.GetString(this.IntLabel), "Quality");

      MessageKBCT.WriteLogFile("	**********************************************", "Quality");
      data=data+df.format(this.jdfDimInterpretability.getValue())+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(HILK)= "+df.format(this.jdfDimInterpretability.getValue()), "Quality");
  	  double inputsNb= this.kbct.GetNbInputs();
      if (!this.fingramsFlag) {
      double comp= this.kbct.GetOutput(1).GetLabelsNumber()/this.NbTotalPremises;
  	  double sum=0;
  	  for (int n=0; n<inputsNb; n++) {
          int NbL= this.kbct.GetInput(n+1).GetLabelsNumber();
          double a=NbL-1;
          double v=1/a;
  		  sum= sum + v;
  	  }
      double part=sum/inputsNb;
      double cov=1;
      this.NauckIndex= comp*part*cov;
      data=data+df.format(this.NauckIndex)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(Nauck)= "+df.format(this.NauckIndex), "Quality");
      }
      this.IshibuchiNbRules= this.kbct.GetNbActiveRules();
      data=data+df.format(this.IshibuchiNbRules)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(Rules)= "+df.format(this.IshibuchiNbRules), "Quality");
      int[] UsedInputsByRule= this.UsedInputsByRule((int)this.IshibuchiNbRules, (int)inputsNb);
      this.IshibuchiTotalRuleLength= this.TotalSum(UsedInputsByRule);
      data=data+df.format(this.IshibuchiTotalRuleLength)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(Total rule length)= "+df.format(this.IshibuchiTotalRuleLength), "Quality");
      if (!this.fingramsFlag) {
      this.IshibuchiAverageRuleLength= this.IshibuchiTotalRuleLength/this.IshibuchiNbRules;
      data=data+df.format(this.IshibuchiAverageRuleLength)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(Average rule length)= "+df.format(this.IshibuchiAverageRuleLength), "Quality");
      }
      int NbInputs=this.kbct.GetNbInputs();
      int[] NbLabelsPerInput= new int[NbInputs];
      for (int n=0; n<NbInputs; n++) {
           NbLabelsPerInput[n]= this.kbct.GetInput(n+1).GetLabelsNumber();
      }
      this.AccumulatedRuleComplexity=0;
      for (int n=0; n<this.IshibuchiNbRules; n++) {
   	       Rule r= this.kbct.GetRule(n+1); 
   	       this.AccumulatedRuleComplexity= this.AccumulatedRuleComplexity + this.computeInterpLocalWeightRule(r, NbLabelsPerInput);
      }
      data=data+df.format(this.AccumulatedRuleComplexity)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(Accumulated rule complexity)= "+df.format(this.AccumulatedRuleComplexity), "Quality");
      if (!this.fingramsFlag) {
      this.AccumulatedRuleComplexitySC11=0;
      for (int n=0; n<this.IshibuchiNbRules; n++) {
    	   Rule r= this.kbct.GetRule(n+1); 
    	   this.AccumulatedRuleComplexitySC11= this.AccumulatedRuleComplexitySC11 + this.computeInterpLocalWeightRuleSC11(r, NbLabelsPerInput);
      }
      data=data+df.format(this.AccumulatedRuleComplexitySC11)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(Accumulated Rule Complexity) (SC2011)= "+df.format(this.AccumulatedRuleComplexitySC11), "Quality");
      }
      File temp1= JKBCTFrame.BuildFile("temprbqCompleteness.fis");
      String fis_name= temp1.getAbsolutePath();
      JKBCT kbctinfer= new JKBCT(this.kbct);
      kbctinfer.SetKBCTFile(this.kbct.GetKBCTFile());
      kbctinfer.Save();
      kbctinfer.SaveFISquality(fis_name);
      JFIS fis= new JFIS(fis_name);
      int nbR= fis.NbActiveRules();
      double[][] datavaluesTraining= this.ddtrain.GetData();
      int NbDataTraining= this.ddtrain.GetActiveCount();
      int contfrTraining=0;
      int contdfrTraining=0;
      this.MinFiredRulesTraining=0;
      this.MaxFiredRulesTraining=0;
      boolean warning= false;
      this.maxSFR= new int[nbR];
      double limBT= MainKBCT.getConfig().GetBlankThres();
      for (int option=0; option<NbDataTraining; option++) {
  	  	  int contp=0;
  	  	  boolean[] firedRules= new boolean[nbR];
    	  fis.Infer(datavaluesTraining[option]);
    	  for (int n=0; n<nbR; n++) {
    		  double poids= fis.GetRule(n).Poids();
              if (poids > limBT) {
            	  contp++;
            	  firedRules[n]= true;
              }
    	  }
    	  if (contp > 0) {
    		  contdfrTraining++;
        	  for (int n=0; n<nbR; n++) {
                   if (firedRules[n]) {
                	   if (this.maxSFR[n] < contp)
                		   this.maxSFR[n]= contp;
                   }
        	  }
    	  }
    	  if (!warning && contp==1) {
    		  this.MinFiredRulesTraining= contp;
    		  warning=true;
    	  }
    	  if (contp > this.MaxFiredRulesTraining) {
    		  this.MaxFiredRulesTraining= contp;
    	  }
  	      contfrTraining= contfrTraining+contp;
      }
	  File tempkbini= JKBCTFrame.BuildFile("kb_unred.kb.xml");
	  File tempkbend= JKBCTFrame.BuildFile("kb_espresso_red.kb.xml");
      JLogicalView jlv= new JLogicalView(this.kbct, tempkbini, tempkbend);
      boolean[] res= jlv.computeLVindex(this.ddtrain);
      if (!res[0] && !res[1])
          this.LVindexTraining= jlv.getLVindex();
      ////////////
      int contfrTest=0;
      int contdfrTest=0;
      if (!this.fingramsFlag) {
      int NbDataTest= 0;
      if (!this.warningAbort) {
          double[][] datavaluesTest= this.ddtest.GetData();
          this.MinFiredRulesTest=0;
          this.MaxFiredRulesTest=0;
          warning= false;
          NbDataTest= this.ddtest.GetActiveCount();
          for (int option=0; option<NbDataTest; option++) {
      	  	  int contp=0;
        	  fis.Infer(datavaluesTest[option]);
        	  for (int n=0; n<nbR; n++) {
        		  
        		  double poids= fis.GetRule(n).Poids();
                  if (poids > 0) {
                	  contp++;
                  }
        	  }
        	  if (contp>0) 
        		  contdfrTest++;
        	  if (!warning && contp==1) {
        		  this.MinFiredRulesTest= contp;
        		  warning=true;
        	  }
        	  if (contp > this.MaxFiredRulesTest) {
        		  this.MaxFiredRulesTest= contp;
        	  }
      	      contfrTest= contfrTest+contp;
          }
          res= jlv.computeLVindex(this.ddtest);
          if (!res[0] && !res[1])
              this.LVindexTest= jlv.getLVindex();
      }
      jlv=null;
      double[] frt= this.evaluateInterpretabilityExplanationMaximum(this.kbct);
      this.AverageFiredRulesTheory= frt[0];
      data=data+df.format(this.AverageFiredRulesTheory)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(Theoretical Average fired rules)= "+df.format(this.AverageFiredRulesTheory), "Quality");
      this.MinFiredRulesTheory= frt[1];
      data=data+df.format(this.MinFiredRulesTheory)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(Theoretical Min fired rules)= "+df.format(this.MinFiredRulesTheory), "Quality");
      this.MaxFiredRulesTheory= frt[2];
      data=data+df.format(this.MaxFiredRulesTheory)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(Theoretical Max fired rules)= "+df.format(this.MaxFiredRulesTheory), "Quality");
    
	  this.AverageFiredRulesTraining= (double)contfrTraining/contdfrTraining;
      data=data+df.format(this.AverageFiredRulesTraining)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(Average inferential fired rules - training data)= "+df.format(this.AverageFiredRulesTraining), "Quality");
      }
      data=data+df.format(this.MinFiredRulesTraining)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(Minimum inferential fired rules - training data)= "+this.MinFiredRulesTraining, "Quality");
      data=data+df.format(this.MaxFiredRulesTraining)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(Maximum inferential fired rules - training data)= "+this.MaxFiredRulesTraining, "Quality");
      data=data+df.format(this.LVindexTraining)+"	";
      MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(Logical View index - training data)= "+this.LVindexTraining, "Quality");
      if (!this.fingramsFlag) {
      if (!this.warningAbort) {
    	  this.AverageFiredRulesTest= (double)contfrTest/contdfrTest;
          data=data+df.format(this.AverageFiredRulesTest)+"	";
          MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(Average inferential fired rules - test data)= "+df.format(this.AverageFiredRulesTest), "Quality");
          data=data+df.format(this.MinFiredRulesTest)+"	";
          MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(Minimum inferential fired rules - test data)= "+this.MinFiredRulesTest, "Quality");
          data=data+df.format(this.MaxFiredRulesTest)+"	";
          MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(Maximum inferential fired rules - test data)= "+this.MaxFiredRulesTest, "Quality");
          data=data+df.format(this.LVindexTest)+"	";
          MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("InterpretabilityIndex")+"(Logical View index - test data)= "+this.LVindexTest, "Quality");
      }
      }
      MessageKBCT.WriteLogFile("	**********************************************", "Quality");
      MessageKBCT.WriteLogFile("	EXCEL", "Quality");
      MessageKBCT.WriteLogFile("	"+data.replace(".",","), "Quality");
      // todo se guarda en LogQuality
 	  // en LOGint solo lo del EXCEL
 	  MessageKBCT.BuildLogFile(JKBCTFrame.BuildFile("LOGint.txt").getAbsolutePath(),null,null,null, "Interpretability");
      MessageKBCT.WriteLogFile(data.replace(".",","), "Interpretability"); // LOGint.txt
      MessageKBCT.WriteLogFile("	**********************************************", "Quality");
      d= new Date(System.currentTimeMillis());
      MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "Quality");
      kbctinfer.Close();
      kbctinfer.Delete();
      fis.Close();
      fis.Delete();
    } catch (Throwable t) {
        t.printStackTrace();
    	MessageKBCT.Error(null, t);
        this.InterpIndex=0.0;
        this.IntLabel= "very_low";
        this.RBbehaviour=0.0;
        this.InferLabel= "very_low";
        this.jdfNbTotalDefinedLabels.setValue(0);
        this.jdfPartitionInterpretability.setValue(0);
        this.jdfTotalNbRules.setValue(0);
        this.jdfTotalNbPremises.setValue(0);
        this.jdfPercentageNbRulesWithLessThanLPercentInputs.setValue(0);
        this.jdfPercentageNbRulesWithBetweenLAndMPercentInputs.setValue(0);
        this.jdfPercentageNbRulesWithMoreThanMPercentInputs.setValue(0);
        this.jdfRuleBaseStructuralComplexity.setValue(0);
        this.jdfRuleBaseStructuralDimension.setValue(0);
        this.jdfRuleBaseStructuralFramework.setValue(0);
        this.jdfRBInterpretability.setValue(0);
        this.jdfNbTotalInputs.setValue(0);
        this.jdfNbTotalUsedLabels.setValue(0);
        this.jdfRuleBaseConceptualDimension.setValue(0);
        this.jdfPercentageNbTotalElementaryUsedLabels.setValue(0);
        this.jdfPercentageNbTotalNOTCompositeUsedLabels.setValue(0);
        this.jdfPercentageNbTotalORCompositeUsedLabels.setValue(0);
        this.jdfRuleBaseConceptualComplexity.setValue(0);
        this.jdfRuleBaseConceptualFramework.setValue(0);
        this.jdfDBInterpretability.setValue(0);
        this.jdfDimInterpretability.setValue(this.InterpIndex);
        this.jLabelInterpMsg.setText("("+LocaleKBCT.GetString(this.IntLabel)+")");
    }
    this.indInt= this.jdfDimInterpretability.getValue();    
  }
//------------------------------------------------------------------------------
  // Copy from fuzzyClassifier.java (GUAJE-jMetal)
  protected double[] evaluateInterpretabilityExplanationMaximum( JKBCT kbct ) {
      double[] result= new double[3];
      for (int n=0; n<result.length; n++) { 
           result[n]= 1.0;
      }
      int NbInputs= kbct.GetNbInputs();
      int NbLabels[]= new int[NbInputs];
      for (int n=0; n<NbInputs; n++) {
    	  NbLabels[n]= kbct.GetInput(n+1).GetLabelsNumber();
      }
      int NbRules= kbct.GetNbRules();
      //int cont=0;
      int[] c= new int[NbRules];
      for (int n=0; n<NbRules; n++) {
    	  Rule r1= kbct.GetRule(n+1);
    	  if (r1.GetActive()) {
    		  c[n]=1;
              for (int k=0; k<NbRules; k++) {
                  if (k != n) {
            	      Rule r2= kbct.GetRule(k+1);
            	      if (r2.GetActive()) {
           	              boolean res= this.getRulesSimultaneouslyFired(r1, r2, NbLabels);
            	          if (res) {
            	              //cont++;
            	        	  c[n]++;
            	          }
            	      }
                  }
              }
    	  }
      }
      double cont=0;
      double min=NbRules;
      double max=1;
      for (int n=0; n<NbRules; n++) {
           //System.out.println("c["+n+"]="+c[n]);
    	   cont=cont+c[n];
    	   if (c[n] > max)
    		   max= c[n];
    	   
    	   if (c[n] < min)
    		   min= c[n];
    	   
      }
      //System.out.println("Interp2 (cont)= "+cont);
      result[0]= (cont/(double)NbRules);
      result[1]= min;
      result[2]= max;
      return result;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Check if rules "r1" and "r2" can be fired simultaneously.
   * </p>
   * @param r1 first rule
   * @param r2 second rule
   * @return true ("r1" and "r2" can be fired by the same input) / false
   */
  private boolean getRulesSimultaneouslyFired( Rule r1, Rule r2, int[] NbLabels ) {
    boolean result= true;
    int NbLim= r1.GetNbInputs();
    int[] r1_labels= r1.Get_in_labels_number();
    int[] r2_labels= r2.Get_in_labels_number();
    // increasing order
    for (int n=0; n<NbLim; n++) {
    	 if (r1_labels[n]!=r2_labels[n]) {
             boolean v= this.isL2firedWhenL1(r1_labels[n], r2_labels[n], NbLabels[n]);
    	     if (!v) {
                 return false;
             }
         }
    }
    return result;
  }
  //------------------------------------------------------------------------------
  /**
   * @param lab_num1 number of first label
   * @param lab_num2 number of second label
   * @param NOL number of labels
   * @return true (labels can be fired simultaneously) / false (otherwise)
   */
  private boolean isL2firedWhenL1( int lab_num1, int lab_num2, int NOL ) {
    double score=0;
	int[] bl1= this.containedLabels(lab_num1, NOL);
    int[] bl2= this.containedLabels(lab_num2, NOL);
    int cont11=0; // basic in L1
    int cont12=0; // basic in L2
    int cont2=0; // included
    double cont3=0; // border
    for (int n=0; n<NOL; n++) {
    	 if (bl1[n]==1)
    		 cont11++;
    	 
    	 if (bl2[n]==1)
    		 cont12++;

    	 if ( (bl1[n]==1) && (bl2[n]==1) )
             cont2++;
    	 
         //if ( (lab_num1 > NOL) || (lab_num1 > NOL) ) {
    	     if ( (bl1[n]==1) && (bl2[n]==0) && (n>0) && (bl1[n-1]==1) && (bl2[n-1]==1) ) 
                 cont3= cont3+1;

    	     if ( (bl1[n]==1) && (bl2[n]==0) && (n<NOL-1) && (bl1[n+1]==1) && (bl2[n+1]==1) ) 
                 cont3= cont3+1;
    	 
    	     if ( (bl1[n]==1) && (bl2[n]==0) && (n>0) && (bl1[n-1]==0) && (bl2[n-1]==1) ) 
                 cont3= cont3+1;
    	 
    	     if ( (bl1[n]==1) && (bl2[n]==0) && (n<NOL-1) && (bl1[n+1]==0) && (bl2[n+1]==1) ) 
                 cont3= cont3+1;
         //}
    }
    if (cont2==cont11) {
    	score=1;
    } else if ( (cont11==1) && (cont12==1) && (cont3==1) ) {
    	    score=0.5;

    } else if ( (cont11>1) && (cont2==0) && (cont3==0) ) {
    	score=0;
    } else {
    	if (cont2+cont3 > cont12) 
    		cont3--;
    		
    	score= ((double)(cont2+cont3)/(double)(cont11+1));
    }
    if (score > 0)
        return true;
    else 
    	return false;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Return number of new label obtained as merge of "lab_num1" and "lab_num2".
   * </p>
   * @param lab_num1 number of first label
   * @param lab_num2 number of second label
   * @param NOL number of labels
   * @return true (labels can be fired simultaneously) / false (otherwise)
   */
  private int[] containedLabels( int lab_num, int NOL ) {
    int[] result= new int[NOL];
    for (int n=0; n<NOL; n++) {
   	     result[n]=0;
    }
    if (lab_num==0) {
    	// Don't care
        for (int n=1; n<=NOL; n++) {
        	 result[n-1]=1;
        }
    } else if (lab_num <= NOL) {
    	// Basic
        for (int n=1; n<=NOL; n++) {
       	     if (lab_num==n) 
       	    	 result[n-1]=1;
        }
    } else if ( (lab_num > NOL) && (lab_num <= 2*NOL) ) {
    	// NOT
        for (int n=1; n<=NOL; n++) {
      	     if (lab_num!=n+NOL) 
      	    	 result[n-1]=1;
       }
    } else if (lab_num > 2*NOL) {
    	// OR
        int SelLabel= lab_num-2*NOL;
        int auxNbOR= jnikbct.NbORLabels(SelLabel, NOL);
        int optionaux= jnikbct.option(SelLabel, auxNbOR, NOL);
        for (int n=1; n<=NOL; n++) {
      	     if ( (n >= optionaux) && (n <= optionaux+auxNbOR-1) ) 
      	    	 result[n-1]=1;
       }
    }
    return result;
  }
  //------------------------------------------------------------------------------
  // Copy from JConsistency.java
  private double computeInterpLocalWeightRule(Rule r, int[] NbLabels) {
	  double result=-1;
	  double maxNbLabels=9;
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
           } 
           resultV[n]= 1+Math.pow((prem/maxNbLabels),NbLabels[n]);
      }
      for (int n=0; n<lim; n++) {
           if (n==0)
    	       result= resultV[n];
           else
    	       result= result*resultV[n];
      }
   
      return result;
  }
  private double computeInterpLocalWeightRuleSC11(Rule r, int[] NbLabels) {
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
           } else {
        	   prem=prem+NbLabels[n];        	   
           }
           resultV[n]= 2-(prem/NbLabels[n]);
          
      }
      for (int n=0; n<lim; n++) {
           if (n==0)
    	       result= resultV[n];
           else
    	       result= result*resultV[n];
      }
      return result;
  }
//------------------------------------------------------------------------------
  private int[] UsedInputsByRule(int NbRules, int NbInputs) {
    int[] result= new int[NbRules];
    int lim= this.kbct.GetNbRules();
    int cAR= 0;
    for (int n=0; n<lim; n++) {
      Rule r= this.kbct.GetRule(n+1);
      if (r.GetActive()) {
        int[] premises= r.Get_in_labels_number();
        int cont=0;
        for (int m=0; m<NbInputs; m++) {
          if (premises[m]!=0)
            cont++;
        }
        result[cAR++]= cont;
      }
    }
    return result;
  }
//------------------------------------------------------------------------------
  private int[] DefindedLabelsByInput(int NbInputs) {
    int[] result= new int[NbInputs];
    for (int n=0; n<NbInputs; n++)
      result[n]= this.kbct.GetInput(n+1).GetLabelsNumber();

    return result;
  }
//------------------------------------------------------------------------------
  /*
   * NbInputs: Number of inputs
   * Nature: Elementary/ORComposite/NOTComposite
   */
  private int[] UsedLabelsByInput(int NbInputs, String Nature) {
    int[] result= new int[NbInputs];
    for (int n=0; n<NbInputs; n++) {
        int NbLabs=this.kbct.GetInput(n+1).GetLabelsNumber();
    	int[] aux= this.UsedLabels(n+1,NbLabs,"INPUT");
    	//for (int m=0; m<aux.length; m++) {
    	//for (int m=0; m<NbLabs; m++) {
    		//System.out.println("var"+String.valueOf(n+1)+"  -> m="+String.valueOf(m+1)+"  aux="+aux[m]);
    	//}
    	int ini=0;
    	int lim= NbLabs;
    	if (Nature.equals("NOTComposite")) {
            ini= NbLabs;
    		lim= 2*NbLabs;
    	} else if (Nature.equals("ORComposite")) {
            ini= 2*NbLabs;
    		lim= aux.length;
    	} else if (Nature.equals("All")) {
            ini= 0;
    		lim= aux.length;
    	}
    	int sum=0;
		//System.out.println("NbInputs: "+NbInputs+"  -> n="+n+"  -> Nature="+Nature+"  auxlength="+aux.length);
        for (int m=ini; m<lim; m++) {
            if (aux[m]==1)
        	    sum++;
        }
        result[n]=sum;
    }
    return result;
  }
//------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Build an array with labels used in any rule.
   * Array dimension is equal to number of labels (Basic Labels + NOT Labels + OR Labels).
   * </p>
   * @param var_number number of the variable
   * @param NbLabels number of labels of the variable
   * @param var_type type of the variable
   * @return array of int (1 (label used) / 0 (label not used))
   */
  public int[] UsedLabels(int var_number, int NbLabels, String var_type) {
    int DefLabels=2*NbLabels+jnikbct.serie(NbLabels-1)-3;
    if (NbLabels<=3)
    	DefLabels= DefLabels+NbLabels;
    
	int[] USED_LABELS= new int [DefLabels];
    for (int m=0; m < USED_LABELS.length; m++)
      USED_LABELS[m]=0;

    int NbRules= this.kbct.GetNbRules();
    for (int n=0; n<NbRules; n++) {
      Rule r= this.kbct.GetRule(n+1);
      if (r.GetActive()) {
        int SelLabel=0;
        if (var_type.equals("INPUT"))
           SelLabel= r.Get_in_labels_number()[var_number-1];
        else if (var_type.equals("OUTPUT"))
           SelLabel= r.Get_out_labels_number()[var_number-1];

        if ( (SelLabel > 0) && (SelLabel<=USED_LABELS.length) )
          USED_LABELS[SelLabel - 1] = 1;
      }
    }
    return USED_LABELS;
  }

//------------------------------------------------------------------------------
  private double TotalSum(int[] d) {
    double result=0.0;
    int N= d.length;
    for (int n=0; n<N; n++)
      result= result + d[n];

    return result;
  }

//------------------------------------------------------------------------------
  private JFIS BuildInterpretabilityKB1_DataBaseInterpretability() {
    return this.BuildInterpretabilityFIS(1, 0);
  }
//------------------------------------------------------------------------------
  private JFIS BuildInterpretabilityKB211_RuleBaseStructuralDimension() {
    return this.BuildInterpretabilityFIS(211, 1);
  }
//------------------------------------------------------------------------------
  private JFIS BuildInterpretabilityKB212_RuleBaseStructuralComplexity() {
    return this.BuildInterpretabilityFIS(212, 2);
  }
//------------------------------------------------------------------------------
  private JFIS BuildInterpretabilityKB213_RuleBaseStructuralFramework() {
    return this.BuildInterpretabilityFIS(213, 3);
  }
//------------------------------------------------------------------------------
  private JFIS BuildInterpretabilityKB221_RuleBaseConceptualDimension() {
    return this.BuildInterpretabilityFIS(221, 4);
  }
//------------------------------------------------------------------------------
  private JFIS BuildInterpretabilityKB222_RuleBaseConceptualComplexity() {
    return this.BuildInterpretabilityFIS(222, 5);
  }
//------------------------------------------------------------------------------
  private JFIS BuildInterpretabilityKB223_RuleBaseConceptualFramework() {
    return this.BuildInterpretabilityFIS(223, 6);
  }
//------------------------------------------------------------------------------
  private JFIS BuildInterpretabilityKB2_RuleBaseInterpretability() {
    return this.BuildInterpretabilityFIS(2, 7);
  }
//------------------------------------------------------------------------------
  private JFIS BuildInterpretabilityKB3_InterpretabilityIndex() {
    return this.BuildInterpretabilityFIS(3, 8);
  }
//------------------------------------------------------------------------------
  private JFIS BuildInterpretabilityKB4_LocalExplanation() {
    return this.BuildInterpretabilityFIS(4, 9);
  }
//------------------------------------------------------------------------------
  private JFIS BuildInterpretabilityFIS(int Number, int Index) {
    JFIS result= null;
    //System.out.println("Number="+Number);
    String NameKB= MainKBCT.getConfig().GetKBCTintFilePath(Number, Index);
    //System.out.println("NameKB="+NameKB);
    JKBCT kbctInterp= new JKBCT(NameKB);
    File tempFIS= new File(NameKB+".fis");
    //File tempFIS= new File(NameKB.substring(0,NameKB.length()-3) + ".fis");
    //System.out.println("tempFIS="+tempFIS.getAbsolutePath());
    try {
        kbctInterp.Save();
        String OLD_conjunction= JConvert.conjunction;
        String[] OLD_disjunction= JConvert.disjunction;
        String[] OLD_defuzzification= JConvert.defuzzification;
        String conj= MainKBCT.getConfig().GetKBCTintConjunction(Index+1);
        String[] disj= {MainKBCT.getConfig().GetKBCTintDisjunction(Index+1)};
        String[] defuz= {MainKBCT.getConfig().GetKBCTintDefuzzification(Index+1)};
        this.conjunction[Index]= conj;
        this.disjunction[Index]= disj[0];
        this.defuzzification[Index]= defuz[0];

        JConvert.SetFISoptions(conj, disj, defuz);
        this.kbctDataBase[Index]= kbctInterp;
        //System.out.println("tempFIS.getAbsolutePath()="+tempFIS.getAbsolutePath());
        this.kbctRulesInfer[Index]= kbctInterp.SaveFIS(tempFIS.getAbsolutePath());
        JConvert.SetFISoptions(OLD_conjunction, OLD_disjunction, OLD_defuzzification);
        result= new JFIS(tempFIS.getAbsolutePath());
    } catch (Throwable t) {
        //t.printStackTrace();
    	MessageKBCT.Error(null, t);
    }
    return result;
  }
//------------------------------------------------------------------------------
  void jButtonViewRB_actionPerformed(int RB) {
     try {
      switch(RB) {
        case 1: if (this.jifKB1_DataBaseInterpretability!=null)
                    this.jifKB1_DataBaseInterpretability.dispose();
                this.jifKB1_DataBaseInterpretability= new JInferenceFrame(this.parent, this.InterpKB1_DataBaseInterpretability, new JSemaphore(), null, this.kbctRulesInfer[0]);
                this.jifKB1_DataBaseInterpretability.setData(this.FIS_KB1_DataBaseInterpretability);
                this.jifKB1_DataBaseInterpretability.setVisible(true);
                break;
        case 211: if (this.jifKB211_RuleBaseStructuralDimension!=null)
                    this.jifKB211_RuleBaseStructuralDimension.dispose();
                this.jifKB211_RuleBaseStructuralDimension= new JInferenceFrame(this.parent, this.InterpKB211_RuleBaseStructuralDimension, new JSemaphore(), null, this.kbctRulesInfer[1]);
                this.jifKB211_RuleBaseStructuralDimension.setData(this.FIS_KB211_RuleBaseStructuralDimension);
                this.jifKB211_RuleBaseStructuralDimension.setVisible(true);
                break;
        case 212: if (this.jifKB212_RuleBaseStructuralComplexity!=null)
                    this.jifKB212_RuleBaseStructuralComplexity.dispose();
                this.jifKB212_RuleBaseStructuralComplexity= new JInferenceFrame(this.parent, this.InterpKB212_RuleBaseStructuralComplexity, new JSemaphore(), null, this.kbctRulesInfer[2]);
                this.jifKB212_RuleBaseStructuralComplexity.setData(this.FIS_KB212_RuleBaseStructuralComplexity);
                this.jifKB212_RuleBaseStructuralComplexity.setVisible(true);
                break;
        case 213: if (this.jifKB213_RuleBaseStructuralFramework!=null)
                    this.jifKB213_RuleBaseStructuralFramework.dispose();
                this.jifKB213_RuleBaseStructuralFramework= new JInferenceFrame(this.parent, this.InterpKB213_RuleBaseStructuralFramework, new JSemaphore(), null, this.kbctRulesInfer[3]);
                this.jifKB213_RuleBaseStructuralFramework.setData(this.FIS_KB213_RuleBaseStructuralFramework);
                this.jifKB213_RuleBaseStructuralFramework.setVisible(true);
                break;
        case 221: if (this.jifKB221_RuleBaseConceptualDimension!=null)
                    this.jifKB221_RuleBaseConceptualDimension.dispose();
                 this.jifKB221_RuleBaseConceptualDimension= new JInferenceFrame(this.parent, this.InterpKB221_RuleBaseConceptualDimension, new JSemaphore(), null, this.kbctRulesInfer[4]);
                 this.jifKB221_RuleBaseConceptualDimension.setData(this.FIS_KB221_RuleBaseConceptualDimension);
                 this.jifKB221_RuleBaseConceptualDimension.setVisible(true);
                 break;
        case 222: if (this.jifKB222_RuleBaseConceptualComplexity!=null)
                    this.jifKB222_RuleBaseConceptualComplexity.dispose();
                 this.jifKB222_RuleBaseConceptualComplexity= new JInferenceFrame(this.parent, this.InterpKB222_RuleBaseConceptualComplexity, new JSemaphore(), null, this.kbctRulesInfer[5]);
                 this.jifKB222_RuleBaseConceptualComplexity.setData(this.FIS_KB222_RuleBaseConceptualComplexity);
                 this.jifKB222_RuleBaseConceptualComplexity.setVisible(true);
                 break;
        case 223: if (this.jifKB223_RuleBaseConceptualFramework!=null)
                    this.jifKB223_RuleBaseConceptualFramework.dispose();
                this.jifKB223_RuleBaseConceptualFramework= new JInferenceFrame(this.parent, this.InterpKB223_RuleBaseConceptualFramework, new JSemaphore(), null, this.kbctRulesInfer[6]);
                this.jifKB223_RuleBaseConceptualFramework.setData(this.FIS_KB223_RuleBaseConceptualFramework);
                this.jifKB223_RuleBaseConceptualFramework.setVisible(true);
                break;
        case 2: if (this.jifKB2_RuleBaseInterpretability!=null)
                    this.jifKB2_RuleBaseInterpretability.dispose();
                this.jifKB2_RuleBaseInterpretability= new JInferenceFrame(this.parent, this.InterpKB2_RuleBaseInterpretability, new JSemaphore(), null, this.kbctRulesInfer[7]);
                this.jifKB2_RuleBaseInterpretability.setData(this.FIS_KB2_RuleBaseInterpretability);
                this.jifKB2_RuleBaseInterpretability.setVisible(true);
                break;
        case 3: if (this.jifKB3_InterpretabilityIndex!=null)
                    this.jifKB3_InterpretabilityIndex.dispose();
                this.jifKB3_InterpretabilityIndex= new JInferenceFrame(this.parent, this.InterpKB3_InterpretabilityIndex, new JSemaphore(), null, this.kbctRulesInfer[8]);
                this.jifKB3_InterpretabilityIndex.setData(this.FIS_KB3_InterpretabilityIndex);
                this.jifKB3_InterpretabilityIndex.setVisible(true);
                break;
        case 4: if (this.jifKB4_LocalExplanation!=null)
                     this.jifKB4_LocalExplanation.dispose();
                this.jifKB4_LocalExplanation= new JInferenceFrame(this.parent, this.InterpKB4_LocalExplanation, new JSemaphore(), null, this.kbctRulesInfer[9]);
                this.jifKB4_LocalExplanation.setData(this.FIS_KB4_LocalExplanation);
                this.jifKB4_LocalExplanation.setVisible(true);
                break;
      }
    } catch (Throwable t) { 
        //t.printStackTrace();
    	MessageKBCT.Error(null, t);
    }
  }
//------------------------------------------------------------------------------
  void jButtonOpenKBfile_actionPerformed(int KB) {
   try {
     JOpenFileChooser file_chooser = new JOpenFileChooser(MainKBCT.getConfig().GetKBCTFilePath());
     file_chooser.setAcceptAllFileFilterUsed(true);
     JFileFilter filter2 = new JFileFilter(("XML").toLowerCase(), LocaleKBCT.GetString("FilterKBFile"));
     //JFileFilter filter1 = new JFileFilter(("KB").toLowerCase(), LocaleKBCT.GetString("FilterKBFile"));
     file_chooser.addChoosableFileFilter(filter2);
     //file_chooser.addChoosableFileFilter(filter1);
     //file_chooser.setFileFilter(filter2);
     if( file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ) {
         String newKB=file_chooser.getSelectedFile().getAbsolutePath();
         JKBCT kbctNew= new JKBCT(newKB);
         if (this.validNewKB(kbctNew, KB)) {
        	 //this.kbctDataBase[KB-1]= kbctNew;
        	 //this.BuildInterpretabilityFIS(KB);
        	 //System.out.println("KB="+KB);
             MainKBCT.getConfig().SetKBCTintFilePath(newKB,KB);
             //this.jbInit(); 
        	 //System.out.println("Range="+kbctNew.GetInput(1).GetInputInterestRange()[1]);
        	 this.ComputeInterpretability();
         } else {
             MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("InvalidKBinterpretabilityFile"));
         }
     }
    } catch (Throwable t) { 
        //t.printStackTrace();
    	MessageKBCT.Error(null, t);
    }
  }
//------------------------------------------------------------------------------
  private boolean validNewKB(JKBCT newKB, int KB) {
      /*JKBCT oldKB= this.kbctDataBase[KB-1];
      if (newKB.GetNbInputs() != oldKB.GetNbInputs()) {
    	  return false;
      } else if (newKB.GetNbOutputs() != oldKB.GetNbOutputs()) {
    	  return false;
      }*/
      JKBCT oldKB;
      switch (KB) {
        case 1: oldKB= this.kbctDataBase[0];
                if (newKB.GetNbInputs() != oldKB.GetNbInputs()) {
      	            return false;
                } else if (newKB.GetNbOutputs() != oldKB.GetNbOutputs()) {
      	            return false;
      	        }
                oldKB= this.kbctDataBase[8];
                if (newKB.GetOutput(1).GetLabelsNumber() != oldKB.GetInput(1).GetLabelsNumber()) {
                    return false;
                }
                break;
        case 211: oldKB= this.kbctDataBase[1];
                if (newKB.GetNbInputs() != oldKB.GetNbInputs()) {
	                return false;
                } else if (newKB.GetNbOutputs() != oldKB.GetNbOutputs()) {
	                return false;
	            }
                oldKB= this.kbctDataBase[3];
                if (newKB.GetOutput(1).GetLabelsNumber() != oldKB.GetInput(1).GetLabelsNumber()) {
                    return false;
                }
                break;
        case 212: oldKB= this.kbctDataBase[2];
                if (newKB.GetNbInputs() != oldKB.GetNbInputs()) {
                    return false;
                } else if (newKB.GetNbOutputs() != oldKB.GetNbOutputs()) {
                    return false;
                }
                oldKB= this.kbctDataBase[3];
                if (newKB.GetOutput(1).GetLabelsNumber() != oldKB.GetInput(2).GetLabelsNumber()) {
                    return false;
                }
                break;
        case 213: oldKB= this.kbctDataBase[3];
                if (newKB.GetNbInputs() != oldKB.GetNbInputs()) {
                    return false;
                } else if (newKB.GetNbOutputs() != oldKB.GetNbOutputs()) {
                    return false;
                }
                oldKB= this.kbctDataBase[7];
                if (newKB.GetOutput(1).GetLabelsNumber() != oldKB.GetInput(1).GetLabelsNumber()) {
                    return false;
                }
                break;
        case 221: oldKB= this.kbctDataBase[4];
                if (newKB.GetNbInputs() != oldKB.GetNbInputs()) {
                    return false;
                } else if (newKB.GetNbOutputs() != oldKB.GetNbOutputs()) {
                    return false;
                }
                oldKB= this.kbctDataBase[6];
                if (newKB.GetOutput(1).GetLabelsNumber() != oldKB.GetInput(1).GetLabelsNumber()) {
                    return false;
                }
                break;
        case 222: oldKB= this.kbctDataBase[5];
                if (newKB.GetNbInputs() != oldKB.GetNbInputs()) {
                    return false;
                } else if (newKB.GetNbOutputs() != oldKB.GetNbOutputs()) {
                    return false;
                }
                oldKB= this.kbctDataBase[6];
                if (newKB.GetOutput(1).GetLabelsNumber() != oldKB.GetInput(2).GetLabelsNumber()) {
                    return false;
                }
                break;
        case 223 : oldKB= this.kbctDataBase[6];
                if (newKB.GetNbInputs() != oldKB.GetNbInputs()) {
                    return false;
                } else if (newKB.GetNbOutputs() != oldKB.GetNbOutputs()) {
                    return false;
                }
                oldKB= this.kbctDataBase[7];
                if (newKB.GetOutput(1).GetLabelsNumber() != oldKB.GetInput(2).GetLabelsNumber()) {
                    return false;
                }
                break;
        case 2: oldKB= this.kbctDataBase[7];
                if (newKB.GetNbInputs() != oldKB.GetNbInputs()) {
                    return false;
                } else if (newKB.GetNbOutputs() != oldKB.GetNbOutputs()) {
                    return false;
                }
                oldKB= this.kbctDataBase[8];
                if (newKB.GetOutput(1).GetLabelsNumber() != oldKB.GetInput(2).GetLabelsNumber()) {
                    return false;
                }
                break;
        case 3: oldKB= this.kbctDataBase[8];
                if (newKB.GetNbInputs() != oldKB.GetNbInputs()) {
                    return false;
                } else if (newKB.GetNbOutputs() != oldKB.GetNbOutputs()) {
                    return false;
                }
                break;
        case 4: oldKB= this.kbctDataBase[9];
                if (newKB.GetNbInputs() != oldKB.GetNbInputs()) {
                    return false;
                } else if (newKB.GetNbOutputs() != oldKB.GetNbOutputs()) {
                    return false;
                }
                break;
      }
      return true;
  }
//------------------------------------------------------------------------------
  void jButtonViewDB_actionPerformed(int DB) {
    switch(DB) {
      case 1: if (this.jdbfif1!=null)
                  this.jdbfif1.dispose();
              this.jdbfif1= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[0], this.conjunction[0], this.disjunction[0], this.defuzzification[0], 1);
              break;
      case 211: if (this.jdbfif211!=null)
                  this.jdbfif211.dispose();
              this.jdbfif211= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[1], this.conjunction[1], this.disjunction[1], this.defuzzification[1], 2);
              break;
      case 212: if (this.jdbfif212!=null)
                  this.jdbfif212.dispose();
              this.jdbfif212= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[2], this.conjunction[2], this.disjunction[2], this.defuzzification[2], 3);
              break;
      case 213: if (this.jdbfif213!=null)
                  this.jdbfif213.dispose();
              this.jdbfif213= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[3], this.conjunction[3], this.disjunction[3], this.defuzzification[3], 4);
              break;
      case 221: if (this.jdbfif221!=null)
                  this.jdbfif221.dispose();
              this.jdbfif221= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[4], this.conjunction[4], this.disjunction[4], this.defuzzification[4], 5);
              break;
      case 222: if (this.jdbfif222!=null)
                  this.jdbfif222.dispose();
              this.jdbfif222= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[5], this.conjunction[5], this.disjunction[5], this.defuzzification[5], 6);
              break;
      case 223: if (this.jdbfif223!=null)
                  this.jdbfif223.dispose();
              this.jdbfif223= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[6], this.conjunction[6], this.disjunction[6], this.defuzzification[6], 7);
              break;
      case 2: if (this.jdbfif2!=null)
                  this.jdbfif2.dispose();
              this.jdbfif2= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[7], this.conjunction[7], this.disjunction[7], this.defuzzification[7], 8);
              break;
      case 3: if (this.jdbfif3!=null)
                  this.jdbfif3.dispose();
              this.jdbfif3= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[8], this.conjunction[8], this.disjunction[8], this.defuzzification[8], 9);
              break;
      case 4: if (this.jdbfif4!=null)
                  this.jdbfif4.dispose();
              this.jdbfif4= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[9], this.conjunction[9], this.disjunction[9], this.defuzzification[9], 10);
      break;
    }
  }
//------------------------------------------------------------------------------
  void jButtonPartition_actionPerformed(int var, int DB, boolean input) {
    //System.out.println("var="+var+"  DB="+DB+"  input="+input);
	switch(DB) {
      case 1: switch(var) {
                 case 1: if (input) {
         	                 if (this.jdbfif1_NbDefinedLabels!=null)
                                 this.jdbfif1_NbDefinedLabels.dispose();
                             this.jdbfif1_NbDefinedLabels= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[0].GetInput(1), 1, 1);
                         } else {
                             if (this.jdbfif3_DBInterpretability!=null)
                                 this.jdbfif3_DBInterpretability.dispose();
                             this.jdbfif3_DBInterpretability= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[0].GetOutput(1), 1, 1);
                         }
                         break;
                 case 2: if (this.jdbfif1_PartitionInterpretability!=null)
                             this.jdbfif1_PartitionInterpretability.dispose();
                         this.jdbfif1_PartitionInterpretability= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[0].GetInput(2), 1, 2);
                         break;
              }
              break;
      case 211: switch(var) {
                  case 1: if (input) {
                	          if (this.jdbfif211_NbRules!=null)
                                  this.jdbfif211_NbRules.dispose();
                              this.jdbfif211_NbRules= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[1].GetInput(1), 2, 1);
                          } else {
                	          if (this.jdbfif213_RuleBaseStructuralDimension!=null)
                                  this.jdbfif213_RuleBaseStructuralDimension.dispose();
                              this.jdbfif213_RuleBaseStructuralDimension= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[1].GetOutput(1), 1, 1);
                          }
                          break;
                  case 2: if (this.jdbfif211_NbPremises!=null)
                              this.jdbfif211_NbPremises.dispose();
                          this.jdbfif211_NbPremises= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[1].GetInput(2), 2, 2);
                          break;
              }
              break;
      case 212: switch(var) {
                 case 1: if (input) { 
                	         if (this.jdbfif212_PercentageNbRulesWithLessThanLInputs!=null)
                                 this.jdbfif212_PercentageNbRulesWithLessThanLInputs.dispose();
                             this.jdbfif212_PercentageNbRulesWithLessThanLInputs= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[2].GetInput(1), 3, 1);
                         } else {
                	         if (this.jdbfif213_RuleBaseStructuralComplexity!=null)
                                 this.jdbfif213_RuleBaseStructuralComplexity.dispose();
                             this.jdbfif213_RuleBaseStructuralComplexity= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[2].GetOutput(1), 3, 1);
                         }
                         break;
                 case 2: if (this.jdbfif212_PercentageNbRulesWithBetweenLandMInputs!=null)
                             this.jdbfif212_PercentageNbRulesWithBetweenLandMInputs.dispose();
                         this.jdbfif212_PercentageNbRulesWithBetweenLandMInputs= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[2].GetInput(2), 3, 2);
                         break;
                 case 3: if (this.jdbfif212_PercentageNbRulesWithMoreThanMInputs!=null)
                             this.jdbfif212_PercentageNbRulesWithMoreThanMInputs.dispose();
                         this.jdbfif212_PercentageNbRulesWithMoreThanMInputs= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[2].GetInput(3), 3, 3);
                         break;
                 }
                 break;
      case 213: switch(var) {
                 case 1: if (input) {
                	         if (this.jdbfif213_RuleBaseStructuralDimension!=null)
                                 this.jdbfif213_RuleBaseStructuralDimension.dispose();
                             this.jdbfif213_RuleBaseStructuralDimension= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[3].GetInput(1), 4, 1);
                         } else {
                	         if (this.jdbfif2_RuleBaseStructuralFramework!=null)
                                 this.jdbfif2_RuleBaseStructuralFramework.dispose();
                             this.jdbfif2_RuleBaseStructuralFramework= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[3].GetOutput(1), 4, 1);
                         }
                         break;
                 case 2: if (this.jdbfif213_RuleBaseStructuralComplexity!=null)
                             this.jdbfif213_RuleBaseStructuralComplexity.dispose();
                         this.jdbfif213_RuleBaseStructuralComplexity= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[3].GetInput(2), 4, 2);
                         break;
                 }
                 break;
      case 221: switch(var) {
                 case 1: if (input) {
                	         if (this.jdbfif221_NbTotalInputs!=null)
                                 this.jdbfif221_NbTotalInputs.dispose();
                             this.jdbfif221_NbTotalInputs= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[4].GetInput(1), 5, 1);
                         } else {
                	         if (this.jdbfif223_RuleBaseConceptualDimension!=null)
                                 this.jdbfif223_RuleBaseConceptualDimension.dispose();
                             this.jdbfif223_RuleBaseConceptualDimension= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[4].GetOutput(1), 5, 1);
                         }
                         break;
                 case 2: if (this.jdbfif221_NbTotalUsedLabels!=null)
                             this.jdbfif221_NbTotalUsedLabels.dispose();
                         this.jdbfif221_NbTotalUsedLabels= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[4].GetInput(2), 5, 2);
                         break;
                 }
                 break;
      case 222: switch(var) {
                 case 1: if (input) {
                	         if (this.jdbfif222_PercentageNbTotalElementaryUsedLabels!=null)
                                 this.jdbfif222_PercentageNbTotalElementaryUsedLabels.dispose();
                             this.jdbfif222_PercentageNbTotalElementaryUsedLabels= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[5].GetInput(1), 6, 1);
                         } else {
                	         if (this.jdbfif223_RuleBaseConceptualComplexity!=null)
                                 this.jdbfif223_RuleBaseConceptualComplexity.dispose();
                             this.jdbfif223_RuleBaseConceptualComplexity= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[5].GetOutput(1), 6, 1);
                         }
                         break;
                 case 2: if (this.jdbfif222_PercentageNbTotalORCompositeUsedLabels!=null)
                             this.jdbfif222_PercentageNbTotalORCompositeUsedLabels.dispose();
                         this.jdbfif222_PercentageNbTotalORCompositeUsedLabels= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[5].GetInput(2), 6, 2);
                         break;
                 case 3: if (this.jdbfif222_PercentageNbTotalNOTCompositeUsedLabels!=null)
                             this.jdbfif222_PercentageNbTotalNOTCompositeUsedLabels.dispose();
                         this.jdbfif222_PercentageNbTotalNOTCompositeUsedLabels= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[5].GetInput(3), 6, 3);
                         break;
                 }
                 break;
      case 223: switch(var) {
                 case 1: if (input) {
                	         if (this.jdbfif223_RuleBaseConceptualDimension!=null)
                                 this.jdbfif223_RuleBaseConceptualDimension.dispose();
                             this.jdbfif223_RuleBaseConceptualDimension= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[6].GetInput(1), 7, 1);
                         } else {
                	         if (this.jdbfif2_RuleBaseConceptualFramework!=null)
                                 this.jdbfif2_RuleBaseConceptualFramework.dispose();
                             this.jdbfif2_RuleBaseConceptualFramework= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[6].GetOutput(1), 7, 1);
                         }
                         break;
                 case 2: if (this.jdbfif223_RuleBaseConceptualComplexity!=null)
                             this.jdbfif223_RuleBaseConceptualComplexity.dispose();
                         this.jdbfif223_RuleBaseConceptualComplexity= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[6].GetInput(2), 7, 2);
                         break;
                 }
                 break;
      case 2: switch(var) {
                 case 1: if (input) {
                	         if (this.jdbfif2_RuleBaseStructuralFramework!=null)
                                 this.jdbfif2_RuleBaseStructuralFramework.dispose();
                             this.jdbfif2_RuleBaseStructuralFramework= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[7].GetInput(1), 8, 1);
                         } else {
                	         if (this.jdbfif3_RBInterpretability!=null)
                                 this.jdbfif3_RBInterpretability.dispose();
                             this.jdbfif3_RBInterpretability= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[7].GetOutput(1), 8, 1);
                         }
                         break;
                 case 2: if (this.jdbfif2_RuleBaseConceptualFramework!=null)
                             this.jdbfif2_RuleBaseConceptualFramework.dispose();
                         this.jdbfif2_RuleBaseConceptualFramework= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[7].GetInput(2), 8, 2);
                         break;
                 }
                 break;
      case 3: switch(var) {
                 case 1: if (input) {
                             if (this.jdbfif3_DBInterpretability!=null)
                                 this.jdbfif3_DBInterpretability.dispose();
                             this.jdbfif3_DBInterpretability= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[8].GetInput(1), 9, 1);
                         } else {
                             if (this.jdbfif3_InterpretabilityIndex!=null)
                                 this.jdbfif3_InterpretabilityIndex.dispose();
                             this.jdbfif3_InterpretabilityIndex= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[8].GetOutput(1), 9, 1);
                         }
                         break;
                 case 2: if (this.jdbfif3_RBInterpretability!=null)
                             this.jdbfif3_RBInterpretability.dispose();
                         this.jdbfif3_RBInterpretability= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[8].GetInput(2), 9, 2);
                         break;
                 }
                 break;
      case 4: switch(var) {
                 case 1: if (input) {
                             //System.out.println("case 8");
                	         if (this.jdbfif4_NbRulesTogetherFired2!=null)
                                 this.jdbfif4_NbRulesTogetherFired2.dispose();
                             this.jdbfif4_NbRulesTogetherFired2= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[9].GetInput(1), 10, 1);
                         } else {
                             if (this.jdbfif4_LocalExplanation!=null)
                                 this.jdbfif4_LocalExplanation.dispose();
                             this.jdbfif4_LocalExplanation= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[9].GetOutput(1), 10, 1);
                         }
                         break;
                 case 2: if (this.jdbfif4_NbRulesTogetherFired3!=null)
                             this.jdbfif4_NbRulesTogetherFired3.dispose();
                         this.jdbfif4_NbRulesTogetherFired3= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[9].GetInput(2), 10, 2);
                         break;
                 case 3: if (this.jdbfif4_NbRulesTogetherFired4!=null)
                             this.jdbfif4_NbRulesTogetherFired4.dispose();
                         this.jdbfif4_NbRulesTogetherFired4= new JDataBaseForKBInterpretabilityFrame(this.parent, this.kbctDataBase[9].GetInput(3), 10, 3);
                         break;
                 }
                 break;
    }
  }
//------------------------------------------------------------------------------
  void jButtonViewAll_actionPerformed() {
    if (this.jButtonViewAll.isSelected()) {
      this.jMenuView.setVisible(true);
      this.jMenuViewKB1.setVisible(true);
      this.jMenuViewKB2.setVisible(true);
      this.jMenuViewKB211.setVisible(true);
      this.jMenuViewKB212.setVisible(true);
      this.jMenuViewKB213.setVisible(true);
      this.jMenuViewKB221.setVisible(true);
      this.jMenuViewKB222.setVisible(true);
      this.jMenuViewKB223.setVisible(true);
      this.jMenuViewKB3.setVisible(true);
      this.jMenuOpenKBfile.setVisible(true);
      this.jMenuOpenKBfile1.setVisible(true);
      this.jMenuOpenKBfile2.setVisible(true);
      this.jMenuOpenKBfile211.setVisible(true);
      this.jMenuOpenKBfile212.setVisible(true);
      this.jMenuOpenKBfile213.setVisible(true);
      this.jMenuOpenKBfile221.setVisible(true);
      this.jMenuOpenKBfile222.setVisible(true);
      this.jMenuOpenKBfile223.setVisible(true);
      this.jMenuOpenKBfile3.setVisible(true);
      if (!this.jButtonViewRBbehaviour.isSelected()) {
          this.jMenuViewKB4.setVisible(false);
          this.jMenuOpenKBfile4.setVisible(false);
      }
      this.jMenuInputs.setVisible(true);
      this.jMenuReset.setVisible(true);
      this.jPanelInputsRB1.setVisible(true);
      this.jPanelRuleBase1.setVisible(true);
      this.jPanelInputsRB2.setVisible(true);
      this.jPanelRuleBaseRB2.setVisible(true);
      this.jPanelRuleBaseRB21.setVisible(true);
      this.jPanelRuleBaseRB22.setVisible(true);
      this.jPanelRuleBase2.setVisible(true);
      this.jPanelRuleBase3.setVisible(true);
      this.jPanelInputs.setVisible(true);
      this.jPanelRuleBase.setVisible(true);
      this.jMainPanelComplex.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
      this.jMainPanelComplex.setWheelScrollingEnabled(true);
    } else {
    	if (!this.jButtonViewRBbehaviour.isSelected()) {
            this.jMenuView.setVisible(false);
            this.jMenuOpenKBfile.setVisible(false);
            this.jMenuInputs.setVisible(false);
            this.jMenuReset.setVisible(false);
    	}
        this.jMenuViewKB1.setVisible(false);
        this.jMenuViewKB2.setVisible(false);
        this.jMenuViewKB211.setVisible(false);
        this.jMenuViewKB212.setVisible(false);
        this.jMenuViewKB213.setVisible(false);
        this.jMenuViewKB221.setVisible(false);
        this.jMenuViewKB222.setVisible(false);
        this.jMenuViewKB223.setVisible(false);
        this.jMenuViewKB3.setVisible(false);
        this.jMenuOpenKBfile1.setVisible(false);
        this.jMenuOpenKBfile2.setVisible(false);
        this.jMenuOpenKBfile211.setVisible(false);
        this.jMenuOpenKBfile212.setVisible(false);
        this.jMenuOpenKBfile213.setVisible(false);
        this.jMenuOpenKBfile221.setVisible(false);
        this.jMenuOpenKBfile222.setVisible(false);
        this.jMenuOpenKBfile223.setVisible(false);
        this.jMenuOpenKBfile3.setVisible(false);
        this.jPanelInputsRB1.setVisible(false);
        this.jPanelRuleBase1.setVisible(false);
        this.jPanelInputsRB2.setVisible(false);
        this.jPanelRuleBaseRB2.setVisible(false);
        this.jPanelRuleBaseRB21.setVisible(false);
        this.jPanelRuleBaseRB22.setVisible(false);
        this.jPanelRuleBase2.setVisible(false);
        this.jPanelRuleBase3.setVisible(false);
        this.jPanelInputs.setVisible(false);
        this.jPanelRuleBase.setVisible(false);
        this.jMainPanelComplex.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.jMainPanelComplex.setWheelScrollingEnabled(false);
    }
    this.pack();
    this.setLocation(this.ChildPosition(this.getSize()));
    this.repaint();
  }
//------------------------------------------------------------------------------
  void jButtonViewRBbehaviour_actionPerformed() {
    if (this.jButtonViewRBbehaviour.isSelected()) {
      if (!flagFirstBehaviorInference) {
      	flagFirstBehaviorInference= true;
        if (!MainKBCT.getConfig().GetTESTautomatic()) {
            MessageKBCT.Information(this, LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                                          "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                                          "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
    	    this.RuleBaseBehaviour();
    	    this.computeBuildResultsPanelInference();
        }
      }
      this.jMenuView.setVisible(true);
      this.jMenuViewKB4.setVisible(true);
      this.jMenuOpenKBfile.setVisible(true);
      this.jMenuOpenKBfile4.setVisible(true);
      if (!this.jButtonViewAll.isSelected()) {
          this.jMenuViewKB1.setVisible(false);
          this.jMenuViewKB2.setVisible(false);
          this.jMenuViewKB211.setVisible(false);
          this.jMenuViewKB212.setVisible(false);
          this.jMenuViewKB213.setVisible(false);
          this.jMenuViewKB221.setVisible(false);
          this.jMenuViewKB222.setVisible(false);
          this.jMenuViewKB223.setVisible(false);
          this.jMenuViewKB3.setVisible(false);
          this.jMenuOpenKBfile1.setVisible(false);
          this.jMenuOpenKBfile2.setVisible(false);
          this.jMenuOpenKBfile211.setVisible(false);
          this.jMenuOpenKBfile212.setVisible(false);
          this.jMenuOpenKBfile213.setVisible(false);
          this.jMenuOpenKBfile221.setVisible(false);
          this.jMenuOpenKBfile222.setVisible(false);
          this.jMenuOpenKBfile223.setVisible(false);
          this.jMenuOpenKBfile3.setVisible(false);
      }
      this.jMenuInputs.setVisible(true);
      this.jMenuReset.setVisible(true);
      this.jPanelInputsInf.setVisible(true);
      this.jPanelKBI.setVisible(true);
      this.jMainPanelInfer.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
      this.jMainPanelInfer.setWheelScrollingEnabled(true);
    } else {
    	if (!this.jButtonViewAll.isSelected()) {
            this.jMenuView.setVisible(false);
            this.jMenuOpenKBfile.setVisible(false);
            this.jMenuInputs.setVisible(false);
            this.jMenuReset.setVisible(false);
    	}
        this.jMenuViewKB4.setVisible(false);
        this.jMenuOpenKBfile4.setVisible(false);
        this.jPanelInputsInf.setVisible(false);
        this.jPanelKBI.setVisible(false);
        this.jMainPanelInfer.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.jMainPanelInfer.setWheelScrollingEnabled(false);
    }
    this.pack();
    this.setLocation(this.ChildPosition(this.getSize()));
    this.repaint();
  }
//------------------------------------------------------------------------------
  void jMenuInputs_actionPerformed() {
      final JDialog jd = new JDialog(this);
      jd.setTitle(LocaleKBCT.GetString("Interpretability"));
      jd.getContentPane().setLayout(new GridBagLayout());
      JPanel jPanelSaisie = new JPanel(new GridBagLayout());
      JPanel jPanelValidation = new JPanel(new GridBagLayout());
      JButton jButtonApply = new JButton(LocaleKBCT.GetString("Apply"));
      JButton jButtonCancel = new JButton(LocaleKBCT.GetString("Cancel"));
      JButton jButtonReset = new JButton(LocaleKBCT.GetString("Reset"));
	  if (this.main_TabbedPane.getSelectedIndex()==0) {
        JLabel jLabelNbTotalDefinedLabels = new JLabel(LocaleKBCT.GetString("NbTotalDefinedLabels") + " :");
        this.jdfInputsTotalNbDefinedLabels.setValue(this.jdfNbTotalDefinedLabels.getValue());
        JLabel jLabelPartitionInterpretability = new JLabel(LocaleKBCT.GetString("PartitionInterpretability") + " :");
        this.jdfInputsPartitionInterpretability.setValue(this.jdfPartitionInterpretability.getValue());
	    JLabel jLabelNbTotalRules = new JLabel(LocaleKBCT.GetString("NbTotalRules") + " :");
        this.jdfInputsTotalNbRules.setValue(this.jdfTotalNbRules.getValue());
        JLabel jLabelNbTotalPremises = new JLabel(LocaleKBCT.GetString("NbTotalPremises") + " :");
        this.jdfInputsTotalNbPremises.setValue(this.jdfTotalNbPremises.getValue());
        JLabel jLabelPercentageNbRulesWithLessThanLPercentInputs = new JLabel(LocaleKBCT.GetString("PercentageNbRulesWithLessThanLPercentInputs") + " :");
        this.jdfInputsPercentageNbRulesWithLessThanLPercentInputs.setValue(this.jdfPercentageNbRulesWithLessThanLPercentInputs.getValue());
        JLabel jLabelPercentageNbRulesWithBetweenLAndMPercentInputs = new JLabel(LocaleKBCT.GetString("PercentageNbRulesWithBetweenLAndMPercentInputs") + " :");
        this.jdfInputsPercentageNbRulesWithBetweenLAndMPercentInputs.setValue(this.jdfPercentageNbRulesWithBetweenLAndMPercentInputs.getValue());
        JLabel jLabelPercentageNbRulesWithMoreThanMPercentInputs = new JLabel(LocaleKBCT.GetString("PercentageNbRulesWithMoreThanMPercentInputs") + " :");
        this.jdfInputsPercentageNbRulesWithMoreThanMPercentInputs.setValue(this.jdfPercentageNbRulesWithMoreThanMPercentInputs.getValue());
        JLabel jLabelNbTotalInputs = new JLabel(LocaleKBCT.GetString("NbTotalInputs") + " :");
        this.jdfInputsNbTotalInputs.setValue(this.jdfNbTotalInputs.getValue());
        JLabel jLabelNbTotalUsedLabels = new JLabel(LocaleKBCT.GetString("NbTotalUsedLabels") + " :");
        this.jdfInputsNbTotalUsedLabels.setValue(this.jdfNbTotalUsedLabels.getValue());
        JLabel jLabelPercentageNbTotalElementaryUsedLabels = new JLabel(LocaleKBCT.GetString("PercentageNbTotalElementaryUsedLabels") + " :");
        this.jdfInputsPercentageNbTotalElementaryUsedLabels.setValue(this.jdfPercentageNbTotalElementaryUsedLabels.getValue());
        JLabel jLabelPercentageNbTotalORCompositeUsedLabels = new JLabel(LocaleKBCT.GetString("PercentageNbTotalORCompositeUsedLabels") + " :");
        this.jdfInputsPercentageNbTotalORCompositeUsedLabels.setValue(this.jdfPercentageNbTotalORCompositeUsedLabels.getValue());
        JLabel jLabelPercentageNbTotalNOTCompositeUsedLabels = new JLabel(LocaleKBCT.GetString("PercentageNbTotalNOTCompositeUsedLabels") + " :");
        this.jdfInputsPercentageNbTotalNOTCompositeUsedLabels.setValue(this.jdfPercentageNbTotalNOTCompositeUsedLabels.getValue());
        jd.getContentPane().add(jPanelSaisie, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
          ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        jPanelSaisie.add(jLabelNbTotalDefinedLabels, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelSaisie.add(this.jdfInputsTotalNbDefinedLabels, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
        jPanelSaisie.add(jLabelPartitionInterpretability, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelSaisie.add(this.jdfInputsPartitionInterpretability, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
        jPanelSaisie.add(jLabelNbTotalRules, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelSaisie.add(this.jdfInputsTotalNbRules, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
        jPanelSaisie.add(jLabelNbTotalPremises, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelSaisie.add(this.jdfInputsTotalNbPremises, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
        jPanelSaisie.add(jLabelPercentageNbRulesWithLessThanLPercentInputs, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelSaisie.add(this.jdfInputsPercentageNbRulesWithLessThanLPercentInputs, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
        jPanelSaisie.add(jLabelPercentageNbRulesWithBetweenLAndMPercentInputs, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelSaisie.add(this.jdfInputsPercentageNbRulesWithBetweenLAndMPercentInputs, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
        jPanelSaisie.add(jLabelPercentageNbRulesWithMoreThanMPercentInputs, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelSaisie.add(this.jdfInputsPercentageNbRulesWithMoreThanMPercentInputs, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
        jPanelSaisie.add(jLabelNbTotalInputs, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelSaisie.add(this.jdfInputsNbTotalInputs, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
        jPanelSaisie.add(jLabelNbTotalUsedLabels, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelSaisie.add(this.jdfInputsNbTotalUsedLabels, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
        jPanelSaisie.add(jLabelPercentageNbTotalElementaryUsedLabels, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelSaisie.add(this.jdfInputsPercentageNbTotalElementaryUsedLabels, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
        jPanelSaisie.add(jLabelPercentageNbTotalORCompositeUsedLabels, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelSaisie.add(this.jdfInputsPercentageNbTotalORCompositeUsedLabels, new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
        jPanelSaisie.add(jLabelPercentageNbTotalNOTCompositeUsedLabels, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelSaisie.add(this.jdfInputsPercentageNbTotalNOTCompositeUsedLabels, new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
	  } else {
        JLabel jLabelNbRulesTogetherFired2 = new JLabel(LocaleKBCT.GetString("NbRulesTogetherFired2") + " :");
	    this.jdfInputsNbRulesTogetherFired2.setValue(this.jdfNbRulesTogetherFired2.getValue());
        JLabel jLabelNbRulesTogetherFired3 = new JLabel(LocaleKBCT.GetString("NbRulesTogetherFired3") + " :");
	    this.jdfInputsNbRulesTogetherFired3.setValue(this.jdfNbRulesTogetherFired3.getValue());
        JLabel jLabelNbRulesTogetherFired4 = new JLabel(LocaleKBCT.GetString("NbRulesTogetherFired4") + " :");
	    this.jdfInputsNbRulesTogetherFired4.setValue(this.jdfNbRulesTogetherFired4.getValue());
        jd.getContentPane().add(jPanelSaisie, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
          ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        jPanelSaisie.add(jLabelNbRulesTogetherFired2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelSaisie.add(this.jdfInputsNbRulesTogetherFired2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
        jPanelSaisie.add(jLabelNbRulesTogetherFired3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelSaisie.add(this.jdfInputsNbRulesTogetherFired3, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
        jPanelSaisie.add(jLabelNbRulesTogetherFired4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelSaisie.add(this.jdfInputsNbRulesTogetherFired4, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
      }
      jd.getContentPane().add(jPanelValidation, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
      jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      jPanelValidation.add(jButtonCancel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      jPanelValidation.add(jButtonReset, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      jButtonApply.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
     	  if (JKBInterpretabilityFrame.this.main_TabbedPane.getSelectedIndex()==0) {
     		double[] inputs= new double[12];
  		    inputs[0]= JKBInterpretabilityFrame.this.jdfInputsTotalNbDefinedLabels.getValue(); 
  		    inputs[1]= JKBInterpretabilityFrame.this.jdfInputsPartitionInterpretability.getValue(); 
  		    inputs[2]= JKBInterpretabilityFrame.this.jdfInputsTotalNbRules.getValue(); 
   		    inputs[3]= JKBInterpretabilityFrame.this.jdfInputsTotalNbPremises.getValue(); 
    	    inputs[4]= JKBInterpretabilityFrame.this.jdfInputsPercentageNbRulesWithLessThanLPercentInputs.getValue(); 
    	    inputs[5]= JKBInterpretabilityFrame.this.jdfInputsPercentageNbRulesWithBetweenLAndMPercentInputs.getValue(); 
    	    inputs[6]= JKBInterpretabilityFrame.this.jdfInputsPercentageNbRulesWithMoreThanMPercentInputs.getValue(); 
  		    inputs[7]= JKBInterpretabilityFrame.this.jdfInputsNbTotalInputs.getValue(); 
   		    inputs[8]= JKBInterpretabilityFrame.this.jdfInputsNbTotalUsedLabels.getValue(); 
    	    inputs[9]= JKBInterpretabilityFrame.this.jdfInputsPercentageNbTotalElementaryUsedLabels.getValue(); 
    	    inputs[10]= JKBInterpretabilityFrame.this.jdfInputsPercentageNbTotalORCompositeUsedLabels.getValue(); 
    	    inputs[11]= JKBInterpretabilityFrame.this.jdfInputsPercentageNbTotalNOTCompositeUsedLabels.getValue(); 
        	if ( (inputs[0] < 0) || (inputs[1] < 0) || (inputs[2] < 0) || (inputs[3] < 0) || 
        	     (inputs[4] < 0) || (inputs[5] < 0) || (inputs[6] < 0) || (inputs[7] < 0) ||
        	     (inputs[8] < 0) || (inputs[9] < 0) || (inputs[10] < 0) || (inputs[11] < 0) )
        		MessageKBCT.Error(JKBInterpretabilityFrame.this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("InputsShouldBeGreaterThanZero"));
        	else if ( (inputs[1] > 1))
        		MessageKBCT.Error(JKBInterpretabilityFrame.this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("PartitionInterpretability01"));
        	else if ( (inputs[4] > 100) || (inputs[5] > 100) || (inputs[6] > 100) )
        		MessageKBCT.Error(JKBInterpretabilityFrame.this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("Percentages2"));
        	else if ( (inputs[4] + inputs[5] + inputs[6] != 100))
        		MessageKBCT.Error(JKBInterpretabilityFrame.this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("Percentages1"));
        	else if ( (inputs[9] > 100) || (inputs[10] > 100) || (inputs[11] > 100) )
        		MessageKBCT.Error(JKBInterpretabilityFrame.this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("Percentages2"));
        	else if ( (inputs[9] + inputs[10] + inputs[11] != 100))
        		MessageKBCT.Error(JKBInterpretabilityFrame.this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("Percentages1"));
        	else	
    	        JKBInterpretabilityFrame.this.ComputeInterpretability(inputs);
    	  } else {
     		double[] inputs= new double[3];
     		inputs[0]= JKBInterpretabilityFrame.this.jdfInputsNbRulesTogetherFired2.getValue(); 
       		inputs[1]= JKBInterpretabilityFrame.this.jdfInputsNbRulesTogetherFired3.getValue(); 
        	inputs[2]= JKBInterpretabilityFrame.this.jdfInputsNbRulesTogetherFired4.getValue();
        	if ( (inputs[0] < 0) || (inputs[1] < 0) || (inputs[2] < 0) )
        		MessageKBCT.Error(JKBInterpretabilityFrame.this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("InputsShouldBeGreaterThanZero"));
        	else	
    	        JKBInterpretabilityFrame.this.RuleBaseBehaviour(inputs);
    	  }
          jd.dispose();
      } } );
      jButtonCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
        jd.dispose();
      } } );
      jButtonReset.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
    	  if (JKBInterpretabilityFrame.this.main_TabbedPane.getSelectedIndex()==0) {
    		JKBInterpretabilityFrame.this.jdfInputsTotalNbDefinedLabels.setValue(JKBInterpretabilityFrame.this.NbTotalDefinedLabels); 
      		JKBInterpretabilityFrame.this.jdfInputsPartitionInterpretability.setValue(JKBInterpretabilityFrame.this.PartitionInterpretability); 
    	    JKBInterpretabilityFrame.this.jdfInputsTotalNbRules.setValue(JKBInterpretabilityFrame.this.NbTotalRules);
    	    JKBInterpretabilityFrame.this.jdfInputsTotalNbPremises.setValue(JKBInterpretabilityFrame.this.NbTotalPremises);
    	    JKBInterpretabilityFrame.this.jdfInputsPercentageNbRulesWithLessThanLPercentInputs.setValue(JKBInterpretabilityFrame.this.PercentageNbRulesWithLessThanLPercentInputs);
    	    JKBInterpretabilityFrame.this.jdfInputsPercentageNbRulesWithBetweenLAndMPercentInputs.setValue(JKBInterpretabilityFrame.this.PercentageNbRulesWithBetweenLAndMPercentInputs);
    	    JKBInterpretabilityFrame.this.jdfInputsPercentageNbRulesWithMoreThanMPercentInputs.setValue(JKBInterpretabilityFrame.this.PercentageNbRulesWithMoreThanMPercentInputs);
  		    JKBInterpretabilityFrame.this.jdfInputsNbTotalInputs.setValue(JKBInterpretabilityFrame.this.NbTotalInputs); 
   		    JKBInterpretabilityFrame.this.jdfInputsNbTotalUsedLabels.setValue(JKBInterpretabilityFrame.this.NbTotalLabelsUsedByInput); 
    	    JKBInterpretabilityFrame.this.jdfInputsPercentageNbTotalElementaryUsedLabels.setValue(JKBInterpretabilityFrame.this.PercentageNbTotalElementaryLabelsUsedByInput); 
    	    JKBInterpretabilityFrame.this.jdfInputsPercentageNbTotalORCompositeUsedLabels.setValue(JKBInterpretabilityFrame.this.PercentageNbTotalORCompositeLabelsUsedByInput); 
    	    JKBInterpretabilityFrame.this.jdfInputsPercentageNbTotalNOTCompositeUsedLabels.setValue(JKBInterpretabilityFrame.this.PercentageNbTotalNOTCompositeLabelsUsedByInput); 
    	  } else {
       		JKBInterpretabilityFrame.this.jdfInputsNbRulesTogetherFired2.setValue(JKBInterpretabilityFrame.this.NbTuples2);
       		JKBInterpretabilityFrame.this.jdfInputsNbRulesTogetherFired3.setValue(JKBInterpretabilityFrame.this.NbTuples3);
        	JKBInterpretabilityFrame.this.jdfInputsNbRulesTogetherFired4.setValue(JKBInterpretabilityFrame.this.NbTuples4);
    	  }
      } } );
      jd.setModal(true);
      jd.pack();
      jd.setLocation(JChildFrame.ChildPosition(this, jd.getSize()));
      jd.setVisible(true);
  }
//------------------------------------------------------------------------------
  void jMenuReset_actionPerformed() {
    //System.out.println("panel:"+this.main_TabbedPane.getSelectedIndex());
    double[] inputs;
    if (this.main_TabbedPane.getSelectedIndex()==0) {
	    inputs= new double[12];
	    inputs[0]= this.NbTotalDefinedLabels; 
	    inputs[1]= this.PartitionInterpretability; 
	    inputs[2]= this.NbTotalRules; 
	    inputs[3]= this.NbTotalPremises; 
	    inputs[4]= this.PercentageNbRulesWithLessThanLPercentInputs; 
	    inputs[5]= this.PercentageNbRulesWithBetweenLAndMPercentInputs; 
	    inputs[6]= this.PercentageNbRulesWithMoreThanMPercentInputs; 
		inputs[7]= this.NbTotalInputs; 
   		inputs[8]= this.NbTotalLabelsUsedByInput; 
    	inputs[9]= this.PercentageNbTotalElementaryLabelsUsedByInput; 
    	inputs[10]= this.PercentageNbTotalORCompositeLabelsUsedByInput; 
    	inputs[11]= this.PercentageNbTotalNOTCompositeLabelsUsedByInput; 
        this.ComputeInterpretability(inputs);
    } else {
	    inputs= new double[3];
	    inputs[0]= this.NbTuples2; 
	    inputs[1]= this.NbTuples3; 
	    inputs[2]= this.NbTuples4; 
	    this.RuleBaseBehaviour(inputs);
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
            return JKBInterpretabilityFrame.this.print(graphics, pageFormat);
        }
      };
      new JPrintPreview(this, p);
    } catch (Exception ex) { 
    	MessageKBCT.Error(null, ex);
    }
  }
//------------------------------------------------------------------------------
  void jMenuExport_actionPerformed() {
    try {
      ExportDialog export= new ExportDialog();
      JPanel panel = new JPanel() {
    	static final long serialVersionUID=0;	
        public void paint(Graphics g) {
          JKBInterpretabilityFrame.this.getContentPane().paint(g);
          g.translate(0, JKBInterpretabilityFrame.this.getContentPane().getHeight());
          JKBInterpretabilityFrame.this.getContentPane().paint(g);
          g.setColor(Color.gray);
          g.drawLine(0,0,0, JKBInterpretabilityFrame.this.getContentPane().getHeight()-1);
        }
      };
      panel.setSize(new Dimension(this.getContentPane().getWidth(), this.getContentPane().getHeight()));
      export.showExportDialog( panel, "Export view as ...", panel, "export" );
    } catch (Exception ex) { 
        //ex.printStackTrace();
    	MessageKBCT.Error(null, ex);
    }
  }
  //------------------------------------------------------------------------------
  public double getIntIndex() {
	  return this.indInt;
  }
  //------------------------------------------------------------------------------
  public double getTotalRuleNumber() {
	  return this.IshibuchiNbRules;
  }
  //------------------------------------------------------------------------------
  public double getTotalRuleLength() {
	  return this.IshibuchiTotalRuleLength;
  }
  //------------------------------------------------------------------------------
  public double getLogViewIndex() {
	  return this.LVindexTraining;
  }
  //------------------------------------------------------------------------------
  public double getMaxSFR() {
	  return this.MaxFiredRulesTraining;
  }
  public double getAvSFR() {
	  return this.AverageFiredRulesTraining;
  }
  public double getAvSFRtest() {
	  return this.AverageFiredRulesTest;
  }
  public double getMinSFR() {
	  return this.MinFiredRulesTraining;
  }
  //------------------------------------------------------------------------------
  public double getAccumulatedRuleComplexity() {
	  return this.AccumulatedRuleComplexity;
  }
  //------------------------------------------------------------------------------
  public double[] getInterpretabilityIndicators() {
        double[] intind= new double[7];
        intind[0]= this.getTotalRuleLength();
        intind[1]= this.getMaxSFR();
        intind[2]= this.getAvSFR();
        intind[3]= this.getMinSFR();
        intind[4]= this.getAccumulatedRuleComplexity();
        intind[5]= this.getIntIndex();
        intind[6]= this.getLogViewIndex();
        return intind;
  }
  //------------------------------------------------------------------------------
  public int[] getMaxSFRperRule() {
	  return this.maxSFR;
  }
  //------------------------------------------------------------------------------
  /**
   * Analyze the rules that can be fired simultaneously
   */
  private void RuleBaseBehaviour() {
	  MessageKBCT.BuildLogFile(JKBCTFrame.BuildFile("LogQuality.txt").getAbsolutePath(), null, null, null, "Interpretability");
	  Date d= new Date(System.currentTimeMillis());
      MessageKBCT.WriteLogFile("----------------------------------", "Interpretability");
	  MessageKBCT.WriteLogFile("time begin -> "+DateFormat.getTimeInstance().format(d), "Interpretability");
      int NbInputs=this.kbct.GetNbInputs();
      //System.out.println("NbInputs -> "+NbInputs);
      int NbLabels[]= new int[NbInputs];
      for (int n=0; n<NbInputs; n++) {
    	  NbLabels[n]= this.kbct.GetInput(n+1).GetLabelsNumber();
      }
      int NbMaxTuples= (int)Math.pow(2,NbInputs);
      //System.out.println("NbMaxTuples -> "+NbMaxTuples);
      int[] NbTuples= new int[NbMaxTuples-1];
      
      Vector[] tuples= new Vector[NbMaxTuples];
      // tuples[0] -> Two Rules
      tuples[0]= new Vector();
      int NbRules=this.kbct.GetNbRules();
      for (int n=0; n<NbRules; n++) {
    	  Rule r1= this.kbct.GetRule(n+1);
    	  if (r1.GetActive()) {
    		  Vector vAux= new Vector();
              for (int k=n+1; k<NbRules; k++) {
            	  Rule r2= this.kbct.GetRule(k+1);
            	  if (r2.GetActive()) {
           	          boolean res= this.RulesCanBeSimultaneouslyFired(r1, r2, NbLabels);
            	      if (res) {
            		     Vector rules= new Vector();
            		     rules.add(r1.GetNumber());
            		     rules.add(r2.GetNumber());
            		     vAux.add(rules);
            	      }
            	  }
              }
            	  tuples[0].add(vAux);
    	  }
      }
      Object[] TR= tuples[0].toArray();
      NbTuples[0]= 0;
      for (int n=0; n<TR.length; n++) {
    	  Vector vaux= (Vector)TR[n];
          Object[] par= vaux.toArray();
          NbTuples[0]= NbTuples[0]+par.length;
    	  for (int k=0; k<par.length; k++) {
    		  Object[] elements= ((Vector)par[k]).toArray();
    	  }
      }
      boolean end= false;
      int cont=0;
      while ( (!end) && (cont+2<NbMaxTuples) ) {
    	  tuples[cont+1]= this.BuildTuples(tuples[cont], tuples[0]);
          if (tuples[cont+1].isEmpty()) {
        	  end=true;
          } else {      	  
              Object[] TRaux= tuples[cont+1].toArray();
              int nbTuplas=0;
              for (int n=0; n<TRaux.length; n++) {
            	  Vector vaux= (Vector)TRaux[n];
                  Object[] tup= vaux.toArray();
                  nbTuplas= nbTuplas+tup.length;
              }
              NbTuples[cont+1]=nbTuplas;
        	  cont++;
          }
      }
      this.NbTuples2= NbTuples[0];
      if (NbTuples.length > 1)
          this.NbTuples3= NbTuples[1];
      else
    	  this.NbTuples3= 0;
      for (int n=2; n<NbMaxTuples-1; n++) {
          if (NbTuples.length > n)
              this.NbTuples4= this.NbTuples4 + NbTuples[n];
          else
        	  this.NbTuples4= this.NbTuples4 + 0;
      }
      double[] inputs= new double[3];
      inputs[0]= this.NbTuples2;
      inputs[1]= this.NbTuples3;
      inputs[2]= this.NbTuples4;
      this.RuleBaseBehaviour(inputs);
  }
  //------------------------------------------------------------------------------
  /**
   * Analyze the rules that can be fired simultaneously
   */
  private void RuleBaseBehaviour(double[] inputs) {
      // KB8
      try {
        this.InterpKB4_LocalExplanation= this.BuildInterpretabilityKB4_LocalExplanation();
        this.FIS_KB4_LocalExplanation= new double[3];
        FIS_KB4_LocalExplanation[0]= inputs[0];
        FIS_KB4_LocalExplanation[1]= inputs[1];
        FIS_KB4_LocalExplanation[2]= inputs[2];
        InterpKB4_LocalExplanation.Infer(FIS_KB4_LocalExplanation);
        double[] outputsKB4_LocalExplanation= InterpKB4_LocalExplanation.SortiesObtenues();
        this.RBbehaviour= outputsKB4_LocalExplanation[0];
        this.jdfNbRulesTogetherFired2.setValue(inputs[0]);
        this.jdfNbRulesTogetherFired3.setValue(inputs[1]);
        this.jdfNbRulesTogetherFired4.setValue(inputs[2]);
        this.jdfInfInterpretability.setValue(outputsKB4_LocalExplanation[0]);
        JKBCTOutput out= this.kbctDataBase[4].GetOutput(1);
        int labFired= out.GetLabelFired(this.RBbehaviour);
        this.InferLabel= out.GetLabelsName(labFired-1);
        this.jdfInfInterpretability.setValue(this.RBbehaviour);
        this.jLabelRBbehaviourMsg.setText("("+LocaleKBCT.GetString(this.InferLabel)+")");
        //System.out.println("RBB - pQ");
        MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("NbRulesTogetherFired2")+"= "+this.jdfNbRulesTogetherFired2.getValue(), "Interpretability");
        MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("NbRulesTogetherFired3")+"= "+this.jdfNbRulesTogetherFired3.getValue(), "Interpretability");
        MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("NbRulesTogetherFired4")+"= "+this.jdfNbRulesTogetherFired4.getValue(), "Interpretability");
        MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("outputKBLocalExplanation")+"= "+this.jdfInfInterpretability.getValue(), "Interpretability");
        MessageKBCT.WriteLogFile("	"+LocaleKBCT.GetString("Label")+"= "+LocaleKBCT.GetString(this.InferLabel), "Interpretability");
        Date d= new Date(System.currentTimeMillis());
        MessageKBCT.WriteLogFile("time end -> "+DateFormat.getTimeInstance().format(d), "Interpretability");
        MessageKBCT.CloseLogFile("Interpretability");
      } catch (Throwable t) {
          t.printStackTrace();
      	  MessageKBCT.Error(null, t);
          this.jdfNbRulesTogetherFired2.setValue(0);
          this.jdfNbRulesTogetherFired3.setValue(0);
          this.jdfNbRulesTogetherFired4.setValue(0);
          this.jdfInfInterpretability.setValue(0);
      }
  }
  //------------------------------------------------------------------------------
  /**
   * 
   **/
  private Vector BuildTuples(Vector LastTuple, Vector TwoRules) {
      Vector result= new Vector();
      Object[] TR= TwoRules.toArray();
      Object[] LT= LastTuple.toArray();
      for (int n=0; n<LT.length; n++) {
          Vector resultAux= new Vector();
    	  Vector vaux= (Vector)LT[n];
          Object[] tup= vaux.toArray();
    	  for (int k=0; k<tup.length; k++) {
    		  Object[] elements= ((Vector)tup[k]).toArray();
    		  Integer index1= new Integer(elements[0].toString());
    		  Integer index2= new Integer(elements[1].toString());
              if (!((Vector)LT[index2-1]).isEmpty()) {
            	  Vector vauxCheck= (Vector)LT[index2-1];
                  Object[] tupCheck= vauxCheck.toArray();
            	  for (int m=0; m<tupCheck.length; m++) {
            		  Object[] elementsCheck= ((Vector)tupCheck[m]).toArray();
            		  boolean flag= false;
                      int ind=0;
            		  while (!flag) {
                		  Integer ind1= new Integer(elements[ind+1].toString());
                		  Integer ind2= new Integer(elementsCheck[ind].toString());
            			  if (ind1.intValue() != ind2.intValue())
            				  flag=true;

            			  if (!flag && (ind+2==elements.length)) {
            				  flag=true;
            		    	  Vector vauxTR= (Vector)TR[index1-1];
            		    	  if (!vauxTR.isEmpty()) {
            		    		  Object[] tupTR= vauxTR.toArray();
            		              for (int i=0; i<tupTR.length; i++) {
            	            		   Object[] elementsTR= ((Vector)tupTR[i]).toArray();
            	                	   Integer first1= new Integer(elementsTR[0].toString());
            	                	   Integer last1= new Integer(elementsTR[1].toString());
            	                	   Integer first2= new Integer(elements[0].toString());
            	                	   Integer last2= new Integer(elementsCheck[elementsCheck.length-1].toString());
                                       if ( (first1.intValue()==first2.intValue()) && (last1.intValue()==last2.intValue()) ) {
                                           Vector v= new Vector();
                                    	   v.add(first1.intValue());
         		    		    		   for (int j=0; j<elementsCheck.length; j++) {
                                               int value= (new Integer(elementsCheck[j].toString())).intValue();
         		    		    			   v.add(value);
         		    		    		   }
         		    		    		   resultAux.add(v);
                                    	   break;
            		    		       }
            		              }
            		    	  }
            			  }
                          ind++;
            		  }
            	  }
              }
    	  }
    	  result.add(resultAux);
      }
	  return result;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Check if rules "r1" and "r2" can be fired simultaneously.
   * </p>
   * @param r1 first rule
   * @param r2 second rule
   * @return true ("r1" and "r2" can be fired by the same input) / false
   */
  private boolean RulesCanBeSimultaneouslyFired( Rule r1, Rule r2, int[] NbLabels ) {
    int NbLim= NbLim= r1.GetNbInputs();
    int[] r1_labels= r1_labels= r1.Get_in_labels_number();
    int[] r2_labels= r2_labels= r2.Get_in_labels_number();
    // increasing order
    for (int n=0; n<NbLim; n++) {
      if (r1_labels[n]!=r2_labels[n]) {
         if ( !this.LabelsCanBeSimultaneouslyFired(r1_labels[n], r2_labels[n], NbLabels[n])) {
            return false;
         }
      }
    }
    return true;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Return number of new label obtained as merge of "lab_num1" and "lab_num2".
   * </p>
   * @param lab_num1 number of first label
   * @param lab_num2 number of second label
   * @param NOL number of labels
   * @return true (labels can be fired simultaneously) / false (otherwise)
   */
  private boolean LabelsCanBeSimultaneouslyFired( int lab_num1, int lab_num2, int NOL ) {
    boolean result=false;
    int min= Math.min(lab_num1, lab_num2);
    int max= Math.max(lab_num1, lab_num2);
    int NbOR= 0;
    if (min==0) {
      result=true;
    } else if ( (min <= NOL) && (max <= NOL) && (max-min==1) ) {
        result= true;
    } else if ( (min <= NOL) && (max==min+NOL) ){
        result= true;
    } else if ( (min <= NOL) && (max > NOL) && (max <= 2*NOL) ){
        if ( (NOL==2) || (max-NOL != min) )
          result= true;
    } else if ( (min <= NOL) && (max > 2*NOL) ) {
        int SelLabel= max-2*NOL;
        int auxNbOR= jnikbct.NbORLabels(SelLabel, NOL);
        int optionaux= jnikbct.option(SelLabel, auxNbOR, NOL);
        if ( (min >= optionaux) && (min <= optionaux+auxNbOR-1) ) {
          result= true;
        } else if (Math.abs(optionaux-min) == 1) {
          result = true;
        } else if (Math.abs(optionaux+auxNbOR-1-min) == 1) {
          result = true;
        }
    } else if ( (min > NOL) && (min <= 2*NOL) && (max > NOL) && (max <= 2*NOL) ) {
    	result= true;
    } else if ( (min > NOL) && (min <= 2*NOL) && (max > 2*NOL) ) {
        int optionMin= min-NOL;
        int SelLabelmax= max-2*NOL;
        int NbORmax= jnikbct.NbORLabels(SelLabelmax, NOL);
        int optionMax= jnikbct.option(SelLabelmax, NbORmax, NOL);
        if ( (optionMax >= optionMin) || (optionMax+NbORmax-1 <= optionMin) )
          result= true;
    } else if ( (min > 2*NOL) && (max > 2*NOL) ) {
        int SelLabelmin= min-2*NOL;
        int NbORmin= jnikbct.NbORLabels(SelLabelmin, NOL);
        int optionMin= jnikbct.option(SelLabelmin, NbORmin, NOL);
        int SelLabelmax= max-2*NOL;
        int NbORmax= jnikbct.NbORLabels(SelLabelmax, NOL);
        int optionMax= jnikbct.option(SelLabelmax, NbORmax, NOL);
        if ( (NbORmin < NbORmax) && (optionMin >= optionMax) && (optionMin+NbORmin-1<=optionMax+NbORmax-1) ) {
            result= true;
        } else if (optionMax == optionMin+NbORmin) {
            result= true;
        } else if (optionMin == optionMax+NbORmax) {
            result= true;
        } else if ( (optionMax > optionMin) &&
                    (optionMax <= optionMin+NbORmin-1) ) {
            result= true;
        } else if ( (optionMax < optionMin) &&
                    (optionMin <= optionMax+NbORmax-1) ) {
            result= true;
        }
    }

    if (NbOR == NOL-1) {
      // NOT
        result= true;
    } 
    return result;
  }
//------------------------------------------------------------------------------
  boolean selectVariablesInTestDataFile() throws Throwable {
	  boolean warning= false;
	  //System.out.println("JKBInterpretabilityFrame: selectVariablesInTestDataFile");
	  String selVars= MainKBCT.getJMF().getSelectedVariablesInDataFile();
	  selVars= selVars.replaceAll(", ",",");
	  //System.out.println(" -> SelVars= "+selVars);
	  String[] selV= selVars.split(",");
      int NbIn= this.kbct.GetNbInputs();
      //System.out.println("NbIn="+NbIn);
      int NbOut= this.kbct.GetNbOutputs();
      //System.out.println("NbOut="+NbOut);
      int DataVar= this.ddtest.VariableCount();
      //System.out.println("DataVar="+DataVar);
      Integer[] vars= new Integer[NbIn+NbOut];
      if (selV.length == NbIn+NbOut) {
          if (DataVar <= NbIn + NbOut) {
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
        PrintWriter fOut = new PrintWriter(new FileOutputStream(this.ddtest.FileName()+".jrbqtst"), true);
        int NbLines= this.ddtest.VariableData(0).length;
        int NbCol= NbIn+NbOut;
        for (int k = 0; k < NbLines; k++) {
          for (int n = 0; n < NbCol; n++) {
            int d= vars[n].intValue()-1;
            fOut.print(df.format(this.ddtest.VariableData(d)[k]));
            if (n==NbCol-1)
              fOut.println();
            else
              fOut.print(",");
          }
        }
        fOut.flush();
        fOut.close();
        this.ddtest= new JExtendedDataFile(this.ddtest.FileName()+".jrbqtst", true);
      }
    
	//System.out.println("Select 3");
    return warning;
  }
//------------------------------------------------------------------------------
  public int print(java.awt.Graphics g, PageFormat pageFormat) throws PrinterException {
     java.awt.Graphics2D  g2 = ( java.awt.Graphics2D )g;
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
     this.getContentPane().print( g2 );
     return Printable.PAGE_EXISTS;
  }
//------------------------------------------------------------------------------
  void jMenuHelp_actionPerformed() {
	  if (this.main_TabbedPane.getSelectedIndex()==0)
		  MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonQuality.html#GlobDesc"));
	  else if (this.main_TabbedPane.getSelectedIndex()==1)
		  MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonQuality.html#LocExp"));
	  else if (this.main_TabbedPane.getSelectedIndex()==2)
		  MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonQuality.html#OtherInd"));
	  else
		  MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonQuality.html#Interpretability"));

	  MainKBCT.getJB().setVisible(true);
	  SwingUtilities.updateComponentTreeUI(this);
  }
}