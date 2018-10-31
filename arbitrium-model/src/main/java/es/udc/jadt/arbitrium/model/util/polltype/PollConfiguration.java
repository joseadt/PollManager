package es.udc.jadt.arbitrium.model.util.polltype;

import java.util.List;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.polltype.ConfigurationParameters;
import es.udc.jadt.arbitrium.model.entities.vote.Vote;
import es.udc.jadt.arbitrium.util.pollresult.PollResult;

public interface PollConfiguration {

	public PollResult getResult(Poll poll, List<Vote> votes);

	public ConfigurationParameters getConfigurationParameters();

}
