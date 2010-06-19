package org.tpspencer.tal.objexj.sample.model.stock.impl;

import org.tpspencer.tal.objexj.annotations.ObjexObj;
import org.tpspencer.tal.objexj.sample.beans.stock.ProductBean;

@ObjexObj(ProductBean.class)
public class ProductImpl {

    private final ProductBean bean;
    
    public ProductImpl(ProductBean bean) {
        this.bean = bean;
    }
}
