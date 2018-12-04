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
//                           JSolveProblem.java
//
//
//**********************************************************************

package kbct;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import kbctAux.MessageKBCT;
import kbctFrames.JConsistencyFrame;
import kbctFrames.JExpertFrame;
import kbctFrames.JKBCTFrame;
import KB.LabelKBCT;
import fis.JExtendedDataFile;

/**
 * Solve consistency problems of the Knowledge Base.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

public class JSolveProblem {
  private JKBCT kbct;
  private JExpertFrame jef;
  private JConsistencyFrame jcf;
  private Enumeration CONSISTENCY_ERRORS;
  private Enumeration REDUNDANCY_OR_SPECIFICITY_WARNINGS;
  private Enumeration UNUSUED_INPUTS_WARNINGS;
  private Enumeration UNUSUED_INPUT_LABELS_WARNINGS;
  private Enumeration UNUSUED_OUTPUTS_WARNINGS;
  private Enumeration UNUSUED_OUTPUT_LABELS_WARNINGS;
  private Vector V_CONSISTENCY_ERRORS;
  private Vector V_REDUNDANCY_OR_SPECIFICITY_WARNINGS;
  private Vector V_UNUSUED_INPUTS_WARNINGS;
  private Vector V_UNUSUED_INPUT_LABELS_WARNINGS;
  private Vector V_UNUSUED_OUTPUTS_WARNINGS;
  private Vector V_UNUSUED_OUTPUT_LABELS_WARNINGS;
  private Vector<InfoConsistency> Solutions1;
  private Vector<InfoConsistency> Solutions2;
  private Vector<InfoConsistency> Solutions3;
  private Vector<InfoConsistency> Solutions4;
  private JExtendedDataFile jedf;

  public JSolveProblem(JKBCT kbct, JExpertFrame jef, JConsistencyFrame jcf) {
    this.kbct= kbct;
    this.jef= jef;
    this.jcf= jcf;
    this.jedf= this.jef.getJExtDataFile();
  }

  //------------------------------------------------------------------------------
  public void SolveProblem1(InfoConsistency ic) {
     String sol= ic.getSolution();
     sol= sol+"\n"+LocaleKBCT.GetString("DoYouWantToContinue");
     int Remove= ic.getRuleNumToRemove();
     if (Remove==1) {
       int option= 0;
       if (this.jedf != null) {
           if (this.jcf != null)
               option= MessageKBCT.SolveConclusionNotDefined(this.jcf, sol);
           else
               option= MessageKBCT.SolveConclusionNotDefined(this.jef, sol);
         } else {
           if (this.jcf != null)
             MessageKBCT.Information(this.jcf, sol);
           else
             MessageKBCT.Information(this.jef, sol);

           option= -1;
         }
         if (option==0) {
             int res= this.CompleteRule(ic.getRuleNum1());
             if (res==0) {
            	 String msg= LocaleKBCT.GetString("ThereIsNoSuitableConclusionThenRemoveRule1")+"\n"+
            	             LocaleKBCT.GetString("ThereIsNoSuitableConclusionThenRemoveRule2");
                 if (this.jcf != null)
                     MessageKBCT.Information(this.jcf, msg);
                   else
                     MessageKBCT.Information(this.jef, msg);

                 this.RemoveRule(ic.getRuleNum1());
             }
         } else if (option==1)
             this.RemoveRule(ic.getRuleNum1());
       
     } else if (Remove==2) {
         if (ic.getMessage1().equals("SamePremiseDifferentConclussions")) {
           int option= 0;
           if (this.jedf != null) {
             if (this.jcf != null)
                 option= MessageKBCT.SolveSamePremiseDifferentConclusions(this.jcf, sol);
             else
                 option= MessageKBCT.SolveSamePremiseDifferentConclusions(this.jef, sol);
           } else {
             if (this.jcf != null)
               MessageKBCT.Information(this.jcf, sol);
             else
               MessageKBCT.Information(this.jef, sol);

             option= -1;
           }
           if (option==0) {
               int[] SelRules= new int[2];
               SelRules[0]= ic.getRuleNum1()-1;
               SelRules[1]= ic.getRuleNum2()-1;
               this.SolveConflictExpandingRules(SelRules, this.jedf, false, false, false);
           }
         } else {
           int ruleNum1= ic.getRuleNum1();
           int ruleNum2= ic.getRuleNum2();
           int option= 0;
           if (this.jcf != null)
               option= MessageKBCT.RemoveRule2(this.jcf, sol, ruleNum1, ruleNum2);
           else
               option= MessageKBCT.RemoveRule2(this.jef, sol, ruleNum1, ruleNum2);

           if (option==0)
               this.RemoveRule(ruleNum1);
           else if (option==1)
               this.RemoveRule(ruleNum2);
         }
     }
  }
  //------------------------------------------------------------------------------
  public void SolveProblem2(InfoConsistency ic) {
     String sol= ic.getSolution();
     int Remove= ic.getRuleNumToRemove();
     int ruleNum1= ic.getRuleNum1();
     int ruleNum2= ic.getRuleNum2();
     if (Remove==4) {
    	 //System.out.println("SolveProblem2: Remove4");
         int option= 0;
         if (this.jedf != null) {
             if (this.jcf != null)
                 option= MessageKBCT.SolveNoEmptyIntersectionSameConclusions(this.jcf, sol+"\n"+LocaleKBCT.GetString("DoYouWantToContinue"));
             else
                 option= MessageKBCT.SolveNoEmptyIntersectionSameConclusions(this.jef, sol+"\n"+LocaleKBCT.GetString("DoYouWantToContinue"));
           } else {
             if (this.jcf != null)
               MessageKBCT.Information(this.jcf, sol);
             else
               MessageKBCT.Information(this.jef, sol);

             option= -1;
           }
           if (option==0) {
             int[] SelRules= new int[2];
             SelRules[0]= ruleNum1-1;
             SelRules[1]= ruleNum2-1;
             this.SolveConflictExpandingRules(SelRules, this.jedf, true, false, true);
           }
     } else if (Remove==3) {
    	 //System.out.println("SolveProblem2: Remove3");
         int option= 2;
         if (this.jedf != null) {
           if (this.jcf != null)
               option= MessageKBCT.SolveInclusionDifferentConclusions(this.jcf, sol+"\n"+LocaleKBCT.GetString("DoYouWantToContinue"), ruleNum1, ruleNum2);
           else
               option= MessageKBCT.SolveInclusionDifferentConclusions(this.jef, sol+"\n"+LocaleKBCT.GetString("DoYouWantToContinue"), ruleNum1, ruleNum2);
         } else {
           if (this.jcf != null)
             MessageKBCT.Information(this.jcf, sol);
           else
             MessageKBCT.Information(this.jef, sol);

           option= -1;
         }
         if (option==0)
             this.RemoveRule(ruleNum1);
         else if (option==1)
             this.RemoveRule(ruleNum2);
         else if (option==2) {
             int[] SelRules= new int[2];
             SelRules[0]= ruleNum1-1;
             SelRules[1]= ruleNum2-1;
             this.SolveConflictExpandingRules(SelRules, this.jedf, true, false, false);
         }
     } else if (Remove==2) {
    	 //System.out.println("SolveProblem2: Remove2");
         int option= 0;
         if (this.jcf != null)
             option= MessageKBCT.RemoveRule2(this.jcf, sol+"\n"+LocaleKBCT.GetString("DoYouWantToContinue"), ruleNum1, ruleNum2);
         else
             option= MessageKBCT.RemoveRule2(this.jef, sol+"\n"+LocaleKBCT.GetString("DoYouWantToContinue"), ruleNum1, ruleNum2);

         if (option==0)
             this.RemoveRule(ruleNum1);
         else if (option==1)
             this.RemoveRule(ruleNum2);

     } else if (Remove==1) {
    	 //System.out.println("SolveProblem2: Remove1");
         int option= 0;
         boolean commonPart=false;
         if (ic.getMessage1().equals("HaveIntersectionNoEmptyCommomPart"))
        	 commonPart=true;
         
         if (this.jedf != null) {
           if (this.jcf != null)
               option= MessageKBCT.SolveNoEmptyIntersectionDifferentConclusions(this.jcf, sol+"\n"+LocaleKBCT.GetString("DoYouWantToContinue"));
           else
               option= MessageKBCT.SolveNoEmptyIntersectionDifferentConclusions(this.jef, sol+"\n"+LocaleKBCT.GetString("DoYouWantToContinue"));
         } else {
           if (this.jcf != null)
             MessageKBCT.Information(this.jcf, sol);
           else
             MessageKBCT.Information(this.jef, sol);

           option= -1;
         }
         if (option==0) {
           int[] SelRules= new int[2];
           SelRules[0]= ruleNum1-1;
           SelRules[1]= ruleNum2-1;
      	   //System.out.println("SolveProblem2: SolveConflictExpandingRules");
           this.SolveConflictExpandingRules(SelRules, this.jedf, true, commonPart, false);
         }
     } else if (Remove==0) {
    	 //System.out.println("SolveProblem2: Remove0");
         if (this.jcf != null)
             MessageKBCT.Information(this.jcf, sol);
         else
             MessageKBCT.Information(this.jef, sol);
     }
  }
//------------------------------------------------------------------------------
  public int CompleteRule(int r) {
        int result=0;
	    int option= 0;
	    String msg=LocaleKBCT.GetString("ThisOperationWillComplete")+" "+LocaleKBCT.GetString("Rule")+" "+r+" "+LocaleKBCT.GetString("FromKB");
	    if (this.jcf != null)
	        option= MessageKBCT.Confirm(this.jcf, msg, 0, false, false, false);
	    else
	        option= MessageKBCT.Confirm(this.jef, msg, 0, false, false, false);

	    if (option==0) {
	      result= this.jef.CompleteRule(r);
	      if (this.jcf!=null) {
	        this.jcf.dispose();
	        JKBCTFrame.RemoveTranslatable(this.jcf);
	        this.jcf.ReLoad();
	      }
	    }
	    return result;
  }
  //------------------------------------------------------------------------------
  void RemoveRule(int r) {
    int option= 0;
    String msg=LocaleKBCT.GetString("ThisOperationWillRemove")+" "+LocaleKBCT.GetString("Rule")+" "+r+" "+LocaleKBCT.GetString("FromKB");
    if (this.jcf != null)
        option= MessageKBCT.Confirm(this.jcf, msg, 0, false, false, false);
    else
        option= MessageKBCT.Confirm(this.jef, msg, 0, false, false, false);

    if (option==0) {
      this.jef.RemoveRule(r);
      if (this.jcf!=null) {
        this.jcf.dispose();
        JKBCTFrame.RemoveTranslatable(this.jcf);
        this.jcf.ReLoad();
      }
    }
  }
  //------------------------------------------------------------------------------
  public void SolveConflictExpandingRules(int[] SelectedRules, JExtendedDataFile jedf, boolean expand, boolean commonPart, boolean split) {
    try {
      File temp1= JKBCTFrame.BuildFile("tempExpRules1.kb.xml");
      JKBCT kbctaux1= new JKBCT(this.jef.getKBCTTemp());
      long ptr1= kbctaux1.GetPtr();
      kbctaux1.SetKBCTFile(temp1.getAbsolutePath());
      File temp2= JKBCTFrame.BuildFile("tempExpRules2.kb.xml");
      JKBCT kbctaux2= new JKBCT(this.jef.getKBCTTemp());
      long ptr2= kbctaux2.GetPtr();
      kbctaux2.SetKBCTFile(temp2.getAbsolutePath());
      kbctaux1.Save();
      kbctaux2.Save();
      //JConsistency jc= new JConsistency(this.jef, kbctaux1);
      JConsistency jc= new JConsistency(kbctaux1);
      File temp3= JKBCTFrame.BuildFile("tempExpRules3.kb.xml");
      File temp4= JKBCTFrame.BuildFile("tempExpRules3.fis");
      kbctaux2= jc.solveConflict(kbctaux1, kbctaux2, SelectedRules, temp3, temp4, jedf, expand, commonPart, split);
      int NbRules= this.jef.getKBCTTemp().GetNbRules();
      for (int n=0; n<NbRules; n++) {
        this.jef.getKBCTTemp().RemoveRule(0);
      }
      int NbNewRules= kbctaux2.GetNbRules();
      for (int n=0; n<NbNewRules; n++) {
        this.jef.getKBCTTemp().AddRule(kbctaux2.GetRule(n+1));
      }
      this.jef.ReInitTableRules();
      if (this.jcf != null) {
        this.jcf.dispose();
        JKBCTFrame.RemoveTranslatable(this.jcf);
        this.jcf.ReLoad();
      }
      kbctaux1.Close();
      kbctaux1.Delete();
      jnikbct.DeleteKBCT(ptr1+1);
      kbctaux2.Close();
      kbctaux2.Delete();
      jnikbct.DeleteKBCT(ptr2+1);
   } catch (Throwable t) {
      //t.printStackTrace();
      MessageKBCT.Error(null, t);
   }
 }
  //------------------------------------------------------------------------------
  public void SolveProblem3and4(InfoConsistency ic) {
     int NumLabel= ic.getLabelNum();
     if (NumLabel==-1)
       this.SolveProblemVariable(ic, false, 0);
     else
       this.SolveProblemLabel(ic, false, 0);
  }
  //------------------------------------------------------------------------------
  public String SolveProblemVariable(InfoConsistency ic, boolean ReduceDataBase, int mode) {
     //System.out.println("SolveProblemVariable 1");
     String message= null;
     String sol= ic.getSolution();
     if (ic.isRemoveVar()) {
       //System.out.println("SolveProblemVariable 2.1.1");
       int option=0;
       if (mode==0) {
           sol= sol+"\n"+LocaleKBCT.GetString("DoYouWantToContinue");
           if (this.jcf != null)
             option= MessageKBCT.ReduceDataBase(this.jcf, sol, false);
           else
             option= MessageKBCT.ReduceDataBase(this.jef, sol, false);
       }
       if (option==0) {
         //System.out.println("SolveProblemVariable 2.1.2");
         int varNum= ic.getVarNum();
         String VariableType= ic.getVarType();
         if (VariableType.equals("Input"))
           message= LocaleKBCT.GetString("TheVariable").toLowerCase() + " '" +
                    this.kbct.GetInput(varNum).GetName() + "' (" +
                    LocaleKBCT.GetString("Imp") + ": " +
                    LocaleKBCT.GetString(this.kbct.GetInput(varNum).GetTrust()) + ") " +
                    LocaleKBCT.GetString("FromKB");
         else if (VariableType.equals("Output"))
           message= LocaleKBCT.GetString("TheVariable").toLowerCase() + " '" +
                    this.kbct.GetOutput(varNum).GetName() + "' (" +
                    LocaleKBCT.GetString("Imp") + ": " +
                    LocaleKBCT.GetString(this.kbct.GetOutput(varNum).GetTrust()) + ") " +
                    LocaleKBCT.GetString("FromKB");

         if (mode==0) {
             message= LocaleKBCT.GetString("ThisOperationWillRemove")+" "+message;
             if (this.jcf != null)
               option= MessageKBCT.Confirm(this.jcf, message+"\n"+LocaleKBCT.GetString("DoYouWantToContinue"), 0, false, false, false);
             else
               option= MessageKBCT.Confirm(this.jef, message+"\n"+LocaleKBCT.GetString("DoYouWantToContinue"), 0, false, false, false);
         } else {
        	 if (!this.jef.getKBCTTemp().GetInput(varNum).GetTrust().equals("hhigh"))
                 message= LocaleKBCT.GetString("Removed")+" "+message;
        	 else
                 message= message+" -> "+LocaleKBCT.GetString("UnalterableVariableMsg");
         }
         //System.out.println("SolveProblemVariable 2.1.3");
         if (option==0) {
             //System.out.println("SolveProblemVariable 2.1.4");
        	 if (!this.jef.getKBCTTemp().GetInput(varNum).GetTrust().equals("hhigh")) {
                 if (VariableType.equals("Input"))
                     this.jef.RemoveInput(varNum, mode);
                 else if (VariableType.equals("Output"))
                          this.jef.RemoveOutput(varNum, mode);

                 if (this.jcf != null) {
                     //System.out.println("SolveProblemVariable 2.1.5");
                     this.jcf.dispose();
                     JKBCTFrame.RemoveTranslatable(this.jcf);
                     if (!ReduceDataBase)
                         this.jcf.ReLoad();
                 }
        	 }
         }
       }
     } else if (mode==0) {
         //System.out.println("SolveProblemVariable 2.2");
         if (this.jcf != null)
           MessageKBCT.Information(this.jcf, sol);
         else
           MessageKBCT.Information(this.jef, sol);
     }
     //System.out.println("SolveProblemVariable 3");
     return message;
  }
  //------------------------------------------------------------------------------
  public String SolveProblemLabel(InfoConsistency ic, boolean ReduceDataBase, int mode) {
    String message= null;
    String sol= ic.getSolution();
    int VarNum= ic.getVarNum();
    String VarType= ic.getVarType();
    int option= -1;
    if ( ( (VarType.equals("Input")) && (this.jef.getKBCTTemp().GetInput(VarNum).GetLabelsNumber() >= 3) ) ||
         ( (VarType.equals("Output")) && (this.jef.getKBCTTemp().GetOutput(VarNum).GetLabelsNumber() >= 3) ) ) {
         if (mode == 0) {
            sol = sol + "\n" + LocaleKBCT.GetString("DoYouWantToContinue");
            if (this.jcf != null)
              option = MessageKBCT.ReduceDataBase(this.jcf, sol, false);
            else
              option = MessageKBCT.ReduceDataBase(this.jef, sol, false);

         } else
              option= 0;
    }
    if (option==-1) {
        if (mode==0) {
          if (this.jcf != null)
            MessageKBCT.Information(this.jcf,LocaleKBCT.GetString("MinNumberOfLabels"));
          else
            MessageKBCT.Information(this.jef,LocaleKBCT.GetString("MinNumberOfLabels"));
        }
    } else if (option==0) {
      int LabNum= ic.getLabelNum();
      if (VarType.equals("Input")) {
        message= LocaleKBCT.GetString("TheLabel").toLowerCase() + " '" +
                 ic.getLabelName() + "' " +
                 LocaleKBCT.GetString("Of") + " " +
                 LocaleKBCT.GetString("TheVariable").toLowerCase() + " '" +
                 this.kbct.GetInput(VarNum).GetName() + "' (" +
                 LocaleKBCT.GetString("Imp") + ": " +
                 LocaleKBCT.GetString(this.kbct.GetInput(VarNum).GetTrust()) + ") " +
                 LocaleKBCT.GetString("FromKB");
      } else if (VarType.equals("Output")) {
        message= LocaleKBCT.GetString("TheLabel").toLowerCase() + " '" +
                 ic.getLabelName() + "' " +
                 LocaleKBCT.GetString("Of") + " " +
                 LocaleKBCT.GetString("TheVariable").toLowerCase() + " '" +
                 this.kbct.GetOutput(VarNum).GetName() + "' (" +
                 LocaleKBCT.GetString("Imp") + ": " +
                 LocaleKBCT.GetString(this.kbct.GetOutput(VarNum).GetTrust()) + ") " +
                 LocaleKBCT.GetString("FromKB");
      }
      if (mode==0) {
        message= LocaleKBCT.GetString("ThisOperationWillRemove")+" "+message;
        if (this.jcf != null)
          option= MessageKBCT.Confirm(this.jcf, message+"\n"+LocaleKBCT.GetString("DoYouWantToContinue"), 0, false, false, false);
        else
          option= MessageKBCT.Confirm(this.jef, message+"\n"+LocaleKBCT.GetString("DoYouWantToContinue"), 0, false, false, false);

      } else {
          if (!this.jef.getKBCTTemp().GetInput(VarNum).GetTrust().equals("hhigh"))
    	      message= LocaleKBCT.GetString("Removed")+" "+message;
   	      else
              message= message+" -> "+LocaleKBCT.GetString("UnalterableVariableMsg");
      }
      if (option==0) {
          if (!this.jef.getKBCTTemp().GetInput(VarNum).GetTrust().equals("hhigh")) {
    	      if (VarType.equals("Input")) {
    	    	  //System.out.println("VarNum="+VarNum+"   LabNum="+LabNum);
                  this.jef.getKBCTTemp().RemoveMFInInput(VarNum, LabNum);
    	      } else if (VarType.equals("Output")) {
                  this.jef.getKBCTTemp().RemoveMFInOutput(VarNum, LabNum);
              }
              if (this.jcf != null) {
                  this.jcf.dispose();
                  JKBCTFrame.RemoveTranslatable(this.jcf);
                  if (!ReduceDataBase)
                      this.jcf.ReLoad();
              }
          }
      }
    }
    return message;
  }
  //------------------------------------------------------------------------------
  public String SolveProblemLabelsGroup(InfoConsistency ic, int mode) {
    // La parte comentada era para agrupar etiquetas en salidas
    String message= null;
    int option=0;
    if (mode==0) {
      String sol= ic.getSolution();
      sol= sol+"\n"+LocaleKBCT.GetString("DoYouWantToContinue");
      if (this.jcf != null)
        option= MessageKBCT.ReduceDataBase(this.jcf, sol, true);
      else
        option= MessageKBCT.ReduceDataBase(this.jef, sol, true);

    } else {
      message= LocaleKBCT.GetString("MergedLabels")+" ";
    }
    if (option==0) {
      //String var_type= ic.getVarType();
      int var_num= ic.getVarNum();
      int[] GroupLabels= ic.getGroupLabels();
      JVariable Temp= null;
      //if (var_type.equals("Input"))
          Temp= this.jef.getKBCTTemp().GetInput(var_num);
      //else if (var_type.equals("Output"))
          //Temp= this.jef.Temp_kbct.GetOutput(var_num);
      int NbLabels= Temp.GetLabelsNumber();
      LabelKBCT e_ini= Temp.GetLabel(GroupLabels[0]);
      LabelKBCT e_fin= Temp.GetLabel(GroupLabels[GroupLabels.length-1]);
      if (Temp.GetType().equals("numerical")) {
          LabelKBCT e_group= JConvert.ORLabel(e_ini, e_fin, e_ini.GetLabel_Number(), NbLabels);
          Temp.ReplaceLabel(e_ini.GetLabel_Number(), e_group);
      }
      String ScaleName= Temp.GetScaleName();
      String[] LabelsNames;
      if (ScaleName.equals("user"))
        LabelsNames= Temp.GetUserLabelsName();
      else
        LabelsNames= Temp.GetLabelsName();

      String e_group_Name;
      if (ScaleName.equals("user")) {
          e_group_Name=LabelsNames[GroupLabels[0]-1];
          message= message + LabelsNames[GroupLabels[0]-1];
      } else {
          e_group_Name=LocaleKBCT.GetString(LabelsNames[GroupLabels[0]-1]);
          message= message + LocaleKBCT.GetString(LabelsNames[GroupLabels[0]-1]);
      }
      for (int n=1; n<GroupLabels.length; n++) {
        if (ScaleName.equals("user")) {
          e_group_Name= e_group_Name + " "+LocaleKBCT.GetString("OR")+" "+ LabelsNames[GroupLabels[n]-1];
          message= message + ", "+ LabelsNames[GroupLabels[n]-1];
        } else {
          e_group_Name= e_group_Name + " "+LocaleKBCT.GetString("OR")+" "+ LocaleKBCT.GetString(LabelsNames[GroupLabels[n]-1]);
          message= message + ", "+ LocaleKBCT.GetString(LabelsNames[GroupLabels[n]-1]);
        }
      }
      message= message+ " ("+Temp.GetName()+")";
      for (int n=1; n<=NbLabels; n++) {
        if (n==e_ini.GetLabel_Number())
          Temp.SetUserLabelsName(n, e_group_Name);
        else {
          if (ScaleName.equals("user"))
            Temp.SetUserLabelsName(n, LabelsNames[n-1]);
          else
            Temp.SetUserLabelsName(n, LocaleKBCT.GetString(LabelsNames[n-1]));
        }
      }
      Temp.SetScaleName("user");
      Temp.SetLabelsName();
      Temp.SetORLabelsName();
      //if (var_type.equals("Input"))
        this.jef.getKBCTTemp().ReplaceInput(var_num+1, Temp);
      //else if (var_type.equals("Output"))
        //this.jef.Temp_kbct.ReplaceOutput(var_num+1, Temp);
      for (int n= 1; n<GroupLabels.length; n++) {
          Temp.RemoveLabel(GroupLabels[1]-1, false);
          Temp.SetORLabelsName();
          //if (var_type.equals("Input")) {
            Object[] listeners = this.kbct.GetListeners().toArray();
            for (int i = 0; i < listeners.length; i++)
              ( (KBCTListener) listeners[i]).MFRemovedInInput(var_num, GroupLabels[1]-1);

            this.kbct.ReplaceInput(var_num+1, Temp);
          //}
          /*else if (var_type.equals("Output")) {
            Object[] listeners = this.kbct.GetListeners().toArray();
            for (int i = 0; i < listeners.length; i++)
              ( (KBCTListener) listeners[i]).MFRemovedInOutput(var_num, GroupLabels[1]-1);

            this.kbct.ReplaceOutput(var_num+1, Temp);
          }*/
      }
    }
    return message;
  }
  //------------------------------------------------------------------------------
  public void setKBCT(JKBCT kbct) { this.kbct= kbct; }
  //------------------------------------------------------------------------------
  public void setJConsistency(JConsistency jc) {
    jc.TranslateVectorsToEnumeration();
    Vector[] vv= jc.generateErrorMessages();
    this.V_CONSISTENCY_ERRORS= vv[0];
    this.V_REDUNDANCY_OR_SPECIFICITY_WARNINGS= vv[1];
    this.V_UNUSUED_INPUTS_WARNINGS= vv[2];
    this.V_UNUSUED_INPUT_LABELS_WARNINGS= vv[3];
    this.V_UNUSUED_OUTPUTS_WARNINGS= vv[4];
    this.V_UNUSUED_OUTPUT_LABELS_WARNINGS= vv[5];
    this.TranslateVectorsToEnumeration();
    this.generateSolutionMessages();
  }
  //------------------------------------------------------------------------------
  public void generateSolutionMessages() {
    this.Solutions1= new Vector<InfoConsistency>();
    while (this.CONSISTENCY_ERRORS.hasMoreElements()) {
      InfoConsistency ic= (InfoConsistency)this.CONSISTENCY_ERRORS.nextElement();
      String message= ic.getMessage1();
      String sol="";
      if (message.equals("SamePremiseDifferentConclussions")) {
          sol= LocaleKBCT.GetString("TwoOptions")+":"+"\n"
                 +"1. - "+LocaleKBCT.GetString("SelectConclusion")+"\n"
                 +"2. - "+LocaleKBCT.GetString("ChangePremises");
          ic.setRuleNumToRemove(2);
      } else if (message.equals("SamePremiseSameConclussions")) {
                 sol= LocaleKBCT.GetString("RemoveOneRuleSamePremiseDifferentConclusion");
                 ic.setRuleNumToRemove(2);
      } else if (message.equals("IsIncomplete_ConclusionNotDefined")) {
                 sol=LocaleKBCT.GetString("TwoOptions")+":"+"\n"
                     +"1. - "+LocaleKBCT.GetString("DefineConclusion")+" "+LocaleKBCT.GetString("Rule").toLowerCase()+"\n"
                     +"2. - "+LocaleKBCT.GetString("RemoveThe")+" "+LocaleKBCT.GetString("Rule").toLowerCase();
                 ic.setRuleNumToRemove(1);
      }
      ic.setSolution(sol);
      this.Solutions1.add(ic);
    }
    this.Solutions2= new Vector<InfoConsistency>();
    while (this.REDUNDANCY_OR_SPECIFICITY_WARNINGS.hasMoreElements()) {
      InfoConsistency ic= (InfoConsistency)this.REDUNDANCY_OR_SPECIFICITY_WARNINGS.nextElement();
      String message1= ic.getMessage1();
      String message2= ic.getMessage2();
      String sol="";
      if ( (message1.equals("HaveIntersectionNoEmpty")) || (message1.equals("HaveIntersectionNoEmptyCommomPart")) ) {
        if (message2.equals("SameConclusions")) {
            //sol= LocaleKBCT.GetString("SupportEffect");
            sol=LocaleKBCT.GetString("TwoOptions")+":"+"\n"
            +"1. - "+LocaleKBCT.GetString("SupportEffect")+"\n"
            +"2. - "+LocaleKBCT.GetString("ChangeInputSpaceTwoRule");
            ic.setRuleNumToRemove(4);
        } else if (message2.equals("DifferentConclusions")) {
            sol=LocaleKBCT.GetString("ThreeOptions")+":"+"\n"
                +"1. - "+LocaleKBCT.GetString("DoNothing")+" "+LocaleKBCT.GetString("DependsOnInferenceSystem")+"\n"
                +"2. - "+LocaleKBCT.GetString("ChangeInputSpaceOneRule")+"\n"
                +"3. - "+LocaleKBCT.GetString("ChangeInputSpaceTwoRule");
            ic.setRuleNumToRemove(1);
        }
      } else if (message1.equals("IsIncludedIntoTheOneCoveredByTheRule")) {
        if (message2.equals("SameConclusions")) {
            sol= LocaleKBCT.GetString("RemoveOneRule")+" (rule "+ic.getRuleNum1()+").";
            ic.setRuleNumToRemove(2);
        } else if (message2.equals("DifferentConclusions")) {
             sol=LocaleKBCT.GetString("TwoOptions")+":"+"\n"
                 +"1. - "+LocaleKBCT.GetString("RemoveOneRule")+" (rule "+ic.getRuleNum1()+")."+"\n"
                 +"2. - "+LocaleKBCT.GetString("ChangeInputSpaceTwoRule");
             ic.setRuleNumToRemove(3);
        }
      }
      ic.setSolution(sol);
      this.Solutions2.add(ic);
    }
    this.Solutions3= new Vector<InfoConsistency>();
    while (this.UNUSUED_INPUTS_WARNINGS.hasMoreElements()) {
      InfoConsistency ic= (InfoConsistency)this.UNUSUED_INPUTS_WARNINGS.nextElement();
      String sol=LocaleKBCT.GetString("TwoOptions")+":"+"\n"
          +"1. - "+LocaleKBCT.GetString("RemoveThe")+" "+LocaleKBCT.GetString("Variable").toLowerCase()+"\n"
          +"2. - "+LocaleKBCT.GetString("UseVariable");
      ic.setVarType("Input");
      ic.setRemoveVar(true);
      ic.setLabelNum(-1);
      ic.setSolution(sol);
      this.Solutions3.add(ic);
    }
    while(this.UNUSUED_INPUT_LABELS_WARNINGS.hasMoreElements()) {
      InfoConsistency ic= (InfoConsistency)this.UNUSUED_INPUT_LABELS_WARNINGS.nextElement();
      String sol=LocaleKBCT.GetString("TwoOptions")+":"+"\n"
          +"1. - "+LocaleKBCT.GetString("RemoveThe")+" "+LocaleKBCT.GetString("Label").toLowerCase()+"\n"
          +"2. - "+LocaleKBCT.GetString("UseLabel");
      ic.setSolution(sol);
      ic.setVarType("Input");
      //System.out.println("JSolveProblem -> "+ic.getVarNum()+"  "+ic.getError());
      this.Solutions3.add(ic);
    }
    this.Solutions4= new Vector<InfoConsistency>();
    while (this.UNUSUED_OUTPUTS_WARNINGS.hasMoreElements()) {
      InfoConsistency ic= (InfoConsistency)this.UNUSUED_OUTPUTS_WARNINGS.nextElement();
      String message= ic.getMessage1();
      String sol="";
      ic.setVarType("Output");
      if (message.equals("IsNotDefined")) {
          sol=LocaleKBCT.GetString("DefineConclusion")+" "+LocaleKBCT.GetString("Rule").toLowerCase()+" "+ic.getRuleNum1();
          ic.setRemoveVar(false);
      } else {
          sol=LocaleKBCT.GetString("TwoOptions")+":"+"\n"
              +"1. - "+LocaleKBCT.GetString("RemoveThe")+" "+LocaleKBCT.GetString("Variable").toLowerCase()+"\n"
              +"2. - "+LocaleKBCT.GetString("UseVariable");
          ic.setRemoveVar(true);
      }
      ic.setLabelNum(-1);
      ic.setSolution(sol);
      this.Solutions4.add(ic);
    }
    while (this.UNUSUED_OUTPUT_LABELS_WARNINGS.hasMoreElements()) {
      InfoConsistency ic= (InfoConsistency)this.UNUSUED_OUTPUT_LABELS_WARNINGS.nextElement();
      String sol=LocaleKBCT.GetString("TwoOptions")+":"+"\n"
          +"1. - "+LocaleKBCT.GetString("RemoveThe")+" "+LocaleKBCT.GetString("Label").toLowerCase()+"\n"
          +"2. - "+LocaleKBCT.GetString("UseLabel");
      ic.setSolution(sol);
      ic.setVarType("Output");
      this.Solutions4.add(ic);
    }
  }
  //------------------------------------------------------------------------------
  public void TranslateVectorsToEnumeration() {
    this.CONSISTENCY_ERRORS= this.V_CONSISTENCY_ERRORS.elements();
    this.REDUNDANCY_OR_SPECIFICITY_WARNINGS= this.V_REDUNDANCY_OR_SPECIFICITY_WARNINGS.elements();
    this.UNUSUED_INPUTS_WARNINGS= this.V_UNUSUED_INPUTS_WARNINGS.elements();
    this.UNUSUED_INPUT_LABELS_WARNINGS= this.V_UNUSUED_INPUT_LABELS_WARNINGS.elements();
    this.UNUSUED_OUTPUTS_WARNINGS= this.V_UNUSUED_OUTPUTS_WARNINGS.elements();
    this.UNUSUED_OUTPUT_LABELS_WARNINGS= this.V_UNUSUED_OUTPUT_LABELS_WARNINGS.elements();
  }
  //------------------------------------------------------------------------------
  public Vector getSolutions(int Number) {
    switch (Number) {
      case 1: return this.Solutions1;
      case 2: return this.Solutions2;
      case 3: return this.Solutions3;
      case 4: return this.Solutions4;
    }
    return null;
  }
  //------------------------------------------------------------------------------
  public JExpertFrame getJEF() { return this.jef; }
}
