package org.tpspencer.tal.objexj.locator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;

/**
 * This class is configured with all the factories. When a 
 * request comes in it will attempt to find the type of the
 * container as the first part of the ID string (before the
 * first \) and then use the appropriate factory.
 * 
 * @author Tom Spencer
 */
public final class SimpleContainerLocator implements ContainerFactory {
	
	/** The map of factories to use, keyed by type */
	private final Map<String, ContainerFactory> factories;
	
	/**
	 * Main constructor for {@link SimpleContainerLocator}
	 * 
	 * @param factories The factories to use
	 */
	public SimpleContainerLocator(Map<String, ContainerFactory> factories) {
		Assert.notEmpty(factories, "You must supply valid factories for the container locator");
		
		this.factories = factories;
	}
	
	/**
	 * Simply finds the type of the ID and attempts to
	 * locate the {@link ContainerFactory} for that type
	 * and use it.
	 */
	public Container get(String id) {
		Container ret = null;
		String type = getType(id);
		if( type != null ) ret = factories.get(type).get(id);
		
		// TODO: Probably an error if no type or no container
		
		return ret;
	}
	
	public EditableContainer create(String type, Object rootObject) {
		EditableContainer ret = factories.get(type).create(type, rootObject);
		
		// TODO: Probably an error if no type or no container
		
		return ret;
	}
	
	/**
	 * Simply finds the type of the ID and attempts to
	 * locate the {@link ContainerFactory} for that type
	 * and use it.
	 */
	public EditableContainer get(String id, String transactionId) {
		EditableContainer ret = null;
		String type = getType(id);
		if( type != null ) ret = factories.get(type).get(id, transactionId);
		
		// TODO: Probably an error if no type or no container
		
		return ret;
	}
	
	/**
	 * Simply finds the type of the ID and attempts to
	 * locate the {@link ContainerFactory} for that type
	 * and use it.
	 */
	public EditableContainer open(String id) {
		EditableContainer ret = null;
		String type = getType(id);
		if( type != null ) ret = factories.get(type).open(id);
		
		// TODO: Probably an error if no type or no container
		
		return ret;
	}
	
	/**
	 * Helper to extract the type of container from the ID.
	 * This will only return a value if the type is also a
	 * valid container type for which we have a factory for.
	 * 
	 * @param id The ID of the container
	 * @return Its valid type or null
	 */
	private String getType(String id) {
		int index = id.indexOf('/');
		if( index < 0 ) index = id.indexOf('\\');
		
		String type = null;
		if( index > 0 ) type = id.substring(0, index);
		
		// Ensure type is valid even if given
		if( type == null || factories == null || !factories.containsKey(type) )
			type = null;
		
		return type;
	}
}
