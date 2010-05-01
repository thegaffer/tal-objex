package org.tpspencer.tal.objexj.sample.api.stock;


/**
 * Adds the business operations on to the core state.
 * This includes
 * 
 * @author Tom Spencer
 */
public interface Product {

	/**
	 * Call to get the products raw state. The return is
	 * effectively a detached copy and no changes made to
	 * the return will be persisted or remembered unless
	 * a call is made to setProductState. 
	 * 
	 * @return The products core state
	 */
	public ProductState getProductState();
	
	/**
	 * Call to set the products state. Every attempt is
	 * made to set the state even if invalid.
	 * 
	 * @param product The new state to set
	 */
	public void setProductState(ProductState product);
	
	// TODO: Standard getErrors method
}
