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
//                              JRule.java
//
//
// Author(s) : Jean-Luc LABLEE
// FISPRO Version : 2.1
// Contact : fispro@ensam.inra.fr
// Last modification date:  September 15, 2004
// File :

//**********************************************************************
package fis;

/**
 * fis.JRule.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 04/06/13
 */

//------------------------------------------------------------------------------
public class JRule {
  private long ptr;
//------------------------------------------------------------------------------
  public JRule( long ptr ) throws Throwable { this.ptr = ptr; }
//------------------------------------------------------------------------------
  public int [] Facteurs() throws Throwable { return jnifis.GetRuleProps(this.ptr); }
//------------------------------------------------------------------------------
  public double [] Actions() throws Throwable { return jnifis.GetRuleConcs(this.ptr); }
//------------------------------------------------------------------------------
  public boolean GetActive() throws Throwable { return jnifis.GetRuleActive(this.ptr); }
//------------------------------------------------------------------------------
  public void SetActive( boolean active ) throws Throwable { jnifis.SetRuleActive(this.ptr, active); }
//------------------------------------------------------------------------------
  public double Poids() throws Throwable { return jnifis.RulePoids(this.ptr); }
//------------------------------------------------------------------------------
  public long Ptr() { return this.ptr; }
//------------------------------------------------------------------------------
  public void Delete() {
    try { jnifis.DeleteRule(this.ptr); }
    catch( Throwable t ) {}
  }
//------------------------------------------------------------------------------
  public boolean PremisseEqual( JRule rule ) throws Throwable { return jnifis.RulePremisseEqual(this.ptr, rule.Ptr()); }
//------------------------------------------------------------------------------
  public void SetProp( int input_number, int prop ) throws Throwable { jnifis.SetRuleProp(this.ptr, input_number, prop); }
//------------------------------------------------------------------------------
  public void SetConc( int output_number, double conc ) throws Throwable { jnifis.SetRuleConc(this.ptr, output_number, conc); }
}
