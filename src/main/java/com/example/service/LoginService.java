package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.User;
import com.example.repository.UserRepository;

/**
 * ログイン処理を行うサービス.
 * 
 * @author kenta_ichiyoshi
 *
 */
@Service
@Transactional
public class LoginService {

	@Autowired
	UserRepository repository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	/**
	 * ログインする.
	 * 
	 * @param mailAddress メールアドレス
	 * @param password　パスワード
	 * @return　ログイン者情報
	 */
	public User login(String mailAddress,String password) {
		User user = repository.findByMailAddress(mailAddress);
		if(user == null) {
			return null;
		}
		if(!(passwordEncoder.matches(password, user.getPassword()))) {
			return null;			
		}
		return user;
		
	}
}
