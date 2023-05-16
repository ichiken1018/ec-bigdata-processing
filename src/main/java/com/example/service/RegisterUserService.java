package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.User;
import com.example.repository.UserRepository;

/**
 * ユーザー登録を操作するサービス.
 * 
 * @author kenta_ichiyoshi
 *
 */
@Service
@Transactional
public class RegisterUserService {

	@Autowired
	UserRepository repository;
	
	/**
	 * ユーザー登録をする.
	 * 
	 * @param user ユーザー情報
	 */
	public void registerUser(User user) {
		repository.insert(user);
	}
}
