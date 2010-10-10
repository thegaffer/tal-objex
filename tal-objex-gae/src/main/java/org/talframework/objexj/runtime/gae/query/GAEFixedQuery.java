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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.QueryRequest;
import org.talframework.objexj.QueryResult;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.InternalContainer;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * This class implements the Objex Query interface as a fixed 
 * query. The query is fixed in terms that the input parameters
 * are treated as fields to query on and are used as values
 * against the mandatory and optional parameters.
 * 
 * @author Tom Spencer
 */
public final class GAEFixedQuery extends GAEBaseQuery {
    
    private Class<? extends ObjexObjStateBean> kind;
    private Map<String, FilterOperator> mandatoryParameters;
    private Map<String, FilterOperator> parameters;
    private List<String> defaultSorts;
    private boolean containerSpecific = true;
    
    public QueryResult execute(InternalContainer container, ContainerStrategy strategy, QueryRequest request) {
        Query query = getQuery(kind.getSimpleName(), containerSpecific ? container.getId() : null);
        
        addParameters(query, mandatoryParameters, request.getParameters(), true);
        addParameters(query, parameters, request.getParameters(), false);
        
        List<String> sortFields = request.getSortFields();
        addSorts(query, sortFields != null ? sortFields : defaultSorts);
        
        return retreiveResults(query, container, strategy, request);
    }
    
    /**
     * Helper to add parameters to the query
     * 
     * @param query The query
     * @param params The known mandatory parameters and operators
     * @param values The values provided by the request
     * @param mandatory Indicates if its a failure if the parameter is not supplied
     */
    private void addParameters(Query query, Map<String, FilterOperator> params, Map<String, Object> values, boolean mandatory) {
        if( params == null || params.size() == 0 ) return;
        
        Iterator<String> it = params.keySet().iterator();
        while( it.hasNext() ) {
            String name = it.next();
            Object val = null;
            if( values.containsKey(name) ) val = values.get(name);
            else if( mandatory ) throw new IllegalArgumentException("Mandatory parameter not supplied to query: " + name);
            
            if( val != null ) query.addFilter(name, params.get(name), val);
        }
    }
    
    /**
     * Helper to add the correct sort to the query.
     * 
     * @param query The query
     * @param sorts
     * @param values
     * @return
     */
    private boolean addSorts(Query query, List<String> sorts) {
        if( sorts == null || sorts.size() == 0 ) return false;
        
        boolean ret = false;
        
        Iterator<String> it = sorts.iterator();
        while( it.hasNext() ) {
            String sort = it.next();
            
            if( sort.charAt(0) == '>' ) query.addSort(sort.substring(1), SortDirection.ASCENDING);
            else if( sort.charAt(0) == '<' ) query.addSort(sort.substring(1), SortDirection.DESCENDING);
            else query.addSort(sort);
        }
        
        return ret;
    }
    
    /**
     * @return the kind
     */
    public Class<? extends ObjexObjStateBean> getKind() {
        return kind;
    }

    /**
     * @param kind the kind to set
     */
    public void setKind(Class<? extends ObjexObjStateBean> kind) {
        this.kind = kind;
    }
    
    /**
     * @return The parameters in the original string, string format
     */
    public Map<String, String> getMandatoryParameters() {
        return mapParameterStrings(mandatoryParameters);
    }

    /**
     * Call to set the parameters. The parameters should be
     * keyed by field and a string indicating the operator. 
     * The operator should be one of =, !=, <, <=, >, >= or in.
     * 
     * <p>Note: Mandatory are parameters that there must be
     * a value for</p>
     * 
     * @param parameters The mandatory parameters to set.
     */
    public void setMandatoryParameters(Map<String, String> parameters) {
        mandatoryParameters = mapParameters(parameters);
    }
    
    /**
     * @return The parameters in the original string, string format
     */
    public Map<String, String> getParameters() {
        return mapParameterStrings(parameters);
    }

    /**
     * Call to set the parameters. The parameters should be
     * keyed by field and a string indicating the operator. 
     * The operator should be one of =, !=, <, <=, >, >= or in.
     * 
     * @param parameters The mandatory parameters to set.
     */
    public void setParameters(Map<String, String> parameters) {
        this.parameters = mapParameters(parameters);
    }
    
