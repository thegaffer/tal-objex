/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.sample.gaetest.util;

import java.text.DateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility methods for dealing with a request
 * 
 * @author Tom Spencer
 */
public class RequestUtil {

    /**
     * Helper to obtain a valid parameter from the request.
     * 
     * @param req The request
     * @param param The parameter
     * @return Its value or null
     */
    public static String getParameter(HttpServletRequest req, String param) {
        String ret = req.getParameter(param);
        if( ret != null && ret.length() == 0 ) ret = null;
        return ret;        
    }
    
    /**
     * Helper to convert a parameter into a double
     * 
     * @param req The request
     * @param param The parameter
     * @return Its value or 0
     */
    public static double getDoubleParameter(HttpServletRequest req, String param) {
        String val = getParameter(req, param);
        if( val == null ) return 0.0;
        
        try {
            return Double.parseDouble(val);
        }
        catch( Exception e ) {
            return 0.0;
        }
    }
    
    /**
     * Helper to convert a parameter into a double
     * 
     * @param req The request
     * @param param The parameter
     * @return Its value or 0
     */
    public static long getLongParameter(HttpServletRequest req, String param) {
        String val = getParameter(req, param);
        if( val == null ) return 0;
        
        try {
            return Long.parseLong(val);
        }
        catch( Exception e ) {
            return 0;
        }
    }
    
    /**
     * Simple helper to convert a parameter into a date. Only
     * uses short date formats
     * 
     * @param req The request
     * @param param The parameter
     * @return Its value or null
     */
    public static Date getDateParameter(HttpServletRequest req, String param) {
        String val = getParameter(req, param);
        if( val == null ) return null;
        
        try {
            return DateFormat.getDateInstance(DateFormat.SHORT).parse(val);
        }
        catch( Exception e ) {
            return null;
        }
    }
}
