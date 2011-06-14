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
package org.talframework.objexj.runtimes.globals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talframework.objexj.container.ObjectStrategy;

/**
 * This class describes the storage of a class inside the globals DB.
 * It is produced automatically when the class is first seen and then
 * persisted and used in the future. This ensures that field positions
 * remain the same at all times.
 *
 * @author Tom Spencer
 */
public class GlobalsObjectStrategy {

    /** The field names in their order - note not all fields still exist */
    private List<String> fields;
    
    /**
     * Call to create the globals object strategy. It is saved away against the
     * container type if required.
     * 
     * @param containerType
     * @param strategy
     */
    public GlobalsObjectStrategy(String containerType, ObjectStrategy strategy) {
        String type = strategy.getTypeName();
        
        // TODO: Load existing
        fields = new ArrayList<String>();
        
        // Now merge in
        Map<String, Integer> fields = new HashMap<String, Integer>();
        for( int i = 0 ; i < this.fields.size() ; i++ ) fields.put(this.fields.get(i), i);
    
        int size = fields.size();
        /*for( String prop : strategy.getProperties() ) {
            // TODO: If ref list, set, map or large blob or memo should be in own node??
            if( !fields.containsKey(prop) ) {
                this.fields.add(prop);
                fields.put(prop, this.fields.size());
            }
        }*/
        
        if( this.fields.size() > size ) save();
    }
    
    private void save() {
        // TODO: Save this away
    }
    
    /**
     * @return The fields in order - not all these fields are neccessarily still value
     */
    public List<String> getFields() {
        return fields;
    }
}
