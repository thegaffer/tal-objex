package org.tpspencer.tal.objexj.object;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;

/**
 * Very basic implementation of ObjectStrategy that requires
 * minimal setup. In many situations this class will be fine.
 * The name of the object will be set from the simple name of
 * the state class, the parentIdProp will be "parentId" and
 * we will use SimpleObjexObj as the class for objects.
 * 
 * @author Tom Spencer
 */
public class SimpleObjectStrategy implements ObjectStrategy {
	
	/** Holds the name of the object */
	private final String name;
	/** Holds the name of the id property inside state object */
	private final String idProp;
	/** Holds the name of the parentId property inside state object */
	private final String parentIdProp;
	/** Holds the class that holds the state */
	private final Class<? extends ObjexObjStateBean> stateClass;
	/** Holds the constructor for the object object to instantiate */
    private final Constructor<? extends ObjexObjStateBean> stateClassConstructor;
    /** Holds the constructor for the object object to instantiate */
    private final Constructor<? extends ObjexObjStateBean> stateCopyConstructor;
	/** Holds the raw class for the main objex object */
	private final Class<? extends InternalObjexObj> objexClass;
	/** Holds the constructor for the object object to instantiate */
	private final Constructor<? extends InternalObjexObj> objexClassConstructor;
	
	public SimpleObjectStrategy(Class<? extends ObjexObjStateBean> stateClass) {
		this.name = stateClass.getSimpleName();
		this.idProp = "id";
		this.parentIdProp = "parentId";
		this.stateClass = stateClass;
		this.stateClassConstructor = determineStateConstructor(stateClass);
		this.stateCopyConstructor = determineCopyConstructor(stateClass);
		this.objexClass = null;
		this.objexClassConstructor = null;
	}
	
	public SimpleObjectStrategy(String name, Class<? extends InternalObjexObj> objexClass, Class<? extends ObjexObjStateBean> stateClass) {
	    this.name = name;
	    this.idProp = "id";
        this.parentIdProp = "parentId";
        this.stateClass = stateClass;
        this.stateClassConstructor = determineStateConstructor(stateClass);
        this.stateCopyConstructor = determineCopyConstructor(stateClass);
        this.objexClass = objexClass;
        this.objexClassConstructor = determineObjexConstructor(objexClass);
    }
	
	private Constructor<? extends ObjexObjStateBean> determineStateConstructor(Class<? extends ObjexObjStateBean> stateClass) {
	    try {
            Constructor<? extends ObjexObjStateBean> ret = stateClass.getConstructor(Object.class, Object.class);
            if( ret == null ) throw new IllegalArgumentException("Cannot use the object state class provided has it has no valid constructor: " + stateClass);
            return ret;
        }
        catch( Exception e ) {
            throw new IllegalArgumentException("Cannot use the object state class provided has it has no valid constructor: " + stateClass);
        }
	}
	
	private Constructor<? extends ObjexObjStateBean> determineCopyConstructor(Class<? extends ObjexObjStateBean> stateClass) {
        try {
            Constructor<? extends ObjexObjStateBean> ret = stateClass.getConstructor(stateClass);
            return ret;
        }
        catch( Exception e ) {
            return null;
        }
    }
	
	private Constructor<? extends InternalObjexObj> determineObjexConstructor(Class<? extends InternalObjexObj> objexClass) {
        if( objexClass == null ) return null;
        
        try {
            Constructor<? extends InternalObjexObj> ret = objexClass.getConstructor(ObjexObjStateBean.class);
            if( ret == null ) throw new IllegalArgumentException("Cannot use the object class provided has it has no valid constructor: " + objexClass);
            return ret;
        }
        catch( Exception e ) {
            throw new IllegalArgumentException("Cannot use the object class provided has it has no valid constructor: " + objexClass);
        }
    }

	/**
	 * Returns the name
	 */
	public String getTypeName() {
		return name;
	}
	
	/**
	 * Simply returns the configured id property name
	 */
	public String getIdProp() {
		return idProp;
	}
	
	/**
	 * Returns the configured parentId property name
	 */
	public String getParentIdProp() {
		return parentIdProp;
	}
	
	/**
	 * Returns an instance of SimpleObjexObj around this class
	 */
	public ObjexObj getObjexObjInstance(Container container, ObjexID parent, ObjexID id, ObjexObjStateBean state) {
	    InternalObjexObj ret = null;
	    if( objexClassConstructor != null ) {
	        try {
	            ret = objexClassConstructor.newInstance(state);
	        }
	        catch( Exception e ) {
	            throw new IllegalArgumentException("Cannot create objex obj likely a bad configuration argument: " + objexClass, e);
	        }
	    }
	    else {
	        ret = new SimpleObjexObj(this, state);
	    }
	    
	    if( ret != null ) {
	        ret.init(container, id, parent);
	    }
	    
	    return ret;
	}
	
	/**
	 * Simply returns the configured state class
	 */
	public Class<?> getStateClass() {
		return stateClass;
	}
	
	/**
	 * Simply creates a new instance from the stateClass member
	 */
	public ObjexObjStateBean getNewStateInstance(Object id, Object parentId) {
		try {
			return stateClassConstructor.newInstance(id, parentId);
		}
		catch( RuntimeException e ) {
			throw e;
		}
		catch( Exception e ) {
			throw new IllegalStateException("Cannot create new state object: " + stateClass, e);
		}
	}
	
	/**
     * Simply creates a new instance from the stateClass member
     */
    public ObjexObjStateBean getClonedStateInstance(ObjexObjStateBean source) {
       try {
           if( stateCopyConstructor != null ) {
                return stateCopyConstructor.newInstance(source);
           }
           else {
               ObjexObjStateBean ret = stateClass.newInstance();
               
               BeanWrapper copyWrapper = new BeanWrapperImpl(ret);
               BeanWrapper wrapper = new BeanWrapperImpl(source);
               PropertyDescriptor[] props = wrapper.getPropertyDescriptors();
               for( int i = 0 ; i < props.length ; i++ ) {
                   if( copyWrapper.isWritableProperty(props[i].getName()) ) {
                       copyWrapper.setPropertyValue(
                               props[i].getName(), 
                               wrapper.getPropertyValue(props[i].getName()));
                   }
               }
               
               return ret;
           }
        }
        catch( RuntimeException e ) {
            throw e;
        }
        catch( Exception e ) {
            throw new IllegalStateException("Cannot create new state object: " + stateClass, e);
        }
    }
}
