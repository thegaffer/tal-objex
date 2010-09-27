package org.talframework.objexj.service.beans.rules;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.talframework.objexj.service.beans.CategoryBean;

public class CategoryValidator implements ConstraintValidator<CategoryValid, CategoryBean> {

    public void initialize(CategoryValid constraintAnnotation) {
    }
    
    public boolean isValid(CategoryBean value, ConstraintValidatorContext context) {
        if( value != null &&
                value.getName() != null && 
                value.getName().equals("RootCat_edited") ) return false;
        
        return true;
    }
}
