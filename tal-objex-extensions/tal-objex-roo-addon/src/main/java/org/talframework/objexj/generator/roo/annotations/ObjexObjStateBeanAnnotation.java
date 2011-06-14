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
