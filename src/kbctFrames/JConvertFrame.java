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
//                            JConvertFrame.java
//
//
//**********************************************************************

package kbctFrames;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import kbct.JConvert;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
import kbctAux.JFileFilter;
import kbctAux.JOpenFileChooser;
import kbctAux.MessageKBCT;
import kbctAux.Translatable;

/**
 * <p align="left"> kbctFrames.JConvertFrame display a window with options to convert files. <p>
 * <p align="left"> Convert files: <p>
 * <ul>
 *     <li> GUAJE to FisPro </li>
 *     <li> FisPro to GUAJE </li>
 *     <li> GUAJE to Matlab </li>
 *     <li> FisPro to Matlab </li>
 *     <li> GUAJE to Xfuzzy </li>
 *     <li> GUAJE to Espresso </li>
 * </ul>
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
public class JConvertFrame extends JDialog implements Translatable {
  static final long serialVersionUID=0;	
  private JButton jButtonGuajeToFis = new JButton();
  private JButton jButtonFisToGuaje = new JButton();
  private JButton jButtonGuajeToMatlab = new JButton();
  private JButton jButtonFisToMatlab = new JButton();
  private JButton jButtonGuajeToXfuzzy = new JButton();
  private JButton jButtonGuajeToEspresso = new JButton();
  private String File_to_convert = "";
  private String File_converted= "";
  private JMenuItem jMenuClose = new JMenuItem();
  private String title;
  //----------------------------------------------------------------------------
  public JConvertFrame(JKBCTFrame parent) {
    super(parent);
    try {
      title= "GenerateFIS";
      String msg= LocaleKBCT.GetString("InfoGenerateFIS1")+"\n"+
                  "     -> FisPro"+ "\n"+
                  "     -> Matlab"+ "\n"+
                  "     -> Xfuzzy"+ "\n"+
                  "     -> Espresso"+ "\n"+
                  LocaleKBCT.GetString("InfoGenerateFIS2");
      MessageKBCT.Information(this, msg);
      this.jbInit();
      this.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { this_windowClosing(); } });
      jMenuClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JConvertFrame.this.dispose(); } });
      jButtonGuajeToFis.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonGuajeToFis_actionPerformed(e); }} );
      jButtonFisToGuaje.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonFisToGuaje_actionPerformed(e); }} );
      jButtonGuajeToMatlab.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonGuajeToMatlab_actionPerformed(e); }} );
      jButtonFisToMatlab.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonFisToMatlab_actionPerformed(e); }} );
      jButtonGuajeToXfuzzy.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonGuajeToXfuzzy_actionPerformed(e); }} );
      jButtonGuajeToEspresso.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonGuajeToEspresso_actionPerformed(e); }} );
      JKBCTFrame.AddTranslatable(this);
    } catch (Throwable t) {
        MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en constructor en JConvert: "+t);
    }
  }
  //----------------------------------------------------------------------------
  private void jbInit() throws Throwable {
	this.setIconImage(LocaleKBCT.getIconGUAJE().getImage());
	JMenuBar jmb= new JMenuBar();
    jmb.add(this.jMenuClose);
    this.setJMenuBar(jmb);
    this.getContentPane().setLayout(new GridBagLayout());
    this.getContentPane().add(jButtonGuajeToFis, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                    ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(jButtonFisToGuaje, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(jButtonGuajeToMatlab, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(jButtonFisToMatlab, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                    ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(jButtonGuajeToXfuzzy, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
                    ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(jButtonGuajeToEspresso, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    Dimension dim = getToolkit().getScreenSize();
    this.setSize(450, 150);
    this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
    this.setResizable(false);
    this.Translate();
  }
  //----------------------------------------------------------------------------
  public void Translate() {
    this.setTitle(LocaleKBCT.GetString(this.title));
    this.jMenuClose.setText(LocaleKBCT.GetString("Close"));
    this.jButtonGuajeToFis.setText(LocaleKBCT.GetString("GuajeToFis"));
    this.jButtonFisToGuaje.setText(LocaleKBCT.GetString("FisToGuaje"));
    this.jButtonGuajeToMatlab.setText(LocaleKBCT.GetString("GuajeToMatlab"));
    this.jButtonFisToMatlab.setText(LocaleKBCT.GetString("FisToMatlab"));
    this.jButtonGuajeToXfuzzy.setText(LocaleKBCT.GetString("GuajeToXfuzzy"));
    this.jButtonGuajeToEspresso.setText(LocaleKBCT.GetString("GuajeToEspresso"));
    this.repaint();
  }
  //------------------------------------------------------------------------------
  public void this_windowClosing() { this.dispose(); }
  //----------------------------------------------------------------------------
  private void jButtonGuajeToFis_actionPerformed(ActionEvent e) { this.Convert ("GUAJE","FIS"); }
  //----------------------------------------------------------------------------
  private void jButtonFisToGuaje_actionPerformed(ActionEvent e) { this.Convert ("FIS","GUAJE"); }
  //----------------------------------------------------------------------------
  private void jButtonGuajeToMatlab_actionPerformed(ActionEvent e) { this.Convert ("GUAJE","MATLAB"); }
  //----------------------------------------------------------------------------
  private void jButtonFisToMatlab_actionPerformed(ActionEvent e) { this.Convert ("FIS","MATLAB"); }
  //----------------------------------------------------------------------------
  private void jButtonGuajeToXfuzzy_actionPerformed(ActionEvent e) { this.Convert ("GUAJE","XFUZZY"); }
  //----------------------------------------------------------------------------
  private void jButtonGuajeToEspresso_actionPerformed(ActionEvent e) { this.Convert ("GUAJE","ESPRESSO"); }
  //----------------------------------------------------------------------------
  private void Convert (String type_orig, String type_dest) {
    JOpenFileChooser file_chooser = new JOpenFileChooser(MainKBCT.getConfig().GetKBCTFilePath());
    file_chooser.setAcceptAllFileFilterUsed(false);
    if (type_orig.equals("GUAJE")) {
    	//file_chooser.addChoosableFileFilter(new JFileFilter(("KB").toLowerCase(), LocaleKBCT.GetString("FilterKBFile")));
    	file_chooser.addChoosableFileFilter(new JFileFilter(("XML").toLowerCase(), LocaleKBCT.GetString("FilterKBFile")));
    } else if (type_orig.equals("FIS"))
    	file_chooser.addChoosableFileFilter(new JFileFilter(("FIS").toLowerCase(), LocaleKBCT.GetString("FilterFISFile")));

    if( file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ) {
        MainKBCT.getConfig().SetKBCTFilePath(file_chooser.getSelectedFile().getParent());
        File_to_convert = file_chooser.getSelectedFile().getAbsolutePath();
        if ( ( (type_orig.equals("GUAJE")) && (File_to_convert.endsWith(".kb.xml")) ) || (type_orig.equals("FIS")) ) {
            JFileChooser file_chooser_save = new JFileChooser(MainKBCT.getConfig().GetKBCTFilePath());
            file_chooser_save.setDialogTitle(LocaleKBCT.GetString("Save"));
            file_chooser_save.setAcceptAllFileFilterUsed(false);
            JFileFilter filter_save= new JFileFilter();
            if (type_dest.equals("FIS") || (type_dest.equals("MATLAB")))
                filter_save = new JFileFilter(("FIS").toLowerCase(), LocaleKBCT.GetString("FilterFISFile"));
            else if (type_dest.equals("XFUZZY"))
                filter_save = new JFileFilter(("XFL").toLowerCase(), LocaleKBCT.GetString("FilterXfuzzyFile"));
            else if (type_dest.equals("ESPRESSO"))
                filter_save = new JFileFilter(("PLA").toLowerCase(), LocaleKBCT.GetString("FilterEspressoFile"));
            else if (type_dest.equals("GUAJE")) {
                filter_save = new JFileFilter(("XML").toLowerCase(), LocaleKBCT.GetString("FilterKBFile"));
            }
            file_chooser_save.addChoosableFileFilter(filter_save);
            if (type_dest.equals("FIS"))
                file_chooser_save.setSelectedFile(new File(MainKBCT.getConfig().GetKBCTFilePath() + System.getProperty("file.separator") + LocaleKBCT.GetString("newfis") + "." +("FIS").toLowerCase()));
            else if (type_dest.equals("MATLAB"))
                file_chooser_save.setSelectedFile(new File(MainKBCT.getConfig().GetKBCTFilePath() + System.getProperty("file.separator") + LocaleKBCT.GetString("newmatlabfis") + "." +("FIS").toLowerCase()));
            else if (type_dest.equals("XFUZZY"))
                file_chooser_save.setSelectedFile(new File(MainKBCT.getConfig().GetKBCTFilePath() + System.getProperty("file.separator") + LocaleKBCT.GetString("newxfuzzy") + "." +("XFL").toLowerCase()));
            else if (type_dest.equals("ESPRESSO"))
                file_chooser_save.setSelectedFile(new File(MainKBCT.getConfig().GetKBCTFilePath() + System.getProperty("file.separator") + LocaleKBCT.GetString("newespresso") + "." +("PLA").toLowerCase()));

            if (file_chooser_save.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File_converted = file_chooser_save.getSelectedFile().getAbsolutePath();
                if (File_converted.lastIndexOf('.') == -1)
                    if (type_dest.equals("FIS") || (type_dest.equals("MATLAB")))
                        File_converted += '.' + ("FIS").toLowerCase();
                    else if (type_dest.equals("XFUZZY"))
                        File_converted += '.' + ("XFL").toLowerCase();
                    else if (type_dest.equals("ESPRESSO"))
                        File_converted += '.' + ("PLA").toLowerCase();
                    else if (type_dest.equals("GUAJE"))
                        File_converted += '.'+("KB").toLowerCase()+'.'+("XML").toLowerCase();

                    File f = new File(File_converted);
                    boolean warning = false;
                    if (f.exists()) {
                        String message="";
                        if (type_dest.equals("FIS"))
                            message = LocaleKBCT.GetString("TheFISFile") + " : " +File_converted + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");
                        else if (type_dest.equals("MATLAB"))
                            message = LocaleKBCT.GetString("TheMatlabFile") + " : " +File_converted + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");
                        else if (type_dest.equals("XFUZZY"))
                            message = LocaleKBCT.GetString("TheXfuzzyFile") + " : " +File_converted + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");
                        else if (type_dest.equals("ESPRESSO"))
                            message = LocaleKBCT.GetString("TheEspressoFile") + " : " +File_converted + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");
                        else if (type_dest.equals("GUAJE"))
                            message = LocaleKBCT.GetString("TheKBCTFile") + " : " +File_converted + " " + LocaleKBCT.GetString("AlreadyExist") + "\n" + LocaleKBCT.GetString("DoYouWantToReplaceIt");

                        if (MessageKBCT.Confirm(this, message, 1, false, false, false) == JOptionPane.NO_OPTION)
                            warning = true;
                    }
                try {
                    if (!warning) {
                        MainKBCT.getConfig().SetKBCTFilePath(file_chooser.getSelectedFile().getParent());
                        if (type_orig.equals("GUAJE") && type_dest.equals("FIS"))
                            this.GuajeToFis(File_to_convert, File_converted);
                        else if (type_orig.equals("FIS") && type_dest.equals("GUAJE"))
                            this.FisToGuaje(File_to_convert, File_converted);
                        else if (type_orig.equals("GUAJE") && type_dest.equals("MATLAB"))
                            this.GuajeToMatlab(File_to_convert, File_converted);
                        else if (type_orig.equals("FIS") && type_dest.equals("MATLAB"))
                            this.FisToMatlab(File_to_convert, File_converted);
                        else if (type_orig.equals("GUAJE") && type_dest.equals("XFUZZY"))
                            this.GuajeToXfuzzy(File_to_convert, File_converted);
                        else if (type_orig.equals("GUAJE") && type_dest.equals("ESPRESSO"))
                            this.GuajeToEspresso(File_to_convert, File_converted);
                    }
                } catch (Throwable t) {
               	    t.printStackTrace();
                    MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JConvert en Convert: "+t);
                }
            }
        } else {
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("OpenValidKBXML"));
        }
      }
  }
  //----------------------------------------------------------------------------
  void GuajeToFis (String file_name_GUAJE, String file_name_FIS) throws Throwable{
      JConvert.GuajeToFis(file_name_GUAJE, file_name_FIS, false);
  }
  //----------------------------------------------------------------------------
  void FisToGuaje (String file_name_FIS, String file_name_GUAJE) throws Throwable{
      JConvert.FisToGuaje(file_name_FIS, file_name_GUAJE);
  }
  //----------------------------------------------------------------------------
  void GuajeToMatlab (String file_name_GUAJE, String file_name_MATLAB) throws Throwable{
      JConvert.GuajeToMatlab(file_name_GUAJE, file_name_MATLAB);
  }
  //----------------------------------------------------------------------------
  void FisToMatlab (String file_name_FIS, String file_name_MATLAB) throws Throwable{
      JConvert.FisToMatlab(file_name_FIS, file_name_MATLAB);
  }
  //----------------------------------------------------------------------------
  void GuajeToXfuzzy (String file_name_GUAJE, String file_name_XFUZZY) throws Throwable{
      JConvert.GuajeToXfuzzy(file_name_GUAJE, file_name_XFUZZY);
  }
  //----------------------------------------------------------------------------
  void GuajeToEspresso (String file_name_GUAJE, String file_name_ESPRESSO) throws Throwable{
      JConvert.GuajeToEspresso(file_name_GUAJE, file_name_ESPRESSO);
  }
}