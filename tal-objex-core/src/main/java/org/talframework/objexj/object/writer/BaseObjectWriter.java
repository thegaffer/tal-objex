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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObj.ObjexFieldType;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.object.fields.ObjexRefList;
import org.talframework.objexj.object.fields.ObjexRefMap;
import org.talframework.objexj.object.fields.ObjexRefSet;
import org.talframework.objexj.object.writer.BaseObjectReader.ObjectReaderBehaviour;

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
public abstract class BaseObjectWriter implements ObjexStateWriter {
    
    /** If false then only persisted properties are output */
    private boolean includeNonPersisted = false;
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
    protected BaseObjectWriter() {
    }
    
    /**
     * Constructs and allows the caller to turn on various behaviours at
     * construction time.
     * 
     * @param behaviours The behaviours to turn on
     */
    protected BaseObjectWriter(ObjectWriterBehaviour... behaviours) {
        setBehaviour(true, behaviours);
    }
    
    /**
     * Call to turn one or behaviours on
     * 
     * @param behaviours The behaviours to turn on
     */
    public void setBehaviourOn(ObjectWriterBehaviour... behaviours) {
        setBehaviour(true, behaviours);
    }
    
    /**
     * Call to turn one or behaviours off
     * 
     * @param behaviours The behaviours to turn off
     */
    public void setBehaviourOff(ObjectWriterBehaviour... behaviours) {
        setBehaviour(false, behaviours);
    }
    
