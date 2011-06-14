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

import java.util.List;

import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulate;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulationUtils;
import org.talframework.objexj.annotations.source.ObjexEnrich;
import org.talframework.objexj.generator.roo.utils.TypeDetailsUtil;

/**
 * This class represents a @ObjexEnrich annotation and holds its
 * values in the source.
 *
 * @author Tom Spencer
 */
public class ObjexEnrichAnnotation {

    @AutoPopulate private boolean postObjectCheck = false;
    
    /**
     * Helper to get the ObjexCheck annotation and its values if
     * it exists.
     * 
     * @param annotations The annotations
     * @return The {@link ObjexEnrichAnnotation} instance if it exists in the annotations
     */
    //@Trace
    public static ObjexEnrichAnnotation get(List<? extends AnnotationMetadata> annotations) {
        ObjexEnrichAnnotation ret = null;
        
        AnnotationMetadata am = TypeDetailsUtil.getAnnotation(annotations, ObjexEnrich.class.getName());
        if( am != null ) ret = new ObjexEnrichAnnotation(am);
        
        return ret;
    }
    
    /**
     * Constructs an {@link ObjexEnrichAnnotation} instance converting the
     * annotations values to this class.
     * 
     * @param annotationMetadata
     */
    private ObjexEnrichAnnotation(AnnotationMetadata annotationMetadata) {
        AutoPopulationUtils.populate(this, annotationMetadata);
    }

    /**
     * @return the postObjectCheck
     */
    public boolean isPostObjectCheck() {
        return postObjectCheck;
    }

    /**
     * Setter for the postObjectCheck field
     *
     * @param postObjectCheck the postObjectCheck to set
     */
    public void setPostObjectCheck(boolean postObjectCheck) {
        this.postObjectCheck = postObjectCheck;
    }
}
