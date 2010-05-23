package org.tpspencer.tal.objexj.roo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.roo.addon.javabean.JavaBeanMetadataProvider;
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
import org.tpspencer.tal.objexj.annotations.ObjexStateBean;

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
    private static final Log logger = LogFactory.getLog(ObjexStateBeanMetadataProvider.class);

    public ObjexStateBeanMetadataProvider(MetadataService metadataService, MetadataDependencyRegistry metadataDependencyRegistry, FileManager fileManager, JavaBeanMetadataProvider javaBeanProvider) {
        super(metadataService, metadataDependencyRegistry, fileManager);
        javaBeanProvider.addMetadataTrigger(new JavaType(ObjexStateBean.class.getName()));
        addMetadataTrigger(new JavaType(ObjexStateBean.class.getName()));
        
        logger.debug("*** ObjexStateBean Provider Started");
    }

    @Override
    protected ItdTypeDetailsProvidingMetadataItem getMetadata(
            String metadataIdentificationString, JavaType aspectName,
            PhysicalTypeMetadata governorPhysicalTypeMetadata,
            String itdFilename) {
        logger.debug("*** ObjexStateBean Metadata Requested: " + governorPhysicalTypeMetadata.getId());
        return new ObjexStateBeanMetadata(metadataIdentificationString, aspectName, governorPhysicalTypeMetadata);
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
