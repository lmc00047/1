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
//                        JEvaluationFuzzyPartition.java
//
//
//**********************************************************************
package kbct;

/**
 * kbct.JEvaluationFuzzyPartition
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0, 03/08/15
 */

public class JEvaluationFuzzyPartition {
  private JVariable var;
  private int NbLabels;
  private double[] DataSet;
  private int NbData;
  private double[] DoM;
  private double PC;
  private double PE;
  private double ChenIndex;
  //----------------------------------------------------------------------------
  public JEvaluationFuzzyPartition(JVariable v, double[] data) {
    this.var= v;
    this.NbLabels= this.var.GetLabelsNumber();
    this.DataSet= data;
    this.NbData= this.DataSet.length;
    this.DoM= new double[this.NbData];
    this.CalculateDoM();
    this.PartitionCoefficient();
    this.PartitionEntropy();
    this.Chen();
  }
  //----------------------------------------------------------------------------
  /**
   * Compute membership degree of the k-th element of the
   * data set to the i-th element of the fuzzy partition
   */
  private void CalculateDoM () {
    double Ysup= 1.0;
    double Yinf= 0.0;
    double[] Vertex= new double[this.NbLabels];
    for (int i=0; i < this.NbLabels; i++) {
      if (!this.var.GetType().endsWith("numerical"))
        Vertex[i]= this.var.GetLabel(i+1).GetP1();
      else {
        double[] d= this.var.GetLabel(i+1).GetParams();
        if (d.length==4)
          Vertex[i]= (d[1]+d[2])/2;
        else
          Vertex[i]= d[1];
      }
    }

    for (int k=0; k<this.NbData; k++) {
      if (this.DataSet[k] < Vertex[0])
          this.DoM[k]= 1.0;
      else if (this.DataSet[k] > Vertex[this.NbLabels-1])
          this.DoM[k]= 1.0;
      else {
          for (int i=1; i < Vertex.length; i++) {
               if (this.DataSet[k] == Vertex[i-1]) {
                 this.DoM[k]= 1.0;
                 break;
               } else if ( (this.DataSet[k] > Vertex[i-1]) && (this.DataSet[k] < Vertex[i]) ) {
                  double[] d1= this.var.GetLabel(i).GetParams();
                  double[] d2= this.var.GetLabel(i+1).GetParams();
                  if ( (d1.length==3) && (d2.length==3) ) {
                    double A= (Ysup - Yinf)/(Vertex[i-1]-Vertex[i]);
                    double B= Ysup - A*Vertex[i-1];
                    this.DoM[k]= A*this.DataSet[k]+B;
                  } else if ( (d1.length==4) && (d1[1] < this.DataSet[k]) && (this.DataSet[k] < d1[2]) ) {
                    this.DoM[k]= 1.0;
                  } else if ( (d2.length==4) && (d2[1] < this.DataSet[k]) && (this.DataSet[k] < d2[2]) ) {
                    this.DoM[k]= 1.0;
                  } else {
                    double A= (Ysup - Yinf)/(d2[0]-d2[1]);
                    double B= Ysup - A*d2[0];
                    this.DoM[k]= A*this.DataSet[k]+B;
                  }
                  break;
               }
          }
      }
    }
  }
  //----------------------------------------------------------------------------
  /**
   * Return nuber of labels
   */
  public int getNbLabels () { return NbLabels; }
  //----------------------------------------------------------------------------
  /**
   * Calculate Partition Coefficient
   */
  private void PartitionCoefficient () {
    double result= 0.0;
    for (int n=0; n<this.DoM.length; n++)
         result= result + (this.DoM[n])*(this.DoM[n]) + (1 - this.DoM[n])*(1 - this.DoM[n]);

    double aux= this.NbData;
    this.PC= result/aux;
  }
  //----------------------------------------------------------------------------
  /**
   * Return PC (Partition Coefficient)
   */
  public double getPC () { return PC; }
  //----------------------------------------------------------------------------
  /**
   * Calculate Partition Entropy
   */
  private void PartitionEntropy () {
    double result= 0.0;
    // If DoM[n]= 0.0 THEN result= NaN
    // 0*Infinity = NaN (indetermination) = 0
    for (int n=0; n<this.DoM.length; n++) {
      if (this.DoM[n] != 0)
        result= result + (this.DoM[n])*Math.log(Math.abs(this.DoM[n]));
      if ( (1-this.DoM[n]) != 0 )
        result= result + (1 - this.DoM[n])*Math.log(Math.abs(1 - this.DoM[n]));
    }
    double aux= this.NbData;
    if (result < 0)
    	result= (-1)*result;
    
    this.PE= result/aux;
  }
  //----------------------------------------------------------------------------
  /**
   * Return PE (Partition Entropy)
   */
  public double getPE () { return PE; }
  //----------------------------------------------------------------------------
  /**
   * Calculate Chen index
   */
  private void Chen () {
    double r1= 0.0;
    double r2= 0.0;
    for (int k=0; k<this.NbData; k++) {
      r1= r1 + Math.max(this.DoM[k], 1 - this.DoM[k]);
      r2= r2 + Math.min(this.DoM[k], 1 - this.DoM[k]);
    }
    double aux1= 2;
    double aux2= this.NbLabels*(this.NbLabels-1);
    r2= (aux1/aux2)*r2;
    double aux3= (r1 - r2);
    double aux4= this.NbData;
    this.ChenIndex= aux3/aux4;
  }
  //----------------------------------------------------------------------------
  /**
   * Return Chen Index
   */
  public double getChenIndex () { return ChenIndex; }
}
