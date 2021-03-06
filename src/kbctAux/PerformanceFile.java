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
//                       PerformanceFile.java
//
//
//**********************************************************************

package kbctAux;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

import fis.jnifis;

/**
 * kbctAux.PerformanceFile.
 * Build a JFile in order to read "result" file generated by FisPro.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

//------------------------------------------------------------------------------
public class PerformanceFile extends JFile {
//------------------------------------------------------------------------------
  public PerformanceFile( String file_name ) throws Exception {
    super(file_name);
    this.LoadFile(file_name);
  }
//------------------------------------------------------------------------------
  public void Reload( String file_name ) throws Exception {
    this.LoadFile(file_name);
    super.Reload(file_name);
  }
//------------------------------------------------------------------------------
  private void LoadFile( String file_name ) throws Exception {
    File f = new File(file_name);
    BufferedReader br = new BufferedReader( new InputStreamReader( new FileInputStream(f) ) );
    String buffer;
    Vector<Vector> vect_data = new Vector<Vector>();
    br.readLine(); // saltar linea 1
    while( (buffer = br.readLine()) != null ) {
      StringTokenizer st = new StringTokenizer( buffer, " " );
      Vector<Double> v = new Vector<Double>();
      while( st.countTokens() != 0 ) {
    	String aux= st.nextToken();
    	if (aux.contains("-")) {
    		int index= aux.indexOf("-");
    		if (index>0) {
    		    String aux1=aux.substring(0,index);
                v.add(new Double(aux1));
    		    String aux2=aux.substring(index);
                v.add(new Double(aux2));
    		} else 
                v.add(new Double(aux));
     	} else
            v.add(new Double(aux));
      }
      vect_data.add(v);
    }
    this.InitData(vect_data.size());
    for( int i=0 ; i<this.DataLength() ; i++ ) {
      Vector v = (Vector)vect_data.elementAt(i);
      this.InitDataRow(i, v.size());
      for( int j=0 ; j<v.size() ; j++ )
        this.setDataElement(i, j, ((Double)v.elementAt(j)).doubleValue());
    }
  }
//------------------------------------------------------------------------------
  protected double [][] LoadData() throws Throwable { return jnifis.DataFile(this.FileName()); }
}