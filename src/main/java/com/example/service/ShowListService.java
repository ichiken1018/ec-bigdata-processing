package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Category;
import com.example.domain.Item;
import com.example.domain.Page;
import com.example.form.SearchItemForm;
import com.example.repository.CategoryRepository;
import com.example.repository.ItemRepository;

/**
 * 商品一覧表示を操作するサービス.
 * 
 * @author kenta_ichiyoshi
 *
 */
@Service
@Transactional
public class ShowListService {
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * 商品一覧を表示する
	 * 
	 * @param page ページ数
	 * @param form 検索入力フォーム
	 * @return 検索された商品一覧
	 */
	public List<Item> showItemList(Integer page) {
		Integer itemsPerPage = Page.ITEMS_PER_PAGE.getValue();
		Integer offset = itemsPerPage * (page - 1);

		List<Item> itemList = itemRepository.findAll(offset);
		itemList = setCategoryList(itemList);

		return itemList;
	}

	/**
	 * 商品数を計算する.
	 * 
	 * @return 全商品数
	 */
	public Integer countItems() {
		return itemRepository.countItems();
	}

	/**
	 * 階層に紐付くカテゴリ情報を取得する.
	 * 
	 * @param depth 階層
	 * @return 検索されたカテゴリ情報
	 */
	public List<Category> pickUpCategoryListByDepth(Integer depth) {
		List<Category> categoryList = categoryRepository.findByDepth(depth);
		if (categoryList.size() == 0) {
			throw new RuntimeException("categoriesテーブルに該当カテゴリが存在しません。");
		}
		return categoryList;
	}

	/**
	 * 子idに紐付くカテゴリ情報を取得する.
	 * 
	 * @param childId 子id
	 * @return 検索されたカテゴリ情報
	 */
	public List<Category> pickUpCategoryListByChildId(Integer childId) {
		List<Category> categoryList = categoryRepository.findByChildId(childId);
		return categoryList;
	}

	/**
	 * 親idと階層に紐付くカテゴリ情報を取得する.
	 * 
	 * @param parentId 親id
	 * @param depth    階層
	 * @return 検索されたカテゴリ情報
	 */
	public List<Category> pickUpCategoryListByParentIdAndDepth(Integer parentId, Integer depth) {
		List<Category> categoryList = categoryRepository.findByParentIdAndDepth(parentId, depth);
		return categoryList;
	}

	/**
	 * 商品を検索する.
	 * 
	 * @param form フォーム
	 * @param page ページ
	 * @return 検索された商品
	 */
	public List<Item> showListByForm(SearchItemForm form, Integer page) {

		Integer itemsPerPage = Page.ITEMS_PER_PAGE.getValue();
		Integer offset = itemsPerPage * (page - 1);

		String itemName = form.getName();
		String itemBrand = form.getBrand();
		Integer categoryId = determineCategoryId(form);

		List<Item> itemList = itemRepository.findByForm(itemName, categoryId, itemBrand, offset);
		itemList = setCategoryList(itemList);
		return itemList;
	}

	/**
	 * 検索商品数を計算する.
	 * 
	 * @param form 入力フォーム
	 * @return 検索された商品数.
	 */
	public Integer countByForm(SearchItemForm form) {
		String itemName = form.getName();
		String itemBrand = form.getBrand();
		Integer categoryId = determineCategoryId(form);

		Integer count = itemRepository.countByForm(itemName, categoryId, itemBrand);

		return count;
	}

	/**
	 * itemドメインのcategoryListにカテゴリ情報を取得する.
	 * 
	 * @param itemList 商品リスト
	 * @return カテゴリ情報を取得した商品リスト
	 */
	private List<Item> setCategoryList(List<Item> itemList) {
		for (Item item : itemList) {
			Integer categoryId = item.getCategoryId();
			List<Category> categoryList = categoryRepository.findByChildId(categoryId);
			item.setCategoryList(categoryList);
		}
		return itemList;
	}

	/**
	 * カテゴリIdを特定する.
	 * 
	 * @param form 入力フォーム
	 * @return 入力フォームに紐付いたカテゴリId
	 */
	private Integer determineCategoryId(SearchItemForm form) {
		Integer parentId = Integer.parseInt(form.getParentId());
		Integer childId = Integer.parseInt(form.getChildId());
		Integer grandChildId = Integer.parseInt(form.getGrandChildId());

		Integer categoryId = null;
		if (grandChildId > 0) {
			categoryId = grandChildId;
		} else if (childId > 0) {
			categoryId = childId;
		} else if (parentId > 0) {
			categoryId = parentId;
		}

		return categoryId;
	}
}
