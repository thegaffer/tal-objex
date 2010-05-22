package org.tpspencer.tal.gaetest.config;

import java.util.HashMap;
import java.util.Map;

import org.tpspencer.tal.gaetest.model.BookingBean;
import org.tpspencer.tal.objexj.container.SimpleContainerStrategy;
import org.tpspencer.tal.objexj.gae.GAEMiddlewareFactory;
import org.tpspencer.tal.objexj.locator.ContainerFactory;
import org.tpspencer.tal.objexj.locator.SimpleContainerFactory;
import org.tpspencer.tal.objexj.locator.SimpleContainerLocator;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjectStrategy;

/**
 * In lieu of a full Spring config, this is the configuration
 * for the test app. 
 * 
 * @author Tom Spencer
 */
public class GAETestAppConfig {
	private static final GAETestAppConfig INSTANCE = new GAETestAppConfig();
	
	private final SimpleContainerLocator containerLocator;
	
	private GAETestAppConfig() {
		GAEMiddlewareFactory factory = new GAEMiddlewareFactory();

		// Booking container
		SimpleContainerStrategy bookingStrategy = new SimpleContainerStrategy("Booking",
		        "Booking",
				new ObjectStrategy[]{
					new SimpleObjectStrategy(BookingBean.class)
				});
		SimpleContainerFactory bookingFactory = new SimpleContainerFactory(bookingStrategy, factory);
		
		// Add all containers
		Map<String, ContainerFactory> factories = new HashMap<String, ContainerFactory>();
		factories.put("Booking", bookingFactory);
		
		containerLocator = new SimpleContainerLocator(factories);
	}
	
	public static GAETestAppConfig getInstance() {
		return INSTANCE;
	}
	
	public SimpleContainerLocator getContainerLocator() {
		return containerLocator;
	}
}
