/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.objexj.container.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ValidationError;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.container.TransactionCache;
import org.talframework.objexj.container.TransactionCache.ObjectRole;
import org.talframework.objexj.object.DefaultObjexID;

public class TestContainerValidator {
    
    private Mockery context = new JUnit4Mockery();
    
    private Container container = null;
    private TransactionCache cache = null;
    
    private ObjexObj root = null;
    private ObjexObj child1 = null;
    private ObjexObj child2 = null;
    private ObjexObj parent1 = null;
    private ObjexObj child11 = null;
    private ObjexObj child12 = null;
    private ObjexObj parent2 = null;
    private ObjexObj child21 = null;
    private ObjexObj child22 = null;
    private ObjexObj child23 = null;
    
    /** Empty map for new objects in the test */
    private Map<ObjexID, ObjexObjStateBean> newObjects;
    /** Empty map for updated objects in the test */
    private Map<ObjexID, ObjexObjStateBean> updatedObjects;
    /** Empty map for removed objects in the test */
    private Map<ObjexID, ObjexObjStateBean> removedObjects;
    
    @Before
    public void setup() {
        container = context.mock(Container.class);
        cache = context.mock(TransactionCache.class);
        
        final ObjexID rootId = new DefaultObjexID("Root/1");
        final ObjexID child1Id = new DefaultObjexID("Child/1");
        final ObjexID child2Id = new DefaultObjexID("Child/2");
        final ObjexID parent1Id = new DefaultObjexID("Parent/1");
        final ObjexID child11Id = new DefaultObjexID("Child/11");
        final ObjexID child12Id = new DefaultObjexID("Child/12");
        final ObjexID parent2Id = new DefaultObjexID("Parent/2");
        final ObjexID child21Id = new DefaultObjexID("Child/21");
        final ObjexID child22Id = new DefaultObjexID("Child/22");
        final ObjexID child23Id = new DefaultObjexID("Child/23");
        
        root = context.mock(ObjexObj.class, "root");
        child1 = context.mock(ObjexObj.class, "child1");
        child2 = context.mock(ObjexObj.class, "child2");
        parent1 = context.mock(ObjexObj.class, "parent1");
        child11 = context.mock(ObjexObj.class, "child11");
        child12 = context.mock(ObjexObj.class, "child12");
        parent2 = context.mock(ObjexObj.class, "parent2");
        child21 = context.mock(ObjexObj.class, "child21");
        child22 = context.mock(ObjexObj.class, "child22");
        child23 = context.mock(ObjexObj.class, "child23");
        
        context.checking(new Expectations() {{
            allowing(container).getObject(rootId); will(returnValue(root));
            allowing(container).getObject(parent1Id); will(returnValue(parent1));
            allowing(container).getObject(parent2Id); will(returnValue(parent2));
            allowing(container).getObject(child1Id); will(returnValue(child1));
            allowing(container).getObject(child2Id); will(returnValue(child2));
            allowing(container).getObject(child11Id); will(returnValue(child11));
            allowing(container).getObject(child12Id); will(returnValue(child12));
            allowing(container).getObject(child21Id); will(returnValue(child21));
            allowing(container).getObject(child22Id); will(returnValue(child22));
            allowing(container).getObject(child23Id); will(returnValue(child23));
            
            allowing(root).getId(); will(returnValue(rootId));
            allowing(root).getParent(); will(returnValue(null));
            allowing(parent1).getId(); will(returnValue(parent1Id));
            allowing(parent1).getParent(); will(returnValue(root));
            allowing(parent2).getId(); will(returnValue(parent2Id));
            allowing(parent2).getParent(); will(returnValue(root));
            allowing(child1).getId(); will(returnValue(child1Id));
            allowing(child1).getParent(); will(returnValue(root));
            allowing(child2).getId(); will(returnValue(child2Id));
            allowing(child2).getParent(); will(returnValue(root));
            allowing(child11).getId(); will(returnValue(child11Id));
            allowing(child11).getParent(); will(returnValue(parent1));
            allowing(child12).getId(); will(returnValue(child12Id));
            allowing(child12).getParent(); will(returnValue(parent1));
            allowing(child21).getId(); will(returnValue(child21Id));
            allowing(child21).getParent(); will(returnValue(parent2));
            allowing(child22).getId(); will(returnValue(child22Id));
            allowing(child22).getParent(); will(returnValue(parent2));
            allowing(child23).getId(); will(returnValue(child23Id));
            allowing(child23).getParent(); will(returnValue(parent2));
        }});
        
        newObjects = new HashMap<ObjexID, ObjexObjStateBean>();
        updatedObjects  = new HashMap<ObjexID, ObjexObjStateBean>();
        removedObjects  = new HashMap<ObjexID, ObjexObjStateBean>();
    }

    /**
     * This basic test ensures we validate both objects
     * inside that need to be validated (1 new and 1 old)
     * and then validate against the parent which is not
     * changing.
     */
    @Test
    public void basic() {
        newObjects.put(child1.getId(), null);
        updatedObjects.put(child2.getId(), null);
        
        context.checking(new Expectations() {{
            oneOf(cache).getObjects(ObjectRole.NEW); will(returnValue(newObjects));
            oneOf(cache).getObjects(ObjectRole.UPDATED); will(returnValue(updatedObjects));
            
            oneOf(child1).validate(with(any(ValidationRequest.class)));
            oneOf(child2).validate(with(any(ValidationRequest.class)));
            oneOf(child1).validate(with(any(ValidationRequest.class)));
            oneOf(child2).validate(with(any(ValidationRequest.class)));
            oneOf(root).validate(with(any(ValidationRequest.class)));
        }});
        
        ContainerValidator validator = new ContainerValidator(container, cache);
        validator.validate(null);
        
        context.assertIsSatisfied();
    }
    
