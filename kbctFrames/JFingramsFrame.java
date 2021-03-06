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
//                          JBeginnerFrame.java
//
//
//**********************************************************************

package kbctFrames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import java.io.File;
import java.util.Vector;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
//import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
//import javax.swing.JPopupMenu;
//import javax.swing.SwingUtilities;
//import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
//import javax.swing.event.HyperlinkEvent;
//import javax.swing.event.HyperlinkListener;
//import javax.swing.JButton;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
/* kristov: BEGIN add some necessary imports for DOM manipulation */
import org.apache.batik.dom.ExtendedNode;
import org.w3c.dom.*;
import org.w3c.dom.events.*;
import org.w3c.dom.svg.*;
/* kristov: END add some necessary imports for DOM manipulation */

import org.freehep.util.export.ExportDialog;
import print.JPrintPreview;

//import kbct.JKBCT;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbct.Edge;
import kbct.Vertex;
import kbct.Graph;
import kbct.DijkstraAlgorithm;
import kbct.PageRank;

import kbctAux.DoubleField;
import kbctAux.MessageKBCT;
import kbctAux.Translatable;

/**
 * kbctFrames.JFingramsFrame creates a Frame to display Fingrams.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
public class JFingramsFrame extends JFrame implements Translatable {
  static final long serialVersionUID=0;	
  private JKBCTFrame parent;
  private JMenuBar jMenuBarBF= new JMenuBar();
  private ImageIcon icon_sup = LocaleKBCT.getIconSUP();
  private JLabel jlabelSUP = new JLabel(icon_sup,JLabel.CENTER);
  private JMenuItem jbSup= new JMenuItem();
  private ImageIcon icon_sdown = LocaleKBCT.getIconSDOWN();
  private JLabel jlabelSDOWN = new JLabel(icon_sdown,JLabel.CENTER);
  private JMenuItem jbSdown= new JMenuItem();
  private ImageIcon icon_sleft = LocaleKBCT.getIconSLEFT();
  private JLabel jlabelSLEFT = new JLabel(icon_sleft,JLabel.CENTER);
  private JMenuItem jbSright= new JMenuItem();
  private ImageIcon icon_sright = LocaleKBCT.getIconSRIGHT();
  private JLabel jlabelSRIGHT = new JLabel(icon_sright,JLabel.CENTER);
  private JMenuItem jbSleft= new JMenuItem();
  private JMenuItem jMenuPrint= new JMenuItem();
  private JMenuItem jMenuExport= new JMenuItem();
  private JMenuItem jMenuHelp= new JMenuItem();
  private JMenuItem jMenuClose= new JMenuItem();
  private ImageIcon icon_kbct = LocaleKBCT.getIconGUAJE();
  // The SVG canvas.
  JSVGCanvas svgCanvas = new JSVGCanvas();
  JSVGCanvas svgCanvasLegend = new JSVGCanvas();
  private ImageIcon icon_zin = LocaleKBCT.getIconZIN();
  private JLabel jlabelZIN = new JLabel(icon_zin,JLabel.CENTER);
  private JMenuItem jbZin= new JMenuItem();
  private ImageIcon icon_zout = LocaleKBCT.getIconZOUT();
  private JLabel jlabelZOUT = new JLabel(icon_zout,JLabel.CENTER);
  private JMenuItem jbZout= new JMenuItem();
  private ImageIcon icon_zres = LocaleKBCT.getIconZRES();
  private JLabel jlabelZRES = new JLabel(icon_zres,JLabel.CENTER);
  private JMenuItem jbZres= new JMenuItem();
  private JScrollPane jScrollPanelMain= new JScrollPane();
  private JScrollPane jScrollPanelMeasures= new JScrollPane();
  private JScrollPane jScrollPanelLegend= new JScrollPane();
  private JPanel jPanelMeasures= new JPanel();
  private int NbRules;
  private Vector edges;
  private Vector nodes;
  private double[][] matrix;
  private double[] DoC;
  private double[] CoC;
  private double[] CoI;
  private double[] PR;
  private double[] CovData;
  private double[] Goodness;
  private boolean[] actRules;
  private DoubleField[] jdfDoC;
  private DoubleField[] jdfCoC;
  private DoubleField[] jdfCoI;
  private DoubleField[] jdfPR;
  private DoubleField[] jdfCovData;
  private DoubleField[] jdfGoodness;
  private JPanel jPanelDegreeOfCentrality;
  private JPanel jPanelCentralityOfCloseness;
  private JPanel jPanelCentralityOfIntermediation;
  private JPanel jPanelPageRank;
  private JPanel jPanelCoverageData;
  private JPanel jPanelGoodness;
  
  //----------------------------------------------------------------------------
  public JFingramsFrame(JKBCTFrame jkf, String imageTitle, String imageFile) {
    try {
    //	System.out.println("imageTitle="+imageTitle);
    //	System.out.println("imageFile="+imageFile);
      this.parent= jkf;
      this.jbInit(imageTitle, imageFile);
      jbZin.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { makeZoomIN(); } });
      jbZout.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { makeZoomOUT(); } });
      jbZres.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { makeZoomRES(); } });
      jMenuHelp.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuHelp_actionPerformed(); }} );
      jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { this_windowClose(); }} );
      jMenuPrint.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuPrint_actionPerformed(); } });
      jMenuExport.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jMenuExport_actionPerformed(); } });
      this.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) { this_windowClosing(); }
        public void windowActivated(WindowEvent e) { }
      });
      jbSup.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { makeShiftUP(); } });
	  this.jbSup.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent ae) {
        	  makeShiftUP();
          }
      });
	  this.jbSdown.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent ae) {
        	  makeShiftDOWN();
          }
      });
	  this.jbSright.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent ae) {
        	  makeShiftRIGHT();
          }
      });
	  this.jbSleft.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent ae) {
        	  makeShiftLEFT();
          }
      });
	  JKBCTFrame.AddTranslatable(this);
    } catch (Throwable t) {
    	t.printStackTrace();
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in building JFingramsFrame: "+t);
    }
  }
  //----------------------------------------------------------------------------
  private void jbInit(String t, String f) throws Throwable {
    this.setIconImage(icon_kbct.getImage());
    this.setTitle(LocaleKBCT.GetString("Fingrams")+": "+t);
    this.setState(JBeginnerFrame.NORMAL);
    this.setJMenuBar(this.jMenuBarBF);
    this.jbZin.add(this.jlabelZIN);
    this.jMenuBarBF.add(this.jbZin);
    this.jbZout.add(this.jlabelZOUT);
    this.jMenuBarBF.add(this.jbZout);
    this.jbZres.add(this.jlabelZRES);
    this.jMenuBarBF.add(this.jbZres);
    this.jbSleft.add(this.jlabelSLEFT);
    this.jMenuBarBF.add(this.jbSleft);
    this.jbSup.add(this.jlabelSUP);
    this.jMenuBarBF.add(this.jbSup);
    this.jbSdown.add(this.jlabelSDOWN);
    this.jMenuBarBF.add(this.jbSdown);
    this.jbSright.add(this.jlabelSRIGHT);
    this.jMenuBarBF.add(this.jbSright);
    this.jMenuBarBF.add(this.jMenuPrint);
    this.jMenuBarBF.add(this.jMenuExport);
    this.jMenuBarBF.add(this.jMenuHelp);
    this.jMenuBarBF.add(this.jMenuClose);
    this.jMenuPrint.setToolTipText(f);
    this.jMenuExport.setToolTipText(f);
    this.jMenuHelp.setToolTipText(f);
    this.jMenuClose.setToolTipText(f);

	JTabbedPane main_TabbedPane = new JTabbedPane();
	//JPanel jPanelSaisieF = new JPanel(new GridBagLayout());
	//JPanel jPanelSaisieM = new JPanel(new GridBagLayout());

	main_TabbedPane.addTab(LocaleKBCT.GetString("Fingram"), null, jScrollPanelMain, LocaleKBCT.GetString("Fingram"));
	main_TabbedPane.addTab(LocaleKBCT.GetString("Measures"), null, jScrollPanelMeasures, LocaleKBCT.GetString("Measures"));
	main_TabbedPane.addTab(LocaleKBCT.GetString("Legend"), null, jScrollPanelLegend, LocaleKBCT.GetString("Legend"));

	this.getContentPane().setLayout(new GridBagLayout());
	this.getContentPane().add(main_TabbedPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    this.nodes= new Vector();
    this.edges= new Vector();
    NbRules= MainKBCT.getJMF().jef.Temp_kbct.GetNbRules();
    matrix= new double[NbRules][NbRules];
    DoC= new double[NbRules];
    CoC= new double[NbRules];
    CoI= new double[NbRules];
    PR= new double[NbRules];
    CovData= new double[NbRules];
    Goodness= new double[NbRules];
    
	this.svgCanvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {
        public void documentLoadingCompleted(SVGDocumentLoaderEvent evt) {
            SVGDocument svgDoc = svgCanvas.getSVGDocument();
            SVGSVGElement rootElement = svgDoc.getRootElement();
            linkNodes(rootElement);
            //System.out.println("Loaded SVG: "+svgDoc.getDocumentURI());
            List<Vertex> graphnodes= new ArrayList<Vertex>();
        	Object[] nod= nodes.toArray();
        	//System.out.println("NR="+NbRules);
        	boolean[] nodeRules= new boolean[NbRules];
        	for (int n=0; n<nod.length; n++) {
       		     String aux= nod[n].toString();
       		     //System.out.println(aux);
       		     if (!aux.contains("UNCOVERED INSTANCES (")) {
       		       aux= aux.replace(":", "--");
        		   aux= aux.replace("(cov=", "--");
       		       aux= aux.replace("; G=", "--");
       		       //aux= aux.replace("; C=", "--");
        		   String[] parts1= aux.split("--");
        		   int rule= (new Integer(parts1[0].substring(4))).intValue();
        		   nodeRules[rule-1]= true;
       		       //System.out.println("rule: "+rule);
       		       //System.out.println("parts1: "+parts1.length);
       		       //System.out.println("parts1[0]: "+parts1[0]);
       		       //System.out.println("parts1[1]: "+parts1[1]);
       		       //System.out.println("parts1[2]: "+parts1[2]);
       		       //System.out.println("parts1[3]: "+parts1[3]);
       		       double covData= (new Double(parts1[2].substring(0,parts1[2].length()-1))).doubleValue();
       		       CovData[rule-1]=covData;
       		       //System.out.println("  covData: "+covData);
       		       //System.out.println("  part: "+parts1[parts1.length-2]);
                   //double good= (new Double(parts1[3].substring(0,parts1[3].length()-1))).doubleValue();
       		       int indaux= parts1[3].indexOf(";");
       		       if (indaux==-1)
       		      	  indaux= parts1[3].length()-1;
       		    	
       		       //System.out.println("  indaux: "+indaux);
                   double good= (new Double(parts1[3].substring(0,indaux))).doubleValue();
       		       Goodness[rule-1]=good;
       		       //System.out.println("  good: "+good);
       		       //System.out.println("node"+String.valueOf(n+1)+": "+aux);
        	  }
        	}
        	for (int n=0; n<NbRules; n++) {
        		 if (nodeRules[n]) {
        			 int rule= n+1;
         			 Vertex location = new Vertex("R" + rule, "R" + rule);
        			 graphnodes.add(location);
        		 }
        	}
            List<Edge> graphedges= new ArrayList<Edge>();
            Object[] ed= edges.toArray();
        	//System.out.println("ed.length= "+ed.length);
        	for (int n=0; n<ed.length; n++) {
        		 String aux= ed[n].toString();
        		 aux= aux.replace("(", "--");
        		 aux= aux.replace("->", "--");
        		 //System.out.println("edge"+String.valueOf(n+1)+": "+aux);
        		 String[] parts1= aux.split("--");
        		 int HeadRule= (new Integer(parts1[0].substring(1,parts1[0].length()-1))).intValue();
        		 //System.out.println("  HR="+HeadRule);
        		 //System.out.println("  parts1[1]="+parts1[1]);
        		 int TailRule= (new Integer(parts1[1].substring(2,parts1[1].length()-1))).intValue();
        		 //System.out.println("  TR="+TailRule);
        		 double weight= (new Double(parts1[2].substring(0,parts1[2].length()-1))).doubleValue();
        		 //System.out.println("  W="+weight);
        		 //System.out.println("HR="+HeadRule+"  TR="+TailRule+"  W="+weight);
        		 // Symmetric matrix
        		 matrix[HeadRule-1][TailRule-1]= weight;
        		 matrix[TailRule-1][HeadRule-1]= weight;
        		 int duration=1;
        		 if (weight==0)
        			 duration=0;
        		 
        		 if ( (nodeRules[HeadRule-1]) && ((nodeRules[TailRule-1])) ) {
        			 Vertex locH = new Vertex("R" + HeadRule, "R" + HeadRule);
        			 int indH= graphnodes.indexOf(locH);
        			 //System.out.println("HeadRule"+HeadRule+"  -> "+indH);
        			 Vertex locT = new Vertex("R" + TailRule, "R" + TailRule);
        			 int indT= graphnodes.indexOf(locT);
        			 //System.out.println("TailRule"+TailRule+"  -> "+indT);
        		     Edge lane = new Edge("Edge"+n, graphnodes.get(indH), graphnodes.get(indT), duration);
        		     graphedges.add(lane);       
        		     Edge symlane = new Edge("Edge"+String.valueOf(n+1), graphnodes.get(indT), graphnodes.get(indH), duration);
        		     graphedges.add(symlane);    
        		 }
            }
        	//Object[] objn= graphnodes.toArray();
        	//for (int n=0; n<objn.length; n++) {
        	//	 System.out.println(graphnodes.get(n).toString());
        	//}
        	//Object[] obje= graphedges.toArray();
        	//for (int n=0; n<obje.length; n++) {
        	//	 System.out.println(graphedges.get(n).toString());
        	//}
        	Graph graph = new Graph(graphnodes, graphedges);
    		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
    		PageRank pr= new PageRank(graph);
    		/*dijkstra.execute(graphnodes.get(0));
    		System.out.println("sourceNode: "+graphnodes.get(0).getName()); // R1
    		System.out.println("endNode: "+graphnodes.get(4).getName()); // R5
    		LinkedList<Vertex> path = dijkstra.getPath(graphnodes.get(4));
    		if ( (path != null) && (path.size() > 0) ) {
    	 	    for (Vertex vertex : path) {
    			     System.out.println(vertex);
    			     System.out.println(path.size()); // Number of nodes in the path
    		    }
    		}*/
    		int cspaths=0;
            for (int i=0; i<NbRules; i++) {
            	if (nodeRules[i]) {
            	 String ri= "R" + String.valueOf(i+1);
         		 Vertex locRi = new Vertex(ri, ri);
        		 int indRi= graphnodes.indexOf(locRi);
            	 dijkstra.execute(graphnodes.get(indRi));
            	 int totalDist=0;
            	 PR[i]= pr.rank(ri);
            	 for (int j=0; j<NbRules; j++) {
            		  if (matrix[i][j] > 0) {
            			  DoC[i]++;
            		  }
            		  if (nodeRules[j]) {
             			Vertex locRj = new Vertex("R" + String.valueOf(j+1), "R" + String.valueOf(j+1));
            			int indRj= graphnodes.indexOf(locRj);
              		    LinkedList<Vertex> path= dijkstra.getPath(graphnodes.get(indRj));
              		    if ( (path != null) && (path.size() > 0) ) {
        			      //System.out.print(path.size()+" ->	"); // Number of nodes in the path
            			  totalDist= totalDist+path.size()-1;
            			  //CoC[i]= CoC[i]+1/(double)(path.size()-1);
        			      path.removeFirst();
        			      path.removeLast();
            	 	      for (Vertex vertex : path) {
            			     //System.out.print(vertex+"	");
            			     CoI[(new Integer(vertex.getName().substring(1))).intValue()-1]++;
            		      }
       			          //System.out.println();
       			          cspaths++;
            		    }
            		  }
            	 }
            	 if (totalDist>0)
            	     CoC[i]=1/(double)totalDist;
            	 else
            		 CoC[i]=0;
            	}	 
            }
            //System.out.println("cspaths -> "+cspaths);
            //System.out.println("lim -> "+String.valueOf((NbRules-1)*(NbRules-2)));
            for (int i=0; i<NbRules; i++) {
          	     //System.out.println(CoC[i]);
           	     //System.out.println(CoI[i]);
        	     if (cspaths > 0)
        	         CoI[i]=(double)CoI[i]/cspaths;
        	     else 
            	     CoI[i]=0;
            }
            int[] rankDoC= getRuleRanking(DoC);
            int[] rankCoC= getRuleRanking(CoC);
            int[] rankCoI= getRuleRanking(CoI);
            int[] rankPR= getRuleRanking(PR);
            int[] rankCovData= getRuleRanking(CovData);
            int[] rankGoodness= getRuleRanking(Goodness);
            jdfDoC= new DoubleField[NbRules];
            jdfCoC= new DoubleField[NbRules];
            jdfCoI= new DoubleField[NbRules];
            jdfPR= new DoubleField[NbRules];
            jdfCovData= new DoubleField[NbRules];
            jdfGoodness= new DoubleField[NbRules];
            for (int n=0; n<NbRules; n++) {
                //System.out.println("R"+String.valueOf(n+1)+" -> "+rankDoC[n]);
            	if (actRules[rankDoC[n]]) {
                    JLabel jLabelDoC= new JLabel();
                    jPanelDegreeOfCentrality.add(jLabelDoC, new GridBagConstraints(0, n, 1, 1, 0.0, 0.0
                          ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 20, 5, 5), 0, 0));

                    jdfDoC[n]= new DoubleField(5);
                    //jdfDoC[n].setEnabled(false);
                    jdfDoC[n].setBackground(Color.WHITE);
                    jdfDoC[n].setForeground(Color.BLUE);
                    jdfDoC[n].setEditable(false);
                    jdfDoC[n].setValue(DoC[rankDoC[n]]);
                    jPanelDegreeOfCentrality.add(jdfDoC[n], new GridBagConstraints(1, n, 1, 1, 0.0, 0.0
                          ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 60, 0));

                    jLabelDoC.setText("R" + String.valueOf(rankDoC[n]+1) + " :");
            	}
            	if (actRules[rankCoC[n]]) {
                    JLabel jLabelCoC= new JLabel();
                    jPanelCentralityOfCloseness.add(jLabelCoC, new GridBagConstraints(0, n, 1, 1, 0.0, 0.0
                          ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 20, 5, 5), 0, 0));

                    jdfCoC[n]= new DoubleField(5);
                    //jdfCoC[n].setEnabled(false);
                    jdfCoC[n].setBackground(Color.WHITE);
                    jdfCoC[n].setForeground(Color.BLUE);
                    jdfCoC[n].setEditable(false);
                    //System.out.println(CoC[rankCoC[n]]);
                    jdfCoC[n].setValue(CoC[rankCoC[n]]);
                    jPanelCentralityOfCloseness.add(jdfCoC[n], new GridBagConstraints(1, n, 1, 1, 0.0, 0.0
                          ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 60, 0));

                    jLabelCoC.setText("R" + String.valueOf(rankCoC[n]+1) + " :");
            	}
            	if (actRules[rankCoI[n]]) {
                    JLabel jLabelCoI= new JLabel();
                    jPanelCentralityOfIntermediation.add(jLabelCoI, new GridBagConstraints(0, n, 1, 1, 0.0, 0.0
                          ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 20, 5, 5), 0, 0));

                    jdfCoI[n]= new DoubleField(5);
                    //jdfCoI[n].setEnabled(false);
                    jdfCoI[n].setBackground(Color.WHITE);
                    jdfCoI[n].setForeground(Color.BLUE);
                    jdfCoI[n].setEditable(false);
                    jdfCoI[n].setValue(CoI[rankCoI[n]]);
                    jPanelCentralityOfIntermediation.add(jdfCoI[n], new GridBagConstraints(1, n, 1, 1, 0.0, 0.0
                          ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 60, 0));

                    jLabelCoI.setText("R" + String.valueOf(rankCoI[n]+1) + " :");
            	}
            	if (actRules[rankPR[n]]) {
                    JLabel jLabelPR= new JLabel();
                    jPanelPageRank.add(jLabelPR, new GridBagConstraints(0, n, 1, 1, 0.0, 0.0
                          ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 20, 5, 5), 0, 0));

                    jdfPR[n]= new DoubleField(5);
                    //jdfPR[n].setEnabled(false);
                    jdfPR[n].setBackground(Color.WHITE);
                    jdfPR[n].setForeground(Color.BLUE);
                    jdfPR[n].setEditable(false);
                    jdfPR[n].setValue(PR[rankPR[n]]);
                    jPanelPageRank.add(jdfPR[n], new GridBagConstraints(1, n, 1, 1, 0.0, 0.0
                          ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 60, 0));

                    jLabelPR.setText("R" + String.valueOf(rankPR[n]+1) + " :");
            	}
            	if (actRules[rankCovData[n]]) {
                    JLabel jLabelCovData= new JLabel();
                    jPanelCoverageData.add(jLabelCovData, new GridBagConstraints(0, n, 1, 1, 0.0, 0.0
                          ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 20, 5, 5), 0, 0));

                    jdfCovData[n]= new DoubleField(5);
                    //jdfCovData[n].setEnabled(false);
                    jdfCovData[n].setBackground(Color.WHITE);
                    jdfCovData[n].setForeground(Color.BLUE);
                    jdfCovData[n].setEditable(false);
                    jdfCovData[n].setValue(CovData[rankCovData[n]]);
                    jPanelCoverageData.add(jdfCovData[n], new GridBagConstraints(1, n, 1, 1, 0.0, 0.0
                          ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 60, 0));

                    jLabelCovData.setText("R" + String.valueOf(rankCovData[n]+1) + " :");
            	}
            	if (actRules[rankGoodness[n]]) {
                    JLabel jLabelGoodness= new JLabel();
                    jPanelGoodness.add(jLabelGoodness, new GridBagConstraints(0, n, 1, 1, 0.0, 0.0
                          ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 20, 5, 5), 0, 0));

                    jdfGoodness[n]= new DoubleField(5);
                    jdfGoodness[n].setBackground(Color.WHITE);
                    jdfGoodness[n].setForeground(Color.BLUE);
                    jdfGoodness[n].setEditable(false);
                    jdfGoodness[n].setValue(Goodness[rankGoodness[n]]);
                    jPanelGoodness.add(jdfGoodness[n], new GridBagConstraints(1, n, 1, 1, 0.0, 0.0
                          ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 60, 0));

                    jLabelGoodness.setText("R" + String.valueOf(rankGoodness[n]+1) + " :");
            	}
            }
         }
    });

	/*this.svgCanvasLegend.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {
        public void documentLoadingCompleted(SVGDocumentLoaderEvent evt) {
        	System.out.println("SVG file loaded");
        }
    });*/

    File faux = new File(f);
	this.svgCanvas.setURI(faux.toURI().toString());
    this.svgCanvas.setEnableZoomInteractor(true);
    this.svgCanvas.setEnableImageZoomInteractor(true);
    this.svgCanvas.setEnableRotateInteractor(true);
    this.svgCanvas.setEnablePanInteractor(true);
    this.svgCanvas.setEnableResetTransformInteractor(true);
    this.svgCanvas.setToolTipText(f);
    this.svgCanvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
    //this.svgCanvas.setFocusable(false);

    actRules= new boolean[NbRules];
    for (int n=0; n<NbRules; n++) {
    	 actRules[n]= MainKBCT.getJMF().jef.Temp_kbct.GetRule(n+1).GetActive();
    }

    jPanelDegreeOfCentrality= new JPanel(new GridBagLayout());
    TitledBorder titledBorderDoC = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)), LocaleKBCT.GetString("CentralityOfDegree"));
    jPanelDegreeOfCentrality.setBorder(titledBorderDoC);
    jPanelMeasures.add(jPanelDegreeOfCentrality, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    // Closeness
    // It is computed as the inverse of the farness
    // The farness of a node s is defined as the sum of its distances (shortest paths) to all other nodes
    jPanelCentralityOfCloseness= new JPanel(new GridBagLayout());
    TitledBorder titledBorderCoC = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)), LocaleKBCT.GetString("CentralityOfCloseness"));
    jPanelCentralityOfCloseness.setBorder(titledBorderCoC);
    jPanelMeasures.add(jPanelCentralityOfCloseness, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    jPanelCentralityOfIntermediation= new JPanel(new GridBagLayout());
    TitledBorder titledBorderCoI = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)), LocaleKBCT.GetString("CentralityOfIntermediation"));
    jPanelCentralityOfIntermediation.setBorder(titledBorderCoI);
    jPanelMeasures.add(jPanelCentralityOfIntermediation, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    jPanelPageRank= new JPanel(new GridBagLayout());
    TitledBorder titledBorderPR = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)), LocaleKBCT.GetString("PageRank"));
    jPanelPageRank.setBorder(titledBorderPR);
    jPanelMeasures.add(jPanelPageRank, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    jPanelCoverageData= new JPanel(new GridBagLayout());
    TitledBorder titledBorderCov = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)), LocaleKBCT.GetString("Coverage"));
    jPanelCoverageData.setBorder(titledBorderCov);
    jPanelMeasures.add(jPanelCoverageData, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    
    jPanelGoodness= new JPanel(new GridBagLayout());
    TitledBorder titledBorderGoodness = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)), LocaleKBCT.GetString("Goodness"));
    jPanelGoodness.setBorder(titledBorderGoodness);
    jPanelMeasures.add(jPanelGoodness, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    //this.getContentPane().add(this.jScrollPanelMain, new GridBagConstraints(1, 0, 0, 0, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    this.jScrollPanelMain.setWheelScrollingEnabled(true);
    this.jScrollPanelMain.getViewport().setBackground(Color.WHITE);
    this.jScrollPanelMain.getViewport().add(this.svgCanvas, BorderLayout.CENTER);
    
    this.jScrollPanelMeasures.setWheelScrollingEnabled(true);
    this.jScrollPanelMeasures.getViewport().add(this.jPanelMeasures, BorderLayout.CENTER);

    String flegend="";
    if (f.endsWith("InconsistenciesPathfinder.svg"))
    	flegend= f.substring(0,f.length()-29);
    else if (f.endsWith("Pathfinder.svg"))
    	flegend= f.substring(0,f.length()-14);
    else if (f.endsWith("PathfinderInstance.svg"))
    	flegend= f.substring(0,f.length()-22);
    else if (f.endsWith("Complete.svg"))
    	flegend= f.substring(0,f.length()-12);
    	
	flegend= flegend+"legend.svg";
     
    //String flegend= faux.getParent()+System.getProperty("file.separator")+"prueba.svg";
    //System.out.println("flegend -> "+flegend);
    File fauxlegend = new File(flegend);
	this.svgCanvasLegend.setURI(fauxlegend.toURI().toString());
    this.svgCanvasLegend.setEnableZoomInteractor(false);
    this.svgCanvasLegend.setEnableImageZoomInteractor(false);
    this.svgCanvasLegend.setEnableRotateInteractor(false);
    this.svgCanvasLegend.setEnablePanInteractor(false);
    this.svgCanvasLegend.setEnableResetTransformInteractor(false);
    //this.svgCanvasLegend.setToolTipText(flegend);
    this.svgCanvasLegend.setDocumentState(JSVGCanvas.ALWAYS_STATIC);

    this.jScrollPanelLegend.setWheelScrollingEnabled(false);
    this.jScrollPanelLegend.getViewport().setBackground(Color.WHITE);
    this.jScrollPanelLegend.getViewport().add(this.svgCanvasLegend, BorderLayout.CENTER);

    //this.pack();
    this.setLocation(JChildFrame.ChildPosition(MainKBCT.getJMF(), this.getSize()));
    this.setSize(550, 400);
	this.setVisible(true);
	this.setResizable(true);
    this.Translate();
  }
  //----------------------------------------------------------------------------
  public void Translate() {
    this.jbZin.setToolTipText(LocaleKBCT.GetString("ZoomIn")+" (CTRL+I)");
    this.jbZout.setToolTipText(LocaleKBCT.GetString("ZoomOut")+" (CTRL+O)");
    this.jbZres.setToolTipText(LocaleKBCT.GetString("ResetZoom"));
    this.jbSup.setToolTipText(LocaleKBCT.GetString("ShiftUp")+" (SHIFT+DOWN)");
    this.jbSdown.setToolTipText(LocaleKBCT.GetString("ShiftDown")+" (SHIFT+UP)");
    this.jbSright.setToolTipText(LocaleKBCT.GetString("ShiftRight")+" (SHIFT+LEFT)");
    this.jbSleft.setToolTipText(LocaleKBCT.GetString("ShiftLeft")+" (SHIFT+RIGHT)");
    this.jMenuPrint.setText(LocaleKBCT.GetString("Print"));
    this.jMenuExport.setText(LocaleKBCT.GetString("Export"));
    this.jMenuHelp.setText(LocaleKBCT.GetString("Help"));
    this.jMenuClose.setText(LocaleKBCT.GetString("Close"));
    this.repaint();
  }
  //------------------------------------------------------------------------------
  public void jMenuHelp_actionPerformed() {
      MainKBCT.setJB(new JBeginnerFrame("expert/ExpertButtonFingrams.html#Apply"));
	  MainKBCT.getJB().setVisible(true);
	  SwingUtilities.updateComponentTreeUI(this);
  }
  //------------------------------------------------------------------------------
  public void jMenuPrint_actionPerformed() {
	    try {
	        Printable p= new Printable() {
	          public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
	            if (pageIndex >= 1)
	              return Printable.NO_SUCH_PAGE;
	            else
	              return JFingramsFrame.this.print(graphics, pageFormat);
	          }
	        };
	        new JPrintPreview(this, p);
	      } catch (Exception ex) {
	    	  ex.printStackTrace();
	    	  MessageKBCT.Error(null, ex);
	      }
  }
