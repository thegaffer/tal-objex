package org.tpspencer.tal.objexj.roo.fields;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.tpspencer.tal.objexj.roo.ObjexObjMetadata;
import org.tpspencer.tal.objexj.roo.utils.MethodMetadataWrapper;

/**
 * This helper class aids the {@link ObjexObjMetadata} class to
 * output relevant getter/setter for a reference property.
 * 
 * @author Tom Spencer
 */
public class ReferencePropHelper extends PropHelper {

    public ReferencePropHelper(ObjexObjProperty prop) {
        super(prop);
        
        addGetter();
        addRefGetter();
        if( prop.isSettable() ) {
            if( prop.isOwned() ) {
                addCreator();
                addRemove();
            }
            else {
                addSetter();
                addRefSetter();
            }
        }
    }
    
    @Override
    public void build(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        builder.getImportRegistrationResolver().addImport(new JavaType("org.tpspencer.tal.objexj.object.ObjectUtils"));
        builder.getImportRegistrationResolver().addImport(new JavaType("org.tpspencer.tal.objexj.object.StateBeanUtils"));
        builder.getImportRegistrationResolver().addImport(new JavaType("org.tpspencer.tal.objexj.ObjexObj"));

        super.build(builder, typeDetails, typeId);
    }

    private void addGetter() {
        JavaSymbolName name = new JavaSymbolName(getGetterName());
        
        MethodMetadataWrapper getter = new MethodMetadataWrapper(name, prop.getMemberType());
        getter.addBody("return ObjectUtils.getObject(this, bean." + getGetterName() + "(), " + prop.getMemberType().getSimpleTypeName() + ".class);");
        methods.add(getter);
    }
    
    private void addSetter() {
        JavaSymbolName name = new JavaSymbolName(getSetterName());
        
        MethodMetadataWrapper setter = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        setter.addParameter(new JavaSymbolName("val"), prop.getMemberType(), null);
        setter.addBody("String ref = ObjectUtils.getObjectRef(val);");
        setter.addBody("if( !StateBeanUtils.hasChanged(bean.get" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "(), ref) ) return;");
        setter.addBody("ensureUpdateable(bean);");
        setter.addBody("bean." + getSetterName() + "(ref);");
        methods.add(setter);
    }
    
    private void addCreator() {
        JavaSymbolName name = new JavaSymbolName("create" + prop.getName().getSymbolNameCapitalisedFirstLetter());
        
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, prop.getMemberType());
        if( prop.getNewTypeName() == null ) method.addParameter("type", String.class.getName(), null);
        
        method.addBody("ensureUpdateable(bean);");
        
        // Remove if already present
        method.addBody("if( bean." + getGetterName() + "() != null )");
        method.addBody("\tObjectUtils.removeObject(this, bean, bean." + getGetterName() + "());");
        
        if( prop.getNewTypeName() == null ) method.addBody("ObjexObj val = ObjectUtils.createObject(this, bean, type);");
        else method.addBody("ObjexObj val = ObjectUtils.createObject(this, bean, \"" + prop.getNewTypeName() + "\");");
        method.addBody("bean." + getSetterName() + "(val.getId().toString());");
        method.addBody("return val.getBehaviour(" + prop.getMemberType().getSimpleTypeName() + ".class);");
        
        methods.add(method);
    }
    
    private void addRemove() {
        JavaSymbolName name = new JavaSymbolName("remove" + prop.getName().getSymbolNameCapitalisedFirstLetter());
        
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        
        method.addBody("ensureUpdateable(bean);");
        method.addBody("if( bean." + getGetterName() + "() != null )");
        method.addBody("\tObjectUtils.removeObject(this, bean, bean." + getGetterName() + "());");
        
        methods.add(method);
    }
    
    private void addRefGetter() {
        JavaSymbolName name = new JavaSymbolName(getGetterName() + "Ref");
        
        MethodMetadataWrapper getter = new MethodMetadataWrapper(name, prop.getType());
        getter.addBody("return bean.get" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "();");
        methods.add(getter);
    }
    
    private void addRefSetter() {
        JavaSymbolName name = new JavaSymbolName(getSetterName() + "Ref");
        
        MethodMetadataWrapper setter = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        setter.addParameter(new JavaSymbolName("val"), prop.getType(), null);
        setter.addBody("if( !StateBeanUtils.hasChanged(bean.get" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "(), val) ) return;");
        setter.addBody("ensureUpdateable(bean);");
        setter.addBody("bean.set" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "(val);");
        methods.add(setter);
    }
}
