package org.tpspencer.tal.objexj.roo.fields;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.tpspencer.tal.objexj.roo.ObjexObjMetadata;
import org.tpspencer.tal.objexj.roo.utils.MethodMetadataWrapper;

/**
 * This helper class aids the {@link ObjexObjMetadata} class to
 * output relevant getter/setter for a collection (list or map)
 * property.
 * 
 * @author Tom Spencer
 */
public class CollectionPropHelper extends PropHelper {

    public CollectionPropHelper(ObjexObjProperty prop) {
        super(prop);
        
        addStandardGetter();
        addItemGetter();
        addRefGetter();
        
        if( prop.isSettable() ) {
            if( prop.isOwned() ) addCreate();
            else {
                addAdd();
                addStandardSetter();
                addRefSetter();
            }
            
            addRemoveById();
            if( prop.isListReferenceProp() ) addRemoveByIndex();
            else addRemoveByKey();
            addRemoveAll();
        }
    }
    
    @Override
    public void build(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        builder.getImportRegistrationResolver().addImport(new JavaType("org.tpspencer.tal.objexj.object.ObjectUtils"));
        builder.getImportRegistrationResolver().addImport(new JavaType("org.tpspencer.tal.objexj.ObjexObj"));
        if( prop.isListReferenceProp() ) builder.getImportRegistrationResolver().addImport(new JavaType("java.util.ArrayList"));
        builder.getImportRegistrationResolver().addImport(new JavaType("java.util.Iterator"));
        
        super.build(builder, typeDetails, typeId);
    }
    
    private String getSinglePropName() {
        String baseProp = prop.getName().getSymbolNameCapitalisedFirstLetter();
        String singleProp = baseProp;
        
        if( singleProp.endsWith("ies") && singleProp.length() > 3 ) singleProp = singleProp.substring(0, singleProp.length() - 3) + "y";
        else if( singleProp.endsWith("s") && singleProp.length() > 1 ) singleProp = singleProp.substring(0, singleProp.length() - 1);
        
        return singleProp;
    }
    
    private String getGetItemName() {
        return "get" + getSinglePropName();
    }
    
    private String getCreateName() {
        return "create" + getSinglePropName();
    }
    
    private String getAddName() {
        return "add" + getSinglePropName();
    }
    
    private String getRemoveName() {
        return "remove" + getSinglePropName();
    }
    
    private String getRemoveAllName() {
        return "remove" + prop.getName().getSymbolNameCapitalisedFirstLetter();
    }
    
    private String getRefGetterName() {
        return "get" + getSinglePropName() + "Refs";
    }
    
    private String getRefSetterName() {
        return "set" + getSinglePropName() + "Refs";
    }
    
    /**
     * @return The JavaType to hold the references
     */
    private JavaType getNatualContainerType() {
        List<JavaType> enclosingTypeParams = new ArrayList<JavaType>();
        
        if( prop.isMapReferenceProp() ) {
            // TODO: This is only tested/works for a list currently
            return null;
        }
        else {
            enclosingTypeParams.add(prop.getMemberType());
            return new JavaType("java.util.List", 0, null, null, enclosingTypeParams); // List<MyInterface>
        }
    }
    
