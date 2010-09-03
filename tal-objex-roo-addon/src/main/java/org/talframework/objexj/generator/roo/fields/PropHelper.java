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
import java.util.Iterator;
import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.talframework.objexj.generator.roo.ObjexObjMetadata;
import org.talframework.objexj.generator.roo.utils.MethodMetadataWrapper;

/**
 * This abstract class provides support for all the property
 * helper classes that aid {@link ObjexObjMetadata} to output
 * methods relating to properties
 * 
 * @author Tom Spencer
 */
public abstract class PropHelper {
    
    /** Holds the property we are generating methods for */
    protected final ObjexObjProperty prop;
    /** Holds the methods added by derived class */
    protected List<MethodMetadataWrapper> methods = new ArrayList<MethodMetadataWrapper>();
    
    protected PropHelper(ObjexObjProperty prop) {
        this.prop = prop;
    }
    
    /**
     * @return The main getter method name
     */
    protected String getGetterName() {
        return "get" + prop.getName().getSymbolNameCapitalisedFirstLetter();
    }
    
    /**
     * @return The main setter method name
     */
    protected String getSetterName() {
        return "set" + prop.getName().getSymbolNameCapitalisedFirstLetter();
    }
    
    /**
     * Call to actually build the methods relating to the property
     * inside the ObjexObj.
     * 
     * @param builder The builder to use
     * @param typeDetails The details of the governing type
     * @param typeId The metadata id for the above details
     */
    public void build(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        Iterator<MethodMetadataWrapper> it = methods.iterator();
        while( it.hasNext() ) {
            it.next().addMetadata(builder, typeDetails, typeId);
        }
    }
}
