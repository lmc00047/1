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
//                              JKBCT.java
//
//
//**********************************************************************
package kbct;

import java.util.Vector;

import KB.Rule;
import fis.JSemaphore;

/**
 * kbct.JKBCT is used in order to generate and modify a Knowledge Base.
 * It calls to jnikbct methods.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

//------------------------------------------------------------------------------
public class JKBCT {
  private long ptr;
  private String KBCTFile = null;
  private Vector<JSemaphore> InputsSemaphore = new Vector<JSemaphore>();
  private Vector<JSemaphore> OutputsSemaphore = new Vector<JSemaphore>();
  protected long CopyKBCTPtr = 0;
  private Vector<KBCTListener> Listeners = new Vector<KBCTListener>();
//------------------------------------------------------------------------------
  /**
   * Generate a new KBCT. Default name is "New Knowledge Base"
   */
  public JKBCT() {
    this.ptr = jnikbct.NewKBCT();
    this.SetName(LocaleKBCT.GetString("NewKBCT"));
    this.CopyKBCTPtr = jnikbct.NewKBCT(this.ptr);
  }
//------------------------------------------------------------------------------
  /**
   * Generate a new KBCT. KBCT name is "KBCTFile"
   */
  public JKBCT(String KBCTFile) {
    try {
      this.ptr = jnikbct.NewKBCT(KBCTFile);
    } catch (Throwable t) {
       t.printStackTrace();
    }
    this.KBCTFile = KBCTFile;
    if (!this.KBCTFile.endsWith("xml")) {
	    this.KBCTFile= this.KBCTFile +".xml";
    }
    /*if (this.KBCTFile.endsWith("xml")) {
    	this.KBCTFile= this.KBCTFile.substring(0,this.KBCTFile.length()-4);
    	System.out.println("KBCTFile= "+this.KBCTFile);
    	//System.out.println("NbInputs= "+this.GetNbInputs());
    }*/

    for (int i = 0; i < this.GetNbInputs(); i++)
      this.InputsSemaphore.add(new JSemaphore());

    for (int i = 0; i < this.GetNbOutputs(); i++)
      this.OutputsSemaphore.add(new JSemaphore());

    this.CopyKBCTPtr = jnikbct.NewKBCT(this.ptr);
  }
//------------------------------------------------------------------------------
  /**
   * Generate a new KBCT. It is a clone of "kbct".
   */
  public JKBCT(JKBCT kbct) {
    this.ptr = jnikbct.NewKBCT(kbct.ptr);
    for (int i = 0; i < this.GetNbInputs(); i++)
      this.InputsSemaphore.add(new JSemaphore());

    for (int i = 0; i < this.GetNbOutputs(); i++)
      this.OutputsSemaphore.add(new JSemaphore());

    this.CopyKBCTPtr = jnikbct.NewKBCT(this.ptr);
  }
//------------------------------------------------------------------------------
  /**
   * Generate a new KBCT. KBCT pointer is "ptr".
   */
  protected JKBCT(long ptr) throws Throwable {
    this.ptr = ptr;
    for (int i = 0; i < this.GetNbInputs(); i++)
      this.InputsSemaphore.add(new JSemaphore());

    for (int i = 0; i < this.GetNbOutputs(); i++)
      this.OutputsSemaphore.add(new JSemaphore());
  }
//------------------------------------------------------------------------------
  /**
   * Remove this JKBCT.
   */
  public void Delete() {
    try {
      jnikbct.DeleteKBCT(this.ptr);
      this.ptr = 0;
      jnikbct.DeleteKBCT(this.CopyKBCTPtr);
      this.CopyKBCTPtr = 0;
    }
    catch (Throwable t) {}
  }
//------------------------------------------------------------------------------
  /**
   * Store this JKBCT in a file, whose name is "KBCTFile".
   */
  public void Save(String KBCTFile, boolean TEST) throws Throwable {
    jnikbct.SaveKBCT(ptr, KBCTFile);
    if (!TEST) {
      this.KBCTFile = KBCTFile;
      this.CopyKBCTPtr = jnikbct.NewKBCT(this.ptr);
    }
  }
//------------------------------------------------------------------------------
  /**
   * Save this JKBCT as FIS configuration file.
   */
  public JKBCT SaveFIS(String FISFile) throws Throwable {
    return jnikbct.SaveFIS(this.KBCTFile, FISFile);
  }
//------------------------------------------------------------------------------
  /**
   * Save this JKBCT as FIS configuration file.
   */
  public JKBCT SaveFISquality(String FISFile) throws Throwable {
    return jnikbct.SaveFISquality(this.KBCTFile, FISFile);
  }
