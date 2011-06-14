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
package org.talframework.objexj.exceptions;

/**
 * Indicates that an attempt to obtain a named query failed
 * because it was not found by the runtime environment. This
 * generally indicates some configuration error.
 * 
 * @author Tom Spencer
 */
public final class QueryNotFoundException extends BaseObjexException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the name of the query that was not found */
    private final String name;
    
    public QueryNotFoundException(String name) {
        super();
        this.name = name;
    }
    
    public QueryNotFoundException(String name, Exception cause) {
        super(cause);
        this.name = name;
    }
    
    @Override
    public <T> T accept(ObjexExceptionVisitor visitor, Class<T> expected) {
        return visitor.visit(this, expected);
    }
    
    @Override
    public String getMessage() {
        return "Query [" + name + "] not found";
    }
}
