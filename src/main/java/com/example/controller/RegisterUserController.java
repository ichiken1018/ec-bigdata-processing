package com.example.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.User;
import com.example.form.RegisterUserForm;
import com.example.service.RegisterUserService;

/**
 * ユーザー登録を操作するコントローラ.
 * 
 * @author kenta_ichiyoshi
 *
 */
@Controller
@RequestMapping("/register-user")
public class RegisterUserController {

	@Autowired
	RegisterUserService service;
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * ユーザー登録画面に遷移する.
	 * 
	 * @param model モデル
	 * @param form  フォーム
	 * @return ユーザー登録画面
	 */
	@GetMapping("/toRegister")
	public String toRegister(Model model, RegisterUserForm form) {
		return "register";
	}

	/**
	 * ユーザー登録する.
	 * 
	 * @param form   フォーム
	 * @param result バリデーション結果
	 * @param model  モデル
	 * @return
	 */
	@PostMapping("/register")
	public String registerUser(@Validated RegisterUserForm form, BindingResult result, Model model) {
		// エラーがあればユーザー登録画面に遷移
		if (result.hasErrors()) {
			return toRegister(model, form);
		}

		User user = new User();
		BeanUtils.copyProperties(form, user);
		user.setPassword(passwordEncoder.encode(form.getPassword()));
		service.registerUser(user);
		return "redirect:/login-user";
	}
}
