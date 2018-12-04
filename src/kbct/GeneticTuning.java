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
//                           GeneticTuning.java
//
//
//**********************************************************************

package kbct;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.util.Vector;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import kbctAux.MessageKBCT;
import kbctFrames.JKBCTFrame;
import KB.LabelKBCT;
import fis.JExtendedDataFile;

/**
 * Make a genetic tuning of the Data Base (Fuzzy Partitions), preserving SFP (Strong Fuzzy Partitions).
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
public class GeneticTuning extends GA {
  private double[] UoD;
  private int[] NbGenIniVariable;
  private int[] NbLabelsByInput;
  private Vector<int[]> NbMPbyLabel;
  private double[][] intervalos;
  private double MutProb;
  private double ParAlfa;
  private boolean BoundedOptimization= false;
  private int[] MaskVariables;
  private double epsilon= 0.001;
  private DecimalFormat df;
  private double[][] OLDpopulation;
  private double[][] NEWpopulation;
  private double[][] Hijos;
  
//------------------------------------------------------------------------------
  public GeneticTuning(JKBCT kbct, JExtendedDataFile DataFile, long[] exc, String LogName, boolean goOn) {
	  super(kbct,DataFile,exc,LogName,goOn);
	  this.df= new DecimalFormat();
	  this.df.setMaximumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
	  this.df.setMinimumFractionDigits(MainKBCT.getConfig().GetNumberOfDecimals());
	  DecimalFormatSymbols dfs= this.df.getDecimalFormatSymbols();
	  dfs.setDecimalSeparator((new String(".").charAt(0)));
	  this.df.setDecimalFormatSymbols(dfs);
	  this.df.setGroupingSize(20);
	  int NbInputs=this.kbct.GetNbInputs();
	  this.MaskVariables= new int[NbInputs+1];
	  for (int n=0; n<NbInputs; n++) {
		   String type= this.kbct.GetInput(n+1).GetType();
		   String trust= this.kbct.GetInput(n+1).GetTrust();
		   //System.out.println("trust="+trust);
		   if ( (!type.equals("numerical")) || (trust.equals("hhigh")) )
		       this.MaskVariables[n]=1;
	       else 
		       this.MaskVariables[n]=0;
	  }
      // output
	  String type= this.kbct.GetOutput(1).GetType();
	  String trust= this.kbct.GetOutput(1).GetTrust();
	  String classif= this.kbct.GetOutput(1).GetClassif();
	  //System.out.println("trust="+trust);
	  if ( (!type.equals("numerical")) || (trust.equals("hhigh")) || (classif.equals("yes")) )
	       this.MaskVariables[NbInputs]=1;
      else 
	       this.MaskVariables[NbInputs]=0;

	  //for (int n=0; n<NbInputs; n++) {
		//   if (this.MaskInputs[n]==1)
			//   System.out.println("Do not modify variable "+String.valueOf(n+1));
	  //}
    	  
      boolean warning= false;
      if (this.goOn) {
    	  File f= new File(this.fileLog);
    	  if (!f.exists()) {
    		  warning= true;
    	  } else
    	      this.readSavedConfiguration();
    	  
    	  f=null;
      }
      if (!warning) {
	      this.begin();
      } else {
    	  MessageKBCT.Error("fileLog does not exist", null);
      }
  }
//------------------------------------------------------------------------------
  private void begin() {
	  this.init();
	  boolean classifFlag= true;
      if (this.kbct.GetOutput(1).GetType().equals("numerical")) { 
          classifFlag= false;
      }
      if (!this.goOn) {
	      MessageKBCT.WriteLogFile("----------------------------------", "Optimization");
     	  //System.out.println(" -> htsize="+jnikbct.getHashtableSize());
	      OLDevaluation= this.evaluatePopulation("OLD",0);
     	  //System.out.println(" -> htsize="+jnikbct.getHashtableSize());
          //jnikbct.cleanHashtable(this.kbptr);
          jnikbct.cleanHashtable(this.kbptr,this.exc);
          //System.out.println(" -> htsize="+jnikbct.getHashtableSize());
          if (classifFlag) {   
              MessageKBCT.WriteLogFile("    "+LocaleKBCT.GetString("OldError")+" (1- ACCBT0) = "+OLDevaluation[0], "Optimization");
          } else {
              MessageKBCT.WriteLogFile("    "+LocaleKBCT.GetString("OldError")+" (RMSE) = "+OLDevaluation[0], "Optimization");
          }
      } else {
    	  this.exchange();
      }
      boolean end= false;
      int n=0;
      int lim= 6*this.NbGenes;
      if (this.milestoneGeneration > 0)
    	  lim= this.milestoneGeneration;
      else if (this.goOn)
    	  lim= 4*lim;
    	  
      System.out.println("init="+String.valueOf(this.initialGeneration+1));
      if (this.initialGeneration+lim > this.NbGenerations) {
          System.out.println("milestone="+this.NbGenerations);
          MessageKBCT.WriteLogFile("    "+LocaleKBCT.GetString("MilestoneGeneration")+"= "+this.NbGenerations, "Optimization");
      } else {
          System.out.println("milestone="+String.valueOf(this.initialGeneration+lim));
          MessageKBCT.WriteLogFile("    "+LocaleKBCT.GetString("MilestoneGeneration")+"= "+String.valueOf(this.initialGeneration+lim), "Optimization");
      }  
      for (n=this.initialGeneration; n<this.NbGenerations; n++) {
	       long TM=Runtime.getRuntime().totalMemory();
	       long MM=Runtime.getRuntime().maxMemory();
	       //long FM=Runtime.getRuntime().freeMemory();
    	   //System.out.println("TM="+TM);
    	   //System.out.println("MM="+MM);
    	   //System.out.println("FM="+FM);
	       if ( (TM>=MM) || (n > this.initialGeneration+lim-1) ) {
	    	   if (TM >=MM) {
	        	   System.out.println("TM="+TM);
	        	   System.out.println("MM="+MM);
	    	       System.out.println("Out of Memory");
                   if (!MainKBCT.getConfig().GetTESTautomatic()) {
	    	           String message= LocaleKBCT.GetString("WarningGeneticTuningHalted")+"\n"+
                       LocaleKBCT.GetString("WarningJVMOutOfMemory")+"\n"+
                       LocaleKBCT.GetString("WarningGeneticTuningSaveCurrentPopulation")+" "+this.fileLog+"."+"\n"+
                       LocaleKBCT.GetString("WarningReleaseMemory")+"\n"+
                       LocaleKBCT.GetString("WarningGeneticTuningJavaOutOfMemory1")+"\n"+
                       LocaleKBCT.GetString("WarningGeneticTuningJavaOutOfMemory2");
                       if (!MainKBCT.flagHalt) {
	    	               MessageKBCT.Information(null, message);
	    	               MainKBCT.flagHalt= true;
                       }
                   }
	    	   }
	    	   //System.out.println("Save current configuration");
	    	   MainKBCT.getConfig().SetInitialGeneration(n);
               this.saveCurrentConfiguration(n);
	    	   MainKBCT.getConfig().SetInitialGeneration(0);
               break;
	       } else {
	           System.out.println("Generation: "+String.valueOf(n+1));
    	       MessageKBCT.WriteLogFile("    "+LocaleKBCT.GetString("Generation")+"= "+String.valueOf(n+1), "Optimization");
	           /* Seleccion mediante el metodo de Baker */
	           //this.SelectBaker(this.OLDevaluation);
	           //System.out.println(">>Tournament");
	           this.SelectTournament();
	           /* Cruce */
	           //this.MaxMinCrossover();
	           //System.out.println(">>BLXalfaCrossover");
	           this.BLXalfaCrossover(n);
	           /* Mutacion */
	           //this.MutacionNoUniforme(n);
	           //this.MutacionAleatoria(n);
	           //System.out.println(">>RandomMutation");
	           this.RandomMutation();
	           /* Seleccion elitista */
	           //System.out.println(">>SetRanking");
	           this.setRanking();
	           //System.out.println(">>Elitism");
	           this.Elitist();
	           /* Evaluacion de los individuos de la poblacion actual */
	           //System.out.println(">>Evaluation");
	           NEWevaluation= this.evaluatePopulation("NEW",n);
	           this.exchange();
	           MessageKBCT.WriteLogFile("----------------------------------", "Optimization");
	           this.setRanking();
	           int bg=this.BestGuy();
	           if (NEWevaluation[bg-1]==1) {
                   end= true;
                   break;
	           }
          	   //System.out.println(" -> htsize="+jnikbct.getHashtableSize());
	           jnikbct.cleanHashtable(this.kbptr,this.exc);
          	   //System.out.println(" -> htsize="+jnikbct.getHashtableSize());
	       }
      }
      if ( (end) || (n==this.NbGenerations) ) {
     	MainKBCT.getConfig().SetInitialGeneration(LocaleKBCT.DefaultInitialGeneration());
        int bg= this.BestGuy();
        if (classifFlag) {   
            MessageKBCT.WriteLogFile("    "+LocaleKBCT.GetString("BestError")+" (1 - ACCBT0) = "+NEWevaluation[bg-1], "Optimization");
        } else {
            MessageKBCT.WriteLogFile("    "+LocaleKBCT.GetString("BestError")+" (RMSE) = "+NEWevaluation[bg-1], "Optimization");
        }
	    File tempkb= JKBCTFrame.BuildFile("temprboptGT.kb.xml");
        String kb_name= tempkb.getAbsolutePath();
        this.kbctTuning= new JKBCT(this.kbct);
        this.kbctTuning.SetKBCTFile(kb_name);
        try {
          this.kbctTuning=this.UpdateKBCT(this.kbct,"NEW",bg-1);
          this.kbctTuning.Save();
        } catch (Throwable t) {
        	t.printStackTrace();
    	    MessageKBCT.Error(null, t);
        }
        tempkb= null;
      }
  }
