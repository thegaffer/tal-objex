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
