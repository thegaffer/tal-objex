package org.talframework.objexj.object.fields;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.exceptions.ObjectInvalidException;
import org.talframework.objexj.object.testmodel.pojo.Category;

/**
 * This class tests the ObjexRefSet class
 *
 * @author Tom Spencer
 */
public class TestObjexRefSet {

    /**
     * Tests the majority of the happy path elements
     */
    @Test
    public void basic() {
        ObjexObj owningObj = mock(ObjexObj.class);
        ObjexObj refObj1 = mock(ObjexObj.class);
        ObjexObj refObj2 = mock(ObjexObj.class);
        InternalContainer container = mock(InternalContainer.class);
        
        when(owningObj.getContainer()).thenReturn(container);
        when(refObj1.getId()).thenReturn(new DefaultObjexID("Test", 1));
        when(refObj1.getContainer()).thenReturn(container);
        when(refObj2.getId()).thenReturn(new DefaultObjexID("Test", 2));
        when(refObj2.getContainer()).thenReturn(container);
        
        ObjexRefSet<ObjexObj> underTest = new ObjexRefSet<ObjexObj>(owningObj);
        assertEquals(0, underTest.size());
        
        // Ensure we can add objects
        underTest.add(refObj1);
        verify(refObj1).getId();
        verify(refObj1).getContainer();
        assertEquals(1, underTest.size());
        
        underTest.add(refObj2);
        verify(refObj2).getId();
        verify(refObj2).getContainer();
        assertEquals(2, underTest.size());
        
        // Ensure we can use the iterator
        Iterator<ObjexObj> it = underTest.iterator();
        assertTrue(it.hasNext());
        ObjexObj first = it.next();
        assertTrue(first == refObj1 || first == refObj2);
        assertTrue(it.hasNext());
        ObjexObj second = it.next();
        assertTrue(second == refObj1 || second == refObj2);
        assertTrue(first != second);
        assertFalse(it.hasNext());
        
        // Ensure we can remove ok
        it.remove();
        assertEquals(1, underTest.size());
    }
    
    /**
     * Ensure we go and get objects if don't have the cached object
     */
    @Test
    public void existing() {
        ObjexObj owningObj = mock(ObjexObj.class);
        ObjexID id = new DefaultObjexID("test", 1);
        ObjexObj refObj1 = mock(ObjexObj.class);
        InternalContainer container = mock(InternalContainer.class);
        
        when(owningObj.getContainer()).thenReturn(container);
        when(container.getObject(id)).thenReturn(refObj1);
        
        Set<ObjexID> existingRefs = new HashSet<ObjexID>();
        existingRefs.add(id);
        
        ObjexRefSet<ObjexObj> underTest = new ObjexRefSet<ObjexObj>(owningObj, existingRefs);
        assertEquals(refObj1, underTest.iterator().next());
        verify(container).getObject(id);
    }
    
    /**
     * Ensures we cannot add anything that is not an ObjexObj
     */
    @Test(expected=ObjectInvalidException.class)
    @Ignore("Need category bean to compile first!")
    public void invalidObject() {
        ObjexObj owningObj = mock(ObjexObj.class);
        InternalContainer container = mock(InternalContainer.class);
        when(owningObj.getContainer()).thenReturn(container);
        
        ObjexRefSet<Category> underTest = new ObjexRefSet<Category>(owningObj);
        underTest.add(new Category());
    }
    
    /**
     * Ensures we cannot add the same reference twice
     */
    @Test(expected=ObjectInvalidException.class)
    public void duplicateObject() {
        ObjexObj owningObj = mock(ObjexObj.class);
        ObjexObj refObj1 = mock(ObjexObj.class);
        InternalContainer container = mock(InternalContainer.class);
        
        when(owningObj.getContainer()).thenReturn(container);
        when(refObj1.getId()).thenReturn(new DefaultObjexID("Test", 1));
        when(refObj1.getContainer()).thenReturn(container);
        
        ObjexRefSet<ObjexObj> underTest = new ObjexRefSet<ObjexObj>(owningObj);
        underTest.add(refObj1);
        underTest.add(refObj1);
    }
    
    /**
     * Ensures we cannot added an object from a different container.
     * NOTE: This should be possible, so is only tested against this
     * set as a reminder that when we allow foreign references we need
     * to update the ObjexRefList/Set/Map code.
     */
    @Test(expected=ObjectInvalidException.class)
    public void crossContainer() {
        ObjexObj owningObj = mock(ObjexObj.class);
        ObjexObj refObj1 = mock(ObjexObj.class);
        InternalContainer container = mock(InternalContainer.class);
        InternalContainer container1 = mock(InternalContainer.class);
        
        when(owningObj.getContainer()).thenReturn(container);
        when(refObj1.getId()).thenReturn(new DefaultObjexID("Test", 1));
        when(refObj1.getContainer()).thenReturn(container1);
        
        ObjexRefSet<ObjexObj> underTest = new ObjexRefSet<ObjexObj>(owningObj);
        underTest.add(refObj1);
    }
}
