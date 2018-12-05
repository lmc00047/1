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
//                          JTutorialFrame.java
//
//
//**********************************************************************

package kbctFrames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension; //import java.awt.GridBagConstraints;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
//import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent; //import java.util.Vector;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.swing.ImageIcon;
import javax.swing.JFrame; //import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem; //import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener; //import javax.swing.event.MenuEvent;
//import javax.swing.event.MenuListener;

import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbctAux.MessageKBCT;
import kbctAux.Translatable;

/**
 * kbctFrames.JTutorialFrame creates a Frame to display HTML help files.
 * 
 *@author Jose Maria Alonso Moral
 *@version 3.0 , 03/08/15
 */
public class JTutorialFrame extends JFrame implements Translatable {
	static final long serialVersionUID = 0;
	private JMenuBar jMenuBarBF = new JMenuBar();
	private JMenuItem jMenuClose = new JMenuItem();
	private ImageIcon icon_kbct = LocaleKBCT.getIconGUAJE();
	private JScrollPane jScrollPanel = new JScrollPane();
	private JTextPane jTextPanel = new JTextPane();
	private JScrollPane jScrollPanelQuality = new JScrollPane();
	private JTextPane jTextPanelQuality = new JTextPane();
	private JTabbedPane main_TabbedPaneExpert = new JTabbedPane();
	private JTabbedPane main_TabbedPaneData = new JTabbedPane();
	private JTabbedPane main_TabbedPaneFingrams = new JTabbedPane();
	private JTabbedPane main_TabbedPaneKBimprovement = new JTabbedPane();
	private JScrollPane jScrollPanelSE0 = new JScrollPane();
	private JTextPane jTextPanelSE0 = new JTextPane();
	private JScrollPane jScrollPanelSE1 = new JScrollPane();
	private JTextPane jTextPanelSE1 = new JTextPane();
	private JScrollPane jScrollPanelSE2 = new JScrollPane();
	private JTextPane jTextPanelSE2 = new JTextPane();
	private JScrollPane jScrollPanelSE3 = new JScrollPane();
	private JTextPane jTextPanelSE3 = new JTextPane();
	private JScrollPane jScrollPanelSE4 = new JScrollPane();
	private JTextPane jTextPanelSE4 = new JTextPane();
	private JScrollPane jScrollPanelSE5 = new JScrollPane();
	private JTextPane jTextPanelSE5 = new JTextPane();
	private JScrollPane jScrollPanelSE6 = new JScrollPane();
	private JTextPane jTextPanelSE6 = new JTextPane();
	private JScrollPane jScrollPanelSD0 = new JScrollPane();
	private JTextPane jTextPanelSD0 = new JTextPane();
	private JScrollPane jScrollPanelSD1 = new JScrollPane();
	private JTextPane jTextPanelSD1 = new JTextPane();
	private JScrollPane jScrollPanelSD2 = new JScrollPane();
	private JTextPane jTextPanelSD2 = new JTextPane();
	private JScrollPane jScrollPanelSD3 = new JScrollPane();
	private JTextPane jTextPanelSD3 = new JTextPane();
	private JScrollPane jScrollPanelSD4 = new JScrollPane();
	private JTextPane jTextPanelSD4 = new JTextPane();
	private JScrollPane jScrollPanelSD5 = new JScrollPane();
	private JTextPane jTextPanelSD5 = new JTextPane();
	private JScrollPane jScrollPanelSD6 = new JScrollPane();
	private JTextPane jTextPanelSD6 = new JTextPane();
	private JScrollPane jScrollPanelSD7 = new JScrollPane();
	private JTextPane jTextPanelSD7 = new JTextPane();
	private JScrollPane jScrollPanelFD0 = new JScrollPane();
	private JTextPane jTextPanelFD0 = new JTextPane();
	private JScrollPane jScrollPanelFD1 = new JScrollPane();
	private JTextPane jTextPanelFD1 = new JTextPane();
	private JScrollPane jScrollPanelFD2 = new JScrollPane();
	private JTextPane jTextPanelFD2 = new JTextPane();
	private JScrollPane jScrollPanelFD3 = new JScrollPane();
	private JTextPane jTextPanelFD3 = new JTextPane();
	private JScrollPane jScrollPanelFD4 = new JScrollPane();
	private JTextPane jTextPanelFD4 = new JTextPane();
	private JScrollPane jScrollPanelFD5 = new JScrollPane();
	private JTextPane jTextPanelFD5 = new JTextPane();
	private JScrollPane jScrollPanelFD6 = new JScrollPane();
	private JTextPane jTextPanelFD6 = new JTextPane();
	private JScrollPane jScrollPanelID0 = new JScrollPane();
	private JTextPane jTextPanelID0 = new JTextPane();
	private JScrollPane jScrollPanelID1 = new JScrollPane();
	private JTextPane jTextPanelID1 = new JTextPane();
	private JScrollPane jScrollPanelID2 = new JScrollPane();
	private JTextPane jTextPanelID2 = new JTextPane();
	private JScrollPane jScrollPanelID3 = new JScrollPane();
	private JTextPane jTextPanelID3 = new JTextPane();
	private JScrollPane jScrollPanelID4 = new JScrollPane();
	private JTextPane jTextPanelID4 = new JTextPane();
	private JScrollPane jScrollPanelID5 = new JScrollPane();
	private JTextPane jTextPanelID5 = new JTextPane();
	private JScrollPane jScrollPanelID6 = new JScrollPane();
	private JTextPane jTextPanelID6 = new JTextPane();
	private JScrollPane jScrollPanelID7 = new JScrollPane();
	private JTextPane jTextPanelID7 = new JTextPane();
	private JScrollPane jScrollPanelID8 = new JScrollPane();
	private JTextPane jTextPanelID8 = new JTextPane();
	private String PageView;
	private String GUAJEpath;
	private JOutputFrame jof;
	private Dimension dim = getToolkit().getScreenSize();
	private double[][] KBquality;

