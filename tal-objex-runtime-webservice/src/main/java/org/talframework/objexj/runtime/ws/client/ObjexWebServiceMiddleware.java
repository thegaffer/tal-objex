package org.talframework.objexj.runtime.ws.client;

import java.util.Map;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexIDStrategy;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.TransactionCache;
import org.talframework.objexj.events.EventListener;

/**
 * This class implements a middleware that uses a WebService call
 * to get and save the objects.
 *
 * @author Tom Spencer
 */
public class ObjexWebServiceMiddleware implements ContainerMiddleware {

    public void init(Container container) {
        // TODO Auto-generated method stub
        
    }
    
    public String getContainerId() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public boolean isNew() {
        // TODO Auto-generated method stub
        return false;
    }
    
    public ObjexObjStateBean loadObject(Class<? extends ObjexObjStateBean> type, ObjexID id) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public TransactionCache open() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public TransactionCache getCache() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public ObjexIDStrategy getIdStrategy() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public String save(String status, Map<String, String> header) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public String suspend() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void clear() {
        // TODO Auto-generated method stub
        
    }
    
    public void registerListener(EventListener listener) {
        // TODO Auto-generated method stub
        
    }
    
    public void registerListenerForTransaction(EventListener listener) {
        // TODO Auto-generated method stub
        
    }
}
