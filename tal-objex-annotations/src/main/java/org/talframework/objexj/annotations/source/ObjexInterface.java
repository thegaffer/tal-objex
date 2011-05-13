package org.talframework.objexj.annotations.source;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a natural ObjexInterface. This generator
 * will look at the class provided, which should be marked as an
 * ObjexObj. It will then generate a method for each public method
 * on the target class - including any getters/setters that have
 * been generated as part of inspecting that classes private
 * properties.
 *
 * @author Tom Spencer
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface ObjexInterface {
    Class<?> value();
}
