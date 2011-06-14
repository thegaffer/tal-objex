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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talframework.domain.annotations.CalculatedProperty;
import org.talframework.domain.annotations.ChildProperty;
import org.talframework.domain.annotations.IdentityObject;
import org.talframework.domain.annotations.ReferenceProperty;
import org.talframework.domain.annotations.TransientProperty;
import org.talframework.domain.annotations.ValueObject;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObj.ObjexFieldType;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.container.ObjectStrategy.PropertyCharacteristic;
import org.talframework.objexj.container.ObjectStrategy.PropertyStrategy;
import org.talframework.objexj.container.ObjectStrategy.PropertyTypeEnum;
import org.talframework.util.beans.BeanDefinition;
import org.talframework.util.beans.definition.BeanDefinitionsSingleton;

/**
 * This class provides static methods for compiling an {@link ObjectStrategy}
 * out of a class in various different ways.
 *
 * @author Tom Spencer
 */
public class ObjectStrategyCompiler {

    /**
     * This method creates a strategy by looking at the class and looking
     * at a property file (of the same name as the class in the classpath)
     * to determine child and reference properties. Use this method if you
     * use these property files or if you class is extremely simple and
     * does not contain references to other objects, owned or not.
     * 
     * @param name The name of the Objex Object
     * @param cls The class that is the domain object
     * @return The strategy describing this Objex object
     */
    public static ObjectStrategy calculateStrategy(String name, Class<?> cls) {
        checkClass(cls, false);
        BeanDefinition def = BeanDefinitionsSingleton.getInstance().getDefinition(cls);
        return calculate(name, cls, def, new ConfigPropertyTypeCalculator(cls, def));
    }
    
    /**
     * This method creates a strategy by looking at the class and using the
     * domain annotations to understand the child and ref properties. Use this
     * method of creating the strategy if you implement the annotations.
     * 
     * @param name The name of the Objex Object
     * @param cls The class that is the domain object
     * @return The strategy describing this Objex object
     */
    public static ObjectStrategy calculateStrategyFromAnnotations(String name, Class<?> cls) {
        checkClass(cls, false);
        BeanDefinition def = BeanDefinitionsSingleton.getInstance().getDefinition(cls);
        return calculate(name, cls, def, new AnnotationPropertyTypeCalculator(cls, def));
    }
    
    /**
     * This method creates a strategy by looking at the class and using the 
     * passed in arrays of children and reference properties. Use this method
     * of creating the strategy if you do not implement the annotations or
     * create property files describing your objects.
     * 
     * @param name The name of the Objex Object
     * @param cls The class that is the domain object
     * @return The strategy describing this Objex object
     */
    public static ObjectStrategy calculateStrategy(String name, String[] childProps, String[] refProps, Class<?> cls) {
        checkClass(cls, false);
        BeanDefinition def = BeanDefinitionsSingleton.getInstance().getDefinition(cls);
        return calculate(name, cls, def, new CustomPropertyTypeCalculator(childProps, refProps, cls, def));
    }
    
    ///////////////////////////////////////////////////////
    
    /**
     * Helper to check the passed in class is appropriate
     */
    private static void checkClass(Class<?> cls, boolean strict) {
        if( cls == null ) throw new IllegalArgumentException("The class provided to default object strategy cannot be null");
        if( ObjexObj.class.isAssignableFrom(cls) && !InternalObjexObj.class.isAssignableFrom(cls) ) throw new IllegalArgumentException("Cannot use DefaultObjectStrategy for a class that implements ObjexObj, but not InternalObjexObj: " + cls);
        if( strict && !cls.isAnnotationPresent(IdentityObject.class) ) throw new IllegalArgumentException("The class provided to default object strategy must be an IdentityObject if strict is true: " + cls);
    }
    
    /**
     * The core calculate method which uses the Type Calculator to work
     * out the strategy for each property
     * 
     * @param name The name of the class (if null the classes simple name)
     * @param cls The class
     * @param definition The bean definition of that class
     * @param typeCalculator The type calculator to use
     * @return The object strategy
     */
    private static ObjectStrategy calculate(String name, Class<?> cls, BeanDefinition definition, PropertyTypeCalculator typeCalculator) {
        if( name == null ) name = cls.getSimpleName();
        
        List<PropertyStrategy> props = new ArrayList<ObjectStrategy.PropertyStrategy>();
        for( String prop : definition.getProperties() ) {
            PropertyStrategy strategy = typeCalculator.getPropertyStrategy(prop);
            if( strategy != null ) props.add(strategy);
        }
        
        return new DefaultObjectStrategy(name, cls, props.toArray(new PropertyStrategy[props.size()]));
    }
    
    /**
     * This helper class is used to determine a properties type.
     * The base class works for non-child, non-reference props,
     * the derived classes should handle these.
     *
     * @author Tom Spencer
     */
    private static class PropertyTypeCalculator {
        /** The class that owns the properties */
        protected final Class<?> owningClass;
        /** The definition of the owning class */
        protected final BeanDefinition owningDefinition;
        
        public PropertyTypeCalculator(Class<?> owningClass, BeanDefinition definition) {
            this.owningClass = owningClass;
            this.owningDefinition = definition;
        }
        