//------------------------------------------------------------------------------
  /**
   * Store a linguistic summaru of JKBCT in a HTML file, whose name is "HTMLFile".
   */
  public void SaveLinguisticSummary(String HTMLFile) throws Throwable {
    jnikbct.SaveLinguisticSummary(ptr, HTMLFile);
  }
//------------------------------------------------------------------------------
  /**
   * Store current JKBCT.
   */
  public void Save() throws Throwable { this.Save(this.KBCTFile, false); }
//------------------------------------------------------------------------------
  /**
   * Set the name of this JKBCT.
   */
  public void SetName(String name) { jnikbct.SetName(ptr, name); }
//------------------------------------------------------------------------------
  /**
   * Return the name of this JKBCT.
   */
  public String GetName() { return jnikbct.GetName(ptr); }
//------------------------------------------------------------------------------
  /**
   * Check changes.
   */
  public boolean Modified() {
    if (this.CopyKBCTPtr == 0) {
      return true;
    }
    return!jnikbct.EqualKBCT(this.ptr, this.CopyKBCTPtr);
  }
//------------------------------------------------------------------------------
  /**
   * Return number of inputs in this JKBCT.
   */
  public int GetNbInputs() { return jnikbct.GetNbInputs(ptr); }
//------------------------------------------------------------------------------
  /**
   * Return number of inputs in this JKBCT.
   */
  public int GetNbActiveInputs() { return jnikbct.GetNbActiveInputs(ptr); }
//------------------------------------------------------------------------------
  /**
   * Return number of outputs in this JKBCT.
   */
  public int GetNbOutputs() { return jnikbct.GetNbOutputs(ptr); }
//------------------------------------------------------------------------------
  /**
   * Return number of rules in this JKBCT.
   */
  public int GetNbRules() { return jnikbct.GetNbRules(ptr); }
//------------------------------------------------------------------------------
  /**
   * Return a vector with 1 (rule to be expanded) and 0 (rule to keep the same).
   */
  public int[] GetRulesToBeExpanded() { return jnikbct.GetRulesToBeExpanded(ptr); }
//------------------------------------------------------------------------------
  /**
   * Return number of active rules in this JKBCT.
   */
  public int GetNbActiveRules() { return jnikbct.GetNbActiveRules(ptr); }
//------------------------------------------------------------------------------
  /**
   * Return the input "input_number".
   */
  public String GetRuleDescription(int rule_number) {
    return jnikbct.GetRuleDescription(ptr, rule_number, "NULL");
  }
//------------------------------------------------------------------------------
  /**
   * Return the input "input_number".
   */
  public String GetRuleDescription(int rule_number, String fingramViewer) {
    return jnikbct.GetRuleDescription(ptr, rule_number, fingramViewer);
  }
//------------------------------------------------------------------------------
  /**
   * Return the input "input_number".
   */
  public JKBCTInput GetInput(int input_number) {
    return new JKBCTInput(jnikbct.GetInput(ptr, input_number), input_number+1);
  }
//------------------------------------------------------------------------------
  /**
   * This method is used in FisPro. In KBCT all variables are actives
   */
  public JKBCTInput GetActiveInput(int active_number) {
    for (int i = 0, active = 0; i < this.GetNbInputs(); i++) {
      JKBCTInput input = this.GetInput(i);
      if (input.GetActive() == true) {
        if (active == active_number)
          return input;
        else
          active++;
      }
    }
    return null;
  }
//------------------------------------------------------------------------------
  /**
   * Return the output "output_number".
   */
  public JKBCTOutput GetOutput(int output_number) {
    return new JKBCTOutput(jnikbct.GetOutput(ptr, output_number), output_number+1);
  }
//------------------------------------------------------------------------------
  /**
   * This method is used in FisPro. In KBCT all variables are actives
   */
  public JKBCTOutput GetActiveOutput(int active_number) {
    for (int i = 0, active = 0; i < this.GetNbOutputs(); i++) {
      JKBCTOutput output = this.GetOutput(i);
      if (output.GetActive() == true) {
        if (active == active_number)
          return output;
        else
          active++;
      }
    }
    return null;
  }
//------------------------------------------------------------------------------
  /**
   * Return the Rule "rule_number".
   */
  public Rule GetRule(int rule_number) {
    return jnikbct.GetRule(ptr, rule_number);
  }
