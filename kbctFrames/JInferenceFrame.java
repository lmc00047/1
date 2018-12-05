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
//                              JInferenceFrame.java
//
//
//**********************************************************************

//***********************************************************************

//
//
//                              JInferenceFrame.java
//
//
// Author(s) : Jean-Luc LABLEE
// FISPRO Version : 2.1
// Contact : fispro@ensam.inra.fr
// Last modification date : September 15, 2004
// File :

//**********************************************************************

// Contains: Parameters, JInferenceFrame

package kbctFrames;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
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
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import kbct.JFIS;
import kbct.JKBCT;
import kbct.KBCTListener;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbctAux.DoubleFieldInput;
import kbctAux.MessageKBCT;
import kbctAux.NumberFieldActionListener;
import kbctAux.SortableHeaderListener;
import kbctAux.Translatable;

import org.freehep.util.export.ExportDialog;

import print.JPrintPreview;
import util.sortabletable.SortButtonRenderer;
import util.sortabletable.SortableTableModel;
import util.sortabletable.TableSorter;
import KB.Rule;
import fis.FISPlot;
import fis.JExtendedDataFile;
import fis.JInput;
import fis.JOutput;
import fis.JRule;
import fis.JSemaphore;

//------------------------------------------------------------------------------
class Parameters {
  int SliderPosition;
  double [] Range;
  public Parameters() { this.SliderPosition = JInferenceFrame.CursorResolution/2; }
  public void SetSliderPosition( int position ) { this.SliderPosition = position; }
  public int GetSliderPosition() { return this.SliderPosition; }
  public void SetRange( double [] range ) { this.Range = range; }
  public double [] GetRange() { return this.Range; }
}

/**
 * kbctFrames.JInferenceFrame builds the frame which will be used for inference.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
public class JInferenceFrame extends JChildFrame implements SortableHeaderListener.SortableObject, Translatable {
  static final long serialVersionUID=0;	
  public static int CursorResolution = 1000;       // resolution du curseur
  private JKBCT kbct;
  private JFIS fis; /**< Objet FIS */
  private int nb_inputs; /**< Nombre d'entrées actives dans le FIS */
  private int nb_outputs; /**< Nombre de sorties actives dans le FIS */
  private int nb_rules; /**< Nombre de règles actives dans le FIS */
  private final int RowHeight = 50; /** Hauteur des cellules de la table (pour affichage correct d'un objet FISPlot) */
  private final int FieldRowHeight = 16; /** Hauteur des objets champ (pour affichage correct d'un objet DoubleFieldInput) */
  private SortableTableModel rules_model; /**< Modèle de données règles triable */
  private KBCTListener KBCTListener = null; /**< Listener du FIS JInferenceFrame#fis */
  private Component split_pane = null; /**< Objet conteneur des tables entrées et sorties */
  private SortButtonRenderer table_header_renderer; /**< Renderer des headers de table triable\n les 3 tables régles, entrées et sorties utilisent le même objet renderer */
  private Vector tables_headers; /**< Vecteur contenant les objets JTableHeader des 3 tables règles, entrées et sorties */
  private Vector InputsParameters = new Vector();
  private Vector OutputsParameters = new Vector();
  private JSemaphore frame_open;   /**< Semaphore d'ouverture de la fenetre JInferenceFrame */
  private JMenuBar jMenuBarInference;
  private JMenuItem jMenuLinks= new JMenuItem();
  private JMenuItem jMenuFingrams= new JMenuItem();
  //private JMenu jMenuFingrams= new JMenu();
  //private JMenuItem jMenuFingramsWS= new JMenuItem();
  //private JMenuItem jMenuFingramsWOS= new JMenuItem();
  //private JMenuItem jMenuFingramsALL= new JMenuItem();
  private JMenuItem jMenuPrint= new JMenuItem();
  private JMenuItem jMenuExport= new JMenuItem();
  private JMenuItem jMenuClose= new JMenuItem();
  private JMenuItem jMenuHelp= new JMenuItem();
  private JMenu jMenuData= new JMenu();
  private JMenuItem jMenuSetDataValue= new JMenuItem();
  private JMenuItem jMenuAllData= new JMenuItem();
  private Vector infer_range; /**< Domaine d'inférence des entrées */
  private JTable inputs_table; /**< Table des entrées */
  private JTable outputs_table; /**< Table des sorties */
  private Vector sliders_value; /**< Valeur des curseurs pour chaque entrée */
  private double []outputs_infer; /**< Valeur des sorties inférées */
  private Vector outputs_agg;
  public JExtendedDataFile data_file; /**< Fichier de données */
  public String data_file_name;
  private int sort_col = -1; /**< Mémorisation du numéro de colonne triées. -1 pour aucune colonne triée */
  private boolean is_ascent; /**< Mémorisation ordre de tri */
  private JDoubleSlider []sliders; /**< Objets curseurs */
  private DefaultBoundedRangeModel scroll_model; /**< Modèle des scrollbar verticales des tables entrées et sorties */
  private JKBCTFrame Parent;
  private JExpertFrame Parent_EF;
  private Vector inferedValues= null;
  private Vector aggregatedValues= null;
  protected static JFISConsole[] jfc= new JFISConsole[8];
  private int[] rulesIndex;
  public JLinksFrame jlf;
//------------------------------------------------------------------------------
  /**
   * Classe modèle de colonne pour table règle
   */
class RulesColumnModel extends DefaultTableColumnModel {
  static final long serialVersionUID=0;	
  public void addColumn(TableColumn tc) { if( tc.getModelIndex() == 0 ) super.addColumn(tc); } /**< Surcharge de DefaultTableColumnModel.addColumn pour afficher la première colonne du modèle (numéro des règles)*/
}
//------------------------------------------------------------------------------
  /**
   * Classe modèle de colonne pour table entrées
   *
   */
class InputsColumnModel extends DefaultTableColumnModel {
  static final long serialVersionUID=0;	
/**
  *  Surcharge de DefaultTableColumnModel.addColumn pour afficher les colonnes des entrées actives
  * */
  public void addColumn(TableColumn tc) {
    int index = tc.getModelIndex();
    if( (index > 0) && (index <= JInferenceFrame.this.nb_inputs) )
      super.addColumn(tc);
  }
}
//------------------------------------------------------------------------------
  /**
   * Classe modèle de colonne pour table sortiees
   */
class OutputsColumnModel extends DefaultTableColumnModel {
  static final long serialVersionUID=0;	
/**
  *  Surcharge de DefaultTableColumnModel.addColumn pour afficher les colonnes des sorties actives
  * */
  public void addColumn(TableColumn tc) {
    int index = tc.getModelIndex();
    if( index > JInferenceFrame.this.nb_inputs )
      super.addColumn(tc);
  }
}
//------------------------------------------------------------------------------
/**
 * Classe table utilisée pour les tables de la fenêtre
 */
class JInferenceTable extends JTable {
  static final long serialVersionUID=0;	
  public JInferenceTable(TableModel dm, TableColumnModel cm) {
    super(dm, cm);
    this.createDefaultColumnsFromModel(); // construit la table
    this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    this.setBackground(UIManager.getColor("Button.background"));
    this.setColumnSelectionAllowed(false);
    this.setCellSelectionEnabled(false);
  }
  public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {} // table non sélectionnable
  public Dimension getPreferredScrollableViewportSize() { return new Dimension(this.getPreferredSize().width, this.getPreferredSize().height); }
  public boolean isCellEditable(int row, int column) { return false; }  // cellules non éditables
}
//------------------------------------------------------------------------------
/**
 * Classe table utilisée pour les tables entête de la fenêtre
 */
class JInferenceTableHeader extends JInferenceTable {
  static final long serialVersionUID=0;	
  public JInferenceTableHeader(TableModel dm, TableColumnModel cm) {
    super( dm, cm );
    for( int i=0 ; i<this.getColumnCount() ; i++ )
      this.getColumnModel().getColumn(i).setHeaderRenderer(JInferenceFrame.this.table_header_renderer); // entête triable

    this.getTableHeader().setReorderingAllowed(false);
    this.getTableHeader().setResizingAllowed(false);
    this.getTableHeader().addMouseListener(new SortableHeaderListener(this.getTableHeader(),JInferenceFrame.this.table_header_renderer, JInferenceFrame.this)); // listener de header triable
    JInferenceFrame.this.tables_headers.add(this.getTableHeader()); // ajout du header dans le vecteurs des headers
  }
}
//------------------------------------------------------------------------------
/**
 * Construction de la Table entête de règles
 * @return un objet JPanel contenant la table entête règle et son entête triable
 */
  private Component RulesTableHeader() throws Throwable {
    // construit la table à partir du modèle de données et du modèle de colonne
    // les données de cette table sont indépendante du modèle this.rules_model.
    // le modèle this.rules_model est passé au constructeur uniquement pour récupérer les JTable.tableHeader
    // surcharge de la fonction getRowCount et getValueAt qui renvoit une chaine vide
    JTable table = new JInferenceTableHeader(this.rules_model, new RulesColumnModel()) {
   	  static final long serialVersionUID=0;	
      public int getRowCount() { return 1; } // une seule ligne
      public Object getValueAt(int row, int column) { return new String(); }
    };
    table.setShowGrid(false);
    // construction du panel de retour avec la table et son entête
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(table.getTableHeader(), BorderLayout.NORTH);
    panel.add(table, BorderLayout.SOUTH);
    return panel;
  }
