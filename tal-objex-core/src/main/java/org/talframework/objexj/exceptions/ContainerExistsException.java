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
 * Indicates that an attempt to create a container that 
 * already exists has been made. If this occurs as part
 * of the initialisation and you are checking that
 * known stores exist you probably want to catch this
 * and move on.
 * 
 * @author Tom Spencer
 */
public final class ContainerExistsException extends BaseObjexException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the ID of the container that was not found */
    private final String id;
    
    public ContainerExistsException(String id) {
        super();
        this.id = id;
    }
    
    public ContainerExistsException(String id, Exception cause) {
        super(cause);
        this.id = id;
    }
    
    @Override
    public <T> T accept(ObjexExceptionVisitor visitor, Class<T> expected) {
        return visitor.visit(this, expected);
    }
    
    @Override
    public String getMessage() {
        return "Container [" + id + "] not found";
    }
}
