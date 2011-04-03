package org.talframework.objexj.object;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.ValidationError;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.exceptions.ObjectFieldInvalidException;
import org.talframework.objexj.validation.SimpleValidationRequest;
import org.talframework.objexj.validation.groups.ChildGroup;
import org.talframework.objexj.validation.groups.FieldGroup;
import org.talframework.objexj.validation.groups.InterObjectEnrichmentGroup;
import org.talframework.objexj.validation.groups.InterObjectGroup;
import org.talframework.objexj.validation.groups.IntraObjectEnrichmentGroup;
import org.talframework.objexj.validation.groups.IntraObjectGroup;
import org.talframework.util.beans.BeanDefinition;
import org.talframework.util.beans.definition.BeanDefinitionsSingleton;

/**
 * This class implements the invocation handler to make a non-ObjexObj
 * business object appear and behave as an ObjexObj.
 *
 * @author Tom Spencer
 */
public class ProxyObjexObj implements InvocationHandler, ObjexObj {

    /** The strategy for this object */
    private final ObjectStrategy strategy;
    
    /** The container that the object belongs to */
    private InternalContainer container;
    /** ID of the object */
    private ObjexID id;
    /** ID of this object parent (or null if this is the root object) */
    private ObjexID parentId;
    
    /** Cached parent object */
    private ObjexObj parent; 
    
    /** Holds the real object */
    private final Object realObject;
    
    public ProxyObjexObj(ObjectStrategy strategy, Object realObject) {
        this.strategy = strategy;
        this.realObject = realObject;
    }
    
    /**
     * The main initialisation called by the framework code to initialise
     * an ObjexObj. Failure to do so will result in a number of errors.
     * 
     * @param container The container
     * @param id The ID of the object
     * @param parentId The parentID of the object
     */
    public void init(InternalContainer container, ObjexID id, ObjexID parentId) {
        if( container == null ) throw new IllegalArgumentException("Cannot create object without a container");
        if( id == null || id.isNull() ) throw new IllegalArgumentException("Cannot create object without a valid id");
        
        this.container = container;
        this.id = id;
        this.parentId = parentId;
    }
    
    /**
     * Dispatches the invoked methods as applicable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = null;
        
        // If ObjexObj, invoke on us
        if( method.getDeclaringClass().equals(ObjexObj.class) ) {
            // Get behaviour is different because we still want to run off the proxy object
            if( method.getName().equals("getBehaviour") ) {
                Class<?> behaviour = (Class<?>)args[0]; 
                if( behaviour.isInstance(this) ) return behaviour.cast(this);
                else throw new ClassCastException("The behaviour is not supported by this object");
            }
            else {
                ret = method.invoke(this, args);
            }
        }
        
        // Otherwise invoke on the real object
        else {
            // TODO: Add in ability to prevent methods with side effects being call if container not open
            ret = method.invoke(realObject, args);
        }
        
        return ret;
    }
    
    public ObjexID getId() {
        checkInitialised();
        
        return id;
    }
    
    public ObjexID getParentId() {
        checkInitialised();
        
        return parentId;
    }
    
    public String getType() {
        checkInitialised();
        
        return strategy.getTypeName();
    }
    
    public ObjexObj getParent() {
        checkInitialised();
        
        if( parent == null && parentId != null ) {
            parent = container.getObject(parentId);
        }
            
        return parent;
    }
    
    public ObjexObj getRoot() {
        checkInitialised();
        
        return container.getRootObject();
    }
    
    public Container getContainer() {
        checkInitialised();
        
        return container;
    }
    
    public <T> T getBehaviour(Class<T> behaviour) {
        throw new RuntimeException("Cannot use getBehavour on proxy invocation directly");
    }
    
    /**
     * TODO: This should be abstracted, also should realObject be held as template arg (T) for this method!!
     */
    public void validate(ValidationRequest request) {
        Validator validator = request.getValidator();
        
        Set<ConstraintViolation<Object>> violations = null;
        
        switch(request.getValidationType()) {
        case INTRA_OBJECT:
            validator.validate(realObject, IntraObjectEnrichmentGroup.class);
            violations = validator.validate(realObject, IntraObjectGroup.class, FieldGroup.class, Default.class);
            break;
            
        case INTER_OBJECT:
            validator.validate(realObject, InterObjectEnrichmentGroup.class);
            violations = validator.validate(realObject, InterObjectGroup.class);
            break;
            
        case CHILDREN:
            violations = validator.validate(realObject, ChildGroup.class);
            break;
        }
        
        // Turn violations into errors
        if( violations != null ) {
            Iterator<ConstraintViolation<Object>> it = violations.iterator();
            while( it.hasNext() ) {
                ConstraintViolation<Object> violation = it.next();
                
                // TODO: Need to test no prop path = non-field error
                String prop = violation.getPropertyPath().toString();
                if( prop != null && prop.startsWith("stateBean.") ) prop = prop.substring(5);
                else if( prop != null && prop.equals("stateBean") ) prop = null;
                else if( prop != null && prop.length() == 0 ) prop = null;
                
                Object[] params = violation.getInvalidValue() != null ? new Object[]{violation.getInvalidValue()} : null;
                
                ValidationError error = new SimpleValidationRequest.SimpleValidationError(id, violation.getMessageTemplate(), prop, params);
                request.addError(error);
            }
        }
    }
    