    /**
     * @return the defaultSorts
     */
    public List<String> getDefaultSorts() {
        return defaultSorts;
    }

    /**
     * @param defaultSorts the defaultSorts to set
     */
    public void setDefaultSorts(List<String> defaultSorts) {
        this.defaultSorts = defaultSorts;
    }
        
    /**
     * @return the containerSpecific
     */
    public boolean isContainerSpecific() {
        return containerSpecific;
    }

    /**
     * @param containerSpecific the containerSpecific to set
     */
    public void setContainerSpecific(boolean containerSpecific) {
        this.containerSpecific = containerSpecific;
    }

    /**
     * Helper to map parameters from the configured string form into
     * the runtime FilterOperator form.
     * 
     * @param parameters The parameters to set
     * @return The map of parameters to filter operators
     */
    private Map<String, FilterOperator> mapParameters(Map<String, String> parameters) {
        if( parameters == null ) return null;
        
        Map<String, FilterOperator> ret = new HashMap<String, FilterOperator>();
        
        Iterator<String> it = parameters.keySet().iterator();
        while( it.hasNext() ) {
            String name = it.next();
            String op = parameters.get(name);
            
            FilterOperator fo = null;
            if( "=".equals(op) ) fo = FilterOperator.EQUAL;
            else if( "!=".equals(op) ) fo = FilterOperator.NOT_EQUAL;
            else if( "<".equals(op) ) fo = FilterOperator.LESS_THAN;
            else if( "<=".equals(op) ) fo = FilterOperator.LESS_THAN_OR_EQUAL;
            else if( ">".equals(op) ) fo = FilterOperator.GREATER_THAN;
            else if( ">=".equals(op) ) fo = FilterOperator.GREATER_THAN_OR_EQUAL;
            else if( "in".equalsIgnoreCase(op) ) fo = FilterOperator.IN;
            
            if( fo != null ) ret.put(name, fo);
            else throw new IllegalArgumentException("Cannot add query parameter [" + name + "] because operator [" + op + "] not recognised. Must be =,!=,<,<=,>,>=,in");
        }
        
        return ret.size() > 0 ? ret : null;
    }
    
    /**
     * Helper to return a map of parameters where the operators
     * are strings.
     * 
     * @param parameters The parameters to map
     * @return The map of parameters
     */
    private Map<String, String> mapParameterStrings(Map<String, FilterOperator> parameters) {
        if( parameters == null ) return null;
        
        Map<String, String> ret = new HashMap<String, String>();
        
        Iterator<String> it = parameters.keySet().iterator();
        while( it.hasNext() ) {
            String name = it.next();
            ret.put(name, parameters.get(name).toString());
        }
        
        return ret;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GAEFixedQuery [defaultSorts=" + defaultSorts + ", kind=" + kind + ", mandatoryParameters=" + mandatoryParameters + ", parameters=" + parameters
                + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((defaultSorts == null) ? 0 : defaultSorts.hashCode());
        result = prime * result + ((kind == null) ? 0 : kind.hashCode());
        result = prime * result + ((mandatoryParameters == null) ? 0 : mandatoryParameters.hashCode());
        result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if( this == obj ) return true;
        if( obj == null ) return false;
        if( getClass() != obj.getClass() ) return false;
        GAEFixedQuery other = (GAEFixedQuery)obj;
        if( defaultSorts == null ) {
            if( other.defaultSorts != null ) return false;
        }
        else if( !defaultSorts.equals(other.defaultSorts) ) return false;
        if( kind == null ) {
            if( other.kind != null ) return false;
        }
        else if( !kind.equals(other.kind) ) return false;
        if( mandatoryParameters == null ) {
            if( other.mandatoryParameters != null ) return false;
        }
        else if( !mandatoryParameters.equals(other.mandatoryParameters) ) return false;
        if( parameters == null ) {
            if( other.parameters != null ) return false;
        }
        else if( !parameters.equals(other.parameters) ) return false;
        return true;
    }
}
