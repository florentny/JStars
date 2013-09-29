/*
 * $Id: jupiterWin.java,v 1.14 2010/01/30 21:39:18 fc Exp $
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
 * Copyright (c) 2003-2004 Florent Charpin
 *
 * email: jstars@florent.us
 *
 */

package jstars.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import jstars.util.ephemObject;
import jstars.util.Jlibastro.Jdate;
import java.awt.Font;


/**
 * Main windows for Jupiter
 * @author Florent Charpin
 */
public class jupiterWin extends javax.swing.JFrame {
    
    private double NSflag = 1.0;
    private double EWflag = 1.0;
    private java.awt.Image img;
    private int s_value;
    private final int plot_step = 336;  // 14 days
    double sat[][];
    String satday[];
    
    
    private double plot[][] =new double[plot_step][4];
    
    protected class plotPanel extends javax.swing.JPanel {
        // TODO: display dates
        int w, h;
        double r;
        double t_r;
        int top_off, r_off, l_off, b_off;
        /**
         *
         * @param g
         */
        @Override
        public void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            double[] adj;
            
            java.awt.font.FontRenderContext frc = g2.getFontRenderContext();
            Font font = new Font("Dialog", Font.PLAIN, 10);
            g2.setFont(font);
            java.awt.geom.Rectangle2D bounds = g2.getFont().getStringBounds("99/99", frc);
            l_off = (int) bounds.getWidth() + 5;
            top_off = 5;
            r_off = 10;
            //l_off = 30;
            b_off = 5;
            w = getWidth() - r_off - l_off;
            h = getHeight() - top_off - b_off;
            r = (double)w / 60.0;
            t_r = (double)h / plot_step;
            
            // box
            g2.setPaint(Color.black);
            g2.drawLine(l_off, top_off, l_off, h + top_off);
            g2.drawLine(l_off + w, top_off, l_off + w, h + top_off);
            g2.drawLine(l_off, top_off, l_off + w, top_off);
            g2.drawLine(l_off, h + top_off, l_off + w, h + top_off);
            
            
            
