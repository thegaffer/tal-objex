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
package org.talframework.objexj.runtime.gae;

import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.ObjexIDStrategy;
import org.talframework.objexj.runtime.gae.object.ContainerBean;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;

/**
 * This ID Strategy creates an ObjexID by allocating it
 * using the GAE Data Service.
 * 
 * FUTURE: New implementation that obtains blocks in a transaction
 * 
 * @author Tom Spencer
 */
public final class GAEAllocateObjexIDStrategy implements ObjexIDStrategy {
    private static final GAEAllocateObjexIDStrategy INSTANCE = new GAEAllocateObjexIDStrategy();
    
    /**
     * Hidden constructor, there need only be 1 {@link GAEAllocateObjexIDStrategy} 
     * instance in the classloader.
     */
    private GAEAllocateObjexIDStrategy() {   
    }
    
    /**
     * @return The single {@link GAEAllocateObjexIDStrategy} instance in classloader
     */
    public static GAEAllocateObjexIDStrategy getInstance() {
        return INSTANCE;
    }

    /**
     * Gets and allocates ID from range. 
     * 
     * TODO: Should we just pass through a hardcoded type here?
     */
    public ObjexID createId(Container container, Class<? extends ObjexObjStateBean> stateType, String type, ObjexObj obj) {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        
        Key root = KeyFactory.createKey(ContainerBean.class.getSimpleName(), container.getId());
        
        // KeyRange range = service.allocateIds(root, stateType.getSimpleName(), 1);
        KeyRange range = service.allocateIds(root, container.getType(), 1);
        return new DefaultObjexID(type, range.getStart().getId());
    }
    
    public Key createContainerId(String id) {
        if( id != null ) {
            return KeyFactory.createKey(ContainerBean.class.getSimpleName(), id);
        }
        else {
            DatastoreService service = DatastoreServiceFactory.getDatastoreService();
            KeyRange range = service.allocateIds(ContainerBean.class.getSimpleName(), 1);
            return range.getStart();
        }
    }
}