//------------------------------------------------------------------------------
  /**
   * Return rule position of "r" in knowledge base.
   */
  public int GetRulePosition(Rule r) {
    return jnikbct.GetRulePosition(ptr, r);
  }
//------------------------------------------------------------------------------
  /**
   * Set "Active" rules rule_number.
   */
  public void SetRuleActive( int rule_number, boolean active ) {
    jnikbct.SetRuleActive(this.ptr, rule_number, active);
  }
//------------------------------------------------------------------------------
  /**
   * Add "input" to the Knowledge Base.
   */
  public void AddInput(JKBCTInput input) {
	jnikbct.AddInput(ptr, input.GetV());
    this.InputsSemaphore.add(new JSemaphore());
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).InputAdded(this.GetNbInputs() - 1);
  }
//------------------------------------------------------------------------------
  /**
   * Add "output" to the Knowledge Base.
   */
  public void AddOutput(JKBCTOutput output) {
    jnikbct.AddOutput(ptr, output.GetV());
    this.OutputsSemaphore.add(new JSemaphore());
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).OutputAdded(this.GetNbOutputs() - 1);
  }
//------------------------------------------------------------------------------
  /**
   * Delete the input "input_number".
   */
  public void RemoveInput(int input_number) {
    this.InputsSemaphore.removeElementAt(input_number-1);
	jnikbct.RemoveInput(this.ptr, input_number);
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).InputRemoved(input_number);
  }
//------------------------------------------------------------------------------
  /**
   * Replace the input "input_number" by "new_input".
   */
  public void ReplaceInput(int input_number, JVariable new_input) {
    jnikbct.ReplaceInput(this.ptr, input_number-1, new_input.GetV());
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).InputReplaced((int)input_number);
  }
//------------------------------------------------------------------------------
  /**
   * Delete the output "output_number".
   */
  public void RemoveOutput(int output_number) {
    this.OutputsSemaphore.removeElementAt(output_number-1);
    jnikbct.RemoveOutput(this.ptr, output_number);
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).OutputRemoved(output_number);
  }
//------------------------------------------------------------------------------
  /**
   * Replace the output "output_number" by "new_output".
   */
  public void ReplaceOutput(int output_number, JVariable new_output) {
    jnikbct.ReplaceOutput(this.ptr, output_number-1, new_output.GetV());
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).OutputReplaced((int)output_number);
  }
//------------------------------------------------------------------------------
  /**
   * Add Rule "rule" to Knowledge Base.
   */
  public void AddRule(Rule rule) { jnikbct.AddRule(ptr, rule); }
//------------------------------------------------------------------------------
  /**
   * Replace Rule "rule_number" by new Rule "rule".
   */
  public void ReplaceRule(int rule_number, Rule rule) {
    jnikbct.ReplaceRule(this.ptr, rule_number, rule);
  }
//------------------------------------------------------------------------------
  /**
   * Delete Rule "rule_number".
   */
  public void RemoveRule(int rule_number) {
    jnikbct.RemoveRule(ptr, rule_number);
  }
//------------------------------------------------------------------------------
  /**
   * Return the JSemaphore for input "input_number".
   */
  public JSemaphore GetInputSemaphore(int input_number) {
    return (JSemaphore)this.InputsSemaphore.elementAt(input_number);
  }
//------------------------------------------------------------------------------
  /**
   * Return the JSemaphore for output "output_number".
   */
  public JSemaphore GetOutputSemaphore(int output_number) {
    return (JSemaphore)this.OutputsSemaphore.elementAt(output_number);
  }
//------------------------------------------------------------------------------
  /**
   * Add KBCT Listener.
   */
  public void AddKBCTListener(KBCTListener listener) {
    this.Listeners.add(listener);
  }
//------------------------------------------------------------------------------
  /**
   * Remove KBCT Listener.
   */
  public void RemoveKBCTListener(KBCTListener listener) {
    this.Listeners.remove(listener);
  }
//------------------------------------------------------------------------------
  /**
   * Warning of closing current knowledge base.
   */
  public void Close() {
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).KBCTClosed();
  }
//------------------------------------------------------------------------------
  /**
   * Warning of setting "Active" for input "input_number".
   */
  public void InputActiveChanged(int input_number) {
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).InputActiveChanged(input_number);
  }
//------------------------------------------------------------------------------
  /**
   * Warning of setting "Active" for output "output_number".
   */
  public void OutputActiveChanged(int output_number) {
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).OutputActiveChanged(output_number);
  }
//------------------------------------------------------------------------------
  /**
   * Warning of changing "Name" for input "input_number".
   */
  public void InputNameChanged(int input_number) {
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).InputNameChanged(input_number);
  }