//------------------------------------------------------------------------------
  private void init() {
	  this.NbGenerations= MainKBCT.getConfig().GetNbGenerations();
      if (!this.goOn) {
        MessageKBCT.WriteLogFile(LocaleKBCT.GetString("NbGenerations")+"= "+this.NbGenerations, "Optimization");
      }
      this.PopLength= MainKBCT.getConfig().GetPopulationLength();
      if (!this.goOn) {
        MessageKBCT.WriteLogFile(LocaleKBCT.GetString("PopLength")+"= "+this.PopLength, "Optimization");
      }
	  this.TournamentSize= MainKBCT.getConfig().GetTournamentSize();
      if (!this.goOn) {
        MessageKBCT.WriteLogFile(LocaleKBCT.GetString("TournamentSize")+"= "+this.TournamentSize, "Optimization");
      }
      this.MutProb= MainKBCT.getConfig().GetMutationProb()/this.NbGenes;
      if (!this.goOn) {
	    MessageKBCT.WriteLogFile(LocaleKBCT.GetString("MutProb")+"= "+MainKBCT.getConfig().GetMutationProb(), "Optimization");
      }
	  this.CrossoverProb= MainKBCT.getConfig().GetCrossoverProb();
      if (!this.goOn) {
        MessageKBCT.WriteLogFile(LocaleKBCT.GetString("CrossoverProb")+"= "+this.CrossoverProb, "Optimization");
      }
	  this.ParAlfa= MainKBCT.getConfig().GetParAlfa();
      if (!this.goOn) {
        MessageKBCT.WriteLogFile(LocaleKBCT.GetString("ParAlfa")+"= "+this.ParAlfa, "Optimization");
      }

      this.BoundedOptimization= MainKBCT.getConfig().GetBoundedOptimization();
      String BoundedOpt= LocaleKBCT.GetString("No");
	  if (MainKBCT.getConfig().GetBoundedOptimization())
		  BoundedOpt= LocaleKBCT.GetString("Yes");
      if (!this.goOn) {
          MessageKBCT.WriteLogFile(LocaleKBCT.GetString("BoundedOptimization")+"= "+BoundedOpt, "Optimization");
      }
	  
	  int NbInputs= this.kbct.GetNbInputs();
	  if (!this.BoundedOptimization) {
	      this.UoD= new double[2*(NbInputs+1)];
	  }
	  this.NbGenIniVariable= new int[NbInputs+1];
	  this.NbLabelsByInput= new int[NbInputs+1];
	  this.NbMPbyLabel= new Vector<int[]>();
      Vector<Double> pob= new Vector<Double>();
      Vector<Double> interv= new Vector<Double>();
	  int contGen=0;
	  for (int n=0; n<NbInputs+1; n++) {
		  this.NbGenIniVariable[n]= contGen;
		  JVariable jvar;
		  if (n==NbInputs)
			  jvar= this.kbct.GetOutput(1);
		  else	  
		      jvar= this.kbct.GetInput(n+1);
		  
		  if (!this.BoundedOptimization) {
	          this.UoD[2*n]=jvar.GetInputInterestRange()[0]; 
	          this.UoD[2*n+1]=jvar.GetInputInterestRange()[1];
		  }
		  int NbLabels= jvar.GetLabelsNumber();
		  if (jvar.GetType().equals("numerical")) {
		    NbLabelsByInput[n]= NbLabels;
		    int[] mp= new int[NbLabels];
		    for (int k=0; k<NbLabels; k++) {
			   double[] par= jvar.GetLabel(k+1).GetParams();
			   int NbPar= par.length;
			   if (NbPar==4) {
				   mp[k]=2;
				   pob.add(new Double(par[1]));
				   double min1=0, max1=0;
				   if (this.BoundedOptimization) {
				       min1= (par[0]+par[1])/2;
				       max1= (par[1]+par[2])/2;
				   } /*else {
					   min1= par[0];
					   max1= par[2];
				   }*/
				   interv.add(new Double(this.df.format(min1)));
				   interv.add(new Double(this.df.format(max1)));
				   pob.add(new Double(par[2]));
				   double min2=0, max2=0;
				   if (this.BoundedOptimization) {
				       min2= (par[1]+par[2])/2;
				       max2= (par[2]+par[3])/2;
				   } /*else {
					   min2= par[1];
					   max2= par[3];
				   }*/
				   interv.add(new Double(this.df.format(min2)));
				   interv.add(new Double(this.df.format(max2)));
				   contGen=contGen+2;
			   } else {
				   mp[k]=1;
				   contGen++;
				   if (NbPar==3) {
				       pob.add(new Double(par[1]));
				       double min=0, max=0;
					   if (this.BoundedOptimization) {
					       min= (par[0]+par[1])/2;
					       max= (par[1]+par[2])/2;
					   } /*else {
					       min= par[0];
					       max= par[2];
					   }*/
					   interv.add(new Double(this.df.format(min)));
					   interv.add(new Double(this.df.format(max)));
				   } 
   		       }
 	        }
		    NbMPbyLabel.add(mp);
	      } else {
			  NbLabelsByInput[n]= NbLabels;
			  int[] mp= new int[NbLabels];
			  for (int k=0; k<NbLabels; k++) {
				   mp[k]=0;
			  }
		      NbMPbyLabel.add(mp);
	      }
		  jvar=null;
	  }
      Object[] objPop= pob.toArray();
      double[] initPob= new double[objPop.length];
      for (int n=0; n<objPop.length; n++) {
    	  initPob[n]= ((Double)objPop[n]).doubleValue();
      }
      objPop=null;
	  if (this.BoundedOptimization) {
          Object[] objInterv= interv.toArray();
          intervalos= new double[this.PopLength][objInterv.length];
          for (int m=0; m<this.PopLength; m++) {
              for (int n=0; n<objInterv.length; n++) {
    	           intervalos[m][n]= ((Double)objInterv[n]).doubleValue();
              }
          }
          objInterv= null;
	  }
	  this.NbGenes= initPob.length;
      if (!this.goOn) {
        MessageKBCT.WriteLogFile(LocaleKBCT.GetString("NbGenes")+"= "+this.NbGenes, "Optimization");
      }
      if (!this.goOn) {
        OLDpopulation= new double[this.PopLength][this.NbGenes];
	    for (int n=0; n<this.PopLength; n++) {
		  for (int k=0; k<this.NbGenes; k++) {
			  if (n==0) {
				  OLDpopulation[n][k]= initPob[k];
			  } else {
				  double min, max;
				  if (this.BoundedOptimization) {
					  min= intervalos[n][2*k];
					  max= intervalos[n][2*k+1];
				  } else {
				      int input= this.getInputNumber(k);
				      min= this.UoD[2*input];
				      max= this.UoD[2*input+1];
				      if (!this.isBeginVariable(k))
					      min= OLDpopulation[n][k-1];
						  
				      if (!this.isEndVariable(k))
					      max= OLDpopulation[0][k+1];
				  }
				  OLDpopulation[n][k]= (new Double(this.df.format(min + (max-min)*Rand()))).doubleValue();
     		  }
		  }
	    }
	    NEWpopulation= new double[this.PopLength][this.NbGenes];
	    for (int n=0; n<this.PopLength; n++) {
		  for (int k=0; k<this.NbGenes; k++) {
			  NEWpopulation[n][k]=OLDpopulation[n][k];
		  }
	    }
	    NEWevalIndex= new int[this.PopLength];
	    OLDevalIndex= new int[this.PopLength];
	    NEWevaluation= new double[this.PopLength];
	    OLDevaluation= new double[this.PopLength];
	    for (int n=0; n<this.PopLength; n++) {
		  NEWevalIndex[n]=0;
		  OLDevalIndex[n]=0;
		  NEWevaluation[n]=0;
		  OLDevaluation[n]=0;
	    }
    }
	Torneo= new int[this.TournamentSize];
	sample= new int[this.PopLength];
	this.Hijos= new double[2][this.NbGenes];
  }
