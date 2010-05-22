package org.tpspencer.tal.objexj.container;

import java.util.HashMap;
import java.util.Map;

import org.tpspencer.tal.objexj.object.ObjectStrategy;

public class SimpleContainerStrategy implements ContainerStrategy {
	
	private final String name;
	private final String rootObject;
	private final Map<String, ObjectStrategy> objectStrategies;
	
	public SimpleContainerStrategy(String name, String rootObject, ObjectStrategy[] strategies) {
		this.name = name;
		this.rootObject = rootObject;
		this.objectStrategies = new HashMap<String, ObjectStrategy>();
		
		for( int i = 0 ; i < strategies.length ; i++ ) {
			this.objectStrategies.put(strategies[i].getTypeName(), strategies[i]);
		}
	}

	public String getContainerName() {
		return name;
	}
	
	public String getRootObjectName() {
	    return rootObject;
	}
	
	public ObjectStrategy getObjectStrategy(String name) {
	    return objectStrategies != null ? objectStrategies.get(name) : null;
	}
	
	public Map<String, ObjectStrategy> getObjectStrategies() {
		return objectStrategies;
	}
}
