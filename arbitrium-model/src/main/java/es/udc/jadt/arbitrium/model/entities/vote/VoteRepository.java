package es.udc.jadt.arbitrium.model.entities.vote;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.udc.jadt.arbitrium.model.entities.comment.Comment;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

	Vote findOneByIdAndUserEmail(Long id, String email);

	@Query("SELECT new es.udc.jadt.arbitrium.model.entities.comment.Comment(v.user,v.comment) FROM "
			+ "Vote v INNER JOIN v.selectedOptions o WHERE (o.poll.id = :id) AND (v.comment IS NOT EMPTY OR v.comment <> '')")
	List<Comment> findPollComments(@Param("id") Long pollId);

}
