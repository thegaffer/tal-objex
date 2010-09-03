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
 * Indicates that an attempt to load a known container failed
 * because it was not found by the runtime environment. This
 * generally indicates some programmatic error.
 * 
 * @author Tom Spencer
 */
public final class ObjectIDInvalidException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the ID of the object that is invalid */
    private final Object id;
    
    public ObjectIDInvalidException(Object id) {
        super();
        this.id = id;
    }
    
    public ObjectIDInvalidException(String id, Exception cause) {
        super(cause);
        this.id = id;
    }
    
    @Override
    public String getMessage() {
        return "Object ID [" + id + "] appears invalid for the container";
    }
}
