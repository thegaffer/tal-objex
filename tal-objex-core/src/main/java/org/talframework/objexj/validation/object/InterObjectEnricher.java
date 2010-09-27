package org.talframework.objexj.validation.object;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.talframework.objexj.validation.groups.InterObjectEnrichmentGroup;

/**
 * This is the enricher for custom inter object enrichment.
 * This is used by the Objex generators and will call any
 * @ObjexEnrich method (marked as belonging to the 
 * {@link InterObjectEnrichmentGroup} group). It typically 
 * does nothing if you have no such methods.
 *
 * @author Tom Spencer
 */
public class InterObjectEnricher implements ConstraintValidator<InterObjectEnrich, SelfInterObjectEnricher> {

    public void initialize(InterObjectEnrich constraintAnnotation) {
    }
    
    public boolean isValid(SelfInterObjectEnricher value, ConstraintValidatorContext context) {
        value.enrichObjectAgainstOthers();
        return true;
    }
}
