package es.udc.jadt.arbitrium.model.discussion;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import es.udc.jadt.arbitrium.model.comment.Comment;
import es.udc.jadt.arbitrium.model.group.UserGroup;
import es.udc.jadt.arbitrium.model.userprofile.UserProfile;

@Entity
public class Discussion {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private UserProfile createdBy;

	@ManyToOne
	private UserGroup userGroup;

	private String tittle;

	private String description;

	@OneToMany
	private List<Comment> comments;

	public Discussion() {
	}

	public Discussion(UserProfile createdBy, String tittle, String description) {
		this.createdBy = createdBy;
		this.tittle = tittle;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserProfile getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserProfile createdBy) {
		this.createdBy = createdBy;
	}

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
