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
//                              JVariableFrame.java
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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import kbct.JKBCT;
import kbct.JKBCTInput;
import kbct.JVariable;
import kbct.KBCTListener;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbctAux.DoubleField;
import kbctAux.IntegerField;
import kbctAux.MessageKBCT;

import org.freehep.util.export.ExportDialog;

import print.JPrintPreview;
import KB.LabelKBCT;
import KB.variable;
import fis.JSemaphore;

/**
 * kbctFrames.JVariableFrame generates a window with all information about one variable.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
public class JVariableFrame extends JChildFrame {
  static final long serialVersionUID=0;	
  protected JExpertFrame parent;
  protected JKBCT kbct;
  protected JSemaphore Semaphore;
  protected int Number;
  protected JVariable Temp;
  protected KBCTListener KBCTListener;
  protected JPanel PanelInput = new JPanel(new GridBagLayout());
  // menus
  protected JMenuBar jMenuBarInput = new JMenuBar();
  // menu range
  protected JMenu jMenuRange = new JMenu();
  protected JMenuItem jMenuPhysicalRange = new JMenuItem();
  protected JMenuItem jMenuInterestRange = new JMenuItem();
  // menu print
  protected JMenuItem jMenuPrint = new JMenuItem();
  // menu export
  protected JMenuItem jMenuExport= new JMenuItem();
  // menu close
  protected JMenuItem jMenuClose= new JMenuItem();
  // menu help
  protected JMenuItem jMenuHelp= new JMenuItem();
  // nom
  protected JLabel jLabelName = new JLabel();
  protected JTextField jTFName = new JTextField();
  // tipo
  protected JLabel jLabelType = new JLabel();
  protected DefaultComboBoxModel jDCBMTypeModel = new DefaultComboBoxModel() {
    static final long serialVersionUID=0;	
    public Object getElementAt(int index) {
        String Type;
        switch(index) {
          case 0:  Type = "numerical"; break;
          case 1:  Type = "logical"; break;
          case 2:  Type = "categorical"; break;
          default: Type = "categorical"; break;
        }
        try { return LocaleKBCT.GetString(Type); }
        catch( Throwable t ) { return null; }
      }
    };
  protected JComboBox jCBType = new JComboBox(jDCBMTypeModel);
  // trust
  protected JLabel jLabelImp = new JLabel();
  protected DefaultComboBoxModel jDCBMImpModel = new DefaultComboBoxModel() {
    static final long serialVersionUID=0;	
    public Object getElementAt(int index) {
        String Imp;
        switch(index) {
          case 0:  Imp = "llow"; break;
          case 1:  Imp = "middle"; break;
          case 2:  Imp = "hhigh"; break;
          default: Imp = "llow"; break;
        }
        try { return LocaleKBCT.GetString(Imp); }
        catch( Throwable t ) { return null; }
      }
    };
  protected JComboBox jCBImp = new JComboBox(jDCBMImpModel);
  // panel physical range
  protected JPanel jPanelRangeP = new JPanel(new GridBagLayout());
  protected TitledBorder titledBorderRangeP;
  protected JLabel jLabelLowerP = new JLabel();
  protected DoubleField dfLowerP = new DoubleField();
  protected JLabel jLabelUpperP = new JLabel();
  protected DoubleField dfUpperP = new DoubleField();
  // panel interest range
  protected JPanel jPanelRangeI = new JPanel(new GridBagLayout());
  protected TitledBorder titledBorderRangeI;
  protected JLabel jLabelLowerI = new JLabel();
  protected DoubleField dfLowerI = new DoubleField();
  protected JLabel jLabelUpperI = new JLabel();
  protected DoubleField dfUpperI = new DoubleField();
  // labels
  protected JPanel jPanelLabels= new JPanel();
  protected JPanel jPanelTable = new JPanel(new GridBagLayout());
  // buttons
  protected JPanel jPanelButtons = new JPanel(new GridBagLayout());
  protected TitledBorder titledBorderPanelTable;
  // number of labels
  protected JLabel jLabelNOL = new JLabel();
  protected DefaultComboBoxModel jDCBMNOLModel = new DefaultComboBoxModel() {
    static final long serialVersionUID=0;	
    public Object getElementAt(int index) {
        String NOL;
        switch(index) {
          case 0: NOL = "2"; break;
          case 1: NOL = "3"; break;
          case 2: NOL = "4"; break;
          case 3: NOL = "5"; break;
          case 4: NOL = "6"; break;
          case 5: NOL = "7"; break;
          case 6: NOL = "8"; break;
          case 7: NOL = "9"; break;
          default:NOL = "3"; break;
        }
        try { return LocaleKBCT.GetString(NOL); }
        catch( Throwable t ) { return null; }
      }
    };
  protected JComboBox jCBNOL = new JComboBox(jDCBMNOLModel);
  // Linguistic labels
  protected JLabel jLabelLL = new JLabel();
  protected DefaultComboBoxModel jDCBMLLModel = new DefaultComboBoxModel() {
    static final long serialVersionUID=0;	
    public Object getElementAt(int index) {
        String NOL;
        switch(index) {
          case 0: NOL = "small-large"; break;
          case 1: NOL = "few-lot"; break;
          case 2: NOL = "low-high"; break;
          case 3: NOL = "negative-positive"; break;
          case 4: NOL = "user"; break;
          default:NOL = "low-high"; break;
        }
        try { return LocaleKBCT.GetString(NOL); }
        catch( Throwable t ) { return null; }
      }
    };
  protected JComboBox jCBLL = new JComboBox(jDCBMLLModel);
  // new scale
  protected JButton jButtonNewScale = new JButton();
  // set number of labels (NbLab > 9)
  protected JButton jButtonSetNbLabels = new JButton();
  // change name
  protected JButton jButtonChangeName = new JButton();
  // modal points
  protected JButton jButtonModalPoints = new JButton();
  // advanced options
  protected JButton jButtonAdvancedOptions = new JButton();

  protected JTable jt;
  /*
  *  Flags para solo lanzar aviso de usuario experto la primera vez
  *  que el usuario pulse el botón (Botones de la ventana de Variable).
  */
  protected static boolean JButtonNewScalePressed= false;
  protected static boolean JButtonSetNbLabelsPressed= false;
  protected static boolean JButtonChangeNamePressed= false;
  private static boolean JButtonModalPointsPressed= false;
  protected static boolean JButtonAdvancedOptionsPressed= false;
  protected static int AuthorizationButtonNewScale= JOptionPane.NO_OPTION;
  protected static int AuthorizationButtonSetNbLabels= JOptionPane.NO_OPTION;
  protected static int AuthorizationButtonChangeName= JOptionPane.NO_OPTION;
  private static int AuthorizationButtonModalPoints= JOptionPane.NO_OPTION;
  protected static int AuthorizationButtonAdvancedOptions= JOptionPane.NO_OPTION;
  protected boolean AdvChangeLabel= false;
  protected boolean ClassifFLAG= false;
  // classif (only for outputs)
  protected JLabel jLabelClassif = new JLabel();
  protected DefaultComboBoxModel jDCBMClassifModel = new DefaultComboBoxModel() {
    static final long serialVersionUID=0;	
    public Object getElementAt(int index) {
        String Classif;
        switch(index) {
          case 0:  Classif= "yes"; break;
          case 1:  Classif= "no"; break;
          default: Classif= "yes"; break;
        }
        try { return LocaleKBCT.GetString(Classif); }
        catch( Throwable t ) { return null; }
      }
    };
  protected JComboBox jCBClassif = new JComboBox(jDCBMClassifModel);
  protected int UserNbLabels;
  protected IntegerField jNbLabels = new IntegerField();
  protected boolean output= false;
  
