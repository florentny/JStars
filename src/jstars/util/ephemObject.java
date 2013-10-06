/*
 * $Id: ephemObject.java,v 1.8 2004/06/26 22:58:05 fc Exp $
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
 *
 */


package jstars.util;

/**
 * Base class for all Solar System bodies
 * @author Florent Charpin
 */


public class ephemObject {
    public final static int SUN = 0;
    public final static int MERCURY = 1;
    public final static int VENUS = 2;
    public final static int EARTH = 3;
    public final static int MARS = 4;
    public final static int JUPITER = 5;
    public final static int SATURN = 6;
    public final static int URANUS = 7;
    public final static int NEPTUNE = 8;
    public final static int PLUTO = 9;
    public final static int MOON = 10;
    //11 to 30 reserved for user object
    public final static int star = 1000;
    
    int objType = -1;
    Location loc;
    public Jlibastro.TimeData loctime;
    double h0;
    
    public double RA = 0.0;
    public double Dec = 0.0;
    
    public double Az = 0.0;
    public double Alt = 0.0;
    
    public double eclLongitude = 0.0;
    public double eclLatitude = 0.0;
    
    public double earthDistance = 0.0;
    public double sunDistance = 0.0;
    
    public double riseAz = 0.0;
    public double setAz = 0.0;
    public double transAlt = 0.0;
    
    double RA_i = 0.0;
    double Dec_i = 0.0;
    
    // time in local or UTC.
    public Jlibastro.Jdate rise;
    public Jlibastro.Jdate trans;
    public Jlibastro.Jdate set;
    // time in UTC
    double drise, dtrans, dset;
    double timeUp;
    
    double lastCall = 0.0;
    
    
    public ephemObject(Location loc, Jlibastro.TimeData loctime) {
        this.loc = loc;
        this.loctime = loctime;
        h0 = -0.009890199095;
    }
    
    public String getEarthDistanceString() {
        if(earthDistance == 0.0)
            return " ";
        String ret = String.valueOf(earthDistance);
        if(ret.length() > 7)
            return ret.substring(0,7) +  " a.u.";
        else
            return ret +  " a.u.";
    }
    
    /**
     *
     * @return
     */    
    public String getSunDistanceString() {
        if(sunDistance == 0.0)
            return " ";
        String ret = String.valueOf(sunDistance);
        if(ret.length() > 7)
            return ret.substring(0,7) +  " a.u.";
        else
            return ret +  " a.u.";
    }
    
    /**
     *
     * @return
     */    
    public String getEcLonString() {
        return Jlibastro.dispHHMMSS(eclLongitude);
    }
    
    /**
     *
     * @return
     */    
    public String getEcLatString() {
        return Jlibastro.dispHHMMSS(eclLatitude);
    }
    
    /**
     *
     * @return
     */    
    public String getRAString() {
        return Jlibastro.dispHHMMSS_SS(RA);
    }
    
    /**
     *
     * @return
     */    
    public String getDecString() {
        return Jlibastro.dispHHMMSS_SS(Dec);
    }
    
    public String getAzString() {
        return Jlibastro.dispHHMMSS(Az);
    }
    
    public String getAltString() {
        return Jlibastro.dispHHMMSS(Alt);
    }
    
    public String getRiseAzString() {
        return Jlibastro.dispHHMMSS(riseAz);
    }
    
    public String getSetAzString() {
        return Jlibastro.dispHHMMSS(setAz);
    }
    
    public String getTransAltString() {
        return Jlibastro.dispHHMMSS(transAlt);
    }
    
    /**
     *
     * @return
     */    
    public double getRadRA() {
        return Math.toRadians(RA / 24.0 * 360.0);
    }
    
    
    public void calculatePosition() {
        
    }
    
