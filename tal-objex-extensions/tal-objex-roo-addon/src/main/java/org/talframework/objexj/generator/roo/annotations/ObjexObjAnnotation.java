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

package org.talframework.objexj.generator.roo.annotations;

import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.annotations.populator.AbstractAnnotationValues;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulate;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulationUtils;
import org.springframework.roo.model.JavaType;
import org.talframework.objexj.annotations.source.ObjexObj;

public class ObjexObjAnnotation extends AbstractAnnotationValues {
    
    @AutoPopulate private JavaType value = null;
    private String objexType = null;
    
    /**
     * Helper to get the {@link ObjexObj} annotation and its values if
     * it exists.
     * 
     * @param annotations The annotations
     * @return The {@link ObjexObjAnnotation} instance if it exists in the annotations
     */
    //@Trace
    public static ObjexObjAnnotation get(PhysicalTypeMetadata typeDetails) {
        ObjexObjAnnotation ret = new ObjexObjAnnotation(typeDetails);
        return ret;
    }
    
    private ObjexObjAnnotation(PhysicalTypeMetadata typeDetails) {
        super(typeDetails, new JavaType(ObjexObj.class.getName()));
        AutoPopulationUtils.populate(this, annotationMetadata);
    }

    /**
     * @return the value
     */
    public JavaType getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(JavaType value) {
        this.value = value;
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
}
