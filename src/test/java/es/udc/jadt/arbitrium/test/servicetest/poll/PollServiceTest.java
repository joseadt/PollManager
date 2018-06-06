package es.udc.jadt.arbitrium.test.servicetest.poll;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.poll.PollRepository;
import es.udc.jadt.arbitrium.model.entities.poll.PollType;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfileRepository;
import es.udc.jadt.arbitrium.model.service.poll.PollService;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.EndDateInThePastException;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.EndDateTooCloseException;
import es.udc.jadt.arbitrium.util.exceptions.PollAlreadyClosedException;
import es.udc.jadt.arbitrium.util.exceptions.UserWithoutPermisionException;

@RunWith(MockitoJUnitRunner.class)
public class PollServiceTest {

	@Mock
	private PollRepository pollRepo;
	
	@Mock
	private UserProfileRepository userRepo;

	@InjectMocks
	private PollService service;

	private final static String DEFAULT_EMAIL = "user@email.com";

	private final static String USER_NAME = "John Doe";

	private final static String PASSWORD = "12345";

	@Test
	public void CreatePollTest() throws EndDateInThePastException, EndDateTooCloseException {
		UserProfile userProfile = new UserProfile(DEFAULT_EMAIL, USER_NAME, PASSWORD);
		final Long defaultId= new Long(1);
		userProfile.setId(defaultId);
		List<String> options = Arrays.asList("OPTION1","OPTION2","OPTION3");

		when(userRepo.findOne(userProfile.getId())).thenReturn(userProfile);
		when(pollRepo.save(any(Poll.class))).thenAnswer(new Answer<Poll>() {
			@Override
			public Poll answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				Poll poll = (Poll) args[0];
				poll.setId(defaultId);
				return poll;
			}
		});
		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.MINUTE, endDate.get(Calendar.MINUTE) + (int) PollService.MINIMUM_DURATION + 3);
		Poll returnedPoll = service.createPoll(userProfile.getId(), options, PollType.PROPOSAL, endDate);
		assertEquals(defaultId, returnedPoll.getId());
	}

	@Test(expected = EndDateInThePastException.class)
	public void CreatePollWithPassedEndDateTest() throws EndDateInThePastException, EndDateTooCloseException {
		UserProfile userProfile = new UserProfile(DEFAULT_EMAIL, USER_NAME, PASSWORD);
		final Long defaultId = new Long(1);
		userProfile.setId(defaultId);
		List<String> options = Arrays.asList("OPTION1", "OPTION2", "OPTION3");

		when(userRepo.findOne(userProfile.getId())).thenReturn(userProfile);

		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.YEAR, endDate.get(Calendar.YEAR) - 1);
		service.createPoll(userProfile.getId(), options, PollType.PROPOSAL, endDate);
	}


	@Test
	public void ClosePollTest() throws UserWithoutPermisionException, PollAlreadyClosedException {
		UserProfile userProfile = new UserProfile(DEFAULT_EMAIL, USER_NAME, PASSWORD);
		final Long defaultId= new Long(1);
		userProfile.setId(defaultId);
		List<String> options = Arrays.asList("OPTION1","OPTION2","OPTION3");
		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.DAY_OF_MONTH, endDate.get(Calendar.DAY_OF_MONTH) +1);
		;

		List<PollOption> pollOption = new ArrayList<PollOption>();

		Poll poll = new Poll(userProfile, null, PollType.PROPOSAL, new Timestamp(endDate.getTimeInMillis()));
		for (String optionName : options) {
			PollOption option = new PollOption(poll, optionName);
			pollOption.add(option);

		}
		poll.setOptions(pollOption);
		poll.setId(defaultId);

		poll.setAuthor(userProfile);

		when(userRepo.findOne(defaultId)).thenReturn(userProfile);
		when(pollRepo.findOne(defaultId)).thenReturn(poll);
		
		when(pollRepo.save(poll)).thenAnswer(new Answer<Poll>() {

			@Override
			public Poll answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				Poll poll = (Poll) args[0];
				assertEquals(defaultId, poll.getId());
				assertTrue(poll.getEndDate().getTime() - Calendar.getInstance().getTimeInMillis() <= 0);
				return poll;
			}
		});
		service.closePoll(defaultId, defaultId);
		Mockito.verify(pollRepo).save(poll);

	}

	@Test(expected = UserWithoutPermisionException.class)
	public void ShoulThrowNotPermissionExceptionOnCloseTest()
			throws UserWithoutPermisionException, PollAlreadyClosedException {
		UserProfile userProfile = new UserProfile(DEFAULT_EMAIL, USER_NAME, PASSWORD);
		final Long defaultId = new Long(1);
		userProfile.setId(defaultId);
		List<String> options = Arrays.asList("OPTION1", "OPTION2", "OPTION3");
		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.DAY_OF_MONTH, endDate.get(Calendar.DAY_OF_MONTH) + 1);
		;

		UserProfile userProfile2 = new UserProfile(DEFAULT_EMAIL, USER_NAME.concat("1234"), PASSWORD);

		List<PollOption> pollOption = new ArrayList<PollOption>();
		userProfile.setId(new Long(2));
		Poll poll = new Poll(userProfile, null, PollType.PROPOSAL, new Timestamp(endDate.getTimeInMillis()));
		for (String optionName : options) {
			PollOption option = new PollOption(poll, optionName);
			pollOption.add(option);

		}
		poll.setOptions(pollOption);
		poll.setId(defaultId);
		poll.setAuthor(userProfile2);

		when(userRepo.findOne(defaultId)).thenReturn(userProfile);
		when(pollRepo.findOne(defaultId)).thenReturn(poll);

		service.closePoll(defaultId, defaultId);
	}
}