//------------------------------------------------------------------------------
  /**
   * Construction de la table entête des entrées\n
   * - les champs de saisie sur la première ligne
   * - les curseurs sur la seconde ligne
   *
   * @return un objet JPanel contenant la table entête entrées et son entête triable
   */
  private Component InputsTableHeader() throws Throwable {
    this.sliders = new JDoubleSlider[this.nb_inputs]; // tableau des objets curseurs
    // données utilisées par la table: objets de saisie DoubleFieldInput et curseur JDoubleSlider
    final Object [][]data = new Object[2][this.nb_inputs];
    for( int i=0 ; i<this.nb_inputs ; i++ ) {
      data[0][i] = new DoubleFieldInput(this.fis.GetInput(i));  // champ de saisie
      JDoubleSlider slider = new JDoubleSlider();  // curseur
      data[1][i] = slider;
      this.sliders[i] = slider;
    }
    // construit la table à partir du modèle de données et du modèle de colonne
    // les données de cette table sont indépendante du modèle this.rules_model.
    // le modèle this.rules_model est passé au constructeur uniquement pour récupérer les JTable.tableHeader
    // surcharge de la fonction getRowCount et getValueAt
    final JTable table = new JInferenceTableHeader(this.rules_model, new InputsColumnModel()) {
   	  static final long serialVersionUID=0;	
      public int getRowCount() { return 3; } // 3 lignes: 2 première lignes: objets data, 3eme ligne vide: correspond à la 3eme ligne de la table OutputsTableHeader
      // surcharge de JTable.getValueAt renvoit les objets du tableau data pour les 2 premières lignes, un JPanel vide pour la troisième ligne
      public Object getValueAt(int row, int column) { return row < 2 ? data[row][this.convertColumnIndexToModel(column)-1] : new JPanel(); }
      public void setValueAt(Object aValue, int row, int column) {} // ne fait rien
      public boolean isCellEditable(int row, int column) { return true; }  // cellules éditables
      // surcharge de JTable.tableChanged car après un appel à JTable.tableChanged les hauteurs de lignes sont perdues
      public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
        this.setRowHeight(0, JInferenceFrame.this.FieldRowHeight);
        this.setRowHeight(2, JInferenceFrame.this.RowHeight);
      }
    };
    // configuration des listeners
    final double[] infer_values = new double[this.fis.NbInputs()]; // valeur des entrées pour l'inférence
    for( int i=0 ; i<this.fis.NbInputs() ; i++ ) {
      final int fi = i;
      final DoubleFieldInput dfi = (DoubleFieldInput)data[0][i];
      final JDoubleSlider slider = (JDoubleSlider)data[1][i];
      // listener de curseur
      slider.addChangeListener(new ChangeListener() {
        // changement d'etat du curseur
        public void stateChanged(ChangeEvent e) {
          double input_value = slider.getDoubleValue(); // lecture de la valeur du curseur
          JInferenceFrame.this.sliders_value.setElementAt(new java.lang.Double(input_value), fi);
          // mise à jour du champ de saisie
          dfi.setValue(input_value);
          ((DefaultTableModel)table.getModel()).fireTableCellUpdated(0, fi+1);
          // inference
          infer_values[fi] = input_value;
          try {
            JInferenceFrame.this.fis.Infer(infer_values);
            JInferenceFrame.this.outputs_infer = JInferenceFrame.this.fis.SortiesObtenues();
            JInferenceFrame.this.outputs_agg = new Vector();
            for (int n=0; n<JInferenceFrame.this.fis.NbActiveOutputs(); n++) {
              double[][] d= JInferenceFrame.this.fis.AgregationResult(n);
              JInferenceFrame.this.outputs_agg.add(new Double(d[0].length));
              for (int m=0; m<d[0].length; m++) {
                JInferenceFrame.this.outputs_agg.add(new Double(d[0][m]));
                JInferenceFrame.this.outputs_agg.add(new Double(d[1][m]));
              }
            }
          } catch(Throwable t) {
               t.printStackTrace();
               System.out.println("exc: "+t);
          }
          // mise à jour des panels graphiques de l'entrée modifiée
          for( int row=0 ; row<JInferenceFrame.this.inputs_table.getRowCount() ; row++ )
            ((DefaultTableModel)JInferenceFrame.this.inputs_table.getModel()).fireTableCellUpdated(row, /*table.convertColumnIndexToModel(fi)-1*/ fi+1);
          // mise à jour des sorties
          if( JInferenceFrame.this.outputs_table != null )
            for( int i=0 ; i<JInferenceFrame.this.outputs_table.getColumnCount() ; i++ )
              for( int j=0 ; j<JInferenceFrame.this.outputs_table.getRowCount() ; j++ )
                ((DefaultTableModel)JInferenceFrame.this.outputs_table.getModel()).fireTableCellUpdated(j, JInferenceFrame.this.outputs_table.convertColumnIndexToModel(i));
        }
      } );
      // listener de champ de saisie
      dfi.SetAction(new NumberFieldActionListener() {
        public void NumberFieldAction() {
          // limite la saisie au domaine d'inférence
          double []range = (double [])JInferenceFrame.this.infer_range.elementAt(fi);
          if( dfi.getValue() < range[0] ) dfi.setValue(range[0]);
          if( dfi.getValue() > range[1] ) dfi.setValue(range[1]);
          // mise à jour du curseur
          slider.setDoubleValue(dfi.getValue());
          ((DefaultTableModel)table.getModel()).fireTableCellUpdated(1, fi+1);
        }
      } );
      // valeur par défaut des listeners
      double []range = (double [])this.infer_range.elementAt(i); // domaine d'inférence
      slider.setDoubleMinMax(range[0], range[1]);
      double value = ((java.lang.Double)this.sliders_value.elementAt(i)).doubleValue();
      slider.setDoubleValue(value);
      dfi.setValue(value);
    }
    // configuration des renderers et éditeurs de cellule
    for( int i=0 ; i<table.getColumnCount() ; i++ ) {
      // renderers
      table.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
   	    static final long serialVersionUID=0;	
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) { return (Component)value; }
      } );
      // éditeurs
      table.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(new JTextField()) {
   	    static final long serialVersionUID=0;	
        // JTextField virtuel (ne correspond pas au type d'éditeur utilisé)
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) { return (Component)table.getValueAt(row, column); }
        // Active l'éditeur de cellule au premier clic de souris\n
        // par défaut les éditeurs de cellules sont activés après un double clic
        public boolean isCellEditable(EventObject evt) {
          if( evt instanceof MouseEvent ) { return ((MouseEvent)evt).getClickCount() >= 1; }
          return true;
        }
      } );
    }
    // construction du panel de retour avec la table et son entête
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(table.getTableHeader(), BorderLayout.NORTH);
    panel.add(table, BorderLayout.SOUTH);
    return panel;
  }
