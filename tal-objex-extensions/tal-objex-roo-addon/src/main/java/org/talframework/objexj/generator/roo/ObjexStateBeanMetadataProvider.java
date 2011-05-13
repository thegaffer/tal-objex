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

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.itd.AbstractItdMetadataProvider;
import org.springframework.roo.classpath.itd.ItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Path;
import org.talframework.objexj.annotations.source.ObjexStateBean;
import org.talframework.objexj.generator.roo.annotations.ObjexObjStateBeanAnnotation;

/**
 * This class links the Spring Roo framework to the 
 * {@link ObjexStateBeanMetadata} item, an instance of which
 * is added to each and every class holding the 
 * {@link ObjexStateBean} annotation.
 * 
 * @author Tom Spencer
 */
@Component(immediate=true)
@Service
public class ObjexStateBeanMetadataProvider extends AbstractItdMetadataProvider {
    
    protected void activate(ComponentContext context) {
        metadataDependencyRegistry.registerDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(), getProvidesType());
        addMetadataTrigger(new JavaType(ObjexStateBean.class.getName()));
    }
    
    protected void deactivate(ComponentContext context) {
        metadataDependencyRegistry.deregisterDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(), getProvidesType());
        removeMetadataTrigger(new JavaType(ObjexStateBean.class.getName()));   
    }

    protected ItdTypeDetailsProvidingMetadataItem getMetadata(
            String metadataIdentificationString, JavaType aspectName,
            PhysicalTypeMetadata governorPhysicalTypeMetadata,
            String itdFilename) {
        
        // Get main annotation
        ObjexObjStateBeanAnnotation values = ObjexObjStateBeanAnnotation.get(governorPhysicalTypeMetadata);
        if( values.getName() == null || values.getName().equals("") ) throw new IllegalArgumentException("Error, type does not appear to have an ObjexStateBean annotation or a ObjexObj name value: " + governorPhysicalTypeMetadata);
        
        return new ObjexStateBeanMetadata(metadataIdentificationString, aspectName, governorPhysicalTypeMetadata, values);
    }
    
    public String getItdUniquenessFilenameSuffix() {
        return "ObjexStateBean";
    }
    
    public String getProvidesType() {
        return ObjexStateBeanMetadata.getMetadataIdentiferType();
    }
    
    protected String getGovernorPhysicalTypeIdentifier(String metadataIdentificationString) {
        JavaType javaType = ObjexStateBeanMetadata.getJavaType(metadataIdentificationString);
        Path path = ObjexStateBeanMetadata.getPath(metadataIdentificationString);
        String physicalTypeIdentifier = PhysicalTypeIdentifier.createIdentifier(javaType, path);
        return physicalTypeIdentifier;
    }
    
    protected String createLocalIdentifier(JavaType javaType, Path path) {
        return ObjexStateBeanMetadata.createIdentifier(javaType, path);
    }
}
