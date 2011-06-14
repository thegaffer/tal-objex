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
package org.talframework.objexj.runtimes.gae;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.object.writer.BaseObjectReader;

import com.google.appengine.api.datastore.Entity;

/**
 * This class reads an object in from an entity that exists 
 * in the datastore.
 *
 * @author Tom Spencer
 */
public final class GAEObjectReader extends BaseObjectReader {
    
    /** Transient internal member holding the current entity */
    private Entity currentEntity;
    
    public GAEObjectReader() {
        super(ObjectReaderBehaviour.INCLUDE_OWNED, ObjectReaderBehaviour.INCLUDE_REFERENCES);
    }
    
    /**
     * Call to read in a single object when the entity has already
     * been found. This can be used after running queries, for 
     * instance.
     * 
     * @param entity The entity to read from
     * @param obj The object to read into
     */
    public void readObject(Entity entity, ObjexObj obj) {
        currentEntity = entity;
        super.read(obj);
        currentEntity = null;
    }
    
    /**
     * {@inheritDoc}
     * 
     * We check whether the entity has this property
     */
    @Override
    protected boolean propertyExists(String name, Class<?> expected) {
        return currentEntity.hasProperty(name);
    }
    
    /**
     * {@inheritDoc}
     * 
     * We have no concept in GAE of 'isSet' we just return true here.
     */
    @Override
    protected boolean shouldSet(String name) {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * Simply reads from the current entity
     */
    @Override
    protected Object getProperty(String name, Class<?> expected, org.talframework.objexj.ObjexObj.ObjexFieldType type, boolean persistent) {
        return currentEntity.getProperty(name);
    }
    
    /**
     * {@inheritDoc}
     * 
     * Simply reads from the current entity
     */
    @Override
    protected Object getListProperty(String name, Class<?> expected, org.talframework.objexj.ObjexObj.ObjexFieldType type, boolean persistent) {
        return currentEntity.getProperty(name);
    }
    
    /**
     * {@inheritDoc}
     * 
     * Simply reads from the current entity
     */
    @Override
    protected Object getSetProperty(String name, Class<?> expected, org.talframework.objexj.ObjexObj.ObjexFieldType type, boolean persistent) {
        return currentEntity.getProperty(name);
    }
    
    /**
     * {@inheritDoc}
     * 
     * Simply reads from the current entity
     */
    @Override
    protected Object getMapProperty(String name, Class<?> expected, org.talframework.objexj.ObjexObj.ObjexFieldType type, boolean persistent) {
        return currentEntity.getProperty(name);
    }
}
