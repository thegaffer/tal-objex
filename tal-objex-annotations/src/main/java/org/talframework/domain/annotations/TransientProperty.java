package org.talframework.domain.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a property as being transient. This tells
 * us that the value of this property is specific to the current
 * projection of the object in memory and it may change through
 * other factors. It should typically not be cached in any way.
 * 
 * <p>As with other property annotations, this should be marked on the
 * public JavaBean getter method for the property</p>
 * 
 * <p>Objex will intepret this as meaning the property should not
 * be persisted.</p>
 *
 * @author Tom Spencer
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TransientProperty {

}
