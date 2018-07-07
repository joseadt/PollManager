package es.udc.jadt.arbitrium.service.poll;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.data.jpa.domain.Specification;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.poll.PollRepository;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfileRepository;
import es.udc.jadt.arbitrium.model.service.poll.PollService;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.EndDateInThePastException;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.EndDateTooCloseException;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.UserIsNotTheAuthorException;
import es.udc.jadt.arbitrium.model.service.util.EntityNotFoundException;
import es.udc.jadt.arbitrium.model.util.SpecificationFilter;
import es.udc.jadt.arbitrium.util.exceptions.PollAlreadyClosedException;
import es.udc.jadt.arbitrium.util.exceptions.UserWithoutPermisionException;
import es.udc.jadt.arbitrium.util.polltype.PollType;

@RunWith(MockitoJUnitRunner.class)
public class PollServiceTest {

	@Mock
	private PollRepository pollRepo;

	@Mock
	private UserProfileRepository userRepo;

	@InjectMocks
	private PollService service;

	private static Long POLL_ID = Long.valueOf(456);

	private final static String DEFAULT_EMAIL = "user@email.com";

	private final static String USER_NAME = "John Doe";

	private final static String PASSWORD = "12345";

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void CreatePollTest() throws EndDateInThePastException, EndDateTooCloseException {
		UserProfile userProfile = new UserProfile(DEFAULT_EMAIL, USER_NAME, PASSWORD);
		final Long defaultId= new Long(1);
		userProfile.setId(defaultId);
		Poll poll = new Poll();

		poll.setEndDate(Instant.now().plus(Duration.ofDays(2)));


		when(this.userRepo.findOne(userProfile.getId())).thenReturn(userProfile);
		when(this.pollRepo.save(any(Poll.class))).thenAnswer(new Answer<Poll>() {
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
		Poll returnedPoll = this.service.createPoll(userProfile.getId(), poll);
		assertEquals(defaultId, returnedPoll.getId());
	}

	@Test(expected = EndDateInThePastException.class)
	public void CreatePollWithPassedEndDateTest() throws EndDateInThePastException, EndDateTooCloseException {
		UserProfile userProfile = new UserProfile(DEFAULT_EMAIL, USER_NAME, PASSWORD);
		final Long defaultId = new Long(1);
		userProfile.setId(defaultId);
		List<String> options = Arrays.asList("OPTION1", "OPTION2", "OPTION3");

		Poll poll = new Poll();

		poll.setEndDate(Instant.now().minus(Duration.ofDays(2)));

		when(this.userRepo.findOne(userProfile.getId())).thenReturn(userProfile);

		this.service.createPoll(userProfile.getId(), poll);
	}


	@Test
	public void ClosePollTest() throws UserWithoutPermisionException, PollAlreadyClosedException {
		UserProfile userProfile = new UserProfile(DEFAULT_EMAIL, USER_NAME, PASSWORD);
		final Long defaultId= new Long(1);
		userProfile.setId(defaultId);
		List<String> optionsDescriptions = Arrays.asList("OPTION1","OPTION2","OPTION3");
		Instant endDate = Instant.now();
		endDate = endDate.plus(Duration.ofDays(2));
		System.out.println(endDate);
		List<PollOption> pollOptions = new ArrayList<PollOption>();
		for (String desc : optionsDescriptions) {
			pollOptions.add(new PollOption(null, desc));
		}

		Poll poll = new Poll(userProfile, pollOptions, PollType.PROPOSAL, endDate);
		poll.setId(defaultId);

		poll.setAuthor(userProfile);

		when(this.userRepo.findOne(defaultId)).thenReturn(userProfile);
		when(this.pollRepo.findOne(defaultId)).thenReturn(poll);

		when(this.pollRepo.save(poll)).thenAnswer(new Answer<Poll>() {

			@Override
			public Poll answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				Poll poll = (Poll) args[0];
				assertEquals(defaultId, poll.getId());
				assertFalse(poll.getEndDate().isAfter(Instant.now()));
				return poll;
			}
		});
		this.service.closePoll(defaultId, defaultId);
		Mockito.verify(this.pollRepo).save(poll);

	}

	@Test(expected = UserWithoutPermisionException.class)
	public void ShoulThrowNotPermissionExceptionOnCloseTest()
			throws UserWithoutPermisionException, PollAlreadyClosedException {
		UserProfile userProfile = new UserProfile(DEFAULT_EMAIL, USER_NAME, PASSWORD);
		final Long defaultId = new Long(1);
		userProfile.setId(defaultId);
		List<String> options = Arrays.asList("OPTION1", "OPTION2", "OPTION3");
		Instant endDate = Instant.now();
		Duration duration = Duration.ofDays(2);

		endDate.plus(duration);

		UserProfile userProfile2 = new UserProfile(DEFAULT_EMAIL, USER_NAME.concat("1234"), PASSWORD);

		List<PollOption> pollOption = new ArrayList<PollOption>();
		userProfile.setId(new Long(2));

		for (String optionName : options) {
			PollOption option = new PollOption(null, optionName);
			pollOption.add(option);

		}
		Poll poll = new Poll(userProfile, pollOption, PollType.PROPOSAL, endDate);
		poll.setId(defaultId);
		poll.setAuthor(userProfile2);

		when(this.userRepo.findOne(defaultId)).thenReturn(userProfile);
		when(this.pollRepo.findOne(defaultId)).thenReturn(poll);

		this.service.closePoll(defaultId, defaultId);
	}

	@Test
	public void FindByIdTest() throws EntityNotFoundException {
		Poll poll = new Poll();
		Poll otherPoll = new Poll();

		poll.setId(POLL_ID);

		Mockito.when(this.pollRepo.findOne(POLL_ID)).thenReturn(poll);

		Poll returnedPoll = this.service.findPollById(POLL_ID);

		assertEquals(poll, returnedPoll);
	}

	@Test
	public void SavePollTest() throws EntityNotFoundException, UserIsNotTheAuthorException {
		Poll poll = new Poll();
		poll.setId(POLL_ID);
		UserProfile user = new UserProfile(DEFAULT_EMAIL, USER_NAME, PASSWORD);
		poll.setAuthor(user);

		Long userId = Long.valueOf(1);

		user.setId(userId);

		Mockito.when(this.pollRepo.findOne(POLL_ID)).thenReturn(poll);
		Mockito.when(this.userRepo.findOneByEmail(DEFAULT_EMAIL)).thenReturn(user);
		this.service.savePoll(poll, user.getEmail());

		Mockito.verify(this.pollRepo).save(poll);
	}

	@Test
	public void SavePollEntityNotFound() throws EntityNotFoundException, UserIsNotTheAuthorException {
		Poll poll = new Poll();
		poll.setId(POLL_ID);

		Long userId = Long.valueOf(1);

		Mockito.when(this.pollRepo.findOne(POLL_ID)).thenReturn(null);

		this.exception.expect(EntityNotFoundException.class);
		this.exception.expectMessage(
				String.format(EntityNotFoundException.DEFAULT_MESSAGE_FORMAT, poll.getId(),
						Poll.class.getName()));

		this.service.savePoll(poll, DEFAULT_EMAIL);

	}

	@Test
	public void SavePollUserNotFoundException() throws EntityNotFoundException, UserIsNotTheAuthorException {
		Poll poll = new Poll();
		poll.setId(POLL_ID);

		Long userId = Long.valueOf(1);

		Mockito.when(this.pollRepo.findOne(POLL_ID)).thenReturn(poll);
		Mockito.when(this.userRepo.findOneByEmail(DEFAULT_EMAIL)).thenReturn(null);

		this.exception.expect(EntityNotFoundException.class);
		this.exception.expectMessage(
				String.format(EntityNotFoundException.DEFAULT_MESSAGE_FORMAT, DEFAULT_EMAIL,
						UserProfile.class.getName()));
		this.service.savePoll(poll, DEFAULT_EMAIL);

	}

	@Test
	public void SavePollUserIsNotTheAuthor() throws EntityNotFoundException, UserIsNotTheAuthorException {
		Poll poll = new Poll();
		poll.setId(POLL_ID);
		UserProfile user = new UserProfile(DEFAULT_EMAIL, USER_NAME, PASSWORD);
		poll.setAuthor(null);

		Long userId = Long.valueOf(1);

		user.setId(userId);

		Mockito.when(this.pollRepo.findOne(POLL_ID)).thenReturn(poll);
		Mockito.when(this.userRepo.findOneByEmail(DEFAULT_EMAIL)).thenReturn(user);

		this.exception.expect(UserIsNotTheAuthorException.class);
		this.exception.expectMessage(
				String.format(UserIsNotTheAuthorException.DEFAULT_MESSAGE_FORMAT, userId.toString(), POLL_ID.toString()));

		this.service.savePoll(poll, DEFAULT_EMAIL);
	}

	@Test
	public void FindPollsByKeywords() {
		final Poll poll = new Poll();
		poll.setName("Nombre pRUEBA");
		final String keywords = "Nombre rue";
		final boolean onDescription = false;

		Mockito.when(
				this.pollRepo.findAll(ArgumentMatchers.<Specification<Poll>>any()))
				.thenAnswer(new Answer<List<Poll>>() {

					@SuppressWarnings("unchecked")
					@Override
					public List<Poll> answer(InvocationOnMock invocation) throws Throwable {
						Object[] args = invocation.getArguments();
						SpecificationFilter<Poll> filter = (SpecificationFilter<Poll>) args[0];
						Object[] specificationArgs = filter.getArgs();
						List<String> keywordsList = (List<String>) specificationArgs[0];
						assertTrue(keywordsList.containsAll(Arrays.asList(keywords.split(" "))));
						Boolean onDescToo = (Boolean) specificationArgs[1];
						assertEquals(Boolean.valueOf(onDescription), onDescToo);

						return Arrays.asList(poll);
					}
				});

		List<Poll> polls = this.service.findByKeywords(keywords, onDescription);
		assertTrue(polls.contains(poll));
	}

}
