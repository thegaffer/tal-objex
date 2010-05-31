package org.tpspencer.tal.objexj.roo;

import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.annotations.populator.AbstractAnnotationValues;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulate;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulationUtils;
import org.springframework.roo.model.JavaType;
import org.tpspencer.tal.objexj.annotations.ObjexObj;

public class ObjexObjAnnotationValues extends AbstractAnnotationValues {
    
    @AutoPopulate private JavaType value = null;
    
    public ObjexObjAnnotationValues(PhysicalTypeMetadata governorPhysicalTypeMetadata) {
        super(governorPhysicalTypeMetadata, new JavaType(ObjexObj.class.getName()));
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
}
