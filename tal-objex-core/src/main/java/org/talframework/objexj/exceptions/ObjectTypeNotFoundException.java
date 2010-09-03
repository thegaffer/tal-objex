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
 * This exception is thrown a type of object is not found.
 * This can be because we try and create of a type the
 * container has no knowledge of, or it may indicate a
 * configuration error.
 * 
 * @author Tom Spencer
 */
public final class ObjectTypeNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the type of object that was not found */
    private final String type;
    
    public ObjectTypeNotFoundException(String type) {
        super();
        this.type = type;
    }
    
    public ObjectTypeNotFoundException(String type, Exception cause) {
        super(cause);
        this.type = type;
    }
    
    @Override
    public String getMessage() {
        return "ObjectType [" + type + "] not found";
    }
}
