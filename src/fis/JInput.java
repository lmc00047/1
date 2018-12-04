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
//                              JInput.java
//
//
// Author(s) : Jean-Luc LABLEE
// FISPRO Version : 2.1
// Contact : fispro@ensam.inra.fr
// Last modification date:  September 15, 2004
// File :

//**********************************************************************
package fis;

import kbct.LocaleKBCT;

/**
 * fis.JInput.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 04/06/13
 */

//------------------------------------------------------------------------------
public class JInput {
  protected long ptr;
//------------------------------------------------------------------------------
  public JInput() throws Throwable {
    this.ptr = jnifis.NewInput();
    this.SetName("new input");
    this.SetRange(0, 1);
    this.SetActive(true);
  }
//------------------------------------------------------------------------------
  public JInput( JInput input ) throws Throwable { this.ptr = jnifis.NewInput( input.Ptr() ); }
//------------------------------------------------------------------------------
  public JInput( long ptr ) { this.ptr = ptr; }
//------------------------------------------------------------------------------
  public JInput( double [] t, double min, double max, boolean sort ) throws Throwable {
    this.ptr = jnifis.NewInput(t, min, max, sort);
    this.SetName("new input");
    this.SetActive(true);
  }
//------------------------------------------------------------------------------
  public JInput NewRegular( int nb_sef, double min, double max) throws Throwable {
    JInput new_input = new JInput( jnifis.NewRegularInput(nb_sef, min, max) );
    new_input.SetName(this.GetName());
    new_input.SetActive(this.GetActive());
    // les SEF sont créés sans nom -> affectation d'un nom par defaut
    for( int i=0 ; i<new_input.GetNbMF() ; i++ )
      ((JMF)new_input.GetMF(i)).SetName(LocaleKBCT.GetString("MF") + String.valueOf(i+1) );

    return new_input;
  }
//------------------------------------------------------------------------------
  public JInput NewIrregular( double []sommets, int nb_sef, double min, double max) throws Throwable {
    JInput new_input = new JInput( jnifis.NewIrregularInput(sommets, nb_sef, min, max) );
    new_input.SetName(this.GetName());
    new_input.SetActive(this.GetActive());
    // les SEF sont créés sans nom -> affectation d'un nom par defaut
    for( int i=0 ; i<new_input.GetNbMF() ; i++ )
      ((JMF)new_input.GetMF(i)).SetName(LocaleKBCT.GetString("MF") + String.valueOf(i+1) );

    return new_input;
  }
//------------------------------------------------------------------------------
  public void Delete() throws Throwable { jnifis.DeleteInput( ptr ); ptr = 0; }
//------------------------------------------------------------------------------
  public long Ptr() { return ptr; }
//------------------------------------------------------------------------------
  public String GetName() throws Throwable { return jnifis.GetInputName( ptr ); }
//------------------------------------------------------------------------------
  public boolean GetActive() throws Throwable { return jnifis.GetInputActive( ptr ); }
//------------------------------------------------------------------------------
  public double[] GetRange() throws Throwable { return jnifis.GetInputRange( ptr ); }
//------------------------------------------------------------------------------
  public void SetName( String name ) throws Throwable { jnifis.SetInputName(this.ptr, name); }
//------------------------------------------------------------------------------
  public void SetRange( double[] range ) throws Throwable { jnifis.SetInputRange(this.ptr, range[0], range[1]); }
//------------------------------------------------------------------------------
  public void SetRange( double range_min, double range_max ) throws Throwable { jnifis.SetInputRange(this.ptr, range_min, range_max); }
//------------------------------------------------------------------------------
  public void SetActive( boolean active ) throws Throwable { jnifis.SetInputActive(this.ptr, active); }
//------------------------------------------------------------------------------
  public int GetNbMF() throws Throwable {  return jnifis.GetNbMF( ptr ); }
//------------------------------------------------------------------------------
  public JMF GetMF( int mf_number ) throws Throwable { return new JMF( jnifis.GetMF(ptr, mf_number) ); }
//------------------------------------------------------------------------------
  public void AddMF( JMF sef ) throws Throwable { jnifis.AddMF(ptr, sef.Ptr()); }
//------------------------------------------------------------------------------
  public void ReplaceMF( int mf_number, JMF new_sef ) throws Throwable { jnifis.ReplaceMF(ptr, mf_number, new_sef.Ptr()); }
//------------------------------------------------------------------------------
  public double [] Appartenance() throws Throwable { return jnifis.InputAppartenance(ptr); }
}
