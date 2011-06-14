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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.container.impl.SimpleContainerStrategy;
import org.talframework.objexj.container.middleware.InMemoryMiddlewareFactory;
import org.talframework.objexj.locator.ContainerFactory;
import org.talframework.objexj.locator.SimpleContainerFactory;
import org.talframework.objexj.sample.api.order.Order;
import org.talframework.objexj.sample.api.order.OrderItem;
import org.talframework.objexj.sample.api.repository.OrderRepository;
import org.talframework.objexj.sample.api.repository.OrderService;

/**
 * This is the full Integration test for the order service.
 * It uses the in memory middleware
 *
 * @author Tom Spencer
 */
public class TestIntegrationOrderService {

    /** Holds the service under test */
    private OrderService underTest;
    
    @Before
    public void setup() {
        ObjectStrategy[] strategies = new ObjectStrategy[]{OrderImpl.STRATEGY, OrderItemImpl.STRATEGY};
        ContainerStrategy strategy = new SimpleContainerStrategy("Order", "Order", strategies);
        ContainerFactory locator = new SimpleContainerFactory(strategy, new InMemoryMiddlewareFactory());
        
        underTest = new OrderServiceImpl(locator);
    }
    
    /**
     * Single test to full test the repository
     */
    @Test
    public void basic() {
        // Create new, add an item and save
        OrderRepository repo = underTest.createNewOrder();
        
        Order order = repo.getOrder();
        order.setAccount(12345);
        OrderItem item = order.createItem();
        item.setName("Test Item");
        item.setDescription("This is purely a test");
        item.setQuantity(1);
        item.setMeasure("no");
        item.setPrice(99.99);
        item.setCurrency("GBP");
        
        repo.persist();
        String newOrderId = repo.getId();
        
        // Now get it again, test it and then add another item, then suspend
        repo = underTest.getOpenRepository(newOrderId);
        Assert.assertNotNull(repo.getOrder());
        Assert.assertEquals(12345, repo.getOrder().getAccount());
        Assert.assertNotNull(repo.getOrder().getItems());
        Assert.assertEquals(1, repo.getOrder().getItems().size());
        Assert.assertEquals("Test Item", repo.getOrder().getItems().get(0).getName());
        
        order = repo.getOrder();
        item = order.createItem();
        item.setName("Another Item");
        item.setQuantity(2);
        item.setPrice(79.99);
        
        String transId = repo.suspend();
        
        // Get the suspended transaction, test it, change account then save
        repo = underTest.getOpenRepository(transId);
        Assert.assertNotNull(repo.getOrder());
        Assert.assertNotNull(repo.getOrder().getItems());
        Assert.assertEquals(2, repo.getOrder().getItems().size());
        Assert.assertEquals("Another Item", repo.getOrder().getItems().get(1).getName());
        
        repo.getOrder().setAccount(23456);
        repo.persist();
        
        // Finally get the repo read-only and test
        repo = underTest.getRepository(newOrderId);
        Assert.assertNotNull(repo.getOrder());
        Assert.assertEquals(23456, repo.getOrder().getAccount());
        Assert.assertNotNull(repo.getOrder().getItems());
        Assert.assertEquals(2, repo.getOrder().getItems().size());
        Assert.assertEquals("Test Item", repo.getOrder().getItems().get(0).getName());
        Assert.assertEquals("Another Item", repo.getOrder().getItems().get(1).getName());
    }
}
