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

import java.util.Map;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObj.ObjexFieldType;

/**
 * This class reads or loads the state of an {@link ObjexObj} by reading 
 * from a source map. This class only reads 1 object at a time and expects 
 * the reference properties to be held as ObjexID or String (or lists, 
 * sets and maps of strings) in the bean class.
 * 
 * <p>If merging in order to determine if we get the value from the
 * source bean, this class tests if the map holds a property of that
 * name. If this does exist then we do use the value from the source, 
 * otherwise we ignore it.</p>
 * 
 * @author Tom Spencer
 */
public class MapObjectReader extends BaseObjectReader {
    
    /** The map we are reading from */
    private Map<String, Object> properties;
    
    public MapObjectReader() {
        super();
    }
    
    public MapObjectReader(ObjectReaderBehaviour... behaviours) {
        super(behaviours);
    }
    
    /**
     * Call to set the state of the target object given the simple bean holding
     * the values.
     * 
     * @param bean
     * @param target
     */
    public void readObject(Map<String, Object> properties, ObjexObj target) {
        this.properties = properties;
        super.read(target);
    }

    /**
     * Helper to determine if property should be read at all
     * 
     * @param name The name of the property
     * @return True if property should be read (set on the target) false otherwise
     */
    protected boolean shouldSet(String name) {
        return !isMerge() || properties.containsKey(name);
    }
    
    @Override
    protected boolean propertyExists(String name, Class<?> expected) {
        return properties.containsKey(name);
    }
    
    @Override
    protected Object getProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent) {
        return properties.get(name);
    }
    
    @Override
    protected Object getSetProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent) {
        return properties.get(name);
    }
    
    @Override
    protected Object getListProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent) {
        return properties.get(name);
    }
    
    @Override
    protected Object getMapProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent) {
        return properties.get(name);
    }
}
