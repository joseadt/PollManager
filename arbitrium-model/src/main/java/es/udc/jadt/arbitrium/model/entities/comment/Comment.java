package es.udc.jadt.arbitrium.model.entities.comment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import es.udc.jadt.arbitrium.model.entities.discussion.Discussion;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;

@Entity
@IdClass(CommentPK.class)
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Id
	@ManyToOne
	private Discussion discussion;

	@ManyToOne
	private UserProfile commentedBy;

	private String content;

	public Comment() {
	}

	public Comment(Discussion discussion, UserProfile commentedBy, String content) {
		this.discussion = discussion;
		this.commentedBy = commentedBy;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Discussion getDiscussion() {
		return discussion;
	}

	public void setDiscussion(Discussion discussion) {
		this.discussion = discussion;
	}

	public UserProfile getCommentedBy() {
		return commentedBy;
	}

	public void setCommentedBy(UserProfile commentedBy) {
		this.commentedBy = commentedBy;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
