package org.talframework.objexj.runtime.gae.event;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.talframework.objexj.Container;
import org.talframework.objexj.exceptions.ContainerNotFoundException;
import org.talframework.objexj.exceptions.EventHandlerNotFoundException;
import org.talframework.objexj.locator.SingletonContainerLocator;

/**
 * This servlet processes events from containers by
 * dispatching the event to the event handler on
 * the container.
 * 
 * @author Tom Spencer
 */
public class GAEEventProcessor extends HttpServlet {
    private final static long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String containerId = req.getHeader("X-Objex-Container");
        String sourceContainerId = req.getHeader("X-Objex-SourceContainer");
        String event = req.getHeader("X-Objex-Event");
        String state = req.getHeader("X-Objex-State");
        
        try {
            Container container = SingletonContainerLocator.getInstance().get(containerId);
            container.processEvent(new EventImpl(event, sourceContainerId, state, req));
        }
        catch( ContainerNotFoundException e ) {
            // TODO: Log
            resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        catch( EventHandlerNotFoundException e ) {
            // TODO: Log
            resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
        catch( Exception e ) {
            // TODO: Log
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
