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

package org.talframework.objexj.runtime.gae;

import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.ObjexIDStrategy;

/**
 * This strategy is used when a container is first created to
 * create IDs in a range. As the IDStrategy is created just
 * for the middleware instance (and no-one else is creating)
 * this is fine for most purposes.
 * 
 * SUGGEST: Should this synchronise itself, fine in WebApp, not from WebService
 * 
 * @author Tom Spencer
 */
public final class GAENewObjexIDStrategy implements ObjexIDStrategy {

    private long lastId;
    
    public GAENewObjexIDStrategy(long last) {
        this.lastId = last;
    }
    
    /**
     * Simply creates a new ID and increments the last number
     */
    public ObjexID createId(Container container, Class<? extends ObjexObjStateBean> stateType, String type, ObjexObj obj) {
        return new DefaultObjexID(type, ++lastId);
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
