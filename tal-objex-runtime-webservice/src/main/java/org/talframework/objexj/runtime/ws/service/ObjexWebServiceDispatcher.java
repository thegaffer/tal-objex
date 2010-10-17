/*
 * Copyright 2010 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.objexj.runtime.ws.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.ContainerMiddlewareFactory;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.locator.InterceptingContainerFactory;
import org.talframework.objexj.locator.SimpleContainerFactory;
import org.talframework.objexj.object.RecursiveObjectCompiler;
import org.talframework.objexj.object.writer.PropertyWriter;
import org.talframework.objexj.runtime.rs.service.WebServiceInterceptor;
import org.talframework.objexj.runtime.ws.ObjexHeaders;
import org.talframework.tal.aspects.annotations.HttpTrace;
import org.talframework.tal.aspects.annotations.Trace;

/**
 * This class represents the Objex Web Service dispatcher.
 * It handles requests and dispatches them to the actual
 * container in question.
 * 
 * <p>This dispatcher works on the URL that is requested
 * to determine what to do. The url after the server and
 * context root then is:</p>
 * 
 * <p><code><pre>http://{server}/{context}/containerType/container|objectType/containerId[/objectId]</pre></code></p>
 * 
 * <p>This kind of forces the ID of a container to be
 * containerType/containerId, although this is the default
 * anyway. Any ID after this is interpreted as an individual
 * object ID, and so the request will get, or post to, that 
 * object.</p>
 * 
 * <p>There are also various request headers supported
 * which in turn affect the interpretation of the request
 * or the response back. See {@link ObjexHeaders} for
 * more information.</p>
 * 
 * SUGGEST: Support doDelete for removing containers?
 *
 * @author Tom Spencer
 */
