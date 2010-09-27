package org.talframework.objexj.generator.roo.generator.object;

import static org.talframework.objexj.generator.roo.utils.TypeConstants.CHILD_VALIDATOR;
import static org.talframework.objexj.generator.roo.utils.TypeConstants.CHILD_VALIDATOR_METHOD;
import static org.talframework.objexj.generator.roo.utils.TypeConstants.CONSTARINT_CONTEXT;
import static org.talframework.objexj.generator.roo.utils.TypeConstants.CONSTRAINT_BUILDER;
import static org.talframework.objexj.generator.roo.utils.TypeConstants.INTER_ENRICHER;
import static org.talframework.objexj.generator.roo.utils.TypeConstants.INTER_ENRICHER_METHOD;
import static org.talframework.objexj.generator.roo.utils.TypeConstants.INTER_VALIDATOR;
import static org.talframework.objexj.generator.roo.utils.TypeConstants.INTER_VALIDATOR_METHOD;
import static org.talframework.objexj.generator.roo.utils.TypeConstants.INTRA_ENRICHER;
import static org.talframework.objexj.generator.roo.utils.TypeConstants.INTRA_ENRICHER_METHOD;
import static org.talframework.objexj.generator.roo.utils.TypeConstants.INTRA_VALIDATOR;
import static org.talframework.objexj.generator.roo.utils.TypeConstants.INTRA_VALIDATOR_METHOD;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.talframework.objexj.generator.roo.compiler.ValidationMethodsCompiler;
import org.talframework.objexj.generator.roo.generator.BaseGenerator;
import org.talframework.objexj.generator.roo.utils.MethodMetadataWrapper;

/**
 * This class is responsible for actually generating the
 * validation methods.
 *
 * @author Tom Spencer
 */
public final class ValidationGenerator extends BaseGenerator {

    public ValidationGenerator(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        super(builder, typeDetails, typeId);
    }
    
    /**
     * Call to generate the validation and enrichment methods
     * and plumbing.
     */
    //@Trace
    public void build(ValidationMethodsCompiler methods) {
        addEnricher(INTRA_ENRICHER, INTRA_ENRICHER_METHOD, methods.getIntraObjectEnrichMethods());
        addEnricher(INTER_ENRICHER, INTER_ENRICHER_METHOD, methods.getInterObjectEnrichMethods());
        
        addValidator(INTRA_VALIDATOR, INTRA_VALIDATOR_METHOD, methods.getIntraObjectValidateMethods());
        addValidator(INTER_VALIDATOR, INTER_VALIDATOR_METHOD, methods.getInterObjectValidateMethods());
        addValidator(CHILD_VALIDATOR, CHILD_VALIDATOR_METHOD, methods.getChildValidateMethods());
    }
    
    /**
     * Helper to create a validation method given the interface
     * to implement and the method name for that interface.
     */
    //@Trace
    private void addValidator(String iface, String name, Map<String, ? extends MethodMetadata> methods) {
        if( methods == null || methods.size() == 0 ) return;
        
        builder.getImportRegistrationResolver().addImport(new JavaType(CONSTRAINT_BUILDER));
        builder.addImplementsType(new JavaType(iface));
        
        MethodMetadataWrapper wrapper = new MethodMetadataWrapper(new JavaSymbolName(name), JavaType.BOOLEAN_PRIMITIVE);
        wrapper.addParameter("context", CONSTARINT_CONTEXT, null);
        
        wrapper.addBody("ConstraintViolationBuilder violation = null;\n");
        
        Iterator<String> it = methods.keySet().iterator();
        while( it.hasNext() ) {
            String message = it.next();
            MethodMetadata method = methods.get(message);
            
            wrapper.addBody("if( !this." + method.getMethodName().getSymbolName() + "() ) {");
            wrapper.addBody("\tcontext.disableDefaultConstraintViolation();");
            wrapper.addBody("\tviolation = context.buildConstraintViolationWithTemplate(\"" + message + "\");");
            wrapper.addBody("\tviolation.addConstraintViolation();");
            wrapper.addBody("}\n");
        }
        
        wrapper.addBody("if( violation != null ) context.disableDefaultConstraintViolation();");
        wrapper.addBody("return violation != null ? false : true;");
        
        wrapper.addMetadata(builder, typeDetails, typeId);
    }
    
    /**
     * Helper to create a validation method given the interface
     * to implement and the method name for that interface.
     */
    //@Trace
    private void addEnricher(String iface, String name, List<? extends MethodMetadata> methods) {
        if( methods == null || methods.size() == 0 ) return;
        
        builder.addImplementsType(new JavaType(iface));
        
        MethodMetadataWrapper wrapper = new MethodMetadataWrapper(new JavaSymbolName(name), JavaType.VOID_PRIMITIVE);
        wrapper.addParameter("context", CONSTARINT_CONTEXT, null);
        
        Iterator<? extends MethodMetadata> it = methods.iterator();
        while( it.hasNext() ) {
            MethodMetadata method = it.next();
            
            wrapper.addBody("this." + method.getMethodName().getSymbolName() + "();");
        }
        
        wrapper.addMetadata(builder, typeDetails, typeId);
    }
}
