/* TODO: Insert your copyright notice */

package ${package}.model;

import org.talframework.objexj.annotations.ObjexObj;
import ${package}.beans.ObjectBean;

/**
 * This is a sample ObjexObj implementation class. Either
 * rename it and point to appropriate state bean or use
 * it as a template for other beans.
 * 
 * <p>Note it is good practice to implement a non-Objex
 * exposing business interface from this object. Which
 * is not shown in the sample.</p>
 * 
 * TODO: Amend this Javadoc to describe actual business object & replace author
 *
 * @author Tom Spencer
 */
@ObjexObj(ObjectBean.class)
public class ObjectImpl {
    
    /** Member holds the state bean for this object */
    private final ObjectBean bean;
    
    /**
     * Basic constructor for the object taking in the 
     * current value of the state bean.
     */
    public ObjectImpl(ObjectBean bean) {
        this.bean = bean;
    }
    
}
