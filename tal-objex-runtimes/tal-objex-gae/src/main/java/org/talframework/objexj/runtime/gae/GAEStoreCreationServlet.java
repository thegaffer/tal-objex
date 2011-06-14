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
package org.talframework.objexj.runtime.gae;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.talframework.objexj.locator.SingletonContainerLocator;

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
