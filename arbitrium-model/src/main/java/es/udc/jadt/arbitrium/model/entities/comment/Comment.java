package es.udc.jadt.arbitrium.model.entities.comment;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;

@MappedSuperclass
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;

	@ManyToOne
	protected UserProfile commentedBy;

	protected String content;

	protected Comment() {
	}

	/**
	 * @param commentedBy
	 * @param content
	 * @param discussion
	 */
	public Comment(UserProfile commentedBy, String content) {
		this.id = id;
		this.commentedBy = commentedBy;
		this.content = content;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the commentedBy
	 */
	public UserProfile getCommentedBy() {
		return this.commentedBy;
	}

	/**
	 * @param commentedBy
	 *            the commentedBy to set
	 */
	public void setCommentedBy(UserProfile commentedBy) {
		this.commentedBy = commentedBy;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

}
