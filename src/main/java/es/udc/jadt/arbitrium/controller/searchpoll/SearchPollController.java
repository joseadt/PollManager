package es.udc.jadt.arbitrium.controller.searchpoll;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.support.web.Ajax;

@Controller
public class SearchPollController {

	private static final String VIEW_NAME = "search/findpoll";

	private List<Poll> getPolls(String keywords) {
		List<Poll> pollList = new ArrayList<Poll>();

		for (int i = 0; i < 5; i++) {
			Poll poll = new Poll();
			poll.setName("Poll " + i);
			poll.setId(Long.valueOf(i));
			pollList.add(poll);

		}

		return pollList;
	}

	@GetMapping("searchPoll")
	String searchPoll(Model model, @RequestParam(required = false) String keywords,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		String returnedView = VIEW_NAME;
		if (Ajax.isAjaxRequest(requestedWith)) {
			returnedView = returnedView.concat(" :: pollSearchBody");
		}
		
		model.addAttribute("pollList", getPolls(keywords));


		return returnedView;
	}
}
