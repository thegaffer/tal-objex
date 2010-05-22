package org.tpspencer.tal.gaetest.actions;

import javax.servlet.http.HttpServletRequest;

import org.tpspencer.tal.gaetest.GAEObjexSampleApp;
import org.tpspencer.tal.gaetest.OrderServiceFactory;
import org.tpspencer.tal.gaetest.GAEObjexSampleApp.SampleAppState;
import org.tpspencer.tal.gaetest.util.RequestUtil;
import org.tpspencer.tal.objexj.sample.api.order.Order;
import org.tpspencer.tal.objexj.sample.api.order.OrderItem;
import org.tpspencer.tal.objexj.sample.api.repository.OrderRepository;
import org.tpspencer.tal.objexj.sample.api.repository.OrderService;

public class OrderActions {
    
    public static class ViewOrdersAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            return "/viewOrders";
        }
    }
    
    public static class ViewOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            state.setCurrentOrder(RequestUtil.getParameter(request, "orderId"));
            state.setCurrentOrderTransaction(null);
            state.setCurrentOrderItem(null);
            
            return "/viewOrder";
        }
    }
    
    public static class NewOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            state.setCurrentOrder(null);
            state.setCurrentOrderTransaction(null);
            request.setAttribute("newOrder", true);
            
            return "/newOrderForm";
        }
    }
    
    public static class EditOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            return "/orderForm";
        }
    }
    
    public static class NewOrderItemAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            request.setAttribute("newOrderItem", true);
            return "/newOrderItem";
        }
    }
    
    public static class EditOrderItemAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            String item = RequestUtil.getParameter(request, "item");
            if( item != null ) state.setCurrentOrderItem(item);
            
            if( state.getCurrentOrderItem() != null ) {
                return "editOrderItem";
            }
            else {
                return "/viewOrder";
            }
            
        }
    }
    
    public static class OpenOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            OrderRepository repository = service.getOpenRepository(state.getCurrentOrder(), null);
            state.setCurrentOrderTransaction(repository.suspend());
            
            return "/viewOrder";
        }
    }
    
    public static class SaveOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            OrderRepository repository = service.getOpenRepository(state.getCurrentOrder(), state.getCurrentOrderTransaction());
            repository.persist();
            state.setCurrentOrderTransaction(null);
            
            return "/viewOrder";
        }
    }
    
    public static class SubmitOrderItemAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            OrderRepository repository = service.getOpenRepository(state.getCurrentOrder(), state.getCurrentOrderTransaction());
            
            Order order = repository.getOrder();
            
            String itemId = RequestUtil.getParameter(request, "id");
            
            OrderItem item = null;
            if( itemId == null ) item = order.createNewItem();
            else item = order.getItem(itemId);
            
            // Bind
            item.setName(RequestUtil.getParameter(request, "name"));
            item.setDescription(RequestUtil.getParameter(request, "description"));
            item.setPrice(RequestUtil.getDoubleParameter(request, "price"));
            item.setCurrency(RequestUtil.getParameter(request, "currency"));
            item.setQuantity(RequestUtil.getDoubleParameter(request, "quantity"));
            item.setMeasure(RequestUtil.getParameter(request, "measure"));
            
            state.setCurrentOrderTransaction(repository.suspend());
            
            return "/viewOrder";
        }
    }
    
    public static class SubmitNewOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            
            String account = RequestUtil.getParameter(request, "account");
            String name = RequestUtil.getParameter(request, "name");
            String orderId = account + "/" + name;
            
            OrderRepository repository = service.createNewOrder(orderId);
            repository.getOrder().setAccount(Long.parseLong(account));
            
            state.setCurrentOrder(repository.getId());
            state.setCurrentOrderTransaction(repository.suspend());
            
            return "/viewOrder";
        }
    }
    
    public static class SubmitOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            OrderRepository repository = service.getOpenRepository(state.getCurrentOrder(), state.getCurrentOrderTransaction());
            
            // Bind
            Order order = repository.getOrder();
            order.setAccount(RequestUtil.getLongParameter(request, "account"));
            
            state.setCurrentOrderTransaction(repository.suspend());
            
            return "/viewOrder";
        }
    }
}
