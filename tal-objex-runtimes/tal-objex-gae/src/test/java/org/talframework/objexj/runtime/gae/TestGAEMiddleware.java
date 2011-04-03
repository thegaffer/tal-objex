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

package org.talframework.objexj.runtime.gae;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.impl.SimpleTransactionCache;
import org.talframework.objexj.runtime.gae.GAEAllocateObjexIDStrategy;
import org.talframework.objexj.runtime.gae.GAEMiddleware;
import org.talframework.objexj.runtime.gae.GAENewObjexIDStrategy;
import org.talframework.objexj.runtime.gae.GAETransaction;
import org.talframework.objexj.runtime.gae.object.ContainerBean;

public class TestGAEMiddleware {
    
    private Mockery context = new JUnit4Mockery();
    private Container container;
    private ContainerStrategy strategy;
    
    @Before
    public void setup() {
        strategy = context.mock(ContainerStrategy.class);
        container = context.mock(Container.class);
        
        context.checking(new Expectations() {{
            allowing(strategy).getStandardListeners(); will(returnValue(null));
        }});
    }

    /**
     * Ensures basic checks
     */
    @Test
    public void basic() {
        // Setup
        ContainerBean bean = new ContainerBean();
        bean.setType("Test");
        bean.setContainerId("123");
        
        // Test
        GAEMiddleware underTest = new GAEMiddleware(strategy, bean);
        underTest.init(container);
        
        // Check
        Assert.assertEquals("Test/123", underTest.getContainerId());
        Assert.assertTrue(underTest.isNew());
        Assert.assertEquals(GAEAllocateObjexIDStrategy.getInstance(), underTest.getIdStrategy());
        Assert.assertNull(underTest.getCache());
    }
    
    /**
     * Ensures basic checks if we have a transaction
     */
    @Test
    public void transaction() {
        // Setup
        ContainerBean bean = new ContainerBean();
        bean.setType("Test");
        bean.setContainerId("123");
        
        GAETransaction trans = new GAETransaction();
        trans.setContainerBean(bean);
        trans.setCache(new SimpleTransactionCache());
        
        // Test
        GAEMiddleware underTest = new GAEMiddleware(strategy, trans);
        underTest.init(container);
        
        // Check
        Assert.assertEquals("Test/123", underTest.getContainerId());
        Assert.assertTrue(underTest.isNew());
        Assert.assertNotNull(underTest.getIdStrategy());
        Assert.assertTrue(underTest.getIdStrategy() instanceof GAENewObjexIDStrategy);
        Assert.assertNotNull(underTest.getCache());
    }
    
    /**
     * Just checks cannot load if not initialised
     */
    @Test(expected=IllegalStateException.class)
    public void loadIfNotInitialised() {
        ContainerBean bean = new ContainerBean();
        GAEMiddleware underTest = new GAEMiddleware(strategy, bean);
        underTest.loadObject(ObjexObjStateBean.class, null);
    }
    
    /**
     * Just checks cannot save if no transaction
     */
    @Test(expected=IllegalStateException.class)
    public void saveIfNoTransaction() {
        ContainerBean bean = new ContainerBean();
        GAEMiddleware underTest = new GAEMiddleware(strategy, bean);
        underTest.init(container);
        
        underTest.save(null, null);
    }
    
    /**
     * Just checks cannot suspend if no transaction
     */
    @Test(expected=IllegalStateException.class)
    public void suspendIfNoTransaction() {
        ContainerBean bean = new ContainerBean();
        GAEMiddleware underTest = new GAEMiddleware(strategy, bean);
        underTest.init(container);
        
        underTest.suspend();
    }
    
    /**
     * Just checks it is ok to call clear at any time.
     */
    public void clearIfNoTransaction() {
        ContainerBean bean = new ContainerBean();
        GAEMiddleware underTest = new GAEMiddleware(strategy, bean);
        underTest.init(container);
        
        underTest.clear();
    }
}
