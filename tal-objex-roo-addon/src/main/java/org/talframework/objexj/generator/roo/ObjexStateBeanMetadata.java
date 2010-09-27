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

package org.talframework.objexj.generator.roo;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.classpath.PhysicalTypeIdentifierNamingUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.DefaultFieldMetadata;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.DefaultAnnotationMetadata;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.metadata.MetadataIdentificationUtils;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Path;
import org.talframework.objexj.annotations.ObjexStateBean;
import org.talframework.objexj.generator.roo.annotations.ObjexObjStateBeanAnnotation;
import org.talframework.objexj.generator.roo.compiler.PropertyCompiler;
import org.talframework.objexj.generator.roo.fields.ObjexField;
import org.talframework.objexj.generator.roo.generator.state.StateCloneGenerator;
import org.talframework.objexj.generator.roo.generator.state.StateFieldAccessorGenerator;
import org.talframework.objexj.generator.roo.generator.state.StateGAEGenerator;
import org.talframework.objexj.generator.roo.generator.state.StateGenerator;
import org.talframework.objexj.generator.roo.generator.state.StateTempRefsGenerator;
import org.talframework.objexj.generator.roo.generator.state.StateWriterGenerator;
import org.talframework.objexj.generator.roo.generator.state.StateXmlWriterGenerator;
import org.talframework.objexj.generator.roo.utils.TypeDetailsUtil;

/**
 * This class enriches the class, holding a {@link ObjexStateBean}
 * annotation, to which it is attached with the persistence
 * tags and ensures there is a getter/setter as appropriate for
 * the bean.
 * 
 * @author Tom Spencer
 */
public class ObjexStateBeanMetadata extends AbstractItdTypeDetailsProvidingMetadataItem {
    private static final String PROVIDES_TYPE_STRING = ObjexStateBeanMetadata.class.getName();
    private static final String PROVIDES_TYPE = MetadataIdentificationUtils.create(PROVIDES_TYPE_STRING);
    
    private StateGenerator stateGenerator;
    private StateCloneGenerator cloneGenerator;
    private StateTempRefsGenerator tempRefsGenerator;
    private StateFieldAccessorGenerator fieldGenerator;
    private StateWriterGenerator writerGenerator;
    private StateXmlWriterGenerator xmlWriterGenerator;
    private StateGAEGenerator gaeGenerator;
    
    private List<ObjexField> properties = null;
    
    public ObjexStateBeanMetadata(String identifier, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata, ObjexObjStateBeanAnnotation annotationValues) {
        super(identifier, aspectName, governorPhysicalTypeMetadata);
        
        initialiseGenerators();
        
        // Annotation, UID and ObjexObj interface
        builder.addTypeAnnotation(getJDOAnnotation());
        if( !TypeDetailsUtil.isImplementing(governorTypeDetails, "org.talframework.objexj.ObjexObjStateBean") ) 
            builder.addImplementsType(new JavaType("org.talframework.objexj.ObjexObjStateBean"));
        builder.addField(getSerialVersionField());
        
        // Call the individual generators
        stateGenerator.generate(annotationValues);
        cloneGenerator.generate(getProperties());
        fieldGenerator.generate(getProperties());
        tempRefsGenerator.generate(getProperties());
        gaeGenerator.generate(getProperties());
        writerGenerator.generate(getProperties());
        xmlWriterGenerator.generate(getProperties());
        
        // Create the ITD Type
        itdTypeDetails = builder.build();
    }
    
    private void initialiseGenerators() {
        if( stateGenerator == null ) stateGenerator = new StateGenerator(builder, governorTypeDetails, getId());
        if( cloneGenerator == null ) cloneGenerator = new StateCloneGenerator(builder, governorTypeDetails, getId());
        if( tempRefsGenerator == null ) tempRefsGenerator = new StateTempRefsGenerator(builder, governorTypeDetails, getId());
        if( fieldGenerator == null ) fieldGenerator = new StateFieldAccessorGenerator(builder, governorTypeDetails, getId());
        if( writerGenerator == null ) writerGenerator = new StateWriterGenerator(builder, governorTypeDetails, getId());
        if( xmlWriterGenerator == null ) xmlWriterGenerator = new StateXmlWriterGenerator(builder, governorTypeDetails, getId());
        if( gaeGenerator == null ) gaeGenerator = new StateGAEGenerator(builder, governorTypeDetails, getId());
    }
    
    /**
     * @return The properties against this type
     */
    public List<ObjexField> getProperties() {
        if( this.properties == null ) {
            PropertyCompiler compiler = new PropertyCompiler();
            this.properties = compiler.compile(governorTypeDetails);
        }
        
        return properties;
    }
    
    /**
     * Finds the target annotation on the type or from another metadata item
     * or creates it if is does not exist.
     * 
     * @return The target annotation metadata
     */
    private AnnotationMetadata getJDOAnnotation() {
        AnnotationMetadata ret = TypeDetailsUtil.getAnnotation(governorTypeDetails, "javax.jdo.annotations.PersistenceCapable");
        
        if( ret == null ) {
            List<AnnotationAttributeValue<?>> attrs = new ArrayList<AnnotationAttributeValue<?>>();
            ret = new DefaultAnnotationMetadata(new JavaType("javax.jdo.annotations.PersistenceCapable"), attrs);
        }
        
        return ret;
    }
    
    /**
     * @return The metadata item for the ID field
     */
    private FieldMetadata getSerialVersionField() {
        FieldMetadata ret = TypeDetailsUtil.getField(governorTypeDetails, "serialVersionUID");
        
        if( ret == null ) {
            List<AnnotationMetadata> annotations = new ArrayList<AnnotationMetadata>();
            ret = new DefaultFieldMetadata(getId(), Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE, new JavaSymbolName("serialVersionUID"), JavaType.LONG_PRIMITIVE, null, annotations);
        }
        
        return ret;
    }
    
    public static final String getMetadataIdentiferType() {
        return PROVIDES_TYPE;
    }
    
    public static final String createIdentifier(JavaType javaType, Path path) {
        return PhysicalTypeIdentifierNamingUtils.createIdentifier(PROVIDES_TYPE_STRING, javaType, path);
    }
    
    public static final JavaType getJavaType(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.getJavaType(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }

    public static final Path getPath(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.getPath(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }
    
    public static boolean isValid(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.isValid(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }
}