//------------------------------------------------------------------------------
  protected JVariableFrame( JExpertFrame parent ) { super( parent ); }
//------------------------------------------------------------------------------
  public int print(java.awt.Graphics g, PageFormat pageFormat) throws PrinterException {
     //System.out.println("JVariableFrame: print");
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
     this.PanelInput.print( g2 );
     return Printable.PAGE_EXISTS;
 }
//------------------------------------------------------------------------------
  protected void InitComponents() {
    this.getContentPane().setLayout(new GridBagLayout());
    this.getContentPane().add(this.PanelInput);
    // panel header
    JPanel jPanelHeader = new JPanel(new GridBagLayout());
    this.PanelInput.add(jPanelHeader, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets( 0, 0, 0, 0), 0, 0));
    // label nom
    jPanelHeader.add(jLabelName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 0, 0));
    // jTextField nom
    jPanelHeader.add(jTFName, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 20, 0));
    // Type
    jPanelHeader.add(jLabelType, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 0, 0));
    for( int i=0 ; i<3 ; i++ )
      this.jDCBMTypeModel.addElement(new String());

    jPanelHeader.add(jCBType, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
    // Imp
    jPanelHeader.add(jLabelImp, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 0, 0));
    for( int i=0 ; i<3 ; i++ )
        this.jDCBMImpModel.addElement(new String());

    jPanelHeader.add(jCBImp, new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
    // Classif
    if (this.ClassifFLAG) {
      jPanelHeader.add(jLabelClassif, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 0, 0));
      for( int i=0 ; i<2 ; i++ )
         this.jDCBMClassifModel.addElement(new String());

      jPanelHeader.add(jCBClassif, new GridBagConstraints(3, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
    }
    // panel physical range
    titledBorderRangeP = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(134, 134, 134)),"");
    jPanelRangeP.setBorder(titledBorderRangeP);
    jPanelRangeP.add(jLabelLowerP, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    jPanelRangeP.add(dfLowerP, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 50, 0));
    jPanelRangeP.add(jLabelUpperP, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 0, 0));
    jPanelRangeP.add(dfUpperP, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 50, 0));
    this.PanelInput.add(jPanelRangeP, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
    // panel interest range
    titledBorderRangeI = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    jPanelRangeI.setBorder(titledBorderRangeI);
    jPanelRangeI.add(jLabelLowerI, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    jPanelRangeI.add(dfLowerI, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 50, 0));
    jPanelRangeI.add(jLabelUpperI, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 0, 0));
    jPanelRangeI.add(dfUpperI, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 50, 0));
    this.PanelInput.add(jPanelRangeI, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
    // PanelTable
    titledBorderPanelTable = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    int NOL= 3;
    try { NOL = this.Temp.GetLabelsNumber(); }
    catch (Throwable T) {
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JVariableFrame: InitComponents 1: "+T);
    }
    Vector Title= new Vector();
    for (int n=0; n<NOL; n++)
      Title.add(LocaleKBCT.GetString("Label")+" "+String.valueOf(n+1));

    Vector Data= new Vector();
    Data.add(Title);
    String[] LabelsNames= null;
    if (!this.Temp.GetScaleName().equals("user"))
      LabelsNames= this.Temp.GetLabelsName();
    else
      LabelsNames= this.Temp.GetUserLabelsName();

    Vector v= new Vector();
    for (int n=0; n<NOL; n++) {
      if (!this.Temp.GetScaleName().equals("user"))
        v.add(LocaleKBCT.GetString(LabelsNames[n]));
      else
        v.add(LabelsNames[n]);
    }
    Data.add(v);

    jt= new JTable(Data,Title);
    this.SetUpTable();
    this.InitColumnSizes();
    jPanelTable.setBorder(titledBorderPanelTable);
    jPanelTable.add(jPanelLabels, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets (5, 5, 5, 5), 0, 0));
    jPanelLabels.add(jLabelNOL, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    for( int i=0 ; i<8 ; i++ )
        this.jDCBMNOLModel.addElement(new String());

    jPanelLabels.add(jCBNOL, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanelLabels.add(jLabelLL, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    for( int i=0 ; i<5 ; i++ )
        this.jDCBMLLModel.addElement(new String());

    jPanelLabels.add(jCBLL, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanelTable.add(jPanelLabels, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets (5, 5, 5, 5), 0, 0));
    jPanelTable.add(jt, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    jPanelTable.add(jPanelButtons, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    jPanelButtons.add(jButtonNewScale, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    jPanelButtons.add(jButtonChangeName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    jPanelButtons.add(jButtonSetNbLabels, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    jPanelButtons.add(jButtonModalPoints, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    jPanelButtons.add(jButtonAdvancedOptions, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    this.PanelInput.add(jPanelTable, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
  }
//------------------------------------------------------------------------------
  protected void jbInit() {
    // menus
    this.setJMenuBar(this.jMenuBarInput);
    // menu range
    jMenuBarInput.add(jMenuRange);
      jMenuRange.add(jMenuPhysicalRange);
      jMenuRange.add(jMenuInterestRange);
    // menu print
    jMenuBarInput.add(jMenuPrint);
    // menu export
    jMenuBarInput.add(jMenuExport);
    // menu help
    jMenuBarInput.add(jMenuHelp);
    // menu close
    jMenuBarInput.add(jMenuClose);

    jLabelImp.setToolTipText(LocaleKBCT.GetString("ExpertTrust1"));
    String Imp= this.Temp.GetTrust();
    if (Imp.equals(LocaleKBCT.GetString("hhigh")))
      jCBImp.setToolTipText(LocaleKBCT.GetString("ExpertTrust2"));
    else if (Imp.equals(LocaleKBCT.GetString("average")))
      jCBImp.setToolTipText(LocaleKBCT.GetString("ExpertTrust3"));
    else if (Imp.equals(LocaleKBCT.GetString("llow")))
      jCBImp.setToolTipText(LocaleKBCT.GetString("ExpertTrust4"));

    this.InitComponents();
    this.dfLowerP.setEnabled(false);
    this.dfUpperP.setEnabled(false);
    this.dfLowerI.setEnabled(false);
    this.dfUpperI.setEnabled(false);
    this.jt.setEnabled(false);
    this.jMenuPhysicalRange.setEnabled(false);
    this.jMenuInterestRange.setEnabled(false);
    if (! this.Temp.GetType().equals("numerical")) {
      this.jButtonAdvancedOptions.setEnabled(false);
      this.jButtonModalPoints.setEnabled(false);
    }
    if (! this.Temp.GetScaleName().equals("user")) {
      this.jButtonChangeName.setEnabled(false);
      this.jButtonSetNbLabels.setEnabled(false);
    }
  }
//------------------------------------------------------------------------------
  private void SetUpTable() {
    for( int i=0 ; i<this.jt.getColumnCount() ; i++ )
    jt.getColumnModel().getColumn(i).setCellRenderer( new DefaultTableCellRenderer() {
   	  static final long serialVersionUID=0;	
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        super.setForeground(Color.BLUE);
        if (row==0)
            super.setBackground(Color.GREEN);
        else
            super.setBackground(Color.WHITE);

        this.setToolTipText((String)value);
        return this;
       }
    });
  }
//------------------------------------------------------------------------------
  private int HeaderWidth( TableColumnModel columns, int column ) {
    try { return columns.getColumn(column).getHeaderRenderer().getTableCellRendererComponent(this.jt, columns.getColumn(column).getHeaderValue(), false, false, 0, column).getPreferredSize().width; }
    catch (NullPointerException e) { return jt.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(this.jt, columns.getColumn(column).getHeaderValue(), false, false, 0, column).getPreferredSize().width; }
  }
//------------------------------------------------------------------------------
  private int CellWidth( TableColumnModel columns, int column ) {
    if( this.kbct.GetNbRules() == 0 )
      return 0;
    try { return columns.getColumn(column).getCellEditor().getTableCellEditorComponent(this.jt, this.jt.getModel().getValueAt(0, column), false, 0, column).getPreferredSize().width; }
    catch (NullPointerException e) { return jt.getDefaultEditor(columns.getClass()).getTableCellEditorComponent(this.jt, this.jt.getModel().getValueAt(0, column), false, 0, column).getPreferredSize().width; }
  }
//------------------------------------------------------------------------------
  private void InitColumnSizes() {
    TableColumnModel columns = jt.getColumnModel();
    int sum=0;
    for( int i=0 ; i<this.jt.getColumnCount() ; i++ ) {
        int w=Math.max(HeaderWidth(columns,i),CellWidth(columns,i))+60;
    	columns.getColumn(i).setPreferredWidth(w);
    	sum=sum+w;
    }
    Dimension dim = getToolkit().getScreenSize();
    if (sum+100 > dim.width) {
        int w= 2*dim.width/(3*this.jt.getColumnCount());
    	for( int i=0 ; i<this.jt.getColumnCount() ; i++ ) {
        	columns.getColumn(i).setPreferredWidth(w);
        }
    }
  }
//------------------------------------------------------------------------------
  public void Show() throws Throwable {
    jbInit();
    InitObjectsWithTemp();
    Translate();
    Events();
    this.pack();
    this.setLocation(this.ChildPosition(this.getSize()));
    if( this.Semaphore != null )
      this.Semaphore.Acquire();

    this.setVisible(true);
  }
//------------------------------------------------------------------------------
  protected void TranslateComponents() {
    jLabelName.setText(LocaleKBCT.GetString("Name")+":");
    jLabelType.setText(LocaleKBCT.GetString("Type")+":");
    jLabelImp.setText(LocaleKBCT.GetString("Imp")+":");
    jLabelClassif.setText(LocaleKBCT.GetString("Classif")+":");
    jLabelNOL.setText(LocaleKBCT.GetString("NOL")+":");
    jLabelLL.setText(LocaleKBCT.GetString("LL")+":");
    titledBorderRangeP.setTitle(LocaleKBCT.GetString("RangeP"));
    titledBorderRangeI.setTitle(LocaleKBCT.GetString("RangeI"));
    jLabelLowerP.setText(LocaleKBCT.GetString("Lower")+":");
    jLabelUpperP.setText(LocaleKBCT.GetString("Upper")+":");
    jLabelLowerI.setText(LocaleKBCT.GetString("Lower")+":");
    jLabelUpperI.setText(LocaleKBCT.GetString("Upper")+":");
    titledBorderPanelTable.setTitle(LocaleKBCT.GetString("Labels"));
    jButtonNewScale.setText(LocaleKBCT.GetString("NewScale"));
    jButtonSetNbLabels.setText(LocaleKBCT.GetString("SetNbLabels"));
    jButtonChangeName.setText(LocaleKBCT.GetString("ChangeName"));
    jButtonModalPoints.setText(LocaleKBCT.GetString("ModalPoints"));
    jButtonAdvancedOptions.setText(LocaleKBCT.GetString("AdvOpt"));
  }
//------------------------------------------------------------------------------
  public void Translate() throws Throwable {
    // menu range
    jMenuRange.setText(LocaleKBCT.GetString("Range"));
      jMenuPhysicalRange.setText(LocaleKBCT.GetString("RangeP"));
      jMenuInterestRange.setText(LocaleKBCT.GetString("RangeI"));
    // menu print
    jMenuPrint.setText(LocaleKBCT.GetString("Print"));
    // menu export
    jMenuExport.setText(LocaleKBCT.GetString("Export"));
    // menu close
    jMenuClose.setText(LocaleKBCT.GetString("Close"));
    // menu help
    jMenuHelp.setText(LocaleKBCT.GetString("Help"));

    this.TranslateComponents();
    this.dfLowerP.setLocale(LocaleKBCT.Locale());
    this.dfLowerP.setValue(this.Temp.GetInputPhysicalRange()[0]);
    this.dfUpperP.setLocale(LocaleKBCT.Locale());
    this.dfUpperP.setValue(this.Temp.GetInputPhysicalRange()[1]);
    this.dfLowerI.setLocale(LocaleKBCT.Locale());
    this.dfLowerI.setValue(this.Temp.GetInputInterestRange()[0]);
    this.dfUpperI.setLocale(LocaleKBCT.Locale());
    this.dfUpperI.setValue(this.Temp.GetInputInterestRange()[1]);
    this.repaint();
  }
//------------------------------------------------------------------------------
  protected void InitObjectsWithTemp() {
    this.jTFName.setText(this.Temp.GetName());
    this.jDCBMTypeModel.setSelectedItem(LocaleKBCT.GetString(this.Temp.GetType()));
    String Imp= this.Temp.GetTrust();
    this.jDCBMImpModel.setSelectedItem(LocaleKBCT.GetString(Imp));
    this.jDCBMClassifModel.setSelectedItem(LocaleKBCT.GetString(this.Temp.GetClassif()));
    this.jDCBMLLModel.setSelectedItem(LocaleKBCT.GetString(this.Temp.GetScaleName()));
    try {
    	int NOL= this.Temp.GetLabelsNumber();
        if (NOL <= 9)
    	    this.jDCBMNOLModel.setSelectedItem(LocaleKBCT.GetString("" +NOL+ ""));
        else
    	    this.jDCBMNOLModel.setSelectedItem("" +NOL+ "");
    } catch (Throwable t) {
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JVariableFrame: InitObjectsWithTemp: "+t);
    }
    this.dfLowerP.setValue(this.Temp.GetInputPhysicalRange()[0]);
    this.dfUpperP.setValue(this.Temp.GetInputPhysicalRange()[1]);
    this.dfLowerI.setValue(this.Temp.GetInputInterestRange()[0]);
    this.dfUpperI.setValue(this.Temp.GetInputInterestRange()[1]);
  }
//------------------------------------------------------------------------------
  protected void Events() {
    jMenuRange.addMenuListener(new MenuListener() {
      public void menuSelected(MenuEvent e) { jMenuRange_menuSelected(); }
      public void menuDeselected(MenuEvent e) {}
      public void menuCanceled(MenuEvent e) {}
    });
    jMenuPhysicalRange.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuPhysicalRange_actionPerformed(); } });
    jMenuInterestRange.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuInterestRange_actionPerformed(); } });
    jMenuPrint.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuPrint_actionPerformed(); } });
    jMenuExport.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExport_actionPerformed(); } });
    jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuClose_actionPerformed(); } });
    jMenuHelp.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuHelp_actionPerformed(); } });
    jTFName.addFocusListener(new FocusAdapter() { public void focusLost(FocusEvent e) { jTFName_focusLost(); } });
    jTFName.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jTFName_actionPerformed(); } });
    jCBType.addItemListener(new ItemListener() { public void itemStateChanged(ItemEvent e) { jType_itemStateChanged(e); }});
    jCBImp.addItemListener(new ItemListener() { public void itemStateChanged(ItemEvent e) { jImp_itemStateChanged(e); }});
    jCBClassif.addItemListener(new ItemListener() { public void itemStateChanged(ItemEvent e) { jClassif_itemStateChanged(e); }});
    jCBNOL.addItemListener(new ItemListener() { public void itemStateChanged(ItemEvent e) { jNOL_itemStateChanged(e); }});
    jCBLL.addItemListener(new ItemListener() { public void itemStateChanged(ItemEvent e) { jLL_itemStateChanged(e); }});
    jButtonNewScale.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonNewScale_actionPerformed(); }} );
    jButtonSetNbLabels.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonSetNbLabels_actionPerformed(); }} );
    jButtonChangeName.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonChangeName_actionPerformed(); }} );
    jButtonAdvancedOptions.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonAdvOpt_actionPerformed(); }} );
    jButtonModalPoints.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonModalPoints_actionPerformed(); }} );
    this.KBCTListener = new KBCTListener() {
      public void KBCTClosed() { JVariableFrame.this.dispose(); }
      public void InputRemoved( int input_number ) {}
      public void OutputRemoved( int output_number ) {}
      public void InputAdded( int input_number ) {}
      public void OutputAdded( int output_number ) {}
      public void InputReplaced( int input_number ) {
       try {
         JInputFrame jif = new JInputFrame(JVariableFrame.this.parent,
                                             JVariableFrame.this.kbct,
                                             JVariableFrame.this.Number);
         jif.Show();
         if (JVariableFrame.this.AdvChangeLabel) {
           jif.jButtonAdvOpt_actionPerformed();
           JVariableFrame.this.AdvChangeLabel= false;
         }
         JVariableFrame.this.dispose();
       } catch (Throwable t) {
           //MessageKBCT.Error(null, LocaleKBCT.GetString("Error"),"InputReplaced: "+t);
       }
      }
      public void OutputReplaced( int output_number ) {
        try {
          JOutputFrame jof = new JOutputFrame(JVariableFrame.this.parent,
                                              JVariableFrame.this.kbct,
                                              JVariableFrame.this.Number);
          jof.Show();
          if (JVariableFrame.this.AdvChangeLabel) {
            jof.jButtonAdvOpt_actionPerformed();
            JVariableFrame.this.AdvChangeLabel= false;
          }
          JVariableFrame.this.dispose();
        } catch (Throwable t) {
          //MessageKBCT.Error(null, LocaleKBCT.GetString("Error"),"OutputReplaced: "+t);
        }
      }
      public void InputActiveChanged( int input_number ) {}
      public void OutputActiveChanged( int output_number ) {}
      public void InputNameChanged( int input_number ) {}
      public void OutputNameChanged( int output_number ) {}
      public void InputPhysicalRangeChanged( int input_number ) {}
      public void InputInterestRangeChanged( int input_number ) {}
      public void OutputPhysicalRangeChanged( int output_number ) {}
      public void OutputInterestRangeChanged( int output_number ) {}
      public void MFRemovedInInput( int input_number, int mf_number ) {
        JVariableFrame.this.AdvChangeLabel= true;
      }
      public void MFRemovedInOutput( int output_number, int mf_number ) {
        JVariableFrame.this.AdvChangeLabel= true;
      }
      public void MFAddedInInput( int input_number ) {
        int NOL= JVariableFrame.this.Temp.GetLabelsNumber();
        for (int n=0; n<NOL-1; n++) {
          if (!JVariableFrame.this.Temp.GetScaleName().equals("user"))
            JVariableFrame.this.Temp.SetUserLabelsName(n+1, LocaleKBCT.GetString(JVariableFrame.this.Temp.GetLabelsName(n)));
          else
            JVariableFrame.this.Temp.SetUserLabelsName(n+1, JVariableFrame.this.Temp.GetUserLabelsName(n));
        }
        JVariableFrame.this.AdvChangeLabel= true;
      }
      public void MFAddedInOutput( int output_number ) {
        int NOL= JVariableFrame.this.Temp.GetLabelsNumber();
        for (int n=0; n<NOL-1; n++) {
          if (!JVariableFrame.this.Temp.GetScaleName().equals("user"))
            JVariableFrame.this.Temp.SetUserLabelsName(n+1, LocaleKBCT.GetString(JVariableFrame.this.Temp.GetLabelsName(n)));
          else
            JVariableFrame.this.Temp.SetUserLabelsName(n+1, JVariableFrame.this.Temp.GetUserLabelsName(n));
        }
        JVariableFrame.this.AdvChangeLabel= true;
      }
      public void MFReplacedInInput( int input_number ) {JVariableFrame.this.AdvChangeLabel= true;}
      public void MFReplacedInOutput( int output_number ) {JVariableFrame.this.AdvChangeLabel= true;}
      public void OutputDefaultChanged( int output_number ) {}
      public void RulesModified() {}
      };
    this.kbct.AddKBCTListener(this.KBCTListener);
  }
