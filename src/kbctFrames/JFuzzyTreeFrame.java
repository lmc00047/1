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
//                         JFuzzyTreeFrame.java
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

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultTreeCellRenderer;

import kbct.DisplayVariableGroup;
import kbct.FuzzyTree;
import kbct.FuzzyTreeTableModel;
import kbct.LocaleKBCT;
import kbctAux.MessageKBCT;

import org.freehep.util.export.ExportDialog;

import print.JPrintPreview;
import util.tableheader.ColumnGroup;
import util.tableheader.GroupableTableHeader;
import util.tableheader.MultiLineHeaderRenderer;
import util.treetable.JTreeTable;

//***********************************************************************

//
//
//                              JFuzzyTreeFrame.java
//
//
// Author(s) : Jean-Luc LABLEE
// FISPRO Version : 2.1
// Contact : fispro@ensam.inra.fr
// Last modification date:  September 15, 2004
// File :

//**********************************************************************

/**
 * kbctFrames.JFuzzyTreeFrame
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

// Contains: FuzzyJTreeTable, JFuzzyTreeFrame

//------------------------------------------------------------------------------
class FuzzyJTreeTable extends JTreeTable {
  static final long serialVersionUID=0;	
//------------------------------------------------------------------------------
  public FuzzyJTreeTable(FuzzyTreeTableModel treeTableModel) {
    super(treeTableModel);
    this.setAutoCreateColumnsFromModel(true);
    // dissine les branches de l'arbre
    this.tree.putClientProperty("JTree.lineStyle", "Angled");
    // suppression des icones de l'arbre
    DefaultTreeCellRenderer dtr = (DefaultTreeCellRenderer)this.tree.getCellRenderer();
    dtr.setLeafIcon(null);
    dtr.setIcon(null);
    dtr.setDisabledIcon(null);
    dtr.setClosedIcon(null);
    dtr.setOpenIcon(null);
    // deploie les branches de l'arbre
    for( int i=0 ; i<this.tree.getRowCount() ; i++ )
      this.tree.expandRow(i);

    // entête des colonnes de type multi-ligne
    for( int i=0 ; i<this.getColumnModel().getColumnCount() ; i++ )
      this.getColumnModel().getColumn(i).setHeaderRenderer(new MultiLineHeaderRenderer() {
   	    static final long serialVersionUID=0;	
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
          return super.getTableCellRendererComponent(table, LocaleKBCT.GetStringNoException(FuzzyJTreeTable.this.getModel().getColumnName(column)), isSelected, hasFocus, row, column);
        }
      });
    // centrage des colonnes
    for( int i=1 ; i<this.getColumnModel().getColumnCount() ; i++ ) {
      this.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
   	    static final long serialVersionUID=0;	
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
          super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column);
          // centrage des elements
          super.setHorizontalAlignment(SwingConstants.CENTER);
          return this;
        }
      });
    }
    // affichage des groupes de colonnes
    for( int i=0 ; i<treeTableModel.fuzzy_tree.VariablesGroups.size() ; i++ ) {
      final DisplayVariableGroup dvg = (DisplayVariableGroup)treeTableModel.fuzzy_tree.VariablesGroups.elementAt(i);
      if( (dvg.VariablesNamesSize() > 1) && (dvg.Display() == true) ) {
        // c'est un groupe de colonnes affichables
        TableColumnModel cm = this.getColumnModel();
        ColumnGroup cg = new ColumnGroup(new String()); // chaine vide, c'est le renderer qui fait la traduction et affiche le nom de la colonne
        // recherche des colonnes appartenant au groupe en comparant les noms de colonnes
        for( int j=0 ; j<dvg.VariablesNamesSize() ; j++ )
          for( int k=0 ; k<cm.getColumnCount() ; k++ )
            if( dvg.VariableName(j).equals(cm.getColumn(k).getHeaderValue()) )
              cg.add(cm.getColumn(k));

        // ajout du groupe au header
        GroupableTableHeader header = (GroupableTableHeader)this.getTableHeader();
        header.addColumnGroup(cg);
        // renderer du groupe de colonne
        cg.setHeaderRenderer(new DefaultTableCellRenderer() {
   	    static final long serialVersionUID=0;	
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
          super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
          super.setHorizontalAlignment(SwingConstants.CENTER);
          super.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
          super.setText(LocaleKBCT.GetString(dvg.GroupName()));
          return this;
        }
      });
     }
    }
    // largeur de la première colonne
    TableColumnModel cm = this.getColumnModel();
    cm.getColumn(0).setPreferredWidth(treeTableModel.fuzzy_tree.getPreferredSize().width);
  }
//------------------------------------------------------------------------------
  // l'objet TableHeader par défaut est de type GroupableTableHeader
  protected JTableHeader createDefaultTableHeader() { return new GroupableTableHeader(columnModel); }
}


//------------------------------------------------------------------------------
public class JFuzzyTreeFrame extends JChildFrame {
  static final long serialVersionUID=0;	
  private JScrollPane jScrollPaneTree;
  private GridBagLayout gridBagLayout = new GridBagLayout();
  private JPanel jPanelDisplay = new JPanel();
  private GridBagLayout gridBLDisplay = new GridBagLayout();
  private TitledBorder titledBorderDisplay;
  private ButtonGroup buttonGroupDisplayNodes = new ButtonGroup();
  private JRadioButton jRBAllNodes = new JRadioButton();
  private JRadioButton jRBLeafs = new JRadioButton();
  private JMenuItem jMenuPrint = new JMenuItem();
  private JMenuItem jMenuExport = new JMenuItem();
  private JMenuItem jMenuClose = new JMenuItem();
  private FuzzyTree jFuzzyTree;           // arbre flou
  private JTreeTable jFuzzyTreeGraph;     // représentation graphique de l'arbre flou
  private String FileName;
//------------------------------------------------------------------------------
  public JFuzzyTreeFrame( JMainFrame parent, String file_name ) {
    super(parent);
    this.FileName = file_name;
    try {
      jFuzzyTree = new FuzzyTree(file_name);
      jFuzzyTreeGraph = new FuzzyJTreeTable(new FuzzyTreeTableModel(jFuzzyTree));
      jbInit();
    } catch(Throwable t) {
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en constructor en JFuzzyTreeFrame: "+t);
    }
  }
//------------------------------------------------------------------------------
  private void jbInit() throws Throwable {
    JMenuBar jmb = new JMenuBar();
    jmb.add(this.jMenuPrint);
    jmb.add(this.jMenuExport);
    jmb.add(this.jMenuClose);
    this.setJMenuBar(jmb);
    titledBorderDisplay = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"");
    this.getContentPane().setLayout(gridBagLayout);
    this.jScrollPaneTree = new JScrollPane(this.jFuzzyTreeGraph);
    this.jFuzzyTreeGraph.setPreferredScrollableViewportSize(this.jFuzzyTreeGraph.getPreferredSize());
    jPanelDisplay.setLayout(gridBLDisplay);
    jPanelDisplay.setBorder(titledBorderDisplay);
    buttonGroupDisplayNodes.add(jRBAllNodes);
    buttonGroupDisplayNodes.add(jRBLeafs);
    this.getContentPane().add(jScrollPaneTree, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    this.getContentPane().add(jPanelDisplay, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
    jMenuPrint.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuPrint_actionPerformed(); } });
    jMenuExport.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExport_actionPerformed(); } });
    jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuClose_actionPerformed(); } });
    jPanelDisplay.add(jRBAllNodes, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanelDisplay.add(jRBLeafs, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    // radio bouton all nodes /leafs
    class DisplayActionListener implements ActionListener {
      public void actionPerformed(ActionEvent e) {
        JFuzzyTreeFrame.this.jFuzzyTree.DisplayAllNodes= JFuzzyTreeFrame.this.jRBAllNodes.isSelected();
        JFuzzyTreeFrame.this.jFuzzyTreeGraph.repaint();
      }
    }
    this.jRBAllNodes.addActionListener(new DisplayActionListener());
    this.jRBLeafs.addActionListener(new DisplayActionListener());
    if( JFuzzyTreeFrame.this.jFuzzyTree.DisplayAllNodes == true )
      this.jRBAllNodes.setSelected(true);
    else
      this.jRBLeafs.setSelected(true);

    // affichage des checkBoxs
    for( int i=0 ; i<this.jFuzzyTree.VariablesGroups.size() ; i++ ) {
      final int index = i;
      final DisplayVariableGroup dv = (DisplayVariableGroup)this.jFuzzyTree.VariablesGroups.elementAt(i);
      JCheckBox cb = new JCheckBox() {
   	    static final long serialVersionUID=0;	
        public String getText() {
          // retourne le texte de la check box traduit
          if( dv != null )
            return LocaleKBCT.GetString(dv.GroupName());

          return null;
        }
      };
      cb.setSelected(dv.Display());   // selectionne la check box si le groupe est affichable
      cb.addActionListener(new ActionListener() {
        // ActionListener de la check box
        public void actionPerformed(ActionEvent e) {
          DisplayVariableGroup dv = (DisplayVariableGroup)JFuzzyTreeFrame.this.jFuzzyTree.VariablesGroups.elementAt(index);
          dv.Display(((JCheckBox)e.getSource()).isSelected());
          JFuzzyTreeFrame.this.jScrollPaneTree.getViewport().remove(JFuzzyTreeFrame.this.jFuzzyTreeGraph);
          JFuzzyTreeFrame.this.jFuzzyTreeGraph = new FuzzyJTreeTable(new FuzzyTreeTableModel(jFuzzyTree));
          JFuzzyTreeFrame.this.jScrollPaneTree.getViewport().add(JFuzzyTreeFrame.this.jFuzzyTreeGraph);
          JFuzzyTreeFrame.this.repaint();
        }
      });
      jPanelDisplay.add(cb, new GridBagConstraints(0, i+1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }
    this.Translate();
    this.pack();
    this.setLocation(0,0);
    this.setVisible(true);
  }
//------------------------------------------------------------------------------
  public void Translate() throws Throwable {
    this.setTitle(LocaleKBCT.GetString("Tree") + ": " + this.FileName);
    this.titledBorderDisplay.setTitle(LocaleKBCT.GetString("Display"));
    this.jRBLeafs.setText(LocaleKBCT.GetString("Leafs"));
    this.jRBAllNodes.setText(LocaleKBCT.GetString("AllNodes"));
    this.jMenuPrint.setText(LocaleKBCT.GetString("Print"));
    this.jMenuExport.setText(LocaleKBCT.GetString("Export"));
    this.jMenuClose.setText(LocaleKBCT.GetString("Close"));
    this.repaint();
  }
//------------------------------------------------------------------------------
  void jMenuPrint_actionPerformed() {
    try {
      Printable p= new Printable() {
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
          if (pageIndex >= 1)
            return Printable.NO_SUCH_PAGE;
          else
            return JFuzzyTreeFrame.this.print(graphics, pageFormat);
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
          JFuzzyTreeFrame.this.jScrollPaneTree.paint(g);
          g.translate(0, JFuzzyTreeFrame.this.jScrollPaneTree.getHeight());
          JFuzzyTreeFrame.this.jScrollPaneTree.paint(g);
          g.setColor(Color.gray);
          g.drawLine(0,0,0, JFuzzyTreeFrame.this.jScrollPaneTree.getHeight()-1);
        }
      };
      panel.setSize(new Dimension(this.jScrollPaneTree.getWidth(), this.jScrollPaneTree.getHeight()));
      export.showExportDialog( panel, "Export view as ...", panel, "export" );
    } catch (Exception ex) { MessageKBCT.Error(null, ex); }
  }
//------------------------------------------------------------------------------
  public int print(java.awt.Graphics g, PageFormat pageFormat) throws PrinterException {
     java.awt.Graphics2D  g2 = ( java.awt.Graphics2D )g;
     double scalew=1;
     double scaleh=1;
     double pageHeight = pageFormat.getImageableHeight();
     // See how much we need to scale the image to fit in the page's width
     double pageWidth = pageFormat.getImageableWidth();
     if(  getWidth() >= pageWidth )
       scalew= pageWidth / getWidth();

     if(  getHeight() >= pageHeight)
       scaleh= pageWidth / getHeight();

     g2.translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
     g2.scale( scalew, scaleh );
     this.jScrollPaneTree.print( g2 );
     return Printable.PAGE_EXISTS;
 }
//------------------------------------------------------------------------------
  void jMenuClose_actionPerformed() { this.dispose(); }
}