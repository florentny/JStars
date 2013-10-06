/*
 * $Id: Jsun.java,v 1.7 2010/01/30 21:39:17 fc Exp $
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

package jstars.util;

/**
 *
 * @author  fc
 */


public class Jsun extends ephemObject {
    
    double geolongitude;
    public double eclLat = 0.0;
    
    public double dawn, dusk;
    Jplanet Earth = null;
    
    Jsun(Location loc, Jlibastro.TimeData loctime) {
        super(loc, loctime);
        objType = SUN;
        h0 = -0.01454441;
    }
    
    @Override
    public String toString() {
        return "Sun";
    }
    
    @Override
    void interpolate(double m) {
        
        double coor [] = calculatePosition(loctime.JD_0hUT_1 + m, true);
        RA_i = coor[0];
        Dec_i = coor[1];
        
    }
    
    @Override
    public void calculatePosition() {
        double coor[];
        coor = calculatePosition(loctime.JDE, false);
        RA = coor[0];
        Dec = coor[1];
    }
    
    public double []  calculatePosition(double JDay, boolean interpolate) {
        double ret[] = new double[2];
        double A,B;
        double L0, M, e, C, ep;
        double T = (JDay - 2451545.0) / 36525.0;
        
        if(Earth == null) {
            L0 = 280.46646 + 36000.76983 * T + 0.0003032 * Math.pow(T, 2);
            M = 357.52911 + 35999.05029 * T + 0.0001537 * Math.pow(T, 2);
            e = 0.016708634 - 0.000042037 * T - 0.0000001267 * Math.pow(T, 2);
            L0 = Jlibastro.norm360(L0);
            M = Jlibastro.norm360(M);
            
            C = (1.914602 - 0.004817 * T - 0.000014 * Math.pow(T, 2)) * Math.sin(Math.toRadians(M))
            + (0.019993 - 0.000101 * T ) * Math.sin(Math.toRadians(2*M))
            + 0.000289 * Math.sin(Math.toRadians(3*M));
            geolongitude = L0 + C;
            eclLongitude = geolongitude;
        }
        else {
            Earth.calculatePosition(JDay, interpolate);
            geolongitude = Jlibastro.norm360(Earth.L + 180.0);
            eclLat = Earth.B * -1.0;
            if(interpolate == false) {
                this.eclLongitude = geolongitude;
                this.eclLatitude = eclLat;
                //System.out.println(geolongitude);
            }
        }
        
        // calculate obliquity
        //ep = 23.43929111 - 0.013004167 * T - 0.000000164 * Math.pow(T, 2) + 0.000005036* Math.pow(T, 3);
        ep = Jlibastro.obliquity(T);
        A = Math.cos(Math.toRadians(ep)) * Math.sin(Math.toRadians(geolongitude));
        B = Math.cos(Math.toRadians(geolongitude));
        //ret[0] = Math.atan2(Math.cos(Math.toRadians(ep)) * Math.sin(Math.toRadians(geolongitude)), Math.cos(Math.toRadians(geolongitude)));
        ret[0] = Math.atan2(A, B);
        ret[0] = Jlibastro.to24(Math.toDegrees(ret[0]) / 360.0 * 24.0);
        ret[1] = Math.asin(Math.sin(Math.toRadians(ep)) * Math.sin(Math.toRadians(geolongitude)));
        ret[1] = Math.toDegrees(ret[1]);
        
        
        return ret;
    }
    
    
    public double[] calculatetwilight(double dip) {
        double ret[] = new double[2];
        double ho_save = h0;
        h0 = Math.toRadians(dip);
        double num[] = calculateRTS(RA * -1, Dec);
        if(loctime.isDispLT()) {
            ret[0] = Jlibastro.to24(num[1] + loctime.tzOffset);
            ret[1] = Jlibastro.to24(num[2] + loctime.tzOffset);
        }
        else {
            ret[0] = Jlibastro.to24(num[1]);
            ret[1] = Jlibastro.to24(num[2]);
        }
        h0 = ho_save;
        
        return ret;
    }
    
    public String[] getTwilightTime(double dip, boolean save) {
        double ttime[] = calculatetwilight(dip);
        String ret[] = new String[2];
        ret[0] = Jlibastro.dispHHMM(ttime[0], loctime.isHourAMPM());
        ret[1] = Jlibastro.dispHHMM(ttime[1], loctime.isHourAMPM());
        if(save) {
            dawn = ttime[0];
            dusk = ttime[1];
        }
        return ret;
    }
    
    public void setEarth(Jplanet _earth) {
        Earth = _earth;
    }
    
    public double getEarthDistance() {
        Earth.calculatePosition();
        return Earth.sunDistance;
    }
    
    public String getEarthDistanceString() {
        
        Earth.calculatePosition();
        return Earth.getSunDistanceString();
    }
    
    public double getMagnitude() {
        return -27.0;
    }
    
}