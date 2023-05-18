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

/**
 * ログイン、ログアウト処理を行うコントローラ.
 * 
 * @author kenta_ichiyoshi
 *
 */
@Controller
@RequestMapping("/login-logout")
public class LoginLogoutController {

	@Autowired
	private LoginService service;

	/**
	 *ログイン画面へ遷移する. 
	 * @param form フォーム
	 * @return　ログイン画面
	 */
	@GetMapping("/toLogin")
	public String toLogin(LoginForm form) {
		return "login";
	}

	@Autowired
	private HttpSession session;

	/**
	 * ログインする.
	 * 
	 * @param form フォーム
	 * @param model　モデル
	 * @return　商品一覧画面に遷移する
	 */
	@PostMapping("/login")
	public String login(LoginForm form, Model model) {
		User loginResult = service.login(form.getMailAddress(), form.getPassword());
		if (loginResult == null) {
			model.addAttribute("errorMessage", "fail to login");
			return toLogin(form);
		}
		session.setAttribute("userName", loginResult.getName());
		return "redirect:/list";
	}

	/**
	 * ログアウトする.
	 * @param form フォーム
	 * @return　ログイン画面に遷移する.
	 */
	@GetMapping("/logout")
	public String logout(LoginForm form) {
		session.invalidate();
		return "redirect:/login-logout/toLogin";
	}

}
