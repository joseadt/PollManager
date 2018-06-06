package es.udc.jadt.arbitrium.model.entities.polloption;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
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
	@ManyToOne(fetch = FetchType.LAZY)
	private Poll poll;

	private String description;
	
	@ManyToMany(mappedBy = "selectedOptions")
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
		return optionId;
	}

	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}

	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}

}
