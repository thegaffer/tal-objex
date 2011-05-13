/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.objexj.object.writer;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObj.ObjexFieldType;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.object.fields.ObjexChildList;
import org.talframework.objexj.object.fields.ObjexChildMap;
import org.talframework.objexj.object.fields.ObjexChildSet;
import org.talframework.objexj.object.fields.ObjexRefList;
import org.talframework.objexj.object.fields.ObjexRefMap;
import org.talframework.objexj.object.fields.ObjexRefSet;
import org.talframework.objexj.object.fields.ProxyReference;

/**
 * This class is a base reader that aims to make it easier to implement
 * a reader that reads in a single object at a time - it does not support
 * reading in a tree of objects recursively. This class must be derived
 * from to answer the basic questions about the source data, including
 * whether the data for any property is held in the source and what its
 * value is. All logic otherwise is in this class.
 * 
 * <p>This class implements a number of optional behaviours that control
 * what properties we do attempt to set/change on the target. See the
 * {@link ObjectReaderBehaviour} for more details.</p>
 * 
 * @author Tom Spencer
 */
public abstract class BaseObjectReader implements ObjexStateReader {
    
    /** If true then only properties set on the input are actually read, requires bean to have isSet'Name' methods */
    private boolean merge = false;
    /** If true then owned references properties are considered */
    private boolean includeOwned = false;
    /** If true then non-owned reference properties are considered */
    private boolean includeReferences = false;
    
    /** The target object */
    private ObjexObj target;
    
    /**
     * Default constructor for setting the characteristics of the reader
     * separately.
     */
    protected BaseObjectReader() {
    }
    
    /**
     * Constructs and allows the caller to turn on various behaviours at
     * construction time.
     * 
     * @param behaviours The behaviours to turn on
     */
    protected BaseObjectReader(ObjectReaderBehaviour... behaviours) {
        setBehaviour(true, behaviours);
    }
    
    /**
     * Call to turn one or behaviours on
     * 
     * @param behaviours The behaviours to turn on
     */
    public void setBehaviourOn(ObjectReaderBehaviour... behaviours) {
        setBehaviour(true, behaviours);
    }
    
    /**
     * Call to turn one or behaviours off
     * 
     * @param behaviours The behaviours to turn off
     */
    public void setBehaviourOff(ObjectReaderBehaviour... behaviours) {
        setBehaviour(false, behaviours);
    }
    
    /**
     * Call to determine if a particular behaviour is on or off.
     * 
     * @param behaviour The behaviour to test
     * @return The behaviour
     */
    public boolean isBehaviourOn(ObjectReaderBehaviour behaviour) {
        boolean ret = false;
        
        switch( behaviour ) {
        case MERGE:
            ret = merge;
            break;
            
        case INCLUDE_OWNED:
            ret = includeOwned;
            break;
            
        case INCLUDE_REFERENCES:
            ret = includeReferences;
            break;
        }
        
        return ret;
    }
    
    /**
     * Internal helper to set a behaviour either on or off.
     * 
     * @param on If true the behaviour is turned on, otherwise off
     * @param behaviours The behaviour(s) to turn on or off
     */
    private void setBehaviour(boolean on, ObjectReaderBehaviour... behaviours) {
        for( ObjectReaderBehaviour behaviour : behaviours ) {
            switch( behaviour ) {
            case MERGE:
                merge = on;
                break;
                
            case INCLUDE_OWNED:
                includeOwned = on;
                break;
                
            case INCLUDE_REFERENCES:
                includeReferences = on;
                break;
            }
        }
    }
    
    /**
     * Call to begin the read of the target property.
     * 
     * @param target The object to read into
     */
    protected void read(ObjexObj target) {
        this.target = target;
        target.acceptReader(this);
        this.target = null;
    }
    
    /**
     * Reads the property from the state bean if it is compatible
     */
    @SuppressWarnings("unchecked")
    public <T> T read(String name, T current, Class<?> expected, ObjexFieldType type, boolean persistent) {
        if( !shouldSet(name) ) return current;
        if( !propertyExists(name, expected)) return current;
        
        T ret = null;
        Object val = getProperty(name, expected, type, persistent);
        
        // See if conversion is needed
        if( val != null && !expected.isInstance(val) ) {
            /* Auto-boxing */ if( expected.isPrimitive() && Number.class.isInstance(val) ) ret = (T)val;
            
            // TODO: Other conversions - could we use the PropertyEditor style here??
        }
        else {
            ret = (T)val;
        }
        
        return ret;
    }
    
