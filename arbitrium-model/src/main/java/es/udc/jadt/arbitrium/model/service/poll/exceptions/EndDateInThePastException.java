package es.udc.jadt.arbitrium.model.service.poll.exceptions;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class EndDateInThePastException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3184557154116254671L;
	private static final String DEFAULT_EXCEPTION_MESSAGE = "THe calendar introduced has a time before current time : Calendar -> ";

	public EndDateInThePastException(Calendar calendar) {
		super(new StringBuilder().append(DEFAULT_EXCEPTION_MESSAGE).append(calendar.toString()).toString());

	}

	public EndDateInThePastException(Date endDate) {
		super(new StringBuilder().append(DEFAULT_EXCEPTION_MESSAGE).append(endDate.toString()).toString());
	}

	public EndDateInThePastException(Instant endDate) {
		super(new StringBuilder().append(DEFAULT_EXCEPTION_MESSAGE).append(endDate.toString()).toString());
	}

}
