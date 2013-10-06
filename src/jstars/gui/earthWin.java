/*
 * $Id: earthWin.java,v 1.5 2010/01/30 21:39:17 fc Exp $
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
 * Copyright (c) 2004 Florent Charpin
 *
 * email: jstars@florent.us
 *
 */

package jstars.gui;

import java.awt.Graphics2D;

/**
 *
 * @author  Florent Charpin
 */
public class earthWin extends javax.swing.JFrame {
    
    double elat, elon;
    jstars.util.Jlibastro libastro;
    int detailFlag = 1;
    boolean daynight = true;
    boolean spherical = true;
    int x_off, y_off, w, h;
    double zoom = 1.0;
    
    protected class earthPanel extends javax.swing.JPanel {
        @Override
        public void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            x_off = 0;
            y_off = 0;
            if(spherical) {
                w = ((int)(this.getWidth() * zoom)  -16)/ 2;
                h = ((int)(this.getHeight() * zoom) - 16) / 2;
                if(w > h) {
                    //x_off = w - h;
                    w = h;
                }
                else if(w < h){
                    //y_off = h - w;
                    h = w;
                }
                //x_off += 8;
                x_off = (this.getWidth() - (w*2)) / 2;
                //y_off += 8;
                y_off = (this.getHeight() - (h*2)) / 2;
            }
            else {
                w = ((int)(this.getWidth() ));
                h = ((int)(this.getHeight() ));
            }
            
            if(detailFlag == 1) {
                if(spherical) {
                    g2.setColor(java.awt.Color.blue);
                    g2.fill(new java.awt.geom.Ellipse2D.Double(x_off, y_off, w*2,h*2));
                }
                else {
                    g2.setColor(java.awt.Color.blue);
                    g2.fill(new java.awt.geom.Rectangle2D.Double(0,0,w,h));
                }
            }
            
            if(detailFlag == 0) {
                if(daynight)
                    drawSun(g2);
                drawMap(g2);
            }
            else {
                drawMap(g2);
                if(daynight)
                    drawSun(g2);
            }
            if(gridCheckBox.isSelected()) {
                drawGrid(g2);
            }
            if((spherical == true) && (detailFlag == 0)) {
                g2.setColor(java.awt.Color.gray);
                g2.drawOval(x_off, y_off, w*2,h*2);
            }
            
            if(detailFlag == 1)
                g2.setColor(java.awt.Color.red);
            else
                g2.setColor(java.awt.Color.green);
            double coord[] = new double[2];
            if(spherical) {
                boolean vis = spherexy(Math.toRadians(libastro.userData.getLongitude()*-1+elon) , Math.toRadians(libastro.userData.getLatitude()), Math.toRadians(elat), coord);
                int x = (int) (coord[0] * w + w) + x_off;
                int y = (int) (coord[1] * -1.0 * h + h) + y_off;
                if(vis) {
                    //g2.drawOval(x-3, y-3, 6,6);
                    g2.drawLine(x-3, y, x+3, y);
                    g2.drawLine(x, y-3, x, y+3);
                }
            }
            else {
                int cylcoor[] = new int[2];
                cylproj(Math.toRadians(libastro.userData.getLongitude()*-1+elon), Math.toRadians(libastro.userData.getLatitude()), 0, cylcoor);
                int x = cylcoor[0];
                int y = cylcoor[1];
                g2.drawLine(x-3, y, x+3, y);
                    g2.drawLine(x, y-3, x, y+3);
            }
            
            
        }
        
