package org.talframework.objexj.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ValidationError;
import org.talframework.objexj.ValidationRequest;

/**
 * This class implements the validation request as simply
 * as possible.
 *
 * @author Tom Spencer
 */
public final class SimpleValidationRequest implements ValidationRequest {
    
    /** Holds the type of validation being performed */
    private ValidationType type;
    /** Holds all the errors, keyed by the object ID */
    private Map<ObjexID, List<ValidationError>> errors;
    
    public SimpleValidationRequest(ValidationType initialType) {
        this.type = initialType;
    }
    
    /**
     * Clears any errors against a specific object.
     * 
     * @param id The ID of the object
     */
    public void clearErrors(ObjexID id) {
        if( errors != null && errors.containsKey(id) ) errors.remove(id);
    }
    
    /**
     * Call to set the validation type. This allows 
     * the same validation request to be used for 
     * full validation.
     * 
     * @param type The new type
     */
    public void setType(ValidationType type) {
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    public ValidationType getValidationType() {
        return type;
    }
    
    /**
     * {@inheritDoc}
     */
    public void addError(ObjexID id, String error, Object... params) {
        if( id == null ) throw new IllegalArgumentException("Cannot log an error against a null object");
        if( error == null ) throw new IllegalArgumentException("Cannot pass a null error into validation request");
        
        if( errors == null ) errors = new HashMap<ObjexID, List<ValidationError>>();
        
        List<ValidationError> objectErrors = errors.get(id);
        if( objectErrors == null ) {
            objectErrors = new ArrayList<ValidationError>();
            errors.put(id, objectErrors);
        }
        
        objectErrors.add(new SimpleValidationError(id, error, null, params));
    }
    
    /**
     * {@inheritDoc}
     */
    public void addFieldError(ObjexID id, String field, String error, Object val) {
        if( id == null ) throw new IllegalArgumentException("Cannot log a field error against a null object");
        if( error == null ) throw new IllegalArgumentException("Cannot pass a null error into validation request");
        if( field == null ) throw new IllegalArgumentException("Cannot pass a null field into validation request");
        
        if( errors == null ) errors = new HashMap<ObjexID, List<ValidationError>>();
        
        List<ValidationError> objectErrors = errors.get(id);
        if( objectErrors == null ) {
            objectErrors = new ArrayList<ValidationError>();
            errors.put(id, objectErrors);
        }
        
        objectErrors.add(new SimpleValidationError(id, error, field, val));
    }
    
    /**
     * {@inheritDoc}
     */
    public void addError(ValidationError error) {
        if( error == null ) throw new IllegalArgumentException("Cannot pass a null error into validation request");
        
        if( errors == null ) errors = new HashMap<ObjexID, List<ValidationError>>();
        
        ObjexID id = error.getObjectId();
        if( errors.containsKey(id) ) errors.get(id).add(error);
        else {
            List<ValidationError> e = new ArrayList<ValidationError>();
            e.add(error);
            errors.put(id, e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void addErrors(ObjexID id, List<ValidationError> objectErrors) {
        if( id == null ) throw new IllegalArgumentException("Cannot log errors against a null object");
        if( objectErrors == null ) throw new IllegalArgumentException("Cannot pass a null errors list into validation request");
        if( objectErrors.size() == 0 ) return;
        
        if( errors == null ) errors = new HashMap<ObjexID, List<ValidationError>>();
        
        if( errors.containsKey(id) ) errors.get(id).addAll(objectErrors);
        else errors.put(id, objectErrors);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean hasErrors() {
        return errors != null && errors.size() > 0;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean hasErrors(ObjexID id) {
        return errors != null && errors.containsKey(id);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean hasErrors(ObjexID id, String field) {
        List<ValidationError> errors = getErrors(id);
        if( errors != null ) {
            Iterator<ValidationError> it = errors.iterator();
            while( it.hasNext() ) {
                if( field.equals(it.next().getField()) ) return true;
            }
        }

        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<ValidationError> getErrors() {
        List<ValidationError> ret = null;
        
        if( errors != null ) {
            Iterator<ObjexID> it = errors.keySet().iterator();
            while( it.hasNext() ) {
                if( ret == null ) ret = errors.get(it.next());
                else ret.addAll(errors.get(it.next()));
            }
        }
        
        return ret;
    }
    
    /**
     * {@inheritDoc}
     */
    public Map<ObjexID, List<ValidationError>> getErrorMap() {
        return errors != null ? new HashMap<ObjexID, List<ValidationError>>(errors) : null;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<ValidationError> getErrors(ObjexID id) {
        return errors != null ? errors.get(id) : null;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<ValidationError> getErrors(ObjexID id, String field) {
        if( errors == null || !errors.containsKey(id) ) return null;
        
        List<ValidationError> ret = null;
        List<ValidationError> errors = this.errors.get(id);
        Iterator<ValidationError> it = errors.iterator();
        while( it.hasNext() ) {
            ValidationError err = it.next();
            if( field.equals(err.getField()) ) {
                if( ret == null ) ret = new ArrayList<ValidationError>();
                ret.add(err);
            }
        }
        
        return ret;
    }
    
    /**
     * Simple validation error implementation class
     *
     * @author Tom Spencer
     */
    public final static class SimpleValidationError implements ValidationError {
        
        private final ObjexID objectId;
        private final String error;
        private final String field;
        private final Object[] params;
        
        public SimpleValidationError(ObjexID objectId, String error, String field, Object... params) {
            this.objectId = objectId;
            this.error = error;
            this.field = field;
            this.params = params;
        }

        /**
         * @return the objectId
         */
        public ObjexID getObjectId() {
            return objectId;
        }

        /**
         * @return the error
         */
        public String getError() {
            return error;
        }

        /**
         * @return the field
         */
        public String getField() {
            return field;
        }

        /**
         * @return the params
         */
        public Object[] getParams() {
            return params;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "SimpleValidationError [error=" + error + ", field=" + field + ", objectId=" + objectId + ", params=" + Arrays.toString(params) + "]";
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((error == null) ? 0 : error.hashCode());
            result = prime * result + ((field == null) ? 0 : field.hashCode());
            result = prime * result + ((objectId == null) ? 0 : objectId.hashCode());
            result = prime * result + Arrays.hashCode(params);
            return result;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if( this == obj ) return true;
            if( obj == null ) return false;
            if( getClass() != obj.getClass() ) return false;
            SimpleValidationError other = (SimpleValidationError)obj;
            if( error == null ) {
                if( other.error != null ) return false;
            }
            else if( !error.equals(other.error) ) return false;
            if( field == null ) {
                if( other.field != null ) return false;
            }
            else if( !field.equals(other.field) ) return false;
            if( objectId == null ) {
                if( other.objectId != null ) return false;
            }
            else if( !objectId.equals(other.objectId) ) return false;
            if( !Arrays.equals(params, other.params) ) return false;
            return true;
        }
    }
}
