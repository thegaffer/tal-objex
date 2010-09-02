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
public class SimplePropHelper extends PropHelper {

    public SimplePropHelper(ObjexObjProperty prop) {
        super(prop);
        
        addObjexGetter();
        if( prop.isSettable() ) addObjexSetter();
    }
    
    @Override
    public void build(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        builder.getImportRegistrationResolver().addImport(new JavaType("org.tpspencer.tal.objexj.object.StateBeanUtils"));
        
        super.build(builder, typeDetails, typeId);
    }
    
    private void addObjexGetter() {
        JavaSymbolName name = prop.getType().equals(JavaType.BOOLEAN_PRIMITIVE) ?
                new JavaSymbolName("is" + prop.getName().getSymbolNameCapitalisedFirstLetter()) :
                new JavaSymbolName("get" + prop.getName().getSymbolNameCapitalisedFirstLetter());
        
        MethodMetadataWrapper getter = new MethodMetadataWrapper(name, prop.getType());
        if( prop.getType().isPrimitive() ) getter.addBody("return bean.get" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "();");
        else getter.addBody("return cloneValue(bean.get" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "());");
        methods.add(getter);
    }
    
    private void addObjexSetter() {
        JavaSymbolName name = new JavaSymbolName("set" + prop.getName().getSymbolNameCapitalisedFirstLetter());
        
        MethodMetadataWrapper setter = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        setter.addParameter(new JavaSymbolName("val"), prop.getType(), null);
        setter.addBody("if( !StateBeanUtils.hasChanged(bean.get" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "(), val) ) return;");
        setter.addBody("ensureUpdateable(bean);");
        setter.addBody("bean.set" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "(val);");
        methods.add(setter);
    }
}
