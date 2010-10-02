package org.talframework.objexj.generator.roo.generator.object;

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
 * This class generators out the ObjexObj property accessors
 * for all fields for the object
 *
 * @author Tom Spencer
 */
public final class ObjexObjFieldAccessorGenerator extends BaseGenerator implements FieldVisitor {
    
    /** Holds the methods generated as we visit the properties */
    private List<MethodMetadataWrapper> methods;
    
    private boolean includeSimpleUtils = false;
    private boolean includeReferenceUtils = false;
    private boolean includeListUtils = false;
    private boolean includeMapUtils = false;

    public ObjexObjFieldAccessorGenerator(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
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
        includeSimpleUtils = false;
        includeReferenceUtils = false;
        includeListUtils = false;
        includeMapUtils = false;
        
        Iterator<ObjexField> it = fields.iterator();
        while( it.hasNext() ) {
            it.next().accept(this);
        }
        
        if( includeSimpleUtils ) builder.getImportRegistrationResolver().addImport(new JavaType(TypeConstants.SIMPLEUTILS));
        if( includeReferenceUtils ) builder.getImportRegistrationResolver().addImport(new JavaType(TypeConstants.REFUTILS));
        if( includeListUtils ) builder.getImportRegistrationResolver().addImport(new JavaType(TypeConstants.REFLISTUTILS));
        if( includeMapUtils ) builder.getImportRegistrationResolver().addImport(new JavaType(TypeConstants.REFMAPUTILS));
        
        Iterator<MethodMetadataWrapper> it2 = methods.iterator();
        while( it2.hasNext() ) {
            it2.next().addMetadata(builder, typeDetails, typeId);
        }
    }

    /**
     * Simply generates a get/set method pair
     */
    public void visitSimple(SimpleField prop) {
        includeSimpleUtils = true;
        
        // a. Getter
        MethodMetadataWrapper getter = startMethod(prop.getGetterMethodName(), prop.getType());
        getter.addBody(getSimpleType(prop.getBeanType()) + " rawValue = bean." + prop.getBeanGetterMethodName() + "();");
        getter.addBody(getValueFromRawValue(prop));
        getter.addBody("return val;");
        methods.add(getter);
        
        // b. Setter
        MethodMetadataWrapper setter = startSetter(prop.getSetterMethodName(), prop.getType());
        setter.addBody(getRawValueFromValue(prop));
        setter.addBody("bean." + prop.getBeanSetterMethodName() + "(SimpleFieldUtils.setSimple(this, bean, rawValue, bean." + prop.getBeanGetterMethodName() + "()));");
        methods.add(setter);
    }
    
    public void visitReference(SimpleReferenceField prop) {
        includeReferenceUtils = true;
        
        // a. Getter
        MethodMetadataWrapper getter = startMethod(prop.getGetterMethodName(), prop.getType());
        getter.addBody("return ReferenceFieldUtils.getReference(this, " + getSimpleType(prop.getType()) + ".class, bean." + prop.getBeanGetterMethodName() + "());");
        methods.add(getter);
        
        // b. Setter
        MethodMetadataWrapper setter = startSetter(prop.getSetterMethodName(), prop.getType());
        setter.addBody("bean." + prop.getBeanSetterMethodName() + "(ReferenceFieldUtils.setReference(this, bean, bean." + prop.getBeanGetterMethodName() + "(), val, " + prop.isOwned() + ", \"" + prop.getNewType() + "\"));");
        methods.add(setter);
        
        if( prop.isOwned() ) {
            // c. Create
            MethodMetadataWrapper creator = startMethod(prop.getCreateMethodName(), prop.getType());
            if( prop.getNewType() == null ) creator.addParameter(new JavaSymbolName("type"), JavaType.STRING_OBJECT, null);
            else creator.addBody("String type = \"" + prop.getNewType() + "\";");
            creator.addBody(getSimpleType(prop.getType()) + " val = ReferenceFieldUtils.createReference(this, bean, " + getSimpleType(prop.getType()) + ".class, type);");
            creator.addBody("bean." + prop.getBeanSetterMethodName() + "(ReferenceFieldUtils.setReference(this, bean, bean." + prop.getBeanGetterMethodName() + "(), val, " + prop.isOwned() + ", \"" + prop.getNewType() + "\"));");
            creator.addBody("return val;");
            methods.add(creator);
        }
    }
    
