package org.talframework.objexj.object.fields;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;

/**
 * This fairly simple class is held by the Objex collections
 * implementations as the value. Each instance holds a reference
 * and then an optional cached object, which it will get as
 * needed if the container is provided. The only other piece
 * of logic is that if the ID) is changed then any cached object
 * is cleared automatically.
 *
 * @author Tom Spencer
 */
class ObjexRef {
    /** The reference to the object */
    private ObjexID id;
    /** The (cached) object itself */
    private ObjexObj obj;
    
    public ObjexRef(ObjexID id) {
        this.id = id;
        this.obj = null;
    }
    
    /**
     * @return the id
     */
    public ObjexID getId() {
        return id;
    }
    
    /**
     * Setter for the id field
     *
     * @param ref the id to set
     */
    public void setId(ObjexID id) {
        this.id = id;
        this.obj = null;
    }
    
    /**
     * @return the obj
     */
    public ObjexObj getObj(Container container) {
        if( obj == null ) obj = container.getObject(id);
        return obj;
    }
    
    /**
     * Setter for the obj field
     *
     * @param obj the obj to set
     */
    public void setObj(ObjexObj obj) {
        this.obj = obj;
    }
}