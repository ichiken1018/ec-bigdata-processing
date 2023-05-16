package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.User;
import com.example.form.LoginForm;
import com.example.service.LoginService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login-logout")
public class LoginLogoutController {

	@Autowired
	private LoginService service;

	@GetMapping("/toLogin")
	public String toLogin(LoginForm form) {
		return "login";
	}

	@Autowired
	private HttpSession session;

	@PostMapping("/login")
	public String login(LoginForm form, Model model) {
		User loginResult = service.login(form.getMailAddress(), form.getPassword());
		if (loginResult == null) {
			model.addAttribute("errorMessage", "fail to login");
			return toLogin(form);
		}
		session.setAttribute("userName", loginResult.getName());
		return "redirect:/";
	}

	@GetMapping("/logout")
	public String logout(LoginForm form) {
		session.invalidate();
		return "redirect:/login-logout/toLogin";
	}

}
