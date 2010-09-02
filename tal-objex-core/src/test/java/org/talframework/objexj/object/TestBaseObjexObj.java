package org.tpspencer.tal.objexj.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ValidationRequest;
import org.tpspencer.tal.objexj.container.InternalContainer;
import org.tpspencer.tal.objexj.exceptions.ObjectFieldInvalidException;
import org.tpspencer.tal.objexj.service.beans.CategoryBean;

/**
 * This test class tests the {@link BaseObjexObj} class.
 *
 * @author Tom Spencer
 */
public class TestBaseObjexObj {
    
    private Mockery context = new JUnit4Mockery();
    
    private InternalContainer container;
    private ObjexID id;
    private ObjexID parentId;
    private ObjexObj parent;
    private BaseObjexObj underTest;

    @Before
    public void setup() {
        container = context.mock(InternalContainer.class);
        id = context.mock(ObjexID.class, "id");
        parentId = context.mock(ObjexID.class, "parentId");
        parent = context.mock(ObjexObj.class, "parent");
        
        context.checking(new Expectations() {{
            allowing(id).isNull(); will(returnValue(false));
            allowing(parentId).isNull(); will(returnValue(false));
            allowing(container).getObject(parentId); will(returnValue(parent));
            allowing(container).isOpen(); will(returnValue(true));
        }});
        
        underTest = new DummyObject();
        underTest.init(container, id, parentId);
    }
    
    /**
     * Ensures the basic access to the properties
     * via Dummy
     */
    @Test
    public void basic() {
        Assert.assertEquals(id, underTest.getId());
        Assert.assertEquals("DummyObject", underTest.getType());
        Assert.assertEquals(parentId, underTest.getParentId());
        Assert.assertEquals(parent, underTest.getParent());
        Assert.assertEquals(container, underTest.getContainer());
        Assert.assertTrue(underTest.isUpdateable());
        
        Assert.assertEquals(underTest, underTest.getBehaviour(DummyObject.class));
        
        Assert.assertEquals("Cat1", underTest.getProperty("name", String.class));
        Assert.assertEquals("Cat1 Desc", underTest.getPropertyAsString("description"));
        
        final ObjexObj mainProduct = context.mock(ObjexObj.class, "mainProduct");
        context.checking(new Expectations() {{
            oneOf(container).getObject("Product/101"); will(returnValue(mainProduct));
        }});
        Assert.assertEquals(mainProduct, underTest.getProperty("mainProduct"));
        
        underTest.setProperty("name", "Cat1_edited");
        Assert.assertEquals("Cat1_edited", underTest.getProperty("name"));
        
        Assert.assertNotNull(underTest.createReference("products", null));
        
        context.assertIsSatisfied();
    }
    
    /**
     * Ensures we cannot access a property for which
     * there is not an accessor for
     */
    @Test(expected=ObjectFieldInvalidException.class)
    public void invalidProperty() {
        underTest.getProperty("invalid");
    }
    
