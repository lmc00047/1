//+++++++++++++++++++++++++++++++++++++++++++++++++++++
// File automatically generated by Xfuzzy - DO NOT EDIT
//+++++++++++++++++++++++++++++++++++++++++++++++++++++

package pkg;

import xfuzzy.lang.*;

public class xfl_defuz_CenterOfArea extends DefuzMethod {
 public xfl_defuz_CenterOfArea() {
   super("xfl","CenterOfArea");
   Parameter single[] = new Parameter[0];
   setSingleParameters(single);
  }

 public double compute(AggregateMemFunc mf) {
   double min = mf.min();
   double max = mf.max();
   double step = mf.step();
  double num=0, denom=0;
  for(double x=min; x<=max; x+=step) {
   double m = mf.compute(x);
   num += x*m;
   denom += m;
  }
  if(denom==0) return (min+max)/2;
  return num/denom;
  }

 public boolean test () {
   return true;
  }

 public boolean test(AggregateMemFunc mf) {
   return true;
  }

 public String getJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "     double num=0, denom=0;"+eol;
   code += "     for(double x=min; x<=max; x+=step) {"+eol;
   code += "      double m = mf.compute(x);"+eol;
   code += "      num += x*m;"+eol;
   code += "      denom += m;"+eol;
   code += "     }"+eol;
   code += "     if(denom==0) return (min+max)/2;"+eol;
   code += "     return num/denom;"+eol;
   return code;
  }

 public String getCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "     double x, m, num=0, denom=0;"+eol;
   code += "     for(x=min; x<=max; x+=step) {"+eol;
   code += "      m = compute(mf,x);"+eol;
   code += "      num += x*m;"+eol;
   code += "      denom += m;"+eol;
   code += "     }"+eol;
   code += "     if(denom==0) return (min+max)/2;"+eol;
   code += "     return num/denom;"+eol;
   return code;
  }

 public String getCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "     double num=0, denom=0;"+eol;
   code += "     for(double x=min; x<=max; x+=step) {"+eol;
   code += "      double m = mf.compute(x);"+eol;
   code += "      num += x*m;"+eol;
   code += "      denom += m;"+eol;
   code += "     }"+eol;
   code += "     if(denom==0) return (min+max)/2;"+eol;
   code += "     return num/denom;"+eol;
   return code;
  }
}
