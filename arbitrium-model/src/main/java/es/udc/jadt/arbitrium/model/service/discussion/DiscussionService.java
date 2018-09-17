package es.udc.jadt.arbitrium.model.service.discussion;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.jadt.arbitrium.model.entities.comment.DiscussionComment;
import es.udc.jadt.arbitrium.model.entities.discussion.Discussion;
import es.udc.jadt.arbitrium.model.entities.discussion.DiscussionRepository;
import es.udc.jadt.arbitrium.model.entities.group.GroupRepository;
import es.udc.jadt.arbitrium.model.entities.group.UserGroup;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfileRepository;
import es.udc.jadt.arbitrium.model.service.util.ServiceHelper;
import es.udc.jadt.arbitrium.model.service.util.exceptions.EntityNotFoundException;
import es.udc.jadt.arbitrium.util.exceptions.UserWithoutPermisionException;

/**
 * The Class DiscussionService.
 *
 * @author JADT
 */
@Service
public class DiscussionService {

	/** The discussion repository. */
	@Autowired
	private DiscussionRepository discussionRepository;

	/** The group repository. */
	@Autowired
	private GroupRepository groupRepository;

	/** The user repository. */
	@Autowired
	private UserProfileRepository userRepository;

	/**
	 * Creates the discussion.
	 *
	 * @param tittle
	 *            the tittle
	 * @param description
	 *            the description
	 * @param email
	 *            the email
	 * @param groupId
	 *            the group id
	 * @return the discussion
	 * @throws EntityNotFoundException
	 *             the entity not found exception
	 * @throws UserWithoutPermisionException
	 *             the user without permision exception
	 */
	@Transactional
	public Discussion createDiscussion(String tittle, String description, String email, Long groupId)
			throws EntityNotFoundException, UserWithoutPermisionException {
		UserProfile user = this.userRepository.findOneByEmail(email);
		if (user == null) {
			throw new EntityNotFoundException(UserProfile.class, email);
		}
		UserGroup userGroup = ServiceHelper.findOneById(this.groupRepository, groupId, UserGroup.class);

		if (userGroup.getMembers() == null || !userGroup.getMembers().contains(user)) {
			throw new UserWithoutPermisionException("Can't create discussion: Doesn't belong to group", userGroup);
		}

		Discussion discussion = new Discussion();
		discussion.setCreatedBy(user);
		discussion.setUserGroup(userGroup);

		discussion.setTittle(tittle);
		discussion.setDescription(description);
		discussion.setCreationDate(LocalDate.now());

		return this.discussionRepository.save(discussion);
	}

	/**
	 * Find discussion.
	 *
	 * @param id
	 *            the discussion id
	 * @return the discussion
	 * @throws EntityNotFoundException
	 *             the entity not found exception
	 */
	@Transactional
	public Discussion findDiscussion(Long id) throws EntityNotFoundException {
		Discussion discussion = null;

		discussion = this.discussionRepository.findByIdWithComments(id)
				.orElseThrow(() -> new EntityNotFoundException(Discussion.class, id));

		return discussion;
	}

	/**
	 * Adds the comment.
	 *
	 * @param discussionId
	 *            the discussion id
	 * @param commentContent
	 *            the comment content
	 * @param userEmail
	 *            the user email
	 * @throws EntityNotFoundException
	 *             the entity not found exception
	 */
	@Transactional
	public void addComment(Long discussionId, String commentContent, String userEmail) throws EntityNotFoundException {
		UserProfile userProfile = this.userRepository.findOneByEmail(userEmail);

		if (userProfile == null) {
			throw new EntityNotFoundException(UserProfile.class, userEmail);
		}

		Optional<Discussion> optDiscussion = this.discussionRepository.findById(discussionId);

		if (!optDiscussion.isPresent()) {
			throw new EntityNotFoundException(Discussion.class, discussionId);
		}

		Discussion discussion = optDiscussion.get();

		DiscussionComment comment = new DiscussionComment(userProfile, commentContent, discussion);
		discussion.addComment(comment);

		this.discussionRepository.save(discussion);
	}

}
