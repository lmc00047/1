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
//                            JVertexFile.java
//
//
//**********************************************************************

package kbctFrames;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

import kbct.LocaleKBCT;

/**
 * Classs used in order to generate the window with information about
 * induction of partitions.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
//------------------------------------------------------------------------------
public class JVertexFile {
  private Vector vertices = new Vector();
//------------------------------------------------------------------------------
  public JVertexFile( String file_name ) throws Exception {
	 // System.out.println("JVertexFile: "+file_name);
    // creation d'un buffer de lecture du fichier f
    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file_name))));
    String buffer = br.readLine();
    while( buffer != null ) {
      // tant que le fichier n'est pas entièrement lu
      Vector variable = new Vector();
      int nb_lines = Integer.parseInt(buffer);
      for( int i=0 ; i<nb_lines-1 ; i++ ) {
        Vector v = new Vector();
        buffer = br.readLine();
        StringTokenizer st = new StringTokenizer( buffer, "," );
        if( st.countTokens() != (4 + i) )
          throw new Exception( LocaleKBCT.GetString("ErrorInVerticesFile") + " " + file_name);

        st.nextToken();
        st.nextToken();
        while( st.countTokens() != 0 )
          v.add(new Double(st.nextToken()));

        variable.add(v);
      }
      this.vertices.add(variable);
      buffer = br.readLine();
    }
	  /*System.out.println("JVertexFile: size -> "+VariableCount());
	  int lim= Vertices(2).size();
	  System.out.println("JVertexFile: var2 -> "+lim);
	  for (int n=0; n<lim; n++) {
		  System.out.println("JVertexFile: var2 -> n="+n+"  -> "+Vertices(2).get(n).toString());
	  }*/
  }
//------------------------------------------------------------------------------
public int VariableCount() { return this.vertices.size(); }
//------------------------------------------------------------------------------
public Vector Vertices( int variable ) { return (Vector)this.vertices.elementAt(variable); }
}