    /**
     * Reads the property from the state bean if it is compatible
     */
    @SuppressWarnings("unchecked")
    public <T> T readReference(String name, T current, Class<?> expected, ObjexFieldType type, boolean persistent) {
        if( type == ObjexFieldType.REFERENCE && !includeReferences ) return current;
        if( type == ObjexFieldType.OWNED_REFERENCE && !includeOwned ) return current;
        if( !shouldSet(name) ) return current;
        if( !propertyExists(name, expected)) return current;
        
        Object val = getProperty(name, expected, type, persistent);
        ObjexID id = val instanceof ObjexObj ? ((ObjexObj)val).getId() : DefaultObjexID.getId(val);
        return (T)expected.cast(Proxy.newProxyInstance(
                    this.getClass().getClassLoader(), 
                    new Class<?>[]{ObjexObj.class, expected}, 
                    new ProxyReference(id, target.getContainer())));
    }
    
    /**
     * Reads the property from the state bean if it is compatible
     */
    @Override
    public <T> List<T> readReferenceList(String name, List<T> current, Class<?> expected, ObjexFieldType type, boolean persistent) {
        if( type == ObjexFieldType.REFERENCE && !includeReferences ) return current;
        if( type == ObjexFieldType.OWNED_REFERENCE && !includeOwned ) return current;
        if( !shouldSet(name) ) return current;
        
        // At this point, we should always return a non-null value!
        if( !propertyExists(name, expected)) {
            if( current != null ) return current;
            else if( type == ObjexFieldType.OWNED_REFERENCE ) return new ObjexChildList<T>(target);
            else return new ObjexRefList<T>(target);
        }
        
        Object val = getListProperty(name, expected, type, persistent);
        
        List<ObjexID> refs = new ArrayList<ObjexID>();
        if( val instanceof Collection<?> ) {
            for( Object obj : ((Collection<?>)val) ) {
                ObjexID ref = getRef(obj);
                if( ref != null && !ref.isNull() ) refs.add(ref);
            }
        }
        else if( val instanceof Object[] ) {
            for( Object obj : ((Object[])val) ) {
                ObjexID ref = getRef(obj);
                if( ref != null && !ref.isNull() ) refs.add(ref);
            }
        }
        else if( val instanceof String ) {
            splitRefs(val.toString(), refs);
        }
        else if( val != null ) {
            // TODO: Objex Exception?
            throw new IllegalArgumentException("Cannot process () as property, " + name + ", on target [" + target + "]. Not holding valid ref type");
        }
        
        // Form new reference list and return it
        return type == ObjexFieldType.OWNED_REFERENCE ? new ObjexChildList<T>(target, refs) : new ObjexRefList<T>(target, refs);
    }
    
    /**
     * Reads the property from the state bean if it is compatible
     */
    @Override
    public <T> Set<T> readReferenceSet(String name, Set<T> current, Class<?> expected, ObjexFieldType type, boolean persistent) {
        if( type == ObjexFieldType.REFERENCE && !includeReferences ) return current;
        if( type == ObjexFieldType.OWNED_REFERENCE && !includeOwned ) return current;
        if( !shouldSet(name) ) return current;
        
        // At this point, we should always return a non-null value!
        if( !propertyExists(name, expected)) {
            if( current != null ) return current;
            else if( type == ObjexFieldType.OWNED_REFERENCE ) return new ObjexChildSet<T>(target);
            else return new ObjexRefSet<T>(target);
        }
        
        Object val = getSetProperty(name, expected, type, persistent);
        
        Set<ObjexID> refs = new HashSet<ObjexID>();
        if( val instanceof Collection<?> ) {
            for( Object obj : ((Collection<?>)val) ) {
                ObjexID ref = getRef(obj);
                if( ref != null && !ref.isNull() ) refs.add(ref);
            }
        }
        else if( val instanceof Object[] ) {
            for( Object obj : ((Object[])val) ) {
                ObjexID ref = getRef(obj);
                if( ref != null && !ref.isNull() ) refs.add(ref);
            }
        }
        else if( val instanceof String ) {
            splitRefs(val.toString(), refs);
        }
        else if( val != null ) {
            // TODO: Objex Exception?
            throw new IllegalArgumentException("Cannot process () as property, " + name + ", on target [" + target + "]. Not holding valid ref type");
        }
        
        // Form new reference list and return it
        return type == ObjexFieldType.OWNED_REFERENCE ? new ObjexChildSet<T>(target, refs) : new ObjexRefSet<T>(target, refs);
    }
    
