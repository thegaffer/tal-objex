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
