/*
 * Copyright 2010 Thomas Spencer
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
