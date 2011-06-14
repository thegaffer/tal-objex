/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.object;

import java.lang.reflect.Proxy;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.Event;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.QueryRequest;
import org.talframework.objexj.QueryResult;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.container.InternalContainer;

/**
 * This class tests the proxy objex obj implementation.
 *
 * @author Tom Spencer
 */
public class TestProxyObjexObj {
    private static final Log logger = LogFactory.getLog(TestProxyObjexObj.class);

    /**
     * This test serves to illustrate how much time we are loosing by using Java
     * proxy objects over physical objects. The recommendation is that proxy objects
     * are replaced once the system is constructed.
     * 
     * <p>Here are the conclusions on my machine, a two year run of the mill PC
     * using the Sun JVM:</p>
     * <ul>
     * <li>It takes 7.5micro-seconds to create a proxy object</li>
     * <li>So 100,000 proxy objects take 750ms</li>
     * <li>It takes 2/2.5 times slower to access an object. 1400ns vs 600ns for a set.
     * This is basically the difference in using reflection.</li>
     * <li>When I get a reading, get is similar, 200ns vs 500ns, but often I don't get
     * readings here.</li>
     * <li>Overall 100,000 creates, sets and reads takes less than 1 second which in
     * reality represents 1000s os users (this test allows for 2 seconds)</li>
     * </ul>
     * 
     * <p>So in conclusion using proxy objects is going to slow things down and you
     * should consider making your business objects implement the {@link ObjexObj}
     * interface under extreme load.</p>
     */
    @Test
    public void proxyPerf() {
        // InternalContainer container = mock(InternalContainer.class);
        // when(container.isOpen()).thenReturn(true);
        // Cannot use mockito as otherwise the performance of set falls off a cliff
        InternalContainer container = new TestContainer();
        
        int testRun = 100000;
        
        // a. Create objects normally
        Product[] normal = new Product[testRun];
        long st = System.currentTimeMillis();
        for( int i = 0 ; i < testRun ; i++ ) {
            normal[i] = new ProductImpl();
        }
        long normalTime = System.currentTimeMillis() - st;
        
        // b. Create objects and a proxy
        Product[] proxy = new Product[testRun];
        st = System.currentTimeMillis();
        for( int i = 0 ; i < testRun ; i++ ) {
            proxy[i] = (Product)Proxy.newProxyInstance(this.getClass().getClassLoader(), 
                    new Class<?>[]{InternalObjexObj.class, Product.class}, new ProxyObjexObj(null, new ProductImpl()));
        }
        long proxyTime = System.currentTimeMillis() - st;
        
        for( int i = 0 ; i < testRun ; i++ ) ((InternalObjexObj)proxy[i]).init(container, new DefaultObjexID("Def", 1), null);
        
        // c. Set a property on the normal objects
        st = System.currentTimeMillis();
        for( int i = 0 ; i < testRun ; i++ ) {
            normal[i].setName("Product" + (i + 1));
        }
        long normalSetTime = System.currentTimeMillis() - st;
        
        // d. Set a property on the proxy objects
        st = System.currentTimeMillis();
        for( int i = 0 ; i < testRun ; i++ ) {
            proxy[i].setName("Product" + (i + 1));
        }
        long proxySetTime = System.currentTimeMillis() - st;
        
        // e. Get a property on the normal objects
        st = System.currentTimeMillis();
        for( int i = 0 ; i < testRun ; i++ ) {
            String name = normal[i].getName();
            Assert.assertTrue(name.startsWith("Product"));
        }
        long normalGetTime = System.currentTimeMillis() - st;
        
        // f. Get a property on the proxy objects
        st = System.currentTimeMillis();
        for( int i = 0 ; i < testRun ; i++ ) {
            String name = proxy[i].getName();
            Assert.assertTrue(name.startsWith("Product"));
        }
        long proxyGetTime = System.currentTimeMillis() - st;
        
        logger.info("Time for normal object creation ...: " + normalTime + " (" + determinePerObject(normalTime, testRun) + "ms)");
        logger.info("Time for proxy object creation  ...: " + proxyTime + " (" + determinePerObject(proxyTime, testRun) + "ms)");
        logger.info("Time for normal object set      ...: " + normalSetTime + " (" + determinePerObject(normalSetTime, testRun) + "ms)");
        logger.info("Time for proxy object set      ...: " + proxySetTime + " (" + determinePerObject(proxySetTime, testRun) + "ms)");
        logger.info("Time for normal object get      ...: " + normalGetTime + " (" + determinePerObject(normalGetTime, testRun) + "ms)");
        logger.info("Time for proxy object get      ...: " + proxyGetTime + " (" + determinePerObject(proxyGetTime, testRun) + "ms)");
        
        // Ensure this whole method is done in less than 2 secs, otherwise shows issues!!
        Assert.assertTrue((normalTime + normalGetTime + normalSetTime + proxyTime + proxyGetTime + proxySetTime) < 2000);
        // Not using time so we get just to core elements
    }
    
