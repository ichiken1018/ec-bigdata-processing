package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.example.domain.User;
@SpringBootTest
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private NamedParameterJdbcTemplate template;
	private User user;
	
	@BeforeEach
	@DisplayName("テストユーザー情報を登録")
	void setUp() throws Exception {
		user = new User();
		user.setName("test");
		user.setEmail("testUser@sample.com");
		user.setPassword("testtest");
		userRepository.insert(user);
		System.out.println("テスト用データを登録");
	}

	@AfterEach
	@DisplayName("テストユーザー情報を削除")
	void tearDown() throws Exception {
		delete("test");
		System.out.println("テスト用データを削除");
	}
	
	public void delete(String name) {
		String sql = "delete from users where name = :name";
		MapSqlParameterSource param = new MapSqlParameterSource("name",name);
		template.update(sql, param);
	}

	@Test
	@DisplayName("ユーザー登録を確認するテスト")
	void testInsert() {
		assertEquals("test",user.getName(),"nameが登録されていません");
		assertEquals("testUser@sample.com",user.getEmail(),"mailが登録されていません");
		assertEquals("testtest",user.getPassword(),"passが登録されていません");
	}
	
	@Test
	@DisplayName("メールとパスワードからユーザー情報を確認するテスト")	
	void testFindByMailAndPass() {
		user = userRepository.findByMailAndPassword(user.getEmail(),user.getPassword());
		assertEquals("test",user.getName(),"nameが登録されていません");
		assertEquals("testUser@sample.com",user.getEmail(),"mailが登録されていません");
		assertEquals("testtest",user.getPassword(),"passが登録されていません");
	}

	@Test
	@DisplayName("メールからユーザー情報を確認するテスト")	
	void testFindByMail() {
		user = userRepository.findByMail(user.getEmail());
		assertEquals("test",user.getName(),"nameが登録されていません");
		assertEquals("testUser@sample.com",user.getEmail(),"mailが登録されていません");
		assertEquals("testtest",user.getPassword(),"passが登録されていません");
	}

}
