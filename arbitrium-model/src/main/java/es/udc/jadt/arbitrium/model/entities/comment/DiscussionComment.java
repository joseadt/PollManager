package es.udc.jadt.arbitrium.model.entities.comment;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import es.udc.jadt.arbitrium.model.entities.discussion.Discussion;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;

@Entity
@IdClass(DiscussionCommentPK.class)
public class DiscussionComment extends Comment {


	@Id
	@ManyToOne
	private Discussion discussion;

	public DiscussionComment(Long id, UserProfile commentBy, String content, Discussion discussion) {
		super(id, commentBy, content);
		this.discussion = discussion;
	}

	/**
	 * @return the discussion
	 */
	public Discussion getDiscussion() {
		return this.discussion;
	}

	/**
	 * @param discussion
	 *            the discussion to set
	 */
	public void setDiscussion(Discussion discussion) {
		this.discussion = discussion;
	}

}
