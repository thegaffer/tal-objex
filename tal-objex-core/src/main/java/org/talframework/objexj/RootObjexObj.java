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

import org.talframework.objexj.events.EventListener;

/**
 * This interface is implemented by any object that is
 * capable of being the root object inside a container.
 * A root object is the primary object and has a special
 * role to obtain determine the current status of the
 * container as a whole and to obtain the header that
 * is used when notifying any listeners of a new
 * container. If the root object does not support this
 * interface it simply means the container will have
 * no status and no header is sent out with events when
 * the container is saved.
 * 
 * @author Tom Spencer
 */
public interface RootObjexObj {

    /**
     * @return The current status of the container
     */
    public String getStatus();
    
    /**
     * The header is sent out in any event to listeners
     * of this container. The header should contain only
     * the key details of this container - and further
     * these details can only be string values. Whilst
     * the values of the header can be calculated unless
     * this is non-trivial we recommend always saving 
     * the header elements in the root object.
     * 
     * @return The set of header items for the container
     */
    public Map<String, String> getHeader();
    
    /**
     * This method is called to get any applicable listeners
     * for this container.
     * 
     * @return The applicable listeners to send events to.
     */
    public List<EventListener> getListeners();
    
    /**
     * Called to get any existing errors on the container.
     * This is called at the beginning of the first validation
     * run since opening.
     * 
     * @return The existing errors
     */
    public ValidationRequest getErrors();
    
    /**
     * This method is called after any validate on the container
     * as a whole. It gives the root object a chance to save
     * the errors if it is acceptable to save the container with
     * any errors. The root object can also adjust the errors
     * if required.
     * 
     * @param request The validation request
     * @return The amended validation request (may be the original)
     */
    public ValidationRequest processValidation(ValidationRequest request);
    
    /**
     * This is called before saving to make sure the root object
     * believes it is valid to save given any errors etc.
     * 
     * @param request The validation request
     * @return True if we can save, false otherwise
     */
    public boolean canSave(ValidationRequest request);
}
