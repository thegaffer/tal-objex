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
