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

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.itd.AbstractItdMetadataProvider;
import org.springframework.roo.classpath.itd.ItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Path;
import org.talframework.objexj.annotations.source.ObjexObj;
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
        values.setObjexType(stateMetadata.getType());
        
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
