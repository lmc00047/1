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
//                          FISListener.java
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
 * fis.FISListener.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 04/06/13
 */

//------------------------------------------------------------------------------
public interface FISListener {
  public void FISClosed();
  public void InputReplaced( int input_number );
  public void OutputReplaced( int output_number );
  public void InputActiveChanged( int input_number );
  public void OutputActiveChanged( int output_number );
  public void InputNameChanged( int input_number );
  public void OutputNameChanged( int output_number );
  public void InputRangeChanged( int input_number );
  public void OutputRangeChanged( int output_number );
  public void InputRemoved( int input_number );
  public void OutputRemoved( int output_number );
  public void InputAdded( int input_number );
  public void OutputAdded( int output_number );
  public void MFRemovedInInput( int input_number );
  public void MFRemovedInOutput( int output_number );
  public void MFAddedInInput( int input_number );
  public void MFAddedInOutput( int output_number );
  public void MFReplacedInInput( int input_number );
  public void MFReplacedInOutput( int output_number );
  public void OutputDefuzChanged( int output_number );
  public void OutputDisjChanged( int output_number );
  public void OutputClassifChanged( int output_number );
  public void OutputDefaultChanged( int output_number );
  public void OutputAlarmThresChanged( int output_number );
  public void RulesModified();
  public void ConjunctionChanged();
}
