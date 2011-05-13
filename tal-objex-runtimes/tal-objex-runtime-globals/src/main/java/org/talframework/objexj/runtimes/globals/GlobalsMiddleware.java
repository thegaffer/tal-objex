package org.talframework.objexj.runtimes.globals;

import java.util.Map;

import org.talframework.objexj.container.ContainerMiddleware;

/**
 * This class represents the middleware when using the Globals DB.
 *
 * @author Tom Spencer
 */
public class GlobalsMiddleware implements ContainerMiddleware {

    /** Contains the map of object strategies we use in this container */
    private Map<String, GlobalsObjectStrategy> globalsStrategies;
    
}
