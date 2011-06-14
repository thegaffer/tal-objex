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

/**
 * This annotation marks a property that should be exposed on
 * the bean and any natural interface linked to this type. Note
 * that all properties of a class marked with {@link ObjexObj}
 * are considered object properties, but this allows finer
 * control. 
 * 
 * @author Tom Spencer
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface ObjexProp {
    /** Determines if the field is gettable externally */
    boolean gettable() default true;
    /** Determines if the field is settable externally */
    boolean settable() default true;
    /** Holds the optional name of the external property, i.e. the getter/setter */
    String externalName() default "";
    /** Holds the type to expose the property as (if Object then the type of the property is used) */
    Class<?> exposeAs() default Object.class;
    /** Optional class with static methods that we can perform the transformations with */
    Class<?> transformerClass() default Object.class;
    /** Name of a function on transformer class or non-static inside this object that performs get transformation */
    String transformerGetFunction() default "";
    /** Name of a function on transformer class or non-static inside this object that performs set transformation */
    String transformerSetFunction() default "";
}
