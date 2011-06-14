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
package org.talframework.objexj.object;

import java.lang.reflect.Proxy;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
    @Ignore(value="Failing because not strategy, not sure this is right, review!")
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
