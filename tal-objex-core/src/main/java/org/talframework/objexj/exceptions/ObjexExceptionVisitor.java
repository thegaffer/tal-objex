package org.talframework.objexj.exceptions;

/**
 * This visitor supports a double dispatch mechanism on
 * the objex exceptions. This is typically for runtime
 * engines and interfaces to use and not for specific
 * client use.
 *
 * @author Tom Spencer
 */
public interface ObjexExceptionVisitor {

    public <T> T visit(ContainerExistsException e, Class<T> expected);
    
    public <T> T visit(ContainerInvalidException e, Class<T> expected);
    
    public <T> T visit(ContainerNotFoundException e, Class<T> expected);
    
    public <T> T visit(ContainerTypeNotFoundException e, Class<T> expected);
    
    public <T> T visit(EventHandlerNotFoundException e, Class<T> expected);
    
    public <T> T visit(ObjectErrorException e, Class<T> expected);
    
    public <T> T visit(ObjectFieldErrorException e, Class<T> expected);
    
    public <T> T visit(ObjectFieldInvalidException e, Class<T> expected);
    
    public <T> T visit(ObjectIDInvalidException e, Class<T> expected);
    
    public <T> T visit(ObjectInvalidException e, Class<T> expected);
    
    public <T> T visit(ObjectNotFoundException e, Class<T> expected);
    
    public <T> T visit(ObjectRemovedException e, Class<T> expected);
    
    public <T> T visit(ObjectTypeNotFoundException e, Class<T> expected);
    
    public <T> T visit(QueryNotFoundException e, Class<T> expected);
    
    public <T> T visit(TransactionNotFoundException e, Class<T> expected);
}
