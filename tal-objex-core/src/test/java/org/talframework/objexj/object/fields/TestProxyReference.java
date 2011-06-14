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
package org.talframework.objexj.object.fields;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;

/**
 * This class tests the proxy reference field
 *
 * @author Tom Spencer
 */
public class TestProxyReference {
    
    private Mockery context = new JUnit4Mockery();

    /**
     * Simple test to ensure we do not get the object for the simple methods
     */
    @Test
    public void basic() {
        final Container container = context.mock(Container.class);
        
        ProxyReference ref = new ProxyReference(new DefaultObjexID("test", 123), container);
        
        Assert.assertEquals(new DefaultObjexID("test", 123), ref.getId());
        Assert.assertEquals("test", ref.getType());
        Assert.assertEquals(container, ref.getContainer());
    }
    
    /**
     * Ensures we go to the container when we need to get the object
     */
    @Test
    public void getObject() {
        final Container container = context.mock(Container.class);
        final ObjexObj obj = context.mock(ObjexObj.class);
        final ObjexID id = new DefaultObjexID("test", 123);
        
        context.checking(new Expectations() {{
            oneOf(container).getObject(id); will(returnValue(obj));
            oneOf(obj).getProperty("test"); will(returnValue("Testing 123"));
        }});
        
        ProxyReference ref = new ProxyReference(id, container);
        
        Assert.assertEquals("Testing 123", ref.getProperty("test"));
    }
}
