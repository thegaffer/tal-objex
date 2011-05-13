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

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talframework.domain.annotations.CalculatedProperty;
import org.talframework.domain.annotations.ChildProperty;
import org.talframework.domain.annotations.IdentityObject;
import org.talframework.domain.annotations.Property;
import org.talframework.domain.annotations.ReferenceProperty;
import org.talframework.domain.annotations.TransientProperty;
import org.talframework.domain.annotations.ValueObject;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObj.ObjexFieldType;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.object.fields.ObjexChildList;
import org.talframework.objexj.object.fields.ObjexChildMap;
import org.talframework.objexj.object.fields.ObjexChildSet;
import org.talframework.objexj.object.fields.ObjexRefList;
import org.talframework.objexj.object.fields.ObjexRefMap;
import org.talframework.objexj.object.fields.ObjexRefSet;
import org.talframework.objexj.object.fields.ProxyReference;
import org.talframework.objexj.object.writer.BaseObjectReader.ObjectReaderBehaviour;
import org.talframework.objexj.object.writer.BeanObjectReader;
import org.talframework.util.beans.BeanDefinition;
import org.talframework.util.beans.definition.BeanDefinitionsSingleton;

/**
 * The basic implementation of the object strategy interface. This will
 * be fine for most scenarios, though you can derive from this class
 * if you need. 
 * 
 * <p>A static method exists to process the class based on its type
 * and annotation information. Or the constructors can be used directly.</p>
 * 
 * @author Tom Spencer
 */
public class DefaultObjectStrategy implements ObjectStrategy {
	
	/** Holds the name of the object */
	private final String name;
	/** The main class */
	private final Class<?> mainClass;
	/** The property strategies for the class */
	private final Map<String, PropertyStrategy> properties;
	/** The method to create the object with */
	private final ObjectCreateMethod createMethod;
	/** If the method is proxy, then this is the calculated list of interfaces to support */
	private final Class<?>[] proxyInterfaces;
	
	/**
	 * Call to form an Object Strategy from a class. All but the simplest of
	 * classes will likely have Objex annotations to improve further.
	 * 
	 * @param name The name to use internally (if null then the classes simple name is used)
	 * @param cls The class to generate strategy for
	 * @param strict If true then only allowed object types can be used
	 * @return The strategy
	 */
	public static ObjectStrategy calculateStrategy(String name, Class<?> cls, boolean strict) {
	    // throw new UnsupportedOperationException("Not yet implemented");
	    if( cls == null ) throw new IllegalArgumentException("The class provided to default object strategy cannot be null");
	    if( ObjexObj.class.isAssignableFrom(cls) && !InternalObjexObj.class.isAssignableFrom(cls) ) throw new IllegalArgumentException("Cannot use DefaultObjectStrategy for a class that implements ObjexObj, but not InternalObjexObj: " + cls);
	    if( strict && !cls.isAnnotationPresent(IdentityObject.class) ) throw new IllegalArgumentException("The class provided to default object strategy must be an IdentityObject if strict is true: " + cls);
	    
	    if( name == null ) name = cls.getSimpleName();
	    
	    // TODO: Determine if any Property marked props, if so all props must be marked
	    boolean requirePropsMarked = false;
	    
	    List<PropertyStrategy> props = new ArrayList<ObjectStrategy.PropertyStrategy>();
	    BeanDefinition def = BeanDefinitionsSingleton.getInstance().getDefinition(cls);
	    for( String prop : def.getProperties() ) {
	        Class<?> elementType = null;
	        ObjexFieldType type = null;
	        PropertyTypeEnum propertyType = null;
	        
	        ChildProperty child = def.getReadAnnotation(prop, ChildProperty.class);
	        ReferenceProperty ref = child == null ? def.getReadAnnotation(prop, ReferenceProperty.class) : null;
	        boolean marked = (child != null || ref != null) ? true : def.getReadAnnotation(prop, Property.class) != null;
	        
	        type = child != null ? ObjexFieldType.OWNED_REFERENCE : (ref != null ? ObjexFieldType.REFERENCE : null);
	        
	        // Is a child or reference property
	        if( type != null ) {
	            elementType = child != null ? child.value() : ref.value();
	            if( !elementType.isInterface() ) throw new IllegalArgumentException("A child or reference property must be based (element type) on an interface type: " + cls);
	            
	            Class<?> propsClass = def.getPropertyType(prop);
	            if( propsClass.equals(elementType) ) propertyType = PropertyTypeEnum.OBJECT;
	            else {
	                propertyType = getPropertyType(propsClass, true);
	                if( propertyType != PropertyTypeEnum.LIST &&
	                        propertyType != PropertyTypeEnum.SET &&
	                        propertyType != PropertyTypeEnum.MAP ) throw new IllegalArgumentException("A child or reference property must be either the element type directly (single) or a List, Set or Map - it must be directly to these interfaces, not a specific collection implementing these interfaces!");
	                
	                // FUTURE: Could try and compare elementType against the generics template arg if it can be deduced!
	            }
	        }
	        
	        // Is a simple property
	        else if( !requirePropsMarked || marked ) {
	            propertyType = getPropertyType(def.getPropertyType(prop), strict);
                type = getObjexTypeFromProp(propertyType);
	        }
	        
	        // Create if valid
	        if( type != null ) {
	            if( elementType != null ) props.add(new PropertyStrategy(prop, type, elementType, propertyType, getPropertyCharacteristics(def, prop)));
	            else props.add(new PropertyStrategy(prop, type, propertyType, getPropertyCharacteristics(def, prop)));
	        }
	    }
	    
	    return new DefaultObjectStrategy(cls, props.toArray(new PropertyStrategy[props.size()]));
	}
	
