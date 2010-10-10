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

package org.talframework.objexj;

import java.io.Serializable;
import java.util.Map;

/**
 * This interface represents a bean that holds an objects
 * fundamental state. It also acts as the persistable 
 * state of the object - by separating out the state from
 * the behaviour we can plug objex objects into a number
 * of different runtimes without drama. 
 * 
 * <p><b>Note: </b>This interface should not be used
 * externally by any client to a container because it's
 * mis-use will bypass the behaviour of the objects. It
 * you have any imports or reference to this object you
 * are probably doing something wrong!</p>
 * 
 * <p>Authors Note: I really did not want to have this
 * interface at all, but I've come to the conclusion it
 * is pretty much required. Given the Roo generators it
 * is no drama to implement this interface.</p>
 * 
 * @author Tom Spencer
 */
public interface ObjexObjStateBean extends Serializable {
    
    /**
     * Initialises the state bean when it is being created
     * for a new object in the container. It will be set
     * to writable immediately.
     * 
     * @param parentId The ID of the parent object
     */
    public void create(ObjexID parentId);
    
    /**
     * Called in the transaction before save to set the
     * runtime ID of the object. The ID will be null
     * when this call is made.
     * 
     * @param id The underlying runtime environment ID
     */
    public void preSave(Object id);
    
    /**
     * Call to clone this state bean. The resulting state
     * bean will be initially set to read-only.
     * 
     * @return The cloned instance
     */
    public ObjexObjStateBean cloneState();
    
    /**
     * @return The raw ID of the object (will likely not be an ObjexID)
     */
    public Object getId();

	/**
	 * @return The ID of the parent object to this one (if any) 
	 */
	public String getParentId();
	
	/**
     * @return The ObjexObj type that this bean represents
     */
    public String getObjexObjType();
    
    /**
     * Determines if the state bean is editable (i.e. has 
     * changed and the bean is in a transaction).
     * 
     * @return True if the bean can be changed
     */
    public boolean isEditable();
    
    /**
     * Sets the state bean to be editable.
     */
    public void setEditable();
    
    /**
     * Called to update any temporary references this
     * object may be holding. This is done as the transaction
     * commits to turn any temp IDs into real IDs.
     * 
     * @param refs A map holding the original temp ID and its new real ID
     */
    public void updateTemporaryReferences(Map<ObjexID, ObjexID> refs);
    
    /**
     * Call to have the object accept the writer and then store
     * all of its state on the writer. ID, ParentID and Type 
     * are not written.
     * 
     * @param writer The writer to write into
     * @param includeNonPersistent Indicates if non-persisted fields should be written
     */
    public void acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent);
    
    /**
     * Call to have the object accept the reader and then read
     * all of its state from the reader. ID, ParentID and Type
     * are not read.
     * 
     * @param reader The reader to write into
     */
    public void acceptReader(ObjexStateReader reader);
    
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
        /** Indicates the field is a text field */
        STRING,
        /** Indicates the field is a potentially large field */
        MEMO,
        /** Indicates the field is a number */
        NUMBER,
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
