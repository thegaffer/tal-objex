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
package org.talframework.objexj.container.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ValidationError;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.container.ContainerObjectCache;
import org.talframework.objexj.container.ContainerObjectCache.CacheState;

public class TestContainerValidator {
    
    /**
     * This basic test ensures we validate both objects
     * inside that need to be validated (1 new and 1 old)
     * and then validate against the parent which is not
     * changing.
     */
    @Test
    public void basic() {
        ContainerObjectCache cache = mock(ContainerObjectCache.class);
        ObjexObj root = mock(ObjexObj.class);
        ObjexObj child1 = mock(ObjexObj.class);
        ObjexObj child2 = mock(ObjexObj.class);
        
        Set<ObjexObj> changedOrNew = new HashSet<ObjexObj>();
        changedOrNew.add(child1);
        changedOrNew.add(child2);
        
        when(child1.getParent()).thenReturn(root);
        when(child2.getParent()).thenReturn(root);
        when(root.getId()).thenReturn(new DefaultObjexID("Root", 1));
        when(cache.getObjects(CacheState.NEWORCHANGED)).thenReturn(changedOrNew);
        
        ContainerValidator validator = new ContainerValidator(cache);
        validator.validate(null);
        
        verify(child1, times(2)).validate(any(ValidationRequest.class));
        verify(child2, times(2)).validate(any(ValidationRequest.class));
        verify(root, times(1)).validate(any(ValidationRequest.class));
    }
    
    /**
     * This ensures that even if the parent changes it still
     * gets a parent validation call
     */
    @Test
    public void parentAndChild() {
        ContainerObjectCache cache = mock(ContainerObjectCache.class);
        ObjexObj root = mock(ObjexObj.class);
        ObjexObj child2 = mock(ObjexObj.class);
        
        Set<ObjexObj> changedOrNew = new HashSet<ObjexObj>();
        changedOrNew.add(root);
        changedOrNew.add(child2);
        
        when(child2.getParent()).thenReturn(root);
        when(root.getId()).thenReturn(new DefaultObjexID("Root", 1));
        when(cache.getObjects(CacheState.NEWORCHANGED)).thenReturn(changedOrNew);
        
        ContainerValidator validator = new ContainerValidator(cache);
        validator.validate(null);
        
        verify(child2, times(2)).validate(any(ValidationRequest.class));
        verify(root, times(3)).validate(any(ValidationRequest.class));
    }
    
    /**
     * This ensures that all parents all the way back to
     * the root get validated
     */
    @Test
    public void multiParent() {
        ContainerObjectCache cache = mock(ContainerObjectCache.class);
        ObjexObj root = mock(ObjexObj.class);
        ObjexObj parent1 = mock(ObjexObj.class);
        ObjexObj parent2 = mock(ObjexObj.class);
        ObjexObj child11 = mock(ObjexObj.class);
        ObjexObj child21 = mock(ObjexObj.class);
        
        Set<ObjexObj> changedOrNew = new HashSet<ObjexObj>();
        changedOrNew.add(child11);
        changedOrNew.add(child21);
        
        when(parent1.getParent()).thenReturn(root);
        when(child11.getParent()).thenReturn(parent1);
        when(parent2.getParent()).thenReturn(root);
        when(child21.getParent()).thenReturn(parent2);
        when(root.getId()).thenReturn(new DefaultObjexID("Root", 1));
        when(parent1.getId()).thenReturn(new DefaultObjexID("Parent", 1));
        when(parent2.getId()).thenReturn(new DefaultObjexID("Parent", 2));
        when(cache.getObjects(CacheState.NEWORCHANGED)).thenReturn(changedOrNew);
        
        ContainerValidator validator = new ContainerValidator(cache);
        validator.validate(null);
        
        verify(child11, times(2)).validate(any(ValidationRequest.class));
        verify(child21, times(2)).validate(any(ValidationRequest.class));
        verify(parent1, times(1)).validate(any(ValidationRequest.class));
        verify(parent2, times(1)).validate(any(ValidationRequest.class));
        verify(root, times(1)).validate(any(ValidationRequest.class));
    }
    
