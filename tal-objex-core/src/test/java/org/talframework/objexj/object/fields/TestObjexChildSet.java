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
