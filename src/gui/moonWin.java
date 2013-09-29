/*
 * $Id: moonWin.java,v 1.8 2010/01/30 21:39:18 fc Exp $
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

import java.awt.Graphics2D;
import java.awt.geom.Area;

/**
 *
 * @author  fc
 */

public class moonWin extends javax.swing.JFrame {
    
    protected class phaseLabel extends javax.swing.JLabel {
        int flag;
        public phaseLabel(int flag) {
            super();
            this.flag = flag;
        }
        
        @Override
        public void paintComponent(java.awt.Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(img, 1,1,21,21,this);
            g2.setPaint(java.awt.Color.black);
            //g2.draw(new Ellipse2D.Double(1, 1, 20,20));
            if(flag == 2) {
                g2.fill(new java.awt.geom.Arc2D.Double(1, 1, 20, 20, 270, 180, java.awt.geom.Arc2D.OPEN));
            }
            else if(flag == 0) {
                g2.fill(new java.awt.geom.Arc2D.Double(1, 1, 20, 20, 90, 180, java.awt.geom.Arc2D.OPEN));
            }
            else if(flag == 3) {
                g2.fill(new java.awt.geom.Arc2D.Double(1, 1, 20, 20, 0, 360, java.awt.geom.Arc2D.OPEN));
            }
        }
        
    }
    
    protected class JmoonPanel extends javax.swing.JPanel {
        
