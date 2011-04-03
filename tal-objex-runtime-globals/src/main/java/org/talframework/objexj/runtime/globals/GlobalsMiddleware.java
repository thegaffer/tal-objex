package org.talframework.objexj.runtime.globals;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.container.ObjexIDStrategy;

import com.intersys.globals.Connection;
import com.intersys.globals.ConnectionContext;
import com.intersys.globals.NodeReference;

/**
 * This class persists and retreives objects that belong to a container
 * from Globals storage. Each instance of this class acts for a
 * specific container.
 * 
 * <p>This classes main job is to actively manage the storage. Using
 * globals we very carefully lay out the storage as follows:</p>
 * 
 * <code><pre>
 * document(containerId)=lastIdUsed
 * document(containerId, "control")={object describing document}
 * document(containerId, "objects", [objectid], "type")=object type
 * document(containerId, "objects", [objectid], "state")=object
 * document(containerId, "transactions", transactionId, "objects", [objectId], "type")
 * document(containerId, "transactions", transactionId, "objects", [objectId], "state") 
 * </pre></code>
 *
 * @author Tom Spencer
 */
public class GlobalsMiddleware /*implements ContainerMiddleware*/ {
    
    /** Holds the strategy for the container */
    private ContainerStrategy strategy;
    /** The ID of the container */
    private String containerId = null;
    /** The ID of the transaction - if there is one */
    private String transactionId = null;
    
    /** The connection to globals */
    private Connection connection;

    public GlobalsMiddleware() {
        this.connection = ConnectionContext.getConnection();
        this.connection.connect("USER", "SYS", "SYS");
    }
    
    public GlobalsMiddleware(String id) {
        this(id, false);
    }
    
    public GlobalsMiddleware(String id, boolean transaction) {
        this();
        
        if( transaction ) {
            // break id down, probably on :
        }
        else {
            containerId = id;
        }
    }
    
    public void init(Container container) {
    }
    
    public String getContainerId() {
        return containerId;
    }
    
    public ObjexIDStrategy getIdStrategy() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public ObjexObjStateBean loadObject(Class<? extends ObjexObjStateBean> type, ObjexID id) {
        NodeReference node = connection.createNodeReference(strategy.getContainerName());
        node.appendSubscript(strategy.getContainerName());
        node.appendSubscript(containerId); // TODO: Possibly several subscripts in / format!!
        node.appendSubscript("objects");
        node.appendSubscript(id.getType());
        node.appendSubscript(id.getId().toString());
        
        // If no node here, then does not exist
        if( !node.exists() ) return null;
        
        ObjectStrategy objectStrategy = strategy.getObjectStrategy(id.getType());
        ObjexObjStateBean stateBean = null; //type.newInstance();
        //stateBean.setId(id);
        //stateBean.setParent(node.getString("parent"));
        GlobalsObjectReader reader = new GlobalsObjectReader(node);
        stateBean.acceptReader(reader);
        
        return stateBean;
    }
    
}
