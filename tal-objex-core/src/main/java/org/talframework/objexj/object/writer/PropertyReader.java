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

import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;

/**
 * This class implements the state reader interface by reading
 * from a map of name/value pairs. It is intended this is paired
 * to the {@link PropertyWriter}, but this reader can handle
 * any map.
 *
 * @author Tom Spencer
 */
public class PropertyReader implements ObjexStateReader {

    public <T> T read(String name, Class<T> expected, ObjexFieldType type, boolean persistent) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public String readReference(String name, ObjexFieldType type, boolean persistent) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public List<String> readReferenceList(String name, ObjexFieldType type, boolean persistent) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public Map<String, String> readReferenceMap(String name, ObjexFieldType type, boolean persistent) {
        // TODO Auto-generated method stub
        return null;
    }
}
