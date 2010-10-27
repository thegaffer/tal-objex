package org.talframework.objexj.runtime.rs.service;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.talframework.objexj.exceptions.BaseObjexException;
import org.talframework.objexj.exceptions.ContainerExistsException;
import org.talframework.objexj.exceptions.ContainerInvalidException;
import org.talframework.objexj.exceptions.ContainerNotFoundException;
import org.talframework.objexj.exceptions.ContainerTypeNotFoundException;
import org.talframework.objexj.exceptions.EventHandlerNotFoundException;
import org.talframework.objexj.exceptions.ObjectErrorException;
import org.talframework.objexj.exceptions.ObjectFieldErrorException;
import org.talframework.objexj.exceptions.ObjectFieldInvalidException;
import org.talframework.objexj.exceptions.ObjectIDInvalidException;
import org.talframework.objexj.exceptions.ObjectNotFoundException;
import org.talframework.objexj.exceptions.ObjectRemovedException;
import org.talframework.objexj.exceptions.ObjectTypeNotFoundException;
import org.talframework.objexj.exceptions.ObjexExceptionVisitor;
import org.talframework.objexj.exceptions.QueryNotFoundException;
import org.talframework.objexj.exceptions.TransactionNotFoundException;

/**
 * This class maps all the Objex exception types into an appropriate
 * HTTP status code.
 *
 * @author Tom Spencer
 */
@Provider
public class ObjexExceptionMapper implements ExceptionMapper<BaseObjexException>, ObjexExceptionVisitor{
    
    public Response toResponse(BaseObjexException exception) {
        return exception.accept(this, Response.class);
    }
    
    /**
     * Helper to form a response
     * 
     * @param exception The exception
     * @return The response
     */
    private Response formResponse(BaseObjexException exception, int status) {
        return Response.status(status).
                    entity(exception.getMessage()).
                    type(MediaType.TEXT_PLAIN_TYPE).
                    build();
    }
    
    public <T> T visit(ContainerExistsException e, Class<T> expected) {
        return expected.cast(formResponse(e, 400));
    }
    
    public <T> T visit(ContainerInvalidException e, Class<T> expected) {
        return expected.cast(formResponse(e, 400));
    }
    
    public <T> T visit(ContainerNotFoundException e, Class<T> expected) {
        return expected.cast(formResponse(e, 404));
    }
    
    public <T> T visit(ContainerTypeNotFoundException e, Class<T> expected) {
        return expected.cast(formResponse(e, 404));
    }
    
    public <T> T visit(EventHandlerNotFoundException e, Class<T> expected) {
        return expected.cast(formResponse(e, 404));
    }
    
    public <T> T visit(ObjectErrorException e, Class<T> expected) {
        return expected.cast(formResponse(e, 400));
    }
    
    public <T> T visit(ObjectFieldErrorException e, Class<T> expected) {
        return expected.cast(formResponse(e, 400));
    }
    
    public <T> T visit(ObjectFieldInvalidException e, Class<T> expected) {
        return expected.cast(formResponse(e, 404));
    }
    
    public <T> T visit(ObjectIDInvalidException e, Class<T> expected) {
        return expected.cast(formResponse(e, 404));
    }
    
    public <T> T visit(ObjectNotFoundException e, Class<T> expected) {
        return expected.cast(formResponse(e, 404));
    }
    
    public <T> T visit(ObjectRemovedException e, Class<T> expected) {
        return expected.cast(formResponse(e, 400));
    }
    
    public <T> T visit(ObjectTypeNotFoundException e, Class<T> expected) {
        return expected.cast(formResponse(e, 404));
    }
    
    public <T> T visit(QueryNotFoundException e, Class<T> expected) {
        return expected.cast(formResponse(e, 404));
    }
    
    public <T> T visit(TransactionNotFoundException e, Class<T> expected) {
        return expected.cast(formResponse(e, 404));
    }
}
