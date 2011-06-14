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
package org.talframework.objexj.object;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.talframework.objexj.container.ObjectStrategy;

/**
 * Although ObjexObj objects should be behaviourally rich
 * and fully protect the domain, there are going to be
 * simple objects and simple containers. SimpleObjexObj
 * has been created to allow the creation of an ObjexObj
 * that the rest of the Objex plumbing will accept, but
 * which needs no further coding.
 * 
 * <p>To use a SimpleObjexObj it requires two things.
 * Firstly a {@link ObjexObjStateBean} derived class that
 * holds the state and represents the persisted form. And
 * secondly an extended object strategy that describes
 * the reference properties (properties that are 
 * references to other objects) so we can deal with
 * those appropriately. If this class is used there are 
 * certain features you need to be aware of:</p>
 * 
 * <ul>
 * <li>Firstly the class will not expose natural getter/
 * setter methods for the properties, so will not be
 * useful where reflection is used</li> 
 * <li>Secondly the class make use of reflection over
 * the state bean. Although reasonably efficient in
 * terms of caching the introspection calls this will
 * still not be the same as native access.
 * </ul>
 * 
 * @author Tom Spencer
 */
@XmlRootElement
public final class SimpleObjexObj extends BaseObjexObj {

	/** The strategy for this object */
    private final ObjectStrategy strategy;
	/** Member holds the state object */
	private Object state;
	
	public SimpleObjexObj() {
	    throw new IllegalArgumentException("Cannot create a SimpleObjexObj directly");
	}
	
	public SimpleObjexObj(ObjectStrategy strategy, Object state) {
	    if( strategy == null ) throw new IllegalArgumentException("Cannot create object without a strategy");
		if( state == null ) throw new IllegalArgumentException("Cannot create object without a state object");
		
		this.strategy = strategy;
		this.state = state;
	}
	
	/**
	 * Default is simple name of outer class
	 */
	@Override
	public String getType() {
		return strategy.getTypeName();
	}
	
	@XmlAnyElement
	@Override
	@Valid
	protected Object getStateBean() {
	    return state;
	}
	
	@Override
    protected ObjectStrategy getStrategy() {
        return strategy;
    }
}
