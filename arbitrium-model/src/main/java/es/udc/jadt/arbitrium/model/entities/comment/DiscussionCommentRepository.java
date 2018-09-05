package es.udc.jadt.arbitrium.model.entities.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.udc.jadt.arbitrium.model.entities.discussion.Discussion;

@Repository
public interface DiscussionCommentRepository extends JpaRepository<DiscussionComment, DiscussionCommentPK> {

	List<DiscussionComment> findByDiscussion(Discussion discussion);
}
