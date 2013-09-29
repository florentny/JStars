/*
 * $Id: Jlibastro.java,v 1.16 2010/01/30 21:39:17 fc Exp $
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
 */

package jstars.util;

import java.text.DecimalFormat;
import java.util.Calendar;



/**
 * Main astro library
 * @author Florent Charpin
 * @version 1.0
 */


public class Jlibastro {
    
    /**
     * Array of all major Solar System bodies.
     */
    public ephemObject SolSys[];
    
    /**
     * Date class used by Jlibastro to store date and time details
     */    
    static public class Jdate implements java.lang.Cloneable {
        static DecimalFormat TimeFormatter = new DecimalFormat("00");
        public int year;
        public int month;
        public int day;
        public int hour;
        public int min;
        public int sec;
        
        /**
         * Constructor for Jdate class
         */        
        public Jdate() {
            year = 1970;
            month = 1;
            day = 1;
            hour = 0;
            min = 0;
            sec = 0;
        }
        
        /**
         *
         * @return
         */
        @Override
        public Jdate clone() {
            try {
                return (Jdate) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new InternalError();
            }
            
        }
        
        /**
         *
         * @param addDate
         */
        public void add(Jdate addDate) {
            sec += addDate.sec;
            if(sec > 59) {
                min++;
                sec = sec - 59;
            }
            min += addDate.min;
            if(min > 59) {
                hour++;
                min = min - 59;
            }
            hour += addDate.hour;
            if(hour > 23) {
                day++;
                hour = hour - 24;
            }
            day += addDate.day;
            // TO DO
        }
        
        
        /**
         *
         * @return
         */
        public boolean isValidDate() {
            if((month < 1) || (month > 12))
                return false;
            if((day < 1) || (day > 31))
                return false;
            if((hour < 0) || (hour > 23))
                return false;
            if((min < 0) || (min > 59))
                return false;
            if((sec < 0) || (sec > 59))
                return false;
            switch(month) {
                case 4:
                case 6:
                case 9:
                case 11:
                    if(day == 31)
                        return false;
                    break;
                case 2:
                    if(day > 29)
                        return false;
                    if(day == 29)
                        if((year % 4) != 0)
                            return false;
                    break;
                default:
                    break;
            }
            return true;
        }
        
        /**
         *
         * @return
         */
        public boolean isValidTime() {
            
            if(day < 0)
                return false;
            if((hour < 0) || (hour > 23))
                return false;
            if((min < 0) || (min > 59))
                return false;
            if((sec < 0) || (sec > 59))
                return false;
            return true;
        }
        
        /**
         *
         * @param d
         */
        public void setTimeFromDouble(double d) {
            if(Double.isNaN(d)) {
                hour = Integer.MAX_VALUE;
                return;
            }
            
            if((d < 0.0) || (d >= 24.0)) {
                // TO DO
            }
            hour = (int) Math.floor(d);
            d -= Math.floor(d);
            min = (int) Math.floor(d * 60.0);
            d -= Math.floor(d * 60.0) /60.0;
            sec = (int) Math.floor(d * 3600.0);
        }
        
        /**
         *
         * @param r360
         * @return
         */
        public double getTimeInDegree(boolean r360) {
            double ret = hour;
            ret = ret + min / 60.0 + sec / 3600.0;
            ret = (ret / 24.0) * 360.0;
            if((r360 == false) && (ret > 180.0))
                ret -= 360.0;
            return ret;
        }
        
        /**
         *
         * @return
         */
        public String dispTimeHHMM() {
            return dispTimeHHMM(false, false);
        }
        
        /**
         *
         * @return
         */
        public String dispTimeHHMM_12() {
            return dispTimeHHMM(false, true);
        }
        
        /**
         *
         * @param round
         * @param ampm
         * @return
         */
        public String dispTimeHHMM(boolean round, boolean ampm) {
            if(hour == Integer.MAX_VALUE) {
                return "--:--";
            }
            int MM = min;
            int HH = hour;
            if((round) && (sec > 30)) {
                MM = (++MM) % 60;
                if(MM == 0)
                    HH = (++HH) % 24;
            }
            if(ampm) {
                if(HH >= 12) {
                    HH = HH -12;
                    return TimeFormatter.format((HH == 0) ? 12:HH) + ":" + TimeFormatter.format(MM) + " pm";
                }
                else
                    return TimeFormatter.format((HH == 0) ? 12:HH) + ":" + TimeFormatter.format(MM) + " am";
            }
            return TimeFormatter.format(HH) + ":" + TimeFormatter.format(MM);
        }
        
