package org.talframework.domain.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Typically when evaluating any identity or value object a framework is
 * usually going to conclude that any JavaBean style property is going 
 * to be a property of that object it can use. This is not always the
 * case, this annotation can be used to mark only those properties that
 * are for public consumption and hide or not touch the rest.
 * 
 * <p>As with other proeprty annotations, this should be marked on the
 * public JavaBean getter method for the property</p>
 * 
 * <p>In Objex if any single property has this annotation, then only
 * properties that have this or the reference or child properties will
 * be considered as public properties - this is independent of whether
 * they are marked as Calculated or Transient. Note that if there are
 * no {@link Property} annotations, even if there are 
 * {@link ReferenceProperty} or {@link ChildProperty} ones then all
 * JavaBean style properties will be considered properties of the object.
 *
 * @author Tom Spencer
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
}
