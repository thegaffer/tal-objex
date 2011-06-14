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
 * Indicates that the object being changed/validated is
 * invalid. This exception is only thrown if a validation
 * request has not been set on the current thread. If you
 * receive these exceptions it is because you are not
 * setting the current validation request object for the
 * objects to find within their set methods.
 * 
 * @author Tom Spencer
 */
public final class ObjectFieldErrorException extends BaseObjexException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the field name */
    private final String field;
    /** Holds the error code */
    private final String error;
    /** The parameters of the error */
    private final Object[] params;
    
    public ObjectFieldErrorException(String field, String error, Object... params) {
        super();
        this.field = field;
        this.error = error;
        this.params = params;
    }
    
    @Override
    public <T> T accept(ObjexExceptionVisitor visitor, Class<T> expected) {
        return visitor.visit(this, expected);
    }
    
    @Override
    public String getMessage() {
        return "ObjectFieldError [" + error + "] on field [" + field + "]: " + params;
    }
}
