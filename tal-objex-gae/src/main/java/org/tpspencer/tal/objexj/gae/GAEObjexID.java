package org.tpspencer.tal.objexj.gae;

import org.tpspencer.tal.objexj.ObjexID;

/**
 * This class implements the ObjexID interface for
 * a Google App Engine datastore container. The
 * ID must contain the object state class name and
 * the ID that is unique the the container.
 * 
 * @author Tom Spencer
 */
public final class GAEObjexID implements ObjexID {

	private final String type;
	private final long id;
	
	/**
	 * Constructs an GAEObjexID given a string. The
	 * constructor will split the string into its
	 * constituent parts.
	 * 
	 * @param id The ID to split
	 */
	public GAEObjexID(String id) {
		int index = id.indexOf('|');
		if( index < 0 || index >= id.length() - 1 ) throw new IllegalArgumentException("Invalid string passed as ObjexID: " + id);
		
		this.type = id.substring(0, index);
		try {
			this.id = Long.parseLong(id.substring(index + 1));
		}
		catch( NumberFormatException e ) {
			throw new IllegalArgumentException("The id of the object cannot be determined from the ID supplied: " + id);
		}
	}
	
	/**
	 * Constructs a new GAEObjexID given the type of
	 * object and its ID unique in the container.
	 * 
	 * @param type The type of object
	 * @param id Its ID
	 */
	public GAEObjexID(String type, long id) {
		this.type = type;
		this.id = id;
	}
	
	/**
	 * Helper to extract the Google App Engine object ID
	 * given the ID.
	 * 
	 * @param id The id of the object as supplied
	 * @return The ID
	 */
	public static GAEObjexID getId(Object id) {
		if( id == null ) return new GAEObjexID(null, 0);
		if( id instanceof GAEObjexID ) return (GAEObjexID)id;
		else if( id instanceof String ) return new GAEObjexID((String)id);
		else throw new IllegalArgumentException("Supplied ID is unrecognised in the container: " + id);
	}
	
	/**
	 * @return The type of object the ID represents
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @return The unique ID within the container
	 */
	public long getId() {
		return id;
	}
	
	public boolean isNull() {
		return id == 0;
	}
	
	public boolean isRoot() {
		return id == 1;
	}
	
	@Override
	public String toString() {
		return type + "|" + id;
	}
}
