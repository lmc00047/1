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
//                           JRuleFrameInfer.java
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
import java.util.Vector;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import kbct.JKBCT;
import kbct.JVariable;
import kbct.LocaleKBCT;
import kbctAux.MessageKBCT;

import org.freehep.util.export.ExportDialog;

import print.JPrintPreview;
import KB.Rule;
import KB.variable;
import fis.JSemaphore;

/**
 * kbctFrames.JRuleFrameInfer displays a table of rules.
 * It is used in Inferences.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
//------------------------------------------------------------------------------
public class JRuleFrameInfer extends JChildFrame {
  static final long serialVersionUID=0;	
  private JTable jTableRules;
  private JScrollPane jPanelRules= new JScrollPane();
  private DefaultTableModel RuleModel;
  private int NbIn;
  private int NbOut;
  private Vector Title;
  private Vector Data;
  private JSemaphore JRuleFrameOpen= new JSemaphore();
  private JMenuBar jMenuBarRules= new JMenuBar();
  private JMenuItem jMenuPrint= new JMenuItem();
  private JMenuItem jMenuExport= new JMenuItem();
  private JMenuItem jMenuClose= new JMenuItem();
  private JKBCT kbct;
  private JExpertFrame Parent;
  private int width;
  private int height;
//------------------------------------------------------------------------------
  public JRuleFrameInfer( JExpertFrame parent, JKBCT kbct, JSemaphore open, int width, int height ) {
    super(parent);
    this.Parent= parent;
    this.kbct= new JKBCT(kbct);
    this.JRuleFrameOpen = open;
    this.width= width;
    this.height= height;
    try {
      jbInit();
      this.JRuleFrameOpen.Acquire();
      JMainFrame.AddTranslatable(this);
      this.setLocation(this.ChildPosition(this.getSize()));
      this.setVisible(true);
    } catch( Throwable t ) {
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en constructor de JRuleFrameInfer: "+t);
    }
  }
//------------------------------------------------------------------------------
  private void jbInit() throws Throwable {
    this.setJMenuBar(this.jMenuBarRules);
    jMenuBarRules.add(jMenuPrint);
    jMenuBarRules.add(jMenuExport);
    jMenuBarRules.add(jMenuClose);
    this.jbInitTables();
    this.Translate();
    this.Events();
    this.setSize(this.width,this.height/2);
  }
//------------------------------------------------------------------------------
  private void jbInitTables() throws Throwable {
    NbIn = this.kbct.GetNbInputs();
    NbOut = this.kbct.GetNbOutputs();
    int NbRules = this.kbct.GetNbRules();
    Title = new Vector();
    for( int i=0 ; i<NbIn+NbOut+2 ; i++ )
      Title.add(new String());

    Data = new Vector();
    for( int rule=0 ; rule<NbRules ; rule++ ) {
      Rule jr = (Rule)this.kbct.GetRule(rule+1);
      Vector rule_data = new Vector();
      rule_data.add(new Integer(rule+1));
      rule_data.add(jr.GetType());
      if (jr.GetActive()) {
        int[] in_labels_number= jr.Get_in_labels_number();
        for (int n=0; n<NbIn;n++) {
          JVariable in= kbct.GetInput(n+1);
          variable v= in.GetV();
          if (! v.GetFlagModify()) {
            if (in.GetScaleName().equals("user"))
                rule_data.add(in.GetUserLabelsName(in_labels_number[n] - 1));
            else {
                String LabelName= in.GetLabelsName(in_labels_number[n] - 1);
                if (!LabelName.equals(""))
                   rule_data.add(LocaleKBCT.GetString(LabelName));
                else
                   rule_data.add(LabelName);
            }
          } else
                rule_data.add(in.GetLabelsName(in_labels_number[n] - 1));
        }
        int[] out_labels_number= jr.Get_out_labels_number();
        for (int n=0; n<NbOut;n++) {
          JVariable out= kbct.GetOutput(n+1);
          variable v= out.GetV();
          if (! v.GetFlagModify()) {
            if (out.GetScaleName().equals("user")) {
                if ( (out.GetInputInterestRange()[0]!=1) &&
                	 (out.GetInputInterestRange()[1]!=out.GetLabelsNumber()) &&
               	     ( (out.GetType().equals("logical")) || (out.GetType().equals("categorical")) ) ) {
                    rule_data.add(out_labels_number[n]);
                } else 
                    rule_data.add(out.GetUserLabelsName(out_labels_number[n] - 1));
            } else {
                String LabelName=out.GetLabelsName(out_labels_number[n] - 1);
                if (!LabelName.equals(""))
                   rule_data.add(LocaleKBCT.GetString(LabelName));
                else
                   rule_data.add(LabelName);
            }
          } else
                rule_data.add(out.GetLabelsName(out_labels_number[n] - 1));
         }
        } else {
            for (int n=0; n<NbIn+NbOut;n++)
              rule_data.add("");
        }
        Data.add( rule_data );
      }
    jTableRules = new JTable();
    RuleModel = new DefaultTableModel() {
   	  static final long serialVersionUID=0;	
      public Class getColumnClass(int c) { return getValueAt(0, c).getClass(); }
      public boolean isCellEditable(int row, int col) { return false; }
    };
    RuleModel.setDataVector(Data, Title);
    jTableRules.setModel(RuleModel);
    for( int i=0 ; i<this.jTableRules.getColumnCount() ; i++ )
      this.jTableRules.getColumnModel().getColumn(i).setHeaderRenderer(new DefaultTableCellRenderer() {
   	    static final long serialVersionUID=0;	
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
          super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
          super.setHorizontalAlignment(SwingConstants.CENTER);
          super.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
          if( column == 0 ) { super.setText(LocaleKBCT.GetString("Rule")); }
          if( column == 1 ) { super.setText(LocaleKBCT.GetString("Type")); }
          try {
            JVariable input = null;
            if( (column == 2) && (NbIn != 0) ) {
              input = kbct.GetInput(1);
              super.setText(LocaleKBCT.GetString("If") + " " + input.GetName());
            }
            if( (column > 2) && (column < (NbIn+2)) ) {
              input = kbct.GetInput(column-1);
              super.setText(LocaleKBCT.GetString("AND") + " " + input.GetName());
            }
            if( column == (NbIn+2) ) {
              input = kbct.GetOutput(1);
              super.setText(LocaleKBCT.GetString("THEN") + " " + input.GetName());
            }
            if( (column > (NbIn+2)) && (column < (NbIn+NbOut+2)) ) {
              input = kbct.GetOutput(column-NbIn-1);
              super.setText(input.GetName());
            }
            //super.setForeground(Color.blue);
            super.setForeground(Color.yellow);
            super.setBackground(Color.black);
          } catch( Throwable t ) {
              MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JRuleFrame en jbInitTables: "+t);
          }
          return this;
          }
        });
    jTableRules.getTableHeader().setReorderingAllowed(false);
    jTableRules.getTableHeader().setResizingAllowed(true);
    jTableRules.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jPanelRules.getViewport().add(jTableRules);
    int lim= NbIn+NbOut+2;
    for (int n=0; n<lim; n++)
         SetUpInitColumns(n);

    InitColumnSizes();
    jTableRules.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    this.getContentPane().setLayout(new GridBagLayout());
    this.getContentPane().add(jPanelRules, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
  }
//------------------------------------------------------------------------------
  private void Events() {
    jMenuPrint.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuPrint_actionPerformed(); }} );
    jMenuExport.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExport_actionPerformed(); }} );
    jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuClose_actionPerformed(); }} );
  }
//------------------------------------------------------------------------------
  public void Translate() throws Throwable {
    jMenuPrint.setText(LocaleKBCT.GetString("Print"));
    jMenuExport.setText(LocaleKBCT.GetString("Export"));
    jMenuClose.setText(LocaleKBCT.GetString("Close"));
    this.setTitle(LocaleKBCT.GetString("Inference")+": "+LocaleKBCT.GetString("Rules"));
    this.jTableRules.getTableHeader().repaint();
    this.repaint();
  }
//------------------------------------------------------------------------------
  public void dispose() {
    super.dispose();
    this.JRuleFrameOpen.Release();
    this.kbct.Close();
    this.kbct.Delete();    
  }
//------------------------------------------------------------------------------
  private void SetUpInitColumns(int column) throws Throwable {
    jTableRules.getColumnModel().getColumn(column).setCellRenderer( new DefaultTableCellRenderer() {
   	  static final long serialVersionUID=0;	
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        Rule r= kbct.GetRule(row+1);
        String RuleType= r.GetType();
        boolean RuleActive= r.GetActive();
        if (!RuleActive) {
          super.setForeground(Color.yellow);
          super.setBackground(Color.yellow);
          return this;
        }
        if (RuleType.equals("I"))
          super.setForeground(Color.red);
        else if (RuleType.equals("E"))
          super.setForeground(Color.black);
        else if (RuleType.equals("S"))
          super.setForeground(Color.blue);

        if ( (column==0) || (column==1) )
          super.setBackground(Color.green);
        else
          super.setBackground(Color.white);

        return this;
      }
    });
  }
//------------------------------------------------------------------------------
  private int CellWidth( TableColumnModel columns, int column ) {
    if( this.kbct.GetNbRules() == 0 )
      return 0;

    try { return columns.getColumn(column).getCellEditor().getTableCellEditorComponent(this.jTableRules, this.jTableRules.getModel().getValueAt(0, column), false, 0, column).getPreferredSize().width; }
    catch (NullPointerException e) { return jTableRules.getDefaultEditor(columns.getClass()).getTableCellEditorComponent(this.jTableRules, this.jTableRules.getModel().getValueAt(0, column), false, 0, column).getPreferredSize().width; }
  }
//------------------------------------------------------------------------------
  private int HeaderWidth( TableColumnModel columns, int column ) {
    try { return columns.getColumn(column).getHeaderRenderer().getTableCellRendererComponent(this.jTableRules, columns.getColumn(column).getHeaderValue(), false, false, 0, column).getPreferredSize().width; }
    catch (NullPointerException e) { return jTableRules.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(this.jTableRules, columns.getColumn(column).getHeaderValue(), false, false, 0, column).getPreferredSize().width; }
  }
//------------------------------------------------------------------------------
  private void InitColumnSizes() {
    TableColumnModel columns = jTableRules.getColumnModel();
    columns.getColumn(0).setPreferredWidth(HeaderWidth(columns, 0)+30);
    columns.getColumn(1).setPreferredWidth(HeaderWidth(columns, 1)+30);
    for( int i=2 ; i<this.jTableRules.getColumnCount() ; i++ )
      columns.getColumn(i).setPreferredWidth(Math.max(HeaderWidth(columns,i),CellWidth(columns,i))+30);
  }
//------------------------------------------------------------------------------
  void jMenuPrint_actionPerformed() {
    try {
      Printable p= new Printable() {
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
          if (pageIndex >= 1)
            return Printable.NO_SUCH_PAGE;
          else
            return JRuleFrameInfer.this.print(graphics, pageFormat);
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
          JRuleFrameInfer.this.jPanelRules.paint(g);
          g.translate(0, JRuleFrameInfer.this.jPanelRules.getHeight());
          JRuleFrameInfer.this.jPanelRules.paint(g);
          g.setColor(Color.gray);
          g.drawLine(0,0,0, JRuleFrameInfer.this.jPanelRules.getHeight()-1);
        }
      };
      panel.setSize(new Dimension(this.jPanelRules.getWidth(), this.jPanelRules.getHeight()));
      export.showExportDialog( panel, "Export view as ...", panel, "export" );
    } catch (Exception ex) { MessageKBCT.Error(null, ex); }
  }
//------------------------------------------------------------------------------
  void jMenuClose_actionPerformed() { this.dispose(); }
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
     this.jPanelRules.print( g2 );
     return Printable.PAGE_EXISTS;
 }
}