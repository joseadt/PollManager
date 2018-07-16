package es.udc.jadt.arbitrium.controller.group;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import es.udc.jadt.arbitrium.model.entities.group.UserGroup;
import es.udc.jadt.arbitrium.model.service.group.GroupService;
import es.udc.jadt.arbitrium.model.service.util.EntityNotFoundException;
import es.udc.jadt.arbitrium.model.service.util.exceptions.UserAlreadyInGroupException;

@Controller
public class GroupController {

	private static final String GROUP_VIEW = "group/show";

	@Autowired
	private GroupService groupService;

	@GetMapping("group/{id}")
	public String shwoGroup(Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith, @PathVariable Long id) {
		try {
			UserGroup group = this.groupService.findGroupById(id);
			model.addAttribute("userGroup", group);
			model.addAttribute("groupName", group.getName());
			model.addAttribute("polls", group.getPolls());
			model.addAttribute("members", group.getMembers());
		} catch (EntityNotFoundException e) {
			return "redirect:/error";
		}

		return GROUP_VIEW;
	}

	@PostMapping("joingroup/{id}")
	public String joinGroup(HttpServletRequest request, @PathVariable Long id) {
		Principal principal = request.getUserPrincipal();

		try {
			this.groupService.joinGroup(id, principal.getName());
		} catch (EntityNotFoundException | UserAlreadyInGroupException e) {
			return "redirect:/error";
		}

		return "redirect:/group/" + id.toString();
	}
}
