package org.tpspencer.tal.objexj.object;

import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.Map;

import org.tpspencer.tal.objexj.ObjexObjStateBean;

/**
 * Contains various helper and utility methods for dealing
 * with {@link ObjexObjStateBean}. Most notably there is
 * a bunch of methods for determining if a value has
 * changed or not.
 *
 * @author Tom Spencer
 */
public class StateBeanUtils {

    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(String oldVal, String newVal) {
        if( oldVal == newVal ) return false;
        if( oldVal != null && oldVal.equals(newVal) ) return false;
        return true;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(Date oldVal, Date newVal) {
        if( oldVal == newVal ) return false;
        if( oldVal != null && oldVal.equals(newVal) ) return false;
        return true;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(Currency oldVal, Currency newVal) {
        if( oldVal == newVal ) return false;
        if( oldVal != null && oldVal.equals(newVal) ) return false;
        return true;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(double oldVal, double newVal) {
        return oldVal != newVal;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(float oldVal, float newVal) {
        return oldVal != newVal;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(long oldVal, long newVal) {
        return oldVal != newVal;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(int oldVal, int newVal) {
        return oldVal != newVal;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(short oldVal, short newVal) {
        return oldVal != newVal;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(byte oldVal, byte newVal) {
        return oldVal != newVal;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(char oldVal, char newVal) {
        return oldVal != newVal;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(boolean oldVal, boolean newVal) {
        return oldVal != newVal;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(Double oldVal, Double newVal) {
        if( oldVal == newVal ) return false;
        if( oldVal != null && oldVal.equals(newVal) ) return false;
        return true;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(Float oldVal, Float newVal) {
        if( oldVal == newVal ) return false;
        if( oldVal != null && oldVal.equals(newVal) ) return false;
        return true;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(Long oldVal, Long newVal) {
        if( oldVal == newVal ) return false;
        if( oldVal != null && oldVal.equals(newVal) ) return false;
        return true;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(Integer oldVal, Integer newVal) {
        if( oldVal == newVal ) return false;
        if( oldVal != null && oldVal.equals(newVal) ) return false;
        return true;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(Short oldVal, Short newVal) {
        if( oldVal == newVal ) return false;
        if( oldVal != null && oldVal.equals(newVal) ) return false;
        return true;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(Character oldVal, Character newVal) {
        if( oldVal == newVal ) return false;
        if( oldVal != null && oldVal.equals(newVal) ) return false;
        return true;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(Byte oldVal, Byte newVal) {
        if( oldVal == newVal ) return false;
        if( oldVal != null && oldVal.equals(newVal) ) return false;
        return true;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(Boolean oldVal, Boolean newVal) {
        if( oldVal == newVal ) return false;
        if( oldVal != null && oldVal.equals(newVal) ) return false;
        return true;
    }
    
    /**
     * Compares the values to see if different.
     * 
     * SUGGEST: This is not accurate as list may not have changed, do we want this to be strong!
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(Collection<?> oldVal, Collection<?> newVal) {
        if( oldVal == newVal ) return false;
        return true;
        
        /*
        if( (oldVal == null && newVal != null) || (oldVal != null && newVal == null) ) return true;
        if( oldVal.size() != newVal.size() ) return true;
        
        boolean ret = false;
        Iterator<?> oldIt = oldVal.iterator();
        Iterator<?> newIt = oldVal.iterator();
        while( oldIt.hasNext() ) {
            Object old = oldIt.next();
            Object obj = newIt.next();
            
            if( (old == null && obj != null) || !old.equals(obj) ) {
                ret = true;
                break;
            }
        }
        
        return ret;
        */
    }
    
    /**
     * Compares the values to see if different.
     * 
     * SUGGEST: This is not accurate as list may not have changed, do we want this to be strong!
     * 
     * @param oldVal The old value
     * @param newVal The new value
     * @return True if it has changed, false otherwise
     */
    public static boolean hasChanged(Map<?, ?> oldVal, Map<?, ?> newVal) {
        if( oldVal == newVal ) return false;
        return true;
    }
}
