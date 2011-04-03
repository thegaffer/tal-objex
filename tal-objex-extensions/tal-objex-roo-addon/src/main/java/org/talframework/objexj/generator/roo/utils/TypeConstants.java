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
    public static final String OBJEXSTATEWRITER = "org.talframework.objexj.ObjexStateWriter";
    public static final String OBJEXSTATEREADER = "org.talframework.objexj.ObjexStateReader";
    public static final String OBJEXID = "org.talframework.objexj.ObjexID";
    
    ///////////////////////////////
    // Object
    
    public static final String BASEOBJ = "org.talframework.objexj.object.BaseObjexObj";
    public static final String SIMPLEUTILS = "org.talframework.objexj.object.utils.SimpleFieldUtils";
    public static final String REFUTILS = "org.talframework.objexj.object.utils.ReferenceFieldUtils";
    public static final String REFLISTUTILS = "org.talframework.objexj.object.utils.ReferenceListFieldUtils";
    public static final String REFMAPUTILS = "org.talframework.objexj.object.utils.ReferenceMapFieldUtils";
    public static final String BEANUTILS = "org.talframework.objexj.object.utils.StateBeanUtils";
    
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
    
    ///////////////////////////////
    // XML - JAXB
    
    public static final String XML_OBJEXOBJ = "org.talframework.objexj.object.xml.XmlObjexObj";
    public static final JavaType XML_OBJEXOBJ_TYPE = new JavaType(XML_OBJEXOBJ);
    public static final String XML_ROOT = "javax.xml.bind.annotation.XmlRootElement";
    public static final JavaType XML_ROOT_TYPE = new JavaType(XML_ROOT);
    public static final String XML_TYPE = "javax.xml.bind.annotation.XmlType";
    public static final JavaType XML_TYPE_TYPE = new JavaType(XML_TYPE);
    public static final String XML_ACCESSOR = "javax.xml.bind.annotation.XmlAccessorType";
    public static final JavaType XML_ACCESSOR_TYPE = new JavaType(XML_ACCESSOR); 
    public static final JavaType XML_ACCESS_TYPE = new JavaType("javax.xml.bind.annotation.XmlAccessType");
    public static final String XML_TRANSIENT = "javax.xml.bind.annotation.XmlTransient";
    public static final String XML_ATTRIBUTE = "javax.xml.bind.annotation.XmlAttribute";
    public static final String XML_ELEMENT = "javax.xml.bind.annotation.XmlElement";
    public static final String XML_ANY_ELEMENT = "javax.xml.bind.annotation.XmlAnyElement";
    public static final String XML_ELEMENT_WRAPPER = "javax.xml.bind.annotation.XmlElementWrapper";
    public static final String XML_ID = "javax.xml.bind.annotation.XmlID";
    public static final String XML_IDREF = "javax.xml.bind.annotation.XmlIDREF";
    public static final String XML_LIST = "javax.xml.bind.annotation.XmlList";
}
