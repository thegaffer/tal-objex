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
package org.talframework.objexj.object.fields;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.exceptions.ObjectInvalidException;
import org.talframework.objexj.object.testmodel.pojo.Category;

/**
 * Tests the reference list implementation
 *
 * @author Tom Spencer
 */
public class TestObjexRefList {

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
        
        List<ObjexObj> underTest = new ObjexRefList<ObjexObj>(owningObj);
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
        assertEquals(refObj1, it.next());
        assertTrue(it.hasNext());
        assertEquals(refObj2, it.next());
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
        
        List<ObjexID> existingRefs = new ArrayList<ObjexID>();
        existingRefs.add(id);
        
        List<ObjexObj> underTest = new ObjexRefList<ObjexObj>(owningObj, existingRefs);
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
        
        List<Category> underTest = new ObjexRefList<Category>(owningObj);
        underTest.add(new Category());
    }
}
