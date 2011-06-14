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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * This class represents a transaction of a container that is in-flight,
 * that is it has been suspended.
 *
 * @author Tom Spencer
 */
public class GAETransaction implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The type of the container */ 
    private final String type;
    /** The container id - or null if this has not been saved */
    private final String id;
    /** The transaction id (this is the key we are stored in the cache with */
    private final String transactionId;
    /** The version of the container when this transaction was started */
    private final long version;
    
    /** Holds the new objects created in this transaction */
    private final Map<String, Map<String, Object>> newObjects;
    /** Holds the changed objects created in this transaction */
    private final Map<String, Map<String, Object>> changedObjects;
    /** Holds the removed objects created in this transaction */
    private final Map<String, Map<String, Object>> removedObjects;
    
    /**
     * Constructs a new transaction
     * 
     * @param type The type of container (must be provided)
     * @param id The ID of the container (if null then its a new container)
     * @param transactionId The id of the transaction (must be provided)
     * @param version The version of the container when first opened
     */
    public GAETransaction(String type, String id, String transactionId, long version) {
        this.type = type;
        this.id = id;
        this.transactionId = transactionId;
        this.version = version;
        
        newObjects = new HashMap<String, Map<String,Object>>();
        changedObjects = new HashMap<String, Map<String,Object>>();
        removedObjects = new HashMap<String, Map<String,Object>>();
    }
    
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the transactionId
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * @return the version
     */
    public long getVersion() {
        return version;
    }
    
    /**
     * Static method to retrieve a transaction from the cache
     * 
     * @param transId The transaction id
     * @return The transaction (or null)
     */
    public static GAETransaction retrieve(String transId) {
        MemcacheService service = MemcacheServiceFactory.getMemcacheService();
        Object trans = service.get(transId);
        return GAETransaction.class.cast(trans);
    }
    
    /**
     * Call to store the transaction on the cache
     */
    public void store() {
        MemcacheService service = MemcacheServiceFactory.getMemcacheService();
        service.put(transactionId, this);
    }
    
    /**
     * Call to clear this transaction from the cache
     */
    public void clear() {
        MemcacheService service = MemcacheServiceFactory.getMemcacheService();
        service.delete(transactionId);
    }

    /**
     * @return the newObjects
     */
    public Map<String, Map<String, Object>> getNewObjects() {
        return newObjects;
    }

    /**
     * @return the changedObjects
     */
    public Map<String, Map<String, Object>> getChangedObjects() {
        return changedObjects;
    }

    /**
     * @return the removedObjects
     */
    public Map<String, Map<String, Object>> getRemovedObjects() {
        return removedObjects;
    }
}
