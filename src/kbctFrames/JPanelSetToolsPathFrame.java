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
//                       JPanelSetFisProPathFrame.java
//
//

//**********************************************************************

package kbctFrames;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbctAux.JOpenFileChooser;
import kbctAux.MessageKBCT;
import kbctAux.Translatable;
import kbctFrames.JMainFrame;

/**
 * kbct.JPanelSetToolsPathFrame shows a window in order to set FisPro/ORE/Weka root Paths
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 07/08/15
 */
public class JPanelSetToolsPathFrame extends JDialog implements Translatable {
  static final long serialVersionUID=0;
  private JPanel jp= new JPanel(new GridBagLayout());
  private JLabel jmsg= new JLabel();
  private JTextField jtfFisProPath = new JTextField("C:/Program Files/FisPro/FISPRO-3.5/bin");
  private JButton jbSelectFisProPath = new JButton("Fispro 3.5");
  private JTextField jtfGraphvizPath = new JTextField("C:/Program Files (x86)/Graphviz2.38/bin");
  private JButton jbSelectGraphvizPath = new JButton("Graphviz dot");
  private JButton jbApply= new JButton();
  private JButton jbCancel= new JButton();
  
  public JPanelSetToolsPathFrame() {
  }
  
//------------------------------------------------------------------------------
  public JPanelSetToolsPathFrame(Frame f) {
    super(f);
    this.getContentPane().setLayout(new GridBagLayout());
    this.getContentPane().add(jp, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                  ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    this.jtfFisProPath.setEditable(false);
    this.jtfGraphvizPath.setEditable(false);
    jp.add(this.jmsg, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    jp.add(this.jtfGraphvizPath, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.jbSelectGraphvizPath.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jbSelectGraphvizPath_actionPerformed(); } });
    jp.add(this.jbSelectGraphvizPath, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, -10));
    int c=1;
    	c++;
        jp.add(this.jtfFisProPath, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        this.jbSelectFisProPath.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jbSelectFisProPath_actionPerformed(); } });
        jp.add(this.jbSelectFisProPath, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, -10));
    //}
    this.jbApply.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { try {
		jbApply_actionPerformed();
	} catch (Throwable e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} } });
    jp.add(this.jbApply, new GridBagConstraints(0, c+1, 1, 1, 0.0, 0.0
             ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, -10));
    this.jbCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jbCancel_actionPerformed(); } });
    jp.add(this.jbCancel, new GridBagConstraints(1, c+1, 1, 1, 0.0, 0.0
             ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, -10));
    this.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { this_windowClosing(); } });
    this.Translate();
    this.setSize(600, 150);
    Dimension dim = getToolkit().getScreenSize();
    this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
    this.pack();
    this.setVisible(true);
  }
//------------------------------------------------------------------------------
  public void Translate() {
    this.setTitle(LocaleKBCT.GetString("SetToolsPath"));
    this.jmsg.setText(LocaleKBCT.GetString("MsgSelToolsPath"));
    this.jbApply.setText(LocaleKBCT.GetString("Apply"));
    this.jbCancel.setText(LocaleKBCT.GetString("Cancel"));
  }
//------------------------------------------------------------------------------
  void jbSelectFisProPath_actionPerformed() {
    JOpenFileChooser file_chooser = new JOpenFileChooser("");
    if( !this.jtfFisProPath.getText().equals("") )
      file_chooser.setSelectedFile(new File(this.jtfFisProPath.getText()));

    file_chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if( file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION )
      this.jtfFisProPath.setText(file_chooser.getSelectedFile().getAbsolutePath());
  }
//------------------------------------------------------------------------------
  void jbSelectGraphvizPath_actionPerformed() {
    JOpenFileChooser file_chooser = new JOpenFileChooser("");
    if( !this.jtfGraphvizPath.getText().equals("") )
      file_chooser.setSelectedFile(new File(this.jtfGraphvizPath.getText()));

    file_chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if( file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION )
      this.jtfGraphvizPath.setText(file_chooser.getSelectedFile().getAbsolutePath());
  }
//------------------------------------------------------------------------------
 public void jbApply_actionPerformed() throws Throwable {
	 // System.out.println("entraa");
    try {
      if (this.jtfGraphvizPath.getText().equals("") == true) {
          throw new Exception("SelectAGraphvizPath");
      } else if(this.jtfFisProPath.getText().equals("") == true) {
          throw new Exception("SelectAFisProPath");
      } else {
          if (LocaleKBCT.isWindowsPlatform()) {
    	      String dotexe= this.jtfGraphvizPath.getText() + System.getProperty("file.separator") + "dot.exe";
    	      File fauxg= new File(dotexe);
    	      if (!fauxg.exists())
    	          throw new Exception("SelectAGraphvizPath");
    	      String dllfispro= this.jtfFisProPath.getText() + System.getProperty("file.separator") + "jnifis.dll";
    	      File fauxf= new File(dllfispro);
    	      //System.out.println("ffis -> "+fauxf.getAbsolutePath());
    	      if (!fauxf.exists())
    	          throw new Exception("SelectAFisProPath");
    	      this.SetBatFile(this.jtfGraphvizPath.getText(),this.jtfFisProPath.getText());
          } else {
    	      String dotexe= this.jtfGraphvizPath.getText() + System.getProperty("file.separator") + "dot";
    	      File faux= new File(dotexe);
    	      if (!faux.exists())
    	          throw new Exception("SelectAGraphvizPath");
    	      String sofispro= this.jtfFisProPath.getText() + System.getProperty("file.separator") + "libjnifis.so";
    	      File fauxf= new File(sofispro);
    	      if (!fauxf.exists())
    	          throw new Exception("SelectAFisProPath");
              this.SetMakefile(this.jtfFisProPath.getText(), this.jtfGraphvizPath.getText());
          }
          JMainFrame jm = new JMainFrame();
          jm.InitJKBCTFrameWithKBCT();
          jm.jbInit();
          jm.Events();
          
         // MessageKBCT.Information(this, LocaleKBCT.GetString("LaunchKBCTagain")); //AQUIIIIIII MENSAJEEEE LAUNCH GUAJE AGAIN
          //this.dispose(); //Elimina la ventana
          //System.exit(-1);//Elimina todo lo de la maquina virtual java
          //coger siempre el mismo archivo, y que salga directamente el valor del indice de interpretabilidad, guardarlo en un archivo.
      }
    } catch (Exception ex) {
      if ((ex.getMessage().equals("SelectAFisProPath")) || 
    	   (ex.getMessage().equals("SelectAGraphvizPath")) ) {
    	    String ReadTXT= "ReadTXT";
    	    String msg1=""; //, msg2="";
    	    if (LocaleKBCT.isWindowsPlatform()) {
    	        if (ex.getMessage().equals("SelectAGraphvizPath"))
    	            msg1= "DefaultGraphvizPathWin";
    	        else if (ex.getMessage().equals("SelectAFisProPath"))
    	            msg1= "DefaultFisproPathWin";
    	    } else {
    	        if (ex.getMessage().equals("SelectAGraphvizPath"))
    	            msg1= "DefaultGraphvizPathLin";
    	        else if (ex.getMessage().equals("SelectAFisProPath"))
    	            msg1= "DefaultFisproPathLin";
    	    }
        Throwable exc= new Throwable(LocaleKBCT.GetString(ex.getMessage())+"\n"+
                                     LocaleKBCT.GetString(msg1)+"\n"+
                                     LocaleKBCT.GetString(ReadTXT));
        MessageKBCT.Error(null, exc);
      } else {
          MessageKBCT.Error(null, ex);
      }
      this.dispose();
      System.exit(-1);
    }
  }
//------------------------------------------------------------------------------
  void jbCancel_actionPerformed() { this.close(); }
//------------------------------------------------------------------------------
  void this_windowClosing() { this.close(); }
//------------------------------------------------------------------------------
  void close () {
    String ReadTXT= "ReadTXT";
    String msg;
    if (LocaleKBCT.isWindowsPlatform())
        msg= "YouMustModifyKbctBat";
    else
        msg= "YouMustModifyKbctMake";
    Throwable ex= new Throwable(LocaleKBCT.GetString(msg)+"\n"+LocaleKBCT.GetString(ReadTXT));
    MessageKBCT.Error(null, ex);
    this.dispose();
    System.exit(-1);
  }
