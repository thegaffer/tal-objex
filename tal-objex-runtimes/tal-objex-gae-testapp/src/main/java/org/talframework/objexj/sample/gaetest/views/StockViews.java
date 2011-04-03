/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.objexj.sample.gaetest.views;

import java.util.List;
import java.util.Map;

import org.talframework.objexj.sample.api.repository.StockRepository;
import org.talframework.objexj.sample.api.repository.StockService;
import org.talframework.objexj.sample.api.stock.Category;
import org.talframework.objexj.sample.api.stock.Product;
import org.talframework.objexj.sample.gaetest.GAEObjexSampleApp;
import org.talframework.objexj.sample.gaetest.StockServiceFactory;
import org.talframework.objexj.sample.gaetest.GAEObjexSampleApp.SampleAppState;

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
