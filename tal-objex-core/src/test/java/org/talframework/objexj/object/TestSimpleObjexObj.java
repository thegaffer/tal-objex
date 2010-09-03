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

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.exceptions.ObjectFieldInvalidException;
import org.talframework.objexj.service.beans.CategoryBean;

/**
 * This class tests the behaviour of the {@link SimpleObjexObj}
 * class.
 * 
 * @author Tom Spencer
 */
public class TestSimpleObjexObj {
    
    private Mockery context = new JUnit4Mockery();
    
    private ObjectStrategy strategy;
    private InternalContainer container;
    private InternalContainer editContainer;
    private ObjexID testId;
    private ObjexID testParentId;
    private CategoryBean category;
    
    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        strategy = context.mock(ObjectStrategy.class);
        container = context.mock(InternalContainer.class);
        editContainer = context.mock(InternalContainer.class, "editContainer");
        testId = new DefaultObjexID("Category", 1000);
        testParentId = new DefaultObjexID("Category", 500);
        
        final ObjexObj obj = context.mock(ObjexObj.class);
        final List<ObjexObj> productObjs = new ArrayList<ObjexObj>();
        productObjs.add(obj);
        productObjs.add(obj);
        productObjs.add(obj);
        
        final Map<String, ObjexObj> categoryObjs = new HashMap<String, ObjexObj>();
        categoryObjs.put("open", obj);
        categoryObjs.put("closed", obj);
        
        List<String> products = new ArrayList<String>();
        products.add("Product/2");
        products.add("Product/3");
        products.add("Product/4");
        
        Map<String, String> categories = new HashMap<String, String>();
        categories.put("open", "Category/5");
        categories.put("closed", "Category/6");
        
        category = new CategoryBean("Cat1");
        category.setMainProduct("Product/1");
        category.setProducts(products);
        category.setCategories(categories);
        
        final Map<String, Class<?>> referenceProps = new HashMap<String, Class<?>>();
        referenceProps.put("mainProduct", String.class);
        referenceProps.put("products", String.class);
        referenceProps.put("categories", String.class);
        
