package es.udc.jadt.reupoll.model.vote;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import es.udc.jadt.reupoll.model.polloption.PollOption;
import es.udc.jadt.reupoll.model.userprofile.UserProfile;

@Entity
public class Vote implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToMany
	private List<PollOption> selectedOptions;

	private String comment;

	@ManyToOne
	private UserProfile user;

	public Vote() {
	}

	public Vote(List<PollOption> selectedOptions, String comment, UserProfile user) {
		this.selectedOptions = selectedOptions;
		this.comment = comment;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<PollOption> getSelectedOptions() {
		return selectedOptions;
	}

	public void setSelectedOptions(List<PollOption> selectedOptions) {
		this.selectedOptions = selectedOptions;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

}
