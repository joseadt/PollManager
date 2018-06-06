package es.udc.jadt.arbitrium.model.comment;

import java.io.Serializable;

import es.udc.jadt.arbitrium.model.discussion.Discussion;

@SuppressWarnings("serial")
public class CommentPK implements Serializable {

	private Long id;
	
	private Discussion discussion;

	public CommentPK() {
	}

	public CommentPK(Long id, Discussion discussion) {
		this.id = id;
		this.discussion = discussion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Discussion getDiscussion() {
		return discussion;
	}

	public void setDiscussion(Discussion discussion) {
		this.discussion = discussion;
	}

}
