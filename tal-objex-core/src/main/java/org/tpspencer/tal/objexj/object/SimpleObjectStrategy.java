package org.tpspencer.tal.objexj.object;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;

/**
 * Very basic implementation of ObjectStrategy that requires
 * minimal setup. In many situations this class will be fine.
 * The name of the object will be set from the simple name of
 * the state class, the parentIdProp will be "parentId" and
 * we will use SimpleObjexObj as the class for objects.
 * 
 * TODO: Allow more customisation
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
	private final Class<?> stateClass;
	
	public SimpleObjectStrategy(Class<?> stateClass) {
		this.name = stateClass.getSimpleName();
		this.idProp = "id";
		this.parentIdProp = "parentId";
		this.stateClass = stateClass;
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
		return new SimpleObjexObj(this, container, id, parent, state);
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
	public Object getNewStateInstance() {
		try {
			return stateClass.newInstance();
		}
		catch( RuntimeException e ) {
			throw e;
		}
		catch( Exception e ) {
			throw new IllegalStateException("Cannot create new state object: " + stateClass, e);
		}
	}
}
