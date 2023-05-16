package com.example.form;

public class LoginForm {

	/** ログインメールアドレス */
	private String mailAddress;
	/** ログインパスワード */
	private String password;

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
		return "LoginLogoutForm [mailAddress=" + mailAddress + ", password=" + password + "]";
	}

}
