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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.object.testmodel.pojo.SimpleProduct;

/**
 * This class tests the behaviour of the {@link SimpleObjexObj}
 * class. It only includes tests for that beyond {@link BaseObjexObj}
 * test.
 * 
 * @author Tom Spencer
 */
public class TestSimpleObjexObj {
    
    /**
     * Ensures after setup the state bean and strategy are returned
     */
    @Test
    public void basic() {
        ObjectStrategy strategy = mock(ObjectStrategy.class);
        SimpleProduct product = new SimpleProduct();
        
        SimpleObjexObj test = new SimpleObjexObj(strategy, product);
        
        assertEquals(product, test.getStateBean());
        assertEquals(strategy, test.getStrategy());
    }
    
    /**
     * Ensures the type comes not from the class, but from
     * the strategy
     */
    @Test
    public void typeFromStrategy() {
        ObjectStrategy strategy = mock(ObjectStrategy.class);
        SimpleProduct product = new SimpleProduct();
        
        SimpleObjexObj test = new SimpleObjexObj(strategy, product);
        
        when(strategy.getTypeName()).thenReturn("Product");
        
        assertEquals("Product", test.getType());
    }
}
