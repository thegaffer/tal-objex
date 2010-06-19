package org.tpspencer.tal.objexj.object;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;

/**
 * This class contains various helper methods that can be
 * used to implement Objex Objects.
 * 
 * @author Tom Spencer
 */
public class ObjectUtils {
    
    /**
     * Given an object we expect to be a real ObjexObj
     * return its ID.
     * 
     * @param obj The object (either an ObjexObj or a obj)
     * @return The id of the object
     */
    public static ObjexID getObjectId(Object obj) {
        if( obj instanceof ObjexObj ) return ((ObjexObj)obj).getId();
        else if( obj instanceof ObjexID ) return ((ObjexID)obj);
        else return null; // TODO: Throw exception
    }
    
    /**
     * Given an object we expect to be a real ObjexObj
     * return its ID as a reference
     * 
     * @param obj The object (either an ObjexObj or a obj)
     * @return The id of the object
     */
    public static String getObjectRef(Object obj) {
        ObjexID ret = getObjectId(obj);
        return ret != null ? ret.toString() : null;
    }
    
    /**
     * Given an id that is either a stringified refernece or
     * an ObjexID directly return its real ID. The object is
     * treated as local to the provided objex obj
     * 
     * @param obj An ObjexObj that is requesting this service
     * @param id The ID/reference
     * @return The real objex ID
     */
    public static ObjexID getObjectId(ObjexObj obj, Object id) {
        return obj.getContainer().getObjectId(id);
    }
    
    /**
     * Given an id that is either a stringified refernece or
     * an ObjexID directly return its real ID. The object is
     * treated as local to the provided objex obj
     * 
     * @param obj An ObjexObj that is requesting this service
     * @param id The ID
     * @return The id as a stringified reference
     */
    public static String getObjectRef(ObjexObj obj, Object id) {
        ObjexID ret = getObjectId(obj, id);
        return ret != null ? ret.toString() : null;
    }
    
    /**
     * Call to get an object given its ID from the container
     * that holds the object.
     * 
     * @param obj The requesting object
     * @param id The ID of the object to get
     * @param expected The type of object expected
     * @return The object
     */
    public static <T> T getObject(ObjexObj obj, Object id, Class<T> expected) {
        return obj.getContainer().getObject(id, expected);
    }
    
    /**
     * Call to get a list of objects given their references
     * and the object which owns this list.
     * 
     * TODO: Inefficient, use container method!?!
     * 
     * @param obj The object that owns the list
     * @param refs The references
     * @param expected The expected base type of the references
     * @return The returned list
     */
    public static <T> List<T> getObjectList(ObjexObj obj, List<String> refs, Class<T> expected) {
        if( refs == null ) return null;
        
        ArrayList<T> ret = new ArrayList<T>();
        
        Container container = obj.getContainer();
        Iterator<String> it = refs.iterator(); 
        while( it.hasNext() ) {
            T t = container.getObject(it.next(), expected);
            if( t != null ) ret.add(t);
        }
        
        return ret;
    }
    
    /**
     * Test that an object is held by the list of references.
     * 
     * @param refs The refs that should hold ID
     * @param id The ID of the object we are after
     * @return True if the id is held
     * @throws IllegalArgumentException If not held
     */
    public static boolean testObjectHeld(List<String> refs, Object id) {
        boolean ret = refs != null ? refs.contains(id.toString()) : false;
        if( !ret ) throw new IllegalArgumentException("The object [" + id + "] is not held so cannot be returned");
        return ret;
    }
    
    /**
     * Call to create an object of the given type with a
     * parent of the given object.
     * 
     * @param obj The parent object
     * @param type The type of object to create
     * @return The new object
     */
    public static ObjexObj createObject(ObjexObj obj, String type) {
        Container container = obj.getContainer();
        if( container instanceof EditableContainer ) {
            return ((EditableContainer)container).newObject(type, obj.getId());
        }
        else {
            // TODO: Throw an exception!!!
            return null;
        }
    }
    
    /**
     * Call to remove an object from the container.
     * 
     * @param obj The object we are removing from
     * @param id The ID of the object to remove
     */
    public static void removeObject(ObjexObj obj, String id) {
        Container container = obj.getContainer();
        if( container instanceof EditableContainer ) {
            ObjexID realId = container.getObjectId(id);
            ((EditableContainer)container).removeObject(realId);
        }
        else {
            // TODO: Throw an exception!!!
        }
    }
}
