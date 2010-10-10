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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talframework.objexj.QueryRequest;


/**
 * Default implementation of the query request object
 * 
 * @author Tom Spencer
 */
public final class DefaultQueryRequest implements QueryRequest {

    /** The name of the query to perform */
    private String name;
    /** The offset to the request */
    private int offset;
    /** The page size for the request */
    private int pageSize;
    /** The parameters for the request */
    private Map<String, Object> parameters;
    /** The sort fields */
    private List<String> sortFields;
    /** Indicates if maximum results are required */
    private boolean maxResultsRequired;
    /** The handle to the request */
    private Object handle;
    
    /**
     * Default constructor
     */
    public DefaultQueryRequest() {
    }
    
    /**
     * Helper constructor to construct the request in one line.
     * 
     * @param name The name of the query to perform
     * @param offset The offset for first result
     * @param pageSize The page size
     * @param parameters The dynamic parameters
     * @param maxResultsRequired The max results required
     * @param handle The handle to the result
     */
    public DefaultQueryRequest(
            String name, 
            int offset,
            int pageSize,
            Map<String, Object> parameters,
            List<String> sortFields,
            boolean maxResultsRequired,
            Object handle) {
        this.name = name;
        this.offset = offset;
        this.pageSize = pageSize;
        this.parameters = parameters;
        this.sortFields = sortFields;
        this.maxResultsRequired = maxResultsRequired;
        this.handle = handle;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }
    /**
     * @param offset the offset to set
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }
    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }
    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    /**
     * @return the parameters
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }
    /**
     * @param parameters the parameters to set
     */
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
    /**
     * Helper to add a parameter
     * 
     * @param name The name of the parameter
     * @param value Its value
     */
    public void addParameter(String name, Object value) {
        if( this.parameters == null ) this.parameters = new HashMap<String, Object>();
        this.parameters.put(name, value);
    }
    /**
     * @return the maxResultsRequired
     */
    public boolean isMaxResultsRequired() {
        return maxResultsRequired;
    }
    /**
     * @param maxResultsRequired the maxResultsRequired to set
     */
    public void setMaxResultsRequired(boolean maxResultsRequired) {
        this.maxResultsRequired = maxResultsRequired;
    }
    /**
     * @return the handle
     */
    public Object getHandle() {
        return handle;
    }
    /**
     * @param handle the handle to set
     */
    public void setHandle(Object handle) {
        this.handle = handle;
    }

    /**
     * @return the sortFields
     */
    public List<String> getSortFields() {
        return sortFields;
    }

    /**
     * @param sortFields the sortFields to set
     */
    public void setSortFields(List<String> sortFields) {
        this.sortFields = sortFields;
    }
    
    public void addSortField(String field) {
        this.sortFields = new ArrayList<String>();
        this.sortFields.add(field);
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("QueryRequest: ");
        builder.append("name=").append(name);
        builder.append(", offset=").append(offset);
        builder.append(", pageSize=").append(pageSize);
        builder.append(", params=").append(parameters);
        builder.append(", sortFields=").append(sortFields);
        builder.append(", maxResult=").append(maxResultsRequired);
        builder.append(", handle=").append(handle);
        return builder.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((handle == null) ? 0 : handle.hashCode());
        result = prime * result + (maxResultsRequired ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + offset;
        result = prime * result + pageSize;
        result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
        result = prime * result + ((sortFields == null) ? 0 : sortFields.hashCode());
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
        DefaultQueryRequest other = (DefaultQueryRequest)obj;
        if( handle == null ) {
            if( other.handle != null ) return false;
        }
        else if( !handle.equals(other.handle) ) return false;
        if( maxResultsRequired != other.maxResultsRequired ) return false;
        if( name == null ) {
            if( other.name != null ) return false;
        }
        else if( !name.equals(other.name) ) return false;
        if( offset != other.offset ) return false;
        if( pageSize != other.pageSize ) return false;
        if( parameters == null ) {
            if( other.parameters != null ) return false;
        }
        else if( !parameters.equals(other.parameters) ) return false;
        if( sortFields == null ) {
            if( other.sortFields != null ) return false;
        }
        else if( !sortFields.equals(other.sortFields) ) return false;
        return true;
    }
}
