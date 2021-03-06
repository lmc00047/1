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
//                              JConsistency.java
//
//
//**********************************************************************

package kbct;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
//import java.text.DateFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import kbctAux.MessageKBCT;
import kbctAux.PerformanceFile;
//import kbctFrames.JFISConsole;
import kbctFrames.JKBCTFrame;
//import kbctFrames.JExpertFrame;
//import kbctFrames.JKBInterpretabilityFrame;
import KB.Rule;
import fis.JExtendedDataFile;
import fis.JOutput;
import fis.jnifis;

/**
 * Check consistency of the Knowledge Base.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 3/08/15
 */
public class JConsistency {
  //private JExpertFrame parent;
  private JKBCT kbct;
  private boolean WARNING;
  private Enumeration CONSISTENCY_ERRORS;
  private Enumeration REDUNDANCY_OR_SPECIFICITY_WARNINGS;
  private Enumeration UNUSUED_INPUTS_WARNINGS;
  private Enumeration UNUSUED_INPUT_LABELS_WARNINGS;
  private Enumeration UNUSUED_OUTPUTS_WARNINGS;
  private Enumeration UNUSUED_OUTPUT_LABELS_WARNINGS;
  private Vector<InfoConsistency> V_CONSISTENCY_ERRORS;
  private Vector<InfoConsistency> V_REDUNDANCY_OR_SPECIFICITY_WARNINGS;
  private Vector<InfoConsistency> V_UNUSUED_INPUTS_WARNINGS;
  private Vector<InfoConsistency> V_UNUSUED_INPUT_LABELS_WARNINGS;
  private Vector<InfoConsistency> V_UNUSUED_OUTPUTS_WARNINGS;
  private Vector<InfoConsistency> V_UNUSUED_OUTPUT_LABELS_WARNINGS;
  private Hashtable<String,int[]> ht_UNUSUED_LABELS;
  private String fis_name= null;
  //private JKBInterpretabilityFrame jkbif;
  //------------------------------------------------------------------------------
  //public JConsistency(JExpertFrame jexp, JKBCT kbct) {
  public JConsistency(JKBCT kbct) {
    //this.parent= jexp;
	WARNING= false;
    this.kbct= kbct;
  }
  //------------------------------------------------------------------------------
  /**
   * Analysis of consistency (Check consistency of the knowledge base).
   * Result of checking is available from getVectors().
   * @param ReduceDataBase flag for analysis of consistency under a data base reduction process
   */
  public void AnalysisOfConsistency(boolean ReduceDataBase) {
     int NbInputs= this.kbct.GetNbInputs();
     int NbOutputs= this.kbct.GetNbOutputs();
     int NbVariables= NbInputs + NbOutputs;
     int NbRules= this.kbct.GetNbRules();
     this.V_CONSISTENCY_ERRORS= new Vector<InfoConsistency>();
     this.V_REDUNDANCY_OR_SPECIFICITY_WARNINGS= new Vector<InfoConsistency>();
     this.V_UNUSUED_INPUTS_WARNINGS= new Vector<InfoConsistency>();
     this.V_UNUSUED_INPUT_LABELS_WARNINGS= new Vector<InfoConsistency>();
     this.V_UNUSUED_OUTPUTS_WARNINGS= new Vector<InfoConsistency>();
     this.V_UNUSUED_OUTPUT_LABELS_WARNINGS= new Vector<InfoConsistency>();
     int UNUSUED_VARIABLES[]= new int [NbVariables];
     for (int n=0; n < UNUSUED_VARIABLES.length; n++)
       UNUSUED_VARIABLES[n] = 0;

     this.init_ht_UNUSUED_LABELS(NbVariables, NbInputs);
     for (int n=0; n<NbRules; n++) {
       int RuleNumber = n+1;
       Rule r1= this.kbct.GetRule(RuleNumber);
       if (r1.GetActive()) {
         int[] inLabels= r1.Get_in_labels_number();
         int[] outLabels= r1.Get_out_labels_number();
         if (!ReduceDataBase) {
           for (int m=RuleNumber; m < NbRules; m++) {
             Rule r2= this.kbct.GetRule(m+1);
             if (r2.GetActive()) {
               if (SamePremise(r1, r2)) {
                 InfoConsistency ic= new InfoConsistency();
                 ic.setRuleNum1(RuleNumber);
                 ic.setRuleNum2(m+1);
                 if (DifferentConclusions(r1, r2))
                     ic.setMessage1("SamePremiseDifferentConclussions");
                 else
                     ic.setMessage1("SamePremiseSameConclussions");

                 this.V_CONSISTENCY_ERRORS.add(ic);
               } else {
                 boolean c1 = this.Covered(r1, r2);
                 boolean c2 = this.Covered(r2, r1);
                 InfoConsistency ic= new InfoConsistency();
                 if (c1 && c2) {
                   ic.setRuleNum1(RuleNumber);
                   ic.setRuleNum2(m+1);
                   ic.setMessage1("HaveIntersectionNoEmpty");
                   if (DifferentConclusions(r1, r2))
                     ic.setMessage2("DifferentConclusions");
                   else
                     ic.setMessage2("SameConclusions");

                   this.V_REDUNDANCY_OR_SPECIFICITY_WARNINGS.add(ic);
                 } else if (c2) {
                   ic.setRuleNum1(RuleNumber);
                   ic.setRuleNum2(m+1);
                   ic.setMessage1("IsIncludedIntoTheOneCoveredByTheRule");
                   if (DifferentConclusions(r1, r2))
                     ic.setMessage2("DifferentConclusions");
                   else
                     ic.setMessage2("SameConclusions");
                   this.V_REDUNDANCY_OR_SPECIFICITY_WARNINGS.add(ic);
                 } else if (c1) {
                   ic.setRuleNum1(m+1);
                   ic.setRuleNum2(RuleNumber);
                   ic.setMessage1("IsIncludedIntoTheOneCoveredByTheRule");
                   if (DifferentConclusions(r1, r2))
                     ic.setMessage2("DifferentConclusions");
                   else
                     ic.setMessage2("SameConclusions");

                   this.V_REDUNDANCY_OR_SPECIFICITY_WARNINGS.add(ic);
                 } else if (this.CommomPart(r1, r2)) {
                     ic.setRuleNum1(RuleNumber);
                     ic.setRuleNum2(m+1);
                     ic.setMessage1("HaveIntersectionNoEmptyCommomPart");
                     if (DifferentConclusions(r1, r2))
                       ic.setMessage2("DifferentConclusions");
                     else
                       ic.setMessage2("SameConclusions");

                     this.V_REDUNDANCY_OR_SPECIFICITY_WARNINGS.add(ic);
                 }
               }
             }
           }
           if (this.IncompleteRule(RuleNumber, this.kbct)) {
               InfoConsistency ic= new InfoConsistency();
               ic.setRuleNum1(RuleNumber);
               ic.setMessage1("IsIncomplete_ConclusionNotDefined");
               this.V_CONSISTENCY_ERRORS.add(ic);
           } else {
               int L= outLabels.length;
               for (int m=0; m<L; m++)
                 if (outLabels[m]==0) {
                   InfoConsistency ic= new InfoConsistency();
                   ic.setVarNum(m+1);
                   ic.setRuleNum1(RuleNumber);
                   ic.setMessage1("IsNotDefined");
                   this.V_UNUSUED_OUTPUTS_WARNINGS.add(ic);
                 }
           }
         }
         for (int m=0; m < NbVariables; m++) {
           int[] UNUSUED_LABELS = (int[]) ht_UNUSUED_LABELS.get("" + m);
           int NOL= UNUSUED_LABELS.length;
           if ( (m < NbInputs) && (inLabels[m] != 0)) {
             if (UNUSUED_VARIABLES[m] == 0)
               UNUSUED_VARIABLES[m] = 1;
         	 
             if (inLabels[m] > NOL) {
                 if (inLabels[m] <= 2*NOL) {
                   // Composite NOT labels
                   UNUSUED_LABELS[inLabels[m]-NOL-1]= 1;
                   if (inLabels[m]-NOL-2 >= 0)
                     UNUSUED_LABELS[inLabels[m]-NOL-2]= 1;

                   if (inLabels[m]-NOL < NOL)
                     UNUSUED_LABELS[inLabels[m]-NOL]= 1;
                 } else {
                   // Composite OR labels
                   int SelLabel= inLabels[m]-2*NOL;
                   int NbNewRules= jnikbct.NbORLabels(SelLabel, NOL);
                   int option= jnikbct.option(SelLabel, NbNewRules, NOL);
                   for (int k=0; k<NOL; k++) {
                     if ( (k >= option-2) && (k+1 <= option+NbNewRules) )
                         UNUSUED_LABELS[k]= 1;
                   }
                 }
             } else {
            	 UNUSUED_LABELS[inLabels[m]-1] = 1;
             }
           }
           else if ( (m >= NbInputs) && (outLabels[m-NbInputs] != 0)) {
             if (UNUSUED_VARIABLES[m] == 0)
               UNUSUED_VARIABLES[m] = 1;

             UNUSUED_LABELS[outLabels[m-NbInputs]-1] = 1;
           }
           ht_UNUSUED_LABELS.put("" + m, UNUSUED_LABELS);
         }
       }
     }

     for (int n=0; n < NbVariables; n++) {
       if (UNUSUED_VARIABLES[n] == 0) {
         InfoConsistency ic= new InfoConsistency();
         if (n < NbInputs){
           ic.setVarNum(n+1);
           this.V_UNUSUED_INPUTS_WARNINGS.add(ic);
         } else {
           ic.setVarNum(n - NbInputs + 1);
           ic.setMessage1("");
           this.V_UNUSUED_OUTPUTS_WARNINGS.add(ic);
         }
       } else {
         int UNUSUED_LABELS[] = (int[]) ht_UNUSUED_LABELS.get("" + n);
         for (int m = 0; m < UNUSUED_LABELS.length; m++) {
           if (UNUSUED_LABELS[m] == 0) {
             InfoConsistency ic= new InfoConsistency();
             ic.setLabelNum(m);
             if (n < NbInputs) {
               ic.setVarNum(n+1);
               if (this.kbct.GetInput(n + 1).GetScaleName().equals("user"))
                 ic.setUser(true);
               else
                 ic.setUser(false);

               this.V_UNUSUED_INPUT_LABELS_WARNINGS.add(ic);
             } else {
               ic.setVarNum(n - NbInputs + 1);
               if (this.kbct.GetOutput(n - NbInputs + 1).GetScaleName().equals("user"))
                 ic.setUser(true);
               else
                 ic.setUser(false);

               this.V_UNUSUED_OUTPUT_LABELS_WARNINGS.add(ic);
             }
           }
         }
       }
     }
     this.CONSISTENCY_ERRORS= this.V_CONSISTENCY_ERRORS.elements();
     this.REDUNDANCY_OR_SPECIFICITY_WARNINGS= this.V_REDUNDANCY_OR_SPECIFICITY_WARNINGS.elements();
     this.UNUSUED_INPUTS_WARNINGS= this.V_UNUSUED_INPUTS_WARNINGS.elements();
     this.UNUSUED_INPUT_LABELS_WARNINGS= this.V_UNUSUED_INPUT_LABELS_WARNINGS.elements();
     this.UNUSUED_OUTPUTS_WARNINGS= this.V_UNUSUED_OUTPUTS_WARNINGS.elements();
     this.UNUSUED_OUTPUT_LABELS_WARNINGS= this.V_UNUSUED_OUTPUT_LABELS_WARNINGS.elements();
     if (this.CONSISTENCY_ERRORS.hasMoreElements() ||
         this.REDUNDANCY_OR_SPECIFICITY_WARNINGS.hasMoreElements() ||
         this.UNUSUED_INPUTS_WARNINGS.hasMoreElements() ||
         this.UNUSUED_INPUT_LABELS_WARNINGS.hasMoreElements() ||
         this.UNUSUED_OUTPUTS_WARNINGS.hasMoreElements() ||
         this.UNUSUED_OUTPUT_LABELS_WARNINGS.hasMoreElements() || ReduceDataBase )
            this.WARNING= true;
  }
  //------------------------------------------------------------------------------
  /**
   * Analysis of redundancy or specificity.
   * @param kbctsimp knowledge base
   * @param r1 merged rule
   * @param rule_number_r1 number of the first rule to merge
   * @param rule_number_r2 number of the second rule to merge
   * @return true (there is at least one redundancy or specificity warning) / false
   */
  private boolean IsThereAnyRedundancyOrSpecificityWarning(JKBCT kbctsimp, Rule r1, int rule_number_r1, int rule_number_r2) {
    int NbInputs= kbctsimp.GetNbInputs();
    int NbOutputs= kbctsimp.GetNbOutputs();
    int NbVariables= NbInputs + NbOutputs;
    int NbRules= kbctsimp.GetNbRules();
    this.init_ht_UNUSUED_LABELS(NbVariables, NbInputs);
    int NbConfR1= this.CountConflicts(kbctsimp, kbctsimp.GetRule(rule_number_r1), NbRules);
    int NbConfR2= this.CountConflicts(kbctsimp, kbctsimp.GetRule(rule_number_r2), NbRules);
    int NbConfR= this.CountConflicts(kbctsimp, r1, NbRules);
    if (NbConfR <= NbConfR1+NbConfR2)
      return false;
    else
      return true;
  }
  //------------------------------------------------------------------------------
  /**
   * Analysis of redundancy or specificity.
   * @param kbctsimp knowledge base
   * @param rule_number number of the rule added to kbct
   * @param r1 rule for analysis
   * @param NbRules number of rules in kbctsimp
   * @return number of conflicts generated by the rule r1
   */
  private int CountConflicts (JKBCT kbctsimp, Rule r1, int NbRules) {
    int result= 0;
    for (int n=0; n<NbRules; n++) {
        int RuleNumber = n+1;
        Rule r2= kbctsimp.GetRule(RuleNumber);
        if (r2.GetActive()) {
   	       long TM=Runtime.getRuntime().totalMemory();
	       long MM=Runtime.getRuntime().maxMemory();
	       if (TM>=MM) {
               if (!MainKBCT.getConfig().GetTESTautomatic()) {
	    	     String message= LocaleKBCT.GetString("WarningSimplificationHalted")+"\n"+
                 LocaleKBCT.GetString("WarningJVMOutOfMemory")+"\n"+
                 LocaleKBCT.GetString("WarningSimplificationSaveKB")+"\n"+
                 LocaleKBCT.GetString("WarningReleaseMemory");
                 if (!MainKBCT.flagHalt) {
	    	         MessageKBCT.Information(null, message);
                 }
               }
               if (!MainKBCT.flagHalt) {
                   MessageKBCT.WriteLogFile(LocaleKBCT.GetString("WarningSimplificationHalted"), "Simplify");
                   MainKBCT.flagHalt= true;
               }
               break;
	       } else {
              if (SamePremise(r1, r2)) {
                if (DifferentConclusions(r1, r2))
                    result++;
              } else {
                boolean c1 = this.Covered(r1, r2);
                boolean c2 = this.Covered(r2, r1);
                if (c1 && c2) {
                  if (DifferentConclusions(r1, r2))
                    result++;
                } else if (c1 || c2) {
                  if (DifferentConclusions(r1, r2))
                    result++;
                } else if (this.CommomPart(r1, r2)) {
                    if (DifferentConclusions(r1, r2))
                        result++;
                }
              }
           }
        }
    }
    return result;
  }
  //------------------------------------------------------------------------------
  /**
   * Analysis of redundancy or specificity.
   * @param kbctsimp knowledge base
   * @return number of conflicts
   */
  public int ContLinguisticConflicts(JKBCT kbctsimp) {
    int NbInputs= kbctsimp.GetNbInputs();
    int NbOutputs= kbctsimp.GetNbOutputs();
    int NbVariables= NbInputs + NbOutputs;
    int NbRules= kbctsimp.GetNbRules();
    this.init_ht_UNUSUED_LABELS(NbVariables, NbInputs);
    int NbConf=0;
    for (int n=0; n<NbRules; n++) {
    	if (kbctsimp.GetRule(n+1).GetActive())
            NbConf= NbConf + this.CountConflicts(kbctsimp, kbctsimp.GetRule(n+1), NbRules);
    }
    return NbConf;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left"> Check if two rules have same premises. </p>
   * <p align="left">
   * Return:
   * <ul>
   *   <li> true (same premises) </li>
   *   <li> false (different premises) </li>
   * </ul>
   * </p>
   * @param r1 first rule
   * @param r2 second rule
   * @return true (same premise) / false (different premise)
   */
  public static boolean SamePremise(Rule r1, Rule r2) {
    int[] inLabels1= r1.Get_in_labels_number();
    int[] inLabels2= r2.Get_in_labels_number();
    int L1= inLabels1.length;
    int L2= inLabels2.length;
    if (L1!=L2)
      return false;

    for (int n=0; n<L1; n++)
      if (inLabels1[n]!=inLabels2[n])
        return false;

    return true;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left"> Check if two rules have different conclusions. </p>
   * <p align="left">
   * Return:
   * <ul>
   *   <li> true (different conclusions) </li>
   *   <li> false (same conclusions) </li>
   * </ul>
   * </p>
   * @param r1 first rule
   * @param r2 second rule
   * @return true (different conclusions) / false (same conclusions)
   */
  public static boolean DifferentConclusions(Rule r1, Rule r2) {
    int[] outLabels1= r1.Get_out_labels_number();
    int[] outLabels2= r2.Get_out_labels_number();
    int L1= outLabels1.length;
    int L2= outLabels2.length;
    if (L1!=L2)
      return true;

    for (int n=0; n<L1; n++)
      if (outLabels1[n]!=outLabels2[n])
        return true;

    return false;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left"> Check if the input space covered by the rule r2
   *                  is included into the one covered by the rule r1. </p>
   * <p align="left">
   * Return:
   * <ul>
   *   <li> true (included) </li>
   *   <li> false (not included) </li>
   * </ul>
   * </p>
   * @param r1 first rule
   * @param r2 second rule
   * @return true (r2 included into r1) / false
   */
  public boolean Covered(Rule r1, Rule r2) {
    int[] inLabels1= r1.Get_in_labels_number();
    int[] inLabels2= r2.Get_in_labels_number();
    int L1= inLabels1.length;
    int L2= inLabels2.length;
    if (L1!=L2)
      return false;

    boolean warning1= false;
    boolean warning2= false;
    boolean warning3= false;
    boolean warning4= false;
    boolean warning5= false;
    boolean warning6= false;
    for (int n = 0; n < L1; n++) {
      int NOL= ((int[]) ht_UNUSUED_LABELS.get("" + n)).length;
      if ((inLabels1[n] != inLabels2[n]) && (inLabels2[n] != 0)) {
          if (inLabels1[n]==0)
              warning1= true;
          else if (inLabels1[n] > NOL) {
               if (inLabels1[n] <= 2*NOL) {
                   if (inLabels2[n] <= NOL) {
                     // inLabels1[n] -> NOT
                     // inLabels2[n] -> Basic Label
                     if (inLabels2[n] != inLabels1[n] - NOL)
                       warning1 = true;
                     else
                       return false;

                   } else if ( (inLabels2[n] > NOL) && (inLabels2[n] <= 2*NOL) ) {
                     // inLabels1[n] -> NOT
                     // inLabels2[n] -> NOT
                     // There is not inclusion
                       return false;
                   } else if (inLabels2[n] > 2*NOL) {
                     // inLabels1[n] -> NOT
                     // inLabels2[n] -> OR
                       int SelLabel= inLabels2[n]-2*NOL;
                       int NbNewRules= jnikbct.NbORLabels(SelLabel, NOL);
                       int option= jnikbct.option(SelLabel, NbNewRules, NOL);
                       if ( (inLabels1[n]-NOL < option) || (inLabels1[n]-NOL >= option+NbNewRules) )
                             warning3= true;
                       else
                             warning4= true;
                   }
               } else {
                   if (inLabels2[n] <= NOL) {
                     // inLabels1[n] -> OR
                     // inLabels2[n] -> Basic Label
                     int SelLabel= inLabels1[n]-2*NOL;
                     int NbNewRules= jnikbct.NbORLabels(SelLabel, NOL);
                     int option= jnikbct.option(SelLabel, NbNewRules, NOL);
                     int[] labels= new int[NbNewRules];
                     for (int k=0; k<NbNewRules; k++)
                          labels[k]= option+k;

                     for (int k=0; k<NOL; k++)
                       for (int i=0; i<NbNewRules; i++)
                          if ( (k+1==labels[i]) && (inLabels2[n]==k+1) )
                              warning2= true;

                     if (warning2) {
                         warning2= false;
                         warning1= true;
                     }
                     else
                         return false;
                  } else if ( (inLabels2[n] > NOL) && (inLabels2[n] <= 2*NOL) ) {
                       // inLabels1[n] -> OR
                       // inLabels2[n] -> NOT
                       int SelLabel= inLabels1[n]-2*NOL;
                       int NbNewRules= jnikbct.NbORLabels(SelLabel, NOL);
                       int option= jnikbct.option(SelLabel, NbNewRules, NOL);
                       if ( (inLabels2[n]-NOL < option) || (inLabels2[n]-NOL >= option+NbNewRules) )
                             warning6= true;
                       else
                             return false;
                  } else if (inLabels2[n] > 2*NOL) {
                       // inLabels1[n] -> OR
                       // inLabels2[n] -> OR
                       int SelLabel1= inLabels1[n]-2*NOL;
                       int NbNewRules1= jnikbct.NbORLabels(SelLabel1, NOL);
                       int option1= jnikbct.option(SelLabel1, NbNewRules1, NOL);
                       int SelLabel2= inLabels2[n]-2*NOL;
                       int NbNewRules2= jnikbct.NbORLabels(SelLabel2, NOL);
                       int option2= jnikbct.option(SelLabel2, NbNewRules2, NOL);
                       if ( (option1<=option2) && (option1+NbNewRules1>=option2+NbNewRules2) )
                         warning3= true;
                       else {
                         if ((option2<=option1) && (option2+NbNewRules2>=option1+NbNewRules1)) {
                           warning4= true;
                           warning6= true;
                         } else
                           return false;
                       }
                  }
               }
           } else if (inLabels1[n] <= NOL) {
               if (inLabels2[n] <= NOL) {
                    // inLabels1[n] -> Basic Label
                    // inLabels2[n] -> Basic Label
                    return false;
               }
               else if ( (inLabels2[n] > NOL) && (inLabels2[n] <= 2*NOL) ) {
                    // inLabels1[n] -> Basic Label
                    // inLabels2[n] -> NOT
                    if (inLabels1[n] == inLabels2[n] - NOL) {
                        return false;
                    } else
                        warning5= true;
               }
               else if (inLabels2[n] > 2*NOL) {
                    // inLabels1[n] -> Basic Label
                    // inLabels2[n] -> OR
                    int SelLabel= inLabels2[n]-2*NOL;
                    int NbNewRules= jnikbct.NbORLabels(SelLabel, NOL);
                    int option= jnikbct.option(SelLabel, NbNewRules, NOL);
                    if ( (option>inLabels1[n]) || (option+NbNewRules<=inLabels1[n]) )
                          return false;
                    else
                          warning5= true;
               }
           }
      }
    }
    if (warning4)
      if (!warning6)
        return false;

    if (warning5)
      if ( (!warning1) && (!warning3) )
        return false;

    if (warning3)
        return true;

    if (warning1)
        return true;

    return false;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left"> Check if there is no empty intersection between r1 and r2. </p>
   * <p align="left">
   * Return:
   * <ul>
   *   <li> true (no empty intersection between r1 and r2) </li>
   *   <li> false </li>
   * </ul>
   * </p>
   * @param r1 first rule
   * @param r2 second rule
   * @return true (no empty intersection between r1 and r2) / false
   */
  public boolean CommomPart(Rule r1, Rule r2) {
    //System.out.println("CommonPart: r1="+r1.GetNumber()+"  r2="+r2.GetNumber());
	int[] inLabels1= r1.Get_in_labels_number();
    int[] inLabels2= r2.Get_in_labels_number();
    int L1= inLabels1.length;
    int L2= inLabels2.length;
    if (L1!=L2)
      return false;

    for (int n = 0; n < L1; n++) {
      int NOL= ((int[]) ht_UNUSUED_LABELS.get("" + n)).length;
      //System.out.println("CommonPart: in1="+inLabels1[n]+"  in2="+inLabels2[n]);
      if ( (inLabels1[n]!=inLabels2[n]) && 
    	   !((inLabels1[n] > NOL) && (inLabels1[n] <= 2*NOL) && (inLabels2[n] > NOL) && (inLabels2[n] <= 2*NOL)) &&
    	   !((inLabels1[n] > 2*NOL) && (inLabels2[n] > NOL) && (inLabels2[n] <= 2*NOL)) && 
    	   !((inLabels1[n] > NOL) && (inLabels1[n] <= 2*NOL) && (inLabels2[n] > 2*NOL)) ) {
        if ( (inLabels1[n] > 0) && (inLabels1[n] <= NOL) && (inLabels2[n] > 0) && (inLabels2[n] <= NOL) ) {
      	    // L1 -> basic
    	    // L2 -> basic
            //System.out.println("CommonPart: false 1");
    	    return false;
        } else if ( (inLabels1[n] > 0) && (inLabels1[n] <= NOL) && (inLabels2[n] > 2*NOL) ) {
    	    // L1 -> basic
    	    // L2 -> OR
            int SelLabel= inLabels2[n]-2*NOL;
            int NbNewRules= jnikbct.NbORLabels(SelLabel, NOL);
            int option= jnikbct.option(SelLabel, NbNewRules, NOL);
            if ( (option>inLabels1[n]) || (option+NbNewRules<=inLabels1[n]) ) {
                //System.out.println("CommonPart: false 2");
                return false;
            }
        } else if ( (inLabels1[n] > 0) && (inLabels1[n] <= NOL) && (inLabels2[n] > NOL) && (inLabels2[n] <= 2*NOL) && (inLabels2[n]-NOL==inLabels1[n]) ) {
    	    // L1 -> basic
    	    // L2 -> NOT
            //System.out.println("CommonPart: false 3");
    	    return false;
        } else if ( (inLabels1[n] > 2*NOL) && (inLabels2[n] > 0) && (inLabels2[n] <= NOL) ) {
      	    // L1 -> OR
    	    // L2 -> basic
            int SelLabel= inLabels1[n]-2*NOL;
            int NbNewRules= jnikbct.NbORLabels(SelLabel, NOL);
            int option= jnikbct.option(SelLabel, NbNewRules, NOL);
            if ( (option>inLabels2[n]) || (option+NbNewRules<=inLabels2[n]) ) {
                  //System.out.println("CommonPart: false 4");
                  return false;
            }
        } else if ( (inLabels1[n] > 2*NOL) && (inLabels2[n] > 2*NOL) && (inLabels1[n]!=inLabels2[n]) ) {
    	    // L1 -> OR
    	    // L2 -> OR
            int SelLabel1= inLabels1[n]-2*NOL;
            int NbNewRules1= jnikbct.NbORLabels(SelLabel1, NOL);
            int option1= jnikbct.option(SelLabel1, NbNewRules1, NOL);
            int SelLabel2= inLabels2[n]-2*NOL;
            int NbNewRules2= jnikbct.NbORLabels(SelLabel2, NOL);
            int option2= jnikbct.option(SelLabel2, NbNewRules2, NOL);
            //System.out.println("o1="+option1+"  nr1="+NbNewRules1);
            //System.out.println("o2="+option2+"  nr2="+NbNewRules2);
            if ( (option1 > option2+NbNewRules2-1) ||
            	 (option2 > option1+NbNewRules1-1) ) {
                  //System.out.println("CommonPart: false 5");
                  return false;
            }
        } else if ( (inLabels1[n] > NOL) && (inLabels1[n] <= 2*NOL) && (inLabels2[n] > 0) && (inLabels2[n] <= NOL) && (inLabels1[n]-NOL==inLabels2[n]) ) {
    	    // L1 -> NOT
    	    // L2 -> basic
            //System.out.println("CommonPart: false 6");
    	    return false;
        } 
      }
    }
    //System.out.println("CommonPart: true");
    return true;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left"> Check if the rule "RuleNumber" is incomplete
   *                  (conclusion of the rule is not defined).
   * </p>
   * <p align="left">
   * Return:
   * <ul>
   *   <li> true (incomplete) </li>
   *   <li> false (not incomplete) </li>
   * </ul>
   * </p>
   * @param RuleNumber number of the rule
   * @param kbct knowledge base
   * @return true (incomplete) / false (not incomplete)
   */
  public boolean IncompleteRule(int RuleNumber, JKBCT kbct) {
    Rule r= kbct.GetRule(RuleNumber);
    int[] outLabels= r.Get_out_labels_number();
    int L= outLabels.length;
    for (int n=0; n<L; n++)
      if (outLabels[n]!=0)
        return false;

    return true;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   *  Check if there are labels which are always used together.
   * </p>
   * @param kbct knowledge base
   * @param var_type type of the variable
   * @param var_num number of the variable
   * @param lab_num number of the label
   * @return one InfoConsistency object for each two labels to group in this variable
   */
  public InfoConsistency GroupLabels( JKBCT kbct, String var_type, int var_num, int lab_num) {
    int n=var_num-1;
    if (var_type.equals("Input")) {
       int NbInputs=kbct.GetNbInputs();
       int m=lab_num;
       while ( (n<NbInputs) ) {
         JKBCTInput in= kbct.GetInput(n+1);
         if ( (in.GetType().equals("numerical")) && (!in.GetTrust().equals("hhigh")) ) {
             int NbLabels= in.GetLabelsNumber();
             int[] USED_LABELS= this.UsedLabels(n+1, NbLabels, "INPUT");
             while (m<=NbLabels-1) {
                   if ( (!this.isLabelUsed(m, USED_LABELS, 0, false, 0)) && (!this.isLabelUsedInOR(m, "end", USED_LABELS,NbLabels)) ) {
                       if ( (!this.isLabelUsed(m+1, USED_LABELS, 0, false, 0)) && (!this.isLabelUsedInOR(m+1,"begin",USED_LABELS,NbLabels)) ) {
                            if (this.areUsedTogether(m,m+1,USED_LABELS, NbLabels))
                                return this.BuildIcForGroupLabels(2, m, in, n, m, "Input");
                            else
                                m=m+1;
                       } else
                       m=m+1;
                   } else
                   m=m+1;
             }
             n++;
             m=1;
         } else {
        	 n++;
         }
       }
    }
    return null;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Build an InfoConsistency object with information about labels to group.
   * </p>
   * @param NbOR number of labels to group
   * @param option number of the first basic label to group
   * @param var JVariable object (the variable)
   * @param n number of the variable - 1
   * @param k number of the label - 1
   * @param var_type type of the variable
   * @return InfoConsistency object with information about labels to group in this variable
   */
  private InfoConsistency BuildIcForGroupLabels(int NbOR, int option, JVariable var, int n, int k, String var_type) {
    InfoConsistency ic= new InfoConsistency();
    String error=LocaleKBCT.GetString("LabelsAlwaysUsedTogether")
                 + " '"+var.GetName()
                 + "' ("+ LocaleKBCT.GetString("Imp") + ": "
                 + LocaleKBCT.GetString(var.GetTrust()) + ") :";
    int[] gl= new int[NbOR];
    for (int l=option; l<option+NbOR; l++) {
      if (l==option) {
          if (var.GetScaleName().equals("user"))
              error = error + "\n"+"     " + var.GetUserLabelsName(l-1);
          else
              error = error + "\n"+"     " + LocaleKBCT.GetString(var.GetLabelsName(l-1));
      } else {
          if (var.GetScaleName().equals("user"))
              error = error + ", " + var.GetUserLabelsName(l-1);
          else
              error = error + ", " + LocaleKBCT.GetString(var.GetLabelsName(l-1));
      }
      gl[l-option]=l;
    }
    String solution=LocaleKBCT.GetString("GroupLabels");
    ic.setError(error);
    ic.setSolution(solution);
    ic.setVarType(var_type);
    ic.setVarNum(n+1);
    ic.setGroupLabels(gl);
    ic.setLabelNum(k-1);
    return ic;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Check if label "lab_number" is used in any rule.
   * </p>
   * @param lab_number number of the label
   * @param USED_LABELS labels used for this variable: 1 (label used) / 0 (label not used)
   * @param NOL number of labels of this variable
   * @param warning composite label
   * @param OR_lab_number number of composite label with OR operator
   * @return true (label used in at least one rule) / false
   */
  public boolean isLabelUsed(int lab_number, int[] USED_LABELS, int NOL, boolean warning, int OR_lab_number) {
    if (USED_LABELS[lab_number-1]==1) {
      return true;
    } else if (warning) {
      if (USED_LABELS[NOL+lab_number-1]==1) {
        return true;
      }
      for (int n=0; n<NOL-3; n++) {
        int NbORLabels= n+2;
        int lim=lab_number;
        if (lim > NOL - NbORLabels + 1)
          lim= NOL - NbORLabels + 1;

        int ini= lab_number-NbORLabels;
        if (ini <= lim - (NOL - n - 1) )
          ini= lim - (NOL - n - 1);

        if (ini < 0)
          ini=0;

        for (int k=ini; k<lim; k++) {
          int ORLabel= jnikbct.NumberORLabel(k+1, NbORLabels, NOL);
          if ( (OR_lab_number != ORLabel) && (USED_LABELS[ORLabel-1]==1) ) {
            return true;
          }
        }
      }
    }
    return false;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Check if labels "lab_number_1" and "lab_number_2" are used together in any rule.
   * </p>
   * @param lab_number_1 label 1
   * @param lab_number_2 label 2
   * @param USED_LABELS used labels
   * @param NOL number of labels
   * @return true ("lab_number_1" and "lab_number_2" are used together in at least one rule) / false
   */
  public boolean areUsedTogether(int lab_number_1, int lab_number_2, int[] USED_LABELS, int NOL) {
    for (int n=1;n<lab_number_1;n++) {
      if (USED_LABELS[NOL+n-1]==1)
        return true;
    }
    for (int n=lab_number_2+1;n<NOL+1;n++) {
      if (USED_LABELS[NOL+n-1]==1)
        return true;
    }
    for (int n=0; n<NOL-3; n++) {
      int number= 2*NOL + n*(NOL-1) + lab_number_1;
      if ( (n<NOL-lab_number_1) && (number<=USED_LABELS.length) && (USED_LABELS[number - 1]==1) )
        return true;

      if (n>0) {
        for (int m=lab_number_1 - n; m<lab_number_1; m++) {
          number= 2*NOL + n*(NOL-1) + m;
          if ( (number<=USED_LABELS.length) && (USED_LABELS[number - 1]==1) )
            return true;
        }
      }
    }
    return false;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Check if basic label "lab_number" is used in any OR label.
   * </p>
   * @param lab_number number of the label
   * @param option begining or end
   * @param USED_LABELS used labels
   * @param NOL number of labels
   * @return true ("lab_number" is used in any OR label) / false
   */
  public boolean isLabelUsedInOR(int lab_number, String option, int[] USED_LABELS, int NOL) {
    // option= begin
    // option= end
    if (option.equals("begin")) {
       for (int n=0; n<NOL-3; n++) {
         if ( (lab_number<=NOL-1-n) && (2*NOL+n*(NOL-1)+lab_number-1 < USED_LABELS.length) && (USED_LABELS[2*NOL+n*(NOL-1)+lab_number-1]==1) ) {
           return true;
         }
       }
    } else if (option.equals("end")) {
       for (int n=0; n<NOL-3; n++) {
         if ( (lab_number>n+1) && (2*NOL+n*(NOL-1)+lab_number-(n+1)-1 < USED_LABELS.length) && (USED_LABELS[2*NOL+n*(NOL-1)+lab_number-(n+1)-1]==1) ) {
           return true;
         }
      }
    }
    return false;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Build an array with labels used in any rule.
   * Array dimension is equal to number of labels (Basic Labels + NOT Labels + OR Labels).
   * </p>
   * @param var_number number of the variable
   * @param NbLabels number of labels of the variable
   * @param var_type type of the variable
   * @return array of int (1 (label used) / 0 (label not used))
   */
  public int[] UsedLabels(int var_number, int NbLabels, String var_type) {
    int DefLabels=2*NbLabels+jnikbct.serie(NbLabels-1)-3;
	if (NbLabels<=3)
	   	DefLabels= DefLabels+NbLabels;
	    
	int[] USED_LABELS= new int [DefLabels];
    for (int m=0; m < USED_LABELS.length; m++)
      USED_LABELS[m]=0;

    int NbRules= this.kbct.GetNbRules();
    for (int n=0; n<NbRules; n++) {
      Rule r= this.kbct.GetRule(n+1);
      if (r.GetActive()) {
        int SelLabel=0;
        if (var_type.equals("INPUT"))
           SelLabel= r.Get_in_labels_number()[var_number-1];
        else if (var_type.equals("OUTPUT"))
           SelLabel= r.Get_out_labels_number()[var_number-1];

        if ( (SelLabel > 0) && (SelLabel<=USED_LABELS.length) )
          USED_LABELS[SelLabel - 1] = 1;

        if ( (SelLabel>NbLabels) && (SelLabel<=2*NbLabels) )
          USED_LABELS[SelLabel - 1 - NbLabels] = 1;

      }
    }
    return USED_LABELS;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Init arrays "UNUSUED LABELS" for each variable (inputs/outputs) in the knowledge base.
   * </p>
   * @param NbVariables number of variables
   * @param NbInputs number of inputs
   * @return number of labels of the last variable
   */
  private int init_ht_UNUSUED_LABELS(int NbVariables, int NbInputs) {
    int NbLabels=0;
    this.ht_UNUSUED_LABELS= new Hashtable<String,int[]>();
    for (int n=0; n < NbVariables; n++) {
      if (n < NbInputs)
        NbLabels = this.kbct.GetInput(n + 1).GetLabelsNumber();
      else
        NbLabels = this.kbct.GetOutput(n - NbInputs + 1).GetLabelsNumber();

      int UNUSUED_LABELS[]= new int [NbLabels];
      for (int m=0; m < UNUSUED_LABELS.length; m++)
        UNUSUED_LABELS[m]=0;

      this.ht_UNUSUED_LABELS.put(""+n, UNUSUED_LABELS);
    }
    return NbLabels;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Simplify Rule Base contains in "kbct".
   * </p>
   * @param kbct knowledge base to be simplified
   * @param jcf JConsistencyFrame object: used for solving problems
   * @return simplified knowledge base
   */
  public JKBCT SimplifyRuleBase( JKBCT kbct, JExtendedDataFile jedf, double[][] qold) {
	    //System.out.println("JConsistency: SimplifyRuleBase1 -> htsize="+jnikbct.getHashtableSize());
	    //System.out.println("JConsistency: SimplifyRuleBase1 -> id="+jnikbct.getId());
	boolean change= false;
    JKBCT kbctaux= new JKBCT(kbct);
    //long ptr= kbctaux.GetPtr();
    kbctaux.SetKBCTFile(kbct.GetKBCTFile());
    int NbInputs= kbctaux.GetNbInputs();
    int NbOutputs= kbctaux.GetNbOutputs();
    this.init_ht_UNUSUED_LABELS(NbInputs+NbOutputs, NbInputs);
    int[] NbLabelsPerInput= new int[NbInputs];
	String simpRankOpt= MainKBCT.getConfig().GetSimpRuleRanking();
	//System.out.println("simpRankOpt="+simpRankOpt);
    if (!simpRankOpt.equals("false")) {
        for (int n=0; n<NbInputs; n++) {
             NbLabelsPerInput[n]= kbctaux.GetInput(n+1).GetLabelsNumber();
        }
    }
    // Simplify rules
    int contS=0;
    int contIC=0;
    int contDC=0;
    int NbRold= kbctaux.GetNbRules();
	int NbRnew= NbRold;
	MessageKBCT.WriteLogFile("  "+"> Simplify RB", "Simplify");
    //System.out.println("rule ranking");
	//double[][] qnew;
	//JKBCT aux1, aux2;
    //System.out.println("JConsistency: SimplifyRuleBase2 -> htsize="+jnikbct.getHashtableSize());
    //System.out.println("JConsistency: SimplifyRuleBase2 -> id="+jnikbct.getId());
    if (simpRankOpt.equals("ComparisonRuleComplexity")) {
        double int1a, int1b, int1c;
        JKBCT aux1a= this.SimplifyRB(kbctaux);
        aux1a.SetKBCTFile(kbctaux.GetKBCTFile());
        double[][] qnew1a= this.KBquality(aux1a, null, 0, 0, jedf);
        if (!this.GetWorseKBquality(qold, qnew1a)) {
            //int nbR= aux1a.GetNbRules();
            int1a= this.computeInterpLocalRB(aux1a, NbLabelsPerInput, "S");
            //this.jkbif= new JKBInterpretabilityFrame(this.parent, aux1a);
            //this.jkbif.setVisible(false);
            //int1a= this.jkbif.getIntIndex();
            //this.jkbif.dispose();
            //System.out.println("rules(1a)="+nbR);
        } else {
        	int1a=-1;
        }
        MainKBCT.getConfig().SetSimpRuleRanking("IncreasingRuleComplexity");
        JKBCT auxb= this.OrderByInterpretabilityRules(kbctaux, NbLabelsPerInput);
        auxb.SetKBCTFile(kbctaux.GetKBCTFile());
        JKBCT aux1b= this.SimplifyRB(auxb);
        aux1b.SetKBCTFile(auxb.GetKBCTFile());
        double[][] qnew1b= this.KBquality(aux1b, null, 0, 0, jedf);
        if (!this.GetWorseKBquality(qold, qnew1b)) {
            //int nbR= aux1b.GetNbRules();
            int1b=this.computeInterpLocalRB(aux1b, NbLabelsPerInput, "IC");
            //System.out.println("rules(1b)="+nbR);
        } else {
        	int1b=-1;
        }
        MainKBCT.getConfig().SetSimpRuleRanking("DecreasingRuleComplexity");
		JKBCT auxc= this.OrderByInterpretabilityRules(kbctaux, NbLabelsPerInput);
        auxc.SetKBCTFile(kbctaux.GetKBCTFile());
        JKBCT aux1c= this.SimplifyRB(auxc);
        aux1c.SetKBCTFile(auxc.GetKBCTFile());
        double[][] qnew1c= this.KBquality(aux1c, null, 0, 0, jedf);
        if (!this.GetWorseKBquality(qold, qnew1c)) {
            //int nbR= aux1c.GetNbRules();
            int1c=this.computeInterpLocalRB(aux1c, NbLabelsPerInput, "DC");
            //System.out.println("rules(1c)="+nbR);
        } else {
        	int1c=-1;
        }
        //System.out.println("int1a="+int1a);
        //System.out.println("int1b="+int1b);
        //System.out.println("int1c="+int1c);
        if ( (int1a >= 0) && (int1a <= int1b) && (int1a <= int1c) ) {
            kbctaux= aux1a;
            contS++;
	    	//System.out.println("--> S");
        } else if ( (int1b >= 0) && (int1b <= int1a) && (int1b <= int1c) ) {
            kbctaux= aux1b;
            contIC++;
 	    	//System.out.println("--> IC");
        } else if ( (int1c >= 0) && (int1c <= int1a) && (int1c <= int1b) ) {
            kbctaux= aux1c;
            contDC++;
    	    //System.out.println("--> DC");
        }
        MainKBCT.getConfig().SetSimpRuleRanking("ComparisonRuleComplexity");
        //MainKBCT.getConfig().SetSimpRuleRanking("false");
    } else {
       if (!simpRankOpt.equals("false")) {
		   JKBCT aux1= this.OrderByInterpretabilityRules(kbctaux, NbLabelsPerInput);
           aux1.SetKBCTFile(kbctaux.GetKBCTFile());
           kbctaux= aux1;
       }
       //System.out.println("S1");
       JKBCT aux2= this.SimplifyRB(kbctaux);
       aux2.SetKBCTFile(kbctaux.GetKBCTFile());
       //System.out.println("S2");
       double[][] qnew= this.KBquality(aux2, null, 0, 0, jedf);
       if (!this.GetWorseKBquality(qold, qnew))
           kbctaux= aux2;

       //System.out.println("S3");
       NbRnew= kbctaux.GetNbRules();
       MessageKBCT.WriteLogFile("    "+"-> "+LocaleKBCT.GetString("NbTotalRules")+"= "+NbRnew, "Simplify");
       if (NbRold != NbRnew)
           change= true;
      }
      boolean end2= false;
  	  MessageKBCT.WriteLogFile("  "+"> Merge Rules", "Simplify");
	  //  System.out.println("JConsistency: SimplifyRuleBase3 -> htsize="+jnikbct.getHashtableSize());
	  //  System.out.println("JConsistency: SimplifyRuleBase3 -> id="+jnikbct.getId());
      long ptrid= jnikbct.getId();
	  while (!end2) {
         NbRold= kbctaux.GetNbRules();
         simpRankOpt= MainKBCT.getConfig().GetSimpRuleRanking();
         if (simpRankOpt.equals("ComparisonRuleComplexity")) {
             Vector vv1a= this.orderExpertInducedRules(kbctaux);
             JKBCT auxa= (JKBCT)vv1a.get(0);
             Object[] ae1a= (Object[])vv1a.get(1);
             Object[] ai1a= (Object[])vv1a.get(2);
             long ptrauxa= auxa.GetPtr();
             JKBCT aux1a= this.MergeRuleBase(auxa, ae1a, ai1a, jedf, qold);
    	     aux1a.SetKBCTFile(auxa.GetKBCTFile());
    	     auxa.Close();
    	     auxa.Delete();
    	     jnikbct.DeleteKBCT(ptrauxa+1);
             //int nbR= aux1a.GetNbRules();
             double int1a=this.computeInterpLocalRB(aux1a, NbLabelsPerInput, "S");
	         //System.out.println("merge rules(1a)="+nbR);

	         MainKBCT.getConfig().SetSimpRuleRanking("IncreasingRuleComplexity");
    	     JKBCT auxb= this.OrderByInterpretabilityRules(kbctaux, NbLabelsPerInput);
    	     auxb.SetKBCTFile(kbctaux.GetKBCTFile());
             Vector vv1b= this.orderExpertInducedRules(auxb);
             auxb= (JKBCT)vv1b.get(0);
             Object[] ae1b= (Object[])vv1b.get(1);
             Object[] ai1b= (Object[])vv1b.get(2);
             long ptrauxb= auxb.GetPtr();
    	     JKBCT aux1b= this.MergeRuleBase(auxb, ae1b, ai1b, jedf, qold);
    	     aux1b.SetKBCTFile(auxb.GetKBCTFile());
    	     auxb.Close();
    	     auxb.Delete();
    	     jnikbct.DeleteKBCT(ptrauxb+1);
             //nbR= aux1b.GetNbRules();
             double int1b=this.computeInterpLocalRB(aux1b, NbLabelsPerInput, "IC");
	         //System.out.println("merge rules(1b)="+nbR);

    	     MainKBCT.getConfig().SetSimpRuleRanking("DecreasingRuleComplexity");
    	     JKBCT auxc= this.OrderByInterpretabilityRules(kbctaux, NbLabelsPerInput);
    	     auxc.SetKBCTFile(kbctaux.GetKBCTFile());
             Vector vv1c= this.orderExpertInducedRules(auxc);
             auxc= (JKBCT)vv1c.get(0);
             Object[] ae1c= (Object[])vv1c.get(1);
             Object[] ai1c= (Object[])vv1c.get(2);
             long ptrauxc= auxc.GetPtr();
    	     JKBCT aux1c= this.MergeRuleBase(auxc, ae1c, ai1c, jedf, qold);
    	     aux1c.SetKBCTFile(auxc.GetKBCTFile());
    	     auxc.Close();
    	     auxc.Delete();
    	     jnikbct.DeleteKBCT(ptrauxc+1);
             //nbR= aux1c.GetNbRules();
             double int1c=this.computeInterpLocalRB(aux1c, NbLabelsPerInput, "DC");
	         //System.out.println("merge rules(1c)="+nbR);

    	     //System.out.println("int1a="+int1a);
    	     //System.out.println("int1b="+int1b);
    	     //System.out.println("int1c="+int1c);
    	     if ( (int1a <= int1b) && (int1a <= int1c) ) {
    	           kbctaux= aux1a;
    	           contS++;
    	    	   //System.out.println("--> S");
    	     } else if ( (int1b <= int1a) && (int1b <= int1c) ) {
    	           kbctaux= aux1b;
    	           contIC++;
    	    	   //System.out.println("--> IC");
    	     } else if ( (int1c <= int1a) && (int1c <= int1b) ) {
    	           kbctaux= aux1c;
    	           contDC++;
    	    	   //System.out.println("--> DC");
    	     }
    	     MainKBCT.getConfig().SetSimpRuleRanking("ComparisonRuleComplexity");
    	     //MainKBCT.getConfig().SetSimpRuleRanking("false");
    	} else {
    		if (!MainKBCT.getConfig().GetSimpRuleRanking().equals("false")) {
    	         JKBCT aux1= this.OrderByInterpretabilityRules(kbctaux, NbLabelsPerInput);
    	         aux1.SetKBCTFile(kbctaux.GetKBCTFile());
                 kbctaux= aux1;
      	    }
            Vector vv= this.orderExpertInducedRules(kbctaux);
            kbctaux= (JKBCT)vv.get(0);
            Object[] ae= (Object[])vv.get(1);
            Object[] ai= (Object[])vv.get(2);
            // Merge Rules
            long ptraux= kbctaux.GetPtr(); 
            kbctaux= this.MergeRuleBase(kbctaux, ae, ai, jedf, qold);
            jnikbct.DeleteKBCT(ptraux);
            jnikbct.DeleteKBCT(ptraux+1);
        }
        NbRnew= kbctaux.GetNbRules();
        if (NbRold != NbRnew)
          change= true;
        else
          end2= true;
      }
      MessageKBCT.WriteLogFile("    "+"-> "+LocaleKBCT.GetString("NbTotalRules")+"= "+kbctaux.GetNbRules(), "Simplify");
      long[] exc= new long[8];
	  exc[0]= kbctaux.GetPtr();
	  exc[1]= kbctaux.GetCopyPtr();
      exc[2]= -1;
      exc[3]= -1;
      exc[4]= -1;
      exc[5]= -1;
	  exc[6]= -1;
	  exc[7]= -1;
      jnikbct.cleanHashtable(ptrid,exc);
	  //  System.out.println("JConsistency: SimplifyRuleBase4 -> htsize="+jnikbct.getHashtableSize());
	  //  System.out.println("JConsistency: SimplifyRuleBase4 -> id="+jnikbct.getId());
	  ptrid= jnikbct.getId();
      if (jedf != null) {
          // Delete rules without getting worse accuracy over learning set
          if (MainKBCT.getConfig().GetRuleRemoval()) {
             NbRold= kbctaux.GetNbRules();
             // Order rules by weight
             //if (MainKBCT.getConfig().GetOrderRulesByWeight())
             //    kbctaux= this.OrderByWeightRules(kbctaux, jedf, SelectedRules);
         	 MessageKBCT.WriteLogFile("  "+"> Force Rule Removing", "Simplify");
         	 simpRankOpt= MainKBCT.getConfig().GetSimpRuleRanking();
         	 if (simpRankOpt.equals("ComparisonRuleComplexity")) {
                 JKBCT aux1a= this.DeleteNoNeededRules(kbctaux, jedf, qold);
        	     aux1a.SetKBCTFile(kbctaux.GetKBCTFile());
        	     //int nbR= aux1a.GetNbRules();
                 double int1a=this.computeInterpLocalRB(aux1a, NbLabelsPerInput, "S");
    	         //System.out.println("delete rules(1a)="+nbR);

    	         MainKBCT.getConfig().SetSimpRuleRanking("IncreasingRuleComplexity");
        	     JKBCT auxb= this.OrderByInterpretabilityRules(kbctaux, NbLabelsPerInput);
        	     auxb.SetKBCTFile(kbctaux.GetKBCTFile());
                 JKBCT aux1b= this.DeleteNoNeededRules(auxb, jedf, qold);
        	     aux1b.SetKBCTFile(auxb.GetKBCTFile());
        	     //nbR= aux1b.GetNbRules();
                 double int1b=this.computeInterpLocalRB(aux1b, NbLabelsPerInput, "IC");
    	         //System.out.println("delete rules(1b)="+nbR);

        	     MainKBCT.getConfig().SetSimpRuleRanking("DecreasingRuleComplexity");
        	     JKBCT auxc= this.OrderByInterpretabilityRules(kbctaux, NbLabelsPerInput);
        	     auxc.SetKBCTFile(kbctaux.GetKBCTFile());
                 JKBCT aux1c= this.DeleteNoNeededRules(auxc, jedf, qold);
        	     aux1c.SetKBCTFile(auxc.GetKBCTFile());
                 //nbR= aux1c.GetNbRules();
                 double int1c=this.computeInterpLocalRB(aux1c, NbLabelsPerInput, "DC");
    	         //System.out.println("delete rules(1c)="+nbR);

        	     //System.out.println("int1a="+int1a);
        	     //System.out.println("int1b="+int1b);
        	     //System.out.println("int1c="+int1c);
        	     if ( (int1a <= int1b) && (int1a <= int1c) ) {
        	           kbctaux= aux1a;
        	           contS++;
        	    	   //System.out.println("--> S");
        	     } else if ( (int1b <= int1a) && (int1b <= int1c) ) {
        	           kbctaux= aux1b;
        	           contIC++;
        	    	   //System.out.println("--> IC");
        	     } else if ( (int1c <= int1a) && (int1c <= int1b) ) {
        	           kbctaux= aux1c;
        	           contDC++;
        	    	   //System.out.println("--> DC");
        	     }
        	     MainKBCT.getConfig().SetSimpRuleRanking("ComparisonRuleComplexity");
        	 } else {
         	     if (!MainKBCT.getConfig().GetSimpRuleRanking().equals("false")) {
         	          JKBCT aux1= this.OrderByInterpretabilityRules(kbctaux, NbLabelsPerInput);
        	          aux1.SetKBCTFile(kbctaux.GetKBCTFile());
                      kbctaux= aux1;
         	     }
                 kbctaux= this.DeleteNoNeededRules(kbctaux, jedf, qold);
            }
            NbRnew= kbctaux.GetNbRules();
            MessageKBCT.WriteLogFile("    "+"-> "+LocaleKBCT.GetString("NbTotalRules")+"= "+NbRnew, "Simplify");
            if (NbRold != NbRnew)
                change= true;
          }
          exc= new long[8];
    	  exc[0]= kbctaux.GetPtr();
    	  exc[1]= kbctaux.GetCopyPtr();
          exc[2]= -1;
          exc[3]= -1;
          exc[4]= -1;
          exc[5]= -1;
  	      exc[6]= -1;
	      exc[7]= -1;
          jnikbct.cleanHashtable(ptrid,exc);
  	    //System.out.println("JConsistency: SimplifyRuleBase5 -> htsize="+jnikbct.getHashtableSize());
	    //System.out.println("JConsistency: SimplifyRuleBase5 -> id="+jnikbct.getId());
          if (MainKBCT.getConfig().GetPremiseRemoval()) {
             //if (MainKBCT.getConfig().GetOrderRulesByWeight()) {
               // if (!MainKBCT.getConfig().GetRuleRemoval())
                    //kbctaux= this.OrderByWeightRules(kbctaux, jedf, SelectedRules);

        	    // Order rules by number of premises
       	        //kbctaux= this.OrderByNbPremisesRules(kbctaux, jedf);
             //}
        	 boolean loopend= false;
        	 while (!loopend) {
        	   long TM=Runtime.getRuntime().totalMemory();
        	   long MM=Runtime.getRuntime().maxMemory();
        	   if (TM>=MM) {
                   if (!MainKBCT.getConfig().GetTESTautomatic()) {
        	           String message= LocaleKBCT.GetString("WarningSimplificationHalted")+"\n"+
                       LocaleKBCT.GetString("WarningJVMOutOfMemory")+"\n"+
                       LocaleKBCT.GetString("WarningSimplificationSaveKB")+"\n"+
                       LocaleKBCT.GetString("WarningReleaseMemory");
                       if (!MainKBCT.flagHalt) {
        	    	       MessageKBCT.Information(null, message);
                       }
                   }
                   if (!MainKBCT.flagHalt) {
                       MessageKBCT.WriteLogFile(LocaleKBCT.GetString("WarningSimplificationHalted"), "Simplify");
                       MainKBCT.flagHalt= true;
                   }
                   break;
        	   } else {
        	       //int NbRules= kbctaux.GetNbRules();
         	       MessageKBCT.WriteLogFile("  "+"> Force Premise Removing", "Simplify");
                   boolean warning=false;
               	   simpRankOpt= MainKBCT.getConfig().GetSimpRuleRanking();
         	       if (simpRankOpt.equals("ComparisonRuleComplexity")) {
        	           int[] ruleNbPremisesOLD= this.countPremisesRB(kbctaux);
          	           JKBCT aux1a= this.DeleteNoNeededPremises(kbctaux, jedf, qold);
         	           aux1a.SetKBCTFile(kbctaux.GetKBCTFile());
        	           int[] ruleNbPremisesNEW1a= this.countPremisesRB(kbctaux);
    	               double int1a= this.computeInterpLocalRB(aux1a, NbLabelsPerInput, "S");

         	           MainKBCT.getConfig().SetSimpRuleRanking("IncreasingRuleComplexity");
         	           JKBCT auxb= this.OrderByInterpretabilityRules(kbctaux, NbLabelsPerInput);
         	           auxb.SetKBCTFile(kbctaux.GetKBCTFile());
          	           JKBCT aux1b= this.DeleteNoNeededPremises(auxb, jedf, qold);
         	           aux1b.SetKBCTFile(auxb.GetKBCTFile());
        	           int[] ruleNbPremisesNEW1b= this.countPremisesRB(aux1b);
    	               double int1b= this.computeInterpLocalRB(aux1b, NbLabelsPerInput, "IC");
        	           
         	           MainKBCT.getConfig().SetSimpRuleRanking("DecreasingRuleComplexity");
         	 		   JKBCT auxc= this.OrderByInterpretabilityRules(kbctaux, NbLabelsPerInput);
         	           auxc.SetKBCTFile(kbctaux.GetKBCTFile());
         	           JKBCT aux1c= this.DeleteNoNeededPremises(auxc, jedf, qold);
         	           aux1c.SetKBCTFile(auxc.GetKBCTFile());
        	           int[] ruleNbPremisesNEW1c= this.countPremisesRB(aux1c);
    	               double int1c= this.computeInterpLocalRB(aux1c, NbLabelsPerInput, "DC");

         	           //System.out.println("int1a="+int1a);
         	           //System.out.println("int1b="+int1b);
         	           //System.out.println("int1c="+int1c);
         	           if ( (int1a <= int1b) && (int1a <= int1c) ) {
         	               kbctaux= aux1a;
         	               contS++;
         	 	    	   //System.out.println("--> S");
             	           for (int i=0; i<ruleNbPremisesNEW1a.length; i++) {
                                if (ruleNbPremisesNEW1a[i]!=ruleNbPremisesOLD[i]) {
                           	        warning=true;
                       	            break;
                                }
                           }
         	           } else if ( (int1b <= int1a) && (int1b <= int1c) ) {
         	               kbctaux= aux1b;
         	               contIC++;
         	  	    	   //System.out.println("--> IC");
             	           for (int i=0; i<ruleNbPremisesNEW1b.length; i++) {
                               if (ruleNbPremisesNEW1b[i]!=ruleNbPremisesOLD[i]) {
                          	        warning=true;
                      	            break;
                               }
                           }
         	           } else if ( (int1c <= int1a) && (int1c <= int1b) ) {
         	               kbctaux= aux1c;
         	               contDC++;
         	     	       //System.out.println("--> DC");
             	           for (int i=0; i<ruleNbPremisesNEW1c.length; i++) {
                               if (ruleNbPremisesNEW1c[i]!=ruleNbPremisesOLD[i]) {
                          	        warning=true;
                      	            break;
                               }
                           }
         	           }
         	           MainKBCT.getConfig().SetSimpRuleRanking("ComparisonRuleComplexity");
         	       } else {
             	       if (!MainKBCT.getConfig().GetSimpRuleRanking().equals("false")) {
            	            JKBCT aux1= this.OrderByInterpretabilityRules(kbctaux, NbLabelsPerInput);
           	                aux1.SetKBCTFile(kbctaux.GetKBCTFile());
                            kbctaux= aux1;
            	       }
        	           int[] ruleNbPremisesOLD= this.countPremisesRB(kbctaux);
          	           kbctaux= this.DeleteNoNeededPremises(kbctaux, jedf, qold);
        	           int[] ruleNbPremisesNEW= this.countPremisesRB(kbctaux);
                       for (int i=0; i<ruleNbPremisesNEW.length; i++) {
                   	    //System.out.println("ruleNbPremisesNEW[i]="+ruleNbPremisesNEW[i]+"  ruleNbPremisesOLD[i]"+ruleNbPremisesOLD[i]);
                           if (ruleNbPremisesNEW[i]!=ruleNbPremisesOLD[i]) {
                       	    //System.out.println("warning true");
                     	        warning=true;
                   	            break;
                           }
                       }
         	       }
                   if (warning) {
            	       change=true;
                       // la segunda vez elimina redundancias aparecidas al forzar la eliminaci�n de premisas
            	       NbRold= kbctaux.GetNbRules();
             	       MessageKBCT.WriteLogFile("    "+"-> Simplify RB after Force Premise Removing", "Simplify");
             	       //String rrOpt= MainKBCT.getConfig().GetSimpRuleRanking();
             	       //if (!rrOpt.equals("false") && !rrOpt.equals("RandomIDRuleComplexity") ) {
             	       JKBCT aux2;
                   	   simpRankOpt= MainKBCT.getConfig().GetSimpRuleRanking();
             	       if (simpRankOpt.equals("ComparisonRuleComplexity")) {
             	           double int1a, int1b, int1c;
             	           JKBCT aux1a= this.SimplifyRB(kbctaux);
             	           aux1a.SetKBCTFile(kbctaux.GetKBCTFile());
             	           double[][] qnew1a= this.KBquality(aux1a, null, 0, 0, jedf);
             	           if (!this.GetWorseKBquality(qold, qnew1a)) {
             	                //int nbR= aux1a.GetNbRules();
             	                int1a= this.computeInterpLocalRB(aux1a, NbLabelsPerInput, "S");
             	                //System.out.println("rules(1a)="+nbR);
             	           } else {
             	         	    int1a=-1;
             	           }
             	           MainKBCT.getConfig().SetSimpRuleRanking("IncreasingRuleComplexity");
             	           JKBCT auxb= this.OrderByInterpretabilityRules(kbctaux, NbLabelsPerInput);
             	           auxb.SetKBCTFile(kbctaux.GetKBCTFile());
             	           JKBCT aux1b= this.SimplifyRB(auxb);
             	           aux1b.SetKBCTFile(auxb.GetKBCTFile());
             	           double[][] qnew1b= this.KBquality(aux1b, null, 0, 0, jedf);
             	           if (!this.GetWorseKBquality(qold, qnew1b)) {
             	                //int nbR= aux1b.GetNbRules();
             	                int1b=this.computeInterpLocalRB(aux1b, NbLabelsPerInput, "IC");
             	                //System.out.println("rules(1b)="+nbR);
             	           } else {
             	         	    int1b=-1;
             	           }
             	           MainKBCT.getConfig().SetSimpRuleRanking("DecreasingRuleComplexity");
             	 		   JKBCT auxc= this.OrderByInterpretabilityRules(kbctaux, NbLabelsPerInput);
             	           auxc.SetKBCTFile(kbctaux.GetKBCTFile());
             	           JKBCT aux1c= this.SimplifyRB(auxc);
             	           aux1c.SetKBCTFile(auxc.GetKBCTFile());
             	           double[][] qnew1c= this.KBquality(aux1c, null, 0, 0, jedf);
             	           if (!this.GetWorseKBquality(qold, qnew1c)) {
             	                //int nbR= aux1c.GetNbRules();
             	                int1c=this.computeInterpLocalRB(aux1c, NbLabelsPerInput, "DC");
             	                //System.out.println("rules(1c)="+nbR);
             	           } else {
             	         	    int1c=-1;
             	           }
             	           //System.out.println("int1a="+int1a);
             	           //System.out.println("int1b="+int1b);
             	           //System.out.println("int1c="+int1c);
             	           if ( (int1a >= 0) && (int1a <= int1b) && (int1a <= int1c) ) {
             	               kbctaux= aux1a;
             	               contS++;
             	 	    	   //System.out.println("--> S");
             	           } else if ( (int1b >= 0) && (int1b <= int1a) && (int1b <= int1c) ) {
             	               kbctaux= aux1b;
             	               contIC++;
             	  	    	   //System.out.println("--> IC");
             	           } else if ( (int1c >= 0) && (int1c <= int1a) && (int1c <= int1b) ) {
             	               kbctaux= aux1c;
             	               contDC++;
             	     	       //System.out.println("--> DC");
             	           }
             	           MainKBCT.getConfig().SetSimpRuleRanking("ComparisonRuleComplexity");
             	       } else {
             	           if (!MainKBCT.getConfig().GetSimpRuleRanking().equals("false")) {
             	               JKBCT aux1= this.OrderByInterpretabilityRules(kbctaux, NbLabelsPerInput);
            	               aux1.SetKBCTFile(kbctaux.GetKBCTFile());
                               kbctaux= aux1;
             	           }
                 	       aux2= this.SimplifyRB(kbctaux);
                           //System.out.println("SimplifyRB");
                	       double[][] qnew= this.KBquality(aux2, null, 0, 0, jedf);
                	       if (!this.GetWorseKBquality(qold, qnew))
                	           kbctaux= aux2;
             	       }
                   } else {
            	       loopend=true;
                   }
        	    }
            }
        }
      }
    MessageKBCT.WriteLogFile("  "+"> Rule ranking", "Simplify");
    MessageKBCT.WriteLogFile("    "+"->      contS= "+contS, "Simplify");
    MessageKBCT.WriteLogFile("    "+"->      contIC= "+contIC, "Simplify");
    MessageKBCT.WriteLogFile("    "+"->      contDC= "+contDC, "Simplify");
    //System.out.println("JConsistency: SimplifyRuleBase6 -> htsize="+jnikbct.getHashtableSize());
    //System.out.println("JConsistency: SimplifyRuleBase6 -> id="+jnikbct.getId());
	//long ptr= kbctaux.GetPtr();
    if (!change) { 
        kbctaux.Close();
        kbctaux.Delete();
        //jnikbct.DeleteKBCT(ptr+1);        
        //jnikbct.cleanHashtable(ptr-1);
    }
    if (change) {
        //System.out.println("CHANGE: "+ptr);
        //jnikbct.cleanHashtable(ptr+1);
        return kbctaux;
    } else {
        return null;
    }
  }
//------------------------------------------------------------------------------
  private Vector orderExpertInducedRules (JKBCT kbct1) {
    Vector v= new Vector();
	JKBCT res= new JKBCT(kbct1);
    res.SetKBCTFile(kbct1.GetKBCTFile());
    int NbRules= res.GetNbRules();
    // Separate expert and induced rules
    JKBCT auxkbct_E= new JKBCT(res);
    long ptrE= auxkbct_E.GetPtr();
    auxkbct_E.SetKBCTFile(res.GetKBCTFile());
    JKBCT auxkbct_I= new JKBCT(res);
    long ptrI= auxkbct_I.GetPtr();
    auxkbct_I.SetKBCTFile(res.GetKBCTFile());
    int NbRulesAuxKbct= res.GetNbRules();
    int cont_ER_rem=0;
    int cont_IorSR_rem=0;
    for (int i=0; i<NbRulesAuxKbct; i++) {
         Rule r= res.GetRule(i+1);
         String RuleType= r.GetType();
         if (RuleType.equals("E")) {
             auxkbct_I.RemoveRule(i-cont_ER_rem);
             cont_ER_rem++;
         } else if (RuleType.equals("I") || RuleType.equals("S") || RuleType.equals("P")) {
             auxkbct_E.RemoveRule(i-cont_IorSR_rem);
             cont_IorSR_rem++;
         }
    }
    // Order expert rules
    int NbRulesE= auxkbct_E.GetNbRules();
    Vector v_E= this.ContRelatedRules(auxkbct_E);
    Vector v_Enew= this.OrderKBCT(auxkbct_E, v_E);
    auxkbct_E= (JKBCT)v_Enew.lastElement();
    Object[] ae= v_Enew.toArray();
    v_E=null;
    v_Enew=null;
    // Order induced rules
    int NbRulesI= auxkbct_I.GetNbRules();
    Vector v_I= this.ContRelatedRules(auxkbct_I);
    Vector v_Inew= this.OrderKBCT(auxkbct_I, v_I);
    auxkbct_I= (JKBCT)v_Inew.lastElement();
    Object[] ai= v_Inew.toArray();
    v_I=null;
    v_Inew=null;
    // Join expert and induced rules
    NbRules= res.GetNbRules();
    for (int n=0; n<NbRules; n++)
         res.RemoveRule(0);

    for (int n=0; n<NbRulesE; n++)
         res.AddRule(auxkbct_E.GetRule(n+1));

    for (int n=0; n<NbRulesI; n++)
         res.AddRule(auxkbct_I.GetRule(n+1));

    auxkbct_E.Close();
    auxkbct_E.Delete();
    jnikbct.DeleteKBCT(ptrE+1);
    auxkbct_I.Close();
    auxkbct_I.Delete();
    jnikbct.DeleteKBCT(ptrI+1);
    auxkbct_E= null;
    auxkbct_I= null;
    v.add(res);
    v.add(ae);
    v.add(ai);
	return v;
  }
//------------------------------------------------------------------------------
  private double computeInterpLocalRB(JKBCT rb, int[] NbLabels, String opt) {
     double res=0;
     int nbR= rb.GetNbRules();
     for (int n=0; n<nbR; n++) {
		Rule r= rb.GetRule(n+1);
		double ind=this.computeInterpLocalWeightRule(r, NbLabels);
		if (!opt.equals("DC"))
		    ind=1-ind;
			
        //System.out.println("rule"+String.valueOf(n+1)+"="+ind);
	    res=res+ind;
     }
     return res;
  }
//------------------------------------------------------------------------------
  private int[] countPremisesRB(JKBCT rb) {
	  int NbRules= rb.GetNbRules();
      int[] ruleNbPremises= new int[NbRules];
      for (int i=0; i<NbRules; i++) {
          int[] premises= rb.GetRule(i+1).Get_in_labels_number();
          int cont=0;
          for (int k=0; k<premises.length; k++) {
   	         if (premises[k]!=0)
   		         cont++;
          }
          ruleNbPremises[i]=cont;
      }
      return ruleNbPremises;	  
  }
//------------------------------------------------------------------------------
  private double computeInterpLocalWeightRule(Rule r, int[] NbLabels) {
	  double result=-1;
	  double maxNbLabels=9;
	  int[] premises= r.Get_in_labels_number();
      int lim= premises.length;
      double[] resultV= new double[lim];
      //System.out.println("rule"+r.GetNumber());
      for (int n=0; n<lim; n++) {
           double prem=0;
           if (premises[n]>0) {
               if (premises[n] <= NbLabels[n]) {
            	   prem= prem+1;
               } else if (premises[n] <= 2*NbLabels[n]) {
            	   prem= prem+NbLabels[n]-0.5;///////////
               } else {
            	   int SelLab= premises[n]- 2*NbLabels[n];
            	   int nbORlabs= jnikbct.NbORLabels(SelLab,NbLabels[n]);
            	   prem= prem+nbORlabs;
               }
               //System.out.println(" --> prem="+prem);
           } //else {
    	   //prem=NbLabels[n];        	   
       //}
       //resultV[n]= 1-(prem/NbLabels[n]);
       //resultV[n]= 2-(prem/NbLabels[n]);
       resultV[n]= 1+Math.pow((prem/maxNbLabels),NbLabels[n]);
           //System.out.println(" --> resultV[n]="+resultV[n]);
      }
      for (int n=0; n<lim; n++) {
           if (n==0)
    	       result= resultV[n];
           else
        	   //result= result+resultV[n];
        	   result= result*resultV[n];
      }
      //System.out.println(" --> result="+result);
      int choice=0;
      String rrOpt= MainKBCT.getConfig().GetSimpRuleRanking();
      if (rrOpt.equals("RandomIDRuleComplexity")) {
    	  choice= (int)(2*Math.random());
      }
      if ( (rrOpt.equals("DecreasingRuleComplexity")) ||
    	   (rrOpt.equals("RandomIDRuleComplexity") && (choice==1)) ) {
          result= 1-result; // reverse order (first the more complex rules)
      }
      //System.out.println("choice="+choice);
      return result;
  }
//------------------------------------------------------------------------------
  public JKBCT OrderByInterpretabilityRules(JKBCT kbctsimp, int[] NbLabelsPerInput) {
	    try {
	        JKBCT kbctaux= new JKBCT(kbctsimp);
	        kbctaux.SetKBCTFile(kbctsimp.GetKBCTFile());
            int lim= kbctsimp.GetNbActiveRules();
            double[] ruleIntWeights= new double[lim];
            Rule[] rules= new Rule[lim];
            for (int n=0; n<lim; n++) {
            	 Rule r= kbctsimp.GetRule(n+1);
            	 ruleIntWeights[n]= this.computeInterpLocalWeightRule(r, NbLabelsPerInput);
            	 rules[n]= r;
            }
	    	// ordenar las reglas
	        int[] orderedRules= new int[lim];
	        for (int n=0; n<lim; n++) {
	             orderedRules[n]= n+1;
	        }
	        for (int i=1; i<lim; i++) {
	             for (int n=0; n<i; n++) {
	                	if (ruleIntWeights[orderedRules[i]-1]>ruleIntWeights[orderedRules[n]-1]) {
	                		for (int m=i; m>n; m--) {
	                 			 orderedRules[m]=orderedRules[m-1];
	                  	}
	                  	orderedRules[n]= i+1;
	                    break;
	                  }
	             }
	        }
	        for (int n=0; n<lim; n++) {
	        	 //System.out.println("rule"+n+"="+String.valueOf(orderedRules[n]-1));
	  	         Rule r= rules[orderedRules[n]-1];
	  	         r.SetNumber(n+1);
        	     kbctaux.ReplaceRule(n+1, r);
	  	    }
	        return kbctaux;
     } catch (Throwable t) {
        t.printStackTrace();
        MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);
     }
     return null;
  }
//------------------------------------------------------------------------------
  /**
   * <p align="left">
   * KB ordering (according to the number of fired samples by rule).
   * </p>
   * @param kbct knowledge base to be ordered
   * @param jedf JExtendedDataFile object: used for solving problems
   * @return ordered knowledge base
   */
  /*private JKBCT OrderByWeightRules(JKBCT kbct, JExtendedDataFile jedf) {
    JKBCT auxkbct= new JKBCT(kbct);
    auxkbct.SetKBCTFile(kbct.GetKBCTFile());
    try {
    	File tempauxKB= JKBCTFrame.BuildFile("tempLinksaux.kb");
        File tempauxFIS= JKBCTFrame.BuildFile("tempLinksaux.fis");
    	int NbRules= auxkbct.GetNbRules();
        double[] ruleWeights= new double[NbRules];
        double[] ruleWeightsCC= new double[NbRules];
        double[] ruleWeightsIC= new double[NbRules];
        int[] orderedRules= new int[NbRules];
        JExtendedDataFile jedfauxCC= new JExtendedDataFile(jedf.ActiveFileName(), true);
        JExtendedDataFile jedfauxIC= new JExtendedDataFile(jedf.ActiveFileName(), true);
        int NbActiveData= jedfauxCC.GetActiveCount();
        boolean[] actives= new boolean[NbRules];
        for (int n=0; n<NbRules; n++) {
    	    Rule r= auxkbct.GetRule(n+1);
    	    actives[n]= r.GetActive();
            r.SetActive(false);
        }
        Rule[] rules= new Rule[NbRules];
        jnifis.FisproPath(MainKBCT.getConfig().GetKbctPath());
        for (int i=0; i<NbRules; i++) {
               rules[i]= auxkbct.GetRule(i+1);
               if (actives[i]) {
                  auxkbct.GetRule(i+1).SetActive(true);
                  int[] outputs= rules[i].Get_out_labels_number();
                  auxkbct.SetKBCTFile(tempauxKB.getAbsolutePath());
                  auxkbct.Save();
                  auxkbct.SaveFIS(tempauxFIS.getAbsolutePath());
                  JFIS fis= new JFIS(tempauxFIS.getAbsolutePath());
                  ruleWeightsCC[i]=this.CumulatedWeights(jedfauxCC, outputs, fis, true);
                  ruleWeightsIC[i]=this.CumulatedWeights(jedfauxIC, outputs, fis, false);
               	  ruleWeights[i]= ruleWeightsCC[i] - ruleWeightsIC[i];
                  auxkbct.GetRule(i+1).SetActive(false);
               } else { 
            	   ruleWeights[i]= -NbActiveData;
               } 
          }
            // ordenar las reglas
            for (int i=0; i<ruleWeights.length; i++) {
        	    orderedRules[i]= i+1;
            }
            int lim=ruleWeights.length;
            for (int i=1; i<lim; i++) {
                    for (int n=0; n<i; n++) {
                    	if (ruleWeights[orderedRules[i]-1]>ruleWeights[orderedRules[n]-1]) {
                    		for (int m=i; m>n; m--) {
                    			orderedRules[m]=orderedRules[m-1];
                    		}
                    		orderedRules[n]=i+1;
                            break;
                    	}
                    }
            }
        for (int i=0; i<orderedRules.length; i++) {
        	Rule r= rules[orderedRules[i]-1];
        	r.SetNumber(i+1);
        	r.SetActive(actives[orderedRules[i]-1]);
        	auxkbct.ReplaceRule(i+1, r);
        }
    } catch (Throwable t) {
      //t.printStackTrace();
      MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);
    }
    return auxkbct;
  }*/
//------------------------------------------------------------------------------
  /**
   * <p align="left">
   * KB ordering (according to the number of premises by rule).
   * </p>
   * @param kbct knowledge base to be ordered
   * @return ordered knowledge base
   */
  /*private JKBCT OrderByNbPremisesRules(JKBCT kbct) {
	    JKBCT auxkbct= new JKBCT(kbct);
	    auxkbct.SetKBCTFile(kbct.GetKBCTFile());
        int NbRules= auxkbct.GetNbRules();
	    int[] ruleNbPremises= new int[NbRules];
	    int[] orderedRules= new int[NbRules];
        Rule[] rules= new Rule[NbRules];
        for (int i=0; i<NbRules; i++) {
	         rules[i]= auxkbct.GetRule(i+1);
             int[] premises= rules[i].Get_in_labels_number();
             int cont=0;
             for (int k=0; k<premises.length; k++) {
            	 if (premises[k]!=0)
            		 cont++;
             }
             ruleNbPremises[i]=cont;
	    }
	    // ordenar las reglas
        int lim=ruleNbPremises.length;
	    for (int i=0; i<lim; i++) {
       	    orderedRules[i]= i+1;
        }
        for (int i=1; i<lim; i++) {
             for (int k=0; k<i; k++) {
	              if (ruleNbPremises[orderedRules[i]-1]<ruleNbPremises[orderedRules[k]-1]) {
	                  for (int m=i; m>k; m--) {
	                      orderedRules[m]=orderedRules[m-1];
	                  }
	                  orderedRules[k]=i+1;
	                  break;
	              }
	         }
         }
         for (int i=0; i<orderedRules.length; i++) {
	          Rule r= rules[orderedRules[i]-1];
	          r.SetNumber(i+1);
	          auxkbct.ReplaceRule(i+1, r);
	     }
      	 // ordenar por pesos las reglas con el mismo numero de premisas
         return auxkbct;
  }*/
//------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Compute comulated weights by rule (regarding the number of managed examples).
   * </p>
   */
  private double CumulatedWeights(JExtendedDataFile jedfaux, int[] outputs, JFIS fis, boolean CC) {
    double ruleWeight= 0;
    try {
      // Desactivar los datos cuya conclusion no coincide con la de la regla
      boolean[] dataActive= new boolean[jedfaux.DataLength()];
      int contInactiveData=0;
      boolean AllInactive=false;
      for (int k=0; k<dataActive.length; k++) {
           dataActive[k]= jedfaux.GetActive(k);
           boolean incorrectConclusion= false;
           for (int m=0; m<outputs.length; m++) {
                int var= jedfaux.VariableCount()-outputs.length+m;
                double output= jedfaux.VariableData(var)[k];
                // Nature -> 0 (fuzzy)
                // Nature -> 1 (crisp)
                JOutput jout= fis.GetOutput(m);
                if (jout.GetNature()==JOutput.FUZZY) {
                	int NbMF= jout.GetNbMF();
                    int SelLab=NbMF;
                	for (int i=0; i<NbMF-1; i++) {
                	   double[] par= jout.GetMF(i).GetParams();
                       double lim=0;
                	   if (par.length==3) {
                		   lim=(par[1]+par[2])/2;
                	   } else if (par.length==4) {
                		   lim=(par[2]+par[3])/2;
                	   }
                	   if (output < lim) {
                		   SelLab=i+1;
                		   break;
                	   }
                	}
            	   if (outputs[m] != SelLab)
                       incorrectConclusion= true;
                }
                else if (outputs[m] != output) {
                    incorrectConclusion= true;
                }
           }
           if ( ( (CC) && (incorrectConclusion) ) ||
        		( (!CC) && (!incorrectConclusion) ) )  {
               contInactiveData++;
               if (contInactiveData==dataActive.length)
                   AllInactive=true;
               else
               	   jedfaux.SetActive(k+1, false);
           } 
       }
       if ( (!AllInactive) && (contInactiveData>0) ) {
    	   System.out.print("");
    	   //System.out.println("contInactiveData="+contInactiveData);
    	   //System.out.println("jedfaux.ActiveFileName()="+jedfaux.ActiveFileName());
           fis.Links(jedfaux, 1e-6, false, jedfaux.ActiveFileName(), null);
           ruleWeight= this.readRulesItemsFile(jedfaux.ActiveFileName()+"."+jnifis.LinksRulesItemsExtension());
       } 
       for (int k=0; k<dataActive.length; k++) {
     	  jedfaux.SetActive(k+1, dataActive[k]);
       }
    } catch (Throwable t) {
      t.printStackTrace();
      MessageKBCT.Error(LocaleKBCT.GetString("Error"), t);
    }
    return ruleWeight;
  }
//------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Read info from Fispro Links files.
   * </p>
   */
  double readRulesItemsFile(String file) {
    double result=0;
    try {
      LineNumberReader lnr= new LineNumberReader(new InputStreamReader(new FileInputStream(file)));
      lnr.readLine(); // numero de reglas
      lnr.readLine(); // numero maximo de datos manejados por una regla
      String l;
      while ((l=lnr.readLine())!=null) {
       	    int pos= l.indexOf(",");
      	    String aux= l.substring(pos+1);
            pos= aux.indexOf(",");
            result= result + new Double(aux.substring(0,pos)).doubleValue();
      }
    } catch( Throwable except ) { 
        except.printStackTrace();
    	MessageKBCT.Error( null, except );
    }
    return result;
  }
//------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Force rule removing.
   * </p>
   */
  private JKBCT DeleteNoNeededRules(JKBCT kbctaux, JExtendedDataFile jedf, double[][] qold) {
    JKBCT auxkbct= new JKBCT(kbctaux);
    auxkbct.SetKBCTFile(kbctaux.GetKBCTFile());
    int outSel=0;
    boolean warning= false;
    if (MainKBCT.getConfig().GetOnlyRBsimplification()) { 
    	String outs=MainKBCT.getConfig().GetOutputClassSelected();
    	if (!outs.equals(LocaleKBCT.GetString("all"))) {
        	warning=true;
    	    outSel= (new Integer(outs)).intValue(); 
    	}
    }
    int NbTotalRules= auxkbct.GetNbRules();
    for (int n= NbTotalRules; n>0; n--) {
      Rule r = auxkbct.GetRule(n);
      if (r.GetActive()) {
        boolean warn=false;
        if (warning) {
          if (r.Get_out_labels_number()[0]!=outSel) 
         	 warn=true;
        }
        if ( !warn && (r.GetActive()) && (!r.GetType().equals("E")) ) {
 	       long TM=Runtime.getRuntime().totalMemory();
	       long MM=Runtime.getRuntime().maxMemory();
	       if (TM>=MM) {
               if (!MainKBCT.getConfig().GetTESTautomatic()) {
	    	     String message= LocaleKBCT.GetString("WarningSimplificationHalted")+"\n"+
                 LocaleKBCT.GetString("WarningJVMOutOfMemory")+"\n"+
                 LocaleKBCT.GetString("WarningSimplificationSaveKB")+"\n"+
                 LocaleKBCT.GetString("WarningReleaseMemory");
                 if (!MainKBCT.flagHalt) {
	    	         MessageKBCT.Information(null, message);
                 }
               }
               if (!MainKBCT.flagHalt) {
                   MessageKBCT.WriteLogFile(LocaleKBCT.GetString("WarningSimplificationHalted"), "Simplify");
                   MainKBCT.flagHalt= true;
               }
               break;
	       } else {
               r.SetActive(false);
               double[][] qnew = this.KBquality(auxkbct, null, 0, 0, jedf);

               r.SetActive(true);
               if ( (this.isThereMoreThanOneRulePerOutputClass(auxkbct, r.Get_out_labels_number()[0])) && (!this.GetWorseKBquality(qold, qnew)) ) {
                   MessageKBCT.WriteLogFile("    "+"    "+"-> "+LocaleKBCT.GetString("Remove")+": "+r.GetNumber(), "Simplify");
                   auxkbct.RemoveRule(r.GetNumber()-1);
               }
            }
         }
       }
    }
    return auxkbct;
  }
//------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Force premise removing.
   * </p>
   */
  private JKBCT DeleteNoNeededPremises(JKBCT kbctaux, JExtendedDataFile jedf, double[][] qold) {
    JKBCT auxkbct= new JKBCT(kbctaux);
    auxkbct.SetKBCTFile(kbctaux.GetKBCTFile());
    int outSel=0;
    boolean warning= false;
    if (MainKBCT.getConfig().GetOnlyRBsimplification()) { 
    	String outs=MainKBCT.getConfig().GetOutputClassSelected();
    	if (!outs.equals(LocaleKBCT.GetString("all"))) {
        	warning=true;
    	    outSel= (new Integer(outs)).intValue(); 
    	}
    }
    int NbTotalRules= auxkbct.GetNbRules();
    int NbConfOLD= this.ContLinguisticConflicts(auxkbct);
    // ordenar las variables por numero de apariciones
    int[] premByVar= new int[auxkbct.GetNbInputs()];
    for (int n=0; n<premByVar.length; n++) {
    	premByVar[n]=0;
    }    
    for (int n=1; n<=NbTotalRules; n++) {
        Rule r = auxkbct.GetRule(n);
        if (r.GetActive()) {
          int[] aux= r.Get_in_labels_number();
          for (int k=0; k<aux.length; k++) {
               if (aux[k]!=0)
            	   premByVar[k]++;
          }
        }
    }    
    int[] orderedVar= new int[auxkbct.GetNbInputs()];
    for (int i=0; i<orderedVar.length; i++) {
   	    orderedVar[i]= i+1;
    }
    for (int i=1; i<orderedVar.length; i++) {
         for (int k=0; k<i; k++) {
              if (premByVar[orderedVar[i]-1]<premByVar[orderedVar[k]-1]) {
                  for (int m=i; m>k; m--) {
                      orderedVar[m]=orderedVar[m-1];
                  }
                  orderedVar[k]=i+1;
                  break;
              }
         }
     }
    for (int n=1; n<=NbTotalRules; n++) {
        Rule r = auxkbct.GetRule(n);
        boolean warn=false;
        if (warning) {
            if (r.Get_out_labels_number()[0]!=outSel) 
           	    warn=true;
        }
        if ( (!warn) && (r.GetActive()) && (!r.GetType().equals("E")) ) {
   	       long TM=Runtime.getRuntime().totalMemory();
	       long MM=Runtime.getRuntime().maxMemory();
	       if (TM>=MM) {
               if (!MainKBCT.getConfig().GetTESTautomatic()) {
	    	     String message= LocaleKBCT.GetString("WarningSimplificationHalted")+"\n"+
                 LocaleKBCT.GetString("WarningJVMOutOfMemory")+"\n"+
                 LocaleKBCT.GetString("WarningSimplificationSaveKB")+"\n"+
                 LocaleKBCT.GetString("WarningReleaseMemory");
                 if (!MainKBCT.flagHalt) {
	    	         MessageKBCT.Information(null, message);
                 }
               }
               if (!MainKBCT.flagHalt) {
                   MessageKBCT.WriteLogFile(LocaleKBCT.GetString("WarningSimplificationHalted"), "Simplify");
                   MainKBCT.flagHalt= true;
               }
               break;
	       } else {
               int[] premises= new int[auxkbct.GetNbInputs()];
               int[] aux= r.Get_in_labels_number();
               for (int k=0; k<premises.length; k++) {
                    premises[k]= aux[k];
               }
               for (int i=0; i<premises.length; i++) {
        	     if (premises[orderedVar[i]-1]!=0) {
             	    r.SetInputLabel(orderedVar[i], 0);
                    auxkbct.ReplaceRule(r.GetNumber(), r);
                    double[][] qnew = this.KBquality(auxkbct, null, 0, 0, jedf);
                    int NbConfNEW= this.ContLinguisticConflicts(auxkbct);
                    if ( (NbConfNEW > NbConfOLD) || this.GetWorseKBquality(qold, qnew) ) {
                      r.SetInputLabel(orderedVar[i], premises[orderedVar[i]-1]);
                      auxkbct.ReplaceRule(r.GetNumber(), r);
                      r = auxkbct.GetRule(r.GetNumber());
                      return auxkbct;
                    } else {
                    	//System.out.println("remove premise i="+orderedVar[i]);
              	        MessageKBCT.WriteLogFile("    "+"    "+"-> "+LocaleKBCT.GetString("Rule")+" "+r.GetNumber()+", "+LocaleKBCT.GetString("Input")+" "+orderedVar[i], "Simplify");
                    }
                 }
        	   }
           }
        }
      }
    return auxkbct;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Return a vector with knowledge bases of related rules by same conclusion.
   * </p>
   * @param auxkbct knowledge base
   * @return Vector with JKBCT objects (contain blocks of rules with the same conclusion)
   */
  private Vector ContRelatedRules( JKBCT auxkbct ) {
    int NbRules= auxkbct.GetNbRules();
    Vector<JKBCT> v= new Vector<JKBCT>();
    while (NbRules > 0)  {
      JKBCT aux= new JKBCT(auxkbct);
      aux.SetKBCTFile(auxkbct.GetKBCTFile());
      Rule r= auxkbct.GetRule(1);
      auxkbct.RemoveRule(0);
      NbRules--;
      int lim= aux.GetNbRules();
      int cont= 1;
      for ( int m= 1; m<lim; m++) {
        Rule raux= aux.GetRule(m+1);
        if (JConsistency.DifferentConclusions(r, raux)) {
          aux.RemoveRule(m);
          lim--;
          m--;
          cont++;
        } else {
          auxkbct.RemoveRule(cont-1);
          NbRules--;
        }
      }
      v.add(aux);
    }
    return v;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Order knowledge base "kbct" according to knowledge bases in vector "v".
   * </p>
   * @param kbct knowledge base
   * @param v Vector of JKBCT objects (contain blocks of rules with the same conclusion)
   * @return Vector with: number of rules in each block, and kbct with the rules in order.
   */
  private Vector OrderKBCT( JKBCT kbct, Vector v ) {
    Vector vresult= new Vector();
    int NbRules= kbct.GetNbRules();
    for (int n=0; n<NbRules; n++)
      kbct.RemoveRule(0);

    Enumeration ev= v.elements();
    while (ev.hasMoreElements()) {
      JKBCT aux= (JKBCT)ev.nextElement();
      vresult.add(new Integer(aux.GetNbRules()));
      for (int n=0; n<aux.GetNbRules(); n++)
        kbct.AddRule(aux.GetRule(n+1));
      
      long ptr= aux.GetPtr();
      aux.Close();
      aux.Delete();
      jnikbct.DeleteKBCT(ptr+1);
    }
    vresult.add(kbct);
    return vresult;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Simplify Rule Base contains in "kbct".
   * Remove incomplete and redundant rules.
   * </p>
   * @param kbct initial knowledge base
   * @return simplified knowledge base (removed redundant rules)
   */
  private JKBCT SimplifyRB( JKBCT kbct) {
    JKBCT kbctaux= new JKBCT(kbct);
    kbctaux.SetKBCTFile(kbct.GetKBCTFile());
    int outSel=0;
    boolean warning= false;
    if (MainKBCT.getConfig().GetOnlyRBsimplification()) { 
    	String outs=MainKBCT.getConfig().GetOutputClassSelected();
    	if (!outs.equals(LocaleKBCT.GetString("all"))) {
        	warning=true;
    	    outSel= (new Integer(outs)).intValue(); 
    	}
    }
    int NbRules= kbctaux.GetNbRules();
    for (int n=0; n<NbRules; n++) {
         //System.out.println("SimplifyRB: n="+n);
	     long TM=Runtime.getRuntime().totalMemory();
	     long MM=Runtime.getRuntime().maxMemory();
	     if (TM>=MM) {
               if (!MainKBCT.getConfig().GetTESTautomatic()) {
	    	     String message= LocaleKBCT.GetString("WarningSimplificationHalted")+"\n"+
                 LocaleKBCT.GetString("WarningJVMOutOfMemory")+"\n"+
                 LocaleKBCT.GetString("WarningSimplificationSaveKB")+"\n"+
                 LocaleKBCT.GetString("WarningReleaseMemory");
                 if (!MainKBCT.flagHalt) {
	    	         MessageKBCT.Information(null, message);
                 }
               }
               if (!MainKBCT.flagHalt) {
                   MessageKBCT.WriteLogFile(LocaleKBCT.GetString("WarningSimplificationHalted"), "Simplify");
                   MainKBCT.flagHalt= true;
               }
               break;
	     } else {
             Rule r1= kbctaux.GetRule(n+1);
             boolean warn=false;
             if (warning) {
            	 int o=r1.Get_out_labels_number()[0];
                 if ( (o!=outSel) && (o!=0) ) 
                	 warn=true;
             }
             if (!warn && r1.GetActive()) {
            	 /*if ( (r1.GetNumber()==1) || (r1.GetNumber()==2) ) {
            		 int[] ins= r1.Get_in_labels_number();
            		 System.out.println("SimplifyRB");
            		 for (int i=0; i<ins.length; i++) {
            			  System.out.print(ins[i]+" ");
            		 }
            		 System.out.println();
            	 }*/
                 if (this.IncompleteRule(r1.GetNumber(), kbctaux)) {
                     // Borrar regla n+1;
                     MessageKBCT.WriteLogFile("    "+"    "+"-> "+LocaleKBCT.GetString("Remove")+": "+r1.GetNumber(), "Simplify");
                     kbctaux.RemoveRule(r1.GetNumber()-1);
                     n--;
                     if (n<0)
                         n=0;

                     NbRules--;
                 } else {
                     for (int m=n+1; m < NbRules; m++) {
                          Rule r2= kbctaux.GetRule(m+1);
                          if (r2.GetActive()) {
                              if (this.IncompleteRule(r2.GetNumber(), kbctaux)) {
                                  // Borrar regla m+1
                                  MessageKBCT.WriteLogFile("    "+"    "+"-> "+LocaleKBCT.GetString("Remove")+": "+r2.GetNumber(), "Simplify");
                                  kbctaux.RemoveRule(r2.GetNumber()-1);
                                  m--;
                                  if (m<n+1)
                                      m=n+1;

                                  NbRules--;
                              } else if (!DifferentConclusions(r1, r2)) {
                                  if (SamePremise(r1, r2)) {
                                     if ( (r2.GetType().equals("E")) && !(r1.GetType().equals("E")) ) {
                                         MessageKBCT.WriteLogFile("    "+"    "+"-> "+LocaleKBCT.GetString("Remove")+": "+r1.GetNumber(), "Simplify");
                                         kbctaux.RemoveRule(r1.GetNumber()-1);
                                         n--;
                                     if (n<0)
                                         n=0;

                                     NbRules--;
                              } else {
                                  MessageKBCT.WriteLogFile("    "+"    "+"-> "+LocaleKBCT.GetString("Remove")+": "+r2.GetNumber(), "Simplify");
                                  kbctaux.RemoveRule(r2.GetNumber()-1);
                                  m--;
                                  if (m<n+1)
                                      m=n+1;

                                  NbRules--;
                              }
                          } else {
                            boolean c1= this.Covered(r2, r1);
                            boolean c2= this.Covered(r1, r2);
                            if (!(c1 && c2)) {
                                if (c1) {
                                   if ( (r2.GetType().equals("E")) || !(r1.GetType().equals("E")) ) {
                                      MessageKBCT.WriteLogFile("    "+"    "+"-> "+LocaleKBCT.GetString("Remove")+": "+r1.GetNumber(), "Simplify");
                                      kbctaux.RemoveRule(r1.GetNumber()-1);
                                      n--;
                                      if (n<0)
                                          n=0;
 
                                      NbRules--;
                                   }
                               } else if (c2) {
                                   if ( (r1.GetType().equals("E")) || !(r2.GetType().equals("E")) ) {
                                       MessageKBCT.WriteLogFile("    "+"    "+"-> "+LocaleKBCT.GetString("Remove")+": "+r2.GetNumber(), "Simplify");
                                       kbctaux.RemoveRule(r2.GetNumber()-1);
                                       m--;
                                       if (m<n+1)
                                           m=n+1;

                                       NbRules--;
                               }
                            }
                         }
                     }
                 }
               }
            }
          }
        }
      }
    }
    return kbctaux;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Merge Rule Base contains in "kbct" according to information in "ae" and "ai".
   * ae and ai contains numbers of consecutive rules with same conclusion.
   * <ul>
   *   <li> ae= array of expert rules  </li>
   *   <li> ai= array of induced rules </li>
   * </ul>
   * </p>
   * @param kbct initial knowledge base
   * @param ae numbers of expert rules with same conclusion in each block
   * @param ai numbers of induced rules with same conclusion in each block
   * @param jcf JConsistencyFrame object
   * @return simplified knowledge base (merged rules)
   */
  private JKBCT MergeRuleBase( JKBCT kbct, Object[] ae, Object[] ai, JExtendedDataFile jedf, double[][] qold ) {
	    //long ptr= jnikbct.getId();
	    //System.out.println("JConsistency: MergeRuleBase1 -> htsize="+jnikbct.getHashtableSize());
	    //System.out.println("JConsistency: MergeRuleBase1 -> id="+jnikbct.getId());
    JKBCT auxkbct= new JKBCT(kbct);
    long ptraux= auxkbct.GetPtr();
    //System.out.println("JConsistency: MergeRuleBase1 -> ptraux="+ptraux);
    auxkbct.SetKBCTFile(kbct.GetKBCTFile());
    int ini=0;
    for (int i=0; i<2; i++) {
      Object[] a = null;
      if (i == 0) {
        a = ae;
        ini = 0;
      }
      else
        a = ai;

      for (int k = 0; k < a.length - 1; k++) {
        int NbTotalRules= auxkbct.GetNbRules();
        int NbRulesSameConc= ((Integer)a[k]).intValue();
        int NbRules= ini + NbRulesSameConc;
        if (NbRulesSameConc > 1) {
          int NbOldRules = auxkbct.GetNbRules();
          int L0 = ini;
          int L1 = NbRulesSameConc;
          int remRules = NbOldRules - L0 - L1;
          boolean end = false;
          while (!end) {
     	     long TM=Runtime.getRuntime().totalMemory();
    	     long MM=Runtime.getRuntime().maxMemory();
    	     if (TM>=MM) {
                   if (!MainKBCT.getConfig().GetTESTautomatic()) {
    	    	     String message= LocaleKBCT.GetString("WarningSimplificationHalted")+"\n"+
                     LocaleKBCT.GetString("WarningJVMOutOfMemory")+"\n"+
                     LocaleKBCT.GetString("WarningSimplificationSaveKB")+"\n"+
                     LocaleKBCT.GetString("WarningReleaseMemory");
                     if (!MainKBCT.flagHalt) {
    	    	         MessageKBCT.Information(null, message);
                     }
                   }
                   if (!MainKBCT.flagHalt) {
                       MessageKBCT.WriteLogFile(LocaleKBCT.GetString("WarningSimplificationHalted"), "Simplify");
                       MainKBCT.flagHalt= true;
                   }
                   break;
    	     } else {
                int[] SelRules = new int[L1];
                for (int n = 0; n < SelRules.length; n++) {
                  SelRules[n] = L0 + n;
                }
        	    //System.out.println("JConsistency: MergeRuleBase2 -> htsize="+jnikbct.getHashtableSize());
        	    //System.out.println("JConsistency: MergeRuleBase2 -> id="+jnikbct.getId());
        	    //System.out.println("JConsistency: MergeRuleBase2 -> ptraux="+ptraux);
        	    auxkbct = this.MergeRules(auxkbct, SelRules, jedf, qold, null);
        	    long[] exc= new long[8];
        		exc[0]= auxkbct.GetPtr();
        		exc[1]= auxkbct.GetCopyPtr();
        	    exc[2]= -1;
        	    exc[3]= -1;
        	    exc[4]= -1;
        	    exc[5]= -1;
        	    exc[6]= -1;
        	    exc[7]= -1;
                jnikbct.cleanHashtable(ptraux-1,exc);
        	    //System.out.println("JConsistency: MergeRuleBase3 -> htsize="+jnikbct.getHashtableSize());
        	    //System.out.println("JConsistency: MergeRuleBase3 -> id="+jnikbct.getId());
                int NbNewRules = auxkbct.GetNbRules();
                if (NbNewRules == NbOldRules)
                   end = true;
                else {
                   NbOldRules = NbNewRules;
                   L1 = NbNewRules - L0 - remRules;
                   if (L1 == 1)
                       end = true;
                   else
                	   return auxkbct;
                }
             }
          }
        }
        int NewNbTotalRules= auxkbct.GetNbRules();
        ini= NbRules - (NbTotalRules - NewNbTotalRules);
      }
      if (i==0)
        ae= null;
      else
        ai= null;
    }
    //System.out.println("JConsistency: MergeRuleBase4 -> htsize="+jnikbct.getHashtableSize());
    //System.out.println("JConsistency: MergeRuleBase4 -> id="+jnikbct.getId());
    return auxkbct;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Check if rules "r1" and "r2" can be merged.
   * </p>
   * @param r1 first rule
   * @param r2 second rule
   * @return true ("r1" and "r2" can be merged) / false
   */
  private boolean CanBeMerged( Rule r1, Rule r2 ) {
    if (!r1.GetType().equals(r2.GetType())) {
      return false;
    }
    int NbLim= NbLim= r1.GetNbInputs();
    int[] r1_labels= r1_labels= r1.Get_in_labels_number();
    int[] r2_labels= r2_labels= r2.Get_in_labels_number();
    // increasing order
    for (int n=0; n<NbLim; n++) {
      if (r1_labels[n]!=r2_labels[n]) {
         if ( this.NumberMergedLabel(r1_labels[n], r2_labels[n], ((int[])ht_UNUSUED_LABELS.get("" + String.valueOf(n))).length)==-1 ) {
            return false;
         }
      }
    }
    // decreasing order
    /*for (int n=NbLim-1; n>=0; n--) {
      if (r1_labels[n]!=r2_labels[n]) {
         if ( this.NumberMergedLabel(r1_labels[n], r2_labels[n], ((int[])ht_UNUSUED_LABELS.get("" + String.valueOf(n))).length)==-1 )
            return false;
      }
    }*/
    return true;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Merge rules "r1" and "r2".
   * </p>
   * @param r1 first rule
   * @param r2 second rule
   * @return merged rule
   */
  private Rule Merge( Rule r1, Rule r2 ) {
    int NbInputs= r1.GetNbInputs();
    int NbOutputs= r1.GetNbOutputs();
    int[] r1_in_labels= r1.Get_in_labels_number();
    int[] r2_in_labels= r2.Get_in_labels_number();
    int[] r1_out_labels= r1.Get_out_labels_number();
    int[] r_in_labels= new int[NbInputs];
    int[] r_out_labels= new int[NbOutputs];
    for (int n=0; n<NbInputs; n++) {
        if (r1_in_labels[n]==r2_in_labels[n])
          r_in_labels[n]= r1_in_labels[n];
        else
          r_in_labels[n] = this.NumberMergedLabel(r1_in_labels[n], r2_in_labels[n], ((int[])ht_UNUSUED_LABELS.get("" + n)).length);
    }
    for (int n=0; n<NbOutputs; n++)
        r_out_labels[n]= r1_out_labels[n];

    int min= Math.min(r1.GetNumber(),r2.GetNumber());
    Rule r= new Rule(min, NbInputs, NbOutputs, r_in_labels, r_out_labels, r1.GetType(), r1.GetActive());
    return r;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Merge Rule Base contains in "kbct" according to information in "selRules".
   * @param kbct initial knowledge base
   * @param selRules set of rules to merge (same type, same conclusion, all active rules)
   * @param jcf JConsistencyFrame object
   * @return simplified knowledge base (merged rules)
   */
  public JKBCT MergeRules ( JKBCT kbct, int[] SelectedRules, JExtendedDataFile jedf, double[][] qold, Rule rmod ) {
    this.init_ht_UNUSUED_LABELS(kbct.GetNbInputs()+kbct.GetNbOutputs(), kbct.GetNbInputs());
    JKBCT auxkbct= new JKBCT(kbct);
    auxkbct.SetKBCTFile(kbct.GetKBCTFile());
    //long ptr= kbct.GetPtr();
    //kbct.Close();
    //kbct.Delete();
    //jnikbct.DeleteKBCT(ptr+1);
    int outSel=0;
    boolean warning= false;
    if (MainKBCT.getConfig().GetOnlyRBsimplification()) { 
    	String outs=MainKBCT.getConfig().GetOutputClassSelected();
    	if (!outs.equals(LocaleKBCT.GetString("all"))) {
        	warning=true;
    	    outSel= (new Integer(outs)).intValue(); 
    	}
    }
    boolean flag= false;
    if (qold==null)
      flag= true;

    boolean end= false;
    int NbSelRules= SelectedRules.length;
    for (int i=0; i<NbSelRules-1; i++) {
      if (!end) {
        Rule r1= auxkbct.GetRule(SelectedRules[i]+1);
        boolean warn=false;
        if (warning) {
            if (r1.Get_out_labels_number()[0]!=outSel) 
           	    warn=true;
        }
        if (!warn && r1.GetActive()) {
    	    long TM=Runtime.getRuntime().totalMemory();
    	    long MM=Runtime.getRuntime().maxMemory();
    	    if (TM>=MM) {
                   if (!MainKBCT.getConfig().GetTESTautomatic()) {
    	    	     String message= LocaleKBCT.GetString("WarningSimplificationHalted")+"\n"+
                     LocaleKBCT.GetString("WarningJVMOutOfMemory")+"\n"+
                     LocaleKBCT.GetString("WarningSimplificationSaveKB")+"\n"+
                     LocaleKBCT.GetString("WarningReleaseMemory");
                     if (!MainKBCT.flagHalt) {
    	    	         MessageKBCT.Information(null, message);
                     }
                   }
                   if (!MainKBCT.flagHalt) {
                       MessageKBCT.WriteLogFile(LocaleKBCT.GetString("WarningSimplificationHalted"), "Simplify");
                       MainKBCT.flagHalt= true;
                   }
                   break;
    	    } else {
                for (int m=i+1; m < NbSelRules; m++) {
                   Rule r2= auxkbct.GetRule(SelectedRules[m]+1);
            	
                   if ( (r2.GetActive()) && (r2.GetNumber() != r1.GetNumber()) ) {
       	              TM=Runtime.getRuntime().totalMemory();
    	              MM=Runtime.getRuntime().maxMemory();
    	              if (TM>=MM) {
                         if (!MainKBCT.getConfig().GetTESTautomatic()) {
    	    	           String message= LocaleKBCT.GetString("WarningSimplificationHalted")+"\n"+
                           LocaleKBCT.GetString("WarningJVMOutOfMemory")+"\n"+
                           LocaleKBCT.GetString("WarningSimplificationSaveKB")+"\n"+
                           LocaleKBCT.GetString("WarningReleaseMemory");
    	                   if (!MainKBCT.flagHalt) {
    	    	               MessageKBCT.Information(null, message);
    	                   }
                         }
                         if (!MainKBCT.flagHalt) {
                             MessageKBCT.WriteLogFile(LocaleKBCT.GetString("WarningSimplificationHalted"), "Simplify");
                             MainKBCT.flagHalt= true;
                         }
                         break;
    	              } else {
    	            	  Rule r= null;
                          if ( this.CanBeMerged(r1, r2) )
                              r= this.Merge(r1, r2);

                          if (r != null) {
            	             if ( (rmod==null) || !(JConsistency.SamePremise(r,rmod)) ) {
                                if (!flag) {
                                   boolean fusion= true;
                                   fusion= ! this.IsThereAnyRedundancyOrSpecificityWarning(auxkbct, r, r1.GetNumber(), r2.GetNumber());
                                   if (fusion) {
                                      double[][] qnew = this.KBquality(auxkbct, r, r1.GetNumber(), r2.GetNumber(), jedf);
                                      if (qnew==null)
                                         end= true;

                                      if (!this.GetWorseKBquality(qold, qnew)) {
                                         int min= Math.min(r1.GetNumber(), r2.GetNumber());
                                         int max= Math.max(r1.GetNumber(), r2.GetNumber());
                    	             	 /*if ( (r1.GetNumber()==1) && (r2.GetNumber()==2) ) {
                    	            		 int[] ins1= r1.Get_in_labels_number();
                    	            		 int[] ins2= r2.Get_in_labels_number();
                	            			 System.out.println("Merge rules");
                    	            		 for (int k=0; k<ins1.length; k++) {
                    	            			  System.out.print(ins1[k]+" ");
                    	            		 }
                	            			 System.out.println();
                    	            		 for (int k=0; k<ins2.length; k++) {
                   	            			      System.out.print(ins2[k]+" ");
                   	            		     }
                	            			 System.out.println();
                    	            	 }*/
                                     	 MessageKBCT.WriteLogFile("  "+"  "+"-> "+LocaleKBCT.GetString("Group")+": "+r1.GetNumber()+" "+LocaleKBCT.GetString("AND")+" "+r2.GetNumber(), "Simplify");
                  	                     auxkbct.ReplaceRule(min, r);
                                         auxkbct.RemoveRule(max-1);
                                         NbSelRules--;
                                         r1 = auxkbct.GetRule(r.GetNumber());
                                         m=0;
                                         return auxkbct;
                                      } 
                                    } 
                      } else {
                        boolean fusion= true;
                        fusion= ! this.IsThereAnyRedundancyOrSpecificityWarning(auxkbct, r, r1.GetNumber(), r2.GetNumber());
                        if (fusion) {
                          int min= Math.min(r1.GetNumber(), r2.GetNumber());
                          int max= Math.max(r1.GetNumber(), r2.GetNumber());
                      	  MessageKBCT.WriteLogFile("  "+"  "+"-> "+LocaleKBCT.GetString("Group")+": "+r1.GetNumber()+" "+LocaleKBCT.GetString("AND")+" "+r2.GetNumber(), "Simplify");
                	      auxkbct.ReplaceRule(min, r);
                          auxkbct.RemoveRule(max-1);
                          NbSelRules--;
                          r1 = auxkbct.GetRule(r.GetNumber());
                          m=0;
                          return auxkbct;
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    kbct.Close();
    kbct.Delete();
    return auxkbct;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Conflicts Solving (through expanding conflict rules).
   * @param kbctexp expanded knowledge base
   * @param kbct initial knowledge base
   * @param SelectedRules set of conflict rules (rule1, rule2)
   * @return new knowledge base
   */
  public JKBCT solveConflict ( JKBCT kbctexp, JKBCT kbct, int[] SelectedRules, File KBfileLinks, File FISfileLinks, JExtendedDataFile jedf, boolean expand, boolean commonPart, boolean split) {
    //System.out.println("Jconsitency: solveConflict: expand="+expand+"  commonPart="+commonPart+"  split="+split);
	Vector<Integer> expRules= new Vector<Integer>();
    expRules.add(new Integer(kbct.GetNbRules()));
    int NbExpR1=0;
    int NbExpR2=0;
    // expandir las reglas que producen el conflicto a su forma can�nica
    if (expand) {
        //System.out.println("Jconsitency: NbR="+kbct.GetNbRules());
        Rule[] rexp1= this.expandConflictRules(kbctexp, SelectedRules[0]+1, SelectedRules[1]+1, commonPart);
        NbExpR1= rexp1.length;
        expRules.add(new Integer(rexp1.length));
        for (int m=0; m<rexp1.length; m++) {
          kbct.AddRule(rexp1[m]);
        }
        //System.out.println("Jconsitency: NbR (exp1)="+kbct.GetNbRules());
        Rule[] rexp2= this.expandConflictRules(kbctexp, SelectedRules[1]+1, SelectedRules[0]+1, commonPart);
        NbExpR2= rexp2.length;
        expRules.add(new Integer(rexp2.length));
        for (int m=0; m<rexp2.length; m++) {
          kbct.AddRule(rexp2[m]);
        }
        //System.out.println("Jconsitency: NbR (exp2)="+kbct.GetNbRules());
    }
    boolean r1MoreSpecific= false;
    if (NbExpR1 < NbExpR2)
    	r1MoreSpecific= true;
    
    // eliminar las reglas originales
    Rule[] rr= new Rule[SelectedRules.length];
    boolean order= false;
    if (expand) {
      int[] rules= new int[2];
      if (SelectedRules[0]>SelectedRules[1]) {
    	  rules[0]=SelectedRules[0];
    	  rules[1]=SelectedRules[1];
      } else {
    	  rules[0]=SelectedRules[1];
    	  rules[1]=SelectedRules[0];
    	  order=true;
      }
      for (int n=0; n<rules.length; n++) {
        rr[n]= kbct.GetRule(rules[n]+1);
        kbct.RemoveRule(rules[n]);
      }
      //System.out.println("Jconsitency: NbR(exp)="+kbct.GetNbRules());
    }
    ////////////
    // crear un fis sin reglas al que se a�adira cada vez solo las reglas conflictivas
    JKBCT kbctaux= new JKBCT(kbct);
    //long ptr= kbctaux.GetPtr();
    kbctaux.SetKBCTFile(KBfileLinks.getAbsolutePath());
    int lim= kbctaux.GetNbRules();
    for (int i=0; i<lim; i++) {
      kbctaux.RemoveRule(0);
    }
    Vector<Integer> rulesToRemove= new Vector<Integer>();
    Object[] objRulesExp= expRules.toArray();
    //System.out.println("Jconsitency: NbR (pbjexp)="+objRulesExp.length);
    if (expand) {
      int INI1= ((Integer)objRulesExp[0]).intValue() - SelectedRules.length;
      // comparar las reglas expandidas para ver cuales producen conflicto (igual premisa pero diferente conclusi�n)
      for (int n=1; n<objRulesExp.length-1; n++) {
        int FIN1= INI1 + ((Integer)objRulesExp[n]).intValue() +1;
        for (int m=INI1; m<FIN1; m++) {
          if (m==0)
        	  m++;
          
          Rule r1= kbct.GetRule(m);
          int INI2= FIN1;
          int FIN2= INI2 + ((Integer)objRulesExp[n+1]).intValue();
          for (int k=INI2; k<FIN2; k++) {
        	if (k==0)
        		k++;
        	
            Rule r2= kbct.GetRule(k);
            if ( (JConsistency.SamePremise(r1, r2)) && (JConsistency.DifferentConclusions(r1, r2)) ) {
              int R1Num= r1.GetNumber();
              int R2Num= r2.GetNumber();
              // determinar que regla de las conflictivas se debe eliminar (conclusion equivocada segun datos)
              //System.out.println("Jconsitency: conflict rules r1="+R1Num+"  r2="+R2Num);
              kbctaux.AddRule(r1);
              kbctaux.AddRule(r2);
              int ret= this.ruleToRemove(kbctaux, KBfileLinks, FISfileLinks, jedf);
              kbctaux.RemoveRule(0);
              kbctaux.RemoveRule(0);
              if (ret > 0) {
                switch (ret) {
                  case 1: rulesToRemove.add(new Integer(R1Num));
                          break;
                  case 2: rulesToRemove.add(new Integer(R2Num));
                          break;
                }
              } else {
                  rulesToRemove.add(new Integer(R1Num));
                  rulesToRemove.add(new Integer(R2Num));
              }
            } else if ((split) && (JConsistency.SamePremise(r1, r2)) && (!JConsistency.DifferentConclusions(r1, r2))) {
                int R1Num= r1.GetNumber();
                int R2Num= r2.GetNumber();
            	if (r1MoreSpecific) {
            		rulesToRemove.add(new Integer(R1Num));
            	} else {
            		rulesToRemove.add(new Integer(R2Num));            		
            	}
            }
          }
        }
        INI1= FIN1 + 1;
      }
    } else {
        int R1Num= SelectedRules[0]+1;
        int R2Num= SelectedRules[1]+1;
        // determinar que regla de las conflictivas se debe eliminar (conclusion equivocada segun datos)
        Rule r1= kbct.GetRule(R1Num);
        Rule r2= kbct.GetRule(R2Num);
        kbctaux.AddRule(r1);
        kbctaux.AddRule(r2);
        int ret= this.ruleToRemove(kbctaux, KBfileLinks, FISfileLinks, jedf);
        kbctaux.RemoveRule(0);
        kbctaux.RemoveRule(0);
        if (ret > 0) {
          switch (ret) {
            case 1: rulesToRemove.add(new Integer(R1Num));
                    break;
            case 2: rulesToRemove.add(new Integer(R2Num));
                    break;
          }
        } else {
            rulesToRemove.add(new Integer(R1Num));
            rulesToRemove.add(new Integer(R2Num));
        }
    }
    kbctaux.Close();
    kbctaux.Delete();
    //jnikbct.DeleteKBCT(ptr+1);
    Object[] rulesVaux= rulesToRemove.toArray();
    // Delete repeated rules
    Vector<Integer> vaux= new Vector<Integer>();
    for (int n=0; n<rulesVaux.length; n++) {
    	boolean warnRuleRepeated= false;
    	for (int k=n+1; k<rulesVaux.length; k++) {
    		if (((Integer)rulesVaux[n]).intValue()==((Integer)rulesVaux[k]).intValue()) {
    			warnRuleRepeated= true;
    			break;
    		}
    	}
    	if (!warnRuleRepeated)
    		vaux.add(((Integer)rulesVaux[n]).intValue());
    }
    Object[] rulesRem= vaux.toArray();
    int[] rulesRemOrd= new int[rulesRem.length];
    if (rulesRem.length > 0) {
        for (int n=0; n<rulesRemOrd.length; n++) {
          rulesRemOrd[n]= ((Integer)rulesRem[n]).intValue();
        }
        for (int n=1; n<rulesRemOrd.length; n++) {
          for (int m=0; m<n; m++) {
            if (rulesRemOrd[n] > rulesRemOrd[m]) {
              int v= rulesRemOrd[m];
              rulesRemOrd[m]= rulesRemOrd[n];
              for (int k= n; k>m+1; k--) {
                rulesRemOrd[k]= rulesRemOrd[k-1];
              }
              rulesRemOrd[m+1]= v;
              break;
          }
        }
      }
    }
    if (expand) {
      int L0= ((Integer)objRulesExp[0]).intValue() - SelectedRules.length;
      int L1= ((Integer)objRulesExp[1]).intValue();
      int L2= ((Integer)objRulesExp[2]).intValue();
      // eliminar las reglas conflictivas que eran erroneas
      boolean w1= false;
      boolean w2= false;
      for (int n=0; n<rulesRemOrd.length; n++) {
        kbct.RemoveRule(rulesRemOrd[n]-1);
        if (rulesRemOrd[n] <= L0+L1) {
          L1--;
          w1= true;
        } else {
          L2--;
          w2= true;
        }
      }
      int NbOldRules= kbct.GetNbRules();
      boolean end= false;
      if (!w1) {
        if (L1 > 1) {
          for (int n=L1-1; n>=0; n--) {
            kbct.RemoveRule(L0 + n);
          }
          if (order)
              kbct.AddRule(rr[1]);
          else
        	  kbct.AddRule(rr[0]);
        }
      } else {
    	if (L1 > 1) {
          while (!end) {
            int[] SelRules1= new int[L1];
            for (int n=0; n<SelRules1.length; n++) {
              SelRules1[n]= L0 + n;
            }
            // reagrupar las reglas que quedan procedentes de r1
            if (SelRules1.length > 1) {
               double[][] qold= this.KBquality(kbct, null, 0, 0, jedf);
               if (order)
             	  kbct= this.MergeRules(kbct, SelRules1, jedf, qold, rr[1]);
               else
             	  kbct= this.MergeRules(kbct, SelRules1, jedf, qold, rr[0]);
            }
            int NbNewRules= kbct.GetNbRules();
            if (NbNewRules == NbOldRules ) {
              end= true;
            } else {
                NbOldRules= NbNewRules;
                L1= NbNewRules - L0 - L2;
                if (L1<=1) {
                  end= true;
                }
            }
          }
        }
      }
      end= false;
      NbOldRules= kbct.GetNbRules();
      if (!w2) {
        if (L2 > 1) {
          for (int n=L2-1; n>=0; n--) {
            kbct.RemoveRule(NbOldRules - L2 + n);
          }
          if (order)
              kbct.AddRule(rr[0]);
          else
        	  kbct.AddRule(rr[1]);
        }
      } else {
        if (L2 > 1) {
          while (!end) {
            int Nr= kbct.GetNbRules();
            int[] SelRules2= new int[L2];
            for (int n=0; n<SelRules2.length; n++) {
                 SelRules2[n]= Nr - L2 + n;
            }
            // reagrupar las reglas que quedan procedentes de r2
            if (SelRules2.length > 1) {
              double[][] qold= this.KBquality(kbct, null, 0, 0, jedf);
              if (order)
              	  kbct= this.MergeRules(kbct, SelRules2, jedf, qold, rr[0]);
              else
            	  kbct= this.MergeRules(kbct, SelRules2, jedf, qold, rr[1]);
            }
            int NbNewRules= kbct.GetNbRules();
            if (NbNewRules == NbOldRules )
              end= true;
            else {
              L2= L2 - (NbOldRules - NbNewRules);
              NbOldRules= NbNewRules;
              if (L2<=1)
                end= true;
            }
          }
        } 
      }
    } else {
      for (int n=0; n<rulesRemOrd.length; n++) {
        kbct.RemoveRule(rulesRemOrd[n]-1);
      }
    }
    return kbct;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Expanding a rule into its canonical form.
   * @param kbct initial knowledge base
   * @param Nbr number of rule to expand
   * @return the set of expanded rules
   */
  public Rule[] expandRule ( JKBCT kbct, int Nbr ) {
    Vector<Rule> expandedRules= new Vector<Rule>();
    Rule r= kbct.GetRule(Nbr);
    int[] inputs= r.Get_in_labels_number();
    int[] outputs= r.Get_out_labels_number();
    boolean warning= false;
    for (int n=0; n<inputs.length; n++) {
      int NbLab= kbct.GetInput(n+1).GetLabelsNumber();
       if (inputs[n] > NbLab) {
          if (inputs[n] <= 2*NbLab) {
            warning= true;
            int L= inputs[n] - NbLab;
            for (int m=0; m<NbLab; m++) {
              if (m+1 != L) {
                int[] inputsNew= new int[inputs.length];
                for (int k=0; k<inputs.length; k++) {
                  if (k==n) {
                      inputsNew[k]= m+1;
                  } else {
                      inputsNew[k]= inputs[k];
                  }
                }
                int[] outputsNew= new int[outputs.length];
                for (int k=0; k<outputs.length; k++) {
                  outputsNew[k]= outputs[k];
                }
                Rule rNew= new Rule(kbct.GetNbRules(), inputsNew.length, outputsNew.length, inputsNew, outputsNew, r.GetType(), r.GetActive());
                kbct.AddRule( rNew );
                Rule[] rres= this.expandRule(kbct, rNew.GetNumber());
                for (int k=0; k<rres.length; k++) {
                   expandedRules.add(rres[k]);
                }
              }
            }
            break;
          } else {
            warning= true;
            int selLab= inputs[n] - 2*NbLab;
            int NbORLab= jnikbct.NbORLabels(selLab, NbLab);
            int firstLab= jnikbct.option(selLab, NbORLab, NbLab);
            for (int m=firstLab-1; m<firstLab+NbORLab-1; m++) {
                int[] inputsNew= new int[inputs.length];
                for (int k=0; k<inputs.length; k++) {
                  if (k==n) {
                      inputsNew[k]= m+1;
                  } else {
                      inputsNew[k]= inputs[k];
                  }
                }
                int[] outputsNew= new int[outputs.length];
                for (int k=0; k<outputs.length; k++) {
                  outputsNew[k]= outputs[k];
                }
                Rule rNew= new Rule(kbct.GetNbRules(), inputsNew.length, outputsNew.length, inputsNew, outputsNew, r.GetType(), r.GetActive());
                kbct.AddRule( rNew );
                Rule[] rres= this.expandRule(kbct, rNew.GetNumber());
                for (int k=0; k<rres.length; k++) {
                   expandedRules.add(rres[k]);
                }
              }
            break;
          }
      }
    }
    if (!warning) {
      expandedRules.add(r);
    }
    Object[] obj= expandedRules.toArray();
    Rule[] result= new Rule[obj.length];
    for (int n=0; n<result.length; n++) {
      result[n]= (Rule)obj[n];
    }
    return result;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Expanding conflict rules.
   * @param kbct initial knowledge base
   * @param ruleToExpand number of rule to expand (rule1)
   * @param ConfRule number of conflict rule to expand (rule2)
   * @param commonPart flag to expand the common part or not
   * @return set of expanded rules
   */
  public Rule[] expandConflictRules ( JKBCT kbct, int ruleToExpand, int ConfRule, boolean commonPart ) {
    int Nr1= ruleToExpand;
    int Nr2= ConfRule;
    Vector<Rule> expandedRules= new Vector<Rule>();
    Rule r1= kbct.GetRule(Nr1);
    int[] inputs1= r1.Get_in_labels_number();
    int[] inputs_exp1= r1.Get_in_labels_exp();
    int[] outputs1= r1.Get_out_labels_number();
    Rule r2= kbct.GetRule(Nr2);
    int[] inputs2= r2.Get_in_labels_number();
    boolean warning= false;
    for (int n=0; n<inputs1.length; n++) {
      int NbLab= kbct.GetInput(n+1).GetLabelsNumber();
      if (inputs_exp1[n]==0) {
        if (inputs1[n]==0) {
          warning= true;
          int[] newpremise= this.buildNewPremises(inputs1[n], inputs2[n], "BLANK", NbLab, commonPart);
          for (int m=0; m<newpremise.length; m++) {
        	Rule rNew= this.buildExpandedRule(inputs1, n, newpremise[m], outputs1, r1.GetType(), r1.GetActive());
            kbct.AddRule( rNew );
            Rule[] rres= this.expandConflictRules(kbct, rNew.GetNumber(), Nr2, commonPart);
            for (int k=0; k<rres.length; k++) {
               expandedRules.add(rres[k]);
            }
          }
          break;
        } else if (inputs1[n] > NbLab) {
                if (inputs1[n] <= 2*NbLab) {
                  warning= true;
                  int NOTlabel=0;
                  String type;
                  int auxPrem=0;
                  int basicLab=inputs1[n]-NbLab;
                  if ( (basicLab>1) && (basicLab<NbLab) && (inputs2[n] > 0) && (NbLab > 3) ) {
                	  if (inputs2[n] <= NbLab) {
                		     if (inputs2[n] < basicLab) {
                			   NOTlabel=jnikbct.NumberORLabel(1, basicLab-1, NbLab);
                			   auxPrem=jnikbct.NumberORLabel(basicLab+1, NbLab-basicLab, NbLab);
                		     } else {
               		    	   auxPrem=jnikbct.NumberORLabel(1, basicLab-1, NbLab);
                			   NOTlabel=jnikbct.NumberORLabel(basicLab+1, NbLab-basicLab, NbLab);
                		     }
                 	  } else if (inputs2[n] > 2*NbLab) {
                         int selLab= inputs2[n] - 2*NbLab;
                         int NbORLabMod= jnikbct.NbORLabels(selLab, NbLab);
                         int first= jnikbct.option(selLab, NbORLabMod, NbLab);
                 		 if (first < basicLab) {
                			 NOTlabel=jnikbct.NumberORLabel(1, basicLab-1, NbLab);
                			 auxPrem=jnikbct.NumberORLabel(basicLab+1, NbLab-basicLab, NbLab);
                		 } else {
                			 auxPrem=jnikbct.NumberORLabel(1, basicLab-1, NbLab);
                			 NOTlabel=jnikbct.NumberORLabel(basicLab+1, NbLab-basicLab, NbLab);
                		 }
                	  }
                 	 type="OR";
                  } else {
                 	 NOTlabel=inputs1[n];
                 	 type="NOT";
                  }
                  int[] newpremise= this.buildNewPremises(NOTlabel, inputs2[n], type, NbLab, commonPart);
                  if (auxPrem>0) {
                	  int[] aux= new int[newpremise.length+1];
                      aux[0]= auxPrem;
                	  for (int i=1; i<aux.length; i++) {
                		  aux[i]= newpremise[i-1];
                	  }
                	  newpremise= aux;
                  }
                  for (int m=0; m<newpremise.length; m++) {
                	Rule rNew= this.buildExpandedRule(inputs1, n, newpremise[m], outputs1, r1.GetType(), r1.GetActive());
                    kbct.AddRule( rNew );
                    Rule[] rres= this.expandConflictRules(kbct, rNew.GetNumber(), Nr2, commonPart);
                    for (int k=0; k<rres.length; k++) {
                       expandedRules.add(rres[k]);
                    }
                  }
                break;
              } else {
                warning= true;
                int[] newpremise= this.buildNewPremises(inputs1[n], inputs2[n], "OR", NbLab, commonPart);
                for (int m=0; m<newpremise.length; m++) {
                	Rule rNew= this.buildExpandedRule(inputs1, n, newpremise[m], outputs1, r1.GetType(), r1.GetActive());
                    kbct.AddRule( rNew );
                    Rule[] rres= this.expandConflictRules(kbct, rNew.GetNumber(), Nr2, commonPart);
                    for (int k=0; k<rres.length; k++) {
                       expandedRules.add(rres[k]);
                    }
                  }
                 break;
              }
          }
       }
    }
    if (!warning) {
      expandedRules.add(r1);
    }
    Object[] obj= expandedRules.toArray();
    Rule[] result= new Rule[obj.length];
    for (int n=0; n<result.length; n++) {
      result[n]= (Rule)obj[n];
    }
    return result;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Building a expanded rule.
   * @param inputs1 initial premises
   * @param n number of the new premise (number of variable)
   * @param newpremise value of the new premise (number of label)
   * @param outputs1 initial outputs
   * @param type rule type (E,P,I)
   * @param active flag to know if the rule is active or not
   * @return the expanded rule
   */
  private Rule buildExpandedRule ( int[] inputs1, int n, int newpremise, int[] outputs1, String type, boolean active ) {
      int[] inputsNew= new int[inputs1.length];
      for (int k=0; k<inputs1.length; k++) {
        if (k==n) {
            inputsNew[k]= newpremise;
        } else {
            inputsNew[k]= inputs1[k];
        }
      }
      int[] outputsNew= new int[outputs1.length];
      for (int k=0; k<outputs1.length; k++) {
        outputsNew[k]= outputs1[k];
      }
      Rule rNew= new Rule(kbct.GetNbRules(), inputsNew.length, outputsNew.length, inputsNew, outputsNew, type, active);
      int[] premises_exp = new int[inputsNew.length];
      for (int k=0; k<premises_exp.length; k++) {
          if (k<=n)
    	      premises_exp[k]=1;
          else
        	  premises_exp[k]=0;
      }
      rNew.Set_in_labels_exp(premises_exp);
      return rNew;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Building a new premises.
   * @param label label1
   * @param labelMod label taken as model (for comparison)
   * @param type label type (OR, BLANK)
   * @param NbLab number of basic labels (in current variable)
   * @param commonPart flag to expand the common part or not
   * @return the expanded rule
   */
  private int[] buildNewPremises ( int label, int labMod, String type, int NbLab, boolean commonPart ) {
	int[] newpremise= new int[2];
    boolean flag= false;
    if (!commonPart) {
      if (labMod==0) {
         flag=true;
	  } else if (label > 0) {
          if (label==labMod)
          	  flag=true;
          else if ( (type.equals("OR")) && (label==NbLab) && (labMod==label) )
        	  flag=true;
          else if ( (label <= NbLab) && (labMod > NbLab) )
			  flag=true;
		  else if ( (label > NbLab) && (label <= 2*NbLab) ) {
		      if ( (labMod==label-NbLab) || ( (labMod>NbLab) && (labMod<=2*NbLab) && (label != labMod) ) )
		      	  flag=true;
		      else if (labMod>2*NbLab) {
                  int selLab= labMod - 2*NbLab;
                  int NbORLab= jnikbct.NbORLabels(selLab, NbLab);
                  int first= jnikbct.option(selLab, NbORLab, NbLab);
                  int basicNOT= label-2*NbLab;
                  if ( (basicNOT>=first) && (basicNOT<=first+NbORLab-1) )
                    	flag= true;
		      }
	      } else if (label > 2*NbLab) {
              if ( (labMod > NbLab) && (labMod <= 2*NbLab) )
            	  flag=true;
              else if (labMod > 2*NbLab) {
                  int selLab1= label - 2*NbLab;
                  int NbORLab1= jnikbct.NbORLabels(selLab1, NbLab);
                  int first1= jnikbct.option(selLab1, NbORLab1, NbLab);
                  int selLab2= labMod - 2*NbLab;
                  int NbORLab2= jnikbct.NbORLabels(selLab2, NbLab);
                  int first2= jnikbct.option(selLab2, NbORLab2, NbLab);
                  if ( (first2 < first1) || (NbORLab2>NbORLab1) || (first2+NbORLab2 > first1+NbORLab1) )
                    	flag= true;
	    	  }
	      }
      }
	  if (flag) {
      	  newpremise= new int[1];
      	  newpremise[0]=label;
      } else {
        if (type.equals("BLANK")) {
  	      if (labMod<=NbLab) {
    	      // label is a elementary label
    	      newpremise[0]=labMod;
    	      if (NbLab > 2)
    	          newpremise[1]=labMod+NbLab;
    	      else {
    	    	  if (labMod==1)
    	    	      newpremise[1]=2;
    	    	  else
    	    	      newpremise[1]=1;
    	      }
          } else if (labMod<=2*NbLab) {
    	      // label is a composite NOT label
              if (NbLab > 2)
        	      newpremise[0]=labMod-NbLab;
              else {
    	    	  if (labMod==3)
    	    	      newpremise[1]=2;
    	    	  else
    	    	     newpremise[1]=1;
              }
    	      newpremise[1]=labMod;
          } else {
    	      // label is a composite OR label
              int selLab= labMod - 2*NbLab;
              int NbORLab= jnikbct.NbORLabels(selLab, NbLab);
              int firstLab= jnikbct.option(selLab, NbORLab, NbLab);
              if ( (firstLab>1) && (firstLab+NbORLab-1<NbLab) )  {
    	          newpremise= new int[3];
    	          newpremise[0]=labMod;
                  if (firstLab==2) {
        	          newpremise[1]=1;
                  } else {
        	          int NbNewORLab1=firstLab-1;
    	              newpremise[1]=jnikbct.NumberORLabel(1, NbNewORLab1, NbLab);
                  }
                  if (firstLab+NbORLab==NbLab) {
        	          newpremise[2]=NbLab;
                  } else {
        	          int NbNewORLab2=NbLab-(firstLab+NbORLab-1);
    	              newpremise[2]=jnikbct.NumberORLabel(firstLab+NbORLab, NbNewORLab2, NbLab);
                  }
              } else {
    	          int NbNewORLab=NbLab-NbORLab;
                  if (firstLab==1) {
        	          newpremise[0]=labMod;
                      if (NbNewORLab==1)
                  	      newpremise[1]=NbLab;
                      else
        	              newpremise[1]=jnikbct.NumberORLabel(firstLab+NbORLab, NbNewORLab, NbLab);
                  } else {
                      if (NbNewORLab==1)
                	      newpremise[0]=1;
                      else
        	              newpremise[0]=jnikbct.NumberORLabel(1, NbNewORLab, NbLab);

                      newpremise[1]=labMod;
                  }
              }
          }
        } else if (type.equals("OR")) {
              int selLab= label - 2*NbLab;
              int NbORLabMod= jnikbct.NbORLabels(selLab, NbLab);
              int first= jnikbct.option(selLab, NbORLabMod, NbLab);
          	  int last= first + NbORLabMod - 1;
              if (labMod<=NbLab) {
        	    // label is a elementary label
                if ( (labMod>first) && (labMod<last) ) {
                	newpremise= new int[3];
                	if (labMod==first+1) {
                		newpremise[1]=first;
                		if (NbORLabMod-2>1)
                	        newpremise[2]=jnikbct.NumberORLabel(first+2, NbORLabMod-2, NbLab);
                		else
                			newpremise[2]=labMod+1;
                	} else if (labMod==last-1) {
                		newpremise[1]=last;
                		if (NbORLabMod-2>1)
                    	    newpremise[2]=jnikbct.NumberORLabel(first, NbORLabMod-2, NbLab);
                		else
                			newpremise[2]=labMod-1;
                	} else {
                		newpremise[1]=jnikbct.NumberORLabel(first, labMod-first, NbLab);
                	    newpremise[2]=jnikbct.NumberORLabel(labMod+1, last-labMod, NbLab);
                	}
                } else {
                	int NbNewORLab= NbORLabMod - 1;
                    if (NbNewORLab>1) {
                	  if (labMod==first) {
                	      newpremise[1]=jnikbct.NumberORLabel(first+1, NbNewORLab, NbLab);
                      } else {
                	      newpremise[1]=jnikbct.NumberORLabel(first, NbNewORLab, NbLab);
                      }
                    } else {
                        if (labMod==first)
                    	    newpremise[1]=last;
                        else
                        	newpremise[1]=first;
                    }
                }
 	    	    newpremise[0]=labMod;
              } else {
        	    // label is a composite OR label
                selLab= labMod - 2*NbLab;
                int NbORLab= jnikbct.NbORLabels(selLab, NbLab);
                if (NbORLab < NbORLabMod) {
                  int firstLab= jnikbct.option(selLab, NbORLab, NbLab);
                  if ( (firstLab>first) && (firstLab+NbORLab-1<last) )  {
        	          newpremise= new int[3];
        	          newpremise[0]=labMod;
                      if (firstLab==first+1) {
            	          newpremise[1]=first;
                      } else {
            	          int NbNewORLab1=firstLab-first;
        	              newpremise[1]=jnikbct.NumberORLabel(first, NbNewORLab1, NbLab);
                      }
                      if (firstLab+NbORLab==last) {
            	          newpremise[2]=last;
                      } else {
            	          int NbNewORLab2=last-(firstLab+NbORLab-1);
        	              newpremise[2]=jnikbct.NumberORLabel(firstLab+NbORLab, NbNewORLab2, NbLab);
                      }
                  } else {
        	          int NbNewORLab=last-first-NbORLab+1;
                      if (firstLab==first) {
            	          newpremise[0]=labMod;
                          if (NbNewORLab==1)
                    	      newpremise[1]=last;
                          else
            	              newpremise[1]=jnikbct.NumberORLabel(firstLab+NbORLab, NbNewORLab, NbLab);
                      } else {
                          if (NbNewORLab==1)
                      	      newpremise[0]=first;
                          else
            	              newpremise[0]=jnikbct.NumberORLabel(first, NbNewORLab, NbLab);

                          newpremise[1]=labMod;
                      }
                  }
                }
              }
        } else {
            int basicLab= label-NbLab;
            int first=1;
            int last= NbLab;
		    if (basicLab==1) {
    	  	    first++;
		    } else if (basicLab==NbLab) {
                last--;
		    }
		    int NbORLabMod= NbLab-1;
		    if (NbORLabMod > 2) {
              if (labMod <= NbLab) {
                  if ( (labMod>first) && (labMod<last) ) {
                	  newpremise= new int[3];
                	  if (labMod==first+1) {
                		  newpremise[1]=first;
                	      newpremise[2]=jnikbct.NumberORLabel(first+2, NbORLabMod-2, NbLab);
                	  } else if (label==last-1) {
                		  newpremise[1]=last;
              	          newpremise[2]=jnikbct.NumberORLabel(first, NbORLabMod-2, NbLab);
              	      } else {
              		      newpremise[1]=jnikbct.NumberORLabel(first, labMod-first, NbLab);
              	          newpremise[2]=jnikbct.NumberORLabel(labMod+1, last-labMod, NbLab);
              	      }
                  } else {
              	      int NbNewORLab= NbORLabMod - 1;
                      if (labMod==first) {
              	          newpremise[1]=jnikbct.NumberORLabel(first+1, NbNewORLab, NbLab);
                      } else {
              	          newpremise[1]=jnikbct.NumberORLabel(first, NbNewORLab, NbLab);
                      }
                  }
	    	      newpremise[0]=labMod;
              } else if (labMod > 2*NbLab) {
                int selLab= labMod - 2*NbLab;
                int NbORLab= jnikbct.NbORLabels(selLab, NbLab);
                int firstLab= jnikbct.option(selLab, NbORLab, NbLab);
                if ( (firstLab>first) && (firstLab+NbORLab-1<last) )  {
        	        newpremise= new int[3];
        	        newpremise[0]=labMod;
                    if (firstLab==first+1) {
            	        newpremise[1]=first;
                    } else {
            	        int NbNewORLab1=firstLab-first;
        	            newpremise[1]=jnikbct.NumberORLabel(first, NbNewORLab1, NbLab);
                    }
                    if (firstLab+NbORLab==last) {
            	        newpremise[2]=last;
                    } else {
            	        int NbNewORLab2=last-(firstLab+NbORLab-1);
        	            newpremise[2]=jnikbct.NumberORLabel(firstLab+NbORLab, NbNewORLab2, NbLab);
                    }
                } else {
        	        int NbNewORLab=NbLab-NbORLab-1;
                    if (firstLab==first) {
            	        newpremise[0]=labMod;
                        if (NbNewORLab==1)
                    	    newpremise[1]=last;
                        else
            	            newpremise[1]=jnikbct.NumberORLabel(firstLab+NbORLab, NbNewORLab, NbLab);
                    } else {
                        if (NbNewORLab==1)
                    	    newpremise[0]=first;
                        else
            	            newpremise[0]=jnikbct.NumberORLabel(first, NbNewORLab, NbLab);

                        newpremise[1]=labMod;
                    }
                }
              }
		    } else {
			  newpremise[0]=labMod;
              switch (labMod) {
                case 1: if (basicLab==2)
                	        newpremise[1]=3;
                        else
                	        newpremise[1]=2;
                        break;
                case 2: if (basicLab==1)
        	                newpremise[1]=3;
                        else
        	                newpremise[1]=1;
                        break;
                case 3: if (basicLab==2)
        	                newpremise[1]=1;
                        else
        	                newpremise[1]=2;
                        break;
              }
            }
          }
        }
      } else {
    	  if ( (label==labMod) || (label <= NbLab) ) {
          	  newpremise= new int[1];
          	  newpremise[0]=label;
          } else {
        	  if (label==0) {
            	  newpremise= new int[NbLab];
                  for (int n=0; n<NbLab; n++)
            	       newpremise[n]=n+1;
        	  } else if ( (label > NbLab) && (label <= 2*NbLab) ) {
        		  // NOT
            	  newpremise= new int[NbLab-1];
            	  int NL= label - NbLab;
            	  int cont=0;
                  for (int n=0; n<NbLab; n++) {
                      if (n+1 != NL)
                	      newpremise[cont++]=n+1;
                  }
        	  } else {
        		  // OR
                  int selLab= label - 2*NbLab;
                  int NbORLab= jnikbct.NbORLabels(selLab, NbLab);
                  int firstLab= jnikbct.option(selLab, NbORLab, NbLab);
            	  newpremise= new int[NbORLab];
            	  int cont=0;
        		  for (int n=firstLab; n<firstLab+NbORLab; n++) {
        			  newpremise[cont++]=n;
        		  }
        	  }
          }
      }
      return newpremise;	  
  }
//------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Setting the rule to remove.
   * @param kbct initial knowledge base
   * @param KBfileLinks info Links (KBCT)
   * @param FISfileLinks info Links (FIS)
   * @param jedf data file
   * @return the number of rule to remove (-2 means no rule to remove)
   */
    public int ruleToRemove ( JKBCT kbct, File KBfileLinks, File FISfileLinks, JExtendedDataFile jedf ) {
      try {
        JKBCT kbctaux= new JKBCT(kbct);
        //long ptr= kbctaux.GetPtr();
        //jnifis.FisproPath(MainKBCT.getConfig().GetKbctPath());
        jnifis.setUserHomeFisproPath(MainKBCT.getConfig().GetKbctPath());
        JExtendedDataFile DataFile= jedf;
        if (DataFile != null) {
          double[] ruleWeights= new double[2];
          double[] ruleWeightsCC= new double[2];
          double[] ruleWeightsIC= new double[2];
          int[] orderedRules= new int[2];
          JExtendedDataFile jedfauxCC= new JExtendedDataFile(DataFile.ActiveFileName(), true);
          JExtendedDataFile jedfauxIC= new JExtendedDataFile(DataFile.ActiveFileName(), true);
          int NbActiveData= jedfauxCC.GetActiveCount(); 
          int NbRules= kbctaux.GetNbRules();
          boolean[] actives= new boolean[NbRules];
          for (int n=0; n<NbRules; n++) {
    	    Rule r= kbctaux.GetRule(n+1);
    	    actives[n]= r.GetActive();
            r.SetActive(false);
          }
          Rule[] rules= new Rule[2];
            for (int i=0; i<2; i++) {
               rules[i]= kbctaux.GetRule(i+1);
               if (actives[i]) {
                  kbctaux.GetRule(i+1).SetActive(true);
                  int[] outputs= rules[i].Get_out_labels_number();
                  kbctaux.SetKBCTFile(KBfileLinks.getAbsolutePath());
                  kbctaux.Save();
                  kbctaux.SaveFIS(FISfileLinks.getAbsolutePath());
                  JFIS fis= new JFIS(FISfileLinks.getAbsolutePath());
                  ruleWeightsCC[i]=this.CumulatedWeights(jedfauxCC, outputs, fis, true);
                  ruleWeightsIC[i]=this.CumulatedWeights(jedfauxIC, outputs, fis, false);
               	  ruleWeights[i]= ruleWeightsCC[i] - ruleWeightsIC[i];
                  kbctaux.GetRule(i+1).SetActive(false);
               } else { 
            	   ruleWeights[i]= -NbActiveData;
               } 
            }
            kbctaux.Close();
            kbctaux.Delete();
            //jnikbct.DeleteKBCT(ptr+1);
            // ordenar las reglas
            for (int i=0; i<ruleWeights.length; i++) {
        	    orderedRules[i]= i+1;
            }
            int lim=ruleWeights.length;
            for (int i=1; i<lim; i++) {
                    for (int n=0; n<i; n++) {
                    	if (ruleWeights[orderedRules[i]-1]>ruleWeights[orderedRules[n]-1]) {
                    		for (int m=i; m>n; m--) {
                    			orderedRules[m]=orderedRules[m-1];
                    		}
                    		orderedRules[n]=i+1;
                            break;
                    	}
                    }
            }
            return orderedRules[1];
        }
      } catch (Throwable t) {
          t.printStackTrace();
          MessageKBCT.Error(null, t);
      }
      return -2;
    }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Return number of new label obtained as merge of "lab_num1" and "lab_num2".
   * </p>
   * @param lab_num1 number of first label
   * @param lab_num2 number of second label
   * @param NOL number of labels
   * @return number of merged label (-1 means that there is no merged label)
   */
  private int NumberMergedLabel( int lab_num1, int lab_num2, int NOL ) {
    int result=-1;
    int min= Math.min(lab_num1, lab_num2);
    int max= Math.max(lab_num1, lab_num2);
    int NbOR= 0;
    int option= 0;
    if (min==0) {
      result=0;
    } else if ( (min <= NOL) && (max <= NOL) && (max-min==1) ) {
      option= min;
      NbOR= 2;
      if (NOL==2)
        result= 0;
      else
        result= jnikbct.NumberORLabel( option, NbOR, NOL);
    } else if ( (min <= NOL) && (max==min+NOL) ){
      result= 0;
    } else if ( (min <= NOL) && (max > NOL) && (max <= 2*NOL) ){
        if (NOL==2)
          result= min;
        else if (max-NOL != min)
          result= max;
    } else if ( (min <= NOL) && (max > 2*NOL) ) {
        int SelLabel= max-2*NOL;
        int auxNbOR= jnikbct.NbORLabels(SelLabel, NOL);
        int optionaux= jnikbct.option(SelLabel, auxNbOR, NOL);
        if ( (min >= optionaux) && (min <= optionaux+auxNbOR-1) ) {
          result= max;
        } else if (Math.abs(optionaux-min) == 1) {
          NbOR= auxNbOR+1;
          option= min;
          result = jnikbct.NumberORLabel(option, NbOR, NOL);
        } else if (Math.abs(optionaux+auxNbOR-1-min) == 1) {
          NbOR= auxNbOR+1;
          option= optionaux;
          result = jnikbct.NumberORLabel(option, NbOR, NOL);
        }
    } else if ( (min > NOL) && (min <= 2*NOL) && (max > 2*NOL) ) {
        int optionMin= min-NOL;
        int SelLabelmax= max-2*NOL;
        int NbORmax= jnikbct.NbORLabels(SelLabelmax, NOL);
        int optionMax= jnikbct.option(SelLabelmax, NbORmax, NOL);
        if ( (optionMax > optionMin) || (optionMax+NbORmax-1 < optionMin) )
          result= min;
    } else if ( (min > 2*NOL) && (max > 2*NOL) ) {
        int SelLabelmin= min-2*NOL;
        int NbORmin= jnikbct.NbORLabels(SelLabelmin, NOL);
        int optionMin= jnikbct.option(SelLabelmin, NbORmin, NOL);
        int SelLabelmax= max-2*NOL;
        int NbORmax= jnikbct.NbORLabels(SelLabelmax, NOL);
        int optionMax= jnikbct.option(SelLabelmax, NbORmax, NOL);
        if ( (NbORmin < NbORmax) && (optionMin >= optionMax) && (optionMin+NbORmin-1<=optionMax+NbORmax-1) ) {
            result= max;
        } else if (optionMax == optionMin+NbORmin) {
            option= optionMin;
            NbOR= NbORmin+NbORmax;
            result= jnikbct.NumberORLabel(option, NbOR, NOL);
        } else if (optionMin == optionMax+NbORmax) {
            option= optionMax;
            NbOR= NbORmin+NbORmax;
            result= jnikbct.NumberORLabel(option, NbOR, NOL);
        } else if ( (optionMax > optionMin) &&
                    (optionMax <= optionMin+NbORmin-1) ) {
            option= optionMin;
            NbOR= NbORmax + optionMax - optionMin;
            result= jnikbct.NumberORLabel(option, NbOR, NOL);
        } else if ( (optionMax < optionMin) &&
                    (optionMin <= optionMax+NbORmax-1) ) {
            option= optionMax;
            NbOR= NbORmin + optionMin - optionMax;
            result= jnikbct.NumberORLabel(option, NbOR, NOL);
        }
    }

    if (NbOR == NOL-1) {
      // NOT
      if (option==1)
        result= 2*NOL;
      else
        result= NOL+1;
    } else if (result > 2*NOL+jnikbct.serie(NOL-1)-3) {
        result=0;
    }
    return result;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Evaluate quality of knowledge base contains in "kbctaux" after merging rules "r1" and "r2".
   * Return an array with next information:
   * <ul>
   *   <li> Performance. </li>
   *   <li> Coverage. </li>
   *   <li> Number of error cases. </li>
   *   <li> Number of ambiguity cases. </li>
   * </ul>
   * </p>
   * @param kbctaux knowledge base
   * @param r merged rule
   * @param r1 number of the first rule
   * @param r2 number of the second rule
   * @param jcf JConsistencyFrame object
   * @return quality indices [number of output] [ [performance], [coverage], [error cases], [ambiguity cases] ]
   */
  public double[][] KBquality( JKBCT kbctaux, Rule r, int r1, int r2, JExtendedDataFile jedf ) {
    double[][] result= null;
    try {
      JExtendedDataFile DataFile= jedf;
      if ( (DataFile!=null) && (kbctaux.GetNbActiveRules() > 0) ) {
        File temp;
        Rule[] RR= null;
        if ( (this.fis_name == null) || (r==null) ) {
          temp= JKBCTFrame.BuildFile("temprbqcons."+r1+"."+r2+".fis");
          this.fis_name = temp.getAbsolutePath();
          kbctaux.Save();
          kbctaux.SaveFISquality(fis_name);
        } else {
            temp= new File(fis_name);
            int NbRules= kbctaux.GetNbRules();
            Rule[] RRaux= new Rule[NbRules-1];
            int min= Math.min(r1, r2);
            int max= Math.max(r1, r2);
            for (int n=0; n<RRaux.length; n++) {
                if (n==min-1) {
                    int[] r_prem= r.Get_in_labels_number();
                    int[] r_conc= r.Get_out_labels_number();
                    int[] prem= new int[r_prem.length];
                    for (int m=0; m<prem.length; m++)
                      prem[m]= r_prem[m];

                    int[] conc= new int[r_conc.length];
                    for (int m=0; m<conc.length; m++)
                      conc[m]= r_conc[m];

                    RRaux[n]= new Rule(n+1, prem.length, conc.length, prem, conc, r.GetType(), r.GetActive());
                } else if (n < max-1) {
                    Rule raux= kbctaux.GetRule(n+1);
                    int[] raux_prem= raux.Get_in_labels_number();
                    int[] raux_conc= raux.Get_out_labels_number();
                    int[] prem= new int[raux_prem.length];
                    for (int m=0; m<prem.length; m++)
                      prem[m]= raux_prem[m];

                    int[] conc= new int[raux_conc.length];
                    for (int m=0; m<conc.length; m++)
                      conc[m]= raux_conc[m];

                    RRaux[n]= new Rule(n+1, prem.length, conc.length, prem, conc, raux.GetType(), raux.GetActive());
                } else if (n >= max-1) {
                    Rule raux= kbctaux.GetRule(n+2);
                    int[] raux_prem= raux.Get_in_labels_number();
                    int[] raux_conc= raux.Get_out_labels_number();
                    int[] prem= new int[raux_prem.length];
                    for (int m=0; m<prem.length; m++)
                      prem[m]= raux_prem[m];

                    int[] conc= new int[raux_conc.length];
                    for (int m=0; m<conc.length; m++)
                      conc[m]= raux_conc[m];

                    RRaux[n]= new Rule(n+1, prem.length, conc.length, prem, conc, raux.GetType(), raux.GetActive());
                }
            }
            int NbInputs= kbctaux.GetNbInputs();
            int NbOutputs= kbctaux.GetNbOutputs();
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
            RR= JConvert.SpreadRules(RRaux, NbOL, VarType);
            String old_fis_name= fis_name;
            fis_name= JConvert.SaveFisRules(fis_name, RR);
            File f_old_fis= new File(old_fis_name);
            f_old_fis.delete();
        }
        JFIS fiskb= new JFIS(fis_name);
        int NbOutputs= fiskb.NbOutputs();
        result= new double[NbOutputs][4];
        if (fiskb.NbRules() > 0) {
          for (int n=0; n<NbOutputs; n++) {
            String ResultFile=temp.getParentFile() + System.getProperty("file.separator") + "resultcons";
            double[] inf_result= fiskb.Infer(n, DataFile.ActiveFileName(), ResultFile, 0.1, false );
            result[n][0]=inf_result[0];
            result[n][1]=inf_result[1]*100;
            fis.JOutput output = fiskb.GetOutput(n);
            if (output.GetNature()!=0) {
              JKBCTOutput jout= kbctaux.GetOutput(n+1);
              int NbLabels= jout.GetLabelsNumber();
              PerformanceFile PerfFile= new PerformanceFile(ResultFile);
              double[][] data= PerfFile.GetData();
              int NbData= PerfFile.DataLength();
              double[] observed= new double[NbData];
              double[] infered= new double[NbData];
              double[] warning= new double[NbData];
              double[] error= new double[NbData];
              for (int m=0; m<NbData; m++) {
                observed[m]= data[m][0];
                infered[m]= data[m][1];
                warning[m]= data[m][2];
                if (observed[m] != infered[m])
                  error[m]= 1;
                else
                  error[m]= 0;
              }
              double NbOutputError= 0;
              double NbOutputAmbiguity= 0;
              for (int k=0; k<NbData; k++) {
                for (int m=0; m<NbLabels; m++) {
                  if (observed[k]==m+1) {
                    if (warning[k]==2) {
                       NbOutputAmbiguity++;
                    } else if (error[k]!=0)
                       NbOutputError++;
                  }
                }
              }
              result[n][2]= NbOutputError;  // NbErrorsCases
              result[n][3]= NbOutputAmbiguity;  // NbAmbiguityCases
            } else {
              // numerical
              result[n][2]= -1;  // NbErrorsCases
              result[n][3]= -1;  // NbAmbiguityCases
            }
          }
        }
      }
    } catch (java.lang.OutOfMemoryError e) {
        e.printStackTrace();
        MessageKBCT.Error(null, e);
    } catch (Throwable t) {
        t.printStackTrace();
        MessageKBCT.Error(null, t);
    }
    return result;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Evaluate quality of knowledge base contains in "kbctaux" after merging rules "r1" and "r2".
   * Return an array with next information:
   * <ul>
   *   <li> Performance. </li>
   *   <li> Coverage. </li>
   *   <li> Number of error cases. </li>
   *   <li> Number of ambiguity cases. </li>
   * </ul>
   * </p>
   * @param kbctaux knowledge base
   * @param r merged rule
   * @param r1 number of the first rule
   * @param r2 number of the second rule
   * @param jcf JConsistencyFrame object
   * @return quality indices [number of output] [ [performance], [coverage], [error cases], [ambiguity cases] ]
   */
  public double[][] KBfisquality( JFIS fiskb, JKBCT kbctaux, JExtendedDataFile jedf ) {
    double[][] result= null;
    try {
      JExtendedDataFile DataFile= jedf;
      if ( (DataFile!=null) && (kbctaux.GetNbRules() > 0) ) {
        File temp= JKBCTFrame.BuildFile("temprbqcons.fis");
        int NbOutputs= fiskb.NbOutputs();
        result= new double[NbOutputs][4];
        for (int n=0; n<NbOutputs; n++) {
          String ResultFile=temp.getParentFile() + System.getProperty("file.separator") + "resultcons";
          double[] inf_result= fiskb.Infer(n, DataFile.ActiveFileName(), ResultFile, 0.1, false );
          result[n][0]=inf_result[0];
          result[n][1]=inf_result[1]*100;
          fis.JOutput output = fiskb.GetOutput(n);
          if (output.GetNature()!=0) {
            JKBCTOutput jout= kbctaux.GetOutput(n+1);
            int NbLabels= jout.GetLabelsNumber();
            PerformanceFile PerfFile= new PerformanceFile(ResultFile);
            double[][] data= PerfFile.GetData();
            int NbData= PerfFile.DataLength();
            double[] observed= new double[NbData];
            double[] infered= new double[NbData];
            double[] warning= new double[NbData];
            double[] error= new double[NbData];
            for (int m=0; m<PerfFile.DataLength(); m++) {
              observed[m]= data[m][0];
              infered[m]= data[m][1];
              warning[m]= data[m][2];
              if (observed[m] != infered[m])
                error[m]= 1;
              else
                error[m]= 0;
            }
            double NbOutputError= 0;
            double NbOutputAmbiguity= 0;
            for (int k=0; k<NbData; k++) {
              for (int m=0; m<NbLabels; m++) {
                if (observed[k]==m+1) {
                  if (warning[k]==2) {
                     NbOutputAmbiguity++;
                  } else if (error[k]!=0)
                     NbOutputError++;
                }
              }
            }
            result[n][2]= NbOutputError;  // NbErrorsCases
            result[n][3]= NbOutputAmbiguity;  // NbAmbiguityCases
          } else {
            // numerical
            result[n][2]= -1;  // NbErrorsCases
            result[n][3]= -1;  // NbAmbiguityCases
          }
        }
    }
    } catch (java.lang.OutOfMemoryError e) {
    	e.printStackTrace();
        MessageKBCT.Error(null, e);
    } catch (Throwable t) {
        t.printStackTrace();
        MessageKBCT.Error(null, t);
    }
    return result;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Check if "OldQuality" is better than "NewQuality".
   * </p>
   * @param OldQuality quality indices of the knowledge base before changes
   * @param NewQuality quality indices of the knowledge base after changes
   * @return true (precision is reduced) / false
   */
  public boolean GetWorseKBquality( double[][] OldQuality, double[][] NewQuality ) {
    if ( (OldQuality==null) && (NewQuality==null) ) {
      return false;
    }
    if ( (OldQuality!=null) && (NewQuality==null) ) {
      return true;
    }
    for (int n=0; n<OldQuality.length; n++) {
      double lim= OldQuality[n][1] - (MainKBCT.getConfig().GetMaximumLossOfCoverage()/100)*OldQuality[n][1];

      if (NewQuality[n][1] < lim) {
        return true;
      }
      if (MainKBCT.getConfig().GetSelectedPerformance()) {
        lim= OldQuality[n][0] + (MainKBCT.getConfig().GetMaximumLossOfPerformance()/100)*OldQuality[n][0];
        if (NewQuality[n][0] > lim) {
          return true;
        }
      } else {
        if ( (OldQuality[n][2]!=-1) && (NewQuality[n][2] > OldQuality[n][2] + MainKBCT.getConfig().GetMaximumNumberNewErrorCases()) ) {
           return true;
        }
        if ( (OldQuality[n][3]!=-1) && (NewQuality[n][3] > OldQuality[n][3] + MainKBCT.getConfig().GetMaximumNumberNewAmbiguityCases()) ) {
              return true;
        }
        lim= OldQuality[n][0] + (MainKBCT.getConfig().GetMaximumLossOfPerformance()/100)*OldQuality[n][0];
        if (NewQuality[n][0] > lim) {
          return true;
        }
      }
    }
    return false;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left"> Return result of "Analysis of consistency". </p>
   * <p align="left">
   * There are six levels of messages:
   * <ul>
   *   <li> 1) Consistency Errors
   *        <ul>
   *              <li> Rules with indefinite conclusions. </li>
   *              <li> Duplicated rules (same premises and same conclusions). </li>
   *              <li> Contradictory rules (same premises and different conclusions). </li>
   *        </ul>
   *   </li>
   *   <li> 2) Redundancy or Specificity Warnings
   *        <ul>
   *              <li> The input space covered by a rule is included into the one covered by another rule. </li>
   *              <li> Two rules have intersection no empty. </li>
   *        </ul>
   *   </li>
   *   <li> 3) Unusued Input Warnings
   *        <ul>
   *              <li> An input variable is used by none of the rules. </li>
   *        </ul>
   *   </li>
   *   <li> 4) Unusued Input Label Warnings
   *        <ul>
   *              <li> A linguistic term of an input variable is used by none of the rules. </li>
   *        </ul>
   *   </li>
   *   <li> 5) Unusued Output Warnings
   *        <ul>
   *              <li> An output variable is used by none of the rules. </li>
   *        </ul>
   *   </li>
   *   <li> 6) Unusued Output Label Warnings
   *        <ul>
   *              <li> A linguistic term of an output variable is used by none of the rules. </li>
   *        </ul>
   *   </li>
   * </ul>
   * </p>
   * @return Vector with: V_CONSISTENCY_ERRORS, V_REDUNDANCY_OR_SPECIFICITY_WARNINGS, V_UNUSUED_INPUTS_WARNINGS, V_UNUSUED_INPUT_LABELS_WARNINGS, V_UNUSUED_OUTPUTS_WARNINGS, V_UNUSUED_OUTPUT_LABELS_WARNINGS
   */
  public Vector[] getVectors() {
    Vector[] v= new Vector[6];
    v[0]= this.V_CONSISTENCY_ERRORS;
    v[1]= this.V_REDUNDANCY_OR_SPECIFICITY_WARNINGS;
    v[2]= this.V_UNUSUED_INPUTS_WARNINGS;
    v[3]= this.V_UNUSUED_INPUT_LABELS_WARNINGS;
    v[4]= this.V_UNUSUED_OUTPUTS_WARNINGS;
    v[5]= this.V_UNUSUED_OUTPUT_LABELS_WARNINGS;
    return v;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Return Vector:
   * <ul>
   *   <li> Number=1 -> Consistency Errors  </li>
   *   <li> Number=2 -> Redundancy or Specificity Warnings  </li>
   *   <li> Number=3 -> Unusued Input Warnings  </li>
   *   <li> Number=4 -> Unusued Input Label Warnings  </li>
   *   <li> Number=5 -> Unusued Output Warnings  </li>
   *   <li> Number=6 -> Unusued Output Label Warnings  </li>
   * </ul>
   * </p>
   * @param Number of the problem level
   * @return Vector with messages related to the selected problem level
   */
  public Vector getVector(int Number) {
    switch (Number) {
      case 1: return this.V_CONSISTENCY_ERRORS;
      case 2: return this.V_REDUNDANCY_OR_SPECIFICITY_WARNINGS;
      case 3: return this.V_UNUSUED_INPUTS_WARNINGS;
      case 4: return this.V_UNUSUED_INPUT_LABELS_WARNINGS;
      case 5: return this.V_UNUSUED_OUTPUTS_WARNINGS;
      case 6: return this.V_UNUSUED_OUTPUT_LABELS_WARNINGS;
    }
    return null;
  }
  //------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Return WARNING:
   * <ul>
   *   <li> true (There are alarms) </li>
   *   <li> false (Consistency checking finished without alarms) </li>
   * </ul>
   * </p>
   * @return WARNING: true (There are alarms) / false
   */
  public boolean getWARNING() { return this.WARNING; }
  //------------------------------------------------------------------------------
  /**
   * Set knowledge base "kbct".
   * @param kbct current knowledge base
   */
  public void setKBCT(JKBCT kbct) { this.kbct= kbct; }
  //------------------------------------------------------------------------------
  /**
   * Generate Enumeration objects from the Vector ones.
   */
  public void TranslateVectorsToEnumeration() {
    this.CONSISTENCY_ERRORS= this.V_CONSISTENCY_ERRORS.elements();
    this.REDUNDANCY_OR_SPECIFICITY_WARNINGS= this.V_REDUNDANCY_OR_SPECIFICITY_WARNINGS.elements();
    this.UNUSUED_INPUTS_WARNINGS= this.V_UNUSUED_INPUTS_WARNINGS.elements();
    this.UNUSUED_INPUT_LABELS_WARNINGS= this.V_UNUSUED_INPUT_LABELS_WARNINGS.elements();
    this.UNUSUED_OUTPUTS_WARNINGS= this.V_UNUSUED_OUTPUTS_WARNINGS.elements();
    this.UNUSUED_OUTPUT_LABELS_WARNINGS= this.V_UNUSUED_OUTPUT_LABELS_WARNINGS.elements();
  }
  //------------------------------------------------------------------------------
  /**
   * Generate error messages for consistency analysis results.
   */
  public Vector[] generateErrorMessages() {
    Vector<InfoConsistency> v_errors_CONSISTENCY_ERRORS= new Vector<InfoConsistency>();
    while (this.CONSISTENCY_ERRORS.hasMoreElements()) {
      InfoConsistency ic= (InfoConsistency)this.CONSISTENCY_ERRORS.nextElement();
      String message= ic.getMessage1();
      String error="";
      if (message.equals("SamePremiseDifferentConclussions"))
          error= LocaleKBCT.GetString("TheRules") + " " +
                 ic.getRuleNum1() + " " +
                 LocaleKBCT.GetString("and") + " " +
                 ic.getRuleNum2() + " " +
                 LocaleKBCT.GetString(message);
      else if (message.equals("SamePremiseSameConclussions"))
          error= LocaleKBCT.GetString("TheRules") + " " +
                 ic.getRuleNum1() + " " +
                 LocaleKBCT.GetString("and") + " " +
                 ic.getRuleNum2() + " " +
                 LocaleKBCT.GetString(message);
      else if (message.equals("IsIncomplete_ConclusionNotDefined"))
          error= "DC"+LocaleKBCT.GetString("TheRule") + " " +
                 ic.getRuleNum1() + " " +
                 LocaleKBCT.GetString(message);

      ic.setError(error);
      v_errors_CONSISTENCY_ERRORS.add(ic);
   }
   this.V_CONSISTENCY_ERRORS= v_errors_CONSISTENCY_ERRORS;
   Vector<InfoConsistency> v_errors_REDUNDANCY_OR_SPECIFICITY_WARNINGS= new Vector<InfoConsistency>();
   while (this.REDUNDANCY_OR_SPECIFICITY_WARNINGS.hasMoreElements()) {
      InfoConsistency ic= (InfoConsistency)this.REDUNDANCY_OR_SPECIFICITY_WARNINGS.nextElement();
      String message= ic.getMessage1();
      String message2= ic.getMessage2();
      String warning="";
      //if ( (message2.equals("DifferentConclusions")) || ( message2.equals("SameConclusions") && (message.equals("HaveIntersectionNoEmpty")) ) )
      if (message2.equals("DifferentConclusions"))
          warning= "DC";
      else if (message2.equals("SameConclusions"))
          warning= "SC";

      if ( (message.equals("HaveIntersectionNoEmpty")) || (message.equals("HaveIntersectionNoEmptyCommomPart")) )
          warning= warning +
                   LocaleKBCT.GetString("TheRules") + " " +
                   ic.getRuleNum1() + " " +
                   LocaleKBCT.GetString("and") + " " +
                   ic.getRuleNum2() + " " +
                   //LocaleKBCT.GetString(message);
                   LocaleKBCT.GetString("HaveIntersectionNoEmpty");
      else if (message.equals("IsIncludedIntoTheOneCoveredByTheRule"))
          warning= warning +
                   LocaleKBCT.GetString("TheInputSpaceCoveredByTheRule") + " " +
                   ic.getRuleNum1() + " " +
                   LocaleKBCT.GetString(message) + " " +
                   ic.getRuleNum2();

      ic.setError(warning);
      v_errors_REDUNDANCY_OR_SPECIFICITY_WARNINGS.add(ic);
   }
   this.V_REDUNDANCY_OR_SPECIFICITY_WARNINGS= v_errors_REDUNDANCY_OR_SPECIFICITY_WARNINGS;
   Vector<InfoConsistency> v_errors_UNUSUED_INPUTS_WARNINGS= new Vector<InfoConsistency>();
   while (this.UNUSUED_INPUTS_WARNINGS.hasMoreElements()) {
      InfoConsistency ic= (InfoConsistency)this.UNUSUED_INPUTS_WARNINGS.nextElement();
      int variable_number= ic.getVarNum();
      String warning= LocaleKBCT.GetString("TheVariable") + " '" +
                      this.kbct.GetInput(variable_number).GetName() + "' (" +
                      LocaleKBCT.GetString("Imp") + ": " +
                      LocaleKBCT.GetString(this.kbct.GetInput(variable_number).GetTrust()) + ") " +
                      LocaleKBCT.GetString("UsedByNoneRule");

      ic.setError(warning);
      v_errors_UNUSUED_INPUTS_WARNINGS.add(ic);
   }
   this.V_UNUSUED_INPUTS_WARNINGS= v_errors_UNUSUED_INPUTS_WARNINGS;
   Vector<InfoConsistency> v_errors_UNUSUED_INPUT_LABELS_WARNINGS= new Vector<InfoConsistency>();
   while (this.UNUSUED_INPUT_LABELS_WARNINGS.hasMoreElements()) {
      InfoConsistency ic= (InfoConsistency)this.UNUSUED_INPUT_LABELS_WARNINGS.nextElement();
      int variable_number= ic.getVarNum();
      //System.out.println("variable_number -> "+variable_number);
      String warning= "";
      if (this.kbct.GetInput(variable_number).GetScaleName().equals("user"))
         ic.setUser(true);

      if (ic.isUser()) {
          warning= LocaleKBCT.GetString("TheLabel") + " '" +
                   this.kbct.GetInput(variable_number).GetUserLabelsName(ic.getLabelNum()) + "' " +
                   LocaleKBCT.GetString("Of") + " " +
                   LocaleKBCT.GetString("TheVariable").toLowerCase() + " '" +
                   this.kbct.GetInput(variable_number).GetName() + "' (" +
                   LocaleKBCT.GetString("Imp") + ": " +
                   LocaleKBCT.GetString(this.kbct.GetInput(variable_number).GetTrust()) + ") " +
                   LocaleKBCT.GetString("UsedByNoneRule");
         ic.setLabelName(this.kbct.GetInput(variable_number).GetUserLabelsName(ic.getLabelNum()));
      } else {
          warning= LocaleKBCT.GetString("TheLabel") + " '" +
                   LocaleKBCT.GetString(this.kbct.GetInput(variable_number).GetLabelsName(ic.getLabelNum())) + "' " +
                   LocaleKBCT.GetString("Of") + " " +
                   LocaleKBCT.GetString("TheVariable").toLowerCase() + " '" +
                   this.kbct.GetInput(variable_number).GetName() + "' (" +
                   LocaleKBCT.GetString("Imp") + ": " +
                   LocaleKBCT.GetString(this.kbct.GetInput(variable_number).GetTrust()) + ") " +
                   LocaleKBCT.GetString("UsedByNoneRule");
          ic.setLabelName(LocaleKBCT.GetString(this.kbct.GetInput(variable_number).GetLabelsName(ic.getLabelNum())));
      }
      //System.out.println(" -> "+warning);
      ic.setError(warning);
      v_errors_UNUSUED_INPUT_LABELS_WARNINGS.add(ic);
   }
   this.V_UNUSUED_INPUT_LABELS_WARNINGS= v_errors_UNUSUED_INPUT_LABELS_WARNINGS;
   Vector<InfoConsistency> v_errors_UNUSUED_OUTPUTS_WARNINGS= new Vector<InfoConsistency>();
   while (this.UNUSUED_OUTPUTS_WARNINGS.hasMoreElements()) {
      InfoConsistency ic= (InfoConsistency)this.UNUSUED_OUTPUTS_WARNINGS.nextElement();
      String message= ic.getMessage1();
      int variable_number= ic.getVarNum();
      String warning= "";
      if (message.equals("IsNotDefined"))
          warning= LocaleKBCT.GetString("TheOutput") + " " +
                   variable_number + " (" +
                   LocaleKBCT.GetString("Imp") + ": " +
                   LocaleKBCT.GetString(this.kbct.GetOutput(variable_number).GetTrust()) + ") " +
                   LocaleKBCT.GetString("Of") + " " +
                   LocaleKBCT.GetString("TheRule").toLowerCase() + " " +
                   ic.getRuleNum1() + " " +
                   LocaleKBCT.GetString(message);
      else
          warning= LocaleKBCT.GetString("TheVariable") + " '" +
                   this.kbct.GetOutput(variable_number).GetName() + "' (" +
                   LocaleKBCT.GetString("Imp") + ": " +
                   LocaleKBCT.GetString(this.kbct.GetOutput(variable_number).GetTrust()) + ") " +
                   LocaleKBCT.GetString("UsedByNoneRule");

      ic.setError(warning);
      v_errors_UNUSUED_OUTPUTS_WARNINGS.add(ic);
   }
   this.V_UNUSUED_OUTPUTS_WARNINGS= v_errors_UNUSUED_OUTPUTS_WARNINGS;
   Vector<InfoConsistency> v_errors_UNUSUED_OUTPUT_LABELS_WARNINGS= new Vector<InfoConsistency>();
   while (this.UNUSUED_OUTPUT_LABELS_WARNINGS.hasMoreElements()) {
      InfoConsistency ic= (InfoConsistency)this.UNUSUED_OUTPUT_LABELS_WARNINGS.nextElement();
      int variable_number= ic.getVarNum();
      String warning= "";
      int label_number= ic.getLabelNum();
      if (this.kbct.GetOutput(variable_number).GetScaleName().equals("user"))
         ic.setUser(true);

      if (ic.isUser()) {
          warning= LocaleKBCT.GetString("TheLabel") + " '" +
                   this.kbct.GetOutput(variable_number).GetUserLabelsName(label_number) + "' " +
                   LocaleKBCT.GetString("Of") + " " +
                   LocaleKBCT.GetString("TheVariable").toLowerCase() + " '" +
                   this.kbct.GetOutput(variable_number).GetName() + "' (" +
                   LocaleKBCT.GetString("Imp") + ": " +
                   LocaleKBCT.GetString(this.kbct.GetOutput(variable_number).GetTrust()) + ") " +
                   LocaleKBCT.GetString("UsedByNoneRule");
         ic.setLabelName(this.kbct.GetOutput(variable_number).GetUserLabelsName(label_number));
      } else {
          warning= LocaleKBCT.GetString("TheLabel") + " '" +
                   LocaleKBCT.GetString(this.kbct.GetOutput(variable_number).GetLabelsName(label_number)) + "' " +
                   LocaleKBCT.GetString("Of") + " " +
                   LocaleKBCT.GetString("TheVariable").toLowerCase() + " '" +
                   this.kbct.GetOutput(variable_number).GetName() + "' (" +
                   LocaleKBCT.GetString("Imp") + ": " +
                   LocaleKBCT.GetString(this.kbct.GetOutput(variable_number).GetTrust()) + ") " +
                   LocaleKBCT.GetString("UsedByNoneRule");
          ic.setLabelName(LocaleKBCT.GetString(this.kbct.GetOutput(variable_number).GetLabelsName(label_number)));
      }
      ic.setError(warning);
      v_errors_UNUSUED_OUTPUT_LABELS_WARNINGS.add(ic);
   }
   this.V_UNUSUED_OUTPUT_LABELS_WARNINGS= v_errors_UNUSUED_OUTPUT_LABELS_WARNINGS;
   Vector[] result= new Vector[6];
   result[0]= this.V_CONSISTENCY_ERRORS;
   result[1]= this.V_REDUNDANCY_OR_SPECIFICITY_WARNINGS;
   result[2]= this.V_UNUSUED_INPUTS_WARNINGS;
   result[3]= this.V_UNUSUED_INPUT_LABELS_WARNINGS;
   result[4]= this.V_UNUSUED_OUTPUTS_WARNINGS;
   result[5]= this.V_UNUSUED_OUTPUT_LABELS_WARNINGS;
   return result;
  }
  //------------------------------------------------------------------------------
  /**
   * Counting number of rules per output class.
   */
  /*private int countRulesPerOutputClass(JKBCT kbctaux, int classLabel) {
	 int cont=0;
	 int lim= kbctaux.GetNbActiveRules();
	 for (int n=0; n<lim; n++) {
		  Rule r= kbctaux.GetRule(n+1);
		  int[] out= r.Get_out_labels_number();
		  if (out[0]==classLabel)
			  cont++;
	 }
	 if (cont>0)
		 return cont;
	 else 
		 return -1;
  }*/
  //------------------------------------------------------------------------------
  /**
   * Counting number of rules per output class.
   */
  /*private boolean isThereAtLeastOneRulesPerOutputClass(JKBCT kbctaux, int classLabel) {
	 int lim= kbctaux.GetNbActiveRules();
	 for (int n=0; n<lim; n++) {
		  Rule r= kbctaux.GetRule(n+1);
		  int[] out= r.Get_out_labels_number();
		  if (out[0]==classLabel)
			  return true;
	 }
     return false;
  }*/ 
  //------------------------------------------------------------------------------
  /**
   * Counting number of rules per output class.
   */
  private boolean isThereMoreThanOneRulePerOutputClass(JKBCT kbctaux, int classLabel) {
     boolean warning= false;
	 int lim= kbctaux.GetNbRules();
	 for (int n=0; n<lim; n++) {
		  Rule r= kbctaux.GetRule(n+1);
		  if (r.GetActive()) {
		      int[] out= r.Get_out_labels_number();
		      if (out[0]==classLabel) {
			      if (!warning)
				      warning= true;
			      else
			          return true;
		      }
		  }
	 }
     return false;
  }  
}