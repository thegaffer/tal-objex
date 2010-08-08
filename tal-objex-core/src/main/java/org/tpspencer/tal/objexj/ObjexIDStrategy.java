package org.tpspencer.tal.objexj;

/**
 * This interface represents a class that can create objex IDs.
 * This interface is used in two scenarios. 
 * 
 * <p>Firstly when the object is first created. At this time 
 * the object will be null and it is possible for a Temporary 
 * ObjexID to be assigned.</p>
 * 
 * <p>The second time is when the object is actually persisted
 * if it was given a temporary ID the first time. In this case
 * the object being created will be a real object.</p> 
 * 
 * @author Tom Spencer
 */
public interface ObjexIDStrategy {

    /**
     * Call to create an ID. If obj is null then the object is
     * being created initially and it is legal to return a Temp
     * ID.
     * 
     * @param container The container
     * @param stateType The type of state object
     * @param type The type of object
     * @param obj The finished object (if present a temp ID cannot be returned)
     * @return The ID
     */
    public ObjexID createId(EditableContainer container, Class<? extends ObjexObjStateBean> stateType, String type, ObjexObj obj);
}