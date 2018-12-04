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
//                              JFIS.java
//
//
//**********************************************************************
package kbct;

import java.util.Vector;

import fis.FISListener;
import fis.JExtendedDataFile;
import fis.JInput;
import fis.JOutput;
import fis.JRule;
import fis.JSemaphore;
import fis.LinksItemsRules;
import fis.jnifis;

import fis.optimization.crossvalidation.CrossValidation;
import static org.apache.commons.io.FilenameUtils.concat;
import static org.apache.commons.lang.StringUtils.EMPTY;

/**
 * kbct.JFIS is used to represent a FisPro configuration file.
 * It calls to jnifis in order to invoke c++ functions of fisPro library.
 * It is used in order to make inductions (partitions and rules) and inferences.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */

//------------------------------------------------------------------------------
public class JFIS {
  private long ptr;
  private String FISFile = null;
  private Vector<JSemaphore> InputsSemaphore = new Vector<JSemaphore>();
  private Vector<JSemaphore> OutputsSemaphore = new Vector<JSemaphore>();
  private long CopyFISPtr = 0;
  private Vector<FISListener> Listeners = new Vector<FISListener>();
  private Vector<KBCTListener> KBCTListeners = new Vector<KBCTListener>();
//------------------------------------------------------------------------------
  public JFIS() throws Throwable {
    this.ptr = jnifis.NewFIS();
    this.SetName(LocaleKBCT.GetString("NewFIS"));
    this.SetConjunction(0);
    this.CopyFISPtr = jnifis.NewFIS(this.ptr);
  }
//------------------------------------------------------------------------------
  public JFIS( String FISFile ) throws Throwable {
    // true -> Ensure Output Limit Inference
    this.ptr = jnifis.NewFIS(FISFile, true);
    this.FISFile = FISFile;
    for( int i=0 ; i<this.NbInputs() ; i++ )
      this.InputsSemaphore.add(new JSemaphore());

    for( int i=0 ; i<this.NbOutputs() ; i++ )
      this.OutputsSemaphore.add(new JSemaphore());

    this.CopyFISPtr = jnifis.NewFIS(this.ptr);
  }
//------------------------------------------------------------------------------
  public JFIS( String FISFile, boolean cover ) throws Throwable {
    this.ptr = jnifis.NewFIS(FISFile, cover);
    this.FISFile = FISFile;
    for( int i=0 ; i<this.NbInputs() ; i++ )
      this.InputsSemaphore.add(new JSemaphore());

    for( int i=0 ; i<this.NbOutputs() ; i++ )
      this.OutputsSemaphore.add(new JSemaphore());

    this.CopyFISPtr = jnifis.NewFIS(this.ptr);
  }
//------------------------------------------------------------------------------
  public JFIS( JFIS fis ) throws Throwable {
    this.ptr = jnifis.NewFIS(fis.ptr);
    for( int i=0 ; i<this.NbInputs() ; i++ )
      this.InputsSemaphore.add(new JSemaphore());

    for( int i=0 ; i<this.NbOutputs() ; i++ )
      this.OutputsSemaphore.add(new JSemaphore());

    this.CopyFISPtr = jnifis.NewFIS(this.ptr);
  }
//------------------------------------------------------------------------------
  protected JFIS( long ptr ) throws Throwable {
    this.ptr = ptr;
    for( int i=0 ; i<this.NbInputs() ; i++ )
      this.InputsSemaphore.add(new JSemaphore());

    for( int i=0 ; i<this.NbOutputs() ; i++ ) {
      this.OutputsSemaphore.add(new JSemaphore());
      if( this.GetOutput(i).GetName().equals("") == true )
        this.GetOutput(i).SetName(LocaleKBCT.GetString("Output") + " " + Integer.toString(i+1) );
    }
  }
//------------------------------------------------------------------------------
  protected long NewFISFPA( fis.JExtendedDataFile data, int strategy, int min_card, double min_deg ) throws Throwable {
    return jnifis.NewFISFPA(this.ptr, data.ActiveFileName(), strategy, min_card, min_deg );
  }
//------------------------------------------------------------------------------
  protected long NewGENFIS( JExtendedDataFile data, boolean sort, double thres ) throws Throwable {
    String data_file = null;
    if( data != null )
      data_file = data.ActiveFileName();

    return jnifis.NewGENFIS(this.ptr, data_file, sort, thres );
  }
//------------------------------------------------------------------------------
//  public JFIS NewFISSIMPLE( JExtendedDataFile data, String validation_data, double homogeneity_threshold, double loss_performance, int output_number, int min_cardinality, double min_matching_degree, double loss_coverage, boolean keep_last, double max_err) throws Throwable {
//    return new JFIS(jnifis.NewFISSIMPLE(this.ptr, data.ActiveFileName(), validation_data, homogeneity_threshold, loss_performance, output_number, min_cardinality, min_matching_degree, loss_coverage, keep_last, max_err));
//  }
	protected JFIS NewFISSIMPLE( JExtendedDataFile data, String resultFile, double homogeneity_threshold, double loss_performance, int output_number, double min_matching_degree, double loss_coverage, double coverage_min, boolean keep_last, double max_err, boolean ruleRemoval, boolean variableRemoval, String validationFile) throws Throwable
	{
		return new JFIS(jnifis.NewFISSIMPLE(this.ptr, data.ActiveFileName(), resultFile, homogeneity_threshold, loss_performance, output_number, min_matching_degree, loss_coverage, coverage_min, keep_last, max_err, ruleRemoval, variableRemoval, validationFile));
	}
//------------------------------------------------------------------------------
  //public JFIS NewFISOPT( JExtendedDataFile data, String key, int iterations, double std_gauss, int constraints, int fails, double center_thres, int output_number, boolean without_redondancy, int seed_value, double sw1, double sw2, double sw3, int cardinal_vocabulary, double mu_thres ) throws Throwable {
    //long new_fis = jnifis.NewFISOPT(this.ptr, data.ActiveFileName(), key, iterations, std_gauss, constraints, fails, center_thres, output_number, without_redondancy, seed_value, sw1, sw2, sw3, cardinal_vocabulary, mu_thres);
	//return new_fis == 0 ? null : new JFIS(new_fis);
  //}
	//  public JFIS NewFISOPT( JExtendedDataFile data, String optimization_order, boolean output, boolean rules, boolean cardinal_vocabulary, int number_of_loops, double loss_coverage, int iterations, double std_gauss, int constraints, int fails, double center_thres, int output_number, boolean without_redondancy, int seed_value, double sw1, double sw2, double sw3, double mu_thres ) throws Throwable
		//{
		//	long new_fis = jnifis.NewFISOPT(this.ptr, data.ActiveFileName(), optimization_order, output, rules, cardinal_vocabulary, number_of_loops, loss_coverage, iterations, std_gauss, constraints, fails, center_thres, output_number, without_redondancy, seed_value, sw1, sw2, sw3, mu_thres);
		//	return new_fis == 0 ? null : new JFIS(new_fis);
		//}
	protected JFIS NewFISOPT( JExtendedDataFile data, String optimization_order, boolean output, boolean rules, int number_of_loops, double loss_coverage, int iterations, double std_gauss, int constraints, int fails, int output_number, int seed_value, double sw1, double sw2, double sw3, double mu_thres, CrossValidation crossValidation, double minBetweenMF) throws Throwable
	{
		long new_fis = jnifis.NewFISOPT(this.ptr, concat(MainKBCT.getConfig().GetKBCTFilePath(), EMPTY), data.ActiveFileName(), optimization_order, output, rules, number_of_loops, loss_coverage, iterations, std_gauss, constraints, fails, output_number, seed_value, sw1, sw2, sw3, mu_thres, crossValidation.isEnabled(), crossValidation.getPairSample().getLearningRatio(), crossValidation.getClassifSample().isClassif(), crossValidation.getClassifSample().getPrecision(), crossValidation.getClassifSample().getClassifVariable(), crossValidation.isEnabled() ? crossValidation.getPairSample().getPairNumber() : 0, minBetweenMF);
		return new_fis == 0 ? null : new JFIS(new_fis);
	}
// new
	//------------------------------------------------------------------------------
  public JFIS NewCustomFISOPT( JExtendedDataFile data, String key, int iterations, double std_gauss, int constraints, int fails, double center_thres, int output_number, boolean without_redondancy, int seed_value, double sw1, double sw2, double sw3, int cardinal_vocabulary, double mu_thres, double loss_coverage ) throws Throwable
	{
		long new_fis = jnifis.NewCustomFISOPT(this.ptr, data.ActiveFileName(), key, iterations, std_gauss, constraints, fails, center_thres, output_number, without_redondancy, seed_value, sw1, sw2, sw3, cardinal_vocabulary, mu_thres, loss_coverage);
		return new_fis == 0 ? null : new JFIS(new_fis);
	}
//------------------------------------------------------------------------------
  /*public JFIS [] NewTreeFIS( JExtendedDataFile data, String tree_file,  String valid_file, int maxtreedepth, double min_ed_gain, double min_significant, double ambiguity_thres, int outputNumber, boolean prune, boolean split, double relperfloss, boolean relgain, boolean display ) throws Throwable {
    String data_file = null;
    if( data != null )
      data_file = data.ActiveFileName();

    JFIS [] result = new JFIS[2];
    result[0] = result[1] = null;
    long [] long_fis = jnifis.NewTreeFIS(this.ptr,data_file,tree_file,valid_file,maxtreedepth,min_ed_gain,min_significant,ambiguity_thres,outputNumber,prune,split,relperfloss,relgain,display);
    if( long_fis[0] != 0 )
      result[0] = new JFIS(long_fis[0]);

    if( long_fis[1] != 0 )
      result[1] = new JFIS(long_fis[1]);

    return result;
  }*/
  /*public JFIS [] NewTreeFIS( JExtendedDataFile data, String tree_file,  String valid_file, int maxtreedepth, double min_ed_gain, double min_significant, int min_leaf_card, double ambiguity_thres, int outputNumber, boolean prune, boolean split, double relperfloss, boolean relgain, boolean display, double covThresh ) throws Throwable
	{
		String data_file = null;
		if( data != null )
			data_file = data.ActiveFileName();
		JFIS [] result = new JFIS[2];
		result[0] = result[1] = null;
		long [] long_fis = jnifis.NewTreeFIS(this.ptr, data_file, tree_file, valid_file, maxtreedepth, min_ed_gain, min_significant, ambiguity_thres,outputNumber,
				prune, split,relperfloss,relgain, display, covThresh, min_leaf_card);
		if( long_fis[0] != 0 )
			result[0] = new JFIS(long_fis[0]);
		if( long_fis[1] != 0 )
			result[1] = new JFIS(long_fis[1]);
		return result;
	}*/
	public JFIS [] NewTreeFIS( JExtendedDataFile data, String tree_file,  String valid_file, String resultFistreeFile, String perfFistreeFile, int maxtreedepth, double min_ed_gain, double min_significant, int min_leaf_card, double ambiguity_thres, int outputNumber, boolean prune, boolean split, double relperfloss, boolean relgain, boolean display, double covThresh ) throws Throwable
	{
		String data_file = null;
		if( data != null )
			data_file = data.ActiveFileName();
		JFIS [] result = new JFIS[2];
		result[0] = result[1] = null;
		long [] long_fis = jnifis.NewTreeFIS(this.ptr, data_file, tree_file, valid_file, resultFistreeFile, perfFistreeFile, maxtreedepth, min_ed_gain, min_significant, ambiguity_thres,outputNumber,
				prune, split,relperfloss,relgain, display, covThresh, min_leaf_card);
		if( long_fis[0] != 0 )
			result[0] = new JFIS(long_fis[0]);
		if( long_fis[1] != 0 )
			result[1] = new JFIS(long_fis[1]);
		return result;
	}
//------------------------------------------------------------------------------
  public void Links( JExtendedDataFile data, double threshold, boolean sort, String file_prefix, LinksItemsRules items_rules ) throws Throwable {
    jnifis.Links(this.ptr, data.ActiveFileName(), threshold, sort, file_prefix);
    if( items_rules != null )
      items_rules.LoadFile(file_prefix+"."+jnifis.LinksItemsRulesExtension());
  }
//------------------------------------------------------------------------------
  public void Delete() {
    try {
      jnifis.DeleteFIS( this.ptr );
      this.ptr = 0;
      jnifis.DeleteFIS( this.CopyFISPtr );
      this.CopyFISPtr = 0;
    } catch ( Throwable t ) {}
  }
//------------------------------------------------------------------------------
  public void Save( String FISFile ) throws Throwable {
    jnifis.SaveFIS( ptr, FISFile );
    this.FISFile = FISFile;
    this.CopyFISPtr = jnifis.NewFIS(this.ptr);
  }
//------------------------------------------------------------------------------
  public void Save() throws Throwable { this.Save(this.FISFile); }
//------------------------------------------------------------------------------
  public void SetConjunction( int index ) throws Throwable {
    jnifis.SetConjunction(ptr, TypeConjunction()[index]);
    // dispatch l'événement ConjunctionChanged
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).ConjunctionChanged();
  }
