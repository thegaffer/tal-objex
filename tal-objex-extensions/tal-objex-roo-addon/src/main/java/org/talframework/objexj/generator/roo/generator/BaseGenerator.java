package org.talframework.objexj.generator.roo.generator;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;

/**
 * This class is the base class for any generator and
 * holds details about the type we are generating for.
 *
 * @author Tom Spencer
 */
public abstract class BaseGenerator {

    protected final DefaultItdTypeDetailsBuilder builder;
    protected final ClassOrInterfaceTypeDetails typeDetails;
    protected final String typeId;
    
    /**
     * Constructs a base generator holding the type details
     * given.
     * 
     * @param builder The builder
     * @param typeDetails The type details
     * @param typeId The type ID
     */
    public BaseGenerator(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        this.builder = builder;
        this.typeDetails = typeDetails;
        this.typeId = typeId;
    }
}
