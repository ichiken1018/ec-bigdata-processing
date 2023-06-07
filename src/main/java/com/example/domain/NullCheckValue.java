package com.example.domain;

/**
 * カテゴリーnull値を表す列挙型
 * 
 * @author kenta_ichiyoshi
 *
 */
public enum NullCheckValue {

	/** nullカテゴリー定数 */
	NULLCATEGORY(-1),;

	/** nullカテゴリーid値 */
	private final int nullCategoryId;

	/**
	 * コンストラクタ
	 * 
	 * @param nullCategoryId nullカテゴリーid値
	 */
	private NullCheckValue(final int nullCategoryId) {
		this.nullCategoryId = nullCategoryId;
	}

	/**
	 * 定数に含まれるnullカテゴリーid値を返します.
	 * 
	 * @return nullカテゴリーid値
	 */
	public int getNullCategoryIDValue() {
		return nullCategoryId;
	}

}
