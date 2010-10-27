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

package org.talframework.objexj.object.writer;

import java.util.List;
import java.util.Map;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;

/**
 * This class allows objects to be read from property maps
 * that have been prepared. This is most useful for the
 * Restful Web Services, but is included in the core for
 * any other use in the future as it is not specific to
 * RS Web Services.
 *
 * @author Tom Spencer
 */
public class PropertyReader implements ObjexStateReader {
    
    /** If true then only properties set on the input are actually read */
    private boolean merge = false;
    /** If true then owned references properties are considered */
    private boolean includeOwned = false;
    /** If true then non-owned reference properties are considered */
    private boolean includeReferences = false;
    
    /** The bean we are reading from */
    private Map<String, Object> properties;
    
    public void readObject(Map<String, Object> properties, ObjexObj target) {
        this.properties = properties;
        target.acceptReader(this);
    }

    /**
     * Reads the property from the state bean if it is compatible
     */
    public <T> T read(String name, T current, Class<T> expected, ObjexFieldType type, boolean persistent) {
        T ret = current;
        
        if( !merge || properties.containsKey(name) ) {
            ret = expected.cast(properties.get(name));
        }
        
        return ret;
    }
    
    /**
     * Reads the property from the state bean if it is compatible
     */
    public String readReference(String name, String current, ObjexFieldType type, boolean persistent) {
        if( type == ObjexFieldType.REFERENCE && !includeReferences ) return current;
        if( type == ObjexFieldType.OWNED_REFERENCE && !includeOwned ) return current;
                
        String ret = current;
        
        if( !merge || properties.containsKey(name) ) {
            ret = String.class.cast(properties.get(name));
        }
        
        return ret;
    }
    
    /**
     * Reads the property from the state bean if it is compatible
     */
    @SuppressWarnings("unchecked")
    public List<String> readReferenceList(String name, List<String> current, ObjexFieldType type, boolean persistent) {
        if( type == ObjexFieldType.REFERENCE && !includeReferences ) return current;
        if( type == ObjexFieldType.OWNED_REFERENCE && !includeOwned ) return current;
                
        List<String> ret = current;
        
        if( !merge || properties.containsKey(name) ) {
            ret = List.class.cast(properties.get(name));
        }
        
        return ret;
    }
    
    /**
     * Reads the property from the state bean if it is compatible
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> readReferenceMap(String name, Map<String, String> current, ObjexFieldType type, boolean persistent) {
        if( type == ObjexFieldType.REFERENCE && !includeReferences ) return current;
        if( type == ObjexFieldType.OWNED_REFERENCE && !includeOwned ) return current;
                
        Map<String, String> ret = current;
        
        if( !merge || properties.containsKey(name) ) {
            ret = Map.class.cast(properties.get(name));
        }
        
        return ret;
    }
    
    /**
     * @return the merge
     */
    public boolean isMerge() {
        return merge;
    }

    /**
     * Setter for the merge field
     *
     * @param merge the merge to set
     */
    public void setMerge(boolean merge) {
        this.merge = merge;
    }

    /**
     * @return the includeOwned
     */
    public boolean isIncludeOwned() {
        return includeOwned;
    }

    /**
     * Setter for the includeOwned field
     *
     * @param includeOwned the includeOwned to set
     */
    public void setIncludeOwned(boolean includeOwned) {
        this.includeOwned = includeOwned;
    }

    /**
     * @return the includeReferences
     */
    public boolean isIncludeReferences() {
        return includeReferences;
    }

    /**
     * Setter for the includeReferences field
     *
     * @param includeReferences the includeReferences to set
     */
    public void setIncludeReferences(boolean includeReferences) {
        this.includeReferences = includeReferences;
    }
}
