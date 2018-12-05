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
//                              JPrintPreview.java
//
//
// Author(s) : Jean-Luc LABLEE
// FISPRO Version : 2.1
// Contact : fispro@ensam.inra.fr
// Last modification date:  September 15, 2004
// File :

//**********************************************************************
package print;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

import kbct.LocaleKBCT;
import kbctAux.MessageKBCT;
import kbctFrames.JChildFrame;
import kbctFrames.JKBCTFrame;
import fis.JFISDialog;

/**
 * print.JPrintPreview.
 * A fairly generic class that provides a print preview capability
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
public class JPrintPreview extends JFISDialog {
	static final long serialVersionUID=0;	
    protected int               fPageWidth;
    protected int               fPageHeight;
    protected Printable         fTarget;
    protected JComboBox         fComboBoxScale;
    protected PreviewContainer  fPanelPreview;
    protected ActionListener    fPrintListener;
    protected int scale = 50;
    protected PrinterJob prnJob;
    protected PageFormat pageFormat;
    protected JScrollPane ps = null;
    protected Component parent;
    protected JPanel toolbar;

    public JPrintPreview( JKBCTFrame parent, Printable target) {
        super(parent);
        this.parent = parent;
        this.Init( target );
  	    //System.out.println("JPrintPreview: PrintA");
    }

    public JPrintPreview( JChildFrame parent, Printable target) {
        super(parent);
        this.parent = parent;
        this.Init( target );
  	    //System.out.println("JPrintPreview: PrintB");
    }

    public JPrintPreview( Component parent, Printable target) {
        super();
        this.parent = parent;
        this.Init( target );
  	    //System.out.println("JPrintPreview: PrintC");
    }

    protected JPrintPreview( JKBCTFrame parent ) {
        super(parent);
        this.parent = parent;
  	    //System.out.println("JPrintPreview: PrintD");
    }

    protected JPrintPreview( Component parent ) {
        super();
        this.parent = parent;
  	    //System.out.println("JPrintPreview: PrintE");
    }

    protected void Init( Printable target ) {
        fTarget = target;
        this.ToolbarConstruct();
        getContentPane().add( toolbar, BorderLayout.NORTH);
        this.prnJob = PrinterJob.getPrinterJob();
        this.pageFormat = prnJob.defaultPage();
        if (JPrintPreview.this.prnJob.getPrintService()!=null)
            this.setTitle(LocaleKBCT.GetString("PrintPreview") + " : " + JPrintPreview.this.prnJob.getPrintService().getName());
        else
            this.setTitle(LocaleKBCT.GetString("PrintPreview") + " : ");
        
        this.ShowPreview();
        this.pack();
        this.setModal(true);
        this.setLocation(JChildFrame.ChildPosition(parent, this.getSize()));
        this.setVisible(true);
    }

    protected void ToolbarConstruct() {
        toolbar = new JPanel(new GridBagLayout());
        JButton buttonPrint = new JButton( LocaleKBCT.GetString("Print"));
        fPrintListener = new ActionListener() {
            public void actionPerformed( ActionEvent e) {
                try {
                    JPrintPreview.this.prnJob.setPrintService(JPrintPreview.this.prnJob.getPrintService());
                    JPrintPreview.this.prnJob.setPrintable( fTarget, JPrintPreview.this.pageFormat );
                    setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    JPrintPreview.this.prnJob.print();
                    setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    dispose();
                } catch ( PrinterException ex) { MessageKBCT.Error(null, ex ); }
            }
        };
        buttonPrint.addActionListener( fPrintListener);
        toolbar.add( buttonPrint, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        JButton buttonSetup = new JButton( LocaleKBCT.GetString("PrintSetup"));
        ActionListener lst = new ActionListener() {
            public void actionPerformed( ActionEvent e) {
               Thread runner = new Thread() {
               public void run() {
                 JPrintPreview.this.pageFormat = JPrintPreview.this.prnJob.pageDialog(JPrintPreview.this.pageFormat);
                 setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                 if (JPrintPreview.this.prnJob.getPrintService()!=null)
                     JPrintPreview.this.setTitle(LocaleKBCT.GetString("PrintPreview") + " : " + JPrintPreview.this.prnJob.getPrintService().getName());
                 else
                     JPrintPreview.this.setTitle(LocaleKBCT.GetString("PrintPreview") + " : ");
                	 
                 JPrintPreview.this.ShowPreview();
                 JPrintPreview.this.pack();
                 setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                 } };
            runner.start();
            }
        };
        buttonSetup.addActionListener( lst);
        toolbar.add( buttonSetup, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
        String[] scales = { "10 %", "25 %", "50 %", "100 %" };
        fComboBoxScale = new JComboBox( scales);
        fComboBoxScale.setSelectedItem( "50 %");
        lst = new ActionListener() {
            public void actionPerformed( ActionEvent e) {
                Thread runner = new Thread() {
                    public void run() {
                        setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        String str = fComboBoxScale.getSelectedItem().toString();
                        if ( str.endsWith( "%")) str = str.substring( 0, str.length()-1);
                        str = str.trim();
                        int sc = 0;
                        try { sc = Integer.parseInt( str); }
                        catch ( NumberFormatException ex) { return; }
                        int w = ( int)( fPageWidth*sc/100);
                        int h = ( int)( fPageHeight*sc/100);
                        Component[] comps = fPanelPreview.getComponents();
                        for ( int k=0; k<comps.length; k++) {
                            if ( !( comps[k] instanceof PagePreview)) continue;
                            PagePreview pp = ( PagePreview)comps[k];
                            pp.setScaledSize( w, h);
                        }
                        fPanelPreview.doLayout();
                        fPanelPreview.getParent().getParent().validate();
                        setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                };
                runner.start();
            }
        };
        fComboBoxScale.addActionListener( lst);
        fComboBoxScale.setEditable(true);
        toolbar.add( fComboBoxScale, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    }

    protected void ShowPreview() {
        if( this.ps != null )
          this.getContentPane().remove(this.ps);

        fPanelPreview = new PreviewContainer();
        if ( pageFormat.getHeight()==0 || pageFormat.getWidth()==0) {
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Unable to determine default page size");
            return;
        }
        setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        fPageWidth = ( int)( pageFormat.getWidth());
        fPageHeight = ( int)( pageFormat.getHeight());
        int pageIndex = 0;
        int w = ( int)( fPageWidth*scale/100);
        int h = ( int)( fPageHeight*scale/100);
        try {
            while ( true) {
                BufferedImage img = new BufferedImage( fPageWidth, fPageHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = img.createGraphics();
                g.setColor( Color.white);
                g.fillRect( 0, 0, fPageWidth, fPageHeight);
                if ( fTarget.print( g, pageFormat, pageIndex) != Printable.PAGE_EXISTS) break;
                PagePreview pp = new PagePreview( w, h, img);
                fPanelPreview.add( pp);
                pageIndex++;
            }
        } catch ( PrinterException e) { MessageKBCT.Error(null, e ); }
        finally { setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); }
        this.ps = new JScrollPane( fPanelPreview) {
       	  static final long serialVersionUID=0;	
          public Dimension getMinimumSize() { return  new Dimension(100,100); }
        };
        getContentPane().add( this.ps, BorderLayout.CENTER);
    }

    class PreviewContainer extends JPanel {
   	    static final long serialVersionUID=0;	
        protected int H_GAP = 16;
        protected int V_GAP = 10;

        public Dimension getPreferredSize() {
            int n = getComponentCount();
            if ( n == 0) return new Dimension( H_GAP, V_GAP);

            Component comp = getComponent( 0);
            Dimension dc = comp.getPreferredSize();
            int w = dc.width;
            int h = dc.height;
            Dimension dp = getParent().getSize();
            int nCol = Math.max( ( dp.width-H_GAP)/( w+H_GAP), 1);
            int nRow = n/nCol;
            if ( nRow*nCol < n) nRow++;

            int ww = nCol*( w+H_GAP) + H_GAP;
            int hh = nRow*( h+V_GAP) + V_GAP;
            Insets ins = getInsets();
            return new Dimension( ww+ins.left+ins.right, hh+ins.top+ins.bottom);
        }

        public Dimension getMaximumSize() { return getPreferredSize(); }

        public Dimension getMinimumSize() { return getPreferredSize(); }

        public void doLayout() {
            Insets ins = getInsets();
            int x = ins.left + H_GAP;
            int y = ins.top + V_GAP;
            int n = getComponentCount();
            if ( n == 0) return;

            Component comp = getComponent( 0);
            Dimension dc = comp.getPreferredSize();
            int w = dc.width;
            int h = dc.height;
            Dimension dp = getParent().getSize();
            int nCol = Math.max( ( dp.width-H_GAP)/( w+H_GAP), 1);
            int nRow = n/nCol;
            if ( nRow*nCol < n) nRow++;

            int index = 0;
            for ( int k = 0; k<nRow; k++) {
                for ( int m = 0; m<nCol; m++) {
                    if ( index >= n) return;
                    comp = getComponent( index++);
                    comp.setBounds( x, y, w, h);
                    x += w+H_GAP;
                }
                y += h+V_GAP;
                x = ins.left + H_GAP;
            }
        }
    }

    class PagePreview extends JPanel {
   	    static final long serialVersionUID=0;	
        protected int m_w;
        protected int m_h;
        protected Image m_source;
        protected Image m_img;

        public PagePreview( int w, int h, Image source) {
            m_w = w;
            m_h = h;
            m_source= source;
            m_img = m_source.getScaledInstance( m_w, m_h, Image.SCALE_SMOOTH);
            m_img.flush();
            setBackground( Color.white);
            setBorder( new MatteBorder( 1, 1, 2, 2, Color.black));
        }

        public void setScaledSize( int w, int h) {
            m_w = w;
            m_h = h;
            m_img = m_source.getScaledInstance( m_w, m_h, Image.SCALE_SMOOTH);
            repaint();
        }

        public Dimension getPreferredSize() {
            Insets ins = getInsets();
            return new Dimension( m_w+ins.left+ins.right, m_h+ins.top+ins.bottom);
        }

        public Dimension getMaximumSize() { return getPreferredSize(); }

        public Dimension getMinimumSize() { return getPreferredSize(); }

        public void paint( Graphics g) {
            g.setColor( getBackground());
            g.fillRect( 0, 0, getWidth(), getHeight());
            g.drawImage( m_img, 0, 0, this);
            paintBorder( g);
        }
    }
}