        context.checking(new Expectations() {{
            allowing(strategy).getTypeName(); will(returnValue("Category"));
            allowing(strategy).getReferenceProperties(); will(returnValue(referenceProps));
            allowing(strategy).getOwnedReferenceProperties(); will(returnValue(referenceProps));
            
            allowing(editContainer).isOpen(); will(returnValue(true));
            allowing(container).isOpen(); will(returnValue(false));
            allowing(container).getObject(with(anything())); will(returnValue(obj));
            allowing(container).getObjectList(with(any(List.class))); will(returnValue(productObjs));
            allowing(container).getObjectMap(with(any(Map.class))); will(returnValue(categoryObjs));
        }});
    }

    /**
     * General test covers most of the read-only logic
     */
	@Test
	public void basic() {
	    SimpleObjexObj obj = new SimpleObjexObj(strategy, category);
		obj.init(container, testId, testParentId);
		
		Assert.assertEquals(testId, obj.getId());
		Assert.assertEquals(testParentId, obj.getParentId());
		Assert.assertEquals("Category", obj.getType());
		Assert.assertEquals(container, obj.getContainer());
		Assert.assertFalse(obj.isUpdateable());
		
		// Getting properties
		Assert.assertEquals("Cat1", obj.getProperty("name", String.class));
		Assert.assertNotNull(obj.getProperty("mainProduct"));
		Assert.assertTrue(obj.getProperty("mainProduct") instanceof ObjexObj);
		Assert.assertNotNull(obj.getProperty("mainProductRef"));
		Assert.assertEquals("Product/1", obj.getProperty("mainProductRef"));
		Assert.assertNotNull(obj.getProperty("products"));
		Assert.assertNotNull(obj.getProperty("categories"));
		
		obj.validate(null);
	}
	
	/**
	 * Ensures we cannot access the obj if not initialised
	 */
	@Test(expected=IllegalStateException.class)
	public void notInitialised() {
	    SimpleObjexObj obj = new SimpleObjexObj(strategy, category);
	    obj.getId();
	}
	
	/**
	 * Ensures obj fails if no strategy is provided
	 */
	@Test(expected=IllegalArgumentException.class)
	public void noStrategy() {
	    new SimpleObjexObj(null, category);
	}
	
	/**
	 * Ensures obj fails if no state bean is provided
	 */
	@Test(expected=IllegalArgumentException.class)
    public void noStateBean() {
	    new SimpleObjexObj(strategy, null);
	}
	
	/**
	 * Ensures we can set basic properties
	 */
	@Test
	public void setProperties() {
	    final SimpleObjexObj obj = new SimpleObjexObj(strategy, category);
        obj.init(editContainer, testId, testParentId);
        
        context.checking(new Expectations() {{
            oneOf(editContainer).addObjectToTransaction(obj, category);
        }});
        
        obj.setProperty("name", "Test Set");
        category.setEditable(); // Needed because this is normally set by container
        obj.setProperty("description", "Test Description");
        
        Assert.assertEquals("Test Set", obj.getProperty("name"));
        Assert.assertEquals("Test Description", obj.getProperty("description"));
        
        context.assertIsSatisfied();
	}
	
	/**
	 * Ensures we fail if trying to
	 */
	@Test(expected=IllegalArgumentException.class)
    public void setInvalidProperty() {
	    final SimpleObjexObj obj = new SimpleObjexObj(strategy, category);
        obj.init(editContainer, testId, testParentId);
        obj.setProperty("name2", "Test");
	}
	
	/**
	 * Ensures we fail if trying to set property with 
	 * incompatible type.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void setPropertyWithWrongType() {
	    final SimpleObjexObj obj = new SimpleObjexObj(strategy, category);
        obj.init(editContainer, testId, testParentId);
        obj.setProperty("name", new Long(1));
	}
	
	/**
	 * Ensures we can set normal reference properties
	 */
	@Test
	public void setReferenceProperty() {
	    final SimpleObjexObj obj = new SimpleObjexObj(strategy, category);
        obj.init(editContainer, testId, testParentId);
        
        final ObjexObj product = context.mock(ObjexObj.class, "product");
        
        context.checking(new Expectations() {{
            oneOf(product).getId(); will(returnValue(new DefaultObjexID("Product/101")));
            oneOf(editContainer).addObjectToTransaction(obj, category);
        }});
        
        obj.setProperty("mainProduct", product);
        Assert.assertEquals("Product/101", obj.getProperty("mainProductRef"));
        context.assertIsSatisfied();
	}
	
	/**
	 * Ensures we cannot directly set ref property with a
	 * ID list.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void setReferencePropertyWithReference() {
	    final SimpleObjexObj obj = new SimpleObjexObj(strategy, category);
        obj.init(editContainer, testId, testParentId);
        
        context.checking(new Expectations() {{
            oneOf(editContainer).addObjectToTransaction(obj, category);
        }});
        
        obj.setProperty("mainProductRef", "Product/101"); // This will work
        Assert.assertEquals("Product/101", obj.getProperty("mainProductRef"));
        context.assertIsSatisfied();
        
        obj.setProperty("mainProduct", "Product/101"); // This will not!
    }
	
	/**
	 * Ensures we can set reference list properties
	 */
	@Test
	public void setReferenceList() {
	    final SimpleObjexObj obj = new SimpleObjexObj(strategy, category);
        obj.init(editContainer, testId, testParentId);
        
        final ObjexObj product1 = context.mock(ObjexObj.class, "product1");
        final ObjexObj product2 = context.mock(ObjexObj.class, "product2");
        List<ObjexObj> products = new ArrayList<ObjexObj>();
        products.add(product1);
        products.add(product2);
        
        context.checking(new Expectations() {{
            oneOf(product1).getId(); will(returnValue(new DefaultObjexID("Product/101")));
            oneOf(product2).getId(); will(returnValue(new DefaultObjexID("Product/102")));
            oneOf(editContainer).addObjectToTransaction(obj, category);
        }});
        
        obj.setProperty("products", products);
        context.assertIsSatisfied();
    }
	
	/**
     * Ensures we can not set reference list properties directly
     * with a list of objex IDs
     */
    @Test(expected=IllegalArgumentException.class)
    public void setReferenceListWithReference() {
        final SimpleObjexObj obj = new SimpleObjexObj(strategy, category);
        obj.init(editContainer, testId, testParentId);
        
        List<String> products = new ArrayList<String>();
        products.add("Product/101");
        products.add("Product/102");
        
        context.checking(new Expectations() {{
            oneOf(editContainer).addObjectToTransaction(obj, category);
        }});
        
        obj.setProperty("productsRef", products); // This will work
        context.assertIsSatisfied();
        
        obj.setProperty("products", products); // This won't
    }
	
	/**
	 * Ensures we can set reference map properties
	 */
    @Test
	public void setReferenceMap() {
	    final SimpleObjexObj obj = new SimpleObjexObj(strategy, category);
        obj.init(editContainer, testId, testParentId);
        
        final ObjexObj cat1 = context.mock(ObjexObj.class, "cat1");
        final ObjexObj cat2 = context.mock(ObjexObj.class, "cat2");
        Map<Object, ObjexObj> cats = new HashMap<Object, ObjexObj>();
        cats.put("open", cat1);
        cats.put("closed", cat2);
        
        context.checking(new Expectations() {{
            oneOf(cat1).getId(); will(returnValue(new DefaultObjexID("Category/201")));
            oneOf(cat2).getId(); will(returnValue(new DefaultObjexID("Category/202")));
            oneOf(editContainer).addObjectToTransaction(obj, category);
        }});
        
        obj.setProperty("categories", cats);
        context.assertIsSatisfied();
	}
    
    /**
     * Ensures we can not set references directly on a reference
     * map property
     */
    @Test(expected=IllegalArgumentException.class)
    public void setReferenceMapWithReferences() {
        final SimpleObjexObj obj = new SimpleObjexObj(strategy, category);
        obj.init(editContainer, testId, testParentId);
        
        Map<Object, Object> cats = new HashMap<Object, Object>();
        cats.put("open", "Category/201");
        cats.put("closed", "Category/202");
        
        context.checking(new Expectations() {{
            oneOf(editContainer).addObjectToTransaction(obj, category);
        }});
        
        obj.setProperty("categoriesRef", cats); // This will work
        context.assertIsSatisfied();
        
        obj.setProperty("categories", cats); // This will fail
    }
    
    /**
     * Ensures we can create a new object successfully
     */
    @Test
    public void createReference() {
        final SimpleObjexObj obj = new SimpleObjexObj(strategy, category);
        obj.init(editContainer, testId, testParentId);
        
        final ObjexObj newProduct = context.mock(ObjexObj.class, "newProduct");
        final ObjexID newProductId = new DefaultObjexID("Product", 301);
        
        context.checking(new Expectations() {{
            oneOf(editContainer).newObject(obj, category, "Product"); will(returnValue(newProduct));
            oneOf(newProduct).getId(); will(returnValue(newProductId));
            oneOf(editContainer).addObjectToTransaction(obj, category);
        }});
        
        ObjexObj createdProduct = obj.createReference("mainProduct", "Product");
        Assert.assertEquals(newProduct, createdProduct);
        Assert.assertEquals("Product/301", obj.getProperty("mainProductRef"));
        
        context.assertIsSatisfied();
    }
    
    /**
     * Ensures we can create a new object on a reference prop
     */
    @Test
    public void createReferenceOnList() {
        final SimpleObjexObj obj = new SimpleObjexObj(strategy, category);
        obj.init(editContainer, testId, testParentId);
        
        final ObjexObj newProduct = context.mock(ObjexObj.class, "newProduct");
        final ObjexID newProductId = new DefaultObjexID("Product", 301);
        
        context.checking(new Expectations() {{
            oneOf(editContainer).newObject(obj, category, "Product"); will(returnValue(newProduct));
            oneOf(newProduct).getId(); will(returnValue(newProductId));
            oneOf(editContainer).addObjectToTransaction(obj, category);
        }});
        
        int size = obj.getProperty("productsRef", List.class).size(); 
        ObjexObj createdProduct = obj.createReference("products", "Product");
        Assert.assertEquals(newProduct, createdProduct);
        Assert.assertTrue(obj.getProperty("productsRef", List.class).size() == (size + 1));
        
        context.assertIsSatisfied();
    }
    
    /**
     * Current maps are not supported by the generic 
     * createReference method because we would not know
     * the key. This test just makes sure of this.
     */
    @Test(expected=ObjectFieldInvalidException.class)
    public void createReferenceOnMap() {
        final SimpleObjexObj obj = new SimpleObjexObj(strategy, category);
        obj.init(editContainer, testId, testParentId);
        obj.createReference("categories", "Category");
    }
    
    /**
     * Ensures property must be an owned reference in order
     * for createReference to consider it
     */
    @Test(expected=ObjectFieldInvalidException.class)
    public void createReferenceOnNonOwnedProperty() {
        final SimpleObjexObj obj = new SimpleObjexObj(strategy, category);
        obj.init(editContainer, testId, testParentId);
        obj.createReference("name", "Product");
    }
}
