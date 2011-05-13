package org.talframework.objexj.object.fields;

import java.util.Map;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.exceptions.ObjectInvalidException;

public class ObjexChildMap<K, V> extends ObjexRefMap<K, V> {

    /**
     * Constructor for using with a brand new object
     * 
     * @param owner The object that owns this set
     */
    public ObjexChildMap(ObjexObj owner) {
        super(owner);
    }
    
    /**
     * Constructor for using with an existing set of references,
     * i.e. has been loaded from store.
     * 
     * @param owner The object that owns this set
     * @param refs The IDs of the objects already held
     */
    public ObjexChildMap(ObjexObj owner, Map<K, ObjexID> refs) {
        super(owner, refs);
    }
    
    /**
     * Overridden to actually create the objex object from obj
     */
    @Override
    protected ObjexRef createRef(ObjexRef existing, K key, Object obj) {
        // If existing, remove it!
        if( existing != null ) {
            getInternalContainer().removeObject(existing.getObj(getContainer()));
        }
        
        // Ensure we are not dealing with an initialised objex obj
        if( obj instanceof ObjexObj && ((ObjexObj)obj).getContainer() != null ) {
            throw new ObjectInvalidException("Cannot add initialised ObjexObj as child");
        }
        
        // Turn the object into a real object
        ObjexObj newObject = getInternalContainer().createObject(getOwningObject(), obj);
        
        // Set on reference
        if( existing == null ) existing = new ObjexRef(newObject.getId());
        else existing.setId(newObject.getId());
        existing.setObj(newObject);
        return existing;
    }
    
    /**
     * Overridden to remove the object from the container
     */
    @Override
    protected void removeRef(ObjexRef ref) {
        if( ref != null ) {
            getInternalContainer().removeObject(ref.getObj(getContainer()));
        }
    }
}