        /**
         * Call to get the strategy for the property. 
         * 
         * <p>In the base class this only works for simple properties. In
         * the derived classes this should determine if the properties is
         * a reference or child property and override accordingly.
         *  
         * @param name The name of the property
         * @return The strategy for the property
         */
        public PropertyStrategy getPropertyStrategy(String name) {
            PropertyTypeEnum propertyType = getPropertyType(name);
            ObjexFieldType type = getObjexType(name, propertyType);
            return new PropertyStrategy(name, owningDefinition.getPropertyType(name), type, propertyType, getPropertyCharacteristics(name));
        }
        
        /**
         * Helper to get the type of the property
         */
        protected PropertyTypeEnum getPropertyType(String name) {
            PropertyTypeEnum ret = null;
            
            Class<?> cls = owningDefinition.getPropertyType(name);
            
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
            else if( Date.class.equals(cls) ) ret = PropertyTypeEnum.DATE;
            else if( List.class.equals(cls) ) ret = PropertyTypeEnum.LIST;
            else if( Set.class.equals(cls) ) ret = PropertyTypeEnum.SET;
            else if( Map.class.equals(cls) ) ret = PropertyTypeEnum.MAP;
            else if( isValidBeanType(cls) ) ret = PropertyTypeEnum.OBJECT; 
            
            return ret;
        }
        
        /**
         * Helper to determine if the class is valid as a bean type. If
         * using annotations override to ensure this is a value object.
         */
        protected boolean isValidBeanType(Class<?> cls) {
            return true;
        }
        
        /**
         * Helper to determine if this is a valid reference property. If
         * it is a property type enum is returned, null otherwise
         */
        protected PropertyTypeEnum isValidReference(String name, Class<?> elementType) {
            if( elementType == null ) throw new IllegalArgumentException("Cannot determine the element type for property: " + name);
            if( !elementType.isInterface() ) throw new IllegalArgumentException("A child or reference property must be based (element type) on an interface type: " + owningClass);
            
            PropertyTypeEnum ret = null;
            
            Class<?> prop = owningDefinition.getPropertyType(name);
            
            if( prop.equals(elementType) ) ret = PropertyTypeEnum.OBJECT;
            else {
                ret = getPropertyType(name);
                if( ret != PropertyTypeEnum.LIST &&
                        ret != PropertyTypeEnum.SET &&
                        ret != PropertyTypeEnum.MAP ) ret = null;
            }
            
            return ret;
        }
        
        /**
         * Helper to determine the objex type given the property type 
         */
        protected ObjexFieldType getObjexType(String name, PropertyTypeEnum type) {
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
        
        /**
         * Helper determines the element type held by the child or reference property.
         * This must either be List, Set or Map (cannot be derived or classes of these)
         * or it must be directly a reference to a business interface
         */
        protected Class<?> determineElementType(String name) {
            Class<?> ret = null;
            Class<?> propClass = owningDefinition.getPropertyType(name);
            
            if( List.class.equals(propClass) ) {
                Type type = owningDefinition.getPropertyGenericType(name);
                if( type instanceof ParameterizedType &&
                        ((ParameterizedType)type).getActualTypeArguments().length == 1 &&
                        ((ParameterizedType)type).getActualTypeArguments()[0] instanceof Class ) {
                    ret = Class.class.cast(((ParameterizedType)type).getActualTypeArguments()[0]);
                }
            }
            else if( Set.class.equals(propClass) ) {
                Type type = owningDefinition.getPropertyGenericType(name);
                if( type instanceof ParameterizedType &&
                        ((ParameterizedType)type).getActualTypeArguments().length == 1 &&
                        ((ParameterizedType)type).getActualTypeArguments()[0] instanceof Class ) {
                    ret = Class.class.cast(((ParameterizedType)type).getActualTypeArguments()[0]);
                }
            }
            else if( Map.class.equals(propClass) ) {
                Type type = owningDefinition.getPropertyGenericType(name);
                if( type instanceof ParameterizedType &&
                        ((ParameterizedType)type).getActualTypeArguments().length == 2 &&
                        ((ParameterizedType)type).getActualTypeArguments()[1] instanceof Class ) {
                    ret = Class.class.cast(((ParameterizedType)type).getActualTypeArguments()[1]);
                }
            }
            else {
                ret = propClass;
            }
            
            // Final checks
            if( ret != null ) {
                // Make sure not a collection
                if( Collection.class.isAssignableFrom(ret) ) throw new IllegalArgumentException("Cannot use the property [" + name + "] as a child or reference because it must be a business interface or List, Set or Map directly: " + ret);
                if( Map.class.isAssignableFrom(ret) ) throw new IllegalArgumentException("Cannot use the property [" + name + "] as a child or reference because it must be a business interface or List, Set or Map directly: " + ret);
                if( !ret.isInterface() ) throw new IllegalArgumentException("Cann use the property [" + name + "] as it must be an interface to a business object: " + ret);
            }
            
            return ret;
        }
                
        /**
         * Helper determines property characteristics like calculated and
         * transient
         */
        protected PropertyCharacteristic[] getPropertyCharacteristics(String name) {
            // TODO: Calculated if on primary interface there is no setter
            return null;
        }
    }
    
