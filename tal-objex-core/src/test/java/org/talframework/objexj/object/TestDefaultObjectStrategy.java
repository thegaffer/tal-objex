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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObj.ObjexFieldType;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.container.ObjectStrategy.PropertyCharacteristic;
import org.talframework.objexj.container.ObjectStrategy.PropertyTypeEnum;
import org.talframework.objexj.object.testmodel.api.ICategory;
import org.talframework.objexj.object.testmodel.objex.Category;
import org.talframework.objexj.object.testmodel.pojo.SimpleProduct;

/**
 * Tests the {@link DefaultObjectStrategy} class.
 * 
 * @author Tom Spencer
 */
public class TestDefaultObjectStrategy {
    
    private InternalContainer container;
    
    @Before
    public void setup() {
        container = mock(InternalContainer.class);
    }

	@Test
	public void basic() {
	    DefaultObjectStrategy strategy = new DefaultObjectStrategy(Category.class, new ObjectStrategy.PropertyStrategy("name", String.class, ObjexFieldType.STRING, PropertyTypeEnum.STRING, PropertyCharacteristic.PERSISTENT));
	    
	    // Simple tests
	    assertEquals("Category", strategy.getTypeName());
	    assertEquals(Category.class, strategy.getMainClass());
	    assertNull(strategy.getObjexID(new Category()));
	    assertNotNull(strategy.getProperty("name"));
	    assertNull(strategy.getProperty("nonExistent"));
	    
	    // Construction
	    ObjexObj obj = strategy.createInstance(container, new DefaultObjexID("Parent", 1), new DefaultObjexID("Category", 2), null);
	    assertNotNull(obj);
	    assertTrue(obj instanceof Category);
	    
	    obj = strategy.createReferenceProxy(container, new DefaultObjexID("Category", 2));
	    assertNotNull(obj);
	    assertTrue(obj instanceof ICategory);
	    assertFalse(obj instanceof Category);
    }
	
	/**
	 * Ensures strategy works with proxy object
	 */
	@Test
    public void proxyObj() {
	    DefaultObjectStrategy strategy = new DefaultObjectStrategy(org.talframework.objexj.object.testmodel.pojo.Category.class, new ObjectStrategy.PropertyStrategy("name", String.class, ObjexFieldType.STRING, PropertyTypeEnum.STRING, PropertyCharacteristic.PERSISTENT));
	    
	    ObjexObj obj = strategy.createInstance(container, new DefaultObjexID("Parent", 1), new DefaultObjexID("Category", 2), null);
        assertNotNull(obj);
        assertFalse(obj instanceof Category);
        assertTrue(obj instanceof ICategory);
        
        obj = strategy.createReferenceProxy(container, new DefaultObjexID("Category", 2));
        assertNotNull(obj);
        assertTrue(obj instanceof ICategory);
    }
	
	/**
	 * Ensures strategy works with simple object. Also tests that we clone
	 * an object if the seed is provided
	 */
	@Test
	public void simpleObj() {
	    DefaultObjectStrategy underTest = new DefaultObjectStrategy(
	            "Product",
	            SimpleProduct.class, 
	            new ObjectStrategy.PropertyStrategy("name", String.class, ObjexFieldType.STRING, PropertyTypeEnum.STRING, PropertyCharacteristic.PERSISTENT),
	            new ObjectStrategy.PropertyStrategy("description", String.class, ObjexFieldType.STRING, PropertyTypeEnum.STRING, PropertyCharacteristic.PERSISTENT),
	            new ObjectStrategy.PropertyStrategy("price", double.class, ObjexFieldType.NUMBER, PropertyTypeEnum.DOUBLE, PropertyCharacteristic.PERSISTENT),
	            new ObjectStrategy.PropertyStrategy("stockLevel", int.class, ObjexFieldType.NUMBER, PropertyTypeEnum.INT, PropertyCharacteristic.PERSISTENT));
	    
	    assertEquals("Product", underTest.getTypeName());
	    
	    SimpleProduct seed = new SimpleProduct("Seed", "Description of Seed", 65, 14.95);
	    ObjexObj obj = underTest.createInstance(container, null, new DefaultObjexID("Product", 1), seed);
	    assertEquals("Seed", obj.getProperty("name"));
	    assertEquals("Description of Seed", obj.getProperty("description"));
	    assertEquals(14.95, obj.getProperty("price"));
	    assertEquals(65, obj.getProperty("stockLevel"));
	}
	
	
}
