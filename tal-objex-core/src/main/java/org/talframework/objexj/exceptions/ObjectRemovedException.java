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
 * Indicates that an attempt to use an object that has been
 * removed. 
 * 
 * @author Tom Spencer
 */
public final class ObjectRemovedException extends BaseObjexException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the ID of the container */
    private final String containerId;
    /** Holds the ID of the object that was not found */
    private final String id;
    
    public ObjectRemovedException(String containerId, String id) {
        super();
        this.containerId = containerId;
        this.id = id;
    }
    
    @Override
    public <T> T accept(ObjexExceptionVisitor visitor, Class<T> expected) {
        return visitor.visit(this, expected);
    }
    
    @Override
    public String getMessage() {
        return "Object [" + id + "] already removed in transaction [" + containerId + "]";
    }
}
