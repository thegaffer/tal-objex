package org.talframework.objexj.validation;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.talframework.objexj.ValidationError;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.ValidationRequest.ValidationType;
import org.talframework.objexj.exceptions.ObjectErrorException;
import org.talframework.objexj.object.testbeans.ProductBean;
import org.talframework.objexj.object.testbeans.StockBean;
import org.talframework.objexj.object.testmodel.ProductImpl;
import org.talframework.objexj.object.testmodel.StockImpl;
import org.talframework.objexj.validation.groups.ChildGroup;
import org.talframework.objexj.validation.groups.FieldGroup;
import org.talframework.objexj.validation.groups.InterObjectEnrichmentGroup;
import org.talframework.objexj.validation.groups.InterObjectGroup;
import org.talframework.objexj.validation.groups.IntraObjectEnrichmentGroup;
import org.talframework.objexj.validation.groups.IntraObjectGroup;

/**
 * This class tests the basic validation strategy available
 * inside the validator.
 *
 * @author Tom Spencer
 */
public class TestValidation {
    
    private Mockery context = new JUnit4Mockery();
    
    /**
     * This test mocks out the validator to ensure the basic
     * method works
     */
    @Test
    public void basic() {
        final ValidationRequest request = context.mock(ValidationRequest.class);
        final Validator validator = context.mock(Validator.class);
        final StockImpl obj = new StockImpl(new StockBean());
        
        context.checking(new Expectations() {{
            allowing(request).getValidator(); will(returnValue(validator));
            
            // Intra Object Level
            oneOf(request).getValidationType(); will(returnValue(ValidationType.INTRA_OBJECT));
            oneOf(validator).validate(obj, IntraObjectEnrichmentGroup.class); will(returnValue(null));
            oneOf(validator).validate(obj, IntraObjectGroup.class, FieldGroup.class, Default.class); will(returnValue(null));
            
            // Inter Object Level
            oneOf(request).getValidationType(); will(returnValue(ValidationType.INTER_OBJECT));
            oneOf(validator).validate(obj, InterObjectEnrichmentGroup.class); will(returnValue(null));
            oneOf(validator).validate(obj, InterObjectGroup.class); will(returnValue(null));
            
            // Child Level
            oneOf(request).getValidationType(); will(returnValue(ValidationType.CHILDREN));
            oneOf(validator).validate(obj, ChildGroup.class); will(returnValue(null));
        }});
        
        obj.validate(request); // Intra
        obj.validate(request); // Inter
        obj.validate(request); // Child
        
        context.assertIsSatisfied();
    }
    
    /**
     * This tests that we validate correctly when setting a method
     */
    @Test(expected=ObjectErrorException.class)
    public void onSet() {
        ProductBean bean = new ProductBean("Product1", "Product1", 1, 10);
        ProductImpl obj = new ProductImpl(bean);
        
        obj.setName(null);
    }
    
    /**
     * This tests that the object checks itself
     */
    @Test
    public void selfIntraObjectChecks() {
        ProductBean bean = new ProductBean("Product1", "Product1", 1, 10);
        ProductImpl obj = new ProductImpl(bean);
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final javax.validation.Validator validator = factory.getValidator();
        final ValidationRequest request = context.mock(ValidationRequest.class);
        final ValidationError expected = new SimpleValidationRequest.SimpleValidationError(null, "invalid.object.internal", null, new Object[]{obj});
        
        context.checking(new Expectations() {{
            oneOf(request).getValidator(); will(returnValue(validator));
            oneOf(request).getValidationType(); will(returnValue(ValidationType.INTRA_OBJECT));
            oneOf(request).addError(with(expected));
        }});
        
        obj.validate(request);
    }
}
