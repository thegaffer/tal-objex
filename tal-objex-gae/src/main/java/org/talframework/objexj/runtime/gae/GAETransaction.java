package org.talframework.objexj.runtime.gae;

import java.io.Serializable;

import org.talframework.objexj.container.TransactionCache;
import org.talframework.objexj.runtime.gae.object.ContainerBean;

/**
 * This class represents the transaction that is stored inside the
 * GAE MemCache service.
 * 
 * @author Tom Spencer
 */
public final class GAETransaction implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Holds the ID of the transaction */
    private String id;
    /** Holds the root container bean */
    private ContainerBean containerBean;
    /** Holds the actual cache of objects */
    private TransactionCache cache;
    /** Holds the last ID used to create object in the transaction (only used if container is new) */
    private long lastId = 1;
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the containerBean
     */
    public ContainerBean getContainerBean() {
        return containerBean;
    }
    /**
     * @param containerBean the containerBean to set
     */
    public void setContainerBean(ContainerBean containerBean) {
        this.containerBean = containerBean;
    }
    /**
     * @return the cache
     */
    public TransactionCache getCache() {
        return cache;
    }
    /**
     * @param cache the cache to set
     */
    public void setCache(TransactionCache cache) {
        this.cache = cache;
    }
    /**
     * @return the lastId
     */
    public long getLastId() {
        return lastId;
    }
    /**
     * @param lastId the lastId to set
     */
    public void setLastId(long lastId) {
        this.lastId = lastId;
    }
}
