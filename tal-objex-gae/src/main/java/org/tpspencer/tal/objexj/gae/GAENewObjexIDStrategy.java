package org.tpspencer.tal.objexj.gae;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexIDStrategy;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.DefaultObjexID;

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

    private long lastId = 1;
    
    public GAENewObjexIDStrategy(long last) {
        this.lastId = last;
    }
    
    /**
     * Simply creates a new ID and increments the last number
     */
    public ObjexID createId(Container container, Class<? extends ObjexObjStateBean> stateType, String type, ObjexObj obj) {
        return new DefaultObjexID(type, lastId++);
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
