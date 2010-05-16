package org.tpspencer.tal.objexj.sample.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.Assert;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * Base class for testing simple bean classes
 * 
 * @author Tom Spencer
 */
public abstract class BeanTestCase {
    
    /** Holds the bean class */
    private final Class<?> bean;
    
    public BeanTestCase(Class<?> bean) {
        this.bean = bean;
    }

    /**
     * Ensure we can get/set every property and
     * when we get is the same we set!
     */
    public void testAccessorsAndMutators() {
        BeanWrapper wrapper = new BeanWrapperImpl(bean);
        
        PropertyDescriptor[] props = wrapper.getPropertyDescriptors();
        for( int i = 0 ; i < props.length ; i++ ) {
            if( props[i].getWriteMethod() == null ) continue;
            Object val = getSuitableValue(props[i].getPropertyType(), i);
            wrapper.setPropertyValue(props[i].getName(), val);
            
            if( props[i].getReadMethod() == null ) continue;
            Object val2 = wrapper.getPropertyValue(props[i].getName());
            Assert.assertEquals(val, val2);
        }
    }
    
    /**
     * Ensure two beans setup with same value
     * have same hash code and two that are
     * different do not. Ideally do this against
     * every property
     */
    public void testHashCode() {
        BeanWrapper wrapper1 = new BeanWrapperImpl(bean);
        BeanWrapper wrapper2 = new BeanWrapperImpl(bean);
        BeanWrapper wrapper3 = new BeanWrapperImpl(bean);
        
        PropertyDescriptor[] props = wrapper1.getPropertyDescriptors();
        for( int i = 0 ; i < props.length ; i++ ) {
            if( props[i].getWriteMethod() == null ) continue;
            Object val = getSuitableValue(props[i].getPropertyType(), i);
            wrapper1.setPropertyValue(props[i].getName(), val);
            wrapper2.setPropertyValue(props[i].getName(), val);
            
            Assert.assertTrue(wrapper1.getWrappedInstance().hashCode() == wrapper2.getWrappedInstance().hashCode());
            Assert.assertFalse(wrapper1.getWrappedInstance().hashCode() == wrapper3.getWrappedInstance().hashCode());
            
            wrapper3.setPropertyValue(props[i].getName(), val);
        }
    }
    
    /**
     * Ensure two beans setup with the same values
     * are equals, and two that are not are not
     * equal. Ideally test this against each and
     * every property
     */
    public void testEquals() {
        BeanWrapper wrapper1 = new BeanWrapperImpl(bean);
        BeanWrapper wrapper2 = new BeanWrapperImpl(bean);
        BeanWrapper wrapper3 = new BeanWrapperImpl(bean);
        
        PropertyDescriptor[] props = wrapper1.getPropertyDescriptors();
        for( int i = 0 ; i < props.length ; i++ ) {
            if( props[i].getWriteMethod() == null ) continue;
            Object val = getSuitableValue(props[i].getPropertyType(), i);
            wrapper1.setPropertyValue(props[i].getName(), val);
            wrapper2.setPropertyValue(props[i].getName(), val);
            
            Assert.assertTrue(wrapper1.getWrappedInstance().equals(wrapper2.getWrappedInstance()));
            Assert.assertFalse(wrapper1.getWrappedInstance().equals(wrapper3.getWrappedInstance()));
            
            wrapper3.setPropertyValue(props[i].getName(), val);
        }
    }
    
    /**
     * We don't care what it is but we do not expect
     * the bean to have the basic toString output
     */
    public void testToString() {
        // TODO:
    }
    
    /**
     * Helper to get a suitable value given a property
     * type.
     * 
     * @param type The type of the property
     * @param seed The seed value
     * @return The value
     */
    private Object getSuitableValue(Class<?> type, int seed) {
        try {
            Object val = null;
            
            if( String.class.isAssignableFrom(type) ) val = "Test" + seed;
            else if( Date.class.isAssignableFrom(type) ) val = new Date();
            else if( Double.class.isAssignableFrom(type) ) val = new Double(seed);
            else if( double.class.equals(type) ) val = (double)seed;
            else if( Float.class.isAssignableFrom(type) ) val = new Float(seed);
            else if( float.class.equals(type) ) val = (float)seed;
            else if( Long.class.isAssignableFrom(type) ) val = new Long(seed);
            else if( long.class.equals(type) ) val = (long)seed;
            else if( Integer.class.isAssignableFrom(type) ) val = new Integer(seed);
            else if( int.class.equals(type) ) val = (int)seed;
            else if( Short.class.isAssignableFrom(type) ) val = new Short((short)seed);
            else if( short.class.equals(type) ) val = (short)seed;
            else if( Character.class.isAssignableFrom(type) ) val = new Character((char)seed);
            else if( char.class.equals(type) ) val = (char)seed;
            else if( Boolean.class.isAssignableFrom(type) ) val = new Boolean(seed != 0);
            else if( boolean.class.equals(type) ) val = (seed != 0);
            
            else if( type.isArray() ) val = Array.newInstance(type.getComponentType(), 1);
            else if( SortedSet.class.isAssignableFrom(type) ) val = new TreeSet<Object>();
            else if( Set.class.isAssignableFrom(type) ) val = new HashSet<Object>();
            else if( List.class.isAssignableFrom(type) ) val = new ArrayList<Object>();
            else if( Map.class.isAssignableFrom(type) ) val = new HashMap<Object, Object>();

            // try and create instance
            else type.newInstance();
            
            return val;
        }
        catch( Exception e ) {
            throw new IllegalArgumentException("Cannot create value for property: " + e);
        }
    }
}
