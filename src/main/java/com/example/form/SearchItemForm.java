package com.example.form;

/**
 * 検索入力値を保持するフォーム
 * 
 * @author kenta_ichiyoshi
 *
 */
public class SearchItemForm {

	/** 商品名 */
	private String name;

	/** 親カテゴリ */
	private String parentId;

	/** 子カテゴリ */
	private String childId;

	/** 孫カテゴリ */
	private String grandChildId;

	/** ブランド名 */
	private String brand;

	public SearchItemForm() {

	}

	public SearchItemForm(String name, String parentId, String childId, String grandChildId, String brand) {
		super();
		this.name = name;
		this.parentId = parentId;
		this.childId = childId;
		this.grandChildId = grandChildId;
		this.brand = brand;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getChildId() {
		return childId;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

	public String getGrandChildId() {
		return grandChildId;
	}

	public void setGrandChildId(String grandChildId) {
		this.grandChildId = grandChildId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Override
	public String toString() {
		return "SearchItemForm [name=" + name + ", parentId=" + parentId + ", childId=" + childId + ", grandChildId="
				+ grandChildId + ", brand=" + brand + "]";
	}

}
