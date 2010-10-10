package org.talframework.objexj.runtime.ws.service;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.ContainerMiddlewareFactory;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.locator.InterceptingContainerFactory;
import org.talframework.objexj.locator.SimpleContainerFactory;
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
 * <p><code><pre>http://{server}/{context}/containerType/containerId</pre></code></p>
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
        
        // TODO: Get request object and then based on headers get that, that and children or that and all children!
        ObjexObj obj = objectId != null ? container.getObject(objectId) : container.getRootObject();
        
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
        
    }
    
    /**
     * Helper method to get any additional object ID from the
     * requested URL. Returns null if there is no such URL
     */
    @Trace
    private String getObjectId(HttpServletRequest req) {
        
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
}
