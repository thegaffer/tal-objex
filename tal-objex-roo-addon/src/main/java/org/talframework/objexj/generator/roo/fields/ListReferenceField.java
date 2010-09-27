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

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.talframework.objexj.generator.roo.annotations.ObjexRefPropAnnotation;

/**
 * This class represents a list reference field
 * 
 * @author Tom Spencer
 */
public final class ListReferenceField extends ReferenceField {
    
    /** Holds the type of the reference field */
    private JavaType memberType;
    
    /**
     * Default constructor for manually creating the reference field.
     */
    public ListReferenceField() {
        super(false);
    }

    /**
     * Helper constructor that creates the field from the field
     * metadata and the ObjexRefProp annotation.
     * 
     * @param fm The field
     * @param field The fields prop annotation
     */
    public ListReferenceField(FieldMetadata fm, ObjexRefPropAnnotation field) {
        super(true);
        
        setName(fm.getFieldName());
        if( field.getExternalName() != null ) setName(new JavaSymbolName(field.getExternalName()));
        
        // Set type
        List<JavaType> enclosingTypeParams = new ArrayList<JavaType>();
        enclosingTypeParams.add(field.getType());
        setType(new JavaType("java.util.List", 0, null, null, enclosingTypeParams));
        
        setBeanName(fm.getFieldName());
        setBeanType(fm.getFieldType());
        
        setTransformer("ObjectUtils");
        setGetTransformer("getObjectList(this, rawValue, " + field.getType().getSimpleTypeName() + ".class)");
        setSetTransformer("getObjectRefList(val)");
        
        setSettable(field.isSettable());
        setGettable(field.isGettable());
        
        setNewType(field.getNewType());
        setOwned(field.isOwned());
        memberType = field.getType();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(FieldVisitor visitor) {
        visitor.visitList(this);
    }
    
    /**
     * @return the memberType
     */
    public JavaType getMemberType() {
        return memberType;
    }

    /**
     * Setter for the memberType field
     *
     * @param memberType the memberType to set
     */
    public void setMemberType(JavaType memberType) {
        this.memberType = memberType;
    }
    
    public String getMemberTypeName() {
        return memberType.getSimpleTypeName();
    }
    
    public String getItemReference() {
        String baseProp = getName().getSymbolNameCapitalisedFirstLetter();
        String singleProp = baseProp;
        
        if( singleProp.endsWith("ies") && singleProp.length() > 3 ) singleProp = singleProp.substring(0, singleProp.length() - 3) + "y";
        else if( singleProp.endsWith("s") && singleProp.length() > 1 ) singleProp = singleProp.substring(0, singleProp.length() - 1);
        
        return singleProp;
    }
    
    public String getRemoveAllName() {
        return "removeAll" + getName().getSymbolNameCapitalisedFirstLetter();
    }
    
    @Override
    public String getCreatorName() {
        return "create" + getItemReference();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReferencePropName() {
        String ret = getItemReference();
        return ret + "Refs";
    }
}