//------------------------------------------------------------------------------
  /**
   * Construction de la table entête des sorties\n
   * - la valeur de la sortie sur la première ligne
   * - la valeur de l'alarme sur la seconde ligne
   * - le panel graphique agrégation sur la 3eme ligne
   *
   * @return un objet JPanel contenant la table entête sorties et son entête triable
   */
  private Component OutputsTableHeader() throws Throwable {
  // construit la table à partir du modèle de données et du modèle de colonne
  // les données de cette table sont indépendante du modèle this.rules_model.
  // le modèle this.rules_model est passé au constructeur uniquement pour récupérer les JTable.tableHeader
  // surcharge de la fonction getRowCount et getValueAt
    JTable table = new JInferenceTableHeader(this.rules_model, new OutputsColumnModel()) {
   	  static final long serialVersionUID=0;	
      public int getRowCount() { return 3; } // 3 lignes
      public Object getValueAt(int row, int column) { return new String(); } // chaine vide
      public Class getColumnClass(int column) { return String.class; }
      // surcharge de JTable.tableChanged car après un appel à JTable.tableChanged les hauteurs de lignes sont perdues
      public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
        this.setRowHeight(0, JInferenceFrame.this.FieldRowHeight);
        this.setRowHeight(2, JInferenceFrame.this.RowHeight);
      }
    };
    ((DefaultTableModel)table.getModel()).fireTableDataChanged(); // force le changement pour réaffichage de la table
    // configuration des renderers
    for( int i=0 ; i<table.getColumnCount() ; i++ ) {
      final int output_number = table.convertColumnIndexToModel(i)-JInferenceFrame.this.nb_inputs-1;
      final JOutput output = JInferenceFrame.this.fis.GetOutput(output_number);
      if (JInferenceFrame.this.inferedValues != null)
        JInferenceFrame.this.inferedValues.add(new Double(JInferenceFrame.this.outputs_infer[output_number]));

      if (JInferenceFrame.this.aggregatedValues != null) {
        Vector v= new Vector();
        Object[] obj= JInferenceFrame.this.outputs_agg.toArray();
        Vector vv= new Vector();
        int cont=0;
        int lim= 2*((Double)obj[cont++]).intValue()+1;
        for (int n=1; n<obj.length; n++) {
          while (cont < lim) {
            vv.add(obj[n++]);
            cont++;
          }
          v.add(vv);
          vv= new Vector();
          if (n<obj.length)
            lim= 2*((Double)obj[n]).intValue()+1;

          cont= 1;
        }
        JInferenceFrame.this.aggregatedValues.add(v);
      }
      table.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
   	    static final long serialVersionUID=0;	
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
          // premiere ligne: valeur de la sortie
          if( (row == 0) && (JInferenceFrame.this.outputs_infer != null) ) {
            double out = JInferenceFrame.this.outputs_infer[output_number];
            try {
              // affiche la sortie inférée dans un objet DoubleFieldInput
              DoubleFieldInput dfi = new DoubleFieldInput(output);
              dfi.setValue(out);
              return dfi;
            } catch( Throwable t ) {}
            return this;
          }
          // 2eme ligne: valeur de l'alarme
          if( row == 1 ) {
            try {
              super.getTableCellRendererComponent(table, value, isSelected,hasFocus, row, column);
              switch(output.GetAlarm()) {
                case 0: this.setText(""); break;
                case 1: this.setText("No rule"); break;
                case 2: this.setText("Ambiguity"); break;
                case 3: this.setText("No connex"); break;
                case 4: this.setText("No connex"); break;
              }
              this.setForeground(Color.red);
              this.setHorizontalAlignment(SwingConstants.CENTER);
              this.setBackground(UIManager.getColor("Button.background"));
            } catch( Throwable t ) {}
            return this;
          }
          // 3eme ligne: affichage du panel agrégation
          if( row == 2 ) {
            try {
              if( output.GetNature() == JOutput.FUZZY )
                return new FuzzyAgregationRenderer(output, output_number); // sortie floue
              else
                return new CrispAgregationRenderer(output, output_number); // sortie nette
            } catch( Throwable t ) {} }
          return this;
        }
      });
    }
    // construction du panel de retour avec la table et son entête
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(table.getTableHeader(), BorderLayout.NORTH);
    panel.add(table, BorderLayout.SOUTH);
    return panel;
  }
//------------------------------------------------------------------------------
  /**
   * Construction de la table des numéros des règles
   * @return un objet JTable contenant les une colonne avec les numéros de règles
   */
  private JTable RulesTable() throws Throwable {
    JTable table = new JInferenceTable(this.rules_model, new RulesColumnModel() );
    table.setRowHeight(this.RowHeight); // hauteur des lignes
    // configuration du renderer
    table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
   	  static final long serialVersionUID=0;	
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        super.setHorizontalAlignment(SwingConstants.CENTER);
        return this;
      }
    });
    return table;
  }
//------------------------------------------------------------------------------
  /**
   * Construction de la table des entrées actives
   * @return un objet JScrollPane contenant la table des entrées actives et:
   * - la table JInferenceFrame#RulesTable en JScrollPane.rowHeader
   * - la table JInferenceFrame#InputsTableHeader en JScrollPane.columnHeader
   * - la table JInferenceFrame#RulesTableHeader en JScrollPane.upperLeft
   */
  private JScrollPane InputsTable() throws Throwable {
    JTable table = new JInferenceTable(this.rules_model, new InputsColumnModel()) {
   	  static final long serialVersionUID=0;	
      protected void configureEnclosingScrollPane() {} // supprime l'insertion du JTable.tableHeader dans le JScrollPane
    };
    this.inputs_table = table;
    // configuration des renderers
    for( int i=0 ; i<table.getColumnCount() ; i++ ) {
      int input_number = table.convertColumnIndexToModel(i)-1;
      table.getColumnModel().getColumn(i).setCellRenderer(new InputRenderer(this.fis.GetInput(input_number), input_number));
    }
    table.setRowHeight(this.RowHeight); // hauteur des lignes
    // construction du panel de retour
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(table, BorderLayout.CENTER);
    JScrollPane scroll = new JScrollPane(panel);
    scroll.setColumnHeaderView(this.InputsTableHeader());
    scroll.setRowHeaderView(this.RulesTable());
    scroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, this.RulesTableHeader());
    return scroll;
  }
//------------------------------------------------------------------------------
  /**
   * Construction de la table des sorties actives
   * @return un objet JScrollPane contenant la table des sorties actives et:
   * - la table JInferenceFrame#OutputsTableHeader en JScrollPane.columnHeader
   */
  private JScrollPane OutputsTable() throws Throwable {
    JTable table = new JInferenceTable(this.rules_model, new OutputsColumnModel()) {
   	  static final long serialVersionUID=0;	
      protected void configureEnclosingScrollPane() {} // supprime l'insertion du JTable.tableHeader dans le JScrollPane
    };
    this.outputs_table = table;
    // configuration des renderers
    for( int i=0 ; i<table.getColumnCount() ; i++ ) {
      int output_number = table.convertColumnIndexToModel(i)-JInferenceFrame.this.nb_inputs-1;
      JOutput output = this.fis.GetOutput(output_number);
      if( output.GetNature() == JOutput.CRISP )
        table.getColumnModel().getColumn(i).setCellRenderer(new CrispRenderer());
      else
        table.getColumnModel().getColumn(i).setCellRenderer(new FuzzyRenderer(output, output_number));
    }
    table.setRowHeight(this.RowHeight); // hauteur des lignes
    // construction du panel de retour
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(table, BorderLayout.CENTER);
    JScrollPane scroll = new JScrollPane(panel);
    scroll.setColumnHeaderView(this.OutputsTableHeader());
    return scroll;
  }
//------------------------------------------------------------------------------
  /**
   * Re-Initialization
   */
  private void ReInit() {
    try {
      this.Init();
      this.Translate();
    } catch( Throwable t ) {
      if( this.split_pane != null ) {
        this.getContentPane().remove(this.split_pane);
        this.split_pane = new JPanel();
        this.getContentPane().add(this.split_pane);
        this.repaint();
      }
      MessageKBCT.Error(this, LocaleKBCT.GetStringNoException(t.getClass().getName()), t.getLocalizedMessage());
    }
  }
