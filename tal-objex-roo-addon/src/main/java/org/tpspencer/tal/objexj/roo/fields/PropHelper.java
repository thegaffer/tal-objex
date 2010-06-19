package org.tpspencer.tal.objexj.roo.fields;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.tpspencer.tal.objexj.roo.ObjexObjMetadata;
import org.tpspencer.tal.objexj.roo.utils.MethodMetadataWrapper;

/**
 * This abstract class provides support for all the property
 * helper classes that aid {@link ObjexObjMetadata} to output
 * methods relating to properties
 * 
 * @author Tom Spencer
 */
public abstract class PropHelper {
    
    /** Holds the property we are generating methods for */
    protected final ObjexObjProperty prop;
    /** Holds the methods added by derived class */
    protected List<MethodMetadataWrapper> methods = new ArrayList<MethodMetadataWrapper>();
    
    protected PropHelper(ObjexObjProperty prop) {
        this.prop = prop;
    }
    
    /**
     * @return The main getter method name
     */
    protected String getGetterName() {
        return "get" + prop.getName().getSymbolNameCapitalisedFirstLetter();
    }
    
    /**
     * @return The main setter method name
     */
    protected String getSetterName() {
        return "set" + prop.getName().getSymbolNameCapitalisedFirstLetter();
    }
    
    /**
     * Call to actually build the methods relating to the property
     * inside the ObjexObj.
     * 
     * @param builder The builder to use
     * @param typeDetails The details of the governing type
     * @param typeId The metadata id for the above details
     */
    public void build(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        Iterator<MethodMetadataWrapper> it = methods.iterator();
        while( it.hasNext() ) {
            it.next().addMetadata(builder, typeDetails, typeId);
        }
    }
}
