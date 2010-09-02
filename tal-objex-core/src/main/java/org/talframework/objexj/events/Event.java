package org.tpspencer.tal.objexj.events;

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
