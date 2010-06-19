package org.tpspencer.tal.objexj.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates that the method will update
 * the object. The generators ensure that the object is
 * in a transaction before allowing the method to proceed.
 * There is no need to mark a method prefixed 'set' with
 * this annotation.
 * 
 * @author Tom Spencer
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface ObjexUpdateMethod {

}
