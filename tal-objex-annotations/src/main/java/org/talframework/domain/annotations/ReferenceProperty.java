package org.talframework.domain.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a property as representing a reference to another
 * object. It is applied to the public Java Getter (Accessor) method of a
 * property.
 * 
 * <p>The type of the property this refers to should be directly to an
 * interface, or it should be to a List, Set or Map (interface versions)
 * where the element/value generic type should be to an interface. This
 * interface is marked on the annotation for additional type checking.
 * For instance ...</p>
 * <p><code><pre>
 * &#64;ReferenceProperty(SomeInterface.class)
 * public SomeInterface getRef();                   // Valid
 * 
 * &#64;ReferenceProperty(SomeInterface.class)
 * public List<SomeInterface> getRefs();            // Valid, can use Set also
 * 
 * &#64;ReferenceProperty(SomeInterface.class)
 * public Map<String, SomeInterface> getRefMap();   // Valid
 * 
 * &#64;ReferenceProperty(SomeInterface.class)
 * public ArrayList<SomeInterface) getRefs();       // Invalid, cannot use ArrayList directly, only List!
 * 
 * &#64;ReferenceProperty(SomeObject.class)
 * &#64;SomeObject getRefs();                       // Invalid, cannot use object/class directly
 * </pre></code></p>
 * 
 * <p>Objex uses this annotation to understand and manage the non-owned
 * relationships between the objects.</p> 
 *
 * @author Tom Spencer
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReferenceProperty {
    /** Holds an interface detailing the type of element to hold in reference */
    Class<?> value();
}