        private void drawSun(java.awt.Graphics2D g2) {
            java.awt.geom.GeneralPath spath = null;
            g2.setBackground(java.awt.Color.yellow);
            g2.setColor(java.awt.Color.gray);
            
            // position where sun overhead
            double slat = Math.toRadians(libastro.SolSys[0].Dec);
            double slon = libastro.SolSys[0].RA * 360.0 / 24.0;
            slon -= libastro.timeData.getGWsideralTime().getTimeInDegree(true);
            if(slon > 180.0)
                slon -= 360;
            slon = Math.toRadians(slon);
            /* find direction and length of line from center to subsolar point */
            double stuff1[] = new double[2];
            spherexy(Math.toRadians(elon) + slon , slat, Math.toRadians(elat), stuff1);
            int dx; int dy;
            if(detailFlag == 0) {
                dx = (int)(stuff1[0] * 20.0 * w);
                dy = (int)(stuff1[1] * -20.0 * w);
            }
            else {
                dx = (int)(stuff1[0] * -20.0 * w);
                dy = (int)(stuff1[1] * 20.0 * w);
            }
            int dgrow = 0;
            if(detailFlag == 1)
                dgrow = 4;
            
            int x, y; // coord
            int xp, yp;
            double[] coord = new double[2];
            int[] cylcoor = new int[2];
            boolean vis = true;
            
            double[] zxc = new double[2];
            double plat, plong;
            for(int n=0; n <= 3600; n+=5) {
                jstars.util.Jlibastro.sphereproj(Math.toRadians(n/5.0), Math.PI/2.0, slat, zxc);
                plat = Math.PI/2.0 - Math.acos(zxc[0]);
                plong = slon - zxc[1];
                if(spherical) {
                    vis = spherexy(plong + Math.toRadians(elon) , plat, Math.toRadians(elat), coord);
                    x = (int) (coord[0] * w + w) + x_off;
                    y = (int) (coord[1] * -1.0 * h + h) + y_off;
                }
                else { // cyl
                    cylproj(plong + Math.toRadians(elon), plat, 0, cylcoor);
                    x = cylcoor[0];
                    y = cylcoor[1];
                    if((spath != null) && (Math.abs(spath.getCurrentPoint().getX() - x) > (w/2))) {
                        if(((slat >= 0.0) && (detailFlag == 1)) || ((slat < 0.0) && (detailFlag == 0))) { // Summer North
                            if(spath.getCurrentPoint().getX() < 20.0) {
                                spath.lineTo(0, (int) spath.getCurrentPoint().getY());
                                spath.lineTo(0, h + 100);
                                spath.lineTo(w, h + 100);
                                spath.lineTo(w, y);
                            }
                            else {
                                spath.lineTo(w, (int) spath.getCurrentPoint().getY());
                                spath.lineTo(w, h + 100);
                                spath.lineTo(0, h + 100);
                                spath.lineTo(0, y);
                            }
                        }
                        else { // Summer South
                            if(spath.getCurrentPoint().getX() < 90.0) {
                                spath.lineTo(0, (int) spath.getCurrentPoint().getY());
                                spath.lineTo(0, 0);
                                spath.lineTo(w, 0);
                                spath.lineTo(w, y);
                            }
                            else {
                                spath.lineTo(w, y);
                                spath.lineTo(w, 0);
                                spath.lineTo(0, 0);
                                spath.lineTo(0, y);
                            }
                        }
                    }
                }
                if((x == 0) || (y == 0)) {
                    continue;
                }
                if(vis == true) {
                    if(spath != null)
                        spath.lineTo(x,y);
                    else {
                        spath = new java.awt.geom.GeneralPath();
                        spath.moveTo(x,y);
                    }
                }
                else {
                    double limb[] = new double[4];
                    lc(x_off - 0, y_off - 0, (2*w) + dgrow, x, y, x+dx, y+dy, limb);
                    if(((int)limb[2] == 0) || ((int)limb[3] == 0)) {
                        continue;
                    }
                    if(spath != null)
                        spath.lineTo((int)limb[2],(int)limb[3]);
                    else {
                        spath = new java.awt.geom.GeneralPath();
                        spath.moveTo((int)limb[2],(int)limb[3]);
                    }
                }
            }
            if(detailFlag == 0) {
                g2.setColor(java.awt.Color.yellow);
            }
            else {
                g2.setColor(new java.awt.Color(0,0,0,80));
            }
            
            g2.fill(spath);
        }
        
        private void drawGrid(java.awt.Graphics2D g2) {
            g2.setBackground(java.awt.Color.black);
            g2.setColor(java.awt.Color.gray);
            int x, y; // coord
            int xp, yp;
            double[] coord = new double[2];
            int[] cylcoor = new int[2];
            boolean vis = true;
            
            // longitude
            for(int i = 0; i < 360; i+=20) {
                xp = 0; yp = 0;
                double lon = Math.toRadians((double)i+ elon);
                for(int j = 90; j >= -90; j-=1) {
                    double lat = Math.toRadians(j);
                    if(spherical) {
                        vis = spherexy(lon, lat, Math.toRadians(elat), coord);
                        x = (int) (coord[0] * w + w) + x_off;
                        y = (int) (coord[1] * -1.0 * h + h) + y_off;
                    }
                    else { // cyl
                        cylproj(lon, lat, 0, cylcoor);
                        x = cylcoor[0];
                        y = cylcoor[1];
                    }
                    if((j != 90) && (vis == true)) {
                        g2.drawLine(xp, yp, x, y);
                    }
                    xp = x; yp = y;
                }
            }
            // latitude
            for(int j = 90; j >= -90; j-=10) {
                xp = 0; yp = 0;
                
                double lat = Math.toRadians(j);
                for(int i = 0; i <= 360; i+=1) {
                    double lon = Math.toRadians((double)i+elon);
                    if(spherical) {
                        vis = spherexy(lon , lat, Math.toRadians(elat), coord);
                        x = (int) (coord[0] * w + w) + x_off;
                        y = (int) (coord[1] * -1.0 * h + h) + y_off;
                    }
                    else {
                        cylproj(lon, lat, 0, cylcoor);
                        x = cylcoor[0];
                        y = cylcoor[1];
                    }
                    if((i != 0) && (vis == true)) {
                        g2.drawLine(xp, yp, x, y);
                    }
                    xp = x; yp = y;
                }
            }
            
            
            
        }
        
