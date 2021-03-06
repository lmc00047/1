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
//                           XMLParserHandler.java
//
//
//**********************************************************************

package xml;

import java.util.Hashtable;

import kbct.MainKBCT;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * xml.XMLParserHandler is a parser for XML files
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
public class XMLParserHandler extends DefaultHandler {
  protected String ExpertFile;
  protected Hashtable fisOperators= new Hashtable();
  protected Hashtable dataInfo = new Hashtable();
  protected Hashtable varInfo = new Hashtable();
  protected Hashtable contextInfo = new Hashtable();
  protected Hashtable interpretabilityInfo = new Hashtable();
  protected Hashtable ontologyInfo = new Hashtable();
  protected Hashtable systemInfo = new Hashtable();
  protected Hashtable systemInputs = new Hashtable();
  protected Hashtable systemInputLabels = new Hashtable();
  protected Hashtable systemOutputs = new Hashtable();
  protected Hashtable systemOutputLabels = new Hashtable();
  protected Hashtable systemRules = new Hashtable();
  protected Hashtable confParameters = new Hashtable();

  public XMLParserHandler() {}

  // SAX calls this method when it encounters an element
  public void startElement(String namespaceURI,
                           String localName,
                           String qualifiedName,
                           Attributes att) throws SAXException {

      //System.out.println("number of att="+att.getLength());
      //System.out.println("namespaceURI="+namespaceURI);
      //System.out.println("localName="+localName);
      //System.out.println("qualifiedName="+qualifiedName);
    if (qualifiedName.equals("ExpertFile")) {
      this.ExpertFile = "";
      String idValue= att.getValue("value");
      if (idValue!=null) {
    	  this.ExpertFile = idValue;
      }
    }
    if (qualifiedName.equals("Ontology")) {
        //this.ExpertFile = "";
        String idValue= att.getValue("OntFile");
        if (idValue!=null) {
            ontologyInfo.put("OntFile",idValue);
        }
      }
    if (qualifiedName.equals("Conjunction")) {
      String idValue = att.getValue("value");
      if (idValue != null)
          fisOperators.put("Conjunction",idValue);
    }
    if (qualifiedName.startsWith("Input")) {
        String idValue1 = att.getValue("Active");
        if (idValue1 != null)
            systemInputs.put(qualifiedName+"-Active",idValue1);

        String idValue2 = att.getValue("Name");
        if (idValue2 != null)
            systemInputs.put(qualifiedName+"-Name",idValue2);

        String idValue3 = att.getValue("Type");
        if (idValue3 != null)
            systemInputs.put(qualifiedName+"-Type",idValue3);
        
        String idValue4 = att.getValue("Trust");
        if (idValue4 != null)
            systemInputs.put(qualifiedName+"-Trust",idValue4);

        String idValue5 = att.getValue("PhysicalLowerRange");
        if (idValue5 != null)
            systemInputs.put(qualifiedName+"-PhysicalLowerRange",idValue5);

        String idValue6 = att.getValue("PhysicalUpperRange");
        if (idValue6 != null)
            systemInputs.put(qualifiedName+"-PhysicalUpperRange",idValue6);

        String idValue7 = att.getValue("InterestLowerRange");
        if (idValue7 != null)
            systemInputs.put(qualifiedName+"-InterestLowerRange",idValue7);
        
        String idValue8 = att.getValue("InterestUpperRange");
        if (idValue8 != null)
            systemInputs.put(qualifiedName+"-InterestUpperRange",idValue8);

        String idValue9 = att.getValue("ScaleOfLabels");
        if (idValue9 != null)
            systemInputs.put(qualifiedName+"-ScaleOfLabels",idValue9);

        String idValue10 = att.getValue("FlagOntology");
        if (idValue10 != null)
            systemInputs.put(qualifiedName+"-FlagOntology",idValue10);

        String idValue11 = att.getValue("NameFromOntology");
        if (idValue11 != null)
            systemInputs.put(qualifiedName+"-NameFromOntology",idValue11);

        String idValue12 = att.getValue("FlagOntObjectProperty");
        if (idValue12 != null)
            systemInputs.put(qualifiedName+"-FlagOntObjectProperty",idValue12);

        String idValue13 = att.getValue("FlagOntDatatypeProperty");
        if (idValue13 != null)
            systemInputs.put(qualifiedName+"-FlagOntDatatypeProperty",idValue13);
        
        String idValue14 = att.getValue("Labels");
        if (idValue14 != null)
            systemInputs.put(qualifiedName+"-Labels",idValue14);

        if (qualifiedName.contains("Label")) {
        	String idValue15 = att.getValue("Name");
            if (idValue15 != null)
              systemInputLabels.put(qualifiedName+"-Name",idValue15);

        	String idValue16 = att.getValue("MF");
            if (idValue16 != null)
              systemInputLabels.put(qualifiedName+"-MF",idValue16);

        	String idValue17 = att.getValue("P1");
            if (idValue17 != null)
              systemInputLabels.put(qualifiedName+"-P1",idValue17);

        	String idValue18 = att.getValue("P2");
            if (idValue18 != null)
              systemInputLabels.put(qualifiedName+"-P2",idValue18);

        	String idValue19 = att.getValue("P3");
            if (idValue19 != null)
              systemInputLabels.put(qualifiedName+"-P3",idValue19);

        	String idValue20 = att.getValue("P4");
            if (idValue20 != null)
              systemInputLabels.put(qualifiedName+"-P4",idValue20);

        	String idValue21 = att.getValue("ModalPoint");
            if (idValue21 != null)
              systemInputLabels.put(qualifiedName+"-ModalPoint",idValue21);

        	String idValue22 = att.getValue("ModalPointAux");
            if (idValue22 != null)
              systemInputLabels.put(qualifiedName+"-ModalPointAux",idValue22);
        }
    }
    if (qualifiedName.startsWith("Output")) {
        String idValue1 = att.getValue("Disjunction");
        if (idValue1 != null)
            fisOperators.put(qualifiedName+"-Disjunction",idValue1);

        String idValue2 = att.getValue("Defuzzification");
        if (idValue2 != null)
            fisOperators.put(qualifiedName+"-Defuzzification",idValue2);

        String idValue3 = att.getValue("Active");
        if (idValue3 != null)
            systemOutputs.put(qualifiedName+"-Active",idValue3);

        String idValue4 = att.getValue("Name");
        if (idValue4 != null)
            systemOutputs.put(qualifiedName+"-Name",idValue4);

        String idValue5 = att.getValue("Type");
        if (idValue5 != null)
            systemOutputs.put(qualifiedName+"-Type",idValue5);
        
        String idValue6 = att.getValue("Trust");
        if (idValue6 != null)
            systemOutputs.put(qualifiedName+"-Trust",idValue6);

        String idValue7 = att.getValue("Classif");
        if (idValue7 != null)
            systemOutputs.put(qualifiedName+"-Classif",idValue7);

        String idValue8 = att.getValue("PhysicalLowerRange");
        if (idValue8 != null)
            systemOutputs.put(qualifiedName+"-PhysicalLowerRange",idValue8);

        String idValue9 = att.getValue("PhysicalUpperRange");
        if (idValue9 != null)
            systemOutputs.put(qualifiedName+"-PhysicalUpperRange",idValue9);

        String idValue10 = att.getValue("InterestLowerRange");
        if (idValue10 != null)
            systemOutputs.put(qualifiedName+"-InterestLowerRange",idValue10);
        
        String idValue11 = att.getValue("InterestUpperRange");
        if (idValue11 != null)
            systemOutputs.put(qualifiedName+"-InterestUpperRange",idValue11);

        String idValue12 = att.getValue("ScaleOfLabels");
        if (idValue12 != null)
            systemOutputs.put(qualifiedName+"-ScaleOfLabels",idValue12);
        
        String idValue13 = att.getValue("FlagOntology");
        if (idValue13 != null)
            systemOutputs.put(qualifiedName+"-FlagOntology",idValue13);

        String idValue14 = att.getValue("NameFromOntology");
        if (idValue14 != null)
            systemOutputs.put(qualifiedName+"-NameFromOntology",idValue14);

        String idValue15 = att.getValue("FlagOntObjectProperty");
        if (idValue15 != null)
            systemOutputs.put(qualifiedName+"-FlagOntObjectProperty",idValue15);

        String idValue16 = att.getValue("FlagOntDatatypeProperty");
        if (idValue16 != null)
            systemOutputs.put(qualifiedName+"-FlagOntDatatypeProperty",idValue16);

        String idValue17 = att.getValue("Labels");
        if (idValue17 != null)
            systemOutputs.put(qualifiedName+"-Labels",idValue17);

        if (qualifiedName.contains("Label")) {
        	String idValue18 = att.getValue("Name");
            if (idValue18 != null)
              systemOutputLabels.put(qualifiedName+"-Name",idValue18);

        	String idValue19 = att.getValue("MF");
            if (idValue19 != null)
              systemOutputLabels.put(qualifiedName+"-MF",idValue19);

        	String idValue20 = att.getValue("P1");
            if (idValue20 != null)
              systemOutputLabels.put(qualifiedName+"-P1",idValue20);

        	String idValue21 = att.getValue("P2");
            if (idValue21 != null)
              systemOutputLabels.put(qualifiedName+"-P2",idValue21);

        	String idValue22 = att.getValue("P3");
            if (idValue22 != null)
              systemOutputLabels.put(qualifiedName+"-P3",idValue22);

        	String idValue23 = att.getValue("P4");
            if (idValue23 != null)
              systemOutputLabels.put(qualifiedName+"-P4",idValue23);

        	String idValue24 = att.getValue("ModalPoint");
            if (idValue24 != null)
              systemOutputLabels.put(qualifiedName+"-ModalPoint",idValue24);
        }
    }
    if (qualifiedName.startsWith("Variable")) {
      String idValue1 = att.getValue("Name");
      if (idValue1!=null)
    	  varInfo.put(qualifiedName+"-Name",idValue1);

      String idValue2 = att.getValue("Type");
      if (idValue2!=null)
    	  varInfo.put(qualifiedName+"-Type",idValue2);

      String idValue3 = att.getValue("LowerRange");
      if (idValue3!=null)
    	  varInfo.put(qualifiedName+"-LowerRange",idValue3);

      String idValue4 = att.getValue("UpperRange");
      if (idValue4!=null)
    	  varInfo.put(qualifiedName+"-UpperRange",idValue4);

      String idValue5 = att.getValue("Labels");
      if (idValue5!=null)
    	  varInfo.put(qualifiedName+"-Labels",idValue5);

      String idValue6 = att.getValue("Output");
      if (idValue6!=null)
    	  varInfo.put(qualifiedName+"-Output",idValue6);
    }
    if (qualifiedName.equals("Context")) {
        String idValue1 = att.getValue("Problem");
        if (idValue1 != null)
            contextInfo.put("Problem",idValue1);

        String idValue2 = att.getValue("Inputs");
        if (idValue2 != null)
        	contextInfo.put("Inputs",idValue2);

        String idValue3 = att.getValue("Classification");
        if (idValue3!=null)
        	contextInfo.put("Classification",idValue3);

        String idValue4 = att.getValue("OutputLabels");
        if (idValue4!=null)
        	contextInfo.put("OutputLabels",idValue4);
    }
    if (qualifiedName.startsWith("KB")) {
        String idValue1 = att.getValue("path");
        if (idValue1 != null)
            interpretabilityInfo.put(qualifiedName+"-path",idValue1);

        String idValue2 = att.getValue("conjunction");
        if (idValue2 != null)
        	interpretabilityInfo.put(qualifiedName+"-conjunction",idValue2);

        String idValue3 = att.getValue("disjunction");
        if (idValue3!=null)
        	interpretabilityInfo.put(qualifiedName+"-disjunction",idValue3);

        String idValue4 = att.getValue("defuzzification");
        if (idValue4!=null)
        	interpretabilityInfo.put(qualifiedName+"-defuzzification",idValue4);
    }
    if (qualifiedName.equals("OrigDataFile")) {
        String idValue = att.getValue("value");
        if (idValue != null)
            dataInfo.put("OrigDataFile",idValue);
    }
    if (qualifiedName.equals("VariableNames")) {
        String idValue = att.getValue("value");
        if (idValue != null)
            dataInfo.put("VariableNames",idValue);
    }
    if (qualifiedName.equals("SelectedVariables")) {
        String idValue = att.getValue("value");
        if (idValue != null)
            dataInfo.put("SelectedVariables",idValue);
    }
    if (qualifiedName.equals("DataFile")) {
        String idValue = att.getValue("value");
        if (idValue != null)
            dataInfo.put("DataFile",idValue);
    }
    if (qualifiedName.equals("DataVariableCount")) {
        String idValue = att.getValue("value");
        if (idValue != null)
            dataInfo.put("DataVariableCount",idValue);
    }
    if (qualifiedName.equals("System")) {
        String idValue1 = att.getValue("Name");
        if (idValue1 != null)
            systemInfo.put("Name",idValue1);

        String idValue2 = att.getValue("Inputs");
        if (idValue2 != null)
            systemInfo.put("Inputs",idValue2);

        String idValue3 = att.getValue("Outputs");
        if (idValue3 != null)
            systemInfo.put("Outputs",idValue3);

        String idValue4 = att.getValue("Rules");
        if (idValue4 != null)
            systemInfo.put("Rules",idValue4);
    }
    if (qualifiedName.startsWith("Rule")) {
        //String idValue1 = att.getValue("NbInputs");
        String idValue1 = (String)this.systemInfo.get("Inputs");
        if (idValue1 != null) {
            systemRules.put(qualifiedName+"-NbInputs",idValue1);
            int NbInputs= (new Integer(idValue1)).intValue();
            for (int n=0; n<NbInputs; n++) {
                String idValueIn = att.getValue("I"+String.valueOf(n+1));
                if (idValueIn != null)
                    systemRules.put(qualifiedName+"-I"+String.valueOf(n+1),idValueIn);
            }
        }
        //String idValue2 = att.getValue("NbOutputs");
        String idValue2 = (String)this.systemInfo.get("Outputs");
        if (idValue2 != null) {
            systemRules.put(qualifiedName+"-NbOutputs",idValue2);
            int NbOutputs= (new Integer(idValue2)).intValue();
            for (int n=0; n<NbOutputs; n++) {
                String idValueOut = att.getValue("O"+String.valueOf(n+1));
                if (idValueOut != null)
                    systemRules.put(qualifiedName+"-O"+String.valueOf(n+1),idValueOut);
            }
        }
        String idValue3 = att.getValue("Nature");
        if (idValue3 != null)
            systemRules.put(qualifiedName+"-Nature",idValue3);

        String idValue4 = att.getValue("Active");
        if (idValue4 != null)
            systemRules.put(qualifiedName+"-Active",idValue4);
    }
    // configuration for automatic generation
    if (qualifiedName.startsWith("SMOTE")) {
        String idValue1= att.getValue("Selected");
        if (idValue1 != null) {
            confParameters.put(qualifiedName+"-Selected",idValue1);
            Boolean sel= new Boolean(idValue1);
            MainKBCT.getConfig().SetSMOTE(sel.booleanValue());
            if (sel) {
                String Neighbors= att.getValue("Neighbors");
                if (Neighbors != null) {
                	int nn= (new Integer(Neighbors)).intValue();
                    confParameters.put(qualifiedName+"-Neighbors",Neighbors);
                	MainKBCT.getConfig().SetSMOTEnumberOfNeighbors(nn);
                }
                String Type= att.getValue("Type");
                if (Type != null) {
                    confParameters.put(qualifiedName+"-Type",Type);
                    MainKBCT.getConfig().SetSMOTEtype(Type);
                }
                String Balancing= att.getValue("Balancing");
                if (Balancing != null) {
                    Boolean bal= new Boolean(Balancing); 
                    confParameters.put(qualifiedName+"-Balancing",Balancing);
                    MainKBCT.getConfig().SetSMOTEbalancing(bal.booleanValue());
                }
                String BalancingAll= att.getValue("BalancingAll");
                if (BalancingAll != null) {
                    Boolean balall= new Boolean(BalancingAll); 
                    confParameters.put(qualifiedName+"-BalancingAll",BalancingAll);
                    MainKBCT.getConfig().SetSMOTEbalancingALL(balall.booleanValue());
                }
                String Quantity= att.getValue("Quantity");
                if (Quantity != null) {
                    double q= (new Double(Quantity)).doubleValue();
                    confParameters.put(qualifiedName+"-Quantity",Quantity);
                    MainKBCT.getConfig().SetSMOTEquantity(q);
                }
                String Distance= att.getValue("Distance");
                if (Distance != null) {
                    confParameters.put(qualifiedName+"-Distance",Distance);
                    MainKBCT.getConfig().SetSMOTEdistance(Distance);
                }
                String Interpolation= att.getValue("Interpolation");
                if (Interpolation != null) {
                    confParameters.put(qualifiedName+"-Interpolation",Interpolation);
                    MainKBCT.getConfig().SetSMOTEinterpolation(Interpolation);
                }
                String Alpha= att.getValue("Alpha");
                if (Alpha != null) {
                    double alpha= (new Double(Alpha)).doubleValue();
                    confParameters.put(qualifiedName+"-Alpha",Alpha);
                   	MainKBCT.getConfig().SetSMOTEalpha(alpha);
                }
                String Mu= att.getValue("Mu");
                if (Mu != null) {
                    double mu= (new Double(Mu)).doubleValue();
                    confParameters.put(qualifiedName+"-Mu",Mu);
                    MainKBCT.getConfig().SetSMOTEmu(mu);
                }
            }
        }
    }
    if (qualifiedName.startsWith("FeatureSelection")) {
        String idValue1= att.getValue("Selected");
        if (idValue1 != null) {
            confParameters.put(qualifiedName+"-Selected",idValue1);
            Boolean sel= new Boolean(idValue1);
            MainKBCT.getConfig().SetFeatureSelection(sel.booleanValue());
            if (sel.booleanValue()) {
                String p1= att.getValue("P1");
                if (p1 != null) {
                    confParameters.put(qualifiedName+"-P1",p1);
                    MainKBCT.getConfig().SetFeatureSelectionC45P1(p1);
                }
                String p2= att.getValue("P2");
                if (p2 != null) {
                    confParameters.put(qualifiedName+"-P2",p2);
                    MainKBCT.getConfig().SetFeatureSelectionC45P2(p2);
                }
                String p3= att.getValue("P3");
                if (p3 != null) {
                    confParameters.put(qualifiedName+"-P3",p3);
                    MainKBCT.getConfig().SetFeatureSelectionC45P3(p3);
                }
                String p4 = att.getValue("P4");
                if (p4 != null) {
                    confParameters.put(qualifiedName+"-P4",p4);
                    MainKBCT.getConfig().SetFeatureSelectionC45P4(p4);
                }
            }
        }
    }
    if (qualifiedName.startsWith("InducePartitions")) {
        String idValue1= att.getValue("Selected");
        if (idValue1 != null) {
            confParameters.put(qualifiedName+"-Selected",idValue1);
            Boolean sel= new Boolean(idValue1);
            MainKBCT.getConfig().SetInducePartitions(sel.booleanValue());
            if (sel.booleanValue()) {
                String InductionType= att.getValue("InductionType");
                if (InductionType != null) {
                    confParameters.put(qualifiedName+"-InductionType",InductionType);
                    MainKBCT.getConfig().SetInductionType(InductionType);
                    if (InductionType.equals("hfp")) {
                        String Distance= att.getValue("Distance");
                        if (Distance != null) {
                            confParameters.put(qualifiedName+"-Distance",Distance);
                            MainKBCT.getConfig().SetDistance(Distance);
                        }
                    }
                }
                String InductionNbLabels= att.getValue("InductionNbLabels");
                if (InductionNbLabels != null) {
                    confParameters.put(qualifiedName+"-InductionNbLabels",InductionNbLabels);
                    Integer indnb= new Integer(InductionNbLabels);
                    MainKBCT.getConfig().SetInductionNbLabels(indnb.intValue());
                }
            }
        }
    }
    if (qualifiedName.startsWith("PartitionSelection")) {
        String idValue1= att.getValue("Selected");
        if (idValue1 != null) {
            confParameters.put(qualifiedName+"-Selected",idValue1);
            Boolean sel= new Boolean(idValue1);
            MainKBCT.getConfig().SetPartitionSelection(sel.booleanValue());
            if (sel.booleanValue()) {
                String Distance= att.getValue("Distance");
                if (Distance != null) {
                    confParameters.put(qualifiedName+"-Distance",Distance);
                    MainKBCT.getConfig().SetDistance(Distance);
                }
            }
        }
    }
    if (qualifiedName.startsWith("RuleInduction")) {
        String idValue1= att.getValue("Selected");
        if (idValue1 != null) {
            confParameters.put(qualifiedName+"-Selected",idValue1);
            Boolean sel= new Boolean(idValue1);
            MainKBCT.getConfig().SetRuleInduction(sel.booleanValue());
            if (sel.booleanValue()) {
                String DataSelection= att.getValue("DataSelection");
                if (DataSelection != null) {
                    confParameters.put(qualifiedName+"-DataSelection",DataSelection);
                    Boolean ds= new Boolean(DataSelection);
                    MainKBCT.getConfig().SetDataSelection(ds.booleanValue());
                    if (ds.booleanValue()) {
                        String TolThresDataSelection= att.getValue("TolThresDataSelection");
                        if (TolThresDataSelection != null) {
                            confParameters.put(qualifiedName+"-TolThresDataSelection",TolThresDataSelection);
                            Double tds= new Double(TolThresDataSelection);
                            MainKBCT.getConfig().SetTolThresDataSelection(tds.doubleValue());
                        }
                    }
                }
                String ClusteringSelection= att.getValue("ClusteringSelection");
                if (ClusteringSelection != null) {
                    confParameters.put(qualifiedName+"-ClusteringSelection",ClusteringSelection);
                    Boolean cs= new Boolean(ClusteringSelection);
                    MainKBCT.getConfig().SetClusteringSelection(cs.booleanValue());
                    if (cs.booleanValue()) {
                        String ClustersNumber= att.getValue("ClustersNumber");
                        if (ClustersNumber != null) {
                            confParameters.put(qualifiedName+"-ClustersNumber",ClustersNumber);
                            Integer cn= new Integer(ClustersNumber);
                            MainKBCT.getConfig().SetClustersNumber(cn.intValue());
                        }
                    }
                }
                String InductionRulesAlgorithm= att.getValue("InductionRulesAlgorithm");
                if (InductionRulesAlgorithm != null) {
                    confParameters.put(qualifiedName+"-InductionRulesAlgorithm",InductionRulesAlgorithm);
                    MainKBCT.getConfig().SetInductionRulesAlgorithm(InductionRulesAlgorithm);
                    if (InductionRulesAlgorithm.equals("Fast Prototyping Algorithm")) {
                        String Strategy= att.getValue("Strategy");
                        if (Strategy != null) {
                            confParameters.put(qualifiedName+"-Strategy",Strategy);
                            MainKBCT.getConfig().SetStrategy(Strategy);
                        }
                        String MinCard= att.getValue("MinCard");
                        if (MinCard != null) {
                            confParameters.put(qualifiedName+"-MinCard",MinCard);
                            Integer mc= new Integer(MinCard);
                            MainKBCT.getConfig().SetMinCard(mc.intValue());
                        }
                        String MinDeg= att.getValue("MinDeg");
                        if (MinDeg != null) {
                            confParameters.put(qualifiedName+"-MinDeg",MinDeg);
                            Double md= new Double(MinDeg);
                            MainKBCT.getConfig().SetMinDeg(md.doubleValue());
                        }
                    } else if (InductionRulesAlgorithm.equals("Fuzzy Decision Trees")) {
                        String TreeFile= att.getValue("TreeFile");
                        if (TreeFile != null) {
                            confParameters.put(qualifiedName+"-TreeFile",TreeFile);
                            MainKBCT.getConfig().SetTreeFile(TreeFile);
                        }
                        String MaxTreeDepth= att.getValue("MaxTreeDepth");
                        if (MaxTreeDepth != null) {
                            confParameters.put(qualifiedName+"-MaxTreeDepth",MaxTreeDepth);
                            Integer mtd= new Integer(MaxTreeDepth);
                            MainKBCT.getConfig().SetMaxTreeDepth(mtd.intValue());
                        }
                        String MinSignificantLevel= att.getValue("MinSignificantLevel");
                        if (MinSignificantLevel != null) {
                            confParameters.put(qualifiedName+"-MinSignificantLevel",MinSignificantLevel);
                            Double msl= new Double(MinSignificantLevel);
                            MainKBCT.getConfig().SetMinSignificantLevel(msl.doubleValue());
                        }
                        String LeafMinCard= att.getValue("LeafMinCard");
                        if (LeafMinCard != null) {
                            confParameters.put(qualifiedName+"-LeafMinCard",LeafMinCard);
                            Integer lmc= new Integer(LeafMinCard);
                            MainKBCT.getConfig().SetLeafMinCard(lmc.intValue());
                        }
                        String ToleranceThreshold= att.getValue("ToleranceThreshold");
                        if (ToleranceThreshold != null) {
                            confParameters.put(qualifiedName+"-ToleranceThreshold",ToleranceThreshold);
                            Double th= new Double(ToleranceThreshold);
                            MainKBCT.getConfig().SetToleranceThreshold(th.doubleValue());
                        }
                        String MinEDGain= att.getValue("MinEDGain");
                        if (MinEDGain != null) {
                            confParameters.put(qualifiedName+"-MinEDGain",MinEDGain);
                            Double meg= new Double(MinEDGain);
                            MainKBCT.getConfig().SetMinEDGain(meg.doubleValue());
                        }
                        String CovThresh= att.getValue("CovThresh");
                        if (CovThresh != null) {
                            confParameters.put(qualifiedName+"-CovThresh",CovThresh);
                            Double ct= new Double(CovThresh);
                            MainKBCT.getConfig().SetCovThresh(ct.doubleValue());
                        }
                        String PerfLoss= att.getValue("PerfLoss");
                        if (PerfLoss != null) {
                            confParameters.put(qualifiedName+"-PerfLoss",PerfLoss);
                            Double pl= new Double(PerfLoss);
                            MainKBCT.getConfig().SetPerfLoss(pl.doubleValue());
                        }
                        String Prune= att.getValue("Prune");
                        if (Prune != null) {
                            confParameters.put(qualifiedName+"-Prune",Prune);
                            Boolean pr= new Boolean(Prune);
                            MainKBCT.getConfig().SetPrune(pr.booleanValue());
                        }
                        String Split= att.getValue("Split");
                        if (Split != null) {
                            confParameters.put(qualifiedName+"-Split",Split);
                            Boolean sp= new Boolean(Split);
                            MainKBCT.getConfig().SetSplit(sp.booleanValue());
                        }
                        String Display= att.getValue("Display");
                        if (Display != null) {
                            confParameters.put(qualifiedName+"-Display",Display);
                            Boolean d= new Boolean(Display);
                            MainKBCT.getConfig().SetDisplay(d.booleanValue());
                        }
                    } /*else if (InductionRulesAlgorithm.equals("PrototypeRules")) {
                    	
                    }*/
                }
            }
        }
    }
    if (qualifiedName.startsWith("RuleRanking")) {
        String idValue1= att.getValue("Selected");
        if (idValue1 != null) {
            confParameters.put(qualifiedName+"-Selected",idValue1);
            Boolean sel= new Boolean(idValue1);
            MainKBCT.getConfig().SetRuleRanking(sel.booleanValue());
            if (sel.booleanValue()) {
                String OrderRulesByOutputClass= att.getValue("OrderRulesByOutputClass");
                boolean out= false;
                if (OrderRulesByOutputClass != null) {
                    confParameters.put(qualifiedName+"-OrderRulesByOutputClass",OrderRulesByOutputClass);
                    out= (new Boolean(OrderRulesByOutputClass)).booleanValue();                    
                    MainKBCT.getConfig().SetOrderRulesByOutputClass(out);
                }
                String OrderRulesByLocalWeight= att.getValue("OrderRulesByLocalWeight");
                if (OrderRulesByLocalWeight != null) {
                    confParameters.put(qualifiedName+"-OrderRulesByLocalWeight",OrderRulesByLocalWeight);
                    Boolean or= new Boolean(OrderRulesByLocalWeight);
                    MainKBCT.getConfig().SetOrderRulesByLocalWeight(or.booleanValue());
                }
                String OrderRulesByGlobalWeight= att.getValue("OrderRulesByGlobalWeight");
                if (OrderRulesByGlobalWeight != null) {
                    confParameters.put(qualifiedName+"-OrderRulesByGlobalWeight",OrderRulesByGlobalWeight);
                    Boolean or= new Boolean(OrderRulesByGlobalWeight);
                    MainKBCT.getConfig().SetOrderRulesByGlobalWeight(or.booleanValue());
                }
                String OrderRulesByWeight= att.getValue("OrderRulesByWeight");
                if (OrderRulesByWeight != null) {
                    confParameters.put(qualifiedName+"-OrderRulesByWeight",OrderRulesByWeight);
                    Boolean or= new Boolean(OrderRulesByWeight);
                    MainKBCT.getConfig().SetOrderRulesByWeight(or.booleanValue());
                }
                String OrderRulesByLocalIntWeight= att.getValue("OrderRulesByLocalIntWeight");
                if (OrderRulesByLocalIntWeight != null) {
                    confParameters.put(qualifiedName+"-OrderRulesByLocalIntWeight",OrderRulesByLocalIntWeight);
                    Boolean or= new Boolean(OrderRulesByLocalIntWeight);
                    MainKBCT.getConfig().SetOrderRulesByLocalIntWeight(or.booleanValue());
                }
                String OrderRulesByGlobalIntWeight= att.getValue("OrderRulesByGlobalIntWeight");
                if (OrderRulesByGlobalIntWeight != null) {
                    confParameters.put(qualifiedName+"-OrderRulesByGlobalIntWeight",OrderRulesByGlobalIntWeight);
                    Boolean or= new Boolean(OrderRulesByGlobalIntWeight);
                    MainKBCT.getConfig().SetOrderRulesByGlobalIntWeight(or.booleanValue());
                }
                String OrderRulesByIntWeight= att.getValue("OrderRulesByIntWeight");
                if (OrderRulesByIntWeight != null) {
                    confParameters.put(qualifiedName+"-OrderRulesByIntWeight",OrderRulesByIntWeight);
                    Boolean or= new Boolean(OrderRulesByIntWeight);
                    MainKBCT.getConfig().SetOrderRulesByIntWeight(or.booleanValue());
                }
                String OrderRulesByNumberPremises= att.getValue("OrderRulesByNumberPremises");
                if (OrderRulesByNumberPremises != null) {
                    confParameters.put(qualifiedName+"-OrderRulesByNumberPremises",OrderRulesByNumberPremises);
                    Boolean or= new Boolean(OrderRulesByNumberPremises);
                    MainKBCT.getConfig().SetOrderRulesByNumberPremises(or.booleanValue());
                }
                String ReverseOrderRules= att.getValue("ReverseOrderRules");
                if (ReverseOrderRules != null) {
                    confParameters.put(qualifiedName+"-ReverseOrderRules",ReverseOrderRules);
                    Boolean or= new Boolean(ReverseOrderRules);
                    MainKBCT.getConfig().SetReverseOrderRules(or.booleanValue());
                }
                if (!out) {
                    String OutputClassSelected= att.getValue("OutputClassSelected");
                    if (OutputClassSelected != null) {
                        confParameters.put(qualifiedName+"-OutputClassSelected",OutputClassSelected);
                        MainKBCT.getConfig().SetOutputClassSelected(OutputClassSelected);
                    }
                }
            }
        }
    }
    if (qualifiedName.startsWith("SolveLingConflicts")) {
        String idValue1= att.getValue("Selected");
        if (idValue1 != null) {
            confParameters.put(qualifiedName+"-Selected",idValue1);
            Boolean slc= new Boolean(idValue1);
            MainKBCT.getConfig().SetSolveLingConflicts(slc.booleanValue());
        }
    }
    if (qualifiedName.startsWith("LVreduction")) {
        String idValue1= att.getValue("Selected");
        if (idValue1 != null) {
            confParameters.put(qualifiedName+"-Selected",idValue1);
            Boolean lvr= new Boolean(idValue1);
            MainKBCT.getConfig().SetLVreduction(lvr.booleanValue());
        }
    }
    if (qualifiedName.startsWith("LinguisticSimplification")) {
        String idValue1= att.getValue("Selected");
        if (idValue1 != null) {
            confParameters.put(qualifiedName+"-Selected",idValue1);
            Boolean sel= new Boolean(idValue1);
            MainKBCT.getConfig().SetSimplify(sel.booleanValue());
            if (sel.booleanValue()) {
                String FirstReduceRuleBase= att.getValue("FirstReduceRuleBase");
                if (FirstReduceRuleBase != null) {
                    confParameters.put(qualifiedName+"-FirstReduceRuleBase",FirstReduceRuleBase);
                    Boolean frrb= new Boolean(FirstReduceRuleBase);
                    MainKBCT.getConfig().SetFirstReduceRuleBase(frrb.booleanValue());
                }
                String OnlyDBsimplification= att.getValue("OnlyDBsimplification");
                if (OnlyDBsimplification != null) {
                    confParameters.put(qualifiedName+"-OnlyDBsimplification",OnlyDBsimplification);
                    Boolean odbs= new Boolean(OnlyDBsimplification);
                    MainKBCT.getConfig().SetOnlyDBsimplification(odbs.booleanValue());
                }
                String OnlyRBsimplification= att.getValue("OnlyRBsimplification");
                if (OnlyRBsimplification != null) {
                    confParameters.put(qualifiedName+"-OnlyRBsimplification",OnlyRBsimplification);
                    Boolean orbs= new Boolean(OnlyRBsimplification);
                    MainKBCT.getConfig().SetOnlyRBsimplification(orbs.booleanValue());
                }
                String MaximumLossOfCoverage= att.getValue("MaximumLossOfCoverage");
                if (MaximumLossOfCoverage != null) {
                    confParameters.put(qualifiedName+"-MaximumLossOfCoverage",MaximumLossOfCoverage);
                    Double mloc= new Double(MaximumLossOfCoverage);
                    MainKBCT.getConfig().SetMaximumLossOfCoverage(mloc.doubleValue());
                }
                String MaximumLossOfPerformance= att.getValue("MaximumLossOfPerformance");
                if (MaximumLossOfPerformance != null) {
                    confParameters.put(qualifiedName+"-MaximumLossOfPerformance",MaximumLossOfPerformance);
                    Double mlop= new Double(MaximumLossOfPerformance);
                    MainKBCT.getConfig().SetMaximumLossOfPerformance(mlop.doubleValue());
                }
                String MaximumNumberNewErrorCases= att.getValue("MaximumNumberNewErrorCases");
                if (MaximumNumberNewErrorCases != null) {
                    confParameters.put(qualifiedName+"-MaximumNumberNewErrorCases",MaximumNumberNewErrorCases);
                    Integer mnnec= new Integer(MaximumNumberNewErrorCases);
                    MainKBCT.getConfig().SetMaximumNumberNewErrorCases(mnnec.intValue());
                }
                String MaximumNumberNewAmbiguityCases= att.getValue("MaximumNumberNewAmbiguityCases");
                if (MaximumNumberNewAmbiguityCases != null) {
                    confParameters.put(qualifiedName+"-MaximumNumberNewAmbiguityCases",MaximumNumberNewAmbiguityCases);
                    Integer mnnac= new Integer(MaximumNumberNewAmbiguityCases);
                    MainKBCT.getConfig().SetMaximumNumberNewAmbiguityCases(mnnac.intValue());
                }
                String MaximumNumberNewUnclassifiedCases= att.getValue("MaximumNumberNewUnclassifiedCases");
                if (MaximumNumberNewUnclassifiedCases != null) {
                    confParameters.put(qualifiedName+"-MaximumNumberNewUnclassifiedCases",MaximumNumberNewUnclassifiedCases);
                    Integer mnnuc= new Integer(MaximumNumberNewUnclassifiedCases);
                    MainKBCT.getConfig().SetMaximumNumberNewUnclassifiedCases(mnnuc.intValue());
                }
                String RuleRemoval= att.getValue("RuleRemoval");
                if (RuleRemoval != null) {
                    confParameters.put(qualifiedName+"-RuleRemoval",RuleRemoval);
                    Boolean rr= new Boolean(RuleRemoval);
                    MainKBCT.getConfig().SetRuleRemoval(rr.booleanValue());
                }
                String VariableRemoval= att.getValue("VariableRemoval");
                if (VariableRemoval != null) {
                    confParameters.put(qualifiedName+"-VariableRemoval",VariableRemoval);
                    Boolean vr= new Boolean(VariableRemoval);
                    MainKBCT.getConfig().SetVariableRemoval(vr.booleanValue());
                }
                String PremiseRemoval= att.getValue("PremiseRemoval");
                if (PremiseRemoval != null) {
                    confParameters.put(qualifiedName+"-PremiseRemoval",PremiseRemoval);
                    Boolean pr= new Boolean(PremiseRemoval);
                    MainKBCT.getConfig().SetPremiseRemoval(pr.booleanValue());
                }
                String RuleRanking= att.getValue("RuleRanking");
                if (RuleRanking != null) {
                    confParameters.put(qualifiedName+"-RuleRanking",RuleRanking);
                    MainKBCT.getConfig().SetSimpRuleRanking(RuleRanking);
                }
                String SelectedPerformance= att.getValue("SelectedPerformance");
                if (SelectedPerformance != null) {
                    confParameters.put(qualifiedName+"-SelectedPerformance",SelectedPerformance);
                    Boolean sp= new Boolean(SelectedPerformance);
                    MainKBCT.getConfig().SetSelectedPerformance(sp.booleanValue());
                }
            }
        }
    }
    if (qualifiedName.startsWith("Optimization")) {
        String idValue1= att.getValue("Selected");
        if (idValue1 != null) {
            confParameters.put(qualifiedName+"-Selected",idValue1);
            Boolean sel= new Boolean(idValue1);
            MainKBCT.getConfig().SetOptimization(sel.booleanValue());
            if (sel.booleanValue()) {
                String OptAlgorithm= att.getValue("OptAlgorithm");
                if (OptAlgorithm != null) {
                    confParameters.put(qualifiedName+"-OptAlgorithm",OptAlgorithm);
                    Integer oa= new Integer(OptAlgorithm);
                    MainKBCT.getConfig().SetOptAlgorithm(oa.intValue());
                    String OptAlgorithmName= att.getValue("OptAlgorithmName");
                    if (OptAlgorithmName != null) {
                        confParameters.put(qualifiedName+"-OptAlgorithmName",OptAlgorithmName);
                    }
                    if (OptAlgorithm.equals("0") || OptAlgorithm.equals("2")) {
           			    // GeneticTuning -> 0
              	        // GA rule selection -> 2
                        String NbGenerations= att.getValue("NbGenerations");
                        if (NbGenerations != null) {
                            confParameters.put(qualifiedName+"-NbGenerations",NbGenerations);
                            Integer ng= new Integer(NbGenerations);
                            if (oa.intValue()==0)
                                MainKBCT.getConfig().SetNbGenerations(ng.intValue());
                            else if (oa.intValue()==2)
                                MainKBCT.getConfig().SetRuleSelectionNbGenerations(ng.intValue());
                        }
                        String PopulationLength= att.getValue("PopulationLength");
                        if (PopulationLength != null) {
                            confParameters.put(qualifiedName+"-PopulationLength",PopulationLength);
                            Integer pl= new Integer(PopulationLength);
                            if (oa.intValue()==0)
                                MainKBCT.getConfig().SetPopulationLength(pl.intValue());
                            else if (oa.intValue()==2)
                                MainKBCT.getConfig().SetRuleSelectionPopulationLength(pl.intValue());
                        }
                        String TournamentSize= att.getValue("TournamentSize");
                        if (TournamentSize != null) {
                            confParameters.put(qualifiedName+"-TournamentSize",TournamentSize);
                            Integer ts= new Integer(TournamentSize);
                            if (oa.intValue()==0)
                                MainKBCT.getConfig().SetTournamentSize(ts.intValue());
                            else if (oa.intValue()==2)
                                MainKBCT.getConfig().SetRuleSelectionTournamentSize(ts.intValue());
                        }
                        String MutationProb= att.getValue("MutationProb");
                        if (MutationProb != null) {
                            confParameters.put(qualifiedName+"-MutationProb",MutationProb);
                            Double mp= new Double(MutationProb);
                            if (oa.intValue()==0)
                                MainKBCT.getConfig().SetMutationProb(mp.doubleValue());
                            else if (oa.intValue()==2)
                                MainKBCT.getConfig().SetRuleSelectionMutationProb(mp.doubleValue());
                        }
                        String CrossoverProb= att.getValue("CrossoverProb");
                        if (CrossoverProb != null) {
                            confParameters.put(qualifiedName+"-CrossoverProb",CrossoverProb);
                            Double cp= new Double(CrossoverProb);
                            if (oa.intValue()==0)
                                MainKBCT.getConfig().SetCrossoverProb(cp.doubleValue());
                            else if (oa.intValue()==2)
                                MainKBCT.getConfig().SetRuleSelectionCrossoverProb(cp.doubleValue());
                        }
                        if (OptAlgorithm.equals("0")) {
                            String ParAlfa= att.getValue("ParAlfa");
                            if (ParAlfa != null) {
                                confParameters.put(qualifiedName+"-ParAlfa",ParAlfa);
                                Double pa= new Double(ParAlfa);
                                MainKBCT.getConfig().SetParAlfa(pa.doubleValue());
                            }
                            String BoundedOptimization= att.getValue("BoundedOptimization");
                            if (BoundedOptimization != null) {
                                confParameters.put(qualifiedName+"-BoundedOptimization",BoundedOptimization);
                                Boolean bo= new Boolean(BoundedOptimization);
                                MainKBCT.getConfig().SetBoundedOptimization(bo.booleanValue());
                            }
                            String MilestoneGeneration= att.getValue("MilestoneGeneration");
                            if (MilestoneGeneration != null) {
                                confParameters.put(qualifiedName+"-MilestoneGeneration",MilestoneGeneration);
                                Integer mg= new Integer(MilestoneGeneration);
                                //System.out.println("	>> milestoneGeneration="+MilestoneGeneration);
                                MainKBCT.getConfig().SetMilestoneGeneration(mg.intValue());
                            }
                        }
                        if (OptAlgorithm.equals("2")) {
                            String W1= att.getValue("W1");
                            if (W1 != null) {
                                confParameters.put(qualifiedName+"-W1",W1);
                                Double w1= new Double(W1);
                                MainKBCT.getConfig().SetRuleSelectionW1(w1.doubleValue());
                            }
                            String W2= att.getValue("W2");
                            if (W2 != null) {
                                confParameters.put(qualifiedName+"-W2",W2);
                                Double w2= new Double(W2);
                                MainKBCT.getConfig().SetRuleSelectionW2(w2.doubleValue());
                            }
                            String InterpretabilityIndex= att.getValue("InterpretabilityIndex");
                            if (InterpretabilityIndex != null) {
                                confParameters.put(qualifiedName+"-InterpretabilityIndex",InterpretabilityIndex);
                                Integer ii= new Integer(InterpretabilityIndex);
                                MainKBCT.getConfig().SetRuleSelectionInterpretabilityIndex(ii.intValue());
                            }
                        }
                        String InitialGeneration= att.getValue("InitialGeneration");
                        if (InitialGeneration != null) {
                            confParameters.put(qualifiedName+"-InitialGeneration",InitialGeneration);
                            Integer ig= new Integer(InitialGeneration);
                            if (oa.intValue()==0)
                                MainKBCT.getConfig().SetInitialGeneration(ig.intValue());
                            else if (oa.intValue()==2)
                                MainKBCT.getConfig().SetRuleSelectionInitialGeneration(ig.intValue());
                        }
                    } else if (OptAlgorithm.equals("1")) {
              	        // solisWetts (FisPro)
                        String SWoption= att.getValue("SWoption");
                        if (SWoption != null) {
                            confParameters.put(qualifiedName+"-SWoption",SWoption);
                            Integer swo= new Integer(SWoption);
                            MainKBCT.getConfig().SetSWoption(swo.intValue());
                        }
                        String SWoptName= att.getValue("SWoptName");
                        if (SWoptName != null) {
                            confParameters.put(qualifiedName+"-SWoptName",SWoptName);
                            //MainKBCT.getConfig().SetSWoptName(SWoptName);
                        }
                        String NbIterations= att.getValue("NbIterations");
                        if (NbIterations != null) {
                            confParameters.put(qualifiedName+"-NbIterations",NbIterations);
                            Integer ni= new Integer(NbIterations);
                            MainKBCT.getConfig().SetNbIterations(ni.intValue());
                        }
                        String BoundedOptimization= att.getValue("BoundedOptimization");
                        if (BoundedOptimization != null) {
                            confParameters.put(qualifiedName+"-BoundedOptimization",BoundedOptimization);
                            Boolean bo= new Boolean(BoundedOptimization);
                            MainKBCT.getConfig().SetBoundedOptimization(bo.booleanValue());
                        }
                    }
                }
            }
        }
    }
    if (qualifiedName.startsWith("Completeness")) {
        String idValue1= att.getValue("Selected");
        if (idValue1 != null) {
            confParameters.put(qualifiedName+"-Selected",idValue1);
            Boolean c= new Boolean(idValue1);
            MainKBCT.getConfig().SetCompleteness(c.booleanValue());
        }
    }
    if (qualifiedName.startsWith("Fingrams")) {
        String idValue1= att.getValue("Selected");
        if (idValue1 != null) {
        	//System.out.println("Fingrams");
            confParameters.put(qualifiedName+"-Selected",idValue1);
            Boolean sel= new Boolean(idValue1);
            MainKBCT.getConfig().SetFingram(sel.booleanValue());
            if (sel) {
                String Option= att.getValue("Option");
                if (Option != null) {
                    confParameters.put(qualifiedName+"-Option",Option);
                    if (Option.equals("ALL")) {
                    	//System.out.println("ALL");
                        MainKBCT.getConfig().SetFingramWS(true);
                        MainKBCT.getConfig().SetFingramWOS(true);
                    } else if (Option.equals("WS")) {
                        MainKBCT.getConfig().SetFingramWS(true);
                        MainKBCT.getConfig().SetFingramWOS(false);
                    } else if (Option.equals("WOS")) {
                        MainKBCT.getConfig().SetFingramWS(false);
                        MainKBCT.getConfig().SetFingramWOS(true);
                    }
                    if ( (Option.equals("ALL")) || (Option.equals("WS")) ) {
                    	// SMOTE parameters
                    	String Neighbors= att.getValue("Neighbors");
                        if (Neighbors != null) {
                        	int nn= (new Integer(Neighbors)).intValue();
                            confParameters.put(qualifiedName+"-Neighbors",Neighbors);
                        	MainKBCT.getConfig().SetSMOTEnumberOfNeighbors(nn);
                        }
                        String Type= att.getValue("Type");
                        if (Type != null) {
                            confParameters.put(qualifiedName+"-Type",Type);
                            MainKBCT.getConfig().SetSMOTEtype(Type);
                        }
                        String Balancing= att.getValue("Balancing");
                        if (Balancing != null) {
                            Boolean bal= new Boolean(Balancing); 
                            confParameters.put(qualifiedName+"-Balancing",Balancing);
                            MainKBCT.getConfig().SetSMOTEbalancing(bal.booleanValue());
                        }
                        String BalancingAll= att.getValue("BalancingAll");
                        if (BalancingAll != null) {
                            Boolean balall= new Boolean(BalancingAll); 
                            confParameters.put(qualifiedName+"-BalancingAll",BalancingAll);
                            MainKBCT.getConfig().SetSMOTEbalancingALL(balall.booleanValue());
                        }
                        String Quantity= att.getValue("Quantity");
                        if (Quantity != null) {
                            double q= (new Double(Quantity)).doubleValue();
                            confParameters.put(qualifiedName+"-Quantity",Quantity);
                            MainKBCT.getConfig().SetSMOTEquantity(q);
                        }
                        String Distance= att.getValue("Distance");
                        if (Distance != null) {
                            confParameters.put(qualifiedName+"-Distance",Distance);
                            MainKBCT.getConfig().SetSMOTEdistance(Distance);
                        }
                        String Interpolation= att.getValue("Interpolation");
                        if (Interpolation != null) {
                            confParameters.put(qualifiedName+"-Interpolation",Interpolation);
                            MainKBCT.getConfig().SetSMOTEinterpolation(Interpolation);
                        }
                        String Alpha= att.getValue("Alpha");
                        if (Alpha != null) {
                            double alpha= (new Double(Alpha)).doubleValue();
                            confParameters.put(qualifiedName+"-Alpha",Alpha);
                           	MainKBCT.getConfig().SetSMOTEalpha(alpha);
                        }
                        String Mu= att.getValue("Mu");
                        if (Mu != null) {
                            double mu= (new Double(Mu)).doubleValue();
                            confParameters.put(qualifiedName+"-Mu",Mu);
                            MainKBCT.getConfig().SetSMOTEmu(mu);
                        }
                    }
                }
                String GLthres= att.getValue("GLthres");
                if (GLthres != null) {
                    confParameters.put(qualifiedName+"-GLthres",GLthres);
                    double glth= (new Double(GLthres)).doubleValue();
                    MainKBCT.getConfig().SetGoodnessLowThreshold(glth);
                }
                String GHthres= att.getValue("GHthres");
                if (GHthres != null) {
                    confParameters.put(qualifiedName+"-GHthres",GHthres);
                    double ghth= (new Double(GHthres)).doubleValue();
                    MainKBCT.getConfig().SetGoodnessHighThreshold(ghth);
                }
                String PFthres= att.getValue("PFthres");
                if (PFthres != null) {
                    confParameters.put(qualifiedName+"-PFthres",PFthres);
                    double pfthres= (new Double(PFthres)).doubleValue();
                    MainKBCT.getConfig().SetPathFinderThreshold(pfthres);
                }
                String PFq= att.getValue("PFq");
                if (PFq != null) {
                    confParameters.put(qualifiedName+"-PFq",PFq);
                    int pfq= (new Integer(PFq)).intValue();
                    MainKBCT.getConfig().SetPathFinderParQ(pfq);
                }
                String Metric= att.getValue("Metric");
                if (Metric != null) {
                    confParameters.put(qualifiedName+"-Metric",Metric);
                    MainKBCT.getConfig().SetFingramsMetric(Metric);
                }
                String Layout= att.getValue("Layout");
                if (Layout != null) {
                    confParameters.put(qualifiedName+"-Layout",Layout);
                    MainKBCT.getConfig().SetFingramsLayout(Layout);
                }
            }
        }
    }
  }

  // SAX calls this method to pass in character data
  public void characters(char ch[], int start, int length) throws SAXException {
  }

  // SAX call this method when it encounters an end tag
  public void endElement(String namespaceURI,
                         String localName,
                         String qualifiedName) throws SAXException {
  }
}