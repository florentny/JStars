/*
 * dataWin.java
 *
 * $Id: dataWin.java,v 1.10 2013/09/13 03:08:11 fc Exp $
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import jstars.util.ephemObject;

/**
 *
 * @author  fc
 */



public class dataWin extends javax.swing.JFrame implements java.awt.print.Printable {
    
    class RowRenderer extends javax.swing.table.DefaultTableCellRenderer {
        final Color color = new Color(247,245,213);
        String head = null;
        
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            
            if(row % 2 == 0)
                setBackground(Color.white);
            else
                setBackground(color);
            
            head = table.getColumnName(column);
            if(head.equals(headerName[0])) {
                setHorizontalAlignment(SwingConstants.LEFT);
            }
            else if((head.equals(headerName[7])) || (head.equals(headerName[9])) || (head.equals(headerName[11]))) {
                setHorizontalAlignment(SwingConstants.CENTER);
            }
            else {
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
            
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
        }
    }
    
    javax.swing.JToggleButton dataButton;
    selectDialog dialog;
    private jstars.util.Jlibastro libastro;
    private DefaultTableModel model;
    // if changing headerName[] change getTableCellRendererComponent() as well.
    private String headerName[] = {"Object", "Right Asc.", "Declination", "Azimuth", "Altitude",
    "Ecl. Long.", "Ecl. Lat.", "Rise", "Rise Az.", "Transit", "Tran. Alt", "Set", "Set Az.", "Time Up",
    "Earth Dist." , "Sun Dist.", "Elong.", "Phase", "Mag."};
    private boolean dispCol[] = { true, true, true, true, true, false, false, true, false, true, false, true,
    false, false,  false, false, false, false, false };
    private int colNum = 8;
    
