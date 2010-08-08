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

package org.tpspencer.tal.objexj.gae;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tpspencer.tal.objexj.locator.SingletonContainerLocator;

/**
 * This admin servlet will pre-create any stores that don't
 * currently exist. 
 *
 * @author Tom Spencer
 */
public class GAEStoreCreationServlet extends HttpServlet {
    private final static long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        try {
            SingletonContainerLocator.getInstance().createStores();
            
            // Simple sucess response
            StringBuilder builder = new StringBuilder();
            builder.append("<html><head>");
            builder.append("<title>Objex Store Creation Sucessful</title>");
            builder.append("</head><body>");
            builder.append("<h2>Success</h2>");
            builder.append("<p>Stores now exist</p>");
            builder.append("</body></html>");
            resp.getWriter().append(builder.toString());
        }
        catch( Exception e ) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}