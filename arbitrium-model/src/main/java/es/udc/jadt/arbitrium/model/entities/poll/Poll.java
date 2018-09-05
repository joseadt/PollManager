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
import javax.persistence.OneToOne;

import es.udc.jadt.arbitrium.model.entities.group.UserGroup;
import es.udc.jadt.arbitrium.model.entities.participantsgroup.ParticipantsGroup;
import es.udc.jadt.arbitrium.model.entities.pollconfig.ConfigurationParameters;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.util.polltype.PollType;

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

	@OneToOne(cascade = CascadeType.ALL)
	private ConfigurationParameters configuration;

	@ManyToOne
	private UserGroup userGroup;

	public Poll() {

	}

	public Poll(UserProfile author, List<PollOption> options, PollType pollType,
			Instant endDate) {

		this.author = author;
		for (PollOption option : options) {
			option.setPoll(this);
		}
		this.options = options;
		this.pollType = pollType;
		this.creationDate = Instant.now();
		this.endDate = endDate;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserProfile getAuthor() {
		return this.author;
	}

	public void setAuthor(UserProfile author) {
		this.author = author;
	}

	public List<PollOption> getOptions() {
		return this.options;
	}

	public void setOptions(List<PollOption> options) {
		for (PollOption option : options) {
			option.setPoll(this);
		}
		this.options = options;
	}

	public PollType getPollType() {
		return this.pollType;
	}

	public void setPollType(PollType pollType) {
		this.pollType = pollType;
	}

	public Instant getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Instant creationDate) {
		this.creationDate = creationDate;
	}

	public Instant getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Instant endDate) {
		this.endDate = endDate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ParticipantsGroup> getParticipantsGroups() {
		return this.participantsGroups;
	}

	public void setParticipantsGroups(List<ParticipantsGroup> participantsGroups) {
		this.participantsGroups = participantsGroups;
	}

	public ConfigurationParameters getConfiguration() {
		return this.configuration;
	}

	public void setConfiguration(ConfigurationParameters configuration) {
		this.configuration = configuration;
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

	public void addOption(PollOption option) {
		option.setPoll(this);
		this.options.add(option);
	}
}
