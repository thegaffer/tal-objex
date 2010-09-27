package org.talframework.objexj.validation.object;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.talframework.objexj.validation.groups.ChildGroup;

/**
 * This annotation invokes the generic internal child
 * object validator, which in turns calls the appropriate
 * method back on the exposing object. This allows you 
 * to keep validation internal to your business object.
 * 
 * @author Tom Spencer
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChildrenValidator.class)
@Documented
public @interface ChildrenValid {
    
    String message() default "invalid.object.children";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
