package org.talframework.objexj.generator.roo.generator.object;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.DefaultAnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.EnumAttributeValue;
import org.springframework.roo.classpath.details.annotations.StringAttributeValue;
import org.springframework.roo.model.EnumDetails;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.talframework.objexj.generator.roo.annotations.ObjexObjAnnotation;
import org.talframework.objexj.generator.roo.generator.BaseGenerator;
import org.talframework.objexj.generator.roo.utils.ConstructorMetadataWrapper;
import org.talframework.objexj.generator.roo.utils.MethodMetadataWrapper;
import org.talframework.objexj.generator.roo.utils.PropertyMetadataWrapper;
import org.talframework.objexj.generator.roo.utils.TypeConstants;
import org.talframework.objexj.generator.roo.utils.TypeDetailsUtil;

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
        
        // Make the object a JAXB one
        if( TypeDetailsUtil.getAnnotation(typeDetails, TypeConstants.XML_ROOT) == null ) {
            List<AnnotationAttributeValue<?>> values = new ArrayList<AnnotationAttributeValue<?>>();
            values.add(new StringAttributeValue(new JavaSymbolName("name"), annotationValues.getObjexType()));
            builder.addTypeAnnotation(new DefaultAnnotationMetadata(TypeConstants.XML_TYPE_TYPE, values));
            
            values = new ArrayList<AnnotationAttributeValue<?>>();
            values.add(new EnumAttributeValue(new JavaSymbolName("value"), new EnumDetails(TypeConstants.XML_ACCESS_TYPE, new JavaSymbolName("NONE"))));
            builder.addTypeAnnotation(new DefaultAnnotationMetadata(TypeConstants.XML_ACCESSOR_TYPE, values));
        }

        addNoArgConstructor();
        addGetType();
        addStateAccessor();
    }
    
    private void addNoArgConstructor() {
        ConstructorMetadataWrapper cons = new ConstructorMetadataWrapper();
        cons.addBody("throw new IllegalAccessError(\"Cannot construct ObjexObj directly, must use the container\");");
        cons.addMetadata(builder, typeDetails, typeId);
    }
    
    private void addGetType() {
        MethodMetadataWrapper getter = new MethodMetadataWrapper(new JavaSymbolName("getType"), JavaType.STRING_OBJECT);
        // getter.addAnnotation("java.lang.Override");
        getter.addBody("return \"" + annotationValues.getObjexType() + "\";");
        getter.addMetadata(builder, typeDetails, typeId);
    }
    
    /**
     * @return The metadata item for the ID field
     */
    private void addStateAccessor() {
        PropertyMetadataWrapper prop = new PropertyMetadataWrapper(new JavaSymbolName("bean"), annotationValues.getValue());
        prop.addMetadata(builder, typeDetails, typeId);
        
        MethodMetadataWrapper getter = new MethodMetadataWrapper("getStateBean", TypeConstants.OBJEXSTATEBEAN);
        getter.addBody("return bean;");
        getter.addMetadata(builder, typeDetails, typeId);
    }
}
