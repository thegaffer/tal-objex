package org.talframework.objexj.sample.gaetest.actions;

import javax.servlet.http.HttpServletRequest;

import org.talframework.objexj.sample.api.repository.StockRepository;
import org.talframework.objexj.sample.api.repository.StockService;
import org.talframework.objexj.sample.api.stock.Category;
import org.talframework.objexj.sample.gaetest.GAEObjexSampleApp;
import org.talframework.objexj.sample.gaetest.StockServiceFactory;
import org.talframework.objexj.sample.gaetest.util.RequestUtil;

public class CategoryActions {
    
    public static class ViewCategoryAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, GAEObjexSampleApp.SampleAppState state) {
            String category = RequestUtil.getParameter(request, "category");
            if( category != null ) {
                state.setCurrentCategory(category);
            }
            
            return "/viewStock";
        }
    }
    
    public static class EditCategoryAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, GAEObjexSampleApp.SampleAppState state) {
            String category = RequestUtil.getParameter(request, "category");
            if( category != null ) state.setCurrentCategory(category);
            
            if( state.getCurrentCategory() != null ) {
                return "/editCategory";
            }
            else {
                return "/viewStock";
            }
        }
    }
    
    public static class NewCategoryAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, GAEObjexSampleApp.SampleAppState state) {
            return "/newCategory";
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
            if( c.getParentCategoryId() != null ) state.setCurrentCategory(c.getParentCategoryId().toString());
            
            // Back to stock view
            return "/viewStock";
        }
    }
}
