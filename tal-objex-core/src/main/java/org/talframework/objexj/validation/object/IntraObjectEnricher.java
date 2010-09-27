package org.talframework.objexj.validation.object;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.talframework.objexj.validation.groups.IntraObjectEnrichmentGroup;

/**
 * This is the enricher for custom intra object enrichment.
 * This is used by the Objex generators and will call any
 * @ObjexEnrich method (marked as belonging to the 
 * {@link IntraObjectEnrichmentGroup} group). It typically 
 * does nothing if you have no such methods.
 *
 * @author Tom Spencer
 */
public class IntraObjectEnricher implements ConstraintValidator<IntraObjectEnrich, SelfIntraObjectEnricher> {

    public void initialize(IntraObjectEnrich constraintAnnotation) {
    }
    
    public boolean isValid(SelfIntraObjectEnricher value, ConstraintValidatorContext context) {
        value.enrichObject();
        return true;
    }
}