    public void UpdateAll() {
        if(rise == null)
            rise = new Jlibastro.Jdate();
        if(trans == null)
            trans = new Jlibastro.Jdate();
        if(set == null)
            set = new Jlibastro.Jdate();
        
        calculatePosition();
        
        // Don't need to recalculate RTS if same day and location
        if(true) {
            double num[] = calculateRTS(RA, Dec);
            dtrans = num[0];
            drise = num[1];
            dset = num[2];
            if(loctime.isDispLT()) {
                num[0] = Jlibastro.to24(num[0] + loctime.tzOffset);
                num[1] = Jlibastro.to24(num[1] + loctime.tzOffset);
                num[2] = Jlibastro.to24(num[2] + loctime.tzOffset);
            }
            else {
                num[0] = Jlibastro.to24(num[0]);
                num[1] = Jlibastro.to24(num[1]);
                num[2] = Jlibastro.to24(num[2]);
            }
            trans.setTimeFromDouble(num[0]);
            rise.setTimeFromDouble(num[1]);
            set.setTimeFromDouble(num[2]);
            timeUp = dset - drise;
            if(timeUp < 0.0)
                timeUp = 24.0 + timeUp;
        }
        
        calculateAzAlt();
    }
    
    void calculateAzAlt() {
        double H = loctime.localSideralTime.getTimeInDegree(true) - (RA / 24.0 * 360.0);
        double num[] = Jlibastro.AzAlt(RA, Dec, H, loc.latitude, loc.longitude);
        Az = num[0];
        Alt = num[1];
    }
    
    /**
     *
     * @param RA_s
     * @param Dec_s
     * @return
     */    
    public double[] calculateRTS(double RA_s, double  Dec_s) {
        double ret[] = new double[3];
        double m0, m1, m2;
        boolean fixedAlt = false;
        
        if(RA_s < 0.0) {
            fixedAlt = true;
            RA_s *= -1;
        }
        
        double L = Math.toRadians(loc.longitude);
        double lat = Math.toRadians(loc.latitude);
        double theta0 = loctime.GwSideralTime0_UT_1.getTimeInDegree(false);
        double ires = 0.0;
        double H0;
        
        // First calculate approximate times:
        // m0 is transit time
        // m1 is rise time
        // mw is set time
        ires = (Math.sin(h0) - (Math.sin(lat) * Math.sin(Math.toRadians(Dec_s))))
        / (Math.cos(lat) * Math.cos(Math.toRadians(Dec_s)));
        ires = Math.acos(ires);
        H0 = Math.toDegrees(ires);
        m0 = ((RA_s / 24.0 * 360.0) + loc.longitude - theta0) / 360.0;
        m0 = normalizedayfraction(m0);
        if(!Double.isNaN(H0)) {
            m1 = (m0 - (H0 / 360.0));
            m2 = (m0 + (H0 / 360.0));
            m1 = normalizedayfraction(m1);
            m2 = normalizedayfraction(m2);
        } else {
            m1 = Double.NaN;
            m2 = Double.NaN;
        }
        // Choose the right day.
        double offset = loctime.tzOffset / 24.0;
        if(offset < 0.0) {  // West of GMT
            offset = Math.abs(offset);
            if(m0 < offset)
                m0 += 1.0;
            if(!Double.isNaN(H0)) {
                if(m1 < offset)
                    m1 += 1.0;
                if(m2 < offset)
                    m2 += 1.0;
            }
        } else { // East of GMT
            offset = 1.0 - offset;
            if(m0 > offset)
                m0 = m0 - 1.0;
            if(!Double.isNaN(H0)) {
                if(m1 > offset)
                    m1 = m1 - 1.0;
                if(m2 > offset)
                    m2 = m2 - 1.0;
            }
        }
        
        // iterate and adjust RA and DEC
        
        double stgw, dm;
        double H = 0;
        double h[] = {0.0, 0.0};
        if(fixedAlt == false) {
            do {
                // interpolate RA and DEC
                this.interpolate(m0);
                
                stgw = theta0 + 360.985647 * m0;
                H = stgw - loc.longitude - (RA_i / 24.0 * 360.0);
                dm = (H / 360.0) * -1;
                m0 += dm;
            } while (dm > 0.000347);
        }
        if(fixedAlt == false)
            transAlt = (Jlibastro.AzAlt(RA_i / 24.0 * 360.0, Dec_i, H, loc.latitude, loc.longitude))[1];
        
        if(!Double.isNaN(H0)) {
            do {
                // interpolate RA and DEC
                this.interpolate(m1);
                
                stgw = theta0 + 360.985647 * m1;
                H = Jlibastro.norm360(stgw - loc.longitude - (RA_i / 24.0 * 360.0));
                h = (Jlibastro.AzAlt(RA_i / 24.0 * 360.0, Dec_i, H, loc.latitude, loc.longitude));
                dm = (h[1] - Math.toDegrees(h0))
                / (Math.cos(Math.toRadians(Dec_i)) * Math.cos(lat) * Math.sin(Math.toRadians(H)) * 360.0);
                m1 += dm;
            } while (dm > 0.000347);
            
            // rise  azimute
            if(fixedAlt == false)
                riseAz = h[0];
            
            do {
                //for(int i=0; i<5; i++) {
                // interpolate RA and DEC
                this.interpolate(m2);
                
                stgw = theta0 + 360.985647 * m2;
                H = Jlibastro.norm360(stgw - loc.longitude - (RA_i / 24.0 * 360.0));
                h = (Jlibastro.AzAlt(RA_i / 24.0 * 360.0, Dec_i, H, loc.latitude, loc.longitude));
                dm = (h[1] - Math.toDegrees(h0))
                / (Math.cos(Math.toRadians(Dec_i)) * Math.cos(lat) * Math.sin(Math.toRadians(H)) * 360.0);
                m2 += dm;
            } while (dm > 0.000347);
            
            // set  azimute
            if(fixedAlt == false)
                setAz = h[0];
        }
        
        ret[0] = normalizedayfraction(m0) * 24.0;
        if(!Double.isNaN(H0)) {
            ret[1] = normalizedayfraction(m1) * 24.0;
            ret[2] = normalizedayfraction(m2) * 24.0;
        }
        else {
            ret[1] = Double.NaN;
            ret[2] = Double.NaN;
        }
        
        return ret;
    }
    
