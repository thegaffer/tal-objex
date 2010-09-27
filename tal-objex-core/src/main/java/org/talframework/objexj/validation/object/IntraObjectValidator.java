package org.talframework.objexj.validation.object;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.talframework.objexj.validation.groups.IntraObjectGroup;

/**
 * This is the validator for custom intra object validation.
 * This is used by the Objex generators and will call any
 * @ObjexCheck method (marked as belonging to the 
 * {@link IntraObjectGroup} group). It typically does nothing
 * if you have no such methods.
 *
 * @author Tom Spencer
 */
public class IntraObjectValidator implements ConstraintValidator<IntraObjectValid, SelfIntraObjectValidator> {

    public void initialize(IntraObjectValid constraintAnnotation) {
    }
    
    public boolean isValid(SelfIntraObjectValidator value, ConstraintValidatorContext context) {
        return value.validateObject(context);
    }
}
