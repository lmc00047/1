//+++++++++++++++++++++++++++++++++++++++++++++++++++++
// File automatically generated by Xfuzzy - DO NOT EDIT
//+++++++++++++++++++++++++++++++++++++++++++++++++++++

package pkg;

import xfuzzy.lang.*;

public class xfl_mf_sigma extends ParamMemFunc {
 public xfl_mf_sigma() {
   super("xfl","sigma");
   Parameter single[] = new Parameter[2];
   single[0] = new Parameter("a");
   single[1] = new Parameter("b");
   setSingleParameters(single);
  }

 public double compute(double x) {
   double a = singleparam[0].value;
   double b = singleparam[1].value;
 return 1/(1+ Math.exp( (a-x)/b )); 
  }

 public double greatereq(double x) {
   double min = this.u.min();
   double a = singleparam[0].value;
   double b = singleparam[1].value;
   double y = (b>0? x : min);
   return 1 / (1+ Math.exp( (a-y)/b ));
  }

 public double smallereq(double x) {
   double max = this.u.max();
   double a = singleparam[0].value;
   double b = singleparam[1].value;
   double y = (b<0? x : max);
   return 1 / (1+ Math.exp( (a-y)/b ));
  }

 public double[] deriv_eq(double x) {
   double[] deriv = new double[getNumberOfParameters()];
   double a = singleparam[0].value;
   double b = singleparam[1].value;
   double aux1 = Math.exp((a-x)/b);
   double aux2 = aux1/((1+aux1)*(1+aux1));
   deriv[0] = - aux2/b;
   deriv[1] = aux2*(a-x)/(b*b);
   return deriv;
  }

 public double[] deriv_greq(double x) {
   double[] deriv = new double[getNumberOfParameters()];
   double min = this.u.min();
   double a = singleparam[0].value;
   double b = singleparam[1].value;
   double y = (b>0? x : min);
   double aux1 = Math.exp((a-y)/b);
   double aux2 = aux1/((1+aux1)*(1+aux1));
   deriv[0] = - aux2/b;
   deriv[1] = aux2*(a-y)/(b*b);
   return deriv;
  }

 public double[] deriv_smeq(double x) {
   double[] deriv = new double[getNumberOfParameters()];
   double max = this.u.max();
   double a = singleparam[0].value;
   double b = singleparam[1].value;
   double y = (b<0? x : max);
   double aux1 = Math.exp((a-y)/b);
   double aux2 = aux1/((1+aux1)*(1+aux1));
   deriv[0] = - aux2/b;
   deriv[1] = aux2*(a-y)/(b*b);
   return deriv;
  }

 public boolean test () {
   double min = this.u.min();
   double max = this.u.max();
   double a = singleparam[0].value;
   double b = singleparam[1].value;
   return ( a>=min && a<=max );
  }

 public void update() {
   if(!isAdjustable()) return;
   double[] pos = get();
   double[] desp = getDesp();
   boolean[] adj = getAdjustable();
   double min = this.u.min();
   double max = this.u.max();
   double a = singleparam[0].value;
   double b = singleparam[1].value;
  a += desp[0];
  b += desp[1];
  if(a<min) a = min;
  if(a>max) a = max;
  if(b/pos[1]<=0) b = pos[1]/2;
  pos[0] = a;
  pos[1] = b;
   updateValues(pos);
  }

 public String getEqualJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return 1/(1+ Math.exp( (a-x)/b )); "+eol;
   return code;
  }

 public String getGreqJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "      double y = (b>0? x : min);"+eol;
   code += "      return 1 / (1+ Math.exp( (a-y)/b ));"+eol;
   return code;
  }

 public String getSmeqJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "      double y = (b<0? x : max);"+eol;
   code += "      return 1 / (1+ Math.exp( (a-y)/b ));"+eol;
   return code;
  }


 public String getEqualCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return 1/(1+ exp( (a-x)/b )); "+eol;
   return code;
  }

 public String getGreqCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "      double y = (b>0? x : min);"+eol;
   code += "      return 1 / (1+ exp( (a-y)/b ));"+eol;
   return code;
  }

 public String getSmeqCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "      double y = (b<0? x : max);"+eol;
   code += "      return 1 / (1+ exp( (a-y)/b ));"+eol;
   return code;
  }


 public String getEqualCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return 1/(1+ exp( (a-x)/b )); "+eol;
   return code;
  }

 public String getGreqCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "      double y = (b>0? x : min);"+eol;
   code += "      return 1 / (1+ exp( (a-y)/b ));"+eol;
   return code;
  }

 public String getSmeqCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "      double y = (b<0? x : max);"+eol;
   code += "      return 1 / (1+ exp( (a-y)/b ));"+eol;
   return code;
  }

}
