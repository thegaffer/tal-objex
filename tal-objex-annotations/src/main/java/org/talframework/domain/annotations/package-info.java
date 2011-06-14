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
/**
 * This package contains the annotations which are intended to mark
 * intent in your domain objects and their properties.
 * 
 * <p>It should be noted that the intent here is not to specify
 * either validation or persistence concerns. Validation should be
 * specified inside your objects with your own code, or other
 * annotations/aspects specifically for validation. Persistence
 * is the concern of some persistence engine. If your using a
 * persistence engine that needs information include that in
 * separate annotations (if you have to!).
 * 
 * <p>These annotations are used by Objex to deal with the objects,
 * but every attempt is made in the annotations to make them free
 * of Objex (save for the package!), so feel free to use them in
 * other situations.</p>
 */
package org.talframework.domain.annotations;