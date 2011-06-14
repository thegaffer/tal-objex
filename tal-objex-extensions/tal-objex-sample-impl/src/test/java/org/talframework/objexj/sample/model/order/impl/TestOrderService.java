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
package org.talframework.objexj.sample.model.order.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.Container;
import org.talframework.objexj.locator.ContainerFactory;
import org.talframework.objexj.sample.api.repository.OrderRepository;
import org.talframework.objexj.sample.model.order.impl.OrderServiceImpl;

/**
 * Unit test of the service class
 * 
 * @author Tom Spencer
 */
public class TestOrderService {
    
    private Mockery context = new JUnit4Mockery();
    
    /** Mocked factory for service to use */
    private ContainerFactory containerFactory = null;
    
    @Before
    public void setup() {
        containerFactory = context.mock(ContainerFactory.class);
    }
    
    @Test
    public void create() {
        OrderServiceImpl service = new OrderServiceImpl(containerFactory);
        final Container container = context.mock(Container.class);
        // final ObjexObj obj = context.mock(ObjexObj.class);
        
        context.checking(new Expectations() {{
            oneOf(containerFactory).create();
              will(returnValue(container));
        }});
        
        OrderRepository repo = service.createNewOrder();
        Assert.assertNotNull(repo);
        // Assert.assertNotNull(repo.getOrder());
        context.assertIsSatisfied();
    }
    
    @Test
    public void get() {
        OrderServiceImpl service = new OrderServiceImpl(containerFactory);
        final Container container = context.mock(Container.class);
        
        context.checking(new Expectations() {{
            oneOf(containerFactory).get("123"); will(returnValue(container));
        }});
        
        OrderRepository repo = service.getRepository("123");
        Assert.assertNotNull(repo);
        context.assertIsSatisfied();
    }
    
    @Test
    public void open() {
        OrderServiceImpl service = new OrderServiceImpl(containerFactory);
        final Container container = context.mock(Container.class);
        
        context.checking(new Expectations() {{
            oneOf(containerFactory).open("321"); will(returnValue(container));
        }});
        
        OrderRepository repo = service.getOpenRepository("321");
        Assert.assertNotNull(repo);
        context.assertIsSatisfied();
    }
}
