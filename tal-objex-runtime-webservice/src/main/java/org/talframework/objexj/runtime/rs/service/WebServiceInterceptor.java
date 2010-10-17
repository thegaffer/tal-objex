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

package org.talframework.objexj.runtime.rs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.TransactionCache;
import org.talframework.objexj.locator.BaseInterceptingMiddleware;

public class WebServiceInterceptor extends BaseInterceptingMiddleware {
    
    private Map<ObjexID, ObjexObjStateBean> objects = new HashMap<ObjexID, ObjexObjStateBean>();
    
    /**
     * Call to get the state beans for the given objects.
     * 
     * @param ids The ID of the objects to get
     * @return The map of state beans
     */
    public List<ObjexObjStateBean> findObjects(Set<ObjexID> ids) {
        TransactionCache cache = super.getCache();
        
        List<ObjexObjStateBean> ret = new ArrayList<ObjexObjStateBean>();
        for( ObjexID id : ids ) {
            ObjexObjStateBean state = null;
            
            if( cache != null ) state = cache.findObject(id, null);
            if( state == null ) state = objects.get(id);
            
            if( state == null ) throw new IllegalArgumentException("Obtained object has not been loaded via middleware or is in cache, you are probably using a custom container that is not compatible with the Restful WebService: " + id);
            ret.add(state);
        }
        
        return ret;
    }
    
    /**
     * Overridden to get the state beans
     */
    @Override
    public ObjexObjStateBean loadObject(Class<? extends ObjexObjStateBean> type, ObjexID id) {
        ObjexObjStateBean ret = super.loadObject(type, id);
        
        // Save object for use
        if( ret != null ) objects.put(id, ret);
        
        return ret;
    }
}
