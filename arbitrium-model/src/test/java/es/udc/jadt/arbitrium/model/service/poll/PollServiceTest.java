package es.udc.jadt.arbitrium.model.service.poll;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.BeforeClass;
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
import es.udc.jadt.arbitrium.model.entities.pollconfig.PollConfiguration;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfileRepository;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.EndDateInThePastException;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.UserIsNotTheAuthorException;
import es.udc.jadt.arbitrium.model.service.util.exceptions.EntityNotFoundException;
import es.udc.jadt.arbitrium.model.util.SpecificationFilter;
import es.udc.jadt.arbitrium.model.util.polltype.PollType;
import es.udc.jadt.arbitrium.util.exceptions.UserWithoutPermisionException;

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

	private final static String ANOTHER_EMAIL = "user2@email.com";

	private final static String USER_NAME = "John Doe";

	private final static String PASSWORD = "12345";

	private final static String NON_EXISTENT_EMAIL = "another@email.com";

	private final static Long DEFAULT_ID = new Long(1);

	private final static Long ANOTHER_ID = new Long(2);

	private static UserProfile userProfile;

	private static final UserProfile userProfile2 = new UserProfile(DEFAULT_EMAIL, USER_NAME.concat("1234"), PASSWORD);

	private final static List<String> options = Arrays.asList("OPTION1", "OPTION2", "OPTION3");

	private final static Instant endDate = Instant.now().plus(Duration.ofDays(2));

	private static Poll poll;

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@BeforeClass
	public static void initializeClass() {
		userProfile = new UserProfile(DEFAULT_EMAIL, USER_NAME, PASSWORD);
		List<PollOption> pollOptions = new ArrayList<PollOption>();
		for (String desc : options) {
			pollOptions.add(new PollOption(null, desc));
		}

		poll = new Poll(userProfile, pollOptions, PollType.PROPOSAL, endDate);
		poll.setId(DEFAULT_ID);

		userProfile2.setId(ANOTHER_ID);
	}

	@Before
	public void initialize() {
		poll.setId(DEFAULT_ID);
		poll.setAuthor(userProfile);
		when(this.userRepo.getOne(DEFAULT_ID)).thenReturn(userProfile);
		when(this.userRepo.findOneByEmail(DEFAULT_EMAIL)).thenReturn(userProfile);
		when(this.userRepo.findOneByEmail(ANOTHER_EMAIL)).thenReturn(userProfile2);
		when(this.userRepo.findOneByEmail(NON_EXISTENT_EMAIL)).thenReturn(null);
		when(this.pollRepo.findById(DEFAULT_ID)).thenReturn(Optional.of(poll));
		when(this.pollRepo.save(any(Poll.class))).thenAnswer(new Answer<Poll>() {
			@Override
			public Poll answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				Poll poll = (Poll) args[0];
				poll.setId(DEFAULT_ID);
				return poll;
			}
		});

	}

	@Test
	public void createPollTest() throws Exception {
		poll.setId(null);
		PollConfiguration pollConfiguration = new PollConfiguration();
		Poll returnedPoll = this.service.createPoll(DEFAULT_EMAIL, poll);
		assertEquals(DEFAULT_ID, returnedPoll.getId());
		poll.setId(null);
		Poll anotherPoll = this.service.createPoll(DEFAULT_EMAIL, poll, pollConfiguration, null);
		assertEquals(DEFAULT_ID, anotherPoll.getId());
	}

	@Test(expected = EndDateInThePastException.class)
	public void createPollWithPassedEndDateTest() throws Exception {
		userProfile.setId(DEFAULT_ID);

		Poll poll = new Poll();

		poll.setEndDate(Instant.now().minus(Duration.ofDays(2)));

		this.service.createPoll(userProfile.getEmail(), poll);
	}

	@Test
	public void closePollTest() throws Exception {

		poll.setAuthor(userProfile);

		this.service.closePoll(DEFAULT_ID, DEFAULT_ID);
		Mockito.verify(this.pollRepo).save(poll);

	}

	@Test(expected = UserWithoutPermisionException.class)
	public void shoulThrowNotPermissionExceptionOnCloseTest() throws Exception {
		List<String> options = Arrays.asList("OPTION1", "OPTION2", "OPTION3");
		Instant endDate = Instant.now();
		Duration duration = Duration.ofDays(2);

		endDate.plus(duration);

		poll.setId(DEFAULT_ID);
		poll.setAuthor(userProfile2);

		this.service.closePoll(DEFAULT_ID, DEFAULT_ID);
	}

	@Test
	public void findByIdTest() throws EntityNotFoundException {

		Poll returnedPoll = this.service.findPollById(DEFAULT_ID);

		assertEquals(poll, returnedPoll);
	}

	@Test
	public void savePollTest() throws EntityNotFoundException, UserIsNotTheAuthorException {
		this.service.savePoll(poll, DEFAULT_EMAIL);

		Mockito.verify(this.pollRepo).save(poll);
	}

	@Test
	public void savePollEntityNotFoundTest() throws EntityNotFoundException, UserIsNotTheAuthorException {
		poll.setId(POLL_ID);
		Mockito.when(this.pollRepo.findById(POLL_ID)).thenReturn(Optional.empty());

		this.exception.expect(EntityNotFoundException.class);
		this.exception.expectMessage(
				String.format(EntityNotFoundException.DEFAULT_MESSAGE_FORMAT, poll.getId(), Poll.class.getName()));

		this.service.savePoll(poll, DEFAULT_EMAIL);

	}

	@Test
	public void savePollUserNotFoundExceptionTest() throws EntityNotFoundException, UserIsNotTheAuthorException {
		this.exception.expect(EntityNotFoundException.class);
		this.exception.expectMessage(EntityNotFoundException.messageExample(UserProfile.class, NON_EXISTENT_EMAIL));
		this.service.savePoll(poll, NON_EXISTENT_EMAIL);

	}

	@Test
	public void savePollUserIsNotTheAuthor() throws EntityNotFoundException, UserIsNotTheAuthorException {

		this.exception.expect(UserIsNotTheAuthorException.class);
		this.exception.expectMessage(
				String.format(UserIsNotTheAuthorException.DEFAULT_MESSAGE_FORMAT, userProfile2.getId(), poll.getId()));

		this.service.savePoll(poll, ANOTHER_EMAIL);
	}

	@Test
	public void findPollsByKeywords() {
		final Poll poll = new Poll();
		poll.setName("Nombre pRUEBA");
		final String keywords = "Nombre rue";
		final boolean onDescription = false;

		Mockito.when(this.pollRepo.findAll(ArgumentMatchers.<Specification<Poll>>any()))
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
