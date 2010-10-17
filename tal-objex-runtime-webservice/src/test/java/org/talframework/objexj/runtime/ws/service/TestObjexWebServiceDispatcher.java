package org.talframework.objexj.runtime.ws.service;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.locator.InterceptingContainerFactory;
import org.talframework.objexj.object.RecursiveObjectCompiler;
import org.talframework.objexj.object.writer.PropertyWriter;
import org.talframework.objexj.runtime.rs.service.WebServiceInterceptor;

/**
 * This class tests the web service dispatcher
 *
 * @author Tom Spencer
 */
public class TestObjexWebServiceDispatcher {

    private Mockery context = new JUnit4Mockery();
    
    private ObjexWebServiceDispatcher underTest;
    private InterceptingContainerFactory factory;
    private ContainerStrategy strategy;
    private Container container;
    
    @Before
    public void setup() {
        factory = context.mock(InterceptingContainerFactory.class);
        strategy = context.mock(ContainerStrategy.class);
        container = context.mock(Container.class);
        
        underTest = new ObjexWebServiceDispatcher();
        underTest.setStrategy(strategy);
        underTest.setFactory(factory);
    }
    
    /**
     * Simple test for a store
     */
    @Test
    public void getBasic() throws Exception {
        final HttpServletRequest req = context.mock(HttpServletRequest.class);
        final HttpServletResponse resp = context.mock(HttpServletResponse.class);
        final ObjexObj rootObj = context.mock(ObjexObj.class);
        final PrintWriter writer = new PrintWriter(new StringWriter());
        
        context.checking(new Expectations() {{
            exactly(2).of(req).getPathInfo(); will(returnValue("/type"));
            allowing(req).getHeader(with(any(String.class))); will(returnValue(null));
            oneOf(factory).get(with(any(WebServiceInterceptor.class)), with("type")); will(returnValue(container));
            oneOf(container).getRootObject(); will(returnValue(rootObj));
            oneOf(rootObj).getId(); will(returnValue(new DefaultObjexID("Test", 1)));
            oneOf(rootObj).acceptWriter(with(any(RecursiveObjectCompiler.class)), with(false));
            oneOf(resp).setContentType("text/plain");
            oneOf(resp).getWriter(); will(returnValue(writer));
            oneOf(rootObj).getId(); will(returnValue(new DefaultObjexID("Test", 1)));
            oneOf(rootObj).getParentId(); will(returnValue(null));
            oneOf(rootObj).getType(); will(returnValue("Test"));
            oneOf(rootObj).acceptWriter(with(any(PropertyWriter.class)), with(false));
            oneOf(resp).setStatus(HttpServletResponse.SC_OK);
        }});
        
        underTest.doGet(req, resp);
        context.assertIsSatisfied();
    }
    
    /**
     * Ensures if there is no path we get an exception
     */
    @Test(expected=ServletException.class)
    public void noPath() throws Exception {
        final HttpServletRequest req = context.mock(HttpServletRequest.class);
        final HttpServletResponse resp = context.mock(HttpServletResponse.class);
        
        context.checking(new Expectations() {{
            oneOf(req).getPathInfo(); will(returnValue(null));
        }});
        
        underTest.doGet(req, resp);
    }
    
    
}
