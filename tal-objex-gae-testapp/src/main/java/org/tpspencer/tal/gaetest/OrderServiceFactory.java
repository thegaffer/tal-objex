package org.tpspencer.tal.gaetest;

import org.tpspencer.tal.objexj.container.ContainerMiddlewareFactory;
import org.tpspencer.tal.objexj.gae.GAEMiddlewareFactory;
import org.tpspencer.tal.objexj.sample.api.repository.OrderService;

public class OrderServiceFactory {
    private static final OrderServiceFactory INSTANCE = new OrderServiceFactory();
    
    private OrderService service = null;
    
    private OrderServiceFactory() {
        try {
            ContainerMiddlewareFactory middlewareFactory = new GAEMiddlewareFactory();
            
            Class<?> cls = Class.forName("org.tpspencer.tal.objexj.sample.model.order.impl.OrderServiceImpl");
            service = (OrderService)cls.getConstructor(ContainerMiddlewareFactory.class).newInstance(middlewareFactory);
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
