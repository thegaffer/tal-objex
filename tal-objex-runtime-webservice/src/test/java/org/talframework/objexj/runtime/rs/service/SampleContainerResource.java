package org.talframework.objexj.runtime.rs.service;

import javax.ws.rs.Path;

import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.impl.SimpleContainerStrategy;
import org.talframework.objexj.container.middleware.InMemoryMiddlewareFactory;
import org.talframework.objexj.locator.SimpleContainerFactory;
import org.talframework.objexj.runtime.rs.service.ObjexContainerResource;
import org.talframework.objexj.sample.model.stock.impl.CategoryImpl;
import org.talframework.objexj.sample.model.stock.impl.ProductImpl;

@Path("/_stock")
public class SampleContainerResource extends ObjexContainerResource {

    private static ContainerStrategy strategy;
    
    static {
        strategy = new SimpleContainerStrategy("Stock", "Stock", "Category", CategoryImpl.STRATEGY, ProductImpl.STRATEGY);
    }
    
    public SampleContainerResource() {
        super("Stock", new SimpleContainerFactory(strategy, new InMemoryMiddlewareFactory()));
    }
}
