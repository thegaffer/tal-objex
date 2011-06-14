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
package org.talframework.objexj.object;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.object.fields.ProxyReference;
import org.talframework.objexj.object.writer.BaseObjectReader.ObjectReaderBehaviour;
import org.talframework.objexj.object.writer.BeanObjectReader;

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
     * Constructs an object strategy using a given state class and
     * the default ObjexObj. This constructor initialises the strategy
     * 
     * @param stateClass The state class to use
     */
	public DefaultObjectStrategy(Class<?> mainClass, PropertyStrategy... properties) {
	    this(mainClass.getSimpleName(), mainClass, properties);
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
    public Collection<String> getPropertyNames() {
        return properties.keySet();
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
