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

import java.util.List;

import org.springframework.roo.classpath.PhysicalTypeIdentifierNamingUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.metadata.MetadataIdentificationUtils;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Path;
import org.talframework.objexj.generator.roo.annotations.ObjexObjAnnotation;
import org.talframework.objexj.generator.roo.compiler.ValidationMethodsCompiler;
import org.talframework.objexj.generator.roo.fields.ObjexField;
import org.talframework.objexj.generator.roo.generator.object.ObjexObjFieldAccessorGenerator;
import org.talframework.objexj.generator.roo.generator.object.ObjexObjGenerator;
import org.talframework.objexj.generator.roo.generator.object.ObjexXmlWriterGenerator;
import org.talframework.objexj.generator.roo.generator.object.ValidationGenerator;
import org.talframework.objexj.generator.roo.utils.TypeDetailsUtil;

/**
 * Metadata Item for an Objex Object. This object in the code contains
 * the real business behaviour.
 * 
 * TODO: Set methods on owned members that copy fields into existing (creating first as necc)
 * 
 * @author Tom Spencer
 */
public class ObjexObjMetadata extends AbstractItdTypeDetailsProvidingMetadataItem {
    
    private static final String PROVIDES_TYPE_STRING = ObjexObjMetadata.class.getName();
    private static final String PROVIDES_TYPE = MetadataIdentificationUtils.create(PROVIDES_TYPE_STRING);
    
    private ObjexObjGenerator objGenerator;
    private ObjexObjFieldAccessorGenerator fieldGenerator;
    private ValidationGenerator validationGenerator;
    private ObjexXmlWriterGenerator xmlGenerator;
    
    public ObjexObjMetadata(String identifier, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata, ObjexObjAnnotation values, List<ObjexField> properties) {
        super(identifier, aspectName, governorPhysicalTypeMetadata);
        initialiseGenerators();
        
        if( !TypeDetailsUtil.isExtending(governorTypeDetails, "org.talframework.objexj.object.BaseObjexObj") ) {
            builder.addExtendsType(new JavaType("org.talframework.objexj.object.BaseObjexObj"));
        }
        
        // Call the generators
        objGenerator.generate(values);
        fieldGenerator.generate(properties);
        validationGenerator.build(getValidationMethods());
        
        itdTypeDetails = builder.build();
    }
    
    private void initialiseGenerators() {
        if( objGenerator == null ) objGenerator = new ObjexObjGenerator(builder, governorTypeDetails, getId());
        if( fieldGenerator == null ) fieldGenerator = new ObjexObjFieldAccessorGenerator(builder, governorTypeDetails, getId());
        if( validationGenerator == null ) validationGenerator = new ValidationGenerator(builder, governorTypeDetails, getId());
        if( xmlGenerator == null ) xmlGenerator = new ObjexXmlWriterGenerator(builder, governorTypeDetails, getId());
    }
    
    private ValidationMethodsCompiler getValidationMethods() {
        ValidationMethodsCompiler validators = new ValidationMethodsCompiler();
        validators.compile(governorTypeDetails);
        return validators;
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