    /**
     * Call to determine if a particular behaviour is on or off.
     * 
     * @param behaviour The behaviour to test
     * @return The behaviour
     */
    public boolean isBehaviourOn(ObjectWriterBehaviour behaviour) {
        boolean ret = false;
        
        switch( behaviour ) {
        case INCLUDE_NONPERSISTED:
            ret = includeNonPersisted;
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
    private void setBehaviour(boolean on, ObjectWriterBehaviour... behaviours) {
        for( ObjectWriterBehaviour behaviour : behaviours ) {
            switch( behaviour ) {
            case INCLUDE_NONPERSISTED:
                includeNonPersisted = on;
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
     * Call to begin the writer of the target.
     * 
     * @param target The object to read into
     */
    protected void write(ObjexObj target) {
        this.target = target;
        target.acceptWriter(this, includeNonPersisted);
        this.target = null;
    }
    
    
    /**
     * After checking this property should be written given the 
     * behaviours set on this instance. Simple writes using derived
     * class.
     */
    
    @Override
    public void write(String name, Object obj, ObjexFieldType type, boolean persistent) {
        if( !shouldWrite(name, obj) ) return;
        
        writeProperty(name, obj, type, persistent);
    }
    
    /**
     * Converts the references into a stringified object id and
     * writes away subject to ensure the property should be 
     * written given the behaviours set on this instance.
     */
    @Override
    public void writeReference(String name, Object ref, ObjexFieldType type, boolean persistent) {
        if( !includeNonPersisted && !persistent ) return;
        if( type == ObjexFieldType.REFERENCE && !includeReferences ) return;
        if( type == ObjexFieldType.OWNED_REFERENCE && !includeOwned ) return;
        if( !shouldWrite(name, ref) ) return;
        
        ref = getRefAsString(ref);
        writeProperty(name, ref, type, persistent);
    }
    
    /**
     * Converts the references into a list pointing to stringified
     * object ids and writes away subject to ensure the property
     * should be written given the behaviours set on this instance.
     */
    @Override
    public void writeReferenceList(String name, List<?> refs, ObjexFieldType type, boolean persistent) {
        if( !includeNonPersisted && !persistent ) return;
        if( type == ObjexFieldType.REFERENCE && !includeReferences ) return;
        if( type == ObjexFieldType.OWNED_REFERENCE && !includeOwned ) return;
        if( !shouldWrite(name, refs) ) return;
        
        List<String> strRefs = null;
        if( refs instanceof ObjexRefList<?> ) {
            strRefs = ((ObjexRefList<?>)refs).toRefList();
        }
        else {
            strRefs = new ArrayList<String>();
            if( refs != null ) {
                for( Object ref : refs ) strRefs.add(getRefAsString(ref));
            }
        }
        
        writeListProperty(name, strRefs, type, persistent);
    }
    
    /**
     * Converts the references into a set pointing to stringified
     * object ids and writes away subject to ensure the property
     * should be written given the behaviours set on this instance.
     */
    @Override
    public void writeReferenceSet(String name, Set<?> refs, ObjexFieldType type, boolean persistent) {
        if( !includeNonPersisted && !persistent ) return;
        if( type == ObjexFieldType.REFERENCE && !includeReferences ) return;
        if( type == ObjexFieldType.OWNED_REFERENCE && !includeOwned ) return;
        if( !shouldWrite(name, refs) ) return;
        
        Set<String> strRefs = null;
        if( refs instanceof ObjexRefSet<?> ) {
            strRefs = ((ObjexRefSet<?>)refs).toRefSet();
        }
        else {
            strRefs = new HashSet<String>();
            if( refs != null ) {
                for( Object ref : refs ) strRefs.add(getRefAsString(ref));
            }
        }
        
        writeSetProperty(name, strRefs, type, persistent);
    }
    
    /**
     * Converts the references into a map pointing to stringified
     * object ids and writes away subject to ensure the property
     * should be written given the behaviours set on this instance.
     */
    @Override
    public void writeReferenceMap(String name, Map<String, ?> refs, ObjexFieldType type, boolean persistent) {
        if( !includeNonPersisted && !persistent ) return;
        if( type == ObjexFieldType.REFERENCE && !includeReferences ) return;
        if( type == ObjexFieldType.OWNED_REFERENCE && !includeOwned ) return;
        if( !shouldWrite(name, refs) ) return;
        
        Map<String, String> strRefs = null;
        if( refs instanceof ObjexRefMap<?, ?> ) {
            strRefs = ((ObjexRefMap<?, ?>)refs).toRefMap();
        }
        else {
            strRefs = new HashMap<String, String>();
            if( refs != null ) {
                for( Object key : refs.keySet() ) strRefs.put(key.toString(), getRefAsString(refs.get(key)));
            }
        }
        
        writeMapProperty(name, strRefs, type, persistent);
    }
    
    
    ////////////////////////////////////////////////
    // Abstract Callbacks
    
    /**
     * Helper to determine if property should be written at all
     * 
     * @param name The name of the property
     * @param val The current (raw) value of the property
     * @return True if property should be read (set on the target) false otherwise
     */
    protected abstract boolean shouldWrite(String name, Object val);
    
    /**
     * Called to write a single scaler value into whatever we are writing
     * into. The property has already been checked to ensure we should be
     * writing it away.
     * 
     * @param name The name of the property
     * @param val The value to write
     * @param type The objex field type
     * @param persistent Whether it is a persistent field or not
     */
    protected abstract void writeProperty(String name, Object val, ObjexFieldType type, boolean persistent);
    
    /**
     * Called to write a list value into whatever we are writing
     * into. The property has already been checked to ensure we should be
     * writing it away.
     * 
     * @param name The name of the property
     * @param val The value to write as a list of String (object ref) values
     * @param type The objex field type
     * @param persistent Whether it is a persistent field or not
     */
    protected abstract void writeListProperty(String name, List<String> refs, ObjexFieldType type, boolean persistent);
    
    /**
     * Called to write a set value into whatever we are writing
     * into. The property has already been checked to ensure we should be
     * writing it away.
     * 
     * @param name The name of the property
     * @param val The value to write as a set of String (object ref) values
     * @param type The objex field type
     * @param persistent Whether it is a persistent field or not
     */
    protected abstract void writeSetProperty(String name, Set<String> refs, ObjexFieldType type, boolean persistent);
    
    /**
     * Called to write a map value into whatever we are writing
     * into. The property has already been checked to ensure we should be
     * writing it away.
     * 
     * @param name The name of the property
     * @param val The value to write as a map of String to String (object refs) values
     * @param type The objex field type
     * @param persistent Whether it is a persistent field or not
     */
    protected abstract void writeMapProperty(String name, Map<String, String> refs, ObjexFieldType type, boolean persistent);
    
    /**
     * This method converts a reference property value (or in the
     * case of a collection or map an element in that collection
     * or map) into a stringified reference to an object.
     * 
     * @param ref The reference as held by the source object
     * @return The value
     */
    protected String getRefAsString(Object ref) {
        String ret = null;
        
        if( ref == null ) ret = null;
        else if( ref instanceof ObjexObj ) ret = ((ObjexObj)ref).getId().toString();
        else if( ref instanceof ObjexID ) ret = ref.toString();
        // TPS: A real object should not be doing this!! else if( ref instanceof String) ret = ref.toString();
        else throw new IllegalArgumentException("Attempt to save an ObjexObj reference property that is not an ObjexObj instance [" + target + "] or a reference: " + ref);
        
        return ret;
    }
    
    ///////////////////////////////////////////////
    // Accessors/Mutators
    
    /**
     * @return the includeNonPersisted setting
     */
    public boolean isIncludeNonPersisted() {
        return includeNonPersisted;
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
    public enum ObjectWriterBehaviour {
        /** If this behaviour is on then we also write out non-persisted values */
        INCLUDE_NONPERSISTED,
        /** If this behaviour is on we consider owned properties, ignored otherwise */ 
        INCLUDE_OWNED,
        /** If this behaviour is on we consider reference properties, ignored otherwise */
        INCLUDE_REFERENCES
    }
}
