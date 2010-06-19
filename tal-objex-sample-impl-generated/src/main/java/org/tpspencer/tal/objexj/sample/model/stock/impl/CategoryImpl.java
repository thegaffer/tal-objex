package org.tpspencer.tal.objexj.sample.model.stock.impl;

import org.tpspencer.tal.objexj.annotations.ObjexObj;
import org.tpspencer.tal.objexj.sample.api.stock.Category;
import org.tpspencer.tal.objexj.sample.beans.stock.CategoryBean;

@ObjexObj(CategoryBean.class)
public class CategoryImpl implements Category {
    
    private CategoryBean bean;
    
    public CategoryImpl(CategoryBean bean) {
        this.bean = bean;
    }

    public String getParentCategoryId() {
        return bean.getParentId();
    }
}