//------------------------------------------------------------------------------
  public void SetName( String name ) throws Throwable { jnifis.SetFISName(ptr, name); }
//------------------------------------------------------------------------------
  public String GetName() throws Throwable { return jnifis.GetFISName(ptr); }
//------------------------------------------------------------------------------
  public int GetConjunction() throws Throwable {
    String conj = jnifis.GetConjunction(ptr);
    String [] type_conjs = JFIS.TypeConjunction();
    for( int i=0 ; i<type_conjs.length ; i++ )
      if( conj.equals(type_conjs[i]) == true )
        return i;

    throw new Exception(LocaleKBCT.GetString("UnknownConjunction") + ": " + conj);
  }
//------------------------------------------------------------------------------
  static public String [] TypeConjunction() throws Throwable { return jnifis.TypeConjunction(); }
//------------------------------------------------------------------------------
  static public String [] TypeMissingValues() throws Throwable { return jnifis.TypeMissingValues(); }
//------------------------------------------------------------------------------
  public boolean Modified() throws Throwable {
    if( this.CopyFISPtr == 0 )
      return true;

    return !jnifis.EqualFIS(this.ptr, this.CopyFISPtr);
  }
//------------------------------------------------------------------------------
  public int NbInputs() throws Throwable { return jnifis.NbInputs( ptr ); }
