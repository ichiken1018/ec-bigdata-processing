package com.example.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.User;
import com.example.form.RegisterUserForm;
import com.example.service.RegisterUserService;

@Controller
@RequestMapping("/register-user")
public class RegisterUserController {

	@Autowired
	RegisterUserService service;

	@GetMapping("/toRegister")
	public String toRegister(Model model,RegisterUserForm form) {
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@Validated RegisterUserForm form, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {
		if(result.hasErrors()) {
			return toRegister(model,form);
		}
		
		User user = new User();
		BeanUtils.copyProperties(form, user);
		service.registerUser(user);
		return "redirect:/login-logout/toLogin";
	}
}
