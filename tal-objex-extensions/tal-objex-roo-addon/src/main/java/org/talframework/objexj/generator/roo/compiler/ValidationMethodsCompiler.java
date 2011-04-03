package org.talframework.objexj.generator.roo.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.model.JavaType;
import org.talframework.objexj.generator.roo.annotations.ObjexCheckAnnotation;
import org.talframework.objexj.generator.roo.annotations.ObjexEnrichAnnotation;

/**
 * This class produces a list of all @ObjexCheck and 
 * @ObjexEnrich annotated methods.
 *
 * @author Tom Spencer
 */
public class ValidationMethodsCompiler {

    /** Holds all intra-object enrich methods */
    private List<MethodMetadata> intraObjectEnrichMethods;
    /** Holds all intra-object validate methods */
    private Map<String, MethodMetadata> intraObjectValidateMethods;
    /** Holds all inter-object enrich methods */
    private List<MethodMetadata> interObjectEnrichMethods;
    /** Holds all inter-object validate methods */
    private Map<String, MethodMetadata> interObjectValidateMethods;
    /** Holds all child validate methods */
    private Map<String, MethodMetadata> childValidateMethods;
    
    /**
     * Compiles all the custom validate and enrich methods
     * 
     * @param type The type to get details for
     */
    //@Trace
    public void compile(ClassOrInterfaceTypeDetails type) {
        List<? extends MethodMetadata> methods = type.getDeclaredMethods();
        if( methods == null || methods.size() == 0 ) return;
        
        Iterator<? extends MethodMetadata> it = methods.iterator();
        while( it.hasNext() ) {
            MethodMetadata method = it.next();
            
            // See if check
            ObjexCheckAnnotation check = ObjexCheckAnnotation.get(method.getAnnotations());
            if( check != null ) {
                if( !checkNoArguments(method) ) continue;
                if( !checkBooleanReturn(method) ) continue;
                
                if( check.isChildObjectCheck() ) addChildValidateMethod(check.getMessage(), method);
                else if( check.isPostObjectCheck() ) addInterObjectValidateMethod(check.getMessage(), method);
                else addIntraObjectValidateMethod(check.getMessage(), method);
                
                continue;
            }
            
            // See if enrich
            ObjexEnrichAnnotation enrich = ObjexEnrichAnnotation.get(method.getAnnotations());
            if( enrich != null ) {
                if( !checkNoArguments(method) ) break;
                
                if( enrich.isPostObjectCheck() ) addInterObjectEnrichMethod(method);
                else addIntraObjectEnrichMethod(method);
                
                continue;
            }
        }
    }
    
    /**
     * Internal helper to check the method on the business object
     * takes no arguments.
     */
    //@Trace
    private boolean checkNoArguments(MethodMetadata method) {
        if( method.getParameterNames() != null && method.getParameterNames().size() > 0 ) return false;
        return true;
    }
    
    /**
     * Internal helper to check the method on the business object
     * returns a boolean
     */
    //@Trace
    private boolean checkBooleanReturn(MethodMetadata method) {
        if( JavaType.BOOLEAN_PRIMITIVE.equals(method.getReturnType()) ) return true;
        if( JavaType.BOOLEAN_OBJECT.equals(method.getReturnType()) ) return true;
        return false;
    }
    
    ///////////////////////////////////////////

    /**
     * @return the intraObjectEnrichMethods
     */
    public List<MethodMetadata> getIntraObjectEnrichMethods() {
        return intraObjectEnrichMethods;
    }

    /**
     * Setter for the intraObjectEnrichMethods field
     *
     * @param intraObjectEnrichMethods the intraObjectEnrichMethods to set
     */
    public void setIntraObjectEnrichMethods(List<MethodMetadata> intraObjectEnrichMethods) {
        this.intraObjectEnrichMethods = intraObjectEnrichMethods;
    }
    
    /**
     * Helper to add the validate method
     */
    //@Trace
    private void addIntraObjectEnrichMethod(MethodMetadata method) {
        if( this.intraObjectEnrichMethods == null ) this.intraObjectEnrichMethods = new ArrayList<MethodMetadata>();
        this.intraObjectEnrichMethods.add(method);
    }

    /**
     * @return the intraObjectValidateMethods
     */
    public Map<String, MethodMetadata> getIntraObjectValidateMethods() {
        return intraObjectValidateMethods;
    }

    /**
     * Setter for the intraObjectValidateMethods field
     *
     * @param intraObjectValidateMethods the intraObjectValidateMethods to set
     */
    public void setIntraObjectValidateMethods(Map<String, MethodMetadata> intraObjectValidateMethods) {
        this.intraObjectValidateMethods = intraObjectValidateMethods;
    }
    
    /**
     * Helper to add the validate method
     */
    //@Trace
    private void addIntraObjectValidateMethod(String message, MethodMetadata method) {
        if( this.intraObjectValidateMethods == null ) this.intraObjectValidateMethods = new HashMap<String, MethodMetadata>();
        this.intraObjectValidateMethods.put(message, method);
    }

    /**
     * @return the interObjectEnrichMethods
     */
    public List<MethodMetadata> getInterObjectEnrichMethods() {
        return interObjectEnrichMethods;
    }

    /**
     * Setter for the interObjectEnrichMethods field
     *
     * @param interObjectEnrichMethods the interObjectEnrichMethods to set
     */
    public void setInterObjectEnrichMethods(List<MethodMetadata> interObjectEnrichMethods) {
        this.interObjectEnrichMethods = interObjectEnrichMethods;
    }
    
    /**
     * Helper to add the validate method
     */
    //@Trace
    private void addInterObjectEnrichMethod(MethodMetadata method) {
        if( this.interObjectEnrichMethods == null ) this.interObjectEnrichMethods = new ArrayList<MethodMetadata>();
        this.interObjectEnrichMethods.add(method);
    }

    /**
     * @return the interObjectValidateMethods
     */
    public Map<String, MethodMetadata> getInterObjectValidateMethods() {
        return interObjectValidateMethods;
    }

    /**
     * Setter for the interObjectValidateMethods field
     *
     * @param interObjectValidateMethods the interObjectValidateMethods to set
     */
    public void setInterObjectValidateMethods(Map<String, MethodMetadata> interObjectValidateMethods) {
        this.interObjectValidateMethods = interObjectValidateMethods;
    }
    
    /**
     * Helper to add the validate method
     */
    //@Trace
    private void addInterObjectValidateMethod(String message, MethodMetadata method) {
        if( this.interObjectValidateMethods == null ) this.interObjectValidateMethods = new HashMap<String, MethodMetadata>();
        this.interObjectValidateMethods.put(message, method);
    }

    /**
     * @return the childValidateMethods
     */
    public Map<String, MethodMetadata> getChildValidateMethods() {
        return childValidateMethods;
    }

    /**
     * Setter for the childValidateMethods field
     *
     * @param childValidateMethods the childValidateMethods to set
     */
    public void setChildValidateMethods(Map<String, MethodMetadata> childValidateMethods) {
        this.childValidateMethods = childValidateMethods;
    }

    /**
     * Helper to add the validate method
     */
    //@Trace
    private void addChildValidateMethod(String message, MethodMetadata method) {
        if( this.childValidateMethods == null ) this.childValidateMethods = new HashMap<String, MethodMetadata>();
        this.childValidateMethods.put(message, method);
    }
}
