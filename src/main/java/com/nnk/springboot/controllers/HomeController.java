
package com.nnk.springboot.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home(Authentication authentication) {

		if (authentication != null && authentication.isAuthenticated()) {
			return "redirect:/bidList/list";
		}

		return "redirect:/login";
	}

	@GetMapping("/admin/home")
	public String adminHome() {
		return "redirect:/bidList/list";
	}
}