            for(int i=0; i < plot_step; i++) {
                if((i % 24) == 0) {
                    g2.setPaint(Color.gray);
                    g2.drawLine(l_off, (int)(i * t_r) + top_off , w + l_off, (int)(i * t_r) + top_off);
                    g2.drawString(satday[(int)((plot_step-i)/24)-1], 3, (int)(i * t_r) + top_off + (int)(bounds.getHeight()/2) + (h/28));
                }
            }
            for(int z=0; z < 2; z++) {
                for(int i=1; i < plot_step; i++) {
                    if(((plot[i-1][0] < plot[i][0]) && (z==0)) || ((z==1)) && (plot[i-1][0] >= plot[i][0])) {
                        g2.setPaint(Color.red);
                        g2.drawLine((int)((w/2) + plot[i-1][0] * r) + l_off, (int)(h - ((i - 1) * t_r)) + top_off,
                        (int)((w/2) + plot[i][0] * r) + l_off, (int)(h - (i * t_r)) + top_off);
                    }
                    if(((plot[i-1][1] < plot[i][1]) && (z==0)) || ((z==1)) && (plot[i-1][1] >= plot[i][1])) {
                        g2.setPaint(Color.green);
                        g2.drawLine((int)((w/2) + plot[i-1][1] * r) + l_off, (int)(h - ((i - 1) * t_r)) + top_off,
                        (int)((w/2) + plot[i][1] * r) + l_off, (int)(h - (i * t_r)) + top_off);
                    }
                    if(((plot[i-1][2] < plot[i][2]) && (z==0)) || ((z==1)) && (plot[i-1][2] >= plot[i][2])) {
                        g2.setPaint(Color.blue);
                        g2.drawLine((int)((w/2) + plot[i-1][2] * r) + l_off, (int)(h - ((i - 1) * t_r)) + top_off,
                        (int)((w/2) + plot[i][2] * r) + l_off, (int)(h - (i * t_r)) + top_off);
                    }
                    if(((plot[i-1][3] < plot[i][3]) && (z==0)) || ((z==1)) && (plot[i-1][3] >= plot[i][3])) {
                        g2.setPaint(Color.orange);
                        g2.drawLine((int)((w/2) + plot[i-1][3] * r) + l_off, (int)(h - ((i - 1) * t_r)) + top_off,
                        (int)((w/2) + plot[i][3] * r) + l_off, (int)(h - (i * t_r)) + top_off);
                    }
                }
                if(z==0) {
                    g2.setPaint(Color.white);
                    g2.fillRect((int)((w / 2.0) - (double)r * 1.0) + l_off, top_off + 1, (int)(r * 2.0) + 1, h - 1);
                    //Jupiter's lines
                    g2.setPaint(Color.black);
                    g2.drawLine( (int)((w / 2.0) - (double)r * 1.0) + l_off , top_off , (int)((w / 2.0) - r * 1.0) + l_off, h + top_off);
                    g2.drawLine( (int)((w / 2.0) + (double)r * 1.0) + l_off , top_off , (int)((w / 2.0) + r * 1.0) + l_off, h + top_off);
                }
            }
            // legend
            g2.setPaint(Color.black);
            font = new Font("Dialog", Font.PLAIN, 10);
            g2.setFont(font);
            g2.drawString("E", l_off + 10, top_off + 13);
            g2.drawString("W", l_off + w - 15, top_off + 13);
            int leg_off = 28;
            bounds = g2.getFont().getStringBounds("Io", frc);
            g2.drawString("Io", (int)(l_off + w - bounds.getWidth() - leg_off), (int)(h + top_off - h/28*7 + bounds.getHeight()/2));
            bounds = g2.getFont().getStringBounds("Europa", frc);
            g2.drawString("Europa", (int)(l_off + w - bounds.getWidth() - leg_off), (int)(h + top_off - h/28*5 + bounds.getHeight()/2));
            bounds = g2.getFont().getStringBounds("Ganymede", frc);
            g2.drawString("Ganymede", (int)(l_off + w - bounds.getWidth() - leg_off), (int)(h + top_off - h/28*3 + bounds.getHeight()/2));
            bounds = g2.getFont().getStringBounds("Callisto", frc);
            g2.drawString("Callisto", (int)(l_off + w - bounds.getWidth() - leg_off), (int)(h + top_off - h/28*1 + bounds.getHeight()/2));
            g2.setPaint(Color.red);
            g2.drawLine((int)(l_off + w - leg_off + 4), (int)(h + top_off - h/28*7), l_off + w -5, (int)(h + top_off - h/28*7));
            g2.setPaint(Color.green);
            g2.drawLine((int)(l_off + w - leg_off + 4), (int)(h + top_off - h/28*5), l_off + w -5, (int)(h + top_off - h/28*5));
            g2.setPaint(Color.blue);
            g2.drawLine((int)(l_off + w - leg_off + 4), (int)(h + top_off - h/28*3), l_off + w -5, (int)(h + top_off - h/28*3));
            g2.setPaint(Color.orange);
            g2.drawLine((int)(l_off + w - leg_off + 4), (int)(h + top_off - h/28*1), l_off + w -5, (int)(h + top_off - h/28*1));
            
        }
    }
    
    
    protected class JmoonsPanel extends javax.swing.JPanel {
        
        int w, h;
        int satSize;
        /**
         *
         * @param g
         */
        @Override
        public void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(Color.yellow);
            w = this.getWidth();
            h = this.getHeight();
            
            // ratio to shrink jupiter
            double r = w / (s_value * 16.0);
            double jup_size = 32.0 * r;
            g2.drawImage(img, (w - (int)jup_size)/2, (h - (int)jup_size)/2, (int)jup_size, (int)jup_size, this);
            satSize = (int) Math.min(Math.max(jup_size / 13.0, 2.0), 10.0);
            //System.out.println(satSize);
            
            // Sattelites
            g2.setPaint(Color.white);
            if((Math.abs(sat[0][2]) > 1.0) || (sat[0][0] > 270.0) || (sat[0][0] < 90.0))
                g2.fill(new Arc2D.Double(pix_value_x(EWflag * sat[0][2]), pix_value_y(NSflag * sat[0][3]), satSize, satSize, 0, 360,Arc2D.PIE));
            if((Math.abs(sat[1][2]) > 1.0) || (sat[1][0] > 270.0) || (sat[1][0] < 90.0))
                g2.fill(new Arc2D.Double(pix_value_x(EWflag * sat[1][2]), pix_value_y(NSflag * sat[1][3]), satSize, satSize, 0, 360,Arc2D.PIE));
            if((Math.abs(sat[2][2]) > 1.0) || (sat[2][0] > 270.0) || (sat[2][0] < 90.0))
                g2.fill(new java.awt.geom.Arc2D.Double(pix_value_x(EWflag * sat[2][2]), pix_value_y(NSflag * sat[2][3]), satSize, satSize, 0, 360,Arc2D.PIE));
            if((Math.abs(sat[2][2]) > 1.0) || (sat[2][0] > 270.0) || (sat[2][0] < 90.0))
                g2.fill(new java.awt.geom.Arc2D.Double(pix_value_x(EWflag * sat[3][2]), pix_value_y(NSflag * sat[3][3]), satSize, satSize, 0, 360,Arc2D.PIE));
            
            int x, y;
            String s1, s2;
            if(NSflag == 1.0)
                s1 = "N";
            else
                s1 = "S";
            if(EWflag == 1.0)
                s2 = "W";
            else
                s2 = "E";
            java.awt.font.FontRenderContext frc = g2.getFontRenderContext();
            Font font = new Font("Dialog", Font.PLAIN, 12);
            g2.setFont(font);
            java.awt.geom.Rectangle2D bounds = g2.getFont().getStringBounds(s1, frc);
            java.awt.geom.Rectangle2D bounds2 = g2.getFont().getStringBounds(s2, frc);
            x =  w - (int)(bounds.getWidth()/2.0) - (int)bounds2.getWidth() - 23 - 5;
            y = (int)bounds.getHeight() + 2;
            g2.drawString(s1, x, y);
            x += bounds.getWidth() / 2;
            y += 5;
            g2.drawLine(x, y , x, y + 18);
            g2.drawLine(x, y , x + 3, y + 4);
            g2.drawLine(x, y , x - 3, y + 4);
            y += 18;
            g2.drawLine(x, y, x + 18, y);
            x += 18;
            g2.drawLine(x, y, x - 4, y + 3);
            g2.drawLine(x, y, x - 4, y - 3);
            x += 5;
            y += bounds2.getHeight() / 2;
            g2.drawString(s2, x, y);
            
            
        }
        
        /**
         *
         * @param distance
         * @return
         */
        private double pix_value_x(double distance) {
            return ((distance + (s_value / 2.0)) * w / s_value);
        }
        
        /**
         *
         * @param distance
         * @return
         */
        private double pix_value_y(double distance) {
            return ((distance + (s_value / 2.0)) * h / s_value);
        }
        
    }
    
    javax.swing.JToggleButton jupiterButton;
    jstars.util.Jjupiter Jupiter;
    jstars.util.Jlibastro libastro;
    
    /**
     * Creates new form jupiterWin
     * @param _libastro
     * @param Button
     */
    public jupiterWin(jstars.util.Jlibastro _libastro, javax.swing.JToggleButton Button) {
        jupiterButton = Button;
        Jupiter = ( jstars.util.Jjupiter) _libastro.SolSys[ephemObject.JUPITER];
        java.net.URL picURL = getClass().getResource("/jstars/images/jupiter2.jpg");
        img = java.awt.Toolkit.getDefaultToolkit().getImage(picURL);
        java.awt.MediaTracker mt = new java.awt.MediaTracker(this);
        mt.addImage(img, 0);
        try {
            mt.waitForAll();
        } catch(InterruptedException exp) {
            exp.printStackTrace();
        }
        initComponents();
        satTable.getColumn("Satellite").setPreferredWidth(110);
        satTable.getColumn("X (+W)").setPreferredWidth(60);
        satTable.getColumn("Y (+N)").setPreferredWidth(60);
        satTable.getColumn("Z (+F)").setPreferredWidth(60);
        
        int th, tw;
        th = 0; tw = 0;
        for(int r=0; r < this.satTable.getRowCount(); r++) {
            th += satTable.getRowHeight(r);
        }
        //th += satTable.getIntercellSpacing().height * (satTable.getRowCount() +1);
        satTable.setPreferredScrollableViewportSize(new java.awt.Dimension(365, th));
        //int th1 = satTable.getColumn("Satellite").getHeaderRenderer().getTableCellRendererComponent(satTable, "Satellite", false, false, 0,0).getPreferredSize().width;
        th += 16;
        jPanel3.setMinimumSize(new java.awt.Dimension(365, th));
        
        pack();
        s_value = 54;
        libastro = _libastro;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        satPlotDialog = new javax.swing.JDialog(this, false);
        plotPanel = new plotPanel();
        dialogPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        printButton = new javax.swing.JButton();
        topPanel = new javax.swing.JPanel();
        headerLabel = new javax.swing.JLabel();
        dateRangeLabel = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        satdisp = new JmoonsPanel();
        jSlider2 = new javax.swing.JSlider();
        jPanel2 = new javax.swing.JPanel();
        RALabel = new javax.swing.JLabel();
        RAValue = new javax.swing.JLabel();
        decValue = new javax.swing.JLabel();
        DecLabel = new javax.swing.JLabel();
        AzValue = new javax.swing.JLabel();
        AzLabel = new javax.swing.JLabel();
        AltValue = new javax.swing.JLabel();
        AltLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        satTable = new javax.swing.JTable();
        TimeModeLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        flipNSMenuItem = new javax.swing.JMenuItem();
        flipEWMenuItem1 = new javax.swing.JMenuItem();
        closeMenuItem = new javax.swing.JMenuItem();
        toolMenu = new javax.swing.JMenu();
        plotMenuItem = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        satPlotDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        satPlotDialog.setTitle("Plot of Jupiter's Satellites");
        satPlotDialog.setModal(true);
        plotPanel.setBackground(new java.awt.Color(255, 255, 255));
        plotPanel.setMinimumSize(new java.awt.Dimension(200, 300));
        plotPanel.setPreferredSize(new java.awt.Dimension(300, 450));
        satPlotDialog.getContentPane().add(plotPanel, java.awt.BorderLayout.CENTER);

        okButton.setText("Close");
        okButton.setFocusable(false);
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        dialogPanel.add(okButton);

        printButton.setText("Print");
        printButton.setFocusable(false);
        printButton.setEnabled(false);
        dialogPanel.add(printButton);

        satPlotDialog.getContentPane().add(dialogPanel, java.awt.BorderLayout.SOUTH);

        topPanel.setLayout(new java.awt.GridLayout(2, 1));

        topPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerLabel.setFont(new java.awt.Font("Dialog", 1, 24));
        headerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLabel.setText("Jupiter's moons");
        headerLabel.setAlignmentY(0.0F);
        headerLabel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(3, 1, 5, 1)));
        topPanel.add(headerLabel);

        dateRangeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dateRangeLabel.setText("11/19/03 - 22/19/03");
        dateRangeLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        topPanel.add(dateRangeLabel);

        satPlotDialog.getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);

        setTitle("Jupiter");
        setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(getClass().getResource("/jstars/images/jupiter.gif")));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        mainPanel.setLayout(new java.awt.BorderLayout());

        mainPanel.setBackground(new java.awt.Color(0, 0, 0));
        mainPanel.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setBackground(new java.awt.Color(240, 210, 144));
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Jupiter");
        jLabel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255)));
        jLabel1.setOpaque(true);
        mainPanel.add(jLabel1, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setMinimumSize(new java.awt.Dimension(16, 120));
        jPanel1.setPreferredSize(new java.awt.Dimension(350, 170));
        satdisp.setBackground(new java.awt.Color(0, 0, 0));
        satdisp.setForeground(new java.awt.Color(255, 255, 255));
        satdisp.setMinimumSize(null);
        satdisp.setPreferredSize(null);
        jPanel1.add(satdisp, java.awt.BorderLayout.CENTER);

        jSlider2.setMaximum(54);
        jSlider2.setMinimum(6);
        jSlider2.setOrientation(javax.swing.JSlider.VERTICAL);
        jSlider2.setToolTipText("Zoom in/out");
        jSlider2.setValue(54);
        jSlider2.setFocusable(false);
        jSlider2.setInverted(true);
        jSlider2.setMaximumSize(null);
        jSlider2.setMinimumSize(null);
        jSlider2.setPreferredSize(null);
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });

        jPanel1.add(jSlider2, java.awt.BorderLayout.WEST);

        mainPanel.add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        RALabel.setFont(new java.awt.Font("Dialog", 0, 12));
        RALabel.setText("RA:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 0, 8);
        jPanel2.add(RALabel, gridBagConstraints);

        RAValue.setFont(new java.awt.Font("Dialog", 0, 12));
        RAValue.setText("00:00:00.00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 10);
        jPanel2.add(RAValue, gridBagConstraints);

        decValue.setFont(new java.awt.Font("Dialog", 0, 12));
        decValue.setText("00:00:00.00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanel2.add(decValue, gridBagConstraints);

        DecLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        DecLabel.setText("Dec:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 8);
        jPanel2.add(DecLabel, gridBagConstraints);

        AzValue.setFont(new java.awt.Font("Dialog", 0, 12));
        AzValue.setText("00:00:00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 10);
        jPanel2.add(AzValue, gridBagConstraints);

        AzLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        AzLabel.setText("Azimuth:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 8);
        jPanel2.add(AzLabel, gridBagConstraints);

        AltValue.setFont(new java.awt.Font("Dialog", 0, 12));
        AltValue.setText("00:00:00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanel2.add(AltValue, gridBagConstraints);

        AltLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        AltLabel.setText("Altitude");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 8);
        jPanel2.add(AltLabel, gridBagConstraints);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel3.setMinimumSize(new java.awt.Dimension(103, 88));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setMaximumSize(null);
        satTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Io (I)", new Double(0.0), new Double(0.0), new Double(0.0), null},
                {"Europa (II)", new Double(0.0), new Double(0.0), new Double(0.0), null},
                {"Ganymede(III)", new Double(0.0), new Double(0.0), new Double(0.0), null},
                {"Callisto (IV)", new Double(0.0), new Double(0.0), new Double(0.0), null}
            },
            new String [] {
                "Satellite", "X (+W)", "Y (+N)", "Z (+F)", "Radius"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        satTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        satTable.setFocusable(false);
        satTable.setMaximumSize(null);
        satTable.setMinimumSize(new java.awt.Dimension(150, 150));
        satTable.setPreferredScrollableViewportSize(new java.awt.Dimension(300, 80));
        satTable.setPreferredSize(null);
        jScrollPane1.setViewportView(satTable);

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 15, 5, 5);
        jPanel2.add(jPanel3, gridBagConstraints);

        TimeModeLabel.setBackground(new java.awt.Color(255, 255, 255));
        TimeModeLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        TimeModeLabel.setText("Time Mode: UTC");
        TimeModeLabel.setBorder(new javax.swing.border.EtchedBorder());
        TimeModeLabel.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        jPanel2.add(TimeModeLabel, gridBagConstraints);

        mainPanel.add(jPanel2, java.awt.BorderLayout.SOUTH);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        fileMenu.setText("File");
        flipNSMenuItem.setText("Flip N/S");
        flipNSMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flipNSMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(flipNSMenuItem);

        flipEWMenuItem1.setText("Filp E/W");
        flipEWMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flipEWMenuItem1ActionPerformed(evt);
            }
        });

        fileMenu.add(flipEWMenuItem1);

        closeMenuItem.setText("Close");
        closeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(closeMenuItem);

        menuBar.add(fileMenu);

        toolMenu.setText("Plot");
        plotMenuItem.setText("Plot");
        plotMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plotMenuItemActionPerformed(evt);
            }
        });

        toolMenu.add(plotMenuItem);

        jMenuItem1.setText("Setup");
        jMenuItem1.setEnabled(false);
        toolMenu.add(jMenuItem1);

        menuBar.add(toolMenu);

        setJMenuBar(menuBar);

    }//GEN-END:initComponents
    
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        satPlotDialog.dispose();
    }//GEN-LAST:event_okButtonActionPerformed
    
    /**
     *
     * @param evt
     */
    private void flipEWMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flipEWMenuItem1ActionPerformed
        if(EWflag < 0.0)
            EWflag = 1.0;
        else
            EWflag= -1.0;
        satdisp.repaint();
    }//GEN-LAST:event_flipEWMenuItem1ActionPerformed
    
    /**
     *
     * @param evt
     */
    private void flipNSMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flipNSMenuItemActionPerformed
        if(NSflag < 0.0)
            NSflag = 1.0;
        else
            NSflag= -1.0;
        satdisp.repaint();
    }//GEN-LAST:event_flipNSMenuItemActionPerformed
    
    /**
     *
     * @param evt
     */
    private void plotMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plotMenuItemActionPerformed
        // Satellites for next two weeks
        
        boolean savedispLT = libastro.timeData.isDispLT();
        Jdate date_save = libastro.timeData.copyLocalTime();
        Jdate wdate = libastro.timeData.copyLocalTime();
        wdate.hour = 0;
        wdate.min =0;
        wdate.sec = 0;
        Jdate step = new Jdate();
        step.hour = 1;
        step.day = 0;
        satday = new String[14];
        for(int i=plot_step - 1; i >= 0; i--) {
            libastro.setUserDate(wdate, true);
            libastro.updateAll(ephemObject.JUPITER);
            double satt[][] = Jupiter.satellite();
            plot[i][0] = 1.0 * satt[0][2];
            plot[i][1] = 1.0 * satt[1][2];
            plot[i][2] = 1.0 * satt[2][2];
            plot[i][3] = 1.0 * satt[3][2];
            if((i % 24) == 0) {
                satday[(int)i/24] = wdate.dispDateMMDD();
            }
            if(i > 0)
                wdate.add(step);
        }
        libastro.setUserDate(date_save, false);
        libastro.updateAll(ephemObject.JUPITER);
        this.dateRangeLabel.setText(date_save.dispDateString() + " - " + wdate.dispDateString());
        satPlotDialog.setModal(false);
        satPlotDialog.pack();
        satPlotDialog.setVisible(true);
        
    }//GEN-LAST:event_plotMenuItemActionPerformed
    
    /**
     *
     * @param evt
     */
    private void jSlider2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider2StateChanged
        javax.swing.JSlider s = (javax.swing.JSlider) evt.getSource();
        s_value = s.getValue();
        satdisp.repaint();
        
    }//GEN-LAST:event_jSlider2StateChanged
    
    /**
     *
     * @param evt
     */
    private void closeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeMenuItemActionPerformed
        jupiterButton.setSelected(false);
        satPlotDialog.dispose();
        dispose();
    }//GEN-LAST:event_closeMenuItemActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        jupiterButton.setSelected(false);
        satPlotDialog.dispose();
        dispose();
    }//GEN-LAST:event_exitForm
    
    void updateDisp() {
        RAValue.setText(Jupiter.getRAString());
        decValue.setText(Jupiter.getDecString());
        AzValue.setText(Jupiter.getAzString());
        AltValue.setText(Jupiter.getAltString());
        sat = Jupiter.satellite();
        for(int i=0; i < 4; i++) {
            satTable.setValueAt(new java.lang.Double(sat[i][2]), i, 1);
            satTable.setValueAt(new Double(sat[i][3]), i, 2);
            satTable.setValueAt(new Double(sat[i][1]), i, 4);
            if((sat[i][0] > 270.0) || (sat[i][0] < 90.0))
                satTable.setValueAt(new Double(Math.sqrt(Math.abs(Math.pow(sat[i][1], 2) - Math.pow(sat[i][2],2)))), i, 3);
            else
                satTable.setValueAt(new Double(-1.0 * Math.sqrt(Math.abs(Math.pow(sat[i][1], 2) - Math.pow(sat[i][2],2)))), i, 3);
        }
        satdisp.repaint();
    }
    
    public void SetTimeMode(String disp) {
        TimeModeLabel.setText("TZ: " + disp);
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AltLabel;
    private javax.swing.JLabel AltValue;
    private javax.swing.JLabel AzLabel;
    private javax.swing.JLabel AzValue;
    private javax.swing.JLabel DecLabel;
    private javax.swing.JLabel RALabel;
    private javax.swing.JLabel RAValue;
    private javax.swing.JLabel TimeModeLabel;
    private javax.swing.JMenuItem closeMenuItem;
    private javax.swing.JLabel dateRangeLabel;
    private javax.swing.JLabel decValue;
    private javax.swing.JPanel dialogPanel;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem flipEWMenuItem1;
    private javax.swing.JMenuItem flipNSMenuItem;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton okButton;
    private javax.swing.JMenuItem plotMenuItem;
    private javax.swing.JPanel plotPanel;
    private javax.swing.JButton printButton;
    private javax.swing.JDialog satPlotDialog;
    private javax.swing.JTable satTable;
    private javax.swing.JPanel satdisp;
    private javax.swing.JMenu toolMenu;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
    
}