    /**
     * Ensures we cannot set a purely read-only property
     */
    @Test(expected=ObjectFieldInvalidException.class)
    public void invalidSet() {
        underTest.setProperty("name2", "test");
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void invalidCreate() {
        underTest.createReference("Categories", "Category");
    }
    
    @Test(expected=ClassCastException.class)
    public void invalidBehaviour() {
        underTest.getBehaviour(String.class);
    }
    
    @Test
    public void testRoot() {
        context.checking(new Expectations() {{
            oneOf(parent).getRoot(); will(returnValue(parent));
        }});
        
        Assert.assertEquals(parent, underTest.getRoot());
        
        // Now try with self being root
        underTest.init(container, id, null);
        Assert.assertEquals(underTest, underTest.getRoot());
    }
    
    @Test(expected=IllegalStateException.class)
    public void testNotInitialised() {
        underTest = new DummyObject();
        underTest.getId();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testInitialiseNoId() {
        underTest.init(container, null, parentId);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testInitialiseNullId() {
        final ObjexID nullId = context.mock(ObjexID.class, "nullId");
        context.checking(new Expectations() {{
            oneOf(nullId).isNull(); will(returnValue(true));
        }});
        
        underTest.init(container, null, parentId);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testInitialiseNoContainer() {
        underTest.init(null, id, parentId);
    }
    
    
    
    /**
     * Dummy test object that extends {@link BaseObjexObj} to
     * allow the latter to be tested. 
     * 
     * <p><b>Note: </b>Do not use this as an example of an
     * {@link ObjexObj} implementation. It does not check
     * the state bean is editable or add the object to
     * the current transaction.
     *
     * @author Tom Spencer
     */
    public class DummyObject extends BaseObjexObj {
        
        private CategoryBean state;
        
        public DummyObject() {
            state = new CategoryBean();
            state.setName("Cat1");
            state.setDescription("Cat1 Desc");
            state.setMainProduct("Product/101");
            state.setEditable();
        }
        
        public void validate(ValidationRequest request) {
            
        }
        
        /**
         * Test can set method that stops us setting Error
         * on the description.
         */
        public boolean canSetDescription(String desc) {
            if( "Error".equals(desc) ) return false;
            return true;
        }
        
        /**
         * Test onSet handler that sets the old name in
         * the description field.
         */
        public void onSetName(String oldName) {
            state.setDescription(oldName);
        }
        
        //////////////////////////////////////
        // Getter / Setters
        
        /**
         * @return the name
         */
        public String getName() {
            return state.getName();
        }
        
        /**
         * @param name the name to set
         */
        public void setName(String name) {
            if( !state.isEditable() ) getInternalContainer().addObjectToTransaction(this, state);
            
            state.setName(name);
        }
        
        /**
         * @return the description
         */
        public String getDescription() {
            return state.getDescription();
        }
        
        /**
         * @param description the description to set
         */
        public void setDescription(String description) {
            if( !state.isEditable() ) getInternalContainer().addObjectToTransaction(this, state);
            
            state.setDescription(description);
        }
        
        public String getName2() {
            return state.getName();
        }
        
        /**
         * @return the mainProduct
         */
        public ObjexObj getMainProduct() {
            return getContainer().getObject(state.getMainProduct());
        }
        
        /**
         * @return the mainProduct
         */
        public String getMainProductRef() {
            return state.getMainProduct();
        }

        /**
         * Setter for the mainProduct field
         *
         * @param mainProduct the mainProduct to set
         */
        public void setMainProduct(ObjexObj mainProduct) {
            if( !state.isEditable() ) getInternalContainer().addObjectToTransaction(this, state);
            
            if( mainProduct == null ) state.setMainProduct(null);
            else state.setMainProduct(mainProduct.getId().toString());
        }
        
        /**
         * Setter for the mainProduct field
         *
         * @param mainProduct the mainProduct to set
         */
        public void setMainProductRef(String mainProduct) {
            if( !state.isEditable() ) getInternalContainer().addObjectToTransaction(this, state);
            
            state.setMainProduct(mainProduct);
        }

        /**
         * @return the products
         */
        public List<ObjexObj> getProducts() {
            return getContainer().getObjectList(state.getProducts());
        }
        
        /**
         * @return the products
         */
        public List<String> getProductsRef() {
            return state.getProducts();
        }
        
        /**
         * Creates a reference. We are only testing this is
         * called.
         * 
         * @param type
         * @return
         */
        public ObjexObj createProduct(String type) {
            return context.mock(ObjexObj.class, "newParent");
        }

        /**
         * Setter for the products field
         *
         * @param products the products to set
         */
        public void setProducts(List<ObjexObj> products) {
            if( !state.isEditable() ) getInternalContainer().addObjectToTransaction(this, state);
            
            List<String> prods = products != null ? new ArrayList<String>() : null;
            if( products != null ) {
                Iterator<ObjexObj> it = products.iterator();
                while( it.hasNext() ) {
                    prods.add(it.next().getId().toString());
                }
            }
            state.setProducts(prods);
        }
        
        /**
         * Setter for the products field
         *
         * @param products the products to set
         */
        public void setProductsRef(List<String> products) {
            if( !state.isEditable() ) getInternalContainer().addObjectToTransaction(this, state);
            
            state.setProducts(products);
        }

        /**
         * @return the categories
         */
        public Map<? extends Object, ObjexObj> getCategories() {
            return getContainer().getObjectMap(state.getCategories());
        }
        
        /**
         * @return the categories
         */
        public Map<String, String> getCategoriesRef() {
            return state.getCategories();
        }

        /**
         * Setter for the categories field
         *
         * @param categories the categories to set
         */
        public void setCategories(Map<String, ObjexObj> categories) {
            if( !state.isEditable() ) getInternalContainer().addObjectToTransaction(this, state);
            
            Map<String, String> cats = categories != null ? new HashMap<String, String>() : null;
            if( categories != null ) {
                Iterator<String> it = categories.keySet().iterator();
                while( it.hasNext() ) {
                    String k = it.next();
                    ObjexObj val = categories.get(k);
                    cats.put(k, val != null ? val.getId().toString() : null);
                }
            }
            state.setCategories(cats);
        }
        
        /**
         * Setter for the categories field
         *
         * @param categories the categories to set
         */
        public void setCategoriesRef(Map<String, String> categories) {
            if( !state.isEditable() ) getInternalContainer().addObjectToTransaction(this, state);
            
            state.setCategories(categories);
        }
    }
}