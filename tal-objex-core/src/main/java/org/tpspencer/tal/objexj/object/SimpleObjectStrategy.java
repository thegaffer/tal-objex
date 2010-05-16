package org.tpspencer.tal.objexj.object;

import java.lang.reflect.Constructor;

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
	private final Class<? extends ObjexObj> objexClass;
	private final Constructor<? extends ObjexObj> objexClassConstructor;
	
	public SimpleObjectStrategy(Class<? extends ObjexObjStateBean> stateClass) {
		this.name = stateClass.getSimpleName();
		this.idProp = "id";
		this.parentIdProp = "parentId";
		this.stateClass = stateClass;
		this.objexClass = null;
		this.objexClassConstructor = null;
	}
	
	public SimpleObjectStrategy(String name, Class<? extends ObjexObj> objexClass, Class<? extends ObjexObjStateBean> stateClass) {
	    this.name = name;
	    this.idProp = "id";
        this.parentIdProp = "parentId";
        this.stateClass = stateClass;
        this.objexClass = objexClass;
    
        if( this.objexClass != null ) {
            try {
                objexClassConstructor = this.objexClass.getConstructor(ObjectStrategy.class, Container.class, ObjexID.class, ObjexID.class, Object.class);
                if( objexClassConstructor == null ) throw new IllegalArgumentException("Cannot used the object class provided has it has no valid constructor: " + objexClass);
            }
            catch( Exception e ) {
                throw new IllegalArgumentException("Cannot used the object class provided has it has no valid constructor: " + objexClass);
            }
        }
        else {
            objexClassConstructor = null;
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
	public ObjexObj getObjexObjInstance(Container container, ObjexID parent, ObjexID id, Object state) {
	    if( objexClassConstructor != null ) {
	        try {
	            return objexClassConstructor.newInstance(this, container, id, parent, state);
	        }
	        catch( Exception e ) {
	            throw new IllegalArgumentException("Cannot create objex obj likely a bad configuration argument: " + objexClass, e);
	        }
	    }
	    else {
	        return new SimpleObjexObj(this, container, id, parent, state);
	    }
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
	public ObjexObjStateBean getNewStateInstance() {
		try {
			return (ObjexObjStateBean)stateClass.newInstance();
		}
		catch( RuntimeException e ) {
			throw e;
		}
		catch( Exception e ) {
			throw new IllegalStateException("Cannot create new state object: " + stateClass, e);
		}
	}
}