    /**
     * This class derives from the base to look at the annotations
     * on each property
     *
     * @author Tom Spencer
     */
    private static final class AnnotationPropertyTypeCalculator extends PropertyTypeCalculator {
        
        public AnnotationPropertyTypeCalculator(Class<?> owningClass, BeanDefinition definition) {
            super(owningClass, definition);
        }
        
        /**
         * {@inheritDoc}
         * 
         * <p>Overridden to handle child and reference properties first
         */
        @Override
        public PropertyStrategy getPropertyStrategy(String name) {
            ChildProperty child = owningDefinition.getReadAnnotation(name, ChildProperty.class);
            ReferenceProperty ref = child == null ? owningDefinition.getReadAnnotation(name, ReferenceProperty.class) : null;
            
            if( child != null ) {
                PropertyTypeEnum propType = isValidReference(name, child.value());
                return new PropertyStrategy(name, owningDefinition.getPropertyType(name), ObjexFieldType.OWNED_REFERENCE, child.value(), propType, getPropertyCharacteristics(name));
            }
            else if( ref != null ) {
                PropertyTypeEnum propType = isValidReference(name, ref.value());
                return new PropertyStrategy(name, owningDefinition.getPropertyType(name), ObjexFieldType.REFERENCE, ref.value(), propType, getPropertyCharacteristics(name));
            }
            else {
                // boolean marked = owningDefinition.getReadAnnotation(name, Property.class) != null;
                // FUTURE: Ensure other properties are marked!!??
                
                return super.getPropertyStrategy(name);
            }
        }
        
        /**
         * {@inheritDoc}
         * 
         * <p>Overridden to determine from annotations</p>
         */
        @Override
        protected PropertyCharacteristic[] getPropertyCharacteristics(String name) {
            PropertyCharacteristic[] ret = null;
            
            boolean trans = owningDefinition.getReadAnnotation(name, TransientProperty.class) != null;
            boolean calc = owningDefinition.getReadAnnotation(name, CalculatedProperty.class) != null;
            if( !trans && calc ) ret = new PropertyCharacteristic[]{PropertyCharacteristic.PERSISTENT, PropertyCharacteristic.CALCULATED};
            else if( !trans ) ret = new PropertyCharacteristic[]{PropertyCharacteristic.PERSISTENT};
            else if( calc ) ret = new PropertyCharacteristic[]{PropertyCharacteristic.CALCULATED};
            
            return ret;
        }
        
        /**
         * {@inheritDoc}
         * 
         * <p>Overridden to ensure the bean is a ValueObject and marked as such</p>
         */
        @Override
        protected boolean isValidBeanType(Class<?> cls) {
            boolean ret = false;
            if( cls.isAnnotationPresent(ValueObject.class) ) ret = true;
            return ret;
        }
    }
    
    /**
     * This class derives from the base to read and look at a config
     * file holding further information about the properties.
     *
     * @author Tom Spencer
     */
    private static final class ConfigPropertyTypeCalculator extends PropertyTypeCalculator {
        
        public ConfigPropertyTypeCalculator(Class<?> owningClass, BeanDefinition definition) {
            super(owningClass, definition);
        }
        
        @Override
        public PropertyStrategy getPropertyStrategy(String name) {
            // TODO: Check property file for this property
            return super.getPropertyStrategy(name);
        }
    }
    
    private static final class CustomPropertyTypeCalculator extends PropertyTypeCalculator {
        
        /** Holds the properties that are known to be child properties */
        private final String[] childProperties;
        /** Holds the properties that are known to be reference properties */
        private final String[] referenceProperties;
        
        public CustomPropertyTypeCalculator(String[] children, String[] refs, Class<?> owningClass, BeanDefinition definition) {
            super(owningClass, definition);
            this.childProperties = children;
            this.referenceProperties = refs;
        }
        
        @Override
        public PropertyStrategy getPropertyStrategy(String name) {
            if( testExists(name, childProperties) ) {
                Class<?> elementType = determineElementType(name);
                
                PropertyTypeEnum propType = isValidReference(name, elementType);
                return new PropertyStrategy(name, owningDefinition.getPropertyType(name), ObjexFieldType.OWNED_REFERENCE, elementType, propType, getPropertyCharacteristics(name));
            }
            else if( testExists(name, referenceProperties) ) {
                Class<?> elementType = determineElementType(name);
                
                PropertyTypeEnum propType = isValidReference(name, elementType);
                return new PropertyStrategy(name, owningDefinition.getPropertyType(name), ObjexFieldType.REFERENCE, elementType, propType, getPropertyCharacteristics(name));
            }
            else {
                return super.getPropertyStrategy(name);
            }
        }
        
        /**
         * Helper to determine if property exists in an array of property names
         */
        private boolean testExists(String prop, String[] props) {
            if( props == null || props.length == 0 ) return false;
            
            boolean ret = false;
            for( String p : props ) {
                if( p.equals(prop) ) ret = true;
                if( ret ) break;
            }
            
            return ret;
        }
    }
}
