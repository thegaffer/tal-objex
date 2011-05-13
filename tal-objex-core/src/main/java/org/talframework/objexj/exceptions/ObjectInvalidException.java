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
 * Generally indicates using an object that is not an ObjexObj
 * object.
 * 
 * @author Tom Spencer
 */
public final class ObjectInvalidException extends BaseObjexException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the message form the source */
    private final String message;
    
    public ObjectInvalidException(String message) {
        super();
        this.message = message;
    }
    
    public ObjectInvalidException(String message, Exception cause) {
        super(cause);
        this.message = message;
    }
    
    @Override
    public <T> T accept(ObjexExceptionVisitor visitor, Class<T> expected) {
        return visitor.visit(this, expected);
    }
    
    @Override
    public String getMessage() {
        return "Object is not a valid: " + message;
    }
}