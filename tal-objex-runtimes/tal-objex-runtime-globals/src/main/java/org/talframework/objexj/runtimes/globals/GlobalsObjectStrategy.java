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
        for( String prop : strategy.getProperties() ) {
            if( !fields.containsKey(prop) ) {
                this.fields.add(prop);
                fields.put(prop, this.fields.size());
            }
        }
        
        if( this.fields.size() > size ) save();
    }
    
    private void save() {
        // TODO: Save this away
    }
}
