/*
 * $Id: Jplanet.java,v 1.6 2010/01/30 21:39:17 fc Exp $
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


public class Jplanet extends ephemObject {
    
    double [][] L0,L1,L2,L3,L4,L5;
    double [][] B0,B1,B2,B3,B4,B5;
    double [][] R0,R1,R2,R3,R4,R5;
    
    double L,B,R;
    public double eclLon = 0.0;
    public double eclLat = 0.0;
    
    public double phase;
    
    protected Jplanet Earth = null;
    
    Jplanet(Location loc, Jlibastro.TimeData loctime, int planet) {
        super(loc, loctime);
        objType = planet;
        
        // initialize VSOP87 data
        VSOP87 vsop = VSOP87.getInstance();
        switch(objType) {
            case MERCURY:
                L0 = vsop.mercury.L0;
                L1 = vsop.mercury.L1;
                L2 = vsop.mercury.L2;
                L3 = vsop.mercury.L3;
                L4 = vsop.mercury.L4;
                L5 = vsop.mercury.L5;
                B0 = vsop.mercury.B0;
                B1 = vsop.mercury.B1;
                B2 = vsop.mercury.B2;
                B3 = vsop.mercury.B3;
                B4 = vsop.mercury.B4;
                B5 = vsop.mercury.B5;
                R0 = vsop.mercury.R0;
                R1 = vsop.mercury.R1;
                R2 = vsop.mercury.R2;
                R3 = vsop.mercury.R3;
                R4 = vsop.mercury.R4;
                R5 = vsop.mercury.R5;
                break;
            case VENUS:
                L0 = vsop.venus.L0;
                L1 = vsop.venus.L1;
                L2 = vsop.venus.L2;
                L3 = vsop.venus.L3;
                L4 = vsop.venus.L4;
                L5 = vsop.venus.L5;
                B0 = vsop.venus.B0;
                B1 = vsop.venus.B1;
                B2 = vsop.venus.B2;
                B3 = vsop.venus.B3;
                B4 = vsop.venus.B4;
                B5 = vsop.venus.B5;
                R0 = vsop.venus.R0;
                R1 = vsop.venus.R1;
                R2 = vsop.venus.R2;
                R3 = vsop.venus.R3;
                R4 = vsop.venus.R4;
                R5 = vsop.venus.R5;
                break;
            case EARTH:
                L0 = vsop.earth.L0;
                L1 = vsop.earth.L1;
                L2 = vsop.earth.L2;
                L3 = vsop.earth.L3;
                L4 = vsop.earth.L4;
                L5 = vsop.earth.L5;
                B0 = vsop.earth.B0;
                B1 = vsop.earth.B1;
                B2 = vsop.earth.B2;
                B3 = vsop.earth.B3;
                B4 = vsop.earth.B4;
                B5 = null;
                R0 = vsop.earth.R0;
                R1 = vsop.earth.R1;
                R2 = vsop.earth.R2;
                R3 = vsop.earth.R3;
                R4 = vsop.earth.R4;
                R5 = vsop.earth.R5;
                break;
            case MARS:
                L0 = vsop.mars.L0;
                L1 = vsop.mars.L1;
                L2 = vsop.mars.L2;
                L3 = vsop.mars.L3;
                L4 = vsop.mars.L4;
                L5 = vsop.mars.L5;
                B0 = vsop.mars.B0;
                B1 = vsop.mars.B1;
                B2 = vsop.mars.B2;
                B3 = vsop.mars.B3;
                B4 = vsop.mars.B4;
                B5 = vsop.mars.B5;
                R0 = vsop.mars.R0;
                R1 = vsop.mars.R1;
                R2 = vsop.mars.R2;
                R3 = vsop.mars.R3;
                R4 = vsop.mars.R4;
                R5 = null;
                break;
            case JUPITER:
                L0 = vsop.jupiter.L0;
                L1 = vsop.jupiter.L1;
                L2 = vsop.jupiter.L2;
                L3 = vsop.jupiter.L3;
                L4 = vsop.jupiter.L4;
                L5 = vsop.jupiter.L5;
                B0 = vsop.jupiter.B0;
                B1 = vsop.jupiter.B1;
                B2 = vsop.jupiter.B2;
                B3 = vsop.jupiter.B3;
                B4 = vsop.jupiter.B4;
                B5 = vsop.jupiter.B5;
                R0 = vsop.jupiter.R0;
                R1 = vsop.jupiter.R1;
                R2 = vsop.jupiter.R2;
                R3 = vsop.jupiter.R3;
                R4 = vsop.jupiter.R4;
                R5 = vsop.jupiter.R5;
                break;
            case SATURN:
                L0 = vsop.saturn.L0;
                L1 = vsop.saturn.L1;
                L2 = vsop.saturn.L2;
                L3 = vsop.saturn.L3;
                L4 = vsop.saturn.L4;
                L5 = vsop.saturn.L5;
                B0 = vsop.saturn.B0;
                B1 = vsop.saturn.B1;
                B2 = vsop.saturn.B2;
                B3 = vsop.saturn.B3;
                B4 = vsop.saturn.B4;
                B5 = vsop.saturn.B5;
                R0 = vsop.saturnR.R0;
                R1 = vsop.saturnR.R1;
                R2 = vsop.saturnR.R2;
                R3 = vsop.saturnR.R3;
                R4 = vsop.saturnR.R4;
                R5 = vsop.saturnR.R5;
                break;
            case URANUS:
                L0 = vsop.uranus.L0;
                L1 = vsop.uranus.L1;
                L2 = vsop.uranus.L2;
                L3 = vsop.uranus.L3;
                L4 = vsop.uranus.L4;
                L5 = vsop.uranus.L5;
                B0 = vsop.uranus.B0;
                B1 = vsop.uranus.B1;
                B2 = vsop.uranus.B2;
                B3 = vsop.uranus.B3;
                B4 = vsop.uranus.B4;
                B5 = null;
                R0 = vsop.uranus.R0;
                R1 = vsop.uranus.R1;
                R2 = vsop.uranus.R2;
                R3 = vsop.uranus.R3;
                R4 = vsop.uranus.R4;
                R5 = null;
                break;
            case NEPTUNE:
                L0 = vsop.neptune.L0;
                L1 = vsop.neptune.L1;
                L2 = vsop.neptune.L2;
                L3 = vsop.neptune.L3;
                L4 = vsop.neptune.L4;
                L5 = vsop.neptune.L5;
                B0 = vsop.neptune.B0;
                B1 = vsop.neptune.B1;
                B2 = vsop.neptune.B2;
                B3 = vsop.neptune.B3;
                B4 = vsop.neptune.B4;
                B5 = null;
                R0 = vsop.neptune.R0;
                R1 = vsop.neptune.R1;
                R2 = vsop.neptune.R2;
                R3 = vsop.neptune.R3;
                R4 = vsop.neptune.R4;
                R5 = null;
                break;
            case PLUTO:
                break;
            default:
                break;
        }
    }
    
    @Override
    public String toString() {
        switch(objType) {
            case MERCURY:
                return "Mercury";
            case VENUS:
                return "Venus";
            case MARS:
                return "Mars";
            case JUPITER:
                return "Jupiter";
            case SATURN:
                return "Saturn";
            case URANUS:
                return "Uranus";
            case NEPTUNE:
                return "Neptune";
            case PLUTO:
                return "Pluto";
            default:
                return "Unkown";
        }
        
    }
    
    public void setEarth(Jplanet _earth) {
        Earth = _earth;
    }
    
    @Override
    public void calculatePosition() {
        if(lastCall == loctime.JDE)
            return;
        double coor[];
        coor = calculatePosition(loctime.JDE, false);
        RA = coor[0];
        Dec = coor[1];
        lastCall = loctime.JDE;
    }
    
    public double []  calculatePosition(double JDay, boolean iterate) {
        double coor[] = new double[2];
        
        double T = (JDay - 2451545.0) / 36525.0;
        calculateHeliocentric(JDay);
        
        if(iterate == false) {
            eclLongitude = eclLon;
            eclLatitude = eclLat;
        }
        
        if(objType != EARTH) {
            double obl = Jlibastro.obliquity(T);
            coor = Jlibastro.eclipticalToEquatorial(obl, eclLon, eclLat);
        }
        return coor;
    }
    
    
    void calculateHeliocentric(double JDay) {
        double x,y,z;
        double lambda, beta;
        
        double T = (JDay - 2451545.0) / 365250.0;
        double l0 = vsopSum(L0, T);
        double l1 = vsopSum(L1, T);
        double l2 = vsopSum(L2, T);
        double l3 = vsopSum(L3, T);
        double l4 = vsopSum(L4, T);
        double l5 = vsopSum(L5, T);
        double b0 = vsopSum(B0, T);
        double b1 = vsopSum(B1, T);
        double b2 = vsopSum(B2, T);
        double b3 = vsopSum(B3, T);
        double b4 = vsopSum(B4, T);
        double b5 = vsopSum(B5, T);
        double r0 = vsopSum(R0, T);
        double r1 = vsopSum(R1, T);
        double r2 = vsopSum(R2, T);
        double r3 = vsopSum(R3, T);
        double r4 = vsopSum(R4, T);
        double r5 = vsopSum(R5, T);
        
        L = l0 + l1 * T + l2 * Math.pow(T, 2) + l3 * Math.pow(T, 3) + l4 * Math.pow(T, 4) + l5 * Math.pow(T, 5);
        L = Jlibastro.norm360(Math.toDegrees(L / 100000000.0));
        B = b0 + b1 * T + b2 * Math.pow(T, 2) + b3 * Math.pow(T, 3) + b4 * Math.pow(T, 4) + b5 * Math.pow(T, 5);
        B = Math.toDegrees(B / 100000000.0);
        sunDistance = r0 + r1 * T + r2 * Math.pow(T, 2) + r3 * Math.pow(T, 3) + r4 * Math.pow(T, 4) + r5 * Math.pow(T, 5);
        sunDistance = sunDistance / 100000000.0;
        
        if(objType != EARTH) {
            x = sunDistance * Math.cos(Math.toRadians(B)) * Math.cos(Math.toRadians(L))
            - Earth.sunDistance * Math.cos(Math.toRadians(Earth.B)) * Math.cos(Math.toRadians(Earth.L));
            y = sunDistance * Math.cos(Math.toRadians(B)) * Math.sin(Math.toRadians(L))
            - Earth.sunDistance * Math.cos(Math.toRadians(Earth.B)) * Math.sin(Math.toRadians(Earth.L));
            z = sunDistance * Math.sin(Math.toRadians(B)) - Earth.sunDistance * Math.sin(Math.toRadians(Earth.B));
            
            eclLon = Jlibastro.norm360(Math.toDegrees(Math.atan2(y, x)));
            eclLat = Math.toDegrees(Math.atan2(z, (Math.sqrt(x * x + y * y))));
            //System.out.println("lambda: " + eclLon + " beta: " + eclLat);
            
            //Earth distance
            earthDistance = Math.sqrt(x*x + y*y +z*z);
            
            phase = (Math.pow((sunDistance + earthDistance), 2) - Math.pow(Earth.sunDistance, 2)) / (4 * sunDistance * earthDistance);
        }
        
    }
    
    double vsopSum(double[][] S, double T) {
        double term = 0.0;
        if(S == null)
            return 0.0;
        int len =  S.length;
        for(int i=0; i < len; i++) {
            term += S[i][0] * Math.cos(S[i][1] + S[i][2] * T);
        }
        return term;
    }

    @Override
    void interpolate(double m) {
        
        //double coor [] = calculatePosition(loctime.JD_0hUT_1 + m + (loctime.DeltaT / 86400.0), true);
        double coor [] = calculatePosition(loctime.JD_0hUT_1 + m, true);
        RA_i = coor[0];
        Dec_i = coor[1];
    }
    
    @Override
    public double getPhase() {
        return phase;
    }

    @Override
    public double getMagnitude() {
        double i = Math.toDegrees(Math.acos(2*phase -1));
        if(objType == MERCURY) {
            return -0.42 + 5 * Math.log(earthDistance * sunDistance) / Math.log(10) + 0.0380 * i - 0.000273 * i*i + 0.000002 * Math.pow(i, 3);
        }
        if(objType == VENUS) {
            return -4.40 + 5 * Math.log(earthDistance * sunDistance) /Math.log(10) + 0.00009 * i + 0.000239 * i*i - 0.00000065 * Math.pow(i, 3);
        }
        if(objType == MARS) {
            return -1.52 + 5 * Math.log(earthDistance * sunDistance) /Math.log(10) + 0.016 * i ;
        }
        if(objType == URANUS) {
            return -7.19 + 5 * Math.log(earthDistance * sunDistance) /Math.log(10);
        }
        if(objType == NEPTUNE) {
            return -6.87 + 5 * Math.log(earthDistance * sunDistance) /Math.log(10);
        }
        if(objType == PLUTO) {
            return -1.00 + 5 * Math.log(earthDistance * sunDistance) /Math.log(10);
        }
        
        return Double.NaN;
    }
    
}


