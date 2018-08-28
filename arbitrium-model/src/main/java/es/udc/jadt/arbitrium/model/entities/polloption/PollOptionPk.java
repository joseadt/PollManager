package es.udc.jadt.arbitrium.model.entities.polloption;

import java.io.Serializable;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;

public class PollOptionPk implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Long optionId;

	private Poll poll;

	public PollOptionPk() {
	}

	public PollOptionPk(Long optionId, Poll poll) {
		this.optionId = optionId;
		this.poll = poll;
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

	@Override
	public String toString() {
		return "PollOptionPk [optionId=" + this.optionId + ", poll=" + this.poll + "]";
	}

}