//------------------------------------------------------------------------------
  public int NbActiveInputs() throws Throwable {
    int nb_active = 0;
    for( int i=0 ; i<this.NbInputs() ; i++ )
      if( this.GetInput(i).GetActive() == true )
        nb_active++;

    return nb_active;
  }
//------------------------------------------------------------------------------
  public int NbOutputs() throws Throwable { return jnifis.NbOutputs( ptr ); }
//------------------------------------------------------------------------------
  public int NbActiveOutputs() throws Throwable {
    int nb_active = 0;
    for( int i=0 ; i<this.NbOutputs() ; i++ )
      if( this.GetOutput(i).GetActive() == true )
        nb_active++;

    return nb_active;
  }
//------------------------------------------------------------------------------
  public int NbRules() throws Throwable { return jnifis.NbRules( ptr ); }
//------------------------------------------------------------------------------
  public int NbActiveRules() throws Throwable {
    int nb_active = 0;
    for( int i=0 ; i<this.NbRules() ; i++ )
      if( this.GetRule(i).GetActive() == true )
        nb_active++;

    return nb_active;
  }
//------------------------------------------------------------------------------
  public JInput GetInput( int input_number ) throws Throwable { return new JInput( jnifis.GetInput( ptr, input_number ) ); }
//------------------------------------------------------------------------------
  public JInput GetActiveInput( int active_number ) throws Throwable {
    for( int i=0, active=0 ; i<this.NbInputs() ; i++ ) {
      JInput input = this.GetInput(i);
      if( input.GetActive() == true ) {
        if( active == active_number )
          return input;
        else
          active++;
      }
    }
    return null;
  }
