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
