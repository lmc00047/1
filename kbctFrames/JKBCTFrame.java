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
//                              JKBCTFrame.java
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import kbct.JFIS;
import kbct.JKBCT;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbctAux.DoubleField;
import kbctAux.IntegerField;
import kbctAux.MessageKBCT;
import kbctAux.Translatable;
import KB.Rule;
import fis.JRule;
import fis.jnifis;

import org.apache.batik.swing.JSVGCanvas;

/**
 * kbctFrames.JKBCTFrame.
 * 
 *@author Jose Maria Alonso Moral
 *@version 3.0 , 03/08/15
 */
public class JKBCTFrame extends JFrame implements Translatable {
	static final long serialVersionUID = 0;
	protected static Vector<JExpertFrame> JExpertFrames = new Vector<JExpertFrame>();
	protected static Vector<JMainFrame> JMainFrames = new Vector<JMainFrame>();
	protected static Vector Translatables = new Vector();
	// etiquetas
	protected ImageIcon icon_guaje = LocaleKBCT.getIconGUAJE();
	// menu options
	protected JMenu jMenuOptions = new JMenu();
	protected JMenu jMenuLanguage = new JMenu();
	// protected JRadioButtonMenuItem jRBMenuFrench = new JRadioButtonMenuItem();
	protected JRadioButtonMenuItem jRBMenuEnglish = new JRadioButtonMenuItem();
	protected JRadioButtonMenuItem jRBMenuSpanish = new JRadioButtonMenuItem();
	protected JMenu jMenuLook = new JMenu();
	protected UIManager.LookAndFeelInfo[] lafi;
	protected JRadioButtonMenuItem jRB[];
	protected JMenu jMenuUser = new JMenu();
	protected JRadioButtonMenuItem jRBMenuBeginner = new JRadioButtonMenuItem();
	protected JRadioButtonMenuItem jRBMenuExpert = new JRadioButtonMenuItem();
	// menu ayuda
	protected JMenu jMenuHelp = new JMenu();
	protected JMenuItem jMenuItemHelp = new JMenuItem();
	protected JMenuItem jMenuItemQuickStart = new JMenuItem();
	protected JMenuItem jMenuAbout = new JMenuItem();
	protected JMenuItem jMenuLicense = new JMenuItem();

	public JExpertFrame jef = null;
	protected JMainFrame Parent = null;
	protected JRuleFrameInfer jrfi;
	public static String KBExpertFile = "";
	public static String OntologyFile = "";

	protected JTextField jTFExpertName = new JTextField("C:/GUAJE/IRIS.txt.kb.xml"); //aquiiiiiiiiiii
	// object kbct temporal
	public JKBCT Temp_kbct = null;
	// coherencia
	protected JButton jButtonConsistency = new JButton();
	// simplification
	protected JButton jButtonSimplify = new JButton();
	// reduction by logView
	protected JButton jButtonLogView = new JButton();
	// optimization data base
	protected JButton jButtonOptimization = new JButton();
	// completeness of the rule base
	protected JButton jButtonCompleteness = new JButton();
	// evaluation of quality (rule base)
	protected JButton jButtonQuality = new JButton();
	// Inferencias para probar la base de reglas
	protected JButton jButtonInfer = new JButton();
	// Linguisitic summary
	protected JButton jButtonSummary = new JButton();
	// Optimization SW Parameter
	protected DefaultComboBoxModel jDCBMoptionModel = new DefaultComboBoxModel() {
		static final long serialVersionUID = 0;
		public Object getElementAt(int index) {
			String opt;
			switch (index) {
			case 0:
				opt = "VariableByVariable";
				break;
			case 1:
				opt = "LabelByLabel";
				break;
			default:
				opt = "VariableByVariable";
				break;
			}
			try {
				return LocaleKBCT.GetString(opt);
			} catch (Throwable t) {
				return null;
			}
		}
	};
	protected JComboBox jCBopt = new JComboBox(jDCBMoptionModel);
	protected IntegerField jNbIterations = new IntegerField();
	// Optimization GT Parameters
	protected IntegerField jNbGenerations = new IntegerField();
	protected IntegerField jPopLength = new IntegerField();
	protected IntegerField jTournamentSize = new IntegerField();
	protected DoubleField jMutProb = new DoubleField();
	protected DoubleField jCrossoverProb = new DoubleField();
	protected DoubleField jW1ACC = new DoubleField();
	protected DoubleField jW2INT = new DoubleField();
	protected DoubleField jParAlfa = new DoubleField();
	// Optimization SW Parameter
	protected DefaultComboBoxModel jDCBMBoundedOptimizationModel = new DefaultComboBoxModel() {
		static final long serialVersionUID = 0;
		public Object getElementAt(int index) {
			String opt;
			switch (index) {
			case 0:
				opt = "Yes";
				break;
			case 1:
				opt = "No";
				break;
			default:
				opt = "Yes";
				break;
			}
			try {
				return LocaleKBCT.GetString(opt);
			} catch (Throwable t) {
				return null;
			}
		}
	};
	protected JComboBox jCBBoundedOptimization = new JComboBox(jDCBMBoundedOptimizationModel);
	protected IntegerField jNbInitialGeneration = new IntegerField();
	protected IntegerField jNbMilestoneGeneration = new IntegerField();
	protected DoubleField jCompletenessThres = new DoubleField();
	// SMOTE parameters
	protected IntegerField jNumberOfNeighbors = new IntegerField();
	protected DefaultComboBoxModel jDCBMtypeModel = new DefaultComboBoxModel() {
		static final long serialVersionUID = 0;
		public Object getElementAt(int index) {
			String opt;
			switch (index) {
			case 0:
				opt = "minority";
				break;
			case 1:
				opt = "ASMO";
				break;
			case 2:
				opt = "both";
				break;
			default:
				opt = "minority";
				break;
			}
			try {
				return opt;
			} catch (Throwable t) {
				return null;
			}
		}
	};
	protected JComboBox jCBtype = new JComboBox(jDCBMtypeModel);
	protected DefaultComboBoxModel jDCBMsmoteBalancingModel = new DefaultComboBoxModel() {
		static final long serialVersionUID = 0;
		public Object getElementAt(int index) {
			String opt;
			switch (index) {
			case 0:
				opt = "No";
				break;
			case 1:
				opt = "Yes";
				break;
			default:
				opt = "No";
				break;
			}
			try {
				return LocaleKBCT.GetString(opt);
			} catch (Throwable t) {
				return null;
			}
		}
	};
	protected JComboBox jCBsmoteBalancing = new JComboBox(jDCBMsmoteBalancingModel);
	JCheckBox jCBbalancingALL = new JCheckBox();
	protected DoubleField jQuantity = new DoubleField();
	protected DefaultComboBoxModel jDCBMsmoteDistanceModel = new DefaultComboBoxModel() {
		static final long serialVersionUID = 0;
		public Object getElementAt(int index) {
			String opt;
			switch (index) {
			case 0:
				opt = "Euclidean";
				break;
			case 1:
				opt = "HVDM";
				break;
			default:
				opt = "Euclidean";
				break;
			}
			try {
				return opt;
			} catch (Throwable t) {
				return null;
			}
		}
	};
	protected JComboBox jCBsmoteDistance = new JComboBox(jDCBMsmoteDistanceModel);
	protected DefaultComboBoxModel jDCBMinterpolationModel = new DefaultComboBoxModel() {
		static final long serialVersionUID = 0;
		public Object getElementAt(int index) {
			String opt;
			switch (index) {
			case 0:
				opt = "standard";
				break;
			case 1:
				opt = "BLX-alpha";
				break;
			case 2:
				opt = "SBX";
				break;
			default:
				opt = "standard";
				break;
			}
			try {
				return opt;
			} catch (Throwable t) {
				return null;
			}
		}
	};
	protected JComboBox jCBinterpolation = new JComboBox(jDCBMinterpolationModel);
	protected DoubleField jAlpha = new DoubleField();
	protected DoubleField jMu = new DoubleField();
	protected DefaultComboBoxModel jDCBMintIndexModel = new DefaultComboBoxModel() {
		    static final long serialVersionUID=0;	
		    public Object getElementAt(int index) {
		        String intIndex;
		        switch(index) {
		          case 0:  intIndex = "IshibuchiNbRules"; break;
		          case 1:  intIndex = "IshibuchiTotalRuleLength"; break;
		          case 2:  intIndex = "MaxFiredRulesTraining"; break;
		          case 3:  intIndex = "AverageFiredRulesTraining"; break;
		          case 4:  intIndex = "MinFiredRulesTraining"; break;
		          case 5:  intIndex = "AccumulatedRuleComplexity"; break;
		          case 6:  intIndex = "InterpretabilityIndex"; break;
		          case 7:  intIndex = "LVindexTraining"; break;
		          default: intIndex = "IshibuchiTotalRuleLength"; break;
		        }
		        try {
		        	if (index==6)
		        	    return LocaleKBCT.GetString(intIndex)+" (HILK)";
		        	else
		        		return LocaleKBCT.GetString(intIndex);
		        } catch( Throwable t ) { return null; }
		      }
		    };
    protected JComboBox jCBintIndex = new JComboBox(jDCBMintIndexModel);
	// Fingrams
	protected JButton jButtonFingrams = new JButton();

	public boolean flagOnlyInterpretability = false;
	protected JFISConsole jfcLicense;
	protected JFISConsole jfcGPL;
	protected JFISConsole jfcFS1;
	protected JFISConsole jfcFS2;

	//private JCheckBox jcbm1small = new JCheckBox();
	//private JCheckBox jcbm1smpfcons = new JCheckBox();
	//private JCheckBox jcbm1smpfconsRed = new JCheckBox();
	private JCheckBox jcbm1nsall = new JCheckBox();
	private JCheckBox jcbm1nspfcons = new JCheckBox();
	private JCheckBox jcbm1nspfconsRed = new JCheckBox();
	private JCheckBox jcbm1nspfinstance = new JCheckBox();
	//private JCheckBox jcbm2small = new JCheckBox();
	//private JCheckBox jcbm2smpfcons = new JCheckBox();
	//private JCheckBox jcbm2smpfconsRed = new JCheckBox();
	//private JCheckBox jcbm2nsall = new JCheckBox();
	//private JCheckBox jcbm2nspfcons = new JCheckBox();
	//private JCheckBox jcbm2nspfconsRed = new JCheckBox();
	//private JCheckBox jcbm3small = new JCheckBox();
	//private JCheckBox jcbm3smpfcons = new JCheckBox();
	//private JCheckBox jcbm3smpfconsRed = new JCheckBox();
	//private JCheckBox jcbm3nsall = new JCheckBox();
	//private JCheckBox jcbm3nspfcons = new JCheckBox();
	//private JCheckBox jcbm3nspfconsRed = new JCheckBox();
	//private JCheckBox jcbm4small = new JCheckBox();
	//private JCheckBox jcbm4smpfcons = new JCheckBox();
	//private JCheckBox jcbm4smpfconsRed = new JCheckBox();
	//private JCheckBox jcbm4nsall = new JCheckBox();
	//private JCheckBox jcbm4nspfcons = new JCheckBox();
	//private JCheckBox jcbm4nspfconsRed = new JCheckBox();
	
	private boolean regOpt;
	protected static final JFingramsFrame[] fingrams= new JFingramsFrame[4];
	protected JFrame jdSVG;
	protected DoubleField jPathFinderThres = new DoubleField();
	protected DoubleField jGoodnessHighThres = new DoubleField();
	protected DoubleField jGoodnessLowThres = new DoubleField();
	protected JComboBox jCBparQ;
	private int NbNodes;
	private int NbActiveRules;
	
	protected boolean expertWindow= false;

