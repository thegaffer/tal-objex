package org.talframework.objexj.validation.object;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * This annotation invokes the generic internal intra
 * object enricher, which in turns calls the appropriate
 * method back on the exposing object. This allows you 
 * to keep enrichment internal to your business object.
 * 
 * @author Tom Spencer
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IntraObjectEnricher.class)
@Documented
public @interface IntraObjectEnrich {
    
    String message() default "enrich.object.internal";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
