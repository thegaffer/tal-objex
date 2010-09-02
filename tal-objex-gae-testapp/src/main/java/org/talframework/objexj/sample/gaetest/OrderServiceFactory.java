package org.talframework.objexj.sample.gaetest;

import org.talframework.objexj.container.ContainerMiddlewareFactory;
import org.talframework.objexj.runtime.gae.GAEMiddlewareFactory;
import org.talframework.objexj.sample.api.repository.OrderService;

public class OrderServiceFactory {
    private static final OrderServiceFactory INSTANCE = new OrderServiceFactory();
    
    private OrderService service = null;
    
    private OrderServiceFactory() {
        try {
            ContainerMiddlewareFactory middlewareFactory = new GAEMiddlewareFactory();
            
            Class<?> cls = Class.forName("org.talframework.objexj.sample.model.order.impl.OrderServiceImpl");
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
