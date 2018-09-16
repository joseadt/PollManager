package es.udc.jadt.arbitrium.controller.vote;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.udc.jadt.arbitrium.model.entities.vote.Vote;
import es.udc.jadt.arbitrium.model.service.util.exceptions.EntityNotFoundException;
import es.udc.jadt.arbitrium.model.service.vote.VoteService;

@Controller
public class VoteController {

	private static final String VOTE_VIEW = "vote/showvote";

	@Autowired
	private VoteService voteService;

	@PostMapping("vote")
	String doVote(HttpServletRequest request, @RequestParam("optSelection") List<Long> optionsIds,
			@RequestParam("pollId") Long pollId, @RequestParam("voteComment") String comment) {

		Vote vote = null;
		try {
			vote = this.voteService.createVote(request.getUserPrincipal().getName(), pollId, optionsIds, comment);
		} catch (EntityNotFoundException e) {
			return "redirect:/error";
		}

		return "redirect:/vote/" + vote.getId();
	}

	@GetMapping("vote/{id}")
	String getVote(HttpServletRequest request, Model model, @PathVariable Long id) {

		try {
			String email = request.getUserPrincipal().getName();
			Vote vote = this.voteService.findByIdAndEmail(id, email);
			model.addAttribute(vote);
		} catch (EntityNotFoundException e) {
			model.addAttribute("notFound", true);
		} catch (Exception e) {
			model.addAttribute("otherError", true);
		}
		return VOTE_VIEW;
	}

}
