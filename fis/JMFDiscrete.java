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
//                              JMFDiscrete.java
//
//
// Author(s) : Jean-Luc LABLEE
// FISPRO Version : 2.1
// Contact : fispro@ensam.inra.fr
// Last modification date:  September 15, 2004
// File :

//**********************************************************************

package fis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 * fis.JExtendedDataFile is the class used to work with data files.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 04/06/13
 */

//------------------------------------------------------------------------------
public class JMFDiscrete extends JMF {
//------------------------------------------------------------------------------
  public JMFDiscrete( JMF sef ) { super( sef.Ptr() ); }
//------------------------------------------------------------------------------
  public JMFDiscrete( String name, double[] params ) throws Throwable {
    super( jnifis.NewMFDiscrete(name, params) );
  }
//------------------------------------------------------------------------------
  public void Draw( FISPlot plot, int data_set ) throws Throwable {
    double [] params = GetParams();
    for( int i=0 ; i<params.length ; i++ ) {
      plot.addPoint(data_set, params[i], 0, false);
      plot.addPoint(data_set, params[i], 1, true);
    }
  }
//------------------------------------------------------------------------------
  public void DrawAlphaKernel( FISPlot pb, Graphics2D g2d, double alpha )  throws Throwable {
    g2d.setColor(Color.blue);
    double [] params = this.GetParams();
    for( int i=0 ; i<params.length ; i++ )
      g2d.draw(new Line2D.Double(pb.XtoPixel(params[i]), pb.YtoPixel(0), pb.XtoPixel(params[i]), pb.YtoPixel(alpha)));
  }
}
