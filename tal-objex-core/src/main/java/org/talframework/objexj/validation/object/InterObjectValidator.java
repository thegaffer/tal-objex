package org.talframework.objexj.validation.object;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.talframework.objexj.validation.groups.InterObjectGroup;

/**
 * This is the validator for custom child object validation.
 * This is used by the Objex generators and will call any
 * @ObjexCheck method (marked as belonging to the 
 * {@link InterObjectGroup} group). It typically does nothing
 * if you have no such methods.
 *
 * @author Tom Spencer
 */
public class InterObjectValidator implements ConstraintValidator<InterObjectValid, SelfInterObjectValidator> {

    public void initialize(InterObjectValid constraintAnnotation) {
    }
    
    public boolean isValid(SelfInterObjectValidator value, ConstraintValidatorContext context) {
        return value.validateObjectAgainstOthers(context);
    }
}
