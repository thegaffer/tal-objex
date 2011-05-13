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

package org.talframework.objexj.object.writer;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObj.ObjexFieldType;
import org.talframework.util.beans.BeanDefinition;
import org.talframework.util.beans.definition.BeanDefinitionsSingleton;

/**
 * This class reads or loads the state of an {@link ObjexObj} by reading 
 * from equivilent properties in a simple bean. The properties must be 
 * named identically in the bean as they are in the {@link ObjexObj}.
 * This class only reads 1 object at a time and expects the reference
 * properties to be held as ObjexID or String (or lists, sets and maps
 * of strings) in the bean class.
 * 
 * <p>If merging in order to determine if we get the value from the
 * source bean, this class tests if there is a isSet'name' method, where
 * name is the name of the property. If this does exist then we do 
 * use the value from the source only if it returns true. If there is
 * no isSet'name' method we always take the value from the source.</p>
 * 
 * @author Tom Spencer
 */
public class BeanObjectReader extends BaseObjectReader {
    
    /** The bean we are reading from */
    private Object bean;
    /** The definition of the bean we are reading */
    private BeanDefinition beanDefinition;
    
    public BeanObjectReader() {
        super();
    }
    
    public BeanObjectReader(ObjectReaderBehaviour... behaviours) {
        super(behaviours);
    }
    
    /**
     * Call to set the state of the target object given the simple bean holding
     * the values.
     * 
     * @param bean
     * @param target
     */
    public void readObject(Object bean, ObjexObj target) {
        this.bean = bean;
        this.beanDefinition = BeanDefinitionsSingleton.getInstance().getDefinition(bean.getClass());
        super.read(target);
        
    }

    /**
     * Helper to determine if property should be read at all. 
     * 
     * <p>If merging, this class will set the property if either
     * if there is an isSetX method for any property that returns
     * true. If there is no isSetX then we set the property
     * anyway.</p>
     * 
     * @param name The name of the property
     * @return True if property should be read (set on the target) false otherwise
     */
    protected boolean shouldSet(String name) {
        boolean doSet = true;
        if( isMerge() ) {
            String isSet = name + "Set";
            if( beanDefinition.hasProperty(isSet) && beanDefinition.canRead(isSet) ) {
                doSet = beanDefinition.read(bean, isSet, Boolean.class);
            }
        }
        
        return doSet;
    }
    
    @Override
    protected boolean propertyExists(String name, Class<?> expected) {
        if( !beanDefinition.hasProperty(name) ) return false;
        if( !beanDefinition.canRead(name) ) return false;
        return true;
    }
    
    @Override
    protected Object getProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent) {
        return beanDefinition.read(bean, name);
    }
    
    @Override
    protected Object getSetProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent) {
        return beanDefinition.read(bean, name);
    }
    
    @Override
    protected Object getListProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent) {
        return beanDefinition.read(bean, name);
    }
    
    @Override
    protected Object getMapProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent) {
        return beanDefinition.read(bean, name);
    }
}
