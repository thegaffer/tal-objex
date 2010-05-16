package org.tpspencer.tal.gaetest;

import org.tpspencer.tal.objexj.container.ContainerMiddlewareFactory;
import org.tpspencer.tal.objexj.gae.GAEMiddlewareFactory;
import org.tpspencer.tal.objexj.sample.api.repository.StockService;

public class StockServiceFactory {
    private static final StockServiceFactory INSTANCE = new StockServiceFactory();
    
    private StockService service = null;
    
    private StockServiceFactory() {
        try {
            ContainerMiddlewareFactory middlewareFactory = new GAEMiddlewareFactory();
            
            Class<?> cls = Class.forName("org.tpspencer.tal.objexj.sample.model.stock.impl.StockServiceImpl");
            service = (StockService)cls.getConstructor(ContainerMiddlewareFactory.class).newInstance(middlewareFactory);
        }
        catch( Exception e ) {
            throw new IllegalArgumentException("Cannot initialise order service, likely a coding or config error", e);
        }
    }
    
    public static StockServiceFactory getInstance() {
        return INSTANCE;
    }
    
    public StockService getService() {
        return service;
    }
}
