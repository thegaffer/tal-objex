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
