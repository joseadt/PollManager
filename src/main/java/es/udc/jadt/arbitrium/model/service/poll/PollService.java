package es.udc.jadt.arbitrium.model.service.poll;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.poll.PollRepository;
import es.udc.jadt.arbitrium.model.entities.poll.PollType;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfileRepository;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.EndDateInThePastException;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.EndDateTooCloseException;
import es.udc.jadt.arbitrium.util.exceptions.PollAlreadyClosedException;
import es.udc.jadt.arbitrium.util.exceptions.UserWithoutPermisionException;

@Service
public class PollService {

	@Autowired
	private UserProfileRepository userRepository;

	@Autowired
	private PollRepository pollRepository;

	public static final long MINIMUM_DURATION = 20L;
	
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


		poll.setCreationDate(new Timestamp(currentCalendar.getTimeInMillis()));
		poll.setEndDate(new Timestamp(endDate.getTimeInMillis()));
		poll.setOptions(pollOption);
		
		return pollRepository.save(poll);

	}

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
