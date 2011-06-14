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

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.container.impl.SimpleContainerStrategy;
import org.talframework.objexj.container.middleware.InMemoryMiddlewareFactory;
import org.talframework.objexj.locator.ContainerFactory;
import org.talframework.objexj.locator.SimpleContainerFactory;
import org.talframework.objexj.sample.api.repository.StockRepository;
import org.talframework.objexj.sample.api.repository.StockService;
import org.talframework.objexj.sample.api.stock.Category;
import org.talframework.objexj.sample.api.stock.Product;
import org.talframework.objexj.sample.model.stock.impl.CategoryImpl;
import org.talframework.objexj.sample.model.stock.impl.ProductImpl;
import org.talframework.objexj.sample.model.stock.impl.StockServiceImpl;

/**
 * This is the full Integration test for the stock service.
 * It uses the in memory middleware
 *
 * @author Tom Spencer
 */
public class TestIntegrationStockService {

    /** Holds the service under test */
    private StockService underTest;
    
    @Before
    public void setup() {
        ObjectStrategy[] strategies = new ObjectStrategy[]{CategoryImpl.STRATEGY, ProductImpl.STRATEGY};
        ContainerStrategy strategy = new SimpleContainerStrategy("Stock", "Stock", "Category", strategies);
        ContainerFactory locator = new SimpleContainerFactory(strategy, new InMemoryMiddlewareFactory());
        
        underTest = new StockServiceImpl(locator);
    }
    
    /**
     * Single test to full test the repository
     */
    @Test
    public void basic() {
        // Get repository, add an item and suspend
        underTest.ensureCreated();
        StockRepository repository = underTest.getOpenRepository();
        Assert.assertNotNull(repository.findCategory(null));
        Assert.assertEquals("Root", repository.findCategory(null).getName());
        
        Category rootCat = repository.findCategory(null);
        setCategory(repository.findCategory(null), "RootCat", "Amended");
        setCategory(repository.createNewCategory(null), "Cat1", "Cat1 Desc");
        setCategory(repository.createNewCategory(null), "Cat2", "Cat2 Desc");
        setProduct(repository.createNewProduct(null), "Product1", "Product1 Test", 59.99, "GBP");
        setProduct(repository.createNewProduct(null), "Product2", "Product2 Test", 19.99, "USD");
        
        String transId = repository.suspend();
        
        // Get the suspended transaction, test it, change account then save
        repository = underTest.getOpenRepository(transId);
        rootCat = repository.findCategory(null);
        Assert.assertNotNull(rootCat);
        Assert.assertEquals("RootCat", rootCat.getName());
        Assert.assertNotNull(repository.getRootCategories());
        Assert.assertEquals(2, repository.getRootCategories().size());
        Assert.assertEquals("Cat1", repository.getRootCategories().get(0).getName());
        Assert.assertEquals("Cat2", repository.getRootCategories().get(1).getName());
        Assert.assertNotNull(rootCat.getProducts());
        Assert.assertEquals(2, rootCat.getProducts().size());
        Assert.assertEquals("Product1", rootCat.getProducts().get(0).getName());
        Assert.assertEquals("Product2", rootCat.getProducts().get(1).getName());
        
        Category cat = repository.getRootCategories().get(0);
        setCategory(cat.createCategory(), "ChildCat1", "Child Category");
        setProduct(cat.createProduct(), "Product3", "Product3 Test", 39.99, "GBP");
        
        repository.persist();
        
        // Finally get the repo read-only and test
        repository = underTest.getRepository();
        rootCat = repository.findCategory(null);
        Assert.assertNotNull(rootCat);
        Assert.assertEquals("RootCat", rootCat.getName());
        Assert.assertNotNull(repository.getRootCategories());
        Assert.assertEquals(2, repository.getRootCategories().size());
        Assert.assertEquals("Cat1", repository.getRootCategories().get(0).getName());
        Assert.assertEquals("Cat2", repository.getRootCategories().get(1).getName());
        Assert.assertNotNull(rootCat.getProducts());
        Assert.assertEquals(2, rootCat.getProducts().size());
        Assert.assertEquals("Product1", rootCat.getProducts().get(0).getName());
        Assert.assertEquals("Product2", rootCat.getProducts().get(1).getName());
        cat = rootCat.getCategories().get(0);
        Assert.assertEquals(1, cat.getCategories().size());
        Assert.assertEquals("ChildCat1", cat.getCategories().get(0).getName());
        Assert.assertEquals(1, cat.getProducts().size());
        Assert.assertEquals("Product3", cat.getProducts().get(0).getName());
    }
    
    /**
     * Helper to set the fields on a category
     */
    private void setCategory(Category cat, String name, String desc) {
        cat.setName(name);
        cat.setDescription(desc);
    }
    
    /**
     * Helper to set the fields on a product
     */
    private void setProduct(Product product, String name, String desc, double price, String curr) {
        product.setName(name);
        product.setDescription(desc);
        product.setPrice(price);
        product.setCurrency(curr);
        if( product.getEffectiveFrom() == null ) product.setEffectiveFrom(new Date());
        if( product.getEffectiveTo() == null ) product.setEffectiveTo(new Date());
    }
}
