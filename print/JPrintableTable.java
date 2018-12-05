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
//                              JPrintableTable.java
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
import javax.swing.table.TableColumnModel;

/**
 * print.JPrintableTable.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

public class JPrintableTable implements Printable {
    JTable ppTable;
    JTableHeader tableHeader;
    int [] subTableSplit = null;
    int totalNumPages=0;
    int prevPageIndex = 0;
    int subPageIndex = 0;
    int subTableSplitSize = 0;
    double tableHeightOnFullPage, headerHeight;
    double pageWidth, pageHeight;
    int fontHeight, fontDesent;
    double tableHeight, rowHeight;
    PageFormat pageFormat;

    public JPrintableTable( JTable ppTable ) {
	this.ppTable = ppTable;
        this.ppTable.setSize(this.ppTable.getPreferredSize());
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
	Graphics2D g2=(Graphics2D)g;
        if( (this.pageFormat == null) || (this.pageFormat.equals(pageFormat) == false)) {
    	  getPageInfo(g, pageFormat);
          this.pageFormat = pageFormat;
	}
	g2.setColor(Color.black);
	if(pageIndex>=totalNumPages)
		return NO_SUCH_PAGE;

	if (prevPageIndex != pageIndex) {
		subPageIndex++;
		if( subPageIndex == subTableSplitSize -1)
			subPageIndex=0;
	}
	g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
	int rowIndex = pageIndex/ (subTableSplitSize -1);
	printTablePart(g2, rowIndex, subPageIndex);
	prevPageIndex= pageIndex;
	return Printable.PAGE_EXISTS;
    }

    public void getPageInfo(Graphics g, PageFormat pageFormat) {
        subTableSplit = null;
        subTableSplitSize = 0;
        subPageIndex = 0;
        prevPageIndex = 0;
        fontHeight=g.getFontMetrics().getHeight();
        fontDesent=g.getFontMetrics().getDescent();
        tableHeader = ppTable.getTableHeader();
        tableHeader.setSize(tableHeader.getPreferredSize());
        headerHeight = tableHeader.getHeight();
        pageHeight = pageFormat.getImageableHeight();
        pageWidth =  pageFormat.getImageableWidth();
        tableHeight = ppTable.getHeight();
        rowHeight = ppTable.getRowHeight();
        tableHeightOnFullPage = (int)(pageHeight - headerHeight - fontHeight*2);
        tableHeightOnFullPage = Math.floor(tableHeightOnFullPage/rowHeight) * rowHeight;
        TableColumnModel tableColumnModel = tableHeader.getColumnModel();
        int columns = tableColumnModel.getColumnCount();
        int [] temp = new int[columns];
        int columnIndex = 0;
        temp[0] = 0;
        int columnWidth;
        int length = 0;
        subTableSplitSize = 0;
        while ( columnIndex < columns ) {
           columnWidth = tableColumnModel.getColumn(columnIndex).getWidth();
           if ( length + columnWidth > pageWidth ) {
              temp[subTableSplitSize+1] = temp[subTableSplitSize] + length;
              length = columnWidth;
              subTableSplitSize++;
           } else
               length += columnWidth;

           columnIndex++;
        }
        if ( length > 0 )  {  // if are more columns left, part page
           temp[subTableSplitSize+1] = temp[subTableSplitSize] + length;
           subTableSplitSize++;
        }
        subTableSplitSize++;
        subTableSplit = new int[subTableSplitSize];
        for ( int i=0; i < subTableSplitSize; i++ )
           subTableSplit[i]= temp[i];

        totalNumPages = (int)(tableHeight/tableHeightOnFullPage);
        if ( tableHeight%tableHeightOnFullPage >= rowHeight ) {
            // at least 1 more row left
            totalNumPages++;
        }
        totalNumPages *= (subTableSplitSize-1);
    }

    public void printTablePart(Graphics2D g2, int rowIndex, int columnIndex) {
        String pageNumber = "Page: "+(rowIndex+1);
        if ( subTableSplitSize > 1 )
                pageNumber += "-" + (columnIndex+1);

        int pageLeft = subTableSplit[columnIndex];
        int pageRight = subTableSplit[columnIndex + 1];
        int pageWidth =  pageRight-pageLeft;
	// page number message
        g2.drawString(pageNumber, pageWidth/2-35, (int)(pageHeight - fontHeight));
        double clipHeight = Math.min(tableHeightOnFullPage, tableHeight - rowIndex*tableHeightOnFullPage);
        g2.translate(-subTableSplit[columnIndex], 0);
        g2.setClip(pageLeft ,0, pageWidth, (int)headerHeight);
        tableHeader.paint(g2);   // draw the header on every page
        g2.translate(0, headerHeight);
        g2.translate(0,  -tableHeightOnFullPage*rowIndex);
        // cut table image and draw on the page
        g2.setClip(pageLeft, (int)tableHeightOnFullPage*rowIndex, pageWidth, (int)clipHeight);
        ppTable.paint(g2);
        double pageTop =  tableHeightOnFullPage*rowIndex - headerHeight;
        //double pageBottom = pageTop +  clipHeight + headerHeight;
        g2.setColor(Color.gray);
        g2.drawLine(pageLeft, (int)pageTop, pageLeft, (int)(pageTop + clipHeight + headerHeight));
    }
}
