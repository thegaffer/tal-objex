package org.tpspencer.tal.objexj.sample.model.order.impl;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexIDStrategy;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.container.ContainerMiddlewareFactory;
import org.tpspencer.tal.objexj.container.ContainerStrategy;
import org.tpspencer.tal.objexj.container.SimpleContainerStrategy;
import org.tpspencer.tal.objexj.locator.ContainerFactory;
import org.tpspencer.tal.objexj.locator.SimpleContainerFactory;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.sample.api.order.OrderSummary;
import org.tpspencer.tal.objexj.sample.api.repository.OrderRepository;
import org.tpspencer.tal.objexj.sample.api.repository.OrderService;
import org.tpspencer.tal.objexj.sample.beans.order.OrderBean;
import org.tpspencer.tal.objexj.sample.beans.order.OrderItemBean;

/**
 * This class implements the order service
 * 
 * @author Tom Spencer
 */
public class OrderServiceImpl implements OrderService {
    
    public static final ObjectStrategy ORDER_STRATEGY = new ObjectStrategy() {
        public String getTypeName() { return "OrderBean"; }
        public Class<? extends ObjexObjStateBean> getStateClass() { return OrderBean.class; }
        
        public ObjexObj getObjexObjInstance(Container container, ObjexID parent, ObjexID id, ObjexObjStateBean state) {
            OrderImpl ret = new OrderImpl((OrderBean)state);
            ret.init(container, id, parent);
            return ret;
        }
        
        public ObjexObjStateBean getNewStateInstance(ObjexID parentId) {
            return new OrderBean(parentId);
        }
        
        public ObjexObjStateBean getClonedStateInstance(ObjexObjStateBean source) {
            return new OrderBean((OrderBean)source);
        }
        
        public ObjexIDStrategy getIdStrategy() {
            return null;
        }
    }; 
    
    public static final ObjectStrategy ITEM_STRATEGY = new ObjectStrategy() {
        public String getTypeName() { return "OrderItemBean"; }
        public Class<? extends ObjexObjStateBean> getStateClass() { return OrderItemBean.class; }
        
        public ObjexObj getObjexObjInstance(Container container, ObjexID parent, ObjexID id, ObjexObjStateBean state) {
            OrderItemImpl ret = new OrderItemImpl((OrderItemBean)state);
            ret.init(container, id, parent);
            return ret;
        }
        
        public ObjexObjStateBean getNewStateInstance(ObjexID parentId) {
            return new OrderItemBean(parentId);
        }
        
        public ObjexObjStateBean getClonedStateInstance(ObjexObjStateBean source) {
            return new OrderItemBean((OrderItemBean)source);
        }
        
        public ObjexIDStrategy getIdStrategy() {
            return null;
        }
    }; 
	
	/** Holds the Objex factory for the order container type */
	private ContainerFactory locator;
	
	public OrderServiceImpl(ContainerFactory locator) {
        this.locator = locator;
    }
    
    public OrderServiceImpl(ContainerMiddlewareFactory middlewareFactory) {
        ObjectStrategy[] strategies = new ObjectStrategy[]{ORDER_STRATEGY, ITEM_STRATEGY};
        ContainerStrategy strategy = new SimpleContainerStrategy("Order", "OrderBean", strategies);
        
        locator = new SimpleContainerFactory(strategy, middlewareFactory);
    }

	/**
	 * Simply gets the container from the container factory
	 * and wraps it inside a new {@link OrderRepositoryImpl}
	 * instance.
	 */
	public OrderRepository getRepository(String id) {
		Container container = locator.get(id);
		if( container == null ) throw new IllegalArgumentException("Container does not exist: " + id);
		return new OrderRepositoryImpl(container);
	}
	
	/**
	 * Simply gets the container from the container factory
	 * and wraps it inside a new {@link OrderRepositoryImpl}
	 * instance. 
	 */
	public OrderRepository getOpenRepository(String id) {
		EditableContainer container = locator.open(id);
		if( container == null ) throw new IllegalArgumentException("Container or transaction does not exist: " + id);
		return new OrderRepositoryImpl(container);
	}
	
	public OrderRepository createNewOrder() {
	    EditableContainer container = locator.create();
	    
	    return new OrderRepositoryImpl(container);
	}
	
	/**
	 * TODO: Implement search across docs using doc store object
	 */
	public OrderSummary[] findOrdersByAccount(String accountId) {
	    throw new UnsupportedOperationException("Search across docs not yet implemented");
	}
	
	/**
	 * TODO: Implement search across docs
	 */
	public OrderSummary[] findOrdersByStockItem(String stockItemId) {
	    throw new UnsupportedOperationException("Search across docs not yet implemented");
	}

	/**
	 * @return the locator
	 */
	public ContainerFactory getLocator() {
		return locator;
	}

	/**
	 * @param locator the locator to set
	 */
	public void setLocator(ContainerFactory locator) {
		this.locator = locator;
	}
}
