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
//                         InfoConsistency.java
//
//
//**********************************************************************

package kbct;

/**
 * Additional information to generate error and solution messages of consistency
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

public class InfoConsistency {
  private String message1= null;
  private String message2= null;
  private String error= null;
  private String solution= null;
  private int RuleNumber1= 0;
  private int RuleNumber2= 0;
  private int VariableNumber= 0;
  private String VariableType= null;
  private boolean RemoveVariable= false;
  private int LabelNumber= -1;
  private String LabelName= null;
  private int RuleNumberToRemove= 0;
  private boolean user= false;
  private int[] GroupLabels= null;
  //----------------------------------------------------------------------------
  /**
   * Set message "msg" related to conflict.
   */
  public void setMessage1(String msg) { this.message1= msg; }
  //----------------------------------------------------------------------------
  /**
   * Return message related to conflict.
   */
  public String getMessage1() { return this.message1; }
  //----------------------------------------------------------------------------
  /**
   * Set message "msg" related to conflict.
   */
  public void setMessage2(String msg) { this.message2= msg; }
  //----------------------------------------------------------------------------
  /**
   * Return message related to conflict.
   */
  public String getMessage2() { return this.message2; }
  //----------------------------------------------------------------------------
  /**
   * Set message "msg" in order to solve conflict.
   */
  public void setError(String msg) { this.error= msg; }
  //----------------------------------------------------------------------------
  /**
   * Return message "solution" in order to solve conflict.
   */
  public String getError() { return this.error; }
  //----------------------------------------------------------------------------
  /**
   * Set message "msg" in order to solve conflict.
   */
  public void setSolution(String msg) { this.solution= msg; }
  //----------------------------------------------------------------------------
  /**
   * Return message "solution" in order to solve conflict.
   */
  public String getSolution() { return this.solution; }
  //----------------------------------------------------------------------------
  /**
   * Set number of rule r1.
   */
  public void setRuleNum1(int r1) { this.RuleNumber1= r1; }
  //----------------------------------------------------------------------------
  /**
   * Return number of rule r1.
   */
  public int getRuleNum1() { return this.RuleNumber1; }
  //----------------------------------------------------------------------------
  /**
   * Set number of rule r2.
   */
  public void setRuleNum2(int r2) { this.RuleNumber2= r2; }
  //----------------------------------------------------------------------------
  /**
   * Return number of rule r2.
   */
  public int getRuleNum2() { return this.RuleNumber2; }
  //----------------------------------------------------------------------------
  /**
   * Set number of variable v.
   */
  public void setVarNum(int v) { this.VariableNumber= v; }
  //----------------------------------------------------------------------------
  /**
   * Return number of variable v.
   */
  public int getVarNum() { return this.VariableNumber; }
  //----------------------------------------------------------------------------
  /**
   * Set type of variable var: Input or Output
   */
  public void setVarType(String var) { this.VariableType= var; }
  //----------------------------------------------------------------------------
  /**
   * Return type of variable var: "Input" or "Output"
   */
  public String getVarType() { return this.VariableType; }
  //----------------------------------------------------------------------------
  /**
   * Set FLAG to "true" or "false" in order to propose either remove or nor the rule r.
   */
  public void setRemoveVar(boolean r) { this.RemoveVariable= r; }
  //----------------------------------------------------------------------------
  /**
   * Return FLAG "RemoveVariable": "true" or "false" in order to propose either remove or nor the rule r.
   */
  public boolean isRemoveVar() { return this.RemoveVariable; }
  //----------------------------------------------------------------------------
  /**
   * Set FLAG to "true" or "false" in order to know if label is defined by user.
   */
  public void setUser(boolean u) { this.user= u; }
  //----------------------------------------------------------------------------
  /**
   * Return FLAG "user": "true" or "false" in order to know if label is defined by user.
   */
  public boolean isUser() { return this.user; }
  //----------------------------------------------------------------------------
  /**
   * Set number of label l.
   */
  public void setLabelNum(int l) { this.LabelNumber= l; }
  //----------------------------------------------------------------------------
  /**
   * Return number of label l.
   */
  public int getLabelNum() { return this.LabelNumber; }
  //----------------------------------------------------------------------------
  /**
   * Set name of label l.
   */
  public void setLabelName(String l) { this.LabelName= l; }
  //----------------------------------------------------------------------------
  /**
   * Return name of label l.
   */
  public String getLabelName() { return this.LabelName; }
  //----------------------------------------------------------------------------
  /**
   * Set number of rules to remove: 1 or 2.
   */
  public void setRuleNumToRemove(int r) { this.RuleNumberToRemove= r; }
  //----------------------------------------------------------------------------
  /**
   * Return number of rules to remove: 1 or 2.
   */
  public int getRuleNumToRemove() { return this.RuleNumberToRemove; }
  //----------------------------------------------------------------------------
  /**
   * Set GroupLabels (array with numbers of labels to group).
   */
  public void setGroupLabels(int[] gl) { this.GroupLabels= gl; }
  //----------------------------------------------------------------------------
  /**
   * Return GroupLabels (array with numbers of labels to group).
   */
  public int[] getGroupLabels() { return this.GroupLabels; }
}
