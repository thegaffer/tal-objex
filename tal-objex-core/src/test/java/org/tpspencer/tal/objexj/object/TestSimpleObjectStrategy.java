package org.tpspencer.tal.objexj.object;

import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexIDStrategy;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.ValidationRequest;
import org.tpspencer.tal.objexj.container.InternalContainer;

/**
 * Tests the {@link SimpleObjectStrategy} class.
 * 
 * @author Tom Spencer
 */
public class TestSimpleObjectStrategy {
    
    private Mockery context = new JUnit4Mockery();
    private InternalContainer container;
    private ObjexID id;
    private ObjexID parentId;
    private ObjexIDStrategy idStrategy;
    
    @Before
    public void setup() {
        container = context.mock(InternalContainer.class);
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
	    StateBean bean = new StateBean(parentId);
	    ObjexObj obj = strategy.getObjexObjInstance(container, parentId, id, bean);
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
        StateBean bean = new StateBean(parentId);
        ObjexObj obj = strategy.getObjexObjInstance(container, parentId, id, bean);
        Assert.assertNotNull(obj);
    }
	
	public static class StateBean implements ObjexObjStateBean {
	    private static final long serialVersionUID = 1L;
	    
	    public StateBean() {}
	    
	    public StateBean(ObjexID parent) {}
	    public StateBean(StateBean bean) {}
	    
	    public void create(ObjexID parentId) {}
	    public void preSave(Object id) {}
	    
	    public void setEditable() {
	    }
	    
	    public boolean isEditable() {
	        return true;
	    }
	    
	    public ObjexObjStateBean clone() {
	        return new StateBean();
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
	    
	    public void validate(ValidationRequest request) {
	    }
	}
}
