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

package org.talframework.objexj.container.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.events.EventHandler;
import org.talframework.objexj.events.EventListener;
import org.talframework.objexj.exceptions.EventHandlerNotFoundException;
import org.talframework.objexj.exceptions.ObjectTypeNotFoundException;
import org.talframework.objexj.exceptions.QueryNotFoundException;
import org.talframework.objexj.query.Query;

public final class SimpleContainerStrategy implements ContainerStrategy {
	
	private final String name;
	private final String id;
	private final String rootObject;
	private final Map<String, ObjectStrategy> objectStrategies;
	private final Map<Class<?>, ObjectStrategy> classStrategies;
	private Map<String, Query> namedQueries;
	private Map<String, EventHandler> eventHandlers;
	private List<EventListener> standardListeners;
	
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
		this.classStrategies = new HashMap<Class<?>, ObjectStrategy>();
		
		for( int i = 0 ; i < strategies.length ; i++ ) {
			this.objectStrategies.put(strategies[i].getTypeName(), strategies[i]);
			this.classStrategies.put(strategies[i].getMainClass(), strategies[i]);
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
        this.classStrategies = new HashMap<Class<?>, ObjectStrategy>();
        
        for( int i = 0 ; i < strategies.length ; i++ ) {
            this.objectStrategies.put(strategies[i].getTypeName(), strategies[i]);
            this.classStrategies.put(strategies[i].getMainClass(), strategies[i]);
        }
    }
	
	/**
     * @return the namedQueries
     */
	public Map<String, Query> getNamedQueries() {
        return namedQueries;
    }

    /**
     * @param namedQueries the namedQueries to set
     */
    public void setNamedQueries(Map<String, Query> namedQueries) {
        this.namedQueries = namedQueries;
    }

    /**
     * @return the eventHandlers
     */
    public Map<String, EventHandler> getEventHandlers() {
        return eventHandlers;
    }

    /**
     * @param eventHandlers the eventHandlers to set
     */
    public void setEventHandlers(Map<String, EventHandler> eventHandlers) {
        this.eventHandlers = eventHandlers;
    }
    
    /**
     * @return the standardListeners
     */
    @Override
    public List<EventListener> getStandardListeners() {
        return standardListeners;
    }

    /**
     * @param standardListeners the standardListeners to set
     */
    public void setStandardListeners(List<EventListener> standardListeners) {
        this.standardListeners = standardListeners;
    }

    @Override
    public String getContainerName() {
		return name;
	}
	
    @Override
    public String getContainerId() {
	    return id;
	}
	
    @Override
    public String getRootObjectName() {
	    return rootObject;
	}
	
    @Override
    public ObjexID getRootObjectID() {
	    return new DefaultObjexID(rootObject, 1);
	}
	
	@Override
    public ObjectStrategy getObjectStrategy(String type) {
	    ObjectStrategy ret = objectStrategies.get(type);
	    if( ret == null ) throw new ObjectTypeNotFoundException(type);
	    return ret;
	}
	
	@Override
	public ObjectStrategy getObjectStrategyForObject(Object object) {
	    ObjectStrategy ret = classStrategies.get(object.getClass());
        return ret;
	}
	
	@Override
    public Query getQuery(String name) {
	    if( namedQueries != null && namedQueries.containsKey(name) ) return namedQueries.get(name);
	    else throw new QueryNotFoundException(name);
	}
	
	@Override
    public EventHandler getEventHandler(String name) {
	    if( eventHandlers != null && eventHandlers.containsKey(name) ) return eventHandlers.get(name);
	    else throw new EventHandlerNotFoundException(name);
	}
}
