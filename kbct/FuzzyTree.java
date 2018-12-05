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
//                             FuzzyTree.java
//
//
//**********************************************************************

// Contains: FuzzyTree, FuzzyNode, FuzzyNodeClassif, FuzzyNodeRegression, FuzzyNodeFuzzyOutput

package kbct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

//***********************************************************************

//
//
//                              FuzzyTree.java
//
//
// Author(s) : Jean-Luc LABLEE
// FISPRO Version : 1.0
// Contact : fispro@ensam.inra.fr
// Last modification date : December 25, 2002
// File :

//**********************************************************************

/**
 * kbct.FuzzyTree
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 04/06/13
 */

//------------------------------------------------------------------------------
public class FuzzyTree extends JTree {
  static final long serialVersionUID=0;	
  private int NbClass;                      // Nombre de classes de l'arbre
  private FuzzyNode Root;                   // noeud racine de l'arbre
  private DefaultTreeModel TreeModel;       // Model de larbre
  private Vector<FuzzyNode> Nodes = new Vector<FuzzyNode>();      // vecteur des noeuds de l'arbre
  public Vector VariablesGroups = new Vector();    // vecteur des groupes de variables
  public boolean DisplayAllNodes = true;           // Affiche tous les noeuds
  private boolean Classif = false;          // arbre classif
  private boolean Regression = false;       // arbre régression
  private boolean FuzzyOutput = false;      // arbre flou
//------------------------------------------------------------------------------
  public FuzzyTree( String file_name ) throws FileNotFoundException, IOException, Exception {
    // ouverture du fichier file_name
    File f = new File(file_name);
    // creation d'un buffer de lecture du fichier f
    BufferedReader br = new BufferedReader( new InputStreamReader( new FileInputStream(f) ) );
    String buffer = br.readLine();
    Vector<String> params = new Vector<String>(); // paramètres sur première ligne du fichier
    try {
      StringTokenizer st = new StringTokenizer( buffer, "," ); // decompose le buffer (séparateur ,)
      this.NbClass = Integer.parseInt(st.nextToken().trim()); // lecture du nombre de classes
      while( st.countTokens() != 0 )
        params.add(new String(st.nextToken().trim()));  // lectures des paramètres
    } catch( NumberFormatException e ) { throw new Exception( LocaleKBCT.GetString("ErrorInTreeFile") + ": " + file_name + "\n" + LocaleKBCT.GetString("UnexpectedValueForNumberOfClass") + " : " + buffer); }

    // determination du type d'arbre
    if( this.NbClass < 0 ) {
      // arbre flou
      this.FuzzyOutput = true;
      this.NbClass = Math.abs(this.NbClass);
    } else if( this.NbClass == 0 )  // arbre régression
        this.Regression = true;
    else if( this.NbClass > 0 )   // arbre classification
        this.Classif = true;

    if( (buffer = br.readLine()) != null ) {
      try {
        // init de la racine de l'arbre
        this.Root = this.CreateNode(buffer);  // creation du noeud racine
        this.Root.InitVariablesGroups(this.VariablesGroups, params);  // Init des noms des variables affichables
        this.TreeModel = new DefaultTreeModel(this.Root);  // creation du model de l'arbre
        while( (buffer = br.readLine()) != null ) {
          // tant que le fichier n'est pas entièrement lu
          FuzzyNode new_node = null;
          new_node = this.CreateNode(buffer); // creation d'un nouveau noeud
          DefaultMutableTreeNode parent_node = this.ParentNode(new_node); // recherche du noeud parent du nouveau noeud
          this.TreeModel.insertNodeInto(new_node, parent_node, parent_node.getChildCount()); // insertion du nouveau noeud dans le noeud parent
        }
      } catch( Exception e ) { throw new Exception( LocaleKBCT.GetString("ErrorInTreeFile") + ": " + file_name + "\n" + e.getMessage() ); }
    }
    br.close(); // fermeture du fichier arbre
    this.setModel(this.TreeModel); // affectation du model à l'arbre
  }
//------------------------------------------------------------------------------
// recherche et retourne le noeud parent du noeud new_node
  public FuzzyNode ParentNode( FuzzyNode new_node ) throws Exception {
    for( int i=0 ; i<this.Nodes.size() ; i++ )    // parmis tous les noeud enregistrés
      if( ((FuzzyNode)this.Nodes.elementAt(i)).Node() == new_node.Parent() )
        return (FuzzyNode)this.Nodes.elementAt(i);

    throw new Exception( LocaleKBCT.GetString("ParentNode") + ": " + Integer.toString(new_node.Parent()) + " " + LocaleKBCT.GetString("NotFoundForNode") + ": " + Integer.toString(new_node.Node()) );
  }
//------------------------------------------------------------------------------
// cré et retourne un noeud en fonction du type d'arbre
  private FuzzyNode CreateNode( String buffer ) throws Exception {
    FuzzyNode new_node = null;
    if( this.Classif == true )
      new_node = new FuzzyNodeClassif( buffer, this.NbClass );  // creation d'un noeud Classif

    if( this.Regression == true )
      new_node = new FuzzyNodeRegression( buffer ); // creation d'un noeud Regression

    if( this.FuzzyOutput == true )
      new_node = new FuzzyNodeFuzzyOutput( buffer, this.NbClass );  // creation d'un noeud flou

    this.Nodes.add(new_node);   // ajout du noeud dans le vecteur des noeuds
    return new_node;
  }
//------------------------------------------------------------------------------
  public int NbClass() { return this.NbClass; }
//------------------------------------------------------------------------------
  public boolean Classif() { return this.Classif; }
//------------------------------------------------------------------------------
  public boolean Regression() { return this.Regression; }
//------------------------------------------------------------------------------
  public boolean FuzzyOutput() { return this.FuzzyOutput; }
}


