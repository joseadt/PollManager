package es.udc.jadt.arbitrium.model.entities.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscussionCommentRepository extends JpaRepository<DiscussionComment, DiscussionCommentPK> {
}