        private int lc(int cx, int cy, int cw, int x1, int y1, int x2, int y2, double seg[]) {
            int dx = x2 - x1;
            int dy = y2 - y1;
            int r = cw/2;
            int xc = cx + r;
            int yc = cy + r;
            int A = x1 - xc;
            int B = y1 - yc;
            double a = dx*dx + dy*dy;	/* O(2 * 2**16 * 2**16) */
            double b = 2*(dx*A + dy*B);	/* O(4 * 2**16 * 2**16) */
            double c = A*A + B*B - r*r;	/* O(2 * 2**16 * 2**16) */
            double d = b*b - 4*a*c;		/* O(2**32 * 2**32) */
            double sqrtd;
            double t1, t2;
            
            if (d <= 0)
                return (-1);	/* containing line is purely outside circle */
            
            sqrtd = Math.sqrt(d);
            t1 = (-b - sqrtd)/(2.0*a);
            t2 = (-b + sqrtd)/(2.0*a);
            
            if (t1 >= 1.0 || t2 <= 0.0)
                return (-1);	/* segment is purely outside circle */
            
        /* we know now that some part of the segment is inside,
         * ie, t1 < 1 && t2 > 0
         */
            
            if (t1 <= 0.0) {
                /* (x1,y1) is inside circle */
                seg[0] = x1;
                seg[1] = y1;
            } else {
                seg[0] = (int)(x1 + dx*t1);
                seg[1] = (int)(y1 + dy*t1);
            }
            
            if (t2 >= 1.0) {
                /* (x2,y2) is inside circle */
                seg[2] = x2;
                seg[3] = y2;
            } else {
                seg[2] = (int)(x1 + dx*t2);
                seg[3] = (int)(y1 + dy*t2);
            }
            
            return (0);
        }
        
        private void cylproj(double map0, double map1, double r_elon, int[] res) {
            res[0] = (int)(jstars.util.Jlibastro.range180((map0 + r_elon)/Math.PI*180.0)/2.0*w/180.0) + (w/2);
            res[1] = (int)(Math.tan((map1 - 0.1)/-2.0)*h*.63) + (h/2);
        }
        
