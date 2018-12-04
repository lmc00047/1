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
//                          JBeginnerFrame.java
//
//
//**********************************************************************

package kbctFrames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbctAux.MessageKBCT;
import kbctAux.Translatable;

/**
 * kbctFrames.JBeginnerFrame creates a Frame to display HTML help files.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
public class JBeginnerFrame extends JFrame implements Translatable {
  static final long serialVersionUID=0;	
  private JMenuBar jMenuBarBF= new JMenuBar();
  private JMenuItem jMenuIni= new JMenuItem();
  private JMenuItem jMenuForward= new JMenuItem();
  private JMenuItem jMenuBackward= new JMenuItem();
  private JMenuItem jMenuClose= new JMenuItem();
  private ImageIcon icon_kbct= LocaleKBCT.getIconGUAJE();
  private JScrollPane jScrollPanel= new JScrollPane();
  private JTextPane jTextPanel= new JTextPane();
  private String PageView;
  private Vector<String> pages;
  private int NbPage;
  //----------------------------------------------------------------------------
  public JBeginnerFrame(String PageHTML) {
    try {
      this.PageView=PageHTML;
      this.jbInit();
      jMenuIni.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuIni_actionPerformed(); }} );
      jMenuForward.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuForward_actionPerformed(); }} );
      jMenuBackward.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuBackward_actionPerformed(); }} );
      jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { this_windowClose(); }} );
      this.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) { this_windowClosing(); }
        public void windowActivated(WindowEvent e) { this_windowActivated(); }
      });
      this.jTextPanel.addHyperlinkListener(new HyperlinkListener() { public void hyperlinkUpdate(HyperlinkEvent e) { this_hyperlinkUpdate(e);}});
      JKBCTFrame.AddTranslatable(this);
      pages= new Vector<String>();
      pages.add((String)this.PageView);
      this.NbPage=0;
    } catch (Throwable t) {
    	t.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in building JBeginnerFrame: "+t);
    }
  }
  //----------------------------------------------------------------------------
  private void jbInit() throws Throwable {
    this.setIconImage(icon_kbct.getImage());
    this.setState(JBeginnerFrame.NORMAL);
    this.setJMenuBar(this.jMenuBarBF);
    this.jMenuBarBF.add(this.jMenuIni);
    this.jMenuBarBF.add(this.jMenuForward);
    this.jMenuBarBF.add(this.jMenuBackward);
    this.jMenuBarBF.add(this.jMenuClose);
    Dimension dim = getToolkit().getScreenSize();
    this.setSize(550, 300);
    this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
    jScrollPanel.setWheelScrollingEnabled(true);
    jScrollPanel.getViewport().add(jTextPanel, null);
    this.Translate();
  }
  //----------------------------------------------------------------------------
  public void Translate() {
    this.setTitle(LocaleKBCT.GetString("BeginnerMode"));
    this.jMenuIni.setText(LocaleKBCT.GetString("Ini"));
    this.jMenuForward.setText(">>");
    this.jMenuBackward.setText("<<");
    this.jMenuClose.setText(LocaleKBCT.GetString("Close"));
    this.jMenuForward.setToolTipText(LocaleKBCT.GetString("Forward"));
    this.jMenuBackward.setToolTipText(LocaleKBCT.GetString("Backward"));
    this.getContentPane().add(jScrollPanel, BorderLayout.CENTER);
    jTextPanel.setBackground(Color.LIGHT_GRAY);
    try {
      String Lang= MainKBCT.getConfig().GetLanguage();
      if ( (Lang.equals("fr")) || (Lang.equals("es")) )
          Lang= "en";

      //System.out.println("kbctpath= "+System.getProperty("kbctpath"));
      //System.out.println("espressopath= "+System.getProperty("espressopath"));
      //System.out.println("graphpath= "+System.getProperty("graphpath"));
      //System.out.println("user.dir= "+System.getProperty("user.dir"));
      String PageToShow="";
      if (LocaleKBCT.isWindowsPlatform())
        PageToShow= "file:\\"+System.getProperty("user.dir")+ System.getProperty("file.separator") + "help" + System.getProperty("file.separator") + Lang + System.getProperty("file.separator") + this.PageView;
      else {
    	String kbctpath= System.getProperty("guajepath");
    	if (kbctpath.equals(""))
    		kbctpath=System.getProperty("user.dir");
    	
        PageToShow= "file://" + kbctpath+ System.getProperty("file.separator") + "help" + System.getProperty("file.separator") + Lang + System.getProperty("file.separator") + this.PageView;
      }
      jTextPanel.setPage(PageToShow);
    } catch (java.net.UnknownHostException e) {
        this.PageView="index.html";
        this.dispose();
        JKBCTFrame.RemoveTranslatable(this);
        MainKBCT.setJB(new JBeginnerFrame(this.PageView));
        MainKBCT.getJB().setVisible(true);
    } catch (Exception e) {
        e.printStackTrace();
    	MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JBeginnerFrame en Translate: "+e);
    }
    jTextPanel.setSelectedTextColor(Color.RED);
    jTextPanel.setEditable(false);
    this.repaint();
  }
  //------------------------------------------------------------------------------
  public void jMenuIni_actionPerformed() {
    this.PageView="index.html";
    this.dispose();
    JKBCTFrame.RemoveTranslatable(this);
    MainKBCT.setJB(new JBeginnerFrame(this.PageView));
    MainKBCT.getJB().setVisible(true);
  }
  //------------------------------------------------------------------------------
  public void jMenuForward_actionPerformed() {
    Object[] pp= this.pages.toArray();
    if (this.NbPage+1<pp.length) {
      this.NbPage++;
      this.PageView= (String)pp[this.NbPage];
      this.dispose();
      JKBCTFrame.RemoveTranslatable(this);
      MainKBCT.setJB(new JBeginnerFrame(this.PageView));
      MainKBCT.getJB().NbPage= this.NbPage;
      MainKBCT.getJB().pages= this.pages;
      MainKBCT.getJB().setVisible(true);
    }
  }
  //------------------------------------------------------------------------------
  public void jMenuBackward_actionPerformed() {
    Object[] pp= this.pages.toArray();
    if (this.NbPage-1>=0) {
      this.NbPage--;
      this.PageView= (String)pp[this.NbPage];
      this.dispose();
      JKBCTFrame.RemoveTranslatable(this);
      MainKBCT.setJB(new JBeginnerFrame(this.PageView));
      MainKBCT.getJB().NbPage= this.NbPage;
      MainKBCT.getJB().pages= this.pages;
      MainKBCT.getJB().setVisible(true);
    }
  }
  //------------------------------------------------------------------------------
  public void this_windowClose() {
	int opt= MessageKBCT.Confirm(this, LocaleKBCT.GetString("CloseWindow_ChangeExpertUse") + "\n" + LocaleKBCT.GetString("DoYouWantToCloseIt"), 1, false, false, false); 
    if(opt == JOptionPane.YES_OPTION) {
      MainKBCT.getConfig().SetUser("Expert");
      this.dispose();
      JKBCTFrame.RemoveTranslatable(this);
    } else {
       MainKBCT.setJB(new JBeginnerFrame(this.PageView));
       MainKBCT.getJB().setVisible(true);
       this.dispose();
    }
  }
  //------------------------------------------------------------------------------
  public void this_windowClosing() {
    this.this_windowClose();
  }
  //------------------------------------------------------------------------------
  public void this_windowActivated() {
    Object[] pp= this.pages.toArray();
    if (this.NbPage<pp.length-1)
      this.jMenuForward.setEnabled(true);
    else
      this.jMenuForward.setEnabled(false);
    if (this.NbPage > 0)
      this.jMenuBackward.setEnabled(true);
    else
      this.jMenuBackward.setEnabled(false);
  }
  //----------------------------------------------------------------------------
  /**
   * Jump to hyperlink reference.
   */
  public void this_hyperlinkUpdate (HyperlinkEvent e) {
    if (e.getEventType().toString().equals("ACTIVATED")) {
      try {
        String url=e.getURL().toString();
        if ( (url.equals("http://www7.inra.fr/mia/M/fispro/")) ||
             (url.equals("http://www.mathworks.es/products/fuzzy-logic/")) ||
             (url.equals("http://www.imse.cnm.es/Xfuzzy")) ||
             (url.equals("https://forja.rediris.es/projects/xfuzzy/")) ||
             (url.equals("http://www.cs.columbia.edu/~cs4861/s07-sis/")) ||
             (url.equals("http://oa.upm.es/588")) ||
             (url.startsWith("http://dx.doi.org")) ||
             (url.startsWith("http://sourceforge.net")) ||
             (url.startsWith("http://sci2s.ugr.es/fss/")) ||
             (url.startsWith("http://www.")) ) {
             MessageKBCT.Information(this, LocaleKBCT.GetString("ExternalPage")+"\n"+url);
        } else {
          jTextPanel.setPage(url);
          this.PageView=url.substring(url.lastIndexOf("help")+8);
          Object[] pp= this.pages.toArray();
          for (int n=this.NbPage+1; n<pp.length; n++)
            this.pages.remove(pp[n]);
          this.pages.add(this.PageView);
          this.NbPage++;

          pp= this.pages.toArray();
          if (this.NbPage<pp.length-1)
            this.jMenuForward.setEnabled(true);
          else
            this.jMenuForward.setEnabled(false);
          if (this.NbPage > 0)
            this.jMenuBackward.setEnabled(true);
          else
            this.jMenuBackward.setEnabled(false);
        }
      } catch (Exception ex) {
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JBeginner: this_hyperlinkUpdate: "+e);
      }
    }
  }
}