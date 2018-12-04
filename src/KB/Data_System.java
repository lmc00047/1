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
//                              Data_System.java
//
//
//**********************************************************************

package KB;

import java.util.Hashtable;

import kbct.LocaleKBCT;

  /**
   * Contains a Knowledge Base: inputs, outputs, rules
   *
   *@author     Jose Maria Alonso Moral
   *@version    3.0 , 03/08/15
   */

  public class Data_System {
    private String Name;
    private int Ninputs, Noutputs, Nrules, N_active_rules;
    private Hashtable<String,variable> inputs;
    private Hashtable<String,variable> outputs;
    private Hashtable<String,Rule> rules;
    private int conti, conto, contr;
    //----------------------------------------------------------------------------
    public Data_System() {
      Ninputs=0;
      Noutputs=0;
      Nrules=0;
      N_active_rules=0;
      Name= "";
      inputs= new Hashtable<String,variable>();
      outputs= new Hashtable<String,variable>();
      rules= new Hashtable<String,Rule>();
      conti=0;
      conto=0;
      contr=0;
    }
    //----------------------------------------------------------------------------
    /**
     * Set the name of the Knowledge Base.
     * @param n name of the knowledge base
     */
    public void SetName(String n) { Name=n; }
    //----------------------------------------------------------------------------
    /**
     * Return the name of the Knowledge Base.
     * @return name of the knowledge base
     */
    public String GetName() { return Name; }
    //----------------------------------------------------------------------------
    /**
     * Set the number of inputs.
     * @param n number of inputs
     */
    public void SetNbInputs(int n) { Ninputs=n; }
    //----------------------------------------------------------------------------
    /**
     * Return the number of inputs.
     * @return number of inputs
     */
    public int GetNbInputs() { return Ninputs; }
    //----------------------------------------------------------------------------
    /**
     * Return the number of active inputs.
     * @return number of inputs
     */
    public int GetNbActiveInputs() {
    	int res= Ninputs;
    	for (int n=0; n<Ninputs; n++) {
    		 if (!this.GetInput(n+1).isActive()) 
    			 res--;
    	}
    	return res;
    }
    //----------------------------------------------------------------------------
    /**
     * Set the number of outputs.
     * @param n number of outputs
     */
    public void SetNbOutputs(int n) { Noutputs=n; }
    //----------------------------------------------------------------------------
    /**
     * Return the number of outputs.
     * @return number of outputs
     */
    public int GetNbOutputs() { return Noutputs; }
    //----------------------------------------------------------------------------
    /**
     * Set the number of rules.
     * @param n number of rules
     */
    public void SetNbRules(int n) { Nrules=n; }
    //----------------------------------------------------------------------------
    /**
     * Return the number of rules.
     * @return number of rules
     */
    public int GetNbRules() { return Nrules; }
    //----------------------------------------------------------------------------
    /**
     * Add the variable "input" to the Knowledge Base.<br>
     * The number of inputs is increased in one.
     * @param input input variable to add
     */
    public void AddInput (variable input) {
      this.conti= this.conti+1;
      inputs.put(""+this.conti,input);
      String input_name= input.GetName();
      int n=0;
      int m= this.GetNbInputs()+2;
      while (n < this.GetNbInputs()) {
        if (input_name.equals(this.GetInput(n+1).GetName())) {
            input.SetName(LocaleKBCT.GetString("Input")+" "+m);
            input_name= input.GetName();
            m++;
            n=0;
        } else { n++; }
      }
      this.SetNbInputs(this.GetNbInputs()+1);
    }
    //----------------------------------------------------------------------------
    /**
     * Return the input "input_number".
     * @param input_number number of input to be returned
     * @return input "input_number"
     */
    public variable GetInput(long input_number) {
      return (variable)inputs.get(""+input_number);
    }
    //----------------------------------------------------------------------------
    /**
     * Add the variable "output" to the Knowledge Base.<br>
     * The number of outputs is increased in one.
     * @param output output variable to add
     */
    public void AddOutput (variable output) {
      this.conto= this.conto+1;
      outputs.put(""+this.conto,output);
      String output_name= output.GetName();
      int n=0;
      int m= this.GetNbOutputs()+2;
      while (n < this.GetNbOutputs()) {
        if (output_name.equals(this.GetOutput(n+1).GetName())) {
            output.SetName(LocaleKBCT.GetString("Output")+" "+m);
            output_name= output.GetName();
            m++;
            n=0;
        } else { n++; }
      }
      this.SetNbOutputs(this.GetNbOutputs()+1);
    }
    //----------------------------------------------------------------------------
    /**
     * Return the output "output_number".
     * @param output_number number of output to be returned
     * @return output "output_number"
     */
    public variable GetOutput(int output_number) {
      return (variable)outputs.get(""+output_number);
    }
    //----------------------------------------------------------------------------
    /**
     * Replace the input "input_number" by the new input "v".
     * @param input_number input variable to be replaced
     * @param v new input variable
     */
    public void ReplaceInput (long input_number, variable v) {
      inputs.put(""+input_number, v);
    }
    //----------------------------------------------------------------------------
    /**
     * Replace the output "output_number" by the new output "v".
     * @param output_number output variable to be replaced
     * @param v new output variable
     */
    public void ReplaceOutput (int output_number, variable v) {
      outputs.put(""+output_number, v);
    }
    //----------------------------------------------------------------------------
    /**
     * Delete the input "input_number".<br>
     * The number of inputs is decreased in one.
     * @param input_number input variable to be deleted
     */
    public void RemoveInput (int input_number) {
      inputs.remove(""+input_number);
      this.conti=input_number-1;
      int N_inputs= this.GetNbInputs()+1;
      this.SetNbInputs((int)this.conti);
      for (int n= input_number+1; n < N_inputs; n++) {
        variable v= this.GetInput(n);
        inputs.remove(""+n);
        this.AddInput(v);
      }
    }
    //----------------------------------------------------------------------------
    /**
     * Delete the output "output_number".<br>
     * The number of outputs is decreased in one.
     * @param output_number output variable to be deleted
     */
    public void RemoveOutput (int output_number) {
      outputs.remove(""+output_number);
      this.conto=output_number-1;
      int N_outputs= this.GetNbOutputs()+1;
      this.SetNbOutputs((int)this.conto);
      for (int n= output_number+1; n < N_outputs; n++) {
        variable v= this.GetOutput(n);
        outputs.remove(""+n);
        this.AddOutput(v);
      }
    }
    //----------------------------------------------------------------------------
    /**
     * Add the Rule "rule" to the Knowledge Base.<br>
     * The number of rules is increased in one.
     * @param rule Rule to be added
     */
    public void AddRule (Rule rule) {
      this.contr= this.contr+1;
      rule.SetNumber(this.contr);
      rules.put(""+this.contr,rule);
      this.SetNbRules(this.GetNbRules()+1);
      if (rule.Active)
        this.N_active_rules++;
    }
    //----------------------------------------------------------------------------
    /**
     * Return the Rule "rule_number".
     * @param rule_number number of rule to be returned
     * @return Rule "rule_number"
     */
    public Rule GetRule(int rule_number) {
      return (Rule)rules.get(""+rule_number);
    }
    //----------------------------------------------------------------------------
    /**
     * Replace the Rule "rule_number" by the new Rule "r".
     * @param rule_number Rule to be replaced
     * @param r new Rule
     */
    public void ReplaceRule (int rule_number, Rule r) {
      boolean old_active= this.GetRule(rule_number).GetActive();
      r.SetNumber(rule_number);
      rules.put(""+rule_number, r);
      boolean active= r.GetActive();
      if (old_active!=active) {
        if (active)
          this.N_active_rules++;
        else
          this.N_active_rules--;
      }
    }
    //----------------------------------------------------------------------------
    /**
     * Delete the Rule "rule_number".<br>
     * The number of rules is decreased in one.
     * @param rule_number Rule to be deleted
     */
    public void RemoveRule (int rule_number) {
      rule_number= rule_number+1;
      if (this.GetRule(rule_number).GetActive())
        this.N_active_rules--;

      rules.remove(""+rule_number);
      this.contr=rule_number-1;
      int N_rules= this.GetNbRules()+1;
      this.SetNbRules((int)this.contr);
      for (int n= rule_number+1; n < N_rules; n++) {
        Rule r= this.GetRule(n);
        r.SetNumber(r.GetNumber()-1);
        rules.remove(""+n);
        if (r.GetActive())
          this.N_active_rules--;

        this.AddRule(r);
      }
    }
    //----------------------------------------------------------------------------
    /**
     * Set the Rule "rule" to "Active".
     * @param rule_number Rule to be modified
     * @param active new state (true/false)
     */
    public void SetRuleActive (int rule_number, boolean active) {
      Rule r= this.GetRule(rule_number);
      boolean old_active= this.GetRule(rule_number).GetActive();
      r.SetActive(active);
      //System.out.println("DataSystem: SetRuleActive: "+rule_number);
      //System.out.println("DataSystem: SetRuleActive: "+active);
      if (old_active!=active) {
        if (active)
          this.N_active_rules++;
        else
          this.N_active_rules--;
      }
      this.ReplaceRule(rule_number, r);
    }
    //----------------------------------------------------------------------------
    /**
     * Return the number of active rules.
     * @return number of active rules
     */
    public int GetNbActiveRules() { return N_active_rules; }
    //----------------------------------------------------------------------------
    /**
     * Return a vector with N (rules to be expanded) and 0 (rule to keep the same).
     * @return number of active rules
     */
    public int[] GetRulesToBeExpanded() { 
        int[] result= new int[this.GetNbActiveRules()];
        int NbInputs= this.GetNbInputs();
        int[] NbOL= new int[NbInputs];
        for (int n=0; n<NbInputs; n++)
            NbOL[n]= this.GetInput(n+1).GetLabelsNumber();

        int cr=0;
        for (int n=0; n<this.GetNbRules(); n++) {
             Rule r= this.GetRule(n+1);
             if (r.GetActive()) {
                 int[] premises= r.Get_in_labels_number();
       	         int cp=0;
                 for (int m=0; m<premises.length; m++) {
        	          if ( (premises[m] > NbOL[m]+1) && (premises[m] < 2*NbOL[m]) ) {
        		          cp++;
        	          }
                 }
   	             if (cp==0)
	    	         result[cr]=0;
   	             else 
   	        	     result[cr]= (int)Math.pow(2,cp);
   	             
   	             cr++;
             }
        }
    	return result; 
    }
}