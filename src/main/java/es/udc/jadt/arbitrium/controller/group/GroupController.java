package es.udc.jadt.arbitrium.controller.group;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import es.udc.jadt.arbitrium.model.entities.group.UserGroup;
import es.udc.jadt.arbitrium.model.service.group.GroupService;
import es.udc.jadt.arbitrium.model.service.util.EntityNotFoundException;
import es.udc.jadt.arbitrium.model.service.util.exceptions.UserAlreadyInGroupException;
import es.udc.jadt.arbitrium.support.web.Ajax;

@Controller
public class GroupController {

	private static final String GROUP_VIEW = "group/show";

	private static final String SEARCH_GROUPS_VIEW = "group/search";

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

	@GetMapping("group/search")
	public String searchGroups(Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
			@RequestParam(value = "keywords", required = false) String keywords,
			@RequestParam(value = "page", required = false) Integer page) {
		String returned = SEARCH_GROUPS_VIEW;
		if (Ajax.isAjaxRequest(requestedWith)) {
			returned = SEARCH_GROUPS_VIEW.concat(" :: groupListFragment");
		}

		Page<UserGroup> groups = this.groupService.searchGroups((page != null && page > 0) ? page : 0, keywords);

		model.addAttribute("groupList", groups.getContent());
		model.addAttribute("hasNext", groups.hasNext());
		model.addAttribute("hasPrevious", groups.hasPrevious());

		return returned;
	}
}