    @Override
    public <T> Map<String, T> readReferenceMap(String name, Map<String, T> current, Class<?> expected, ObjexFieldType type, boolean persistent) {
        if( type == ObjexFieldType.REFERENCE && !includeReferences ) return current;
        if( type == ObjexFieldType.OWNED_REFERENCE && !includeOwned ) return current;
        if( !shouldSet(name) ) return current;
        
        // At this point, we should always return a non-null value!
        if( !propertyExists(name, expected)) {
            if( current != null ) return current;
            else if( type == ObjexFieldType.OWNED_REFERENCE ) return new ObjexChildMap<String, T>(target);
            else return new ObjexRefMap<String, T>(target);
        }
        
        Object val = getMapProperty(name, expected, type, persistent);
        
        Map<String, ObjexID> refs = new HashMap<String, ObjexID>();
        if( val instanceof Map<?, ?> ) {
            Map<?, ?> map = (Map<?, ?>)val;
            for( Object key : map.keySet() ) {
                ObjexID ref = getRef(map.get(key));
                if( ref != null && !ref.isNull() ) refs.put(key.toString(), ref);
            }
        }
        else if( val instanceof String ) {
            splitRefs(name, val.toString(), refs);
        }
        else if( val != null ) {
            // TODO: Objex Exception?
            throw new IllegalArgumentException("Cannot process () as property, " + name + ", on target [" + target + "]. Not holding valid ref type");
        }
        
        // Form new reference list and return it
        return type == ObjexFieldType.OWNED_REFERENCE ? new ObjexChildMap<String, T>(target, refs) : new ObjexRefMap<String, T>(target, refs);
    }
    
    ////////////////////////////////////////////////
    // Heleprs
    
    /**
     * Helper to determine if property should be read at all
     * 
     * @param name The name of the property
     * @return True if property should be read (set on the target) false otherwise
     */
    protected abstract boolean shouldSet(String name);
    
    /**
     * Helper to determine if property exists in the source
     * 
     * @param name The name of the property
     * @param expected The expected type of the property (or of elements if a map/collection)
     * @return True if property should be read (set on the target) false otherwise
     */
    protected abstract boolean propertyExists(String name, Class<?> expected);
    
