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
 * Indicates that an attempt to obtain a named handler failed
 * because it was not found by the runtime environment. This
 * generally indicates some configuration error.
 * 
 * @author Tom Spencer
 */
public class EventHandlerNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the name of the event that was not found */
    private final String name;
    
    public EventHandlerNotFoundException(String name) {
        super();
        this.name = name;
    }
    
    public EventHandlerNotFoundException(String name, Exception cause) {
        super(cause);
        this.name = name;
    }
    
    @Override
    public String getMessage() {
        return "Event Handler [" + name + "] not found";
    }
}
