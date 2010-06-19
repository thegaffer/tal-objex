package org.tpspencer.tal.objexj.sample.model.stock.impl;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.container.ContainerMiddlewareFactory;
import org.tpspencer.tal.objexj.container.ContainerStrategy;
import org.tpspencer.tal.objexj.container.SimpleContainerStrategy;
import org.tpspencer.tal.objexj.locator.ContainerFactory;
import org.tpspencer.tal.objexj.locator.SimpleContainerFactory;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.sample.api.repository.StockRepository;
import org.tpspencer.tal.objexj.sample.api.repository.StockService;
import org.tpspencer.tal.objexj.sample.api.stock.Category;
import org.tpspencer.tal.objexj.sample.beans.stock.CategoryBean;
import org.tpspencer.tal.objexj.sample.beans.stock.ProductBean;

public class StockServiceImpl implements StockService {
    
    public static final ObjectStrategy CATEGORY_STRATEGY = new ObjectStrategy() {
        public String getTypeName() { return "CategoryBean"; }
        public String getIdProp() { return "id"; }
        public String getParentIdProp() { return "parentId"; }
        public Class<?> getStateClass() { return CategoryBean.class; }
        
        public ObjexObj getObjexObjInstance(Container container, ObjexID parent, ObjexID id, ObjexObjStateBean state) {
            CategoryImpl ret = new CategoryImpl((CategoryBean)state);
            ret.init(container, id, parent);
            return ret;
        }
        
        public ObjexObjStateBean getNewStateInstance(Object id, Object parentId) {
            return new CategoryBean(id, parentId);
        }
        
        public ObjexObjStateBean getClonedStateInstance(ObjexObjStateBean source) {
            return new CategoryBean((CategoryBean)source);
        }
    }; 
    
    public static final ObjectStrategy PRODUCT_STRATEGY = new ObjectStrategy() {
        public String getTypeName() { return "ProductBean"; }
        public String getIdProp() { return "id"; }
        public String getParentIdProp() { return "parentId"; }
        public Class<?> getStateClass() { return ProductBean.class; }
        
        public ObjexObj getObjexObjInstance(Container container, ObjexID parent, ObjexID id, ObjexObjStateBean state) {
            ProductImpl ret = new ProductImpl((ProductBean)state);
            ret.init(container, id, parent);
            return ret;
        }
        
        public ObjexObjStateBean getNewStateInstance(Object id, Object parentId) {
            return new ProductBean(id, parentId);
        }
        
        public ObjexObjStateBean getClonedStateInstance(ObjexObjStateBean source) {
            return new ProductBean((ProductBean)source);
        }
    }; 
    
    /** Holds the Objex factory for the order container type */
    private final ContainerFactory locator;
    
    public StockServiceImpl(ContainerFactory locator) {
        this.locator = locator;
    }
    
    public StockServiceImpl(ContainerMiddlewareFactory middlewareFactory) {
        ObjectStrategy[] strategies = new ObjectStrategy[]{CATEGORY_STRATEGY, PRODUCT_STRATEGY};
        ContainerStrategy strategy = new SimpleContainerStrategy("Stock", "CategoryBean", strategies);
        
        locator = new SimpleContainerFactory(strategy, middlewareFactory);
    }

    public StockRepository getRepository() {
        return new StockRepositoryImpl(locator.get("Stock/Stock"));
    }
    
    public StockRepository getOpenRepository() {
        return new StockRepositoryImpl(locator.open("Stock/Stock"));
    }
    
    public StockRepository getOpenRepository(String id) {
        return new StockRepositoryImpl(locator.getTransaction("Stock/Stock", id));
    }

    public void ensureCreated() {
        EditableContainer container = locator.create("Stock/Stock");
        if( container.isNew() ) {
            Category rootCat = container.getRootObject().getBehaviour(Category.class);
            rootCat.setName("Root");
            rootCat.setDescription("The root category for all Stock");
            
            container.saveContainer();
        }
    }
}