    /**
     *
     * @param dfrac
     * @return
     */    
    private double normalizedayfraction(double dfrac) {
        if(dfrac < 0.0)
            return 1.0 + dfrac;
        if(dfrac >= 1.0)
            return dfrac - 1.0;
        return dfrac;
    }
    
    /**
     *
     * @param m
     */    
    void interpolate(double m) {
        RA_i = RA;
        Dec_i = Dec;
    }
    
    /**
     *
     * @return
     */    
    public String riseTimeString() {
        if(loctime.isHourAMPM())
            return rise.dispTimeHHMM_12();
        else
            return rise.dispTimeHHMM();
    }
    
    /**
     *
     * @return
     */    
    public String transTimeString() {
        if(loctime.isHourAMPM())
            return trans.dispTimeHHMM_12();
        else
            return trans.dispTimeHHMM();
    }
    
    /**
     *
     * @return
     */    
    public String setTimeString() {
        if(loctime.isHourAMPM())
            return set.dispTimeHHMM_12();
        else
            return set.dispTimeHHMM();
    }
    
    /**
     *
     * @return
     */    
    public String TimeUpString() {
        return Jlibastro.dispHHMM(timeUp, false);
    }
    
    /**
     * Calculate the phase as seen from earth
     * @return phase in percentage (100% = fully illuminated)
     */    
    public double getPhase() {
        return Double.NaN;
    }
    
    /**
     * Calculate the magnitude as seen from earth
     * @return magnitude
     */    
    public double getMagnitude() {
        return Double.NaN;
    }
    
}





