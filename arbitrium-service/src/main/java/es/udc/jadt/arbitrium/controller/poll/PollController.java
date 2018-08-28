package es.udc.jadt.arbitrium.controller.poll;

import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.service.poll.PollService;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.EndDateInThePastException;
import es.udc.jadt.arbitrium.model.service.poll.exceptions.UserIsNotTheAuthorException;
import es.udc.jadt.arbitrium.model.service.user.UserService;
import es.udc.jadt.arbitrium.model.service.util.EntityNotFoundException;
import es.udc.jadt.arbitrium.model.util.polltype.PollType;
import es.udc.jadt.arbitrium.support.web.Ajax;
import es.udc.jadt.arbitrium.support.web.MessageHelper;

@Controller
public class PollController {

    private static final String CREATE_POLL_VIEW = "poll/create";

	private static final String EDIT_POLL_VIEW = "poll/edit";

	private static final String SEE_POLL_VIEW = "poll/view";

	private static final String NOT_FOUND_VIEW = "error/notfound";

	private static final String POLL_CONFIG_VIEW = "poll/fragments/pollConfigs";

	private static final String CONFIG_FRAGMENT_SUFFIX = "Config";

    @Autowired
    private PollService pollService;

	@Autowired
	private UserService userService;


    @GetMapping("createpoll")
    String createPoll(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		model.addAttribute(new PollForm());
		model.addAttribute("configFragment", getPollTypeFragment(PollType.values()[0]));
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
			poll = this.pollService.createPoll(principal.getName(), pollForm.getPoll());
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
			poll = this.pollService.findPollById(id);

		} catch (EntityNotFoundException e) {
			return "redirect:/error";
		}

		if (!principal.getName().equals(poll.getAuthor().getEmail())) {
			return "redirect:/error";
		}
		model.addAttribute("id", id);
		PollForm pollForm = new PollForm(poll);

		model.addAttribute(pollForm);

		return EDIT_POLL_VIEW;
	}

	@PostMapping("editpoll/{id}")
	String editPoll(HttpServletRequest request, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
			@ModelAttribute PollForm pollForm, @PathVariable Long id)
			throws EntityNotFoundException, UserIsNotTheAuthorException {

		Principal principal = request.getUserPrincipal();
		Poll poll;
		try {
			poll = this.pollService.findPollById(id);

		} catch (EntityNotFoundException e) {
			return "redirect:/error";
		}



		poll.setName(pollForm.getTitle());
		poll.setDescription(pollForm.getDescription());
		poll.setEndDate(Instant.ofEpochMilli(pollForm.getEndDate().getTime()));
		poll.setPollType(pollForm.getPollType());
		List<PollOption> options = new ArrayList<PollOption>();



		poll.getOptions().addAll(options);

		this.pollService.savePoll(poll, principal.getName());

		return "redirect:/editpoll/" + id;

	}

	@GetMapping("poll/{id}")
	String viewPoll(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
			@PathVariable Long id) {
		Poll poll = null;

		try {
			 poll= this.pollService.findPollById(id);
		} catch (EntityNotFoundException e) {

			// TODO: MODEL BLOCK TO RETURN SPECIFIC DATA ABOUT THE ERROR (ENTITY CLASS, ID,
			// PATH, ETC)
			return Ajax.isAjaxRequest(requestedWith) ? NOT_FOUND_VIEW.concat(" :: notFoundFragment") : NOT_FOUND_VIEW;

		}

		model.addAttribute(poll);

		if(Ajax.isAjaxRequest(requestedWith)) {
			return SEE_POLL_VIEW.concat(" :: pollFragment");
		}

		return SEE_POLL_VIEW;
	}

	@GetMapping("pollconfig/{pollType}")
	String getConfig(Model model, @PathVariable String pollType) {
		PollType type = PollType.valueOf(pollType);
		String pollConfigFragmentName = getPollTypeFragment(type);
		model.addAttribute(new PollForm());
		return POLL_CONFIG_VIEW.concat(" :: ").concat(pollConfigFragmentName);
	}

	private String getPollTypeFragment(PollType pollType) {

		return pollType.getName().toLowerCase().concat(CONFIG_FRAGMENT_SUFFIX);
	}
}
