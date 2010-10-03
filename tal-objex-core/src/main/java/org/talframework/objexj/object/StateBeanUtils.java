/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.objexj.object;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;

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
     */
    public static String updateTempReferences(String ref, Map<ObjexID, ObjexID> tempRefs) {
        if( ref == null ) return ref;
        if( tempRefs == null || tempRefs.size() == 0 ) return ref;
        else if( tempRefs.containsKey(ref) ) return tempRefs.get(ref).toString();
        else return ref;
    }
    
    /**
     * Helper to update any temp IDs in a list of objex object
     * references. 
     * 
     * @param refs The references
     * @param tempRefs The temp IDs and their real counterparts
     * @return The amended list
     */
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
    
    /**
     * Helper to replace all temporary references inside a map
     * with the new real references.
     * 
     * @param refs The reference map
     * @param tempRefs The temp references and their real ID counterpart
     * @return The amended map of references
     */
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
    }
}
