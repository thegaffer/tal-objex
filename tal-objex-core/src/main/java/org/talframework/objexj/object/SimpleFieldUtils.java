package org.talframework.objexj.object;

import org.talframework.objexj.ObjexObjStateBean;

/**
 * This static class contains various methods that the
 * generated code makes use of when dealing with
 * simple fields. It can be used without using 
 * the generators if required.
 *
 * @author Tom Spencer
 */
public final class SimpleFieldUtils extends FieldUtils {

    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static <T> T setSimple(BaseObjexObj obj, ObjexObjStateBean state, T val, T current) {
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     * 
     * <p>Needed to avoid ambiguous methods due to autoboxing</p>
     */
    public static Double setSimple(BaseObjexObj obj, ObjexObjStateBean state, Double val, Double current) {
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static Float setSimple(BaseObjexObj obj, ObjexObjStateBean state, Float val, Float current) {
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static Long setSimple(BaseObjexObj obj, ObjexObjStateBean state, Long val, Long current) {
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static Integer setSimple(BaseObjexObj obj, ObjexObjStateBean state, Integer val, Integer current) {
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static Short setSimple(BaseObjexObj obj, ObjexObjStateBean state, Short val, Short current) {
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static Boolean setSimple(BaseObjexObj obj, ObjexObjStateBean state, Boolean val, Boolean current) {
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static Character setSimple(BaseObjexObj obj, ObjexObjStateBean state, Character val, Character current) {
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static Byte setSimple(BaseObjexObj obj, ObjexObjStateBean state, Byte val, Byte current) {
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static double setSimple(BaseObjexObj obj, ObjexObjStateBean state, double val, double current) {
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static float setSimple(BaseObjexObj obj, ObjexObjStateBean state, float val, float current) {
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static long setSimple(BaseObjexObj obj, ObjexObjStateBean state, long val, long current) {
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static int setSimple(BaseObjexObj obj, ObjexObjStateBean state, int val, int current) {
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static short setSimple(BaseObjexObj obj, ObjexObjStateBean state, short val, short current) {
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static boolean setSimple(BaseObjexObj obj, ObjexObjStateBean state, boolean val, boolean current) {
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static char setSimple(BaseObjexObj obj, ObjexObjStateBean state, char val, char current) {
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static byte setSimple(BaseObjexObj obj, ObjexObjStateBean state, byte val, byte current) {
        if( val != current ) setEditable(obj, state);
        return val;
    }
}
