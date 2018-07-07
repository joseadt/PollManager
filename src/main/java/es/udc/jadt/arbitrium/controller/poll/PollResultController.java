package es.udc.jadt.arbitrium.controller.poll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.vote.Vote;
import es.udc.jadt.arbitrium.model.service.poll.PollService;
import es.udc.jadt.arbitrium.model.service.util.EntityNotFoundException;
import es.udc.jadt.arbitrium.model.service.vote.VoteService;
import es.udc.jadt.arbitrium.util.generics.Pair;
import es.udc.jadt.arbitrium.util.pollresult.PollResult;

@Controller
public class PollResultController {

	private static final String RESULT_VIEW_PAGE = "poll/results";

	@Autowired
	private PollService pollService;

	@Autowired
	private VoteService voteService;

	@GetMapping("result/{id}")
	String viewResults(Model model, @PathVariable Long id) {

		model.addAttribute("pollId", id);
		try {
			Pair<Poll, List<Vote>> pair = this.pollService.getPollVotes(id);
			Poll poll = pair.getFirst();
			PollResult results = poll.getPollType().getConfiguration().getResult(poll, pair.getSecond());
			model.addAttribute(poll);
			model.addAttribute("winnerOption", results.getWinnerOption());
			model.addAttribute("winnerOptionVotes", results.getWinnerOptionNumberOfVotes());
			model.addAttribute("resultMap", results.getResult());
		} catch (EntityNotFoundException e) {
			model.addAttribute("notFound", Boolean.TRUE);
		}


		return RESULT_VIEW_PAGE;
	}
}
