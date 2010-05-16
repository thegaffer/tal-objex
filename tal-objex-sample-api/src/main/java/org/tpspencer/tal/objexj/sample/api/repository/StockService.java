package org.tpspencer.tal.objexj.sample.api.repository;

/**
 * This service provides access to the stock repository.
 * There is only 1 stock repository, but it is possible
 * to have
 * 
 * @author Tom Spencer
 */
public interface StockService {

    /**
     * @return A read-only repository for the stock store
     */
	public StockRepository getRepository();
	
	/**
	 * @return An editable repository for the stock store
	 */
	public StockRepository getOpenRepository();
	
	/**
	 * @param id The existing transaction ID
	 * @return The open stock repository
	 */
	public StockRepository getOpenRepository(String id);
	
	/**
	 * Ensures the stock container has been created
	 */
	public void ensureCreated();
}
