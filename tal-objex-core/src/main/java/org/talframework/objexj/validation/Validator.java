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
package org.talframework.objexj.validation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ValidationError;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.ValidationRequest.ValidationType;

/**
 * This helper class can be used to perform the validation
 * of one or more objects.
 *
 * @author Tom Spencer
 */
public class Validator {
    
    /**
     * Call to validate a single object with the
     * given level of validation.
     * 
     * @param type The type of validation to perform
     * @param objs The object to validate
     */
    public static List<ValidationError> validate(ValidationType type, ObjexObj obj) {
        if( obj == null ) return null;
        
        ValidationRequest previous = null;
        ValidationRequest current = new SimpleValidationRequest(type);
        
        try {
            previous = CurrentValidationRequest.start(current);
            obj.validate(current);
        }
        finally {
            CurrentValidationRequest.end(previous);
        }
        
        return current.getErrors();
    }
    
    /**
     * Call to validate an array of objects with the
     * given level of validation.
     * 
     * @param type The type of validation to perform
     * @param objs The objects to validate
     */
    public static List<ValidationError> validate(ValidationType type, ObjexObj... objs) {
        if( objs == null || objs.length == 0 ) return null;
        
        ValidationRequest previous = null;
        ValidationRequest current = new SimpleValidationRequest(type);
        
        try {
            previous = CurrentValidationRequest.start(current);
            
            for( ObjexObj objexObj : objs ) {
                objexObj.validate(current);
            }
        }
        finally {
            CurrentValidationRequest.end(previous);
        }
        
        return current.getErrors();
    }
    
    /**
     * Call to validate a collection of objects with the
     * given level of validation.
     * 
     * @param type The type of validation to perform
     * @param objs The objects to validate
     */
    public static List<ValidationError> validate(ValidationType type, Collection<ObjexObj> objs) {
        if( objs == null || objs.size() == 0 ) return null;
        
        ValidationRequest previous = null;
        ValidationRequest current = new SimpleValidationRequest(type);
        
        try {
            previous = CurrentValidationRequest.start(current);
            
            Iterator<ObjexObj> it = objs.iterator();
            while( it.hasNext() ) {
                it.next().validate(current);
            }
        }
        finally {
            CurrentValidationRequest.end(previous);
        }
        
        return current.getErrors();
    }
}