    /**
     * Called when we need to get a single scalar value from the
     * source of the data. This is only called after we have checked
     * we are interested in the property and after ensure it exists
     * via a call to propertyExists.
     * 
     * <p>Note that the expected parameter is the expected type of
     * the property on the target bean after conversion.
     * In the source object it might be held as a String, ObjexID,
     * ObjexObj instance or a type of class the container can determine
     * the ID of.</p>
     * 
     * @param name The name of the property
     * @param expected The expected type (of members) - see above
     * @param type The objex field type
     * @param persistent Whether it is a persistent field or not
     * @return The properties value from source
     */
    protected abstract Object getProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent);
    
    /**
     * Called when we need to get a list value from the
     * source of the data. This is only called after we have checked
     * we are interested in the property and after ensure it exists
     * via a call to propertyExists. 
     * 
     * <p>The list can be a collection of objects that are stringified 
     * references, ObjexIDs, actual ObjexObj or another object that
     * the container of the target object is able to deduce is a
     * reference. It could also be an Object array, Object[], of
     * the same or finally a tokenised string of stringified 
     * references with the delimitter of ',', ';' or ':' - a list
     * of one reference has no such delimitter.</p>
     * 
     * <p>Note that the expected parameter is the expected type of
     * the members in the property on the target bean after conversion.
     * In the source object it might be held as a String, ObjexID,
     * ObjexObj instance or a type of class the container can determine
     * the ID of.</p>
     * 
     * @param name The name of the property
     * @param expected The expected type (of members) - see above
     * @param type The objex field type
     * @param persistent Whether it is a persistent field or not
     * @return The properties value from source
     */
    protected abstract Object getListProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent);
    
    /**
     * Called when we need to get a set value from the
     * source of the data. This is only called after we have checked
     * we are interested in the property and after ensure it exists
     * via a call to propertyExists.
     * 
     * <p>The set can be a collection of objects that are stringified 
     * references, ObjexIDs, actual ObjexObj or another object that
     * the container of the target object is able to deduce is a
     * reference. It could also be an Object array, Object[], of
     * the same or finally a tokenised string of stringified 
     * references with the delimitter of ',', ';' or ':' - a list
     * of one reference has no such delimitter.</p>
     * 
     * <p>Note that the expected parameter is the expected type of
     * the members in the property on the target bean after conversion.
     * In the source object it might be held as a String, ObjexID,
     * ObjexObj instance or a type of class the container can determine
     * the ID of.</p>
     * 
     * @param name The name of the property
     * @param expected The expected type (of members) - see above
     * @param type The objex field type
     * @param persistent Whether it is a persistent field or not
     * @return The properties value from source
     */
    protected abstract Object getSetProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent);
    
    /**
     * Called when we need to get a map value from the
     * source of the data. This is only called after we have checked
     * we are interested in the property and after ensure it exists
     * via a call to propertyExists.
     * 
     * <p>The map can be a map of objects where the value is either 
     * the stringified references, ObjexIDs, actual ObjexObj or another 
     * object that the container of the target object is able to deduce 
     * is a reference. Or tt could also be a tokenised string of 
     * "key=stringified reference" values that are in turn split with
     * the delimitter of ',', ';' or ':' - a list of one map=ref entry
     * has no such delimitter.</p>
     * 
     * <p>Note that the expected parameter is the expected type of
     * the members in the property on the target bean after conversion.
     * In the source object it might be held as a String, ObjexID,
     * ObjexObj instance or a type of class the container can determine
     * the ID of.</p>
     * 
     * @param name The name of the property
     * @param expected The expected type (of members) - see above
     * @param type The objex field type
     * @param persistent Whether it is a persistent field or not
     * @return The properties value from source
     */
    protected abstract Object getMapProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent);
    
    /**
     * Helper that given an object can determine the id of the
     * object it refer's to.
     * 
     * @param obj The object to get ID for
     * @return The ID of that object
     */
    private ObjexID getRef(Object obj) {
        ObjexID ret = null;
        if( obj instanceof String ) ret = new DefaultObjexID(obj.toString());
        else if( obj instanceof ObjexID ) ret = (ObjexID)obj;
        else if( obj instanceof ObjexObj ) ret = ((ObjexObj)obj).getId();
        else target.getContainer().getIdOfObject(obj);
        
        return ret;
    }
    
    /**
     * Attempts to process a string either as a delimitted list of
     * references or a single ref and add it to the collection of
     * refs. The split characters looked at are ',', ';' and ':'.
     * 
     * @param source The source string
     * @param refs The collection to put each ref into
     */
    private void splitRefs(String source, Collection<ObjexID> refs) {
        String splitter = null;
        if( source.indexOf(',') > 0 ) splitter = ",";
        else if( source.indexOf(';') > 0 ) splitter = ";";
        else if( source.indexOf(':') > 0 ) splitter = ":";
        
        if( splitter != null ) {
            String[] tokens = source.split(splitter);
            for( String t : tokens ) {
                ObjexID ref = getRef(t);
                if( ref != null && !ref.isNull() ) refs.add(ref);
            }
        }
        else {
            ObjexID ref = getRef(source);
            if( ref != null && !ref.isNull() ) refs.add(ref);
        }
    }
    
    /**
     * Treats the string a one or more "key=ref" values, where
     * ref is the stringified form of an {@link ObjexID}. Multiple
     * references are split either by a ',', and ';' or a ':'. 
     * This does assume that the keys do not contain these either
     * of these symbols!!
     * 
     * @param name The name of the property so we can raise an exception
     * @param source The source string
     * @param refs The map to put each ref into
     */
    private void splitRefs(String name, String source, Map<String, ObjexID> refs) {
        String splitter = null;
        if( source.indexOf(',') > 0 ) splitter = ",";
        else if( source.indexOf(';') > 0 ) splitter = ";";
        else if( source.indexOf(':') > 0 ) splitter = ":";
        
        if( splitter != null ) {
            String[] tokens = source.split(splitter);
            for( String t : tokens ) {
                String[] entry = t.split("=");
                if( entry == null || entry.length != 2 ) throw new IllegalArgumentException("Cannot process () as map property, " + name + ", on target [" + target + "]. Not holding valid key=ref type");
                
                ObjexID ref = getRef(entry[0]);
                if( ref != null && !ref.isNull() ) refs.put(entry[0], ref);
            }
        }
        else {
            String[] entry = source.split("=");
            if( entry == null || entry.length != 2 ) throw new IllegalArgumentException("Cannot process () as map property, " + name + ", on target [" + target + "]. Not holding valid key=ref type");
            
            ObjexID ref = getRef(entry[0]);
            if( ref != null && !ref.isNull() ) refs.put(entry[0], ref);
        }
    }
    
    /**
     * @return the merge
     */
    public boolean isMerge() {
        return merge;
    }

    /**
     * @return the includeOwned
     */
    public boolean isIncludeOwned() {
        return includeOwned;
    }

    /**
     * @return the includeReferences
     */
    public boolean isIncludeReferences() {
        return includeReferences;
    }

    /**
     * @return the target
     */
    public ObjexObj getTarget() {
        return target;
    }

    /**
     * This enum holds all the behaviours we support in the single object
     * reader. Clients can turn these on by providing a list of 
     * capabilities to the constructor, or turning them on afterwards.
     *
     * @author Tom Spencer
     */
    public enum ObjectReaderBehaviour {
        /** If this behaviour is on then we merge values between target and source */
        MERGE,
        /** If this behaviour is on we consider owned properties, ignored otherwise */ 
        INCLUDE_OWNED,
        /** If this behaviour is on we consider reference properties, ignored otherwise */
        INCLUDE_REFERENCES
    }
}
