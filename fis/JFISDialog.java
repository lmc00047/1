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
//                              JFISDialog.java
//
//
// Author(s) : Jean-Luc LABLEE
// FISPRO Version : 2.1
// Contact : fispro@ensam.inra.fr
// Last modification date:  September 15, 2004
// File :

//**********************************************************************
package fis;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Component;

import javax.swing.JDialog;

import kbctFrames.JKBCTFrame;
import kbctFrames.JChildFrame;

/**
 * fis.JFISDialog.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 04/06/13
 */
//------------------------------------------------------------------------------
/**
 * extension of JDialog for printing
 */

public class JFISDialog extends JDialog {
  static final long serialVersionUID=0;
  public JFISDialog(JKBCTFrame parent) throws HeadlessException {
	super(parent);
    //System.out.println("JFISDialog: Print1");
  }
  public JFISDialog(JChildFrame parent) throws HeadlessException {
	super(parent);
    //System.out.println("JFISDialog: Print2");
  }
  public JFISDialog(Component parent) throws HeadlessException {
    super();
    //System.out.println("JFISDialog: Print3");
  }
//------------------------------------------------------------------------------
  public JFISDialog() throws HeadlessException {
    //System.out.println("JFISDialog: Print4");
    this.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        Dimension min_dim = JFISDialog.this.getMinimumSize();
        Dimension d = JFISDialog.this.getSize();
        if (d.height < min_dim.height || d.width < min_dim.width) {
          if (d.height < min_dim.height) d.height = min_dim.height;
          if (d.width < min_dim.width) d.width = min_dim.width;
          JFISDialog.this.setSize(d);
        }
      }
    });
  }
//------------------------------------------------------------------------------
  public void pack() {
    super.pack();
    Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
    this.setSize(new Dimension(Math.min(this.getSize().width,screen_size.width),Math.min(this.getSize().height,screen_size.height)));
  }
}