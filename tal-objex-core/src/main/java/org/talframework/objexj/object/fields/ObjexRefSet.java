package org.talframework.objexj.object.fields;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.exceptions.ObjectInvalidException;

/**
 * This class represents a set of ObjexObj references. The references
 * are held initially as strings, but the map exposes the actual 
 * underlying object which is obtained from the container and then
 * held (or cached).
 * 
 * SUGGEST: Add a class parameter so we getBehaviour instead of unsafe cast to E
 * 
 * @author Tom Spencer
 * @param <E> The type of element this will actually store
 */
public class ObjexRefSet<E> extends AbstractSet<E> {

    /** The object this list belongs to */
    private final ObjexObj owningObject;
    /** Member holds the real internal list of references */ 
    protected Set<ObjexRef> refs;
    
    /**
     * Constructor for using with a brand new object
     * 
     * @param owner The object that owns this set
     */
    public ObjexRefSet(ObjexObj owner) {
        this.owningObject = owner;
        this.refs = new HashSet<ObjexRef>();
    }
    
    /**
     * Constructor for using with an existing set of references,
     * i.e. has been loaded from store.
     * 
     * @param owner The object that owns this set
     * @param refs The IDs of the objects already held
     */
    public ObjexRefSet(ObjexObj owner, Set<ObjexID> refs) {
        this(owner);
        
        for( ObjexID ref : refs ) {
            this.refs.add(new ObjexRef(ref));
        }
    }
    
    /**
     * @return The internal container
     */
    protected Container getContainer() {
        return owningObject.getContainer();
    }
    
    /**
     * @return The internal container
     */
    protected InternalContainer getInternalContainer() {
        return InternalContainer.class.cast(owningObject.getContainer());
    }
    
    /**
     * @return The object that 'owns' this list
     */
    protected ObjexObj getOwningObject() {
        return owningObject;
    }
    
    /**
     * Helper to turn this list into a set of pure ObjexID
     * stringified references.
     * 
     * @return This set as a set of references
     */
    public Set<String> toRefSet() {
        Set<String> ret = new HashSet<String>();
        for( ObjexRef ref : refs ) {
            ret.add(ref.getId().toString());
        }
        return ret;
    }
    
    ///////////////////////////////////////////////////////
    
    @Override
    public int size() {
        return refs.size();
    }
    
    @Override
    public Iterator<E> iterator() {
        return new ObjexSetIterator(refs.iterator());
    }
    
    @Override
    public boolean add(E obj) {
        // Ensure we are dealing with an objex obj
        if( !(obj instanceof ObjexObj) ) throw new ObjectInvalidException("Adding as a reference");
        
        // Ensure object is from same container
        ObjexObj objexObj = ObjexObj.class.cast(obj);
        if( objexObj.getContainer() != getContainer() ) {
            throw new ObjectInvalidException("Adding an object from another container, or another instance (/transaction) of same container");
        }
        
        // Ensure we don't already have this object
        ObjexID id = objexObj.getId();
        for( ObjexRef ref : refs ) {
            if( ref.getId().equals(id) ) throw new ObjectInvalidException("Cannot add reference to same object twice in a set, " + id);
        }

        // Finally create the ref and add it
        ObjexRef ref = new ObjexRef(id);
        ref.setObj(objexObj);
        return refs.add(ref);
    }
    
    /**
     * Internal method called when about to remove a reference
     * from the list. The default does nothing, but can be
     * overridden to actually delete the object from the container
     * if owned set.
     * 
     * @param ref The reference about to be removed
     */
    protected void removeRef(ObjexRef ref) {
    }
    
    /**
     * Inner class that is the iterator for the ObjexRefSet.
     *
     * @author Tom Spencer
     * @param <E>
     */
    class ObjexSetIterator implements Iterator<E> {
        
        /** The iterator from the set backing up the ObjexRefSet */
        private final Iterator<ObjexRef> it;
        /** The ObjexRef from last call to next - used if we get a call to remove */
        private ObjexRef last;
        
        public ObjexSetIterator(Iterator<ObjexRef> it) {
            this.it = it;
        }
        
        @Override
        public boolean hasNext() {
            return it.hasNext();
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            last = it.next();
            return (E)last.getObj(getContainer());
        }
        
        @Override
        public void remove() {
            removeRef(last);
            it.remove();
        }
    }
}