        private void drawMap(java.awt.Graphics2D g2) {
            java.awt.geom.GeneralPath cpath = null;
            java.awt.geom.GeneralPath cpath1 = null;
            long start = System.currentTimeMillis();
            g2.setBackground(java.awt.Color.black);
            if(detailFlag == 0)
                g2.setColor(java.awt.Color.red);
            else
                g2.setColor(java.awt.Color.green);
            
            int x, y; // coord
            int xp, yp;
            double[] coord = new double[2];
            int[] cylcoor = new int[2];
            boolean vis = true;
            int fx = 0;
            int fy = 0;
            int fx1 = 0;
            double _map[][];
            double r_elon = Math.toRadians(elon);
            double r_elat = Math.toRadians(elat);
            
            for(int z = 0; z < jstars.guidata.earthMap.map.length; z++) {
                xp = 0; yp = 0;
                fx = 0; fy = 0;
                _map = (double[][]) jstars.guidata.earthMap.map[z];
                if(detailFlag ==1) {
                    if(jstars.guidata.earthMap.mapland[z])
                        g2.setColor(java.awt.Color.green);
                    else
                        g2.setColor(java.awt.Color.blue);
                    
                }
                //if(z>(jstars.guidata.earthMap.map.length - 11))
                //    g2.setColor(java.awt.Color.green);
                for(int k=0; k < _map.length; k++) {
                    if(spherical) {
                        vis = spherexy(_map[k][0] + r_elon , _map[k][1], r_elat, coord);
                        x = (int) (coord[0] * w + w) + x_off;
                        y = (int) (coord[1] * -1.0 * h + h) + y_off;
                    }
                    else {
                        cylproj(_map[k][0], _map[k][1], r_elon, cylcoor);
                        x = cylcoor[0];
                        y = cylcoor[1];
                    }
                    if(vis) {
                        if(detailFlag == 0) {
                            if(k != 0) {
                                if((spherical == true) || (Math.abs(xp - x) < (w/2)))
                                    g2.drawLine(xp, yp, x, y);
                            }
                            else {
                                fx = x; fy = y;
                            }
                        }
                        else {
                            if(cpath == null) {
                                cpath = new java.awt.geom.GeneralPath();
                                cpath.moveTo(x,y);
                                cpath.lineTo(x,y);
                                fx = x; fy = y;
                            }
                            else {
                                java.awt.geom.Point2D pointxy = cpath.getCurrentPoint();
                                int dist = jstars.util.Jlibastro.distance(x,y, (int)pointxy.getX(), (int)pointxy.getY());
                                if(spherical) {
                                    if(dist > (20*zoom)) {
                                        //System.out.println(dist);
                                        // draw arc.
                                        double  _start = Math.toDegrees(Math.acos((pointxy.getX()-x_off - w) / w));
                                        if(((int)pointxy.getY() - y_off) > (w)) {
                                            _start = 360 - _start;
                                        }
                                        double _extend = Math.toDegrees(Math.acos(((double)x-x_off -w) / w));
                                        if((y - y_off) > (w)) {
                                            _extend = 360 - _extend;
                                            
                                        }
                                        _extend -= _start;
                                        if(_extend < -200)
                                            _extend = _extend + 360;
                                        if(_extend > 200)
                                            _extend = _extend - 360;
                                        
                                        java.awt.Shape _arc = new java.awt.geom.Arc2D.Double(x_off, y_off, w*2.0, w*2.0,_start, _extend, java.awt.geom.Arc2D.OPEN);
                                        cpath.append(_arc, true);
                                    }
                                    else if(dist > 0)
                                        cpath.lineTo(x,y);
                                }
                                else { // cylindrical
                                    if(Math.abs(cpath.getCurrentPoint().distance(x,y)) > 100.0) {
                                        if(cpath1 == null) {
                                            cpath1 = new java.awt.geom.GeneralPath();
                                            cpath1.moveTo(x,y);
                                            cpath1.lineTo(x,y);
                                            fx1 = x;
                                        }
                                        if(cpath.getCurrentPoint().getX() < 30.0) {
                                            cpath.lineTo(x - this.getWidth(), y);
                                        }
                                        else {
                                            cpath.lineTo(x + this.getWidth(), y);
                                        }
                                    }
                                    else {
                                        cpath.lineTo(x,y);
                                    }
                                    if(cpath1 != null) {
                                        if(Math.abs(cpath1.getCurrentPoint().distance(x,y)) > 100.0) {
                                            if(cpath1.getCurrentPoint().getX() < 30.0) {
                                                cpath1.lineTo(x - this.getWidth(), y);
                                            }
                                            else {
                                                cpath1.lineTo(x + this.getWidth(), y);
                                            }
                                        }
                                        else {
                                            cpath1.lineTo(x,y);
                                        }
                                    }
                                }
                            }
                        }
                        
                    }
                    xp = x;
                    yp = y;
                }
                if((detailFlag == 0) && (fx != 0)) {
                    g2.drawLine(fx, fy, xp, yp);
                }
                if(cpath != null) {
                    if(detailFlag == 1) {
                        java.awt.geom.Point2D pointxy = cpath.getCurrentPoint();
                        int dist = jstars.util.Jlibastro.distance(fx,fy, (int)pointxy.getX(), (int)pointxy.getY());
                        if(spherical) {
                            if((dist > 5) && !vis) {
                                double  _start = Math.toDegrees(Math.acos((pointxy.getX()-x_off - w) / w));
                                if(((int)pointxy.getY() - y_off) > (w)) {
                                    _start = 360 - _start;
                                }
                                double _extend = Math.toDegrees(Math.acos(((double)fx-x_off -w) / w));
                                if((fy - y_off) > (w)) {
                                    _extend = 360 - _extend;
                                    
                                }
                                _extend -= _start;
                                if(_extend < -200)
                                    _extend = _extend + 360;
                                if(_extend > 200)
                                    _extend = _extend - 360;
                                
                                
                                java.awt.Shape _arc = new java.awt.geom.Arc2D.Double(x_off, y_off, w*2.0, w*2.0,_start, _extend, java.awt.geom.Arc2D.OPEN);
                                cpath.append(_arc, true);
                            }
                        }
                        else {
                            // complete hack to deal with Antartica....
                            if((fx - cpath.getCurrentPoint().getX() > 20)) {
                                cpath.lineTo((int) cpath.getCurrentPoint().getX(), h + 250);
                                cpath.lineTo(fx, h+250);
                            }
                            if(cpath1 != null)
                                if((fx1 - cpath1.getCurrentPoint().getX() > 20)) {
                                    cpath1.lineTo((int) cpath1.getCurrentPoint().getX(), h + 250);
                                    cpath1.lineTo(fx1, h+250);
                                }
                            
                        }
                        g2.fill(cpath);
                        if(cpath1 != null)
                            g2.fill(cpath1);
                    }
                    cpath = null;
                    cpath1 = null;
                }
            }
            System.out.println("--> " + (System.currentTimeMillis() - start));
        }
        
    }
    
    
    private boolean spherexy(double lon, double lat, double elat, double [] coor) {
        double res[] = new double[2];
        boolean vis = jstars.util.Jlibastro.sphereproj(lon, Math.PI/2 - lat, elat, res);
        
        coor[0] = Math.sin(Math.PI / 2.0 - Math.abs(lat));
        coor[0] = Math.sin(lon) * coor[0];
        double zsR = Math.sqrt(1.0 - res[0]*res[0]);
        double zsA = Math.sin(res[1]);
        double zcA = Math.cos(res[1]);
        coor[1] = zsR*zcA;
        return vis;
    }
    
    javax.swing.JToggleButton earthButton;
    /** Creates new form earthWin */
    public earthWin(jstars.util.Jlibastro _libastro, javax.swing.JToggleButton Button) {
        earthButton = Button;
        libastro = _libastro;
        initComponents();
        earthViewPanel.setFocusable(true);
        this.zoomSlider.setVisible(false);
        this.tiltSlider.setVisible(false);
        this.latSlider.setFont(new java.awt.Font("Dialog", 0, 8));
        this.elat = libastro.userData.getLatitude();
        this.elon = libastro.userData.getLongitude();
        this.latSlider.setValue((int) libastro.userData.getLatitude());
        this.lonSlider.setValue((int) libastro.userData.getLongitude());
        pack();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        prefDialog = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        detailButtonGroup = new javax.swing.ButtonGroup();
        projGroup = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        lonSlider = new javax.swing.JSlider();
        latSlider = new javax.swing.JSlider();
        earthViewPanel = new earthPanel();
        zoomSlider = new javax.swing.JSlider();
        tiltSlider = new javax.swing.JSlider();
        jLabelFillBR = new javax.swing.JLabel();
        jLabelFillBL = new javax.swing.JLabel();
        jLabelFillTL = new javax.swing.JLabel();
        jLabelFillTR = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        jMenuCenterItem = new javax.swing.JMenuItem();
        jMenuResetItem = new javax.swing.JMenuItem();
        exitItem = new javax.swing.JMenuItem();
        optionMenu = new javax.swing.JMenu();
        jRadioButtonWire = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonSolid = new javax.swing.JRadioButtonMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jRadioButtonSpherical = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonCylindrical = new javax.swing.JRadioButtonMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        gridCheckBox = new javax.swing.JCheckBoxMenuItem();
        sunCheckBox = new javax.swing.JCheckBoxMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuSliders = new javax.swing.JMenu();
        jCheckBoxLat = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxLon = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxZoom = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxTilt = new javax.swing.JCheckBoxMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        prefItem = new javax.swing.JMenuItem();

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        jButton1.setText("OK");
        jPanel1.add(jButton1);

        jButton2.setText("CANCEL");
        jPanel1.add(jButton2);

        prefDialog.getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(new javax.swing.border.TitledBorder("Grid"));
        jLabel1.setText("Latitude lines");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 12;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jSpinner1, gridBagConstraints);

        jLabel2.setText("longitude lines");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 12;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jSpinner2, gridBagConstraints);

        jLabel3.setText("Color");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel3, gridBagConstraints);

        jButton3.setBackground(new java.awt.Color(102, 255, 102));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jButton3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 79;
        gridBagConstraints.ipady = 18;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(jPanel3, gridBagConstraints);

        prefDialog.getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        mainPanel.setLayout(new java.awt.GridBagLayout());

        mainPanel.setBackground(new java.awt.Color(0, 0, 0));
        mainPanel.setForeground(new java.awt.Color(255, 255, 255));
        mainPanel.setFont(new java.awt.Font("Dialog", 0, 8));
        mainPanel.setPreferredSize(new java.awt.Dimension(400, 400));
        lonSlider.setFont(new java.awt.Font("Dialog", 0, 8));
        lonSlider.setMajorTickSpacing(60);
        lonSlider.setMaximum(180);
        lonSlider.setMinimum(-180);
        lonSlider.setMinorTickSpacing(10);
        lonSlider.setPaintTicks(true);
        lonSlider.setToolTipText("0");
        lonSlider.setValue(0);
        lonSlider.setDoubleBuffered(true);
        lonSlider.setInverted(true);
        lonSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                lonSliderStateChanged(evt);
            }
        });
        lonSlider.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lonSliderKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        mainPanel.add(lonSlider, gridBagConstraints);

        latSlider.setFont(new java.awt.Font("Dialog", 0, 8));
        latSlider.setMajorTickSpacing(45);
        latSlider.setMaximum(90);
        latSlider.setMinimum(-90);
        latSlider.setMinorTickSpacing(10);
        latSlider.setOrientation(javax.swing.JSlider.VERTICAL);
        latSlider.setPaintTicks(true);
        latSlider.setValue(0);
        latSlider.setDoubleBuffered(true);
        latSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                latSliderStateChanged(evt);
            }
        });
        latSlider.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                latSliderKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        mainPanel.add(latSlider, gridBagConstraints);

        earthViewPanel.setBackground(new java.awt.Color(0, 0, 0));
        earthViewPanel.setPreferredSize(new java.awt.Dimension(300, 300));
        earthViewPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                earthViewPanelKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        mainPanel.add(earthViewPanel, gridBagConstraints);

        zoomSlider.setFont(new java.awt.Font("Dialog", 0, 8));
        zoomSlider.setMajorTickSpacing(100);
        zoomSlider.setMaximum(500);
        zoomSlider.setMinimum(20);
        zoomSlider.setMinorTickSpacing(25);
        zoomSlider.setOrientation(javax.swing.JSlider.VERTICAL);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setDoubleBuffered(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        mainPanel.add(zoomSlider, gridBagConstraints);

        tiltSlider.setFont(new java.awt.Font("Dialog", 0, 8));
        tiltSlider.setMajorTickSpacing(60);
        tiltSlider.setMaximum(180);
        tiltSlider.setMinimum(-180);
        tiltSlider.setMinorTickSpacing(10);
        tiltSlider.setPaintTicks(true);
        tiltSlider.setToolTipText("0");
        tiltSlider.setValue(0);
        tiltSlider.setDoubleBuffered(true);
        tiltSlider.setInverted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        mainPanel.add(tiltSlider, gridBagConstraints);

        jLabelFillBR.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        mainPanel.add(jLabelFillBR, gridBagConstraints);

        jLabelFillBL.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        mainPanel.add(jLabelFillBL, gridBagConstraints);

        jLabelFillTL.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        mainPanel.add(jLabelFillTL, gridBagConstraints);

        jLabelFillTR.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        mainPanel.add(jLabelFillTR, gridBagConstraints);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        fileMenu.setText("File");
        jMenuCenterItem.setMnemonic('C');
        jMenuCenterItem.setText("Center on Current Location");
        jMenuCenterItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuCenterItemActionPerformed(evt);
            }
        });

        fileMenu.add(jMenuCenterItem);

        jMenuResetItem.setMnemonic('R');
        jMenuResetItem.setText("Reset to Defaults");
        fileMenu.add(jMenuResetItem);

        exitItem.setMnemonic('x');
        exitItem.setText("Exit");
        exitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitItemActionPerformed(evt);
            }
        });

        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        optionMenu.setText("Options");
        jRadioButtonWire.setText("Wireframe");
        detailButtonGroup.add(jRadioButtonWire);
        jRadioButtonWire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonWireActionPerformed(evt);
            }
        });

        optionMenu.add(jRadioButtonWire);

        jRadioButtonSolid.setSelected(true);
        jRadioButtonSolid.setText("Solid");
        detailButtonGroup.add(jRadioButtonSolid);
        jRadioButtonSolid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonSolidActionPerformed(evt);
            }
        });

        optionMenu.add(jRadioButtonSolid);

        optionMenu.add(jSeparator1);

        jRadioButtonSpherical.setSelected(true);
        jRadioButtonSpherical.setText("Spherical");
        projGroup.add(jRadioButtonSpherical);
        jRadioButtonSpherical.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonSphericalActionPerformed(evt);
            }
        });

        optionMenu.add(jRadioButtonSpherical);

        jRadioButtonCylindrical.setText("Cylindrical");
        projGroup.add(jRadioButtonCylindrical);
        jRadioButtonCylindrical.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonCylindricalActionPerformed(evt);
            }
        });

        optionMenu.add(jRadioButtonCylindrical);

        optionMenu.add(jSeparator4);

        gridCheckBox.setSelected(true);
        gridCheckBox.setText("Grid");
        gridCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gridCheckBoxActionPerformed(evt);
            }
        });

        optionMenu.add(gridCheckBox);

        sunCheckBox.setSelected(true);
        sunCheckBox.setText("day/night");
        sunCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sunCheckBoxActionPerformed(evt);
            }
        });

        optionMenu.add(sunCheckBox);

        optionMenu.add(jSeparator2);

        jMenuSliders.setText("Sliders");
        jCheckBoxLat.setSelected(true);
        jCheckBoxLat.setText("Latitude");
        jCheckBoxLat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxLatActionPerformed(evt);
            }
        });

        jMenuSliders.add(jCheckBoxLat);

        jCheckBoxLon.setSelected(true);
        jCheckBoxLon.setText("Longitude");
        jCheckBoxLon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxLonActionPerformed(evt);
            }
        });

        jMenuSliders.add(jCheckBoxLon);

        jCheckBoxZoom.setText("Zoom");
        jCheckBoxZoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxZoomActionPerformed(evt);
            }
        });

        jMenuSliders.add(jCheckBoxZoom);

        jCheckBoxTilt.setText("Tilt");
        jCheckBoxTilt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxTiltActionPerformed(evt);
            }
        });

        jMenuSliders.add(jCheckBoxTilt);

        optionMenu.add(jMenuSliders);

        optionMenu.add(jSeparator3);

        prefItem.setMnemonic('p');
        prefItem.setText("Preference");
        prefItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prefItemActionPerformed(evt);
            }
        });

        optionMenu.add(prefItem);

        menuBar.add(optionMenu);

        setJMenuBar(menuBar);

    }//GEN-END:initComponents

    private void jMenuCenterItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuCenterItemActionPerformed
        this.elat = libastro.userData.getLatitude();
        this.elon = libastro.userData.getLongitude();
        this.latSlider.setValue((int) libastro.userData.getLatitude());
        this.lonSlider.setValue((int) libastro.userData.getLongitude());
        this.repaint();
    }//GEN-LAST:event_jMenuCenterItemActionPerformed
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed
    
    private void prefItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prefItemActionPerformed
        this.prefDialog.pack();
        this.prefDialog.setVisible(true);
        
    }//GEN-LAST:event_prefItemActionPerformed
    
    private void jRadioButtonCylindricalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonCylindricalActionPerformed
        spherical = false;
        this.jCheckBoxLat.setEnabled(false);
        this.jCheckBoxTilt.setEnabled(false);
        this.jCheckBoxZoom.setEnabled(false);
        this.latSlider.setVisible(false);
        this.zoomSlider.setVisible(false);
        this.tiltSlider.setVisible(false);
        this.repaint();
    }//GEN-LAST:event_jRadioButtonCylindricalActionPerformed
    
    private void jRadioButtonSphericalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonSphericalActionPerformed
        spherical = true;
        this.jCheckBoxLat.setEnabled(true);
        this.jCheckBoxTilt.setEnabled(true);
        this.jCheckBoxZoom.setEnabled(true);
        if(this.jCheckBoxLat.isSelected())
            this.latSlider.setVisible(true);
        if(this.jCheckBoxZoom.isSelected())
            this.zoomSlider.setVisible(true);
        if(this.jCheckBoxTilt.isSelected())
            this.tiltSlider.setVisible(true);
        this.repaint();
    }//GEN-LAST:event_jRadioButtonSphericalActionPerformed
    
    private void jCheckBoxTiltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxTiltActionPerformed
        if(this.jCheckBoxTilt.isSelected() == true) {
            this.tiltSlider.setVisible(true);
        }
        else {
            this.tiltSlider.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBoxTiltActionPerformed
    
    private void jCheckBoxLonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxLonActionPerformed
        if(this.jCheckBoxLon.isSelected() == true) {
            this.lonSlider.setVisible(true);
        }
        else {
            this.lonSlider.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBoxLonActionPerformed
    
    private void jCheckBoxLatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxLatActionPerformed
        if(this.jCheckBoxLat.isSelected() == true) {
            this.latSlider.setVisible(true);
        }
        else {
            this.latSlider.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBoxLatActionPerformed
    
    private void jCheckBoxZoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxZoomActionPerformed
        if(this.jCheckBoxZoom.isSelected() == true) {
            this.zoomSlider.setVisible(true);
        }
        else {
            this.zoomSlider.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBoxZoomActionPerformed
    
    private void keyEvent(java.awt.event.KeyEvent evt) {
        if(evt.getKeyChar() == '-')
            zoom *= 0.98;
        if(evt.getKeyChar() == '=')
            zoom *= 1.02;
        if(zoom < .2)
            zoom = .2;
        if(zoom > 5.0)
            zoom = 5.0;
        System.out.println("+++ " + zoom);
        this.earthViewPanel.repaint();
    }
    
    private void lonSliderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lonSliderKeyPressed
        keyEvent(evt);
    }//GEN-LAST:event_lonSliderKeyPressed
    
    private void latSliderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_latSliderKeyPressed
        keyEvent(evt);
    }//GEN-LAST:event_latSliderKeyPressed
    
    private void earthViewPanelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_earthViewPanelKeyPressed
        keyEvent(evt);
    }//GEN-LAST:event_earthViewPanelKeyPressed
    
    private void sunCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sunCheckBoxActionPerformed
        if(this.sunCheckBox.isSelected() == true)
            daynight = true;
        else
            daynight = false;
        this.earthViewPanel.repaint();
    }//GEN-LAST:event_sunCheckBoxActionPerformed
    
    private void jRadioButtonSolidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonSolidActionPerformed
        this.detailFlag = 1;
        this.earthViewPanel.repaint();
    }//GEN-LAST:event_jRadioButtonSolidActionPerformed
    
    private void jRadioButtonWireActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonWireActionPerformed
        this.detailFlag = 0;
        this.earthViewPanel.repaint();
    }//GEN-LAST:event_jRadioButtonWireActionPerformed
    
    private void gridCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gridCheckBoxActionPerformed
        this.earthViewPanel.repaint();
    }//GEN-LAST:event_gridCheckBoxActionPerformed
    
    private void lonSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_lonSliderStateChanged
        javax.swing.JSlider s = (javax.swing.JSlider) evt.getSource();
        this.elon = s.getValue();
        this.earthViewPanel.repaint();
    }//GEN-LAST:event_lonSliderStateChanged
    
    private void latSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_latSliderStateChanged
        javax.swing.JSlider s = (javax.swing.JSlider) evt.getSource();
        this.elat = s.getValue();
        this.earthViewPanel.repaint();
    }//GEN-LAST:event_latSliderStateChanged
    
    private void exitItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitItemActionPerformed
        earthButton.setSelected(false);
        dispose();
    }//GEN-LAST:event_exitItemActionPerformed
    
    
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        earthButton.setSelected(false);
        dispose();
    }//GEN-LAST:event_exitForm
    
    void updateDisp() {
        
        repaint();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup detailButtonGroup;
    private javax.swing.JPanel earthViewPanel;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JCheckBoxMenuItem gridCheckBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBoxMenuItem jCheckBoxLat;
    private javax.swing.JCheckBoxMenuItem jCheckBoxLon;
    private javax.swing.JCheckBoxMenuItem jCheckBoxTilt;
    private javax.swing.JCheckBoxMenuItem jCheckBoxZoom;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelFillBL;
    private javax.swing.JLabel jLabelFillBR;
    private javax.swing.JLabel jLabelFillTL;
    private javax.swing.JLabel jLabelFillTR;
    private javax.swing.JMenuItem jMenuCenterItem;
    private javax.swing.JMenuItem jMenuResetItem;
    private javax.swing.JMenu jMenuSliders;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButtonMenuItem jRadioButtonCylindrical;
    private javax.swing.JRadioButtonMenuItem jRadioButtonSolid;
    private javax.swing.JRadioButtonMenuItem jRadioButtonSpherical;
    private javax.swing.JRadioButtonMenuItem jRadioButtonWire;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSlider latSlider;
    private javax.swing.JSlider lonSlider;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu optionMenu;
    private javax.swing.JDialog prefDialog;
    private javax.swing.JMenuItem prefItem;
    private javax.swing.ButtonGroup projGroup;
    private javax.swing.JCheckBoxMenuItem sunCheckBox;
    private javax.swing.JSlider tiltSlider;
    private javax.swing.JSlider zoomSlider;
    // End of variables declaration//GEN-END:variables
    
}
