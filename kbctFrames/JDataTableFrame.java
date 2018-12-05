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
//                              JDataTableFrame.java
//
//
// Author(s) : Mathieu GRELIER
// FISPRO Version : 2.1
// Contact : fispro@ensam.inra.fr
// Last modification date:  September 15, 2004
// File :

//**********************************************************************

// Contains: JDataTableFrame, HeaderListenerBak, InputsColumnModel,
// OutputsColumnModel, ExamplesColumnModel, JDataTablePart

/**
 * kbctFrames.JDataTableFrame.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

package kbctFrames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import kbct.JKBCT;
import kbct.JKBCTInput;
import kbct.JKBCTOutput;
import kbct.KBCTListener;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbctAux.MessageKBCT;
import kbctAux.SortableHeaderListener;
import util.BoundedRangeModelChangeListener;
import util.sortabletable.SortButtonRenderer;
import util.sortabletable.SortableTableModel;
import fis.JExtendedDataFile;
import fis.JExtendedFileListener;
import fis.JFileListener;

public class JDataTableFrame extends JChildFrame implements SortableHeaderListener.SortableObject {
  static final long serialVersionUID=0;	
  private JKBCT kbct;
  private int InputCount, OutputCount;
  private JExtendedDataFile extend_data_file; /**< Fichier de données étendu*/
  private SortableTableModel DataFrameModel; /**< Modèle de données règles triable */
  private JTable ExamplesTable; //this table is a field, for value settings convenience
  private JTable InputsTable;
  private JTable OutputsTable;
  private SortButtonRenderer renderer; //triangular button indicating sorting order
  private ListSelectionModel rowSM;
  private JScrollPane InputsScrollPane;
  private JSplitPane jspData = null; /**< Objet conteneur des tables entrées et sorties */
  private int hlenght, vlenght; // totlenght; //dimensions in the JDataTableFrame used many times
  double[][] TmpDataFile;
  public Object[] VectorName; //the names in the columns headers
  private Vector<JTableHeader> tables_headers; /**< Vector containing the JTableHeader objects of the (maximum) three tables used in the JDataTableFrame : examples/active, inputs and outputs */
  private JPopupMenu jPopupRowsSelected; //right button click popup menu
  private JMenuItem RowsSelection;
  private JMenuItem RowsNegativeSelection;
  private JMenuItem RowsSwitch;
  private JMenuItem ActiveAll;
  private JMenuItem FingramsGen;
  private KBCTListener kbct_listener = null; /**< Listener du FIS JInferenceFrame#fis */
  private JFileListener file_listener = null;
  private int sort_col = -1; /**< Mémorisation du numéro de colonne triées. -1 pour aucune colonne triée */
  private boolean is_ascent; /**< Mémorisation ordre de tri */
  private JMenuBar jMenuBarDataTable= new JMenuBar();
  private JMenuItem jMenuClose= new JMenuItem();
  private boolean ShowInactive = false;
//------------------------------------------------------------------------------
  public JDataTableFrame(JKBCTFrame parent, JKBCT kbct, JExtendedDataFile data_file) {
    super(parent);
    this.extend_data_file = data_file;
    this.kbct = kbct;
    try {
      jDataTableInit();
      this.pack();
      this.setLocation(this.ChildPosition(this.getSize()));
      this.setVisible(true);
    } catch (Throwable t) { 
    	MessageKBCT.Error(null, t);
    	//t.printStackTrace();
    }
  }
//------------------------------------------------------------------------------
  public void dispose() {
    super.dispose();
    JExtendedDataFile.jhtTableFrames.remove(this.extend_data_file.FileName());   //this data table is not dispalyed now, so its key is removed from the jhtTableFrames hash table
    this.extend_data_file.RemoveJFileListener(this.file_listener);
    if (this.kbct != null)
      this.kbct.RemoveKBCTListener(this.kbct_listener);
  }
