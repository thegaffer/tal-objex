package org.talframework.objexj.runtime.rs.service;

import javax.ws.rs.Path;

import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.impl.SimpleContainerStrategy;
import org.talframework.objexj.container.middleware.InMemoryMiddlewareFactory;
import org.talframework.objexj.locator.SimpleContainerFactory;
import org.talframework.objexj.runtime.rs.service.ObjexInteractiveResource;
import org.talframework.objexj.sample.model.stock.impl.CategoryImpl;
import org.talframework.objexj.sample.model.stock.impl.ProductImpl;

@Path("/_stock")
public class SampleInteractiveResource extends ObjexInteractiveResource {

    private static ContainerStrategy strategy;
    
    static {
        strategy = new SimpleContainerStrategy("Stock", "Stock", "Category", CategoryImpl.STRATEGY, ProductImpl.STRATEGY);
    }
    
    public SampleInteractiveResource() {
        super("Stock", new SimpleContainerFactory(strategy, new InMemoryMiddlewareFactory()));
    }
}
