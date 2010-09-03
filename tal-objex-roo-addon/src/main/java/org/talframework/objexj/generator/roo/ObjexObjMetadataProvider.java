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

import org.springframework.roo.addon.tostring.ToStringMetadataProvider;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.itd.AbstractItdMetadataProvider;
import org.springframework.roo.classpath.itd.ItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.metadata.MetadataDependencyRegistry;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.project.Path;
import org.springframework.roo.support.lifecycle.ScopeDevelopment;
import org.springframework.roo.support.util.Assert;
import org.talframework.objexj.annotations.ObjexObj;

/**
 * This class links an class with the {@link ObjexObj} annotation
 * to a {@link ObjexObjMetadata} instance. 
 * 
 * @author Tom Spencer
 */
@ScopeDevelopment
public class ObjexObjMetadataProvider extends AbstractItdMetadataProvider {
    
    public ObjexObjMetadataProvider(MetadataService metadataService, MetadataDependencyRegistry metadataDependencyRegistry, FileManager fileManager, ToStringMetadataProvider toStringProvider) {
        super(metadataService, metadataDependencyRegistry, fileManager);
        
        JavaType type = new JavaType(ObjexObj.class.getName());
        toStringProvider.addMetadataTrigger(type); // Not sure about this, what about base!!
        addMetadataTrigger(type);
    }

    @Override
    protected ItdTypeDetailsProvidingMetadataItem getMetadata(
            String metadataIdentificationString, JavaType aspectName,
            PhysicalTypeMetadata governorPhysicalTypeMetadata,
            String itdFilename) {
        
        ObjexObjAnnotationValues values = new ObjexObjAnnotationValues(governorPhysicalTypeMetadata);
        Assert.notNull(values.getValue(), "Error, type does not appear to have ObjexObj annotation or a ObjexObj StateBean value: " + governorPhysicalTypeMetadata);
        
        String stateMetadataKey = ObjexStateBeanMetadata.createIdentifier(values.getValue(), Path.SRC_MAIN_JAVA);
        ObjexStateBeanMetadata stateMetadata = (ObjexStateBeanMetadata)metadataService.get(stateMetadataKey);
        Assert.notNull(stateMetadata, "Error, cannot find the state bean metadata for the ObjexObj state bean class: " + values.getValue() + " [" + stateMetadataKey + "]");
        
        return new ObjexObjMetadata(metadataIdentificationString, aspectName, governorPhysicalTypeMetadata, values, stateMetadata.getProperties());
    }
    
    public String getItdUniquenessFilenameSuffix() {
        return "ObjexObj";
    }
    
    public String getProvidesType() {
        return ObjexObjMetadata.getMetadataIdentiferType();
    }
    
    @Override
    protected String getGovernorPhysicalTypeIdentifier(String metadataIdentificationString) {
        JavaType javaType = ObjexObjMetadata.getJavaType(metadataIdentificationString);
        Path path = ObjexObjMetadata.getPath(metadataIdentificationString);
        String physicalTypeIdentifier = PhysicalTypeIdentifier.createIdentifier(javaType, path);
        return physicalTypeIdentifier;
    }
    
    @Override
    protected String createLocalIdentifier(JavaType javaType, Path path) {
        return ObjexObjMetadata.createIdentifier(javaType, path);
    }
}