//------------------------------------------------------------------------------
  void jMenuRange_menuSelected() {
    if( this.Temp.GetV().GetType().equals("numerical") ) {
      this.jMenuPhysicalRange.setEnabled(true);
      this.jMenuInterestRange.setEnabled(true);
    } else {
        this.jMenuPhysicalRange.setEnabled(false);
        this.jMenuInterestRange.setEnabled(false);
    }
  }
//------------------------------------------------------------------------------
  public void dispose() {
      if (this.Semaphore != null)
        this.Semaphore.Release();

      if (this.KBCTListener != null)
        this.kbct.RemoveKBCTListener(this.KBCTListener);

      super.dispose();
  }
//------------------------------------------------------------------------------
  void jType_itemStateChanged(ItemEvent e) { }
//------------------------------------------------------------------------------
  void jImp_itemStateChanged(ItemEvent e) {
    if( e.getStateChange() == ItemEvent.SELECTED ) {
      try {
        switch (this.jCBImp.getSelectedIndex()) {
          case 0: this.Temp.SetTrust("llow");
                  jCBImp.setToolTipText(LocaleKBCT.GetString("ExpertTrust4"));
                  break;
          case 1: this.Temp.SetTrust("middle");
                  jCBImp.setToolTipText(LocaleKBCT.GetString("ExpertTrust3"));
                  break;
          case 2: this.Temp.SetTrust("hhigh");
                  jCBImp.setToolTipText(LocaleKBCT.GetString("ExpertTrust2"));
                  break;
        }
      } catch (Throwable t) {
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JVariableFrame: Imp_itemStateChanged: "+t);
      }
    }
  }