//------------------------------------------------------------------------------
  protected void exchange() {
      for (int n=0; n<this.PopLength; n++) {
    	  OLDevaluation[n]= NEWevaluation[n];
    	  OLDevalIndex[n]= NEWevalIndex[n];
          for (int m=0; m<this.NbGenes; m++) {
        	  OLDpopulation[n][m]= NEWpopulation[n][m];
          }
      }
  }
//------------------------------------------------------------------------------
  private double[] evaluatePopulation(String pop, int ngen) {
	  int lim;
	  if (pop.equals("NEW"))
	      lim= NEWpopulation.length;
	  else if (pop.equals("OLD"))
	      lim= OLDpopulation.length;
	  else
		  lim= this.Hijos.length;

	  double[] result= new double[lim];
	  for (int n=0; n<result.length; n++) {
		  if ( ( (pop.equals("NEW")) && ( (NEWevalIndex==null) || ( (NEWevalIndex != null) && (NEWevalIndex[n]==0) ) ) ) ||
			   ( (pop.equals("OLD")) && ( (OLDevalIndex==null) || ( (OLDevalIndex != null) && (OLDevalIndex[n]==0) ) ) ) ||
			   (pop.equals("CROSSOVER")) ) {
	        	//System.out.println("n="+n);
	        	//System.out.println(" -> htsize="+jnikbct.getHashtableSize());
		        File tempkb= JKBCTFrame.BuildFile("temprboptGT"+ngen+"."+n+".kb.xml");
				try {
		            if (!tempkb.exists())
						tempkb.createNewFile();
				} catch (IOException e) {
					System.out.print("Error creating temporal file");
					e.printStackTrace();
				}
	            String kb_name= tempkb.getAbsolutePath();
                JKBCT kbctaux= new JKBCT(this.kbct);
                kbctaux.SetKBCTFile(kb_name);
	       	    //long ptr1= kbctaux.GetPtr();
                if (kbctaux.GetPtr()==-1) {
                    System.out.println("ERROR CREATING KB: "+kbname);                	
                }
          	    if (pop.equals("NEW"))
          	      kbctaux= this.UpdateKBCT(kbctaux,"NEW",n);
          	    else if (pop.equals("OLD"))
            	  kbctaux= this.UpdateKBCT(kbctaux,"OLD",n);
        	    else
            	  kbctaux= this.UpdateKBCT(kbctaux,"CROSSOVER",n);
          	    
           	    //long ptr2= kbctaux.GetPtr();
        	    //System.out.println(" -> ptr1="+ptr1+"  ptr2="+ptr2);
        	    //for (long i=ptr1; i<ptr2; i++) {
        		  //   jnikbct.DeleteKBCT(i);
        	    //}
        	    //System.out.println(" -> htsize="+jnikbct.getHashtableSize());
	            JFIS jfis= null;
	            File tempfis= null;
	            try {
                    kbctaux.Save();
		            tempfis= JKBCTFrame.BuildFile("temprboptGT.fis");
	                String fis_name= tempfis.getAbsolutePath();
	                kbctaux.SaveFIS(fis_name);
	                jfis= new JFIS(fis_name);
	            } catch (Throwable t) {
	            	System.out.println("-> FIS ERROR: Discarding generated KB and trying again...");
	            	//t.printStackTrace();
	        	    //MessageKBCT.Error(null, t);
	            }
	            if (jfis!=null) {
	                result[n]= this.FISerr(jfis, kbctaux);
		            jfis.Close();
	            } else {
	            	result[n]= 2;
	            }
	            if (pop.equals("NEW"))
	                NEWevalIndex[n]=1;
	            else if (pop.equals("OLD"))
	                OLDevalIndex[n]=1;

	            kbctaux.Close();
	            kbctaux.Delete();
	            jfis=null;
	            kbctaux=null;
	            tempkb=null;
	            tempfis=null;
	            tempkb=null;
	    } else {
	    	result[n]=NEWevaluation[n];
	    }
	  }
	  return result;
  }