//------------------------------------------------------------------------------
  /**
   * Initialization
   */
  private void Init() throws Throwable {
    this.nb_inputs = this.fis.NbInputs();
    this.nb_outputs = this.fis.NbOutputs();
    this.nb_rules = this.fis.NbRules();
    if( this.nb_rules == 0 )
      throw new Exception(LocaleKBCT.GetString("NoActiveRule"));

    // init des classes
    if( this.data_file != null )
      this.fis.InitClasses(this.data_file.ActiveFileName());

    this.fis.InitPossibles();
    // construction des menus
    this.jMenuBarInference = new JMenuBar();
    this.setJMenuBar(this.jMenuBarInference);
    this.jMenuLinks = new JMenuItem();
    this.jMenuFingrams = new JMenuItem();
    //this.jMenuFingrams = new JMenu();
    //this.jMenuFingramsWS = new JMenuItem();
    //this.jMenuFingramsWOS = new JMenuItem();
    //this.jMenuFingramsALL = new JMenuItem();
    this.jMenuPrint = new JMenuItem();
    this.jMenuExport = new JMenuItem();
    this.jMenuClose = new JMenuItem();
    this.jMenuHelp = new JMenuItem();
    this.jMenuData = new JMenu();
    this.jMenuSetDataValue = new JMenuItem();
    this.jMenuAllData = new JMenuItem();
    this.jMenuBarInference.add(this.jMenuData);
    this.jMenuData.add(this.jMenuSetDataValue);
    this.jMenuData.add(this.jMenuAllData);
    this.jMenuBarInference.add(this.jMenuFingrams);
    //this.jMenuFingrams.add(this.jMenuFingramsWS);
    //this.jMenuFingrams.add(this.jMenuFingramsWOS);
    //this.jMenuFingrams.add(this.jMenuFingramsALL);
    this.jMenuBarInference.add(this.jMenuLinks);
    this.jMenuBarInference.add(this.jMenuPrint);
    this.jMenuBarInference.add(this.jMenuExport);
    this.jMenuBarInference.add(this.jMenuHelp);
    this.jMenuBarInference.add(this.jMenuClose);
    jMenuSetDataValue.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuSetDataValue_actionPerformed(); } });
    jMenuAllData.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuAllData_actionPerformed(); } });
    if( this.data_file != null ) {
      jMenuData.setVisible(true);
      jMenuLinks.setVisible(true);
      jMenuFingrams.setVisible(true);
    } else {
      jMenuData.setVisible(false);
      jMenuLinks.setVisible(false);
      jMenuFingrams.setVisible(false);
    }
    jMenuLinks.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuLinks_actionPerformed(); } });
    jMenuFingrams.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuFingrams_actionPerformed("WOS"); } });
    //jMenuFingramsWS.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuFingrams_actionPerformed("WS"); } });
    //jMenuFingramsWOS.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuFingrams_actionPerformed("WOS"); } });
    //jMenuFingramsALL.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuFingrams_actionPerformed("ALL"); } });
    jMenuPrint.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuPrint_actionPerformed(); } });
    jMenuExport.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExport_actionPerformed(); } });
    jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuClose_actionPerformed(); } });
    jMenuHelp.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuHelp_actionPerformed(); } });
    // construction du modèle de données
    Object [][]data = new Object[this.nb_rules][this.nb_inputs + this.nb_outputs + 1];
    Object []column_names = new Object[this.nb_inputs + this.nb_outputs + 1];
    final Class []column_class = new Class[this.nb_inputs + this.nb_outputs + 1];
    column_names[0] = new String("Rules");
    column_class[0] = Integer.class;
    // construction des tableaux d'entrées et sorties
    for( int i=0 ; i<this.fis.NbInputs() ; i++ ) {
      column_names[i+1] = new String(this.fis.GetInput(i).GetName());
      column_class[i+1] = Integer.class;
    }
    for( int i=0 ; i<this.fis.NbOutputs() ; i++ ) {
      column_names[this.nb_inputs+i+1] = new String(this.fis.GetOutput(i).GetName());
      column_class[this.nb_inputs+i+1] = java.lang.Double.class;
    }
    // construction du tableau data
    for( int i=0, i_active=0 ; i<this.kbct.GetNbRules() ; i++ ) {
      Rule r= this.kbct.GetRule(i+1);
      if (r.GetActive()) {
        JRule rule= this.fis.GetRule(i_active);
        data[i_active][0] = new Integer(r.GetNumber()); // numero des règles actives
        for( int j=0 ; j<this.fis.NbInputs() ; j++ )
            data[i_active][j+1] = new Integer(rule.Facteurs()[j]);

        for( int j=0 ; j<this.fis.NbOutputs() ; j++ )
            data[i_active][this.nb_inputs+j+1] = new java.lang.Double(rule.Actions()[j]);

        i_active++;
      }
    }
    // construction du modèle triable
    this.rules_model = new SortableTableModel() {
   	  static final long serialVersionUID=0;	
      public Class getColumnClass(int col) { return column_class[col]; }
      public boolean isCellEditable(int row, int column) { return false; }
    };
    this.rules_model.setDataVector(data, column_names);
    this.rules_model.setSorter(new TableSorter(this.rules_model) {
      // modification de la classe de triage
      // surcharge de la fonction de tri de la classe TableSorter pour implementer les régles de tri suivantes:
      // - si 2 cellules sont égales, on tri sur les numéros de règles ascendants (colonne 0)
      // - sur les colonnes d'entrées:
      //     - si une cellule est nulle (pas de SEF), elle est toujours triée en fin de tableau
      public void sort(final int column, final boolean isAscent) {
        int n = model.getRowCount();
        int[] indexes = model.getIndexes();
        // affichage test du tri
        for (int i=0; i<n-1; i++) {
          int k = i;
          for (int j=i+1; j<n; j++) {
            if (isAscent) {
              // tri ascendant
              if( column < JInferenceFrame.this.nb_inputs+1 ) {
                // colonnes d'entrées
                if( this.Zero(column, j, k) == true ) // la cellule j est nulle et k > 0: on force le tri j > k
                  k = j;
                else if( (compare(column, j, k) < 0) && (((Number)model.getValueAt(j, column)).doubleValue() != 0) ) // k > j et j != 0
                  k = j;
                else if ((compare(column, j, k) == 0) && (compare(0, j, k) < 0)) // les cellules sont égales, tri sur les numéros de règles
                  k = j;
              } else {
                // colonnes de sorties
                if( compare(column, j, k) < 0 )
                  k = j;
                else if ((compare(column, j, k) == 0) && (compare(0, j, k) < 0)) // les cellules sont égales, tri sur les numéros de règles
                  k = j;
              }
            } else {
              // tri descendant
              if (compare(column, j, k) > 0)
                k = j;
              else if ((compare(column, j, k) == 0) && (compare(0, j, k) < 0)) // les cellules sont égales, tri sur les numéros de règles
                k = j;
            }
          }
          int tmp = indexes[i];
          indexes[i] = indexes[k];
          indexes[k] = tmp;
          // affichage test du tri
        }
      }
      // fonction utilisée par la fonction sort ci-dessus pour implementer le tri particulier des cellules d'entrées nulles
      // return true si row2 est nulle et row1 non nulle, false sinon
      public boolean Zero(int column, int row1, int row2) {
        Object o1 = model.getValueAt(row1, column);
        Object o2 = model.getValueAt(row2, column);
        if( (o1 == null) || (o2 == null) ) { return  false; }
        else if( (((Number)o2).doubleValue() == 0) && (((Number)o1).doubleValue() != 0) ) { return true; }
        return false;
      }
    });
    // construction des tables entrées et sorties
    this.tables_headers = new Vector();
    this.table_header_renderer = new SortButtonRenderer() {
   	  static final long serialVersionUID=0;	
      // renderer des entêtes des tables
      // surcharge de getTableCellRendererComponent pour traduction de la colonne règle et ajout des tooltips
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if( table.convertColumnIndexToModel(column) == 0 ) {
          ((JButton)comp).setText(LocaleKBCT.GetString(value.toString()));
          ((JButton)comp).setToolTipText(null);
        }
        try {
          if( (table.convertColumnIndexToModel(column) > 0) && (table.convertColumnIndexToModel(column) <= JInferenceFrame.this.nb_inputs) )
            ((JButton)comp).setToolTipText(JInferenceFrame.this.fis.GetInput(table.convertColumnIndexToModel(column)-1).GetName());

          if( table.convertColumnIndexToModel(column) > JInferenceFrame.this.nb_inputs )
            ((JButton)comp).setToolTipText(JInferenceFrame.this.fis.GetOutput(table.convertColumnIndexToModel(column)-JInferenceFrame.this.nb_inputs-1).GetName());
        } catch( Throwable t ) {}
        return comp;
      }
    };
    JScrollPane jsp_inputs = this.InputsTable();
    JScrollPane jsp_outputs = this.OutputsTable();
    // tri des colonnes de la table
    if( this.sort_col != -1 ) {
      this.table_header_renderer.setPressedColumn(this.sort_col);
      this.table_header_renderer.setSelectedColumn(this.sort_col);
      if( this.is_ascent == false ) // fait un appel supplémentaire à SortButtonRenderer.setSelectedColumn pour mettre la variable SortButtonRenderer.state dans le bon état
        this.table_header_renderer.setSelectedColumn(this.sort_col);

      ((SortableTableModel)this.inputs_table.getModel()).sortByColumn(this.sort_col, this.is_ascent);
      this.table_header_renderer.setPressedColumn(-1);
    }
    // asservi la scrollbar verticale de la table sortie sur la scrollbar verticale de la table entrée
    JScrollBar inputs_Bar = jsp_inputs.getVerticalScrollBar();
    JScrollBar outputs_Bar = jsp_outputs.getVerticalScrollBar();
    jsp_outputs.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER); // cache la scrollbar verticale
    // modèle de scroll bar
    this.scroll_model = new DefaultBoundedRangeModel();
    inputs_Bar.setModel( this.scroll_model );
    outputs_Bar.setModel( this.scroll_model );
    // tables dans un JSplitPane
    if( this.split_pane != null )
      this.getContentPane().remove(this.split_pane);

    this.split_pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jsp_inputs, jsp_outputs ) {
   	  static final long serialVersionUID=0;	
      public int getDividerSize() { return 2; }
      public Dimension getMinimumSize() { return  new Dimension(300,200); }
    };
    // configure le redimensionnement du JSplitPane en fonction du nombres d'entrées et sorties actives
    int nb_active_inputs = this.fis.NbInputs();
    int nb_active_outputs = this.fis.NbOutputs();
    ((JSplitPane)this.split_pane).setResizeWeight((1.0/(nb_active_inputs+nb_active_outputs))*nb_active_inputs);
    // listener de SIF
    if( this.KBCTListener != null )
      this.fis.RemoveKBCTListener(this.KBCTListener);

    this.KBCTListener = new KBCTListener() {
      public void KBCTClosed() { JInferenceFrame.this.dispose(); }
      public void InputReplaced( int input_number )   { JInferenceFrame.this.ReInit(); }
      public void OutputReplaced( int output_number ) { JInferenceFrame.this.ReInit(); }
      public void InputActiveChanged( int input_number )   { JInferenceFrame.this.ReInit(); }
      public void OutputActiveChanged( int output_number ) { JInferenceFrame.this.ReInit(); }
      public void InputNameChanged( int input_number )   { JInferenceFrame.this.ReInit(); }
      public void OutputNameChanged( int output_number ) { JInferenceFrame.this.ReInit(); }
      public void InputPhysicalRangeChanged( int input_number ) {}
      public void InputInterestRangeChanged( int input_number ) {JInferenceFrame.this.ReInit();}
      public void OutputPhysicalRangeChanged( int output_number ) {}
      public void OutputInterestRangeChanged( int output_number ) {JInferenceFrame.this.ReInit();}
      public void InputRemoved( int input_number ) {
        JInferenceFrame.this.InputsParameters.removeElementAt(input_number);
        JInferenceFrame.this.ReInit();
      }
      public void OutputRemoved( int output_number ) {
        JInferenceFrame.this.OutputsParameters.removeElementAt(output_number);
        JInferenceFrame.this.ReInit();
      }
      public void InputAdded( int input_number ) {
        JInferenceFrame.this.InputsParameters.add(input_number, new Parameters());
        JInferenceFrame.this.ReInit();
      }
      public void OutputAdded( int output_number ) {
        JInferenceFrame.this.OutputsParameters.add(output_number, new Parameters());
        JInferenceFrame.this.ReInit();
      }
      public void MFRemovedInInput( int input_number, int mf_number )    { JInferenceFrame.this.ReInit(); }
      public void MFRemovedInOutput( int output_number, int mf_number )  { JInferenceFrame.this.ReInit(); }
      public void MFReplacedInInput( int input_number )   { JInferenceFrame.this.ReInit(); }
      public void MFReplacedInOutput( int output_number ) { JInferenceFrame.this.ReInit(); }
      public void OutputDefuzChanged( int output_number ) { JInferenceFrame.this.ReInit(); }
      public void OutputDisjChanged( int output_number )  { JInferenceFrame.this.ReInit(); }
      public void OutputClassifChanged( int output_number ) { JInferenceFrame.this.ReInit(); }
      public void MFAddedInInput( int input_number ) {}
      public void MFAddedInOutput( int output_number ) {}
      public void OutputDefaultChanged( int output_number ) { JInferenceFrame.this.ReInit(); }
      public void OutputAlarmThresChanged( int output_number ) { JInferenceFrame.this.ReInit(); }
      public void RulesModified() { JInferenceFrame.this.ReInit(); }
      public void ConjunctionChanged() { JInferenceFrame.this.ReInit(); }
    };
    this.fis.AddKBCTListener(this.KBCTListener);
    this.Translate();
    this.getContentPane().add(this.split_pane);
  }
