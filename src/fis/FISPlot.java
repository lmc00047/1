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
//                              FISPlot.java
//
//
//**********************************************************************

package fis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.Enumeration;
import java.util.Vector;

import ptolemy.plot.Plot;
import ptolemy.plot.PlotBox;

/**
 * fis.FISPlot.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 04/06/13
 */

//------------------------------------------------------------------------------
// extension de la classe Plot

// les fonctions static sont destin�es � �tre utilis�es par les classes Histogram qui ne peuvent pas h�riter de FISPlot.

public class FISPlot extends Plot {
  static final long serialVersionUID=0; 
  public double GetXMin() { return this._xMin; }
  public double GetXMax() { return this._xMax; }
  public static void DisableZoom( PlotBox pb ) { pb.removeMouseListener(pb.getMouseListeners()[0]); }
  // ajustement de la taille du rectangle plot au panel(il faut 1 paintComponent avant !!!!)
  public static void AdjustPlotRectangleSize( Graphics g, PlotBox pb ) {
    Rectangle r1 = pb.getPlotRectangle();
    r1.height = r1.height + r1.y;
    r1.y = 0;
    r1.width = pb.getSize().width - r1.x -10;
    pb.setPlotRectangle(r1);
  }
  //------------------------------------------------------------------------------
  // dessine le domaine de l'entr�e sur l'axe X
  public static void DrawXInputRange( Graphics g, PlotBox pb, double [] input_range, double x_min, double x_max ) {
      Graphics2D g2d = (Graphics2D)g;
      g2d.setColor(Color.red);
      Rectangle rect = pb.getPlotRectangle();
      if( rect.height <= 0 ) return;
      float [] dash = new float[2];
      dash[0] = (float)rect.height/21;
      dash[1] = dash[0];
      BasicStroke bs_dash = new BasicStroke( (float)1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 0, dash, 0 );
      g2d.setStroke(bs_dash);
      if( (input_range[0] >= x_min) && (input_range[0] <= x_max) ) {
        int x = FISPlot.XtoPixel(pb, input_range[0], x_min, x_max);
        g2d.draw(new Line2D.Double(x, rect.y, x, rect.y+rect.height) );
      }
      if( (input_range[1] >= x_min) && (input_range[1] <= x_max) ) {
        int x = FISPlot.XtoPixel(pb, input_range[1], x_min, x_max);
        g2d.draw(new Line2D.Double(x, rect.y, x, rect.y+rect.height) );
      }
  }
//------------------------------------------------------------------------------
      /**
      * Draw Modal Points in Induce Partition Window
      * @param g Graphics object
      * @param pb PlotBox object
      * @param MPV modal points
      * @param x_min lower range
      * @param x_max upper range
      */
  public static void DrawMPInducePartitions( Graphics g, PlotBox pb, Vector MPV, double x_min, double x_max  ) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.green);
        Rectangle rect = pb.getPlotRectangle();
        if( rect.height <= 0 ) return;
        float [] dash = new float[2];
        dash[0] = (float)rect.height/21;
        dash[1] = dash[0];
        BasicStroke bs_dash = new BasicStroke( (float)2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 0, dash, 0 );
        g2d.setStroke(bs_dash);
        Enumeration en= MPV.elements();
        while (en.hasMoreElements()) {
          String MP = (String)en.nextElement();
          Double mp = new Double(MP);
          int x = FISPlot.XtoPixel(pb, mp.doubleValue(), x_min, x_max);
          g2d.draw(new Line2D.Double(x, rect.y, x, rect.y+rect.height) );
        }
  }
//------------------------------------------------------------------------------
    /**
     * Dessine les bornes du domaine de l'entr�e sur l'axe Y\n
     * dessine 2 traits pointill�s rouge sur les bornes
     * @param g objet Graphics
     * @param input_range bornes � afficher
     */
  public void DrawYInputRange( Graphics g, double [] input_range ) {
      Graphics2D g2d = (Graphics2D)g;
      g2d.setColor(Color.red);
      Rectangle rect = this.getPlotRectangle();
      if( rect.width <= 0 ) return;
      float [] dash = new float[2];
      dash[0] = (float)rect.width/21;
      dash[1] = dash[0];
      BasicStroke bs_dash = new BasicStroke( (float)1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 0, dash, 0 );
      g2d.setStroke(bs_dash);
      if( (input_range[0] >= this._yMin) && (input_range[0] <= this._yMax) ) {
        int y = this.YtoPixel(input_range[0]);
        g2d.draw(new Line2D.Double(rect.x, y, rect.x+rect.width, y) );
      }
      if( (input_range[1] >= this._yMin) && (input_range[1] <= this._yMax) ) {
        int y = this.YtoPixel(input_range[1]);
        g2d.draw(new Line2D.Double(rect.x, y, rect.x+rect.width, y) );
      }
  }
//------------------------------------------------------------------------------
  /**
    * Conversion d'une abscisse x en abscisse pixel dans le panel
    * @param pb objet PlotBox
    * @param x abscisse � convertir
    * @param x_min abscisse min de l'objet PlotBox (passer PlotBox._xMin)
    * @param x_max abscisse max de l'objet PlotBox (passer PlotBox._xMax)
    * @return abscisse pixel dans le panel
    */
  public static int XtoPixel( PlotBox pb, double x, double x_min, double x_max ) {
    Rectangle rect = pb.getPlotRectangle();
    return (int)(rect.getX() + (((x-x_min)*rect.getWidth())/(x_max-x_min)));
  }
//------------------------------------------------------------------------------
  /**
    * Conversion d'une abscisse x en pixel en abscisse dans le panel
    * @param x abscisse en pixel � convertir
    * @return abscisse dans le panel
    */
  public double PixelToX( int x ) {
     Rectangle rect = this.getPlotRectangle();
     return this._xMin + ((((double)x - (double)rect.x)/(double)rect.width)*(this._xMax-this._xMin));
  }
//------------------------------------------------------------------------------
     /**
     * Conversion d'une ordonn�e y en pixel en ordonnn�e dans le panel
     * @param x abscisse en pixel � convertir
     * @return abscisse dans le panel
     */
  public double PixelToY( int y ) {
      Rectangle rect = this.getPlotRectangle();
      y-- ; //there's an one-pixel offset
      return ((this._yMin)*2 + ((this._yMax - this._yMin) - (this._yMin + ((((double)y - (double)rect.y)/(double)rect.height)*(this._yMax-this._yMin)))));
  }
//------------------------------------------------------------------------------
    /**
    * Conversion d'une abscisse x en abscisse pixel dans le panel\n
    * @param x abscisse � convertir
    */
  public int XtoPixel( double x ) { return FISPlot.XtoPixel(this, x, this._xMin, this._xMax); }
//------------------------------------------------------------------------------
    /**
    * Conversion d'une ordonn�e y en ordonn�e pixel dans le panel
    * @param y ordonn�e � convertir
    * @return ordonn�e pixel dans le panel
    */
  public int YtoPixel( double y ) {
    Rectangle rect = this.getPlotRectangle();
    return (int)(rect.getY() + rect.getHeight() - (((y-_yMin)*rect.getHeight())/(_yMax-_yMin))) + 1;
  }
}
