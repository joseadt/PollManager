package es.udc.jadt.arbitrium.model.service.discussion;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.jadt.arbitrium.model.entities.discussion.Discussion;
import es.udc.jadt.arbitrium.model.entities.discussion.DiscussionRepository;
import es.udc.jadt.arbitrium.model.entities.group.GroupRepository;
import es.udc.jadt.arbitrium.model.entities.group.UserGroup;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfileRepository;
import es.udc.jadt.arbitrium.model.service.util.EntityNotFoundException;
import es.udc.jadt.arbitrium.util.exceptions.UserWithoutPermisionException;

@Service
public class DiscussionService {

	@Autowired
	private DiscussionRepository discussionRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private UserProfileRepository userRepository;

	@Transactional
	public Discussion createDiscussion(String tittle, String description, String email, Long groupId)
			throws EntityNotFoundException, UserWithoutPermisionException {
		UserProfile user = this.userRepository.findOneByEmail(email);
		if (user == null) {
			throw new EntityNotFoundException(UserProfile.class, email);
		}
		UserGroup userGroup = null;
		try {
			userGroup = this.groupRepository.getOne(groupId);
		 }catch(javax.persistence.EntityNotFoundException e){
			 throw new EntityNotFoundException(UserGroup.class,groupId);
		}

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
}