//------------------------------------------------------------------------------
  private void jDataTableInit() throws Throwable {
    this.jMenuBarDataTable.add(this.jMenuClose);
    this.setJMenuBar(this.jMenuBarDataTable);
    jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JDataTableFrame.this.dispose(); } });
    this.DataFrameModel = new SortableTableModel() { // necessary to display boolean values as checkboxes :
   	    static final long serialVersionUID=0;	
    	public Class getColumnClass(int col) {
	return getValueAt(0, col).getClass();
      }
      public boolean isCellEditable(int row, int col) {
	if (col == 1)
          // only the second column can be edited (the checkboxes)
	  return true;
	else
	  return false;
      }
    };
    if (kbct != null) {
      if (this.kbct_listener != null)
	      this.kbct.RemoveKBCTListener(this.kbct_listener);

    this.kbct_listener = new KBCTListener() {
	  public void KBCTClosed() { JDataTableFrame.this.dispose(); }
	  public void InputReplaced(int input_number) { JDataTableFrame.this.ReInit(); }
	  public void OutputReplaced(int output_number) { JDataTableFrame.this.ReInit(); }
	  public void InputActiveChanged(int input_number) { JDataTableFrame.this.ReInit(); }
	  public void OutputActiveChanged(int output_number) { JDataTableFrame.this.ReInit(); }
	  public void InputNameChanged(int input_number) { JDataTableFrame.this.ReInit(); }
	  public void OutputNameChanged(int output_number) { JDataTableFrame.this.ReInit(); }
      public void InputPhysicalRangeChanged( int input_number ) {}
      public void InputInterestRangeChanged( int input_number ) {}
      public void OutputPhysicalRangeChanged( int output_number ) {}
      public void OutputInterestRangeChanged( int output_number ) {}
	  public void InputRemoved(int input_number) {}
	  public void OutputRemoved(int output_number) {}
	  public void InputAdded(int input_number) {}
	  public void OutputAdded(int output_number) {}
      public void MFRemovedInInput( int input_number, int mf_removed ) {}
      public void MFRemovedInOutput( int output_number, int mf_removed ) {}
	  public void MFReplacedInInput(int input_number) {}
	  public void MFReplacedInOutput(int output_number) {}
	  public void OutputDefuzChanged(int output_number) {}
	  public void OutputDisjChanged(int output_number) {}
	  public void OutputClassifChanged(int output_number) {}
	  public void MFAddedInInput(int input_number) {}
	  public void MFAddedInOutput(int output_number) {}
	  public void OutputDefaultChanged(int output_number) {}
	  public void OutputAlarmThresChanged(int output_number) {}
	  public void RulesModified() {}
	  public void ConjunctionChanged() {}
      };
      this.kbct.AddKBCTListener(this.kbct_listener);
    }
    //extended data file listener
    if (this.file_listener != null)
      this.extend_data_file.RemoveJFileListener(this.file_listener);

    this.file_listener = new JExtendedFileListener() {
      public void Closed() { JDataTableFrame.this.dispose(); }
      public void ReLoaded() {}
     //the activation status of one example was modified
      public void ActivationChanged(int item, boolean active) {
	int row = JDataTableFrame.this.ConvertExampleToIndex(item) ; //because rows can be sorted
	JDataTableFrame.this.DataFrameModel.setValueAt(new Boolean(active), row, 1); //use of the data model's setValueAt // the table's one is just used for clicks on it
	JDataTableFrame.this.ExamplesTable.repaint() ; JDataTableFrame.this.InputsTable.repaint() ;
	if (JDataTableFrame.this.kbct != null)
	   JDataTableFrame.this.OutputsTable.repaint() ; //to update the gray/black colors
      }
      //the activation status of an array of examples was modified
      public void ActivationChanged(int []items, boolean active) {
        int[] rows = JDataTableFrame.this.ConvertExamplesToIndices(items) ; //because rows can be sorted
        for (int i=0 ; i<rows.length ; i++) {
          int row = rows[i] ;
  	  JDataTableFrame.this.DataFrameModel.setValueAt(new Boolean(active), row, 1);  //use of the data model's setValueAt // the table's one is just used for clicks on it
        }
        JDataTableFrame.this.ExamplesTable.repaint() ; JDataTableFrame.this.InputsTable.repaint() ;
        if (JDataTableFrame.this.kbct != null)
	   JDataTableFrame.this.OutputsTable.repaint() ; //to update the gray/black colors
      }
    };
    this.extend_data_file.AddJFileListener(this.file_listener);
    // building of the table model
    // affecte les vecteurs de données et de titre au modèle de table
    TmpDataFile = this.extend_data_file.GetData();
    hlenght = TmpDataFile[0].length;
    VectorName = new Object[hlenght + 2];
    int i;
    VectorName[0] = new String(LocaleKBCT.GetString("items"));
    VectorName[1] = new String(LocaleKBCT.GetString("Active"));
    if (kbct == null) {
      for (i = 2; i < hlenght + 2; i++)
	VectorName[i] = new String(LocaleKBCT.GetString("Variable") + " " + (i - 1));
    }
    //sinon prend le nombre de variables actives. On doit récupérer leur
    //indices et leurs noms
    else {
      //Filling of the VectorName Vector
      InputCount = kbct.GetNbInputs();
      for (i = 0; i < InputCount; i++) { // au max on explore jusqu'à l'avant-dernier
	       JKBCTInput CurrentInput = kbct.GetInput(i+1);
	       VectorName[i + 2] = new String(CurrentInput.GetName());
      }
      OutputCount = Math.min(kbct.GetNbOutputs(), this.hlenght-this.InputCount); // cas sorties sans données
      for (int j = 0; j < OutputCount; j++) {
	       JKBCTOutput CurrentOutput = kbct.GetOutput(j+1);
	       VectorName[i + 2] = new String(CurrentOutput.GetName());
	       i++;
      }
    }
    //Table height is given by the lenght of the first dimension
    vlenght = TmpDataFile.length;
    //on doit rajouter deux nouvelles colonnes, exemples et actifs
    //Columns 'Example' and 'Active' must be added
    Object[][] FormatedDataFile = new Object[vlenght][hlenght + 2];
    for (i = 0; i < vlenght; i++) {
      FormatedDataFile[i][0] = new Integer(i + 1); //Example number
      //retreiving of the active examples
      FormatedDataFile[i][1] = new Boolean(this.extend_data_file.GetActive(i));
      for (int j = 0; j < hlenght; j++)
	       FormatedDataFile[i][j + 2] = new Double(TmpDataFile[i][j]);
    }
    //retreiving of the active examples
    //Building of the table model
    DataFrameModel.setDataVector(FormatedDataFile, VectorName);
    this.tables_headers = new Vector<JTableHeader>(); //vector for go through table headers
    renderer = new SortButtonRenderer() { // triangular button, this method is overiddeb with an anonymous class to have the headers colored in gray for inactive inputs
      static final long serialVersionUID=0;	
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	((JButton)comp).setEnabled(true);
	try {
	  if (JDataTableFrame.this.ShowInactive == true) {
	    int col_mod = table.convertColumnIndexToModel(column);
	    if (col_mod == 0) {
	      ((JButton)comp).setText(LocaleKBCT.GetString("items"));
             ((JButton)comp).setToolTipText(null);
             return comp;
	    }
	    if (col_mod == 1) {
	      ((JButton)comp).setText(LocaleKBCT.GetString("Active"));
              ((JButton)comp).setToolTipText(null);
              return comp;
	    }
	    if ( (col_mod > 1) && (col_mod < JDataTableFrame.this.InputCount + 2)) {
	      if (JDataTableFrame.this.kbct.GetInput(col_mod - 2).GetActive() == false)
		((JButton)comp).setEnabled(false);
	    }
	    if (col_mod >= JDataTableFrame.this.InputCount + 2) {
	      if (JDataTableFrame.this.kbct.GetOutput(col_mod - JDataTableFrame.this.InputCount - 2).GetActive() == false)
		((JButton)comp).setEnabled(false);
	    }
	  }
	} catch (Throwable t) {}
	return comp;
      }
    };
    //Inputs JScrollPane Creation
    rowSM = new DefaultListSelectionModel(); // one selection model for all the tables
    ExamplesTable = new JDataTablePart(this.DataFrameModel, new ExamplesColumnModel(), rowSM); // the "example/active" table
    /* Now, again, we have to consider 2 cases : a fis is open or not.
	 Case no : we will just create one table for all the inputs. Its column model
     is defined by the NoFisInputsColumnModel. We will put this table
     with the "examples table" just created in a JScrollPane.
     Case yes : we will create two others tables. One for the inputs (model defined by
     the InputsColumnModel class) and another for the outputs (see OutputsColumnModel)
     We will put the "examples table" with the "inputs table" in a JScrollPane. We will
	 also put the "outputs table" in a JScrollPane (actually, we put those tables in
	 JPanels before to use layout managers). Those two JScrollPanes are finally put
     in a JSplitPane. */
    //case no : two tables only, in a JScrollPane
    InputsTable = new JDataTablePart(this.DataFrameModel, new InputsColumnModel(), rowSM);
    JPanel InputsPanel = new JPanel(new BorderLayout());
    InputsPanel.add(InputsTable, BorderLayout.CENTER);
    if (this.InputsScrollPane != null)
      this.getContentPane().remove(this.InputsScrollPane);

    InputsScrollPane = new JScrollPane(InputsPanel);
    InputsScrollPane.setColumnHeaderView(InputsTable.getTableHeader());
    InputsScrollPane.setRowHeaderView(this.ExamplesTable);
    JPanel ExampleHeaderPanel = new JPanel(new BorderLayout());
    ExampleHeaderPanel.add(ExamplesTable.getTableHeader(), BorderLayout.NORTH);
    InputsScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, ExampleHeaderPanel);
    // tri des colonnes de la table
    if (this.sort_col != -1) {
      this.renderer.setPressedColumn(this.sort_col);
      this.renderer.setSelectedColumn(this.sort_col);
      if (this.is_ascent == false)
        // fait un appel supplémentaire à SortButtonRenderer.setSelectedColumn pour mettre la variable SortButtonRenderer.state dans le bon état
	this.renderer.setSelectedColumn(this.sort_col);

      this.DataFrameModel.sortByColumn(this.sort_col, this.is_ascent);
      this.renderer.setPressedColumn( -1);
    }
    if (kbct == null) {
      SetUpColors();
      this.setTitle(this.extend_data_file.GetFileName());
      this.getContentPane().add(InputsScrollPane);
    } else {
      //Outputs JScrollPane Creation
      OutputsTable = new JDataTablePart(this.DataFrameModel, new OutputsColumnModel(), rowSM);
      //here, the three tables have been created, we have to set up the color of their columns and their rows
      SetUpColors();
      JPanel OutputsPanel = new JPanel(new BorderLayout());
      OutputsPanel.add(OutputsTable, BorderLayout.CENTER);
      JScrollPane OutputsTableScrollPane = new JScrollPane(OutputsPanel);
      OutputsTableScrollPane.setColumnHeaderView(OutputsTable.getTableHeader());
      //allows the vertical scrollbar of the outputs table to be controlled by the vertical scrollbar of the inputs table
      JScrollBar inputs_Bar = InputsScrollPane.getVerticalScrollBar();
      JScrollBar outputs_Bar = OutputsTableScrollPane.getVerticalScrollBar();
      OutputsTableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER); // cache la scrollbar verticale //hides the vertical scrollbar
      inputs_Bar.getModel().addChangeListener(new BoundedRangeModelChangeListener(outputs_Bar.getModel()));
      //Affichage du JSplitPane //JSplitPane display
      if (this.jspData != null)
	      this.getContentPane().remove(this.jspData);

      this.jspData = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, InputsScrollPane, OutputsTableScrollPane) {
    	    static final long serialVersionUID=0;	
    	  
	//protected boolean continuousLayout = true;
	public int getDividerSize() {
	  return 2;
	}
      };
      this.setTitle(this.extend_data_file.GetFileName());
      this.jspData.setResizeWeight((1.0 / (this.InputsTable.getColumnCount() + this.OutputsTable.getColumnCount())) * this.InputsTable.getColumnCount());
      this.getContentPane().add(jspData);
    }
    //right click button popup menu building
    CreateMenuItems();
    this.jPopupRowsSelected = new JPopupMenu();
    this.jPopupRowsSelected.add(RowsSelection);
    this.jPopupRowsSelected.add(RowsNegativeSelection);
    this.jPopupRowsSelected.add(RowsSwitch);
    this.jPopupRowsSelected.add(ActiveAll);
    this.jPopupRowsSelected.addSeparator();
    this.jPopupRowsSelected.add(FingramsGen);
    this.RowsSelection.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e) { JDataTablePopup_selection_mousePressed(e);} });
    this.RowsNegativeSelection.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e) { JDataTablePopup_negativeselection_mousePressed(e); } });
    this.RowsSwitch.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e) { JDataTablePopup_switch_mousePressed(e); } });
    this.ActiveAll.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e) { JDataTablePopup_activeall_mousePressed(e); } });
    this.FingramsGen.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e) { JDataTablePopup_fingramsGen_mousePressed(e); } });
    this.Translate();
  }
