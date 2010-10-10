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

package org.talframework.objexj.runtime.gae.query;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withOffset;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.QueryRequest;
import org.talframework.objexj.QueryResult;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.DefaultObjexID;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.query.DefaultQueryResult;
import org.talframework.objexj.runtime.gae.GAEMiddlewareFactory;
import org.talframework.objexj.runtime.gae.persistence.PersistenceManagerFactorySingleton;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultIterator;

/**
 * This helper base class contains the logic for actually running
 * a GAE based query and extracting the results. You may derive
 * from this class to implement your own GAE based queries.
 * 
 * @author Tom Spencer
 */
public abstract class GAEBaseQuery implements org.talframework.objexj.query.Query {
    
    /**
     * Helper to initially prepare and create the query. Note that
     * the query will be prepared to only return keys.
     * 
     * @param type The entity type we are searching for
     * @param containerId The ID of the container
     * @return The query
     */
    protected Query getQuery(String type, String containerId) {
        Query ret = null;
        
        if( containerId != null ) {
            Key key = GAEMiddlewareFactory.getContainerKey(containerId, true);
            ret = new Query(type, key);
            ret.setKeysOnly();
        }
        else {
            ret = new Query(type);
        }
        
        return ret;
    }

    /**
     * Helper to execute the query and get the prepared query back
     * from which results can be obtained
     * 
     * @param query The query
     * @return The prepared query ready to get results from
     */
    protected PreparedQuery executeQuery(Query query) {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        return service.prepare(query);
    }
    
    /**
     * Helper to execute the query and get the results
     * 
     * @param query The prepared query
     * @param container The container the query is being performed on
     * @param strategy The strategy for the container
     * @param request The request
     * @return The result
     */
    protected QueryResult retreiveResults(Query query, InternalContainer container, ContainerStrategy strategy, QueryRequest request) {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        return retreiveResults(service.prepare(query), container, strategy, request);
    }
    
    /**
     * Helper to get the results from a prepared query. This
     * can be done in 1 step using the alternative retreiveResults
     * method.
     * 
     * FUTURE: This method is slow because JDO getObjectsByID does not appear to work. This should be changed
     * 
     * @param query The prepared query
     * @param container The container the query is being performed on
     * @param strategy The strategy for the container
     * @param request The request
     * @return The result
     */
    protected QueryResult retreiveResults(PreparedQuery query, InternalContainer container, ContainerStrategy strategy, QueryRequest request) {
        QueryResult ret = null;
        
        // Get offset
        int offset = request.getOffset();
        
        // Extract the cursor if there is one
        Cursor cursor = null;
        if( offset > 0 && request.getHandle() != null ) {
            cursor = Cursor.fromWebSafeString(request.getHandle().toString());
        }
        
        // Ensure sensible limit
        int limit = request.getPageSize();
        if( limit <= 0 || limit > 30 ) limit = 30;
        
        PersistenceManager pm = PersistenceManagerFactorySingleton.getManager();
        try {
            List<ObjexObj> res = new ArrayList<ObjexObj>();
            
            QueryResultIterator<Entity> qrl = null;
            
            if( cursor != null ) qrl = query.asQueryResultIterator(withLimit(limit).cursor(cursor));
            else if( offset > 0 ) qrl = query.asQueryResultIterator(withOffset(offset).limit(limit));
            else qrl = query.asQueryResultIterator(withLimit(limit));
            
            // TODO: This is the slow bit, getting each object!
            while( qrl.hasNext() ) {
                Entity e = qrl.next();
                ObjectStrategy objectStrategy = strategy.getObjectStrategyForState(e.getKind());
                
                ObjexObjStateBean bean = pm.getObjectById(objectStrategy.getStateClass(), e.getKey());
                ObjexID id = getObjexId(objectStrategy.getTypeName(), e.getKey());
                ObjexID parentId = bean.getParentId() != null ? new DefaultObjexID(bean.getParentId()) : null;
                res.add(objectStrategy.getObjexObjInstance(container, parentId, id, bean));
            }
            
            ret = new DefaultQueryResult(
                    offset, 
                    request.isMaxResultsRequired() ? query.countEntities() : -1, 
                    res, 
                    res.size() < limit ? true : false, 
                    qrl.getCursor().toWebSafeString());
        }
        finally {
            if( pm != null ) pm.close();
        }
        
        return ret;
    }
    
    private ObjexID getObjexId(String type, Key key) {
        if( key.getName() != null ) return new DefaultObjexID(type, key.getName());
        else return new DefaultObjexID(type, key.getId());
    }
}
