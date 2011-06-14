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
package org.talframework.objexj.runtime.globals.descriptor;

import java.util.Set;

/**
 * This internal class describes a document type. This is calculated when the globals
 * runtime first see's the document (or container type). It is saved in the globals
 * datastore and retrieved for the future (although it is checked for differences).
 * This is really there so we can change the structure of our objects easily, but
 * still benefit from the increased performance of the value list.
 *
 * @author Tom Spencer
 */
public class DocumentDescriptor {

    /** Member holds the type of container */
    private String type;
    /** Member holds a definition of each object */
    private Set<ObjectDescriptor> objects;
}
