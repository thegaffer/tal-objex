package org.tpspencer.tal.gaetest.actions;

import javax.servlet.http.HttpServletRequest;

import org.tpspencer.tal.gaetest.GAEObjexSampleApp;
import org.tpspencer.tal.gaetest.StockServiceFactory;
import org.tpspencer.tal.gaetest.util.RequestUtil;
import org.tpspencer.tal.objexj.sample.api.repository.StockRepository;
import org.tpspencer.tal.objexj.sample.api.repository.StockService;
import org.tpspencer.tal.objexj.sample.api.stock.Category;

public class CategoryActions {
    
    public static class ViewCategoryAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, GAEObjexSampleApp.SampleAppState state) {
            String category = RequestUtil.getParameter(request, "category");
            if( category != null ) {
                state.setCurrentCategory(category);
            }
            
            return "/html/stock/viewStock.jsp";
        }
    }
    
    public static class EditCategoryAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, GAEObjexSampleApp.SampleAppState state) {
            String category = RequestUtil.getParameter(request, "category");
            if( category != null ) state.setCurrentCategory(category);
            
            if( state.getCurrentCategory() != null ) {
                return "/html/stock/editCategory.jsp";
            }
            else {
                return "/html/stock/viewStock.jsp";
            }
        }
    }
    
    public static class NewCategoryAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, GAEObjexSampleApp.SampleAppState state) {
            return "/html/stock/newCategory.jsp";
        }
    }
    
    public static class SubmitCategoryAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, GAEObjexSampleApp.SampleAppState state) {
            StockService stockService = StockServiceFactory.getInstance().getService();
            StockRepository repository = stockService.getOpenRepository();
            
            String categoryId = RequestUtil.getParameter(request, "id");
            String parentId = RequestUtil.getParameter(request, "parentId");
            
            // Get holds of category as appropriate (new/existing)
            Category c = null;
            if( categoryId == null ) c = repository.createNewCategory(parentId);
            else c = repository.findCategory(categoryId);
            
            // Bind
            c.setName(RequestUtil.getParameter(request, "name"));
            c.setDescription(RequestUtil.getParameter(request, "description"));
            
            // Persist the changes
            repository.persist();
            
            // Show parent (can only edit/create from parent)
            if( c.getParentId() != null ) state.setCurrentCategory(c.getParentId().toString());
            
            // Back to stock view
            return "/html/stock/viewStock.jsp";
        }
    }
}
