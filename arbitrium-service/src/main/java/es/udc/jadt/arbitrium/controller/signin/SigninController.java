package es.udc.jadt.arbitrium.controller.signin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SigninController {

	private static final String VIEW_NAME = "signin/signin";

	@GetMapping("signin")
	public String signin() {
		// TODO apply ajax
		return VIEW_NAME;
		
	}

}
