package es.udc.jadt.arbitrium.util.pollresult;

import java.util.Map;

import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;

public class SimpleResult implements PollResult {


	private Map<PollOption, Integer> results;


	public SimpleResult(Map<PollOption, Integer> mapCounter) {
		this.results = mapCounter;
	}


	public void setResults(Map<PollOption, Integer> results) {
		this.results = results;
	}


	@Override
	public Map<PollOption, Integer> getResult() {
		return this.results;
	}

}
