package es.udc.jadt.arbitrium.model.service.poll;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.jadt.arbitrium.model.entities.comment.Comment;
import es.udc.jadt.arbitrium.model.entities.group.GroupRepository;
import es.udc.jadt.arbitrium.model.entities.group.UserGroup;
import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.poll.PollRepository;
import es.udc.jadt.arbitrium.model.entities.poll.specification.PollFilters;
import es.udc.jadt.arbitrium.model.entities.pollconfig.PollConfiguration;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOptionRepository;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfileRepository;
import es.udc.jadt.arbitrium.model.entities.vote.Vote;
import es.udc.jadt.arbitrium.model.entities.vote.VoteRepository;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.EndDateInThePastException;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.EndDateTooCloseException;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.UserIsNotTheAuthorException;
import es.udc.jadt.arbitrium.model.service.util.ServiceHelper;
import es.udc.jadt.arbitrium.model.service.util.exceptions.EntityNotFoundException;
import es.udc.jadt.arbitrium.util.exceptions.PollAlreadyClosedException;
import es.udc.jadt.arbitrium.util.exceptions.UserWithoutPermisionException;
import es.udc.jadt.arbitrium.util.generics.Pair;

/**
 * The Class PollService.
 *
 * @author JADT
 */
@Service
public class PollService {

	/** The user repository. */
	@Autowired
	private UserProfileRepository userRepository;

	/** The poll repository. */
	@Autowired
	private PollRepository pollRepository;

	/** The poll option repository. */
	@Autowired
	private PollOptionRepository pollOptionRepository;

	/** The vote repository. */
	@Autowired
	private VoteRepository voteRepository;

	/** The group repository. */
	@Autowired
	private GroupRepository groupRepository;

	/** The Constant MINIMUM_DURATION. */
	public static final long MINIMUM_DURATION = 20L;

	/**
	 * Creates the poll.
	 *
	 * @param userId
	 *            the user id
	 * @param poll
	 *            the poll
	 * @return the poll
	 * @throws EndDateInThePastException
	 *             the end date in the past exception
	 * @throws EndDateTooCloseException
	 *             the end date too close exception
	 */
	@Transactional
	public Poll createPoll(Long userId, Poll poll) throws EndDateInThePastException, EndDateTooCloseException {

		UserProfile user = this.userRepository.getOne(userId);

		poll.setAuthor(user);

		poll.setCreationDate(Instant.now());

		if (poll.getCreationDate().isAfter(poll.getEndDate())) {

			throw new EndDateInThePastException(poll.getEndDate());
		}

		poll = this.pollRepository.save(poll);

		return poll;

	}

	/**
	 * Creates the poll.
	 *
	 * @param email
	 *            the email
	 * @param poll
	 *            the poll
	 * @return the poll
	 * @throws EndDateInThePastException
	 *             the end date in the past exception
	 * @throws EntityNotFoundException
	 *             the entity not found exception
	 */
	@Transactional
	public Poll createPoll(String email, Poll poll) throws EndDateInThePastException, EntityNotFoundException {

		return createPoll(email, poll, null);
	}

	/**
	 * Creates the poll.
	 *
	 * @param email
	 *            the email
	 * @param poll
	 *            the poll
	 * @param groupId
	 *            the group id
	 * @return the poll
	 * @throws EndDateInThePastException
	 *             the end date in the past exception
	 * @throws EntityNotFoundException
	 *             the entity not found exception
	 */
	@Transactional
	public Poll createPoll(String email, Poll poll, Long groupId)
			throws EndDateInThePastException, EntityNotFoundException {
		UserProfile user = this.userRepository.findOneByEmail(email);
		poll.setAuthor(user);

		if (groupId != null) {
			UserGroup userGroup = ServiceHelper.findOneById(this.groupRepository, groupId, UserGroup.class);

			if (!(userGroup.getMembers().contains(user) || userGroup.getCreator().equals(user))) {
				throw new EntityNotFoundException("User is not member or creator of a group with id " + groupId,
						UserGroup.class, groupId);
			}
			poll.setUserGroup(userGroup);
		}

		Instant currentCalendar = Instant.now();
		if (currentCalendar.isAfter(poll.getEndDate())) {
			throw new EndDateInThePastException(poll.getEndDate());
		}

		poll.setCreationDate(currentCalendar);
		poll = this.pollRepository.save(poll);

		return poll;
	}

