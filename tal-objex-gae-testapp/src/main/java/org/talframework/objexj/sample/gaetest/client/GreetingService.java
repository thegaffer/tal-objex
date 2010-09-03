/*
 * Generated as part of Google Eclipse Plugin Sample.
 * This source is governed by any license Google places
 * on it. 
 */

package org.talframework.objexj.sample.gaetest.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
}
