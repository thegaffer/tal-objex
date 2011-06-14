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
package org.talframework.objexj.object.utils;


/**
 * This base class contains a number of helper methods that
 * the other field utility classes use.
 *
 * @author Tom Spencer
 */
public abstract class FieldUtils {

    /**
     * Internal helper to ensure the state bean is editable
     * and if not add the object to the container.
     */
    /*protected static void setEditable(BaseObjexObj obj, ObjexObjStateBean state) {
        if( !state.isEditable() ) {
            obj.getInternalContainer().addObjectToTransaction(obj, state);
            if( !state.isEditable() ) state.setEditable();
        }
    }
    
    *//**
     * Helper to remove a specific object from the container
     *//*
    protected static void removeObject(BaseObjexObj parentObj, Object id) {
        Container container = parentObj.getContainer();
        
        ObjexObj objToRemove = container.getObject(id);
        if( objToRemove != null && container instanceof InternalContainer ) {
            ((InternalContainer)container).removeObject(objToRemove);
        }
        else if( objToRemove != null ){
            throw new IllegalArgumentException("Cannot remove object, invalid container: " + objToRemove);
        }
    }
    
    *//**
     * Helper to copy all of the incoming objects properties into
     * current where they match on name.
     *//*
    protected static void copyObjects(Object current, Object incoming) {
        BeanDefinition currentDef = BeanDefinitionsSingleton.getInstance().getDefinition(current.getClass());
        BeanDefinition incomingDef = BeanDefinitionsSingleton.getInstance().getDefinition(incoming.getClass());
        
        for( String prop : incomingDef.getProperties() ) {
            if( !incomingDef.canRead(prop) || !currentDef.hasProperty(prop) || !currentDef.canWrite(prop) ) continue;
            currentDef.write(current, prop, incomingDef.read(incoming, prop));
        }
    }*/
}
