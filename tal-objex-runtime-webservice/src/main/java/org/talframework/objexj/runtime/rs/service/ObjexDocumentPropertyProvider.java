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
import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.object.RecursiveObjectCompiler;
import org.talframework.objexj.object.writer.PropertyWriter;
import org.talframework.objexj.runtime.rs.DocumentResult;

/**
 * This class is a JSR 311 writer that takes an object and
 * writes it out as a series of property value/name pairs.
 * This is the default text response.
 * 
 * TODO: Make this x-www-form-urlencoded (need the & and relevant substitution)
 *
 * @author Tom Spencer
 */
@Provider
@Produces(MediaType.TEXT_PLAIN)
public class ObjexDocumentPropertyProvider implements MessageBodyWriter<DocumentResult> {
    
    /**
     * Can handle if text plain and type is {@link ObjexObj}
     */
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return DocumentResult.class.isAssignableFrom(type);
    }
    
    /**
     * We can't determine size so returning -1
     */
    public long getSize(DocumentResult t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }
    
    /**
     * Writes the object out
     */
    public void writeTo(DocumentResult t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        PropertyWriter writer = new PropertyWriter(new PrintWriter(entityStream));
        
        ObjexObj rootObj = t.getObject();
        RecursiveObjectCompiler compiler = new RecursiveObjectCompiler(rootObj.getContainer());
        compiler.setRecurseDepth(-1);
        Map<ObjexID, ObjexObj> objs = compiler.getObjects(rootObj);
        
        StringBuilder buf = new StringBuilder();
        for( ObjexID id : objs.keySet() ) {
            buf.setLength(0);
            
            ObjexObj obj = objs.get(id);
            String prefix = buf.append("object").append('[').append(id.toString()).append(']').toString();
            writer.writeObject(prefix, obj);
        }
    }
}
