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

import java.util.List;
import java.util.Map;

import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulate;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulationUtils;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.talframework.objexj.annotations.ObjexRefProp;
import org.talframework.objexj.generator.roo.utils.TypeDetailsUtil;

/**
 * This class describes a property or field of an Objex
 * object. These classes are formed by a compiler reading
 * the state bean.
 * 
 * @author Tom Spencer
 */
public class ObjexObjProperty {

    /** Holds the name of the property */
    private final JavaSymbolName name;
    /** Holds the basic type of the property (on the state bean) */
    private final JavaType type;
    
    /** Determines if the property is settable or not */
    @AutoPopulate("settable") private boolean settable = true;
    
    /** Holds the member type if a reference property */
    @AutoPopulate("type") private JavaType memberType = null;
    /** Holds the type name to create (if any) */ 
    @AutoPopulate("newType") private String newTypeName = null;
    /** Holds whether owned references (if a reference prop) */
    @AutoPopulate("owned") private boolean owned = false;
    
    // FUTURE: Validation etc
    
    public ObjexObjProperty(FieldMetadata fm) {
        this.name = fm.getFieldName();
        this.type = fm.getFieldType();
        
        AnnotationMetadata am = TypeDetailsUtil.getAnnotation(fm.getAnnotations(), ObjexRefProp.class.getName());
        if( am != null ) {
            AutoPopulationUtils.populate(this, am);
            if( "".equals(newTypeName) ) newTypeName = null;
        }
    }
    
    public ObjexObjProperty(String name, String type) {
        this.name = new JavaSymbolName(name);
        this.type = new JavaType(type);
    }
    
    /**
     * @return The name of the property
     */
    public JavaSymbolName getName() {
        return name;
    }
    
    /**
     * @return The type of the property
     */
    public JavaType getType() {
        return type;
    }

    /**
     * @return True if this property is a simple reference property
     */
    public boolean isReferenceProp() {
        return memberType != null && 
            type.getFullyQualifiedTypeName().equals(String.class.getName());
    }
    
    /**
     * @return True if this property is a list reference property
     */
    public boolean isListReferenceProp() {
        return memberType != null && 
            type.getFullyQualifiedTypeName().equals(List.class.getName());
    }
    
    /**
     * @return True if this property is a map reference property
     */
    public boolean isMapReferenceProp() {
        return memberType != null && 
            type.getFullyQualifiedTypeName().equals(Map.class.getName());
    }

    /**
     * @return the settable
     */
    public boolean isSettable() {
        return settable;
    }

    /**
     * @param settable the settable to set
     */
    public void setSettable(boolean settable) {
        this.settable = settable;
    }

    /**
     * @return the memberType
     */
    public JavaType getMemberType() {
        return memberType;
    }

    /**
     * @param memberType the memberType to set
     */
    public void setMemberType(JavaType memberType) {
        this.memberType = memberType;
    }

    /**
     * @return the newTypeName
     */
    public String getNewTypeName() {
        return newTypeName;
    }

    /**
     * @param newTypeName the newTypeName to set
     */
    public void setNewTypeName(String newTypeName) {
        this.newTypeName = newTypeName;
    }

    /**
     * @return the owned
     */
    public boolean isOwned() {
        return owned;
    }

    /**
     * @param owned the owned to set
     */
    public void setOwned(boolean owned) {
        this.owned = owned;
    }
}
