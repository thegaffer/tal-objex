package org.tpspencer.tal.objexj.sample.model.stock.impl;

import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.container.ContainerMiddlewareFactory;
import org.tpspencer.tal.objexj.container.ContainerStrategy;
import org.tpspencer.tal.objexj.container.SimpleContainerStrategy;
import org.tpspencer.tal.objexj.locator.ContainerFactory;
import org.tpspencer.tal.objexj.locator.SimpleContainerFactory;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjectStrategy;
import org.tpspencer.tal.objexj.sample.api.repository.StockRepository;
import org.tpspencer.tal.objexj.sample.api.repository.StockService;
import org.tpspencer.tal.objexj.sample.api.stock.Category;
import org.tpspencer.tal.objexj.sample.beans.stock.CategoryBean;
import org.tpspencer.tal.objexj.sample.beans.stock.ProductBean;

public class StockServiceImpl implements StockService {
    
    /** Holds the Objex factory for the order container type */
    private final ContainerFactory locator;
    
    public StockServiceImpl(ContainerFactory locator) {
        this.locator = locator;
    }
    
    public StockServiceImpl(ContainerMiddlewareFactory middlewareFactory) {
        ObjectStrategy[] strategies = new ObjectStrategy[]{
                new SimpleObjectStrategy("Category", CategoryImpl.class, CategoryBean.class),
                new SimpleObjectStrategy("Product", ProductImpl.class, ProductBean.class),
        };
        ContainerStrategy strategy = new SimpleContainerStrategy("Stock", strategies);
        
        locator = new SimpleContainerFactory(strategy, middlewareFactory);
    }

    public StockRepository getRepository() {
        return new StockRepositoryImpl(locator.get("Stock/Stock"));
    }
    
    public StockRepository getOpenRepository() {
        return new StockRepositoryImpl(locator.open("Stock/Stock", true));
    }
    
    public StockRepository getOpenRepository(String id) {
        return new StockRepositoryImpl(locator.get("Stock/Stock", id));
    }

    public void ensureCreated() {
        EditableContainer container = locator.open("Stock/Stock", false);
        if( container.isNew() ) {
            Category rootCat = container.newObject("Category", null).getBehaviour(Category.class);
            rootCat.setName("Root");
            rootCat.setDescription("The root category for all Stock");
            
            container.saveContainer();
        }
    }
}
