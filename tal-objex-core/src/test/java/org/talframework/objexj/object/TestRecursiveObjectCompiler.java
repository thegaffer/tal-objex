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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.object.testmodel.api.ICategory;
import org.talframework.objexj.object.testmodel.api.IProduct;
import org.talframework.objexj.object.testmodel.objex.Category;
import org.talframework.objexj.object.testmodel.objex.Product;

/**
 * This class tests the recursive object compiler
 *
 * @author Tom Spencer
 */
public class TestRecursiveObjectCompiler {

    private Mockery context = new JUnit4Mockery();
    
    private InternalContainer container;
    private Category cat1 = null;
    private Category cat2 = null;
    private Category cat3 = null;
    private Category cat4 = null;
    
    @Before
    public void setup() {
        // Mock internal container so we can initialise the objects
        container = context.mock(InternalContainer.class);
        
        // Setup our model naturally
        cat1 = createCategory("Cat1", "Cat1", new DefaultObjexID("Category", 1), null);
        cat1.setProducts(new ArrayList<IProduct>());
        cat1.getProducts().add(createProduct("Product1", "Product1", 1, 10, new DefaultObjexID("Product", 1), cat1.getId()));
        cat1.getProducts().add(createProduct("Product2", "Product2", 1, 10, new DefaultObjexID("Product", 2), cat1.getId()));
        cat1.setMainProduct(cat1.getProducts().get(0));
        cat1.setCategories(new HashMap<String, ICategory>());
        
        cat2 = createCategory("Cat2", "Cat2", new DefaultObjexID("Category", 2), cat1.getId());
        cat2.setProducts(new ArrayList<IProduct>());
        cat2.getProducts().add(createProduct("Product3", "Product3", 1, 10, new DefaultObjexID("Product", 3), cat2.getId()));
        cat2.getProducts().add(createProduct("Product4", "Product4", 1, 10, new DefaultObjexID("Product", 4), cat2.getId()));
        cat2.setMainProduct(cat2.getProducts().get(0));
        cat2.setCategories(new HashMap<String, ICategory>());
        
        cat3 = createCategory("Cat3", "Cat3", new DefaultObjexID("Category", 3), cat1.getId());
        cat3.setProducts(new ArrayList<IProduct>());
        cat3.getProducts().add(createProduct("Product5", "Product5", 1, 10, new DefaultObjexID("Product", 5), cat3.getId()));
        cat3.getProducts().add(createProduct("Product6", "Product6", 1, 10, new DefaultObjexID("Product", 6), cat3.getId()));
        cat3.setMainProduct(cat3.getProducts().get(0));
        
        cat4 = createCategory("Cat4", "Cat4", new DefaultObjexID("Category", 4), cat2.getId());
        cat4.setProducts(new ArrayList<IProduct>());
        cat4.getProducts().add(createProduct("Product7", "Product7", 1, 10, new DefaultObjexID("Product", 7), cat3.getId()));
        cat4.getProducts().add(createProduct("Product8", "Product8", 1, 10, new DefaultObjexID("Product", 8), cat4.getId()));
        cat4.setMainProduct(cat4.getProducts().get(0));
        
        cat1.getCategories().put("Cat2", cat2);
        cat1.getCategories().put("Cat3", cat3);
        cat2.getCategories().put("Cat4", cat4);
    }
    
    /**
     * Helper to create and initialise a category
     */
    private Category createCategory(String name, String description, ObjexID id, ObjexID parentId) {
        Category ret = new Category(name, description);
        ret.init(container, id, parentId);
        return ret;
    }
    
    /**
     * Helper to create and initialise a product
     */
    private Product createProduct(String name, String description, int level, double price, ObjexID id, ObjexID parentId) {
        Product ret = new Product(name, description, level, price);
        ret.init(container, id, parentId);
        return ret;
    }
    
    /**
     * Ensures the default 1 level recursion works
     */
    @Test
    public void basic() {
        RecursiveObjectCompiler compiler = new RecursiveObjectCompiler(container);
        Map<ObjexID, ObjexObj> objs = compiler.getObjects(cat1);
        
        Assert.assertNotNull(objs);
        Assert.assertEquals(5, objs.size()); // Main Category, its 2 Products and its 2 Sub-Categories
    }
    
    /**
     * Ensures we can get all objects in a container
     */
    @Test
    public void findAll() {
        RecursiveObjectCompiler compiler = new RecursiveObjectCompiler(container);
        compiler.setRecurseDepth(-1);
        Map<ObjexID, ObjexObj> objs = compiler.getObjects(cat1);
        
        Assert.assertNotNull(objs);
        Assert.assertEquals(12, objs.size());
    }
    
    /**
     * Ensures the default 1 level recursion works
     */
    @Test
    public void referencesOnly() {
        RecursiveObjectCompiler compiler = new RecursiveObjectCompiler(container);
        compiler.setIgnoreOwned();
        Map<ObjexID, ObjexObj> objs = compiler.getObjects(cat1);
        
        Assert.assertNotNull(objs);
        Assert.assertEquals(2, objs.size());    // Itself and it's one reference, the main product
    }
    
    /**
     * Ensures the default 1 level recursion works
     */
    @Test
    public void childrenOnly() {
        RecursiveObjectCompiler compiler = new RecursiveObjectCompiler(container);
        compiler.setIgnoreReferenced();
        Map<ObjexID, ObjexObj> objs = compiler.getObjects(cat1);
        
        Assert.assertNotNull(objs);
        Assert.assertEquals(5, objs.size());    // As per depth of 1
    }
}