    private void addStandardGetter() {
        JavaSymbolName name = new JavaSymbolName(getGetterName());
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, getNatualContainerType());
        if( prop.isMapReferenceProp() ) method.addBody("return ObjectUtils.getObjectMap(bean." + getGetterName() + "());");
        else method.addBody("return ObjectUtils.getObjectList(this, bean." + getGetterName() + "(), " + prop.getMemberType().getSimpleTypeName() + ".class);");
        methods.add(method);
    }
    
    private void addItemGetter() {
        JavaSymbolName name = new JavaSymbolName(getGetItemName() + "ById");
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, prop.getMemberType());
        method.addParameter("id", "java.lang.Object", null);
        
        method.addBody("ObjectUtils.testObjectHeld(bean." + getGetterName() + "(), id);");
        method.addBody("return ObjectUtils.getObject(this, id, " + prop.getMemberType() + ".class);");
        
        methods.add(method);
    }
    
    private void addRefGetter() {
        JavaSymbolName name = new JavaSymbolName(getRefGetterName());
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, prop.getType());
        method.addBody("return bean." + getGetterName() + "();");
        // TODO: This returns the list directly and changes can be made!!!
        methods.add(method);
    }
    
    private void addStandardSetter() {
        // TODO: Future, unsure about this right now!!
    }
    
    private void addRefSetter() {
        JavaSymbolName name = new JavaSymbolName(getRefSetterName());
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        method.addParameter(new JavaSymbolName("val"), prop.getType(), null);
        method.addBody("checkUpdateable();"); // TODO: Or Annotation and Aspect!!??
        method.addBody("bean." + getSetterName() + "(val);");
        methods.add(method);
    }
    
    private void addCreate() {
        String newType = prop.getNewTypeName();
        
        JavaSymbolName name = new JavaSymbolName(getCreateName());
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, prop.getMemberType());
        if( newType == null ) method.addParameter("type", String.class.getName(), null);
        method.addBody("checkUpdateable();"); // TODO: Or Annotation and Aspect!!??
        if( newType == null ) method.addBody("ObjexObj val = ObjectUtils.createObject(this, type);");
        else method.addBody("ObjexObj val = ObjectUtils.createObject(this, \"" + newType + "\");");
        method.addBody("if( bean." + getGetterName() + "() == null ) bean." + getSetterName() + "(new ArrayList<String>());");
        method.addBody("bean." + getGetterName() + "().add(val.getId().toString());");
        method.addBody("return val.getBehaviour(" + prop.getMemberType().getSimpleTypeName() + ".class);");
        methods.add(method);
    }
    
    private void addAdd() {
        JavaSymbolName name = new JavaSymbolName(getAddName());
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        method.addParameter(new JavaSymbolName("val"), prop.getMemberType(), null);
        method.addBody("checkUpdateable();"); // TODO: Or Annotation and Aspect!!??
        method.addBody("String ref = ObjectUtils.getObjexId(val);");
        
        method.addBody("if( bean." + getGetterName() + "() == null ) bean." + getSetterName() + "(new ArrayList<String>());");
        method.addBody("bean." + getGetterName() + "().add(ref);");
        methods.add(method);
    }
    
    private void addRemoveById() {
        JavaSymbolName name = new JavaSymbolName(getRemoveName() + "ById");
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        method.addParameter("id", "java.lang.Object", null);
        method.addBody("checkUpdateable();"); // TODO: Or Annotation and Aspect!!??
        
        method.addBody("String ref = ObjectUtils.getObjectRef(this, id);");
        
        method.addBody("Iterator<String> it = bean." + getGetterName() + "().iterator();");
        method.addBody("while( it.hasNext() ) {");
        method.addBody("\tif( ref.equals(it.next()) ) it.remove();");
        method.addBody("}");
        
        if( prop.isOwned() ) {
            method.addBody("ObjectUtils.removeObject(this, ref);");
        }
        
        methods.add(method);
    }
    
    private void addRemoveByIndex() {
        JavaSymbolName name = new JavaSymbolName(getRemoveName());
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        method.addParameter(new JavaSymbolName("index"), JavaType.INT_PRIMITIVE, null);
        method.addBody("checkUpdateable();"); // TODO: Or Annotation and Aspect!!??
        
        method.addBody("List<String> refs = bean." + getGetterName() + "();");
        method.addBody("if( refs != null && index >= 0 && index < refs.size() ) {");
        method.addBody("\tString ref = refs.get(index);");
        if( prop.isOwned() ) method.addBody("\tObjectUtils.removeObject(this, ref);");
        method.addBody("\trefs.remove(index);");
        method.addBody("}");
        
        methods.add(method);
    }
    
    private void addRemoveByKey() {
        // TODO: RemoveByKey
    }
    
    private void addRemoveAll() {
        JavaSymbolName name = new JavaSymbolName(getRemoveAllName());
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        method.addBody("checkUpdateable();"); // TODO: Or Annotation and Aspect!!??
        
        // Remove all refs from container if owned
        if( prop.isOwned() ) {
            method.addBody("Iterator<String> it = bean." + getGetterName() + "().iterator();");
            method.addBody("while( it.hasNext() ) {");
            method.addBody("\tString ref = it.next();");
            method.addBody("\tObjectUtils.removeObject(this, ref);");
            method.addBody("\tit.remove();");
            method.addBody("}");
        }
        
        method.addBody("bean." + getSetterName() + "(null);");
        methods.add(method);
    }
}
