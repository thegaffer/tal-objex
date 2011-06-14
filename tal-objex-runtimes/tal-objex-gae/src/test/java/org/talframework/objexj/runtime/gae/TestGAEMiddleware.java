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
