package com.example.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	/**
	 * 静的リソースに対してセキュリティの設定を無効にする.
	 * 
	 */
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**");
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// 下記の設定は全てのパスをログインしなくてもアクセスできるようにする設定(全リンク利用可で設定)
		http.authorizeHttpRequests().requestMatchers("/**").permitAll();
		return http.build();
	}
	
	/**
	 * <pre>
	 * bcryptアルゴリズムでハッシュ化する実装を返します.
	 * これを指定することでパスワード暗号化やマッチ確認する際に
	 * @Autowired
	 * private PasswordEncoder passwordEncoder;
	 * と記載するとDIされるようになります。
	 * </pre>
	 * 
	 * @return bcryptアルゴリズムで暗号化する実装オブジェクト
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
