/*
 * $Id: Jpluto.java,v 1.6 2010/01/30 21:39:17 fc Exp $
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
 * Calculations based on Chapter 37 of Jean Meeus
 * Astronomical Algorithms, 2nd edition, 1998
 *
 */

package jstars.util;

/**
 *
 * @author  fc
 */
public class Jpluto extends Jplanet {
    
    final int[][] arg = {
        {0, 0, 1},
        {0, 0, 2},
        {0, 0, 3},
        {0, 0, 4},
        {0, 0, 5},
        {0, 0, 6},
        {0, 1, -1},
        {0, 1, 0},
        {0, 1, 1},
        {0, 1, 2},
        {0, 1, 3},
        {0, 2, -2},
        {0, 2, -1},
        {0, 2, 0},
        {1, -1, 0},
        {1, -1, 1},
        {1, 0, -3},
        {1, 0, -2},
        {1, 0, -1},
        {1, 0, 0},
        {1, 0, 1},
        {1, 0, 2},
        {1, 0, 3},
        {1, 0, 4},
        {1, 1, -3},
        {1, 1, -2},
        {1, 1, -1},
        {1, 1, 0},
        {1, 1, 1},
        {1, 1, 3},
        {2, 0, -6},
        {2, 0, -5},
        {2, 0, -4},
        {2, 0, -3},
        {2, 0, -2},
        {2, 0, -1},
        {2, 0, 0},
        {2, 0, 1},
        {2, 0, 2},
        {2, 0, 3},
        {3, 0, -2},
        {3, 0, -1},
        {3, 0, 0}
    };
    
    final double[][] Lon = {
        {-19799805, 19850055},
        {897144, -4954829},
        {611149, 1211027},
        {-341243, -189585},
        {129287, -34992},
        {-38164, 30893},
        {20442, -9987},
        {-4063, -5071},
        {-6016, -3336},
        {-3956, 3039},
        {-667, 3572},
        {1276, 501},
        {1152, -917},
        {630, -1277},
        {2571, -459},
        {899, -1449},
        {-1016, 1043},
        {-2343, -1012},
        {7042, 788},
        {1199, -338},
        {418, -67},
        {120, -274},
        {-60, -159},
        {-82, -29},
        {-36, -20},
        {-40, 7},
        {-14, 22},
        {4, 13},
        {5,2},
        {-1,0},
        {2,0},
        {-4, 5},
        {4, -7},
        {14, 24},
        {-49, -34},
        {163, -48},
        {9, 24},
        {-4, 1},
        {-3,1},
        {1,3},
        {-3, -1},
        {5, -3},
        {0,0}
    };
    
    final double[][] Lat = {
        {-5452852, -14974862},
        {3527812, 1672790},
        {-1050748, 327647},
        {178690, -292153},
        {18650, 100340},
        {-30697, -25823},
        {4878, 11248},
        {226, -64},
        {2030, -836},
        {69, -604},
        {-247, -567},
        {-57, 1},
        {-122, 175},
        {-49, -164},
        {-197, 199},
        {-25, 217},
        {589, -248},
        {-269, 711},
        {185, 193},
        {315, 807},
        {-130, -43},
        {5, 3},
        {2, 17},
        {2, 5},
        {2, 3},
        {3, 1},
        {2, -1},
        {1, -1},
        {0, -1},
        {0, 0},
        {0, -2},
        {2, 2},
        {-7, 0},
        {10, -8},
        {-3, 20},
        {6, 5},
        {14, 17},
        {-2, 0},
        {0, 0},
        {0, 0},
        {0, 1},
        {0, 0},
        {1, 0}
    };
    
    final double Radius[][] = {
        {66865439, 68951812},
        {-11827535, -332538},
        {1593179, -1438890},
        {-18444, 483220},
        {-65977, -85431},
        {31174, -6032},
        {-5794, 22161},
        {4601, 4032},
        {-1729, 234},
        {-415, 702},
        {239, 723},
        {67, -67},
        {1034, -451},
        {-129, 504},
        {480, -231},
        {2, -441},
        {-3359, 265},
        {7856, -7832},
        {36, 45763},
        {8663, 8547},
        {-809, -769},
        {263, -144},
        {-126, 32},
        {-35, -16},
        {-19, -4},
        {-15, 8},
        {-4, 12},
        {5, 6},
        {3, 1},
        {6, -2},
        {2, 2},
        {-2, -2},
        {14, 13},
        {-63, 13},
        {136, -236},
        {273, 1065},
        {251, 149},
        {-25, -9},
        {9, -2},
        {-8, 7},
        {2, -10},
        {19, 35},
        {10, 2}
    };
    
    
    Jpluto(Location loc, jstars.util.Jlibastro.TimeData loctime) {
        super(loc, loctime, ephemObject.PLUTO);
    }
    
    
    @Override
    public String toString() {
        return "Pluto";
    }
    
    
    @Override
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
    
    @Override
    void calculateHeliocentric(double JDay) {
        double x,y,z;
        double l, b, r;
        double alpha, lambda, beta;
        
        double T = (JDay - 2451545.0) / 36525.0;
        double J =  34.35 + 3034.9057 * T;
        double S =  50.08 + 1222.1138 * T;
        double P = 238.96 +  144.9600 * T;
        
        l = b = r = 0.0;
        for(int i=0; i < arg.length; i++) {
            alpha = Math.toRadians(arg[i][0] * J + arg[i][1] * S + arg[i][2] * P);
            l += Lon[i][0] * Math.sin(alpha) + Lon[i][1] * Math.cos(alpha);
            b += Lat[i][0] * Math.sin(alpha) + Lat[i][1] * Math.cos(alpha);
            r += Radius[i][0] * Math.sin(alpha) + Radius[i][1] * Math.cos(alpha);
        }
        
        l = 238.958116 + 144.96 * T + l * 0.000001;
        b = -3.908239 + b * 0.000001;
        r = 40.7241346 + r * 0.0000001;
        
        // TO DO high prec
        
        x = sunDistance * Math.cos(Math.toRadians(B)) * Math.cos(Math.toRadians(L))
        - Earth.sunDistance * Math.cos(Math.toRadians(Earth.B)) * Math.cos(Math.toRadians(Earth.L));
        y = sunDistance * Math.cos(Math.toRadians(B)) * Math.sin(Math.toRadians(L))
        - Earth.sunDistance * Math.cos(Math.toRadians(Earth.B)) * Math.sin(Math.toRadians(Earth.L));
        z = sunDistance * Math.sin(Math.toRadians(B)) - Earth.sunDistance * Math.sin(Math.toRadians(Earth.B));
        
        earthDistance = Math.sqrt(x*x + y*y +z*z);
        
        eclLon = Jlibastro.norm360(l);
        eclLat = b;
        sunDistance = r;
    }
    
    @Override
    public double getPhase() {
        return 1.0;
    }
    
}
