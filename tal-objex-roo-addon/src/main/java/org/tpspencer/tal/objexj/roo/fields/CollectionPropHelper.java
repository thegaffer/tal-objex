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
        if( prop.isMapReferenceProp() ) method.addBody("return getContainer().getObjectMap(bean." + getGetterName() + "(), " + prop.getMemberType().getSimpleTypeName() + ".class);");
        else method.addBody("return getContainer().getObjectList(bean." + getGetterName() + "(), " + prop.getMemberType().getSimpleTypeName() + ".class);");
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
        method.addBody("return cloneValue(bean." + getGetterName() + "());");
        methods.add(method);
    }
    
    private void addStandardSetter() {
        // TODO: Future, unsure about this right now!!
    }
    
    private void addRefSetter() {
        JavaSymbolName name = new JavaSymbolName(getRefSetterName());
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        method.addParameter(new JavaSymbolName("val"), prop.getType(), null);

        method.addBody("ensureUpdateable(bean);");
        addGetRefsSnippet(method, "refs", true);
        method.addBody("refs.add(val);");
        
        methods.add(method);
    }
    
    private void addCreate() {
        String newType = prop.getNewTypeName();
        
        JavaSymbolName name = new JavaSymbolName(getCreateName());
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, prop.getMemberType());
        if( newType == null ) method.addParameter("type", String.class.getName(), null);
        method.addBody("checkUpdateable();");
        
        if( newType == null ) method.addBody("ObjexObj val = ObjectUtils.createObject(this, bean, type);");
        else method.addBody("ObjexObj val = ObjectUtils.createObject(this, bean, \"" + newType + "\");");
        
        method.addBody("ensureUpdateable(bean);");
        addGetRefsSnippet(method, "refs", true);
        method.addBody("refs.add(val.getId().toString());");
        
        method.addBody("return val.getBehaviour(" + prop.getMemberType().getSimpleTypeName() + ".class);");
        methods.add(method);
    }
    
    private void addAdd() {
        JavaSymbolName name = new JavaSymbolName(getAddName());
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        method.addParameter(new JavaSymbolName("val"), prop.getMemberType(), null);
        method.addBody("checkUpdateable();");
        method.addBody("String ref = ObjectUtils.getObjexId(val);");
        
        method.addBody("ensureUpdateable(bean);");
        addGetRefsSnippet(method, "refs", true);
        method.addBody("refs.add(ref);");
        
        methods.add(method);
    }
    
    private void addRemoveById() {
        JavaSymbolName name = new JavaSymbolName(getRemoveName() + "ById");
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        method.addParameter("id", "java.lang.Object", null);
        method.addBody("checkUpdateable();");
        
        method.addBody("String ref = ObjectUtils.getObjectRef(this, id);");
        
        addGetRefsSnippet(method, "refs", false);
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
    
    private void addRemoveByIndex() {
        JavaSymbolName name = new JavaSymbolName(getRemoveName());
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        method.addParameter(new JavaSymbolName("index"), JavaType.INT_PRIMITIVE, null);
        method.addBody("checkUpdateable();");
        
        addGetRefsSnippet(method, "refs", false);
        method.addBody("if( refs == null || index < 0 || index >= refs.size() ) return;");
        
        if( prop.isOwned() ) method.addBody("String ref = refs.get(index);");
        method.addBody("ensureUpdateable(bean);");
        method.addBody("refs.remove(index);");
        if( prop.isOwned() ) method.addBody("ObjectUtils.removeObject(this, bean, ref);");
        
        methods.add(method);
    }
    
    private void addRemoveByKey() {
        // TODO: RemoveByKey
    }
    
    private void addRemoveAll() {
        JavaSymbolName name = new JavaSymbolName(getRemoveAllName());
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        
        addGetRefsSnippet(method, "refs", false);
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
        method.addBody("bean." + getSetterName() + "(null);");
        
        methods.add(method);
    }
    
    /**
     * Helper to get the existing refs, create them
     * if they do not exist.
     */
    private void addGetRefsSnippet(MethodMetadataWrapper method, String refs, boolean create) {
        method.addBody("List<String> " + refs + " = bean." + getGetterName() + "();");
        if( !create ) return;
        
        method.addBody("if( " + refs + " == null ) {");
        method.addBody("\trefs = new ArrayList<String>();");
        method.addBody("\tbean." + getSetterName() + "(refs);");
        method.addBody("}");
    }
}
