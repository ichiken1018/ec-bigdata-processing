package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Category;
import com.example.domain.Item;
import com.example.repository.CategoryRepository;
import com.example.repository.ItemRepository;

/**
 * 商品詳細を操作するサービス.
 * 
 * @author kenta_ichiyoshi
 *
 */
@Service
@Transactional
public class ShowDetailService {

	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * 詳細画面を表示する.
	 * 
	 * @param itemId 商品id
	 * @return 検索された商品詳細情報
	 */
	public Item showItemDetail(Integer itemId) {
		Item item = itemRepository.load(itemId);
		List<Category> categoryList = categoryRepository.findByChildId(item.getCategoryId());
		item.setCategoryList(categoryList);
		StringBuilder nameAll = new StringBuilder();
		boolean isFirstCategory = true;
		for (Category category : categoryList) {
			if (isFirstCategory) {
				nameAll.append(category.getName());
				isFirstCategory = false;
			} else {
				nameAll.append("/").append(category.getName());
			}
		}
		item.setCategoryNameAll(nameAll.toString());
		return item;
	}
}
