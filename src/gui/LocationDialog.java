/*
 * LocationDialog.java
 *
 * $Id: LocationDialog.java,v 1.9 2010/01/30 21:39:18 fc Exp $
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

import jstars.util.Location;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.TreeNode;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Document;


/**
 *
 * @author  florent Charpin
 */
public class LocationDialog extends javax.swing.JDialog {
    
    Location location, lochelp;
    jstars.gui.JStarsGUI mainwin;
    
    /** Creates new form LocationDialog */
    public LocationDialog(java.awt.Frame parent, boolean modal, Location loc) {
        super(parent, modal);
        location = loc;
        mainwin = (JStarsGUI) parent;
        initComponents();
        nameField.setText(location.name);
        TZLabel.setText(location.getLocalTZ());
        LatLabel.setText(location.latString());
        LonLabel.setText(location.lonString());
        lochelp = new Location();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        OkButton = new javax.swing.JButton();
        ApplyButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        dispPanel = new javax.swing.JPanel();
        TZLabel = new javax.swing.JLabel();
        TimeZoneButton = new javax.swing.JButton();
        nameField = new javax.swing.JTextField();
        LatLabel = new javax.swing.JLabel();
        LonLabel = new javax.swing.JLabel();
        LonButton = new javax.swing.JButton();
        TZLabel3 = new javax.swing.JLabel();
        TimeZoneButton3 = new javax.swing.JButton();
        LatButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        LocTree = new javax.swing.JTree(buildTreeFromXML());
        TreeSelectionModel tsm = LocTree.getSelectionModel();
        tsm.setSelectionMode(javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION);

        setTitle("Location Editor");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jSplitPane1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 12, 5));

        OkButton.setText("OK");
        OkButton.setPreferredSize(null);
        OkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OkButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(OkButton);

        ApplyButton.setText("Apply");
        ApplyButton.setPreferredSize(null);
        ApplyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ApplyButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(ApplyButton);

        CancelButton.setText("Cancel");
        CancelButton.setPreferredSize(null);
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(CancelButton);

        jPanel1.add(buttonPanel, java.awt.BorderLayout.SOUTH);

        mainPanel.setLayout(new java.awt.BorderLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        dispPanel.setLayout(new java.awt.GridBagLayout());

        dispPanel.setBackground(new java.awt.Color(255, 255, 255));
        dispPanel.setPreferredSize(new java.awt.Dimension(350, 190));
        TZLabel.setText("GMT+0");
        TZLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 5);
        dispPanel.add(TZLabel, gridBagConstraints);

        TimeZoneButton.setText("Time Zone");
        TimeZoneButton.setFocusable(false);
        TimeZoneButton.setPreferredSize(null);
        TimeZoneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TimeZoneButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 0, 5);
        dispPanel.add(TimeZoneButton, gridBagConstraints);

        nameField.setFont(new java.awt.Font("Dialog", 1, 12));
        nameField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        nameField.setPreferredSize(null);
        nameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameFieldFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 12;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 5);
        dispPanel.add(nameField, gridBagConstraints);

        LatLabel.setText("N 0:00:00");
        LatLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 10, 0, 5);
        dispPanel.add(LatLabel, gridBagConstraints);

        LonLabel.setText("W 0:00:00");
        LonLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 5);
        dispPanel.add(LonLabel, gridBagConstraints);

        LonButton.setText("Longitude");
        LonButton.setFocusable(false);
        LonButton.setMaximumSize(new java.awt.Dimension(100, 25));
        LonButton.setPreferredSize(null);
        LonButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LonButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 0, 5);
        dispPanel.add(LonButton, gridBagConstraints);

        TZLabel3.setText("---");
        TZLabel3.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 5);
        dispPanel.add(TZLabel3, gridBagConstraints);

        TimeZoneButton3.setText("Elevation");
        TimeZoneButton3.setFocusable(false);
        TimeZoneButton3.setMaximumSize(new java.awt.Dimension(600, 600));
        TimeZoneButton3.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 0, 5);
        dispPanel.add(TimeZoneButton3, gridBagConstraints);

        LatButton.setText("Latitude");
        LatButton.setFocusable(false);
        LatButton.setMaximumSize(new java.awt.Dimension(100, 25));
        LatButton.setPreferredSize(null);
        LatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LatButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 0, 5);
        dispPanel.add(LatButton, gridBagConstraints);

        jLabel1.setText("Location Name:");
        jLabel1.setMaximumSize(new java.awt.Dimension(38, 30));
        jLabel1.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 0, 5);
        dispPanel.add(jLabel1, gridBagConstraints);

        mainPanel.add(dispPanel, java.awt.BorderLayout.CENTER);

        jPanel1.add(mainPanel, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel1);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(100, 200));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 363));
        LocTree.setMaximumSize(new java.awt.Dimension(600, 1600));
        LocTree.setRootVisible(false);
        LocTree.setShowsRootHandles(true);
        LocTree.setAutoscrolls(true);
        LocTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LocTreeMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(LocTree);

        jSplitPane1.setLeftComponent(jScrollPane1);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents
    
    private void LocTreeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LocTreeMouseClicked
        javax.swing.JTree t = (javax.swing.JTree) evt.getSource();
        javax.swing.tree.TreeNode node = (TreeNode) t.getClosestPathForLocation(evt.getX(), evt.getY()).getLastPathComponent();
        if(node.isLeaf() == true) {
            nameField.setText(((LocNode)node).loc.name);
            TZLabel.setText(((LocNode)node).loc.getLocalTZ());
            LatLabel.setText(((LocNode)node).loc.latString());
            LonLabel.setText(((LocNode)node).loc.lonString());
        }
        
    }//GEN-LAST:event_LocTreeMouseClicked
    
    private void LonButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LonButtonActionPerformed
        String str = (String) javax.swing.JOptionPane.showInputDialog("Please enter new longitude",
        LonLabel.getText());
        if(str != null) {
            double d = mainwin.libastro.fromHHMMSS(str,180);
            if(!Double.isNaN(d)) {
                lochelp.setLongitude(d);
                LonLabel.setText(lochelp.lonString());
            }
            else {
                javax.swing.JOptionPane.showMessageDialog(null,"Invalid Longitude",
                "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_LonButtonActionPerformed
    
    private void LatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LatButtonActionPerformed
        String str = (String) javax.swing.JOptionPane.showInputDialog("Please enter new latitude",
        LatLabel.getText());
        if(str != null) {
            double d = mainwin.libastro.fromHHMMSS(str, 90);
            if(!Double.isNaN(d)) {
                lochelp.setLatitude(d);
                LatLabel.setText(lochelp.latString());
            }
            else {
                javax.swing.JOptionPane.showMessageDialog(null,"Invalid Latitude",
                "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
            
        }
    }//GEN-LAST:event_LatButtonActionPerformed
    
    private void nameFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameFieldFocusLost
        location.name = nameField.getText();
    }//GEN-LAST:event_nameFieldFocusLost
    
    private void OkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OkButtonActionPerformed
        setValues();
        setVisible(false);
        dispose();
    }//GEN-LAST:event_OkButtonActionPerformed
    
    private void ApplyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ApplyButtonActionPerformed
        setValues();
    }//GEN-LAST:event_ApplyButtonActionPerformed
    
    private void TimeZoneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TimeZoneButtonActionPerformed
        Object[] selvalues =  java.util.SimpleTimeZone.getAvailableIDs();
        java.util.Arrays.sort(selvalues);
        String str = (String) javax.swing.JOptionPane.showInputDialog(this,
        "Select Time Zone",
        "Select Time Zone",
        javax.swing.JOptionPane.QUESTION_MESSAGE,
        null,
        selvalues,
        TZLabel.getText());
        if(str != null)
            TZLabel.setText(str);
        
    }//GEN-LAST:event_TimeZoneButtonActionPerformed
    
    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelButtonActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_CancelButtonActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    void setValues() {
        location.name = nameField.getText();
        location.setLocalTZ(TZLabel.getText());
        location.setLatitude(mainwin.libastro.fromHHMMSS(LatLabel.getText(), 90));
        location.setLongitude(mainwin.libastro.fromHHMMSS(LonLabel.getText(), 180));
        mainwin.libastro.updateLocation();
        mainwin.updateLoc();
        mainwin.libastro.updateTime();
        mainwin.updateFrames();
    }
    
    
    DefaultTreeModel buildTreeFromXML() {
 
        DefaultMutableTreeNode jroot = new DefaultMutableTreeNode("Location");
        DefaultMutableTreeNode gparent, parent, city;
        Document document;
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            java.io.InputStream xmlURL = getClass().getResourceAsStream("location.xml");
            document = builder.parse(xmlURL);
        }
        catch(ParserConfigurationException pce) {
            pce.printStackTrace();
            return new DefaultTreeModel(jroot);
        }
        catch(org.xml.sax.SAXException sxe) {
            sxe.printStackTrace();
            return new DefaultTreeModel(jroot);
        }
        catch(java.io.IOException ioe) {
            ioe.printStackTrace();
            return new DefaultTreeModel(jroot);
        }
        
        Element root = document.getDocumentElement();
        if(root.getTagName().compareTo("location") != 0) {
            System.out.println("Error: Cannot location <location> element in location.xml file");
            return new DefaultTreeModel(jroot);
        }
        org.w3c.dom.NodeList nl = root.getChildNodes();
        for(int i=0; nl.getLength() > i; i++) {
            if(nl.item(i).getNodeName().equals("zone")) {
                gparent = new DefaultMutableTreeNode(nl.item(i).getAttributes().getNamedItem("name").getNodeValue());
                jroot.add(gparent);
                org.w3c.dom.NodeList gnl = nl.item(i).getChildNodes();
                for(int j=0; gnl.getLength() > j; j++) {
                    if(gnl.item(j).getNodeName().equals("area")) {
                        parent = new DefaultMutableTreeNode(gnl.item(j).getAttributes().getNamedItem("name").getNodeValue());
                        gparent.add(parent);
                        org.w3c.dom.NodeList nlsite = gnl.item(j).getChildNodes();
                        buildSubTree(parent, nlsite);
                    }
                }
            }
            if(nl.item(i).getNodeName().equals("area")) {
                parent = new DefaultMutableTreeNode(nl.item(i).getAttributes().getNamedItem("name").getNodeValue());
                jroot.add(parent);
                org.w3c.dom.NodeList nlsite = nl.item(i).getChildNodes();
                buildSubTree(parent, nlsite);
                
            }
        }
        
        return new DefaultTreeModel(jroot);
    }
    
    void buildSubTree(DefaultMutableTreeNode parent, org.w3c.dom.NodeList tnode) {
        double d;
        for(int j=0; tnode.getLength() > j; j++) {
            org.w3c.dom.Node sitenode =  tnode.item(j);
            if(sitenode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                org.w3c.dom.NodeList child = sitenode.getChildNodes();
                LocNode locnode = new LocNode();
                for(int k=0; child.getLength() > k; k++) {
                    if(child.item(k).getNodeName().equals("name")) {
                        locnode.loc.name = child.item(k).getFirstChild().getNodeValue();
                        parent.add(locnode);
                    }
                    if(child.item(k).getNodeName().equals("TZ")) {
                        locnode.loc.setLocalTZ(child.item(k).getFirstChild().getNodeValue());
                    }
                    if(child.item(k).getNodeName().equals("latitude")) {
                        d = mainwin.libastro.fromHHMMSS(child.item(k).getFirstChild().getNodeValue(), 90);
                        if(!Double.isNaN(d)) {
                            locnode.loc.setLatitude(d);
                        }
                        else {
                            locnode.loc.setLatitude(0.0);
                        }
                    }
                    if(child.item(k).getNodeName().equals("longitude")) {
                        d = mainwin.libastro.fromHHMMSS(child.item(k).getFirstChild().getNodeValue(), 180);
                        if(!Double.isNaN(d)) {
                            locnode.loc.setLongitude(d);
                        }
                        else {
                            locnode.loc.setLongitude(0.0);
                        }
                    }
                }
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ApplyButton;
    private javax.swing.JButton CancelButton;
    private javax.swing.JButton LatButton;
    private javax.swing.JLabel LatLabel;
    private javax.swing.JTree LocTree;
    private javax.swing.JButton LonButton;
    private javax.swing.JLabel LonLabel;
    private javax.swing.JButton OkButton;
    private javax.swing.JLabel TZLabel;
    private javax.swing.JLabel TZLabel3;
    private javax.swing.JButton TimeZoneButton;
    private javax.swing.JButton TimeZoneButton3;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel dispPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField nameField;
    // End of variables declaration//GEN-END:variables
    
}

class LocNode extends javax.swing.tree.DefaultMutableTreeNode {
    public Location loc;
    
    LocNode() {
        loc = new Location();
    }
    
    @Override
    public String toString() {
        return loc.name;
    }
    
}

