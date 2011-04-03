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

package org.talframework.objexj;

import java.util.List;
import java.util.Map;


/**
 * This interface represents wrapper (or container) or related
 * objects. The container might be a store, which is akin to a
 * rolodex or similar, or a document which is akin to a folder
 * in a filing cabinet. Inside the folder or the rolodex are 
 * pieces of paper - these are the objects.
 * 
 * @author Tom Spencer
 */
public interface Container {

	/**
	 * @return The ID of the container
	 */
	public String getId();
	
	/**
	 * @return The type of the container
	 */
	public String getType();
	
	/**
	 * @return The root object for the container
	 */
	public ObjexObj getRootObject();
	
	/**
	 * Get an object given its ID
	 * 
	 * @param id The ID of the object (may be an ObjexID, may be a string)
	 * @param expected The expected type of that object
	 * @return The object (or null if not found)
	 * @throws IllegalArgumentException If a null object id is supplied
	 */
	public <T> T getObject(Object id, Class<T> expected);
	
	/**
	 * Call to get an object given its ID
	 * 
	 * @param id The ID of the object (may be an ObjexID, may be a string)
	 * @return The object (or null if not found)
	 * @throws IllegalArgumentException If a null object id is supplied
	 */
	public ObjexObj getObject(Object id);
	
	/**
	 * Call to get a list of objects from a given list of
	 * references. The references may be {@link ObjexID} 
	 * instances or simply strings that are the stringified
	 * form of object references.
	 *  
	 * @param ids The IDs in to get
	 * @return A collection of the valid objects found
	 */
	public List<ObjexObj> getObjectList(List<? extends Object> ids);
	
	/**
	 * Templates version of getObjects that allows the
	 * type of element in the list to be specified. This
	 * method will query each object for this behaviour.
	 * 
	 * @param ids The IDs
	 * @param expectedElement The expected type of element.
	 * @return The list of objects
	 */
	public <T> List<T> getObjectList(List<? extends Object> ids, Class<T> expectedElement);
	
	/**
	 * Call to get a map of objects from a given map of
     * references. The references may be {@link ObjexID} 
     * instances or simply strings that are the stringified
     * form of object references. The keys are unchanged
     * in the resulting map.
	 * 
	 * @param ids The IDs in to get
	 * @return A map containing the references to get
	 */
	public Map<String, ObjexObj> getObjectMap(Map<String, ? extends Object> ids);
	
	/**
	 * As standard getObjectMap, but ensures each element
	 * returned (value in the map) is of the given type
	 * be called getBehaviour on it.
	 * 
	 * @param ids The map of keys to Object IDs
	 * @param expectedElement The expected behaviour for each element
	 * @return The map containing the objects
	 */
	public <T> Map<String, T> getObjectMap(Map<String, ? extends Object> ids, Class<T> expectedElement);
	
	/**
	 * Call to execute a named query against the container.
	 * 
	 * <p>Queries against a container are named and cannot just
	 * be constructed by the client. This is a) to ensure there
	 * is control for performance (i.e. stop silly queries by
	 * making it a design task) and b) because queries are 
	 * often Database specific (which is hidden by Objex).</p> 
	 * 
	 * @param request The request to perform
	 * @return The list of objects found
	 */
	public QueryResult executeQuery(QueryRequest request);
	
	/**
     * Call to process an event from the same or another
     * container. The container is not opened before processing
     * an event because a) the event process may be read-only
     * (such as a Systems Integration task or sending a 
     * notification) and b) the event may be discarded. If
     * writable access is needed to the container then you
     * should open it from within the event processor.
     * 
     * @param event The event
     */
    public void processEvent(Event event);
	
	/**
	 * Call to open this container for editing. The returned 
	 * instance may be of a different object that you should 
	 * now interact with instead of this instance.
	 * 
	 * @return An editable container to start working with 
	 */
	public Container openContainer();
	
	/**
     * Call to determine if the editable container represents a
     * new container that has not yet been persisted. If so the
     * next object will be the root object.
     * 
     * @return Determines if the container if new (and not yet persisted)
     */
    public boolean isNew();
    
    /**
     * Call to determine if the editable container can still
     * accept changes.
     * 
     * @return True if it is open, false otherwise
     */
    public boolean isOpen();
    
    /**
     * Call to validate the container prior to a save
     * 
     * @return The validation request with the errors (if any)
     */
    public ValidationRequest validate();
    
    /**
     * Call to save the container. Once this is done the
     * no further changes can be made via this editable
     * container.
     * 
     * @return The ID of the container
     */
    public String saveContainer();
    
    /**
     * Call to close the container without saving any
     * changes. No further changes can be made via this
     * editable container. 
     */
    public void closeContainer();
    
    /**
     * Call to suspend the transaction and come back to
     * it later. This is not supported in all runtime
     * environments.
     * 
     * @return The ID of the transaction 
     */
    public String suspend();
}
