/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.object.fields;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.exceptions.ObjectInvalidException;

/**
 * This class represents a map of ObjexObj references. The references
 * are held initially as strings, but the map exposes the actual 
 * underlying object which is obtained from the container and then
 * held (or cached).
 * 
 * SUGGEST: Add a class parameter so we getBehaviour instead of unsafe cast to V
 * FUTURE: This is not a particularly efficient implementation at present!
 * 
 * @author Tom Spencer
 * @param <E> The type of element this will actually store
 */
public class ObjexRefMap<K, V> extends AbstractMap<K, V> {
    
    /** The object this list belongs to */
    private final ObjexObj owningObject;
    /** Member holds the real internal map of references */ 
    protected Map<K, ObjexRef> refs;
    
    /**
     * Constructor for using with a brand new object
     * 
     * @param owner The object that owns this set
     */
    public ObjexRefMap(ObjexObj owner) {
        this.owningObject = owner;
        this.refs = new HashMap<K, ObjexRef>();
    }
    
    /**
     * Constructor for using with an existing set of references,
     * i.e. has been loaded from store.
     * 
     * @param owner The object that owns this set
     * @param refs The IDs of the objects already held
     */
    public ObjexRefMap(ObjexObj owner, Map<K, ObjexID> refs) {
        this(owner);
        
        for( K key : refs.keySet() ) {
            this.refs.put(key, new ObjexRef(refs.get(key)));
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
    
    protected ObjexRef createRef(ObjexRef existing, K key, Object obj) {
        // Ensure we are dealing with an objex obj
        if( !(obj instanceof ObjexObj) ) throw new ObjectInvalidException("Adding as a reference");
        
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
     * Internal method called when we are about to remove a
     * reference from the map. 
     * 
     * @param ref
     * @return
     */
    protected void removeRef(ObjexRef ref) {
    }
    
    /**
     * Helper to turn this list into a list of pure ObjexID
     * stringified references.
     * 
     * @return This list as a list of references
     */
    public Map<String, String> toRefMap() {
        Map<String, String> ret = new HashMap<String, String>();
        for( Object key : refs.keySet() ) {
            ret.put(key.toString(), refs.get(key).getId().toString());
        }
        return ret;
    }

    //////////////////////////////////////////////////
    
    /**
     * The main method that AbstractMap requires. Implemented by
     * creating an inner class that serves up an iterator from 
     * the underlying map.
     */
    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return new MapEntrySet();
    }
    
    /**
     * Overridden because it will be quicker to ask the internal
     * map for its size.
     */
    @Override
    public int size() {
        return refs.size();
    }
    
    /**
     * Overridden because it will be quicker to ask the internal
     * map for the key set, although this is wrapped to ensure
     * you cannot remove via this interface. In the future we
     * could even allow remove here!
     */
    @Override
    public Set<K> keySet() {
        return new MapKeySet();
    }
    
    /**
     * Overridden because it will be quicker to ask the internal
     * map for an element
     */
    @SuppressWarnings("unchecked") 
    @Override
    public V get(Object key) {
        ObjexRef ref = refs.get(key);
        if( ref != null ) return (V)ref.getObj(getContainer());
        else return null;
    }
    
    /**
     * Overridden because it will be quicker to ask the internal
     * map it it contains the key
     */
    @Override
    public boolean containsKey(Object key) {
        return refs.containsKey(key);
    }
    
    /**
     * Overridden so the map is editable
     */
    @Override
    public V put(K key, V value) {
        ObjexRef existing = refs.get(key);
        @SuppressWarnings("unchecked")
        V ret = existing != null ? (V)existing.getObj(getContainer()) : null;
        
        ObjexRef ref = createRef(existing, key, value);
        if( ref != null ) refs.put(key, ref);
        
        return ret;
    }
    
    /**
     * Overridden because it is likely more efficient. 
     */
    @SuppressWarnings("unchecked") 
    @Override
    public V remove(Object key) {
        ObjexRef old = refs.remove(key);
        if( old != null ) removeRef(old);
        return old != null ? (V)old.getObj(getContainer()) : null;
    }
    
    /**
     * Overridden because it is likely more efficient, also
     * the derived class will override to actually remove the
     * objects from the container.
     */
    @Override
    public void clear() {
        refs.clear();
    }
    
    /**
     * This simple set is so we can wrap the keyset from the 
     * underlying map and ensure you cannot remove from the map
     * via this set - otherwise child objects might go missing!
     *
     * @author Tom Spencer
     */
    class MapKeySet extends AbstractSet<K> {
        
        @Override
        public int size() {
            return refs.size();
        }
        
        @Override
        public Iterator<K> iterator() {
            return new Iterator<K>() {
                private Iterator<K> it = refs.keySet().iterator();
                
                @Override
                public boolean hasNext() {
                    return it.hasNext();
                }
                
                @Override
                public K next() {
                    return it.next();
                }
                
                @Override
                public void remove() {
                    throw new UnsupportedOperationException("Cannot remove from the key set via ObjexMap");
                }
            };
        }
    }
    
    /**
     * The class for the entry set that converts between a 
     * Map<K, ObjexRef> which is held internally and the
     * Map<K, V> exposed externally. This sits atop the internal
     * maps entry set.
     *
     * @author Tom Spencer
     */
    class MapEntrySet extends AbstractSet<Map.Entry<K, V>> {
        
        @Override
        public int size() {
            return refs.size();
        }
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new ObjexEntrySetIterator(refs.entrySet().iterator());
        }
    
        /**
         * Inner (inner) class that is the iterator for the ObjexRefSet.
         *
         * @author Tom Spencer
         * @param <E>
         */
        class ObjexEntrySetIterator implements Iterator<Map.Entry<K, V>> {
            
            /** The iterator from the set backing up the ObjexRefSet */
            private final Iterator<Map.Entry<K, ObjexRef>> it;
            /** The ObjexRef from last call to next - used if we get a call to remove */
            private Map.Entry<K, ObjexRef> last;
            
            public ObjexEntrySetIterator(Iterator<Map.Entry<K, ObjexRef>> it) {
                this.it = it;
            }
            
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }
            
            @SuppressWarnings("unchecked")
            @Override
            public Map.Entry<K, V> next() {
                last = it.next();
                
                return new Map.Entry<K, V>() {
                    @Override
                    public K getKey() {
                        return last.getKey();
                    }
                    
                    @Override
                    public V getValue() {
                        return (V)last.getValue().getObj(getContainer());
                    }
                    
                    @Override
                    public V setValue(V arg0) {
                        throw new UnsupportedOperationException("Cannot set using the entry set");
                    }
                };
            }
            
            @Override
            public void remove() {
                removeRef(last.getValue());
                it.remove();
            }
        }
    }
}
