package org.tpspencer.tal.objexj;

import java.io.Serializable;

/**
 * This interface represents an ID of a object inside
 * the current container. All IDs must be stringifiable
 * and reconstructable given the string - they must
 * also be able to easily determine if the represent
 * a null or root object.
 * 
 * @author Tom Spencer
 */
public interface ObjexID extends Serializable {

	/**
	 * @return True if the ID is a null ID
	 */
	public boolean isNull();
	
	/**
	 * @return True if the ID is a temporary ID inside a transaction
	 */
	public boolean isTemp();

	/**
	 * @return The type of object represented by the ID
	 */
	public String getType();
	
	/**
	 * @return The ID of the object, either a Long or a String
	 */
	public Object getId();
}
