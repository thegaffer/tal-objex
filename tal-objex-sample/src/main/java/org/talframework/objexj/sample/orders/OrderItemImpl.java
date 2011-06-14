/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.sample.orders;

/**
 * This class is our OrderItem business object implementation in the sample.
 *
 * @author Tom Spencer
 */
public class OrderItemImpl implements OrderItem {

    private String ref;
    
    private String name;
    private String description;
    private double unit;
    private String measure;
    private double price;
    private String currency;
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Orderitem#getRef()
     */
    @Override
    public String getRef() {
        return ref;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Orderitem#setRef(java.lang.String)
     */
    @Override
    public void setRef(String ref) {
        this.ref = ref;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Orderitem#getName()
     */
    @Override
    public String getName() {
        return name;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Orderitem#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Orderitem#getDescription()
     */
    @Override
    public String getDescription() {
        return description;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Orderitem#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Orderitem#getUnit()
     */
    @Override
    public double getUnit() {
        return unit;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Orderitem#setUnit(double)
     */
    @Override
    public void setUnit(double unit) {
        this.unit = unit;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Orderitem#getMeasure()
     */
    @Override
    public String getMeasure() {
        return measure;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Orderitem#setMeasure(java.lang.String)
     */
    @Override
    public void setMeasure(String measure) {
        this.measure = measure;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Orderitem#getPrice()
     */
    @Override
    public double getPrice() {
        return price;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Orderitem#setPrice(double)
     */
    @Override
    public void setPrice(double price) {
        this.price = price;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Orderitem#getCurrency()
     */
    @Override
    public String getCurrency() {
        return currency;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Orderitem#setCurrency(java.lang.String)
     */
    @Override
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
