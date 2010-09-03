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

package org.talframework.objexj.generator.roo.fields;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.FieldMetadata;

/**
 * This class scans the fields of the type and creates
 * {@link ObjexObjProperty} instances for each one found.
 * 
 * @author Tom Spencer
 */
public class PropertyCompiler {
    
    public PropertyCompiler() {
        
    }
    
    public List<ObjexObjProperty> compile(ClassOrInterfaceTypeDetails typeDetails) {
        List<ObjexObjProperty> ret = new ArrayList<ObjexObjProperty>();
        
        List<? extends FieldMetadata> fields = typeDetails.getDeclaredFields();
        if( fields != null ) {
            Iterator<? extends FieldMetadata> it = fields.iterator();
            while( it.hasNext() ) {
                FieldMetadata fm = it.next();
                if( Modifier.isStatic(fm.getModifier()) ) continue;
                
                ret.add(new ObjexObjProperty(fm));
            }
        }
        
        return ret;
    }
}