        /**
         *
         * @return
         */
        public String dispTimeHHMMSS() {
            return TimeFormatter.format(hour) + ":" + TimeFormatter.format(min)
            + ":" + TimeFormatter.format(sec);
        }
        
        /**
         *
         * @return
         */
        public String dispDDDHHMMSS() {
            return day + ":" + TimeFormatter.format(hour) + ":" + TimeFormatter.format(min)
            + ":" + TimeFormatter.format(sec);
        }
        
        /**
         *
         * @return
         */
        public int getTimeInSec() {
            return (hour * 3600) + (min * 60) + sec;
        }
        
        /**
         *
         * @return
         */
        public String dispDateString() {
            return TimeFormatter.format(month)
            + "/" + TimeFormatter.format(day)
            + "/" + TimeFormatter.format(year);
        }
        
        /**
         *
         * @return
         */
        public String dispDateMMDD() {
            return TimeFormatter.format(month)
            + "/" + TimeFormatter.format(day);
            
        }
        
    }
    
    /**
     * This class is used to store all the different dates needed by Jlibastor
     */    
    public class TimeData  {
        /** local date and time */
        final DecimalFormat TimeFormatter = new DecimalFormat("00");
        java.util.GregorianCalendar localDate;
        
        Jdate localJdate;
        Jdate localSideralTime;
        Jdate localSideralTime0_LT;
        Jdate UTDate;
        Jdate oldlocal;
        float tzOffset = 0f;
        /** Julian Day */
        double JD;
        /**
         * Julian Day at 00:00 UTC on the current day
         */
        double JD_0hUT;
        /**
         * Julian Day at 00:00 UTC on the next day
         */
        double JD_0hUT_1;
        /**
         * Julian Day ajusted with delta T
         */
        double JDE;
        /** Delta between dynamic and universal time */
        float DeltaT;
        Jdate GwSideralTime;
        Jdate GwSideralTime0_UT;
        Jdate GwSideralTime0_UT_1;
        
        private boolean dispLT = true;
        private boolean hourAMPM = false;
        public int gen = 0;
        
        /**
         * Constructor
         */        
        public TimeData() {
            localSideralTime = new Jdate();
            //localSideralTime0_UT = new Jdate();
            localSideralTime0_LT = new Jdate();
            UTDate = new Jdate();
            GwSideralTime = new Jdate();
            
            localJdate = new Jdate();
            GwSideralTime0_UT = new Jdate();
            GwSideralTime0_UT_1 = new Jdate();
        }
        
        /**
         *
         * @return
         */
        public Jdate copyLocalTime() {
            return (Jdate) localJdate.clone();
        }
        
        /**
         *
         * @return
         */
        public double getJDE() {
            return JDE;
        }
        /**
         *
         * @return
         */
        public String getDeltaT() {
            final DecimalFormat deltaFormat = new DecimalFormat("###0.0");
            return deltaFormat.format(DeltaT);
            
        }
        
        /**
         *
         * @return
         */
        public int getLTInSec() {
            return localJdate.getTimeInSec();
        }
        
        /**
         *
         * @return
         */
        public String getTZOffsetString() {
            StringBuffer str = new StringBuffer("");
            if(tzOffset < 0.0f)
                str.append("-");
            float f = Math.abs(tzOffset);
            str.append((int) Math.floor(f));
            str.append(":");
            str.append(TimeFormatter.format((f -Math.floor(f)) * 60));
            return str.toString();
        }
        
        /**
         *
         * @return
         */
        public String getLocalDateString() {
            return TimeFormatter.format(localDate.get(Calendar.MONTH) + 1)
            + "/" + TimeFormatter.format(localDate.get(Calendar.DAY_OF_MONTH))
            + "/" + TimeFormatter.format(localDate.get(Calendar.YEAR));
        }
        
