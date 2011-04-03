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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.groups.Default;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.ValidationError;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.exceptions.ObjectFieldInvalidException;
import org.talframework.objexj.validation.SimpleValidationRequest;
import org.talframework.objexj.validation.groups.ChildGroup;
import org.talframework.objexj.validation.groups.FieldGroup;
import org.talframework.objexj.validation.groups.InterObjectEnrichmentGroup;
import org.talframework.objexj.validation.groups.InterObjectGroup;
import org.talframework.objexj.validation.groups.IntraObjectEnrichmentGroup;
import org.talframework.objexj.validation.groups.IntraObjectGroup;

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
	 * Called to get the state bean. This is mainly present so
	 * we can mark the state bean for validation.
	 * 
	 * @return
	 */
	@Valid
	protected abstract ObjexObjStateBean getStateBean();
	
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
	 * Default is simple name of outer class
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
	 * Passes the writer to the state bean. The derived class should
	 * override if it has non-persistent fields.
	 */
	public void acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
	    getStateBean().acceptWriter(writer, includeNonPersistent);
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
	    
	    try {
	        PropertyDescriptor prop = getPropertyDescriptor(name);
	        if( prop.getReadMethod() == null ) throw new ObjectFieldInvalidException(name, "No read method available");
	        return prop.getReadMethod().invoke(this, (Object[])null);
	    }
	    catch( RuntimeException e ) {
	        throw e;
	    }
	    catch( Exception e ) {
	        throw new ObjectFieldInvalidException(name, e.getMessage(), e);
	    }
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
	    
	    try {
            PropertyDescriptor prop = getPropertyDescriptor(name);
            if( prop.getWriteMethod() == null ) throw new ObjectFieldInvalidException(name, "No write method available");
            prop.getWriteMethod().invoke(this, val);
        }
        catch( RuntimeException e ) {
            throw e;
        }
        catch( Exception e ) {
            throw new ObjectFieldInvalidException(name, e.getMessage(), e);
        }
	}
	
	/**
     * Attempts to find a create'Name' method on this class
     * and invoke it. The method may or may not have a type
     * parameter - it is passed if it doesn't.
     * 
     * <p><b>Note: </b>If type is passed, but the create
     * method is found but does not support passing in a 
     * type then the parameter is ignored.</p>
     * 
     * @param name The name of the reference property
     * @param type The type of object to create
     * @return The newly constructed object
     */
    public ObjexObj createReference(String name, String type) {
        String createMethod = "create";
        if( Character.isLowerCase(name.charAt(0)) ) {
            createMethod += name.substring(0, 1).toUpperCase();
            if( name.length() > 1 ) createMethod += name.substring(1);
        }
        else {
            createMethod += name;
        }
        
        // Try non-plural
        String altCreateMethod = null;
        if( createMethod.endsWith("ies") ) altCreateMethod = createMethod.substring(0, createMethod.length() - 3) + "y";
        else if( createMethod.endsWith("s") ) altCreateMethod = createMethod.substring(0, createMethod.length() - 1);
        
        // Find the method
        boolean includeType = false;
        Method method = findMethod(createMethod, String.class);
        if( method != null ) includeType = true;
        
        if( method == null ) method = findMethod(altCreateMethod, String.class);
        if( method != null ) includeType = true;
        
        if( method == null ) method = findMethod(createMethod, (Class<?>[])null);
        if( method == null ) method = findMethod(altCreateMethod, (Class<?>[])null);
        
        // Invoke if found
        if( method != null ) {
            try {
                Object[] args = null;
                if( includeType ) args = new Object[]{type};
                return (ObjexObj)method.invoke(this, args);
            }
            catch( RuntimeException e ) {
                throw e;
            }
            catch( Exception e ) {
                throw new UnsupportedOperationException("Creation failed", e);
            }
        }
        else {
            throw new UnsupportedOperationException("Creation not supported on property: " + name);
        }
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
    
    /**
     * Helper to ensure an object is updateable before it
     * is updated. This method checks the container is open
     * and adds this object to the transaction if 
     * neccessary.
     * 
     * @param state The state bean
     */
    protected void ensureUpdateable(ObjexObjStateBean state) {
        checkUpdateable();
        if( !state.isEditable() ) container.addObjectToTransaction(this, state);
    }
    
    /**
     * Helper to get a property descriptor on this class for
     * the given property. The default creates the descriptor
     * using full introspection, but can be overridden if
     * there is some unusable behaviour - for instance the
     * getter and/or setter is not named normally.
     * 
     * @param name The name of the property
     * @return The property descriptor
     * @throws 
     */
    protected PropertyDescriptor getPropertyDescriptor(String name) {
        try {
            return new PropertyDescriptor(name, this.getClass());
        }
        catch( RuntimeException e ) {
            throw e;
        }
        catch( Exception e ) {
            throw new ObjectFieldInvalidException(name, e.getMessage(), e);
        }
    }
    
    /**
     * Helper to find a method. Used from createReference as we test
     * for a number of different names and parameters
     * 
     * @param name The name of the method
     * @param params The parameters we want
     * @return The method if found
     */
    private Method findMethod(String name, Class<?>... params) {
        Method ret = null;
        try {
            ret = this.getClass().getMethod(name, params);
        }
        catch( NoSuchMethodException e ) {}
        
        return ret;
    }
    
    /**
     * This method will attempt to clone the basic value
     * if it is possible. Obviously this is not needed
     * for Strings and other immutable objects because 
     * they cannot be changed. It is also not needed for
     * primitives. However, any {@link Cloneable} object
     * is cloned and an array is copied into a new 
     * instance.
     * 
     * <p>Note that the clone, if one occurs, is a 
     * typically a shallow copy.</p>
     * 
     * @param val
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T> T cloneValue(T val) {
        if( val == null ) return null;
        
        // Most basic types are immutable (primitives, Number, String etc) 
        T ret = val;
        
        // Others should cloneable
        if( val instanceof Cloneable ) {
            try {
                Method clone = val.getClass().getMethod("clone", (Class<?>[])null);
                if( Modifier.isPublic(clone.getModifiers()) ) ret = (T)clone.invoke(val, (Object[])null);
            }
            catch( RuntimeException e ) { throw e; }
            catch( Exception e ) {}
        }
        
        return ret;
    }
}