//------------------------------------------------------------------------------
  public JOutput GetOutput( int output_number ) throws Throwable { return new JOutput( jnifis.GetOutput( ptr, output_number ) ); }
//------------------------------------------------------------------------------
  public JOutput GetActiveOutput( int active_number ) throws Throwable {
    for( int i=0, active=0 ; i<this.NbOutputs() ; i++ ) {
      JOutput output = this.GetOutput(i);
      if( output.GetActive() == true ) {
        if( active == active_number )
          return output;
        else
          active++;
      }
    }
    return null;
  }
//------------------------------------------------------------------------------
  public JRule GetRule( int rule_number ) throws Throwable { return new JRule( jnifis.GetRule( ptr, rule_number ) ); }
//------------------------------------------------------------------------------
  public void AddInput( JInput input ) throws Throwable {
    jnifis.AddInput( ptr, input.Ptr() );
    this.InputsSemaphore.add(new JSemaphore());
    // dispatch l'événement InputAdded
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).InputAdded(this.NbInputs()-1);
  }
//------------------------------------------------------------------------------
  public void AddOutput( JOutput output ) throws Throwable {
    jnifis.AddOutput( ptr, output.Ptr() );
    this.OutputsSemaphore.add(new JSemaphore());
    // dispatch l'événement OutputAdded
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).OutputAdded(this.NbOutputs()-1);
  }
