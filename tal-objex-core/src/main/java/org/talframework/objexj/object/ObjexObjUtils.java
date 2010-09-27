package org.talframework.objexj.object;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.DefaultObjexID;

/**
 * This static class provides the core implementation of the
 * field related methods on a on ObjexObj. The generator
 * delegates to this class. The core responsibilities of this
 * class are:
 * <ul>
 * <li>For setting a field, determines if the field has changed
 * and if so ensure the state bean is editable, i.e. is in 
 * the transaction.</li>
 * <li>For setting a reference field implements a generic 
 * copy if the instance is not an ObjexObj instance.</li>
 * <li>For a list field provides methods to get at an element
 * by index, removing by index or by ID and removing all.</li>
 * <li>For a map field provides methods to get at an element
 * by key, removing by key or by ID and removing all.</li>
 * </ul>
 * 
 * TODO: Decide if this is not just part of ObjectUtils
 *
 * @author Tom Spencer
 */
public final class ObjexObjUtils {
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static <T> T setSimple(BaseObjexObj obj, ObjexObjStateBean state, T val, T current) {
        if( (val == null && current != null) || (val != null && !val.equals(current)) ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static double setSimple(BaseObjexObj obj, ObjexObjStateBean state, double val, double current) {
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static float setSimple(BaseObjexObj obj, ObjexObjStateBean state, float val, float current) {
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static long setSimple(BaseObjexObj obj, ObjexObjStateBean state, long val, long current) {
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static int setSimple(BaseObjexObj obj, ObjexObjStateBean state, int val, int current) {
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static short setSimple(BaseObjexObj obj, ObjexObjStateBean state, short val, short current) {
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static boolean setSimple(BaseObjexObj obj, ObjexObjStateBean state, boolean val, boolean current) {
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static char setSimple(BaseObjexObj obj, ObjexObjStateBean state, char val, char current) {
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Simple helper to prepare the value to set on the state
     * bean. If the value has changed the object will be marked
     * in the current transaction if it is not already.
     */
    public static byte setSimple(BaseObjexObj obj, ObjexObjStateBean state, byte val, byte current) {
        if( val != current ) setEditable(obj, state);
        return val;
    }
    
    /**
     * Helper method that sets a reference field to the incoming
     * ObjexObj field.
     */
    public static <T> String setReference(BaseObjexObj obj, ObjexObjStateBean state, T val, String current) {
        String ret = current;
        
        String newId = null;
        if( val instanceof ObjexObj ) newId = ((ObjexObj)val).getId().toString();
        else if( val instanceof ObjexID ) newId = ((ObjexID)val).toString();
        else if( val instanceof String ) newId = val.toString();
        else throw new IllegalArgumentException("Attempt to set a reference field with a non-ObjexObj object");
            
        // Test if it has changed
        if( current == null || !current.equals(newId) ) {
            ret = newId;
            setEditable(obj, state);
        }
        
        return ret;
    }
    
    /**
     * Helper method copies the incoming object into the object referenced
     * by this field already. If the incoming object is an ObjexObj and
     * is not the one we already have then an error is thrown.
     */
    public static <T> String setOwnedReference(BaseObjexObj obj, ObjexObjStateBean state, String type, T val, String current) {
        String ret = current;
        
        // If objex obj, ensure it is the same one and do nothing
        if( val instanceof ObjexObj ) {
            if( !((ObjexObj)val).getId().toString().equals(current) ) {
                throw new IllegalArgumentException("Attempt to change the ObjexObj instance on a owned reference field");
            }
        }
        
        else {
            // Create a new instance as necc
            ObjexObj currentObj = null;
            if( current == null ) {
                currentObj = obj.getInternalContainer().newObject(obj, state, type);
            }
            else {
                currentObj = obj.getContainer().getObject(current);
            }
            
            copyObjects(currentObj, val);
        }
        
        return ret;
    }

    public static <T> String createOwnedReference(BaseObjexObj obj, ObjexObjStateBean state, String type, String current) {
        if( current != null ) removeObject(current);
        
        ObjexObj newObj = obj.getInternalContainer().newObject(obj, state, type);
        setEditable(obj, state);
        return newObj.getId().toString();
    }
    
    /**
     * Helper to set a reference list against a set of incoming objects 
     */
    public static <T> List<String> setList(BaseObjexObj obj, ObjexObjStateBean state, boolean owned, List<T> val, List<String> current) {
        List<String> ret = current;
        
        // If owned
        if( owned ) {
            // TODO: Complex!!
        }
        
        // If not owned, each val must be an ObjexObj instances
        else {
            ret = new ArrayList<String>(val.size());
            Iterator<T> it = val.iterator();
            while( it.hasNext() ) {
                T ref = it.next();
                if( !(ref instanceof ObjexObj) ) throw new IllegalArgumentException("Attempt to set reference list using non-ObjexObj properties: " + ref);
                ret.add(((ObjexObj)ref).getId().toString());
            }
        }
        
        if( (ret != null && !ret.equals(current)) || current != null ) setEditable(obj, state);
        else ret = current;
        
        return ret;
    }
    
    /**
     * Helper to add a new element to a list
     */
    public static List<String> addList(BaseObjexObj obj, ObjexObjStateBean state, String type, List<String> current) {
        List<String> ret = null;
        
        if( current != null ) ret = new ArrayList<String>(current);
        else ret = new ArrayList<String>();
        
        ObjexObj newObj = obj.getInternalContainer().newObject(obj, state, type);
        setEditable(obj, state);
        ret.add(newObj.getId().toString());
        
        return ret;
    }

    /**
     * Helper to get a list element by index
     */
    public static <T> T getListByIndex(BaseObjexObj obj, ObjexObjStateBean state, int index, List<String> current, Class<T> expected) {
        T ret = null;
        
        String id = null;
        if( current != null && index >= 0 && index < current.size() ) id = current.get(index);
        
        if( id != null ) {
            ObjexObj refObj = obj.getContainer().getObject(id);
            if( refObj == null ) ret = null;
            else if( !ObjexObj.class.equals(expected) ) ret = refObj.getBehaviour(expected);
            else ret = expected.cast(refObj);
        }
        
        return ret;
    }
    
    public static List<String> removeListByIndex(BaseObjexObj obj, ObjexObjStateBean state, boolean owned, int index, List<String> current) {
        List<String> ret = current;
        
        String id = null;
        if( current != null && index >= 0 && index < current.size() ) id = current.get(index);
        
        // Remove all by the ID if owned
        if( id != null && owned ) {
            removeListById(obj, state, owned, id, current);
        }
        
        // Remove just that 1 index
        else if( id != null && !owned ) {
            setEditable(obj, state);
            ret = new ArrayList<String>(current);
            ret.remove(index);
        }
        
        return ret;
    }
    
    /**
     * Helper to remove from a list an object by its ID
     */
    public static List<String> removeListById(BaseObjexObj obj, ObjexObjStateBean state, boolean owned, Object id, List<String> current) {
        if( current == null || current.size() == 0 ) return current;
        
        ObjexID realId = DefaultObjexID.getId(id);
        
        List<String> ret = new ArrayList<String>(current);
        boolean removed = false;
        Iterator<String> it = current.iterator();
        while( it.hasNext() ) {
            String ref = it.next();
            if( realId.equals(ref) ) {
                setEditable(obj, state);
                it.remove();
                removed = true;
            }
        }
        
        if( removed ) {
            removeObject(realId);
        }
        
        return ret;
    }

    /**
     * Helper to remove all objects in a list
     */
    public static List<String> removeListAll(BaseObjexObj obj, ObjexObjStateBean state, boolean owned, List<String> current) {
        if( current == null || current.size() == 0 ) return current;
        
        if( owned ) removeObjects(current);
        
        setEditable(obj, state);
        return null;
    }
    
    
    /**
     * Helper to set a reference list against a set of incoming objects 
     */
    public static <T> Map<String, String> setMap(BaseObjexObj obj, ObjexObjStateBean state, boolean owned, Map<String, T> val, Map<String, String> current) {
        Map<String, String> ret = current;
        
        // If owned
        if( owned ) {
            // TODO: Complex!!
        }
        
        // If not owned, each val must be an ObjexObj instances
        else {
            ret = new HashMap<String, String>();
            Iterator<String> it = val.keySet().iterator();
            while( it.hasNext() ) {
                String key = it.next();
                T ref = val.get(key);
                if( !(ref instanceof ObjexObj) ) throw new IllegalArgumentException("Attempt to set reference list using non-ObjexObj properties: " + ref);
                ret.put(key, ((ObjexObj)ref).getId().toString());
            }
        }
        
        if( (ret != null && !ret.equals(current)) || current != null ) setEditable(obj, state);
        else ret = current;
        
        return ret;
    }
    
    /**
     * Helper to add a new element to a map
     */
    public static Map<String, String> addMap(BaseObjexObj obj, ObjexObjStateBean state, boolean owned, String type, String key, Map<String, String> current) {
        Map<String, String> ret = null;
        
        if( current != null ) {
            if( owned && current.containsKey(key) ) removeObject(current.get(key)); 
            ret = new HashMap<String, String>(current);
        }
        else {
            ret = new HashMap<String, String>();
        }
        
        ObjexObj newObj = obj.getInternalContainer().newObject(obj, state, type);
        setEditable(obj, state);
        ret.put(key, newObj.getId().toString());
        
        return ret;
    }

    /**
     * Helper to get a map element by key
     */
    public static <T> T getMapByKey(BaseObjexObj obj, ObjexObjStateBean state, String key, Map<String, String> current, Class<T> expected) {
        T ret = null;
        
        String id = null;
        if( current != null && current.containsKey(key) ) id = current.get(key);
        
        if( id != null ) {
            ObjexObj refObj = obj.getContainer().getObject(id);
            if( refObj == null ) ret = null;
            else if( !ObjexObj.class.equals(expected) ) ret = refObj.getBehaviour(expected);
            else ret = expected.cast(refObj);
        }
        
        return ret;
    }
    
    /**
     * Helper to remove from a map by key
     */
    public static Map<String, String> removeMapByKey(BaseObjexObj obj, ObjexObjStateBean state, boolean owned, String key, Map<String, String> current) {
        Map<String, String> ret = current;
        
        String id = null;
        if( current != null && current.containsKey(key) ) {
            if( owned ) removeMapById(obj, state, owned, id, current);
            setEditable(obj, state);
            
            ret = new HashMap<String, String>(current);
            ret.remove(key);
        }
        
        return ret;
    }
    
    /**
     * Helper to remove from a list an object by its ID
     */
    public static Map<String, String> removeMapById(BaseObjexObj obj, ObjexObjStateBean state, boolean owned, Object id, Map<String, String> current) {
        if( current == null || current.size() == 0 ) return current;
        
        ObjexID realId = DefaultObjexID.getId(id);
        
        HashMap<String, String> ret = new HashMap<String, String>(current);
        boolean removed = false;
        Iterator<String> it = current.keySet().iterator();
        while( it.hasNext() ) {
            String ref = current.get(it.next());
            if( realId.equals(ref) ) {
                setEditable(obj, state);
                it.remove();
                removed = true;
            }
        }
        
        if( removed ) {
            removeObject(realId);
        }
        
        return ret;
    }

    /**
     * Helper to remove all objects in a map
     */
    public static Map<String, String> removeMapAll(BaseObjexObj obj, ObjexObjStateBean state, boolean owned, Map<String, String> current) {
        if( current == null || current.size() == 0 ) return current;
        
        if( owned ) removeObjects(current);
        
        setEditable(obj, state);
        return null;
    }
    
    
    //////////////////////////////////////////////
    // Heleprs
    
    /**
     * Internal helper to ensure the state bean is editable
     * and if not add the object to the container.
     */
    private static void setEditable(BaseObjexObj obj, ObjexObjStateBean state) {
        if( !state.isEditable() ) {
            obj.getInternalContainer().addObjectToTransaction(obj, state);
            if( !state.isEditable() ) state.setEditable();
        }
    }
    
    /**
     * Helper to copy all of the incoming objects properties into
     * current where they match on name.
     * 
     * FUTURE: Use our own bean implementation
     */
    private static void copyObjects(Object current, Object incoming) {
        BeanWrapper currentWrapper = new BeanWrapperImpl(current);
        BeanWrapper incomingWrapper = new BeanWrapperImpl(incoming);
        
        PropertyDescriptor[] props = incomingWrapper.getPropertyDescriptors();
        for( int i = 0 ; i < props.length ; i++ ) {
            if( currentWrapper.isWritableProperty(props[i].getName()) ) {
                currentWrapper.setPropertyValue(props[i].getName(), incomingWrapper.getPropertyValue(props[i].getName()));
            }
        }
    }
 
    /**
     * Helper to remove a specific object from the container
     */
    private static void removeObject(Object id) {
       // TODO: Remove the object 
    }
    
    /**
     * Helper to remove all objects from a list
     */
    private static void removeObjects(List<String> ids) {
       // TODO: Remove at the container level 
    }
    
    /**
     * Helper to remove all objects from a list
     */
    private static void removeObjects(Map<String, String> ids) {
       // TODO: Remove at the container level 
    }
}