        /**
         *
         * @return
         */
        public String getUTDateString() {
            return UTDate.dispDateString();
        }
        
        
        /**
         *
         * @return
         */
        public String getLocalTimeString() {
            if(this.isHourAMPM() == false)
                return TimeFormatter.format(localDate.get(Calendar.HOUR_OF_DAY))
                + ":" + TimeFormatter.format(localDate.get(Calendar.MINUTE))
                + ":" + TimeFormatter.format(localDate.get(Calendar.SECOND));
            else {
                int hh;
                String ampm;
                if(localDate.get(Calendar.HOUR_OF_DAY) >= 12) {
                    hh = localDate.get(Calendar.HOUR_OF_DAY) - 12;
                    ampm = " pm";
                }
                else {
                    ampm = " am";
                    hh = localDate.get(Calendar.HOUR_OF_DAY);
                }
                if(hh == 0)
                    hh = 12;
                return TimeFormatter.format(hh)
                + ":" + TimeFormatter.format(localDate.get(Calendar.MINUTE))
                + ":" + TimeFormatter.format(localDate.get(Calendar.SECOND)) + ampm;
                
                
            }
        }
        
        /**
         *
         * @return
         */
        public String getUTTime() {
            return TimeFormatter.format(UTDate.hour) + ":" + TimeFormatter.format(UTDate.min)
            + ":" + TimeFormatter.format(UTDate.sec);
        }
        
        /**
         *
         * @return
         */
        public String getLocalSideral() {
            return  TimeFormatter.format(localSideralTime.hour) + ":" + TimeFormatter.format(localSideralTime.min)
            + ":" + TimeFormatter.format(localSideralTime.sec);
        }
        
        /**
         *
         * @return
         */
        public String getLocalSideral0_LT() {
            return  TimeFormatter.format(localSideralTime0_LT.hour) + ":" + TimeFormatter.format(localSideralTime0_LT.min)
            + ":" + TimeFormatter.format(localSideralTime0_LT.sec);
        }
        
        
        /**
         *
         * @return
         */
        public double getCurrentYearDouble() {
            double ret;
            ret = (JD - Jtime.julianDate(localJdate.year, 1, 1)) / 365.65 + localJdate.year;
            return ret;
        }
        
        /** Getter for property hour24.
         * @return Value of property hour24.
         *
         */
        public boolean isHourAMPM() {
            return hourAMPM;
        }
        
        /**
         * Setter for property hour24.
         * @param hourAMPM
         */
        public void setHourAMPM(boolean hourAMPM) {
            this.hourAMPM = hourAMPM;
        }
        
        /** Getter for property dispLT.
         * @return Value of property dispLT.
         */
        public boolean isDispLT() {
            return dispLT;
        }
        
        /** Setter for property dispLT.
         * @param dispLT New value of property dispLT.
         */
        public void setDispLT(boolean dispLT) {
            this.dispLT = dispLT;
        }
        
        public Jdate getGWsideralTime() {
            return this.GwSideralTime;
        }
        
    }
    
    public Location userData;
    /**
     * The master time and date. All calculation in a Jlibastro instance are done for this time and date. Also refered to as the current time.
     * Not to be confused with the wallclock time.
     */
    public TimeData timeData;
    
    /** Creates a new instance of Jlibastro */
    public Jlibastro() {
        userData = new Location();
        timeData = new TimeData();
        timeData.localDate = new java.util.GregorianCalendar();
        SolSys = new ephemObject[11];
        SolSys[0] = new Jsun(userData, timeData);
        SolSys[1] = new Jplanet(userData, timeData, ephemObject.MERCURY);
        SolSys[2] = new Jplanet(userData, timeData, ephemObject.VENUS);
        SolSys[3] = new Jplanet(userData, timeData, ephemObject.EARTH);
        SolSys[4] = new Jplanet(userData, timeData, ephemObject.MARS);
        SolSys[5] = new Jjupiter(userData, timeData);
        SolSys[6] = new Jsaturn(userData, timeData);
        SolSys[7] = new Jplanet(userData, timeData, ephemObject.URANUS);
        SolSys[8] = new Jplanet(userData, timeData, ephemObject.NEPTUNE);
        SolSys[9] = new Jpluto(userData, timeData);
        SolSys[10] = new Jmoon(userData, timeData, (Jsun) SolSys[0]);
        for(int i = ephemObject.MERCURY; i <= ephemObject.PLUTO; i++) {
            if(i != ephemObject.EARTH)
                ((Jplanet)SolSys[i]).Earth = (Jplanet) SolSys[ephemObject.EARTH];
        }
        ((Jsun)SolSys[ephemObject.SUN]).Earth = (Jplanet) SolSys[ephemObject.EARTH];
        
        updateLocation();
        updateTime();
        setEquinoxToCurrentDate();
    }
    
    
    /**
     * Return version number of jlibastro
     * @return version nunber
     */
    public String version() {
        return "0.1";
    }
    
