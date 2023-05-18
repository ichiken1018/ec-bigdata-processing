package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.User;

/**
 * usersテーブルを操作するリポジトリ.
 * 
 * @author kenta_ichiyoshi
 *
 */
@Repository
public class UserRepository {
	@Autowired
	NamedParameterJdbcTemplate template;

	private static final RowMapper<User> USER_ROW_MAPPER = (rs, i) -> {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setMailAddress(rs.getString("mail_address"));
		user.setPassword(rs.getString("password"));
		return user;
	};

	/**
	 * ユーザー情報を挿入する.
	 * 
	 * @param user ユーザー情報
	 */
	public void insert(User user) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO users(name,mail_address,password)");
		sql.append(" VALUES(:name,:mailAddress,:password);");

		SqlParameterSource param = new BeanPropertySqlParameterSource(user);

		template.update(sql.toString(), param);

	}

	/**
	 * メールとパスワードからユーザー情報を取得する.
	 * 
	 * @param mailAddress メールアドレス
	 * @param password    パスワード
	 * @return 検索されたユーザー情報
	 */
	public User findByMailAddressAndPassword(String mailAddress, String password) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,mail_address,password FROM users");
		sql.append(" WHERE mail_address = :mailAddress AND password = :password;");

		SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", mailAddress).addValue("password",
				password);

		List<User> userList = template.query(sql.toString(), param, USER_ROW_MAPPER);

		if (userList.size() == 0) {
			return null;
		}
		return userList.get(0);

	}
	public User findByMailAddress(String mailAddress) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,mail_address,password FROM users");
		sql.append(" WHERE mail_address = :mailAddress;");
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", mailAddress);
		
		List<User> userList = template.query(sql.toString(), param, USER_ROW_MAPPER);
		
		if (userList.size() == 0) {
			return null;
		}
		return userList.get(0);
		
	}

}