//------------------------------------------------------------------------------
  /**
   * Fermeture de la fenêtre
   */
  public void dispose() {
    super.dispose();
    JKBCTFrame.RemoveTranslatable(this);
    this.frame_open.Release();
    this.fis.RemoveKBCTListener(this.KBCTListener);
    if (this.Parent.jrfi!=null)
      this.Parent.jrfi.dispose();
  }
//------------------------------------------------------------------------------
  void jMenuPrint_actionPerformed() {
    try {
      Printable p= new Printable() {
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
          if (pageIndex >= 1)
              return Printable.NO_SUCH_PAGE;
          else
              return JInferenceFrame.this.print(graphics, pageFormat);
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
          JInferenceFrame.this.split_pane.paint(g);
          g.translate(0, JInferenceFrame.this.split_pane.getHeight());
          JInferenceFrame.this.split_pane.paint(g);
          g.setColor(Color.gray);
          g.drawLine(0,0,0, JInferenceFrame.this.split_pane.getHeight()-1);
        }
      };
      panel.setSize(new Dimension(this.split_pane.getWidth(), this.split_pane.getHeight()));
      export.showExportDialog( panel, "Export view as ...", panel, "export" );
    } catch (Exception ex) { MessageKBCT.Error(null, ex); }
  }
//------------------------------------------------------------------------------
  public void jMenuLinks_actionPerformed() {
    try {
    	this.jlf= new JLinksFrame(this, this.fis, this.data_file_name, false, false);
    } catch( Throwable t ) { 
        //t.printStackTrace();
    	MessageKBCT.Error(null, t);
    }
  }
//------------------------------------------------------------------------------
  public void jMenuFingrams_actionPerformed(String smoteopt) {
    try {
    	// Include instance-based fingram
        //System.out.println("JInferenceFrame: crear FINGRAMS: INI");
    	if ( (!MainKBCT.getConfig().GetTESTautomatic()) && (!MainKBCT.getConfig().GetTutorialFlag()) && (this.Parent!=null) && (this.Parent.Temp_kbct!=null) ) {
            if (this.Parent.jef != null) {
     		    this.Parent.jef.AssessingInterpretability(true);
        	    this.Parent.jef.setPathFinderParameters((int)this.Parent.jef.jkbif.getMaxSFR(),this.Parent.jef.Temp_kbct.GetNbActiveRules());
            } else {
     		    this.Parent_EF.AssessingInterpretability(true);
        	    this.Parent_EF.setPathFinderParameters((int)this.Parent_EF.jkbif.getMaxSFR(),this.Parent_EF.Temp_kbct.GetNbActiveRules());
            }
     	}
    	if ( (smoteopt.equals("WS")) || (smoteopt.equals("ALL")) ) {
    		MainKBCT.getConfig().SetFingramWS(true);
    	    this.jlf= new JLinksFrame(this, this.fis, this.data_file_name, true, true);
    	} else {
    		MainKBCT.getConfig().SetFingramWS(false);
    	}
    	if (  (smoteopt.equals("WOS")) || (smoteopt.equals("ALL"))  ) {
    		boolean aux=false, flag= false;
    		if (smoteopt.equals("ALL")) {
    			aux= MainKBCT.getConfig().GetTESTautomatic();
    			flag= true;
    			MainKBCT.getConfig().SetTESTautomatic(true);
    		}
    		MainKBCT.getConfig().SetFingramWOS(true);
    	    //System.out.println("JInferenceFrame: crear JLinkFrames: INI");
    		this.jlf= new JLinksFrame(this, this.fis, this.data_file_name, true, false);
    	    //System.out.println("JInferenceFrame: crear JLinkFrames: END");
    	    if (flag)
			    MainKBCT.getConfig().SetTESTautomatic(aux);
    	} else {
    		MainKBCT.getConfig().SetFingramWOS(false);
    	}
    	if (!this.jlf.getCancelFlag()) {
    	  boolean reg= false;
    	  if (this.Parent.Temp_kbct.GetOutput(1).GetClassif().equals("no"))
	        reg= true;

    	  if (!MainKBCT.getConfig().GetTESTautomatic()) {
    		boolean aux= MainKBCT.getConfig().GetTESTautomatic();
    		MainKBCT.getConfig().SetTESTautomatic(true);
            double[] accind= new double[4];
 	        double[] intind= new double[6];
 	        double fingramsIntIndex= this.jlf.getCIS();
 	        boolean[] covdata;
 	        if (this.Parent.jef!=null) {
    		    this.Parent.jef.AssessingAccuracy(true,false);
                accind[0]= this.Parent.jef.jrbqf.getCoverage();
                if (reg) {
 	               accind[1]= this.Parent.jef.jrbqf.getRMSE();
 	               accind[2]= this.Parent.jef.jrbqf.getMSE();
 	               accind[3]= this.Parent.jef.jrbqf.getMAE();
                } else {
                   accind[1]= this.Parent.jef.jrbqf.getAccIndexLRN();
                   accind[2]= this.Parent.jef.jrbqf.getAvConfFD();
 	            }
                covdata= this.Parent.jef.jrbqf.getCovData();
 	            this.Parent.jef.jrbqf.dispose();
 	            this.Parent.jef.jrbqf= null;
 	            MainKBCT.getConfig().SetTESTautomatic(aux);
   	            intind= this.Parent.jef.jkbif.getInterpretabilityIndicators();
 	            this.Parent.jef.jkbif.dispose();
 	            this.Parent.jef.jkbif= null;
    		} else {
    		    this.Parent_EF.AssessingAccuracy(true,false);
                accind[0]= this.Parent_EF.jrbqf.getCoverage();
                if (reg) {
 	               accind[1]= this.Parent_EF.jrbqf.getRMSE();
 	               accind[2]= this.Parent_EF.jrbqf.getMSE();
 	               accind[3]= this.Parent_EF.jrbqf.getMAE();
                } else {
                   accind[1]= this.Parent_EF.jrbqf.getAccIndexLRN();
                   accind[2]= this.Parent_EF.jrbqf.getAvConfFD();
 	            }
                covdata= this.Parent_EF.jrbqf.getCovData();
 	            this.Parent_EF.jrbqf.dispose();
 	            this.Parent_EF.jrbqf= null;
 	            MainKBCT.getConfig().SetTESTautomatic(aux);
   	            intind= this.Parent_EF.jkbif.getInterpretabilityIndicators();
 	            this.Parent_EF.jkbif.dispose();
 	            this.Parent_EF.jkbif= null;
    		}
    	    //System.out.println("JInferenceFrame: visualizeSVG: INI");
    		this.Parent.visualizeSVG(smoteopt, reg, accind, covdata, intind, fingramsIntIndex, null);
    	    //System.out.println("JInferenceFrame: visualizeSVG: END");
    	  }
    	}
    } catch( Throwable t ) { 
        t.printStackTrace();
    	MessageKBCT.Error(null, t);
    }
    //System.out.println("JInferenceFrame: crear FINGRAMS: END");
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
     this.split_pane.print( g2 );
     return Printable.PAGE_EXISTS;
 }
//------------------------------------------------------------------------------
  public void jMenuClose_actionPerformed() {
	  if (this.jfc!=null) {
	      for (int n=0; n<this.jfc.length; n++) {
	    	   if (this.jfc[n]!=null)
	    	       this.jfc[n].dispose();
	      }
	  }
	  if (this.jlf!=null) {
		  this.jlf.dispose();
	  }
      this.dispose();
      this.kbct.Close();
      this.kbct.Delete();
 }
