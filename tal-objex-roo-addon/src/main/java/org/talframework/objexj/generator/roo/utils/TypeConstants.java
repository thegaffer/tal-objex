package org.talframework.objexj.generator.roo.utils;

import org.springframework.roo.model.DataType;
import org.springframework.roo.model.JavaType;

/**
 * Holds various constants used throughout the generator.
 *
 * @author Tom Spencer
 */
public class TypeConstants {
    
    public static final JavaType OBJECT = new JavaType("java.lang.Object", 0, DataType.TYPE, null, null);

    public static final String OBJEXOBJ = "org.talframework.objexj.ObjexObj";
    public static final String OBJEXSTATEBEAN = "org.talframework.objexj.ObjexObjStateBean";
    public static final String OBJEXID = "org.talframework.objexj.ObjexID";
    
    ///////////////////////////////
    // Object
    
    public static final String BASEOBJ = "org.talframework.objexj.object.BaseObjexObj";
    public static final String SIMPLEUTILS = "org.talframework.objexj.object.SimpleFieldUtils";
    public static final String REFUTILS = "org.talframework.objexj.object.ReferenceFieldUtils";
    public static final String REFLISTUTILS = "org.talframework.objexj.object.ReferenceListFieldUtils";
    public static final String REFMAPUTILS = "org.talframework.objexj.object.ReferenceMapFieldUtils";
    public static final String BEANUTILS = "org.talframework.objexj.object.StateBeanUtils";
    
    ///////////////////////////////
    // Validation
    
    public static final String INTRA_ENRICHER = "org.talframework.objexj.validation.object.SelfIntraObjectEnricher";
    public static final String INTRA_ENRICHER_METHOD = "enrichObject";
    public static final String INTER_ENRICHER = "org.talframework.objexj.validation.object.SelfInterObjectEnricher";
    public static final String INTER_ENRICHER_METHOD = "enrichObjectAgainstOthers";
    
    public static final String INTRA_VALIDATOR = "org.talframework.objexj.validation.object.SelfIntraObjectValidator";
    public static final String INTRA_VALIDATOR_METHOD = "validateObject";
    public static final String INTER_VALIDATOR = "org.talframework.objexj.validation.object.SelfInterObjectValidator";
    public static final String INTER_VALIDATOR_METHOD = "validateObjectAgainstOthers";
    public static final String CHILD_VALIDATOR = "org.talframework.objexj.validation.object.SelfChildValidator";
    public static final String CHILD_VALIDATOR_METHOD = "validateChildren";
    
    public static final String CONSTARINT_CONTEXT = "javax.validation.ConstraintValidatorContext";
    public static final String CONSTRAINT_BUILDER = "javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder";
}
