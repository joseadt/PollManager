package es.udc.jadt.arbitrium.model.service.vote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

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
import es.udc.jadt.arbitrium.model.service.util.EntityNotFoundException;
import es.udc.jadt.arbitrium.model.service.vote.VoteService;

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

	private static final String DEFAULT_USERNAME = "John Doe";

	private static final String DEFAULT_PASSWORD = "QWERTY";

	@Test
	public void VoteTest() throws EntityNotFoundException {
		final Poll poll = new Poll();
		poll.setId(DEFAULT_ID);
		final PollOption option1 = new PollOption();
		option1.setPoll(poll);
		option1.setOptionId(OPTION_ID_1);
		final PollOption option2 = new PollOption();
		option2.setPoll(poll);
		option2.setOptionId(OPTION_ID_2);
		UserProfile user = new UserProfile(DEFAULT_EMAIL, DEFAULT_USERNAME, DEFAULT_PASSWORD);

		List<PollOption> optionsList = Arrays.asList(option1, option2);

		when(this.userProfileRepository.findOneByEmail(DEFAULT_EMAIL)).thenReturn(user);
		when(this.pollRepository.getOne(DEFAULT_ID)).thenReturn(poll);
		when(this.pollOptionRepository.getOne(any(PollOptionPk.class))).thenAnswer(new Answer<PollOption>() {

			@Override
			public PollOption answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				PollOptionPk pk = (PollOptionPk) args[0];
				if (pk.getPoll().equals(poll)) {
					if(pk.getOptionId().equals(OPTION_ID_1)) {
						return option1;
					}

					if (pk.getOptionId().equals(OPTION_ID_2)) {
						return option2;
					}
				}

				return null;
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

		Vote vote = this.service.createVote(DEFAULT_EMAIL, DEFAULT_ID, Arrays.asList(OPTION_ID_1, OPTION_ID_2));

		Mockito.verify(this.voteRepository).save(any(Vote.class));
		assertNotNull(vote);
		assertNotNull(vote.getId());
		assertTrue(vote.getSelectedOptions().containsAll(optionsList));
		assertEquals(optionsList.size(), vote.getSelectedOptions().size());
		assertEquals(user, vote.getUser());

	}

	@Test
	public void UserNotFoundExceptionTest() throws EntityNotFoundException {
		final Poll poll = new Poll();
		poll.setId(DEFAULT_ID);
		final PollOption option1 = new PollOption();
		option1.setPoll(poll);
		option1.setOptionId(OPTION_ID_1);
		final PollOption option2 = new PollOption();
		option2.setPoll(poll);
		option2.setOptionId(OPTION_ID_2);

		this.exception.expect(EntityNotFoundException.class);
		this.exception.expect(EntityNotFoundException.class);
		this.exception.expectMessage(String.format(EntityNotFoundException.DEFAULT_MESSAGE_FORMAT, DEFAULT_EMAIL,
				UserProfile.class.getName()));
		when(this.userProfileRepository.findOneByEmail(DEFAULT_EMAIL)).thenReturn(null);


		this.service.createVote(DEFAULT_EMAIL, DEFAULT_ID, Arrays.asList(OPTION_ID_1, OPTION_ID_2));

	}

	@Test
	public void PollNotFoundExceptionTest() throws EntityNotFoundException {
		UserProfile user = new UserProfile(DEFAULT_EMAIL, DEFAULT_USERNAME, DEFAULT_PASSWORD);


		when(this.userProfileRepository.findOneByEmail(DEFAULT_EMAIL)).thenReturn(user);
		when(this.pollRepository.getOne(DEFAULT_ID)).thenReturn(null);

		this.exception.expect(EntityNotFoundException.class);
		this.exception.expectMessage(
				String.format(EntityNotFoundException.DEFAULT_MESSAGE_FORMAT, DEFAULT_ID, Poll.class.getName()));

		this.service.createVote(DEFAULT_EMAIL, DEFAULT_ID, Arrays.asList(OPTION_ID_1, OPTION_ID_2));
	}

	@Test
	public void OptionNotFoundExceptionTest() throws EntityNotFoundException {
		final Poll poll = new Poll();
		poll.setId(DEFAULT_ID);
		final PollOption option1 = new PollOption();
		option1.setPoll(poll);
		option1.setOptionId(OPTION_ID_1);
		final PollOption option2 = new PollOption();
		option2.setPoll(poll);
		option2.setOptionId(OPTION_ID_2);
		UserProfile user = new UserProfile(DEFAULT_EMAIL, DEFAULT_USERNAME, DEFAULT_PASSWORD);


		when(this.userProfileRepository.findOneByEmail(DEFAULT_EMAIL)).thenReturn(user);
		when(this.pollRepository.getOne(DEFAULT_ID)).thenReturn(poll);
		when(this.pollOptionRepository.getOne(any(PollOptionPk.class))).thenAnswer(new Answer<PollOption>() {

			@Override
			public PollOption answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				PollOptionPk pk = (PollOptionPk) args[0];
				if (pk.getPoll().equals(poll)) {
					if(pk.getOptionId().equals(OPTION_ID_1)) {
						return option1;
					}

					if (pk.getOptionId().equals(OPTION_ID_2)) {
						return option2;
					}
				}

				return null;
			}
		});

		this.exception.expect(EntityNotFoundException.class);
		this.exception.expectMessage(
				String.format(EntityNotFoundException.DEFAULT_MESSAGE_FORMAT,
						new PollOptionPk(NON_EXISTANT_OPTION_ID, poll), PollOption.class.getName()));
		this.service.createVote(DEFAULT_EMAIL, DEFAULT_ID, Arrays.asList(OPTION_ID_1, NON_EXISTANT_OPTION_ID));
	}
}
