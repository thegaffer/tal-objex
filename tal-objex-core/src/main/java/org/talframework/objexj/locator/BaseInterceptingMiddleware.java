package org.talframework.objexj.locator;

import java.util.Map;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ObjexIDStrategy;
import org.talframework.objexj.container.TransactionCache;
import org.talframework.objexj.events.EventListener;

/**
 * This class is a base class for any intercepting middleware. It
 * implements all methods by called the read middleware. Derived
 * classes can then just implement the methods they require.
 *
 * @author Tom Spencer
 */
public abstract class BaseInterceptingMiddleware implements InterceptingMiddleware {

    /** Holds the real middleware */
    private ContainerMiddleware middleware;
    
    public void setRealMiddleware(ContainerMiddleware middleware) {
        this.middleware = middleware;
    }
    
    /**
     * {@inheritDoc}
     */
    public void init(Container container) {
        middleware.init(container);
    }
    
    /**
     * {@inheritDoc}
     */
    public String getContainerId() {
        return middleware.getContainerId();
    }
    
    /**
     * {@inheritDoc}
     */
    public ObjexObjStateBean loadObject(Class<? extends ObjexObjStateBean> type, ObjexID id) {
        return middleware.loadObject(type, id);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isNew() {
        return middleware.isNew();
    }
    
    /**
     * {@inheritDoc}
     */
    public TransactionCache open() {
        return middleware.open();
    }
    
    /**
     * {@inheritDoc}
     */
    public ObjexIDStrategy getIdStrategy() {
        return middleware.getIdStrategy();
    }
    
    /**
     * {@inheritDoc}
     */
    public TransactionCache getCache() {
        return middleware.getCache();
    }
    
    /**
     * {@inheritDoc}
     */
    public String suspend() {
        return middleware.suspend();
    }
    
    /**
     * {@inheritDoc}
     */
    public void clear() {
        middleware.clear();
    }
    
    /**
     * {@inheritDoc}
     */
    public String save(String status, Map<String, String> header) {
        return middleware.save(status, header);
    }
    
    /**
     * {@inheritDoc}
     */
    public void registerListener(EventListener listener) {
        middleware.registerListener(listener);
    }
    
    /**
     * {@inheritDoc}
     */
    public void registerListenerForTransaction(EventListener listener) {
        middleware.registerListenerForTransaction(listener);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "InterceptingMiddleware: " + middleware;
    }
}
