package es.udc.jadt.reupoll.service.pollservice.exceptions;

import java.util.Calendar;

public class EndDateInThePastException extends Exception {


	private static final String DEFAULT_EXCEPTION_MESSAGE = "THe calendar introduced has a time before current time : Calendar -> ";

	public EndDateInThePastException(Calendar calendar) {
		super(new StringBuilder().append(DEFAULT_EXCEPTION_MESSAGE).append(calendar.toString()).toString());

	}

}
