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

package org.talframework.objexj.runtime.gae.persistence;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

/**
 * The single instance of the PersistenceManagerFactory
 * 
 * TODO: Create an interface, using singleton injection technique and add other platform elements like getting a new ID
 * 
 * @author Tom Spencer
 */
public final class PersistenceManagerFactorySingleton {
    private static final PersistenceManagerFactory INSTANCE = JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private PersistenceManagerFactorySingleton() {
    }

    public static PersistenceManager getManager() {
        return INSTANCE.getPersistenceManager();
    }
}
