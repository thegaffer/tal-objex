package org.talframework.objexj.validation;

import java.util.List;
import java.util.Map;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ValidationError;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.exceptions.ObjectErrorException;
import org.talframework.objexj.exceptions.ObjectFieldErrorException;

/**
 * This class provides access to the current validation
 * request. In order not to polute the idea of a JavaBean
 * set method, implementations of set that perform field
 * level validate during the set in Objex objects will
 * query this class for the current validation request.
 * If there is one, they will record the error, otherwise
 * they will throw an exception.
 *
 * @author Tom Spencer
 */
public class CurrentValidationRequest implements ValidationRequest {
    /** Holds the thread local instance */
    private static ThreadLocal<ValidationRequest> request = new ThreadLocal<ValidationRequest>();
    
    /**
     * Call to start a new validation request. Although
     * the case for nesting validation requests is
     * dubious it is allowed for by returning the 
     * current value. You should wrap the call to
     * start/end in a try/finally block ...
     * 
     * <code><pre>
     * ValidationRequest previous = null;
     * try {
     *   previous = ValidationRequest.start(null);
     *   ... validate
     * }
     * finally {
     *   ValidationRequest.end(previous);
     * }
     * </pre></code>
     * 
     * <p>If the validation request passed in is null a
     * new default one is created.</p>
     * 
     * @param current The new validation request (if null a default one is created)
     * @return The previous validation request (if any)
     */
    public static ValidationRequest start(ValidationRequest current) {
        ValidationRequest ret = request.get();
        request.set(current != null ? current : new SimpleValidationRequest(ValidationType.FIELD));
        return ret;
    }
    
    /**
     * @return The current validation request (if any)
     */
    public static ValidationRequest getCurrent() {
        return request.get();
    }
    
    /**
     * Call to end the validation request. See start.
     * 
     * @param previous The previous validation request for thread or null
     */
    public static void end(ValidationRequest previous) {
        if( previous != null ) request.set(previous);
        else request.remove();
    }
    
    /**
     * {@inheritDoc}
     */
    public ValidationType getValidationType() {
        ValidationRequest req = CurrentValidationRequest.getCurrent();
        if( req != null ) return req.getValidationType();
        else return ValidationType.FIELD;
    }
    
    /**
     * {@inheritDoc}
     */
    public void addError(ObjexID id, String error, Object... params) {
        ValidationRequest req = CurrentValidationRequest.getCurrent();
        if( req == null ) throw new ObjectErrorException(error, params);
        req.addError(id, error, params);
    }
    
    /**
     * {@inheritDoc}
     */
    public void addFieldError(ObjexID id, String field, String error, Object val) {
        ValidationRequest req = CurrentValidationRequest.getCurrent();
        if( req == null ) throw new ObjectFieldErrorException(field, error, val);
        req.addFieldError(id, field, error, val);
    }
    
    /**
     * {@inheritDoc}
     */
    public void addError(ValidationError error) {
        ValidationRequest req = CurrentValidationRequest.getCurrent();
        if( req == null ) throw new ObjectErrorException(error.getError(), error.getParams());
        req.addError(error);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean hasErrors() {
        ValidationRequest req = CurrentValidationRequest.getCurrent();
        if( req != null ) return req.hasErrors();
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean hasErrors(ObjexID id) {
        ValidationRequest req = CurrentValidationRequest.getCurrent();
        if( req != null ) return req.hasErrors(id);
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean hasErrors(ObjexID id, String field) {
        ValidationRequest req = CurrentValidationRequest.getCurrent();
        if( req != null ) return req.hasErrors(id, field);
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<ValidationError> getErrors() {
        ValidationRequest req = CurrentValidationRequest.getCurrent();
        if( req != null ) return req.getErrors();
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    public Map<ObjexID, List<ValidationError>> getErrorMap() {
        ValidationRequest req = CurrentValidationRequest.getCurrent();
        if( req != null ) return req.getErrorMap();
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<ValidationError> getErrors(ObjexID id) {
        ValidationRequest req = CurrentValidationRequest.getCurrent();
        if( req != null ) return req.getErrors(id);
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<ValidationError> getErrors(ObjexID id, String field) {
        ValidationRequest req = CurrentValidationRequest.getCurrent();
        if( req != null ) return req.getErrors(id, field);
        return null;
    }
}
