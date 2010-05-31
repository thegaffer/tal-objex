package org.tpspencer.tal.objexj.object;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;

public interface InternalObjexObj extends ObjexObj {

    /**
     * Called by the Objex framework to initialise an object post
     * it's constructor.
     * 
     * @param container
     * @param id
     * @param parentId
     */
    public void init(Container container, ObjexID id, ObjexID parentId);
}
