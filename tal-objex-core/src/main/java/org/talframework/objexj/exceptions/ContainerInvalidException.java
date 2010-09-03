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

import org.talframework.objexj.ValidationRequest;

/**
 * Indicates that an attempt to save a container failed 
 * because it has errors that must be resolved.
 * 
 * @author Tom Spencer
 */
public class ContainerInvalidException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the ID of the container that is invalid */
    private final String id;
    /** Holds the ValidationRequest with all errors on it */
    private final ValidationRequest request;
    
    public ContainerInvalidException(String id, ValidationRequest request) {
        super();
        this.id = id;
        this.request = request;
    }
    
    @Override
    public String getMessage() {
        return "Container [" + id + "] invalid: " + request;
    }
}
