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
package org.talframework.objexj.locator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.talframework.objexj.Container;
import org.talframework.objexj.exceptions.ContainerTypeNotFoundException;

/**
 * This singleton class can find containers where the type of
 * container is not known. The ID of the container will contain
 * the short 'type' of container and this is looked up against
 * the map of ContainerFactories it holds. Although this is a
 * singleton the map of factories can be configured via Spring
 * or other DI tool.
 * 
 * <p>Note, I did consider putting an interface on this class
 * and then allowing the instance to be injected in to support
 * better testing, but in the end I decided that is a bit
 * overkill right now. This does compromise unit testing
 * slightly, but I decided this was ok for now. This decision
 * can be revisited at a later stage.</p>
 * 
 * @author Tom Spencer
 */
public final class SingletonContainerLocator {
    private static final SingletonContainerLocator INSTANCE = new SingletonContainerLocator();
    
	/** The map of factories to use, keyed by type */
	private final Map<String, ContainerFactory> factories;
	
	/**
	 * Hidden constructor
	 */
	private SingletonContainerLocator() {
	    this.factories = new HashMap<String, ContainerFactory>();
	}
	
	/**
	 * @return The single instance of the container locator
	 */
	public static SingletonContainerLocator getInstance() {
	    return INSTANCE;
	}
	
	/**
	 * Simply finds the type of the ID and attempts to
	 * locate the {@link ContainerFactory} for that type
	 * and then use it to get the container.
	 */
	public Container get(String id) {
		return getFactory(getType(id)).get(id);
	}

	/**
     * Simply finds the type of the ID and attempts to
     * locate the {@link ContainerFactory} for that type
     * and then use it to open the container.
     */
    public Container open(String id) {
	    return getFactory(getType(id)).open(id);
	}
    
    /**
     * Call to create all stores we know about if (and
     * only if) they do not exist already.
     */
    public void createStores() {
        Iterator<ContainerFactory> it = factories.values().iterator();
        while( it.hasNext() ) {
            it.next().createStore();
        }
    }
	
	/**
	 * Helper to extract the type of container from the ID.
	 * This will only return a value if the type is also a
	 * valid container type for which we have a factory for.
	 * 
	 * @param id The ID of the container
	 * @return Its valid type or null
	 */
	private String getType(String id) {
		int index = id.indexOf('/');
		if( index < 0 ) index = id.indexOf('\\');
		
		String type = id; // Container ID is just its name
		if( index > 0 ) type = id.substring(0, index);
		
		// Ensure type is valid even if given
		if( type == null || factories == null || !factories.containsKey(type) )
			type = null;
		
		return type;
	}
	
	private ContainerFactory getFactory(String type) {
	    ContainerFactory ret = null;
	    if( factories != null ) ret = factories.get(type);
	    if( ret == null ) throw new ContainerTypeNotFoundException(type);
	    return ret;
	}

    /**
     * @return the factories
     */
    public Map<String, ContainerFactory> getFactories() {
        return factories;
    }
	
	public void setFactories(Map<String, ContainerFactory> factories) {
	    this.factories.clear();
	    
	    if( factories == null || factories.size() == 0 ) return;
	    
	    Iterator<String> it = factories.keySet().iterator();
	    while( it.hasNext() ) {
	        String name = it.next();
	        this.factories.put(name, factories.get(name));
	    }
	}
}
