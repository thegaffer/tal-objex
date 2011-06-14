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
package org.talframework.objexj.annotations.source;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This annotation marks the property as a reference property.
 * The property must either be a reference to an interface or
 * a list, set or map holding elements of an interface. The
 * list, set or map must be the interface versions only, i.e.
 * {@link List}, {@link Set} or {@link Map} - these will be
 * set by Objex in runtime instances.
 * 
 * @author Tom Spencer
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface ObjexRefProp {
    /** Indicates if the property is considered owned */
    boolean owned() default true;
    /** Holds the optional name of the external property */
    String externalName() default "";
    /** Determines if the field is gettable externally */
    boolean gettable() default true;
    /** Determines if the field is settable externally */
    boolean settable() default true;
}