	/**
	 * Internal helper to the calculate method to work out a properties
	 * type.
	 * 
	 * <p>The strict parameters means two things. First if it is false and the
	 * class (cls) is not recognised then the property type is just set as a
	 * OBJECT, otherwise this generates a failure. Additionally for known 
	 * property types, strict tests for an exact match, whereas if it is false
	 * then derivation works. For instance if strict then java.sql.Date is
	 * rejected, but if not it is set as a DATE because it derived from 
	 * java.util.Date. It is recommended to work in strict mode.</p>  
	 * 
	 * @param cls The class to work out type of
	 * @param strict If strict, then only exact matches are allowed, otherwise derivations are considered
	 * @return The property type
	 */
	private static PropertyTypeEnum getPropertyType(Class<?> cls, boolean strict) {
	    PropertyTypeEnum ret = null;
	    
	    if( String.class.equals(cls) ) ret = PropertyTypeEnum.STRING;
	    else if( cls.isPrimitive() || Number.class.isAssignableFrom(cls) ) {
    	    if( Double.class.equals(cls) || double.class.equals(cls) ) ret = PropertyTypeEnum.DOUBLE;
    	    else if( Float.class.equals(cls) || float.class.equals(cls) ) ret = PropertyTypeEnum.FLOAT;
    	    else if( Long.class.equals(cls) || long.class.equals(cls) ) ret = PropertyTypeEnum.LONG;
    	    else if( Integer.class.equals(cls) || int.class.equals(cls) ) ret = PropertyTypeEnum.INT;
    	    else if( Short.class.equals(cls) || short.class.equals(cls) ) ret = PropertyTypeEnum.SHORT;
    	    else if( Character.class.equals(cls) || char.class.equals(cls) ) ret = PropertyTypeEnum.CHAR;
    	    else if( Byte.class.equals(cls) || byte.class.equals(cls) ) ret = PropertyTypeEnum.BYTE;
    	    else if( Boolean.class.equals(cls) || boolean.class.equals(cls) ) ret = PropertyTypeEnum.BOOL;
    	    else throw new IllegalArgumentException("Property type not a recognised primitive or number: " + cls);
	    }
	    else if( testClass(Date.class, cls, strict) ) ret = PropertyTypeEnum.DATE;
	    else if( testClass(List.class, cls, strict) ) ret = PropertyTypeEnum.LIST;
	    else if( testClass(Set.class, cls, strict) ) ret = PropertyTypeEnum.SET;
	    else if( testClass(Map.class, cls, strict) ) ret = PropertyTypeEnum.MAP;
	    else if( cls.isAnnotationPresent(ValueObject.class) ) ret = PropertyTypeEnum.OBJECT;
	    else if( !strict ) ret = PropertyTypeEnum.OBJECT;
        
	    return ret;
	}
	
	/**
	 * Helepr to test a class (cls) is either equal to, or if not in strict
	 * mode derived from, the given comparison class.
	 * 
	 * @param comparison The class we want to compare cls with
	 * @param cls The class to compare
	 * @param strict If true must be equal to comparison, otherwise comparison is assignable from cls
	 * @return True if the cls compares to the comparison class
	 */
	private static boolean testClass(Class<?> comparison, Class<?> cls, boolean strict) {
	    if( comparison.equals(cls) ) return true;
	    else if( !strict && comparison.isAssignableFrom(cls) ) return true;
	    return false;
	}
	
