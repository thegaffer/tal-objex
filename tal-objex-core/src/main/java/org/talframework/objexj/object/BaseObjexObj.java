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
package org.talframework.objexj.object;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.ValidationError;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.container.ObjectStrategy.PropertyCharacteristic;
import org.talframework.objexj.container.ObjectStrategy.PropertyStrategy;
import org.talframework.objexj.container.ObjectStrategy.PropertyTypeEnum;
import org.talframework.objexj.exceptions.ObjectErrorException;
import org.talframework.objexj.exceptions.ObjectFieldInvalidException;
import org.talframework.objexj.exceptions.ObjectInvalidException;
import org.talframework.objexj.validation.CurrentValidationRequest;
import org.talframework.objexj.validation.SimpleValidationRequest;
import org.talframework.objexj.validation.groups.ChildGroup;
import org.talframework.objexj.validation.groups.FieldChangeGroup;
import org.talframework.objexj.validation.groups.FieldGroup;
import org.talframework.objexj.validation.groups.InterObjectEnrichmentGroup;
import org.talframework.objexj.validation.groups.InterObjectGroup;
import org.talframework.objexj.validation.groups.IntraObjectEnrichmentGroup;
import org.talframework.objexj.validation.groups.IntraObjectGroup;
import org.talframework.util.beans.BeanDefinition;
import org.talframework.util.beans.definition.BeanDefinitionsSingleton;

/**
 * Base class for an ObjexObj. Most basic methods are implemented
 * but all can be overridden as required.
 * 
 * <p>Note: Because this class is the intended base class for 
 * generated objex obj classes it is important there are no
 * field intialisers on this class and it has a default 
 * constructor.</p>
 * 
 * @author Tom Spencer
 */
public abstract class BaseObjexObj implements InternalObjexObj {

    /** The container that the object belongs to */
    private InternalContainer container;
    /** ID of the object */
	private ObjexID id;
	/** ID of this object parent (or null if this is the root object) */
	private ObjexID parentId;
	
	/** Cached parent object */
	private ObjexObj parent;
	
	/**
	 * The main initialisation called by the framework code to initialise
	 * an ObjexObj. Failure to do so will result in a number of errors.
	 * 
	 * @param container The container
	 * @param id The ID of the object
	 * @param parentId The parentID of the object
	 */
	public void init(InternalContainer container, ObjexID id, ObjexID parentId) {
	    if( container == null ) throw new IllegalArgumentException("Cannot create object without a container");
        if( id == null || id.isNull() ) throw new IllegalArgumentException("Cannot create object without a valid id");
        
	    this.container = container;
	    this.id = id;
	    this.parentId = parentId;
	}
	
	/**
	 * Called to get the object that represents the real business
	 * object which holds the properties.
	 * 
	 * <p>The default implementation simply returns this and so
	 * gets all the properties etc from itself.</p> 
	 * 
	 * @return The state bean
	 */
	protected Object getStateBean() {
	    return this;
	}
	
	/**
	 * This is called during the base classes implementation of the
	 * acceptReader/Writer and setProperty methods. These methods are
	 * implemented against the state bean using the Object Strategy
	 * for guidance. There may be better ways to implement these methods,
	 * particularly when the business object derives from this class
	 * directly. Otherwise, simply return the ObjectStrategy object
	 * to get this behaviour.
	 * 
	 * @return The object strategy for the object if known
	 */
	protected ObjectStrategy getStrategy() {
	    return null;
	}
	
	////////////////////////////////////////////
    // ObjexObj Methods
    
	/**
	 * Simply returns the ID
	 */
	@XmlAttribute
    @XmlJavaTypeAdapter(value=DefaultObjexID.XmlObjexIDAdaptor.class)
	@XmlID
	public ObjexID getId() {
	    checkInitialised();
		return id;
	}
	
	/**
	 * Default is simple name of this class (the most derived class).
	 * Typically overridden in derived classes.
	 */
	public String getType() {
	    checkInitialised();
		return this.getClass().getSimpleName();
	}
	
	/**
	 * Simply returns the parent ID
	 */
	public ObjexID getParentId() {
	    checkInitialised();
		return parentId;
	}
	
	/**
	 * Exposed as a protected method to allow derived class to implement the
	 * acceptReader method and read this in.
	 * 
	 * @param parentId The parent ID to set to
	 */
	protected void setParentId(ObjexID parentId) {
	    this.parentId = parentId;
	    this.parent = null;
	}
	
