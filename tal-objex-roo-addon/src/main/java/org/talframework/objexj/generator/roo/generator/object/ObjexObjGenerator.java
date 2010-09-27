package org.talframework.objexj.generator.roo.generator.object;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.springframework.roo.model.JavaSymbolName;
import org.talframework.objexj.generator.roo.annotations.ObjexObjAnnotation;
import org.talframework.objexj.generator.roo.generator.BaseGenerator;
import org.talframework.objexj.generator.roo.utils.MethodMetadataWrapper;
import org.talframework.objexj.generator.roo.utils.TypeConstants;

/**
 * This class enriches the basic details on the ObjexObj
 * class.
 *
 * @author Tom Spencer
 */
public class ObjexObjGenerator extends BaseGenerator {
    
    private ObjexObjAnnotation annotationValues;
    
    public ObjexObjGenerator(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        super(builder, typeDetails, typeId);
    }
    
    /**
     * Call to generate the accessor/mutator methods for the given
     * fields.
     * 
     * @param fields The fields
     */
    public void generate(ObjexObjAnnotation annotationValues) {
        this.annotationValues = annotationValues;
        
        addStateAccessor();
    }
    
    /**
     * @return The metadata item for the ID field
     */
    private void addStateAccessor() {
        MethodMetadataWrapper localGetter = new MethodMetadataWrapper(new JavaSymbolName("getLocalState"), annotationValues.getValue());
        localGetter.addBody("return bean;");
        localGetter.addMetadata(builder, typeDetails, typeId);
        
        MethodMetadataWrapper getter = new MethodMetadataWrapper("getStateBean", TypeConstants.OBJEXSTATEBEAN);
        getter.addBody("return bean;");
        getter.addMetadata(builder, typeDetails, typeId);
    }
}
