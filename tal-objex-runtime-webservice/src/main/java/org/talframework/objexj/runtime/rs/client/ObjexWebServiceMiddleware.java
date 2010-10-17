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
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ObjexIDStrategy;
import org.talframework.objexj.container.TransactionCache;
import org.talframework.objexj.events.EventListener;

/**
 * This class implements a middleware that uses a WebService call
 * to get and save the objects.
 *
 * @author Tom Spencer
 */
public class ObjexWebServiceMiddleware implements ContainerMiddleware {

    public void init(Container container) {
        // TODO Auto-generated method stub
        
    }
    
    public String getContainerId() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public boolean isNew() {
        // TODO Auto-generated method stub
        return false;
    }
    
    public ObjexObjStateBean loadObject(Class<? extends ObjexObjStateBean> type, ObjexID id) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public TransactionCache open() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public TransactionCache getCache() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public ObjexIDStrategy getIdStrategy() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public String save(String status, Map<String, String> header) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public String suspend() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void clear() {
        // TODO Auto-generated method stub
        
    }
    
    public void registerListener(EventListener listener) {
        // TODO Auto-generated method stub
        
    }
    
    public void registerListenerForTransaction(EventListener listener) {
        // TODO Auto-generated method stub
        
    }
}