	/**
	 * Gets the parent from the container and caches
	 * it so it does not do this again.
	 */
	public ObjexObj getParent() {
	    checkInitialised();
		if( parentId == null || parentId.isNull() ) return null;
		
		if( parent == null ) {
			synchronized (this) {
				parent = container.getObject(parentId);
			}
		}
		
		return parent;
	}
	
	/**
	 * Either returns self if we have no parent or asks
	 * parent for the root
	 */
	public ObjexObj getRoot() {
	    checkInitialised();
		if( parentId == null || parentId.isNull() ) return this;
		else return getParent().getRoot();
	}
	
	/**
	 * Simply returns the container
	 */
	public Container getContainer() {
	    checkInitialised();
		return container;
	}
	
	/**
	 * Helper to allow derived to get at internal container.
	 * 
	 * @return The container
	 */
	public InternalContainer getInternalContainer() {
	    checkInitialised();
	    return container;
	}
	
	/**
	 * The default method tests whether the instance supports
	 * this interface.
	 */
	public <T> T getBehaviour(Class<T> behaviour) {
	    checkInitialised();
		if( behaviour.isInstance(this) ) return behaviour.cast(this);
		else throw new ClassCastException("The behaviour is not supported by this object");
	}
	
	/**
	 * Simply checks if we are in an Editable Container
	 */
	public boolean isUpdateable() {
	    checkInitialised();
	    return container.isOpen();
	}
	
	/**
	 * Base version tries to find a property on this
	 * class (derived) and uses it to get the value.
	 */
	public Object getProperty(String name) {
	    checkInitialised();
	    
	    Object ret = null;
	    
	    // See if property on the state bean
	    Object state = getStateBean();
	    BeanDefinition def = BeanDefinitionsSingleton.getInstance().getDefinition(state.getClass());
        if( def.hasProperty(name) ) {
            if( def.canRead(name) ) ret = def.read(state, name);
            else throw new ObjectFieldInvalidException(name, "Field not readable");
        }
        
        // Test for common attributes
        else {
            if( "id".equals(name) ) ret = getId();
            else if( "parent".equals(name) ) ret = getParent();
            else if( "parentId".equals(name) ) ret = getParentId();
            else if( "type".equals(name) ) ret = getType();
            else if( "container".equals(name) ) ret = getContainer();
            else throw new ObjectFieldInvalidException(name, "Unknown field");
        }
        
        return ret;
	}
	
	/**
	 * Uses the raw getProperty method and then casts as 
	 * appropriate.
	 */
	public <T> T getProperty(String name, Class<T> expected) {
	    Object val = getProperty(name);
	    return expected.cast(val);
	}
	
	/**
     * Uses the name as a property and reflects value
     * turning it into a String if non-null
     */
    public String getPropertyAsString(String name) {
        Object obj = getProperty(name);
        return obj != null ? obj.toString() : null;
    }
    
    /**
	 * Delegates handling to private setProperty method
	 * if one exists.
	 * 
	 * @throws ObjectFieldInvalidException If cannot set this property
	 */
	public void setProperty(String name, Object val) {
	    checkUpdateable();
	    
	    Object state = getStateBean();
	    ObjectStrategy strategy = getStrategy();
	    if( strategy == null ) throw new UnsupportedOperationException("Generic setProperty not supported unless derived class provides a strategy: " + this.getClass());
	    
	    BeanDefinition def = BeanDefinitionsSingleton.getInstance().getDefinition(state.getClass());
        if( !def.hasProperty(name) ) throw new ObjectFieldInvalidException(name, "field does not exist");
        if( !def.canWrite(name) ) throw new ObjectFieldInvalidException(name, "No write method available");
        if( !def.getPropertyType(name).isAssignableFrom(val.getClass()) ) throw new ObjectFieldInvalidException(name, "Incompatible types: " + val);
        
        PropertyStrategy prop = strategy.getProperty(name);
        
        // If owned reference, ensure seed is un-initialised ObjexObj (or not an ObjexObj) (this is done in collections)
        if( prop != null && 
                prop.isObjexType(ObjexFieldType.OWNED_REFERENCE) && 
                prop.isType(PropertyTypeEnum.OBJECT) ) {
            if( val instanceof ObjexObj && ((ObjexObj)val).getContainer() != null ) {
                throw new ObjectInvalidException("Cannot add initialised ObjexObj as child");
            }
            
            val = getInternalContainer().createObject(this, val);
        }
        
        // If un-owned reference, ensure seed is an ObjexObj (this is done in collections)
        else if( prop != null && 
                prop.isObjexType(ObjexFieldType.REFERENCE) && 
                prop.isType(PropertyTypeEnum.OBJECT) ) {
            if( !(val instanceof ObjexObj) ) throw new ObjectInvalidException("Adding non-ObjexObj as a reference");
        }
        
        def.write(state, name, val);
    }
	
