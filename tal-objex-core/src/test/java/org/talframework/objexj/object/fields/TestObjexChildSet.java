package org.talframework.objexj.object.fields;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.exceptions.ObjectInvalidException;

/**
 * Tests the Objex child set implementation. As {@link ObjexChildSet} derives
 * from {@link ObjexRefSet}, only tests for the extra functionality are 
 * included.
 *
 * @author Tom Spencer
 */
public class TestObjexChildSet {

    @Test
    public void add() {
        ObjexObj owningObj = mock(ObjexObj.class);
        ObjexObj refObj1 = mock(ObjexObj.class);
        ObjexObj objToAdd = mock(ObjexObj.class);
        InternalContainer container = mock(InternalContainer.class);
        
        when(owningObj.getContainer()).thenReturn(container);
        when(refObj1.getId()).thenReturn(new DefaultObjexID("Test", 1));
        when(refObj1.getContainer()).thenReturn(container);
        when(container.createObject(owningObj, objToAdd)).thenReturn(refObj1);
        
        Set<ObjexObj> underTest = new ObjexChildSet<ObjexObj>(owningObj);
        assertEquals(0, underTest.size());
        
        assertTrue(underTest.add(objToAdd));
        assertEquals(1, underTest.size());
        assertEquals(refObj1, underTest.iterator().next());
    }
    
    @Test(expected=ObjectInvalidException.class)
    public void addInitialisedObject() {
        ObjexObj owningObj = mock(ObjexObj.class);
        ObjexObj refObj1 = mock(ObjexObj.class);
        InternalContainer container = mock(InternalContainer.class);
        
        when(owningObj.getContainer()).thenReturn(container);
        when(refObj1.getId()).thenReturn(new DefaultObjexID("Test", 1));
        when(refObj1.getContainer()).thenReturn(container);
        
        Set<ObjexObj> underTest = new ObjexChildSet<ObjexObj>(owningObj);
        underTest.add(refObj1);
    }
    
    @Test
    public void remove() {
        ObjexObj owningObj = mock(ObjexObj.class);
        ObjexID id = new DefaultObjexID("test", 1);
        ObjexObj refObj1 = mock(ObjexObj.class);
        InternalContainer container = mock(InternalContainer.class);
        
        when(owningObj.getContainer()).thenReturn(container);
        when(container.getObject(id)).thenReturn(refObj1);
        
        Set<ObjexID> existingRefs = new HashSet<ObjexID>();
        existingRefs.add(id);
        
        Set<ObjexObj> underTest = new ObjexChildSet<ObjexObj>(owningObj, existingRefs);
        
        Iterator<ObjexObj> it = underTest.iterator();
        it.next();
        it.remove();
        
        verify(container).removeObject(refObj1);
        assertEquals(0, underTest.size());
    }
}
