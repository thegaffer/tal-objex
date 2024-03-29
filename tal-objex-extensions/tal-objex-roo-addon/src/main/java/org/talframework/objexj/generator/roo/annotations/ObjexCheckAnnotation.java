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
import org.talframework.objexj.annotations.source.ObjexCheck;
import org.talframework.objexj.generator.roo.utils.TypeDetailsUtil;

/**
 * This class represents a @ObjexCheck annotation and holds its
 * values in the source.
 *
 * @author Tom Spencer
 */
public class ObjexCheckAnnotation {

    @AutoPopulate private String message = null;
    @AutoPopulate private boolean postObjectCheck = false;
    @AutoPopulate private boolean childObjectCheck = false;
    
    /**
     * Helper to get the ObjexCheck annotation and its values if
     * it exists.
     * 
     * @param annotations The annotations
     * @return The {@link ObjexCheckAnnotation} instance if it exists in the annotations
     */
    //@Trace
    public static ObjexCheckAnnotation get(List<? extends AnnotationMetadata> annotations) {
        ObjexCheckAnnotation ret = null;
        
        AnnotationMetadata am = TypeDetailsUtil.getAnnotation(annotations, ObjexCheck.class.getName());
        if( am != null ) ret = new ObjexCheckAnnotation(am);
        
        return ret;
    }
    
    /**
     * Constructs an {@link ObjexCheckAnnotation} instance converting the
     * annotations values to this class.
     * 
     * @param annotationMetadata
     */
    private ObjexCheckAnnotation(AnnotationMetadata annotationMetadata) {
        AutoPopulationUtils.populate(this, annotationMetadata);
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for the message field
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
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

    /**
     * @return the childObjectCheck
     */
    public boolean isChildObjectCheck() {
        return childObjectCheck;
    }

    /**
     * Setter for the childObjectCheck field
     *
     * @param childObjectCheck the childObjectCheck to set
     */
    public void setChildObjectCheck(boolean childObjectCheck) {
        this.childObjectCheck = childObjectCheck;
    }
}
