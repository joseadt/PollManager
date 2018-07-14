package es.udc.jadt.arbitrium.model.service.group;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.Before;
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

import es.udc.jadt.arbitrium.model.entities.group.GroupRepository;
import es.udc.jadt.arbitrium.model.entities.group.UserGroup;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfileRepository;
import es.udc.jadt.arbitrium.model.service.util.EntityNotFoundException;
import es.udc.jadt.arbitrium.model.service.util.exceptions.UserAlreadyInGroupException;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceTest {

	@Mock
	private UserProfileRepository userRepository;

	@Mock
	private GroupRepository groupRepository;

	@InjectMocks
	private GroupService groupService;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	private static final String DEFAULT_GROUP_NAME = "GROUP 1";

	private static final String DEFAULT_USER_EMAIL = "john.doe@email.com";

	private static final String NON_EXISTENT_EMAIL = "noone@nomail.com";

	private static final String DEFAULT_PASSWORD = "12345";

	private static final Long DEFAULT_GROUP_ID = 333L;

	private static final Long NON_EXISTENT_ID = -1L;

	private UserProfile defaultUser;

	private UserGroup defaultGroup;

	@Before
	public void initializeTest() {
		this.defaultGroup = new UserGroup();
		this.defaultGroup.setName(DEFAULT_GROUP_NAME);
		this.defaultGroup.setId(DEFAULT_GROUP_ID);
		this.defaultGroup.setIsPrivate(false);

		this.defaultUser = new UserProfile(DEFAULT_USER_EMAIL, DEFAULT_USER_EMAIL,
					DEFAULT_PASSWORD);

		when(this.userRepository.findOneByEmail(DEFAULT_USER_EMAIL)).thenReturn(this.defaultUser);
		when(this.userRepository.findOneByEmail(not(eq(DEFAULT_USER_EMAIL)))).thenReturn(null);
		when(this.groupRepository.getOne(DEFAULT_GROUP_ID)).thenReturn(this.defaultGroup);
		when(this.groupRepository.getOne(not(eq(DEFAULT_GROUP_ID))))
				.thenThrow(javax.persistence.EntityNotFoundException.class);

		when(this.groupRepository.save(any(UserGroup.class))).thenAnswer(new Answer<UserGroup>() {

			@Override
			public UserGroup answer(InvocationOnMock invocation) throws Throwable {
				UserGroup group = invocation.getArgument(0);
				group.setId(DEFAULT_GROUP_ID);

				return group;
			}
		});
	}

	@Test
	public void createGroupTest() throws EntityNotFoundException {

		UserGroup group = this.groupService.createGroup(DEFAULT_GROUP_NAME, DEFAULT_USER_EMAIL, false);

		assertNotNull(group);
		assertEquals(DEFAULT_GROUP_ID, group.getId());
		assertEquals(DEFAULT_GROUP_NAME, group.getName());
		assertNotNull(group.getMembers());
		assertEquals(1, group.getMembers().size());
		assertTrue(group.getMembers().contains(this.defaultUser));

		Mockito.verify(this.groupRepository).save(group);

	}

	@Test
	public void createGroupUserNotFoundTest() throws EntityNotFoundException {
		this.exception.expect(EntityNotFoundException.class);
		this.exception.expectMessage(EntityNotFoundException.messageExample(UserProfile.class, NON_EXISTENT_EMAIL));
		this.groupService.createGroup(DEFAULT_GROUP_NAME, NON_EXISTENT_EMAIL, false);
	}

	@Test
	public void joinGroupTest() throws Exception {
		this.groupService.joinGroup(DEFAULT_GROUP_ID, DEFAULT_USER_EMAIL);

		assertNotNull(this.defaultUser.getUserGroups());
		assertFalse(this.defaultUser.getUserGroups().isEmpty());
		assertTrue(this.defaultUser.getUserGroups().contains(this.defaultGroup));

		assertNotNull(this.defaultGroup.getMembers());
		assertFalse(this.defaultGroup.getMembers().isEmpty());
		assertTrue(this.defaultGroup.getMembers().contains(this.defaultUser));

	}

	@Test
	public void joinGroupUserNotFoundTest() throws EntityNotFoundException, UserAlreadyInGroupException {
		this.exception.expect(EntityNotFoundException.class);
		this.exception.expectMessage(EntityNotFoundException.messageExample(UserProfile.class, NON_EXISTENT_EMAIL));
		this.groupService.joinGroup(DEFAULT_GROUP_ID, NON_EXISTENT_EMAIL);
	}

	@Test
	public void joinGroupUserGroupNotFoundTest() throws EntityNotFoundException, UserAlreadyInGroupException {
		this.exception.expect(EntityNotFoundException.class);
		this.exception.expectMessage(EntityNotFoundException.messageExample(UserGroup.class, NON_EXISTENT_ID));
		this.groupService.joinGroup(NON_EXISTENT_ID, DEFAULT_USER_EMAIL);
	}

	@Test
	public void joinGroupUserAlreadyInGroupTest() throws EntityNotFoundException, UserAlreadyInGroupException {
		this.defaultUser.addGroup(this.defaultGroup);
		this.defaultGroup.addMember(this.defaultUser);
		this.exception.expect(UserAlreadyInGroupException.class);
		this.exception
				.expectMessage(UserAlreadyInGroupException.messageFormatExample(DEFAULT_USER_EMAIL, DEFAULT_GROUP_ID));
		this.groupService.joinGroup(DEFAULT_GROUP_ID, DEFAULT_USER_EMAIL);
	}
}
