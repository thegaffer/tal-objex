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

package org.talframework.objexj.object;

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
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.exceptions.ObjectFieldInvalidException;

/**
 * Although ObjexObj objects should be behaviourally rich
 * and fully protect the domain, there are going to be
 * simple objects and simple containers. SimpleObjexObj
 * has been created to allow the creation of an ObjexObj
 * that the rest of the Objex plumbing will accept, but
 * which needs no further coding.
 * 
 * <p>To use a SimpleObjexObj it requires two things.
 * Firstly a {@link ObjexObjStateBean} derived class that
 * holds the state and represents the persisted form. And
 * secondly an extended object strategy that describes
 * the reference properties (properties that are 
 * references to other objects) so we can deal with
 * those appropriately. If this class is used there are 
 * certain features you need to be aware of:</p>
 * 
 * <ul>
 * <li>Firstly the class will not expose natural getter/
 * setter methods for the properties, so will not be
 * useful where reflection is used</li> 
 * <li>Secondly the class make use of reflection over
 * the state bean. Although reasonably efficient in
 * terms of caching the introspection calls this will
 * still not be the same as native access.
 * </ul>
 * 
 * FUTURE: Allow implementation of simple interfaces via use of a proxy
 * 
 * @author Tom Spencer
 */
public final class SimpleObjexObj extends BaseObjexObj {

	/** The strategy for this object */
	private final ObjectStrategy strategy;
	
	/** Member holds the detail or state object */
	private ObjexObjStateBean state;
	
	public SimpleObjexObj(ObjectStrategy strategy, ObjexObjStateBean state) {
	    if( strategy == null ) throw new IllegalArgumentException("Cannot create object without a strategy");
		if( state == null ) throw new IllegalArgumentException("Cannot create object without a state object");
		
		this.strategy = strategy;
		this.state = state;
	}
	
	/**
	 * Default is simple name of outer class
	 */
	public String getType() {
		return strategy.getTypeName();
	}
	
	/**
	 * Overridden based on whether state object is
	 * updateable or not.
	 */
	@Override
	public boolean isUpdateable() {
	    if( state.isEditable() ) return true;
	    return super.isUpdateable();
	}
	
	/**
	 * Returns a cloned copy of the state object
	 * 
	 * @deprecated
	 */
	public Object getStateObject() {
		return state.cloneState();
	}
	
	/**
	 * Helper that returns the state object without 
	 * cloning it. Can be used by the derived class.
	 * 
	 * @return The state object
	 * @deprecated
	 */
	protected ObjexObjStateBean getState() {
		return state;
	}
	
	/**
	 * Helper to return an editable version of the
	 * state object. This is useful if holding the
	 * object as part of a behavioural implementation.
	 * Using it does mean that the object will be
	 * set as edited even through nothing may have
	 * changed. 
	 * 
	 * @return The state object in editable form
	 */
	public ObjexObjStateBean getEditableLocalState() {
	    if( !state.isEditable() ) getInternalContainer().addObjectToTransaction(this, state);
	    return state;
	}
	
	
	/**
     * Uses the name as a property and reflects value from
     * state bean.
     */
	@SuppressWarnings("unchecked")
    @Override
    public Object getProperty(String name) {
        checkInitialised();
        
        String realName = convertReferencePropertyName(name);
        
        BeanWrapper wrapper = new BeanWrapperImpl(getState());
        Object ret = wrapper.getPropertyValue(realName);
        
        // See if it is a reference prop and convert
        if( name.equals(realName) && isReferenceProperty(realName) ) {
            if( ret instanceof Map<?, ?> ) ret = getContainer().getObjectMap((Map<Object, Object>)ret);
            else if( ret instanceof List<?> ) ret = getContainer().getObjectList((List<Object>)ret);
            else ret = getContainer().getObject(ret);
        }
        
        // Attempt to Clone value
        ret = cloneValue(ret);
        
        return ret;
    }
    
    /**
     * Delegates handling to private setProperty method.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setProperty(String name, Object val) {
        checkUpdateable();
        
        BeanWrapper wrapper = new BeanWrapperImpl(state);
        
        val = handleReferenceProperty(wrapper, name, val);
        name = convertReferencePropertyName(name);
        
        // Basic checks
        if( !wrapper.getPropertyType(name).isAssignableFrom(val.getClass()) ) {
            throw new IllegalArgumentException("Cannot set property because type [" + wrapper.getPropertyType(name) + "] not compatible with: " + val);
        }
        if( !wrapper.isWritableProperty(name) ) {
            throw new IllegalArgumentException("Cannot set property because it is not updateable: " + name);
        }
        
        Object current = wrapper.getPropertyValue(name);
        if( (current != null && current.equals(val)) || current == val ) return;
        
        // FUTURE: Wrap in a canSet/onSet provided by Strategy 
        if( !state.isEditable() ) getInternalContainer().addObjectToTransaction(this, state);
        wrapper.setPropertyValue(name, val);
    }
    
    /**
     * Overridden from base to check the property is a
     * reference property and further that on the state
     * bean it is held as reference or a list.
     * 
     * <p>Currently creating a new object on a reference
     * field is not supported.</p>
     */
    @SuppressWarnings("unchecked")
    @Override
    public ObjexObj createReference(String name, String type) {
        checkInitialised();
        
        Map<String, Class<?>> refProps = strategy.getOwnedReferenceProperties();
        
        ObjexObj newObj = null;
        if( refProps != null && refProps.containsKey(name) ) {
            BeanWrapper wrapper = new BeanWrapperImpl(getState());
            Class<?> refType = wrapper.getPropertyType(name);
            
            // Maps currently unsupported
            if( Map.class.isAssignableFrom(refType) ) {
                throw new ObjectFieldInvalidException(name, "Cannot create new object inside map reference currently");
            }
            
            // Create new object and add to reference prop
            else if( List.class.isAssignableFrom(refType) ) {
                newObj = getInternalContainer().newObject(this, state, type);
                Object id = newObj.getId();
                if( refProps.get(name).equals(String.class) ) id = id.toString();
                
                List<Object> currentRefs = getProperty(name + "Ref", List.class);
                if( currentRefs == null ) currentRefs = new ArrayList<Object>();
                currentRefs.add(id);
                setProperty(name + "Ref", currentRefs);
            }
            
            // Create reference and set in property
            else {
                // FUTURE: Should really remove current if owned (also on set)!
                
                newObj = getInternalContainer().newObject(this, state, type);
                setProperty(name, newObj);
            }
        }
        else {
            throw new ObjectFieldInvalidException(name, "Cannot create new object on a non-reference or non-owned property");
        }
        
        return newObj;
    }
	
