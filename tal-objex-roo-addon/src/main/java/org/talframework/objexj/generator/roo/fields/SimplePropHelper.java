/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.objexj.generator.roo.fields;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.talframework.objexj.generator.roo.ObjexObjMetadata;
import org.talframework.objexj.generator.roo.utils.MethodMetadataWrapper;

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
        builder.getImportRegistrationResolver().addImport(new JavaType("org.talframework.objexj.object.StateBeanUtils"));
        
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
