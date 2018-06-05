package es.udc.jadt.arbitrium.controller.poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import es.udc.jadt.arbitrium.service.pollservice.PollService;
import es.udc.jadt.arbitrium.support.web.Ajax;

@Controller
public class PollController {

    private static final String CREATE_POLL_VIEW = "poll/create";

    @Autowired
    private PollService pollService;
    
    
    
    @GetMapping("createpoll")
    String createPoll(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
	model.addAttribute(new PollForm());
	if (Ajax.isAjaxRequest(requestedWith)) {
	    return CREATE_POLL_VIEW.concat(" :: pollForm");
	}
	
	return CREATE_POLL_VIEW;
    }

}
