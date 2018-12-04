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
//                              JFitToPagePrintableTable.java
//
//
// Author(s) : Jean-Luc LABLEE
// FISPRO Version : 2.1
// Contact : fispro@ensam.inra.fr
// Last modification date:  September 15, 2004
// File :

//**********************************************************************
package print;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

/**
 * print.JFisToPagePrintableTable.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

public class JFitToPagePrintableTable implements Printable{
  JTable tableView;

  public JFitToPagePrintableTable(JTable tableView) {
    this.tableView = tableView;
    this.tableView.setSize(this.tableView.getPreferredSize());
  }

  public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
     	Graphics2D  g2 = (Graphics2D) g;
     	g2.setColor(Color.black);
     	int fontHeight=g2.getFontMetrics().getHeight();
     	int fontDesent=g2.getFontMetrics().getDescent();
     	//leave room for page number and title
     	double pageHeight = pageFormat.getImageableHeight()-(fontHeight);
     	double pageWidth = pageFormat.getImageableWidth();
     	double tableWidth = (double) this.tableView.getColumnModel().getTotalColumnWidth();
     	double scale = 1;
     	if( tableWidth >= pageWidth )
          scale =  pageWidth / tableWidth;

        JTableHeader header = this.tableView.getTableHeader();
        header.setSize(header.getPreferredSize());
     	double headerHeightOnPage = header.getHeight()*scale;
     	double tableWidthOnPage = tableWidth*scale;
     	double oneRowHeight = (this.tableView.getRowHeight() /*+ this.tableView.getRowMargin()*/)*scale;
     	int numRowsOnAPage = (int)((pageHeight-headerHeightOnPage)/oneRowHeight);
     	double pageHeightForTable = oneRowHeight*numRowsOnAPage;
     	int totalNumPages= (int)Math.ceil(((double)this.tableView.getRowCount())/numRowsOnAPage);
     	if( pageIndex >= totalNumPages ) return NO_SUCH_PAGE;

     	g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
     	g2.drawString("Page: "+(pageIndex+1),(int)pageWidth/2-35,
                      (int)(pageHeight+fontHeight-fontDesent));//bottom center
     	g2.translate(0f,Math.rint(headerHeightOnPage/*+fontHeight*/));
     	g2.translate(0f,Math.rint(-pageIndex*pageHeightForTable));
     	//If this piece of the table is smaller than the size available,
     	//clip to the appropriate bounds.
        int y1 = 0, x2 = 0, y2 = 0;
     	if( pageIndex + 1 == totalNumPages ) {
          int lastRowPrinted = numRowsOnAPage * pageIndex;
          int numRowsLeft = this.tableView.getRowCount() - lastRowPrinted;
          y1 = (int)Math.rint(pageHeightForTable * pageIndex);
          x2 = (int)Math.ceil(tableWidthOnPage);
          y2 = (int)Math.rint(oneRowHeight * numRowsLeft);
        }
     	//else clip to the entire area available.
     	else {
          y1 = (int)Math.rint(pageHeightForTable * pageIndex);
          x2 = (int)Math.ceil(tableWidthOnPage);
          y2 = (int)Math.rint(pageHeightForTable);
        }
        g2.setClip(0, y1, x2, y2);
        g2.scale(scale,scale);
        this.tableView.paint(g2);
     	g2.scale(1/scale,1/scale);
        g2.setColor(Color.gray);
        g2.drawLine(0, (int)Math.rint(pageIndex*pageHeightForTable), 0, (int)Math.rint((pageIndex + 1)*pageHeightForTable));
     	g2.translate(0f,Math.rint(pageIndex*pageHeightForTable));
     	g2.translate(0f,Math.rint(-headerHeightOnPage));
     	g2.setClip(0, 0,(int) Math.ceil(tableWidthOnPage),
                               (int)Math.rint(headerHeightOnPage));
     	g2.scale(scale,scale);
     	header.paint(g2);//paint header at top
     	return Printable.PAGE_EXISTS;
   }
}