    /**
     * Internal helper for formatting the time readings nicely
     */
    private String determinePerObject(long time, int nosObjects) {
        double t = time;
        t = t/nosObjects;
        
        DecimalFormat df = new DecimalFormat("0.0000");
        return df.format(t);
    }
    
    /**
     * Interface to our business object that we will proxy in these tests
     *
     * @author Tom Spencer
     */
    private static interface Product {
        public String getRef();
        public void setRef(String ref);
        public String getName();
        public void setName(String name);
        public String getDescription();
        public void setDescription(String description);
        public double getPrice();
        public void setPrice(double price);
        public String getCurrency();
        public void setCurrency(String currency);
    }
    
    /**
     * Implementation of our business object that we will proxy in these tests
     *
     * @author Tom Spencer
     */
    private static class ProductImpl implements Product {
        private String ref = null;
        private String name = null;
        private String description = null;
        private double price = 0.0;
        private String currency = null;
        
        public String getRef() {
            return ref;
        }
        public void setRef(String ref) {
            this.ref = ref;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public double getPrice() {
            return price;
        }
        public void setPrice(double price) {
            this.price = price;
        }
        public String getCurrency() {
            return currency;
        }
        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
    
    /**
     * This test container exists because when testing the performance of proxy 
     * objects any set method on an object will ask the container if it is open.
     * If we use a mock for the container then performance falls off a cliff so
     * we have this container instead. Only the isOpen method needs to do anything
     * in our tests.
     *
     * @author Tom Spencer
     */
    private static class TestContainer implements InternalContainer {

        @Override
        public String getId() {
            return null;
        }

        @Override
        public String getType() {
            return null;
        }

        @Override
        public ObjexObj getRootObject() {
            return null;
        }

        @Override
        public ObjexID getIdOfObject(Object obj) {
            return null;
        }

        @Override
        public <T> T getObject(Object id, Class<T> expected) {
            return null;
        }

        @Override
        public ObjexObj getObject(Object id) {
            return null;
        }

        @Override
        public List<ObjexObj> getObjectList(List<? extends Object> ids) {
            return null;
        }

        @Override
        public <T> List<T> getObjectList(List<? extends Object> ids, Class<T> expectedElement) {
            return null;
        }

        @Override
        public Map<String, ObjexObj> getObjectMap(Map<String, ? extends Object> ids) {
            return null;
        }

        @Override
        public <T> Map<String, T> getObjectMap(Map<String, ? extends Object> ids, Class<T> expectedElement) {
            return null;
        }

        @Override
        public QueryResult executeQuery(QueryRequest request) {
            return null;
        }

        @Override
        public void processEvent(Event event) {
        }

        @Override
        public Container openContainer() {
            return null;
        }

        @Override
        public boolean isNew() {
            return false;
        }

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public ValidationRequest validate() {
            return null;
        }

        @Override
        public String saveContainer() {
            return null;
        }

        @Override
        public void closeContainer() {
        }

        @Override
        public String suspend() {
            return null;
        }

        @Override
        public ObjexObj createObject(ObjexObj parentObj, Object source) {
            return null;
        }

        @Override
        public void removeObject(ObjexObj obj) {
        }
        
    }
}
