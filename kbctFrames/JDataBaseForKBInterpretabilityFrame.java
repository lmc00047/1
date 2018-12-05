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
//              JDataBaseForKBInterpretabilityFrame.java
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import kbct.JKBCT;
import kbct.JVariable;
import kbct.LocaleKBCT;
import kbctAux.MessageKBCT;

import org.freehep.util.export.ExportDialog;

import print.JPrintPreview;
import fis.FISPlot;

/**
 * kbctFrames.JDataBaseForKBInterpretabilityFrame.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

//------------------------------------------------------------------------------
public class JDataBaseForKBInterpretabilityFrame extends JChildFrame {
  static final long serialVersionUID=0;	
  private JKBCTFrame Parent;
  private JKBCT kbct;
  private JVariable var;
  private int VarNb;
  private boolean Variable= false;
  private int KB;
  private JScrollPane jPanelVariables= new JScrollPane();
  private JPanel jPanelDefinitions= new JPanel();
  private JMenuItem jMenuPrint= new JMenuItem();
  private JMenuItem jMenuExport= new JMenuItem();
  private JMenuItem jMenuClose= new JMenuItem();
  private String conjunction= null;
  private String disjunction= null;
  private String defuzzification= null;
//------------------------------------------------------------------------------
  public void Translate() throws Throwable {
    this.setTitle(LocaleKBCT.GetString("Interpretability"));
    this.jMenuPrint.setText(LocaleKBCT.GetString("Print"));
    this.jMenuExport.setText(LocaleKBCT.GetString("Export"));
    this.jMenuClose.setText(LocaleKBCT.GetString("Close"));
    this.repaint();
  }
//------------------------------------------------------------------------------
  public JDataBaseForKBInterpretabilityFrame( JKBCTFrame parent, JKBCT kbct, String conj, String disj, String defuz, int KB ) {
    super(parent);
    this.Parent= parent;
    this.kbct= kbct;
    this.conjunction= conj;
    this.disjunction= disj;
    this.defuzzification= defuz;
    this.KB= KB;
    try {
      jbInit();
    } catch(Throwable t) {
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en constructor de JDataBaseForKBInterpretabilityFrame: "+t);
    }
  }
//------------------------------------------------------------------------------
  public JDataBaseForKBInterpretabilityFrame( JKBCTFrame parent, JVariable v, int KB, int VarNb ) {
    super(parent);
    this.Parent= parent;
    this.var= v;
    this.Variable= true;
    this.VarNb= VarNb;
    this.KB= KB;
    try {
      jbInit();
    } catch(Throwable t) {
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en constructor de JDataBaseForKBInterpretabilityFrame: "+t);
    }
  }
//------------------------------------------------------------------------------
  private void jbInit() throws Throwable {
    JMenuBar jmb= new JMenuBar();
    jmb.add(this.jMenuPrint);
    jmb.add(this.jMenuExport);
    jmb.add(this.jMenuClose);
    this.setJMenuBar(jmb);
    this.getContentPane().setLayout(new GridBagLayout());
    this.jPanelDefinitions.setLayout(new GridBagLayout());
      TitledBorder titledBorderKB1= new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)), LocaleKBCT.GetString("KBCT")+String.valueOf(KB));
      JPanel KBi= new JPanel();
      KBi.setLayout(new GridBagLayout());
      if (!this.Variable) {
        for (int n=0; n<this.kbct.GetNbInputs(); n++) {
          JPanel p= this.BuildJPanelVariable(this.kbct.GetInput(n+1), n+1, false);
          KBi.add(p, new GridBagConstraints(n, 0, 1, 1, 1.0, 1.0
                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        }
        JPanel pout= this.BuildJPanelVariable(this.kbct.GetOutput(1), 1, true);
        KBi.add(pout, new GridBagConstraints(this.kbct.GetNbInputs(), 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        JPanel poperators= this.BuildJPanelOperators();
        KBi.add(poperators, new GridBagConstraints(this.kbct.GetNbInputs()+1, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        KBi.setBorder(titledBorderKB1);
      } else {
        JPanel p;
        if (this.var.isInput()) {
            p= this.BuildJPanelVariable(this.var, this.VarNb, false);
            KBi.setBorder(titledBorderKB1);
        } else
            p= this.BuildJPanelVariable(this.var, this.VarNb, true);

        KBi.add(p, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      }
      this.jPanelDefinitions.add(KBi, new GridBagConstraints(0, 1, 1, 0, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      jPanelDefinitions.addMouseListener(new MouseListener() {
      public void mousePressed(MouseEvent e) {
        JDataBaseForKBInterpretabilityFrame.this.jPanelDefinitions.repaint();
      }
      public void mouseReleased(MouseEvent e) {
        JDataBaseForKBInterpretabilityFrame.this.jPanelDefinitions.repaint();
      }
      public void mouseEntered(MouseEvent e) {
        JDataBaseForKBInterpretabilityFrame.this.jPanelDefinitions.repaint();
      }
      public void mouseExited(MouseEvent e) {
        JDataBaseForKBInterpretabilityFrame.this.jPanelDefinitions.repaint();
      }
      public void mouseClicked(MouseEvent e) {
        JDataBaseForKBInterpretabilityFrame.this.jPanelDefinitions.repaint();
      }
    });
    this.jPanelVariables.getViewport().add(this.jPanelDefinitions);
    this.getContentPane().add(jPanelVariables, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
       ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));

    jMenuPrint.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JDataBaseForKBInterpretabilityFrame.this.jMenuPrint_actionPerformed(); } });
    jMenuExport.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JDataBaseForKBInterpretabilityFrame.this.jMenuExport_actionPerformed(); } });
    jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JDataBaseForKBInterpretabilityFrame.this.dispose(); } });
    this.Translate();
    this.pack();
    this.setLocation(this.ChildPosition(this.getSize()));
    this.setVisible(true);
  }
//------------------------------------------------------------------------------
  private JPanel BuildJPanelOperators() throws Throwable {
    JPanel result= new JPanel(new GridBagLayout());
    TitledBorder titledBorderResult= new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),LocaleKBCT.GetString("Operators"));
    result.setBorder(titledBorderResult);
    JLabel jLabelConjunction= new JLabel(LocaleKBCT.GetString("Conjunction_value")+" "+this.conjunction);
    result.add(jLabelConjunction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    JLabel jLabelDisjunction= new JLabel(LocaleKBCT.GetString("Disjunction_value")+" "+this.disjunction);
    result.add(jLabelDisjunction, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    JLabel jLabelDefuzzification= new JLabel(LocaleKBCT.GetString("Defuzzification_value")+" "+this.defuzzification);
    result.add(jLabelDefuzzification, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    return result;
  }
//------------------------------------------------------------------------------
  private JPanel BuildJPanelVariable(JVariable jvar, int Nb_var, boolean output) throws Throwable {
    JPanel result= new JPanel(new GridBagLayout());
    TitledBorder titledBorderResult;
    if (output)
      titledBorderResult= new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),LocaleKBCT.GetString("Output"));
    else
      titledBorderResult = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),LocaleKBCT.GetString("Input")+" "+Nb_var);

    result.setBorder(titledBorderResult);
    JLabel jLabelName= new JLabel(LocaleKBCT.GetString("Name")+"= "+jvar.GetName());
    result.add(jLabelName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    JLabel jLabelType= new JLabel(LocaleKBCT.GetString("Type")+"= "+LocaleKBCT.GetString(jvar.GetType()));
    result.add(jLabelType, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    JLabel jLabelRange= new JLabel(LocaleKBCT.GetString("Range")+"=  [ "+jvar.GetInputInterestRange()[0]+", "+jvar.GetInputInterestRange()[1]+"]");
    result.add(jLabelRange, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    String LabelsNames=LocaleKBCT.GetString(jvar.GetLabelsName(0))+", ";
    int Lim= jvar.GetLabelsNumber();
    for (int n=1; n<Lim; n++) {
      LabelsNames= LabelsNames+LocaleKBCT.GetString(jvar.GetLabelsName(n));
      if (n<Lim-1)
          LabelsNames= LabelsNames+", ";
    }
    JLabel jLabelLinguisticTerms= new JLabel(LocaleKBCT.GetString("Labels")+"= "+LabelsNames);
    result.add(jLabelLinguisticTerms, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    JMFsPanel jGraphMFs= new JMFsPanel(jvar);
    FISPlot.DisableZoom(jGraphMFs);
    result.add(jGraphMFs, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
           ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    return result;
  }
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
            return JDataBaseForKBInterpretabilityFrame.this.print(graphics, pageFormat);
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
          JDataBaseForKBInterpretabilityFrame.this.jPanelVariables.paint(g);
          g.translate(0, JDataBaseForKBInterpretabilityFrame.this.jPanelVariables.getHeight());
          JDataBaseForKBInterpretabilityFrame.this.jPanelVariables.paint(g);
          g.setColor(Color.gray);
          g.drawLine(0,0,0, JDataBaseForKBInterpretabilityFrame.this.jPanelVariables.getHeight()-1);
        }
      };
      panel.setSize(new Dimension(this.jPanelVariables.getWidth(), this.jPanelVariables.getHeight()));
      export.showExportDialog( panel, "Export view as ...", panel, "export" );
    } catch (Exception ex) { MessageKBCT.Error(null, ex); }
  }
//------------------------------------------------------------------------------
  public int print(java.awt.Graphics g, PageFormat pageFormat) throws PrinterException {
     java.awt.Graphics2D g2 = ( java.awt.Graphics2D )g;
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
     this.jPanelVariables.print( g2 );
     return Printable.PAGE_EXISTS;
 }
}