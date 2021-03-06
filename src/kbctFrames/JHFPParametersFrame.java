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
//                       JHFPParametersFrame.java
//
//
//**********************************************************************

package kbctFrames;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
//import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kbct.JFISHFP;
import kbct.LocaleKBCT;
import kbct.MainKBCT;
//import kbctAux.DoubleField;
//import kbctAux.IntegerField;
import kbctAux.MessageKBCT;

/**
 * kbctFrames.JHFPParametersFrame
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
//------------------------------------------------------------------------------
public class JHFPParametersFrame extends JDialog {
  static final long serialVersionUID=0;	
  private JKBCTFrame Parent;
  private JLabel jLabelDistance = new JLabel();
  private JComboBox jCBDistance = new JComboBox();
  private DefaultComboBoxModel jCBDistanceModel = new DefaultComboBoxModel() {
    static final long serialVersionUID=0;	
    public Object getElementAt(int index) {
      try { return LocaleKBCT.GetString(JFISHFP.DistanceType()[index]); }
      catch( Throwable t ) { return null; }
      }
    };
  private JPanel jPanelValidation = new JPanel(new GridBagLayout());
  private JButton jButtonCancel = new JButton();
  private JButton jButtonApply = new JButton();
  private JButton jButtonDefault = new JButton();
  private JFISHFP hfp;
//------------------------------------------------------------------------------
  public JHFPParametersFrame( JKBCTFrame parent, JFISHFP hfp ) {
    super(parent);
    this.hfp = hfp;
    this.Parent = parent;
    try { jbInit(); }
    catch(Throwable t) { MessageKBCT.Error(null, t); }
  }
//------------------------------------------------------------------------------
  private void jbInit() throws Throwable {
	this.setIconImage(LocaleKBCT.getIconGUAJE().getImage());
    this.getContentPane().setLayout(new GridBagLayout());
    this.getContentPane().add(jLabelDistance,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    for( int i=0 ; i<JFISHFP.DistanceType().length ; i++ )
      this.jCBDistanceModel.addElement(new String());  // on met des String vide dans le mod�le, c'est la m�thode getElementAt qui fait la traduction
    this.jCBDistance.setModel(this.jCBDistanceModel);
    this.getContentPane().add(jCBDistance,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 0, 0));
    this.getContentPane().add(jPanelValidation,   new GridBagConstraints(0, 7, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 5), 0, 0));
    jPanelValidation.add(jButtonApply,   new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    jPanelValidation.add(jButtonCancel,    new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    jPanelValidation.add(jButtonDefault,    new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    jButtonApply.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonApply_actionPerformed(e); } });
    jButtonCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonCancel_actionPerformed(e); } });
    jButtonDefault.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { jButtonDefault_actionPerformed(e); } });

    // init des objets
    if (this.hfp!=null) {
      Object [] params = this.hfp.GetParameters();
      String [] types = JFISHFP.DistanceType();
      for( int i=0 ; i<types.length ; i++ )
        if( params[0].equals(types[i]) )
          this.jCBDistance.setSelectedIndex(i);

    } else {
        int dist=0;
        if (MainKBCT.getConfig().GetDistance().equals("symbolic"))
        	dist=1;
        else if (MainKBCT.getConfig().GetDistance().equals("symbnum"))
            dist=2;
        	
        this.jCBDistance.setSelectedIndex(dist);
    }
    // affichage
    this.Translate();
    this.pack();
    this.setResizable(false);
    this.setModal(true);
    // si se quita el comentario, no se pinta el icono en la ventana
    this.setLocation(JChildFrame.ChildPosition(this.Parent, this.getSize()));
    this.setVisible(true);
  }
//------------------------------------------------------------------------------
  private void Translate() {
    this.setTitle(LocaleKBCT.GetString("HFPParameters"));
    jLabelDistance.setText(LocaleKBCT.GetString("Distance") + ":");
    jButtonApply.setText(LocaleKBCT.GetString("Apply"));
    jButtonCancel.setText(LocaleKBCT.GetString("Cancel"));
    jButtonDefault.setText(LocaleKBCT.GetString("DefaultValues"));
  }
//------------------------------------------------------------------------------
  void jButtonApply_actionPerformed(ActionEvent e) {
  try { 
      MainKBCT.getConfig().SetDistance((String)this.jCBDistance.getSelectedItem());
      if (this.hfp!=null)
        this.hfp.SetParameters(this.jCBDistance.getSelectedIndex());
  } catch(Throwable t) { MessageKBCT.Error(null, t); }
      this.dispose();
  }
//------------------------------------------------------------------------------
  void jButtonCancel_actionPerformed(ActionEvent e) { this.dispose(); }
//------------------------------------------------------------------------------
  void jButtonDefault_actionPerformed(ActionEvent e) {
	  // 0 -> numerical
	  // 1 -> symbolic
	  // 2 -> symbnum
      int dist=0;
      if (LocaleKBCT.DefaultDistance().equals("symbolic"))
      	dist=1;
      else if (LocaleKBCT.DefaultDistance().equals("symbnum"))
        dist=2;

      this.jCBDistance.setSelectedIndex(dist);
  }
}