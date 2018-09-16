/*
 *
 */
package es.udc.jadt.arbitrium.model.service.group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.jadt.arbitrium.model.entities.group.GroupRepository;
import es.udc.jadt.arbitrium.model.entities.group.UserGroup;
import es.udc.jadt.arbitrium.model.entities.group.specifications.GroupFilters;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfileRepository;
import es.udc.jadt.arbitrium.model.service.util.ServiceHelper;
import es.udc.jadt.arbitrium.model.service.util.exceptions.EntityNotFoundException;
import es.udc.jadt.arbitrium.model.service.util.exceptions.UserAlreadyInGroupException;

// TODO: Auto-generated Javadoc
/**
 * The Class GroupService.
 *
 * @author JADT
 */
@Service
public class GroupService {

	/** The user repository. */
	@Autowired
	private UserProfileRepository userRepository;

	/** The group repository. */
	@Autowired
	private GroupRepository groupRepository;

	/**
	 * Creates a group.
	 *
	 * @param groupName
	 *            the group name
	 * @param userEmail
	 *            the user email
	 * @param isPrivate
	 *            true if is a private group
	 * @return the user group
	 * @throws EntityNotFoundException
	 *             If some entity isn't found
	 */
	@Transactional
	public UserGroup createGroup(String groupName, String userEmail, boolean isPrivate) throws EntityNotFoundException {
		UserProfile user = this.userRepository.findOneByEmail(userEmail);
		if (user == null) {
			throw new EntityNotFoundException(UserProfile.class, userEmail);
		}
		UserGroup group = new UserGroup();
		group.setName(groupName);
		group.addMember(user);
		group.setIsPrivate(isPrivate);
		return this.groupRepository.save(group);

	}

	/**
	 * Join group.
	 *
	 * @param groupId
	 *            the group id
	 * @param userEmail
	 *            the user email
	 * @throws EntityNotFoundException
	 *             the entity not found exception
	 * @throws UserAlreadyInGroupException
	 *             the user already in group exception
	 */
	@Transactional
	public void joinGroup(Long groupId, String userEmail) throws EntityNotFoundException, UserAlreadyInGroupException {
		UserProfile user = this.userRepository.findOneByEmail(userEmail);
		if (user == null) {
			throw new EntityNotFoundException(UserProfile.class, userEmail);
		}
		UserGroup group = null;

		group = ServiceHelper.findOneById(this.groupRepository, groupId, UserGroup.class);

		if ((user.getUserGroups() != null && user.getUserGroups().contains(group))) {
			throw new UserAlreadyInGroupException(userEmail, groupId);
		}

		group.addMember(user);
		user.addGroup(group);

		this.groupRepository.save(group);
	}

	/**
	 * Find group by id.
	 *
	 * @param id
	 *            the id
	 * @return the user group
	 * @throws EntityNotFoundException
	 *             the entity not found exception
	 */
	@Transactional
	public UserGroup findGroupById(Long id) throws EntityNotFoundException {
		UserGroup group = ServiceHelper.findOneById(this.groupRepository, id, UserGroup.class);

		return group;
	}

	/**
	 * Search groups.
	 *
	 * @param index
	 *            the index
	 * @param keywords
	 *            the keywords
	 * @return the page
	 */
	@Transactional
	public Page<UserGroup> searchGroups(int index, String keywords) {
		List<String> keywordsList = (keywords != null) ? Arrays.asList(keywords.split(" ")) : new ArrayList<>();

		return this.groupRepository.findAll(GroupFilters.groupKeywordsFilter(keywordsList), PageRequest.of(index, 10));
	}

	/**
	 * Find groups by user.
	 *
	 * @param email
	 *            the email
	 * @return the list
	 */
	@Transactional
	public List<UserGroup> findGroupsByUser(String email) {
		return this.groupRepository.findByAMember(email);
	}

}
