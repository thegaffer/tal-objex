package org.tpspencer.tal.objexj.object;

import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexObjStateBean;

/**
 * Base class for an ObjexObj. Most basic methods are implemented
 * but all can be overridden as required.
 * 
 * @author Tom Spencer
 */
public class SimpleObjexObj extends BaseObjexObj {

	/** The strategy for this object */
	private final ObjectStrategy strategy;
	
	/** Member holds the detail or state object */
	private ObjexObjStateBean state;
	
	public SimpleObjexObj(ObjectStrategy strategy, ObjexObjStateBean state) {
	    if( strategy == null ) throw new IllegalArgumentException("Cannot create object without a strategy");
		if( state == null ) throw new IllegalArgumentException("Cannot create object without a state object");
		
		this.strategy = strategy;
		this.state = state;
	}
	
	/**
	 * Default is simple name of outer class
	 */
	public String getType() {
		return strategy.getTypeName();
	}
	
	/**
	 * Returns either the state object directly or a copy
	 * depending on if in open editable container.
	 */
	public ObjexObjStateBean getStateObject() {
		if( isUpdateable() )
			return state;
		else
			return cloneState();
	}
	
	/**
	 * Helper that returns the state object without 
	 * cloning it. Can be used by the derived class.
	 * 
	 * @return The state object
	 */
	protected Object getLocalState() {
		return state;
	}
	
	/**
	 * Returns either the state object directly or a copy
	 * depending on if in open editable container.
	 */
	public <T> T getStateObject(Class<T> expected) {
		return expected.cast(getStateObject());
	}
	
	/**
	 * Internal helper to clone the state object which is
	 * done if we are not in an open {@link EditableContainer}.
	 * Default uses reflect to create a new instance of the
	 * state object and copy in every parameter.
	 * 
	 * <p>Derived class can override as neccessary</p>
	 * 
	 * @return The cloned version
	 */
	protected ObjexObjStateBean cloneState() {
		return strategy.getClonedStateInstance(state);
	}
}
