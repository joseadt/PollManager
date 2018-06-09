package es.udc.jadt.arbitrium.model.entities.poll;


import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import es.udc.jadt.arbitrium.model.entities.participantsgroup.ParticipantsGroup;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;

@Entity
public class Poll implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private UserProfile author;

	private String name;

	private String description;

	@OneToMany(mappedBy = "poll", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<PollOption> options;

	private PollType pollType;

	private Instant creationDate;

	private Instant endDate;

	@OneToMany(mappedBy = "poll", fetch = FetchType.LAZY)
	private List<ParticipantsGroup> participantsGroups;

	public Poll() {

	}

	public Poll(UserProfile author, List<PollOption> options, PollType pollType,
			Instant endDate) {

		this.author = author;
		this.options = options;
		this.pollType = pollType;
		this.creationDate = Instant.now();
		this.endDate = endDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserProfile getAuthor() {
		return author;
	}

	public void setAuthor(UserProfile author) {
		this.author = author;
	}

	public List<PollOption> getOptions() {
		return options;
	}

	public void setOptions(List<PollOption> options) {
		this.options = options;
	}

	public PollType getPollType() {
		return pollType;
	}

	public void setPollType(PollType pollType) {
		this.pollType = pollType;
	}

	public Instant getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Instant creationDate) {
		this.creationDate = creationDate;
	}

	public Instant getEndDate() {
		return endDate;
	}

	public void setEndDate(Instant endDate) {
		this.endDate = endDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ParticipantsGroup> getParticipantsGroups() {
		return participantsGroups;
	}

	public void setParticipantsGroups(List<ParticipantsGroup> participantsGroups) {
		this.participantsGroups = participantsGroups;
	}


}
