package com.example.domain;

/**
 * カテゴリーのドメイン.
 * 
 * @author kenta_ichiyoshi
 *
 */
public class Category {
	/** カテゴリID */
	private Integer id;
	/** 親カテゴリID 不要 */
//	private Integer parentId;
	/** 階層の深さ */
	private Integer depth;
	/** 大、中、小カテゴリ名 〇を1つずつ取得したもの */
	private String name;

	/** カテゴリ名総称 〇/〇/〇 不要 */
//	private String nameAll;

	public Category() {

	}

	public Category(Integer id, Integer depth, String name) {
		super();
		this.id = id;
		this.depth = depth;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", depth=" + depth + ", name=" + name + "]";
	}

}