    private String rowName[] =new String[11];
    private boolean dispRow[] = new boolean[11];
    private int rowNum = 11;
    
    
    /** Creates new form dataWin */
    public dataWin(jstars.util.Jlibastro libastro, javax.swing.JToggleButton Button) {
        dataButton = Button;
        this.libastro = libastro;
        // <TEMP>
        for(int i=0; i < 7; i++)
            dispRow[i] = true;
        dispRow[3] = dispRow[7] = dispRow[8] = dispRow[9] = false;
        dispRow[10] = true;
        rowNum = 11;
        for(int i=0; i < rowNum; i++)
            rowName[i] = libastro.SolSys[i].toString();
        // </TEMP>
        model = new DefaultTableModel();
        initComponents();
        //table.setModel(model);
        //model = (javax.swing.table.DefaultTableModel) table.getModel();
        createTable();
        //pack();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        mainPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        TimeModeLabel = new javax.swing.JLabel();
        MenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        saveMenuItem = new javax.swing.JMenuItem();
        printMenuItem = new javax.swing.JMenuItem();
        closeMenuItem = new javax.swing.JMenuItem();
        setupMenu = new javax.swing.JMenu();
        ColButton = new javax.swing.JMenuItem();
        rowButton = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Data Table");
        setBackground(new java.awt.Color(255, 255, 255));
        setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(getClass().getResource("/jstars/images/data.gif")));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        mainPanel.setLayout(new java.awt.BorderLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        table.setFocusable(false);
        table.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 250));
        table.setPreferredSize(null);
        jScrollPane1.setViewportView(table);

        mainPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        TimeModeLabel.setBackground(new java.awt.Color(255, 255, 255));
        TimeModeLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        TimeModeLabel.setText("TZ: EDT  Equa: Geocentric 2003.6");
        TimeModeLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        TimeModeLabel.setPreferredSize(null);
        TimeModeLabel.setOpaque(true);
        getContentPane().add(TimeModeLabel, java.awt.BorderLayout.SOUTH);

        MenuBar.setPreferredSize(null);
        fileMenu.setText("File");
        saveMenuItem.setText("Save to File");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(saveMenuItem);

        printMenuItem.setText("Print");
        printMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(printMenuItem);

        closeMenuItem.setText("Close");
        closeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(closeMenuItem);

        MenuBar.add(fileMenu);

        setupMenu.setText("Setup");
        ColButton.setText("Columns");
        ColButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ColButtonActionPerformed(evt);
            }
        });

        setupMenu.add(ColButton);

        rowButton.setText("Rows");
        rowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rowButtonActionPerformed(evt);
            }
        });

        setupMenu.add(rowButton);

        MenuBar.add(setupMenu);

        toolsMenu.setText("Tools");
        jMenuItem1.setText("Sky At A Glance");
        jMenuItem1.setEnabled(false);
        toolsMenu.add(jMenuItem1);

        jMenuItem2.setText("Year planner");
        jMenuItem2.setEnabled(false);
        toolsMenu.add(jMenuItem2);

        MenuBar.add(toolsMenu);

        setJMenuBar(MenuBar);

        pack();
    }//GEN-END:initComponents
    
    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        final JFileChooser fc = new JFileChooser();
        
        int returnVal = fc.showSaveDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fc.getSelectedFile();
            try {
                java.io.FileWriter out = new java.io.FileWriter(file);
                for(int i=0; i < table.getRowCount(); i++) {
                    for(int j=0; j < table.getColumnCount(); j++) {
                        out.write((String) table.getValueAt(i, j) + ",");
                    }
                    out.write("\n");
                }
                out.flush();
                out.close();
            }
            catch(java.io.IOException exp) {
            }
        }
        
    }//GEN-LAST:event_saveMenuItemActionPerformed
    
    private void printMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printMenuItemActionPerformed
        java.awt.print.PrinterJob pj = java.awt.print.PrinterJob.getPrinterJob();
        pj.setPrintable(this);
        if(pj.printDialog() == false)
            return;
        try{
            pj.print();
        } catch (Exception PrintException) {
        }
    }//GEN-LAST:event_printMenuItemActionPerformed
    
    private void closeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeMenuItemActionPerformed
        dataButton.setSelected(false);
        dispose();
    }//GEN-LAST:event_closeMenuItemActionPerformed
    
    private void rowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rowButtonActionPerformed
        if(dialog == null)
            dialog = new selectDialog(this, true, rowName, dispRow);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        dialog = null;
        model = new DefaultTableModel();
        //table.setModel(model);
        createTable();
    }//GEN-LAST:event_rowButtonActionPerformed
    
    private void ColButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ColButtonActionPerformed
        if(dialog == null)
            dialog = new selectDialog(this, true, headerName, dispCol);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        dialog = null;
        colNum = 0;
        for(int i=0; i < dispCol.length; i++)
            if(dispCol[i] == true)
                colNum++;
        model = new DefaultTableModel();
        //table.setModel(model);
        createTable();
    }//GEN-LAST:event_ColButtonActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        //System.exit(0);
        dataButton.setSelected(false);
        dispose();
    }//GEN-LAST:event_exitForm
    
    public void adjustDisp() {
        model = new DefaultTableModel();
        //table.setModel(model);
        createTable();
    }
    
    void createTable() {
        int tw = 0, th = 0;
        table.setModel(model);
        libastro.SolSys[ephemObject.EARTH].calculatePosition();
        for(int i=0; i < headerName.length; i++) {
            if(dispCol[i] == true)
                model.addColumn(headerName[i]);
        }
        for(int i=0; i < rowNum; i++) {
            if(dispRow[i] == false)
                continue;
            model.addRow(getRow(i));
        }
        
        int colCount = table.getColumnCount();
        javax.swing.table.TableColumn column;
        RowRenderer rowrend = new RowRenderer();
        for(int i=0; i < colCount; i++) {
            column = table.getColumn(table.getColumnName(i));
            //column.setCellRenderer(new RowRenderer());
            column.setCellRenderer(rowrend);
        }
        
        // set column width
        int cw = 75, tmp = 0;
        //javax.swing.table.TableCellRenderer tcr;
        //int colnumber = table.getColumnCount();
        javax.swing.table.TableColumn tc = null;
        java.awt.Component component;
        int j = 0;
        for(int i=0; i < headerName.length; i++) {
            if(dispCol[i] == true) {
                tc = table.getColumnModel().getColumn(j);
                component = table.getDefaultRenderer(table.getColumnClass(j))
                .getTableCellRendererComponent(table, headerName[i], false, false, 0, j);
                cw = component.getPreferredSize().width + table.getColumnModel().getColumnMargin() * 2 + 5;
                for(int r=0; r < table.getRowCount(); r++) {
                    component = table.getDefaultRenderer(table.getColumnClass(j))
                    .getTableCellRendererComponent(table, table.getValueAt(r, j), false, false, 0, j);
                    tmp = component.getPreferredSize().width + table.getColumnModel().getColumnMargin() * 2 + 5;
                    cw = tmp > cw ? tmp : cw;
                }
                j++;
                tw += cw;
            }
            if(tc != null)
                tc.setPreferredWidth(cw);
        }
        for(int r=0; r < table.getRowCount(); r++) {
            th += table.getRowHeight(r);
        }
        table.setPreferredScrollableViewportSize(new java.awt.Dimension(tw, th));
        pack();
        pack(); // bug??
        
    }
    
    void updateDisp() {
        int idx = -1;
        int idx2 = 0;
        String [] res;
        int j;
        libastro.SolSys[ephemObject.EARTH].calculatePosition();
        for(int i=0; i < dispRow.length; i++) {
            if(dispRow[i] == false)
                continue;
            idx++;
            res = getRow(i);
            idx2 = 0;
            for(j=0; j < colNum; j++) {
                // if(dispCol[j] == true)
                model.setValueAt(res[j], idx, j);
            }
        }
    }
    
    String [] getRow(int i) {
        final java.text.DecimalFormat percFormatter = new java.text.DecimalFormat("0.0");
        ephemObject obj = libastro.SolSys[i];
        libastro.updateAll(i);
        String []  ret = new String[colNum];
        int j = 0;
        if(dispCol[0] == true)
            ret[j++] = obj.toString();
        if(dispCol[1] == true)
            ret[j++] = obj.getRAString();
        if(dispCol[2] == true)
            ret[j++] = obj.getDecString();
        if(dispCol[3] == true)
            ret[j++] = obj.getAzString();
        if(dispCol[4] == true)
            ret[j++] = obj.getAltString();
        if(dispCol[5] == true)
            ret[j++] = obj.getEcLonString();
        if(dispCol[6] == true)
            ret[j++] = obj.getEcLatString();
        if(dispCol[7] == true)
            ret[j++] = obj.riseTimeString();
        if(dispCol[8] == true)
            ret[j++] = obj.getRiseAzString();
        if(dispCol[9] == true)
            ret[j++] = obj.transTimeString();
        if(dispCol[10] == true)
            ret[j++] = obj.getTransAltString();
        if(dispCol[11] == true)
            ret[j++] = obj.setTimeString();
        if(dispCol[12] == true)
            ret[j++] = obj.getSetAzString();
        if(dispCol[13] == true)
            ret[j++] = obj.TimeUpString();
        if(dispCol[14] == true)
            ret[j++] = obj.getEarthDistanceString();
        if(dispCol[15] == true)
            ret[j++] = obj.getSunDistanceString();
        if(dispCol[16] == true)
            ret[j++] = libastro.getElongationString(i);
        if(dispCol[17] == true) {
            double ph = obj.getPhase();
            if(!Double.isNaN(ph))
                ret[j++] = percFormatter.format(ph * 100.0);
            else
                ret[j++]="";
        }
        if(dispCol[18] == true) {
            double mag = obj.getMagnitude();
            if(!Double.isNaN(mag))
                ret[j++] = percFormatter.format(mag);
            else
                ret[j++]="";
        }
        return ret;
    }
    
    @Override
    public int print(java.awt.Graphics g, java.awt.print.PageFormat pageFormat, int pageIndex) throws java.awt.print.PrinterException {
        java.awt.Graphics2D  g2 = (java.awt.Graphics2D) g;
        g2.setColor(Color.black);
        int fontHeight=g2.getFontMetrics().getHeight();
        
        double pageWidth = pageFormat.getImageableWidth();
        double tableWidth = (double) table.getColumnModel().getTotalColumnWidth();
        double scale = 1;
        if (tableWidth >= pageWidth) {
            scale =  pageWidth / tableWidth;
        }
        
        double headerHeightOnPage = table.getTableHeader().getHeight();
        
        if(pageIndex >= 1) {
            return NO_SUCH_PAGE;
        }
        g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        String str =  libastro.userData.name + "    " + libastro.userData.latString()
        + " " + libastro.userData.lonString();
        g2.drawString(str, 10, fontHeight);
        str = libastro.timeData.getLocalDateString() + " " + libastro.timeData.getLocalTimeString()
        + " " + libastro.getCurrentTZName() + "  " + libastro.timeData.getUTTime() + " UT";
        g2.drawString(str, 10, fontHeight*2 + 5);
        
        g2.translate(0.0, fontHeight*2 + 25.0);
        g2.scale(scale,scale);
        table.getTableHeader().paint(g2);
        g2.translate(0f, headerHeightOnPage);
        table.paint(g2);
        
        
        return java.awt.print.Printable.PAGE_EXISTS;
    }
    
    public void SetTimeMode(String disp) {
        TimeModeLabel.setText("TZ: " + disp);
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem ColButton;
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JLabel TimeModeLabel;
    private javax.swing.JMenuItem closeMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuItem printMenuItem;
    private javax.swing.JMenuItem rowButton;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenu setupMenu;
    private javax.swing.JTable table;
    private javax.swing.JMenu toolsMenu;
    // End of variables declaration//GEN-END:variables
    
}

