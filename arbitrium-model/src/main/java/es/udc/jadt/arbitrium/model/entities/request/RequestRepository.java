package es.udc.jadt.arbitrium.model.entities.request;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.udc.jadt.arbitrium.model.entities.group.UserGroup;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

	List<Request> findByRequestor(UserProfile requestor);

	List<Request> findByAdmin(UserProfile admin);

	List<Request> findByUserGroup(UserGroup userGroup);

	List<Request> findByUserGroupIn(List<UserGroup> userGroup);
}