	/**
	 * Internal helper to calculate the basic ObjexFieldType from the
	 * property type
	 * 
	 * @param type The type to get more generic field type from
	 * @return The field type
	 */
	private static ObjexFieldType getObjexTypeFromProp(PropertyTypeEnum type) {
	    ObjexFieldType ret = null;
	    switch(type) {
	    case CHAR:
	    case STRING:
	        ret = ObjexFieldType.STRING;
	        break;
	        
	    case DATE:
	        ret = ObjexFieldType.DATE;
	        break;
	           
	    case DOUBLE:
	    case FLOAT:
	    case LONG:
	    case INT:
	    case SHORT:
	        ret = ObjexFieldType.NUMBER;
	        break;
	        
	    case BYTE:
	        ret = ObjexFieldType.BLOB;
	        break;
	        
	    case BOOL:
	        ret = ObjexFieldType.BOOL;
	        break;
	    
	    default:
	        ret = ObjexFieldType.OBJECT;
	        break;
	    }
	    
	    return ret;
	}
	
	private static PropertyCharacteristic[] getPropertyCharacteristics(BeanDefinition def, String prop) {
	    PropertyCharacteristic[] ret = null;
	    
        boolean trans = def.getReadAnnotation(prop, TransientProperty.class) != null;
        boolean calc = def.getReadAnnotation(prop, CalculatedProperty.class) != null;
        if( !trans && calc ) ret = new PropertyCharacteristic[]{PropertyCharacteristic.PERSISTENT, PropertyCharacteristic.CALCULATED};
        else if( !trans ) ret = new PropertyCharacteristic[]{PropertyCharacteristic.PERSISTENT};
        else if( calc ) ret = new PropertyCharacteristic[]{PropertyCharacteristic.CALCULATED};
        
        return ret;
	}
	
    /**
     * Constructs an object strategy using a given state class and
     * the default ObjexObj. This constructor initialises the strategy
     * 
     * @param stateClass The state class to use
     */
	public DefaultObjectStrategy(Class<?> mainClass, PropertyStrategy... properties) {
		this.name = mainClass.getSimpleName();
		this.mainClass = mainClass;
		if( Serializable.class.isAssignableFrom(mainClass) ) throw new RuntimeException("Cannot have a serialisable object as ObjexObj!");
		
		this.properties = new HashMap<String, ObjectStrategy.PropertyStrategy>();
		this.createMethod = getCreateMethod();
		this.proxyInterfaces = getProxyInterfaces();
		
		for( PropertyStrategy prop : properties ) {
		    this.properties.put(prop.getName(), prop);
		}
	}
	
	/**
     * Constructs an object strategy using a given state class and
     * the ObjexObj class. This constructor initialises the strategy
     * 
     * @param stateClass The state class to use
     */
    public DefaultObjectStrategy(String name, Class<?> mainClass, PropertyStrategy... properties) {
        this.name = name;
        if( this.name == null || this.name.length() == 0 ) throw new RuntimeException("An ObjexObj must have a name!");
        this.mainClass = mainClass;
        if( Serializable.class.isAssignableFrom(mainClass) ) throw new RuntimeException("Cannot have a serialisable object as ObjexObj!");
        
        this.properties = new HashMap<String, ObjectStrategy.PropertyStrategy>();
        this.createMethod = getCreateMethod();
        this.proxyInterfaces = getProxyInterfaces();
        
        for( PropertyStrategy prop : properties ) {
            this.properties.put(prop.getName(), prop);
        }
    }
    
    /**
     * Called during construction to determine the create method to use.
     * 
     * <p>The base method uses the following logic:</p>
     * <ul>
     * <li>If the object supports InternalObjexObj, creates it directly</li>
     * <li>If the object implements interfaces, but not InternalObjexObj,
     * then uses the Proxy.</li>
     * <li>If the object does not support interfaces at all then creates
     * a Simple object</li>
     * </ul>
     * 
     * @return
     */
    protected ObjectCreateMethod getCreateMethod() {
        ObjectCreateMethod ret = null;
        
        if( InternalObjexObj.class.isAssignableFrom(this.mainClass) ) ret = ObjectCreateMethod.FULL;
        else if( this.mainClass.getInterfaces() != null && this.mainClass.getInterfaces().length > 0 ) ret = ObjectCreateMethod.PROXY;
        else ret = ObjectCreateMethod.SIMPLE;
            
        return ret;
    }
    
