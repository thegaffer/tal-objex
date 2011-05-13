package org.talframework.objexj.object.fields;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.exceptions.ObjectInvalidException;

/**
 * Ensures the owned/child version of our ref map worked correctly.
 *
 * @author Tom Spencer
 */
public class TestObjexChildMap {

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
        
        Map<String, ObjexObj> underTest = new ObjexChildMap<String, ObjexObj>(owningObj);
        assertEquals(0, underTest.size());
        
        assertEquals(null, underTest.put("obj1", objToAdd));
        assertEquals(1, underTest.size());
        assertEquals(refObj1, underTest.get("obj1"));
    }
    
    @Test(expected=ObjectInvalidException.class)
    public void addInitialisedObject() {
        ObjexObj owningObj = mock(ObjexObj.class);
        ObjexObj refObj1 = mock(ObjexObj.class);
        InternalContainer container = mock(InternalContainer.class);
        
        when(owningObj.getContainer()).thenReturn(container);
        when(refObj1.getId()).thenReturn(new DefaultObjexID("Test", 1));
        when(refObj1.getContainer()).thenReturn(container);
        
        Map<String, ObjexObj> underTest = new ObjexChildMap<String, ObjexObj>(owningObj);
        underTest.put("obj2", refObj1);
    }
    
    @Test
    public void remove() {
        // Setup
        ObjexObj owningObj = mock(ObjexObj.class);
        ObjexID id = new DefaultObjexID("test", 1);
        ObjexObj refObj1 = mock(ObjexObj.class);
        InternalContainer container = mock(InternalContainer.class);
        
        when(owningObj.getContainer()).thenReturn(container);
        when(container.getObject(id)).thenReturn(refObj1);
        
        Map<String, ObjexID> existingRefs = new HashMap<String, ObjexID>();
        existingRefs.put("obj1", id);
        
        // Test
        Map<String, ObjexObj> underTest = new ObjexChildMap<String, ObjexObj>(owningObj, existingRefs);
        underTest.remove("obj1");
        
        // Assert
        verify(container).removeObject(refObj1);
        assertEquals(0, underTest.size());
    }
    
    @Test
    public void removeByEntrySet() {
        // Setup
        ObjexObj owningObj = mock(ObjexObj.class);
        ObjexID id = new DefaultObjexID("test", 1);
        ObjexObj refObj1 = mock(ObjexObj.class);
        InternalContainer container = mock(InternalContainer.class);
        
        when(owningObj.getContainer()).thenReturn(container);
        when(container.getObject(id)).thenReturn(refObj1);
        
        Map<String, ObjexID> existingRefs = new HashMap<String, ObjexID>();
        existingRefs.put("obj1", id);
        
        // Test
        Map<String, ObjexObj> underTest = new ObjexChildMap<String, ObjexObj>(owningObj, existingRefs);
        Iterator<Map.Entry<String, ObjexObj>> it = underTest.entrySet().iterator();
        it.next();
        it.remove();
        
        // Assert
        verify(container).removeObject(refObj1);
        assertEquals(0, underTest.size());
    }
}
