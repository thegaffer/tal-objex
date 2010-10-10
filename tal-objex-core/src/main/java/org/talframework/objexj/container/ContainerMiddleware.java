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

package org.talframework.objexj.container;

import java.util.Map;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.events.EventListener;

/**
 * This interface represents the connection between a
 * container and its runtime environment. By abstracting
 * certain container elements into this interface we can
 * leverage the same container code in a number of
 * runtime environments. This interfaces responsibility
 * is to load objects and to control what an ObjexID is
 * and how to convert between it an other objects.
 * 
 * TODO: Method to load a bunch of objects!!
 * 
 * @author Tom Spencer
 */
public interface ContainerMiddleware {
	
	/**
	 * Call to initialise the middleware against the container.
	 * Each middleware instance is matched to a container
	 * instance.
	 * 
	 * @param container The container
	 */
	public void init(Container container);
	
	/**
	 * Call to get the containers ID. This is done because a
	 * middleware is often created based on a transaction ID.
	 * 
	 * @return The containers ID
	 */
	public String getContainerId();
	
	/**
	 * Called to actually load an object from the persistent
	 * store.
	 * 
	 * @param type The type of the object
	 * @param id The ID of the object
	 * @return The object
	 */
	public ObjexObjStateBean loadObject(Class<? extends ObjexObjStateBean> type, ObjexID id);
	
	// TODO: Load objects
	
	/**
     * @return True if the container is a new container (not yet persisted)
     */
    public boolean isNew();
    
    /**
     * Called to open the middleware. Not all middlewares will
     * support being opened after construction.
     * 
     * @return The cache for the transaction if successfully open
     */
    public TransactionCache open();
    
    /**
     * @return The default strategy for the middleware
     */
    public ObjexIDStrategy getIdStrategy();
    
    /**
     * Call to get an existing {@link TransactionCache} for the
     * transaction.
     * 
     * @return The transaction cache, or null
     */
    public TransactionCache getCache();
    
    /**
     * Call to suspend the transaction. This is not supported by
     * all environments. The idea is the cache of objects is 
     * stored away for later retreival using the ID returned.
     * 
     * @return The ID of the transaction
     */
    public String suspend();
    
    /**
     * Call to effectively abort the operation.
     * 
     * @param cache The cache of objects inside transaction
     */
    public void clear();
    
    /**
     * Call to save or commit the transaction.
     * 
     * @param status The current status of this container (post transaction)
     * @param header The header information for this container
     * @return The ID of the container
     */
    public String save(String status, Map<String, String> header);
    
    /**
     * Causes the middleware to register an event listener
     * against the container. This listener will be persisted
     * and used from now on.
     * 
     * FUTURE: Should there be an unregister listener??
     * 
     * @param listener The listern to register
     */
    public void registerListener(EventListener listener);
    
    /**
     * Causes the middleware to register an event listener
     * against the current transaction only. The listener
     * will be invoked when the container is saved, but
     * will not be registered so it is invoked in the
     * future.
     * 
     * @param listener The listern to register
     */
    public void registerListenerForTransaction(EventListener listener);
}
