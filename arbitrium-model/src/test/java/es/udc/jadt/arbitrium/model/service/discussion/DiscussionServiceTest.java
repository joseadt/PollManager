package es.udc.jadt.arbitrium.model.service.discussion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalMatchers.and;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import es.udc.jadt.arbitrium.model.entities.discussion.Discussion;
import es.udc.jadt.arbitrium.model.entities.discussion.DiscussionRepository;
import es.udc.jadt.arbitrium.model.entities.group.GroupRepository;
import es.udc.jadt.arbitrium.model.entities.group.UserGroup;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfileRepository;
import es.udc.jadt.arbitrium.model.service.util.EntityNotFoundException;
import es.udc.jadt.arbitrium.util.exceptions.UserWithoutPermisionException;

@RunWith(MockitoJUnitRunner.class)
public class DiscussionServiceTest {

	@Mock
	DiscussionRepository discussionRepository;

	@Mock
	UserProfileRepository userRepository;

	@Mock
	GroupRepository groupRepository;

	@InjectMocks
	DiscussionService service;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	private static final Long DEFAULT_ID = 1L;

	private static final Long DEFAULT_ID_2 = 2L;

	private static final Long NON_EXISTENT_ID = -1L;

	private static final String DEFAULT_EMAIL = "johnDoe@email.com";

	private static final String NON_EXISTENT_EMAIL = "nonExist@email.com";

	private static final String DEFAULT_PASSWORD = "password";

	private static final String DEFAULT_TITTLE = "TITTLE";

	private static final String DEFAULT_DESCRIPTION = "DESC";

	private static UserProfile USER_PROFILE;

	private static UserGroup USER_GROUP;

	private static UserGroup OTHER_GROUP;

	private static Discussion SAVED_DISCUSSION;


	@BeforeClass
	public static void initializeClass() {
		USER_PROFILE = new UserProfile(DEFAULT_EMAIL, DEFAULT_EMAIL, DEFAULT_PASSWORD);
		USER_PROFILE.setId(DEFAULT_ID);
		USER_GROUP = new UserGroup();
		USER_GROUP.setId(DEFAULT_ID);
		USER_GROUP.addMember(USER_PROFILE);
		USER_PROFILE.addGroup(USER_GROUP);

		OTHER_GROUP = new UserGroup();
		OTHER_GROUP.setId(DEFAULT_ID_2);

	}

	@Before
	public void initialize() {
		SAVED_DISCUSSION = null;
		when(this.userRepository.findOneByEmail(DEFAULT_EMAIL)).thenReturn(USER_PROFILE);
		when(this.userRepository.findOneByEmail(not(eq(DEFAULT_EMAIL)))).thenReturn(null);
		when(this.groupRepository.getOne(DEFAULT_ID)).thenReturn(USER_GROUP);
		when(this.groupRepository.getOne(DEFAULT_ID_2)).thenReturn(OTHER_GROUP);

		when(this.groupRepository.getOne(and(not(eq(DEFAULT_ID)), not(eq(DEFAULT_ID_2)))))
				.thenThrow(javax.persistence.EntityNotFoundException.class);
		when(this.discussionRepository.save(any())).thenAnswer(new Answer<Discussion>() {

			@Override
			public Discussion answer(InvocationOnMock invocation) throws Throwable {
				SAVED_DISCUSSION = invocation.getArgument(0);
				SAVED_DISCUSSION.setId(DEFAULT_ID);
				return SAVED_DISCUSSION;
			}

		});

	}

	@Test
	public void createDiscussionTest() throws Exception {
		Discussion discussion = this.service.createDiscussion(DEFAULT_TITTLE, DEFAULT_DESCRIPTION, DEFAULT_EMAIL,
				DEFAULT_ID);

		assertNotNull(discussion);
		assertEquals(DEFAULT_TITTLE, discussion.getTittle());
		assertEquals(USER_GROUP, discussion.getUserGroup());
		assertEquals(DEFAULT_DESCRIPTION, discussion.getDescription());
		assertEquals(SAVED_DISCUSSION, discussion);

		verify(this.discussionRepository).save(any());

	}

	@Test
	public void createDiscussionUserNotFoundTest() throws Exception {
		this.exception.expect(EntityNotFoundException.class);
		this.exception.expectMessage(EntityNotFoundException.messageExample(UserProfile.class, NON_EXISTENT_EMAIL));
		this.service.createDiscussion(DEFAULT_TITTLE, DEFAULT_DESCRIPTION, NON_EXISTENT_EMAIL, DEFAULT_ID);

	}

	@Test
	public void createDiscussionGroupNotFoundTest() throws Exception {
		this.exception.expect(EntityNotFoundException.class);
		this.exception.expectMessage(EntityNotFoundException.messageExample(UserGroup.class, NON_EXISTENT_ID));
		this.service.createDiscussion(DEFAULT_TITTLE, DEFAULT_DESCRIPTION, DEFAULT_EMAIL, NON_EXISTENT_ID);
	}

	@Test
	public void createDiscussionUserNotInGroupTest() throws Exception {

		this.exception.expect(UserWithoutPermisionException.class);
		this.service.createDiscussion(DEFAULT_TITTLE, DEFAULT_DESCRIPTION, DEFAULT_EMAIL, DEFAULT_ID_2);

	}

}
