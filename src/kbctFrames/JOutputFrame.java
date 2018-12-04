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
//                              JOutputFrame.java
//
//**********************************************************************
package kbctFrames;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import kbct.JKBCT;
import kbct.JKBCTOutput;
import kbct.LocaleKBCT;
import kbctAux.MessageKBCT;
import KB.LabelKBCT;

/**
 * kbctFrames.JOutputFrame generates a window with all information about one output.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 03/08/15
 */
public class JOutputFrame extends JVariableFrame {
  static final long serialVersionUID=0;
//------------------------------------------------------------------------------
  protected JOutputFrame( JExpertFrame parent ) { 
	  super( parent );
	  this.output= true;
  }
//------------------------------------------------------------------------------
  public JOutputFrame( JExpertFrame parent, JKBCT kbct, int output_number ) {
    super(parent);
    this.output= true;
    this.parent= parent;
    this.kbct = kbct;
    this.Number = output_number;
    this.Temp = kbct.GetOutput(output_number+1);
    this.Semaphore = kbct.GetOutputSemaphore(output_number);
    this.ClassifFLAG= true;
  }
//------------------------------------------------------------------------------
  protected void TranslateComponents() {
    this.setTitle(LocaleKBCT.GetString("Output"));
    super.TranslateComponents();
    if ( (this.jCBType.getSelectedIndex()>0) && (this.parent.getJExtDataFile()!=null) ) {
    	jCBNOL.setEditable(false);
	    jCBNOL.setEnabled(false);
    }
  }
//------------------------------------------------------------------------------
  public void Translate() throws Throwable { super.Translate(); }
//------------------------------------------------------------------------------
  void jType_itemStateChanged(ItemEvent e) {
    if( e.getStateChange() == ItemEvent.SELECTED ) {
      try {
        switch (this.jCBType.getSelectedIndex()) {
          case 0: this.Temp.SetType("numerical");
                  this.Temp.SetInputPhysicalRange(0, 1);
                  this.Temp.SetInputInterestRange(0, 1);
                  this.Temp.SetLabelProperties();
                  this.kbct.ReplaceOutput(this.Temp.GetPtr(), (JKBCTOutput)this.Temp);
                  this.jButtonAdvancedOptions.setEnabled(true);
                  this.jButtonModalPoints.setEnabled(true);
                  break;
          case 1: this.Temp.SetType("logical");
                  this.Temp.SetLabelsNumber(2);
                  this.Temp.SetInputPhysicalRange(1, 2);
                  this.Temp.SetInputInterestRange(1, 2);
                  this.Temp.SetLabelsName();
                  this.Temp.SetLabelProperties();
                  this.kbct.ReplaceOutput(this.Temp.GetPtr(), (JKBCTOutput)this.Temp);
                  this.jButtonAdvancedOptions.setEnabled(false);
                  this.jButtonModalPoints.setEnabled(false);
                  break;
          case 2: this.Temp.SetType("categorical");
                  this.Temp.SetInputPhysicalRange(1, this.Temp.GetLabelsNumber());
                  this.Temp.SetInputInterestRange(1, this.Temp.GetLabelsNumber());
                  this.Temp.SetLabelProperties();
                  this.kbct.ReplaceOutput(this.Temp.GetPtr(), (JKBCTOutput)this.Temp);
                  this.jButtonAdvancedOptions.setEnabled(false);
                  this.jButtonModalPoints.setEnabled(false);
                  break;
        }
      } catch (Throwable t) {
        if (!t.toString().equals("java.lang.NullPointerException"))
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JOutputFrame: jType_itemStateChanged: "+t);
      }
    }
  }
//------------------------------------------------------------------------------
  void jNOL_itemStateChanged(ItemEvent e) {
    if( e.getStateChange() == ItemEvent.SELECTED ) {
      try {
        Vector<String> MPV= new Vector<String>();
        for (int n=0; n<this.Temp.GetLabelsNumber(); n++) {
          String MP= this.Temp.GetMP(n+1);
          if (!MP.equals("No MP"))
            MPV.add(MP);
        }
        if (this.Temp.GetV().GetType().equals("logical")) {
            MessageKBCT.Error(this, LocaleKBCT.GetString("WARNING"), LocaleKBCT.GetString("IfSelectedTypeIs")+" "+LocaleKBCT.GetString("logical")+", "+LocaleKBCT.GetString("NumberOfLabelsMustBe")+" 2");
            this.Temp.SetLabelsNumber(2);
            this.Temp.SetInputPhysicalRange(1, 2);
            this.Temp.SetInputInterestRange(1, 2);
            JOutputFrame jof = new JOutputFrame(super.parent, this.kbct, this.Number);
            jof.Show();
            this.dispose();
        } else {
          switch (this.jCBNOL.getSelectedIndex()) {
            case 0: this.Temp.SetLabelsNumber(2);
                    break;
            case 1: this.Temp.SetLabelsNumber(3);
                    break;
            case 2: this.Temp.SetLabelsNumber(4);
                    break;
            case 3: this.Temp.SetLabelsNumber(5);
                    break;
            case 4: this.Temp.SetLabelsNumber(6);
                    break;
            case 5: this.Temp.SetLabelsNumber(7);
                    break;
            case 6: this.Temp.SetLabelsNumber(8);
                    break;
            case 7: this.Temp.SetLabelsNumber(9);
                    break;
          }
          if (this.Temp.GetV().GetType().equals("categorical")) {
            this.Temp.SetInputPhysicalRange(1, this.Temp.GetLabelsNumber());
            this.Temp.SetInputInterestRange(1, this.Temp.GetLabelsNumber());
          }
          this.Temp.SetLabelsName();
          this.Temp.SetORLabelsName();
          this.Temp.SetLabelProperties();
          Enumeration en= MPV.elements();
          int cont= 0;
          while (en.hasMoreElements()) {
            String MP = (String) en.nextElement();
            double modal_point = (new Double(MP)).doubleValue();
            int NOL= this.Temp.GetLabelsNumber();
            for (int n = cont; n < NOL; n++) {
              LabelKBCT label = this.Temp.GetLabel(n + 1);
              if ( (label.GetParams().length==1) ||
            	   ( (label.GetParams().length==3) && 
            	     (label.GetP1() <= modal_point) &&
                     (label.GetP3() >= modal_point)) ) {
                this.Temp.SetMP(n + 1, MP, false);
                cont = n + 1;
                break;
              }
            }
          }
          this.kbct.ReplaceOutput(this.Temp.GetPtr(), (JKBCTOutput)this.Temp);
        }
      } catch (Throwable t) {
    	  t.printStackTrace();
        if (!t.toString().equals("java.lang.NullPointerException"))
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JOutputFrame: jNOL_itemStateChanged: "+t);
      }
    }
  }
//------------------------------------------------------------------------------
  void jLL_itemStateChanged(ItemEvent e) {
    if( e.getStateChange() == ItemEvent.SELECTED ) {
      try {
          if ( (this.jCBLL.getSelectedIndex()<4) && (this.jCBNOL.getSelectedIndex() == -1) ) {
	    	     String message= LocaleKBCT.GetString("WarningScaleLabels");
	    	     MessageKBCT.Information(null, message);
	    	     this.jCBLL.setSelectedIndex(4);
          } else {
             switch (this.jCBLL.getSelectedIndex()) {
               case 0: this.Temp.SetScaleName("small-large"); break;
               case 1: this.Temp.SetScaleName("few-lot"); break;
               case 2: this.Temp.SetScaleName("low-high"); break;
               case 3: this.Temp.SetScaleName("negative-positive"); break;
               case 4: this.Temp.SetScaleName("user");
                       this.jButtonChangeName.setEnabled(true);
                       break;
             }
             this.Temp.SetLabelsName();
             this.Temp.SetORLabelsName();
             this.kbct.ReplaceOutput(this.Temp.GetPtr(), (JKBCTOutput)this.Temp);
          }
      } catch (Throwable t) {
        if (!t.toString().equals("java.lang.NullPointerException"))
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JOutputFrame: jLL_itemStateChanged: "+t);
      }
    }
  }
//------------------------------------------------------------------------------
  void jButtonNewScale_actionPerformed() {
    if (!JVariableFrame.JButtonNewScalePressed || JVariableFrame.AuthorizationButtonNewScale==JOptionPane.NO_OPTION) {
    	JVariableFrame.JButtonNewScalePressed = true;
    	JVariableFrame.AuthorizationButtonNewScale= MessageKBCT.Confirm(this, LocaleKBCT.GetString("OnlyExpertUsers") + "\n" + LocaleKBCT.GetString("DoYouWantToContinue"), 1, false, false, false);
    }
    if ( (JVariableFrame.AuthorizationButtonNewScale==JOptionPane.YES_OPTION) && (JVariableFrame.JButtonNewScalePressed) ) {
        try {
          JChangeNameFrame jcnf = new JChangeNameFrame(this, this.Temp, "ChangeName", 9);
          jcnf.setLocation(JChildFrame.ChildPosition(this, jcnf.getSize()));
          String[] Labels_Names= jcnf.Show();
          if (Labels_Names != null) {
            this.Temp.SetNewScale(Labels_Names);
            this.Temp.SetScaleName("user");
            this.Temp.SetLabelsName();
            this.Temp.SetORLabelsName();
            this.kbct.ReplaceOutput(this.Temp.GetPtr(), (JKBCTOutput)this.Temp);
          }
        } catch (Throwable t) {
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JOutputFrame: jButtonNewScale_actionPerformed: "+t);
        }
    }
  }
//------------------------------------------------------------------------------
  void jButtonChangeName_actionPerformed() {
    if (!JVariableFrame.JButtonChangeNamePressed || JVariableFrame.AuthorizationButtonChangeName==JOptionPane.NO_OPTION) {
    	JVariableFrame.JButtonChangeNamePressed = true;
    	JVariableFrame.AuthorizationButtonChangeName= MessageKBCT.Confirm(this, LocaleKBCT.GetString("OnlyExpertUsers") + "\n" + LocaleKBCT.GetString("DoYouWantToContinue"), 1, false, false, false);
    }
    if ( (JVariableFrame.AuthorizationButtonChangeName==JOptionPane.YES_OPTION) && (JVariableFrame.JButtonChangeNamePressed) ) {
        try {
          JChangeNameFrame jcnf = new JChangeNameFrame(this, this.Temp, "ChangeName", this.Temp.GetLabelsNumber());
          jcnf.setLocation(JChildFrame.ChildPosition(this, jcnf.getSize()));
          String[] Labels_Names= jcnf.Show();
          if (Labels_Names != null) {
            for (int n = 0; n < Labels_Names.length; n++) {
              String aux = Labels_Names[n];
              this.Temp.SetUserLabelsName(n + 1, aux);
            }
            this.Temp.SetScaleName("user");
            this.Temp.SetLabelsName();
            this.Temp.SetORLabelsName();
            this.kbct.ReplaceOutput(this.Temp.GetPtr(), (JKBCTOutput)this.Temp);
          }
        } catch (Throwable t) {
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JOutputFrame: jButtonChangeName_actionPerformed: "+t);
        }
    }
  }
//------------------------------------------------------------------------------
  void jButtonSetNbLabels_actionPerformed() {
	  if (!JVariableFrame.JButtonSetNbLabelsPressed || JVariableFrame.AuthorizationButtonSetNbLabels==JOptionPane.NO_OPTION) {
	    	JVariableFrame.JButtonSetNbLabelsPressed = true;
	    	JVariableFrame.AuthorizationButtonSetNbLabels= MessageKBCT.Confirm(this, LocaleKBCT.GetString("OnlyExpertUsers") + "\n" + LocaleKBCT.GetString("DoYouWantToContinue"), 1, false, false, false);
	  }
	  if ( (JVariableFrame.AuthorizationButtonSetNbLabels==JOptionPane.YES_OPTION) && (JVariableFrame.JButtonSetNbLabelsPressed) ) {
        final JDialog jd = new JDialog(this);
    	jd.setIconImage(LocaleKBCT.getIconGUAJE().getImage());
        jd.setTitle(LocaleKBCT.GetString("SetNbLabels"));
        jd.getContentPane().setLayout(new GridBagLayout());
        JPanel jPanelSaisie = new JPanel(new GridBagLayout());
        JPanel jPanelValidation = new JPanel(new GridBagLayout());
        JButton jButtonApply = new JButton(LocaleKBCT.GetString("Apply"));
        JButton jButtonCancel = new JButton(LocaleKBCT.GetString("Cancel"));
        JButton jButtonDefault = new JButton(LocaleKBCT.GetString("DefaultValues"));
        JLabel jLabelNbLabels = new JLabel(LocaleKBCT.GetString("NOL") + " :");
        this.jNbLabels.setValue(this.Temp.GetLabelsNumber());
        jd.getContentPane().add(jPanelSaisie, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
          ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        jPanelSaisie.add(jLabelNbLabels, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
          ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelSaisie.add(this.jNbLabels, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 40, 0));
        jd.getContentPane().add(jPanelValidation, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
          ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
        jPanelValidation.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
          ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        jPanelValidation.add(jButtonCancel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
          ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        jPanelValidation.add(jButtonDefault, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
          ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        jButtonApply.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
          int NbLab= JOutputFrame.this.jNbLabels.getValue();
          boolean warning= false;
          if (NbLab < 2) {
            MessageKBCT.Information(JOutputFrame.this, LocaleKBCT.GetString("NbLabelsShouldBe"));
            warning= true;
          } 
          if (!warning) {
        	JOutputFrame.this.UserNbLabels= NbLab;
        	JOutputFrame.this.Temp.SetScaleName("user");
        	JOutputFrame.this.Temp.SetLabelsNumber(NbLab);
        	JOutputFrame.this.Temp.SetLabelsName();
        	JOutputFrame.this.Temp.SetORLabelsName();
        	JOutputFrame.this.Temp.SetLabelProperties();
            if (JOutputFrame.this.Temp.GetV().GetType().equals("categorical")) {
            	JOutputFrame.this.Temp.SetInputPhysicalRange(1, JOutputFrame.this.Temp.GetLabelsNumber());
            	JOutputFrame.this.Temp.SetInputInterestRange(1, JOutputFrame.this.Temp.GetLabelsNumber());
              }
        	JOutputFrame.this.kbct.ReplaceOutput(JOutputFrame.this.Temp.GetPtr(), (JKBCTOutput)JOutputFrame.this.Temp);
            jd.dispose();
          }
        } } );
        jButtonCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
          jd.dispose();
        } } );
        jButtonDefault.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
    	  JOutputFrame.this.jNbLabels.setValue(JOutputFrame.this.Temp.GetLabelsNumber());
        } } );
        jd.setResizable(false);
        jd.setModal(true);
        jd.pack();
        jd.setLocation(JChildFrame.ChildPosition(this, jd.getSize()));
        jd.setVisible(true);
	  }
  }
