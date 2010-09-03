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

package org.talframework.objexj.events;

import org.talframework.objexj.Container;

/**
 * This interface represents something that can handle
 * an event. Handlers are typically registered against
 * a container by name and then selected based upon 
 * the name in the incoming event.
 * 
 * @author Tom Spencer
 */
public interface EventHandler {

    public void execute(Container container, Event event);
}
