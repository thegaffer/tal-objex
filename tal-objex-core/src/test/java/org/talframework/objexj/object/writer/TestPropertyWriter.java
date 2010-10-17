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

package org.talframework.objexj.object.writer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.object.testbeans.CategoryBean;
import org.talframework.objexj.object.testbeans.ProductBean;
import org.talframework.objexj.object.testmodel.ProductImpl;

/**
 * This class tests the property writer.
 *
 * @author Tom Spencer
 */
public class TestPropertyWriter {
    
    private Mockery context = new JUnit4Mockery();

    /**
     * Tests a simple object
     */
    @Test
    public void basic() {
        ProductBean product = createTestProduct();
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter printer = new PrintWriter(stringWriter);
        PropertyWriter writer = new PropertyWriter(printer);
        writer.writeState("test[product]", new DefaultObjexID("Product", 1).toString(), product);
        
        StringBuilder buf = new StringBuilder();
        buf.append("test[product].id=Product/1").append('\n');
        buf.append("test[product].objType=Product").append('\n');
        buf.append("test[product].name=Product1").append('\n');
        buf.append("test[product].description=Description of Product").append('\n');
        buf.append("test[product].stockLevel=2").append('\n');
        buf.append("test[product].price=99.99").append('\n');
        Assert.assertEquals(buf.toString(), stringWriter.toString());
    }
    
    /**
     * Tests a simple object, but as an ObjexObj
     */
    @Test
    public void asObject() {
        ProductBean product = createTestProduct();
        ProductImpl obj = new ProductImpl(product);
        obj.init(context.mock(InternalContainer.class), new DefaultObjexID("Product", 1), new DefaultObjexID("Category", 1));
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter printer = new PrintWriter(stringWriter);
        PropertyWriter writer = new PropertyWriter(printer);
        writer.writeObject("test[product]", obj);
        
        StringBuilder buf = new StringBuilder();
        buf.append("test[product].id=Product/1").append('\n');
        buf.append("test[product].parentId=Category/1").append('\n');
        buf.append("test[product].objType=Product").append('\n');
        buf.append("test[product].name=Product1").append('\n');
        buf.append("test[product].description=Description of Product").append('\n');
        buf.append("test[product].stockLevel=2").append('\n');
        buf.append("test[product].price=99.99").append('\n');
        Assert.assertEquals(buf.toString(), stringWriter.toString());
    }
    
    /**
     * Tests an object that has references
     */
    @Test
    public void withReferences() {
        CategoryBean category = createTestCategory();
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter printer = new PrintWriter(stringWriter);
        PropertyWriter writer = new PropertyWriter(printer);
        writer.writeState("test[cat]", new DefaultObjexID("Category", 1).toString(), category);
     
        StringBuilder buf = new StringBuilder();
        buf.append("test[cat].id=Category/1").append('\n');
        buf.append("test[cat].objType=Category").append('\n');
        buf.append("test[cat].name=Cat1").append('\n');
        buf.append("test[cat].description=Description of Category").append('\n');
        buf.append("test[cat].mainProduct=Product/1").append('\n');
        buf.append("test[cat].products[0]=Product/1").append('\n');
        buf.append("test[cat].products[1]=Product/3").append('\n');
        buf.append("test[cat].categories[laptops]=Category/5").append('\n');
        buf.append("test[cat].categories[printers]=Category/9").append('\n');
        buf.append("test[cat].categories[desktops]=Category/8").append('\n');
        Assert.assertEquals(buf.toString(), stringWriter.toString());
    }
    
    /**
     * As above, but only child output so no mainProduct
     * will be output.
     */
    @Test
    public void childrenOnly() {
        CategoryBean category = createTestCategory();
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter printer = new PrintWriter(stringWriter);
        PropertyWriter writer = new PropertyWriter(printer);
        writer.setIgnoreReferenced();
        writer.writeState("test[cat]", new DefaultObjexID("Category", 1).toString(), category);
     
        StringBuilder buf = new StringBuilder();
        buf.append("test[cat].id=Category/1").append('\n');
        buf.append("test[cat].objType=Category").append('\n');
        buf.append("test[cat].name=Cat1").append('\n');
        buf.append("test[cat].description=Description of Category").append('\n');
        buf.append("test[cat].products[0]=Product/1").append('\n');
        buf.append("test[cat].products[1]=Product/3").append('\n');
        buf.append("test[cat].categories[laptops]=Category/5").append('\n');
        buf.append("test[cat].categories[printers]=Category/9").append('\n');
        buf.append("test[cat].categories[desktops]=Category/8").append('\n');
        Assert.assertEquals(buf.toString(), stringWriter.toString());
    }
    
    /**
     * As above, but only references output so no products
     * (other than mainProduct) or categories will be output.
     */
    @Test
    public void referencesOnly() {
        CategoryBean category = createTestCategory();
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter printer = new PrintWriter(stringWriter);
        PropertyWriter writer = new PropertyWriter(printer);
        writer.setIgnoreOwned();
        writer.writeState("test[cat]", new DefaultObjexID("Category", 1).toString(), category);
     
        StringBuilder buf = new StringBuilder();
        buf.append("test[cat].id=Category/1").append('\n');
        buf.append("test[cat].objType=Category").append('\n');
        buf.append("test[cat].name=Cat1").append('\n');
        buf.append("test[cat].description=Description of Category").append('\n');
        buf.append("test[cat].mainProduct=Product/1").append('\n');
        Assert.assertEquals(buf.toString(), stringWriter.toString());
    }
    
    /**
     * @return A test product to use in tests
     */
    private ProductBean createTestProduct() {
        return new ProductBean("Product1", "Description of Product", 2, 99.99);
    }
    
    /**
     * @return A test category to use in tests
     */
    private CategoryBean createTestCategory() {
        List<String> products = new ArrayList<String>();
        products.add("Product/1");
        products.add("Product/3");
        
        Map<String, String> subCategories = new HashMap<String, String>();
        subCategories.put("laptops", "Category/5");
        subCategories.put("desktops", "Category/8");
        subCategories.put("printers", "Category/9");
        
        CategoryBean category = new CategoryBean("Cat1", "Description of Category");
        category.setMainProduct("Product/1");
        category.setProducts(products);
        category.setCategories(subCategories);
        
        return category;
    }
}