//------------------------------------------------------------------------------
  private void SetUpColors() throws Throwable {
    for (int i = 0; i < (ExamplesTable.getColumnModel().getColumnCount()) - 1; i++) {
      // only the example number column must be colored in light gray
      ExamplesTable.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
    static final long serialVersionUID=0;	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	  try {
	    // grise les exemples inactifs // Inactive examples are colored in light gray
	    this.setForeground(Color.black);
	    if ( ( (Boolean) table.getModel().getValueAt(row, 1)).booleanValue() == false)
	      super.setForeground(Color.lightGray);
	  } catch (Throwable t) {}
	  return this;
	}
      });
    }
    for (int i = 0; i < InputsTable.getColumnModel().getColumnCount(); i++) {
      InputsTable.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
    static final long serialVersionUID=0;	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	  // grise les colonnes correspondant à des entrées ou sorties inactives //Columns corresponding to inactive inputs are colored in lighyt gray
	  this.setForeground(Color.black);
	  try {
	    if (JDataTableFrame.this.ShowInactive == true) {
	      if (JDataTableFrame.this.kbct.GetInput(column).GetActive() == false)
		this.setForeground(Color.lightGray);
	    }
	    // grise les exemples inactifs // Inactive examples are colored in light gray
	    if (((Boolean)table.getModel().getValueAt(row, 1)).booleanValue() == false)
	      super.setForeground(Color.lightGray);
	  } catch (Throwable t) {}
	  return this;
	}
      });
    }
    if (this.kbct != null) {
      for (int i = 0; i < OutputsTable.getColumnModel().getColumnCount(); i++) {
        OutputsTable.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
      static final long serialVersionUID=0;	
	  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
  	    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
  	    // grise les colonnes correspondant à des entrées ou sorties inactives //Columns corresponding to inactive inputs are colored in lighyt gray
 	    this.setForeground(Color.black);
	    try {
	      if (JDataTableFrame.this.ShowInactive == true) {
	        if (JDataTableFrame.this.kbct.GetOutput(column).GetActive() == false)
		  this.setForeground(Color.lightGray);
 	      }
	      // grise les exemples inactifs // Inactive examples are colored in light gray
	      if (((Boolean) table.getModel().getValueAt(row, 1)).booleanValue() == false)
	        super.setForeground(Color.lightGray);
	    } catch (Throwable t) {}
	    return this;
	  }
      });
    }
   }
  }

