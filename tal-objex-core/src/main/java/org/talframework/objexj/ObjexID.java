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

import java.io.Serializable;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This interface represents an ID of a object inside
 * the current container. All IDs must be stringifiable
 * and reconstructable given the string - they must
 * also be able to easily determine if the represent
 * a null or root object.
 * 
 * @author Tom Spencer
 */
@XmlJavaTypeAdapter(DefaultObjexID.XmlObjexIDAdaptor.class)
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
