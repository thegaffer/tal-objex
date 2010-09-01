package org.tpspencer.tal.objexj.object;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexIDStrategy;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.container.InternalContainer;

/**
 * Very basic implementation of ObjectStrategy that requires
 * minimal setup. In many situations this class will be fine.
 * The name of the object will be set from the simple name of
 * the state class, the parentIdProp will be "parentId" and
 * we will use SimpleObjexObj as the class for objects.
 * 
 * TODO: Make reference property map based on annotations in state bean
 * 
 * @author Tom Spencer
 */
public final class SimpleObjectStrategy implements ObjectStrategy {
	
	/** Holds the name of the object */
	private String name;
	/** Holds the ID Strategy */
	private ObjexIDStrategy idStrategy;
	/** Holds the class that holds the state */
    private Class<? extends ObjexObjStateBean> stateClass;
    /** Holds the raw class for the main objex object */
    private Class<? extends InternalObjexObj> objexClass;
    /** Holds the optional set of reference properties on the class */
    private Map<String, Class<?>> referenceProps;
    /** Holds the optional super-set of reference properties that are considered owned */
    private Map<String, Class<?>> ownedReferenceProps;
    
    /** Holds the constructor for the object object to instantiate */
    private Constructor<? extends InternalObjexObj> objexClassConstructor;
	
    /**
     * Default constructor. Client must call init explicitly
     * if using this constructor.
     */
    public SimpleObjectStrategy() {
    }
	
    /**
     * Constructs an object strategy using a given state class and
     * the default ObjexObj. This constructor initialises the strategy
     * 
     * @param stateClass The state class to use
     */
	public SimpleObjectStrategy(Class<? extends ObjexObjStateBean> stateClass) {
		this.name = stateClass.getSimpleName();
		this.stateClass = stateClass;
		this.objexClass = null;
		
		init();
	}
	
	/**
     * Constructs an object strategy using a given state class and
     * the ObjexObj class. This constructor initialises the strategy
     * 
     * @param stateClass The state class to use
     */
    public SimpleObjectStrategy(String name, Class<? extends InternalObjexObj> objexClass, Class<? extends ObjexObjStateBean> stateClass) {
	    this.name = name;
	    this.stateClass = stateClass;
        this.objexClass = objexClass;
        
        init();
    }
	
	/**
	 * Initialises the strategy object.
	 * This is called automatically if the non-default constructor is used.
	 */
	public void init() {
	    this.objexClassConstructor = determineObjexConstructor(objexClass);
	}
	
	/**
	 * Helper to determine the constructor for the objex class
	 */
	@SuppressWarnings("unchecked")
    private Constructor<? extends InternalObjexObj> determineObjexConstructor(Class<? extends InternalObjexObj> objexClass) {
        if( objexClass == null ) return null;
        
        try {
            Constructor<? extends InternalObjexObj> ret = null;
            
            Constructor<? extends InternalObjexObj>[] cons = (Constructor<? extends InternalObjexObj>[])objexClass.getConstructors();
            for( Constructor<? extends InternalObjexObj> constructor : cons ) {
                Class<?>[] params = constructor.getParameterTypes();
                if( params != null && params.length == 1 ) {
                    if( ObjexObjStateBean.class.isAssignableFrom(params[0]) ) {
                        ret = constructor;
                    }
                }
            }
            
            if( ret == null ) throw new IllegalArgumentException("Cannot use the object class provided has it has no valid constructor: " + objexClass);
            return ret;
        }
        catch( Exception e ) {
            throw new IllegalArgumentException("Cannot use the object class provided has it has no valid constructor: " + objexClass, e);
        }
    }

	/**
	 * Returns the name
	 */
	public String getTypeName() {
		return name;
	}
	
	/**
	 * @param name The name
	 */
	public void setTypeName(String name) {
        this.name = name;
    }
	
	/**
	 * Returns the ID strategy
	 */
	public ObjexIDStrategy getIdStrategy() {
	    return idStrategy;
	}
	
	/**
	 * Call to set the ID strategy
	 * 
	 * @param idStrategy
	 */
	public void setIdStrategy(ObjexIDStrategy idStrategy) {
        this.idStrategy = idStrategy;
    }
	
	/**
     * @return the objexClass
     */
    public Class<? extends InternalObjexObj> getObjexClass() {
        return objexClass;
    }

    /**
     * @param objexClass the objexClass to set
     */
    public void setObjexClass(Class<? extends InternalObjexObj> objexClass) {
        this.objexClass = objexClass;
    }

    /**
     * Simply returns the configured state class
     */
    public Class<? extends ObjexObjStateBean> getStateClass() {
        return stateClass;
    }
    
    /**
     * @param stateClass the stateClass to set
     */
    public void setStateClass(Class<? extends ObjexObjStateBean> stateClass) {
        this.stateClass = stateClass;
    }

    /**
	 * Returns an instance of SimpleObjexObj around this class
	 */
	public ObjexObj getObjexObjInstance(InternalContainer container, ObjexID parent, ObjexID id, ObjexObjStateBean state) {
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
	 * Simply returns the map of reference properties
	 */
	public Map<String, Class<?>> getReferenceProperties() {
	    return referenceProps;
	}
	
	/**
	 * @param referenceProps The map of reference props to set
	 */
	public void setReferenceProperties(Map<String, Class<?>> referenceProps) {
        this.referenceProps = referenceProps;
    }
	
	/**
     * Simply returns the map of reference properties
     */
    public Map<String, Class<?>> getOwnedReferenceProperties() {
        return ownedReferenceProps;
    }
    
    /**
     * @param referenceProps The map of reference props to set
     */
    public void setOwnedReferenceProperties(Map<String, Class<?>> referenceProps) {
        this.ownedReferenceProps = referenceProps;
    }
}
