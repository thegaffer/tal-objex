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
package org.talframework.objexj.validation.object;

import javax.validation.ConstraintValidatorContext;

import org.talframework.objexj.validation.groups.IntraObjectGroup;

/**
 * Implementing this interface on an Objex object means it
 * will be called as part of the intra-object validation
 * step. Typically you do not use or implement this interface
 * directly, but use the Objex generators to generate out
 * the relevant code connecting to your @ObjexCheck methods.
 *
 * @author Tom Spencer
 */
@IntraObjectValid(groups={IntraObjectGroup.class})
public interface SelfIntraObjectValidator {

    /**
     * Called on the object exposing this interface to actually
     * validate itself with respect to its internal state.
     * 
     * @param context The validation context so errors can be raised
     * @return True if the object is valid, false otherwise
     */
    public boolean validateObject(ConstraintValidatorContext context);
}