        java.awt.GradientPaint gp;
        java.awt.Color color = new java.awt.Color(220,220,220);
        @Override
        public void paintComponent(java.awt.Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();
            gp = new java.awt.GradientPaint(0,0, java.awt.Color.white, w,h,color , true);
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);
        }
        @Override
        public boolean isOpaque() {
            return true;
        }
        
    }
    
    jstars.util.Jmoon TheMoon;
    jstars.util.Jsun TheSun;
    jstars.util.Jlibastro libastro;
    javax.swing.JToggleButton moonButton;
    java.awt.Image img;
    int w, h;
    
    class PhasePanel extends javax.swing.JPanel {
        Graphics2D g2;
        int x, y, start;
        double [] ret;
        int angle;
        
        @Override
        public void paintComponent(java.awt.Graphics g) {
            
            g2 = (Graphics2D) g;
            g2.drawImage(img, 10,10,w,h,this);
            
            ret = TheMoon.illuminatedDisk();
            if(ret[1] > 180) {
                angle = 270;
                java.awt.geom.Ellipse2D.Double dark = new java.awt.geom.Ellipse2D.Double();
                dark.setFrame(15, 15, w - 10 , h - 10);
                Area st1 = new Area(dark);
                java.awt.geom.Rectangle2D.Double rect = new java.awt.geom.Rectangle2D.Double();
                rect.setFrame(w/2 + 10,  15, w , h);
                Area st3 = new Area(rect);
                st1.subtract(st3);
                if(ret[0] >= 0.5) {
                    int cresent = (int) ((1.0 -  ret[0]) * 135);
                    dark.setFrame(15 + cresent, 15, w - 10 - cresent *2 , h - 10);
                    Area st2 = new Area(dark);
                    st1.subtract(st2);
                }
                else {
                    int cresent = (int) (( ret[0]) * 135);
                    dark.setFrame(15 + cresent, 15, w - 10 - cresent *2 , h - 10);
                    Area st2 = new Area(dark);
                    st1.add(st2);
                }
                g2.fill(st1);
            }
            else {
                angle = 90;
                java.awt.geom.Ellipse2D.Double dark = new java.awt.geom.Ellipse2D.Double();
                dark.setFrame(15, 15, w - 10 , h - 10);
                Area st1 = new Area(dark);
                java.awt.geom.Rectangle2D.Double rect = new java.awt.geom.Rectangle2D.Double();
                rect.setFrame(10, 10, w/2 , h);
                Area st3 = new Area(rect);
                st1.subtract(st3);
                if(ret[0] >= 0.5) {
                    int cresent = (int) ((1.0 -  ret[0]) * 135);
                    dark.setFrame(15 + cresent, 15, w - 10 - cresent *2 , h - 10);
                    Area st2 = new Area(dark);
                    st1.subtract(st2);
                }
                else {
                    int cresent = (int) (( ret[0]) * 135);
                    dark.setFrame(15 + cresent, 15, w - 10 - cresent *2 , h - 10);
                    Area st2 = new Area(dark);
                    st1.add(st2);
                }
                g2.fill(st1);
            }
        }
    }
    
    /** Creates new form moonWin */
    public moonWin(jstars.util.Jlibastro _libastro, javax.swing.JToggleButton Button) {
        libastro = _libastro;
        TheMoon = (jstars.util.Jmoon) _libastro.SolSys[jstars.util.ephemObject.MOON];
        TheSun = (jstars.util.Jsun) _libastro.SolSys[jstars.util.ephemObject.SUN];
        moonButton = Button;
        java.net.URL picURL = getClass().getResource("/jstars/images/fullmoon.png");
        img = java.awt.Toolkit.getDefaultToolkit().getImage(picURL);
        java.awt.MediaTracker mt = new java.awt.MediaTracker(this);
        mt.addImage(img, 0);
        try {
            mt.waitForAll();
        } catch(InterruptedException exp) {
            exp.printStackTrace();
        }
        w = img.getWidth(this);
        h = img.getHeight(this);

        initComponents();
        pack();
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        moonPanel = new JmoonPanel();
        titleLabel = new javax.swing.JLabel();
        RALabel = new javax.swing.JLabel();
        RAValue = new javax.swing.JLabel();
        decValue = new javax.swing.JLabel();
        DecLabel = new javax.swing.JLabel();
        AzValue = new javax.swing.JLabel();
        AzLabel = new javax.swing.JLabel();
        AltValue = new javax.swing.JLabel();
        AltLabel = new javax.swing.JLabel();
        TimeModeLabel = new javax.swing.JLabel();
        riseLabel = new javax.swing.JLabel();
        riseValue = new javax.swing.JLabel();
        setlabel = new javax.swing.JLabel();
        setValue = new javax.swing.JLabel();
        riseAzLabel = new javax.swing.JLabel();
        riseAZ = new javax.swing.JLabel();
        setAzLabel = new javax.swing.JLabel();
        setAz = new javax.swing.JLabel();
        transLabel = new javax.swing.JLabel();
        transValue = new javax.swing.JLabel();
        transAltLabel = new javax.swing.JLabel();
        transAlt = new javax.swing.JLabel();
        eclonLabel = new javax.swing.JLabel();
        eclonValue = new javax.swing.JLabel();
        eclatlabel = new javax.swing.JLabel();
        eclatValue = new javax.swing.JLabel();
        distLabel = new javax.swing.JLabel();
        distValue = new javax.swing.JLabel();
        phasePanel = new javax.swing.JPanel();
        phasepicPanel = new PhasePanel();
        phaseDatePanel = new javax.swing.JPanel();
        FQsymbol = new phaseLabel(0);
        FQLabel = new javax.swing.JLabel();
        FMsymbol = new phaseLabel(1);
        FMLabel = new javax.swing.JLabel();
        LQsymbol = new phaseLabel(2);
        LQLabel = new javax.swing.JLabel();
        NMsymbol = new phaseLabel(3);
        NMLabel = new javax.swing.JLabel();
        FQLabel1 = new javax.swing.JLabel();
        FMLabel1 = new javax.swing.JLabel();
        LQLabel1 = new javax.swing.JLabel();
        NMLabel1 = new javax.swing.JLabel();
        moonMenu = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        closeMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Moon");
        setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(getClass().getResource("/jstars/images/moon.gif"))
        );
        setName("Moon");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        moonPanel.setLayout(new java.awt.GridBagLayout());

        moonPanel.setBackground(java.awt.Color.white);
        titleLabel.setBackground(new java.awt.Color(255, 255, 255));
        titleLabel.setFont(new java.awt.Font("Dialog", 1, 18));
        titleLabel.setForeground(new java.awt.Color(51, 51, 51));
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Moon");
        titleLabel.setBorder(new javax.swing.border.EtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 15, 5);
        moonPanel.add(titleLabel, gridBagConstraints);

        RALabel.setFont(new java.awt.Font("Dialog", 0, 12));
        RALabel.setText("RA:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 8);
        moonPanel.add(RALabel, gridBagConstraints);

        RAValue.setFont(new java.awt.Font("Dialog", 0, 12));
        RAValue.setText("00:00:00.00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        moonPanel.add(RAValue, gridBagConstraints);

        decValue.setFont(new java.awt.Font("Dialog", 0, 12));
        decValue.setText("00:00:00.00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 10);
        moonPanel.add(decValue, gridBagConstraints);

        DecLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        DecLabel.setText("Declination:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 10, 8);
        moonPanel.add(DecLabel, gridBagConstraints);

        AzValue.setFont(new java.awt.Font("Dialog", 0, 12));
        AzValue.setText("00:00:00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        moonPanel.add(AzValue, gridBagConstraints);

        AzLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        AzLabel.setText("Azimuth:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 8);
        moonPanel.add(AzLabel, gridBagConstraints);

        AltValue.setFont(new java.awt.Font("Dialog", 0, 12));
        AltValue.setText("00:00:00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 10);
        moonPanel.add(AltValue, gridBagConstraints);

        AltLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        AltLabel.setText("Altitude");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 7, 8);
        moonPanel.add(AltLabel, gridBagConstraints);

        TimeModeLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        TimeModeLabel.setText("Time Mode: UTC");
        TimeModeLabel.setBorder(new javax.swing.border.EtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        moonPanel.add(TimeModeLabel, gridBagConstraints);

        riseLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        riseLabel.setText("Rise:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 8);
        moonPanel.add(riseLabel, gridBagConstraints);

        riseValue.setFont(new java.awt.Font("Dialog", 0, 12));
        riseValue.setText("00:00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        moonPanel.add(riseValue, gridBagConstraints);

        setlabel.setFont(new java.awt.Font("Dialog", 0, 12));
        setlabel.setText("Set:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 8);
        moonPanel.add(setlabel, gridBagConstraints);

        setValue.setFont(new java.awt.Font("Dialog", 0, 12));
        setValue.setText("00:00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        moonPanel.add(setValue, gridBagConstraints);

        riseAzLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        riseAzLabel.setText("Az:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        moonPanel.add(riseAzLabel, gridBagConstraints);

        riseAZ.setFont(new java.awt.Font("Dialog", 0, 12));
        riseAZ.setText("000:00:00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        moonPanel.add(riseAZ, gridBagConstraints);

        setAzLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        setAzLabel.setText("Az:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        moonPanel.add(setAzLabel, gridBagConstraints);

        setAz.setFont(new java.awt.Font("Dialog", 0, 12));
        setAz.setText("000:00:00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        moonPanel.add(setAz, gridBagConstraints);

        transLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        transLabel.setText("Transit:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 8);
        moonPanel.add(transLabel, gridBagConstraints);

        transValue.setFont(new java.awt.Font("Dialog", 0, 12));
        transValue.setText("00:00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 10);
        moonPanel.add(transValue, gridBagConstraints);

        transAltLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        transAltLabel.setText("Alt:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 3);
        moonPanel.add(transAltLabel, gridBagConstraints);

        transAlt.setFont(new java.awt.Font("Dialog", 0, 12));
        transAlt.setText("+00:00:00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        moonPanel.add(transAlt, gridBagConstraints);

        eclonLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        eclonLabel.setText("Ecliptic  Longitude:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        moonPanel.add(eclonLabel, gridBagConstraints);

        eclonValue.setFont(new java.awt.Font("Dialog", 0, 12));
        eclonValue.setText("000:00:00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        moonPanel.add(eclonValue, gridBagConstraints);

        eclatlabel.setFont(new java.awt.Font("Dialog", 0, 12));
        eclatlabel.setText("Ecliptic Latitude:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        moonPanel.add(eclatlabel, gridBagConstraints);

        eclatValue.setFont(new java.awt.Font("Dialog", 0, 12));
        eclatValue.setText("+00:00:00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        moonPanel.add(eclatValue, gridBagConstraints);

        distLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        distLabel.setText("Distance:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 0, 0);
        moonPanel.add(distLabel, gridBagConstraints);

        distValue.setFont(new java.awt.Font("Dialog", 0, 12));
        distValue.setText("380000 km");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        moonPanel.add(distValue, gridBagConstraints);

        phasePanel.setLayout(new java.awt.BorderLayout());

        phasePanel.setOpaque(false);
        phasepicPanel.setOpaque(false);
        phasepicPanel.setPreferredSize(new java.awt.Dimension(w+20, h+20));
        phasepicPanel.setMinimumSize(new java.awt.Dimension(w+20, h+20));
        phasepicPanel.setSize(w+20, h+20);
        phasePanel.add(phasepicPanel, java.awt.BorderLayout.CENTER);

        phaseDatePanel.setLayout(new java.awt.GridBagLayout());

        phaseDatePanel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(3, 3, 3, 5)), new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true)));
        phaseDatePanel.setOpaque(false);
        FQsymbol.setFont(new java.awt.Font("Dialog", 0, 10));
        FQsymbol.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        FQsymbol.setMaximumSize(new java.awt.Dimension(22, 22));
        FQsymbol.setMinimumSize(new java.awt.Dimension(22, 22));
        FQsymbol.setPreferredSize(new java.awt.Dimension(22, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 8, 5);
        phaseDatePanel.add(FQsymbol, gridBagConstraints);

        FQLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        FQLabel.setText("13:56");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        phaseDatePanel.add(FQLabel, gridBagConstraints);

        FMsymbol.setFont(new java.awt.Font("Dialog", 0, 10));
        FMsymbol.setMaximumSize(new java.awt.Dimension(22, 22));
        FMsymbol.setMinimumSize(new java.awt.Dimension(22, 22));
        FMsymbol.setPreferredSize(new java.awt.Dimension(22, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 8, 5);
        phaseDatePanel.add(FMsymbol, gridBagConstraints);

        FMLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        FMLabel.setText("13:56");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        phaseDatePanel.add(FMLabel, gridBagConstraints);

        LQsymbol.setFont(new java.awt.Font("Dialog", 0, 10));
        LQsymbol.setMaximumSize(new java.awt.Dimension(22, 22));
        LQsymbol.setMinimumSize(new java.awt.Dimension(22, 22));
        LQsymbol.setPreferredSize(new java.awt.Dimension(22, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 8, 5);
        phaseDatePanel.add(LQsymbol, gridBagConstraints);

        LQLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        LQLabel.setText("13:56");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        phaseDatePanel.add(LQLabel, gridBagConstraints);

        NMsymbol.setFont(new java.awt.Font("Dialog", 0, 10));
        NMsymbol.setMaximumSize(new java.awt.Dimension(22, 22));
        NMsymbol.setMinimumSize(new java.awt.Dimension(22, 22));
        NMsymbol.setPreferredSize(new java.awt.Dimension(22, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 8, 5);
        phaseDatePanel.add(NMsymbol, gridBagConstraints);

        NMLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        NMLabel.setText("13:56");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        phaseDatePanel.add(NMLabel, gridBagConstraints);

        FQLabel1.setFont(new java.awt.Font("Dialog", 0, 10));
        FQLabel1.setText("16/13/03");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        phaseDatePanel.add(FQLabel1, gridBagConstraints);

        FMLabel1.setFont(new java.awt.Font("Dialog", 0, 10));
        FMLabel1.setText("6/13/03");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        phaseDatePanel.add(FMLabel1, gridBagConstraints);

        LQLabel1.setFont(new java.awt.Font("Dialog", 0, 10));
        LQLabel1.setText("6/13/03");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        phaseDatePanel.add(LQLabel1, gridBagConstraints);

        NMLabel1.setFont(new java.awt.Font("Dialog", 0, 10));
        NMLabel1.setText("6/13/03");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        phaseDatePanel.add(NMLabel1, gridBagConstraints);

        phasePanel.add(phaseDatePanel, java.awt.BorderLayout.EAST);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        moonPanel.add(phasePanel, gridBagConstraints);

        getContentPane().add(moonPanel, java.awt.BorderLayout.CENTER);

        jMenu1.setText("File");
        closeMenuItem.setText("close");
        closeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeMenuItemActionPerformed(evt);
            }
        });

        jMenu1.add(closeMenuItem);

        moonMenu.add(jMenu1);

        setJMenuBar(moonMenu);

    }//GEN-END:initComponents
    
    private void closeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeMenuItemActionPerformed
        moonButton.setSelected(false);
        dispose();
    }//GEN-LAST:event_closeMenuItemActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        moonButton.setSelected(false);
        dispose();
    }//GEN-LAST:event_exitForm
    
    void updateDisp() {
        RAValue.setText(TheMoon.getRAString());
        decValue.setText(TheMoon.getDecString());
        AltValue.setText(TheMoon.getAltString());
        AzValue.setText(TheMoon.getAzString());
        riseValue.setText(TheMoon.riseTimeString());
        setValue.setText(TheMoon.setTimeString());
        transValue.setText(TheMoon.transTimeString());
        riseAZ.setText(TheMoon.getRiseAzString());
        setAz.setText(TheMoon.getSetAzString());
        transAlt.setText(TheMoon.getTransAltString());
        eclonValue.setText(TheMoon.getEcLonString());
        eclatValue.setText(TheMoon.getEcLatString());
        distValue.setText(String.valueOf(TheMoon.earthDistance) + " km");
        
        double [] phases = TheMoon.getPhases();
        NMLabel1.setText(libastro.fromJD(phases[0]).dispDateMMDD());
        FQLabel1.setText(libastro.fromJD(phases[1]).dispDateMMDD());
        FMLabel1.setText(libastro.fromJD(phases[2]).dispDateMMDD());
        LQLabel1.setText(libastro.fromJD(phases[3]).dispDateMMDD());
        
        NMLabel.setText(libastro.fromJD(phases[0]).dispTimeHHMM(false, this.libastro.timeData.isHourAMPM()));
        FQLabel.setText(libastro.fromJD(phases[1]).dispTimeHHMM(false, this.libastro.timeData.isHourAMPM()));
        FMLabel.setText(libastro.fromJD(phases[2]).dispTimeHHMM(false, this.libastro.timeData.isHourAMPM()));
        LQLabel.setText(libastro.fromJD(phases[3]).dispTimeHHMM(false, this.libastro.timeData.isHourAMPM()));
        
        phasepicPanel.repaint();
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
    private javax.swing.JLabel FMLabel;
    private javax.swing.JLabel FMLabel1;
    private javax.swing.JLabel FMsymbol;
    private javax.swing.JLabel FQLabel;
    private javax.swing.JLabel FQLabel1;
    private javax.swing.JLabel FQsymbol;
    private javax.swing.JLabel LQLabel;
    private javax.swing.JLabel LQLabel1;
    private javax.swing.JLabel LQsymbol;
    private javax.swing.JLabel NMLabel;
    private javax.swing.JLabel NMLabel1;
    private javax.swing.JLabel NMsymbol;
    private javax.swing.JLabel RALabel;
    private javax.swing.JLabel RAValue;
    private javax.swing.JLabel TimeModeLabel;
    private javax.swing.JMenuItem closeMenuItem;
    private javax.swing.JLabel decValue;
    private javax.swing.JLabel distLabel;
    private javax.swing.JLabel distValue;
    private javax.swing.JLabel eclatValue;
    private javax.swing.JLabel eclatlabel;
    private javax.swing.JLabel eclonLabel;
    private javax.swing.JLabel eclonValue;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar moonMenu;
    private javax.swing.JPanel moonPanel;
    private javax.swing.JPanel phaseDatePanel;
    private javax.swing.JPanel phasePanel;
    private javax.swing.JPanel phasepicPanel;
    private javax.swing.JLabel riseAZ;
    private javax.swing.JLabel riseAzLabel;
    private javax.swing.JLabel riseLabel;
    private javax.swing.JLabel riseValue;
    private javax.swing.JLabel setAz;
    private javax.swing.JLabel setAzLabel;
    private javax.swing.JLabel setValue;
    private javax.swing.JLabel setlabel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel transAlt;
    private javax.swing.JLabel transAltLabel;
    private javax.swing.JLabel transLabel;
    private javax.swing.JLabel transValue;
    // End of variables declaration//GEN-END:variables
    
}
