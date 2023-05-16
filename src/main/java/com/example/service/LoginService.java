package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.User;
import com.example.repository.UserRepository;

@Service
@Transactional
public class LoginService {

	@Autowired
	UserRepository repository;
	
	public User login(String mailAddress,String password) {
		User user = repository.findByMailAddressAndPassword(mailAddress, password);
		return user;
	}
}
