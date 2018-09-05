package es.udc.jadt.arbitrium.controller.discussion;

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

import es.udc.jadt.arbitrium.model.entities.discussion.Discussion;
import es.udc.jadt.arbitrium.model.service.discussion.DiscussionService;
import es.udc.jadt.arbitrium.model.service.group.GroupService;
import es.udc.jadt.arbitrium.model.service.user.UserService;
import es.udc.jadt.arbitrium.model.service.util.EntityNotFoundException;
import es.udc.jadt.arbitrium.support.web.Ajax;
import es.udc.jadt.arbitrium.util.exceptions.UserWithoutPermisionException;

@Controller
public class DiscussionController {

	@Autowired
	private GroupService groupService;

	@Autowired
	private UserService userService;

	@Autowired
	private DiscussionService discussionService;

	private static final String CREATE_VIEW = "discussion/create";

	private static final String SHOW_VIEW = "discussion/show";

	@GetMapping("discussion/create")
	String getCreateView(HttpServletRequest request, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

		Principal principal = request.getUserPrincipal();

		model.addAttribute("userGroups", this.groupService.findGroupsByUser(principal.getName()));
		model.addAttribute("discussionForm", new DiscussionForm());

		if(Ajax.isAjaxRequest(requestedWith)) {
			return CREATE_VIEW.concat(" :: discussionFormFragment");
		}
		return CREATE_VIEW;
	}

	@PostMapping("discussion/create")
	String createDiscussion(HttpServletRequest request, @Valid @ModelAttribute DiscussionForm form, Errors errors,
			RedirectAttributes ra) {

		Principal principal = request.getUserPrincipal();
		Discussion discussion = null;
		try {
			discussion = this.discussionService.createDiscussion(form.getTitle(), form.getDescription(),
					principal.getName(),
					form.getGroupId());
		} catch (EntityNotFoundException | UserWithoutPermisionException e) {
			return "redirect:/error";
		}

		return "redirect:/discussion/show/" + discussion.getId();
	}

	@GetMapping("discussion/show/{id}")
	String showDiscussion(HttpServletRequest request, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith, @PathVariable Long id) {

		Discussion discussion = null;

		try {
			discussion = this.discussionService.findDiscussion(id);
		} catch (EntityNotFoundException e) {
			return "redirect:/error";
		}

		model.addAttribute(discussion);
		model.addAttribute("commentList", discussion.getComments());
		model.addAttribute("commentForm", new CommentForm(id));

		if (Ajax.isAjaxRequest(requestedWith)) {
			return SHOW_VIEW.concat(" :: discussionFragment");
		}

		return SHOW_VIEW;
	}

	@PostMapping("discussion/addComment")
	String addComment(HttpServletRequest request, @ModelAttribute("commentForm") CommentForm commentForm) {
		Principal principal = request.getUserPrincipal();

		try {
			this.discussionService.addComment(commentForm.discussionId, commentForm.content, principal.getName());
		} catch (EntityNotFoundException e) {
			return "redirect:/error";
		}

		return "redirect:/discussion/show/" + commentForm.getDiscussionId();
	}

}
