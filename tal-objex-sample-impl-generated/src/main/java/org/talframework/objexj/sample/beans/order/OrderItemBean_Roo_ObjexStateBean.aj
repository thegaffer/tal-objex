// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.talframework.objexj.sample.beans.order;

import java.lang.Object;
import java.lang.String;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.object.utils.StateBeanUtils;
import org.talframework.util.beans.BeanComparison;

privileged aspect OrderItemBean_Roo_ObjexStateBean {
    
    declare parents: OrderItemBean implements ObjexObjStateBean;
    
    declare @type: OrderItemBean: @PersistenceCapable;
    
    declare @type: OrderItemBean: @XmlRootElement;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String OrderItemBean.id;
    
    @Persistent(column = "parentId")
    private String OrderItemBean.parentId;
    
    @NotPersistent
    private transient boolean OrderItemBean._editable;
    
    @NotPersistent
    private transient int OrderItemBean.setFields;
    
    @NotPersistent
    private transient int OrderItemBean.changedFields;
    
    public OrderItemBean.new() {
        super();
        _editable = false;
    }

    @XmlAttribute
    @XmlID
    public String OrderItemBean.getId() {
        return this.id;
    }
    
    public void OrderItemBean.setId(String val) {
        if( this.id != null ) throw new IllegalArgumentException("You cannot set a parent ID on an object once it is set");
        this.id = val;
    }
    
    @XmlAttribute
    public String OrderItemBean.getParentId() {
        return this.parentId;
    }
    
    public void OrderItemBean.setParentId(String val) {
        if( this.parentId != null ) throw new IllegalArgumentException("You cannot set a parent ID on an object once it is set");
        this.parentId = val;
    }
    
    @XmlTransient
    public boolean OrderItemBean.isEditable() {
        return _editable;
    }
    
    public void OrderItemBean.setEditable() {
        _editable = true;
    }
    
    @XmlTransient
    public String OrderItemBean.getObjexObjType() {
        return "OrderItem";
    }
    
    public void OrderItemBean.create(ObjexID parentId) {
        this.parentId = parentId != null ? parentId.toString() : null;
    }
    
    public void OrderItemBean.preSave(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public ObjexObjStateBean OrderItemBean.cloneState() {
        OrderItemBean ret = new OrderItemBean();
        ret.ref = this.ref;
        ret.name = this.name;
        ret.description = this.description;
        ret.stockItem = this.stockItem;
        ret.quantity = this.quantity;
        ret.measure = this.measure;
        ret.price = this.price;
        ret.currency = this.currency;
        ret.id = this.id;
        ret.parentId = this.parentId;
        return ret;
    }
    
    public String OrderItemBean.toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OrderItemBean: { ");
        builder.append("id=").append(getId());
        builder.append("parentId=").append(getParentId());
        builder.append("ref=").append(ref);
        builder.append("name=").append(name);
        builder.append("description=").append(description);
        builder.append("stockItem=").append(stockItem);
        builder.append("quantity=").append(quantity);
        builder.append("measure=").append(measure);
        builder.append("price=").append(price);
        builder.append("currency=").append(currency);
        return builder.append(" }").toString();
    }
    
    public int OrderItemBean.hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((ref == null) ? 0 : ref.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((stockItem == null) ? 0 : stockItem.hashCode());
        long quantity_temp = Double.doubleToRawLongBits(quantity);
        result = prime * result + (int)(quantity_temp ^ (quantity_temp >>> 32));
        result = prime * result + ((measure == null) ? 0 : measure.hashCode());
        long price_temp = Double.doubleToRawLongBits(price);
        result = prime * result + (int)(price_temp ^ (price_temp >>> 32));
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
        return result;
    }
    
    public boolean OrderItemBean.equals(Object obj) {
        OrderItemBean other = BeanComparison.basic(OrderItemBean.class, this, obj);
        boolean same = other != null;
        if( same ) {
        	same = BeanComparison.equals(same, getId(), other.getId());
        	same = BeanComparison.equals(same, getParentId(), other.getParentId());
        	same = BeanComparison.equals(same, ref, other.ref);
        	same = BeanComparison.equals(same, name, other.name);
        	same = BeanComparison.equals(same, description, other.description);
        	same = BeanComparison.equals(same, stockItem, other.stockItem);
        	same = BeanComparison.equals(same, quantity, other.quantity);
        	same = BeanComparison.equals(same, measure, other.measure);
        	same = BeanComparison.equals(same, price, other.price);
        	same = BeanComparison.equals(same, currency, other.currency);
        }
        return same;
    }
    
    public String OrderItemBean.getRef() {
        return ref;
    }
    
    public void OrderItemBean.setRef(String val) {
        setFields |= 1;
        ref = val;
    }
    
    public boolean OrderItemBean.isRefSet() {
        return (setFields & 1) > 0;
    }
    
    public boolean OrderItemBean.isRefChanged() {
        return (changedFields & 1) > 0;
    }
    
    public String OrderItemBean.getName() {
        return name;
    }
    
    public void OrderItemBean.setName(String val) {
        setFields |= 2;
        name = val;
    }
    
    public boolean OrderItemBean.isNameSet() {
        return (setFields & 2) > 0;
    }
    
    public boolean OrderItemBean.isNameChanged() {
        return (changedFields & 2) > 0;
    }
    
    public String OrderItemBean.getDescription() {
        return description;
    }
    
    public void OrderItemBean.setDescription(String val) {
        setFields |= 4;
        description = val;
    }
    
    public boolean OrderItemBean.isDescriptionSet() {
        return (setFields & 4) > 0;
    }
    
    public boolean OrderItemBean.isDescriptionChanged() {
        return (changedFields & 4) > 0;
    }
    
    public String OrderItemBean.getStockItem() {
        return stockItem;
    }
    
    public void OrderItemBean.setStockItem(String val) {
        setFields |= 8;
        stockItem = val;
    }
    
    public boolean OrderItemBean.isStockItemSet() {
        return (setFields & 8) > 0;
    }
    
    public boolean OrderItemBean.isStockItemChanged() {
        return (changedFields & 8) > 0;
    }
    
    public double OrderItemBean.getQuantity() {
        return quantity;
    }
    
    public void OrderItemBean.setQuantity(double val) {
        setFields |= 16;
        quantity = val;
    }
    
    public boolean OrderItemBean.isQuantitySet() {
        return (setFields & 16) > 0;
    }
    
    public boolean OrderItemBean.isQuantityChanged() {
        return (changedFields & 16) > 0;
    }
    
    public String OrderItemBean.getMeasure() {
        return measure;
    }
    
    public void OrderItemBean.setMeasure(String val) {
        setFields |= 32;
        measure = val;
    }
    
    public boolean OrderItemBean.isMeasureSet() {
        return (setFields & 32) > 0;
    }
    
    public boolean OrderItemBean.isMeasureChanged() {
        return (changedFields & 32) > 0;
    }
    
    public double OrderItemBean.getPrice() {
        return price;
    }
    
    public void OrderItemBean.setPrice(double val) {
        setFields |= 64;
        price = val;
    }
    
    public boolean OrderItemBean.isPriceSet() {
        return (setFields & 64) > 0;
    }
    
    public boolean OrderItemBean.isPriceChanged() {
        return (changedFields & 64) > 0;
    }
    
    public String OrderItemBean.getCurrency() {
        return currency;
    }
    
    public void OrderItemBean.setCurrency(String val) {
        setFields |= 128;
        currency = val;
    }
    
    public boolean OrderItemBean.isCurrencySet() {
        return (setFields & 128) > 0;
    }
    
    public boolean OrderItemBean.isCurrencyChanged() {
        return (changedFields & 128) > 0;
    }
    
    public void OrderItemBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = StateBeanUtils.updateTempReferences(parentId, refs);
        stockItem = StateBeanUtils.updateTempReferences(stockItem, refs);
    }
    
    public void OrderItemBean.acceptReader(ObjexStateReader reader) {
        ref = reader.read("ref", ref, java.lang.String.class, ObjexFieldType.STRING, true);
        name = reader.read("name", name, java.lang.String.class, ObjexFieldType.STRING, true);
        description = reader.read("description", description, java.lang.String.class, ObjexFieldType.STRING, true);
        stockItem = reader.readReference("stockItem", stockItem, ObjexFieldType.REFERENCE, true);
        quantity = reader.read("quantity", quantity, double.class, ObjexFieldType.NUMBER, true);
        measure = reader.read("measure", measure, java.lang.String.class, ObjexFieldType.STRING, true);
        price = reader.read("price", price, double.class, ObjexFieldType.NUMBER, true);
        currency = reader.read("currency", currency, java.lang.String.class, ObjexFieldType.STRING, true);
    }
    
    public void OrderItemBean.acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
        writer.write("ref", ref, ObjexFieldType.STRING, true);
        writer.write("name", name, ObjexFieldType.STRING, true);
        writer.write("description", description, ObjexFieldType.STRING, true);
        writer.writeReference("stockItem", stockItem, ObjexFieldType.REFERENCE, true);
        writer.write("quantity", quantity, ObjexFieldType.NUMBER, true);
        writer.write("measure", measure, ObjexFieldType.STRING, true);
        writer.write("price", price, ObjexFieldType.NUMBER, true);
        writer.write("currency", currency, ObjexFieldType.STRING, true);
    }
    
}
