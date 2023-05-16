package com.example.domain;

import java.util.List;

/**
 * itemのドメイン.
 * 
 * @author kenta_ichiyoshi
 *
 */
public class Item {
	/** 商品ID */
	private Integer itemId;
	/** 商品名 */
	private String name;
	/** 商品状態 */
	private Integer condition;
	/** カテゴリID */
	private Integer categoryId;
	/** ブランド名 */
	private String brand;
	/** 値段 */
	private double price;
	/** 発送方法 */
	private Integer shipping;
	/** 商品説明 */
	private String description;
	/** カテゴリドメイン情報をつめたリスト */
	private List<Category> categoryList;

	// テーブル結合後に必要になるカラム
	/** テーブル結合後のカテゴリ名 */
	private String categoryNameAll;

	public Item() {
	}

	public Item(Integer itemId, String name, Integer condition, Integer categoryId, String brand, double price,
			Integer shipping, String description, List<Category> categoryList, String categoryNameAll) {
		super();
		this.itemId = itemId;
		this.name = name;
		this.condition = condition;
		this.categoryId = categoryId;
		this.brand = brand;
		this.price = price;
		this.shipping = shipping;
		this.description = description;
		this.categoryList = categoryList;
		this.categoryNameAll = categoryNameAll;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCondition() {
		return condition;
	}

	public void setCondition(Integer condition) {
		this.condition = condition;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Integer getShipping() {
		return shipping;
	}

	public void setShipping(Integer shipping) {
		this.shipping = shipping;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public String getCategoryNameAll() {
		return categoryNameAll;
	}

	public void setCategoryNameAll(String categoryNameAll) {
		this.categoryNameAll = categoryNameAll;
	}

	@Override
	public String toString() {
		return "Item [itemId=" + itemId + ", name=" + name + ", condition=" + condition + ", categoryId=" + categoryId
				+ ", brand=" + brand + ", price=" + price + ", shipping=" + shipping + ", description=" + description
				+ ", categoryList=" + categoryList + ", categoryNameAll=" + categoryNameAll + "]";
	}

}