//------------------------------------------------------------------------------
  private JKBCT UpdateKBCT(JKBCT kbctnew, String popul, int m) {
      int cont=0;
	  for (int n=0; n<kbctnew.GetNbInputs(); n++) {
		  JKBCTInput in= kbctnew.GetInput(n+1);
		  if (in.GetType().equals("numerical")) {
		      int NbLabels= this.NbLabelsByInput[n];
		      if (NbLabels > 0) {
		          Object[] obj= this.NbMPbyLabel.toArray();
		          //System.out.println("n="+n);
		          //System.out.println("obj.length="+obj.length);
		          int[] mps= (int[])obj[n];
		          int NbMP=0;
		          for (int k=0; k<NbLabels; k++) {
			           NbMP= NbMP+mps[k];
		          }
		          double[] mp= new double[NbMP];
		          for (int k=0; k<mp.length; k++) {
                       if (popul.equals("NEW"))
			               mp[k]= NEWpopulation[m][cont];
                       else if (popul.equals("OLD"))
			               mp[k]= OLDpopulation[m][cont];
                       else
			               mp[k]= this.Hijos[m][cont];
            	  
                       // rounding value to avoid conflicts because it is rounded when saved in kb file
                       mp[k]= (new Double(this.df.format(mp[k]))).doubleValue();
			           cont++;
		          }
        		  //System.out.println("warning: input -> "+String.valueOf(n+1));
		          //for (int k=0; k<mp.length; k++) {
		    	      // System.out.println("mp[k]="+mp[k]);
		          //}
		          int pos=0;
		          for (int k=0; k<NbLabels; k++) {
   			           LabelKBCT lab= in.GetLabel(k+1);
                       if (k==0) {
            	           if (mp[pos]!=mp[pos+1]) {
        	                   lab.SetP2(mp[pos]);
    	                       lab.SetP3(mp[pos+1]);
            	           } else {
             		           //System.out.println("warning: input -> "+String.valueOf(n+1));
            		           //System.out.println("warning: label -> "+String.valueOf(k+1));
        	                   lab.SetP2(mp[pos]-this.epsilon);
    	                       lab.SetP3(mp[pos]+this.epsilon);
            	           }
                       } else if (k==NbLabels-1) {
            	           if (mp[pos]!=mp[pos-1]) {
        	                   lab.SetP1(mp[pos-1]);
    	                       lab.SetP2(mp[pos]);
            	           } else {
            		           //System.out.println("warning: input -> "+String.valueOf(n+1));
            		           //System.out.println("warning: label -> "+String.valueOf(k+1));
        	                   lab.SetP2(mp[pos-1]-this.epsilon);
    	                       lab.SetP3(mp[pos-1]+this.epsilon);
            	           }
                       } else {
            	           if ( (mp[pos-1]!=mp[pos]) && (mp[pos]!=mp[pos+1]) ) {
        	                     lab.SetP1(mp[pos-1]);
    	                         lab.SetP2(mp[pos]);
    	                         lab.SetP3(mp[pos+1]);
            	           } else {
            		             if ( (mp[pos-1]==mp[pos]) && (mp[pos]==mp[pos+1]) ) {
                		               //System.out.println("warning: input -> "+String.valueOf(n+1));
                		               //System.out.println("warning: label -> "+String.valueOf(k+1));
            	                       lab.SetP1(mp[pos]-this.epsilon);
        	                           lab.SetP2(mp[pos]);
        	                           lab.SetP3(mp[pos]+this.epsilon);
            		             } else if (mp[pos-1]==mp[pos]) {
                		               //System.out.println("warning: input -> "+String.valueOf(n+1));
                		               //System.out.println("warning: label -> "+String.valueOf(k+1));
            	                       lab.SetP1(mp[pos-1]-this.epsilon);
        	                           lab.SetP2(mp[pos-1]+this.epsilon);
            		             } else if (mp[pos]==mp[pos+1]) {
                		               //System.out.println("warning: input -> "+String.valueOf(n+1));
                		               //System.out.println("warning: label -> "+String.valueOf(k+1));
            	                       lab.SetP2(mp[pos]-this.epsilon);
        	                           lab.SetP3(mp[pos]+this.epsilon);
            		             }
            	            }
              }
        	  if (mps[k]==2) {
        	      lab.SetP4(mp[pos+2]);
        	      pos++;
        	  }
              pos++;
			  in.ReplaceLabel(k+1,lab);
		    }
		    kbctnew.ReplaceInput(n+2, in);
	      }
		}
	  }
	  return kbctnew;
  }
