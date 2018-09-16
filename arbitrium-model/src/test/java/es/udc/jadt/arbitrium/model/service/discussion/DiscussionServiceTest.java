package es.udc.jadt.arbitrium.model.service.discussion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalMatchers.and;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
import es.udc.jadt.arbitrium.model.service.util.exceptions.EntityNotFoundException;
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

	private static final Long DEFAULT_DISCUSSION_ID = 10L;

	private static final Long NON_EXISTENT_ID = -1L;

	private static final String DEFAULT_EMAIL = "johnDoe@email.com";

	private static final String NON_EXISTENT_EMAIL = "nonExist@email.com";

	private static final String DEFAULT_PASSWORD = "password";

	private static final String DEFAULT_TITTLE = "TITTLE";

	private static final String DEFAULT_DESCRIPTION = "DESC";

	private static final String DEFAULT_COMMENT_CONTENT = "Comment content";

	private static UserProfile USER_PROFILE;

	private static UserGroup USER_GROUP;

	private static UserGroup OTHER_GROUP;

	private static Discussion SAVED_DISCUSSION;

	private static Optional<Discussion> OPTIONAL_DISCUSSION;

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

		Discussion discussion = new Discussion(USER_PROFILE, DEFAULT_TITTLE, DEFAULT_DESCRIPTION);
		discussion.setId(DEFAULT_DISCUSSION_ID);
		OPTIONAL_DISCUSSION = Optional.of(discussion);

	}

	@Before
	public void initialize() {
		Discussion discussion = new Discussion(USER_PROFILE, DEFAULT_TITTLE, DEFAULT_DESCRIPTION);
		discussion.setId(DEFAULT_DISCUSSION_ID);
		OPTIONAL_DISCUSSION = Optional.of(discussion);

		SAVED_DISCUSSION = null;
		when(this.userRepository.findOneByEmail(DEFAULT_EMAIL)).thenReturn(USER_PROFILE);
		when(this.userRepository.findOneByEmail(not(eq(DEFAULT_EMAIL)))).thenReturn(null);
		when(this.groupRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(USER_GROUP));
		when(this.groupRepository.findById(DEFAULT_ID_2)).thenReturn(Optional.of(OTHER_GROUP));

		when(this.groupRepository.findById(and(not(eq(DEFAULT_ID)), not(eq(DEFAULT_ID_2)))))
				.thenReturn(Optional.empty());
		when(this.discussionRepository.save(any())).thenAnswer(new Answer<Discussion>() {

			@Override
			public Discussion answer(InvocationOnMock invocation) throws Throwable {
				SAVED_DISCUSSION = invocation.getArgument(0);
				SAVED_DISCUSSION.setId(DEFAULT_ID);
				return SAVED_DISCUSSION;
			}

		});

		when(this.discussionRepository.findById(DEFAULT_DISCUSSION_ID)).thenReturn(OPTIONAL_DISCUSSION);

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

	@Test
	public void addCommentTest() throws Exception {
		this.service.addComment(DEFAULT_DISCUSSION_ID, DEFAULT_COMMENT_CONTENT, DEFAULT_EMAIL);

		Discussion discussion = this.discussionRepository.findById(DEFAULT_DISCUSSION_ID).get();

		assertNotNull(discussion.getComments());
		assertEquals(1, discussion.getComments().size());
		assertEquals(DEFAULT_COMMENT_CONTENT, discussion.getComments().get(0).getContent());

	}

	@Test
	public void addCommentUserNotFoundTest() throws Exception {
		this.exception.expect(EntityNotFoundException.class);
		this.exception.expectMessage(EntityNotFoundException.messageExample(UserProfile.class, NON_EXISTENT_EMAIL));
		this.service.addComment(DEFAULT_DISCUSSION_ID, DEFAULT_COMMENT_CONTENT, NON_EXISTENT_EMAIL);
	}

	@Test
	public void addCommentDiscussionNotFoundTest() throws Exception {
		this.exception.expect(EntityNotFoundException.class);
		this.exception.expectMessage(EntityNotFoundException.messageExample(Discussion.class, NON_EXISTENT_ID));
		this.service.addComment(NON_EXISTENT_ID, DEFAULT_COMMENT_CONTENT, DEFAULT_EMAIL);

	}

}
