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
    
    @Override
    public boolean isCollection() {
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getReferencePropName() {
        return getName().getSymbolNameCapitalisedFirstLetter() + "Ref";
    }
    
    public String getCreateMethodName() {
        return "create" + getName().getSymbolNameCapitalisedFirstLetter();
    }
    
    public String getRemoveMethodName() {
        return "remove" + getName().getSymbolNameCapitalisedFirstLetter();
    }
}
