package es.udc.jadt.arbitrium.model.entities.group;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<UserGroup, Long>, JpaSpecificationExecutor<UserGroup> {

	@Query("SELECT g FROM UserGroup g join g.members m where m.email= :email")
	List<UserGroup> findByAMember(@Param("email") String email);
}
