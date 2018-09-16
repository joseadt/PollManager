package es.udc.jadt.arbitrium.controller.group;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import es.udc.jadt.arbitrium.model.entities.group.UserGroup;
import es.udc.jadt.arbitrium.model.service.group.GroupService;
import es.udc.jadt.arbitrium.model.service.util.exceptions.EntityNotFoundException;
import es.udc.jadt.arbitrium.support.web.Ajax;

/**
 * Spring MVC controller for mapping paths that manage the creations of groups
 *
 * @author JADT
 */
@Controller
public class CreateGroupController {

	private static final String CREATE_VIEW = "group/create";

	@Autowired
	private GroupService groupService;

	@GetMapping("group/create")
	public String createGroup(Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

		model.addAttribute("groupForm", new GroupForm());

		if (Ajax.isAjaxRequest(requestedWith)) {
			return CREATE_VIEW.concat(" :: groupFormFragment");
		}
		return CREATE_VIEW;
	}

	@PostMapping("group/create")
	public String createGroup(HttpServletRequest request, @ModelAttribute GroupForm groupForm)
			throws EntityNotFoundException {
		Principal principal = request.getUserPrincipal();

		UserGroup group = this.groupService.createGroup(groupForm.getGroupName(), principal.getName(),
				groupForm.isPrivate());

		return "redirect:/group/" + group.getId();
	}
}