class selectDialog extends javax.swing.JDialog {
    
    String[] name;
    boolean[] state;
    JCheckBox[] check;
    
    public selectDialog(java.awt.Frame parent, boolean modal, String[] _name, boolean[] _state) {
        
        super(parent, modal);
        state = _state;
        
        this.setBackground(Color.white);
        JPanel root = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        JButton OkButton = new JButton();
        JButton CancelButton = new JButton();
        
        root.setBackground(Color.white);
        root.setLayout(new BorderLayout());
        root.setBorder(new javax.swing.border.EmptyBorder(10,10,5,10));
        getContentPane().add(root);
        setResizable(false);
        
        bottomPanel.setBorder(new javax.swing.border.EmptyBorder(20,10,5,10));
        bottomPanel.setLayout(new java.awt.FlowLayout());
        bottomPanel.setBackground(Color.white);
        OkButton.setText("OK");
        OkButton.setPreferredSize(null);
        bottomPanel.add(OkButton);
        OkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                OKButtonActionPerformed(evt);
            }
        });
        
        CancelButton.setText("Cancel");
        CancelButton.setPreferredSize(null);
        bottomPanel.add(CancelButton);
        CancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });
        root.add(bottomPanel, BorderLayout.SOUTH);
        
        //topPanel.setLayout(new javax.swing.BoxLayout(topPanel, javax.swing.BoxLayout.Y_AXIS));
        topPanel.setLayout(new java.awt.GridBagLayout());
        topPanel.setBorder(new javax.swing.border.LineBorder(Color.black,1));
        topPanel.setBackground(Color.white);
        
        GridBagConstraints gridBagConstraints;
        check = new JCheckBox[_name.length];
        int ii = 0;
        for(int i=0; i < _name.length; i++) {
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = ii % 2;
            gridBagConstraints.gridy = ii / 2;
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            check[i] = new JCheckBox(_name[i]);
            check[i].setSelected(_state[i]);
            check[i].setBackground(Color.white);
            topPanel.add(check[i], gridBagConstraints);
            if(_name[i].equals("Unkown"))
                check[i].setVisible(false);
            else
                ii++;
        }
        root.add(topPanel, BorderLayout.CENTER);
        pack();
    }
    
    void OKButtonActionPerformed(ActionEvent evt) {
        for(int i=0; i < state.length; i++)
            if(check[i].isSelected())
                state[i] = true;
            else
                state[i] = false;
        
        setVisible(false);
        dispose();
    }
    
    void CancelButtonActionPerformed(ActionEvent evt) {
        setVisible(false);
        dispose();
    }
    
}