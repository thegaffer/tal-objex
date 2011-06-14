/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
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
public final class MapReferenceField extends ReferenceField {
    
    /** Holds the type of the reference field */
    private JavaType memberType;
    
    /**
     * Default constructor for manually creating the reference field.
     */
    public MapReferenceField() {
        super(false);
    }

    /**
     * Helper constructor that creates the field from the field
     * metadata and the ObjexRefProp annotation.
     * 
     * @param fm The field
     * @param field The fields prop annotation
     */
    public MapReferenceField(FieldMetadata fm, ObjexRefPropAnnotation field) {
        super(true);
        
        setName(fm.getFieldName());
        if( field.getExternalName() != null ) setName(new JavaSymbolName(field.getExternalName()));
        
        // Set type
        List<JavaType> enclosingTypeParams = new ArrayList<JavaType>();
        enclosingTypeParams.add(JavaType.STRING_OBJECT); // TODO: Can we deduce this!?
        enclosingTypeParams.add(field.getType());
        setType(new JavaType("java.util.Map", 0, null, null, enclosingTypeParams));
        
        setBeanName(fm.getFieldName());
        setBeanType(fm.getFieldType());
        
        setTransformer("ObjectUtils");
        setGetTransformer("getObjectMap(this, rawValue, " + field.getType().getSimpleTypeName() + ".class)");
        setSetTransformer("getObjectRefMap(val)");
        
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
        visitor.visitMap(this);
    }
    
    @Override
    public boolean isCollection() {
        return true;
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
        
        return baseProp;
    }
    
    public String getCreateMethodName() {
        return "create" + getItemReference();
    }
    
    public String getAddMethodName() {
        return "add" + getItemReference();
    }
    
    public String getGetByKeyMethodName() {
        return "get" + getItemReference() + "ByKey";
    }
    
    public String getRemoveByKeyMethodName() {
        return "remove" + getItemReference() + "ByKey";
    }
    
    public String getRemoveByIdMethodName() {
        return "remove" + getItemReference() + "ById";
    }
    
    public String getRemoveAllMethodName() {
        return "removeAll" + getName().getSymbolNameCapitalisedFirstLetter();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getReferencePropName() {
        String baseProp = getName().getSymbolName();
        String singleProp = baseProp;
        
        if( singleProp.endsWith("ies") && singleProp.length() > 3 ) singleProp = singleProp.substring(0, singleProp.length() - 3) + "y";
        else if( singleProp.endsWith("s") && singleProp.length() > 1 ) singleProp = singleProp.substring(0, singleProp.length() - 1);
        
        return singleProp + "Refs";
    }
}