//------------------------------------------------------------------------------
  private void BLXalfaCrossover(int ngen) {
	   int mom, dad, ultimo;
	   int last = 1;
	   if (firstflag) {
	     last= (int) (this.PopLength * this.CrossoverProb);
	  	 firstflag=false;
	   }
       for (mom=ultimo=0; mom<last; mom+=2) {
	     dad=mom+1;
	  	 ultimo++;
	     // Se obtienen dos hijos
	  	 this.Hijos= this.BLXalfa(dad, mom);
	     // Se evaluan los dos descendientes 
	  	 double[] eval= this.evaluatePopulation("CROSSOVER",ngen);
	     // Se actualiza el fitness de los descendientes
	     NEWevaluation[mom]= eval[0];
	     NEWevaluation[dad]= eval[1];
	     NEWevalIndex[mom]=1;
	     NEWevalIndex[dad]=1;
	   }
  }
//------------------------------------------------------------------------------
  private double[][] BLXalfa(int dad, int mom) {
      double[] Padre= NEWpopulation[dad];
      double[] Madre= NEWpopulation[mom];
	  double[][] H= new double[2][this.NbGenes];
	  double temp, I;
	  for (int n=0; n<this.NbGenes; n++) {
	    double puntox= Madre[n];
	    double puntoy= Padre[n];
	    int input= this.getInputNumber(n);
	    if (this.MaskVariables[input]==0) {
	        if (puntox>puntoy) {
	            temp=puntox;
	            puntox=puntoy;
	            puntoy=temp;
	        }
	        I=puntoy-puntox;
	        puntox=puntox-I*this.ParAlfa;
	        puntoy=puntoy+I*this.ParAlfa;
		    if (this.BoundedOptimization) {
			    double puntoxm= puntox;
		        if (puntoxm<intervalos[mom][2*n])
		            puntoxm=intervalos[mom][2*n];

		        double puntoym= puntoy;
		        if (puntoym>intervalos[mom][2*n+1])
		            puntoym=intervalos[mom][2*n+1];

		        H[0][n]= puntoxm + (puntoym-puntoxm)*Rand();

		        double puntoxd= puntox;
	            if (puntoxd<intervalos[dad][2*n])
		            puntoxd=intervalos[dad][2*n];

		        double puntoyd= puntoy;
		        if (puntoyd>intervalos[dad][2*n+1])
		            puntoyd=intervalos[dad][2*n+1];

		        H[1][n]= puntoxd + (puntoyd-puntoxd)*Rand();
		    } else {
			    double rinf= this.UoD[2*input];
			    double rsup= this.UoD[2*input+1];
			    double min= rinf;
			    boolean ini= this.isBeginVariable(n);
			    if (!ini)
			        min= NEWpopulation[mom][n-1];
					  
	            double max= rsup;
			    boolean fin= this.isEndVariable(n);
			    if (!fin)
			        max= NEWpopulation[mom][n+1];

		        double puntoxm= puntox;
		        if (puntoxm<min)
		            puntoxm=min;

		        double puntoym= puntoy;
		        if (puntoym>max)
		            puntoym=max;

		        H[0][n]= puntoxm + (puntoym-puntoxm)*Rand();
			    min= rinf;
			    max= rsup;
			    if (!ini)
			        min= NEWpopulation[dad][n-1];
					  
			    if (!fin)
			        max= NEWpopulation[dad][n+1];

		        double puntoxd= puntox;
		        if (puntoxd<min)
		            puntoxd=min;

		        double puntoyd= puntoy;
		        if (puntoyd>max)
		            puntoyd=max;

		        H[1][n]= puntoxd + (puntoyd-puntoxd)*Rand();
          }
	    } else {
	    	H[0][n]= puntox;
	    	H[1][n]= puntoy;
	    }
   	    NEWpopulation[mom][n]=H[0][n];
      	NEWpopulation[dad][n]=H[1][n];
      }
	  return H;
  }
