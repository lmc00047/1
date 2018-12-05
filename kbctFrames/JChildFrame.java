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
//                         JChildFrame.java
//
//
//**********************************************************************

package kbctFrames;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import kbctAux.Translatable;

/**
 * kbctFrames.JChildFrame
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
//------------------------------------------------------------------------------
  public abstract class JChildFrame extends JDialog implements Translatable {
  protected JKBCTFrame parent;
  protected JRuleFrame parent_JRF;
//------------------------------------------------------------------------------
  JChildFrame( JKBCTFrame parent ) {
    super(parent);
    this.parent= parent;
    JKBCTFrame.AddTranslatable(this);
    this.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { this_windowClosing(); } });
  }
//------------------------------------------------------------------------------
  JChildFrame( JRuleFrame parent ) {
    super(parent);
    this.parent_JRF = parent;
    JKBCTFrame.AddTranslatable(this);
    this.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { this_windowClosing(); } });
  }
//------------------------------------------------------------------------------
  public void dispose() {
    JKBCTFrame.RemoveTranslatable(this);
    super.dispose();
  }
//------------------------------------------------------------------------------
  public Point ChildPosition( Dimension child_dimension ) {
    if (this.parent!=null)
      return ChildPosition( this.parent, child_dimension );
    else if (this.parent_JRF!=null)
      return ChildPosition( this.parent_JRF, child_dimension );
    else
      return null;
  }
//------------------------------------------------------------------------------
  public static Point ChildPosition( Component parent, Dimension child_dimension ) {
    int x_parent_center = parent.getX() + parent.getSize().width/2;
    int y_parent_center = parent.getY() + parent.getSize().height/2;
    int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
    int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
    int x_child = Math.min(x_parent_center - child_dimension.width/2, screen_width - child_dimension.width);
    x_child = Math.max(x_child, 0);
    int y_child = Math.min(y_parent_center - child_dimension.height/2, screen_height - child_dimension.height);
    y_child = Math.max(y_child, 0);
    return new Point(x_child, y_child);
  }
//------------------------------------------------------------------------------
  public void this_windowClosing() { this.dispose(); }
}