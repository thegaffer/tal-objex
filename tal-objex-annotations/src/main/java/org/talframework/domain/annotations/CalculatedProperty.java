package org.talframework.domain.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This marks a property as being calculated internally from other
 * state. This does not mean the value is not persisted or anything,
 * it might be, but it does imply that it should not be set generally
 * speaking.
 * 
 * <p>As with other proeprty annotations, this should be marked on the
 * public JavaBean getter method for the property</p>
 * 
 * <p>Objex uses this to ensure the property is not set by clients
 * using its dynamic property access features. The actual class will
 * protect itself by not exposing a setter to this method on the
 * interfaces to the class.</p>
 *
 * @author Tom Spencer
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CalculatedProperty {

}
