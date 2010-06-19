package org.tpspencer.tal.gaetest.views;

import java.util.List;
import java.util.Map;

import org.tpspencer.tal.gaetest.GAEObjexSampleApp;
import org.tpspencer.tal.gaetest.StockServiceFactory;
import org.tpspencer.tal.gaetest.GAEObjexSampleApp.SampleAppState;
import org.tpspencer.tal.objexj.sample.api.repository.StockRepository;
import org.tpspencer.tal.objexj.sample.api.repository.StockService;
import org.tpspencer.tal.objexj.sample.api.stock.Category;
import org.tpspencer.tal.objexj.sample.api.stock.Product;

public class StockViews {

    public static class StockView implements GAEObjexSampleApp.View {
        public String prepare(SampleAppState state, Map<String, Object> model) {
            StockService stockService = StockServiceFactory.getInstance().getService();
            StockRepository repository = stockService.getRepository();
            
            List<Category> categories = null;
            List<Product> products = null;
            
            Category currentCategory = repository.findCategory(state != null ? state.getCurrentCategory() : null);
            if( currentCategory != null ) {
                categories = currentCategory.getCategories();
                products = currentCategory.getProducts();
            }
            
            if( currentCategory != null ) model.put("currentCategory", currentCategory);
            if( categories != null && categories.size() > 0 ) model.put("categories", categories);
            if( products != null && products.size() > 0 ) model.put("products", products);
            
            return "/html/stock/viewStock.jsp";
        }
    }
    
    public static class NewCategoryView implements GAEObjexSampleApp.View {
        public String prepare(SampleAppState state, Map<String, Object> model) {
            // nothing!

            return "/html/stock/newCategory.jsp";
        }
    }
    
    public static class EditCategoryView implements GAEObjexSampleApp.View {
        public String prepare(SampleAppState state, Map<String, Object> model) {
            StockService stockService = StockServiceFactory.getInstance().getService();
            StockRepository repository = stockService.getRepository();
            
            Category currentCategory = null;
            
            if( state == null || state.getCurrentCategory() == null ) {
                // TODO: Fix this is not right - should not have knowledge of IDs!!!
                currentCategory = repository.findCategory("Category|1");
            }
            else {
                currentCategory = repository.findCategory(state.getCurrentCategory());
            }
            
            if( currentCategory != null ) model.put("category", currentCategory);

            return "/html/stock/editCategory.jsp";
        }
    }
    
    public static class NewProductView implements GAEObjexSampleApp.View {
        public String prepare(SampleAppState state, Map<String, Object> model) {
            // nothing!

            return "/html/stock/newProduct.jsp";
        }
    }
    
    public static class EditProductView implements GAEObjexSampleApp.View {
        public String prepare(SampleAppState state, Map<String, Object> model) {
            StockService stockService = StockServiceFactory.getInstance().getService();
            StockRepository repository = stockService.getRepository();
            
            model.put("product", repository.findProduct(state.getCurrentProduct()));
            
            return "/html/stock/editProduct.jsp";
        }
    }
}
