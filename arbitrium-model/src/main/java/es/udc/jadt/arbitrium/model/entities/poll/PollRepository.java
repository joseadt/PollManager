package es.udc.jadt.arbitrium.model.entities.poll;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.udc.jadt.arbitrium.model.entities.discussion.Discussion;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long>, JpaSpecificationExecutor<Poll> {

	/**
	 * Find a list of Polls using an id of a discussion
	 *
	 * @param discussion
	 * @return
	 */
	List<Poll> findByDiscussion(Discussion discussion);

	/**
	 * Find a list of Polls using an id of a discussion
	 *
	 * @param discussionId
	 *            The id of a discussion
	 * @return A list of polls
	 */
	@Query("SELECT p FROM Poll p where discussion.id = :discussionId")
	List<Poll> findByDiscussionId(@Param("discussionId") Long discussionId);

	/**
	 * Find a list of Polls that aren't associated with any discussion
	 * 
	 * @return A list of polls
	 */
	@Query("SELECT p FROM Poll p where discussion is null")
	List<Poll> findPollWithouDiscussion();
}
