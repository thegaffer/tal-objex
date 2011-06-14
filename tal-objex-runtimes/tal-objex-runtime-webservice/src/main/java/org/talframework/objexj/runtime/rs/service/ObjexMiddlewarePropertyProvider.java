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
package org.talframework.objexj.runtime.rs.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.object.writer.PropertyWriter;
import org.talframework.objexj.runtime.rs.MiddlewareResult;

/**
 * This class is a JSR 311 writer that takes an state beans
 * and writes them out as a series of property value/name 
 * pairs. This is the default text response.
 * 
 * TODO: Make this x-www-form-urlencoded (need the & and relevant substitution)
 *
 * @author Tom Spencer
 */
@Provider
@Produces(MediaType.TEXT_PLAIN)
public class ObjexMiddlewarePropertyProvider implements MessageBodyWriter<MiddlewareResult> {
    
    /**
     * Can handle if text plain and type is {@link ObjexObj}
     */
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type.equals(MiddlewareResult.class);
    }
    
    /**
     * We can't determine size so returning -1
     */
    public long getSize(MiddlewareResult t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }
    
    /**
     * Writes the object out
     */
    public void writeTo(MiddlewareResult t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        PropertyWriter writer = new PropertyWriter(new PrintWriter(entityStream));
        
        List<ObjexObjStateBean> objects = t.getObjects();
        
        StringBuilder buf = new StringBuilder();
        for( ObjexObjStateBean obj : objects ) {
            String id = obj.getId().toString();
            buf.setLength(0);
            String prefix = buf.append("object").append('[').append(id.toString()).append(']').toString();
            writer.writeState(prefix, id, obj);
        }
    }
}
