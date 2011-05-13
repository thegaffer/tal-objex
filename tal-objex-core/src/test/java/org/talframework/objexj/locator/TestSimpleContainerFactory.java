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

package org.talframework.objexj.locator;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerMiddlewareFactory;
import org.talframework.objexj.container.ContainerObjectCache;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.container.ObjectStrategy;

/**
 * TODO: Write unit tests
 * 
 * @author Tom Spencer
 */
public class TestSimpleContainerFactory {

    @Test
    public void create() {
        ContainerStrategy strategy = mock(ContainerStrategy.class);
        ContainerMiddlewareFactory middlewareFactory = mock(ContainerMiddlewareFactory.class);
        ContainerMiddleware middleware = mock(ContainerMiddleware.class);
        ContainerObjectCache cache = mock(ContainerObjectCache.class);
        
        ObjectStrategy rootStrategy =  mock(ObjectStrategy.class);
        ObjexObj root = mock(ObjexObj.class);
        
        when(middlewareFactory.createContainer(strategy)).thenReturn(middleware);
        when(middleware.init(any(Container.class))).thenReturn(cache);
        when(strategy.getRootObjectName()).thenReturn("Root");
        when(strategy.getObjectStrategy("Root")).thenReturn(rootStrategy);
        when(rootStrategy.getTypeName()).thenReturn("Root");
        when(rootStrategy.createInstance(any(InternalContainer.class), (ObjexID)isNull(), eq(new DefaultObjexID("Root", 1)), isNull())).thenReturn(root);
        
        SimpleContainerFactory underTest = new SimpleContainerFactory(strategy, middlewareFactory);
        
        assertNotNull(underTest.create());
        verify(cache).addObject(root, true);
    }
    
	@Test
	public void get() {
	    ContainerStrategy strategy = mock(ContainerStrategy.class);
        ContainerMiddlewareFactory middlewareFactory = mock(ContainerMiddlewareFactory.class);
        ContainerMiddleware middleware = mock(ContainerMiddleware.class);
        
        when(middlewareFactory.getMiddleware(strategy, "123")).thenReturn(middleware);
        when(middleware.init(any(Container.class))).thenReturn(null);
        when(middleware.getContainerId()).thenReturn("123");
        
        SimpleContainerFactory underTest = new SimpleContainerFactory(strategy, middlewareFactory);
        
        Container container = underTest.get("123");
        assertNotNull(container);
        verify(middlewareFactory).getMiddleware(strategy, "123");
        verify(middleware).init(container);
        verify(middleware).getContainerId();
        verifyNoMoreInteractions(middlewareFactory, middleware, strategy);
    }
	
	@Test
    public void open() {
	    ContainerStrategy strategy = mock(ContainerStrategy.class);
        ContainerMiddlewareFactory middlewareFactory = mock(ContainerMiddlewareFactory.class);
        ContainerMiddleware middleware = mock(ContainerMiddleware.class);
        ContainerObjectCache cache = mock(ContainerObjectCache.class);
        
        when(middlewareFactory.getTransaction(strategy, "123")).thenReturn(middleware);
        when(middleware.init(any(Container.class))).thenReturn(cache);
        when(middleware.getContainerId()).thenReturn("123");
        
        SimpleContainerFactory underTest = new SimpleContainerFactory(strategy, middlewareFactory);
        
        Container container = underTest.open("123");
        assertNotNull(container);
        verify(middlewareFactory).getTransaction(strategy, "123");
        verify(middleware).init(container);
        verify(middleware).getContainerId();
        verifyNoMoreInteractions(middlewareFactory, middleware, strategy);
    }
}
