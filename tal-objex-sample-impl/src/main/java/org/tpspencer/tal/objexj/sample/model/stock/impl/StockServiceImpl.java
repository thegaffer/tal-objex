package org.tpspencer.tal.objexj.sample.model.stock.impl;

import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.container.ContainerMiddlewareFactory;
import org.tpspencer.tal.objexj.container.ContainerStrategy;
import org.tpspencer.tal.objexj.container.SimpleContainerStrategy;
import org.tpspencer.tal.objexj.locator.ContainerFactory;
import org.tpspencer.tal.objexj.locator.SimpleContainerFactory;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.sample.api.repository.StockRepository;
import org.tpspencer.tal.objexj.sample.api.repository.StockService;
import org.tpspencer.tal.objexj.sample.api.stock.Category;

public class StockServiceImpl implements StockService {
    
    /** Holds the Objex factory for the order container type */
    private final ContainerFactory locator;
    
    public StockServiceImpl(ContainerFactory locator) {
        this.locator = locator;
    }
    
    public StockServiceImpl(ContainerMiddlewareFactory middlewareFactory) {
        ObjectStrategy[] strategies = new ObjectStrategy[]{CategoryImpl.STRATEGY, ProductImpl.STRATEGY};
        ContainerStrategy strategy = new SimpleContainerStrategy("Stock", "Stock", "Category", strategies);
        
        locator = new SimpleContainerFactory(strategy, middlewareFactory);
    }

    public StockRepository getRepository() {
        return new StockRepositoryImpl(locator.get("Stock"));
    }
    
    public StockRepository getOpenRepository() {
        return new StockRepositoryImpl(locator.open("Stock"));
    }
    
    public StockRepository getOpenRepository(String id) {
        return new StockRepositoryImpl(locator.open(id));
    }

    public void ensureCreated() {
        // TODO: Not sure this will work!!
        EditableContainer container = locator.create();
        if( container.isNew() ) {
            Category rootCat = container.getRootObject().getBehaviour(Category.class);
            rootCat.setName("Root");
            rootCat.setDescription("The root category for all Stock");
            
            container.saveContainer();
        }
    }
}
