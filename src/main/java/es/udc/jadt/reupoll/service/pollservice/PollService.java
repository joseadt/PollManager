package es.udc.jadt.reupoll.service.pollservice;

import java.util.Calendar;
import java.util.List;

import es.udc.jadt.reupoll.model.poll.Poll;
import es.udc.jadt.reupoll.model.poll.PollType;
import es.udc.jadt.reupoll.service.pollservice.exceptions.EndDateInThePastException;
import es.udc.jadt.reupoll.service.pollservice.exceptions.EndDateTooCloseException;
import es.udc.jadt.reupoll.util.exceptions.PollAlreadyClosedException;
import es.udc.jadt.reupoll.util.exceptions.UserWithoutPermisionException;


public interface PollService {

	Poll createPoll(Long userId, List<String> pollOptions, PollType type, Calendar endDate)
			throws EndDateInThePastException, EndDateTooCloseException;

	void closePoll(Long userId, Long pollId) throws UserWithoutPermisionException, PollAlreadyClosedException;
}
