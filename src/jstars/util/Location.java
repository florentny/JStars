/*
 * $Id: Location.java,v 1.10 2010/01/30 21:39:17 fc Exp $
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
 *
 * @author Florent Charpin
 */


public class Location implements java.lang.Cloneable {
    
    // location data
    public String name;
    double latitude;
    double longitude;
    double elevation;
    double equinox;
    /** Name of the local TZ */
    String localTZ;
    
    public int gen = 0;
    
    // property function
    
    public double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(double d) {
        latitude = d;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(double d) {
        longitude = d;
    }
    
    public String getLocalTZ() {
        return localTZ;
    }
    
    public void setLocalTZ(String str) {
        localTZ = str;
    }
    
    /** Creates a new instance of Location */
    public Location() {
        // default values - NYC
        name = "New York, NY";
        latitude = 40.751666667; // N 40:45:06
        longitude = 73.99417; // W 73:59:39
        elevation = 25f; // 25m
        equinox = 2000.0f;
        localTZ = "America/New_York";
    }
    
    public String latString() {
        final java.text.DecimalFormat form00 = new java.text.DecimalFormat("00");
        StringBuffer str = new StringBuffer();
        
        if(latitude < 0)
            str.append("S ");
        else
            str.append("N ");
        
        double lat = Math.abs(latitude);
        str.append(form00.format((Math.floor(lat))));
        str.append(":");
        lat -= Math.floor(lat);
        str.append(form00.format((Math.floor(lat * 60.0))));
        str.append(":");
        lat -= Math.floor(lat * 60.0) / 60.0;
        str.append(form00.format((Math.floor(lat * 3600.0))));
        
        return str.toString();
    }
    
    public String lonString() {
        final java.text.DecimalFormat form00 = new java.text.DecimalFormat("#00");
        int min,sec = 0;
        StringBuffer str = new StringBuffer();
        
        if(longitude < 0)
            str.append("E ");
        else
            str.append("W ");
        
        double lon = Math.abs(longitude);
        str.append(form00.format((Math.floor(lon))));
        str.append(":");
        lon -= Math.floor(lon);
        min = (int) (Math.floor(lon * 60.0));
        lon -= Math.floor(lon * 60.0) / 60.0;
        sec = (int) (Math.round(lon * 3600.0));
        if(sec == 60) {
            sec = 0;
            min++;
        }
        str.append(form00.format(min));
        str.append(":");
        str.append(form00.format(sec));
        
        return str.toString();
    }
    
    @Override
    public Location clone() {
        
        try {
            Location l = (Location) super.clone();
            return l;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        
    }
    
    /**
     * Getter for property equinox.
     * @return Value of property equinox.
     */
    public double getEquinox() {
        return equinox;
    }
    
    /**
     * Setter for property equinox.
     * @param equinox New value of property equinox.
     */
    public void setEquinox(double equinox) {
        this.equinox = equinox;
    }
    
}
