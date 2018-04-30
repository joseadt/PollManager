package es.udc.jadt.reupoll.model.poll;


import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import es.udc.jadt.reupoll.model.group.UserGroup;
import es.udc.jadt.reupoll.model.polloption.PollOption;
import es.udc.jadt.reupoll.model.userprofile.UserProfile;

@Entity
public class Poll implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private UserProfile author;

	@OneToMany(mappedBy = "poll", fetch = FetchType.EAGER)
	private List<PollOption> options;

	private PollType pollType;

	private Date creationDate;

	private Date endDate;

	@ManyToMany
	private List<UserGroup> groups;

	@ManyToMany
	private List<UserProfile> independantParticipants;


	public Poll() {

	}

	public Poll(UserProfile author, List<PollOption> options, PollType pollType, Date creationDate,
			Date endDate) {

		this.author = author;
		this.options = options;
		this.pollType = pollType;
		this.creationDate = creationDate;
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
