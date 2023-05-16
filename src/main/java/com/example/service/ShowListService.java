package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Category;
import com.example.domain.Item;
import com.example.form.SearchItemForm;
import com.example.repository.CategoryRepository;
import com.example.repository.ItemRepository;

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
		// ページのオフセット値の計算
		Integer offset = 30 * (page - 1);
		List<Item> itemList = itemRepository.findAll(offset);
		itemList = setCategoryList(itemList);
		return itemList;
	}

	// 商品数計算(全件数)
	public Integer countItems() {
		return itemRepository.countItems();
	}

	// 階層ごとのカテゴリー情報取得
	public List<Category> pickUpCategoryListByDepth(Integer depth) {
		List<Category> categoryList = categoryRepository.findByDepth(depth);
		if (categoryList.size() == 0) {
			throw new RuntimeException("categoriesテーブルに該当カテゴリが存在しません。");
		}
		return categoryList;
	}

	public List<Category> pickUpCategoryListByChildId(Integer childId) {
		List<Category> categoryList = categoryRepository.findByChildId(childId);
		return categoryList;
	}

	public List<Category> pickUpCategoryListByParentIdAndDepth(Integer parentId, Integer depth) {
		List<Category> categoryList = categoryRepository.findByParentIdAndDepth(parentId, depth);
		return categoryList;
	}

	/**
	 * 商品を検索する.
	 * 
	 * @param form 入力フォーム(親子孫id)
	 * @param page ページ
	 * @return 検索された商品
	 */
	public List<Item> showListByForm(SearchItemForm form, Integer page) {
		Integer offset = 30 * (page - 1);
		String name = form.getName();
		String brand = form.getBrand();
		Integer id = null;
		if (Integer.parseInt(form.getGrandChildId()) > 0) {
			id = Integer.parseInt(form.getGrandChildId());
		} else if (Integer.parseInt(form.getChildId()) > 0) {
			id = Integer.parseInt(form.getChildId());
		} else if (Integer.parseInt(form.getParentId()) > 0) {
			id = Integer.parseInt(form.getParentId());
		}
		List<Item> itemList = itemRepository.findByForm(name, id, brand, offset);
		itemList = setCategoryList(itemList);
		return itemList;
	}

	/**
	 * 商品数を計算する.
	 * 
	 * @param form 入力フォーム
	 * @return 検索された商品数.
	 */
	public Integer countByForm(SearchItemForm form) {
		String name = form.getName();
		String brand = form.getBrand();
		Integer id = null;
		if (Integer.parseInt(form.getGrandChildId()) > 0) {
			id = Integer.parseInt(form.getGrandChildId());
		} else if (Integer.parseInt(form.getChildId()) > 0) {
			id = Integer.parseInt(form.getChildId());
		} else if (Integer.parseInt(form.getParentId()) > 0) {
			id = Integer.parseInt(form.getParentId());
		}
		Integer count = itemRepository.countByForm(name, id, brand);
		return count;
	}

	// itemドメインのcategoryListにつめる
	public List<Item> setCategoryList(List<Item> itemList) {
		for (Item item : itemList) {
			Integer categoryId = item.getCategoryId();
			List<Category> categoryList = categoryRepository.findByChildId(categoryId);
			item.setCategoryList(categoryList);
		}
		return itemList;
	}
}
