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
package org.talframework.objexj.container.middleware;

import java.util.HashMap;
import java.util.Map;

import org.talframework.objexj.ObjexID;

/**
 * This acts as a cache for the in-memory middleware to use
 * and suspend. Note, there is no management of this so if
 * containers are left open then the cache reference in here
 * will remain.
 * 
 * @author Tom Spencer
 */
public final class SingletonContainerCache {
    private static final SingletonContainerCache INSTANCE = new SingletonContainerCache();
    
    private Map<String, Map<ObjexID, Map<String, Object>>> cache;
    
    /** Hidden constructor */
    private SingletonContainerCache() {}
    
    /**
     * @return The single container cache instance
     */
    public static SingletonContainerCache getInstance() {
        return INSTANCE;
    }
    
    /**
     * Call to get a transaction from the cache.
     * 
     * @param transactionId The ID of the transaction
     * @return The cache
     */
    public Map<ObjexID, Map<String, Object>> getCache(String transactionId) {
        if( transactionId == null ) throw new IllegalArgumentException("Transaction ID must not be null when using the in-memory container cache");
        
        return cache != null ? cache.get(transactionId) : null;
    }
    
    /**
     * Call to add the transaction to the cache.
     * 
     * @param transactionId The ID of the cache
     * @param cache The cache itself
     */
    public void setCache(String transactionId, Map<ObjexID, Map<String, Object>> cache) {
        if( transactionId == null ) throw new IllegalArgumentException("Transaction ID must not be null when using the in-memory container cache");
        
        if( this.cache == null ) this.cache = new HashMap<String, Map<ObjexID,Map<String,Object>>>();
        this.cache.put(transactionId, cache);
    }
}
