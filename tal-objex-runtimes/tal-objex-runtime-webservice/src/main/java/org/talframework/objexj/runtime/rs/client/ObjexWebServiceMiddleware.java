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
package org.talframework.objexj.runtime.rs.client;

import java.util.Map;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerObjectCache;
import org.talframework.objexj.events.EventListener;

/**
 * This class implements a middleware that uses a WebService call
 * to get and save the objects.
 *
 * @author Tom Spencer
 */
public class ObjexWebServiceMiddleware implements ContainerMiddleware {
    
    @Override
    public ContainerObjectCache init(Container container) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getContainerId() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public boolean exists(ObjexID id, boolean accurate) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public ObjexObj loadObject(ObjexObj obj) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Map<ObjexID, ObjexObj> loadObjects(ObjexObj... objs) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public boolean isNew() {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public boolean isOpen() {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void open() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public ObjexID getNextObjectId(String type) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String save(ContainerObjectCache cache, String status, Map<String, String> header) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String suspend(ContainerObjectCache cache) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void clear(ContainerObjectCache cache) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void registerListener(EventListener listener) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void registerListenerForTransaction(EventListener listener) {
        // TODO Auto-generated method stub
        
    }
}