    /**
     * @return
     */
    public boolean updateAll() {
        
        updateTime();
        return true;
    }
    
    /**
     * Update local time and TZ based on the location
     */
    public void updateLocation() {
        
        // Setup  Time zone and Offset
        java.util.TimeZone TZ = java.util.SimpleTimeZone.getTimeZone(userData.localTZ);
        timeData.localDate.setTimeZone(TZ);
        int offset = TZ.getOffset(timeData.localDate.getTimeInMillis());
        timeData.tzOffset = offset / 3600000.0f;
        setEquinoxToCurrentDate();
    }
    
    public void setEquinoxToCurrentDate() {
        userData.setEquinox((double)(timeData.UTDate.year) + (timeData.UTDate.month * 10 / 12) / 10.0);
    }
    
    /** Set local time and date of user to current wall clock */
    public void setUserDateToWallClock() {
        timeData.localDate.setTimeInMillis((new java.util.Date()).getTime());
        updateTime();
    }
    
    /**
     * @param cal
     * @param adj
     */
    public void setUserDate(Jdate cal, boolean adj) {
        if(timeData.dispLT)
            timeData.localDate.set(cal.year, cal.month - 1, cal.day, cal.hour, cal.min, cal.sec);
        else {
            int hh = cal.hour + (int) Math.floor(timeData.tzOffset);
            timeData.localDate.set(cal.year, cal.month - 1, cal.day,hh , cal.min, cal.sec);
            /// TO DO
        }
        updateLocation();
        updateTime();
        if(adj) {
            if(timeData.dispLT) {
                cal.day = timeData.localDate.get(Calendar.DAY_OF_MONTH);
                cal.month = timeData.localDate.get(Calendar.MONTH) + 1;
                cal.year = timeData.localDate.get(Calendar.YEAR);
                cal.hour = timeData.localDate.get(Calendar.HOUR_OF_DAY);
                cal.min = timeData.localDate.get(Calendar.MINUTE);
                cal.sec = timeData.localDate.get(Calendar.SECOND);
            }
            else {
                cal.day = timeData.UTDate.day;
                cal.month = timeData.UTDate.month;
                cal.year = timeData.UTDate.year;
                cal.hour = timeData.UTDate.hour;
                cal.min = timeData.UTDate.min;
                cal.sec = timeData.UTDate.sec;
            }
        }
        
    }
    
    /**
     * Set a new location
     * @param loc New location
     */
    public void setLocation(Location loc) {
        userData = (Location) loc.clone();
        
    }
    
    /**
     * Return a copy of the current Location object
     * @return copy of current Location
     */
    public Location getLocation() {
        return (Location) userData.clone();
    }
    
    /**
     * Get short name of the current Time Zone
     * @return current time zone name
     */
    public String getCurrentTZName() {
        if(timeData.localDate.getTimeZone().inDaylightTime(timeData.localDate.getTime()) == true) {
            return timeData.localDate.getTimeZone().getDisplayName(true, java.util.TimeZone.SHORT);
        }
        else {
            return timeData.localDate.getTimeZone().getDisplayName(false, java.util.TimeZone.SHORT);
        }
    }
    
    /**
     * Return current time zone name or UTC if display mode is set to UTC
     * @return Time zone name
     */
    public String getTZdisp() {
        if(timeData.dispLT)
            return getCurrentTZName();
        return "UTC";
    }
    
