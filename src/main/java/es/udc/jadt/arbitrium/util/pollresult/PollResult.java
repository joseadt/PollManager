package es.udc.jadt.arbitrium.util.pollresult;

import java.util.Map;

import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;

public interface PollResult {

	Map<PollOption, Integer> getResult();
}
