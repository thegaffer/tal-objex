package org.tpspencer.tal.objexj.roo.fields;

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
    
    private void addObjexGetter() {
        // TODO: Boolean is!
        JavaSymbolName name = new JavaSymbolName("get" + prop.getName().getSymbolNameCapitalisedFirstLetter());
        
        MethodMetadataWrapper getter = new MethodMetadataWrapper(name, prop.getType());
        getter.addBody("return bean.get" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "();");
        methods.add(getter);
    }
    
    private void addObjexSetter() {
        JavaSymbolName name = new JavaSymbolName("set" + prop.getName().getSymbolNameCapitalisedFirstLetter());
        
        MethodMetadataWrapper setter = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        setter.addParameter(new JavaSymbolName("val"), prop.getType(), null);
        setter.addBody("checkUpdateable();"); // TODO: Or Annotation and Aspect!!??
        setter.addBody("bean.set" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "(val);");
        methods.add(setter);
    }
}
