package com.example.domain;

/**
 * カテゴリー階層を表す列挙型
 * 
 * @author kenta_ichiyoshi
 *
 */
public enum CategoryDepth {

	/** カテゴリー定数 */
	PARENTCATEGORY(0), CHILDCATEGORY(1), GRANDCHILDCATEGORY(2),;

	/** 階層値 */
	private final int depth;

	/**
	 * コンストラクタ
	 * 
	 * @param depth 階層
	 */
	private CategoryDepth(final int depth) {
		this.depth = depth;
	}

	/**
	 * 定数に含まれる階層値を返します.
	 * 
	 * @return 階層値
	 */
	public int getDepth() {
		return depth;
	}

}
