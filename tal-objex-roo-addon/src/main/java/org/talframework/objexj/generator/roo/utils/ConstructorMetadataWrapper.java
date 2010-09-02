package org.talframework.objexj.generator.roo.utils;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.ConstructorMetadata;
import org.springframework.roo.classpath.details.DefaultConstructorMetadata;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotatedJavaType;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;

public class ConstructorMetadataWrapper {

    /** The public modifier for the operation */
    private int modifier = Modifier.PUBLIC;
    /** The annotations for the method */
    private List<AnnotationMetadata> annotations = null;
    /** The parameters for the method */
    private List<MethodParameter> parameters = null;
    /** The body of the method */
    private InvocableMemberBodyBuilder bodyBuilder = null;
    
    /**
     * @return the modifier
     */
    public int getModifier() {
        return modifier;
    }
    
    public void addModifier(int modifier) {
        this.modifier = this.modifier & modifier;
    }

    /**
     * @param modifier the modifier to set
     */
    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    /**
     * @return the annotations
     */
    public List<AnnotationMetadata> getAnnotations() {
        if( annotations == null ) return new ArrayList<AnnotationMetadata>();
        return annotations;
    }
    
    /**
     * Helper to add an annotation over this type.
     * 
     * @param am The annotation
     */
    public void addAnnotation(AnnotationMetadata am) {
        if( this.annotations == null ) this.annotations = new ArrayList<AnnotationMetadata>();
        this.annotations.add(am);
    }

    /**
     * @param annotations the annotations to set
     */
    public void setAnnotations(List<AnnotationMetadata> annotations) {
        this.annotations = annotations;
    }

    /**
     * @return the parameters
     */
    public List<JavaSymbolName> getParameterNames() {
        List<JavaSymbolName> ret = new ArrayList<JavaSymbolName>();
        
        if( parameters != null ) {
            Iterator<MethodParameter> it = parameters.iterator();
            while( it.hasNext() ) {
                ret.add(it.next().getName());
            }
        }
        
        return ret;
    }
    
    /**
     * @return the parameters
     */
    public List<AnnotatedJavaType> getParameterTypes() {
        List<AnnotatedJavaType> ret = new ArrayList<AnnotatedJavaType>();
        
        if( parameters != null ) {
            Iterator<MethodParameter> it = parameters.iterator();
            while( it.hasNext() ) {
                ret.add(it.next().getType());
            }
        }
        
        return ret;
    }
    
    /**
     * Adds a parameter to this method
     * 
     * @param name
     * @param type
     * @param annotations
     */
    public void addParameter(String name, String type, List<AnnotationMetadata> annotations) {
        addParameter(new JavaSymbolName(name), new JavaType(type), annotations);
    }
    
    /**
     * Adds a parameter to this method
     * 
     * @param name
     * @param type
     * @param annotations
     */
    public void addParameter(JavaSymbolName name, JavaType type, List<AnnotationMetadata> annotations) {
        if( this.parameters == null ) this.parameters = new ArrayList<MethodParameter>();
        this.parameters.add(new MethodParameter(name, type, annotations));
    }

    public void addBody(String body) {
        if( bodyBuilder == null ) bodyBuilder = new InvocableMemberBodyBuilder();
        bodyBuilder.appendFormalLine(body);
    }

    /**
     * Call to add the metadata to the builder for this field. 
     * 
     * @param builder The builder to add it
     * @param typeDetails The details of the type
     * @param typeId The ID of the type
     */
    public void addMetadata(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        List<? extends ConstructorMetadata> constructors = typeDetails.getDeclaredConstructors();
        
        List<AnnotatedJavaType> expectedParams = getParameterTypes();
        ConstructorMetadata ret = null;
        if( constructors != null ) {
            Iterator<? extends ConstructorMetadata> it = constructors.iterator();
            while( it.hasNext() && ret == null ) {
                ConstructorMetadata cm = it.next();
                
                List<AnnotatedJavaType> params = cm.getParameterTypes();
                if( params.size() != expectedParams.size() ) continue;
                else {
                    boolean match = true;
                    for( int i = 0 ; i < params.size() ; i++ ) {
                        AnnotatedJavaType p = params.get(i);
                        AnnotatedJavaType ep = expectedParams.get(i);
                        if( !p.equals(ep) ) {
                            match = false;
                            break;
                        }
                    }
                    
                    if( match ) ret = cm;
                }
            }
        }
        
        if( ret == null ) {
            String body = bodyBuilder != null ? bodyBuilder.getOutput() : null;
            ret = new DefaultConstructorMetadata(typeId, modifier, expectedParams, getParameterNames(), getAnnotations(), body);
        }
        
        builder.addConstructor(ret);
    }
    
    private static class MethodParameter {
        private final JavaSymbolName name;
        private final AnnotatedJavaType type;
        
        public MethodParameter(JavaSymbolName name, JavaType type, List<AnnotationMetadata> annotations) {
            this.name = name;
            this.type = new AnnotatedJavaType(type, annotations);
        }

        /**
         * @return the name
         */
        public JavaSymbolName getName() {
            return name;
        }

        /**
         * @return the type
         */
        public AnnotatedJavaType getType() {
            return type;
        }
    }
}
