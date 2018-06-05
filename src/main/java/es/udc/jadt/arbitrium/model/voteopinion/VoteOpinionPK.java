package es.udc.jadt.arbitrium.model.voteopinion;

import java.io.Serializable;

import es.udc.jadt.arbitrium.model.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.vote.Vote;

public class VoteOpinionPK implements Serializable {

	private UserProfile user;

	private Vote vote;

	public VoteOpinionPK() {
	}

	public VoteOpinionPK(UserProfile user, Vote vote) {
		this.user = user;
		this.vote = vote;
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

}
