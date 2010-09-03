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

package org.talframework.objexj.exceptions;

/**
 * This exception represents an attempt to access an invalid
 * field - either because the field does not exist, is not
 * readable or is not writeable. It generally reflects a
 * programming or other error.
 *
 * @author Tom Spencer
 */
public final class ObjectFieldInvalidException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the field name */
    private final String field;
    /** The reason it is invalid */
    private final String reason;
    
    public ObjectFieldInvalidException(String field, String reason) {
        super();
        this.field = field;
        this.reason = reason;
    }
    
    public ObjectFieldInvalidException(String field, String reason, Exception cause) {
        super(cause);
        this.field = field;
        this.reason = reason;
    }
    
    @Override
    public String getMessage() {
        return "ObjectFieldInvalid for field [" + field + "]: " + reason;
    }
}
