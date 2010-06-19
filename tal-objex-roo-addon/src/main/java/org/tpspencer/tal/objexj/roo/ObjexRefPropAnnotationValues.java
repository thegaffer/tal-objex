package org.tpspencer.tal.objexj.roo;

import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulate;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulationUtils;
import org.springframework.roo.model.JavaType;

public class ObjexRefPropAnnotationValues {

    @AutoPopulate private boolean owned = false;
    @AutoPopulate private JavaType type = null;
    @AutoPopulate private String newType = null;
    @AutoPopulate private boolean settable = true;
    
    public ObjexRefPropAnnotationValues(AnnotationMetadata annotationMetadata) {
        AutoPopulationUtils.populate(this, annotationMetadata);
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

    /**
     * @return the type
     */
    public JavaType getType() {
        return type;
    }

    /**
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
     * @param newType the newType to set
     */
    public void setNewType(String newType) {
        this.newType = newType;
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
    
}
