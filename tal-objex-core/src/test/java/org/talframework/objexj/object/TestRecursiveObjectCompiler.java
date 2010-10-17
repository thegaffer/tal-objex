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
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.object.testbeans.CategoryBean;
import org.talframework.objexj.object.testbeans.ProductBean;
import org.talframework.objexj.object.testmodel.CategoryImpl;
import org.talframework.objexj.object.testmodel.ProductImpl;

/**
 * This class tests the recursive object compiler
 *
 * @author Tom Spencer
 */
public class TestRecursiveObjectCompiler {

    private Mockery context = new JUnit4Mockery();
    
    private Container container;
    private CategoryImpl cat1 = null;
    private CategoryImpl cat2 = null;
    private CategoryImpl cat3 = null;
    private CategoryImpl cat4 = null;
    
    @Before
    public void setup() {
        // Mock internal container so we can initialise the objects
        final InternalContainer container = context.mock(InternalContainer.class);
        
        // The IDs
        final ObjexID cat1Id = new DefaultObjexID("Category", 1);
        final ObjexID cat2Id = new DefaultObjexID("Category", 2);
        final ObjexID cat3Id = new DefaultObjexID("Category", 3);
        final ObjexID cat4Id = new DefaultObjexID("Category", 4);
        final ObjexID prod1Id = new DefaultObjexID("Product", 1);
        final ObjexID prod2Id = new DefaultObjexID("Product", 2);
        final ObjexID prod3Id = new DefaultObjexID("Product", 3);
        final ObjexID prod4Id = new DefaultObjexID("Product", 4);
        final ObjexID prod5Id = new DefaultObjexID("Product", 5);
        final ObjexID prod6Id = new DefaultObjexID("Product", 6);
        final ObjexID prod7Id = new DefaultObjexID("Product", 7);
        final ObjexID prod8Id = new DefaultObjexID("Product", 8);
        
        // The Beans
        CategoryBean cat1Bean = new CategoryBean("Cat1", "Cat1");
        addSubCategories(cat1Bean, cat2Id.toString(), cat3Id.toString());
        addProducts(cat1Bean, prod1Id.toString(), prod2Id.toString());
        cat1Bean.setMainProduct(prod3Id.toString());
        
        CategoryBean cat2Bean = new CategoryBean("Cat2", "Cat2");
        addProducts(cat2Bean, prod3Id.toString(), prod4Id.toString());
        cat2Bean.setMainProduct(prod3Id.toString());
        
        CategoryBean cat3Bean = new CategoryBean("Cat3", "Cat3");
        addSubCategories(cat3Bean, cat4Id.toString());
        addProducts(cat3Bean, prod5Id.toString(), prod6Id.toString());
        cat3Bean.setMainProduct(prod5Id.toString());
        
        CategoryBean cat4Bean = new CategoryBean("Cat4", "Cat4");
        addProducts(cat4Bean, prod7Id.toString(), prod8Id.toString());
        cat4Bean.setMainProduct(prod7Id.toString());
        
        // The Impl Classes
        cat1 = new CategoryImpl(cat1Bean);
        cat1.init(container, cat1Id, null);
        cat2 = new CategoryImpl(cat2Bean);
        cat2.init(container, cat2Id, cat1.getId());
        cat3 = new CategoryImpl(cat3Bean);
        cat3.init(container, cat3Id, cat1.getId());
        cat4 = new CategoryImpl(cat4Bean);
        cat4.init(container, cat4Id, cat3.getId());
        
        final ProductImpl prod1 = new ProductImpl(new ProductBean("Product1", "Product1", 1, 10));
        prod1.init(container, prod1Id, cat1.getId());
        final ProductImpl prod2 = new ProductImpl(new ProductBean("Product2", "Product2", 1, 10));
        prod2.init(container, prod2Id, cat1.getId());
        final ProductImpl prod3 = new ProductImpl(new ProductBean("Product3", "Product3", 1, 10));
        prod3.init(container, prod3Id, cat2.getId());
        final ProductImpl prod4 = new ProductImpl(new ProductBean("Product4", "Product4", 1, 10));
        prod4.init(container, prod4Id, cat2.getId());
        final ProductImpl prod5 = new ProductImpl(new ProductBean("Product5", "Product5", 1, 10));
        prod5.init(container, prod5Id, cat3.getId());
        final ProductImpl prod6 = new ProductImpl(new ProductBean("Product6", "Product6", 1, 10));
        prod6.init(container, prod6Id, cat3.getId());
        final ProductImpl prod7 = new ProductImpl(new ProductBean("Product7", "Product7", 1, 10));
        prod7.init(container, prod7Id, cat4.getId());
        final ProductImpl prod8 = new ProductImpl(new ProductBean("Product8", "Product8", 1, 10));
        prod8.init(container, prod8Id, cat4.getId());
        
        context.checking(new Expectations() {{
            allowing(container).getObject(cat1Id.toString()); will(returnValue(cat1));
            allowing(container).getObject(cat2Id.toString()); will(returnValue(cat2));
            allowing(container).getObject(cat3Id.toString()); will(returnValue(cat3));
            allowing(container).getObject(cat4Id.toString()); will(returnValue(cat4));
            allowing(container).getObject(prod1Id.toString()); will(returnValue(prod1));
            allowing(container).getObject(prod2Id.toString()); will(returnValue(prod2));
            allowing(container).getObject(prod3Id.toString()); will(returnValue(prod3));
            allowing(container).getObject(prod4Id.toString()); will(returnValue(prod4));
            allowing(container).getObject(prod5Id.toString()); will(returnValue(prod5));
            allowing(container).getObject(prod6Id.toString()); will(returnValue(prod6));
            allowing(container).getObject(prod7Id.toString()); will(returnValue(prod7));
            allowing(container).getObject(prod8Id.toString()); will(returnValue(prod8));
        }});
        
        this.container = container;
    }
    
    /**
     * Ensures the default 1 level recursion works
     */
    @Test
    public void basic() {
        RecursiveObjectCompiler compiler = new RecursiveObjectCompiler(container);
        Map<ObjexID, ObjexObj> objs = compiler.getObjects(cat1);
        
        Assert.assertNotNull(objs);
        Assert.assertEquals(6, objs.size());
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
        Assert.assertEquals(2, objs.size());
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
        Assert.assertEquals(5, objs.size());
    }
    
    /**
     * Helper to set categories on a category
     */
    private void addSubCategories(CategoryBean bean, String... categories) {
        Map<String, String> cats = new HashMap<String, String>();
        for( String cat : categories ) {
            cats.put(cat, cat);
        }
        
        bean.setCategories(cats);
    }
    
    /**
     * Helper to set products on a category
     */
    private void addProducts(CategoryBean bean, String... products) {
        List<String> prods = new ArrayList<String>();
        for( String product : products ) {
            prods.add(product);
        }
        
        bean.setProducts(prods);
    }
}
