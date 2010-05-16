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
            return "/html/order/orders.jsp";
        }
    }
    
    public static class ViewOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            state.setCurrentOrder(RequestUtil.getParameter(request, "orderId"));
            state.setCurrentOrderTransaction(null);
            state.setCurrentOrderItem(null);
            
            return "/html/order/order.jsp";
        }
    }
    
    public static class NewOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            state.setCurrentOrder(null);
            state.setCurrentOrderTransaction(null);
            request.setAttribute("newOrder", true);
            
            return "/html/order/orderForm.jsp";
        }
    }
    
    public static class EditOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            return "/html/order/orderForm.jsp";
        }
    }
    
    public static class NewOrderItemAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            request.setAttribute("newOrderItem", true);
            return "/html/order/orderItem.jsp";
        }
    }
    
    public static class EditOrderItemAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            String item = RequestUtil.getParameter(request, "item");
            if( item != null ) state.setCurrentOrderItem(item);
            
            if( state.getCurrentOrderItem() != null ) {
                return "/html/order/orderItem.jsp";
            }
            else {
                return "/html/order/order.jsp";
            }
            
        }
    }
    
    public static class OpenOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            OrderRepository repository = service.getRepository(state.getCurrentOrder());
            repository.open();
            state.setCurrentOrderTransaction(repository.suspend());
            
            return "/html/order/order.jsp";
        }
    }
    
    public static class SaveOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            OrderRepository repository = service.getOpenRepository(state.getCurrentOrder(), state.getCurrentOrderTransaction());
            repository.persist();
            state.setCurrentOrderTransaction(null);
            
            return "/html/order/order.jsp";
        }
    }
    
    public static class SubmitOrderItemAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            OrderService service = null;
            OrderRepository repository = null;
            if( state.getCurrentOrderTransaction() != null ) {
                repository = service.getOpenRepository(state.getCurrentOrder(), state.getCurrentOrderTransaction());
            }
            else {
                repository = service.getRepository(state.getCurrentOrder());
            }
            
            Order order = repository.getOrder();
            
            String itemId = RequestUtil.getParameter(request, "id");
            
            OrderItem item = null;
            if( itemId == null ) item = order.createNewItem();
            else item = order.getItem(itemId);
            
            // Bind
            item.setName(RequestUtil.getParameter(request, "name"));
            item.setDescription(RequestUtil.getParameter(request, "description"));
            item.setPrice(RequestUtil.getDoubleParameter(request, "price"));
            // TODO: Other item parameters
            
            // Save order (unless in a transaction)
            if( state.getCurrentOrderTransaction() == null ) repository.persist();
            
            return "/html/order/order.jsp";
        }
    }
    
    public static class SubmitOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            
            // Create or get order repository
            OrderRepository repository = null;
            if( state.getCurrentOrder() == null ) {
                repository = service.createNewOrder();
            }
            else if( state.getCurrentOrderTransaction() != null ) {
                repository = service.getOpenRepository(state.getCurrentOrder(), state.getCurrentOrderTransaction());
            }
            else {
                repository = service.getRepository(state.getCurrentOrder());
            }
            
            // Bind
            Order order = repository.getOrder();
            order.setAccount(RequestUtil.getLongParameter(request, "account"));
            
            // Save order (unless in a transaction)
            if( state.getCurrentOrderTransaction() == null ) repository.persist();
            
            return "/html/order/order.jsp";
        }
    }
}
