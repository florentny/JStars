/*
 * $Id: JStarsGUI.java,v 1.17 2010/01/30 21:39:18 fc Exp $
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
 * Unless credited otherwise pictures are from NASA NSSDC gallery (http://nssdc.gsfc.nasa.gov/photo_gallery)
 *
 * email: jstars@florent.us
 *
 */

package jstars.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import javax.swing.JDialog;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;
import jstars.util.ephemObject;
import jstars.util.Jlibastro.Jdate;




/**
 *
 * @author  Florent Charpin
 */

public class JStarsGUI extends javax.swing.JFrame {
    public static String version = "Version 2.0";
    public static String copyright = "© 2004-2010 Florent Charpin";
    
    java.util.Timer timer;
    Runnable updateFromLoop;
    jstars.util.Jlibastro libastro;
    
    
    private JDialog dialog;
    private JDialog preference;
    private boolean RTclock;
    private Jdate customDate;
    private Jdate pause;
    private Jdate step;
    private sunWin sunWindow;
    private moonWin moonWindow;
    private dataWin dataWindow;
    private jupiterWin jupiterWindow;
    private earthWin earthWindow;
    
    
    private java.util.TreeMap<String, String> lafMap;
    private JRadioButtonMenuItem[] lafItem;
    
    
    /** Creates new form jstarsGUI */
    public JStarsGUI() {
        
        updateFromLoop = new Runnable() {
            @Override
            public void run() {
                updateTime();
            }
        };
        
        RTclock = true;
        //customDate = new Jlibastro.Jdate();
        pause = new Jdate();
        pause.year = 0;
        pause.month = 0;
        pause.day = 0;
        pause.min = 0;
        pause.sec = 5;
        step = new Jdate();
        step.year = 0;
        step.month = 0;
        step.day = 0;
        step.hour = 1;
        libastro = new jstars.util.Jlibastro();
        initComponents();
        initLookAndFellMenu();
        loadSettings(true);
        libastro.updateLocation();
        updateLoc();
        updateTime();
        
    }
    
