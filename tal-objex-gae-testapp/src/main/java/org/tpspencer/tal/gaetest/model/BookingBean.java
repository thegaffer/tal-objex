package org.tpspencer.tal.gaetest.model;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class BookingBean {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY) 
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true") 
	private String id;
	
	private String account;
	private String collectionDetails;
	private String deliveryDetails;
	private String goodsDetails;
	private String serviceDetails;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}
	/**
	 * @return the collectionDetails
	 */
	public String getCollectionDetails() {
		return collectionDetails;
	}
	/**
	 * @param collectionDetails the collectionDetails to set
	 */
	public void setCollectionDetails(String collectionDetails) {
		this.collectionDetails = collectionDetails;
	}
	/**
	 * @return the deliveryDetails
	 */
	public String getDeliveryDetails() {
		return deliveryDetails;
	}
	/**
	 * @param deliveryDetails the deliveryDetails to set
	 */
	public void setDeliveryDetails(String deliveryDetails) {
		this.deliveryDetails = deliveryDetails;
	}
	/**
	 * @return the goodsDetails
	 */
	public String getGoodsDetails() {
		return goodsDetails;
	}
	/**
	 * @param goodsDetails the goodsDetails to set
	 */
	public void setGoodsDetails(String goodsDetails) {
		this.goodsDetails = goodsDetails;
	}
	/**
	 * @return the serviceDetails
	 */
	public String getServiceDetails() {
		return serviceDetails;
	}
	/**
	 * @param serviceDetails the serviceDetails to set
	 */
	public void setServiceDetails(String serviceDetails) {
		this.serviceDetails = serviceDetails;
	}
}
