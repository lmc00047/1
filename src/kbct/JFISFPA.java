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
//                              JFISFPA.java
//
//
//**********************************************************************

package kbct;

/**
 * kbct.JFISFPA is used to represent a FisPro configuration file when
 * we want to induce rules with FPA.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

//------------------------------------------------------------------------------
public class JFISFPA extends JFIS {
  public JFISFPA( JFIS fis, fis.JExtendedDataFile data, int strategy, int min_card, double min_deg  ) throws Throwable {
    super( fis.NewFISFPA(data, strategy, min_card, min_deg) );
  }
}
