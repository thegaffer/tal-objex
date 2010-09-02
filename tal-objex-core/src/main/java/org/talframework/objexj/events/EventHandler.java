package org.talframework.objexj.events;

import org.talframework.objexj.Container;

/**
 * This interface represents something that can handle
 * an event. Handlers are typically registered against
 * a container by name and then selected based upon 
 * the name in the incoming event.
 * 
 * @author Tom Spencer
 */
public interface EventHandler {

    public void execute(Container container, Event event);
}
