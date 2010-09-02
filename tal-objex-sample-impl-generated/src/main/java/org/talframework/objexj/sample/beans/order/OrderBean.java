package org.talframework.objexj.sample.beans.order;

import java.util.List;

import org.talframework.objexj.annotations.ObjexRefProp;
import org.talframework.objexj.annotations.ObjexStateBean;
import org.talframework.objexj.sample.api.order.OrderItem;

@ObjexStateBean(name="Order")
public class OrderBean {
    private static final long serialVersionUID = 1L;

    private long account;
    
    @ObjexRefProp(owned=true, type=OrderItem.class, newType="OrderItem")
    private List<String> items;
    
    @ObjexRefProp(owned=true, type=OrderItem.class)
    private String test;
}
