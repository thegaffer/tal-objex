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
package org.talframework.objexj.runtime.rs.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.TransactionCache;
import org.talframework.objexj.locator.BaseInterceptingMiddleware;

public class WebServiceInterceptor extends BaseInterceptingMiddleware {
    
    private Map<ObjexID, ObjexObjStateBean> objects = new HashMap<ObjexID, ObjexObjStateBean>();
    
    /**
     * Call to get the state bean for an individual object
     * 
     * @param obj The reference, which may be to an object or its ID
     * @return The object requested or null
     */
    public ObjexObjStateBean findObject(Object obj) {
        ObjexID id = null;
        if( obj instanceof ObjexID ) id = (ObjexID)obj;
        else if( obj instanceof ObjexObj ) id = ((ObjexObj)obj).getId();
        else if( obj instanceof String ) id = DefaultObjexID.getId(obj);
        
        return id != null ? findObject(id) : null;
    }
    
    /**
     * Call to get the state bean for an individual object
     * 
     * @param id The ID of the object
     * @return The object requested or null
     */
    public ObjexObjStateBean findObject(ObjexID id) {
        ObjexObjStateBean ret = null;
        
        TransactionCache cache = super.getCache();
        if( cache != null ) ret = cache.findObject(id, null);
        if( ret == null ) ret = objects.get(id);
        
        return ret;
    }
    
    /**
     * Call to get state beans related to given input which
     * might be an array, collection or map of IDs or real
     * Objects - or a singluar ID or Obj.
     * 
     * @param obj The array, collection or map
     * @return The relevant state beans
     */
    public List<ObjexObjStateBean> findObjects(Object obj) {
        List<ObjexObjStateBean> ret = new ArrayList<ObjexObjStateBean>();
        
        if( obj.getClass().isArray() ) {
            int size = Array.getLength(obj);
            for( int i = 0 ; i < size ; i++ ) {
                ObjexObjStateBean bean = findObject(Array.get(obj, i));
                if( bean != null ) ret.add(bean);
            }
        }
        else if( Collection.class.isInstance(obj) ) {
            Collection<?> coll = (Collection<?>)obj;
            for( Object o : coll ) {
                ObjexObjStateBean bean = findObject(o);
                if( bean != null ) ret.add(bean);
            }
        }
        else if( Map.class.isInstance(obj) ) {
            Map<?, ?> map = (Map<?, ?>)obj;
            for( Object key : map.keySet() ) {
                Object o = map.get(key);
                ObjexObjStateBean bean = findObject(o);
                if( bean != null ) ret.add(bean);
            }
        }
        else if( obj != null ){
            ObjexObjStateBean bean = findObject(obj);
            if( bean != null ) ret.add(bean);
        }
        
        return ret;
    }
    
    /**
     * Call to get the state beans for the given objects.
     * 
     * @param ids The ID of the objects to get
     * @return The map of state beans
     */
    public List<ObjexObjStateBean> findObjects(Set<ObjexID> ids) {
        List<ObjexObjStateBean> ret = new ArrayList<ObjexObjStateBean>();
        for( ObjexID id : ids ) {
            ObjexObjStateBean state = findObject(id);
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
