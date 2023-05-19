package com.example.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.domain.LoginUser;
import com.example.domain.User;
import com.example.repository.UserRepository;
/**
 * ログイン後の管理者情報に権限情報を付与するサービスクラス.
 * 
 * @author kenta_ichiyoshi
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	/**DBから情報を得るためのリポジトリ */
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByMail(email);
		//System.out.println("mailAddress:" + email);
		if (user == null) {
			throw new UsernameNotFoundException("this mailAddress　is not registered");
		}
		// 権限付与の例
		Collection<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_USER")); // ユーザ権限付与
		//System.out.println("管理者：" + authorityList);
		return new LoginUser(user, authorityList);
	}
}