//------------------------------------------------------------------------------
  class HeaderListenerBak extends MouseAdapter {
    JTableHeader header;
    SortButtonRenderer renderer;
    //------------------------------------------------------------------------------
    HeaderListenerBak(JTableHeader header, SortButtonRenderer renderer) {
      this.header = header;
      this.renderer = renderer;
    }
    //------------------------------------------------------------------------------
    public void mousePressed(MouseEvent e) {
      int col = header.columnAtPoint(e.getPoint());
      int sortCol = header.getTable().convertColumnIndexToModel(col);
      renderer.setPressedColumn(sortCol);
      renderer.setSelectedColumn(sortCol);
      for (int i = 0; i < JDataTableFrame.this.tables_headers.size(); i++)
	((JTableHeader)JDataTableFrame.this.tables_headers.elementAt(i)).repaint();

      if (header.getTable().isEditing())
	header.getTable().getCellEditor().stopCellEditing();

      boolean isAscent;
      if (SortButtonRenderer.DOWN == renderer.getState(col))
	      isAscent = true;
      else
	      isAscent = false;

      ((SortableTableModel)header.getTable().getModel()).sortByColumn(sortCol, isAscent);
    }
    //------------------------------------------------------------------------------
    public void mouseReleased() {
      renderer.setPressedColumn( -1); // clear
      for (int i = 0; i < JDataTableFrame.this.tables_headers.size(); i++)
	((JTableHeader)JDataTableFrame.this.tables_headers.elementAt(i)).repaint();
    }
  }

