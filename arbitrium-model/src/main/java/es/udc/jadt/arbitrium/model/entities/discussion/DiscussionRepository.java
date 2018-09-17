package es.udc.jadt.arbitrium.model.entities.discussion;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.udc.jadt.arbitrium.model.entities.group.UserGroup;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Long> {

	List<Discussion> findByUserGroup(UserGroup userGroup);

	List<Discussion> findByUserGroupIn(List<UserGroup> userGroups);

	/**
	 * Find a discussion with the comments already initialized.
	 *
	 * @param id
	 *            the id
	 * @return the list
	 */
	@Query("SELECT d FROM Discussion d WHERE id = :id")
	@EntityGraph(attributePaths = { "comments" }, type = EntityGraphType.LOAD)
	Optional<Discussion> findByIdWithComments(@Param("id") Long id);
}
