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
    
    private boolean includeObjexObj = false;
    private boolean includeStateUtils = true;
    private boolean includeObjectUtils = false;
    private boolean includeIterator = false;
    private boolean includeList = false;
    private boolean includeMap = false;

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
        includeObjexObj = false;
        includeStateUtils = true;
        includeObjectUtils = false;
        includeIterator = false;
        includeList = false;
        includeMap = false;
        
        Iterator<ObjexField> it = fields.iterator();
        while( it.hasNext() ) {
            it.next().accept(this);
        }
        
        if( includeObjexObj ) builder.getImportRegistrationResolver().addImport(new JavaType(TypeConstants.OBJEXOBJ));
        if( includeStateUtils ) builder.getImportRegistrationResolver().addImport(new JavaType(TypeConstants.BEANUTILS));
        if( includeObjectUtils ) builder.getImportRegistrationResolver().addImport(new JavaType(TypeConstants.OBJUTILS));
        if( includeIterator ) builder.getImportRegistrationResolver().addImport(new JavaType("java.util.Iterator"));
        if( includeList ) builder.getImportRegistrationResolver().addImport(new JavaType("java.util.ArrayList"));
        if( includeList ) builder.getImportRegistrationResolver().addImport(new JavaType("java.util.List"));
        if( includeMap ) builder.getImportRegistrationResolver().addImport(new JavaType("java.util.HashMap"));
        if( includeMap ) builder.getImportRegistrationResolver().addImport(new JavaType("java.util.Map"));
        
        Iterator<MethodMetadataWrapper> it2 = methods.iterator();
        while( it2.hasNext() ) {
            it2.next().addMetadata(builder, typeDetails, typeId);
        }
    }

    public void visitSimple(SimpleField prop) {
        generateSimpleAccessor(prop);
        generateSimpleMutator(prop);
    }
    
    public void visitReference(SimpleReferenceField prop) {
        includeObjectUtils = true;
        
        generateSimpleAccessor(prop);
        
        if( prop.isOwned() ) {
            includeObjexObj = true;
            generateRefCreate(prop);
            generateRefRemove(prop);
            generateRefSetter(prop);
        }
        else {
            generateSimpleMutator(prop);
        }
    }
    
    public void visitList(ListReferenceField prop) {
        includeObjectUtils = true;
        includeIterator = true;
        includeList = true;
        
        generateSimpleAccessor(prop);
        
        generateListItemGet(prop);
        generateListRemoveById(prop);
        generateListRemoveByIndex(prop);
        generateListRemoveAll(prop);
        
        if( prop.isOwned() ) {
            includeObjexObj = true;
            generateListCreate(prop);
        }
        else {
            generateListAdd(prop);
            generateSimpleMutator(prop);
        }
    }
    
    public void visitMap(MapReferenceField prop) {
        includeObjectUtils = true;
        includeIterator = true;
        includeMap = true;
        
        generateSimpleAccessor(prop);
        
        generateMapItemGet(prop);
        generateMapRemoveById(prop);
        generateMapRemoveByKey(prop);
        generateMapRemoveAll(prop);
        
        if( prop.isOwned() ) {
            includeObjexObj = true;
            generateMapCreate(prop);
        }
        else {
            // TODO: Add
            generateSimpleMutator(prop);
        }
    }
    
    //////////////////////////////////
    // Helpers
    
    /**
     * Helper to generate a basic getter method
     */
    private void generateSimpleAccessor(ObjexField prop) {
        MethodMetadataWrapper getter = new MethodMetadataWrapper(new JavaSymbolName(prop.getGetterName()), prop.getType());
        
        getter.addBody(prop.getBeanTypeName() + " rawValue = bean." + prop.getBeanGetterName() + "();");
        
        // Convert to bean's type
        // a. Not transformed & primitive
        if( !prop.isTransformed() && !prop.getType().isPrimitive() ) getter.addBody("return rawValue;");
        // b. Not transformed and not primitive
        else if( !prop.isTransformed() ) getter.addBody("return cloneValue(rawValue);");
        // b. Exposed type natively handles
        else if( prop.getTransformer() == null ) getter.addBody("return new " + prop.getTypeName() + "(rawValue);");
        // c. Function
        else getter.addBody("return " + prop.getTransformer() + "." + prop.getGetTransformer() + ";");
        
        methods.add(getter);
    }
    
    /**
     * Helper to generate a basic setter method
     */
    private void generateSimpleMutator(ObjexField prop) {
        MethodMetadataWrapper setter = new MethodMetadataWrapper(new JavaSymbolName(prop.getSetterName()), JavaType.VOID_PRIMITIVE);
        setter.addParameter(new JavaSymbolName("val"), prop.getType(), null);

        // Convert to bean's type
        // a. Not transformed
        if( !prop.isTransformed() ) setter.addBody(prop.getBeanTypeName() + " rawValue = val;");
        // b. Exposed type natively handles
        else if( prop.getTransformer() == null ) setter.addBody(prop.getBeanTypeName() + " rawValue = val." + prop.getSetTransformer() + ";");
        // c. Function
        else setter.addBody(prop.getBeanTypeName() + " rawValue = " + prop.getTransformer() + "." + prop.getSetTransformer() + ";");
        
        setter.addBody("if( !StateBeanUtils.hasChanged(bean." + prop.getBeanGetterName() + "(), rawValue) ) return;");
        setter.addBody("ensureUpdateable(bean);");
        setter.addBody("bean." + prop.getBeanSetterName() + "(rawValue);");
        
        // TODO: Triggers
        
        methods.add(setter);
    }
    
    /**
     * Generates a method to create a new child
     */
    private void generateRefCreate(SimpleReferenceField prop) {
        MethodMetadataWrapper method = new MethodMetadataWrapper(new JavaSymbolName(prop.getCreatorName()), prop.getType());
        if( prop.getNewType() == null ) method.addParameter("type", String.class.getName(), null);
        
        method.addBody("ensureUpdateable(bean);");
        
        // Remove if already present
        method.addBody("if( bean." + prop.getBeanGetterName() + "() != null ) ObjectUtils.removeObject(this, bean, bean." + prop.getBeanGetterName() + "());");
        
        if( prop.getNewType() == null ) method.addBody("ObjexObj val = ObjectUtils.createObject(this, bean, type);");
        else method.addBody("ObjexObj val = ObjectUtils.createObject(this, bean, \"" + prop.getNewType() + "\");");
        method.addBody("bean." + prop.getBeanSetterName() + "(val.getId().toString());");
        method.addBody("return val.getBehaviour(" + prop.getType().getSimpleTypeName() + ".class);");
        
        methods.add(method);
    }
    
    /**
     * Generates a create member method on a list ref field
     */
    private void generateListCreate(ListReferenceField prop) {
        String newType = prop.getNewType();
        
        MethodMetadataWrapper method = new MethodMetadataWrapper(prop.getCreatorName(), prop.getMemberTypeName());
        if( newType == null ) method.addParameter("type", String.class.getName(), null);

        method.addBody("checkUpdateable();");
        
        if( newType == null ) method.addBody("ObjexObj val = ObjectUtils.createObject(this, bean, type);");
        else method.addBody("ObjexObj val = ObjectUtils.createObject(this, bean, \"" + newType + "\");");
        
        method.addBody("ensureUpdateable(bean);");
        method.addBody("List<String> refs = bean." + prop.getBeanGetterName() + "();");
        method.addBody("if( refs == null ) {");
        method.addBody("\trefs = new ArrayList<String>();");
        method.addBody("\tbean." + prop.getBeanSetterName() + "(refs);");
        method.addBody("}");
        method.addBody("refs.add(val.getId().toString());");
        
        method.addBody("return val.getBehaviour(" + prop.getMemberType().getSimpleTypeName() + ".class);");
        methods.add(method);
    }
    
    /**
     * Generates a create member method on a list ref field
     */
    private void generateMapCreate(MapReferenceField prop) {
        String newType = prop.getNewType();
        
        MethodMetadataWrapper method = new MethodMetadataWrapper(prop.getCreatorName(), prop.getMemberTypeName());
        method.addParameter("key", String.class.getName(), null);
        if( newType == null ) method.addParameter("type", String.class.getName(), null);

        method.addBody("checkUpdateable();");
        
        if( newType == null ) method.addBody("ObjexObj val = ObjectUtils.createObject(this, bean, type);");
        else method.addBody("ObjexObj val = ObjectUtils.createObject(this, bean, \"" + newType + "\");");
        
        method.addBody("ensureUpdateable(bean);");
        method.addBody("Map<String, String> refs = bean." + prop.getBeanGetterName() + "();");
        method.addBody("if( refs == null ) {");
        method.addBody("\trefs = new HashMap<String, String>();");
        method.addBody("\tbean." + prop.getBeanSetterName() + "(refs);");
        method.addBody("}");
        method.addBody("refs.put(key, val.getId().toString());");
        
        method.addBody("return val.getBehaviour(" + prop.getMemberType().getSimpleTypeName() + ".class);");
        methods.add(method);
    }
    
    /**
     * Generates the set method on an owned reference property.
     * This creates the reference if required and copies all
     * fields from input into the object.
     */
    private void generateRefSetter(SimpleReferenceField prop) {
        // TODO: Generate the reference setter
    }
    
    /**
     * Generates a getter by ID on a ref or map property
     */
    private void generateListItemGet(ListReferenceField prop) {
        JavaSymbolName name = new JavaSymbolName("get" + prop.getItemReference() + "ByIndex");
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, prop.getMemberType());
        method.addParameter(new JavaSymbolName("index"), JavaType.INT_PRIMITIVE, null);
        
        method.addBody("List<String> refs = bean." + prop.getBeanGetterName() + "();");
        method.addBody("if( refs != null && index >= 0 && index < refs.size() ) return ObjectUtils.getObject(this, refs.get(index), " + prop.getMemberTypeName() + ".class);");
        method.addBody("return null;");
        
        methods.add(method);
    }
    
    /**
     * Generates a getter by key on a map property
     */
    private void generateMapItemGet(MapReferenceField prop) {
        JavaSymbolName name = new JavaSymbolName("get" + prop.getItemReference() + "ByIndex");
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, prop.getMemberType());
        method.addParameter(new JavaSymbolName("key"), JavaType.STRING_OBJECT, null);
        
        method.addBody("Map<String, String> refs = bean." + prop.getBeanGetterName() + "();");
        method.addBody("if( refs == null || !refs.containsKey(key) ) return null;");
        method.addBody("else return ObjectUtils.getObject(this, refs.get(key), " + prop.getMemberTypeName() + ".class");
        
        methods.add(method);
    }
    
    /**
     * Generates a getter by ID on a ref or map property
     */
    private void generateListAdd(ListReferenceField prop) {
        JavaSymbolName name = new JavaSymbolName("add" + prop.getItemReference());
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        method.addParameter(new JavaSymbolName("val"), prop.getMemberType(), null);
        method.addBody("checkUpdateable();");
        method.addBody("String ref = ObjectUtils.getObjexId(val);");
        
        method.addBody("ensureUpdateable(bean);");
        method.addBody("List<String> refs = bean." + prop.getBeanGetterName() + "();");
        method.addBody("if( refs == null ) {");
        method.addBody("\trefs = new ArrayList<String>();");
        method.addBody("\tbean." + prop.getBeanSetterName() + "(refs);");
        method.addBody("}");
        method.addBody("refs.add(ref);");
        
        methods.add(method);
    }
    
    /**
     * Generates a method to remove a owned reference property
     */
    private void generateRefRemove(SimpleReferenceField prop) {
        JavaSymbolName name = new JavaSymbolName("remove" + prop.getName().getSymbolNameCapitalisedFirstLetter());
        
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        
        method.addBody("ensureUpdateable(bean);");
        method.addBody("if( bean." + prop.getBeanGetterName() + "() != null ) ObjectUtils.removeObject(this, bean, bean." + prop.getBeanGetterName() + "());");
        
        methods.add(method);
    }
    
    private void generateListRemoveById(ListReferenceField prop) {
        JavaSymbolName name = new JavaSymbolName("remove" + prop.getItemReference() + "ById");
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        method.addParameter("id", "java.lang.Object", null);
        method.addBody("checkUpdateable();");
        
        method.addBody("String ref = ObjectUtils.getObjectRef(this, id);");
        method.addBody("List<String> refs = bean." + prop.getBeanGetterName() + "();");
        method.addBody("if( refs == null || refs.size() == 0 ) return;");
        if( prop.isOwned() ) method.addBody("int size = refs.size();");
        
        method.addBody("Iterator<String> it = refs.iterator();");
        method.addBody("while( it.hasNext() ) {");
        method.addBody("\tif( ref.equals(it.next()) ) {");
        method.addBody("\t\tensureUpdateable(bean);");
        method.addBody("\t\tit.remove();");
        method.addBody("\t}");
        method.addBody("}");
        
        if( prop.isOwned() ) {
            method.addBody("if( refs.size() == size ) return;");
            method.addBody("ObjectUtils.removeObject(this, bean, ref);");
        }
        
        methods.add(method);
    }
    
    private void generateMapRemoveById(MapReferenceField prop) {
        JavaSymbolName name = new JavaSymbolName("remove" + prop.getItemReference() + "ById");
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        method.addParameter("id", "java.lang.Object", null);
        method.addBody("checkUpdateable();");
        
        method.addBody("String ref = ObjectUtils.getObjectRef(this, id);");
        method.addBody("Map<String, String> refs = bean." + prop.getBeanGetterName() + "();");
        method.addBody("if( refs == null || refs.size() == 0 ) return;");
        if( prop.isOwned() ) method.addBody("int size = refs.size();");
        
        method.addBody("Iterator<String> it = refs.keySet().iterator();");
        method.addBody("while( it.hasNext() ) {");
        method.addBody("\tif( ref.equals(it.next()) ) {");
        method.addBody("\t\tensureUpdateable(bean);");
        method.addBody("\t\tit.remove();");
        method.addBody("\t}");
        method.addBody("}");
        
        if( prop.isOwned() ) {
            method.addBody("if( refs.size() == size ) return;");
            method.addBody("ObjectUtils.removeObject(this, bean, ref);");
        }
        
        methods.add(method);
    }
    
    private void generateListRemoveByIndex(ListReferenceField prop) {
        JavaSymbolName name = new JavaSymbolName("remove" + prop.getItemReference() + "ByIndex");
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        method.addParameter(new JavaSymbolName("index"), JavaType.INT_PRIMITIVE, null);
        method.addBody("checkUpdateable();");
        
        method.addBody("List<String> refs = bean." + prop.getBeanGetterName() + "();");
        method.addBody("if( refs == null || index < 0 || index >= refs.size() ) return;");
        
        if( prop.isOwned() ) method.addBody("String ref = refs.get(index);");
        method.addBody("ensureUpdateable(bean);");
        method.addBody("refs.remove(index);");
        if( prop.isOwned() ) method.addBody("ObjectUtils.removeObject(this, bean, ref);");
        
        methods.add(method);
    }
    
    private void generateMapRemoveByKey(MapReferenceField prop) {
        JavaSymbolName name = new JavaSymbolName("remove" + prop.getItemReference() + "ByIndex");
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        method.addParameter(new JavaSymbolName("ref"), JavaType.INT_PRIMITIVE, null);
        method.addBody("checkUpdateable();");
        
        method.addBody("Map<String, String> refs = bean." + prop.getBeanGetterName() + "();");
        method.addBody("if( refs == null || !refs.containsKey(ref) ) ) return;");
        
        method.addBody("ensureUpdateable(bean);");
        method.addBody("refs.remove(ref);");
        if( prop.isOwned() ) method.addBody("ObjectUtils.removeObject(this, bean, ref);");
        
        methods.add(method);
    }
    
    private void generateListRemoveAll(ListReferenceField prop) {
        JavaSymbolName name = new JavaSymbolName(prop.getRemoveAllName());
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        
        method.addBody("List<String> refs = bean." + prop.getBeanGetterName() + "();");
        method.addBody("if( refs == null || refs.size() == 0 ) return;");
        method.addBody("checkUpdateable();");
        
        // Remove all refs from container if owned
        if( prop.isOwned() ) {
            method.addBody("Iterator<String> it = refs.iterator();");
            method.addBody("while( it.hasNext() ) {");
            method.addBody("\tString ref = it.next();");
            method.addBody("\tObjectUtils.removeObject(this, bean, ref);");
            method.addBody("\tit.remove();");
            method.addBody("}");
        }
        method.addBody("ensureUpdateable(bean);");
        method.addBody("bean." + prop.getBeanSetterName() + "(null);");
        
        methods.add(method);
    }
    
    private void generateMapRemoveAll(MapReferenceField prop) {
        JavaSymbolName name = new JavaSymbolName(prop.getRemoveAllName());
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        
        method.addBody("Map<String, String> refs = bean." + prop.getBeanGetterName() + "();");
        method.addBody("if( refs == null || refs.size() == 0 ) return;");
        method.addBody("checkUpdateable();");
        
        // Remove all refs from container if owned
        if( prop.isOwned() ) {
            method.addBody("Iterator<String> it = refs.keySet.iterator();");
            method.addBody("while( it.hasNext() ) {");
            method.addBody("\tString ref = it.next();");
            method.addBody("\tObjectUtils.removeObject(this, bean, ref);");
            method.addBody("}");
        }
        method.addBody("ensureUpdateable(bean);");
        method.addBody("bean." + prop.getBeanSetterName() + "(null);");
        
        methods.add(method);
    }
}