//------------------------------------------------------------------------------
// classe de base abstraite d'un noeud
abstract class FuzzyNode extends DefaultMutableTreeNode {
  private int Node;                 // numero du noeud
  private int Parent;               // numero du noeud parent
  protected Vector Variables= new Vector();  // vecteur des variables du noeud
  protected StringTokenizer st;
//------------------------------------------------------------------------------
  public FuzzyNode( String buffer ) throws Exception {
    st = new StringTokenizer(buffer, "," ); // decompose le buffer
    if( st.countTokens() < 7 )
      throw new Exception( LocaleKBCT.GetString("UnexpectedFormat") + " : " + buffer );

    // lecture du noeud (partie commune à tous les types de noeuds)
    this.Node = Integer.parseInt(st.nextToken().trim());  // lecture du numero du noeud
    this.Variables.add(new String( st.nextToken().trim() + " " + st.nextToken().trim() ));  // lecture du label variable + label sef
    this.Parent = Integer.parseInt(st.nextToken().trim());  // lecture du numero du noeud parent
    st.nextToken(); // saute le label feuille
    this.Variables.add( new Integer(st.nextToken().trim()) ); // lecture du nombre d'exemples
    this.Variables.add( new Double(st.nextToken().trim()) );  // lecture entropie
    this.setUserObject(this.Variables.elementAt(0));
  }
//------------------------------------------------------------------------------
// retourne le numero du noeud parent
  public int Parent() { return this.Parent; }
//------------------------------------------------------------------------------
// retourne le numero du noeud
  public int Node() { return this.Node; }
//------------------------------------------------------------------------------
// rempli le vecteur display_variables avec le nom des variables
  public void InitVariablesGroups( Vector display_variables, Vector params ) {
    display_variables.add(new DisplayVariableGroup(new String("Samples")));
    display_variables.add(new DisplayVariableGroup(new String("Entropie")));
  }
}


//------------------------------------------------------------------------------
// classe noeud classif
class FuzzyNodeClassif extends FuzzyNode {
  static final long serialVersionUID=0;	
  private int nb_class; // nombre de classes pour la classif
//------------------------------------------------------------------------------
  public FuzzyNodeClassif(  String buffer, int nb_class ) throws Exception {
    super( buffer );
    this.nb_class = nb_class;
    // lecture du noeud
    this.Variables.add(new Integer(st.nextToken().trim()) );  // lecture de la classe majoritaire
    for( int i=0 ; i<nb_class ; i++ )
      this.Variables.add(new Double(st.nextToken().trim()));  // lecture des effectifs par classe
  }
//------------------------------------------------------------------------------
// rempli le vecteur display_variables avec le nom des variables
  public void InitVariablesGroups( Vector display_variables, Vector params ) {
    super.InitVariablesGroups(display_variables, params);
    // le nom de la variable du groupe MajorityClass est affiché sur 2 lignes
    display_variables.add(new DisplayVariableGroup(new String("MajorityClass"), new String("Majority\nClass")));
    // les effectifs par classe sont gérés par un groupe de variables ClassEffectif
    DisplayVariableGroup new_dv = new DisplayVariableGroup(new String("ClassEffectif"), Integer.toString(1));
    for( int i=1 ; i<this.nb_class ; i++ )
      new_dv.AddVariableName(Integer.toString(i+1));

    display_variables.add(new_dv);
  }
}


//------------------------------------------------------------------------------
// classe noeud regression
class FuzzyNodeRegression extends FuzzyNode {
  static final long serialVersionUID=0;	
//------------------------------------------------------------------------------
  public FuzzyNodeRegression( String buffer ) throws Exception {
    super( buffer );
    // lecture du noeud
    this.Variables.add(new Double(st.nextToken().trim()) ); // lecture moyenne
    this.Variables.add(new Double(st.nextToken().trim()) ); // lecture ecart-type
  }
//------------------------------------------------------------------------------
// rempli le vecteur display_variables avec le nom des variables
  public void InitVariablesGroups( Vector display_variables, Vector params ) {
    super.InitVariablesGroups(display_variables, params);
    display_variables.add(new DisplayVariableGroup(new String("Mean")));
    display_variables.add(new DisplayVariableGroup(new String("StandardDeviation")));
  }
}


//------------------------------------------------------------------------------
// classe noeud flou
class FuzzyNodeFuzzyOutput extends FuzzyNode {
  static final long serialVersionUID=0;	
  private int nb_class; // nombre de classe (en fait nombre de sef dans ce cas)
//------------------------------------------------------------------------------
  public FuzzyNodeFuzzyOutput(  String buffer, int nb_class ) throws Exception {
    super( buffer );
    this.nb_class = nb_class;
    // lecture du noeud
    this.Variables.add(new String(st.nextToken().trim()));  // lecture sef majoritaire
    for( int i=0 ; i<nb_class ; i++ )
      this.Variables.add(new Double(st.nextToken().trim()));  // lecture de l'effectif par sef
  }
//------------------------------------------------------------------------------
// rempli le vecteur display_variables avec le nom des variables
  public void InitVariablesGroups( Vector display_variables, Vector params ) {
    super.InitVariablesGroups(display_variables, params);
    // le nom de la variable du groupe MajorityClass est affiché sur 2 lignes
    display_variables.add(new DisplayVariableGroup(new String("MajorityClass"), new String("Majority\nClass")));
    // les effectifs par sef sont gérés par un groupe de variables ClassEffectif
    DisplayVariableGroup new_dv = new DisplayVariableGroup(new String("ClassEffectif"), (String)params.elementAt(0));
    for( int i=1 ; i<this.nb_class ; i++ )
      new_dv.AddVariableName((String)params.elementAt(i));

    display_variables.add(new_dv);
  }
}
