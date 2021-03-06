/*
 * $Id: CustomTimeDialog.java,v 1.5 2004/03/10 04:47:35 fc Exp $
 *
 * JStars is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * JStars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * Copyright (c) 2003 Florent Charpin
 * 
 * email: jstars@florent.us
 *
 *
 */

package jstars.gui;

import jstars.util.Jlibastro;
import java.text.DecimalFormat;

/**
 *
 * @author  florent Charpin
 */
public class CustomTimeDialog extends javax.swing.JDialog {
    static DecimalFormat TimeFormatter = new DecimalFormat("00");
    static DecimalFormat YearFormatter = new DecimalFormat("0000");
    Jlibastro.Jdate jdate;
    Jlibastro.Jdate jdate1;
    boolean bTZ = true;
    
    /** Creates new form CustomTimeDialog */
    public CustomTimeDialog(java.awt.Frame parent, boolean modal, Jlibastro.Jdate datetime,
    String TZ, String Title, boolean cal) {
        super(parent, modal);
        jdate1 = new Jlibastro.Jdate();
        jdate = datetime;
        initComponents();
        if(TZ == null) {
            bTZ = false;
            mainPanel.remove(TZLabel);
            mainHeader.setText(Title);
            setTitle("Enter Interval");
            mainPanel.remove(YearField);
            mainPanel.remove(monthField);
            mainPanel.remove(div1);
            mainPanel.remove(div2);
            mainPanel.remove(yearLabel);
            mainPanel.remove(monthLabel);
            mainPanel.remove(dayField);
            mainPanel.remove(dayLabel);
            
            if(cal) {
                java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 2;
                gridBagConstraints.ipadx = 5;
                gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
                gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
                mainPanel.add(dayField, gridBagConstraints);
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 3;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
                gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
                gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
                dayLabel.setText("days");
                mainPanel.add(dayLabel, gridBagConstraints);
                
            }
            
        }
        else {
            TZLabel.setText("Time Zone: " + TZ);
        }
        pack();
        YearField.setText(YearFormatter.format(jdate.year));
        monthField.setText(TimeFormatter.format(jdate.month));
        if((cal) && (TZ == null))
            dayField.setText(YearFormatter.format(jdate.day));
        else
            dayField.setText(TimeFormatter.format(jdate.day));
        hourField.setText(TimeFormatter.format(jdate.hour));
        minField.setText(TimeFormatter.format(jdate.min));
        secField.setText(TimeFormatter.format(jdate.sec));
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        mainPanel = new javax.swing.JPanel();
        mainHeader = new javax.swing.JLabel();
        YearField = new javax.swing.JTextField();
        div1 = new javax.swing.JLabel();
        monthField = new javax.swing.JTextField();
        div2 = new javax.swing.JLabel();
        dayField = new javax.swing.JTextField();
        yearLabel = new javax.swing.JLabel();
        monthLabel = new javax.swing.JLabel();
        dayLabel = new javax.swing.JLabel();
        hourField = new javax.swing.JTextField();
        div3 = new javax.swing.JLabel();
        minField = new javax.swing.JTextField();
        div4 = new javax.swing.JLabel();
        secField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        TZLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        OKButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setTitle("Enter Time & Date");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        mainPanel.setLayout(new java.awt.GridBagLayout());

        mainPanel.setBackground(new java.awt.Color(153, 204, 255));
        mainHeader.setFont(new java.awt.Font("Dialog", 1, 18));
        mainHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mainHeader.setText("Enter New Date and Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 5, 15);
        mainPanel.add(mainHeader, gridBagConstraints);

        YearField.setFont(new java.awt.Font("Dialog", 1, 14));
        YearField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        YearField.setText("0000");
        YearField.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        mainPanel.add(YearField, gridBagConstraints);

        div1.setFont(new java.awt.Font("Dialog", 1, 14));
        div1.setText("/");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(12, 4, 0, 4);
        mainPanel.add(div1, gridBagConstraints);

        monthField.setFont(new java.awt.Font("Dialog", 1, 14));
        monthField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        monthField.setText("00");
        monthField.setPreferredSize(null);
        monthField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthFieldActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        mainPanel.add(monthField, gridBagConstraints);

        div2.setFont(new java.awt.Font("Dialog", 1, 14));
        div2.setText("/");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(12, 4, 0, 4);
        mainPanel.add(div2, gridBagConstraints);

        dayField.setFont(new java.awt.Font("Dialog", 1, 14));
        dayField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        dayField.setText("00");
        dayField.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        mainPanel.add(dayField, gridBagConstraints);

        yearLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        yearLabel.setText("year");
        yearLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 15);
        mainPanel.add(yearLabel, gridBagConstraints);

        monthLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        monthLabel.setText("month");
        monthLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        mainPanel.add(monthLabel, gridBagConstraints);

        dayLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        dayLabel.setText("day");
        dayLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        mainPanel.add(dayLabel, gridBagConstraints);

        hourField.setFont(new java.awt.Font("Dialog", 1, 14));
        hourField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        hourField.setText("00");
        hourField.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        mainPanel.add(hourField, gridBagConstraints);

        div3.setFont(new java.awt.Font("Dialog", 1, 14));
        div3.setText(":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 0, 4);
        mainPanel.add(div3, gridBagConstraints);

        minField.setFont(new java.awt.Font("Dialog", 1, 14));
        minField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        minField.setText("00");
        minField.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        mainPanel.add(minField, gridBagConstraints);

        div4.setFont(new java.awt.Font("Dialog", 1, 14));
        div4.setText(":");
        div4.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 0, 4);
        mainPanel.add(div4, gridBagConstraints);

        secField.setFont(new java.awt.Font("Dialog", 1, 14));
        secField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        secField.setText("00");
        secField.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        mainPanel.add(secField, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel9.setText("hour");
        jLabel9.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 15, 4);
        mainPanel.add(jLabel9, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel10.setText("min");
        jLabel10.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 15, 0);
        mainPanel.add(jLabel10, gridBagConstraints);

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel11.setText("sec");
        jLabel11.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 15, 0);
        mainPanel.add(jLabel11, gridBagConstraints);