	// ------------------------------------------------------------------------------
	public JKBCTFrame() {
		this.lafi = UIManager.getInstalledLookAndFeels();
		this.jRB = new JRadioButtonMenuItem[lafi.length];
		for (int i = 0; i < lafi.length; i++) {
			this.jRB[i] = new JRadioButtonMenuItem(lafi[i].getName());
			this.jRB[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jRBMenuLook_actionPerformed(e);
				}
			});
		}
	}
	// ------------------------------------------------------------------------------
	protected boolean isAvailableLookAndFeel(String laf) {
		try {
			Class lnfClass = Class.forName(laf);
			LookAndFeel newLAF = (LookAndFeel) (lnfClass.newInstance());
			return newLAF.isSupportedLookAndFeel();
		} catch (Exception e) {
			return false;
		}
	}
	// ------------------------------------------------------------------------------
	public void Translate() {}
	// ------------------------------------------------------------------------------
	protected void TranslateOptions() {
		// menu options
		this.jMenuOptions.setText(LocaleKBCT.GetString("Options"));
		this.jMenuLanguage.setText(LocaleKBCT.GetString("Language"));
		// this.jRBMenuFrench.setText("Français");
		this.jRBMenuEnglish.setText("English");
		this.jRBMenuSpanish.setText("Español");
		this.jMenuLook.setText(LocaleKBCT.GetString("Look"));
		this.jMenuUser.setText(LocaleKBCT.GetString("User"));
		this.jRBMenuBeginner.setText(LocaleKBCT.GetString("Beginner"));
		this.jRBMenuExpert.setText(LocaleKBCT.GetString("Expert"));
		// menu ayuda
		this.jMenuHelp.setText(LocaleKBCT.GetString("Help"));
		this.jMenuItemHelp.setText(LocaleKBCT.GetString("Help"));
		this.jMenuItemQuickStart.setText(LocaleKBCT.GetString("QuickStart"));
		this.jMenuAbout.setText(LocaleKBCT.GetString("About"));
		this.jMenuLicense.setText(LocaleKBCT.GetString("License"));
	}
	// ------------------------------------------------------------------------------
	protected void TranslateFileChooser() {
		UIManager.put("FileChooser.cancelButtonMnemonic", new Integer(
				LocaleKBCT.GetString("FCCancelButtonMnemonic").charAt(0)));
		UIManager.put("FileChooser.cancelButtonText", LocaleKBCT
				.GetString("FCCancelButtonText"));
		UIManager.put("FileChooser.cancelButtonToolTipText", LocaleKBCT
				.GetString("FCCancelButtonToolTipText"));
		UIManager.put("FileChooser.detailsViewButtonToolTipText", LocaleKBCT
				.GetString("FCDetailsViewButtonToolTipText"));
		UIManager.put("FileChooser.fileNameLabelMnemonic", new Integer(
				LocaleKBCT.GetString("FCFileNameLabelMnemonic").charAt(0)));
		UIManager.put("FileChooser.fileNameLabelText", LocaleKBCT
				.GetString("FCFileNameLabelText"));
		UIManager.put("FileChooser.filesOfTypeLabelMnemonic", new Integer(
				LocaleKBCT.GetString("FCFilesOfTypeLabelMnemonic").charAt(0)));
		UIManager.put("FileChooser.filesOfTypeLabelText", LocaleKBCT
				.GetString("FCFilesOfTypeLabelText"));
		UIManager.put("FileChooser.homeFolderToolTipText", LocaleKBCT
				.GetString("FCHomeFolderToolTipText"));
		UIManager.put("FileChooser.listViewButtonToolTipText", LocaleKBCT
				.GetString("FCListViewButtonToolTipText"));
		UIManager.put("FileChooser.newFolderToolTipText", LocaleKBCT
				.GetString("FCNewFolderToolTipText"));
		UIManager.put("FileChooser.openButtonMnemonic", new Integer(LocaleKBCT
				.GetString("FCOpenButtonMnemonic").charAt(0)));
		UIManager.put("FileChooser.openButtonText", LocaleKBCT
				.GetString("FCOpenButtonText"));
		UIManager.put("FileChooser.openButtonToolTipText", LocaleKBCT
				.GetString("FCOpenButtonToolTipText"));
		UIManager.put("FileChooser.saveButtonMnemonic", new Integer(LocaleKBCT
				.GetString("FCSaveButtonMnemonic").charAt(0)));
		UIManager.put("FileChooser.saveButtonText", LocaleKBCT
				.GetString("FCSaveButtonText"));
		UIManager.put("FileChooser.saveButtonToolTipText", LocaleKBCT
				.GetString("FCSaveButtonToolTipText"));
		UIManager.put("FileChooser.upFolderToolTipText", LocaleKBCT
				.GetString("FCUpFolderToolTipText"));
		// propriedades de FileChooser CDE/Motif
		UIManager.put("FileChooser.filterLabelText", LocaleKBCT
				.GetString("FCFilesOfTypeLabelText"));
		UIManager.put("FileChooser.foldersLabelText", LocaleKBCT
				.GetString("FCFolderLabelText"));
		UIManager.put("FileChooser.filesLabelText", LocaleKBCT
				.GetString("FCFilesLabelText"));
		UIManager.put("FileChooser.enterFileNameLabelText", LocaleKBCT
				.GetString("FCFileNameLabelText"));
	}
	// ------------------------------------------------------------------------------
	protected void OptionsEvents() {
		// eventos de la ventana principal
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				this_windowClosing();
			}
			public void windowActivated(WindowEvent e) {
				this_windowActivated();
			}
		});
		// menu options
		// jRBMenuFrench.addActionListener(new ActionListener() { public void
		// actionPerformed(ActionEvent e) { jRBMenuFrench_actionPerformed(); }}
		// );
		jRBMenuEnglish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jRBMenuEnglish_actionPerformed();
			}
		});
		jRBMenuSpanish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jRBMenuSpanish_actionPerformed();
			}
		});
		jRBMenuBeginner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jRBMenuBeginner_actionPerformed(e);
			}
		});
		jRBMenuExpert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jRBMenuExpert_actionPerformed(e);
			}
		});
		// menu ayuda
		jMenuItemHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemHelp_actionPerformed(e);
			}
		});
		jMenuItemQuickStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemQuickStart_actionPerformed(e);
			}
		});
		jMenuLicense.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuLicense_actionPerformed();
			}
		});
		jMenuAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuAbout_actionPerformed();
			}
		});
		jMenuHelp.addMenuListener(new MenuListener() {
			public void menuSelected(MenuEvent e) {
				jMenuHelp_menuSelected();
			}
			public void menuDeselected(MenuEvent e) {}
			public void menuCanceled(MenuEvent e) {}
		});
	}
	// ------------------------------------------------------------------------------
	/*
	 * void jRBMenuFrench_actionPerformed() { try {
	 * jRBMenuFrench.setSelected(true); jRBMenuEnglish.setSelected(false);
	 * jRBMenuSpanish.setSelected(false); MainKBCT.setLocale(new LocaleKBCT(
	 * "fr", "FR" )); MainKBCT.getConfig().SetLanguage("fr");
	 * MainKBCT.getConfig().SetCountry("FR"); TranslateTranslatables();
	 * SwingUtilities.updateComponentTreeUI(this); if (this.jef!=null) {
	 * this.jef.jRBMenuFrench.setSelected(true);
	 * this.jef.jRBMenuEnglish.setSelected(false);
	 * this.jef.jRBMenuSpanish.setSelected(false);
	 * SwingUtilities.updateComponentTreeUI(this.jef); } else if
	 * (this.Parent!=null) { this.Parent.jRBMenuFrench.setSelected(true);
	 * this.Parent.jRBMenuEnglish.setSelected(false);
	 * this.Parent.jRBMenuSpanish.setSelected(false);
	 * SwingUtilities.updateComponentTreeUI(this.Parent); } } catch( Throwable
	 * ex ) { MessageKBCT.Error(this, LocaleKBCT.GetString("Error"),
	 * "Error in JKBCTFrame in jRBMenuFrench_actionPerformed: "+ex); } }
	 */
	// ------------------------------------------------------------------------------
	void jRBMenuEnglish_actionPerformed() {
		try {
			// jRBMenuFrench.setSelected(false);
			jRBMenuEnglish.setSelected(true);
			jRBMenuSpanish.setSelected(false);
			MainKBCT.setLocale(new LocaleKBCT("en", "GB"));
			MainKBCT.getConfig().SetLanguage("en");
			MainKBCT.getConfig().SetCountry("GB");
			TranslateTranslatables();
			SwingUtilities.updateComponentTreeUI(this);
			if (this.jef != null) {
				// this.jef.jRBMenuFrench.setSelected(false);
				this.jef.jRBMenuEnglish.setSelected(true);
				this.jef.jRBMenuSpanish.setSelected(false);
				SwingUtilities.updateComponentTreeUI(this.jef);
			} else if (this.Parent != null) {
				// this.Parent.jRBMenuFrench.setSelected(false);
				this.Parent.jRBMenuEnglish.setSelected(true);
				this.Parent.jRBMenuSpanish.setSelected(false);
				SwingUtilities.updateComponentTreeUI(this.Parent);
			}
		} catch (Throwable ex) {
			MessageKBCT.Error(this, LocaleKBCT.GetString("Error"),
					"Error in JKBCTFrame in jRBMenuEnglish_actionPerformed: "
							+ ex);
		}
	}
	// ------------------------------------------------------------------------------
	void jRBMenuSpanish_actionPerformed() {
		try {
			// jRBMenuFrench.setSelected(false);
			jRBMenuEnglish.setSelected(false);
			jRBMenuSpanish.setSelected(true);
			MainKBCT.setLocale(new LocaleKBCT("es", "ES"));
			MainKBCT.getConfig().SetLanguage("es");
			MainKBCT.getConfig().SetCountry("ES");
			TranslateTranslatables();
			SwingUtilities.updateComponentTreeUI(this);
			if (this.jef != null) {
				// this.jef.jRBMenuFrench.setSelected(false);
				this.jef.jRBMenuEnglish.setSelected(false);
				this.jef.jRBMenuSpanish.setSelected(true);
				SwingUtilities.updateComponentTreeUI(this.jef);
			} else if (this.Parent != null) {
				// this.Parent.jRBMenuFrench.setSelected(false);
				this.Parent.jRBMenuEnglish.setSelected(false);
				this.Parent.jRBMenuSpanish.setSelected(true);
				SwingUtilities.updateComponentTreeUI(this.Parent);
			}
		} catch (Throwable ex) {
			MessageKBCT.Error(this, LocaleKBCT.GetString("Error"),
					"Error in JKBCTFrame in jRBMenuSpanish_actionPerformed: "
							+ ex);
		}
	}
	// ------------------------------------------------------------------------------
	void jRBMenuLook_actionPerformed(ActionEvent e) {
		String lnfName = e.getActionCommand();
		try {
			UIManager.setLookAndFeel(lnfName);
			MainKBCT.getConfig().SetLookAndFeel(lnfName);
			SwingUtilities.updateComponentTreeUI(this);
			this.updateState();
			if (this.jef != null) {
				SwingUtilities.updateComponentTreeUI(this.jef);
				this.jef.updateState();
			} else if (this.Parent != null) {
				SwingUtilities.updateComponentTreeUI(this.Parent);
				this.Parent.updateState();
			}
		} catch (Exception exc) {
			MessageKBCT.Error(this, LocaleKBCT.GetString("Error"),
					"Error in JKBCTFrame in jRBMenuLook_actionPerformed: "
							+ exc);
		}
	}
	// ------------------------------------------------------------------------------
	void jMenuHelp_menuSelected() {
		if (this.jRBMenuBeginner.isSelected()) {
			this.jMenuItemHelp.setEnabled(false);
		} else {
			this.jMenuItemHelp.setEnabled(true);
		}
		SwingUtilities.updateComponentTreeUI(this);
	}
	// ------------------------------------------------------------------------------
	void jRBMenuBeginner_actionPerformed(ActionEvent e) {
		String use = e.getActionCommand();
		if (use.equals(LocaleKBCT.GetString("Help")))
			use = LocaleKBCT.GetString("Beginner");
		try {
			MainKBCT.getConfig().SetUser(use);
			jRBMenuBeginner.setSelected(true);
			jRBMenuExpert.setSelected(false);
			jMenuItemHelp.setEnabled(false);
			SwingUtilities.updateComponentTreeUI(this);
            if (this.expertWindow)
    			MainKBCT.setJB(new JBeginnerFrame("windowExpert.html"));
            else            	
			    MainKBCT.setJB(new JBeginnerFrame("index.html"));
            
			MainKBCT.getJB().setVisible(true);
			this.updateHelpMenu(true, false, false);
		} catch (Throwable ex) {
			MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JKBCTFrame in jRBMenuBeginner_actionPerformed: " + ex);
		}
	}
	// ------------------------------------------------------------------------------
	void jRBMenuExpert_actionPerformed(ActionEvent e) {
		String user = e.getActionCommand();
		try {
			MainKBCT.getConfig().SetUser(user);
			jRBMenuBeginner.setSelected(false);
			jRBMenuExpert.setSelected(true);
			this.jMenuItemHelp.setEnabled(true);
			SwingUtilities.updateComponentTreeUI(this);
			MainKBCT.getJB().dispose();
			this.updateHelpMenu(false, true, true);
		} catch (Throwable ex) {
			MessageKBCT.Error(this, LocaleKBCT.GetString("Error"),
					"Error in JKBCTFrame in jRBMenuExpert_actionPerformed: "
							+ ex);
		}
	}
	// ------------------------------------------------------------------------------
	public void updateState() {
		String lnfName = UIManager.getLookAndFeel().getClass().getName();
		for (int i = 0; i < this.jRB.length; i++) {
			if (lnfName.equals(this.lafi[i].getClassName()))
				this.jRB[i].setSelected(true);
			else
				this.jRB[i].setSelected(false);
		}
	}
	// ------------------------------------------------------------------------------
	void this_windowClosing() {
		if (this.jfcGPL != null)
			this.jfcGPL.dispose();

		if (this.jfcLicense != null)
			this.jfcLicense.dispose();

		if (this.jfcFS1 != null)
			this.jfcFS1.dispose();

		if (this.jfcFS2 != null)
			this.jfcFS2.dispose();

		this.dispose();
	}
	// ------------------------------------------------------------------------------
	void this_windowActivated() {
		if (MainKBCT.getConfig().GetUserChange().equals("Yes")) {
			if (MainKBCT.getConfig().GetUser().equals(LocaleKBCT.GetString("Beginner"))) {
				jRBMenuBeginner.setSelected(true);
				jRBMenuExpert.setSelected(false);
				jMenuItemHelp.setEnabled(false);
				this.updateHelpMenu(true, false, false);
			} else {
				jRBMenuBeginner.setSelected(false);
				jRBMenuExpert.setSelected(true);
				jMenuItemHelp.setEnabled(true);
				this.updateHelpMenu(false, true, true);
			}
			if (this.jef != null)
				SwingUtilities.updateComponentTreeUI(this.jef);

			if (this.Parent != null)
				SwingUtilities.updateComponentTreeUI(this.Parent);

			SwingUtilities.updateComponentTreeUI(this);
		}
		if (this.jTFExpertName != null)
			this.jTFExpertName.setText(JKBCTFrame.KBExpertFile);

		if (this.Temp_kbct != null) {
			if ( (this.Temp_kbct.GetNbActiveRules() > 0) ) {
				
				if ( ( (this.jef != null) && (this.jef.getJExtDataFile() != null) ) ||
				     ( (this.Parent != null) && (this.Parent.jef != null) && (this.Parent.jef.getJExtDataFile() != null) ) ) {
				      this.jButtonCompleteness.setEnabled(true);
				      this.jButtonConsistency.setEnabled(true);
				      this.jButtonSimplify.setEnabled(true);
				      this.jButtonLogView.setEnabled(true);
				      this.jButtonOptimization.setEnabled(true);
				      this.jButtonQuality.setEnabled(true);
				      this.jButtonFingrams.setEnabled(true);
				} else {
					  this.jButtonCompleteness.setEnabled(false);
					  this.jButtonConsistency.setEnabled(false);
					  this.jButtonSimplify.setEnabled(false);
					  this.jButtonLogView.setEnabled(false);
					  this.jButtonOptimization.setEnabled(false);
					  this.jButtonQuality.setEnabled(false);
					  this.jButtonFingrams.setEnabled(false);
				}
			    this.jButtonInfer.setEnabled(true);
				this.jButtonSummary.setEnabled(true);
			} else {
				this.jButtonCompleteness.setEnabled(false);
				this.jButtonConsistency.setEnabled(false);
				this.jButtonSimplify.setEnabled(false);
				this.jButtonLogView.setEnabled(false);
				this.jButtonOptimization.setEnabled(false);
				this.jButtonQuality.setEnabled(false);
				this.jButtonInfer.setEnabled(false);
				this.jButtonSummary.setEnabled(false);
				this.jButtonFingrams.setEnabled(false);
			}
		}
		this.repaint();
	}
	// ------------------------------------------------------------------------------
	void jMenuItemHelp_actionPerformed(ActionEvent e) {
		jRBMenuBeginner_actionPerformed(e);
	}
	// ------------------------------------------------------------------------------
	void jMenuItemQuickStart_actionPerformed(ActionEvent e) {
		if (MainKBCT.getJT() != null) {
			MainKBCT.getJT().this_windowClose(false);
		}
		MainKBCT.setJT(new JTutorialFrame("Tutorial-Intro.html"));
		MainKBCT.getJT().setVisible(true);
	}
	// ------------------------------------------------------------------------------
	void jMenuAbout_actionPerformed() {
		new JAboutFrame(this);
	}
	// ------------------------------------------------------------------------------
	void jMenuLicense_actionPerformed() {
		try {
			if (this.jfcGPL != null)
				this.jfcGPL.dispose();

			String gplicense = MainKBCT.getConfig().GetKbctPath()
					+ System.getProperty("file.separator") + "License"
					+ System.getProperty("file.separator") + "GPL-v3.txt";
			this.jfcGPL = new JFISConsole(this, gplicense, true);
			if (this.jfcLicense != null)
				this.jfcLicense.dispose();

			this.jfcLicense = new JFISConsole(this, MainKBCT.getConfig()
					.GetKbctPath()
					+ System.getProperty("file.separator")
					+ "License"
					+ System.getProperty("file.separator") + "LICENSE.txt",
					true);
			this.jfcLicense.setLocation(this.jfcLicense.getX()
					+ this.jfcLicense.getInsets().top, this.jfcLicense.getY()
					+ this.jfcLicense.getInsets().top);
		} catch (Throwable t) {
			t.printStackTrace();
			MessageKBCT
					.Error(this, LocaleKBCT.GetString("Error"),
							"Error in JKBCTFrame in jMenuLicense_actionPerformed: "
									+ t);
		}
	}
	// ------------------------------------------------------------------------------
	private static void TranslateTranslatables() throws Throwable {
		if (JKBCTFrame.Translatables != null) {
			for (int i = 0; i < JKBCTFrame.Translatables.size(); i++) {
				Translatable tt = (Translatable) JKBCTFrame.Translatables
						.elementAt(i);
				if (tt != null)
					tt.Translate();
			}
		}
	}
	// ------------------------------------------------------------------------------
	public static File BuildFile(String f_name) {
		File f_new = new File(MainKBCT.getConfig().GetKbctPath()
				+ System.getProperty("file.separator") + "tmp");
		f_new.mkdirs();
		File result = new File(f_new, f_name);
		return result;
	}
	// ------------------------------------------------------------------------------
	protected void TranslateRules_FIS2KBCT(JFIS fis, JKBCT kbct) throws Throwable {
		int NbRules = fis.NbRules();
		for (int n = 0; n < NbRules; n++) {
			JRule rule = fis.GetRule(n);
			int[] premises = rule.Facteurs();
			double[] concs = rule.Actions();
			int rule_number = kbct.GetNbRules();
			int N_inputs = premises.length;
			int N_outputs = concs.length;
			int[] out_labels = new int[N_outputs];
			for (int k = 0; k < N_outputs; k++)
				out_labels[k] = (int) concs[k];

			if (MainKBCT.getConfig().GetClusteringSelection()) {
				kbct.AddRule(new Rule(rule_number, N_inputs, N_outputs,
						premises, out_labels, "P", true));
			} else
				kbct.AddRule(new Rule(rule_number, N_inputs, N_outputs,
						premises, out_labels, "I", true));
		}
	}
	// ------------------------------------------------------------------------------
	public static void AddTranslatable(Translatable t) {
		JKBCTFrame.Translatables.add(t);
	}
	// ------------------------------------------------------------------------------
	public static void RemoveTranslatable(Translatable t) {
		JKBCTFrame.Translatables.remove(t);
	}
	// ------------------------------------------------------------------------------
	public JKBCT getKBCTTemp() {
		return this.Temp_kbct;
	}
	// ------------------------------------------------------------------------------
	/**
	 * set SMOTE parameter
	 */
	public void changeSMOTEParameters() {
		final JDialog jd = new JDialog(this);
		jd.setIconImage(LocaleKBCT.getIconGUAJE().getImage());
		jd.setTitle(LocaleKBCT.GetString("SMOTE"));
		jd.getContentPane().setLayout(new GridBagLayout());
		JPanel jPanelSaisie = new JPanel(new GridBagLayout());
		JPanel jPanelValidation = new JPanel(new GridBagLayout());
		JButton jButtonApply = new JButton(LocaleKBCT.GetString("Apply"));
		JButton jButtonCancel = new JButton(LocaleKBCT.GetString("Cancel"));
		JButton jButtonDefault = new JButton(LocaleKBCT.GetString("DefaultValues"));
		jd.getContentPane().add(jPanelSaisie, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(5, 5, 5, 5), 0, 0));

		JLabel jLabelNumberOfNeighbors = new JLabel(LocaleKBCT.GetString("SMOTEnumberOfNeighbors")+ " :");
		jPanelSaisie.add(jLabelNumberOfNeighbors, new GridBagConstraints(0, 0,
				1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.jNumberOfNeighbors.setValue(MainKBCT.getConfig().GetSMOTEnumberOfNeighbors());
		jPanelSaisie.add(this.jNumberOfNeighbors, new GridBagConstraints(1, 0,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));

		JLabel jLabelType = new JLabel(LocaleKBCT.GetString("SMOTEtype") + " :");
		jPanelSaisie.add(jLabelType, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		if (this.jDCBMtypeModel.getSize() == 0) {
			for (int i = 0; i < 3; i++)
				this.jDCBMtypeModel.addElement(new String());
		}
		jPanelSaisie.add(jCBtype, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));

        String auxt= MainKBCT.getConfig().GetSMOTEtype(); 
		if (auxt.equals("minority"))
			this.jCBtype.setSelectedIndex(0);
		else if (auxt.equals("ASMO"))
			this.jCBtype.setSelectedIndex(1);
		else
			this.jCBtype.setSelectedIndex(2);

		JLabel jLabelBalancing = new JLabel(LocaleKBCT.GetString("SMOTEbalancing")	+ " :");
		jPanelSaisie.add(jLabelBalancing, new GridBagConstraints(0, 2, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		if (this.jDCBMsmoteBalancingModel.getSize() == 0) {
			for (int i = 0; i < 2; i++)
				this.jDCBMsmoteBalancingModel.addElement(new String());
		}
		jPanelSaisie.add(jCBsmoteBalancing, new GridBagConstraints(1, 2, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 40, 0));

		if (MainKBCT.getConfig().GetSMOTEbalancing())
			this.jCBsmoteBalancing.setSelectedIndex(1);
		else
			this.jCBsmoteBalancing.setSelectedIndex(0);

		jCBbalancingALL.setText(LocaleKBCT.GetString("All"));
		jPanelSaisie.add(jCBbalancingALL, new GridBagConstraints(1, 3, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		this.jCBbalancingALL.setSelected(MainKBCT.getConfig()
				.GetSMOTEbalancingALL());

		JLabel jLabelQuantity = new JLabel(LocaleKBCT.GetString("SMOTEquantity")+ " :");
		jPanelSaisie.add(jLabelQuantity, new GridBagConstraints(0, 4, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		this.jQuantity.setValue(MainKBCT.getConfig().GetSMOTEquantity());
		jPanelSaisie.add(this.jQuantity, new GridBagConstraints(1, 4, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 40, 0));

		JLabel jLabelDistance = new JLabel(LocaleKBCT.GetString("SMOTEdistance")+ " :");
		jPanelSaisie.add(jLabelDistance, new GridBagConstraints(0, 5, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		if (this.jDCBMsmoteDistanceModel.getSize() == 0) {
			for (int i = 0; i < 2; i++)
				this.jDCBMsmoteDistanceModel.addElement(new String());
		}
		jPanelSaisie.add(jCBsmoteDistance, new GridBagConstraints(1, 5, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 40, 0));

        String auxd= MainKBCT.getConfig().GetSMOTEdistance(); 
		if (auxd.equals("Euclidean"))
			this.jCBsmoteDistance.setSelectedIndex(0);
		else if (auxd.equals("HVDM"))
			this.jCBsmoteDistance.setSelectedIndex(1);

		JLabel jLabelInterpolation = new JLabel(LocaleKBCT.GetString("SMOTEinterpolation")	+ " :");
		jPanelSaisie.add(jLabelInterpolation, new GridBagConstraints(0, 6, 1,
				1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		if (this.jDCBMinterpolationModel.getSize() == 0) {
			for (int i = 0; i < 3; i++)
				this.jDCBMinterpolationModel.addElement(new String());
		}
		jPanelSaisie.add(jCBinterpolation, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 40, 0));

		String auxi= MainKBCT.getConfig().GetSMOTEinterpolation(); 
		//System.out.println("JKBCTFrame: changeSMOTEParameters() => "+auxi);
		if (auxi.equals("standard"))
			this.jCBinterpolation.setSelectedIndex(0);
		else if (auxi.equals("BLX-alpha"))
			this.jCBinterpolation.setSelectedIndex(1);
		else
			this.jCBinterpolation.setSelectedIndex(2);

		JLabel jLabelAlpha = new JLabel(LocaleKBCT.GetString("SMOTEalpha")
				+ " :");
		jPanelSaisie.add(jLabelAlpha, new GridBagConstraints(0, 7, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		this.jAlpha.setValue(MainKBCT.getConfig().GetSMOTEalpha());
		jPanelSaisie.add(this.jAlpha, new GridBagConstraints(1, 7, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 40, 0));

		JLabel jLabelMu = new JLabel(LocaleKBCT.GetString("SMOTEmu") + " :");
		jPanelSaisie.add(jLabelMu, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 0, 0));

		this.jMu.setValue(MainKBCT.getConfig().GetSMOTEmu());
		jPanelSaisie.add(this.jMu, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));

		jd.getContentPane().add(jPanelValidation, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5),
						0, 0));
		jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jPanelValidation.add(jButtonCancel, new GridBagConstraints(1, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jPanelValidation.add(jButtonDefault, new GridBagConstraints(2, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jButtonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainKBCT.getConfig().SetSMOTEnumberOfNeighbors(JKBCTFrame.this.jNumberOfNeighbors.getValue());
				int optType = JKBCTFrame.this.jCBtype.getSelectedIndex();
				switch (optType) {
				  case 0:
					MainKBCT.getConfig().SetSMOTEtype("minority");
					break;
				  case 1:
					MainKBCT.getConfig().SetSMOTEtype("ASMO");
					break;
				  case 2:
					MainKBCT.getConfig().SetSMOTEtype("both");
					break;
				  default:
					MainKBCT.getConfig().SetSMOTEtype("minority");
					break;
				}
				int optBalancing = JKBCTFrame.this.jCBsmoteBalancing.getSelectedIndex();
				switch (optBalancing) {
				  case 0:
					MainKBCT.getConfig().SetSMOTEbalancing(false);
					break;
				  case 1:
					MainKBCT.getConfig().SetSMOTEbalancing(true);
					break;
				  default:
					MainKBCT.getConfig().SetSMOTEbalancing(false);
					break;
				}
				MainKBCT.getConfig().SetSMOTEbalancingALL(JKBCTFrame.this.jCBbalancingALL.isSelected());
				MainKBCT.getConfig().SetSMOTEquantity(JKBCTFrame.this.jQuantity.getValue());
				int optDistance = JKBCTFrame.this.jCBsmoteDistance.getSelectedIndex();
				switch (optDistance) {
				  case 0:
					MainKBCT.getConfig().SetSMOTEdistance("Euclidean");
					break;
				  case 1:
					MainKBCT.getConfig().SetSMOTEdistance("HVDM");
					break;
				  default:
					MainKBCT.getConfig().SetSMOTEdistance("Euclidean");
					break;
				}
				int optInterpolation = JKBCTFrame.this.jCBinterpolation.getSelectedIndex();
				//System.out.println("optInterpolation="+optInterpolation);
				switch (optInterpolation) {
				  case 0:
					MainKBCT.getConfig().SetSMOTEinterpolation("standard");
					break;
				  case 1:
					MainKBCT.getConfig().SetSMOTEinterpolation("BLX-alpha");
					break;
				  case 2:
					MainKBCT.getConfig().SetSMOTEinterpolation("SBX");
					break;
				  default:
					MainKBCT.getConfig().SetSMOTEinterpolation("standard");
					break;
				}
				MainKBCT.getConfig().SetSMOTEalpha(JKBCTFrame.this.jAlpha.getValue());
				MainKBCT.getConfig().SetSMOTEmu(JKBCTFrame.this.jMu.getValue());
				jd.dispose();
			}
		});
		jButtonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jd.dispose();
			}
		});
		jButtonDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JKBCTFrame.this.jNumberOfNeighbors.setValue(LocaleKBCT.DefaultSMOTEnumberOfNeighbors());
				JKBCTFrame.this.jCBtype.setSelectedIndex(0);
				JKBCTFrame.this.jCBsmoteBalancing.setSelectedIndex(0);
				JKBCTFrame.this.jCBbalancingALL.setSelected(LocaleKBCT.DefaultSMOTEbalancingALL());
				JKBCTFrame.this.jQuantity.setValue(LocaleKBCT.DefaultSMOTEquantity());
				JKBCTFrame.this.jCBsmoteDistance.setSelectedIndex(0);
				JKBCTFrame.this.jCBinterpolation.setSelectedIndex(0);
				JKBCTFrame.this.jAlpha.setValue(LocaleKBCT.DefaultSMOTEalpha());
				JKBCTFrame.this.jMu.setValue(LocaleKBCT.DefaultSMOTEmu());
			}
		});
        jd.setResizable(false);
		jd.setModal(true);
		jd.pack();
		jd.setLocation(JChildFrame.ChildPosition(this, jd.getSize()));
		jd.setVisible(true);
	}
	// ------------------------------------------------------------------------------
	/**
	 * set GA RuleSelection parameters
	 */
	public void setOptimizationGARSParameters() {
		final JDialog jd = new JDialog(this);
		jd.setIconImage(LocaleKBCT.getIconGUAJE().getImage());
		jd.setTitle(LocaleKBCT.GetString("Optimization") + ": "
				+ LocaleKBCT.GetString("RuleSelection"));
		jd.getContentPane().setLayout(new GridBagLayout());
		JPanel jPanelSaisie = new JPanel(new GridBagLayout());
		JPanel jPanelValidation = new JPanel(new GridBagLayout());
		JButton jButtonApply = new JButton(LocaleKBCT.GetString("Apply"));
		JButton jButtonCancel = new JButton(LocaleKBCT.GetString("Cancel"));
		JButton jButtonDefault = new JButton(LocaleKBCT.GetString("DefaultValues"));
		JLabel jLabelNbGenerations = new JLabel(LocaleKBCT.GetString("NbGenerations") + " :");
		JLabel jLabelPopLength = new JLabel(LocaleKBCT.GetString("PopLength") + " :");
		JLabel jLabelTournamentSize = new JLabel(LocaleKBCT.GetString("TournamentSize") + " :");
		JLabel jLabelMutProb = new JLabel(LocaleKBCT.GetString("MutProb") + " :");
		JLabel jLabelCrossoverProb = new JLabel(LocaleKBCT.GetString("CrossoverProb") + " :");
		JLabel jLabelW1ACC = new JLabel(LocaleKBCT.GetString("W1ACC") + " :");
		JLabel jLabelW2INT = new JLabel(LocaleKBCT.GetString("W2INT") + " :");
		JLabel jLabelRSInterpretabilityIndex = new JLabel(LocaleKBCT.GetString("InterpretabilityIndex") + " :");
		JLabel jLabelInitialGeneration = new JLabel(LocaleKBCT.GetString("InitialGeneration") + " :");
		JLabel jLabelMilestoneGeneration = new JLabel(LocaleKBCT.GetString("MilestoneGeneration") + " :");
		this.jNbGenerations.setValue(MainKBCT.getConfig().GetRuleSelectionNbGenerations());
		this.jNbInitialGeneration.setValue(MainKBCT.getConfig().GetRuleSelectionInitialGeneration());
		this.jNbMilestoneGeneration.setValue(MainKBCT.getConfig().GetRuleSelectionMilestoneGeneration());
		this.jPopLength.setValue(MainKBCT.getConfig().GetRuleSelectionPopulationLength());
		this.jTournamentSize.setValue(MainKBCT.getConfig().GetRuleSelectionTournamentSize());
		this.jMutProb.setValue(MainKBCT.getConfig().GetRuleSelectionMutationProb());
		this.jCrossoverProb.setValue(MainKBCT.getConfig().GetRuleSelectionCrossoverProb());
		this.jW1ACC.setValue(MainKBCT.getConfig().GetRuleSelectionW1());
		this.jW2INT.setValue(MainKBCT.getConfig().GetRuleSelectionW2());
		if (this.jDCBMintIndexModel.getSize() == 0) {
			for (int i = 0; i < 8; i++)
				this.jDCBMintIndexModel.addElement(new String());
		}
		this.jCBintIndex.setSelectedIndex(MainKBCT.getConfig().GetRuleSelectionInterpretabilityIndex());
		jd.getContentPane().add(jPanelSaisie, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(5, 5, 5, 5), 0, 0));
		jPanelSaisie.add(jLabelNbGenerations, new GridBagConstraints(0, 0, 1,
				1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jNbGenerations, new GridBagConstraints(1, 0, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 0), 40, 0));
		jPanelSaisie.add(jLabelPopLength, new GridBagConstraints(0, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jPopLength, new GridBagConstraints(1, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 0), 40, 0));
		jPanelSaisie.add(jLabelTournamentSize, new GridBagConstraints(0, 2, 1,
				1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jTournamentSize, new GridBagConstraints(1, 2, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 0), 40, 0));
		jPanelSaisie.add(jLabelMutProb, new GridBagConstraints(0, 3, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jMutProb, new GridBagConstraints(1, 3, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 0), 40, 0));
		jPanelSaisie.add(jLabelCrossoverProb, new GridBagConstraints(0, 4, 1,
				1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jCrossoverProb, new GridBagConstraints(1, 4, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 0), 40, 0));
		jPanelSaisie.add(jLabelW1ACC, new GridBagConstraints(0, 5, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jW1ACC, new GridBagConstraints(1, 5, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 0), 40, 0));
		jPanelSaisie.add(jLabelW2INT, new GridBagConstraints(0, 6, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jW2INT, new GridBagConstraints(1, 6, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 0), 40, 0));
		jPanelSaisie.add(jLabelRSInterpretabilityIndex, new GridBagConstraints(0, 7,
				1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jCBintIndex, new GridBagConstraints(1,
				7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
		jPanelSaisie.add(jLabelInitialGeneration, new GridBagConstraints(0, 8,
				1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jNbInitialGeneration, new GridBagConstraints(1,
				8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
		jPanelSaisie.add(jLabelMilestoneGeneration, new GridBagConstraints(0, 9,
				1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jNbMilestoneGeneration, new GridBagConstraints(1,
				9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
		jd.getContentPane().add(jPanelValidation, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5),
						0, 0));
		jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jPanelValidation.add(jButtonCancel, new GridBagConstraints(1, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jPanelValidation.add(jButtonDefault, new GridBagConstraints(2, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jButtonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int NbGenerations = JKBCTFrame.this.jNbGenerations.getValue();
				int PopLength = JKBCTFrame.this.jPopLength.getValue();
				int TournamentSize = JKBCTFrame.this.jTournamentSize.getValue();
				double MutProb = JKBCTFrame.this.jMutProb.getValue();
				double CrossoverProb = JKBCTFrame.this.jCrossoverProb.getValue();
				double W1ACC = JKBCTFrame.this.jW1ACC.getValue();
				double W2INT = JKBCTFrame.this.jW2INT.getValue();
				int NbInitialGeneration = JKBCTFrame.this.jNbInitialGeneration.getValue();
				int NbMilestoneGeneration = JKBCTFrame.this.jNbMilestoneGeneration.getValue();
				int intIndex= JKBCTFrame.this.jCBintIndex.getSelectedIndex();
				boolean warning = false;
				if (NbGenerations < 1) {
					MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("NbGenerationsShouldBe"));
					warning = true;
				}
				if (PopLength < 2) {
					MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("PopLengthShouldBe"));
					warning = true;
				}
				if ((TournamentSize < 2) || (TournamentSize > PopLength)) {
					MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("TournamentSizeShouldBe"));
					warning = true;
				}
				if ((MutProb < 0) || (MutProb > 1)) {
					MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("MutProbShouldBe"));
					warning = true;
				}
				if ((CrossoverProb < 0) || (CrossoverProb > 1)) {
					MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("CrossoverProbShouldBe"));
					warning = true;
				}
				if (NbInitialGeneration >= NbGenerations) {
					MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("NbInitialGenerationShouldBe"));
					warning = true;
				}
				if (NbMilestoneGeneration >= NbGenerations) {
					MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("NbMilestoneGenerationShouldBe"));
					warning = true;
				}
				if (!warning) {
					MainKBCT.getConfig().SetRuleSelectionNbGenerations(NbGenerations);
					MainKBCT.getConfig().SetRuleSelectionPopulationLength(PopLength);
					MainKBCT.getConfig().SetRuleSelectionTournamentSize(TournamentSize);
					MainKBCT.getConfig().SetRuleSelectionMutationProb(MutProb);
					MainKBCT.getConfig().SetRuleSelectionCrossoverProb(CrossoverProb);
					double W1 = W1ACC / (W1ACC + W2INT);
					MainKBCT.getConfig().SetRuleSelectionW1(W1);
					double W2 = W2INT / (W1ACC + W2INT);
					MainKBCT.getConfig().SetRuleSelectionW2(W2);
					MainKBCT.getConfig().SetRuleSelectionInterpretabilityIndex(intIndex);
					MainKBCT.getConfig().SetRuleSelectionInitialGeneration(NbInitialGeneration);
					MainKBCT.getConfig().SetRuleSelectionMilestoneGeneration(NbMilestoneGeneration);
					jd.dispose();
				}
			}
		});
		jButtonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jd.dispose();
			}
		});
		jButtonDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JKBCTFrame.this.jNbGenerations.setValue(LocaleKBCT.DefaultRuleSelectionNbGenerations());
				JKBCTFrame.this.jPopLength.setValue(LocaleKBCT.DefaultRuleSelectionPopulationLength());
				JKBCTFrame.this.jTournamentSize.setValue(LocaleKBCT.DefaultRuleSelectionTournamentSize());
				JKBCTFrame.this.jMutProb.setValue(LocaleKBCT.DefaultRuleSelectionMutationProb());
				JKBCTFrame.this.jCrossoverProb.setValue(LocaleKBCT.DefaultRuleSelectionCrossoverProb());
				JKBCTFrame.this.jW1ACC.setValue(LocaleKBCT.DefaultRuleSelectionW1());
				JKBCTFrame.this.jW2INT.setValue(LocaleKBCT.DefaultRuleSelectionW2());
				JKBCTFrame.this.jCBintIndex.setSelectedIndex(LocaleKBCT.DefaultRuleSelectionInterpretabilityIndex());
				JKBCTFrame.this.jNbInitialGeneration.setValue(LocaleKBCT.DefaultRuleSelectionInitialGeneration());
				JKBCTFrame.this.jNbMilestoneGeneration.setValue(LocaleKBCT.DefaultRuleSelectionMilestoneGeneration());
			}
		});
        jd.setResizable(false);
		jd.setModal(true);
		jd.pack();
		jd.setLocation(JChildFrame.ChildPosition(this, jd.getSize()));
		jd.setVisible(true);
	}
	// ------------------------------------------------------------------------------
	/**
	 * set GeneticTuning parameters
	 */
	public void setOptimizationGTParameters() {
		final JDialog jd = new JDialog(this);
		jd.setIconImage(LocaleKBCT.getIconGUAJE().getImage());
		jd.setTitle(LocaleKBCT.GetString("Optimization") + ": " + LocaleKBCT.GetString("GeneticTuning"));
		jd.getContentPane().setLayout(new GridBagLayout());
		JPanel jPanelSaisie = new JPanel(new GridBagLayout());
		JPanel jPanelValidation = new JPanel(new GridBagLayout());
		JButton jButtonApply = new JButton(LocaleKBCT.GetString("Apply"));
		JButton jButtonCancel = new JButton(LocaleKBCT.GetString("Cancel"));
		JButton jButtonDefault = new JButton(LocaleKBCT.GetString("DefaultValues"));
		JLabel jLabelNbGenerations = new JLabel(LocaleKBCT.GetString("NbGenerations") + " :");
		JLabel jLabelPopLength = new JLabel(LocaleKBCT.GetString("PopLength") + " :");
		JLabel jLabelTournamentSize = new JLabel(LocaleKBCT.GetString("TournamentSize") + " :");
		JLabel jLabelMutProb = new JLabel(LocaleKBCT.GetString("MutProb") + " :");
		JLabel jLabelCrossoverProb = new JLabel(LocaleKBCT.GetString("CrossoverProb") + " :");
		JLabel jLabelParAlfa = new JLabel(LocaleKBCT.GetString("ParAlfa") + " :");
		JLabel jLabelBoundedOptimization = new JLabel(LocaleKBCT.GetString("BoundedOptimization") + " :");
		JLabel jLabelInitialGeneration = new JLabel(LocaleKBCT.GetString("InitialGeneration") + " :");
		JLabel jLabelMilestoneGeneration = new JLabel(LocaleKBCT.GetString("MilestoneGeneration") + " :");
		this.jNbGenerations.setValue(MainKBCT.getConfig().GetNbGenerations());
		this.jNbInitialGeneration.setValue(MainKBCT.getConfig().GetInitialGeneration());
		this.jNbMilestoneGeneration.setValue(MainKBCT.getConfig().GetMilestoneGeneration());
		this.jPopLength.setValue(MainKBCT.getConfig().GetPopulationLength());
		this.jTournamentSize.setValue(MainKBCT.getConfig().GetTournamentSize());
		this.jMutProb.setValue(MainKBCT.getConfig().GetMutationProb());
		this.jCrossoverProb.setValue(MainKBCT.getConfig().GetCrossoverProb());
		this.jParAlfa.setValue(MainKBCT.getConfig().GetParAlfa());
		jd.getContentPane().add(jPanelSaisie, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(5, 5, 5, 5), 0, 0));
		jPanelSaisie.add(jLabelNbGenerations, new GridBagConstraints(0, 0, 1,
				1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jNbGenerations, new GridBagConstraints(1, 0, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 0), 40, 0));
		jPanelSaisie.add(jLabelPopLength, new GridBagConstraints(0, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jPopLength, new GridBagConstraints(1, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 0), 40, 0));
		jPanelSaisie.add(jLabelTournamentSize, new GridBagConstraints(0, 2, 1,
				1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jTournamentSize, new GridBagConstraints(1, 2, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 0), 40, 0));
		jPanelSaisie.add(jLabelMutProb, new GridBagConstraints(0, 3, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jMutProb, new GridBagConstraints(1, 3, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 0), 40, 0));
		jPanelSaisie.add(jLabelCrossoverProb, new GridBagConstraints(0, 4, 1,
				1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jCrossoverProb, new GridBagConstraints(1, 4, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 0), 40, 0));
		jPanelSaisie.add(jLabelParAlfa, new GridBagConstraints(0, 5, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jParAlfa, new GridBagConstraints(1, 5, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 0), 40, 0));
		jPanelSaisie.add(jLabelBoundedOptimization, new GridBagConstraints(0,
				6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		if (this.jDCBMBoundedOptimizationModel.getSize() == 0) {
			for (int i = 0; i < 2; i++)
				this.jDCBMBoundedOptimizationModel.addElement(new String());
		}
		jPanelSaisie.add(this.jCBBoundedOptimization, new GridBagConstraints(1,
				6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
		if (MainKBCT.getConfig().GetBoundedOptimization())
			this.jCBBoundedOptimization.setSelectedIndex(0);
		else
			this.jCBBoundedOptimization.setSelectedIndex(1);

		jPanelSaisie.add(jLabelInitialGeneration, new GridBagConstraints(0, 7,
				1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jNbInitialGeneration, new GridBagConstraints(1,
				7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
		jPanelSaisie.add(jLabelMilestoneGeneration, new GridBagConstraints(0,
				8, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jNbMilestoneGeneration, new GridBagConstraints(1,
				8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
		jd.getContentPane().add(jPanelValidation, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5),
						0, 0));
		jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jPanelValidation.add(jButtonCancel, new GridBagConstraints(1, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jPanelValidation.add(jButtonDefault, new GridBagConstraints(2, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jButtonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int NbGenerations = JKBCTFrame.this.jNbGenerations.getValue();
				int PopLength = JKBCTFrame.this.jPopLength.getValue();
				int TournamentSize = JKBCTFrame.this.jTournamentSize.getValue();
				double MutProb = JKBCTFrame.this.jMutProb.getValue();
				double CrossoverProb = JKBCTFrame.this.jCrossoverProb.getValue();
				double ParAlfa = JKBCTFrame.this.jParAlfa.getValue();
				int NbInitialGeneration = JKBCTFrame.this.jNbInitialGeneration.getValue();
				int NbMilestoneGeneration = JKBCTFrame.this.jNbMilestoneGeneration.getValue();
				boolean warning = false;
				if (NbGenerations < 1) {
					MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("NbGenerationsShouldBe"));
					warning = true;
				}
				if (PopLength < 2) {
					MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("PopLengthShouldBe"));
					warning = true;
				}
				if ((TournamentSize < 2) || (TournamentSize > PopLength)) {
					MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("TournamentSizeShouldBe"));
					warning = true;
				}
				if ((MutProb < 0) || (MutProb > 1)) {
					MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("MutProbShouldBe"));
					warning = true;
				}
				if ((CrossoverProb < 0) || (CrossoverProb > 1)) {
					MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("CrossoverProbShouldBe"));
					warning = true;
				}
				if ((ParAlfa < 0) || (ParAlfa > 1)) {
					MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("ParAlfaShouldBe"));
					warning = true;
				}
				if (NbInitialGeneration >= NbGenerations) {
					MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("NbInitialGenerationShouldBe"));
					warning = true;
				}
				if (NbMilestoneGeneration >= NbGenerations) {
					MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("NbMilestoneGenerationShouldBe"));
					warning = true;
				}
				if (!warning) {
					MainKBCT.getConfig().SetNbGenerations(NbGenerations);
					MainKBCT.getConfig().SetPopulationLength(PopLength);
					MainKBCT.getConfig().SetTournamentSize(TournamentSize);
					MainKBCT.getConfig().SetMutationProb(MutProb);
					MainKBCT.getConfig().SetCrossoverProb(CrossoverProb);
					MainKBCT.getConfig().SetParAlfa(ParAlfa);
					MainKBCT.getConfig().SetInitialGeneration(NbInitialGeneration);
					MainKBCT.getConfig().SetMilestoneGeneration(NbMilestoneGeneration);
					if (JKBCTFrame.this.jCBBoundedOptimization.getSelectedIndex() == 1)
						MainKBCT.getConfig().SetBoundedOptimization(false);
					else
						MainKBCT.getConfig().SetBoundedOptimization(true);
					jd.dispose();
				}
			}
		});
		jButtonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jd.dispose();
			}
		});
		jButtonDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JKBCTFrame.this.jNbGenerations.setValue(LocaleKBCT.DefaultNbGenerations());
				JKBCTFrame.this.jPopLength.setValue(LocaleKBCT.DefaultPopulationLength());
				JKBCTFrame.this.jTournamentSize.setValue(LocaleKBCT.DefaultTournamentSize());
				JKBCTFrame.this.jMutProb.setValue(LocaleKBCT.DefaultMutationProb());
				JKBCTFrame.this.jCrossoverProb.setValue(LocaleKBCT.DefaultCrossoverProb());
				JKBCTFrame.this.jParAlfa.setValue(LocaleKBCT.DefaultParAlfa());
				JKBCTFrame.this.jNbInitialGeneration.setValue(LocaleKBCT.DefaultInitialGeneration());
				JKBCTFrame.this.jNbMilestoneGeneration.setValue(LocaleKBCT.DefaultMilestoneGeneration());
				if (LocaleKBCT.DefaultBoundedOptimization())
					JKBCTFrame.this.jCBBoundedOptimization.setSelectedIndex(0);
				else
					JKBCTFrame.this.jCBBoundedOptimization.setSelectedIndex(1);
			}
		});
        jd.setResizable(false);
		jd.setModal(true);
		jd.pack();
		jd.setLocation(JChildFrame.ChildPosition(this, jd.getSize()));
		jd.setVisible(true);
	}
	// ------------------------------------------------------------------------------
	/**
	 * set NbIterations parameter
	 */
	public void setOptimizationSWParameters() {
		final JDialog jd = new JDialog(this);
		jd.setIconImage(LocaleKBCT.getIconGUAJE().getImage());
		jd.setTitle(LocaleKBCT.GetString("Optimization") + ": " + LocaleKBCT.GetString("SolisWetts"));
		jd.getContentPane().setLayout(new GridBagLayout());
		JPanel jPanelSaisie = new JPanel(new GridBagLayout());
		JPanel jPanelValidation = new JPanel(new GridBagLayout());
		JButton jButtonApply = new JButton(LocaleKBCT.GetString("Apply"));
		JButton jButtonCancel = new JButton(LocaleKBCT.GetString("Cancel"));
		JButton jButtonDefault = new JButton(LocaleKBCT.GetString("DefaultValues"));
		JLabel jLabelSWoption = new JLabel(LocaleKBCT.GetString("SWoption") + " :");
		JLabel jLabelNbIterations = new JLabel(LocaleKBCT.GetString("NbIterations") + " :");
		JLabel jLabelBoundedOptimization = new JLabel(LocaleKBCT.GetString("Cnear") + " :");
		this.jNbIterations.setValue(MainKBCT.getConfig().GetNbIterations());
		jd.getContentPane().add(jPanelSaisie, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(5, 5, 5, 5), 0, 0));
		jPanelSaisie.add(jLabelSWoption, new GridBagConstraints(0, 0, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		if (this.jDCBMoptionModel.getSize() == 0) {
			for (int i = 0; i < 2; i++)
				this.jDCBMoptionModel.addElement(new String());
		}
		jPanelSaisie.add(this.jCBopt, new GridBagConstraints(1, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 0), 40, 0));
		this.jCBopt.setSelectedIndex(MainKBCT.getConfig().GetSWoption() - 1);
		jPanelSaisie.add(jLabelNbIterations, new GridBagConstraints(0, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jNbIterations, new GridBagConstraints(1, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 0), 40, 0));
		jPanelSaisie.add(jLabelBoundedOptimization, new GridBagConstraints(0,
				6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		if (this.jDCBMBoundedOptimizationModel.getSize() == 0) {
			for (int i = 0; i < 2; i++)
				this.jDCBMBoundedOptimizationModel.addElement(new String());
		}
		jPanelSaisie.add(this.jCBBoundedOptimization, new GridBagConstraints(1,
				6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
		if (MainKBCT.getConfig().GetBoundedOptimization())
			this.jCBBoundedOptimization.setSelectedIndex(0);
		else
			this.jCBBoundedOptimization.setSelectedIndex(1);

		jd.getContentPane().add(jPanelValidation, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5),
						0, 0));
		jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jPanelValidation.add(jButtonCancel, new GridBagConstraints(1, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jPanelValidation.add(jButtonDefault, new GridBagConstraints(2, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jButtonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opt = JKBCTFrame.this.jCBopt.getSelectedIndex();
				int value = JKBCTFrame.this.jNbIterations.getValue();
				if (value < 1) {
					MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("NbIterationsShouldBe"));
				} else {
					MainKBCT.getConfig().SetSWoption(opt + 1);
					MainKBCT.getConfig().SetNbIterations(value);
					if (JKBCTFrame.this.jCBBoundedOptimization
							.getSelectedIndex() == 1)
						MainKBCT.getConfig().SetBoundedOptimization(false);
					else
						MainKBCT.getConfig().SetBoundedOptimization(true);
					jd.dispose();
				}
			}
		});
		jButtonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jd.dispose();
			}
		});
		jButtonDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JKBCTFrame.this.jCBopt.setSelectedIndex(LocaleKBCT.DefaultSWoption() - 1);
				JKBCTFrame.this.jNbIterations.setValue(LocaleKBCT.DefaultNbIterations());
				if (LocaleKBCT.DefaultBoundedOptimization())
					JKBCTFrame.this.jCBBoundedOptimization.setSelectedIndex(0);
				else
					JKBCTFrame.this.jCBBoundedOptimization.setSelectedIndex(1);
			}
		});
        jd.setResizable(false);
		jd.setModal(true);
		jd.pack();
		jd.setLocation(JChildFrame.ChildPosition(this, jd.getSize()));
		jd.setVisible(true);
	}
	// ------------------------------------------------------------------------------
	/**
	 * set CompletenessThres parameter
	 */
	public void setCompletenessThreshold() {
		final JDialog jd = new JDialog(this);
		jd.setIconImage(LocaleKBCT.getIconGUAJE().getImage());
		jd.setTitle(LocaleKBCT.GetString("Completeness"));
		jd.getContentPane().setLayout(new GridBagLayout());
		JPanel jPanelSaisie = new JPanel(new GridBagLayout());
		JPanel jPanelValidation = new JPanel(new GridBagLayout());
		JButton jButtonApply = new JButton(LocaleKBCT.GetString("Apply"));
		JButton jButtonCancel = new JButton(LocaleKBCT.GetString("Cancel"));
		JButton jButtonDefault = new JButton(LocaleKBCT.GetString("DefaultValues"));
		JLabel jLabelCompletenessThres = new JLabel(LocaleKBCT.GetString("CompletenessThres") + " :");
		this.jCompletenessThres.setValue(MainKBCT.getConfig().GetCompletenessThres());
		jd.getContentPane().add(jPanelSaisie, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(5, 5, 5, 5), 0, 0));
		jPanelSaisie.add(jLabelCompletenessThres, new GridBagConstraints(0, 0,
				1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jPanelSaisie.add(this.jCompletenessThres, new GridBagConstraints(1, 0,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));

		jd.getContentPane().add(jPanelValidation, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5),
						0, 0));
		jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jPanelValidation.add(jButtonCancel, new GridBagConstraints(1, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jPanelValidation.add(jButtonDefault, new GridBagConstraints(2, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jButtonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double value = JKBCTFrame.this.jCompletenessThres.getValue();
				if ((value < 0) || (value > 1)) {
					MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("CompletenessThresShouldBe"));
				} else {
					MainKBCT.getConfig().SetCompletenessThres(value);
					jd.dispose();
				}
			}
		});
		jButtonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jd.dispose();
			}
		});
		jButtonDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JKBCTFrame.this.jCompletenessThres.setValue(LocaleKBCT
						.DefaultCompletenessThres());
			}
		});
		jd.pack();
        jd.setResizable(false);
		jd.setModal(true);
		jd.setLocation(JChildFrame.ChildPosition(this, jd.getSize()));
		jd.setVisible(true);
	}
	// ------------------------------------------------------------------------------
	/**
	 * generate fingrams windows
	 */
	protected void visualizeSVG(String smOpt, boolean ro, double[] accind, boolean[] covdata, double[] intind, double intFing, boolean[] selFing) {
		// System.out.println("constructSVG: "+smOpt);
		this.regOpt = ro;
		if (this.jdSVG != null) {
			this.jdSVG.dispose();
		}
		jdSVG = new JFrame();
		jdSVG.setIconImage(icon_guaje.getImage());
		jdSVG.setTitle(LocaleKBCT.GetString("Fingrams"));
		jdSVG.getContentPane().setLayout(new GridBagLayout());
		//JTabbedPane main_TabbedPane = new JTabbedPane();
		JPanel jPanelSaisieMS = new JPanel(new GridBagLayout());
		//JPanel jPanelSaisieMA = new JPanel(new GridBagLayout());
		//JPanel jPanelSaisieMSFD = new JPanel(new GridBagLayout());
		//JPanel jPanelSaisieMAFD = new JPanel(new GridBagLayout());

	    TitledBorder titledBorderMS = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)), "");
	    jPanelSaisieMS.setBorder(titledBorderMS);

		JTabbedPane secondary_TabbedPane = new JTabbedPane();
		JPanel jPanelResults = new JPanel(new GridBagLayout());
		JPanel jPanelLegend = new JPanel(new GridBagLayout());

		JPanel jPanelValidation = new JPanel(new GridBagLayout());
		JButton jButtonApply = new JButton(LocaleKBCT.GetString("Apply"));
		JButton jButtonCancel = new JButton(LocaleKBCT.GetString("Cancel"));
		JButton jButtonHelp = new JButton(LocaleKBCT.GetString("Help"));
		
		//JKBCTFrame.fingrams = new JFingramsFrame[24];
		//JKBCTFrame.fingrams = new JFingramsFrame[3];
		
		// Superior tabbed pane
		//main_TabbedPane.addTab("MS", null, jPanelSaisieMS, "MS");
		//main_TabbedPane.addTab("MSFD", null, jPanelSaisieMSFD, "MSFD");
		//main_TabbedPane.addTab("MA", null, jPanelSaisieMA, "MA");
		//main_TabbedPane.addTab("MAFD", null, jPanelSaisieMAFD, "MAFD");
		jdSVG.getContentPane().add(
				//main_TabbedPane,
				jPanelSaisieMS,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(5, 5, 5, 5), 0, 0));

		// Inferior tabbed pane
		secondary_TabbedPane.addTab(LocaleKBCT.GetString("Quality"), null, jPanelResults, LocaleKBCT.GetString("Quality"));
		secondary_TabbedPane.addTab(LocaleKBCT.GetString("Legend"), null, jPanelLegend, LocaleKBCT.GetString("Legend"));
		
		jdSVG.getContentPane().add(secondary_TabbedPane,
				new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(5, 5, 5, 5), 0, 0));

		// Values for tab of results
		DoubleField jdfCoverage= new DoubleField();
	    JLabel jLabelCoverage= new JLabel();

	    // Adding info for tab of results
	    jPanelResults.add(jLabelCoverage, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	    //jdfCoverage.setEnabled(false);
        jdfCoverage.setBackground(Color.WHITE);
	    jdfCoverage.setForeground(Color.BLUE);
	    jdfCoverage.setEditable(false);
	    jPanelResults.add(jdfCoverage, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));

	    jdfCoverage.setValue(accind[0]);
	    jLabelCoverage.setText(LocaleKBCT.GetString("Coverage") + " (%) :");
	    int c=0;
        if ( (accind[0] < 100) && (covdata != null) ) {
	        JLabel jLabelItems= new JLabel(LocaleKBCT.GetString("items") + ":");

        	jPanelResults.add(jLabelItems, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	        
            int lim= covdata.length;
            int cont=0;
            for (int n=0; n<lim; n++) {
           	     if (!covdata[n])
            	     cont++;
            }
	        Vector lines= new Vector();
            String l="";
            int k=0;
            for (int n=0; n<lim; n++) {
            	 if (!covdata[n]) {
            		 //System.out.print(String.valueOf(n+1)+", ");
            		 l=l+String.valueOf(n+1);
            		 k++;
            		 if (k < cont)
            			 l= l+", ";
            		 if ( (l.length()>35) || (k==cont) ) {
            			 lines.add(l);
            			 l= "";
            		 }
            	 }
            }
            Object[] obj= lines.toArray();
            int NbLines= obj.length;
            for (int n=0; n<NbLines; n++) {
                JLabel items= new JLabel(obj[n].toString());
                items.setToolTipText(LocaleKBCT.GetString("NCitems"));
            	jPanelResults.add(items, new GridBagConstraints(1, 1+c, 1, 1, 0.0, 0.0
		                ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
                c++;
            }
        }
        if (this.regOpt) {
    	    DoubleField jdfRMSE= new DoubleField();
    	    JLabel jLabelRMSE= new JLabel();
    	    DoubleField jdfMSE= new DoubleField();
    	    JLabel jLabelMSE= new JLabel();
    	    DoubleField jdfMAE= new DoubleField();
    	    JLabel jLabelMAE= new JLabel();

    	    jPanelResults.add(jLabelRMSE, new GridBagConstraints(0, c+1, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	        //jdfRMSE.setEnabled(false);
            jdfRMSE.setBackground(Color.WHITE);
    	    jdfRMSE.setForeground(Color.BLUE);
    	    jdfRMSE.setEditable(false);
	        jPanelResults.add(jdfRMSE, new GridBagConstraints(1, c+1, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));
		
            jdfRMSE.setValue(accind[1]);
	        jLabelRMSE.setText("RMSE :");
	        jPanelResults.add(jLabelMSE, new GridBagConstraints(0, c+2, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	        //jdfMSE.setEnabled(false);
            jdfMSE.setBackground(Color.WHITE);
    	    jdfMSE.setForeground(Color.BLUE);
    	    jdfMSE.setEditable(false);
	        jPanelResults.add(jdfMSE, new GridBagConstraints(1, c+2, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));
		
            jdfMSE.setValue(accind[2]);
	        jLabelMSE.setText("MSE :");
	        //jdfMSE.setValue((Math.pow(resulti[1],2))/2);
	        //jdfMAE.setValue(resulti[2]);
	        jPanelResults.add(jLabelMAE, new GridBagConstraints(0, c+3, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	        //jdfMAE.setEnabled(false);
            jdfMAE.setBackground(Color.WHITE);
    	    jdfMAE.setForeground(Color.BLUE);
    	    jdfMAE.setEditable(false);
	        jPanelResults.add(jdfMAE, new GridBagConstraints(1, c+3, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));
		
            jdfMAE.setValue(accind[3]);
	        jLabelMAE.setText("MAE :");
        } else {
    	    DoubleField jdfAccuracy= new DoubleField();
    	    JLabel jLabelAccuracy= new JLabel();
    	    DoubleField jdfAvConfFD= new DoubleField();
    	    JLabel jLabelAvConfFD= new JLabel();
    	    jPanelResults.add(jLabelAccuracy, new GridBagConstraints(0, c+1, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	        //jdfAccuracy.setEnabled(false);
            jdfAccuracy.setBackground(Color.WHITE);
    	    jdfAccuracy.setForeground(Color.BLUE);
    	    jdfAccuracy.setEditable(false);
	        jPanelResults.add(jdfAccuracy, new GridBagConstraints(1, c+1, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));
		
            jdfAccuracy.setValue(accind[1]);
	        jLabelAccuracy.setText(LocaleKBCT.GetString("Accuracy") + " :");
	        jPanelResults.add(jLabelAvConfFD, new GridBagConstraints(0, c+2, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	        //jdfAvConfFD.setEnabled(false);
	        jdfAvConfFD.setBackground(Color.WHITE);
		    jdfAvConfFD.setForeground(Color.BLUE);
		    jdfAvConfFD.setEditable(false);
	        jPanelResults.add(jdfAvConfFD, new GridBagConstraints(1, c+2, 1, 1, 0.0, 0.0
	                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));
		
	        jdfAvConfFD.setValue(accind[2]);
	        jLabelAvConfFD.setText(LocaleKBCT.GetString("AvgCFD") + " :");
        }
        DoubleField jdfTotalRuleLength= new DoubleField();
	    JLabel jLabelTotalRuleLength= new JLabel();
	    jPanelResults.add(jLabelTotalRuleLength, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 20, 5, 5), 0, 0));

	    //jdfTotalRuleLength.setEnabled(false);
        jdfTotalRuleLength.setBackground(Color.WHITE);
	    jdfTotalRuleLength.setForeground(Color.BLUE);
	    jdfTotalRuleLength.setEditable(false);
	    jPanelResults.add(jdfTotalRuleLength, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));

	    jdfTotalRuleLength.setValue(intind[0]);
	    jLabelTotalRuleLength.setText(LocaleKBCT.GetString("IshibuchiTotalRuleLength") + " :");

        DoubleField jdfSimFiredRulesMax= new DoubleField();
	    JLabel jLabelSimFiredRulesMax= new JLabel();
	    jPanelResults.add(jLabelSimFiredRulesMax, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 20, 5, 5), 0, 0));

	    //jdfSimFiredRulesMax.setEnabled(false);
        jdfSimFiredRulesMax.setBackground(Color.WHITE);
	    jdfSimFiredRulesMax.setForeground(Color.BLUE);
	    jdfSimFiredRulesMax.setEditable(false);
	    jPanelResults.add(jdfSimFiredRulesMax, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));

	    jdfSimFiredRulesMax.setValue(intind[1]);
	    jLabelSimFiredRulesMax.setText(LocaleKBCT.GetString("MaxFiredRulesTraining") + " :");

        DoubleField jdfSimFiredRulesAvg= new DoubleField();
	    JLabel jLabelSimFiredRulesAvg= new JLabel();
	    jPanelResults.add(jLabelSimFiredRulesAvg, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 20, 5, 5), 0, 0));

	    //jdfSimFiredRulesAvg.setEnabled(false);
        jdfSimFiredRulesAvg.setBackground(Color.WHITE);
	    jdfSimFiredRulesAvg.setForeground(Color.BLUE);
	    jdfSimFiredRulesAvg.setEditable(false);
	    jPanelResults.add(jdfSimFiredRulesAvg, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));

	    jdfSimFiredRulesAvg.setValue(intind[2]);
	    jLabelSimFiredRulesAvg.setText(LocaleKBCT.GetString("AverageFiredRulesTraining") + " :");

        DoubleField jdfSimFiredRulesMin= new DoubleField();
	    JLabel jLabelSimFiredRulesMin= new JLabel();
	    jPanelResults.add(jLabelSimFiredRulesMin, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 20, 5, 5), 0, 0));

	    //jdfSimFiredRulesMin.setEnabled(false);
        jdfSimFiredRulesMin.setBackground(Color.WHITE);
	    jdfSimFiredRulesMin.setForeground(Color.BLUE);
	    jdfSimFiredRulesMin.setEditable(false);
	    jPanelResults.add(jdfSimFiredRulesMin, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));

	    jdfSimFiredRulesMin.setValue(intind[3]);
	    jLabelSimFiredRulesMin.setText(LocaleKBCT.GetString("MinFiredRulesTraining") + " :");

        DoubleField jdfAccRuleBaseComplexity= new DoubleField();
	    JLabel jLabelAccRuleBaseComplexity= new JLabel();
	    jPanelResults.add(jLabelAccRuleBaseComplexity, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 20, 5, 5), 0, 0));

	    //jdfAccRuleBaseComplexity.setEnabled(false);
        jdfAccRuleBaseComplexity.setBackground(Color.WHITE);
	    jdfAccRuleBaseComplexity.setForeground(Color.BLUE);
	    jdfAccRuleBaseComplexity.setEditable(false);
	    jPanelResults.add(jdfAccRuleBaseComplexity, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));

	    jdfAccRuleBaseComplexity.setValue(intind[4]);
	    jLabelAccRuleBaseComplexity.setText(LocaleKBCT.GetString("AccumulatedRuleComplexity") + " :");

        DoubleField jdfFingIndex= new DoubleField();
	    JLabel jLabelFingIndex= new JLabel();
	    jPanelResults.add(jLabelFingIndex, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 20, 5, 5), 0, 0));

	    //jdfFingIndex.setEnabled(false);
        jdfFingIndex.setBackground(Color.WHITE);
	    jdfFingIndex.setForeground(Color.BLUE);
	    jdfFingIndex.setEditable(false);
	    jPanelResults.add(jdfFingIndex, new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));

	    jdfFingIndex.setValue(intFing);
	    jLabelFingIndex.setText(LocaleKBCT.GetString("InterpretabilityIndex")+" (Fingrams) :");
	    
	    DoubleField jdfHILKintInd= new DoubleField();
	    JLabel jLabelHILKintInd= new JLabel();
	    jPanelResults.add(jLabelHILKintInd, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 20, 5, 5), 0, 0));

	    //jdfHILKintInd.setEnabled(false);
        jdfHILKintInd.setBackground(Color.WHITE);
	    jdfHILKintInd.setForeground(Color.BLUE);
	    jdfHILKintInd.setEditable(false);
	    jPanelResults.add(jdfHILKintInd, new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));

	    jdfHILKintInd.setValue(intind[5]);
	    jLabelHILKintInd.setText(LocaleKBCT.GetString("InterpretabilityIndex")+" (HILK) :");
	    
        DoubleField jdfLVindex= new DoubleField();
	    JLabel jLabelLVindex= new JLabel();
	    jPanelResults.add(jLabelLVindex, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 20, 5, 5), 0, 0));

	    //jdfLVindex.setEnabled(false);
        jdfLVindex.setBackground(Color.WHITE);
	    jdfLVindex.setForeground(Color.BLUE);
	    jdfLVindex.setEditable(false);
	    jPanelResults.add(jdfLVindex, new GridBagConstraints(3, 7, 1, 1, 0.0, 0.0
	              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 40, 0));

	    jdfLVindex.setValue(intind[6]);
	    jLabelLVindex.setText(LocaleKBCT.GetString("LVindexTraining") + " :");
	    
	    // Info tab legend
		String kbctpath= MainKBCT.getConfig().GetKbctPath();
	    String icons= kbctpath+System.getProperty("file.separator")+"icons";
		File faux= null;
		if (this.regOpt)
			faux = new File(icons+ System.getProperty("file.separator")+ "fingramRegressionLegend.svg");
		else 
			faux = new File(icons+ System.getProperty("file.separator")+ "fingramClassificationLegend.svg");
		
		JSVGCanvas svgCanvas = new JSVGCanvas();
		if (faux!=null)
		    svgCanvas.setURI(faux.toURI().toString());
		
		svgCanvas.setEnableZoomInteractor(false);
		svgCanvas.setEnableImageZoomInteractor(false);
	    svgCanvas.setEnableRotateInteractor(false);
	    svgCanvas.setEnablePanInteractor(false);
	    svgCanvas.setEnableResetTransformInteractor(false);
	    svgCanvas.setFocusable(false);
	    svgCanvas.setEnabled(false);
		
		jPanelLegend.add(svgCanvas, new GridBagConstraints(0, 0, 1,
				1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
	    
		String fingramsMetric = MainKBCT.getConfig().GetFingramsMetric();
		this.jcbm1nsall.setText(LocaleKBCT.GetString("OriginalFingram"));
		this.jcbm1nspfinstance.setText(LocaleKBCT.GetString("InstanceFingram"));
		if (!fingramsMetric.equals(LocaleKBCT.GetString("All"))
				&& !(fingramsMetric.equals("MS") || fingramsMetric.equals("MSFD") || fingramsMetric.equals("MA")) ) {
			//main_TabbedPane.setEnabledAt(0, false);
			this.jcbm1nspfcons.setSelected(false);
			this.jcbm1nspfconsRed.setSelected(false);
		} else {
			if (((smOpt.equals("WOS")) || (smOpt.equals("ALL")))
					&& (this.checkFingramExists("Complete.svg"))) {
				this.jcbm1nsall.setEnabled(true);
			} else {
				this.jcbm1nsall.setEnabled(false);
			}
			//System.out.println("JKBCTFrame -> visualizeSVG -> "+MainKBCT.getConfig().GetFingramsSelectedSample());
			if (((smOpt.equals("WOS")) || (smOpt.equals("ALL")))
					&& (MainKBCT.getConfig().GetFingramsSelectedSample() > 0) 
					&& (this.checkFingramExists("PathfinderInstance.svg"))) {
				//System.out.println("o1");
				this.jcbm1nspfinstance.setEnabled(true);
				this.jcbm1nspfinstance.setSelected(true);
			} else {
				//System.out.println("o2");
				this.jcbm1nspfinstance.setEnabled(false);
				this.jcbm1nspfinstance.setSelected(false);
			}
			this.jcbm1nspfcons.setText("PFNET: " + LocaleKBCT.GetString("OnlyConsistency"));
			if (((smOpt.equals("WOS")) || (smOpt.equals("ALL")))
					&& (this.checkFingramExists("Pathfinder.svg"))) {
				this.jcbm1nspfcons.setEnabled(true);
				if (!this.regOpt)
					this.jcbm1nspfcons.setSelected(true);
				else
					this.jcbm1nspfcons.setSelected(false);

			} else {
				this.jcbm1nspfcons.setEnabled(false);
				this.jcbm1nspfcons.setSelected(false);
			}
			if (this.regOpt)
				this.jcbm1nspfconsRed.setText("PFNET");
			else
				this.jcbm1nspfconsRed.setText("PFNET: " + LocaleKBCT.GetString("ConsistencyAndRedundancy"));

			if (((smOpt.equals("WOS")) || (smOpt.equals("ALL")))
					&& (this.checkFingramExists("Pathfinder.svg"))) {
				this.jcbm1nspfconsRed.setEnabled(true);
				this.jcbm1nspfconsRed.setSelected(true);
			} else {
				this.jcbm1nspfconsRed.setSelected(false);
				this.jcbm1nspfconsRed.setEnabled(false);
			}
			/*this.jcbm1small.setText(LocaleKBCT.GetString("OriginalFingram")	+ " (SMOTE)");
			if ((!this.regOpt)
					&& ((smOpt.equals("WS")) || (smOpt.equals("ALL")))
					&& (this.checkFingramExists(".smote.scienceMap.ms.dot.svg"))) {
				this.jcbm1small.setEnabled(true);
			} else {
				this.jcbm1small.setEnabled(false);
			}
			this.jcbm1smpfcons.setText("PFNET: " + LocaleKBCT.GetString("OnlyConsistency") + " (SMOTE)");
			if ((!this.regOpt)
					&& ((smOpt.equals("WS")) || (smOpt.equals("ALL")))
					&& (this.checkFingramExists(".smote.scienceMap.msc.mpf.dot.svg"))) {
				this.jcbm1smpfcons.setEnabled(true);
			} else {
				this.jcbm1smpfcons.setEnabled(false);
			}
			this.jcbm1smpfconsRed.setText("PFNET: "	+ LocaleKBCT.GetString("ConsistencyAndRedundancy")
					+ " (SMOTE)");
			if ((!this.regOpt)
					&& ((smOpt.equals("WS")) || (smOpt.equals("ALL")))
					&& (this.checkFingramExists(".smote.scienceMap.ms.mpf.dot.svg"))) {
				this.jcbm1smpfconsRed.setEnabled(true);
			} else {
				this.jcbm1smpfconsRed.setEnabled(false);
			}
			this.jcbm1smpfconsRed.setSelected(false);*/
		}
		if (MainKBCT.getConfig().GetTutorialFlag()) {
			this.jcbm1nsall.setSelected(true);
			//this.jcbm1nspfinstance.setSelected(true);
            //if (MainKBCT.getConfig().GetFINGRAMSautomatic())
			  //  this.jcbm1nspfinstance.setEnabled(true);
		} else {
		    this.jcbm1nsall.setSelected(false);
			//this.jcbm1nspfinstance.setSelected(false);
		}
		//this.jcbm1small.setSelected(false);
		//this.jcbm1smpfcons.setSelected(false);
		//this.jcbm1smpfconsRed.setSelected(false);

		/*if (!fingramsMetric.equals(LocaleKBCT.GetString("All"))
				&& !fingramsMetric.equals("MSFD")) {
			main_TabbedPane.setEnabledAt(1, false);

		} else {
			this.jcbm2nsall.setText(LocaleKBCT.GetString("OriginalFingram"));
			if (((smOpt.equals("WOS")) || (smOpt.equals("ALL")))
					&& (this.checkFingramExists(".scienceMap.msfd.dot.svg"))) {
				this.jcbm2nsall.setEnabled(true);
			} else {
				this.jcbm2nsall.setEnabled(false);
			}
			this.jcbm2nspfcons.setText("PFNET: "
					+ LocaleKBCT.GetString("OnlyConsistency"));
			if (((smOpt.equals("WOS")) || (smOpt.equals("ALL")))
					&& (this
							.checkFingramExists(".scienceMap.msfdc.mpf.dot.svg"))) {
				this.jcbm2nspfcons.setEnabled(true);
				// this.jcbm2nspfcons.setSelected(true);
			} else {
				this.jcbm2nspfcons.setEnabled(false);
			}
			if (this.regOpt)
				this.jcbm2nspfconsRed.setText("PFNET");
			else
				this.jcbm2nspfconsRed.setText("PFNET: "
						+ LocaleKBCT.GetString("ConsistencyAndRedundancy"));

			if (((smOpt.equals("WOS")) || (smOpt.equals("ALL")))
					&& (this.checkFingramExists(".scienceMap.msfd.mpf.dot.svg"))) {
				this.jcbm2nspfconsRed.setEnabled(true);
				// this.jcbm2nspfconsRed.setSelected(true);
			} else {
				this.jcbm2nspfconsRed.setEnabled(false);
			}
			this.jcbm2small.setText(LocaleKBCT.GetString("OriginalFingram")
					+ " (SMOTE)");
			if ((!this.regOpt)
					&& ((smOpt.equals("WS")) || (smOpt.equals("ALL")))
					&& (this
							.checkFingramExists(".smote.scienceMap.msfd.dot.svg"))) {
				this.jcbm2small.setEnabled(true);
			} else {
				this.jcbm2small.setEnabled(false);
			}
			this.jcbm2smpfcons.setText("PFNET: "
					+ LocaleKBCT.GetString("OnlyConsistency") + " (SMOTE)");
			if ((!this.regOpt)
					&& ((smOpt.equals("WS")) || (smOpt.equals("ALL")))
					&& (this
							.checkFingramExists(".smote.scienceMap.msfdc.mpf.dot.svg"))) {
				this.jcbm2smpfcons.setEnabled(true);
			} else {
				this.jcbm2smpfcons.setEnabled(false);
			}
			this.jcbm2smpfconsRed.setText("PFNET: "
					+ LocaleKBCT.GetString("ConsistencyAndRedundancy")
					+ " (SMOTE)");
			if ((!this.regOpt)
					&& ((smOpt.equals("WS")) || (smOpt.equals("ALL")))
					&& (this
							.checkFingramExists(".smote.scienceMap.msfd.mpf.dot.svg"))) {
				this.jcbm2smpfconsRed.setEnabled(true);
			} else {
				this.jcbm2smpfconsRed.setEnabled(false);
			}
		}*/
		/*this.jcbm2nsall.setSelected(false);
		this.jcbm2nspfcons.setSelected(false);
		this.jcbm2nspfconsRed.setSelected(false);
		this.jcbm2small.setSelected(false);
		this.jcbm2smpfcons.setSelected(false);
		this.jcbm2smpfconsRed.setSelected(false);*/

		/*if (!fingramsMetric.equals(LocaleKBCT.GetString("All"))
				&& !fingramsMetric.equals("MA")) {
			main_TabbedPane.setEnabledAt(2, false);

		} else {
			this.jcbm3nsall.setText(LocaleKBCT.GetString("OriginalFingram"));
			if (((smOpt.equals("WOS")) || (smOpt.equals("ALL")))
					&& (this.checkFingramExists(".scienceMap.ma.dot.svg"))) {
				this.jcbm3nsall.setEnabled(true);
			} else {
				this.jcbm3nsall.setEnabled(false);
			}
			this.jcbm3nspfcons.setText("PFNET: "
					+ LocaleKBCT.GetString("OnlyConsistency"));
			if (((smOpt.equals("WOS")) || (smOpt.equals("ALL")))
					&& (this.checkFingramExists(".scienceMap.mac.fpf.dot.svg"))) {
				this.jcbm3nspfcons.setEnabled(true);
				// this.jcbm3nspfcons.setSelected(true);
			} else {
				this.jcbm3nspfcons.setEnabled(false);
			}
			if (this.regOpt)
				this.jcbm3nspfconsRed.setText("PFNET");
			else
				this.jcbm3nspfconsRed.setText("PFNET: "
						+ LocaleKBCT.GetString("ConsistencyAndRedundancy"));

			if (((smOpt.equals("WOS")) || (smOpt.equals("ALL")))
					&& (this.checkFingramExists(".scienceMap.ma.fpf.dot.svg"))) {
				this.jcbm3nspfconsRed.setEnabled(true);
				// this.jcbm3nspfconsRed.setSelected(true);
			} else {
				this.jcbm3nspfconsRed.setEnabled(false);
			}
			this.jcbm3small.setText(LocaleKBCT.GetString("OriginalFingram")
					+ " (SMOTE)");
			if ((!this.regOpt)
					&& ((smOpt.equals("WS")) || (smOpt.equals("ALL")))
					&& (this.checkFingramExists(".smote.scienceMap.ma.dot.svg"))) {
				this.jcbm3small.setEnabled(true);
			} else {
				this.jcbm3small.setEnabled(false);
			}
			this.jcbm3smpfcons.setText("PFNET: "
					+ LocaleKBCT.GetString("OnlyConsistency") + " (SMOTE)");
			if ((!this.regOpt)
					&& ((smOpt.equals("WS")) || (smOpt.equals("ALL")))
					&& (this
							.checkFingramExists(".smote.scienceMap.mac.fpf.dot.svg"))) {
				this.jcbm3smpfcons.setEnabled(true);
			} else {
				this.jcbm3smpfcons.setEnabled(false);
			}
			this.jcbm3smpfconsRed.setText("PFNET: "
					+ LocaleKBCT.GetString("ConsistencyAndRedundancy")
					+ " (SMOTE)");
			if ((!this.regOpt)
					&& ((smOpt.equals("WS")) || (smOpt.equals("ALL")))
					&& (this
							.checkFingramExists(".smote.scienceMap.ma.fpf.dot.svg"))) {
				this.jcbm3smpfconsRed.setEnabled(true);
			} else {
				this.jcbm3smpfconsRed.setEnabled(false);
			}
		}*/
		/*this.jcbm3nsall.setSelected(false);
		this.jcbm3nspfcons.setSelected(false);
		this.jcbm3nspfconsRed.setSelected(false);
		this.jcbm3small.setSelected(false);
		this.jcbm3smpfcons.setSelected(false);
		this.jcbm3smpfconsRed.setSelected(false);*/

		/*if (!fingramsMetric.equals(LocaleKBCT.GetString("All"))
				&& !fingramsMetric.equals("MAFD")) {
			main_TabbedPane.setEnabledAt(3, false);

		} else {
			this.jcbm4nsall.setText(LocaleKBCT.GetString("OriginalFingram"));
			if (((smOpt.equals("WOS")) || (smOpt.equals("ALL")))
					&& (this.checkFingramExists(".scienceMap.mafd.dot.svg"))) {
				this.jcbm4nsall.setEnabled(true);
			} else {
				this.jcbm4nsall.setEnabled(false);
			}
			this.jcbm4nspfcons.setText("PFNET: "
					+ LocaleKBCT.GetString("OnlyConsistency"));
			if (((smOpt.equals("WOS")) || (smOpt.equals("ALL")))
					&& (this
							.checkFingramExists(".scienceMap.mafdc.fpf.dot.svg"))) {
				this.jcbm4nspfcons.setEnabled(true);
				// this.jcbm4nspfcons.setSelected(true);
			} else {
				this.jcbm4nspfcons.setEnabled(false);
			}
			if (this.regOpt)
				this.jcbm4nspfconsRed.setText("PFNET");
			else
				this.jcbm4nspfconsRed.setText("PFNET: "
						+ LocaleKBCT.GetString("ConsistencyAndRedundancy"));

			if (((smOpt.equals("WOS")) || (smOpt.equals("ALL")))
					&& (this.checkFingramExists(".scienceMap.mafd.fpf.dot.svg"))) {
				this.jcbm4nspfconsRed.setEnabled(true);
			} else {
				this.jcbm4nspfconsRed.setEnabled(false);
			}
			this.jcbm4small.setText(LocaleKBCT.GetString("OriginalFingram")
					+ " (SMOTE)");
			if ((!this.regOpt)
					&& ((smOpt.equals("WS")) || (smOpt.equals("ALL")))
					&& (this
							.checkFingramExists(".smote.scienceMap.mafd.dot.svg"))) {
				this.jcbm4small.setEnabled(true);
			} else {
				this.jcbm4small.setEnabled(false);
			}
			this.jcbm4smpfcons.setText("PFNET: "
					+ LocaleKBCT.GetString("OnlyConsistency") + " (SMOTE)");
			if ((!this.regOpt)
					&& ((smOpt.equals("WS")) || (smOpt.equals("ALL")))
					&& (this
							.checkFingramExists(".smote.scienceMap.mafdc.fpf.dot.svg"))) {
				this.jcbm4smpfcons.setEnabled(true);
			} else {
				this.jcbm4smpfcons.setEnabled(false);
			}
			this.jcbm4smpfconsRed.setText("PFNET: "
					+ LocaleKBCT.GetString("ConsistencyAndRedundancy")
					+ " (SMOTE)");
			if ((!this.regOpt)
					&& ((smOpt.equals("WS")) || (smOpt.equals("ALL")))
					&& (this
							.checkFingramExists(".smote.scienceMap.mafd.fpf.dot.svg"))) {
				this.jcbm4smpfconsRed.setEnabled(true);
			} else {
				this.jcbm4smpfconsRed.setEnabled(false);
			}
		}*/
		/*this.jcbm4nsall.setSelected(false);
		this.jcbm4nspfcons.setSelected(false);
		this.jcbm4nspfconsRed.setSelected(false);
		this.jcbm4small.setSelected(false);
		this.jcbm4smpfcons.setSelected(false);
		this.jcbm4smpfconsRed.setSelected(false);*/

		if (selFing!=null)
		    this.setSelectedFingrams(selFing);
			
		/*if (fingramsMetric.equals(LocaleKBCT.GetString("All"))) {
			main_TabbedPane.setSelectedIndex(0);
		} else if (fingramsMetric.equals("MS")) {
			main_TabbedPane.setSelectedIndex(0);
		} else if (fingramsMetric.equals("MSFD")) {
			main_TabbedPane.setSelectedIndex(1);
		} else if (fingramsMetric.equals("MA")) {
			main_TabbedPane.setSelectedIndex(2);
		} else if (fingramsMetric.equals("MAFD")) {
			main_TabbedPane.setSelectedIndex(3);
		}*/
		if (this.regOpt) {
			jPanelSaisieMS.add(this.jcbm1nsall, new GridBagConstraints(1, 0, 1,
					1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMS.add(this.jcbm1nspfconsRed, new GridBagConstraints(2,
					0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMS.add(this.jcbm1nspfinstance, new GridBagConstraints(3,
					0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		} else {
			jPanelSaisieMS.add(this.jcbm1nsall, new GridBagConstraints(1, 0, 1,
					1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			//jPanelSaisieMS.add(this.jcbm1small, new GridBagConstraints(1, 1, 1,
				//	1, 0.0, 0.0, GridBagConstraints.WEST,
					//GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMS.add(this.jcbm1nspfcons, new GridBagConstraints(2, 0,
					1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			//jPanelSaisieMS.add(this.jcbm1smpfcons, new GridBagConstraints(2, 1,
				//	1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					//GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMS.add(this.jcbm1nspfconsRed, new GridBagConstraints(3,
					0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			//jPanelSaisieMS.add(this.jcbm1smpfconsRed, new GridBagConstraints(3,
				//	1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					//GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMS.add(this.jcbm1nspfinstance, new GridBagConstraints(4,
					0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		}
		/*if (this.regOpt) {
			jPanelSaisieMSFD.add(this.jcbm2nsall, new GridBagConstraints(1, 0,
					1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMSFD.add(this.jcbm2nspfconsRed, new GridBagConstraints(
					2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		} else {
			jPanelSaisieMSFD.add(this.jcbm2nsall, new GridBagConstraints(1, 0,
					1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMSFD.add(this.jcbm2small, new GridBagConstraints(1, 1,
					1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMSFD.add(this.jcbm2nspfcons, new GridBagConstraints(2,
					0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMSFD.add(this.jcbm2smpfcons, new GridBagConstraints(2,
					1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMSFD.add(this.jcbm2nspfconsRed, new GridBagConstraints(
					3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMSFD.add(this.jcbm2smpfconsRed, new GridBagConstraints(
					3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		}*/
		/*if (this.regOpt) {
			jPanelSaisieMA.add(this.jcbm3nsall, new GridBagConstraints(1, 0, 1,
					1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMA.add(this.jcbm3nspfconsRed, new GridBagConstraints(2,
					0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		} else {
			jPanelSaisieMA.add(this.jcbm3nsall, new GridBagConstraints(1, 0, 1,
					1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMA.add(this.jcbm3small, new GridBagConstraints(1, 1, 1,
					1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMA.add(this.jcbm3nspfcons, new GridBagConstraints(2, 0,
					1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMA.add(this.jcbm3smpfcons, new GridBagConstraints(2, 1,
					1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMA.add(this.jcbm3nspfconsRed, new GridBagConstraints(3,
					0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMA.add(this.jcbm3smpfconsRed, new GridBagConstraints(3,
					1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		}*/
		/*if (this.regOpt) {
			jPanelSaisieMAFD.add(this.jcbm4nsall, new GridBagConstraints(1, 0,
					1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMAFD.add(this.jcbm4nspfconsRed, new GridBagConstraints(
					2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		} else {
			jPanelSaisieMAFD.add(this.jcbm4nsall, new GridBagConstraints(1, 0,
					1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMAFD.add(this.jcbm4small, new GridBagConstraints(1, 1,
					1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMAFD.add(this.jcbm4nspfcons, new GridBagConstraints(2,
					0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMAFD.add(this.jcbm4smpfcons, new GridBagConstraints(2,
					1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMAFD.add(this.jcbm4nspfconsRed, new GridBagConstraints(
					3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisieMAFD.add(this.jcbm4smpfconsRed, new GridBagConstraints(
					3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		}*/
		jdSVG.getContentPane().add(jPanelValidation, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5),
						0, 0));
		jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jPanelValidation.add(jButtonCancel, new GridBagConstraints(1, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jPanelValidation.add(jButtonHelp, new GridBagConstraints(2, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jButtonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// System.out.println("Apply");
				// System.out.println(JKBCTFrame.this.getTitle().substring(4)+
				// "." +
				// jnifis.LinksRulesLinksExtension()+".scienceMap.ms.dot.svg");
                JKBCTFrame.this.displaySelectedFingrams();
			}
		});
		jButtonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JKBCTFrame.this.cancelSVG();
			}
		});
		jButtonHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonFingrams.html"));
				MainKBCT.getJB().setVisible(true);
			}
		});
		this.jdSVG.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				for (int n = 0; n < JKBCTFrame.this.fingrams.length; n++) {
					if (JKBCTFrame.this.fingrams[n] != null)
						JKBCTFrame.this.fingrams[n].this_windowClosing();
				}
				jdSVG.dispose();
				//System.out.println("closeSVG -> automatic -> "+MainKBCT.getConfig().GetTESTautomatic());
				if (!MainKBCT.getConfig().GetTESTautomatic()) {
				    MainKBCT.getConfig().SetFingramsSelectedSample(LocaleKBCT.DefaultFingramsSelectedSample());
	                MainKBCT.getConfig().SetFingramsLayout(LocaleKBCT.DefaultFingramsLayout());
	                MainKBCT.getConfig().SetFingramsMetric(LocaleKBCT.DefaultFingramsMetric());
				}
			}

			public void windowActivated(WindowEvent e) {
			}
		});
		jdSVG.setResizable(false);
		jdSVG.pack();
		jdSVG.setLocation(JChildFrame.ChildPosition(this, jdSVG.getSize()));
		jdSVG.setVisible(true);
	}
	// ------------------------------------------------------------------------------
    protected void cancelSVG() {
		if (JInferenceFrame.jfc!=null) {
		     for (int n=0; n<JInferenceFrame.jfc.length; n++) {
		    	   if ( (JInferenceFrame.jfc[n]!=null) && (!JInferenceFrame.jfc[n].isActive()) )
		    		   JInferenceFrame.jfc[n].dispose();
		     }
		}
		for (int n = 0; n < this.fingrams.length; n++) {
			if (this.fingrams[n] != null)
				this.fingrams[n].this_windowClosing();
		}
		this.jdSVG.dispose();
		//System.out.println("cancelSVG -> automatic -> "+MainKBCT.getConfig().GetTESTautomatic());
		if (!MainKBCT.getConfig().GetTESTautomatic()) {
		    MainKBCT.getConfig().SetFingramsSelectedSample(LocaleKBCT.DefaultFingramsSelectedSample());
            MainKBCT.getConfig().SetFingramsLayout(LocaleKBCT.DefaultFingramsLayout());
            MainKBCT.getConfig().SetFingramsMetric(LocaleKBCT.DefaultFingramsMetric());
		}
    }
	//------------------------------------------------------------------------------
    protected void displaySelectedFingrams() {
		if (this.jcbm1nsall.isSelected()) {
			this.buildFingramWindow(0, LocaleKBCT.GetString("OriginalFingram"),
					"Complete.svg");
		}
		/*if (this.jcbm1small.isSelected()) {
			this.buildFingramWindow(1, "MS: "
					+ LocaleKBCT.GetString("OriginalFingram")
					+ " (SMOTE)", ".smote.scienceMap.ms.dot.svg");
		}*/
		if (this.jcbm1nspfcons.isSelected()) {
			//this.buildFingramWindow(2, "MS: PFNET - "
			this.buildFingramWindow(1, "PFNET - "
					+ LocaleKBCT.GetString("OnlyConsistency"),
					"InconsistenciesPathfinder.svg");
		}
		/*if (this.jcbm1smpfcons.isSelected()) {
			this.buildFingramWindow(3, "MS: PFNET - "
					+ LocaleKBCT.GetString("OnlyConsistency")
					+ " (SMOTE)", ".smote.scienceMap.msc.mpf.dot.svg");
		}*/
		if (this.jcbm1nspfconsRed.isSelected()) {
			if (this.regOpt)
				//this.buildFingramWindow(4, "MS: PFNET", ".scienceMap.ms.mpf.dot.svg");
			    this.buildFingramWindow(2, "PFNET", "Pathfinder.svg");
			else
				//this.buildFingramWindow(4, "MS: PFNET - "
				this.buildFingramWindow(2, "PFNET - "
						+ LocaleKBCT.GetString("ConsistencyAndRedundancy"), "Pathfinder.svg");
		}
		if (this.jcbm1nspfinstance.isSelected()) {
			//this.buildFingramWindow(2, "MS: PFNET - "
			this.buildFingramWindow(3, LocaleKBCT.GetString("InstanceFingram"),
					"PathfinderInstance.svg");
		}
		/*if (this.jcbm1smpfconsRed.isSelected()) {
			this.buildFingramWindow(5, "MS: PFNET - "
					+ LocaleKBCT.GetString("ConsistencyAndRedundancy")
					+ " (SMOTE)", ".smote.scienceMap.ms.mpf.dot.svg");
		}*/
		/*if (this.jcbm2nsall.isSelected()) {
			this.buildFingramWindow(6, "MSFD: "
					+ LocaleKBCT.GetString("OriginalFingram"),
					".scienceMap.msfd.dot.svg");
		}
		if (this.jcbm2small.isSelected()) {
			this.buildFingramWindow(7, "MSFD: "
					+ LocaleKBCT.GetString("OriginalFingram")
					+ " (SMOTE)", ".smote.scienceMap.msfd.dot.svg");
		}
		if (this.jcbm2nspfcons.isSelected()) {
			this.buildFingramWindow(8, "MSFD: PFNET - "
					+ LocaleKBCT.GetString("OnlyConsistency"),
					".scienceMap.msfdc.mpf.dot.svg");
		}
		if (this.jcbm2smpfcons.isSelected()) {
			this.buildFingramWindow(9, "MSFD: PFNET - "
							+ LocaleKBCT.GetString("OnlyConsistency")
							+ " (SMOTE)",
							".smote.scienceMap.msfdc.mpf.dot.svg");
		}
		if (this.jcbm2nspfconsRed.isSelected()) {
			if (this.regOpt)
				this.buildFingramWindow(10, "MSFD: PFNET",
						".scienceMap.msfd.mpf.dot.svg");
			else
				this.buildFingramWindow(10, "MSFD: PFNET - "
						+ LocaleKBCT.GetString("ConsistencyAndRedundancy"),
						".scienceMap.msfd.mpf.dot.svg");
		}
		if (this.jcbm2smpfconsRed.isSelected()) {
			this.buildFingramWindow(11, "MSFD: PFNET - "
					+ LocaleKBCT.GetString("ConsistencyAndRedundancy")
					+ " (SMOTE)", ".smote.scienceMap.msfd.mpf.dot.svg");
		}
		if (this.jcbm3nsall.isSelected()) {
			this.buildFingramWindow(12, "MA: "
					+ LocaleKBCT.GetString("OriginalFingram"),
					".scienceMap.ma.dot.svg");
		}
		if (this.jcbm3small.isSelected()) {
			this.buildFingramWindow(13, "MA: "
					+ LocaleKBCT.GetString("OriginalFingram")
					+ " (SMOTE)", ".smote.scienceMap.ma.dot.svg");
		}
		if (this.jcbm3nspfcons.isSelected()) {
			this.buildFingramWindow(14, "MA: PFNET - "
					+ LocaleKBCT.GetString("OnlyConsistency"),
					".scienceMap.mac.fpf.dot.svg");
		}
		if (this.jcbm3smpfcons.isSelected()) {
			this.buildFingramWindow(15, "MA: PFNET - "
					+ LocaleKBCT.GetString("OnlyConsistency")
					+ " (SMOTE)", ".smote.scienceMap.mac.fpf.dot.svg");
		}
		if (this.jcbm3nspfconsRed.isSelected()) {
			if (this.regOpt)
				this.buildFingramWindow(16, "MA: PFNET",
						".scienceMap.ma.fpf.dot.svg");
			else
				this.buildFingramWindow(16, "MA: PFNET - "
						+ LocaleKBCT.GetString("ConsistencyAndRedundancy"),
						".scienceMap.ma.fpf.dot.svg");
		}
		if (this.jcbm3smpfconsRed.isSelected()) {
			this.buildFingramWindow(17, "MA: PFNET - "
					+ LocaleKBCT.GetString("ConsistencyAndRedundancy")
					+ " (SMOTE)", ".smote.scienceMap.ma.fpf.dot.svg");
		}
		if (this.jcbm4nsall.isSelected()) {
			this.buildFingramWindow(18, "MAFD: "
					+ LocaleKBCT.GetString("OriginalFingram"),
					".scienceMap.mafd.dot.svg");
		}
		if (this.jcbm4small.isSelected()) {
			this.buildFingramWindow(19, "MAFD: "
					+ LocaleKBCT.GetString("OriginalFingram")
					+ " (SMOTE)", ".smote.scienceMap.mafd.dot.svg");
		}
		if (this.jcbm4nspfcons.isSelected()) {
			this.buildFingramWindow(20, "MAFD: PFNET - "
					+ LocaleKBCT.GetString("OnlyConsistency"),
					".scienceMap.mafdc.fpf.dot.svg");
		}
		if (this.jcbm4smpfcons.isSelected()) {
			this.buildFingramWindow(21, "MAFD: PFNET - "
							+ LocaleKBCT.GetString("OnlyConsistency")
							+ " (SMOTE)",
							".smote.scienceMap.mafdc.fpf.dot.svg");
		}
		if (this.jcbm4nspfconsRed.isSelected()) {
			if (this.regOpt)
				this.buildFingramWindow(22, "MAFD: PFNET",
						".scienceMap.mafd.fpf.dot.svg");
			else
				this.buildFingramWindow(22, "MAFD: PFNET - "
						+ LocaleKBCT.GetString("ConsistencyAndRedundancy"),
						".scienceMap.mafd.fpf.dot.svg");
		}
		if (this.jcbm4smpfconsRed.isSelected()) {
			this.buildFingramWindow(23, "MAFD: PFNET - "
					+ LocaleKBCT.GetString("ConsistencyAndRedundancy")
					+ " (SMOTE)", ".smote.scienceMap.mafd.fpf.dot.svg");
		}*/
    }
	// ------------------------------------------------------------------------------
	private boolean checkFingramExists(String fname) {
		try {
			//System.out.println("checkFingramExists: "+this.Parent.jef.jif.data_file_name);
			//System.out.println("checkFingramExists: "+this.jef.jif.data_file_name+"."+jnifis.LinksItemsRulesExtension());
			//System.out.println("checkFingramExists: "+fname);
			//System.out.println("checkFingramExists: "+this.getTitle());
			//System.out.println("checkFingramExists: "+this.jef.getTitle());
			String file;
			if (this.jef != null)
				file = this.jef.getTitle().substring(4) + "." + jnifis.LinksRulesLinksExtension() + "." + fname;
			else
				file = this.getTitle().substring(4) + "." + jnifis.LinksRulesLinksExtension() + "." + fname;
		    
			//System.out.println("checkFingramExists: "+file);
			File f = new File(file);
			if (f.exists())
				return true;
			else
				return false;

		} catch (Throwable except) {
			except.printStackTrace();
			MessageKBCT.Error(null, except);
		}
		return false;
	}
	// ------------------------------------------------------------------------------
	protected boolean[] getSelectedFingrams() {
		boolean[] res= new boolean[4];
		res[0]=this.jcbm1nsall.isSelected();
		res[1]=this.jcbm1nspfcons.isSelected();
		res[2]=this.jcbm1nspfconsRed.isSelected();
		res[3]=this.jcbm1nspfinstance.isSelected();
		/*res[3]=this.jcbm1small.isSelected();
		res[4]=this.jcbm1smpfcons.isSelected();
		res[5]=this.jcbm1smpfconsRed.isSelected();
		res[6]=this.jcbm2nsall.isSelected();
		res[7]=this.jcbm2nspfcons.isSelected();
		res[8]=this.jcbm2nspfconsRed.isSelected();
		res[9]=this.jcbm2small.isSelected();
		res[10]=this.jcbm2smpfcons.isSelected();
		res[11]=this.jcbm2smpfconsRed.isSelected();
		res[12]=this.jcbm3nsall.isSelected();
		res[13]=this.jcbm3nspfcons.isSelected();
		res[14]=this.jcbm3nspfconsRed.isSelected();
		res[15]=this.jcbm3small.isSelected();
		res[16]=this.jcbm3smpfcons.isSelected();
		res[17]=this.jcbm3smpfconsRed.isSelected();
		res[18]=this.jcbm4nsall.isSelected();
		res[19]=this.jcbm4nspfcons.isSelected();
		res[20]=this.jcbm4nspfconsRed.isSelected();
		res[21]=this.jcbm4small.isSelected();
		res[22]=this.jcbm4smpfcons.isSelected();
		res[23]=this.jcbm4smpfconsRed.isSelected();*/
		return res;
	}
	// ------------------------------------------------------------------------------
	protected void setSelectedFingrams(boolean[] sel) {
		this.jcbm1nsall.setSelected(sel[0]);
		this.jcbm1nspfcons.setSelected(sel[1]);
		this.jcbm1nspfconsRed.setSelected(sel[2]);
		this.jcbm1nspfinstance.setSelected(sel[3]);
		/*this.jcbm1small.setSelected(sel[3]);
		this.jcbm1smpfcons.setSelected(sel[4]);
		this.jcbm1smpfconsRed.setSelected(sel[5]);
		this.jcbm2nsall.setSelected(sel[6]);
		this.jcbm2nspfcons.setSelected(sel[7]);
		this.jcbm2nspfconsRed.setSelected(sel[8]);
		this.jcbm2small.setSelected(sel[9]);
		this.jcbm2smpfcons.setSelected(sel[10]);
		this.jcbm2smpfconsRed.setSelected(sel[11]);
		this.jcbm3nsall.setSelected(sel[12]);
		this.jcbm3nspfcons.setSelected(sel[13]);
		this.jcbm3nspfconsRed.setSelected(sel[14]);
		this.jcbm3small.setSelected(sel[15]);
		this.jcbm3smpfcons.setSelected(sel[16]);
		this.jcbm3smpfconsRed.setSelected(sel[17]);
		this.jcbm4nsall.setSelected(sel[18]);
		this.jcbm4nspfcons.setSelected(sel[19]);
		this.jcbm4nspfconsRed.setSelected(sel[20]);
		this.jcbm4small.setSelected(sel[21]);
		this.jcbm4smpfcons.setSelected(sel[22]);
		this.jcbm4smpfconsRed.setSelected(sel[23]);*/
	}
	// ------------------------------------------------------------------------------
	private void buildFingramWindow(int W, String find, String fname) {
		try {
	        if ((this.fingrams[W] != null) && (this.fingrams[W].isVisible())) {
	        	this.fingrams[W].dispose();
	        	this.fingrams[W]=null;
	        }
			if ((this.fingrams[W] == null)
					|| ((this.fingrams[W] != null) && (!this.fingrams[W].isVisible()))) {
				if (this.checkFingramExists(fname)) {
					if (this.jef != null)
						this.fingrams[W] = new JFingramsFrame(this, find, this.jef.getTitle().substring(4) + "." + jnifis.LinksRulesLinksExtension() + "." + fname);
					else
						this.fingrams[W] = new JFingramsFrame(this, find, this.getTitle().substring(4) + "." + jnifis.LinksRulesLinksExtension() + "." + fname);
				    
					this.fingrams[W].setLocation(0, 10 * W);
				}
			} /*
			 * else { if (this.fingrams[W].isVisible())
			 * System.out.println("already open"); else
			 * System.out.println("close -> reopen"); }
			 */
		} catch (Throwable except) {
			except.printStackTrace();
			MessageKBCT.Error(null, except);
		    MainKBCT.getConfig().SetFingramsSelectedSample(LocaleKBCT.DefaultFingramsSelectedSample());
            MainKBCT.getConfig().SetFingramsLayout(LocaleKBCT.DefaultFingramsLayout());
            MainKBCT.getConfig().SetFingramsMetric(LocaleKBCT.DefaultFingramsMetric());
		}
	}
	// ------------------------------------------------------------------------------
		/**
		 * set PathFindeThres parameter
		 */
		public void setPathFinderParameters(int nn, int nar) {
            this.NbNodes= nn;
            this.NbActiveRules= nar;
			final JDialog jd = new JDialog(this);
			jd.setIconImage(LocaleKBCT.getIconGUAJE().getImage());
			jd.setTitle(LocaleKBCT.GetString("Fingrams"));
			jd.getContentPane().setLayout(new GridBagLayout());
			JPanel jPanelSaisie = new JPanel(new GridBagLayout());
			JPanel jPanelValidation = new JPanel(new GridBagLayout());
			JButton jButtonApply = new JButton(LocaleKBCT.GetString("Apply"));
			JButton jButtonCancel = new JButton(LocaleKBCT.GetString("Cancel"));
			JButton jButtonDefault = new JButton(LocaleKBCT.GetString("DefaultValues"));
			JLabel jLabelGoodnessHighThres = new JLabel(LocaleKBCT.GetString("GoodnessThres")+" ("+LocaleKBCT.GetString("high")+"):");
			this.jGoodnessHighThres.setValue(MainKBCT.getConfig().GetGoodnessHighThreshold());
			JLabel jLabelGoodnessLowThres = new JLabel(LocaleKBCT.GetString("GoodnessThres")+" ("+LocaleKBCT.GetString("low")+"):");
			this.jGoodnessLowThres.setValue(MainKBCT.getConfig().GetGoodnessLowThreshold());
			JLabel jLabelPathfinderThres = new JLabel(LocaleKBCT.GetString("PathFinderThres")	+ " :");
			this.jPathFinderThres.setValue(MainKBCT.getConfig().GetPathFinderThreshold());
			JLabel jLabelParQ = new JLabel(LocaleKBCT.GetString("ParameterQ") + " :");
			DefaultComboBoxModel jDCBMparQ = new DefaultComboBoxModel() {
			    static final long serialVersionUID=0;	
			    public Object getElementAt(int index) {
			    	if ( ( (JKBCTFrame.this.NbNodes > 2) && (index+2==JKBCTFrame.this.NbNodes) ) ||
			    			( (JKBCTFrame.this.NbNodes <= 2) && (index==1) ) )
			    		return JKBCTFrame.this.NbActiveRules-1;
			    	else
                        return index+2;
			      }
			    };
			this.jCBparQ= new JComboBox(jDCBMparQ);
			
			jd.getContentPane().add(jPanelSaisie, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(5, 5, 5, 5), 0, 0));
			jPanelSaisie.add(jLabelGoodnessHighThres, new GridBagConstraints(0, 0,
					1, 1, 0.0, 0.0, GridBagConstraints.EAST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSaisie.add(this.jGoodnessHighThres, new GridBagConstraints(1, 0,
					1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
			jPanelSaisie.add(jLabelGoodnessLowThres, new GridBagConstraints(0, 1,
					1, 1, 0.0, 0.0, GridBagConstraints.EAST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSaisie.add(this.jGoodnessLowThres, new GridBagConstraints(1, 1,
					1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
			jPanelSaisie.add(jLabelPathfinderThres, new GridBagConstraints(0, 2,
					1, 1, 0.0, 0.0, GridBagConstraints.EAST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSaisie.add(this.jPathFinderThres, new GridBagConstraints(1, 2,
					1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
			jPanelSaisie.add(jLabelParQ, new GridBagConstraints(0, 3,
					1, 1, 0.0, 0.0, GridBagConstraints.EAST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

			for( int i=2 ; i<this.NbNodes ; i++ )
		         jDCBMparQ.addElement(new String());

			//System.out.println("this.NbNodes -> "+this.NbNodes);
			//System.out.println("this.NbActiveRules -> "+this.NbActiveRules);
			
			if (this.NbNodes < this.NbActiveRules)
				jDCBMparQ.addElement(new String());
			
            if (this.NbNodes > 2)
			    jDCBMparQ.setSelectedItem(this.NbNodes-1);
            else {
				jDCBMparQ.addElement(new String());
			    jDCBMparQ.setSelectedItem(this.NbActiveRules-1);
            }
			jPanelSaisie.add(jCBparQ, new GridBagConstraints(1, 3,
					1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
			
			jd.getContentPane().add(jPanelValidation, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5),
							0, 0));
			jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1,
					1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
					new Insets(5, 5, 5, 5), 0, 0));
			jPanelValidation.add(jButtonCancel, new GridBagConstraints(1, 0, 1, 1,
					1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
					new Insets(5, 5, 5, 5), 0, 0));
			jPanelValidation.add(jButtonDefault, new GridBagConstraints(2, 0, 1, 1,
					1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
					new Insets(5, 5, 5, 5), 0, 0));
			jButtonApply.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					double valueGTh = JKBCTFrame.this.jGoodnessHighThres.getValue();
					double valueGTl = JKBCTFrame.this.jGoodnessLowThres.getValue();
					double valuePT = JKBCTFrame.this.jPathFinderThres.getValue();
					if ((valueGTh < 0) || (valueGTh > 1) || (valueGTl < 0) || (valueGTl > 1) || (valuePT < 0) || (valuePT > 1)) {
						MessageKBCT.Information(JKBCTFrame.this, LocaleKBCT.GetString("PTGTShouldBe"));
					} else {
						MainKBCT.getConfig().SetGoodnessHighThreshold(valueGTh);
						MainKBCT.getConfig().SetGoodnessLowThreshold(valueGTl);
						MainKBCT.getConfig().SetPathFinderThreshold(valuePT);
						int qvalue = JKBCTFrame.this.jCBparQ.getSelectedIndex()+2;
						//System.out.println("Q="+qvalue);
						MainKBCT.getConfig().SetPathFinderParQ(qvalue);
						jd.dispose();
					}
				}
			});
			jButtonCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jd.dispose();
				}
			});
			jButtonDefault.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JKBCTFrame.this.jGoodnessHighThres.setValue(LocaleKBCT.DefaultGoodnessHighThreshold());
					JKBCTFrame.this.jGoodnessLowThres.setValue(LocaleKBCT.DefaultGoodnessLowThreshold());
					JKBCTFrame.this.jPathFinderThres.setValue(LocaleKBCT.DefaultPathFinderThreshold());
				}
			});
            jd.setResizable(false);
			jd.setModal(true);
			jd.pack();
			jd.setLocation(JChildFrame.ChildPosition(this, jd.getSize()));
			jd.setVisible(true);
		}
		//------------------------------------------------------------------------------
		  public void updateHelpMenu(boolean o1, boolean o2, boolean o3) {
			this.jRBMenuBeginner.setSelected(o1);
			this.jRBMenuExpert.setSelected(o2);
			this.jMenuItemHelp.setEnabled(o3);
			SwingUtilities.updateComponentTreeUI(this);
			if (this.jef!=null) {
			    this.jef.jRBMenuBeginner.setSelected(o1);
			    this.jef.jRBMenuExpert.setSelected(o2);
			    this.jef.jMenuItemHelp.setEnabled(o3);
			    SwingUtilities.updateComponentTreeUI(this.jef);
			}
		  }
}