    /**
     * This test just add's all children in as a combination of
     * new and updated objects
     */
    @Test
    public void testAllChildren() {
        ContainerObjectCache cache = mock(ContainerObjectCache.class);
        ObjexObj root = mock(ObjexObj.class);
        ObjexObj child1 = mock(ObjexObj.class);
        ObjexObj child2 = mock(ObjexObj.class);
        ObjexObj parent1 = mock(ObjexObj.class);
        ObjexObj parent2 = mock(ObjexObj.class);
        ObjexObj child11 = mock(ObjexObj.class);
        ObjexObj child12 = mock(ObjexObj.class);
        ObjexObj child21 = mock(ObjexObj.class);
        ObjexObj child22 = mock(ObjexObj.class);
        ObjexObj child23 = mock(ObjexObj.class);
        
        Set<ObjexObj> changedOrNew = new HashSet<ObjexObj>();
        changedOrNew.add(parent2);
        changedOrNew.add(child1);
        changedOrNew.add(child11);
        changedOrNew.add(child12);
        changedOrNew.add(child2);
        changedOrNew.add(child21);
        changedOrNew.add(child22);
        changedOrNew.add(child23);
        
        when(child1.getParent()).thenReturn(root);
        when(child11.getParent()).thenReturn(parent1);
        when(child12.getParent()).thenReturn(parent1);
        when(child2.getParent()).thenReturn(root);
        when(child21.getParent()).thenReturn(parent2);
        when(child22.getParent()).thenReturn(parent2);
        when(child23.getParent()).thenReturn(parent2);
        when(root.getId()).thenReturn(new DefaultObjexID("Root", 1));
        when(parent1.getId()).thenReturn(new DefaultObjexID("Parent", 1));
        when(parent2.getId()).thenReturn(new DefaultObjexID("Parent", 2));
        when(cache.getObjects(CacheState.NEWORCHANGED)).thenReturn(changedOrNew);
        
        ContainerValidator validator = new ContainerValidator(cache);
        validator.validate(null);
        
        verify(child11, times(2)).validate(any(ValidationRequest.class));
        verify(child12, times(2)).validate(any(ValidationRequest.class));
        verify(child21, times(2)).validate(any(ValidationRequest.class));
        verify(child22, times(2)).validate(any(ValidationRequest.class));
        verify(child23, times(2)).validate(any(ValidationRequest.class));
        verify(child1, times(2)).validate(any(ValidationRequest.class));
        verify(child2, times(2)).validate(any(ValidationRequest.class));
        verify(parent1, times(1)).validate(any(ValidationRequest.class));
        verify(parent2, times(3)).validate(any(ValidationRequest.class));
        verify(root, times(1)).validate(any(ValidationRequest.class));
    }
    
    /**
     * This tests that existing errors are merged in against
     * the current errors unless the object is removed.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testMerging() {
        ContainerObjectCache cache = mock(ContainerObjectCache.class);
        ObjexObj root = mock(ObjexObj.class);
        ObjexObj parent1 = mock(ObjexObj.class);
        ObjexObj child1 = mock(ObjexObj.class);
        ObjexObj child2 = mock(ObjexObj.class);
        ObjexObj child11 = mock(ObjexObj.class);
        
        Set<ObjexObj> changedOrNew = new HashSet<ObjexObj>();
        changedOrNew.add(child11);
        changedOrNew.add(child1);
        
        when(parent1.getParent()).thenReturn(root);
        when(child1.getParent()).thenReturn(root);
        when(child11.getParent()).thenReturn(parent1);
        when(root.getId()).thenReturn(new DefaultObjexID("Root", 1));
        when(parent1.getId()).thenReturn(new DefaultObjexID("Parent", 1));
        when(child1.getId()).thenReturn(new DefaultObjexID("Child", 1));
        when(child2.getId()).thenReturn(new DefaultObjexID("Child", 2));
        when(cache.getObjects(CacheState.NEWORCHANGED)).thenReturn(changedOrNew);
        
        // Old errors
        ValidationRequest oldRequest = mock(ValidationRequest.class);
        final List<ValidationError> child1Errors = mock(List.class, "child1Errors");
        final List<ValidationError> child2Errors = mock(List.class, "child2Errors");
        final Map<ObjexID, List<ValidationError>> oldErrors = new HashMap<ObjexID, List<ValidationError>>();
        oldErrors.put(child1.getId(), child1Errors);
        oldErrors.put(child2.getId(), child2Errors);
        when(oldRequest.getErrorMap()).thenReturn(oldErrors);
        when(child2Errors.size()).thenReturn(1);
        
        ContainerValidator validator = new ContainerValidator(cache);
        ValidationRequest result = validator.validate(oldRequest);
        assertNotNull(result);
        assertTrue(result.hasErrors(child2.getId()));
        assertFalse(result.hasErrors(child1.getId()));
        
        verify(child1, times(2)).validate(any(ValidationRequest.class));
        verify(child11, times(2)).validate(any(ValidationRequest.class));
        verify(parent1, times(1)).validate(any(ValidationRequest.class));
        verify(root, times(1)).validate(any(ValidationRequest.class));
    }
}
