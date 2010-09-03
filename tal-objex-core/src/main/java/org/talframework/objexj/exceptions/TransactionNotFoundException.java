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

package org.talframework.objexj.exceptions;

/**
 * Indicates that an attempt to load a transaction failed. This
 * might be a programmatic error because the transaction did not
 * exist, or it might be because the transaction has expired as
 * transactions have a lifetime.
 * 
 * @author Tom Spencer
 */
public final class TransactionNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private final String id;
    
    public TransactionNotFoundException(String id) {
        super();
        this.id = id;
    }
    
    public TransactionNotFoundException(String id, Exception cause) {
        super(cause);
        this.id = id;
    }
    
    @Override
    public String getMessage() {
        return "Transaction [" + id + "] not found";
    }
}
