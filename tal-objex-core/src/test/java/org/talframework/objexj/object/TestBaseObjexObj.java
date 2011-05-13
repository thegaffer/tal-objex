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

import java.lang.reflect.Proxy;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.exceptions.ObjectFieldInvalidException;
import org.talframework.objexj.object.fields.ProxyReference;
import org.talframework.objexj.object.testmodel.api.ICategory;
import org.talframework.objexj.object.testmodel.api.IProduct;
import org.talframework.objexj.object.testmodel.objex.Category;

/**
 * This test class tests the {@link BaseObjexObj} class.
 *
 * @author Tom Spencer
 */
public class TestBaseObjexObj {
    
    private Mockery context = new JUnit4Mockery();
    
    private InternalContainer container;
    private ObjexID id;
    private ObjexID parentId;
    private ObjexObj parent;
    private BaseObjexObj underTest;

    @Before
    public void setup() {
        container = context.mock(InternalContainer.class);
        id = context.mock(ObjexID.class, "id");
        parentId = context.mock(ObjexID.class, "parentId");
        parent = context.mock(ObjexObj.class, "parent");
        
        context.checking(new Expectations() {{
            allowing(id).isNull(); will(returnValue(false));
            allowing(parentId).isNull(); will(returnValue(false));
            allowing(container).getObject(parentId); will(returnValue(parent));
            allowing(container).isOpen(); will(returnValue(true));
        }});
        
        underTest = new Category("Cat1", "Cat1 Desc");
        underTest.init(container, id, parentId);
        ((Category)underTest).setMainProduct(IProduct.class.cast(Proxy.newProxyInstance(
                this.getClass().getClassLoader(), 
                new Class<?>[]{ObjexObj.class, IProduct.class}, 
                new ProxyReference(DefaultObjexID.getId("Product/101"), container))));
    }
    
    /**
     * Ensures the basic access to the properties
     * via Dummy
     */
    @Test
    public void basic() {
        Assert.assertEquals(id, underTest.getId());
        Assert.assertEquals("Category", underTest.getType());
        Assert.assertEquals(parentId, underTest.getParentId());
        Assert.assertEquals(parent, underTest.getParent());
        Assert.assertEquals(container, underTest.getContainer());
        Assert.assertTrue(underTest.isUpdateable());
        
        Assert.assertEquals(underTest, underTest.getBehaviour(ICategory.class));
        
        Assert.assertEquals("Cat1", underTest.getProperty("name", String.class));
        Assert.assertEquals("Cat1 Desc", underTest.getPropertyAsString("description"));
        
        final ObjexObj mainProduct = context.mock(ObjexObj.class, "mainProduct");
        context.checking(new Expectations() {{
            oneOf(container).getObject(DefaultObjexID.getId("Product/101")); will(returnValue(mainProduct));
            oneOf(mainProduct).getParentId(); will(returnValue(DefaultObjexID.getId("Category/110")));
        }});
        Assert.assertEquals(DefaultObjexID.getId("Category/110"), ((ObjexObj)underTest.getProperty("mainProduct")).getParentId());
        
        context.assertIsSatisfied();
    }
    
    /**
     * Ensures we cannot use the base setProperty on the base object
     */
    public void cannotSet() {
        underTest.setProperty("name", "Cat1_edited");
        Assert.assertEquals("Cat1_edited", underTest.getProperty("name"));
    }
    
    /**
     * Ensures we cannot access a property for which
     * there is not an accessor for
     */
    @Test(expected=ObjectFieldInvalidException.class)
    public void invalidProperty() {
        underTest.getProperty("invalid");
    }
    
    /**
     * Ensures we cannot set a purely read-only property
     */
    @Test(expected=ObjectFieldInvalidException.class)
    public void invalidSet() {
        underTest.setProperty("name2", "test");
    }
    
    @Test(expected=ClassCastException.class)
    public void invalidBehaviour() {
        underTest.getBehaviour(String.class);
    }
    
    @Test
    public void testRoot() {
        context.checking(new Expectations() {{
            oneOf(parent).getRoot(); will(returnValue(parent));
        }});
        
        Assert.assertEquals(parent, underTest.getRoot());
        
        // Now try with self being root
        underTest.init(container, id, null);
        Assert.assertEquals(underTest, underTest.getRoot());
    }
    
    @Test(expected=IllegalStateException.class)
    public void testNotInitialised() {
        underTest = new Category();
        underTest.getId();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testInitialiseNoId() {
        underTest.init(container, null, parentId);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testInitialiseNullId() {
        final ObjexID nullId = context.mock(ObjexID.class, "nullId");
        context.checking(new Expectations() {{
            oneOf(nullId).isNull(); will(returnValue(true));
        }});
        
        underTest.init(container, null, parentId);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testInitialiseNoContainer() {
        underTest.init(null, id, parentId);
    }
    
    
    
}
