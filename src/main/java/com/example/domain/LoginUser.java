package com.example.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

/**
 * ログイン認証のドメイン.
 * 
 * @author kenta_ichiyoshi
 *
 */
public class LoginUser extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;
	/** 管理者情報 */
	private final User user;

	public LoginUser(User user, Collection<GrantedAuthority> authorityList) {

		super(user.getEmail(), user.getPassword(), authorityList);
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}