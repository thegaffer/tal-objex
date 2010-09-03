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

/**
 * This interface represents a validation error against
 * an object.
 *
 * @author Tom Spencer
 */
public interface ValidationError {

    /**
     * @return The ID of the object in error
     */
    public ObjexID getObjectId();
    
    /**
     * @return The field (relative to object) that caused the error
     */
    public String getError();
    
    /**
     * @return The name of the field (if any) that has the error
     */
    public String getField();
    
    /**
     * @return The parameters to the error code (if any)
     */
    public Object[] getParams();
    
    public static enum ErrorSeverity {
        ERROR,
        WARNING,
        INFO
    }
}
