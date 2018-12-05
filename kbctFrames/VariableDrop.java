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
//                         VariableDrop.java
//
//
//**********************************************************************

package kbctFrames;

import java.awt.dnd.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.*;
import java.io.IOException;
import java.util.Vector;
import java.util.Collections;
import java.util.Iterator; 
import java.util.List;

import org.umu.ore.data.VariableCollection;
import org.umu.ore.util.OntologyManagerStrategy;
import org.umu.ore.util.comparators.OntClassImplComparator;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.vocabulary.OWL;

import KB.variable;
import kbct.JKBCT;
import kbct.JKBCTInput;
import kbct.JKBCTOutput;
import kbct.LocaleKBCT;
import kbctAux.MessageKBCT;
import kbctFrames.JExpertFrame;

/**
 * Manage the drop of variables (input/output) from the ontology frame.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
public class VariableDrop extends DropTarget {
	static final long serialVersionUID=0;	
    JExpertFrame jef;
    boolean input= false;
    
    public VariableDrop(JExpertFrame jef, boolean in) throws HeadlessException {
        super();
        this.jef= jef;
        this.input= in;
    }

    public synchronized void drop(DropTargetDropEvent dtde) {
        //System.out.println("VariableDrop: drop");
        Transferable tr = dtde.getTransferable();
        Object o = null;
        for (DataFlavor d : tr.getTransferDataFlavors()) {
        	//System.out.println("Name= "+d.getHumanPresentableName());
            if (d.getHumanPresentableName().equalsIgnoreCase(DataFlavor.javaJVMLocalObjectMimeType)) {
                try {
                    o = tr.getTransferData(d);
                    break;
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String uri = null;
        if (o instanceof String) {
            uri = (String) o;
            //System.out.println("uri= "+uri);
            OntModel m = OntologyManagerStrategy.getOntologyManager().getBaseModelWithImports();
            Object aux = m.getOntResource((String) o);
            if (aux != null) {
                o = aux;
            } else {
                //System.out.println("variable collection");
                o = VariableCollection.getVariableCollection().getVariableByName((String) o);
            }
        }
        /*if (o instanceof Variable) {
            System.out.println("variable");
        }*/
        if ( o instanceof OntClass ||
        	(o instanceof OntResource && ((OntResource) o).isClass()) ||
        	 o instanceof DatatypeProperty || 
        	(o instanceof OntResource && ((OntResource) o).isDatatypeProperty()) ||
        	 o instanceof ObjectProperty ||
        	(o instanceof OntResource && ((OntResource) o).isObjectProperty()) ) {
        //if ( o instanceof ObjectProperty || 
        //	(o instanceof OntResource && ((OntResource) o).isObjectProperty()) ||
        //	 o instanceof DatatypeProperty ||
        //	(o instanceof OntResource && ((OntResource) o).isDatatypeProperty())) {
            //String uri= ((OntResource) o).getNameSpace();
            //System.out.println("uri= "+uri);
            String lname= ((OntResource) o).getLocalName();
            //System.out.println("lname= "+lname);
            String aux;
            Vector vsubclasses= new Vector();
            int cont= 0;
            //if (!(o instanceof OntClass) && !(o instanceof ObjectProperty)) { 
            if (o instanceof OntClass) {
                //System.out.println("OntClass");
                aux = ((OntClass)o).getLocalName();
                //System.out.println("aux= "+aux);
            } else if (o instanceof DatatypeProperty) {
                //System.out.println("DatatypeProperty");
                aux = ((DatatypeProperty)o).getLocalName();
                //System.out.println("aux= "+aux);
            } else if (o instanceof ObjectProperty) {
                //System.out.println("ObjectProperty");
                aux = ((ObjectProperty)o).getLocalName();
                //System.out.println("aux= "+aux);
            } else {
                //System.out.println("OntResource");
                OntClass ontologyClass = ((OntResource)o).getOntModel().getOntClass(uri);
                if (ontologyClass != null) {
                  List list = ontologyClass.listSubClasses(true).toList();
                  Collections.sort(list, new OntClassImplComparator());
                  Iterator ei = list.iterator();
                  for (; ei.hasNext();) {
                    OntResource actClass = (OntResource) ei.next();
                    if (!actClass.getURI().equals(OWL.Nothing.getURI())) {
                    	aux= actClass.getLocalName();
                        //System.out.println("aux= "+aux);
                        vsubclasses.add(aux);
                        cont++;
                    }
                  }
                }
            }
            try {
                variable v= new variable();
                JKBCT kbct= this.jef.getKBCTTemp();
                //System.out.println("cont= "+cont);
                String[] LabNames= new String[3];
                if (cont > 1) {
                    LabNames= new String[cont];
                    Object[] obj= vsubclasses.toArray();
                    for (int n=0; n< cont; n++) {
                    	 LabNames[n]= obj[n].toString();
                    }
                }
                v.SetFlagOntology(true);
            	//System.out.println("SET var1: flagOntology="+v.GetFlagOntology());
                if (this.input) {
                    JKBCTInput new_input = new JKBCTInput(v, kbct.GetNbInputs()+1);
                    new_input.SetName(lname);
                    new_input.SetNameFromOntology(lname);
                    if  (o instanceof ObjectProperty || 
                        (o instanceof OntResource && ((OntResource) o).isObjectProperty()) ) 
                    	 new_input.SetFlagOntObjectProperty(true);
                    else
                    	 new_input.SetFlagOntDatatypeProperty(true);
                    	
                    if (cont > 1) {
                        new_input.SetLabelsNumber(cont);
                        new_input.SetInputPhysicalRange(1, cont);
                        new_input.SetInputInterestRange(1, cont);
                        new_input.SetNewScale(LabNames);
                        new_input.SetScaleName("user");
                        new_input.SetORLabelsName();
                        new_input.SetLabelProperties();
                    }
                    kbct.AddInput( new_input );
                } else {
                    JKBCTOutput new_output = new JKBCTOutput(v, kbct.GetNbOutputs()+1);
                    new_output.SetName(lname);
                    new_output.SetNameFromOntology(lname);
                    if  (o instanceof ObjectProperty || 
                            (o instanceof OntResource && ((OntResource) o).isObjectProperty()) ) 
                        	 new_output.SetFlagOntObjectProperty(true);
                        else
                        	 new_output.SetFlagOntDatatypeProperty(true);

                    if (cont > 1) {
                        new_output.SetLabelsNumber(cont);
                        new_output.SetInputPhysicalRange(1, cont);
                        new_output.SetInputInterestRange(1, cont);
                        new_output.SetNewScale(LabNames);
                        new_output.SetScaleName("user");
                        new_output.SetLabelProperties();
                    }
                    kbct.AddOutput( new_output );
                }
                this.jef.InitJExpertFrameWithKBCT();
            	//System.out.println("SET var2: flagOntology="+this.jef.getKBCTTemp().GetOutput(2).GetV().GetFlagOntology());
              } catch( Throwable en ) {
                  en.printStackTrace();
                  MessageKBCT.Error(this.jef, LocaleKBCT.GetString("Error"), "Error in VariableDrop generating NewInput: "+en);
              }
        } else {
            dtde.rejectDrop();
        }
        /*if (o instanceof ObjectProperty || (o instanceof OntResource && ((OntResource) o).isObjectProperty())) {
            System.out.println("ObjectProperty");
        }
        if (o instanceof Individual || o instanceof org.mindswap.pellet.Individual || (o instanceof OntResource && ((OntResource) o).isIndividual())) {
            System.out.println("Individual");
        }
        if (o instanceof DatatypeProperty || (o instanceof OntResource && ((OntResource) o).isDatatypeProperty())) {
            System.out.println("DatatypeProperty");
        }*/
    }
}