//------------------------------------------------------------------------------
  void jButtonAdvOpt_actionPerformed() {
    if (!JVariableFrame.JButtonAdvancedOptionsPressed || JVariableFrame.AuthorizationButtonAdvancedOptions==JOptionPane.NO_OPTION) {
    	JVariableFrame.JButtonAdvancedOptionsPressed = true;
    	JVariableFrame.AuthorizationButtonAdvancedOptions= MessageKBCT.Confirm(this, LocaleKBCT.GetString("OnlyExpertUsers") + "\n" +
                                                                           LocaleKBCT.GetString("SemanticAnalysisOnlyStrongFuzzyPartitions") + "\n" +
                                                                           LocaleKBCT.GetString("DoYouWantToContinue"), 1, false, false, false);
    }
    if ( (JVariableFrame.AuthorizationButtonAdvancedOptions==JOptionPane.YES_OPTION) && (JVariableFrame.JButtonAdvancedOptionsPressed) ) {
        try {
          JAdvOptFrame jaof = new JAdvOptFrame(this, this.kbct, this.Number, this.Temp, "Output");
          jaof.setLocation(JChildFrame.ChildPosition(this, this.getSize()));
          jaof.Show();
        } catch (Throwable t) {
            MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error en JOutputFrame en jButtonAdvOpt_actionPerformed: "+t);
        }
    }
  }
