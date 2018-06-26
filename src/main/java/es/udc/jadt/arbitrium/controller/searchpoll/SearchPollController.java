package es.udc.jadt.arbitrium.controller.searchpoll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.service.poll.PollService;
import es.udc.jadt.arbitrium.support.web.Ajax;

@Controller
public class SearchPollController {

	private static final String VIEW_NAME = "search/findpoll";

	@Autowired
	private PollService pollService;

	@GetMapping("searchPoll")
	String searchPoll(Model model, @RequestParam(required = false) String keywords,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		String returnedView = VIEW_NAME;
		if (Ajax.isAjaxRequest(requestedWith)) {
			returnedView = returnedView.concat(" :: pollSearchBody");
		}
		
		List<Poll> pollList = pollService.findByKeywords((keywords != null) ? keywords : "", false);
		model.addAttribute("pollList", pollList);


		return returnedView;
	}
}
