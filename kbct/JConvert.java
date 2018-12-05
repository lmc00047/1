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
//                              JConvert.java
//
//
//**********************************************************************

package kbct;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import xml.XMLWriter;

import kbctAux.MessageKBCT;
import kbctFrames.JKBCTFrame;
import KB.LabelKBCT;
import KB.Rule;
import KB.variable;
import fis.JInput;
import fis.JOutput;
import fis.JMF;
import fis.JRule;

/**
 * <p align="left"> Convert files: <p>
 * <ul>
 *     <li> GUAJE to FisPro </li>
 *     <li> FisPro to Matlab </li>
 *     <li> GUAJE to Matlab </li>
 *     <li> GUAJE to Xfuzzy </li>
 *     <li> GUAJE to Espresso </li>
 * </ul>
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
public class JConvert {
   public static String conjunction= null;
   public static String[] disjunction= null;
   public static String[] defuzzification= null;
   private static int cont=1;
  //----------------------------------------------------------------------------
  /**
   * Convert GUAJE archive "file_name_GUAJE" to FisPro archive "file_name_FIS".
   * @param file_name_GUAJE name of the GUAJE file to be translated
   * @param file_name_FIS name of the translated FIS file
   * @param quality true (under a quality evaluation in a simplification process) / false
   * @return KB with the expanded rule base
   * @throws Throwable exception
   */
  public static JKBCT GuajeToFis (String file_name_GUAJE, String file_name_FIS, boolean quality) throws Throwable{
    JKBCT kbct;
    if (!quality)
      kbct= SpreadKB(file_name_GUAJE);
    else
      kbct= SpreadKBquality(file_name_GUAJE);

    PrintWriter fOut = new PrintWriter(new FileOutputStream(file_name_FIS), true);
    DecimalFormat df= new DecimalFormat();
    df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
    dfs.setDecimalSeparator((new String(".").charAt(0)));
    df.setDecimalFormatSymbols(dfs);
    df.setGroupingSize(20);
    if (JConvert.conjunction==null || JConvert.disjunction==null || JConvert.defuzzification==null) {
      int NbOutputs= kbct.GetNbOutputs();
      String[] OutputTypes= null;
      if (NbOutputs >0) {
        OutputTypes= new String[NbOutputs];
        for (int n=0; n<NbOutputs; n++)
          OutputTypes[n]= kbct.GetOutput(n+1).GetType();
      }
      //System.out.println("setFIS default parameters");
      JConvert.SetFISoptionsDefault(NbOutputs, OutputTypes);
    }
    fOut.println("[Interface]");
    fOut.println("DataSep=,");
    fOut.println();
    fOut.println("[System]");
    fOut.println("Name='"+kbct.GetName()+"'");
    int Ninputs= kbct.GetNbInputs();
    fOut.println("Ninputs=" + Ninputs);
    int Noutputs= kbct.GetNbOutputs();
    fOut.println("Noutputs=" + Noutputs);
    boolean warnCheck= false;
    JKBCTOutput jout= kbct.GetOutput(1);
    if ( (jout.GetScaleName().equals("user")) && 
         (jout.GetInputInterestRange()[0]!=1) &&
     	 (jout.GetInputInterestRange()[1]!=jout.GetLabelsNumber()) &&
     	 (jout.isOutput()) &&
     	 ( (jout.GetType().equals("logical")) || (jout.GetType().equals("categorical")) ) ) {
         warnCheck= true;
    } 
    if (!warnCheck) {
      for (int n=0; n<kbct.GetNbRules(); n++) {
       	Rule rr= kbct.GetRule(n+1);
     	if (rr.GetActive()) {
     		int[] conclusions= rr.Get_out_labels_number();
     		boolean warn= false;        			
     		for (int k=0; k<conclusions.length; k++) {
     			if (conclusions[k]!=0) {
     				warn=true;
     				break;
     		    }
     		}
     	    if (!warn)
     	    	kbct.SetRuleActive(n+1,false);
     	}
      }
    }
    int Nrules= kbct.GetNbActiveRules();
    fOut.println("Nrules=" + Nrules);
    fOut.println("Nexceptions=0");
    fOut.println("Conjunction='"+conjunction+"'");
    fOut.println("MissingValues='random'");
    fOut.println();
    for (int k=0; k<Ninputs; k++) {
        int N_variable= k+1;
        JKBCTInput in= kbct.GetInput(N_variable);
        variable v= in.GetV();
        fOut.println();
        fOut.println("[Input"+N_variable+"]");
        boolean Active= v.isActive();
        if (Active)
          fOut.println("Active='yes'");
        else
          fOut.println("Active='no'");

        String variableName=v.GetName();
        fOut.println("Name='"+variableName+"'");
        String interestLowerRange=df.format(v.GetLowerInterestRange());
        String interestUpperRange=df.format(v.GetUpperInterestRange());
        fOut.println("Range=[   "+interestLowerRange+",   "+interestUpperRange+"]");
        int numberOfLabels= v.GetLabelsNumber();
        fOut.println("NMFs="+numberOfLabels);
        String scaleOfLabels= v.GetScaleName();
        if (!quality) {
            for (int n=0; n<numberOfLabels; n++) {
              int N_label= n+1;
              LabelKBCT e= v.GetLabel(N_label);
              if (v.GetFlagModify()) {
                  if (e.GetName().equals("trapezoidal"))
                      fOut.println("MF"+N_label+"='"+v.GetLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+",   "+df.format(e.GetP4())+"]");
                  else if (e.GetName().equals("SemiTrapezoidalInf") || e.GetName().equals("triangular") || e.GetName().equals("SemiTrapezoidalSup"))
                      fOut.println("MF"+N_label+"='"+v.GetLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+"]");
                  else if (e.GetName().equals("universal") || e.GetName().equals("gaussian"))
                      fOut.println("MF"+N_label+"='"+v.GetLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+"]");
                  else if (e.GetName().equals("discrete"))
                      fOut.println("MF"+N_label+"='"+v.GetLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+"]");
              } else if (scaleOfLabels.equals("user")) {
                  if (e.GetName().equals("trapezoidal"))
                      fOut.println("MF"+N_label+"='"+v.GetUserLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+",   "+df.format(e.GetP4())+"]");
                  else if (e.GetName().equals("SemiTrapezoidalInf") || e.GetName().equals("triangular") || e.GetName().equals("SemiTrapezoidalSup"))
                      fOut.println("MF"+N_label+"='"+v.GetUserLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+"]");
                  else if (e.GetName().equals("universal") || e.GetName().equals("gaussian"))
                      fOut.println("MF"+N_label+"='"+v.GetUserLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+"]");
                  else if (e.GetName().equals("discrete"))
                      fOut.println("MF"+N_label+"='"+v.GetUserLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+"]");
              } else {
                  if (e.GetName().equals("trapezoidal"))
                      fOut.println("MF"+N_label+"='"+LocaleKBCT.GetString(v.GetLabelsName()[n])+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+",   "+df.format(e.GetP4())+"]");
                  else if (e.GetName().equals("SemiTrapezoidalInf") || e.GetName().equals("triangular") || e.GetName().equals("SemiTrapezoidalSup"))
                      fOut.println("MF"+N_label+"='"+LocaleKBCT.GetString(v.GetLabelsName()[n])+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+"]");
                  else if (e.GetName().equals("universal") || e.GetName().equals("gaussian"))
                      fOut.println("MF"+N_label+"='"+LocaleKBCT.GetString(v.GetLabelsName()[n])+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+"]");
                  else if (e.GetName().equals("discrete"))
                      fOut.println("MF"+N_label+"='"+LocaleKBCT.GetString(v.GetLabelsName()[n])+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+"]");
              }
            }
          } else {
            for (int n=0; n<numberOfLabels; n++) {
              int N_label= n+1;
              LabelKBCT e= v.GetLabel(N_label);
              if (in.GetType().equals("numerical")) {
                if (e.GetName().equals("trapezoidal"))
                    fOut.println("MF"+N_label+"='"+in.GetMFLabelNames()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+",   "+df.format(e.GetP4())+"]");
                else if (e.GetName().equals("SemiTrapezoidalInf") || e.GetName().equals("triangular") || e.GetName().equals("SemiTrapezoidalSup"))
                    fOut.println("MF"+N_label+"='"+in.GetMFLabelNames()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+"]");
                else if (e.GetName().equals("universal") || e.GetName().equals("gaussian"))
                    fOut.println("MF"+N_label+"='"+in.GetMFLabelNames()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+"]");
                else if (e.GetName().equals("discrete"))
                    fOut.println("MF"+N_label+"='"+in.GetMFLabelNames()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+"]");
              } else {
                if (scaleOfLabels.equals("user")) {
                    if (e.GetName().equals("trapezoidal"))
                        fOut.println("MF"+N_label+"='"+v.GetUserLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+",   "+df.format(e.GetP4())+"]");
                    else if (e.GetName().equals("SemiTrapezoidalInf") || e.GetName().equals("triangular") || e.GetName().equals("SemiTrapezoidalSup"))
                        fOut.println("MF"+N_label+"='"+v.GetUserLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+"]");
                    else if (e.GetName().equals("universal") || e.GetName().equals("gaussian"))
                        fOut.println("MF"+N_label+"='"+v.GetUserLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+"]");
                    else if (e.GetName().equals("discrete"))
                        fOut.println("MF"+N_label+"='"+v.GetUserLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+"]");
                } else {
                    if (e.GetName().equals("trapezoidal"))
                        fOut.println("MF"+N_label+"='"+LocaleKBCT.GetString(v.GetLabelsName()[n])+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+",   "+df.format(e.GetP4())+"]");
                    else if (e.GetName().equals("SemiTrapezoidalInf") || e.GetName().equals("triangular") || e.GetName().equals("SemiTrapezoidalSup"))
                        fOut.println("MF"+N_label+"='"+LocaleKBCT.GetString(v.GetLabelsName()[n])+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+"]");
                    else if (e.GetName().equals("universal") || e.GetName().equals("gaussian"))
                        fOut.println("MF"+N_label+"='"+LocaleKBCT.GetString(v.GetLabelsName()[n])+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+"]");
                    else if (e.GetName().equals("discrete"))
                        fOut.println("MF"+N_label+"='"+LocaleKBCT.GetString(v.GetLabelsName()[n])+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+"]");
                }
              }
            }
        }
        fOut.println();
    }
    for (int k=0; k<Noutputs; k++) {
        int N_variable= k+1;
        JKBCTOutput out= kbct.GetOutput(N_variable);
        variable v= out.GetV();
        fOut.println("[Output"+N_variable+"]");
        String variableType=v.GetType();
        boolean numerical= true;
        if (variableType.equals("numerical"))
            fOut.println("Nature='fuzzy'");
        else {
            fOut.println("Nature='crisp'");
            numerical= false;
        }
        fOut.println("Defuzzification='"+defuzzification[k]+"'");
        fOut.println("Disjunction='"+disjunction[k]+"'");
        fOut.println("DefaultValue=     -1.000");
        String variableClassif=v.GetClassif();
        //System.out.println("classif="+variableClassif);
        fOut.println("Classif='"+variableClassif+"'");
        boolean Active= v.isActive();
        if (Active)
          fOut.println("Active='yes'");
        else
          fOut.println("Active='no'");

        String variableName=v.GetName();
        fOut.println("Name='"+variableName+"'");
        String interestLowerRange=df.format(v.GetLowerInterestRange());
        String interestUpperRange=df.format(v.GetUpperInterestRange());
        fOut.println("Range=[   "+interestLowerRange+",   "+interestUpperRange+"]");
        if (numerical) {
            int numberOfLabels= v.GetLabelsNumber();
            fOut.println("NMFs="+numberOfLabels);
            String scaleOfLabels= v.GetScaleName();
            if (!quality) {
              for (int n=0; n<numberOfLabels; n++) {
                int N_label= n+1;
                LabelKBCT e= v.GetLabel(N_label);
                if (v.GetFlagModify()) {
                    if (e.GetName().equals("trapezoidal"))
                        fOut.println("MF"+N_label+"='"+v.GetLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+",   "+df.format(e.GetP4())+"]");
                    else if (e.GetName().equals("SemiTrapezoidalInf") || e.GetName().equals("triangular") || e.GetName().equals("SemiTrapezoidalSup"))
                        fOut.println("MF"+N_label+"='"+v.GetLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+"]");
                    else if (e.GetName().equals("universal") || e.GetName().equals("gaussian"))
                        fOut.println("MF"+N_label+"='"+v.GetLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+"]");
                    else if (e.GetName().equals("discrete"))
                        fOut.println("MF"+N_label+"='"+v.GetLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+"]");
                } else if (scaleOfLabels.equals("user")) {
                    if (e.GetName().equals("trapezoidal"))
                        fOut.println("MF"+N_label+"='"+v.GetUserLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+",   "+df.format(e.GetP4())+"]");
                    else if (e.GetName().equals("SemiTrapezoidalInf") || e.GetName().equals("triangular") || e.GetName().equals("SemiTrapezoidalSup"))
                        fOut.println("MF"+N_label+"='"+v.GetUserLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+"]");
                    else if (e.GetName().equals("universal") || e.GetName().equals("gaussian"))
                        fOut.println("MF"+N_label+"='"+v.GetUserLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+"]");
                    else if (e.GetName().equals("discrete"))
                        fOut.println("MF"+N_label+"='"+v.GetUserLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+"]");
                } else {
                    if (e.GetName().equals("trapezoidal"))
                        fOut.println("MF"+N_label+"='"+LocaleKBCT.GetString(v.GetLabelsName()[n])+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+",   "+df.format(e.GetP4())+"]");
                    else if (e.GetName().equals("SemiTrapezoidalInf") || e.GetName().equals("triangular") || e.GetName().equals("SemiTrapezoidalSup"))
                        fOut.println("MF"+N_label+"='"+LocaleKBCT.GetString(v.GetLabelsName()[n])+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+"]");
                    else if (e.GetName().equals("universal") || e.GetName().equals("gaussian"))
                        fOut.println("MF"+N_label+"='"+LocaleKBCT.GetString(v.GetLabelsName()[n])+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+"]");
                    else if (e.GetName().equals("discrete"))
                        fOut.println("MF"+N_label+"='"+LocaleKBCT.GetString(v.GetLabelsName()[n])+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+"]");
                }
              }
            } else {
              for (int n=0; n<numberOfLabels; n++) {
                  int N_label= n+1;
                  LabelKBCT e= v.GetLabel(N_label);
                  if (variableType.equals("numerical")) {
                    if (e.GetName().equals("trapezoidal"))
                        fOut.println("MF"+N_label+"='"+out.GetMFLabelNames()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+",   "+df.format(e.GetP4())+"]");
                    else if (e.GetName().equals("SemiTrapezoidalInf") || e.GetName().equals("triangular") || e.GetName().equals("SemiTrapezoidalSup"))
                        fOut.println("MF"+N_label+"='"+out.GetMFLabelNames()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+"]");
                    else if (e.GetName().equals("universal") || e.GetName().equals("gaussian"))
                        fOut.println("MF"+N_label+"='"+out.GetMFLabelNames()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+"]");
                    else if (e.GetName().equals("discrete"))
                        fOut.println("MF"+N_label+"='"+out.GetMFLabelNames()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+"]");
                  } else {
                    if (scaleOfLabels.equals("user")) {
                        if (e.GetName().equals("trapezoidal"))
                            fOut.println("MF"+N_label+"='"+v.GetUserLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+",   "+df.format(e.GetP4())+"]");
                        else if (e.GetName().equals("SemiTrapezoidalInf") || e.GetName().equals("triangular") || e.GetName().equals("SemiTrapezoidalSup"))
                            fOut.println("MF"+N_label+"='"+v.GetUserLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+"]");
                        else if (e.GetName().equals("universal") || e.GetName().equals("gaussian"))
                            fOut.println("MF"+N_label+"='"+v.GetUserLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+"]");
                        else if (e.GetName().equals("discrete"))
                            fOut.println("MF"+N_label+"='"+v.GetUserLabelsName()[n]+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+"]");
                    } else {
                        if (e.GetName().equals("trapezoidal"))
                            fOut.println("MF"+N_label+"='"+LocaleKBCT.GetString(v.GetLabelsName()[n])+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+",   "+df.format(e.GetP4())+"]");
                        else if (e.GetName().equals("SemiTrapezoidalInf") || e.GetName().equals("triangular") || e.GetName().equals("SemiTrapezoidalSup"))
                            fOut.println("MF"+N_label+"='"+LocaleKBCT.GetString(v.GetLabelsName()[n])+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+",   "+df.format(e.GetP3())+"]");
                        else if (e.GetName().equals("universal") || e.GetName().equals("gaussian"))
                            fOut.println("MF"+N_label+"='"+LocaleKBCT.GetString(v.GetLabelsName()[n])+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+",   "+df.format(e.GetP2())+"]");
                        else if (e.GetName().equals("discrete"))
                            fOut.println("MF"+N_label+"='"+LocaleKBCT.GetString(v.GetLabelsName()[n])+"','"+e.GetName()+"',[   "+df.format(e.GetP1())+"]");
                    }
                  }
              }
            }
          } else {
            fOut.println("NMFs=0");
          }
        fOut.println();
    }
    fOut.println("[Rules]");
    int NbInputs= kbct.GetNbInputs();
    int NbOutputs= kbct.GetNbOutputs();
    int NbRules= kbct.GetNbRules();
    for (int n=0; n<NbRules; n++) {
      Rule r= kbct.GetRule(n+1);
      if (r.GetActive()) {
        int[] premises= r.Get_in_labels_number();
        int[] conc= r.Get_out_labels_number();
        for (int m=0; m<NbInputs;m++)
          fOut.print(premises[m]+", ");
        fOut.print("   ");
        for (int m=0; m<NbOutputs;m++) {
          if (m==NbOutputs-1)
            fOut.println(df.format(conc[m]) + ", ");
          else
            fOut.print(df.format(conc[m]) + ", ");
        }
      }
    }
    fOut.println();
    fOut.println("[Exceptions]");
    fOut.println();
    fOut.flush();
    fOut.close();
    return kbct;
  }
  //----------------------------------------------------------------------------
  /**
   * Convert FisPro archive "file_name_FIS" to GUAJE archive "file_name_GUAJE".
   * @param file_name_FIS name of the FIS file to be translated
   * @param file_name_GUAJE name of the translated GUAJE file
   * @return KB with the rule base expanded
   * @throws Throwable exception
   */
  public static void FisToGuaje (String file_name_FIS, String file_name_GUAJE) throws Throwable{
	JFIS fis= null;
	try {
	    fis= new JFIS(file_name_FIS);
    } catch (Throwable t) {
    	//System.out.println("Error in open FIS");
	    //t.printStackTrace();
	    MessageKBCT.Error(null, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("ErrorInFISFile"));
    }
	if (fis != null) {
      // KB file
      JKBCT kb= new JKBCT();
      kb.SetName(fis.GetName());
      if (file_name_GUAJE.endsWith(".ikb.xml"))
    	  file_name_GUAJE= file_name_GUAJE.replace(".ikb.xml", ".kb.xml");
      else if (!file_name_GUAJE.endsWith(".kb.xml"))
    	  file_name_GUAJE= file_name_GUAJE + ".kb.xml";
    	
      kb.SetKBCTFile(file_name_GUAJE);
      // INPUTS
      int NbInputs= fis.NbActiveInputs();
      for (int n=0; n<NbInputs; n++) {
    	 JInput in= fis.GetActiveInput(n);
    	 String name= in.GetName();
    	 double[] range= in.GetRange();
    	 double lpr= range[0]; 
    	 double upr= range[1]; 
    	 double lir= range[0]; 
    	 double uir= range[1];
    	 int ln= in.GetNbMF(); 
    	 String type= "numerical";
    	 String trust= "llow";
    	 String classif= "no"; 
    	 String label_name= "user";
    	 boolean Active= true; 
    	 boolean FM= false;
         variable v= new variable(name, type, trust, classif, lpr, upr, lir, uir, ln, label_name, Active, FM);
         v.InitLabelsName(ln);
         v.SetLabelsName();
         //System.out.println("ln -> "+ln);
         boolean typeWarning= false;
         for (int m=0; m<ln; m++) {
        	  JMF lab= in.GetMF(m);
              v.SetUserLabelsName(m+1, lab.GetName());
              double[] par= lab.GetParams();
              LabelKBCT e= null;
              if (par.length==1) {
                  e= new LabelKBCT("discrete", par[0], m+1);
                  typeWarning= true;
              } else if (par.length==2) {
            		/*   
            		 * <li> universal -> this.UNIVERSAL -> 5 </li>
            		 * <li> gaussian -> this.GAUSSIAN -> 6 </li>
            		 * <li> door -> this.DOOR -> 7 </li>
            		 * <li> sinus -> this.SINUS -> 8 </li>
            		 * <li> SinusInf -> this.SINUSINF -> 9 </li>
            		 * <li> SinusSup -> this.SINUSSUP -> 10 </li>
            	    */
            	  int lt= lab.GetType();
            	  // System.out.println("lab.GetType() -> "+lab.GetType());
            	  if ( (lt==5) || (lt==7) ) {
                      e= new LabelKBCT("trapezoidal", par[0], par[0], par[1], par[1], m+1);
            	  } else if (lt==6) {
                      e= new LabelKBCT("triangular", par[0]-2*par[1], par[0], par[0]+2*par[1], m+1);
            	  } else if (lt==8) {
                      e= new LabelKBCT("triangular", par[0], (par[0]+par[1])/2, par[1], m+1);
            	  } else if (lt==9) {
                      e= new LabelKBCT("SemiTrapezoidalInf", par[0], par[0], par[1], m+1);
            	  } else if (lt==10) {
                      e= new LabelKBCT("SemiTrapezoidalSup", par[0], par[1], par[1], m+1);
            	  }
              } else if (par.length==3) {
            	  if (m==0)
                      e= new LabelKBCT("SemiTrapezoidalInf", par[0], par[1], par[2], m+1);
            	  else if (m==ln-1)
                      e= new LabelKBCT("SemiTrapezoidalSup", par[0], par[1], par[2], m+1);
            	  else
                      e= new LabelKBCT("triangular", par[0], par[1], par[2], m+1);
              } else if (par.length==4)
                  e= new LabelKBCT("trapezoidal", par[0], par[1], par[2], par[3], m+1);
              else
            	  e= new LabelKBCT();
              
              v.AddLabel(e);
         }
         if (typeWarning) {
        	 if (ln==2)
        		 v.SetType("logical");
        	 else 
        		 v.SetType("categorical");
         }
         JKBCTInput new_input = new JKBCTInput(v, kb.GetNbInputs()+1);
         new_input.SetName(name);
         kb.AddInput(new_input);
      }
      // OUTPUTS
      boolean classFlag= false;
      int numberOfOutputLabels= 0;
      int NbOutputs= fis.NbActiveOutputs();
      String[] disjop= new String[NbOutputs];
      String[] defuzzop= new String[NbOutputs];
      for (int n=0; n<NbOutputs; n++) {
    	 JOutput out= fis.GetActiveOutput(n);
    	 String name= out.GetName();
    	 double[] range= out.GetRange();
    	 double lpr= range[0]; 
    	 double upr= range[1]; 
    	 double lir= range[0]; 
    	 double uir= range[1];
    	 int ln= out.GetNbMF(); 
    	 String type= "numerical";
    	 int nature= out.GetNature();
    	 if (nature==1) {
    		 if (ln==2)
    			 type= "logical";
    		 else
    			 type= "categorical";
    	 }
    	 //System.out.println("nature -> "+nature);
    	 String trust= "llow";
    	 boolean cl= out.GetClassif();
   	     String classif= "no";
   	     if (cl)
   	    	 classif= "yes";
   	     
   	     int disj= out.GetDisjunction();
   	     //System.out.println("disj -> "+disj);
   	     disjop[n]= "max";
   	     if (disj == 0)
   	   	     disjop[n]= "sum";
   	    	 
   	     int defuzz= out.GetDefuz();
   	     //System.out.println("defuzz -> "+defuzz);
   	     defuzzop[n]= "sugeno";
   	     if (nature==1) {
   	         if (defuzz == 1)
   	    	     defuzzop[n]= "MaxCrisp";
   	     } else {
   	         if (defuzz == 0)
   	    	     defuzzop[n]= "area";
   	         else if (defuzz == 1)
   	    	     defuzzop[n]= "MeanMax";
   	     }
    	 String label_name= "user";
    	 boolean Active= true; 
    	 boolean FM= false;
         variable v= new variable(name, type, trust, classif, lpr, upr, lir, uir, ln, label_name, Active, FM);
         v.InitLabelsName(ln);
         v.SetLabelsName();
         //System.out.println("ln -> "+ln);
         if (nature==1) {
        	 ln= (int)upr;
        	 for (int m=0; m<ln; m++) {
        		  LabelKBCT e= new LabelKBCT("discrete", m+1, m+1);
                  v.AddLabel(e);
        	 }
         } else {
             for (int m=0; m<ln; m++) {
       	       JMF lab= out.GetMF(m);
               v.SetUserLabelsName(m+1, lab.GetName());
               double[] par= lab.GetParams();
               LabelKBCT e= null;
               if (par.length==1) {
                   e= new LabelKBCT("discrete", par[0], m+1);
               } else if (par.length==2) {
             		/*   
             		 * <li> universal -> this.UNIVERSAL -> 5 </li>
             		 * <li> gaussian -> this.GAUSSIAN -> 6 </li>
             		 * <li> door -> this.DOOR -> 7 </li>
             		 * <li> sinus -> this.SINUS -> 8 </li>
             		 * <li> SinusInf -> this.SINUSINF -> 9 </li>
             		 * <li> SinusSup -> this.SINUSSUP -> 10 </li>
             	    */
             	  int lt= lab.GetType();
             	  // System.out.println("lab.GetType() -> "+lab.GetType());
             	  if ( (lt==5) || (lt==7) ) {
                       e= new LabelKBCT("trapezoidal", par[0], par[0], par[1], par[1], m+1);
             	  } else if (lt==6) {
                       e= new LabelKBCT("triangular", par[0]-2*par[1], par[0], par[0]+2*par[1], m+1);
             	  } else if (lt==8) {
                       e= new LabelKBCT("triangular", par[0], (par[0]+par[1])/2, par[1], m+1);
             	  } else if (lt==9) {
                       e= new LabelKBCT("SemiTrapezoidalInf", par[0], par[0], par[1], m+1);
             	  } else if (lt==10) {
                       e= new LabelKBCT("SemiTrapezoidalSup", par[0], par[1], par[1], m+1);
             	  }
               } else if (par.length==3) {
           	       if (m==0)
                     e= new LabelKBCT("SemiTrapezoidalInf", par[0], par[1], par[2], m+1);
           	       else if (m==ln-1)
                     e= new LabelKBCT("SemiTrapezoidalSup", par[0], par[1], par[2], m+1);
           	       else
                     e= new LabelKBCT("triangular", par[0], par[1], par[2], m+1);
               } else if (par.length==4)
                   e= new LabelKBCT("trapezoidal", par[0], par[1], par[2], par[3], m+1);
               else
           	       e= new LabelKBCT();
             
               v.AddLabel(e);
             }
         }
   	     if (n==0) {
   	    	classFlag= cl;
   	    	numberOfOutputLabels= v.GetLabelsNumber();
   	     }
         JKBCTOutput new_output = new JKBCTOutput(v, kb.GetNbOutputs()+1);
         new_output.SetName(name);
         kb.AddOutput(new_output);
      }
      // RULES
      if (NbOutputs>0) {
        int NbRules= fis.NbRules();
        for (int n=0; n<NbRules; n++) {
		     JRule rule = fis.GetRule(n);
		     int[] premises = rule.Facteurs();
		     double[] concs = rule.Actions();
		     int rule_number = kb.GetNbRules();
		     int N_inputs = premises.length;
		     int N_outputs = concs.length;
		     int[] out_labels = new int[N_outputs];
		     for (int k = 0; k < N_outputs; k++)
			      out_labels[k] = (int) concs[k];

		     kb.AddRule(new Rule(rule_number, N_inputs, N_outputs, premises, out_labels, "I", rule.GetActive()));
        }
      }
      kb.Save();
      // IKB file
      int conj= fis.GetConjunction();
      String conjop= "min";
      if (conj==1)
    	  conjop= "prod";
      else if (conj==2)
    	  conjop= "luka";
    
      String ikb= file_name_GUAJE.replace(".kb.xml", ".ikb.xml");
      try {
    	XMLWriter.createIKBFile(ikb);
    	int lim= JConvert.disjunction.length;
        String[] auxdisj= new String[lim];
        String[] auxdefuzz= new String[lim];
        if (NbOutputs > 0) {
            for (int n=0; n<lim; n++) {
            	 auxdisj[n]= JConvert.disjunction[n];
            	 auxdefuzz[n]= JConvert.defuzzification[n];
            }
            JConvert.disjunction= new String[NbOutputs];
            JConvert.defuzzification= new String[NbOutputs];
            for (int n=0; n<NbOutputs; n++) {
           	     JConvert.disjunction[n]= disjop[n];
           	     JConvert.defuzzification[n]= defuzzop[n];
            }
            XMLWriter.writeIKBFileExpert(file_name_GUAJE, conjop, true);
        } else 
            XMLWriter.writeIKBFileExpert(file_name_GUAJE, conjop, false);
      	
        XMLWriter.writeIKBFileData("", "no", "", "", 0);
        XMLWriter.closeIKBFile(false);
        String problem= fis.GetName();
    	String[] KBintPath= MainKBCT.getConfig().GetKBCTintFilePath();
        String[] conjs=MainKBCT.getConfig().GetKBCTintConjunction();
        String[] disjs=MainKBCT.getConfig().GetKBCTintDisjunction();
        String[] defuzz=MainKBCT.getConfig().GetKBCTintDefuzzification();
        XMLWriter.writeIKBFileContext(problem, NbInputs, classFlag, numberOfOutputLabels);
        XMLWriter.writeIKBFileInterpretability(KBintPath,conjs,disjs,defuzz);
        XMLWriter.writeIKBFileOntology("");
        XMLWriter.closeIKBFile(true);
        if (NbOutputs > 0) {
            JConvert.disjunction= new String[lim];
            JConvert.defuzzification= new String[lim];
            for (int n=0; n<lim; n++) {
       	         JConvert.disjunction[n]= auxdisj[n];
       	         JConvert.defuzzification[n]= auxdefuzz[n];
            }
        }
      } catch (Throwable t) {
  	      t.printStackTrace();
  	      MessageKBCT.Error(null, t);
      }
      kb.Close();
	}
  } 
  //----------------------------------------------------------------------------
  /**
   * Convert FisPro archive "file_name_FIS" to MATLAB archive "file_name_MATLAB".
   * One MATLAB archive is generated for each output from FisPro archive
   * (file_name_MATLAB_o1 for output 1, file_name_MATLAB_o2 for output 2, ...).
   * @param file_name_FIS name of the FIS file to be translated
   * @param file_name_MATLAB name of the translated MATLAB file
   * @throws Throwable exception
   */
  public static void FisToMatlab (String file_name_FIS, String file_name_MATLAB) throws Throwable{
    LineNumberReader lnr= new LineNumberReader(new InputStreamReader(new FileInputStream(file_name_FIS)));
    String l;
    String NumOutputs="";
    String NumInputs="";
    int Noutputs=0;
    int Ninputs=0;
    String[] Type= null;
    String TypeOutput="sugeno";
    String[] DefuzzMethod= null;
    double[] nbOutLabels= null;
    boolean[] classifOpt= null;
    while((l=lnr.readLine())!=null) {
      if (l.startsWith("Ninputs")) {
          NumInputs= l.substring(8);
          Ninputs= Integer.parseInt(NumInputs);
      } else if (l.startsWith("Noutputs")) {
          NumOutputs= l.substring(9);
          Noutputs= Integer.parseInt(NumOutputs);
          if (Noutputs > 0) {
              Type = new String[Noutputs];
              DefuzzMethod = new String[Noutputs];
              nbOutLabels = new double[Noutputs];
              classifOpt = new boolean[Noutputs];
          }
          for (int n=0; n<Noutputs; n++) {
               while ((l=lnr.readLine())!=null) {
                       if (l.startsWith("Nature")) {
                           Type[n]= l.substring(7);
                           l=lnr.readLine();
                           DefuzzMethod[n]= l.substring(16);
              			   while ((l=lnr.readLine())!=null) {
                       	     //System.out.println("l="+l);
              				 if (l.startsWith("Classif")) {
              					 String caux= l.substring(9,l.length()-1);
              					 //System.out.println("caux="+caux);
              					 if (caux.equals("yes"))
              				         classifOpt[n]=true;
              					 else 
              						 classifOpt[n]=false;
              					
              					 //System.out.print("classifOpt="+classifOpt[n]);
              				 } else if (l.startsWith("Range")) {
                           	     int indaux= l.indexOf(",");
                         	     String aux= l.substring(indaux+4,l.length()-1);
                         	     //System.out.println("indaux="+indaux+"   aux="+aux);
                                 nbOutLabels[n]= (new Double(aux)).doubleValue();
              				     break;
              				 }
              			   } 
                       }
               }
          }
      }
    }
    lnr.close();
    boolean warning= false;
    if (Noutputs==0) {
      warning=true;
      Noutputs++;
    }
    for (int n=0; n<Noutputs; n++) {
      lnr= new LineNumberReader(new InputStreamReader(new FileInputStream(file_name_FIS)));
      String file_name= file_name_MATLAB.substring(0, file_name_MATLAB.lastIndexOf("."+("FIS").toLowerCase()));
      if (warning)
        file_name= file_name + "." + ("FIS").toLowerCase();
      else
        file_name= file_name + "_o" + String.valueOf(n+1) + "." + ("FIS").toLowerCase();

      PrintWriter fOut = new PrintWriter(new FileOutputStream(file_name), true);
      while((l= lnr.readLine())!=null) {
        if (l.equals("[System]")) {
            fOut.println(l);
            fOut.println(lnr.readLine());
            if (warning) {
                TypeOutput= "mamdani";
                fOut.println("Type='mamdani'");
            } else {
                if (Type[n].equals("'fuzzy'")) {
                    TypeOutput= "mamdani";
                    fOut.println("Type='mamdani'");
                } else if (Type[n].equals("'crisp'")) {
                    TypeOutput= "sugeno";
                    fOut.println("Type='sugeno'");
                }
            }
            fOut.println("NumInputs="+lnr.readLine().substring(8));
            //fOut.println("NumOutputs="+lnr.readLine().substring(9));
            lnr.readLine();
            fOut.println("NumOutputs="+nbOutLabels[n]);
            fOut.println("NumRules="+lnr.readLine().substring(7));
            lnr.readLine();
            String conjuction=lnr.readLine().substring(12);
            fOut.println("AndMethod="+conjuction);
            fOut.println("OrMethod='max'");
            //fOut.println("ImpMethod="+conjuction);
            //fOut.println("AggMethod='max'");
            fOut.println("ImpMethod='prod'");
            fOut.println("AggMethod='sum'");
            //fOut.println("DefuzzMethod='wtaver'");
            if (warning)
              fOut.println("DefuzzMethod='centroid'");
            else {
              if (DefuzzMethod[n].equals("'area'"))
                fOut.println("DefuzzMethod='centroid'");
              else if (DefuzzMethod[n].equals("'MeanMax'"))
                fOut.println("DefuzzMethod='mom'");
              else if (DefuzzMethod[n].equals("'sugeno'"))
                fOut.println("DefuzzMethod='wtaver'");
              else {
            	  if (TypeOutput.equals("mamdani"))
                      fOut.println("DefuzzMethod='centroid'");
            	  else
                      fOut.println("DefuzzMethod='wtaver'");
              }
            }
            fOut.println();
        } else if (l.startsWith("[Input")){
            fOut.println(l);
            lnr.readLine();
            String InputName= lnr.readLine();
            InputName= ModifyString(InputName);
            fOut.println(InputName);
            //Range: quitar ","
            String FisRange=lnr.readLine();
            int RangeSeparator= FisRange.indexOf(",");
            fOut.println("Range=["+FisRange.substring(10, RangeSeparator)+" "+FisRange.substring(RangeSeparator+4, FisRange.length()-1)+"]");
            int NOL= Integer.parseInt(lnr.readLine().substring(5));
            fOut.println("NumMFs="+NOL);
            for (int m=0; m<NOL; m++) {
              String FisLabel= lnr.readLine();
              String Label_Name="", Label_Def="";
              double P1=0, P2=0, P3=0, P4=0;
              int ini= 5;
              if (m >= 9)
                ini= 6;

              for (int k=0; k < 2; k++){
                int ind=0;
                for (int i=0;i<FisLabel.length();i++)
                     if((FisLabel.charAt(i))==',') {
                         ind=i; break;
                         }
                switch (k) {
                  case 0: Label_Name = FisLabel.substring(ini, ind - 1);
                          FisLabel = FisLabel.substring(ind + 2);
                          break;
                  case 1: Label_Def = FisLabel.substring(0, ind - 1);
                          FisLabel = FisLabel.substring(ind + 5);
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

              for (int k=0; k < N_params; k++){
                     int ind=0;
                     for (int i=0;i<FisLabel.length();i++)
                          if((FisLabel.charAt(i))==',') {
                              ind=i; break;
                              }
                     switch (k) {
                       case 0: if (N_params>1)
                                 P1= (new Double(FisLabel.substring(0, ind))).doubleValue();
                               else
                                 P1= (new Double(FisLabel.substring(0, FisLabel.length()-1))).doubleValue();
                               FisLabel= FisLabel.substring(ind + 4);
                               break;
                       case 1: if (N_params>2)
                                 P2= (new Double(FisLabel.substring(0, ind))).doubleValue();
                               else
                                 P2= (new Double(FisLabel.substring(0, FisLabel.length()-1))).doubleValue();
                               FisLabel= FisLabel.substring(ind + 4);
                               break;
                       case 2: if (N_params>3)
                                 P3= (new Double(FisLabel.substring(0, ind))).doubleValue();
                               else
                                 P3= (new Double(FisLabel.substring(0, FisLabel.length()-1))).doubleValue();
                               FisLabel= FisLabel.substring(ind + 4);
                               break;
                       case 3: P4= (new Double(FisLabel.substring(0, FisLabel.length()-1))).doubleValue();
                               break;
                     }
                  }
                  Label_Name= ModifyString(Label_Name);
                  String MatlabLabelDef="";
                  if (Label_Def.equals("trapezoidal"))
                      MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+P1+" "+P2+" "+P3+" "+P4+"]";
                  else if (Label_Def.equals("SemiTrapezoidalInf"))
                      MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+P1+" "+P1+" "+P2+" "+P3+"]";
                  else if (Label_Def.equals("SemiTrapezoidalSup"))
                      MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+P1+" "+P2+" "+P3+" "+P3+"]";
                  else if (Label_Def.equals("triangular"))
                      MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trimf"+"',["+P1+" "+P2+" "+P3+"]";
                  else if (Label_Def.equals("gaussian"))
                      MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"gaussmf"+"',["+P1+" "+P2+"]";
                  else if (Label_Def.equals("universal"))
                      MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+P1+" "+P1+" "+P2+" "+P2+"]";
                  else if (Label_Def.equals("discrete"))
                      MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trimf"+"',["+P1+" "+P1+" "+P1+"]";

              fOut.println("MF" + String.valueOf(m + 1) + "=" + MatlabLabelDef);
            }
            fOut.println();
        } else if (l.equals("[Output"+String.valueOf(n+1)+"]")){
            if (classifOpt[n]) {
        	    for (int k=0; k<(int)nbOutLabels[n]; k++) {
        		     String out= String.valueOf(k+1);
        		     fOut.println("[Output"+out+"]");
        		     fOut.println("Name='class"+out+"'");
        		     fOut.println("Range=[0 1]");
        		     fOut.println("NumMFs=2");
        		     fOut.println("MF1='val0':'constant',[0]");
        		     fOut.println("MF2='val1':'constant',[1]");
        		     fOut.println();
        	    }
            } else {
                fOut.println(l);
                for (int m=0; m<6; m++)
                     lnr.readLine();

                String OutputName= lnr.readLine();
                OutputName= ModifyString(OutputName);
                fOut.println(OutputName);
                // Range: quitar ","
                String FisRange=lnr.readLine();
                int RangeSeparator= FisRange.indexOf(",");
                fOut.println("Range=["+FisRange.substring(10, RangeSeparator)+" "+FisRange.substring(RangeSeparator+4, FisRange.length()-1)+"]");
                int NOL= Integer.parseInt(lnr.readLine().substring(5));
                if ( (NOL==0) && (TypeOutput.equals("sugeno")) ) {
                      NOL= (new Double(FisRange.substring(RangeSeparator+4, FisRange.length()-1))).intValue();
                      fOut.println("NumMFs="+NOL);
                      for (int k=0; k < NOL; k++) {
                           int NbLabelOutput= k+1;
                           fOut.println("MF"+NbLabelOutput+"='output"+NbLabelOutput+"':'constant',["+NbLabelOutput+"]");
                      }
                } else {
                    fOut.println("NumMFs="+NOL);
                    // Hay que cambiar "," por ":"
                    for (int m=0; m<NOL; m++) {
                         String FisLabel= lnr.readLine();
                         String Label_Name="", Label_Def="";
                         double P1=0, P2=0, P3=0, P4=0;
                         int ini= 5;
                         if (m >= 9)
                            ini= 6;

                         for (int k=0; k < 2; k++){
                              int ind=0;
                              for (int i=0;i<FisLabel.length();i++)
                                   if((FisLabel.charAt(i))==',') {
                                       ind=i; break;
                                   }
                                   switch (k) {
                                       case 0: Label_Name = FisLabel.substring(ini, ind - 1);
                                               FisLabel = FisLabel.substring(ind + 2);
                                               break;
                                       case 1: Label_Def = FisLabel.substring(0, ind - 1);
                                               FisLabel = FisLabel.substring(ind + 5);
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

                              for (int k=0; k < N_params; k++) {
                                   int ind=0;
                                   for (int i=0;i<FisLabel.length();i++)
                                        if((FisLabel.charAt(i))==',') {
                                            ind=i; break;
                                        }
                                   switch (k) {
                                        case 0: if (N_params>1)
                                                    P1= (new Double(FisLabel.substring(0, ind))).doubleValue();
                                                else
                                                    P1= (new Double(FisLabel.substring(0, FisLabel.length()-1))).doubleValue();
                                                FisLabel= FisLabel.substring(ind + 4);
                                                break;
                                        case 1: if (N_params>2)
                                                    P2= (new Double(FisLabel.substring(0, ind))).doubleValue();
                                                else
                                                    P2= (new Double(FisLabel.substring(0, FisLabel.length()-1))).doubleValue();
                                                FisLabel= FisLabel.substring(ind + 4);
                                                break;
                                        case 2: if (N_params>3)
                                                    P3= (new Double(FisLabel.substring(0, ind))).doubleValue();
                                                else
                                                    P3= (new Double(FisLabel.substring(0, FisLabel.length()-1))).doubleValue();
                                                FisLabel= FisLabel.substring(ind + 4);
                                                break;
                                        case 3: P4= (new Double(FisLabel.substring(0, FisLabel.length()-1))).doubleValue();
                                                break;
                                  }
                              }
                              Label_Name= ModifyString(Label_Name);
                              String MatlabLabelDef="";
                              if (Label_Def.equals("trapezoidal"))
                                  MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+P1+" "+P2+" "+P3+" "+P4+"]";
                              else if (Label_Def.equals("SemiTrapezoidalInf"))
                                  MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+P1+" "+P1+" "+P2+" "+P3+"]";
                              else if (Label_Def.equals("SemiTrapezoidalSup"))
                                  MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+P1+" "+P2+" "+P3+" "+P3+"]";
                              else if (Label_Def.equals("triangular"))
                                  MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trimf"+"',["+P1+" "+P2+" "+P3+"]";
                              else if (Label_Def.equals("gaussian"))
                                  MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"gaussmf"+"',["+P1+" "+P2+"]";
                              else if (Label_Def.equals("universal"))
                                  MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+P1+" "+P1+" "+P2+" "+P2+"]";
                              else if (Label_Def.equals("discrete"))
                                  MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trimf"+"',["+P1+" "+P1+" "+P1+"]";

                              fOut.println("MF" + String.valueOf(m + 1) + "=" + MatlabLabelDef);
                            }
                         }
                         fOut.println();
                   }
        } else if (l.equals("[Rules]")) {
          fOut.println(l);
          l= lnr.readLine();
          // Change rules format
          while (!l.equals("")) {
            String FisRule= l;
            int ini=0;
            int cont=0;
            String MatlabRule="";
            if (classifOpt[n]) {
                for (int i=0;i<FisRule.length();i++) {
                    if((FisRule.charAt(i))==',') {
                       if (ini==0)
                           MatlabRule= FisRule.substring(ini, i);
                       else
                           MatlabRule= MatlabRule + " " + FisRule.substring(ini, i);

                       cont++;
                       if (cont < Ninputs)
                           ini= i+2;
                       else {
                           ini= i+5;
                           String raux= FisRule.substring(ini, ini+1);
                           //System.out.println("raux="+raux);
                           int rout= (new Integer(raux)).intValue();
                           MatlabRule= MatlabRule +",";
                           for (int k=0; k<nbOutLabels[n]; k++) {
                        	    if (rout==k+1) {
                        	    	MatlabRule= MatlabRule + " " + "2";
                        	    } else {
                        	    	MatlabRule= MatlabRule + " " + "1";
                        	    }
                           }
                           break;
                       }
                    }
               }
               MatlabRule= MatlabRule+" (1) : 1";
            } else {
                for (int i=0;i<FisRule.length();i++) {
                     if((FisRule.charAt(i))==',') {
                        if (ini==0)
                            MatlabRule= FisRule.substring(ini, i);
                        else
                            MatlabRule= MatlabRule + " " + FisRule.substring(ini, i);

                        cont++;
                        if (cont < Ninputs)
                            ini= i+2;
                        else {
                            ini= i+5;
                            break;
                        }
                     }
                }
                MatlabRule= MatlabRule+", "+FisRule.substring(ini, ini+1)+" (1) : 1";
            }
            fOut.println(MatlabRule);
            l= lnr.readLine();
          }
        }
      }
      fOut.flush();
      fOut.close();
      lnr.close();
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Convert GUAJE archive "file_name_GUAJE" to MATLAB archive "file_name_MATLAB".
   * One MATLAB archive is generated for each output from GUAJE archive
   * (file_name_MATLAB_o1 for output 1, file_name_MATLAB_o2 for output 2, ...).
   * @param file_name_GUAJE name of the GUAJE file to be translated
   * @param file_name_MATLAB name of the translated MATLAB file
   * @throws Throwable exception
   */
  public static void GuajeToMatlab (String file_name_GUAJE, String file_name_MATLAB) throws Throwable{
	JKBCT kbct= SpreadKB(file_name_GUAJE);
    int NbInputs= kbct.GetNbInputs();
    int NbOutputs= kbct.GetNbOutputs();
    int NbRules= kbct.GetNbActiveRules();
    String[] OutputTypes= null;
    if (NbOutputs >0) {
        OutputTypes= new String[NbOutputs];
        for (int n=0; n<NbOutputs; n++)
          OutputTypes[n]= kbct.GetOutput(n+1).GetType();
    }
    if (JConvert.conjunction==null || JConvert.disjunction==null || JConvert.defuzzification==null) {
        //System.out.println("setFIS default parameters");
        JConvert.SetFISoptionsDefault(NbOutputs, OutputTypes);
    }
    boolean warning= false;
    if (NbOutputs==0) {
      warning=true;
      NbOutputs++;
    }
    for (int n=0; n<NbOutputs; n++) {
      JKBCTOutput jout= kbct.GetOutput(n+1);
      String file_name= file_name_MATLAB.substring(0, file_name_MATLAB.lastIndexOf("."+("FIS").toLowerCase()));
      if (warning)
        file_name= file_name + "." + ("FIS").toLowerCase();
      else
        file_name= file_name + "_o" + String.valueOf(n+1) + "." + ("FIS").toLowerCase();

      PrintWriter fOut = new PrintWriter(new FileOutputStream(file_name), true);
      fOut.println("[System]");
      fOut.println("Name='"+kbct.GetName()+"'");
      String TypeOutput= "mamdani";
      if (warning) {
          TypeOutput= "mamdani";
          fOut.println("Type='mamdani'");
      } else {
          if (OutputTypes[n].equals("numerical")) {
              TypeOutput= "mamdani";
          } else if ( (OutputTypes[n].equals("categorical")) || (OutputTypes[n].equals("logical")) ) {
              TypeOutput= "sugeno";
          }
      }
      fOut.println("Type='"+TypeOutput+"'");
      fOut.println("NumInputs="+NbInputs);
      fOut.println("NumOutputs="+jout.GetLabelsNumber());
      fOut.println("NumRules="+NbRules);
      fOut.println("AndMethod='"+JConvert.conjunction+"'");
      fOut.println("OrMethod='max'");
      fOut.println("ImpMethod='prod'");
      fOut.println("AggMethod='sum'");
      if (warning)
          fOut.println("DefuzzMethod='centroid'");
        else {
          if (JConvert.defuzzification[n].equals("'area'"))
            fOut.println("DefuzzMethod='centroid'");
          else if (JConvert.defuzzification[n].equals("'MeanMax'"))
            fOut.println("DefuzzMethod='mom'");
          else if (JConvert.defuzzification[n].equals("'sugeno'"))
            fOut.println("DefuzzMethod='wtaver'");
          else {
        	  if (TypeOutput.equals("mamdani"))
                  fOut.println("DefuzzMethod='centroid'");
        	  else
                  fOut.println("DefuzzMethod='wtaver'");
          }
      }
      fOut.println();
      for (int k=0; k<NbInputs; k++) {
    	   JKBCTInput jin= kbct.GetInput(k+1);
           fOut.println("[Input"+String.valueOf(k+1)+"]");
           fOut.println("Name='"+jin.GetName()+"'");
           double[] range= jin.GetInputInterestRange();
           fOut.println("Range=["+range[0]+" "+range[1]+"]");
           int NOL= jin.GetLabelsNumber();
           fOut.println("NumMFs="+NOL);
           String[] labNames;
           if (jin.GetScaleName().equals("user"))
               labNames= jin.GetUserLabelsName();
           else
               labNames= jin.GetLabelsName();
           
           for (int m=0; m<NOL; m++) {
                LabelKBCT lab= jin.GetLabel(m+1);
                double[] params= lab.GetParams();
                int labdef= lab.GetLabelType_Number();
                String Label_Name= labNames[m];
                Label_Name= ModifyString(Label_Name);
                String MatlabLabelDef="";
                if (labdef==LabelKBCT.TRAPEZOIDAL)
                       MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+params[0]+" "+params[1]+" "+params[2]+" "+params[3]+"]";
                else if (labdef==LabelKBCT.SEMITRAPEZOIDALINF)
                       MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+params[0]+" "+params[0]+" "+params[1]+" "+params[2]+"]";
                else if (labdef==LabelKBCT.SEMITRAPEZOIDALSUP)
                       MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+params[0]+" "+params[1]+" "+params[2]+" "+params[2]+"]";
                else if (labdef==LabelKBCT.TRIANGULAR)
                       MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trimf"+"',["+params[0]+" "+params[1]+" "+params[2]+"]";
                else if (labdef==LabelKBCT.GAUSSIAN)
                       MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"gaussmf"+"',["+params[0]+" "+params[1]+"]";
                else if (labdef==LabelKBCT.UNIVERSAL)
                       MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+params[0]+" "+params[0]+" "+params[1]+" "+params[1]+"]";
                else if (labdef==LabelKBCT.DISCRETE)
                       MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trimf"+"',["+params[0]+" "+params[0]+" "+params[0]+"]";

               fOut.println("MF" + String.valueOf(m + 1) + "=" + MatlabLabelDef);
             }
             fOut.println();
      }
      int outNOL= jout.GetLabelsNumber();
      if ( (jout.GetType().equals("logical")) || (jout.GetType().equals("categorical")) ) {
      	  for (int k=0; k<outNOL; k++) {
  		     String out= String.valueOf(k+1);
  		     fOut.println("[Output"+out+"]");
  		     fOut.println("Name='class"+out+"'");
  		     fOut.println("Range=[0 1]");
  		     fOut.println("NumMFs=2");
  		     fOut.println("MF1='val0':'constant',[0]");
  		     fOut.println("MF2='val1':'constant',[1]");
  		     fOut.println();
  	      }
   	   } else {
   		   // numerical output
              for (int k=0; k<outNOL; k++) {
                  fOut.println("[Output"+String.valueOf(k+1)+"]");
                  String outname= jout.GetName()+String.valueOf(k+1);
                  outname= ModifyString(outname);
                  fOut.println("Name='"+outname+"'");
                  double[] range= jout.GetInputInterestRange();
                  fOut.println("Range=["+range[0]+" "+range[1]+"]");
                  int NOL= jout.GetLabelsNumber();
                  fOut.println("NumMFs="+NOL);
                  String[] labNames;
                  if (jout.GetScaleName().equals("user"))
                      labNames= jout.GetUserLabelsName();
                  else
                      labNames= jout.GetLabelsName();
                  
                  for (int m=0; m<NOL; m++) {
                       LabelKBCT lab= jout.GetLabel(m+1);
                       double[] params= lab.GetParams();
                       int labdef= lab.GetLabelType_Number();
                       String Label_Name= labNames[m];
                       Label_Name= ModifyString(Label_Name);
                       String MatlabLabelDef="";
                       if (labdef==LabelKBCT.TRAPEZOIDAL)
                              MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+params[0]+" "+params[1]+" "+params[2]+" "+params[3]+"]";
                       else if (labdef==LabelKBCT.SEMITRAPEZOIDALINF)
                              MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+params[0]+" "+params[0]+" "+params[1]+" "+params[2]+"]";
                       else if (labdef==LabelKBCT.SEMITRAPEZOIDALSUP)
                              MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+params[0]+" "+params[1]+" "+params[2]+" "+params[2]+"]";
                       else if (labdef==LabelKBCT.TRIANGULAR)
                              MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trimf"+"',["+params[0]+" "+params[1]+" "+params[2]+"]";
                       else if (labdef==LabelKBCT.GAUSSIAN)
                              MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"gaussmf"+"',["+params[0]+" "+params[1]+"]";
                       else if (labdef==LabelKBCT.UNIVERSAL)
                              MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+params[0]+" "+params[0]+" "+params[1]+" "+params[1]+"]";
                       else if (labdef==LabelKBCT.DISCRETE)
                              MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trimf"+"',["+params[0]+" "+params[0]+" "+params[0]+"]";

                      fOut.println("MF" + String.valueOf(m + 1) + "=" + MatlabLabelDef);
                    }
                    fOut.println();
              }
   	   }
       fOut.println("[Rules]");
       for (int k=0; k<NbRules; k++) {
           Rule r= kbct.GetRule(k+1);
           if (r.GetActive()) {
             int[] premises= r.Get_in_labels_number();
             int[] conc= r.Get_out_labels_number();
             for (int m=0; m<NbInputs;m++)
               fOut.print(premises[m]+" ");

             fOut.print(",");
             for (int m=0; m<outNOL; m++) {
         	    if (conc[n]==m+1) {
         	    	fOut.print(" " + "2");
         	    } else {
         	    	fOut.print(" " + "1");
         	    }
             }
             fOut.println(" (1) : 1");
           }
         }
      
      /*while((l= lnr.readLine())!=null) {
        if (l.equals("[System]")) {
        } else if (l.startsWith("[Input")){
        } else if (l.equals("[Output"+String.valueOf(n+1)+"]")){
            if (classifOpt[n]) {
            } else {
                fOut.println(l);
                for (int m=0; m<6; m++)
                     lnr.readLine();

                String OutputName= lnr.readLine();
                OutputName= ModifyString(OutputName);
                fOut.println(OutputName);
                // Range: quitar ","
                String FisRange=lnr.readLine();
                int RangeSeparator= FisRange.indexOf(",");
                fOut.println("Range=["+FisRange.substring(10, RangeSeparator)+" "+FisRange.substring(RangeSeparator+4, FisRange.length()-1)+"]");
                int NOL= Integer.parseInt(lnr.readLine().substring(5));
                if ( (NOL==0) && (TypeOutput.equals("sugeno")) ) {
                      NOL= (new Double(FisRange.substring(RangeSeparator+4, FisRange.length()-1))).intValue();
                      fOut.println("NumMFs="+NOL);
                      for (int k=0; k < NOL; k++) {
                           int NbLabelOutput= k+1;
                           fOut.println("MF"+NbLabelOutput+"='output"+NbLabelOutput+"':'constant',["+NbLabelOutput+"]");
                      }
                } else {
                    fOut.println("NumMFs="+NOL);
                    // Hay que cambiar "," por ":"
                    for (int m=0; m<NOL; m++) {
                         String FisLabel= lnr.readLine();
                         String Label_Name="", Label_Def="";
                         double P1=0, P2=0, P3=0, P4=0;
                         int ini= 5;
                         if (m >= 9)
                            ini= 6;

                         for (int k=0; k < 2; k++){
                              int ind=0;
                              for (int i=0;i<FisLabel.length();i++)
                                   if((FisLabel.charAt(i))==',') {
                                       ind=i; break;
                                   }
                                   switch (k) {
                                       case 0: Label_Name = FisLabel.substring(ini, ind - 1);
                                               FisLabel = FisLabel.substring(ind + 2);
                                               break;
                                       case 1: Label_Def = FisLabel.substring(0, ind - 1);
                                               FisLabel = FisLabel.substring(ind + 5);
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

                              for (int k=0; k < N_params; k++) {
                                   int ind=0;
                                   for (int i=0;i<FisLabel.length();i++)
                                        if((FisLabel.charAt(i))==',') {
                                            ind=i; break;
                                        }
                                   switch (k) {
                                        case 0: if (N_params>1)
                                                    P1= (new Double(FisLabel.substring(0, ind))).doubleValue();
                                                else
                                                    P1= (new Double(FisLabel.substring(0, FisLabel.length()-1))).doubleValue();
                                                FisLabel= FisLabel.substring(ind + 4);
                                                break;
                                        case 1: if (N_params>2)
                                                    P2= (new Double(FisLabel.substring(0, ind))).doubleValue();
                                                else
                                                    P2= (new Double(FisLabel.substring(0, FisLabel.length()-1))).doubleValue();
                                                FisLabel= FisLabel.substring(ind + 4);
                                                break;
                                        case 2: if (N_params>3)
                                                    P3= (new Double(FisLabel.substring(0, ind))).doubleValue();
                                                else
                                                    P3= (new Double(FisLabel.substring(0, FisLabel.length()-1))).doubleValue();
                                                FisLabel= FisLabel.substring(ind + 4);
                                                break;
                                        case 3: P4= (new Double(FisLabel.substring(0, FisLabel.length()-1))).doubleValue();
                                                break;
                                  }
                              }
                              Label_Name= ModifyString(Label_Name);
                              String MatlabLabelDef="";
                              if (Label_Def.equals("trapezoidal"))
                                  MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+P1+" "+P2+" "+P3+" "+P4+"]";
                              else if (Label_Def.equals("SemiTrapezoidalInf"))
                                  MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+P1+" "+P1+" "+P2+" "+P3+"]";
                              else if (Label_Def.equals("SemiTrapezoidalSup"))
                                  MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+P1+" "+P2+" "+P3+" "+P3+"]";
                              else if (Label_Def.equals("triangular"))
                                  MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trimf"+"',["+P1+" "+P2+" "+P3+"]";
                              else if (Label_Def.equals("gaussian"))
                                  MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"gaussmf"+"',["+P1+" "+P2+"]";
                              else if (Label_Def.equals("universal"))
                                  MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trapmf"+"',["+P1+" "+P1+" "+P2+" "+P2+"]";
                              else if (Label_Def.equals("discrete"))
                                  MatlabLabelDef="'"+Label_Name+"'"+":"+"'"+"trimf"+"',["+P1+" "+P1+" "+P1+"]";

                              fOut.println("MF" + String.valueOf(m + 1) + "=" + MatlabLabelDef);
                            }
                         }
                         fOut.println();
                   }
        } else if (l.equals("[Rules]")) {
          fOut.println(l);
          l= lnr.readLine();
          // Change rules format
          while (!l.equals("")) {
            String FisRule= l;
            int ini=0;
            int cont=0;
            String MatlabRule="";
            if (classifOpt[n]) {
                for (int i=0;i<FisRule.length();i++) {
                    if((FisRule.charAt(i))==',') {
                       if (ini==0)
                           MatlabRule= FisRule.substring(ini, i);
                       else
                           MatlabRule= MatlabRule + " " + FisRule.substring(ini, i);

                       cont++;
                       if (cont < Ninputs)
                           ini= i+2;
                       else {
                           ini= i+5;
                           String raux= FisRule.substring(ini, ini+1);
                           //System.out.println("raux="+raux);
                           int rout= (new Integer(raux)).intValue();
                           MatlabRule= MatlabRule +",";
                           for (int k=0; k<nbOutLabels[n]; k++) {
                        	    if (rout==k+1) {
                        	    	MatlabRule= MatlabRule + " " + "2";
                        	    } else {
                        	    	MatlabRule= MatlabRule + " " + "1";
                        	    }
                           }
                           break;
                       }
                    }
               }
               MatlabRule= MatlabRule+" (1) : 1";
            } else {
                for (int i=0;i<FisRule.length();i++) {
                     if((FisRule.charAt(i))==',') {
                        if (ini==0)
                            MatlabRule= FisRule.substring(ini, i);
                        else
                            MatlabRule= MatlabRule + " " + FisRule.substring(ini, i);

                        cont++;
                        if (cont < Ninputs)
                            ini= i+2;
                        else {
                            ini= i+5;
                            break;
                        }
                     }
                }
                MatlabRule= MatlabRule+", "+FisRule.substring(ini, ini+1)+" (1) : 1";
            }
            fOut.println(MatlabRule);
            l= lnr.readLine();
          }
        }
      }*/
      fOut.flush();
      fOut.close();
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Convert GUAJE archive "file_name_GUAJE" to Xfuzzy archive "file_name_XFUZZY".
   * One Xfuzzy archive is generated for each output from GUAJE archive
   * @param file_name_GUAJE name of the GUAJE file to be translated
   * @param file_name_XFUZZY name of the translated Xfuzzy file
   * @throws Throwable exception
   */
  public static void GuajeToXfuzzy (String file_name_GUAJE, String file_name_XFUZZY) throws Throwable{
	  //if (file_name_KBCT.endsWith("xml"))
	  //	  file_name_KBCT= file_name_KBCT.substring(0,file_name_KBCT.length()-4);

	  // Read information from file_name_KBCT
      JKBCT kbct= new JKBCT(file_name_GUAJE);
      long ptr= kbct.GetPtr();
      // Write information to file_name_XFUZZY
      PrintWriter fOut = new PrintWriter(new FileOutputStream(file_name_XFUZZY), true);
      //// operatorset
      fOut.println("operatorset opset {");
      if (JConvert.conjunction!=null) {
          if (JConvert.conjunction.equals("min"))
              fOut.println(" and xfl.min();");
          else if (JConvert.conjunction.equals("prod"))
              fOut.println(" and xfl.prod();");
          else if (JConvert.conjunction.equals("luka"))
              fOut.println(" and xfl.lukasiewicz();");
          else
              fOut.println(" and xfl.min();");
      } else
         fOut.println(" and xfl.min();");

      int Noutputs= kbct.GetNbOutputs();
      if (Noutputs==1) {
          if ( (JConvert.disjunction!=null) && (JConvert.disjunction[0]!=null) ) {
              if (JConvert.disjunction[0].equals("max"))
                fOut.println(" or xfl.max();");
              else if (JConvert.disjunction[0].equals("sum"))
                fOut.println(" or xfl.sum();");
              else
                fOut.println(" or xfl.max();");
          } else
              fOut.println(" or xfl.max();");

          if ( (JConvert.defuzzification!=null) && (JConvert.defuzzification[0]!=null) ) {
              if (JConvert.defuzzification[0].equals("area"))
                  fOut.println(" defuz xfl.CenterOfArea();");
              else if (JConvert.defuzzification[0].equals("sugeno"))
                fOut.println(" defuz xfl.TakagiSugeno();");
              else
                fOut.println(" defuz xfl.MaxLabel();");
          } else
              fOut.println(" defuz xfl.MaxLabel();");
      } else {
          if (JConvert.conjunction!=null) {
              if (JConvert.conjunction.equals("min"))
                  fOut.println(" or xfl.max();");
              else if (JConvert.conjunction.equals("prod"))
                  fOut.println(" or xfl.sum();");
              else if (JConvert.conjunction.equals("luka"))
                  fOut.println(" or xfl.lukasiewicz();");
              else
                  fOut.println(" or xfl.max();");
          } else
             fOut.println(" or xfl.max();");

          fOut.println(" defuz xfl.MaxLabel();");
      }
      fOut.println("}");
      fOut.println();
      //// inputs and outputs
      int Ninputs= kbct.GetNbInputs();
      String[] v_names= new String[Ninputs+Noutputs];
      for (int n=0; n<Ninputs+Noutputs; n++) {
        variable v= null;
        if (n < Ninputs)
            v= kbct.GetInput(n+1).GetV();
        else
            v= kbct.GetOutput(n+1-Ninputs).GetV();

        v_names[n]= v.GetName();
        v_names[n]= ModifyString(v_names[n]);
        fOut.println("type T"+ v_names[n] + "[" + v.GetInputInterestRange()[0] + "," + v.GetInputInterestRange()[1] + "] {");
        int Nlabels= v.GetLabelsNumber();
        for (int k=0; k<Nlabels; k++) {
          LabelKBCT l= v.GetLabel(k+1);
          String LabelName= ModifyLabelName(v, k);
          fOut.print(" l_" + LabelName);
          String mf_name= l.GetName();
          if ( mf_name.equals("triangular") ) {
              fOut.println(" xfl.triangle("+l.GetP1()+","+l.GetP2()+","+l.GetP3()+");");
          } else if ( mf_name.equals("SemiTrapezoidalInf") ) {
              if (l.GetP1()==l.GetP2())
                fOut.println(" xfl.triangle("+(l.GetP1()-1)+","+l.GetP2()+","+l.GetP3()+");");
              else
                fOut.println(" xfl.trapezoid("+(l.GetP1()-1)+","+l.GetP1()+","+l.GetP2()+","+l.GetP3()+");");
          } else if ( mf_name.equals("SemiTrapezoidalSup") ) {
              fOut.println(" xfl.slope("+l.GetP1()+","+(1/(l.GetP2()-l.GetP1()))+");");
          } else if ( mf_name.equals("trapezoidal") ) {
              fOut.println(" xfl.trapezoid("+l.GetP1()+","+l.GetP2()+","+l.GetP3()+","+l.GetP4()+");");
          } else if ( mf_name.equals("universal") ) {
              fOut.println(" xfl.rectangle("+l.GetP1()+","+l.GetP2()+");");
          } else if ( mf_name.equals("gaussian") ) {
              fOut.println(" xfl.bell("+l.GetP1()+","+l.GetP2()+");");
          } else if ( mf_name.equals("discrete") ) {
              fOut.println(" xfl.singleton("+l.GetP1()+");");
          }
        }
        fOut.println("}");
        fOut.println();
      }
      //// rules
      File f= new File(file_name_GUAJE);
      String RB_name= f.getName().substring(0,f.getName().length()-3).replace(' ','_');
      int Nrules= kbct.GetNbRules();
      if (Nrules > 0) {
        RB_name= RB_name.replace('.','_');
        RB_name= RB_name.replace('-','_');
        fOut.print("rulebase " + RB_name + " (");
        for (int n=0; n<v_names.length; n++) {
          if (n==Ninputs-1)
            fOut.print("T" + v_names[n] + " " + v_names[n] + " : ");
          else if (n==Ninputs+Noutputs-1)
            fOut.println("T" + v_names[n] + " " + v_names[n] + ") using opset {");
          else
            fOut.print("T" + v_names[n] + " " + v_names[n] + ", ");
        }
        for (int n=0; n<Nrules; n++) {
            Rule r= kbct.GetRule(n+1);
            if (r.GetActive()) {
              int[] antecedent= r.Get_in_labels_number();
              int[] consecuent= r.Get_out_labels_number();
              fOut.print("if(");
              for (int k=0; k<antecedent.length; k++) {
                variable v= kbct.GetInput(k+1).GetV();
                int NOL= v.GetLabelsNumber();
                int l_number= antecedent[k];
                if (l_number > NOL) {
                  if (l_number<= 2*NOL) {
                    // NOT
                    String LabelName= ModifyLabelName(v, l_number-NOL-1);
                    fOut.print(v_names[k]+" != l_"+LabelName);
                    if (k!=antecedent.length-1) {
                      for (int i=k+1; i<antecedent.length; i++) {
                        if (antecedent[i]!=0) {
                          fOut.print(" & ");
                          break;
                        }
                      }
                    }
                  } else {
                    // OR
                    int SelLabel= l_number-2*NOL;
                    int NbLabelsOR= jnikbct.NbORLabels(SelLabel, NOL);
                    int option= jnikbct.option(SelLabel, NbLabelsOR, NOL);
                    String LabelName= ModifyLabelName(v, option-1);
                    fOut.print("( "+v_names[k]+" == l_"+LabelName);
                    for (int m=0; m<NbLabelsOR-1; m++) {
                      LabelName= ModifyLabelName(v, option+m);
                      fOut.print(" | "+v_names[k]+" == l_" +LabelName);
                    }
                    fOut.print(" ) ");
                    if (k!=antecedent.length-1) {
                      for (int i=k+1; i<antecedent.length; i++) {
                        if (antecedent[i]!=0) {
                          fOut.print(" & ");
                          break;
                        }
                      }
                    }
                  }
                }
                else if (l_number>0) {
                  // normal label
                  String LabelName= ModifyLabelName(v, l_number-1);
                  fOut.print(v_names[k]+" == l_"+LabelName);
                  if (k!=antecedent.length-1) {
                    for (int i=k+1; i<antecedent.length; i++) {
                      if (antecedent[i]!=0) {
                        fOut.print(" & ");
                        break;
                      }
                    }
                  }
                }
                if (k==antecedent.length-1)
                  fOut.print(") -> ");
              }
              for (int k=0; k<consecuent.length; k++) {
                variable v= kbct.GetOutput(k+1).GetV();
                int NOL= v.GetLabelsNumber();
                int l_number= consecuent[k];
                if (l_number > NOL) {
                  if (l_number<= 2*NOL) {
                    // NOT
                    String LabelName= ModifyLabelName(v, l_number-NOL-1);
                    fOut.print(v_names[k+Ninputs]+" != l_"+LabelName);
                    if (k!=consecuent.length-1) {
                      for (int i=k+1; i<consecuent.length; i++) {
                        if (consecuent[i]!=0) {
                          fOut.print(" , ");
                          break;
                        }
                      }
                    }
                  } else {
                    // OR
                    int SelLabel= l_number-2*NOL;
                    int NbLabelsOR= jnikbct.NbORLabels(SelLabel, NOL);
                    int option= jnikbct.option(SelLabel, NbLabelsOR, NOL);
                    String LabelName= ModifyLabelName(v, option-1);
                    fOut.print("( "+v_names[k+Ninputs]+" = l_"+LabelName);
                    for (int m=0; m<NbLabelsOR-1; m++) {
                      LabelName= ModifyLabelName(v, option+m);
                      fOut.print(" | "+v_names[k]+" = l_" +LabelName);
                    }
                    fOut.print(" ) ");
                    if (k!=consecuent.length-1) {
                      for (int i=k+1; i<consecuent.length; i++) {
                        if (consecuent[i]!=0) {
                          fOut.print(" , ");
                          break;
                        }
                      }
                    }
                  }
                } else if (l_number>0) {
                  // normal label
                  String LabelName= ModifyLabelName(v, l_number-1);
                  fOut.print(v_names[k+Ninputs]+" = l_"+LabelName);
                  if (k!=consecuent.length-1) {
                    for (int i=k+1; i<consecuent.length; i++) {
                      if (consecuent[i]!=0) {
                        fOut.print(" , ");
                        break;
                      }
                    }
                  }
                }
                if (k==consecuent.length-1)
                  fOut.println(";");
              }
            }
        }
        fOut.println("}");
      }
      //// system
      fOut.print("system (");
      for (int n=0; n<v_names.length; n++) {
        if (n==Ninputs-1)
          fOut.print("T" + v_names[n] + " " + v_names[n] + " : ");
        else if (n==Ninputs+Noutputs-1)
          fOut.println("T" + v_names[n] + " " + v_names[n] + ") {");
        else
          fOut.print("T" + v_names[n] + " " + v_names[n] + ", ");
      }
      if (Nrules > 0) {
        fOut.print(" "+RB_name+"(");
        for (int n=0; n<v_names.length; n++) {
          if (n==Ninputs-1)
            fOut.print(v_names[n] + " : ");
          else if (n==Ninputs+Noutputs-1)
            fOut.println(v_names[n] + ");");
          else
            fOut.print(v_names[n] + ", ");
        }
      }
      fOut.println("}");
      fOut.flush();
      fOut.close();
      kbct.Close();
      kbct.Delete();
      jnikbct.DeleteKBCT(ptr+1);
  }
  //----------------------------------------------------------------------------
  private static String ModifyLabelName (variable v, int option) {
    String LabelName= v.GetLabelsName()[option];
    LabelName= ModifyString(LabelName);
    return LabelName;
  }
  //----------------------------------------------------------------------------
  private static String ModifyString (String s) {
    s= s.replace(' ','_');
    s= s.replace('/','_');
    s= s.replace('(','_');
    s= s.replace(')','_');
    s= s.replace(':','_');
    s= s.replace('-','_');
    s= s.replace('.','_');
    return s;
  }
  //----------------------------------------------------------------------------
  /**
   * Expand the expert KB.
   * Change NOT/OR labels.
   * @param file_name_KBCT name of the KBCT file whose rule base is going to be expanded
   * @return JKBCT object with expanded rule base
   * @throws Throwable exception
   */
  public static JKBCT SpreadKB (String file_name_KBCT) throws Throwable {
    // La parte comentada sirve para hacer la extensi�n en las salidas
    JKBCT kbct= new JKBCT(file_name_KBCT);
    int NbInputs= kbct.GetNbInputs();
    int NbOutputs= kbct.GetNbOutputs();
    int NbRules= kbct.GetNbRules();
    int[] NbOL= new int[NbInputs+NbOutputs];
    for (int k=0; k < NbInputs; k++)
         NbOL[k]= kbct.GetInput(k+1).GetLabelsNumber();

    for (int k=0; k < NbOutputs; k++)
         NbOL[k+NbInputs]= kbct.GetOutput(k+1).GetLabelsNumber();

    int n=0;
    int lim= NbRules;
    int iniIn= 0;
    //int iniOut= 0;
    int NbR= 1;
    while (n<lim) {
      Rule r = kbct.GetRule(NbR);
      if (r.GetActive()) {
        int[] premises = r.Get_in_labels_number();
        //int[] conc = r.Get_out_labels_number();
        int[] premises_exp = r.Get_in_labels_exp();
        //int[] conc_exp = r.Get_out_labels_exp();
        //boolean flag= false;
        for (int m = iniIn; m < NbInputs; m++) {
           int NOL= NbOL[m];
           if ( (premises_exp[m]==0) && (premises[m] > NOL) ) {
        	 //System.out.println("n="+n);
        	 //System.out.println("m="+m);
        	 //System.out.println(" -> htsize="+jnikbct.getHashtableSize());
        	 long ptr1= kbct.GetPtr();
             if (premises[m] <= 2*NOL) {
                 kbct= ModifyRuleBaseNOT(kbct,NbR,m, NOL, premises[m]-NOL /*, "Input"*/);
             } else {
                 kbct= ModifyRuleBaseOR(kbct,NbR,m, NOL, premises[m]-2*NOL /*, "Input"*/);
             }
        	 long ptr2= kbct.GetPtr();
        	 //System.out.println(" -> ptr1="+ptr1+"  ptr2="+ptr2);
        	 for (long i=ptr1; i<ptr2; i++) {
        		  jnikbct.DeleteKBCT(i);
        	 }
        	 //System.out.println(" -> htsize="+jnikbct.getHashtableSize());
             n--;
             iniIn= m+1;
             m= NbInputs;
             //flag= true;
           }
        }
      /*if (!flag)
        for (int m = iniOut; m < NbOutputs; m++) {
           int NOL= NbOL[m+NbInputs];
           if ( (conc_exp[m]==0) && (conc[m] > NOL) ) {
             if (conc[m] <= 2*NOL) {
                 kbct= ModifyRuleBaseNOT(kbct,NbR,m, NOL, conc[m]-NOL, "Output");
             } else {
                 kbct= ModifyRuleBaseOR(kbct,NbR,m, NOL, conc[m]-2*NOL, "Output");
             }
             n--;
             iniOut= m+1;
             m= NbOutputs;
           }
        }*/
      }
      n++;
      if (n==NbR) {
         iniIn=0;
         //iniOut=0;
         NbR= NbR+1;
      }
      lim= kbct.GetNbRules();
    }
    return kbct;
  }
  //----------------------------------------------------------------------------
  /**
   * Expand rules with NOT.
   * @param kbct initial knowledge base
   * @param NbRule number of the rule to be expanded
   * @param NbVar number of the variable
   * @param NbLabels number of labels
   * @param SelLabel number of selected label
   * @return JKBCT object with expanded rule base
   */
  private static JKBCT ModifyRuleBaseNOT (JKBCT kbct, int NbRule, int NbVar, int NbLabels, int SelLabel /*, String VarType*/) {
     // La parte comentada sirve para hacer la extensi�n en las salidas
     // * @param VarType type of the variable
     JKBCT result= new JKBCT(kbct);
     int NbRules= result.GetNbRules();
     // borrar reglas por encima
     for (int n= NbRules; n >= NbRule; n--)
       result.RemoveRule(n-1);

     // crear nuevas etiquetas y reglas
     //int NbNewRules= 2;
     int NOTLabel= SelLabel;

     JVariable v= null;
     //if (VarType.equals("Input")) {
         v= result.GetInput(NbVar + 1);
     //}
     /*else if (VarType.equals("Output")) {
         v= result.GetOutput(NbVar + 1);
     }*/
     if (!v.GetType().equals("numerical")) {
       result= null;
       v= null;
       return ModifyRuleBaseNOTCategorical(kbct, NbRule, NbVar, NbLabels, SelLabel /*, VarType*/);
     } else {
       return ModifyRuleBaseNOTNumerical(result, kbct, v, NOTLabel, NbRule, NbVar, NbLabels /*, VarType*/);
     }
  }
  //----------------------------------------------------------------------------
  /**
   * Expand rules with NOT categorical labels.
   * @param kbct initial knowledge base
   * @param NbRule number of the rule to be expanded
   * @param NbVar number of the variable
   * @param NbLabels number of labels
   * @param SelLabel number of selected label
   * @return JKBCT object with expanded rule base
   */
  private static JKBCT ModifyRuleBaseNOTCategorical (JKBCT kbct, int NbRule, int NbVar, int NbLabels, int SelLabel /*, String VarType*/) {
     // La parte comentada sirve para hacer la extensi�n en las salidas
     // * @param VarType type of the variable
     JKBCT result= new JKBCT(kbct);
     int NbRules= result.GetNbRules();
     // borrar reglas por encima
     for (int n= NbRules; n >= NbRule; n--)
       result.RemoveRule(n-1);

     // crear nuevas etiquetas y reglas
     int NbNewRules= NbLabels -1;
     for (int n=0; n<NbNewRules; n++) {
         int label= 0;
         if (n < SelLabel-1)
            label= n+1;
         else if (n >= SelLabel-1)
            label= n+2;
          Rule r= kbct.GetRule(NbRule);

          //if (VarType.equals("Input")) {
              int[] InputsLabels= new int[r.GetNbInputs()];
              for (int m=0; m<InputsLabels.length; m++) {
                if (m==NbVar)
                  InputsLabels[m]= label;
                else
                  InputsLabels[m] = r.Get_in_labels_number()[m];
              }
              Rule rnew= new Rule(NbRule+n, r.GetNbInputs(), r.GetNbOutputs(), InputsLabels, r.Get_out_labels_number(), r.GetType(), r.GetActive());
              int[] in_labels_exp= rnew.Get_in_labels_exp();
              for (int m=0; m<NbVar+1; m++)
                in_labels_exp[m]= 1;
              rnew.Set_in_labels_exp(in_labels_exp);
              result.AddRule(rnew);
          //}
          /*else if (VarType.equals("Output")) {
              int[] OutputsLabels= new int[r.GetNbOutputs()];
              for (int m=0; m<OutputsLabels.length; m++) {
                if (m==NbVar)
                  OutputsLabels[m]= label;
                else
                  OutputsLabels[m] = r.Get_out_labels_number()[m];
              }

              Rule rnew= new Rule(NbRule+n, r.GetNbInputs(), r.GetNbOutputs(), r.Get_in_labels_number(), OutputsLabels, r.GetType());
              int[] in_labels_exp= rnew.Get_in_labels_exp();
              for (int m=0; m<in_labels_exp.length; m++)
                in_labels_exp[m]= 1;
              rnew.Set_in_labels_exp(in_labels_exp);
              int[] out_labels_exp= rnew.Get_out_labels_exp();
              for (int m=0; m<NbVar+1; m++)
                out_labels_exp[m]= 1;
              rnew.Set_out_labels_exp(out_labels_exp);
              result.AddRule(rnew);
          }*/
     }
     // a�adir reglas
     for (int n=NbRule+1; n < kbct.GetNbRules()+1; n++) {
          Rule rnew= kbct.GetRule(n);
          rnew.SetNumber(result.GetNbRules()+1);
          result.AddRule(rnew);
     }
     return result;
  }
  //----------------------------------------------------------------------------
  /**
   * Expand rules with NOT numerical labels.
   * @param result knowledge base to be returned after the changes
   * @param kbct initial knowledge base
   * @param v JVariable
   * @param NOTLabel number of selected label
   * @param NbRule number of the rule to be expanded
   * @param NbVar number of the variable
   * @param NbLabels number of labels
   * @return JKBCT object with expanded rule base (result)
   */
  private static JKBCT ModifyRuleBaseNOTNumerical (JKBCT result, JKBCT kbct, JVariable v, int NOTLabel, int NbRule, int NbVar, int NbLabels /*, String VarType*/) {
     // La parte comentada sirve para hacer la extensi�n en las salidas
     // * @param VarType type of the variable
     variable var= v.GetV();
     LabelKBCT e= v.GetLabel(NOTLabel);
     int LabelNumber= v.GetLabelsNumber() + 1;
     LabelKBCT[] labels= new LabelKBCT[2];
     String L1= null;
     String L2= null;
     int NbNewRules= 2;
     if (NOTLabel==1) {
         NbNewRules= 1;
         //L1= "Bigger Than Label "+NOTLabel;
         if (var.GetFlagModify())
            L1= LocaleKBCT.GetString("NOT")+"("+v.GetLabelsName(NOTLabel-1)+")";
         else if (v.GetScaleName().equals("user"))
            L1= LocaleKBCT.GetString("NOT")+"("+v.GetUserLabelsName(NOTLabel-1)+")";
         else
            L1= LocaleKBCT.GetString("NOT")+"("+LocaleKBCT.GetString(v.GetLabelsName(NOTLabel-1))+")";
     } else if (NOTLabel==NbLabels) {
         NbNewRules= 1;
         //L1= "Smaller Than Label "+NOTLabel;
         if (var.GetFlagModify())
            L1= LocaleKBCT.GetString("NOT")+"("+v.GetLabelsName(NOTLabel-1)+")";
         else if (v.GetScaleName().equals("user"))
            L1= LocaleKBCT.GetString("NOT")+"("+v.GetUserLabelsName(NOTLabel-1)+")";
         else
            L1= LocaleKBCT.GetString("NOT")+"("+LocaleKBCT.GetString(v.GetLabelsName(NOTLabel-1))+")";
     } else {
         NbNewRules= 2;
         if (var.GetFlagModify()) {
            L1= LocaleKBCT.GetString("SmallerThan")+"("+v.GetLabelsName(NOTLabel-1)+")";
            L2= LocaleKBCT.GetString("BiggerThan")+"("+v.GetLabelsName(NOTLabel-1)+")";
         } else if (v.GetScaleName().equals("user")) {
            L1= LocaleKBCT.GetString("SmallerThan")+"("+v.GetUserLabelsName(NOTLabel-1)+")";
            L2= LocaleKBCT.GetString("BiggerThan")+"("+v.GetUserLabelsName(NOTLabel-1)+")";
         } else {
            L1= LocaleKBCT.GetString("SmallerThan")+"("+LocaleKBCT.GetString(v.GetLabelsName(NOTLabel-1))+")";
            L2= LocaleKBCT.GetString("BiggerThan")+"("+LocaleKBCT.GetString(v.GetLabelsName(NOTLabel-1))+")";
         }
     }
     if (!ExistLabel(var, L1)) {
         if (NOTLabel==1) {
             labels[0]= BiggerThan(e, v.GetInputInterestRange()[1], LabelNumber);
             v.AddLabel(labels[0]);
         } else if (NOTLabel==NbLabels) {
             labels[0]= SmallerThan(e, v.GetInputInterestRange()[0], LabelNumber);
             v.AddLabel(labels[0]);
         } else {
           labels[0]= SmallerThan(e, v.GetInputInterestRange()[0], LabelNumber);
           v.AddLabel(labels[0]);
           labels[1]= BiggerThan(e, v.GetInputInterestRange()[1], LabelNumber+1);
           v.AddLabel(labels[1]);
         }
         String[] LabelsNames= new String[v.GetLabelsNumber()];
         for (int n=0; n<LabelsNames.length; n++)
          if (L2 != null) {
            if (n==LabelsNames.length-2)
                 LabelsNames[n]= L1;
            else if (n==LabelsNames.length-1)
                 LabelsNames[n]= L2;
            else if (var.GetFlagModify())
                 LabelsNames[n]= v.GetLabelsName(n);
            else if (v.GetScaleName().equals("user"))
                 LabelsNames[n]= v.GetUserLabelsName(n);
            else
                 LabelsNames[n]= LocaleKBCT.GetString(v.GetLabelsName(n));
          } else {
              if (n==LabelsNames.length-1)
                   LabelsNames[n]= L1;
              else if (var.GetFlagModify())
                   LabelsNames[n]= v.GetLabelsName(n);
              else if (v.GetScaleName().equals("user"))
                   LabelsNames[n]= v.GetUserLabelsName(n);
              else
                   LabelsNames[n]= LocaleKBCT.GetString(v.GetLabelsName(n));
          }
         var.InitLabelsName(LabelsNames.length);
         var.SetFlagModify(true);
         for (int n=0; n<LabelsNames.length; n++)
           var.SetLabelsName(n+1, LabelsNames[n]);

       //if (VarType.equals("Input"))
         jnikbct.ReplaceInput(result.GetPtr(), NbVar+1, var);
       //else if (VarType.equals("Output"))
         //jnikbct.ReplaceOutput(result.GetPtr(), NbVar+1, var);
     } else {
         LabelNumber= NumberOfLabel(var, L1);
     }
     var= null;
     for (int n=0; n<NbNewRules; n++) {
        Rule r= kbct.GetRule(NbRule);
        //if (VarType.equals("Input")) {
            int[] InputsLabels= new int[r.GetNbInputs()];
            for (int m=0; m<InputsLabels.length; m++) {
              if (m==NbVar)
                InputsLabels[m]= LabelNumber+n;
              else
                InputsLabels[m] = r.Get_in_labels_number()[m];
            }
            Rule rnew= new Rule(NbRule+n, r.GetNbInputs(), r.GetNbOutputs(), InputsLabels, r.Get_out_labels_number(), r.GetType(), r.GetActive());
            int[] in_labels_exp= rnew.Get_in_labels_exp();
            for (int m=0; m<NbVar+1; m++)
              in_labels_exp[m]= 1;
            rnew.Set_in_labels_exp(in_labels_exp);
            result.AddRule(rnew);
        //}
        /*else if (VarType.equals("Output")) {
            int[] OutputsLabels= new int[r.GetNbOutputs()];
            for (int m=0; m<OutputsLabels.length; m++) {
              if (m==NbVar)
                OutputsLabels[m]= LabelNumber+n;
              else
                OutputsLabels[m] = r.Get_out_labels_number()[m];
            }

            Rule rnew= new Rule(NbRule+n, r.GetNbInputs(), r.GetNbOutputs(), r.Get_in_labels_number(), OutputsLabels, r.GetType());
            int[] in_labels_exp= rnew.Get_in_labels_exp();
            for (int m=0; m<in_labels_exp.length; m++)
              in_labels_exp[m]= 1;
            rnew.Set_in_labels_exp(in_labels_exp);
            int[] out_labels_exp= rnew.Get_out_labels_exp();
            for (int m=0; m<NbVar+1; m++)
              out_labels_exp[m]= 1;
            rnew.Set_out_labels_exp(out_labels_exp);
            result.AddRule(rnew);
        }*/
   }
   // a�adir reglas
   for (int n=NbRule+1; n < kbct.GetNbRules()+1; n++) {
        Rule rnew= kbct.GetRule(n);
        rnew.SetNumber(result.GetNbRules()+1);
        result.AddRule(rnew);
   }
   return result;
  }
  //----------------------------------------------------------------------------
  /**
   * Expand rules with OR.
   * @param kbct initial knowledge base
   * @param NbRule number of the rule to be expanded
   * @param NbVar number of the variable
   * @param NbLabels number of labels
   * @param SelLabel number of selected label
   * @return JKBCT object with expanded rule base
   */
  private static JKBCT ModifyRuleBaseOR (JKBCT kbct, int NbRule, int NbVar, int NbLabels, int SelLabel /*, String VarType*/) {
     // La parte comentada sirve para hacer la extensi�n en las salidas
     // * @param VarType type of the variable
     JKBCT result= new JKBCT(kbct);
     int NbRules= result.GetNbRules();
     // borrar reglas por encima
     for (int n= NbRules; n >= NbRule; n--)
       result.RemoveRule(n-1);

     // crear nuevas reglas
     int NbLabelsOR= jnikbct.NbORLabels(SelLabel, NbLabels);
     int option= jnikbct.option(SelLabel, NbLabelsOR, NbLabels);
     JVariable v= null;
     //if (VarType.equals("Input")) {
         v= result.GetInput(NbVar + 1);
     //} else if (VarType.equals("Output")) {
        // v= result.GetOutput(NbVar + 1);
     //}
     if (!v.GetType().equals("numerical")) {
       result= null;
       v= null;
       return ModifyRuleBaseORCategorical(kbct, NbRule, NbVar, NbLabelsOR, option /*, VarType*/);
     } else {
       return ModifyRuleBaseORNumerical(result, kbct, v, SelLabel, NbLabelsOR, NbLabels, NbRule, NbVar, option /*, VarType*/);
     }
  }
  //----------------------------------------------------------------------------
  /**
   * Expand rules with OR categorical labels.
   * @param kbct initial knowledge base
   * @param NbRule number of the rule to be expanded
   * @param NbVar number of the variable
   * @param NbNewRules number of expanded rules
   * @param option number of the first label in the composite OR label
   * @return JKBCT object with expanded rule base
   */
  private static JKBCT ModifyRuleBaseORCategorical (JKBCT kbct, int NbRule, int NbVar, int NbNewRules, int option /*String VarType*/) {
     // La parte comentada sirve para hacer la extensi�n en las salidas
     // * @param VarType type of the variable
     JKBCT result= new JKBCT(kbct);
     int NbRules= result.GetNbRules();
     // borrar reglas por encima
     for (int n= NbRules; n >= NbRule; n--)
       result.RemoveRule(n-1);

     for (int n=0; n<NbNewRules; n++) {
       Rule r= kbct.GetRule(NbRule);
       int label= option + n;
       //if (VarType.equals("Input")) {
           int[] InputsLabels= new int[r.GetNbInputs()];
           for (int m=0; m<InputsLabels.length; m++) {
             if (m==NbVar)
               InputsLabels[m]= label;
             else
               InputsLabels[m] = r.Get_in_labels_number()[m];
           }
           Rule rnew= new Rule(NbRule+n, r.GetNbInputs(), r.GetNbOutputs(), InputsLabels, r.Get_out_labels_number(), r.GetType(), r.GetActive());
           int[] in_labels_exp= rnew.Get_in_labels_exp();
           for (int m=0; m<NbVar+1; m++)
                in_labels_exp[m]= 1;

           rnew.Set_in_labels_exp(in_labels_exp);
           result.AddRule(rnew);
       //}
       /*else if (VarType.equals("Output")) {
           int[] OutputsLabels= new int[r.GetNbOutputs()];
           for (int m=0; m<OutputsLabels.length; m++) {
             if (m==NbVar)
               OutputsLabels[m]= label;
             else
               OutputsLabels[m] = r.Get_out_labels_number()[m];
           }

           Rule rnew= new Rule(NbRule+n, r.GetNbInputs(), r.GetNbOutputs(), r.Get_in_labels_number(), OutputsLabels, r.GetType());
           int[] out_labels_exp= rnew.Get_out_labels_exp();
           for (int m=0; m<NbVar+1; m++)
             out_labels_exp[m]= 1;
           rnew.Set_out_labels_exp(out_labels_exp);
           result.AddRule(rnew);
         }*/
       }
     // a�adir reglas
     for (int n=NbRule+1; n < kbct.GetNbRules()+1; n++) {
          Rule rnew= kbct.GetRule(n);
          rnew.SetNumber(result.GetNbRules()+1);
          result.AddRule(rnew);
     }
     return result;
  }
  //----------------------------------------------------------------------------
  /**
   * Expand rules with OR categorical labels.
   * @param result knowledge base to be returned after the changes
   * @param kbct initial knowledge base
   * @param v JVariable
   * @param SelLabel number of selected label
   * @param NbLabelsOR number of basic labels in the composite OR label
   * @param NbLabels number of labels in the variable v
   * @param NbRule number of the rule to be expanded
   * @param NbVar number of the variable
   * @param option number of the first label in the composite OR label
   * @return JKBCT object with expanded rule base (result)
   */
  private static JKBCT ModifyRuleBaseORNumerical (JKBCT result, JKBCT kbct, JVariable v, int SelLabel, int NbLabelsOR, int NbLabels, int NbRule, int NbVar, int option /*, String VarType*/) {
     // La parte comentada sirve para hacer la extensi�n en las salidas
     // * @param VarType type of the variable
     variable var= v.GetV();
     int LabelNumber= v.GetLabelsNumber() + 1;
     String LabelName= v.GetORLabelsName(SelLabel-1);
     if (!ExistLabel(var, LabelName)) {
       LabelKBCT e_ini= v.GetLabel(option);
       LabelKBCT e_fin= v.GetLabel(option+NbLabelsOR-1);
       LabelKBCT e_new= ORLabel(e_ini, e_fin, LabelNumber, NbLabels);
       v.AddLabel(e_new);
       String[] LabelsNames= new String[v.GetLabelsNumber()];
       for (int n=0; n<LabelsNames.length; n++)
          if (n==LabelsNames.length-1)
             LabelsNames[n]= LabelName;
          else if (var.GetFlagModify())
             LabelsNames[n]= v.GetLabelsName(n);
          else if (v.GetScaleName().equals("user"))
             LabelsNames[n]= v.GetUserLabelsName(n);
          else
             LabelsNames[n]= LocaleKBCT.GetString(v.GetLabelsName(n));

       var.SetFlagModify(true);
       var.InitLabelsName(LabelsNames.length);
       for (int n=0; n<LabelsNames.length; n++)
         var.SetLabelsName(n+1, LabelsNames[n]);

       //if (VarType.equals("Input"))
           jnikbct.ReplaceInput(result.GetPtr(), NbVar+1, var);
       //else if (VarType.equals("Output"))
           //jnikbct.ReplaceOutput(result.GetPtr(), NbVar+1, var);
     } else {
         LabelNumber= NumberOfLabel(var, LabelName);
     }
     var= null;
     Rule r= kbct.GetRule(NbRule);
     //if (VarType.equals("Input")) {
           int[] InputsLabels= new int[r.GetNbInputs()];
           for (int m=0; m<InputsLabels.length; m++) {
             if (m==NbVar)
               InputsLabels[m]= LabelNumber;
             else
               InputsLabels[m] = r.Get_in_labels_number()[m];
           }
           Rule rnew= new Rule(NbRule, r.GetNbInputs(), r.GetNbOutputs(), InputsLabels, r.Get_out_labels_number(), r.GetType(), r.GetActive());
           int[] in_labels_exp= rnew.Get_in_labels_exp();
           for (int m=0; m<NbVar+1; m++)
                in_labels_exp[m]= 1;

           rnew.Set_in_labels_exp(in_labels_exp);
           result.AddRule(rnew);
     //}
     /*else if (VarType.equals("Output")) {
           int[] OutputsLabels= new int[r.GetNbOutputs()];
           for (int m=0; m<OutputsLabels.length; m++) {
             if (m==NbVar)
               OutputsLabels[m]= LabelNumber;
             else
               OutputsLabels[m] = r.Get_out_labels_number()[m];
           }

           Rule rnew= new Rule(NbRule, r.GetNbInputs(), r.GetNbOutputs(), r.Get_in_labels_number(), OutputsLabels, r.GetType());
           int[] out_labels_exp= rnew.Get_out_labels_exp();
           for (int m=0; m<NbVar+1; m++)
             out_labels_exp[m]= 1;
           rnew.Set_out_labels_exp(out_labels_exp);
           result.AddRule(rnew);
     }*/
   // a�adir reglas
   for (int n=NbRule+1; n < kbct.GetNbRules()+1; n++) {
        Rule r_new= kbct.GetRule(n);
        r_new.SetNumber(result.GetNbRules()+1);
        result.AddRule(r_new);
   }
   return result;
  }
  //----------------------------------------------------------------------------
  /**
   * Expand the expert KB (only used in quality evaluation).
   * Change NOT/OR labels.
   * @param file_name_KBCT name of the KBCT file with the initial knowledge base
   * @return JKBCT expanded
   * @throws Throwable exception
   */
  public static JKBCT SpreadKBquality (String file_name_KBCT) throws Throwable {
    JKBCT kbct= new JKBCT(file_name_KBCT);
    // Spread rules
    int NbRules= kbct.GetNbRules();
    Rule[] RRaux= new Rule[kbct.GetNbActiveRules()];
    int contActR=0;
    for (int n=0; n<NbRules; n++) {
      Rule r= kbct.GetRule(n+1);
      if (r.GetActive())
         RRaux[contActR++]= r;
    }
    int NbInputs= kbct.GetNbInputs();
    int NbOutputs= kbct.GetNbOutputs();
    int[] NbOL= new int[NbInputs+NbOutputs];
    String[] VarType= new String[NbOL.length];
    for (int k=0; k < NbInputs; k++) {
         JKBCTInput jin=kbct.GetInput(k+1);
         NbOL[k]= jin.GetLabelsNumber();
         VarType[k]= jin.GetType();
    }
    for (int k=0; k < NbOutputs; k++) {
         JKBCTOutput jout=kbct.GetOutput(k+1);
         NbOL[k+NbInputs]= jout.GetLabelsNumber();
         VarType[k+NbInputs]= jout.GetType();
    }
    Rule[] RR= SpreadRules(RRaux, NbOL, VarType);
    for (int n=0; n<NbRules; n++)
      kbct.RemoveRule(0);

    for (int n=0; n<RR.length; n++)
      kbct.AddRule(RR[n]);

    for (int n=0; n<NbInputs; n++) {
       JKBCTInput jin= kbct.GetInput(n+1);
       if (jin.GetType().equals("numerical")) {
         jin.initMFLabelNames();
         int NOL= jin.GetLabelsNumber();
         String[] LN= jin.GetLabelsName();
         for (int m=0; m<NOL; m++)
           jin.AddMFLabelName(LN[m]);

         int lim= NOL;
         String[] ORNames= null;
         if (NOL > 3) {
           ORNames = jin.GetORLabelsName();
           lim= lim + ORNames.length;
         }
         int cont=0;
         for (int m=0; m<lim; m++) {
           LabelKBCT e1= null;
           LabelKBCT e2= null;
           if (m<NOL) {
             // Label NOT
             if (m==0) {
               e1= NOTLabel(jin.GetLabel(1), jin.GetInputInterestRange()[1], NOL+1, NOL);
               if (jin.GetScaleName().equals("user"))
                 jin.AddMFLabelName(LocaleKBCT.GetString("NOT")+"("+LN[0]+")");
               else
                 jin.AddMFLabelName(LocaleKBCT.GetString("NOT")+"("+LocaleKBCT.GetString(LN[0])+")");
             } else if (m==NOL-1) {
               e1= NOTLabel(jin.GetLabel(NOL), jin.GetInputInterestRange()[0], 3*NOL-2, NOL);
               if (jin.GetScaleName().equals("user"))
                 jin.AddMFLabelName(LocaleKBCT.GetString("NOT")+"("+LN[NOL-1]+")");
               else
                 jin.AddMFLabelName(LocaleKBCT.GetString("NOT")+"("+LocaleKBCT.GetString(LN[NOL-1])+")");
             } else {
               e1= SmallerThan(jin.GetLabel(m+1), jin.GetInputInterestRange()[0], NOL+m+1+cont);
               if (jin.GetScaleName().equals("user"))
                 jin.AddMFLabelName(LocaleKBCT.GetString("SmallerThan")+"("+LN[m]+")");
               else
                 jin.AddMFLabelName(LocaleKBCT.GetString("SmallerThan")+"("+LocaleKBCT.GetString(LN[m])+")");

               e2= BiggerThan(jin.GetLabel(m+1), jin.GetInputInterestRange()[1], NOL+m+2+cont);
               if (jin.GetScaleName().equals("user"))
                 jin.AddMFLabelName(LocaleKBCT.GetString("BiggerThan")+"("+LN[m]+")");
               else
                 jin.AddMFLabelName(LocaleKBCT.GetString("BiggerThan")+"("+LocaleKBCT.GetString(LN[m])+")");

               cont= cont+1;
             }
             jin.AddLabel(e1);
             if (e2 != null)
               jin.AddLabel(e2);
           } else {
             // Label OR
             int SelLabel= m-NOL+1;
             int NbLabelsOR= jnikbct.NbORLabels(SelLabel, NOL);
             int option= jnikbct.option(SelLabel, NbLabelsOR, NOL);
             LabelKBCT e_ini= jin.GetLabel(option);
             LabelKBCT e_fin= jin.GetLabel(option+NbLabelsOR-1);
             e1= ORLabel(e_ini, e_fin, m+2*NOL-1, NOL);
             jin.AddMFLabelName(ORNames[m-NOL]);
             jin.AddLabel(e1);
           }
         }
         jnikbct.ReplaceInput( kbct.GetPtr(), n+1, jin.GetV());
       }
    }
    for (int n=0; n<NbOutputs; n++) {
      JKBCTOutput jout= kbct.GetOutput(n+1);
      if (jout.GetType().equals("numerical")) {
        jout.initMFLabelNames();
        int NOL= jout.GetLabelsNumber();
        String[] LN= jout.GetLabelsName();
        for (int m=0; m<NOL; m++)
          jout.AddMFLabelName(LN[m]);

        // Esto se usar�a si se usase etiquetas compuestas en las salidas
        /*int lim= NOL;
        String[] ORNames= null;
        if (NOL > 3) {
          ORNames = jout.GetORNameLabels();
          lim= lim + ORNames.length;
        }

        for (int m=0; m<lim; m++) {
          LabelKBCT e1= null;
          LabelKBCT e2= null;
          if (m<NOL) {
            // Label NOT
            if (m==0) {
              e1= NOTLabel(jout.GetMF(1), jout.GetRangeI()[1], NOL+1, NOL);
              if (jout.GetScaleName().equals("user"))
                jout.AddMFLabelName(LocaleKBCT.GetString("NOT")+"("+LN[0]+")");
              else
                jout.AddMFLabelName(LocaleKBCT.GetString("NOT")+"("+LocaleKBCT.GetString(LN[0])+")");
            } else if (m==NOL-1) {
              e1= NOTLabel(jout.GetMF(NOL), jout.GetRangeI()[0], 3*NOL-2, NOL);
              if (jout.GetScaleName().equals("user"))
                jout.AddMFLabelName(LocaleKBCT.GetString("NOT")+"("+LN[NOL-1]+")");
              else
                jout.AddMFLabelName(LocaleKBCT.GetString("NOT")+"("+LocaleKBCT.GetString(LN[NOL-1])+")");
            } else {
              e1= SmallerThan(jout.GetMF(m+1), jout.GetRangeI()[0], NOL+m+1);
              if (jout.GetScaleName().equals("user"))
                jout.AddMFLabelName(LocaleKBCT.GetString("SmallerThan")+"("+LN[m]+")");
              else
                jout.AddMFLabelName(LocaleKBCT.GetString("SmallerThan")+"("+LocaleKBCT.GetString(LN[m])+")");
              e2= BiggerThan(jout.GetMF(m+1), jout.GetRangeI()[1], NOL+m+2);
              if (jout.GetScaleName().equals("user"))
                jout.AddMFLabelName(LocaleKBCT.GetString("BiggerThan")+"("+LN[m]+")");
              else
                jout.AddMFLabelName(LocaleKBCT.GetString("BiggerThan")+"("+LocaleKBCT.GetString(LN[m])+")");
            }
            jout.AddMF(e1);
            if (e2 != null) {
              jout.AddMF(e2);
            }
          } else {
            // Label OR
            int SelLabel= m-NOL+1;
            int NbLabelsOR= jnikbct.NbORLabels(SelLabel, NOL);
            int option= jnikbct.option(SelLabel, NbLabelsOR, NOL);
            LabelKBCT e_ini= jout.GetMF(option);
            LabelKBCT e_fin= jout.GetMF(option+NbLabelsOR-1);
            e1= ORLabel(e_ini, e_fin, m+2*NOL-1, NOL);
            jout.AddMFLabelName(ORNames[m-NOL]);
            jout.AddMF(e1);
          }
        }*/
        jnikbct.ReplaceOutput( kbct.GetPtr(), n+1, jout.GetV());
      }
    }
    return kbct;
  }
  //----------------------------------------------------------------------------
  /**
   * Expand Rule Base "RR".
   * This method is only used in evaluation of quality for simplification process.
   * @param RR initial rules
   * @param NOLL numbers of labels for each variable
   * @param vt types of variables
   * @return array with expanded rules
   * @throws Throwable exception
   */
  public static Rule[] SpreadRules(Rule[] RR, int[] NOLL, String[] vt) throws Throwable {
    // La parte comentada sirve para hacer la extensi�n en las salidas
    int NbRules= RR.length;
    int[] NbOL= NOLL;
    String[] VarType= vt;
    int n=0;
    int lim= NbRules;
    int iniIn= 0;
    //int iniOut= 0;
    int NbR= 1;
    //Vector v= new Vector();
    while (n<lim) {
    	Rule r = RR[NbR-1];
        if (r.GetActive()) {
          int[] premises = r.Get_in_labels_number();
          //int[] conc = r.Get_out_labels_number();
          int[] premises_exp = r.Get_in_labels_exp();
          //int[] conc_exp = r.Get_out_labels_exp();
          //boolean flag= false;
          for (int m = iniIn; m < premises.length; m++) {
             int NOL= NbOL[m];
             if ( (premises_exp[m]==0) && (premises[m] > NOL) ) {
               if (VarType[m].equals("numerical")) {
                 if (premises[m] > 2*NOL) {
                   r.SetInputLabel(m+1, premises[m] + NOL-2);
                   for (int k=0; k<m+1; k++)
                     premises_exp[k]= 1;
                   r.Set_in_labels_exp(premises_exp);
                   RR[NbR-1]= r;
                 } else if (premises[m] == 2*NOL) {
                   r.SetInputLabel(m+1, 3*NOL-2);
                   for (int k=0; k<m+1; k++)
                     premises_exp[k]= 1;
                   r.Set_in_labels_exp(premises_exp);
                   RR[NbR-1]= r;
                 } else if ( (premises[m] > NOL+1) && (premises[m] < 2*NOL) ) {
                     // NOT -> 2 rules
                     RR= ModifyRBqualityNOTNumerical(RR,NbR,m,NOL,premises[m]-NOL/*, "Input"*/);
                 }
               } else {
                 // VarType[m] == categorical or logical
                 if (premises[m] > 2*NOL)
                   RR= ModifyRBqualityORCategorical(RR,NbR,m,NOL,premises[m]-2*NOL/*,"Input"*/);
                 else
                   RR= ModifyRBqualityNOTCategorical(RR,NbR,m,NOL,premises[m]-NOL/*,"Input"*/);
               }
               n--;
               iniIn= m+1;
               m= premises.length;
               //flag= true;
             }
          }
        /*if (!flag)
          for (int m = iniOut; m < conc.length; m++) {
             int NOL= NbOL[m+premises.length];
             if ( (conc_exp[m]==0) && (conc[m] > NOL) ) {
               if (VarType[m].equals("numerical")) {
                 if (conc[m] > 2*NOL) {
                   r.SetOutputLabel(m+1, conc[m]+NOL-2);
                   for (int k=0; k<premises_exp.length; k++)
                     premises_exp[k]= 1;
                   r.Set_in_labels_exp(premises_exp);
                   for (int k=0; k<m+1; k++)
                     conc_exp[k]= 1;
                   r.Set_out_labels_exp(conc_exp);
                   RR[NbR-1]= r;
                 } else if (conc[m] == 2*NOL) {
                   r.SetOutputLabel(m+1, 3*NOL-2);
                   RR[NbR-1]= r;
                 } else if ( (conc[m] > NOL+1) && (conc[m] < 2*NOL) ) {
                     // NOT -> 2 rules
                     RR= ModifyRuleBaseNOTquality(RR,NbR,m,NOL,conc[m]-NOL,"Output");
                 }
               } else {
                 if (premises[m] > 2*NOL) {
                   RR= ModifyRBORCategorical(RR,NbR,m,NOL,conc[m]-2*NOL,"Output");
                 } else {
                   RR= ModifyRBNOTCategorical(RR,NbR,m,NOL,conc[m]-NOL,"Output");
                 }
               }
               n--;
               iniOut= m+1;
               m= conc.length;
             }
          }*/
        }
        n++;
        if (n==NbR) {
           iniIn=0;
           //iniOut=0;
           NbR= NbR+1;
        }
        lim= RR.length;
    }
    return RR;
  }
  //----------------------------------------------------------------------------
  /**
   * Expand rules with NOT labels.
   * This method is only used in evaluation of quality for simplification process.
   * @param RR initial rules
   * @param NbRule number of the rule to be expanded
   * @param NbVar number of the variable
   * @param NbLabels number of labels in the variable v
   * @param SelLabel number of selected label
   * @return array with expanded rules
   */
  private static Rule[] ModifyRBqualityNOTNumerical (Rule[] RR, int NbRule, int NbVar, int NbLabels, int SelLabel/*, String VarType*/) {
     // La parte comentada sirve para hacer la extensi�n en las salidas
     // crear nuevas etiquetas y reglas
     Rule[] rr= new Rule[RR.length+1];
     for (int n=0; n<NbRule-1;n++)
         rr[n]= RR[n];

     for (int n=NbRule+1; n<rr.length;n++)
         rr[n]= RR[n-1];

     int NbNewRules= 2;
     int NOTLabel= SelLabel;
     int LabelNumber= NbLabels + 2*(NOTLabel-1);
     for (int n=0; n<NbNewRules; n++) {
        Rule r= RR[NbRule-1];
        //if (VarType.equals("Input")) {
            int[] InputsLabels= new int[r.GetNbInputs()];
            for (int m=0; m<InputsLabels.length; m++) {
              if (m==NbVar)
                InputsLabels[m]= LabelNumber+n;
              else
                InputsLabels[m] = r.Get_in_labels_number()[m];
            }
            Rule rnew= new Rule(NbRule+n, r.GetNbInputs(), r.GetNbOutputs(), InputsLabels, r.Get_out_labels_number(), r.GetType(), r.GetActive());
            int[] in_labels_exp= rnew.Get_in_labels_exp();
            for (int m=0; m<NbVar+1; m++)
              in_labels_exp[m]= 1;
            rnew.Set_in_labels_exp(in_labels_exp);
            rr[n+NbRule-1]= rnew;
        //}
        /*else if (VarType.equals("Output")) {
            int[] OutputsLabels= new int[r.GetNbOutputs()];
            for (int m=0; m<OutputsLabels.length; m++) {
              if (m==NbVar)
                OutputsLabels[m]= LabelNumber+n;
              else
                OutputsLabels[m] = r.Get_out_labels_number()[m];
            }
            Rule rnew= new Rule(NbRule+n, r.GetNbInputs(), r.GetNbOutputs(), r.Get_in_labels_number(), OutputsLabels, r.GetType());
            int[] in_labels_exp= rnew.Get_in_labels_exp();
            for (int m=0; m<in_labels_exp.length; m++)
              in_labels_exp[m]= 1;
            rnew.Set_in_labels_exp(in_labels_exp);
            int[] out_labels_exp= rnew.Get_out_labels_exp();
            for (int m=0; m<NbVar+1; m++)
              out_labels_exp[m]= 1;
            rnew.Set_out_labels_exp(out_labels_exp);
            rr[n+NbRule-1]= rnew;
        }*/
   }
   return rr;
  }
  //----------------------------------------------------------------------------
  /**
   * Expand Rule Base "RR".
   * Expand rules with NOT categorical labels.
   * This method is only used in evaluation of quality for simplification process.
   * @param RR initial rules
   * @param NbRule number of the rule to be expanded
   * @param NbVar number of the variable
   * @param NbLabels number of labels in the variable v
   * @param SelLabel number of selected label
   * @return array with expanded rules
   */
  private static Rule[] ModifyRBqualityNOTCategorical (Rule[] RR, int NbRule, int NbVar, int NbLabels, int SelLabel/*, String VarType*/) {
    // La parte comentada sirve para hacer la extensi�n en las salidas
    int NbNewRules= NbLabels-1;
    Rule[] rr= new Rule[RR.length-1+NbNewRules];
    for (int n=0; n<NbRule-1;n++)
        rr[n]= RR[n];

    for (int n=NbRule+NbNewRules-1; n<rr.length;n++)
        rr[n]= RR[n-NbNewRules+1];

    for (int n=0; n<NbNewRules; n++) {
       Rule r= RR[NbRule-1];
       int label= 0;
       if (n < SelLabel-1)
          label= n+1;
       else if (n >= SelLabel-1)
          label= n+2;

       //if (VarType.equals("Input")) {
           int[] InputsLabels= new int[r.GetNbInputs()];
           for (int m=0; m<InputsLabels.length; m++) {
             if (m==NbVar)
               InputsLabels[m]= label;
             else
               InputsLabels[m] = r.Get_in_labels_number()[m];
           }
           Rule rnew= new Rule(NbRule+n, r.GetNbInputs(), r.GetNbOutputs(), InputsLabels, r.Get_out_labels_number(), r.GetType(), r.GetActive());
           int[] in_labels_exp= rnew.Get_in_labels_exp();
           for (int m=0; m<NbVar+1; m++)
             in_labels_exp[m]= 1;
           rnew.Set_in_labels_exp(in_labels_exp);
           rr[n+NbRule-1]= rnew;
       //}
       /*else if (VarType.equals("Output")) {
           int[] OutputsLabels= new int[r.GetNbOutputs()];
           for (int m=0; m<OutputsLabels.length; m++) {
             if (m==NbVar)
               OutputsLabels[m]= label;
             else
               OutputsLabels[m] = r.Get_out_labels_number()[m];
           }
           Rule rnew= new Rule(NbRule+n, r.GetNbInputs(), r.GetNbOutputs(), r.Get_in_labels_number(), OutputsLabels, r.GetType());
           int[] in_labels_exp= rnew.Get_in_labels_exp();
           for (int m=0; m<in_labels_exp.length; m++)
             in_labels_exp[m]= 1;
           rnew.Set_in_labels_exp(in_labels_exp);
           int[] out_labels_exp= rnew.Get_out_labels_exp();
           for (int m=0; m<NbVar+1; m++)
             out_labels_exp[m]= 1;
           rnew.Set_out_labels_exp(out_labels_exp);
           rr[n+NbRule-1]= rnew;
       }*/
    }
    return rr;
  }
  //----------------------------------------------------------------------------
  /**
   * Expand Rule Base "RR".
   * Expand rules with OR categorical labels.
   * This method is only used in evaluation of quality for simplification process.
   * @param RR initial rules
   * @param NbRule number of the rule to be expanded
   * @param NbVar number of the variable
   * @param NbLabels number of labels in the variable v
   * @param SelLabel number of selected label
   * @return array with expanded rules
   */
  private static Rule[] ModifyRBqualityORCategorical (Rule[] RR, int NbRule, int NbVar, int NbLabels, int SelLabel/*, String VarType*/) {
    // La parte comentada sirve para hacer la extensi�n en las salidas
    int NbNewRules= jnikbct.NbORLabels(SelLabel, NbLabels);
    Rule[] rr= new Rule[RR.length-1+NbNewRules];
    for (int n=0; n<NbRule-1;n++)
        rr[n]= RR[n];

    for (int n=NbRule+NbNewRules-1; n<rr.length;n++)
        rr[n]= RR[n-NbNewRules+1];

    int option= jnikbct.option(SelLabel, NbNewRules, NbLabels);
    for (int n=0; n<NbNewRules; n++) {
       Rule r= RR[NbRule-1];
       int label= option + n;
       //if (VarType.equals("Input")) {
           int[] InputsLabels= new int[r.GetNbInputs()];
           for (int m=0; m<InputsLabels.length; m++) {
             if (m==NbVar)
               InputsLabels[m]= label;
             else
               InputsLabels[m] = r.Get_in_labels_number()[m];
           }
           Rule rnew= new Rule(NbRule+n, r.GetNbInputs(), r.GetNbOutputs(), InputsLabels, r.Get_out_labels_number(), r.GetType(), r.GetActive());
           int[] in_labels_exp= rnew.Get_in_labels_exp();
           for (int m=0; m<NbVar+1; m++)
                in_labels_exp[m]= 1;
           rnew.Set_in_labels_exp(in_labels_exp);
           rr[n+NbRule-1]= rnew;
       //}
       /*else if (VarType.equals("Output")) {
           int[] OutputsLabels= new int[r.GetNbOutputs()];
           for (int m=0; m<OutputsLabels.length; m++) {
             if (m==NbVar)
               OutputsLabels[m]= label;
             else
               OutputsLabels[m] = r.Get_out_labels_number()[m];
           }
           Rule rnew= new Rule(NbRule+n, r.GetNbInputs(), r.GetNbOutputs(), r.Get_in_labels_number(), OutputsLabels, r.GetType());
           int[] out_labels_exp= rnew.Get_out_labels_exp();
           for (int m=0; m<NbVar+1; m++)
             out_labels_exp[m]= 1;
           rnew.Set_out_labels_exp(out_labels_exp);
           rr[n+NbRule-1]= rnew;
         }*/
       }
     return rr;
  }
  //----------------------------------------------------------------------------
  /**
   * Build a new Label smaller than e.
   * @param e initial LabelKBCT
   * @param Lim P1 (lower interest range)
   * @param Number number of the new label
   * @return new LabelKBCT
   */
  private static LabelKBCT SmallerThan (LabelKBCT e, double Lim, int Number) {
    LabelKBCT result= new LabelKBCT("SemiTrapezoidalInf", Lim, e.GetP1(), e.GetP2(), Number);
    return result;
  }
  //----------------------------------------------------------------------------
  /**
   * Build a new Label bigger than e.
   * @param e initial LabelKBCT
   * @param Lim P3 (upper interest range)
   * @param Number number of the new label
   * @return new LabelKBCT
   */
  private static LabelKBCT BiggerThan (LabelKBCT e, double Lim, int Number) {
    LabelKBCT result= null;
    int NbParams= e.GetParams().length;
    if ( NbParams == 3)
         result= new LabelKBCT("SemiTrapezoidalSup", e.GetP2(), e.GetP3(), Lim, Number);
    else if ( NbParams == 4)
         result= new LabelKBCT("SemiTrapezoidalSup", e.GetP3(), e.GetP4(), Lim, Number);

    return result;
  }
  //----------------------------------------------------------------------------
  /**
   * Build a new Label with OR.
   * @param e_ini first LabelKBCT in the composite OR label
   * @param e_fin last LabelKBCT in the composite OR label
   * @param Number number of the new label
   * @param NOL number of labels of the variable
   * @return new LabelKBCT
   */
  public static LabelKBCT ORLabel (LabelKBCT e_ini, LabelKBCT e_fin, int Number, int NOL) {
    LabelKBCT result= null;
    int NbParams_fin= e_fin.GetParams().length;
    if (e_ini.GetLabel_Number()==1) {
          if ( NbParams_fin == 3)
            result= new LabelKBCT("SemiTrapezoidalInf", e_ini.GetP1(), e_fin.GetP2(), e_fin.GetP3(), Number);
          else if ( NbParams_fin == 4)
            result= new LabelKBCT("SemiTrapezoidalInf", e_ini.GetP1(), e_fin.GetP3(), e_fin.GetP4(), Number);
      } else if (e_fin.GetLabel_Number()==NOL) {
            result= new LabelKBCT("SemiTrapezoidalSup", e_ini.GetP1(), e_ini.GetP2(), e_fin.GetP3(), Number);
      } else {
          if ( NbParams_fin == 3)
            result= new LabelKBCT("trapezoidal", e_ini.GetP1(), e_ini.GetP2(), e_fin.GetP2(), e_fin.GetP3(), Number);
          else if ( NbParams_fin == 4)
            result= new LabelKBCT("trapezoidal", e_ini.GetP1(), e_ini.GetP2(), e_fin.GetP3(), e_fin.GetP4(), Number);
    }
    return result;
  }
  //----------------------------------------------------------------------------
  /**
   * Build a new Label with NOT.
   * @param e_basic initial LabelKBCT
   * @param lim beginning or end of the new label
   * @param Number number of the new label
   * @param NOL number of labels of the variable
   * @return new LabelKBCT
   */
  public static LabelKBCT NOTLabel (LabelKBCT e_basic, double lim, int Number, int NOL) {
    LabelKBCT result= null;
    if (e_basic.GetLabel_Number()==1)
        result= new LabelKBCT("SemiTrapezoidalSup", e_basic.GetP2(), e_basic.GetP3(), lim, Number);
    else if (e_basic.GetLabel_Number()==NOL)
        result= new LabelKBCT("SemiTrapezoidalInf", lim, e_basic.GetP1(), e_basic.GetP2(), Number);

    return result;
  }
  //----------------------------------------------------------------------------
  /**
   * Check if there is a label "LabelName" in variable "v".
   * @param v variable
   * @param LabelName name of the label
   * @return true (there is a label "LabelName" in variable "v") / false
   */
  private static boolean ExistLabel (variable v, String LabelName) {
    int NOL= v.GetLabelsNumber();
    String[] Labels= v.GetLabelsName();
    for (int n=0; n<NOL; n++) {
      if (Labels[n].equals(LabelName))
        return true;
    }
    return false;
  }
  //----------------------------------------------------------------------------
  /**
   * Return the number of label "LabelName" in variable "v".
   * @param v variable
   * @param LabelName name of the label
   * @return number of the label "LabelName" in variable "v"
   */
  private static int NumberOfLabel (variable v, String LabelName) {
    int NOL= v.GetLabelsNumber();
    String[] Labels= v.GetLabelsName();
    for (int n=0; n<NOL; n++)
      if (Labels[n].equals(LabelName))
        return n+1;

    return NOL+1;
  }
  //----------------------------------------------------------------------------
  /**
   * Save Rule Base "RR" in FisPro archive "file_name_FIS".
   * This method is only used in evaluation of quality for simplification process.
   * @param file_name_FIS name of the FIS file with the initial knowledge base
   * @param RR new rule base
   * @return name of the new FIS file
   * @throws Throwable exception
   */
  public static String SaveFisRules (String file_name_FIS, Rule[] RR) throws Throwable{
    File temp = JKBCTFrame.BuildFile("temprbqconsaux"+cont+".fis");
    cont++;
    String fis_name= temp.getAbsolutePath();
    PrintWriter fOut = new PrintWriter(new FileOutputStream(fis_name),false);
    LineNumberReader lnr= new LineNumberReader(new InputStreamReader(new FileInputStream(file_name_FIS)));
    String l;
    DecimalFormat df= new DecimalFormat();
    df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
    dfs.setDecimalSeparator((new String(".").charAt(0)));
    df.setDecimalFormatSymbols(dfs);
    df.setGroupingSize(20);
    int cnt=0;
    for (int n=0; n<RR.length; n++) {
      if (RR[n].GetActive())
        cnt++;
    }
    int NbRules= RR.length;
    int NbActiveRules= cnt;
    while((l= lnr.readLine())!=null) {
      if (l.startsWith("Nrules"))
        fOut.println("Nrules="+NbActiveRules);
      else if (l.equals("[Rules]")) {
        fOut.println(l);
        for (int n=0; n<NbRules; n++) {
          Rule r= RR[n];
          if (r.GetActive()) {
            int[] premises= r.Get_in_labels_number();
            int[] conc= r.Get_out_labels_number();
            for (int m=0; m<premises.length;m++)
              fOut.print(premises[m]+", ");

            fOut.print("   ");
            for (int m=0; m<conc.length;m++) {
              if (m==conc.length-1)
                fOut.println(df.format(conc[m]) + ", ");
              else
                fOut.print(df.format(conc[m]) + ", ");
            }
          }
        }
        break;
      } else {
        fOut.println(l);
      }
    }
    fOut.println();
    fOut.println("[Exceptions]");
    fOut.println();
    fOut.flush();
    fOut.close();
    lnr.close();
    return fis_name;
  }
//------------------------------------------------------------------------------
  public static void SetFISoptions(JKBCT kbct, Component parent) {
    String Msg;
    if (conjunction==null) {
        Msg= LocaleKBCT.GetString("Default_conjunction_value")+"\n"+
             LocaleKBCT.GetString("DoYouWantToReplaceIt");
    } else  {
        Msg= LocaleKBCT.GetString("Conjunction_value")+" "+LocaleKBCT.GetString(conjunction)+". "+"\n"+
             LocaleKBCT.GetString("DoYouWantToReplaceIt");
    }
    int option= MessageKBCT.Confirm(parent, Msg, 1, false, false, true);
    if (option==0) {
        String selection= (String)MessageKBCT.SelectFisProConjunction(parent);
        if (selection==null)
            conjunction= "min";
        else if ( selection.equals("m�nimo") || selection.startsWith("min") )
            conjunction= "min";
        else if ( selection.startsWith("prod") )
            conjunction= "prod";
        else if (selection.equals("Lukasiewicz"))
            conjunction= "luka";
        else
            conjunction= "min";
    } else if(option==2) {
        conjunction= "min";
    } else {
        if (conjunction==null)
            conjunction= "min";
    }
    int NbOutputs= kbct.GetNbOutputs();
    if (disjunction==null)
      disjunction= new String[NbOutputs];

    if (defuzzification==null)
      defuzzification= new String[NbOutputs];

    for (int n=0; n<NbOutputs; n++) {
      JKBCTOutput out= kbct.GetOutput(n+1);
      String OutputName= out.GetName();
      String OutputType= out.GetType();
      if (disjunction[n]==null) {
          Msg= LocaleKBCT.GetString("Output")+": "+OutputName+"\n"+
               LocaleKBCT.GetString("Default_disjunction_value")+"\n"+
               LocaleKBCT.GetString("DoYouWantToReplaceIt");
      } else {
          Msg= LocaleKBCT.GetString("Output")+": "+OutputName+"\n"+
               LocaleKBCT.GetString("Disjunction_value")+" "+LocaleKBCT.GetString(disjunction[n])+". "+"\n"+
               LocaleKBCT.GetString("DoYouWantToReplaceIt");
      }
      option= MessageKBCT.Confirm(parent, Msg, 1, false, false, true);
      if (option==0) {
          String selection= (String)MessageKBCT.SelectFisProDisjunction(parent);
          if (selection==null)
            disjunction[n]= "max";
          else if (selection.startsWith("sum"))
            disjunction[n]= "sum";
          else
            disjunction[n]= "max";
      } else if (option==2) { 
          disjunction[n]= "max";
      } else {
          if (disjunction[n]==null)
              disjunction[n]= "max";
      }
      if (OutputType.equals("numerical")) {
          if (defuzzification[n]==null) {
              Msg= LocaleKBCT.GetString("Output")+": "+OutputName+"\n"+
                   LocaleKBCT.GetString("Default_defuzzification_value_fuzzy")+"\n"+
                   LocaleKBCT.GetString("DoYouWantToReplaceIt");
          } else {
              Msg= LocaleKBCT.GetString("Output")+": "+OutputName+"\n"+
                   LocaleKBCT.GetString("Defuzzification_value")+" "+LocaleKBCT.GetString(defuzzification[n])+". "+"\n"+
                   LocaleKBCT.GetString("DoYouWantToReplaceIt");
          }
          option= MessageKBCT.Confirm(parent, Msg, 1, false, false, true);
          if (option==0) {
              String selection= (String)MessageKBCT.SelectFisProDefuzzification(parent, "Fuzzy");
              if (selection==null)
                defuzzification[n]="area";
              else if (selection.equals("sugeno"))
                defuzzification[n]="sugeno";
              else if ( selection.startsWith("m") )
                defuzzification[n]="MeanMax";
              else
                defuzzification[n]="area";
          } else if (option==2) {
              defuzzification[n]="area";
          }else {
              if (defuzzification[n]==null)
                defuzzification[n]="area";
          }
      } else {
          if (defuzzification[n]==null) {
              Msg= LocaleKBCT.GetString("Output")+": "+OutputName+"\n"+
                   LocaleKBCT.GetString("Default_defuzzification_value_crisp")+"\n"+
                   LocaleKBCT.GetString("DoYouWantToReplaceIt");
          } else {
              Msg= LocaleKBCT.GetString("Output")+": "+OutputName+"\n"+
                   LocaleKBCT.GetString("Defuzzification_value")+" "+LocaleKBCT.GetString(defuzzification[n])+". "+"\n"+
                   LocaleKBCT.GetString("DoYouWantToReplaceIt");
          }
          option= MessageKBCT.Confirm(parent, Msg, 1, false, false, true);
          if (option==0) {
            String selection= (String)MessageKBCT.SelectFisProDefuzzification(parent, "crisp");
            if (selection==null)
                defuzzification[n]="MaxCrisp";
            else if (selection.equals("sugeno"))
                defuzzification[n]="sugeno";
            else
                defuzzification[n]="MaxCrisp";
          } else if (option==2) { 
              defuzzification[n]="MaxCrisp";
          } else {
            if (defuzzification[n]==null)
                defuzzification[n]="MaxCrisp";
          }
      }
    }
  }
//------------------------------------------------------------------------------
  public static void SetFISoptions(String conj, String[] disj, String[] defuz) {
    conjunction= conj;
    disjunction= disj;
    defuzzification= defuz;
  }
//------------------------------------------------------------------------------
  public static void SetFISoptionsDefault(int NbOutputs, String[] OutputType) {
    conjunction= "min";
    if (NbOutputs >0) {
      disjunction= new String[NbOutputs];
      defuzzification= new String[NbOutputs];
      for (int n=0; n<NbOutputs; n++) {
        disjunction[n]= "max";
        if (OutputType[n].equals("numerical"))
            defuzzification[n]= "area";
        else
            defuzzification[n]= "MaxCrisp";
      }
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Convert GUAJE archive "file_name_GUAJE" to Espresso archive "file_name_ESPRESSO".
   * @param file_name_GUAJE name of the GUAJE file to be translated
   * @param file_name_ESPRESSO name of the translated ESPRESSO file
   * @return KB with the simplified rule base
   * @throws Throwable exception
   */
  public static JKBCT GuajeToEspresso (String file_name_GUAJE, String file_name_ESPRESSO) throws Throwable{
    //System.out.println("KbctToEspresso:  "+file_name_KBCT+"  ->  "+file_name_ESPRESSO);
	JKBCT kbct= new JKBCT(file_name_GUAJE);

    PrintWriter fOut = new PrintWriter(new FileOutputStream(file_name_ESPRESSO), false);
    DecimalFormat df= new DecimalFormat();
    df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
    DecimalFormatSymbols dfs= df.getDecimalFormatSymbols();
    dfs.setDecimalSeparator((new String(".").charAt(0)));
    df.setDecimalFormatSymbols(dfs);
    df.setGroupingSize(20);
    if (JConvert.conjunction==null || JConvert.disjunction==null || JConvert.defuzzification==null) {
      int NbOutputs= kbct.GetNbOutputs();
      String[] OutputTypes= null;
      if (NbOutputs >0) {
        OutputTypes= new String[NbOutputs];
        for (int n=0; n<NbOutputs; n++)
          OutputTypes[n]= kbct.GetOutput(n+1).GetType();
      }
      JConvert.SetFISoptionsDefault(NbOutputs, OutputTypes);
    }
    int Ninputs= kbct.GetNbInputs();
    //System.out.println("Ninputs:  "+Ninputs);
    int isum=0;
 	for (int k=0; k<Ninputs; k++) {
         int N_variable= k+1;
         JKBCTInput in= kbct.GetInput(N_variable);
         int Nlabels= in.GetLabelsNumber();
         isum= isum+Nlabels;
	}
    fOut.println(".i "+ isum);
    int Noutputs= kbct.GetNbOutputs();
    //System.out.println("Noutputs:  "+Noutputs);
    int osum=0;
 	for (int k=0; k<Noutputs; k++) {
         int N_variable= k+1;
         JKBCTOutput out= kbct.GetOutput(N_variable);
         int Nlabels= out.GetLabelsNumber();
         osum= osum+Nlabels;
	}
    fOut.println(".o "+ osum);
    //int Nrules= kbct.GetNbActiveRules();
    int Nrules=0;
    //fOut.println(".p "+ Nrules);
    String bufferTmp = ".type fr\n";
    //fOut.println(".type fr");
    for (int n=0; n<kbct.GetNbRules(); n++) {
     	Rule rr= kbct.GetRule(n+1);
     	if (rr.GetActive()) {
     		int[] premises= rr.Get_in_labels_number();
     		boolean warn= false;        			
     		for (int k=0; k<premises.length; k++) {
     			if (premises[k]!=0) {
     				warn=true;
     				break;
     		    }
     		}
     	    if (!warn) {
     	    	kbct.SetRuleActive(n+1,false);
     	    } else {
     		    int[] conclusions= rr.Get_out_labels_number();
     		    for (int k=0; k<conclusions.length; k++) {
     			     if (conclusions[k]!=0) {
     				     warn=true;
     				     break;
     		         }
     		    }
         	    if (!warn) {
         	    	kbct.SetRuleActive(n+1,false);
         	    } else {
         	    	int nBits = 0;
         	    	int bitsOffset = 0;
         	    	// counting the number of bits
         	    	for (int k=0; k<premises.length; k++)
         	    		nBits += UsedLabels(kbct.GetInput(k+1).GetLabelsNumber(), premises[k]).length;
         	    	int[][] bitsMatrix = new int[1][nBits];
         	    	for (int k=0;k<bitsMatrix[0].length;k++)
         	    		bitsMatrix[0][k]=0;
         	    	for (int k=0; k<premises.length; k++)
         	    	{
             	    	int numExp = 0;
        	            int N_variable= k+1;
         	            JKBCTInput in= kbct.GetInput(N_variable);
         	            int Nlabels= in.GetLabelsNumber();
         	            int[] pp= UsedLabels(Nlabels, premises[k]);
         	    		// counting the labels used
             	    	for (int i=0; i<Nlabels; i++)
             	    		if (pp[i]==1)
             	    			numExp++;
             	    	// exploding the matrix and adding the bits relates to the labels
             	    	bitsMatrix=explodeMatrix(bitsMatrix, bitsOffset,numExp,pp);
             	    	bitsOffset += pp.length;
         	    	}
         	    	for (int k=0; k<conclusions.length; k++) {
         	    		int N_variable= k+1;
         	    		JKBCTOutput out= kbct.GetOutput(N_variable);
         	    		int Nlabels= out.GetLabelsNumber();
         	    		int[] pp= UsedLabels(Nlabels, conclusions[k]);
         	    		for (int k1=0;k1<bitsMatrix.length;k1++)
         	    		{
         	    			// printing the premises
         	    			for (int k2=0;k2<bitsMatrix[k1].length;k2++)
         	    				if (bitsMatrix[k1][k2]==-1)
         	    					bufferTmp+="-";
         	    					//fOut.print("-");
         	    				else
         	    					bufferTmp+=bitsMatrix[k1][k2];
         	    					//fOut.print(bitsMatrix[k1][k2]);
         	    			bufferTmp+=" ";
         	    			//fOut.print(" ");
         	    			// printing the consequences
             	    		for (int i=0; i<Nlabels; i++)
             	    			if (pp[i]==-1)
             	    				bufferTmp+="-";
             	    				//fOut.print("-");
             	    			else
             	    				bufferTmp+=pp[i];
             	    				//fOut.print(pp[i]);
             	    		bufferTmp+="\n";
             	    		Nrules++;
             	    		//fOut.println();
         	    		}
         	    	}

//        	    	for (int k=0; k<premises.length; k++) {
//         	             int N_variable= k+1;
//         	             JKBCTInput in= kbct.GetInput(N_variable);
//         	             int Nlabels= in.GetLabelsNumber();
//         	             int[] pp= UsedLabels(Nlabels, premises[k]);
//         	             for (int i=0; i<Nlabels; i++) {
//         	            	  if (pp[i]==-1) {
//         	            		  //RC check for previous explosions
//         	            	      fOut.print("-");
//         	            	  } else {
//         	            		  //RC check for a possible explosion
//         	            		  //RC check for previous explosions
//         	            		  fOut.print(pp[i]);
//         	            	  }
//         	             }
//         	    	}
//         	        fOut.print(" ");
//         	    	for (int k=0; k<conclusions.length; k++) {
//        	             int N_variable= k+1;
//         	             JKBCTOutput out= kbct.GetOutput(N_variable);
//         	             int Nlabels= out.GetLabelsNumber();
//         	             int[] pp= UsedLabels(Nlabels, conclusions[k]);
//         	             for (int i=0; i<Nlabels; i++) {
//         	            	  if (pp[i]==-1) {
//         	            	      fOut.print("-");
//         	            	  } else {
//         	            		  fOut.print(pp[i]);
//         	            	  }
//         	             }
//         	    	}
//         	        fOut.println();
         	    }
     	    }
     	}
    }
    fOut.println(".p "+ Nrules);
    fOut.print(bufferTmp);
    fOut.println(".e");
    fOut.flush();
    fOut.close();
    return kbct;
  }
  
  private static int[][] explodeMatrix(int mtrx[][],int bitsOffset,int iterations,int[] vect)
  {
	  if (iterations == 0)
		  iterations = 1;
	  int rules = mtrx.length;
	  int bits = mtrx[rules-1].length;
	  int[][] newMtrx = new int[rules*iterations][bits];
	  for (int k=0;k<iterations;k++)
		  for (int i=0;i<rules;i++)
		  {
			  for (int j=0;j<bitsOffset;j++)
				  newMtrx[(k*rules)+i][j] = mtrx[i][j];
		  }
	  int[][] newVect = new int[iterations][vect.length];
	  int currentIdx = 0;
	  for (int j=0;j<vect.length;j++)
	  {
		  boolean findFlag = false;
		  for (int i=0;i<newVect.length;i++)
			  if (vect[j]==1)
				  if (i==currentIdx)
				  {
					  newVect[i][j] = vect[j];
					  findFlag = true;
				  }
				  else
					  newVect[i][j] = 0;
			  else
				  newVect[i][j] = vect[j];
		  if (findFlag)
			  currentIdx++;
	  }
	  for (int k0=0;k0<iterations;k0++)
		  for (int k1=0;k1<rules;k1++)
			  for (int k2=0;k2<vect.length;k2++)
				  newMtrx[(k0*rules)+k1][bitsOffset+k2] = newVect[k0][k2];
	  return newMtrx;
  }
  
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Build an array with labels used in the selected premise.
   * Array dimension is equal to number of elementary labels.
   * </p>
   * @param NbLabels number of labels of the variable
   * @param Prem number of premise (Basic label / NOT / OR)
   * @return array of int (1 (label used) / 0 (label not used))
   */
  private static int[] UsedLabels(int NbLabels, int Prem) {
	int[] res= new int [NbLabels];
    for (int m=0; m<res.length; m++) {
         res[m]=-1;
    }
    if (Prem > 0) {
    	if (Prem <= NbLabels) {
    		// elementary label
    		//System.out.println("EL -> "+Prem+"  NOL="+NbLabels);
    		for (int m=0; m<NbLabels; m++) {
    			 if (Prem == m+1)
    				 res[m]=1;
    		     else
    		    	 res[m]=0;
    		}
    	} else if (Prem <= 2*NbLabels) {
    		// NOT composite label
    		//System.out.println("NOT -> "+Prem+"  NOL="+NbLabels);
    		for (int m=0; m<NbLabels; m++) {
   			     if (Prem == NbLabels+m+1)
   				     res[m]=0;
   		         else
   		    	     res[m]=-1;
    		}
   		} else {
   			// OR composite label
    		//System.out.println("OR -> "+Prem+"  NOL="+NbLabels);
            int SelLabel= Prem-2*NbLabels;
            int NbOR= jnikbct.NbORLabels(SelLabel, NbLabels);
            int first= jnikbct.option(SelLabel, NbOR, NbLabels);
            //System.out.println("Prem="+Prem+"  SL="+SelLabel+"  NOR="+NbOR+"  F="+first);
    		for (int m=0; m<NbLabels; m++) {
  			     if ( (m+1 >= first) && (m+1 <= first+NbOR-1) )
  				     res[m]=1;
  		         else
  		    	     res[m]=0;
   		    }
    	}
    }
    return res;
  }
}