//------------------------------------------------------------------------------
  void jClassif_itemStateChanged(ItemEvent e) {
    if( e.getStateChange() == ItemEvent.SELECTED ) {
      try {
    	  //System.out.println("this.jCBClassif.getSelectedIndex(): "+this.jCBClassif.getSelectedIndex());
          switch (this.jCBClassif.getSelectedIndex()) {
            case 0: this.Temp.SetClassif("yes");
                    break;
            case 1: this.Temp.SetClassif("no");
                    break;
          }
        } catch (Throwable t) {
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JVariableFrame: Classif_itemStateChanged: "+t);
        }
     }
  }
//------------------------------------------------------------------------------
  void jNOL_itemStateChanged(ItemEvent e) {}
//------------------------------------------------------------------------------
  void jLL_itemStateChanged(ItemEvent e) {}
//------------------------------------------------------------------------------
  void jButtonNewScale_actionPerformed() {}
//------------------------------------------------------------------------------
  void jButtonSetNbLabels_actionPerformed() {}
//------------------------------------------------------------------------------
  void jButtonChangeName_actionPerformed() {}
//------------------------------------------------------------------------------
  void jButtonModalPoints_actionPerformed() {
    if (!JVariableFrame.JButtonModalPointsPressed || JVariableFrame.AuthorizationButtonModalPoints==JOptionPane.NO_OPTION) {
    	JVariableFrame.JButtonModalPointsPressed = true;
    	JVariableFrame.AuthorizationButtonModalPoints= MessageKBCT.Confirm(this, LocaleKBCT.GetString("OnlyExpertUsers") + "\n" + LocaleKBCT.GetString("DoYouWantToContinue"), 1, false, false, false);
    }
    if ( (JVariableFrame.AuthorizationButtonModalPoints==JOptionPane.YES_OPTION) && (JVariableFrame.JButtonModalPointsPressed) ) {
        try {
          JModalPointsFrame jmpf = new JModalPointsFrame(this, this.Temp, "ModalPoints");
          jmpf.setLocation(JChildFrame.ChildPosition(this, jmpf.getSize()));
          double[] MP= jmpf.Show();
          //for (int n=0; n<MP.length; n++) {
          //     System.out.println("mp["+n+"]="+MP[n]);
          //}
          if (MP != null) {
        	int NOL= this.Temp.GetLabelsNumber();
            int[] NOMPlab= new int[NOL];
            for (int n=0; n<NOL; n++) {
            	   int nbpar= this.Temp.GetLabel(n+1).GetParams().length;
            	   if (nbpar==4) {
            		   NOMPlab[n]= 2;
            	   } else {
            		   NOMPlab[n]= 1;
            	   }
            }
            DecimalFormat df= new DecimalFormat();
            df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
            df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
            DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
            dfs.setDecimalSeparator((new String(".").charAt(0)));
            df.setDecimalFormatSymbols(dfs);
            df.setGroupingSize(20);
            int cont=0;
            for (int n=0; n<NOL; n++) {
            	if (NOMPlab[n]==1) {
                    String aux="No MP";
                    if (MP[cont] != this.Temp.GetInputInterestRange()[0]-1)
                        aux = df.format(new Double(MP[cont]));

                    if (MP[cont] >= this.Temp.GetInputInterestRange()[0]) {
                        this.Temp.SetMP(n+1, aux, false);
                        variable var= this.Temp.GetV();
          	            LabelKBCT et= var.GetLabel(n+1);
          	            if (et.GetParams().length==3) {
              	            et.SetP2(MP[cont]);
                            var.ReplaceLabel(n+1,et);
                            if (n==0) {
              	                LabelKBCT post= var.GetLabel(2);
               	                post.SetP1(MP[cont]);
                                var.ReplaceLabel(2,post);
                            } else if (n==NOL-1) {
            	                LabelKBCT ant= var.GetLabel(n);
                                if (ant.GetParams().length==3)
              	                    ant.SetP3(MP[cont]);
                                else
                	                ant.SetP4(MP[cont]);

                                var.ReplaceLabel(n,ant);
                            } else {
            	                LabelKBCT ant= var.GetLabel(n);
                                if (ant.GetParams().length==3)
                	                ant.SetP3(MP[cont]);
                                else
               	                    ant.SetP4(MP[cont]);

                                var.ReplaceLabel(n,ant);
            	                LabelKBCT post= var.GetLabel(n+2);
            	                post.SetP1(MP[cont]);
                                var.ReplaceLabel(n+2,post);
                            }
          	            }
                        this.Temp.SetV(var);
                   } else {
                         this.Temp.SetMP(n+1, aux, false);
                   }
            	} else {
                    for (int k=0; k<2; k++) {
            		  String aux="No MP";
                      if (MP[cont] != this.Temp.GetInputInterestRange()[0]-1)
                          aux = df.format(new Double(MP[cont]));

                      if (MP[cont] >= this.Temp.GetInputInterestRange()[0]) {
                          if (k==0)
                    	      this.Temp.SetMP(n+1, aux, false);
                          else
                    	      this.Temp.SetMPaux(n+1, aux);
                        	  
                          variable var= this.Temp.GetV();
          	              LabelKBCT et= var.GetLabel(n+1);
          	              if (k==0) {
              	              et.SetP2(MP[cont]);
                              var.ReplaceLabel(n+1,et);
                              if (n > 0) {
            	                  LabelKBCT ant= var.GetLabel(n);
                                  if (ant.GetParams().length==3)
              	                      ant.SetP3(MP[cont]);
                                  else
                	                  ant.SetP4(MP[cont]);

                                  var.ReplaceLabel(n,ant);
                              }
          	              } else {
              	              et.SetP3(MP[cont]);
                              var.ReplaceLabel(n+1,et);
                              if (n < NOL-1) {
              	                  LabelKBCT post= var.GetLabel(n+2);
            	                  post.SetP1(MP[cont]);
                                  var.ReplaceLabel(n+2,post);
                              }
          	              }
                          this.Temp.SetV(var);
                     } else {
                         this.Temp.SetMP(n+1, aux, false);
                     }
                     if (k==0)
                    	cont++;
            	   }
                }
            	cont++;
            }
          }
        } catch (Throwable t) {
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JVariableFrame: jButtonModalPoints_actionPerformed: "+t);
        }
    }
  }
