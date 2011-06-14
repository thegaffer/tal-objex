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

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObj.ObjexFieldType;
import org.talframework.objexj.object.writer.BaseObjectWriter;

import com.google.appengine.api.datastore.Entity;

/**
 * This class writes a single object out into an entity that can
 * be added or updated in the datastore.
 *
 * @author Tom Spencer
 */
public class GAEObjectWriter extends BaseObjectWriter {
    
    /** Transient internal member holding the current entity */
    private Entity currentEntity;
    
    public GAEObjectWriter() {
        super(ObjectWriterBehaviour.INCLUDE_OWNED, ObjectWriterBehaviour.INCLUDE_REFERENCES);
    }
    
    /**
     * Call to write a single object into a GAE entity
     * 
     * @param entity The entity to read from
     * @param obj The object to read into
     */
    public void writeObject(Entity entity, ObjexObj obj) {
        currentEntity = entity;
        super.write(obj);
        currentEntity = null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean shouldWrite(String name, Object val) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeProperty(String name, Object val, ObjexFieldType type, boolean persistent) {
        if( !persistent ) return;
        
        switch(type) {
        case MEMO:
        case BLOB:
        case SHORT_BLOB:
            currentEntity.setUnindexedProperty(name, val);
            
        default:
            currentEntity.setProperty(name, val);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeListProperty(String name, List<String> refs, ObjexFieldType type, boolean persistent) {
        writeProperty(name, refs, type, persistent);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeSetProperty(String name, Set<String> refs, ObjexFieldType type, boolean persistent) {
        writeProperty(name, refs, type, persistent);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeMapProperty(String name, Map<String, String> refs, ObjexFieldType type, boolean persistent) {
        writeProperty(name, refs, type, persistent);
    }
    
}
