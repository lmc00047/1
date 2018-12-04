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
//                              JPanelGenerateSample.java
//
// Author(s) : Jean-luc LABLEE
// FISPRO Version : 3.0 - IDDN.FR.001.030024.000.R.P.2005.003.31235
// Contact : fispro@ensam.inra.fr
// Last modification date:  December 25, 2005
//
//**********************************************************************
package fis;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kbct.JKBCT;
import kbct.LocaleKBCT;
import kbctAux.DoubleField;
import kbctAux.IntegerField;

/**
 * fis.JPanelGenerateSample.
 *
 *@author     Jose Maria Alonso Moral
 *@version    3.0 , 04/06/13
 */
public class JPanelGenerateSample extends JPanel {
    static final long serialVersionUID=0;	
    private JLabel jLabelSampleNumber = null;
    private IntegerField integerFieldSampleNumber = null;
    private JLabel jLabelValidationDataRatio = null;
    private IntegerField integerFieldValidationDataRatio = null;
    private JLabel jLabelPercent = null;
    private JCheckBox jCheckBoxClassif = null;
    private JLabel jLabelSeed = null;
    private IntegerField integerFieldSeed = null;
    private JLabel jLabelPrecision = null;
    private DoubleField doubleFieldPrecision = null;
    private JExtendedDataFile dataFile = null;
    private JComboBox jComboBoxColumnNumber = null;
    private JKBCT kbct = null;

    /**
     * This method initializes 
     */
    public JPanelGenerateSample(JExtendedDataFile dataFile, JKBCT kbct) {
        super();
        //System.out.println("constructor de JPanelGenerateSample");
        this.dataFile = dataFile;
        this.kbct = kbct;
        initialize();
        // init classif jCheckBoxClassif
        boolean classif = false;
        if (this.kbct!=null) {
        	if (this.kbct.GetOutput(1).GetClassif().equals("yes"))
        		classif=true;
        }
        jCheckBoxClassif.setSelected(classif);
        this.setClassif(classif);
        // remplissage de jComboBoxColumnNumber
        Vector<String> items = new Vector<String>();
        for (int i = 0; i < this.dataFile.VariableCount(); i++) {
            try {
                items.add(this.VariableName(i));
            } catch (Throwable e) {
                items.add(Integer.toString(i + 1));
            }
        }
        jComboBoxColumnNumber.setModel(new DefaultComboBoxModel(items));
        jComboBoxColumnNumber.setSelectedIndex(this.dataFile.VariableCount() - 1);
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridx = 1;
        GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
        gridBagConstraints81.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints81.gridy = 6;
        gridBagConstraints81.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints81.insets = new java.awt.Insets(5, 5, 0, 5);
        gridBagConstraints81.ipadx = 40;
        gridBagConstraints81.gridx = 1;
        GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
        gridBagConstraints71.gridx = 0;
        gridBagConstraints71.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints71.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints71.gridy = 6;
        jLabelPrecision = new JLabel();
        jLabelPrecision.setText(LocaleKBCT.GetString("Precision") + ":");
        jLabelPrecision.setEnabled(false);
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints4.gridy = 3;
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints4.insets = new java.awt.Insets(5, 5, 0, 5);
        gridBagConstraints4.ipadx = 40;
        gridBagConstraints4.gridx = 1;
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints3.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints3.gridy = 3;
        jLabelSeed = new JLabel();
        jLabelSeed.setText(LocaleKBCT.GetString("Seed") + ":");
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints2.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints2.gridy = 4;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.insets = new java.awt.Insets(5, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints1.gridy = 2;
        jLabelPercent = new JLabel();
        jLabelPercent.setText("%");
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        gridBagConstraints9.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints9.gridy = 2;
        gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints9.insets = new java.awt.Insets(5, 5, 0, 5);
        gridBagConstraints9.ipadx = 40;
        gridBagConstraints9.gridx = 1;
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.gridx = 0;
        gridBagConstraints8.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints8.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints8.gridy = 2;
        jLabelValidationDataRatio = new JLabel();
        jLabelValidationDataRatio.setText(LocaleKBCT.GetString("ValidationDataRatio") + ":");
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints7.gridy = 1;
        gridBagConstraints7.insets = new java.awt.Insets(5, 5, 0, 5);
        gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints7.ipadx = 40;
        gridBagConstraints7.gridx = 1;
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.gridx = 0;
        gridBagConstraints6.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints6.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints6.gridy = 1;
        jLabelSampleNumber = new JLabel();
        jLabelSampleNumber.setText(LocaleKBCT.GetString("SampleNumber") + ":");
        this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(211, 130));
        this.add(jLabelSampleNumber, gridBagConstraints6);
        this.add(getIntegerFieldSampleNumber(), gridBagConstraints7);
        this.add(getIntegerFieldValidationDataRatio(), gridBagConstraints9);
        this.add(jLabelValidationDataRatio, gridBagConstraints8);
        this.add(jLabelPercent, gridBagConstraints1);
        this.add(getJCheckBoxClassif(), gridBagConstraints2);
        this.add(jLabelSeed, gridBagConstraints3);
        this.add(getIntegerFieldSeed(), gridBagConstraints4);
        this.add(jLabelPrecision, gridBagConstraints71);
        this.add(getDoubleFieldPrecision(), gridBagConstraints81);
        this.add(getJComboBoxColumnNumber(), gridBagConstraints);
    }

