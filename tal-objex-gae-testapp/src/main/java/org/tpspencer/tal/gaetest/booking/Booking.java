package org.tpspencer.tal.gaetest.booking;

import org.tpspencer.tal.gaetest.model.BookingBean;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexObj;

public class Booking {

	private ObjexObj obj;
	
	/**
	 * Constructs a new booking and adds it to the container.
	 * 
	 * @param container The container
	 */
	public Booking(EditableContainer container) {
		BookingBean bean = new BookingBean();
		container.newObject(bean);
	}
	
	/**
	 * Constructs a booking based on an existing booking.
	 * 
	 * @param obj
	 */
	public Booking(ObjexObj obj) {
		
	}
}