//------------------------------------------------------------------------------
  /*private void MaxMinCrossover() {
   int mom, dad, ultimo, i;
   double temp1, temp2;
   int last = 1;
   double[][] C= new double[4][this.NbGenes];
      
   if (firstflag) {
     last= (int) (this.PopLength * this.CrossoverProb);
  	 firstflag=false;
   }

   for (mom=ultimo=0; mom<last; mom+=2) {
     dad=mom+1;
  	 ultimo++;

  	 for (i=0; i<this.NbGenes; i++) {
       temp1=this.NEWpopulation[mom][i];
       temp2=this.NEWpopulation[dad][i];
       // Se obtienen los cuatro sucesores: uno aplicando la t-norma,
       // uno la t-conorma
       // y dos de la funcion promedio
       C[0][i]=gtuning.T_producto_logico((float)temp1,(float)temp2);
       C[1][i]=gtuning.S_suma_logica((float)temp1,(float)temp2);
       C[2][i]=gtuning.Promedio1((float)temp1,(float)temp2,(float)this.ParA);
       C[3][i]=gtuning.Promedio1((float)temp1,(float)temp2,(float)(1.0-this.ParA));
     }

     // Se evaluan los cuatro descendientes y se seleccionan los dos mejores 
  	 double[] eval= this.evaluatePopulation(C, null);
  	 int[] order= this.order(eval);
     //System.out.println("The best are "+String.valueOf(order[0]-1)+" and "+String.valueOf(order[1]-1));
     for (int m=0; m<this.NbGenes; m++) {
      	 this.NEWpopulation[mom][m]=C[order[0]-1][m];
         this.NEWpopulation[dad][m]=C[order[1]-1][m];
     }  	 
     // Se actualiza el fitness de los descendientes
     this.NEWevaluation[mom]= eval[order[0]-1];
     this.NEWevaluation[dad]= eval[order[1]-1];
     this.NEWevalIndex[mom]=1;
     this.NEWevalIndex[dad]=1;
     }
  }*/
