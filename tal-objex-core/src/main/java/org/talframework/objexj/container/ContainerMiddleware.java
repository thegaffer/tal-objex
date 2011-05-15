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
import org.talframework.objexj.ObjexObj;
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
	 * @return The object cache the container should use
	 */
	public ContainerObjectCache init(Container container);
	
	/**
	 * Call to get the containers ID. This is done because a
	 * middleware is often created based on a transaction ID.
	 * Note that this should be called once by the container
	 * and then cached - it might not be that efficient to
	 * keep getting this.
	 * 
	 * @return The containers ID
	 */
	public String getContainerId();
	
	/**
	 * This is called to determine if an object exists. The 
	 * container uses this before loading an object to see
	 * if the object first exists (and thus save the creation
	 * of the in-memory instance). However, unless accurate
	 * is true the middleware should aim to answer quickly
	 * rather than accurately - a wasted memory creation is
	 * nothing compared with remove call!!
	 * 
	 * @param id The ID of the object to test for
	 * @param accurate If true then the middleware should be accurate, otherwise only return false if definitely not existing
	 * @return True if the object exists, or is likely to exist, false otherwise.
	 */
	public boolean exists(ObjexID id, boolean accurate);
	
	/**
	 * Called to actually load an object from the persistent
	 * store.
	 * 
	 * @param obj The initialised object to load into
	 * @return The object is returned back only if it was found and loaded
	 */
	public ObjexObj loadObject(ObjexObj obj);
	
	/**
	 * Call to load one or more objects at the same time. Only
	 * those that were actually loaded are returned in the map 
	 * 
	 * @param objs The objects to load into
	 * @return The objects, keyed by ID, that were actually loaded
	 */
	public Map<ObjexID, ObjexObj> loadObjects(ObjexObj... objs);
	
	/**
     * @return True if the container is a new container (not yet persisted)
     */
    public boolean isNew();
    
    /**
     * @return True if the container is open for changes
     */
    public boolean isOpen();
    
    /**
     * Called to open the middleware. Not all middlewares will
     * support being opened after construction and may throw a
     * runtime exception. If successful it is expected that a
     * call to isOpen will be true.
     */
    public void open();
    
    /**
     * Called to get the next object ID. Middlewares should
     * typically favour wasting IDs rather than ensuring the
     * IDs are in anyway continuous. (Or if not the middleware
     * is responsible for aligning any temporary IDs when the
     * container is finally saved).
     * 
     * <p>This is only called if the container does not create
     * an ObjexID based on the objects seed state.</p>
     * 
     * @param type The type of object
     * @return The next objex ID
     */
    public ObjexID getNextObjectId(String type);
    
    /**
     * Call to suspend the transaction. This is not supported by
     * all environments. The idea is the cache of objects is 
     * stored away for later retreival using the ID returned.
     * 
     * @param cache The cache of objects inside transaction
     * @return The ID of the transaction
     */
    public String suspend(ContainerObjectCache cache);
    
    /**
     * Call to effectively abort the operation.
     * 
     * @param cache The cache of objects inside transaction
     */
    public void clear(ContainerObjectCache cache);
    
    /**
     * Call to save or commit the transaction.
     * 
     * @param cache The cache of objects inside transaction
     * @param status The current status of this container (post transaction)
     * @param header The header information for this container
     * @return The ID of the container
     */
    public String save(ContainerObjectCache cache, String status, Map<String, String> header);
    
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
