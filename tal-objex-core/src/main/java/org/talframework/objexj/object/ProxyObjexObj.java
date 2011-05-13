package org.talframework.objexj.object;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.validation.Valid;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.ObjectStrategy;

/**
 * This class implements the invocation handler to make a non-ObjexObj
 * business object appear and behave as an ObjexObj.
 *
 * @author Tom Spencer
 */
public class ProxyObjexObj extends BaseObjexObj implements InvocationHandler {

    /** The strategy for this object */
    private final ObjectStrategy strategy;
    /** Holds the real object */
    private final Object realObject;
    
    public ProxyObjexObj(ObjectStrategy strategy, Object realObject) {
        this.strategy = strategy;
        this.realObject = realObject;
    }
    
    /**
     * Dispatches the invoked methods as applicable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = null;
        
        // If ObjexObj (or InternalObjexObj), invoke on us
        if( method.getDeclaringClass().equals(ObjexObj.class) ) {
            // Get behaviour is different because we still want to run off the proxy object
            if( method.getName().equals("getBehaviour") ) {
                Class<?> behaviour = (Class<?>)args[0]; 
                if( behaviour.isInstance(proxy) ) return behaviour.cast(proxy);
                else throw new ClassCastException("The behaviour is not supported by this object");
            }
            else {
                ret = method.invoke(this, args);
            }
        }
        else if( method.getDeclaringClass().equals(InternalObjexObj.class) ) {
            ret = method.invoke(this, args);
        }
        
        // Otherwise invoke on the real object
        else {
            // Ensure if there are side effects to the method the container is open
            // Right now if it is a getter, then assume no side-effect!!
            if( args != null && args.length > 0 && 
                    (!method.getName().startsWith("get") || 
                     !method.getName().startsWith("is")) ) {
                checkUpdateable();
            }
            
            // FUTURE: Add annotations to allow side effect methods to be marked (or better still non-sideeffect methods to be marked)
            
            ret = method.invoke(realObject, args);
        }
        
        return ret;
    }
    
    @Override
    public String getType() {
        checkInitialised();
        
        return strategy.getTypeName();
    }
    
    @Override
    public <T> T getBehaviour(Class<T> behaviour) {
        throw new RuntimeException("Cannot use getBehavour on proxy invocation directly");
    }
    
    @Override
    @Valid
    protected Object getStateBean() {
        return realObject;
    }
    
    @Override
    protected ObjectStrategy getStrategy() {
        return strategy;
    }
}
