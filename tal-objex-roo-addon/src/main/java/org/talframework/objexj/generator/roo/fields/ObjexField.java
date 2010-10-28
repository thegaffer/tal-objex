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

import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.talframework.objexj.generator.roo.ObjexObjMetadata;

/**
 * This abstract class provides support for all the property
 * helper classes that aid {@link ObjexObjMetadata} to output
 * methods relating to properties
 * 
 * @author Tom Spencer
 */
public abstract class ObjexField {
    
    /** Indicates if the field exists on the bean or is made-up for the object */
    private final boolean naturalBeanField;
    /** Holds the exposed name of the field */
    private JavaSymbolName name = null;
    /** Holds the exposed type of the field */
    private JavaType type = null;
    /** Holds the ObjexType of the field */
    private String objexType = null;
    /** Holds the name of the field on the state bean */
    private JavaSymbolName beanName = null;
    /** Holds the type of the property on the state bean */
    private JavaType beanType = null;
    /** The name of the transform to use, might be a static class or 'this' (if null exposed type capable of transformation) */
    private String transformer = null;
    /** Holds the get transformer function if type and beanType are different */
    private String getTransformer = null;
    /** Holds the set transformer function if type and beanType are different */
    private String setTransformer = null;
    /** If a property is not settable, then it is not exposed */
    private boolean settable = true;
    /** If a property is not gettable, then it is not exposed */
    private boolean gettable = true;
    
    public ObjexField(boolean naturalBeanField) {
        this.naturalBeanField = naturalBeanField;
    }
    
    /**
     * Call to accept the visitor and call the relevant
     * visit method on it. GoF Visitor Pattern.
     * 
     * @param visitor The visitor to visit
     */
    public abstract void accept(FieldVisitor visitor);
    
    /**
     * @return the name
     */
    public JavaSymbolName getName() {
        return name;
    }

    /**
     * Setter for the name field
     *
     * @param name the name to set
     */
    public void setName(JavaSymbolName name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public JavaType getType() {
        return type;
    }

    /**
     * Setter for the type field
     *
     * @param type the type to set
     */
    public void setType(JavaType type) {
        this.type = type;
    }
    
    /**
     * @return the objexType
     */
    public String getObjexType() {
        return objexType;
    }

    /**
     * Setter for the objexType field
     *
     * @param objexType the objexType to set
     */
    public void setObjexType(String objexType) {
        this.objexType = objexType;
    }

    /**
     * @return the beanName
     */
    public JavaSymbolName getBeanName() {
        return beanName;
    }

    /**
     * Setter for the beanName field
     *
     * @param beanName the beanName to set
     */
    public void setBeanName(JavaSymbolName beanName) {
        this.beanName = beanName;
    }

    /**
     * @return the beanType
     */
    public JavaType getBeanType() {
        return beanType;
    }

    /**
     * Setter for the beanType field
     *
     * @param beanType the beanType to set
     */
    public void setBeanType(JavaType beanType) {
        this.beanType = beanType;
    }

    /**
     * @return the getTransformer
     */
    public String getGetTransformer() {
        String ret = getTransformer;
        if( getTransformer == null ) ret = "new" + type.getNameIncludingTypeParameters();
        return ret;
    }

    /**
     * Setter for the getTransformer field
     *
     * @param getTransformer the getTransformer to set
     */
    public void setGetTransformer(String getTransformer) {
        this.getTransformer = getTransformer;
    }

    /**
     * @return the setTransformer
     */
    public String getSetTransformer() {
        return setTransformer;
    }

    /**
     * Setter for the setTransformer field
     *
     * @param setTransformer the setTransformer to set
     */
    public void setSetTransformer(String setTransformer) {
        this.setTransformer = setTransformer;
    }

    /**
     * @return the settable
     */
    public boolean isSettable() {
        return settable;
    }

    /**
     * Setter for the settable field
     *
     * @param settable the settable to set
     */
    public void setSettable(boolean settable) {
        this.settable = settable;
    }

    /**
     * @return the gettable
     */
    public boolean isGettable() {
        return gettable;
    }

    /**
     * Setter for the gettable field
     *
     * @param gettable the gettable to set
     */
    public void setGettable(boolean gettable) {
        this.gettable = gettable;
    }
    
    /**
     * @return the naturalBeanField
     */
    public boolean isNaturalBeanField() {
        return naturalBeanField;
    }

    /**
     * @return the transformer
     */
    public String getTransformer() {
        return transformer;
    }

    /**
     * Setter for the transformer field
     *
     * @param transformer the transformer to set
     */
    public void setTransformer(String transformer) {
        this.transformer = transformer;
    }
    
    ///////////////////////////////////////////////
    // Helper Methods
    
    /**
     * Determines if the property is exposed as a different type
     */
    public boolean isTransformed() {
        return !type.equals(beanType);
    }
    
    /**
     * @return The main getter method name
     */
    public String getGetterMethodName() {
        String prefix = "get";
        if( JavaType.BOOLEAN_PRIMITIVE.equals(type) ) prefix = "is";
        else if( JavaType.BOOLEAN_OBJECT.equals(type) ) prefix = "is";
        return prefix + name.getSymbolNameCapitalisedFirstLetter();
    }
    
    /**
     * @return The main setter method name
     */
    public String getSetterMethodName() {
        return "set" + name.getSymbolNameCapitalisedFirstLetter();
    }
    
    /**
     * @return The beans getter method name
     */
    public String getBeanGetterMethodName() {
        String prefix = "get";
        if( JavaType.BOOLEAN_PRIMITIVE.equals(beanType) ) prefix = "is";
        else if( JavaType.BOOLEAN_OBJECT.equals(beanType) ) prefix = "is";
        return prefix + beanName.getSymbolNameCapitalisedFirstLetter();
    }
    
    /**
     * @return The beans setter method name
     */
    public String getBeanSetterMethodName() {
        return "set" + beanName.getSymbolNameCapitalisedFirstLetter();
    }
    
    /**
     * @return The beans setter method name
     */
    public String getBeanIsSetMethodName() {
        return "is" + beanName.getSymbolNameCapitalisedFirstLetter() + "Set";
    }
    
    /**
     * @return The beans setter method name
     */
    public String getBeanIsChangedMethodName() {
        return "is" + beanName.getSymbolNameCapitalisedFirstLetter() + "Changed";
    }
    
    /**
     * @return the simple type name for the field on the bean
     */
    public String getBeanTypeName() {
        return beanType.getNameIncludingTypeParameters();
    }
    
    /**
     * @return the simple type name for the field
     */
    public String getTypeName() {
        return type.getNameIncludingTypeParameters();
    }
}
