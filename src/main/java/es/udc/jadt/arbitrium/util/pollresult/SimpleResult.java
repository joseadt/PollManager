package es.udc.jadt.arbitrium.util.pollresult;

import java.util.Map;
import java.util.Map.Entry;

import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;

public class SimpleResult implements PollResult {


	private PollOption winnerOption;

	private Map<PollOption, Integer> results;


	public SimpleResult(Map<PollOption, Integer> mapCounter) {
		this.results = mapCounter;
		this.winnerOption = null;
		Integer currentMax = 0;
		for (Entry<PollOption, Integer> entry : this.results.entrySet()) {
			if (currentMax.compareTo(entry.getValue()) < 0) {
				currentMax = entry.getValue();
				this.winnerOption = entry.getKey();
			} else if (currentMax.compareTo(entry.getValue()) == 0) {
				this.winnerOption = null;
			}
		}
	}


	public void setResults(Map<PollOption, Integer> results) {
		this.results = results;
	}



	@Override
	public Map<PollOption, Integer> getResult() {
		return this.results;
	}

	@Override
	public PollOption getWinnerOption() {
		return this.winnerOption;
	}

	@Override
	public Integer getWinnerOptionNumberOfVotes() {
		return this.results.get(this.winnerOption);
	}

}
