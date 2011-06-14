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
package org.talframework.objexj;

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
