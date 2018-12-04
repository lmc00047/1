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
//                              JPrintPreviewTable.java
//
//
// Author(s) : Jean-Luc LABLEE
// FISPRO Version : 2.1
// Contact : fispro@ensam.inra.fr
// Last modification date:  September 15, 2004
// File :

//**********************************************************************
package print;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.print.Printable;

import javax.swing.JCheckBox;
import javax.swing.JTable;

import kbct.LocaleKBCT;
import kbctFrames.JKBCTFrame;

/**
 * print.JPrintPreviewTable.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

public class JPrintPreviewTable extends JPrintPreview {
  static final long serialVersionUID=0;	
  protected JTable table;

  public JPrintPreviewTable(JKBCTFrame parent, JTable table) { 
	  super(parent); 
	  this.table = table;
	  //System.out.println("JPrintPreviewTable: Print1");
  }

  public JPrintPreviewTable(Component parent, JTable table) { 
	  super(parent); 
	  this.table = table; 
	  //System.out.println("JPrintPreviewTable: Print2");
  }

  public void Show() { this.Init( new JPrintableTable(table) ); }

  protected void ToolbarConstruct() {
    super.ToolbarConstruct();
    final JCheckBox fitToPage = new JCheckBox(LocaleKBCT.GetString("FitToPage"));
    fitToPage.addItemListener(new java.awt.event.ItemListener() { public void itemStateChanged(ItemEvent e) {
      JPrintPreviewTable.this.fTarget = fitToPage.isSelected() == true ? (Printable)new JFitToPagePrintableTable(table) : new JPrintableTable(table);
      JPrintPreviewTable.this.ShowPreview();
      JPrintPreviewTable.this.pack();
    } });
    this.toolbar.add( fitToPage, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
  }
}