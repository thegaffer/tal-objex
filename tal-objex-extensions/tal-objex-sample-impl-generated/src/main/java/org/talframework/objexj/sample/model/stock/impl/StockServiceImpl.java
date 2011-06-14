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
package org.talframework.objexj.sample.model.stock.impl;

import org.talframework.objexj.Container;
import org.talframework.objexj.container.ContainerMiddlewareFactory;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.container.impl.SimpleContainerStrategy;
import org.talframework.objexj.locator.ContainerFactory;
import org.talframework.objexj.locator.SimpleContainerFactory;
import org.talframework.objexj.object.DefaultObjectStrategy;
import org.talframework.objexj.sample.api.repository.StockRepository;
import org.talframework.objexj.sample.api.repository.StockService;
import org.talframework.objexj.sample.api.stock.Category;
import org.talframework.objexj.sample.beans.stock.CategoryBean;
import org.talframework.objexj.sample.beans.stock.ProductBean;

public class StockServiceImpl implements StockService {
    public static final ObjectStrategy CATEGORY_STRATEGY = new DefaultObjectStrategy("Category", CategoryImpl.class, CategoryBean.class);
    public static final ObjectStrategy PRODUCT_STRATEGY = new DefaultObjectStrategy("Product", ProductImpl.class, ProductBean.class);
    
    /** Holds the Objex factory for the order container type */
    private final ContainerFactory locator;
    
    public StockServiceImpl(ContainerFactory locator) {
        this.locator = locator;
    }
    
    public StockServiceImpl(ContainerMiddlewareFactory middlewareFactory) {
        ObjectStrategy[] strategies = new ObjectStrategy[]{CATEGORY_STRATEGY, PRODUCT_STRATEGY};
        ContainerStrategy strategy = new SimpleContainerStrategy("Stock", "Stock", "Category", strategies);
        
        locator = new SimpleContainerFactory(strategy, middlewareFactory);
    }

    public StockRepository getRepository() {
        return new StockRepositoryImpl(locator.get("Stock"));
    }
    
    public StockRepository getOpenRepository() {
        return new StockRepositoryImpl(locator.open("Stock"));
    }
    
    public StockRepository getOpenRepository(String id) {
        return new StockRepositoryImpl(locator.open(id));
    }

    public void ensureCreated() {
        Container container = locator.create();
        if( container.isNew() ) {
            Category rootCat = container.getRootObject().getBehaviour(Category.class);
            rootCat.setName("Root");
            rootCat.setDescription("The root category for all Stock");
            
            container.saveContainer();
        }
    }
}
