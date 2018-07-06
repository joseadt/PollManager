package es.udc.jadt.arbitrium.util.votecount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.pollconfig.ConfigurationParameters;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.vote.Vote;



public class ProposalConfiguration implements PollConfiguration {


	private static ProposalConfiguration instance = null;

	private static final String YES_OPTION_STRING = "YES";

	private static final String NO_OPTION_STRING = "NO";

	private final List<PollOption> defaultOptions;


	private ProposalConfiguration() {
		this.defaultOptions = new ArrayList<PollOption>();

		PollOption yesOption = new PollOption();
		yesOption.setDescription(YES_OPTION_STRING);
		this.defaultOptions.add(yesOption);

		PollOption noOption = new PollOption();
		noOption.setDescription(NO_OPTION_STRING);
		this.defaultOptions.add(noOption);

	}

	@Override
	public Map<PollOption,Integer> getResult(Poll poll, List<Vote> votes) {
		if (votes == null) {
			return null;
		}

		int yesCounter = 0;
		int noCounter = 0;
		List<Vote> nullVotes = new ArrayList<Vote>();

		for (Vote vote : votes) {
			PollOption votedOption = (vote.getSelectedOptions() != null && vote.getSelectedOptions().size() == 1)
					? vote.getSelectedOptions().get(0)
					: null;

			if (votedOption == null || !this.defaultOptions.contains(votedOption) || !votedOption.getPoll().equals(poll)) {
				nullVotes.add(vote);
				continue;
			}

			if (votedOption.getDescription().equals(YES_OPTION_STRING)) {
				yesCounter++;
			} else {
				noCounter++;
			}

		}
		Map<PollOption, Integer> results = new HashMap<PollOption, Integer>();
		return results;
	}

	@Override
	public ConfigurationParameters fullConfiguration() {
		ConfigurationParameters cp = new ConfigurationParameters();

		cp.setDefaultOptions(this.defaultOptions);

		return cp;
	}

	public static PollConfiguration getInstance() {
		if (instance == null) {
			instance = new ProposalConfiguration();
		}

		return instance;
	}




}
