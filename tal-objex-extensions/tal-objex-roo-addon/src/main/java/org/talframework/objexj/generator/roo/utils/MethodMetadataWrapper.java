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
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.springframework.roo.classpath.details.DefaultMethodMetadata;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.annotations.AnnotatedJavaType;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.DefaultAnnotationMetadata;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;

public class MethodMetadataWrapper {

    /** The name of the property */
    private final JavaSymbolName name;
    /** The type of the property */
    private final JavaType returnType;
    /** The public modifier for the operation */
    private int modifier = Modifier.PUBLIC;
    /** The annotations for the method */
    private List<AnnotationMetadata> annotations = null;
    /** The parameters for the method */
    private List<MethodParameter> parameters = null;
    /** The exceptions that can be thrown */
    private List<JavaType> throwTypes = null;
    /** The body of the method */
    private InvocableMemberBodyBuilder bodyBuilder = null;
    
    public MethodMetadataWrapper(String name, String returnType) {
        this.name = new JavaSymbolName(name);
        this.returnType = new JavaType(returnType);
    }
    
    public MethodMetadataWrapper(JavaSymbolName name, JavaType returnType) {
        this.name = name;
        this.returnType = returnType;
    }

    /**
     * @return the name
     */
    public JavaSymbolName getName() {
        return name;
    }

    /**
     * @return the returnType
     */
    public JavaType getReturnType() {
        return returnType;
    }
    
    /**
     * @return the modifier
     */
    public int getModifier() {
        return modifier;
    }
    
    public void addModifier(int modifier) {
        this.modifier = this.modifier & modifier;
    }

    /**
     * @param modifier the modifier to set
     */
    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    /**
     * @return the annotations
     */
    public List<AnnotationMetadata> getAnnotations() {
        if( annotations == null ) return new ArrayList<AnnotationMetadata>();
        return annotations;
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
        List<AnnotationAttributeValue<?>> attributes = new ArrayList<AnnotationAttributeValue<?>>();
        AnnotationMetadata am = new DefaultAnnotationMetadata(new JavaType(annotation), attributes);
        addAnnotation(am);
    }
    
    public void addAnnotation(String annotation, AnnotationAttributeValue<?>... attrs) {
        List<AnnotationAttributeValue<?>> attributes = new ArrayList<AnnotationAttributeValue<?>>();
        if( attrs != null ) {
            for( int i = 0 ; i < attrs.length ; i++ ) {
                attributes.add(attrs[i]);
            }
        }
        AnnotationMetadata am = new DefaultAnnotationMetadata(new JavaType(annotation), attributes);
        addAnnotation(am);
    }

    /**
     * @param annotations the annotations to set
     */
    public void setAnnotations(List<AnnotationMetadata> annotations) {
        this.annotations = annotations;
    }

    /**
     * @return the parameters
     */
    public List<JavaSymbolName> getParameterNames() {
        List<JavaSymbolName> ret = new ArrayList<JavaSymbolName>();
        
        if( parameters != null ) {
            Iterator<MethodParameter> it = parameters.iterator();
            while( it.hasNext() ) {
                ret.add(it.next().getName());
            }
        }
        
        return ret;
    }
    
    /**
     * @return the parameters
     */
    public List<AnnotatedJavaType> getParameterTypes() {
        List<AnnotatedJavaType> ret = new ArrayList<AnnotatedJavaType>();
        
        if( parameters != null ) {
            Iterator<MethodParameter> it = parameters.iterator();
            while( it.hasNext() ) {
                ret.add(it.next().getType());
            }
        }
        
        return ret;
    }
    
    /**
     * Adds a parameter to this method
     * 
     * @param name
     * @param type
     * @param annotations
     */
    public void addParameter(String name, String type, List<AnnotationMetadata> annotations) {
        addParameter(new JavaSymbolName(name), new JavaType(type), annotations);
    }
    
    /**
     * Adds a parameter to this method
     * 
     * @param name
     * @param type
     * @param annotations
     */
    public void addParameter(JavaSymbolName name, JavaType type, List<AnnotationMetadata> annotations) {
        if( this.parameters == null ) this.parameters = new ArrayList<MethodParameter>();
        this.parameters.add(new MethodParameter(name, type, annotations));
    }

    /**
     * @return the throwTypes
     */
    public List<JavaType> getThrowTypes() {
        if( throwTypes == null ) throwTypes = new ArrayList<JavaType>();
        return throwTypes;
    }

    /**
     * @param throwTypes the throwTypes to set
     */
    public void setThrowTypes(List<JavaType> throwTypes) {
        this.throwTypes = throwTypes;
    }
    
    public void addThrowType(String exception) {
        addThrowType(new JavaType(exception));
    }
    
    public void addThrowType(JavaType exception) {
        if( throwTypes == null ) throwTypes = new ArrayList<JavaType>();
        throwTypes.add(exception);
    }
    
    public void addBody(String body) {
        if( bodyBuilder == null ) bodyBuilder = new InvocableMemberBodyBuilder();
        bodyBuilder.appendFormalLine(body);
    }

    /**
     * Call to add the metadata to the builder for this field. 
     * 
     * @param builder The builder to add it
     * @param typeDetails The details of the type
     * @param typeId The ID of the type
     */
    public void addMetadata(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        List<? extends MethodMetadata> methods = typeDetails.getDeclaredMethods();
        
        List<AnnotatedJavaType> expectedParams = getParameterTypes();
        MethodMetadata ret = null;
        if( methods != null ) {
            Iterator<? extends MethodMetadata> it = methods.iterator();
            while( it.hasNext() && ret == null ) {
                MethodMetadata mm = it.next();
                
                if( !mm.getMethodName().equals(getName()) ) continue;
                if( returnType != null && !mm.getReturnType().equals(returnType) ) continue;
                List<AnnotatedJavaType> params = mm.getParameterTypes();
                if( params.size() != expectedParams.size() ) continue;
                else {
                    boolean match = true;
                    for( int i = 0 ; i < params.size() ; i++ ) {
                        AnnotatedJavaType p = params.get(i);
                        AnnotatedJavaType ep = expectedParams.get(i);
                        if( !p.equals(ep) ) {
                            match = false;
                            break;
                        }
                    }
                    
                    if( match ) ret = mm;
                }
            }
        }
        
        if( ret == null ) {
            String body = bodyBuilder != null ? bodyBuilder.getOutput() : null;
            ret = new DefaultMethodMetadata(typeId, getModifier(), getName(), getReturnType(), expectedParams, getParameterNames(), getAnnotations(), getThrowTypes(), body);
        }
        
        builder.addMethod(ret);
    }
    
    private static class MethodParameter {
        private final JavaSymbolName name;
        private final AnnotatedJavaType type;
        
        public MethodParameter(JavaSymbolName name, JavaType type, List<AnnotationMetadata> annotations) {
            this.name = name;
            this.type = new AnnotatedJavaType(type, annotations);
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
        public AnnotatedJavaType getType() {
            return type;
        }
    }
}