//------------------------------------------------------------------------------
  protected void Replace( JKBCTOutput new_output ) throws Throwable { this.kbct.ReplaceOutput(this.Number, new_output); }
//------------------------------------------------------------------------------
  protected void PhysicalRangeChanged() { this.kbct.OutputPhysicalRangeChanged(this.Number); }
//------------------------------------------------------------------------------
  protected void InterestRangeChanged() { this.kbct.OutputInterestRangeChanged(this.Number); }
//------------------------------------------------------------------------------
  protected void ActiveChanged() { this.kbct.OutputActiveChanged(this.Number); }
//------------------------------------------------------------------------------
  protected void NameChanged() { this.kbct.OutputNameChanged(this.Number);}
//------------------------------------------------------------------------------
  public void this_windowClosing() { this.consistency(); }
//------------------------------------------------------------------------------
  public void consistency() {
    boolean warning= false;
    if (this.Temp.GetScaleName().equals("user")) {
      for (int n=0; n < this.Temp.GetLabelsNumber(); n++)
        if (this.Temp.GetUserLabelsName(n).equals(""))
          warning= true;
    }
    if (warning) {
      MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), LocaleKBCT.GetString("LabelNotDefined"));
      warning= false;
      try {
        JOutputFrame jof = new JOutputFrame(super.parent, this.kbct, this.Number);
        jof.Show();
      } catch (Throwable t) {
          MessageKBCT.Error(this, LocaleKBCT.GetString("Error"), "Error in JOutputFrame: consistency(): "+t);
      }
    }
    this.dispose();
  }
}