//------------------------------------------------------------------------------
  /**
   * Warning of changing "Name" for output "output_number".
   */
  public void OutputNameChanged(int output_number) {
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).OutputNameChanged(output_number);
  }
//------------------------------------------------------------------------------
  /**
   * Warning of changing "Physical Range" for input "input_number".
   */
  public void InputPhysicalRangeChanged(int input_number) {
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).InputPhysicalRangeChanged(input_number);
  }
//------------------------------------------------------------------------------
  /**
   * Warning of changing "Interest Range" for input "input_number".
   */
  public void InputInterestRangeChanged(int input_number) {
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).InputInterestRangeChanged(input_number);
  }
//------------------------------------------------------------------------------
  /**
   * Warning of changing "Physical Range" for output "output_number".
   */
  public void OutputPhysicalRangeChanged(int output_number) {
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).OutputPhysicalRangeChanged(output_number);
  }
//------------------------------------------------------------------------------
  /**
   * Warning of changing "Interest Range" for output "output_number".
   */
  public void OutputInterestRangeChanged(int output_number) {
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).OutputInterestRangeChanged(output_number);
  }
//------------------------------------------------------------------------------
  /**
   * Warning of removing label "mf_number" for input "input_number".
   */
  public void RemoveMFInInput(int input_number, int mf_number) {
    jnikbct.RemoveMFInInput(this.ptr, input_number, mf_number);
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).MFRemovedInInput(input_number, mf_number);
  }
//------------------------------------------------------------------------------
  /**
   * Warning of removing label "mf_number" for output "output_number".
   */
  public void RemoveMFInOutput(int output_number, int mf_number) {
    jnikbct.RemoveMFInOutput(this.ptr, output_number, mf_number);
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).MFRemovedInOutput(output_number, mf_number);
  }
//------------------------------------------------------------------------------
  /**
   * Warning of adding label for input "input_number".
   */
  public void MFAddedInInput(int input_number) {
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).MFAddedInInput(input_number);
  }
//------------------------------------------------------------------------------
  /**
   * Warning of adding label for output "output_number".
   */
  public void MFAddedInOutput(int output_number) {
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).MFAddedInOutput(output_number);
  }
//------------------------------------------------------------------------------
  /**
   * Warning of replacing label for input "input_number".
   */
  public void MFReplacedInInput(int input_number) {
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).MFReplacedInInput(input_number);
  }
//------------------------------------------------------------------------------
  /**
   * Warning of replacing label for output "output_number".
   */
  public void MFReplacedInOutput(int output_number) {
    Object[] listeners = this.Listeners.toArray();
    for (int i = 0; i < listeners.length; i++)
      ( (KBCTListener) listeners[i]).MFReplacedInOutput(output_number);
  }
//------------------------------------------------------------------------------
  /**
   * Return number of active input.
   */
  public int GetActiveInputNumber(int active_number) {
    for (int i = 0, active = 0; i < this.GetNbInputs(); i++)
      if (this.GetInput(i).GetActive() == true) {
        if (active == active_number)
          return i;
        else
          active++;
      }
    return -1;
  }
//------------------------------------------------------------------------------
  /**
   * Return number of active output.
   */
  public int GetActiveOutputNumber(int active_number) {
    for (int i = 0, active = 0; i < this.GetNbOutputs(); i++)
      if (this.GetOutput(i).GetActive() == true) {
        if (active == active_number)
          return i;
        else
          active++;
      }
    return -1;
  }
//------------------------------------------------------------------------------
  /**
   * Return pointer "ptr".
   */
  public long GetPtr() { return this.ptr; }
//------------------------------------------------------------------------------
  /**
   * Return pointer "ptr_copy".
   */
  public long GetCopyPtr() { return this.CopyKBCTPtr; }
//------------------------------------------------------------------------------
  /**
   * Return "KBCTFile".
   */
  public String GetKBCTFile() { return this.KBCTFile; }
//------------------------------------------------------------------------------
  /**
   * Set "KBCTFile".
   */
  public void SetKBCTFile(String f) { this.KBCTFile= f; }
//------------------------------------------------------------------------------
  /**
   * Return "Listeners".
   */
  public Vector GetListeners() { return this.Listeners; }
//------------------------------------------------------------------------------
  /**
   * Return a list 11101101 where 1 means input used in the rule base.
   */
  public int[] GetInputsUsedInRuleBase() { 
	  return jnikbct.GetInputsUsedInRuleBase(ptr);
  }
}
