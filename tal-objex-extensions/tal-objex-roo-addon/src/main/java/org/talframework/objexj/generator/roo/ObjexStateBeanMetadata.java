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
import org.talframework.objexj.annotations.source.ObjexStateBean;
import org.talframework.objexj.generator.roo.annotations.ObjexObjStateBeanAnnotation;
import org.talframework.objexj.generator.roo.compiler.PropertyCompiler;
import org.talframework.objexj.generator.roo.fields.ObjexField;
import org.talframework.objexj.generator.roo.generator.state.StateBeanGenerator;
import org.talframework.objexj.generator.roo.generator.state.StateCloneGenerator;
import org.talframework.objexj.generator.roo.generator.state.StateFieldAccessorGenerator;
import org.talframework.objexj.generator.roo.generator.state.StateGAEGenerator;
import org.talframework.objexj.generator.roo.generator.state.StateGenerator;
import org.talframework.objexj.generator.roo.generator.state.StateTempRefsGenerator;
import org.talframework.objexj.generator.roo.generator.state.StateWriterGenerator;
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
    private StateBeanGenerator beanGenerator;
    private StateTempRefsGenerator tempRefsGenerator;
    private StateFieldAccessorGenerator fieldGenerator;
    private StateWriterGenerator writerGenerator;
    private StateGAEGenerator gaeGenerator;
    
    private String type = null;
    private List<ObjexField> properties = null;
    
    public ObjexStateBeanMetadata(String identifier, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata, ObjexObjStateBeanAnnotation annotationValues) {
        super(identifier, aspectName, governorPhysicalTypeMetadata);
        
        initialiseGenerators();
        
        type = annotationValues.getName();
        
        // Annotation, UID and ObjexObj interface
        builder.addTypeAnnotation(getJDOAnnotation());
        if( !TypeDetailsUtil.isImplementing(governorTypeDetails, "org.talframework.objexj.ObjexObjStateBean") ) 
            builder.addImplementsType(new JavaType("org.talframework.objexj.ObjexObjStateBean"));
        builder.addField(getSerialVersionField());
        
        // Call the individual generators
        stateGenerator.generate(annotationValues);
        cloneGenerator.generate(getProperties());
        beanGenerator.generate(getProperties());
        fieldGenerator.generate(getProperties());
        tempRefsGenerator.generate(getProperties());
        gaeGenerator.generate(getProperties());
        writerGenerator.generate(getProperties());
        
        // Create the ITD Type
        itdTypeDetails = builder.build();
    }
    
    private void initialiseGenerators() {
        if( stateGenerator == null ) stateGenerator = new StateGenerator(builder, governorTypeDetails, getId());
        if( cloneGenerator == null ) cloneGenerator = new StateCloneGenerator(builder, governorTypeDetails, getId());
        if( beanGenerator == null ) beanGenerator = new StateBeanGenerator(builder, governorTypeDetails, getId());
        if( tempRefsGenerator == null ) tempRefsGenerator = new StateTempRefsGenerator(builder, governorTypeDetails, getId());
        if( fieldGenerator == null ) fieldGenerator = new StateFieldAccessorGenerator(builder, governorTypeDetails, getId());
        if( writerGenerator == null ) writerGenerator = new StateWriterGenerator(builder, governorTypeDetails, getId());
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
    
    public String getType() {
        return type;
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
