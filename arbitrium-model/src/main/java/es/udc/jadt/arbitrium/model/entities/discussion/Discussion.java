package es.udc.jadt.arbitrium.model.entities.discussion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import es.udc.jadt.arbitrium.model.entities.comment.DiscussionComment;
import es.udc.jadt.arbitrium.model.entities.group.UserGroup;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;

@Entity
public class Discussion {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private UserProfile createdBy;

	@ManyToOne
	private UserGroup userGroup;

	private String tittle;

	private String description;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<DiscussionComment> comments;

	private LocalDate creationDate;

	public Discussion() {
	}

	public Discussion(UserProfile createdBy, String tittle, String description) {
		this.createdBy = createdBy;
		this.tittle = tittle;
		this.description = description;
		this.creationDate = LocalDate.now();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserProfile getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(UserProfile createdBy) {
		this.createdBy = createdBy;
	}

	public String getTittle() {
		return this.tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the userGroup
	 */
	public UserGroup getUserGroup() {
		return this.userGroup;
	}

	/**
	 * @param userGroup
	 *            the userGroup to set
	 */
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	/**
	 * @return the comments
	 */
	public List<DiscussionComment> getComments() {
		return this.comments;
	}

	/**
	 * Sets the comments.
	 *
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(List<DiscussionComment> comments) {
		this.comments = comments;
	}

	/**
	 * @return the creationDate
	 */
	public LocalDate getCreationDate() {
		return this.creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Adds the comment.
	 *
	 * @param comment
	 *            the comment
	 * @return true, if successful
	 */
	public boolean addComment(DiscussionComment comment) {
		if (this.comments == null) {
			this.comments = new ArrayList<>();
		}

		return this.comments.add(comment);
	}
}
