package es.udc.jadt.arbitrium.controller.vote;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.udc.jadt.arbitrium.model.service.util.EntityNotFoundException;
import es.udc.jadt.arbitrium.model.service.vote.VoteService;

@Controller
public class VoteController {


	@Autowired
	private VoteService voteService;

	@PostMapping("vote")
	String doVote(HttpServletRequest request, @RequestParam("optSelection") List<Long> optionsIds,
			@RequestParam("pollId") Long pollId) {


		try {
			this.voteService.createVote(request.getUserPrincipal().getName(), pollId, optionsIds);
		} catch (EntityNotFoundException e) {
			return "redirect:/error";
		}

		return "redirect:/";
	}

}
