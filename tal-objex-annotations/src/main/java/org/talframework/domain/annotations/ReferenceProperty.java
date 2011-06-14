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
 * This annotation marks a property as representing a reference to another
 * object. It is applied to the public Java Getter (Accessor) method of a
 * property.
 * 
 * <p>The type of the property this refers to should be directly to an
 * interface, or it should be to a List, Set or Map (interface versions)
 * where the element/value generic type should be to an interface. This
 * interface is marked on the annotation for additional type checking.
 * For instance ...</p>
 * <p><code><pre>
 * &#64;ReferenceProperty(SomeInterface.class)
 * public SomeInterface getRef();                   // Valid
 * 
 * &#64;ReferenceProperty(SomeInterface.class)
 * public List<SomeInterface> getRefs();            // Valid, can use Set also
 * 
 * &#64;ReferenceProperty(SomeInterface.class)
 * public Map<String, SomeInterface> getRefMap();   // Valid
 * 
 * &#64;ReferenceProperty(SomeInterface.class)
 * public ArrayList<SomeInterface) getRefs();       // Invalid, cannot use ArrayList directly, only List!
 * 
 * &#64;ReferenceProperty(SomeObject.class)
 * &#64;SomeObject getRefs();                       // Invalid, cannot use object/class directly
 * </pre></code></p>
 * 
 * <p>Objex uses this annotation to understand and manage the non-owned
 * relationships between the objects.</p> 
 *
 * @author Tom Spencer
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReferenceProperty {
    /** Holds an interface detailing the type of element to hold in reference */
    Class<?> value();
}
