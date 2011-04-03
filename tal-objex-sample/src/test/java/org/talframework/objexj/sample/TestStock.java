package org.talframework.objexj.sample;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.impl.SimpleContainerStrategy;
import org.talframework.objexj.container.middleware.InMemoryMiddlewareFactory;
import org.talframework.objexj.locator.ContainerFactory;
import org.talframework.objexj.locator.SimpleContainerFactory;
import org.talframework.objexj.sample.stock.Category;
import org.talframework.objexj.sample.stock.CategoryImpl;
import org.talframework.objexj.sample.stock.Product;
import org.talframework.objexj.sample.stock.ProductImpl;

/**
 * This class demonstrates how to get hold of the Stock container
 * and access the objects within it. This serves as a good 
 * demonstration/example of how to use Objex.
 *
 * @author Tom Spencer
 */
public class TestStock {
    
    /** The container factory that will get hold of the stock store */
    private static ContainerFactory locator = null;
    
    /**
     * Simple setup script that creates the container factory. This is done inside
     * a class level setup script to highlight that you would setup your container
     * factories at startup - if using a DI framework, then set these factories up
     * inside that configuration.
     * 
     * TODO: Rename container factory to locator and have a StoreLocator and a DocumentLocator??
     */
    @BeforeClass
    public static void createLocator() {
        // This just set's up the 'strategy' object for the container that describes
        // the container. We pass in here our basic POJO classes that represent the
        // objects that can be held in the container. The first one is the 'root' 
        // object. We also pass in the name of the container and, as this container is
        // a store (i.e. there is just 1 Stock Document holding all our products) we
        // pass in the fixed ID of the container.
        ContainerStrategy strategy = null; // TODO: new SimpleContainerStrategy("Order", "Order", CategoryImpl.class, ProductImpl.class);
        
        // This line just sets up a simple factory serving the stock document with the 
        // in-memory middleware
        locator = new SimpleContainerFactory(strategy, new InMemoryMiddlewareFactory());
    }
    
    public void setup() {
        
    }
    
    public void teardown() {
        
    }

    /**
     * Simple test showing how to get hold of the store
     */
    @Test
    public void basic() {
        // This line get's a handle to the stock
        Container container = locator.get("Stock");
        
        // This line gets the root object, this will always be present
        ObjexObj obj = container.getRootObject();
        Assert.assertNotNull(obj);
        
        // Further the object will implement the category interface, but not the category impl
        Assert.assertTrue(obj instanceof Category);
        Assert.assertFalse(obj instanceof CategoryImpl);
        
        // NOTE: Typically you should not cast ObjexObj, but instead query for behaviour
        // This is done to allow advanced object to vary the interfaces they expose 
        // according to state.
        Assert.assertNotNull(obj.getBehaviour(Category.class));
        Assert.assertNull(obj.getBehaviour(CategoryImpl.class));
    }
    
    /**
     * This test demonstrates basic interaction with the objects
     */
    @Test
    public void basicInteraction() {
        Container container = locator.get("Stock");

        // Typicaly you will get the root object, get the natural interface from it
        // via getBehaviour and simply use that object. You could cast the rootObject
        // into a Category, but as per note above it is encouraged to use getBehaviour
        // to support more advanced and dynamic objects.
        Category category = container.getRootObject().getBehaviour(Category.class);
        Assert.assertEquals("Category 1", category.getName());
        
        // Getting another object is just as simple. However, the trick is that the 
        // object you know have is also an Objex object - as can be seen from the assert
        Product product = category.getProducts().get(0);
        Assert.assertTrue(product instanceof ObjexObj);
        
        // You can get objects from the container directory if you know the ID, but
        // please note the ID of an object is an Opaque identifier and no meaning
        // should be deduced from its value.
        Product product1 = container.getObject("Product|2", Product.class);
        Assert.assertEquals(product.getName(), product1.getName());
        // Note: It is container specific behaviour to determine whether product and
        // product1 in this instance are the same class. They may be they may not be!
    }
    
    /**
     * This test demonstrates the same interaction, but using the Objex Obj native
     * interface. Typically you won't use this if your objects have natural interfaces,
     * but it can be used if you wish. No narrative is provided, but this is the similar
     * as the test above.
     */
    @Test
    public void objexInteraction() {
        Container container = locator.get("Stock");
        
        ObjexObj category = container.getRootObject();
        Assert.assertEquals("Category 1", category.getProperty("name"));
        Assert.assertTrue(category.getProperty("name", List.class).get(0) instanceof ObjexObj);
        
        ObjexObj product = container.getObject("Product|2");
        Assert.assertEquals("Product 1", product.getProperty("name"));
    }
    
    /**
     * This test shows how to edit an object and then save the 
     * container
     */
    @Test
    public void basicEditing() {
        // Note this time we open the container instead of simply getting it
        Container container = locator.open("Stock");
        
        // Now just edit elements in the container
        Category category = container.getRootObject().getBehaviour(Category.class);
        category.setName("Edited Category");
        
        Product product = category.getProducts().get(0);
        product.setName("Edited Product");
        product.setPrice(12.49);

        // Validate. This is optional, but the container is validated when you save 
        // and a ContainerInvalidException (runtime) exception is thrown. If you are
        // happy catching that then you don't need to explicitly validate
        ValidationRequest validation = container.validate();
        if( validation.hasErrors() ) {
            // Do something about it, we don't expect any so are throwing a runtime exception
            throw new RuntimeException("Errors are not part of the test, throwing exception to make sure code not broken");
        }
        
        // Now save the container
        container.saveContainer();
        
        // And a few tests to make sure our changes were persisted
        container = null;
        container = locator.get("Stock");
        category = container.getRootObject().getBehaviour(Category.class);
        Assert.assertEquals("Edited Category", category.getName());
        Assert.assertEquals("Edited Product", category.getProducts().get(0).getName());
        Assert.assertEquals(12.49, category.getProducts().get(0).getPrice(), 0);
    }
    
    /**
     * This test shows how to edit the relations of an object including
     * adding and removing children.
     */
    @Test
    public void editingRelations() {
        Container container = locator.open("Stock");
        Category category = container.getRootObject().getBehaviour(Category.class);
        
        // So now lets create our new product as an instance of the Product interface
        // We are creating this as an instance of ProductImpl, this is significant
        Product product = new ProductImpl();
        product.setName("Hello");
        product.setDescription("The Hello product");
        product.setPrice(9.99);
        
        // Now we just need to add it to the products for the category
        Assert.assertEquals(1, category.getProducts().size());
        category.getProducts().add(product);
        Assert.assertEquals(2, category.getProducts().size());
        
        // Although the passed-in product instance is a valid object it really should
        // not be used anymore. It is not magically an Objex object and if we try
        // to make another product reference this product it will fail
        try {
            category.getProducts().get(0).setNearestProduct(product);
            Assert.fail("We should not get here because above is invalid");
        }
        catch( Exception e ) {} // Do nothing - left to the read to look at what this exception is!!
        
        // We must re-get the product from the category to do this
        Product originalProduct = category.getProducts().get(0);
        Product newProduct = category.getProducts().get(1);
        originalProduct.setNearestProduct(newProduct);
        
        // Some assert's so you believe me about these objects!
        Assert.assertEquals("Product 1", originalProduct.getName());
        Assert.assertEquals("Hello", newProduct.getName());
        Assert.assertNotNull(originalProduct.getNearestProduct());
        Assert.assertEquals(originalProduct.getNearestProduct().getName(), newProduct);
        
        // As an alternative to saving (the teardown will clear anyway), heres an example
        // of aborting the changes made to a container.
        container.closeContainer();
    }
}