    public void visitList(ListReferenceField prop) {
        includeListUtils = true;
        
        // a. Getter
        MethodMetadataWrapper method = startMethod(prop.getGetterMethodName(), prop.getType());
        method.addBody("return ReferenceListFieldUtils.getList(this, " + getSimpleType(prop.getMemberType()) + ".class, bean." + prop.getBeanGetterMethodName() + "());");
        methods.add(method);
        
        // b. Setter
        method = startSetter(prop.getSetterMethodName(), prop.getType());
        method.addBody("bean." + prop.getBeanSetterMethodName() + "(ReferenceListFieldUtils.setList(this, bean, bean." + prop.getBeanGetterMethodName() + "(), val, " + prop.isOwned() + "));");
        methods.add(method);
        
        // c. Item by Index
        method = startMethod(prop.getGetByIndexMethodName(), prop.getMemberType());
        method.addParameter(new JavaSymbolName("index"), JavaType.INT_PRIMITIVE, null);
        method.addBody("return ReferenceListFieldUtils.getElementByIndex(this, " + getSimpleType(prop.getMemberType()) + ".class, bean." + prop.getBeanGetterMethodName() + "(), index);");
        methods.add(method);
        
        // d. Create
        if( prop.isOwned() ) {
            method = startMethod(prop.getCreateMethodName(), prop.getMemberType());
            if( prop.getNewType() == null ) method.addParameter(new JavaSymbolName("type"), JavaType.STRING_OBJECT, null);
            else method.addBody("String type = \"" + prop.getNewType() + "\";");
            method.addBody(getSimpleType(prop.getMemberType()) + " val = ReferenceListFieldUtils.createElement(this, bean, " + getSimpleType(prop.getMemberType()) + ".class, type);");
            method.addBody("bean." + prop.getBeanSetterMethodName() + "(ReferenceListFieldUtils.addElement(this, bean, bean." + prop.getBeanGetterMethodName() + "(), val));");
            method.addBody("return val;");
            methods.add(method);
        }

        // e. Add
        else {
            method = startMethod(prop.getAddMethodName(), JavaType.VOID_PRIMITIVE);
            method.addParameter(new JavaSymbolName("val"), prop.getMemberType(), null);
            method.addBody("bean." + prop.getBeanSetterMethodName() + "(ReferenceListFieldUtils.addElement(this, bean, bean." + prop.getBeanGetterMethodName() + "(), val);");
            methods.add(method);
        }
        
        // f. Remove Index
        method = startMethod(prop.getRemoveByIndexMethodName(), JavaType.VOID_PRIMITIVE);
        method.addParameter(new JavaSymbolName("index"), JavaType.INT_PRIMITIVE, null);
        method.addBody("bean." + prop.getBeanSetterMethodName() + "(ReferenceListFieldUtils.removeElementByIndex(this, bean, bean." + prop.getBeanGetterMethodName() + "(), " + prop.isOwned() + ", index));");
        methods.add(method);
        
        // g. Remove ID
        method = startMethod(prop.getRemoveByIdMethodName(), JavaType.VOID_PRIMITIVE);
        method.addParameter("id", "java.lang.Object", null);
        method.addBody("bean." + prop.getBeanSetterMethodName() + "(ReferenceListFieldUtils.removeElementById(this, bean, bean." + prop.getBeanGetterMethodName() + "(), " + prop.isOwned() + ", id));");
        methods.add(method);
        
        // h. Remove All
        method = startMethod(prop.getRemoveAllMethodName(), JavaType.VOID_PRIMITIVE);
        method.addBody("bean." + prop.getBeanSetterMethodName() + "(ReferenceListFieldUtils.removeAll(this, bean, bean." + prop.getBeanGetterMethodName() + "(), " + prop.isOwned() + "));");
        methods.add(method);
    }
    
