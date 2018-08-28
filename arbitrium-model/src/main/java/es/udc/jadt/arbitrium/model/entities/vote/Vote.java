package es.udc.jadt.arbitrium.model.entities.vote;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import es.udc.jadt.arbitrium.model.entities.group.UserGroup;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;

@Entity
public class Vote implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<PollOption> selectedOptions;

	private String token;

	private String comment;

	@ManyToOne
	private UserProfile user;

	/**
	 * The user group. Voto realizado como miembro de un grupo Registrado para poder
	 * recompilar datos del resultado o participacion sobre grupos.
	 */
	@ManyToOne
	private UserGroup userGroup;

	public Vote() {
	}

	public Vote(List<PollOption> selectedOptions, String comment, UserProfile user) {
		this.selectedOptions = selectedOptions;
		this.comment = comment;
		this.user = user;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<PollOption> getSelectedOptions() {
		return this.selectedOptions;
	}

	public void setSelectedOptions(List<PollOption> selectedOptions) {
		this.selectedOptions = selectedOptions;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public UserProfile getUser() {
		return this.user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

}
