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
package org.talframework.objexj.object.fields;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.ValidationRequest;

/**
 * This class represents a reference property to an object. This
 * class holds the ID of the object until such time as a method is
 * called that requires that object to be loaded, at which point
 * it is loaded.
 * 
 * <p>Authors note: This class somewhat goes against my sensibilities.
 * Basically we are "wasting" memory by holding additional references
 * to the container and realobject. However, this is to allow us to
 * create business objects that are infrastructure independent (a 
 * good thing). So given that a) these references are only held
 * transitively (not serialised), and b) that by using the Roo ITD we 
 * can minimize further this kind of usage I have come to accept 
 * this</p>
 * 
 * @author Tom Spencer
 */
public class ProxyReference implements InvocationHandler, ObjexObj {

    private ObjexID id;
    private Container container;
    private ObjexObj realObject;
    
    public ProxyReference(ObjexID id, Container container) {
        this.id = id;
        this.container = container;
    }
    
    /**
     * Handles the getId method itself, but for those it can't ensures the object
     * is loaded and returns it.
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = null;
        
        if( method.getDeclaringClass().equals(ObjexObj.class) ) {
            ret = method.invoke(this, args);
        }
        else {
            getRealObject();
            ret = method.invoke(realObject, args);
        }
        
        return ret;
    }
    
    ///////////////////////////////////////////
    // Internal
    
    private ObjexObj getRealObject() {
        if( realObject == null ) {
            // TODO: Should this be synchronised!?!
            if( realObject == null ) {
                realObject = container.getObject(id);
                // TODO: Should we throw an exception if not existing??
            }
        }
        
        return realObject;
    }
    
    ///////////////////////////////////////////
    // ObjexObj Methods
    // Handled if we can, otherwise delegated to the real object
    
    @Override
    public ObjexID getId() {
        return id;
    }
    
    @Override
    public String getType() {
        return id.getType();
    }
    
    @Override
    public ObjexID getParentId() {
        return getRealObject().getParentId();
    }
    
    @Override
    public ObjexObj getParent() {
        return getRealObject().getParent();
    }
    
    @Override
    public ObjexObj getRoot() {
        return container.getRootObject();
    }
    
    @Override
    public Container getContainer() {
        return container;
    }
    
    @Override
    public <T> T getBehaviour(Class<T> behaviour) {
        return getRealObject().getBehaviour(behaviour);
    }
    
    @Override
    public Object getProperty(String name) {
        return getRealObject().getProperty(name);
    }
    
    @Override
    public <T> T getProperty(String name, Class<T> expected) {
        return getRealObject().getProperty(name, expected);
    }
    
    @Override
    public String getPropertyAsString(String name) {
        return getRealObject().getPropertyAsString(name);
    }
    
    @Override
    public void setProperty(String name, Object newValue) {
        getRealObject().setProperty(name, newValue);
    }
    
    @Override
    public void validate(ValidationRequest request) {
        getRealObject().validate(request);
    }
    
    @Override
    public void acceptReader(ObjexStateReader reader) {
        getRealObject().acceptReader(reader);
    }
    
    @Override
    public void acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
        getRealObject().acceptWriter(writer, includeNonPersistent);
    }
}
