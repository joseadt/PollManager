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
import es.udc.jadt.arbitrium.model.service.util.EntityNotFoundException;

@Service
public class VoteService {

	@Autowired
	private UserProfileRepository userRepository;

	@Autowired
	private PollRepository pollRepository;

	@Autowired
	private PollOptionRepository pollOptionRepository;

	@Autowired
	private VoteRepository voteRepository;

	@Transactional
	public Vote createVote(String email, Long pollId, List<Long> optionsIds) throws EntityNotFoundException {
		UserProfile user = this.userRepository.findOneByEmail(email);
		if(user==null) {
			throw new EntityNotFoundException(UserProfile.class, email);
		}
		Poll poll = this.pollRepository.findOne(pollId);

		if (poll == null) {
			throw new EntityNotFoundException(Poll.class, pollId);
		}

		List<PollOption> selectedOptions = new ArrayList<PollOption>();
		for (Long id : optionsIds) {
			PollOption option = this.pollOptionRepository.findOne(new PollOptionPk(id, poll));
			if (option == null) {
				throw new EntityNotFoundException(PollOption.class, new PollOptionPk(id, poll));
			}

			selectedOptions.add(option);
		}

		return this.voteRepository.save(new Vote(selectedOptions, "", user));
	}

	@Transactional
	public Vote findByIdAndEmail(Long id, String email) throws EntityNotFoundException {

		Vote vote = this.voteRepository.findOneByIdAndUserEmail(id, email);

		if(vote==null) {
			ArrayList<Object> findParameters = new ArrayList<Object>();
			findParameters.add(id);
			findParameters.add(email);
			throw new EntityNotFoundException(Vote.class, findParameters);
		}
		return vote;
	}

}
