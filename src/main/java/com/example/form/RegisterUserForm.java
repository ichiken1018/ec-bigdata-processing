package com.example.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterUserForm {

	/** ユーザー名 */
	@NotBlank(message = "Please enter name")
	private String name;
	/** メールアドレス */
	@NotBlank(message = "Please enter mailaddress")
	@Email(message="Please enter in email format")
	private String mailAddress;
	/** パスワード */
	@NotBlank(message = "Please enter password")
	@Size(message="Please set password betweed 8 and 16 characters")
	private String password;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "RegisterUserForm [name=" + name + ", mailAddress=" + mailAddress + ", password=" + password + "]";
	}

}
