package org.talframework.objexj.generator.roo.annotations;

import java.util.List;

import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulate;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulationUtils;
import org.springframework.roo.model.JavaType;
import org.talframework.objexj.annotations.ObjexProp;
import org.talframework.objexj.generator.roo.utils.TypeConstants;
import org.talframework.objexj.generator.roo.utils.TypeDetailsUtil;

public class ObjexPropAnnotation {
    
    @AutoPopulate private String externalName = null;
    @AutoPopulate private JavaType exposeAs = null;
    @AutoPopulate private JavaType transformerClass = null;
    @AutoPopulate private String transformerGetFunction = null;
    @AutoPopulate private String transformerSetFunction = null;
    @AutoPopulate private boolean gettable = true;
    @AutoPopulate private boolean settable = true;

    /**
     * Helper to get the {@link ObjexProp} annotation and its values if
     * it exists.
     * 
     * @param annotations The annotations
     * @return The {@link ObjexPropAnnotation} instance if it exists in the annotations
     */
    //@Trace
    public static ObjexPropAnnotation get(List<? extends AnnotationMetadata> annotations) {
        ObjexPropAnnotation ret = null;
        
        AnnotationMetadata am = TypeDetailsUtil.getAnnotation(annotations, ObjexProp.class.getName());
        if( am != null ) ret = new ObjexPropAnnotation(am);
        
        return ret;
    }
    
    private ObjexPropAnnotation(AnnotationMetadata annotationMetadata) {
        AutoPopulationUtils.populate(this, annotationMetadata);
        
        if( "".equals(externalName) ) externalName = null;
        if( "".equals(transformerGetFunction) ) transformerSetFunction = null;
        if( "".equals(transformerSetFunction) ) transformerSetFunction = null;
        if( TypeConstants.OBJECT.equals(exposeAs) ) exposeAs = null;
        if( TypeConstants.OBJECT.equals(transformerClass) ) transformerClass = null;
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
     * @return the exposeAs
     */
    public JavaType getExposeAs() {
        return exposeAs;
    }

    /**
     * Setter for the exposeAs field
     *
     * @param exposeAs the exposeAs to set
     */
    public void setExposeAs(JavaType exposeAs) {
        this.exposeAs = exposeAs;
    }

    /**
     * @return the transformerClass
     */
    public JavaType getTransformerClass() {
        return transformerClass;
    }

    /**
     * Setter for the transformerClass field
     *
     * @param transformerClass the transformerClass to set
     */
    public void setTransformerClass(JavaType transformerClass) {
        this.transformerClass = transformerClass;
    }

    /**
     * @return the transformerGetFunction
     */
    public String getTransformerGetFunction() {
        return transformerGetFunction;
    }

    /**
     * Setter for the transformerGetFunction field
     *
     * @param transformerGetFunction the transformerGetFunction to set
     */
    public void setTransformerGetFunction(String transformerGetFunction) {
        this.transformerGetFunction = transformerGetFunction;
    }

    /**
     * @return the transformerSetFunction
     */
    public String getTransformerSetFunction() {
        return transformerSetFunction;
    }

    /**
     * Setter for the transformerSetFunction field
     *
     * @param transformerSetFunction the transformerSetFunction to set
     */
    public void setTransformerSetFunction(String transformerSetFunction) {
        this.transformerSetFunction = transformerSetFunction;
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