    /**
     * This ensures that even if the parent changes it still
     * gets a parent validation call
     */
    @Test
    public void parentAndChild() {
        updatedObjects.put(root.getId(), null);
        updatedObjects.put(child2.getId(), null);
        
        context.checking(new Expectations() {{
            oneOf(cache).getObjects(ObjectRole.NEW); will(returnValue(null));
            oneOf(cache).getObjects(ObjectRole.UPDATED); will(returnValue(updatedObjects));
            
            oneOf(child2).validate(with(any(ValidationRequest.class)));
            oneOf(child2).validate(with(any(ValidationRequest.class)));
            oneOf(root).validate(with(any(ValidationRequest.class)));
            oneOf(root).validate(with(any(ValidationRequest.class)));
            oneOf(root).validate(with(any(ValidationRequest.class)));
        }});
        
        ContainerValidator validator = new ContainerValidator(container, cache);
        validator.validate(null);
        
        context.assertIsSatisfied();
    }
    
    /**
     * This ensures that all parents all the way back to
     * the root get validated
     */
    @Test
    public void multiParent() {
        updatedObjects.put(child11.getId(), null);
        updatedObjects.put(child21.getId(), null);
        
        context.checking(new Expectations() {{
            oneOf(cache).getObjects(ObjectRole.NEW); will(returnValue(null));
            oneOf(cache).getObjects(ObjectRole.UPDATED); will(returnValue(updatedObjects));
            
            oneOf(child11).validate(with(any(ValidationRequest.class)));
            oneOf(child11).validate(with(any(ValidationRequest.class)));
            oneOf(child21).validate(with(any(ValidationRequest.class)));
            oneOf(child21).validate(with(any(ValidationRequest.class)));
            oneOf(parent2).validate(with(any(ValidationRequest.class)));
            oneOf(parent1).validate(with(any(ValidationRequest.class)));
            oneOf(root).validate(with(any(ValidationRequest.class)));
        }});
        
        ContainerValidator.validate(container, cache, null);
        
        context.assertIsSatisfied();
    }
    
    /**
     * This test just add's all children in as a combination of
     * new and updated objects
     */
    @Test
    public void testAllChildren() {
        newObjects.put(child2.getId(), null);
        newObjects.put(child23.getId(), null);
        updatedObjects.put(child1.getId(), null);
        updatedObjects.put(child11.getId(), null);
        updatedObjects.put(child12.getId(), null);
        updatedObjects.put(child21.getId(), null);
        updatedObjects.put(child22.getId(), null);
        updatedObjects.put(parent2.getId(), null);
        
        context.checking(new Expectations() {{
            oneOf(cache).getObjects(ObjectRole.NEW); will(returnValue(newObjects));
            oneOf(cache).getObjects(ObjectRole.UPDATED); will(returnValue(updatedObjects));
            
            exactly(2).of(child1).validate(with(any(ValidationRequest.class)));
            exactly(2).of(child2).validate(with(any(ValidationRequest.class)));
            exactly(2).of(child11).validate(with(any(ValidationRequest.class)));
            exactly(2).of(child12).validate(with(any(ValidationRequest.class)));
            exactly(2).of(child21).validate(with(any(ValidationRequest.class)));
            exactly(2).of(child22).validate(with(any(ValidationRequest.class)));
            exactly(2).of(child23).validate(with(any(ValidationRequest.class)));
            exactly(3).of(parent2).validate(with(any(ValidationRequest.class)));
            oneOf(parent1).validate(with(any(ValidationRequest.class)));
            oneOf(root).validate(with(any(ValidationRequest.class)));
        }});
        
        ContainerValidator.validate(container, cache, null);
        
        context.assertIsSatisfied();
    }
    
    /**
     * This tests that existing errors are merged in against
     * the current errors unless the object is removed.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testMerging() {
        updatedObjects.put(child1.getId(), null);
        removedObjects.put(child11.getId(), null);
        
        final ValidationRequest oldRequest = context.mock(ValidationRequest.class);
        final List<ValidationError> child1Errors = context.mock(List.class, "child1Errors");
        final List<ValidationError> child2Errors = context.mock(List.class, "child2Errors");
        final Map<ObjexID, List<ValidationError>> oldErrors = new HashMap<ObjexID, List<ValidationError>>();
        oldErrors.put(child1.getId(), child1Errors);
        oldErrors.put(child2.getId(), child2Errors);
        
        context.checking(new Expectations() {{
            oneOf(cache).getObjects(ObjectRole.NEW); will(returnValue(null));
            oneOf(cache).getObjects(ObjectRole.UPDATED); will(returnValue(updatedObjects));
            oneOf(cache).findObject(child2.getId(), ObjectRole.REMOVED); will(returnValue(null));
            
            oneOf(oldRequest).getErrorMap(); will(returnValue(oldErrors));
            
            exactly(2).of(child1).validate(with(any(ValidationRequest.class)));
            oneOf(root).validate(with(any(ValidationRequest.class)));
            
            oneOf(child2Errors).size(); will(returnValue(1));
        }});
        
        ValidationRequest result = ContainerValidator.validate(container, cache, oldRequest);
        
        // Test the errors remoain for child2, but not for child1
        Assert.assertNotNull(result);
        Assert.assertTrue(result.hasErrors(child2.getId()));
        Assert.assertFalse(result.hasErrors(child1.getId()));
        
        context.assertIsSatisfied();
    }
}
