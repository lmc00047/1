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
//                              jnikbct.java
//
//
//**********************************************************************
package kbct;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import xml.XMLParser;
import xml.XMLWriter;
import KB.Data_System;
import KB.LabelKBCT;
import KB.Rule;
import KB.variable;
import fis.jnifis;

/**
 * <p align="left">
 * kbct.jnikbct has two types of methods:
 * </p>
 * <ul>
 *   <li> native methods from jnikbct c++ library </li>
 *   <li> java methods to generate and modify the Knowledge Base </li>
 * </ul>
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
//------------------------------------------------------------------------------
public class jnikbct {
  /**
   * store of Knowledge Base systems
   */
  private static Hashtable<String,Data_System> ht;
  /**
   * Knowlege Base identifier
   */
  private static long id;
  //----------------------------------------------------------------------------
  public jnikbct() {
    id= 0;
    ht= new Hashtable<String,Data_System>();
  }
  //----------------------------------------------------------------------------
  /**
   * Return how many different values are there in data.
   */
  public static double[] InitUniq(double[] data) {
	  int cont=0;
	  int lim= data.length;
	  //System.out.println("  lim="+lim);
	  int[] flags= new int[lim];
	  for (int n=0; n<lim; n++) {
		   //System.out.println("  data["+n+"]="+data[n]);
		   flags[n]=0;
	  }
	  for (int n=0; n<lim; n++) {
		   if (flags[n]==0) {
		     double v= data[n];
 	  	     //System.out.println("  v="+v);
             for (int k=n+1; k<lim; k++) {
        	    if ( (flags[k]==0) && (v == data[k]) ) {
        	    	flags[n]++;
        	    	flags[k]=-1;
        	  	    //System.out.println("  -> v="+v);
        	    }
             }
		   }
	  }
	  Vector<Double> values= new Vector<Double>();
	  for (int n=0; n<lim; n++) {
		  if (flags[n]>=0) {
			  cont++;
			  values.add(new Double(data[n]));
		  }
	  }
	  double[] res= new double[cont+1];
	  //System.out.println("  cont="+cont);
	  res[0]= cont;
	  Object[] obj= values.toArray();
	  double[] auxobj= new double[obj.length];
	  for (int n=0; n<obj.length; n++) {
		   auxobj[n]= ((Double)obj[n]).doubleValue();
	  }
	  double[] ordobj= new double[obj.length];
	  double max= jnikbct.findMax(auxobj)[1];
	  //System.out.println("  max="+max+"  auxobj.length="+auxobj.length);
	  for (int n=0; n<obj.length; n++) {
		   double[] aux= jnikbct.findMin(auxobj);
		   //System.out.println("n="+n+"  min="+aux[1]+"  minind="+aux[0]);
		   ordobj[n]= aux[1];
		   auxobj[(int)aux[0]-1]= max+1;
	  }
	  for (int n=1; n<res.length; n++) {
		   res[n]= ordobj[n-1];
	  }
	  return res;
  }
  //----------------------------------------------------------------------------
  /**
   * Open data file "file_name".
   */
  public static double [][] DataFile( String file_name ) throws Throwable {
    return jnifis.DataFile(file_name);
  }
  //----------------------------------------------------------------------------
  /**
   * Set data file path.
   */
  public static void SetDataFilePath( String new_path ) throws Throwable {
    jnifis.SetDataFilePath(new_path);
  }
  //----------------------------------------------------------------------------
  /**
   * Generate a new Knowledge Base
   */
  public static long NewKBCT() {
    jnikbct.id= jnikbct.id+1;
    Data_System ds= new Data_System();
    ht.put(""+jnikbct.id,ds);
    return jnikbct.id;
  }
  //----------------------------------------------------------------------------
  /**
   * Generate a new Knowledge Base. It was stored in "file_name".
   */
  public static long NewKBCT( String file_name ) throws Throwable {
	  if (file_name.endsWith("xml"))
		  return NewKBCTXML(file_name);
	  else {
		  long res= NewKBCTPlain(file_name);
		  // Save as xml
		  SaveKBCT(res,file_name+".xml");
		  return res;
	  }
  }    
  //----------------------------------------------------------------------------
  /**
   * Generate a new Knowledge Base. It was stored in "file_name".
   */
  public static long NewKBCTPlain( String file_name ) throws Throwable {
    jnikbct.id= jnikbct.id+1;
    Data_System ds= new Data_System();
	//System.out.println("FileName="+file_name);
    File file = new File(file_name);
    LineNumberReader lnr= new LineNumberReader(new InputStreamReader(new FileInputStream(file)));
    String l, aux;
    int NbInputs=0, NbOutputs=0, NbRules= 0;
    int cont=0;
    while((l= lnr.readLine())!=null) {
        cont++;
        if (l.equals("[System]")) {
            aux= lnr.readLine();
            ds.SetName(aux.substring(6,aux.length()-1));
            NbInputs= Integer.parseInt(lnr.readLine().substring(8));
        	//System.out.println("NbInputs="+NbInputs);
            NbOutputs= Integer.parseInt(lnr.readLine().substring(9));
        	//System.out.println("NbOutputs="+NbOutputs);
            NbRules= Integer.parseInt(lnr.readLine().substring(7));
        	//System.out.println("NbRules="+NbRules);
        } else if ((l.startsWith("[Input")) || (l.startsWith("[Output"))) {
            aux= lnr.readLine();
            variable v= new variable();
            if (aux.substring(7).equals("'yes'"))
              v.SetActive(true);
            else
              v.SetActive(false);

            aux= lnr.readLine();
            //System.out.println("aux1="+aux);
            v.SetName(aux.substring(6,aux.length()-1));
            aux= lnr.readLine();
            v.SetType(aux.substring(6,aux.length()-1));
            aux= lnr.readLine();
            v.SetTrust(aux.substring(7,aux.length()-1));
            if (l.startsWith("[Output")) {
              aux= lnr.readLine();
              v.SetClassif(aux.substring(9,aux.length()-1));
            }
            aux= lnr.readLine();
            int indPR= aux.indexOf(",");
            v.SetLowerPhysicalRange((new Double(aux.substring(19, indPR))).doubleValue());
            v.SetUpperPhysicalRange((new Double(aux.substring(indPR+2, aux.length()-1))).doubleValue());
            aux= lnr.readLine();
            int indIR= aux.indexOf(",");
            v.SetLowerInterestRange((new Double(aux.substring(19, indIR))).doubleValue());
            v.SetUpperInterestRange((new Double(aux.substring(indIR+2, aux.length()-1))).doubleValue());
            int LabelsNumber= Integer.parseInt(lnr.readLine().substring(17));
            v.InitLabelsName(LabelsNumber);
            aux= lnr.readLine();
            String LinguisticLabel= aux.substring(19, aux.length()-1);
            v.SetScaleName(LinguisticLabel);
            for (int n=0; n < LabelsNumber; n++) {
              aux= lnr.readLine();
              String Label_Name="", Label_Def="", MP="No MP", MPaux="No MP";
              double P1=0, P2=0, P3=0, P4=0;
              for (int m=0; m < 2; m++){
                int ind=0;
                int pos=0;
                for (int i=0;i<aux.length();i++) {
                    if((aux.charAt(i))=='=') {
                        pos=i;
                    } else if((aux.charAt(i))==',') {
                         ind=i; break;
                    }
                }
                switch (m) {
                     case 0: Label_Name = aux.substring(pos+2, ind - 1);
                             aux = aux.substring(ind + 2);
                             break;
                     case 1: Label_Def = aux.substring(0, ind - 1);
                             aux = aux.substring(ind + 5);
                             break;
                }
              }
              int N_params= 1;
              if (Label_Def.equals("trapezoidal"))
                  N_params= 4;
              else if (Label_Def.equals("SemiTrapezoidalInf") || Label_Def.equals("triangular") || Label_Def.equals("SemiTrapezoidalSup"))
                  N_params= 3;
              else if (Label_Def.equals("universal") || Label_Def.equals("gaussian"))
                  N_params= 2;
              else if (Label_Def.equals("discrete"))
                  N_params= 1;

              int ind= 0;
              for (int i=0;i<aux.length();i++)
                   if((aux.charAt(i))==']') {
                       ind=i; break;
                   }

              if (N_params <4)
                  MP= aux.substring(ind+6,aux.length()-1);
              else {
                  int indaux= 0;
                  String ss= aux.substring(ind+6,aux.length()-1);
                  //System.out.println("ss="+ss);
                  for (int i=0;i<ss.length();i++)
                       if((ss.charAt(i))==']') {
                           indaux=i; break;
                       }
                  MP= ss.substring(0,indaux);
                  //System.out.println("MP="+MP);
            	  MPaux= ss.substring(indaux+6,ss.length());
                  //System.out.println("MPaux="+MPaux);
              }
              for (int m=0; m < N_params; m++){
                   ind=0;
                   for (int i=0;i<aux.length();i++)
                        if((aux.charAt(i))==',') {
                            ind=i; break;
                            }
                   switch (m) {
                     case 0: if (N_params>1)
                               P1= (new Double(aux.substring(0, ind))).doubleValue();
                             else
                               P1= (new Double(aux.substring(0, ind-1))).doubleValue();
                             aux= aux.substring(ind + 4);
                             break;
                     case 1: if (N_params>2)
                               P2= (new Double(aux.substring(0, ind))).doubleValue();
                             else
                               P2= (new Double(aux.substring(0, ind-1))).doubleValue();
                             aux= aux.substring(ind + 4);
                             break;
                     case 2: if (N_params>3)
                               P3= (new Double(aux.substring(0, ind))).doubleValue();
                             else
                               P3= (new Double(aux.substring(0, ind-1))).doubleValue();
                             aux= aux.substring(ind + 4);
                             break;
                     case 3: P4= (new Double(aux.substring(0, ind-1))).doubleValue();
                             break;
                   }
                }
                LabelKBCT e= new LabelKBCT();
                if (Label_Def.equals("trapezoidal")) {
                  e= new LabelKBCT(Label_Def, P1, P2, P3, P4, n+1);
                  e.SetMP(MP);
                  e.SetMPaux(MPaux);
                } else if (Label_Def.equals("SemiTrapezoidalInf") || Label_Def.equals("triangular") || Label_Def.equals("SemiTrapezoidalSup")) {
                  e= new LabelKBCT(Label_Def, P1, P2, P3, n+1);
                  e.SetMP(MP);
                } else if (Label_Def.equals("universal") || Label_Def.equals("gaussian")) {
                  e= new LabelKBCT(Label_Def, P1, P2, n+1);
                  e.SetMP(MP);
                } else if (Label_Def.equals("discrete")) {
                  e= new LabelKBCT(Label_Def, P1, n+1);
                  e.SetMP(MP);
                }
                v.SetLabelsName(n+1, Label_Name);
                if (LinguisticLabel.equals("user"))
                  v.SetUserLabelsName(n+1, Label_Name);

                v.AddLabel(e);
                v.SetLabelsNumber(LabelsNumber);
            }
            aux= lnr.readLine();
            //System.out.println("aux2="+aux);
            if ( (aux!=null) && (aux.startsWith("FlagOntology")) )
                v.SetFlagOntology((new Boolean(aux.substring(14,aux.length()-1))).booleanValue());

            if (v.GetFlagOntology()) {
              aux= lnr.readLine();
              //System.out.println("aux3="+aux);
              if ( (aux!=null) && (aux.startsWith("NameFromOntology")) )
                  v.SetNameFromOntology(aux.substring(18,aux.length()-1));

              aux= lnr.readLine();
              //System.out.println("aux4="+aux);
              if ( (aux!=null) && (aux.startsWith("FlagOntDatatypeProperty")) )
                  v.SetFlagOntDatatypeProperty((new Boolean(aux.substring(25,aux.length()-1))).booleanValue());

              aux= lnr.readLine();
              //System.out.println("aux5="+aux);
              if ( (aux!=null) && (aux.startsWith("FlagOntObjectProperty")) )
                  v.SetFlagOntObjectProperty((new Boolean(aux.substring(23,aux.length()-1))).booleanValue());
            }
            v.SetORLabelsName();
            if (l.startsWith("[Input"))
              ds.AddInput(v);
            else if (l.startsWith("[Output"))
              ds.AddOutput(v);

        } else if (l.equals("[Rules]")) {
            for (int n=0; n<NbRules; n++) {
              int[] in_labels= new int[NbInputs];
              int[] out_labels= new int[NbOutputs];
              aux= lnr.readLine();
              for (int m=0; m<NbInputs+NbOutputs; m++) {
                 int ind= aux.indexOf(",");
                 if (m < NbInputs) {
                   in_labels[m]= Integer.parseInt(aux.substring(0, ind));
                   if (m==NbInputs-1)
                     aux= aux.substring(ind+5);
                   else
                     aux= aux.substring(ind+2);
                 } else if (m>=NbInputs) {
                    out_labels[m-NbInputs]= Integer.parseInt(aux.substring(0, ind-4));
                    aux= aux.substring(ind+2);
                 }
              }
              aux= aux.substring(3); //Type
              String Type= aux.substring(0,1);
              String Active= aux.substring(6); //Active
              boolean a;
              if (Active.equals("A"))
                a= true;
              else
                a= false;

              Rule r= new Rule(n+1, NbInputs, NbOutputs, in_labels, out_labels, Type, a);
              ds.AddRule(r);
            }
        }
    }
    if (ht==null) {
    	//System.out.println("ht NULL");
        ht= new Hashtable<String,Data_System>();
    	//System.out.println("Created new ht");
    }
    //if (ds==null)
    	//System.out.println("ds NULL");

	//System.out.println("jnikbct.id="+jnikbct.id);
    ht.put(""+jnikbct.id,ds);
    lnr.close();
    return jnikbct.id;
  }
  //----------------------------------------------------------------------------
  /**
   * Generate a new Knowledge Base. It was stored in "file_name".
   */
  public static long NewKBCTXML( String file_name ) throws Throwable {
      //System.out.println("NewKBCTXML: file_name="+file_name);
	    jnikbct.id= jnikbct.id+1;
	    Data_System ds= new Data_System();
	    XMLParser theParser = new XMLParser();
        Hashtable hsystem= (Hashtable)theParser.getXMLinfo(file_name, "SystemInfo");
        ds.SetName((String)hsystem.get("Name"));
        int NbInputs= (new Integer((String)hsystem.get("Inputs"))).intValue();
        int NbOutputs= (new Integer((String)hsystem.get("Outputs"))).intValue();
        int NbRules= (new Integer((String)hsystem.get("Rules"))).intValue();
        Hashtable hinputs= (Hashtable)theParser.getXMLinfo(file_name, "SystemInputs");
        Hashtable hinputLabels= (Hashtable)theParser.getXMLinfo(file_name, "SystemInputLabels");
        Hashtable houtputs= (Hashtable)theParser.getXMLinfo(file_name, "SystemOutputs");
        Hashtable houtputLabels= (Hashtable)theParser.getXMLinfo(file_name, "SystemOutputLabels");
        int lim= NbInputs+NbOutputs;
        for (int k=0; k<lim; k++) {
            variable v= new variable();

            boolean act= false;
            if (k<NbInputs)
                act= (new Boolean((String)hinputs.get("Input"+String.valueOf(k+1)+"-Active"))).booleanValue();
            else
                act= (new Boolean((String)houtputs.get("Output"+String.valueOf(k-NbInputs+1)+"-Active"))).booleanValue();
            	
            v.SetActive(act);

            String name;
            if (k<NbInputs)
                name= (String)hinputs.get("Input"+String.valueOf(k+1)+"-Name");
            else
                name= (String)houtputs.get("Output"+String.valueOf(k-NbInputs+1)+"-Name");
            	
            v.SetName(name);
        	
            String type;
            if (k<NbInputs)
                type= (String)hinputs.get("Input"+String.valueOf(k+1)+"-Type");
            else
                type= (String)houtputs.get("Output"+String.valueOf(k-NbInputs+1)+"-Type");
            	
            v.SetType(type);

            String trust;
            if (k<NbInputs)
            	trust= (String)hinputs.get("Input"+String.valueOf(k+1)+"-Trust");
            else
            	trust= (String)houtputs.get("Output"+String.valueOf(k-NbInputs+1)+"-Trust");
            	
            v.SetTrust(trust);

            if (k>=NbInputs)
                v.SetClassif((String)houtputs.get("Output"+String.valueOf(k-NbInputs+1)+"-Classif"));

            double lpr, upr;
            if (k<NbInputs) {
                lpr= (new Double((String)hinputs.get("Input"+String.valueOf(k+1)+"-PhysicalLowerRange"))).doubleValue();
                upr= (new Double((String)hinputs.get("Input"+String.valueOf(k+1)+"-PhysicalUpperRange"))).doubleValue();
            } else {
                lpr= (new Double((String)houtputs.get("Output"+String.valueOf(k-NbInputs+1)+"-PhysicalLowerRange"))).doubleValue();
                upr= (new Double((String)houtputs.get("Output"+String.valueOf(k-NbInputs+1)+"-PhysicalUpperRange"))).doubleValue();
            }
            v.SetLowerPhysicalRange(lpr);
            v.SetUpperPhysicalRange(upr);

            double lir, uir;
            if (k<NbInputs) {
                lir= (new Double((String)hinputs.get("Input"+String.valueOf(k+1)+"-InterestLowerRange"))).doubleValue();
                uir= (new Double((String)hinputs.get("Input"+String.valueOf(k+1)+"-InterestUpperRange"))).doubleValue();
            } else {
                lir= (new Double((String)houtputs.get("Output"+String.valueOf(k-NbInputs+1)+"-InterestLowerRange"))).doubleValue();
                uir= (new Double((String)houtputs.get("Output"+String.valueOf(k-NbInputs+1)+"-InterestUpperRange"))).doubleValue();
            }
            v.SetLowerInterestRange(lir);
            v.SetUpperInterestRange(uir);

            int numberOfLabels;
            if (k<NbInputs) {
            	numberOfLabels= (new Integer((String)hinputs.get("Input"+String.valueOf(k+1)+"-Labels"))).intValue();
            } else {
            	numberOfLabels= (new Integer((String)houtputs.get("Output"+String.valueOf(k-NbInputs+1)+"-Labels"))).intValue();
            }
            v.InitLabelsName(numberOfLabels);

            String scaleOfLabels;
            if (k<NbInputs) {
            	scaleOfLabels= (String)hinputs.get("Input"+String.valueOf(k+1)+"-ScaleOfLabels");
            } else {
            	scaleOfLabels= (String)houtputs.get("Output"+String.valueOf(k-NbInputs+1)+"-ScaleOfLabels");
            }
            v.SetScaleName(scaleOfLabels);
        	//System.out.println("var: scaleOfLabels="+scaleOfLabels);

            String flagOntology;
            if (k<NbInputs) {
            	flagOntology= (String)hinputs.get("Input"+String.valueOf(k+1)+"-FlagOntology");
            } else {
            	flagOntology= (String)houtputs.get("Output"+String.valueOf(k-NbInputs+1)+"-FlagOntology");
            }
            v.SetFlagOntology((new Boolean(flagOntology)).booleanValue());
        	//System.out.println("var: flagOntology="+flagOntology);

            String nameOntology;
            if (k<NbInputs) {
            	nameOntology= (String)hinputs.get("Input"+String.valueOf(k+1)+"-NameFromOntology");
            } else {
            	nameOntology= (String)houtputs.get("Output"+String.valueOf(k-NbInputs+1)+"-NameFromOntology");
            }
            v.SetNameFromOntology(nameOntology);
        	//System.out.println("var: nameOntology="+nameOntology);

            String flagOntObjectProperty;
            if (k<NbInputs) {
            	flagOntObjectProperty= (String)hinputs.get("Input"+String.valueOf(k+1)+"-FlagOntObjectProperty");
            } else {
            	flagOntObjectProperty= (String)houtputs.get("Output"+String.valueOf(k-NbInputs+1)+"-FlagOntObjectProperty");
            }
            v.SetFlagOntObjectProperty((new Boolean(flagOntObjectProperty)).booleanValue());
            
            String flagOntDatatypeProperty;
            if (k<NbInputs) {
            	flagOntDatatypeProperty= (String)hinputs.get("Input"+String.valueOf(k+1)+"-FlagOntDatatypeProperty");
            } else {
            	flagOntDatatypeProperty= (String)houtputs.get("Output"+String.valueOf(k-NbInputs+1)+"-FlagOntDatatypeProperty");
            }
            v.SetFlagOntDatatypeProperty((new Boolean(flagOntDatatypeProperty)).booleanValue());

            for (int n=0; n<numberOfLabels; n++) {
                String Label_Name="", Label_Def="", MP="No MP", MPaux="No MP";
                double P1=0, P2=0, P3=0, P4=0;
                if (k<NbInputs) {
                	Label_Name= (String)hinputLabels.get("Input"+String.valueOf(k+1)+"-Label"+String.valueOf(n+1)+"-Name");
                	Label_Def= (String)hinputLabels.get("Input"+String.valueOf(k+1)+"-Label"+String.valueOf(n+1)+"-MF");
                	MP= (String)hinputLabels.get("Input"+String.valueOf(k+1)+"-Label"+String.valueOf(n+1)+"-ModalPoint");
                    String sp= (String)hinputLabels.get("Input"+String.valueOf(k+1)+"-Label"+String.valueOf(n+1)+"-ModalPointAux");
                	if (sp!=null)
                	    MPaux= sp;

                	String p1= (String)hinputLabels.get("Input"+String.valueOf(k+1)+"-Label"+String.valueOf(n+1)+"-P1");
                    if (p1!=null)
                        P1= (new Double(p1)).doubleValue();

                    String p2= (String)hinputLabels.get("Input"+String.valueOf(k+1)+"-Label"+String.valueOf(n+1)+"-P2");
                    if (p2!=null)
                        P2= (new Double(p2)).doubleValue();
                
                    String p3= (String)hinputLabels.get("Input"+String.valueOf(k+1)+"-Label"+String.valueOf(n+1)+"-P3");
                    if (p3!=null)
                        P3= (new Double(p3)).doubleValue();

                    String p4= (String)hinputLabels.get("Input"+String.valueOf(k+1)+"-Label"+String.valueOf(n+1)+"-P4");
                    if (p4!=null)
                        P4= (new Double(p4)).doubleValue();

                } else {
                	Label_Name= (String)houtputLabels.get("Output"+String.valueOf(k-NbInputs+1)+"-Label"+String.valueOf(n+1)+"-Name");
                	Label_Def= (String)houtputLabels.get("Output"+String.valueOf(k-NbInputs+1)+"-Label"+String.valueOf(n+1)+"-MF");
                	MP= (String)houtputLabels.get("Output"+String.valueOf(k-NbInputs+1)+"-Label"+String.valueOf(n+1)+"-ModalPoint");
                    String sp= (String)houtputLabels.get("Output"+String.valueOf(k-NbInputs+1)+"-Label"+String.valueOf(n+1)+"-ModalPointAux");
                	if (sp!=null)
                	    MPaux= sp;

                	String p1= (String)houtputLabels.get("Output"+String.valueOf(k-NbInputs+1)+"-Label"+String.valueOf(n+1)+"-P1");
                    if (p1!=null)
                        P1= (new Double(p1)).doubleValue();

                    String p2= (String)houtputLabels.get("Output"+String.valueOf(k-NbInputs+1)+"-Label"+String.valueOf(n+1)+"-P2");
                    if (p2!=null)
                        P2= (new Double(p2)).doubleValue();
                
                    String p3= (String)houtputLabels.get("Output"+String.valueOf(k-NbInputs+1)+"-Label"+String.valueOf(n+1)+"-P3");
                    if (p3!=null)
                        P3= (new Double(p3)).doubleValue();

                    String p4= (String)houtputLabels.get("Output"+String.valueOf(k-NbInputs+1)+"-Label"+String.valueOf(n+1)+"-P4");
                    if (p4!=null)
                        P4= (new Double(p4)).doubleValue();
                }
                LabelKBCT e= new LabelKBCT();
                if (Label_Def.equals("trapezoidal")) {
                    e= new LabelKBCT(Label_Def, P1, P2, P3, P4, n+1);
                    e.SetMP(MP);
                    e.SetMP(MPaux);
                } else if (Label_Def.equals("SemiTrapezoidalInf") || Label_Def.equals("triangular") || Label_Def.equals("SemiTrapezoidalSup")) {
                    e= new LabelKBCT(Label_Def, P1, P2, P3, n+1);
                    e.SetMP(MP);
                } else if (Label_Def.equals("universal") || Label_Def.equals("gaussian")) {
                    e= new LabelKBCT(Label_Def, P1, P2, n+1);
                    e.SetMP(MP);
                } else if (Label_Def.equals("discrete")) {
                    e= new LabelKBCT(Label_Def, P1, n+1);
                    e.SetMP(MP);
                }
                v.SetLabelsName(n+1, Label_Name);
                if (scaleOfLabels.equals("user"))
                    v.SetUserLabelsName(n+1, Label_Name);

                v.AddLabel(e);
                v.SetLabelsNumber(numberOfLabels);
            }
            v.SetORLabelsName();
            if (k<NbInputs) {
                ds.AddInput(v);
            } else {
                ds.AddOutput(v);
            }
        }
        Hashtable hrules= (Hashtable)theParser.getXMLinfo(file_name, "SystemRules");
        for (int n=0; n<NbRules; n++) {
            int[] in_labels= new int[NbInputs];
            int[] out_labels= new int[NbOutputs];
            for (int m=0; m<NbInputs; m++) {
                String in= (String)hrules.get("Rule"+String.valueOf(n+1)+"-I"+String.valueOf(m+1)); 
            	in_labels[m]= Integer.parseInt(in);
            }
            for (int m=0; m<NbOutputs; m++) {
                String out= (String)hrules.get("Rule"+String.valueOf(n+1)+"-O"+String.valueOf(m+1)); 
                out_labels[m]= Integer.parseInt(out);
            }
            String nature= (String)hrules.get("Rule"+String.valueOf(n+1)+"-Nature");
            boolean active= (new Boolean((String)hrules.get("Rule"+String.valueOf(n+1)+"-Active"))).booleanValue();
            Rule r= new Rule(n+1, NbInputs, NbOutputs, in_labels, out_labels, nature, active);
            ds.AddRule(r);
          }
        ht.put(""+jnikbct.id,ds);
	    return jnikbct.id;
  }
  //----------------------------------------------------------------------------
  /**
   * Generate a new Knowledge Base which is a clone of original KBCT.
   * It is used to detect changes when KBCT is going to be closed.
   */
  public static long NewKBCT( long kbct_ptr ) {
    jnikbct.id= jnikbct.id+1;
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        Data_System ds_copy= new Data_System();
        ds_copy.SetName(ds.GetName());
        for (int n= 0; n < ds.GetNbInputs(); n++ ) {
             variable v= ds.GetInput(n+1);
             variable v_new= new variable(v.GetName(), v.GetType(), v.GetTrust(), v.GetClassif(), v.GetLowerPhysicalRange(), v.GetUpperPhysicalRange(), v.GetLowerInterestRange(), v.GetUpperInterestRange(), v.GetLabelsNumber(), v.GetScaleName(), v.isActive(), v.GetFlagModify());
            //System.out.println("vnamei="+v.GetName());
            //System.out.println("vnameinew="+v_new.GetName());
            //if (v_new.GetName().equals("Measurement time"))
            //System.out.println("v.GetLabelsNumber()="+v.GetLabelsNumber());

            v_new.SetFlagOntology(v.GetFlagOntology());
            v_new.SetNameFromOntology(v.GetNameFromOntology());
            v_new.SetFlagOntObjectProperty(v.GetFlagOntObjectProperty());
            v_new.SetFlagOntDatatypeProperty(v.GetFlagOntDatatypeProperty());
            v_new.InitLabelsName(v.GetLabelsNumber());
            for (int m=0; m < v.GetLabelsNumber(); m++ ) {
                 LabelKBCT e= v.GetLabel(m+1);
                 LabelKBCT e_new= new LabelKBCT();
                 if (e.GetName().equals("trapezoidal")) {
                     e_new= new LabelKBCT(e.GetName(), e.GetP1(), e.GetP2(), e.GetP3(), e.GetP4(), m+1);
                     e_new.SetMP(e.GetMP());
                     e_new.SetMPaux(e.GetMPaux());
                 } else if (e.GetName().equals("SemiTrapezoidalInf") || e.GetName().equals("triangular") || e.GetName().equals("SemiTrapezoidalSup")) {
                     e_new= new LabelKBCT(e.GetName(), e.GetP1(), e.GetP2(), e.GetP3(), m+1);
                     e_new.SetMP(e.GetMP());
                 } else if (e.GetName().equals("universal") || e.GetName().equals("gaussian")) {
                     e_new= new LabelKBCT(e.GetName(), e.GetP1(), e.GetP2(), m+1);
                     e_new.SetMP(e.GetMP());
                 } else if (e.GetName().equals("discrete")) {
                     e_new = new LabelKBCT(e.GetName(), e.GetP1(), m+1);
                     e_new.SetMP(e.GetMP());
                 }
                 //if (v_new.GetName().equals("Measurement time"))
                 //   System.out.println("v.GetFlagModify()="+v.GetFlagModify());
        
                 if (v.GetFlagModify())
                     v_new.SetLabelsName(m+1, v.GetLabelsName()[m]);
                 else if (!v.GetScaleName().equals("user"))
                     v_new.SetLabelsName(m+1, v.GetLabelsName()[m]);
                 else {
                     v_new.SetUserLabelsName(m+1, v.GetUserLabelsName()[m]);
                     v_new.SetLabelsName(m+1, v.GetUserLabelsName()[m]);
             //if (v_new.GetName().equals("Measurement time"))
               //  System.out.println("namei="+v.GetUserLabelsName()[m]);
                 }
                 v_new.AddLabel(e_new);
             }
             v_new.SetFlagModify(v.GetFlagModify());
             if (v.GetFlagModify())
                 v_new.SetORLabelsName(v.GetORLabelsName());
             else {
                 v_new.SetORLabelsName();
        //if (v_new.GetName().equals("Measurement time")) {
        //    System.out.println("set or labels name");
        //    v_new.SetORLabelsName(v.GetORLabelsName());
        //    System.out.println("OR name = "+v_new.GetORLabelsName()[0]);
        //}
             }
             ds_copy.AddInput(v_new);
        }
        for (int n= 0; n < ds.GetNbOutputs(); n++ ) {
             variable v= ds.GetOutput(n+1);
             variable v_new= new variable(v.GetName(), v.GetType(), v.GetTrust(), v.GetClassif(), v.GetLowerPhysicalRange(), v.GetUpperPhysicalRange(), v.GetLowerInterestRange(), v.GetUpperInterestRange(), v.GetLabelsNumber(), v.GetScaleName(), v.isActive(), v.GetFlagModify());
             v_new.SetFlagOntology(v.GetFlagOntology());
             v_new.SetNameFromOntology(v.GetNameFromOntology());
             v_new.SetFlagOntObjectProperty(v.GetFlagOntObjectProperty());
             v_new.SetFlagOntDatatypeProperty(v.GetFlagOntDatatypeProperty());
             v_new.InitLabelsName(v.GetLabelsNumber());
             for (int m=0; m < v.GetLabelsNumber(); m++ ) {
                  LabelKBCT e= v.GetLabel(m+1);
                  LabelKBCT e_new= new LabelKBCT();
                  if (e.GetName().equals("trapezoidal")) {
                      e_new= new LabelKBCT(e.GetName(), e.GetP1(), e.GetP2(), e.GetP3(), e.GetP4(), m+1);
                      e_new.SetMP(e.GetMP());
                      e_new.SetMPaux(e.GetMPaux());
                  } else if (e.GetName().equals("SemiTrapezoidalInf") || e.GetName().equals("triangular") || e.GetName().equals("SemiTrapezoidalSup")) {
                      e_new= new LabelKBCT(e.GetName(), e.GetP1(), e.GetP2(), e.GetP3(), m+1);
                      e_new.SetMP(e.GetMP());
                  } else if (e.GetName().equals("universal") || e.GetName().equals("gaussian")) {
                      e_new= new LabelKBCT(e.GetName(), e.GetP1(), e.GetP2(), m+1);
                      e_new.SetMP(e.GetMP());
                  } else if (e.GetName().equals("discrete")) {
                      e_new= new LabelKBCT(e.GetName(), e.GetP1(), m+1);
                      e_new.SetMP(e.GetMP());
                  }
                  if (v.GetFlagModify())
                      v_new.SetLabelsName(m+1, v.GetLabelsName()[m]);
                  else if (!v.GetScaleName().equals("user"))
                      v_new.SetLabelsName(m+1, v.GetLabelsName()[m]);
                  else
                      v_new.SetUserLabelsName(m+1, v.GetUserLabelsName()[m]);

                  v_new.AddLabel(e_new);
               }
               v_new.SetFlagModify(v.GetFlagModify());
               if (v.GetFlagModify())
                   v_new.SetORLabelsName(v.GetORLabelsName());
               else
                   v_new.SetORLabelsName();

               ds_copy.AddOutput(v_new);
           }
           for (int n= 0; n < ds.GetNbRules(); n++ ) {
                Rule r= ds.GetRule(n+1);
                Rule r_new= new Rule(n+1, r.GetNbInputs(), r.GetNbOutputs(), r.Get_in_labels_number(), r.Get_out_labels_number(), r.GetType(), r.GetActive());
               ds_copy.AddRule(r_new);
           }
           ht.put(""+jnikbct.id,ds_copy);
    } else {
	    //System.out.println("NewKBCT");
	    //System.out.println("kbct_ptr="+kbct_ptr);
	    //System.out.println("DS not stored");
	    return -1;
    }
    return jnikbct.id;
  }
  //----------------------------------------------------------------------------
  /**
   * Delete the KBCT which identifier is "kbct_ptr".
   */
  public static void DeleteKBCT( long kbct_ptr ) {
	  //if (kbct_ptr==25)
	    //  System.out.println("jnikbct.DeleteKBCT -> "+kbct_ptr);
	  ht.remove(""+kbct_ptr);
  }
  //----------------------------------------------------------------------------
  /**
   * Compare two KBCT. If both are equal, it returns true. In other case, return false.
   */
  public static boolean EqualKBCT( long kbct1_ptr, long kbct2_ptr ) {
    //System.out.println("kbct1_ptr="+kbct1_ptr+"   kbct2_ptr="+kbct2_ptr);
    Data_System ds1= (Data_System)ht.get(""+kbct1_ptr);
    Data_System ds2= (Data_System)ht.get(""+kbct2_ptr);
    if ( (ds1==null) || (ds2==null) ) {
        /*System.out.println("ds1 != ds2");
        if (ds1==null)
            System.out.println("ds1 NULL");
        if (ds2==null)
            System.out.println("ds2 NULL");
        */
    	return false;
    }
    if ((!ds1.GetName().equals(ds2.GetName())) ||
        (ds1.GetNbInputs() != ds2.GetNbInputs()) ||
        (ds1.GetNbOutputs() != ds2.GetNbOutputs()) ||
        (ds1.GetNbRules() != ds2.GetNbRules()) ||
        (ds1.GetNbActiveRules() != ds2.GetNbActiveRules())) {
         //System.out.println("NbActiveRules");
         /*System.out.println("Name1="+ds1.GetName());
         System.out.println("Name2="+ds2.GetName());
         System.out.println("NbInputs1="+ds1.GetNbInputs());
         System.out.println("NbInputs2="+ds2.GetNbInputs());
         System.out.println("NbOutputs1="+ds1.GetNbOutputs());
         System.out.println("NbOutputs2="+ds2.GetNbOutputs());
         System.out.println("NbRules1="+ds1.GetNbRules());
         System.out.println("NbRules2="+ds2.GetNbRules());
         System.out.println("NbActiveRules1="+ds1.GetNbActiveRules());
         System.out.println("NbActiveRules2="+ds2.GetNbActiveRules());*/
         return false;
    }
    for (int n=0; n <ds1.GetNbInputs(); n++){
      variable v1= ds1.GetInput(n+1);
      variable v2= ds2.GetInput(n+1);
      boolean r= EqualVar(v1,v2);
      if (!r) {
        //System.out.println("input variable");
        return r;
      }
    }
    for (int n=0; n <ds1.GetNbOutputs(); n++){
      variable v1= ds1.GetOutput(n+1);
      variable v2= ds2.GetOutput(n+1);
      boolean r= EqualVar(v1,v2);
      if (!r) {
          //System.out.println("output variable");
          return r;
      }
    }
    for (int n=0; n<ds1.GetNbRules(); n++) {
      Rule r1= ds1.GetRule(n+1);
      Rule r2= ds2.GetRule(n+1);
      if ((r1.GetNbInputs() != r2.GetNbInputs()) ||
          (r1.GetNbOutputs() != r2.GetNbOutputs()) ) {
           //System.out.println("rules: nb variables");
           return false;
      }
      int[] in_labels1= r1.Get_in_labels_number();
      int[] out_labels1= r1.Get_out_labels_number();
      int[] in_labels2= r2.Get_in_labels_number();
      int[] out_labels2= r2.Get_out_labels_number();
      int NbInputs= r1.GetNbInputs();
      for (int m=0; m<r1.GetNbInputs()+r1.GetNbOutputs(); m++)
          if (((m<NbInputs) && (in_labels1[m] != in_labels2[m])) ||
              ((m>=NbInputs) && (out_labels1[m-NbInputs] != out_labels2[m-NbInputs])) ) {
               //System.out.println("rules: terms");
               return false;
          }
      String type1= r1.GetType();
      String type2= r2.GetType();
      if (!type1.equals(type2)) {
        //System.out.println("rules: type");
        return false;
      }
      boolean active1= r1.GetActive();
      boolean active2= r2.GetActive();
      if (active1 != active2) {
        //System.out.println("rules: active");
        return false;
      }
    }
    return true;
  }
  //----------------------------------------------------------------------------
  /**
   * Compare two variables. If both are equal, it returns true. In other case, return false.
   */
  public static boolean EqualVar( variable v1, variable v2 ) {
    if ((!v1.GetName().equals(v2.GetName())) ||
        (!v1.GetType().equals(v2.GetType())) ||
        (!v1.GetTrust().equals(v2.GetTrust())) ||
        (!v1.GetClassif().equals(v2.GetClassif())) ||
        (v1.GetLowerPhysicalRange() != v2.GetLowerPhysicalRange()) ||
        (v1.GetUpperPhysicalRange() != v2.GetUpperPhysicalRange()) ||
        (v1.GetLowerInterestRange() != v2.GetLowerInterestRange()) ||
        (v1.GetLowerInterestRange() != v2.GetLowerInterestRange()) ||
        (v1.GetLabelsNumber() != v2.GetLabelsNumber()) ||
        (v1.GetFlagOntology() != v2.GetFlagOntology()) ||
        (v1.GetFlagOntDatatypeProperty() != v2.GetFlagOntDatatypeProperty()) ||
        (v1.GetFlagOntObjectProperty() != v2.GetFlagOntObjectProperty())) {
        //System.out.println("ev1");
    	return false;
    }
    for (int m= 0; m < v1.GetLabelsNumber(); m++) {
         LabelKBCT e1= v1.GetLabel(m+1);
         LabelKBCT e2= v2.GetLabel(m+1);
         if ((!e1.GetName().equals(e2.GetName())) ||
             (!e1.GetMP().equals(e2.GetMP()))) {
              //System.out.println("ev2");
              return false;
         }
         if ((e1.GetName().equals("trapezoidal")) &&
             ((e1.GetP1() != e2.GetP1()) ||
              (e1.GetP2() != e2.GetP2()) ||
              (e1.GetP3() != e2.GetP3()) ||
              (e1.GetP4() != e2.GetP4()) || 
              (!e1.GetMPaux().equals(e2.GetMPaux()))) ) {
              //System.out.println("ev3");
              //System.out.println("ev3: P1: "+e1.GetP1()+"  "+e2.GetP1());
              //System.out.println("ev3: P2: "+e1.GetP2()+"  "+e2.GetP2());
              //System.out.println("ev3: P3: "+e1.GetP3()+"  "+e2.GetP3());
              //System.out.println("ev3: P4: "+e1.GetP4()+"  "+e2.GetP4());
              //System.out.println("ev3: P4: "+e1.GetMPaux()+"  "+e2.GetMPaux());
              return false;
         }
         if ((e1.GetName().equals("SemiTrapezoidalInf") || e1.GetName().equals("triangular") || e1.GetName().equals("SemiTrapezoidalSup")) &&
             ((e1.GetP1() != e2.GetP1()) ||
              (e1.GetP2() != e2.GetP2()) ||
              (e1.GetP3() != e2.GetP3())) ) {
              //System.out.println("ev4");
              return false;
         }
    }
    return true;
  }
  //----------------------------------------------------------------------------
  /**
   * Store the KBCT which identifier is "kbct_ptr" in file "file_name".
   */
  public static void SaveKBCT( long kbct_ptr, String file_name ) throws Throwable {
    //System.out.println("saveKBCT: "+file_name);
	if (!file_name.endsWith("xml"))
		file_name= file_name + ".xml";

	SaveKBCTxml(kbct_ptr, file_name);
  }
  //----------------------------------------------------------------------------
  /**
   * Store the KBCT which identifier is "kbct_ptr" in file "file_name".
   */
  public static void SaveKBCTxml( long kbct_ptr, String file_name ) throws Throwable {
    //System.out.println("saveKBCT: "+file_name+"  ptr="+kbct_ptr);
    int Ninputs = jnikbct.GetNbInputs(kbct_ptr);
    int Noutputs = jnikbct.GetNbOutputs(kbct_ptr);
    int Nrules = jnikbct.GetNbRules(kbct_ptr);
    XMLWriter.createKBFile(file_name);
    String Name= jnikbct.GetName(kbct_ptr);
    XMLWriter.writeKBFileSystem(Name,Ninputs,Noutputs,Nrules);
    DecimalFormat df= new DecimalFormat();
    df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
    dfs.setDecimalSeparator((new String(".").charAt(0)));
    df.setDecimalFormatSymbols(dfs);
    df.setGroupingSize(20);
    XMLWriter.createKBFileEntity("Inputs");
    for (int n = 0; n < Ninputs; n++) {
      int N_variable= n+1;
      variable v= jnikbct.GetInput(kbct_ptr, N_variable);
      boolean Active= v.isActive();
      String variableName=v.GetName();
      String variableType=v.GetType();
      String variableTrust=v.GetTrust();
      String physicalLowerRange=df.format(v.GetLowerPhysicalRange());
      String physicalUpperRange=df.format(v.GetUpperPhysicalRange());
      String interestLowerRange=df.format(v.GetLowerInterestRange());
      String interestUpperRange=df.format(v.GetUpperInterestRange());
      int numberOfLabels= v.GetLabelsNumber();
      String scaleOfLabels= v.GetScaleName();
      boolean flont= v.GetFlagOntology();
      boolean flontdatprop= v.GetFlagOntDatatypeProperty();
      boolean flontobjprop= v.GetFlagOntObjectProperty();
      //System.out.println("jnikbct: SaveKBCT: flagOntology="+flont);
      String nameont= v.GetNameFromOntology();
      XMLWriter.writeKBFileVariable("Input"+N_variable, Active, variableName, variableType, variableTrust, "input", physicalLowerRange, physicalUpperRange, interestLowerRange, interestUpperRange, numberOfLabels, scaleOfLabels, flont, nameont, flontdatprop, flontobjprop);
      XMLWriter.createKBFileEntity("Labels");
      for (int m= 0; m < numberOfLabels; m++) {
         int N_label= m+1;
         LabelKBCT e= v.GetLabel(N_label);
         String labelName;
         if (!scaleOfLabels.equals("user"))
             labelName=v.GetLabelsName()[m];
         else 
             labelName=v.GetUserLabelsName()[m];

         String labelMF=e.GetName();
         String modalPoint=e.GetMP();
         String mpAux= null;
         double[] dparams= e.GetParams();
         String[] params= new String[dparams.length];
         for (int k=0; k<params.length; k++) {
        	 params[k]=df.format(dparams[k]);
         }
         
         if (labelMF.equals("trapezoidal")) {
        	 mpAux= e.GetMPaux();
        	 //System.out.println("mp="+modalPoint);
        	 //System.out.println("mpAux="+mpAux);
         } 

         XMLWriter.writeKBFileLabel("Input"+N_variable+"-Label"+N_label, labelName, labelMF, params, modalPoint, mpAux);
      }
      // XML writer
      XMLWriter.closeKBFile(false);
    }
    XMLWriter.closeKBFile(false);
    XMLWriter.createKBFileEntity("Outputs");
    for (int n = 0; n < Noutputs; n++) {
      int N_variable= n+1;
      variable v= jnikbct.GetOutput(kbct_ptr, N_variable);
      boolean Active= v.isActive();
      String variableName=v.GetName();
      String variableType=v.GetType();
      String variableTrust=v.GetTrust();
      String variableClassif=v.GetClassif();
      String physicalLowerRange=df.format(v.GetLowerPhysicalRange());
      String physicalUpperRange=df.format(v.GetUpperPhysicalRange());
      String interestLowerRange=df.format(v.GetLowerInterestRange());
      String interestUpperRange=df.format(v.GetUpperInterestRange());
      int numberOfLabels= v.GetLabelsNumber();
      String scaleOfLabels= v.GetScaleName();
      boolean flont= v.GetFlagOntology();
      boolean flontdatprop= v.GetFlagOntDatatypeProperty();
      boolean flontobjprop= v.GetFlagOntObjectProperty();
      String nameont= v.GetNameFromOntology();
      XMLWriter.writeKBFileVariable("Output"+N_variable, Active, variableName, variableType, variableTrust, variableClassif, physicalLowerRange, physicalUpperRange, interestLowerRange, interestUpperRange, numberOfLabels, scaleOfLabels, flont, nameont, flontdatprop, flontobjprop);
      XMLWriter.createKBFileEntity("Labels");
      for (int m= 0; m < numberOfLabels; m++) {
          int N_label= m+1;
          LabelKBCT e= v.GetLabel(N_label);
          String labelName;
          if (!scaleOfLabels.equals("user"))
              labelName=v.GetLabelsName()[m];
          else 
              labelName=v.GetUserLabelsName()[m];

          String labelMF=e.GetName();
          String modalPoint=e.GetMP();
          String mpAux= null;
          double[] dparams= e.GetParams();
          String[] params= new String[dparams.length];
          for (int k=0; k<params.length; k++) {
         	 params[k]=df.format(dparams[k]);
          }
          
          if (labelMF.equals("trapezoidal")) {
         	  mpAux= e.GetMPaux();
          } 

          XMLWriter.writeKBFileLabel("Output"+N_variable+"-Label"+N_label, labelName, labelMF, params, modalPoint, mpAux);
       }
      XMLWriter.closeKBFile(false);
    }
    XMLWriter.closeKBFile(false);
    XMLWriter.createKBFileEntity("Rules");
    for (int n = 0; n < Nrules; n++) {
      Rule r= jnikbct.GetRule(kbct_ptr, n+1);
      int[] in_labels_number= r.Get_in_labels_number();
      int[] out_labels_number= r.Get_out_labels_number();
      String ruleNature=r.GetType();
      boolean ruleActive=r.GetActive();
      XMLWriter.writeKBFileRule("Rule"+String.valueOf(n+1), in_labels_number, out_labels_number, ruleNature, ruleActive);
    }
    XMLWriter.closeKBFile(false);
    XMLWriter.closeKBFile(true);
  }
  //----------------------------------------------------------------------------
  /**
   * Save the KB, with identifier "kbct_ptr" from file "file_name_GUAJE", 
   * as a FIS configuration file.
   */
  public static JKBCT SaveFIS( String file_name_GUAJE, String file_name_FIS ) throws Throwable {
    return JConvert.GuajeToFis(file_name_GUAJE, file_name_FIS, false);
  }
  //----------------------------------------------------------------------------
  /**
   * Save the KB, with identifier "kbct_ptr" from file "file_name_GUAJE", 
   * as a FIS configuration file.
   */
  public static JKBCT SaveFISquality( String file_name_GUAJE, String file_name_FIS ) throws Throwable {
    return JConvert.GuajeToFis(file_name_GUAJE, file_name_FIS, true);
  }
  //----------------------------------------------------------------------------
  /**
   * Set the name "name" to the KBCT which identifier is "kbct_ptr".
   */
  public static void SetName( long kbct_ptr, String name ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds!=null) {
        ds.SetName(name);
        ht.put(""+kbct_ptr,ds);
    } else {
	    System.out.println("jnikbct.SetName");
		System.out.println("kbct_ptr="+kbct_ptr);
		System.out.println("DS not stored");
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Return the name of the KBCT which identifier is "kbct_ptr".
   */
  public static String GetName( long kbct_ptr ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
	if (ds!=null)  
        return ds.GetName();
	else {
	    System.out.println("jnikbct.GetName");
		System.out.println("kbct_ptr="+kbct_ptr);
		System.out.println("DS not stored");
        return "";
	}
  }
  //----------------------------------------------------------------------------
  /**
   * Return number of inputs of the KBCT which identifier is "kbct_ptr".
   */
  public static int GetNbInputs( long kbct_ptr ) {
	Data_System ds= (Data_System)ht.get(""+kbct_ptr);
	if (ds!=null)  
        return ds.GetNbInputs();
	else {
	    System.out.println("jnikbct.GetNbInputs");
		System.out.println("kbct_ptr="+kbct_ptr);
		System.out.println("DS not stored");
        return 0;
	}
  }
  //----------------------------------------------------------------------------
  /**
   * Return number of active inputs of the KBCT which identifier is "kbct_ptr".
   */
  public static int GetNbActiveInputs( long kbct_ptr ) {
	Data_System ds= (Data_System)ht.get(""+kbct_ptr);
	if (ds!=null)  
        return ds.GetNbActiveInputs();
	else {
	    System.out.println("jnikbct.GetNbActiveInputs");
		System.out.println("kbct_ptr="+kbct_ptr);
		System.out.println("DS not stored");
        return 0;
	}
  }
  //----------------------------------------------------------------------------
  /**
   * Return number of outputs of the KBCT which identifier is "kbct_ptr".
   */
  public static int GetNbOutputs( long kbct_ptr ) {
	Data_System ds= (Data_System)ht.get(""+kbct_ptr);
	if (ds!=null)  
        return ds.GetNbOutputs();
	else {
	    System.out.println("jnikbct.GetNbOutputs");
		System.out.println("kbct_ptr="+kbct_ptr);
		System.out.println("DS not stored");
        return 0;
	}
  }
  //----------------------------------------------------------------------------
  /**
   * Return linguistic description of rule "rule_number" in the KBCT which identifier is "kbct_ptr".
   */
  public static String GetRuleDescription( long kbct_ptr, int rule_number, String FV ) {
	  // if == fingramViewer then outLabel without GetString()
	  String ruleDescription="";
	  Data_System ds= (Data_System)ht.get(""+kbct_ptr);
	  Rule r= ds.GetRule(rule_number);
	  boolean flag = false;
	  int[] in_labels_number = r.Get_in_labels_number();
	  int[] out_labels_number = r.Get_out_labels_number();
	  for (int m = 0; m < r.GetNbInputs(); m++) {
		  if (in_labels_number[m] > 0) {
			  variable jin = ds.GetInput(m + 1);
			  String[] labNames = null;
			  if (jin.GetScaleName().equals("user")) {
				  labNames = jin.GetUserLabelsName();
			  } else {
				  labNames = jin.GetLabelsName();
			  }
			  int NL = jin.GetLabelsNumber();
			  String lab;
			  if (in_labels_number[m] <= NL) {
				  if (jin.GetScaleName().equals("user"))
					  lab = labNames[in_labels_number[m] - 1];
				  else
					  lab = LocaleKBCT.GetString(labNames[in_labels_number[m] - 1]);
			  } else if ((in_labels_number[m] > NL)
					  && (in_labels_number[m] <= 2 * NL)) {
				  if (jin.GetScaleName().equals("user"))
					  lab = LocaleKBCT.GetString("NOT")
					  + "("
					  + labNames[in_labels_number[m] - 1 - NL] 
							  + ")";
				  else
					  lab = LocaleKBCT.GetString("NOT")
					  + "("
					  + LocaleKBCT.GetString(labNames[in_labels_number[m] - 1 - NL]) 
					  + ")";
			  } else {
				  String[] LabORNames = jin.GetORLabelsName();
				  lab = LabORNames[in_labels_number[m] - 1 - 2*NL];
				  lab = lab.replace("(", "");
				  lab = lab.replace(")", "");
			  }
			  if (!flag) {
				  ruleDescription= ruleDescription + " "
						  + LocaleKBCT.GetString("IF") + " "
						  + jin.GetName() + " "
						  + LocaleKBCT.GetString("IS") + " "
						  + lab;
				  flag = true;
			  } else {
				  ruleDescription= ruleDescription + " "
						  + LocaleKBCT.GetString("AND") + " "
						  + jin.GetName() + " "
						  + LocaleKBCT.GetString("IS") + " "
						  + lab;
			  }
		  }
	  }
	  ruleDescription= ruleDescription + " " + LocaleKBCT.GetString("THEN") + " ";
	  for (int m = 0; m < r.GetNbOutputs(); m++) {
		  if (out_labels_number[m] > 0) {
			  variable jout = ds.GetOutput(m + 1);
			  String[] labNames = null;
			  if (jout.GetScaleName().equals("user")) {
				  labNames = jout.GetUserLabelsName();
			  } else {
				  labNames = jout.GetLabelsName();
			  }
			  String lab = labNames[out_labels_number[m] - 1];
			  if ( (!jout.GetScaleName().equals("user")) && (!FV.equals("fingramViewer")) )
				  lab = LocaleKBCT.GetString(lab);

			  if (m == 0)
				  ruleDescription= ruleDescription + jout.GetName() + " "
						  + LocaleKBCT.GetString("IS") + " "
						  + lab;
			  else
				  ruleDescription= ruleDescription + " "
						  + LocaleKBCT.GetString("AND") + " "
						  + jout.GetName() + " "
						  + LocaleKBCT.GetString("IS") + " "
						  + lab;
		  }
	  }
	  return ruleDescription;
  }
  //----------------------------------------------------------------------------
  /**
   * Return input "input_number" of the KBCT which identifier is "kbct_ptr".
   */
  public static variable GetInput( long kbct_ptr, int input_number ) {
	Data_System ds= (Data_System)ht.get(""+kbct_ptr);
	if (ds!=null)  
        return ds.GetInput(input_number);
	else {
	    System.out.println("jnikbct.GetInput");
		System.out.println("kbct_ptr="+kbct_ptr);
		System.out.println("DS not stored");
        return null;
	}
  }
  //----------------------------------------------------------------------------
  /**
   * Return output "output_number" of the KBCT which identifier is "kbct_ptr".
   */
  public static variable GetOutput( long kbct_ptr, int output_number ) {
	Data_System ds= (Data_System)ht.get(""+kbct_ptr);
	if (ds!=null)  
        return ds.GetOutput(output_number);
	else {
	    System.out.println("jnikbct.GetOutput");
		System.out.println("kbct_ptr="+kbct_ptr);
		System.out.println("DS not stored");
        return null;
	}
  }
  //----------------------------------------------------------------------------
  /**
   * Add input "v" to the KBCT which identifier is "kbct_ptr".
   */
  public static void AddInput( long kbct_ptr, variable v ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        ds.AddInput(v);
        ht.put(""+kbct_ptr,ds);
    } else {
	    System.out.println("jnikbct.AddInput");
		System.out.println("kbct_ptr="+kbct_ptr);
		System.out.println("DS not stored");
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Delete input "input_number" of the KBCT which identifier is "kbct_ptr".
   */
  public static void RemoveInput( long kbct_ptr, int input_number ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        ds.RemoveInput(input_number);
        ht.put(""+kbct_ptr,ds);
    } else {
	    System.out.println("jnikbct.RemoveInput");
		System.out.println("kbct_ptr="+kbct_ptr);
		System.out.println("DS not stored");
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Replace input "input_number" of the KBCT which identifier is "kbct_ptr" by the new input "v".
   */
  public static void ReplaceInput( long kbct_ptr, int input_number, variable v ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        ds.ReplaceInput(input_number, v);
        ht.put(""+kbct_ptr,ds);
    } else {
	    System.out.println("jnikbct.ReplaceInput");
		System.out.println("kbct_ptr="+kbct_ptr);
		System.out.println("DS not stored");
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Return a list 11101101 where 1 means input used in the rule base.
   */
  public static int[] GetInputsUsedInRuleBase( long kbct_ptr ) {
	Data_System ds= (Data_System)ht.get(""+kbct_ptr);
	  if (ds != null) {
	  int NbInputs= ds.GetNbInputs();  
	  int[] result= new int[NbInputs];
	  for (int n=0; n<NbInputs; n++) {
		   result[n]=0;
	  }
	  int NbRules= ds.GetNbRules();
	  int cont=0;
      for (int n=0; n<NbRules; n++) {
	     //System.out.println("n="+n);
		 Rule r= ds.GetRule(n+1);
		 if (r.GetActive()) {
			 int[] premises= r.Get_in_labels_number();
			 for (int m=0; m<NbInputs; m++) {
				  if (premises[m] > 0) {
					  if (result[m]==0) {
						  result[m]=1;
						  cont++;
						  if (cont==NbInputs) {
							  //System.out.println(" --> STOP");
							  m= NbInputs;
							  n= NbRules;
						  }
					  }
				  }
			 }
		 }
	  }
	  //for (int n=0; n<NbInputs; n++) {
		// System.out.println("res["+n+"]="+result[n]);
  	  //}
      return result;
	} else {
	    System.out.println("jnikbct.GetInputsUsedInRuleBase");
		System.out.println("kbct_ptr="+kbct_ptr);
		System.out.println("DS not stored");
        return null;
	}
  }
  //----------------------------------------------------------------------------
  /**
   * Replace output "output_number" of the KBCT which identifier is "kbct_ptr" by the new output "v".
   */
  public static void ReplaceOutput( long kbct_ptr, int output_number, variable v ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        ds.ReplaceOutput(output_number, v);
        ht.put(""+kbct_ptr,ds);
    } else {
	    System.out.println("jnikbct.ReplaceOutput");
		System.out.println("kbct_ptr="+kbct_ptr);
		System.out.println("DS not stored");
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Add output "v" to the KBCT which identifier is "kbct_ptr".
   */
  public static void AddOutput( long kbct_ptr, variable v ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        ds.AddOutput(v);
        ht.put(""+kbct_ptr,ds);
    } else {
	    System.out.println("jnikbct.AddOutput");
		System.out.println("kbct_ptr="+kbct_ptr);
		System.out.println("DS not stored");
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Delete output "output_number" of the KBCT which identifier is "kbct_ptr".
   */
  public static void RemoveOutput( long kbct_ptr, int output_number ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        ds.RemoveOutput(output_number);
        ht.put(""+kbct_ptr,ds);
    } else {
	    System.out.println("jnikbct.RemoveOutput");
		System.out.println("kbct_ptr="+kbct_ptr);
		System.out.println("DS not stored");
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Return a vector with 1 (rule to be expanded) and 0 (rule to keep the same).
   */
  public static int[] GetRulesToBeExpanded( long kbct_ptr ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null)
        return ds.GetRulesToBeExpanded();
    else {
	    System.out.println("jnikbct.GetRulesToBeExpanded");
		System.out.println("kbct_ptr="+kbct_ptr);
		System.out.println("DS not stored");
        return null;    	
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Return number of rules of the KBCT which identifier is "kbct_ptr".
   */
  public static int GetNbRules( long kbct_ptr ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null)
        return ds.GetNbRules();
    else {
	    System.out.println("jnikbct.GetNbRules");
		System.out.println("kbct_ptr="+kbct_ptr);
		System.out.println("DS not stored");
        return 0;    	
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Return number of active rules of the KBCT which identifier is "kbct_ptr".
   */
  public static int GetNbActiveRules( long kbct_ptr ) {
	  Data_System ds= (Data_System)ht.get(""+kbct_ptr);
	  if (ds!=null)
          return ds.GetNbActiveRules();
	  else {
		  System.out.println("jnikbct.GetNbActiveRules");
		  System.out.println("kbct_ptr="+kbct_ptr);
		  System.out.println("DS not stored");
		  return 0;
	  }
  }
  //----------------------------------------------------------------------------
  /**
   * Return Rule "rule_number" of the KBCT which identifier is "kbct_ptr".
   */
  public static Rule GetRule( long kbct_ptr, int rule_number ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds!=null)
        return ds.GetRule(rule_number);
    else {
		  System.out.println("jnikbct.GetRule");
		  System.out.println("kbct_ptr="+kbct_ptr);
		  System.out.println("DS not stored");
		  return null;
    }
  }
  //----------------------------------------------------------------------------
  public static int GetRulePosition( long kbct_ptr, Rule r ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        int NbRules= ds.GetNbRules();
        for (int n=0; n<NbRules; n++) {
             Rule raux= ds.GetRule(n+1);
             if ( (r.GetType() == raux.GetType()) &&
                  (JConsistency.SamePremise(r, raux)) &&
                  (!JConsistency.DifferentConclusions(r, raux)) )
                   return n+1;
        }
        return -1;
    } else {
		  System.out.println("jnikbct.GetRulePosition");
		  System.out.println("kbct_ptr="+kbct_ptr);
		  System.out.println("DS not stored");
		  return -1;
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Add Rule "r" to the KBCT which identifier is "kbct_ptr".
   */
  public static void AddRule( long kbct_ptr, Rule r ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        ds.AddRule(r);
        ht.put(""+kbct_ptr,ds);
    } else {
	    System.out.println("jnikbct.AddRule");
	    System.out.println("kbct_ptr="+kbct_ptr);
	    System.out.println("DS not stored");
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Replace Rule "rule_number" of the KBCT which identifier is "kbct_ptr" by the new Rule "r".
   */
  public static void ReplaceRule( long kbct_ptr, int rule_number, Rule r ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        ds.ReplaceRule(rule_number, r);
        ht.put(""+kbct_ptr,ds);
    } else {
	    System.out.println("jnikbct.ReplaceRule");
	    System.out.println("kbct_ptr="+kbct_ptr);
	    System.out.println("DS not stored");
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Delete Rule "rule_number" of the KBCT which identifier is "kbct_ptr".
   */
  public static void RemoveRule( long kbct_ptr, int rule_number ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        ds.RemoveRule(rule_number);
        ht.put(""+kbct_ptr,ds);
    } else {
	    System.out.println("jnikbct.RemoveRule");
	    System.out.println("kbct_ptr="+kbct_ptr);
	    System.out.println("DS not stored");
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Delete label "mf_number" from input "input_number" of the KBCT which identifier is "kbct_ptr".
   */
  public static void RemoveMFInInput( long kbct_ptr, int input_number, int mf_number ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        ds.GetInput(input_number).RemoveLabel(mf_number, true);
        ht.put(""+kbct_ptr,ds);
    } else {
	    System.out.println("jnikbct.RemoveMFInInput");
	    System.out.println("kbct_ptr="+kbct_ptr);
	    System.out.println("DS not stored");
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Delete label "mf_number" from output "output_number" of the KBCT which identifier is "kbct_ptr".
   */
  public static void RemoveMFInOutput( long kbct_ptr, int output_number, int mf_number ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        ds.GetOutput(output_number).RemoveLabel(mf_number, true);
        ht.put(""+kbct_ptr,ds);
    } else {
	    System.out.println("jnikbct.RemoveMFInOutput");
	    System.out.println("kbct_ptr="+kbct_ptr);
	    System.out.println("DS not stored");
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Return name of input "input_ptr" of the KBCT which identifier is "kbct_ptr".
   */
  public static String GetInputName( long kbct_ptr, long input_ptr ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        variable v= (variable)ds.GetInput(input_ptr);
        return v.GetName();
    } else {
	    System.out.println("jnikbct.GetInputName");
	    System.out.println("kbct_ptr="+kbct_ptr);
	    System.out.println("DS not stored");
        return "";    	
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Return input physical range of input "input_ptr" of the KBCT which identifier is "kbct_ptr".
   */
  public static double[] GetInputPhysicalRange( long kbct_ptr, long input_ptr ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        variable v= (variable)ds.GetInput(input_ptr);
        return v.GetInputPhysicalRange();
    } else {
	    System.out.println("jnikbct.GetInputPhysicalRange");
	    System.out.println("kbct_ptr="+kbct_ptr);
	    System.out.println("DS not stored");
	    return null;
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Return input interest range of input "input_ptr" of the KBCT which identifier is "kbct_ptr".
   */
  public static double[] GetInputInterestRange( long kbct_ptr, long input_ptr ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        variable v= (variable)ds.GetInput(input_ptr);
        return v.GetInputInterestRange();
    } else {
	    System.out.println("jnikbct.GetInputInterestRange");
	    System.out.println("kbct_ptr="+kbct_ptr);
	    System.out.println("DS not stored");
	    return null;
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Return true (all KBCT inputs are active).
   */
  public static boolean GetInputActive( long input_ptr ) {return true;}
  //----------------------------------------------------------------------------
  /**
   * Set name "name" to input "input_ptr" of the KBCT which identifier is "kbct_ptr".
   */
  public static void SetInputName( long kbct_ptr, long input_ptr, String name ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        variable v= (variable)ds.GetInput(input_ptr);
        v.SetName(name);
        ds.ReplaceInput(input_ptr,v);
        ht.put(""+kbct_ptr, ds);
    } else {
	    System.out.println("jnikbct.SetInputName");
	    System.out.println("kbct_ptr="+kbct_ptr);
	    System.out.println("DS not stored");
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Set input physical range of input "input_ptr" of the KBCT which identifier is "kbct_ptr".
   */
  public static void SetInputPhysicalRange( long kbct_ptr, long input_ptr, double domain_inf, double domain_sup ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        variable v= (variable)ds.GetInput(input_ptr);
        v.SetInputPhysicalRange(domain_inf, domain_sup);
        ds.ReplaceInput(input_ptr,v);
        ht.put(""+kbct_ptr, ds);
    } else {
	    System.out.println("jnikbct.SetInputPhysicalRange");
	    System.out.println("kbct_ptr="+kbct_ptr);
	    System.out.println("DS not stored");
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Set input interest range of input "input_ptr" of the KBCT which identifier is "kbct_ptr".
   */
  public static void SetInputInterestRange( long kbct_ptr, long input_ptr, double domain_inf, double domain_sup ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        variable v= (variable)ds.GetInput(input_ptr);
        v.SetInputInterestRange(domain_inf, domain_sup);
        ds.ReplaceInput(input_ptr,v);
        ht.put(""+kbct_ptr, ds);
    } else {
	    System.out.println("jnikbct.SetInputInterestRange");
	    System.out.println("kbct_ptr="+kbct_ptr);
	    System.out.println("DS not stored");
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Return label "mf_number" of input "input_ptr" of the KBCT which identifier is "kbct_ptr".
   */
  public static LabelKBCT GetInputMF( long kbct_ptr, long input_ptr, int mf_number ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        variable v= (variable)ds.GetInput(input_ptr);
        return v.GetLabel(mf_number);
    } else {
	    System.out.println("jnikbct.GetInputMF");
	    System.out.println("kbct_ptr="+kbct_ptr);
	    System.out.println("DS not stored");
        return null;
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Add label "e" to input "input_ptr" of the KBCT which identifier is "kbct_ptr".
   */
  public static void AddInputMF( long kbct_ptr, long input_ptr, LabelKBCT e ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        variable v= (variable)ds.GetInput(input_ptr);
        v.AddLabel(e);
        ds.ReplaceInput(input_ptr, v);
    } else {
	    System.out.println("jnikbct.AddInputMF");
	    System.out.println("kbct_ptr="+kbct_ptr);
	    System.out.println("DS not stored");
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Return true (all KBCT outputs are active).
   */
  public static boolean GetOutputActive( long input_ptr ) {return true;}
  //----------------------------------------------------------------------------
  /**
   * Set "Active" rules rule_number.
   */
  public static void SetRuleActive( long kbct_ptr, int rule_number, boolean active ) {
    Data_System ds= (Data_System)ht.get(""+kbct_ptr);
    if (ds != null) {
        ds.SetRuleActive(rule_number, active);
        ht.put(""+kbct_ptr, ds);
    } else {
	    System.out.println("jnikbct.SetRuleActive");
	    System.out.println("kbct_ptr="+kbct_ptr);
	    System.out.println("DS not stored");
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Return factorial of number "n".
   */
  public static int factorial( int n ) {
    if (n==1)
      return n;
    else
      return n*factorial(n-1);
  }
  //----------------------------------------------------------------------------
  /**
   * Return serie for number "n".
   */
  public static int serie( int n ) {
    if (n<=1)
      return n;
    else
      return n+serie(n-1);
  }
  //----------------------------------------------------------------------------
  /**
   * Return number of basic labels in composite OR label "SelLabel".
   */
  public static int NbORLabels ( int SelLabel, int NbLabels) {
    if (SelLabel < NbLabels)
        return 2;
    else if ( (SelLabel >= NbLabels) && (SelLabel <= 2*NbLabels-3) )
      return 3;
    else if ( (SelLabel > 2*NbLabels-3) && (SelLabel <= 3*NbLabels-6) )
      return 4;
    else if ( (SelLabel > 3*NbLabels-6) && (SelLabel <= 4*NbLabels-10) )
      return 5;
    else if ( (SelLabel > 4*NbLabels-10) && (SelLabel <= 5*NbLabels-15) )
      return 6;
    else if ( (SelLabel > 5*NbLabels-15) && (SelLabel <= 6*NbLabels-21) )
      return 7;
    else if ( (SelLabel > 6*NbLabels-21) && (SelLabel <= 7*NbLabels-28) )
        return 8;
    else if ( (SelLabel > 7*NbLabels-28) && (SelLabel <= 8*NbLabels-36) )
        return 9;
    else if ( (SelLabel > 8*NbLabels-36) && (SelLabel <= 9*NbLabels-45) )
        return 10;
    else if ( (SelLabel > 9*NbLabels-45) && (SelLabel <= 10*NbLabels-55) )
        return 11;
    else if ( (SelLabel > 10*NbLabels-55) && (SelLabel <= 11*NbLabels-66) )
        return 12;
    else if ( (SelLabel > 11*NbLabels-66) && (SelLabel <= 12*NbLabels-78) )
        return 13;
    else if ( (SelLabel > 12*NbLabels-78) && (SelLabel <= 13*NbLabels-91) )
        return 14;
    else if ( (SelLabel > 13*NbLabels-91) && (SelLabel <= 14*NbLabels-105) )
        return 15;
    else if ( (SelLabel > 14*NbLabels-105) && (SelLabel <= 15*NbLabels-120) )
        return 16;
    else if ( (SelLabel > 15*NbLabels-120) && (SelLabel <= 16*NbLabels-136) )
        return 17;
    else if ( (SelLabel > 16*NbLabels-136) && (SelLabel <= 17*NbLabels-153) )
        return 18;
    else if ( (SelLabel > 17*NbLabels-153) && (SelLabel <= 18*NbLabels-171) )
        return 19;
    else if ( (SelLabel > 18*NbLabels-171) && (SelLabel <= 19*NbLabels-190) )
        return 20;
    else if ( (SelLabel > 19*NbLabels-190) && (SelLabel <= 20*NbLabels-210) )
        return 21;
    else if ( (SelLabel > 20*NbLabels-210) && (SelLabel <= 21*NbLabels-231) )
        return 22;
    else if ( (SelLabel > 21*NbLabels-231) && (SelLabel <= 22*NbLabels-253) )
        return 23;
    else if ( (SelLabel > 22*NbLabels-253) && (SelLabel <= 23*NbLabels-276) )
        return 24;
    else if ( (SelLabel > 23*NbLabels-276) && (SelLabel <= 24*NbLabels-300) )
        return 25;
    else if ( (SelLabel > 24*NbLabels-300) && (SelLabel <= 25*NbLabels-325) )
        return 26;
    else if ( (SelLabel > 25*NbLabels-325) && (SelLabel <= 26*NbLabels-351) )
        return 27;
    else if ( (SelLabel > 26*NbLabels-351) && (SelLabel <= 27*NbLabels-378) )
        return 28;
    else if ( (SelLabel > 27*NbLabels-378) && (SelLabel <= 28*NbLabels-406) )
        return 29;
    else if ( (SelLabel > 28*NbLabels-406) && (SelLabel <= 29*NbLabels-435) )
        return 30;
    else if ( (SelLabel > 29*NbLabels-435) && (SelLabel <= 30*NbLabels-465) )
        return 31;
    else if ( (SelLabel > 30*NbLabels-465) && (SelLabel <= 31*NbLabels-496) )
        return 32;
    else if ( (SelLabel > 31*NbLabels-496) && (SelLabel <= 32*NbLabels-528) )
        return 33;
    else if ( (SelLabel > 32*NbLabels-528) && (SelLabel <= 33*NbLabels-561) )
        return 34;
    else if ( (SelLabel > 33*NbLabels-561) && (SelLabel <= 34*NbLabels-595) )
        return 35;
    else if ( (SelLabel > 34*NbLabels-595) && (SelLabel <= 35*NbLabels-630) )
        return 36;
    else if ( (SelLabel > 35*NbLabels-630) && (SelLabel <= 36*NbLabels-666) )
        return 37;
    else if ( (SelLabel > 36*NbLabels-666) && (SelLabel <= 37*NbLabels-703) )
        return 38;
    else if ( (SelLabel > 37*NbLabels-703) && (SelLabel <= 38*NbLabels-741) )
        return 39;
    else if ( (SelLabel > 38*NbLabels-741) && (SelLabel <= 39*NbLabels-780) )
        return 40;

    return 0;
  }
  //----------------------------------------------------------------------------
  /**
   * Return number of first basic label in label "SelLabel".
   * SelLabel= Number - 2 NOL.
   */
  public static int option ( int SelLabel, int NbLabelsOR, int NOL) {
    // Sellabel= Number - 2 NOL
    return SelLabel - (NbLabelsOR-2)*NOL + jnikbct.serie(NbLabelsOR-2);
  }
  //----------------------------------------------------------------------------
  /**
   * Return number of composite OR label.
   */
  public static int NumberORLabel ( int option, int NbLabelsOR, int NOL) {
    int aux= 0;
    if (NbLabelsOR > 3) {
        aux=serie(NOL-1)-serie(NOL-(NbLabelsOR-1));    	
    } else {
      switch (NbLabelsOR) {
        case 1: return option;
        case 2: aux=0; break;
        case 3: aux=NOL-1;break;
      }
    }
    return 2*NOL + aux + option;
  }
//------------------------------------------------------------------------------
  public static double[] findMin(double[] d) {
      double[] result= new double[2];
	  double min=d[0];
      int minIndex=1;
	  for (int n=1; n<d.length; n++) {
          if (d[n]<min) {
		      min= d[n];
		      minIndex=n+1;
          }
	  }
      result[0]= minIndex;
      result[1]= min;
	  return result;
  }
//------------------------------------------------------------------------------
  public static double[] findMax(double[] d) {
      double[] result= new double[2];
      double max=d[0];
      int maxIndex=1;
	  for (int n=1; n<d.length; n++) {
          if (d[n]>max) {
		      max= d[n];
		      maxIndex=n+1;
          }
	  }
      result[0]= maxIndex;
      result[1]= max;
	  return result;
  }
  //----------------------------------------------------------------------------
  /**
   * Store the KBCT which identifier is "kbct_ptr" in file "file_name" as a linguistic summary on HTML format.
   */
  public static void SaveLinguisticSummary(long kbct_ptr, String file_name) throws Throwable {
    PrintWriter fOut = new PrintWriter(new FileOutputStream(file_name), true);
  	fOut.println("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
  	fOut.println("<html xmlns='http://www.w3.org/1999/xhtml'>");
  	fOut.println("<head>");
  	fOut.println("<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'/>");
  	fOut.println("<title>Linguistic Summary</title>");
  	fOut.println("<style type='text/css'>");
  	fOut.println("</style></head>");
  	fOut.println("<body>");
  	//fOut.println("<p class='front-1' style='mso-margin-top-alt: auto' align='center'><font color='#FF0000' size='5'><b>"+LocaleKBCT.GetString("KBCT")+"</b></font></p>");
  	fOut.println("<p class='front-1' style='mso-margin-top-alt: auto' align='center'><font color='black' size='5'><b>"+jnikbct.GetName(kbct_ptr)+"</b></font></p>");
  	fOut.println("<br>");
  	fOut.println("<table border='1' width='100%'>");
  	fOut.println("<tr>");
  	fOut.println("<td width='10%' align='center' bgcolor='red'><b><font face='Times New Roman'>"+LocaleKBCT.GetString("Rule")+"</font></b></td>");
  	fOut.println("<td width='90%' align='center' bgcolor='red'><b><font face='Times New Roman'>"+LocaleKBCT.GetString("LinguisticRules")+"</font></b></td>");
  	fOut.println("</tr>");
    DecimalFormat df= new DecimalFormat();
    df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
    dfs.setDecimalSeparator((new String(".").charAt(0)));
    df.setDecimalFormatSymbols(dfs);
    df.setGroupingSize(20);
    int Nins = jnikbct.GetNbInputs(kbct_ptr);
    int Nouts = jnikbct.GetNbOutputs(kbct_ptr);
    int Nvars= Nins+Nouts;
    String[] varNames= new String[Nvars];
    int[] nbLabs= new int[Nvars];
    Vector vLabNames= new Vector();
    Vector vLabORNames= new Vector();
    Vector vLabRanges= new Vector();
    for (int n=0; n<Nvars; n++) {
        int N_variable= n+1;
        variable v;
        if (n<Nins)
        	v= jnikbct.GetInput(kbct_ptr, N_variable);
        else
        	v= jnikbct.GetOutput(kbct_ptr, N_variable-Nins);

        varNames[n]=v.GetName();
        String scaleOfLabels= v.GetScaleName();
        if (!scaleOfLabels.equals("user")) {
        	String[] vnames= v.GetLabelsName();
        	String[] vlocnames= new String[vnames.length];
        	for (int k=0; k<vnames.length; k++) {
        		vlocnames[k]= LocaleKBCT.GetString(vnames[k]);
        	}
            vLabNames.add(n,vlocnames);
        } else
            vLabNames.add(n,v.GetUserLabelsName());
        
        nbLabs[n]= v.GetLabelsNumber();
        double[] ranges= new double[2];
        ranges[0]= v.GetLowerInterestRange();
        ranges[1]= v.GetUpperInterestRange();
        vLabRanges.add(n,ranges);
        vLabORNames.add(n, v.GetORLabelsName());
    }
    int Nrules = jnikbct.GetNbRules(kbct_ptr);
    for (int n=0; n<Nrules; n++) {
        Rule r= jnikbct.GetRule(kbct_ptr, n+1);
        if (r.GetActive()) {
       	  fOut.println("<tr>");
          fOut.println("<td width='10%' align='center'><b><font face='Times New Roman' size='3'>R"+String.valueOf(n+1)+"</font></b></td>");
          String rule= jnikbct.GetRuleDescription(kbct_ptr, n+1, "NULL");
          /*int[] in_labels_number= r.Get_in_labels_number();
          int[] out_labels_number= r.Get_out_labels_number();
          for (int m=0; m<r.GetNbInputs();m++) {
        	if (in_labels_number[m] > 0) {
        		String[] names= (String[])vLabNames.get(m);
        		String lab;
                if (in_labels_number[m] <= nbLabs[m])
        		    lab= names[in_labels_number[m]-1];
                else if ( (in_labels_number[m] > nbLabs[m]) && (in_labels_number[m] <= 2*nbLabs[m]) )
        		    lab= LocaleKBCT.GetString("NOT")+"("+names[in_labels_number[m]-1-nbLabs[m]]+")";
                else {               	
                	lab= ((String[])vLabORNames.get(m))[in_labels_number[m]-1-2*nbLabs[m]];
                	lab= lab.replace("(","");
                	lab= lab.replace(")","");
                }
                if (rule.equals(""))
        		    rule= LocaleKBCT.GetString("IF")+" "+varNames[m]+" "+LocaleKBCT.GetString("IS")+" "+lab;
                else
        		    rule= rule+" "+LocaleKBCT.GetString("AND")+" "+varNames[m]+" "+LocaleKBCT.GetString("IS")+" "+lab;
        	}
          }
          rule= rule+" "+LocaleKBCT.GetString("THEN")+" ";
          for (int m=0; m<r.GetNbOutputs();m++) {
        	if (out_labels_number[m] > 0) {
        		String lab=((String[])vLabNames.get(m+Nins))[out_labels_number[m]-1];
                if (m==0)
        		    rule= rule+varNames[m+Nins]+" "+LocaleKBCT.GetString("IS")+" "+lab;
                else
        		    rule= rule+" "+LocaleKBCT.GetString("AND")+" "+varNames[m+Nins]+" "+LocaleKBCT.GetString("IS")+" "+lab;
        	}
          }*/
      	  fOut.println("<td width='90%' align='left'><b><font face='Times New Roman' size='3'>"+rule+"</font></b></td>");
   	  	  fOut.println("</tr>");
        }
    }
  	fOut.println("</table>");
  	fOut.println("<br>");
  	fOut.println("<table border='2' width='100%'>");
  	fOut.println("<tr>");
  	fOut.println("<td width='30%' align='center' bgcolor='red'><b><font face='Times New Roman'>"+LocaleKBCT.GetString("LinguisticVariable")+"</font></b></td>");
  	fOut.println("<td width='50%' align='center' bgcolor='red'><b><font face='Times New Roman'>"+LocaleKBCT.GetString("LinguisticTerms")+"</font></b></td>");
  	fOut.println("<td width='20%' align='center' bgcolor='red'><b><font face='Times New Roman'>"+LocaleKBCT.GetString("Range")+"</font></b></td>");
  	fOut.println("</tr>");
    for (int n=0; n<Nvars; n++) {
      	fOut.println("<tr>");
      	fOut.println("<td width='30%' align='center'><b><font face='Times New Roman'>"+varNames[n]+"</font></b></td>");
        String labels="";
        String[] names= (String[])vLabNames.get(n);
        for (int m= 0; m < nbLabs[n]; m++) {
           if (m==nbLabs[n]-1)
               labels=labels+names[m];
           else        	   
               labels=labels+names[m]+", ";
        }
      	fOut.println("<td width='50%' align='center'><b><font face='Times New Roman'>"+labels+"</font></b></td>");
        String interestLowerRange= df.format(((double[])vLabRanges.get(n))[0]);
        String interestUpperRange= df.format(((double[])vLabRanges.get(n))[1]);
      	fOut.println("<td width='20%' align='center'><b><font face='Times New Roman'>[   "+interestLowerRange+",   "+interestUpperRange+"]</font></b></td>");
   	  	fOut.println("</tr>");
    }
  	fOut.println("</table>");
  	fOut.println("</body></html>");
    fOut.flush();
    fOut.close();
  }
  //----------------------------------------------------------------------------
  /**
   * Save rule weights (accuracy).
   */
  public static void SaveRuleWeights(String option, String file_name, int[] rules, double[] weights, double[] weightsCC, double[] weightsIC) throws Throwable {
    // CC= correct conclusion
    // IC= incorrect conclusion
	PrintWriter fOut = new PrintWriter(new FileOutputStream(file_name), true);
    if (option.equals("Local"))
	    fOut.println(LocaleKBCT.GetString("CumulatedLocalWeight"));
    else if (option.equals("Global")) 
	    fOut.println(LocaleKBCT.GetString("CumulatedGlobalWeight"));
    else if (option.equals("L+G"))
	    fOut.println(LocaleKBCT.GetString("CumulatedWeight"));

    fOut.println("******************************************");
    DecimalFormat df= new DecimalFormat();
    df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals()+2);//5
    df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals()+2);//5
    DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
    dfs.setDecimalSeparator((new String(".").charAt(0)));
    df.setDecimalFormatSymbols(dfs);
    df.setGroupingSize(20);
    double total= 0;
    double totalCC= 0;
    double totalIC= 0;
    int NbRules= rules.length;
    for (int n=0; n<NbRules; n++) {
         if (weights[n]>=0) {
             fOut.println(LocaleKBCT.GetString("Rule")+ String.valueOf(rules[n]+1)+"  => "+df.format(weights[n])+"  (CC="+df.format(weightsCC[n])+")  (IC="+df.format(weightsIC[n])+")");
             total= total + weights[n];
             totalCC= totalCC + weightsCC[n];
             totalIC= totalIC + weightsIC[n];
         }
    }
    fOut.println(LocaleKBCT.GetString("sum").toUpperCase()+ "  => "+df.format(total)+"  (CC="+df.format(totalCC)+")  (IC="+df.format(totalIC)+")");
    double avt= total/NbRules;
    double avtCC= totalCC/NbRules;
    double avtIC= totalIC/NbRules;
    fOut.println(LocaleKBCT.GetString("averagevalue").toUpperCase()+ "  => "+df.format(avt)+"  (CC="+df.format(avtCC)+")  (IC="+df.format(avtIC)+")");
    fOut.println("******************************************");
    fOut.flush();
    fOut.close();
  }
  //----------------------------------------------------------------------------
  /**
   * Save rule weights (interpretability).
   */
  public static void SaveRuleIntWeights(String option, String file_name, int[] rules, double[] weights) throws Throwable {
	PrintWriter fOut = new PrintWriter(new FileOutputStream(file_name), true);
    if (option.equals("Local"))
	    fOut.println(LocaleKBCT.GetString("CumulatedLocalIntWeight"));
    else if (option.equals("Global")) 
	    fOut.println(LocaleKBCT.GetString("CumulatedGlobalIntWeight"));
    else if (option.equals("L+G"))
	    fOut.println(LocaleKBCT.GetString("CumulatedIntWeight"));
    
    fOut.println("******************************************");
    DecimalFormat df= new DecimalFormat();
    df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals()+2);//5
    df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals()+2);//5
    DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
    dfs.setDecimalSeparator((new String(".").charAt(0)));
    df.setDecimalFormatSymbols(dfs);
    df.setGroupingSize(20);
    double total= 0;
    int NbRules= rules.length;
    for (int n=0; n<NbRules; n++) {
         fOut.println(LocaleKBCT.GetString("Rule")+ String.valueOf(rules[n]+1)+"  => "+df.format(weights[n]));
         if (weights[n]>0)
             total= total + weights[n];
    }
    fOut.println(LocaleKBCT.GetString("sum").toUpperCase()+ "  => "+df.format(total));
    double avt= total/NbRules;
    fOut.println(LocaleKBCT.GetString("averagevalue").toUpperCase()+ "  => "+df.format(avt));
    fOut.println("******************************************");
    fOut.flush();
    fOut.close();
  }
  //----------------------------------------------------------------------------
  /**
   * 
   */
  public static void cleanHashtable() {
	    //System.out.println("clean all hashtable");
  	    ht.clear();
	    ht= new Hashtable<String,Data_System>();
	    id= 0;
	    System.gc();
  }
  //----------------------------------------------------------------------------
  /**
   * 
   */
  public static void cleanHashtable(long ptr) {
      //System.out.println("cleanHashtable -> "+ptr);
      //long lim= ht.size();
      //System.out.println(lim);
      Enumeration en= ht.keys();
      while (en.hasMoreElements()) {
    	  String key= (String)en.nextElement();
    	  //System.out.println("id="+jnikbct.getId());
    	  //System.out.println("key="+key);
    	  Integer in= new Integer(key);
   	      if (in.intValue() > ptr) {
   	    	  //System.out.println("jnikbct.cleanHash -> remove "+in.longValue());
   	 	      //if (in.intValue()==25)
   		         //System.out.println("jnikbct.DeleteKBCT -> "+in.intValue());
   	    	  
   	    	  jnikbct.DeleteKBCT(in.longValue());
   	      }
      }
      /*while (lim > ptr) {
    	  if (ht.containsKey(""+lim)) {
    		  //System.out.println("Remove");
    	      ht.remove(""+lim);
    	  }
    	  lim--;
      }*/
      //System.out.println(" -> "+ht.size());
	  id= ptr+1;
      System.gc();
  }
  //----------------------------------------------------------------------------
  /**
   * 
   */
  public static void cleanHashtable(long ptr, long[] ptrexc) {
	  //System.out.println("cleanHashtable -> "+ptr);
      Enumeration en= ht.keys();
      //System.out.println("total -> "+ht.size());
      //int c=0;
      while (en.hasMoreElements()) {
    	  String key= (String)en.nextElement();
    	  //System.out.println("id="+jnikbct.getId());
    	  //System.out.println("key="+key);
    	  Integer in= new Integer(key);
   	      if ( (in.intValue() > ptr) &&
   	    	   (in.intValue()!=ptrexc[0]) && (in.intValue()!=ptrexc[1]) &&
   	    	   (in.intValue()!=ptrexc[2]) && (in.intValue()!=ptrexc[3]) &&
   	    	   (in.intValue()!=ptrexc[4]) && (in.intValue()!=ptrexc[5]) &&
  	    	   (in.intValue()!=ptrexc[6]) && (in.intValue()!=ptrexc[7]) ) {
   	    	  //if (in.longValue()==294)
   	    	    //  System.out.println("jnikbct.cleanHashExc -> remove "+in.longValue());
   	    	  
   	    	  jnikbct.DeleteKBCT(in.longValue());
   	    	  //c++;
   	      }
      }
      //System.out.println(" -> removed: "+c);
      //System.out.println(" -> remaining: "+ht.size());
      long maxexc= Math.max(ptrexc[1],ptrexc[3]);
      maxexc= Math.max(maxexc,ptrexc[5]);
      maxexc= Math.max(maxexc,ptrexc[7]);
	  id= Math.max(ptr,maxexc)+1;
      System.gc();
  }
  //----------------------------------------------------------------------------
  /**
   * 
   */
  public static int getHashtableSize() {
	    if (ht!=null) {
	        //System.out.println("jnikbct.getHashtableSize: "+ht.size());
	    	//Enumeration en= ht.keys();
	    	//while (en.hasMoreElements()) {
	    	//	System.out.println(" ht -> "+en.nextElement().toString());
	    	//}
  	        return ht.size();
	    }
	    else
	    	return -1;
  }
  //----------------------------------------------------------------------------
  /**
   * 
   */
  public static long getId() {
  	    return id;
  }
  //----------------------------------------------------------------------------
  /**
   * 
   */
  public static void saveCurrentConfigParameters(String confFile) {
      //System.out.println("Saving confFile -> "+confFile);
	  XMLWriter.createCFFile(confFile);
	  XMLWriter.writeCFFile();
      XMLWriter.closeCFFile(true);
  }
}