//------------------------------------------------------------------------------
  /**
   * Column model for the inputs table
   */
  class InputsColumnModel extends DefaultTableColumnModel {
    static final long serialVersionUID=0;	
	  
    /**
     *  Overriding of DefaultTableColumnModel.addColumn to display the inputs columns only
     * */
    public void addColumn(TableColumn tc) {
      int index = tc.getModelIndex();
      if (kbct == null) {
	if (index > 1)
	  super.addColumn(tc);
      } else {
	if (ShowInactive == true) {
	  if ( (index > 1) && (index < JDataTableFrame.this.InputCount + 2))
	    super.addColumn(tc);
	} else {
	  if ( (index > 1) && (index < JDataTableFrame.this.InputCount + 2)) {
	    try {
	      JKBCTInput CurrentInput = kbct.GetInput(index - 2);
	      if (CurrentInput.GetActive() == true)
		super.addColumn(tc);
	    } catch (Throwable t) { MessageKBCT.Error(null, t); }
	  }
	}
      }
    }
  }

  //------------------------------------------------------------------------------
  /**
   * Column model for the outputs table
   */
  class OutputsColumnModel extends DefaultTableColumnModel {
    static final long serialVersionUID=0;	
	  
    /**
     *  Overriding of DefaultTableColumnModel.addColumn to display the outputs columns only
     * */
    public void addColumn(TableColumn tc) {
      int index = tc.getModelIndex();
      if (ShowInactive == true) {
	if (index >= JDataTableFrame.this.InputCount + 2)
	  super.addColumn(tc);
      } else {
	if (index >= JDataTableFrame.this.InputCount + 2) {
	  try {
	    JKBCTOutput CurrentOutput = kbct.GetOutput(index - JDataTableFrame.this.InputCount - 2);
	    if (CurrentOutput.GetActive() == true)
	      super.addColumn(tc);
	  } catch (Throwable t) { MessageKBCT.Error(null, t); }
	}
      }
    }
  }