    public Object getProperty(String name) {
        checkInitialised();
        
        String realName = convertReferencePropertyName(name);
        
        BeanDefinition def = BeanDefinitionsSingleton.getInstance().getDefinition(realObject.getClass());
        Object ret = null;
        if( def.hasProperty(realName) && def.canRead(realName) ) ret = def.read(realObject, realName);
        
        // TODO: Attempt to Clone value?? (Not sure this is really worth it. It can be broken easily so why bother)
        
        return ret;
    }
    
    public <T> T getProperty(String name, Class<T> expected) {
        Object val = getProperty(name);
        return expected.cast(val);
    }
    
    public String getPropertyAsString(String name) {
        Object obj = getProperty(name);
        return obj != null ? obj.toString() : null;
    }
    
    public void setProperty(String name, Object newValue) {
        checkUpdateable();
        
        String realName = convertReferencePropertyName(name);
        BeanDefinition def = BeanDefinitionsSingleton.getInstance().getDefinition(realObject.getClass());
        if( def.hasProperty(realName) ) throw new ObjectFieldInvalidException(name, "field does not exist");
        if( def.canWrite(realName) ) throw new ObjectFieldInvalidException(name, "No write method available"); 
        def.write(realObject, realName, newValue);
    }
    
    public ObjexObj createReference(String name, String type) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void acceptReader(ObjexStateReader reader) {
        // TODO Auto-generated method stub
        
    }
    
    public void acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
        // TODO Auto-generated method stub
        
    }
    
    /////////////////////////////////////////////////
    // Internal
    
    /**
     * Helper method to ensure this object is initialised as
     * an ObjexObj - if not throws an {@link IllegalStateException}.
     * 
     * @throws IllegalStateException If not initialised
     */
    protected void checkInitialised() {
        if( container == null ) throw new IllegalStateException("The object has not been initialised: " + this); 
    }
    
    /**
     * Ensures the object is updateable before continuing
     * 
     * @throws IllegalStateException If not editable
     */
    protected void checkUpdateable() {
        checkInitialised();
        if( !isUpdateable() ) throw new IllegalStateException("Cannot update an object that is not inside a transaction: " + this);
    }
    
    /**
     * Simply checks if we are in an Editable Container
     */
    public boolean isUpdateable() {
        checkInitialised();
        return container.isOpen();
    }
    
    /**
     * This method converts the name of the requested property
     * when the request prop is a reference property suffixed 
     * with Ref. This means the client wants just the raw 
     * reference value (scalar, list or map).
     * 
     * @param name The original name of the property 
     * @return The real property name
     */
    protected String convertReferencePropertyName(String name) {
        String ret = name;
        
        Map<String, Class<?>> refProps = strategy.getReferenceProperties();
        if( refProps != null && (name.endsWith("Ref") && name.length() > 3) ) {
            String possibleRef = name.substring(0, name.length() - 3);
            if( refProps.containsKey(possibleRef) ) {
                ret = possibleRef;
            }
        }
        
        return ret;
    }
}