	// ----------------------------------------------------------------------------
	public JTutorialFrame(String PageHTML) {
		try {
			// System.out.println("JTutorialFrame");
			this.PageView = PageHTML;
			this.GUAJEpath = MainKBCT.getConfig().GetKBCTFilePath();
			// System.out.println("JTutorialFrame: "+this.GUAJEpath);
			MainKBCT.getConfig().SetTutorialFlag(true);
			this.jbInit();
			// this.setVisible(true);
			jMenuClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					this_windowClose(false);
				}
			});
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					this_windowClosing();
				}

				public void windowActivated(WindowEvent e) {
				}
			});
			this.jTextPanel.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelQuality.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelSE0.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelSE1.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelSE2.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelSE3.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelSE4.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelSE5.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelSE6.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelSD0.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelSD1.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelSD2.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelSD3.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelSD4.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelSD5.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelSD6.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelSD7.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelFD0.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelFD1.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelFD2.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelFD3.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelFD4.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelFD5.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelFD6.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelID0.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelID1.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelID2.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelID3.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelID4.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelID5.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelID6.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelID7.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			this.jTextPanelID8.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					this_hyperlinkUpdate(e);
				}
			});
			JKBCTFrame.AddTranslatable(this);
		} catch (Throwable t) {
			t.printStackTrace();
			MessageKBCT.Error(this, LocaleKBCT.GetString("Error"),
					"Error in building JTutorialFrame: " + t);
		}
	}
	// ----------------------------------------------------------------------------
	private void jbInit() throws Throwable {
		this.setIconImage(icon_kbct.getImage());
		this.setState(JTutorialFrame.NORMAL);
		this.setJMenuBar(this.jMenuBarBF);
		this.jMenuBarBF.add(this.jMenuClose);
		if (this.PageView.endsWith("TutExpert.html")) {
			this.getContentPane().add(this.main_TabbedPaneExpert,BorderLayout.CENTER);
			this.main_TabbedPaneExpert.addTab(LocaleKBCT.GetString("STEP") + 0, null,
							this.jScrollPanelSE0, LocaleKBCT.GetString("Outline"));
			this.jScrollPanelSE0.setWheelScrollingEnabled(true);
			this.jScrollPanelSE0.getViewport().add(jTextPanelSE0, null);
			this.main_TabbedPaneExpert.addTab(LocaleKBCT.GetString("STEP") + 1,
					null, this.jScrollPanelSE1, LocaleKBCT.GetString("NewKB"));
			this.jScrollPanelSE1.setWheelScrollingEnabled(true);
			this.jScrollPanelSE1.getViewport().add(jTextPanelSE1, null);
			this.main_TabbedPaneExpert.addTab(LocaleKBCT.GetString("STEP") + 2,
					null, this.jScrollPanelSE2, LocaleKBCT.GetString("CreateNewInput"));
			this.jScrollPanelSE2.setWheelScrollingEnabled(true);
			this.jScrollPanelSE2.getViewport().add(jTextPanelSE2, null);
			this.main_TabbedPaneExpert.addTab(LocaleKBCT.GetString("STEP") + 3,
					null, this.jScrollPanelSE3, LocaleKBCT.GetString("CreateNewOutput"));
			this.jScrollPanelSE3.setWheelScrollingEnabled(true);
			this.jScrollPanelSE3.getViewport().add(jTextPanelSE3, null);
			this.main_TabbedPaneExpert.addTab(LocaleKBCT.GetString("STEP") + 4,
					null, this.jScrollPanelSE4, LocaleKBCT.GetString("SetFISoptionsMenu"));
			this.jScrollPanelSE4.setWheelScrollingEnabled(true);
			this.jScrollPanelSE4.getViewport().add(jTextPanelSE4, null);
			this.main_TabbedPaneExpert.addTab(LocaleKBCT.GetString("STEP") + 5,
					null, this.jScrollPanelSE5, LocaleKBCT.GetString("DefineRules"));
			this.jScrollPanelSE5.setWheelScrollingEnabled(true);
			this.jScrollPanelSE5.getViewport().add(jTextPanelSE5, null);
			this.main_TabbedPaneExpert.addTab(LocaleKBCT.GetString("STEP") + 6,
					null, this.jScrollPanelSE6, LocaleKBCT.GetString("MakeInferences"));
			this.jScrollPanelSE6.setWheelScrollingEnabled(true);
			this.jScrollPanelSE6.getViewport().add(jTextPanelSE6, null);
			for (int n = 1; n < 7; n++) {
				this.main_TabbedPaneExpert.setEnabledAt(n, false);
			}
		} else if (this.PageView.endsWith("TutData.html")) {
			this.getContentPane().add(this.main_TabbedPaneData,	BorderLayout.CENTER);
			this.main_TabbedPaneData.addTab(LocaleKBCT.GetString("STEP") + 0, null,
							this.jScrollPanelSD0, LocaleKBCT.GetString("Outline"));
			this.jScrollPanelSD0.setWheelScrollingEnabled(true);
			this.jScrollPanelSD0.getViewport().add(jTextPanelSD0, null);
			this.main_TabbedPaneData.addTab(LocaleKBCT.GetString("STEP") + 1,
					null, this.jScrollPanelSD1, LocaleKBCT.GetString("NewKBdata"));
			this.jScrollPanelSD1.setWheelScrollingEnabled(true);
			this.jScrollPanelSD1.getViewport().add(jTextPanelSD1, null);
			this.main_TabbedPaneData.addTab(LocaleKBCT.GetString("STEP") + 2,
					null, this.jScrollPanelSD2, LocaleKBCT.GetString("DataVisualization"));
			this.jScrollPanelSD2.setWheelScrollingEnabled(true);
			this.jScrollPanelSD2.getViewport().add(jTextPanelSD2, null);
			this.main_TabbedPaneData.addTab(LocaleKBCT.GetString("STEP") + 3,
					null, this.jScrollPanelSD3, LocaleKBCT.GetString("Induce_Partitions"));
			this.jScrollPanelSD3.setWheelScrollingEnabled(true);
			this.jScrollPanelSD3.getViewport().add(jTextPanelSD3, null);
			this.main_TabbedPaneData.addTab(LocaleKBCT.GetString("STEP") + 4,
					null, this.jScrollPanelSD4, LocaleKBCT.GetString("SetFISoptionsMenu"));
			this.jScrollPanelSD4.setWheelScrollingEnabled(true);
			this.jScrollPanelSD4.getViewport().add(jTextPanelSD4, null);
			this.main_TabbedPaneData.addTab(LocaleKBCT.GetString("STEP") + 5,
					null, this.jScrollPanelSD5, LocaleKBCT.GetString("Induce_Rules"));
			this.jScrollPanelSD5.setWheelScrollingEnabled(true);
			this.jScrollPanelSD5.getViewport().add(jTextPanelSD5, null);
			this.main_TabbedPaneData.addTab(LocaleKBCT.GetString("STEP") + 6,
					null, this.jScrollPanelSD6, LocaleKBCT.GetString("MakeInferences"));
			this.jScrollPanelSD6.setWheelScrollingEnabled(true);
			this.jScrollPanelSD6.getViewport().add(jTextPanelSD6, null);
			this.main_TabbedPaneData.addTab(LocaleKBCT.GetString("STEP") + 7,
					null, this.jScrollPanelSD7, LocaleKBCT.GetString("QualityMsg"));
			this.jScrollPanelSD7.setWheelScrollingEnabled(true);
			this.jScrollPanelSD7.getViewport().add(jTextPanelSD7, null);

			for (int n = 1; n < 8; n++) {
				this.main_TabbedPaneData.setEnabledAt(n, false);
			}
		} else if (this.PageView.endsWith("TutFingrams.html")) {
			this.getContentPane().add(this.main_TabbedPaneFingrams,	BorderLayout.CENTER);
			this.main_TabbedPaneFingrams.addTab(LocaleKBCT.GetString("STEP") + 0, null,
					this.jScrollPanelFD0, LocaleKBCT.GetString("Outline"));
			this.jScrollPanelFD0.setWheelScrollingEnabled(true);
			this.jScrollPanelFD0.getViewport().add(jTextPanelFD0, null);
			this.main_TabbedPaneFingrams.addTab(LocaleKBCT.GetString("STEP") + 1, null,
					this.jScrollPanelFD1, LocaleKBCT.GetString("NewKBdata"));
			this.jScrollPanelFD1.setWheelScrollingEnabled(true);
			this.jScrollPanelFD1.getViewport().add(jTextPanelFD1, null);
			this.main_TabbedPaneFingrams.addTab(LocaleKBCT.GetString("STEP") + 2, null,
					this.jScrollPanelFD2, LocaleKBCT.GetString("Induce_Partitions"));
			this.jScrollPanelFD2.setWheelScrollingEnabled(true);
			this.jScrollPanelFD2.getViewport().add(jTextPanelFD2, null);
			this.main_TabbedPaneFingrams.addTab(LocaleKBCT.GetString("STEP") + 3, null,
					this.jScrollPanelFD3, LocaleKBCT.GetString("SetFISoptionsMenu"));
			this.jScrollPanelFD3.setWheelScrollingEnabled(true);
			this.jScrollPanelFD3.getViewport().add(jTextPanelFD3, null);
			this.main_TabbedPaneFingrams.addTab(LocaleKBCT.GetString("STEP") + 4, null,
					this.jScrollPanelFD4, LocaleKBCT.GetString("Induce_Rules"));
			this.jScrollPanelFD4.setWheelScrollingEnabled(true);
			this.jScrollPanelFD4.getViewport().add(jTextPanelFD4, null);
			this.main_TabbedPaneFingrams.addTab(LocaleKBCT.GetString("STEP") + 5, null,
					this.jScrollPanelFD5, LocaleKBCT.GetString("GenerateFingrams"));
			this.jScrollPanelFD5.setWheelScrollingEnabled(true);
			this.jScrollPanelFD5.getViewport().add(jTextPanelFD5, null);
			this.main_TabbedPaneFingrams.addTab(LocaleKBCT.GetString("STEP") + 6, null,
					this.jScrollPanelFD6, LocaleKBCT.GetString("VisualizeFingrams"));
			this.jScrollPanelFD6.setWheelScrollingEnabled(true);
			this.jScrollPanelFD6.getViewport().add(jTextPanelFD6, null);

			for (int n = 1; n < 7; n++) {
				this.main_TabbedPaneFingrams.setEnabledAt(n, false);
			}
		} else if (this.PageView.endsWith("TutImprovement.html")) {
			//this.getContentPane().add(this.main_TabbedPaneKBimprovement, BorderLayout.CENTER);
		    this.getContentPane().setLayout(new GridBagLayout());
		    this.getContentPane().add(this.main_TabbedPaneKBimprovement, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
		              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 400));
		    this.getContentPane().add(this.jScrollPanelQuality, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
		              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 450));

		    this.jScrollPanelQuality.getViewport().add(jTextPanelQuality, null);
			this.jScrollPanelQuality.setVisible(false);
			this.KBquality= new double[4][10];

			this.main_TabbedPaneKBimprovement.addTab(LocaleKBCT.GetString("STEP") + 0, null,
					this.jScrollPanelID0, LocaleKBCT.GetString("Outline"));
			this.jScrollPanelID0.setWheelScrollingEnabled(true);
			this.jScrollPanelID0.getViewport().add(jTextPanelID0, null);
			this.main_TabbedPaneKBimprovement.addTab(LocaleKBCT.GetString("STEP") + 1, null,
					this.jScrollPanelID1, LocaleKBCT.GetString("NewKBdata"));
			this.jScrollPanelID1.setWheelScrollingEnabled(true);
			this.jScrollPanelID1.getViewport().add(jTextPanelID1, null);
			this.main_TabbedPaneKBimprovement.addTab(LocaleKBCT.GetString("STEP") + 2, null,
					this.jScrollPanelID2, LocaleKBCT.GetString("Induce_Partitions"));
			this.jScrollPanelID2.setWheelScrollingEnabled(true);
			this.jScrollPanelID2.getViewport().add(jTextPanelID2, null);
			this.main_TabbedPaneKBimprovement.addTab(LocaleKBCT.GetString("STEP") + 3, null,
					this.jScrollPanelID3, LocaleKBCT.GetString("SetFISoptionsMenu"));
			this.jScrollPanelID3.setWheelScrollingEnabled(true);
			this.jScrollPanelID3.getViewport().add(jTextPanelID3, null);
			this.main_TabbedPaneKBimprovement.addTab(LocaleKBCT.GetString("STEP") + 4, null,
					this.jScrollPanelID4, LocaleKBCT.GetString("Induce_Rules"));
			this.jScrollPanelID4.setWheelScrollingEnabled(true);
			this.jScrollPanelID4.getViewport().add(jTextPanelID4, null);
			this.main_TabbedPaneKBimprovement.addTab(LocaleKBCT.GetString("STEP") + 5, null,
					this.jScrollPanelID5, LocaleKBCT.GetString("GenerateAnalyzeFingrams"));
			this.jScrollPanelID5.setWheelScrollingEnabled(true);
			this.jScrollPanelID5.getViewport().add(jTextPanelID5, null);
			this.main_TabbedPaneKBimprovement.addTab(LocaleKBCT.GetString("STEP") + 6, null,
					this.jScrollPanelID6, LocaleKBCT.GetString("SimplifyMsg"));
			this.jScrollPanelID6.setWheelScrollingEnabled(true);
			this.jScrollPanelID6.getViewport().add(jTextPanelID6, null);
			this.main_TabbedPaneKBimprovement.addTab(LocaleKBCT.GetString("STEP") + 7, null,
					this.jScrollPanelID7, LocaleKBCT.GetString("OptimizationMsg"));
			this.jScrollPanelID7.setWheelScrollingEnabled(true);
			this.jScrollPanelID7.getViewport().add(jTextPanelID7, null);
			this.main_TabbedPaneKBimprovement.addTab(LocaleKBCT.GetString("STEP") + 8, null,
					this.jScrollPanelID8, LocaleKBCT.GetString("OptimizationMsg"));
			this.jScrollPanelID8.setWheelScrollingEnabled(true);
			this.jScrollPanelID8.getViewport().add(jTextPanelID8, null);

			for (int n = 1; n < 9; n++) {
				this.main_TabbedPaneKBimprovement.setEnabledAt(n, false);
			}

		} else {
			this.getContentPane().add(jScrollPanel, BorderLayout.CENTER);
			this.jScrollPanel.setWheelScrollingEnabled(true);
			this.jScrollPanel.getViewport().add(jTextPanel, null);
		}
		this.pack();
		this.setSize(600, 300);
		this.setResizable(true);
		this.setLocation(this.dim.width/2, this.dim.height/2);
		//this.setLocation(this.dim.width - 600, this.dim.height - 350);
		this.Translate();
	}
	// ----------------------------------------------------------------------------
	public void Translate() {
		this.setTitle(LocaleKBCT.GetString("QuickStart"));
		this.jMenuClose.setText(LocaleKBCT.GetString("Exit"));
		String PageToShow = this.getHtmlPageToShow(this.PageView);
		try {
			File f = new File(PageToShow);
			// System.out.println(f.getParent());
			String ParentUrl = f.getParent();
			if (this.PageView.contains("TutExpert.html")) {
				jTextPanelSE0.setBackground(Color.LIGHT_GRAY);
				jTextPanelSE0.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Expert-Step0.html");
				jTextPanelSE0.setSelectedTextColor(Color.RED);
				jTextPanelSE0.setEditable(false);
				jTextPanelSE1.setBackground(Color.LIGHT_GRAY);
				jTextPanelSE1.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Expert-Step1.html");
				jTextPanelSE1.setSelectedTextColor(Color.RED);
				jTextPanelSE1.setEditable(false);
				jTextPanelSE2.setBackground(Color.LIGHT_GRAY);
				jTextPanelSE2.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Expert-Step2.html");
				jTextPanelSE2.setSelectedTextColor(Color.RED);
				jTextPanelSE2.setEditable(false);
				jTextPanelSE3.setBackground(Color.LIGHT_GRAY);
				jTextPanelSE3.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Expert-Step3.html");
				jTextPanelSE3.setSelectedTextColor(Color.RED);
				jTextPanelSE3.setEditable(false);
				jTextPanelSE4.setBackground(Color.LIGHT_GRAY);
				jTextPanelSE4.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Expert-Step4.html");
				jTextPanelSE4.setSelectedTextColor(Color.RED);
				jTextPanelSE4.setEditable(false);
				jTextPanelSE5.setBackground(Color.LIGHT_GRAY);
				jTextPanelSE5.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Expert-Step5.html");
				jTextPanelSE5.setSelectedTextColor(Color.RED);
				jTextPanelSE5.setEditable(false);
				jTextPanelSE6.setBackground(Color.LIGHT_GRAY);
				jTextPanelSE6.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Expert-Step6.html");
				jTextPanelSE6.setSelectedTextColor(Color.RED);
				jTextPanelSE6.setEditable(false);
			} else if (this.PageView.contains("TutData.html")) {
				jTextPanelSD0.setBackground(Color.LIGHT_GRAY);
				jTextPanelSD0.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Data-Step0.html");
				jTextPanelSD0.setSelectedTextColor(Color.RED);
				jTextPanelSD0.setEditable(false);
				jTextPanelSD1.setBackground(Color.LIGHT_GRAY);
				jTextPanelSD1.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Data-Step1.html");
				jTextPanelSD1.setSelectedTextColor(Color.RED);
				jTextPanelSD1.setEditable(false);
				jTextPanelSD2.setBackground(Color.LIGHT_GRAY);
				jTextPanelSD2.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Data-Step2.html");
				jTextPanelSD2.setSelectedTextColor(Color.RED);
				jTextPanelSD2.setEditable(false);
				jTextPanelSD3.setBackground(Color.LIGHT_GRAY);
				jTextPanelSD3.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Data-Step3.html");
				jTextPanelSD3.setSelectedTextColor(Color.RED);
				jTextPanelSD3.setEditable(false);
				jTextPanelSD4.setBackground(Color.LIGHT_GRAY);
				jTextPanelSD4.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Data-Step4.html");
				jTextPanelSD4.setSelectedTextColor(Color.RED);
				jTextPanelSD4.setEditable(false);
				jTextPanelSD5.setBackground(Color.LIGHT_GRAY);
				jTextPanelSD5.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Data-Step5.html");
				jTextPanelSD5.setSelectedTextColor(Color.RED);
				jTextPanelSD5.setEditable(false);
				jTextPanelSD6.setBackground(Color.LIGHT_GRAY);
				jTextPanelSD6.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Data-Step6.html");
				jTextPanelSD6.setSelectedTextColor(Color.RED);
				jTextPanelSD6.setEditable(false);
				jTextPanelSD7.setBackground(Color.LIGHT_GRAY);
				jTextPanelSD7.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Data-Step7.html");
				jTextPanelSD7.setSelectedTextColor(Color.RED);
				jTextPanelSD7.setEditable(false);
			} else if (this.PageView.contains("TutFingrams.html")) {
				jTextPanelFD0.setBackground(Color.LIGHT_GRAY);
				jTextPanelFD0.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Fingrams-Step0.html");
				jTextPanelFD0.setSelectedTextColor(Color.RED);
				jTextPanelFD0.setEditable(false);
				jTextPanelFD1.setBackground(Color.LIGHT_GRAY);
				jTextPanelFD1.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Fingrams-Step1.html");
				jTextPanelFD1.setSelectedTextColor(Color.RED);
				jTextPanelFD1.setEditable(false);
				jTextPanelFD2.setBackground(Color.LIGHT_GRAY);
				jTextPanelFD2.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Fingrams-Step2.html");
				jTextPanelFD2.setSelectedTextColor(Color.RED);
				jTextPanelFD2.setEditable(false);
				jTextPanelFD3.setBackground(Color.LIGHT_GRAY);
				jTextPanelFD3.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Fingrams-Step3.html");
				jTextPanelFD3.setSelectedTextColor(Color.RED);
				jTextPanelFD3.setEditable(false);
				jTextPanelFD4.setBackground(Color.LIGHT_GRAY);
				jTextPanelFD4.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Fingrams-Step4.html");
				jTextPanelFD4.setSelectedTextColor(Color.RED);
				jTextPanelFD4.setEditable(false);
				jTextPanelFD5.setBackground(Color.LIGHT_GRAY);
				jTextPanelFD5.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Fingrams-Step5.html");
				jTextPanelFD5.setSelectedTextColor(Color.RED);
				jTextPanelFD5.setEditable(false);
				jTextPanelFD6.setBackground(Color.LIGHT_GRAY);
				jTextPanelFD6.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Fingrams-Step6.html");
				jTextPanelFD6.setSelectedTextColor(Color.RED);
				jTextPanelFD6.setEditable(false);
			} else if (this.PageView.contains("TutImprovement.html")) {
				jTextPanelID0.setBackground(Color.LIGHT_GRAY);
				jTextPanelID0.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Improvement-Step0.html");
				jTextPanelID0.setSelectedTextColor(Color.RED);
				jTextPanelID0.setEditable(false);
				jTextPanelID1.setBackground(Color.LIGHT_GRAY);
				jTextPanelID1.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Improvement-Step1.html");
				jTextPanelID1.setSelectedTextColor(Color.RED);
				jTextPanelID1.setEditable(false);
				jTextPanelID2.setBackground(Color.LIGHT_GRAY);
				jTextPanelID2.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Improvement-Step2.html");
				jTextPanelID2.setSelectedTextColor(Color.RED);
				jTextPanelID2.setEditable(false);
				jTextPanelID3.setBackground(Color.LIGHT_GRAY);
				jTextPanelID3.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Improvement-Step3.html");
				jTextPanelID3.setSelectedTextColor(Color.RED);
				jTextPanelID3.setEditable(false);
				jTextPanelID4.setBackground(Color.LIGHT_GRAY);
				jTextPanelID4.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Improvement-Step4.html");
				jTextPanelID4.setSelectedTextColor(Color.RED);
				jTextPanelID4.setEditable(false);
				jTextPanelID5.setBackground(Color.LIGHT_GRAY);
				jTextPanelID5.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Improvement-Step5.html");
				jTextPanelID5.setSelectedTextColor(Color.RED);
				jTextPanelID5.setEditable(false);
				jTextPanelID6.setBackground(Color.LIGHT_GRAY);
				jTextPanelID6.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Improvement-Step6.html");
				jTextPanelID6.setSelectedTextColor(Color.RED);
				jTextPanelID6.setEditable(false);
				jTextPanelID7.setBackground(Color.LIGHT_GRAY);
				jTextPanelID7.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Improvement-Step7.html");
				jTextPanelID7.setSelectedTextColor(Color.RED);
				jTextPanelID7.setEditable(false);
				jTextPanelID8.setBackground(Color.LIGHT_GRAY);
				jTextPanelID8.setPage(ParentUrl
						+ System.getProperty("file.separator")
						+ "Improvement-Step8.html");
				jTextPanelID8.setSelectedTextColor(Color.RED);
				jTextPanelID8.setEditable(false);
                ////////////////
				jTextPanelQuality.setBackground(Color.LIGHT_GRAY);
				this.writeQualityFile(0,0,0);
		        File temp= JKBCTFrame.BuildFile("Tutorial-Quality.html");
				if (LocaleKBCT.isWindowsPlatform())
			        jTextPanelQuality.setPage("file:\\"+ temp.getAbsolutePath());
				else
			        jTextPanelQuality.setPage("file://"+ temp.getAbsolutePath());

				jTextPanelQuality.setSelectedTextColor(Color.RED);
				jTextPanelQuality.setEditable(false);
			} else if ((!this.PageView.contains("TutExpert.html"))
					&& !(this.PageView.contains("TutData.html"))
                    && !(this.PageView.contains("TutFingrams.html"))					
                    && !(this.PageView.contains("TutImprovement.html"))) {
				// Mirar
				// http://download.oracle.com/javase/6/docs/api/javax/swing/text/html/HTMLEditorKit.html
				// http://download.oracle.com/javase/6/docs/api/javax/swing/JTextPane.html#setEditorKit(javax.swing.text.EditorKit)
				jTextPanel.setBackground(Color.LIGHT_GRAY);
				jTextPanel.setPage(PageToShow);
				jTextPanel.setSelectedTextColor(Color.RED);
				jTextPanel.setEditable(false);
			}
		} catch (java.net.UnknownHostException e) {
			this.PageView = "Tutorial-Intro.html";
			this.dispose();
			JKBCTFrame.RemoveTranslatable(this);
		} catch (Exception e) {
			//e.printStackTrace();
			MessageKBCT.Error(this, LocaleKBCT.GetString("Error"),
					"Error in JTutorialFrame: Translate: " + e);
		}
		this.repaint();
	}
	// ------------------------------------------------------------------------------
	public void jMenuTutExpert_actionPerformed() {
		this.this_windowClose(true);
		MainKBCT.setJT(new JTutorialFrame("TutExpert.html"));
		MainKBCT.getJT().setSize(600, 390);
		MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 440);
		MainKBCT.getJT().setVisible(true);
	}
	// ------------------------------------------------------------------------------
	public void jMenuTutData_actionPerformed() {
		this.this_windowClose(true);
		MainKBCT.setJT(new JTutorialFrame("TutData.html"));
		MainKBCT.getJT().setSize(600, 410);
		MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 460);
		MainKBCT.getJT().setVisible(true);
	}
	// ------------------------------------------------------------------------------
	public void jMenuTutFingrams_actionPerformed() {
		this.this_windowClose(true);
		MainKBCT.setJT(new JTutorialFrame("TutFingrams.html"));
		MainKBCT.getJT().setSize(600, 390);
		MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 440);
		MainKBCT.getJT().setVisible(true);
	}
	// ------------------------------------------------------------------------------
	public void jMenuTutImprovement_actionPerformed() {
		this.this_windowClose(true);
		MainKBCT.setJT(new JTutorialFrame("TutImprovement.html"));
		MainKBCT.getJT().setSize(600, 430);
		MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 480);
		MainKBCT.getJT().setVisible(true);
	}
	// ------------------------------------------------------------------------------
	public void this_windowClose(boolean update) {
		MainKBCT.getJMF().jMenuClose_actionPerformed();
		this.dispose();
		JKBCTFrame.RemoveTranslatable(this);
		if (!update) {
			MainKBCT.getConfig().SetTutorialFlag(false);
			MainKBCT.getConfig().SetKBCTFilePath(this.GUAJEpath);
		}
	}
	// ------------------------------------------------------------------------------
	public void this_windowClosing() {
		this.this_windowClose(false);
	}
	// ----------------------------------------------------------------------------
	/**
	 * Jump to hyperlink reference.
	 */
	public void this_hyperlinkUpdate(HyperlinkEvent e) {
		// System.out.println("link");
		if (e.getEventType().toString().equals("ACTIVATED")) {
			try {
				String url = e.getURL().toString();
				if ((url.equals("http://www.inra.fr/Internet/Departements/MIA/M/fispro/"))
						|| (url.equals("http://www.mathworks.com"))
						|| (url.equals("http://www.imse.cnm.es/Xfuzzy"))) {
					MessageKBCT.Information(this, LocaleKBCT.GetString("ExternalPage"));
				} else {
					// System.out.println("url="+url);
					if (url.endsWith("TutExpert.html")) {
						// System.out.println("EXPERT TUTORIAL");
						this.jMenuTutExpert_actionPerformed();
					} else if (url.endsWith("TutData.html")) {
						// System.out.println("DATA TUTORIAL");
						this.jMenuTutData_actionPerformed();
					} else if (url.endsWith("TutFingrams.html")) {
						// System.out.println("FINGRAMS TUTORIAL");
						this.jMenuTutFingrams_actionPerformed();
					} else if (url.endsWith("TutImprovement.html")) {
						// System.out.println("IMPROVEMENT TUTORIAL");
						this.jMenuTutImprovement_actionPerformed();

					} else {
						/******************* TUTORIAL EXPERT *********************/
						if (url.endsWith("DoES0.html")) {
							System.out.println("START");
							MainKBCT.getJMF().jMenuClose_actionPerformed();
							this.main_TabbedPaneExpert.setEnabledAt(1, true);
							this.main_TabbedPaneExpert.setSelectedIndex(1);
							for (int n = 2; n < 7; n++) {
								this.main_TabbedPaneExpert.setEnabledAt(n,false);
							}
							MainKBCT.getJT().setSize(600, 340);
							MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 390);
						} else if (url.endsWith("DoES1.html")) {
							System.out.println("  -> Build an empty KB");
							buildIKBandInicialize("EXPERT");

						} else if (url.endsWith("NextES1.html")) {
							if (MainKBCT.getJMF().jef == null) {
								MessageKBCT.Warning(this,
												LocaleKBCT.GetString("QuickStart")
														+ LocaleKBCT.GetString("Expert").toUpperCase()
														+ " - "
														+ LocaleKBCT.GetString("STEP")
														+ 1,
												LocaleKBCT.GetString("WarningTutExpertS1"));
							} else {
								System.out.println("Go to STEP2");
								this.main_TabbedPaneExpert.setEnabledAt(1,false);
								this.main_TabbedPaneExpert.setEnabledAt(2, true);
								this.main_TabbedPaneExpert.setSelectedIndex(2);
								MainKBCT.getJT().setSize(600, 450);
								MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 500);
							}
						} else if (url.endsWith("HelpES1.html")) {
							System.out.println("Display HELP about STEP1");
							MainKBCT.setJB(new JBeginnerFrame("main/MainMenuKB.html#New"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackES2.html")) {
							System.out.println("Go to STEP1");
							this.main_TabbedPaneExpert.setEnabledAt(2, false);
							this.main_TabbedPaneExpert.setEnabledAt(1, true);
							this.main_TabbedPaneExpert.setSelectedIndex(1);
							MainKBCT.getJT().setSize(600, 340);
							MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 390);

						} else if (url.endsWith("DoES2.html")) {
							System.out.println("  -> Create a new input");
							MainKBCT.getJMF().jef.jMenuNewInput_actionPerformed();

						} else if (url.endsWith("NextES2.html")) {
							if (MainKBCT.getJMF().jef.Temp_kbct.GetNbInputs() == 0) {
								MessageKBCT.Warning(this,
												LocaleKBCT.GetString("QuickStart")
														+ LocaleKBCT.GetString("Expert").toUpperCase()
														+ " - "
														+ LocaleKBCT.GetString("STEP")
														+ 2,
												LocaleKBCT.GetString("WarningTutExpertS2"));
							} else {
								System.out.println("Go to STEP3");
								this.main_TabbedPaneExpert.setEnabledAt(2,false);
								this.main_TabbedPaneExpert.setEnabledAt(3, true);
								this.main_TabbedPaneExpert.setSelectedIndex(3);
								MainKBCT.getJT().setSize(600, 430);
								MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 480);
							}
						} else if (url.endsWith("HelpES2.html")) {
							System.out.println("Display HELP about STEP2");
							MainKBCT.setJB(new JBeginnerFrame("expert/ExpertMenuInputs.html#New"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackES3.html")) {
							System.out.println("Go to STEP2");
							this.main_TabbedPaneExpert.setEnabledAt(3, false);
							this.main_TabbedPaneExpert.setEnabledAt(2, true);
							this.main_TabbedPaneExpert.setSelectedIndex(2);
							MainKBCT.getJT().setSize(600, 450);
							MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 500);

						} else if (url.endsWith("DoES3.html")) {
							if (MainKBCT.getJMF().jef.Temp_kbct.GetNbOutputs() > 0) {
								MessageKBCT.Warning(this,
												LocaleKBCT.GetString("QuickStart")
														+ LocaleKBCT.GetString("Expert").toUpperCase()
														+ " - "
														+ LocaleKBCT.GetString("STEP")
														+ 3,
												LocaleKBCT.GetString("WarningTutExpertS3a"));
							} else {
								System.out.println("  -> Create a new output");
								MainKBCT.getJMF().jef.jMenuNewOutput_actionPerformed();
							}
							if ((this.jof == null) || (!this.jof.isVisible())) {
								this.jof = new JOutputFrame(MainKBCT.getJMF().jef, MainKBCT.getJMF().jef.Temp_kbct, 0);
								this.jof.Show();
							}

						} else if (url.endsWith("NextES3.html")) {
							if (MainKBCT.getJMF().jef.Temp_kbct.GetNbOutputs() == 0) {
								MessageKBCT.Warning(this,
												LocaleKBCT.GetString("QuickStart")
														+ LocaleKBCT.GetString("Expert").toUpperCase()
														+ " - "
														+ LocaleKBCT.GetString("STEP")
														+ 3,
												LocaleKBCT.GetString("WarningTutExpertS3b"));
							} else {
								System.out.println("Go to STEP4");
								this.main_TabbedPaneExpert.setEnabledAt(3,false);
								this.main_TabbedPaneExpert.setEnabledAt(4, true);
								this.main_TabbedPaneExpert.setSelectedIndex(4);
								MainKBCT.getJT().setSize(600, 430);
								MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 480);
							}
						} else if (url.endsWith("HelpES3.html")) {
							System.out.println("Display HELP about STEP3");
							MainKBCT.setJB(new JBeginnerFrame("expert/ExpertMenuOutputs.html#New"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackES4.html")) {
							System.out.println("Go to STEP3");
							this.main_TabbedPaneExpert.setEnabledAt(4, false);
							this.main_TabbedPaneExpert.setEnabledAt(3, true);
							this.main_TabbedPaneExpert.setSelectedIndex(3);
							MainKBCT.getJT().setSize(600, 430);
							MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 480);

						} else if (url.endsWith("DoES4.html")) {
							System.out.println("  -> Set fuzzy operators");
							MainKBCT.getJMF().jef.jMenuSetFISoptions_actionPerformed();

						} else if (url.endsWith("NextES4.html")) {
							System.out.println("Go to STEP5");
							this.main_TabbedPaneExpert.setEnabledAt(4, false);
							this.main_TabbedPaneExpert.setEnabledAt(5, true);
							this.main_TabbedPaneExpert.setSelectedIndex(5);
							MainKBCT.getJT().setSize(600, 400);
							MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 450);

						} else if (url.endsWith("HelpES4.html")) {
							System.out.println("Display HELP about STEP4");
							MainKBCT.setJB(new JBeginnerFrame("main/MainButtons.html#SetFisOperators"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackES5.html")) {
							System.out.println("Go to STEP4");
							this.main_TabbedPaneExpert.setEnabledAt(5, false);
							this.main_TabbedPaneExpert.setEnabledAt(4, true);
							this.main_TabbedPaneExpert.setSelectedIndex(4);
							MainKBCT.getJT().setSize(600, 430);
							MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 480);

						} else if (url.endsWith("DoES5.html")) {
							System.out.println("  -> Define rules");
							MainKBCT.getJMF().jef.jMenuNewRule_actionPerformed();

						} else if (url.endsWith("HelpES5.html")) {
							System.out.println("Display HELP about STEP5");
							MainKBCT.setJB(new JBeginnerFrame("expert/ExpertMenuRules.html#New"));
							MainKBCT.getJB().setVisible(true);
						} else if (url.endsWith("NextES5.html")) {
							if (MainKBCT.getJMF().jef.Temp_kbct.GetNbRules() == 0) {
								MessageKBCT.Warning(this,
												LocaleKBCT.GetString("QuickStart")
														+ LocaleKBCT.GetString("Expert").toUpperCase()
														+ " - "
														+ LocaleKBCT.GetString("STEP")
														+ 5,
												LocaleKBCT.GetString("WarningTutExpertS5"));
							} else {
								System.out.println("Go to STEP6");
								this.main_TabbedPaneExpert.setEnabledAt(5,false);
								this.main_TabbedPaneExpert.setEnabledAt(6, true);
								this.main_TabbedPaneExpert.setSelectedIndex(6);
								MainKBCT.getJT().setSize(600, 380);
								MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 430);
							}
						} else if (url.endsWith("BackES6.html")) {
							if (MainKBCT.getJMF().jef.jif != null)
								MainKBCT.getJMF().jef.jif.dispose();

							System.out.println("Go to STEP5");
							this.main_TabbedPaneExpert.setEnabledAt(6, false);
							this.main_TabbedPaneExpert.setEnabledAt(5, true);
							this.main_TabbedPaneExpert.setSelectedIndex(5);
							MainKBCT.getJT().setSize(600, 400);
							MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 450);

						} else if (url.endsWith("DoES6.html")) {
							System.out.println("  -> Inference");
							MainKBCT.getJMF().jef.jButtonInfer();

						} else if (url.endsWith("HelpES6.html")) {
							System.out.println("Display HELP about STEP6");
							MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonInference.html"));
							MainKBCT.getJB().setVisible(true);
						}

						/******************* TUTORIAL DATA *********************/
						else if (url.endsWith("DoDS0.html")) {
							System.out.println("START");
							// System.out.println("IKB1: "+MainKBCT.getJMF().IKBFile);
							// System.out.println("DF1: "+MainKBCT.getJMF().KBDataFile);
							// System.out.println("KB1: "+MainKBCT.getJMF().KBExpertFile);
							MainKBCT.getJMF().jMenuClose_actionPerformed();
							// System.out.println("IKB2: "+MainKBCT.getJMF().IKBFile);
							// System.out.println("DF2: "+MainKBCT.getJMF().KBDataFile);
							// System.out.println("KB2: "+MainKBCT.getJMF().KBExpertFile);
							this.cleanDir("DATA");
							this.main_TabbedPaneData.setEnabledAt(1, true);
							this.main_TabbedPaneData.setSelectedIndex(1);
							for (int n = 2; n < 8; n++) {
								this.main_TabbedPaneData.setEnabledAt(n, false);
							}
						} else if (url.endsWith("DoDS1.html")) {
							// Data are split into 80% training and 20% test
							// New data inside the new generated DATA0 dir
							System.out.println("  -> Build a new KB");
							buildIKBandInicialize("DATA");

						} else if (url.endsWith("NextDS1.html")) {
							if ((MainKBCT.getJMF().jef == null)
									|| (MainKBCT.getJMF().jef.getJExtDataFile() == null)) {
								MessageKBCT.Warning(this, LocaleKBCT.GetString("QuickStart")
										+ LocaleKBCT.GetString("Data").toUpperCase()
										+ " - " + LocaleKBCT.GetString("STEP") + 1,
										LocaleKBCT.GetString("WarningTutDataS1"));
							} else {
								System.out.println("Go to STEP2");
								this.main_TabbedPaneData.setEnabledAt(1, false);
								this.main_TabbedPaneData.setEnabledAt(2, true);
								this.main_TabbedPaneData.setSelectedIndex(2);
							}
						} else if (url.endsWith("HelpDS1.html")) {
							System.out.println("Display HELP about STEP1");
							MainKBCT.setJB(new JBeginnerFrame("main/MainMenuKB.html#New"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackDS2.html")) {
							System.out.println("Go to STEP1");
							this.main_TabbedPaneData.setEnabledAt(2, false);
							this.main_TabbedPaneData.setEnabledAt(1, true);
							this.main_TabbedPaneData.setSelectedIndex(1);

						} else if (url.endsWith("DoDS2.html")) {
							// Visualizing data (table and histograms)
							System.out.println("  -> Data Table");
							MainKBCT.getJMF().jMenuDataTable_actionPerformed();
							System.out.println("  -> Data View");
							MainKBCT.getJMF().jMenuDataView_actionPerformed();

						} else if (url.endsWith("NextDS2.html")) {
							System.out.println("Go to STEP3");
							this.main_TabbedPaneData.setEnabledAt(2, false);
							this.main_TabbedPaneData.setEnabledAt(3, true);
							this.main_TabbedPaneData.setSelectedIndex(3);

						} else if (url.endsWith("HelpDS2.html")) {
							System.out.println("Display HELP about STEP2");
							MainKBCT.setJB(new JBeginnerFrame("main/MainMenuData.html#View"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackDS3.html")) {
							System.out.println("Go to STEP2");
							this.main_TabbedPaneData.setEnabledAt(3, false);
							this.main_TabbedPaneData.setEnabledAt(2, true);
							this.main_TabbedPaneData.setSelectedIndex(2);

						} else if (url.endsWith("DoDS3.html")) {
                            this.generatePartitions("DATA");

						} else if (url.endsWith("NextDS3.html")) {
							System.out.println("Go to STEP4");
							this.main_TabbedPaneData.setEnabledAt(3, false);
							this.main_TabbedPaneData.setEnabledAt(4, true);
							this.main_TabbedPaneData.setSelectedIndex(4);

						} else if (url.endsWith("HelpDS3.html")) {
							System.out.println("Display HELP about STEP3");
							MainKBCT.setJB(new JBeginnerFrame("main/MainMenuData.html#InducePartitions"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackDS4.html")) {
							System.out.println("Go to STEP3");
							this.main_TabbedPaneData.setEnabledAt(4, false);
							this.main_TabbedPaneData.setEnabledAt(3, true);
							this.main_TabbedPaneData.setSelectedIndex(3);

						} else if (url.endsWith("DoDS4.html")) {
							System.out.println("  -> Set fuzzy operators");
							MainKBCT.getJMF().jef.jMenuSetFISoptions_actionPerformed();

						} else if (url.endsWith("NextDS4.html")) {
							System.out.println("Go to STEP5");
							this.main_TabbedPaneData.setEnabledAt(4, false);
							this.main_TabbedPaneData.setEnabledAt(5, true);
							this.main_TabbedPaneData.setSelectedIndex(5);

						} else if (url.endsWith("HelpDS4.html")) {
							System.out.println("Display HELP about STEP4");
							MainKBCT.setJB(new JBeginnerFrame("main/MainButtons.html#SetFisOperators"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackDS5.html")) {
							System.out.println("Go to STEP4");
							this.main_TabbedPaneData.setEnabledAt(5, false);
							this.main_TabbedPaneData.setEnabledAt(4, true);
							this.main_TabbedPaneData.setSelectedIndex(4);

						} else if (url.endsWith("DoDS5.html")) {
							this.generateRules("WM");

						} else if (url.endsWith("NextDS5.html")) {
							if (MainKBCT.getJMF().jef.Temp_kbct.GetNbRules() == 0) {
								MessageKBCT.Warning(this, LocaleKBCT.GetString("QuickStart")
										+ LocaleKBCT.GetString("Data").toUpperCase()
										+ " - " + LocaleKBCT.GetString("STEP") + 5,
										LocaleKBCT.GetString("WarningTutDataS5"));
							} else {
								System.out.println("Go to STEP6");
								this.main_TabbedPaneData.setEnabledAt(5, false);
								this.main_TabbedPaneData.setEnabledAt(6, true);
								this.main_TabbedPaneData.setSelectedIndex(6);
							}
						} else if (url.endsWith("HelpDS5.html")) {
							System.out.println("Display HELP about STEP5");
							MainKBCT.setJB(new JBeginnerFrame("main/MainMenuData.html#InduceRules"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackDS6.html")) {
							System.out.println("Go to STEP5");
							this.main_TabbedPaneData.setEnabledAt(6, false);
							this.main_TabbedPaneData.setEnabledAt(5, true);
							this.main_TabbedPaneData.setSelectedIndex(5);

						} else if (url.endsWith("DoDS6.html")) {
							// Open Inference Window
							System.out.println("  -> Inference");
							MainKBCT.getJMF().jef.jButtonInfer();

						} else if (url.endsWith("NextDS6.html")) {
							System.out.println("Go to STEP7");
							this.main_TabbedPaneData.setEnabledAt(6, false);
							this.main_TabbedPaneData.setEnabledAt(7, true);
							this.main_TabbedPaneData.setSelectedIndex(7);

						} else if (url.endsWith("HelpDS6.html")) {
							System.out.println("Display HELP about STEP6");
							MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonInference.html"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackDS7.html")) {
							System.out.println("Go to STEP6");
							this.main_TabbedPaneData.setEnabledAt(7, false);
							this.main_TabbedPaneData.setEnabledAt(6, true);
							this.main_TabbedPaneData.setSelectedIndex(6);

						} else if (url.endsWith("DoDS7.html")) {
							// Quality Assessment
							System.out.println("  -> Evaluate (Accuracy & Interpretability)");
							MainKBCT.getJMF().jef.jButtonQuality_actionPerformed(true,true);

						} else if (url.endsWith("HelpDS7.html")) {
							System.out.println("Display HELP about STEP7");
							MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonQuality.html"));
							MainKBCT.getJB().setVisible(true);
						}
						/******************* TUTORIAL FINGRAMS *********************/
						else if (url.endsWith("DoFS0.html")) {
							System.out.println("START");
							// System.out.println("IKB1: "+MainKBCT.getJMF().IKBFile);
							// System.out.println("DF1: "+MainKBCT.getJMF().KBDataFile);
							// System.out.println("KB1: "+MainKBCT.getJMF().KBExpertFile);
							MainKBCT.getJMF().jMenuClose_actionPerformed();
							// System.out.println("IKB2: "+MainKBCT.getJMF().IKBFile);
							// System.out.println("DF2: "+MainKBCT.getJMF().KBDataFile);
							// System.out.println("KB2: "+MainKBCT.getJMF().KBExpertFile);
							this.cleanDir("FINGRAMS");
							this.main_TabbedPaneFingrams.setEnabledAt(1, true);
							this.main_TabbedPaneFingrams.setSelectedIndex(1);
							for (int n = 2; n < 7; n++) {
								this.main_TabbedPaneFingrams.setEnabledAt(n, false);
							}
						} else if (url.endsWith("DoFS1.html")) {
							// Available Data are split into 80% training and 20% test
							// New data inside the new generated DATA0 dir
							System.out.println("  -> Build a new KB");
							buildIKBandInicialize("FINGRAMS");

						} else if (url.endsWith("NextFS1.html")) {
							if ((MainKBCT.getJMF().jef == null)
									|| (MainKBCT.getJMF().jef.getJExtDataFile() == null)) {
								MessageKBCT.Warning(this, LocaleKBCT.GetString("QuickStart")
										+ LocaleKBCT.GetString("Fingrams").toUpperCase()
										+ " - " + LocaleKBCT.GetString("STEP") + 1,
										LocaleKBCT.GetString("WarningTutDataS1"));
							} else {
								System.out.println("Go to STEP2");
								this.main_TabbedPaneFingrams.setEnabledAt(1,false);
								this.main_TabbedPaneFingrams.setEnabledAt(2,true);
								this.main_TabbedPaneFingrams.setSelectedIndex(2);
							}
						} else if (url.endsWith("HelpFS1.html")) {
							System.out.println("Display HELP about STEP1");
							MainKBCT.setJB(new JBeginnerFrame("main/MainMenuKB.html#New"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackFS2.html")) {
							System.out.println("Go to STEP1");
							this.main_TabbedPaneFingrams.setEnabledAt(2, false);
							this.main_TabbedPaneFingrams.setEnabledAt(1, true);
							this.main_TabbedPaneFingrams.setSelectedIndex(1);

						} else if (url.endsWith("DoFS2.html")) {
                            this.generatePartitions("FINGRAMS");
							
						} else if (url.endsWith("NextFS2.html")) {
							System.out.println("Go to STEP3");
							this.main_TabbedPaneFingrams.setEnabledAt(2, false);
							this.main_TabbedPaneFingrams.setEnabledAt(3, true);
							this.main_TabbedPaneFingrams.setSelectedIndex(3);

						} else if (url.endsWith("HelpFS2.html")) {
							System.out.println("Display HELP about STEP2");
							MainKBCT.setJB(new JBeginnerFrame("main/MainMenuData.html#InducePartitions"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackFS3.html")) {
							System.out.println("Go to STEP2");
							this.main_TabbedPaneFingrams.setEnabledAt(3, false);
							this.main_TabbedPaneFingrams.setEnabledAt(2, true);
							this.main_TabbedPaneFingrams.setSelectedIndex(2);

						} else if (url.endsWith("DoFS3.html")) {
							System.out.println("  -> Set fuzzy operators");
							MainKBCT.getJMF().jef.jMenuSetFISoptions_actionPerformed();

						} else if (url.endsWith("NextFS3.html")) {
							System.out.println("Go to STEP4");
							this.main_TabbedPaneFingrams.setEnabledAt(3, false);
							this.main_TabbedPaneFingrams.setEnabledAt(4, true);
							this.main_TabbedPaneFingrams.setSelectedIndex(4);

						} else if (url.endsWith("HelpFS3.html")) {
							System.out.println("Display HELP about STEP3");
							MainKBCT.setJB(new JBeginnerFrame("main/MainButtons.html#SetFisOperators"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackFS4.html")) {
							System.out.println("Go to STEP3");
							this.main_TabbedPaneFingrams.setEnabledAt(4, false);
							this.main_TabbedPaneFingrams.setEnabledAt(3, true);
							this.main_TabbedPaneFingrams.setSelectedIndex(3);

						} else if (url.endsWith("DoFS4.html")) {
							//System.out.println("  -> Generate rules (FDT)");
							this.generateRules("FDT");

						} else if (url.endsWith("NextFS4.html")) {
							if (MainKBCT.getJMF().jef.Temp_kbct.GetNbRules() == 0) {
								MessageKBCT.Warning(this, LocaleKBCT.GetString("QuickStart")
										+ LocaleKBCT.GetString("Fingrams").toUpperCase()
										+ " - "	+ LocaleKBCT.GetString("STEP") + 4,
										LocaleKBCT.GetString("WarningTutFingramsS4"));
							} else {
								System.out.println("Go to STEP5");
								this.main_TabbedPaneFingrams.setEnabledAt(4, false);
								this.main_TabbedPaneFingrams.setEnabledAt(5, true);
								this.main_TabbedPaneFingrams.setSelectedIndex(5);
							}

						} else if (url.endsWith("HelpFS4.html")) {
							System.out.println("Display HELP about STEP4");
							MainKBCT.setJB(new JBeginnerFrame("main/MainMenuData.html#InduceRules"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackFS5.html")) {
							System.out.println("Go to STEP4");
							this.main_TabbedPaneFingrams.setEnabledAt(5, false);
							this.main_TabbedPaneFingrams.setEnabledAt(4, true);
							this.main_TabbedPaneFingrams.setSelectedIndex(4);

						} else if (url.endsWith("DoFS5.html")) {
							// Generate Fingrams
							System.out.println("  -> Generate Fingrams");
	                        MainKBCT.getConfig().SetFINGRAMSautomatic(true);
	                        MainKBCT.getConfig().SetFingramsSelectedSample(1);
	                        MainKBCT.getConfig().SetFingramsLayout(LocaleKBCT.DefaultFingramsLayout());
	                        MainKBCT.getConfig().SetFingramsMetric(LocaleKBCT.DefaultFingramsMetric());
	                  	    MainKBCT.getConfig().SetPathFinderThreshold(LocaleKBCT.DefaultPathFinderThreshold());        	  
	                	    MainKBCT.getConfig().SetPathFinderParQ(LocaleKBCT.DefaultPathFinderParQ());        	  
	                	    MainKBCT.getConfig().SetGoodnessHighThreshold(LocaleKBCT.DefaultGoodnessHighThreshold());        	  
	                	    MainKBCT.getConfig().SetGoodnessLowThreshold(LocaleKBCT.DefaultGoodnessLowThreshold());        	  
							MainKBCT.getJMF().jef.jButtonFingrams_actionPerformed(true,null);
							
						} else if (url.endsWith("NextFS5.html")) {
							System.out.println("Go to STEP6");
							this.main_TabbedPaneFingrams.setEnabledAt(5, false);
							this.main_TabbedPaneFingrams.setEnabledAt(6, true);
							this.main_TabbedPaneFingrams.setSelectedIndex(6);

						} else if (url.endsWith("HelpFS5.html")) {
							System.out.println("Display HELP about STEP5");
							MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonFingrams.html"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackFS6.html")) {
							System.out.println("Go to STEP5");
							this.main_TabbedPaneFingrams.setEnabledAt(6, false);
							this.main_TabbedPaneFingrams.setEnabledAt(5, true);
							this.main_TabbedPaneFingrams.setSelectedIndex(5);

						} else if (url.endsWith("DoFS6.html")) {
							// Visualize Fingrams
							System.out.println("  -> Visualize Fingrams");
							MainKBCT.getJMF().jef.displaySelectedFingrams();
							
						} else if (url.endsWith("HelpFS6.html")) {
							System.out.println("Display HELP about STEP6");
							MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonFingrams.html#Apply"));
							MainKBCT.getJB().setVisible(true);
						}
						/******************* TUTORIAL IMPROVEMENT *********************/
						else if (url.endsWith("DoIS0.html")) {
							System.out.println("START");
							// System.out.println("IKB1: "+MainKBCT.getJMF().IKBFile);
							// System.out.println("DF1: "+MainKBCT.getJMF().KBDataFile);
							// System.out.println("KB1: "+MainKBCT.getJMF().KBExpertFile);
							MainKBCT.getJMF().jMenuClose_actionPerformed();
							// System.out.println("IKB2: "+MainKBCT.getJMF().IKBFile);
							// System.out.println("DF2: "+MainKBCT.getJMF().KBDataFile);
							// System.out.println("KB2: "+MainKBCT.getJMF().KBExpertFile);
							this.cleanDir("IMPROVEMENT");
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(1, true);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setSelectedIndex(1);
							for (int n = 2; n < 8; n++) {
								MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(n, false);
							}
							MainKBCT.getJT().setSize(600, 490);
							MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 540);
							MainKBCT.getJT().jScrollPanelQuality.setVisible(false);
						} else if (url.endsWith("DoIS1.html")) {
							// Available Data are split into 75% training and 25% test
							// New data inside the new generated DATA0 dir
							System.out.println("  -> Build a new KB");
							buildIKBandInicialize("IMPROVEMENT");

						} else if (url.endsWith("NextIS1.html")) {
							if ((MainKBCT.getJMF().jef == null)
									|| (MainKBCT.getJMF().jef.getJExtDataFile() == null)) {
								MessageKBCT.Warning(this, LocaleKBCT.GetString("QuickStart")
										+ LocaleKBCT.GetString("Improvement").toUpperCase()
										+ " - " + LocaleKBCT.GetString("STEP") + 1,
										LocaleKBCT.GetString("WarningTutDataS1"));
							} else {
								System.out.println("Go to STEP2");
								MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(1,false);
								MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(2,true);
								MainKBCT.getJT().main_TabbedPaneKBimprovement.setSelectedIndex(2);
								MainKBCT.getJT().setSize(600, 450);
								MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 500);
							}
						} else if (url.endsWith("HelpIS1.html")) {
							System.out.println("Display HELP about STEP1");
							MainKBCT.setJB(new JBeginnerFrame("main/MainMenuKB.html#New"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackIS2.html")) {
							System.out.println("Go to STEP1");
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(2, false);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(1, true);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setSelectedIndex(1);

						} else if (url.endsWith("DoIS2.html")) {
                            this.generatePartitions("IMPROVEMENT");
							
						} else if (url.endsWith("NextIS2.html")) {
							System.out.println("Go to STEP3");
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(2, false);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(3, true);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setSelectedIndex(3);

						} else if (url.endsWith("HelpIS2.html")) {
							System.out.println("Display HELP about STEP2");
							MainKBCT.setJB(new JBeginnerFrame("main/MainMenuData.html#InducePartitions"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackIS3.html")) {
							System.out.println("Go to STEP2");
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(3, false);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(2, true);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setSelectedIndex(2);

						} else if (url.endsWith("DoIS3.html")) {
							System.out.println("  -> Set fuzzy operators");
							MainKBCT.getJMF().jef.jMenuSetFISoptions_actionPerformed();

						} else if (url.endsWith("NextIS3.html")) {
							System.out.println("Go to STEP4");
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(3, false);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(4, true);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setSelectedIndex(4);
							MainKBCT.getJT().setSize(600, 390);
							MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 440);

						} else if (url.endsWith("HelpIS3.html")) {
							System.out.println("Display HELP about STEP3");
							MainKBCT.setJB(new JBeginnerFrame("main/MainButtons.html#SetFisOperators"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackIS4.html")) {
							System.out.println("Go to STEP3");
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(4, false);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(3, true);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setSelectedIndex(3);
							MainKBCT.getJT().jScrollPanelQuality.setVisible(false);
							MainKBCT.getJT().setSize(600, 300);
							MainKBCT.getJT().setLocation(this.dim.width - 600, this.dim.height - 350);

						} else if (url.endsWith("DoIS4.html")) {
							//System.out.println("  -> Generate rules (FDT)");
							this.generateRules("FDT");
							MainKBCT.getJT().updateQuality(0);
							MainKBCT.getJT().writeQualityFile(0,1,4);
							MainKBCT.getJT().refreshQualityPanel();
							MainKBCT.getJT().jScrollPanelQuality.setVisible(true);
							MainKBCT.getJT().setSize(800, 700);
							MainKBCT.getJT().setLocation(this.dim.width - 800, this.dim.height - 750);

						} else if (url.endsWith("NextIS4.html")) {
							if (MainKBCT.getJMF().jef.Temp_kbct.GetNbRules() == 0) {
								MessageKBCT.Warning(this, LocaleKBCT.GetString("QuickStart")
										+ LocaleKBCT.GetString("Improvement").toUpperCase()
										+ " - "	+ LocaleKBCT.GetString("STEP") + 4,
										LocaleKBCT.GetString("WarningTutFingramsS4"));
							} else {
								System.out.println("Go to STEP5");
								MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(4, false);
								MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(5, true);
								MainKBCT.getJT().main_TabbedPaneKBimprovement.setSelectedIndex(5);
							    MainKBCT.getJT().refreshQualityPanel();
							}

						} else if (url.endsWith("HelpIS4.html")) {
							System.out.println("Display HELP about STEP4");
							MainKBCT.setJB(new JBeginnerFrame("main/MainMenuData.html#InduceRules"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackIS5.html")) {
							System.out.println("Go to STEP4");
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(5, false);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(4, true);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setSelectedIndex(4);
							MainKBCT.getJT().updateQuality(0);
							MainKBCT.getJT().writeQualityFile(0,1,4);
							MainKBCT.getJT().refreshQualityPanel();

						} else if (url.endsWith("DoIS5.html")) {
							// Generate, Visualize, and Analyze Fingrams
							System.out.println("  -> Generate, Visualize, and Analyze Fingrams");
	                        MainKBCT.getConfig().SetFINGRAMSautomatic(true);
	                        MainKBCT.getConfig().SetFingramsSelectedSample(1);
	                        MainKBCT.getConfig().SetFingramsLayout(LocaleKBCT.DefaultFingramsLayout());
	                        MainKBCT.getConfig().SetFingramsMetric(LocaleKBCT.DefaultFingramsMetric());
	                  	    MainKBCT.getConfig().SetPathFinderThreshold(LocaleKBCT.DefaultPathFinderThreshold());        	  
	                	    MainKBCT.getConfig().SetPathFinderParQ(LocaleKBCT.DefaultPathFinderParQ());        	  
	                	    MainKBCT.getConfig().SetGoodnessHighThreshold(LocaleKBCT.DefaultGoodnessHighThreshold());        	  
	                	    MainKBCT.getConfig().SetGoodnessLowThreshold(LocaleKBCT.DefaultGoodnessLowThreshold());        	  
							MainKBCT.getJMF().jef.jButtonFingrams_actionPerformed(true,null);
							MainKBCT.getJMF().jef.displaySelectedFingrams();
							
						} else if (url.endsWith("NextIS5.html")) {
							System.out.println("Go to STEP6");
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(5, false);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(6, true);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setSelectedIndex(6);
							MainKBCT.getJT().updateQuality(1);
							MainKBCT.getJT().writeQualityFile(0,2,4);
							MainKBCT.getJT().refreshQualityPanel();

						} else if (url.endsWith("HelpIS5.html")) {
							System.out.println("Display HELP about STEP5");
							MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonFingrams.html"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackIS6.html")) {
							System.out.println("Go to STEP5");
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(6, false);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(5, true);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setSelectedIndex(5);
							MainKBCT.getJT().updateQuality(1);
							MainKBCT.getJT().writeQualityFile(0,2,4);
							MainKBCT.getJT().refreshQualityPanel();

						} else if (url.endsWith("DoIS6.html")) {
							// Logical View Reduction
							System.out.println("  -> Logical View Reduction");
							if (MainKBCT.getJMF().jef.jdSVG != null) {
								MainKBCT.getJMF().jef.cancelSVG();
							}
							MainKBCT.getJMF().jef.jButtonLogView_actionPerformed(true);
							MainKBCT.getJMF().jef.jMenuSave_actionPerformed();
							MainKBCT.getJMF().jMenuSave_actionPerformed();
							// Linguistic Simplification
							System.out.println("  -> Linguistic Simplification");
							MainKBCT.getJMF().jef.jButtonSimplify_actionPerformed(true);
							MainKBCT.getJMF().jef.jMenuSave_actionPerformed();
							MainKBCT.getJMF().jMenuSave_actionPerformed();
	                        MainKBCT.getConfig().SetFINGRAMSautomatic(true);
	                        MainKBCT.getConfig().SetFingramsSelectedSample(1);
	                        MainKBCT.getConfig().SetFingramsLayout(LocaleKBCT.DefaultFingramsLayout());
	                        MainKBCT.getConfig().SetFingramsMetric(LocaleKBCT.DefaultFingramsMetric());
	                  	    MainKBCT.getConfig().SetPathFinderThreshold(LocaleKBCT.DefaultPathFinderThreshold());        	  
	                	    MainKBCT.getConfig().SetPathFinderParQ(LocaleKBCT.DefaultPathFinderParQ());        	  
	                	    MainKBCT.getConfig().SetGoodnessHighThreshold(LocaleKBCT.DefaultGoodnessHighThreshold());        	  
	                	    MainKBCT.getConfig().SetGoodnessLowThreshold(LocaleKBCT.DefaultGoodnessLowThreshold());        	  
							MainKBCT.getJMF().jef.jButtonFingrams_actionPerformed(true,null);
						    MainKBCT.getJMF().jef.displaySelectedFingrams();
						    MainKBCT.getJT().updateQuality(2);
						    MainKBCT.getJT().writeQualityFile(0,3,4);
						    MainKBCT.getJT().refreshQualityPanel();
							
						} else if (url.endsWith("NextIS6.html")) {
							System.out.println("Go to STEP7");
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(6, false);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(7, true);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setSelectedIndex(7);
						    MainKBCT.getJT().updateQuality(2);
						    MainKBCT.getJT().writeQualityFile(0,3,4);
						    MainKBCT.getJT().refreshQualityPanel();

						} else if (url.endsWith("HelpIS6.html")) {
							System.out.println("Display HELP about STEP6");
							MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonSimplification.html"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackIS7.html")) {
							System.out.println("Go to STEP6");
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(7, false);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(6, true);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setSelectedIndex(6);
							MainKBCT.getJT().updateQuality(2);
							MainKBCT.getJT().writeQualityFile(0,3,4);
							MainKBCT.getJT().refreshQualityPanel();

						} else if (url.endsWith("DoIS7.html")) {
							// Optimization / Partition Tuning / SW
							System.out.println("  -> Partition Tuning (SW)");
							if (MainKBCT.getJMF().jef.jdSVG != null) {
								MainKBCT.getJMF().jef.cancelSVG();
							}
							int optOpt= MainKBCT.getConfig().GetOptOptimization();
					    	int algorithm= MainKBCT.getConfig().GetOptAlgorithm();
					    	int swOpt= MainKBCT.getConfig().GetSWoption();
					    	boolean boundedOpt= MainKBCT.getConfig().GetBoundedOptimization();
					    	int nbIt= MainKBCT.getConfig().GetNbIterations();
							MainKBCT.getConfig().SetOptOptimization(0);
							MainKBCT.getConfig().SetOptAlgorithm(1);
							MainKBCT.getConfig().SetSWoption(2);
							MainKBCT.getConfig().SetBoundedOptimization(false);
							MainKBCT.getConfig().SetNbIterations(100);
							MainKBCT.getJMF().jef.jButtonOptimization_actionPerformed(true);
							MainKBCT.getConfig().SetOptOptimization(optOpt);
							MainKBCT.getConfig().SetOptAlgorithm(algorithm);
							MainKBCT.getConfig().SetSWoption(swOpt);
							MainKBCT.getConfig().SetBoundedOptimization(boundedOpt);
							MainKBCT.getConfig().SetNbIterations(nbIt);
	                        MainKBCT.getConfig().SetFINGRAMSautomatic(true);
	                        MainKBCT.getConfig().SetFingramsSelectedSample(1);
	                        MainKBCT.getConfig().SetFingramsLayout(LocaleKBCT.DefaultFingramsLayout());
	                        MainKBCT.getConfig().SetFingramsMetric(LocaleKBCT.DefaultFingramsMetric());
	                  	    MainKBCT.getConfig().SetPathFinderThreshold(LocaleKBCT.DefaultPathFinderThreshold());        	  
	                	    MainKBCT.getConfig().SetPathFinderParQ(LocaleKBCT.DefaultPathFinderParQ());        	  
	                	    MainKBCT.getConfig().SetGoodnessHighThreshold(LocaleKBCT.DefaultGoodnessHighThreshold());        	  
	                	    MainKBCT.getConfig().SetGoodnessLowThreshold(LocaleKBCT.DefaultGoodnessLowThreshold());        	  
							MainKBCT.getJMF().jef.jButtonFingrams_actionPerformed(true,null);
						    MainKBCT.getJMF().jef.displaySelectedFingrams();
							System.out.println();
							MainKBCT.getJT().updateQuality(3);
							MainKBCT.getJT().writeQualityFile(0,4,4);
							MainKBCT.getJT().refreshQualityPanel();
							
						} else if (url.endsWith("NextIS7.html")) {
							System.out.println("Go to STEP8");
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(7, false);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(8, true);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setSelectedIndex(8);
							MainKBCT.getJT().updateQuality(3);
							MainKBCT.getJT().writeQualityFile(0,4,4);
							MainKBCT.getJT().refreshQualityPanel();

						} else if (url.endsWith("HelpIS7.html")) {
							System.out.println("Display HELP about STEP7");
							MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonOptimization.html#SW"));
							MainKBCT.getJB().setVisible(true);

						} else if (url.endsWith("BackIS8.html")) {
							System.out.println("Go to STEP7");
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(8, false);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setEnabledAt(7, true);
							MainKBCT.getJT().main_TabbedPaneKBimprovement.setSelectedIndex(7);

						} else if (url.endsWith("DoIS8.html")) {
							// Quality Assessment
							System.out.println("  -> Evaluate (Accuracy & Interpretability)");
							MainKBCT.getJMF().jef.jButtonQuality_actionPerformed(true,true);
							
						} else if (url.endsWith("HelpIS8.html")) {
							System.out.println("Display HELP about STEP8");
							MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonQuality.html"));
							MainKBCT.getJB().setVisible(true);
						}
     					/******************* END TUTORIALS *********************/
						else {
							jTextPanel.setPage(url);
							this.PageView = url.substring(url.lastIndexOf("quickstart") + 14);
							// System.out.println("this.PageView -> "+this.PageView);
						}
					}
				}
			} catch (Throwable t) {
				t.printStackTrace();
				MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JTutorialFrame: this_hyperlinkUpdate: " + e);
			}
		}
	}
	// ------------------------------------------------------------------------------
	private void generatePartitions(String opt) {
		// Induce partitions from data
		System.out.println("  -> Generate partitions");
		if (opt.equals("DATA")) {
			// Save current configuration
		    String dis= MainKBCT.getConfig().GetDistance();
		    // Set default parameters for partition induction
		    MainKBCT.getConfig().SetDistance(LocaleKBCT.DefaultDistance());
		    // Generate and select partitions
		    MainKBCT.getJMF().jMenuDataInducePartitions_actionPerformed(true, false, true);
		    // reset configuration
		    MainKBCT.getConfig().SetDistance(dis);
		} else if (opt.equals("FINGRAMS")) {
			// Save current configuration
	  	    int nbL= MainKBCT.getConfig().GetInductionNbLabels();
			String indType= MainKBCT.getConfig().GetInductionType();
			// Only generate uniform partitions (5 terms)
	        MainKBCT.getConfig().SetInductionNbLabels(4);
		    MainKBCT.getConfig().SetInductionType("regular");
			MainKBCT.getJMF().jMenuDataInducePartitions_actionPerformed(true, true, false);
		    // reset configuration
			MainKBCT.getConfig().SetInductionNbLabels(nbL);
		    MainKBCT.getConfig().SetInductionType(indType);
		} else if (opt.equals("IMPROVEMENT")) {
			// Save current configuration
	  	    int nbL= MainKBCT.getConfig().GetInductionNbLabels();
			String indType= MainKBCT.getConfig().GetInductionType();
			// Only generate uniform partitions (5 terms)
	        MainKBCT.getConfig().SetInductionNbLabels(5);
		    MainKBCT.getConfig().SetInductionType("hfp");
			MainKBCT.getJMF().jMenuDataInducePartitions_actionPerformed(true, true, false);
		    // reset configuration
			MainKBCT.getConfig().SetInductionNbLabels(nbL);
		    MainKBCT.getConfig().SetInductionType(indType);
		}
		// Show generated partitions
		MainKBCT.getJMF().jExpertButton_actionPerformed();
	}
	// ------------------------------------------------------------------------------
	private void generateRules(String opt) {
		if (opt.equals("WM")) {
		    // Induce rules from data with WM
		    System.out.println("  -> Generate rules (WM)");
		    // Set default parameters for rule induction
		    MainKBCT.getConfig().SetInductionRulesAlgorithm("Wang and Mendel");
			// Induce rules
			MainKBCT.getJMF().jMenuDataInduceRules_actionPerformed(true,false);
		} else if (opt.startsWith("FDT")) {
		    // Induce rules from data with FDT
		    System.out.println("  -> Generate rules (FDT)");
		    // save FDT config parameters
		    String ira= MainKBCT.getConfig().GetInductionRulesAlgorithm();
		    String tf= MainKBCT.getConfig().GetTreeFile();
	        int mtd= MainKBCT.getConfig().GetMaxTreeDepth();
	        double msl= MainKBCT.getConfig().GetMinSignificantLevel();
	        int lmc= MainKBCT.getConfig().GetLeafMinCard();
	        double th= MainKBCT.getConfig().GetToleranceThreshold();
	        double medg= MainKBCT.getConfig().GetMinEDGain();
	        double cth= MainKBCT.getConfig().GetCovThresh();
	        double pl= MainKBCT.getConfig().GetPerfLoss();
	        boolean pr= MainKBCT.getConfig().GetPrune();
	        boolean sp= MainKBCT.getConfig().GetSplit();
	        boolean disp= MainKBCT.getConfig().GetDisplay();
		    // Set default parameters for rule induction
		    MainKBCT.getConfig().SetInductionRulesAlgorithm("Fuzzy Decision Trees");
		    MainKBCT.getConfig().SetTreeFile(LocaleKBCT.DefaultTreeFile());
		    //if (opt.equals("FDT3"))
	          //  MainKBCT.getConfig().SetMaxTreeDepth(3);
		    //else
		        MainKBCT.getConfig().SetMaxTreeDepth(LocaleKBCT.DefaultMaxTreeDepth());

	        MainKBCT.getConfig().SetMinSignificantLevel(LocaleKBCT.DefaultMinSignificantLevel());
	        MainKBCT.getConfig().SetLeafMinCard(LocaleKBCT.DefaultLeafMinCard());
	        MainKBCT.getConfig().SetToleranceThreshold(LocaleKBCT.DefaultToleranceThreshold());
	        MainKBCT.getConfig().SetMinEDGain(LocaleKBCT.DefaultMinEDGain());
	        MainKBCT.getConfig().SetCovThresh(LocaleKBCT.DefaultCovThresh());
	        MainKBCT.getConfig().SetPerfLoss(LocaleKBCT.DefaultPerfLoss());
	        MainKBCT.getConfig().SetPrune(LocaleKBCT.DefaultPrune());
	        MainKBCT.getConfig().SetSplit(LocaleKBCT.DefaultSplit());
	        MainKBCT.getConfig().SetDisplay(LocaleKBCT.DefaultDisplay());
			// Induce rules
			MainKBCT.getJMF().jMenuDataInduceRules_actionPerformed(true,false);
            // reset config values
		    MainKBCT.getConfig().SetInductionRulesAlgorithm(ira);
		    MainKBCT.getConfig().SetTreeFile(tf);
	        MainKBCT.getConfig().SetMaxTreeDepth(mtd);
	        MainKBCT.getConfig().SetMinSignificantLevel(msl);
	        MainKBCT.getConfig().SetLeafMinCard(lmc);
	        MainKBCT.getConfig().SetToleranceThreshold(th);
	        MainKBCT.getConfig().SetMinEDGain(medg);
	        MainKBCT.getConfig().SetCovThresh(cth);
	        MainKBCT.getConfig().SetPerfLoss(pl);
	        MainKBCT.getConfig().SetPrune(pr);
	        MainKBCT.getConfig().SetSplit(sp);
	        MainKBCT.getConfig().SetDisplay(disp);
		}
		// Show generated rules
		MainKBCT.getJMF().jExpertButton_actionPerformed();
	}
	// ------------------------------------------------------------------------------
	private void buildIKBandInicialize(String opt) throws Throwable {
		String baseDir = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "quickstart";

		if (opt.equals("EXPERT")) {
			MainKBCT.getConfig().SetProblem("EXPERT");
			MainKBCT.getConfig().SetNumberOfInputs(0);
			MainKBCT.getConfig().SetNumberOfOutputLabels(0);
			baseDir = baseDir + System.getProperty("file.separator") + "EXPERT";
		} else if (opt.equals("DATA")) {
			MainKBCT.getConfig().SetProblem("IRIS");
			MainKBCT.getConfig().SetNumberOfInputs(4);
			MainKBCT.getConfig().SetNumberOfOutputLabels(3);
			baseDir = baseDir + System.getProperty("file.separator") + "DATA";
		} else if (opt.equals("FINGRAMS")) {
			MainKBCT.getConfig().SetProblem("WINE");
			MainKBCT.getConfig().SetNumberOfInputs(13);
			MainKBCT.getConfig().SetNumberOfOutputLabels(3);
			baseDir = baseDir + System.getProperty("file.separator") + "FINGRAMS";
		} else if (opt.equals("IMPROVEMENT")) {
			MainKBCT.getConfig().SetProblem("WINE");
			MainKBCT.getConfig().SetNumberOfInputs(13);
			MainKBCT.getConfig().SetNumberOfOutputLabels(3);
			baseDir = baseDir + System.getProperty("file.separator") + "IMPROVEMENT";
		}
		MainKBCT.getConfig().SetKBCTFilePath(baseDir);
		MainKBCT.getJMF().jMenuNew_actionPerformed();
		if (opt.equals("EXPERT")) {
			MainKBCT.getJMF().jMenuExpertNew_actionPerformed();
			MainKBCT.getJMF().jExpertButton_actionPerformed();
		} else {
			boolean aux = MainKBCT.getConfig().GetTESTautomatic();
		    MainKBCT.getConfig().SetTESTautomatic(true);
			if (opt.equals("DATA")) {
			    String fileDataToOpen = baseDir	+ System.getProperty("file.separator") + "IRIS.txt";
			    //System.out.println(fileDataToOpen);
			    this.cleanDir("DATA");
			    MainKBCT.getJMF().jMenuDataOpen_actionPerformed(fileDataToOpen);
			    MainKBCT.getJMF().ValidationDataRatio= 0.8;
			    MainKBCT.getJMF().Seed= 1;
			    MainKBCT.getJMF().jMenuDataGenerateSample_actionPerformed(true);
			    MainKBCT.getJMF().jMenuClose_actionPerformed();
			    String fileIKBtoOpen = baseDir
					+ System.getProperty("file.separator") + "DATA0"
					+ System.getProperty("file.separator")
					+ "IRIS.txt.0.ikb.xml";

			    MainKBCT.getJMF().jMenuOpen_actionPerformed(fileIKBtoOpen);
			    if (MainKBCT.getJMF().KBDataFile.equals("")) {
				    // System.out.println("--------------- WARNING --------------------");
				    MainKBCT.getJMF().jMenuClose_actionPerformed();
			    }
			} else if (opt.equals("FINGRAMS")) {
			    String fileDataToOpen = baseDir	+ System.getProperty("file.separator") + "WINE.txt";
			    // System.out.println(fileDataToOpen);
			    this.cleanDir("FINGRAMS");
			    MainKBCT.getJMF().jMenuDataOpen_actionPerformed(fileDataToOpen);
			} else if (opt.equals("IMPROVEMENT")) {
			    String fileDataToOpen = baseDir	+ System.getProperty("file.separator") + "WINE.txt";
			    // System.out.println(fileDataToOpen);
			    this.cleanDir("IMPROVEMENT");
			    MainKBCT.getJMF().jMenuDataOpen_actionPerformed(fileDataToOpen);
			    MainKBCT.getJMF().ValidationDataRatio= 0.75;
			    MainKBCT.getJMF().Seed= 1;
			    MainKBCT.getJMF().jMenuDataGenerateSample_actionPerformed(true);
			    MainKBCT.getJMF().jMenuClose_actionPerformed();
			    String fileIKBtoOpen = baseDir
					+ System.getProperty("file.separator") + "IMPROVEMENT0"
					+ System.getProperty("file.separator")
					+ "WINE.txt.0.ikb.xml";

			    MainKBCT.getJMF().jMenuOpen_actionPerformed(fileIKBtoOpen);
			    if (MainKBCT.getJMF().KBDataFile.equals("")) {
				    // System.out.println("--------------- WARNING --------------------");
				    MainKBCT.getJMF().jMenuClose_actionPerformed();
			    }
			}
			MainKBCT.getConfig().SetTESTautomatic(aux);
		}
	}
	// ------------------------------------------------------------------------------
	private void cleanDir(String opt) {
		String baseDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "quickstart";
		String fileDataToOpen= "";
        if (opt.equals("DATA")) {
		    baseDir = baseDir + System.getProperty("file.separator") + "DATA";
		    fileDataToOpen = baseDir + System.getProperty("file.separator") + "IRIS.txt";
        } else if (opt.equals("FINGRAMS")) {
		    baseDir = baseDir + System.getProperty("file.separator") + "FINGRAMS";
		    fileDataToOpen = baseDir + System.getProperty("file.separator") + "WINE.txt";
        } else if (opt.equals("IMPROVEMENT")) {
		    baseDir = baseDir + System.getProperty("file.separator") + "IMPROVEMENT";
		    fileDataToOpen = baseDir + System.getProperty("file.separator") + "WINE.txt";
        }
		// System.out.println(fileDataToOpen);
		File fdir = new File(baseDir);
		File[] ff = fdir.listFiles();
		for (int n = 0; n < ff.length; n++) {
			// System.out.println(ff[n].getAbsolutePath());
			if (ff[n].exists() && ff[n].isDirectory()) {
				File[] ffaux = ff[n].listFiles();
				for (int k = 0; k < ffaux.length; k++) {
					if (ffaux[k].exists()) {
						// System.out.println("DELETE: "+ffaux[k].getAbsolutePath());
						ffaux[k].delete();
					}
				}
			}
			if (ff[n].exists() && !ff[n].getAbsolutePath().equals(fileDataToOpen)) {
				// System.out.println("DELETE: "+ff[n].getAbsolutePath());
				ff[n].delete();
			}
		}
	}
	// ------------------------------------------------------------------------------
	private String getHtmlPageToShow(String Page) {
		String Lang = MainKBCT.getConfig().GetLanguage();
		if ((Lang.equals("fr")) || (Lang.equals("es")))
			Lang = "en";

		String dir = "quickstart";
		String PageToShow = "";
		if (LocaleKBCT.isWindowsPlatform())
			PageToShow = "file:\\" + System.getProperty("user.dir")
					+ System.getProperty("file.separator") + dir
					+ System.getProperty("file.separator") + Lang
					+ System.getProperty("file.separator") + Page;
		else {
			String kbctpath = System.getProperty("guajepath");
			if (kbctpath.equals(""))
				kbctpath = System.getProperty("user.dir");

			PageToShow = "file://" + kbctpath
					+ System.getProperty("file.separator") + dir
					+ System.getProperty("file.separator") + Lang
					+ System.getProperty("file.separator") + Page;
		}
		// PageToShow="http://www.softcomputing.es";
		return PageToShow;
	}
// ------------------------------------------------------------------------------
	private void updateQuality(int step) {
		// ACC(training), Coverage(training), ACC(test), Coverage(test), NR, TRL, AFR(training), AFR(test)
		MainKBCT.getJMF().jef.jButtonQuality_actionPerformed(false, true);
		if (MainKBCT.getJMF().jef.jrbqf != null) {
		   this.KBquality[step][0]= MainKBCT.getJMF().jef.jrbqf.getAccIndexLRN();
		   this.KBquality[step][3]= MainKBCT.getJMF().jef.jrbqf.getAccIndexTST();
		   this.KBquality[step][4]= MainKBCT.getJMF().jef.jrbqf.getCoverage();
		   this.KBquality[step][5]= MainKBCT.getJMF().jef.jrbqf.getAvConfFD();
		}
		//System.out.println("ACC-train -> "+this.KBquality[step][0]);
		if (MainKBCT.getJMF().jef.jrbqfTut != null) {
		    this.KBquality[step][1]= MainKBCT.getJMF().jef.jrbqfTut.getCoverage();
		    this.KBquality[step][2]= MainKBCT.getJMF().jef.jrbqfTut.getAvConfFD();
		}
		if (MainKBCT.getJMF().jef.jkbif != null) {
	        this.KBquality[step][6]= MainKBCT.getJMF().jef.jkbif.getTotalRuleNumber();
	        this.KBquality[step][7]= MainKBCT.getJMF().jef.jkbif.getTotalRuleLength();
	        this.KBquality[step][8]= MainKBCT.getJMF().jef.jkbif.getAvSFR();
	        this.KBquality[step][9]= MainKBCT.getJMF().jef.jkbif.getAvSFRtest();
	        //this.KBquality[step][10]= 0;
		}
    	/*int lim1= this.KBquality.length;
    	int lim2= this.KBquality[0].length;
        for (int n=0; n<lim1; n++) {
            for (int m=0; m<lim2; m++) {
            	 this.KBquality[n][m]= 1;
            }        	
        }*/
	}
	// ------------------------------------------------------------------------------
	private void writeQualityFile(int stepini, int stepend, int L) {
        File temp= JKBCTFrame.BuildFile("Tutorial-Quality.html");
        DecimalFormat df= new DecimalFormat();
        df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
        df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
        DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
        dfs.setDecimalSeparator((new String(".").charAt(0)));
        df.setDecimalFormatSymbols(dfs);
        df.setGroupingSize(20);
        try {
          PrintWriter fOut = new PrintWriter(new FileOutputStream(temp.getAbsolutePath()), true);
      	  fOut.println("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
      	  fOut.println("<html>");
      	  fOut.println("<head></head>");
      	  fOut.println("<body>");
      	  fOut.println("<h1>Quality Evaluation</h1>");
      	  fOut.println("<h2>Summary</h2>");
      	  fOut.println("<table border='2' width='110%'>");
      	  // Header
      	  fOut.println("<tr>");
      	  fOut.println("<td width='10%' align='center' bgcolor='black'><b><font face='Times New Roman' color='white'>KB</font></b></td>");
      	  fOut.println("<td width='10%' align='center' bgcolor='black'><b><font face='Times New Roman' color='white'>ACC(train)</font></b></td>");
      	  fOut.println("<td width='10%' align='center' bgcolor='black'><b><font face='Times New Roman' color='white'>COV(train)</font></b></td>");
      	  fOut.println("<td width='10%' align='center' bgcolor='black'><b><font face='Times New Roman' color='white'>ACFD(train)</font></b></td>");
      	  fOut.println("<td width='10%' align='center' bgcolor='black'><b><font face='Times New Roman' color='white'>ACC(test)</font></b></td>");
      	  fOut.println("<td width='10%' align='center' bgcolor='black'><b><font face='Times New Roman' color='white'>COV(test)</font></b></td>");
      	  fOut.println("<td width='10%' align='center' bgcolor='black'><b><font face='Times New Roman' color='white'>ACFD(test)</font></b></td>");
      	  fOut.println("<td width='10%' align='center' bgcolor='black'><b><font face='Times New Roman' color='white'>NR</font></b></td>");
      	  fOut.println("<td width='10%' align='center' bgcolor='black'><b><font face='Times New Roman' color='white'>TRL</font></b></td>");
      	  fOut.println("<td width='10%' align='center' bgcolor='black'><b><font face='Times New Roman' color='white'>AFR(train)</font></b></td>");
      	  fOut.println("<td width='10%' align='center' bgcolor='black'><b><font face='Times New Roman' color='white'>AFR(test)</font></b></td>");
      	  //fOut.println("<td width='10%' align='center' bgcolor='black'><b><font face='Times New Roman' color='white'>COFCI</font></b></td>");
      	  fOut.println("</tr>");
          // Main
      	  int lim1= this.KBquality.length;
      	  int lim2= this.KBquality[0].length;
      	  //System.out.println("lim1= "+lim1);
      	  //System.out.println("lim2= "+lim2);
      	  for (int n=stepini; n<stepend; n++) {  	
      	     fOut.println("<tr>");
      	     fOut.println("<td width='20%' align='center'><b><font face='Times New Roman'>STEP"+String.valueOf(n+L)+"</font></b></td>");
      	     for (int m=0; m<lim2; m++) {
      	    	  if (m==6 || m==7) 
      	              fOut.println("<td width='10%' align='center'><b><font face='Times New Roman'>"+(int)this.KBquality[n][m]+"</font></b></td>");
      	    	  else 
      	    	      fOut.println("<td width='10%' align='center'><b><font face='Times New Roman'>"+df.format(this.KBquality[n][m])+"</font></b></td>");
      	     }
      		 fOut.println("</tr>");
          }
      	  fOut.println("</table>");
      	  fOut.println("</body></html>");
          fOut.flush();
          fOut.close();
        } catch (Throwable t) {
            t.printStackTrace();
            MessageKBCT.Error(null, t);
        }
  }
  // ------------------------------------------------------------------------------
	private void refreshQualityPanel() {
	  try {
		 MainKBCT.getJT().jTextPanelQuality= new JTextPane();
		 MainKBCT.getJT().jTextPanelQuality.setBackground(Color.LIGHT_GRAY);
         File temp= JKBCTFrame.BuildFile("Tutorial-Quality.html");
	     if (LocaleKBCT.isWindowsPlatform())
	    	 MainKBCT.getJT().jTextPanelQuality.setPage("file:\\"+ temp.getAbsolutePath());
	     else
	    	 MainKBCT.getJT().jTextPanelQuality.setPage("file://"+ temp.getAbsolutePath());
	     
	     MainKBCT.getJT().jTextPanelQuality.setSelectedTextColor(Color.RED);
	     MainKBCT.getJT().jTextPanelQuality.setEditable(false);
	     MainKBCT.getJT().jScrollPanelQuality.getViewport().removeAll();
	     MainKBCT.getJT().jScrollPanelQuality.getViewport().add(MainKBCT.getJT().jTextPanelQuality);

	  } catch (Throwable t) {
          t.printStackTrace();
          MessageKBCT.Error(null, t);
	  }
	}
}