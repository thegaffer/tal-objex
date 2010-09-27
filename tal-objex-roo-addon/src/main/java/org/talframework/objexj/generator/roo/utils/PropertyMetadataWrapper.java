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

package org.talframework.objexj.generator.roo.utils;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultFieldMetadata;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.DefaultAnnotationMetadata;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;

public class PropertyMetadataWrapper {

    /** The name of the property */
    private final JavaSymbolName name;
    /** The type of the property */
    private final JavaType type;
    /** The modifier */
    private int modifier = Modifier.PRIVATE;
    /** The initialiser for the field */
    private String initialiser = null;
    /** The annotations for the field */
    private List<AnnotationMetadata> annotations = null;
    
    public PropertyMetadataWrapper(FieldMetadata fm) {
        this.name = fm.getFieldName();
        this.type = fm.getFieldType();
    }
    
    public PropertyMetadataWrapper(JavaSymbolName name, JavaType type) {
        this.name = name;
        this.type = type;
    }

    /**
     * @return the name
     */
    public JavaSymbolName getName() {
        return name;
    }

    /**
     * @return the type
     */
    public JavaType getType() {
        return type;
    }
    
    public int getModifier() {
        return modifier;
    }
    
    public void addModifier(int modifier) {
        this.modifier = this.modifier | modifier;
    }
    
    public void setModifier(int modifier) {
        this.modifier = modifier;
    }
    
    /**
     * @return the initialiser
     */
    public String getInitialiser() {
        return initialiser;
    }

    /**
     * @param initialiser the initialiser to set
     */
    public void setInitialiser(String initialiser) {
        this.initialiser = initialiser;
    }
    
    /**
     * @return the annotations
     */
    public List<AnnotationMetadata> getAnnotations() {
        if( this.annotations == null ) this.annotations = new ArrayList<AnnotationMetadata>();
        return annotations;
    }

    /**
     * @param annotations the annotations to set
     */
    public void setAnnotations(List<AnnotationMetadata> annotations) {
        this.annotations = annotations;
    }
    
    /**
     * Helper to add an annotation over this type.
     * 
     * @param am The annotation
     */
    public void addAnnotation(AnnotationMetadata am) {
        if( this.annotations == null ) this.annotations = new ArrayList<AnnotationMetadata>();
        this.annotations.add(am);
    }
    
    public void addAnnotation(String annotation) {
        if( this.annotations == null ) this.annotations = new ArrayList<AnnotationMetadata>();
        
        List<AnnotationAttributeValue<?>> attributes = new ArrayList<AnnotationAttributeValue<?>>();
        AnnotationMetadata am = new DefaultAnnotationMetadata(new JavaType(annotation), attributes);
        this.annotations.add(am);
    }
    
    public void addAnnotation(String annotation, AnnotationAttributeValue<?>... attrs) {
        if( this.annotations == null ) this.annotations = new ArrayList<AnnotationMetadata>();
        
        List<AnnotationAttributeValue<?>> attributes = new ArrayList<AnnotationAttributeValue<?>>();
        if( attrs != null ) {
            for( int i = 0 ; i < attrs.length ; i++ ) {
                attributes.add(attrs[i]);
            }
        }
        AnnotationMetadata am = new DefaultAnnotationMetadata(new JavaType(annotation), attributes);
        this.annotations.add(am);
    }

    /**
     * Adds this property to the state bean (if it does not exist)
     * 
     * @param builder The builder to add it
     * @param typeDetails The details of the type
     * @param typeId The ID of the type
     */
    public void addMetadata(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        List<? extends FieldMetadata> fields = typeDetails.getDeclaredFields();
        
        FieldMetadata ret = null;
        if( fields != null ) {
            Iterator<? extends FieldMetadata> it = fields.iterator();
            while( it.hasNext() && ret == null ) {
                FieldMetadata fm = it.next();
                if( fm.getFieldName().equals(name) ) ret = fm;
            }
        }
        
        if( ret == null ) {
            ret = new DefaultFieldMetadata(typeId, getModifier(), getName(), getType(), getInitialiser(), getAnnotations());
        }
        
        builder.addField(ret);
    }
}