//------------------------------------------------------------------------------
  public void RemoveInput( int input_number ) throws Throwable {
    this.InputsSemaphore.removeElementAt(input_number);
    jnifis.RemoveInput( this.ptr, input_number );
    // dispatch l'événement InputRemoved
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).InputRemoved(input_number);
  }
//------------------------------------------------------------------------------
  public void ReplaceInput( int input_number, JInput new_input ) throws Throwable {
    jnifis.ReplaceInput( ptr, input_number, new_input.Ptr() );
    // dispatch l'événement InputChanged
    // dans la structure actuelle du programme une entrée d'un sif est modifié lors de l'appel à ReplaceInput
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).InputReplaced(input_number);
  }
//------------------------------------------------------------------------------
  public void RemoveOutput( int output_number ) throws Throwable {
    this.OutputsSemaphore.removeElementAt(output_number);
    jnifis.RemoveOutput( this.ptr, output_number );
    // dispatch l'événement OutputRemoved
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).OutputRemoved(output_number);
  }
//------------------------------------------------------------------------------
  public void ReplaceOutput( int output_number, JOutput new_output ) throws Throwable {
    jnifis.ReplaceOutput( ptr, output_number, new_output.Ptr() );
    // dispatch l'événement OutputChanged
    // dans la structure actuelle du programme une sortie d'un sif est modifié lors de l'appel à ReplaceOutput
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).OutputReplaced(output_number);
  }
//------------------------------------------------------------------------------
  public JRule NewRule( boolean active, int [] facteurs, double [] actions )  throws Throwable { return new JRule( jnifis.NewRule(this.ptr, active, facteurs, actions) ); }
//------------------------------------------------------------------------------
  public void AddRule( JRule rule ) throws Throwable {
    jnifis.AddRule( ptr, rule.Ptr() );
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).RulesModified();
  }
//------------------------------------------------------------------------------
  public void RemoveRule( int rule_number ) throws Throwable {
    jnifis.RemoveRule( ptr, rule_number );
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).RulesModified();
  }
