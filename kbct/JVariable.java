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
//                              JVariable.java
//
//
//**********************************************************************
package kbct;

import KB.LabelKBCT;
import KB.variable;
import fis.jnifis;

/**
 * kbct.JVariable is used to generate and modify variables.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

//------------------------------------------------------------------------------
public class JVariable {
  protected int ptr;
  protected variable v;
  protected boolean input= false;
  protected boolean output= false;
//------------------------------------------------------------------------------
  /**
   * <p align="left"> Generate a new JVariable: </p>
   * <ul>
   *   <li> variable: v </li>
   *   <li> identifier: ptr </li>
   * </ul>
   */
  public JVariable( variable v, int ptr, String var ) {
    this.v = v;
    this.ptr= ptr;
    if (var.equals("Input"))
      this.input= true;
    else if (var.equals("Output"))
      this.output= true;
  }
//------------------------------------------------------------------------------
  /**
   * <p align="left"> Generate a new JVariable: </p>
   * <ul>
   *   <li> identifier: ptr </li>
   * </ul>
   */
  public JVariable( int ptr, String var ) throws Throwable {
    this.ptr = ptr;
    this.v= new variable();
    if (var.equals("Input"))
      this.input= true;
    else if (var.equals("Output"))
      this.output= true;

    v.SetName(jnifis.GetInputName(ptr));
    v.SetInputInterestRange(jnifis.GetInputRange(ptr)[0], jnifis.GetInputRange(ptr)[1]);
    v.SetInputPhysicalRange(jnifis.GetInputRange(ptr)[0], jnifis.GetInputRange(ptr)[1]);
  }
//------------------------------------------------------------------------------
  /**
   * <p align="left"> Generate a new JVariable: </p>
   * <ul>
   *   <li> identifier: ptr </li>
   *   <li> vertex: t </li>
   *   <li> lower range: min </li>
   *   <li> upper range: max </li>
   * </ul>
   */
  public JVariable( int ptr, double [] t, double min, double max, String var ) throws Throwable {
    this.ptr = ptr;
    this.v= new variable();
    if (var.equals("Input"))
      this.input= true;
    else if (var.equals("Output"))
      this.output= true;

    this.SetName("new variable");
    this.SetType("numerical");
    this.SetInputInterestRange(min, max);
    this.SetInputPhysicalRange(min, max);
    this.SetLabelsNumber(0);
    LabelKBCT e_first, e_last;
    if (min <= t[0])
      e_first= new LabelKBCT("SemiTrapezoidalInf", min, t[0], t[1], 1);
    else
      e_first= new LabelKBCT("SemiTrapezoidalInf", t[0], t[0], t[1], 1);

    this.AddLabel(e_first);
    //System.out.println("JVariable: t.length -> "+t.length);
    for (int n=1; n<t.length-1; n++) {
        LabelKBCT e= new LabelKBCT("triangular", t[n-1], t[n], t[n+1], n+1);
        this.AddLabel(e);
    }
    if (max >= t[t.length-1])
      e_last= new LabelKBCT("SemiTrapezoidalSup", t[t.length-2], t[t.length-1], max, t.length);
    else
      e_last= new LabelKBCT("SemiTrapezoidalSup", t[t.length-2], t[t.length-1], t[t.length-1], t.length);

    this.AddLabel(e_last);
    //System.out.println("JVariable: this.GetLabelsNumber() -> "+this.GetLabelsNumber());
  }
//------------------------------------------------------------------------------
  /**
   * Set variable "v".
   */
  public void SetV( variable v ) { this.v = v; }
//------------------------------------------------------------------------------
  /**
   * Return variable.
   */
  public variable GetV() { return v; }
//------------------------------------------------------------------------------
  /**
   * Return pointer "ptr".
   */
  public int GetPtr() { return ptr; }
//------------------------------------------------------------------------------
  /**
   * Return true (all KBCT inputs are active).
   */
  public boolean GetActive() { return jnikbct.GetInputActive( ptr ); }
//------------------------------------------------------------------------------
  /**
   * Set variable name.
   */
  public void SetName(String name) { v.SetName(name); }
//------------------------------------------------------------------------------
  /**
   * Return variable name.
   */
  public String GetName() { return v.GetName(); }
//------------------------------------------------------------------------------
  /**
   * Set Ontology flag.
   */
  public void SetFlagOntology(boolean f) { v.SetFlagOntology(f); }
//------------------------------------------------------------------------------
  /**
   * Return Ontology flag.
   */
  public boolean GetFlagOntology() { return v.GetFlagOntology(); }
//------------------------------------------------------------------------------
  /**
   * Set variable name.
   */
  public void SetNameFromOntology(String name) { v.SetNameFromOntology(name); }
