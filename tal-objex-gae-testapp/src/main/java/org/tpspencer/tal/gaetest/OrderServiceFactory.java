package org.tpspencer.tal.gaetest;

import org.tpspencer.tal.objexj.sample.api.repository.OrderService;

public class OrderServiceFactory {
    private static final OrderServiceFactory INSTANCE = new OrderServiceFactory();
    
    private OrderService service = null;
    
    private OrderServiceFactory() {
        try {
            Class<?> cls = Class.forName("org.tpspencer.tal.objexj.sample.model.order.impl.OrderServiceImpl");
            service = (OrderService)cls.newInstance();
        }
        catch( Exception e ) {
            throw new IllegalArgumentException("Cannot initialise order service, likely a coding or config error", e);
        }
    }
    
    public static OrderServiceFactory getInstance() {
        return INSTANCE;
    }
    
    public OrderService getService() {
        return service;
    }
}
