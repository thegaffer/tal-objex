/*
 * Copyright 2009 Thomas Spencer
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
