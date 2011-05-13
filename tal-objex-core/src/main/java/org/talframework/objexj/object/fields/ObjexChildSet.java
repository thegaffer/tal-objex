package org.talframework.objexj.object.fields;

import java.util.Set;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.exceptions.ObjectInvalidException;

/**
 * This class represents a set of child objects and is held by another
 * object.
 *
 * @author Tom Spencer
 */
public class ObjexChildSet<E> extends ObjexRefSet<E> {

    /**
     * Constructor for using with a brand new object
     * 
     * @param owner The object that owns this set
     */
    public ObjexChildSet(ObjexObj owner) {
        super(owner);
    }
    
    /**
     * Constructor for using with an existing set of references,
     * i.e. has been loaded from store.
     * 
     * @param owner The object that owns this set
     * @param refs The IDs of the objects already held
     */
    public ObjexChildSet(ObjexObj owner, Set<ObjexID> refs) {
        super(owner, refs);
    }
    
    /**
     * Overridden to ensure the object is not an initialised ObjexObj and
     * then add it to container before adding it to set.
     */
    @Override
    public boolean add(E obj) {
        // Ensure we are not dealing with an initialised objex obj
        if( obj instanceof ObjexObj && ((ObjexObj)obj).getContainer() != null ) {
            throw new ObjectInvalidException("Cannot add initialised ObjexObj as child");
        }
        
        // Turn the object into a real object/initialise it
        ObjexObj newObject = getInternalContainer().createObject(getOwningObject(), obj);
        
        // Set on reference
        ObjexRef ref = new ObjexRef(newObject.getId());
        ref.setObj(newObject);
        return refs.add(ref);
    }
    
    /**
     * Removes the object being removed from the container
     */
    @Override
    protected void removeRef(ObjexRef ref) {
        getInternalContainer().removeObject(ref.getObj(getContainer()));
    }
}
