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
//                              LabelKBCT.java
//
//
//**********************************************************************

package KB;

import fis.FISPlot;

/**
 * Define Labels: Name, Parameters, Modal Points
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
public class LabelKBCT {
   private String name;
   private double[] params;
   private int Label_Number;
   private String MP;
   private String MPaux;
   public final static int TRIANGULAR=0;
   public final static int TRAPEZOIDAL= 1;
   public final static int SEMITRAPEZOIDALINF= 2;
   public final static int SEMITRAPEZOIDALSUP= 3;
   public final static int DISCRETE= 4;
   public final static int UNIVERSAL= 5;
   public final static int GAUSSIAN= 6;
   //public final static int DOOR= 7;
   //public final static int SINUS= 8;
   //public final static int SINUSINF= 9;
   //public final static int SINUSSUP= 10;
   //----------------------------------------------------------------------------
   /**
    * <p align="left"> Generate a new label. </p>
    * <p align="left">
    * Default values:
    * <ul>
    *   <li> Name: "" </li>
    *   <li> Number of parameters: 3 (0,0,0) </li>
    *   <li> Number of label: 0 </li>
    *   <li> Modal point: "No MP" (no modal point definition) </li>
    * </ul>
    * </p>
    */
   public LabelKBCT() {
     name= "";
     params= new double[3];
     Label_Number=0;
     MP="No MP";
     MPaux="No MP";
   }
   //----------------------------------------------------------------------------
   /**
    * <p align="left"> Generate a new label. </p>
    * <p align="left">
    * Values:
    * <ul>
    *   <li> Name: name </li>
    *   <li> Number of parameters: 1 (p1) </li>
    *   <li> Number of label: label_number </li>
    *   <li> Modal point: "No MP" (no modal point definition) </li>
    * </ul>
    * </p>
    *@param name name of the label
    *@param p1 parameter (discrete label)
    *@param label_number number of the label
    */
   public LabelKBCT(String name, double p1, int label_number) {
     this.name= name;
     params= new double[1];
     params[0]= p1;
     Label_Number= label_number;
     MP="No MP";
     MPaux="No MP";
   }
   //----------------------------------------------------------------------------
   /**
    * <p align="left"> Generate a new label. </p>
    * <p align="left">
    * Values:
    * <ul>
    *   <li> Name: name </li>
    *   <li> Number of parameters: 2 (p1, p2) </li>
    *   <li> Number of label: label_number </li>
    *   <li> Modal point: "No MP" (no modal point definition) </li>
    * </ul>
    * </p>
    *@param name name of the label
    *@param p1 first parameter
    *@param p2 second parameter
    *@param label_number number of the label
    */
   public LabelKBCT(String name, double p1, double p2, int label_number) {
     this.name= name;
     params= new double[2];
     params[0]= p1;
     params[1]= p2;
     Label_Number= label_number;
     MP="No MP";
     MPaux="No MP";
   }
   //----------------------------------------------------------------------------
   /**
    * <p align="left"> Generate a new label. </p>
    * <p align="left">
    * Values:
    * <ul>
    *   <li> Name: name </li>
    *   <li> Number of parameters: 3 (p1, p2, p3) </li>
    *   <li> Number of label: label_number </li>
    *   <li> Modal point: "No MP" (no modal point definition) </li>
    * </ul>
    * </p>
    *@param name name of the label
    *@param p1 first parameter
    *@param p2 second parameter
    *@param p3 third parameter
    *@param label_number number of the label
    */
   public LabelKBCT(String name, double p1, double p2, double p3, int label_number) {
     this.name= name;
     params= new double[3];
     params[0]= p1;
     params[1]= p2;
     params[2]= p3;
     Label_Number= label_number;
     MP="No MP";
     MPaux="No MP";
   }
   //----------------------------------------------------------------------------
   /**
    * <p align="left"> Generate a new label. </p>
    * <p align="left">
    * Values:
    * <ul>
    *   <li> Name: name </li>
    *   <li> Number of parameters: 4 (p1, p2, p3, p4) </li>
    *   <li> Number of label: label_number </li>
    *   <li> Modal point: "No MP" (no modal point definition) </li>
    * </ul>
    * </p>
    *@param name name of the label
    *@param p1 first parameter
    *@param p2 second parameter
    *@param p3 third parameter
    *@param p4 fourth parameter
    *@param label_number number of the label
    */
   public LabelKBCT(String name, double p1, double p2, double p3, double p4, int label_number) {
     this.name= name;
     params= new double[4];
     params[0]= p1;
     params[1]= p2;
     params[2]= p3;
     params[3]= p4;
     Label_Number= label_number;
     MP="No MP";
     MPaux="No MP";
   }
   //----------------------------------------------------------------------------
   /**
    * Set the name of this label.
    *@param name name of the label
    */
   public void SetName(String name) { this.name= name; }
   //----------------------------------------------------------------------------
   /**
    * Return the name of this label.
    *@return name
    */
   public String GetName() { return this.name; }
   //----------------------------------------------------------------------------
   /**
    * Set params length.
    *@param n dimension of array "params"
    */
   public void InitParams (int n) { params= new double[n]; }
   //----------------------------------------------------------------------------
   /**
    * Set parameter P1.
    *@param p1 first parameter
    */
   public void SetP1 (double p1) { params[0]= p1; }
   //----------------------------------------------------------------------------
   /**
    * Return parameter P1.
    *@return p1
    */
   public double GetP1 () { return params[0]; }
   //----------------------------------------------------------------------------
   /**
    * Set parameter P2.
    *@param p2 second parameter
    */
   public void SetP2 (double p2) { params[1]= p2; }
   //----------------------------------------------------------------------------
   /**
    * Return parameter P2.
    *@return p2
    */
   public double GetP2 () { return params[1]; }
   //----------------------------------------------------------------------------
   /**
    * Set parameter P3.
    *@param p3 third parameter
    */
   public void SetP3 (double p3) { params[2]= p3; }
   //----------------------------------------------------------------------------
   /**
    * Return parameter P3.
    *@return p3
    */
   public double GetP3 () { return params[2]; }
   //----------------------------------------------------------------------------
   /**
    * Set parameter P4.
    *@param p4 fourth parameter
    */
   public void SetP4 (double p4) { params[3]= p4; }
   //----------------------------------------------------------------------------
   /**
    * Return parameter P4.
    *@return p4
    */
   public double GetP4 () { return params[3]; }
   //----------------------------------------------------------------------------
   /**
    * Return all parameters of this label.<br>
    * The number of parameters is contained between 1 and 4.
    *@return params pi
    */
   public double[] GetParams () { return params; }
   //----------------------------------------------------------------------------
   /**
    * Set the number of this label.
    * @param ln Label number
    */
   public void SetLabel_Number(int ln) { this.Label_Number= ln; }
   //----------------------------------------------------------------------------
   /**
    * Return the number of this label.
    *@return Label_Number
    */
   public int GetLabel_Number() { return Label_Number; }
   //----------------------------------------------------------------------------
   /**
    * <p align="left"> Return the number related to the type of membership function: </p>
    * <ul>
    *   <li> triangular -> this.TRIANGULAR -> 0 </li>
    *   <li> trapezoidal -> this.TRAPEZOIDAL -> 1 </li>
    *   <li> SemiTrapezoidalInf -> this.SEMITRAPEZOIDALINF -> 2 </li>
    *   <li> SemiTrapezoidalSup -> this.SEMITRAPEZOIDALSUP -> 3 </li>
    *   <li> discrete -> this.DISCRETE -> 4 </li>
    * </ul>
    *@return Label type
    */
   public int GetLabelType_Number () {
	/*   
	 * <li> universal -> this.UNIVERSAL -> 5 </li>
	 * <li> gaussian -> this.GAUSSIAN -> 6 </li>
	 * <li> door -> this.DOOR -> 7 </li>
	 * <li> sinus -> this.SINUS -> 8 </li>
	 * <li> SinusInf -> this.SINUSINF -> 9 </li>
	 * <li> SinusSup -> this.SINUSSUP -> 10 </li>
    */
     if (this.GetName().equals("triangular"))
       return LabelKBCT.TRIANGULAR;
     else if (this.GetName().equals("trapezoidal"))
       return LabelKBCT.TRAPEZOIDAL;
     else if (this.GetName().equals("SemiTrapezoidalInf"))
       return LabelKBCT.SEMITRAPEZOIDALINF;
     else if (this.GetName().equals("SemiTrapezoidalSup"))
       return LabelKBCT.SEMITRAPEZOIDALSUP;
     else if (this.GetName().equals("discrete"))
       return LabelKBCT.DISCRETE;
     else
       return -1;
   }
   //----------------------------------------------------------------------------
   /**
    * Set modal point "mp".
    * @param mp modal point
    */
   public void SetMP (String mp) { MP= mp; }
   //----------------------------------------------------------------------------
   /**
    * Return modal point.
    * @return MP (modal point)
    */
   public String GetMP () { return MP; }
   //----------------------------------------------------------------------------
   /**
    * Set modal point "mpaux".
    * @param mp modal point
    */
   public void SetMPaux (String mpaux) { MPaux= mpaux; }
   //----------------------------------------------------------------------------
   /**
    * Return modal point.
    * @return MPaux (modal point)
    */
   public String GetMPaux () { return MPaux; }
   //----------------------------------------------------------------------------
   /**
    * Draw this label with its modal point (if it is defined).
    * @param plot FISPlot object
    * @param data_set point
    * @throws Throwable
    */
   public void Draw( FISPlot plot, int data_set ) throws Throwable {
     String MP= this.GetMP();
     if (this.GetName().equals("triangular")) {
       plot.addPoint(data_set, this.GetP1(), 0, false);
       plot.addPoint(data_set, this.GetP2(), 1, true);
       plot.addPoint(data_set, this.GetP3(), 0, true);
       if (!MP.equals("No MP")) {
         Double mp= new Double(MP);
         if (!mp.isNaN()) {
             plot.addPoint(data_set, mp.doubleValue(), 0, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.2, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.3, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.5, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.6, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.8, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.9, false);
             plot.addPoint(data_set, mp.doubleValue(), 1.1, true);
        } else {
             plot.addPoint(data_set, this.GetP2(), 0, false);
             plot.addPoint(data_set, this.GetP2(), 0.2, true);
             plot.addPoint(data_set, this.GetP2(), 0.3, false);
             plot.addPoint(data_set, this.GetP2(), 0.5, true);
             plot.addPoint(data_set, this.GetP2(), 0.6, false);
             plot.addPoint(data_set, this.GetP2(), 0.8, true);
             plot.addPoint(data_set, this.GetP2(), 0.9, false);
             plot.addPoint(data_set, this.GetP2(), 1.1, true);
         }
       }
     } else if (this.GetName().equals("trapezoidal")) {
       plot.addPoint(data_set, this.GetP1(), 0, false);
       plot.addPoint(data_set, this.GetP2(), 1, true);
       plot.addPoint(data_set, this.GetP3(), 1, true);
       plot.addPoint(data_set, this.GetP4(), 0, true);
       if (!MP.equals("No MP")) {
         Double mp= new Double(MP);
         if (!mp.isNaN()) {
             plot.addPoint(data_set, mp.doubleValue(), 0, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.2, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.3, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.5, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.6, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.8, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.9, false);
             plot.addPoint(data_set, mp.doubleValue(), 1.1, true);
         /*} else {
             double m= (this.GetP2()+this.GetP3())/2;
             plot.addPoint(data_set, m, 0, false);
             plot.addPoint(data_set, m, 0.2, true);
             plot.addPoint(data_set, m, 0.3, false);
             plot.addPoint(data_set, m, 0.5, true);
             plot.addPoint(data_set, m, 0.6, false);
             plot.addPoint(data_set, m, 0.8, true);
             plot.addPoint(data_set, m, 0.9, false);
             plot.addPoint(data_set, m, 1.1, true);
         }*/
         }
       }
       String MPaux= this.GetMPaux();
       if (!MPaux.equals("No MP")) {
           Double mpaux= new Double(MPaux);
           if (!mpaux.isNaN()) {
               plot.addPoint(data_set, mpaux.doubleValue(), 0, false);
               plot.addPoint(data_set, mpaux.doubleValue(), 0.2, true);
               plot.addPoint(data_set, mpaux.doubleValue(), 0.3, false);
               plot.addPoint(data_set, mpaux.doubleValue(), 0.5, true);
               plot.addPoint(data_set, mpaux.doubleValue(), 0.6, false);
               plot.addPoint(data_set, mpaux.doubleValue(), 0.8, true);
               plot.addPoint(data_set, mpaux.doubleValue(), 0.9, false);
               plot.addPoint(data_set, mpaux.doubleValue(), 1.1, true);
           /*} else {
               double m= (this.GetP2()+this.GetP3())/2;
               plot.addPoint(data_set, m, 0, false);
               plot.addPoint(data_set, m, 0.2, true);
               plot.addPoint(data_set, m, 0.3, false);
               plot.addPoint(data_set, m, 0.5, true);
               plot.addPoint(data_set, m, 0.6, false);
               plot.addPoint(data_set, m, 0.8, true);
               plot.addPoint(data_set, m, 0.9, false);
               plot.addPoint(data_set, m, 1.1, true);
           }*/
           }
         }
     } else if (this.GetName().equals("SemiTrapezoidalInf")) {
       plot.addPoint(data_set, plot.GetXMin(), 1, false);
       plot.addPoint(data_set, this.GetP2(), 1, true);
       plot.addPoint(data_set, this.GetP3(), 0, true);
       if (!MP.equals("No MP")) {
         Double mp= new Double(MP);
         if (!mp.isNaN()) {
             plot.addPoint(data_set, mp.doubleValue(), 0, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.2, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.3, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.5, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.6, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.8, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.9, false);
             plot.addPoint(data_set, mp.doubleValue(), 1.1, true);
         } else {
             plot.addPoint(data_set, this.GetP2(), 0, false);
             plot.addPoint(data_set, this.GetP2(), 0.2, true);
             plot.addPoint(data_set, this.GetP2(), 0.3, false);
             plot.addPoint(data_set, this.GetP2(), 0.5, true);
             plot.addPoint(data_set, this.GetP2(), 0.6, false);
             plot.addPoint(data_set, this.GetP2(), 0.8, true);
             plot.addPoint(data_set, this.GetP2(), 0.9, false);
             plot.addPoint(data_set, this.GetP2(), 1.1, true);
         }
       }
     } else if (this.GetName().equals("SemiTrapezoidalSup")) {
       plot.addPoint(data_set, this.GetP1(), 0, false);
       plot.addPoint(data_set, this.GetP2(), 1, true);
       plot.addPoint(data_set, plot.GetXMax(), 1, true);
       if (!MP.equals("No MP")) {
         Double mp= new Double(MP);
         if (!mp.isNaN()) {
             plot.addPoint(data_set, mp.doubleValue(), 0, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.2, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.3, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.5, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.6, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.8, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.9, false);
             plot.addPoint(data_set, mp.doubleValue(), 1.1, true);
         } else {
             plot.addPoint(data_set, this.GetP2(), 0, false);
             plot.addPoint(data_set, this.GetP2(), 0.2, true);
             plot.addPoint(data_set, this.GetP2(), 0.3, false);
             plot.addPoint(data_set, this.GetP2(), 0.5, true);
             plot.addPoint(data_set, this.GetP2(), 0.6, false);
             plot.addPoint(data_set, this.GetP2(), 0.8, true);
             plot.addPoint(data_set, this.GetP2(), 0.9, false);
             plot.addPoint(data_set, this.GetP2(), 1.1, true);
         }
       }
     } else if (this.GetName().equals("discrete")) {
       plot.addPoint(data_set, this.GetP1(), 0, false);
       plot.addPoint(data_set, this.GetP1(), 1, true);
     } else if (this.GetName().equals("universal")) {
       plot.addPoint(data_set, this.GetP1(), 1, false);
       plot.addPoint(data_set, this.GetP2(), 1, true);
       if (!MP.equals("No MP")) {
         Double mp= new Double(MP);
         if (!mp.isNaN()) {
             plot.addPoint(data_set, mp.doubleValue(), 0, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.2, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.3, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.5, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.6, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.8, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.9, false);
             plot.addPoint(data_set, mp.doubleValue(), 1.1, true);
         } else {
             double m = this.GetP2() - this.GetP1();
             plot.addPoint(data_set, m, 0, false);
             plot.addPoint(data_set, m, 0.2, true);
             plot.addPoint(data_set, m, 0.3, false);
             plot.addPoint(data_set, m, 0.5, true);
             plot.addPoint(data_set, m, 0.6, false);
             plot.addPoint(data_set, m, 0.8, true);
             plot.addPoint(data_set, m, 0.9, false);
             plot.addPoint(data_set, m, 1.1, true);
         }
       }
     } else if (this.GetName().equals("gaussian")) {
       int nb_points = 25;
       double [] x = new double [nb_points];
       double [] y = new double [nb_points];
       double dx = (5*this.GetP1()) / (nb_points-1);
       plot.addPoint(data_set, this.GetP2(), 1, false);
       for( int i=0 ; i<nb_points ; i++ ) {
         x[i] = i * dx;
         y[i] = Math.exp(-Math.pow(x[i], 2)/(2*Math.pow(this.GetP1(), 2)));
       }
       plot.addPoint(data_set, plot.GetXMax(), 0, false);
       for( int i=nb_points-1 ; i>=0 ; i-- )
         plot.addPoint(data_set, this.GetP2()+x[i], y[i], true);

       for( int i=0 ; i<nb_points ; i++ )
         plot.addPoint(data_set, this.GetP2()-x[i], y[i], true);

       plot.addPoint(data_set, plot.GetXMin(), 0, true);
       if (!MP.equals("No MP")) {
         Double mp= new Double(MP);
         if (!mp.isNaN()) {
             plot.addPoint(data_set, mp.doubleValue(), 0, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.2, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.3, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.5, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.6, false);
             plot.addPoint(data_set, mp.doubleValue(), 0.8, true);
             plot.addPoint(data_set, mp.doubleValue(), 0.9, false);
             plot.addPoint(data_set, mp.doubleValue(), 1.1, true);
         } else {
             plot.addPoint(data_set, this.GetP2(), 0, false);
             plot.addPoint(data_set, this.GetP2(), 0.2, true);
             plot.addPoint(data_set, this.GetP2(), 0.3, false);
             plot.addPoint(data_set, this.GetP2(), 0.5, true);
             plot.addPoint(data_set, this.GetP2(), 0.6, false);
             plot.addPoint(data_set, this.GetP2(), 0.8, true);
             plot.addPoint(data_set, this.GetP2(), 0.9, false);
             plot.addPoint(data_set, this.GetP2(), 1.1, true);
         }
       }
    }
   }
   //----------------------------------------------------------------------------
   public double getMembershipDegree(double x) {
	   double mdeg=0;
	   int np= this.params.length;
	   if (np==1) {
		   if (this.GetP1()==x)
		       mdeg=1;
	   } else if (np==3) {
		   if (this.GetP2()==x)
			   mdeg=1;
		   else if (this.GetLabelType_Number()==LabelKBCT.SEMITRAPEZOIDALINF) {
			   if ( (this.GetP1()<=x) && (this.GetP2()>=x) )
				   mdeg=1;
			   else {
				   double A= 1/(this.GetP2()-this.GetP3());
				   double B=-A*this.GetP3();
				   mdeg= A*x + B;
			   }
		   } else if (this.GetLabelType_Number()==LabelKBCT.SEMITRAPEZOIDALSUP) {
			   if ( (this.GetP2()<=x) && (this.GetP3()>=x) )
				   mdeg=1;
			   else {
				   double A= 1/(this.GetP2()-this.GetP1());
				   double B=-A*this.GetP1();
				   mdeg= A*x + B;
			   }
		   } else {
			   // TRIANGULAR
			   if ( (this.GetP1()<=x) && (this.GetP2()>=x) ) {
				   double A= 1/(this.GetP2()-this.GetP1());
				   double B=-A*this.GetP1();
				   mdeg= A*x + B;
			   } else {
				   double A= 1/(this.GetP2()-this.GetP3());
				   double B=-A*this.GetP3();
				   mdeg= A*x + B;
			   }
		   }
	   } else if (np==4) {
		   if ( (this.GetP2()<=x) && (this.GetP3()>=x) )
			   mdeg=1;
		   else if ((this.GetP1()<=x) && (this.GetP2()>=x)) {
			   double A= 1/(this.GetP2()-this.GetP1());
			   double B=-A*this.GetP1();
			   mdeg= A*x + B;
		   } else if ((this.GetP3()<=x) && (this.GetP4()>=x)) {
			   double A= 1/(this.GetP3()-this.GetP4());
			   double B=-A*this.GetP4();
			   mdeg= A*x + B;
		   }
	   }
	   return mdeg;
   }
}