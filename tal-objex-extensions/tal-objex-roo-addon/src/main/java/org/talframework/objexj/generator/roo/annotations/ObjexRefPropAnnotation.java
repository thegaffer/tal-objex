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
import org.springframework.roo.model.JavaType;
import org.talframework.objexj.annotations.source.ObjexRefProp;
import org.talframework.objexj.generator.roo.utils.TypeConstants;
import org.talframework.objexj.generator.roo.utils.TypeDetailsUtil;

public class ObjexRefPropAnnotation {

    @AutoPopulate private String externalName = null;
    @AutoPopulate private boolean owned = false;
    @AutoPopulate private JavaType type = null;
    @AutoPopulate private String newType = null;
    @AutoPopulate private boolean gettable = true;
    @AutoPopulate private boolean settable = true;
    
    /**
     * Helper to get the {@link ObjexRefProp} annotation and its values if
     * it exists.
     * 
     * @param annotations The annotations
     * @return The {@link ObjexRefPropAnnotation} instance if it exists in the annotations
     */
    //@Trace
    public static ObjexRefPropAnnotation get(List<? extends AnnotationMetadata> annotations) {
        ObjexRefPropAnnotation ret = null;
        
        AnnotationMetadata am = TypeDetailsUtil.getAnnotation(annotations, ObjexRefProp.class.getName());
        if( am != null ) ret = new ObjexRefPropAnnotation(am);
        
        return ret;
    }
    
    private ObjexRefPropAnnotation(AnnotationMetadata annotationMetadata) {
        AutoPopulationUtils.populate(this, annotationMetadata);
        
        if( "".equals(externalName) ) externalName = null;
        if( "".equals(newType) ) newType = null;
        if( TypeConstants.OBJECT.equals(type) ) type = new JavaType(TypeConstants.OBJEXOBJ);
    }

    /**
     * @return the externalName
     */
    public String getExternalName() {
        return externalName;
    }

    /**
     * Setter for the externalName field
     *
     * @param externalName the externalName to set
     */
    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }

    /**
     * @return the owned
     */
    public boolean isOwned() {
        return owned;
    }

    /**
     * Setter for the owned field
     *
     * @param owned the owned to set
     */
    public void setOwned(boolean owned) {
        this.owned = owned;
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
     * @return the newType
     */
    public String getNewType() {
        return newType;
    }

    /**
     * Setter for the newType field
     *
     * @param newType the newType to set
     */
    public void setNewType(String newType) {
        this.newType = newType;
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
}
