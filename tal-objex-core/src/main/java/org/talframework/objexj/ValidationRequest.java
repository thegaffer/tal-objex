/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.objexj;

import java.util.List;
import java.util.Map;


/**
 * This class represents a request to validate an object
 * or object(s) inside a container.
 *
 * @author Tom Spencer
 */
public interface ValidationRequest {
    
    /**
     * This enumeration represents the different levels of
     * validation. Field level validation only validates
     * each field. Intra-object level validation validates
     * that the object is valid against other fields in the
     * same object. Whilst inter-object validation validates
     * against the fields.
     *
     * @author Tom Spencer
     */
    public static enum ValidationType {
        FIELD,          /** Field validation only */
        INTRA_OBJECT,   /** Values correct within the same object */
        INTER_OBJECT,   /** Values correct against other objects */
        CHILDREN        /** Children of the object are ok relative to parent */
    }
    
    /**
     * @return The type of validation to perform
     */
    public ValidationType getValidationType();
    
    /**
     * Call to record a generic error
     * 
     * @param id The object generating the error
     * @param error The error code
     * @param params The parameters for the error
     */
    public void addError(ObjexID id, String error, Object... params);
    
    /**
     * Call to record an invalid field value
     * 
     * @param id The object generating the error
     * @param field The name of the field
     * @param error The error code
     * @param val The offending value
     */
    public void addFieldError(ObjexID id, String field, String error, Object val);
    
    /**
     * Adds a pre-constructed error against the object
     * 
     * @param error The error to log
     */
    public void addError(ValidationError error);
    
    /**
     * Determines if there are any validation errors
     * 
     * @return True if there are any errors, false otherwise
     */
    public boolean hasErrors();
    
    /**
     * Determines if there are any validation errors
     * against a specific object.
     * 
     * @param id The ID of the object to check for
     * @return True if the object does have errors against it
     */
    public boolean hasErrors(ObjexID id);
    
    /**
     * Determines if there are any validation errors
     * against a field on a specific object.
     * 
     * @param id The ID of the object to check for
     * @param field The name of the field to check for
     * @return True if the object does have errors against it
     */
    public boolean hasErrors(ObjexID id, String field);
    
    /**
     * @return The errors that have been logged
     */
    public List<ValidationError> getErrors();
    
    /**
     * @return All errors in a map keyed by the object ID
     */
    public Map<ObjexID, List<ValidationError>> getErrorMap();
    
    /**
     * Returns the errors logged against a specific 
     * object in the container.
     * 
     * @param id The ID the object to check for
     * @return A list of any errors
     */
    public List<ValidationError> getErrors(ObjexID id);
    
    /**
     * Returns all the errors on a particular object 
     * against a particular field.
     * 
     * @param id The ID of the object
     * @param field The field we want errors for
     * @return The errors for that field
     */
    public List<ValidationError> getErrors(ObjexID id, String field);
}
