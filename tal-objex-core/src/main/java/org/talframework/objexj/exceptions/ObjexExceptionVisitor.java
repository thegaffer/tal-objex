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
 * This visitor supports a double dispatch mechanism on
 * the objex exceptions. This is typically for runtime
 * engines and interfaces to use and not for specific
 * client use.
 *
 * @author Tom Spencer
 */
public interface ObjexExceptionVisitor {

    public <T> T visit(ContainerExistsException e, Class<T> expected);
    
    public <T> T visit(ContainerInvalidException e, Class<T> expected);
    
    public <T> T visit(ContainerNotFoundException e, Class<T> expected);
    
    public <T> T visit(ContainerTypeNotFoundException e, Class<T> expected);
    
    public <T> T visit(EventHandlerNotFoundException e, Class<T> expected);
    
    public <T> T visit(ObjectErrorException e, Class<T> expected);
    
    public <T> T visit(ObjectFieldErrorException e, Class<T> expected);
    
    public <T> T visit(ObjectFieldInvalidException e, Class<T> expected);
    
    public <T> T visit(ObjectIDInvalidException e, Class<T> expected);
    
    public <T> T visit(ObjectInvalidException e, Class<T> expected);
    
    public <T> T visit(ObjectNotFoundException e, Class<T> expected);
    
    public <T> T visit(ObjectRemovedException e, Class<T> expected);
    
    public <T> T visit(ObjectTypeNotFoundException e, Class<T> expected);
    
    public <T> T visit(QueryNotFoundException e, Class<T> expected);
    
    public <T> T visit(TransactionNotFoundException e, Class<T> expected);
}
