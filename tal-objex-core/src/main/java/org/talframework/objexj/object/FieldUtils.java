package org.talframework.objexj.object;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.InternalContainer;

/**
 * This base class contains a number of helper methods that
 * the other field utility classes use.
 *
 * @author Tom Spencer
 */
public abstract class FieldUtils {

    /**
     * Internal helper to ensure the state bean is editable
     * and if not add the object to the container.
     */
    protected static void setEditable(BaseObjexObj obj, ObjexObjStateBean state) {
        if( !state.isEditable() ) {
            obj.getInternalContainer().addObjectToTransaction(obj, state);
            if( !state.isEditable() ) state.setEditable();
        }
    }
    
    /**
     * Helper to remove a specific object from the container
     */
    protected static void removeObject(BaseObjexObj parentObj, Object id) {
        Container container = parentObj.getContainer();
        
        ObjexObj objToRemove = container.getObject(id);
        if( objToRemove != null && container instanceof InternalContainer ) {
            ((InternalContainer)container).removeObject(objToRemove);
        }
        else if( objToRemove != null ){
            throw new IllegalArgumentException("Cannot remove object, invalid container: " + objToRemove);
        }
    }
    
    /**
     * Helper to copy all of the incoming objects properties into
     * current where they match on name.
     * 
     * FUTURE: Use our own bean implementation
     */
    protected static void copyObjects(Object current, Object incoming) {
        BeanWrapper currentWrapper = new BeanWrapperImpl(current);
        BeanWrapper incomingWrapper = new BeanWrapperImpl(incoming);
        
        PropertyDescriptor[] props = incomingWrapper.getPropertyDescriptors();
        for( int i = 0 ; i < props.length ; i++ ) {
            if( currentWrapper.isWritableProperty(props[i].getName()) ) {
                currentWrapper.setPropertyValue(props[i].getName(), incomingWrapper.getPropertyValue(props[i].getName()));
            }
        }
    }
}
