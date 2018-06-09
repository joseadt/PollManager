package es.udc.jadt.arbitrium.controller.poll;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.service.poll.PollService;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.EndDateInThePastException;
import es.udc.jadt.arbitrium.model.service.user.UserService;
import es.udc.jadt.arbitrium.model.service.util.EntityNotFoundException;
import es.udc.jadt.arbitrium.support.web.Ajax;
import es.udc.jadt.arbitrium.support.web.MessageHelper;

@Controller
public class PollController {

    private static final String CREATE_POLL_VIEW = "poll/create";

	private static final String EDIT_POLL_VIEW = "poll/edit";


    @Autowired
    private PollService pollService;
    
	@Autowired
	private UserService userService;
    
    
    @GetMapping("createpoll")
    String createPoll(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
	model.addAttribute(new PollForm());
	if (Ajax.isAjaxRequest(requestedWith)) {
	    return CREATE_POLL_VIEW.concat(" :: pollForm");
	}
	
	return CREATE_POLL_VIEW;
    }

	@PostMapping("createpoll")
	String createPoll(HttpServletRequest request,@Valid @ModelAttribute PollForm pollForm, Errors errors, RedirectAttributes ra) {
		if (errors.hasErrors()) {
			return CREATE_POLL_VIEW;
		}
		Principal principal = request.getUserPrincipal();
		Poll poll = null;
		try {
			poll = pollService.createPoll(principal.getName(), pollForm.getPoll(), pollForm.getOptions());
		} catch (EndDateInThePastException e) {
			return "redirect:/error";
		}
		MessageHelper.addSuccessAttribute(ra, "createpoll.success");
		return "redirect:/editpoll/" + poll.getId();
	}

	@GetMapping("editpoll/{id}")
	String editPoll(HttpServletRequest request, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith, @PathVariable Long id) {


		Principal principal = request.getUserPrincipal();
		Poll poll;
		try {
			poll = pollService.findPollById(id);

		} catch (EntityNotFoundException e) {
			return "redirect:/error";
		}

		if (!principal.getName().equals(poll.getAuthor().getEmail())) {
			return "redirect:/error";
		}

		PollForm pollForm = new PollForm(poll);

		model.addAttribute(pollForm);

		return CREATE_POLL_VIEW;
	}
}
