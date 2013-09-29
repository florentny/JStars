/*
 * $Id: Jjupiter.java,v 1.6 2010/01/30 21:39:17 fc Exp $
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
 */

package jstars.util;

/**
 *
 * @author  Florent Charpin
 */
public class Jjupiter extends Jplanet {
    
    /**
     * @param loc
     * @param loctime
     */
    Jjupiter(Location loc, jstars.util.Jlibastro.TimeData loctime) {
        super(loc, loctime, ephemObject.JUPITER);
    }
    
    
    /**
     * @return
     */
    @Override
    public double getMagnitude() {
        return -9.40 + 5 * Math.log(earthDistance * sunDistance) /Math.log(10);
    }
    
    
    /**
     * @return
     */
    public double[][] satellite() {
        double [][] sat = new double[4][4];
        
        // Jupiter ephemeris
        double d = loctime.getJDE() - 2451545.0;
        double V = Jlibastro.norm360(172.74 + 0.00111588 * d);
        double M = Jlibastro.norm360(357.529 + 0.9856003 * d);
        double N = Jlibastro.norm360(20.020 + 0.0830853 * d + 0.329 * Math.sin(Math.toRadians(V)));
        double J = Jlibastro.norm360(66.115 + 0.9025179 * d - 0.329 * Math.sin(Math.toRadians(V)));
        double A = 1.915 * Math.sin(Math.toRadians(M)) + 0.020 * Math.sin(Math.toRadians(2*M));
        double Bs = 5.555 * Math.sin(Math.toRadians(N)) + 0.168 * Math.sin(Math.toRadians(2*N));
        double K = Jlibastro.norm360(J + A - Bs);
        double phi = Math.toDegrees(Math.asin((Earth.sunDistance / earthDistance) * Math.sin(Math.toRadians(K))));
        double dfinal = d - (earthDistance / 173);
        
        
        // Calculate 4 satellite location - u1-4
        sat[0][0] = Jlibastro.norm360(163.8069 + 203.4058646 * dfinal + phi - Bs);
        sat[1][0] = Jlibastro.norm360(358.4140 + 101.2916335 * dfinal + phi - Bs);
        sat[2][0] = Jlibastro.norm360(5.7176 + 50.2345180 * dfinal + phi - Bs);
        sat[3][0] = Jlibastro.norm360(224.8092 + 21.4879800 * dfinal + phi - Bs);
        
        // Correction to u1-u4
        double G = 331.18 + 50.310482 * dfinal;
        double H = 87.45 + 21.569231 * dfinal;
        sat[0][0] = sat[0][0] + 0.473 * Math.sin(2 * (Math.toRadians(sat[0][0]) - Math.toRadians(sat[1][0]))); // pertubation sat I by sat II
        sat[1][0] = sat[1][0] + 1.065 * Math.sin(2 * (Math.toRadians(sat[1][0]) - Math.toRadians(sat[3][0]))); // pertubation sat II by sat III
        sat[2][0] = sat[2][0] + 0.165 * Math.sin(Math.toRadians(G));  // eccentricities of sat III
        sat[3][0] = sat[3][0] + 0.843 * Math.sin(Math.toRadians(H));  // eccentricities of sat IV
        
        // Calculate distance radius
        sat[0][1] = 5.9057 - 0.0244 * Math.cos(Math.toRadians((2 * (sat[0][0] - sat[1][0]))));
        sat[1][1] = 9.3966 - 0.0882 * Math.cos(Math.toRadians((2 * (sat[1][0] - sat[2][0]))));
        sat[2][1] = 14.9883 - 0.0216 * Math.cos(Math.toRadians(G));
        sat[3][1] = 26.3627 - 0.1939 * Math.cos(Math.toRadians(H));
        
        // Calculate De
        double lamba = 34.35 + 0.083091 * d + 0.329 * Math.sin(Math.toRadians(V)) + Bs;
        double Ds = 3.12 * Math.sin(Math.toRadians(lamba + 42.8));
        double De = Ds - 2.22 * Math.sin(Math.toRadians(phi)) * Math.cos(Math.toRadians(lamba + 22.0))
        - 1.30 * ((sunDistance - earthDistance) / earthDistance) * Math.sin(Math.toRadians(lamba - 100.5));
        
        // Calculate X and Y
        sat[0][2] = sat[0][1] * Math.sin(Math.toRadians(sat[0][0]));
        sat[0][3] =  -1.0* sat[0][1] * Math.cos(Math.toRadians(sat[0][0])) * Math.sin(Math.toRadians(De));
        
        sat[1][2] = sat[1][1] * Math.sin(Math.toRadians(sat[1][0]));
        sat[1][3] =  -1.0* sat[1][1] * Math.cos(Math.toRadians(sat[1][0])) * Math.sin(Math.toRadians(De));
        
        sat[2][2] = sat[2][1] * Math.sin(Math.toRadians(sat[2][0]));
        sat[2][3] =  -1.0* sat[2][1] * Math.cos(Math.toRadians(sat[2][0])) * Math.sin(Math.toRadians(De));
        
        sat[3][2] = sat[3][1] * Math.sin(Math.toRadians(sat[3][0]));
        sat[3][3] =  -1.0* sat[3][1] * Math.cos(Math.toRadians(sat[3][0])) * Math.sin(Math.toRadians(De));
        
        
        return sat;
    }
    
}