public class ObjexWebServiceDispatcher extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /** The factory to get the containers when requested */
    private InterceptingContainerFactory factory;
    /** Holds the container strategy */
    private ContainerStrategy strategy;
    /** The minimum depth we recurse */
    private int minDepth = 1;

    @Override
    public void init(ServletConfig config) throws ServletException {
        String middlewareClass = config.getInitParameter("middlewareClass");
        String strategyClass = config.getInitParameter("strategy");
        
        ContainerMiddlewareFactory middlewareFactory = createInstance(ContainerMiddlewareFactory.class, middlewareClass, "middlewareClass", true);
        ContainerStrategy strategy = createInstance(ContainerStrategy.class, strategyClass, "Container Strategy", true);
        
        factory = new SimpleContainerFactory(strategy, middlewareFactory);
        
        super.init(config);
    }
    
    /**
     * Overridden to return back the requested container an object 
     * within it.
     */
    @Override
    @HttpTrace
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String containerId = getContainerId(req);
        String objectId = getObjectId(req);
        
        WebServiceInterceptor interceptor = new WebServiceInterceptor();
        Container container = factory.get(interceptor, containerId);
        
        // Get all objects
        ObjexObj obj = objectId != null ? container.getObject(objectId) : container.getRootObject();
        
        RecursiveObjectCompiler compiler = new RecursiveObjectCompiler(container);
        compiler.setRecurseDepth(getRecursionDepth(req));
        Map<ObjexID, ObjexObj> objects = compiler.getObjects(obj);
        
        // Output the response
        resp.setContentType("text/plain"); // TODO: Is there a mime type for properties!!
        
        StringBuilder buf = new StringBuilder();
        PropertyWriter writer = new PropertyWriter(resp.getWriter());
        for( ObjexID id : objects.keySet() ) {
            obj = objects.get(id);
            buf.setLength(0);
            String prefix = buf.append("object").append('[').append(id.toString()).append(']').toString();
            writer.writeObject(prefix, obj);
        }
        
        resp.setStatus(HttpServletResponse.SC_OK);
    }
    
    /**
     * Overridden to allow the client to post some new objects or
     * changes to existing objects inside an existing container.
     */
    @Override
    @HttpTrace
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String containerId = getContainerId(req);
        String objectId = getObjectId(req);
        
        WebServiceInterceptor interceptor = new WebServiceInterceptor();
        Container container = factory.open(interceptor, containerId);
    }
    
    /**
     * Overridden to allow the client to create a new container.
     */
    @Override
    @HttpTrace
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebServiceInterceptor interceptor = new WebServiceInterceptor();
        Container container = factory.create(interceptor);
    }
    
    /**
     * Helper method to get the container ID from the requested
     * URL.
     */
    @Trace
    private String getContainerId(HttpServletRequest req) {
        String containerType = stripLeading(req.getServletPath());
        // TODO: Should we just use strategy type!?!
        
        String path = stripLeading(req.getPathInfo());
        String[] pathElements = path != null ? path.split("/") : null;
        
        String ret = containerType;
        
        if( pathElements != null && pathElements.length > 0 ) {
            String requestType = pathElements[0];
            
            StringBuilder buf = new StringBuilder();
            buf.append(containerType);
            int size = pathElements.length;
            if( !requestType.equals("container") ) size -= 1; 
            for( int i = 1 ; i < size ; i++ ) {
                buf.append('/').append(pathElements[i]);
            }
            
            ret = buf.toString();
        }
        
        return ret;
    }
    
    /**
     * Helper method to get any additional object ID from the
     * requested URL. Returns null if there is no such URL
     */
    @Trace
    private String getObjectId(HttpServletRequest req) {
        String path = stripLeading(req.getPathInfo());
        String[] pathElements = path != null ? path.split("/") : null;
        
        String ret = null;
        
        if( pathElements != null && !pathElements[0].equals("container") ) {
            ret = pathElements[0] + "/" + pathElements[pathElements.length - 1];
        }
        
        return ret;
    }
    
    /**
     * @param req The current request
     * @return The depth to recurse objects
     */
    private int getRecursionDepth(HttpServletRequest req) {
        int ret = minDepth;
        
        if( req.getHeader(ObjexHeaders.FETCH_ALL.toString()) != null ) ret = -1;
        else if( req.getHeader(ObjexHeaders.FETCH_OBJECT.toString()) != null ) ret = 0;
        else if( req.getHeader(ObjexHeaders.FETCH_DESCENDANTS.toString()) != null ) {
            String depth = req.getHeader(ObjexHeaders.FETCH_DESCENDANTS.toString());
            ret = Integer.parseInt(depth);
        }
        
        // Return lower of depth of min
        return ret >= 0 && ret < minDepth ? minDepth : ret;
    }
    
    /**
     * Helper to create an instance of a class given its name.
     * 
     * TODO: This should be in bean utils
     * 
     * @param expected The expected type of the class
     * @param name The name of the class
     * @param role The role of this class (used in exception message)
     * @return An instance of the class given its name
     */
    private <T> T createInstance(Class<T> expected, String name, String role, boolean required) {
        if( name == null ) {
            if( required ) throw new IllegalArgumentException("The " + role + " is missing");
            else return null;
        }
        
        T ret = null;
        try {
            Class<?> cls = Class.forName(name);
            if( !expected.isAssignableFrom(cls) ) {
                throw new IllegalArgumentException("The " + role + " does not appear to be of the correct type [" + expected + "]: " + name);
            }

            ret = expected.cast(cls.newInstance());
        }
        catch( ClassNotFoundException e ) {
            throw new IllegalArgumentException("The " + role + " does not appear to exist in classpath: " + name, e);
        }
        catch( IllegalAccessException e ) {
            throw new IllegalArgumentException("Could not create the " + role + ": " + name, e);
        }
        catch( InstantiationException e ) {
            throw new IllegalArgumentException("Could not create the " + role + ": " + name, e);
        }
        
        return ret;
    }

    /**
     * Helper to strip any leading / from the input val.
     * If the path is only / then it is null'ed
     */
    private String stripLeading(String val) {
        if( "/".equals(val) ) val = null;
        else if( val != null && val.startsWith("/") && val.length() > 1 ) val = val.substring(1);
        return val;
    }

    /**
     * @return the factory
     */
    public InterceptingContainerFactory getFactory() {
        return factory;
    }

    /**
     * Setter for the factory field
     *
     * @param factory the factory to set
     */
    public void setFactory(InterceptingContainerFactory factory) {
        this.factory = factory;
    }

    /**
     * @return the strategy
     */
    public ContainerStrategy getStrategy() {
        return strategy;
    }

    /**
     * Setter for the strategy field
     *
     * @param strategy the strategy to set
     */
    public void setStrategy(ContainerStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * @return the minDepth
     */
    public int getMinDepth() {
        return minDepth;
    }

    /**
     * Setter for the minDepth field
     *
     * @param minDepth the minDepth to set
     */
    public void setMinDepth(int minDepth) {
        this.minDepth = minDepth;
    }
}