	public void validate(ValidationRequest request) {
	    // FUTURE: Can we support validation on the strategy object?
	}
	
	/////////////////////////////////////////////
	// Internal
	
	/**
	 * Helper to determine if given property is a 
	 * reference property or not.
	 */
	protected boolean isReferenceProperty(String name) {
	    Map<String, Class<?>> refProps = strategy.getReferenceProperties();
	    return refProps != null && refProps.containsKey(name);
	}
	
	/**
	 * This helper method is called when setting a property
	 * to convert the incoming value for reference properties.
	 * If the property is a reference prop then this incoming
	 * value should be either an ObjexObj, a List of ObjexObj
	 * or a Map of Object to ObjexObj instances as appropriate.
	 * This method converts it to a reference (scalar, list or
	 * map as needed). If the property is not a reference prop
	 * then the value is simply ignored.
	 */
	protected Object handleReferenceProperty(BeanWrapper wrapper, String name, Object val) {
	    Object ret = val;
	    
	    if( isReferenceProperty(name) ) {
	        Class<?> referenceType = strategy.getReferenceProperties().get(name);
	        boolean storeAsString = referenceType.equals(String.class);
	        Class<?> storageType = wrapper.getPropertyType(name);
	        
	        ret = null;
	        
	        // Map
	        if( storageType.isAssignableFrom(Map.class) ) {
	            if( !Map.class.isInstance(val) ) throw new IllegalArgumentException("Trying to set map property [" + name + "] with non-map value"); 
	            
	            Map<?, ?> objects = (Map<?, ?>)val;
	            if( objects != null && objects.size() > 0 ) {
	                Map<Object, Object> refs = new HashMap<Object, Object>(objects.size());
	                Iterator<?> it = objects.keySet().iterator();
	                while( it.hasNext() ) {
	                    Object k = it.next();
	                    Object obj = objects.get(k);
	                    if( !(obj instanceof ObjexObj) ) throw new IllegalArgumentException("Trying to set map reference [" + name + "] with at least 1 non-ObjexObj entry: " + k + "=" + obj);
	                    ObjexID id = ((ObjexObj)obj).getId();
	                    refs.put(k, storeAsString ? id.toString() : id);
	                }
	                
	                ret = refs;
	            }
	        }
	        
	        // List
	        else if( storageType.isAssignableFrom(List.class) ) {
	            if( !List.class.isInstance(val) ) throw new IllegalArgumentException("Trying to set list property [" + name + "] with non-list value");
	            
	            List<?> objects = (List<?>)val;
	            if( objects != null && objects.size() > 0 ) {
	                List<Object> refs = new ArrayList<Object>(objects.size());
	                Iterator<?> it = objects.iterator();
	                while( it.hasNext() ) {
	                    Object obj = it.next();
	                    if( !(obj instanceof ObjexObj) ) throw new IllegalArgumentException("Trying to set list reference [" + name + "] with at least 1 non-ObjexObj entry: " + obj);
                        ObjexID id = ((ObjexObj)obj).getId();
                        refs.add(storeAsString ? id.toString() : id);
	                }
	                
	                ret = refs;
	            }
	        }
	        
	        // Simple ref
	        else if( val != null ) {
	            if( !(val instanceof ObjexObj) ) throw new IllegalArgumentException("Trying to set ref property [" + name + "] with non-ObjexObj value");
	            ObjexID id = ((ObjexObj)val).getId();
	            ret = storeAsString ? id.toString() : id;
	        }
	    }
	    
	    return ret;
	}
	
	/**
	 * This method converts the name of the requested property
	 * when the request prop is a reference property suffixed 
	 * with Ref. This means the client wants just the raw 
	 * reference value (scalar, list or map).
	 * 
	 * @param name The original name of the property 
	 * @return The real property name
	 */
	protected String convertReferencePropertyName(String name) {
	    String ret = name;
	    
	    Map<String, Class<?>> refProps = strategy.getReferenceProperties();
        if( refProps != null && (name.endsWith("Ref") && name.length() > 3) ) {
            String possibleRef = name.substring(0, name.length() - 3);
            if( refProps.containsKey(possibleRef) ) {
                ret = possibleRef;
            }
        }
	    
	    return ret;
	}
}