//------------------------------------------------------------------------------
  void SetBatFile(String GraphvizPath, String FisproPath) throws Exception {
    String file_name= System.getProperty("user.dir") + System.getProperty("file.separator") + "guaje.bat";
    PrintWriter fOut = new PrintWriter(new FileOutputStream(file_name), true);
    String GUAJEPATH= System.getProperty("user.dir");
    if (GUAJEPATH.startsWith("Archivos de programa (x86)",3))
    	GUAJEPATH= GUAJEPATH.replaceFirst("Archivos de programa (x86)","Archiv~2");
    else if (GUAJEPATH.startsWith("Archivos de programa",3))
    	GUAJEPATH= GUAJEPATH.replaceFirst("Archivos de programa","Archiv~1");
    else if (GUAJEPATH.startsWith("Program Files (x86)",3))
    	GUAJEPATH= GUAJEPATH.replaceFirst("Program Files (x86)","Progra~2");
    else if (GUAJEPATH.startsWith("Program Files",3))
    	GUAJEPATH= GUAJEPATH.replaceFirst("Program Files","Progra~1");
    else if (GUAJEPATH.startsWith("Documents and Settings",3))
    	GUAJEPATH= GUAJEPATH.replaceFirst("Documents and Settings","Docume~1");
    fOut.println("set GUAJEPATH="+"\""+GUAJEPATH+"\"");

    if (FisproPath.startsWith("Archivos de programa",3))
    	FisproPath= FisproPath.replaceFirst("Archivos de programa","Archiv~1");
    else if ( (FisproPath.startsWith("Program Files",3)) &&
    		!(FisproPath.startsWith("Program Files (x86)",3)))
    	FisproPath= FisproPath.replaceFirst("Program Files","Progra~1");
    fOut.println("set FISPRO="+"\""+FisproPath+"\"");
    
  //GraphvizPath = "C:/Program Files (x86)/Graphviz2.38/bin";//estaaaaaaaaaaaaaaaaaa
    if (GraphvizPath.startsWith("Archivos de programa",3))
	    GraphvizPath= GraphvizPath.replaceFirst("Archivos de programa","Archiv~1");
    else if ( (GraphvizPath.startsWith("Program Files",3)) &&
    		!(GraphvizPath.startsWith("Program Files (x86)",3)))
        GraphvizPath= GraphvizPath.replaceFirst("Program Files","Progra~1");

    fOut.println("set GRAPHVIZ="+"\""+GraphvizPath+"\"");
    String base= "%GUAJEPATH%"+System.getProperty("file.separator")+"libs";
    String FINGRAMSPATH= base+System.getProperty("file.separator")+"vismaps";
    fOut.println("set FINGRAMSPATH="+FINGRAMSPATH);
    String WEKAPATH= base+System.getProperty("file.separator")+"wekalib";
    fOut.println("set WEKAPATH="+WEKAPATH);
    String OREPATH= base+System.getProperty("file.separator")+"orelibs";
    fOut.println("set OREPATH="+OREPATH);
    String KEELPATH= base+System.getProperty("file.separator")+"keellibs";
    fOut.println("set KEELPATH="+KEELPATH);
    String XFUZZYPATH= base+System.getProperty("file.separator")+"xfuzzy";
    fOut.println("set XFUZZYPATH="+XFUZZYPATH);
    String ESPRESSOPATH= base+System.getProperty("file.separator")+"espresso";
    fOut.println("set ESPRESSOPATH="+ESPRESSOPATH);
    String EXECPATH= base+System.getProperty("file.separator")+"exec";
    fOut.println("set EXECPATH="+EXECPATH);
    String BATIKPATH= base+System.getProperty("file.separator")+"batik";
    fOut.println("set BATIKPATH="+BATIKPATH);
    String JAMAPATH= base+System.getProperty("file.separator")+"jamalib";
    fOut.println("set JAMAPATH="+JAMAPATH);
    String CPATHFISPRO= "%FISPRO%" +
            System.getProperty("file.separator")+"jar"+
            System.getProperty("file.separator")+"plot.jar;"+
            "%FISPRO%" +
            System.getProperty("file.separator")+"jar"+
            System.getProperty("file.separator")+"util.jar;"+
            "%FISPRO%" +
            System.getProperty("file.separator")+"jar"+
            System.getProperty("file.separator")+"freehep-base.jar;"+
            "%FISPRO%" +
            System.getProperty("file.separator")+"jar"+
            System.getProperty("file.separator")+"freehep-graphics2d.jar;"+
            "%FISPRO%" +
            System.getProperty("file.separator")+"jar"+
            System.getProperty("file.separator")+"freehep-graphicsio.jar;"+
            "%FISPRO%" +
            System.getProperty("file.separator")+"jar"+
            System.getProperty("file.separator")+"freehep-graphicsio-emf.jar;"+
            "%FISPRO%" +
            System.getProperty("file.separator")+"jar"+
            System.getProperty("file.separator")+"freehep-graphicsio-gif.jar;"+
            "%FISPRO%" +
            System.getProperty("file.separator")+"jar"+
            System.getProperty("file.separator")+"freehep-graphicsio-pdf.jar;"+
            "%FISPRO%" +
            System.getProperty("file.separator")+"jar"+
            System.getProperty("file.separator")+"freehep-graphicsio-ps.jar";

    fOut.println("set CPATHFISPRO="+CPATHFISPRO);
    String CPATHFINGRAMS= "%FINGRAMSPATH%" +
            System.getProperty("file.separator") + "fingramsGenerator.jar";

    fOut.println("set CPATHFINGRAMS="+CPATHFINGRAMS);
    String CPATHEXEC= "%EXECPATH%" +
            System.getProperty("file.separator") + "commons-exec-1.3.jar";
    
    fOut.println("set CPATHEXEC="+CPATHEXEC);
    String CPATHKEEL= "%KEELPATH%" +
            System.getProperty("file.separator") + "SMOTE-I.jar;"
            + "%KEELPATH%" +
            System.getProperty("file.separator") + "RunKeel.jar";
    fOut.println("set CPATHKEEL="+CPATHKEEL);

    String CPATHWEKA= "%WEKAPATH%" +
                        System.getProperty("file.separator") + "weka.jar";

    fOut.println("set CPATHWEKA="+CPATHWEKA);
    String CPATHORE1= "%OREPATH%" +
                        System.getProperty("file.separator") + "ORE-UMU.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"oreapi"+
                        System.getProperty("file.separator")+"ore-api.jar";

    fOut.println("set CPATHORE1="+CPATHORE1);
    String CPATHORE2=  "%OREPATH%" +
                        System.getProperty("file.separator")+"jena"+
                        System.getProperty("file.separator")+"arq.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"jena"+
                        System.getProperty("file.separator")+"commons-logging-1.1.1.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"jena"+
                        System.getProperty("file.separator")+"concurrent.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"jena"+
                        System.getProperty("file.separator")+"icu4j_3_4.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"jena"+
                        System.getProperty("file.separator")+"iri.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"jena"+
                        System.getProperty("file.separator")+"jena.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"jena"+
                        System.getProperty("file.separator")+"log4j-1.2.12.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"jena"+
                        System.getProperty("file.separator")+"xercesImpl.jar";

    fOut.println("set CPATHORE2="+CPATHORE2);
    String CPATHORE3=  "%OREPATH%" +
                        System.getProperty("file.separator")+"jgoodies"+
                        System.getProperty("file.separator")+"forms-1.1.0.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"pellet"+
                        System.getProperty("file.separator")+"aterm-java-1.6.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"pellet"+
                        System.getProperty("file.separator")+"pellet.jar";

    fOut.println("set CPATHORE3="+CPATHORE3);
    String CPATHORE4=  "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"commons-lang-2.2.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"owlapi-api.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"owlapi-apibinding.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"owlapi-change.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"owlapi-debugging.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"owlapi-dig1_1.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"owlapi-functionalparser.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"owlapi-functionalrenderer.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"owlapi-impl.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"owlapi-krssparser.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"owlapi-metrics.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"owlapi-oboparser.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"owlxmlparser.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"owlapi-owlxmlrenderer.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"owlapi-rdfapi.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"owlapi-rdfxmlparser.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"owlapi-rdfxmlrenderer.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"owlapi"+
                        System.getProperty("file.separator")+"owlapi-util.jar";

    fOut.println("set CPATHORE4="+CPATHORE4);
    String CPATHORE5=  "%OREPATH%" +
                        System.getProperty("file.separator")+"xsd"+
                        System.getProperty("file.separator")+"relaxngDatatype.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"xsd"+
                        System.getProperty("file.separator")+"xsdlib.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"ws-client"+
                        System.getProperty("file.separator")+"commons-logging-api.jar;"+
                        "%OREPATH%" +
                        System.getProperty("file.separator")+"ws-client"+
                        System.getProperty("file.separator")+"em-client.jar";
    
    fOut.println("set CPATHORE5="+CPATHORE5);
    fOut.println("set CPATHORE=%CPATHORE1%;%CPATHORE2%;%CPATHORE3%;%CPATHORE4%;%CPATHORE5%");
    String CPATHXFUZZY= "%XFUZZYPATH%;"
            + "%XFUZZYPATH%" +
            System.getProperty("file.separator") + "xfuzzy.jar";
    fOut.println("set CPATHXFUZZY="+CPATHXFUZZY);
    String CPATHBATIK= "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-anim.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-awt-util.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-bridge.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-codec.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-css.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-dom.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-ext.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-extension.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-gui-util.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-gvt.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-parser.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-script.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-svg-dom.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-svggen.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-swing.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-transcoder.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-util.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"batik-xml.jar;"+
                        "%BATIKPATH%" +
                        System.getProperty("file.separator")+"xml-apis-ext.jar";
    		
    fOut.println("set CPATHBATIK="+CPATHBATIK);
    String CPATHJAMA= "%JAMAPATH%" +
    		           System.getProperty("file.separator")+"Jama-1.0.2.jar";
    
    fOut.println("set CPATHJAMA="+CPATHJAMA);
    String CPATHGUAJE= "%GUAJEPATH%" +
                      System.getProperty("file.separator") + "libs"+
                      System.getProperty("file.separator") + "guajelib"+
                      System.getProperty("file.separator") + "GUAJE-v3.0.jar";

    fOut.println("set CPATHGUAJE="+CPATHGUAJE);
    fOut.println("set CPATH=%CPATHGUAJE%;%CPATHFISPRO%;%CPATHFINGRAMS%;%CPATHEXEC%;%CPATHWEKA%;%CPATHKEEL%;%CPATHXFUZZY%;%CPATHORE%;%CPATHBATIK%;%CPATHJAMA%");
    
    String exec= "java -Xmx512m"+
                 " -Despressopath=%ESPRESSOPATH% -Dgraphpath=%GRAPHVIZ%"+
                 " -Djava.library.path=%FISPRO%" +
                 " -Dpellet.configuration=file:config" +
                 System.getProperty("file.separator") + "pellet.properties"+
                 " -Dlog4j.configuration=file:config"+
                 System.getProperty("file.separator") + "log4j.properties"+
                 " -classpath %CPATH% kbct.MainKBCT";
    		
	fOut.print(exec);
    fOut.close();
    String fnameSP= System.getProperty("user.dir") 
    		        + System.getProperty("file.separator")
    		        + "scripts" 
    		        + System.getProperty("file.separator") 
    		        + "setPATH.bat";
    File faux= new File(fnameSP);
    
    if (faux.exists()) { 
        PrintWriter fOutSP = new PrintWriter(new FileOutputStream(fnameSP), true);
        fOutSP.println("set GUAJEPATH="+"\""+GUAJEPATH+"\"");
        fOutSP.println("set FISPRO="+"\""+FisproPath+"\"");
        fOutSP.println("set GRAPHVIZ="+"\""+GraphvizPath);
        fOutSP.println("set FINGRAMSPATH="+FINGRAMSPATH);
        fOutSP.println("set WEKAPATH="+WEKAPATH);
        fOutSP.println("set OREPATH="+OREPATH);
        fOutSP.println("set KEELPATH="+KEELPATH);
        fOutSP.println("set ESPRESSOPATH="+ESPRESSOPATH);
        fOutSP.println("set BATIKPATH="+BATIKPATH);
        fOutSP.println("set JAMAPATH="+JAMAPATH);
        fOutSP.println("set XFUZZYPATH="+XFUZZYPATH);
        fOutSP.println("set EXECPATH="+EXECPATH);
        fOutSP.close();
    }
  }
