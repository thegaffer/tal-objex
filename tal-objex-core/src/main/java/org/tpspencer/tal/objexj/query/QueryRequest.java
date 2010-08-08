package org.tpspencer.tal.objexj.query;

import java.util.List;
import java.util.Map;

/**
 * This interface represents the input to a query.
 * 
 * @author Tom Spencer
 */
public interface QueryRequest {
    
    /**
     * @return The name of the query to execute
     */
    public String getName();

    /**
     * @return The offset from the beginning of the results to get from (0 indexed)
     */
    public int getOffset();
    
    /**
     * Note that there are likely to be platform specific 
     * limits on this size.
     * 
     * @return The number of results to get in one go
     */
    public int getPageSize();
    
    /**
     * @return The parameters for the request
     */
    public Map<String, Object> getParameters();
    
    /**
     * @return A list of the fields to sort with (often a query as a default set)
     */
    public List<String> getSortFields();
    
    /**
     * @return Indicates if max results is required in the results
     */
    public boolean isMaxResultsRequired();
    
    /**
     * This handle should only be provided if the request the
     * very next page from a previous result. It is ignored
     * if the requested offset is 0. Some platforms validate
     * this handle is valid against the request, but this should
     * not be relied on.
     * 
     * @return The handle from a previous query
     */
    public Object getHandle();
}
