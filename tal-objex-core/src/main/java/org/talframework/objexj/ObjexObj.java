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
package org.talframework.objexj;

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
	 * Call to have the object accept the writer and then store
	 * all of its state on the writer.
	 * 
	 * @param writer The writer to write into
	 * @param includeNonPersistent Indicates if non-persisted fields should be written
	 */
	public void acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent);
	
	/**
	 * Call to have the object accept the reader and then read
	 * all of its state from the reader.
	 * 
	 * @param reader The reader to write into
	 */
	public void acceptReader(ObjexStateReader reader);
	
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
	
	/**
     * This enum represents the possible types of each field. It
     * is used to remove ambiguity that can arise from simply 
     * looking at the objects type - for instance a String and
     * a Memo are both represented as strings.
     * 
     * SUGGEST: This is not extensible, if it needs to be (and I'm not sure it does) consider a field type annotation?
     *
     * @author Tom Spencer
     */
    public static enum ObjexFieldType {
        /** Indicates the field represents the parent ID */
        PARENT_ID,
        /** Indicates the field is a text field */
        STRING,
        /** Indicates the field is a potentially large field */
        MEMO,
        /** Indicates the field is a number */
        NUMBER,
        /** Indicates the field is a boolean */
        BOOL,
        /** Indicates the field is a date field */
        DATE,
        /** Indicates the field is a short binary (byte) field */
        SHORT_BLOB,
        /** Indicates the field is a blob (byte) field */
        BLOB,
        /** Indicates the field references an external blob field */
        BLOB_REFERENCE,
        /** Indicates the field represents a user */
        USER,
        /** Indicates the field references another object */
        REFERENCE,
        /** Indicates the field references an owned object */
        OWNED_REFERENCE,
        /** Indicates the field holds an arbitrary object */
        OBJECT;
    }
}
