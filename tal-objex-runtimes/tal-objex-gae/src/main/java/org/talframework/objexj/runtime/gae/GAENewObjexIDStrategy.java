/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
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