//------------------------------------------------------------------------------
  public int print(java.awt.Graphics g, PageFormat pageFormat) throws PrinterException {
     java.awt.Graphics2D  g2 = ( java.awt.Graphics2D )g;
     double scalew=1;
     double scaleh=1;
     double pageHeight = pageFormat.getImageableHeight();
     double pageWidth = pageFormat.getImageableWidth();
     if(  getWidth() >= pageWidth )
       scalew =  pageWidth / getWidth();

     if(  getHeight() >= pageHeight)
       scaleh =  pageWidth / getHeight();

     g2.translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
     g2.scale( scalew, scaleh );
     this.jScrollPanelMain.print( g2 );
     return Printable.PAGE_EXISTS;
 }
  //------------------------------------------------------------------------------
  public void jMenuExport_actionPerformed() {
	 try {
        ExportDialog export= new ExportDialog();
	    JPanel panel = new JPanel() {
	      static final long serialVersionUID=0;	
	      public void paint(Graphics g) {
	            JFingramsFrame.this.paint(g);
	            g.translate(0, JFingramsFrame.this.getHeight());
	            JFingramsFrame.this.paint(g);
	            g.setColor(Color.gray);
	            g.drawLine(0,0,0, JFingramsFrame.this.getHeight()-1);
	      }
	    };
	    panel.setSize(new Dimension(this.getWidth(), this.getHeight()));
	    export.showExportDialog( panel, "Export view as ...", panel, "export" );
      } catch (Exception ex) { MessageKBCT.Error(null, ex); }
  }
  //------------------------------------------------------------------------------
  public void makeZoomIN() {
      //System.out.println("Make ZOOM");
      try {
          Robot robot = new Robot();
          // Simulate a mouse click
          //robot.mousePress(InputEvent.BUTTON1_MASK);
          //robot.mouseRelease(InputEvent.BUTTON1_MASK);
          // Simulate a key press
          robot.keyPress(KeyEvent.VK_CONTROL);
          robot.keyPress(KeyEvent.VK_I);
          robot.keyRelease(KeyEvent.VK_I);
          robot.keyRelease(KeyEvent.VK_CONTROL);
      } catch (Exception e) {
          e.printStackTrace();
      }            	
  }
  //------------------------------------------------------------------------------
  public void makeZoomOUT() {
  	try {
        Robot robot = new Robot();
        // Simulate a key press
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_O);
        robot.keyRelease(KeyEvent.VK_O);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    } catch (Exception e) {
        e.printStackTrace();
    }            	
  }
  //------------------------------------------------------------------------------
  public void makeZoomRES() {
	  try {
          Robot robot = new Robot();
          // Simulate a key press
          robot.keyPress(KeyEvent.VK_CONTROL);
          robot.keyPress(KeyEvent.VK_T);
          robot.keyRelease(KeyEvent.VK_T);
          robot.keyRelease(KeyEvent.VK_CONTROL);
      } catch (Exception e) {
          e.printStackTrace();
      }            	
  }
  //------------------------------------------------------------------------------
  public void makeShiftUP() {
      try {
          Robot robot = new Robot();
          // Simulate a key press
          robot.keyPress(KeyEvent.VK_SHIFT);
          robot.keyPress(KeyEvent.VK_DOWN);
          robot.keyRelease(KeyEvent.VK_DOWN);
          robot.keyRelease(KeyEvent.VK_SHIFT);
      } catch (Exception e) {
          e.printStackTrace();
      }            	
  }
  //------------------------------------------------------------------------------
  public void makeShiftDOWN() {
	  try {
          Robot robot = new Robot();
          // Simulate a key press
          robot.keyPress(KeyEvent.VK_SHIFT);
          robot.keyPress(KeyEvent.VK_UP);
          robot.keyRelease(KeyEvent.VK_UP);
          robot.keyRelease(KeyEvent.VK_SHIFT);
      } catch (Exception e) {
          e.printStackTrace();
      }            	
  }
  //------------------------------------------------------------------------------
  public void makeShiftLEFT() {
	  try {
          Robot robot = new Robot();
          // Simulate a key press
          robot.keyPress(KeyEvent.VK_SHIFT);
          robot.keyPress(KeyEvent.VK_RIGHT);
          robot.keyRelease(KeyEvent.VK_RIGHT);
          robot.keyRelease(KeyEvent.VK_SHIFT);
      } catch (Exception e) {
          e.printStackTrace();
      }            	
  }
  //------------------------------------------------------------------------------
  public void makeShiftRIGHT() {
	  try {
          Robot robot = new Robot();
          // Simulate a key press
          robot.keyPress(KeyEvent.VK_SHIFT);
          robot.keyPress(KeyEvent.VK_LEFT);
          robot.keyRelease(KeyEvent.VK_LEFT);
          robot.keyRelease(KeyEvent.VK_SHIFT);
      } catch (Exception e) {
          e.printStackTrace();
      }            	
  }
  //------------------------------------------------------------------------------
  private void this_windowClose() {
	  //System.out.println("close fingram window");
      JKBCTFrame.RemoveTranslatable(this);
      this.dispose();
  }
  //------------------------------------------------------------------------------
  public void this_windowClosing() {
    this.this_windowClose();
  }
  //------------------------------------------------------------------------------
  private class MouseClickEventListener implements EventListener {
	  private JFingramsFrame parent;
	  private Node target;
      private String className;
      private String nodeTitle;
      MouseClickEventListener (JFingramsFrame p, Node target, String className, String nodeTitle) {
          this.parent= p;
    	  this.target = target;
          this.className = className;
          this.nodeTitle = nodeTitle;
      }
      public void handleEvent (org.w3c.dom.events.Event evt) {
          //System.out.println ("clicked on: [" + this.className + "] " + this.nodeTitle);
    	  //System.out.println("EVT -> "+evt.getType());
    	  if ( (this.className.equals("node")) && (MainKBCT.getJMF().jef.Temp_kbct.GetNbActiveRules() > 1) ) {
    	      /* output info */
              //System.out.println ("clicked on: [" + this.className + "] " + this.nodeTitle);
              String ruleID= this.nodeTitle.split(":")[0];
              //System.out.println("ruleID -> "+ruleID);
              if (!ruleID.contains("UNCOVERED INSTANCES")) {
                int option= MessageKBCT.Confirm(this.parent, LocaleKBCT.GetString("DoYouWantToDeactivateRule")+" "+ruleID+"?");
                //System.out.println("opt: "+option);
                if (option==0) {
            	  try {
            	     int rnumber= (new Integer(ruleID.substring(4))).intValue();
                	 // Deactivate rule
               	     MainKBCT.getJMF().jef.Temp_kbct.SetRuleActive(rnumber, false);
            	     // close window
                     boolean[] selFing= JFingramsFrame.this.parent.getSelectedFingrams();
                     // generate new fingrams and re-open
                     boolean aux= MainKBCT.getConfig().GetTESTautomatic();
                     MainKBCT.getConfig().SetTESTautomatic(true);
                     MainKBCT.getConfig().SetFINGRAMSautomatic(true);
                     JFingramsFrame.this.parent.cancelSVG();
                     MainKBCT.getJMF().jef.jButtonFingrams_actionPerformed(true,selFing);
				     //JFingramsFrame.this.parent.setSelectedFingrams(selFing);
				     //JFingramsFrame.this.parent.jdSVG.repaint();
				     JFingramsFrame.this.parent.displaySelectedFingrams();
                     MainKBCT.getConfig().SetTESTautomatic(aux);
                     JFingramsFrame.this.dispose();
            	  } catch (Throwable t) {
            		 MessageKBCT.Error(null, t);
            	  }
                }
              }
    	  } else if (this.className.equals("edge")) {
              //System.out.println ("clicked on: [" + this.className + "] " + this.nodeTitle);
    	  }
          /* don't propagate event to the parent node */
          evt.stopPropagation();
      }
  }
  private void linkNodes(Node root) {
      /* is it an element node? */
      if (root.getNodeType () == Node.ELEMENT_NODE) {
          Element rootElem = (Element) root;
          /* is it a graph node? */
          if (rootElem.getTagName () == "g") {
              String attrClass = rootElem.getAttribute ("class");
              NodeList titleElements = rootElem.getElementsByTagName ("title");
              /* does it have a title? */
              if (titleElements.getLength () > 0) {
                  Node titleNode = titleElements.item (0);
                  /* does the title contain text? */
                  if (titleNode.hasChildNodes ()) {
                      String title = titleNode.getFirstChild().getNodeValue();
                      if (attrClass.equals("edge")) {
                          //System.out.println("  edge: "+title);
                          this.edges.add(title);
                      } else if (attrClass.equals("node")) {
                    	 // System.out.println("  node: "+title);
                    	  this.nodes.add(title);
                      }
                      /* associate event handler */
                      ((ExtendedNode) rootElem).addEventListener ("click", new MouseClickEventListener(this, rootElem, attrClass, title), false);
                  }
              }
          }
      }
      /* visit descendants (if any) */
      if (root.hasChildNodes ()) {
          NodeList children = root.getChildNodes ();
          for (int i = 0; i < children.getLength (); ++i) {
              linkNodes (children.item (i));
          }
      }
  }
  //------------------------------------------------------------------------------
  private int[] getRuleRanking(double[] weights) {
	  int[] res= new int[NbRules];
	  for (int n=0; n<NbRules; n++) {
		   res[n]= this.getMaxIndex(weights);
		   weights= this.excludeElement(weights, res[n]);
	  }
	  return res;
  }
  //------------------------------------------------------------------------------
  private int getMaxIndex(double[] weights) {
	  int res=0;
	  //for (int n=0; n<weights.length; n++) {
		//   System.out.print(weights[n]+"	");
	  //}
      //System.out.println();
	  double max= weights[0];
	  for (int n=1; n<weights.length; n++) {
		   if (weights[n] > max) {
			   max= weights[n];
			   res= n;
		   }
	  }
	  //System.out.println("max -> "+res);
	  return res;
  }
  //------------------------------------------------------------------------------
  private double[] excludeElement(double[] weights, int ind) {
	  double[] res= new double[weights.length];
	  int c=0;
	  for (int n=0; n<weights.length; n++) {
		   if (n!=ind)
			   res[c++]= weights[n];
		   else
			   res[c++]= -1;
	  }
	  return res;
  }
}