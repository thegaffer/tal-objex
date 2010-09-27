/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.objexj.sample.model.stock.impl;

import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.annotations.ObjexObj;
import org.talframework.objexj.sample.api.stock.Product;
import org.talframework.objexj.sample.beans.stock.ProductBean;

@ObjexObj(ProductBean.class)
public class ProductImpl implements Product {

    private final ProductBean bean;
    
    public ProductImpl(ProductBean bean) {
        this.bean = bean;
    }
    
    @Override
    protected ObjexObjStateBean getStateBean() {
        return bean;
    }

    
}
