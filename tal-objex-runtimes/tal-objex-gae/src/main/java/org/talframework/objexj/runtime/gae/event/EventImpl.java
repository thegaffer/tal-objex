/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.runtime.gae.event;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.talframework.objexj.Event;

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
