package org.talframework.objexj.container.impl;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexIDStrategy;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.RootObjexObj;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.container.TransactionCache;
import org.talframework.objexj.container.TransactionCache.ObjectRole;
import org.talframework.objexj.events.Event;
import org.talframework.objexj.events.EventHandler;
import org.talframework.objexj.exceptions.ContainerInvalidException;
import org.talframework.objexj.object.DefaultObjexID;
import org.talframework.objexj.object.ObjectStrategy;
import org.talframework.objexj.query.Query;
import org.talframework.objexj.query.QueryRequest;
import org.talframework.objexj.query.QueryResult;
import org.talframework.objexj.service.beans.CategoryBean;

/**
 * This class tests our simple container
 *
 * @author Tom Spencer
 */
public class TestSimpleContainer {

private Mockery context = new JUnit4Mockery();
    
    private ContainerStrategy strategy;
    private ObjectStrategy testStrategy;
    private ContainerMiddleware middleware;
    
    private RootObject root = null;
    private ObjexObj obj = null;
    
    /**
     * Sets up the main collaborators including their
     * default behaviour.
     */
    @Before
    public void setup() {
        strategy = context.mock(ContainerStrategy.class);
        testStrategy = context.mock(ObjectStrategy.class, "test");
        middleware = context.mock(ContainerMiddleware.class);
        
        final ObjexID rootId = new DefaultObjexID("Test", 1);
        root = context.mock(RootObject.class, "root");
        final ObjexObjStateBean rootState = context.mock(ObjexObjStateBean.class, "rootState");
        
        final ObjexID objId = new DefaultObjexID("Test", 1234);
        obj = context.mock(ObjexObj.class, "obj");
        final ObjexObjStateBean objState = context.mock(ObjexObjStateBean.class, "objState");
        
        
        
        context.checking(new Expectations() {{
            atMost(1).of(middleware).init(with(any(Container.class)));
            atMost(1).of(middleware).loadObject(CategoryBean.class, rootId); will(returnValue(rootState));
            atMost(1).of(middleware).loadObject(CategoryBean.class, objId); will(returnValue(objState));
            
            // Container Strategy Defaults
            allowing(strategy).getObjectStrategy("Test"); will(returnValue(testStrategy));
            allowing(strategy).getRootObjectID(); will(returnValue(rootId));
            allowing(strategy).getContainerName(); will(returnValue("TestContainer"));
            
            // Test Object Strategy Defaults
            allowing(testStrategy).getStateClass(); will(returnValue(CategoryBean.class));
            allowing(testStrategy).getObjexObjInstance(with(any(InternalContainer.class)), (ObjexID)with(anything()), with(rootId), with(any(ObjexObjStateBean.class)));
                will(returnValue(root));
            allowing(testStrategy).getObjexObjInstance(with(any(InternalContainer.class)), (ObjexID)with(anything()), with(objId), with(any(ObjexObjStateBean.class)));
                will(returnValue(obj));
                
            // Default Object
            allowing(root).getId(); will(returnValue(rootId));
            allowing(rootState).getParentId(); will(returnValue(null));
            allowing(obj).getId(); will(returnValue(objId));
            allowing(objState).getParentId(); will(returnValue("Test/1"));
        }});
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
        
        context.assertIsSatisfied();
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
        
        final CategoryBean bean = new CategoryBean();
        
        context.checking(new Expectations() {{
            oneOf(obj).getBehaviour(CategoryBean.class); will(returnValue(bean));
        }});
        
        Assert.assertNotNull(container.getObject("Test/1234", CategoryBean.class));
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
        final SimpleContainer container = new SimpleContainer("test", strategy, middleware, false);
        
        final QueryRequest request = context.mock(QueryRequest.class);
        final Query query = context.mock(Query.class);
        final QueryResult result = context.mock(QueryResult.class);
        
        context.checking(new Expectations() {{
            oneOf(request).getName(); will(returnValue("testQuery"));
            oneOf(strategy).getQuery("testQuery"); will(returnValue(query));
            oneOf(query).execute(container, strategy, request); will(returnValue(result));
        }});
        
        Assert.assertEquals(result, container.executeQuery(request));
        context.assertIsSatisfied();
    }
    
    /**
     * Ensures we can fire off an event
     */
    @Test
    public void processEvent() {
        final SimpleContainer container = new SimpleContainer("test", strategy, middleware, false);
        
        final Event event = context.mock(Event.class);
        final EventHandler handler = context.mock(EventHandler.class);
        
        context.checking(new Expectations() {{
            oneOf(event).getEventName(); will(returnValue("testEvent"));
            oneOf(strategy).getEventHandler("testEvent"); will(returnValue(handler));
            oneOf(handler).execute(container, event);
        }});
        
        container.processEvent(event);
        context.assertIsSatisfied();
    }
 
    /**
     * Ensures we can open a container and then close it
     */
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
    
    /**
     * Tests we can validate the container. Note it does not test 
     * the validator, this is done in its own tests.
     * 
     */
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
    
    /**
     * Ensures we can save a container
     */
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
    
    /**
     * Ensures the save throws an exception if there are errors
     * on save
     */
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
    
    /**
     * Ensures new ID's are generated if there are temp IDs
     */
    public void saveAndAssign() {
        // TODO: Assigning is not complete yet
    }
    
    /**
     * Ensures we can suspend the container
     */
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
    
    /**
     * Tests we can add an object that is about to be updated
     * to the container
     */
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
    
    /**
     * Tests that if we try to update an object that is 
     * already removed it fails.
     * 
     * FUTURE: Probably should fail with an exception, but currently does not
     */
    @Test //(expected=IllegalArgumentException.class)
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
    
    /**
     * Ensures we can add an object to the container
     */
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
    
    /**
     * This tests we can remove an object from the container
     */
    @Test
    public void removeObj() {
        final TransactionCache cache = context.mock(TransactionCache.class);
        final CategoryBean state = new CategoryBean();
        
        context.checking(new Expectations() {{
            oneOf(middleware).getCache(); will(returnValue(cache));
            oneOf(cache).addObject(ObjectRole.REMOVED, obj.getId(), state);
        }});
        
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, true);
        container.removeObject(obj, state);
        context.assertIsSatisfied();
    }
    
    /**
     * Ensures the we can get an objects role in the transaction
     */
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
    
    /**
     * As above, but outside a transaction so should get null
     */
    @Test
    public void getRoleOutsideTransaction() {
        SimpleContainer container = new SimpleContainer("test", strategy, middleware, false);
        Assert.assertEquals(ObjectRole.NONE, container.getObjectRole(obj.getId()));
    }
    
    /**
     * Test interface so we can have a root object
     */
    private static interface RootObject extends ObjexObj, RootObjexObj {
    }
}