//------------------------------------------------------------------------------
  /**
   * Return variable name.
   */
  public String GetNameFromOntology() { return v.GetNameFromOntology(); }
//------------------------------------------------------------------------------
  /**
   * Set OntDatatypeProperty flag.
   */
  public void SetFlagOntDatatypeProperty(boolean f) { v.SetFlagOntDatatypeProperty(f); }
//------------------------------------------------------------------------------
  /**
   * Return OntDatatypeProperty flag.
   */
  public boolean GetFlagOntDatatypeProperty() { return v.GetFlagOntDatatypeProperty(); }
//------------------------------------------------------------------------------
  /**
   * Set OntObjectProperty flag.
   */
  public void SetFlagOntObjectProperty(boolean f) { v.SetFlagOntObjectProperty(f); }
//------------------------------------------------------------------------------
  /**
   * Return OntObjectProperty flag.
   */
  public boolean GetFlagOntObjectProperty() { return v.GetFlagOntObjectProperty(); }
//------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Set input physical range:
   * </p>
   * <ul>
   *   <li> range[0] -> min </li>
   *   <li> range[1] -> max </li>
   * </ul>
   */
  public void SetInputPhysicalRange(double[] range) {
    v.SetInputPhysicalRange(range[0], range[1]);
  }
//------------------------------------------------------------------------------
  /**
   * Set input physical range
   */
  public void SetInputPhysicalRange(double range_min, double range_max) {
    double [] r = new double[2];
    r[0]= range_min;
    r[1]= range_max;
    this.SetInputPhysicalRange(r);
  }
//------------------------------------------------------------------------------
  /**
   * Return input physical range.
   */
  public double[] GetInputPhysicalRange() {
    return v.GetInputPhysicalRange();
  }
//------------------------------------------------------------------------------
  /**
   * <p align="left">
   * Set input interest range:
   * </p>
   * <ul>
   *   <li> range[0] -> min </li>
   *   <li> range[1] -> max </li>
   * </ul>
   */
  public void SetInputInterestRange(double[] range) {
    v.SetInputInterestRange(range[0], range[1]);
  }
//------------------------------------------------------------------------------
  /**
   * Set input interest range
   */
  public void SetInputInterestRange(double range_min, double range_max) {
    v.SetInputInterestRange(range_min, range_max);
  }
//------------------------------------------------------------------------------
  /**
   * Return input interest range.
   */
  public double[] GetInputInterestRange() {
    return v.GetInputInterestRange();
  }
//------------------------------------------------------------------------------
  /**
   * Set TRUST (low, middle, high) of expert about this variable.
   */
  public void SetTrust(String imp) { v.SetTrust(imp); }
//------------------------------------------------------------------------------
  /**
   * Return TRUST (low, middle, high) of expert about this variable.
   */
  public String GetTrust() { return v.GetTrust(); }
//------------------------------------------------------------------------------
  /**
   * Set variable type (numerical, categorical, logical)
   */
  public void SetType(String vt) { v.SetType(vt); }
//------------------------------------------------------------------------------
  /**
   * Return variable type (numerical, categorical, logical).
   */
  public String GetType() { return v.GetType(); }
//------------------------------------------------------------------------------
  /**
   * Set number of labels.
   */
  public void SetLabelsNumber(int NOL) { v.SetLabelsNumber(NOL); }
//------------------------------------------------------------------------------
  /**
   * Return number of labels.
   */
  public int GetLabelsNumber() { return v.GetLabelsNumber(); }
//------------------------------------------------------------------------------
  /**
   * Set modal point "new_MP" to label "label_number".
   */
  public void SetMP(int label_number, String new_MP, boolean opt) {
    v.SetMP(label_number, new_MP, opt);
  }
//------------------------------------------------------------------------------
  /**
   * Return modal point of label "label_number".
   */
  public String GetMP(int label_number) {
    return v.GetMP(label_number);
  }
//------------------------------------------------------------------------------
  /**
   * Set modal point "new_MP" to label "label_number".
   */
  public void SetMPaux(int label_number, String new_MP) {
    v.SetMPaux(label_number, new_MP);
  }
//------------------------------------------------------------------------------
  /**
   * Return modal point of label "label_number".
   */
  public String GetMPaux(int label_number) {
    return v.GetMPaux(label_number);
  }
//------------------------------------------------------------------------------
  /**
   * Set names for a new scale of labels (defined by the user).
   */
  public void SetNewScale(String[] names) { v.SetNewScale(names); }
//------------------------------------------------------------------------------
  /**
   * Set name of scale of labels.
   * (low-high, few-lot, negative-positive, ...)
   */
  public void SetScaleName(String new_LabelName) {
    v.SetScaleName(new_LabelName);
  }
