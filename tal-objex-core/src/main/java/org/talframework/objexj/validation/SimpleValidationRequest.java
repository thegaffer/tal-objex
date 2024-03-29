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
package org.talframework.objexj.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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
    /** The validator */
    private Validator validator;
    
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
    
    public javax.validation.Validator getValidator() {
        if( validator == null ) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
        
        return validator;
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
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((errors == null) ? 0 : errors.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((validator == null) ? 0 : validator.hashCode());
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
        SimpleValidationRequest other = (SimpleValidationRequest)obj;
        if( errors == null ) {
            if( other.errors != null ) return false;
        }
        else if( !errors.equals(other.errors) ) return false;
        if( type == null ) {
            if( other.type != null ) return false;
        }
        else if( !type.equals(other.type) ) return false;
        if( validator == null ) {
            if( other.validator != null ) return false;
        }
        else if( !validator.equals(other.validator) ) return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SimpleValidationRequest [errors=" + errors + "]";
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
            ValidationError other = (ValidationError)obj;
            if( error == null ) {
                if( other.getError() != null ) return false;
            }
            else if( !error.equals(other.getError()) ) return false;
            if( field == null ) {
                if( other.getField() != null ) return false;
            }
            else if( !field.equals(other.getField()) ) return false;
            if( objectId == null ) {
                if( other.getObjectId() != null ) return false;
            }
            else if( !objectId.equals(other.getObjectId()) ) return false;
            if( !Arrays.equals(params, other.getParams()) ) return false;
            return true;
        }
    }
}