//------------------------------------------------------------------------------
  public void jMenuHelp_actionPerformed() {
      MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonInference.html"));
	  MainKBCT.getJB().setVisible(true);
	  SwingUtilities.updateComponentTreeUI(this);
  }
//------------------------------------------------------------------------------
  public void jMenuSetDataValue_actionPerformed() {
    try {
	  this.data_file = new JExtendedDataFile(this.data_file_name+".active", true);
	  int NbData= this.data_file.GetActiveCount();
      Integer op= (Integer)MessageKBCT.SelectDataValue(this,NbData);
      int option= -1;
      if (op!=null)
        option= op.intValue();

      if (option>=0)
        this.setData(option, false, 0);
    } catch (Throwable t) {
        //t.printStackTrace();
        MessageKBCT.Error("JInferenceFrame: jMenuSetDataValue_actionPerformed:", t);
    }
  }
//------------------------------------------------------------------------------
  public void jMenuAllData_actionPerformed() {
    try {
  	  this.data_file = new JExtendedDataFile(this.data_file_name+".active", true);
  	  int opt=1;
  	  if (!MainKBCT.getConfig().GetTESTautomatic())
          opt= MessageKBCT.SelectMode(this, LocaleKBCT.GetString("SelectMode"));
  	  
      if ( (opt==1) && (!MainKBCT.getConfig().GetTESTautomatic()) )
        MessageKBCT.Information(this, LocaleKBCT.GetString("Processing")+" ..."+"\n"+
                                      "   "+LocaleKBCT.GetString("TakeLongTime")+"\n"+
                                      "... "+LocaleKBCT.GetString("PleaseWait")+" ...");
      if (opt!=-1) {
        int NbData= this.data_file.GetActiveCount();
        this.inferedValues= new Vector();
        this.aggregatedValues= new Vector();
        for (int option=0; option<NbData; option++) {
          if (option>=0) {
            if (this.setData(option+1, true, opt) > 0)
              break;
          }
        }
        String log= this.saveLogInferedValues();
        this.inferedValues= null;
        this.aggregatedValues= null;
        if (this.jfc[this.jfc.length-1]!=null)
          this.jfc[this.jfc.length-1].dispose();

        try {
          if ( (!log.equals("")) && (!MainKBCT.getConfig().GetTESTautomatic()) )
        	  this.jfc[this.jfc.length-1]= new JFISConsole(this.Parent, log, false);
        } catch (Throwable t) {
          //t.printStackTrace();
          MessageKBCT.Error("JInferenceFrame: jMenuAllData_actionPerformed: error printing log file with infered values in JFISConsole", t);
        }
      }
    } catch (Throwable t) {
	        //t.printStackTrace();
	        MessageKBCT.Error("JInferenceFrame: jMenuAllData_actionPerformed:", t);
	}
  }
//------------------------------------------------------------------------------
  private String saveLogInferedValues() {
    String result="";
    try {
      File LogFile= JKBCTFrame.BuildFile("inferedValuesLogFile.txt");
      if (MainKBCT.getConfig().GetTESTautomatic()) {
          File auxname= new File(this.data_file_name);
          //System.out.println(auxname.getName());
    	  LogFile= JKBCTFrame.BuildFile("inferedValuesLogFile."+auxname.getName()+".txt");
      }
      
      PrintWriter fOut = new PrintWriter(new FileOutputStream(LogFile), true);
  	  fOut.println("++++++++++++++++++++++++++++++++++");
      Date date= new Date(System.currentTimeMillis());
      fOut.println("BEGIN: "+DateFormat.getDateTimeInstance().format(date));
      result= LogFile.getAbsolutePath();
      fOut.println(result);
      String title= LocaleKBCT.GetString("Data")+ "   ";
      if (this.fis.NbActiveOutputs() == 1) {
          title= title + LocaleKBCT.GetString("infered") + "   " +LocaleKBCT.GetString("observed") + "   " + LocaleKBCT.GetString("Measures");
      } else {
          for (int k=0; k<this.fis.NbActiveOutputs(); k++) {
            title= title + LocaleKBCT.GetString("infered") + (k+1) + "   " +LocaleKBCT.GetString("observed") + (k+1) + "   " + LocaleKBCT.GetString("Measures") + "   ";
          }
      }
      fOut.println(title);

      Enumeration en_infer= this.inferedValues.elements();
      Enumeration en_aggre= this.aggregatedValues.elements();

      int cont=0;
      DecimalFormat df= new DecimalFormat();
      df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
      df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
      DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
      dfs.setDecimalSeparator((new String(".").charAt(0)));
      df.setDecimalFormatSymbols(dfs);
      df.setGroupingSize(20);
      while (en_infer.hasMoreElements()) {
        String aux= ""+(cont+1);
          int i=this.fis.NbActiveOutputs();
          while (i>0) {
            for (int k=0; k<this.fis.NbActiveOutputs()-1; k++) {
              en_aggre.nextElement();
            }
            Vector v= (Vector)en_aggre.nextElement();
            Object[] d= v.toArray();
            for (int n=0; n<d.length; n++) {
              aux= aux + "            " + df.format(en_infer.nextElement()) + "            " + df.format(this.data_file.VariableData(this.data_file.VariableCount()-i)[cont]);
              Vector vv= (Vector)d[n];
              Object[] dd= vv.toArray();
              for (int m=0; m<dd.length; m++) {
                aux= aux + "            " + "c" + (int)((Double)dd[m]).doubleValue() + "   " + df.format(((Double)dd[m+1]).doubleValue());
                m++;
              }
              i--;
            }
          }
        cont++;
        fOut.println(aux);
      }
      fOut.println();
      date= new Date(System.currentTimeMillis());
  	  fOut.println("END "+DateFormat.getDateTimeInstance().format(date));
      fOut.flush();
      fOut.close();
    } catch (Throwable t) {
        t.printStackTrace();
        MessageKBCT.Error("JInferenceFrame: saveLogInferedValues: error saving log file with infered values", t);
    }
    return result;
  }
//------------------------------------------------------------------------------
  private int setData(int option, boolean loop, int opt) {
    double[] data= this.data_file.GetData()[option-1];
    this.sliders_value = new Vector();
    for(int i=0; i<this.nb_inputs; i++)
      this.sliders_value.add(new java.lang.Double((double)Math.round(1000*data[i])/1000));

    this.ReInit();

    if (opt==0) {
      String Msg= LocaleKBCT.GetString("NbData")+": "+ option +"\n";
      Msg= Msg + LocaleKBCT.GetString("OutputShouldBe")+": "+"\n";
      if (this.nb_outputs==1) {
          Msg= Msg + "    " + LocaleKBCT.GetString("Output")+" = "+ data[data.length-1] +"\n";
      } else {
          for (int i=0; i<this.nb_outputs; i++)
             Msg= Msg + "    " + LocaleKBCT.GetString("Output")+" "+String.valueOf(i+1)+" = "+ data[this.nb_inputs+i] +"\n";
      }
      if (!loop)
        return MessageKBCT.Information(this,Msg);
      else
        return MessageKBCT.Confirm(this, Msg, 0);
    } else
        return 0;
  }
//------------------------------------------------------------------------------
  protected void setData(double[] data) {
    this.sliders_value = new Vector();
    for(int i=0; i<this.nb_inputs; i++)
      this.sliders_value.add(new java.lang.Double((double)Math.round(1000*data[i])/1000));

    this.ReInit();
  }
//------------------------------------------------------------------------------
  public JInferenceFrame( JKBCTFrame parent, JFIS fis, JSemaphore open, JExtendedDataFile data_file, JKBCT kbct ) throws Throwable {
    super(parent);
    this.Parent= parent;
    if (this.Parent.jef==null)
        this.Parent_EF= (JExpertFrame)parent;
    	
    //System.out.println("JInferenceFrame: 1216");
    this.InitJIF(fis, open, data_file, kbct);
  }
