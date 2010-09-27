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

import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.model.JavaSymbolName;
import org.talframework.objexj.generator.roo.annotations.ObjexRefPropAnnotation;

/**
 * This class represents a reference field
 * 
 * @author Tom Spencer
 */
public final class SimpleReferenceField extends ReferenceField {
    
    /**
     * Default constructor for manually creating the reference field.
     */
    public SimpleReferenceField() {
        super(false);
    }

    /**
     * Helper constructor that creates the field from the field
     * metadata and the ObjexRefProp annotation.
     * 
     * @param fm The field
     * @param field The fields prop annotation
     */
    public SimpleReferenceField(FieldMetadata fm, ObjexRefPropAnnotation field) {
        super(true);
        
        setName(fm.getFieldName());
        if( field.getExternalName() != null ) setName(new JavaSymbolName(field.getExternalName()));
        setType(field.getType());
        
        setBeanName(fm.getFieldName());
        setBeanType(fm.getFieldType());
        
        setTransformer("ObjectUtils");
        setGetTransformer("getObject(this, rawValue, " + field.getType().getSimpleTypeName() + ".class)");
        setSetTransformer("getObjectRef(val)");
        
        setSettable(field.isSettable());
        setGettable(field.isGettable());
        
        setNewType(field.getNewType());
        setOwned(field.isOwned());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(FieldVisitor visitor) {
        visitor.visitReference(this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getReferencePropName() {
        return getName().getSymbolNameCapitalisedFirstLetter() + "Ref";
    }
    
    @Override
    public String getCreatorName() {
        return "create" + getName().getSymbolNameCapitalisedFirstLetter();
    }
}
