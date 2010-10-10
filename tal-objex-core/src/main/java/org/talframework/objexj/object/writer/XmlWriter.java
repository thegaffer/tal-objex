/*
 * Copyright 2010 Thomas Spencer
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

import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;

/**
 * This class implements the {@link ObjexStateWriter} interface
 * to write objects into XML.
 *
 * @author Tom Spencer
 */
public class XmlWriter implements ObjexStateWriter {

    public void write(String name, Object obj, ObjexFieldType type, boolean persistent) {
        // TODO Auto-generated method stub
        
    }
    
    public void writeReference(String name, String ref, ObjexFieldType type, boolean persistent) {
        // TODO Auto-generated method stub
        
    }
    
    public void writeReferenceList(String name, List<String> ref, ObjexFieldType type, boolean persistent) {
        // TODO Auto-generated method stub
        
    }
    
    public void writeReferenceMap(String name, Map<String, String> ref, ObjexFieldType type, boolean persistent) {
        // TODO Auto-generated method stub
        
    }
}
