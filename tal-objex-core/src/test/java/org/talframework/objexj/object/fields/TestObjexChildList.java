package org.talframework.objexj.object.fields;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.exceptions.ObjectInvalidException;

/**
 * Tests the child list implementation. As this class extends from
 * ref list we only test the extra parts.
 *
 * @author Tom Spencer
 */
public class TestObjexChildList {

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
        
        List<ObjexObj> underTest = new ObjexChildList<ObjexObj>(owningObj);
        assertEquals(0, underTest.size());
        
        assertTrue(underTest.add(objToAdd));
        assertEquals(1, underTest.size());
        assertEquals(refObj1, underTest.get(0));
    }
    
    @Test(expected=ObjectInvalidException.class)
    public void addInitialisedObject() {
        ObjexObj owningObj = mock(ObjexObj.class);
        ObjexObj refObj1 = mock(ObjexObj.class);
        InternalContainer container = mock(InternalContainer.class);
        
        when(owningObj.getContainer()).thenReturn(container);
        when(refObj1.getId()).thenReturn(new DefaultObjexID("Test", 1));
        when(refObj1.getContainer()).thenReturn(container);
        
        List<ObjexObj> underTest = new ObjexChildList<ObjexObj>(owningObj);
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
        
        List<ObjexID> existingRefs = new ArrayList<ObjexID>();
        existingRefs.add(id);
        
        List<ObjexObj> underTest = new ObjexChildList<ObjexObj>(owningObj, existingRefs);
        
        Iterator<ObjexObj> it = underTest.iterator();
        it.next();
        it.remove();
        
        verify(container).removeObject(refObj1);
        assertEquals(0, underTest.size());
    }
}
