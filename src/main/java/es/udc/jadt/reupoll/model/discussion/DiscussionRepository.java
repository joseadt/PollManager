package es.udc.jadt.reupoll.model.discussion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.udc.jadt.reupoll.model.group.UserGroup;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Long> {

	List<Discussion> findByUserGroup(UserGroup userGroup);
	
	List<Discussion> findByUserGroupIn(List<UserGroup> userGroups);
}
