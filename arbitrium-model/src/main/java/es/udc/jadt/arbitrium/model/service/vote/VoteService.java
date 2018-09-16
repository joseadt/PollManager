package es.udc.jadt.arbitrium.model.service.vote;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.poll.PollRepository;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOptionPk;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOptionRepository;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfileRepository;
import es.udc.jadt.arbitrium.model.entities.vote.Vote;
import es.udc.jadt.arbitrium.model.entities.vote.VoteRepository;
import es.udc.jadt.arbitrium.model.service.util.ServiceHelper;
import es.udc.jadt.arbitrium.model.service.util.exceptions.EntityNotFoundException;

// TODO: Auto-generated Javadoc
/**
 * The Class VoteService.
 *
 * @author JADT
 */
@Service
public class VoteService {

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

	/**
	 * Creates the vote.
	 *
	 * @param email
	 *            the email
	 * @param pollId
	 *            the poll id
	 * @param optionsIds
	 *            the options ids
	 * @param voteComment
	 *            the vote comment
	 * @return the vote
	 * @throws EntityNotFoundException
	 *             the entity not found exception
	 */
	@Transactional
	public Vote createVote(String email, Long pollId, List<Long> optionsIds, String voteComment)
			throws EntityNotFoundException {
		UserProfile user = this.userRepository.findOneByEmail(email);
		if (user == null) {
			throw new EntityNotFoundException(UserProfile.class, email);
		}

		Poll poll = ServiceHelper.findOneById(this.pollRepository, pollId, Poll.class);

		List<PollOption> selectedOptions = new ArrayList<>();
		for (Long id : optionsIds) {
			PollOption option = ServiceHelper.findOneById(this.pollOptionRepository, new PollOptionPk(id, poll),
					PollOption.class);

			selectedOptions.add(option);
		}

		return this.voteRepository.save(new Vote(selectedOptions, voteComment, user));
	}

	/**
	 * Find by id and email.
	 *
	 * @param id
	 *            the id
	 * @param email
	 *            the email
	 * @return the vote
	 * @throws EntityNotFoundException
	 *             the entity not found exception
	 */
	@Transactional
	public Vote findByIdAndEmail(Long id, String email) throws EntityNotFoundException {

		Vote vote = this.voteRepository.findOneByIdAndUserEmail(id, email);

		if (vote == null) {
			ArrayList<Object> findParameters = new ArrayList<Object>();
			findParameters.add(id);
			findParameters.add(email);
			throw new EntityNotFoundException(Vote.class, findParameters);
		}
		return vote;
	}

}