//------------------------------------------------------------------------------
   /**
    *  Column model for the examples table
    */
   class ExamplesColumnModel extends DefaultTableColumnModel {
     static final long serialVersionUID=0;	
	   
     /**
      *  Overriding of DefaultTableColumnModel.addColumn to display the examples + active columns only
      * */
     public void addColumn(TableColumn tc) {
       int index = tc.getModelIndex();
       if (index <= 1)
	 super.addColumn(tc);
     }
   }

//------------------------------------------------------------------------------
  /**
   * Table class used for the different tables displayed in the frame
   */
  private class JDataTablePart extends JTable {
    static final long serialVersionUID=0;	
    public JDataTablePart(SortableTableModel dm, TableColumnModel cm, ListSelectionModel rsm) {
      super(dm, cm);
      this.createDefaultColumnsFromModel(); // construit la table //building of the table
      this.setSelectionModel(rsm);
      this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      this.setColumnSelectionAllowed(false);
      this.SetSortableHeaders();
      this.addMouseListener(new MouseAdapter() {
	public void mousePressed(MouseEvent e) { JDataTablePart_mousePressed(e); }
	public void mouseReleased(MouseEvent e) { JDataTablePart_mouseReleased(); }
	public void mouseClicked (MouseEvent e) {
	  if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0 && e.getClickCount() == 2) {
	      Point p = e.getPoint();
	      int row = rowAtPoint(p);
	      int column = columnAtPoint(p); // This is the view column! but it corresponds to the model's one
	      if (column == 1 && getColumnModel() instanceof ExamplesColumnModel)
		    return ; //won't work on the checkboxes column

	      int clickedExample = ConvertIndiceToExample(row);
	      double[] row_data = JDataTableFrame.this.extend_data_file.GetData()[clickedExample-1] ;
	      try {
		((JMainFrame)parent).showInferenceFrame() ;
 	        //we set the inference frame's sliders values one by one. Setting the sliders double value is sufficient to have the correct inference
	        for (int i=0 ; i<(row_data.length - kbct.GetNbOutputs() /*- kbct.NbActiveOutputs()*/) ; i++)  //
		  ((JMainFrame)parent).setInferenceFrameInputValue(i, row_data[i]) ;
	      } catch (Throwable t) {t.printStackTrace();}
	    }
	}
      });
    }
    //------------------------------------------------------------------------------
    public void setValueAt(Object data, int row, int column) {
        //to manage the activation of one example by clicking on the table and only in this case
        int example_at_i = ( (Integer) JDataTableFrame.this.ExamplesTable.getModel().getValueAt(row, 0)).intValue();
	JDataTableFrame.this.extend_data_file.SetActive(example_at_i, !(( (Boolean)this.getValueAt(row, 1)).booleanValue())) ;
    }
    //------------------------------------------------------------------------------
    //wil be used by this.pack to set the cells dimensions
    public Dimension getPreferredScrollableViewportSize() {
      Dimension size = super.getPreferredScrollableViewportSize();
      return new Dimension(Math.min(getPreferredSize().width, size.width), size.height);
    }
    //------------------------------------------------------------------------------
    //for headers tooltips : overriding of createDefaultTableHeader method
    protected JTableHeader createDefaultTableHeader() {
      return new JTableHeader(columnModel) {
    	    static final long serialVersionUID=0;	
      public String getToolTipText(MouseEvent e) {
	  java.awt.Point p= e.getPoint();
	  int index= columnModel.getColumnIndexAtX(p.x);
	  int realIndex= columnModel.getColumn(index).getModelIndex();
	  return (String) JDataTableFrame.this.VectorName[realIndex];
	}
      };
    }
    //------------------------------------------------------------------------------
    // SortButtonRenderer renderer = new SortButtonRenderer();
    private void SetSortableHeaders() {
      TableColumnModel model = JDataTablePart.this.getColumnModel();
      for (int i = 0; i < model.getColumnCount(); i++)
	       model.getColumn(i).setHeaderRenderer(JDataTableFrame.this.renderer);

      this.getTableHeader().setReorderingAllowed(false);
      this.getTableHeader().setResizingAllowed(false);
      JTableHeader header = JDataTablePart.this.getTableHeader();
      header.addMouseListener(new SortableHeaderListener(header, JDataTableFrame.this.renderer, JDataTableFrame.this));
      JDataTableFrame.this.tables_headers.add(header); // ajout du header dans le vecteurs des headers
    }
    //------------------------------------------------------------------------------
    // Mouse events on the table
    private void JDataTablePart_mousePressed(MouseEvent e) {
      if (SwingUtilities.isRightMouseButton(e)) {
	JDataTableFrame.this.jPopupRowsSelected.setEnabled(true);
	if (rowSM.getMinSelectionIndex() == -1)
	  EnablePopupItems(false);

	SwingUtilities.updateComponentTreeUI(JDataTableFrame.this.jPopupRowsSelected); // affecte le look actuel au composant jPopupMenuInputs
	jPopupRowsSelected.show(this, e.getX(), e.getY()); // affichage du popup
      }
      if (SwingUtilities.isLeftMouseButton(e)) {
	if (rowSM.getMinSelectionIndex() != -1)
         //if one line is selected, the popup items can be enabled
	  EnablePopupItems(true);
      }
    }
    //------------------------------------------------------------------------------
    private void JDataTablePart_mouseReleased() {
      //disable switch when all rows are selected
      MenuElement[] me = jPopupRowsSelected.getSubElements();
      int[] tmp = GetSelectionArray() ;
      if (tmp.length == JDataTableFrame.this.vlenght || rowSM.getMinSelectionIndex() == -1)
            me[2].getComponent().setEnabled(false);
      else
	    me[2].getComponent().setEnabled(true);
    }
  }; //end of JDataTablePart nested private class
