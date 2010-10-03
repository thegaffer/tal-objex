package org.talframework.objexj.validation;

import java.util.Map;

import javax.validation.ConstraintValidatorContext;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ValidationError;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.ValidationRequest.ValidationType;
import org.talframework.objexj.exceptions.ObjectErrorException;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.SimpleFieldUtils;
import org.talframework.objexj.validation.groups.ChildGroup;
import org.talframework.objexj.validation.groups.FieldGroup;
import org.talframework.objexj.validation.groups.InterObjectEnrichmentGroup;
import org.talframework.objexj.validation.groups.InterObjectGroup;
import org.talframework.objexj.validation.groups.IntraObjectEnrichmentGroup;
import org.talframework.objexj.validation.groups.IntraObjectGroup;
import org.talframework.objexj.validation.object.SelfIntraObjectValidator;

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
        final DummyObjexObj obj = new DummyObjexObj(null);
        
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
        DummyObjexBean bean = new DummyObjexBean();
        bean.name = "Something"; // So this is valid
        DummyObjexObj obj = new DummyObjexObj(bean);
        
        obj.setName(null);
    }
    
    /**
     * This tests that the object checks itself
     */
    @Test
    public void selfIntraObjectChecks() {
        DummyObjexBean bean = new DummyObjexBean();
        bean.name = "Something"; // So this is valid
        DummyObjexObj obj = new DummyObjexObj(bean);
        
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

    public class DummyObjexObj extends BaseObjexObj implements SelfIntraObjectValidator {
        
        private DummyObjexBean bean;
        
        public DummyObjexObj(DummyObjexBean bean) {
            this.bean = bean;
        }
        
        @Override
        protected ObjexObjStateBean getStateBean() {
            return bean;
        }
        
        public boolean validateObject(ConstraintValidatorContext context) {
            return false;
        }
        
        public void setName(String val) {
            String rawValue = val;
            bean.setName(SimpleFieldUtils.setSimple(this, bean, "name", rawValue, bean.getName()));
        }
    }
    
    public class DummyObjexBean implements ObjexObjStateBean {
        private static final long serialVersionUID = 1L;
        
        @NotNull(groups={FieldGroup.class})
        public String name;
        
        public void create(ObjexID parentId) {
        }
        
        public void preSave(Object id) {
        }
        
        public ObjexObjStateBean cloneState() {
            return null;
        }
     
        public String getObjexObjType() {
            return null;
        }
        
        public Object getId() {
            return null;
        }
        
        public String getParentId() {
            return null;
        }
        
        public boolean isEditable() {
            return true;
        }
        
        public void setEditable() {
        }
        
        public void updateTemporaryReferences(Map<ObjexID, ObjexID> refs) {
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Setter for the name field
         *
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }
    }
}