//------------------------------------------------------------------------------
  void jButtonAdvOpt_actionPerformed() {}
//------------------------------------------------------------------------------
  protected void Replace( JKBCTInput new_input ) throws Throwable { this.kbct.ReplaceInput(this.Number, new_input); }
//------------------------------------------------------------------------------
  protected String RangeTypeP() { return new String("PhysicalRange"); }
//------------------------------------------------------------------------------
  protected String RangeTypeI() { return new String("InterestRange"); }
//------------------------------------------------------------------------------
  void jMenuPhysicalRange_actionPerformed() {
    JRangeFrame jrf = new JRangeFrame(this, this.Temp, this.RangeTypeP());
    jrf.setLocation(JChildFrame.ChildPosition(this, jrf.getSize()));
    double [] new_range = jrf.Show();
    if( new_range != null ) {
      try {
        if ((new_range[0]<=Temp.GetInputInterestRange()[0]) && (new_range[1]>=Temp.GetInputInterestRange()[1])){
          this.Temp.SetInputPhysicalRange(new_range);
          this.PhysicalRangeChanged();
          this.dfLowerP.setValue(new_range[0]);
          this.dfUpperP.setValue(new_range[1]);
        } else {throw new Throwable();}
      } catch( Throwable t ) {
        MessageKBCT.Error( this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("RangeP>RangeI"));
      }
    }
  }
