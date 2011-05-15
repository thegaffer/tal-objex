/*
 * Copyright 2010 Thomas Spencer
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
