package org.tpspencer.tal.objexj.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 
 * @author Tom Spencer
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface ObjexRefProp {
    boolean owned() default true;
    Class<?> type();
    String newType() default "";
}
