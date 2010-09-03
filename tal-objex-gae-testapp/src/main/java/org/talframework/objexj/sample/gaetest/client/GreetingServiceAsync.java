/*
 * Generated as part of Google Eclipse Plugin Sample.
 * This source is governed by any license Google places
 * on it. 
 */

package org.talframework.objexj.sample.gaetest.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
}
