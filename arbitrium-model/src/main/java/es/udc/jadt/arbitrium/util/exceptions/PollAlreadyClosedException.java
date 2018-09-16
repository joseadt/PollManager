package es.udc.jadt.arbitrium.util.exceptions;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;

public class PollAlreadyClosedException extends Exception {

	private static final String DEFAULT_MESSAGE = "Poll %s is already closed in this date : %s";
	private final Poll poll;

	public PollAlreadyClosedException(Poll poll) {
		super(String.format(DEFAULT_MESSAGE, poll.getName(), poll.getEndDate().toString()));
		this.poll = poll;
	}

	public Poll getPoll() {
		return poll;
	}
}
