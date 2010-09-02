package org.talframework.objexj.sample.model.stock.impl;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.annotations.ObjexObj;
import org.talframework.objexj.sample.api.stock.Category;
import org.talframework.objexj.sample.beans.stock.CategoryBean;

@ObjexObj(CategoryBean.class)
public class CategoryImpl implements Category {
    
    private CategoryBean bean;
    
    public CategoryImpl(CategoryBean bean) {
        this.bean = bean;
    }

    public String getParentCategoryId() {
        ObjexID parent = super.getParentId();
        return parent != null ? parent.toString() : null;
    }
}