//------------------------------------------------------------------------------
  void jMenuInterestRange_actionPerformed() {
    JRangeFrame jrf = new JRangeFrame(this, this.Temp, this.RangeTypeI());
    jrf.setLocation(JChildFrame.ChildPosition(this, jrf.getSize()));
    double [] new_range = jrf.Show();
    if( new_range != null ) {
      try {
        if ((new_range[0]>=Temp.GetInputPhysicalRange()[0]) && (new_range[1]<=Temp.GetInputPhysicalRange()[1])){
          this.Temp.SetInputInterestRange(new_range);
          this.InterestRangeChanged();
          this.dfLowerI.setValue(new_range[0]);
          this.dfUpperI.setValue(new_range[1]);
          LabelKBCT lfirst= this.Temp.GetLabel(1);
          lfirst.SetP1(new_range[0]);
          this.Temp.ReplaceLabel(1, lfirst);
          int NOL= this.Temp.GetLabelsNumber();
          LabelKBCT llast= this.Temp.GetLabel(NOL);
          llast.SetP3(new_range[1]);
          this.Temp.ReplaceLabel(NOL, llast);
        } else {throw new Throwable("RangeP>RangeI");}
      } catch( Throwable t ) {
        if (t.getMessage().equals("RangeP>RangeI"))
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("RangeP>RangeI"));
        else
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JVariableFrame: jMenuInterestRange_actionPerformed: "+t.getMessage()); }
    }
  }