//------------------------------------------------------------------------------
  private void RandomMutation() {
	  this.Mu_next=0;
	  int posiciones=this.NbGenes*this.PopLength;

	  if (this.MutProb>0)
	   while (Mu_next<posiciones) {
	     /* Se determina el cromosoma y el gen que corresponden a la posicion que
	        se va a mutar */
	     int i=Mu_next/this.NbGenes;
	 	 int j=Mu_next%this.NbGenes;
		 int input= this.getInputNumber(j);
		 //System.out.println("input="+input);
         if (this.MaskVariables[input]==0) {
	         /* Se efectua la mutacion sobre ese gen */
             // Mutacion aleatoria
	 	     double min, max;
	 	     if (this.BoundedOptimization) {
			     min= intervalos[i][2*j];
			     max= intervalos[i][2*j+1];
		     } else {
			     min= this.UoD[2*input];
			     max= this.UoD[2*input+1];
			     if (!this.isBeginVariable(j))
			         min= NEWpopulation[i][j-1];
						  
		         if (!this.isEndVariable(j))
				     max= NEWpopulation[i][j+1];
		     }
	 	     double nval= min + (max-min)*Rand();
	         NEWpopulation[i][j]=nval;
		     NEWevalIndex[i]=0;
         }
         /* Se calcula la siguiente posicion a mutar */
 	     if (this.MutProb<1) {
             double m=Rand();
             Mu_next+= Math.ceil(Math.log(m)/Math.log(1.0-this.MutProb));
         } else {
 	         Mu_next+=1;
         }
	  }
  }
//------------------------------------------------------------------------------
  /*private void MutacionNoUniforme(int Gen) {
	  int posiciones, i, j;
	  double nval, m;
	  this.Mu_next=0;
	  posiciones=this.NbGenes*this.PopLength;

	  if (this.MutProb>0)
	   while (Mu_next<posiciones) {
	     // Se determina el cromosoma y el gen que corresponden a la posicion que
	     //   se va a mutar
	     i=Mu_next/this.NbGenes;
	 	 j=Mu_next%this.NbGenes;
   	     //System.out.println("... Mu_next="+Mu_next+"  posiciones="+posiciones);
   	     //System.out.println("... i="+i+"  j="+j);

	     // Se efectua la mutacion sobre ese gen
	     // delta = Mutacion no uniforme de Michalewicz
	      if (Rand()<0.5) {
	          nval= this.NEWpopulation[i][j] + this.delta(Gen,(intervalos[2*j+1] - this.NEWpopulation[i][j]));
	     } else {
	          nval= this.NEWpopulation[i][j] - this.delta(Gen,(this.NEWpopulation[i][j] - intervalos[2*j]));
	     }
	 	 
	 	 //System.out.println("... nval="+nval);
	     this.NEWpopulation[i][j]=nval;
	     this.NEWevalIndex[i]=0;

	     // Se calcula la siguiente posicion a mutar
	 	 if (this.MutProb<1) {
	       m=Rand();
	       Mu_next+= Math.ceil(Math.log(m)/Math.log(1.0-this.MutProb));
	     } else {
	 	  Mu_next+=1;
	     }
	  }
	  //Mu_next-=posiciones;
  }*/
//------------------------------------------------------------------------------
  /*
   * Mutacion no uniforme de Michalewicz
   */
  /*private double delta(int t, double y) {
      double result=0;
	  double r=this.Rand();
	  double sub=1.0 - t/this.NbGenerations;
	  double potencia= Math.pow(sub,this.ParB);
	  double subtotal= Math.pow(r,potencia);
	  result= (y * (1.0-subtotal));
	  return result;
  }*/
//------------------------------------------------------------------------------
  protected void saveCurrentConfiguration(int NbGen) {
    try {
      FileOutputStream fos= new FileOutputStream(this.fileLog, false);
	  PrintStream out= new PrintStream(fos);
	  out.println("Generation="+NbGen);
	  out.println("NbGenes="+this.NbGenes);
	  out.println("PopLength="+this.PopLength);
	  for (int n=0; n<this.PopLength; n++) {
		  out.println("Population "+n);
		  out.println("eval="+this.NEWevaluation[n]);
		  out.println("evalIndex="+this.NEWevalIndex[n]);
		  for (int m=0; m<this.NbGenes; m++) {
			  out.println(this.NEWpopulation[n][m]);
		  }		  
	  }
	  out.println("Best="+this.NEWevaluation[this.BestGuy()-1]);
      out.flush();
	  out.close();
	  fos.close();
	  out=null;
	  fos=null;
    } catch (Throwable t) {
    	t.printStackTrace();
    	MessageKBCT.Error(null, t);
    }
  }
