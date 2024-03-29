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
