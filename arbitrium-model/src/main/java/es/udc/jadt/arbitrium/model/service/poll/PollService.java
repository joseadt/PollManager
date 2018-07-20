package es.udc.jadt.arbitrium.model.service.poll;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.poll.PollRepository;
import es.udc.jadt.arbitrium.model.entities.poll.specification.PollFilters;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOptionRepository;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfileRepository;
import es.udc.jadt.arbitrium.model.entities.vote.Vote;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.EndDateInThePastException;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.EndDateTooCloseException;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.UserIsNotTheAuthorException;
import es.udc.jadt.arbitrium.model.service.util.EntityNotFoundException;
import es.udc.jadt.arbitrium.util.exceptions.PollAlreadyClosedException;
import es.udc.jadt.arbitrium.util.exceptions.UserWithoutPermisionException;
import es.udc.jadt.arbitrium.util.generics.Pair;

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

		UserProfile user = this.userRepository.getOne(userId);

		poll.setAuthor(user);



		poll.setCreationDate(Instant.now());

		if (poll.getCreationDate().isAfter(poll.getEndDate())) {

			throw new EndDateInThePastException(poll.getEndDate());
		}

		poll = this.pollRepository.save(poll);

		return poll;

	}

	@Transactional
	public Poll createPoll(String email, Poll poll)
			throws EndDateInThePastException {

		UserProfile user = this.userRepository.findOneByEmail(email);
		poll.setAuthor(user);

		Instant currentCalendar = Instant.now();
		if (currentCalendar.isAfter(poll.getEndDate())) {
			throw new EndDateInThePastException(poll.getEndDate());
		}

		poll.setCreationDate(currentCalendar);
		poll = this.pollRepository.save(poll);

		return poll;
	}

	@Transactional
	public void closePoll(Long userId, Long pollId) throws UserWithoutPermisionException, PollAlreadyClosedException {
		UserProfile user = this.userRepository.getOne(userId);
		Poll poll = this.pollRepository.getOne(pollId);


		if(!user.equals(poll.getAuthor())) {

			throw new UserWithoutPermisionException(user, poll);
		}

		if (poll.getEndDate().isBefore(Instant.now())) {
			throw new PollAlreadyClosedException(poll);
		}
		poll.setEndDate(Instant.now());
		this.pollRepository.save(poll);

	}

	@Transactional
	public Poll findPollById(Long pollId) throws EntityNotFoundException {
		Poll poll = this.pollRepository.getOne(pollId);

		if(poll==null) {
			throw new EntityNotFoundException(Poll.class, pollId);
		}

		return poll;
	}

	@Transactional
	public void savePoll(Poll poll, String userId) throws EntityNotFoundException, UserIsNotTheAuthorException {
		Poll returnedPoll = this.pollRepository.getOne(poll.getId());

		if (returnedPoll == null) {
			throw new EntityNotFoundException(Poll.class, poll.getId());
		}

		UserProfile user = this.userRepository.findOneByEmail(userId);


		if(user== null) {
			throw new EntityNotFoundException(UserProfile.class, userId);
		}

		if (!user.equals(returnedPoll.getAuthor())) {
			throw new UserIsNotTheAuthorException(user.getId(), returnedPoll.getId());
		}


		this.pollRepository.save(poll);
	}

	@Transactional
	public List<Poll> findByKeywords(String keywords, boolean findOnDescriptionToo) {
		List<String> keywordsList = Arrays.asList(keywords.split(" "));


		return this.pollRepository.findAll(PollFilters.pollKeywordsFilter(keywordsList, findOnDescriptionToo));
	}

	@Transactional
	public Pair<Poll, List<Vote>> getPollVotes(Long id) throws EntityNotFoundException {
		Poll poll = findPollById(id);
		List<Vote> votes = new ArrayList<Vote>();
		Pair<Poll, List<Vote>> pair = new Pair<Poll, List<Vote>>(poll, votes);
		for (PollOption option : poll.getOptions()) {
			votes.addAll(option.getVotes());
		}

		return pair;

	}


}