//------------------------------------------------------------------------------
  private void InitJIF(JFIS fis, JSemaphore open, JExtendedDataFile data_file, JKBCT kbct) throws Throwable {
	    this.fis = fis;
	    this.kbct= kbct;
	    int lim= this.kbct.GetNbRules();
	    this.rulesIndex= new int[lim];
	    int c=0;
	    for (int n=0; n<lim; n++) {
	    	 if (this.kbct.GetRule(n+1).GetActive())
	    	     this.rulesIndex[n]= c++;
	    	 else
	    	     this.rulesIndex[n]= -1;
	    }
	    //for (int n=0; n<lim; n++) {
	    	//System.out.println("this.rulesIndex[n] -> "+this.rulesIndex[n]);
	    //}
	    this.frame_open = open;
	    this.data_file= data_file;
	    if (this.data_file!=null)
	        this.data_file_name= data_file.FileName();

	    // construction des valeurs initiales des curseurs et domaines d'affichage
	    this.sliders_value = new Vector();
	    this.infer_range = new Vector();
	    for( int i=0 ; i<this.fis.NbInputs() ; i++ ) {
	      double []range = this.fis.GetInput(i).GetRange();
	      this.sliders_value.add(new java.lang.Double(range[0] + ((range[1] - range[0])/2)));
	      this.infer_range.add(range);
	    }
	    this.Init();
	    JKBCTFrame.AddTranslatable(this);
	    this.pack();
	    this.frame_open.Acquire();
	    this.setLocation(this.ChildPosition(this.getSize()));
  }
  //------------------------------------------------------------------------------
  /**
   * Traduction de la fenêtre
   */
  public void Translate() throws Throwable {
    this.setTitle(LocaleKBCT.GetString("Inference"));
    this.jMenuData.setText(LocaleKBCT.GetString("Data"));
    this.jMenuSetDataValue.setText(LocaleKBCT.GetString("setDataValue"));
    this.jMenuAllData.setText(LocaleKBCT.GetString("AllData"));
    this.jMenuSetDataValue.setToolTipText(LocaleKBCT.GetString("setDataValue"));
    this.jMenuLinks.setText(LocaleKBCT.GetString("Links"));
    this.jMenuFingrams.setText(LocaleKBCT.GetString("Fingrams"));
    //this.jMenuFingramsWS.setText(LocaleKBCT.GetString("FingramsWS"));
    //this.jMenuFingramsWOS.setText(LocaleKBCT.GetString("FingramsWOS"));
    //this.jMenuFingramsALL.setText(LocaleKBCT.GetString("FingramsALL"));
    /*if (this.kbct.GetOutput(1).GetClassif().equals("yes")) {
    	this.jMenuFingramsWS.setEnabled(true);
    	this.jMenuFingramsALL.setEnabled(true);
    } else {
    	this.jMenuFingramsWS.setEnabled(false);
    	this.jMenuFingramsALL.setEnabled(false);
    }*/
    this.jMenuPrint.setText(LocaleKBCT.GetString("Print"));
    this.jMenuExport.setText(LocaleKBCT.GetString("Export"));
    this.jMenuClose.setText(LocaleKBCT.GetString("Close"));
    this.jMenuHelp.setText(LocaleKBCT.GetString("Help"));
    this.repaint();
  }
//------------------------------------------------------------------------------
  /**
   * Classe renderer des cellules de la table des sorties nette\n
   * affiche le poids des règles et entre parenthèses la conclusion des règles
   */
class CrispRenderer extends JPanel implements TableCellRenderer {
  static final long serialVersionUID=0;	
  JLabel label_weight = new JLabel(); /**< label poids */
  JLabel label_conc = new JLabel(); /**< label conclusion */
//------------------------------------------------------------------------------
  public CrispRenderer() {
    this.setLayout(new GridBagLayout());
    this.add(this.label_weight, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(this.label_conc,  new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
  }
//------------------------------------------------------------------------------
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    int rule_number = row;
    double poids = 0;
    try { poids = JInferenceFrame.this.fis.GetRule(rule_number).Poids(); } catch( Throwable t ) {}
      this.label_weight.setText(DecimalFormat.getInstance().format(poids)); // affichage poids
      this.label_conc.setText("(" + DecimalFormat.getInstance().format(value) + ")"); // affichage conclusion
      return this;
    }
  }
//------------------------------------------------------------------------------
  /**
   * Classe renderer des cellules agrégation de la table des sorties nettes\n
   * affiche les résultats d'agrégation dans un objet FISPlot
   */
class CrispAgregationRenderer extends FuzzyAgregationRenderer {
  static final long serialVersionUID=0;	
//------------------------------------------------------------------------------
  /**
   * Constructeur
   * @param output objet JOutput de la sortie
   * @param output_number numéro d'ordre de la sortie dans le SIF
   */
  public CrispAgregationRenderer( JOutput output, int output_number ) throws Throwable { super(output, output_number); }
//------------------------------------------------------------------------------
  /**
   * Affichage des résultats d'agrégation sous la forme de SEFs discret
   */
  protected void ShowMF(Graphics g) throws Throwable {
    this.clear(false);
    double [][] result = JInferenceFrame.this.fis.AgregationResult(this.number);
    for( int i=0 ; i<result[0].length ; i++ ) {
      double [] param = { result[0][i] };
      fis.JMF mf = new fis.JMFDiscrete("", param);
      mf.Draw(this, 9); // data set 9 pour affichage en gris
      mf.Delete();
    }
  }
//------------------------------------------------------------------------------
  /**
   * Affichage des résultats d'agrégation
   */
  protected void ShowAlphaKernel(Graphics g) throws Throwable {
    double [][] result = JInferenceFrame.this.fis.AgregationResult(this.number);
    for( int i=0 ; i<result[0].length ; i++ ) {
      double [] param = { result[0][i] };
      fis.JMF mf = new fis.JMFDiscrete("", param );
      if( java.lang.Double.isNaN((double)result[1][i]) == false ) {
        mf.DrawAlphaKernel(this, (Graphics2D)g, result[1][i]);
        Double alpha= new Double(result[1][i]);
        String v= DecimalFormat.getInstance().format(alpha);
        int x= this.XtoPixel(mf.GetParams()[0]);
        if ((i==result[0].length-1) && (alpha.doubleValue()!=0))
          x= x-20;

        int y= 10;
        if (alpha.doubleValue()!=1)
          y= this.YtoPixel(alpha.doubleValue());

        g.drawString(v, x, y);
      }
      mf.Delete();
    }
  }
}
//------------------------------------------------------------------------------
  /**
   * Classe renderer des cellules agrégation de la table des sorties floues\n
   * affiche les résultats d'agrégation dans un objet FISPlot
   */
class FuzzyAgregationRenderer extends FuzzyRenderer {
  static final long serialVersionUID=0;	
//------------------------------------------------------------------------------
  /**
   * Constructeur
   * @param output objet JOutput de la sortie
   * @param output_number numéro d'ordre de la sortie dans le SIF
   */
  public FuzzyAgregationRenderer( JOutput output, int output_number ) throws Throwable { super(output, output_number); }
//------------------------------------------------------------------------------
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) { return this; }
//------------------------------------------------------------------------------
  /**
   * Affichage des SEF de la sortie
   */
  protected void ShowMF(Graphics g) throws Throwable {
    this.clear(false);
    for( int i=0 ; i<this.input.GetNbMF() ; i++ )
      this.input.GetMF(i).NewMF().Draw(this, 9); // data set 9 pour affichage en gris
  }
//------------------------------------------------------------------------------
  /**
   * Affichage des résultats d'agrégation
   */
  protected void ShowAlphaKernel(Graphics g) throws Throwable {
    double [][] result = JInferenceFrame.this.fis.AgregationResult(this.number);
    for( int i=0 ; i<result[0].length ; i++ )
      if( ((int)result[0][i] != 0) && (java.lang.Double.isNaN((double)result[1][i]) == false) ) {
        fis.JMF mf= this.input.GetMF((int)result[0][i]-1).NewMF();
        mf.DrawAlphaKernel(this, (Graphics2D)g, result[1][i]);
      }
    }
  }
//------------------------------------------------------------------------------
  /**
   * Classe renderer des cellules de la table des sorties floues\n
   * affiche le SEF et le poids d'appartenance au SEF dans un objet FISPlot
   */
class FuzzyRenderer extends InputRenderer {
  static final long serialVersionUID=0;	
  int rule_number; /**< numéro de la règle */
//------------------------------------------------------------------------------
  /**
   * Constructeur
   * @param output objet JOutput de la sortie
   * @param output_number numéro d'ordre de la sortie dans le SIF
   */
  public FuzzyRenderer( JOutput output, int output_number ) throws Throwable { super(output, output_number); }
//------------------------------------------------------------------------------
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    int ind = ((Integer)((DefaultTableModel)table.getModel()).getValueAt(row, 0)).intValue()-1;
    this.rule_number = JInferenceFrame.this.rulesIndex[ind];
    this.sef_number = ((java.lang.Double)value).intValue();
    try {
      if( this.sef_number != 0 ) {
      	//System.out.println("NbRules -> "+JInferenceFrame.this.kbct.GetNbRules());
    	//System.out.println("NbActiveRules -> "+JInferenceFrame.this.kbct.GetNbActiveRules());
    	//System.out.println("JInferenceFrame 1413: "+this.rule_number+"  "+this.sef_number);
        double val= this.Alpha();
    	//System.out.println("JInferenceFrame 1415");
        DecimalFormat df= new DecimalFormat();
        df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
        df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
        DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
        dfs.setDecimalSeparator((new String(".").charAt(0)));
        df.setDecimalFormatSymbols(dfs);
        df.setGroupingSize(20);
        this.setToolTipText(this.input.GetMF(this.sef_number-1).GetName()+"  ("+df.format(val)+")");
      }
    } catch( Throwable t ) {
      //t.printStackTrace();
      MessageKBCT.Error( "Error in JInferenceFrame: FuzzyRenderer: getTableCellRendererComponent", t);
    }
    return ((java.lang.Double)value).intValue() == 0 ? new JPanel() : this; // retourne un JPanel vide si pas de SEF
  }
