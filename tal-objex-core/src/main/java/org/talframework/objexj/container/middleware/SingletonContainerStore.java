package org.talframework.objexj.container.middleware;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talframework.objexj.ObjexObjStateBean;

/**
 * This class acts as the source for the containers in an
 * in-memory middleware.
 *
 * @author Tom Spencer
 */
public class SingletonContainerStore {
    private static final SingletonContainerStore INSTANCE = new SingletonContainerStore();
    
    /** Holds each and every container */
    private Map<String, List<ObjexObjStateBean>> containers;
    
    /** Hidden Constructor */
    private SingletonContainerStore() {}
    
    /**
     * @return The single instance of this object
     */
    public static SingletonContainerStore getInstance() { 
        return INSTANCE;
    }
    
    /**
     * Call to get a list of objects for the given container.
     * The container ID cannot be null.
     * 
     * @param container The ID of the container
     * @return The list of objects (or null)
     */
    public List<ObjexObjStateBean> getObjects(String container) {
        if( container == null ) throw new IllegalArgumentException("Cannot get objects from the container store for a null container");
        
        return containers != null ? containers.get(container) : null;
    }
    
    /**
     * Call to set the objects against a given container for later
     * retrieval.
     * 
     * @param container The ID of the container (cannot be null)
     * @param objs The objects that constitute that container
     */
    public void setObjects(String container, List<ObjexObjStateBean> objs) {
        if( container == null ) throw new IllegalArgumentException("Cannot set objects in the container store for a null container");
        
        if( containers == null ) containers = new HashMap<String, List<ObjexObjStateBean>>();
        if( objs != null ) containers.put(container, objs);
        else containers.remove(container);
    }
}