    public void visitMap(MapReferenceField prop) {
        includeListUtils = true;
        
        // a. Getter
        MethodMetadataWrapper method = startMethod(prop.getGetterMethodName(), prop.getType());
        method.addBody("return ReferenceMapFieldUtils.getMap(this, " + getSimpleType(prop.getMemberType()) + ".class, bean." + prop.getBeanGetterMethodName() + "());");
        methods.add(method);
        
        // b. Setter
        method = startSetter(prop.getSetterMethodName(), prop.getType());
        method.addBody("bean." + prop.getBeanSetterMethodName() + "(ReferenceMapFieldUtils.setMap(this, bean, " + prop.isOwned() + ", bean." + prop.getBeanGetterMethodName() + "(), val);");
        methods.add(method);
        
        // c. Item by Key
        method = startMethod(prop.getGetByKeyMethodName(), prop.getMemberType());
        method.addParameter(new JavaSymbolName("key"), JavaType.STRING_OBJECT, null);
        method.addBody("return ReferenceMapFieldUtils.getElementByKey(this, " + getSimpleType(prop.getMemberType()) + ".class, bean." + prop.getBeanGetterMethodName() + "(), key);");
        methods.add(method);
        
        // d. Create
        if( prop.isOwned() ) {
            method = startMethod(prop.getCreateMethodName(), prop.getMemberType());
            method.addParameter(new JavaSymbolName("key"), JavaType.STRING_OBJECT, null);
            if( prop.getNewType() == null ) method.addParameter(new JavaSymbolName("type"), JavaType.STRING_OBJECT, null);
            else method.addBody("String type = \"" + prop.getNewType() + "\";");
            method.addBody(getSimpleType(prop.getMemberType()) + " val = ReferenceMapFieldUtils.createElement(this, bean, " + getSimpleType(prop.getMemberType()) + ".class, type);");
            method.addBody("bean." + prop.getBeanSetterMethodName() + "(ReferenceMapFieldUtils.addElement(this, bean, bean." + prop.getBeanGetterMethodName() + "(), key, val);");
            method.addBody("return val;");
            methods.add(method);
        }

        // e. Add
        else {
            method = startMethod(prop.getAddMethodName(), JavaType.VOID_PRIMITIVE);
            method.addParameter(new JavaSymbolName("key"), JavaType.STRING_OBJECT, null);
            method.addParameter(new JavaSymbolName("val"), prop.getMemberType(), null);
            method.addBody("bean." + prop.getBeanSetterMethodName() + "(ReferenceMapFieldUtils.addElement(this, bean, bean." + prop.getBeanGetterMethodName() + "(), key, val);");
            methods.add(method);
        }
        
        // f. Remove Index
        method = startMethod(prop.getRemoveByKeyMethodName(), JavaType.VOID_PRIMITIVE);
        method.addParameter(new JavaSymbolName("key"), JavaType.STRING_OBJECT, null);
        method.addBody("bean." + prop.getBeanSetterMethodName() + "(ReferenceMapFieldUtils.removeElementByKey(this, bean, bean." + prop.getBeanGetterMethodName() + "(), " + prop.isOwned() + ", key));");
        methods.add(method);
        
        // g. Remove ID
        method = startMethod(prop.getRemoveByIdMethodName(), JavaType.VOID_PRIMITIVE);
        method.addParameter("id", "java.lang.Object", null);
        method.addBody("bean." + prop.getBeanSetterMethodName() + "(ReferenceMapFieldUtils.removeElementById(this, bean, bean." + prop.getBeanGetterMethodName() + "(), " + prop.isOwned() + ", id));");
        methods.add(method);
        
        // h. Remove All
        method = startMethod(prop.getRemoveAllMethodName(), JavaType.VOID_PRIMITIVE);
        method.addBody("bean." + prop.getBeanSetterMethodName() + "(ReferenceMapFieldUtils.removeAll(this, bean, bean." + prop.getBeanGetterMethodName() + "(), " + prop.isOwned() + "));");
        methods.add(method);
    }
    
    //////////////////////////////////
    // Helpers
    
    /**
     * Helper to start and return a method that is called name
     * and returns the given type.
     */
    private MethodMetadataWrapper startMethod(String name, JavaType type) {
        JavaSymbolName javaName = new JavaSymbolName(name);
        return new MethodMetadataWrapper(javaName, type);
    }
    
    /**
     * Helper to start and return a setter method
     */
    private MethodMetadataWrapper startSetter(String name, JavaType type) {
        JavaSymbolName javaName = new JavaSymbolName(name);
        MethodMetadataWrapper method = new MethodMetadataWrapper(javaName, JavaType.VOID_PRIMITIVE);
        method.addParameter(new JavaSymbolName("val"), type, null);
        return method;
    }
    
    /**
     * Helper to return the line that converts a raw value into the return
     */
    private String getValueFromRawValue(ObjexField prop) {
        String ret = null;
        
        // a. Not transformed & primitive
        if( !prop.isTransformed() && !prop.getType().isPrimitive() ) ret = getSimpleType(prop.getType()) + " val = rawValue;";
        
        // b. Not transformed and not primitive
        else if( !prop.isTransformed() ) ret = getSimpleType(prop.getType()) + " val = cloneValue(rawValue);";
        
        // b. Exposed type natively handles
        else if( prop.getTransformer() == null ) ret = getSimpleType(prop.getType()) + " val = new " + prop.getTypeName() + "(rawValue);";
        
        // c. Function
        else ret = getSimpleType(prop.getType()) + " val = " + prop.getTransformer() + "." + prop.getGetTransformer() + ";";
        
        return ret;
    }
    
    /**
     * Helper to return the line that converts to value from raw value
     */
    private String getRawValueFromValue(ObjexField prop) {
        String ret = getSimpleType(prop.getBeanType()) + " rawValue = ";
        
        // a. Not transformed
        if( !prop.isTransformed() ) ret += "val;";
        
        // b. Exposed type natively handles
        else if( prop.getTransformer() == null ) ret += "val." + prop.getSetTransformer() + ";";
        
        // c. Function
        else ret += prop.getTransformer() + "." + prop.getSetTransformer() + ";";
        
        return ret;
    }

    /**
     * @return The simple type name
     */
    private String getSimpleType(JavaType type) {
        return type.getNameIncludingTypeParameters(false, builder.getImportRegistrationResolver());
    }
}
