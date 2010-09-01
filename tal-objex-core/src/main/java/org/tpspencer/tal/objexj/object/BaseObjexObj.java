package org.tpspencer.tal.objexj.object;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.container.InternalContainer;
import org.tpspencer.tal.objexj.exceptions.ObjectFieldInvalidException;

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
	
	////////////////////////////////////////////
    // ObjexObj Methods
    
	/**
	 * Simply returns the ID
	 */
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
	protected InternalContainer getInternalContainer() {
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
        if( createMethod.endsWith("s") ) altCreateMethod = createMethod.substring(0, createMethod.length() - 1);
        
        // Find the method
        boolean includeType = false;
        Method method = findMethod(createMethod, String.class);
        if( method != null ) includeType = true;
        
        if( method == null ) method = findMethod(altCreateMethod, String.class);
        if( method != null ) includeType = true;
        
        if( method == null ) method = findMethod(createMethod, (Class<?>[])null);
        if( method == null ) method = findMethod(altCreateMethod, (Class<?>[])null);
        
        // Invoke if found
        if( method != null && ObjexObj.class.isAssignableFrom(method.getReturnType()) ) {
            try {
                return (ObjexObj)method.invoke(this, includeType ? type : null);
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
}
