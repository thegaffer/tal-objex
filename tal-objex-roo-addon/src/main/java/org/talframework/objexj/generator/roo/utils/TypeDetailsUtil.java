package org.talframework.objexj.generator.roo.utils;

import java.util.Iterator;
import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.annotations.AnnotatedJavaType;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.model.JavaType;

/**
 * This class contains various common utils for detail with
 * TypeDetails
 * 
 * @author Tom Spencer
 */
public class TypeDetailsUtil {
    
    /**
     * Determines if the type implements a particular type already
     * 
     * @param details The type to check
     * @param type The interface
     * @return True if it does implement the interface
     */
    public static boolean isImplementing(ClassOrInterfaceTypeDetails details, String type) {
        List<JavaType> impls = details.getImplementsTypes();
        if( impls == null || impls.size() == 0 ) return false;
        
        boolean bFound = false;
        Iterator<JavaType> it = impls.iterator();
        while( it.hasNext() && !bFound ) {
            if( it.next().getFullyQualifiedTypeName().equals(type) ) bFound = true;
        }
        
        return bFound;
    }
    
    /**
     * Determines if the type extends a particular type already
     * 
     * @param details The type to check
     * @param type The class
     * @return True if it does extend the class
     */
    public static boolean isExtending(ClassOrInterfaceTypeDetails details, String type) {
        List<JavaType> exts = details.getExtendsTypes();
        if( exts == null || exts.size() == 0 ) return false;
        
        boolean bFound = false;
        Iterator<JavaType> it = exts.iterator();
        while( it.hasNext() && !bFound ) {
            if( it.next().getFullyQualifiedTypeName().equals(type) ) bFound = true;
        }
        
        return bFound;
    }

    /**
     * Call to find an particular annotation on the given type.
     * 
     * @param details The type to check
     * @param annotation The annotation (fully qualified) to get
     * @return The metadata for the annotation if found
     */
    public static AnnotationMetadata getAnnotation(ClassOrInterfaceTypeDetails details, String annotation) {
        return getAnnotation(details.getTypeAnnotations(), annotation);
    }
    
    /**
     * Call to find an particular annotation from a list of annotations
     * 
     * @param annotations The list of annotations to check
     * @param annotation The annotation (fully qualified) to get
     * @return The metadata for the annotation if found
     */
    public static AnnotationMetadata getAnnotation(List<? extends AnnotationMetadata> annotations, String annotation) {
        AnnotationMetadata ret = null;
        if( annotations == null || annotations.size() == 0 ) return ret;
        
        JavaType annotationType = new JavaType(annotation);
        Iterator<? extends AnnotationMetadata> it = annotations.iterator();
        while( it.hasNext() && ret == null ) {
            AnnotationMetadata am = it.next();
            if( am.getAnnotationType().equals(annotationType) ) ret = am;
        }
        
        return ret;
    }
    
    /**
     * Call to find a particular field on the given type.
     * 
     * @param details The type to check
     * @param fieldName The name of the field
     * @return The field metadata if found
     */
    public static FieldMetadata getField(ClassOrInterfaceTypeDetails details, String fieldName) {
        FieldMetadata ret = null;
        
        List<? extends FieldMetadata> fields = details.getDeclaredFields();
        Iterator<? extends FieldMetadata> it = fields.iterator();
        while( it.hasNext() && ret == null ) {
            FieldMetadata fm = it.next();
            if( fm.getFieldName().getSymbolName().equals(fieldName) ) ret = fm;
        }
        
        return ret;
    }
    
    /**
     * Call to find a particular method on the given type.
     * 
     * @param details The type to check
     * @param methodName The name of the method
     * @param paramTypes The parameter types for method
     * @return The method metadata if found
     */
    public static MethodMetadata getMethod(ClassOrInterfaceTypeDetails details, String methodName, List<AnnotatedJavaType> paramTypes) {
        MethodMetadata ret = null;
        
        List<? extends MethodMetadata> methods = details.getDeclaredMethods();
        Iterator<? extends MethodMetadata> it = methods.iterator();
        while( it.hasNext() && ret == null ) {
            MethodMetadata mm = it.next();
            
            if( !mm.getMethodName().getSymbolName().equals(methodName) ) continue;
    
            List<AnnotatedJavaType> pt = mm.getParameterTypes();
            if( paramTypes == null ) {
                if( pt != null && pt.size() != 0 ) continue;
            }
            else {
                if( pt.size() != paramTypes.size() ) continue;

                boolean match = true;
                for( int i = 0 ; i < paramTypes.size() && match ; i++ ) {
                    if( !paramTypes.get(i).equals(pt.get(i)) ) match = false;
                }
                
                if( !match ) continue;
            }
            
            // If we get here we have a match
            ret = mm;
        }
        
        return ret;
    }
}
