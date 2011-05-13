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
import org.talframework.objexj.ObjexObj;

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
     * Called to create a new object in this container and
     * return it. This is typically called from internal objects
     * when they are creating new children. 
     * 
     * @param parentObj The object that will serve as the parent
     * @param source The source object, which contains the type of object and any seed values to use
     */
    public ObjexObj createObject(ObjexObj parentObj, Object source);
    
    /**
     * Called to remove an object from the container.
     * 
     * TODO: Do we intend this to be called by parent, or from object itself?!?
     * 
     * @param obj The object to be removed
     */
    public void removeObject(ObjexObj obj);
}