//------------------------------------------------------------------------------
    /**
     * Fixe le domaine d'affichage en abscisse de l'objet FISPlot\n
     * domaine d'affichage = domaine de la sortie
     */
  protected void SetXRange() throws Throwable {
    double []range = this.input.GetRange();
    this.setXRange(range[0], range[1]);
  }
//------------------------------------------------------------------------------
  /**
   * Lecture du poids de la règle
   * @return le poids de la règle FuzzyRenderer#rule_number
   */
  protected double Alpha() throws Throwable { return JInferenceFrame.this.fis.GetRule(this.rule_number).Poids(); }
//------------------------------------------------------------------------------
  protected void ShowRange(Graphics g) throws Throwable {} /**< pas d'affichage du domaine de la sortie */
//------------------------------------------------------------------------------
  protected void ShowSlider(Graphics g) {} /**< pas d'affichage du curseur */
  }
//------------------------------------------------------------------------------
  /**
   * Classe renderer des cellules de la table des entrées\n
   * affiche le SEF et le degré d'appartenance au SEF dans un objet FISPlot
   */
class InputRenderer extends FISPlot implements TableCellRenderer {
  static final long serialVersionUID=0;	
  protected JInput input;  /**< objet entrée */
  protected int sef_number; /**< numéro du SEF à afficher */
  protected int number; /**< numéro d'ordre de l'entrée dans le SIF */
//------------------------------------------------------------------------------
  /**
   * Constructeur
   * @param input objet JInput de l'entrée
   * @param input_number numéro d'ordre de l'entrée dans le SIF
   */
  private InputRenderer( JInput input,  int input_number ) throws Throwable {
    this.input = input;
    this.number = input_number;
    this.setYRange(0, 1);
    // ajout de ticks invisible
    this.addXTick("", java.lang.Double.MAX_VALUE);
    this.addYTick("", java.lang.Double.MAX_VALUE);
    FISPlot.DisableZoom(this);
    this.setForeground(UIManager.getColor("Button.background"));
  }
//------------------------------------------------------------------------------
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    this.sef_number = ((Integer)value).intValue(); // le numéro du SEF à afficher est lu dans la table
    // ajout d'un tooltip avec le nom du SEF
    try {
      if( this.sef_number != 0 ) {
        double val= this.Alpha();
        DecimalFormat df= new DecimalFormat();
        df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
        df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
        DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
        dfs.setDecimalSeparator((new String(".").charAt(0)));
        df.setDecimalFormatSymbols(dfs);
        df.setGroupingSize(20);
        this.setToolTipText(this.input.GetMF(this.sef_number-1).GetName()+"  ("+df.format(val)+")");
      }
    } catch( Throwable t ) {
       //t.printStackTrace();
       MessageKBCT.Error( "Error in JInferenceFrame: InputRenderer: getTableCellRendererComponent", t);
    }
    return ((Integer)value).intValue() == 0 ? new JPanel() : this; // retourne un JPanel vide si pas de SEF
  }
//------------------------------------------------------------------------------
    /**
     * Surcharge de Plot._drawLine pour affichage en épaisseur 0.1
     */
  protected void _drawLine(java.awt.Graphics graphics, int dataset, long startx, long starty, long endx, long endy, boolean clip, float width)
    {super._drawLine(graphics, dataset, startx, starty, endx, endy, clip, (float)0.1);}
//------------------------------------------------------------------------------
    /**
     * Fixe le domaine d'affichage en abscisse de l'objet FISPlot\n
     * domaine d'affichage = domaine d'inférence de l'entrée
     */
  protected void SetXRange() throws Throwable {
    double []range = (double [])JInferenceFrame.this.infer_range.elementAt(this.number);
    this.setXRange(range[0], range[1]);
  }
//------------------------------------------------------------------------------
  /**
   * Lecture du degré d'appartenance au SEF
   * @return degré d'appartenance au SEF
   */
  protected double Alpha() throws Throwable { return this.input.Appartenance()[sef_number-1]; }
//------------------------------------------------------------------------------
  /**
   * Affichage du domaine de l'entrée en pointillé rouge si le domaine d'inférence est supérieur au domaine de l'entrée
   */
  protected void ShowRange(Graphics g) throws Throwable {
    double []input_range = this.input.GetRange();
    double []infer_range = (double [])JInferenceFrame.this.infer_range.elementAt(this.number);
    if( (input_range[0] != infer_range[0]) || (input_range[1] != infer_range[1]) )
      FISPlot.DrawXInputRange(g, this, input_range, this._xMin, this._xMax);
  }
//------------------------------------------------------------------------------
  /**
   * Affichage du curseur
   */
  protected void ShowSlider(Graphics g) {
    Rectangle rect = this.getPlotRectangle();
    if( rect.height > 0 ) {
      Graphics2D g2d = (Graphics2D)g;
      g2d.setColor(Color.black);
      g2d.setStroke(new BasicStroke());
      int x = this.XtoPixel(((java.lang.Double)JInferenceFrame.this.sliders_value.elementAt(this.number)).doubleValue());
      g2d.draw(new Line2D.Double(x, rect.y, x, rect.y+rect.height) );
    }
  }
//------------------------------------------------------------------------------
  /**
   * Affichage du SEF
   */
  protected void ShowMF(Graphics g) throws Throwable {
    this.clear(false);
    this.input.GetMF(this.sef_number-1).NewMF().Draw(this, 9); // data set 9 pour affichage en gris
  }
//------------------------------------------------------------------------------
  /**
   * Affichage du degré d'appartenance au SEF
   */
  protected void ShowAlphaKernel(Graphics g) throws Throwable  { this.input.GetMF(this.sef_number-1).NewMF().DrawAlphaKernel(this, (Graphics2D)g, this.Alpha()); }
//------------------------------------------------------------------------------
  /**
   * Affichage de l'objet FISPlot
   */
  public void paintComponent(Graphics g) {
    try {
      // domaine d'affichage
      this.SetXRange();
      // affichage du SEF
      this.ShowMF(g);
      super.paintComponent(g);
      // ajustement de la taille du rectangle plot au panel
      Rectangle r = this.getPlotRectangle();
      r.x = 0;
      r.y = 0;
      r.width = this.getSize().width -1;
      r.height = this.getSize().height -1;
      this.setPlotRectangle(r);
      super.paintComponent(g);
      this.setPlotRectangle(null);  // supprime la memorisation du setPlotRectangle
      // affichage noyau
      this.ShowAlphaKernel(g);
      // affichage des bornes du domaine de l'entree
      this.ShowRange(g);
      // affichage du curseur
      this.ShowSlider(g);
    } catch( Throwable t ) { MessageKBCT.Error( null, t ); }
  }
};
//------------------------------------------------------------------------------
  public void setSliderInputValue(int input_number, double new_value) {
    sliders[input_number].setDoubleValue(new_value);
  }
//------------------------------------------------------------------------------
/**
 * Classe curseur\n
 * surcharge de la classe JSlider pour gestion de la position du curseur en double. la classe JSlider gère la position du curseur en int.
 */
class JDoubleSlider extends JSlider {
  static final long serialVersionUID=0;	
  final int resolution = 100; /**< résolution du curseur */
  protected double value = 0; /**< valeur du curseur */
  protected double min = 0; /**< valeur minimum du curseur */
  protected double max = this.resolution; /**< valeur maximum du curseur */
  protected boolean manual_adjust = false; /**< ajustement manuel du curseur */
//------------------------------------------------------------------------------
  public JDoubleSlider() {
    // ajuste les limites du curseur
    this.setMinimum(0);
    this.setMaximum(this.resolution);
    // ajoute un listener pour detecter un ajustement manuel du curseur
    this.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        JDoubleSlider.this.manual_adjust = JDoubleSlider.this.getValueIsAdjusting();
    }});
  }
//------------------------------------------------------------------------------
    /**
     * Initialise les valeurs (en double) minimum et maximum du curseur
     * @param min valeur minimum
     * @param max valeur maximum
     */
  public void setDoubleMinMax( double min, double max ) { this.min= min; this.max= max; }
//------------------------------------------------------------------------------
  /**
   * Retourne la valeur double du curseur
   * @return la valeur du curseur
   */
  public double getDoubleValue() {
    // si le curseur n'a pas été ajusté manuellement: renvoit this.value, sinon recalcule la valeur du curseur en fonction des limites et de la résolution
    return this.manual_adjust == false ? this.value : this.min + (((double)super.getValue() / (double)this.resolution) * (this.max - this.min));
  }
//------------------------------------------------------------------------------
    /**
     * Fixe la valeur double du curseur
     * @param value nouvelle valeur du curseur
     */
  public void setDoubleValue( double value ) {
    this.value = value;
    super.setValue((int)(((value - this.min)/(this.max - this.min))*this.resolution));
    super.fireStateChanged(); // force une changement d'etat pour réaffichage du curseur
  }
}
//------------------------------------------------------------------------------
  public void SetSortedColumn( int column ) { this.sort_col= column; }
  public int GetSortedColumn() { return this.sort_col; }
  public void SetSortAscent( boolean is_ascent ) { this.is_ascent= is_ascent; }
  public Vector GetTablesHeaders() { return this.tables_headers; }
};