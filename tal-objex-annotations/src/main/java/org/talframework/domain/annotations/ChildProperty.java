package org.talframework.domain.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a property as representing a owned relationship
 * to another object. This is very must like a reference to another object,
 * but is one where there is a parent/child relationship. For instance an
 * typically an Aeroplane object would 'own' its Wings, the wings are part
 * of the aeroplane, but it references Pilots, because a Pilot may go and
 * fly other aeroplanes the relationship is not as string.
 * 
 * <p>The rules for using the annotation are exactly the same as a
 * reference property annotation. See {@link ReferenceProperty} for more
 * information.</p>
 * 
 * <p>Objex uses this annotation to understand and manage the owned
 * relationships between the objects.</p> 
 *
 * @author Tom Spencer
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChildProperty {
    /** Holds an interface detailing the type of element to hold in child */
    Class<?> value();
}
