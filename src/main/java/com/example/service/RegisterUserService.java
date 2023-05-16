package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.User;
import com.example.repository.UserRepository;

@Service
@Transactional
public class RegisterUserService {

	@Autowired
	UserRepository repository;
	
	public void registerUser(User user) {
		repository.insert(user);
	}
}
