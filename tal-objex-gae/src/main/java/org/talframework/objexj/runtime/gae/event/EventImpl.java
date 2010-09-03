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

package org.talframework.objexj.runtime.gae.event;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.talframework.objexj.events.Event;

/**
 * Implements the Event interface wrapping the request
 * object that contains the parameters.
 * 
 * @author Tom Spencer
 */
public final class EventImpl implements Event {
    
    /** Holds the event */
    private final String event;
    /** Holds the source containers ID */
    private final String sourceContainer;
    /** Holds the source status */
    private final String sourceStatus;
    /** Holds the request */
    private final HttpServletRequest request;
    
    public EventImpl(String event, String container, String status, HttpServletRequest request) {
        this.event = event;
        this.sourceContainer = container;
        this.sourceStatus = status;
        this.request = request;
    }

    public String getEventName() {
        return event;
    }
    
    public String getSourceContainer() {
        return sourceContainer;
    }
    
    public String getSourceStatus() {
        return sourceStatus;
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, String> getPayload() {
        Map<String, String> ret = new HashMap<String, String>();
        
        Enumeration e = request.getParameterNames();
        while( e.hasMoreElements() ) {
            String name = (String)e.nextElement();
            String val = request.getParameter(name);
            ret.put(name, val);
        }
        
        return ret.size() > 0 ? ret : null;
    }
    
    public String getPayload(String name) {
        return request.getParameter(name);
    }
}
