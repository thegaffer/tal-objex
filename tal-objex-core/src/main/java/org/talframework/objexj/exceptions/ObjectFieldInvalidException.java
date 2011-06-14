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
 * This exception represents an attempt to access an invalid
 * field - either because the field does not exist, is not
 * readable or is not writeable. It generally reflects a
 * programming or other error.
 *
 * @author Tom Spencer
 */
public final class ObjectFieldInvalidException extends BaseObjexException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the field name */
    private final String field;
    /** The reason it is invalid */
    private final String reason;
    
    public ObjectFieldInvalidException(String field, String reason) {
        super();
        this.field = field;
        this.reason = reason;
    }
    
    public ObjectFieldInvalidException(String field, String reason, Exception cause) {
        super(cause);
        this.field = field;
        this.reason = reason;
    }
    
    @Override
    public <T> T accept(ObjexExceptionVisitor visitor, Class<T> expected) {
        return visitor.visit(this, expected);
    }
    
    @Override
    public String getMessage() {
        return "ObjectFieldInvalid for field [" + field + "]: " + reason;
    }
}
