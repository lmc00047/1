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
//                              variable.java
//
//
//**********************************************************************

package KB;

import java.util.Hashtable;
import java.util.Vector;

import kbct.LocaleKBCT;
import kbct.jnikbct;

/**
 * Define variables: Name, Type, Trust, Classif, Range, Labels
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
public class variable {
  private String Name, Type, Trust, Classif;
  private double LowerPhysicalRange, UpperPhysicalRange;
  private double LowerInterestRange, UpperInterestRange;
  private int LabelsNumber;
  private String LabelName;
  private String[] LabelsName;
  private String[] UserLabel;
  private Hashtable<String,LabelKBCT> Labels;
  private boolean active;
  private String[] OrNames;
  private boolean FlagModify;
  private boolean FlagOntology;
  private String nameOntology;
  private boolean FlagOntDatatypeProperty;
  private boolean FlagOntObjectProperty;
  private Vector<String> LabelsNamesSaveFISquality;
  private String[] SmallLarge= {"ultra_small", "very_small", "small", "average_small", "average", "average_large", "large", "very_large", "ultra_large"};
  private String[] FewLot= {"ultra_few", "very_few", "few", "average_few", "average", "average_lot", "lot", "very_lot", "ultra_lot"};
  private String[] NegativePositive= {"ultra_negative", "very_negative", "negative", "zero_negative", "zero", "zero_positive", "positive", "very_positive", "ultra_positive"};
  private String[] LowHigh= {"ultra_low", "very_low", "low", "average_low", "average", "average_high", "high", "very_high", "ultra_high"};
  //private Vector vclass;
  //private double[] classNames; 
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Generate a new variable. </p>
   * <p align="left">
   * Default values:
   * <ul>
   *   <li> Name: "" </li>
   *   <li> Type: categorical </li>
   *   <li> Trust: low </li>
   *   <li> Classif: no </li>
   *   <li> Physical range: [1 , 3] </li>
   *   <li> Interest range: [1 , 3] </li>
   *   <li> Number of labels: 3 </li>
   *   <li> Type of linguistic labels: low-high </li>
   *   <li> Labels definition: regular partition </li>
   * </ul>
   * </p>
   */
  public variable() {
    Name= "";
    Type= "numerical";
    Trust= "llow";
    Classif="yes";
    LowerPhysicalRange= 0;
    UpperPhysicalRange= 1;
    LowerInterestRange= 0;
    UpperInterestRange= 1;
    LabelsNumber= 3;
    LabelName= "low-high";
    //classNames= new double[3];
    //for (int n=1; n<=3; n++) {
    	// classNames[n]= n;
    //}
    this.InitLabelsName(this.LabelsNumber);
    this.SetLabelsName();
    Labels= new Hashtable<String,LabelKBCT>();
    this.SetLabelProperties();
    this.active= true;
    this.FlagModify= false;
    this.FlagOntology= false;
    this.FlagOntDatatypeProperty= false;
    this.FlagOntObjectProperty= false;
  }
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Generate a new variable. </p>
   * <p align="left">
   * Values:
   * <ul>
   *   <li> Name: name </li>
   *   <li> Type: type </li>
   *   <li> Trust: trust </li>
   *   <li> Classif: classif </li>
   *   <li> Physical range: [lpr , upr] </li>
   *   <li> Interest range: [lir , uir] </li>
   *   <li> Number of labels: ln </li>
   *   <li> Type of linguistic labels: label_name </li>
   *   <li> Labels definition: no definition </li>
   * </ul>
   * </p>
   * @param name name of the variable
   * @param type numerical, categorical or logical
   * @param trust low, middle or high
   * @param classif yes or no
   * @param lpr lower physical range
   * @param upr upper physical range
   * @param lir lower interest range
   * @param uir upper interest range
   * @param ln number of labels
   * @param label_name scale of labels
   * @param Active flag of variable activation
   * @param FM flag of variable modification
   */
  public variable(String name, String type, String trust, String classif, double lpr, double upr, double lir, double uir, int ln, String label_name, boolean Active, boolean FM) {
    Name= name;
    Type= type;
    Trust= trust;
    Classif= classif;
    LowerPhysicalRange= lpr;
    UpperPhysicalRange= upr;
    LowerInterestRange= lir;
    UpperInterestRange= uir;
    LabelsNumber= ln;
    LabelName= label_name;
    Labels= new Hashtable<String,LabelKBCT>();
    this.active= Active;
    this.FlagModify= FM;
    //this.FlagOntology= false;
  }
  //----------------------------------------------------------------------------
  /**
   * Set variable name "n".
   * @param n name of the variable
   */
  public void SetName(String n) { Name=n; }
  //----------------------------------------------------------------------------
  /**
   * Return the name of this variable.
   * @return name of the variable
   */
  public String GetName() { return Name; }
  //----------------------------------------------------------------------------
  /**
   * Set variable type "t".
   * @param t numerical, categorical or logical
   */
  public void SetType(String t) { Type=t; }
  //----------------------------------------------------------------------------
  /**
   * Return the type of this variable.
   * @return numerical, categorical or logical
   */
  public String GetType() { return Type; }
  //----------------------------------------------------------------------------
  /**
   * Set variable trust "t".
   * @param t low, middle or high
   */
  public void SetTrust(String t) { Trust=t; }
  //----------------------------------------------------------------------------
  /**
   * Return the trust of this variable.
   * @return low, middle or high
   */
  public String GetTrust() { return Trust; }
  //----------------------------------------------------------------------------
  /**
   * Set input physical range [domain_inf, domain_sup].
   * @param domain_inf lower physical range
   * @param domain_sup upper physical range
   */
  public void SetInputPhysicalRange(double domain_inf, double domain_sup) {
    SetLowerPhysicalRange(domain_inf);
    SetUpperPhysicalRange(domain_sup);
  }
  //----------------------------------------------------------------------------
  /**
   * Return input physical range of this variable.<br>
   * [lower physical range, upper physical range].
   * @return physical range
   */
  public double[] GetInputPhysicalRange() {
    double[] pr= new double[2];
    pr[0]= GetLowerPhysicalRange();
    pr[1]= GetUpperPhysicalRange();
    return pr;
  }
  //----------------------------------------------------------------------------
  /**
   * Set lower physical range "n".
   * @param n lower physical range
   */
  public void SetLowerPhysicalRange(double n) { LowerPhysicalRange=n; }
  //----------------------------------------------------------------------------
  /**
   * Return lower physical range of this variable.
   * @return lower physical range
   */
  public double GetLowerPhysicalRange() { return LowerPhysicalRange; }
  //----------------------------------------------------------------------------
  /**
   * Set upper physical range "n".
   * @param n upper physical range
   */
  public void SetUpperPhysicalRange(double n) { UpperPhysicalRange=n; }
  //----------------------------------------------------------------------------
  /**
   * Return upper physical range of this variable.
   * @return upper physical range
   */
  public double GetUpperPhysicalRange() { return UpperPhysicalRange; }
  //----------------------------------------------------------------------------
  /**
   * Set input interest range [domain_inf, domain_sup].
   * @param domain_inf lower interest range
   * @param domain_sup upper interest range
   */
  public void SetInputInterestRange(double domain_inf, double domain_sup) {
    SetLowerInterestRange(domain_inf);
    SetUpperInterestRange(domain_sup);
  }
  //----------------------------------------------------------------------------
  /**
   * Return input interest range of this variable.<br>
   * [lower interest range, upper interest range].
   * @return interest range
   */
  public double[] GetInputInterestRange() {
    double[] ir= new double[2];
    ir[0]= GetLowerInterestRange();
    ir[1]= GetUpperInterestRange();
    return ir;
  }
  //----------------------------------------------------------------------------
  /**
   * Set lower interest range "n".
   * @param n lower interest range
   */
  public void SetLowerInterestRange(double n) { LowerInterestRange=n; }
  //----------------------------------------------------------------------------
  /**
   * Return lower interest range of this variable.
   * @return lower interest range
   */
  public double GetLowerInterestRange() { return LowerInterestRange; }
  //----------------------------------------------------------------------------
  /**
   * Set upper interest range "n".
   * @param n upper interest range
   */
  public void SetUpperInterestRange(double n) { UpperInterestRange=n; }
  //----------------------------------------------------------------------------
  /**
   * Return upper interest range of this variable.
   * @return upper interest range
   */
  public double GetUpperInterestRange() { return UpperInterestRange; }
  //----------------------------------------------------------------------------
  /**
   * Set number of labels "n". It is a number between 2 and 7.
   * @param n number of labels
   */
  public void SetLabelsNumber(int n) {
    LabelsNumber=n;
    if ( (LabelsNumber > this.UserLabel.length) && (this.LabelName.equals("user")) ) {
        String[] aux= new String[this.UserLabel.length];
        for (int m=0; m<aux.length; m++) {
        	aux[m]= this.UserLabel[m];
        }
    	this.UserLabel= new String[LabelsNumber];
        for (int m=0; m<LabelsNumber; m++) {
        	 if (m < aux.length)
        		 this.UserLabel[m]= aux[m];
        	 else
                 this.UserLabel[m]=LocaleKBCT.GetString("Label")+" "+String.valueOf(m+1);
        }
        this.LabelsName= new String[LabelsNumber];
        for (int k=0; k<LabelsNumber; k++) {
        	this.LabelsName[k]=this.UserLabel[k];
        }
    } 
    if (LabelsNumber > this.LabelsName.length) {
        String[] LN= new String[LabelsNumber];
        for (int k=0; k<LabelsNumber; k++) {
          if (k < this.LabelsName.length)
            LN[k]= this.LabelsName[k];
          else
            LN[k]="";
        }
        this.LabelsName= LN;
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Return the number of labels of this variable.
   * @return number of labels
   */
  public int GetLabelsNumber() { return LabelsNumber; }
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Generate two arrays of strings: </p>
   * <ul>
   *   <li> LabelsName: it will contain names of labels. </li>
   *   <li> UserLabel: it will contain names of labels defined by user.</li>
   * </ul>
   * @param Labels_Number number of labels
   */
  public void InitLabelsName(int Labels_Number) {
    this.LabelsName= new String[Labels_Number];
    if (Labels_Number <=9)
        this.UserLabel= new String[9];
    else
        this.UserLabel= new String[Labels_Number];

    for (int n=0; n<Labels_Number; n++)
      this.LabelsName[n]= "";

    for (int n=0; n<this.UserLabel.length; n++) {
         this.UserLabel[n]=LocaleKBCT.GetString("Label")+" "+String.valueOf(n+1);
    }
  }
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Set the name of each label. </p>
   * <ul> There are five scales:
   *   <li> small-large </li>
   *   <li> few-lot </li>
   *   <li> negative-positive </li>
   *   <li> low-high </li>
   *   <li> defined by user </li>
   * </ul>
   */
  public void SetLabelsName() {
    int NOL= this.GetLabelsNumber();
    String NL[] = new String[NOL];
    String NameLabel = this.GetScaleName();
    if ( (NOL > 9) && (this.LabelName.equals("user")) ) {
        for (int n=0; n<NOL; n++) {
             NL[n] = this.UserLabel[n];
        }
    } else {
      switch (NOL) {
        case 2:
          for (int n=0; n<2; n++) {
            if (NameLabel.equals("small-large"))
              NL[n] = this.SmallLarge[n*4+2];
            else if (NameLabel.equals("few-lot"))
              NL[n] = this.FewLot[n*4+2];
            else if (NameLabel.equals("negative-positive"))
              NL[n] = this.NegativePositive[n*4+2];
            else if (NameLabel.equals("low-high"))
              NL[n] = this.LowHigh[n*4+2];
            else if (NameLabel.equals("user"))
              NL[n] = this.UserLabel[n];
          }
          break;
        case 3:
          for (int n=0; n<3; n++) {
            if (NameLabel.equals("small-large"))
              NL[n] = this.SmallLarge[n*2+2];
            else if (NameLabel.equals("few-lot"))
              NL[n] = this.FewLot[n*2+2];
            else if (NameLabel.equals("negative-positive"))
              NL[n] = this.NegativePositive[n*2+2];
            else if (NameLabel.equals("low-high"))
              NL[n] = this.LowHigh[n*2+2];
            else if (NameLabel.equals("user"))
              NL[n] = this.UserLabel[n];
          }
          break;
        case 4:
          for (int n=0; n<4; n++) {
            if (NameLabel.equals("small-large")) {
              if (n<2)
                NL[n] = this.SmallLarge[n+2];
              else
                NL[n] = this.SmallLarge[n+3];
            } else if (NameLabel.equals("few-lot")) {
              if (n<2)
                NL[n] = this.FewLot[n+2];
              else
                NL[n] = this.FewLot[n+3];
            } else if (NameLabel.equals("negative-positive")) {
              if (n<2)
                NL[n] = this.NegativePositive[n+2];
              else
                NL[n] = this.NegativePositive[n+3];
            } else if (NameLabel.equals("low-high")) {
              if (n<2)
                NL[n] = this.LowHigh[n+2];
              else
                NL[n] = this.LowHigh[n+3];
            } else if (NameLabel.equals("user"))
                NL[n] = this.UserLabel[n];
          }
          break;
        case 5:
          for (int n=0; n<5; n++) {
            if (NameLabel.equals("small-large")) {
                if (n<2)
                  NL[n] = this.SmallLarge[n+1];
                else if (n==2)
                  NL[n] = this.SmallLarge[n+2];
                else
                  NL[n] = this.SmallLarge[n+3];
            } else if (NameLabel.equals("few-lot")) {
                if (n<2)
                  NL[n] = this.FewLot[n+1];
                else if (n==2)
                  NL[n] = this.FewLot[n+2];
                else
                  NL[n] = this.FewLot[n+3];
            } else if (NameLabel.equals("negative-positive")) {
                if (n<2)
                  NL[n] = this.NegativePositive[n+1];
                else if (n==2)
                  NL[n] = this.NegativePositive[n+2];
                else
                  NL[n] = this.NegativePositive[n+3];
            } else if (NameLabel.equals("low-high")) {
                if (n<2)
                  NL[n] = this.LowHigh[n+1];
                else if (n==2)
                  NL[n] = this.LowHigh[n+2];
                else
                  NL[n] = this.LowHigh[n+3];
            } else if (NameLabel.equals("user")) {
                  NL[n] = this.UserLabel[n];
            }
          }
          break;
        case 6:
          for (int n=0; n<6; n++) {
            if (NameLabel.equals("small-large")) {
              if (n<3)
                NL[n] = this.SmallLarge[n+1];
              else
                NL[n] = this.SmallLarge[n+2];
            } else if (NameLabel.equals("few-lot")) {
              if (n<3)
                NL[n] = this.FewLot[n+1];
              else
                NL[n] = this.FewLot[n+2];
            } else if (NameLabel.equals("negative-positive")) {
              if (n<3)
                NL[n] = this.NegativePositive[n+1];
              else
                NL[n] = this.NegativePositive[n+2];
            } else if (NameLabel.equals("low-high")) {
              if (n<3)
                NL[n] = this.LowHigh[n+1];
              else
                NL[n] = this.LowHigh[n+2];
            } else if (NameLabel.equals("user"))
              NL[n] = this.UserLabel[n];
          }
          break;
        case 7:
          for (int n=0; n<7; n++) {
            if (NameLabel.equals("small-large"))
              NL[n] = this.SmallLarge[n+1];
            else if (NameLabel.equals("few-lot"))
              NL[n] = this.FewLot[n+1];
            else if (NameLabel.equals("negative-positive"))
              NL[n] = this.NegativePositive[n+1];
            else if (NameLabel.equals("low-high"))
              NL[n] = this.LowHigh[n+1];
            else if (NameLabel.equals("user"))
              NL[n] = this.UserLabel[n];
          }
          break;
        case 8:
          for (int n=0; n<8; n++) {
            if (NameLabel.equals("small-large")) {
              if (n<4)
                NL[n] = this.SmallLarge[n];
              else
                NL[n] = this.SmallLarge[n+1];
            } else if (NameLabel.equals("few-lot")) {
              if (n<4)
                NL[n] = this.FewLot[n];
              else
                NL[n] = this.FewLot[n+1];
            } else if (NameLabel.equals("negative-positive")) {
              if (n<4)
                NL[n] = this.NegativePositive[n];
              else
                NL[n] = this.NegativePositive[n+1];
            } else if (NameLabel.equals("low-high")) {
              if (n<4)
                NL[n] = this.LowHigh[n];
              else
                NL[n] = this.LowHigh[n+1];
            } else if (NameLabel.equals("user"))
              NL[n] = this.UserLabel[n];
          }
          break;
        case 9:
          for (int n=0; n<9; n++) {
            if (NameLabel.equals("small-large"))
              NL[n] = this.SmallLarge[n];
            else if (NameLabel.equals("few-lot"))
              NL[n] = this.FewLot[n];
            else if (NameLabel.equals("negative-positive"))
              NL[n] = this.NegativePositive[n];
            else if (NameLabel.equals("low-high"))
              NL[n] = this.LowHigh[n];
            else if (NameLabel.equals("user"))
              NL[n] = this.UserLabel[n];
          }
          break;
      }
      LabelsName= NL;
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Set the name "new_name" to the label "N_label".
   * @param N_label number of label name to modify
   * @param new_name new label name
   */
  public void SetLabelsName (int N_label, String new_name) {
    LabelsName[N_label-1]= new_name;
  }
  //----------------------------------------------------------------------------
  /**
   * Return names of all labels from this variable.
   * @return names of the labels
   */
  public String[] GetLabelsName() { return LabelsName; }
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Set the name of each OR label. </p>
   */
  public void SetORLabelsName() {
    int NOL= this.GetLabelsNumber();
    if (NOL >= 4)
      this.OrNames= new String[jnikbct.serie(this.GetLabelsNumber()-1)-3];

    if ( !this.GetScaleName().equals("user") ) {
          int ini=0;
		  int lim=NOL-1;
		  int cont=lim;
		  int NbOR=0;
		  for (int n=0; n<NOL-3; n++) {
			  for (int m=ini; m<lim; m++) {
				  if (n==0) {
				      OrNames[NbOR]= "("+LocaleKBCT.GetString(LabelsName[m])+") "+LocaleKBCT.GetString("OR")+" ("+LocaleKBCT.GetString(LabelsName[m+1])+")";
				  } else {
				      OrNames[NbOR]= "("+LocaleKBCT.GetString(LabelsName[m-ini])+")";
				      for (int k=1; k<2+n;k++) {
	                     OrNames[NbOR]= OrNames[NbOR]+" "+LocaleKBCT.GetString("OR")+" ("+LocaleKBCT.GetString(LabelsName[m-ini+k])+")";
				      }
					  
				  }
	              NbOR++;
			  }
			  cont=cont-1;
			  ini=lim;
			  lim= lim+cont;
	      }
  } else {
          int ini=0;
		  int lim=NOL-1;
		  int cont=lim;
		  int NbOR=0;
		  for (int n=0; n<NOL-3; n++) {
			  for (int m=ini; m<lim; m++) {
				  if (n==0) {
				      OrNames[NbOR]= "("+LabelsName[m]+") "+LocaleKBCT.GetString("OR")+" ("+LabelsName[m+1]+")";
				  } else {
				      OrNames[NbOR]= "("+LabelsName[m-ini]+")";
				      for (int k=1; k<2+n;k++) {
				    	      OrNames[NbOR]= OrNames[NbOR]+" "+LocaleKBCT.GetString("OR")+" ("+LabelsName[m-ini+k]+")";
				      }
					  
				  }
	              NbOR++;
			  }
			  cont=cont-1;
			  ini=lim;
			  lim= lim+cont;
	      }
    }
  }
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Set the name of each OR label. </p>
   * @param OrNames names of composite OR labels
   */
  public void SetORLabelsName(String[] OrNames) { this.OrNames= OrNames; }
  //----------------------------------------------------------------------------
  /**
   * Return names of all OR labels from this variable.
   * @return names of composite OR labels
   */
  public String[] GetORLabelsName() { return OrNames; }
  //----------------------------------------------------------------------------
  /**
   * Set the name "new_name" (defined by the user) to the label "N_label".
   * @param N_label number of label name to modify
   * @param new_name new label name (defined by user)
   */
  public void SetUserLabelsName (int N_label, String new_name) {
    //System.out.println("N_label="+N_label+"   new_name="+new_name+"   LNumber="+this.LabelsNumber);
	//UserLabel[N_label-1]= new_name;
    
    int NbLabels= this.GetLabelsNumber();
    if (N_label > NbLabels) {
        String[] aux= new String[N_label];
        for (int n=0; n<NbLabels; n++) {
        	 aux[n]= this.UserLabel[n];
        }
   	    aux[N_label-1]= new_name;
    	this.UserLabel= new String[N_label];
        for (int n=0; n<N_label; n++) {
        	this.UserLabel[n]= aux[n];
        }
        /*String[] names = this.GetLabelsName();
        String[] UserNames = this.GetUserLabelsName();
        this.LabelsName = new String[names.length + 1];
        for (int n = 0; n < NbLabels+1; n++) {
          if (this.GetScaleName().equals("user")) {
            this.LabelsName[n] = UserNames[n];
            this.UserLabel[n]= UserNames[n];
          } else {
              if (n==NbLabels) {
                this.LabelsName[n]= new_name;
                this.UserLabel[n]= new_name;
              } else {
                this.LabelsName[n]= LocaleKBCT.GetString(names[n]);
                this.UserLabel[n]= LocaleKBCT.GetString(names[n]);
              }
          }
        }*/
    } else {
    	this.UserLabel[N_label-1]= new_name;
        this.SetLabelsName(N_label, new_name);
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Return names of labels defined by user.
   * @return names of labels defined by user
   */
  public String[] GetUserLabelsName() { return UserLabel; }
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Set default label properties: </p>
   * <ul>
   *   <li> numerical -> regular partition </li>
   *   <li> categorical or logical -> discrete </li>
   * </ul>
   */
  public void SetLabelProperties() {
    int NOL= this.GetLabelsNumber();
    double Rinf= this.GetInputInterestRange()[0];
    double Rsup= this.GetInputInterestRange()[1];
    	LabelKBCT[] ee= new LabelKBCT[NOL];
    	double ini=Rinf;
    	double pas=(Rsup-Rinf)/(NOL-1);
    	if (this.GetType().equals("numerical")) {
            ee[0]= new LabelKBCT("SemiTrapezoidalInf", ini, ini, ini+pas, 1);
            ee[NOL-1]= new LabelKBCT("SemiTrapezoidalSup", Rsup-pas, Rsup, Rsup, NOL);
        } else {
            ee[0]= new LabelKBCT("discrete", 1, 1);
            ee[NOL-1]= new LabelKBCT("discrete", NOL, NOL);
        }
    	for (int n=1; n<NOL-1; n++) {
        	if (this.GetType().equals("numerical")) {
                ee[n]= new LabelKBCT("triangular", ini, ini+pas, ini+2*pas, n+1);
            } else {
                ee[n]= new LabelKBCT("discrete", n+1, n+1);
            }
        	ini=ini+pas;
    	}
    	for (int n=0; n<NOL; n++) {
            Labels.put(""+ee[n].GetLabel_Number(), ee[n]);
    	}
  }
  //----------------------------------------------------------------------------
  /**
   * Set modal point "mp" in label "label_number".
   * @param label_number number of label
   * @param mp modal point
   */
  public void SetMP (int label_number, String mp, boolean opt) {
    this.GetLabel(label_number).SetMP(mp);
    // opt == true -> logical / categorical
    // opt == false -> numerical
    if ( opt ) {
        this.GetLabel(label_number).SetP1((new Double(mp)).doubleValue());
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Return modal point from label "label_number".
   * @param label_number number of label
   * @return modal point
   */
  public String GetMP (int label_number) {
    return this.GetLabel(label_number).GetMP();
  }
  //----------------------------------------------------------------------------
  /**
   * Set modal point "mp" in label "label_number".
   * @param label_number number of label
   * @param mp modal point
   */
  public void SetMPaux (int label_number, String mp) {
    this.GetLabel(label_number).SetMPaux(mp);
  }
  //----------------------------------------------------------------------------
  /**
   * Return modal point from label "label_number".
   * @param label_number number of label
   * @return modal point
   */
  public String GetMPaux (int label_number) {
    return this.GetLabel(label_number).GetMPaux();
  }
  //----------------------------------------------------------------------------
  /**
   * Set names for a new scale of labels (defined by the user).
   * @param new_names new scale of labels
   */
  public void SetNewScale (String[] new_names) {
    this.UserLabel= new_names;
    this.LabelsName= new_names;
  }
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Set the name of the scale of labels. </p>
   * <ul> There are five scales:
   *   <li> small-large </li>
   *   <li> few-lot </li>
   *   <li> negative-positive </li>
   *   <li> low-high </li>
   *   <li> defined by user </li>
   * </ul>
   * @param new_LabelName scale of labels
   */
  public void SetScaleName(String new_LabelName) { LabelName= new_LabelName; }
  //----------------------------------------------------------------------------
  /**
   * Return the name of the scale of labels.
   * @return scale of labels
   */
  public String GetScaleName() { return LabelName; }
  //----------------------------------------------------------------------------
  /**
   * Add the label "e" to this variable.<br>
   * Number of labels is increased in one.
   * @param e label to be added
   */
  public void AddLabel(LabelKBCT e) {
    int KEY= e.GetLabel_Number();
    Labels.put(""+KEY, e);
    this.SetLabelsNumber(KEY);
  }
  //----------------------------------------------------------------------------
  /**
   * Return label "Label_Number".
   * @param Label_Number number of label
   * @return label "Label_Number"
   */
  public LabelKBCT GetLabel(int Label_Number) {
    return (LabelKBCT)Labels.get(""+Label_Number);
  }
  //----------------------------------------------------------------------------
  /**
   * Delete label "Label_Number". <br>
   * Number of labels is decreased in one.
   * <ul>
   *   <li> expand= true -> Keep Strong Fuzzy partition </li>
   *   <li> expand= false -> only remove </li>
   * </ul>
   * @param Label_Number number of label
   * @param expand true or false
   */
  public void RemoveLabel(int Label_Number, boolean expand) {
    int LabNum= this.GetLabelsNumber();
    int NewLabNum= LabNum-1;
    String[] names= new String[NewLabNum];
    for (int n=0; n<LabNum; n++) {
      if (!this.GetScaleName().equals("user")) {
          if (Label_Number>n) {
            if (expand)
              names[n]= LocaleKBCT.GetString(this.GetLabelsName()[n]);
            else
              names[n]= this.GetLabelsName()[n];
          } else if (Label_Number<n) {
            if (expand)
              names[n-1]= LocaleKBCT.GetString(this.GetLabelsName()[n]);
            else
              names[n-1]= this.GetLabelsName()[n];
          }
      } else {
          if (Label_Number>n)
            names[n]= this.GetUserLabelsName()[n];
          else if (Label_Number<n)
            names[n-1]= this.GetUserLabelsName()[n];
      }
    }
    Labels.remove(""+String.valueOf(Label_Number+1));
    this.SetLabelsNumber(Label_Number);
    for (int n=Label_Number+1; n<LabNum; n++) {
      LabelKBCT e= this.GetLabel(n+1);
      Labels.remove(""+n);
      int ln= e.GetLabel_Number();
      e.SetLabel_Number(ln-1);
      if (!this.GetType().equals("numerical"))
        e.SetP1(ln-1);

      this.AddLabel(e);
    }
    if (expand)
      this.SetScaleName("user");

    for (int n=0; n<NewLabNum; n++) {
      if (expand) {
        if ( (n==Label_Number) || (n==Label_Number-1) )
          this.SetUserLabelsName(n+1, LocaleKBCT.GetString("MoreOrLess")+" ("+ names[n]+")");
        else
          this.SetUserLabelsName(n+1, names[n]);
      } else {
        if (!this.GetScaleName().equals("user"))
            this.SetLabelsName(n+1, names[n]);
        else
            this.SetUserLabelsName(n+1, names[n]);
      }
    }
    this.SetORLabelsName();
    if (this.GetType().equals("numerical")) {
      if (Label_Number==0) {
        LabelKBCT e= this.GetLabel(1);
        e.SetName("SemiTrapezoidalInf");
        e.SetP1(this.LowerInterestRange);
        this.ReplaceLabel(1, e);
      } else if (Label_Number==NewLabNum) {
        LabelKBCT e= this.GetLabel(Label_Number);
        e.SetName("SemiTrapezoidalSup");
        e.SetP3(this.UpperInterestRange);
        this.ReplaceLabel(Label_Number, e);
      } else if (expand) {
        LabelKBCT e1= this.GetLabel(Label_Number);
        LabelKBCT e2= this.GetLabel(Label_Number+1);
        int l=e1.GetParams().length;
        if (l==3) {
            e1.SetP3(e2.GetP2());
            e2.SetP1(e1.GetP2());
        } else if (l==4) {
            e1.SetP4(e2.GetP2());
            e2.SetP1(e1.GetP3());
        }
        this.ReplaceLabel(Label_Number, e1);
        this.ReplaceLabel(Label_Number+1, e2);
      }
    } else {
    	//System.out.println("variable: LabNumber="+Label_Number);
    	// Label_Number begins in 0
        LabelKBCT e= this.GetLabel(Label_Number+1);
        e.SetName("discrete");
        this.SetInputPhysicalRange(1.0, this.GetLabelsNumber());
        this.SetInputInterestRange(1.0, this.GetLabelsNumber());
        this.ReplaceLabel(Label_Number+1, e);
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Replace label "Label_Number" by the new label "e".
   * @param Label_Number number of label
   * @param e label to be replaced
   */
  public void ReplaceLabel(int Label_Number, LabelKBCT e) {
    Labels.put(""+Label_Number, e);
  }
  //----------------------------------------------------------------------------
  /**
   * Set "Active" flag for this variable.
   * @param a flag of variable activation
   */
  public void SetActive(boolean a) { this.active= a; }
  //----------------------------------------------------------------------------
  /**
   * Return "Active" flag.
   * @return true or false (flag of variable activation)
   */
  public boolean isActive() { return this.active; }
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Set the Flag FlagModify. </p>
   * @param Flag of variable modification
   */
  public void SetFlagModify(boolean Flag) { this.FlagModify= Flag; }
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Get the Flag FlagModify. </p>
   * @return true or false (flag of variable modification)
   */
  public boolean GetFlagModify() { return this.FlagModify; }
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Set the Flag FlagOntology. </p>
   * @param Flag of variable origin (defined from Ontology browser)
   */
  public void SetFlagOntology(boolean Flag) { 
	  this.FlagOntology= Flag;
	  //if (this.FlagOntology)
		//  this.SetNameFromOntology(this.Name);
  }
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Get the Flag FlagOntology. </p>
   * @return true or false (flag of variable origin)
   */
  public boolean GetFlagOntology() { return this.FlagOntology; }
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Set variable name extracted from Ontology. </p>
   */
  public void SetNameFromOntology(String name) { 
	  this.nameOntology= name;
  }
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Get variable name extracted from Ontology. </p>
   */
  public String GetNameFromOntology() { return this.nameOntology; }
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Set the Flag FlagOntDatatypeProperty. </p>
   * @param Flag of variable origin (defined from Ontology browser)
   */
  public void SetFlagOntDatatypeProperty(boolean Flag) { 
	  this.FlagOntDatatypeProperty= Flag;
  }
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Get the Flag FlagOntDatatypeProperty. </p>
   * @return true or false (flag of variable origin)
   */
  public boolean GetFlagOntDatatypeProperty() { return this.FlagOntDatatypeProperty; }
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Set the Flag FlagOntObjectProperty. </p>
   * @param Flag of variable origin (defined from Ontology browser)
   */
  public void SetFlagOntObjectProperty(boolean Flag) { 
	  this.FlagOntObjectProperty= Flag;
  }
  //----------------------------------------------------------------------------
  /**
   * <p align="left"> Get the Flag FlagOntObjectProperty. </p>
   * @return true or false (flag of variable origin)
   */
  public boolean GetFlagOntObjectProperty() { return this.FlagOntObjectProperty; }
//------------------------------------------------------------------------------
  /**
   * Set "Classif" option (yes/no) for this variable.
   * @param c yes or no
   */
  public void SetClassif(String c) { this.Classif= c; }
//------------------------------------------------------------------------------
  /**
   * Return "Classif" option (yes/no).
   * @return yes or no ("Classif" option for this variable)
   */
  public String GetClassif() { return this.Classif; }
//------------------------------------------------------------------------------
  /**
   * Init vector "LabelsNamesSaveFISquality" used in "KBCT to FIS" translation for Quality.
   */
  public void initMFLabelNames() { this.LabelsNamesSaveFISquality= new Vector<String>(); }
//------------------------------------------------------------------------------
  /**
   * Add label name to vector "LabelsNamesSaveFISquality" used in "KBCT to FIS" translation for Quality.
   * @param LN label name
   */
  public void AddMFLabelName(String LN) { this.LabelsNamesSaveFISquality.add(LN); }
//------------------------------------------------------------------------------
  /**
   * Return the content of vector "LabelsNamesSaveFISquality" used in "KBCT to FIS" translation for Quality.
   * @return label names
   */
  public Object[] GetMFLabelNames() { return this.LabelsNamesSaveFISquality.toArray(); }
//------------------------------------------------------------------------------
  /**
   */
  public int GetLabelFired(double d) {
    if (this.GetType().equals("numerical")) {
        for (int n=0; n<this.GetLabelsNumber(); n++) {
          LabelKBCT l= this.GetLabel(n+1);
          double[] p= l.GetParams();
          if ( (n==0) && (d<p[0]) )
  	        return 1;

          if ( (n==this.GetLabelsNumber()-1) && ( (p.length==3) && (d>p[1]) ) )
          	return this.GetLabelsNumber();

          switch(p.length) {
            case 1: if (p[0]==d)
                        return l.GetLabel_Number();
                    break;
            case 3: if ( (d >= (p[0] + p[1])/2) && (d <= (p[1] + p[2])/2) )
                        return l.GetLabel_Number();
                    break;
            case 4: if ( (d >= (p[0] + p[1])/2) && (d <= (p[2] + p[3])/2) )
                        return l.GetLabel_Number();
                    break;
          }
        }
    } else
        return (int)d;

    //System.out.println("variable.java -> "+this.GetType());
    //System.out.println("variable.java -> "+d);
    return 0;
  }
}
