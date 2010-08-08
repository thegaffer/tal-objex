package org.tpspencer.tal.objexj.object;

import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexIDStrategy;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;

/**
 * TODO: Write the tests
 * 
 * @author Tom Spencer
 */
public class TestSimpleObjectStrategy {
    
    private Mockery context = new JUnit4Mockery();
    private Container container;
    private ObjexID id;
    private ObjexID parentId;
    private ObjexIDStrategy idStrategy;
    
    @Before
    public void setup() {
        container = context.mock(Container.class);
        id = context.mock(ObjexID.class);
        parentId = context.mock(ObjexID.class, "parent");
        idStrategy = context.mock(ObjexIDStrategy.class);
        
        context.checking(new Expectations(){{
            allowing(id).isNull(); will(returnValue(false));
        }});
    }

	@Test
	public void basic() {
	    SimpleObjectStrategy strategy = new SimpleObjectStrategy();
	    strategy.setTypeName("Test");
	    strategy.setIdStrategy(idStrategy);
	    strategy.setStateClass(StateBean.class);
	    strategy.setObjexClass(TestObj.class);
	    strategy.init();
	    
	    // Simple Tests
	    Assert.assertEquals("Test", strategy.getTypeName());
        Assert.assertNotNull(strategy.getIdStrategy());
        Assert.assertNotNull(strategy.getStateClass());
        Assert.assertNotNull(strategy.getObjexClass());
	    
        // Construction
	    StateBean bean = (StateBean)strategy.getNewStateInstance(parentId);
	    Assert.assertNotNull(bean);
	    
	    StateBean copy = (StateBean)strategy.getClonedStateInstance(bean);
	    Assert.assertNotNull(copy);
	    
	    ObjexObj obj = strategy.getObjexObjInstance(container, parentId, id, copy);
	    Assert.assertNotNull(obj);
    }
	
	@Test
    public void withoutObjexObj() {
        SimpleObjectStrategy strategy = new SimpleObjectStrategy();
        strategy.setTypeName("Test");
        strategy.setStateClass(StateBean.class);
        strategy.init();
        
        // Simple Tests
        Assert.assertEquals("Test", strategy.getTypeName());
        Assert.assertNotNull(strategy.getStateClass());
        Assert.assertNull(strategy.getObjexClass());
        
        // Construction
        StateBean bean = (StateBean)strategy.getNewStateInstance(parentId);
        Assert.assertNotNull(bean);
        
        ObjexObj obj = strategy.getObjexObjInstance(container, parentId, id, bean);
        Assert.assertNotNull(obj);
    }
	
	public static class StateBean implements ObjexObjStateBean {
	    private static final long serialVersionUID = 1L;
	    
	    public StateBean(ObjexID parent) {
	        
	    }
	    
	    public StateBean(StateBean bean) {
	        
	    }
	    
	    public void init(Object id) {
	    }
	    
	    public String getObjexObjType() {
	        return null;
	    }
	    
	    public Object getId() {
	        return null;
	    }
	    
	    public String getParentId() {
	        return null;
	    }
	    
	    public void updateTemporaryReferences(Map<ObjexID, ObjexID> refs) {
	    }
	}
	
	public static class TestObj extends BaseObjexObj {
	    
	    public TestObj(StateBean bean) {
	    }
	    
	    @Override
	    protected Object getLocalState() {
	        return null;
	    }
	    
	    public ObjexObjStateBean getStateObject() {
	        return null;
	    }
	}
}
