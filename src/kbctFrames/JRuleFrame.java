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
//                              JRuleFrame.java
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
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

import kbct.JFIS;
import kbct.JKBCT;
import kbct.JVariable;
import kbct.LocaleKBCT;
import kbct.jnikbct;
import kbctAux.MessageKBCT;

import org.freehep.util.export.ExportDialog;

import print.JPrintPreviewTable;
import KB.Rule;
import fis.JSemaphore;

/**
 * kbctFrames.JRuleFrame displays a table of rules.
 * It is used in induction of rules.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
//------------------------------------------------------------------------------
public class JRuleFrame extends JChildFrame {
  static final long serialVersionUID=0;
  private JTable jTableRules;
  private JScrollPane jPanelRules= new JScrollPane();
  private DefaultTableModel RuleModel;
  private int NbIn;
  private int NbOut;
  private Vector<String> Title;
  private Vector<Vector> Data;
  private JSemaphore JRuleFrameOpen= new JSemaphore();
  private JMenuBar jMenuBarRules= new JMenuBar();
  private JMenu jMenuOptions= new JMenu();
  private JMenuItem jMenuQuality= new JMenuItem();
  private JMenuItem jMenuSave= new JMenuItem();
  private JMenuItem jMenuPrint= new JMenuItem();
  private JMenuItem jMenuExport= new JMenuItem();
  private JMenuItem jMenuClose= new JMenuItem();
  private JMenu jMenuRules= new JMenu();
  private JMenuItem jMenuNewRule = new JMenuItem();
  private JMenuItem jMenuRemoveRule = new JMenuItem();
  private JPopupMenu jPopupMenuRules = new JPopupMenu();
    private JMenuItem jPopupNewRule = new JMenuItem();
    private JMenuItem jPopupRemoveRule = new JMenuItem();
    private JMenuItem jPopupUpRule = new JMenuItem();
    private JMenuItem jPopupDownRule = new JMenuItem();
    private JMenuItem jPopupGroupRule = new JMenuItem();
  protected JKBCT kbctjrf;
  private boolean Simplify;
  private boolean Pruned;
  private boolean Quality;
  private JRulesBaseQualityFrame jrbqf;
  private int cursor=0;
  private String firstKey= null;
  private long firstKeyPressed= -1;
  
//------------------------------------------------------------------------------
  public JRuleFrame( JKBCTFrame parent, JKBCT kbct, JSemaphore open, boolean Simplify, boolean Pruned, boolean Quality, boolean Show ) {
    super(parent);
    this.Simplify= Simplify;
    this.Pruned= Pruned;
    this.Quality= Quality;
    this.kbctjrf= new JKBCT(kbct);
    this.kbctjrf.SetKBCTFile(kbct.GetKBCTFile());
    //System.out.println(this.kbctjrf.GetNbInputs());
    //System.out.println(this.kbctjrf.GetNbOutputs());
    //System.out.println(this.kbctjrf.GetNbRules());
    this.JRuleFrameOpen = open;
    try {
      //this.kbctjrf.Save();
      //System.out.println("PTR -> "+this.kbctjrf.GetPtr());
      if (!Show)
         this.setVisible(false);

      //System.out.println("JRuleFrame: 1");
      jbInit(Show);
      //System.out.println("JRuleFrame: 2");
      if (Show){
        this.JRuleFrameOpen.Acquire();
        JKBCTFrame.AddTranslatable(this);
        this.setLocation(this.ChildPosition(this.getSize()));
        this.setVisible(true);
      }
    } catch( Throwable t ) {
    	t.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en constructor de JRuleFrame: "+t);
    }
  }
//------------------------------------------------------------------------------
  private void jbInit(boolean Show) throws Throwable {
    // menus
    this.setJMenuBar(this.jMenuBarRules);
    jMenuBarRules.add(jMenuRules);
    jMenuRules.add(jMenuNewRule);
    jMenuRules.add(jMenuRemoveRule);
    if (this.Quality)
      jMenuBarRules.add(jMenuQuality);

    jMenuBarRules.add(jMenuSave);
    jMenuBarRules.add(jMenuPrint);
    jMenuBarRules.add(jMenuExport);
    jMenuBarRules.add(jMenuClose);
    jPopupMenuRules.add(jPopupNewRule);
    jPopupMenuRules.add(jPopupRemoveRule);
    jPopupMenuRules.add(jPopupUpRule);
    jPopupMenuRules.add(jPopupDownRule);
    jPopupMenuRules.add(jPopupGroupRule);
    this.jbInitTables();
    this.Translate();
    this.Events();
    if (Show) {
      this.pack();
      this.setVisible(true);
    }
  }
//------------------------------------------------------------------------------
  private void jbInitTables() throws Throwable {
	//System.out.println("jbInitTables 1");
    NbIn = this.kbctjrf.GetNbInputs();
    NbOut = this.kbctjrf.GetNbOutputs();
    int NbRule = this.kbctjrf.GetNbRules();
    //System.out.println("NbIn: "+NbIn);
    //System.out.println("NbOut: "+NbOut);
    //System.out.println("NbRule: "+NbRule);
	//System.out.println("jbInitTables 2");
    Title = new Vector<String>();
    for( int i=0 ; i<NbIn+NbOut+2 ; i++ )
      Title.add(new String());

	//System.out.println("jbInitTables 3");
    Data = new Vector<Vector>();
    for( int rule=0 ; rule<NbRule ; rule++ ) {
      Rule jr = (Rule)this.kbctjrf.GetRule(rule+1);
      Vector rule_data = new Vector();
      rule_data.add(new Integer(rule+1));
      rule_data.add(jr.GetType());
      int[] in_labels_number= jr.Get_in_labels_number();
      for (int n=0; n<NbIn;n++)
        rule_data.add(new Integer(in_labels_number[n]));

      int[] out_labels_number= jr.Get_out_labels_number();
      for (int n=0; n<NbOut;n++)
        rule_data.add(new Integer(out_labels_number[n]));

      Data.add( rule_data );
    }
	//System.out.println("jbInitTables 4");
    jTableRules = new JTable();
    RuleModel = new DefaultTableModel() {
   	  static final long serialVersiionUID=0;
      public Class getColumnClass(int c) { return getValueAt(0, c).getClass(); }
      public boolean isCellEditable(int row, int col) {
        if((col == 0) || (col == 1))
          return false;
        else
          return true;
        }
      };
  	//System.out.println("jbInitTables 5");
    RuleModel.setDataVector(Data, Title);
    RuleModel.addTableModelListener(new TableModelListener() {
      public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        Object data = RuleModel.getValueAt(row, column);
          if( (column == 0) || (column == 1) )
            JRuleFrame.this.repaint();

          if( column >= 2 ) {
            int conc= ((Integer)data).intValue();
            int NbIn= JRuleFrame.this.NbIn;
            Rule r= JRuleFrame.this.kbctjrf.GetRule(row+1);
            if (column <= NbIn+1) {
              r.SetInputLabel(column-1, conc);
              JRuleFrame.this.kbctjrf.ReplaceRule( row+1, r);
            } else {
              r.SetOutputLabel(column-NbIn-1, conc);
              JRuleFrame.this.kbctjrf.ReplaceRule( row+1, r);
            }
          }
        }
    });
	//System.out.println("jbInitTables 6");
    jTableRules.setModel(RuleModel);
    for( int i=0 ; i<this.jTableRules.getColumnCount() ; i++ )
      this.jTableRules.getColumnModel().getColumn(i).setHeaderRenderer(new DefaultTableCellRenderer() {
   	   	static final long serialVersiionUID=0;
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
          super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
          super.setHorizontalAlignment(SwingConstants.CENTER);
          super.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
          if( column == 0 ) { super.setText(LocaleKBCT.GetString("Rule")); }
          if( column == 1 ) { super.setText(LocaleKBCT.GetString("Type")); }
          try {
        	//System.out.println(kbct.GetNbInputs());
        	//System.out.println(kbct.GetNbOutputs());
        	//System.out.println(kbct.GetNbRules());
        	 //System.out.println("NbIn: "+NbIn);
        	 //System.out.println("NbOut: "+NbOut);
        	//if (JRuleFrame.this.kbctjrf==null)
        	//    System.out.println("NULL");
            JVariable input = null;
            if( (column == 2) && (NbIn != 0) ) {
              input = JRuleFrame.this.kbctjrf.GetInput(1);
              super.setText(LocaleKBCT.GetString("If") + " " + input.GetName());
            }
            if( (column > 2) && (column < (NbIn+2)) ) {
              input = JRuleFrame.this.kbctjrf.GetInput(column-1);
              super.setText(LocaleKBCT.GetString("AND") + " " + input.GetName());
            }
            if( column == (NbIn+2) ) {
              input = JRuleFrame.this.kbctjrf.GetOutput(1);
              super.setText(LocaleKBCT.GetString("THEN") + " " + input.GetName());
            }
            if( (column > (NbIn+2)) && (column < (NbIn+NbOut+2)) ) {
              input = JRuleFrame.this.kbctjrf.GetOutput(column-NbIn-1);
              super.setText(input.GetName());
            }
            //super.setForeground(Color.blue);
            super.setForeground(Color.yellow);
            super.setBackground(Color.black);
          } catch( Throwable t ) {
        	  t.printStackTrace();
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JRuleFrame en jbInitTables: "+t);
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
    jPanelRules.getViewport().add(jTableRules);
    jPanelRules.setWheelScrollingEnabled(true);
    SetUpInitColumns(0);
    SetUpInitColumns(1);
	
    for (int n=0; n<NbIn;n++)
      SetUpColumn( jTableRules.getColumnModel().getColumn(n+2), this.kbctjrf.GetInput(n+1));

    for (int n=0; n<NbOut;n++)
      SetUpColumn( jTableRules.getColumnModel().getColumn(NbIn+n+2), this.kbctjrf.GetOutput(n+1));

    InitColumnSizes();
    if (this.cursor>0) {
      JScrollBar scrollBar= jPanelRules.getVerticalScrollBar();
      scrollBar.setMaximum(scrollBar.getMaximum()+this.cursor*50);
      scrollBar.setValue(scrollBar.getMaximum());
      jPanelRules.setVerticalScrollBar(scrollBar);
    }
    jTableRules.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    this.getContentPane().setLayout(new GridBagLayout());
    this.getContentPane().add(jPanelRules, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
  }
//------------------------------------------------------------------------------
  private void Events() {
    // menu options
    if (this.Quality)
    jMenuQuality.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuQuality_actionPerformed(); }} );
    jMenuSave.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuSave_actionPerformed(true); }} );
    jMenuPrint.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuPrint_actionPerformed(); }} );
    jMenuExport.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExport_actionPerformed(); } });
    jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuClose_actionPerformed(); }} );
    // menu rules
    jMenuRules.addMenuListener(new MenuListener() {
      public void menuSelected(MenuEvent e) { jMenuRules_menuSelected(); }
      public void menuDeselected(MenuEvent e) {}
      public void menuCanceled(MenuEvent e) {}
    });
    jMenuNewRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuNewRule_actionPerformed(); }} );
    jMenuRemoveRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuRemoveRule_actionPerformed(); }} );
    // menu popup
    jPopupNewRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupNewRule_actionPerformed(); }} );
    jPopupRemoveRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupRemoveRule_actionPerformed(); }} );
    jPopupUpRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupUpRule_actionPerformed(); }} );
    jPopupDownRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupDownRule_actionPerformed(); }} );
    jPopupGroupRule.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jPopupGroupRule_actionPerformed(); }} );
  }
//------------------------------------------------------------------------------
  public void Translate() throws Throwable {
    jMenuOptions.setText(LocaleKBCT.GetString("Options"));
    if (this.Quality)
    jMenuQuality.setText(LocaleKBCT.GetString("Quality"));
    jMenuSave.setText(LocaleKBCT.GetString("Save"));
    jMenuPrint.setText(LocaleKBCT.GetString("Print"));
    jMenuExport.setText(LocaleKBCT.GetString("Export"));
    jMenuClose.setText(LocaleKBCT.GetString("Close"));
    jMenuRules.setText(LocaleKBCT.GetString("Rules"));
    jMenuNewRule.setText(LocaleKBCT.GetString("NewRule")+" (CTRL+N)");
    jMenuRemoveRule.setText(LocaleKBCT.GetString("Remove"));
    jPopupNewRule.setText(LocaleKBCT.GetString("NewRule")+" (CTRL+N)");
    jPopupRemoveRule.setText(LocaleKBCT.GetString("Remove"));
    this.jPopupUpRule.setText(LocaleKBCT.GetString("Up"));
    this.jPopupDownRule.setText(LocaleKBCT.GetString("Down"));
    this.jPopupGroupRule.setText(LocaleKBCT.GetString("GroupRule")+" (CTRL+M)");
    if (this.Simplify)
      this.setTitle(LocaleKBCT.GetString("Simplify")+": "+LocaleKBCT.GetString("Rules"));
    else if (this.Pruned)
      this.setTitle(LocaleKBCT.GetString("Induce")+": "+LocaleKBCT.GetString("Rules")+": "+LocaleKBCT.GetString("Prune"));
    else
      this.setTitle(LocaleKBCT.GetString("Induce")+": "+LocaleKBCT.GetString("Rules"));

    this.repaint();
  }
//------------------------------------------------------------------------------
  public void dispose() {
    super.dispose();
    if (this.jrbqf != null) {
    	//System.out.println("jrbqf not NULL");
        this.jrbqf.dispose();
    }
    this.JRuleFrameOpen.Release();
    JKBCTFrame.RemoveTranslatable(this);
    if (this.kbctjrf!=null) {
        long ptr= this.kbctjrf.GetPtr();
        this.kbctjrf.Close();
        this.kbctjrf.Delete();
        this.kbctjrf=null;
        jnikbct.DeleteKBCT(ptr+1);
    }
  }
//------------------------------------------------------------------------------
  private void ReInit() {
    try { jbInitTables(); }
    catch( Throwable t ) {
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JRuleFrame en ReInit: "+t);
    }
  }
//------------------------------------------------------------------------------
  private void NewRule() {
    try {
      int rule_number= this.kbctjrf.GetNbRules();
      int input_number= this.kbctjrf.GetNbInputs();
      int output_number= this.kbctjrf.GetNbOutputs();
      int[] in_labels= new int[input_number];
      for (int n=0; n<input_number; n++)
        in_labels[n]= 0;
      int[] out_labels= new int[output_number];
      for (int n=0; n<output_number; n++)
        out_labels[n]= 0;
      this.kbctjrf.AddRule(new Rule(rule_number, input_number, output_number, in_labels, out_labels, "E", true));
      this.cursor= rule_number;
      this.ReInit();
      this.cursor= 0;
      this.repaint();
    } catch( Throwable en ) {
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JRuleFrame en NewRule: "+en);
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
          this.kbctjrf.RemoveRule(SelectedRules[i]-i);
        this.cursor= 0;
        this.ReInit();
        this.repaint();
      }
    }
  }
//------------------------------------------------------------------------------
  private void SetUpInitColumns(int column) throws Throwable {
    jTableRules.getColumnModel().getColumn(column).setCellRenderer( new DefaultTableCellRenderer() {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      this.setHorizontalAlignment(SwingConstants.CENTER);
      Rule r= JRuleFrame.this.kbctjrf.GetRule(row+1);
      if (r!=null) {
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
    comboBox.setRenderer(new BasicComboBoxRenderer() {
       	static final long serialVersiionUID=0;
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
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
		  if (!warn) {
              super.setText(input.GetUserLabelsName(index-1));
		  }
      }
      else if( index==0 ) {
        super.setText(new String());
        return this;
      } else {
        if( index==-1 )
          return this.getListCellRendererComponent(list, value, comboBox.getSelectedIndex(), isSelected, cellHasFocus);

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
       	static final long serialVersiionUID=0;
      public Object getCellEditorValue() { return new Integer(comboBox.getSelectedIndex()); }
    });
    Column.setCellRenderer(new DefaultTableCellRenderer() {
   	static final long serialVersiionUID=0;
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
      if( !warning && ((Integer)value).intValue() == 0 ) {
          this.setText("");
      } else {
        try {
          int NbLabels= input.GetLabelsNumber();
          if (!input.GetScaleName().equals("user")) {
              if (((Integer)value).intValue()>NbLabels) {
                  if (((Integer)value).intValue() > 2*NbLabels)
                    super.setText(input.GetORLabelsName(((Integer)value).intValue()-1-2*NbLabels));
                  else
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
        		  if (!warn) {
                      this.setText(input.GetUserLabelsName(((Integer)value).intValue()-1));
        		  }
        	  } else if (((Integer)value).intValue()>NbLabels) {
                  if (((Integer)value).intValue() > 2*NbLabels)
                    super.setText(input.GetORLabelsName(((Integer)value).intValue()-1-2*NbLabels));
                  else
                    this.setText(LocaleKBCT.GetString("NOT")+"("+input.GetUserLabelsName(((Integer)value).intValue()-1-NbLabels)+")");
              } else {
                  this.setText(input.GetUserLabelsName(((Integer)value).intValue()-1));
              }
          }
        } catch (Throwable t ) {
            this.setText("");
            if( column >= 2 ) {
                int conc= 0;
                int NbIn= JRuleFrame.this.NbIn;
                Rule r= JRuleFrame.this.kbctjrf.GetRule(row+1);
                if (column <= NbIn+1) {
                  r.SetInputLabel(column-1, conc);
                  JRuleFrame.this.kbctjrf.ReplaceRule( row+1, r);
                } else {
                  r.SetOutputLabel(column-NbIn-1, conc);
                  JRuleFrame.this.kbctjrf.ReplaceRule( row+1, r);
                }
            }
        }
      }
      this.setHorizontalAlignment(SwingConstants.CENTER);
      Rule r= JRuleFrame.this.kbctjrf.GetRule(row+1);
      if (r!=null) {
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
    if( this.kbctjrf.GetNbRules() == 0 )
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
  private void UpRules (int[] SelectedRules) {
    for (int i=0; i<SelectedRules.length; i++) {
       int SelR= SelectedRules[i];
       Rule SelRule= this.kbctjrf.GetRule(SelR+1);
       Rule PrevRule= this.kbctjrf.GetRule(SelR);
       this.kbctjrf.ReplaceRule(SelR, SelRule);
       this.kbctjrf.ReplaceRule(SelR+1, PrevRule);
    }
  }
//------------------------------------------------------------------------------
  void jPopupUpRule_actionPerformed() {
    int[] SelectedRules= this.jTableRules.getSelectedRows();
    this.UpRules(SelectedRules);
    try { this.ReInit(); }
    catch (Throwable t) { MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);}
  }
//------------------------------------------------------------------------------
  void DownRules(int[] SelectedRules) {
    for (int i=SelectedRules.length; i > 0; i--) {
       int SelR= SelectedRules[i-1];
       Rule SelRule= this.kbctjrf.GetRule(SelR+1);
       Rule NextRule= this.kbctjrf.GetRule(SelR+2);
       this.kbctjrf.ReplaceRule(SelR+1, NextRule);
       this.kbctjrf.ReplaceRule(SelR+2, SelRule);
    }
  }
//------------------------------------------------------------------------------
  void jPopupDownRule_actionPerformed() {
    int[] SelectedRules= this.jTableRules.getSelectedRows();
    this.DownRules(SelectedRules);
    try { this.ReInit(); }
    catch (Throwable t) { MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);}
  }
//------------------------------------------------------------------------------
  void jPopupGroupRule_actionPerformed() { this.GroupRule(); }
//------------------------------------------------------------------------------
  private void GroupRule() {
    int[] SelectedRules= this.jTableRules.getSelectedRows();
    if (SelectedRules.length>1) {
      int ini= SelectedRules[0];
      for (int i=1; i<SelectedRules.length; i++) {
         if (SelectedRules[i] > ini+i) {
           int lim= SelectedRules[i]-ini-i;
           int[] r= {SelectedRules[i]};
           for (int n=0; n<lim; n++) {
             r[0]= SelectedRules[i] - n;
             this.UpRules(r);
           }
         }
      }
      try { this.ReInit(); }
      catch (Throwable t) { MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);}
    }
  }
//------------------------------------------------------------------------------
  void jTableRules_keyPressed(KeyEvent e) {
    if( this.jTableRules.getSelectedRow() == -1 )
      return;

    if( this.jTableRules.getSelectedRows().length > 0 ) {
    	if (this.firstKey==null) {
    		this.firstKey= ""+KeyEvent.VK_CONTROL;
    		this.firstKeyPressed= System.currentTimeMillis();
    	} else {
    		boolean warning= false;
    		long tt=-1;
            if (this.firstKey.equals(""+KeyEvent.VK_CONTROL)) {
            	if (this.firstKeyPressed!=-1) {
            		tt= System.currentTimeMillis();
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
        else if (e.getKeyCode()==KeyEvent.VK_G)
            this.GroupRule();
        this.firstKey= null;
    	    } else {
        		this.firstKeyPressed= tt;
    	    }
    	}
    }
  }
//------------------------------------------------------------------------------
  void jTableRules_mousePressed(MouseEvent e) {
    if (SwingUtilities.isRightMouseButton(e)) {
      if( this.jTableRules.getSelectedRow() == -1 ) {
        this.jPopupRemoveRule.setEnabled(false);
        this.jPopupUpRule.setEnabled(false);
        this.jPopupDownRule.setEnabled(false);
        this.jPopupGroupRule.setEnabled(false);
      } else {
          this.jPopupRemoveRule.setEnabled(true);
          if ( this.jTableRules.getSelectedRowCount() > 1 ) {
            int[] SelectedRules= this.jTableRules.getSelectedRows();
            int FirstSelectedRule= SelectedRules[0];
            int LastSelectedRule= SelectedRules[SelectedRules.length-1];
            if ( (LastSelectedRule - FirstSelectedRule + 1) == SelectedRules.length ) {
                this.jPopupGroupRule.setEnabled(false);
                // Consecutive rules
                if ( FirstSelectedRule == 0 )
                     this.jPopupUpRule.setEnabled(false);
                else
                     this.jPopupUpRule.setEnabled(true);

                if ( LastSelectedRule == this.kbctjrf.GetNbRules() - 1 )
                     this.jPopupDownRule.setEnabled(false);
                else
                     this.jPopupDownRule.setEnabled(true);
            } else {
                this.jPopupUpRule.setEnabled(false);
                this.jPopupDownRule.setEnabled(false);
                this.jPopupGroupRule.setEnabled(true);
            }
        } else {
            this.jPopupGroupRule.setEnabled(false);
            if ( this.jTableRules.getSelectedRow() == 0 )
                 this.jPopupUpRule.setEnabled(false);
            else
                 this.jPopupUpRule.setEnabled(true);

            if ( this.jTableRules.getSelectedRow() == this.kbctjrf.GetNbRules() - 1 )
                 this.jPopupDownRule.setEnabled(false);
            else
                 this.jPopupDownRule.setEnabled(true);
        }
    }
    SwingUtilities.updateComponentTreeUI(this.jPopupMenuRules);
    jPopupMenuRules.show(this.jTableRules, e.getX(), e.getY());
    }
  }
//------------------------------------------------------------------------------
  void jMenuQuality_actionPerformed() {
    try {
      File temp1= JKBCTFrame.BuildFile("temprbqRulesFrame.fis");
      String fis_name= temp1.getAbsolutePath();
      File temp2= JKBCTFrame.BuildFile("temprbqRulesFrame.kb.xml");
      String kbcopy_name= temp2.getAbsolutePath();
      JKBCT kbctcopy= null;
      boolean warning= false;
      if (temp1.exists()) {
        kbctcopy= new JKBCT(kbcopy_name);
        if (!jnikbct.EqualKBCT(kbctcopy.GetPtr(), this.kbctjrf.GetPtr()))
          warning= true;
      }
      if ( (!temp1.exists()) || (warning) ) {
        kbctcopy= new JKBCT(this.kbctjrf.GetKBCTFile());
        kbctcopy.SetKBCTFile(kbcopy_name);
        kbctcopy.Save();
        this.kbctjrf.Save();
        this.kbctjrf.SaveFISquality(fis_name);
      }
      JFIS fis_file= new JFIS(fis_name);
      File RF= JKBCTFrame.BuildFile("result");
      String ResultFile=RF.getAbsolutePath();
      File f;
      if (this.parent.Parent.DataFileNoSaved != null)
          f = new File(this.parent.Parent.DataFileNoSaved.FileName());
      else
          f = new File(this.parent.Parent.DataFile.FileName());

      String TestFile=f.getAbsolutePath();
      double BlankThres= 0.1;
      String message= LocaleKBCT.GetString("Default_values")+":"+"\n"
                       +"   "+LocaleKBCT.GetString("ResultFile")+"= "+ResultFile+"\n"
                       +"   "+LocaleKBCT.GetString("TestFile")+"= "+TestFile+"\n"
                       +"   "+LocaleKBCT.GetString("BlankThres")+"= "+BlankThres+"\n"
                       +LocaleKBCT.GetString("OnlyExpertUsers")+"\n"
                       +LocaleKBCT.GetString("DoYouWantToMakeChanges");
      int option= MessageKBCT.Confirm(this, message, 1, false, false, false);
      if (this.jrbqf!=null)
          this.jrbqf.dispose();

      if (option==0) {
            if (this.parent.Parent.DataFileNoSaved != null)
              jrbqf= new JRulesBaseQualityFrame(this, fis_file, this.parent.Parent.DataFileNoSaved, new JSemaphore(), false, false);
            else
              jrbqf= new JRulesBaseQualityFrame(this, fis_file, this.parent.Parent.DataFile, new JSemaphore(), false, false);
      } else {
            if (this.parent.Parent.DataFileNoSaved != null)
              jrbqf= new JRulesBaseQualityFrame(this, fis_file, this.parent.Parent.DataFileNoSaved, new JSemaphore(), true, false);
            else
              jrbqf= new JRulesBaseQualityFrame(this, fis_file, this.parent.Parent.DataFile, new JSemaphore(), true, false);
      }
      if (this.Simplify)
          JKBCTFrame.Translatables.add(jrbqf);
    } catch (Throwable t) {
        //t.printStackTrace();
        MessageKBCT.Error(null, t);
    }
  }
//------------------------------------------------------------------------------
  void jMenuSave_actionPerformed(boolean ShowMessage) {
    int confirm= 0;
    if (ShowMessage) {
      if (this.Simplify)
        confirm= MessageKBCT.Confirm(this, LocaleKBCT.GetString("ReplaceRulesFromKB")+ "\n"+
                                           LocaleKBCT.GetString("DoYouWantToReplaceIt"), 0, false, false, false);
      else
        confirm= MessageKBCT.Confirm(this, LocaleKBCT.GetString("RulesAddedToKB")+ "\n"+
                                           LocaleKBCT.GetString("DoYouWantToContinue"), 0, false, false, false);
    }
    if (confirm == 0)
      try {
        if (this.Simplify) {
            int L= this.parent.Temp_kbct.GetNbRules();
            for (int n=0; n<L; n++)
              this.parent.Temp_kbct.RemoveRule(0);
        }
        for (int n = 0; n < this.kbctjrf.GetNbRules(); n++) {
          Rule raux= this.kbctjrf.GetRule(n + 1);
          Rule r = new Rule(raux.GetNumber(), raux.GetNbInputs(), raux.GetNbOutputs(), raux.Get_in_labels_number(), raux.Get_out_labels_number(), raux.GetType(), raux.GetActive());
          if (this.Simplify)
              this.parent.Temp_kbct.AddRule(r);
          else
              this.parent.jef.Temp_kbct.AddRule(r);
        }
        if (this.Simplify) {
          this.parent.Temp_kbct.Save();
          if (this.parent.isVisible())
            ((JExpertFrame)this.parent).ReInitTableRules();
        } else {
          this.parent.jef.Temp_kbct.Save();
          if (this.parent.jef.isVisible())
            this.parent.jef.ReInitTableRules();
        }
        if (JMainFrame.JRFs != null) {
          Object[] obj= JMainFrame.JRFs.toArray();
          for (int n=0; n<obj.length; n++) {
            JRuleFrame jrf= (JRuleFrame)obj[n];
            jrf.dispose();
          }
          JMainFrame.JRFs= null;
        } else {
        	//System.out.println("JRF -> call DISPOSE");
            this.dispose();
        }
      } catch (Throwable t) {
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JRuleFrame en jMenuSave_actionPerformed: "+t);
      }
  }
//------------------------------------------------------------------------------
  void jMenuRules_menuSelected() {
    if( this.kbctjrf == null ) {
        this.jMenuNewRule.setEnabled(false);
        this.jMenuRemoveRule.setEnabled(false);
    } else if (this.kbctjrf.GetNbRules()>0) {
        this.jMenuNewRule.setEnabled(true);
        this.jMenuRemoveRule.setEnabled(true);
    } else if ((this.kbctjrf.GetNbInputs()>0)&&(this.kbctjrf.GetNbOutputs()>0)) {
        this.jMenuNewRule.setEnabled(true);
        this.jMenuRemoveRule.setEnabled(false);
    } else {
        this.jMenuNewRule.setEnabled(false);
        this.jMenuRemoveRule.setEnabled(false);
    }
  }
//------------------------------------------------------------------------------
  void jMenuNewRule_actionPerformed() { this.NewRule(); }
//------------------------------------------------------------------------------
  void jMenuRemoveRule_actionPerformed() { this.RemoveRule(); }
//------------------------------------------------------------------------------
  void jPopupNewRule_actionPerformed() { this.NewRule(); }
//------------------------------------------------------------------------------
  void jPopupRemoveRule_actionPerformed() { this.RemoveRule(); }
//------------------------------------------------------------------------------
  void jMenuPrint_actionPerformed() {
    try {
      final JPrintTable table= new JPrintTable(this.RuleModel);
      TableColumnModel columnsINI = this.jTableRules.getColumnModel();
      TableColumnModel columns = table.getColumnModel();
      columns.getColumn(0).setWidth(columnsINI.getColumn(0).getWidth());
      columns.getColumn(1).setWidth(columnsINI.getColumn(1).getWidth());
      for( int i=2 ; i<table.getColumnCount() ; i++ )
        columns.getColumn(i).setWidth(columnsINI.getColumn(i).getWidth());

      JPrintPreviewTable pp= new JPrintPreviewTable(this, table);
      pp.Show();
    } catch (Exception ex) { MessageKBCT.Error(null, ex); }
  }
//------------------------------------------------------------------------------
  void jMenuExport_actionPerformed() {
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
    } catch (Exception ex) { MessageKBCT.Error(null, ex); }
  }
//------------------------------------------------------------------------------
/**
 * Classe de table imprimable
 */
  public class JPrintTable extends JTable {
  static final long serialVersionUID=0;	
  public JPrintTable(TableModel dm) {
    super( dm );
    // mod�le de colonne
    this.setColumnModel(new DefaultTableColumnModel() {
   	  static final long serialVersionUID=0;	
      public void addColumn(TableColumn tc) {
        if( tc.getModelIndex() <= JRuleFrame.this.kbctjrf.GetNbInputs()+JRuleFrame.this.kbctjrf.GetNbOutputs()+2 ) { super.addColumn(tc); return; }
        }
      });
    this.createDefaultColumnsFromModel();
    // renderers d'ent�te
    for( int i=0 ; i<this.getColumnCount() ; i++ )
      this.getColumnModel().getColumn(i).setHeaderRenderer(new DefaultTableCellRenderer() {
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
              input = JRuleFrame.this.kbctjrf.GetInput(1);
              super.setText(LocaleKBCT.GetString("If") + " " + input.GetName());
            }
            if( (column > 2) && (column < (NbIn+2)) ) {
              input = JRuleFrame.this.kbctjrf.GetInput(column-1);
              super.setText(LocaleKBCT.GetString("AND") + " " + input.GetName());
            }
            if( column == (NbIn+2) ) {
              input = JRuleFrame.this.kbctjrf.GetOutput(1);
              super.setText(LocaleKBCT.GetString("THEN") + " " + input.GetName());
            }
            if( (column > (NbIn+2)) && (column < (NbIn+NbOut+2)) ) {
              input = JRuleFrame.this.kbctjrf.GetOutput(column-NbIn-1);
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
  // renderers de colonnes
  for( int i = 0; i < this.getColumnCount(); i++ )
    this.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
   	  static final long serialVersionUID=0;	
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        int col_mod = table.convertColumnIndexToModel(column);
        if (col_mod >= 2) {
          if (((Integer)value).intValue() == 0)
            this.setText("");
          else {
            try {
              JVariable input;
              int NbInputs= JRuleFrame.this.kbctjrf.GetNbInputs();
              if (col_mod-2 < NbInputs)
                input= JRuleFrame.this.kbctjrf.GetInput(col_mod-2 + 1);
              else
                input= JRuleFrame.this.kbctjrf.GetOutput(col_mod-2 + 1 - NbInputs);

              int NbLabels = input.GetLabelsNumber();
              if (!input.GetScaleName().equals("user")) {
                if (((Integer)value).intValue() > NbLabels) {
                  if (((Integer)value).intValue() > 2 * NbLabels)
                    super.setText(input.GetORLabelsName(((Integer)value).intValue() - 1 - 2 * NbLabels));
                  else
                    this.setText(LocaleKBCT.GetString("NOT") + "(" + LocaleKBCT.GetString(input.GetLabelsName(((Integer)value).intValue() - 1 - NbLabels)) + ")");
                } else
                    this.setText(LocaleKBCT.GetString(input.GetLabelsName(((Integer)value).intValue() - 1)));
              } else {
                  if (((Integer)value).intValue() > NbLabels) {
                    if (((Integer)value).intValue() > 2 * NbLabels)
                      super.setText(input.GetORLabelsName(((Integer)value).intValue() - 1 - 2 * NbLabels));
                    else
                      this.setText(LocaleKBCT.GetString("NOT") + "(" + input.GetUserLabelsName(((Integer) value).intValue() - 1 - NbLabels) + ")");
                  } else
                      this.setText(input.GetUserLabelsName(((Integer)value).intValue() - 1));
              }
            } catch (Throwable t) {
                this.setText("");
                //t.printStackTrace();
            }
          }
        } else
            super.setBackground(Color.green);

        this.setHorizontalAlignment(SwingConstants.CENTER);
        String RuleType= JRuleFrame.this.kbctjrf.GetRule(row+1).GetType();
        if (RuleType.equals("I"))
            super.setForeground(Color.red);
        else if (RuleType.equals("E"))
            super.setForeground(Color.black);
        else if ( (RuleType.equals("S")) || (RuleType.equals("P")) )
            super.setForeground(Color.blue);
        
        return this;
      }
    });
    }
  }
//------------------------------------------------------------------------------
  void jMenuClose_actionPerformed() { this.dispose(); }
}