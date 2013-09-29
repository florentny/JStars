/*
 * $Id: Jtime.java,v 1.4 2004/03/10 04:47:35 fc Exp $
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
 * @author  florent
 */

public final class Jtime {
    
    static final double DT1900[] = {
        -2.8, -0.1, 2.6, 5.3,
        7.7, 10.4, 13.3, 16.0,
        18.2, 20.2, 21.1, 22.4,
        23.5, 23.8, 24.3, 24.0,
        23.9, 23.9, 23.7, 24.0,
        24.3, 25.3, 26.2, 27.3,
        28.2, 29.1, 30.0, 30.7,
        31.4, 32.2, 33.1, 34.0,
        35.0, 36.5, 38.3, 40.2,
        42.2, 44.5, 46.5, 48.5,
        50.5, 52.2, 53.8, 54.9,
        55.8, 56.9, 58.3, 60.0,
        61.6, 63.0, 65.0
    };
    
    /** Static Methods only */
    private Jtime() {
    }
    
    static public double julianDate(int Y, int M, double D) {
        
        if(M <= 2) {
            Y = Y - 1;
            M = M + 12;
        }
        double B;
        B = 2.0 - Math.floor(Y / 100.0) + Math.floor(Math.floor(Y / 100.0) / 4.0);
        return  Math.floor(365.25 * (Y + 4716)) + Math.floor(30.6001 * (M + 1)) + D + B -1524.5;
    }
    
    public static jstars.util.Jlibastro.Jdate UTFromJD(double jday) {
        jstars.util.Jlibastro.Jdate jdate = new jstars.util.Jlibastro.Jdate();
        int A,B,C,D,E,Z;
        double F;
        Z = (int) (jday + 0.5);
        F = (jday + 0.5) - Z;
        if(Z < 2299161) {
            A = Z;
        }
        else {
            int alpha = (int) ((Z - 1867216.25) / 36524.25);
            A = Z + 1 + alpha - (int) (alpha / 4.0);
        }
        B = A + 1524;
        C = (int)((B - 122.1) / 365.25);
        D = (int)(365.25 * C);
        E = (int)((B-D) / 30.6001);
        if(E < 14)
            jdate.month = E - 1;
        else
            jdate.month = E - 13;
        if(jdate.month > 2)
            jdate.year = C - 4716;
        else
            jdate.year = C - 4715;
        double day = B - D - (int)(30.6001 * E) + F;
        jdate.day = (int) day;
        jdate.hour = (int)(F * 24);
        jdate.min = (int)(((F * 24) - jdate.hour) * 60);
        
        return jdate;
    }
    
    static public double deltaT(double JD) {
        double ret = 0.0;
        
        // 2000 - ????
        if(JD > 2451545.0) {
            double t = (JD - 2451545.0) / 36525.0;
            ret = 102.0 + 105*t + 25.3*t*t;
            ret = ret + 0.37 * (JD - 2488070) / 365.25;
        }
        // 1900-2000
        else if(JD > 2415021.0) {
            int i = (int)((JD - 2415021.5) / 730.5);
            if (i > (DT1900.length - 3)) {
                i = DT1900.length - 3;
            }
            double a = DT1900[i+1] - DT1900[i];
            double b = DT1900[i+2] - DT1900[i+1];
            double c = b - a;
            double n = ((JD - 2415021.5) / 730.5) - (i + 1.0);
            ret = DT1900[i+1] + n / 2 * (a + b + n * c);
            
            
        }
        
        return ret;
    }
    
}
