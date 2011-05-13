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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.Event;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.QueryRequest;
import org.talframework.objexj.QueryResult;
import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerObjectCache;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.events.EventHandler;
import org.talframework.objexj.object.testmodel.api.ICategory;
import org.talframework.objexj.query.Query;

/**
 * This class tests our simple container
 *
 * @author Tom Spencer
 */
public class TestSimpleContainer {
    
    private ContainerStrategy strategy = null;
    private ObjectStrategy objectStrategy = null;
    private ContainerMiddleware middleware = null;
    private ContainerObjectCache cache = null;

    /**
     * Sets up the main collaborators including their
     * default behaviour.
     */
    @Before
    public void setup() {
        strategy = mock(ContainerStrategy.class);
        middleware = mock(ContainerMiddleware.class);
        cache = mock(ContainerObjectCache.class);
        
        objectStrategy = mock(ObjectStrategy.class);
        ObjexObj root = mock(ObjexObj.class);
        ObjexObj obj1 = mock(ObjexObj.class);
        
        when(root.getId()).thenReturn(new DefaultObjexID("Test", 1));
        when(obj1.getId()).thenReturn(new DefaultObjexID("Test", 1234));
        when(obj1.getParentId()).thenReturn(new DefaultObjexID("Test", 1));
        
        when(middleware.init(any(Container.class))).thenReturn(cache);
        
        when(strategy.getContainerName()).thenReturn("TestContainer");
        when(strategy.getRootObjectID()).thenReturn(new DefaultObjexID("Test", 1));
        when(strategy.getObjectStrategy("Test")).thenReturn(objectStrategy);
        when(objectStrategy.createInstance(any(InternalContainer.class), any(ObjexID.class), eq(new DefaultObjexID("Test", 1)), any(Object.class))).thenReturn(root);
        when(objectStrategy.createInstance(any(InternalContainer.class), any(ObjexID.class), eq(new DefaultObjexID("Test", 1234)), any(Object.class))).thenReturn(obj1);
        
        when(middleware.exists(any(ObjexID.class), eq(false))).thenReturn(true);
        when(middleware.loadObject(root)).thenReturn(root);
        when(middleware.loadObject(obj1)).thenReturn(obj1);
    }

    /**
     * Ensures we can create a container, get it's basic properties
     * and then get an object from it
     */
    @Test
    public void basic() {
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, false);
        
        Assert.assertEquals("test", container.getId());
        Assert.assertEquals("TestContainer", container.getType());
        
