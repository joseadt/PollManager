package es.udc.jadt.reupoll.model.request;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.udc.jadt.reupoll.model.group.UserGroup;
import es.udc.jadt.reupoll.model.userprofile.UserProfile;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

	List<Request> findByRequestor(UserProfile requestor);

	List<Request> findByAdmin(UserProfile admin);

	List<Request> findByUserGroup(UserGroup userGroup);

	List<Request> findByUserGroupIn(List<UserGroup> userGroup);
}
