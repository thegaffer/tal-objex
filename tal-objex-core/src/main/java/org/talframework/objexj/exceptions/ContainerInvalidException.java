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

import org.talframework.objexj.ValidationRequest;

/**
 * Indicates that an attempt to save a container failed 
 * because it has errors that must be resolved.
 * 
 * @author Tom Spencer
 */
public final class ContainerInvalidException extends BaseObjexException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the ID of the container that is invalid */
    private final String id;
    /** Holds the ValidationRequest with all errors on it */
    private final ValidationRequest request;
    
    public ContainerInvalidException(String id, ValidationRequest request) {
        super();
        this.id = id;
        this.request = request;
    }
    
    public ValidationRequest getRequest() {
        return request;
    }
    
    @Override
    public <T> T accept(ObjexExceptionVisitor visitor, Class<T> expected) {
        return visitor.visit(this, expected);
    }
    
    @Override
    public String getMessage() {
        return "Container [" + id + "] invalid: " + request;
    }
}