    /**
     * This method initializes integerField2	
     * 	
     * @return kbctAux.IntegerField	
     */
    private IntegerField getIntegerFieldSampleNumber() {
        if (integerFieldSampleNumber == null) {
            integerFieldSampleNumber = new IntegerField();
            integerFieldSampleNumber.setValue(1);
        }
        return integerFieldSampleNumber;
    }

    /**
     * This method initializes integerField3	
     * 	
     * @return kbctAux.IntegerField	
     */
    private IntegerField getIntegerFieldValidationDataRatio() {
        if (integerFieldValidationDataRatio == null) {
            integerFieldValidationDataRatio = new IntegerField();
            integerFieldValidationDataRatio.setValue(75);
        }
        return integerFieldValidationDataRatio;
    }

    /**
     * This method initializes jCheckBoxClassif	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getJCheckBoxClassif() {
        if (jCheckBoxClassif == null) {
            jCheckBoxClassif = new JCheckBox();
            jCheckBoxClassif.setText(LocaleKBCT.GetString("Classif"));
            jCheckBoxClassif.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setClassif(jCheckBoxClassif.isSelected());
                }
            });
        }
        return jCheckBoxClassif;
    }

    /**
     * This method initializes integerFieldSeed	
     * 	
     * @return kbctAux.IntegerField	
     */
    private IntegerField getIntegerFieldSeed() {
        if (integerFieldSeed == null) {
            integerFieldSeed = new IntegerField();
            integerFieldSeed.setValue(1);
        }
        return integerFieldSeed;
    }

    /**
     * This method initializes doubleField	
     * 	
     * @return kbctAux.DoubleField	
     */
    private DoubleField getDoubleFieldPrecision() {
        if (doubleFieldPrecision == null) {
            doubleFieldPrecision = new DoubleField();
            doubleFieldPrecision.setValue(0.01D);
            doubleFieldPrecision.setEnabled(false);
        }
        return doubleFieldPrecision;
    }

    public int getSampleNumber() {
        return this.integerFieldSampleNumber.getValue();
    }

    public double getValidationDataRatio() {
        return (double) this.integerFieldValidationDataRatio.getValue() / 100;
    }

    public int getSeed() {
        return this.integerFieldSeed.getValue();
    }

    public boolean getClassif() {
        return this.jCheckBoxClassif.isSelected();
    }

    public int getColumnNumber() {
        return this.jComboBoxColumnNumber.getSelectedIndex();
    }

    public double getPrecision() {
        return this.doubleFieldPrecision.getValue();
    }

    /**
     * This method initializes jComboBoxColumnNumber	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getJComboBoxColumnNumber() {
        if (jComboBoxColumnNumber == null) {
            jComboBoxColumnNumber = new JComboBox();
            jComboBoxColumnNumber.setEnabled(false);
        }
        return jComboBoxColumnNumber;
    }

    private String VariableName(int index) throws Throwable {
        if (this.kbct == null) { 
                return LocaleKBCT.GetString("Variable") + " " + Integer.toString(index + 1); // retourne variable index
        } else {
            int nbInputs=this.kbct.GetNbInputs();
        	if (index < nbInputs)
        	    return this.kbct.GetInput(index+1).GetName();
            else
        	    return this.kbct.GetOutput(index-nbInputs+1).GetName();
        }
    }

    private void setClassif(boolean classif) {
        jComboBoxColumnNumber.setEnabled(classif);
        jLabelPrecision.setEnabled(classif);
        doubleFieldPrecision.setEnabled(classif);
    }

    public boolean getReplace() {
        return true;
    }

    public boolean getTruncate() {
        return false;
    }
}
