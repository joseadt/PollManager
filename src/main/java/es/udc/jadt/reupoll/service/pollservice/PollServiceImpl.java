package es.udc.jadt.reupoll.service.pollservice;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.jadt.reupoll.model.poll.Poll;
import es.udc.jadt.reupoll.model.poll.PollRepository;
import es.udc.jadt.reupoll.model.poll.PollType;
import es.udc.jadt.reupoll.model.polloption.PollOption;
import es.udc.jadt.reupoll.model.userprofile.UserProfile;
import es.udc.jadt.reupoll.model.userprofile.UserProfileRepository;
import es.udc.jadt.reupoll.service.pollservice.exceptions.EndDateInThePastException;
import es.udc.jadt.reupoll.service.pollservice.exceptions.EndDateTooCloseException;
import es.udc.jadt.reupoll.util.exceptions.PollAlreadyClosedException;
import es.udc.jadt.reupoll.util.exceptions.UserWithoutPermisionException;

@Service
public class PollServiceImpl implements PollService {

	@Autowired
	private UserProfileRepository userRepository;

	@Autowired
	private PollRepository pollRepository;

	public static final long MINIMUM_DURATION = 20L;
	
	@Override
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
		long diferencesInMinutes = (endDate.getTimeInMillis() - currentCalendar.getTimeInMillis()) / (60000);

		if ((diferencesInMinutes - MINIMUM_DURATION) < 0) {
			throw new EndDateTooCloseException(endDate);
		}

		poll.setCreationDate(new Timestamp(currentCalendar.getTimeInMillis()));
		poll.setEndDate(new Timestamp(endDate.getTimeInMillis()));
		poll.setOptions(pollOption);
		
		return pollRepository.save(poll);

	}

	@Override
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

}