	/**
	 * Creates a poll and assigns to it a Configuration. It will check with the
	 * pollType and only apply the configuration that that type permits
	 *
	 * @param email
	 * @param poll
	 * @param configuration
	 *            {@link PollConfiguration} with the configuration of the poll that
	 *            you want to apply
	 * @return
	 * @throws EntityNotFoundException
	 * @throws EndDateInThePastException
	 */
	@Transactional
	public Poll createPoll(String email, Poll poll, PollConfiguration configuration, Long groupId)
			throws EndDateInThePastException, EntityNotFoundException {

		ServiceHelper.adaptPollConfigurationToParameters(configuration,
				poll.getPollType().getConfigurationParameters());

		poll.setConfiguration(configuration);
		return this.createPoll(email, poll, groupId);
	}

	/**
	 * Close poll.
	 *
	 * @param userId
	 *            the user id
	 * @param pollId
	 *            the poll id
	 * @throws UserWithoutPermisionException
	 *             the user without permision exception
	 * @throws PollAlreadyClosedException
	 *             the poll already closed exception
	 * @throws EntityNotFoundException
	 *             the entity not found exception
	 */
	@Transactional
	public void closePoll(Long userId, Long pollId)
			throws UserWithoutPermisionException, PollAlreadyClosedException, EntityNotFoundException {
		UserProfile user = this.userRepository.getOne(userId);
		Poll poll = ServiceHelper.findOneById(this.pollRepository, pollId, Poll.class);

		if (!user.equals(poll.getAuthor())) {

			throw new UserWithoutPermisionException(user, poll);
		}

		if (poll.getEndDate().isBefore(Instant.now())) {
			throw new PollAlreadyClosedException(poll);
		}
		poll.setEndDate(Instant.now());
		this.pollRepository.save(poll);

	}

	/**
	 * Find poll by id.
	 *
	 * @param pollId
	 *            the poll id
	 * @return the poll
	 * @throws EntityNotFoundException
	 *             the entity not found exception
	 */
	@Transactional
	public Poll findPollById(Long pollId) throws EntityNotFoundException {
		return ServiceHelper.findOneById(this.pollRepository, pollId, Poll.class);
	}

	/**
	 * Save poll.
	 *
	 * @param poll
	 *            the poll
	 * @param userId
	 *            the user id
	 * @throws EntityNotFoundException
	 *             the entity not found exception
	 * @throws UserIsNotTheAuthorException
	 *             the user is not the author exception
	 */
	@Transactional
	public void savePoll(Poll poll, String userId) throws EntityNotFoundException, UserIsNotTheAuthorException {
		Poll returnedPoll = ServiceHelper.findOneById(this.pollRepository, poll.getId(), Poll.class);

		UserProfile user = this.userRepository.findOneByEmail(userId);

		if (user == null) {
			throw new EntityNotFoundException(UserProfile.class, userId);
		}

		if (!user.equals(returnedPoll.getAuthor())) {
			throw new UserIsNotTheAuthorException(user.getId(), returnedPoll.getId());
		}

		this.pollRepository.save(poll);
	}

	/**
	 * Find by keywords.
	 *
	 * @param keywords
	 *            the keywords
	 * @param findOnDescriptionToo
	 *            the find on description too
	 * @return the list
	 */
	@Transactional
	public List<Poll> findByKeywords(String keywords, boolean findOnDescriptionToo) {
		List<String> keywordsList = Arrays.asList(keywords.split(" "));

		return this.pollRepository.findAll(PollFilters.pollKeywordsFilter(keywordsList, findOnDescriptionToo));
	}

	/**
	 * Gets the poll votes.
	 *
	 * @param id
	 *            the id
	 * @return the poll votes
	 * @throws EntityNotFoundException
	 *             the entity not found exception
	 */
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

	/**
	 * Gets the poll by ID with comments.
	 *
	 * @param id
	 *            the id
	 * @return the poll by ID with comments
	 * @throws EntityNotFoundException
	 *             the entity not found exception
	 */
	@Transactional
	public Pair<Poll, List<Comment>> getPollByIDWithComments(Long id) throws EntityNotFoundException {
		final Poll poll = ServiceHelper.findOneById(this.pollRepository, id, Poll.class);
		final List<Comment> comments = this.voteRepository.findPollComments(id);

		return new Pair<>(poll, comments);
	}

}
