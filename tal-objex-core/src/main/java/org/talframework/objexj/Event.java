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
package org.talframework.objexj;

import java.util.Map;

/**
 * This interface represents an incoming event. Events in
 * Objex are emitted by containers when they are saved. This
 * causes (asynchronously) an event to be invoked on the
 * target container (which could be the same container as the
 * emitter), which then can use the parameters to exposed
 * by this interface to actually process the event. 
 * 
 * @author Tom Spencer
 */
public interface Event {
    
    /**
     * @return The name of this event
     */
    public String getEventName();

    /**
     * @return The ID of the source container
     */
    public String getSourceContainer();
    
    /**
     * @return The current status of the source container
     */
    public String getSourceStatus();
    
    /**
     * @return All payload values as a map
     */
    public Map<String, String> getPayload();
    
    /**
     * Call to get an individual item from the payload
     * 
     * @param name The name of the parameter
     * @return The payload value
     */
    public String getPayload(String name);
}