//------------------------------------------------------------------------------
  /**
   * Return name of scale of labels.
   * (low-high, few-lot, negative-positive, ...)
   */
  public String GetScaleName() { return v.GetScaleName(); }
//------------------------------------------------------------------------------
  /**
   * Set name to label "label_number".
   */
  public void SetLabelsName(int label_number, String name) {
    v.SetLabelsName(label_number, name);
  }
//------------------------------------------------------------------------------
  /**
   * Return an array of String with names of labels.
   */
  public String[] GetLabelsName() { return v.GetLabelsName(); }
//------------------------------------------------------------------------------
  /**
   * Return the name of label "Label_Number".
   */
  public String GetLabelsName(int Label_Number) {
    if (Label_Number<0)
      return "";
    else
      return v.GetLabelsName()[Label_Number];
  }
//------------------------------------------------------------------------------
  /**
   * Set user-defined name to label "label_number".
   */
  public void SetUserLabelsName(int label_number, String name) {
    v.SetUserLabelsName(label_number, name);
  }
//------------------------------------------------------------------------------
  /**
   * Return an array of String with "user scale" names of labels.
   */
  public String[] GetUserLabelsName() {
    return v.GetUserLabelsName();
  }
//------------------------------------------------------------------------------
  /**
   * Return the name of label "Label_Number".
   */
  public String GetUserLabelsName(int Label_Number) {
    if (Label_Number<0)
      return "";
    else
      return v.GetUserLabelsName()[Label_Number];
  }
//------------------------------------------------------------------------------
  /**
   * Set default names of OR labels.
   */
  public void SetORLabelsName() { v.SetORLabelsName(); }
//------------------------------------------------------------------------------
  /**
   * Return an array of String with names of OR labels.
   */
  public String[] GetORLabelsName() { return v.GetORLabelsName(); }
//------------------------------------------------------------------------------
  /**
   * Return the name of OR label "Label_Number".
   */
  public String GetORLabelsName(int Label_Number) {
    return v.GetORLabelsName()[Label_Number];
  }
//------------------------------------------------------------------------------
  /**
   * Add label "e".
   */
  public void AddLabel(LabelKBCT e) { v.AddLabel(e); }
//------------------------------------------------------------------------------
  /**
   * Return label "mf_number".
   */
  public LabelKBCT GetLabel(int mf_number) { return v.GetLabel(mf_number); }
//------------------------------------------------------------------------------
  /**
   * Remove label "mf_number".
   */
  public void RemoveLabel(int mf_number, boolean expand) {
    v.RemoveLabel(mf_number, expand);
  }
//------------------------------------------------------------------------------
  /**
   * Replace label "mf_number" by new label "new_e".
   */
  public void ReplaceLabel(int mf_number, LabelKBCT new_e) {
    v.ReplaceLabel(mf_number, new_e);
  }
//------------------------------------------------------------------------------
  /**
   * Set "Classif" option (only used by outputs).
   */
  public void SetClassif(String c) {
	  //System.out.println("JVariable: SetClassif -> "+c);
	  this.v.SetClassif(c); 
  }
//------------------------------------------------------------------------------
  /**
   * Return "Classif" option (only used by outputs).
   */
  public String GetClassif() { return this.v.GetClassif(); }
//------------------------------------------------------------------------------
  /**
   * Add label name to vector of labels names.
   */
  public void AddMFLabelName(String LN) { v.AddMFLabelName(LN); }
//------------------------------------------------------------------------------
  /**
   * Return vector of labels names.
   */
  public Object[] GetMFLabelNames() { return v.GetMFLabelNames(); }
//------------------------------------------------------------------------------
  /**
   * Init vector for labels names.
   */
  public void initMFLabelNames() { v.initMFLabelNames(); }
//------------------------------------------------------------------------------
  /**
   * Init vector for labels names.
   */
  public void InitLabelsName(int n) { v.InitLabelsName(n); }
//------------------------------------------------------------------------------
  /**
   * Set default names of labels.
   */
  public void SetLabelsName() { 
    v.SetLabelsName();
  }
//------------------------------------------------------------------------------
  /**
   * Set properties of labels.
   */
  public void SetLabelProperties() { v.SetLabelProperties(); }
//------------------------------------------------------------------------------
  /**
   * Return true if variable type is INPUT.
   */
  public boolean isInput() { return this.input; }
//------------------------------------------------------------------------------
  /**
   * Return true if variable type is OUTPUT.
   */
  public boolean isOutput() { return this.output; }
//------------------------------------------------------------------------------
  /**
   */
  public int GetLabelFired(double d) { return v.GetLabelFired(d); }
}
