/*
 * $Id: Jmoon.java,v 1.6 2010/01/30 21:39:17 fc Exp $
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


public class Jmoon extends ephemObject {
    
    private final double[][] periodTermLR = {
        { 6288774 , -20905355 },
        { 1274027 , -3699111 },
        { 658314 , -2955968 },
        { 213618 , -569925 },
        { -185116 , 48888 },
        { -114332 , -3149 },
        { 58793 , 246158 },
        { 57066 , -152138 },
        { 53322 , -170733 },
        { 45758 , -204586 },
        { -40923 , -129620 },
        { -34720 , 108743 },
        { -30383 , 104755 },
        { 15327 , 10321 },
        { -12528 , 0 },
        { 10980 , 79661 },
        { 10675 , -34782 },
        { 10034 , -23210 },
        { 8548 , -21636 },
        { -7888 , 24208 },
        { -6766 , 30824 },
        { -5163 , -8379 },
        { 4987 , -16675 },
        { 4036 , -12831 },
        { 3994 , -10445 },
        { 3861 , -11650 },
        { 3665 , 14403 },
        { -2689 , -7003 },
        { -2602 , 0 },
        { 2390 , 10056 },
        { -2348 , 6322 },
        { 2236 , -9884 },
        { -2120 , 5751 },
        { -2069 , 0 },
        { 2048 , -4950 },
        { -1773 , 4130 },
        { -1595 , 0 },
        { 1215 , -3958 },
        { -1110 , 0 },
        { -892 , 3258 },
        { -810 , 2616 },
        { 759 , -1897 },
        { -713 , -2117 },
        { -700 , 2354 },
        { 691 , 0 },
        { 596 , 0 },
        { 549 , -1423 },
        { 537 , -1117 },
        { 520 , -1571 },
        { -487 , -1739 },
        { -399 , 0 },
        { -381 , -4421 },
        { 351 , 0 },
        { -340 , 0 },
        { 330 , 0 },
        { 327 , 0 },
        { -323 , 1165 },
        { 299 , 0 },
        { 294 , 0 },
        { 0 , 8752 }
    };
    
    private final double[][] argLR = {
        { 0,0,1,0 },
        { 2,0,-1,0 },
        { 2,0,0,0 },
        { 0,0,2,0 },
        { 0,1,0,0 },
        { 0,0,0,2 },
        { 2,0,-2,0 },
        { 2,-1,-1,0 },
        { 2,0,1,0 },
        { 2,-1,0,0 },
        { 0,1,-1,0 },
        { 1,0,0,0 },
        { 0,1,1,0 },
        { 2,0,0,-2 },
        { 0,0,1,2 },
        { 0,0,1,-2 },
        { 4,0,-1,0 },
        { 0,0,3,0 },
        { 4,0,-2,0 },
        { 2,1,-1,0 },
        { 2,1,0,0 },
        { 1,0,-1,0 },
        { 1,1,0,0 },
        { 2,-1,1,0 },
        { 2,0,2,0 },
        { 4,0,0,0 },
        { 2,0,-3,0 },
        { 0,1,-2,0 },
        { 2,0,-1,2 },
        { 2,-1,-2,0 },
        { 1,0,1,0 },
        { 2,-2,0,0 },
        { 0,1,2,0 },
        { 0,2,0,0 },
        { 2,-2,-1,0 },
        { 2,0,1,-2 },
        { 2,0,0,2 },
        { 4,-1,-1,0 },
        { 0,0,2,2 },
        { 3,0,-1,0 },
        { 2,1,1,0 },
        { 4,-1,-2,0 },
        { 0,2,-1,0 },
        { 2,2,-1,0 },
        { 2,1,-2,0 },
        { 2,-1,0,-2 },
        { 4,0,1,0 },
        { 0,0,4,0 },
        { 4,-1,0,0 },
        { 1,0,-2,0 },
        { 2,1,0,-2 },
        { 0,0,2,-2 },
        { 1,1,1,0 },
        { 3,0,-2,0 },
        { 4,0,-3,0 },
        { 2,-1,2,0 },
        { 0,2,1,0 },
        { 1,1,-1,0 },
        { 2,0,3,0 },
        { 2,0,-1,-2 }
    };
    
    private final double[]periodTermB = {
        5128122,
        280602,
        277693,
        173237,
        55413,
        46271,
        32573,
        17198,
        9266,
        8822,
        8216,
        4324,
        4200,
        -3359,
        2463,
        2211,
        2065,
        -1870,
        1828,
        -1794,
        -1749,
        -1565,
        -1491,
        -1475,
        -1410,
        -1344,
        -1335,
        1107,
        1021,
        833,
        777,
        671,
        607,
        596,
        491,
        -451,
        439,
        422,
        421,
        -366,
        -351,
        331,
        315,
        302,
        -283,
        -229,
        223,
        223,
        -220,
        -220,
        -185,
        181,
        -177,
        176,
        166,
        -164,
        132,
        -119,
        115,
        107
    };
    
    private final double[][] argB = {
        { 0,0,0,1  },
        { 0,0,1,1 },
        { 0,0,1,-1 },
        { 2,0,0,-1 },
        { 2,0,-1,1 },
        { 2,0,-1,-1 },
        { 2,0,0,1 },
        { 0,0,2,1 },
        { 2,0,1,-1 },
        { 0,0,2,-1 },
        { 2,-1,0,-1 },
        { 2,0,-2,-1 },
        { 2,0,1,1 },
        { 2,1,0,-1 },
        { 2,-1,-1,1 },
        { 2,-1,0,1 },
        { 2,-1,-1,-1 },
        { 0,1,-1,-1 },
        { 4,0,-1,-1 },
        { 0,1,0,1 },
        { 0,0,0,3 },
        { 0,1,-1,1 },
        { 1,0,0,1 },
        { 0,1,1,1 },
        { 0,1,1,-1 },
        { 0,1,0,-1 },
        { 1,0,0,-1 },
        { 0,0,3,1 },
        { 4,0,0,-1 },
        { 4,0,-1,1 },
        { 0,0,1,-3 },
        { 4,0,-2,1 },
        { 2,0,0,-3 },
        { 2,0,2,-1 },
        { 2,-1,1,-1 },
        { 2,0,-2,1 },
        { 0,0,3,-1 },
        { 2,0,2,1 },
        { 2,0,-3,-1 },
        { 2,1,-1,1 },
        { 2,1,0,1 },
        { 4,0,0,1 },
        { 2,-1,1,1 },
        { 2,-2,0,-1 },
        { 0,0,1,3 },
        { 2,1,1,-1 },
        { 1,1,0,-1 },
        { 1,1,0,1 },
        { 0,1,-2,-1 },
        { 2,1,-1,-1 },
        { 1,0,1,1 },
        { 2,-1,-2,-1 },
        { 0,1,2,1 },
        { 4,0,-2,-1 },
        { 4,-1,-1,-1 },
        { 1,0,1,-1 },
        { 4,0,1,-1 },
        { 1,0,-1,-1 },
        { 4,-1,0,-1 },
        { 2,-2,0,1 }
    };
    
    double[][] resLRB = new double[60][3];
    jstars.util.Jsun jsun;
    
    Jmoon(Location loc, Jlibastro.TimeData loctime, jstars.util.Jsun jsun) {
        super(loc, loctime);
        objType = MOON;
        resLRB = new double[60][3];
        h0 = -0.00218166;
        this.jsun = jsun;
    }
    
    @Override
    public String toString() {
        return "Moon";
    }
    
    
    @Override
    public void calculatePosition() {
        double coor[];
        coor = calculatePosition(loctime.JDE, false);
        RA = coor[0];
        Dec = coor[1];
    }
    
    public double []  calculatePosition(double JDay, boolean iterate) {
        double sumL = 0.0, sumR = 0.0, sumB = 0.0;
        double resL_i, resR_i, resB_i;
        double eclLon = 0.0;
        double eclLat = 0.0;
        int i;
        
        // time T
        double T = (JDay - 2451545.0) / 36525.0;
        // Moon mean longitude
        double LP = Jlibastro.norm360(218.3164477 + 481267.88123421 * T - 0.0015786 * Math.pow(T,2)
        + Math.pow(T,3) / 538841.0 - Math.pow(T,4) / 65194000.0);
        // Moon's mean elongation
        double D = Jlibastro.norm360(297.8501921 + 445267.1114034 * T - 0.0018819 * Math.pow(T,2)
        + Math.pow(T,3) / 545868.0 - Math.pow(T,4) / 113065000.0);
        // Sun's mean anomaly
        double M = Jlibastro.norm360(357.5291029 + 35999.0502909 * T - 0.0018819 * Math.pow(T,2)
        + Math.pow(T,3) / 24490000.0);
        // Moon's mean anomaly
        double MP = Jlibastro.norm360(134.9633964 + 477198.8675055 * T + 0.0087414 * Math.pow(T,2)
        + Math.pow(T,3) / 69699 - Math.pow(T,4) / 14712000.0);
        // Mean distance of Monn to ascending node
        double F = Jlibastro.norm360(93.2720950 + 483202.0175233 * T - 0.0036539 * Math.pow(T,2)
        - Math.pow(T,3) / 3526000.0 + Math.pow(T,4) / 863310000);
        // Venus action on Moon
        double A1 = Math.toRadians(Jlibastro.norm360(119.75 + 131.849 * T));
        // Jupiter action on Moon
        double A2 = Math.toRadians(Jlibastro.norm360(55.09 + 479264.290 * T));
        // Earth flattening action on Moon
        double A3 = Math.toRadians(Jlibastro.norm360(313.45 + 481266.484 * T));
        // eccentricity of the earth
        double E = 1 - 0.002546 * T - 0.0000074 * Math.pow(T,2);
        
        for(i=0; i < 60; i++) {
            if(Math.abs(argLR[i][1]) == 1) {
                resL_i = periodTermLR[i][0] * E;
                resR_i = periodTermLR[i][1] * E;
                resB_i = periodTermB[i] * E;
            }
            else if(Math.abs(argLR[i][1]) == 2) {
                resL_i = periodTermLR[i][0] * Math.pow(E,2);
                resR_i = periodTermLR[i][1] * Math.pow(E,2);
                resB_i = periodTermB[i] * Math.pow(E,2);
            }
            else {
                resL_i = periodTermLR[i][0];
                resR_i = periodTermLR[i][1];
                resB_i = periodTermB[i];
            }
            sumL += resL_i * Math.sin(Math.toRadians(argLR[i][0] * D + argLR[i][1] * M + argLR[i][2] * MP + argLR[i][3] * F));
            sumR += resR_i * Math.cos(Math.toRadians(argLR[i][0] * D + argLR[i][1] * M + argLR[i][2] * MP + argLR[i][3] * F));
            sumB += resB_i * Math.sin(Math.toRadians(argB[i][0] * D + argB[i][1] * M + argB[i][2] * MP + argB[i][3] * F));
        }
        
        sumL = sumL + 3958.0 * Math.sin(A1) + 1962.0 * Math.sin(Math.toRadians(LP - F)) + 318.0 * Math.sin(A2);
        sumB = sumB -2235.0 * Math.sin(Math.toRadians(LP)) + 382.0 * Math.sin(A3) + 175.0 * Math.sin(A1 - Math.toRadians(F))
        + 175.0  * Math.sin(A1+Math.toRadians(F)) + 127.0 * Math.sin(Math.toRadians(LP-MP)) - 115.0  * Math.sin(Math.toRadians(LP+MP));
        
        eclLon = LP + (sumL / 1000000.0);
        eclLat = sumB / 1000000.0;
        double obl = Jlibastro.obliquity(T);
        double coor[] = Jlibastro.eclipticalToEquatorial(obl, eclLon, eclLat);
        //System.out.println("lambda: " + eclLon + " beta: " + eclLat);
        if(iterate == false) {
            eclLongitude = eclLon;
            eclLatitude = eclLat;
            this.earthDistance = Math.rint((385000.56 + (sumR / 1000.0)) * 10.0) / 10.0;
        }
        return coor;
    }
    
    @Override
    void interpolate(double m) {
        
        double coor [] = calculatePosition(loctime.JD_0hUT_1 + m + (loctime.DeltaT / 86400.0), true);
        RA_i = coor[0];
        Dec_i = coor[1];
    }
    
    @Override
    public String getEarthDistanceString() {
        return String.valueOf(earthDistance) + " km";
    }
    
    public double[] illuminatedDisk() {
        double ret[] = new double[2];
        
        double phi = Math.cos(Math.toRadians(eclLatitude)) * Math.cos(Math.toRadians(eclLongitude) - Math.toRadians(jsun.eclLongitude));
        phi = Math.acos(phi);
        double R = jsun.getEarthDistance() * 149597870.0;
        double i = Math.atan2(R * Math.sin(phi), (earthDistance - R * Math.cos(phi)));
        ret[0] = (1.0 + Math.cos(i)) / 2.0;
        
        double A = Math.cos(Math.toRadians(jsun.Dec)) * Math.sin(jsun.getRadRA() - getRadRA());
        double B =  (Math.sin(Math.toRadians(jsun.Dec)) * Math.cos(Math.toRadians(Dec))
        -  Math.cos(Math.toRadians(jsun.Dec)) * Math.sin(Math.toRadians(Dec)) * Math.cos(jsun.getRadRA() - getRadRA()));
        ret[1] = Jlibastro.norm360(Math.toDegrees(Math.atan2(A, B)));
        return ret;
    }
    
    public double[] getPhases() {
        double ret[] = new double[4];
        
        double k = (loctime.getCurrentYearDouble() - 2000.0) * 12.3685;
        // New Moon;
        ret[0] = getPhase(k, 0.0);
        // First Quarter;
        ret[1] = getPhase(k, 0.25);
        // Full Moon;
        ret[2] = getPhase(k, 0.50);
        // Last Quarter;
        ret[3] = getPhase(k, 0.75);
        
        return ret;
    }
    
    double getPhase(double ka, double ki) {
        double k = Math.floor(ka) + ki;
        
        double T = k / 1236.85;
        double JDE = 2451550.09766 + 29.530588861 * k + 0.00015437 * k*k
        - 0.000000150 * Math.pow(T,3) + 0.00000000073 * Math.pow(T,4);
        
        if(true) {
            // TO DO -> High Precision
        }
        
        if(JDE < loctime.getJDE())
            JDE = getPhase(ka + 1.0, ki);
        return JDE;
    }
    
    @Override
    public double getPhase() {
        return  illuminatedDisk()[0];
    }
    
    @Override
    public double getMagnitude() {
        double i = Math.toDegrees(Math.acos(2*getPhase() -1));
        
        return 0.23 + 5 * Math.log(earthDistance / 149597870 * 1.0) / Math.log(10) + 0.026 * i;
        
    }
    
}



