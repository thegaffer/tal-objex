package org.tpspencer.tal.objexj.container;

import java.util.HashMap;
import java.util.Map;

import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.exceptions.ObjectTypeNotFoundException;
import org.tpspencer.tal.objexj.object.DefaultObjexID;
import org.tpspencer.tal.objexj.object.ObjectStrategy;

public final class SimpleContainerStrategy implements ContainerStrategy {
	
	private final String name;
	private final String id;
	private final String rootObject;
	private final Map<String, ObjectStrategy> objectStrategies;
	private final Map<String, ObjectStrategy> objectStrategiesByState;
	
	/**
	 * Construct a container strategy for a document container.
	 * 
	 * @param name The name (or type) of the container
	 * @param rootObject The root object
	 * @param strategies The strategies of the objects inside this container
	 */
	public SimpleContainerStrategy(String name, String rootObject, ObjectStrategy... strategies) {
		this.name = name;
		this.id = null;
		this.rootObject = rootObject;
		this.objectStrategies = new HashMap<String, ObjectStrategy>();
		this.objectStrategiesByState = new HashMap<String, ObjectStrategy>();
		
		for( int i = 0 ; i < strategies.length ; i++ ) {
			this.objectStrategies.put(strategies[i].getTypeName(), strategies[i]);
			this.objectStrategiesByState.put(strategies[i].getStateClass().getSimpleName(), strategies[i]);
		}
	}
	
	/**
	 * Construct a container strategy for a store container
	 * 
	 * @param name The name (or type) of the container
	 * @param id The (fixed) ID of the store
     * @param rootObject The root object
     * @param strategies The strategies of the objects inside this container
	 */
	public SimpleContainerStrategy(String name, String id, String rootObject, ObjectStrategy... strategies) {
        this.name = name;
        this.id = id;
        this.rootObject = rootObject;
        this.objectStrategies = new HashMap<String, ObjectStrategy>();
        this.objectStrategiesByState = new HashMap<String, ObjectStrategy>();
        
        for( int i = 0 ; i < strategies.length ; i++ ) {
            this.objectStrategies.put(strategies[i].getTypeName(), strategies[i]);
            this.objectStrategiesByState.put(strategies[i].getStateClass().getSimpleName(), strategies[i]);
        }
    }

	public String getContainerName() {
		return name;
	}
	
	public String getContainerId() {
	    return id;
	}
	
	public String getRootObjectName() {
	    return rootObject;
	}
	
	public ObjexID getRootObjectID() {
	    return new DefaultObjexID(rootObject, 1);
	}
	
	public ObjectStrategy getObjectStrategy(String type) {
	    ObjectStrategy ret = objectStrategies.get(type);
	    if( ret == null ) throw new ObjectTypeNotFoundException(type);
	    return ret;
	}
	
	public ObjectStrategy getObjectStrategyForState(String stateType) {
	    ObjectStrategy ret = objectStrategiesByState.get(stateType);
        if( ret == null ) throw new ObjectTypeNotFoundException(stateType);
        return ret;
	}
}
