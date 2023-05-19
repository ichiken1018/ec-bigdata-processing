package com.example.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
//		http.authorizeHttpRequests().requestMatchers("/**").permitAll()
		//追加・編集機能以外は全ユーザーが利用できる.
		http.authorizeHttpRequests().requestMatchers("/list**","/list/**","/detail**","/register-user**"
				,"/register-user/**","/pick-up-category-list","/pick-up-category-list/**","/login-user","/login-user/**").permitAll() //一覧、詳細
		.anyRequest().authenticated(); //それ以外のパスは認証が必要.
		//ログインに関する設定
		http.formLogin().loginPage("/login-user").loginProcessingUrl("/login-user/login").failureUrl("/login-user?error=true").defaultSuccessUrl("/list",false)
		.usernameParameter("email").passwordParameter("password");
		
		//ログアウトに関する設定
		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/login-user/logout")).logoutSuccessUrl("/login-user")
		.deleteCookies("JSESSIONID").invalidateHttpSession(true);
		
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
