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

package org.talframework.objexj.generator.roo.compiler;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.talframework.objexj.generator.roo.annotations.ObjexPropAnnotation;
import org.talframework.objexj.generator.roo.annotations.ObjexRefPropAnnotation;
import org.talframework.objexj.generator.roo.fields.ListReferenceField;
import org.talframework.objexj.generator.roo.fields.MapReferenceField;
import org.talframework.objexj.generator.roo.fields.ObjexField;
import org.talframework.objexj.generator.roo.fields.SimpleField;
import org.talframework.objexj.generator.roo.fields.SimpleReferenceField;

/**
 * This class iterates around the Objex objects state bean
 * and extracts all of its fields and creates {@link ObjexField}
 * instances out of them. For reference fields etc it will
 * generate out potentially two properties, one for the
 * ObjexObj instances and another for the references.
 * 
 * @author Tom Spencer
 */
public class PropertyCompiler {

    /**
     * Goes through the types declared fields (if bean) or getter
     * methods (if interface) and works out all the properties.
     * 
     * TODO: Determine if interface and work on getters instead of fields
     * 
     * @param typeDetails The type to generate from
     * @return The list of fields
     */
    //@Trace
    public List<ObjexField> compile(ClassOrInterfaceTypeDetails typeDetails) {
        List<ObjexField> ret = new ArrayList<ObjexField>();
        
        List<? extends FieldMetadata> fields = typeDetails.getDeclaredFields();
        if( fields != null ) {
            Iterator<? extends FieldMetadata> it = fields.iterator();
            while( it.hasNext() ) {
                FieldMetadata fm = it.next();
                if( Modifier.isStatic(fm.getModifier()) ) continue;
                
                ObjexRefPropAnnotation ref = ObjexRefPropAnnotation.get(fm.getAnnotations());
                if( ref != null ) {
                    if( fm.getFieldType().getFullyQualifiedTypeName().equals(String.class.getName()) ) addRefField(ret, fm, ref);
                    else if( fm.getFieldType().getFullyQualifiedTypeName().equals(List.class.getName()) ) addListField(ret, fm, ref);
                    else if( fm.getFieldType().getFullyQualifiedTypeName().equals(Map.class.getName()) ) addMapField(ret, fm, ref);
                    else invalidRefProp(fm, ref);
                }
                else {
                    ObjexPropAnnotation prop = ObjexPropAnnotation.get(fm.getAnnotations());
                    addField(ret, fm, prop);
                }
            }
        }
        
        return ret;
    }
    
    //@TraceWarn
    private void invalidRefProp(FieldMetadata fm, ObjexRefPropAnnotation ref) {
        // No-op for tracing
    }
    
    //@Trace
    private void addField(List<ObjexField> fields, FieldMetadata fm, ObjexPropAnnotation prop) {
        fields.add(new SimpleField(fm, prop));
    }
    
    //@Trace
    private void addRefField(List<ObjexField> fields, FieldMetadata fm, ObjexRefPropAnnotation ref) {
        SimpleReferenceField field = new SimpleReferenceField(fm, ref);
        fields.add(field);
        fields.add(new SimpleField(fm, field.getReferencePropName(), ref));
    }
    
    //@Trace
    private void addListField(List<ObjexField> fields, FieldMetadata fm, ObjexRefPropAnnotation ref) {
        ListReferenceField field = new ListReferenceField(fm, ref);
        fields.add(field);
        fields.add(new SimpleField(fm, field.getReferencePropName(), ref));
    }

    //@Trace
    private void addMapField(List<ObjexField> fields, FieldMetadata fm, ObjexRefPropAnnotation ref) {
        MapReferenceField field = new MapReferenceField(fm, ref);
        fields.add(field);
        fields.add(new SimpleField(fm, field.getReferencePropName(), ref));
    }
}
