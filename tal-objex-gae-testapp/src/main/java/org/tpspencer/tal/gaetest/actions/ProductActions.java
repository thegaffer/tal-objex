package org.tpspencer.tal.gaetest.actions;

import javax.servlet.http.HttpServletRequest;

import org.tpspencer.tal.gaetest.GAEObjexSampleApp;
import org.tpspencer.tal.gaetest.StockServiceFactory;
import org.tpspencer.tal.gaetest.util.RequestUtil;
import org.tpspencer.tal.objexj.sample.api.repository.StockRepository;
import org.tpspencer.tal.objexj.sample.api.repository.StockService;
import org.tpspencer.tal.objexj.sample.api.stock.Product;

public class ProductActions {

    public static class ViewProductAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, GAEObjexSampleApp.SampleAppState state) {
            String product = RequestUtil.getParameter(request, "product");
            if( product != null ) {
                state.setCurrentProduct(product);
                return "/html/stock/viewProduct.jsp";
            }
            else {
                return "/html/stock/viewStock.jsp";
            }
        }
    }
    
    public static class EditProductAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, GAEObjexSampleApp.SampleAppState state) {
            String product = RequestUtil.getParameter(request, "product");
            if( product != null ) state.setCurrentProduct(product);
            
            if( state.getCurrentProduct() != null ) {
                return "/html/stock/editProduct.jsp";
            }
            else {
                return "/html/stock/viewStock.jsp";
            }
        }
    }
    
    public static class NewProductAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, GAEObjexSampleApp.SampleAppState state) {
            return "/html/stock/newProduct.jsp";
        }
    }
    
    public static class SubmitProductAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, GAEObjexSampleApp.SampleAppState state) {
            StockService stockService = StockServiceFactory.getInstance().getService();
            StockRepository repository = stockService.getOpenRepository();
            
            String productId = RequestUtil.getParameter(request, "id");
            String parentId = RequestUtil.getParameter(request, "parentId");
            
            // Get holds of category as appropriate (new/existing)
            Product p = null;
            if( productId == null ) p = repository.createNewProduct(parentId);
            else p = repository.findProduct(productId);
            
            // Bind
            p.setName(RequestUtil.getParameter(request, "name"));
            p.setDescription(RequestUtil.getParameter(request, "description"));
            p.setPrice(RequestUtil.getDoubleParameter(request, "price"));
            p.setCurrency(RequestUtil.getParameter(request, "currency"));
            p.setEffectiveFrom(RequestUtil.getDateParameter(request, "effectiveFrom"));
            p.setEffectiveTo(RequestUtil.getDateParameter(request, "effectiveTo"));
            
            // Persist the changes
            repository.persist();
            
            // Back to stock view
            return "/html/stock/viewStock.jsp";
        }
    }
}
