package org.tpspencer.tal.objexj.sample.model.stock.impl;

import java.util.ArrayList;
import java.util.List;

import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ValidationRequest;
import org.tpspencer.tal.objexj.object.BaseObjexObj;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjectStrategy;
import org.tpspencer.tal.objexj.sample.api.stock.Category;
import org.tpspencer.tal.objexj.sample.api.stock.Product;
import org.tpspencer.tal.objexj.sample.beans.stock.CategoryBean;

/**
 * Manual simple {@link ObjexObj} implementation of the {@link Category}
 * domain object interface. Note in here how the set's are protected
 * against re-setting the same value and how they call ensureUpdateable,
 * which makes sure the object is in the transaction.
 *
 * @author Tom Spencer
 */
public class CategoryImpl extends BaseObjexObj implements Category {
    public static final ObjectStrategy STRATEGY = new SimpleObjectStrategy("Category", CategoryImpl.class, CategoryBean.class);
    
    private final CategoryBean bean;

    public CategoryImpl(CategoryBean state) {
        this.bean = state;
    }
    
    public String getParentCategoryId() {
        ObjexID parentId = getParentId();
        return parentId != null ? parentId.toString() : null;
    }

    public String getName() {
        return bean.getName();
    }
    
    public void setName(String name) {
        if( name == bean.getName() ) return;
        if( name != null && name.equals(bean.getName()) ) return;
        
        ensureUpdateable(bean);
        bean.setName(name);
    }
    
    public String getDescription() {
        return bean.getDescription();
    }
    
    public void setDescription(String description) {
        if( description == bean.getDescription() ) return;
        if( description != null && description.equals(bean.getDescription()) ) return;
        
        ensureUpdateable(bean);
        bean.setDescription(description);
    }
    
    public List<String> getCategoryRefs() {
        return bean.getCategories();
    }
    
    public List<Category> getCategories() {
        return getContainer().getObjectList(bean.getCategories(), Category.class);
    }
    
    public void setCategories(List<String> categories) {
        ensureUpdateable(bean);
        bean.setCategories(categories);
    }
    
    public List<String> getProductRefs() {
        return bean.getProducts();
    }
    
    public List<Product> getProducts() {
        return getContainer().getObjectList(bean.getProducts(), Product.class);
    }
    
    public void setProducts(List<String> products) {
        ensureUpdateable(bean);
        bean.setProducts(products);
    }
    
    public Category createCategory() {
        ObjexObj newCat = getInternalContainer().newObject(this, bean, "Category");
        
        ensureUpdateable(bean);
        List<String> cats = bean.getCategories();
        if( cats == null ) cats = new ArrayList<String>();
        cats.add(newCat.getId().toString());
        bean.setCategories(cats);
        
        return newCat.getBehaviour(Category.class);
    }
    
    public Product createProduct() {
        ObjexObj newProduct = getInternalContainer().newObject(this, bean, "Product");
        
        ensureUpdateable(bean);
        List<String> prods = bean.getProducts();
        if( prods == null ) prods = new ArrayList<String>();
        prods.add(newProduct.getId().toString());
        bean.setProducts(prods);
        
        return newProduct.getBehaviour(Product.class);
    }
    
    public void validate(ValidationRequest request) {
    }
}