	/**
     * Standard validation implementation
     */
    public void validate(ValidationRequest request) {
        Validator validator = request.getValidator();
        
        Set<ConstraintViolation<BaseObjexObj>> violations = null;
        
        switch(request.getValidationType()) {
        case INTRA_OBJECT:
            validator.validate(this, IntraObjectEnrichmentGroup.class);
            violations = validator.validate(this, IntraObjectGroup.class, FieldGroup.class, Default.class);
            break;
            
        case INTER_OBJECT:
            validator.validate(this, InterObjectEnrichmentGroup.class);
            violations = validator.validate(this, InterObjectGroup.class);
            break;
            
        case CHILDREN:
            violations = validator.validate(this, ChildGroup.class);
            break;
        }
        
        // Turn violations into errors
        if( violations != null ) {
            Iterator<ConstraintViolation<BaseObjexObj>> it = violations.iterator();
            while( it.hasNext() ) {
                ConstraintViolation<BaseObjexObj> violation = it.next();
                
                // TODO: Need to test no prop path = non-field error
                String prop = violation.getPropertyPath().toString();
                if( prop != null && prop.startsWith("stateBean.") ) prop = prop.substring(5);
                else if( prop != null && prop.equals("stateBean") ) prop = null;
                else if( prop != null && prop.length() == 0 ) prop = null;
                
                Object[] params = violation.getInvalidValue() != null ? new Object[]{violation.getInvalidValue()} : null;
                
                ValidationError error = new SimpleValidationRequest.SimpleValidationError(id, violation.getMessageTemplate(), prop, params);
                request.addError(error);
            }
        }
    }
    
    /**
     * This is a helper method that can be called to validate a value before
     * it is set.
     * 
     * @param state The state bean of the object
     * @param propertyName The name of the property
     * @param val The value
     * @return True if all is ok with the value
     */
    protected boolean validateValue(Object state, String propertyName, Object val) {
        ValidationRequest request = CurrentValidationRequest.getCurrent();
        
        Validator validator = null;
        if( request != null ) request.getValidator();
        else validator = Validation.buildDefaultValidatorFactory().getValidator();
        
        Set<?> violations = validator.validateValue(state.getClass(), propertyName, val, FieldChangeGroup.class, FieldGroup.class, Default.class);
        
        boolean ret = true;
        if( violations != null ) {
            Iterator<?> it = violations.iterator();
            while( it.hasNext() ) {
                Object v = it.next();
                if( !(v instanceof ConstraintViolation<?>) ) continue;
                
                ConstraintViolation<?> violation = (ConstraintViolation<?>)v;
                
                // TODO: Need to test no prop path = non-field error
                String prop = violation.getPropertyPath().toString();
                if( prop != null && prop.length() == 0 ) prop = null;
                
                Object[] params = violation.getInvalidValue() != null ? new Object[]{violation.getInvalidValue()} : null;
                
                // Add to errors
                if( request != null ) {
                    ValidationError error = new SimpleValidationRequest.SimpleValidationError(id, violation.getMessageTemplate(), prop, params);
                    request.addError(error);
                    ret = false;
                }
                
                // Otherwise fail
                else {
                    throw new ObjectErrorException(violation.getMessageTemplate(), params);
                }
            }
        }
        
        return ret;
    }
    
