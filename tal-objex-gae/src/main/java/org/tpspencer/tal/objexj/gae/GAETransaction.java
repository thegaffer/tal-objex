package org.tpspencer.tal.objexj.gae;

import java.io.Serializable;

import org.tpspencer.tal.objexj.container.TransactionCache;
import org.tpspencer.tal.objexj.gae.object.ContainerBean;

/**
 * This class represents the transaction
 * 
 * @author Tom Spencer
 */
public class GAETransaction implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Holds the ID of the transaction */
    public String id;
    /** Holds the root container bean */
    public ContainerBean containerBean;
    /** Holds the traditional cache */
    public TransactionCache cache;
    
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
}