//------------------------------------------------------------------------------
  /*
   Some functions for Popup menu handling
   */
  private void EnablePopupItems(boolean b_enable) {
    MenuElement[] me = jPopupRowsSelected.getSubElements();
    for (int i = 0; i < me.length - 2; i++)
      //active all and active nothing stay enabled
      me[i].getComponent().setEnabled(b_enable);
    
    if (this.kbct.GetNbRules() > 0) {
    	//System.out.println("Activar/Desactivar Fingrams Genereration from data table");
    	//System.out.println("this.kbct.NbRules -> "+this.kbct.GetNbRules());
    	me[me.length-1].getComponent().setEnabled(b_enable);
    } else {
    	me[me.length-1].getComponent().setEnabled(false);
    }
  }
  private void JDataTablePopup_selection_mousePressed(MouseEvent e) {
    if (SwingUtilities.isLeftMouseButton(e))
      DoSelection(true);
  }
  private void JDataTablePopup_negativeselection_mousePressed(MouseEvent e) {
    if (SwingUtilities.isLeftMouseButton(e))
      DoSelection(false);
  }
  private void DoSelection(boolean bvalue) {
    int[] SelectedIndices = this.GetSelectionArray();
    int[] SelectedItems = ConvertIndicesToExamples(SelectedIndices);
    this.extend_data_file.SetActive(SelectedItems, bvalue) ;
  }
  private void JDataTablePopup_switch_mousePressed(MouseEvent e) {
    if (SwingUtilities.isLeftMouseButton(e))
      DoSwitch();
  }
  private boolean stat_bvalue = false;
  private void DoSwitch() {
    int[] SelectedIndices = this.GetSelectionArray();
    int[] SelectedItems = ConvertIndicesToExamples(SelectedIndices);
    this.extend_data_file.GlobalSetActive(stat_bvalue, true);
    for (int i = 0; i < this.vlenght ; i++)
          this.DataFrameModel.setValueAt(new Boolean(stat_bvalue), i, 1);

    this.extend_data_file.SetActive(SelectedItems, !stat_bvalue);
    stat_bvalue = !stat_bvalue; //to inverse the next switch
  }
//------------------------------------------------------------------------------
  private void JDataTablePopup_activeall_mousePressed(MouseEvent e) {
    if (SwingUtilities.isLeftMouseButton(e)) {
      DoGlobalSelection(true);
      this.rowSM.clearSelection();
      stat_bvalue = true; //to ensure that DoSwitch will work correctly : if we active all the examples and then we do a switch, the result must be : no examples activated. This is avoided directly in the menu
    }
  }
  private void DoGlobalSelection(boolean bvalue) {
        this.extend_data_file.GlobalSetActive(bvalue, false);
  }
//------------------------------------------------------------------------------
  private void JDataTablePopup_fingramsGen_mousePressed(MouseEvent e) {
    if (SwingUtilities.isLeftMouseButton(e)) {
        int[] SelectedIndices = this.GetSelectionArray();
        int[] SelectedItems = ConvertIndicesToExamples(SelectedIndices);
        //System.out.println("selLength -> " + SelectedItems.length);
        //System.out.println("selSample -> " + SelectedItems[0]);
        // instane-based fingram for the first selected sample
    	MainKBCT.getConfig().SetFingramsSelectedSample(SelectedItems[0]);
        this.parent.jef.jButtonFingrams_actionPerformed(false, null);
    }
  }
