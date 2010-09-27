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
import org.talframework.objexj.annotations.ObjexObj;
import org.talframework.objexj.generator.roo.annotations.ObjexObjAnnotation;

/**
 * This class links an class with the {@link ObjexObj} annotation
 * to a {@link ObjexObjMetadata} instance. 
 * 
 * @author Tom Spencer
 */
@Component(immediate=true)
@Service
public class ObjexObjMetadataProvider extends AbstractItdMetadataProvider {
    
    protected void activate(ComponentContext context) {
        metadataDependencyRegistry.registerDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(), getProvidesType());
        addMetadataTrigger(new JavaType(ObjexObj.class.getName()));
    }
    
    protected void deactivate(ComponentContext context) {
        metadataDependencyRegistry.deregisterDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(), getProvidesType());
        removeMetadataTrigger(new JavaType(ObjexObj.class.getName()));   
    }
    
    protected ItdTypeDetailsProvidingMetadataItem getMetadata(
            String metadataIdentificationString, JavaType aspectName,
            PhysicalTypeMetadata governorPhysicalTypeMetadata,
            String itdFilename) {
        
        ObjexObjAnnotation values = ObjexObjAnnotation.get(governorPhysicalTypeMetadata);
        if( values.getValue() == null ) throw new IllegalArgumentException("Error, type does not appear to have ObjexObj annotation or a ObjexObj StateBean value: " + governorPhysicalTypeMetadata);
        
        String stateMetadataKey = ObjexStateBeanMetadata.createIdentifier(values.getValue(), Path.SRC_MAIN_JAVA);
        ObjexStateBeanMetadata stateMetadata = (ObjexStateBeanMetadata)metadataService.get(stateMetadataKey);
        if( stateMetadata == null ) throw new IllegalArgumentException("Error, cannot find the state bean metadata for the ObjexObj state bean class: " + values.getValue() + " [" + stateMetadataKey + "]");
        
        return new ObjexObjMetadata(metadataIdentificationString, aspectName, governorPhysicalTypeMetadata, values, stateMetadata.getProperties());
    }
    
    public String getItdUniquenessFilenameSuffix() {
        return "ObjexObj";
    }
    
    public String getProvidesType() {
        return ObjexObjMetadata.getMetadataIdentiferType();
    }
    
    protected String getGovernorPhysicalTypeIdentifier(String metadataIdentificationString) {
        JavaType javaType = ObjexObjMetadata.getJavaType(metadataIdentificationString);
        Path path = ObjexObjMetadata.getPath(metadataIdentificationString);
        String physicalTypeIdentifier = PhysicalTypeIdentifier.createIdentifier(javaType, path);
        return physicalTypeIdentifier;
    }
    
    protected String createLocalIdentifier(JavaType javaType, Path path) {
        return ObjexObjMetadata.createIdentifier(javaType, path);
    }
}
