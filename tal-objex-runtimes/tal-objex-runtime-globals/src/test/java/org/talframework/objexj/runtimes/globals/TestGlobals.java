/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.runtimes.globals;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.intersys.globals.Connection;
import com.intersys.globals.ConnectionContext;
import com.intersys.globals.NodeReference;
import com.intersys.globals.ValueList;
import com.intersys.globals.impl.ConnectionImpl;

/**
 * This class contains various tests of Globals itself. This test pack
 * is ignored generally as it has no tests against the core of this
 * module.
 *
 * @author Tom Spencer
 */
@Ignore
public class TestGlobals {
    private static final Log logger = LogFactory.getLog(TestGlobals.class);
    
    /** Amount of runs the recursive tests take */
    private static final int RUNS = 10000;
    
    private Connection conn = null;

    @Before
    public void setup() {
        conn = new ConnectionImpl();
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
        
        for( int i = 0 ; i < RUNS ; i++ ) {
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
        
        for( int i = 0 ; i < RUNS ; i++ ) {
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
        
        for( int i = 0 ; i < RUNS ; i++ ) {
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
