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
//                              Rule.java
//
//
//**********************************************************************

package KB;

/**
 * Define rules: premise (inputs) and conclusions (outputs)
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

public class Rule {
   int rule_number, N_inputs, N_outputs;
   int[] in_labels;
   int[] out_labels;
   //----------------------------------------------------------------------------
   /**
   * Exp:
   * <ul>
   *   <li> 0 -> expandido </li>
   *   <li> 1 -> no expandido </li>
   * </ul>
   */
   int[] in_labels_exp;
   int[] out_labels_exp;
   //----------------------------------------------------------------------------
   /**
   * <ul>
   *   <li> type= E (Defined by Expert) </li>
   *   <li> type= I (Induced from data) </li>
   *   <li> type= S (Simplified) </li>
   * </ul>
   */
   String type;
   //----------------------------------------------------------------------------
   /**
    * Active= false -> rule disabled
    */
   boolean Active;
   //----------------------------------------------------------------------------
   /**
    * <p align="left"> Generate a new rule. </p>
    * <p align="left">
    * Default values:
    * <ul>
    *   <li> Number of rule: 0 </li>
    *   <li> Number of inputs: 0 </li>
    *   <li> Number of outputs: 0 </li>
    *   <li> Type: E (Defined by Expert) </li>
    * </ul>
    * </p>
    */
   public Rule() {
     this.rule_number= 0;
     this.N_inputs= 0;
     this.N_outputs= 0;
     this.type= "E";
     this.Active= true;
   }
   //----------------------------------------------------------------------------
   /**
    * <p align="left"> Generate a new rule. </p>
    * <p align="left">
    * Values:
    * <ul>
    *   <li> Number of rule: rule_number </li>
    *   <li> Number of inputs: N_inputs </li>
    *   <li> Number of outputs: N_outputs </li>
    *   <li> Inputs: in_labels </li>
    *   <li> Outputs: out_labels </li>
    * </ul>
    * </p>
    * @param rule_number number of the rule
    * @param N_inputs number of premises
    * @param N_outputs number of conclusions
    * @param in_labels premises
    * @param out_labels conclusions
    * @param type type of rule (E/I/S)
    * @param active flag of rule activation
    */
   public Rule(int rule_number, int N_inputs, int N_outputs, int[] in_labels, int[] out_labels, String type, boolean active) {
     this.rule_number= rule_number;
     this.N_inputs= N_inputs;
     this.N_outputs= N_outputs;
     this.in_labels= in_labels;
     this.out_labels= out_labels;
     this.type= type;
     this.in_labels_exp= new int[N_inputs];
     for (int n=0; n< N_inputs; n++)
       this.in_labels_exp[n]=0;

     this.out_labels_exp= new int[N_outputs];
     for (int n=0; n< N_outputs; n++)
       this.out_labels_exp[n]=0;

     this.Active= active;
   }
   //----------------------------------------------------------------------------
   /**
    * Set the number of this rule.
    * @param rule_number number of the rule
    */
   public void SetNumber(int rule_number) {
     this.rule_number= rule_number;
   }
   //----------------------------------------------------------------------------
   /**
    * Return the number of this rule.
    * @return number of the rule
    */
   public int GetNumber() {
     return this.rule_number;
   }
   //----------------------------------------------------------------------------
   /**
    * Set selected label "label_number" in input "in_label_number".
    * @param label_number number of premise to modify
    * @param in_label_number new premise
    */
   public void SetInputLabel (int label_number, int in_label_number) {
     this.in_labels[label_number-1]= in_label_number;
   }
   //----------------------------------------------------------------------------
   /**
    * Return selected labels in inputs.
    * @return rule premises
    */
   public int[] Get_in_labels_number() {
     return in_labels;
   }
   //----------------------------------------------------------------------------
   /**
    * Set selected label "label_number" in output "out_label_number".
    * @param label_number number of conclusion to modify
    * @param out_label_number new conclusion
    */
   public void SetOutputLabel (int label_number, int out_label_number) {
     this.out_labels[label_number-1]= out_label_number;
   }
   //----------------------------------------------------------------------------
   /**
    * Return selected labels in outputs.
    * @return rule conclusions
    */
   public int[] Get_out_labels_number() {
     return out_labels;
   }
   //----------------------------------------------------------------------------
   /**
    * Set array with information about inputs in expanded rules.
    * @param a flags of expanded premises
    */
   public void Set_in_labels_exp(int[] a) {
     in_labels_exp= a;
   }
   //----------------------------------------------------------------------------
   /**
    * Return information about inputs in expanded rules.
    * @return flags of expanded premises
    */
   public int[] Get_in_labels_exp() {
     return in_labels_exp;
   }
   //----------------------------------------------------------------------------
   /**
    * Set array with information about outputs in expanded rules.
    * @param a flags of expanded conclusions
    */
   public void Set_out_labels_exp(int[] a) {
     out_labels_exp= a;
   }
   //----------------------------------------------------------------------------
   /**
    * Return information about outputs in expanded rules.
    * @return flags of expanded conclusions
    */
   public int[] Get_out_labels_exp() {
     return out_labels_exp;
   }
   //----------------------------------------------------------------------------
   /**
    * <p align="left">
    * Set the type of this rule.
    * <ul>
    *   <li> type= E (Defined by Expert) </li>
    *   <li> type= I (Induced from data) </li>
    *   <li> type= S (Simplified) </li>
    * </ul>
    * </p>
    * @param type type of rule (E/I/S)
    */
   public void SetType(String type) {
     this.type= type;
   }
   //----------------------------------------------------------------------------
   /**
    * <p align="left">
    * Return the type of this rule.
    * <ul>
    *   <li> type= E (Defined by Expert) </li>
    *   <li> type= I (Induced from data) </li>
    *   <li> type= S (Simplified) </li>
    * </ul>
    * </p>
    * @return type of rule (E/I/S)
    */
   public String GetType() {
     return this.type;
   }
   //----------------------------------------------------------------------------
   /**
    * Return the number of input variables in this rule.
    * @return number of premises
    */
   public int GetNbInputs() {
     return this.N_inputs;
   }
   //----------------------------------------------------------------------------
   /**
    * Return the number of output variables in this rule.
    * @return number of conclusions
    */
   public int GetNbOutputs() {
     return this.N_outputs;
   }
   //----------------------------------------------------------------------------
   /**
    * Set the rule Active.
    * @param active flag of rule activation
    */
   public void SetActive(boolean active) {
     this.Active= active;
   }
   //----------------------------------------------------------------------------
   /**
    * Return the Active of this rule.
    * @return flag of rule activation
    */
   public boolean GetActive() {
     return this.Active;
   }
}
