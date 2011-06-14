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
package org.talframework.objexj.container.middleware;

import java.util.HashMap;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.exceptions.ContainerExistsException;

/**
 * Simple test for the in memory middleware factory
 *
 * @author Tom Spencer
 */
public class TestInMemoryMiddlewareFactory {
    
    private Mockery context = new JUnit4Mockery();
    private ContainerStrategy strategy = null;
    
    @Before
    public void setup() {
        strategy = context.mock(ContainerStrategy.class);
        
        SingletonContainerStore.getInstance().setObjects("Test/1234", new HashMap<ObjexID, Map<String,Object>>());
    }
    
    @After
    public void end() {
        SingletonContainerStore.getInstance().setObjects("999", null);
    }

    @Test
    public void get() {
        InMemoryMiddlewareFactory factory = new InMemoryMiddlewareFactory();
        Assert.assertNotNull(factory.getMiddleware(strategy, "Test/1234"));
    }
    
    @Test
    public void create() {
        context.checking(new Expectations() {{
            oneOf(strategy).getContainerId(); will(returnValue(null));
        }});
        
        InMemoryMiddlewareFactory factory = new InMemoryMiddlewareFactory();
        Assert.assertNotNull(factory.createContainer(strategy));
    }

    @Test(expected=ContainerExistsException.class)
    public void noCreateIfExists() {
        context.checking(new Expectations() {{
            oneOf(strategy).getContainerId(); will(returnValue("999"));
        }});
        
        SingletonContainerStore.getInstance().setObjects("999", new HashMap<ObjexID, Map<String,Object>>());
        
        InMemoryMiddlewareFactory factory = new InMemoryMiddlewareFactory();
        factory.createContainer(strategy);
    }
    
    @Test
    public void open() {
        InMemoryMiddlewareFactory factory = new InMemoryMiddlewareFactory();
        Assert.assertNotNull(factory.getTransaction(strategy, "Test/1234"));
    }
}