        Assert.assertNotNull(container.getRootObject());
        Assert.assertNotNull(container.getObject("Test/1234"));
        Assert.assertEquals(container.getObject("Test/1234"), container.getObject("Test/1234"));
        Assert.assertFalse(container.isOpen());
    }
    
    /**
     * This ensures if we ask for a null object we get null.
     * Possibly this should be an exception, but I've decided not
     * for now - you get what you ask for.
     */
    @Test
    public void nullIdObject() {
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, false);
        Assert.assertNull(container.getObject(null));
    }
    
    /**
     * Ensures the container calls getBehaviour appropriately
     */
    @Test
    public void getObjectBehaviour() {
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, false);
        
        ObjexObj obj = mock(ObjexObj.class);
        when(obj.getId()).thenReturn(new DefaultObjexID("Test", 2));
        when(objectStrategy.createInstance(any(InternalContainer.class), any(ObjexID.class), eq(new DefaultObjexID("Test", 2)), any())).thenReturn(obj);
        when(middleware.loadObject(obj)).thenReturn(obj);
        
        ICategory behaviour = mock(ICategory.class);
        when(obj.getBehaviour(ICategory.class)).thenReturn(behaviour);
        
        Assert.assertNotNull(container.getObject("Test/2", ICategory.class));
        verify(obj).getBehaviour(ICategory.class);
    }
    
    /**
     * Ensures we fail if we try and do something editable and
     * the container is not open
     */
    @Test(expected=IllegalStateException.class)
    public void failIfNotOpen() {
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, false);
        container.saveContainer();
    }
    
    /**
     * Ensures we can execute a query
     */
    @Test
    public void executeQuery() {
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, false);
        
        QueryRequest request = mock(QueryRequest.class);
        Query query = mock(Query.class);
        QueryResult result = mock(QueryResult.class);
        
        when(request.getName()).thenReturn("testQuery");
        when(strategy.getQuery("testQuery")).thenReturn(query);
        when(query.execute(container, strategy, request)).thenReturn(result);
        
        Assert.assertEquals(result, container.executeQuery(request));
        verify(query, times(1)).execute(container, strategy, request);
    }
    
    /**
     * Ensures we can fire off an event
     */
    @Test
    public void processEvent() {
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, false);
        
        Event event = mock(Event.class);
        EventHandler handler = mock(EventHandler.class);
        
        when(event.getEventName()).thenReturn("testEvent");
        when(strategy.getEventHandler("testEvent")).thenReturn(handler);
        
        container.processEvent(event);
        verify(handler, times(1)).execute(container, event);
    }

    /**
     * Ensures we can open a container and then close it
     *//*
    @Test
    public void openContainer() {
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, false);
        final TransactionCache cache = context.mock(TransactionCache.class);
        
        context.checking(new Expectations() {{
            oneOf(middleware).open(); will(returnValue(cache));
            oneOf(middleware).clear();
            oneOf(middleware).isNew(); will(returnValue(true));
        }});
        
        Assert.assertFalse(container.isOpen());
        container.openContainer();
        Assert.assertTrue(container.isOpen());
        Assert.assertTrue(container.isNew());
        
        container.closeContainer();
        context.assertIsSatisfied();
    }
    
    *//**
     * Tests we can validate the container. Note it does not test 
     * the validator, this is done in its own tests.
     * 
     *//*
    @Test
    public void validate() {
        final TransactionCache cache = context.mock(TransactionCache.class);
        
        context.checking(new Expectations() {{
            oneOf(middleware).getCache(); will(returnValue(cache));
            oneOf(root).getErrors(); will(returnValue(null));
            allowing(cache).getObjects(with(any(ObjectRole.class))); will(returnValue(null));
            oneOf(cache).findObject(root.getId(), null); will(returnValue(null));
            oneOf(root).processValidation(with(any(ValidationRequest.class)));
        }});
        
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, true);
        final ValidationRequest request = container.validate();
        Assert.assertNotNull(request);
        
        context.checking(new Expectations() {{
            oneOf(root).getErrors(); will(returnValue(request));
        }});
        
        Assert.assertNotNull(container.getErrors());
        context.assertIsSatisfied();
    }
    
    *//**
     * Ensures we can save a container
     *//*
    @Test
    public void save() {
        final TransactionCache cache = context.mock(TransactionCache.class);
        
        context.checking(new Expectations() {{
            oneOf(middleware).getCache(); will(returnValue(cache));
            
            oneOf(root).getErrors(); will(returnValue(null));
            allowing(cache).getObjects(with(any(ObjectRole.class))); will(returnValue(null));
            oneOf(cache).findObject(root.getId(), null); will(returnValue(null));
            oneOf(root).processValidation(with(any(ValidationRequest.class)));
            
            oneOf(root).canSave(with(any(ValidationRequest.class))); will(returnValue(true));
            oneOf(root).getStatus(); will(returnValue("Current"));
            oneOf(root).getHeader(); will(returnValue(null));
            oneOf(middleware).save("Current", null); will(returnValue("abc"));
        }});
        
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, true);
        Assert.assertEquals("abc", container.saveContainer());
        Assert.assertEquals("abc", container.getId());
        context.assertIsSatisfied();
    }
    
    *//**
     * Ensures the save throws an exception if there are errors
     * on save
     *//*
    @Test(expected=ContainerInvalidException.class)
    public void saveWithErrors() {
        final TransactionCache cache = context.mock(TransactionCache.class);
        
        context.checking(new Expectations() {{
            oneOf(middleware).getCache(); will(returnValue(cache));
            
            oneOf(root).getErrors(); will(returnValue(null));
            allowing(cache).getObjects(with(any(ObjectRole.class))); will(returnValue(null));
            oneOf(cache).findObject(root.getId(), null); will(returnValue(null));
            oneOf(root).processValidation(with(any(ValidationRequest.class)));
            
            oneOf(root).canSave(with(any(ValidationRequest.class))); will(returnValue(false));
        }});
        
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, true);
        container.saveContainer();
    }
    
    *//**
     * Ensures new ID's are generated if there are temp IDs
     *//*
    public void saveAndAssign() {
        // TODO: Assigning is not complete yet
    }
    
    *//**
     * Ensures we can suspend the container
     *//*
    @Test
    public void suspend() {
        final TransactionCache cache = context.mock(TransactionCache.class);
        
        context.checking(new Expectations() {{
            oneOf(middleware).getCache(); will(returnValue(cache));
            oneOf(middleware).suspend(); will(returnValue("trans1"));
        }});
        
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, true);
        Assert.assertEquals("trans1", container.suspend());
        context.assertIsSatisfied();
    }
    
    *//**
     * Tests we can add an object that is about to be updated
     * to the container
     *//*
    @Test
    public void updateObject() {
        final TransactionCache cache = context.mock(TransactionCache.class);
        
        final CategoryBean state = new CategoryBean();
        
        context.checking(new Expectations() {{
            oneOf(middleware).getCache(); will(returnValue(cache));
            oneOf(cache).getObjectRole(obj.getId()); will(returnValue(ObjectRole.NONE));
            oneOf(cache).addObject(ObjectRole.UPDATED, obj.getId(), state);
        }});
        
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, true);
        container.addObjectToTransaction(obj, state);
        context.assertIsSatisfied();
    }
    
    *//**
     * Tests that if we try to update an object that is 
     * already removed it fails.
     *//*
    @Test(expected=ObjectRemovedException.class)
    public void updateRemovedObject() {
        final TransactionCache cache = context.mock(TransactionCache.class);
        final CategoryBean state = new CategoryBean();
        
        context.checking(new Expectations() {{
            oneOf(middleware).getCache(); will(returnValue(cache));
            oneOf(cache).getObjectRole(obj.getId()); will(returnValue(ObjectRole.REMOVED));
        }});
        
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, true);
        container.addObjectToTransaction(obj, state);
        context.assertIsSatisfied();
    }
    
    *//**
     * Ensures we can add an object to the container
     *//*
    @Test
    public void addObject() {
        final TransactionCache cache = context.mock(TransactionCache.class);
        final ObjexIDStrategy idStrategy = context.mock(ObjexIDStrategy.class);
        final CategoryBean state = new CategoryBean();
        final ObjexID newId = new DefaultObjexID("Test", "456");
        final ObjexObj newObj = context.mock(ObjexObj.class, "newObj");
        
        context.checking(new Expectations() {{
            oneOf(middleware).getCache(); will(returnValue(cache));
        }});
        
        final SimpleContainer container = new SimpleContainer("test", strategy, middleware, true);
        
        context.checking(new Expectations() {{
            oneOf(testStrategy).getIdStrategy(); will(returnValue(null));
            oneOf(middleware).getIdStrategy(); will(returnValue(idStrategy));
            oneOf(idStrategy).createId(container, CategoryBean.class, "Test", null); will(returnValue(newId));
            oneOf(cache).addObject(with(ObjectRole.NEW), with(newId), with(any(CategoryBean.class)));
            allowing(testStrategy).getObjexObjInstance(with(container), with(obj.getId()), with(newId), with(any(ObjexObjStateBean.class)));
                will(returnValue(newObj));
        }});
        
        container.newObject(obj, state, "Test");
        context.assertIsSatisfied();
    }
    
    *//**
     * This tests we can remove an object from the container
     *//*
    @Test
    public void removeObj() {
        final TransactionCache cache = context.mock(TransactionCache.class);
        final CategoryBean state = new CategoryBean();
        
        context.checking(new Expectations() {{
            oneOf(middleware).getCache(); will(returnValue(cache));
            oneOf(cache).findObject(obj.getId(), null); will(returnValue(state));
            oneOf(cache).addObject(ObjectRole.REMOVED, obj.getId(), state);
        }});
        
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, true);
        container.removeObject(obj);
        context.assertIsSatisfied();
    }
    
    *//**
     * Ensures the we can get an objects role in the transaction
     *//*
    @Test
    public void getRole() {
        final TransactionCache cache = context.mock(TransactionCache.class);
        
        context.checking(new Expectations() {{
            oneOf(middleware).getCache(); will(returnValue(cache));
            oneOf(cache).getObjectRole(obj.getId()); will(returnValue(ObjectRole.UPDATED));
        }});
        
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, true);
        Assert.assertEquals(ObjectRole.UPDATED, container.getObjectRole(obj.getId()));
        context.assertIsSatisfied();
    }
    
    *//**
     * As above, but outside a transaction so should get null
     *//*
    @Test
    public void getRoleOutsideTransaction() {
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, false);
        Assert.assertEquals(ObjectRole.NONE, container.getObjectRole(obj.getId()));
    }
    
    *//**
     * Test interface so we can have a root object
     *//*
    private static interface RootObject extends ObjexObj, RootObjexObj {
    }*/
}
