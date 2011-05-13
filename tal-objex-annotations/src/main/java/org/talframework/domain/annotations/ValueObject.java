package org.talframework.domain.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation, when applied to a class, marks it as simply being
 * a value object. This means that the object's identity is it's state
 * and any single instance of this object is immutable. A good example
 * of a value object is a String in Java.
 * 
 * <p>Objex can make certain efficiencies when it see's classes are
 * marked as value objects.</p>
 *
 * @author Tom Spencer
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueObject {

}
