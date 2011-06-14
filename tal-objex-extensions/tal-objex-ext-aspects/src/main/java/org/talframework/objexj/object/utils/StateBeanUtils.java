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
package org.talframework.objexj.object.utils;


/**
 * Contains various helper and utility methods for dealing
 * with {@link ObjexObjStateBean}. Most notably there is
 * a bunch of methods for determining if a value has
 * changed or not.
 *
 * @author Tom Spencer
 */
public class StateBeanUtils {
    
    /**
     * Helper to return the true reference replacing any temp
     * reference with the new real ID.
     * 
     * @param ref The reference
     * @param tempRefs The temp references
     * @return The new reference (or old one if not a temp reference)
     *//*
    public static String updateTempReferences(String ref, Map<ObjexID, ObjexID> tempRefs) {
        if( ref == null ) return ref;
        if( tempRefs == null || tempRefs.size() == 0 ) return ref;
        else if( tempRefs.containsKey(ref) ) return tempRefs.get(ref).toString();
        else return ref;
    }
    
    *//**
     * Helper to update any temp IDs in a list of objex object
     * references. 
     * 
     * @param refs The references
     * @param tempRefs The temp IDs and their real counterparts
     * @return The amended list
     *//*
    public static List<String> updateTempReferences(List<String> refs, Map<ObjexID, ObjexID> tempRefs) {
        if( refs == null ) return refs;
        if( tempRefs == null || tempRefs.size() == 0 ) return refs;
        
        int ln = refs.size();
        for( int i = 0 ; i < ln ; i++ ) {
            String ref = refs.get(i);
            if( tempRefs.containsKey(ref) ) refs.set(i, tempRefs.get(ref).toString());
        }
        
        return refs;
    }
    
    *//**
     * Helper to replace all temporary references inside a map
     * with the new real references.
     * 
     * @param refs The reference map
     * @param tempRefs The temp references and their real ID counterpart
     * @return The amended map of references
     *//*
    public static Map<?, String> updateTempReferences(Map<String, String> refs, Map<ObjexID, ObjexID> tempRefs) {
        if( refs == null ) return refs;
        if( tempRefs == null || tempRefs.size() == 0 ) return refs;
        
        Iterator<String> it = refs.keySet().iterator();
        while( it.hasNext() ) {
            String key = it.next();
            String ref = refs.get(key);
            if( tempRefs.containsKey(ref) ) refs.put(key, tempRefs.get(ref).toString());
        }
        
        return refs;
    }*/
}
