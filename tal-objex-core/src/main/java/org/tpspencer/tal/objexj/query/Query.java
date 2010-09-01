package org.tpspencer.tal.objexj.query;

import org.tpspencer.tal.objexj.container.ContainerStrategy;
import org.tpspencer.tal.objexj.container.InternalContainer;

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
