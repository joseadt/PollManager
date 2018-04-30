package es.udc.jadt.reupoll.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

	@RequestMapping(method=RequestMethod.GET, value="/")
	public String test(HttpServletResponse response) {
		return "home/homeNotSignedIn.html";
	}
}
