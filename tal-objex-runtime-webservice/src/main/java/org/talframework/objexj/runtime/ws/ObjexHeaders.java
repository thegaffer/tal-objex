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

package org.talframework.objexj.runtime.ws;

/**
 * This enum contains the various headers supported by the
 * Objex WebService.
 *
 * @author Tom Spencer
 */
public enum ObjexHeaders {
    
    /** Indicates we should fetch only the one request object (or root) */
    FETCH_OBJECT("ObjexFetchObject"),
    /** Indicates we should fetch the request object (or root) and direct descendants */
    FETCH_DESCENDANTS("ObjexFetchDescendant"),
    /** Indicates we should fetch all from the given object */
    FETCH_ALL("ObjexFetchAll"),
    
    /** Indicates the container should be suspended not saved */
    SUSPEND("ObjexSuspend"),
    
    /** The checksum header, the value of which is used to validate client */
    CHECK("ObjexChecksum");

    private String header;
    
    ObjexHeaders(String header) {
        this.header = header;
    }
    
    /**
     * Overridden to use the header test
     */
    @Override
    public String toString() {
        return this.header;
    }
    
    /**
     * Call to get the enum matching the given string
     * 
     * @param header The header to get enum for
     * @return The enum values
     */
    public static ObjexHeaders fromString(String header) {
        ObjexHeaders ret = null;
        if( header != null ) { 
            for( ObjexHeaders b : ObjexHeaders.values() ) { 
                if( header.equalsIgnoreCase(b.toString())) { 
                    ret = b;
                    break;
                } 
            } 
        }
        
        return ret;
    }
}
