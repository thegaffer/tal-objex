package org.tpspencer.tal.objexj.roo;

import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.annotations.populator.AbstractAnnotationValues;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulate;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulationUtils;
import org.springframework.roo.model.JavaType;
import org.tpspencer.tal.objexj.annotations.ObjexStateBean;

public class ObjexObjStateBeanAnnotationValues extends AbstractAnnotationValues {
    
    @AutoPopulate private String name = null;
    @AutoPopulate private long version = 1;

    public ObjexObjStateBeanAnnotationValues(PhysicalTypeMetadata governorPhysicalTypeMetadata) {
        super(governorPhysicalTypeMetadata, new JavaType(ObjexStateBean.class.getName()));
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
