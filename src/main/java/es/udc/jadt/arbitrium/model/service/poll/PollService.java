package es.udc.jadt.arbitrium.model.service.poll;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.poll.PollRepository;
import es.udc.jadt.arbitrium.model.entities.poll.PollType;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfileRepository;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.EndDateInThePastException;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.EndDateTooCloseException;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.UserIsNotTheAuthorException;
import es.udc.jadt.arbitrium.model.service.util.EntityNotFoundException;
import es.udc.jadt.arbitrium.util.exceptions.PollAlreadyClosedException;
import es.udc.jadt.arbitrium.util.exceptions.UserWithoutPermisionException;

@Service
public class PollService {

	@Autowired
	private UserProfileRepository userRepository;

	@Autowired
	private PollRepository pollRepository;

	public static final long MINIMUM_DURATION = 20L;

	@Transactional
	public Poll createPoll(Long userId, List<String> pollOptions, PollType type, Calendar endDate)
			throws EndDateInThePastException, EndDateTooCloseException {
		Poll poll = new Poll();
		UserProfile user = userRepository.findOne(userId);

		poll.setAuthor(user);
		
		List<PollOption> pollOption = new ArrayList<PollOption>();
		
		for (String optionName : pollOptions) {
			PollOption option = new PollOption(poll, optionName);
			pollOption.add(option);
			
		}
		Calendar currentCalendar = Calendar.getInstance();
		if (endDate.before(currentCalendar)) {
			
			throw new EndDateInThePastException(endDate);
		}


		poll.setCreationDate(new Timestamp(currentCalendar.getTimeInMillis()));
		poll.setEndDate(new Timestamp(endDate.getTimeInMillis()));
		poll.setOptions(pollOption);
		
		return pollRepository.save(poll);

	}

	@Transactional
	public Poll createPoll(String email, List<String> pollOptions, PollType type, Date endDate)
			throws EndDateInThePastException {
		Poll poll = new Poll();
		UserProfile user = userRepository.findOneByEmail(email);

		poll.setAuthor(user);

		List<PollOption> pollOption = new ArrayList<PollOption>();

		for (String optionName : pollOptions) {
			PollOption option = new PollOption(poll, optionName);
			pollOption.add(option);

		}
		Date currentCalendar = Date.from(Instant.now());
		if (endDate.before(currentCalendar)) {

			throw new EndDateInThePastException(endDate);
		}

		poll.setCreationDate(new Timestamp(currentCalendar.getTime()));
		poll.setEndDate(new Timestamp(endDate.getTime()));
		poll.setOptions(pollOption);

		return pollRepository.save(poll);

	}

	@Transactional
	public void closePoll(Long userId, Long pollId) throws UserWithoutPermisionException, PollAlreadyClosedException {
		UserProfile user = userRepository.findOne(userId);
		Poll poll = pollRepository.findOne(pollId);
		
		
		if(!user.equals(poll.getAuthor())) {
			
			throw new UserWithoutPermisionException(user, poll);
		}

		if (poll.getEndDate().before(new Timestamp(Calendar.getInstance().getTimeInMillis()))) {
			throw new PollAlreadyClosedException(poll);
		}
		poll.setEndDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		pollRepository.save(poll);

	}

	public Poll findPollById(Long pollId) {
		return pollRepository.findOne(pollId);
	}

	public void savePoll(Poll poll, Long userId) throws EntityNotFoundException, UserIsNotTheAuthorException {
		Poll returnedPoll = pollRepository.findOne(poll.getId());
		
		if (returnedPoll == null) {
			throw new EntityNotFoundException(Poll.class, poll.getId());
		}

		UserProfile user = userRepository.findOne(userId);

		
		if(user== null) {
			throw new EntityNotFoundException(UserProfile.class, userId);
		}
		
		if (!user.equals(returnedPoll.getAuthor())) {
			throw new UserIsNotTheAuthorException(userId, returnedPoll.getId());
		}

		pollRepository.save(poll);
	}
}
