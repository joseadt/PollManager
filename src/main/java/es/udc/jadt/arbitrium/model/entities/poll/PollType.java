package es.udc.jadt.arbitrium.model.entities.poll;

import es.udc.jadt.arbitrium.util.votecount.BasicPollConfiguration;
import es.udc.jadt.arbitrium.util.votecount.PollConfiguration;
import es.udc.jadt.arbitrium.util.votecount.ProposalConfiguration;

public enum PollType {

	POLL("POLL", BasicPollConfiguration.getInstance()),

	PROPOSAL("PROPOSAL", ProposalConfiguration.getInstance());

	private String name;

	private PollConfiguration configuration;


	PollType(String name, PollConfiguration configuration) {
		this.name = name;
		this.configuration = configuration;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public String longName() {
		return "PollType.".concat(this.name);
	}

	public PollConfiguration getConfiguration() {
		return this.configuration;
	}

	public String getName() {
		return this.name;
	}

}
