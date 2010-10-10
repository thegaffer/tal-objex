package org.talframework.objexj.object;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ValidationError;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.exceptions.ObjectErrorException;
import org.talframework.objexj.validation.CurrentValidationRequest;
import org.talframework.objexj.validation.SimpleValidationRequest;
import org.talframework.objexj.validation.groups.FieldChangeGroup;
import org.talframework.objexj.validation.groups.FieldGroup;

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
    public static <T> T setSimple(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, T val, T current) {
        if( !validateValue(obj, state, propertyName, val) ) return current;
        
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
    public static Double setSimple(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, Double val, Double current) {
        if( !validateValue(obj, state, propertyName, val) ) return current;
        
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static Float setSimple(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, Float val, Float current) {
        if( !validateValue(obj, state, propertyName, val) ) return current;
        
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static Long setSimple(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, Long val, Long current) {
        if( !validateValue(obj, state, propertyName, val) ) return current;
        
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static Integer setSimple(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, Integer val, Integer current) {
        if( !validateValue(obj, state, propertyName, val) ) return current;
        
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static Short setSimple(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, Short val, Short current) {
        if( !validateValue(obj, state, propertyName, val) ) return current;
        
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static Boolean setSimple(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, Boolean val, Boolean current) {
        if( !validateValue(obj, state, propertyName, val) ) return current;
        
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static Character setSimple(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, Character val, Character current) {
        if( !validateValue(obj, state, propertyName, val) ) return current;
        
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static Byte setSimple(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, Byte val, Byte current) {
        if( !validateValue(obj, state, propertyName, val) ) return current;
        
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static double setSimple(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, double val, double current) {
        if( !validateValue(obj, state, propertyName, val) ) return current;
        
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static float setSimple(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, float val, float current) {
        if( !validateValue(obj, state, propertyName, val) ) return current;
        
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static long setSimple(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, long val, long current) {
        if( !validateValue(obj, state, propertyName, val) ) return current;
        
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static int setSimple(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, int val, int current) {
        if( !validateValue(obj, state, propertyName, val) ) return current;
        
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static short setSimple(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, short val, short current) {
        if( !validateValue(obj, state, propertyName, val) ) return current;
        
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static boolean setSimple(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, boolean val, boolean current) {
        if( !validateValue(obj, state, propertyName, val) ) return current;
        
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static char setSimple(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, char val, char current) {
        if( !validateValue(obj, state, propertyName, val) ) return current;
        
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static byte setSimple(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, byte val, byte current) {
        if( !validateValue(obj, state, propertyName, val) ) return current;
        
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Validates the value.
     * 
     * @param obj The object we are validating
     * @param state The state bean of the object
     * @param propertyName The name of the property
     * @param val The value
     * @return True if all is ok with the value
     */
    private static boolean validateValue(BaseObjexObj obj, ObjexObjStateBean state, String propertyName, Object val) {
        ValidationRequest request = CurrentValidationRequest.getCurrent();
        
        Validator validator = null;
        if( request != null ) request.getValidator();
        else validator = Validation.buildDefaultValidatorFactory().getValidator();
        
        Set<?> violations = validator.validateValue(state.getClass(), propertyName, val, FieldChangeGroup.class, FieldGroup.class, Default.class);
        
        boolean ret = true;
        if( violations != null ) {
            Iterator<?> it = violations.iterator();
            while( it.hasNext() ) {
                Object v = it.next();
                if( !(v instanceof ConstraintViolation<?>) ) continue;
                
                ConstraintViolation<?> violation = (ConstraintViolation<?>)v;
                
                // TODO: Need to test no prop path = non-field error
                String prop = violation.getPropertyPath().toString();
                if( prop != null && prop.length() == 0 ) prop = null;
                
                Object[] params = violation.getInvalidValue() != null ? new Object[]{violation.getInvalidValue()} : null;
                
                // Add to errors
                if( request != null ) {
                    ValidationError error = new SimpleValidationRequest.SimpleValidationError(obj.getId(), violation.getMessageTemplate(), prop, params);
                    request.addError(error);
                    ret = false;
                }
                
                // Otherwise fail
                else {
                    throw new ObjectErrorException(violation.getMessageTemplate(), params);
                }
            }
        }
        
        return ret;
    }
}
