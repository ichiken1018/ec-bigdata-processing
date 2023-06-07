package com.example.domain;

/**
 * 1ページ目を表す列挙型
 * 
 * @author kenta_ichiyoshi
 *
 */
public enum Page {

	/** ページ定数 */
	FIRST_PAGE(1), ITEMS_PER_PAGE(30), PAGE_VALUE_REMAINDER(1),;

	/** 値 */
	private final int value;

	/**
	 * コンストラクタ
	 * 
	 * @param value 値
	 */
	private Page(final int value) {
		this.value = value;
	}

	/**
	 * 定数に含まれる値を返します.
	 * 
	 * @return 値
	 */
	public int getValue() {
		return value;
	}

}
