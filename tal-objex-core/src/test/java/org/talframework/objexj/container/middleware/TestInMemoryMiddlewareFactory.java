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