//------------------------------------------------------------------------------
  public void Infer( double [] values ) throws Throwable { jnifis.Infer(ptr, values); }
//------------------------------------------------------------------------------
  public double [] Infer( int output_number, String data_file, String result_file, double blank_thres, boolean display ) throws Throwable
    { return jnifis.DataFileInfer(ptr, output_number, data_file, result_file, blank_thres, display); }
//------------------------------------------------------------------------------
  public double [] InferErrorRegression() throws Throwable { return jnifis.InferErrorRegression(ptr); }
//------------------------------------------------------------------------------
  public double [] SortiesObtenues() throws Throwable { return jnifis.SortiesObtenues(ptr); }
//------------------------------------------------------------------------------
  public JSemaphore GetInputSemaphore( int input_number ) { return (JSemaphore)this.InputsSemaphore.elementAt(input_number); }
//------------------------------------------------------------------------------
  public JSemaphore GetOutputSemaphore( int output_number ) { return (JSemaphore)this.OutputsSemaphore.elementAt(output_number); }
//------------------------------------------------------------------------------
  public void AddFISListener( FISListener listener ) { this.Listeners.add(listener); }
//------------------------------------------------------------------------------
  public void RemoveFISListener( FISListener listener ) { this.Listeners.remove(listener); }
//------------------------------------------------------------------------------
  public void Close() {
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).FISClosed();
  }
//------------------------------------------------------------------------------
  public void InputActiveChanged( int input_number ) {
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).InputActiveChanged(input_number);
  }
//------------------------------------------------------------------------------
  public void OutputActiveChanged( int output_number ) {
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).OutputActiveChanged(output_number);
  }
//------------------------------------------------------------------------------
  public void InputNameChanged( int input_number ) {
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).InputNameChanged(input_number);
  }
//------------------------------------------------------------------------------
  public void OutputNameChanged( int output_number ) {
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).OutputNameChanged(output_number);
  }
//------------------------------------------------------------------------------
  public void InputRangeChanged( int input_number ) {
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).InputRangeChanged(input_number);
  }
//------------------------------------------------------------------------------
  public void OutputRangeChanged( int output_number ) {
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).OutputRangeChanged(output_number);
  }
//------------------------------------------------------------------------------
  public void RemoveMFInInput( int input_number, int mf_number ) throws Throwable {
    jnifis.RemoveMFInInput(this.ptr, input_number, mf_number);
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).MFRemovedInInput(input_number);
  }
//------------------------------------------------------------------------------
  public void RemoveMFInOutput( int output_number, int mf_number ) throws Throwable {
    jnifis.RemoveMFInOutput(this.ptr, output_number, mf_number);
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).MFRemovedInOutput(output_number);
  }
//------------------------------------------------------------------------------
  public void MFAddedInInput( int input_number ) {
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).MFAddedInInput(input_number);
  }
//------------------------------------------------------------------------------
  public void MFAddedInOutput( int output_number ) {
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).MFAddedInOutput(output_number);
  }
//------------------------------------------------------------------------------
  public void MFReplacedInInput( int input_number ) {
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).MFReplacedInInput(input_number);
  }
//------------------------------------------------------------------------------
  public void MFReplacedInOutput( int output_number ) {
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).MFReplacedInOutput(output_number);
  }
//------------------------------------------------------------------------------
  public void SetOutputDefuz( int output_number, String defuz ) throws Throwable {
    jnifis.SetOutputDefuz(this.ptr, output_number, defuz);
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).OutputDefuzChanged(output_number);
  }
//------------------------------------------------------------------------------
  public void SetOutputDisjunction( int output_number, String disj ) throws Throwable {
    jnifis.SetOutputDisjunction(this.ptr, output_number, disj);
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).OutputDisjChanged(output_number);
  }
//------------------------------------------------------------------------------
  public void SetOutputClassif( int output_number, boolean classif ) throws Throwable {
    jnifis.SetOutputClassif(this.ptr, output_number, classif);
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).OutputClassifChanged(output_number);
  }
