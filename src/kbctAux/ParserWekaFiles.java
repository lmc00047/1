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
//                           ParserWekaFiles.java
//
//
//**********************************************************************
package kbctAux;

import java.io.FileOutputStream;
import java.io.PrintWriter;

import kbct.JKBCT;
import kbct.JKBCTInput;
import kbct.JKBCTOutput;
import weka.core.Instances;
import fis.JExtendedDataFile;

/**
 * kbctAux.ParserWekaFiles
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

public class ParserWekaFiles {
	private JExtendedDataFile jedf;
	private JKBCT kbct;
	private double[] outClasses;
	
	public ParserWekaFiles(JExtendedDataFile jedf, JKBCT kbct) {
    	this.jedf= jedf;
    	this.kbct= kbct;
    }
	
	public void buildWekaARFFfile(String file_name) {
        try {
		    PrintWriter fOut = new PrintWriter(new FileOutputStream(file_name), true);
		    fOut.println("@RELATION "+"'"+this.kbct.GetName()+"'");
		    fOut.println();
            for (int n=0; n<this.kbct.GetNbInputs(); n++) {
                JKBCTInput in= this.kbct.GetInput(n+1);
        		String name= in.GetName().replaceAll("'","");
            	fOut.print("@ATTRIBUTE "+"'"+name+"'"+"	");
            	if (in.GetType().equals("numerical")) {
                	fOut.println("NUMERIC");
            	} else {
                	fOut.println("INTEGER");
            	}
            }
            for (int n=0; n<this.kbct.GetNbOutputs(); n++) {
                JKBCTOutput out= this.kbct.GetOutput(n+1);
        		String name= out.GetName().replaceAll("'","");
            	if (out.GetType().equals("numerical")) {
                    fOut.println("@ATTRIBUTE "+"'"+name+"'"+"	"+"NUMERIC");
            	} else {
            		fOut.print("@ATTRIBUTE "+"'"+name+"'"+"	"+"{");
            		double[] range= out.GetInputInterestRange();
            		int lim=out.GetLabelsNumber();
            		this.outClasses= new double[lim];
            		if ( (range[0]==1) && (range[1]==lim) ) {
            		    for (int k=0; k<lim; k++) {
           		    	     if ( (out.GetScaleName().equals("user")) && (!out.GetMP(k+1).equals(""+String.valueOf(k+1))) && (!out.GetMP(k+1).equals("No MP")) ) {
            		    		 double hv= (new Double(out.GetMP(k+1))).doubleValue();
           			             fOut.print(hv);
            			         this.outClasses[k]= hv;
           		    	     } else {
                			     fOut.print(String.valueOf(k+1)+".0");
                			     this.outClasses[k]= k+1;
           		    	     }
            			     if (k!=lim-1) 
            				     fOut.print(",");
            		    }
            		} else {
            		    for (int k=0; k<lim; k++) {
            		    	 if (out.GetScaleName().equals("user")) {
            		    		 double hv= (new Double(out.GetMP(k+1))).doubleValue();
           			             fOut.print(hv);
            			         this.outClasses[k]= hv;
            		    	 } else {
           			             fOut.print(String.valueOf(range[0]+k));
            			         this.outClasses[k]= range[0]+k;
            		    	 }
           			         if (k!=lim-1) 
           				         fOut.print(",");
           		        }
            		}
        			fOut.println("}");
            	}
          	}
		    fOut.println();
		    fOut.println("@DATA");		    
		    int NbLines = this.jedf.VariableData(0).length;
		    int NbVar= this.jedf.VariableCount();
		    for (int n = 0; n < NbLines; n++) {
		        for (int k = 0; k < NbVar; k++)
		          if (k == NbVar - 1)
		            fOut.println(this.jedf.VariableData(k)[n]);
		          else
		            fOut.print(this.jedf.VariableData(k)[n] + ",");
		    }
		    fOut.flush();
		    fOut.close();
        } catch (Throwable t) {
            t.printStackTrace();
        	MessageKBCT.Error(null, t);
        }
    }
	public void buildKBCTDataFile(String file_name, Instances centroids) {
        try {
		    PrintWriter fOut = new PrintWriter(new FileOutputStream(file_name), true);
		    int NbLines = (centroids.attributeToDoubleArray(0)).length;
            int NbVar= centroids.numAttributes();
            int NbOutputs= this.kbct.GetNbOutputs();
            
            double[][] data= new double[NbVar][NbLines];
            for (int n = 0; n < NbVar; n++) {
                 data[n]= centroids.attributeToDoubleArray(n);
            }            
            for (int n = 0; n < NbLines; n++) {
		        for (int k = 0; k < NbVar; k++) {
		        	if (this.outClasses != null) {
                        if (k>=NbVar-NbOutputs)
                	        data[k][n]++;
		        
		                if (k == NbVar - 1) {
		        	       //System.out.println("data[k]["+n+"]="+data[k][n]);
		                   //fOut.println(data[k][n]);
		                   fOut.println(this.outClasses[(int)data[k][n]-1]);
		                } else {
		                   fOut.print(data[k][n] + ",");
		                }
		        	} else {
		                if (k == NbVar - 1) {
			                fOut.println(data[k][n]);
		                } else {
			                fOut.print(data[k][n] + ",");
		                }
		        	}
		        }
		    }
		    fOut.flush();
		    fOut.close();
        } catch (Throwable t) {
            t.printStackTrace();
        	MessageKBCT.Error(null, t);
        }
    }
}