    private void initLookAndFellMenu() {
        int i;
        lafMap = new java.util.TreeMap<String, String>();
        // Find supported L&F
        UIManager.LookAndFeelInfo[] laflist = UIManager.getInstalledLookAndFeels();
        for(i=0; i < laflist.length; i++) {
            if(!lafMap.containsKey(laflist[i].getName())) {
                if(laflist[i].getName().equals("Metal"))
                    lafMap.put("Java", laflist[i].getClassName());
                else
                    lafMap.put(laflist[i].getName(), laflist[i].getClassName());
            }
        }
        
        // Add common L&F that may be available but not returned by getInstalledLookAndFeels()
        if(!lafMap.containsKey("GTK") && !lafMap.containsKey("GTK"))
            lafMap.put("GTK+", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        if(!lafMap.containsKey("CDE/Motif"))
            lafMap.put("CDE/Motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        if(!lafMap.containsKey("Mac OS X"))
            lafMap.put("Mac OS X", "com.sun.java.swing.plaf.macos.MacOS.LookAndFeel");
        if(!lafMap.containsKey("Kunststoff"))
            if(isAvailableLF("com.incors.plaf.kunststoff.KunststoffLookAndFeel"))
                lafMap.put("Kunststoff", "com.incors.plaf.kunststoff.KunststoffLookAndFeel");
        if(isAvailableLF("com.l2fprod.gui.plaf.skin.SkinLookAndFeel"))
            lafMap.put("SkinLF", "com.l2fprod.gui.plaf.skin.SkinLookAndFeel");
        
        
        lafItem = new JRadioButtonMenuItem[lafMap.size()];
        String currentLF = UIManager.getLookAndFeel().getID();
        if(currentLF.equals("Metal"))
            currentLF = "Java";
        i = 0;
        for (java.util.Iterator it=lafMap.keySet().iterator(); it.hasNext();) {
            lafItem[i] = new JRadioButtonMenuItem();
            String keyName = (String) it.next();
            if(currentLF.equals(keyName))
                lafItem[i].setSelected(true);
            lafItem[i].setFont(new Font("Dialog", 0, 12));
            lafItem[i].setText(keyName);
            buttonGroupLF.add(lafItem[i]);
            lafItem[i].addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    LookAndFeelEvt(evt);
                }
            });
            if(!isAvailableLF((String) lafMap.get(keyName)))
                lafItem[i].setEnabled(false);
            LookMenu.add(lafItem[i]);
            i++;
        }
    }
    
    private boolean isAvailableLF(String LF) {
        try {
            Class cl = Class.forName(LF);
            javax.swing.LookAndFeel LAF = (javax.swing.LookAndFeel)(cl.newInstance());
            return LAF.isSupportedLookAndFeel();
        } catch(Exception e) {
            return false;
        }
    }
    
    private void LookAndFeelEvt(java.awt.event.ActionEvent evt) {
        
        try {
            UIManager.setLookAndFeel((String) lafMap.get(evt.getActionCommand()));
        }
        catch (Exception exp) {
        }
        
        javax.swing.SwingUtilities.updateComponentTreeUI(this);
        mainToolBar.updateUI(); // work around for jdk 1.4.2 bug #4886944
        this.pack();
        this.setVisible(true);
        
        if(dialog != null)
            dialog.dispose();
        dialog = null;
        if(sunWindow != null)
            sunWindow.dispose();
        sunWindow = null;
        if(moonWindow != null)
            moonWindow.dispose();
        moonWindow = null;
        if(dataWindow != null)
            dataWindow.dispose();
        dataWindow = null;
        if(jupiterWindow != null)
            jupiterWindow.dispose();
        jupiterWindow = null;
        if(earthWindow != null)
            earthWindow.dispose();
        earthWindow = null;
        sunButton.setSelected(false);
        moonButton.setSelected(false);
        dataButton.setSelected(false);
        jupiterButton.setSelected(false);
        earthButton.setSelected(false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroupAction = new javax.swing.ButtonGroup();
        buttonGroupTime = new javax.swing.ButtonGroup();
        buttonGroupLF = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        LocationPanel = new javax.swing.JPanel();
        LatitudeLabel = new javax.swing.JLabel();
        LatitudeValue = new javax.swing.JLabel();
        LongitudeLabel = new javax.swing.JLabel();
        LonValue = new javax.swing.JLabel();
        elLabel = new javax.swing.JLabel();
        elValue = new javax.swing.JLabel();
        EquiLabel = new javax.swing.JLabel();
        EquiValue = new javax.swing.JLabel();
        LocationButton = new javax.swing.JButton();
        TZlabel = new javax.swing.JLabel();
        TZValue = new javax.swing.JLabel();
        TZOffLabel = new javax.swing.JLabel();
        TZOffValue = new javax.swing.JLabel();
        DSTLabel = new javax.swing.JLabel();
        DSTValue = new javax.swing.JLabel();
        TimePanel = new javax.swing.JPanel();
        localDateLabel = new javax.swing.JLabel();
        LocalDateValue = new javax.swing.JLabel();
        LocalTime = new javax.swing.JLabel();
        LocalTimeValue = new javax.swing.JLabel();
        UTDateLabel = new javax.swing.JLabel();
        UTDateValue = new javax.swing.JLabel();
        UTTimeLabel = new javax.swing.JLabel();
        UTTimeValue = new javax.swing.JLabel();
        JDLabel = new javax.swing.JLabel();
        JDValue = new javax.swing.JLabel();
        sideralLabel = new javax.swing.JLabel();
        sideralValue = new javax.swing.JLabel();
        DeltaTLabel = new javax.swing.JLabel();
        DeltaTValue = new javax.swing.JLabel();
        LSTLabel = new javax.swing.JLabel();
        LSTValue = new javax.swing.JLabel();
        timeModeLabel = new javax.swing.JLabel();
        DayPanel = new javax.swing.JPanel();
        riseLabel = new javax.swing.JLabel();
        riseValue = new javax.swing.JLabel();
        TransitLabel = new javax.swing.JLabel();
        transitValue = new javax.swing.JLabel();
        dawnLabel = new javax.swing.JLabel();
        dawnValue = new javax.swing.JLabel();
        duskLabel = new javax.swing.JLabel();
        duskValue = new javax.swing.JLabel();
        dayLengthLabel = new javax.swing.JLabel();
        dayLengthValue = new javax.swing.JLabel();
        twilightSel = new javax.swing.JComboBox();
        setLabel = new javax.swing.JLabel();
        setValue = new javax.swing.JLabel();
        AzLabel = new javax.swing.JLabel();
        AzValue = new javax.swing.JLabel();
        AltLabel = new javax.swing.JLabel();
        AltValue = new javax.swing.JLabel();
        ActionPanel = new javax.swing.JPanel();
        UpdateTimeButton = new javax.swing.JButton();
        WallClocktoggle = new javax.swing.JRadioButton();
        SetDatetoggle = new javax.swing.JRadioButton();
        timeDateButton = new javax.swing.JButton();
        stepValue = new javax.swing.JLabel();
        pauseValue = new javax.swing.JLabel();
        stepButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();
        loopButton = new javax.swing.JButton();
        mainToolBar = new javax.swing.JToolBar();
        dataButton = new javax.swing.JToggleButton();
        sunButton = new javax.swing.JToggleButton();
        moonButton = new javax.swing.JToggleButton();
        earthButton = new javax.swing.JToggleButton();
        jupiterButton = new javax.swing.JToggleButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenuItemSave = new javax.swing.JMenuItem();
        jMenuItemSaveAs = new javax.swing.JMenuItem();
        jMenuItemExit = new javax.swing.JMenuItem();
        OptionsMenu = new javax.swing.JMenu();
        prefMenu = new javax.swing.JMenuItem();
        timeMenu = new javax.swing.JMenu();
        LTMenuItem = new javax.swing.JRadioButtonMenuItem();
        UTCMenuItem = new javax.swing.JRadioButtonMenuItem();
        locationMenu = new javax.swing.JMenuItem();
        toolBarMenuItem = new javax.swing.JCheckBoxMenuItem();
        LookMenu = new javax.swing.JMenu();
        ViewMenu = new javax.swing.JMenu();
        DataWinItem = new javax.swing.JMenuItem();
        SunItem = new javax.swing.JMenuItem();
        MoonWinItem = new javax.swing.JMenuItem();
        JupiterWinItem = new javax.swing.JMenuItem();
        earthWinItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        splashItem = new javax.swing.JMenuItem();
        aboutItem = new javax.swing.JMenuItem();

        setTitle("JStars");
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(getClass().getResource("/jstars/images/wicon.gif"))
        );
        setName("mainframe");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setFont(new java.awt.Font("Dialog", 0, 10));
        jPanel2.setLayout(new java.awt.GridLayout(2, 2));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(null);
        LocationPanel.setLayout(new java.awt.GridBagLayout());

        LocationPanel.setBackground(new java.awt.Color(255, 255, 255));
        LocationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Location"));
        LocationPanel.setMinimumSize(new java.awt.Dimension(150, 200));
        LatitudeLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        LatitudeLabel.setText("Latitude:");
        LatitudeLabel.setMaximumSize(new java.awt.Dimension(55, 15));
        LatitudeLabel.setMinimumSize(new java.awt.Dimension(55, 15));
        LatitudeLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        LocationPanel.add(LatitudeLabel, gridBagConstraints);

        LatitudeValue.setFont(new java.awt.Font("Dialog", 0, 10));
        LatitudeValue.setText("N 40:45:06");
        LatitudeValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 13;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 0);
        LocationPanel.add(LatitudeValue, gridBagConstraints);

        LongitudeLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        LongitudeLabel.setText("Longitude:");
        LongitudeLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        LocationPanel.add(LongitudeLabel, gridBagConstraints);

        LonValue.setFont(new java.awt.Font("Dialog", 0, 10));
        LonValue.setText("W 73:59:44");
        LonValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 13;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 0);
        LocationPanel.add(LonValue, gridBagConstraints);

        elLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        elLabel.setText("Elevation:");
        elLabel.setMaximumSize(new java.awt.Dimension(51, 13));
        elLabel.setMinimumSize(new java.awt.Dimension(51, 13));
        elLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        LocationPanel.add(elLabel, gridBagConstraints);

        elValue.setFont(new java.awt.Font("Dialog", 0, 10));
        elValue.setText("---");
        elValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 13;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 0);
        LocationPanel.add(elValue, gridBagConstraints);

        EquiLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        EquiLabel.setText("Equinox:");
        EquiLabel.setMaximumSize(new java.awt.Dimension(51, 13));
        EquiLabel.setMinimumSize(new java.awt.Dimension(51, 13));
        EquiLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 3, 0);
        LocationPanel.add(EquiLabel, gridBagConstraints);

        EquiValue.setFont(new java.awt.Font("Dialog", 0, 10));
        EquiValue.setText("2003.5");
        EquiValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 13;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 0);
        LocationPanel.add(EquiValue, gridBagConstraints);

        LocationButton.setFont(new java.awt.Font("Dialog", 1, 10));
        LocationButton.setText("New York, NY");
        LocationButton.setToolTipText("Click to set a new location");
        LocationButton.setFocusable(false);
        LocationButton.setMargin(new java.awt.Insets(2, 5, 2, 5));
        LocationButton.setPreferredSize(null);
        LocationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationwindow(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 10, 5);
        LocationPanel.add(LocationButton, gridBagConstraints);

        TZlabel.setFont(new java.awt.Font("Dialog", 0, 10));
        TZlabel.setText("Time Zone:");
        TZlabel.setMaximumSize(new java.awt.Dimension(51, 13));
        TZlabel.setMinimumSize(new java.awt.Dimension(51, 13));
        TZlabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        LocationPanel.add(TZlabel, gridBagConstraints);

        TZValue.setFont(new java.awt.Font("Dialog", 0, 10));
        TZValue.setText("EST");
        TZValue.setToolTipText("");
        TZValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 13;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 0);
        LocationPanel.add(TZValue, gridBagConstraints);

        TZOffLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        TZOffLabel.setText("TZ offset:");
        TZOffLabel.setToolTipText("local hours ahead of UTC");
        TZOffLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        TZOffLabel.setMaximumSize(new java.awt.Dimension(51, 13));
        TZOffLabel.setMinimumSize(new java.awt.Dimension(51, 13));
        TZOffLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        LocationPanel.add(TZOffLabel, gridBagConstraints);

        TZOffValue.setFont(new java.awt.Font("Dialog", 0, 10));
        TZOffValue.setText("-5:00");
        TZOffValue.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        TZOffValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 13;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 0);
        LocationPanel.add(TZOffValue, gridBagConstraints);

        DSTLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        DSTLabel.setText("DST:");
        DSTLabel.setToolTipText("Day light on/off");
        DSTLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        DSTLabel.setMaximumSize(new java.awt.Dimension(51, 13));
        DSTLabel.setMinimumSize(new java.awt.Dimension(51, 13));
        DSTLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        LocationPanel.add(DSTLabel, gridBagConstraints);

        DSTValue.setFont(new java.awt.Font("Dialog", 0, 10));
        DSTValue.setText("No");
        DSTValue.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        DSTValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 0);
        LocationPanel.add(DSTValue, gridBagConstraints);

        jPanel2.add(LocationPanel);

        TimePanel.setLayout(new java.awt.GridBagLayout());

        TimePanel.setBackground(new java.awt.Color(255, 255, 255));
        TimePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Time"));
        TimePanel.setFont(new java.awt.Font("Dialog", 0, 10));
        localDateLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        localDateLabel.setText("Local Date:");
        localDateLabel.setMaximumSize(new java.awt.Dimension(60, 25));
        localDateLabel.setMinimumSize(new java.awt.Dimension(51, 10));
        localDateLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        TimePanel.add(localDateLabel, gridBagConstraints);

        LocalDateValue.setFont(new java.awt.Font("Dialog", 0, 10));
        LocalDateValue.setText("March 31, 2003");
        LocalDateValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        TimePanel.add(LocalDateValue, gridBagConstraints);

        LocalTime.setFont(new java.awt.Font("Dialog", 0, 10));
        LocalTime.setText("Local Time:");
        LocalTime.setMaximumSize(new java.awt.Dimension(51, 20));
        LocalTime.setMinimumSize(new java.awt.Dimension(51, 13));
        LocalTime.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        TimePanel.add(LocalTime, gridBagConstraints);

        LocalTimeValue.setFont(new java.awt.Font("Dialog", 1, 10));
        LocalTimeValue.setText("20:23:34");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        TimePanel.add(LocalTimeValue, gridBagConstraints);

        UTDateLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        UTDateLabel.setText("UTC Date:");
        UTDateLabel.setToolTipText("Universal Time Datel");
        UTDateLabel.setMaximumSize(new java.awt.Dimension(51, 13));
        UTDateLabel.setMinimumSize(new java.awt.Dimension(51, 13));
        UTDateLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        TimePanel.add(UTDateLabel, gridBagConstraints);

        UTDateValue.setFont(new java.awt.Font("Dialog", 0, 10));
        UTDateValue.setText("April 1, 2003");
        UTDateValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        TimePanel.add(UTDateValue, gridBagConstraints);

        UTTimeLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        UTTimeLabel.setText("UTC Time:");
        UTTimeLabel.setToolTipText("Universal Time (GMT)");
        UTTimeLabel.setMaximumSize(new java.awt.Dimension(51, 13));
        UTTimeLabel.setMinimumSize(new java.awt.Dimension(51, 13));
        UTTimeLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        TimePanel.add(UTTimeLabel, gridBagConstraints);

        UTTimeValue.setFont(new java.awt.Font("Dialog", 1, 10));
        UTTimeValue.setText("01:23:34");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        TimePanel.add(UTTimeValue, gridBagConstraints);

        JDLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        JDLabel.setText("Julian Day:");
        JDLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        TimePanel.add(JDLabel, gridBagConstraints);

        JDValue.setFont(new java.awt.Font("Dialog", 0, 10));
        JDValue.setText("2452730.56273");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        TimePanel.add(JDValue, gridBagConstraints);

        sideralLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        sideralLabel.setText("Sideral Time:");
        sideralLabel.setToolTipText("Local Sideral Time");
        sideralLabel.setMaximumSize(new java.awt.Dimension(200, 20));
        sideralLabel.setMinimumSize(new java.awt.Dimension(60, 15));
        sideralLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 3);
        TimePanel.add(sideralLabel, gridBagConstraints);

        sideralValue.setFont(new java.awt.Font("Dialog", 0, 10));
        sideralValue.setText("09:10:22");
        sideralValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        TimePanel.add(sideralValue, gridBagConstraints);

        DeltaTLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        DeltaTLabel.setText("Delta T:");
        DeltaTLabel.setMaximumSize(new java.awt.Dimension(51, 13));
        DeltaTLabel.setMinimumSize(new java.awt.Dimension(51, 13));
        DeltaTLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        TimePanel.add(DeltaTLabel, gridBagConstraints);

        DeltaTValue.setFont(new java.awt.Font("Dialog", 0, 10));
        DeltaTValue.setText("+66.35");
        DeltaTValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        TimePanel.add(DeltaTValue, gridBagConstraints);

        LSTLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        LSTLabel.setText("LST@0:");
        LSTLabel.setToolTipText("Local Sideral Time at 0:00 local time");
        LSTLabel.setMaximumSize(new java.awt.Dimension(200, 20));
        LSTLabel.setMinimumSize(new java.awt.Dimension(60, 15));
        LSTLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        TimePanel.add(LSTLabel, gridBagConstraints);

        LSTValue.setFont(new java.awt.Font("Dialog", 0, 10));
        LSTValue.setText("06:17:58");
        LSTValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        TimePanel.add(LSTValue, gridBagConstraints);

        timeModeLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        timeModeLabel.setText("Time mode: EST (local time)");
        timeModeLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        timeModeLabel.setMaximumSize(new java.awt.Dimension(51, 13));
        timeModeLabel.setMinimumSize(new java.awt.Dimension(51, 13));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        TimePanel.add(timeModeLabel, gridBagConstraints);

        jPanel2.add(TimePanel);

        DayPanel.setLayout(new java.awt.GridBagLayout());

        DayPanel.setBackground(new java.awt.Color(255, 255, 255));
        DayPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Sun"));
        DayPanel.setMinimumSize(new java.awt.Dimension(150, 150));
        riseLabel.setFont(new java.awt.Font("Dialog", 1, 10));
        riseLabel.setText("Rise:");
        riseLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        DayPanel.add(riseLabel, gridBagConstraints);

        riseValue.setFont(new java.awt.Font("Dialog", 0, 10));
        riseValue.setText("05:35");
        riseValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        DayPanel.add(riseValue, gridBagConstraints);

        TransitLabel.setFont(new java.awt.Font("Dialog", 1, 10));
        TransitLabel.setText("Transit:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        DayPanel.add(TransitLabel, gridBagConstraints);

        transitValue.setFont(new java.awt.Font("Dialog", 0, 10));
        transitValue.setText("18:24");
        transitValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        DayPanel.add(transitValue, gridBagConstraints);

        dawnLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        dawnLabel.setText("Dawn:");
        dawnLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 2, 0);
        DayPanel.add(dawnLabel, gridBagConstraints);

        dawnValue.setFont(new java.awt.Font("Dialog", 0, 10));
        dawnValue.setText("05:35");
        dawnValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        DayPanel.add(dawnValue, gridBagConstraints);

        duskLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        duskLabel.setText("Dusk:");
        duskLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        DayPanel.add(duskLabel, gridBagConstraints);

        duskValue.setFont(new java.awt.Font("Dialog", 0, 10));
        duskValue.setText("18:24");
        duskValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        DayPanel.add(duskValue, gridBagConstraints);

        dayLengthLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        dayLengthLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dayLengthLabel.setText("Day Length:");
        dayLengthLabel.setToolTipText("Length of Day");
        dayLengthLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 5);
        DayPanel.add(dayLengthLabel, gridBagConstraints);

        dayLengthValue.setFont(new java.awt.Font("Dialog", 0, 10));
        dayLengthValue.setText("08:02");
        dayLengthValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        DayPanel.add(dayLengthValue, gridBagConstraints);

        twilightSel.setBackground(new java.awt.Color(255, 255, 255));
        twilightSel.setFont(new java.awt.Font("Courier", 0, 12));
        twilightSel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "6\u00b0", "12\u00b0", "18\u00b0" }));
        twilightSel.setToolTipText("Select civil, nautical or astronomical twilight (degrees below horizon)");
        twilightSel.setFocusable(false);
        twilightSel.setPreferredSize(null);
        twilightSel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                twilightSelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 15, 0, 0);
        DayPanel.add(twilightSel, gridBagConstraints);

        setLabel.setFont(new java.awt.Font("Dialog", 1, 10));
        setLabel.setText("Set:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        DayPanel.add(setLabel, gridBagConstraints);

        setValue.setFont(new java.awt.Font("Dialog", 0, 10));
        setValue.setText("18:24");
        setValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        DayPanel.add(setValue, gridBagConstraints);

        AzLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        AzLabel.setText("Azimuth:");
        AzLabel.setToolTipText("Azimuth in degrees east of south");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 2, 0);
        DayPanel.add(AzLabel, gridBagConstraints);

        AzValue.setFont(new java.awt.Font("Dialog", 0, 10));
        AzValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        AzValue.setText("00:00:00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 2, 0);
        DayPanel.add(AzValue, gridBagConstraints);

        AltLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        AltLabel.setText("Altitude:");
        AltLabel.setToolTipText("Altitude in degrees above horizon");
        AltLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        DayPanel.add(AltLabel, gridBagConstraints);

        AltValue.setFont(new java.awt.Font("Dialog", 0, 10));
        AltValue.setText("00:00:00");
        AltValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        DayPanel.add(AltValue, gridBagConstraints);

        jPanel2.add(DayPanel);

        ActionPanel.setLayout(new java.awt.GridBagLayout());

        ActionPanel.setBackground(new java.awt.Color(255, 255, 255));
        ActionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Action"));
        ActionPanel.setMinimumSize(new java.awt.Dimension(150, 150));
        UpdateTimeButton.setText("Update Time");
        UpdateTimeButton.setToolTipText("Update Time");
        UpdateTimeButton.setFocusPainted(false);
        UpdateTimeButton.setFocusable(false);
        UpdateTimeButton.setMaximumSize(null);
        UpdateTimeButton.setMinimumSize(null);
        UpdateTimeButton.setPreferredSize(null);
        UpdateTimeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateTimeButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        ActionPanel.add(UpdateTimeButton, gridBagConstraints);

        WallClocktoggle.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroupAction.add(WallClocktoggle);
        WallClocktoggle.setFont(new java.awt.Font("Dialog", 0, 10));
        WallClocktoggle.setSelected(true);
        WallClocktoggle.setText("RT Clock");
        WallClocktoggle.setToolTipText("Set Time to the Real Time (computer's time)");
        WallClocktoggle.setFocusable(false);
        WallClocktoggle.setPreferredSize(null);
        WallClocktoggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WallClocktoggleActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        ActionPanel.add(WallClocktoggle, gridBagConstraints);

        SetDatetoggle.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroupAction.add(SetDatetoggle);
        SetDatetoggle.setFont(new java.awt.Font("Dialog", 0, 10));
        SetDatetoggle.setText("Custom Time");
        SetDatetoggle.setFocusable(false);
        SetDatetoggle.setPreferredSize(null);
        SetDatetoggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SetDatetoggleActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        ActionPanel.add(SetDatetoggle, gridBagConstraints);

        timeDateButton.setFont(new java.awt.Font("Dialog", 0, 10));
        timeDateButton.setText("Date & Time");
        timeDateButton.setFocusable(false);
        timeDateButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        timeDateButton.setMaximumSize(new java.awt.Dimension(75, 23));
        timeDateButton.setPreferredSize(null);
        timeDateButton.setEnabled(false);
        timeDateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeDateButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        ActionPanel.add(timeDateButton, gridBagConstraints);

        stepValue.setFont(new java.awt.Font("Dialog", 0, 10));
        stepValue.setText("000:00:00:00");
        stepValue.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        ActionPanel.add(stepValue, gridBagConstraints);

        pauseValue.setFont(new java.awt.Font("Dialog", 0, 10));
        pauseValue.setText("00:00:00");
        pauseValue.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        ActionPanel.add(pauseValue, gridBagConstraints);

        stepButton.setFont(new java.awt.Font("Dialog", 0, 10));
        stepButton.setText("Step:");
        stepButton.setFocusable(false);
        stepButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        stepButton.setMaximumSize(new java.awt.Dimension(75, 23));
        stepButton.setPreferredSize(null);
        stepButton.setEnabled(false);
        stepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stepButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        ActionPanel.add(stepButton, gridBagConstraints);

        pauseButton.setFont(new java.awt.Font("Dialog", 0, 10));
        pauseButton.setText("Pause:");
        pauseButton.setToolTipText("Set pause time for the main loop");
        pauseButton.setFocusable(false);
        pauseButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pauseButton.setMaximumSize(new java.awt.Dimension(75, 23));
        pauseButton.setPreferredSize(null);
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        ActionPanel.add(pauseButton, gridBagConstraints);

        loopButton.setFont(new java.awt.Font("Dialog", 0, 10));
        loopButton.setText("Loop");
        loopButton.setToolTipText("Start excution loop");
        loopButton.setFocusable(false);
        loopButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        loopButton.setMaximumSize(new java.awt.Dimension(75, 23));
        loopButton.setPreferredSize(null);
        loopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loopButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        ActionPanel.add(loopButton, gridBagConstraints);

        jPanel2.add(ActionPanel);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        mainToolBar.setRollover(true);
        dataButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jstars/images/data.gif")));
        dataButton.setToolTipText("Display Data Table");
        dataButton.setFocusable(false);
        dataButton.setMargin(new java.awt.Insets(4, 4, 4, 4));
        dataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dataButtonActionPerformed(evt);
            }
        });

        mainToolBar.add(dataButton);

        sunButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jstars/images/sun.gif")));
        sunButton.setToolTipText("Display Sun data");
        sunButton.setFocusable(false);
        sunButton.setMargin(new java.awt.Insets(4, 4, 4, 4));
        sunButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sunButtonActionPerformed(evt);
            }
        });

        mainToolBar.add(sunButton);

        moonButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jstars/images/moon.gif")));
        moonButton.setToolTipText("Display Moon data");
        moonButton.setFocusable(false);
        moonButton.setMargin(new java.awt.Insets(4, 4, 4, 4));
        moonButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moonButtonActionPerformed(evt);
            }
        });

        mainToolBar.add(moonButton);

        earthButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jstars/images/earth.gif")));
        earthButton.setToolTipText("Display Earth");
        earthButton.setFocusable(false);
        earthButton.setMargin(new java.awt.Insets(4, 4, 4, 4));
        earthButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                earthButtonActionPerformed(evt);
            }
        });

        mainToolBar.add(earthButton);

        jupiterButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jstars/images/jupiter.gif")));
        jupiterButton.setToolTipText("Display Jupiter and satellite window");
        jupiterButton.setFocusable(false);
        jupiterButton.setMargin(new java.awt.Insets(4, 4, 4, 4));
        jupiterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jupiterButtonActionPerformed(evt);
            }
        });

        mainToolBar.add(jupiterButton);

        getContentPane().add(mainToolBar, java.awt.BorderLayout.NORTH);

        jMenuBar1.setMaximumSize(null);
        jMenuBar1.setMinimumSize(null);
        jMenuBar1.setPreferredSize(null);
        FileMenu.setText("File");
        jMenuItemOpen.setFont(new java.awt.Font("Dialog", 0, 12));
        jMenuItemOpen.setMnemonic('o');
        jMenuItemOpen.setText("Open");
        jMenuItemOpen.setEnabled(false);
        jMenuItemOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenActionPerformed(evt);
            }
        });

        FileMenu.add(jMenuItemOpen);

        jMenuItemSave.setFont(new java.awt.Font("Dialog", 0, 12));
        jMenuItemSave.setMnemonic('s');
        jMenuItemSave.setText("Save");
        jMenuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveActionPerformed(evt);
            }
        });

        FileMenu.add(jMenuItemSave);

        jMenuItemSaveAs.setFont(new java.awt.Font("Dialog", 0, 12));
        jMenuItemSaveAs.setText("Save As");
        jMenuItemSaveAs.setEnabled(false);
        FileMenu.add(jMenuItemSaveAs);

        jMenuItemExit.setFont(new java.awt.Font("Dialog", 0, 12));
        jMenuItemExit.setMnemonic('x');
        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuExit(evt);
            }
        });

        FileMenu.add(jMenuItemExit);

        jMenuBar1.add(FileMenu);

        OptionsMenu.setText("Options");
        prefMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        prefMenu.setMnemonic('P');
        prefMenu.setText("Preferences");
        prefMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prefMenuActionPerformed(evt);
            }
        });

        OptionsMenu.add(prefMenu);

        timeMenu.setText("Display time");
        timeMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        buttonGroupTime.add(LTMenuItem);
        LTMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
        LTMenuItem.setSelected(true);
        LTMenuItem.setText("Local Time");
        LTMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LTMenuItemActionPerformed(evt);
            }
        });

        timeMenu.add(LTMenuItem);

        buttonGroupTime.add(UTCMenuItem);
        UTCMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
        UTCMenuItem.setText("UTC");
        UTCMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UTCMenuItemActionPerformed(evt);
            }
        });

        timeMenu.add(UTCMenuItem);

        OptionsMenu.add(timeMenu);

        locationMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        locationMenu.setText("Change Location");
        locationMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationMenuActionPerformed(evt);
            }
        });

        OptionsMenu.add(locationMenu);

        toolBarMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
        toolBarMenuItem.setSelected(true);
        toolBarMenuItem.setText("Tool Bar");
        toolBarMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBarMenuItemActionPerformed(evt);
            }
        });

        OptionsMenu.add(toolBarMenuItem);

        LookMenu.setText("Look & Feel");
        LookMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        OptionsMenu.add(LookMenu);

        jMenuBar1.add(OptionsMenu);

        ViewMenu.setText("Views");
        DataWinItem.setFont(new java.awt.Font("Dialog", 0, 12));
        DataWinItem.setMnemonic('d');
        DataWinItem.setText("Data Table");
        DataWinItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DataWinItemActionPerformed(evt);
            }
        });

        ViewMenu.add(DataWinItem);

        SunItem.setFont(new java.awt.Font("Dialog", 0, 12));
        SunItem.setMnemonic('s');
        SunItem.setText("Sun");
        SunItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SunItemActionPerformed(evt);
            }
        });

        ViewMenu.add(SunItem);

        MoonWinItem.setFont(new java.awt.Font("Dialog", 0, 12));
        MoonWinItem.setMnemonic('m');
        MoonWinItem.setText("Moon");
        MoonWinItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MoonWinItemActionPerformed(evt);
            }
        });

        ViewMenu.add(MoonWinItem);

        JupiterWinItem.setFont(new java.awt.Font("Dialog", 0, 12));
        JupiterWinItem.setMnemonic('j');
        JupiterWinItem.setText("Jupiter");
        JupiterWinItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JupiterWinItemActionPerformed(evt);
            }
        });

        ViewMenu.add(JupiterWinItem);

        earthWinItem.setFont(new java.awt.Font("Dialog", 0, 12));
        earthWinItem.setMnemonic('e');
        earthWinItem.setText("Earth");
        earthWinItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                earthWinItemActionPerformed(evt);
            }
        });

        ViewMenu.add(earthWinItem);

        jMenuBar1.add(ViewMenu);

        helpMenu.setText("Help");
        splashItem.setFont(new java.awt.Font("Dialog", 0, 12));
        splashItem.setText("Splash Screen");
        splashItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                splashItemActionPerformed(evt);
            }
        });

        helpMenu.add(splashItem);

        aboutItem.setFont(new java.awt.Font("Dialog", 0, 12));
        aboutItem.setText("About");
        aboutItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutItemActionPerformed(evt);
            }
        });

        helpMenu.add(aboutItem);

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }// </editor-fold>//GEN-END:initComponents
    
    private void earthButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_earthButtonActionPerformed
        if(earthButton.isSelected()) {
            if(earthWindow == null) {
                earthWindow = new earthWin(libastro, earthButton);
            }
            earthWindow.updateDisp();
            earthWindow.pack();
            earthWindow.setVisible(true);
        }
        else {
            if(earthWindow != null) {
                earthWindow.dispose();
            }
        }
    }//GEN-LAST:event_earthButtonActionPerformed
    
    private void earthWinItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_earthWinItemActionPerformed
        if(!earthButton.isSelected()) {
            if(earthWindow == null) {
                earthWindow = new earthWin(libastro, earthButton);
            }
            earthButton.setSelected(true);
        }
        earthWindow.updateDisp();
        earthWindow.pack();
        earthWindow.setVisible(true);
    }//GEN-LAST:event_earthWinItemActionPerformed
    
    private void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveActionPerformed
        saveSettings(true);
    }//GEN-LAST:event_jMenuItemSaveActionPerformed
    
    private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenActionPerformed
        
    }//GEN-LAST:event_jMenuItemOpenActionPerformed
    
    private void toolBarMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBarMenuItemActionPerformed
        if(toolBarMenuItem.isSelected()) {
            mainToolBar.setVisible(true);
        } else {
            mainToolBar.setVisible(false);
        }
        this.pack();
    }//GEN-LAST:event_toolBarMenuItemActionPerformed
    
    private void JupiterWinItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JupiterWinItemActionPerformed
        if(!jupiterButton.isSelected()) {
            if(jupiterWindow == null) {
                jupiterWindow = new jupiterWin(libastro, jupiterButton);
            }
            jupiterButton.setSelected(true);
        }
        libastro.updateAll(ephemObject.JUPITER);
        jupiterWindow.SetTimeMode(libastro.getTZdisp());
        jupiterWindow.updateDisp();
        jupiterWindow.pack();
        jupiterWindow.setVisible(true);
        
    }//GEN-LAST:event_JupiterWinItemActionPerformed
    
    private void MoonWinItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MoonWinItemActionPerformed
        if(!moonButton.isSelected()) {
            if(moonWindow == null) {
                moonWindow = new moonWin(libastro, moonButton);
            }
            libastro.updateAll(ephemObject.MOON);
            moonButton.setSelected(true);
        }
        moonWindow.SetTimeMode(libastro.getTZdisp());
        moonWindow.updateDisp();
        moonWindow.pack();
        moonWindow.setVisible(true);
        
    }//GEN-LAST:event_MoonWinItemActionPerformed
    
    private void SunItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SunItemActionPerformed
        if(!sunButton.isSelected()) {
            if(sunWindow == null) {
                sunWindow = new sunWin((jstars.util.Jsun) libastro.SolSys[ephemObject.SUN], sunButton);
            }
            sunWindow.SetTimeMode(libastro.getTZdisp());
            sunWindow.updateDisp();
            sunWindow.pack();
            sunWindow.setVisible(true);
            sunButton.setSelected(true);
        }
        sunWindow.SetTimeMode(libastro.getTZdisp());
        sunWindow.updateDisp();
        sunWindow.pack();
        sunWindow.setVisible(true);
    }//GEN-LAST:event_SunItemActionPerformed
    
    private void DataWinItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DataWinItemActionPerformed
        if(!dataButton.isSelected()) {
            if(dataWindow == null) {
                dataWindow = new dataWin(libastro, dataButton);
            }
            else {
                dataWindow.SetTimeMode(libastro.getTZdisp());
                dataWindow.updateDisp();
            }
            dataButton.setSelected(true);
        }
        dataWindow.pack();
        dataWindow.setVisible(true);
    }//GEN-LAST:event_DataWinItemActionPerformed
    
    private void locationMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationMenuActionPerformed
        if(dialog == null)
            dialog = new jstars.gui.LocationDialog(this, false, libastro.userData);
        dialog.setVisible(true);
    }//GEN-LAST:event_locationMenuActionPerformed
    
    private void jupiterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jupiterButtonActionPerformed
        if(jupiterButton.isSelected()) {
            if(jupiterWindow == null) {
                jupiterWindow = new jupiterWin(libastro, jupiterButton);
            }
            libastro.updateAll(ephemObject.JUPITER);
            jupiterWindow.SetTimeMode(libastro.getTZdisp());
            jupiterWindow.updateDisp();
            jupiterWindow.pack();
            jupiterWindow.setVisible(true);
        }
        else {
            if(jupiterWindow != null) {
                jupiterWindow.dispose();
            }
        }
    }//GEN-LAST:event_jupiterButtonActionPerformed
    
    private void dataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dataButtonActionPerformed
        if(dataButton.isSelected()) {
            if(dataWindow == null) {
                dataWindow = new dataWin(libastro, dataButton);
            }
            else {
                
            }
            dataWindow.SetTimeMode(libastro.getTZdisp());
            dataWindow.updateDisp();
            dataWindow.pack();
            dataWindow.setVisible(true);
        }
        else {
            if(dataWindow != null) {
                dataWindow.dispose();
            }
        }
    }//GEN-LAST:event_dataButtonActionPerformed
    
    private void moonButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moonButtonActionPerformed
        if(moonButton.isSelected()) {
            if(moonWindow == null) {
                moonWindow = new moonWin(libastro, moonButton);
            }
            libastro.updateAll(ephemObject.MOON);
            moonWindow.SetTimeMode(libastro.getTZdisp());
            moonWindow.updateDisp();
            moonWindow.pack();
            moonWindow.setVisible(true);
        }
        else {
            if(moonWindow != null) {
                moonWindow.dispose();
            }
        }
    }//GEN-LAST:event_moonButtonActionPerformed
    
    private void sunButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sunButtonActionPerformed
        if(sunButton.isSelected()) {
            if(sunWindow == null) {
                sunWindow = new sunWin((jstars.util.Jsun) libastro.SolSys[ephemObject.SUN], sunButton);
            }
            sunWindow.SetTimeMode(libastro.getTZdisp());
            sunWindow.updateDisp();
            sunWindow.pack();
            sunWindow.setVisible(true);
        }
        else {
            if(sunWindow != null) {
                sunWindow.dispose();
            }
        }
    }//GEN-LAST:event_sunButtonActionPerformed
    
    private void UTCMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UTCMenuItemActionPerformed
        libastro.timeData.setDispLT(false);
        libastro.updateTime();
        updateFrames();
    }//GEN-LAST:event_UTCMenuItemActionPerformed
    
    private void LTMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LTMenuItemActionPerformed
        this.libastro.timeData.setDispLT(true);
        libastro.updateTime();
        updateFrames();
    }//GEN-LAST:event_LTMenuItemActionPerformed
    
    private void loopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loopButtonActionPerformed
        pauseButton.setEnabled(false);
        stepButton.setEnabled(false);
        WallClocktoggle.setEnabled(false);
        SetDatetoggle.setEnabled(false);
        timeDateButton.setEnabled(false);
        loopButton.setEnabled(false);
        UpdateTimeButton.setText("Stop Looping");
        // Start timer
        timer = new java.util.Timer();
        timer.schedule(new loopTimer(), 0, pause.getTimeInSec()* 1000);
        
    }//GEN-LAST:event_loopButtonActionPerformed
    
    private void splashItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_splashItemActionPerformed
        new Thread((new JStarsGUI.dispSplash())).start();
    }//GEN-LAST:event_splashItemActionPerformed
    
    private void stepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stepButtonActionPerformed
        customDate = libastro.timeData.copyLocalTime();
        JDialog timeDialog = new CustomTimeDialog(this,true, step, null, "Enter Step Interval Between Updates", true);
        timeDialog.setVisible(true);
        updateFrames();
    }//GEN-LAST:event_stepButtonActionPerformed
    
    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseButtonActionPerformed
        customDate = libastro.timeData.copyLocalTime();
        JDialog timeDialog = new CustomTimeDialog(this,true, pause, null, "Enter Pause Interval Between Updates", false);
        timeDialog.setVisible(true);
        updateFrames();
    }//GEN-LAST:event_pauseButtonActionPerformed
    
    private void timeDateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeDateButtonActionPerformed
        JDialog timeDialog = new CustomTimeDialog(this,true, customDate, libastro.getTZdisp(), null, true);
        timeDialog.setVisible(true);
        libastro.setUserDate(this.customDate, true);
        libastro.updateLocation();
        updateLoc();
        updateFrames();
    }//GEN-LAST:event_timeDateButtonActionPerformed
    
    private void SetDatetoggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SetDatetoggleActionPerformed
        RTclock = false;
        timeDateButton.setEnabled(true);
        stepButton.setEnabled(true);
        stepValue.setEnabled(true);
        if(customDate == null)
            customDate = libastro.timeData.copyLocalTime();
        libastro.setUserDate(this.customDate, true);
        libastro.updateLocation();
        updateLoc();
        updateFrames();
    }//GEN-LAST:event_SetDatetoggleActionPerformed
    
    private void WallClocktoggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WallClocktoggleActionPerformed
        RTclock = true;
        timeDateButton.setEnabled(false);
        stepButton.setEnabled(false);
        stepValue.setEnabled(false);
        updateTime();
        libastro.updateLocation();
        updateLoc();
        updateFrames();
    }//GEN-LAST:event_WallClocktoggleActionPerformed
    
    private void prefMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prefMenuActionPerformed
        boolean ampmsave = libastro.timeData.isHourAMPM();
        if(preference == null)
            preference = new jstars.gui.preferenceGUI(this, true, libastro.userData, libastro.timeData);
        preference.setLocationRelativeTo(this);
        preference.setVisible(true);
        preference.dispose();
        preference = null;
        if(libastro.timeData.isDispLT() == true) {
            UTCMenuItem.setSelected(false);
            LTMenuItem.setSelected(true);
        }
        else {
            UTCMenuItem.setSelected(true);
            LTMenuItem.setSelected(false);
        }
        libastro.updateLocation();
        updateLoc();
        updateTime();
        if(ampmsave != libastro.timeData.isHourAMPM()) {
            repack_all();
        }
    }//GEN-LAST:event_prefMenuActionPerformed
    
    private void aboutItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutItemActionPerformed
        String msg = "jstars " + version + "\n\n©2004 Florent Charpin\nemail: jstars@florent.us\nhttp://jstars.sourceforge.net";
        javax.swing.JOptionPane.showMessageDialog(null, msg);
    }//GEN-LAST:event_aboutItemActionPerformed
    
    private void twilightSelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_twilightSelActionPerformed
        updateFrames();
    }//GEN-LAST:event_twilightSelActionPerformed
    
    private void locationwindow(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationwindow
        if(dialog == null)
            dialog = new jstars.gui.LocationDialog(this, false, libastro.userData);
        dialog.setVisible(true);
        
    }//GEN-LAST:event_locationwindow
    
    private void UpdateTimeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateTimeButtonActionPerformed
        if(evt.getActionCommand().equals("Stop Looping")) {
            pauseButton.setEnabled(true);
            WallClocktoggle.setEnabled(true);
            SetDatetoggle.setEnabled(true);
            loopButton.setEnabled(true);
            UpdateTimeButton.setText("Update Time");
            if(SetDatetoggle.isSelected()) {
                stepButton.setEnabled(true);
                timeDateButton.setEnabled(true);
            }
            timer.cancel();
            timer = null;
        }
        else {
            this.updateTime();
        }
    }//GEN-LAST:event_UpdateTimeButtonActionPerformed
    
    private void MenuExit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuExit
        saveSettingsIfChanged();
        System.exit(0);
    }//GEN-LAST:event_MenuExit
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        saveSettingsIfChanged();
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        new Thread(new JStarsGUI.dispSplash()).start();
        try {
            java.lang.Thread.sleep(5500);
        }
        catch(java.lang.InterruptedException ex) {
        }
        JStarsGUI mainscreen = new JStarsGUI();
        mainscreen.setVisible(true);
    }
    
    protected void loadSettings(boolean _default) {
        String line;
        String prop, value;
        int pos;
        java.io.File inputFile = new java.io.File(System.getProperty("user.home") + "/.jstars/config");
        if(inputFile.canRead()) {
            try {
                java.io.BufferedReader userconfig = new java.io.BufferedReader(new java.io.FileReader(inputFile));
                while((line = userconfig.readLine()) != null) {
                    pos = findFirstCol(line);
                    prop = line.substring(0, pos);
                    value = line.substring(pos + 1);
                    if(prop.equals("LocalTime")) {
                        if(value.equals("true"))
                            this.libastro.timeData.setDispLT(true);
                        else {
                            this.libastro.timeData.setDispLT(false);
                        }
                        if(libastro.timeData.isDispLT() == true) {
                            UTCMenuItem.setSelected(false);
                            LTMenuItem.setSelected(true);
                        }
                        else {
                            UTCMenuItem.setSelected(true);
                            LTMenuItem.setSelected(false);
                        }
                    }
                    else if(prop.equals("TimeAMPM")) {
                        if(value.equals("true"))
                            this.libastro.timeData.setHourAMPM(true);
                        else
                            this.libastro.timeData.setHourAMPM(false);
                    }
                    else if(prop.equals("Location")) {
                        this.libastro.userData.name = value;
                    }
                    else if(prop.equals("Latitude")) {
                        this.libastro.userData.setLatitude(libastro.fromHHMMSS(value, 90));
                        
                    }
                    else if(prop.equals("Longitude")) {
                        this.libastro.userData.setLongitude(libastro.fromHHMMSS(value, 180));
                    }
                    else if(prop.equals("TimeZone")) {
                        this.libastro.userData.setLocalTZ(value);
                    }
                }
            } catch(java.io.IOException exp) {
                System.out.println("Cannot save config!");
            }
        }
    }
    
    private int findFirstCol(String str) {
        for(int i=0; i<str.length(); i++) {
            if(str.charAt(i) == ':')
                return i;
        }
        return str.length();
    }
    
    protected void saveSettingsIfChanged() {
        saveSettings(true);
    }
    
    protected void saveSettings(boolean _default) {
        //check if dir exist
        java.io.File outputFile = new java.io.File(System.getProperty("user.home") + "/.jstars");
        if(!outputFile.exists()) {
            // create dir
            if(!outputFile.mkdir()) {
                System.out.println("Cannot save configuration file " + System.getProperty("user.home") + "/.jstars");
                return;
            }
        }
        if(outputFile.isDirectory()) {
            outputFile = new java.io.File(System.getProperty("user.home") + "/.jstars/config");
        }
        try {
            java.io.FileWriter userconfig = new java.io.FileWriter(outputFile);
            userconfig.write("jstars:" + JStarsGUI.version + "\n");
            userconfig.write("LocalTime:" + java.lang.Boolean.toString(libastro.timeData.isDispLT()) + "\n");
            userconfig.write("TimeAMPM:" + java.lang.Boolean.toString(libastro.timeData.isHourAMPM()) + "\n");
            userconfig.write("Location:" + libastro.userData.name + "\n");
            userconfig.write("Latitude:" + libastro.userData.latString() + "\n");
            userconfig.write("Longitude:" + libastro.userData.lonString() + "\n");
            userconfig.write("TimeZone:" + libastro.userData.getLocalTZ() + "\n");
            userconfig.close();
        } catch(java.io.IOException exp) {
            System.out.println("Cannot save config!");
        }
        
    }
    
    void updateTime() {
        if(RTclock) {
            // Set to wallclock time
            libastro.setUserDateToWallClock();
        }
        else {
            customDate.add(step);
            libastro.setUserDate(this.customDate, true);
            libastro.updateLocation();
            updateLoc();
        }
        updateFrames();
    }
    
    void updateFrames() {
        final java.text.DecimalFormat JDFormatter = new java.text.DecimalFormat("#########.00000");
        
        // Time frame
        LocalDateValue.setText(libastro.timeData.getLocalDateString());
        LocalTimeValue.setText(libastro.timeData.getLocalTimeString());
        UTDateValue.setText(libastro.timeData.getUTDateString());
        UTTimeValue.setText(libastro.timeData.getUTTime());
        
        JDValue.setText(JDFormatter.format(libastro.julianDay()));
        sideralValue.setText(libastro.timeData.getLocalSideral());
        LSTValue.setText(libastro.timeData.getLocalSideral0_LT());
        DeltaTValue.setText(libastro.timeData.getDeltaT());
        
        // Sun frame
        libastro.updateAll(ephemObject.SUN);
        riseValue.setText(libastro.SolSys[ephemObject.SUN].riseTimeString());
        transitValue.setText(libastro.SolSys[ephemObject.SUN].transTimeString());
        setValue.setText(libastro.SolSys[ephemObject.SUN].setTimeString());
        dayLengthValue.setText(libastro.SolSys[ephemObject.SUN].TimeUpString());
        String tt[] = null;
        if("18°".equals((String) twilightSel.getSelectedItem())) {
            tt = libastro.getTwilightTime(-18, true);
        }
        if("12°".equals((String) twilightSel.getSelectedItem())) {
            tt = libastro.getTwilightTime(-12, true);
        }
        if("6°".equals((String) twilightSel.getSelectedItem())) {
            tt = libastro.getTwilightTime(-6, true);
        }
        else {
            
        }
        if(tt != null) {
            dawnValue.setText(tt[0]);
            duskValue.setText(tt[1]);
        }
        
        AzValue.setText(libastro.SolSys[ephemObject.SUN].getAzString());
        AltValue.setText(libastro.SolSys[ephemObject.SUN].getAltString());
        
        if(libastro.timeData.isDispLT())
            timeModeLabel.setText("Time Mode: Local Time - " + libastro.getCurrentTZName());
        else
            timeModeLabel.setText("Time Mode: UTC");
        
        // Action Frame
        stepValue.setText(step.dispDDDHHMMSS());
        pauseValue.setText(pause.dispTimeHHMMSS());
        
        // Update external windows
        updateSunWin();
        updateMoonWin();
        updateDataWin();
        updateJupiterWin();
        updateEarthWin();
        
    }
    
    void updateEarthWin() {
        if(earthWindow == null)
            return;
        if(!earthWindow.isShowing()) {
            jupiterButton.setSelected(false);
            return;
        }
        libastro.updateAll(ephemObject.JUPITER);
        earthWindow.updateDisp();
        //earthWindow.SetTimeMode(libastro.getTZdisp());
        
    }
    
    void updateJupiterWin() {
        if(jupiterWindow == null)
            return;
        if(!jupiterWindow.isShowing()) {
            jupiterButton.setSelected(false);
            return;
        }
        libastro.updateAll(ephemObject.JUPITER);
        jupiterWindow.updateDisp();
        jupiterWindow.SetTimeMode(libastro.getTZdisp());
        
    }
    
    void updateMoonWin() {
        if(moonWindow == null)
            return;
        if(!moonWindow.isShowing()) {
            moonButton.setSelected(false);
            return;
        }
        libastro.updateAll(ephemObject.MOON);
        moonWindow.updateDisp();
        moonWindow.SetTimeMode(libastro.getTZdisp());
        
    }
    
    void updateSunWin() {
        if(sunWindow == null)
            return;
        if(!sunWindow.isShowing()) {
            sunButton.setSelected(false);
            return;
        }
        sunWindow.updateDisp();
        sunWindow.SetTimeMode(libastro.getTZdisp());
    }
    
    void updateDataWin() {
        if(dataWindow == null)
            return;
        if(!dataWindow.isShowing()) {
            dataButton.setSelected(false);
            return;
        }
        dataWindow.updateDisp();
        dataWindow.SetTimeMode(libastro.getTZdisp());
    }
    
    void updateLoc() {
        LocationButton.setText(libastro.userData.name);
        TZValue.setText(libastro.getCurrentTZName());
        TZOffValue.setText(libastro.timeData.getTZOffsetString());
        LatitudeValue.setText(libastro.userData.latString());
        LonValue.setText(libastro.userData.lonString());
        if(libastro.getDST())
            DSTValue.setText("Yes");
        else
            DSTValue.setText("No");
        this.EquiValue.setText(java.lang.Double.toString(libastro.userData.getEquinox()));
    }
    
    void repack_all() {
        if(dialog != null)
            dialog.pack();
        if(sunWindow != null)
            sunWindow.pack();
        if(moonWindow != null)
            moonWindow.pack();
        if(dataWindow != null)
            dataWindow.adjustDisp();
        pack();
    }
    
    class loopTimer extends java.util.TimerTask {
        @Override
        public void run() {
            javax.swing.SwingUtilities.invokeLater(updateFromLoop);
            
        }
    }
    
    
    
    private static class splashlabel extends javax.swing.JLabel {
        
        private double _scale = 0.0;
        private float _alpha = 0.0f;
        boolean done = false;
        String s1 = new String("JStars");
        String s2 = new String("http://jstars.sourceforge.net");
        Font f1, f2, f3, f4;
        
        
        splashlabel(javax.swing.ImageIcon arg) {
            super(arg);
            f1 = new Font("Times", Font.ITALIC, 96);
            f2 = new Font("Times", Font.BOLD, 28);
            f3 = new Font("Times", Font.PLAIN, 18);
            f4 = new Font("Times", Font.ITALIC, 18);
        }
        
        @Override
        public void paint(java.awt.Graphics g) {
            super.paint(g);
            if((_scale >= 1.0) && (_alpha >= 1.0)) {
                done = true;
            }
            if(_scale == 0.0)
                return;
            _scale += 0.07;
            if(_scale > 1.0 )
                _scale = 1.0;
            
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setStroke(new java.awt.BasicStroke(3));
            java.awt.font.FontRenderContext frc = g2.getFontRenderContext();
            TextLayout tl = new TextLayout(s1, f1, frc);
            Dimension theSize = getSize();
            theSize.width /= _scale;
            theSize.height /= _scale;
            g2.setColor(Color.red);
            
            float sw = (float) tl.getBounds().getWidth();
            java.awt.geom.AffineTransform transform = new java.awt.geom.AffineTransform();
            transform.scale(_scale, _scale * _scale * _scale);
            transform.translate(500 / 2 - sw / 2, 401 / 2);
            
            java.awt.Shape shape = tl.getOutline(transform);
            
            java.awt.Rectangle r = shape.getBounds();
            g2.setColor(Color.white);
            g2.draw(shape);
            g2.setStroke(new java.awt.BasicStroke(1));
            g2.setColor(Color.black);
            g2.draw(shape);
            
            _alpha += 0.02f;
            if(_alpha >= 1.0) {
                _alpha = 1.0f;
            }
            
            g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, _alpha));
            g2.setColor(Color.red);
            
            g2.setFont(f2);
            tl = new TextLayout(JStarsGUI.version, f2, frc);
            sw = (int) tl.getBounds().getWidth();
            tl.draw(g2,500 / 2 - sw / 2,300);
            
            g2.setColor(Color.white);
            tl = new TextLayout(JStarsGUI.copyright, f3, frc);
            sw = (int) tl.getBounds().getWidth();
            tl.draw(g2,500 / 2 - sw / 2,370);
            
            g2.setColor(Color.white);
            tl = new TextLayout(s2, f4, frc);
            sw = (int) tl.getBounds().getWidth();
            tl.draw(g2,500 / 2 - sw / 2,390);
        }
    }
    
    
    static private class dispSplash implements Runnable {
        
        @Override
        public void run() {
            java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
            javax.swing.JWindow splash = new javax.swing.JWindow();
            java.net.URL picURL = getClass().getResource("/jstars/images/splash.jpg");
            splashlabel slabel = new splashlabel(new javax.swing.ImageIcon(picURL));
            slabel.setBorder(javax.swing.BorderFactory.createRaisedBevelBorder());
            splash.getContentPane().add(slabel, java.awt.BorderLayout.CENTER);
            Dimension scrn = toolkit.getScreenSize();
            Dimension labelSize = slabel.getPreferredSize();
            
            splash.setLocation(scrn.width/2 - (labelSize.width/2),
            scrn.height/2 - (labelSize.height/2));
            splash.pack();
            splash.setVisible(true);
            
            try {
                java.lang.Thread.sleep(250);
            }
            catch (java.lang.InterruptedException ex) {
            }
            slabel._scale = 0.01;
            slabel.done = false;
            while(slabel.done == false) {
                slabel.repaint(0);
                try {
                    java.lang.Thread.sleep(20);
                }
                catch (java.lang.InterruptedException ex) {
                }
            }
            try {
                java.lang.Thread.sleep(1000);
            }
            catch(java.lang.InterruptedException ex) {
            }
            
            splash.dispose();
            
        }
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ActionPanel;
    private javax.swing.JLabel AltLabel;
    private javax.swing.JLabel AltValue;
    private javax.swing.JLabel AzLabel;
    private javax.swing.JLabel AzValue;
    private javax.swing.JLabel DSTLabel;
    private javax.swing.JLabel DSTValue;
    private javax.swing.JMenuItem DataWinItem;
    private javax.swing.JPanel DayPanel;
    private javax.swing.JLabel DeltaTLabel;
    private javax.swing.JLabel DeltaTValue;
    private javax.swing.JLabel EquiLabel;
    private javax.swing.JLabel EquiValue;
    private javax.swing.JMenu FileMenu;
    private javax.swing.JLabel JDLabel;
    private javax.swing.JLabel JDValue;
    private javax.swing.JMenuItem JupiterWinItem;
    private javax.swing.JLabel LSTLabel;
    private javax.swing.JLabel LSTValue;
    private javax.swing.JRadioButtonMenuItem LTMenuItem;
    private javax.swing.JLabel LatitudeLabel;
    private javax.swing.JLabel LatitudeValue;
    private javax.swing.JLabel LocalDateValue;
    private javax.swing.JLabel LocalTime;
    private javax.swing.JLabel LocalTimeValue;
    private javax.swing.JButton LocationButton;
    private javax.swing.JPanel LocationPanel;
    private javax.swing.JLabel LonValue;
    private javax.swing.JLabel LongitudeLabel;
    private javax.swing.JMenu LookMenu;
    private javax.swing.JMenuItem MoonWinItem;
    private javax.swing.JMenu OptionsMenu;
    private javax.swing.JRadioButton SetDatetoggle;
    private javax.swing.JMenuItem SunItem;
    private javax.swing.JLabel TZOffLabel;
    private javax.swing.JLabel TZOffValue;
    private javax.swing.JLabel TZValue;
    private javax.swing.JLabel TZlabel;
    private javax.swing.JPanel TimePanel;
    private javax.swing.JLabel TransitLabel;
    private javax.swing.JRadioButtonMenuItem UTCMenuItem;
    private javax.swing.JLabel UTDateLabel;
    private javax.swing.JLabel UTDateValue;
    private javax.swing.JLabel UTTimeLabel;
    private javax.swing.JLabel UTTimeValue;
    private javax.swing.JButton UpdateTimeButton;
    private javax.swing.JMenu ViewMenu;
    private javax.swing.JRadioButton WallClocktoggle;
    private javax.swing.JMenuItem aboutItem;
    private javax.swing.ButtonGroup buttonGroupAction;
    private javax.swing.ButtonGroup buttonGroupLF;
    private javax.swing.ButtonGroup buttonGroupTime;
    private javax.swing.JToggleButton dataButton;
    private javax.swing.JLabel dawnLabel;
    private javax.swing.JLabel dawnValue;
    private javax.swing.JLabel dayLengthLabel;
    private javax.swing.JLabel dayLengthValue;
    private javax.swing.JLabel duskLabel;
    private javax.swing.JLabel duskValue;
    private javax.swing.JToggleButton earthButton;
    private javax.swing.JMenuItem earthWinItem;
    private javax.swing.JLabel elLabel;
    private javax.swing.JLabel elValue;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemOpen;
    private javax.swing.JMenuItem jMenuItemSave;
    private javax.swing.JMenuItem jMenuItemSaveAs;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToggleButton jupiterButton;
    private javax.swing.JLabel localDateLabel;
    private javax.swing.JMenuItem locationMenu;
    private javax.swing.JButton loopButton;
    private javax.swing.JToolBar mainToolBar;
    private javax.swing.JToggleButton moonButton;
    private javax.swing.JButton pauseButton;
    private javax.swing.JLabel pauseValue;
    private javax.swing.JMenuItem prefMenu;
    private javax.swing.JLabel riseLabel;
    private javax.swing.JLabel riseValue;
    private javax.swing.JLabel setLabel;
    private javax.swing.JLabel setValue;
    private javax.swing.JLabel sideralLabel;
    private javax.swing.JLabel sideralValue;
    private javax.swing.JMenuItem splashItem;
    private javax.swing.JButton stepButton;
    private javax.swing.JLabel stepValue;
    private javax.swing.JToggleButton sunButton;
    private javax.swing.JButton timeDateButton;
    private javax.swing.JMenu timeMenu;
    private javax.swing.JLabel timeModeLabel;
    private javax.swing.JCheckBoxMenuItem toolBarMenuItem;
    private javax.swing.JLabel transitValue;
    private javax.swing.JComboBox twilightSel;
    // End of variables declaration//GEN-END:variables
    
}