//------------------------------------------------------------------------------
  private void readSavedConfiguration() {
    try {
        LineNumberReader lnr= new LineNumberReader(new InputStreamReader(new FileInputStream(this.fileLog)));
        String l, aux;
        l=lnr.readLine();
        aux= l.substring(11);
        this.initialGeneration= (new Integer(aux)).intValue();
        l=lnr.readLine();
        aux= l.substring(8);
        this.NbGenes= (new Integer(aux)).intValue();
        l=lnr.readLine();
        aux= l.substring(10);
        this.PopLength= (new Integer(aux)).intValue();
        NEWpopulation= new double[this.PopLength][this.NbGenes];
	    OLDpopulation= new double[this.PopLength][this.NbGenes];
	    NEWevaluation= new double[this.PopLength];
	    OLDevaluation= new double[this.PopLength];
        NEWevalIndex= new int[this.PopLength];
	    OLDevalIndex= new int[this.PopLength];
	    for (int n=0; n<this.PopLength; n++) {
	        lnr.readLine();
	        l=lnr.readLine();
	        aux= l.substring(5);
	        double ev= (new Double(aux)).doubleValue();
	        NEWevaluation[n]= ev;
	        l=lnr.readLine();
	        aux= l.substring(10);
	        int evi= (new Integer(aux)).intValue();
	        NEWevalIndex[n]= evi;
		    for (int m=0; m<this.NbGenes; m++) {
		        l=lnr.readLine();
		        double gen= (new Double(l)).doubleValue();
		    	this.NEWpopulation[n][m]=gen;
		    }		  
	    }
	    lnr.close();
	    lnr=null;
    } catch (Throwable t) {
    	t.printStackTrace();
    	MessageKBCT.Error(null, t);
    }
  }
//------------------------------------------------------------------------------
  private boolean isBeginVariable(int NbGen) {
	  for (int n=0; n<this.NbGenIniVariable.length; n++) {
		  if (NbGen==this.NbGenIniVariable[n])
			  return true;
	  }
	  return false;
  }
//------------------------------------------------------------------------------
  private boolean isEndVariable(int NbGen) {
	  for (int n=0; n<this.NbGenIniVariable.length; n++) {
		  if (NbGen==this.NbGenIniVariable[n]-1)
			  return true;
	  }
	  if (NbGen==this.NbGenes-1)
		  return true;
	  else
	      return false;
  }
//------------------------------------------------------------------------------
  private int getInputNumber(int NbGen) {
	  for (int n=0; n<this.NbGenIniVariable.length; n++) {
		  if (NbGen < this.NbGenIniVariable[n])
			  return n-1;
	  }
	  return this.NbGenIniVariable.length-1;
  }
//------------------------------------------------------------------------------
  protected void Elitist() {
	  // Se estudia a ver si el mejor cromosoma de la poblacion anterior ha sido
	  //   seleccionado para formar parte de la nueva
      this.BestGuy= this.BestGuy();
      boolean warning=false;
	  for (int i=0; i<this.PopLength; i++) {
		 boolean found=true;
	     for (int k=0; k<this.NbGenes; k++) {
	    	 if (NEWpopulation[i][k]!=OLDpopulation[BestGuy-1][k]) {
	    		 found= false;
	    		 break;
	    	 }		 
	     }
	     if (found) {
             warning=true;
	    	 break;
	     } 
	  }
	  // Si el mejor cromosoma no ha perdurado, se sustituye el ultimo de la
	  //   poblacion por este.
	  if (!warning) {
        for (int m=0; m<this.NbGenes; m++) {
		  NEWpopulation[this.PopLength-1][m]=OLDpopulation[BestGuy-1][m];
        }
        NEWevaluation[this.PopLength-1]=OLDevaluation[BestGuy-1];
        NEWevalIndex[this.PopLength-1]=1;
	  }
  }
//------------------------------------------------------------------------------
  /**
   * Selection based on tournament.
   */
  protected void SelectTournament() {
	  // Torneo.length -> tournament size
	  // if (tournament size == 2) then BinaryTournament
       this.setRanking();
	   
	   for (int i=0; i<this.PopLength; i++) {
	     Torneo[0] = Randint(0,this.PopLength-1);
	     int mejor_torneo=Torneo[0];
	     
	     for (int j=1; j<Torneo.length; j++) {
	       boolean repetido= false;
	       do {
	         Torneo[j] = Randint(0,this.PopLength-1);
	         repetido=false;
	         int k=0;
	         while ((k<j) && (!repetido))
	          if (Torneo[j]==Torneo[k])
	           repetido=true;
	          else
	           k++;
	       } while (repetido);
	       
	       if (OLDevaluation[Torneo[j]] < OLDevaluation[mejor_torneo])
	           mejor_torneo=Torneo[j];
	     }
	     sample[i] = mejor_torneo;
	   }
	   // Se crea la nueva poblacion
	   for (int n=0; n<this.PopLength; n++) {
	          int kk=sample[n];
	          for (int m=0; m<this.NbGenes; m++) {
	               NEWpopulation[n][m]= OLDpopulation[kk][m];
	          }
	          NEWevaluation[n]= OLDevaluation[kk];
	          NEWevalIndex[n]= OLDevalIndex[kk];
	   }
  }
}