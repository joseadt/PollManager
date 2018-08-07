package es.udc.jadt.arbitrium.controller.discussion;

/**
 * The Class CommentForm.
 *
 * @author JADT
 */
public class CommentForm {

	public Long discussionId;

	public String content;

	/**
	 * @param discussionId
	 */
	public CommentForm(Long discussionId) {
		this.discussionId = discussionId;
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

	/**
	 * @return the discussionId
	 */
	public Long getDiscussionId() {
		return this.discussionId;
	}

	/**
	 * @param discussionId
	 *            the discussionId to set
	 */
	public void setDiscussionId(Long discussionId) {
		this.discussionId = discussionId;
	}

}
