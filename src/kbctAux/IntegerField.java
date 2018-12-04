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
//                              IntegerField.java
//
//
//**********************************************************************
package kbctAux;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Define a NumberField. Numbers used are int.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

//------------------------------------------------------------------------------
public class IntegerField extends NumberField {
  static final long serialVersionUID=0;	
  private NumberFormat nf = NumberFormat.getInstance();
  private int prec_value;
//------------------------------------------------------------------------------
  protected boolean ValidCharacter( char c ) {
    if( (Character.isDigit(c)) || (c == ((DecimalFormat)NumberFormat.getCurrencyInstance()).getDecimalFormatSymbols().getMinusSign()) )
      return true;
    else
      return false;
  }
//------------------------------------------------------------------------------
  public void setValue(int value) { this.prec_value = value; this.setText(nf.format(new Integer(value))); }
//------------------------------------------------------------------------------
  public int getValue() {
    try { return nf.parse(this.getText()).intValue(); }
    catch( ParseException e ) { this.setValue(this.prec_value); }
    return this.prec_value;
  }
}