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

package org.talframework.objexj.object;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.container.ObjexIDStrategy;
import org.talframework.objexj.object.testbeans.StockBean;
import org.talframework.objexj.object.testmodel.StockImpl;

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
	    strategy.setStateClass(StockBean.class);
	    strategy.setObjexClass(StockImpl.class);
	    strategy.init();
	    
	    StockBean bean = new StockBean();
        bean.setParentId(parentId.toString());
	    
	    // Simple Tests
	    Assert.assertEquals("Test", strategy.getTypeName());
        Assert.assertNotNull(strategy.getIdStrategy());
        Assert.assertNotNull(strategy.getStateClass());
        Assert.assertNotNull(strategy.getObjexClass());
	    
        // Construction
	    ObjexObj obj = strategy.getObjexObjInstance(container, parentId, id, bean);
	    Assert.assertNotNull(obj);
	    Assert.assertTrue(obj instanceof StockImpl);
    }
	
	@Test
    public void withoutObjexObj() {
        SimpleObjectStrategy strategy = new SimpleObjectStrategy();
        strategy.setTypeName("Test");
        strategy.setStateClass(StockBean.class);
        strategy.init();
        
        StockBean bean = new StockBean();
        bean.setParentId(parentId.toString());
        
        // Simple Tests
        Assert.assertEquals("Test", strategy.getTypeName());
        Assert.assertNotNull(strategy.getStateClass());
        Assert.assertNull(strategy.getObjexClass());
        
        // Construction
        ObjexObj obj = strategy.getObjexObjInstance(container, parentId, id, bean);
        Assert.assertNotNull(obj);
        Assert.assertTrue(obj instanceof SimpleObjexObj);
    }
}
