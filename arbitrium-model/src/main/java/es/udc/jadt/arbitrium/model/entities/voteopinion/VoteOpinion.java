package es.udc.jadt.arbitrium.model.entities.voteopinion;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.vote.Vote;

@Entity
@IdClass(VoteOpinionPK.class)
public class VoteOpinion {

	@Id
	@ManyToOne
	private UserProfile user;

	@Id
	@ManyToOne
	private Vote vote;

	private String content;

	private boolean positive;

	public VoteOpinion() {
	}

	public VoteOpinion(UserProfile user, Vote vote, String content, boolean positive) {
		this.user = user;
		this.vote = vote;
		this.content = content;
		this.positive = positive;
	}

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	public Vote getVote() {
		return vote;
	}

	public void setVote(Vote vote) {
		this.vote = vote;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isPositive() {
		return positive;
	}

	public void setPositive(boolean positive) {
		this.positive = positive;
	}

}
