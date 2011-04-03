package org.talframework.objexj.runtime.globals;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.intersys.globals.Connection;
import com.intersys.globals.ConnectionContext;
import com.intersys.globals.NodeReference;
import com.intersys.globals.ValueList;

/**
 * This is a temporary class that is testing behaviour of globals
 *
 * @author Tom Spencer
 */
public class TestGlobals {
    private static final Log logger = LogFactory.getLog(TestGlobals.class);
    
    private Connection conn = null;

    @Before
    public void setup() {
        conn = ConnectionContext.getConnection();
        conn.connect("USER", "SYS", "SYS");
        conn.startTransaction();
    }
    
    @After
    public void end() {
        //logger.info("Rolling back!");
        //conn.rollback();
        //logger.info("Rolled back!");
        conn.commit();
        conn.close();
    }
    
    @Test
    public void handleNode() {
        logger.info("Conn: " + conn);
        
        NodeReference ref = conn.createNodeReference("Test");
        logger.info("Ref: " + ref);
        ref.appendSubscript("objects");
        logger.info("Ref: " + ref);
        ref.set("Test Node");
        ref.set("Testing SubIndex", "SubIndex", 1);
        ref.close();
        
        NodeReference ref2 = conn.createNodeReference("Test");
        ref2.appendSubscript("objects");
        logger.info("Ref2: " + ref2);
        Assert.assertEquals("Test Node", ref2.getString());
        Assert.assertEquals("Testing SubIndex", ref2.getString("SubIndex", 1));
        ref2.close();
        
        NodeReference ref3 = conn.createNodeReference("Test");
        ref3.appendSubscript("objects");
        ref3.appendSubscript("SubIndex");
        ref3.appendSubscript(1);
        logger.info("Ref3: " + ref3);
        Assert.assertEquals("Testing SubIndex", ref3.getString());
        
        ref3.setSubscriptCount(1);
        Assert.assertEquals("Test Node", ref3.getString());
        ref3.close();
    }
    
    @Test
    public void directWrite() {
        logger.info("Conn: " + conn);
        
        for( int i = 0 ; i < 100000 ; i++ ) {
            NodeReference ref = conn.createNodeReference("Direct");
            ref.appendSubscript("objects");
            for( int j = 0 ; j < 10 ; j++ ) {
                ref.appendSubscript(j);
                ref.set("Test Node Val1");
                ref.setSubscriptCount(ref.getSubscriptCount()-1);
            }
            ref.close();
        }
    }
 
    @Test
    public void subscriptWrite() {
        logger.info("Conn: " + conn);
        
        for( int i = 0 ; i < 100000 ; i++ ) {
            NodeReference ref = conn.createNodeReference("Direct");
            for( int j = 0 ; j < 10 ; j++ ) {
                ref.set("Test Node Val1", "objects", j);
            }
            ref.close();
        }
    }
    
    @Test
    public void subscriptValueList() {
        logger.info("Conn: " + conn);
        
        for( int i = 0 ; i < 100000 ; i++ ) {
            ValueList lst = conn.createList();
            NodeReference ref = conn.createNodeReference("Direct");
            ref.appendSubscript("objects");
            for( int j = 0 ; j < 10 ; j++ ) {
                lst.append(j);
            }
            ref.set(lst, "Test Node Val1", "objects");
            lst.close();
            ref.close();
        }
    }
    
    /**
     * Just shows we share a connection - at least in the same thread.
     */
    @Test
    public void separateConnections() {
        Connection conn1 = ConnectionContext.getConnection();
        Assert.assertTrue(conn1.isConnected());
        
        Connection conn2 = ConnectionContext.getConnection();
        Assert.assertTrue(conn2.isConnected());
        
        Assert.assertEquals(conn1, conn2);
    }
}