//------------------------------------------------------------------------------
  //this function doen't exist in the row selection model class. So we create it.
  protected int[] GetSelectionArray() {
    Vector<Integer> SelectionVector = new Vector<Integer>();
    int i;
    for (i = this.rowSM.getMinSelectionIndex(); i <= (this.rowSM.getMaxSelectionIndex()); i++) {
      if (this.rowSM.isSelectedIndex(i))
	      SelectionVector.add(new Integer(i));
    }
    int lenght = SelectionVector.size();
    int[] SelectionArray = new int[lenght];
    for (i = 0; i < lenght; i++) {
      Integer value = (Integer) SelectionVector.get(i);
      SelectionArray[i] = value.intValue();
    }
    return SelectionArray;
  }
//------------------------------------------------------------------------------
  //necessary function, because rows can be sorted
  private int[] ConvertIndicesToExamples (int[] SelectedIndices) {
   int[] SelectedItems= new int[SelectedIndices.length];
   int example_at_i;
   for (int i=0 ; i< SelectedItems.length ; i++) {
     example_at_i= ((Integer)JDataTableFrame.this.ExamplesTable.getModel().getValueAt(SelectedIndices[i], 0)).intValue();
     SelectedItems[i] = example_at_i;
   }
   return SelectedItems;
  }
//------------------------------------------------------------------------------
  //necessary function, because rows can be sorted
  private int ConvertIndiceToExample (int selectedIndice) {
   int example_at_i;
   example_at_i= ((Integer)JDataTableFrame.this.ExamplesTable.getModel().getValueAt(selectedIndice, 0)).intValue();
   return example_at_i ;
  }
//------------------------------------------------------------------------------
  //necessary function, because rows can be sorted
  private int ConvertExampleToIndex (int example) {
   int index;
   int example_at_index;
   for (index=0 ; index<this.vlenght ; index++) {
     example_at_index = ((Integer)JDataTableFrame.this.ExamplesTable.getModel().getValueAt(index, 0)).intValue();
     if (example_at_index == example)
         break ;
   }
   return index ;
  }
//------------------------------------------------------------------------------
  //necessary function, because rows can be sorted
   private int[] ConvertExamplesToIndices (int[] examples) {
    int[] indices = new int [examples.length];
    int example_at_index;
    for (int i=0; i <examples.length; i++) {
     int j;
     for (j=0; j<this.vlenght; j++) {
	 example_at_index = ((Integer)JDataTableFrame.this.ExamplesTable.getModel().getValueAt(j, 0)).intValue();
	 if (example_at_index == examples[i])
	     break;
     }
     indices[i] = j;
    }
    return indices;
  }
//------------------------------------------------------------------------------
  /**
   * ReInit of the frame. Called by the fis listener JDataTableFrame#fis_listener to reinit and display the frame after the FIS have been modified
   */
  private void ReInit() {
    try {
      int divider_location = 0;
      if (this.jspData != null)
	divider_location = this.jspData.getDividerLocation();

      this.jDataTableInit();
      if (this.jspData != null)
	this.jspData.setDividerLocation(divider_location);

      this.setVisible(true);
    } catch (Throwable t) { MessageKBCT.Error(null, t); }
  }
//------------------------------------------------------------------------------
  public void Translate() throws Throwable {
    this.jMenuClose.setText(LocaleKBCT.GetString("Close"));
  }
//------------------------------------------------------------------------------
  private void CreateMenuItems() {
    RowsSelection = new JMenuItem(new String(LocaleKBCT.GetString("Activate")));
    RowsNegativeSelection = new JMenuItem(new String(LocaleKBCT.GetString("Deactivate")));
    RowsSwitch = new JMenuItem(new String(LocaleKBCT.GetString("Switch")));
    ActiveAll = new JMenuItem(new String(LocaleKBCT.GetString("ActivateAll")));
    FingramsGen = new JMenuItem(new String(LocaleKBCT.GetString("Fingrams")));
  }
//------------------------------------------------------------------------------
  public void SetSortedColumn(int column) { this.sort_col = column; }
  public int GetSortedColumn() { return this.sort_col; }
  public void SetSortAscent(boolean is_ascent) { this.is_ascent = is_ascent; }
  public Vector GetTablesHeaders() { return this.tables_headers; }
  public Dimension getMinimumSize() { return  new Dimension(300,200); }
//------------------------------------------------------------------------------
  public void setSize(Dimension d) {
    super.setSize(d);
    if( (this.jspData != null) && (this.jspData.getDividerLocation() > d.width) )
      this.jspData.setDividerLocation(d.width-100);
  }
}