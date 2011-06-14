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
package org.talframework.domain.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a property as representing a owned relationship
 * to another object. This is very must like a reference to another object,
 * but is one where there is a parent/child relationship. For instance an
 * typically an Aeroplane object would 'own' its Wings, the wings are part
 * of the aeroplane, but it references Pilots, because a Pilot may go and
 * fly other aeroplanes the relationship is not as string.
 * 
 * <p>The rules for using the annotation are exactly the same as a
 * reference property annotation. See {@link ReferenceProperty} for more
 * information.</p>
 * 
 * <p>Objex uses this annotation to understand and manage the owned
 * relationships between the objects.</p> 
 *
 * @author Tom Spencer
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChildProperty {
    /** Holds an interface detailing the type of element to hold in child */
    Class<?> value();
}
