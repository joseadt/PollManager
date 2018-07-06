package es.udc.jadt.arbitrium.util.votecount;

import java.util.List;
import java.util.Map;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.pollconfig.ConfigurationParameters;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.vote.Vote;

public interface PollConfiguration {


	public Map<PollOption,Integer> getResult(Poll poll, List<Vote> votes);

	public ConfigurationParameters fullConfiguration();

}
