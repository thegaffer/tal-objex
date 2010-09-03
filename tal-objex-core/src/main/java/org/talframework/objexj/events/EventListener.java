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
