package es.udc.jadt.arbitrium.model.entities.participantsgroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.vote.Vote;

/**
 * The Class ParticipantsGroup. Gestion de censos separados para una misma
 * votacion.
 */
@SuppressWarnings("serial")
@Entity
public class ParticipantsGroup implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	

	@ManyToMany
	private List<UserProfile> individualParticipants;

	@ManyToMany
	private List<UserProfile> alreadyParticipant;

	@OneToMany(cascade = CascadeType.ALL)
	// Consideramos VOtes como participaciones.
	private List<Vote> votes;

	@ManyToOne
	private Poll poll;

	public ParticipantsGroup(List<UserProfile> individualParticipants, Poll poll) {

		this.individualParticipants = individualParticipants;
		votes = new ArrayList<Vote>();
		this.poll = poll;
	}

	public ParticipantsGroup(Poll poll) {
		this.poll = poll;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public List<UserProfile> getIndividualParticipants() {
		return individualParticipants;
	}

	public void setIndividualParticipants(List<UserProfile> individualParticipants) {
		this.individualParticipants = individualParticipants;
	}

	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}

	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

}
