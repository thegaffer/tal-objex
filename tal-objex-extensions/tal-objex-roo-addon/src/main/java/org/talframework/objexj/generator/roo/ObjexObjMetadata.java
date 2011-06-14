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
import org.talframework.objexj.generator.roo.generator.object.ObjexObjReaderGenerator;
import org.talframework.objexj.generator.roo.generator.object.ValidationGenerator;
import org.talframework.objexj.generator.roo.utils.TypeDetailsUtil;

/**
 * Metadata Item for an Objex Object. This object in the code contains
 * the real business behaviour.
 * 
 * @author Tom Spencer
 */
public class ObjexObjMetadata extends AbstractItdTypeDetailsProvidingMetadataItem {
    
    private static final String PROVIDES_TYPE_STRING = ObjexObjMetadata.class.getName();
    private static final String PROVIDES_TYPE = MetadataIdentificationUtils.create(PROVIDES_TYPE_STRING);
    
    private ObjexObjGenerator objGenerator;
    private ObjexObjFieldAccessorGenerator fieldGenerator;
    private ObjexObjReaderGenerator readerGenerator;
    private ValidationGenerator validationGenerator;
    
    public ObjexObjMetadata(String identifier, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata, ObjexObjAnnotation values, List<ObjexField> properties) {
        super(identifier, aspectName, governorPhysicalTypeMetadata);
        initialiseGenerators();
        
        if( !TypeDetailsUtil.isExtending(governorTypeDetails, "org.talframework.objexj.object.BaseObjexObj") ) {
            builder.addExtendsType(new JavaType("org.talframework.objexj.object.BaseObjexObj"));
        }
        
        // Call the generators
        objGenerator.generate(values);
        fieldGenerator.generate(properties);
        readerGenerator.generate(properties);
        validationGenerator.build(getValidationMethods());
        
        itdTypeDetails = builder.build();
    }
    
    private void initialiseGenerators() {
        if( objGenerator == null ) objGenerator = new ObjexObjGenerator(builder, governorTypeDetails, getId());
        if( fieldGenerator == null ) fieldGenerator = new ObjexObjFieldAccessorGenerator(builder, governorTypeDetails, getId());
        if( readerGenerator == null ) readerGenerator = new ObjexObjReaderGenerator(builder, governorTypeDetails, getId());
        if( validationGenerator == null ) validationGenerator = new ValidationGenerator(builder, governorTypeDetails, getId());
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