    /**
     * Called during construction to determine the proxy interfaces for
     * the class
     * 
     * @return The proxy interfaces
     */
    protected Class<?>[] getProxyInterfaces() {
        Class<?>[] ret = this.mainClass.getInterfaces();
        
        boolean foundObjexObj = false;
        for( Class<?> cls : ret ) {
            if( cls.equals(InternalObjexObj.class) ) {
                foundObjexObj = true;
                break;
            }
        }
        
        if( !foundObjexObj ) {
            Class<?>[] ifaces = new Class<?>[ret.length + 1];
            System.arraycopy(ret, 0, ifaces, 1, ret.length);
            ifaces[0] = InternalObjexObj.class;
            ret = ifaces;
        }
        
        return ret;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTypeName() {
        return name;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getMainClass() {
        return mainClass;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyStrategy getProperty(String name) {
        return properties.get(name);
    }
    
    /**
     * {@inheritDoc}
     * 
     * Derived class can override this to pick out the ID properties
     * if it is possible.
     */
    @Override
    public ObjexID getObjexID(Object obj) {
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ObjexObj createInstance(InternalContainer container, ObjexID parent, ObjexID id, Object seed) {
        InternalObjexObj ret = null;
        
        Object newObject = null;
        try {
            newObject = mainClass.newInstance();
        } 
        catch( Exception e ) {
            // TODO: Exception!!!
        }
        
        switch(createMethod) {
        case PROXY:
            ret = (InternalObjexObj)Proxy.newProxyInstance(this.getClass().getClassLoader(), proxyInterfaces, new ProxyObjexObj(this, newObject));
            break;
            
        case SIMPLE:
            ret = new SimpleObjexObj(this, newObject);
            break;
            
        case FULL:
            ret = (InternalObjexObj)newObject;
            break;
        }
        
        // Initialise
        ret.init(container, id, parent);
        
        // Copy values from seed if the seed is populated
        if( isSeedPopulated(seed) ) seedObjexObject(ret, seed);
        
        // Otherwise ensure all child/reference collection/map props are not null
        else {
            // TODO: Not totally happy with this!
            /*for( String key : properties.keySet() ) {
                PropertyStrategy prop = properties.get(key);
                if( !prop.isObjexType(ObjexFieldType.REFERENCE) && !prop.isObjexType(ObjexFieldType.OWNED_REFERENCE) ) continue;
                    
                boolean ref = prop.isObjexType(ObjexFieldType.REFERENCE);
                
                switch(prop.getPropertyType()) {
                case LIST:
                    if( ret.getProperty(prop.getName()) == null ) ret.setProperty(prop.getName(), ref ? new ObjexRefList(ret) : new ObjexChildList(ret));
                    break;
                    
                case SET:
                    if( ret.getProperty(prop.getName()) == null ) ret.setProperty(prop.getName(), ref ? new ObjexRefSet(ret) : new ObjexChildSet(ret));
                    break;
                    
                case MAP:
                    if( ret.getProperty(prop.getName()) == null ) ret.setProperty(prop.getName(), ref ? new ObjexRefMap(ret) : new ObjexChildMap(ret));
                    break;
                    
                case OBJECT:
                }
            }*/
        }
        
        return ret;
    }
    
    /**
     * {@inheritDoc}
     * 
     * Derived classes should override this create a proxy reference if there
     * is a hard coded one. Default is to create a proxy reference.
     */
    @Override
    public ObjexObj createReferenceProxy(InternalContainer container, ObjexID id) {
        return (ObjexObj)Proxy.newProxyInstance(this.getClass().getClassLoader(), proxyInterfaces, new ProxyReference(id, container));
    }
    
    /**
     * Called to see if the given seed object has properties set. This is
     * called to halt seeding the object if there is no point, but must
     * be implemented in derived class - the base version just returns true
     * if the seed is not null.
     * 
     * @param seed The seed object
     * @return True if it is populated.
     */
    protected boolean isSeedPopulated(Object seed) {
        return seed != null;
    }
    
    /**
     * Helper class to seed the newly created ObjexObj instance with 
     * the seed object. The base class has already called isSeedPopulated
     * before calling this method. The base version uses the BeanReader to
     * seed all properties.
     *  
     * @param realObject The newly constructed {@link ObjexObj} instance
     * @param seed The object containing the seed values
     */
    protected void seedObjexObject(ObjexObj realObject, Object seed) {
        BeanObjectReader reader = new BeanObjectReader(ObjectReaderBehaviour.INCLUDE_OWNED, ObjectReaderBehaviour.INCLUDE_REFERENCES);
        reader.readObject(seed, realObject);
    }
    
    /**
     * Internal enum that tells the createInstance method how it should create
     * the {@link ObjexObj} object.
     *
     * @author Tom Spencer
     */
    private static enum ObjectCreateMethod {
        /** A proxy object is created wrapping the business object */
        PROXY,
        /** A simple objexobj is created wrapping the business object */
        SIMPLE,
        /** A direct instance of the class is created */
        FULL
    }
}
