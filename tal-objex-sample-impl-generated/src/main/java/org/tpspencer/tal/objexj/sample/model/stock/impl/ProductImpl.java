package org.tpspencer.tal.objexj.sample.model.stock.impl;

import org.tpspencer.tal.objexj.annotations.ObjexObj;
import org.tpspencer.tal.objexj.sample.api.stock.Product;
import org.tpspencer.tal.objexj.sample.beans.stock.ProductBean;

@ObjexObj(ProductBean.class)
public class ProductImpl implements Product {

    private final ProductBean bean;
    
    public ProductImpl(ProductBean bean) {
        this.bean = bean;
    }
}
