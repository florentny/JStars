/*
 * $Id: Jsaturn.java,v 1.4 2010/01/30 21:39:17 fc Exp $
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
public class Jsaturn extends Jplanet {
    
    Jsaturn(Location loc, jstars.util.Jlibastro.TimeData loctime) {
        super(loc, loctime, ephemObject.SATURN);
    }
    
    @Override
    public double getMagnitude() {
          return -8.88+ 5 * Math.log(earthDistance * sunDistance) /Math.log(10);
          // TO DO: ring correction
     }
    
}
