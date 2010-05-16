package org.tpspencer.tal.gaetest;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tpspencer.tal.gaetest.actions.CategoryActions;
import org.tpspencer.tal.gaetest.actions.OrderActions;
import org.tpspencer.tal.gaetest.actions.ProductActions;

/**
 * The sample app. This class represents the app and uses a very simple inbuilt MVC type architecture. Requests are dispatched to appropriate actions and from
 * there to JSP views.
 * 
 * @author Tom Spencer
 */
public class GAEObjexSampleApp extends HttpServlet {
    private final static long serialVersionUID = 1L;
    private Map<String, Action> actions = new HashMap<String, Action>();
    
    @Override
    public void init() throws ServletException {
        super.init();
    
        actions.put("/viewStock", new CategoryActions.ViewCategoryAction());
        actions.put("/viewCategory", new CategoryActions.ViewCategoryAction());
        actions.put("/editCategory", new CategoryActions.EditCategoryAction());
        actions.put("/newCategory", new CategoryActions.NewCategoryAction());
        actions.put("/submitCategory", new CategoryActions.SubmitCategoryAction());
        actions.put("/viewProduct", new ProductActions.ViewProductAction());
        actions.put("/editProduct", new ProductActions.EditProductAction());
        actions.put("/newProduct", new ProductActions.NewProductAction());
        actions.put("/submitProduct", new ProductActions.SubmitProductAction());
        
        actions.put("/viewOrders", new OrderActions.ViewOrdersAction());
        actions.put("/viewOrder", new OrderActions.ViewOrderAction());
        actions.put("/newOrder", new OrderActions.NewOrderAction());
        actions.put("/editOrder", new OrderActions.EditOrderAction());
        actions.put("/submitOrder", new OrderActions.SubmitOrderAction());
        actions.put("/newOrderItem", new OrderActions.NewOrderItemAction());
        actions.put("/editOrderItem", new OrderActions.EditOrderItemAction());
        actions.put("/submitOrderItem", new OrderActions.SubmitOrderItemAction());
        actions.put("/openOrder", new OrderActions.OpenOrderAction());
        actions.put("/saveOrder", new OrderActions.SaveOrderAction());
        
        // Initialise stores
        StockServiceFactory.getInstance().getService().ensureCreated();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        
        // Load the state
        SampleAppState state = SampleAppState.load(req);
        
        // Dispatch
        if( path.startsWith("/view/") ) {
            req.setAttribute("state", state); // So its available at render!
            req.getRequestDispatcher(path.substring(5)).include(req, resp);
        }
        else {
            String view = null;
            if( actions.containsKey(path) ) view = actions.get(path).execute(req, state);
            
            state.save(resp);
            
            if( view == null ) {
                Writer w = resp.getWriter();
                w.append("<html><head><title>Invalid Path</title></head><body>");
                w.append("<h3>Path Error!</h3>");
                w.append("<p>").append(path).append(" has no action mapping").append("</p>");
                w.append("</body></html>");
            }
            else {
                resp.sendRedirect("/sampleApp/view" + view);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
    
    /**
     * This class holds the state of the sample app.
     * This class controls what can and cannot be
     * stored across user requests.
     * 
     * @author Tom Spencer
     */
    public static class SampleAppState {
        /** The current category */
        private String currentCategory;
        /** The current product */
        private String currentProduct;
        
        /** The ID of the order (the doc ID) */
        private String currentOrder;
        /** The ID of the current order */
        private String currentOrderItem;
        /** ID of the open transaction against the order */
        private String currentOrderTransaction;
        
        /**
         * @return the currentCategory
         */
        public String getCurrentCategory() {
            return currentCategory;
        }
        /**
         * @param currentCategory the currentCategory to set
         */
        public void setCurrentCategory(String currentCategory) {
            if( "".equals(currentCategory) ) currentCategory = null;
            this.currentCategory = currentCategory;
        }
        /**
         * @return the currentProduct
         */
        public String getCurrentProduct() {
            return currentProduct;
        }
        /**
         * @param currentProduct the currentProduct to set
         */
        public void setCurrentProduct(String currentProduct) {
            if( "".equals(currentProduct) ) currentProduct = null;
            this.currentProduct = currentProduct;
        }
        /**
         * @return the currentOrder
         */
        public String getCurrentOrder() {
            return currentOrder;
        }
        /**
         * @param currentOrder the currentOrder to set
         */
        public void setCurrentOrder(String currentOrder) {
            if( "".equals(currentOrder) ) currentOrder = null;
            this.currentOrder = currentOrder;
        }
        /**
         * @return the currentOrderItem
         */
        public String getCurrentOrderItem() {
            return currentOrderItem;
        }
        /**
         * @param currentOrderItem the currentOrderItem to set
         */
        public void setCurrentOrderItem(String currentOrderItem) {
            if( "".equals(currentOrderItem) ) currentOrderItem = null;
            this.currentOrderItem = currentOrderItem;
        }
        /**
         * @return the currentOrderTransaction
         */
        public String getCurrentOrderTransaction() {
            return currentOrderTransaction;
        }
        /**
         * @param currentOrderTransaction the currentOrderTransaction to set
         */
        public void setCurrentOrderTransaction(String currentOrderTransaction) {
            if( "".equals(currentOrderTransaction) ) currentOrderTransaction = null;
            this.currentOrderTransaction = currentOrderTransaction;
        }
        
        /**
         * Call to 'save' the state of the app into cookies.
         * 
         * @param resp The current response
         */
        protected void save(HttpServletResponse resp) {
            resp.addCookie(new Cookie("category", currentCategory));
            resp.addCookie(new Cookie("product", currentProduct));
            resp.addCookie(new Cookie("order", currentOrder));
            resp.addCookie(new Cookie("orderItem", currentOrderItem));
            resp.addCookie(new Cookie("orderTransaction", currentOrderTransaction));
        }
        
        /**
         * Call to 'load' the app state from submitted cookies
         * 
         * @param req The current request
         * @return The state of the app
         */
        protected static SampleAppState load(HttpServletRequest req) {
            SampleAppState ret = new SampleAppState();
            
            Cookie[] cookies = req.getCookies();
            if( cookies != null ) {
                for( int i = 0 ; i < cookies.length ; i++ ) {
                    if( "category".equals(cookies[i].getName()) ) ret.setCurrentCategory(cookies[i].getValue());
                    else if( "product".equals(cookies[i].getName()) ) ret.setCurrentProduct(cookies[i].getValue());
                    else if( "order".equals(cookies[i].getName()) ) ret.setCurrentOrder(cookies[i].getValue());
                    else if( "orderItem".equals(cookies[i].getName()) ) ret.setCurrentOrderItem(cookies[i].getValue());
                    else if( "orderTransaction".equals(cookies[i].getName()) ) ret.setCurrentOrderTransaction(cookies[i].getValue());
                }
            }
            
            return ret;
        }
    }

    /**
     * The interface our actions should implement.
     * 
     * @author Tom Spencer
     */
    public static interface Action {

        public String execute(HttpServletRequest request, SampleAppState state);
    }
}
