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

import java.util.Date;

import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.talframework.objexj.generator.roo.annotations.ObjexPropAnnotation;
import org.talframework.objexj.generator.roo.annotations.ObjexRefPropAnnotation;

/**
 * This class represents a simple field
 * 
 * @author Tom Spencer
 */
public final class SimpleField extends ObjexField {
    
    /** For Memo or Blob, determines if large versions */
    private boolean large = false;

    /**
     * Default constructor for manually creating the simple field.
     */
    public SimpleField() {
        super(false);
    }

    /**
     * Helper constructor that creates the field from the field
     * metadata and the ObjexProp annotation.
     * 
     * @param fm The field
     * @param field The fields prop annotation
     */
    public SimpleField(FieldMetadata fm, ObjexPropAnnotation field) {
        super(true);
        
        setName(fm.getFieldName());
        setType(fm.getFieldType());
        
        setBeanName(fm.getFieldName());
        setBeanType(fm.getFieldType());
        
        // Overrides if we have the ObjexProp annotation
        if( field != null ) {
            if( field.getExternalName() != null ) setName(new JavaSymbolName(field.getExternalName()));
            
            if( field.getExposeAs() != null ) {
                setType(field.getExposeAs());
                
                // Separate functions for transformation
                if( field.getTransformerGetFunction() != null ) {
                    setTransformer(field.getTransformerClass() != null ? field.getTransformerClass().getSimpleTypeName() : "this");
                    setGetTransformer(field.getTransformerGetFunction());
                    setSetTransformer(field.getTransformerSetFunction());
                }
                
                // Expose type capable of transformation
                else {
                    setSetTransformer(field.getTransformerSetFunction());
                }
            }
        
            setSettable(field.isSettable());
            setGettable(field.isGettable());
            
            // TODO: Set whether the field is large or not
        }
        
        setObjexType(determineObjexFieldType(getBeanType(), field));
        
        if( "ObjexFieldType.MEMO".equals(getObjexType()) ) large = true;
        else if( "ObjexFieldType.BLOB".equals(getObjexType()) ) large = true;
        else if( "ObjexFieldType.OBJECT".equals(getObjexType()) ) large = true;
    }
    
    /**
     * Helper constructor that creates a simple field for a reference
     * property
     * 
     * @param fm The field
     * @param field The fields prop annotation
     */
    public SimpleField(FieldMetadata fm, String name, ObjexRefPropAnnotation ref) {
        super(false);
        
        setBeanName(fm.getFieldName());
        setBeanType(fm.getFieldType());
        setName(new JavaSymbolName(name));
        setType(fm.getFieldType());
        setSettable(!ref.isOwned());
        setGettable(true);
    }
    
    private String determineObjexFieldType(JavaType type, ObjexPropAnnotation field) {
        String ret = "ObjexFieldType.";
        
        String typeName = type.getFullyQualifiedTypeName();
        
        if( field != null && field.getObjexType() != null ) ret += field.getObjexType();
        else if( typeName.equals(String.class.getName()) ) ret += "STRING";
        else if( typeName.equals(Date.class.getName()) ) ret += "DATE";
        else if( typeName.equals(Byte.class.getName()) ) ret += "BLOB";
        else if( typeName.equals(Double.class.getName()) ) ret += "NUMBER";
        else if( typeName.equals(Float.class.getName()) ) ret += "NUMBER";
        else if( typeName.equals(Long.class.getName()) ) ret += "NUMBER";
        else if( typeName.equals(Integer.class.getName()) ) ret += "NUMBER";
        else if( typeName.equals(Short.class.getName()) ) ret += "NUMBER";
        else if( typeName.equals(Character.class.getName()) ) ret += "STRING";
        else if( typeName.equals(Boolean.class.getName()) ) ret += "BOOL";
        else ret += "OBJECT";
        
        return ret;
    }
    
    /**
     * Determines if the field is a large version
     * 
     * @return The field
     */
    public boolean isLarge() {
        return large;
    }
    
    /**
     * Sets if the field represents a large field
     * 
     * @param isLarge
     */
    public void setLarge(boolean isLarge) {
        this.large = isLarge;
    }
    
    @Override
    public void accept(FieldVisitor visitor) {
        visitor.visitSimple(this);
    }
}
