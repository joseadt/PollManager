package es.udc.jadt.arbitrium.util.pollresult;

import java.util.Map;
import java.util.Map.Entry;

import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;

public class SimpleResult implements PollResult {

	private String result;

	public SimpleResult(String msg) {
		this.result = msg;
	}

	public SimpleResult(Map<PollOption, Integer> mapCounter) {
		StringBuilder sb = new StringBuilder();
		for (Entry<PollOption, Integer> entry : mapCounter.entrySet()) {
			sb.append(entry.getKey().getDescription()).append(" : ").append(entry.getValue().toString()).append("\n");
		}

		this.result = sb.toString();
	}

}
