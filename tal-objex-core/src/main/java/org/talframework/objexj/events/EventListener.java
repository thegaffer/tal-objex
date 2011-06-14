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
package org.talframework.objexj.events;

/**
 * This interface represents an event listener. Containers
 * generate events when they are saved. If there are any
 * event listeners configured (and the event listener is
 * configured for the type of event) then the event is
 * emitted. Event emitting is platform dependent.
 * 
 * <p>The event listener itself simply names a container
 * by ID, the name of the event processor that will process
 * the event inside that container, and a set of flags that
 * indicate what type of events the listener is interested
 * in. The event type can be on creation, on any edit, on
 * any state change and on a specific state change.</p>
 * 
 * <p>When an event is sent out a standard message is
 * included indicating the source container, the state it
 * is now in and a custom payload that is built up by
 * the container.</p>
 * 
 * @author Tom Spencer
 */
public interface EventListener {

    /**
     * @return The ID of the container to pass event to
     */
    public String getContainer();
    
    /**
     * @return The name of the event processor to ultimately emit
     */
    public String getEventProcessor();
    
    /**
     * @return Indicates if the event should be sent on creation
     */
    public boolean isOnCreation();
    
    /**
     * @return Indicates if the event should be sent on any state change
     */
    public boolean isOnStateChange();
    
    /**
     * @return The specific state changes that should trigger the event
     */
    public String[] getInterestedStates();
    
    /**
     * @return Indicates if the event should be sent on any edit
     */
    public boolean isOnEdit();
    
    /**
     * Note the event channel mechanism is very much a platform
     * specific mechanism and could mean a number of things. It
     * typically indicates how the message will get to the 
     * recipient container.
     * 
     * @return The channel to send the event on (may be null if it does not matter)
     */
    public String getChannel();
    
    /**
     * Not all platforms will support delay characteristics so
     * it should not be relied upon. 
     * 
     * @return The delay that should occur before it is processed
     */
    public long getDelay();
    
    /**
     * Setting an ETA is again a platform specific feature that
     * attempts to schedule the processing of the event to an
     * absolute time (in milliseconds from Julian Epoch). How
     * 'approximate' approximate is, is again platform dependent.
     * This is only really applicable for transaction specific 
     * listeners as registered or standard listeners will have 
     * no idea what the current 'time' is.
     * 
     * @return The approximate absolute time for processing to occur
     */
    public long getEta();
}
