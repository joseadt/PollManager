package es.udc.jadt.arbitrium.model.service.vote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.poll.PollRepository;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOptionPk;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOptionRepository;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfileRepository;
import es.udc.jadt.arbitrium.model.entities.vote.Vote;
import es.udc.jadt.arbitrium.model.entities.vote.VoteRepository;
import es.udc.jadt.arbitrium.model.service.util.exceptions.EntityNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class VoteServiceTest {

	@Mock
	private VoteRepository voteRepository;
	@Mock
	private PollRepository pollRepository;
	@Mock
	private UserProfileRepository userProfileRepository;
	@Mock
	private PollOptionRepository pollOptionRepository;
	@InjectMocks
	private VoteService service;

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	private static final Long DEFAULT_ID = 1L;

	private static final Long OPTION_ID_1 = 1L;

	private static final Long OPTION_ID_2 = 2L;

	private static final Long NON_EXISTANT_OPTION_ID = -1L;

	private static final String DEFAULT_EMAIL = "john.doe@email.com";

	private static final String NON_EXISTENT_EMAIL = "no@one.com";

	private static final String DEFAULT_USERNAME = "John Doe";

	private static final String DEFAULT_PASSWORD = "QWERTY";

	private static final UserProfile USER = new UserProfile(DEFAULT_EMAIL, DEFAULT_USERNAME, DEFAULT_PASSWORD);

	private static final Poll POLL = new Poll();

	private static final PollOption OPTION_1 = new PollOption();

	private static final PollOption OPTION_2 = new PollOption();

	private static final List<PollOption> OPTIONS = Arrays.asList(OPTION_1, OPTION_2);

	@BeforeClass
	public static void initializeClass() {
		USER.setId(DEFAULT_ID);

		POLL.setId(DEFAULT_ID);
		POLL.setAuthor(USER);
		OPTION_1.setPoll(POLL);
		OPTION_1.setOptionId(OPTION_ID_1);
		OPTION_2.setPoll(POLL);
		OPTION_2.setOptionId(OPTION_ID_2);
		POLL.addOption(OPTION_1);
		POLL.addOption(OPTION_2);

	}

	@Before
	public void initialize() {
		when(this.userProfileRepository.findOneByEmail(DEFAULT_EMAIL)).thenReturn(USER);
		when(this.pollRepository.findById(DEFAULT_ID)).thenReturn(Optional.ofNullable(POLL));
		when(this.pollOptionRepository.findById(any(PollOptionPk.class)))
				.thenAnswer(new Answer<Optional<PollOption>>() {

					@Override
					public Optional<PollOption> answer(InvocationOnMock invocation) throws Throwable {
						Object[] args = invocation.getArguments();
						PollOptionPk pk = (PollOptionPk) args[0];
						if (pk.getPoll().equals(POLL)) {
							if (pk.getOptionId().equals(OPTION_ID_1)) {
								return Optional.of(OPTION_1);
							}

							if (pk.getOptionId().equals(OPTION_ID_2)) {
								return Optional.of(OPTION_2);
							}
						}

						return Optional.empty();
					}
				});
		when(this.voteRepository.save(any(Vote.class))).thenAnswer(new Answer<Vote>() {

			@Override
			public Vote answer(InvocationOnMock invocation) throws Throwable {
				Vote vote = (Vote) invocation.getArguments()[0];
				vote.setId(1L);
				return vote;
			}
		});

	}

	@Test
	public void voteTest() throws EntityNotFoundException {

		String comment = "MY COMMENT";
		Vote vote = this.service.createVote(DEFAULT_EMAIL, DEFAULT_ID, Arrays.asList(OPTION_ID_1, OPTION_ID_2),
				comment);

		Mockito.verify(this.voteRepository).save(any(Vote.class));
		assertNotNull(vote);
		assertNotNull(vote.getId());
		assertTrue(vote.getSelectedOptions().containsAll(OPTIONS));
		assertEquals(OPTIONS.size(), vote.getSelectedOptions().size());
		assertEquals(USER, vote.getUser());
		assertNotNull(vote.getComment());
		assertEquals(comment, vote.getComment());

	}

	@Test
	public void userNotFoundExceptionTest() throws EntityNotFoundException {

		when(this.userProfileRepository.findOneByEmail(NON_EXISTENT_EMAIL)).thenReturn(null);
		this.exception.expect(EntityNotFoundException.class);
		this.exception.expectMessage(EntityNotFoundException.messageExample(UserProfile.class, NON_EXISTENT_EMAIL));

		this.service.createVote(NON_EXISTENT_EMAIL, DEFAULT_ID, Arrays.asList(OPTION_ID_1, OPTION_ID_2), null);

	}

	@Test
	public void PollNotFoundExceptionTest() throws EntityNotFoundException {

		when(this.pollRepository.findById(DEFAULT_ID)).thenReturn(Optional.empty());

		this.exception.expect(EntityNotFoundException.class);
		this.exception.expectMessage(
				String.format(EntityNotFoundException.DEFAULT_MESSAGE_FORMAT, DEFAULT_ID, Poll.class.getName()));

		this.service.createVote(DEFAULT_EMAIL, DEFAULT_ID, Arrays.asList(OPTION_ID_1, OPTION_ID_2), null);
	}

	@Test
	public void OptionNotFoundExceptionTest() throws EntityNotFoundException {

		this.exception.expect(EntityNotFoundException.class);
		this.exception.expectMessage(String.format(EntityNotFoundException.DEFAULT_MESSAGE_FORMAT,
				new PollOptionPk(NON_EXISTANT_OPTION_ID, POLL), PollOption.class.getName()));
		this.service.createVote(DEFAULT_EMAIL, DEFAULT_ID, Arrays.asList(OPTION_ID_1, NON_EXISTANT_OPTION_ID), null);
	}
}
