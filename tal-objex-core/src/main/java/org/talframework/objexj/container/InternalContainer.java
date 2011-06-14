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
package org.talframework.objexj.container;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexObj;

/**
 * This interface represents the container internally.
 * This interface is not part of the core set of interfaces
 * and should not be used by a client of an object 
 * container. 
 *
 * @author Tom Spencer
 */
public interface InternalContainer extends Container {
    
    /**
     * Called to create a new object in this container and
     * return it. This is typically called from internal objects
     * when they are creating new children. 
     * 
     * @param parentObj The object that will serve as the parent
     * @param source The source object, which contains the type of object and any seed values to use
     */
    public ObjexObj createObject(ObjexObj parentObj, Object source);
    
    /**
     * Called to remove an object from the container.
     * 
     * TODO: Do we intend this to be called by parent, or from object itself?!?
     * 
     * @param obj The object to be removed
     */
    public void removeObject(ObjexObj obj);
}
