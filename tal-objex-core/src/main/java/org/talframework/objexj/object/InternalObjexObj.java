package org.talframework.objexj.object;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.InternalContainer;

public interface InternalObjexObj extends ObjexObj {

    /**
     * Called by the Objex framework to initialise an object post
     * it's constructor.
     * 
     * @param container
     * @param id
     * @param parentId
     */
    public void init(InternalContainer container, ObjexID id, ObjexID parentId);
}
