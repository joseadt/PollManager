package es.udc.jadt.arbitrium.model.service.poll;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.poll.PollRepository;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOptionRepository;
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

	@Autowired
	private PollOptionRepository pollOptionRepository;

	public static final long MINIMUM_DURATION = 20L;

	@Transactional
	public Poll createPoll(Long userId, Poll poll)
			throws EndDateInThePastException, EndDateTooCloseException {

		UserProfile user = userRepository.findOne(userId);

		poll.setAuthor(user);
		
		poll.setCreationDate(Instant.now());
		
		if (poll.getCreationDate().isAfter(poll.getEndDate())) {
			
			throw new EndDateInThePastException(poll.getEndDate());
		}

		poll = pollRepository.save(poll);

		return poll;

	}

	@Transactional
	public Poll createPoll(String email, Poll poll, List<String> pollOptions)
			throws EndDateInThePastException {

		UserProfile user = userRepository.findOneByEmail(email);
		poll.setAuthor(user);
		
		Instant currentCalendar = Instant.now();
		if (currentCalendar.isAfter(poll.getEndDate())) {
			throw new EndDateInThePastException(poll.getEndDate());
		}
		
		List<PollOption> options = new ArrayList<PollOption>();

		for (String option : pollOptions) {
			if (option != null) {
				options.add(new PollOption(poll, option));
			}
		}

		poll.setOptions(options);
		poll.setCreationDate(currentCalendar);
		poll = pollRepository.save(poll);

		return poll;
	}

	@Transactional
	public void closePoll(Long userId, Long pollId) throws UserWithoutPermisionException, PollAlreadyClosedException {
		UserProfile user = userRepository.findOne(userId);
		Poll poll = pollRepository.findOne(pollId);
		
		
		if(!user.equals(poll.getAuthor())) {
			
			throw new UserWithoutPermisionException(user, poll);
		}

		if (poll.getEndDate().isBefore(Instant.now())) {
			throw new PollAlreadyClosedException(poll);
		}
		poll.setEndDate(Instant.now());
		pollRepository.save(poll);

	}

	@Transactional
	public Poll findPollById(Long pollId) throws EntityNotFoundException {
		Poll poll = pollRepository.findOne(pollId);
		
		if(poll==null) {
			throw new EntityNotFoundException(Poll.class, pollId);
		}
		
		return poll; 
	}

	@Transactional
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
