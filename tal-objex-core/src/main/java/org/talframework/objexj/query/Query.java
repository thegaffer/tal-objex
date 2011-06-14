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
package org.talframework.objexj.query;

import org.talframework.objexj.QueryRequest;
import org.talframework.objexj.QueryResult;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.InternalContainer;

/**
 * This internal interface represents a query against a
 * container. In the default container implementation an
 * instance of this class is registered against the
 * ContainerStrategy and told to execute.
 * 
 * @author Tom Spencer
 */
public interface Query {
    
    /**
     * Call to execute the query against the given container with
     * the given parameters.
     * 
     * TODO: Do not like the passing of InternalContainer or Strategy here!!! Cyclical dependencies!
     * 
     * @param container The container to run against
     * @param strategy The container strategy (used to get at the object strategies)
     * @param request The request and its parameters
     * @return The result of the query
     */
    public QueryResult execute(InternalContainer container, ContainerStrategy strategy, QueryRequest request);
}
