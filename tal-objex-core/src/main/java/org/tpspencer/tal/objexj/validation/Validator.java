package org.tpspencer.tal.objexj.validation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ValidationError;
import org.tpspencer.tal.objexj.ValidationRequest;
import org.tpspencer.tal.objexj.ValidationRequest.ValidationType;

/**
 * This helper class can be used to perform the validation
 * of one or more objects.
 *
 * @author Tom Spencer
 */
public class Validator {
    
    /**
     * Call to validate a single object with the
     * given level of validation.
     * 
     * @param type The type of validation to perform
     * @param objs The object to validate
     */
    public static List<ValidationError> validate(ValidationType type, ObjexObj obj) {
        if( obj == null ) return null;
        
        ValidationRequest previous = null;
        ValidationRequest current = new SimpleValidationRequest(type);
        
        try {
            previous = CurrentValidationRequest.start(current);
            obj.validate(current);
        }
        finally {
            CurrentValidationRequest.end(previous);
        }
        
        return current.getErrors();
    }
    
    /**
     * Call to validate an array of objects with the
     * given level of validation.
     * 
     * @param type The type of validation to perform
     * @param objs The objects to validate
     */
    public static List<ValidationError> validate(ValidationType type, ObjexObj... objs) {
        if( objs == null || objs.length == 0 ) return null;
        
        ValidationRequest previous = null;
        ValidationRequest current = new SimpleValidationRequest(type);
        
        try {
            previous = CurrentValidationRequest.start(current);
            
            for( ObjexObj objexObj : objs ) {
                objexObj.validate(current);
            }
        }
        finally {
            CurrentValidationRequest.end(previous);
        }
        
        return current.getErrors();
    }
    
    /**
     * Call to validate a collection of objects with the
     * given level of validation.
     * 
     * @param type The type of validation to perform
     * @param objs The objects to validate
     */
    public static List<ValidationError> validate(ValidationType type, Collection<ObjexObj> objs) {
        if( objs == null || objs.size() == 0 ) return null;
        
        ValidationRequest previous = null;
        ValidationRequest current = new SimpleValidationRequest(type);
        
        try {
            previous = CurrentValidationRequest.start(current);
            
            Iterator<ObjexObj> it = objs.iterator();
            while( it.hasNext() ) {
                it.next().validate(current);
            }
        }
        finally {
            CurrentValidationRequest.end(previous);
        }
        
        return current.getErrors();
    }
}