        TZLabel.setText("Time Zone: UTC");
        TZLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        mainPanel.add(TZLabel, gridBagConstraints);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        jPanel2.setMaximumSize(null);
        OKButton.setText("OK");
        OKButton.setPreferredSize(null);
        OKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKButtonActionPerformed(evt);
            }
        });

        jPanel2.add(OKButton);

        cancelButton.setText("CANCEL");
        cancelButton.setPreferredSize(null);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jPanel2.add(cancelButton);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }//GEN-END:initComponents
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    private void OKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKButtonActionPerformed
        
        jdate1.year = Integer.parseInt(YearField.getText());
        jdate1.month = Integer.parseInt(monthField.getText());
        jdate1.day = Integer.parseInt(dayField.getText());
        jdate1.hour = Integer.parseInt(hourField.getText());
        jdate1.min = Integer.parseInt(minField.getText());
        jdate1.sec = Integer.parseInt(secField.getText());
        //test if date+time OK
        if(bTZ == false) {
            if(jdate1.isValidTime() == false) {
                javax.swing.JOptionPane.showMessageDialog(null,"Invalid Values Entered",
                "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        else if(jdate1.isValidDate() == false) {
            javax.swing.JOptionPane.showMessageDialog(null,"Invalid Date Entered",
            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        jdate.year = jdate1.year;
        jdate.month = jdate1.month;
        jdate.day = jdate1.day;
        jdate.hour = jdate1.hour;
        jdate.min = jdate1.min;
        jdate.sec = jdate1.sec;
        
        setVisible(false);
        dispose();
    }//GEN-LAST:event_OKButtonActionPerformed
    
    private void monthFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthFieldActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_monthFieldActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton OKButton;
    private javax.swing.JLabel TZLabel;
    private javax.swing.JTextField YearField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField dayField;
    private javax.swing.JLabel dayLabel;
    private javax.swing.JLabel div1;
    private javax.swing.JLabel div2;
    private javax.swing.JLabel div3;
    private javax.swing.JLabel div4;
    private javax.swing.JTextField hourField;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel mainHeader;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField minField;
    private javax.swing.JTextField monthField;
    private javax.swing.JLabel monthLabel;
    private javax.swing.JTextField secField;
    private javax.swing.JLabel yearLabel;
    // End of variables declaration//GEN-END:variables
    
}
