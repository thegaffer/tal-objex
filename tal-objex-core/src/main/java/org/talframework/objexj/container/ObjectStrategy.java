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

package org.talframework.objexj.container;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObj.ObjexFieldType;
import org.talframework.objexj.container.impl.SimpleContainer;
import org.talframework.objexj.object.BaseObjexObj;

/**
 * This interface represents an object that can describe and create
 * instances of an ObjexObj. 
 * 
 * <p>Use of this class is entirely optional,
 * but the basic implementations of a container, many default 
 * middlewares and the {@link BaseObjexObj} implementation uses it.
 * {@link SimpleContainer} and {@link BaseObjexObj} can still be
 * used, but you must override some of the methods - see those 
 * classes.</p>
 * 
 * <p>It should also be noted that much of this information in here
 * can be deduced from looking at an domain/business objects' class 
 * and annotations - indeed this is typically how this is formed -
 * but is effectively cached in this class. You can also implement 
 * this class directly so that there is no introspection/reflection
 * required.</p>
 * 
 * @author Tom Spencer
 */
public interface ObjectStrategy {

	/**
	 * @return The short name for this object type
	 */
	public String getTypeName();
	
	/**
	 * @return The class of the main class this is a strategy for
	 */
	public Class<?> getMainClass();
	
	/**
     * Given the name of a property returns its strategy object
     *  
     * @return The strategy object for that property or null
     */
    public PropertyStrategy getProperty(String name);
	
	/**
	 * This method is called by the container to get an ID from 
	 * the given object instance. Although Objex will generate an ID
	 * for every object independent of state, it is also possible for
	 * an object to have the ID part of its identity as part of it's 
	 * state. This is called during the creation loop to allow an
	 * ObjexStrategy to deduce this value from the seed object.
	 * 
	 * <p><b>Note: </b>If this is called during object construction
	 * and an ID is returned that object is considered new. If during
	 * the save of the container that ID is found not to be unique 
	 * then an error occurs, but it is not checked until the save.</p>
	 * 
	 * @param obj The object to get ID for
	 * @return The ObjexID if the ID can be found, otherwise null
	 */
	public ObjexID getObjexID(Object obj);
	
	/**
	 * This is called by the container to actually create an instance of the class.
	 * 
	 * @param container The container initialising this object
	 * @param parent The ID of the parent of this object (if there is one)
	 * @param id The ID of this object
	 * @param seed The seed object
	 * @return The newly created ObjexObj instance.
	 */
	public ObjexObj createInstance(InternalContainer container, ObjexID parent, ObjexID id, Object seed);
	
	/**
	 * This is called, typically, by a middleware to create a reference to the 
	 * object. A reference should act as a proxy to the real class by holding its 
	 * ID and the container. It should implement all the same interfaces as the 
	 * real business object, but when any of those methods are called it should
	 * go to the container, get the real object and then invoke the corresponding
	 * method on this object.  
	 * 
	 * <p>Note: This does not need to be a Java Proxy object, although this is
	 * the default way we implement this.</p>
	 *  
	 * @param container The container that the owning object is in
	 * @param id The ID of the object we are creating the reference for
	 * @return The proxy reference
	 */
	public ObjexObj createReferenceProxy(InternalContainer container, ObjexID id);
	
	/**
	 * This class represents basic details about the property
	 * 
	 * <p>It is a class not
	 * an interface because we do not want to abuse the strategy mechanism
	 * and leak too much information. The idea is that this provides just
	 * enough information for all the generic implements inside the core of
	 * Objex. These include setting properties generically, validating and
	 * basic persistence.</p>
	 * 
	 * <p>If you are writing persistence and need more information then you
	 * should really be writing a custom persistence engine and have knowledge
	 * of the domain objects.</p>
	 * 
	 * @author Tom Spencer
	 */
	public static class PropertyStrategy {
	    /** The name of the property */
	    private final String name;
	    /** The basic objex type of the field */
	    private final ObjexFieldType objexType;
	    /** The known type of this field (OBJECT if not known) */
	    private final PropertyTypeEnum propertyType;
	    /** Any additional characteristics */
	    private final PropertyCharacteristic[] characteristics;
	    /** The type of object accepted in a reference or child field - if collection, the E or V type */
	    private final Class<?> elementType;
	    
	    public PropertyStrategy(String name, ObjexFieldType objexType, PropertyTypeEnum propertyType, PropertyCharacteristic... characteristics) {
	        this.name = name;
	        this.objexType = objexType;
	        this.propertyType = propertyType;
	        this.characteristics = characteristics;
	        this.elementType = null;
	    }
	    
	    public PropertyStrategy(String name, ObjexFieldType objexType, Class<?> elementType, PropertyTypeEnum propertyType, PropertyCharacteristic... characteristics) {
	        this.name = name;
            this.objexType = objexType;
            this.propertyType = propertyType;
            this.characteristics = characteristics;
            this.elementType = elementType;
        }
	    
	    /**
	     * @return The name of the property
	     */
	    public String getName() {
            return name;
        }

        /**
         * @return the objexType
         */
        public ObjexFieldType getObjexType() {
            return objexType;
        }

        /**
         * @return the propertyType
         */
        public PropertyTypeEnum getPropertyType() {
            return propertyType;
        }

        /**
         * @return the characteristics
         */
        public PropertyCharacteristic[] getCharacteristics() {
            return characteristics;
        }
        
        /**
         * @return The element type - only set if a reference property, otherwise null
         */
        public Class<?> getElementType() {
            return elementType;
        }
        
        /**
         * Quick method to determine if the property is the given objex type
         * or not.
         * 
         * @param type The type to check against
         * @return True if it does, false otherwise
         */
        public boolean isObjexType(ObjexFieldType type) {
            return objexType == type;
        }
        
        /**
         * Quick method to determine if the property is the given basic type
         * or not.
         * 
         * @param type The type to check against
         * @return True if it does, false otherwise
         */
        public boolean isType(PropertyTypeEnum type) {
            return propertyType == type;
        }
	    
        /**
         * Quick method to determine if the property has the given characteristic
         * or not.
         * 
         * @param characteristic The characteristic to test
         * @return True if it does, false otherwise
         */
        public boolean isCharacteristic(PropertyCharacteristic characteristic) {
            if( characteristics == null || characteristics.length == 0 ) return false;
            
            for( PropertyCharacteristic c : characteristics ) {
                if( c == characteristic ) return true;
            }
            
            return false;
        }
	}
	
	/**
	 * This enum represents some additional characteristic about the
	 * property which is known. A property might include one or more
	 * of these, whereas it only has 1 ObjexFieldType and 1 Property
	 * type.
	 *
	 * @author Tom Spencer
	 */
	public static enum PropertyCharacteristic {
	    PERSISTENT,
	    CALCULATED
	}
	
	/**
	 * To save constant dynamic lookup this enum indicates the basic
	 * type of the property.
	 *
	 * @author Tom Spencer
	 */
	public static enum PropertyTypeEnum {
	    STRING,
	    DATE,
	    DOUBLE,
	    FLOAT,
	    LONG,
	    INT,
	    SHORT,
	    CHAR,
	    BYTE,
	    BOOL,
	    OBJECT,
	    LIST,
	    SET,
	    MAP
	}
}
