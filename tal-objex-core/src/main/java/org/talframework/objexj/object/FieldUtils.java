package org.talframework.objexj.object;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.util.beans.BeanDefinition;
import org.talframework.util.beans.definition.BeanDefinitionsSingleton;

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
     */
    protected static void copyObjects(Object current, Object incoming) {
        BeanDefinition currentDef = BeanDefinitionsSingleton.getInstance().getDefinition(current.getClass());
        BeanDefinition incomingDef = BeanDefinitionsSingleton.getInstance().getDefinition(incoming.getClass());
        
        for( String prop : incomingDef.getProperties() ) {
            if( !incomingDef.canRead(prop) || !currentDef.hasProperty(prop) || !currentDef.canWrite(prop) ) continue;
            currentDef.write(current, prop, incomingDef.read(incoming, prop));
        }
    }
}
