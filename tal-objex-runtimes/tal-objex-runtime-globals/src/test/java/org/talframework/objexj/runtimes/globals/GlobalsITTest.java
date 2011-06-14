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
package org.talframework.objexj.runtimes.globals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.Container;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.impl.SimpleContainerStrategy;
import org.talframework.objexj.locator.ContainerFactory;
import org.talframework.objexj.locator.SimpleContainerFactory;
import org.talframework.objexj.object.ObjectStrategyCompiler;
import org.talframework.objexj.sample.stock.Category;
import org.talframework.objexj.sample.stock.CategoryImpl;
import org.talframework.objexj.sample.stock.ProductImpl;

import com.intersys.gds.Connection;
import com.intersys.gds.schema.GDSConnectionImpl;

public class GlobalsITTest {
    
    private Connection connection;
    private ContainerFactory factory;
    
    @Before
    public void setup() {
        connection = new GDSConnectionImpl();
        connection.connect("", "", "");
        
        ContainerStrategy strategy = new SimpleContainerStrategy("Stock", "Stock", "Category",
                ObjectStrategyCompiler.calculateStrategy("Category", new String[]{"categories", "products"}, null, CategoryImpl.class),
                ObjectStrategyCompiler.calculateStrategy("product", null, new String[]{"nearestProduct"}, ProductImpl.class));
        factory = new SimpleContainerFactory(strategy, new GlobalsMiddlewareFactory(connection, strategy));
    }
    
    @After
    public void teardown() {
        if( connection != null && connection.isConnected() ) connection.close();
    }
    
    @Test
    public void basic() {
        Container stock = factory.create();
        
        Category root = stock.getRootObject().getBehaviour(Category.class);
        root.setName("Root!");
        
        root.getCategories().add(new CategoryImpl("Cat1", "Desc Cat1"));
        root.getCategories().add(new CategoryImpl("Cat2", "Desc Cat2"));
        root.getCategories().add(new CategoryImpl("Cat3", "Desc Cat3"));
        root.getCategories().add(new CategoryImpl("Cat4", "Desc Cat4"));
        
        stock.saveContainer();
    }
}
