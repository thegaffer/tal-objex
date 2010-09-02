package org.talframework.objexj.query;

import java.util.List;

import org.talframework.objexj.ObjexObj;

/**
 * This interface represents the output to a query
 * 
 * @author Tom Spencer
 */
public interface QueryResult {

    /**
     * @return The offset to the query is based on
     */
    public int getOffset();
    
    /**
     * @return The maximum results found
     */
    public int getMaxResults();
    
    /**
     * @return The results that were found (or an empty list)
     */
    public List<ObjexObj> getResults();
    
    /**
     * @return Indicates if thought to be no results after those in this result
     */
    public boolean isEor();
    
    /**
     * This handle is used in some implementations to make getting
     * the next set of results quicker. It is only valid if the
     * next request is for the set of results immediately after
     * those in this result. i.e. the next request is for offset
     * + results. If we are at the end of the results this is
     * not provided.
     * 
     * @return An optional handle to use for the next page
     */
    public Object getHandle();
}