//------------------------------------------------------------------------------
  void SetMakefile(String FisProPath, String GraphvizPath) throws Exception {
    String file_name= "."+System.getProperty("file.separator")+"Makefile";
    LineNumberReader lnr= new LineNumberReader(new InputStreamReader(new FileInputStream(file_name)));
    //String JDK= "/usr/local/jdk-1.6";
    //String GUAJE= "/home/user/GUAJE-vb3.0";
    String GUAJE= System.getProperty("user.dir");
    //String l=null;
    //while((l= lnr.readLine())!=null) {
      //if (l.startsWith("JDK"))
        // JDK= l.substring(4);
      //if (l.startsWith("GUAJE"))
    	//  GUAJE= l.substring(6);
    //}
    PrintWriter fOut = new PrintWriter(new FileOutputStream(file_name), true);
    //fOut.println("# JAVA JDK:  this line should point onto");
    //fOut.println("# your java developpment kit top directory");
    //fOut.println("JDK="+JDK);
    //fOut.println();
    fOut.println("# GUAJE PATH:  this line should point onto");
    fOut.println("# your GUAJE top directory");
    fOut.println("GUAJEPATH="+GUAJE);
    fOut.println();
    fOut.println("# FisPro PATH:  this line should point onto");
    fOut.println("# your FisPro top directory");
    fOut.println("FISPRO="+FisProPath);
    fOut.println();
    fOut.println("# Graphviz PATH:  this line should point onto");
    fOut.println("# your Graphviz top directory");
    fOut.println("GRAPHVIZ="+GraphvizPath);
    fOut.println();
    fOut.println("# FINGRAMS PATH:  this line should point onto");
    fOut.println("# your FINGRAMS top directory");
    fOut.println("FINGRAMSPATH=$(GUAJEPATH)/libs/vismaps");
    fOut.println();
    fOut.println("# Weka PATH:  this line should point onto");
    fOut.println("# your WEKA top directory");
    fOut.println("WEKA=$(GUAJEPATH)/libs/wekalib");
    fOut.println();
    fOut.println("# ORE PATH:  this line should point onto");
    fOut.println("# your ORE top directory");
    fOut.println("ORE=$(GUAJEPATH)/libs/orelibs");
    fOut.println();
    fOut.println("# KEEL PATH:  this line should point onto");
    fOut.println("# your KEEL top directory");
    fOut.println("KEEL=$(GUAJEPATH)/libs/keellibs");
    fOut.println();
    fOut.println("# Espresso PATH:  this line should point onto");
    fOut.println("# your Espresso top directory");
    fOut.println("ESPRESSO=$(GUAJEPATH)/libs/espresso");
    fOut.println();
    fOut.println("# Batik PATH:  this line should point onto");
    fOut.println("# your Batik top directory");
    fOut.println("BATIK=$(GUAJEPATH)/libs/batik");
    fOut.println();
    fOut.println("# JAMA PATH:  this line should point onto");
    fOut.println("# your JAMA top directory");
    fOut.println("JAMA=$(GUAJEPATH)/libs/jamalib");
    fOut.println();
    fOut.println("# XFUZZY PATH:  this line should point onto");
    fOut.println("# your XFUZZY top directory");
    fOut.println("XFUZZYPATH=$(GUAJEPATH)/libs/xfuzzy");
    fOut.println();
    fOut.println("# EXEC PATH:  this line should point onto");
    fOut.println("# your EXEC top directory");
    fOut.println("EXECPATH=$(GUAJEPATH)/libs/exec");
    fOut.println();

    fOut.println("CPATHFISPRO=$(FISPRO)"+
                 System.getProperty("file.separator")+"jar"+
                 System.getProperty("file.separator")+"plot.jar"+
                 ":$(FISPRO)"+
                 System.getProperty("file.separator")+"jar"+
                 System.getProperty("file.separator")+"util.jar"+
                 ":$(FISPRO)"+
                 System.getProperty("file.separator")+"jar"+
                 System.getProperty("file.separator")+"freehep-base.jar"+
                 ":$(FISPRO)"+
                 System.getProperty("file.separator")+"jar"+
                 System.getProperty("file.separator")+"freehep-graphics2d.jar"+
                 ":$(FISPRO)"+
                 System.getProperty("file.separator")+"jar"+
                 System.getProperty("file.separator")+"freehep-graphicsio.jar"+
                 ":$(FISPRO)"+
                 System.getProperty("file.separator")+"jar"+
                 System.getProperty("file.separator")+"freehep-graphicsio-emf"+
                 ":$(FISPRO)"+
                 System.getProperty("file.separator")+"jar"+
                 System.getProperty("file.separator")+"freehep-graphicsio-gif"+
                 ":$(FISPRO)"+
                 System.getProperty("file.separator")+"jar"+
                 System.getProperty("file.separator")+"freehep-graphicsio-pdf"+
                 ":$(FISPRO)"+
                 System.getProperty("file.separator")+"jar"+
                 System.getProperty("file.separator")+"freehep-graphicsio-ps");

    fOut.println("CPATHFINGRAMS=$(FINGRAMSPATH)"+System.getProperty("file.separator") + "fingramsGenerator.jar");
    fOut.println("CPATHWEKA=$(WEKA)"+System.getProperty("file.separator") + "weka.jar");

    fOut.println("CPATHORE1=$(ORE)"+
            System.getProperty("file.separator")+"ORE-UMU.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"oreapi"+
            System.getProperty("file.separator")+"ore-api.jar");

    fOut.println("CPATHORE2=$(ORE)"+
            System.getProperty("file.separator")+"jena"+
            System.getProperty("file.separator")+"arq.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"jena"+
            System.getProperty("file.separator")+"commons-logging-1.1.1.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"jena"+
            System.getProperty("file.separator")+"concurrent.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"jena"+
            System.getProperty("file.separator")+"icu4j_3_4.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"jena"+
            System.getProperty("file.separator")+"iri.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"jena"+
            System.getProperty("file.separator")+"jena.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"jena"+
            System.getProperty("file.separator")+"log4j-1.2.12.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"jena"+
            System.getProperty("file.separator")+"xercesImpl.jar");
    
    fOut.println("CPATHORE3=$(ORE)"+
            System.getProperty("file.separator")+"jgoodies"+
            System.getProperty("file.separator")+"forms-1.1.0.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"pellet"+
            System.getProperty("file.separator")+"aterm-java-1.6.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"pellet"+
            System.getProperty("file.separator")+"pellet.jar");

    fOut.println("CPATHORE4=$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"commons-lang-2.2.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"owlapi-api.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"owlapi-apibinding.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"owlapi-change.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"owlapi-debugging.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"owlapi-dig1_1.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"owlapi-functionalparser.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"owlapi-functionalrenderer.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"owlapi-impl.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"owlapi-krssparser.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"owlapi-metrics.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"owlapi-oboparser.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"owlxmlparser.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"owlapi-owlxmlrenderer.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"owlapi-rdfapi.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"owlapi-rdfxmlparser.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"owlapi-rdfxmlrenderer.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"owlapi"+
            System.getProperty("file.separator")+"owlapi-util.jar");

    fOut.println("CPATHORE5=$(ORE)"+
            System.getProperty("file.separator")+"xsd"+
            System.getProperty("file.separator")+"relaxngDatatype.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"xsd"+
            System.getProperty("file.separator")+"xsdlib.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"ws-client"+
            System.getProperty("file.separator")+"commons-logging-api.jar"+
            ":$(ORE)"+
            System.getProperty("file.separator")+"ws-client"+
            System.getProperty("file.separator")+"em-client.jar");

    fOut.println("CPATHORE=$(CPATHORE1):$(CPATHORE2):$(CPATHORE3):$(CPATHORE4):$(CPATHORE5)");
    fOut.println("CPATHKEEL=$(KEELPATH)"+
            System.getProperty("file.separator") + "SMOTE-I.jar" +
            ":$(KEELPATH)"+
    		System.getProperty("file.separator") + "RunKeel.jar");

    fOut.println("CPATHBATIK=$(BATIK)"+
            System.getProperty("file.separator")+"batik-anim.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"batik-awt-util.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"batik-bridge.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"batik-codec.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"batik-css.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"batik-dom.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"batik-ext.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"batik-extension.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"batik-gui-util.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"batik-gvt.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"batik-parser.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"batik-script.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"batik-svg-dom.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"batik-svggen.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"batik-swing.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"batik-transcoder.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"batik-util.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"batik-xml.jar"+
            ":$(BATIK)"+
            System.getProperty("file.separator")+"xml-apis-ext.jar");

    fOut.println("CPATHJAMA=$(JAMA)"+System.getProperty("file.separator")+"Jama-1.0.2.jar");
    fOut.println("CPATHXFUZZY=$(XFUZZYPATH):$(XFUZZYPATH)"+System.getProperty("file.separator") + "xfuzzy.jar");
    fOut.println("CPATHEXEC=$(EXECPATH)"+System.getProperty("file.separator") + "commons-exec-1.3.jar");
   
    fOut.println("CPATHGUAJE=$(GUAJEPATH)"+
                 System.getProperty("file.separator")+"libs"+
                 System.getProperty("file.separator")+"guajelib"+
                 System.getProperty("file.separator")+"GUAJE-v3.0.jar");
    fOut.println();
    fOut.println("CPATH=$(CPATHGUAJE):$(CPATHFISPRO):$(CPATHFINGRAMS):$(CPATHEXEC):$(CPATHWEKA):$(CPATHKEEL):$(CPATHXFUZZY):$(CPATHORE):$(CPATHBATIK):$(CPATHJAMA)");
    fOut.println();
    fOut.println("execute:");
    //fOut.println("	"+"$(JDK)"+
      //           System.getProperty("file.separator")+"bin"+
        //         System.getProperty("file.separator")+"java"+
    fOut.println("	"+"java"+
                 " -Xmx512m -Dguajepath=$(GUAJEPATH) -Despressopath=$(ESPRESSO) -Dgraphpath=$(GRAPHVIZ) -Djava.library.path=$(FISPRO)"+
                 " -Dpellet.configuration=file:config"+
                 System.getProperty("file.separator")+"pellet.properties"+
                 " -Dlog4j.configuration=file:config"+
                 System.getProperty("file.separator")+"log4j.properties"+
                 " -classpath $(CPATH) kbct.MainKBCT");
    fOut.println();
    fOut.flush();
    fOut.close();

    String fnameTEST= System.getProperty("user.dir") 
	        + System.getProperty("file.separator")
	        + "guajeTEST.sh";
    File faux= new File(fnameTEST);
    if (faux.exists()) { 
      PrintWriter fOutT = new PrintWriter(new FileOutputStream(fnameTEST), true);
      fOutT.println("#!/bin/sh");
      fOutT.println("# guajeTEST.sh");
      fOutT.println("GUAJEPATH="+GUAJE);
      fOutT.println("CPATHGUAJE=$GUAJEPATH"+
            System.getProperty("file.separator")+"libs"+
            System.getProperty("file.separator")+"guajelib"+
            System.getProperty("file.separator")+"GUAJE-v3.0.jar");
      fOutT.println("FISPRO="+FisProPath);
      fOutT.println("FISPROPATH=$FISPRO/jar");
      fOutT.println("CPATHFISPRO=$FISPROPATH"+System.getProperty("file.separator")+"plot.jar");
      fOutT.println("CPATHFISPRO=$CPATHFISPRO:$FISPROPATH"+System.getProperty("file.separator")+"util.jar");
      fOutT.println("CPATHFISPRO=$CPATHFISPRO:$FISPROPATH"+System.getProperty("file.separator")+"freehep-base.jar");
      fOutT.println("CPATHFISPRO=$CPATHFISPRO:$FISPROPATH"+System.getProperty("file.separator")+"freehep-graphics2d.jar");
      fOutT.println("CPATHFISPRO=$CPATHFISPRO:$FISPROPATH"+System.getProperty("file.separator")+"freehep-graphicsio.jar");
      fOutT.println("CPATHFISPRO=$CPATHFISPRO:$FISPROPATH"+System.getProperty("file.separator")+"freehep-graphicsio-emf.jar");
      fOutT.println("CPATHFISPRO=$CPATHFISPRO:$FISPROPATH"+System.getProperty("file.separator")+"freehep-graphicsio-gif.jar");
      fOutT.println("CPATHFISPRO=$CPATHFISPRO:$FISPROPATH"+System.getProperty("file.separator")+"freehep-graphicsio-pdf.jar");
      fOutT.println("CPATHFISPRO=$CPATHFISPRO:$FISPROPATH"+System.getProperty("file.separator")+"freehep-graphicsio-ps.jar");
      fOutT.println("GRAPHVIZ="+GraphvizPath);
      fOutT.println("FINGRAMSPATH=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"vismaps");
      fOutT.println("CPATHFINGRAMS=$FINGRAMSPATH"+System.getProperty("file.separator")+"fingramsGenerator.jar");
      fOutT.println("WEKAPATH=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"wekalib");
      fOutT.println("CPATHWEKA=$WEKAPATH"+System.getProperty("file.separator") + "weka.jar");
      fOutT.println("OREPATH=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"orelibs");
      fOutT.println("CPATHORE=$OREPATH"+System.getProperty("file.separator")+"ORE-UMU.jar:$OREPATH"+System.getProperty("file.separator")+"oreapi"+System.getProperty("file.separator")+"ore-api.jar:$OREPATH"+System.getProperty("file.separator")+"jena"+System.getProperty("file.separator")+"jena.jar:$OREPATH"+System.getProperty("file.separator")+"orelibs"+System.getProperty("file.separator")+"ws-client"+System.getProperty("file.separator")+"em-client.jar");
      fOutT.println("KEELPATH=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"keellibs");
      fOutT.println("CPATHKEEL=$KEELPATH"+System.getProperty("file.separator")+"SMOTE-I.jar:$KEELPATH"+System.getProperty("file.separator")+"RunKeel.jar");
      fOutT.println("ESPRESSOPATH=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"espresso");
      fOutT.println("BATIK=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"batik");
      fOutT.println("CPATHBATIK=$BATIK"+
       System.getProperty("file.separator")+"batik-anim.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-awt-util.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-bridge.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-codec.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-css.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-dom.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-ext.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-extension.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-gui-util.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-gvt.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-parser.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-script.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-svg-dom.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-svggen.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-swing.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-transcoder.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-util.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-xml.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"xml-apis-ext.jar");

      fOutT.println("JAMA=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"jamalib");
      fOutT.println("CPATHJAMA=$JAMA"+System.getProperty("file.separator")+"Jama-1.0.2.jar");
      fOutT.println("XFUZZYPATH=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"xfuzzy");
      fOutT.println("CPATHXFUZZY=$XFUZZYPATH:$XFUZZYPATH"+System.getProperty("file.separator")+"xfuzzy.jar");
      fOutT.println("EXECPATH=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"exec");
      fOutT.println("CPATHEXEC=$EXECPATH"+System.getProperty("file.separator")+"commons-exec-1.3.jar");

      fOutT.println("CPATH=$CPATHGUAJE:$CPATHFISPRO:$CPATHFINGRAMS:$CPATHEXEC:$CPATHWEKA:$CPATHKEEL:$CPATHXFUZZY:$CPATHORE:$CPATHBATIK:$CPATHJAMA");
      fOutT.println("cd $GUAJEPATH;");
      fOutT.println("java -Xmx512m -Dguajepath=$GUAJEPATH -Despressopath=$ESPRESSO -Dgraphpath=$GRAPHVIZ -Djava.library.path=$FISPRO -Dpellet.configuration=file:config/pellet.properties -Dlog4j.configuration=file:config/log4j.properties -classpath $CPATH kbct.MainKBCT $1 TEST $2 $3 $4;");
      fOutT.flush();
      fOutT.close();
    }
    String fnameQUALITY= System.getProperty("user.dir") 
	        + System.getProperty("file.separator")
	        + "guajeQUALITY.sh";
    faux= new File(fnameQUALITY);
    if (faux.exists()) { 
      PrintWriter fOutQ = new PrintWriter(new FileOutputStream(fnameQUALITY), true);
      fOutQ.println("#!/bin/sh");
      fOutQ.println("# guajeQUALITY.sh");
      fOutQ.println("GUAJEPATH="+GUAJE);
      fOutQ.println("CPATHGUAJE=$GUAJEPATH"+
            System.getProperty("file.separator")+"libs"+
            System.getProperty("file.separator")+"guajelib"+
            System.getProperty("file.separator")+"GUAJE-v3.0.jar");
      fOutQ.println("FISPRO="+FisProPath);
      fOutQ.println("FISPROPATH=$FISPRO/jar");
      fOutQ.println("CPATHFISPRO=$FISPROPATH"+System.getProperty("file.separator")+"plot.jar");
      fOutQ.println("CPATHFISPRO=$CPATHFISPRO:$FISPROPATH"+System.getProperty("file.separator")+"util.jar");
      fOutQ.println("CPATHFISPRO=$CPATHFISPRO:$FISPROPATH"+System.getProperty("file.separator")+"freehep-base.jar");
      fOutQ.println("CPATHFISPRO=$CPATHFISPRO:$FISPROPATH"+System.getProperty("file.separator")+"freehep-graphics2d.jar");
      fOutQ.println("CPATHFISPRO=$CPATHFISPRO:$FISPROPATH"+System.getProperty("file.separator")+"freehep-graphicsio.jar");
      fOutQ.println("CPATHFISPRO=$CPATHFISPRO:$FISPROPATH"+System.getProperty("file.separator")+"freehep-graphicsio-emf.jar");
      fOutQ.println("CPATHFISPRO=$CPATHFISPRO:$FISPROPATH"+System.getProperty("file.separator")+"freehep-graphicsio-gif.jar");
      fOutQ.println("CPATHFISPRO=$CPATHFISPRO:$FISPROPATH"+System.getProperty("file.separator")+"freehep-graphicsio-pdf.jar");
      fOutQ.println("CPATHFISPRO=$CPATHFISPRO:$FISPROPATH"+System.getProperty("file.separator")+"freehep-graphicsio-ps.jar");
      fOutQ.println("GRAPHVIZ="+GraphvizPath);
      fOutQ.println("FINGRAMSPATH=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"vismaps");
      fOutQ.println("CPATHFINGRAMS=$FINGRAMSPATH"+System.getProperty("file.separator")+"fingramsGenerator.jar");
      fOutQ.println("WEKAPATH=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"wekalib");
      fOutQ.println("CPATHWEKA=$WEKAPATH"+System.getProperty("file.separator") + "weka.jar");
      fOutQ.println("OREPATH=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"orelibs");
      fOutQ.println("CPATHORE=$OREPATH"+System.getProperty("file.separator")+"ORE-UMU.jar:$OREPATH"+System.getProperty("file.separator")+"oreapi"+System.getProperty("file.separator")+"ore-api.jar:$OREPATH"+System.getProperty("file.separator")+"jena"+System.getProperty("file.separator")+"jena.jar:$OREPATH"+System.getProperty("file.separator")+"orelibs"+System.getProperty("file.separator")+"ws-client"+System.getProperty("file.separator")+"em-client.jar");
      fOutQ.println("KEELPATH=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"keellibs");
      fOutQ.println("CPATHKEEL=$KEELPATH"+System.getProperty("file.separator")+"SMOTE-I.jar:$KEELPATH"+System.getProperty("file.separator")+"RunKeel.jar");
      fOutQ.println("ESPRESSOPATH=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"espresso");
      fOutQ.println("BATIK=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"batik");
      fOutQ.println("CPATHBATIK=$BATIK"+
       System.getProperty("file.separator")+"batik-anim.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-awt-util.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-bridge.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-codec.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-css.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-dom.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-ext.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-extension.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-gui-util.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-gvt.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-parser.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-script.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-svg-dom.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-svggen.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-swing.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-transcoder.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-util.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"batik-xml.jar"+
       ":$BATIK"+
       System.getProperty("file.separator")+"xml-apis-ext.jar");

      fOutQ.println("JAMA=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"jamalib");
      fOutQ.println("CPATHJAMA=$JAMA"+System.getProperty("file.separator")+"Jama-1.0.2.jar");
      fOutQ.println("XFUZZYPATH=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"xfuzzy");
      fOutQ.println("CPATHXFUZZY=$XFUZZYPATH:$XFUZZYPATH"+System.getProperty("file.separator")+"xfuzzy.jar");
      fOutQ.println("EXECPATH=$GUAJEPATH"+System.getProperty("file.separator")+"libs"+System.getProperty("file.separator")+"exec");
      fOutQ.println("CPATHEXEC=$EXECPATH"+System.getProperty("file.separator")+"commons-exec-1.3.jar");

      fOutQ.println("CPATH=$CPATHGUAJE:$CPATHFISPRO:$CPATHFINGRAMS:$CPATHEXEC:$CPATHWEKA:$CPATHKEEL:$CPATHXFUZZY:$CPATHORE:$CPATHBATIK:$CPATHJAMA");
      fOutQ.println("cd $GUAJEPATH;");
      fOutQ.println("java -Xmx512m -Dguajepath=$GUAJEPATH -Despressopath=$ESPRESSO -Dgraphpath=$GRAPHVIZ -Djava.library.path=$FISPRO -Dpellet.configuration=file:config/pellet.properties -Dlog4j.configuration=file:config/log4j.properties -classpath $CPATH kbct.MainKBCT $1 QUALITY $2;");
      fOutQ.flush();
      fOutQ.close();
    }
    String fnameCLEAN= System.getProperty("user.dir") 
	        + System.getProperty("file.separator")
	        + "cleanTMP.sh";
    faux= new File(fnameCLEAN);
    if (faux.exists()) { 
      PrintWriter fOutC = new PrintWriter(new FileOutputStream(fnameCLEAN), true);
      fOutC.println("#!/bin/sh");
      fOutC.println("# cleanTMP.sh");
      fOutC.println("GUAJEPATH="+GUAJE);
      fOutC.println("cd $GUAJEPATH/tmp;rm -f temp*.*;rm -f result*.*;rm -f *.pla;rm -f *.fis;rm -f *.xml;");
      fOutC.flush();
      fOutC.close();
    }
    String fnameIRIStest5= System.getProperty("user.dir") 
	        + System.getProperty("file.separator")
	        + "scripts"
	        + System.getProperty("file.separator")
	        + "IRIS"
	        + System.getProperty("file.separator")
	        + "TEST5.sh";
    faux= new File(fnameIRIStest5);
    if (faux.exists()) { 
      PrintWriter fOutIT5 = new PrintWriter(new FileOutputStream(fnameIRIStest5), true);
      fOutIT5.println("#!/bin/sh");
      fOutIT5.println("# TEST5.sh");
      fOutIT5.println("GUAJEPATH="+GUAJE);
      fOutIT5.println("PROBLEM=IRIS");
      fOutIT5.println("PROBLEMFOLD=examples/iris/CV");
      fOutIT5.println("FILENAME=IRIS.txt_CV");
      fOutIT5.println("$GUAJEPATH/cleanTMP.sh");
      fOutIT5.println("$GUAJEPATH/guajeTEST.sh $GUAJEPATH/$PROBLEMFOLD\"0\"/$FILENAME\"0.ikb.xml\" $1 $2 $3");
      fOutIT5.println("$GUAJEPATH/cleanTMP.sh");
      fOutIT5.println("$GUAJEPATH/guajeTEST.sh $GUAJEPATH/$PROBLEMFOLD\"1\"/$FILENAME\"1.ikb.xml\" $1 $2 $3");
      fOutIT5.println("$GUAJEPATH/cleanTMP.sh");
      fOutIT5.println("$GUAJEPATH/guajeTEST.sh $GUAJEPATH/$PROBLEMFOLD\"2\"/$FILENAME\"2.ikb.xml\" $1 $2 $3");
      fOutIT5.println("$GUAJEPATH/cleanTMP.sh");
      fOutIT5.println("$GUAJEPATH/guajeTEST.sh $GUAJEPATH/$PROBLEMFOLD\"3\"/$FILENAME\"3.ikb.xml\" $1 $2 $3");
      fOutIT5.println("$GUAJEPATH/cleanTMP.sh");
      fOutIT5.println("$GUAJEPATH/guajeTEST.sh $GUAJEPATH/$PROBLEMFOLD\"4\"/$FILENAME\"4.ikb.xml\" $1 $2 $3");
      fOutIT5.println("$GUAJEPATH/cleanTMP.sh");
      fOutIT5.flush();
      fOutIT5.close();
    }
    String fnameIRIStestcv5= System.getProperty("user.dir") 
	        + System.getProperty("file.separator")
	        + "scripts"
	        + System.getProperty("file.separator")
	        + "IRIS"
	        + System.getProperty("file.separator")
	        + "TESTcv5.sh";
    faux= new File(fnameIRIStestcv5);
    if (faux.exists()) { 
      PrintWriter fOutITcv5 = new PrintWriter(new FileOutputStream(fnameIRIStestcv5), true);
      fOutITcv5.println("#!/bin/sh");
      fOutITcv5.println("# TESTcv5.sh");
      fOutITcv5.println("GUAJEPATH="+GUAJE);
      fOutITcv5.println("$GUAJEPATH/scripts/IRIS/TEST5.sh -1 FS confFileParameters-FS.xml");
      fOutITcv5.println("$GUAJEPATH/scripts/IRIS/TEST5.sh FS PD confFileParameters-PD.xml");
      fOutITcv5.println("$GUAJEPATH/scripts/IRIS/TEST5.sh FS_PD RI confFileParameters-RI.xml");
      fOutITcv5.println("$GUAJEPATH/scripts/IRIS/TEST5.sh FS_PD_RI S confFileParameters-S.xml");
      fOutITcv5.println("$GUAJEPATH/scripts/IRIS/TEST5.sh FS_PD_RI_S OGT confFileParameters-OGT.xml");
      fOutITcv5.println("$GUAJEPATH/scripts/IRIS/TEST5.sh FS_PD_RI_S OGT confFileParameters-OGT.xml");
      fOutITcv5.println("$GUAJEPATH/scripts/IRIS/TEST5.sh FS_PD_RI_S OGT confFileParameters-OGT.xml");
      fOutITcv5.println("$GUAJEPATH/scripts/IRIS/TEST5.sh FS_PD_RI_S OGT confFileParameters-OGT.xml");
      fOutITcv5.println("$GUAJEPATH/scripts/IRIS/TEST5.sh FS_PD_RI_S OGT confFileParameters-OGT.xml");
      fOutITcv5.println("$GUAJEPATH/scripts/IRIS/TEST5.sh FS_PD_RI_S_OGT FING confFileParameters-FING.xml");
      fOutITcv5.flush();
      fOutITcv5.close();
    }
    String fnameIRISquality5= System.getProperty("user.dir") 
	        + System.getProperty("file.separator")
	        + "scripts"
	        + System.getProperty("file.separator")
	        + "IRIS"
	        + System.getProperty("file.separator")
	        + "QUALITY5.sh";
    faux= new File(fnameIRISquality5);
    if (faux.exists()) { 
      PrintWriter fOutIQ5 = new PrintWriter(new FileOutputStream(fnameIRISquality5), true);
      fOutIQ5.println("#!/bin/sh");
      fOutIQ5.println("# QUALITY5.sh");
      fOutIQ5.println("GUAJEPATH="+GUAJE);
      fOutIQ5.println("PROBLEM=IRIS");
      fOutIQ5.println("PROBLEMFOLD=examples/iris/CV");
      fOutIQ5.println("FILENAME=IRIS.txt_CV");
      fOutIQ5.println("$GUAJEPATH/cleanTMP.sh");
      fOutIQ5.println("$GUAJEPATH/guajeQUALITY.sh $GUAJEPATH/$PROBLEMFOLD\"0\"/$FILENAME\"0.ikb.xml\" $1");
      fOutIQ5.println("$GUAJEPATH/cleanTMP.sh");
      fOutIQ5.println("$GUAJEPATH/guajeQUALITY.sh $GUAJEPATH/$PROBLEMFOLD\"1\"/$FILENAME\"1.ikb.xml\" $1");
      fOutIQ5.println("$GUAJEPATH/cleanTMP.sh");
      fOutIQ5.println("$GUAJEPATH/guajeQUALITY.sh $GUAJEPATH/$PROBLEMFOLD\"2\"/$FILENAME\"2.ikb.xml\" $1");
      fOutIQ5.println("$GUAJEPATH/cleanTMP.sh");
      fOutIQ5.println("$GUAJEPATH/guajeQUALITY.sh $GUAJEPATH/$PROBLEMFOLD\"3\"/$FILENAME\"3.ikb.xml\" $1");
      fOutIQ5.println("$GUAJEPATH/cleanTMP.sh");
      fOutIQ5.println("$GUAJEPATH/guajeQUALITY.sh $GUAJEPATH/$PROBLEMFOLD\"4\"/$FILENAME\"4.ikb.xml\" $1");
      fOutIQ5.println("$GUAJEPATH/cleanTMP.sh");
      fOutIQ5.flush();
      fOutIQ5.close();
    }
    String fnameIRISqualitycv5= System.getProperty("user.dir") 
	        + System.getProperty("file.separator")
	        + "scripts"
	        + System.getProperty("file.separator")
	        + "IRIS"
	        + System.getProperty("file.separator")
	        + "QUALITYcv5.sh";
    faux= new File(fnameIRISqualitycv5);
    if (faux.exists()) { 
      PrintWriter fOutIQcv5 = new PrintWriter(new FileOutputStream(fnameIRISqualitycv5), true);
      fOutIQcv5.println("#!/bin/sh");
      fOutIQcv5.println("# QUALITYcv5.sh");
      fOutIQcv5.println("GUAJEPATH="+GUAJE);
      fOutIQcv5.println("$GUAJEPATH/scripts/IRIS/QUALITY5.sh FS_PD_RI");
      fOutIQcv5.println("$GUAJEPATH/scripts/IRIS/QUALITY5.sh FS_PD_RI_S");
      fOutIQcv5.println("$GUAJEPATH/scripts/IRIS/QUALITY5.sh FS_PD_RI_S_OGT");
      fOutIQcv5.flush();
      fOutIQcv5.close();
    }
    String fnameWINEtest10= System.getProperty("user.dir") 
	        + System.getProperty("file.separator")
	        + "scripts"
	        + System.getProperty("file.separator")
	        + "WINE"
	        + System.getProperty("file.separator")
	        + "TEST10.sh";
    faux= new File(fnameWINEtest10);
    if (faux.exists()) { 
      PrintWriter fOutWT10 = new PrintWriter(new FileOutputStream(fnameWINEtest10), true);
      fOutWT10.println("#!/bin/sh");
      fOutWT10.println("# TEST10.sh");
      fOutWT10.println("GUAJEPATH="+GUAJE);
      fOutWT10.println("PROBLEM=WINE");
      fOutWT10.println("PROBLEMFOLD=examples/wine/CV");
      fOutWT10.println("FILENAME=WINE.txt_CV");
      fOutWT10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWT10.println("$GUAJEPATH/guajeTEST.sh $GUAJEPATH/$PROBLEMFOLD\"0\"/$FILENAME\"0.ikb.xml\" $1 $2 $3");
      fOutWT10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWT10.println("$GUAJEPATH/guajeTEST.sh $GUAJEPATH/$PROBLEMFOLD\"1\"/$FILENAME\"1.ikb.xml\" $1 $2 $3");
      fOutWT10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWT10.println("$GUAJEPATH/guajeTEST.sh $GUAJEPATH/$PROBLEMFOLD\"2\"/$FILENAME\"2.ikb.xml\" $1 $2 $3");
      fOutWT10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWT10.println("$GUAJEPATH/guajeTEST.sh $GUAJEPATH/$PROBLEMFOLD\"3\"/$FILENAME\"3.ikb.xml\" $1 $2 $3");
      fOutWT10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWT10.println("$GUAJEPATH/guajeTEST.sh $GUAJEPATH/$PROBLEMFOLD\"4\"/$FILENAME\"4.ikb.xml\" $1 $2 $3");
      fOutWT10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWT10.println("$GUAJEPATH/guajeTEST.sh $GUAJEPATH/$PROBLEMFOLD\"5\"/$FILENAME\"5.ikb.xml\" $1 $2 $3");
      fOutWT10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWT10.println("$GUAJEPATH/guajeTEST.sh $GUAJEPATH/$PROBLEMFOLD\"6\"/$FILENAME\"6.ikb.xml\" $1 $2 $3");
      fOutWT10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWT10.println("$GUAJEPATH/guajeTEST.sh $GUAJEPATH/$PROBLEMFOLD\"7\"/$FILENAME\"7.ikb.xml\" $1 $2 $3");
      fOutWT10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWT10.println("$GUAJEPATH/guajeTEST.sh $GUAJEPATH/$PROBLEMFOLD\"8\"/$FILENAME\"8.ikb.xml\" $1 $2 $3");
      fOutWT10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWT10.println("$GUAJEPATH/guajeTEST.sh $GUAJEPATH/$PROBLEMFOLD\"9\"/$FILENAME\"9.ikb.xml\" $1 $2 $3");
      fOutWT10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWT10.flush();
      fOutWT10.close();
    }
    String fnameWINEtestcv10= System.getProperty("user.dir") 
	        + System.getProperty("file.separator")
	        + "scripts"
	        + System.getProperty("file.separator")
	        + "WINE"
	        + System.getProperty("file.separator")
	        + "TESTcv10.sh";
    faux= new File(fnameWINEtestcv10);
    if (faux.exists()) { 
      PrintWriter fOutWTcv10 = new PrintWriter(new FileOutputStream(fnameWINEtestcv10), true);
      fOutWTcv10.println("#!/bin/sh");
      fOutWTcv10.println("# TESTcv10.sh");
      fOutWTcv10.println("GUAJEPATH="+GUAJE);
      fOutWTcv10.println("$GUAJEPATH/scripts/WINE/TEST10.sh -1 FS confFileParameters-FS.xml");
      fOutWTcv10.println("$GUAJEPATH/scripts/WINE/TEST10.sh FS PD confFileParameters-PD.xml");
      fOutWTcv10.println("$GUAJEPATH/scripts/WINE/TEST10.sh FS_PD RI confFileParameters-RI.xml");
      fOutWTcv10.println("$GUAJEPATH/scripts/WINE/TEST10.sh FS_PD_RI S confFileParameters-S.xml");
      fOutWTcv10.println("$GUAJEPATH/scripts/WINE/TEST10.sh FS_PD_RI_S OGT confFileParameters-OGT.xml");
      fOutWTcv10.println("$GUAJEPATH/scripts/WINE/TEST10.sh FS_PD_RI_S OGT confFileParameters-OGT.xml");
      fOutWTcv10.println("$GUAJEPATH/scripts/WINE/TEST10.sh FS_PD_RI_S OGT confFileParameters-OGT.xml");
      fOutWTcv10.println("$GUAJEPATH/scripts/WINE/TEST10.sh FS_PD_RI_S OGT confFileParameters-OGT.xml");
      fOutWTcv10.println("$GUAJEPATH/scripts/WINE/TEST10.sh FS_PD_RI_S OGT confFileParameters-OGT.xml");
      fOutWTcv10.println("$GUAJEPATH/scripts/WINE/TEST10.sh FS_PD_RI_S_OGT FING confFileParameters-FING.xml");
      fOutWTcv10.flush();
      fOutWTcv10.close();
    }
    String fnameWINEquality10= System.getProperty("user.dir") 
	        + System.getProperty("file.separator")
	        + "scripts"
	        + System.getProperty("file.separator")
	        + "WINE"
	        + System.getProperty("file.separator")
	        + "QUALITY10.sh";
    faux= new File(fnameWINEquality10);
    if (faux.exists()) { 
      PrintWriter fOutWQ10 = new PrintWriter(new FileOutputStream(fnameWINEquality10), true);
      fOutWQ10.println("#!/bin/sh");
      fOutWQ10.println("# QUALITY10.sh");
      fOutWQ10.println("GUAJEPATH="+GUAJE);
      fOutWQ10.println("PROBLEM=WINE");
      fOutWQ10.println("PROBLEMFOLD=examples/wine/CV");
      fOutWQ10.println("FILENAME=WINE.txt_CV");
      fOutWQ10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWQ10.println("$GUAJEPATH/guajeQUALITY.sh $GUAJEPATH/$PROBLEMFOLD\"0\"/$FILENAME\"0.ikb.xml\" $1");
      fOutWQ10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWQ10.println("$GUAJEPATH/guajeQUALITY.sh $GUAJEPATH/$PROBLEMFOLD\"1\"/$FILENAME\"1.ikb.xml\" $1");
      fOutWQ10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWQ10.println("$GUAJEPATH/guajeQUALITY.sh $GUAJEPATH/$PROBLEMFOLD\"2\"/$FILENAME\"2.ikb.xml\" $1");
      fOutWQ10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWQ10.println("$GUAJEPATH/guajeQUALITY.sh $GUAJEPATH/$PROBLEMFOLD\"3\"/$FILENAME\"3.ikb.xml\" $1");
      fOutWQ10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWQ10.println("$GUAJEPATH/guajeQUALITY.sh $GUAJEPATH/$PROBLEMFOLD\"4\"/$FILENAME\"4.ikb.xml\" $1");
      fOutWQ10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWQ10.println("$GUAJEPATH/guajeQUALITY.sh $GUAJEPATH/$PROBLEMFOLD\"5\"/$FILENAME\"5.ikb.xml\" $1");
      fOutWQ10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWQ10.println("$GUAJEPATH/guajeQUALITY.sh $GUAJEPATH/$PROBLEMFOLD\"6\"/$FILENAME\"6.ikb.xml\" $1");
      fOutWQ10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWQ10.println("$GUAJEPATH/guajeQUALITY.sh $GUAJEPATH/$PROBLEMFOLD\"7\"/$FILENAME\"7.ikb.xml\" $1");
      fOutWQ10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWQ10.println("$GUAJEPATH/guajeQUALITY.sh $GUAJEPATH/$PROBLEMFOLD\"8\"/$FILENAME\"8.ikb.xml\" $1");
      fOutWQ10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWQ10.println("$GUAJEPATH/guajeQUALITY.sh $GUAJEPATH/$PROBLEMFOLD\"9\"/$FILENAME\"9.ikb.xml\" $1");
      fOutWQ10.println("$GUAJEPATH/cleanTMP.sh");
      fOutWQ10.flush();
      fOutWQ10.close();
    }
    String fnameWINEqualitycv10= System.getProperty("user.dir") 
	        + System.getProperty("file.separator")
	        + "scripts"
	        + System.getProperty("file.separator")
	        + "WINE"
	        + System.getProperty("file.separator")
	        + "QUALITYcv10.sh";
    faux= new File(fnameWINEqualitycv10);
    if (faux.exists()) { 
      PrintWriter fOutWQcv10 = new PrintWriter(new FileOutputStream(fnameWINEqualitycv10), true);
      fOutWQcv10.println("#!/bin/sh");
      fOutWQcv10.println("# QUALITYcv10.sh");
      fOutWQcv10.println("GUAJEPATH="+GUAJE);
      fOutWQcv10.println("$GUAJEPATH/scripts/WINE/QUALITY10.sh FS_PD_RI");
      fOutWQcv10.println("$GUAJEPATH/scripts/WINE/QUALITY10.sh FS_PD_RI_S");
      fOutWQcv10.println("$GUAJEPATH/scripts/WINE/QUALITY10.sh FS_PD_RI_S_OGT");
      fOutWQcv10.flush();
      fOutWQcv10.close();
    }
    String fnameWBCDtest2= System.getProperty("user.dir") 
	        + System.getProperty("file.separator")
	        + "scripts"
	        + System.getProperty("file.separator")
	        + "WBCD"
	        + System.getProperty("file.separator")
	        + "TEST2.sh";
    faux= new File(fnameWBCDtest2);
    if (faux.exists()) { 
      PrintWriter fOutWT2 = new PrintWriter(new FileOutputStream(fnameWBCDtest2), true);
      fOutWT2.println("#!/bin/sh");
      fOutWT2.println("# TEST2.sh");
      fOutWT2.println("GUAJEPATH="+GUAJE);
      fOutWT2.println("PROBLEM=WBCD");
      fOutWT2.println("PROBLEMFOLD=examples/wbcd/wbcd");
      fOutWT2.println("FILENAME=WBCD.txt.");
      fOutWT2.println("$GUAJEPATH/cleanTMP.sh");
      fOutWT2.println("$GUAJEPATH/guajeTEST.sh $GUAJEPATH/$PROBLEMFOLD\"0\"/$FILENAME\"0.ikb.xml\" $1 $2 $3");
      fOutWT2.println("$GUAJEPATH/cleanTMP.sh");
      fOutWT2.println("$GUAJEPATH/guajeTEST.sh $GUAJEPATH/$PROBLEMFOLD\"1\"/$FILENAME\"1.ikb.xml\" $1 $2 $3");
      fOutWT2.println("$GUAJEPATH/cleanTMP.sh");
      fOutWT2.flush();
      fOutWT2.close();
    }
    String fnameWBCDtestcv2= System.getProperty("user.dir") 
	        + System.getProperty("file.separator")
	        + "scripts"
	        + System.getProperty("file.separator")
	        + "WBCD"
	        + System.getProperty("file.separator")
	        + "TESTcv2.sh";
    faux= new File(fnameWBCDtestcv2);
    if (faux.exists()) { 
      PrintWriter fOutWTcv2 = new PrintWriter(new FileOutputStream(fnameWBCDtestcv2), true);
      fOutWTcv2.println("#!/bin/sh");
      fOutWTcv2.println("# TESTcv2.sh");
      fOutWTcv2.println("GUAJEPATH="+GUAJE);
      fOutWTcv2.println("$GUAJEPATH/scripts/WBCD/TEST2.sh -1 FS confFileParameters-FS.xml");
      fOutWTcv2.println("$GUAJEPATH/scripts/WBCD/TEST2.sh FS PD confFileParameters-PD.xml");
      fOutWTcv2.println("$GUAJEPATH/scripts/WBCD/TEST2.sh FS_PD RI confFileParameters-RI.xml");
      fOutWTcv2.println("$GUAJEPATH/scripts/WBCD/TEST2.sh FS_PD_RI S confFileParameters-S.xml");
      fOutWTcv2.println("$GUAJEPATH/scripts/WBCD/TEST2.sh FS_PD_RI_S OGT confFileParameters-OGT.xml");
      fOutWTcv2.println("$GUAJEPATH/scripts/WBCD/TEST2.sh FS_PD_RI_S OGT confFileParameters-OGT.xml");
      fOutWTcv2.println("$GUAJEPATH/scripts/WBCD/TEST2.sh FS_PD_RI_S OGT confFileParameters-OGT.xml");
      fOutWTcv2.println("$GUAJEPATH/scripts/WBCD/TEST2.sh FS_PD_RI_S OGT confFileParameters-OGT.xml");
      fOutWTcv2.println("$GUAJEPATH/scripts/WBCD/TEST2.sh FS_PD_RI_S OGT confFileParameters-OGT.xml");
      fOutWTcv2.println("$GUAJEPATH/scripts/WBCD/TEST2.sh FS_PD_RI_S_OGT FING confFileParameters-FING.xml");
      fOutWTcv2.flush();
      fOutWTcv2.close();
    }
    String fnameWBCDquality2= System.getProperty("user.dir") 
	        + System.getProperty("file.separator")
	        + "scripts"
	        + System.getProperty("file.separator")
	        + "WBCD"
	        + System.getProperty("file.separator")
	        + "QUALITY2.sh";
    faux= new File(fnameWBCDquality2);
    if (faux.exists()) { 
      PrintWriter fOutWQ2 = new PrintWriter(new FileOutputStream(fnameWBCDquality2), true);
      fOutWQ2.println("#!/bin/sh");
      fOutWQ2.println("# QUALITY2.sh");
      fOutWQ2.println("GUAJEPATH="+GUAJE);
      fOutWQ2.println("PROBLEM=WBCD");
      fOutWQ2.println("PROBLEMFOLD=examples/wbcd/wbcd");
      fOutWQ2.println("FILENAME=WBCD.txt.");
      fOutWQ2.println("$GUAJEPATH/cleanTMP.sh");
      fOutWQ2.println("$GUAJEPATH/guajeQUALITY.sh $GUAJEPATH/$PROBLEMFOLD\"0\"/$FILENAME\"0.ikb.xml\" $1");
      fOutWQ2.println("$GUAJEPATH/cleanTMP.sh");
      fOutWQ2.println("$GUAJEPATH/guajeQUALITY.sh $GUAJEPATH/$PROBLEMFOLD\"1\"/$FILENAME\"1.ikb.xml\" $1");
      fOutWQ2.println("$GUAJEPATH/cleanTMP.sh");
      fOutWQ2.flush();
      fOutWQ2.close();
    }
    String fnameWBCDqualitycv2= System.getProperty("user.dir") 
	        + System.getProperty("file.separator")
	        + "scripts"
	        + System.getProperty("file.separator")
	        + "WBCD"
	        + System.getProperty("file.separator")
	        + "QUALITYcv2.sh";
    faux= new File(fnameWBCDqualitycv2);
    if (faux.exists()) { 
      PrintWriter fOutWQcv2 = new PrintWriter(new FileOutputStream(fnameWBCDqualitycv2), true);
      fOutWQcv2.println("#!/bin/sh");
      fOutWQcv2.println("# QUALITYcv2.sh");
      fOutWQcv2.println("GUAJEPATH="+GUAJE);
      fOutWQcv2.println("$GUAJEPATH/scripts/WBCD/QUALITY2.sh FS_PD_RI");
      fOutWQcv2.println("$GUAJEPATH/scripts/WBCD/QUALITY2.sh FS_PD_RI_S");
      fOutWQcv2.println("$GUAJEPATH/scripts/WBCD/QUALITY2.sh FS_PD_RI_S_OGT");
      fOutWQcv2.flush();
      fOutWQcv2.close();
    }
  }
}