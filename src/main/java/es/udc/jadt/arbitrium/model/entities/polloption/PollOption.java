package es.udc.jadt.arbitrium.model.entities.polloption;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.vote.Vote;

@Entity
@IdClass(PollOptionPk.class)
public class PollOption implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long optionId;

	@Id
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "poll_id")
	private Poll poll;

	private String description;

	@ManyToMany(mappedBy = "selectedOptions", fetch = FetchType.LAZY)
	private List<Vote> votes;

	@ManyToOne
	private UserProfile addedBy;

	public PollOption() {
	}

	public PollOption(Poll poll, String description) {
		this.poll = poll;
		this.description = description;
		this.addedBy = null;
	}

	public PollOption(Poll poll, String description, UserProfile addedBy) {
		this.poll = poll;
		this.description = description;
		this.addedBy = addedBy;
	}

	public Long getOptionId() {
		return this.optionId;
	}

	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}

	public Poll getPoll() {
		return this.poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Vote> getVotes() {
		return this.votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
		result = prime * result + ((this.optionId == null) ? 0 : this.optionId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PollOption other = (PollOption) obj;
		if (this.description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!this.description.equals(other.description)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return this.description;
	}




}
