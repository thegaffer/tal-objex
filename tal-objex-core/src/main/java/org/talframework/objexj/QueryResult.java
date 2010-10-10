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
