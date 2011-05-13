package org.talframework.objexj.object.fields;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.exceptions.ObjectInvalidException;

/**
 * This class holds a list of objex object references, but exposes
 * them as real objects by automatically getting them as needed.
 * 
 * SUGGEST: Add a class parameter so we getBehaviour instead of unsafe cast to E
 * 
 * @author Tom Spencer
 * @param <E> The type of element this will actually store
 */
public class ObjexRefList<E> extends AbstractList<E> {
    
    /** The object this list belongs to */
    private final ObjexObj owningObject;
    /** Member holds the real internal list of references */ 
    private List<ObjexRef> refs;
    
    public ObjexRefList(ObjexObj owner) {
        this.owningObject = owner;
        this.refs = new ArrayList<ObjexRef>();
    }
    
    /**
     * Constructor for using with an existing set of references,
     * i.e. has been loaded from store.
     * 
     * @param owner The object that owns this set
     * @param refs The IDs of the objects already held
     */
    public ObjexRefList(ObjexObj owner, List<ObjexID> refs) {
        this(owner);
        
        for( ObjexID ref : refs ) {
            this.refs.add(new ObjexRef(ref));
        }
    }
    
    protected ObjexRef getRef(int index, boolean failIfInvalid) {
        if( index < 0 || index >= refs.size() ) {
            if( failIfInvalid ) throw new ArrayIndexOutOfBoundsException(index);
            else return null;
        }
        else {
            return refs.get(index);
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
     * This method is called by any method to setup or change a reference.
     * If the ref is new, then existing will be null, otherwise change the
     * one provided.
     * 
     * <p>The base version ensure that the object is already an ObjexObj,
     * the derived owned version of this class will turn it into an 
     * ObjexObj if neccessary.</p>
     * 
     * @param ref
     * @param obj
     * @return The updated or new ref
     */
    protected ObjexRef setupRef(ObjexRef existing, E obj) {
        // Ensure we are dealing with an objex obj
        if( !(obj instanceof ObjexObj) ) throw new ObjectInvalidException("Adding non-ObjexObj as a reference");
        
        // Ensure object is from same container
        ObjexObj objexObj = ObjexObj.class.cast(obj);
        if( objexObj.getContainer() != getContainer() ) {
            throw new ObjectInvalidException("Adding an object from another container, or another instance (/transaction) of same container");
        }
        
        // Set on reference
        if( existing == null ) existing = new ObjexRef(objexObj.getId());
        else existing.setId(objexObj.getId());
        existing.setObj(objexObj);
        return existing;
    }
    
    /**
     * Helper to turn this list into a list of pure ObjexID
     * stringified references.
     * 
     * @return This list as a list of references
     */
    public List<String> toRefList() {
        List<String> ret = new ArrayList<String>();
        for( ObjexRef ref : refs ) {
            ret.add(ref.getId().toString());
        }
        return ret;
    }
    
    ///////////////////////////////////////

    @Override
    public int size() {
        return refs.size();
    }
    
    @Override
    public int indexOf(Object obj) {
        if( !(obj instanceof ObjexObj) ) return -1;
        
        int ret = -1;
        if( obj instanceof ObjexObj ) {
            ObjexID id = ObjexObj.class.cast(obj).getId();
            for( int i = 0 ; i < refs.size() ; i++ ) {
                if( refs.get(i).getId().equals(id) ) {
                    ret = i;
                    break;
                }
            }
        }
                
        return ret;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        ObjexRef ref = getRef(index, true);
        return (E)ref.getObj(getContainer());
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public E set(int index, E element) {
        ObjexRef ref = setupRef(getRef(index, true), element);
        return (E)ref.getObj(getContainer());
    }
    
    @Override
    public void add(int index, E element) {
        ObjexRef ref = setupRef(null, element);
        if( index >= size() ) refs.add(ref);
        else if( index <= 0 ) refs.add(0, ref);
        else refs.add(index, ref);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {
        ObjexRef ref = getRef(index, true);
        refs.remove(index);
        return (E)ref.getObj(getContainer());
    }    
}
