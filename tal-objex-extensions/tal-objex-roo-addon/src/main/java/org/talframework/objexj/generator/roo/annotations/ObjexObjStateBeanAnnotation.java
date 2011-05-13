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
import org.talframework.objexj.annotations.source.ObjexStateBean;

public class ObjexObjStateBeanAnnotation extends AbstractAnnotationValues {
    
    @AutoPopulate private String name = null;
    @AutoPopulate private long version = 1;

    /**
     * Helper to get the {@link ObjexStateBean} annotation and its values if
     * it exists.
     * 
     * @param annotations The annotations
     * @return The {@link ObjexObjStateBeanAnnotation} instance if it exists in the annotations
     */
    //@Trace
    public static ObjexObjStateBeanAnnotation get(PhysicalTypeMetadata typeDetails) {
        ObjexObjStateBeanAnnotation ret = new ObjexObjStateBeanAnnotation(typeDetails);
        return ret;
    }
    
    private ObjexObjStateBeanAnnotation(PhysicalTypeMetadata typeDetails) {
        super(typeDetails, new JavaType(ObjexStateBean.class.getName()));
        AutoPopulationUtils.populate(this, annotationMetadata);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the version
     */
    public long getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(long version) {
        this.version = version;
    }
}
