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

import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerMiddlewareFactory;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.exceptions.ContainerExistsException;
import org.talframework.objexj.exceptions.ContainerNotFoundException;

/**
 * Implements the middleware factory interface over the internal
 * InMemoryMiddleware implementation.
 *
 * @author Tom Spencer
 */
public class InMemoryMiddlewareFactory implements ContainerMiddlewareFactory {

    public ContainerMiddleware getMiddleware(ContainerStrategy strategy, String id) {
        if( SingletonContainerStore.getInstance().getObjects(id) == null ) throw new ContainerNotFoundException(id);
        
        return new InMemoryMiddleware(id, false);
    }
    
    public ContainerMiddleware getTransaction(ContainerStrategy strategy, String transactionId) {
        return new InMemoryMiddleware(transactionId, true);
    }
    
    public ContainerMiddleware createContainer(ContainerStrategy strategy) {
        String id = strategy.getContainerId();
        if( id != null && SingletonContainerStore.getInstance().getObjects(id) != null ) {
            throw new ContainerExistsException(id);
        }
        
        return new InMemoryMiddleware(id, true);
    }
}
