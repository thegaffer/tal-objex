package org.talframework.objexj.query;

import java.util.List;

import org.talframework.objexj.ObjexObj;

public final class DefaultQueryResult implements QueryResult {

    /** The offset of the first result returned in larger list */
    private int offset;
    /** The maximum results in query (if requested) */
    private int maxResults;
    /** The results */
    private List<ObjexObj> results;
    /** Determines if the result is known to be the last result */
    private boolean eor;
    /** The handle for use in next request for next page */
    private Object handle;
    
    /**
     * Default constructor
     */
    public DefaultQueryResult() {
    }
    
    /**
     * Helper constructor to construct in one line.
     * 
     * @param offset The offset of first result
     * @param maxResults The max results (if requested)
     * @param results The results found
     * @param eor Indicates of no more results
     * @param handle The handle
     */
    public DefaultQueryResult(
            int offset,
            int maxResults,
            List<ObjexObj> results,
            boolean eor,
            Object handle) {
        this.offset = offset;
        this.maxResults = maxResults;
        this.results = results;
        this.eor = eor;
        this.handle = handle;
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
     * @return the maxResults
     */
    public int getMaxResults() {
        return maxResults;
    }
    /**
     * @param maxResults the maxResults to set
     */
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }
    /**
     * @return the results
     */
    public List<ObjexObj> getResults() {
        return results;
    }
    /**
     * @param results the results to set
     */
    public void setResults(List<ObjexObj> results) {
        this.results = results;
    }
    /**
     * @return the eor
     */
    public boolean isEor() {
        return eor;
    }
    /**
     * @param eor the eor to set
     */
    public void setEor(boolean eor) {
        this.eor = eor;
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
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "QueryResult [eor=" + eor + ", handle=" + handle + ", maxResults=" + maxResults + ", offset=" + offset + ", results=" + results + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (eor ? 1231 : 1237);
        result = prime * result + ((handle == null) ? 0 : handle.hashCode());
        result = prime * result + maxResults;
        result = prime * result + offset;
        result = prime * result + ((results == null) ? 0 : results.hashCode());
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
        DefaultQueryResult other = (DefaultQueryResult)obj;
        if( eor != other.eor ) return false;
        if( handle == null ) {
            if( other.handle != null ) return false;
        }
        else if( !handle.equals(other.handle) ) return false;
        if( maxResults != other.maxResults ) return false;
        if( offset != other.offset ) return false;
        if( results == null ) {
            if( other.results != null ) return false;
        }
        else if( !results.equals(other.results) ) return false;
        return true;
    }
}
