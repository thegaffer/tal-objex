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

package org.talframework.objexj.object.testmodel;

import java.util.List;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;
import org.talframework.objexj.object.testbeans.StockBean;
import org.talframework.objexj.object.utils.ReferenceListFieldUtils;

/**
 * A test objex object that allows us to test elements of 
 * Objex
 *
 * @author Tom Spencer
 */
public class StockImpl extends BaseTestObject {

    private StockBean bean;
    
    public StockImpl(StockBean bean) {
        this.bean = bean;
    }
    
    @Override
    protected ObjexObjStateBean getStateBean() {
        return bean;
    }
    
    public void acceptReader(ObjexStateReader reader) {
        List<String> categories = bean.getCategories();
        List<String> newCategories = reader.readReferenceList("categories", categories, ObjexFieldType.OWNED_REFERENCE, true);
        if( categories != newCategories ) setCategories(getContainer().getObjectList(newCategories));
    }
    
    public List<ObjexObj> getCategories() {
        return ReferenceListFieldUtils.getList(this, ObjexObj.class, bean.getCategories());
    }
    
    public void setCategories(List<ObjexObj> categories) {
        bean.setCategories(ReferenceListFieldUtils.setList(this, bean, bean.getCategories(), categories, true));
    }
}
