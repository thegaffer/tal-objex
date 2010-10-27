package org.talframework.objexj.generator.roo.generator.state;

import java.lang.reflect.Modifier;
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
import org.talframework.objexj.generator.roo.utils.PropertyMetadataWrapper;

/**
 * Generates out the getter/setters for the state bean
 * 
 * @author Tom Spencer
 */
public class StateFieldAccessorGenerator extends BaseGenerator implements FieldVisitor {
    
    /** Holds the methods generated as we visit the properties */
    private List<MethodMetadataWrapper> methods;
    /** The index of the current field (for correct set/changed setting) */
    private int index;
    
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
        
        int size = fields.size();
        if( size > 63 ) throw new IllegalArgumentException("Too many fields on object, limit is 64");
        
        Iterator<ObjexField> it = fields.iterator();
        while( it.hasNext() ) {
            ObjexField field = it.next();
            if( field.isNaturalBeanField() ) {
                field.accept(this);
                ++index;
            }
        }
        
        addSetField(size);
        addChangedField(size);
        
        Iterator<MethodMetadataWrapper> it2 = methods.iterator();
        while( it2.hasNext() ) {
            it2.next().addMetadata(builder, typeDetails, typeId);
        }
    }
    
    private void addSetField(int size) {
        JavaType type = size > 31 ? JavaType.LONG_PRIMITIVE : JavaType.INT_PRIMITIVE;
        PropertyMetadataWrapper setProp = new PropertyMetadataWrapper(new JavaSymbolName("setFields"), type);
        setProp.addModifier(Modifier.TRANSIENT);
        setProp.addAnnotation("javax.jdo.annotations.NotPersistent");
        setProp.addMetadata(builder, typeDetails, typeId);
    }
    
    private void addChangedField(int size) {
        JavaType type = size > 31 ? JavaType.LONG_PRIMITIVE : JavaType.INT_PRIMITIVE;
        PropertyMetadataWrapper changedProp = new PropertyMetadataWrapper(new JavaSymbolName("setFields"), type);
        changedProp.addModifier(Modifier.TRANSIENT);
        changedProp.addAnnotation("javax.jdo.annotations.NotPersistent");
        changedProp.addMetadata(builder, typeDetails, typeId);
    }
    
    public void visitSimple(SimpleField prop) {
        addMethods(prop);
    }
    
    public void visitReference(SimpleReferenceField prop) {
        addMethods(prop);
    }
    
    public void visitList(ListReferenceField prop) {
        addMethods(prop);
    }
    
    public void visitMap(MapReferenceField prop) {
        addMethods(prop);
    }
    
    private void addMethods(ObjexField prop) {
        MethodMetadataWrapper getter = new MethodMetadataWrapper(new JavaSymbolName(prop.getBeanGetterMethodName()), prop.getBeanType());
        getter.addBody("return " + prop.getBeanName().getSymbolName() + ";");
        methods.add(getter);
        
        MethodMetadataWrapper setter = new MethodMetadataWrapper(new JavaSymbolName(prop.getBeanSetterMethodName()), JavaType.VOID_PRIMITIVE);
        setter.addParameter(new JavaSymbolName("val"), prop.getBeanType(), null);
        setter.addBody("setFields |= " + (1 << index));
        // TODO: Determine if changed
        setter.addBody(prop.getBeanName().getSymbolName() + " = val;");
        methods.add(setter);
        
        MethodMetadataWrapper isSet = new MethodMetadataWrapper(new JavaSymbolName(prop.getBeanIsSetMethodName()), JavaType.BOOLEAN_PRIMITIVE);
        isSet.addBody("return setFields & " + (1 << index));
        methods.add(isSet);
        
        MethodMetadataWrapper isChanged = new MethodMetadataWrapper(new JavaSymbolName(prop.getBeanIsSetMethodName()), JavaType.BOOLEAN_PRIMITIVE);
        isChanged.addBody("return changedFields & " + (1 << index));
        methods.add(isChanged);
    }
}
