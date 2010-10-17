package org.talframework.objexj.generator.roo.generator.state;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.talframework.objexj.generator.roo.fields.FieldVisitor;
import org.talframework.objexj.generator.roo.fields.ListReferenceField;
import org.talframework.objexj.generator.roo.fields.MapReferenceField;
import org.talframework.objexj.generator.roo.fields.ObjexField;
import org.talframework.objexj.generator.roo.fields.SimpleField;
import org.talframework.objexj.generator.roo.fields.SimpleReferenceField;
import org.talframework.objexj.generator.roo.generator.BaseGenerator;
import org.talframework.objexj.generator.roo.utils.MethodMetadataWrapper;
import org.talframework.objexj.generator.roo.utils.TypeConstants;

/**
 * Generates out the getter/setters for the state bean
 * 
 * @author Tom Spencer
 */
public class StateFieldAccessorGenerator extends BaseGenerator implements FieldVisitor {
    
    /** Holds the methods generated as we visit the properties */
    private List<MethodMetadataWrapper> methods;
    
    public StateFieldAccessorGenerator(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        super(builder, typeDetails, typeId);
    }
    
    /**
     * Call to generate the accessor/mutator methods for the given
     * fields.
     * 
     * @param fields The fields
     */
    public void generate(List<ObjexField> fields) {
        methods = new ArrayList<MethodMetadataWrapper>();
        
        Iterator<ObjexField> it = fields.iterator();
        while( it.hasNext() ) {
            ObjexField field = it.next();
            if( field.isNaturalBeanField() ) field.accept(this);
        }
        
        Iterator<MethodMetadataWrapper> it2 = methods.iterator();
        while( it2.hasNext() ) {
            it2.next().addMetadata(builder, typeDetails, typeId);
        }
    }
    
    public void visitSimple(SimpleField prop) {
        MethodMetadataWrapper getter = new MethodMetadataWrapper(new JavaSymbolName(prop.getBeanGetterMethodName()), prop.getBeanType());
        if( !prop.isLarge() ) getter.addAnnotation(TypeConstants.XML_ATTRIBUTE);
        getter.addBody("return " + prop.getBeanName().getSymbolName() + ";");
        methods.add(getter);
        
        MethodMetadataWrapper setter = new MethodMetadataWrapper(new JavaSymbolName(prop.getBeanSetterMethodName()), JavaType.VOID_PRIMITIVE);
        setter.addParameter(new JavaSymbolName("val"), prop.getBeanType(), null);
        setter.addBody(prop.getBeanName().getSymbolName() + " = val;");
        methods.add(setter);
    }
    
    public void visitReference(SimpleReferenceField prop) {
        MethodMetadataWrapper getter = new MethodMetadataWrapper(new JavaSymbolName(prop.getBeanGetterMethodName()), prop.getBeanType());
        getter.addAnnotation(TypeConstants.XML_ATTRIBUTE);
        getter.addBody("return " + prop.getBeanName().getSymbolName() + ";");
        methods.add(getter);
        
        MethodMetadataWrapper setter = new MethodMetadataWrapper(new JavaSymbolName(prop.getBeanSetterMethodName()), JavaType.VOID_PRIMITIVE);
        setter.addParameter(new JavaSymbolName("val"), prop.getBeanType(), null);
        setter.addBody(prop.getBeanName().getSymbolName() + " = val;");
        methods.add(setter);
    }
    
    public void visitList(ListReferenceField prop) {
        MethodMetadataWrapper getter = new MethodMetadataWrapper(new JavaSymbolName(prop.getBeanGetterMethodName()), prop.getBeanType());
        getter.addAnnotation(TypeConstants.XML_LIST);
        getter.addBody("return " + prop.getBeanName().getSymbolName() + ";");
        methods.add(getter);
        
        MethodMetadataWrapper setter = new MethodMetadataWrapper(new JavaSymbolName(prop.getBeanSetterMethodName()), JavaType.VOID_PRIMITIVE);
        setter.addParameter(new JavaSymbolName("val"), prop.getBeanType(), null);
        setter.addBody(prop.getBeanName().getSymbolName() + " = val;");
        methods.add(setter);
    }
    
    public void visitMap(MapReferenceField prop) {
        MethodMetadataWrapper getter = new MethodMetadataWrapper(new JavaSymbolName(prop.getBeanGetterMethodName()), prop.getBeanType());
        getter.addBody("return " + prop.getBeanName().getSymbolName() + ";");
        methods.add(getter);
        
        MethodMetadataWrapper setter = new MethodMetadataWrapper(new JavaSymbolName(prop.getBeanSetterMethodName()), JavaType.VOID_PRIMITIVE);
        setter.addParameter(new JavaSymbolName("val"), prop.getBeanType(), null);
        setter.addBody(prop.getBeanName().getSymbolName() + " = val;");
        methods.add(setter);
    }
}
