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
package org.talframework.objexj.runtimes.gae;

import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerMiddlewareFactory;
import org.talframework.objexj.container.ContainerStrategy;

/**
 * Implements the {@link ContainerMiddlewareFactory} interface to
 * the Google App Engine backed runtime service.
 *
 * @author Tom Spencer
 */
public class GAEMiddlewareFactory implements ContainerMiddlewareFactory {

    /**
     * {@inheritDoc}
     * 
     * <p>This implementation creates a GAEMiddleware on the stratgies
     * type</p>
     */
    @Override
    public ContainerMiddleware createContainer(ContainerStrategy strategy) {
        return new GAEMiddleware(strategy.getContainerName());
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>This implementation creates the middleware based on the ID. The
     * returned middleware may or may not be open depending on whether ID
     * is a transaction id or not</p>
     */
    @Override
    public ContainerMiddleware getMiddleware(ContainerStrategy strategy, String id) {
        return new GAEMiddleware(strategy.getContainerName(), id);
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>This implementation creates the middleware. If, having done this, 
     * the middleware is not open, it is opened.</p>
     */
    @Override
    public ContainerMiddleware getTransaction(ContainerStrategy strategy, String id) {
        GAEMiddleware middleware = new GAEMiddleware(strategy.getContainerName(), id);
        if( !middleware.isOpen() ) middleware.open();
        return middleware;
    }
}
