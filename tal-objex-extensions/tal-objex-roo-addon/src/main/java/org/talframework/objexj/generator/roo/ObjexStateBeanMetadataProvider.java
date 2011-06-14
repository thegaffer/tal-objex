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
