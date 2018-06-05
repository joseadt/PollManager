package es.udc.jadt.arbitrium.service.pollservice.exceptions;

import java.util.Calendar;

public class EndDateTooCloseException extends Exception {

	private static final String DEFAULT_MESSAGE = "End date is too close to current time : ";

	public EndDateTooCloseException(Calendar calendar) {
		super(new StringBuilder().append(DEFAULT_MESSAGE).append(calendar.toString()).toString());
	}

}
