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

import org.springframework.roo.addon.javabean.JavaBeanMetadataProvider;
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
import org.talframework.objexj.annotations.ObjexStateBean;

/**
 * This class links the Spring Roo framework to the 
 * {@link ObjexStateBeanMetadata} item, an instance of which
 * is added to each and every class holding the 
 * {@link ObjexStateBean} annotation.
 * 
 * @author Tom Spencer
 */
@ScopeDevelopment
public class ObjexStateBeanMetadataProvider extends AbstractItdMetadataProvider {
    
    public ObjexStateBeanMetadataProvider(MetadataService metadataService, MetadataDependencyRegistry metadataDependencyRegistry, FileManager fileManager, ToStringMetadataProvider toStringProvider, JavaBeanMetadataProvider beanProvider) {
        super(metadataService, metadataDependencyRegistry, fileManager);
        
        JavaType trigger = new JavaType(ObjexStateBean.class.getName());
        
        beanProvider.addMetadataTrigger(trigger);
        toStringProvider.addMetadataTrigger(trigger);
        addMetadataTrigger(trigger);
    }

    @Override
    protected ItdTypeDetailsProvidingMetadataItem getMetadata(
            String metadataIdentificationString, JavaType aspectName,
            PhysicalTypeMetadata governorPhysicalTypeMetadata,
            String itdFilename) {
        
        // Get main annotation
        ObjexObjStateBeanAnnotationValues values = new ObjexObjStateBeanAnnotationValues(governorPhysicalTypeMetadata);
        Assert.notNull(values.getName(), "Error, type does not appear to have an ObjexStateBean annotation or a ObjexObj name value: " + governorPhysicalTypeMetadata);
        
        return new ObjexStateBeanMetadata(metadataIdentificationString, aspectName, governorPhysicalTypeMetadata, values);
    }
    
    public String getItdUniquenessFilenameSuffix() {
        return "ObjexStateBean";
    }
    
    public String getProvidesType() {
        return ObjexStateBeanMetadata.getMetadataIdentiferType();
    }
    
    @Override
    protected String getGovernorPhysicalTypeIdentifier(String metadataIdentificationString) {
        JavaType javaType = ObjexStateBeanMetadata.getJavaType(metadataIdentificationString);
        Path path = ObjexStateBeanMetadata.getPath(metadataIdentificationString);
        String physicalTypeIdentifier = PhysicalTypeIdentifier.createIdentifier(javaType, path);
        return physicalTypeIdentifier;
    }
    
    @Override
    protected String createLocalIdentifier(JavaType javaType, Path path) {
        return ObjexStateBeanMetadata.createIdentifier(javaType, path);
    }
}
