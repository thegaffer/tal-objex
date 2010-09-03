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

package org.talframework.objexj.query;

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
     * @param container The container to run against
     * @param strategy The container strategy (used to get at the object strategies)
     * @param request The request and its parameters
     * @return The result of the query
     */
    public QueryResult execute(InternalContainer container, ContainerStrategy strategy, QueryRequest request);
}