    /**
     * This function is used to check if daylight saving time is in effect
     * @return true if DST is a effect
     */
    public boolean getDST() {
        return timeData.localDate.getTimeZone().inDaylightTime(timeData.localDate.getTime());
    }
    
    
    /**
     * Update all tiem and ephemeride data according to the master time.
     */
    public void updateTime() {
        
        // set localJdate
        timeData.localJdate.year = timeData.localDate.get(Calendar.YEAR);
        timeData.localJdate.month = timeData.localDate.get(Calendar.MONTH) + 1;
        timeData.localJdate.day = timeData.localDate.get(Calendar.DAY_OF_MONTH);
        timeData.localJdate.hour = timeData.localDate.get(Calendar.HOUR_OF_DAY);
        timeData.localJdate.min = timeData.localDate.get(Calendar.MINUTE);
        timeData.localJdate.sec = timeData.localDate.get(Calendar.SECOND);
        
        // calculate UT time/date
        timeData.UTDate.year = timeData.localJdate.year;
        timeData.UTDate.month = timeData.localJdate.month;
        timeData.UTDate.day = timeData.localJdate.day;
        timeData.UTDate.min = timeData.localJdate.min;
        timeData.UTDate.sec = timeData.localJdate.sec;
        // now the harder bit
        timeData.UTDate.hour = timeData.localJdate.hour - (int) Math.ceil(timeData.tzOffset);
        int minOffset = (int) ((Math.ceil(timeData.tzOffset) - timeData.tzOffset) * 60.0);
        if(minOffset != 0) {
            timeData.UTDate.min += minOffset;
            if(timeData.UTDate.min > 59) {
                timeData.UTDate.min -= 60;
                timeData.UTDate.hour++;
            }
        }
        if(timeData.UTDate.hour > 23) {
            timeData.UTDate.hour -= 24;
            timeData.UTDate.day++;
            switch(timeData.UTDate.month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                    if(timeData.UTDate.day == 32) {
                        timeData.UTDate.month++;
                        timeData.UTDate.day = 1;
                    }
                    break;
                case 12:
                    if(timeData.UTDate.day == 32) {
                        timeData.UTDate.month = 1;
                        timeData.UTDate.day = 1;
                        timeData.UTDate.year++;
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    if(timeData.UTDate.day == 31) {
                        timeData.UTDate.month++;
                        timeData.UTDate.day = 1;
                    }
                    break;
                case 2:
                    if(timeData.UTDate.day == 29) {
                        if((timeData.UTDate.year % 4 != 0) || (timeData.UTDate.year % 400 == 0)) {
                            timeData.UTDate.month++;
                            timeData.UTDate.day = 1;
                        }
                    }
                    break;
            }
        }
        
        if(timeData.UTDate.hour < 0) {
            timeData.UTDate.hour += 24;
            timeData.UTDate.day--;
            if(timeData.UTDate.day == 0) {
                switch(timeData.UTDate.month) {
                    case 1:
                        timeData.UTDate.month = 12;
                        timeData.UTDate.day = 31;
                        timeData.UTDate.year--;
                        break;
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        timeData.UTDate.month--;
                        timeData.UTDate.day = 30;
                        break;
                    case 2:
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        timeData.UTDate.month--;
                        timeData.UTDate.day = 31;
                        break;
                    case 3:
                        timeData.UTDate.month = 2;
                        if((timeData.UTDate.year % 4 != 0) || (timeData.UTDate.year % 400 == 0)) {
                            timeData.UTDate.day = 28;
                        }
                        else {
                            timeData.UTDate.day = 29;
                        }
                        break;
                }
            }
        }
        
        
        // calculate Julian Day
        double _df = timeData.UTDate.day
        + timeData.UTDate.hour / 24.0
        + timeData.UTDate.min / 1440.0
        + timeData.UTDate.sec / 86400.0;
        timeData.JD = Jtime.julianDate(timeData.UTDate.year, timeData.UTDate.month, _df);
        
        _df = (double) timeData.UTDate.day;
        timeData.JD_0hUT = Jtime.julianDate(timeData.UTDate.year, timeData.UTDate.month, _df);
        
        _df = (double) timeData.localJdate.day;
        timeData.JD_0hUT_1 = Jtime.julianDate(timeData.localJdate.year, timeData.localJdate.month, _df);
        
        
        // Calculate delta T
        timeData.DeltaT = (float) Jtime.deltaT(timeData.JD);
        
        // Calculate Julian day Ephemeris
        timeData.JDE = timeData.JD + (timeData.DeltaT / 86400.0);
        
        // calculate sideral times
        // Greenwich + local sideral times
        double Tsid = (timeData.JD - 2451545.0) / 36525.0;
        double delta0 = 280.46061837 + 360.98564736629 *(timeData.JD - 2451545.0)
        + 0.000387933 * Math.pow(Tsid, 2) - Math.pow(Tsid, 3) / 38710000.0;
        double sidH = (delta0 % 360.0) / 15.0;
        if(sidH < 0.0)
            sidH +=24.0;
        timeData.GwSideralTime.setTimeFromDouble(sidH);
        sidH -= (userData.longitude / 15.0);
        if(sidH < 0.0)
            sidH +=24.0;
        else if(sidH >= 24.0)
            sidH -=24.0;
        timeData.localSideralTime.setTimeFromDouble(sidH);
        // Greenwich sideral time at 0h UT
        Tsid = (timeData.JD_0hUT - 2451545.0) / 36525.0;
        delta0 = 100.46061837 + 36000.770053608 * Tsid
        + 0.000387933 * Math.pow(Tsid, 2) - Math.pow(Tsid, 3) / 38710000.0;
        sidH = (delta0 % 360.0) / 15.0;
        if(sidH < 0.0)
            sidH +=24.0;
        timeData.GwSideralTime0_UT.setTimeFromDouble(sidH);
        // Greenwich sideral time at 0h UT D-1
        Tsid = (timeData.JD_0hUT_1 - 2451545.0) / 36525.0;
        delta0 = 100.46061837 + 36000.770053608 * Tsid
        + 0.000387933 * Math.pow(Tsid, 2) - Math.pow(Tsid, 3) / 38710000.0;
        sidH = (delta0 % 360.0) / 15.0;
        if(sidH < 0.0)
            sidH +=24.0;
        timeData.GwSideralTime0_UT_1.setTimeFromDouble(sidH);
        // local sideral time at Oh local
        double JD_0L;
        if(timeData.tzOffset < 0.0)
            JD_0L = timeData.JD_0hUT_1;
        else
            JD_0L = timeData.JD_0hUT;
        JD_0L -= (timeData.tzOffset / 24.0);
        Tsid = (JD_0L - 2451545.0) / 36525.0;
        delta0 = 280.46061837 + 360.98564736629 *(JD_0L - 2451545.0)
        + 0.000387933 * Math.pow(Tsid, 2) - Math.pow(Tsid, 3) / 38710000.0;
        sidH = (delta0 % 360.0) / 15.0;
        if(sidH < 0.0)
            sidH +=24.0;
        sidH -= (userData.longitude / 15.0);
        if(sidH < 0.0)
            sidH +=24.0;
        else if(sidH >= 24.0)
            sidH -=24.0;
        timeData.localSideralTime0_LT.setTimeFromDouble(sidH);
    }
    
    /**
     * Returns the current julien day
     * @return julian day
     */
    public double julianDay() {
        return timeData.JD;
    }
    
    
    /**
     * Convert a Julien day to a JDate
     * @param jday Julien day
     * @return Jdate object
     */
    public Jdate fromJD(double jday) {
        Jdate jdate;
        if(timeData.dispLT == true)
            jdate = localFromJD(jday);
        else
            jdate = Jtime.UTFromJD(jday);
        return jdate;
    }
    
    /**
     *
     * @param jday
     * @return
     */
    public Jdate localFromJD(double jday) {
        Jdate jdate = Jtime.UTFromJD(jday + (timeData.tzOffset / 24.0));
        return jdate;
    }
    
    
    /**
     * @param arg
     * @param max
     * @return
     */
    public double fromHHMMSS(String arg, int max) {
        double ret = 0.0;
        int neg = 0;
        if(arg.length() < 5)
            return Double.NaN;
        StringBuffer str = new StringBuffer(arg.trim());
        if((str.charAt(0) == 'N') || (str.charAt(0) == 'W')|| (str.charAt(0) == '+')) {
            neg = 1;
        }
        else if((str.charAt(0) == 'S') || (str.charAt(0) == 'E')|| (str.charAt(0) == '-')) {
            neg = -1;
        }
        if(neg != 0) {
            str.delete(0, 1);
        }
        else {
            neg = 1;
        }
        
        java.util.StringTokenizer st = new java.util.StringTokenizer(str.toString(),":");
        try {
            if(st.hasMoreTokens()) {
                int main = Integer.parseInt(st.nextToken().trim());
                if((main < 0) || (main > max))
                    return Double.NaN;
                ret = (double) main;
            }
            if(st.hasMoreTokens()) {
                int min = Integer.parseInt(st.nextToken().trim());
                if((min < 0) || (min > 59))
                    return Double.NaN;
                ret += (double) min / 60.0;
            }
            else
                return Double.NaN;
            if(st.hasMoreTokens()) {
                int sec = Integer.parseInt(st.nextToken().trim());
                if((sec < 0) || (sec > 59))
                    return Double.NaN;
                ret += (double) sec / 3600.0;
            }
            if(st.hasMoreTokens()) {
                return Double.NaN;
            }
        }
        catch(java.lang.NumberFormatException ex) {
            return Double.NaN;
        }
        
        
        return ret * neg;
    }
    
    /**
     *
     * @param pos
     */
    public void updateAll(int pos) {
        SolSys[pos].UpdateAll();
    }
    
    /**
     *
     * @param dip
     * @param save
     * @return
     */
    public String[] getTwilightTime(double dip, boolean save) {
        Jsun sun = (Jsun) SolSys[ephemObject.SUN];
        return sun.getTwilightTime(dip, save);
    }
    
    /**
     *
     * @param RA
     * @param Dec
     * @return
     */
    public double[]  AzAlt(double RA, double Dec) {
        double H = timeData.localSideralTime.getTimeInDegree(true) - (RA / 24.0 * 360.0);
        return AzAlt(RA, Dec, H, userData.latitude, userData.longitude);
    }
    
    
    
    /**
     *
     * @param planet
     * @return
     */
    public double getEarthDistance(int planet) {
        if((planet) > 0 && (planet < 10))
            return ((Jplanet) SolSys[planet]).sunDistance;
        return Double.NaN;
    }
    
    /**
     *
     * @param planet
     * @return
     */
    public double getElongation(int planet) {
        double s = SolSys[ephemObject.SUN].eclLongitude;
        double p = SolSys[planet].eclLongitude;
        double ret = Double.NaN;
        
        if(p >= s)
            ret = p - s;
        else
            ret = (p - s) + 360.0;
        ret = (ret > 180.0) ? ret - 360 : ret;
        
        return ret;
    }
    
    /**
     *
     * @param planet
     * @return
     */
    public String getElongationString(int planet) {
        if(planet == ephemObject.SUN)
            return "";
        double elg = getElongation(planet);
        elg = (Math.round(elg * 10.0)) / 10.0;
        if(elg >= 0.0)
            return String.valueOf(elg) + " ev.";
        else
            return String.valueOf(elg * -1.0) + " mo.";
    }
    
    
    
    ///////////////////////////////////
    //
    //  Static functions
    //
    ///////////////////////////////////
    
    /**
     *
     * @param RA
     * @param Dec
     * @param H
     * @param lat
     * @param lon
     * @return
     */    
    static public double[]  AzAlt(double RA, double Dec, double H, double lat, double lon) {
        double ret[] = new double[2];
        double Hr = Math.toRadians(H);
        double latr = Math.toRadians(lat);
        double Decr = Math.toRadians(Dec);
        ret[0] = Math.atan2(Math.sin(Hr), (Math.cos(Hr) * Math.sin(latr) - Math.tan(Decr) * Math.cos(latr)));
        
        ret[1] = Math.sin(latr) * Math.sin(Decr) + Math.cos(latr) * Math.cos(Decr) * Math.cos(Hr);
        ret[1] = Math.asin(ret[1]);
        
        ret[0] = norm360(Math.toDegrees(ret[0]));
        ret[1] = Math.toDegrees(ret[1]);
        
        return ret;
    }
    
    /**
     *
     * @param deg
     * @return
     */    
    static public double norm360(double deg) {
        double _deg = deg % 360.0;
        if(_deg < 0.0)
            _deg += 360;
        return _deg;
        
    }
    
    static public double range180(double d) {
        double _deg = d % 360.0;
        if(_deg > 180.0)
            _deg = -360 + _deg;
        else if(_deg < -180.0)
            _deg = 360 + _deg;
        return _deg;
        
    }
    static double to24(double d) {
        double _d = d;
        if(!Double.isNaN(d)) {
            if(d < 0.0)
                _d += 24.0;
            _d = _d % 24;
        }
        return _d;
    }

    /**
     *
     * @param d
     * @return
     */
    static String dispHHMM(double d, boolean dispAMPM) {
        final DecimalFormat form00 = new DecimalFormat("00");
        double _d = d;
        StringBuffer str = new StringBuffer();
        if(!Double.isNaN(_d)) {
            if(_d < 0.0)
                str.append("-");
            _d = Math.abs(_d);
            if(!dispAMPM || ((_d < 13.0) && (_d >= 1.0))) {
                str.append(form00.format((Math.floor(_d))));
            } else {
                if(_d >= 13.0)
                    str.append(form00.format((Math.floor(_d)) - 12));
                else // midnight
                    str.append(form00.format(0.0));
            }
            str.append(":");
            _d -= Math.floor(_d);
            str.append(form00.format((Math.floor(_d * 60.0))));
            if(dispAMPM) {
                if(d < 13.0)
                    str.append(" am");
                else
                    str.append(" pm");
            }
            
        }
        else
            str.append("--:--");
        return str.toString();
    }
    
    /**
     *
     * @param d
     * @return
     */
    static String dispHHMMSS(double d) {
        final DecimalFormat form00 = new DecimalFormat("00");
        StringBuffer str = new StringBuffer();
        if(!Double.isNaN(d)) {
            if(d < 0.0)
                str.append("-");
            d = Math.abs(d);
            str.append(form00.format((Math.floor(d))));
            str.append(":");
            d -= Math.floor(d);
            str.append(form00.format((Math.floor(d * 60.0))));
            str.append(":");
            d -= Math.floor(d * 60.0) / 60.0;
            str.append(form00.format((Math.floor(d * 3600.0))));
        }
        else
            str.append("--:--:--");
        return str.toString();
    }
    
    /**
     *
     * @param d
     * @return
     */
    static String dispHHMMSS_SS(double d) {
        final DecimalFormat form00 = new DecimalFormat("00");
        final DecimalFormat form00_00 = new DecimalFormat("00.00");
        StringBuffer str = new StringBuffer();
        if(!Double.isNaN(d)) {
            if(d < 0.0)
                str.append("-");
            d = Math.abs(d);
            str.append(form00.format((Math.floor(d))));
            str.append(":");
            d -= Math.floor(d);
            str.append(form00.format((Math.floor(d * 60.0))));
            str.append(":");
            d -= Math.floor(d * 60.0) / 60.0;
            str.append(form00_00.format(d * 3600.0));
        }
        else
            str.append("--:--:--");
        return str.toString();
    }
    
    /**
     *
     * @param T
     * @return
     */
    static double obliquity(double T) {
        return ( 23.43929111 - 0.013004167 * T - 0.000000164 * Math.pow(T, 2) + 0.000005036* Math.pow(T, 3));
    }
    
    /** Convert from ecliptical to equatorial coordinates
     * @param obl
     * @param eclLongitude
     * @param eclLatitude
     * @return
     */
    static double[] eclipticalToEquatorial(double obl, double eclLon, double eclLat) {
        double ret[] = new double[2];
        obl = Math.toRadians(obl);
        eclLon = Math.toRadians(eclLon);
        eclLat = Math.toRadians(eclLat);
        double A = Math.cos(obl) * Math.sin(eclLon) - Math.tan(eclLat) * Math.sin(obl);
        double B = Math.cos(eclLon);
        ret[0] = Math.atan2(A, B);
        ret[0] = to24(Math.toDegrees(ret[0]) / 360.0 * 24.0);
        
        ret[1] = Math.sin(obl) * Math.sin(eclLon) * Math.cos(eclLat) + Math.sin(eclLat) * Math.cos(obl);
        ret[1] = Math.asin(ret[1]);
        ret[1] = Math.toDegrees(ret[1]);
        return ret;
    }
    
    public static boolean sphereproj(double A, double b, double elat, double[] res) {
        double c =  Math.PI/2.0 - elat;
        double cc = Math.cos(c);
        res[0] = Math.cos(b)*cc + Math.sin(b)*Math.sin(c)*Math.cos(A);
        if(res[0] > 1.0)
            res[0] = 1.0;
        if(res[0] < -1.0)
            res[0] = -1.0;
        if(cc>0.9999)
            res[1] = Math.PI - A;
        else if (cc<-.9999)
            res[1] = A;
        else {
            double sA = Math.sin(A);
            double x,y;
            y = sA*Math.sin(b)*Math.sin(c);
            x = Math.cos(b) - res[0]*cc;
            if(Math.abs(x) < 1e-5)
                res[1] = y < 0 ? 3*Math.PI/2 : Math.PI/2;
            else
                res[1] = Math.atan2(y,x);  
        }
        res[1] -= 2.0*Math.PI * Math.floor(res[1]/(Math.PI*2.0));
        // point visible?
        if(res[0]>0.0) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public static int distance(int x1, int y1, int x2, int y2) {
            return (int) Math.sqrt((Math.pow((x1 - x2),2) + Math.pow((y1 - y2),2)));
        }
    
}


