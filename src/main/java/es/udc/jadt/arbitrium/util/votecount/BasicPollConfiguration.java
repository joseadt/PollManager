package es.udc.jadt.arbitrium.util.votecount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.pollconfig.ConfigurationParameters;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.vote.Vote;

public class BasicPollConfiguration implements PollConfiguration {

	private static BasicPollConfiguration instance;

	private BasicPollConfiguration() {

	}

	public static BasicPollConfiguration getInstance() {
		if (instance == null) {
			instance = new BasicPollConfiguration();
		}

		return instance;
	}

	@Override
	public Map<PollOption,Integer> getResult(Poll poll, List<Vote> votes) {
		if (poll == null) {
			return null;
		}

		List<PollOption> options = poll.getOptions();

		Map<PollOption, Integer> voteCounter = new HashMap<PollOption, Integer>(options.size());

		for (PollOption option : options) {
			voteCounter.put(option, Integer.valueOf(0));
		}

		if (votes != null && !votes.isEmpty()) {
			for (Vote vote : votes) {
				List<PollOption> votedOptions = vote.getSelectedOptions();
				if (votedOptions.size() > 1 && !Boolean.TRUE.equals(poll.getConfiguration().getIsMultiSelection())) {
					continue;
				}

				for (PollOption option : votedOptions) {
					if (voteCounter.containsKey(option)) {
						Integer value = voteCounter.get(option);
						voteCounter.put(option, Integer.valueOf(value + 1));
					}
				}
			}
		}


		return voteCounter;
	}

	@Override
	public ConfigurationParameters fullConfiguration() {
		ConfigurationParameters cp = new ConfigurationParameters();

		cp.setIsUserDefinedOptions(Boolean.TRUE);

		return cp;
	}

}
