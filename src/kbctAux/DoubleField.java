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
//                              DoubleField.java
//
//
//**********************************************************************
package kbctAux;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Define a NumberField. Numbers used are double.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

//------------------------------------------------------------------------------
public class DoubleField extends NumberField {
  static final long serialVersionUID=0;	
  private DecimalFormat df = (DecimalFormat)NumberFormat.getCurrencyInstance();
  private NumberFormat nf = DecimalFormat.getNumberInstance();
  private double prec_value;
//------------------------------------------------------------------------------
  public DoubleField() { nf.setMaximumFractionDigits(3); }
//------------------------------------------------------------------------------
  public DoubleField(int dig) { nf.setMaximumFractionDigits(dig); }
//------------------------------------------------------------------------------
  public NumberFormat NumberFormat() { return nf; }
//------------------------------------------------------------------------------
  protected boolean ValidCharacter( char c ) {
    if( (Character.isDigit(c)) || (c==df.getDecimalFormatSymbols().getDecimalSeparator()) || (c==df.getDecimalFormatSymbols().getMinusSign()) )
      return true;
    else
      return false;
  }
//------------------------------------------------------------------------------
  public void setLocale( Locale locale ) {
    super.setLocale(locale);
    df = (DecimalFormat)NumberFormat.getCurrencyInstance(locale);
    nf = DecimalFormat.getNumberInstance(locale);
  }
//------------------------------------------------------------------------------
  public void setValue(double value) { this.prec_value = value; this.setText(nf.format(value)); }
//------------------------------------------------------------------------------
  public double getValue() {
    try { return nf.parse(this.getText()).doubleValue(); }
    catch( ParseException e ) { this.setValue(this.prec_value); }
    return this.prec_value;
  }
}