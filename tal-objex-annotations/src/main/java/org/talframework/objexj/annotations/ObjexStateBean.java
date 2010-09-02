package org.talframework.objexj.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates that the class represents the
 * persistable state of an Objex object.
 * 
 * @author Tom Spencer
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface ObjexStateBean {
    long version() default 1;
    String name();
}