//------------------------------------------------------------------------------
  public void SetOutputDefaultValue( int output_number, double default_value ) throws Throwable {
    this.GetOutput(output_number).SetDefaultValue(default_value);
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).OutputDefaultChanged(output_number);
  }
//------------------------------------------------------------------------------
  public void SetAlarmThres( int output_number, double default_value ) throws Throwable {
    this.GetOutput(output_number).SetDefuzThres(default_value);
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).OutputAlarmThresChanged(output_number);
  }
//------------------------------------------------------------------------------
  public double [] GetClassesLabels( int output_number ) throws Throwable {
    return jnifis.GetOutputClassesLabels( this.ptr, output_number );
  }
//------------------------------------------------------------------------------
  public void ReplaceRules( JRule [] new_rules ) throws Throwable {
    // supression des règles existantes
    while( this.NbRules() != 0 )
      this.RemoveRule(0);
    // ajout des nouvelles règles au sif
    for( int i=0 ; i<new_rules.length ; i++ )
      this.AddRule(new_rules[i]);

    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).RulesModified();
  }
//------------------------------------------------------------------------------
  public void Crisp2Fuz( String defuz, int output_number ) throws Throwable {
    jnifis.Crisp2Fuz(this.ptr, defuz, output_number);
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).OutputReplaced(output_number);
  }
//------------------------------------------------------------------------------
  public void Fuz2Crisp( int output_number ) throws Throwable {
    jnifis.Fuz2Crisp(this.ptr, output_number);
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).OutputReplaced(output_number);
  }
//------------------------------------------------------------------------------
  public void SetRuleActive( int rule_number, boolean active ) throws Throwable {
    this.GetRule(rule_number).SetActive(active);
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).RulesModified();
  }
//------------------------------------------------------------------------------
  public void SetRuleProp( int rule_number, int input_number, int prop ) throws Throwable {
    this.GetRule(rule_number).SetProp(input_number, prop);
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).RulesModified();
  }
//------------------------------------------------------------------------------
  public void SetRuleConc( int rule_number, int output_number, double conc ) throws Throwable {
    this.GetRule(rule_number).SetConc(output_number, conc);
    Object [] listeners = this.Listeners.toArray();
    for( int i=0 ; i<listeners.length ; i++ )
      ((FISListener)listeners[i]).RulesModified();
  }
//------------------------------------------------------------------------------
  public int GetActiveInputNumber( int active_number ) throws Throwable {
    for( int i=0, active=0 ; i<this.NbInputs() ; i++ )
      if( this.GetInput(i).GetActive() == true ) {
        if( active == active_number )
          return i;
        else
          active++;
      }
    throw new Exception("No active input number: " + active_number);
  }
//------------------------------------------------------------------------------
  public int GetActiveOutputNumber( int active_number ) throws Throwable {
    for( int i=0, active=0 ; i<this.NbOutputs() ; i++ )
      if( this.GetOutput(i).GetActive() == true ) {
        if( active == active_number )
          return i;
        else
          active++;
      }
    throw new Exception("No active output number: " + active_number);
  }
//------------------------------------------------------------------------------
  public void InitClasses( String data_file ) throws Throwable {
    for( int i=0 ; i<this.NbOutputs() ; i++ )
      jnifis.InitClasses(this.ptr, i, data_file);
  }
//------------------------------------------------------------------------------
  public void InitPossibles() throws Throwable { jnifis.InitPossibles(this.ptr); }
//------------------------------------------------------------------------------
  public double [][] AgregationResult(int output_number) throws Throwable { return jnifis.AgregationResult(this.ptr, output_number); }
//------------------------------------------------------------------------------
  public void AddKBCTListener( KBCTListener listener ) { this.KBCTListeners.add(listener); }
//------------------------------------------------------------------------------
  public void RemoveKBCTListener( KBCTListener listener ) { this.KBCTListeners.remove(listener); }
}