    /**
     * Sets every value of an property of the realObject that is known to
     * also be an ObjexObj property from the strategy object.
     */
    @SuppressWarnings("unchecked")
    public void acceptReader(ObjexStateReader reader) {
        setParentId(reader.read("parentId", parentId, ObjexID.class, ObjexFieldType.PARENT_ID, true));
        
        Object state = getStateBean();
        ObjectStrategy strategy = getStrategy();
        if( strategy == null ) throw new UnsupportedOperationException("Reading/writing not supported unless derived class provides a strategy: " + this.getClass());
        
        BeanDefinition def = BeanDefinitionsSingleton.getInstance().getDefinition(state.getClass());
        for( String prop : def.getProperties() ) {
            PropertyStrategy propStrategy = strategy.getProperty(prop);
            if( propStrategy == null ) continue;
            if( !def.canWrite(prop) ) continue;
            
            Object val = def.canRead(prop) ? def.read(state, prop) : null;
            boolean persistent = propStrategy.isCharacteristic(PropertyCharacteristic.PERSISTENT);
            
            ObjexFieldType type = propStrategy.getObjexType();
            switch(type) {
            case STRING:
            case MEMO:
            case NUMBER:
            case BOOL:
            case DATE:
            case SHORT_BLOB:
            case BLOB:
            case BLOB_REFERENCE:
            case USER:
            case OBJECT:
                def.write(state, prop, reader.read(prop, val, def.getPropertyType(prop), type, persistent));
                break;
                
            case REFERENCE:
            case OWNED_REFERENCE:
                if( propStrategy.isType(PropertyTypeEnum.LIST) ) def.write(state, prop, reader.readReferenceList(prop, (List<?>)val, propStrategy.getElementType(), type, persistent));
                else if( propStrategy.isType(PropertyTypeEnum.SET) ) def.write(state, prop, reader.readReferenceSet(prop, (Set<?>)val, propStrategy.getElementType(), type, persistent));
                else if( propStrategy.isType(PropertyTypeEnum.MAP) ) def.write(state, prop, reader.readReferenceMap(prop, (Map<String, ?>)val, propStrategy.getElementType(), type, persistent));
                else def.write(state, prop, reader.readReference(prop, val, propStrategy.getElementType(), type, persistent));
                break;
            }
        }
    }
    
    /**
     * Writes out every property of the real object if that is known to be
     * a ObjexObj property from the strategy object.
     */
    @SuppressWarnings("unchecked")
    public void acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
        Object state = getStateBean();
        ObjectStrategy strategy = getStrategy();
        if( strategy == null ) throw new UnsupportedOperationException("Reading/writing not supported unless derived class provides a strategy: " + this.getClass());
        
        BeanDefinition def = BeanDefinitionsSingleton.getInstance().getDefinition(state.getClass());
        for( String prop : def.getProperties() ) {
            PropertyStrategy propStrategy = strategy.getProperty(prop);
            if( propStrategy == null ) continue;
            if( !def.canRead(prop) ) continue;
            
            boolean persistent = propStrategy.isCharacteristic(PropertyCharacteristic.PERSISTENT);
            if( !persistent && !includeNonPersistent ) continue;
            
            Object val = def.read(state, prop);
            
            ObjexFieldType type = propStrategy.getObjexType();
            switch(type) {
            case STRING:
            case MEMO:
            case NUMBER:
            case BOOL:
            case DATE:
            case SHORT_BLOB:
            case BLOB:
            case BLOB_REFERENCE:
            case USER:
            case OBJECT:
                writer.write(prop, val, type, persistent);
                break;
                
            case REFERENCE:
            case OWNED_REFERENCE:
                if( propStrategy.isType(PropertyTypeEnum.LIST) ) writer.writeReferenceList(prop, (List<?>)val, type, persistent);
                else if( propStrategy.isType(PropertyTypeEnum.SET) ) writer.writeReferenceSet(prop, (Set<?>)val, type, persistent);
                else if( propStrategy.isType(PropertyTypeEnum.MAP) ) writer.writeReferenceMap(prop, (Map<String, ?>)val, type, persistent);
                else writer.writeReference(prop, val, type, persistent);
                break;
            }
        }
    }

    //////////////////////////////////////////////////
	// Internal
	
    /**
     * Helper method to ensure this object is initialised as
     * an ObjexObj - if not throws an {@link IllegalStateException}.
     * 
     * @throws IllegalStateException If not initialised
     */
	protected void checkInitialised() {
        if( container == null ) throw new IllegalStateException("The object has not been initialised: " + this); 
    }
	
	/**
     * Ensures the object is updateable before continuing
     * 
     * @throws IllegalStateException If not editable
     */
    protected void checkUpdateable() {
        checkInitialised();
        if( !isUpdateable() ) throw new IllegalStateException("Cannot update an object that is not inside a transaction: " + this);
    }
}
