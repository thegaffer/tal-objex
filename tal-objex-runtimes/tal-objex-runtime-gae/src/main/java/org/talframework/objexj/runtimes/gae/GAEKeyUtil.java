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
package org.talframework.objexj.runtimes.gae;

import org.talframework.objexj.ObjexObj;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Contains various helper methods for dealing with Google
 * App Engine Keys
 *
 * @author Tom Spencer
 */
public class GAEKeyUtil {

    /**
     * Calculates a key for a specific entity
     * 
     * @param root The root key (key of the container)
     * @param obj The object to get key for
     * @return The key for this object
     */
    public static Key getKey(Key root, ObjexObj obj) {
        Object val = obj.getId().getId();
        if( val instanceof Long ) return KeyFactory.createKey(root, obj.getType(), ((Long)val).longValue());
        else return KeyFactory.createKey(root, obj.getType(), val.toString());
    }
}
