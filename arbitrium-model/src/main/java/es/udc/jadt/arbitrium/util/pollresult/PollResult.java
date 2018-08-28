/*
 *
 */
package es.udc.jadt.arbitrium.util.pollresult;

import java.util.Map;

import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;

public interface PollResult {

	/**
	 * Gets the result.
	 *
	 * @return the result as map
	 */
	public Map<PollOption, Integer> getResult();


	/**
	 * Gets the winner option.
	 *
	 * @return the winner option , or null if , for exmaple, a tie
	 */
	public PollOption getWinnerOption();


	/**
	 * Gets the number of votes of the winner option
	 *
	 * @return the winner option number of votes
	 */
	public Integer getWinnerOptionNumberOfVotes();

}
