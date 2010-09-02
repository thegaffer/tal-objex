package org.talframework.objexj.container.middleware;

import java.util.HashMap;
import java.util.Map;

import org.talframework.objexj.container.TransactionCache;

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
    
    private Map<String, TransactionCache> cache;
    
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
    public TransactionCache getCache(String transactionId) {
        if( transactionId == null ) throw new IllegalArgumentException("Transaction ID must not be null when using the in-memory container cache");
        
        return cache != null ? cache.get(transactionId) : null;
    }
    
    /**
     * Call to add the transaction to the cache.
     * 
     * @param transactionId The ID of the cache
     * @param cache The cache itself
     */
    public void setCache(String transactionId, TransactionCache cache) {
        if( transactionId == null ) throw new IllegalArgumentException("Transaction ID must not be null when using the in-memory container cache");
        
        if( this.cache == null ) this.cache = new HashMap<String, TransactionCache>();
        this.cache.put(transactionId, cache);
    }
}
