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

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.TransactionCache.ObjectRole;

/**
 * This interface represents the container internally.
 * This interface is not part of the core set of interfaces
 * and should not be used by a client of an object 
 * container. 
 *
 * @author Tom Spencer
 */
public interface InternalContainer extends Container {
    
    /**
     * Gets the role of the object in the transaction if
     * it has one.
     * 
     * @param id The ID of the object to check
     * @return The {@link ObjectRole}
     */
    public ObjectRole getObjectRole(ObjexID id);
    
    /**
     * Called to add an object to the container 
     * 
     * @param obj The object that will serve as the parent
     * @param state The state bean of the parent object (for audit)
     * @param type The type of the new object
     */
    public ObjexObj newObject(ObjexObj obj, ObjexObjStateBean state, String type);
    
    /**
     * Call to add the given object to the transaction. The container
     * is now free to clone the state object so it can know the 
     * changes that have occurred. This is done only from within the
     * object that is changing.
     * 
     * @param obj The object about to be updated
     * @param state The current state of the object
     */
    public void addObjectToTransaction(ObjexObj obj, ObjexObjStateBean state);
    
    /**
     * Called to remove an object from the container.
     * 
     * @param obj The object to be removed
     */
    public void removeObject(ObjexObj obj, ObjexObjStateBean state);
}