//------------------------------------------------------------------------------
  void jMenuPrint_actionPerformed() {
    try {
      //System.out.println("JVariableFrame: jMenuPrint_actionPerformed()");
      Printable p= new Printable() {
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
          if (pageIndex >= 1)
            return Printable.NO_SUCH_PAGE;
          else
            return JVariableFrame.this.print(graphics, pageFormat);
        }
      };
      new JPrintPreview(this, p);
    } catch (Exception ex) {
    	ex.printStackTrace();
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
          JVariableFrame.this.PanelInput.paint(g);
          g.translate(0, JVariableFrame.this.PanelInput.getHeight());
          JVariableFrame.this.PanelInput.paint(g);
          g.setColor(Color.gray);
          g.drawLine(0,0,0, JVariableFrame.this.PanelInput.getHeight()-1);
        }
      };
      panel.setSize(new Dimension(this.PanelInput.getWidth(), this.PanelInput.getHeight()));
      export.showExportDialog( panel, "Export view as ...", panel, "export" );
    } catch (Exception ex) { MessageKBCT.Error(null, ex); }
  }
//------------------------------------------------------------------------------
  void jMenuClose_actionPerformed() { this.dispose(); }
//------------------------------------------------------------------------------
  void jMenuHelp_actionPerformed() { 
		if (this.output)
		    MainKBCT.setJB(new JBeginnerFrame("expert/ExpertMenuOutputs.html#OutputWindow"));
		else
	        MainKBCT.setJB(new JBeginnerFrame("expert/ExpertMenuInputs.html#InputWindow"));
		
		MainKBCT.getJB().setVisible(true);
		SwingUtilities.updateComponentTreeUI(this);
  }
//------------------------------------------------------------------------------
  protected void PhysicalRangeChanged() { this.kbct.InputPhysicalRangeChanged(this.Number); }
//------------------------------------------------------------------------------
  protected void InterestRangeChanged() { this.kbct.InputInterestRangeChanged(this.Number); }
//------------------------------------------------------------------------------
  protected void ActiveChanged() { this.kbct.InputActiveChanged(this.Number); }
//------------------------------------------------------------------------------
  void jTFName_actionPerformed() {
    this.Temp.SetName(this.jTFName.getText());
    this.NameChanged();
  }
//------------------------------------------------------------------------------
  void jTFName_focusLost() {
    this.Temp.SetName(this.jTFName.getText());
    this.NameChanged();
  }
//------------------------------------------------------------------------------
  protected void NameChanged() { this.kbct.InputNameChanged(this.Number);}
}