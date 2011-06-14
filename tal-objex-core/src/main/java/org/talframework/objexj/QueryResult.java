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
