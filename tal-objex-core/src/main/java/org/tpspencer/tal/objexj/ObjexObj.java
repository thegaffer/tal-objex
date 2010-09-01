package org.tpspencer.tal.objexj;

/**
 * This interface represents an actual object held by
 * a container. 
 * 
 * @author Tom Spencer
 */
public interface ObjexObj {
    
    /**
	 * @return The ID of this object
	 */
	public ObjexID getId();
	
	/**
	 * @return The type name of this object
	 */
	public String getType();
	
	/**
	 * @return The ID of this objects parent
	 */
	public ObjexID getParentId();
	
	/**
	 * @return The objects parent
	 */
	public ObjexObj getParent();
	
	/**
	 * @return The root object in the container (amy or may not be parent)
	 */
	public ObjexObj getRoot();
	
	/**
	 * @return The container this object was served from
	 */
	public Container getContainer();
	
	/**
	 * Call to get a behaviour interface from this object. We 
	 * encourage the use of this method over instanceof on
	 * ObjexObj instances in case the interface is supported
	 * only at certain points in the lifecycle of the object
	 * or at times when the object contains strong-form
	 * compound elements.
	 * 
	 * @param behaviour The type of behaviour required
	 * @return
	 */
	public <T> T getBehaviour(Class<T> behaviour);
	
	/**
	 * Helper to get a property given its name. This is
	 * a convienience to save the client having to do
	 * reflection if it does not know the concrete type.
	 * 
	 * @param name The name of the property required
	 * @return The value of the property
	 */
	public Object getProperty(String name);
	
	/**
	 * Convienience method to both get the properties
	 * value and return it as the expected type. If
	 * the property is not of that type a class cast
	 * exception occurs.
	 * 
	 * @param name The name of the property
	 * @param expected It's expected type
	 * @return The property value
	 * @throws ClassCastException If the property is not of that type
	 */
	public <T> T getProperty(String name, Class<T> expected);
	
	/**
     * Helper to get a property given its name. This is
     * a convienience to save the client having to do
     * reflection if it does not know the concrete type.
     * 
     * <p>Note: With this version of the method the property
     * is returned as a string. The formatting is determined
     * by the object. If you prefer to do you own formatting
     * use the getProperty method below.
     * 
     * @param name The name of the property required
     * @return The value of the property as a string
     */
    public String getPropertyAsString(String name);
    
    /**
     * Sets the property if possible. The type of object
     * must be the same or compatible with the value on
     * the state bean.
     * 
     * <p><b>Note: </b>There is no guarantee this method
     * is supported. In real business objects with complex
     * or semi-complex behaviour this is typically not
     * supported in favour of finer grained methods on
     * the behaviour class.</p>
     * 
     * @param name The name of the property
     * @param val The raw value.
     */
    public void setProperty(String name, Object newValue);
    
    /**
     * Call to create a new object in the container under
     * the reference property named. The property named
     * must be an owned reference property. Although type
     * is passed in it may be ignored if the reference
     * property only supports a single pre-determined
     * object type.
     * 
     * @param name The name of the reference property
     * @param type The type of object to create
     * @return The newly constructed object
     */
    public ObjexObj createReference(String name, String type);
	
	/**
	 * Call this method to validate an object. This can be called
	 * at any time to have an object validate itself. It is called
	 * as part of the saving process automatically, but often we
	 * need to know the errors and warnings before that time so
	 * this method is publically available.
	 * 
	 * @param request The validation request object
	 * @return True if the object is valid, false otherwise
	 */
	public void validate(ValidationRequest request);
}
