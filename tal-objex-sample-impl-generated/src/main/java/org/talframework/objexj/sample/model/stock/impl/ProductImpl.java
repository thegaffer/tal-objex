package org.talframework.objexj.sample.model.stock.impl;

import org.talframework.objexj.annotations.ObjexObj;
import org.talframework.objexj.sample.api.stock.Product;
import org.talframework.objexj.sample.beans.stock.ProductBean;

@ObjexObj(ProductBean.class)
public class ProductImpl implements Product {

    private final ProductBean bean;
    
    public ProductImpl(ProductBean bean) {
        this.bean = bean;
    }
}
