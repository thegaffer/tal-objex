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
package org.talframework.objexj.sample.gaetest.actions;

import javax.servlet.http.HttpServletRequest;

import org.talframework.objexj.sample.api.repository.StockRepository;
import org.talframework.objexj.sample.api.repository.StockService;
import org.talframework.objexj.sample.api.stock.Product;
import org.talframework.objexj.sample.gaetest.GAEObjexSampleApp;
import org.talframework.objexj.sample.gaetest.StockServiceFactory;
import org.talframework.objexj.sample.gaetest.util.RequestUtil;

public class ProductActions {

    public static class ViewProductAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, GAEObjexSampleApp.SampleAppState state) {
            String product = RequestUtil.getParameter(request, "product");
            if( product != null ) {
                state.setCurrentProduct(product);
                return "/viewProduct";
            }
            else {
                return "/viewStock";
            }
        }
    }
    
    public static class EditProductAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, GAEObjexSampleApp.SampleAppState state) {
            String product = RequestUtil.getParameter(request, "product");
            if( product != null ) state.setCurrentProduct(product);
            
            if( state.getCurrentProduct() != null ) {
                return "/editProduct";
            }
            else {
                return "/viewStock";
            }
        }
    }
    
    public static class NewProductAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, GAEObjexSampleApp.SampleAppState state) {
            return "/newProduct";
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
            return "/viewStock";
        }
    }
}
